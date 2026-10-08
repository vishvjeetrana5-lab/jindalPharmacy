package aarogya.pharmacy.medicineOpening.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class MedicineOpening_Vld {

    public static void chkSave(MedicineOpening_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Voucher Type", 100);
//        DmShared.isValidText(mrTemp.getReceivedByTag(), "Received From", 200);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Voucher Date", 300);
        DmShared.isValidText(mrTemp.getDepartmentTag(), "Department In", 400);

        boolean flag = false;
        List<GrdMedicineDetail_Mdl> listItemDetail = mrTemp.getListItemDetail();
        for (GrdMedicineDetail_Mdl detail_Mdl : listItemDetail) {
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                flag = true;
                if (DmShared.vNull(detail_Mdl.getTotalQuantity()).doubleValue() == 0) {
                    throw new DmException(500, "Please enter Qty. for " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + ".");
                } else if (DmShared.xNull(detail_Mdl.getBatchNo()).equalsIgnoreCase("")) {
                    throw new DmException(500, "Batch No. is mandatory for " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + ".");
                } else if (DmShared.iNull(detail_Mdl.getIsBarcodeActive()).intValue() == 1) {
                    if (DmShared.xNull(detail_Mdl.getBarcode().trim()).equalsIgnoreCase("")) {
                        throw new DmException(500, "Barcode is mandatory for " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + ".");
                    }
                }
            }
        }

        if (!flag) {
            throw new DmException(500, "Medicines are mandatory.");
        }
    }

}
