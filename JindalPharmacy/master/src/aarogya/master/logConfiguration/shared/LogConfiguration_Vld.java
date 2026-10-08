package aarogya.master.logConfiguration.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class LogConfiguration_Vld {

    public static void chkSave(LogConfiguration_Mdl mrTemp) {
        //DmShared.isValidText(mrTemp.getSharedKeyFieldCode().trim(), "Module", 100);
        DmShared.isValidText(mrTemp.getMenuName().trim(), "Entry", 100);

    }
}
