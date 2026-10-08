package aarogya.selfToolkit.patientRequisition.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTag, voucherText;
    private String departmentTag, departmentText;
    private String departmentByTag, departmentByText;
    private String requestedByTag, requestedByText;
    private String dtVoucher;
    private String siteCodeToTag;
    private String siteCodeToText;

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

    public String getDepartmentTag() {
        return departmentTag;
    }

    public void setDepartmentTag(String departmentTag) {
        this.departmentTag = departmentTag;
    }

    public String getDepartmentText() {
        return departmentText;
    }

    public void setDepartmentText(String departmentText) {
        this.departmentText = departmentText;
    }

    public String getDtVoucher() {
        return dtVoucher;
    }

    public void setDtVoucher(String dtVoucher) {
        this.dtVoucher = dtVoucher;
    }

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

    public String getSiteCodeToTag() {
        return siteCodeToTag;
    }

    public void setSiteCodeToTag(String siteCodeToTag) {
        this.siteCodeToTag = siteCodeToTag;
    }

    public String getSiteCodeToText() {
        return siteCodeToText;
    }

    public void setSiteCodeToText(String siteCodeToText) {
        this.siteCodeToText = siteCodeToText;
    }

}
