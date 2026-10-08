package aarogya.selfToolkit.fifoPopUp.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class FifoPopUp_Vld {

    public FifoPopUp_Vld() {
    }

    public static void chkSave(FifoSetting_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getModuleType().trim(), "Module Type", 100);

        if (DmShared.xNull(mrTemp.getItemCode()).trim().equalsIgnoreCase("")
                && DmShared.xNull(mrTemp.getGroupCode()).trim().equalsIgnoreCase("")
                && DmShared.xNull(mrTemp.getDepartmentCode()).trim().equalsIgnoreCase("")
                && DmShared.xNull(mrTemp.getCategoryCode()).trim().equalsIgnoreCase("")) {
            throw new DmException(200, "At least one parameter selection is mandatory.");
        }
    }
}
