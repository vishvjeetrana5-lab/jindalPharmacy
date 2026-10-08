package aarogya.inventory.workBillDirect.shared;

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
    private String properties;
    private List<OverHeadLine_Mdl> OHLListPurchaseOrder;

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

    public List<OverHeadLine_Mdl> getOHLListPurchaseOrder() {
        return OHLListPurchaseOrder;
    }

    public void setOHLListPurchaseOrder(List<OverHeadLine_Mdl> OHLListPurchaseOrder) {
        this.OHLListPurchaseOrder = OHLListPurchaseOrder;
    }

}
