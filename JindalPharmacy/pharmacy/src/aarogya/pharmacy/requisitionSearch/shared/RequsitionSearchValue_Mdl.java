package aarogya.pharmacy.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class RequsitionSearchValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_Date;
    private String itemCode;
    private String voucherCategory;
    private String patientRegDocId;
    private String formDocId;
    private String reqDocId;
    private String departmentTag;
    private String departmentText;
    private String fieldName;
    private String fieldNameValue;
    private String priorityNameValue;
    private Boolean requisitionWithoutUHID;
    private String fromDepartmentTag;
    private String fromDepartmentText;

    public RequsitionSearchValue_Mdl() {
    }

    public String getReqDocId() {
        return reqDocId;
    }

    public void setReqDocId(String reqDocId) {
        this.reqDocId = reqDocId;
    }

    public String getPatientRegDocId() {
        return patientRegDocId;
    }

    public void setPatientRegDocId(String patientRegDocId) {
        this.patientRegDocId = patientRegDocId;
    }

    public String getFormDocId() {
        return formDocId;
    }

    public void setFormDocId(String formDocId) {
        this.formDocId = formDocId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
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

    public String getPriorityNameValue() {
        return priorityNameValue;
    }

    public void setPriorityNameValue(String priorityNameValue) {
        this.priorityNameValue = priorityNameValue;
    }

    public Boolean getRequisitionWithoutUHID() {
        return requisitionWithoutUHID;
    }

    public void setRequisitionWithoutUHID(Boolean requisitionWithoutUHID) {
        this.requisitionWithoutUHID = requisitionWithoutUHID;
    }

    public String getFromDepartmentTag() {
        return fromDepartmentTag;
    }

    public void setFromDepartmentTag(String fromDepartmentTag) {
        this.fromDepartmentTag = fromDepartmentTag;
    }

    public String getFromDepartmentText() {
        return fromDepartmentText;
    }

    public void setFromDepartmentText(String fromDepartmentText) {
        this.fromDepartmentText = fromDepartmentText;
    }

}
