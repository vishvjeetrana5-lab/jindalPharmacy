package aarogya.master.disabilityMaster.shared;

import aarogya.master.disabilityMaster.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DisabilityMaster_Vld {

    public static void chkSave(DisabilityMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getManualCode(), "Disability code", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Disability name", 200);
    }

}
