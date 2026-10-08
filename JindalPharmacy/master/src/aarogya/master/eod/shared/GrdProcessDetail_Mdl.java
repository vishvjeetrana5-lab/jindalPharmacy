package aarogya.master.eod.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdProcessDetail_Mdl extends KeyField implements Serializable, IsSerializable {

    private String eodDate;
    private String remark;
    private String lastRun;
    private String user;

    public GrdProcessDetail_Mdl() {
    }

    public String getEodDate() {
        return eodDate;
    }

    public void setEodDate(String eodDate) {
        this.eodDate = eodDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastRun() {
        return lastRun;
    }

    public void setLastRun(String lastRun) {
        this.lastRun = lastRun;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
