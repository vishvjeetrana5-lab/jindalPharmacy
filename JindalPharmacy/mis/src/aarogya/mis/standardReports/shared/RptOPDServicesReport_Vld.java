
package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.HashMap;


public class RptOPDServicesReport_Vld {
    
     public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {

        DmShared.isValidText(hashMapFilter.get("fromDate").getText(), "From Date", 100);
        DmShared.isValidText(hashMapFilter.get("toDate").getText(), "To Date", 200);
        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(200, "From Date should be less than or equal to To Date.");
            }
        }
        if ((!DmShared.xNull(hashMapFilter.get("fromTime").getText()).equalsIgnoreCase("")) && (!DmShared.xNull(hashMapFilter.get("toTime").getText()).equalsIgnoreCase(""))) {
            if (DmShared.vNull(hashMapFilter.get("fromTime").getText()).doubleValue() > DmShared.vNull(hashMapFilter.get("toTime").getText()).doubleValue()) {
                throw new DmException(300, "From Time should be less than or equal to To Times.");
            }
        }
    }
     
     public static enum ReportGroup {
        Consultant_Wise("Consultant Wise"), User_Wise("User Wise");

        private String text;

        public String getText() {
            return text;
        }

        ReportGroup(String text) {
            this.text = text;
        }

        public static ReportGroup getByText(String text) {
            ReportGroup rtnValue = null;
             if (DmShared.xNull(text).equalsIgnoreCase(ReportGroup.Consultant_Wise.getText())) {
                rtnValue = Consultant_Wise;
            } else if (DmShared.xNull(text).equalsIgnoreCase(ReportGroup.User_Wise.getText())) {
                rtnValue = User_Wise;
            }
            
            return rtnValue;
        }
    }
    
}
