package dataman.dmToolkit.dmFileServlets;

import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmSession.server.DmServletPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;

public class DmImageFileThumbnail_Srvlt extends DmServletPrivate {

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        String sessionId = "", resourceAlocationId = "";

        try {
            sessionId = req.getParameter("u");
            resourceAlocationId = req.getParameter("r");
            String width = req.getParameter("w");
            String height = req.getParameter("h");

            if (DmShared.xNull(sessionId).equalsIgnoreCase("")) {
                throw new Exception("Invalid requested.");
            }

            if (DmShared.xNull(resourceAlocationId).equalsIgnoreCase("")) {
                throw new Exception("Invalid request.");
            }

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new Exception("Invalid request.");
            }

            if (!DmSession.getSessions().get(sessionId).constainsValueName(resourceAlocationId)) {
                throw new Exception("Invalid request.");
            }

            DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));
            DmAttachment_Mdl attachment = (DmAttachment_Mdl) DmSession.getSessions().get(sessionId).getAttribute(resourceAlocationId);
            GridFSDBFile fsDBFile = fileManagerMongo.getFileFromFolder(attachment.getRuleCode(), attachment.getFileLink());
            if (fsDBFile != null) {
                String fileName = fsDBFile.getFilename();
                if (DmShared.xNull(fileName).trim().equals("")) {
                    fileName = attachment.getFileName();
                }
                String imageMimeType = this.getServletContext().getMimeType(fileName);
                if (!DmShared.xNull(imageMimeType).trim().equals("")) {
                    String[] mimeSplit = imageMimeType.split("/");
                    if (mimeSplit[0].equalsIgnoreCase("image")) {
                        Thumbnails.of(Thumbnails.of(fsDBFile.getInputStream()).scale(1).outputFormat("jpeg").asBufferedImage()).outputFormat("jpeg").size(DmShared.iNull(width).intValue(), DmShared.iNull(height).intValue()).toOutputStream(resp.getOutputStream());
                    } else {
                        resp.setContentType("text/html");
                    }
                } else {
                    resp.setContentType("text/html");
                }
            } else {
                resp.setContentType("text/html");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resp.setStatus(HttpServletResponse.SC_OK);
            DmSession.getSessions().get(sessionId).removeAttribute(resourceAlocationId);
        }
    }
}
