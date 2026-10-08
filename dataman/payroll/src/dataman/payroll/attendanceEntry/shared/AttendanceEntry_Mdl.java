package dataman.payroll.attendanceEntry.shared;

import dataman.payroll.salaryCreation.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AttendanceEntry_Mdl extends KeyField implements Serializable, IsSerializable {

private List<GrdAttendanceEntry_Mdl> attendanceMdlList = new ArrayList<GrdAttendanceEntry_Mdl>();
//private String v_Type;
private String salaryMethodName;
private String departmentName;
private String salaryMethodCode;
private String departmentCode;
private String fromDate;

 public AttendanceEntry_Mdl() {
    }

    public String getSalaryMethodName() {
        return salaryMethodName;
    }

    public void setSalaryMethodName(String salaryMethodName) {
        this.salaryMethodName = salaryMethodName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSalaryMethodCode() {
        return salaryMethodCode;
    }

    public void setSalaryMethodCode(String salaryMethodCode) {
        this.salaryMethodCode = salaryMethodCode;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public List<GrdAttendanceEntry_Mdl> getAttendanceMdlList() {
        return attendanceMdlList;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    
}

