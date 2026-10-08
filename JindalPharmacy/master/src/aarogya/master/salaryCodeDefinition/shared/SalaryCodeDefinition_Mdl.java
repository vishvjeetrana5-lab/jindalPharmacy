package aarogya.master.salaryCodeDefinition.shared;

import dataman.dmBase.self.shared.KeyField;

public class SalaryCodeDefinition_Mdl extends KeyField {

    private String fromDate, toDate; 
   

    public SalaryCodeDefinition_Mdl() {
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }


}
