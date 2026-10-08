package dataman.accounts.cityMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CityMaster_Vld {

    public static void chkSave(CityMaster_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()), "City Name", 100);
        DmShared.isValidAmount(DmShared.xNull(mrTemp.getStateTag()), "State Name", 200);
        DmShared.isValidAmount(DmShared.xNull(mrTemp.getCountryTag()), "Country Name", 300);
    }
}
