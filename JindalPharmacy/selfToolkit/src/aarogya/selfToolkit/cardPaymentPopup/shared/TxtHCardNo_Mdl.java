package aarogya.selfToolkit.cardPaymentPopup.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class TxtHCardNo_Mdl implements Serializable, IsSerializable {

    private String cardRegDocId, cardRegNo;
    private String validDate;
    private String linkAccountCode;
    private String linkAccountName;
    private String cardNo;
    private String contactPerson;

    public TxtHCardNo_Mdl() {
    }

    public String getCardRegDocId() {
        return cardRegDocId;
    }

    public void setCardRegDocId(String cardRegDocId) {
        this.cardRegDocId = cardRegDocId;
    }

    public String getCardRegNo() {
        return cardRegNo;
    }

    public void setCardRegNo(String cardRegNo) {
        this.cardRegNo = cardRegNo;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getLinkAccountCode() {
        return linkAccountCode;
    }

    public void setLinkAccountCode(String linkAccountCode) {
        this.linkAccountCode = linkAccountCode;
    }

    public String getLinkAccountName() {
        return linkAccountName;
    }

    public void setLinkAccountName(String linkAccountName) {
        this.linkAccountName = linkAccountName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

}
