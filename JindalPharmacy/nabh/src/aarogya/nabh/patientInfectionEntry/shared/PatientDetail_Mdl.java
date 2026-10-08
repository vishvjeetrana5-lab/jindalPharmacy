package aarogya.nabh.patientInfectionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PatientDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String recIdUHID;
    private String docIdUHID;
    private String recIdRegistration;
    private String docIdRegistration;

    private String patient;
    private String guardian;
    private String address;

    private String gender;
    private String age;
    private List<GrdPatientInfectionEntry_Mdl> grdPatientInfection = new ArrayList<GrdPatientInfectionEntry_Mdl>();

    public List<GrdPatientInfectionEntry_Mdl> getGrdPatientInfection() {
        return grdPatientInfection;
    }

    public void setGrdPatientInfection(List<GrdPatientInfectionEntry_Mdl> grdPatientInfection) {
        this.grdPatientInfection = grdPatientInfection;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
    private String mobile;

    private DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();

    public PatientDetail_Mdl() {

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

    public String getRecIdRegistration() {
        return recIdRegistration;
    }

    public void setRecIdRegistration(String recIdRegistration) {
        this.recIdRegistration = recIdRegistration;
    }

    public String getDocIdRegistration() {
        return docIdRegistration;
    }

    public void setDocIdRegistration(String docIdRegistration) {
        this.docIdRegistration = docIdRegistration;
    }

}
