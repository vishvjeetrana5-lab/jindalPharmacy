package aarogya.selfToolkit.stStatic.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ConsultantDiary_Mdl implements Serializable, IsSerializable {

    private String docId, v_SNo, tokenNo;
    private String v_Type;
    private String v_Date, timeFrom, timeTo;
    private String consultantCode;
    private String consultationRoomCode;
    private String serviceDocId, serviceV_SNo;
    private String uhid, regDocId;
    private String name, mobileNo, email, remark;
    private String diaryDocId, diaryV_SNo;
    private String priorityTypeCode;
    private String statusRemark;
    private DiaryStatus status;
    private ConsultantDiaryNature nature;

    public ConsultantDiary_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getTokenNo() {
        return tokenNo;
    }

    public void setTokenNo(String tokenNo) {
        this.tokenNo = tokenNo;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultationRoomCode() {
        return consultationRoomCode;
    }

    public void setConsultationRoomCode(String consultationRoomCode) {
        this.consultationRoomCode = consultationRoomCode;
    }

    public String getServiceDocId() {
        return serviceDocId;
    }

    public void setServiceDocId(String serviceDocId) {
        this.serviceDocId = serviceDocId;
    }

    public String getServiceV_SNo() {
        return serviceV_SNo;
    }

    public void setServiceV_SNo(String serviceV_SNo) {
        this.serviceV_SNo = serviceV_SNo;
    }

    public String getUhid() {
        return uhid;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDiaryDocId() {
        return diaryDocId;
    }

    public void setDiaryDocId(String diaryDocId) {
        this.diaryDocId = diaryDocId;
    }

    public String getDiaryV_SNo() {
        return diaryV_SNo;
    }

    public void setDiaryV_SNo(String diaryV_SNo) {
        this.diaryV_SNo = diaryV_SNo;
    }

    public String getPriorityTypeCode() {
        return priorityTypeCode;
    }

    public void setPriorityTypeCode(String priorityTypeCode) {
        this.priorityTypeCode = priorityTypeCode;
    }

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    public DiaryStatus getStatus() {
        return status;
    }

    public void setStatus(DiaryStatus status) {
        this.status = status;
    }

    public ConsultantDiaryNature getNature() {
        return nature;
    }

    public void setNature(ConsultantDiaryNature nature) {
        this.nature = nature;
    }
}
