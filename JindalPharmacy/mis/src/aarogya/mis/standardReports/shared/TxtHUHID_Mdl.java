package aarogya.mis.standardReports.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHUHID_Mdl extends KeyField implements Serializable {

    private String patient, guardian;
    private String mobile;
    private String displayDOB, gender;

    public TxtHUHID_Mdl() {
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
