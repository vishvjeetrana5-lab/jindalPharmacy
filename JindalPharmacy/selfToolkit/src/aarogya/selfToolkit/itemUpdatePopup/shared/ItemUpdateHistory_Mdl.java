package aarogya.selfToolkit.itemUpdatePopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class ItemUpdateHistory_Mdl implements Serializable, IsSerializable {

    private String id;
    private String docId;
    private String v_SNo;
    private String v_Type;
    private String v_No;
    private String v_Date;
    private String recId;
    private String subcode;
    private String itemCode;
    private String unitCode;
    private String previousMrp;
    private String currentMrp;
    private String previousSaleRate;
    private String currentSaleRate;
    private String previousPurchaseRate;
    private String currentPurchaseRate;
    private String previousPurchaseDiscountPer;
    private String currentPurchaseDiscountPer;
    private String previousSchemeOnQty;
    private String currentSchemeOnQty;
    private String previousSchemeFreeQty;
    private String currentSchemeFreeQty;

    public ItemUpdateHistory_Mdl() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getV_No() {
        return v_No;
    }

    public void setV_No(String v_No) {
        this.v_No = v_No;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getPreviousMrp() {
        return previousMrp;
    }

    public void setPreviousMrp(String previousMrp) {
        this.previousMrp = previousMrp;
    }

    public String getCurrentMrp() {
        return currentMrp;
    }

    public void setCurrentMrp(String currentMrp) {
        this.currentMrp = currentMrp;
    }

    public String getPreviousSaleRate() {
        return previousSaleRate;
    }

    public void setPreviousSaleRate(String previousSaleRate) {
        this.previousSaleRate = previousSaleRate;
    }

    public String getCurrentSaleRate() {
        return currentSaleRate;
    }

    public void setCurrentSaleRate(String currentSaleRate) {
        this.currentSaleRate = currentSaleRate;
    }

    public String getPreviousPurchaseRate() {
        return previousPurchaseRate;
    }

    public void setPreviousPurchaseRate(String previousPurchaseRate) {
        this.previousPurchaseRate = previousPurchaseRate;
    }

    public String getCurrentPurchaseRate() {
        return currentPurchaseRate;
    }

    public void setCurrentPurchaseRate(String currentPurchaseRate) {
        this.currentPurchaseRate = currentPurchaseRate;
    }

    public String getPreviousPurchaseDiscountPer() {
        return previousPurchaseDiscountPer;
    }

    public void setPreviousPurchaseDiscountPer(String previousPurchaseDiscountPer) {
        this.previousPurchaseDiscountPer = previousPurchaseDiscountPer;
    }

    public String getCurrentPurchaseDiscountPer() {
        return currentPurchaseDiscountPer;
    }

    public void setCurrentPurchaseDiscountPer(String currentPurchaseDiscountPer) {
        this.currentPurchaseDiscountPer = currentPurchaseDiscountPer;
    }

    public String getPreviousSchemeOnQty() {
        return previousSchemeOnQty;
    }

    public void setPreviousSchemeOnQty(String previousSchemeOnQty) {
        this.previousSchemeOnQty = previousSchemeOnQty;
    }

    public String getCurrentSchemeOnQty() {
        return currentSchemeOnQty;
    }

    public void setCurrentSchemeOnQty(String currentSchemeOnQty) {
        this.currentSchemeOnQty = currentSchemeOnQty;
    }

    public String getPreviousSchemeFreeQty() {
        return previousSchemeFreeQty;
    }

    public void setPreviousSchemeFreeQty(String previousSchemeFreeQty) {
        this.previousSchemeFreeQty = previousSchemeFreeQty;
    }

    public String getCurrentSchemeFreeQty() {
        return currentSchemeFreeQty;
    }

    public void setCurrentSchemeFreeQty(String currentSchemeFreeQty) {
        this.currentSchemeFreeQty = currentSchemeFreeQty;
    }

}
