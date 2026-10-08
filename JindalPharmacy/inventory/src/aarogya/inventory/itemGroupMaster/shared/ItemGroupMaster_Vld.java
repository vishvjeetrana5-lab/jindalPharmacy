package aarogya.inventory.itemGroupMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ItemGroupMaster_Vld {

    public static void chkSave(ItemGroupMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Item Group", 100);
        DmShared.isValidText(mrTemp.getUnderCateCode().trim(), "Under Category", 200);
    }
}
