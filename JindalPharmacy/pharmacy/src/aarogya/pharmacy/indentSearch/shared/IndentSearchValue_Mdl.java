package aarogya.pharmacy.indentSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class IndentSearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromDate;
    private String toDate;
    private String departmentTag;
    private String departmentText;
    private String fieldName;
    private String fieldNameValue;
    
    private String medicineCategoryTag;
    private String medicineCategoryText;
    private String medicineGroupTag;
    private String medicineGroupText;
    private String medicineMakeTag;
    private String medicineMakeText;
    private String medicineTag;
    private String medicineText;
    private String medicineDivisionTag;
    private String medicineDivisionText;
    private String marketingCompanyTag;
    private String marketingCompanyText;

    public String getMedicineCategoryTag() {
        return medicineCategoryTag;
    }

    public void setMedicineCategoryTag(String medicineCategoryTag) {
        this.medicineCategoryTag = medicineCategoryTag;
    }

    public String getMedicineCategoryText() {
        return medicineCategoryText;
    }

    public void setMedicineCategoryText(String medicineCategoryText) {
        this.medicineCategoryText = medicineCategoryText;
    }

    public String getMedicineGroupTag() {
        return medicineGroupTag;
    }

    public void setMedicineGroupTag(String medicineGroupTag) {
        this.medicineGroupTag = medicineGroupTag;
    }

    public String getMedicineGroupText() {
        return medicineGroupText;
    }

    public void setMedicineGroupText(String medicineGroupText) {
        this.medicineGroupText = medicineGroupText;
    }

    public String getMedicineMakeTag() {
        return medicineMakeTag;
    }

    public void setMedicineMakeTag(String medicineMakeTag) {
        this.medicineMakeTag = medicineMakeTag;
    }

    public String getMedicineMakeText() {
        return medicineMakeText;
    }

    public void setMedicineMakeText(String medicineMakeText) {
        this.medicineMakeText = medicineMakeText;
    }

    public String getMedicineTag() {
        return medicineTag;
    }

    public void setMedicineTag(String medicineTag) {
        this.medicineTag = medicineTag;
    }

    public String getMedicineText() {
        return medicineText;
    }

    public void setMedicineText(String medicineText) {
        this.medicineText = medicineText;
    }

    public String getMedicineDivisionTag() {
        return medicineDivisionTag;
    }

    public void setMedicineDivisionTag(String medicineDivisionTag) {
        this.medicineDivisionTag = medicineDivisionTag;
    }

    public String getMedicineDivisionText() {
        return medicineDivisionText;
    }

    public void setMedicineDivisionText(String medicineDivisionText) {
        this.medicineDivisionText = medicineDivisionText;
    }

    public String getMarketingCompanyTag() {
        return marketingCompanyTag;
    }

    public void setMarketingCompanyTag(String marketingCompanyTag) {
        this.marketingCompanyTag = marketingCompanyTag;
    }

    public String getMarketingCompanyText() {
        return marketingCompanyText;
    }

    public void setMarketingCompanyText(String marketingCompanyText) {
        this.marketingCompanyText = marketingCompanyText;
    }

    public IndentSearchValue_Mdl() {
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

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
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
