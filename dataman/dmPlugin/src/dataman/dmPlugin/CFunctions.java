package dataman.dmPlugin;

import aarogya.projectMenu.mi.shared.ExtendedLocalSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrintMap_Mdl;
import dataman.dmBase.self.shared.DmLocalSettingPrinter_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CFunctions {

    private static List<DmLocalSettingPrinter_Mdl> cachedPrinterSettings = new ArrayList<>();
    private static List<DmLocalSettingPrintMap_Mdl> cachedReportMapSettings = new ArrayList<>();

    private static final String localSettingFileName = "localsetting.dat";
    private static final String localPOSSettingFileName = "localpossetting.dat";
    private static final String localConfigurationFileName = "dmConfig.properties";

    private static String serverApplicationUrl = "";

    private static String localMachineName;
    private static String localMachineIp;

    private static Map<String, ILocalSettingsRefreshCommander> mapCommandersForSettingsReloaded;

    public static String getServerApplicationUrl() {
        return serverApplicationUrl;
    }

    public static void setServerApplicationUrl(String serverUrl) {
        serverApplicationUrl = serverUrl;
    }

    public static List<DmLocalSettingPrinter_Mdl> getPrinterSettings() {
        return cachedPrinterSettings;
    }

    public static List<DmLocalSettingPrintMap_Mdl> getReportMapSettings() {
        return cachedReportMapSettings;
    }

    public static String assignPrinterToReportMap(DmLocalSettingPrintMap_Mdl reportMapModel, String printerCode, Map<String, String> additionalParams) throws Exception {
        DmLocalSettingPrinter_Mdl printerModel = new DmLocalSettingPrinter_Mdl();
        printerModel.setPrinterCode(printerCode);
        return assignPrinterToReportMap(reportMapModel, printerModel, additionalParams);
    }

    public static String assignPrinterToReportMap(DmLocalSettingPrintMap_Mdl reportMapModel, DmLocalSettingPrinter_Mdl printerModel, Map<String, String> additionalParams) throws Exception {
        String responseString = "";
        try {
            if (!cachedPrinterSettings.contains(printerModel)) {
                throw new Exception("Printer Settings for Printer : " + printerModel.getPrinterCode() + " not found.");
            }

            DmLocalSettingPrinter_Mdl cachedPrinterModel = cachedPrinterSettings.get(cachedPrinterSettings.indexOf(printerModel));

            if (!cachedReportMapSettings.contains(reportMapModel)) {
                reportMapModel.setPrinterCode(cachedPrinterModel.getPrinterCode());
                cachedReportMapSettings.add(reportMapModel);
            } else {
                cachedReportMapSettings.get(cachedReportMapSettings.indexOf(reportMapModel)).setPrinterCode(printerModel.getPrinterCode());
            }

            JVMLocalSetting_Mdl localSettings = new JVMLocalSetting_Mdl();
            localSettings.setPrinterSettings(cachedPrinterSettings);
            localSettings.setReportMapSettings(cachedReportMapSettings);

            responseString = savePrinterSettings(localSettings, additionalParams);

        } finally {

        }
        return responseString;
    }

    public static boolean isNullOrEmpty(String stringToTest) {
        return ((stringToTest == null) || (stringToTest.equals("")));
    }

    public static String getSystemPath() throws Exception {
        return System.getenv("WINDIR") + "\\system32";
    }

    public static String nullToEmpty(String stringToConvert) {
        String rValue = "";
        if (!isNullOrEmpty(stringToConvert)) {
            rValue = stringToConvert;
        }
        return rValue;
    }

    public static List<DmLocalSettingPrintMap_Mdl> getReportMapsUsingPrinterCode(String printerCode) throws Exception {
        List<DmLocalSettingPrintMap_Mdl> listResult = new ArrayList<>();
        try {
            listResult = cachedReportMapSettings.stream().filter(r -> r.getPrinterCode().equalsIgnoreCase(printerCode)).collect(Collectors.toList());
        } finally {
        }
        return listResult;
    }

    public static <T> String getJsonFromObject(Object obj, TypeToken<T> typeToken) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
            sb.append(gson.toJson(obj, typeToken.getType()));
        } finally {
        }
        return sb.toString();
    }

    public static <T> T getObjectFromJson(String jsonString, TypeToken<T> typeToken) throws Exception {
        T t = null;
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
            t = gson.fromJson(jsonString, typeToken.getType());
        } finally {

        }
        return t;
    }

    public static void savePrinterSettings(String jsonOfLocalSettings, Map<String, String> additionalParams) throws Exception {
        JVMLocalSetting_Mdl settings = getObjectFromJson(jsonOfLocalSettings, new TypeToken<JVMLocalSetting_Mdl>() {
        });
        CFunctions.savePrinterSettings(settings, additionalParams);
    }

    public static void copyFile(File sourceFile, File targetFile) throws Exception {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(targetFile);

            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLen = fis.read(BUFFEREDBYTES);
            while (readLen != -1) {
                fos.write(BUFFEREDBYTES, 0, readLen);
                readLen = fis.read(BUFFEREDBYTES);
            }
        } finally {
            if (fos != null) {
                fos.close();
                fos = null;
            }
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }

