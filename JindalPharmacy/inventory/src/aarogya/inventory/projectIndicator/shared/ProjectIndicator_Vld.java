package aarogya.inventory.projectIndicator.shared;

import aarogya.inventory.projectIndicator.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ProjectIndicator_Vld {

    public static void chkSave(ProjectIndicator_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getvTypeDescription().trim(), "Type", 100);
        DmShared.isValidText(mrTemp.getvDate().trim(), "Date", 200);
    }
}
