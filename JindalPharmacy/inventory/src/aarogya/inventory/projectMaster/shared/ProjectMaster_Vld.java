package aarogya.inventory.projectMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ProjectMaster_Vld {

    public static void chkSave(ProjectMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Project Name", 100);
        DmShared.isValidText(mrTemp.getManualCode().trim(), "Code", 400);
        DmShared.isValidText(mrTemp.getProjectAcName().trim(), "Project A/c", 300);
        DmShared.isValidText(mrTemp.getProjectHeadName().trim(), "Project Head", 400);
    }
}
