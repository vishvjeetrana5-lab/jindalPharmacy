package aarogya.selfToolkit.appointmentSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdAppointmentDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String diaryV_SNo, dateTime, patientName, priorityType, consultantName, mobile, uhidNo, uhidDocId, regNo, status, appoinmentNo;

    public GrdAppointmentDetails_Mdl() {
    }

    public String getDiaryV_SNo() {
        return diaryV_SNo;
    }

    public void setDiaryV_SNo(String diaryV_SNo) {
        this.diaryV_SNo = diaryV_SNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPriorityType() {
        return priorityType;
    }

    public void setPriorityType(String priorityType) {
        this.priorityType = priorityType;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUhidNo() {
        return uhidNo;
    }

    public void setUhidNo(String uhidNo) {
        this.uhidNo = uhidNo;
    }

    public String getUhidDocId() {
        return uhidDocId;
    }

    public void setUhidDocId(String uhidDocId) {
        this.uhidDocId = uhidDocId;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppoinmentNo() {
        return appoinmentNo;
    }

    public void setAppoinmentNo(String appoinmentNo) {
        this.appoinmentNo = appoinmentNo;
    }

}
