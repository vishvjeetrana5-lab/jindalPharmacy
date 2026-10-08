package aarogya.master.allergyMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AllergyMaster_Vld {

    public static void chkSave(AllergyMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Allergy Name", 100);
        DmShared.isValidText(mrTemp.getNatureName().trim(), " Nature ", 200);
        DmShared.isValidText(mrTemp.getActiveName().trim(), "Active ", 300);
    }

}
