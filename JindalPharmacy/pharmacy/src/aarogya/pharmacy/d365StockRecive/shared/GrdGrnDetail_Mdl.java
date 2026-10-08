package aarogya.pharmacy.d365StockRecive.shared;

import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdGrnDetail_Mdl extends KeyField implements Serializable, OHLExtended {

    /*
    sharedKeyFieldCode = itemCode
    sharedKeyFieldName = itemName
     */
    //Item Details
    private String propertyList;
    private String itemGSTNature;
    private String itsBasedOn;
    private String hsnCode;
    private String isBestBefore;
    private String masterMrp;
    private String masterSaleRate;
    private String masterPurchaseRate;
    private String masterSchemeOnQty;
    private String masterSchemeFreeQty;
    private String masterPurchaseDiscountPer;
    private String isIncludeFreeQtyInSaleRate;

    //SKU
    private String skuCode;
    private String skuName;
    private String skuDecimalPlaces;

    //Purchase Unit
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDisplayName;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitConversionRate;

    //Properties
    private String batchNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;

    //Order Details
    private String poDocId;
    private String poRecId;
    private String poV_SNo;
    private String poRefNo;
    private String podMrp;

    //Quantities
    private String challanQty;
    private String recQty;
    private String rejQty;
    private String freeQty;
    private String skuDifferenceQty;
    private String skuQty;
    private String acceptQty;
    private String pendingQty;
    private String billedQty;

    //Rate
    private String mrp;
    private String skuMrp;
    private String rate;
    private String saleRate;
    private String skuSaleRate;
    private String staffSaleRate;
    private String skuStaffSaleRate;
    private String landedSkuRate;
    private String landedSkuValue;
    private String skuRate;
    private String amount;

    //Other
    private String remark;
    private String vSNo;
    private String refNo;
    private String refDate;
    private String supplierName;
    private String discountPer;
    private String discountAmount;
    private String profitPer;
    private String ibCode;
    private String isStatus;
    private String barcode;
    private String isBarcodeActive;
    private String inventoryAc;
    private String inventoryAcName;
    private String saleRateProffitMargine;
    private boolean poMrpExist;

    private List<OverHeadLine_Mdl> OHLList;
    private List<OverHeadLine_Mdl> OHLListPurchaseOrder;
    private List<OverHeadITS_Mdl> lstITS;
    private List<GrdImpurity_Mdl> lstImpurity = new ArrayList<GrdImpurity_Mdl>();
    private List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();

    public GrdGrnDetail_Mdl() {
    }

    public String getPodMrp() {
        return podMrp;
    }

    public void setPodMrp(String podMrp) {
        this.podMrp = podMrp;
    }

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getIsBestBefore() {
        return isBestBefore;
    }

    public void setIsBestBefore(String isBestBefore) {
        this.isBestBefore = isBestBefore;
    }

    public String getMasterMrp() {
        return masterMrp;
    }

    public void setMasterMrp(String masterMrp) {
        this.masterMrp = masterMrp;
    }

    public String getMasterSaleRate() {
        return masterSaleRate;
    }

    public void setMasterSaleRate(String masterSaleRate) {
        this.masterSaleRate = masterSaleRate;
    }

    public String getStaffSaleRate() {
        return staffSaleRate;
    }

    public void setStaffSaleRate(String staffSaleRate) {
        this.staffSaleRate = staffSaleRate;
    }

    public String getSkuStaffSaleRate() {
        return skuStaffSaleRate;
    }

    public void setSkuStaffSaleRate(String skuStaffSaleRate) {
        this.skuStaffSaleRate = skuStaffSaleRate;
    }

    public String getMasterPurchaseRate() {
        return masterPurchaseRate;
    }

    public void setMasterPurchaseRate(String masterPurchaseRate) {
        this.masterPurchaseRate = masterPurchaseRate;
    }

    public String getMasterSchemeOnQty() {
        return masterSchemeOnQty;
    }

    public void setMasterSchemeOnQty(String masterSchemeOnQty) {
        this.masterSchemeOnQty = masterSchemeOnQty;
    }

    public String getMasterSchemeFreeQty() {
        return masterSchemeFreeQty;
    }

    public void setMasterSchemeFreeQty(String masterSchemeFreeQty) {
        this.masterSchemeFreeQty = masterSchemeFreeQty;
    }

    public String getMasterPurchaseDiscountPer() {
        return masterPurchaseDiscountPer;
    }

    public void setMasterPurchaseDiscountPer(String masterPurchaseDiscountPer) {
        this.masterPurchaseDiscountPer = masterPurchaseDiscountPer;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getIsIncludeFreeQtyInSaleRate() {
        return isIncludeFreeQtyInSaleRate;
    }

    public void setIsIncludeFreeQtyInSaleRate(String isIncludeFreeQtyInSaleRate) {
        this.isIncludeFreeQtyInSaleRate = isIncludeFreeQtyInSaleRate;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getPurchaseUnitCode() {
        return purchaseUnitCode;
    }

    public void setPurchaseUnitCode(String purchaseUnitCode) {
        this.purchaseUnitCode = purchaseUnitCode;
    }

    public String getPurchaseUnitName() {
        return purchaseUnitName;
    }

    public void setPurchaseUnitName(String purchaseUnitName) {
        this.purchaseUnitName = purchaseUnitName;
    }

    public String getPurchaseUnitDisplayName() {
        return purchaseUnitDisplayName;
    }

    public void setPurchaseUnitDisplayName(String purchaseUnitDisplayName) {
        this.purchaseUnitDisplayName = purchaseUnitDisplayName;
    }

    public String getPurchaseUnitDecimalPlaces() {
        return purchaseUnitDecimalPlaces;
    }

    public void setPurchaseUnitDecimalPlaces(String purchaseUnitDecimalPlaces) {
        this.purchaseUnitDecimalPlaces = purchaseUnitDecimalPlaces;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getBestBeforeDays() {
        return bestBeforeDays;
    }

    public void setBestBeforeDays(String bestBeforeDays) {
        this.bestBeforeDays = bestBeforeDays;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPoDocId() {
        return poDocId;
    }

    public void setPoDocId(String poDocId) {
        this.poDocId = poDocId;
    }

    public String getPoRecId() {
        return poRecId;
    }

    public void setPoRecId(String poRecId) {
        this.poRecId = poRecId;
    }

    public String getPoV_SNo() {
        return poV_SNo;
    }

    public void setPoV_SNo(String poV_SNo) {
        this.poV_SNo = poV_SNo;
    }

    public String getPoRefNo() {
        return poRefNo;
    }

    public void setPoRefNo(String poRefNo) {
        this.poRefNo = poRefNo;
    }

    public String getChallanQty() {
        return challanQty;
    }

    public void setChallanQty(String challanQty) {
        this.challanQty = challanQty;
    }

    public String getRecQty() {
        return recQty;
    }

    public void setRecQty(String recQty) {
        this.recQty = recQty;
    }

    public String getRejQty() {
        return rejQty;
    }

    public void setRejQty(String rejQty) {
        this.rejQty = rejQty;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getSkuDifferenceQty() {
        return skuDifferenceQty;
    }

    public void setSkuDifferenceQty(String skuDifferenceQty) {
        this.skuDifferenceQty = skuDifferenceQty;
    }

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
    }

    public String getAcceptQty() {
        return acceptQty;
    }

    public void setAcceptQty(String acceptQty) {
        this.acceptQty = acceptQty;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getBilledQty() {
        return billedQty;
    }

    public void setBilledQty(String billedQty) {
        this.billedQty = billedQty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

    public String getSkuSaleRate() {
        return skuSaleRate;
    }

    public void setSkuSaleRate(String skuSaleRate) {
        this.skuSaleRate = skuSaleRate;
    }

    public String getLandedSkuRate() {
        return landedSkuRate;
    }

    public void setLandedSkuRate(String landedSkuRate) {
        this.landedSkuRate = landedSkuRate;
    }

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getProfitPer() {
        return profitPer;
    }

    public void setProfitPer(String profitPer) {
        this.profitPer = profitPer;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getIsBarcodeActive() {
        return isBarcodeActive;
    }

    public void setIsBarcodeActive(String isBarcodeActive) {
        this.isBarcodeActive = isBarcodeActive;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    public List<GrdImpurity_Mdl> getLstImpurity() {
        return lstImpurity;
    }

    public List<ItemUpdateField_Mdl> getLstItemUpdateFields() {
        return lstItemUpdateFields;
    }

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return this.OHLList;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> ohlList) {
        this.OHLList = ohlList;
    }

    public List<OverHeadLine_Mdl> getOHLListPurchaseOrder() {
        return OHLListPurchaseOrder;
    }

    public void setOHLListPurchaseOrder(List<OverHeadLine_Mdl> OHLListPurchaseOrder) {
        this.OHLListPurchaseOrder = OHLListPurchaseOrder;
    }

    public GrdGrnDetail_Mdl clone() {
        GrdGrnDetail_Mdl grd_Mdl = new GrdGrnDetail_Mdl();

        copy(grd_Mdl);

        return grd_Mdl;
    }

    public void copy(GrdGrnDetail_Mdl grdGrn_Mdl) {

        //Item
        grdGrn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdGrn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());

        //Item Details
        grdGrn_Mdl.setPropertyList(this.getPropertyList());
        grdGrn_Mdl.setItemGSTNature(this.getItemGSTNature());
        grdGrn_Mdl.setItsBasedOn(this.getItsBasedOn());
        grdGrn_Mdl.setHsnCode(this.getHsnCode());
        grdGrn_Mdl.setIsBestBefore(this.getIsBestBefore());
        grdGrn_Mdl.setIsBarcodeActive(this.getIsBarcodeActive());
        grdGrn_Mdl.setBarcode(this.getBarcode());
        grdGrn_Mdl.setMasterMrp(this.getMasterMrp());
        grdGrn_Mdl.setMasterSaleRate(this.getMasterSaleRate());
        grdGrn_Mdl.setMasterPurchaseRate(this.getMasterPurchaseRate());
        grdGrn_Mdl.setMasterSchemeOnQty(this.getMasterSchemeOnQty());
        grdGrn_Mdl.setMasterSchemeFreeQty(this.getMasterSchemeFreeQty());
        grdGrn_Mdl.setMasterPurchaseDiscountPer(this.getMasterPurchaseDiscountPer());
        grdGrn_Mdl.setIsIncludeFreeQtyInSaleRate(this.getIsIncludeFreeQtyInSaleRate());

        //SKU
        grdGrn_Mdl.setSkuCode(this.getSkuCode());
        grdGrn_Mdl.setSkuName(this.getSkuName());
        grdGrn_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());

        //Purchase Unit
        grdGrn_Mdl.setPurchaseUnitCode(this.getPurchaseUnitCode());
        grdGrn_Mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        grdGrn_Mdl.setPurchaseUnitDisplayName(this.getPurchaseUnitDisplayName());
        grdGrn_Mdl.setPurchaseUnitDecimalPlaces(this.getPurchaseUnitDecimalPlaces());
        grdGrn_Mdl.setPurchaseUnitConversionRate(this.getPurchaseUnitConversionRate());

        //Properties
        grdGrn_Mdl.setBatchNo(this.getBatchNo());
        grdGrn_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdGrn_Mdl.setBestBeforeDays(this.getBestBeforeDays());
        grdGrn_Mdl.setExpiryDate(this.getExpiryDate());

        //Order Details
        grdGrn_Mdl.setPoDocId(this.getPoDocId());
        grdGrn_Mdl.setPoRecId(this.getPoRecId());
        grdGrn_Mdl.setPoV_SNo(this.getPoV_SNo());
        grdGrn_Mdl.setPoRefNo(this.getPoRefNo());

        //Quantities
        grdGrn_Mdl.setChallanQty(this.getChallanQty());
        grdGrn_Mdl.setRecQty(this.getRecQty());
        grdGrn_Mdl.setRejQty(this.getRejQty());
        grdGrn_Mdl.setFreeQty(this.getFreeQty());
        grdGrn_Mdl.setSkuDifferenceQty(this.getSkuDifferenceQty());
        grdGrn_Mdl.setSkuQty(this.getSkuQty());
        grdGrn_Mdl.setAcceptQty(this.getAcceptQty());
        grdGrn_Mdl.setPendingQty(this.getPendingQty());
        grdGrn_Mdl.setBilledQty(this.getBilledQty());

        //Rate
        grdGrn_Mdl.setMrp(this.getMrp());
        grdGrn_Mdl.setSkuMrp(this.getSkuMrp());
        grdGrn_Mdl.setRate(this.getRate());
        grdGrn_Mdl.setSaleRate(this.getSaleRate());
        grdGrn_Mdl.setSkuSaleRate(this.getSkuSaleRate());
        grdGrn_Mdl.setStaffSaleRate(this.getStaffSaleRate());
        grdGrn_Mdl.setSkuStaffSaleRate(this.getSkuStaffSaleRate());
        grdGrn_Mdl.setLandedSkuRate(this.getLandedSkuRate());
        grdGrn_Mdl.setLandedSkuValue(this.getLandedSkuValue());
        grdGrn_Mdl.setSkuRate(this.getSkuRate());
        grdGrn_Mdl.setAmount(this.getAmount());

        //Other
        grdGrn_Mdl.setRemark(this.getRemark());
        grdGrn_Mdl.setvSNo(this.getvSNo());
        grdGrn_Mdl.setRefNo(this.getRefNo());
        grdGrn_Mdl.setRefDate(this.getRefDate());
        grdGrn_Mdl.setSupplierName(this.getSupplierName());
        grdGrn_Mdl.setDiscountPer(this.getDiscountPer());
        grdGrn_Mdl.setProfitPer(this.getProfitPer());
        grdGrn_Mdl.setIbCode(this.getIbCode());
        grdGrn_Mdl.setIsStatus(this.getIsStatus());
        grdGrn_Mdl.setOHLList(OverHeadLine_Mdl.getOHLListClone(this.getOHLList()));
        grdGrn_Mdl.setOHLListPurchaseOrder(OverHeadLine_Mdl.getOHLListClone(this.getOHLListPurchaseOrder()));
        grdGrn_Mdl.setLstITS(OverHeadITS_Mdl.getITSListClone(this.getLstITS()));
        grdGrn_Mdl.getLstImpurity().addAll(cloneImpurityList(this.getLstImpurity()));
        grdGrn_Mdl.getLstItemUpdateFields().addAll(cloneItemUpdateFieldList(this.getLstItemUpdateFields()));

    }

    public List<GrdImpurity_Mdl> cloneImpurityList(List<GrdImpurity_Mdl> list) {
        List<GrdImpurity_Mdl> grdImpurity_MdlList = null;

        if (list != null) {
            grdImpurity_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdImpurity_MdlList.add(list.get(i).clone());
            }
        }

        return grdImpurity_MdlList;
    }

    public List<ItemUpdateField_Mdl> cloneItemUpdateFieldList(List<ItemUpdateField_Mdl> list) {
        List<ItemUpdateField_Mdl> grdList = null;

        if (list != null) {
            grdList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdList.add(list.get(i).clone());
            }
        }

        return grdList;
    }

    public String getInventoryAc() {
        return inventoryAc;
    }

    public void setInventoryAc(String inventoryAc) {
        this.inventoryAc = inventoryAc;
    }

    public String getInventoryAcName() {
        return inventoryAcName;
    }

    public void setInventoryAcName(String inventoryAcName) {
        this.inventoryAcName = inventoryAcName;
    }

    public String getSaleRateProffitMargine() {
        return saleRateProffitMargine;
    }

    public void setSaleRateProffitMargine(String saleRateProffitMargine) {
        this.saleRateProffitMargine = saleRateProffitMargine;
    }

    public boolean isPoMrpExist() {
        return poMrpExist;
    }

    public void setPoMrpExist(boolean poMrpExist) {
        this.poMrpExist = poMrpExist;
    }

}
