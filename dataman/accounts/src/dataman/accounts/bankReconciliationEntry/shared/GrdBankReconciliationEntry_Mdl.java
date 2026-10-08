package dataman.accounts.bankReconciliationEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdBankReconciliationEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vNo;
    private String vDate;
    private String accountName;
    private String accountCode;
    private String favourOf;
    private String chq;
    private String chqDate;
    private String clearDate;
    private Double debit;
    private Double credit;
    private String debitTag;
    private String creditTag;
    private String tag;
    private String bank_V_SNo;
    private String remark;
    private String image;
    private String isCleared;
    private String isPosted;

    public GrdBankReconciliationEntry_Mdl() {
    }

    public String getVNo() {
        return vNo;
    }

    public void setVNo(String vNo) {
        this.vNo = vNo;
    }

    public String getVDate() {
        return vDate;
    }

    public void setVDate(String vDate) {
        this.vDate = vDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }

    public String getChq() {
        return chq;
    }

    public void setChq(String chq) {
        this.chq = chq;
    }

    public String getChqDate() {
        return chqDate;
    }

    public void setChqDate(String chqDate) {
        this.chqDate = chqDate;
    }

    public String getClearDate() {
        return clearDate;
    }

    public void setClearDate(String clearDate) {
        this.clearDate = clearDate;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public String getDebitTag() {
        return debitTag;
    }

    public void setDebitTag(String debitTag) {
        this.debitTag = debitTag;
    }

    public String getCreditTag() {
        return creditTag;
    }

    public void setCreditTag(String creditTag) {
        this.creditTag = creditTag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBank_V_SNo() {
        return bank_V_SNo;
    }

    public void setBank_V_SNo(String bank_V_SNo) {
        this.bank_V_SNo = bank_V_SNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsCleared() {
        return isCleared;
    }

    public void setIsCleared(String isCleared) {
        this.isCleared = isCleared;
    }

    public String getIsPosted() {
        return isPosted;
    }

    public void setIsPosted(String isPosted) {
        this.isPosted = isPosted;
    }

}
