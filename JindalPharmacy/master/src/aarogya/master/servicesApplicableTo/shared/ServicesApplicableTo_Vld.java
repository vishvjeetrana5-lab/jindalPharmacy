package aarogya.master.servicesApplicableTo.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ServicesApplicableTo_Vld {

    public static void chkSave(ServicesApplicableTo_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim(), "Voucher Type", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getAcknowledgeCode()).trim(), "AutoAcknowledge", 200);
    }
}
