package dataman.accounts.tdsCategoryDescriptionMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class TdsCategoryDescriptionMaster_Vld {

    public static void chkSave(TdsCategoryDescriptionMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "TDS Description", 100);
        DmShared.isValidText(mrTemp.getManualCode(), "Manual Code", 200);
    }

}
