package dataman.accounts.countryMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CountryMaster_Vld {

    public static void chkSave(CountryMaster_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "Country Name", 100);
//        DmShared.isValidText(DmShared.xNull(mrTemp.getCurrencyCode()), "Currency Name", 200);
    }
}
