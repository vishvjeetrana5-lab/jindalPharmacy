package aarogya.master.employeeMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class EmployeeMaster_Vld {

    public static void chkSave(EmployeeMaster_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getDisplayName(), "Display name", 200);
        DmShared.isValidText(mrTemp.getGenderCode(), "Gender", 300);
        DmShared.isValidText(mrTemp.getAccGroup(), "A/c group", 400);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 500);
        DmShared.isValidEMail(mrTemp.getEmail(), 700);
        DmShared.isValidMobile(mrTemp.getMobileNo(), 800);

        if (!DmShared.xNull(mrTemp.getAadhaarNo()).trim().equalsIgnoreCase("")) {
            if (DmShared.xNull(mrTemp.getAadhaarNo()).length() != 12) {
                throw new DmException(550, "Aadhaar No. should be 12 digit.");
            }
        }
        if (!DmShared.xNull(mrTemp.getPan()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getPan()).length() != 10) {
            throw new DmException(600, "PAN should be of 10(Ten) characters.");
        }

        if (!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(currentDate)).getTime())) {
                throw new DmException(1000, "DOB should be less than to Current Date.");
            }
            if ((!DmShared.dNull(mrTemp.getDoa()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase(""))) {
                if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(mrTemp.getDoa())).getTime())) {
                    throw new DmException(1000, "DOB should be less than to Anniv. Date.");
                }
            }
            if ((!DmShared.dNull(mrTemp.getDoj()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase(""))) {
                if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(mrTemp.getDoj())).getTime())) {
                    throw new DmException(1000, "DOB should be less than to Joining Date.");
                }
            }
            if ((!DmShared.dNull(mrTemp.getDob()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getPassportIssueDate()).trim().equalsIgnoreCase(""))) {
                if ((DmShared.convertDate(mrTemp.getDob()).getTime() >= (DmShared.convertDate(mrTemp.getPassportIssueDate())).getTime())) {
                    throw new DmException(1000, "DOB should be less than to Passport Issue Date.");
                }
            }
        }

        if (!DmShared.dNull(mrTemp.getDoa()).trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getDoa()).getTime() > (DmShared.convertDate(currentDate)).getTime())) {
                throw new DmException(1100, "Anniv. Date should be less than or equal to Current Date.");
            }
        }

        if (!DmShared.dNull(mrTemp.getDoj()).trim().equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getDoj()).getTime() > (DmShared.convertDate(currentDate)).getTime())) {
                throw new DmException(1200, "Joining Date should be less than or equal to Current Date.");
            }
        }

        if ((!DmShared.dNull(mrTemp.getDoj()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getDor()).trim().equalsIgnoreCase(""))) {
            if ((DmShared.convertDate(mrTemp.getDoj()).getTime() > (DmShared.convertDate(mrTemp.getDor())).getTime())) {
                throw new DmException(1400, "Joining Date should be less than or equal to Resignation Date.");
            }
        }

        if (!DmShared.dNull(mrTemp.getPassportIssueDate()).equalsIgnoreCase("")) {
            DmShared.isValidText(mrTemp.getPassportExpirydDate(), "Passport Expiry Date", 1500);
        }
        if (!DmShared.dNull(mrTemp.getPassportExpirydDate()).equalsIgnoreCase("")) {
            DmShared.isValidText(mrTemp.getPassportIssueDate(), "Passport Issue Date", 1600);
        }

        if ((!DmShared.dNull(mrTemp.getPassportIssueDate()).trim().equalsIgnoreCase("")) && (!DmShared.dNull(mrTemp.getPassportExpirydDate()).trim().equalsIgnoreCase(""))) {
            if ((DmShared.convertDate(mrTemp.getPassportIssueDate()).getTime() >= (DmShared.convertDate(mrTemp.getPassportExpirydDate())).getTime())) {
                throw new DmException(1700, "Passport Issue Date should be less than  to Passport Expiry Date.");
            }
        }

    }
}
