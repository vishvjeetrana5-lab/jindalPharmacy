package dataman.payroll.attendanceEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AttendanceEntry_Vld {

    public static void chkSave(AttendanceEntry_Mdl scMdl) {

    DmShared.isValidText(scMdl.getSalaryMethodName(), "Salary Method", 100);
    
   
    if (scMdl.getAttendanceMdlList().size() == 0)
       {
          throw new DmException(1700, "Please Fill Details.");
        }   

    }
}
