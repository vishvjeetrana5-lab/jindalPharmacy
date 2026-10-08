package aarogya.master.cardRecharge.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardRecharge_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recId = new RecId();
    private String voucherDate;
    private String voucherTypeCode;
    private String voucherTypeName;
    private String amount, netReceipt;
    private String remark;
    private RegisteredWallet_Mdl cardRegMdl;
    private List<GrdCardPayment_Mdl> lstPayment = new ArrayList<GrdCardPayment_Mdl>();

    public CardRecharge_Mdl() {
    }

    public List<GrdCardPayment_Mdl> getLstPayment() {
        return lstPayment;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNetReceipt() {
        return netReceipt;
    }

    public void setNetReceipt(String netReceipt) {
        this.netReceipt = netReceipt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public RegisteredWallet_Mdl getCardRegMdl() {
        return cardRegMdl;
    }

    public void setCardRegMdl(RegisteredWallet_Mdl cardRegMdl) {
        this.cardRegMdl = cardRegMdl;
    }
}
