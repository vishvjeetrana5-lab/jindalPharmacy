package aarogya.master.patientScheduleMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PatientScheduleMaster_Vld {

    public static void chkSave(PatientScheduleMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 400);
        DmShared.isValidText(mrTemp.getScheduleGroup(), "Schedule Group", 300);
        int count = 0;
        for (GrdPatntSchdulMster_Mdl grdMdl : mrTemp.getGrdPatientSchdulMsterList()) {
            if (!DmShared.xNull(grdMdl.getProcedures()).equalsIgnoreCase("")) {
                count++;
            }
        }
        if (count == 0) {
            throw new DmException(500, "Add atleast one Procedure");
        }
    }
}
