package dataman.dmToolkitR.self.server;

import dataman.dmBase.self.server.ZipUtils;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmPACSetup_Srvlt extends HttpServlet {

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String command = req.getParameter("cmd");
            String showFileName = "";
            String zipFilePath = "";
            String sourceFolder = "";

            if (DmShared.xNull(command).equalsIgnoreCase("jdk8x64")) {

                sourceFolder = DmServer.Path.pacSetup + "/jdk8/x64";
                zipFilePath = DmServer.Path.sessions + "/" + req.getSession().getId();
                new File(zipFilePath).mkdirs();
                zipFilePath += "/" + UUID.randomUUID().toString() + ".zip";
                showFileName = "jdk8x64.zip";

            } else if (DmShared.xNull(command).equalsIgnoreCase("jdk8x32")) {
                sourceFolder = DmServer.Path.pacSetup + "/jdk8/x32";
                zipFilePath = DmServer.Path.sessions + "/" + req.getSession().getId();
                new File(zipFilePath).mkdirs();
                zipFilePath += "/" + UUID.randomUUID().toString() + ".zip";
                showFileName = "jdk8x32.zip";

            } else if (DmShared.xNull(command).equalsIgnoreCase("downloadInstaller")) {
                String contextUrl = DmShared.xNull(req.getParameter("cPath"));
                sourceFolder = DmServer.Path.installer;
                String tempDestFolder = DmServer.Path.sessions + "/" + req.getSession().getId() + File.separatorChar + UUID.randomUUID().toString();//sessionId;
                File tempFile = new File(tempDestFolder);
                tempFile.mkdirs();
                zipFilePath = preparePACInstallerToDownload(sourceFolder, tempDestFolder, contextUrl);
                sourceFolder = tempDestFolder;
                showFileName = "pacInstaller.zip";

            } else if (DmShared.xNull(command).equalsIgnoreCase("installFonts")) {
                sourceFolder = DmServer.Path.fonts;
                zipFilePath = DmServer.Path.sessions + "/" + req.getSession().getId() + File.separatorChar + UUID.randomUUID().toString();//sessionId;
                new File(zipFilePath).mkdirs();
                zipFilePath += "/" + UUID.randomUUID().toString() + ".zip";

            } else if (DmShared.xNull(command).equalsIgnoreCase("installPlugin")) {
                String contextUrl = DmShared.xNull(req.getParameter("cPath"));
                sourceFolder = DmServer.Path.pac;
                String tempDestFolder = DmServer.Path.sessions + "/" + req.getSession().getId();//sessionId;
                File tempFile = new File(tempDestFolder);
                tempFile.mkdirs();
                zipFilePath = preparePACPluginToDonwload(sourceFolder, tempDestFolder, contextUrl);
                sourceFolder = tempDestFolder;
            }

            ZipUtils zipUtils = new ZipUtils(sourceFolder, zipFilePath);
            zipUtils.zipIt();

            resp.setContentType("application/octet-stream");
            resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + showFileName + "\"; filename*=UTF-8''" + showFileName);

            File zipFile = new File(zipUtils.getOutput_Zip_File());
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(zipFile);
                byte[] BUFFER = new byte[1024 * 1024];
                int readLen = fis.read(BUFFER);
                while (readLen != -1) {
                    resp.getOutputStream().write(BUFFER, 0, readLen);
                    readLen = fis.read(BUFFER);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private String preparePACPluginToDonwload(String sourceFolder, String destFolder, String contextPath) throws Exception {
        File sourceFile = new File(sourceFolder);
        File destFile = new File(destFolder);
        copy(sourceFile, destFile);
        createPluginConfigPropertiesFile(destFile, contextPath);
        File zipFile = new File(destFile, "plugin.zip");
        return zipFile.getAbsolutePath();
    }

    private String preparePACInstallerToDownload(String sourceFolder, String destFolder, String contextPath) throws Exception {
        File sourceFile = new File(sourceFolder);
        File destFile = new File(destFolder);
        copy(sourceFile, destFile);
        createInstallerConfigPropertiesFile(destFile, contextPath);
        File zipFile = new File(destFile, "installer.zip");
        return zipFile.getAbsolutePath();
    }

//    private void createPluginConfigPropertiesFile(File destFolder, String contextPath) throws Exception {
//        File configFile = new File(destFolder, "dmConfig.properties");
//        FileWriter writer = new FileWriter(configFile);
//        writer.write("serverurl=" + contextPath);
//        writer.close();
//    }
    private void createPluginConfigPropertiesFile(File destFolder, String contextPath) throws Exception {
        File configFileDirectory = new File(destFolder, "lib");
        if (!configFileDirectory.exists()) {
            configFileDirectory.mkdir();
        }
        File configFile = new File(configFileDirectory, "dmConfig.properties");
        FileWriter writer = new FileWriter(configFile);
        writer.write("serverurl=" + contextPath);
        writer.close();
    }

    private void createInstallerConfigPropertiesFile(File destFolder, String contextPath) throws Exception {
        File configFile = new File(destFolder, "config.properties");
        FileWriter writer = new FileWriter(configFile);
        writer.write("service_url=" + contextPath);
        writer.close();
    }

    private void copy(File sourceLocation, File targetLocation) throws Exception {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private void copyDirectory(File source, File target) throws Exception {
        if (!target.exists()) {
            target.mkdir();
        }

        for (String f : source.list()) {
            copy(new File(source, f), new File(target, f));
        }
    }

    private void copyFile(File source, File target) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(target);
            byte[] buf = new byte[1024 * 1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
            if (out != null) {
                out.close();
                out = null;
            }
        }
    }
}
