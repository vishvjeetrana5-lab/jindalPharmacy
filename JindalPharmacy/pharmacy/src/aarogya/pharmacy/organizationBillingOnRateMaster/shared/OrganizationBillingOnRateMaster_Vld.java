package aarogya.pharmacy.organizationBillingOnRateMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OrganizationBillingOnRateMaster_Vld {

    public static void chkSave(OrganizationBillingOnRateMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Organization", 100);

        DmShared.isValidText(mrTemp.getMedicineSaleOnTag().trim(), "Medicine Sale On", 200);

    }

}
