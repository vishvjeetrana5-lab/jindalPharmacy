package aarogya.nabh.patientInfectionEntry.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PatientInfectionEntry_Vld {

    public static void chkSave(PatientInfectionEntry_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getRegType()), "Reg Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getUhid()), "Uhid", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getInfectionType()), "Infection Type", 300);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getServiceCode()).longValue(), "Service", 400);   
    }
}
