package aarogya.master.serviceGroupMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ServiceGroupMaster_Vld {

    public static void chkSave(ServiceGroupMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Group name", 100);
    }

}
