package aarogya.selfToolkit.stStatic.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ShiftTime_Mdl implements Serializable, IsSerializable {

    private String fromTime, toTime;
    private int graceMinute;

    public ShiftTime_Mdl() {
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

    public int getGraceMinute() {
        return graceMinute;
    }

    public void setGraceMinute(int graceMinute) {
        this.graceMinute = graceMinute;
    }
}
