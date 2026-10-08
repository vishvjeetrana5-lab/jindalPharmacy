package aarogya.selfToolkit.customMultiLine.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CustomMultiLine_Vld {

    public static void chkSave(CustomMultiLine_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getConsultantCode(), "Consultant", 100);
        DmShared.isValidText(mrTemp.getSubject(), "Subject", 200);
        DmShared.isValidText(mrTemp.getRemark(), "Remark", 300);
    }
}
