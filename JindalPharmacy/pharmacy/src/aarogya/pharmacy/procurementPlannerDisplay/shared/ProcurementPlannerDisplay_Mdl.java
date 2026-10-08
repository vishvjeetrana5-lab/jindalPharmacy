package aarogya.pharmacy.procurementPlannerDisplay.shared;
import aarogya.pharmacy.procurementPlannerDisplay.shared.*;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class ProcurementPlannerDisplay_Mdl extends KeyField implements Serializable, IsSerializable {

    private String departmentCode;
    private String departmentName;
    private String itemName;
    private String itemCode;
    private String skuName;
    private String skuCode;
    private String currentStkDept;
    private String currentStkSite;
    private String consumptionDept;
    private String consumptionSite;
    private String surplusDeficitDept;
    private String surplusDeficitSite;
    private String stkRunDaysDept;
    private String stkRunDaysSite;
    private String requiredQtyDept;
    private String requiredQtySite;
    private String pendingPOIndentQty;
    private String recommendedQtyDept;
    private String recommendedQtySite;
    private String finalIndentedQty;
    private String remark;
    private String requiredDate;
    private String purchaseUnitConversionRate;
    private String purchaseRate;
    private String estimatedCost;

    
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

    public String getCurrentStkDept() {
        return currentStkDept;
    }

    public void setCurrentStkDept(String currentStkDept) {
        this.currentStkDept = currentStkDept;
    }

    public String getCurrentStkSite() {
        return currentStkSite;
    }

    public void setCurrentStkSite(String currentStkSite) {
        this.currentStkSite = currentStkSite;
    }

    public String getConsumptionDept() {
        return consumptionDept;
    }

    public void setConsumptionDept(String consumptionDept) {
        this.consumptionDept = consumptionDept;
    }

    public String getConsumptionSite() {
        return consumptionSite;
    }

    public void setConsumptionSite(String consumptionSite) {
        this.consumptionSite = consumptionSite;
    }

    public String getSurplusDeficitDept() {
        return surplusDeficitDept;
    }

    public void setSurplusDeficitDept(String surplusDeficitDept) {
        this.surplusDeficitDept = surplusDeficitDept;
    }

    public String getSurplusDeficitSite() {
        return surplusDeficitSite;
    }

    public void setSurplusDeficitSite(String surplusDeficitSite) {
        this.surplusDeficitSite = surplusDeficitSite;
    }

    public String getStkRunDaysDept() {
        return stkRunDaysDept;
    }

    public void setStkRunDaysDept(String stkRunDaysDept) {
        this.stkRunDaysDept = stkRunDaysDept;
    }

    public String getStkRunDaysSite() {
        return stkRunDaysSite;
    }

    public void setStkRunDaysSite(String stkRunDaysSite) {
        this.stkRunDaysSite = stkRunDaysSite;
    }

    public String getRequiredQtyDept() {
        return requiredQtyDept;
    }

    public void setRequiredQtyDept(String requiredQtyDept) {
        this.requiredQtyDept = requiredQtyDept;
    }

    public String getRequiredQtySite() {
        return requiredQtySite;
    }

    public void setRequiredQtySite(String requiredQtySite) {
        this.requiredQtySite = requiredQtySite;
    }

    public String getPendingPOIndentQty() {
        return pendingPOIndentQty;
    }

    public void setPendingPOIndentQty(String pendingPOIndentQty) {
        this.pendingPOIndentQty = pendingPOIndentQty;
    }

    public String getRecommendedQtyDept() {
        return recommendedQtyDept;
    }

    public void setRecommendedQtyDept(String recommendedQtyDept) {
        this.recommendedQtyDept = recommendedQtyDept;
    }

    public String getRecommendedQtySite() {
        return recommendedQtySite;
    }

    public void setRecommendedQtySite(String recommendedQtySite) {
        this.recommendedQtySite = recommendedQtySite;
    }

    public String getFinalIndentedQty() {
        return finalIndentedQty;
    }

    public void setFinalIndentedQty(String finalIndentedQty) {
        this.finalIndentedQty = finalIndentedQty;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getPurchaseUnitConversionRate() {
        return purchaseUnitConversionRate;
    }

    public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
        this.purchaseUnitConversionRate = purchaseUnitConversionRate;
    }

    public String getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public String getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(String estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

}
