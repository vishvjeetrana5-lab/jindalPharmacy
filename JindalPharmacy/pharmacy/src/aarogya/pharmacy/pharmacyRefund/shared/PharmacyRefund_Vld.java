package aarogya.pharmacy.pharmacyRefund.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class PharmacyRefund_Vld {

    public static void chkSave(PharmacyRefund_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getCustomerTypeCode()), "Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getManualCode()), "Customer", 200);
        if (DmShared.vNull(mrTemp.getAmount()) > 0) {
            throw new DmException(300, "Invalid Amount");
        }
        if (DmShared.vNull(mrTemp.getAmount()) > DmShared.vNull(mrTemp.getEntryBalance())) {
            throw new DmException(300, "Refund Amount can not be greater then Balance Amount : " + DmShared.vNull(mrTemp.getEntryBalance()));
        }
    }
}
