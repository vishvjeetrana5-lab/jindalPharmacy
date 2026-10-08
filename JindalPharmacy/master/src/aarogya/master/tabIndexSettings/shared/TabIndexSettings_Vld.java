package aarogya.master.tabIndexSettings.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class TabIndexSettings_Vld {

    public static void chkSave(TabIndexSettings_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldCode().trim(), "Module", 100);
        DmShared.isValidText(mrTemp.getMenuName().trim(), "Entry", 200);
        boolean blnRtn = true;

        if (mrTemp.getGrdTabIndexMdl() != null) {
            for (int i = 0; i < mrTemp.getGrdTabIndexMdl().size(); i++) {
                if (!DmShared.xNull(mrTemp.getGrdTabIndexMdl().get(i).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    if (DmShared.vNull(mrTemp.getGrdTabIndexMdl().get(i).getSerialNo()) == 0) {
                        throw new DmException(600, "Serial No. is mandatory for " + DmShared.xNull(mrTemp.getGrdTabIndexMdl().get(i).getSharedKeyFieldName()) + ".");
                    }

                }
            }
        }

        for (int i = 0; i < mrTemp.getGrdTabIndexMdl().size(); i++) {
            if (!DmShared.xNull(mrTemp.getGrdTabIndexMdl().get(i).getSerialNo()).equalsIgnoreCase("")) {
                for (int j = i + 1; j < mrTemp.getGrdTabIndexMdl().size(); j++) {
                    if (!DmShared.xNull(mrTemp.getGrdTabIndexMdl().get(i).getSerialNo()).equalsIgnoreCase("")) {
                        if (DmShared.xNull(mrTemp.getGrdTabIndexMdl().get(i).getSerialNo()).equalsIgnoreCase(DmShared.xNull(mrTemp.getGrdTabIndexMdl().get(j).getSerialNo()))) {
                            blnRtn = false;
                            throw new DmException(0, "Serial No. is duplicate.");
                        }
                    }
                }
            }
            if (!blnRtn) {
                break;
            }
        }

    }
}
