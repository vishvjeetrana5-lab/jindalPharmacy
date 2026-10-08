package aarogya.inventory.workBillDirect.shared;

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
    private String itemTypeCode;

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

    //Properties
    private String batchNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String mrp;
    private String skuMrp;
    private String propertiesCsv;

    //Order Details
    private String poDocId;
    private String poRecId;
    private String poV_SNo;
    private String poRefNo;

    //Quantities
    private String challanQty;
    private String recQty;
    private String rejQty;
    private String skuDifferenceQty;
    private String skuQty;
    private String acceptQty;
    private String pendingQty;
    private String returnedQty;

    //Rate
    private String rate;
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
    private String ibCode;
    private String isStatus;
    private String grossAcCode;
    private String grossAcName;
    private List<OverHeadLine_Mdl> OHLList;
    private List<OverHeadLine_Mdl> OHLListPurchaseOrder;
    private List<OverHeadITS_Mdl> lstITS;
    private List<GrdImpurity_Mdl> lstImpurity = new ArrayList<GrdImpurity_Mdl>();
    private List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();

    public GrdGrnDetail_Mdl() {
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

    public String getItemTypeCode() {
        return itemTypeCode;
    }

    public void setItemTypeCode(String itemTypeCode) {
        this.itemTypeCode = itemTypeCode;
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

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public String getColourName() {
        return colourName;
    }

    public void setColourName(String colourName) {
        this.colourName = colourName;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getPropertiesCsv() {
        return propertiesCsv;
    }

    public void setPropertiesCsv(String propertiesCsv) {
        this.propertiesCsv = propertiesCsv;
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

    public String getReturnedQty() {
        return returnedQty;
    }

    public void setReturnedQty(String returnedQty) {
        this.returnedQty = returnedQty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public List<OverHeadITS_Mdl> getLstITS() {
        return lstITS;
    }

    public void setLstITS(List<OverHeadITS_Mdl> lstITS) {
        this.lstITS = lstITS;
    }

    public List<GrdImpurity_Mdl> getLstImpurity() {
        return lstImpurity;
    }

    public void setLstImpurity(List<GrdImpurity_Mdl> lstImpurity) {
        this.lstImpurity = lstImpurity;
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
        grdGrn_Mdl.setGstUnitCode(this.getGstUnitCode());

        //Properties
        grdGrn_Mdl.setBatchNo(this.getBatchNo());
        grdGrn_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdGrn_Mdl.setBestBeforeDays(this.getBestBeforeDays());
        grdGrn_Mdl.setExpiryDate(this.getExpiryDate());
        grdGrn_Mdl.setColourCode(this.getColourCode());
        grdGrn_Mdl.setColourName(this.getColourName());
        grdGrn_Mdl.setSizeCode(this.getSizeCode());
        grdGrn_Mdl.setSizeName(this.getSizeName());
        grdGrn_Mdl.setMrp(this.getMrp());
        grdGrn_Mdl.setSkuMrp(this.getSkuMrp());
        grdGrn_Mdl.setPropertiesCsv(this.getPropertiesCsv());

        //Order Details
        grdGrn_Mdl.setPoDocId(this.getPoDocId());
        grdGrn_Mdl.setPoRecId(this.getPoRecId());
        grdGrn_Mdl.setPoV_SNo(this.getPoV_SNo());
        grdGrn_Mdl.setPoRefNo(this.getPoRefNo());

        //Quantities
        grdGrn_Mdl.setChallanQty(this.getChallanQty());
        grdGrn_Mdl.setRecQty(this.getRecQty());
        grdGrn_Mdl.setRejQty(this.getRejQty());
        grdGrn_Mdl.setSkuDifferenceQty(this.getSkuDifferenceQty());
        grdGrn_Mdl.setSkuQty(this.getSkuQty());
        grdGrn_Mdl.setAcceptQty(this.getAcceptQty());
        grdGrn_Mdl.setPendingQty(this.getPendingQty());
        grdGrn_Mdl.setReturnedQty(this.getReturnedQty());

        //Rate
        grdGrn_Mdl.setRate(this.getRate());
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
        grdGrn_Mdl.setIbCode(this.getIbCode());
        grdGrn_Mdl.setIsStatus(this.getIsStatus());
        grdGrn_Mdl.setGrossAcCode(this.getGrossAcCode());
        grdGrn_Mdl.setGrossAcName(this.getGrossAcName());
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

}
