package aarogya.master.caseMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CaseMaster_Vld {

    public static void chkSave(CaseMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Case name", 100);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Department", 200);
        DmShared.isValidText(mrTemp.getCaseTypeCode(), "Case Type", 300);
    }

}
