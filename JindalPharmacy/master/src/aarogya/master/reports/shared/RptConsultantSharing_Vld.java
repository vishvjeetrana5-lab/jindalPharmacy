package aarogya.master.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptConsultantSharing_Vld {

    public enum ReportViewType {
        DATE_WISE(1, "Date Wise"), CONSULTANT_WISE(2, "Consultant Wise"),
        COLLECTION_CENTER_WISE(3, "Collection Center Wise"), ORGANIZATION_WISE(4, "Organization Wise"),
        MONTH_WISE(5, "Month Wise");

        private String text;
        private int id;

        public String getText() {
            return text;
        }

        public int getId() {
            return id;
        }

        private ReportViewType(int id, String text) {
            this.text = text;
            this.id = id;
        }

        public static ReportViewType getReportType(String reportText) {
            ReportViewType rtnType = null;
            if (DmShared.xNull(reportText).equalsIgnoreCase(DATE_WISE.getText())) {
                rtnType = DATE_WISE;
            } else if (DmShared.xNull(reportText).equalsIgnoreCase(MONTH_WISE.getText())) {
                rtnType = MONTH_WISE;
            } else if (DmShared.xNull(reportText).equalsIgnoreCase(CONSULTANT_WISE.getText())) {
                rtnType = CONSULTANT_WISE;
            } else if (DmShared.xNull(reportText).equalsIgnoreCase(COLLECTION_CENTER_WISE.getText())) {
                rtnType = COLLECTION_CENTER_WISE;
            } else if (DmShared.xNull(reportText).equalsIgnoreCase(ORGANIZATION_WISE.getText())) {
                rtnType = ORGANIZATION_WISE;
            }
            return rtnType;
        }

        public static ReportViewType getReportType(int typeId) {
            ReportViewType rtnType = null;
            if (typeId == DATE_WISE.getId()) {
                rtnType = DATE_WISE;
            } else if (typeId == MONTH_WISE.getId()) {
                rtnType = MONTH_WISE;
            } else if (typeId == CONSULTANT_WISE.getId()) {
                rtnType = CONSULTANT_WISE;
            } else if (typeId == COLLECTION_CENTER_WISE.getId()) {
                rtnType = COLLECTION_CENTER_WISE;
            } else if (typeId == ORGANIZATION_WISE.getId()) {
                rtnType = ORGANIZATION_WISE;
            }
            return rtnType;
        }
    }

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }
        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 200);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 300);
        DmShared.isValidText(hashMapFilter.get("groupBy").getText(), "Group By", 300);
        if (DmShared.iNull(hashMapFilter.get("groupBy").getTag()).intValue() == ReportViewType.CONSULTANT_WISE.getId()) {
            DmShared.isValidText(hashMapFilter.get("consultantType").getText(), "Show Consultant", 100);
        }
    }
}
