package aarogya.master.provisionalStatement.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class RptConsultantSharingComputationLevel_1_Mdl implements Serializable, IsSerializable {

    private String keyName, serviceCode, roomCategoryCode, regDocId, serviceName, serviceAmount, headerConsultantAmount,
            headerConsultantpoolName, headerReferredAmount, headerReferredpoolName,
            LineConsultantAmount, LineConsultantpoolName, PerformingReferredPAmount, PerformingReferredPoolName;

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

    public String getHeaderConsultantAmount() {
        return headerConsultantAmount;
    }

    public void setHeaderConsultantAmount(String headerConsultantAmount) {
        this.headerConsultantAmount = headerConsultantAmount;
    }

    public String getHeaderConsultantpoolName() {
        return headerConsultantpoolName;
    }

    public void setHeaderConsultantpoolName(String headerConsultantpoolName) {
        this.headerConsultantpoolName = headerConsultantpoolName;
    }

    public String getHeaderReferredAmount() {
        return headerReferredAmount;
    }

    public void setHeaderReferredAmount(String headerReferredAmount) {
        this.headerReferredAmount = headerReferredAmount;
    }

    public String getHeaderReferredpoolName() {
        return headerReferredpoolName;
    }

    public void setHeaderReferredpoolName(String headerReferredpoolName) {
        this.headerReferredpoolName = headerReferredpoolName;
    }

    public String getLineConsultantAmount() {
        return LineConsultantAmount;
    }

    public void setLineConsultantAmount(String LineConsultantAmount) {
        this.LineConsultantAmount = LineConsultantAmount;
    }

    public String getLineConsultantpoolName() {
        return LineConsultantpoolName;
    }

    public void setLineConsultantpoolName(String LineConsultantpoolName) {
        this.LineConsultantpoolName = LineConsultantpoolName;
    }

    public String getPerformingReferredPAmount() {
        return PerformingReferredPAmount;
    }

    public void setPerformingReferredPAmount(String PerformingReferredPAmount) {
        this.PerformingReferredPAmount = PerformingReferredPAmount;
    }

    public String getPerformingReferredPoolName() {
        return PerformingReferredPoolName;
    }

    public void setPerformingReferredPoolName(String PerformingReferredPoolName) {
        this.PerformingReferredPoolName = PerformingReferredPoolName;
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

}
