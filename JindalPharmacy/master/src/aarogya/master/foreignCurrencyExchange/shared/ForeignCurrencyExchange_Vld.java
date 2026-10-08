package aarogya.master.foreignCurrencyExchange.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ForeignCurrencyExchange_Vld {

    public static void chkSave(ForeignCurrencyExchange_Mdl mrTemp, String currentDate) {
        DmShared.isValidText(mrTemp.getExchangeDate(), "Exchange Rate for Date", 100);
        if (DmShared.convertDate(mrTemp.getExchangeDate()).getTime() <= DmShared.convertDate(currentDate).getTime()) {
            throw new DmException(0, "Exchange Date should be greater than Current Date.");
        }
        boolean exists = false;
        for (GrdForeignCurrencyExchange_Mdl node : mrTemp.getLstCurrencyExchange()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                exists = true;
                if (DmShared.vNull(node.getRate()).doubleValue() == 0) {
                    throw new DmException(200, "Conversion Rate is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()) + ".");
                }
            }
        }
        if (!exists) {
            throw new DmException(200, "Currency is mandatory.");
        }

    }

}
