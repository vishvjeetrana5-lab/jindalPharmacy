package dataman.accounts.defineCostCenter.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DefineCostCenter_Vld {

    public static void chkSave(DefineCostCenter_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Cost center", 100);
        DmShared.isValidText(mrTemp.getManualCode(), "Manual code", 200);
        DmShared.isValidText(mrTemp.getActiveCode(), "Acitve", 300);
    }

}
