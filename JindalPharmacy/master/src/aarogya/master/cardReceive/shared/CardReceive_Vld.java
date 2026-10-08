package aarogya.master.cardReceive.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CardReceive_Vld {

    public static void chkSave(CardReceive_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode(), "Receiev Date", 100);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Card Serial No.", 200);

        if (!DmShared.xNull(mrTemp.getCounterFormat()).equalsIgnoreCase("")) {
            if (DmShared.isNumeric(mrTemp.getCounterFormat())) {
                if (DmShared.iNull(mrTemp.getCounterFormat()).intValue() != 0) {
                    throw new DmException(300, "Only numeric Zero allow.");
                }
            } else {
                throw new DmException(300, "Only numeric Zero allow.");
            }
        }
    }
}
