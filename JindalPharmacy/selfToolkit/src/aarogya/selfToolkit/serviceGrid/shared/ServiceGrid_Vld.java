package aarogya.selfToolkit.serviceGrid.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.List;

public class ServiceGrid_Vld {

    public static void chkSave(List<ServiceDetail_Mdl> grdServiceLst) {
        String msg = "";
        //=============================< Service Grid Validation >================================================
        if (grdServiceLst.size() > 0) {
            for (ServiceDetail_Mdl serviceDetail_Mdl : grdServiceLst) {

                if (DmShared.iNull(serviceDetail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (DmShared.iNull(serviceDetail_Mdl.getSmfCode()).longValue() == 0) {
                        msg += "<br>" + serviceDetail_Mdl.getSharedKeyFieldName();
                    }
                    if (DmShared.vNull(serviceDetail_Mdl.getAmount()).doubleValue() <= 0) {
                        throw new DmException(0, "ServiceRow", DmShared.iNull(grdServiceLst.indexOf(serviceDetail_Mdl)).toString(), "Please enter Amount in line no. " + DmShared.iNull(grdServiceLst.indexOf(serviceDetail_Mdl) + 1).toString() + ".");
                    }
                }
            }

        }
        if (!DmShared.xNull(msg).trim().equalsIgnoreCase("")) {
            throw new DmException(0, "Followng Services are not valid" + msg);
        }

    }
}
