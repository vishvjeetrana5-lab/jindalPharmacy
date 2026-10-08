package aarogya.selfToolkit.D365_To_His_DataSync.shared;

public class PharmacySaleBillDetail_Mdl {

    private String lineNumber;
    private String d365ItemCode;
    private String itemName;
    private String batchNo;
    private String quantity;
    private String d365UnitCode;
    private String unitName;
    private String oh_Amt_Gross,oh_Amt_Discount,oh_Amt_Taxable,oh_Amt_CGST,oh_Amt_SGST,oh_Amt_UGST,oh_Amt_IGST,oh_Amt_Net;
 
    public PharmacySaleBillDetail_Mdl() {
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getD365ItemCode() {
        return d365ItemCode;
    }

    public void setD365ItemCode(String d365ItemCode) {
        this.d365ItemCode = d365ItemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getD365UnitCode() {
        return d365UnitCode;
    }

    public void setD365UnitCode(String d365UnitCode) {
        this.d365UnitCode = d365UnitCode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getOh_Amt_Gross() {
        return oh_Amt_Gross;
    }

    public void setOh_Amt_Gross(String oh_Amt_Gross) {
        this.oh_Amt_Gross = oh_Amt_Gross;
    }

    public String getOh_Amt_Discount() {
        return oh_Amt_Discount;
    }

    public void setOh_Amt_Discount(String oh_Amt_Discount) {
        this.oh_Amt_Discount = oh_Amt_Discount;
    }

    public String getOh_Amt_Taxable() {
        return oh_Amt_Taxable;
    }

    public void setOh_Amt_Taxable(String oh_Amt_Taxable) {
        this.oh_Amt_Taxable = oh_Amt_Taxable;
    }

    public String getOh_Amt_CGST() {
        return oh_Amt_CGST;
    }

    public void setOh_Amt_CGST(String oh_Amt_CGST) {
        this.oh_Amt_CGST = oh_Amt_CGST;
    }

    public String getOh_Amt_SGST() {
        return oh_Amt_SGST;
    }

    public void setOh_Amt_SGST(String oh_Amt_SGST) {
        this.oh_Amt_SGST = oh_Amt_SGST;
    }

    public String getOh_Amt_UGST() {
        return oh_Amt_UGST;
    }

    public void setOh_Amt_UGST(String oh_Amt_UGST) {
        this.oh_Amt_UGST = oh_Amt_UGST;
    }

    public String getOh_Amt_IGST() {
        return oh_Amt_IGST;
    }

    public void setOh_Amt_IGST(String oh_Amt_IGST) {
        this.oh_Amt_IGST = oh_Amt_IGST;
    }

    public String getOh_Amt_Net() {
        return oh_Amt_Net;
    }

    public void setOh_Amt_Net(String oh_Amt_Net) {
        this.oh_Amt_Net = oh_Amt_Net;
    }

}
