package aarogya.inventory.gstServiceMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class GstServiceMaster_Vld {

    public GstServiceMaster_Vld() {
    }

    public static void chkSave(GstServiceMaster_mdl mrTemp) {

        DmShared.isValidText(mrTemp.getCategoryTag(), "Category", 200);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Name", 300);
        DmShared.isValidText(mrTemp.getGroupTag(), "Group", 400);
        DmShared.isValidText(mrTemp.getSkuTag(), "SKU", 500);
        DmShared.isValidText(mrTemp.getActiveTag(), "Active", 600);
        DmShared.isValidText(mrTemp.getTypeTag(), "Type", 700);

        boolean isItsRateSlabExists = false;
        for (GrdItsRateSlab_Mdl grdRateITS_Mdl : mrTemp.getGrdRateIts_Mdls()) {
            if (!DmShared.xNull(grdRateITS_Mdl.getGstTypeCode()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("")) {
                    throw new DmException(19000, "GST Based On is mandetory");
                }

                if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("1")) {
                    if (!isItsRateSlabExists) {
                        isItsRateSlabExists = true;
                        break;
                    }
                }
            }
        }

        if (DmShared.xNull(mrTemp.getTaxBasedOnTag()).equalsIgnoreCase("1")) {
            if (!isItsRateSlabExists) {
                throw new DmException(19000, "Rate Slab wise GST is mandetory.");
            }
        }
        if (DmShared.iNull(mrTemp.getValue()).intValue() > 0) {
            if (DmShared.xNull(mrTemp.getServiceEvalTypeCode()).equalsIgnoreCase("")) {
                throw new DmException(20000, "Valuation Type is mandetory.");
            }
        }

    }

}
