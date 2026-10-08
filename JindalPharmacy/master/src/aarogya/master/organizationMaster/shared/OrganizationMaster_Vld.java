package aarogya.master.organizationMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OrganizationMaster_Vld {

    public static void chkSave(OrganizationMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
//        DmShared.isValidText(mrTemp.getMedicineSaleOnTag().trim(), "Med. Sale On", 300);
        DmShared.isValidText(mrTemp.getIsNatureTag().trim(), "Nature", 400);
        DmShared.isValidText(mrTemp.getActiveTag().trim(), "Active", 500);

        DmShared.isValidMobile(mrTemp.getMobileNo(), 700);

        DmShared.isValidEMail(mrTemp.getEmail(), 800);
        DmShared.isValidAmount(mrTemp.getRateListCode(), "Rate list", 900);
        DmShared.isValidText(mrTemp.getApplicableDate(), "Effective Date", 1000);

        if (!DmShared.dNull(mrTemp.getAgreementDate()).equalsIgnoreCase("") && !DmShared.dNull(mrTemp.getTarrifRevisionDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getAgreementDate()).getTime() > DmShared.convertDate(mrTemp.getTarrifRevisionDate()).getTime()) {
                throw new DmException(600, "Tariff Revision Date should be greater than Agreement Date");
            }
        }
    }
}
