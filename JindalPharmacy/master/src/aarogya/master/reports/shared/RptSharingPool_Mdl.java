package aarogya.master.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RptSharingPool_Mdl implements Serializable, IsSerializable {

    private String poolName;
    private String consultantName;
    private String poolConfigName;
    private String amount;

    public RptSharingPool_Mdl() {
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getPoolConfigName() {
        return poolConfigName;
    }

    public void setPoolConfigName(String poolConfigName) {
        this.poolConfigName = poolConfigName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
