package aarogya.pharmacy.physicalStockClearance.shared;

import dataman.dmBase.self.shared.*;

public class TxtHDateOn_Mdl extends KeyField {

    //sharedKeyFieldCode = departmentCode
    //sharedKeyFieldName = onDate
    private String departmentCode;
    private String departmentName;
    private String filterTypeCode;
    private String filterTypeName;
    private String filterValueCode;
    private String filterValueName;

    public TxtHDateOn_Mdl() {
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getFilterTypeCode() {
        return filterTypeCode;
    }

    public void setFilterTypeCode(String filterTypeCode) {
        this.filterTypeCode = filterTypeCode;
    }

    public String getFilterTypeName() {
        return filterTypeName;
    }

    public void setFilterTypeName(String filterTypeName) {
        this.filterTypeName = filterTypeName;
    }

    public String getFilterValueCode() {
        return filterValueCode;
    }

    public void setFilterValueCode(String filterValueCode) {
        this.filterValueCode = filterValueCode;
    }

    public String getFilterValueName() {
        return filterValueName;
    }

    public void setFilterValueName(String filterValueName) {
        this.filterValueName = filterValueName;
    }

}
