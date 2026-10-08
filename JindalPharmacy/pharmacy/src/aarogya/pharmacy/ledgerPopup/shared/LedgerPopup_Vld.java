package aarogya.pharmacy.ledgerPopup.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LedgerPopup_Vld {

    public LedgerPopup_Vld() {
    }

    public static void chkSave(LedgerPopup_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getAcGroupCode(), "A/c Group", 200);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 300);

        if (!DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase("")) {
            DmShared.isValidText(mrTemp.getCityCode(), "City", 400);
            if (DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmEnum.DmPartyType.Registered.getId()))
                    || DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmEnum.DmPartyType.Composite.getId()))) {
                DmShared.isValidText(mrTemp.getGstin(), "GSTIN No.", 500);
                DmShared.isValidText(mrTemp.getGstinDate(), "GSTIN Date", 600);
            }
            if (DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmEnum.DmPartyType.Unregistered.getId()))) {
                if (!DmShared.xNull(mrTemp.getGstin()).equalsIgnoreCase("")) {
                    throw new DmException(500, "In case of unregistered party type you should not fill GSTIN No.");
                }
                if (!DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                    throw new DmException(600, "In case of unregistered party type you should not fill GSTIN Date.");
                }
                if (DmShared.xNull(mrTemp.getAadhaar()).equalsIgnoreCase("")) {
                    if (DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("")) {
                        throw new DmException(700, "In case of unregistered party type aadhaar no. or pan no. is mandatory.");
                    }
                }
            }
        } else {
            if (!DmShared.xNull(mrTemp.getGstin()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
                throw new DmException(900, "Party type is mandatory");
            }
        }
        if (!DmShared.xNull(mrTemp.getGstinDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getGstinDate()).getTime() > (DmShared.convertDate(mrTemp.getCurrentDate())).getTime()) {
                throw new DmException(600, "GSTIN Date should not be greater than current date.");
            }
        }
        if (!DmShared.xNull(mrTemp.getAadhaar()).equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getAadhaar()).length() != 12) {
                throw new DmException(700, "Aadhaar No. should be 12 digit.");
            }
        }
        if (!DmShared.xNull(mrTemp.getPan()).trim().equals("")) {
            if (DmShared.xNull(mrTemp.getPan()).trim().length() != 10) {
                throw new DmException(800, "Pan no should be 10 digit.");
            } else {
                DmShared.isValidPanNo(mrTemp.getPan(), 800);
            }
        }
    }

}
