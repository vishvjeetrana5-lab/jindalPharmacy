package aarogya.selfToolkit.prescriptionCustomItem.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class PrescriptionCustomItem_Shared {

    public static String getFormattedString(List<GrdPrescriptionCustomItem_Mdl> listData) {
        String returnValue = "";

        if (listData != null) {
            for (GrdPrescriptionCustomItem_Mdl node : listData) {
                if ((!DmShared.xNull(node.getSharedKeyFieldName()).trim().equalsIgnoreCase(""))
                        || (!DmShared.xNull(node.getRemark()).trim().equalsIgnoreCase(""))) {
                    returnValue += DmShared.xNull(node.getRemark()).trim().equalsIgnoreCase("") ? (DmShared.xNull(node.getSharedKeyFieldName()).trim() + "; ")
                            : (DmShared.xNull(node.getSharedKeyFieldName()).trim() + " (" + DmShared.xNull(node.getRemark()).trim() + ")") + "; ";
                }
            }
        }

        return returnValue.trim();
    }
}
