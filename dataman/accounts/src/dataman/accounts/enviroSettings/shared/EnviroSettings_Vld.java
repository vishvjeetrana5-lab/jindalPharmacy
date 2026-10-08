package dataman.accounts.enviroSettings.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EnviroSettings_Vld {

    public static void chkSave(EnviroSettings_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Site", 100);
        DmShared.isValidText(mrTemp.getTdsRoundOffTag(), "TDS Round Off", 200);
        DmShared.isValidText(mrTemp.getGstRoundOffTag(), "GST Round Off", 300);
    }
}
