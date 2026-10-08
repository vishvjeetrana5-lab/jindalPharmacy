
package dataman.payroll.reports.shared;

import dataman.payroll.reports.shared.*;
import dataman.payroll.reports.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LeaveTextHelp_Mdl extends KeyField implements Serializable, IsSerializable {

    private String leave;
    private String shortName;
    public LeaveTextHelp_Mdl() {
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

}

