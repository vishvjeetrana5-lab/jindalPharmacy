package dataman.dmToolkit.itemAdjustment.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItemAdjustment_Mdl extends KeyField implements Serializable, IsSerializable {

    private String isSelected;
    private String v_TypeCode;
    private String v_TypeName;
    private String v_MnuText;
    private String v_MnuAttachedInModule;
    private String v_No;
    private String v_SNo;
    private String v_Date;
    private String refNo;
    private String partyCode;
    private String partyName;
    private String itemCode;
    private String itemName;
    private String skuQty;
    private String skuCode;
    private String skuName;
    private String landedSkuValue;
    private String adjustment;
    private String sno;
    private String view;

    public GrdItemAdjustment_Mdl() {

    }

    public GrdItemAdjustment_Mdl(GrdItemAdjustment_Mdl giaMdl) {
        this(giaMdl.getIsSelected(), giaMdl.getV_TypeCode(), giaMdl.getV_TypeName(), giaMdl.getV_MnuText(), giaMdl.getV_MnuAttachedInModule(), giaMdl.getV_No(), giaMdl.getV_SNo(), giaMdl.getV_Date(), giaMdl.getRefNo(), giaMdl.getPartyCode(), giaMdl.getPartyName(), giaMdl.getItemCode(), giaMdl.getItemName(), giaMdl.getSkuQty(), giaMdl.getSkuCode(), giaMdl.getSkuName(), giaMdl.getLandedSkuValue(), giaMdl.getAdjustment(), giaMdl.getSno(), giaMdl.getView(), giaMdl.getSharedKeyFieldCode(), giaMdl.getSharedKeyFieldName());
    }

    public GrdItemAdjustment_Mdl(String isSelected, String v_TypeCode, String v_TypeName, String v_MnuText, String v_MnuAttachedInModule, String v_No, String v_SNo, String v_Date, String refNo, String partyCode, String partyName, String itemCode, String itemName, String skuQty, String skuCode, String skuName, String landedSkuValue, String adjustment, String sr, String view, String KeyFieldCode, String KeyFieldName) {
        super(KeyFieldCode, KeyFieldName);
        this.isSelected = isSelected;
        this.v_TypeCode = v_TypeCode;
        this.v_TypeName = v_TypeName;
        this.v_MnuText = v_MnuText;
        this.v_MnuAttachedInModule = v_MnuAttachedInModule;
        this.v_No = v_No;
        this.v_SNo = v_SNo;
        this.v_Date = v_Date;
        this.refNo = refNo;
        this.partyCode = partyCode;
        this.partyName = partyName;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.skuQty = skuQty;
        this.skuCode = skuCode;
        this.skuName = skuName;
        this.landedSkuValue = landedSkuValue;
        this.adjustment = adjustment;
        this.sno = sr;
        this.view = view;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getV_TypeCode() {
        return v_TypeCode;
    }

    public void setV_TypeCode(String v_TypeCode) {
        this.v_TypeCode = v_TypeCode;
    }

    public String getV_TypeName() {
        return v_TypeName;
    }

    public void setV_TypeName(String v_TypeName) {
        this.v_TypeName = v_TypeName;
    }

    public String getV_MnuText() {
        return v_MnuText;
    }

    public void setV_MnuText(String v_MnuText) {
        this.v_MnuText = v_MnuText;
    }

    public String getV_MnuAttachedInModule() {
        return v_MnuAttachedInModule;
    }

    public void setV_MnuAttachedInModule(String v_MnuAttachedInModule) {
        this.v_MnuAttachedInModule = v_MnuAttachedInModule;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getPartyCode() {
        return partyCode;
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
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

    public String getSkuQty() {
        return skuQty;
    }

    public void setSkuQty(String skuQty) {
        this.skuQty = skuQty;
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

    public String getLandedSkuValue() {
        return landedSkuValue;
    }

    public void setLandedSkuValue(String landedSkuValue) {
        this.landedSkuValue = landedSkuValue;
    }

    public String getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(String adjustment) {
        this.adjustment = adjustment;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public GrdItemAdjustment_Mdl clone() {
        return new GrdItemAdjustment_Mdl(this);
    }

}
