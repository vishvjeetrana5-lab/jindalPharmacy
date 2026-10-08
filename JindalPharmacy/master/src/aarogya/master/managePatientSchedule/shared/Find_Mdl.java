package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String uhid, scheduleNo, scheduleName, PatientName, patientRecId, schdlDate, mobile, active;

    public Find_Mdl() {
    }

    public String getUhid() {
        return uhid;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPatientRecId() {
        return patientRecId;
    }

    public String getSchdlDate() {
        return schdlDate;
    }

    public void setSchdlDate(String schdlDate) {
        this.schdlDate = schdlDate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPatientRecId(String patientRecId) {
        this.patientRecId = patientRecId;
    }

    public void setUhid(String uhid) {
        this.uhid = uhid;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String PatientName) {
        this.PatientName = PatientName;
    }

}
