package aarogya.pharmacy.changePaymentSource.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ChangePaymentSource_Vld {

    public static void chkSave(ChangePaymentSource_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getVoucherTypeCode()), "Voucher Type", 0);
        DmShared.isValidText(DmShared.xNull(mrTemp.getRegType()), "Type", 100);
        DmShared.isValidAmount(DmShared.xNull(mrTemp.getPatientDetail().getBillDocId()), "Bill No.", 200);
        DmShared.isValidText(DmShared.dNull(mrTemp.getVoucherDate()), "Date", 300);
        if (!DmShared.dNull(mrTemp.getPatientDetail().getRegDate()).equalsIgnoreCase("")) {
            if ((DmShared.convertDate(mrTemp.getVoucherDate()).getTime() < (DmShared.convertDate(mrTemp.getPatientDetail().getRegDate())).getTime())) {
                throw new DmException(300, "Date should be greater than or equal to registration date (" + DmShared.dNull(mrTemp.getPatientDetail().getRegDate()) + ").");
            }
        }
        DmShared.isValidText(DmShared.xNull(mrTemp.getChangePaymentSource()), "Change Source", 400);
    }
}
