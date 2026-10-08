package aarogya.pharmacy.medicineGroupMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

public class MedicineGroupMaster_Vld {

    public static void chkSave(MedicineGroupMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Medicine Group", 100);
        DmShared.isValidText(mrTemp.getIsCommonTag().trim(), "Common Selection", 300);
        if (!DmShared.xNull(mrTemp.getSaleDiscount()).equalsIgnoreCase("")) {
            if (Double.valueOf(mrTemp.getSaleDiscount()) >= 100) {
                throw new DmException(200, "Discount can't be greater than or equal To 100%.");
            }
        }
    }
}
