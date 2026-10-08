package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHGUITemplate_Mdl extends KeyField implements Serializable, IsSerializable {

    private String appliedSiteText, appliedSiteCode;

    public TxtHGUITemplate_Mdl() {
    }

    public String getAppliedSiteText() {
        return appliedSiteText;
    }

    public void setAppliedSiteText(String appliedSiteText) {
        this.appliedSiteText = appliedSiteText;
    }

    public String getAppliedSiteCode() {
        return appliedSiteCode;
    }

    public void setAppliedSiteCode(String appliedSiteCode) {
        this.appliedSiteCode = appliedSiteCode;
    }

}
