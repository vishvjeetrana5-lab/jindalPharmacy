package aarogya.inventory.purchaseOrder.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHQuotationNo_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
    sharedKeyFieldCode = quotationDocId AS code
    sharedKeyFieldName = quotationRecId AS name
     */
    private String quotationDate;
    private String supplierRefDate;
    private String supplierRefNo;
    private String enquiryRefNo;
    private String enquiryDocId;
    private String guiConfigCode;
    private String quotationValidUpto;

    public TxtHQuotationNo_Mdl() {
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getSupplierRefNo() {
        return supplierRefNo;
    }

    public void setSupplierRefNo(String supplierRefNo) {
        this.supplierRefNo = supplierRefNo;
    }

    public String getSupplierRefDate() {
        return supplierRefDate;
    }

    public void setSupplierRefDate(String supplierRefDate) {
        this.supplierRefDate = supplierRefDate;
    }

    public String getEnquiryRefNo() {
        return enquiryRefNo;
    }

    public void setEnquiryRefNo(String enquiryRefNo) {
        this.enquiryRefNo = enquiryRefNo;
    }

    public String getEnquiryDocId() {
        return enquiryDocId;
    }

    public void setEnquiryDocId(String enquiryDocId) {
        this.enquiryDocId = enquiryDocId;
    }

    public String getGuiConfigCode() {
        return guiConfigCode;
    }

    public void setGuiConfigCode(String guiConfigCode) {
        this.guiConfigCode = guiConfigCode;
    }

    public String getQuotationValidUpto() {
        return quotationValidUpto;
    }

    public void setQuotationValidUpto(String quotationValidUpto) {
        this.quotationValidUpto = quotationValidUpto;
    }

    
}
