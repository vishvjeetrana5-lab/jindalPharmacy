
package aarogya.master.logConfiguration.shared;

import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmOPD;
import aarogya.projectMenu.self.shared.pmTPA;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EntryForm_Mdl extends KeyField implements Serializable,IsSerializable{
    
    private String module;

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
    
    

    public static HashMap<String,List<String>> addForms(){
            HashMap<String,List<String>> entryForms = new HashMap<>();
        
        //------------------------OPD Module-------------------------------------------------------
        List<String> lstOPDForms = new ArrayList<>();
        
        
        lstOPDForms.add(new pmOPD().OPD_RECEIPT.caption);
        lstOPDForms.add(new pmOPD().OPD_REFUND.caption);
        lstOPDForms.add(new pmOPD().OPD_SERVICES.caption);
        
        
        entryForms.put(new pmOPD().moduleName, lstOPDForms);
        
        //------------------------IPD Module-------------------------------------------------------
        
        List<String> lstIPDForms = new ArrayList<>();
        
        
        lstIPDForms.add(new pmIPD().IPD_RECEIPT.caption);
        lstIPDForms.add(new pmIPD().IPD_REFUND.caption);
        lstIPDForms.add(new pmIPD().IPD_BILL_NON_TPA.caption);
        lstIPDForms.add(new pmIPD().UPDATE_PATIENT_DETAIL.caption);
        
        entryForms.put(new pmIPD().moduleName, lstIPDForms);
        
        //------------------------TPA Module-------------------------------------------------------
        List<String> lstTPAForms = new ArrayList<>();
        
        
        lstTPAForms.add(new pmTPA().IPD_BILL_TPA.caption);
        
        entryForms.put(new pmTPA().moduleName, lstTPAForms);
        //------------------------Diagnostic Module-------------------------------------------------------
        List<String> lstDiagnosticForms = new ArrayList<>();
        
        
        lstDiagnosticForms.add(new pmDiagnostic().DIAGNOSTIC_RECEIPT.caption);
        lstDiagnosticForms.add(new pmDiagnostic().DIAGNOSTIC_REFUND.caption);
        lstDiagnosticForms.add(new pmDiagnostic().LAB_REGISTRATION.caption);
        
        entryForms.put(new pmDiagnostic().moduleName, lstDiagnosticForms);
        
        return entryForms;
    }
    
    
    
    
}
