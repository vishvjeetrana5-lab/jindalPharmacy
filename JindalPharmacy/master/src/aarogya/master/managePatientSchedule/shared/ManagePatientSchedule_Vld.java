package aarogya.master.managePatientSchedule.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ManagePatientSchedule_Vld {

    public static void chkSave(GrdSchedules_Mdl mrTemp, String currentDate) {

        DmShared.isValidText(mrTemp.getScheduleDate(), "Schedule Date", 100);
        DmShared.isValidText(mrTemp.getConsultantCode(), "Consultant", 300);

    }
}
