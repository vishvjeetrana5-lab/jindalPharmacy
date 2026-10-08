package dataman.mi.routing;

import aarogya.projectMenu.mi.IMachineConfiguration;
import aarogya.projectMenu.mi.serial.IMachineConfigurationSerial;
import aarogya.projectMenu.mi.shared.MIConfigCommands;
import aarogya.projectMenu.mi.shared.MIConfigDetailsParams;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import aarogya.projectMenu.mi.tcp.IMachineConfigurationTcp;
import com.google.gson.reflect.TypeToken;
import dataman.dmPlugin.CFunctions;
import dataman.dmPlugin.server.NanoHTTPD;
import dataman.dmPlugin.server.router.RouterNanoHTTPD;
import dataman.miToolkit.MiToolkit;
import dataman.stPlugin.MiStartUp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MiConfigService extends RouterNanoHTTPD.DefaultHandler {

    @Override
    public NanoHTTPD.Response post(RouterNanoHTTPD.UriResource uriResource, Map<String, String> urlParams, NanoHTTPD.IHTTPSession session) {
        NanoHTTPD.Response response = null;
        try {
            String userAgent = session.getHeaders().get("user-agent");
            Map<String, List<String>> queryParams = NanoHTTPD.decodeParameters(session.getQueryParameterString());
            if (queryParams.containsKey("cmd")) {
                String cmdName = MiToolkit.nullToEmpty(queryParams.get("cmd").get(0));
                MIConfigCommands command = MIConfigCommands.getCommand(cmdName);
                if (command == MIConfigCommands.MI_Cmd_FetchConfigs) {
                    response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), fetchMachineConfigs());
                } else if (command == MIConfigCommands.MI_Cmd_FetchMachineDetails) {
                    if (queryParams.containsKey(MIConfigDetailsParams.ConfigName.toString())) {
                        String configName = MiToolkit.nullToEmpty(queryParams.get("configName").get(0));
                        response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), fetchMachineDetails(configName));
                    }
                } else if (command == MIConfigCommands.MI_Cmd_SaveMISettings) {
                    response = NanoHTTPD.newFixedLengthResponse(getStatus(), getMimeType(), String.valueOf(saveLocalSettingsOfMachineInterface(session.getParms())));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            response.addHeader("Access-Control-Allow-Origin", "*");
        }
        return response;
    }

    private String fetchMachineDetails(String configName) {
        String jsonString = "";
        try {
            if (!MiToolkit.isNullOrEmpty(configName)) {
                MIConfigEnum config = MIConfigEnum.getConfiguration(configName);
                IMachineConfiguration machineConfig = null;
                try {
                    machineConfig = MiStartUp.createMachineInterfaceParserInstance(config);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (machineConfig != null) {
                    Map<String, String> configDetails = new HashMap<>();
                    configDetails.put(MIConfigDetailsParams.ConfigName.toString(), configName);
                    configDetails.put(MIConfigDetailsParams.MachineCode.toString(), machineConfig.getMachineCode());
                    configDetails.put(MIConfigDetailsParams.MachineModel.toString(), machineConfig.getMachineModel());
                    configDetails.put(MIConfigDetailsParams.MachineName.toString(), machineConfig.getMachineName());
                    configDetails.put(MIConfigDetailsParams.TransmissionMode.toString(), machineConfig.getTransmissionMode());
//                    configDetails.put(MIConfigDetailsParams.ObservableTestList.toString(), CFunctions.getJsonFromObject(machineConfig.getMachineObservableTestList(), new TypeToken<List<String>>() {
//                    }));
                    if (machineConfig instanceof IMachineConfigurationSerial) {
                        IMachineConfigurationSerial serialMachineConfig = (IMachineConfigurationSerial) machineConfig;
                        configDetails.put(MIConfigDetailsParams.BaudRate.toString(), serialMachineConfig.getBaudRate());
                        configDetails.put(MIConfigDetailsParams.CommunicationMode.toString(), serialMachineConfig.getCommunicationMode());
                        configDetails.put(MIConfigDetailsParams.DataBits.toString(), serialMachineConfig.getDataBits());
                        configDetails.put(MIConfigDetailsParams.HardwareControl.toString(), serialMachineConfig.getHardwareControl());
                        configDetails.put(MIConfigDetailsParams.Parity.toString(), serialMachineConfig.getParity());
                        configDetails.put(MIConfigDetailsParams.Protocol.toString(), serialMachineConfig.getProtocol());
                        configDetails.put(MIConfigDetailsParams.ProtocolVersion.toString(), serialMachineConfig.getProtocolVersion());
                        configDetails.put(MIConfigDetailsParams.StopBits.toString(), serialMachineConfig.getStopBits());
                    } else if (machineConfig instanceof IMachineConfigurationTcp) {
                        IMachineConfigurationTcp tcpMachineConfig = (IMachineConfigurationTcp) machineConfig;
                        configDetails.put(MIConfigDetailsParams.CommunicationMode.toString(), tcpMachineConfig.getCommunicationMode());
                        configDetails.put(MIConfigDetailsParams.Protocol.toString(), tcpMachineConfig.getProtocol());
                        configDetails.put(MIConfigDetailsParams.ProtocolVersion.toString(), tcpMachineConfig.getProtocolVersion());
                        configDetails.put(MIConfigDetailsParams.CommunicationPort.toString(), MiToolkit.nullToEmpty(tcpMachineConfig.getCommunicationPort()).trim());
                    }
                    jsonString = CFunctions.getJsonFromObject(configDetails, new TypeToken<Map<String, String>>() {
                    });
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonString;
    }

    private boolean saveLocalSettingsOfMachineInterface(Map<String, String> urlParams) {
        boolean bool = false;
        try {
            String jsonString = urlParams.get("data");
            Map<String, String> additionalParams = new HashMap<>();
            Iterator<String> iterator = urlParams.keySet().iterator();
            while (iterator.hasNext()) {
                String urlKey = iterator.next();
                if ((urlKey != null) && (!urlKey.equals("")) && (!urlKey.equalsIgnoreCase("data"))) {
                    additionalParams.put(urlKey, urlParams.get(urlKey));
                }
            }
            MiStartUp.saveMachineInterfaceSettings(jsonString, additionalParams);
            bool = true;
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    private String fetchMachineConfigs() {
        String jsonString = "";
        try {
            List<String> listConfigs = new ArrayList<>();
            MIConfigEnum[] values = MIConfigEnum.values();
            for (int i = 0; i < values.length; i++) {
                listConfigs.add(values[i].getConfigurationName());
            }
            jsonString = CFunctions.getJsonFromObject(listConfigs, new TypeToken<List<String>>() {
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonString;
    }

    @Override
    public String getText() {
        return "MACHINE_CONFIG";
    }

    @Override
    public NanoHTTPD.Response.IStatus getStatus() {
        return NanoHTTPD.Response.Status.OK;
    }

    @Override
    public String getMimeType() {
        return "application/json";
    }

}
