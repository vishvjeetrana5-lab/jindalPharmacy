package aarogya.master.cardDischarge.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class DefaultValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherText;
    private String voucherTag;
    private String currentDate;
    private String currentTime;
    private String paymentMode;
    private String isDefault;
    private GrdCardPayment_Mdl cardMdl = new GrdCardPayment_Mdl();

    public DefaultValue_Mdl() {
    }

    public String getVoucherText() {
        return voucherText;
    }

    public void setVoucherText(String voucherText) {
        this.voucherText = voucherText;
    }

    public String getVoucherTag() {
        return voucherTag;
    }

    public void setVoucherTag(String voucherTag) {
        this.voucherTag = voucherTag;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public GrdCardPayment_Mdl getCardMdl() {
        return cardMdl;
    }

    public void setCardMdl(GrdCardPayment_Mdl cardMdl) {
        this.cardMdl = cardMdl;
    }

}
