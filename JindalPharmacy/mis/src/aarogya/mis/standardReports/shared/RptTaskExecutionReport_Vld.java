package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;

public class RptTaskExecutionReport_Vld {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);
        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(200, "From Date should be less than or equal to To Date.");
            }
        }
        if ((!DmShared.xNull(hashMapFilter.get("fromTime").getText()).equalsIgnoreCase("")) && (!DmShared.xNull(hashMapFilter.get("toTime").getText()).equalsIgnoreCase(""))) {

            if (DmShared.vNull(hashMapFilter.get("fromTime").getText()).doubleValue() > DmShared.vNull(hashMapFilter.get("toTime").getText()).doubleValue()) {
                throw new DmException(300, "From Time should be less than or equal to To Times.");
            }

        }

        if (!DmShared.xNull(hashMapFilter.get("fromTime").getTag()).equalsIgnoreCase("")) {
            DmShared.isValidText(hashMapFilter.get("toTime").getText(), "To Time", 400);
        }

        if (!DmShared.xNull(hashMapFilter.get("toTime").getTag()).equalsIgnoreCase("")) {
            DmShared.isValidText(hashMapFilter.get("fromTime").getText(), "From Time", 500);
        }

    }

}
