package aarogya.master.cardDeactive.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CardDeactivate_Vld {

    public static void chkSave(CardDeactivate_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVrTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVrDate(), "Voucher Date", 200);
        DmShared.isValidText(mrTemp.getCardRegMdl().getSharedKeyFieldCode(), "Card No.", 300);
        DmShared.isValidText(mrTemp.getRemark(), "Remark", 400);

        if ((DmShared.convertDate(mrTemp.getVrDate()).getTime() < (DmShared.convertDate(mrTemp.getCardRegMdl().getAllotmentDate()).getTime()))) {
            throw new DmException(200, "Alt. Date should not be  less than Voucher date (" + DmShared.dNull(mrTemp.getCardRegMdl().getAllotmentDate()) + ").");
        }
    }
}