//    public static void saveMachineInterfaceSettings(String jsonOfLocalSettings, Map<String, String> additionalParams) throws Exception {
//        JVMLocalSetting_Mdl settings = getObjectFromJson(jsonOfLocalSettings, new TypeToken<JVMLocalSetting_Mdl>() {
//        });
//        CFunctions.saveMachineInterfaceSettings(settings, additionalParams);
//    }
//
//    public static String saveMachineInterfaceSettings(JVMLocalSetting_Mdl localSettings, Map<String, String> additionalParams) throws Exception {
//        String jsonOfJVMLocalSettings = "";
//        ObjectOutputStream oos = null;
//        FileOutputStream fos = null;
//        try {
//            JVMLocalSetting_Mdl savedLocalSettings = loadSavedSettings(false, null);
//            if (savedLocalSettings == null) {
//                savedLocalSettings = new JVMLocalSetting_Mdl();
//            }
//            savedLocalSettings.setExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString(), nullToEmpty(localSettings.getExtendedLocalSetting(ExtendedLocalSettings.MI_Configurations.toString())).trim());
//            fos = new FileOutputStream(getSettingsFile());
//            oos = new ObjectOutputStream(fos);
//            oos.writeObject(savedLocalSettings);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new Exception(ex);
//        } finally {
//            if (oos != null) {
//                oos.close();
//                oos = null;
//            }
//            if (fos != null) {
//                fos.close();
//                fos = null;
//            }
//            loadSavedSettings(true, additionalParams);
//            jsonOfJVMLocalSettings = getJsonFromObject(localSettings, new TypeToken<JVMLocalSetting_Mdl>() {
//            });
//        }
//        return jsonOfJVMLocalSettings;
//    }
    public static String savePrinterSettings(JVMLocalSetting_Mdl localSettings, Map<String, String> additionalParams) throws Exception {
        String jsonOfJVMLocalSettings = "";
        ObjectOutputStream oos = null;
        FileOutputStream fos = null;
        try {
            JVMLocalSetting_Mdl savedLocalSettings = loadSavedSettings(false, null);
            if (savedLocalSettings == null) {
                savedLocalSettings = new JVMLocalSetting_Mdl();
            }
            savedLocalSettings.setPrinterSettings(localSettings.getPrinterSettings());
            savedLocalSettings.setReportMapSettings(localSettings.getReportMapSettings());
            fos = new FileOutputStream(getSettingsFile());
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
            loadSavedSettings(true, additionalParams);
            jsonOfJVMLocalSettings = getJsonFromObject(localSettings, new TypeToken<JVMLocalSetting_Mdl>() {
            });
        }
        return jsonOfJVMLocalSettings;
    }

    public static void addLocalSettingsReloadedRefreshSubscriber(ILocalSettingsRefreshCommander refreshSubscriber) {
        if (mapCommandersForSettingsReloaded == null) {
            mapCommandersForSettingsReloaded = new HashMap<>();
        }
        mapCommandersForSettingsReloaded.put(refreshSubscriber.getClass().getCanonicalName(), refreshSubscriber);
    }

    public static void removeLocalSettingReloadedRefreshSubscriber(ILocalSettingsRefreshCommander refreshSubscriber) {
        if ((mapCommandersForSettingsReloaded != null) && (mapCommandersForSettingsReloaded.containsKey(refreshSubscriber.getClass().getCanonicalName()))) {
            mapCommandersForSettingsReloaded.remove(refreshSubscriber);
        }
    }

    public static void clearAllAttachedLocalSettingsReloadedRefreshSubscriber() {
        if (mapCommandersForSettingsReloaded != null) {
            mapCommandersForSettingsReloaded.clear();
        }
    }

    public static String getRuntimeDirectoryPath() throws Exception {
        return new File(CFunctions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getAbsolutePath() + File.separatorChar;
    }

    public static File getSettingsFile() throws Exception {
        return new File(getRuntimeDirectoryPath() + localSettingFileName);
    }

    public static File getPOSSettingsFile() throws Exception {
        return new File(getRuntimeDirectoryPath() + localPOSSettingFileName);
    }

    public static File getConfigurationFile() throws Exception {
        return new File(getRuntimeDirectoryPath() + localConfigurationFileName);
    }

    public static boolean isSettingsFileExists() throws Exception {
        return new File(getRuntimeDirectoryPath() + localSettingFileName).exists();
    }
    public static boolean isPOSSettingsFileExists() throws Exception {
        return new File(getRuntimeDirectoryPath() + localPOSSettingFileName).exists();
    }

    public static void initializeDmPlugIn(Map<String, String> additionalParams) throws Exception {
        loadSavedSettings(true, additionalParams);
    }

    public static Map<String, ILocalSettingsRefreshCommander> getAllAttachedLocalSettingsReloadedRefreshSubscriber() {
        return mapCommandersForSettingsReloaded;
    }

    public static JVMLocalSetting_Mdl loadSavedSettings(boolean refreshSettingsCache, Map<String, String> additionalParams) throws Exception {
        ObjectInputStream ois = null;
        FileInputStream fis = null;
        JVMLocalSetting_Mdl localSettings = null;
        try {
            File configFile = getSettingsFile();
            if (configFile.exists()) {
                fis = new FileInputStream(configFile);
                ois = new ObjectInputStream(fis);
                localSettings = (JVMLocalSetting_Mdl) ois.readObject();
                if (refreshSettingsCache) {
                    cachedPrinterSettings = localSettings.getPrinterSettings();
                    cachedReportMapSettings = localSettings.getReportMapSettings();
                }
            }
        } finally {
            if (ois != null) {
                ois.close();
                ois = null;
            }
            if (fis != null) {
                fis.close();
                fis = null;
            }

            if (mapCommandersForSettingsReloaded != null) {
                Iterator<String> iterator = mapCommandersForSettingsReloaded.keySet().iterator();
                while (iterator.hasNext()) {
                    String keyName = iterator.next();
                    ILocalSettingsRefreshCommander localSettingsRefreshCommander = mapCommandersForSettingsReloaded.get(keyName);
                    if (localSettingsRefreshCommander != null) {
                        try {
                            localSettingsRefreshCommander.executeAfterLocalSettingReloaded(localSettings, additionalParams);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return localSettings;
    }

    public static String getSavedSettings() throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            TypeToken<JVMLocalSetting_Mdl> typeToken = new TypeToken<JVMLocalSetting_Mdl>() {
            };

            sb.append(getJsonFromObject(loadSavedSettings(false, null), typeToken));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static String urlQueryForAction(String serverUrl, String action) {
        HttpURLConnection conn = null;
        String responseString = "";
        try {
            URL url = new URL(serverUrl + "?a=" + action.trim());

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            int rCode = conn.getResponseCode();

            if (rCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder buffer = new StringBuilder();
                String inLine;
                while ((inLine = reader.readLine()) != null) {
                    buffer.append(inLine);
                }
                reader.close();
                responseString = buffer.toString();
            }

        } catch (Exception ex) {
            responseString = "";
            ex.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return responseString.trim();
    }

    public static String getLocalMachineName() {
        if (localMachineName == null) {
            localMachineName = "Unknown";
            try {
                localMachineName = InetAddress.getLocalHost().getHostName();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return localMachineName;
    }

    public static String getLocalMachineIp() {
        if (localMachineIp == null) {
            localMachineIp = "Unknown";
            try {
                Enumeration< NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    final NetworkInterface cur = interfaces.nextElement();

                    if (cur.isLoopback()) {
                        continue;
                    }

                    for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                        final InetAddress inet_addr = addr.getAddress();

                        if (!(inet_addr instanceof Inet4Address)) {
                            continue;
                        }

                        localMachineIp = inet_addr.getHostAddress();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return localMachineIp;
    }

    public static void downloadPacInstallerFile(String serverUrl, String action, File fileLocation) throws Exception {
        HttpURLConnection conn = null;
        FileOutputStream fos = null;
        InputStream is = null;

        try {
            URL url = new URL(serverUrl + "?a=" + action.trim());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int rCode = conn.getResponseCode();

            if (rCode == HttpURLConnection.HTTP_OK) {
                byte[] BUFFER = new byte[1024 * 1024];
                fos = new FileOutputStream(fileLocation);
                is = conn.getInputStream();
                int readLen = is.read(BUFFER);
                while (readLen != -1) {
                    fos.write(BUFFER, 0, readLen);
                    readLen = is.read(BUFFER);
                }
            }

        } finally {
            if (is != null) {
                is.close();
                is = null;
            }
            if (fos != null) {
                fos.close();
                fos = null;
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
    }
}
