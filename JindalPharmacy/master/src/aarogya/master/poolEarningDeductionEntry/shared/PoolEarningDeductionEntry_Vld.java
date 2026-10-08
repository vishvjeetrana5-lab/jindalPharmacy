package aarogya.master.poolEarningDeductionEntry.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PoolEarningDeductionEntry_Vld {

    public static void chkSave(PoolEarningDeductionEntry_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSalCode(), "Salary Period ", 100);
        DmShared.isValidText(mrTemp.getPoolGrpCode(), "Pool Group Name", 200);

//        if (DmShared.convertDate(mrTemp.getFromDate()).getTime() > DmShared.convertDate(mrTemp.getToDate()).getTime()) {
//            throw new DmException(300, "From Date should be less than or equal to To Date.");
//        }
        boolean isGrdEmpty = true;
        for (PoolEarningDeductionEntryGridEdit_Mdl detailMdl : mrTemp.getGateDetails()) {
            if (!DmShared.xNull(detailMdl.getDespName()).trim().equals("")) {
                isGrdEmpty = false;
                break;
            }
        }
        if (isGrdEmpty) {
            throw new DmException(300, "Please fill Grid details.");
        }

    }

}
