package aarogya.pharmacy.medicineTemplateMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class MedicineTemplateMaster_Vld {

    public static void chkSave(MedicineTemplateMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getTemplateName().trim(), "Template name", 100);

        if (DmShared.xNull(mrTemp.getGrdData().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
            throw new DmException(100, "Medicine details is mandatory");
        }
    }

}
