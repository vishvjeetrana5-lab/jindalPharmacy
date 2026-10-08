package dataman.accounts.advanceEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AdvanceEntry_Vld {

    public static void chkSave(AdvanceEntry_Mdl mrTemp) {
        DmShared.isValidText(DmShared.dNull(mrTemp.getVoucherDate()), "Ent.Date", 100);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getPaidAmount()), "Amt. Paid", 200);

        DmShared.isValidText(DmShared.xNull(mrTemp.getAdvReqDocId()), "Adv. Req. No.", 300);
        DmShared.isValidText(DmShared.xNull(mrTemp.getModuleTag()), "Module", 400);


        if (DmShared.iNull(mrTemp.getPaidAmount()).doubleValue() > DmShared.iNull(mrTemp.getAdvanceReqAmt()).doubleValue()) {
            throw new DmException(500, "Advance amount can not be greater than the advance req. amount.");
        }
//          double a = DmShared.vNull(mrTemp.getTotalAdvancePaidAmt()).doubleValue() + DmShared.vNull(mrTemp.getTotalBillAmt()).doubleValue();
//        if(DmShared.vNull(mrTemp.getAdvanceReqAmt())>(DmShared.vNull(mrTemp.getTotalPoAmt()).doubleValue()-a))
//        {
//              throw new DmException(400, "Advance Req. Amt. should be greater than Total P.O Value-[total advance Paid + total Bill Amt.]");
//        }
         
    }

}
