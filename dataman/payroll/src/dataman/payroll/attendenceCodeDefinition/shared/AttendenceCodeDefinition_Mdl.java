package dataman.payroll.attendenceCodeDefinition.shared;

import dataman.dmBase.self.shared.*;

public class AttendenceCodeDefinition_Mdl extends KeyField {

    private String shortName, workingHours, days, byDefaultCode, byDefault,plusMinus,plusMinusCode;

    public AttendenceCodeDefinition_Mdl() {
    }

    public String getByDefaultCode() {
        return byDefaultCode;
    }

    public void setByDefaultCode(String byDefaultCode) {
        this.byDefaultCode = byDefaultCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getByDefault() {
        return byDefault;
    }

    public void setByDefault(String byDefault) {
        this.byDefault = byDefault;
    }

    public String getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(String plusMinus) {
        this.plusMinus = plusMinus;
    }

    public String getPlusMinusCode() {
        return plusMinusCode;
    }

    public void setPlusMinusCode(String plusMinusCode) {
        this.plusMinusCode = plusMinusCode;
    }

}
