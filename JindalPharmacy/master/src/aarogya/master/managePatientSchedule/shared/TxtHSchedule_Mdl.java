package aarogya.master.managePatientSchedule.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHSchedule_Mdl extends KeyField implements Serializable, IsSerializable {

    private String scheduleManual;
    private String vDate;
    private String scheduleName;
    private String group;

    public TxtHSchedule_Mdl() {
    }

    public String getScheduleManual() {
        return scheduleManual;
    }

    public void setScheduleManual(String scheduleManual) {
        this.scheduleManual = scheduleManual;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVDate() {
        return vDate;
    }

    public void setVDate(String vDate) {
        this.vDate = vDate;
    }

}
