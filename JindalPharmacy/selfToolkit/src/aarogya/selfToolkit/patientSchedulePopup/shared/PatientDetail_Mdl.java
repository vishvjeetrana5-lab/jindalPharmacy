package aarogya.selfToolkit.patientSchedulePopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PatientDetail_Mdl implements Serializable, IsSerializable {

    private String recIdUHID;
    private String docIdUHID;
    private String patient;
    private String guardian;
    private String displayDOB;
    private String gender;
    private String mobile;

    public PatientDetail_Mdl() {
    }

    public String getRecIdUHID() {
        return recIdUHID;
    }

    public void setRecIdUHID(String recIdUHID) {
        this.recIdUHID = recIdUHID;
    }

    public String getDocIdUHID() {
        return docIdUHID;
    }

    public void setDocIdUHID(String docIdUHID) {
        this.docIdUHID = docIdUHID;
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
