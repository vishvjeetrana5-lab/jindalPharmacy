package aarogya.pharmacy.quotation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String quotationDate;
    private String quotationType;
    private String vendorName;
    private String vendorQotNo;
    private String vendorQotDate;
    private String enquiryNo;
    private String enquiryDate;
    private String enquiryRefNo;

    public Find_Mdl() {
    }

    public String getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(String quotationDate) {
        this.quotationDate = quotationDate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorQotNo() {
        return vendorQotNo;
    }

    public void setVendorQotNo(String vendorQotNo) {
        this.vendorQotNo = vendorQotNo;
    }

    public String getVendorQotDate() {
        return vendorQotDate;
    }

    public void setVendorQotDate(String vendorQotDate) {
        this.vendorQotDate = vendorQotDate;
    }

    public String getEnquiryNo() {
        return enquiryNo;
    }

    public void setEnquiryNo(String enquiryNo) {
        this.enquiryNo = enquiryNo;
    }

    public String getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(String enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public String getEnquiryRefNo() {
        return enquiryRefNo;
    }

    public void setEnquiryRefNo(String enquiryRefNo) {
        this.enquiryRefNo = enquiryRefNo;
    }

    public String getQuotationType() {
        return quotationType;
    }

    public void setQuotationType(String quotationType) {
        this.quotationType = quotationType;
    }

}
