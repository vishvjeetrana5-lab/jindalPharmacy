package aarogya.selfToolkit.appointmentSearch.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String fromDate, toDate;
    private String voucherCategory;
    private String consultantTag, consultantText, consultantFromTime;
    private String isPending;
    private String fieldName;
    private String fieldNameValue;
    private boolean locationWise = false;

    public SearchValue_Mdl() {
    }

    public String getIsPending() {
        return isPending;
    }

    public void setIsPending(String isPending) {
        this.isPending = isPending;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getConsultantText() {
        return consultantText;
    }

    public void setConsultantText(String consultantText) {
        this.consultantText = consultantText;
    }

    public String getConsultantTag() {
        return consultantTag;
    }

    public void setConsultantTag(String consultantTag) {
        this.consultantTag = consultantTag;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate.trim();
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate.trim();
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldNameValue() {
        return fieldNameValue;
    }

    public void setFieldNameValue(String fieldNameValue) {
        this.fieldNameValue = fieldNameValue;
    }

    public String getConsultantFromTime() {
        return consultantFromTime;
    }

    public void setConsultantFromTime(String consultantFromTime) {
        this.consultantFromTime = consultantFromTime;
    }

    public boolean isLocationWise() {
        return locationWise;
    }

    public void setLocationWise(boolean locationWise) {
        this.locationWise = locationWise;
    }
}
