package aarogya.master.overHeadMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OverHeadMaster_Vld {

    public static void chkSave(OverHeadMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getCategoryCode(), "Category", 300);

    }
}
