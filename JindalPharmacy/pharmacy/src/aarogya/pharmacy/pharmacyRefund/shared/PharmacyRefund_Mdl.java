package aarogya.pharmacy.pharmacyRefund.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class PharmacyRefund_Mdl extends KeyField implements Serializable, IsSerializable {

    private RecId recIdRefund = new RecId();
    private String voucherDate;
    private String customerTypeCode;
    private String customerTypeName;
    private String lmCode;
//    private String uniqueId;
    private String displayName;
    private String manualCode;
    private String idTypeCode;
    private String idTypeName;
    private String idNo;
    private String address;
    private String cityCode;
    private String cityName;
    private String mobile;
    private String subCode;
    private String regDocId;
    private String amount;
    private String pharmacyBalance;
    private String regBalance;
    private String entryBalance;
    private String remark;
    private String isCancel;
    private String recVDescription;
    private String recVType;
    private String recAttachPaymentMode;
    private ModifyReason_Mdl modifyReason = new ModifyReason_Mdl();
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();

    public PharmacyRefund_Mdl() {
    }

    public RecId getRecIdRefund() {
        return recIdRefund;
    }

    public void setRecIdRefund(RecId recIdRefund) {
        this.recIdRefund = recIdRefund;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
    }

//    public String getUniqueId() {
//        return uniqueId;
//    }
//
//    public void setUniqueId(String uniqueId) {
//        this.uniqueId = uniqueId;
//    }
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getManualCode() {
        return manualCode;
    }

    public void setManualCode(String manualCode) {
        this.manualCode = manualCode;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPharmacyBalance() {
        return pharmacyBalance;
    }

    public void setPharmacyBalance(String pharmacyBalance) {
        this.pharmacyBalance = pharmacyBalance;
    }

    public String getRegBalance() {
        return regBalance;
    }

    public void setRegBalance(String regBalance) {
        this.regBalance = regBalance;
    }

    public String getEntryBalance() {
        return entryBalance;
    }

    public void setEntryBalance(String entryBalance) {
        this.entryBalance = entryBalance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(String isCancel) {
        this.isCancel = isCancel;
    }

    public String getRecVDescription() {
        return recVDescription;
    }

    public void setRecVDescription(String recVDescription) {
        this.recVDescription = recVDescription;
    }

    public String getRecVType() {
        return recVType;
    }

    public void setRecVType(String recVType) {
        this.recVType = recVType;
    }

    public String getRecAttachPaymentMode() {
        return recAttachPaymentMode;
    }

    public void setRecAttachPaymentMode(String recAttachPaymentMode) {
        this.recAttachPaymentMode = recAttachPaymentMode;
    }

    public ModifyReason_Mdl getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(ModifyReason_Mdl modifyReason) {
        this.modifyReason = modifyReason;
    }

    public CardPaymentPopup_Mdl getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(CardPaymentPopup_Mdl cardPayment) {
        this.cardPayment = cardPayment;
    }

}
