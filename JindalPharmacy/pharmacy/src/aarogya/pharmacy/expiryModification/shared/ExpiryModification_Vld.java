package aarogya.pharmacy.expiryModification.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ExpiryModification_Vld {

    public static void chkSave(ExpiryModification_Mdl mrTemp) {
        if (!DmShared.dNull(mrTemp.getNewExpiryDate()).equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getNewExpiryDate()).getTime() <= (DmShared.convertDate(DmShared.xNull(mrTemp.getVoucherDate()))).getTime())) {
                throw new DmException(300, "New Expiry Date should be greater than current date.");
            }
        }
        DmShared.isValidText(DmShared.xNull(mrTemp.getNewExpiryDate()), "New Expiry Date", 100);
    }
}
