package aarogya.master.serviceCategoryMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ServiceCategoryMaster_Vld {

    public static void chkSave(ServiceCategoryMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Category", 100);
    }
}
