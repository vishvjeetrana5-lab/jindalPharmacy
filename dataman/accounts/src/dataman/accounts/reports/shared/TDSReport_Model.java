package dataman.accounts.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TDSReport_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("seperatePage").getTag()).equalsIgnoreCase("")) {
            throw new DmException(100, "Seperate Page is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("partyWise").getTag()).equalsIgnoreCase("")) {
            throw new DmException(100, "Party Wise is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("withNarration").getTag()).equalsIgnoreCase("")) {
            throw new DmException(100, "With Narration is mandatory.");
        }

    }
}
