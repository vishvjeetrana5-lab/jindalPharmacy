package dataman.utility.fileTypeMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class FileTypeMaster_Vld {

    public static void chkSave(FileTypeMaster_Mdl mrTemp) {
        boolean isDefault = false;
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim(), "Rule Code", 100);

        for (GrdFileTypeMaster_Mdl grdFileTypeMaster : mrTemp.getGrdData()) {
            if (grdFileTypeMaster != null) {
                if (DmShared.vNull(grdFileTypeMaster.getDefaultCode()).doubleValue() == 1) {
                    if (isDefault) {
                        throw new DmException(200, "Only one file type can be default.");
                    }
                    isDefault = true;
                }
            }
        }

        if (!isDefault) {
            throw new DmException(200, "Please mention default file type.");
        }
    }
}
