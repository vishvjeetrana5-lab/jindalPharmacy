package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.HashMap;

public class RptPatientSummary_Vld {

    public static enum ReportView {
        BOTH("Both"), TO_REFUND("To Refund"), PENDING("Pending");

        private String text;

        ReportView(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public static ReportView getByText(String text) {
            ReportView rtnValue = null;
            if (DmShared.xNull(text).equalsIgnoreCase(BOTH.getText())) {
                rtnValue = BOTH;
            } else if (DmShared.xNull(text).equalsIgnoreCase(PENDING.getText())) {
                rtnValue = PENDING;
            } else if (DmShared.xNull(text).equalsIgnoreCase(TO_REFUND.getText())) {
                rtnValue = TO_REFUND;
            }
            return rtnValue;
        }
    }

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);

        if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
            throw new DmException(200, "From Date should be less than or equal to To Date.");
        }
        DmShared.isValidText(hashMapFilter.get("status").getText(), "Status", 200);
    }
}
