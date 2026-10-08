package aarogya.master.priceListMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String fieldDataType;
    private String fieldData;
    private String applicableDate;
    private String rateListCode;
    private String itemcode;
    private String fromBfCode;
    private String toBfCode;
    private String fromGsmCode;
    private String toGsmCode;
    private String applicableOn;

    public String getFromBfCode() {
        return fromBfCode;
    }

    public void setFromBfCode(String fromBfCode) {
        this.fromBfCode = fromBfCode;
    }

    public String getToBfCode() {
        return toBfCode;
    }

    public void setToBfCode(String toBfCode) {
        this.toBfCode = toBfCode;
    }

    public String getFromGsmCode() {
        return fromGsmCode;
    }

    public void setFromGsmCode(String fromGsmCode) {
        this.fromGsmCode = fromGsmCode;
    }

    public String getToGsmCode() {
        return toGsmCode;
    }

    public void setToGsmCode(String toGsmCode) {
        this.toGsmCode = toGsmCode;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public SearchValue_Mdl() {
    }

    public String getFieldDataType() {
        return fieldDataType;
    }

    public void setFieldDataType(String fieldDataType) {
        this.fieldDataType = fieldDataType;
    }

    public String getFieldData() {
        return fieldData;
    }

    public void setFieldData(String fieldData) {
        this.fieldData = fieldData;
    }

    public String getApplicableDate() {
        return applicableDate;
    }

    public void setApplicableDate(String applicableDate) {
        this.applicableDate = applicableDate;
    }

    public String getRateListCode() {
        return rateListCode;
    }

    public void setRateListCode(String rateListCode) {
        this.rateListCode = rateListCode;
    }

    public String getApplicableOn() {
        return applicableOn;
    }

    public void setApplicableOn(String applicableOn) {
        this.applicableOn = applicableOn;
    }

}
