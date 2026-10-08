package dataman.payroll.attendanceImport.shared;

import dataman.payroll.attendanceImport.shared.*;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AttendanceImport_Vld {

    public static void chkSave(AttendanceImport_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getFromDate().trim()), "From Date", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getToDate().trim()), "To Date", 200);

        if (DmShared.convertDate(mrTemp.getToDate()).getTime() <= DmShared.convertDate(mrTemp.getFromDate()).getTime()) {
            throw new DmException(0, "Date should be greater than  Current Date.");
        }

    }
}
