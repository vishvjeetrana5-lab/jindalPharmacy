package aarogya.master.receiptBookMapping.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdReceiptMapping_Mdl extends KeyField implements Serializable, IsSerializable {

    private String receiptVoucherCode;
    private String receiptVoucherName;
    private String refundVoucherCode;
    private String refundVoucherName;
    private String isDefaultCode;
    private String isDefaultName;

    public GrdReceiptMapping_Mdl() {
    }

    public String getReceiptVoucherCode() {
        return receiptVoucherCode;
    }

    public void setReceiptVoucherCode(String receiptVoucherCode) {
        this.receiptVoucherCode = receiptVoucherCode;
    }

    public String getReceiptVoucherName() {
        return receiptVoucherName;
    }

    public void setReceiptVoucherName(String receiptVoucherName) {
        this.receiptVoucherName = receiptVoucherName;
    }

    public String getIsDefaultCode() {
        return isDefaultCode;
    }

    public void setIsDefaultCode(String isDefaultCode) {
        this.isDefaultCode = isDefaultCode;
    }

    public String getIsDefaultName() {
        return isDefaultName;
    }

    public void setIsDefaultName(String isDefaultName) {
        this.isDefaultName = isDefaultName;
    }

    public String getRefundVoucherCode() {
        return refundVoucherCode;
    }

    public void setRefundVoucherCode(String refundVoucherCode) {
        this.refundVoucherCode = refundVoucherCode;
    }

    public String getRefundVoucherName() {
        return refundVoucherName;
    }

    public void setRefundVoucherName(String refundVoucherName) {
        this.refundVoucherName = refundVoucherName;
    }

}
