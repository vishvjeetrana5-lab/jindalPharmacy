package aarogya.master.referredMaster.shared;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ReferredMaster_Vld {

    public static void chkSave(ReferredMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Referred name", 200);
        DmShared.isValidText(mrTemp.getDisplayName(), "Display Name", 500);

        if (!DmShared.xNull(mrTemp.geteMail()).equalsIgnoreCase("")) {
            DmShared.isValidEMail(DmShared.xNull(mrTemp.geteMail()), 600);
        }
        if (!DmShared.xNull(mrTemp.getDoa()).trim().equalsIgnoreCase("") && !DmShared.xNull(mrTemp.getDob()).trim().equalsIgnoreCase("")) {
            if (DmShared.convertDate(DmShared.dNull(mrTemp.getDoa())).getTime() <= DmShared.convertDate(DmShared.dNull(mrTemp.getDob())).getTime()) {
                throw new DmException(700, "Anniversary date should not be less than or equal to Date of Birth");
            }
        }
        DmShared.isValidText(mrTemp.getActiveTag(), "Active", 800);
    }

}
