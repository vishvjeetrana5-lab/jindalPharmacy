package aarogya.dashBoard.self.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class Medicine_Shared {

    public static String getDisplayQuantity(String quantity, String conversionRate) throws DmException {

        String displayQty = "";
        if (DmShared.iNull(conversionRate).doubleValue() > 0) {
            displayQty = DmShared.iNull(DmShared.vNull(quantity).doubleValue() / DmShared.vNull(conversionRate).doubleValue()).intValue() + " : " + DmShared.iNull(DmShared.vNull(quantity).doubleValue() % DmShared.vNull(conversionRate).doubleValue()).intValue();
        } else {
            displayQty = "0 : " + DmShared.iNull(quantity).intValue();
        }

        return displayQty;

    }

}
