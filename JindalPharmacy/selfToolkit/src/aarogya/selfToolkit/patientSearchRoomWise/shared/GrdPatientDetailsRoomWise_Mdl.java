package aarogya.selfToolkit.patientSearchRoomWise.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPatientDetailsRoomWise_Mdl extends KeyField implements Serializable, IsSerializable {

    private String dtIPD, patient, guardian, mobile, organizationName, departmentName, docIdIPD, recIdIPD, docIdUHID, recIdUHID;

    public GrdPatientDetailsRoomWise_Mdl() {
    }

    public String getDtIPD() {
        return dtIPD;
    }

    public void setDtIPD(String dtIPD) {
        this.dtIPD = dtIPD;
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

}
