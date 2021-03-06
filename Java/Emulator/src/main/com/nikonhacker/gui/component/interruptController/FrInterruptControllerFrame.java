package com.nikonhacker.gui.component.interruptController;

import com.nikonhacker.Constants;
import com.nikonhacker.Format;
import com.nikonhacker.emu.interrupt.fr.FrInterruptRequest;
import com.nikonhacker.emu.memory.Memory;
import com.nikonhacker.emu.memory.listener.fr.ExpeedIoListener;
import com.nikonhacker.emu.peripherials.interruptController.InterruptController;
import com.nikonhacker.emu.peripherials.interruptController.fr.FrInterruptController;
import com.nikonhacker.gui.EmulatorUI;
import com.nikonhacker.gui.swing.JValueButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Vector;

/**
 * This component emulates a FR interrupt controller with manual operations
 * 
 * See http://mcu.emea.fujitsu.com/document/products_mcu/mb91350/documentation/ds91350a-ds07-16503-5e.pdf, p55-62
 */
public class FrInterruptControllerFrame extends InterruptControllerFrame {

    public FrInterruptControllerFrame(String title, String imageName, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable, int chip, final EmulatorUI ui, final InterruptController interruptController, final Memory memory) {
        super(title, imageName, resizable, closable, maximizable, iconifiable, chip, ui, interruptController, memory);
    }

