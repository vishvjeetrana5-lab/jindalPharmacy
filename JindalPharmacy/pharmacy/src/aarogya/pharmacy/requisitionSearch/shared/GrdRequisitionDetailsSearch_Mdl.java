package aarogya.pharmacy.requisitionSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRequisitionDetailsSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    //sharedkeyFieldCode = reqDocId
    //sharedkeyFieldName = reqRecId
    private String voucherSNo; // reqV_SNo
    private String quantity;
    private String sku;
    private String skuName;
    private String mediName;
    private String mediCode;
    private String remark;
    private String departmentText;
    private String requisitionDate;
    private String requisitionBy;
    private String isChecked;
    private String isCancelled;
    private String itsBasedOn;
    private String itemPacking;
    private String approvedby;
    private String status;
    private String patientName;
    private String priority;
    private String priorityColor;
    private String patientRegDocID;
    private String reqTime;
    private String patientUHID;
    private String patientRegID;
    private String departmentFromName;
    private String entryDate;
    private String dateOfAdmission;
    private String partialPending;

    public GrdRequisitionDetailsSearch_Mdl() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprovedby() {
        return approvedby;
    }

    public void setApprovedby(String approvedby) {
        this.approvedby = approvedby;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public String getVoucherSNo() {
        return voucherSNo;
    }

    public void setVoucherSNo(String voucherSNo) {
        this.voucherSNo = voucherSNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getMediName() {
        return mediName;
    }

    public void setMediName(String mediName) {
        this.mediName = mediName;
    }

    public String getRequisitionDate() {
        return requisitionDate;
    }

    public void setRequisitionDate(String requisitionDate) {
        this.requisitionDate = requisitionDate;
    }

    public String getRequisitionBy() {
        return requisitionBy;
    }

    public void setRequisitionBy(String requisitionBy) {
        this.requisitionBy = requisitionBy;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getMediCode() {
        return mediCode;
    }

    public void setMediCode(String mediCode) {
        this.mediCode = mediCode;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getItemPacking() {
        return itemPacking;
    }

    public void setItemPacking(String itemPacking) {
        this.itemPacking = itemPacking;
    }

    public String getPriority() {
        return priority;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public String getPatientRegDocID() {
        return patientRegDocID;
    }

    public void setPatientRegDocID(String patientRegDocID) {
        this.patientRegDocID = patientRegDocID;
    }

    public String getReqTime() {
        return reqTime;
    }

    public void setReqTime(String reqTime) {
        this.reqTime = reqTime;
    }

    public String getPatientUHID() {
        return patientUHID;
    }

    public void setPatientUHID(String patientUHID) {
        this.patientUHID = patientUHID;
    }

    public String getPatientRegID() {
        return patientRegID;
    }

    public void setPatientRegID(String patientRegID) {
        this.patientRegID = patientRegID;
    }

    public String getDepartmentFromName() {
        return departmentFromName;
    }

    public void setDepartmentFromName(String departmentFromName) {
        this.departmentFromName = departmentFromName;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDateOfAdmission() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        this.dateOfAdmission = dateOfAdmission;
    }

    public String getPartialPending() {
        return partialPending;
    }

    public void setPartialPending(String partialPending) {
        this.partialPending = partialPending;
    }

}
