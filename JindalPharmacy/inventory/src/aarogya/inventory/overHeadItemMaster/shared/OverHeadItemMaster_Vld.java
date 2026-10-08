package aarogya.inventory.overHeadItemMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OverHeadItemMaster_Vld {

    public OverHeadItemMaster_Vld() {
    }

    public static void chkSave(OverHeadItemMaster_mdl mrTemp) {

        DmShared.isValidText(mrTemp.getCategoryTag(), "Category", 200);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 300);
        DmShared.isValidText(mrTemp.getGroupTag(), "Group", 400);
        DmShared.isValidText(mrTemp.getSkuTag(), "SKU", 500);
        DmShared.isValidText(mrTemp.getActiveTag(), "Active", 600);

    }

}
