package com.nikonhacker.emu.memory.listener.fr;

import com.nikonhacker.emu.Platform;
import com.nikonhacker.emu.memory.DebuggableMemory;
import com.nikonhacker.emu.memory.listener.IoActivityListener;
import com.nikonhacker.emu.peripherials.interruptController.fr.FrInterruptController;
import com.nikonhacker.emu.peripherials.programmableTimer.fr.FrFreeRunTimer;
import com.nikonhacker.emu.peripherials.programmableTimer.fr.FrReloadTimer;
import com.nikonhacker.emu.peripherials.serialInterface.fr.FrSerialInterface;

public class ExpeedIoListener extends IoActivityListener {

    private static final int REGISTER_DICR = 0x44;

    // Timer
    public static final  int NUM_TIMER        = 3;
    private static final int REGISTER_TMRLRA0 = 0x48;
    private static final int REGISTER_TMR0    = 0x4A;
    private static final int REGISTER_TMCSR0  = 0x4E;

    private static final int REGISTER_TMRLRA1 = REGISTER_TMRLRA0 + 0x8;
    private static final int REGISTER_TMR1    = REGISTER_TMR0 + 0x8;
    private static final int REGISTER_TMCSR1  = REGISTER_TMCSR0 + 0x8;

    private static final int REGISTER_TMRLRA2 = REGISTER_TMRLRA0 + 0x10;
    private static final int REGISTER_TMR2    = REGISTER_TMR0 + 0x10;
    private static final int REGISTER_TMCSR2  = REGISTER_TMCSR0 + 0x10;

    // 32-bit FreeRun Timer
    public static final  int NUM_FREERUN_TIMER= 2;
    private static final int REGISTER_CPCLR0  = 0x100;
    private static final int REGISTER_TCDT0   = REGISTER_CPCLR0 + 0x8;
    private static final int REGISTER_TCCS0   = REGISTER_CPCLR0 + 0xE;

    private static final int REGISTER_CPCLR1  = REGISTER_CPCLR0 + 0x30;
    private static final int REGISTER_TCDT1   = REGISTER_CPCLR1 + 0x8;
    private static final int REGISTER_TCCS1   = REGISTER_CPCLR1 + 0xE;

    // Serial ports
    /**
     *  Three serial registers have been spotted, configured at 0x60, 0x70 and 0xB0
     *  Let's assume the SerialInterface at 0x60 is the first in the Expeed, and that there are 6 serial interfaces
     *  (60, 70, 80, 90, A0, B0). This is pure speculation of course.
     */
    public static final  int NUM_SERIAL_IF         = 6;
    private static final int SERIAL_IF_OFFSET_BITS = 4;
    private static final int SERIAL_IF_OFFSET      = 1 << SERIAL_IF_OFFSET_BITS;
    private static final int REGISTER_SCR_IBRC0    = 0x60;
    private static final int REGISTER_SMR0         = 0x61;
    private static final int REGISTER_SSR0         = 0x62;
    private static final int REGISTER_ESCR_IBSR0   = 0x63;
    private static final int REGISTER_RDR_TDR0     = 0x64;
    private static final int REGISTER_BGR10        = 0x66;
    private static final int REGISTER_BGR00        = 0x67;
    private static final int REGISTER_ISMK0        = 0x68;
    private static final int REGISTER_ISBA0        = 0x69;
    private static final int REGISTER_FCR10        = 0x6C;
    private static final int REGISTER_FCR00        = 0x6D;
    private static final int REGISTER_FBYTE20      = 0x6E;
    private static final int REGISTER_FBYTE10      = 0x6F;

    // Interrupt controller
    public static final int REGISTER_ICR00 = 0x440;
    public static final int REGISTER_EIRR0 = 0x40;
    public static final int REGISTER_ENIR0 = 0x41;
    public static final int REGISTER_ELVR0 = 0x42;


    private final Platform platform;
    protected boolean logRegisterMessages;

    public ExpeedIoListener(Platform platform, boolean logRegisterMessages) {
        this.platform = platform;
        this.logRegisterMessages = logRegisterMessages;
    }

    @Override
    public boolean matches(int address) {
        return address >>> 16 == 0x0000;
    }

