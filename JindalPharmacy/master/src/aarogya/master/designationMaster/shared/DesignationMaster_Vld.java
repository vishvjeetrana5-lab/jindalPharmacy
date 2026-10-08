package aarogya.master.designationMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DesignationMaster_Vld {

    public static void chkSave(DesignationMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Designation", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
    }
}
