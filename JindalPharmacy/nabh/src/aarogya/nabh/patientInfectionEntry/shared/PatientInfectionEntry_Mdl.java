package aarogya.nabh.patientInfectionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientInfectionEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vtType;
    private String regDocId;
    private String RegNo;
    private String uhid;
    private String patient;
    private String guardian;
    private String regType;
    private String address;
    private String displayDOB;
    private String gender;
    private String mobile;
    private String vDate;
    private String vTime;
    private String infectionType;
    private String serviceCode;
    private String serviceName;
    private String remark;
    private DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
    private List<GrdPatientInfectionEntry_Mdl> grdPatientInfection = new ArrayList<GrdPatientInfectionEntry_Mdl>();

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVtType() {
        return vtType;
    }

    public void setVtType(String vtType) {
        this.vtType = vtType;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public List<GrdPatientInfectionEntry_Mdl> getGrdPatientInfection() {
        return grdPatientInfection;
    }

    public void setGrdPatientInfection(List<GrdPatientInfectionEntry_Mdl> grdPatientInfection) {
        this.grdPatientInfection = grdPatientInfection;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String RegNo) {
        this.RegNo = RegNo;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getvTime() {
        return vTime;
    }

    public void setvTime(String vTime) {
        this.vTime = vTime;
    }

    public String getInfectionType() {
        return infectionType;
    }

    public void setInfectionType(String infectionType) {
        this.infectionType = infectionType;
    }

    public PatientInfectionEntry_Mdl() {

    }

    public DefaultValue_Mdl getDefaultValue() {
        return defaultValue;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayDOB() {
        return displayDOB;
    }

    public void setDisplayDOB(String displayDOB) {
        this.displayDOB = displayDOB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
