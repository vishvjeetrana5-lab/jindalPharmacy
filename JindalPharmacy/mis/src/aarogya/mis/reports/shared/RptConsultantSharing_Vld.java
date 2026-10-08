package aarogya.mis.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.HashMap;

public class RptConsultantSharing_Vld {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }
        DmShared.isValidText(hashMapFilter.get("type").getTag(), "Type", 200);
        DmShared.isValidText(hashMapFilter.get("share").getTag(), "Sharing on", 300);
        DmShared.isValidText(hashMapFilter.get("amountNeeded").getText(), "Amount Needed", 300);
        DmShared.isValidText(hashMapFilter.get("paymentStatus").getText(), "Payment Status", 300);
    }
}
