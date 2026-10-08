/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi.tcp;

import aarogya.projectMenu.mi.IMachineConfiguration;
import java.util.Objects;

/**
 *
 * @author abhishek
 */
public abstract class AbstractTCPMachineConfig implements IMachineConfigurationTcp {

    @Override
    public int hashCode() {
        int hash = 7;
        String thisMachineFullCode = this.getMachineCode() + ":" + this.getMachineManualCode();
        hash = 29 * hash + Objects.hashCode(thisMachineFullCode);
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
//        if (!obj.getClass().isAssignableFrom(IMachineConfiguration.class)) {
//            return false;
//        }
        final IMachineConfiguration other = (IMachineConfiguration) obj;
        String thisMachineFullCode = this.getMachineCode() + ":" + this.getMachineManualCode();
        String otherMachineFullCode = other.getMachineCode() + ":" + other.getMachineManualCode();
        if (!Objects.equals(thisMachineFullCode, otherMachineFullCode)) {
            return false;
        }
        return true;
    }

}
