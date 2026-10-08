package dataman.utility.menuCodification.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MenuCodification_Vld {

    public static void chkSave(GrdDescription_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Entry", 100);
    }
}
