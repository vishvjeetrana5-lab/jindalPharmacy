package dataman.utility.branchMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BranchMaster_Vld {

    public static void chkSave(BranchMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Branch Name", 100);
        DmShared.isValidText(mrTemp.getBranchCode().trim(), "Branch Code", 200);
        DmShared.isValidText(mrTemp.getCityName().trim(), "City Name", 300);
    }
}
