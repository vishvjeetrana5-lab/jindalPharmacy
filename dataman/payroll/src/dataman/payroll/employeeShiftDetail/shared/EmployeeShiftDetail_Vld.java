package dataman.payroll.employeeShiftDetail.shared;

import dataman.payroll.shiftMaster.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.salaryCreation.shared.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EmployeeShiftDetail_Vld {

    public static void chkSave(EmployeeShiftDetail_Mdl scMdl, String currentDate) {
        DmShared.isValidText(scMdl.getEmployeeCode(), "Employee", 100);
        DmShared.isValidText(scMdl.getShiftCode(), "Shift", 200);
        DmShared.isValidText(scMdl.getFromDate(), "From", 300);
        DmShared.isValidText(scMdl.getToDate(), "To", 400);
        if (DmShared.convertDate(scMdl.getFromDate()).getTime() < DmShared.convertDate(currentDate).getTime()) {
            throw new DmException(300, "From Date should be greater than or equal to Current Date.");
        }
        if ((!DmShared.dNull(scMdl.getFromDate()).equalsIgnoreCase("")) && (!DmShared.dNull(scMdl.getToDate()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(scMdl.getFromDate()).getTime() > DmShared.convertDate(scMdl.getToDate()).getTime()) {
                throw new DmException(300, "From Date should be less than or equal to To Date.");
            }
        }

    }
}