    /**
     * Called when reading 8-bit value from register address range
     *
     * @param ioPage
     * @param addr
     * @param value
     * @param accessSource
     * @return value to be returned, or null to return previously written value like normal memory
     */
    public Byte onLoadData8(byte[] ioPage, int addr, byte value, DebuggableMemory.AccessSource accessSource) {
        // Serial Interface configuration registers
        if (addr >= REGISTER_SCR_IBRC0 && addr < REGISTER_SCR_IBRC0 + NUM_SERIAL_IF * SERIAL_IF_OFFSET) {
            int serialInterfaceNr = (addr - REGISTER_SCR_IBRC0) >> SERIAL_IF_OFFSET_BITS;
            FrSerialInterface serialInterface = (FrSerialInterface) platform.getSerialInterfaces()[serialInterfaceNr];
            switch (addr - (serialInterfaceNr << SERIAL_IF_OFFSET_BITS)) {
                case REGISTER_SCR_IBRC0:
                    return (byte)serialInterface.getScrIbcr();
                case REGISTER_SMR0:
                    return (byte)serialInterface.getSmr();
                case REGISTER_SSR0:
                    return (byte)serialInterface.getSsr();
                case REGISTER_ESCR_IBSR0:
                    return (byte)serialInterface.getEscrIbsr();
                case REGISTER_RDR_TDR0:   // written by 16-bit
                    throw new RuntimeException("Cannot read RDR register 8 bit at a time for now");
                case REGISTER_BGR10:      // read by 16-bit
                    return (byte)serialInterface.getBgr1();
                case REGISTER_BGR00:
                    return (byte)serialInterface.getBgr0();
                case REGISTER_ISMK0:
                    return (byte)serialInterface.getIsmk();
                case REGISTER_ISBA0:
                    return (byte)serialInterface.getIsba();
                case REGISTER_FCR10:
                    return (byte)serialInterface.getFcr1();
                case REGISTER_FCR00:
                    return (byte)serialInterface.getFcr0();
                case REGISTER_FBYTE20:    // read by 16-bit
                    return (byte)serialInterface.getFbyte2();
                case REGISTER_FBYTE10:
                    return (byte)serialInterface.getFbyte1();
            }
        }
        else if ((addr >= REGISTER_CPCLR0 && addr < (REGISTER_TCCS0 + 2)) || (addr >= REGISTER_CPCLR1 && addr < (REGISTER_TCCS1 + 2))) {
            // FreeRun timer
            throw new RuntimeException("The FreeRun timer registers cannot be accessed by 8-bit for now");
        }
        else if ((addr >= REGISTER_EIRR0) && (addr < (REGISTER_ELVR0 + 2))) {
            FrInterruptController interruptController = (FrInterruptController)platform.getInterruptController();
            switch (addr) {
                case REGISTER_EIRR0:
                    return (byte)interruptController.getEirr();
                case REGISTER_ENIR0:
                    return (byte)interruptController.getEnir();
                case REGISTER_ELVR0:
                    return (byte)(interruptController.getElvr() >> 8);
                case REGISTER_ELVR0+1:
                    return (byte)(interruptController.getElvr() & 0xFF);
            }
        }
        return null;
    }

