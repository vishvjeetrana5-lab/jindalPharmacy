package aarogya.master.poolGroupMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PoolGroupMaster_Vld {

    public static void chkSave(PoolGroupMaster_Mdl mrTemp) {
       DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Pool Group Name", 100);
        DmShared.isValidText(mrTemp.getActiveName().trim(), "Active ", 200);
    }

}
