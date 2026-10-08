package dataman.accounts.bankReconciliationEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Setting_Mdl implements Serializable, IsSerializable {
    private String statusCode;
    private String statusName;
    private String chkentriesCode;
    private String chkentriesName;
    private String bankName;
    private String bankCode;
    private String fromDate;
    private String toDate;
    private String siteName;
    private String siteCode;

    public Setting_Mdl() {
    }


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getChkentriesCode() {
        return chkentriesCode;
    }

    public void setChkentriesCode(String chkentriesCode) {
        this.chkentriesCode = chkentriesCode;
    }

    public String getChkentriesName() {
        return chkentriesName;
    }

    public void setChkentriesName(String chkentriesName) {
        this.chkentriesName = chkentriesName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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


}
