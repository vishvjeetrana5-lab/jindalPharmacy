package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.HashMap;

public class RptConsultationReport_Vld {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);
        DmShared.isValidText(hashMapFilter.get("opdType").getText(), "Opd Type", 300);
        DmShared.isValidText(hashMapFilter.get("time").getText(), "Time Wise", 400);
        DmShared.isValidText(hashMapFilter.get("age").getText(), "Age Wise", 500);

        if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
            throw new DmException(200, "From Date should be less than or equal to To Date.");
        }

        if (DmShared.xNull(hashMapFilter.get("time").getTag()).equalsIgnoreCase("Y")) {
            DmShared.isValidText(hashMapFilter.get("fromTime").getText(), "From Time", 100);
            DmShared.isValidText(hashMapFilter.get("toTime").getText(), "To Time", 100);
        }

        if (DmShared.xNull(hashMapFilter.get("age").getTag()).equalsIgnoreCase("Y")) {
            DmShared.isValidText(hashMapFilter.get("fromAge").getText(), "From Age", 100);
            DmShared.isValidText(hashMapFilter.get("toAge").getText(), "To Age", 100);
        }
    }
}
