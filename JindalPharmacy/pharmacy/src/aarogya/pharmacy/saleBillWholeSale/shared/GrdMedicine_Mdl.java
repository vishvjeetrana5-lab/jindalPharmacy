package aarogya.pharmacy.saleBillWholeSale.shared;

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

    private String medicineCategory, medicineGroupCode, medicineGroupName, unit;
    private String vSno;
    private String doseName;
    private String doseValue;
    private String freqCode;
    private String freqName;
    private String frequencyValue;
    private String day;
    private String duration;
    private String durationText;
    private String combineDuration;
    private String totalDays;
    private boolean allowBifurcation;
    private String quantity;
    private String batchQuantityInPurchaseUnit;
    private String freeQuantityInSku;
    private String freeQuantityInPurchaseUnit;
    private String freeWithItemCode;
    private String prvFreeQty;
    private String quantityTotalPerItem;
    private String amount;
    private String rate;
    private String wholeSaleRate;
    private String wholeSaleRateActual;
    private String mrp;
    private String discount;
    private String discountPer;
    private String discount2;
    private String discountPer2;
    private String discountPerUnit;
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
    private String totalQuantitySKU;
    private String saleAcCode;
    private String sbDocId;
    private String isChecked;
    private String status;
    private String medicineActiveStatusCode;
    private String medicineActiveStatusName;
    private String prvQty;
    private String purchaseUnitConversionRate;
    private String discountGivenByTag;
    private String discountGivenByText;
    private String discountReasonCode;
    private String discountReasonName;
    private String siteAllowed;
    private boolean returnSale = false, schemeItem = false;
    private TxtHMedicine_Mdl medicineMdl;

    private String gstPer;
    private String gstAmount;
    private String landedSkuValue;
    private String barcode;
    private String itemCodeCsv;
    private String gstUnitCode;
    private String gstUnitName;
    private String purchaseUnitCode;
    private String purchaseUnitName;

    public GrdMedicine_Mdl() {
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

    public String getDiscountPerUnit() {
        return discountPerUnit;
    }

    public void setDiscountPerUnit(String discountPerUnit) {
        this.discountPerUnit = discountPerUnit;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getSiteAllowed() {
        return siteAllowed;
    }

    public void setSiteAllowed(String siteAllowed) {
        this.siteAllowed = siteAllowed;
    }

    public String getMedicineActiveStatusCode() {
        return medicineActiveStatusCode;
    }

    public void setMedicineActiveStatusCode(String medicineActiveStatusCode) {
        this.medicineActiveStatusCode = medicineActiveStatusCode;
    }

    public String getMedicineActiveStatusName() {
        return medicineActiveStatusName;
    }

    public void setMedicineActiveStatusName(String medicineActiveStatusName) {
        this.medicineActiveStatusName = medicineActiveStatusName;
    }

    public String getPrvFreeQty() {
        return prvFreeQty;
    }

    public void setPrvFreeQty(String prvFreeQty) {
        this.prvFreeQty = prvFreeQty;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getCombineDuration() {
        return combineDuration;
    }

    public void setCombineDuration(String combineDuration) {
        this.combineDuration = combineDuration;
    }

    public boolean isSchemeItem() {
        return schemeItem;
    }

    public void setSchemeItem(boolean schemeItem) {
        this.schemeItem = schemeItem;
    }

    public String getTotalQuantitySKU() {
        return totalQuantitySKU;
    }

    public void setTotalQuantitySKU(String totalQuantitySKU) {
        this.totalQuantitySKU = totalQuantitySKU;
    }

    public String getFreeWithItemCode() {
        return freeWithItemCode;
    }

    public void setFreeWithItemCode(String freeWithItemCode) {
        this.freeWithItemCode = freeWithItemCode;
    }

    public String getFreeQuantityInSku() {
        return freeQuantityInSku;
    }

    public void setFreeQuantityInSku(String freeQuantityInSku) {
        this.freeQuantityInSku = freeQuantityInSku;
    }

    public String getBatchQuantityInPurchaseUnit() {
        return batchQuantityInPurchaseUnit;
    }

    public void setBatchQuantityInPurchaseUnit(String batchQuantityInPurchaseUnit) {
        this.batchQuantityInPurchaseUnit = batchQuantityInPurchaseUnit;
    }

    public String getFreeQuantityInPurchaseUnit() {
        return freeQuantityInPurchaseUnit;
    }

    public void setFreeQuantityInPurchaseUnit(String freeQuantityInPurchaseUnit) {
        this.freeQuantityInPurchaseUnit = freeQuantityInPurchaseUnit;
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

    public String getMedicineGroupCode() {
        return medicineGroupCode;
    }

    public void setMedicineGroupCode(String medicineGroupCode) {
        this.medicineGroupCode = medicineGroupCode;
    }

    public String getMedicineGroupName() {
        return medicineGroupName;
    }

    public void setMedicineGroupName(String medicineGroupName) {
        this.medicineGroupName = medicineGroupName;
    }

    public String getDiscountGivenByTag() {
        return discountGivenByTag;
    }

    public void setDiscountGivenByTag(String discountGivenByTag) {
        this.discountGivenByTag = discountGivenByTag;
    }

    public String getDiscountGivenByText() {
        return discountGivenByText;
    }

    public void setDiscountGivenByText(String discountGivenByText) {
        this.discountGivenByText = discountGivenByText;
    }

    public String getDiscountReasonCode() {
        return discountReasonCode;
    }

    public void setDiscountReasonCode(String discountReasonCode) {
        this.discountReasonCode = discountReasonCode;
    }

    public String getDiscountReasonName() {
        return discountReasonName;
    }

    public void setDiscountReasonName(String discountReasonName) {
        this.discountReasonName = discountReasonName;
    }

    public String getQuantityTotalPerItem() {
        return quantityTotalPerItem;
    }

    public void setQuantityTotalPerItem(String quantityTotalPerItem) {
        this.quantityTotalPerItem = quantityTotalPerItem;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getPrvQty() {
        return prvQty;
    }

    public void setPrvQty(String prvQty) {
        this.prvQty = prvQty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getDoseName() {
        return doseName;
    }

    public void setDoseName(String doseName) {
        this.doseName = doseName;
    }

    public String getFreqCode() {
        return freqCode;
    }

    public void setFreqCode(String freqCode) {
        this.freqCode = freqCode;
    }

    public String getFreqName() {
        return freqName;
    }

    public void setFreqName(String freqName) {
        this.freqName = freqName;
    }

    public String getFrequencyValue() {
        return frequencyValue;
    }

    public void setFrequencyValue(String frequencyValue) {
        this.frequencyValue = frequencyValue;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    public String getMedicineCategory() {
        return medicineCategory;
    }

    public void setMedicineCategory(String medicineCategory) {
        this.medicineCategory = medicineCategory;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDoseValue() {
        return doseValue;
    }

    public void setDoseValue(String doseValue) {
        this.doseValue = doseValue;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public boolean isReturnSale() {
        return returnSale;
    }

    public void setReturnSale(boolean returnSale) {
        this.returnSale = returnSale;
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

    public boolean isAllowBifurcation() {
        return allowBifurcation;
    }

    public void setAllowBifurcation(boolean allowBifurcation) {
        this.allowBifurcation = allowBifurcation;
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

//    private GrdMedicine_Mdl(String ibCode, String batchNo, String expDate, String mfgDate, String medicineCategory, String medicineGroupCode, String medicineGroupName, String unit, String vSno, String doseName, String doseValue, String freqCode, String freqName, String frequencyValue, String day, String duration, String durationText, String combineDuration, String totalDays, boolean allowBifurcation, String quantity, String batchQuantityInPurchaseUnit, String freeQuantityInSku, String freeQuantityInPurchaseUnit, String freeWithItemCode, String prvFreeQty, String quantityTotalPerItem, String amount, String rate, String mrp, String skuMrp, String skuRate, String discount, String discountPer, String discount2, String discountPer2, String discountPerUnit, String cgstPer, String cgstAmt, String cgstAcCode, String sgstPer, String sgstAmt, String sgstAcCode, String ugstPer, String ugstAmt, String ugstAcCode, String igstPer, String igstAmt, String igstAcCode, String taxableAmt, String netAmt, String quantitySKU, String totalQuantitySKU, String saleAcCode, String reqDocId, String requisitionNo, String sbDocId, String reqVSno, String isChecked, String status, String medicineActiveStatusCode, String medicineActiveStatusName, String prvQty, String purchaseUnitConversionRate, String discountGivenByTag, String discountGivenByText, String discountReasonCode, String discountReasonName, String siteAllowed, TxtHMedicine_Mdl medicineMdl, String gstPer, String gstAmount, String landedSkuValue, String KeyFieldCode, String KeyFieldName, boolean returnSale, boolean schemeItem, String barcode, String itemCodeCsv, String purchaseUnitCode, String purchaseUnitName) {
    private GrdMedicine_Mdl(String ibCode, String batchNo, String expDate, String mfgDate, String medicineCategory, String medicineGroupCode, String medicineGroupName, String unit, String vSno, String doseName, String doseValue, String freqCode, String freqName, String frequencyValue, String day, String duration, String durationText, String combineDuration, String totalDays, boolean allowBifurcation, String quantity, String batchQuantityInPurchaseUnit, String freeQuantityInSku, String freeQuantityInPurchaseUnit, String freeWithItemCode, String prvFreeQty, String quantityTotalPerItem, String amount, String rate, String mrp, String discount, String discountPer, String discount2, String discountPer2, String discountPerUnit, String cgstPer, String cgstAmt, String cgstAcCode, String sgstPer, String sgstAmt, String sgstAcCode, String ugstPer, String ugstAmt, String ugstAcCode, String igstPer, String igstAmt, String igstAcCode, String taxableAmt, String netAmt, String quantitySKU, String totalQuantitySKU, String saleAcCode, String sbDocId, String isChecked, String status, String medicineActiveStatusCode, String medicineActiveStatusName, String prvQty, String purchaseUnitConversionRate, String discountGivenByTag, String discountGivenByText, String discountReasonCode, String discountReasonName, String siteAllowed, TxtHMedicine_Mdl medicineMdl, String gstPer, String gstAmount, String landedSkuValue, String KeyFieldCode, String KeyFieldName, boolean returnSale, boolean schemeItem, String barcode, String itemCodeCsv, String purchaseUnitCode, String purchaseUnitName) {
        super(KeyFieldCode, KeyFieldName);
        this.ibCode = ibCode;
        this.batchNo = batchNo;
        this.expDate = expDate;
        this.mfgDate = mfgDate;
        this.medicineCategory = medicineCategory;
        this.medicineGroupCode = medicineGroupCode;
        this.medicineGroupName = medicineGroupName;
        this.unit = unit;
        this.vSno = vSno;
        this.doseName = doseName;
        this.doseValue = doseValue;
        this.freqCode = freqCode;
        this.freqName = freqName;
        this.frequencyValue = frequencyValue;
        this.returnSale = returnSale;
        this.schemeItem = schemeItem;
        this.day = day;
        this.duration = duration;
        this.durationText = durationText;
        this.combineDuration = combineDuration;
        this.totalDays = totalDays;
        this.allowBifurcation = allowBifurcation;
        this.quantity = quantity;
        this.batchQuantityInPurchaseUnit = batchQuantityInPurchaseUnit;
        this.freeQuantityInSku = freeQuantityInSku;
        this.freeQuantityInPurchaseUnit = freeQuantityInPurchaseUnit;
        this.freeWithItemCode = freeWithItemCode;
        this.prvFreeQty = prvFreeQty;
        this.quantityTotalPerItem = quantityTotalPerItem;
        this.amount = amount;
        this.rate = rate;
        this.mrp = mrp;
//        this.skuMrp = skuMrp;
//        this.skuRate = skuRate;
        this.discount2 = discount2;
        this.discountPer2 = discountPer2;
        this.discountPerUnit = discountPerUnit;
        this.discount = discount;
        this.discountPer = discountPer;
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
        this.totalQuantitySKU = totalQuantitySKU;
        this.saleAcCode = saleAcCode;
        this.sbDocId = sbDocId;
        this.isChecked = isChecked;
        this.status = status;
        this.medicineActiveStatusCode = medicineActiveStatusCode;
        this.medicineActiveStatusName = medicineActiveStatusName;
        this.prvQty = prvQty;
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
        this.discountGivenByTag = discountGivenByTag;
        this.discountGivenByText = discountGivenByText;
        this.discountReasonCode = discountReasonCode;
        this.discountReasonName = discountReasonName;
        this.siteAllowed = siteAllowed;
        this.medicineMdl = medicineMdl.clone();
        this.gstPer = gstPer;
        this.gstAmount = gstAmount;
        this.landedSkuValue = landedSkuValue;
        this.barcode = barcode;
        this.itemCodeCsv = itemCodeCsv;
        this.purchaseUnitCode = purchaseUnitCode;
        this.purchaseUnitName = purchaseUnitName;
    }

    public GrdMedicine_Mdl(GrdMedicine_Mdl mdl) {
//        this(mdl.getIbCode(), mdl.getBatchNo(), mdl.getExpDate(), mdl.getMfgDate(), mdl.getMedicineCategory(), mdl.getMedicineGroupCode(), mdl.getMedicineGroupName(), mdl.getUnit(), mdl.getvSno(), mdl.getDoseName(), mdl.getDoseValue(), mdl.getFreqCode(), mdl.getFreqName(), mdl.getFrequencyValue(), mdl.getDay(), mdl.getDuration(), mdl.getDurationText(), mdl.getCombineDuration(), mdl.getTotalDays(), mdl.isAllowBifurcation(), mdl.getQuantity(), mdl.getBatchQuantityInPurchaseUnit(), mdl.getFreeQuantityInSku(), mdl.getFreeQuantityInPurchaseUnit(), mdl.getFreeWithItemCode(), mdl.getPrvFreeQty(), mdl.getQuantityTotalPerItem(), mdl.getAmount(), mdl.getRate(), mdl.getMrp(), mdl.getSkuMrp(), mdl.getSkuRate(), mdl.getDiscount(), mdl.getDiscountPer(), mdl.getDiscount2(), mdl.getDiscountPer2(), mdl.getDiscountPerUnit(), mdl.getCgstPer(), mdl.getCgstAmt(), mdl.getCgstAcCode(), mdl.getSgstPer(), mdl.getSgstAmt(), mdl.getSgstAcCode(), mdl.getUgstPer(), mdl.getUgstAmt(), mdl.getUgstAcCode(), mdl.getIgstPer(), mdl.getIgstAmt(), mdl.getIgstAcCode(), mdl.getTaxableAmt(), mdl.getNetAmt(), mdl.getQuantitySKU(), mdl.getTotalQuantitySKU(), mdl.getSaleAcCode(), mdl.getReqDocId(), mdl.getRequisitionNo(), mdl.getSbDocId(), mdl.getReqVSno(), mdl.getIsChecked(), mdl.getStatus(), mdl.getMedicineActiveStatusCode(), mdl.getMedicineActiveStatusName(), mdl.getPrvQty(), mdl.getPurchaseUnitConversionRate(), mdl.getDiscountGivenByTag(), mdl.getDiscountGivenByText(), mdl.getDiscountReasonCode(), mdl.getDiscountReasonName(), mdl.getSiteAllowed(), mdl.getMedicineMdl(), mdl.getGstPer(), mdl.getGstAmount(), mdl.getLandedSkuValue(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.isReturnSale(), mdl.isSchemeItem(), mdl.getBarcode(), mdl.getItemCodeCsv(), mdl.getPurchaseUnitCode(), mdl.getPurchaseUnitName());
        this(mdl.getIbCode(), mdl.getBatchNo(), mdl.getExpDate(), mdl.getMfgDate(), mdl.getMedicineCategory(), mdl.getMedicineGroupCode(), mdl.getMedicineGroupName(), mdl.getUnit(), mdl.getvSno(), mdl.getDoseName(), mdl.getDoseValue(), mdl.getFreqCode(), mdl.getFreqName(), mdl.getFrequencyValue(), mdl.getDay(), mdl.getDuration(), mdl.getDurationText(), mdl.getCombineDuration(), mdl.getTotalDays(), mdl.isAllowBifurcation(), mdl.getQuantity(), mdl.getBatchQuantityInPurchaseUnit(), mdl.getFreeQuantityInSku(), mdl.getFreeQuantityInPurchaseUnit(), mdl.getFreeWithItemCode(), mdl.getPrvFreeQty(), mdl.getQuantityTotalPerItem(), mdl.getAmount(), mdl.getRate(), mdl.getMrp(), mdl.getDiscount(), mdl.getDiscountPer(), mdl.getDiscount2(), mdl.getDiscountPer2(), mdl.getDiscountPerUnit(), mdl.getCgstPer(), mdl.getCgstAmt(), mdl.getCgstAcCode(), mdl.getSgstPer(), mdl.getSgstAmt(), mdl.getSgstAcCode(), mdl.getUgstPer(), mdl.getUgstAmt(), mdl.getUgstAcCode(), mdl.getIgstPer(), mdl.getIgstAmt(), mdl.getIgstAcCode(), mdl.getTaxableAmt(), mdl.getNetAmt(), mdl.getQuantitySKU(), mdl.getTotalQuantitySKU(), mdl.getSaleAcCode(), mdl.getSbDocId(), mdl.getIsChecked(), mdl.getStatus(), mdl.getMedicineActiveStatusCode(), mdl.getMedicineActiveStatusName(), mdl.getPrvQty(), mdl.getPurchaseUnitConversionRate(), mdl.getDiscountGivenByTag(), mdl.getDiscountGivenByText(), mdl.getDiscountReasonCode(), mdl.getDiscountReasonName(), mdl.getSiteAllowed(), mdl.getMedicineMdl(), mdl.getGstPer(), mdl.getGstAmount(), mdl.getLandedSkuValue(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.isReturnSale(), mdl.isSchemeItem(), mdl.getBarcode(), mdl.getItemCodeCsv(), mdl.getPurchaseUnitCode(), mdl.getPurchaseUnitName());
    }

    public String getGstUnitCode() {
        return gstUnitCode;
    }

    public void setGstUnitCode(String gstUnitCode) {
        this.gstUnitCode = gstUnitCode;
    }

    public String getGstUnitName() {
        return gstUnitName;
    }

    public void setGstUnitName(String gstUnitName) {
        this.gstUnitName = gstUnitName;
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
}
