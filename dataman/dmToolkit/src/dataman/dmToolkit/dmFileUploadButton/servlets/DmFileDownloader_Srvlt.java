package dataman.dmToolkit.dmFileUploadButton.servlets;

import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmSession.server.DmServletPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmFileDownloader_Srvlt extends DmServletPrivate {

    private final int READ_BUFFER_SIZE = 2 * 1024 * 1024;

    public static final String SERVLET_PATH = "/" + DmFileDownloader_Srvlt.class.getCanonicalName().replace(".", "/");

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String sessionId = "", fileName = "", directoryPath = "", mongoFileLink = "", fileRuleCode = "";
            byte[] byteBuffer = new byte[READ_BUFFER_SIZE];
            int readLength = -1;

            sessionId = req.getParameter("u");
            directoryPath = req.getParameter("p");
            fileName = req.getParameter("f");
            fileRuleCode = req.getParameter("r");
            mongoFileLink = req.getParameter("m");

            if ((sessionId == null) || (sessionId.equalsIgnoreCase(""))) {
                throw new Exception("session not found.");
            }

            if ((mongoFileLink == null) || (mongoFileLink.equalsIgnoreCase(""))) {
                if ((directoryPath == null) || (directoryPath.equalsIgnoreCase(""))) {
                    throw new Exception("directory not found.");
                }

                if ((fileName == null) || (fileName.equalsIgnoreCase(""))) {
                    throw new Exception("fileName not found.");
                }

                String fullPath = directoryPath + "/" + fileName;

                File file = new File(fullPath);

                if (!file.exists()) {
                    throw new Exception("file doesn't exist.");
                }

                String mimeType = this.getServletContext().getMimeType(fullPath);
                resp.setContentType(mimeType);
                resp.setHeader("Accept", mimeType);

//                resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fileName + "\"; filename*=UTF-8''" + fileName);
                resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fileName + "\"; filename*=\"UTF-8''" + fileName + "\";");
                FileInputStream fis = null;
                try {

                    fis = new FileInputStream(file);

                    readLength = fis.read(byteBuffer);
                    while (readLength != -1) {
                        if (readLength < READ_BUFFER_SIZE) {
                            resp.getOutputStream().write(byteBuffer, 0, readLength);
                        } else if (readLength >= READ_BUFFER_SIZE) {
                            resp.getOutputStream().write(byteBuffer);
                        }
                        readLength = fis.read(byteBuffer);
                    }

                } catch (Exception ex) {
                    throw ex;
                } finally {
                    if (fis != null) {
                        fis.close();
                        fis = null;
                    }
                }

            } else {
                if ((fileRuleCode == null) || (fileRuleCode.equalsIgnoreCase(""))) {
                    throw new Exception("file rule code not found.");
                }

                DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));

                GridFSDBFile fsDBFile = fileManagerMongo.getFileFromFolder(fileRuleCode, mongoFileLink);

                if (fsDBFile != null) {

                    String mimeType = this.getServletContext().getMimeType(fsDBFile.getFilename());
                    resp.setContentType(mimeType);
//                    resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fsDBFile.getFilename() + "\"; filename*=UTF-8''" + fsDBFile.getFilename());
                    resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fsDBFile.getFilename() + "\"; filename*=\"UTF-8''" + fsDBFile.getFilename() + "\";");
                    InputStream inputStream = null;

                    try {
                        inputStream = fsDBFile.getInputStream();

                        readLength = inputStream.read(byteBuffer);
                        while (readLength != -1) {
                            if (readLength < READ_BUFFER_SIZE) {
                                resp.getOutputStream().write(byteBuffer, 0, readLength);
                            } else if (readLength >= READ_BUFFER_SIZE) {
                                resp.getOutputStream().write(byteBuffer);
                            }
                            readLength = inputStream.read(byteBuffer);
                        }
                    } catch (Exception e) {

                    } finally {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }

                } else {
                    resp.setContentType("text/html");
                }

            }

        } catch (Exception ex) {

        } finally {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
