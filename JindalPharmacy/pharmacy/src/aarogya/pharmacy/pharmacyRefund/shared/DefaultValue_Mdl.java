package aarogya.pharmacy.pharmacyRefund.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    ///////////////card Type Popup info///////////////////////////////////////
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();

    //////////////////////////////////////////////////////
    private String currentDate;
    private String ctpSubCode;

    public DefaultValue_Mdl() {
    }

    public String getCtpSubCode() {
        return ctpSubCode;
    }

    public void setCtpSubCode(String ctpSubCode) {
        this.ctpSubCode = ctpSubCode;
    }

    public CardPaymentPopup_Mdl getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(CardPaymentPopup_Mdl cardPayment) {
        this.cardPayment = cardPayment;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
