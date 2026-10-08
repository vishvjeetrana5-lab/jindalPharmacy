package aarogya.pharmacy.pharmacyReceipt.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DefaultValue_Mdl implements Serializable, IsSerializable {

    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private String currentDate;

    public DefaultValue_Mdl() {
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
