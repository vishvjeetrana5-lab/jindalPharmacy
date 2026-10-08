package aarogya.inventory.wipToStockConversion.shared;

import aarogya.inventory.wipToStockConversion.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class WipToStockConversion_Vld {

    public static void chkSave(WipToStockConversion_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Godown In", 300);

        if (mrTemp.isInventoryAccountingActive()) {
            if (DmShared.iNull(mrTemp.getBalancingAc()).longValue() == 0) {
                throw new DmException(100, "Please define balancing a/c for " + mrTemp.getVoucherTypeName());
            }
        }

        if (DmShared.vNull(mrTemp.getProjectCurrentBalance())<=0) {
                throw new DmException(400, "Insufficient balance in Project A/c.");
        }
        
        if (DmShared.vNull(mrTemp.getProjectCurrentBalance())-DmShared.vNull(mrTemp.getTotalStockValue())+DmShared.vNull(mrTemp.getOldTotalStkValue())<0) {
                throw new DmException(700, "Stock value can't be greater than Project A/c balance.");
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
