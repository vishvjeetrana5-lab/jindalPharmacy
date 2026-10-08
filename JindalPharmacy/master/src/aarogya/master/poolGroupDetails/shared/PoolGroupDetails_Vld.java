package aarogya.master.poolGroupDetails.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PoolGroupDetails_Vld {

    public static void chkSave(PoolGroupDetails_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getPoolGroupCode(), "Pool Group Name", 100);
        DmShared.isValidText(mrTemp.getFromDate(), "From Date", 200);
        DmShared.isValidText(mrTemp.getToDate(), "To Date", 300);
        if (DmShared.convertDate(mrTemp.getFromDate()).getTime() > DmShared.convertDate(mrTemp.getToDate()).getTime()) {
            throw new DmException(300, "From Date should be less than or equal to To Date.");
        }
        boolean isGrdEmpty = true;
        for (PoolGroupDetailsGridEdit_Mdl detailMdl : mrTemp.getGateDetails()) {
            if (!DmShared.xNull(detailMdl.getSharedKeyFieldName()).trim().equals("")) {
                isGrdEmpty = false;
                break;
            }
        }
        if (isGrdEmpty) {
            throw new DmException(400, "Please fill Grid details.");
        }
        for (PoolGroupDetailsGridEdit_Mdl detailMdl : mrTemp.getGateDetails()) {
            if (DmShared.xNull(detailMdl.getValueName()).equalsIgnoreCase("%")) {
                if (mrTemp.getTotal() != 100) {
                    throw new DmException(400, "Amount can't less than Or Greater than 100.");
                }

            }
        }
    }

}
