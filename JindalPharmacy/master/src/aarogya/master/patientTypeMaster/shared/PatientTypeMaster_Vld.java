package aarogya.master.patientTypeMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PatientTypeMaster_Vld {

    public static void chkSave(PatientTypeMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Patient Type", 100);
        DmShared.isValidText(mrTemp.getActiveName().trim(), "Active ", 300);
//        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name ", 400);
    }

}
