package aarogya.master.poolGroupDetails.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PoolGroupDetails_Mdl extends KeyField implements Serializable, IsSerializable {

    private String poolGroupName;
    private String poolGroupCode;
    private String fromDate;
    private String toDate;

    private String preparedBy;
    private String modifiedBy;
    private int total;

    private List<PoolGroupDetailsGridEdit_Mdl> gateDetails = new ArrayList<PoolGroupDetailsGridEdit_Mdl>();

    public PoolGroupDetails_Mdl() {
    }

    public String getPoolGroupName() {
        return poolGroupName;
    }

    public void setPoolGroupName(String poolGroupName) {
        this.poolGroupName = poolGroupName;
    }

    public String getPoolGroupCode() {
        return poolGroupCode;
    }

    public void setPoolGroupCode(String poolGroupCode) {
        this.poolGroupCode = poolGroupCode;
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

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public List<PoolGroupDetailsGridEdit_Mdl> getGateDetails() {
        return gateDetails;
    }

    public void setGateDetails(List<PoolGroupDetailsGridEdit_Mdl> gateDetails) {
        this.gateDetails = gateDetails;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
