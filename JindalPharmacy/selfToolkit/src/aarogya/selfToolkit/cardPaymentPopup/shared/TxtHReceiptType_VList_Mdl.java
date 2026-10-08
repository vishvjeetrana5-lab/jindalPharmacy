package aarogya.selfToolkit.cardPaymentPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHReceiptType_VList_Mdl extends KeyField implements Serializable, IsSerializable {

    private String v_type;
    private String description;
    private String paymentMode;
    private String isDefault;
    private GrdCardPayment_Mdl cardPaymentDefaultMdl = new GrdCardPayment_Mdl();

    public TxtHReceiptType_VList_Mdl() {
    }

    public String getV_type() {
        return v_type;
    }

    public void setV_type(String v_type) {
        this.v_type = v_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public GrdCardPayment_Mdl getCardPaymentDefaultMdl() {
        return cardPaymentDefaultMdl;
    }

    public void setCardPaymentDefaultMdl(GrdCardPayment_Mdl cardPaymentDefaultMdl) {
        this.cardPaymentDefaultMdl = cardPaymentDefaultMdl;
    }

    public TxtHReceiptType_VList_Mdl clone() {
        TxtHReceiptType_VList_Mdl defaultValues = new TxtHReceiptType_VList_Mdl();

        copy(defaultValues);

        return defaultValues;
    }

    public void copy(TxtHReceiptType_VList_Mdl defaultValues) {
        defaultValues.setDescription(this.getDescription());
        defaultValues.setPaymentMode(this.getPaymentMode());
        defaultValues.setV_type(this.getV_type());
        defaultValues.setCardPaymentDefaultMdl(this.getCardPaymentDefaultMdl().clone());
        defaultValues.setIsDefault(this.getIsDefault());
    }

}
