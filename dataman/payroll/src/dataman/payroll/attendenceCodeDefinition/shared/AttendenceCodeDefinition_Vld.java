package dataman.payroll.attendenceCodeDefinition.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AttendenceCodeDefinition_Vld {

    public static void chkSave(AttendenceCodeDefinition_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getByDefault(), "By Default", 300);

        if (!DmShared.xNull(mrTemp.getWorkingHours()).equalsIgnoreCase("")) {
            if (DmShared.vNull(mrTemp.getWorkingHours()) > 99.99) {
                throw new DmException(400, "working hours must be equal or less than 99.99");
            }

        }
        
        if (!DmShared.xNull(mrTemp.getDays()).equalsIgnoreCase("")) {
            if (DmShared.vNull(mrTemp.getDays()) > 9.99) {
                throw new DmException(500, "Days must be equal or less than 9.99");
            }

        }

    }
}
