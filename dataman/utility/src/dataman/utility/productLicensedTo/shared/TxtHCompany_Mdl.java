package dataman.utility.productLicensedTo.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class TxtHCompany_Mdl extends KeyField implements Serializable,IsSerializable{

    private String startDt;
    private String endDt;
    private String centralDataPath;

    public TxtHCompany_Mdl() {
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }

    public String getCentralDataPath() {
        return centralDataPath;
    }

    public void setCentralDataPath(String centralDataPath) {
        this.centralDataPath = centralDataPath;
    }

}
