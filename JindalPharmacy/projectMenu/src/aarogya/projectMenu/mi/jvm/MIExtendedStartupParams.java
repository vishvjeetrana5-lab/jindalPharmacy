/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi.jvm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 *
 * @author abhishek
 */
public class MIExtendedStartupParams {

    @Expose(serialize = true, deserialize = true)
    @SerializedName("manMacCode")
    private String manualMachineCode;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("macCode")
    private String machineCode;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("commPort")
    private String machineCommunicationPort;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("result")
    private String machineStartupResult;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("stopBits")
    private String stopBits;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("dataBits")
    private String dataBits;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("hardwareControl")
    private String hardwareControl;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("parity")
    private String parity;

    @Expose(serialize = true, deserialize = true)
    @SerializedName("baudRate")
    private String baudRate;

    public String getMachineCommunicationPort() {
        return machineCommunicationPort;
    }

    public void setMachineCommunicationPort(String machineCommunicationPort) {
        this.machineCommunicationPort = machineCommunicationPort;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineStartupResult() {
        return machineStartupResult;
    }

    public void setMachineStartupResult(String machineStartupResult) {
        this.machineStartupResult = machineStartupResult;
    }

    public String getManualMachineCode() {
        return manualMachineCode;
    }

    public void setManualMachineCode(String manualMachineCode) {
        this.manualMachineCode = manualMachineCode;
    }

    public String getStopBits() {
        return stopBits;
    }

    public void setStopBits(String stopBits) {
        this.stopBits = stopBits;
    }

    public String getDataBits() {
        return dataBits;
    }

    public void setDataBits(String dataBits) {
        this.dataBits = dataBits;
    }

    public String getHardwareControl() {
        return hardwareControl;
    }

    public void setHardwareControl(String hardwareControl) {
        this.hardwareControl = hardwareControl;
    }

    public String getParity() {
        return parity;
    }

    public void setParity(String parity) {
        this.parity = parity;
    }

    public String getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(String buadRate) {
        this.baudRate = buadRate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        String finalMachineCode = this.machineCode + ":" + this.manualMachineCode;
        hash = 53 * hash + Objects.hashCode(finalMachineCode);
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
        final MIExtendedStartupParams other = (MIExtendedStartupParams) obj;
        String finalThisMachineCode = this.machineCode + ":" + this.manualMachineCode;
        String finalOtherMachineCode = other.getMachineCode() + ":" + other.getManualMachineCode();
        if (!Objects.equals(finalThisMachineCode, finalOtherMachineCode)) {
            return false;
        }
        return true;
    }

}
