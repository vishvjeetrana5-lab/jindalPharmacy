package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptStockSummary_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
          DmShared.isValidText(hashMapFilter.get("group1").getTag(), "Group By (First)", 200);
          DmShared.isValidText(hashMapFilter.get("group2").getTag(), "Group By (Second)", 300);
        
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("openingInclude").getTag()).trim().equalsIgnoreCase("")) {
            throw new DmException(40, "Opening Include is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }
        if (DmShared.xNull(hashMapFilter.get("valueOn").getTag()).equalsIgnoreCase("")) {
            throw new DmException(200, "Value On is mandatory.");
        }
        
        
    }

}
