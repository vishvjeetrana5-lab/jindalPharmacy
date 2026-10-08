package dataman.payroll.leaveMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LeaveMaster_Vld {

    public static void chkSave(LeaveMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Leave Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getLeave_type().getValue(), "Type", 300);
        DmShared.isValidText(mrTemp.getLeave_type().getValue(), "Nature", 400);
        DmShared.isValidText(mrTemp.getIsActive(), "isActive", 410);

        if (DmShared.xNull(mrTemp.getGrdLst().get(0).getFromDate()).equalsIgnoreCase("")) {
            throw new DmException(500, "Date details are compulsory.");
        }
    }
}
