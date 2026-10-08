package dataman.stPlugin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.jvmSerialization.shared.JVMLocalSetting_Mdl;
import dataman.dmPlugin.server.util.ServerRunner;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmPlugin.CFunctions;
import dataman.dmPlugin.resources.DmPlugInResources;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class STPluginServer {

    private static final ExecutorService cachedExecutors = Executors.newCachedThreadPool();

    public static ExecutorService getCachedThreads() {
        return cachedExecutors;
    }

    public static void main(String[] args) {
        try {
            if (args != null && args.length > 0) {
                if (args[0].equalsIgnoreCase("update")) {
                    updateFilesFromServer();
                } else if (args[0].equalsIgnoreCase("copy")) {
                    copyDATFile();
                }

            } else {
                start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void start() {
        try {
            System.setProperty("-Dsun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

            if (!SystemTray.isSupported()) {
                System.out.println("System tray not supported..");
                System.exit(1);
            }

            File iniFile = CFunctions.getConfigurationFile();

            System.out.println("Ini Path : " + iniFile.getCanonicalPath());

            Properties props = new Properties();
            props.load(new FileReader(iniFile));

            String serverUrl = props.getProperty("serverurl");
            CFunctions.setServerApplicationUrl(serverUrl);
            if (serverUrl.trim().endsWith("/") || serverUrl.trim().endsWith("\\")) {

            } else {
                serverUrl += "/";
            }
            serverUrl += "dataman/dmToolkit/self/server/DmPlugin_Srvlt";

            int webPort = Integer.parseInt(CFunctions.urlQueryForAction(serverUrl, DmEnum.DmPluginQueryActions.PACPortWeb.toString()));

            STPlugin_Router.webPortNumber = webPort;
            ServerRunner.setServer(STPlugin_Router.class);

            PopupMenu popup = new PopupMenu();

            final MenuItem updateMenu = new MenuItem("Update");
            final MenuItem restartMenu = new MenuItem("Re-Start");
            final MenuItem currentDirectoryMenu = new MenuItem("Current Directory");
            final MenuItem exitMenu = new MenuItem("Exit");

            currentDirectoryMenu.setEnabled(true);
            currentDirectoryMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        frmCurrentDirectory frm = new frmCurrentDirectory(CFunctions.getRuntimeDirectoryPath());
                        frm.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            updateMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (showConfirmation("Are you sure? You want to update plugin application.") == JOptionPane.YES_OPTION) {
                            String jsonFile = "localSetting.json";
                            String sourcePath = new File(STPluginServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
                            System.out.println("Path - " + sourcePath);
                            sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf("\\"));

                            /*=========================<< Copy files into backup folder >>=============================*/
                            File sourceFile = new File(sourcePath);
                            File destinationFile = new File(sourcePath + File.separatorChar + "backup");
                            destinationFile.delete();
                            destinationFile.mkdir();
                            copyFolder(sourceFile, destinationFile);

                            /*=========================<< Save Setting.json file >>=============================*/
                            String json = CFunctions.getSavedSettings();
                            String tempJsonFile = sourcePath + File.separatorChar + "backup" + File.separatorChar + "lib" + File.separatorChar + jsonFile;
                            saveLocalSettingJSONFile(json, tempJsonFile);

                            /*=========================<< Start other jar file (from backup folder) >>=============================*/
                            Runtime.getRuntime().exec(" java -jar " + destinationFile.getPath() + File.separatorChar + "stPlugin.jar update");
                            System.exit(0);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            restartMenu.setEnabled(true);
            restartMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (showConfirmation("Are you sure to restart the server..?") == JOptionPane.YES_OPTION) {
                            restartMenu.setEnabled(false);
                            if (!ServerRunner.stopServer()) {
                                new JOptionPane("Unable to stop Services...", JOptionPane.ERROR, JOptionPane.OK_OPTION).show();
                            }
                            if (!ServerRunner.startServer()) {
                                new JOptionPane("Unable to start Services...", JOptionPane.ERROR, JOptionPane.OK_OPTION).show();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        restartMenu.setEnabled(true);
                    }
                }
            });

            exitMenu.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (showConfirmation("Are you sure to exit from Native Plugin Application..?") == JOptionPane.YES_OPTION) {
                        System.exit(1);
                    }
                }
            });

            popup.add(updateMenu);
            popup.add(restartMenu);
            popup.add(currentDirectoryMenu);
            popup.add(exitMenu);

            URL imgUrl = DmPlugInResources.getResourceURL(DmPlugInResources.DmPluginResourceType.Images, "dataman.png");
            Image img = Toolkit.getDefaultToolkit().createImage(imgUrl);
            double iH = SystemTray.getSystemTray().getTrayIconSize().getHeight();
            double iW = SystemTray.getSystemTray().getTrayIconSize().getWidth();
            Image iImage = img.getScaledInstance((int) iW, (int) iH, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(iImage);
            TrayIcon icon = new TrayIcon(imageIcon.getImage(), "", popup);
            icon.setImageAutoSize(true);
            SystemTray.getSystemTray().add(icon);

            CFunctions.addLocalSettingsReloadedRefreshSubscriber(new MiStartUp());

            if (!ServerRunner.startServer()) {
                new JOptionPane("Unable to start Services...", JOptionPane.ERROR, JOptionPane.OK_OPTION).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateFilesFromServer() {
        try {
            String tempPath, destinationPath;

            System.setProperty("-Dsun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");

            if (!SystemTray.isSupported()) {
                System.out.println("System tray not supported..");
                System.exit(1);
            }

            File iniFile = CFunctions.getConfigurationFile();

            System.out.println("Ini Path : " + iniFile.getCanonicalPath());

            Properties props = new Properties();
            props.load(new FileReader(iniFile));

            String serverUrl = props.getProperty("serverurl");
            CFunctions.setServerApplicationUrl(serverUrl);
            if (serverUrl.trim().endsWith("/") || serverUrl.trim().endsWith("\\")) {

            } else {
                serverUrl += "/";
            }

            destinationPath = new File(STPluginServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();

            /*=========================<< Check weather backup folder exist or not >>=============================*/
            if (destinationPath != null && destinationPath.contains("backup")) {
                destinationPath = destinationPath.substring(0, destinationPath.lastIndexOf("backup") - 1);

                tempPath = destinationPath;

                System.out.println("Destination path : " + destinationPath);

                File tempFile = new File(tempPath);
                /*==================<< Delete old files >>=====================*/
                for (File file : tempFile.listFiles()) {
                    if (!file.getName().equalsIgnoreCase("backup")) {
                        file.delete();
                    }
                }

                tempPath = destinationPath + File.separatorChar + "STPlugin.zip";

                File downloadFile = new File(tempPath);

                /*==================<< Download files from server >>=====================*/
                serverUrl += "dataman/dmToolkit/self/server/DmPACUpdateSetup_Srvlt";
                CFunctions.downloadPacInstallerFile(serverUrl, "", downloadFile);

                unzip(downloadFile, destinationPath);

                System.out.println("file copied");

                /*==================<< Copy dmConfig.properties file >>=====================*/
                tempPath = destinationPath + File.separatorChar + "lib" + File.separatorChar + "dmConfig.properties";
                File destinationFile = new File(tempPath);

                tempPath = destinationPath + File.separatorChar + "backup" + File.separatorChar + "lib" + File.separatorChar + "dmConfig.properties";
                File sourceFile = new File(tempPath);

                destinationFile.delete();
                copyFolder(sourceFile, destinationFile);
                System.out.println("dmConfig.properties copied.");

                /*=========================<< Start other jar file (from backup folder) >>=============================*/
                Runtime.getRuntime().exec(" java -jar " + destinationPath + File.separatorChar + "stPlugin.jar copy");
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyDATFile() {
        try {
            String tempPath;
            String sourcePath = new File(STPluginServer.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            sourcePath = sourcePath.substring(0, sourcePath.lastIndexOf("\\"));

            /*==================<< Copy localsetting.dat file >>=====================*/
            tempPath = sourcePath + File.separatorChar + "backup" + File.separatorChar + "lib" + File.separatorChar + "localSetting.json";
            BufferedReader br = new BufferedReader(new FileReader(tempPath));
            Type listType = new TypeToken<JVMLocalSetting_Mdl>() {
            }.getType();
            JVMLocalSetting_Mdl localSetting_Mdl = new Gson().fromJson(br, listType);

            tempPath = sourcePath + File.separatorChar + "lib" + File.separatorChar + "localsetting.dat";
            File localsettingFile = new File(tempPath);
            if (localsettingFile.exists()) {
                localsettingFile.delete();
            }
            saveLocalSettingDATFile(localSetting_Mdl, localsettingFile);
            System.out.println("localsetting.dat copied.");

            /*=========================<< Deletebackup folder >>=============================*/
            tempPath = sourcePath + File.separatorChar + "backup";
            File backupFile = new File(tempPath);
            if (backupFile.exists()) {
                backupFile.delete();
            }

            /*=========================<< Start other jar file (from backup folder) >>=============================*/
            Runtime.getRuntime().exec(" java -jar " + sourcePath + File.separatorChar + "stPlugin.jar");
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void copyFolder(File src, File dest) throws IOException {
        try {
            if (src.isDirectory()) {
                //if directory not exists, create it
                if (!dest.exists()) {
                    dest.mkdir();
                    System.out.println("Directory copied from " + src + "  to " + dest);
                }

                //list all the directory contents
                String files[] = src.list();

                for (String file : files) {
                    if (!file.equalsIgnoreCase("backup")) {
                        //construct the src and dest file structure
                        File srcFile = new File(src, file);
                        File destFile = new File(dest, file);
                        //recursive copy
                        copyFolder(srcFile, destFile);
                    }
                }

            } else {
                //if file, then copy it
                //Use bytes stream to support all file types
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes 
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }

                in.close();
                out.close();
                System.out.println("File copied from " + src + " to " + dest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveLocalSettingJSONFile(String jsonData, String fileName) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {
            fw = new FileWriter(fileName);
            bw = new BufferedWriter(fw);
            bw.write(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
                if (fw != null) {
                    fw.close();
                    fw = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void saveLocalSettingDATFile(JVMLocalSetting_Mdl localSetting_Mdl, File localsettingFile) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(localsettingFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(localSetting_Mdl);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                    oos = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void unzip(File zipFilePath, String destDir) throws Exception {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int showConfirmation(String messageToShow) {
        return JOptionPane.showConfirmDialog(null, messageToShow, "Dataman", JOptionPane.YES_NO_OPTION);
    }

}
