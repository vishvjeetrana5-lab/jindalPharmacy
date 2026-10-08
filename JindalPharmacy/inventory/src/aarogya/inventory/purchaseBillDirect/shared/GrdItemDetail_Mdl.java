package aarogya.inventory.purchaseBillDirect.shared;

import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdateField_Mdl;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdItemDetail_Mdl extends KeyField implements Serializable {

    /*
    sharedKeyFieldCode = itemCode
    sharedKeyFieldName = itemName
     */
    //Unloading Info
    private String unloadingDocId;
    private String unloadingV_Sno;

    //Department
    private String departmentCode;
    private String departmentName;

    //Item Details
    private String propertyList;
    private String itemGSTNature;
    private String itsBasedOn;
    private String hsnCode;
    private String isBestBefore;
    private boolean manualTax;
    private boolean manualGstType;
    private String gstTypeCode;
    private String gstTypeName;

    private String hsn;

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
    private String purchaseRate;

    //Properties
    private String batchNo;
    private String lotNo;
    private String manufacturingDate;
    private String bestBeforeDays;
    private String expiryDate;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String itemMakeCode;
    private String itemMakeName;
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
    private String shortageQty;
    private String rejQty;
    private String labRejectedQty;
    private String qcRejectedQty;
    private String skuDifferenceQty;
    private String skuQty;
    private String acceptQty;
    private String pendingQty;
    private String returnedQty;

    //Lab
    private String isLabRequired;
    private boolean labStatus;

    //Other
    private String remark;
    private String vSNo;
    private String refNo;
    private String refDate;
    private String supplierName;
    private String ibCode;
    private String isStatus;

    //IAP
    private String isubCode;

    //Servcie
    private boolean serviceRecord;
    private String smfCode;
    private String serviceEvalTypeCode;
    private String serviceEvalTypeName;
    private String serviceEvalRate;

    //Rate
    private String rate;
    private String landedSkuRate;
    private String landedSkuValue;
    private String skuRate;
    private String amount;
    private String drCrNoteAmt;
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

    //Accounts
    private String sgstAcCode;
    private String ugstAcCode;
    private String cgstAcCode;
    private String igstAcCode;
    private String sgstRcmAcCode;
    private String ugstRcmAcCode;
    private String cgstRcmAcCode;
    private String igstRcmAcCode;
    private String itemMastPurchaseAc;

    // private List<OverHeadLine_Mdl> OHLList;
    //  private List<OverHeadLine_Mdl> OHLListPurchaseOrder;
    //  private List<OverHeadITS_Mdl> lstITS;
    private List<GrdImpurity_Mdl> lstImpurity = new ArrayList<GrdImpurity_Mdl>();
    private List<GrdLabEntry_Mdl> lstLabEntry = new ArrayList<>();
    private List<ItemUpdateField_Mdl> lstItemUpdateFields = new ArrayList<>();

    public GrdItemDetail_Mdl() {
    }

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getUnloadingDocId() {
        return unloadingDocId;
    }

    public void setUnloadingDocId(String unloadingDocId) {
        this.unloadingDocId = unloadingDocId;
    }

    public String getUnloadingV_Sno() {
        return unloadingV_Sno;
    }

    public void setUnloadingV_Sno(String unloadingV_Sno) {
        this.unloadingV_Sno = unloadingV_Sno;
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

    public String getLabRejectedQty() {
        return labRejectedQty;
    }

    public void setLabRejectedQty(String labRejectedQty) {
        this.labRejectedQty = labRejectedQty;
    }

    public String getQcRejectedQty() {
        return qcRejectedQty;
    }

    public void setQcRejectedQty(String qcRejectedQty) {
        this.qcRejectedQty = qcRejectedQty;
    }

    public String getDrCrNoteAmt() {
        return drCrNoteAmt;
    }

    public void setDrCrNoteAmt(String drCrNoteAmt) {
        this.drCrNoteAmt = drCrNoteAmt;
    }

    public String getIsLabRequired() {
        return isLabRequired;
    }

    public void setIsLabRequired(String isLabRequired) {
        this.isLabRequired = isLabRequired;
    }

    public boolean isLabStatus() {
        return labStatus;
    }

    public void setLabStatus(boolean labStatus) {
        this.labStatus = labStatus;
    }

    public List<GrdLabEntry_Mdl> getLstLabEntry() {
        return lstLabEntry;
    }

    public void setLstLabEntry(List<GrdLabEntry_Mdl> lstLabEntry) {
        this.lstLabEntry = lstLabEntry;
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

    public List<GrdImpurity_Mdl> getLstImpurity() {
        return lstImpurity;
    }

    public void setLstImpurity(List<GrdImpurity_Mdl> lstImpurity) {
        this.lstImpurity = lstImpurity;
    }

    public boolean isServiceRecord() {
        return serviceRecord;
    }

    public void setServiceRecord(boolean serviceRecord) {
        this.serviceRecord = serviceRecord;
    }

    public String getShortageQty() {
        return shortageQty;
    }

    public void setShortageQty(String shortageQty) {
        this.shortageQty = shortageQty;
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

    public String getIsubCode() {
        return isubCode;
    }

    public void setIsubCode(String isubCode) {
        this.isubCode = isubCode;
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

    public String getItemMastPurchaseAc() {
        return itemMastPurchaseAc;
    }

    public void setItemMastPurchaseAc(String itemMastPurchaseAc) {
        this.itemMastPurchaseAc = itemMastPurchaseAc;
    }

    public GrdItemDetail_Mdl clone() {
        GrdItemDetail_Mdl grd_Mdl = new GrdItemDetail_Mdl();

        copy(grd_Mdl);

        return grd_Mdl;
    }

    public void copy(GrdItemDetail_Mdl grdGrn_Mdl) {
        //Service
        grdGrn_Mdl.setServiceRecord(this.serviceRecord);
        grdGrn_Mdl.setSmfCode(this.getSmfCode());
        grdGrn_Mdl.setServiceEvalTypeCode(this.getServiceEvalTypeCode());
        grdGrn_Mdl.setServiceEvalTypeName(this.getServiceEvalTypeName());
        grdGrn_Mdl.setServiceEvalRate(this.getServiceEvalRate());

        //Unloading
        grdGrn_Mdl.setUnloadingDocId(this.getUnloadingDocId());
        grdGrn_Mdl.setUnloadingV_Sno(this.getUnloadingV_Sno());

        //Department
        grdGrn_Mdl.setDepartmentCode(this.getDepartmentCode());
        grdGrn_Mdl.setDepartmentName(this.getDepartmentName());

        //Item
        grdGrn_Mdl.setSharedKeyFieldCode(this.getSharedKeyFieldCode());
        grdGrn_Mdl.setSharedKeyFieldName(this.getSharedKeyFieldName());

        //Item Details
        grdGrn_Mdl.setPropertyList(this.getPropertyList());
        grdGrn_Mdl.setItemGSTNature(this.getItemGSTNature());
        grdGrn_Mdl.setManualTax(this.isManualTax());
        grdGrn_Mdl.setManualGstType(this.isManualGstType());
        grdGrn_Mdl.setGstTypeCode(this.getGstTypeCode());
        grdGrn_Mdl.setGstTypeName(this.getGstTypeName());
        grdGrn_Mdl.setItsBasedOn(this.getItsBasedOn());
        grdGrn_Mdl.setHsnCode(this.getHsnCode());
        grdGrn_Mdl.setHsn(this.getHsn());
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
        grdGrn_Mdl.setLotNo(this.getLotNo());
        grdGrn_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdGrn_Mdl.setBestBeforeDays(this.getBestBeforeDays());
        grdGrn_Mdl.setExpiryDate(this.getExpiryDate());
        grdGrn_Mdl.setColourCode(this.getColourCode());
        grdGrn_Mdl.setColourName(this.getColourName());
        grdGrn_Mdl.setSizeCode(this.getSizeCode());
        grdGrn_Mdl.setSizeName(this.getSizeName());
        grdGrn_Mdl.setItemMakeCode(this.getItemMakeCode());
        grdGrn_Mdl.setItemMakeName(this.getItemMakeName());
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
        grdGrn_Mdl.setShortageQty(this.getShortageQty());
        grdGrn_Mdl.setRejQty(this.getRejQty());
        grdGrn_Mdl.setLabRejectedQty(this.getLabRejectedQty());
        grdGrn_Mdl.setQcRejectedQty(this.getQcRejectedQty());
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
        grdGrn_Mdl.setDrCrNoteAmt(this.getDrCrNoteAmt());
        grdGrn_Mdl.setDiscountPer(this.getDiscountPer());
        grdGrn_Mdl.setDiscount(this.getDiscount());
        grdGrn_Mdl.setTaxableAmt(this.getTaxableAmt());
        grdGrn_Mdl.setSgstUgstPer(this.getSgstUgstPer());
        grdGrn_Mdl.setSgstUgst(this.getSgstUgst());
        grdGrn_Mdl.setSgstPer(this.getCgstPer());
        grdGrn_Mdl.setSgst(this.getCgst());
        grdGrn_Mdl.setUgstPer(this.getIgstPer());
        grdGrn_Mdl.setUgst(this.getIgst());
        grdGrn_Mdl.setCgstPer(this.getCgstPer());
        grdGrn_Mdl.setCgst(this.getCgst());
        grdGrn_Mdl.setIgstPer(this.getIgstPer());
        grdGrn_Mdl.setIgst(this.getIgst());
        grdGrn_Mdl.setSubTotal(this.getSubTotal());
        grdGrn_Mdl.setFinalDiscountPer(this.getFinalDiscountPer());
        grdGrn_Mdl.setFinalDiscount(this.getFinalDiscount());
        grdGrn_Mdl.setItemValue(this.getItemValue());

        //Lab
        grdGrn_Mdl.setIsLabRequired(this.getIsLabRequired());
        grdGrn_Mdl.setLabStatus(this.isLabStatus());

        //IAP
        grdGrn_Mdl.setIsubCode(this.getIsubCode());
        grdGrn_Mdl.setItemMastPurchaseAc(this.getItemMastPurchaseAc());

        //Accounts
        grdGrn_Mdl.setSgstAcCode(this.getSgstAcCode());
        grdGrn_Mdl.setUgstAcCode(this.getUgstAcCode());
        grdGrn_Mdl.setCgstAcCode(this.getCgstAcCode());
        grdGrn_Mdl.setIgstAcCode(this.getIgstAcCode());
        grdGrn_Mdl.setSgstRcmAcCode(this.getSgstRcmAcCode());
        grdGrn_Mdl.setUgstRcmAcCode(this.getUgstRcmAcCode());
        grdGrn_Mdl.setCgstRcmAcCode(this.getCgstRcmAcCode());
        grdGrn_Mdl.setIgstRcmAcCode(this.getIgstRcmAcCode());

        //Other
        grdGrn_Mdl.setRemark(this.getRemark());
        grdGrn_Mdl.setvSNo(this.getvSNo());
        grdGrn_Mdl.setRefNo(this.getRefNo());
        grdGrn_Mdl.setRefDate(this.getRefDate());
        grdGrn_Mdl.setSupplierName(this.getSupplierName());
        grdGrn_Mdl.setIbCode(this.getIbCode());
        grdGrn_Mdl.setIsStatus(this.getIsStatus());
        grdGrn_Mdl.setServiceRecord(this.isServiceRecord());
        grdGrn_Mdl.getLstImpurity().addAll(cloneImpurityList(this.getLstImpurity()));
        grdGrn_Mdl.getLstLabEntry().addAll(cloneLabList(this.getLstLabEntry()));
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

    public List<GrdLabEntry_Mdl> cloneLabList(List<GrdLabEntry_Mdl> list) {
        List<GrdLabEntry_Mdl> grdILabEntry_MdlList = null;

        if (list != null) {
            grdILabEntry_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdILabEntry_MdlList.add(list.get(i).clone());
            }
        }

        return grdILabEntry_MdlList;
    }

    public List<ItemUpdateField_Mdl> getLstItemUpdateFields() {
        return lstItemUpdateFields;
    }

    public void setLstItemUpdateFields(List<ItemUpdateField_Mdl> lstItemUpdateFields) {
        this.lstItemUpdateFields = lstItemUpdateFields;
    }

    public String getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate = purchaseRate;
    }
}
