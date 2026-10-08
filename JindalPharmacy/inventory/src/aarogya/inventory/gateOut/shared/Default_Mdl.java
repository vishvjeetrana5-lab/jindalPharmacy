package aarogya.inventory.gateOut.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Default_Mdl implements Serializable, IsSerializable {

    private String vTypeCode;
    private String vTypeName;
    private String date;
    private String time;

    public Default_Mdl() {
    }

    public String getvTypeCode() {
        return vTypeCode;
    }

    public void setvTypeCode(String vTypeCode) {
        this.vTypeCode = vTypeCode;
    }

    public String getvTypeName() {
        return vTypeName;
    }

    public void setvTypeName(String vTypeName) {
        this.vTypeName = vTypeName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
