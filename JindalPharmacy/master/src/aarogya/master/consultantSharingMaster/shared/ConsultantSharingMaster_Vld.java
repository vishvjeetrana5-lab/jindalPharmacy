package aarogya.master.consultantSharingMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class ConsultantSharingMaster_Vld {

    public static void chkSave(ConsultantSharingMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getSharedKeyFieldCode().trim(), "Consultant Name", 100);
        DmShared.isValidText(mrTemp.getApplicableFrom().trim(), "Applicable From", 200);

        boolean flag = false;

        for (GrdSharingDetail_Mdl detail_Mdl : mrTemp.getListSharingDetail()) {
            if ((DmShared.iNull(detail_Mdl.getServiceCode()).longValue() != 0) || (DmShared.iNull(detail_Mdl.getGroupCode()).longValue() != 0)
                    || (!DmShared.xNull(detail_Mdl.getModuleName()).trim().equalsIgnoreCase("")) || (DmShared.iNull(detail_Mdl.getCategoryCode()).longValue() != 0)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new DmException(0, "Sharing detail is mandatory.");
        }
    }
}
