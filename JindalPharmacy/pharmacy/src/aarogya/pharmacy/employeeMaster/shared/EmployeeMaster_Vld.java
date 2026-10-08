package aarogya.pharmacy.employeeMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EmployeeMaster_Vld {

    public static void chkSave(Employee_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Name", 100);
        DmShared.isValidText(mrTemp.getEmployeeID().trim(), "Employee ID", 200);
        DmShared.isValidText(mrTemp.getJoinDate().trim(), "Join Date", 300);
        DmShared.isValidText(mrTemp.getAccountGroupTag().trim(), "Account Group", 400);
        DmShared.isValidText(mrTemp.getDepartmentTag().trim(), "Department", 500);
        DmShared.isValidText(mrTemp.getGenderTag().trim(), "Gender", 700);
        DmShared.isValidText(mrTemp.getMaritalStatusTag().trim(), "Marital Status", 800);
    }
}
