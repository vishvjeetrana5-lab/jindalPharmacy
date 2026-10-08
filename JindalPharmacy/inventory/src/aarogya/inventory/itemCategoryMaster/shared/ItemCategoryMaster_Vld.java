package aarogya.inventory.itemCategoryMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ItemCategoryMaster_Vld {

    public static void chkSave(ItemCategoryMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Category Name", 100);
    }
}
