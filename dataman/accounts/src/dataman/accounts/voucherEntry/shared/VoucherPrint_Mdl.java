package dataman.accounts.voucherEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class VoucherPrint_Mdl extends KeyField implements Serializable, IsSerializable {

    private String FromDate;
    private String ToDate;
    private String voucherTypeTag;
    private String voucherTypeText;
    private String accountName;
    private String accountCode;
    private String headerAccountName;
    private String headerAccountCode;
    private String voucherNoCode;
    private String voucherNo;
    private String voucherCategory;
    private String printTitle;

    private boolean paymentVoucherChecked;
    private boolean bankLetterChecked;
    private boolean chqPrintChecked;

    public VoucherPrint_Mdl() {
    }

    public boolean isPaymentVoucherChecked() {
        return paymentVoucherChecked;
    }

    public void setPaymentVoucherChecked(boolean paymentVoucherChecked) {
        this.paymentVoucherChecked = paymentVoucherChecked;
    }

    public boolean isBankLetterChecked() {
        return bankLetterChecked;
    }

    public void setBankLetterChecked(boolean bankLetterChecked) {
        this.bankLetterChecked = bankLetterChecked;
    }

    public boolean isChqPrintChecked() {
        return chqPrintChecked;
    }

    public void setChqPrintChecked(boolean chqPrintChecked) {
        this.chqPrintChecked = chqPrintChecked;
    }

    public String getHeaderAccountName() {
        return headerAccountName;
    }

    public void setHeaderAccountName(String headerAccountName) {
        this.headerAccountName = headerAccountName;
    }

    public String getHeaderAccountCode() {
        return headerAccountCode;
    }

    public void setHeaderAccountCode(String headerAccountCode) {
        this.headerAccountCode = headerAccountCode;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String FromDate) {
        this.FromDate = FromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String ToDate) {
        this.ToDate = ToDate;
    }

    public String getVoucherTypeTag() {
        return voucherTypeTag;
    }

    public void setVoucherTypeTag(String voucherTypeTag) {
        this.voucherTypeTag = voucherTypeTag;
    }

    public String getVoucherTypeText() {
        return voucherTypeText;
    }

    public void setVoucherTypeText(String voucherTypeText) {
        this.voucherTypeText = voucherTypeText;
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

    public String getVoucherNoCode() {
        return voucherNoCode;
    }

    public void setVoucherNoCode(String voucherNoCode) {
        this.voucherNoCode = voucherNoCode;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getPrintTitle() {
        return printTitle;
    }

    public void setPrintTitle(String printTitle) {
        this.printTitle = printTitle;
    }

}
