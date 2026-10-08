package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptMedicineOpeningRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("openingDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "Opening Date is mandatory.");
        }

        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 200);
    }

}
