package aarogya.pharmacy.wholeSaleReports.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

public class CustomerCreditLimitBalanceRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {

        if (DmShared.dNull(hashMapFilter.get("asOnDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "As On Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("creditLimitExist").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "Credit Limit is mandatory.");
        }
    }

}
