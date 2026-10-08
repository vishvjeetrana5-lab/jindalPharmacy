package aarogya.master.consultantLeave.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class ConsultantLeaveMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromDate, fromTime;
    private String toDate, toTime;
    private String consultantName, consultantCode;
    private String remark;
    private boolean fullDay = false;

    public ConsultantLeaveMaster_Mdl() {
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String ConsultantName) {
        this.consultantName = ConsultantName;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String ConsultantCode) {
        this.consultantCode = ConsultantCode;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public boolean isFullDay() {
        return fullDay;
    }

    public void setFullDay(boolean fullDay) {
        this.fullDay = fullDay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
