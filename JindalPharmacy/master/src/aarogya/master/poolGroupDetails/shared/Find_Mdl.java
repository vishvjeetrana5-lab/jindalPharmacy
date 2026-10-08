package aarogya.master.poolGroupDetails.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class Find_Mdl extends KeyField implements Serializable, IsSerializable {

    private String fromDate;
    private String toDate;
    private String poolGroupName;

    public Find_Mdl() {
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getPoolGroupName() {
        return poolGroupName;
    }

    public void setPoolGroupName(String poolGroupName) {
        this.poolGroupName = poolGroupName;
    }

}
