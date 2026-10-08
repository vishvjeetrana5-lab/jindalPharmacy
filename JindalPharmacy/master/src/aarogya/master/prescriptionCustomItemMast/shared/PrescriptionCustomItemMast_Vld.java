package aarogya.master.prescriptionCustomItemMast.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PrescriptionCustomItemMast_Vld {

    public static void chkSave(PrescriptionCustomItemMast_Mdl mrTemp, boolean isClientSide) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 200);
    }

}
