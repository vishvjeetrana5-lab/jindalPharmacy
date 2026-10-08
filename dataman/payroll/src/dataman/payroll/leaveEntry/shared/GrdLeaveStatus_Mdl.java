
package dataman.payroll.leaveEntry.shared;

import dataman.payroll.leaveEntry.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdLeaveStatus_Mdl extends KeyField implements Serializable, IsSerializable {

    private String leave;
    private String opening;
    private String taken;
    private String balance;
   
    public GrdLeaveStatus_Mdl() {
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getTaken() {
        return taken;
    }

    public void setTaken(String taken) {
        this.taken = taken;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }


}

