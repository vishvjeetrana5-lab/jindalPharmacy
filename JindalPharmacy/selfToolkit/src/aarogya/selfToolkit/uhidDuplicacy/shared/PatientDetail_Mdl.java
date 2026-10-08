package aarogya.selfToolkit.uhidDuplicacy.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmSnapshot.shared.DmSnapshot_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PatientDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String patientName;
    private String guardianName;
    private String voucherDate;
    private String maritalStatus;
    private String address1;
    private String address2;
    private String cityName;
    private String areaName;
    private String mobile;
    private String email;
    private String idType;
    private String idNo;
    private DmSnapshot_Mdl snapshot_Mdl = new DmSnapshot_Mdl();

    public PatientDetail_Mdl() {
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public DmSnapshot_Mdl getSnapshot_Mdl() {
        return snapshot_Mdl;
    }

    public void setSnapshot_Mdl(DmSnapshot_Mdl snapshot_Mdl) {
        this.snapshot_Mdl = snapshot_Mdl;
    }

}
