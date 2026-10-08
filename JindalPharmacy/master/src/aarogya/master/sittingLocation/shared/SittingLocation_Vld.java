package aarogya.master.sittingLocation.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class SittingLocation_Vld {

    public static void chkSave(SittingLocation_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getLocationTag().trim(), "Location", 100);
    }
}