    /**
     * Create and add the Interrupt Controller Frame tabs pertaining to the FR CPU
     * @param ui
     * @param interruptController
     * @param memory
     * @param buttonInsets
     * @param tabbedPane
     */
    protected void addTabs(final EmulatorUI ui, final InterruptController interruptController, final Memory memory, Insets buttonInsets, JTabbedPane tabbedPane) {
        // Standard button interrupt panel (only the ones defined as External + NMI)

        JPanel standardInterruptControllerPanel = new JPanel(new BorderLayout());

        ActionListener standardInterruptButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JValueButton button = (JValueButton) e.getSource();
                int interruptNumber = button.getValue();
                String interruptName;
                int icr;
                boolean isNMI;
                if (interruptNumber == 0xF) {
                    interruptName = "NMI";
                    icr = 0;
                    isNMI = true;
                }
                else {
                    int irNumber = interruptNumber - FrInterruptController.INTERRUPT_NUMBER_EXTERNAL_IR_OFFSET;
                    interruptName = "IR" + (irNumber < 10 ? "0" : "") + irNumber;
                    int icrAddress = irNumber + ExpeedIoListener.REGISTER_ICR00;
                    icr = memory.loadUnsigned8(icrAddress) & 0x1F;
                    isNMI = false;
                }
                FrInterruptRequest interruptRequest = new FrInterruptRequest(interruptNumber, isNMI, icr);
                if (interruptController.request(interruptRequest)) {
                    ui.setStatusText(Constants.CHIP_FR, interruptName + " (" +  interruptRequest + ") was requested.");
                }
                else {
                    ui.setStatusText(Constants.CHIP_FR, interruptName + " (" +  interruptRequest + ") was rejected (already requested).");
                }
            }
        };

        JPanel standardButtonGrid = new JPanel(new GridLayout(0,4));

        JValueButton nmiButton = createInterruptButton("INT 0x0F = NMI", 0x0F);
        nmiButton.setForeground(Color.RED);
        nmiButton.setMargin(buttonInsets);
        nmiButton.addActionListener(standardInterruptButtonListener);
        standardButtonGrid.add(nmiButton);

        for (int value = 0; value < 47; value++) {
            JValueButton button = createInterruptButton("INT 0x" + Format.asHex(value + FrInterruptController.INTERRUPT_NUMBER_EXTERNAL_IR_OFFSET, 2)
                    + " = IR" + (value < 10 ? "0" : "") + value, value + FrInterruptController.INTERRUPT_NUMBER_EXTERNAL_IR_OFFSET) ;
            button.setMargin(buttonInsets);
            button.addActionListener(standardInterruptButtonListener);
            standardButtonGrid.add(button);
        }

        standardInterruptControllerPanel.add(standardButtonGrid, BorderLayout.CENTER);

        tabbedPane.addTab("Standard", null, standardInterruptControllerPanel);

        // Custom 256 button panel

        JPanel customInterruptControllerPanel = new JPanel(new BorderLayout());

        JPanel topToolbar = new JPanel(new FlowLayout());
        topToolbar.add(new JLabel("ICR : "));
        Vector<String> labels = new Vector<String>();
        for (int i = 0; i < 32; i++) {
            labels.add(Format.asBinary(i, 5));
        }
        final JComboBox icrComboBox = new JComboBox(labels);
        topToolbar.add(icrComboBox);

        final JCheckBox nmiCheckBox = new JCheckBox("NMI");
        topToolbar.add(nmiCheckBox);

        customInterruptControllerPanel.add(topToolbar, BorderLayout.NORTH);

        ActionListener customInterruptButtonListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JValueButton button = (JValueButton) e.getSource();
                int interruptNumber = button.getValue();
                if (interruptController.request(new FrInterruptRequest(interruptNumber, nmiCheckBox.isSelected(), icrComboBox.getSelectedIndex()))) {
                    ui.setStatusText(Constants.CHIP_FR, "Interrupt 0x" + Format.asHex(interruptNumber, 2) + " was requested.");
                }
                else {
                    ui.setStatusText(Constants.CHIP_FR, "Interrupt 0x" + Format.asHex(interruptNumber, 2) + " was rejected (already requested).");
                }
            }
        };

        JPanel customButtonGrid = new JPanel(new GridLayout(16, 16));
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                final int value = i * 16 + j;
                JValueButton button = createInterruptButton(Format.asHex(value, 2), value);
                button.setMargin(buttonInsets);
                button.addActionListener(customInterruptButtonListener);
                customButtonGrid.add(button);
            }
        }

        customInterruptControllerPanel.add(customButtonGrid, BorderLayout.CENTER);

        tabbedPane.addTab("Custom", null, customInterruptControllerPanel);


        // Timer panel

        JPanel timerPanel = new JPanel(new BorderLayout());

        JPanel timerIntermediaryPanel = new JPanel(new FlowLayout());

        JPanel timerParamPanel = new JPanel(new GridLayout(0, 3));

        JLabel tmpLabel = new JLabel("Request INT");
        tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timerParamPanel.add(tmpLabel);
        Vector<String> timerInterruptNumber = new Vector<String>();
        for (int i = 0; i < 255; i++) {
            timerInterruptNumber.add("0x" + Format.asHex(i, 2));
        }
        final JComboBox timerInterruptComboBox = new JComboBox(timerInterruptNumber);
        timerParamPanel.add(timerInterruptComboBox);
        timerParamPanel.add(new JLabel(""));

        tmpLabel = new JLabel("with ICR : ");
        tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timerParamPanel.add(tmpLabel);
        Vector<String> timerIcrLabels = new Vector<String>();
        for (int i = 0; i < 32; i++) {
            timerIcrLabels.add(Format.asBinary(i, 5));
        }
        final JComboBox timerIcrComboBox = new JComboBox(timerIcrLabels);
        timerParamPanel.add(timerIcrComboBox);
        timerParamPanel.add(new JLabel(""));

        tmpLabel = new JLabel("NMI");
        tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timerParamPanel.add(tmpLabel);
        final JCheckBox timerNmiCheckBox = new JCheckBox();
        timerParamPanel.add(timerNmiCheckBox);
        timerParamPanel.add(new JLabel(""));

        tmpLabel = new JLabel("every ");
        tmpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        timerParamPanel.add(tmpLabel);
        Vector<String> intervals = new Vector<String>();
        intervals.add("1000");
        intervals.add("100");
        intervals.add("10");
        intervals.add("1");
        final JComboBox intervalsComboBox = new JComboBox(intervals);
        timerParamPanel.add(intervalsComboBox);
        timerParamPanel.add(new JLabel("ms"));

        timerParamPanel.add(new JLabel(""));

        JButton startTimerButton = new JButton("Start");
        startTimerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (interruptTimer == null) {
                    startTimer(timerInterruptComboBox.getSelectedIndex(), timerNmiCheckBox.isSelected(), timerIcrComboBox.getSelectedIndex(), Integer.parseInt((String) intervalsComboBox.getSelectedItem()));
                    ((JButton) e.getSource()).setText("Stop");
                }
                else {
                    stopTimer();
                    ((JButton) e.getSource()).setText("Start");
                }
            }
        });
        timerParamPanel.add(startTimerButton);

        timerIntermediaryPanel.add(timerParamPanel);

        timerPanel.add(timerIntermediaryPanel,BorderLayout.CENTER);

        tabbedPane.addTab("Timer", null, timerPanel);
    }

    protected int getStatusTabIndex() {
        return 3;
    }

    private JValueButton createInterruptButton(String text, int interruptNumber) {
        JValueButton button = new JValueButton(text, interruptNumber);

        // Set custom Tooltip
        String tooltip = "INT 0x" + Format.asHex(interruptNumber,2);
        switch (interruptNumber) {
            case 0x00:
                button.setForeground(Color.RED.darker());
                tooltip = "Reset";
                break;
            case 0x01:
                button.setForeground(Color.ORANGE.darker());
                tooltip = "System reserved or Mode Vector";
                break;
            case 0x02:
            case 0x03:
            case 0x04:
            case 0x05:
            case 0x06:
                button.setForeground(Color.ORANGE.darker());
                tooltip = "System reserved";
                break;
            case 0x09:
                button.setForeground(Color.BLUE);
                tooltip = "Emulator Exception";
                break;
            case 0x0A:
                button.setForeground(Color.BLUE);
                tooltip = "Instruction break trap";
                break;
            case 0x0B:
                button.setForeground(Color.BLUE);
                tooltip = "Operand break trap";
                break;
            case 0x0C:
                button.setForeground(Color.BLUE);
                tooltip = "Step trace break trap";
                break;
            case 0x0D:
                button.setForeground(Color.BLUE);
                tooltip = "Emulator exception";
                break;
            case 0x0E:
                button.setForeground(Color.BLUE);
                tooltip = "Undefined instruction exception";
                break;
            case 0x0F:
                tooltip += " or NMI";
                break;
            case 0x40:
            case 0x41:
                button.setForeground(Color.ORANGE.darker());
                tooltip = "System reserved";
                break;
        }

        if (interruptNumber >= 0x10 && interruptNumber <= 0x2F) {
            int irNumber = interruptNumber - FrInterruptController.INTERRUPT_NUMBER_EXTERNAL_IR_OFFSET;
            tooltip += " or IR" + (irNumber <10?"0":"") + irNumber;
        }

        button.setToolTipText(tooltip);

        return button;
    }

    protected void startTimer(final int interruptNumber, final boolean isNmi, final int icr, int interval) {
        interruptTimer = new java.util.Timer(false);
        interruptTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                interruptController.request(new FrInterruptRequest(interruptNumber, isNmi, icr));
            }
        }, 0, interval);
        ui.setStatusText(Constants.CHIP_FR, "Interrupt 0x" + Format.asHex(interruptNumber, 2) + " will be requested every " + interval + "ms");
    }

    protected void stopTimer() {
        if (interruptTimer != null) {
            interruptTimer.cancel();
            interruptTimer = null;
        }
        ui.setStatusText(Constants.CHIP_FR, "Stopped interrupt timer");
    }
}
