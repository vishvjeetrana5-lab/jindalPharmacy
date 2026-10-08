package aarogya.inventory.pendingPODisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdPendingPO_Display_Mdl extends KeyField implements Serializable, IsSerializable {

    private String ApprovedBy;
    private String Approval;
    private String purchaseNo;
    private String purchaseDate;
    private String purchaseOrderValue;
    private String preparedBy;
    private String refNo;
    private String refDate;
    private String remark;
    private String isStatus;
    private String isChecked;
    private String vSNo;
    private String currentUser;
    private String orderQty;
    private String sku;
    private String medName;
    private String reqDate;
    private String rate;
    private String amount;
    private String approvalRemark;
    private String supplier;
    private String currency;
    private String quotationNo;
    private String indCatName;
    private String indDate;
    private String indNo;
    private String wtaCode;
    private String IsView;
    private String supplierEMail;

    public GrdPendingPO_Display_Mdl() {
    }

    public String getIsView() {
        return IsView;
    }

    public void setIsView(String IsView) {
        this.IsView = IsView;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public void setApprovedBy(String ApprovedBy) {
        this.ApprovedBy = ApprovedBy;
    }

    public String getApproval() {
        return Approval;
    }

    public void setApproval(String Approval) {
        this.Approval = Approval;
    }

    public String getPurchaseNo() {
        return purchaseNo;
    }

    public void setPurchaseNo(String purchaseNo) {
        this.purchaseNo = purchaseNo;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
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

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getvSNo() {
        return vSNo;
    }

    public void setvSNo(String vSNo) {
        this.vSNo = vSNo;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMedName() {
        return medName;
    }

    public void setMedName(String medName) {
        this.medName = medName;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getApprovalRemark() {
        return approvalRemark;
    }

    public void setApprovalRemark(String approvalRemark) {
        this.approvalRemark = approvalRemark;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public String getIndCatName() {
        return indCatName;
    }

    public void setIndCatName(String indCatName) {
        this.indCatName = indCatName;
    }

    public String getIndDate() {
        return indDate;
    }

    public void setIndDate(String indDate) {
        this.indDate = indDate;
    }

    public String getIndNo() {
        return indNo;
    }

    public void setIndNo(String indNo) {
        this.indNo = indNo;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getWtaCode() {
        return wtaCode;
    }

    public void setWtaCode(String wtaCode) {
        this.wtaCode = wtaCode;
    }

    public String getPurchaseOrderValue() {
        return purchaseOrderValue;
    }

    public void setPurchaseOrderValue(String purchaseOrderValue) {
        this.purchaseOrderValue = purchaseOrderValue;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getSupplierEMail() {
        return supplierEMail;
    }

    public void setSupplierEMail(String supplierEMail) {
        this.supplierEMail = supplierEMail;
    }

}
