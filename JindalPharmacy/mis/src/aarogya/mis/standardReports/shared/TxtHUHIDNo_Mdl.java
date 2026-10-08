package aarogya.mis.standardReports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHUHIDNo_Mdl extends KeyField implements Serializable, IsSerializable {

    private String patient;
    private String guardian;
    private String organization;
    private String dobDisplay;
    private String gender;

    public TxtHUHIDNo_Mdl() {
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getDobDisplay() {
        return dobDisplay;
    }

    public void setDobDisplay(String dobDisplay) {
        this.dobDisplay = dobDisplay;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
