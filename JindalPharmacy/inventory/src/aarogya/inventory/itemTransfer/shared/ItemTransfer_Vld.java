package aarogya.inventory.itemTransfer.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class ItemTransfer_Vld {

    public static void chkSave(ItemTransfer_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getDepartmentFromTag(), "Godown From", 200);
        DmShared.isValidText(mrTemp.getDepartmentToTag(), "Godown To", 300);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 400);

        if (!DmShared.dNull(mrTemp.getRefDate()).trim().equals("")) {
            if (DmShared.convertDate(DmShared.dNull(mrTemp.getRefDate())).getTime() > DmShared.convertDate(DmShared.dNull(mrTemp.getVoucherDate())).getTime()) {
                throw new DmException(500, "Ref. date can not be greater than voucher date.");
            }
        }

        if (DmShared.iNull(mrTemp.getDepartmentFromTag()).longValue() == DmShared.iNull(mrTemp.getDepartmentToTag()).longValue()) {
            throw new DmException(300, "Godown From and Godown To can not be same.");
        }

        if (DmShared.iNull(mrTemp.getDepartmentFromTag()).longValue() == DmShared.iNull(mrTemp.getDepartmentToTag()).longValue()) {
            throw new DmException(300, "Godown From and Godown To can not be same.");
        }

        boolean flag = false;
        String lineitemCodesCsv = "";
        String requisitionItemChangeCondition = "";

        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (!DmShared.xNull(detail_Mdl.getItemProperty()).trim().equalsIgnoreCase("")) {
                    try {
                        ItemPropertyPopup_Vld.validateProperties(detail_Mdl.getItemProperty(), detail_Mdl.isBestBefore() ? "1" : "0", mrTemp.getVoucherDate(), detail_Mdl.getPropertyMdl());
                    } catch (DmException e) {
                        throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please fill all properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }
                if (DmShared.iNull(detail_Mdl.getIbCode()).longValue() == 0) {
                    throw new DmException(600, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please re-select the item to refresh its properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }

                /*==============================<< Create CSV for restricted department check >>===============================*/
                if (!lineitemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineitemCodesCsv += ",";
                }
                lineitemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());

                /*==============================<< Create condition string for PO item change check >>===============================*/
                if (!requisitionItemChangeCondition.trim().equalsIgnoreCase("")) {
                    requisitionItemChangeCondition += " OR ";
                } else {
                    requisitionItemChangeCondition += " WHERE ";
                }
                requisitionItemChangeCondition += " (rd.docId = '" + DmShared.xNull(detail_Mdl.getRequisitionDocId()) + "'";
                requisitionItemChangeCondition += " AND rd.v_SNo = '" + DmShared.xNull(detail_Mdl.getRequisitionV_SNo()) + "'";
                requisitionItemChangeCondition += " AND rd.itemCode != '" + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()) + "')";

            }
        }

        mrTemp.setLineItemCodesCsv(lineitemCodesCsv);
        mrTemp.setRequisitionItemChangeCondition(requisitionItemChangeCondition);

        if (!flag) {
            throw new DmException(600, "Items are mandatory.");
        }
    }

}
