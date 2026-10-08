package aarogya.selfToolkit.cardPaymentPopup.shared;

import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentPopup_Mdl extends KeyField implements Serializable, IsSerializable {

    private String patientName;
    private String receiptNo;
    private String regNoDocid;
    private String regNo;
    private String receiptTypeCode;
    private String receiptTypeName;
    private String receiptTypePM;
    private String amount;
    private String vDate;
    private String UHIDNo;
    private String UHIDDocid;
    private String creditAmount;
    private String authBy;
    private String remark;
    private String favourOf;
    private Boolean isVisible;
    private String status;
    private STEnum.PaymentSource regPaymentSource;
    private STEnum.PaymentSource paymentSource;
    private String ctoCardType, ctoCardNo, ctoRankNo, ctoCardHolderName, ctoRelation, ctoDate;
    private List<TxtHReceiptType_VList_Mdl> TxtHReceiptTypeVList = new ArrayList<TxtHReceiptType_VList_Mdl>();
    private List<GrdCardPayment_Mdl> grdCardPaymentList = new ArrayList<GrdCardPayment_Mdl>();

    public CardPaymentPopup_Mdl() {
    }

    public String getFavourOf() {
        return favourOf;
    }

    public void setFavourOf(String favourOf) {
        this.favourOf = favourOf;
    }
    

    public STEnum.PaymentSource getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(STEnum.PaymentSource paymentSource) {
        this.paymentSource = paymentSource;
    }

    public STEnum.PaymentSource getRegPaymentSource() {
        return regPaymentSource;
    }

    public void setRegPaymentSource(STEnum.PaymentSource regPaymentSource) {
        this.regPaymentSource = regPaymentSource;
    }

    public String getCTOCardType() {
        return ctoCardType;
    }

    public void setCTOCardType(String ctoCardType) {
        this.ctoCardType = ctoCardType;
    }

    public String getCTOCardNo() {
        return ctoCardNo;
    }

    public void setCTOCardNo(String ctoCardNo) {
        this.ctoCardNo = ctoCardNo;
    }

    public String getCTODate() {
        return ctoDate;
    }

    public void setCTODate(String ctoDate) {
        this.ctoDate = ctoDate;
    }

    public String getCTORankNo() {
        return ctoRankNo;
    }

    public void setCTORankNo(String ctoRankNo) {
        this.ctoRankNo = ctoRankNo;
    }

    public String getCTOCardHolderName() {
        return ctoCardHolderName;
    }

    public void setCTOCardHolderName(String ctoCardHolderName) {
        this.ctoCardHolderName = ctoCardHolderName;
    }

    public String getCTORelation() {
        return ctoRelation;
    }

    public void setCTORelation(String ctoRelation) {
        this.ctoRelation = ctoRelation;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getRegNoDocid() {
        return regNoDocid;
    }

    public void setRegNoDocid(String regNoDocid) {
        this.regNoDocid = regNoDocid;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getReceiptTypeCode() {
        return receiptTypeCode;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
    }

    public String getReceiptTypeName() {
        return receiptTypeName;
    }

    public void setReceiptTypeName(String receiptTypeName) {
        this.receiptTypeName = receiptTypeName;
    }

    public String getReceiptTypePM() {
        return receiptTypePM;
    }

    public void setReceiptTypePM(String receiptTypePM) {
        this.receiptTypePM = receiptTypePM;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getvDate() {
        return vDate;
    }

    public void setvDate(String vDate) {
        this.vDate = vDate;
    }

    public String getUHIDNo() {
        return UHIDNo;
    }

    public void setUHIDNo(String UHIDNo) {
        this.UHIDNo = UHIDNo;
    }

    public String getUHIDDocid() {
        return UHIDDocid;
    }

    public void setUHIDDocid(String UHIDDocid) {
        this.UHIDDocid = UHIDDocid;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getAuthBy() {
        return authBy;
    }

    public void setAuthBy(String authBy) {
        this.authBy = authBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
        
    public List<TxtHReceiptType_VList_Mdl> getTxtHReceiptTypeVList() {
        return TxtHReceiptTypeVList;
    }

    public void setTxtHReceiptTypeVList(List<TxtHReceiptType_VList_Mdl> TxtHReceiptTypeVList) {
        this.TxtHReceiptTypeVList = TxtHReceiptTypeVList;
    }

    public List<GrdCardPayment_Mdl> getGrdCardPaymentList() {
        return grdCardPaymentList;
    }

    public void setGrdCardPaymentList(List<GrdCardPayment_Mdl> grdCardPaymentList) {
        this.grdCardPaymentList = grdCardPaymentList;
    }

    public CardPaymentPopup_Mdl clone() {
        CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();

        copy(cardPayment);

        return cardPayment;
    }

    public void copy(CardPaymentPopup_Mdl cardPayment) {
        cardPayment.setPatientName(this.getPatientName());
        cardPayment.setReceiptNo(this.getReceiptNo());
        cardPayment.setRegNo(this.getRegNo());
        cardPayment.setRegNoDocid(this.getRegNoDocid());
        cardPayment.setReceiptTypeCode(this.getReceiptTypeCode());
        cardPayment.setReceiptTypeName(this.getReceiptTypeName());
        cardPayment.setReceiptTypePM(this.getReceiptTypePM());
        cardPayment.setAmount(this.getAmount());
        cardPayment.setvDate(this.getvDate());
        cardPayment.setUHIDNo(this.getUHIDNo());
        cardPayment.setUHIDDocid(this.getUHIDDocid());
        cardPayment.setCreditAmount(this.getCreditAmount());
        cardPayment.setAuthBy(this.getAuthBy());
        cardPayment.setRemark(this.getRemark());
        cardPayment.setPaymentSource(this.getPaymentSource());
        cardPayment.setRegPaymentSource(this.getRegPaymentSource());
        cardPayment.setCTOCardType(this.getCTOCardType());
        cardPayment.setCTOCardNo(this.getCTOCardNo());
        cardPayment.setCTOCardNo(this.getCTODate());
        cardPayment.setCTORankNo(this.getCTORankNo());
        cardPayment.setCTOCardHolderName(this.getCTOCardHolderName());
        cardPayment.setCTORelation(this.getCTORelation());
        cardPayment.getTxtHReceiptTypeVList().addAll(cloneTxtHReceiptTypeVList(this.getTxtHReceiptTypeVList()));
        cardPayment.getGrdCardPaymentList().addAll(cloneGrdCardPaymentList(this.getGrdCardPaymentList()));

    }

    public List<TxtHReceiptType_VList_Mdl> cloneTxtHReceiptTypeVList(List<TxtHReceiptType_VList_Mdl> list) {
        List<TxtHReceiptType_VList_Mdl> txtHReceiptType_VList_Mdl_List = null;

        if (list != null) {
            txtHReceiptType_VList_Mdl_List = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                txtHReceiptType_VList_Mdl_List.add(list.get(i).clone());
            }
        }

        return txtHReceiptType_VList_Mdl_List;
    }

    public List<GrdCardPayment_Mdl> cloneGrdCardPaymentList(List<GrdCardPayment_Mdl> list) {
        List<GrdCardPayment_Mdl> grdCardPayment_MdlList = null;

        if (list != null) {
            grdCardPayment_MdlList = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                grdCardPayment_MdlList.add(list.get(i).clone());
            }
        }

        return grdCardPayment_MdlList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
