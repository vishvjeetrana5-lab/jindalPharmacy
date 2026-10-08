package aarogya.master.poolEarningDeductionEntry.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PoolEarningDeductionEntry_Mdl extends KeyField implements Serializable, IsSerializable {

    private String salName;
    private String salCode;
    private String poolGrpName;
    private String poolGrpCode;
//    private String empName;
//    private String empCode;
    private String fromDate;
    private String toDate;

    private String preparedBy;
    private String modifiedBy;

    private List<PoolEarningDeductionEntryGridEdit_Mdl> gateDetails = new ArrayList<PoolEarningDeductionEntryGridEdit_Mdl>();

    public PoolEarningDeductionEntry_Mdl() {
    }

//    public String getPoolGroupName() {
//        return poolGroupName;
//    }
//
//    public void setPoolGroupName(String poolGroupName) {
//        this.poolGroupName = poolGroupName;
//    }
//
//    public String getPoolGroupCode() {
//        return poolGroupCode;
//    }
//
//    public void setPoolGroupCode(String poolGroupCode) {
//        this.poolGroupCode = poolGroupCode;
//    }
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

    public List<PoolEarningDeductionEntryGridEdit_Mdl> getGateDetails() {
        return gateDetails;
    }

    public void setGateDetails(List<PoolEarningDeductionEntryGridEdit_Mdl> gateDetails) {
        this.gateDetails = gateDetails;
    }

    public String getSalName() {
        return salName;
    }

    public void setSalName(String salName) {
        this.salName = salName;
    }

    public String getSalCode() {
        return salCode;
    }

    public void setSalCode(String salCode) {
        this.salCode = salCode;
    }

    public String getPoolGrpName() {
        return poolGrpName;
    }

    public void setPoolGrpName(String poolGrpName) {
        this.poolGrpName = poolGrpName;
    }

    public String getPoolGrpCode() {
        return poolGrpCode;
    }

    public void setPoolGrpCode(String poolGrpCode) {
        this.poolGrpCode = poolGrpCode;
    }

}
