package dataman.accounts.nationalityMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class NationalityMaster_Vld {

    public static void chkSave(NationalityMaster_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Currency", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getCountryLocale()), "Country Locale", 200);
    }

}
