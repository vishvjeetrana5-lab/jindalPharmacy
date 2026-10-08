package aarogya.master.receiptBookMapping.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ReceiptBookMapping_Vld {

    public static void chkSave(ReceiptBookMapping_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode().trim(), "Entry Point Name", 100);
    }

}
