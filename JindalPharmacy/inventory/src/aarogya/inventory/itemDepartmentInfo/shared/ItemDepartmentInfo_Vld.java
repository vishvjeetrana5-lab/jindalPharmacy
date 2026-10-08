package aarogya.inventory.itemDepartmentInfo.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import java.util.List;

public class ItemDepartmentInfo_Vld {

    public static void chkSave(ItemDepartmentInfo_Mdl mrTemp) {
        DmShared.isValidAmount(mrTemp.getSiteCode().trim(), "Site Name", 100);
        DmShared.isValidAmount(mrTemp.getDepartmentCode().trim(), "Department", 200);
        boolean flag = false;
        List<GrdItemDepartmentInfo_Mdl> listItemDetail = mrTemp.getGrdMed_Mdl();
        for (GrdItemDepartmentInfo_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                flag = true;

                if (DmShared.vNull(detail_Mdl.getMinStock()).doubleValue() > DmShared.vNull(detail_Mdl.getMaxStock()).doubleValue()) {
                    throw new DmException(600, "check the index", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), " Min. Level cannot greater than Max. Level check the index " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                if ((DmShared.vNull(detail_Mdl.getReOrder()).doubleValue() < DmShared.vNull(detail_Mdl.getMinStock()).doubleValue()) || (DmShared.vNull(detail_Mdl.getReOrder()).doubleValue() > DmShared.vNull(detail_Mdl.getMaxStock()).doubleValue())) {
                    throw new DmException(600, "check the index", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Re-Order Level cannot is greater than  Max. Level or less than Min. Level check the index " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

            }

        }
        if (!flag) {
            throw new DmException(600, "Medicine Name is Mandataory");
        }
    }

}
