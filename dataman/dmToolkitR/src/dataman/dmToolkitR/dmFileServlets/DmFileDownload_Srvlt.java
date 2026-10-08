package dataman.dmToolkitR.dmFileServlets;

import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmSession.server.DmServletPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DmFileDownload_Srvlt extends DmServletPrivate {

    private final int READ_BUFFER_SIZE = 2 * 1024 * 1024;

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        byte[] byteBuffer;
        int readLength;
        String sessionId = "", resourceAlocationId = "";
        try {

            sessionId = req.getParameter("u");
            resourceAlocationId = req.getParameter("r");

            if (DmShared.xNull(sessionId).equalsIgnoreCase("")) {
                throw new Exception("Invalid requested.");
            }

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new Exception("Invalid requested.");
            }

            if (!DmSession.getSessions().get(sessionId).constainsValueName(resourceAlocationId)) {
                throw new Exception("Invalid requested.");
            }

            DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));
            DmAttachment_Mdl attachment = (DmAttachment_Mdl) DmSession.getSessions().get(sessionId).getAttribute(resourceAlocationId);
            GridFSDBFile fsDBFile = fileManagerMongo.getFileFromFolder(attachment.getRuleCode(), attachment.getFileLink());

            if (fsDBFile != null) {
                InputStream inputStream = null;
                try {
                    inputStream = fsDBFile.getInputStream();
                    String mimeType = this.getServletContext().getMimeType(fsDBFile.getFilename());
                    resp.setContentType(mimeType);
                    resp.setHeader("Accept", mimeType);
//                    resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fsDBFile.getFilename() + "\"; filename*=UTF-8''" + fsDBFile.getFilename());
                    resp.setHeader("Content-Disposition", "attachment;filename=" + "\"" + fsDBFile.getFilename() + "\"; filename*=\"UTF-8''" + fsDBFile.getFilename() + "\";");
                    byteBuffer = new byte[READ_BUFFER_SIZE];
                    readLength = inputStream.read(byteBuffer);
                    while (readLength != -1) {
                        if (readLength >= READ_BUFFER_SIZE) {
                            resp.getOutputStream().write(byteBuffer);
                        } else if (readLength < READ_BUFFER_SIZE) {
                            resp.getOutputStream().write(byteBuffer, 0, readLength);
                        }
                        readLength = inputStream.read(byteBuffer);
                    }
                } catch (Exception ex) {
                    throw new Exception(ex.getMessage());
                } finally {
                    inputStream.close();
                    inputStream = null;
                }
            }

        } catch (Exception ex) {
        } finally {
            resp.setStatus(HttpServletResponse.SC_OK);
            DmSession.getSessions().get(sessionId).removeAttribute(resourceAlocationId);
        }
    }
}
