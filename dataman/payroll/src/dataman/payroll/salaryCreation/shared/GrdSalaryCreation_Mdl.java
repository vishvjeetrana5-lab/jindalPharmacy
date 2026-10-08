
package dataman.payroll.salaryCreation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdSalaryCreation_Mdl extends KeyField implements Serializable, IsSerializable {

    private String employee;
    private String employeeCode;
    private String father;
    private String basic;
    private String calcBasic;
    private String grossSalary;
    private String netSalary;
    private String tdays;
    private String offDays;
    private String holidays;
    private String wdays;
    private String departmentCode;
    private String salaryMethodCode;
    private String salaryMethodName;
    private String departmentName;
    private String totalDed;
    private String totalEarn;
    private String leave;
    private String earningDeductionFields;
    private String earningDeductionValues;
    private String additionalOverHeadsFields;
    private String additionalOverHeadsValues;
    private String present;
    private String absent;
    private String earn_Leave;
    private String casual_Leave;
    private String halfDay;
    private String lwp;
    private String loanDocID;
    private String emiDate;

    public GrdSalaryCreation_Mdl() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getCalcBasic() {
        return calcBasic;
    }

    public void setCalcBasic(String calcBasic) {
        this.calcBasic = calcBasic;
    }

    public String getGrossSalary() {
        return grossSalary;
    }

    public void setGrossSalary(String grossSalary) {
        this.grossSalary = grossSalary;
    }

    public String getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(String netSalary) {
        this.netSalary = netSalary;
    }

    public String getTdays() {
        return tdays;
    }

    public void setTdays(String tdays) {
        this.tdays = tdays;
    }

    public String getOffDays() {
        return offDays;
    }

    public void setOffDays(String offDays) {
        this.offDays = offDays;
    }

    public String getHolidays() {
        return holidays;
    }

    public void setHolidays(String holidays) {
        this.holidays = holidays;
    }

    public String getWdays() {
        return wdays;
    }

    public void setWdays(String wdays) {
        this.wdays = wdays;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getSalaryMethodCode() {
        return salaryMethodCode;
    }

    public void setSalaryMethodCode(String salaryMethodCode) {
        this.salaryMethodCode = salaryMethodCode;
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

    public String getTotalDed() {
        return totalDed;
    }

    public void setTotalDed(String totalDed) {
        this.totalDed = totalDed;
    }

    public String getTotalEarn() {
        return totalEarn;
    }

    public void setTotalEarn(String totalEarn) {
        this.totalEarn = totalEarn;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getEarningDeductionFields() {
        return earningDeductionFields;
    }

    public void setEarningDeductionFields(String earningDeductionFields) {
        this.earningDeductionFields = earningDeductionFields;
    }

    public String getEarningDeductionValues() {
        return earningDeductionValues;
    }

    public void setEarningDeductionValues(String earningDeductionValues) {
        this.earningDeductionValues = earningDeductionValues;
    }

    public String getAdditionalOverHeadsFields() {
        return additionalOverHeadsFields;
    }

    public void setAdditionalOverHeadsFields(String additionalOverHeadsFields) {
        this.additionalOverHeadsFields = additionalOverHeadsFields;
    }

    public String getAdditionalOverHeadsValues() {
        return additionalOverHeadsValues;
    }

    public void setAdditionalOverHeadsValues(String additionalOverHeadsValues) {
        this.additionalOverHeadsValues = additionalOverHeadsValues;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
    }

    public String getAbsent() {
        return absent;
    }

    public void setAbsent(String absent) {
        this.absent = absent;
    }

    public String getEarn_Leave() {
        return earn_Leave;
    }

    public void setEarn_Leave(String earn_Leave) {
        this.earn_Leave = earn_Leave;
    }

    public String getCasual_Leave() {
        return casual_Leave;
    }

    public void setCasual_Leave(String casual_Leave) {
        this.casual_Leave = casual_Leave;
    }

    public String getHalfDay() {
        return halfDay;
    }

    public void setHalfDay(String halfDay) {
        this.halfDay = halfDay;
    }

    public String getLwp() {
        return lwp;
    }

    public void setLwp(String lwp) {
        this.lwp = lwp;
    }

    public String getLoanDocID() {
        return loanDocID;
    }

    public void setLoanDocID(String loanDocID) {
        this.loanDocID = loanDocID;
    }

    public String getEmiDate() {
        return emiDate;
    }

    public void setEmiDate(String emiDate) {
        this.emiDate = emiDate;
    }
    
}

