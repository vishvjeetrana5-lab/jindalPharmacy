package aarogya.nabh.roomHelp.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RoomHelp_Vld {

    public static void chkSave(RoomHelp_Mdl mrTemp) {
//        DmShared.isValidText(DmShamrTemp.getVoucherTypeText().trim(), "Type", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode(), "IPD No.", 100);
        DmShared.isValidText(mrTemp.getConsultantTag(), "Consultant", 200);

    }
}
