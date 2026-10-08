package dataman.payroll.holidayMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class Holiday_Vld {

    public static void chkSave(Holiday_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getHolidayDate().trim()), "Date", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName().trim()), "Holiday", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getHolidayType().getValue()), "Holiday Type", 300);

        if (DmShared.convertDate(mrTemp.getHolidayDate()).getTime() <= DmShared.convertDate(currentDate).getTime()) {
            throw new DmException(0, "Date should be greater than  Current Date.");
        }

    }
}
