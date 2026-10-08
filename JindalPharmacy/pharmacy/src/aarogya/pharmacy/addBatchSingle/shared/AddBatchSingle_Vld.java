package aarogya.pharmacy.addBatchSingle.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class AddBatchSingle_Vld {

    public static void chkSave(Batch_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getIpm().getBatchNo(), "Batch No.", 100);
        DmShared.isValidAmount(mrTemp.getIpm().getMrp(), "MRP", 200);
        DmShared.isValidAmount(mrTemp.getSaleRate(), "Sale Rate", 300);

        if (DmShared.vNull(mrTemp.getSaleRate()).doubleValue() > DmShared.vNull(mrTemp.getIpm().getMrp()).doubleValue()) {
            throw new DmException(300, "Sale rate should not be greater than MRP.");
        }

        if (mrTemp.isIsBestBefore()) {
            if (DmShared.dNull(mrTemp.getIpm().getManufacturingDate()).equalsIgnoreCase("")) {
                throw new DmException(400, "Mfg. Date is mandatory.");
            }
        }

        if (DmShared.dNull(mrTemp.getIpm().getExpiryDate()).equalsIgnoreCase("")) {
            throw new DmException(500, "Expiry Date is mandatory.");
        }

        if (DmShared.convertDate(mrTemp.getIpm().getExpiryDate()).getTime() <= DmShared.convertDate(DmShared.dNull(mrTemp.getEntryDate())).getTime()) {
            throw new DmException(500, "Expiry date should be greater than " + DmShared.dNull(mrTemp.getEntryDate()));
        }
    }
}
