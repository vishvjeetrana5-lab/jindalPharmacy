package dataman.utility.walkThroughMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdAuthority_Mdl extends KeyField implements Serializable, IsSerializable {

    private String srlNo;
    private String linkedUserCode;
    private String linkedUserName;

    public GrdAuthority_Mdl() {
    }

    public String getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(String srlNo) {
        this.srlNo = srlNo;
    }

    public String getLinkedUserCode() {
        return linkedUserCode;
    }

    public void setLinkedUserCode(String linkedUserCode) {
        this.linkedUserCode = linkedUserCode;
    }

    public String getLinkedUserName() {
        return linkedUserName;
    }

    public void setLinkedUserName(String linkedUserName) {
        this.linkedUserName = linkedUserName;
    }

}
