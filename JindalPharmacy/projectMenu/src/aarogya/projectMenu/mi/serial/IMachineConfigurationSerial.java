/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi.serial;

import aarogya.projectMenu.mi.IMachineConfiguration;

/**
 *
 * @author abhishek
 */
public interface IMachineConfigurationSerial extends IMachineConfiguration {

    public String getCommunicationMode();

    public String getProtocol();

    public String getProtocolVersion();

    public String getBaudRate();

    public void setBaudRate(String baudRate) throws Exception;

    public String getStopBits();

    public void setStopBits(String stopBits) throws Exception;

    public String getDataBits();

    public void setDataBits(String dataBits) throws Exception;

    public String getParity();

    public void setParity(String parity) throws Exception;

    public String getHardwareControl();

    public void setHardwareControl(String hardwareControl) throws Exception;
}
