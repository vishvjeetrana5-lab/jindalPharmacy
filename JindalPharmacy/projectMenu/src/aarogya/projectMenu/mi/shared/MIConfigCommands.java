package aarogya.projectMenu.mi.shared;

public enum MIConfigCommands {
    MI_Cmd_FetchConfigs("fetchConfigs"),
    MI_Cmd_FetchMachineDetails("fetchMachineDetails"),
    MI_Cmd_SaveMISettings("saveMISettings");

    @Override
    public String toString() {
        return this.cmdName;
    }

    public static MIConfigCommands getCommand(String cmdName) {
        MIConfigCommands command = null;
        if (MI_Cmd_FetchConfigs.toString().equalsIgnoreCase(cmdName)) {
            command = MI_Cmd_FetchConfigs;
        } else if (MI_Cmd_FetchMachineDetails.toString().equalsIgnoreCase(cmdName)) {
            command = MI_Cmd_FetchMachineDetails;
        } else if (MI_Cmd_SaveMISettings.toString().equalsIgnoreCase(cmdName)) {
            command = MI_Cmd_SaveMISettings;
        }
        return command;
    }

    private String cmdName;

    private MIConfigCommands(String cmdName) {
        this.cmdName = cmdName.trim();
    }
}
