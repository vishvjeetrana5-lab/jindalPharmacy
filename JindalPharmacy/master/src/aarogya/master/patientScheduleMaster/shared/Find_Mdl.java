package aarogya.master.patientScheduleMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String scheduleGroup;
    private String shortName;

    public Find_Mdl() {
    }

    public String getScheduleGroup() {
        return scheduleGroup;
    }

    public void setScheduleGroup(String scheduleGroup) {
        this.scheduleGroup = scheduleGroup;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}
