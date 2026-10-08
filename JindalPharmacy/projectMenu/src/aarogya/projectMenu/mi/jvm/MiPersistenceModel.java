/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.projectMenu.mi.jvm;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author abhishek
 */
public class MiPersistenceModel {

    @SerializedName("configName")
    public String machineConfigurationName;

    @SerializedName("labRecId")
    public String labRecordId;

    @SerializedName("sampleRecId")
    public String sampleRecId;

    @SerializedName("subTestName")
    public String subTestName;

    @SerializedName("subTestValue")
    public String subTestValue;

    @SerializedName("subTestUnit")
    public String subTestUnit;

    @SerializedName("machineCode")
    public String machineCode;

    @SerializedName("date")
    public String machineDataDate;

    @SerializedName("localPcIp")
    public String localComputerIp;

    @SerializedName("localPcName")
    public String localComputerName;

    @SerializedName("machineName")
    public String machineName;
}
