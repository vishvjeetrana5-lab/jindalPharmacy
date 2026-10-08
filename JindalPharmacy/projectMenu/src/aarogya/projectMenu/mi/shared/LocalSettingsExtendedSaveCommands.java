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
public enum LocalSettingsExtendedSaveCommands {

    LSE_StartMachines("startMachines");

    private String extraCommand;

    public static LocalSettingsExtendedSaveCommands getLocalSettingsExtendedCommand(String extraCommand) {
        LocalSettingsExtendedSaveCommands command = null;
        if (LocalSettingsExtendedSaveCommands.LSE_StartMachines.toString().equalsIgnoreCase(extraCommand)) {
            command = LSE_StartMachines;
        }
        return command;
    }

    @Override
    public String toString() {
        return extraCommand;
    }

    private LocalSettingsExtendedSaveCommands(String extraCommand) {
        this.extraCommand = extraCommand.trim();
    }
}
