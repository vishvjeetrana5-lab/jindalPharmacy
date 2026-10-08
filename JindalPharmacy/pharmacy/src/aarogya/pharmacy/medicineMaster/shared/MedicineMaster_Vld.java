package aarogya.pharmacy.medicineMaster.shared;

import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.util.Objects;

public class MedicineMaster_Vld {

    public static void chkSave(MedicineMaster_Mdl mrTemp) {

        DmShared.isValidText(mrTemp.getDisplayName(), "Display Name", 16000);
        DmShared.isValidText(mrTemp.getCategoryTag(), "Medicine Category", 200);
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Medicine Name", 100);
        DmShared.isValidText(mrTemp.getCompanyTag(), "Medicine Company", 300);
        DmShared.isValidText(mrTemp.getMedGroupTag(), "Medicine Group", 500);
        DmShared.isValidText(mrTemp.getIsCommonItemTag(), "Common", 710);
        DmShared.isValidText(mrTemp.getIsDiscountAllowTag(), "Sale Discount", 720);

        DmShared.isValidText(mrTemp.getConsignmentTag(), "Consignment Item", 600);
        DmShared.isValidText(mrTemp.getBestBeforeTag(), "Best Before", 700);
        DmShared.isValidText(mrTemp.getLifeSavingTag(), "Life Saving", 800);
        DmShared.isValidText(mrTemp.getNarcoticsTag(), "Narcotics & control Substances", 900);
        DmShared.isValidText(mrTemp.getFlowThroughTag(), "Flow Through", 1000);
        DmShared.isValidText(mrTemp.getHighValueTag(), "High Value", 2000);
        DmShared.isValidText(mrTemp.getReturnableItemTag(), "Returnable Item", 3000);
        DmShared.isValidText(mrTemp.getBilledItemTag(), "   Billed Item", 4000);
        DmShared.isValidText(mrTemp.getPackSize(), "Pack Size", 17000);

        DmShared.isValidText(mrTemp.getPoReqTag(), "PO Required", 17800);
        DmShared.isValidText(mrTemp.getIndentReqTag(), "Indent Required", 17900);
//        DmShared.isValidText(mrTemp.getAllowMRPDiscTag(), "Allow MRP Discount", 17910);
        DmShared.isValidText(mrTemp.getMarginRateTag(), "Rate / margin", 17920);
        DmShared.isValidText(mrTemp.getQuotationReqTag(), "Quotation Required", 18000);

        if (DmShared.vNull(mrTemp.getTaxPerc()).toString().equalsIgnoreCase("")) {
            throw new DmException(10000, "Tax % is Mandatory.");
        }

        DmShared.isValidText(mrTemp.getStatusTag(), "Active", 5000);
        DmShared.isValidText(mrTemp.getSkuTag(), "SKU", 6000);
        DmShared.isValidText(mrTemp.getPurchaseUnitSkuCode(), "Purchase Unit", 7000);
        DmShared.isValidText(mrTemp.getSaleUnitSkuCode(), "Sale Unit", 8000);
        DmShared.isValidText(mrTemp.getConversionRatePurchase(), "Purchase Conversion Rate", 8500);
        DmShared.isValidText(mrTemp.getConversionRateSale(), "Sale Conversion Rate", 9500);

        DmShared.isValidText(mrTemp.getHsnCode(), "HSN", 9501);
        DmShared.isValidText(mrTemp.getIts_Code_Purchase(), "Purchase Its", 9502);
        DmShared.isValidText(mrTemp.getIts_Code_Sales(), "Sale Its", 9503);

        /////////////////// Tax Percent Validation ////////////////////////////////////
        if (!DmShared.vNull(mrTemp.getIts_Tax_Sales()).toString().equalsIgnoreCase(DmShared.vNull(mrTemp.getIts_Tax_Purchase()).toString())) {
            throw new DmException(9000, "ITS tax% should be equal.");
        } else {
            if (!DmShared.vNull(mrTemp.getIts_Tax_Sales()).toString().equalsIgnoreCase(DmShared.vNull(mrTemp.getTaxPerc()).toString())) {
                throw new DmException(9000, "ITS tax% should be equal.");
            }
        }
//        if (!Objects.equals(DmShared.vNull(mrTemp.getIts_Tax_Sales()), DmShared.vNull(mrTemp.getIts_Tax_Purchase()))) {
//            throw new DmException(9000, "ITS tax% should be equal.");
//        } else {
//            if (!Objects.equals(DmShared.vNull(mrTemp.getIts_Tax_Sales()), DmShared.vNull(mrTemp.getTaxPerc()))) {
//                throw new DmException(9000, "ITS tax% should be equal.");
//            }
//        }

        if (DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getSaleUnitSkuCode()))) {
            if (!DmShared.xNull(mrTemp.getConversionRatePurchase()).equalsIgnoreCase(DmShared.xNull(mrTemp.getConversionRateSale()))) {
                throw new DmException(9000, "Purchase conversion rate and sale conversion rate should be same.");
            }
        }
        if (DmShared.vNull(mrTemp.getSkuTag()).doubleValue() == DmShared.vNull(mrTemp.getSaleUnitSkuCode()).doubleValue()) {
            if (!DmShared.xNull(mrTemp.getConversionRateSale()).equalsIgnoreCase("1")) {
                throw new DmException(10000, "Sale conversion rate should be 1.");
            }
        }
        if (DmShared.vNull(mrTemp.getSkuTag()).doubleValue() == DmShared.vNull(mrTemp.getPurchaseUnitSkuCode()).doubleValue()) {
            if (!DmShared.xNull(mrTemp.getConversionRatePurchase()).equalsIgnoreCase(DmShared.xNull("1"))) {
                throw new DmException(11000, "Purchase conversion rate should be 1.");
            }
        }
        if (DmShared.vNull(mrTemp.getConversionRatePurchase()).doubleValue() <= 0) {
            throw new DmException(12000, "Purchase conversion rate should be greater than 0.");
        }
        if (DmShared.vNull(mrTemp.getConversionRateSale()).doubleValue() <= 0) {
            throw new DmException(13000, "Sale conversion rate should be greater than 0.");
        }
        if (DmShared.xNull(mrTemp.getAllowNegetiveStockCode()).equalsIgnoreCase("")) {
            throw new DmException(15000, "Negative Stock is mandatory.");
        }
        if (DmShared.vNull(mrTemp.getSaleRate()).doubleValue() > DmShared.vNull(mrTemp.getMrp()).doubleValue()) {
            throw new DmException(17200, "Sale Rate cannot be greater than MRP.");
        }
        if (DmShared.vNull(mrTemp.getPurchaseRate()).doubleValue() > DmShared.vNull(mrTemp.getMrp()).doubleValue()) {
            throw new DmException(17300, "Purchase Rate cannot be greater than MRP.");
        }
        if (DmShared.vNull(mrTemp.getSchemeFreeQty()).doubleValue() > 0) {
            if (DmShared.vNull(mrTemp.getSchemeOnQty()).doubleValue() == 0) {
                throw new DmException(17400, "Scheme On Quantity is mandatory.");
            }
        }
        if (DmShared.vNull(mrTemp.getSchemeOnQty()).doubleValue() > 0) {
            if (DmShared.vNull(mrTemp.getSchemeFreeQty()).doubleValue() == 0) {
                throw new DmException(17500, "Scheme Free Quantity is mandatory.");
            }
        }
        if (DmShared.vNull(mrTemp.getPurchaseDisPer()).doubleValue() > 100) {
            throw new DmException(17600, "Purchase Discount Percentage Cannot be greater than 100.");
        }
        if (DmShared.vNull(mrTemp.getSaleDisPer()).doubleValue() > 100) {
            throw new DmException(17700, "Sale Discount Percentage Cannot be greater than 100.");
        }

        int baseCount = 0;
        boolean isCompositionExists = false;
        for (GrdComposition_Mdl grdComposition : mrTemp.getGrdComposition_Mdl()) {
            if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() > 0) {
                isCompositionExists = true;
                if (DmShared.iNull(grdComposition.getBaseTag()).longValue() == 1) {
                    ++baseCount;
                }
                if (DmShared.vNull(grdComposition.getPower()).doubleValue() > 0) {
                    if (DmShared.iNull(grdComposition.getUnitTag()).longValue() == 0) {
                        throw new DmException(17100, "Unit is mandatory");
                    }
                }

            }
        }
        if (baseCount == 0 && isCompositionExists) {
            throw new DmException(17100, "Base composition is mandatory");
        } else if (baseCount > 1) {
            throw new DmException(17100, "Only one base composition can be defined as 'Yes'.");
        }

    }

}
