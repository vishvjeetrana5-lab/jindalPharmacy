package dataman.dmToolkitR.dmFileServlets;

import com.mongodb.gridfs.GridFSInputFile;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmServletPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class DmFileUpload_Srvlt extends DmServletPrivate {

    private int READ_BUFFER_SIZE = 1 * 1024 * 1024;

    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        byte[] byteBuffer;
        String sessionId = "", resourceAlocationId = "", fileLinkId = "", fileName = "", ruleCode = "";

        try {
            sessionId = req.getParameter("u");
//            resourceAlocationId = req.getParameter("r");

            if (DmShared.xNull(sessionId).equalsIgnoreCase("")) {
                throw new Exception("Invalid requested.");
            }

//            if (DmShared.xNull(resourceAlocationId).equalsIgnoreCase("")) {
//                throw new Exception("Invalid request.");
//            }
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new Exception("Invalid request.");
            }

//            if (!DmSession.getSessions().get(sessionId).constainsValueName(resourceAlocationId)) {
//                throw new Exception("Invalid request.");
//            }
            String serverFileData = "";
            DmAttachment_Mdl attachment = null;

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            Map<String, List<FileItem>> paramsMap = fileUpload.parseParameterMap(req);

            List<FileItem> listFileId = paramsMap.get("fileId");
            if (listFileId != null) {
                FileItem fileId = listFileId.get(0);
                if (fileId.isFormField()) {
                    resourceAlocationId = new String(fileId.get());
                    attachment = (DmAttachment_Mdl) DmSession.getSessions().get(sessionId).getAttribute(resourceAlocationId);

                    DmFileManagerMongo fileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req));

                    if (attachment != null) {

                        fileName = attachment.getFileName();
                        ruleCode = attachment.getRuleCode();

                        listFileId = paramsMap.get("FileData");
                        FileItem uploadedFile = listFileId.get(0);

                        if (!uploadedFile.isFormField()) {
                            GridFSInputFile fsInputFile = fileManagerMongo.createFileOnDBFolder(attachment.getRuleCode());
                            byteBuffer = new byte[READ_BUFFER_SIZE];
                            InputStream inputStream = uploadedFile.getInputStream();
                            OutputStream outputStream = fsInputFile.getOutputStream();
                            int readLength = inputStream.read(byteBuffer, 0, READ_BUFFER_SIZE);
                            while (readLength != -1) {
                                if (readLength >= this.READ_BUFFER_SIZE) {
                                    outputStream.write(byteBuffer);
                                } else if (readLength < this.READ_BUFFER_SIZE) {
                                    outputStream.write(byteBuffer, 0, readLength);
                                }
                                readLength = inputStream.read(byteBuffer);
                            }
                            inputStream.close();

                            String clientFileLinkId = attachment.getFileLink();
                            attachment.setFileLink(fsInputFile.getId().toString());
                            saveLinkFileCollection(attachment, req);
                            fileManagerMongo.saveFileFromStream(attachment.getFileName(), fsInputFile);

                            System.out.println("File Uploaded : " + attachment.getFileName());

                            serverFileData = resourceAlocationId + "|" + clientFileLinkId + "|" + fsInputFile.getId().toString() + "|" + fileName + "|" + ruleCode;
                        }
                    }
                }
            }

            //List<FileItem> files = fileUpload.parseRequest(req);
//            for (FileItem file : files) {
//                if (file.isFormField()) {
//                    resourceAlocationId = new String(file.get());
//                    attachment = (DmAttachment_Mdl) DmSession.getSessions().get(sessionId).getAttribute(resourceAlocationId);
//                } else {
//                    if (attachment != null) {
//                        GridFSInputFile fsInputFile = fileManagerMongo.createFileOnDBFolder(attachment.getRuleCode());
//                        byteBuffer = new byte[READ_BUFFER_SIZE];
//                        InputStream inputStream = file.getInputStream();
//                        OutputStream outputStream = fsInputFile.getOutputStream();
//                        int readLength = inputStream.read(byteBuffer, 0, READ_BUFFER_SIZE);
//                        while (readLength != -1) {
//                            if (readLength >= this.READ_BUFFER_SIZE) {
//                                outputStream.write(byteBuffer);
//                            } else if (readLength < this.READ_BUFFER_SIZE) {
//                                outputStream.write(byteBuffer, 0, readLength);
//                            }
//                            readLength = inputStream.read(byteBuffer);
//                        }
//                        inputStream.close();
//
//                        attachment.setFileLink(fsInputFile.getId().toString());
//                        saveLinkFileCollection(attachment, req);
//                        fileManagerMongo.saveFileFromStream(attachment.getFileName(), fsInputFile);
//
//                        System.out.println("File Uploaded : " + attachment.getFileName());
//
//                        fileId = fsInputFile.getId().toString();
//                    }
//                }
//            }
            resp.getOutputStream().print(serverFileData);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            DmSession.getSessions().get(sessionId).removeAttribute(resourceAlocationId);
        }
    }

    private void saveLinkFileCollection(DmAttachment_Mdl attachment, HttpServletRequest req) throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            if (DmShared.xNull(attachment.getDocId()).trim().equalsIgnoreCase("")
                    && DmShared.xNull(attachment.getTempDocId()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid DocId.");
            }

            if (DmShared.xNull(attachment.getRuleCode()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid Rule Code.");
            }

            if (DmShared.xNull(attachment.getFileTypeName()).trim().equalsIgnoreCase("")) {
                throw new Exception("Unable to attach file. Invalid File Type.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(req));

            sqlQuery = "Insert into linkFileCollection (fileLink, docId, sNo, ruleCode, fileType, fileName, fileSize, remarks, "
                    + " u_Name, u_EntDt, tempDocId, sessionId) "
                    + " values (:fileLink, :docId, :sNo, :ruleCode,  :fileType, :fileName, :fileSize, :remarks, "
                    + " :u_name, :u_EntDt, :tempDocId, :sessionId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fileLink", attachment.getFileLink());
            psTemp.setString("docid", DmShared.foriegnKey(attachment.getDocId()));
            psTemp.setString("rulecode", attachment.getRuleCode());
            psTemp.setString("sNo", DmShared.iNull(attachment.getsNo()).toString());
            psTemp.setString("filesize", DmShared.iNull(attachment.getFileSize()).toString());
            psTemp.setString("filename", attachment.getFileName());
            psTemp.setString("filetype", attachment.getFileTypeName());
            psTemp.setString("remarks", attachment.getRemarks());
            if (DmShared.xNull(attachment.getUserName()).trim().equalsIgnoreCase("")) {
                psTemp.setString("u_name", DmServer.getSsnUserName(req));
            } else {
                psTemp.setString("u_name", attachment.getUserName());
            }
            if (attachment.getU_EntDt() == null) {
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                psTemp.setTimestamp("u_EntDt", attachment.getU_EntDt());
            }
            if (DmShared.xNull(attachment.getDocId()).trim().equals("")) {
                psTemp.setString("tempDocId", DmShared.foriegnKey(attachment.getTempDocId()));
                psTemp.setString("sessionId", DmShared.foriegnKey(attachment.getSessionId()));
            } else {
                psTemp.setString("tempDocId", null);
                psTemp.setString("sessionId", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }
}

