package dataman.payroll.leaveEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class LeaveEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String employeeName;
    private String employeeCode;
    private String departmentName;
    private String departmentCode;  
    private String availableLeave;
    private String applyDate;
    private String leaveCode;
    private String leaveName;
    private String fromDate;
    private String fromTime;
    private String toDate;
    private String toTime;
    private String duration;
    private String reason;
    private String openingLeave;
    private String takenLeave;
    private String balanceLeave;
    private RecId recId = new RecId();
    private String dtVoucher;
    private String currentDate;
    private String statusCode;
    private String status;
    private String days;
    private String plusMinus;

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

    public LeaveEntry_Mdl() {
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

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getLeaveName() {
        return leaveName;
    }

    public void setLeaveName(String leaveName) {
        this.leaveName = leaveName;
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

    public String getLeaveCode() {
        return leaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        this.leaveCode = leaveCode;
    }

    public String getOpeningLeave() {
        return openingLeave;
    }

    public void setOpeningLeave(String openingLeave) {
        this.openingLeave = openingLeave;
    }

    public String getTakenLeave() {
        return takenLeave;
    }

    public void setTakenLeave(String takenLeave) {
        this.takenLeave = takenLeave;
    }

    public String getBalanceLeave() {
        return balanceLeave;
    }

    public void setBalanceLeave(String balanceLeave) {
        this.balanceLeave = balanceLeave;
    }

  
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getAvailableLeave() {
        return availableLeave;
    }

    public void setAvailableLeave(String availableLeave) {
        this.availableLeave = availableLeave;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
