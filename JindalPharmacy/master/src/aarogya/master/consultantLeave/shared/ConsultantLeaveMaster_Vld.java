package aarogya.master.consultantLeave.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ConsultantLeaveMaster_Vld {

    public static void chkSave(ConsultantLeaveMaster_Mdl mrTemp, String loginDate, String currentTime) {
        DmShared.isValidText(mrTemp.getConsultantCode().trim(), "Consultant Name", 100);
        DmShared.isValidText(mrTemp.getFromDate().trim(), "From Date", 200);
        DmShared.isValidText(mrTemp.getRemark().trim(), "Remark", 700);
        if (DmShared.convertDate(loginDate).getTime() > DmShared.convertDate(mrTemp.getFromDate()).getTime()) {
            throw new DmException(200, "From Date should greater or equal to current date.");
        }
        if (mrTemp.isFullDay()) {
            DmShared.isValidText(mrTemp.getToDate().trim(), "To Date", 300);
        } else {
            DmShared.isValidText(DmShared.xNull(mrTemp.getFromTime()), "From Time", 500);
            DmShared.isValidText(DmShared.xNull(mrTemp.getToTime()), "To Time", 600);

            if ((DmShared.convertDate(mrTemp.getFromDate()).getTime() == (DmShared.convertDate(loginDate)).getTime())) {
                if (DmShared.vNull(mrTemp.getFromTime()).doubleValue() < DmShared.vNull(currentTime).doubleValue()) {
                    throw new DmException(500, "From Time should be greater or equal to current time.");
                }
            }
            if (DmShared.vNull(mrTemp.getFromTime()).doubleValue() > DmShared.vNull(mrTemp.getToTime()).doubleValue()) {
                throw new DmException(500, "To Time should be greater than to From Time.");
            }
        }
        if ((DmShared.convertDate(mrTemp.getFromDate()).getTime() > (DmShared.convertDate(mrTemp.getToDate())).getTime())) {
            throw new DmException(400, "To Date should be greater than or equal to From Date.");
        }
    }
}
