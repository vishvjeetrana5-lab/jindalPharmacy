package aarogya.master.prescriptionCustomGroupMast.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PrescriptionCustomGroupMast_Vld {

    public static void chkSave(PrescriptionCustomGroupMast_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Case name", 100);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 200);
    }

}
