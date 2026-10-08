package aarogya.inventory.physicalStockClearance.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PhysicalStockClearance_Vld {

    public static void chkSave(PhysicalStockClearance_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Date On", 100);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Godown", 100);

        if (mrTemp.isInventoryAccountingActive()) {
            if (DmShared.iNull(mrTemp.getBalancingAc()).longValue() == 0) {
                throw new DmException(100, "Please define balancing a/c for Physical Stock Posting.");
            }
        }
    }
}
