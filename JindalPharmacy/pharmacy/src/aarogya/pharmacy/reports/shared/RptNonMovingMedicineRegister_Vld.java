package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptNonMovingMedicineRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter, String fromDate) {
        if (DmShared.dNull(fromDate).equalsIgnoreCase("")) {
            throw new DmException(20, "Non Moving Days is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }
        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 200);

        DmShared.isValidText(hashMapFilter.get("valueOn").getTag(), "Value On", 200);

    }

}
