package dataman.stPlugin;

import aarogya.projectMenu.mi.IMachineConfiguration;
import aarogya.projectMenu.mi.jvm.MIExtendedStartupParams;
import aarogya.projectMenu.mi.serial.IMachineConfigurationSerial;
import aarogya.projectMenu.mi.shared.ExtendedLocalSettings;
import aarogya.projectMenu.mi.shared.LocalSettingsExtendedSaveCommands;
import aarogya.projectMenu.mi.shared.MIConfigEnum;
import aarogya.projectMenu.servlet.shared.MachineInterfaceConfig_SrvAsync;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmPlugin.CFunctions;
import dataman.dmPlugin.ILocalSettingsRefreshCommander;
import dataman.mi.routing.MiPersistanceService;
import dataman.miToolkit.MiToolkit;
import static dataman.miToolkit.MiToolkit.isNullOrEmpty;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MiStartUp implements ILocalSettingsRefreshCommander {

    private static List<IMachineConfiguration> listRunningMachinesCache;

    public MiStartUp() {
        listRunningMachinesCache = Collections.synchronizedList(new ArrayList<>());
    }

    public static boolean containsMachineInRunningMode(String machineCode, String manualMachineCode) {
        boolean bool = false;
        if (!isNullOrEmpty(machineCode)) {
            MIConfigEnum miConfig = MIConfigEnum.getConfiguration(MiToolkit.nullToEmpty(machineCode));
            if (miConfig != null) {
                bool = containsMachineInRunningMode(miConfig, manualMachineCode);
            }
        }
        return bool;
    }

    public static boolean containsMachineInRunningMode(MIConfigEnum miConfig, String manualMachineCode) {
        boolean bool = false;
        if (miConfig != null) {
            try {
                IMachineConfiguration machineConfig = MiStartUp.createMachineInterfaceParserInstance(miConfig);
                machineConfig.setMachineManualCode(manualMachineCode);
                bool = listRunningMachinesCache.contains(machineConfig);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return bool;
    }

    public static IMachineConfiguration getRunningMachineFromCache(MIConfigEnum miConfig, String manualMachineCode) {
        IMachineConfiguration machineConfig = null;
        if (containsMachineInRunningMode(miConfig, manualMachineCode)) {
            try {
                IMachineConfiguration tempConfig = MiStartUp.createMachineInterfaceParserInstance(miConfig);
                tempConfig.setMachineManualCode(manualMachineCode);
                int indexOfRunningMachine = listRunningMachinesCache.indexOf(tempConfig);
                if (indexOfRunningMachine >= 0) {
                    machineConfig = listRunningMachinesCache.get(indexOfRunningMachine);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return machineConfig;
    }

    public static void removeMachineConfigFromRunningCache(IMachineConfiguration miConfig) {
        MIConfigEnum miConfigEnum = MIConfigEnum.getConfiguration(miConfig.getMachineCode());
        IMachineConfiguration foundMiConfig = getRunningMachineFromCache(miConfigEnum, miConfig.getMachineManualCode());
        if ((foundMiConfig != null) && (foundMiConfig.equals(miConfig))) {
            listRunningMachinesCache.remove(miConfig);
        }
    }

    public static void addMachineConfigToRunningCache(IMachineConfiguration miConfig) {
        removeMachineConfigFromRunningCache(miConfig);
        listRunningMachinesCache.add(miConfig);
    }

    public static IMachineConfiguration createMachineInterfaceParserInstance(MIConfigEnum miConfig) throws Exception {
        IMachineConfiguration configuration = null;
        try {            
            Class<?> clazz = Class.forName(miConfig.getConfigurationFullClassPath());
            Constructor<?> cTor = clazz.getConstructor(MIConfigEnum.class);
            Object objInstance = cTor.newInstance(miConfig);
            if (objInstance instanceof IMachineConfiguration) {
                configuration = (IMachineConfiguration) objInstance;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            configuration = null;
        }
        return configuration;
    }

    @Override
    public void executeAfterLocalSettingReloaded(JVMLocalSetting_Mdl reloadedLocalSettings, Map<String, String> additionalParams) {
        List<MIExtendedStartupParams> listStartupParams = null;
        try {
            if ((additionalParams != null) && (reloadedLocalSettings != null) && (additionalParams.containsKey(LocalSettingsExtendedSaveCommands.LSE_StartMachines.toString()))) {

                boolean startMachineCommand = Boolean.parseBoolean(additionalParams.get(LocalSettingsExtendedSaveCommands.LSE_StartMachines.toString()));

                if (startMachineCommand) {

                    listStartupParams = fetchMachineConfigurations(reloadedLocalSettings.getExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString()));

                    for (int i = 0; i < listStartupParams.size(); i++) {
                        MIExtendedStartupParams startupParam = listStartupParams.get(i);
                        String automationResultString = "";
                        if (startupParam != null) {
                            MIConfigEnum miConfig = MIConfigEnum.getConfiguration(startupParam.getMachineCode());
                            if (miConfig != null) {
                                IMachineConfiguration configuration = null;
                                try {
                                    Object clazzInstance = MiStartUp.createMachineInterfaceParserInstance(miConfig);//clazz.newInstance();
                                    if (clazzInstance instanceof IMachineConfiguration) {
                                        if (MiStartUp.containsMachineInRunningMode(miConfig, startupParam.getManualMachineCode())) {
                                            configuration = MiStartUp.getRunningMachineFromCache(miConfig, startupParam.getManualMachineCode());
                                        } else {
                                            configuration = (IMachineConfiguration) clazzInstance;
                                            configuration.setMachineManualCode(startupParam.getManualMachineCode());
                                        }
                                        configuration = (IMachineConfiguration) clazzInstance;
                                        configuration.setMachineManualCode(startupParam.getManualMachineCode());

                                        configuration.stop();
                                        if (startupParam.getBaudRate() != null) {
                                            ((IMachineConfigurationSerial) configuration).setBaudRate(startupParam.getBaudRate());
                                        }
                                        if (startupParam.getDataBits() != null) {
                                            ((IMachineConfigurationSerial) configuration).setDataBits(startupParam.getDataBits());
                                        }
                                        if (startupParam.getHardwareControl() != null) {
                                            ((IMachineConfigurationSerial) configuration).setHardwareControl(startupParam.getHardwareControl());
                                        }
                                        if (startupParam.getParity() != null) {
                                            ((IMachineConfigurationSerial) configuration).setParity(startupParam.getParity());
                                        }
                                        if (startupParam.getStopBits() != null) {
                                            ((IMachineConfigurationSerial) configuration).setStopBits(startupParam.getStopBits());
                                        }
                                        configuration.setCommunicationPort(startupParam.getMachineCommunicationPort());
                                        configuration.start();
                                        automationResultString = "Machine started successfully...";
                                        MiStartUp.addMachineConfigToRunningCache(configuration);

                                    }
                                } catch (Exception ex) {
                                    if (configuration != null) {
                                        MiStartUp.removeMachineConfigFromRunningCache(configuration);
                                    }
                                    automationResultString = "Machine automation causing exception : " + MiToolkit.nullToEmpty(ex.getMessage()).trim();
                                    ex.printStackTrace();
                                }
                            } else {
                                automationResultString = "Machine Configuration not found...";
                            }
                        }
                        startupParam.setMachineStartupResult(automationResultString);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (listStartupParams != null) {
                new frmMiStartupResults(listStartupParams).show();
            }
        }
    }
    
    

    public static void saveMachineInterfaceSettings(String jsonOfLocalSettings, Map<String, String> additionalParams) throws Exception {
        JVMLocalSetting_Mdl settings = CFunctions.getObjectFromJson(jsonOfLocalSettings, new TypeToken<JVMLocalSetting_Mdl>() {
        });
        saveMachineInterfaceSettings(settings, additionalParams);
    }

    public static String saveMachineInterfaceSettings(JVMLocalSetting_Mdl localSettings, Map<String, String> additionalParams) throws Exception {
        String jsonOfJVMLocalSettings = "";
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            JVMLocalSetting_Mdl savedLocalSettings = CFunctions.loadSavedSettings(false, null);
            if (savedLocalSettings == null) {
                savedLocalSettings = new JVMLocalSetting_Mdl();
            }
            savedLocalSettings.setExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString(), CFunctions.nullToEmpty(localSettings.getExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString())).trim());
            fos = new FileOutputStream(CFunctions.getSettingsFile());
            oos = new ObjectOutputStream(fos);
            oos.writeObject(savedLocalSettings);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex);
        } finally {
            if (oos != null) {
                oos.close();
                oos = null;
            }
            if (fos != null) {
                fos.close();
                fos = null;
            }
            CFunctions.loadSavedSettings(true, additionalParams);
            jsonOfJVMLocalSettings = CFunctions.getJsonFromObject(localSettings, new TypeToken<JVMLocalSetting_Mdl>() {
            });
        }
        return jsonOfJVMLocalSettings;
    }

    private List<MIExtendedStartupParams> fetchMachineConfigurations(String jsonOfListMachineCodes) {
        List<MIExtendedStartupParams> listStartupParams = new ArrayList<>();
        try {
            String urlToHit = CFunctions.getServerApplicationUrl() + "/" + MachineInterfaceConfig_SrvAsync.class.getName().trim().replace(".", "/");// "/dataman/selfToolkit/mi/miFetchMachineConfigs";

            HttpURLConnection httpConnection = null;
            InputStream ins = null;

            try {
                URL url = new URL(urlToHit);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("POST");
                httpConnection.setDoInput(true);
                httpConnection.setDoOutput(true);
                httpConnection.setRequestProperty("user-agent", MiPersistanceService.userAgent);

                httpConnection.getOutputStream().write(jsonOfListMachineCodes.getBytes(StandardCharsets.UTF_8));

                int rCode = httpConnection.getResponseCode();

                if (rCode != 200) {
                    throw new Exception("Server had some exception while processing machine data...");
                }

                StringBuilder sb = new StringBuilder();
                byte[] BUFFEREDBYTES = new byte[1024 * 1024];
                ins = httpConnection.getInputStream();
                int readLength = ins.read(BUFFEREDBYTES);
                while (readLength != -1) {
                    String readString = new String(BUFFEREDBYTES, 0, readLength);
                    sb.append(readString);
                    readLength = ins.read(BUFFEREDBYTES);
                }

                if (!MiToolkit.isNullOrEmpty(sb.toString())) {
                    listStartupParams = CFunctions.getObjectFromJson(sb.toString(), new TypeToken<List<MIExtendedStartupParams>>() {
                    });
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (ins != null) {
                    ins.close();
                    ins = null;
                }
                if (httpConnection != null) {
                    httpConnection.disconnect();
                    httpConnection = null;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listStartupParams;
    }

    @Override
    public void executeAfterServerStarted() {
        try {
//            BC3600_Parser.fillDummyRecords();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void executeAfterServerStopped() {
        try {
//            BC3600_Parser.clearDummyRecords();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
