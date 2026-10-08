package aarogya.selfToolkit.walletSystem.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class PersonDetail_Mdl implements Serializable, IsSerializable {

    private String recIdUHID;
    private String docIdUHID;
    private String person;
    private String initial;
    private String address;
    private String dateDOB;
    private String dateDOA;
    private String mobile;
    private String email;
    private String cityName;
    private String cityCode;

    public PersonDetail_Mdl() {
    }

    public String getDateDOA() {
        return dateDOA;
    }

    public void setDateDOA(String dateDOA) {
        this.dateDOA = dateDOA;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getRecIdUHID() {
        return recIdUHID;
    }

    public void setRecIdUHID(String recIdUHID) {
        this.recIdUHID = recIdUHID;
    }

    public String getDocIdUHID() {
        return docIdUHID;
    }

    public void setDocIdUHID(String docIdUHID) {
        this.docIdUHID = docIdUHID;
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

    public String getDateDOB() {
        return dateDOB;
    }

    public void setDateDOB(String dateDOB) {
        this.dateDOB = dateDOB;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

}
