package dataman.dmToolkit.self.server;

import dataman.dmBase.self.server.ZipUtils;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmPACUpdateSetup_Srvlt extends HttpServlet {

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String showFileName = "";
            String zipFilePath = "";
            String sourceFolder = "";

            sourceFolder = DmServer.Path.pac;
            String tempDestFolder = DmServer.Path.sessions + "/" + req.getSession().getId();//sessionId;
            File tempFile = new File(tempDestFolder);
            tempFile.mkdirs();
            zipFilePath = preparePACPluginToDonwload(sourceFolder, tempDestFolder);
            sourceFolder = tempDestFolder;

//            sourceFolder = DmServer.Path.installer;
//            String tempDestFolder = DmServer.Path.sessions + "/" + req.getSession().getId() + File.separatorChar + UUID.randomUUID().toString();//sessionId;
//            File tempFile = new File(tempDestFolder);
//            tempFile.mkdirs();
//            zipFilePath = preparePACInstallerToDownload(sourceFolder, tempDestFolder);
//            sourceFolder = tempDestFolder;
//            showFileName = "pacInstaller.zip";
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
                OutputStream outputStream = resp.getOutputStream();
                while (readLen != -1) {
                    outputStream.write(BUFFER, 0, readLen);
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

    private String preparePACPluginToDonwload(String sourceFolder, String destFolder) throws Exception {
        File sourceFile = new File(sourceFolder);
        File destFile = new File(destFolder);
        copy(sourceFile, destFile);
//        createPluginConfigPropertiesFile(destFile, contextPath);
        File zipFile = new File(destFile, "plugin.zip");
        return zipFile.getAbsolutePath();
    }

//    private String preparePACInstallerToDownload(String sourceFolder, String destFolder) throws Exception {
//        File sourceFile = new File(sourceFolder);
//        File destFile = new File(destFolder);
//        copy(sourceFile, destFile);
//        File zipFile = new File(destFile, "installer.zip");
//        return zipFile.getAbsolutePath();
//    }
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
