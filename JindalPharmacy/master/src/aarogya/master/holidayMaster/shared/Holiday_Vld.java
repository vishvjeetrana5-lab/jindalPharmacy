package aarogya.master.holidayMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class Holiday_Vld {

    public static void chkSave(Holiday_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(mrTemp.getHolidayDate().trim(), "Date", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Holiday", 200);

        if (DmShared.convertDate(mrTemp.getHolidayDate()).getTime() <= DmShared.convertDate(currentDate).getTime()) {
            throw new DmException(0, "Date should be greater than  Current Date.");
        }

    }
}
