package aarogya.pharmacy.environmentSettingSite.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EnvironmentSettingSite_Vld {

    public static void chkSave(EnvironmentSettingSite_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Site", 100);

    }
}
