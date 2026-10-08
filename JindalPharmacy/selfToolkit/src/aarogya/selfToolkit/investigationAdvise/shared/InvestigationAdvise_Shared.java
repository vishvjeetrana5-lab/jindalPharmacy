package aarogya.selfToolkit.investigationAdvise.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class InvestigationAdvise_Shared {

    public static String getFormattedString(List<GrdInvestigation_Mdl> listData) {
        String returnValue = "";

        if (listData != null) {
            for (GrdInvestigation_Mdl node : listData) {
                if (!DmShared.xNull(node.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                    returnValue += DmShared.xNull(node.getSharedKeyFieldName()).trim() + "; ";
                }
            }
        }

        return returnValue.trim();
    }
}
