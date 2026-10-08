package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PatientDetail_Mdl implements Serializable, IsSerializable {

    private String recIdUHID;
    private String docIdUHID;
    private String recIdOPD;
    private String docIdOPD;
    private String patient;
    private String guardian;
    private String address;
    private String displayDOB;
    private String gender;
    private String mobile;
    private String caseText;
    private String organizationTag;
    private String organizationText;
    private String referredText;
    private String consultantText;

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

    public String getRecIdOPD() {
        return recIdOPD;
    }

    public void setRecIdOPD(String recIdOPD) {
        this.recIdOPD = recIdOPD;
    }

    public String getDocIdOPD() {
        return docIdOPD;
    }

    public void setDocIdOPD(String docIdOPD) {
        this.docIdOPD = docIdOPD;
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

    public String getCaseText() {
        return caseText;
    }

    public void setCaseText(String caseText) {
        this.caseText = caseText;
    }

    public String getOrganizationTag() {
        return organizationTag;
    }

    public void setOrganizationTag(String organizationTag) {
        this.organizationTag = organizationTag;
    }

    public String getOrganizationText() {
        return organizationText;
    }

    public void setOrganizationText(String organizationText) {
        this.organizationText = organizationText;
    }

    public String getReferredText() {
        return referredText;
    }

    public void setReferredText(String referredText) {
        this.referredText = referredText;
    }

    public String getConsultantText() {
        return consultantText;
    }

    public void setConsultantText(String consultantText) {
        this.consultantText = consultantText;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
