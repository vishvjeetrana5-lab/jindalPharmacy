package aarogya.nabh.nabhNavigation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NABHNavigation_Mdl extends KeyField implements Serializable, IsSerializable {

    private String recIdUHID;
    private String docIdUHID;
    private String patient;
    private String guardian;
    private String address;
    private String displayDOB;
    private String gender;
    private String mobile;
    private String caseText;
    private String organizationTag;
    private String organizationText;
    private String consultantText;
    private String ConsultantTag;
    private String age;
    private String regDate;
    private String regTime;
    private String daysOfOPD;
    private String typeTag;
    private String typeText;

    private List<GrdNABHNavigation_Mdl> lstNabhNavigation_Mdl = new ArrayList<GrdNABHNavigation_Mdl>();

    public NABHNavigation_Mdl() {
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

    public String getConsultantText() {
        return consultantText;
    }

    public void setConsultantText(String consultantText) {
        this.consultantText = consultantText;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public String getDaysOfOPD() {
        return daysOfOPD;
    }

    public void setDaysOfOPD(String daysOfOPD) {
        this.daysOfOPD = daysOfOPD;
    }

    public String getConsultantTag() {
        return ConsultantTag;
    }

    public void setConsultantTag(String ConsultantTag) {
        this.ConsultantTag = ConsultantTag;
    }

    public List<GrdNABHNavigation_Mdl> getLstNabhNavigation_Mdl() {
        return lstNabhNavigation_Mdl;
    }

    public String getTypeTag() {
        return typeTag;
    }

    public void setTypeTag(String typeTag) {
        this.typeTag = typeTag;
    }

    public String getTypeText() {
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

}
