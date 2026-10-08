
package dataman.payroll.employeeShiftDetail.shared;

import dataman.payroll.reports.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TxtHShift_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromTime;
    private String toTime;
   
    public TxtHShift_Mdl() {
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

 
}

