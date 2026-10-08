package aarogya.master.serviceGroupMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String printSerialNo;
    private String isPerPage;

    public String getPrintSerialNo() {
        return printSerialNo;
    }

    public void setPrintSerialNo(String printSerialNo) {
        this.printSerialNo = printSerialNo;
    }

    public Find_Mdl() {
    }

    public String getIsPerPage() {
        return isPerPage;
    }

    public void setIsPerPage(String isPerPage) {
        this.isPerPage = isPerPage;
    }

}
