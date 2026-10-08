package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;

public class GrdProvisionalStatement_Mdl extends KeyField implements Serializable, IsSerializable {

   
       private String poolKey, serviceName, unit, poolName, poolAmount, poolGroupCode, 
            consultantCode, consultantName, shairPercent, shairAmount, verification;
       
    public GrdProvisionalStatement_Mdl() {
    }

    public String getPoolKey() {
        return poolKey;
    }

    public void setPoolKey(String poolKey) {
        this.poolKey = poolKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getPoolAmount() {
        return poolAmount;
    }

    public void setPoolAmount(String poolAmount) {
        this.poolAmount = poolAmount;
    }

    public String getPoolGroupCode() {
        return poolGroupCode;
    }

    public void setPoolGroupCode(String poolGroupCode) {
        this.poolGroupCode = poolGroupCode;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getShairPercent() {
        return shairPercent;
    }

    public void setShairPercent(String shairPercent) {
        this.shairPercent = shairPercent;
    }

    public String getShairAmount() {
        return shairAmount;
    }

    public void setShairAmount(String shairAmount) {
        this.shairAmount = shairAmount;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

   
}
