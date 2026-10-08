package aarogya.master.packageMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PackageMaster_Vld {

    public static void chkSave(PackageMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Package Name", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getNatureCode().trim(), "Nature", 300);
    }
}
