package aarogya.inventory.gateInSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdGateInDetailsSearch_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherSNo;
    private String quantity;
    private String unitCode;
    private String unitName;
    private String itemDescription;
    private String specification;
    private String estmValue;
    private String gateOutDate;
    private String gateOutTime;
    private String assetCode;
    private String assetName;
    private String estdDate;
    private String isChecked;
    private String pendingQty;
    private String department;
    
    

        public GrdGateInDetailsSearch_Mdl() {
    }

    public String getVoucherSNo() {
        return voucherSNo;
    }

    public void setVoucherSNo(String voucherSNo) {
        this.voucherSNo = voucherSNo;
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

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getEstmValue() {
        return estmValue;
    }

    public void setEstmValue(String estmValue) {
        this.estmValue = estmValue;
    }

    public String getGateOutDate() {
        return gateOutDate;
    }

    public void setGateOutDate(String gateOutDate) {
        this.gateOutDate = gateOutDate;
    }

    public String getGateOutTime() {
        return gateOutTime;
    }

    public void setGateOutTime(String gateOutTime) {
        this.gateOutTime = gateOutTime;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getEstdDate() {
        return estdDate;
    }

    public void setEstdDate(String estdDate) {
        this.estdDate = estdDate;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getPendingQty() {
        return pendingQty;
    }

    public void setPendingQty(String pendingQty) {
        this.pendingQty = pendingQty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

   
    
}
