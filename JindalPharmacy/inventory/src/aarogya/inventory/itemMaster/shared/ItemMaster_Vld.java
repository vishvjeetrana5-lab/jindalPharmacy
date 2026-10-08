package aarogya.inventory.itemMaster.shared;

import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class ItemMaster_Vld {

    public static void chkSave(ItemMaster_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Item Name", 100);
        DmShared.isValidText(mrTemp.getItemTypeTag(), "Item Type", 200);
        DmShared.isValidText(mrTemp.getMedGroupTag(), "Item Group", 300);
        DmShared.isValidText(mrTemp.getCategoryTag(), "Item Category", 400);
        DmShared.isValidText(mrTemp.getSkuTag(), "SKU", 500);
        DmShared.isValidText(mrTemp.getConsignmentTag(), "Consignment Item", 600);
        DmShared.isValidText(mrTemp.getBestBeforeTag(), "Best Before", 700);
        DmShared.isValidText(mrTemp.getFlowThroughTag(), "Flow Through", 1000);
        DmShared.isValidText(mrTemp.getHighValueTag(), "High Value", 2000);
        DmShared.isValidText(mrTemp.getReturnableItemTag(), "Returnable Item", 3000);
        DmShared.isValidText(mrTemp.getBilledItemTag(), "   Billed Item", 4000);
        DmShared.isValidText(mrTemp.getPurchaseUnitSkuCode(), "Purchase Unit", 7000);
        DmShared.isValidText(mrTemp.getSaleUnitSkuCode(), "Sale Unit", 8000);
        DmShared.isValidText(mrTemp.getConversionRatePurchase(), "Purchase Conversion Rate", 8500);
        DmShared.isValidText(mrTemp.getConversionRateSale(), "Sale Conversion Rate", 9500);

        DmShared.isValidText(mrTemp.getPoReqTag(), "PO Required", 16000);
        DmShared.isValidText(mrTemp.getLabRequiredTag(), "Lab Required", 16500);
        DmShared.isValidText(mrTemp.getIndentReqTag(), "Indent Required", 17000);
        DmShared.isValidText(mrTemp.getQuotationReqTag(), "Quotation Required", 18000);

        if (DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getSaleUnitSkuCode()))) {
            if (DmShared.vNull(mrTemp.getConversionRatePurchase()).doubleValue() != DmShared.vNull(mrTemp.getConversionRateSale()).doubleValue()) {
                throw new DmException(9000, "Purchase conversion rate and sale conversion rate should be same.");
            }
        }
        if (DmShared.iNull(mrTemp.getSkuTag()).longValue() == DmShared.iNull(mrTemp.getSaleUnitSkuCode()).longValue()) {
            if (!DmShared.xNull(mrTemp.getConversionRateSale()).equalsIgnoreCase("1")) {
                throw new DmException(10000, "Sale conversion rate should be 1.");
            }
        }
        if (DmShared.iNull(mrTemp.getSkuTag()).longValue() == DmShared.iNull(mrTemp.getPurchaseUnitSkuCode()).longValue()) {
            if (!DmShared.xNull(mrTemp.getConversionRatePurchase()).equalsIgnoreCase(DmShared.xNull("1"))) {
                throw new DmException(11000, "Purchase conversion rate should be 1.");
            }
        }
        if (DmShared.iNull(mrTemp.getConversionRatePurchase()).longValue() <= 0) {
            throw new DmException(12000, "Purchase conversion rate should be greater than 0.");
        }
        if (DmShared.iNull(mrTemp.getConversionRateSale()).longValue() <= 0) {
            throw new DmException(13000, "Sale conversion rate should be greater than 0.");
        }

        if (DmShared.xNull(mrTemp.getAllowNegetiveStockCode()).equalsIgnoreCase("")) {
            throw new DmException(15000, "Negative Stock is mandatory.");
        }

        boolean isItsRateSlabExists = false;
        for (GrdItsRateSlab_Mdl grdRateITS_Mdl : mrTemp.getGrdRateIts_Mdls()) {
            if (!DmShared.xNull(grdRateITS_Mdl.getGstTypeCode()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getItsBasedOnTag()).equalsIgnoreCase("")) {
                    throw new DmException(19000, "GST Based On is mandetory");
                }

                if (DmShared.xNull(mrTemp.getItsBasedOnTag()).equalsIgnoreCase("1")) {
                    if (!isItsRateSlabExists) {
                        isItsRateSlabExists = true;
                        break;
                    }
                }
            }
        }

        if (DmShared.xNull(mrTemp.getItsBasedOnTag()).equalsIgnoreCase("1")) {
            if (!isItsRateSlabExists) {
                throw new DmException(19000, "Rate Slab wise GST is mandetory.");
            }
        }

    }

}
