package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptCollectionRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {

        if (DmShared.xNull(hashMapFilter.get("dateApplicable").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Date Applicable is mandatory");

        }
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory");
        }

        if (!DmShared.xNull(hashMapFilter.get("fromTime").getText()).equalsIgnoreCase("") && DmShared.xNull(hashMapFilter.get("toTime").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Time is mandatory");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }

        DmShared.isValidText(hashMapFilter.get("type").getTag(), "Report Type", 200);

        if ((!DmShared.xNull(hashMapFilter.get("fromTime").getText()).equalsIgnoreCase("")) && (!DmShared.xNull(hashMapFilter.get("toTime").getText()).equalsIgnoreCase(""))) {

            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() == DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                if (DmShared.vNull(hashMapFilter.get("fromTime").getText()).doubleValue() > DmShared.vNull(hashMapFilter.get("toTime").getText()).doubleValue()) {
                    throw new DmException(300, "For same Date 'From Time' should be less than or equal to 'To Time'.");
                }
            }

        }

//        
//        if(DmShared.xNull(hashMapFilter.get("dateApplicable").getText()).equalsIgnoreCase("Entry On")){
//            if(DmShared.xNull(hashMapFilter.get("fromTime").getText()).equalsIgnoreCase("") || DmShared.xNull(hashMapFilter.get("toTime").getText()).equalsIgnoreCase(""))
//            {
//                 throw new DmException(300, "From Time and To Time is mandatory");
//                }
//       
//        }
        //DmShared.isValidAmount(hashMapFilter.get("reportView").getTag(), "Report View", 200);
    }

}
