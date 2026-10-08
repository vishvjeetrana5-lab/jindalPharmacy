/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi;

import java.util.List;

/**
 *
 * @author abhishek
 */
public interface IMachineConfiguration {

    public String getMachineName();

    public String getMachineCode();

    public String getMachineModel();

    public String getTransmissionMode();

    public String getCommunicationPort();

    public void setCommunicationPort(String portToConnect);

    public List<String> getMachineObservableTestList();

    public String getMachineManualCode();

    public void setMachineManualCode(String manualMachineCode);

    public void start() throws Exception;

    public void stop();
}
