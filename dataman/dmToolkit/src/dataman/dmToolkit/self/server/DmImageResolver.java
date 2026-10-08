package dataman.dmToolkit.self.server;

import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmSize;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coobird.thumbnailator.Thumbnails;

public class DmImageResolver {

    private String sessId;
    private String dirPath;
    private String fileName;
    private String ruleCode;
    private String mongoFileLink;

    public String getSessionId() {
        return sessId;
    }

    public void setSessionId(String sessId) {
        this.sessId = sessId;
    }

    public String getDirectoryPath() {
        return dirPath;
    }

    public void setDirectoryPath(String dirPath) {
        this.dirPath = dirPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getMongoFileLink() {
        return mongoFileLink;
    }

    public void setMongoFileLink(String mongoFileLink) {
        this.mongoFileLink = mongoFileLink;
    }

    public void processImageForServlet(int expectedWidth, int expectedHeight, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String extension = "";
        if ((mongoFileLink == null) || (mongoFileLink.equalsIgnoreCase(""))) {

            if ((dirPath == null) || (dirPath.equalsIgnoreCase(""))) {
                throw new Exception("directory not found.");
            }

            if ((fileName == null) || (fileName.equalsIgnoreCase(""))) {
                throw new Exception("fileName not found.");
            }

            String fullPath = dirPath + "/" + fileName;

            File file = new File(fullPath);

            if (!file.exists()) {
                throw new Exception("file doesn't exist.");
            }
            extension = DmShared.xNull(getExtension(fileName)).trim();

            if (extension.equalsIgnoreCase("")) {
                Thumbnails.of(Thumbnails.of(file).scale(1).outputFormat("jpg").asBufferedImage()).outputFormat("jpg").size(expectedWidth, expectedHeight).toOutputStream(resp.getOutputStream());
            } else {
                Thumbnails.of(Thumbnails.of(file).scale(1).outputFormat(extension).asBufferedImage()).outputFormat(extension).size(expectedWidth, expectedHeight).toOutputStream(resp.getOutputStream());
            }

        } else {
            if ((ruleCode == null) || (ruleCode.equalsIgnoreCase(""))) {
                throw new Exception("file rule code not found.");
            }

            DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));

            GridFSDBFile fsDBFile = fileManagerMongo.getFileFromFolder(ruleCode, mongoFileLink);
            if (fsDBFile != null) {
                String imageMimeType = req.getServletContext().getMimeType(fsDBFile.getFilename());//new javax.activation.MimetypesFileTypeMap().getContentType(fsDBFile.getFilename());
                String[] mimeSplit = imageMimeType.split("/");
                if (mimeSplit[0].equalsIgnoreCase("image")) {
                    Thumbnails.of(Thumbnails.of(fsDBFile.getInputStream()).scale(1).outputFormat(getExtension(fsDBFile.getFilename())).asBufferedImage()).outputFormat(getExtension(fsDBFile.getFilename())).size(expectedWidth, expectedHeight).toOutputStream(resp.getOutputStream());
                } else {
                    resp.setContentType("text/html");
                }
            } else {
                resp.setContentType("text/html");
            }
        }
    }

    public DmSize resolveImageForSize(int expectedWidth, int expectedHeight, DmRSS req) throws Exception {
        DmSize imgSize = new DmSize(expectedWidth, expectedHeight);
        if ((mongoFileLink == null) || (mongoFileLink.equalsIgnoreCase(""))) {

            if ((dirPath == null) || (dirPath.equalsIgnoreCase(""))) {
                throw new Exception("directory not found.");
            }

            if ((fileName == null) || (fileName.equalsIgnoreCase(""))) {
                throw new Exception("fileName not found.");
            }

            String fullPath = dirPath + "/" + fileName;

            File file = new File(fullPath);

            if (!file.exists()) {
                throw new Exception("file doesn't exist.");
            }

            BufferedImage bImage = Thumbnails.of(file).scale(1).outputFormat(getExtension(fileName)).asBufferedImage();
            imgSize = new DmSize(bImage.getWidth(), bImage.getHeight());
            bImage = Thumbnails.of(bImage).size(expectedWidth, expectedHeight).asBufferedImage();
            imgSize.setChangedWidth(bImage.getWidth());
            imgSize.setChangedHeight(bImage.getHeight());
            bImage.flush();
        } else {
            if ((ruleCode == null) || (ruleCode.equalsIgnoreCase(""))) {
                throw new Exception("file rule code not found.");
            }

            DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));

            GridFSDBFile fsDBFile = fileManagerMongo.getFileFromFolder(ruleCode, mongoFileLink);
            if (fsDBFile != null) {
                String imageMimeType = req.getServletContext().getMimeType(fsDBFile.getFilename());//new javax.activation.MimetypesFileTypeMap().getContentType(fsDBFile.getFilename());
                String[] mimeSplit = imageMimeType.split("/");
                if (mimeSplit[0].equalsIgnoreCase("image")) {
                    BufferedImage bImage = Thumbnails.of(fsDBFile.getInputStream()).scale(1).outputFormat(getExtension(fsDBFile.getFilename())).asBufferedImage();
                    imgSize = new DmSize(bImage.getWidth(), bImage.getHeight());
                    bImage = Thumbnails.of(bImage).size(expectedWidth, expectedHeight).asBufferedImage();
                    imgSize.setChangedWidth(bImage.getWidth());
                    imgSize.setChangedHeight(bImage.getHeight());
                    bImage.flush();
                }
            }
        }
        return imgSize;
    }

    private String getExtension(String fileName) {
        String ext = "";
        int index = -1;
        try {
            index = fileName.lastIndexOf(".");
            if (index >= 0) {
                ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ext.trim();
    }

    public DmSize getSizeOfImage(DmRSS req) throws Exception {
        DmSize imgSize = new DmSize();
        if ((mongoFileLink == null) || (mongoFileLink.equalsIgnoreCase(""))) {

            if ((dirPath == null) || (dirPath.equalsIgnoreCase(""))) {
                throw new Exception("directory not found.");
            }

            if ((fileName == null) || (fileName.equalsIgnoreCase(""))) {
                throw new Exception("fileName not found.");
            }

            String fullPath = dirPath + "/" + fileName;

            File file = new File(fullPath);

            if (!file.exists()) {
                throw new Exception("file doesn't exist.");
            }

            BufferedImage bImage = Thumbnails.of(file).scale(1).outputFormat(getExtension(fileName)).asBufferedImage();
            imgSize = new DmSize(bImage.getWidth(), bImage.getHeight());
            bImage.flush();
            bImage = null;
        } else {
            if ((ruleCode == null) || (ruleCode.equalsIgnoreCase(""))) {
                throw new Exception("file rule code not found.");
            }

            DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));

            GridFSDBFile fsDBFile = fileManagerMongo.getFileFromFolder(ruleCode, mongoFileLink);
            if (fsDBFile != null) {
                String imageMimeType = req.getServletContext().getMimeType(fsDBFile.getFilename());//new javax.activation.MimetypesFileTypeMap().getContentType(fsDBFile.getFilename());
                String[] mimeSplit = imageMimeType.split("/");
                if (mimeSplit[0].equalsIgnoreCase("image")) {
                    BufferedImage bImage = Thumbnails.of(fsDBFile.getInputStream()).scale(1).outputFormat(getExtension(fsDBFile.getFilename())).asBufferedImage();
                    imgSize = new DmSize(bImage.getWidth(), bImage.getHeight());
                    bImage.flush();
                    bImage = null;
                }
            }
        }
        return imgSize;
    }

}
