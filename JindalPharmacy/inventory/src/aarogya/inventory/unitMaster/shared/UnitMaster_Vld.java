package aarogya.inventory.unitMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class UnitMaster_Vld {

    public static void chkSave(UnitMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName().trim(), "Unit Name", 100);
        if (DmShared.iNull(mrTemp.getDecimalPlaces()).intValue() > 4) {
            throw new DmException(200, "Decimal places can't be greater than 4.");
        }
        DmShared.isValidText(mrTemp.getGstUnitCode().trim(), "GST Unit", 300);
    }
}
