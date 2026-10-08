package dataman.utility.productLicensedTo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    public Find_Mdl() {
    }
    private String sname;
    private String latestCompCode;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getLatestCompCode() {
        return latestCompCode;
    }

    public void setLatestCompCode(String latestCompCode) {
        this.latestCompCode = latestCompCode;
    }

}
