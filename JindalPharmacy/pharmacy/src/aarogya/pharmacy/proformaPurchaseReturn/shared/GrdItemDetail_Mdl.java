package aarogya.pharmacy.proformaPurchaseReturn.shared;

import dataman.dmToolkit.overHeadLine.shared.OHLExtended;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, OHLExtended {

    /*
    sharedKeyFieldCode = itemCode
    sharedKeyFieldName = itemName
     */
    //Item Details
    private String itemCompany;
    private String itemGSTNature;
    private String itsBasedOn;
    private String hsnCode;
    private String isBestBefore;
    private String propertyList;

    //Bill Details
    private String pbDocId;
    private String pbRecId;
    private String pbRefNo;
    private String pbRefDate;
    private String pbV_Type;
    private String pbV_Date;
    private String pbV_SNo;
    private String pbNetAmount;

    //Challan Details
    private String pcDocId;
    private String pcRecId;
    private String pcV_SNo;

    //Order Details
    private String poDocId;
    private String poRecId;
    private String poV_SNo;
    private String poRefNo;

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
    private String gstUnitCode;

    //Quantities
    private String pendingQty;
    private String pendingFreeQty;
    private String quantity;
    private String freeQty;
    private String skuQty;

    //Rates
    private String mrp;
    private String skuMrp;
    private String saleRate;
    private String skuSaleRate;
    private String rate;
    private String amount;
    private String landedSkuValue;
    private String landedSkuRate;
    private String pBillLandedSkuRate;
    private String staffSkuValue;
    private String skuRate;

    //Properties
    private String batchNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;

    //Other
    private String remark;
    private String v_SNo;
    private String ibCode;
    private String grossAcCode;
    private String grossAcName;

    private String inventoryAc;
    private String inventoryAcName;
    private String isProformaUsed;

    private List<OverHeadLine_Mdl> OHLList;
    private List<OverHeadLine_Mdl> OHLListPurchaseBill;
    private List<OverHeadITS_Mdl> lstITS;

    public GrdItemDetail_Mdl() {
    }

    public String getItemCompany() {
        return itemCompany;
    }

    public void setItemCompany(String itemCompany) {
        this.itemCompany = itemCompany;
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

    public String getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(String propertyList) {
        this.propertyList = propertyList;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
    }

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return this.OHLList;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> ohlList) {
        this.OHLList = ohlList;
    }

    public String getPbDocId() {
        return pbDocId;
    }

    public void setPbDocId(String pbDocId) {
        this.pbDocId = pbDocId;
    }

    public String getPbRecId() {
        return pbRecId;
    }

    public void setPbRecId(String pbRecId) {
        this.pbRecId = pbRecId;
    }

    public String getPbRefNo() {
        return pbRefNo;
    }

    public void setPbRefNo(String pbRefNo) {
        this.pbRefNo = pbRefNo;
    }

    public String getPbRefDate() {
        return pbRefDate;
    }

    public void setPbRefDate(String pbRefDate) {
        this.pbRefDate = pbRefDate;
    }

    public String getPbV_Type() {
        return pbV_Type;
    }

    public void setPbV_Type(String pbV_Type) {
        this.pbV_Type = pbV_Type;
    }

    public String getPbV_Date() {
        return pbV_Date;
    }

    public void setPbV_Date(String pbV_Date) {
        this.pbV_Date = pbV_Date;
    }

    public String getPbV_SNo() {
        return pbV_SNo;
    }

    public void setPbV_SNo(String pbV_SNo) {
        this.pbV_SNo = pbV_SNo;
    }

    public String getPbNetAmount() {
        return pbNetAmount;
    }

    public void setPbNetAmount(String pbNetAmount) {
        this.pbNetAmount = pbNetAmount;
    }

    public String getPcDocId() {
        return pcDocId;
    }

    public void setPcDocId(String pcDocId) {
        this.pcDocId = pcDocId;
    }

    public String getPcRecId() {
        return pcRecId;
    }

    public void setPcRecId(String pcRecId) {
        this.pcRecId = pcRecId;
    }

    public String getPcV_SNo() {
        return pcV_SNo;
    }

    public void setPcV_SNo(String pcV_SNo) {
        this.pcV_SNo = pcV_SNo;
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

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getPendingFreeQty() {
        return pendingFreeQty;
    }

    public void setPendingFreeQty(String pendingFreeQty) {
        this.pendingFreeQty = pendingFreeQty;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getLandedSkuRate() {
        return landedSkuRate;
    }

    public void setLandedSkuRate(String landedSkuRate) {
        this.landedSkuRate = landedSkuRate;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getGrossAcCode() {
        return grossAcCode;
    }

    public void setGrossAcCode(String grossAcCode) {
        this.grossAcCode = grossAcCode;
    }

    public String getGrossAcName() {
        return grossAcName;
    }

    public void setGrossAcName(String grossAcName) {
        this.grossAcName = grossAcName;
    }

    public List<OverHeadLine_Mdl> getOHLListPurchaseBill() {
        return OHLListPurchaseBill;
    }

    public void setOHLListPurchaseBill(List<OverHeadLine_Mdl> OHLListPurchaseBill) {
        this.OHLListPurchaseBill = OHLListPurchaseBill;
    }

    public GrdItemDetail_Mdl clone() {
        GrdItemDetail_Mdl grd_Mdl = new GrdItemDetail_Mdl();

        copy(grd_Mdl);

        return grd_Mdl;
    }

    public void copy(GrdItemDetail_Mdl grdItem_Mdl) {
        //Item
        grdItem_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdItem_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());

        //Item Details
        grdItem_Mdl.setItemGSTNature(this.getItemGSTNature());
        grdItem_Mdl.setItsBasedOn(this.getItsBasedOn());
        grdItem_Mdl.setHsnCode(this.getHsnCode());
        grdItem_Mdl.setIsBestBefore(this.getIsBestBefore());
        grdItem_Mdl.setPropertyList(this.getPropertyList());

        //Bill Details
        grdItem_Mdl.setPbDocId(this.getPbDocId());
        grdItem_Mdl.setPbRecId(this.getPbRecId());
        grdItem_Mdl.setPbRefNo(this.getPbRefNo());
        grdItem_Mdl.setPbRefDate(this.getPbRefDate());
        grdItem_Mdl.setPbV_Type(this.getPbV_Type());
        grdItem_Mdl.setPbV_Date(this.getPbV_Date());
        grdItem_Mdl.setPbV_SNo(this.getPbV_SNo());
        grdItem_Mdl.setPbNetAmount(this.getPbNetAmount());

        //Challan No
        grdItem_Mdl.setPcDocId(this.getPcDocId());
        grdItem_Mdl.setPcRecId(this.getPcRecId());
        grdItem_Mdl.setPcV_SNo(this.getPcV_SNo());

        //Order No
        grdItem_Mdl.setPoDocId(this.getPoDocId());
        grdItem_Mdl.setPoRecId(this.getPoRecId());
        grdItem_Mdl.setPoV_SNo(this.getPoV_SNo());
        grdItem_Mdl.setPoRefNo(this.getPoRefNo());

        //SKU
        grdItem_Mdl.setSkuCode(this.getSkuCode());
        grdItem_Mdl.setSkuName(this.getSkuName());
        grdItem_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());

        //Purchase Unit
        grdItem_Mdl.setPurchaseUnitCode(this.getPurchaseUnitCode());
        grdItem_Mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        grdItem_Mdl.setPurchaseUnitDisplayName(this.getPurchaseUnitDisplayName());
        grdItem_Mdl.setPurchaseUnitDecimalPlaces(this.getPurchaseUnitDecimalPlaces());
        grdItem_Mdl.setPurchaseUnitConversionRate(this.getPurchaseUnitConversionRate());
        grdItem_Mdl.setGstUnitCode(this.getGstUnitCode());

        //Quantity
        grdItem_Mdl.setPendingQty(this.getPendingQty());
        grdItem_Mdl.setPendingFreeQty(this.getPendingFreeQty());
        grdItem_Mdl.setQuantity(this.getQuantity());
        grdItem_Mdl.setFreeQty(this.getFreeQty());
        grdItem_Mdl.setSkuQty(this.getSkuQty());

        //Rate
        grdItem_Mdl.setMrp(this.getMrp());
        grdItem_Mdl.setSkuMrp(this.getSkuMrp());
        grdItem_Mdl.setSaleRate(this.getSaleRate());
        grdItem_Mdl.setSkuSaleRate(this.getSkuSaleRate());
        grdItem_Mdl.setRate(this.getRate());
        grdItem_Mdl.setAmount(this.getAmount());
        grdItem_Mdl.setLandedSkuRate(this.getLandedSkuRate());
        grdItem_Mdl.setLandedSkuValue(this.getLandedSkuValue());
        grdItem_Mdl.setSkuRate(this.getSkuRate());

        //Properties
        grdItem_Mdl.setBatchNo(this.getBatchNo());
        grdItem_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdItem_Mdl.setBestBeforeDays(this.getBestBeforeDays());
        grdItem_Mdl.setExpiryDate(this.getExpiryDate());

        //Other
        grdItem_Mdl.setRemark(this.getRemark());
        grdItem_Mdl.setV_SNo(this.getV_SNo());
        grdItem_Mdl.setGrossAcCode(this.getGrossAcCode());
        grdItem_Mdl.setGrossAcName(this.getGrossAcName());
        grdItem_Mdl.setIsProformaUsed(this.getIsProformaUsed());
        

        grdItem_Mdl.setIbCode(this.getIbCode());
        grdItem_Mdl.setOHLList(OverHeadLine_Mdl.getOHLListClone(this.getOHLList()));
        grdItem_Mdl.setOHLListPurchaseBill(OverHeadLine_Mdl.getOHLListClone(this.getOHLListPurchaseBill()));
        grdItem_Mdl.setLstITS(OverHeadITS_Mdl.getITSListClone(this.getLstITS()));
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

    public String getIsProformaUsed() {
        return isProformaUsed;
    }

    public void setIsProformaUsed(String isProformaUsed) {
        this.isProformaUsed = isProformaUsed;
    }

    public String getStaffSkuValue() {
        return staffSkuValue;
    }

    public void setStaffSkuValue(String staffSkuValue) {
        this.staffSkuValue = staffSkuValue;
    }

    public String getpBillLandedSkuRate() {
        return pBillLandedSkuRate;
    }

    public void setpBillLandedSkuRate(String pBillLandedSkuRate) {
        this.pBillLandedSkuRate = pBillLandedSkuRate;
    }

}
