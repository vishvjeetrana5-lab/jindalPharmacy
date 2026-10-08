package dataman.utility.siteMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class SiteMaster_Vld {

    public enum RegisteredType {

        COMPOSITE("C", "Composite"), REGISTERED("R", "Registered"), UNREGISTERED("U", "Unregistered");

        private final String id;
        private final String value;

        public String getId() {
            return id;
        }

        public String getValue() {
            return value;
        }

        RegisteredType(String id, String text) {
            this.id = id;
            this.value = text;
        }

        public static RegisteredType getKeyOf(String id) {
            RegisteredType returnRegis = null;
            if (id.equalsIgnoreCase(REGISTERED.getId())) {
                returnRegis = REGISTERED;
            } else if (id.equalsIgnoreCase(UNREGISTERED.getId())) {
                returnRegis = UNREGISTERED;
            } else if (id.equalsIgnoreCase(COMPOSITE.getId())) {
                returnRegis = COMPOSITE;
            }
            return returnRegis;
        }

        public static RegisteredType getValueOf(String text) {
            RegisteredType returnRegis = null;

            if (text.equalsIgnoreCase(REGISTERED.getValue())) {
                returnRegis = REGISTERED;
            } else if (text.equalsIgnoreCase(UNREGISTERED.getValue())) {
                returnRegis = UNREGISTERED;
            } else if (text.equalsIgnoreCase(COMPOSITE.getValue())) {
                returnRegis = COMPOSITE;
            }
            return returnRegis;
        }
    }

    public static void chkSave(SiteMaster_Mdl mrTemp) {
        Boolean flag = false;
        DmShared.isValidText(mrTemp.getBranchCode(), "Branch Name ", 100);
        DmShared.isValidText(mrTemp.getRegionalLanguageCode(), "Regional Language ", 200);
        DmShared.isValidText(mrTemp.getPltCode(), "Product License Key", 300);
        DmShared.isValidText(mrTemp.getSiteName(), "Site Name ", 400);
        DmShared.isValidText(mrTemp.getSiteDisplayName(), "Site Display Name", 450);
        DmShared.isValidText(mrTemp.getCode(), "Site Code ", 500);
        DmShared.isValidText(mrTemp.getHoCode(), "Head Office ", 600);
        DmShared.isValidText(mrTemp.getManualCode(), " Manual Code ", 700);
        DmShared.isValidText(mrTemp.getRegistrationTypeCode(), "Registration Type (GST)", 770);
        DmShared.isValidEMail(mrTemp.getEmail(), 800);

//        if (!DmShared.xNull(mrTemp.getPanNo()).trim().equalsIgnoreCase("")) {
//            if (DmShared.xNull(mrTemp.getPanNo()).length() != 10) {
//                throw new DmException(850, "PAN should be of 10(Ten) characters.");
//            } else {
//                DmShared.isValidPanNo(mrTemp.getPanNo(), 850);
//            }
//        }
//
//        if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("")) {
//            DmShared.isValidText(mrTemp.getPanNo(), "PAN", 850);
//        }

        if (DmShared.vNull(mrTemp.getStateNumericCode()).intValue() <= 0) {
            throw new DmException(750, "State is mandatory.");
        }
        
        
//        if (!DmShared.xNull(mrTemp.getTinNo()).equalsIgnoreCase("")) {
//            if (DmShared.xNull(mrTemp.getTinDate()).equalsIgnoreCase("")) {
//                flag = true;
//            }
//        } else {
//            if (DmShared.xNull(mrTemp.getTinNo()).equalsIgnoreCase("")) {
//                if (!DmShared.xNull(mrTemp.getTinDate()).equalsIgnoreCase("")) {
//                    flag = true;
//                }
//            }
//        }
//        if (flag) {
//            throw new DmException(900, "TIN No. and TIN date both are required.");
//        }
//        if (DmShared.xNull(mrTemp.getRegistrationTypeCode()).equalsIgnoreCase((RegisteredType.REGISTERED.getId()))
//                || DmShared.xNull(mrTemp.getRegistrationTypeCode()).equalsIgnoreCase((RegisteredType.COMPOSITE.getId()))) {
//            DmShared.isValidText(mrTemp.getGsTin(), "GSTIN No.", 1000);
//            DmShared.isValidText(mrTemp.getGstinDate(), "GSTIN Date", 1100);
//        }
//        if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("")) {
//            DmShared.isValidGSTIN(mrTemp.getStateNumericCode(), mrTemp.getPanNo(), mrTemp.getGsTin(), 1000);
//        }
//        if (DmShared.xNull(mrTemp.getRegistrationTypeCode()).equalsIgnoreCase((RegisteredType.UNREGISTERED.getId()))) {
//            if (!DmShared.xNull(mrTemp.getGsTin()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
//                throw new DmException(1000, "In case of unregistered party type, you should not fill GSTIN No. and GSTIN Date.");
//            }
//        }
//        if (!DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
//            if (DmShared.convertDate(mrTemp.getGstinDate()).getTime() > (DmShared.convertDate(mrTemp.getCurrentDate())).getTime()) {
//                throw new DmException(1100, "GSTIN Date should not be greater than current date.");
//            }
//        }
    }

}
