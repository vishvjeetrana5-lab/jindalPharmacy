package aarogya.pharmacy.purchaseBillDirect.shared;

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
    private String IsMarkupApplicable;
    private String podMrp;
    private List<OverHeadLine_Mdl> OHLListPurchaseOrder;

    private TxtHMedicineName_Mdl txtHMedName_Mdl = new TxtHMedicineName_Mdl();

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

    public TxtHMedicineName_Mdl getTxtHMedName_Mdl() {
        return txtHMedName_Mdl;
    }

    public void setTxtHMedName_Mdl(TxtHMedicineName_Mdl txtHMedName_Mdl) {
        this.txtHMedName_Mdl = txtHMedName_Mdl;
    }

    public List<OverHeadLine_Mdl> getOHLListPurchaseOrder() {
        return OHLListPurchaseOrder;
    }

    public void setOHLListPurchaseOrder(List<OverHeadLine_Mdl> OHLListPurchaseOrder) {
        this.OHLListPurchaseOrder = OHLListPurchaseOrder;
    }

    public String getIsMarkupApplicable() {
        return IsMarkupApplicable;
    }

    public void setIsMarkupApplicable(String IsMarkupApplicable) {
        this.IsMarkupApplicable = IsMarkupApplicable;
    }

    public String getPodMrp() {
        return podMrp;
    }

    public void setPodMrp(String podMrp) {
        this.podMrp = podMrp;
    }

}
