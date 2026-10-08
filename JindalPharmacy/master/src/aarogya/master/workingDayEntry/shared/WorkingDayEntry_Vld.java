package aarogya.master.workingDayEntry.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class WorkingDayEntry_Vld {

    public static void chkSave(WorkingDayEntry_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSalCode().trim(), "Salary period", 100);
        DmShared.isValidText(mrTemp.getEmployeeCode().trim(), "Employee Name", 200);
        DmShared.isValidText(mrTemp.getWorkingDays().trim(), "Working Days", 300);

    }
}
