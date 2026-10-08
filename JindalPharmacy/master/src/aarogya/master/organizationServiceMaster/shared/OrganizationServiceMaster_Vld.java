package aarogya.master.organizationServiceMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OrganizationServiceMaster_Vld {

    public static void chkSave(OrganizationServiceMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getOrganizationCode(), "Organization", 100);
        DmShared.isValidText(mrTemp.getServiceGroup().trim(), "Service Group", 200);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Org. Service Name", 300);
        DmShared.isValidText(mrTemp.getOrgServiceCode().trim(), "Service Code", 400);

        if (DmShared.xNull(mrTemp.getServiceGroup()).length() > 100) {
            throw new DmException(200, "Service Group Name Length Exceeded !");
        }
    }

}
