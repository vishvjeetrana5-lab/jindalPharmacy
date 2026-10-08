package dataman.accounts.billHandle.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class BillHandle_Vld {

    public static void chkSave(BillHandle_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getBillTypeCode().trim(), "Bill Handle Type", 100);
        DmShared.isValidText(mrTemp.getDate().trim(), "Date", 200);
//        DmShared.isValidText(mrTemp.getModule().trim(), "Module", 300);
        DmShared.isValidText(mrTemp.getFromLocationCode().trim(), "From Location", 300);
        DmShared.isValidText(mrTemp.getToLocationCode().trim(), "To Location", 400);

//        if (DmShared.xNull(mrTemp.getDepartmentByCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getDepartmentToCode()))) {
//            throw new DmException(400, "Department to and Department by cannot be same.");
////        }
//
        boolean isGrdEmpty = true;
        for (GrdBillDetail_Mdl detailMdl : mrTemp.getGrdData()) {
            if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                isGrdEmpty = false;
                break;
            }
        }
        if (isGrdEmpty) {
            throw new DmException(600, "Please fill Grid details.");
        }

//        boolean flag = false;
//        List<GrdBillDetail_Mdl> listItemDetail = mrTemp.getGrdData();
//        for (GrdBillDetail_Mdl detail_Mdl : listItemDetail) {
//            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
//                flag = true;
//                if (!DmShared.dNull(detail_Mdl.getDate()).trim().equals("")) {
//                    if (DmShared.convertDate(detail_Mdl.getDate()).getTime() < DmShared.convertDate(mrTemp.getDate()).getTime()) {
//                        throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Req. Date should be greater than or equal to Requisition Date in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
//                    }
//                }
//            }
//        }
//        if (!flag) {
//            throw new DmException(600, "Requisition Detail is mandatory.");
//        }
//
    }
}
