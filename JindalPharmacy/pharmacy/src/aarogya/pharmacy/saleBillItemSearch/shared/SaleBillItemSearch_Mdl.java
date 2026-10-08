package aarogya.pharmacy.saleBillItemSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class SaleBillItemSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_Date;
    private String saleBillDocId;
    private String voucherCategory;
    private String voucherType;
    private String patientRegDocId;
    private String returnDays;
    private String formDocId;
    private String departmentTag;
    private String departmentText;
    private String lmCode;
    private String fieldName;
    private String fieldNameValue;

    public SaleBillItemSearch_Mdl() {
    }

    public String getReturnDays() {
        return returnDays;
    }

    public void setReturnDays(String returnDays) {
        this.returnDays = returnDays;
    }

    public String getSaleBillDocId() {
        return saleBillDocId;
    }

    public void setSaleBillDocId(String saleBillDocId) {
        this.saleBillDocId = saleBillDocId;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
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

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

}
