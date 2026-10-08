package aarogya.master.categoryPoolMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class CategoryPoolMaster_Vld {

    public static void chkSave(CategoryPoolMaster_Mdl mrTemp) {
//        DmShared.isValidText(mrTemp.getServiceTag().trim(), "Service Category", 100);
//        DmShared.isValidText(mrTemp.getCategoryTag().trim(), "Room Category", 200);
//        DmShared.isValidText(mrTemp.getOrgCode().trim(), "Empanelment", 300);
//        DmShared.isValidText(mrTemp.getModuleName().trim(), "Module Name", 400);

        if ((DmShared.iNull(mrTemp.getServiceCatCode()).longValue() == 0) && (DmShared.iNull(mrTemp.getGroupCode()).longValue() == 0)
                && (DmShared.xNull(mrTemp.getModuleName()).trim().equalsIgnoreCase("")) && (DmShared.iNull(mrTemp.getServiceCode()).longValue() == 0)) {
            throw new DmException(100, "Module Name / Service Category / Service Group / Service is mandatory.");

        }
//        DmShared.isValidText(mrTemp.getGroupCode().trim(), "Service Group Name", 500);
//        DmShared.isValidText(mrTemp.getServiceCode().trim(), "Service Name", 600);
//        DmShared.isValidText(mrTemp.getHeaderConTypeCode().trim(), "Header Consultant", 200);

        if (DmShared.iNull(mrTemp.getHeaderConTypeCode()).intValue() == 0 && DmShared.vNull(mrTemp.getHeaderConAmount()).doubleValue() > 100) {
            throw new DmException(200, " Percentage Can't be greater then 100.");
        }
        if (!DmShared.xNull(mrTemp.getHeaderConTypeCode()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getHeaderConAmount()).equalsIgnoreCase("")) {
            throw new DmException(200, "Header Consultant Amount is mendatory");
        }
        if (DmShared.iNull(mrTemp.getHeaderRefTypeCode()).intValue() == 0 && DmShared.vNull(mrTemp.getHeaderRefAmount()).doubleValue() > 100) {
            throw new DmException(300, " Percentage Can't be greater then 100.");
        }
        if (!DmShared.xNull(mrTemp.getHeaderRefTypeCode()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getHeaderRefAmount()).equalsIgnoreCase("")) {
            throw new DmException(300, "Header Referred Consultant Amount is mendatory");
        }
//        DmShared.isValidText(mrTemp.getHeaderRefTypeCode().trim(), "Header Refeered Consultant", 300);
        if (DmShared.iNull(mrTemp.getLineConTypeCode()).intValue() == 0 && DmShared.vNull(mrTemp.getLineConAmount()).doubleValue() > 100) {
            throw new DmException(400, " Percentage Can't be greater then 100.");
        }
        if (!DmShared.xNull(mrTemp.getLineConTypeCode()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getLineConAmount()).equalsIgnoreCase("")) {
            throw new DmException(400, "Performing Consultant Consultant Amount is mendatory");
        }
//        DmShared.isValidText(mrTemp.getLineConTypeCode().trim(), "Performing Consultant", 400);
        if (DmShared.iNull(mrTemp.getLineRefTypeCode()).intValue() == 0 && DmShared.vNull(mrTemp.getLineRefAmount()).doubleValue() > 100) {
            throw new DmException(500, " Percentage Can't be greater then 100.");
        }
        if (!DmShared.xNull(mrTemp.getLineRefTypeCode()).equalsIgnoreCase("") && DmShared.xNull(mrTemp.getLineRefAmount()).equalsIgnoreCase("")) {
            throw new DmException(500, "Performing Referred Consultant Amount is mendatory");
        }
//        DmShared.isValidText(mrTemp.getLineRefTypeCode().trim(), "Performing Referred Consultant", 500);

    }
}
