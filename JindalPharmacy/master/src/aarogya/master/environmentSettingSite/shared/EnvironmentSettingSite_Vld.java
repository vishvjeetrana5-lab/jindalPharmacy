package aarogya.master.environmentSettingSite.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class EnvironmentSettingSite_Vld {

    public static void chkSave(EnvironmentSettingSite_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Site", 100);
        DmShared.isValidText(mrTemp.getIpdIsRoomCleanTag(), "Default Status (Room Vacant)", 200);
        if (DmShared.vNull(mrTemp.getIpdIsAutoRoomTag()).intValue() == 1) {
            DmShared.isValidText(mrTemp.getIpdTimeFormatRoomTag(), "Time Format", 300);
            if (DmShared.vNull(mrTemp.getIpdTimeFormatRoomTag()).intValue() == 1) {
                DmShared.isValidText(mrTemp.getIpdCheckoutTime(), "Checkout Time", 400);
            }
        }
        if (DmShared.vNull(mrTemp.getIpdHalfDayUpToMin()).intValue() >= 1440) {
            throw new DmException(500, "Half Day Upto can not greater than or equal to one day (1440 minute).");
        }

    }
}
