package aarogya.master.packageDetail.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;

public class PackageDetail_Vld {

    public static void chkSave(PackageDetail_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getPackageCode(), "Package Name", 0);
        DmShared.isValidText(mrTemp.getOrganizationCode(), "Organization", 0);
        DmShared.isValidText(mrTemp.getFromDate(), "From Date", 100);
        DmShared.isValidText(mrTemp.getToDate(), "To Date", 200);
        DmShared.isValidText(mrTemp.getActiveCode(), "Active", 300);

        if ((DmShared.convertDate(mrTemp.getToDate()).getTime() < (DmShared.convertDate(mrTemp.getFromDate()).getTime()))) {
            throw new DmException(200, "From Date should be less than or equal to To Date.");
        }

        boolean flag = false;
        for (GrdServiceMaster_Mdl node : mrTemp.getListService()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                flag = true;
                break;
//                if (DmShared.vNull(node.getQuantity()).doubleValue() == 0) {
//                    throw new DmException(0, "Quantity is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()).trim() + ".");
//                }
//                if (DmShared.vNull(node.getPkgRate()).doubleValue() == 0) {
//                    throw new DmException(0, "Package Rate is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()).trim() + ".");
//                }
            }
        }
        if (!flag) {
            throw new DmException(0, "Service is Mandatory.");
        }

        for (GrdServiceMaster_Mdl node : mrTemp.getListMedicineItem()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                if (DmShared.vNull(node.getQuantity()).doubleValue() == 0) {
                    throw new DmException(0, "Quantity is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()).trim() + ".");
                }
            }
        }

        for (GrdServiceMaster_Mdl node : mrTemp.getListInvestigation()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                if (DmShared.vNull(node.getQuantity()).doubleValue() == 0) {
                    throw new DmException(0, "Quantity is mandatory for " + DmShared.xNull(node.getSharedKeyFieldName()).trim() + ".");
                }
            }
        }
    }
}
