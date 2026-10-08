package dataman.accounts.finalPaymentRequestEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class FinalPaymentRequestEntry_Vld {

    public static void chkSave(FinalPaymentRequestEntry_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(mrTemp.getVoucherDate(), "Req. Date", 100);
        DmShared.isValidText(mrTemp.getModuleTag(), "Module.", 200);
        DmShared.isValidText(mrTemp.getSupplierTag(), "Supplier.", 300);
        DmShared.isValidText(mrTemp.getpBillRecId(), "P.B. No.", 300);
//        DmShared.isValidAmount(mrTemp.getFinalDuePayment(), "Final Due Paymnet", 400);
        if (DmShared.vNull(mrTemp.getFinalDuePayment()).doubleValue() < 0) {
            throw new DmException(400, "Final Due Pay. must be greater than zero.");
        }
        DmShared.isValidText(mrTemp.getPaymentDate(), "Pay Date", 500);
        if ((DmShared.convertDate(mrTemp.getPaymentDate()).getTime() < (DmShared.convertDate(DmShared.dNull(currentDate))).getTime())) {
            throw new DmException(600, "Payment Date should be greater than or equal to Current date.");
        }
        //====================================================================
//        if (mrTemp.getPartPay().equalsIgnoreCase("1")) {
//            double a = DmShared.vNull(mrTemp.getTotalAdvNet()).doubleValue() + DmShared.vNull(mrTemp.getTotalBillPaymnet()).doubleValue();
//            if (DmShared.vNull(mrTemp.getFinalDuePayment()) > (DmShared.vNull(mrTemp.getTotalNetBillEntry()).doubleValue() - a)) {
//                if (DmShared.vNull(mrTemp.getFinalDuePayment()).longValue() != 0) {
//                    throw new DmException(400, "Final Due Pay. should be less than  Net Bill Value-[total advance settled + Total P.Bill Paid]");
//                } else {
//                    double value =DmShared.vNull(mrTemp.getTotalNetBillEntry()).doubleValue();
//                    mrTemp.setAdvanceSetteled(DmShared.vNull(value).toString());
//                    mrTemp.setTotalAdvNet(DmShared.vNull(value).toString());
//                }
//
//            }
//        }
//===================================================================================
    }
}
