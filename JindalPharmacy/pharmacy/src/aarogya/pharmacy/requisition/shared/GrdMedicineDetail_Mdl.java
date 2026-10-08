package aarogya.pharmacy.requisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GrdMedicineDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String skuName;
    private String allowDecimalPlaces;
    private String vSNo;
    private String qty;
    private String date;
    private String remark;
    private String isStatus;
    private String itemCurrPosition;
    private String purchaseDecimalPlaces;
    private String purchaseUnit;
    private List<KeyFieldExtended> lstOldRequisition = new ArrayList();

    public GrdMedicineDetail_Mdl() {
    }

    public String getAllowDecimalPlaces() {
        return allowDecimalPlaces;
    }

    public void setAllowDecimalPlaces(String allowDecimalPlaces) {
        this.allowDecimalPlaces = allowDecimalPlaces;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public List<KeyFieldExtended> getLstOldRequisition() {
        return lstOldRequisition;
    }

    public String getItemCurrPosition() {
        return itemCurrPosition;
    }

    public void setItemCurrPosition(String itemCurrPosition) {
        this.itemCurrPosition = itemCurrPosition;
    }

    public String getPurchaseDecimalPlaces() {
        return purchaseDecimalPlaces;
    }

    public void setPurchaseDecimalPlaces(String purchaseDecimalPlaces) {
        this.purchaseDecimalPlaces = purchaseDecimalPlaces;
    }

    public String getPurchaseUnit() {
        return purchaseUnit;
    }

    public void setPurchaseUnit(String purchaseUnit) {
        this.purchaseUnit = purchaseUnit;
    }
}
