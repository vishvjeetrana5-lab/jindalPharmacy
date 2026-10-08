package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptABCAnalysisRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }
        if (DmShared.vNull(hashMapFilter.get("aClass").getText()).doubleValue() == 0) {
            throw new DmException(40, "A Class is mandatory.");
        }
        if (DmShared.vNull(hashMapFilter.get("bClass").getText()).doubleValue() == 0) {
            throw new DmException(40, "B Class is mandatory.");
        }
        if (DmShared.vNull(hashMapFilter.get("aClass").getText()).doubleValue() > 100) {
            throw new DmException(40, "A Class is cannot be greater than 100.");
        }
        if (DmShared.vNull(hashMapFilter.get("bClass").getText()).doubleValue() > 100) {
            throw new DmException(40, "B Class cannot be greater than  100.");
        }
        if (DmShared.vNull(hashMapFilter.get("bClass").getText()).doubleValue() > DmShared.vNull(hashMapFilter.get("aClass").getText()).doubleValue()) {
            throw new DmException(40, "B Class cannot be greater than A Class.");
        }
        if (DmShared.xNull(hashMapFilter.get("analysisOn").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "Analysis On is mandatory.");
        }
        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 200);

    }

}
