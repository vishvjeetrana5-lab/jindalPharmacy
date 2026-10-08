package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class EGstDetail_Mdl implements Serializable, IsSerializable {

    private String docid;
    private String itemCode;
    private String ibCode;
    private String itemName;
    private String itemDesc;
    private String hsn;
    private String itemGstNature;
    private String quantity;
    private String unitCode;
    private String unitName;
    private String gstUnit;
    private String vSno;
    private String isService;
    private String batchNo;
    private String warrantyDate;
    private String expiryDate;
    private String rate;
    private String freeQty;
    private String discountAmount;
    private String gstRate;
    private String grossAmount;
    private String taxableAmount;
    private String sgstRate;
    private String cgstRate;
    private String igstRate;
    private String cessRate;
    private String cessNonAdvolRate;

    private String sgstAmount;
    private String igstAmount;
    private String cgstAmount;
    private String cessAmount;
    private String cessNaAmount;
    private String stateCessRate;
    private String stateCessAmount;
    private String stateCessNaAmount;
    private String netAmount;
    private String otherCharges;

    private String propertieDispCsv;
    private String barCode;
    private String orderRefNo;
    private String originCountry;
    private String itemUSno;

    public EGstDetail_Mdl() {
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getIbCode() {
        return ibCode;
    }

    public void setIbCode(String ibCode) {
        this.ibCode = ibCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(String taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(String sgstRate) {
        this.sgstRate = sgstRate;
    }

    public String getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(String cgstRate) {
        this.cgstRate = cgstRate;
    }

    public String getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(String igstRate) {
        this.igstRate = igstRate;
    }

    public String getCessRate() {
        return cessRate;
    }

    public void setCessRate(String cessRate) {
        this.cessRate = cessRate;
    }

    public String getCessNonAdvolRate() {
        return cessNonAdvolRate;
    }

    public void setCessNonAdvolRate(String cessNonAdvolRate) {
        this.cessNonAdvolRate = cessNonAdvolRate;
    }

    public String getGstUnit() {
        return gstUnit;
    }

    public void setGstUnit(String gstUnit) {
        this.gstUnit = gstUnit;
    }

    public String getvSno() {
        return vSno;
    }

    public void setvSno(String vSno) {
        this.vSno = vSno;
    }

    public String getIsService() {
        return isService;
    }

    public void setIsService(String isService) {
        this.isService = isService;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getGstRate() {
        return gstRate;
    }

    public void setGstRate(String gstRate) {
        this.gstRate = gstRate;
    }

    public String getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(String sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    public String getIgstAmount() {
        return igstAmount;
    }

    public void setIgstAmount(String igstAmount) {
        this.igstAmount = igstAmount;
    }

    public String getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(String cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    public String getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(String cessAmount) {
        this.cessAmount = cessAmount;
    }

    public String getCessNaAmount() {
        return cessNaAmount;
    }

    public void setCessNaAmount(String cessNaAmount) {
        this.cessNaAmount = cessNaAmount;
    }

    public String getStateCessRate() {
        return stateCessRate;
    }

    public void setStateCessRate(String stateCessRate) {
        this.stateCessRate = stateCessRate;
    }

    public String getStateCessAmount() {
        return stateCessAmount;
    }

    public void setStateCessAmount(String stateCessAmount) {
        this.stateCessAmount = stateCessAmount;
    }

    public String getStateCessNaAmount() {
        return stateCessNaAmount;
    }

    public void setStateCessNaAmount(String stateCessNaAmount) {
        this.stateCessNaAmount = stateCessNaAmount;
    }

    public String getOtherCharges() {
        return otherCharges;
    }

    public void setOtherCharges(String otherCharges) {
        this.otherCharges = otherCharges;
    }

    public String getItemGstNature() {
        return itemGstNature;
    }

    public void setItemGstNature(String itemGstNature) {
        this.itemGstNature = itemGstNature;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getPropertieDispCsv() {
        return propertieDispCsv;
    }

    public void setPropertieDispCsv(String propertieDispCsv) {
        this.propertieDispCsv = propertieDispCsv;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getOrderRefNo() {
        return orderRefNo;
    }

    public void setOrderRefNo(String orderRefNo) {
        this.orderRefNo = orderRefNo;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getItemUSno() {
        return itemUSno;
    }

    public void setItemUSno(String itemUSno) {
        this.itemUSno = itemUSno;
    }

}
