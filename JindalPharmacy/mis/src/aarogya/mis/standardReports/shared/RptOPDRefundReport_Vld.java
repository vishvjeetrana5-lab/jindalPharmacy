package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

public class RptOPDRefundReport_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        DmShared.isValidText(DmShared.dNull(hashMapFilter.get("fromDate").getText()), "From Date", 100);
        DmShared.isValidText(DmShared.dNull(hashMapFilter.get("toDate").getText()), "To Date", 100);

        if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
            throw new DmException(100, "From Date should be less than or equal to To Date.");
        }
    }

}
