package aarogya.selfToolkit.walletSystem.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CardRegistration_Vld {

    public static void chkSave(CardRegistration_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getCardMdl().getSharedKeyFieldCode(), "Wallet Type", 100);
        DmShared.isValidText(mrTemp.getCardMdl().getSubCode(), "Link A/c of Wallet", 200);
        DmShared.isValidText(mrTemp.getCardNo(), "Card No.", 300);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Allotment Date", 700);
        if (mrTemp.getPersonMdl() == null) {
            throw new DmException(800, "Person Detail is mandatory.");
        }
        DmShared.isValidText(mrTemp.getPersonMdl().getPerson(), "Person Name", 800);
        DmShared.isValidText(mrTemp.getPersonMdl().getMobile(), "Mobile No.", 900);
        DmShared.isValidMobile(mrTemp.getPersonMdl().getMobile(), 900);
    }
}
