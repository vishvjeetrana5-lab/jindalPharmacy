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
public enum ExtendedLocalSettings {

    MI_Configurations("machineConfigurations");

    private String settingName;

    @Override
    public String toString() {
        return this.settingName;
    }

    public static ExtendedLocalSettings getExtendedLocalSettings(String settingName) {
        ExtendedLocalSettings settings = null;
        if (MI_Configurations.toString().equalsIgnoreCase(settingName)) {
            settings = MI_Configurations;
        }
        return settings;
    }

    private ExtendedLocalSettings(String settingName) {
        this.settingName = settingName;
    }

}
