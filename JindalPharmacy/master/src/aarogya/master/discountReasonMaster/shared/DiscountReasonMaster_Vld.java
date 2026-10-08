package aarogya.master.discountReasonMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DiscountReasonMaster_Vld {

    public static void chkSave(DiscountReasonMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Discount Reason", 100);
        DmShared.isValidText(mrTemp.getDiscountAccountCode(), "Discount Account", 200);

    }

}
