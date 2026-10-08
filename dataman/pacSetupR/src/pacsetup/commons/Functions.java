package pacsetup.commons;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Functions {

    private static SystemProperties systemProperties;
    private static Properties configProperties;
    private static final String fileName = "config.properties";
    private static ExecutorService executors = Executors.newCachedThreadPool();

    public static void setSystemProperties(SystemProperties properties) {
        systemProperties = properties;
    }

    public static SystemProperties getSystemProperties() {
        return systemProperties;
    }

    public static void writeException(Exception ex) {
        ex.printStackTrace();
    }

    public static boolean deleteFile(String fullFilePath) {
        boolean bool = false;
        try {
            File fileToDelete = new File(fullFilePath);
            if (!fileToDelete.exists()) {
                throw new Exception("File not exists : " + fullFilePath);
            }

            if (fileToDelete.isDirectory()) {
                deleteDirectoryFiles(fileToDelete);
            }

            bool = fileToDelete.delete();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bool;
    }

    private static boolean deleteDirectoryFiles(File folderFile) throws Exception {
        boolean bool = false;
        File[] folderFiles = folderFile.listFiles();
        for (int i = 0; i < folderFiles.length; i++) {
            if (folderFiles[i].isDirectory()) {
                deleteDirectoryFiles(folderFiles[i]);
            }
            bool = folderFiles[i].delete();
        }
        return bool;
    }

    public static String getRuntimeDirectoryLocation() throws Exception {
        return new File(Functions.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getAbsolutePath() + File.separatorChar;
    }

    public static void readApplicationPropertiesFile() throws Exception {
        String runtimeDirectory = getRuntimeDirectoryLocation();
        String fullConfigPath = runtimeDirectory + fileName;
        configProperties = new Properties();
        configProperties.load(new FileInputStream(new File(fullConfigPath)));
    }

    public static Point getCenterOfScreenPoint(Dimension size) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int diffWidth = (int) Math.floor((screenSize.getWidth() - size.getWidth()) / 2);
        int diffHeight = (int) Math.floor((screenSize.getWidth() - size.getWidth()) / 2);
        return new Point(diffWidth, diffHeight);
    }

    public static String getInstallationDirectory() {
        return getSystemProperties().getUser_Directory() + File.separatorChar + "DmPac";
    }

    public static String getConfigurationProperty(String key) throws Exception {
        if (configProperties == null) {
            throw new Exception("Configuration Properties not loaded...");
        }

        return configProperties.getProperty(key, "");
    }

    public static ExecutorService getCachedExecutors() {
        return executors;
    }
}
