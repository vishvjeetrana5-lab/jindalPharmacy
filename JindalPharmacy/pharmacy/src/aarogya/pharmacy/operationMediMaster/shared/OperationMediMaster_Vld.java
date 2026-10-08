package aarogya.pharmacy.operationMediMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class OperationMediMaster_Vld {

    public static void chkSave(OperationMediMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode().trim(), "Operation Name", 100);

        if (DmShared.xNull(mrTemp.getGrdData().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
            throw new DmException(500, "Medicine Details is mandatory");
        }
    }
}
