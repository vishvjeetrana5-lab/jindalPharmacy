package aarogya.master.overHeadCategoryMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OverHeadCategoryMaster_Vld {

    public static void chkSave(OverHeadCategoryMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Category", 100);
    }
}
