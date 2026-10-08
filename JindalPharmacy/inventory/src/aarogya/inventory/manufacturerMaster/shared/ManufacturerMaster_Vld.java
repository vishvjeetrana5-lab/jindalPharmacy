package aarogya.inventory.manufacturerMaster.shared;

import dataman.dmBase.self.shared.DmEnum.DmPartyType;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ManufacturerMaster_Vld {

    public static void chkSave(ManufacturerMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Account name", 100);
        DmShared.isValidText(mrTemp.getDisplayName(), "Display name", 200);
        DmShared.isValidText(mrTemp.getAccGroup(), "A/c group", 300);
        DmShared.isValidText(mrTemp.getNatureCode(), "nature", 500);
        DmShared.isValidEMail(mrTemp.getEmail(), 400);
//        DmShared.isValidText(mrTemp.getPartyType(), "Party Type", 600);
        if (!DmShared.xNull(mrTemp.getPartyType()).trim().equalsIgnoreCase("")) {
            DmShared.isValidText(mrTemp.getCity(), "City", 550);
        }
        if (!DmShared.xNull(mrTemp.getPan()).trim().equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getPan()).length() != 10) {
                throw new DmException(1000, "PAN should be of 10(Ten) characters.");
            } else {
                DmShared.isValidPanNo(mrTemp.getPan(), 1000);
            }
        }
        if (DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmPartyType.Registered.getId()))
                || DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmPartyType.Composite.getId()))) {
            DmShared.isValidText(mrTemp.getGsTin(), "GSTIN No.", 700);
            DmShared.isValidText(mrTemp.getGsTINDate(), "GSTIN Date", 800);
        }
        if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getPan()).trim().equals("")) {
                throw new DmException(1000, "Please fill the PAN No.");
            } else {
                DmShared.isValidGSTIN(mrTemp.getStateNumericCode(), mrTemp.getPan(), mrTemp.getGsTin(), 700);
            }
        }
        if (DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmPartyType.Unregistered.getId()))) {

            if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGsTINDate()).equalsIgnoreCase("")) {
                throw new DmException(850, "In case of unregistered party type, you should not fill GSTIN No. and GSTIN Date.");
            }
            if (mrTemp.isGstApplicable() == true) {
//                if (DmShared.xNull(mrTemp.getAadhar()).equalsIgnoreCase("")) {
//                    if (DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("")) {
//                        throw new DmException(900, "In case of unregistered party type AADHAAR No. or PAN No. is mandatory.");
//                    }
//                }
            }
        }
        if (!DmShared.xNull(mrTemp.getGsTINDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getGsTINDate()).getTime() > (DmShared.convertDate(mrTemp.getCurrentDate())).getTime()) {
                throw new DmException(800, "GSTIN Date should not be greater than current date.");
            }
        }
        if (!DmShared.xNull(mrTemp.getAadhar()).equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getAadhar()).length() != 12) {
                throw new DmException(925, "AADHAAR No. should be 12 digit.");
            }
        }
    }
}
