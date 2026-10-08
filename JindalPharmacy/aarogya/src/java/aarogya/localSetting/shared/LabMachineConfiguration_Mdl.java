package aarogya.localSetting.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LabMachineConfiguration_Mdl extends KeyField implements Serializable, IsSerializable {

    private int uiHashCode;

    private String machineName;
    private String machineCode;

    private String modelNo;
    private String companyName;
    private String communicationMode;
    private String transmissionMode;
    private String protocoll;

    private String ipAddress;
    private String portNo;

    private String comPortNo;
    private String baudRate;
    private String stopBit;
    private String dataBit;
    private String parity;
    private String hardwareControl;
    private String mode;

    private List<GrdLabTestCodification_Mdl> grdData;

    public LabMachineConfiguration_Mdl() {
        grdData = new ArrayList<GrdLabTestCodification_Mdl>();
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(String communicationMode) {
        this.communicationMode = communicationMode;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public void setTransmissionMode(String transmissionMode) {
        this.transmissionMode = transmissionMode;
    }

    public String getProtocoll() {
        return protocoll;
    }

    public void setProtocoll(String protocoll) {
        this.protocoll = protocoll;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPortNo() {
        return portNo;
    }

    public void setPortNo(String portNo) {
        this.portNo = portNo;
    }

    public String getComPortNo() {
        return comPortNo;
    }

    public void setComPortNo(String comPortNo) {
        this.comPortNo = comPortNo;
    }

    public String getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate;
    }

    public String getStopBit() {
        return stopBit;
    }

    public void setStopBit(String stopBit) {
        this.stopBit = stopBit;
    }

    public String getDataBit() {
        return dataBit;
    }

    public void setDataBit(String dataBit) {
        this.dataBit = dataBit;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getHardwareControl() {
        return hardwareControl;
    }

    public void setHardwareControl(String hardwareControl) {
        this.hardwareControl = hardwareControl;
    }

    public List<GrdLabTestCodification_Mdl> getGrdData() {
        return grdData;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public int getSharedKeyFieldHashCode() {
        //return hashCode();
        return uiHashCode;//Objects.hashCode(this.toString());
    }

    public int getUiHashCode() {
        return uiHashCode;
    }

    public void setUiHashCode(int uiHashCode) {
        this.uiHashCode = uiHashCode;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        String hashCodeKey = DmShared.xNull(getSharedKeyFieldCode()).trim() + ":" + DmShared.xNull(getSharedKeyFieldName()).trim();
        hash = 41 * hash + Objects.hashCode(hashCodeKey);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LabMachineConfiguration_Mdl other = (LabMachineConfiguration_Mdl) obj;
        String thisHashCode = DmShared.xNull(getSharedKeyFieldCode()).trim() + ":" + DmShared.xNull(getSharedKeyFieldName()).trim();
        String otherHashCode = DmShared.xNull(other.getSharedKeyFieldCode()).trim() + ":" + DmShared.xNull(other.getSharedKeyFieldName()).trim();
        if (!Objects.equals(thisHashCode, otherHashCode)) {
            return false;
        }
        return true;
    }

}
