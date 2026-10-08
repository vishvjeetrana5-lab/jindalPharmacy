package aarogya.master.additionalUserPermission.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GridPaymentMode_Mdl extends KeyField implements Serializable, IsSerializable {

    private String userName;
    private String IsDefaultTag;
    private String IsDefaultText;
    private String voucherTypeTag;
    private String voucherTypeText;
    private String paymentTypeText;
    private String paymentTypeTag;

    public GridPaymentMode_Mdl() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIsDefaultTag() {
        return IsDefaultTag;
    }

    public void setIsDefaultTag(String IsDefaultTag) {
        this.IsDefaultTag = IsDefaultTag;
    }

    public String getIsDefaultText() {
        return IsDefaultText;
    }

    public void setIsDefaultText(String IsDefaultText) {
        this.IsDefaultText = IsDefaultText;
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

    public String getPaymentTypeText() {
        return paymentTypeText;
    }

    public void setPaymentTypeText(String paymentTypeText) {
        this.paymentTypeText = paymentTypeText;
    }

    public String getPaymentTypeTag() {
        return paymentTypeTag;
    }

    public void setPaymentTypeTag(String paymentTypeTag) {
        this.paymentTypeTag = paymentTypeTag;
    }

}
