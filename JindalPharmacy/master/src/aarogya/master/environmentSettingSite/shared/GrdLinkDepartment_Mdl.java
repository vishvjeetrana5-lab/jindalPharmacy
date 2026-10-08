package aarogya.master.environmentSettingSite.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdLinkDepartment_Mdl extends KeyField implements Serializable, IsSerializable {

    private String linkSiteCode;
    private String linkSiteName;

    public GrdLinkDepartment_Mdl() {
    }

    public String getLinkSiteCode() {
        return linkSiteCode;
    }

    public void setLinkSiteCode(String linkSiteCode) {
        this.linkSiteCode = linkSiteCode;
    }

    public String getLinkSiteName() {
        return linkSiteName;
    }

    public void setLinkSiteName(String linkSiteName) {
        this.linkSiteName = linkSiteName;
    }

}
