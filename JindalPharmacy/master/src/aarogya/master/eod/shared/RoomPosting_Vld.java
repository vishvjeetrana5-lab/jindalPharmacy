package aarogya.master.eod.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class RoomPosting_Vld {

    public static void chkSave(EOD_Mdl mrTemp, String currentDate) {

        DmShared.isValidText(mrTemp.getCurrentDate().trim(), "For Date", 100);
        if ((DmShared.convertDate(mrTemp.getCurrentDate()).getTime() > (DmShared.convertDate(currentDate)).getTime())) {
            throw new DmException(100, "For Date should be less than or equal to Current Date.");
        }

    }

}
