package aarogya.master.corporateEmployeeMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CorporateEmployeeMaster_Vld {

    public static void chkSave(CorporateEmployeeMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getCorporateName().trim(), "Corporate", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 200);
        DmShared.isValidText(mrTemp.getCardHolderName().trim(), "Card Holder Name", 400);
        DmShared.isValidText(mrTemp.getRelationship().trim(), "Relationship", 500);

        if (DmShared.iNull(mrTemp.getAgeYear()).intValue() != 0 || DmShared.iNull(mrTemp.getAgeMonth()).intValue() != 0
                || ((DmShared.iNull(mrTemp.getAgeDay()).intValue() == 0) && (!DmShared.dNull(mrTemp.getDob()).equalsIgnoreCase("")))
                || DmShared.iNull(mrTemp.getAgeDay()).intValue() != 0) {

            if (DmShared.iNull(mrTemp.getAgeYear()).intValue() < 0) {
                throw new DmException(600, "Invalid Age [YY].");
            }
            if (DmShared.iNull(mrTemp.getAgeYear()).intValue() > 255) {
                throw new DmException(601, "Invalid Age [YY].");
            }
            if (DmShared.iNull(mrTemp.getAgeMonth()).intValue() < 0) {
                throw new DmException(602, "Invalid Age [MM].");
            }
            if (DmShared.iNull(mrTemp.getAgeMonth()).intValue() > 12) {
                throw new DmException(602, "Invalid Age [MM].");
            }
            if (DmShared.iNull(mrTemp.getAgeDay()).intValue() < 0) {
                throw new DmException(603, "Invalid Age [DD].");
            }
            if (DmShared.iNull(mrTemp.getAgeDay()).intValue() > 31) {
                throw new DmException(603, "Invalid Age [DD].");
            }
        } else {
            throw new DmException(600, "Age is mandatory.");
        }

        DmShared.isValidAmount(mrTemp.getGenderTag().trim(), "Gender", 700);

    }

}
