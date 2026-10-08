package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.Date;

public class RptBankReconciliationPrint_Mdl extends KeyField implements Serializable, IsSerializable {

    private String vNo;
    private Date vDate;
    private String accountName;
    private String favourOf;
    private String chq;
    private Date chqDate;
    private Date clearDate;
    private Double debit;
    private Double credit;

    private String bankName;
    private String reportType;
    private Date fromDate;
    private Date toDate;
    private String totalAmtCredit;
    private String totalAmtDebit;
    private String amtNotReflectedCredit;
    private String amtNotReflectedDebit;
    private String amtReflectedCredit;
    private String amtReflectedDebit;
    private String balAsPerCompany;
    private String balAsPerBank;
    private String openingAsPerBank;

    private String isCleared;

    public RptBankReconciliationPrint_Mdl() {
    }

    public String getvNo() {
        return vNo;
    }

    public void setvNo(String vNo) {
        this.vNo = vNo;
    }

    public Date getvDate() {
        return vDate;
    }

    public void setvDate(Date vDate) {
        this.vDate = vDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public Date getChqDate() {
        return chqDate;
    }

    public void setChqDate(Date chqDate) {
        this.chqDate = chqDate;
    }

    public Date getClearDate() {
        return clearDate;
    }

    public void setClearDate(Date clearDate) {
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

    public String getIsCleared() {
        return isCleared;
    }

    public void setIsCleared(String isCleared) {
        this.isCleared = isCleared;
    }

    public String getTotalAmtCredit() {
        return totalAmtCredit;
    }

    public void setTotalAmtCredit(String totalAmtCredit) {
        this.totalAmtCredit = totalAmtCredit;
    }

    public String getTotalAmtDebit() {
        return totalAmtDebit;
    }

    public void setTotalAmtDebit(String totalAmtDebit) {
        this.totalAmtDebit = totalAmtDebit;
    }

    public String getAmtNotReflectedCredit() {
        return amtNotReflectedCredit;
    }

    public void setAmtNotReflectedCredit(String amtNotReflectedCredit) {
        this.amtNotReflectedCredit = amtNotReflectedCredit;
    }

    public String getAmtNotReflectedDebit() {
        return amtNotReflectedDebit;
    }

    public void setAmtNotReflectedDebit(String amtNotReflectedDebit) {
        this.amtNotReflectedDebit = amtNotReflectedDebit;
    }

    public String getAmtReflectedCredit() {
        return amtReflectedCredit;
    }

    public void setAmtReflectedCredit(String amtReflectedCredit) {
        this.amtReflectedCredit = amtReflectedCredit;
    }

    public String getAmtReflectedDebit() {
        return amtReflectedDebit;
    }

    public void setAmtReflectedDebit(String amtReflectedDebit) {
        this.amtReflectedDebit = amtReflectedDebit;
    }

    public String getBalAsPerCompany() {
        return balAsPerCompany;
    }

    public void setBalAsPerCompany(String balAsPerCompany) {
        this.balAsPerCompany = balAsPerCompany;
    }

    public String getBalAsPerBank() {
        return balAsPerBank;
    }

    public void setBalAsPerBank(String balAsPerBank) {
        this.balAsPerBank = balAsPerBank;
    }

    public String getOpeningAsPerBank() {
        return openingAsPerBank;
    }

    public void setOpeningAsPerBank(String openingAsPerBank) {
        this.openingAsPerBank = openingAsPerBank;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

}
