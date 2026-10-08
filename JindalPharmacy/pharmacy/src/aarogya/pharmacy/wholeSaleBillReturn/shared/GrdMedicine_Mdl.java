package aarogya.pharmacy.wholeSaleBillReturn.shared;

import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdMedicine_Mdl extends KeyField implements Serializable {
    //SharedFieldCode : itemCode
    //SharedFieldName : itemName

    private String ibCode;
    private String batchNo;
    private String expDate;
    private String mfgDate;
    private boolean bestBefore = false;

    private String unit;
    private String vSno;
    private String prvQuantity;
    private String prvFreeQuantity;
    private String quantity;
    private String sbQuantity;
    private String sbFreeQuantity;
    private String amount;
    private String rate;
    private String wholeSaleRate;
    private String wholeSaleRateActual;
    private String mrp;
    private String skuMrp;
    private String skuRate;
    private String discount;
    private String discount2;
    private String discountPer2;
    private String tempDiscountPer2;
    private String totalDiscount;
    private String cgstPer;
    private String cgstAmt;
    private String cgstAcCode;
    private String sgstPer;
    private String sgstAmt;
    private String sgstAcCode;
    private String ugstPer;
    private String ugstAmt;
    private String ugstAcCode;
    private String igstPer;
    private String igstAmt;
    private String igstAcCode;
    private String taxableAmt;
    private String netAmt;
    private String quantitySKU;
    private String saleAcCode;
    private String sbDocId;
    private String sbRecId;
    private String sbV_Sno;
    private String isChecked;

    private TxtHMedicine_Mdl medicineMdl;
    private String freeQty;
    private String freeWithItemCode;

    private String gstPer;
    private String gstAmount;
    private String landedSkuValue;
    private String barcode;
    private String grossPurchaseRate;
    private String itemCodeCsv;

    public GrdMedicine_Mdl() {
    }

    public String getPrvQuantity() {
        return prvQuantity;
    }

    public void setPrvQuantity(String prvQuantity) {
        this.prvQuantity = prvQuantity;
    }

    public String getPrvFreeQuantity() {
        return prvFreeQuantity;
    }

    public void setPrvFreeQuantity(String prvFreeQuantity) {
        this.prvFreeQuantity = prvFreeQuantity;
    }

    public String getTempDiscountPer2() {
        return tempDiscountPer2;
    }

    public void setTempDiscountPer2(String tempDiscountPer2) {
        this.tempDiscountPer2 = tempDiscountPer2;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getFreeWithItemCode() {
        return freeWithItemCode;
    }

    public void setFreeWithItemCode(String freeWithItemCode) {
        this.freeWithItemCode = freeWithItemCode;
    }

    public String getGstPer() {
        return gstPer;
    }

    public void setGstPer(String gstPer) {
        this.gstPer = gstPer;
    }

    public String getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(String gstAmount) {
        this.gstAmount = gstAmount;
    }

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getSbRecId() {
        return sbRecId;
    }

    public void setSbRecId(String sbRecId) {
        this.sbRecId = sbRecId;
    }

    public String getSbQuantity() {
        return sbQuantity;
    }

    public void setSbQuantity(String sbQuantity) {
        this.sbQuantity = sbQuantity;
    }

    public String getSbFreeQuantity() {
        return sbFreeQuantity;
    }

    public void setSbFreeQuantity(String sbFreeQuantity) {
        this.sbFreeQuantity = sbFreeQuantity;
    }

    public String getSbV_Sno() {
        return sbV_Sno;
    }

    public void setSbV_Sno(String sbV_Sno) {
        this.sbV_Sno = sbV_Sno;
    }

    public String getSbDocId() {
        return sbDocId;
    }

    public void setSbDocId(String sbDocId) {
        this.sbDocId = sbDocId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String Amount) {
        this.amount = Amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getWholeSaleRate() {
        return wholeSaleRate;
    }

    public void setWholeSaleRate(String wholeSaleRate) {
        this.wholeSaleRate = wholeSaleRate;
    }

    public String getWholeSaleRateActual() {
        return wholeSaleRateActual;
    }

    public void setWholeSaleRateActual(String wholeSaleRateActual) {
        this.wholeSaleRateActual = wholeSaleRateActual;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(String cgstPer) {
        this.cgstPer = cgstPer;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(String sgstPer) {
        this.sgstPer = sgstPer;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getUgstPer() {
        return ugstPer;
    }

    public void setUgstPer(String ugstPer) {
        this.ugstPer = ugstPer;
    }

    public String getUgstAmt() {
        return ugstAmt;
    }

    public void setUgstAmt(String ugstAmt) {
        this.ugstAmt = ugstAmt;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(String netAmt) {
        this.netAmt = netAmt;
    }

    public String getCgstAcCode() {
        return cgstAcCode;
    }

    public void setCgstAcCode(String cgstAcCode) {
        this.cgstAcCode = cgstAcCode;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSaleAcCode() {
        return saleAcCode;
    }

    public void setSaleAcCode(String saleAcCode) {
        this.saleAcCode = saleAcCode;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public TxtHMedicine_Mdl getMedicineMdl() {
        return medicineMdl;
    }

    public void setMedicineMdl(TxtHMedicine_Mdl medicineMdl) {
        this.medicineMdl = medicineMdl;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getQuantitySKU() {
        return quantitySKU;
    }

    public void setQuantitySKU(String quantitySKU) {
        this.quantitySKU = quantitySKU;
    }

    public String getIgstPer() {
        return igstPer;
    }

    public void setIgstPer(String igstPer) {
        this.igstPer = igstPer;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getIgstAcCode() {
        return igstAcCode;
    }

    public void setIgstAcCode(String igstAcCode) {
        this.igstAcCode = igstAcCode;
    }

    public String getSkuMrp() {
        return skuMrp;
    }

    public void setSkuMrp(String skuMrp) {
        this.skuMrp = skuMrp;
    }

    public String getSkuRate() {
        return skuRate;
    }

    public void setSkuRate(String skuRate) {
        this.skuRate = skuRate;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public boolean isBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(boolean bestBefore) {
        this.bestBefore = bestBefore;
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

    public String getDiscount2() {
        return discount2;
    }

    public void setDiscount2(String discount2) {
        this.discount2 = discount2;
    }

    public String getDiscountPer2() {
        return discountPer2;
    }

    public void setDiscountPer2(String discountPer2) {
        this.discountPer2 = discountPer2;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemCodeCsv() {
        return itemCodeCsv;
    }

    public void setItemCodeCsv(String itemCodeCsv) {
        this.itemCodeCsv = itemCodeCsv;
    }

    public GrdMedicine_Mdl(String ibCode, String batchNo, String expDate, String mfgDate, boolean bestBefore, String unit, String vSno, String quantity, String sbQuantity, String sbFreeQuantity, String prvQuantity, String prvFreeQuantity, String amount, String rate, String mrp, String skuMrp, String skuRate, String discount, String discount2, String discountPer2, String tempDiscountPer2, String totalDiscount, String cgstPer, String cgstAmt, String cgstAcCode, String sgstPer, String sgstAmt, String sgstAcCode, String ugstPer, String ugstAmt, String ugstAcCode, String igstPer, String igstAmt, String igstAcCode, String taxableAmt, String netAmt, String quantitySKU, String saleAcCode, String sbDocId, String sbRecId, String sbV_Sno, String isChecked, TxtHMedicine_Mdl medicineMdl, String freeQty, String freeWithItemCode, String gstPer, String gstAmount, String landedSkuValue, String KeyFieldCode, String KeyFieldName, String barcode, String itemCodeCsv) {
        super(KeyFieldCode, KeyFieldName);
        this.ibCode = ibCode;
        this.batchNo = batchNo;
        this.expDate = expDate;
        this.mfgDate = mfgDate;
        this.bestBefore = bestBefore;
        this.unit = unit;
        this.vSno = vSno;
        this.quantity = quantity;
        this.sbQuantity = sbQuantity;
        this.sbFreeQuantity = sbFreeQuantity;
        this.amount = amount;
        this.rate = rate;
        this.mrp = mrp;
        this.skuMrp = skuMrp;
        this.skuRate = skuRate;
        this.discount = discount;
        this.discount2 = discount2;
        this.discountPer2 = discountPer2;
        this.cgstPer = cgstPer;
        this.cgstAmt = cgstAmt;
        this.cgstAcCode = cgstAcCode;
        this.sgstPer = sgstPer;
        this.sgstAmt = sgstAmt;
        this.sgstAcCode = sgstAcCode;
        this.ugstPer = ugstPer;
        this.ugstAmt = ugstAmt;
        this.ugstAcCode = ugstAcCode;
        this.igstPer = igstPer;
        this.igstAmt = igstAmt;
        this.igstAcCode = igstAcCode;
        this.taxableAmt = taxableAmt;
        this.netAmt = netAmt;
        this.quantitySKU = quantitySKU;
        this.saleAcCode = saleAcCode;
        this.sbDocId = sbDocId;
        this.sbRecId = sbRecId;
        this.sbV_Sno = sbV_Sno;
        this.isChecked = isChecked;
        this.medicineMdl = medicineMdl.clone();
        this.freeQty = freeQty;
        this.freeWithItemCode = freeWithItemCode;
        this.gstPer = gstPer;
        this.gstAmount = gstAmount;
        this.landedSkuValue = landedSkuValue;
        this.prvFreeQuantity = prvFreeQuantity;
        this.prvQuantity = prvQuantity;
        this.tempDiscountPer2 = tempDiscountPer2;
        this.totalDiscount = totalDiscount;
        this.barcode = barcode;
        this.itemCodeCsv = itemCodeCsv;
    }

    public GrdMedicine_Mdl(GrdMedicine_Mdl mdl) {
        this(mdl.getIbCode(), mdl.getBatchNo(), mdl.getExpDate(), mdl.getMfgDate(), mdl.isBestBefore(), mdl.getUnit(), mdl.getvSno(), mdl.getQuantity(), mdl.getSbQuantity(), mdl.getSbFreeQuantity(), mdl.getPrvQuantity(), mdl.getPrvFreeQuantity(), mdl.getAmount(), mdl.getRate(), mdl.getMrp(), mdl.getSkuMrp(), mdl.getSkuRate(), mdl.getDiscount(), mdl.getDiscount2(), mdl.getDiscountPer2(), mdl.getTempDiscountPer2(), mdl.getTotalDiscount(), mdl.getCgstPer(), mdl.getCgstAmt(), mdl.getCgstAcCode(), mdl.getSgstPer(), mdl.getSgstAmt(), mdl.getSgstAcCode(), mdl.getUgstPer(), mdl.getUgstAmt(), mdl.getUgstAcCode(), mdl.getIgstPer(), mdl.getIgstAmt(), mdl.getIgstAcCode(), mdl.getTaxableAmt(), mdl.getNetAmt(), mdl.getQuantitySKU(), mdl.getSaleAcCode(), mdl.getSbDocId(), mdl.getSbRecId(), mdl.getSbV_Sno(), mdl.getIsChecked(), mdl.getMedicineMdl(), mdl.getFreeQty(), mdl.getFreeWithItemCode(), mdl.getGstPer(), mdl.getGstAmount(), mdl.getLandedSkuValue(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.getBarcode(), mdl.getItemCodeCsv());
    }

    public String getGrossPurchaseRate() {
        return grossPurchaseRate;
    }

    public void setGrossPurchaseRate(String grossPurchaseRate) {
        this.grossPurchaseRate = grossPurchaseRate;
    }

}
