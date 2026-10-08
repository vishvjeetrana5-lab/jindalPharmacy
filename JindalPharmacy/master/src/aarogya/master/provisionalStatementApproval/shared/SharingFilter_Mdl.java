package aarogya.master.provisionalStatementApproval.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SharingFilter_Mdl implements Serializable, IsSerializable {

    private String monthTag;
    private String monthText;
    private String financialYear;
    private String voucherPrefix;
    private String siteName;
    private String siteCode;
    private SharingStatus status = SharingStatus.PENDING;

    public SharingFilter_Mdl() {
    }

    public String getMonthTag() {
        return monthTag;
    }

    public void setMonthTag(String monthTag) {
        this.monthTag = monthTag;
    }

    public String getMonthText() {
        return monthText;
    }

    public void setMonthText(String monthText) {
        this.monthText = monthText;
    }

    public String getFinancialYear() {
        return financialYear;
    }

    public void setFinancialYear(String financialYear) {
        this.financialYear = financialYear;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public SharingStatus getStatus() {
        return status;
    }

    public void setStatus(SharingStatus status) {
        this.status = status;
    }
}
