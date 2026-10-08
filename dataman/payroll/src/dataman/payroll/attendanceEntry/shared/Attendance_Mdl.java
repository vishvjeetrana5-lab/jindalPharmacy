package dataman.payroll.attendanceEntry.shared;

import dataman.payroll.salaryCreation.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Attendance_Mdl extends KeyField implements Serializable, IsSerializable {

private String days;
private String workingHours;
private String natureCode;
private String type_Leave_Attendance;
private String plusMinus;

 public Attendance_Mdl() {
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getType_Leave_Attendance() {
        return type_Leave_Attendance;
    }

    public void setType_Leave_Attendance(String type_Leave_Attendance) {
        this.type_Leave_Attendance = type_Leave_Attendance;
    }

    public String getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(String plusMinus) {
        this.plusMinus = plusMinus;
    }
        
}

