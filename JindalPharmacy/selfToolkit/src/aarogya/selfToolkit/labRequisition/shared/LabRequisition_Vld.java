package aarogya.selfToolkit.labRequisition.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class LabRequisition_Vld {

    public static void checkSave(LabRequisition_Mdl mrTemp) {

        DmShared.isValidAmount(DmShared.iNull(mrTemp.getRequisitionByTag()).doubleValue(), "Requisition By", 100);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getDepartmentByTag()).doubleValue(), "Department By", 200);

        if (mrTemp.getLstRequisition() != null) {
            for (GrdLabRequisition_Mdl node : mrTemp.getLstRequisition()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    if (DmShared.vNull(node.getDefaultDepartmentCode()) == 0) {
                        throw new DmException(600, "Department To is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()) + ".");
                    }
                    if (DmShared.vNull(node.getSiteCode()) == 0) {
                        throw new DmException(600, "Site To is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()) + ".");
                    }
                    if (DmShared.xNull(node.getReqDate()).equalsIgnoreCase("")) {
                        throw new DmException(700, "Req. Date is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()) + ".");
                    }
                    if (DmShared.xNull(node.getReqTime()).equalsIgnoreCase("")) {
                        throw new DmException(800, "Req. Time is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()) + ".");
                    }
                }
            }
        }
    }
}
