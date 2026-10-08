package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;

public class TpaSettlementReceipt_Mdl extends KeyField implements IsSerializable {

    private RecId recIdReceipt = new RecId();
    private String billNo;
    private String voucherPrefix;
    private String billDate;
    private String billType;
    private String billAmount;
    private String BusinessUnit;
    private String D365_ReceiptNo;
    private String D365_ReceiptDate;
    private String Amount;
    private String instrumentType;
    private String intrumentNo;
    private String D365_BankOrCashAcCode;
    private String D365_Status;
    private String D365_Timestamp;
    private String dmReceiptDocid;
    private String dmTimestamp;
    private String dmstatus;

    public TpaSettlementReceipt_Mdl() {
    }

    public RecId getRecIdReceipt() {
        return recIdReceipt;
    }

    public void setRecIdReceipt(RecId recIdReceipt) {
        this.recIdReceipt = recIdReceipt;
    }

    public String getBillNo() {
        return billNo;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getBusinessUnit() {
        return BusinessUnit;
    }

    public void setBusinessUnit(String BusinessUnit) {
        this.BusinessUnit = BusinessUnit;
    }

    public String getD365_ReceiptNo() {
        return D365_ReceiptNo;
    }

    public void setD365_ReceiptNo(String D365_ReceiptNo) {
        this.D365_ReceiptNo = D365_ReceiptNo;
    }

    public String getD365_ReceiptDate() {
        return D365_ReceiptDate;
    }

    public void setD365_ReceiptDate(String D365_ReceiptDate) {
        this.D365_ReceiptDate = D365_ReceiptDate;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getIntrumentNo() {
        return intrumentNo;
    }

    public void setIntrumentNo(String intrumentNo) {
        this.intrumentNo = intrumentNo;
    }

    public String getD365_BankOrCashAcCode() {
        return D365_BankOrCashAcCode;
    }

    public void setD365_BankOrCashAcCode(String D365_BankOrCashAcCode) {
        this.D365_BankOrCashAcCode = D365_BankOrCashAcCode;
    }

    public String getD365_Status() {
        return D365_Status;
    }

    public void setD365_Status(String D365_Status) {
        this.D365_Status = D365_Status;
    }

    public String getD365_Timestamp() {
        return D365_Timestamp;
    }

    public void setD365_Timestamp(String D365_Timestamp) {
        this.D365_Timestamp = D365_Timestamp;
    }

    public String getDmReceiptDocid() {
        return dmReceiptDocid;
    }

    public void setDmReceiptDocid(String dmReceiptDocid) {
        this.dmReceiptDocid = dmReceiptDocid;
    }

    public String getDmTimestamp() {
        return dmTimestamp;
    }

    public void setDmTimestamp(String dmTimestamp) {
        this.dmTimestamp = dmTimestamp;
    }

    public String getDmstatus() {
        return dmstatus;
    }

    public void setDmstatus(String dmstatus) {
        this.dmstatus = dmstatus;
    }

}
