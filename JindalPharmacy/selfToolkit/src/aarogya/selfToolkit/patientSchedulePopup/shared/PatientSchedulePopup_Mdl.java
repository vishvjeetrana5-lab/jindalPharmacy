package aarogya.selfToolkit.patientSchedulePopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;

public class PatientSchedulePopup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String scheduleName, ScheduleCode, scheduleDate;
    private String voucherDate, voucherTime, voucherTypeCode;
    private String docIdUHID;
    private RecId recIdSchedulePopup = new RecId();

    public PatientSchedulePopup_Mdl() {
    }

    public String getDocIdUHID() {
        return docIdUHID;
    }

    public void setDocIdUHID(String docIdUHID) {
        this.docIdUHID = docIdUHID;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public RecId getRecIdSchedulePopup() {
        return recIdSchedulePopup;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public void setRecIdSchedulePopup(RecId recIdSchedulePopup) {
        this.recIdSchedulePopup = recIdSchedulePopup;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getScheduleCode() {
        return ScheduleCode;
    }

    public void setScheduleCode(String ScheduleCode) {
        this.ScheduleCode = ScheduleCode;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherTime() {
        return voucherTime;
    }

    public void setVoucherTime(String voucherTime) {
        this.voucherTime = voucherTime;
    }

}
