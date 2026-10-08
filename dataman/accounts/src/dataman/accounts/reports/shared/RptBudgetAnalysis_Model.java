package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;

public class RptBudgetAnalysis_Model {
public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }
    }    
}
