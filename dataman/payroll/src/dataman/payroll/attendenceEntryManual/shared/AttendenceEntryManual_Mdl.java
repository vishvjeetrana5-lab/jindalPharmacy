package dataman.payroll.attendenceEntryManual.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class AttendenceEntryManual_Mdl extends KeyField implements Serializable, IsSerializable {

    private String employeeName;
    private String employeeCode;
    private String departmentName;
    private String departmentCode;  
    private String entryDate;
    private String attendanceCode;
    private String attendanceName;
    private String fromDate;
    private String fromTime;
    private String toDate;
    private String toTime;
    private String duration;
    private String remark;
    private RecId recId = new RecId();
    private String dtVoucher;
    private String days;
    private String plusMinus;
    private String attendanceShortName;

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

    public AttendenceEntryManual_Mdl() {
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getAttendanceCode() {
        return attendanceCode;
    }

    public void setAttendanceCode(String attendanceCode) {
        this.attendanceCode = attendanceCode;
    }

    public String getAttendanceName() {
        return attendanceName;
    }

    public void setAttendanceName(String attendanceName) {
        this.attendanceName = attendanceName;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(String plusMinus) {
        this.plusMinus = plusMinus;
    }

    public String getAttendanceShortName() {
        return attendanceShortName;
    }

    public void setAttendanceShortName(String attendanceShortName) {
        this.attendanceShortName = attendanceShortName;
    }

}
