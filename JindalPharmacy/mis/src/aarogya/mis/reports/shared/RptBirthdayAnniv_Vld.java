package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.HashMap;

public class RptBirthdayAnniv_Vld {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);
        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(200, "From Date should be less than or equal to To Date.");
            }
        }
    }
}
