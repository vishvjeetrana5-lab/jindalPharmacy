package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RptConsultantSharingComputation_Detail_Mdl implements Serializable, IsSerializable {

    private String keyName,regDocId, serviceCode, roomCategoryCode, serviceName, serviceAmount,poolGroupCode,poolGroupName,poolShairAmount;

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(String serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public String getRoomCategoryCode() {
        return roomCategoryCode;
    }

    public void setRoomCategoryCode(String roomCategoryCode) {
        this.roomCategoryCode = roomCategoryCode;
    }

    public String getRegDocId() {
        return regDocId;
    }

    public void setRegDocId(String regDocId) {
        this.regDocId = regDocId;
    }

    public String getPoolGroupCode() {
        return poolGroupCode;
    }

    public void setPoolGroupCode(String poolGroupCode) {
        this.poolGroupCode = poolGroupCode;
    }

    public String getPoolGroupName() {
        return poolGroupName;
    }

    public void setPoolGroupName(String poolGroupName) {
        this.poolGroupName = poolGroupName;
    }

    public String getPoolShairAmount() {
        return poolShairAmount;
    }

    public void setPoolShairAmount(String poolShairAmount) {
        this.poolShairAmount = poolShairAmount;
    }

}
