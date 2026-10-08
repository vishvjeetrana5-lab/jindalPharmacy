package aarogya.pharmacy.schemeMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class SchemeMaster_Vld {

    public SchemeMaster_Vld() {
    }

    public static void chkSave(SchemeMaster_Mdl mrTemp) {
        DmShared.isValidText(DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim(), "Item Name", 100);
        DmShared.isValidText(DmShared.xNull(mrTemp.getItemUnitCode()).trim(), "Unit", 100);

        boolean flag = false, flagIsChecked = false;
        if (mrTemp.getLstSchemeDetails() != null) {
            List<GrdSchemeDetails_Mdl> listItemDetail = mrTemp.getLstSchemeDetails();
            for (GrdSchemeDetails_Mdl detail_Mdl : listItemDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    flag = true;
                    if (DmShared.iNull(detail_Mdl.getFreeItemUnitCode()).longValue() == 0) {
                        throw new DmException(200, "GridRow", DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl)).toString(), "Please enter Unit in line no. " + DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(detail_Mdl.getSchemeOnQty()).doubleValue() == 0) {
                        throw new DmException(200, "GridRow", DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl)).toString(), "Please enter Scheme On Qty. in line no. " + DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.vNull(detail_Mdl.getSchemeFreeQty()).doubleValue() == 0) {
                        throw new DmException(200, "GridRow", DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl)).toString(), "Please enter Scheme Free Qty. in line no. " + DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.xNull(detail_Mdl.getDescription()).equalsIgnoreCase("")) {
                        throw new DmException(200, "GridRow", DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl)).toString(), "Please enter Description in line no. " + DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl) + 1).toString() + ".");
                    }
                    if (DmShared.xNull(detail_Mdl.getIsChecked()).equalsIgnoreCase("1")) {
                        flagIsChecked = true;
                        if (detail_Mdl.getBatchNo_Mdl() != null) {
                            if (DmShared.iNull(detail_Mdl.getBatchNo_Mdl().getSharedKeyFieldCode()).longValue() == 0) {
                                throw new DmException(200, "GridRow", DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl)).toString(), "Please enter Batch No in line no. " + DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl) + 1).toString() + ".");
                            }
                        } else {
                            throw new DmException(200, "GridRow", DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl)).toString(), "Please enter Batch No in line no. " + DmShared.iNull(mrTemp.getLstSchemeDetails().indexOf(detail_Mdl) + 1).toString() + ".");
                        }
                    }
                }
            }
        }
        if (!flag) {
            throw new DmException(200, "GridRow", "0", "Scheme Details are mandatory.");
        }
        if (!flagIsChecked) {
            throw new DmException(200, "GridRow", "1", "Please check mark a record to continue.");
        }
    }

}
