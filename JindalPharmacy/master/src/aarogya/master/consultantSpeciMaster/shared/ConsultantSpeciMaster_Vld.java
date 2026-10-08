package aarogya.master.consultantSpeciMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ConsultantSpeciMaster_Vld {

    public static void chkSave(ConsultantSpeciMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Specialization", 100);
        DmShared.isValidText(mrTemp.getNatureCode(), "Nature", 200);
    }

}
