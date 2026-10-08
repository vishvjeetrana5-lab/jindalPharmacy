package dataman.payroll.attendanceEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Leave_Mdl extends KeyField implements Serializable, IsSerializable {


private String duration;
private String fromDate;
private String leaveManualCode;
private String leaveCode; 
private String employeeCode;


 public Leave_Mdl() {
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

   
    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getLeaveManualCode() {
        return leaveManualCode;
    }

    public void setLeaveManualCode(String leaveManualCode) {
        this.leaveManualCode = leaveManualCode;
    }

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

   
}

