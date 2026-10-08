package aarogya.selfToolkit.iplApplicableWithOption.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class Applicable_Mdl implements Serializable, IsSerializable {

    private String dateApplicable;
    private String siteCode;

    public Applicable_Mdl() {
    }

    public String getDateApplicable() {
        return dateApplicable;
    }

    public void setDateApplicable(String dateApplicable) {
        this.dateApplicable = dateApplicable;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}
