package aarogya.master.rateListMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class RateListMaster_Vld {

    public static void chkSave(SearchValue_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getRateListCode(), "Rate List", 100);
    }

    public static void chkSavePercent(RateListMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getRateListCode(), "Rate List", 100);
        if (mrTemp.getMedicineSaleOn() == null) {
            throw new DmException(200, "Medicine Sale On is mandatory.");
        }
    }
}
