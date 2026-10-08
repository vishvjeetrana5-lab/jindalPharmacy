package aarogya.master.icdxMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ICDXMaster_Vld {

    public static void chkSave(ICDXMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getManualCode(), "ICDX code", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "ICDX name", 200);
    }

}
