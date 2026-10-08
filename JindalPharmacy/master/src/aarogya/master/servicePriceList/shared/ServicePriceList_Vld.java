package aarogya.master.servicePriceList.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ServicePriceList_Vld {

    public static void chkSave(SearchValue_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getOrganizationCode(), "Organization", 100);
    }

    public static void chkSavePercent(ServicePriceMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getOrganizationCode(), "Organization", 100);
        if (mrTemp.getMedicineSaleOn() == null) {
            throw new DmException(200, "Medicine Sale On is mandatory.");
        }
    }
}
