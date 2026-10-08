package dataman.dmToolkit.dmFileUploadButton.servlets;

import dataman.dmBase.dmSession.server.DmServletPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class DmFileUploadButton_Srvlt extends DmServletPrivate {

    private int READ_BUFFER_SIZE = 1 * 1024 * 1024;

    public static final String SERVLET_PATH = "/" + DmFileUploadButton_Srvlt.class.getCanonicalName().replace(".", "/");

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte[] byteBuffer;
        String sessionId = "", fileName = "";
        try {
            sessionId = req.getParameter("u");
            fileName = req.getParameter("f");

            if (DmShared.xNull(sessionId).equalsIgnoreCase("")) {
                throw new Exception("Invalid requested.");
            }

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new Exception("Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new Exception("File Name not given.");
            }

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);

            String directoryPath = DmServer.Path.sessions + "/" + sessionId;
            String serverFileName = UUID.randomUUID().toString() + fileName;
            String fullFilePath = directoryPath + "/" + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }

            sFile = new File(fullFilePath);

            List<FileItem> files = fileUpload.parseRequest(req);
            for (FileItem file : files) {
                if (!file.isFormField()) {
                    byteBuffer = new byte[READ_BUFFER_SIZE];
                    InputStream inputStream = file.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(sFile);
                    try {
                        int readLength = inputStream.read(byteBuffer, 0, READ_BUFFER_SIZE);
                        while (readLength != -1) {
                            if (readLength >= this.READ_BUFFER_SIZE) {
                                outputStream.write(byteBuffer);
                            } else if (readLength < this.READ_BUFFER_SIZE) {
                                outputStream.write(byteBuffer, 0, readLength);
                            }
                            readLength = inputStream.read(byteBuffer);
                        }
                    } catch (Exception exc) {
                    } finally {
                        inputStream.close();
                        outputStream.close();
                    }
                }
            }

            resp.getOutputStream().print(directoryPath + "|" + fileName + "|" + serverFileName);//prepareClientMessage(directoryPath, fileName));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
