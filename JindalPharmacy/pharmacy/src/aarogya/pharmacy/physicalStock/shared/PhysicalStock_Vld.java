package aarogya.pharmacy.physicalStock.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class PhysicalStock_Vld {

    public static void chkSave(PhysicalStock_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getVoucherTypeCode(), "Issue Type", 100);
        DmShared.isValidText(mrTemp.getVoucherDate(), "Issue Date", 200);
        DmShared.isValidText(mrTemp.getDepartmentCode(), "Department In", 300);

        boolean flag = false;

        for (GrdMedicineDetail_Mdl detail_Mdl : mrTemp.getListMedicineDetail()) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
//                if (DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() == 0) {
//                    throw new DmException(400, "GridRow", DmShared.iNull(listMedicineDetail.indexOf(detail_Mdl)).toString(), "Please enter Qty. in line no. " + DmShared.iNull(listMedicineDetail.indexOf(detail_Mdl) + 1).toString() + ".");
//                }
                if (DmShared.vNull(detail_Mdl.getIbCode()).doubleValue() == 0) {
                    throw new DmException(400, "GridRow", DmShared.iNull(mrTemp.getListMedicineDetail().indexOf(detail_Mdl)).toString(), "Please enter IbCode in line no. " + DmShared.iNull(mrTemp.getListMedicineDetail().indexOf(detail_Mdl) + 1).toString() + ".");
                }
            }
        }

        if (!flag) {
            throw new DmException(400, "Medicines are mandatory.");
        }
    }

}
