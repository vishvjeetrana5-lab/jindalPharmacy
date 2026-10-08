package aarogya.master.cardDischarge.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CardDischarge_Vld {

    public static void chkSave(CardDischarge_Mdl mrTemp) {
        double grdAmount = 0;
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 200);
        DmShared.isValidAmount(mrTemp.getCardRegMdl().getSharedKeyFieldCode(), "Card Reg. No.", 300);
        DmShared.isValidAmount(mrTemp.getAmount(), "Amount", 400);
        if (mrTemp.getLstPayment().isEmpty()) {
            throw new DmException(500, "Payment Mode is mandatory.");
        } else {
            for (GrdCardPayment_Mdl grdMdl : mrTemp.getLstPayment()) {
                if (DmShared.vNull(grdMdl.getAmount()).doubleValue() > 0 && DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    grdAmount += DmShared.vNull(grdMdl.getAmount()).doubleValue();
                }
            }
            if (DmShared.vNull(mrTemp.getAmount()).doubleValue() != grdAmount) {
                throw new DmException(500, "Please adjust full Value.");
            }
        }
    }
}
