package aarogya.selfToolkit.splApplicableWithOption.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class DefaultValue_Mdl extends KeyField implements Serializable, IsSerializable {

    private String copyRateDate;
    private String siteName;
    private String siteCode;

    public DefaultValue_Mdl() {
    }

    public String getCopyRateDate() {
        return copyRateDate;
    }

    public void setCopyRateDate(String copyRateDate) {
        this.copyRateDate = copyRateDate;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

}
