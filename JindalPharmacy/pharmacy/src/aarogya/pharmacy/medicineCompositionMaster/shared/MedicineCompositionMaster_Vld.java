package aarogya.pharmacy.medicineCompositionMaster.shared;

import aarogya.pharmacy.medicineCompositionMaster.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MedicineCompositionMaster_Vld {

    public static void chkSave(MedicineCompositionMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Medicine Composition", 100);
        DmShared.isValidText(mrTemp.getActiveName().trim(), "Active ", 200);

    }

}
