package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.HashMap;

public class RptDischargeReport_Vld {

    public enum ReportType {
        BillWise(1, "Bill Wise"),
        ConsultantWise(2, "Consultant Wise"),
        DepartmentWise(3, "Department Wise");

        private int tagValue;
        private String textValue;

        ReportType(int id, String text) {
            this.tagValue = id;
            this.textValue = text;
        }

        public int getTagValue() {
            return tagValue;
        }

        public String getTextValue() {
            return textValue;
        }

        public static ReportType ReportValue(int id) {
            ReportType rptType = null;
            if (BillWise.getTagValue() == id) {
                rptType = BillWise;
            } else if (ConsultantWise.getTagValue() == id) {
                rptType = ConsultantWise;
            } else if (DepartmentWise.getTagValue() == id) {
                rptType = DepartmentWise;
            }
            return rptType;
        }
    }

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);
        DmShared.isValidText(hashMapFilter.get("type").getText(), "type", 300);

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(200, "From Date should be less than or equal to To Date.");
            }
        }
    }

}
