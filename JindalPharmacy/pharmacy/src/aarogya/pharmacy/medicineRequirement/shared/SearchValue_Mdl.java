package aarogya.pharmacy.medicineRequirement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String asOnDate;
    private String movementFrom;
    private String departmentCode;
    private String departmentName;
    private String companyCode;
    private String companyName;
    private String groupCode;
    private String groupName;
    private String categoryCode;
    private String categoryName;
    private String medicineCode;
    private String medicineName;
    private String showHold;
    private String fieldName;
    private String fieldNameValue;
    private String groupOnName;
    private String groupOnCode;

    public SearchValue_Mdl() {
    }

    public String getGroupOnName() {
        return groupOnName;
    }

    public void setGroupOnName(String groupOnName) {
        this.groupOnName = groupOnName;
    }

    public String getGroupOnCode() {
        return groupOnCode;
    }

    public void setGroupOnCode(String groupOnCode) {
        this.groupOnCode = groupOnCode;
    }

    public String getAsOnDate() {
        return asOnDate;
    }

    public void setAsOnDate(String asOnDate) {
        this.asOnDate = asOnDate;
    }

    public String getMovementFrom() {
        return movementFrom;
    }

    public void setMovementFrom(String movementFrom) {
        this.movementFrom = movementFrom;
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

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getShowHold() {
        return showHold;
    }

    public void setShowHold(String showHold) {
        this.showHold = showHold;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameValue() {
        return fieldNameValue;
    }

    public void setFieldNameValue(String fieldNameValue) {
        this.fieldNameValue = fieldNameValue;
    }

}
