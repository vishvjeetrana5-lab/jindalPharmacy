package aarogya.pharmacy.physicalStockPosting.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;

public class PhysicalStockPosting_Vld {

    public static void chkSave(PhysicalStockPosting_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Date On", 100);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Department", 100);
    }

}
