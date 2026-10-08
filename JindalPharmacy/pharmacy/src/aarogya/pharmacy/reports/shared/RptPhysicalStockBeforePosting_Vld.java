package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

public class RptPhysicalStockBeforePosting_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("reportType").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "Report Type is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("postingDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "Posting Date is mandatory.");
        }

    }

}
