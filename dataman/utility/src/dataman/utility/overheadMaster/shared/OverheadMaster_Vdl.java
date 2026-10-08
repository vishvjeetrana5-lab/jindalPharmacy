package dataman.utility.overheadMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class OverheadMaster_Vdl {

    public static void chkSave(OverheadMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Overhead Name", 100);
         DmShared.isValidText(mrTemp.getManualCode(), "Manual Code", 200);
    }
}
