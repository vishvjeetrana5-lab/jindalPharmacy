package aarogya.inventory.itemReceive.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class ItemReceive_Vld {

    public static void chkSave(ItemReceive_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Godown In", 300);

        if (mrTemp.isInventoryAccountingActive()) {
            if (DmShared.iNull(mrTemp.getBalancingAc()).longValue() == 0) {
                throw new DmException(100, "Please define balancing a/c for " + mrTemp.getVoucherTypeName());
            }
        }

        if (!DmShared.dNull(mrTemp.getRefDate()).equalsIgnoreCase("")) {
            if (DmShared.convertDate(mrTemp.getRefDate()).getTime() > DmShared.convertDate(mrTemp.getVoucherDate()).getTime()) {
                throw new DmException(400, "Ref. Date cannot be greater than Receive Date");
            }
        }

        boolean flag = false;
        String lineitemCodesCsv = "";
        List<GrdItemDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdItemDetail_Mdl detail_Mdl : listItemDetail) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (DmShared.iNull(detail_Mdl.getIbCode()).longValue() == 0) {
                    throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Please re-select the item to refresh its properties in line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                }
                if (mrTemp.isInventoryAccountingActive()) {
                    if (DmShared.iNull(detail_Mdl.getIsubCode()).longValue() == 0) {
                        throw new DmException(500, "GridRow", DmShared.iNull(listItemDetail.indexOf(detail_Mdl)).toString(), "Inventory a/c is required for line no. " + DmShared.iNull(listItemDetail.indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                }

                /*==============================<< Create CSV for restricted department check >>===============================*/
                if (!lineitemCodesCsv.trim().equalsIgnoreCase("")) {
                    lineitemCodesCsv += ",";
                }
                lineitemCodesCsv += DmShared.xNull(detail_Mdl.getSharedKeyFieldCode());
            }

        }
        mrTemp.setLineItemCodesCsv(lineitemCodesCsv);

        if (!flag) {
            throw new DmException(500, "Items are mandatory.");
        }
    }

}
