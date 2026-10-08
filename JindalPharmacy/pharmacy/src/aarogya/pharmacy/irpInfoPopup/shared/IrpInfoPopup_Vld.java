package aarogya.pharmacy.irpInfoPopup.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class IrpInfoPopup_Vld {

    public static void chkSave(IrpInfoPopup_Mdl mrTemp) {        
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Name", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getIrpTypeTag()), "Type", 200);
        DmShared.isValidText(DmShared.xNull(mrTemp.getUrl()), "URL", 300);
        DmShared.isValidText(mrTemp.getIsActiveCode().trim(), "Active", 400);

    }

}
