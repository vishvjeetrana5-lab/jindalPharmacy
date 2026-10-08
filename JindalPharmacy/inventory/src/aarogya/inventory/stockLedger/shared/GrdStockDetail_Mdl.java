package aarogya.inventory.stockLedger.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdStockDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherDate;
    private String voucherType;
    private String mnuText;
    private String mnuAttachedInModule;
    private String batchNo;
    private String acName;
    private String subName;
    private String recQty;
    private String recQtyDisplay;
    private String isuQty;
    private String isuQtyDisplay;
    private String balance;
    private String balanceDisplay;
    private String remark;
    private String particulars;
    private String propertiesDisplay;
    private String value;
    private String valueDisplay;
    private String balanceValue;
    private String balanceValueDisplay;

    public GrdStockDetail_Mdl() {
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getRecQty() {
        return recQty;
    }

    public void setRecQty(String recQty) {
        this.recQty = recQty;
    }

    public String getRecQtyDisplay() {
        return recQtyDisplay;
    }

    public void setRecQtyDisplay(String recQtyDisplay) {
        this.recQtyDisplay = recQtyDisplay;
    }

    public String getIsuQty() {
        return isuQty;
    }

    public void setIsuQty(String isuQty) {
        this.isuQty = isuQty;
    }

    public String getIsuQtyDisplay() {
        return isuQtyDisplay;
    }

    public void setIsuQtyDisplay(String isuQtyDisplay) {
        this.isuQtyDisplay = isuQtyDisplay;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceDisplay() {
        return balanceDisplay;
    }

    public void setBalanceDisplay(String balanceDisplay) {
        this.balanceDisplay = balanceDisplay;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getPropertiesDisplay() {
        return propertiesDisplay;
    }

    public void setPropertiesDisplay(String propertiesDisplay) {
        this.propertiesDisplay = propertiesDisplay;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getBalanceValue() {
        return balanceValue;
    }

    public void setBalanceValue(String balanceValue) {
        this.balanceValue = balanceValue;
    }

    public String getValueDisplay() {
        return valueDisplay;
    }

    public void setValueDisplay(String valueDisplay) {
        this.valueDisplay = valueDisplay;
    }

    public String getBalanceValueDisplay() {
        return balanceValueDisplay;
    }

    public void setBalanceValueDisplay(String balanceValueDisplay) {
        this.balanceValueDisplay = balanceValueDisplay;
    }

    public String getMnuText() {
        return mnuText;
    }

    public void setMnuText(String mnuText) {
        this.mnuText = mnuText;
    }

    public String getMnuAttachedInModule() {
        return mnuAttachedInModule;
    }

    public void setMnuAttachedInModule(String mnuAttachedInModule) {
        this.mnuAttachedInModule = mnuAttachedInModule;
    }
}
