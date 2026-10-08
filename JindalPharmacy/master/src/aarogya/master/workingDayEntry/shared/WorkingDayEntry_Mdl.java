package aarogya.master.workingDayEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class WorkingDayEntry_Mdl extends KeyField implements Serializable, IsSerializable {

//    private String monthCode;
    private String minimumGurantee;
    private String salCode;
    private String salName;
    private String employeeName, employeeCode;
    private String workingDays;

    public WorkingDayEntry_Mdl() {
    }

//    public String getMonthCode() {
//        return monthCode;
//    }
//
//    public void setMonthCode(String monthCode) {
//        this.monthCode = monthCode;
//    }
//
//    public String getMonthName() {
//        return monthName;
//    }
//
//    public void setMonthName(String monthName) {
//        this.monthName = monthName;
//    }
//
//    public String getYearCode() {
//        return yearCode;
//    }
//
//    public void setYearCode(String yearCode) {
//        this.yearCode = yearCode;
//    }
//
//    public String getYearName() {
//        return yearName;
//    }
//
//    public void setYearName(String yearName) {
//        this.yearName = yearName;
//    }
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

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getSalCode() {
        return salCode;
    }

    public void setSalCode(String salCode) {
        this.salCode = salCode;
    }

    public String getSalName() {
        return salName;
    }

    public void setSalName(String salName) {
        this.salName = salName;
    }

    public String getMinimumGurantee() {
        return minimumGurantee;
    }

    public void setMinimumGurantee(String minimumGurantee) {
        this.minimumGurantee = minimumGurantee;
    }

}
