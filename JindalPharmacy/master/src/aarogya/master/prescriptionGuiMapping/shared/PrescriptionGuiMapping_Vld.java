package aarogya.master.prescriptionGuiMapping.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PrescriptionGuiMapping_Vld {

    public static void chkSave(PrescriptionGuiMapping_Mdl mrTemp) {

        // Blank Valid
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Entry", 100);

    }
}
