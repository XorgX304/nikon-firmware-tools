﻿namespace Nikon_Patch
{
    class D600_0101 : Firmware
    {

        Patch[] patch_1080_36mbps = {
            new Patch(1, 0x23B4E, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B54, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B7A, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B80, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23BC6, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23BCC, new byte[] { 0x01 } , new byte[] { 0x02 } ),
                                  };

        Patch[] patch_1080_54mbps = {
            new Patch(1, 0x23B4E, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B54, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B7A, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B80, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23BC6, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23BCC, new byte[] { 0x01 } , new byte[] { 0x03 } ),
                                  };

        Patch[] patch_1080_36mbps_NQ = {
            new Patch(1, 0x23B4E, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B54, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B62, new byte[] { 0x00, 0xB7, 0x1B } , new byte[] { 0x01, 0x6E, 0x36 } ),
            new Patch(1, 0x23B68, new byte[] { 0x00, 0x98, 0x96, 0x80 } , new byte[] { 0x01, 0x31, 0x2D, 0x00 } ),

            new Patch(1, 0x23B7A, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B80, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23B8E, new byte[] { 0x00, 0xB7, 0x1B } , new byte[] { 0x01, 0x6E, 0x36 } ),
            new Patch(1, 0x23B94, new byte[] { 0x00, 0x98, 0x96, 0x80 } , new byte[] { 0x01, 0x31, 0x2D, 0x00 } ),

            new Patch(1, 0x23BC6, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23BCC, new byte[] { 0x01 } , new byte[] { 0x02 } ),
            new Patch(1, 0x23BDA, new byte[] { 0x00, 0xB7, 0x1B } , new byte[] { 0x01, 0x6E, 0x36 } ),
            new Patch(1, 0x23BE0, new byte[] { 0x00, 0x98, 0x96, 0x80 } , new byte[] { 0x01, 0x31, 0x2D, 0x00 } ),
                                  };

        Patch[] patch_1080_54mbps_NQ = {
            new Patch(1, 0x23B4E, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B54, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B62, new byte[] { 0x00, 0xB7, 0x1B } , new byte[] { 0x01, 0x6E, 0x36 } ),
            new Patch(1, 0x23B68, new byte[] { 0x00, 0x98, 0x96, 0x80 } , new byte[] { 0x01, 0x31, 0x2D, 0x00 } ),

            new Patch(1, 0x23B7A, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B80, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23B8E, new byte[] { 0x00, 0xB7, 0x1B } , new byte[] { 0x01, 0x6E, 0x36 } ),
            new Patch(1, 0x23B94, new byte[] { 0x00, 0x98, 0x96, 0x80 } , new byte[] { 0x01, 0x31, 0x2D, 0x00 } ),

            new Patch(1, 0x23BC6, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23BCC, new byte[] { 0x01 } , new byte[] { 0x03 } ),
            new Patch(1, 0x23BDA, new byte[] { 0x00, 0xB7, 0x1B } , new byte[] { 0x01, 0x6E, 0x36 } ),
            new Patch(1, 0x23BE0, new byte[] { 0x00, 0x98, 0x96, 0x80 } , new byte[] { 0x01, 0x31, 0x2D, 0x00 } ),
                                  };

        Patch[] patch_1080_64mbps_NQ = {
            new Patch(1, 0x23B4E, Sys.mbps24 , Sys.mbps64 ),
            new Patch(1, 0x23B54, Sys.mbps20 , Sys.mbps60 ),
            new Patch(1, 0x23B62, Sys.mbps12 , Sys.mbps24 ),
            new Patch(1, 0x23B68, Sys.mbps10 , Sys.mbps20 ),

            new Patch(1, 0x23B7A, Sys.mbps24 , Sys.mbps64 ),
            new Patch(1, 0x23B80, Sys.mbps20 , Sys.mbps60 ),
            new Patch(1, 0x23B8E, Sys.mbps12 , Sys.mbps24 ),
            new Patch(1, 0x23B94, Sys.mbps10 , Sys.mbps20 ),

            new Patch(1, 0x23BC6, Sys.mbps24 , Sys.mbps64 ),
            new Patch(1, 0x23BCC, Sys.mbps20 , Sys.mbps60 ),
            new Patch(1, 0x23BDA, Sys.mbps12 , Sys.mbps24 ),
            new Patch(1, 0x23BE0, Sys.mbps10 , Sys.mbps20 ),
        };

        Patch[] patch_Language_Fix = {
            new Patch(1, 0x38063C, new byte[] { 0xE2, 0x08 }, new byte[] { 0xE0, 0x08 }),
            new Patch(1, 0x385B3C, new byte[] { 0xE2, 0x11 }, new byte[] { 0xE0, 0x11 }),
            new Patch(1, 0x41EC34, new byte[] { 0xE2, 0x06 }, new byte[] { 0xE0, 0x06 }),
            //new Patch(1, 0x3E320C, new byte[] { 0xB1, 0xF4 }, new byte[] { 0xC0, 0x04 }), this case (from 3200) was not found
                          };


		Patch[] patch_dark_current_menu = {
			new Patch(1, 0x423CF2, new byte[] { 0x9E, 0x45, 0x3E, 0x1C, 0x97, 0x94, 0xC0, 0x1D, 0x82, 0x4D, 0xE2, 0x02, 0x91, 0x40, 0xE0, 0x01, 0x81, 0xB0 } , new byte[] { 0x00, 0x54, 0x00, 0x00, 0x97, 0x00, 0x9F, 0xA0, 0x9F, 0xA0, 0x9F, 0xA0, 0x9F, 0xA0, 0x9F, 0xA0, 0x9F, 0xA0 } ),
			new Patch(1, 0x500000, new byte[] { 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF } , new byte[] { 0x9F, 0x8D, 0x9F, 0x06, 0xB1, 0x74, 0x9B, 0x00, 0x4E, 0x5A, 0xC0, 0x04, 0x11, 0x04, 0xA4, 0x40, 0x11, 0x04, 0x9B, 0x00, 0x4F, 0x5E, 0x11, 0x04, 0xA4, 0x40, 0x11, 0x04, 0x9B, 0x00, 0x50, 0x62, 0x11, 0x04, 0xA4, 0x40, 0x11, 0x04, 0x9B, 0x04, 0x80, 0x00, 0x9B, 0x00, 0x05, 0xEA, 0x11, 0x04, 0x9B, 0x00, 0x06, 0x1E, 0x11, 0x04, 0x9B, 0x00, 0x06, 0x52, 0x11, 0x04, 0x9F, 0x8D, 0x9E, 0x45, 0x3E, 0xB1, 0x91, 0x2D, 0x9F, 0x80, 0x00, 0x46, 0x3D, 0x00, 0x97, 0x00 } ),
			new Patch(1, 0x69B290, new byte[] { 0x4A, 0x50, 0x45, 0x47, 0x20, 0x63, 0x6F, 0x6D, 0x70, 0x72, 0x65, 0x73, 0x73, 0x69, 0x6F, 0x6E } , new byte[] { 0x41, 0x73, 0x74, 0x72, 0x6F, 0x70, 0x68, 0x6F, 0x74, 0x6F, 0x67, 0x72, 0x61, 0x70, 0x68, 0x79 } ),
			new Patch(1, 0x69B2AD, new byte[] { 0x4F, 0x70, 0x74, 0x69, 0x6D, 0x61, 0x6C, 0x20, 0x71, 0x75, 0x61, 0x6C, 0x69, 0x74, 0x79 } , new byte[] { 0x53, 0x65, 0x6E, 0x73, 0x6F, 0x72, 0x20, 0x52, 0x41, 0x57, 0x20, 0x4F, 0x6E, 0x21, 0x20 } ),
			new Patch(1, 0x69C82F, new byte[] { 0x4A, 0x50, 0x45, 0x47, 0x20, 0x63, 0x6F, 0x6D, 0x70, 0x72, 0x65, 0x73, 0x73, 0x69, 0x6F, 0x6E } , new byte[] { 0x41, 0x73, 0x74, 0x72, 0x6F, 0x70, 0x68, 0x6F, 0x74, 0x6F, 0x67, 0x72, 0x61, 0x70, 0x68, 0x79 } ),
			new Patch(1, 0x6A3539, new byte[] { 0x43, 0x68, 0x6F, 0x6F, 0x73, 0x65, 0x20, 0x68, 0x6F, 0x77, 0x20, 0x4A, 0x50, 0x45, 0x47, 0x20, 0x69, 0x6D, 0x61, 0x67, 0x65, 0x73, 0x20, 0x28, 0x66, 0x69, 0x6E, 0x65, 0x2C, 0x20, 0x6E, 0x6F, 0x72 } , new byte[] { 0x54, 0x6F, 0x20, 0x72, 0x65, 0x63, 0x6F, 0x72, 0x64, 0x20, 0x75, 0x6E, 0x63, 0x6F, 0x6F, 0x6B, 0x65, 0x64, 0x20, 0x52, 0x41, 0x57, 0x20, 0x66, 0x72, 0x6F, 0x6D, 0x20, 0x74, 0x68, 0x65, 0x20, 0x69 } ),
			new Patch(1, 0x6A355C, new byte[] { 0x6C, 0x2C, 0x0A, 0x61, 0x6E, 0x64, 0x20, 0x62, 0x61, 0x73, 0x69, 0x63, 0x29, 0x20, 0x77, 0x69, 0x6C, 0x6C, 0x20, 0x62, 0x65, 0x20, 0x63, 0x6F, 0x6D, 0x70, 0x72, 0x65, 0x73, 0x73, 0x65, 0x64, 0x2E } , new byte[] { 0x67, 0x65, 0x0A, 0x73, 0x65, 0x6E, 0x73, 0x6F, 0x72, 0x20, 0x66, 0x6F, 0x72, 0x20, 0x61, 0x73, 0x74, 0x72, 0x6F, 0x70, 0x68, 0x6F, 0x74, 0x6F, 0x67, 0x72, 0x61, 0x70, 0x68, 0x79, 0x2E, 0x20, 0x20 } ),
			new Patch(1, 0x6A358D, new byte[] { 0x41, 0x6C, 0x6C, 0x20, 0x69, 0x6D, 0x61, 0x67, 0x65, 0x73, 0x20, 0x61 } , new byte[] { 0x53, 0x65, 0x74, 0x74, 0x69, 0x6E, 0x67, 0x20, 0x62, 0x65, 0x66, 0x6F } ),
			new Patch(1, 0x6A359C, new byte[] { 0x63, 0x6F, 0x6D, 0x70, 0x72, 0x65, 0x73, 0x73, 0x65, 0x64, 0x0A, 0x74, 0x6F, 0x20, 0x61, 0x72, 0x6F, 0x75, 0x6E, 0x64, 0x20, 0x74, 0x68, 0x65, 0x20, 0x73, 0x61, 0x6D, 0x65, 0x20, 0x66, 0x69, 0x6C, 0x65, 0x20, 0x73, 0x69, 0x7A, 0x65, 0x2E, 0x02, 0x4F, 0x70, 0x74, 0x69, 0x6D, 0x61, 0x6C, 0x20, 0x71, 0x75, 0x61, 0x6C, 0x69, 0x74, 0x79, 0x3A, 0x20, 0x50, 0x72, 0x69, 0x6F, 0x72, 0x69, 0x74, 0x79, 0x20, 0x69, 0x73, 0x20, 0x67, 0x69, 0x76 } , new byte[] { 0x66, 0x69, 0x72, 0x6D, 0x77, 0x61, 0x72, 0x65, 0x0A, 0x75, 0x70, 0x64, 0x61, 0x74, 0x65, 0x20, 0x69, 0x73, 0x20, 0x6B, 0x65, 0x70, 0x74, 0x20, 0x75, 0x6E, 0x63, 0x68, 0x61, 0x6E, 0x67, 0x65, 0x64, 0x2E, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x02, 0x53, 0x65, 0x6E, 0x73, 0x6F, 0x72, 0x20, 0x52, 0x41, 0x57, 0x3A, 0x20, 0x54, 0x65, 0x6D, 0x70, 0x6F, 0x72, 0x61, 0x72, 0x69, 0x6C, 0x79, 0x20, 0x65, 0x6E, 0x61, 0x62, 0x6C, 0x65, 0x20, 0x73 } ),
			new Patch(1, 0x6A35E7, new byte[] { 0x20, 0x74, 0x6F, 0x0A, 0x69, 0x6D, 0x61, 0x67, 0x65, 0x20, 0x71, 0x75, 0x61, 0x6C, 0x69, 0x74, 0x79, 0x2E, 0x20, 0x46, 0x69, 0x6C, 0x65, 0x20, 0x73, 0x69, 0x7A, 0x65, 0x73, 0x20, 0x6D, 0x61, 0x79, 0x20, 0x64, 0x69, 0x66, 0x66, 0x65, 0x72, 0x2E } , new byte[] { 0x73, 0x6F, 0x72, 0x0A, 0x52, 0x41, 0x57, 0x2E, 0x20, 0x50, 0x6F, 0x77, 0x65, 0x72, 0x2F, 0x4D, 0x65, 0x74, 0x65, 0x72, 0x69, 0x6E, 0x67, 0x20, 0x6F, 0x66, 0x66, 0x20, 0x74, 0x6F, 0x20, 0x72, 0x65, 0x73, 0x65, 0x74, 0x2E, 0x20, 0x20, 0x20, 0x20 } ),
		};


        public D600_0101()
        {
            p = new Package();
            Model = "D600";
            Version = "1.01";

            Patches.Add(new PatchSet(PatchLevel.Released, "Multi-Language Support", patch_Language_Fix));
            Patches.Add(new PatchSet(PatchLevel.Released, "Video 1080 HQ 36mbps Bit-rate", patch_1080_36mbps, patch_1080_54mbps, patch_1080_36mbps_NQ, patch_1080_54mbps_NQ, patch_1080_64mbps_NQ));
            Patches.Add(new PatchSet(PatchLevel.Released, "Video 1080 HQ 54mbps Bit-rate", patch_1080_54mbps, patch_1080_36mbps, patch_1080_36mbps_NQ, patch_1080_54mbps_NQ, patch_1080_64mbps_NQ));
            Patches.Add(new PatchSet(PatchLevel.Released, "Video 1080 HQ 36mbps Bit-rate NQ old HQ", patch_1080_36mbps_NQ, patch_1080_36mbps, patch_1080_54mbps, patch_1080_54mbps_NQ, patch_1080_64mbps_NQ));
            Patches.Add(new PatchSet(PatchLevel.Released, "Video 1080 HQ 54mbps Bit-rate NQ old HQ", patch_1080_54mbps_NQ, patch_1080_36mbps, patch_1080_54mbps, patch_1080_36mbps_NQ, patch_1080_64mbps_NQ));
            Patches.Add(new PatchSet(PatchLevel.Released, "Video 1080 HQ 64mbps Bit-rate NQ old HQ", patch_1080_64mbps_NQ, patch_1080_36mbps, patch_1080_54mbps, patch_1080_36mbps_NQ, patch_1080_54mbps_NQ));
			Patches.Add(new PatchSet(PatchLevel.Beta, "True Dark Current - Menu based", patch_dark_current_menu));
		}
    }

    class D600_0102 : Firmware
    {
        Patch[] patch_Language_Fix = {
            //new Patch(1, 0x38063C, new byte[] { 0xE2, 0x08 }, new byte[] { 0xE0, 0x08 }),
            //new Patch(1, 0x385B3C, new byte[] { 0xE2, 0x11 }, new byte[] { 0xE0, 0x11 }),
            //new Patch(1, 0x41EC34, new byte[] { 0xE2, 0x06 }, new byte[] { 0xE0, 0x06 }),
            //new Patch(1, 0x3E320C, new byte[] { 0xB1, 0xF4 }, new byte[] { 0xC0, 0x04 }), this case (from 3200) was not found
                          };


        public D600_0102()
        {
            p = new Package();
            Model = "D600";
            Version = "1.02";

            //Patches.Add(new PatchSet(PatchLevel.DevOnly, "Multi-Language Support", patch_Language_Fix));
        }
    }
}