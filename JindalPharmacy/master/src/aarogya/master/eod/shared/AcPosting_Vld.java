package aarogya.master.eod.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class AcPosting_Vld {

    public static void chkSave(AcPosting_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getForDate().trim(), "For Date", 100);
    }

    public static void chkPostingData(List<GrdAcPosting_Mdl> listData) {
        if (listData != null && listData.size() > 0) {
            for (GrdAcPosting_Mdl mdl : listData) {
                if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() == 0) {
                    throw new DmException(0, "Please set Account in " + DmShared.xNull(mdl.getServiceName()).trim() + ".");
                }
                if (DmShared.iNull(mdl.getContraSubCode()).longValue() == 0) {
                    throw new DmException(0, "Control A/c is mandatory.");
                }
            }
        } else {
            throw new DmException(100, "There is no data for posting.");
        }
    }

}
