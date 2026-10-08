package aarogya.inventory.purchaseBill.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable {

    /*
    sharedKeyFieldCode = itemCode
    sharedKeyFieldName = itemName
     */
    //Item Details
    private String itemGSTNature;
    private String itsBasedOn;
    private boolean manualTax;  
    private boolean manualGstType;  
    private String gstTypeCode;  
    private String gstTypeName;  
    private String hsnCode;
    private String hsn;  
    private String isBestBefore;
    private String propertyList;

    //SKU
    private String skuDecimalPlaces;
    private String skuCode;
    private String skuName;

    //Purchase Unit
    private String purchaseUnitCode;
    private String purchaseUnitName;
    private String purchaseUnitDisplayName;
    private String purchaseUnitDecimalPlaces;
    private String purchaseUnitConversionRate;
    private String gstUnitCode;

    //Properties
    private String ibCode;
    private String batchNo;
    private String lotNo;
    private String manufacturingDate;
    private String expiryDate;
    private String mrp;
    private String skuMrp;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String itemMakeCode;
    private String itemMakeName;
    private String propertiesCsv;

    //Order Details
    private String poDocId;
    private String poRecId;
    private String poV_SNo;

    //Challan Details
    private String pcDocId;
    private String pcRecId;
    private String pcV_SNo;
    private String pcRefNo;
    private String pcRefDate;
    private String pcDepartmentCode;

    //Quantities
    private String docQty;
    private String acceptQty;
    private String pendingQty;
    private String skuQty;
    private String returnedQty;

    //Rate
    private String rate;
    private String landedSkuRate;
    private String landedSkuValue;
    private String skuRate;
    private String amount;
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

    //Servcie
    private boolean serviceRecord;
    private String smfCode;
    private String serviceEvalTypeCode;
    private String serviceEvalTypeName;
    private String serviceEvalRate;

    //IAP
    private String isubCode;  
    private String itemMastPurchaseAc;
    
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
    private String vSNo;
    private String isStatus;


    public GrdItemDetail_Mdl() {
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
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

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
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

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
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

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
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

    public String getPcRefNo() {
        return pcRefNo;
    }

    public void setPcRefNo(String pcRefNo) {
        this.pcRefNo = pcRefNo;
    }

    public String getPcRefDate() {
        return pcRefDate;
    }

    public void setPcRefDate(String pcRefDate) {
        this.pcRefDate = pcRefDate;
    }

    public String getPcDepartmentCode() {
        return pcDepartmentCode;
    }

    public void setPcDepartmentCode(String pcDepartmentCode) {
        this.pcDepartmentCode = pcDepartmentCode;
    }

    public String getDocQty() {
        return docQty;
    }

    public void setDocQty(String docQty) {
        this.docQty = docQty;
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

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
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

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getItemMastPurchaseAc() {
        return itemMastPurchaseAc;
    }

    public void setItemMastPurchaseAc(String itemMastPurchaseAc) {
        this.itemMastPurchaseAc = itemMastPurchaseAc;
    }


    public boolean isServiceRecord() {
        return serviceRecord;
    }

    public void setServiceRecord(boolean serviceRecord) {
        this.serviceRecord = serviceRecord;
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
        grdItem_Mdl.setManualTax(this.isManualTax());
        grdItem_Mdl.setManualGstType(this.isManualGstType());
        grdItem_Mdl.setGstTypeCode(this.getGstTypeCode());
        grdItem_Mdl.setGstTypeName(this.getGstTypeName());
        grdItem_Mdl.setHsnCode(this.getHsnCode());
        grdItem_Mdl.setHsn(this.getHsn());
        grdItem_Mdl.setIsBestBefore(this.getIsBestBefore());
        grdItem_Mdl.setPropertyList(this.getPropertyList());

        //SKU
        grdItem_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());
        grdItem_Mdl.setSkuCode(this.getSkuCode());
        grdItem_Mdl.setSkuName(this.getSkuName());

        //Purchase Unit
        grdItem_Mdl.setPurchaseUnitCode(this.getPurchaseUnitCode());
        grdItem_Mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        grdItem_Mdl.setPurchaseUnitDisplayName(this.getPurchaseUnitDisplayName());
        grdItem_Mdl.setPurchaseUnitDecimalPlaces(this.getPurchaseUnitDecimalPlaces());
        grdItem_Mdl.setPurchaseUnitConversionRate(this.getPurchaseUnitConversionRate());
        grdItem_Mdl.setGstUnitCode(this.getGstUnitCode());

        //Properties
        grdItem_Mdl.setIbCode(this.getIbCode());
        grdItem_Mdl.setBatchNo(this.getBatchNo());
        grdItem_Mdl.setLotNo(this.getLotNo());
        grdItem_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdItem_Mdl.setExpiryDate(this.getExpiryDate());
        grdItem_Mdl.setMrp(this.getMrp());
        grdItem_Mdl.setSkuMrp(this.getSkuMrp());
        grdItem_Mdl.setColourCode(this.getColourCode());
        grdItem_Mdl.setColourName(this.getColourName());
        grdItem_Mdl.setSizeCode(this.getSizeCode());
        grdItem_Mdl.setSizeName(this.getSizeName());
        grdItem_Mdl.setPropertiesCsv(this.getPropertiesCsv());

        //Order Details
        grdItem_Mdl.setPoDocId(this.getPoDocId());
        grdItem_Mdl.setPoRecId(this.getPoRecId());
        grdItem_Mdl.setPoV_SNo(this.getPoV_SNo());

        //Order Details
        grdItem_Mdl.setPcDocId(this.getPcDocId());
        grdItem_Mdl.setPcRecId(this.getPcRecId());
        grdItem_Mdl.setPcV_SNo(this.getPcV_SNo());
        grdItem_Mdl.setPcRefNo(this.getPcRefNo());
        grdItem_Mdl.setPcRefDate(this.getPcRefDate());
        grdItem_Mdl.setPcDepartmentCode(this.getPcDepartmentCode());

        //Quantities
        grdItem_Mdl.setDocQty(this.getDocQty());
        grdItem_Mdl.setAcceptQty(this.getAcceptQty());
        grdItem_Mdl.setPendingQty(this.getPendingQty());
        grdItem_Mdl.setSkuQty(this.getSkuQty());
        grdItem_Mdl.setReturnedQty(this.getReturnedQty());

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

        //Service
        grdItem_Mdl.setServiceRecord(this.isServiceRecord());
        grdItem_Mdl.setSmfCode(this.getSmfCode());
        grdItem_Mdl.setServiceEvalTypeCode(this.getServiceEvalTypeCode());
        grdItem_Mdl.setServiceEvalTypeName(this.getServiceEvalTypeName());
        grdItem_Mdl.setServiceEvalRate(this.getServiceEvalRate());

        //IAP
        grdItem_Mdl.setIsubCode(this.getIsubCode());
        grdItem_Mdl.setItemMastPurchaseAc(this.getItemMastPurchaseAc());

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
        grdItem_Mdl.setvSNo(this.getvSNo());
        grdItem_Mdl.setIsStatus(this.getIsStatus());

    }

}
