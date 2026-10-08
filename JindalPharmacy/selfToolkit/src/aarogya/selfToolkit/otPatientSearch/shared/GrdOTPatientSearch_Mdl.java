package aarogya.selfToolkit.otPatientSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdOTPatientSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String  patient, guardian, mobile, organizationName, departmentName, docIdIPD, recIdIPD, docIdUHID, recIdUHID, surgicalNo, surgicalDocId;

    public GrdOTPatientSearch_Mdl() {
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDocIdIPD() {
        return docIdIPD;
    }

    public void setDocIdIPD(String docIdIPD) {
        this.docIdIPD = docIdIPD;
    }

    public String getRecIdIPD() {
        return recIdIPD;
    }

    public void setRecIdIPD(String recIdIPD) {
        this.recIdIPD = recIdIPD;
    }

    public String getDocIdUHID() {
        return docIdUHID;
    }

    public void setDocIdUHID(String docIdUHID) {
        this.docIdUHID = docIdUHID;
    }

    public String getRecIdUHID() {
        return recIdUHID;
    }

    public void setRecIdUHID(String recIdUHID) {
        this.recIdUHID = recIdUHID;
    }

    public String getSurgicalNo() {
        return surgicalNo;
    }

    public void setSurgicalNo(String surgicalNo) {
        this.surgicalNo = surgicalNo;
    }

    public String getSurgicalDocId() {
        return surgicalDocId;
    }

    public void setSurgicalDocId(String surgicalDocId) {
        this.surgicalDocId = surgicalDocId;
    }

}
