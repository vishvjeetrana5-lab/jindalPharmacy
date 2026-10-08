package aarogya.pharmacy.requisition.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import java.util.List;

public class Requisition_Vld {

    public static void chkSave(Requisition_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getDate().trim(), "Date", 100);
        DmShared.isValidText(mrTemp.getRequisitionTypeCode().trim(), "Requisition Type", 200);
        DmShared.isValidText(mrTemp.getSubCode().trim(), "Requisition By", 300);
        DmShared.isValidText(mrTemp.getDepartmentByCode().trim(), "Department By", 400);
        DmShared.isValidText(mrTemp.getDepartmentToCode().trim(), "Department To", 500);
        if (DmShared.xNull(mrTemp.getDepartmentByCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getDepartmentToCode()))) {
            throw new DmException(400, "Department to and Department by cannot be same.");
        }

        boolean flag = false;
        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getGrdData();
        for (GrdMedicineDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (!DmShared.dNull(detail_Mdl.getDate()).trim().equals("")) {
                    if (DmShared.convertDate(detail_Mdl.getDate()).getTime() < DmShared.convertDate(mrTemp.getDate()).getTime()) {
                        throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Req. Date should be greater than or equal to Requisition Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
            }
        }
        if (!flag) {
            throw new DmException(600, "Requisition Detail is mandatory.");
        }

    }
}
