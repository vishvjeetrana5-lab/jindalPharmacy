package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdServiceDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ipdDocId;
    private String ipdRecId;
    private String uhidDocId;
    private String uhidRecId;
    private String patientName;
    private String age;
    private String gender;
    private String admitDate;
    private String srvCode;
    private String srvName;
    private String orgCode;
    private String orgName;
    private String roomCatCode;
    private String roomCatName;
    private String serviceDocId;
    private String serviceVSNo;
    private String consultantCode;
    private String organizationServiceCode;
    private String amount;
    private String discountPer;
    private String discount;
    private String netAmount;
    private String discountGivenBy;
    private String discountReason;
    private String isChecked;

    public GrdServiceDetail_Mdl() {
    }

    public String getIpdDocId() {
        return ipdDocId;
    }

    public void setIpdDocId(String ipdDocId) {
        this.ipdDocId = ipdDocId;
    }

    public String getIpdRecId() {
        return ipdRecId;
    }

    public void setIpdRecId(String ipdRecId) {
        this.ipdRecId = ipdRecId;
    }

    public String getUhidDocId() {
        return uhidDocId;
    }

    public void setUhidDocId(String uhidDocId) {
        this.uhidDocId = uhidDocId;
    }

    public String getUhidRecId() {
        return uhidRecId;
    }

    public void setUhidRecId(String uhidRecId) {
        this.uhidRecId = uhidRecId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAdmitDate() {
        return admitDate;
    }

    public void setAdmitDate(String admitDate) {
        this.admitDate = admitDate;
    }

    public String getSrvCode() {
        return srvCode;
    }

    public void setSrvCode(String srvCode) {
        this.srvCode = srvCode;
    }

    public String getSrvName() {
        return srvName;
    }

    public void setSrvName(String srvName) {
        this.srvName = srvName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getRoomCatCode() {
        return roomCatCode;
    }

    public void setRoomCatCode(String roomCatCode) {
        this.roomCatCode = roomCatCode;
    }

    public String getRoomCatName() {
        return roomCatName;
    }

    public void setRoomCatName(String roomCatName) {
        this.roomCatName = roomCatName;
    }

    public String getServiceDocId() {
        return serviceDocId;
    }

    public void setServiceDocId(String serviceDocId) {
        this.serviceDocId = serviceDocId;
    }

    public String getServiceVSNo() {
        return serviceVSNo;
    }

    public void setServiceVSNo(String serviceVSNo) {
        this.serviceVSNo = serviceVSNo;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getOrganizationServiceCode() {
        return organizationServiceCode;
    }

    public void setOrganizationServiceCode(String organizationServiceCode) {
        this.organizationServiceCode = organizationServiceCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getDiscountGivenBy() {
        return discountGivenBy;
    }

    public void setDiscountGivenBy(String discountGivenBy) {
        this.discountGivenBy = discountGivenBy;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

}
