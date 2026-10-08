package aarogya.inventory.itemTransfer.shared;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_SNo;
    private String quantity;
    private String skuName;
    private String remark;
    private String ibCode;
    private String propertyDisplay;
//    private TxtHItem_Mdl itemMdl;
    private ItemProperty_Mdl propertyMdl;
    private String requisitionDocId;
    private String requisitionNo;
    private String requisitionV_SNo;
    private String itemProperty;
    private boolean bestBefore = false;
    private String purchaseUnitConversionRate;
    private String skuDecimalPlaces;
    private String fromDeptStk;

    public GrdItemDetail_Mdl() {
    }

    public GrdItemDetail_Mdl(GrdItemDetail_Mdl gidMdl) {
//        this(gidMdl.getRequisitionDocId(), gidMdl.getRequisitionNo(), gidMdl.getRequisitionV_SNo(), gidMdl.getV_SNo(), gidMdl.getQuantity(), gidMdl.getSkuName(), gidMdl.getRemark(), gidMdl.getIbCode(), gidMdl.getPropertyDisplay(), gidMdl.getItemMdl(), gidMdl.getPropertyMdl(), gidMdl.getSharedKeyFieldCode(), gidMdl.getSharedKeyFieldName());
        this(gidMdl.getRequisitionDocId(), gidMdl.getRequisitionNo(), gidMdl.getRequisitionV_SNo(), gidMdl.getV_SNo(), gidMdl.getQuantity(),
                gidMdl.getSkuName(), gidMdl.getRemark(), gidMdl.getIbCode(), gidMdl.getPropertyDisplay(), gidMdl.getPropertyMdl(),
                gidMdl.getSharedKeyFieldCode(), gidMdl.getSharedKeyFieldName(), gidMdl.getItemProperty(), gidMdl.isBestBefore(),
                gidMdl.getPurchaseUnitConversionRate(), gidMdl.getSkuDecimalPlaces());
    }

//    private GrdItemDetail_Mdl(String requisitionDocId, String requisitionNo, String requisitionV_SNo, String v_SNo, String quantity, String skuName, String remark, String ibCode, String propertyDisplay, TxtHItem_Mdl itemMdl, ItemProperty_Mdl propertyMdl, String KeyFieldCode, String KeyFieldName) {
    private GrdItemDetail_Mdl(String requisitionDocId, String requisitionNo, String requisitionV_SNo, String v_SNo, String quantity, String skuName,
            String remark, String ibCode, String propertyDisplay, ItemProperty_Mdl propertyMdl, String KeyFieldCode, String KeyFieldName,
            String itemProperty, boolean bestBefore, String purchaseUnitConversionRate, String skuDecimalPlaces) {
        super(KeyFieldCode, KeyFieldName);

        this.requisitionDocId = requisitionDocId;
        this.requisitionNo = requisitionNo;
        this.requisitionV_SNo = requisitionV_SNo;
        this.v_SNo = v_SNo;
        this.quantity = quantity;
        this.skuName = skuName;
        this.remark = remark;
        this.ibCode = ibCode;
        this.propertyDisplay = propertyDisplay;
        this.propertyMdl = propertyMdl != null ? new ItemProperty_Mdl(propertyMdl) : null;
        this.itemProperty = itemProperty;
        this.bestBefore = bestBefore;
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public String getPropertyDisplay() {
        return propertyDisplay;
    }

    public void setPropertyDisplay(String propertyDisplay) {
        this.propertyDisplay = propertyDisplay;
    }

//    public TxtHItem_Mdl getItemMdl() {
//        return itemMdl;
//    }
//
//    public void setItemMdl(TxtHItem_Mdl itemMdl) {
//        this.itemMdl = itemMdl;
//    }
    public ItemProperty_Mdl getPropertyMdl() {
        return propertyMdl;
    }

    public void setPropertyMdl(ItemProperty_Mdl propertyMdl) {
        this.propertyMdl = propertyMdl;
    }

    public String getRequisitionDocId() {
        return requisitionDocId;
    }

    public void setRequisitionDocId(String requisitionDocId) {
        this.requisitionDocId = requisitionDocId;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }

    public String getRequisitionV_SNo() {
        return requisitionV_SNo;
    }

    public void setRequisitionV_SNo(String requisitionV_SNo) {
        this.requisitionV_SNo = requisitionV_SNo;
    }

    public String getItemProperty() {
        return itemProperty;
    }

    public void setItemProperty(String itemProperty) {
        this.itemProperty = itemProperty;
    }

    public boolean isBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(boolean bestBefore) {
        this.bestBefore = bestBefore;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getSkuDecimalPlaces() {
        return skuDecimalPlaces;
    }

    public void setSkuDecimalPlaces(String skuDecimalPlaces) {
        this.skuDecimalPlaces = skuDecimalPlaces;
    }

    public String getFromDeptStk() {
        return fromDeptStk;
    }

    public void setFromDeptStk(String fromDeptStk) {
        this.fromDeptStk = fromDeptStk;
    }
}
