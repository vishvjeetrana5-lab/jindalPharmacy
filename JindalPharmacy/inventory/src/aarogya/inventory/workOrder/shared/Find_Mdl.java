package aarogya.inventory.workOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String orderNo;
    private String purchaseType;
    private String orderNoDate;
    private String supplierPT;
    private String refNo;
    private String refNoDate;
    private String quotationNo;
    private String enquiryNo;
    private String currency;
    private String indentNo;
    private String approvedBy;
    private String approval;

    public Find_Mdl() {
    }

    public String getIndentNo() {
        return indentNo;
    }

    public void setIndentNo(String indentNo) {
        this.indentNo = indentNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNoDate() {
        return orderNoDate;
    }

    public void setOrderNoDate(String orderNoDate) {
        this.orderNoDate = orderNoDate;
    }

    public String getSupplierPT() {
        return supplierPT;
    }

    public void setSupplierPT(String supplierPT) {
        this.supplierPT = supplierPT;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefNoDate() {
        return refNoDate;
    }

    public void setRefNoDate(String refNoDate) {
        this.refNoDate = refNoDate;
    }

    public String getQuotationNo() {
        return quotationNo;
    }

    public void setQuotationNo(String quotationNo) {
        this.quotationNo = quotationNo;
    }

    public String getEnquiryNo() {
        return enquiryNo;
    }

    public void setEnquiryNo(String enquiryNo) {
        this.enquiryNo = enquiryNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

}
