package aarogya.inventory.purchaseReturn.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable {

    /*
    sharedKeyFieldCode = itemCode
    sharedKeyFieldName = itemName
     */
    //Servcie
    private boolean serviceRecord;
    private String smfCode;
    private String serviceEvalTypeCode;
    private String serviceEvalTypeName;
    private String serviceEvalRate;

    //Department
    private String departmentCode;
    private String departmentName;

    //Item Details
    private String itemGSTNature;
    private String itsBasedOn;
    private String isBestBefore;
    private String propertyList;
    private String hsnCode;
    private boolean manualTax;
    private boolean manualGstType;
    private String gstTypeCode;
    private String gstTypeName;
    private String hsn;

    //Bill Details
    private String pbDocId;
    private String pbRecId;
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
    private String quantity;
    private String pendingQty;
    private String skuQty;

    //Rates
    private String rate;
    private String amount;
    private String landedSkuValue;
    private String landedSkuRate;
    private String skuRate;
    private String discountPer;
    private String discount;
    private String taxableAmt;
    private String sgstUgstPer;
    private String sgstUgst;
    private String sgstPer;
    private String sgst;
    private String ugstPer;
    private String ugst;
    private String cgstPer;
    private String cgst;
    private String igstPer;
    private String igst;
    private String subTotal;
    private String finalDiscountPer;
    private String finalDiscount;
    private String itemValue;

    //Properties
    private String mrp;
    private String skuMrp;
    private String batchNo;
    private String lotNo;
    private String ibCode;
    private String expiryDate;
    private String manufacturingDate;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String itemMakeCode;
    private String itemMakeName;
    private String propertiesCsv;

    //IAP
    private String isubCode;

    //Accounts
    private String sgstAcCode;  
    private String ugstAcCode;  
    private String cgstAcCode;  
    private String igstAcCode;  
    private String sgstRcmAcCode;  
    private String ugstRcmAcCode;  
    private String cgstRcmAcCode;  
    private String igstRcmAcCode;  
    
    //Other
    private String remark;
    private String v_SNo;
    private String itemMastPurchaseReturnAc;

    public GrdItemDetail_Mdl() {
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getItsBasedOn() {
        return itsBasedOn;
    }

    public void setItsBasedOn(String itsBasedOn) {
        this.itsBasedOn = itsBasedOn;
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

    public boolean isServiceRecord() {
        return serviceRecord;
    }

    public void setServiceRecord(boolean serviceRecord) {
        this.serviceRecord = serviceRecord;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getItemGSTNature() {
        return itemGSTNature;
    }

    public void setItemGSTNature(String itemGSTNature) {
        this.itemGSTNature = itemGSTNature;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
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

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public String getItemMakeCode() {
        return itemMakeCode;
    }

    public void setItemMakeCode(String itemMakeCode) {
        this.itemMakeCode = itemMakeCode;
    }

    public String getItemMakeName() {
        return itemMakeName;
    }

    public void setItemMakeName(String itemMakeName) {
        this.itemMakeName = itemMakeName;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getItemMastPurchaseReturnAc() {
        return itemMastPurchaseReturnAc;
    }

    public void setItemMastPurchaseReturnAc(String itemMastPurchaseReturnAc) {
        this.itemMastPurchaseReturnAc = itemMastPurchaseReturnAc;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getSgstUgstPer() {
        return sgstUgstPer;
    }

    public void setSgstUgstPer(String sgstUgstPer) {
        this.sgstUgstPer = sgstUgstPer;
    }

    public String getSgstUgst() {
        return sgstUgst;
    }

    public void setSgstUgst(String sgstUgst) {
        this.sgstUgst = sgstUgst;
    }

    public String getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(String sgstPer) {
        this.sgstPer = sgstPer;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getUgstPer() {
        return ugstPer;
    }

    public void setUgstPer(String ugstPer) {
        this.ugstPer = ugstPer;
    }

    public String getUgst() {
        return ugst;
    }

    public void setUgst(String ugst) {
        this.ugst = ugst;
    }

    public String getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(String cgstPer) {
        this.cgstPer = cgstPer;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getIgstPer() {
        return igstPer;
    }

    public void setIgstPer(String igstPer) {
        this.igstPer = igstPer;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getFinalDiscountPer() {
        return finalDiscountPer;
    }

    public void setFinalDiscountPer(String finalDiscountPer) {
        this.finalDiscountPer = finalDiscountPer;
    }

    public String getFinalDiscount() {
        return finalDiscount;
    }

    public void setFinalDiscount(String finalDiscount) {
        this.finalDiscount = finalDiscount;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getIsubCode() {
        return isubCode;
    }

    public void setIsubCode(String isubCode) {
        this.isubCode = isubCode;
    }

    public String getSgstAcCode() {
        return sgstAcCode;
    }

    public void setSgstAcCode(String sgstAcCode) {
        this.sgstAcCode = sgstAcCode;
    }

    public String getUgstAcCode() {
        return ugstAcCode;
    }

    public void setUgstAcCode(String ugstAcCode) {
        this.ugstAcCode = ugstAcCode;
    }

    public String getCgstAcCode() {
        return cgstAcCode;
    }

    public void setCgstAcCode(String cgstAcCode) {
        this.cgstAcCode = cgstAcCode;
    }

    public String getIgstAcCode() {
        return igstAcCode;
    }

    public void setIgstAcCode(String igstAcCode) {
        this.igstAcCode = igstAcCode;
    }

    public String getSgstRcmAcCode() {
        return sgstRcmAcCode;
    }

    public void setSgstRcmAcCode(String sgstRcmAcCode) {
        this.sgstRcmAcCode = sgstRcmAcCode;
    }

    public String getUgstRcmAcCode() {
        return ugstRcmAcCode;
    }

    public void setUgstRcmAcCode(String ugstRcmAcCode) {
        this.ugstRcmAcCode = ugstRcmAcCode;
    }

    public String getCgstRcmAcCode() {
        return cgstRcmAcCode;
    }

    public void setCgstRcmAcCode(String cgstRcmAcCode) {
        this.cgstRcmAcCode = cgstRcmAcCode;
    }

    public String getIgstRcmAcCode() {
        return igstRcmAcCode;
    }

    public void setIgstRcmAcCode(String igstRcmAcCode) {
        this.igstRcmAcCode = igstRcmAcCode;
    }

    
    public boolean isManualTax() {
        return manualTax;
    }

    public void setManualTax(boolean manualTax) {
        this.manualTax = manualTax;
    }

    public boolean isManualGstType() {
        return manualGstType;
    }

    public void setManualGstType(boolean manualGstType) {
        this.manualGstType = manualGstType;
    }

    public String getGstTypeCode() {
        return gstTypeCode;
    }

    public void setGstTypeCode(String gstTypeCode) {
        this.gstTypeCode = gstTypeCode;
    }

    public String getGstTypeName() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName = gstTypeName;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getSmfCode() {
        return smfCode;
    }

    public void setSmfCode(String smfCode) {
        this.smfCode = smfCode;
    }

    public String getServiceEvalTypeCode() {
        return serviceEvalTypeCode;
    }

    public void setServiceEvalTypeCode(String serviceEvalTypeCode) {
        this.serviceEvalTypeCode = serviceEvalTypeCode;
    }

    public String getServiceEvalTypeName() {
        return serviceEvalTypeName;
    }

    public void setServiceEvalTypeName(String serviceEvalTypeName) {
        this.serviceEvalTypeName = serviceEvalTypeName;
    }

    public String getServiceEvalRate() {
        return serviceEvalRate;
    }

    public void setServiceEvalRate(String serviceEvalRate) {
        this.serviceEvalRate = serviceEvalRate;
    }

    public GrdItemDetail_Mdl clone() {
        GrdItemDetail_Mdl grd_Mdl = new GrdItemDetail_Mdl();

        copy(grd_Mdl);

        return grd_Mdl;
    }

    public void copy(GrdItemDetail_Mdl grdItem_Mdl) {
        //Service
        grdItem_Mdl.setServiceRecord(this.serviceRecord);
        grdItem_Mdl.setSmfCode(this.getSmfCode());
        grdItem_Mdl.setServiceEvalTypeCode(this.getServiceEvalTypeCode());
        grdItem_Mdl.setServiceEvalTypeName(this.getServiceEvalTypeName());
        grdItem_Mdl.setServiceEvalRate(this.getServiceEvalRate());

        //Department
        grdItem_Mdl.setDepartmentCode(this.getDepartmentCode());
        grdItem_Mdl.setDepartmentName(this.getDepartmentName());

        //Item
        grdItem_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdItem_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());

        //Item Details
        grdItem_Mdl.setPropertyList(this.getPropertyList());
        grdItem_Mdl.setItemGSTNature(this.getItemGSTNature());
        grdItem_Mdl.setItsBasedOn(this.getItsBasedOn());
        grdItem_Mdl.setHsnCode(this.getHsnCode());
        grdItem_Mdl.setIsBestBefore(this.getIsBestBefore());
        grdItem_Mdl.setManualTax(this.isManualTax());
        grdItem_Mdl.setManualGstType(this.isManualGstType());
        grdItem_Mdl.setGstTypeCode(this.getGstTypeCode());
        grdItem_Mdl.setGstTypeName(this.getGstTypeName());
        grdItem_Mdl.setHsn(this.getHsn());

        //Bill Details
        grdItem_Mdl.setPbDocId(this.getPbDocId());
        grdItem_Mdl.setPbRecId(this.getPbRecId());
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
        grdItem_Mdl.setQuantity(this.getQuantity());
        grdItem_Mdl.setPendingQty(this.getPendingQty());
        grdItem_Mdl.setSkuQty(this.getSkuQty());

        //Rate
        grdItem_Mdl.setRate(this.getRate());
        grdItem_Mdl.setLandedSkuRate(this.getLandedSkuRate());
        grdItem_Mdl.setLandedSkuValue(this.getLandedSkuValue());
        grdItem_Mdl.setSkuRate(this.getSkuRate());
        grdItem_Mdl.setAmount(this.getAmount());
        grdItem_Mdl.setDiscountPer(this.getDiscountPer());
        grdItem_Mdl.setDiscount(this.getDiscount());
        grdItem_Mdl.setTaxableAmt(this.getTaxableAmt());
        grdItem_Mdl.setSgstUgstPer(this.getSgstUgstPer());
        grdItem_Mdl.setSgstUgst(this.getSgstUgst());
        grdItem_Mdl.setSgstPer(this.getCgstPer());
        grdItem_Mdl.setSgst(this.getCgst());
        grdItem_Mdl.setUgstPer(this.getIgstPer());
        grdItem_Mdl.setUgst(this.getIgst());
        grdItem_Mdl.setCgstPer(this.getCgstPer());
        grdItem_Mdl.setCgst(this.getCgst());
        grdItem_Mdl.setIgstPer(this.getIgstPer());
        grdItem_Mdl.setIgst(this.getIgst());
        grdItem_Mdl.setSubTotal(this.getSubTotal());
        grdItem_Mdl.setFinalDiscountPer(this.getFinalDiscountPer());
        grdItem_Mdl.setFinalDiscount(this.getFinalDiscount());
        grdItem_Mdl.setItemValue(this.getItemValue());

        //Properties
        grdItem_Mdl.setBatchNo(this.getBatchNo());
        grdItem_Mdl.setLotNo(this.getLotNo());
        grdItem_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdItem_Mdl.setExpiryDate(this.getExpiryDate());
        grdItem_Mdl.setColourCode(this.getColourCode());
        grdItem_Mdl.setColourName(this.getColourName());
        grdItem_Mdl.setSizeCode(this.getSizeCode());
        grdItem_Mdl.setSizeName(this.getSizeName());
        grdItem_Mdl.setItemMakeCode(this.getItemMakeCode());
        grdItem_Mdl.setItemMakeName(this.getItemMakeName());
        grdItem_Mdl.setMrp(this.getMrp());
        grdItem_Mdl.setSkuMrp(this.getSkuMrp());
        grdItem_Mdl.setPropertiesCsv(this.getPropertiesCsv());

        //IAP
        grdItem_Mdl.setIsubCode(this.getIsubCode());

        //Accounts
        grdItem_Mdl.setSgstAcCode(this.getSgstAcCode());
        grdItem_Mdl.setUgstAcCode(this.getUgstAcCode());
        grdItem_Mdl.setCgstAcCode(this.getCgstAcCode());
        grdItem_Mdl.setIgstAcCode(this.getIgstAcCode());
        grdItem_Mdl.setSgstRcmAcCode(this.getSgstRcmAcCode());
        grdItem_Mdl.setUgstRcmAcCode(this.getUgstRcmAcCode());
        grdItem_Mdl.setCgstRcmAcCode(this.getCgstRcmAcCode());
        grdItem_Mdl.setIgstRcmAcCode(this.getIgstRcmAcCode());
        
        //Other
        grdItem_Mdl.setRemark(this.getRemark());
        grdItem_Mdl.setV_SNo(this.getV_SNo());
        grdItem_Mdl.setItemMastPurchaseReturnAc(this.getItemMastPurchaseReturnAc());

        grdItem_Mdl.setIbCode(this.getIbCode());
    }

}
