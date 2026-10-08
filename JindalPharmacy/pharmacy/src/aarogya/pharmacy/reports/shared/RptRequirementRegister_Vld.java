package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.LinkedHashMap;

public class RptRequirementRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("asOnDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "As On Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("movementFrom").getText()).equalsIgnoreCase("")) {
            throw new DmException(50, "Movement From is mandatory.");
        }
    }

}
