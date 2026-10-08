package dataman.accounts.stateMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class StateMaster_Vld {

    public static void chkSave(StateMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Department Name", 100);
        DmShared.isValidText(mrTemp.getNumericCode().trim(), "Numeric Code", 200);
        DmShared.isValidText(mrTemp.getTextCode().trim(), "Text Code", 300);
        DmShared.isValidText(mrTemp.getUtCode().trim(), "Union Territories", 400);
    }

}
