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

public class AgeingAnalysisFIFO_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("upToDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Up To Date is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("showRecords").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Show Records is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("reportOnChoice").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Report on Choice is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("iInterval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "I Interval is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("iiInterval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "II Interval is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("iiiInterval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "III Interval is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("ivInterval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "IV Interval is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("vInterval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "V Interval is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("viInterval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "VI Interval is mandatory.");
        }
    }
}
