package aarogya.pharmacy.purchaseBill.shared;

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
    private String itemGSTNature;
    private String itsBasedOn;
    private String hsnCode;

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
    private String batchNo;
    private String ibCode;
    private String barcode;
    private String manufacturingDate;
    private String expiryDate;

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
    private String pendingFreeQty;
    private String freeQty;
    private String skuQty;
    private String returnedQty;

    //Rate
    private String mrp;
    private String skuMrp;
    private String rate;
    private String saleRate;
    private String skuSaleRate;
    private String amount;

    //Other
    private String vSNo;
    private String isStatus;
    private String grossAcCode;
    private String grossAcName;
    
    private List<OverHeadLine_Mdl> OHLList;
    private List<OverHeadLine_Mdl> OHLListGRN;
    private List<OverHeadITS_Mdl> lstITS;

    public GrdItemDetail_Mdl() {
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getPendingFreeQty() {
        return pendingFreeQty;
    }

    public void setPendingFreeQty(String pendingFreeQty) {
        this.pendingFreeQty = pendingFreeQty;
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

    @Override
    public List<OverHeadLine_Mdl> getOHLList() {
        return this.OHLList;
    }

    @Override
    public void setOHLList(List<OverHeadLine_Mdl> ohlList) {
        this.OHLList = ohlList;
    }

    public List<OverHeadLine_Mdl> getOHLListGRN() {
        return OHLListGRN;
    }

    public void setOHLListGRN(List<OverHeadLine_Mdl> OHLListGRN) {
        this.OHLListGRN = OHLListGRN;
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

        //SKU
        grdItem_Mdl.setSkuDecimalPlaces(this.getSkuDecimalPlaces());
        grdItem_Mdl.setSkuCode(this.getSkuCode());
        grdItem_Mdl.setSkuName(this.getSkuName());

        //Purchase Unit
        grdItem_Mdl.setPurchaseUnitCode(this.getPurchaseUnitCode());
        grdItem_Mdl.setPurchaseUnitName(this.getPurchaseUnitName());
        grdItem_Mdl.setPurchaseUnitDisplayName(this.getPurchaseUnitDisplayName());
        grdItem_Mdl.setPurchaseUnitConversionRate(this.getPurchaseUnitConversionRate());
        grdItem_Mdl.setPurchaseUnitDecimalPlaces(this.getPurchaseUnitDecimalPlaces());
        grdItem_Mdl.setGstUnitCode(this.getGstUnitCode());

        //Properties
        grdItem_Mdl.setBatchNo(this.getBatchNo());
        grdItem_Mdl.setIbCode(this.getIbCode());
        grdItem_Mdl.setBarcode(this.getBarcode());
        grdItem_Mdl.setManufacturingDate(this.getManufacturingDate());
        grdItem_Mdl.setExpiryDate(this.getExpiryDate());

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

        //Quantities
        grdItem_Mdl.setDocQty(this.getDocQty());
        grdItem_Mdl.setAcceptQty(this.getAcceptQty());
        grdItem_Mdl.setFreeQty(this.getFreeQty());
        grdItem_Mdl.setPendingQty(this.getPendingQty());
        grdItem_Mdl.setPendingFreeQty(this.getPendingFreeQty());
        grdItem_Mdl.setSkuQty(this.getSkuQty());
        grdItem_Mdl.setReturnedQty(this.getReturnedQty());

        //Rate
        grdItem_Mdl.setMrp(this.getMrp());
        grdItem_Mdl.setRate(this.getRate());
        grdItem_Mdl.setSaleRate(this.getSaleRate());
        grdItem_Mdl.setAmount(this.getAmount());

        //Other
        grdItem_Mdl.setvSNo(this.getvSNo());
        grdItem_Mdl.setIsStatus(this.getIsStatus());
        grdItem_Mdl.setGrossAcCode(this.getGrossAcCode());
        grdItem_Mdl.setGrossAcName(this.getGrossAcName());
        grdItem_Mdl.setLstITS(OverHeadITS_Mdl.getITSListClone(this.getLstITS()));
        grdItem_Mdl.setOHLList(OverHeadLine_Mdl.getOHLListClone(this.getOHLList()));
        grdItem_Mdl.setOHLListGRN(OverHeadLine_Mdl.getOHLListClone(this.getOHLListGRN()));

    }

}
