package aarogya.projectMenu.mi.shared;

public enum MIConfigEnum {

    ERBA_EM_200_SERIAL("ERBA", "EM-200", "SERIAL", "ASTM", "Uni-Direction", "ERBA_EM_200_SERIAL_UNI", "ERBA EM-200 SERIAL", "dataman.mi.erba.em200.serial.EM200_Parser"),
    MINDRAY_BC_3600_TCP("MINDRAY", "BC-3600", "TCP", "HL7", "Uni-Direction", "MINDRAY_BC_3600_TCP_UNI", "MINDRAY BC-3600 TCP", "dataman.mi.mindray.bc3600.tcp.BC3600_Parser"),
    MINDRAY_BC_3107_TCP("MINDRAY", "BC-3107", "TCP", "HL7", "Uni-Direction", "MINDRAY_BC_3107_TCP_UNI", "MINDRAY BC-3107 TCP", "dataman.mi.mindray.bc3107.tcp.BC3107_Parser"),
    MINDRAY_BS_240_TCP("MINDRAY", "BS-240", "TCP", "HL7", "Uni-Direction", "MINDRAY_BS_240_TCP_UNI", "MINDRAY BS-240 TCP", "dataman.mi.mindray.bs240.tcp.BS240_Parser"),
    BECKMAN_COULTER_ACCESS2_SERIAL("BECKMAN COULTER", "ACCESS2", "SERIAL", "ASTM", "Uni-Direction", "BECKMAN_COULTER_ACCESS2_SERIAL_UNI", "BECKMAN COULTER ACCESS2 SERIAL", "dataman.mi.beckman_coulter.access2.Access2_Parser"),
    SYSMEX_XN_L_SERIES_SERIAL("SYSMEX", "XN L SERIES", "SERIAL", "ASTM", "Uni-Direction", "SYSMEX_XN_L_SERIES_SERIAL_UNI", "SYSMEX XN L SERIES SERIAL", "dataman.mi.sysmex.xn_l.XN_L_Series_Parser"),
    SYSMEX_XP_SERIES_SERIAL("SYSMEX", "XP SERIES", "SERIAL", "ASTM", "Uni-Direction", "SYSMEX_XP_SERIES_SERIAL_UNI", "SYSMEX XP SERIES SERIAL", "dataman.mi.sysmex.xp_series.Sysmex_XP_Parser");

    private String name;
    private String fullClassPath;
    private String displayName;
    private String communicationMode;
    private String protocol;
    private String transmissionMode;
    private String machineCompany;
    private String machineModel;

    private MIConfigEnum(String machineCompany, String machineModel, String communicationMode, String protocol, String transmissionMode, String name, String displayName, String classPath) {
        this.machineCompany = machineCompany.trim();
        this.machineModel = machineModel.trim();
        this.name = name.trim();
        this.communicationMode = communicationMode.trim();
        this.protocol = protocol.trim();
        this.transmissionMode = transmissionMode.trim();
        this.displayName = displayName.trim();
        this.fullClassPath = classPath.trim();
    }

    public String getConfigurationName() {
        return this.name;
    }

    public String getConfigurationDisplayName() {
        return displayName;
    }

    public String getConfigurationFullClassPath() {
        return this.fullClassPath;
    }

    public String getMachineCompany() {
        return machineCompany;
    }

    public void setMachineCompany(String machineCompany) {
        this.machineCompany = machineCompany;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getCommunicationMode() {
        return communicationMode;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getTransmissionMode() {
        return transmissionMode;
    }

    public static MIConfigEnum getConfiguration(String configName) {
        MIConfigEnum configEnum = null;
        if (MIConfigEnum.ERBA_EM_200_SERIAL.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = ERBA_EM_200_SERIAL;
        } else if (MIConfigEnum.MINDRAY_BC_3107_TCP.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = MINDRAY_BC_3107_TCP;
        } else if (MIConfigEnum.MINDRAY_BC_3600_TCP.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = MINDRAY_BC_3600_TCP;
        } else if (MIConfigEnum.MINDRAY_BS_240_TCP.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = MINDRAY_BS_240_TCP;
        } else if (MIConfigEnum.BECKMAN_COULTER_ACCESS2_SERIAL.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = BECKMAN_COULTER_ACCESS2_SERIAL;
        } else if (MIConfigEnum.SYSMEX_XN_L_SERIES_SERIAL.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = SYSMEX_XN_L_SERIES_SERIAL;
        } else if (MIConfigEnum.SYSMEX_XP_SERIES_SERIAL.getConfigurationName().equalsIgnoreCase(configName)) {
            configEnum = SYSMEX_XP_SERIES_SERIAL;
        }
        return configEnum;
    }

}
