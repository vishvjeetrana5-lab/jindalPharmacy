package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.HashMap;

public class RptAreaWiseConsultation_Vld {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);

        if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
            throw new DmException(200, "From Date should be less than or equal to To Date.");
        }

        if (DmShared.xNull(hashMapFilter.get("reportType").getText()).trim().equalsIgnoreCase("")) {
            throw new DmException(100, "Type is mandatory ");
        }
    }
}
