package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptBillPaymentMaster_Vld {

    public static enum BillType {
        DUE_DATE_PASSED("Pending For Payment-Due Date Passed"),
        DUE_0_7_DAYS("Pending For Payment-Due in 0-7 Days"),
        DUE_8_15_DAYS("Pending For Payment-Due in 8-15 Days"),
        DUE_16_30_DAYS("Pending For Payment-Due in 16-30 Days"),
        DUE_GREATER_30_DAYS("Pending For Payment-Due in >30 Days"),
        NILL_PAYMENT("Nill Payment"),
        PAYMENT_COMPLETED("Payment completed");

        private String text;

        BillType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static BillType getByText(String text) {
          
            for (BillType b : BillType.values()) {
                if (b.getText().equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {

        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }

        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }

        if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
            throw new DmException(100, "From Date should be less than or equal to To Date.");
        }

    }

}
