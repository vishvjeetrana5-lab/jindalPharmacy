package aarogya.master.D365ImportExport.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class SaleBill_Mdl extends KeyField implements IsSerializable, Serializable {

    private String invoiceNo;
    private String date;
    private String referenceNo;
    private String siteCode;
    private String customerNo;
    private String amount;
    private String dateTimeEntry;
    private String action;
    private String actionDateTime;

    /*-----------------Salebill Line-------------*/
    private String v_SNo;
    private String itemCode;
    private String itemName;
    private String itemType;
    private String unitCode;
    private String quantity;
    private String rate;
    private String oh_amt_Discount;
    private String oh_at_Discount;

    public SaleBill_Mdl() {
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDateTimeEntry() {
        return dateTimeEntry;
    }

    public void setDateTimeEntry(String dateTimeEntry) {
        this.dateTimeEntry = dateTimeEntry;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActionDateTime() {
        return actionDateTime;
    }

    public void setActionDateTime(String actionDateTime) {
        this.actionDateTime = actionDateTime;
    }

    public String getV_SNo() {
        return v_SNo;
    }

    public void setV_SNo(String v_SNo) {
        this.v_SNo = v_SNo;
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

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOh_amt_Discount() {
        return oh_amt_Discount;
    }

    public void setOh_amt_Discount(String oh_amt_Discount) {
        this.oh_amt_Discount = oh_amt_Discount;
    }

    public String getOh_at_Discount() {
        return oh_at_Discount;
    }

    public void setOh_at_Discount(String oh_at_Discount) {
        this.oh_at_Discount = oh_at_Discount;
    }

}
