package dataman.payroll.leaveEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LeaveEntry_Vld {

    public static void chkSave(LeaveEntry_Mdl scMdl) {

    DmShared.isValidText(scMdl.getEmployeeName(), "Employee Name", 100);
    DmShared.isValidText(scMdl.getFromDate(), "From Date", 200);
    DmShared.isValidText(scMdl.getToDate(), "To Date", 300);
    DmShared.isValidText(scMdl.getReason(), "Reason", 400);
    DmShared.isValidText(scMdl.getLeaveName(), "Leave Type", 500);
    
    if (!DmShared.xNull(scMdl.getAvailableLeave()).equalsIgnoreCase("")) {
        if (Integer.valueOf(DmShared.xNull(scMdl.getDuration())) > Integer.valueOf(DmShared.xNull(scMdl.getAvailableLeave()))) {

            throw new DmException(500, "Leave is not available.");

            }
        }
    
    if (!DmShared.dNull(scMdl.getFromDate()).equalsIgnoreCase("") && !DmShared.dNull(scMdl.getToDate()).equalsIgnoreCase("")) {
        if (DmShared.convertDate(DmShared.xNull(scMdl.getFromDate())).getTime() > DmShared.convertDate(DmShared.xNull(scMdl.getToDate())).getTime()) {

            throw new DmException(500, "From Date should be less than the To Date.");

            }
        }

    }
}
