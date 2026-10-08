package aarogya.pharmacy.medicineCategoryMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MedicineCategoryMaster_Vld {

    public static void chkSave(MedicineCategoryMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Category Name", 100);
        DmShared.isValidText(mrTemp.getAllowBifurcationTag().trim(), "Allow Bifurcation", 200);

    }
}
