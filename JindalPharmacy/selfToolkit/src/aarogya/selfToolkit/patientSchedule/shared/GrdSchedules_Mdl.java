package aarogya.selfToolkit.patientSchedule.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdSchedules_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vDate, lastDoneDate, lastScheduleDate, nxtFollowupDate, scheduleNo;

    public GrdSchedules_Mdl() {
    }

    public String getVDate() {
        return vDate;
    }

    public void setVDate(String vDate) {
        this.vDate = vDate;
    }

    public String getLastDoneDate() {
        return lastDoneDate;
    }

    public void setLastDoneDate(String lastDoneDate) {
        this.lastDoneDate = lastDoneDate;
    }

    public String getLastScheduleDate() {
        return lastScheduleDate;
    }

    public void setLastScheduleDate(String lastScheduleDate) {
        this.lastScheduleDate = lastScheduleDate;
    }

    public String getNxtFollowupDate() {
        return nxtFollowupDate;
    }

    public void setNxtFollowupDate(String nxtFollowupDate) {
        this.nxtFollowupDate = nxtFollowupDate;
    }

    public String getScheduleNo() {
        return scheduleNo;
    }

    public void setScheduleNo(String scheduleNo) {
        this.scheduleNo = scheduleNo;
    }

}