    /**
     * Called when reading 16-bit value from register address range
     *
     *
     * @param ioPage
     * @param addr
     * @param value
     * @param accessSource
     * @return value to be returned, or null to return previously written value like normal memory
     */
    public Integer onLoadData16(byte[] ioPage, int addr, int value, DebuggableMemory.AccessSource accessSource) {
        // Serial Interface configuration registers
        if (addr >= REGISTER_SCR_IBRC0 && addr < REGISTER_SCR_IBRC0 + NUM_SERIAL_IF * SERIAL_IF_OFFSET) {
            int serialInterfaceNr = (addr - REGISTER_SCR_IBRC0) >> SERIAL_IF_OFFSET_BITS;
            FrSerialInterface serialInterface = (FrSerialInterface) platform.getSerialInterfaces()[serialInterfaceNr];
            switch (addr - (serialInterfaceNr << SERIAL_IF_OFFSET_BITS)) {
                case REGISTER_SCR_IBRC0:
                    return (serialInterface.getScrIbcr() << 8) | serialInterface.getSmr();
                case REGISTER_SSR0:
                    return (serialInterface.getSsr() << 8) | serialInterface.getEscrIbsr();
                case REGISTER_RDR_TDR0:
                    return serialInterface.getRdr();
                case REGISTER_BGR00:
                    return (serialInterface.getBgr1() << 8) | serialInterface.getBgr0();
                case REGISTER_ISMK0:
                    return (serialInterface.getIsmk() << 8) | serialInterface.getIsba();
                case REGISTER_FCR10:
                    return (serialInterface.getFcr1() << 8) | serialInterface.getFcr0();
                case REGISTER_FBYTE20:
                    return (serialInterface.getFbyte2() << 8) | serialInterface.getFbyte1();
            }
        }
        else if ((addr >= REGISTER_CPCLR0 && addr < REGISTER_TCCS0+2) || (addr >= REGISTER_CPCLR1 && addr < REGISTER_TCCS1+2)) {
            // FreeRun timer
            int channel;

            if (addr >= REGISTER_CPCLR1) {
                channel = NUM_TIMER + 1;
                addr -= (REGISTER_CPCLR1-REGISTER_CPCLR0);
            }
            else {
                channel = NUM_TIMER;
            }
            switch (addr) {
                case REGISTER_CPCLR0:
                    return (((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getCpclr() >> 16);
                case REGISTER_CPCLR0+2:
                    return (((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getCpclr() & 0xFFFF);
                case REGISTER_TCDT0:
                    return (((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getTcdt() >> 16);
                case REGISTER_TCDT0+2:
                    return (((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getTcdt() & 0xFFFF);
                case REGISTER_TCCS0:
                    return ((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getTccs();
                default:
                    throw new RuntimeException("Warning: ignoring attempt to read 16-bit register in FreeRun Timer.");
            }
        }
        else if (addr>=REGISTER_EIRR0 && addr<(REGISTER_ELVR0 + 2)) {
            FrInterruptController interruptController = (FrInterruptController)platform.getInterruptController();
            switch (addr) {
                case REGISTER_EIRR0:
                    return ((interruptController.getEirr() << 8) | interruptController.getEnir());
                case REGISTER_ELVR0:
                    return interruptController.getElvr();
            }
        }
        else {
            // Reload Timer configuration registers
            switch (addr) {
                case REGISTER_TMRLRA0:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[0]).getTmrlra();
                case REGISTER_TMR0:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[0]).getTmr();
                case REGISTER_TMCSR0:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[0]).getTmcsr();

                case REGISTER_TMRLRA1:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[1]).getTmrlra();
                case REGISTER_TMR1:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[1]).getTmr();
                case REGISTER_TMCSR1:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[1]).getTmcsr();

                case REGISTER_TMRLRA2:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[2]).getTmrlra();
                case REGISTER_TMR2:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[2]).getTmr();
                case REGISTER_TMCSR2:
                    return ((FrReloadTimer)platform.getProgrammableTimers()[2]).getTmcsr();
            }
        }
        return null;
    }

    /**
     * Called when reading 32-bit value from register address range
     *
     *
     * @param ioPage
     * @param addr
     * @param value
     * @param accessSource
     * @return value to be returned, or null to return previously written value like normal memory
     */
    public Integer onLoadData32(byte[] ioPage, int addr, int value, DebuggableMemory.AccessSource accessSource) {
        if ((addr >= REGISTER_CPCLR0 && addr < REGISTER_TCCS0+2) || (addr >= REGISTER_CPCLR1 && addr < REGISTER_TCCS1+2)) {
            // FreeRun timer
            int channel;

            if (addr >= REGISTER_CPCLR1) {
                channel = NUM_TIMER + 1;
                addr -= (REGISTER_CPCLR1-REGISTER_CPCLR0);
            }
            else {
                channel = NUM_TIMER;
            }
            switch (addr) {
                case REGISTER_CPCLR0:
                    return ((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getCpclr();
                case REGISTER_TCDT0:
                    return ((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).getTcdt();
                default:
                    throw new RuntimeException("Warning: ignoring attempt to write 32-bit register in FreeRun Timer.");
            }
        }
        else if (addr == REGISTER_EIRR0) {
            FrInterruptController interruptController = (FrInterruptController)platform.getInterruptController();
            return ((interruptController.getEirr() << 24) | (interruptController.getEnir() << 16) | interruptController.getElvr());
        }
        return null;
    }

    public void onStore8(byte[] ioPage, int addr, byte value, DebuggableMemory.AccessSource accessSource) {
        if (addr >= REGISTER_ICR00 && addr < REGISTER_ICR00 + 48) {
            // Interrupt request level registers
            ((FrInterruptController)platform.getInterruptController()).updateRequestICR(addr - REGISTER_ICR00, value);
        }
        else if (addr >= REGISTER_SCR_IBRC0 && addr < REGISTER_SCR_IBRC0 + NUM_SERIAL_IF * SERIAL_IF_OFFSET) {
            // Serial Interface configuration registers
            int serialInterfaceNr = (addr - REGISTER_SCR_IBRC0) >> SERIAL_IF_OFFSET_BITS;
            FrSerialInterface serialInterface = (FrSerialInterface) platform.getSerialInterfaces()[serialInterfaceNr];
            switch (addr - (serialInterfaceNr << SERIAL_IF_OFFSET_BITS)) {
                case REGISTER_SCR_IBRC0:   // written by 8-bit
                    serialInterface.setScrIbcr(value & 0xFF);
                    break;
                case REGISTER_SMR0:       // written by 8-bit
                    serialInterface.setSmr(value& 0xFF);
                    break;
                case REGISTER_SSR0:
                    serialInterface.setSsr(value & 0xFF);
                    break;
                case REGISTER_ESCR_IBSR0: // written by 8-bit
                    serialInterface.setEscrIbsr(value & 0xFF);
                    break;
                case REGISTER_RDR_TDR0:   // written by 16-bit
                    throw new RuntimeException("Cannot write TDR register 8 bit at a time for now");
                case REGISTER_BGR10:      // written by 16-bit
                    serialInterface.setBgr1(value & 0xFF);
                    break;
                case REGISTER_BGR00:
                    serialInterface.setBgr0(value & 0xFF);
                    break;
                case REGISTER_ISMK0:
                    serialInterface.setIsmk(value & 0xFF);
                    break;
                case REGISTER_ISBA0:
                    serialInterface.setIsba(value & 0xFF);
                    break;
                case REGISTER_FCR10:
                    serialInterface.setFcr1(value & 0xFF);
                    break;
                case REGISTER_FCR00:      // written by 8-bit
                    serialInterface.setFcr0(value & 0xFF);
                    break;
                case REGISTER_FBYTE20:    // written by 16-bit
                    serialInterface.setFbyte2(value & 0xFF);
                    break;
                case REGISTER_FBYTE10:
                    serialInterface.setFbyte1(value & 0xFF);
                    break;
            }
        }
        else if ((addr >= REGISTER_CPCLR0 && addr < REGISTER_TCCS0+2) || (addr >= REGISTER_CPCLR1 && addr < REGISTER_TCCS1+2)) {
            // FreeRun timer
            throw new RuntimeException("The FreeRun timer registers cannot be accessed by 8-bit for now");
        }
        else if (addr >= REGISTER_EIRR0 && addr < (REGISTER_ELVR0 + 2)) {
            FrInterruptController interruptController = (FrInterruptController)platform.getInterruptController();
            switch (addr) {
                case REGISTER_EIRR0:
                    interruptController.setEirr(value); break;
                case REGISTER_ENIR0:
                    interruptController.setEnir(value); break;
                case REGISTER_ELVR0:
                    interruptController.setElvrHi(value); break;
                case REGISTER_ELVR0+1:
                    interruptController.setElvrLo(value); break;
            }
        }
        else {
            switch (addr) {
                // Delay interrupt register
                case REGISTER_DICR:
                    if ((value & 0x1) == 0) {
                        platform.getInterruptController().removeRequest(FrInterruptController.DELAY_INTERRUPT_REQUEST_NR);
                    }
                    else {
                        platform.getInterruptController().request(FrInterruptController.DELAY_INTERRUPT_REQUEST_NR);
                    }
                    break;
            }
        }
        //System.out.println("Setting register 0x" + Format.asHex(offset, 4) + " to 0x" + Format.asHex(value, 2));
    }

    public void onStore16(byte[] ioPage, int addr, int value, DebuggableMemory.AccessSource accessSource) {
        // Serial Interface configuration registers
        if (addr >= REGISTER_SCR_IBRC0 && addr < REGISTER_SCR_IBRC0 + NUM_SERIAL_IF * SERIAL_IF_OFFSET) {
            int serialInterfaceNr = (addr - REGISTER_SCR_IBRC0) >> SERIAL_IF_OFFSET_BITS;
            FrSerialInterface serialInterface = (FrSerialInterface) platform.getSerialInterfaces()[serialInterfaceNr];
            switch (addr - (serialInterfaceNr << SERIAL_IF_OFFSET_BITS)) {
                case REGISTER_SCR_IBRC0:   // normally written by 8-bit
                    serialInterface.setScrIbcr((value >> 8) & 0xFF);
                    serialInterface.setSmr(value & 0xFF);
                    break;
                case REGISTER_SSR0:       // normally written by 8-bit
                    serialInterface.setSsr((value >> 8) & 0xFF);
                    serialInterface.setEscrIbsr(value & 0xFF);
                    break;
                case REGISTER_RDR_TDR0:   // 16-bit register
                    serialInterface.setTdr(value & 0xFFFF);
                    break;
                case REGISTER_BGR10:      // written by 16-bit
                    serialInterface.setBgr1((value >> 8) & 0xFF);
                    serialInterface.setBgr0(value & 0xFF);
                    break;
                case REGISTER_ISMK0:      // normally written by 8-bit
                    serialInterface.setIsmk((value >> 8) & 0xFF);
                    serialInterface.setIsba(value & 0xFF);
                    break;
                case REGISTER_FCR10:      // normally written by 8-bit
                    serialInterface.setFcr1((value >> 8) & 0xFF);
                    serialInterface.setFcr0(value & 0xFF);
                    break;
                case REGISTER_FBYTE20:    // written by 16-bit
                    serialInterface.setFbyte2((value >> 8) & 0xFF);
                    serialInterface.setFbyte1(value & 0xFF);
                    break;
            }
        }
        else if ((addr >= REGISTER_CPCLR0 && addr < REGISTER_TCCS0+2) || (addr >= REGISTER_CPCLR1 && addr < REGISTER_TCCS1+2)) {
            // FreeRun timer
            int channel;

            if (addr >= REGISTER_CPCLR1) {
                channel = NUM_TIMER + 1;
                addr -= (REGISTER_CPCLR1-REGISTER_CPCLR0);
            }
            else {
                channel = NUM_TIMER;
            }
            switch (addr) {
                case REGISTER_TCCS0:
                    ((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).setTccs(value); break;
                default:
                    throw new RuntimeException("Warning: ignoring attempt to write 16-bit register in FreeRun Timer.");
            }
        }
        else if (addr >= REGISTER_EIRR0 && addr < (REGISTER_ELVR0 + 2)) {
            FrInterruptController interruptController = (FrInterruptController)platform.getInterruptController();
            switch (addr) {
                case REGISTER_EIRR0:
                    interruptController.setEirr(value >> 8);
                    interruptController.setEnir(value & 0xFF);
                    break;
                case REGISTER_ELVR0:
                    interruptController.setElvr(value);
                    break;
            }
        }
        else {
            // Reload Timer configuration registers
            switch (addr) {
                case REGISTER_TMRLRA0:
                    ((FrReloadTimer)platform.getProgrammableTimers()[0]).setTmrlra(value & 0xFFFF);
                    break;
                case REGISTER_TMR0:
                    throw new RuntimeException("Warning: ignoring attempt to write reloadTimer0 value.");
                case REGISTER_TMCSR0:
                    ((FrReloadTimer)platform.getProgrammableTimers()[0]).setTmcsr(value & 0xFFFF);
                    break;

                case REGISTER_TMRLRA1:
                    ((FrReloadTimer)platform.getProgrammableTimers()[1]).setTmrlra(value & 0xFFFF);
                    break;
                case REGISTER_TMR1:
                    throw new RuntimeException("Warning: ignoring attempt to write reloadTimer1 value.");
                case REGISTER_TMCSR1:
                    ((FrReloadTimer)platform.getProgrammableTimers()[1]).setTmcsr(value & 0xFFFF);
                    break;

                case REGISTER_TMRLRA2:
                    ((FrReloadTimer)platform.getProgrammableTimers()[2]).setTmrlra(value & 0xFFFF);
                    break;
                case REGISTER_TMR2:
                    throw new RuntimeException("Warning: ignoring attempt to write reloadTimer2 value");
                case REGISTER_TMCSR2:
                    ((FrReloadTimer)platform.getProgrammableTimers()[2]).setTmcsr(value & 0xFFFF);
                    break;
            }
        }
    }

    public void onStore32(byte[] ioPage, int addr, int value, DebuggableMemory.AccessSource accessSource) {
        if ((addr >= REGISTER_CPCLR0 && addr < REGISTER_TCCS0+2) || (addr >= REGISTER_CPCLR1 && addr < REGISTER_TCCS1+2)) {
            // FreeRun timer
            int channel;

            if (addr >= REGISTER_CPCLR1) {
                channel = NUM_TIMER + 1;
                addr -= (REGISTER_CPCLR1-REGISTER_CPCLR0);
            }
            else {
                channel = NUM_TIMER;
            }
            switch (addr) {
                case REGISTER_CPCLR0:
                    ((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).setCpclr(value); break;
                case REGISTER_TCDT0:
                    ((FrFreeRunTimer)platform.getProgrammableTimers()[channel]).setTcdt(value); break;
                default:
                    throw new RuntimeException("Warning: ignoring attempt to write 32-bit register in FreeRun Timer.");
            }
        }
        else if (addr == REGISTER_EIRR0) {
            FrInterruptController interruptController = (FrInterruptController)platform.getInterruptController();
            interruptController.setEirr((value >> 24) & 0xFF);
            interruptController.setEnir((value >> 16) & 0xFF);
            interruptController.setElvr(value & 0xFFFF);
        }
    }
}
