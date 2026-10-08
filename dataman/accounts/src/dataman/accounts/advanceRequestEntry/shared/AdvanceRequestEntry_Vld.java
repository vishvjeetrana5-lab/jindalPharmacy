package dataman.accounts.advanceRequestEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AdvanceRequestEntry_Vld {

    public static void chkSave(AdvanceRequestEntry_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(mrTemp.getVoucherDate(), "Req. Date", 100);
        DmShared.isValidText(mrTemp.getModuleTag(), "Module.", 200);
        DmShared.isValidText(mrTemp.getPoRecId(), "P.O. No.", 300);
        DmShared.isValidAmount(mrTemp.getAdvanceReqAmt(), "Advance Req.", 400);
        DmShared.isValidText(mrTemp.getDueDate(), "Due Date", 500);
        if ((DmShared.convertDate(mrTemp.getDueDate()).getTime() < (DmShared.convertDate(DmShared.dNull(currentDate))).getTime())) {
            throw new DmException(500, "Due Date should be greater than or equal to Current date.");
        }
        double a = DmShared.vNull(mrTemp.getTotalAdvancePaidAmt()).doubleValue() + DmShared.vNull(mrTemp.getTotalBillPaymentAmt()).doubleValue();
        if(DmShared.vNull(mrTemp.getAdvanceReqAmt())>(DmShared.vNull(mrTemp.getTotalPoAmt()).doubleValue()-a))
        {
              throw new DmException(400, "Advance Req. can't be Greater than Total P.O Value-[total advance Paid + total Bill Payments.]");
        }

    }
}