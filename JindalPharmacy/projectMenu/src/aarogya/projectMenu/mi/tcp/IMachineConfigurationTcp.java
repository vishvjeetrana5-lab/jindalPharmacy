/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi.tcp;

import aarogya.projectMenu.mi.IMachineConfiguration;

/**
 *
 * @author abhishek
 */
public interface IMachineConfigurationTcp extends IMachineConfiguration {

    public String getCommunicationMode();

    public String getProtocol();

    public String getProtocolVersion();

}
