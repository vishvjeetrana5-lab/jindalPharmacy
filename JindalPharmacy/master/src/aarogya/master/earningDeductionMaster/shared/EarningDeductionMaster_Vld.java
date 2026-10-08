package aarogya.master.earningDeductionMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EarningDeductionMaster_Vld {

    public static void chkSave(EarningDeductionMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getNatureCode().trim(), "Nature", 300);
        DmShared.isValidText(mrTemp.getCalculateOnCode().trim(), "Calculated On", 400);
 
        DmShared.isValidText(mrTemp.getRoundCode().trim(), "Round Off", 500);
    }
}
