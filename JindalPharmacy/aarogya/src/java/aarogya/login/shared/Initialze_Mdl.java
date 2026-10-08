package aarogya.login.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Initialze_Mdl implements Serializable, IsSerializable {

    Integer[] mobileDigit;

    public Initialze_Mdl() {
    }

    public Integer[] getMobileDigit() {
        return mobileDigit;
    }

    public void setMobileDigit(Integer[] mobileDigit) {
        this.mobileDigit = mobileDigit;
    }
}
