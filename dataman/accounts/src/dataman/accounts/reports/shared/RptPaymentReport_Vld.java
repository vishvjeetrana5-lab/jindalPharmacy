package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptPaymentReport_Vld {

    public static enum DataType {
        PAY_ENTRY_PENDING("Payment Request Pending For Payment Entry"),
        PAY_ENTRY_APPROVAL("Payment Entry Pending For Payment Entry Authorization");

        private String text;

        DataType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static DataType getByText(String text) {

            for (DataType b : DataType.values()) {
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
