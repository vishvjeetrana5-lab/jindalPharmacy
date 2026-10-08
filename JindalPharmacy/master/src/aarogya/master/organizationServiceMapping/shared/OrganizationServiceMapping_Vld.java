package aarogya.master.organizationServiceMapping.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OrganizationServiceMapping_Vld {

    public static void chkSave(OrganizationServiceMapping_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getOrganizationTag().trim(), "Empanelment", 100);
        DmShared.isValidText(mrTemp.getApplicableDate().trim(), "Applicable Date", 200);

        boolean flag = false;
        for (GrdService_Mdl grdService_Mdl : mrTemp.getListServiceDetail()) {
            if (!DmShared.xNull(grdService_Mdl.getOrgServiceName()).trim().equalsIgnoreCase("")) {
                flag = true;

                if (DmShared.xNull(grdService_Mdl.getOrgServiceCode()).trim().equalsIgnoreCase("")) {
                    throw new DmException(400, "Org. Service Code can not be blank for " + DmShared.xNull(grdService_Mdl.getOrgServiceName()).trim());
                }
                if (DmShared.xNull(grdService_Mdl.getOrgGroupName()).trim().equalsIgnoreCase("")) {
                    throw new DmException(400, "Org. Group Name can not be blank for " + DmShared.xNull(grdService_Mdl.getOrgServiceName()).trim());
                }
            }
        }
        if (!flag) {
            throw new DmException(400, "Service can not be blank.");
        }
    }
}
