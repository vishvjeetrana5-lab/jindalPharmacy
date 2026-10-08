package aarogya.pharmacy.profitSlab.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ProfitSlab_Vld {

    public static void chkSave(ProfitSlab_Mdl mrTemp) {
        if (DmShared.vNull(mrTemp.getMarginFrom()).doubleValue() > DmShared.vNull(mrTemp.getMarginTo()).doubleValue()) {
            throw new DmException(100, "Profit Margin from should be less than margin to.");
        }
    }
}
