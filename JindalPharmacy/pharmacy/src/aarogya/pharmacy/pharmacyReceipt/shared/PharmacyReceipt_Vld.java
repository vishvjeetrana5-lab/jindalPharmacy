package aarogya.pharmacy.pharmacyReceipt.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class PharmacyReceipt_Vld {

    public static void chkSave(PharmacyReceipt_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getCustomerTypeCode()), "Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getManualCode()), "Customer", 200);
        if (DmShared.vNull(mrTemp.getAmount()) <= 0) {
            throw new DmException(300, "Invalid Amount");
        }
    }
}
