package aarogya.pharmacy.medicineRequisitionDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class MedicineRequisitionDisplay_Mdl extends KeyField implements Serializable, IsSerializable {

    private String V_No;
    private String requisitionByName;
    private String requisitionByCode;
    private String departmentCode, departmentToCode;
    private String departmentName, departmentToName;
    private String date;
    private String vCat;
    private String v_SNo;
    private String itemName;
    private String itemCode;
    private String itemDocumentQty;
    private String itemQty;
    private String unitName;
    private String unitCode;
    private String reqDate;
    private String remark;
    private String action;
    private String check;
    private String reqTime;
    private String isStatus;
    private String priorityColor;
    private String totalReqQty;
    private String currentStk;
    private String v_Type;
    private String reqDateTime;
    private String regNo;
    private String uhid;
    private String patientName;
    private String isChecked;
    private String skuName;
    private String skuCode;
    private String unitDecimalPlaces;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getReqDateTime() {
        return reqDateTime;
    }

    public void setReqDateTime(String reqDateTime) {
        this.reqDateTime = reqDateTime;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTotalReqQty() {
        return totalReqQty;
    }

    public void setTotalReqQty(String totalReqQty) {
        this.totalReqQty = totalReqQty;
    }

    public String getCurrentStk() {
        return currentStk;
    }

    public void setCurrentStk(String currentStk) {
        this.currentStk = currentStk;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getV_No() {
        return V_No;
    }

    public void setV_No(String V_No) {
        this.V_No = V_No;
    }

    public String getRequisitionByName() {
        return requisitionByName;
    }

    public void setRequisitionByName(String requisitionByName) {
        this.requisitionByName = requisitionByName;
    }

    public String getRequisitionByCode() {
        return requisitionByCode;
    }

    public void setRequisitionByCode(String requisitionByCode) {
        this.requisitionByCode = requisitionByCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentToCode() {
        return departmentToCode;
    }

    public void setDepartmentToCode(String departmentToCode) {
        this.departmentToCode = departmentToCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentToName() {
        return departmentToName;
    }

    public void setDepartmentToName(String departmentToName) {
        this.departmentToName = departmentToName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getvCat() {
        return vCat;
    }

    public void setvCat(String vCat) {
        this.vCat = vCat;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getUnitDecimalPlaces() {
        return unitDecimalPlaces;
    }

    public void setUnitDecimalPlaces(String unitDecimalPlaces) {
        this.unitDecimalPlaces = unitDecimalPlaces;
    }

    public String getItemDocumentQty() {
        return itemDocumentQty;
    }

    public void setItemDocumentQty(String itemDocumentQty) {
        this.itemDocumentQty = itemDocumentQty;
    }

}
