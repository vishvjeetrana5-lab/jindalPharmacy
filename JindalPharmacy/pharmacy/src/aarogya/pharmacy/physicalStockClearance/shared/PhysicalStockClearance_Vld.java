package aarogya.pharmacy.physicalStockClearance.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PhysicalStockClearance_Vld {

    public static void chkSave(PhysicalStockClearance_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Date On", 100);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Department", 100);
    }

}
