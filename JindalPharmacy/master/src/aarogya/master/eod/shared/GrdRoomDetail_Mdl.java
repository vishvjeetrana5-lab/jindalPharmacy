package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdRoomDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ipdDocId;
    private String ipdRecId;
    private String uhidDocId;
    private String uhidRecId;
    private String patientName;
    private String age;
    private String gender;
    private String admitDate;
    private String orgCode;
    private String orgName;
    private String orgDiscount;
    private String roomCatCode;
    private String roomCatName;
    private String consultantCode;
    private String siteCode;
    private String  rateListCode ;

    public GrdRoomDetail_Mdl() {
    }

    public String getIpdDocId() {
        return ipdDocId;
    }

    public void setIpdDocId(String ipdDocId) {
        this.ipdDocId = ipdDocId;
    }

    public String getIpdRecId() {
        return ipdRecId;
    }

    public void setIpdRecId(String ipdRecId) {
        this.ipdRecId = ipdRecId;
    }

    public String getUhidDocId() {
        return uhidDocId;
    }

    public void setUhidDocId(String uhidDocId) {
        this.uhidDocId = uhidDocId;
    }

    public String getUhidRecId() {
        return uhidRecId;
    }

    public void setUhidRecId(String uhidRecId) {
        this.uhidRecId = uhidRecId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAdmitDate() {
        return admitDate;
    }

    public void setAdmitDate(String admitDate) {
        this.admitDate = admitDate;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDiscount() {
        return orgDiscount;
    }

    public void setOrgDiscount(String orgDiscount) {
        this.orgDiscount = orgDiscount;
    }

    public String getRoomCatCode() {
        return roomCatCode;
    }

    public void setRoomCatCode(String roomCatCode) {
        this.roomCatCode = roomCatCode;
    }

    public String getRoomCatName() {
        return roomCatName;
    }

    public void setRoomCatName(String roomCatName) {
        this.roomCatName = roomCatName;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

}
