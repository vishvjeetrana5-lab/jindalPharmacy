package aarogya.pharmacy.medicineIndentDisplay.shared;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class MedicineIndentDisplay_Mdl extends KeyField implements Serializable, IsSerializable {

    private String departmentCode;
    private String departmentName;
    private String itemName;
    private String itemCode;
    private String action;
    private String check;
    private String isStatus;
    private String requisitionToDeptQty;
    private String requisitionFromDeptQty;
    private String pendingIndentQty;
    private String currentStk;
    private String minQty;
    private String maxQty;
    private String reOrderQty;
    private String netReqQty;
    private String isChecked;
    private String skuName;
    private String skuCode;
    private String unitDecimalPlaces;

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getRequisitionToDeptQty() {
        return requisitionToDeptQty;
    }

    public void setRequisitionToDeptQty(String requisitionToDeptQty) {
        this.requisitionToDeptQty = requisitionToDeptQty;
    }

    public String getRequisitionFromDeptQty() {
        return requisitionFromDeptQty;
    }

    public void setRequisitionFromDeptQty(String requisitionFromDeptQty) {
        this.requisitionFromDeptQty = requisitionFromDeptQty;
    }

    public String getPendingIndentQty() {
        return pendingIndentQty;
    }

    public void setPendingIndentQty(String pendingIndentQty) {
        this.pendingIndentQty = pendingIndentQty;
    }

    public String getMinQty() {
        return minQty;
    }

    public void setMinQty(String minQty) {
        this.minQty = minQty;
    }

    public String getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(String maxQty) {
        this.maxQty = maxQty;
    }

    public String getReOrderQty() {
        return reOrderQty;
    }

    public void setReOrderQty(String reOrderQty) {
        this.reOrderQty = reOrderQty;
    }

    public String getCurrentStk() {
        return currentStk;
    }

    public void setCurrentStk(String currentStk) {
        this.currentStk = currentStk;
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

    public String getNetReqQty() {
        return netReqQty;
    }

    public void setNetReqQty(String netReqQty) {
        this.netReqQty = netReqQty;
    }

    
}
