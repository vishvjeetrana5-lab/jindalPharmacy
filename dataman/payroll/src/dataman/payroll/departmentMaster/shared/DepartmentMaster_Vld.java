package dataman.payroll.departmentMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DepartmentMaster_Vld {

    public static void chkSave(DepartmentMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Department Name", 100);
        DmShared.isValidText(mrTemp.getDisplayName(), "Display Name", 200);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 300);
//        DmShared.isValidText(mrTemp.getCostCenter().trim(), "Cost Center", 400);
    }
}
