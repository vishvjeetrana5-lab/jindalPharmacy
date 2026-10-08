/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi.shared;

/**
 *
 * @author abhishek
 */
public enum MIConfigDetailsParams {

    ConfigName("configName"),
    MachineName("machineName"),
    MachineCode("machineCode"),
    MachineModel("machineModel"),
    TransmissionMode("transMode"),
    ObservableTestList("observableTestList"),
    CommunicationMode("commMode"),
    Protocol("protocol"),
    ProtocolVersion("protocolVersion"),
    BaudRate("baudRate"),
    StopBits("stopBits"),
    DataBits("dataBits"),
    Parity("parity"),
    HardwareControl("hardwareControl"),
    CommunicationPort("communicationPort"),
    MachineConfigsForAutomation("machineConfigsAutomation");

    private String paramName;

    @Override
    public String toString() {
        return this.paramName;
    }

    public static MIConfigDetailsParams getConfigDetailsParam(String paramName) {
        MIConfigDetailsParams param = null;
        if (MIConfigDetailsParams.BaudRate.toString().equalsIgnoreCase(paramName)) {
            param = BaudRate;
        } else if (MIConfigDetailsParams.CommunicationMode.toString().equalsIgnoreCase(paramName)) {
            param = CommunicationMode;
        } else if (MIConfigDetailsParams.ConfigName.toString().equalsIgnoreCase(paramName)) {
            param = ConfigName;
        } else if (MIConfigDetailsParams.DataBits.toString().equalsIgnoreCase(paramName)) {
            param = DataBits;
        } else if (MIConfigDetailsParams.HardwareControl.toString().equalsIgnoreCase(paramName)) {
            param = HardwareControl;
        } else if (MIConfigDetailsParams.MachineCode.toString().equalsIgnoreCase(paramName)) {
            param = MachineCode;
        } else if (MIConfigDetailsParams.MachineModel.toString().equalsIgnoreCase(paramName)) {
            param = MachineModel;
        } else if (MIConfigDetailsParams.MachineName.toString().equalsIgnoreCase(paramName)) {
            param = MachineName;
        } else if (MIConfigDetailsParams.ObservableTestList.toString().equalsIgnoreCase(paramName)) {
            param = ObservableTestList;
        } else if (MIConfigDetailsParams.Parity.toString().equalsIgnoreCase(paramName)) {
            param = Parity;
        } else if (MIConfigDetailsParams.Protocol.toString().equalsIgnoreCase(paramName)) {
            param = Protocol;
        } else if (MIConfigDetailsParams.ProtocolVersion.toString().equalsIgnoreCase(paramName)) {
            param = ProtocolVersion;
        } else if (MIConfigDetailsParams.StopBits.toString().equalsIgnoreCase(paramName)) {
            param = StopBits;
        } else if (MIConfigDetailsParams.TransmissionMode.toString().equalsIgnoreCase(paramName)) {
            param = TransmissionMode;
        }
        return param;
    }

    private MIConfigDetailsParams(String paramName) {
        this.paramName = paramName.trim();
    }

}
