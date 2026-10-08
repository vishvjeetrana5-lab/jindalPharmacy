package dataman.accounts.bankLetterCreation.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBankLetterCreation_Mdl extends KeyField implements Serializable, IsSerializable {

    private String payDate;
    private String module;
    private String supplier;
    private String amtPaid;
    private String orgAmt;
    private String tds;
    private String payMode;
    private String bank;
    private String bankCode;
    private String chqDate;
    private String chqNo;
    private String remark;
    private String approvedBy;
    private String supplierCode;
    private String type;
    private String cardTypeCode;
    private boolean check;

    private String supplieraccountNo, supplierbankName, supplierbranch, supplierifscCode;

    public GrdBankLetterCreation_Mdl() {
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getAmtPaid() {
        return amtPaid;
    }

    public void setAmtPaid(String amtPaid) {
        this.amtPaid = amtPaid;
    }

    public String getOrgAmt() {
        return orgAmt;
    }

    public void setOrgAmt(String orgAmt) {
        this.orgAmt = orgAmt;
    }

    public String getTds() {
        return tds;
    }

    public void setTds(String tds) {
        this.tds = tds;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

    public String getChqNo() {
        return chqNo;
    }

    public void setChqNo(String chqNo) {
        this.chqNo = chqNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardTypeCode() {
        return cardTypeCode;
    }

    public void setCardTypeCode(String cardTypeCode) {
        this.cardTypeCode = cardTypeCode;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSupplieraccountNo() {
        return supplieraccountNo;
    }

    public void setSupplieraccountNo(String supplieraccountNo) {
        this.supplieraccountNo = supplieraccountNo;
    }

    public String getSupplierbankName() {
        return supplierbankName;
    }

    public void setSupplierbankName(String supplierbankName) {
        this.supplierbankName = supplierbankName;
    }

    public String getSupplierbranch() {
        return supplierbranch;
    }

    public void setSupplierbranch(String supplierbranch) {
        this.supplierbranch = supplierbranch;
    }

    public String getSupplierifscCode() {
        return supplierifscCode;
    }

    public void setSupplierifscCode(String supplierifscCode) {
        this.supplierifscCode = supplierifscCode;
    }

}
