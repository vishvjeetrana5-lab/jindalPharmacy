package dataman.payroll.attendenceEntryManual.shared;

import dataman.payroll.attendenceEntryManual.shared.*;
import dataman.payroll.salaryCreation.shared.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AttendenceEntryManual_Vld {

    public static void chkSave(AttendenceEntryManual_Mdl scMdl) {

        DmShared.isValidText(scMdl.getEmployeeName(), "Employee Name", 100);
        DmShared.isValidText(scMdl.getFromDate(), "From Date", 200);
        DmShared.isValidText(scMdl.getToDate(), "To Date", 300);
        DmShared.isValidText(scMdl.getAttendanceName(), "Attendance ", 400);
        DmShared.isValidText(scMdl.getFromTime(), "From Time", 500);
        DmShared.isValidText(scMdl.getToTime(), "To Time", 600);

        if (!DmShared.dNull(scMdl.getFromDate()).equalsIgnoreCase("") && !DmShared.dNull(scMdl.getToDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(DmShared.xNull(scMdl.getFromDate())).getTime() > DmShared.convertDate(DmShared.xNull(scMdl.getToDate())).getTime()) {
                throw new DmException(500, "From Date should be less than the To Date.");
            }
        }

        if (DmShared.convertDate(DmShared.xNull(scMdl.getFromDate())).getTime() == DmShared.convertDate(DmShared.xNull(scMdl.getToDate())).getTime() 
                && DmShared.vNull(scMdl.getFromTime()).doubleValue()>DmShared.vNull(scMdl.getToTime()).doubleValue()) {
            throw new DmException(500, "From Time should be less than the To Time for same date range.");
        }
    }
}
