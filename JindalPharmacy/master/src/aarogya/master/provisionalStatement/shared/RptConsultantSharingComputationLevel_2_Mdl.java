package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RptConsultantSharingComputationLevel_2_Mdl implements Serializable, IsSerializable {
    
    private String poolName;
    private String poolCode;
    private String poolKey;
    private double totalPoolAmount=0d;

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public double getTotalPoolAmount() {
        return totalPoolAmount;
    }

    public void setTotalPoolAmount(double totalPoolAmount) {
        this.totalPoolAmount = totalPoolAmount;
    }

    public String getPoolKey() {
        return poolKey;
    }

    public void setPoolKey(String poolKey) {
        this.poolKey = poolKey;
    }

    public String getPoolCode() {
        return poolCode;
    }

    public void setPoolCode(String poolCode) {
        this.poolCode = poolCode;
    }

}
