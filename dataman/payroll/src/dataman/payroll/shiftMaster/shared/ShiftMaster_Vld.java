package dataman.payroll.shiftMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ShiftMaster_Vld {

    public static void chkSave(ShiftMaster_Mdl scMdl) {

        DmShared.isValidText(scMdl.getShiftName(), "Shift Name", 100);
        DmShared.isValidText(scMdl.getShortName(), "Short Name", 200);
        DmShared.isValidText(scMdl.getFromTime(), "From Time", 300);
        DmShared.isValidText(scMdl.getToTime(), "To Time ", 400);
        DmShared.isValidText(scMdl.getInTimeGrace(), "In Time Grace", 500);
        DmShared.isValidText(scMdl.getOutTimeGrace(), "Out Time Grace", 600);
        DmShared.isValidText(scMdl.getIncludesNexDay(), "Includes Next Day", 700);
        
        if (DmShared.vNull(scMdl.getToTime()).doubleValue()<DmShared.vNull(scMdl.getFromTime()).doubleValue()
                && DmShared.iNull(scMdl.getIncludesNexDay()).intValue()==0){        //0==Includes Next Day (No)
            throw new DmException(400, "To Time can't be less than from time with Includes Next Day(No)");
        }
    }
}
