package aarogya.pharmacy.medicineModification.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MedicineModification_Vld {

    public static void chkSave(MedicineModification_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getDisplayName().trim(), "Medicine Display Name", 100);
        DmShared.isValidAmount(mrTemp.getSharedKeyFieldCode(), "Medicine Name", 200);
        DmShared.isValidAmount(mrTemp.getCategoryTag(), "Company Name", 300);
        DmShared.isValidAmount(mrTemp.getCategoryTag(), "Medicine Category", 400);
        DmShared.isValidAmount(mrTemp.getGroupTag(), "Medicine Group", 500);
        DmShared.isValidText(mrTemp.getItemPacking(), "Packing", 600);

    }

}
