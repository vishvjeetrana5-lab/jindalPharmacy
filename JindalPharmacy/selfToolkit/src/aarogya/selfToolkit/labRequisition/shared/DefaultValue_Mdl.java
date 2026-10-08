package aarogya.selfToolkit.labRequisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTag, voucherText;
    private String dtVoucher;
    private String currDate;
    private String currTime;
    private String departmentByTag, departmentByText;
    private String departmentToTag, departmentToText;
    private String requestedByTag, requestedByText;
    private String defaultSiteCode, defaultSiteName;

    public String getDepartmentByTag() {
        return departmentByTag;
    }

    public void setDepartmentByTag(String departmentByTag) {
        this.departmentByTag = departmentByTag;
    }

    public String getDepartmentByText() {
        return departmentByText;
    }

    public void setDepartmentByText(String departmentByText) {
        this.departmentByText = departmentByText;
    }

    public String getRequestedByTag() {
        return requestedByTag;
    }

    public void setRequestedByTag(String requestedByTag) {
        this.requestedByTag = requestedByTag;
    }

    public String getRequestedByText() {
        return requestedByText;
    }

    public void setRequestedByText(String requestedByText) {
        this.requestedByText = requestedByText;
    }

    public DefaultValue_Mdl() {
    }

    public String getVoucherTag() {
        return voucherTag;
    }

    public void setVoucherTag(String voucherTag) {
        this.voucherTag = voucherTag;
    }

    public String getVoucherText() {
        return voucherText;
    }

    public void setVoucherText(String voucherText) {
        this.voucherText = voucherText;
    }

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public String getCurrTime() {
        return currTime;
    }

    public void setCurrTime(String currTime) {
        this.currTime = currTime;
    }

    public String getDepartmentToTag() {
        return departmentToTag;
    }

    public void setDepartmentToTag(String departmentToTag) {
        this.departmentToTag = departmentToTag;
    }

    public String getDepartmentToText() {
        return departmentToText;
    }

    public void setDepartmentToText(String departmentToText) {
        this.departmentToText = departmentToText;
    }

    public String getDefaultSiteCode() {
        return defaultSiteCode;
    }

    public void setDefaultSiteCode(String defaultSiteCode) {
        this.defaultSiteCode = defaultSiteCode;
    }

    public String getDefaultSiteName() {
        return defaultSiteName;
    }

    public void setDefaultSiteName(String defaultSiteName) {
        this.defaultSiteName = defaultSiteName;
    }

}
