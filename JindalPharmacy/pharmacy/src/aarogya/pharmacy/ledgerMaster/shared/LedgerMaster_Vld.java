package aarogya.pharmacy.ledgerMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LedgerMaster_Vld {

    public LedgerMaster_Vld() {
    }

    public static void chkSave(LedgerMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getDisplayName(), "Display Name", 100);

        if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equals("")) {
            DmShared.isValidText(mrTemp.getManualCode(), "Manual Code", 200);
        }
    }

}
