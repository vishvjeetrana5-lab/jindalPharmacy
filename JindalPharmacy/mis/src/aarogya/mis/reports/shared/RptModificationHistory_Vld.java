
package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;


public class RptModificationHistory_Vld {
    
    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {

        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }

        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }

        if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
            throw new DmException(100, "From Date should be less than or equal to To Date.");
        }

//        if (DmShared.xNull(hashMapFilter.get("reportType").getText()).trim().equalsIgnoreCase("")) {
//            throw new DmException(100, "Report Type is mandatory ");
//        }

//        if ((!DmShared.xNull(hashMapFilter.get("fromTime").getText()).equalsIgnoreCase("")) && (!DmShared.xNull(hashMapFilter.get("toTime").getText()).equalsIgnoreCase(""))) {
//            if (DmShared.vNull(hashMapFilter.get("fromTime").getText()).doubleValue() > DmShared.vNull(hashMapFilter.get("toTime").getText()).doubleValue()) {
//                throw new DmException(300, "From Time should be less than or equal to To Times.");
//            }
//        }
    }
    
}
