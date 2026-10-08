package aarogya.master.serviceGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class ServiceGroupMaster_Mdl extends KeyField implements Serializable, IsSerializable {

    private String printSerialNo;
    private String isPerPageText;
    private String isPerPageTag;

    public String getPrintSerialNo() {
        return printSerialNo;
    }

    public void setPrintSerialNo(String printSerialNo) {
        this.printSerialNo = printSerialNo;
    }

    public String getIsPerPageText() {
        return isPerPageText;
    }

    public void setIsPerPageText(String isPerPageText) {
        this.isPerPageText = isPerPageText;
    }

    public String getIsPerPageTag() {
        return isPerPageTag;
    }

    public void setIsPerPageTag(String isPerPageTag) {
        this.isPerPageTag = isPerPageTag;
    }

    public ServiceGroupMaster_Mdl() {
    }

}
