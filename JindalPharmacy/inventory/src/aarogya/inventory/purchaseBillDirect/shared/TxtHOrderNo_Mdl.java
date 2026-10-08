package aarogya.inventory.purchaseBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.io.Serializable;
import java.util.List;

public class TxtHOrderNo_Mdl extends KeyField implements Serializable, IsSerializable {

    /* 
    sharedKeyFieldCode = poDocId
    sharedKeyFieldName = poRecId
     */
    private String poV_SNo;
    private String poRefNo;
    private String rate;
    private String amount;
    private String orderQty;
    private String pendingQty;
    private String skuQty;
    private String itemCode;
    private String itemName;
    private String unitCode;
    private String unitName;
    private String colourCode;
    private String colourName;
    private String sizeCode;
    private String sizeName;
    private String itemMakeCode;
    private String itemMakeName;
    private String properties;
    private String discountPer;
    private String discount;
    private boolean manualGstType;
    private String gstTypeCode;
    private String gstTypeName;
    private String hsn;

    private TxtHItemName_Mdl txtHItemName_Mdl = new TxtHItemName_Mdl();

    public TxtHOrderNo_Mdl() {
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

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
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

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public TxtHItemName_Mdl getTxtHItemName_Mdl() {
        return txtHItemName_Mdl;
    }

    public void setTxtHItemName_Mdl(TxtHItemName_Mdl txtHItemName_Mdl) {
        this.txtHItemName_Mdl = txtHItemName_Mdl;
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

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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
    
    
}
