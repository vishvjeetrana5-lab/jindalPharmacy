package dataman.payroll.shiftMaster.shared;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShiftMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String shiftName;
    private String shortName;
    private String inTimeGrace;
    private String outTimeGrace;
    private String fromTime;
    private String toTime;
    private String duration;
    private String includesNexDay;
    private String includesNexDayName;

   
    public ShiftMaster_Mdl() {
    }

   
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getInTimeGrace() {
        return inTimeGrace;
    }

    public void setInTimeGrace(String inTimeGrace) {
        this.inTimeGrace = inTimeGrace;
    }

    public String getOutTimeGrace() {
        return outTimeGrace;
    }

    public void setOutTimeGrace(String outTimeGrace) {
        this.outTimeGrace = outTimeGrace;
    }

    public String getIncludesNexDay() {
        return includesNexDay;
    }

    public void setIncludesNexDay(String includesNexDay) {
        this.includesNexDay = includesNexDay;
    }

    public String getIncludesNexDayName() {
        return includesNexDayName;
    }

    public void setIncludesNexDayName(String includesNexDayName) {
        this.includesNexDayName = includesNexDayName;
    }

  
}
