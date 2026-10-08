package dataman.accounts.finalPaymentEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class FinalPaymentEntry_Vld {

    public static void chkSave(FinalPaymentEntry_Mdl mrTemp) {
        int i = 0;
        DmShared.isValidText(DmShared.dNull(mrTemp.getV_Date()), "Ent.Date", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getModuleTag()), "Module", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getSupplierTag()), "Supplier", 300);
        for (GrdPendingFPReqDetail_Mdl grdMdl : mrTemp.getLstPendingFPayReq()) {
            // to check whether any request for payment is there or not.
            i = i + DmShared.iNull(grdMdl.getChecked()).intValue();
            if (i > 0) {
                break;
            }
        }
        if (i == 0) {
            throw new DmException(300, "Select atleast one payment request.");
        }
//        DmShared.isValidAmount(DmShared.iNull(mrTemp.getOriginalAmt()), "Amt. Paid", 400);

//        if (DmShared.iNull(mrTemp.getPaidAmount()).doubleValue() > DmShared.iNull(mrTemp.getAdvanceReqAmt()).doubleValue()) {
//            throw new DmException(500, "Advance amount can not be greater than the advance req. amount.");
//        }
//          double a = DmShared.vNull(mrTemp.getTotalAdvancePaidAmt()).doubleValue() + DmShared.vNull(mrTemp.getTotalBillAmt()).doubleValue();
//        if(DmShared.vNull(mrTemp.getAdvanceReqAmt())>(DmShared.vNull(mrTemp.getTotalPoAmt()).doubleValue()-a))
//        {
//              throw new DmException(400, "Advance Req. Amt. should be greater than Total P.O Value-[total advance Paid + total Bill Amt.]");
//        }
    }
}
