package aarogya.master.referredMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdClinicDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String address;
    private String address1;
    private String cityTag;
    private String cityText;
    private String phone;
    private String timing;

    public GrdClinicDetail_Mdl() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCityTag() {
        return cityTag;
    }

    public void setCityTag(String cityTag) {
        this.cityTag = cityTag;
    }

    public String getCityText() {
        return cityText;
    }

    public void setCityText(String cityText) {
        this.cityText = cityText;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

}
