package dataman.utility.numberSystem.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class NumberSystem_Vld {

    public static void chkSave(NumberSystem_Mdl mrTemp) {
        boolean flag = false;
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode().trim(), "Voucher Type", 100);
        for (int i = 0; i < mrTemp.getGrdNumberFormat().size(); i++) {
            GrdNumberFormat_Mdl numberFormat = mrTemp.getGrdNumberFormat().get(i);
            if ((!DmShared.xNull(numberFormat.getNumberFormat()).equalsIgnoreCase("")) && (!DmShared.xNull(numberFormat.getCounterFormat()).equalsIgnoreCase(""))) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new DmException(200, "Counter Format is mandatory");
        }
    }

}
