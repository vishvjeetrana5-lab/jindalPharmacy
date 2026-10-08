package aarogya.selfToolkit.patientSchedulePopup.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PatientSchedulePopup_Vld {

    public static void chkSave(PatientSchedulePopup_Mdl mrTemp, String currentTime) {
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getDocIdUHID()).longValue(), "UHID NO.", 100);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getScheduleCode()).longValue(), "Schedule", 200);
        DmShared.isValidText(mrTemp.getScheduleDate(), "Schedule Date", 300);
        if (DmShared.convertDate(mrTemp.getScheduleDate()).getTime() < DmShared.convertDate(currentTime).getTime()) {
            throw new DmException(400, "Schedule date must be greater or equal than current date.");
        }
    }

}
