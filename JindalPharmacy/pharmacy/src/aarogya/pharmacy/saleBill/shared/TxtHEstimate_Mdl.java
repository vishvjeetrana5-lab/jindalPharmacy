package aarogya.pharmacy.saleBill.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHEstimate_Mdl extends KeyField implements IsSerializable, Serializable {

    private String patient, estimateDate;
    private String departmentName, departmentCode;
    private String lmCode;
    private String lmName;
    private String lmManualCode;
    private String subCode;
    private String address;
    private String cityCode;
    private String cityName;
    private String stateCode;
    private String stateName;
    private String idTypeCode, idTypeName, idNo;
    private String mobile;
    private String partyType;
    private String regNo_HIS;
    private String uhidNo_HIS;
    private String regDocId_HIS;

    public TxtHEstimate_Mdl() {
    }

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public String getLmManualCode() {
        return lmManualCode;
    }

    public void setLmManualCode(String lmManualCode) {
        this.lmManualCode = lmManualCode;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getEstimateDate() {
        return estimateDate;
    }

    public void setEstimateDate(String estimateDate) {
        this.estimateDate = estimateDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getRegNo_HIS() {
        return regNo_HIS;
    }

    public void setRegNo_HIS(String regNo_HIS) {
        this.regNo_HIS = regNo_HIS;
    }

    public String getUhidNo_HIS() {
        return uhidNo_HIS;
    }

    public void setUhidNo_HIS(String uhidNo_HIS) {
        this.uhidNo_HIS = uhidNo_HIS;
    }

    public String getRegDocId_HIS() {
        return regDocId_HIS;
    }

    public void setRegDocId_HIS(String regDocId_HIS) {
        this.regDocId_HIS = regDocId_HIS;
    }
}
