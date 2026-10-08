package aarogya.master.prescriptionValueTemplate.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PrescriptionValueTemplate_Vld {

    public static void chkSave(PrescriptionValueTemplate_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getConsultantName().trim(), "Consultant Name", 100);
        DmShared.isValidText(mrTemp.getConfigCode().trim(), "GUI Name", 200);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Name", 300);
    }
}
