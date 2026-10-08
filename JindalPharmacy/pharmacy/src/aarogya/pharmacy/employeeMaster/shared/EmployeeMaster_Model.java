package aarogya.pharmacy.employeeMaster.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;

public class EmployeeMaster_Model {

    public static void chkSave(Employee_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Name", 100);
        DmShared.isValidText(mrTemp.getEmployeeID(), "Employee ID", 200);
        DmShared.isValidText(mrTemp.getJoinDate(), "Join Date", 300);
        DmShared.isValidText(mrTemp.getAccountGroupTag(), "Account Group", 400);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department", 500);
        DmShared.isValidText(mrTemp.getDesignationTag(), "Designation", 600);
        DmShared.isValidText(mrTemp.getGenderTag(), "Gender", 700);
        DmShared.isValidText(mrTemp.getMaritalStatusTag(), "Marital Status", 800);
    }

}
