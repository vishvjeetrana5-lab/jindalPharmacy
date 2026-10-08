package dataman.dmToolkit.partyDetailPopup.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PartyDetailPopup_Vld {

    public static void chkSave(PartyDetailPopup_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getName()), "Party Name", 100);
        DmShared.isValidMobile(DmShared.xNull(mrTemp.getMobile()), 300);
        DmShared.isValidEMail(DmShared.xNull(mrTemp.getEmail()), 400);
    }

}
