package dataman.payroll.earningDeductionMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class EarningDeductionMaster_Vld {

    public static void chkSave(EarningDeductionMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 100);
        DmShared.isValidText(mrTemp.getShortName().trim(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getNatureCode().trim(), "Nature", 300);
        DmShared.isValidText(mrTemp.getCalculateOnCode().trim(), "Calculated On", 400);
        DmShared.isValidText(mrTemp.getRoundCode().trim(), "Round Off", 500);
        DmShared.isValidText(mrTemp.getBasedOnCode().trim(), "Based On", 800);
        DmShared.isValidText(mrTemp.getUseInEntryCode().trim(), "Use In Entry", 1000);
        if (DmShared.vNull(mrTemp.getLimitOfAmount()).doubleValue()>0){
            DmShared.isValidText(mrTemp.getLimitForCalsRes().trim(), "Limit for Calculation/Restriction", 900);
        }
    }
}
