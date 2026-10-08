package dataman.accounts.accountMaster.shared;

import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AccountMaster_Vld {

    public static void chkSave(AccountMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Account name", 100);
        DmShared.isValidText(mrTemp.getDisplayName(), "Display name", 200);
        DmShared.isValidText(mrTemp.getAccGroup(), "A/c group", 300);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 500);
        DmShared.isValidEMail(mrTemp.getEmail(), 400);
        if (!DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getPan()).length() != 10) {
                throw new DmException(950, "PAN should be of 10(Ten) characters.");
            } else {
                DmShared.isValidPanNo(mrTemp.getPan(), 950);
            }
        }

        if (DmShared.xNull(mrTemp.getNatureManualCode()).equalsIgnoreCase("TP")) {
            if (!DmShared.xNull(mrTemp.getIsSelfTransportTag()).equalsIgnoreCase("1")) {
                if ((DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())
                        || DmShared.xNull(mrTemp.getPartyType()).trim().equalsIgnoreCase(""))) {
                    if (DmShared.xNull(mrTemp.getTransportId()).trim().equalsIgnoreCase("")) {
                        throw new DmException(960, " Transporter ID is mandatory");
                    }
                }
            }
        }

        if (!DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase("")) {
            DmShared.isValidAmount(mrTemp.getCity(), "City", 450);
            if (DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmEnum.DmPartyType.Registered.getId()))
                    || DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmEnum.DmPartyType.Composite.getId()))) {
                DmShared.isValidText(mrTemp.getGsTIN(), "GSTIN No.", 700);
                DmShared.isValidText(mrTemp.getGsTINDate(), "GSTIN Date", 800);
            }
            if (!DmShared.xNull(mrTemp.getGsTIN()).equalsIgnoreCase("")
                    || !DmShared.dNull(mrTemp.getGsTINDate()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getPartyType()).trim().equalsIgnoreCase("")) {
                    throw new DmException(600, "For defining GST detail, you should have to fill Party Type first.");
                } else if (DmShared.xNull(mrTemp.getPan()).trim().equals("")) {
                    throw new DmException(900, "Please fill the Pan No.");
                } else {
                    DmShared.isValidGSTIN(DmShared.xNull(mrTemp.getStateNumericCode()), mrTemp.getPan(), mrTemp.getGsTIN(), 700);
                }
            }
            if (DmShared.xNull(mrTemp.getPartyType()).equalsIgnoreCase((DmEnum.DmPartyType.Unregistered.getId()))) {
                if (!DmShared.xNull(mrTemp.getGsTIN()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGsTINDate()).equalsIgnoreCase("")) {
                    throw new DmException(850, "In case of unregistered party type you should not fill GSTIN No. and GSTIN Date.");
                }
                if (mrTemp.isGstApplicable() == true) {
                    if (DmShared.xNull(mrTemp.getAadhaar()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("")) {
                            throw new DmException(900, "In case of unregistered party type aadhaar no. or pan no. is mandatory.");
                        }
                    }
                }
            }

        } else {
            if (!DmShared.xNull(mrTemp.getGsTIN()).equalsIgnoreCase("") || !DmShared.xNull(mrTemp.getGsTINDate()).equalsIgnoreCase("")) {
                throw new DmException(1000, "Party type is mandatory");
            }
        }
        if (!DmShared.xNull(mrTemp.getGsTINDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getGsTINDate()).getTime() > (DmShared.convertDate(mrTemp.getCurrentDate())).getTime()) {
                throw new DmException(800, "GSTIN Date should not be greater than current date.");
            }
        }
        if (!DmShared.xNull(mrTemp.getAadhaar()).equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getAadhaar()).length() != 12) {
                throw new DmException(925, "Aadhaar no should be 12 digit.");
            }
        }
    }
}
