package aarogya.selfToolkit.allergy.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class Allergy_Shared {

    public static String getFormattedString(List<GrdAllergy_Mdl> listData) {
        String returnValue = "";

        if (listData != null) {
            for (GrdAllergy_Mdl node : listData) {
                if (!DmShared.xNull(node.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                    returnValue += DmShared.xNull(node.getSharedKeyFieldName()).trim() + "; ";
                }
            }
        }

        return returnValue.trim();
    }
}
