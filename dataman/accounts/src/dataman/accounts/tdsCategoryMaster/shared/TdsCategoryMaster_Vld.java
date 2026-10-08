package dataman.accounts.tdsCategoryMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class TdsCategoryMaster_Vld {

    public static void chkSave(TdsCategoryMaster_Mdl mrTemp) {
        List<GrdTdsCategory_Mdl> listData;
        boolean flagGridData = false;

        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "TDS Description", 100);

        listData = mrTemp.getGrdData();
        if (listData != null) {
            for (int i = 0; i < listData.size(); i++) {
                if (!DmShared.xNull(listData.get(i).getDescriptionCode()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(listData.get(i).getPostingAccountCode()).trim().equalsIgnoreCase("")) {

                        throw new DmException(300, "Please Define Posting A/c For Description: " + listData.get(i).getDescription() + "");
                    }

//                    for (int j = i + 1; j < listData.size(); j++) {
//                        if (DmShared.xNull(listData.get(j).getDescriptionCode()).equalsIgnoreCase(listData.get(i).getDescriptionCode())) {
//                            throw new DmException(300, "Duplicate Record Defined For Description: " + listData.get(i).getDescription() + "");
//                        }
//                    }

                    if (!flagGridData) {
                        flagGridData = true;
                    }
                }
            }
        }

        if (!flagGridData) {
            throw new DmException(200, "Please Fill Entry");
        }

    }

}
