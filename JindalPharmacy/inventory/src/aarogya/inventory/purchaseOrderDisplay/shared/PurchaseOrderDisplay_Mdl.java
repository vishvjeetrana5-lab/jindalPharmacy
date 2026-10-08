package aarogya.inventory.purchaseOrderDisplay.shared;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PurchaseOrderDisplay_Mdl extends KeyField implements Serializable, IsSerializable {

    private String departmentCode;
    private String departmentName;
    private String itemName;
    private String itemCode;
    private String action;
    private String check;
    private String isStatus;
    private String approvedQty;
    private String pendingOrderQty;
    private String isChecked;
    private String skuName;
    private String skuCode;
    private String unitDecimalPlaces;
    private String lastOrderRate;
    private String lastOrderSupplier;
    private String lastOrderSupplierCode;
    private String itemPurchaseunitName;
    private String itemPurchaseunitCode;
    private String purchaseUnitConversionRate;
    private String approvedSupplier;

    List<IndentDetail_Mdl> grdItemDetailList=new ArrayList<>();
    
    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getIsStatus() {
        return isStatus;
    }

    public void setIsStatus(String isStatus) {
        this.isStatus = isStatus;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getUnitDecimalPlaces() {
        return unitDecimalPlaces;
    }

    public void setUnitDecimalPlaces(String unitDecimalPlaces) {
        this.unitDecimalPlaces = unitDecimalPlaces;
    }

    public String getApprovedQty() {
        return approvedQty;
    }

    public void setApprovedQty(String approvedQty) {
        this.approvedQty = approvedQty;
    }

    public String getPendingOrderQty() {
        return pendingOrderQty;
    }

    public void setPendingOrderQty(String pendingOrderQty) {
        this.pendingOrderQty = pendingOrderQty;
    }

    public String getLastOrderRate() {
        return lastOrderRate;
    }

    public void setLastOrderRate(String lastOrderRate) {
        this.lastOrderRate = lastOrderRate;
    }

    public String getLastOrderSupplier() {
        return lastOrderSupplier;
    }

    public void setLastOrderSupplier(String lastOrderSupplier) {
        this.lastOrderSupplier = lastOrderSupplier;
    }

    public String getLastOrderSupplierCode() {
        return lastOrderSupplierCode;
    }

    public void setLastOrderSupplierCode(String lastOrderSupplierCode) {
        this.lastOrderSupplierCode = lastOrderSupplierCode;
    }

    public List<IndentDetail_Mdl> getGrdItemDetailList() {
        return grdItemDetailList;
    }

    public String getItemPurchaseunitName() {
        return itemPurchaseunitName;
    }

    public void setItemPurchaseunitName(String itemPurchaseunitName) {
        this.itemPurchaseunitName = itemPurchaseunitName;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getItemPurchaseunitCode() {
        return itemPurchaseunitCode;
    }

    public void setItemPurchaseunitCode(String itemPurchaseunitCode) {
        this.itemPurchaseunitCode = itemPurchaseunitCode;
    }

    public String getApprovedSupplier() {
        return approvedSupplier;
    }

    public void setApprovedSupplier(String approvedSupplier) {
        this.approvedSupplier = approvedSupplier;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.getSharedKeyFieldCode());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PurchaseOrderDisplay_Mdl other = (PurchaseOrderDisplay_Mdl) obj;
        if (!Objects.equals(this.getSharedKeyFieldCode(), other.getSharedKeyFieldCode())) {
            return false;
        }
        return true;
    }

}
