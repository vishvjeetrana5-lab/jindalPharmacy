package dataman.payroll.shiftMaster.shared;

import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.salaryCreation.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    
    private String shortName;
    private String fromTime;
    private String toTime;
    private String inTimeGrace;
    private String outTimeGrace;


    public Find_Mdl() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
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

}
