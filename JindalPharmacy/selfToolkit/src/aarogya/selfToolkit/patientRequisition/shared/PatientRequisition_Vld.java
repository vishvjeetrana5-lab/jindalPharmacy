package aarogya.selfToolkit.patientRequisition.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class PatientRequisition_Vld {

    public static void checkSave(PatientRequisition_Mdl mrTemp) {
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getRequisitionByTag()).doubleValue(), "Requisition By", 100);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getDepartmentByTag()).doubleValue(), "Department By", 200);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getDepartmentToTag()).doubleValue(), "Department To", 300);
        DmShared.isValidAmount(DmShared.iNull(mrTemp.getDeptToSiteCode()).doubleValue(), "Site To", 350);
        if (DmShared.xNull(mrTemp.getDepartmentByTag()).equalsIgnoreCase(DmShared.xNull(mrTemp.getDepartmentToTag()))) {
            throw new DmException(700, "Department To and Department By cannot be same.");
        }

        boolean flag = false;

        for (GrdRequisitionMedicine_Mdl detail_Mdl : mrTemp.getLstMedicine()) {
            if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                flag = true;
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    if (DmShared.vNull(detail_Mdl.getQty()).doubleValue() <= 0) {
                        throw new DmException(700, "Req. Qty is mandatory for  " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + ".");
                    } else if (DmShared.xNull(detail_Mdl.getReqDate()).equalsIgnoreCase("")) {
                        throw new DmException(700, "Req. Date is mandatory for  " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + ".");
                    } else if (DmShared.xNull(detail_Mdl.getReqTime()).equalsIgnoreCase("")) {
                        throw new DmException(600, "Req. Time is mandatory for  " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + ".");
                    }
                }
            }
        }
        if (!flag) {
            throw new DmException(0, "Medicine Detail is mandatory.");
        }
    }

}
