package aarogya.master.cardRegistration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String cardNo;
    private String cardName;
    private String dtAllotment;
    private String dtExpiry;
    private String person;
    private String address;
    private String city;

    public Find_Mdl() {
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDtAllotment() {
        return dtAllotment;
    }

    public void setDtAllotment(String dtAllotment) {
        this.dtAllotment = dtAllotment;
    }

    public String getDtExpiry() {
        return dtExpiry;
    }

    public void setDtExpiry(String dtExpiry) {
        this.dtExpiry = dtExpiry;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
