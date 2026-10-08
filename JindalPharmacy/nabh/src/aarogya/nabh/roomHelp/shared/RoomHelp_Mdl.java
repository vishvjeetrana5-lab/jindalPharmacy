package aarogya.nabh.roomHelp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RoomHelp_Mdl extends KeyField implements Serializable, IsSerializable {

    private String roomCode;
    private String roomNo;
    private String voucherTypeTag;
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

    private List<GrdRoomHelp_Mdl> grdGUIEntry_Mdl = new ArrayList<GrdRoomHelp_Mdl>();

    public RoomHelp_Mdl() {
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
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

    public List<GrdRoomHelp_Mdl> getGrdGUIEntry_Mdl() {
        return grdGUIEntry_Mdl;
    }

    public String getConsultantTag() {
        return ConsultantTag;
    }

    public void setConsultantTag(String ConsultantTag) {
        this.ConsultantTag = ConsultantTag;
    }

}
