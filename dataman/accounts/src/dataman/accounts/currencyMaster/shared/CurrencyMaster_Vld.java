package dataman.accounts.currencyMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CurrencyMaster_Vld {

    public static void chkSave(CurrencyMaster_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Currency", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getCountryLocale()), "Country Locale", 200);
    }

}
