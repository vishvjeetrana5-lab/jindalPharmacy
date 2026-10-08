package aarogya.master.provisionalStatement.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ProvisionalStatement_Vld {

    public static void chkSave(ProvisionalStatement_Mdl mrTemp) {
        if (mrTemp.getMonth() == null) {
            throw new DmException(100, "Month is mandatory.");
        }
        DmShared.isValidText(mrTemp.getSite(), "Site", 200);
    }
}
