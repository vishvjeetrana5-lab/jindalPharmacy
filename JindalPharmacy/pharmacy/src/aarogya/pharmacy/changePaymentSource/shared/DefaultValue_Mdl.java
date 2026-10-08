package aarogya.pharmacy.changePaymentSource.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String voucherTypeName;
    private String currentDate;

    public DefaultValue_Mdl() {
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
