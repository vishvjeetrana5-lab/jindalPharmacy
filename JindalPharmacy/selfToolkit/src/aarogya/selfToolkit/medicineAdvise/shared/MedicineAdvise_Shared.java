package aarogya.selfToolkit.medicineAdvise.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class MedicineAdvise_Shared {

    public static String getFormattedString(List<GrdMedicineDetail_Mdl> listData) {
        String returnValue = "";

        if (listData != null) {
            for (GrdMedicineDetail_Mdl node : listData) {
                if (!DmShared.xNull(node.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                    returnValue += DmShared.xNull(node.getSharedKeyFieldName()).trim() + " ("
                            + DmShared.xNull(node.getDoseDisplayText()).trim() + ") * "
                            + DmShared.xNull(node.getDuration()).trim() + "; ";
                }
            }
        }

        return returnValue.trim();
    }
}
