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
public class RptPharmacyStkReport_Vld {
    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
     if (DmShared.xNull(hashMapFilter.get("site").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Site is mandatory.");
        }

//        if (DmShared.xNull(hashMapFilter.get("studentName").getText()).equalsIgnoreCase("")) {
//            throw new DmException(100, "Student is mandatory.");
//        }

      
    }
    
    
    
}
