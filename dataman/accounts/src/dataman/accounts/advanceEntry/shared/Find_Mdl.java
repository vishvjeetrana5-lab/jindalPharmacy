package dataman.accounts.advanceEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String reqDate;
    private String reqNo;
    private String poNoTag;
    private String poNoText;
    private String paidAmt;
    private String advanceAmt;
    private String tdsDeduction;
    private String payMode;
    private String bankName;
    private String advanceReqAmt;
    private String approvedBy;
    private String supplierName;

    public Find_Mdl() {

    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }



    public String getPoNoTag() {
        return poNoTag;
    }

    public void setPoNoTag(String poNoTag) {
        this.poNoTag = poNoTag;
    }

    public String getPoNoText() {
        return poNoText;
    }

    public void setPoNoText(String poNoText) {
        this.poNoText = poNoText;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getAdvanceReqAmt() {
        return advanceReqAmt;
    }

    public void setAdvanceReqAmt(String advanceReqAmt) {
        this.advanceReqAmt = advanceReqAmt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getAdvanceAmt() {
        return advanceAmt;
    }

    public void setAdvanceAmt(String advanceAmt) {
        this.advanceAmt = advanceAmt;
    }

    public String getTdsDeduction() {
        return tdsDeduction;
    }

    public void setTdsDeduction(String tdsDeduction) {
        this.tdsDeduction = tdsDeduction;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

 

}
