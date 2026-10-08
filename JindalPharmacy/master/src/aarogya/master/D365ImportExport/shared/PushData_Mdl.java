package aarogya.master.D365ImportExport.shared;

import dataman.dmBase.self.shared.KeyField;

public class PushData_Mdl extends KeyField {

    private String departementcode;
    private String departementName;
    private String netQuantity;
    private String fromDate;
    private String toDate;

    public PushData_Mdl() {
    }

    public String getDepartementcode() {
        return departementcode;
    }

    public void setDepartementcode(String departementcode) {
        this.departementcode = departementcode;
    }

    public String getDepartementName() {
        return departementName;
    }

    public void setDepartementName(String departementName) {
        this.departementName = departementName;
    }

    public String getNetQuantity() {
        return netQuantity;
    }

    public void setNetQuantity(String netQuantity) {
        this.netQuantity = netQuantity;
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
