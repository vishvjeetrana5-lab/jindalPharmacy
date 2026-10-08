/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.reports.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.LinkedHashMap;

/**
 *
 * @author dataman
 */
public class RptGroupWisePurchaseRegister_Vld {

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {

        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }

        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 200);
        DmShared.isValidText(hashMapFilter.get("group1").getTag(), "Group By (First)", 200);
//        DmShared.isValidText(hashMapFilter.get("group2").getTag(), "Group By (Second)", 200);

        if (hashMapFilter.get("group1").getTag().equalsIgnoreCase(hashMapFilter.get("group2").getTag())) {
            throw new DmException(100, "Group By (First) and  Group By (Second) cannot be same.");
        }
    }

}
