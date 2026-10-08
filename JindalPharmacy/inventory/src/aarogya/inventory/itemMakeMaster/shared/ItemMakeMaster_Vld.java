package aarogya.inventory.itemMakeMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ItemMakeMaster_Vld {

    public static void chkSave(ItemMakeMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Item Make", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
        DmShared.isValidEMail(mrTemp.getLclEmail(), 300);
        DmShared.isValidEMail(mrTemp.getRegEmail(), 400);
        DmShared.isValidEMail(mrTemp.getNatEmail(), 500);
    }
}
