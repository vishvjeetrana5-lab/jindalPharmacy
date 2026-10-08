package dataman.payroll.leavingMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LeavingMaster_Vld {

    public static void chkSave(LeavingMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getEmployeeName(), "Employee Name", 100);
        DmShared.isValidText(mrTemp.getResignationMode(), "Resignation Mode", 200);
        DmShared.isValidText(mrTemp.getReason().trim(), "Reason", 300);
        DmShared.isValidText(mrTemp.getSalaryStatus().trim(), "Last Salary Status", 400);
    }
}
