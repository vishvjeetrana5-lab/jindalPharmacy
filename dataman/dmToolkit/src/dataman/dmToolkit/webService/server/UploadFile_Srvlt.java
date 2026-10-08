package dataman.dmToolkit.webService.server;

import com.mongodb.gridfs.GridFSInputFile;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFile_Srvlt extends HttpServlet {

    private final int READ_BUFFER_SIZE = 1 * 1024 * 1024;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, transactionDB = "", fileDB = "";

        FileInputStream fis = null;
        File file = null;
        DmFileManagerMongo fileManagerMongo;
        GridFSInputFile fsInputFile = null;
        byte[] byteBuffer;
        DmAttachment_Mdl attachment = new DmAttachment_Mdl();
        String fileType = "", regDocId = "", ruleCode = "", directoryPath, serverFullFilePath;
        int count = 0;

        try {
            directoryPath = DmServer.Path.sessions + File.separatorChar + UploadFile_Srvlt.class.getSimpleName();

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            List<FileItem> files = fileUpload.parseRequest(request);

            for (FileItem fileItem : files) {
                if (fileItem.isFormField()) {
                    if (DmShared.xNull(fileItem.getFieldName()).trim().equals("filetype")) {
                        fileType = DmShared.xNull(fileItem.getString()).trim();
                    } else if (DmShared.xNull(fileItem.getFieldName()).trim().equals("regdocid")) {
                        regDocId = DmShared.xNull(fileItem.getString()).trim();
                    } else if (DmShared.xNull(fileItem.getFieldName()).trim().equals("rulecode")) {
                        ruleCode = DmShared.xNull(fileItem.getString()).trim();
                    }
                }
            }

            if (DmBaseShared.xNull(fileType).equalsIgnoreCase("")) {
                throw new Exception("File Type can not be blank.");
            }
            if (DmBaseShared.xNull(regDocId).equalsIgnoreCase("")) {
                throw new Exception("RegDocId can not be blank.");
            }
            if (DmBaseShared.xNull(ruleCode).equalsIgnoreCase("")) {
                throw new Exception("Rule Code can not be blank.");
            }
            attachment.setDocId(regDocId);
            attachment.setRuleCode(ruleCode);
            attachment.setFileTypeName(fileType);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            /*==================< Get latest transactionDB & fileDB >====================*/
            sqlQuery = "Select	com.centraldata_path as transactionDB, com.centralfiledata_path as fileDB "
                    + "		from productLicensedTo as plt "
                    + "		left join company as com on plt.latestCompCode = com.comp_code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                transactionDB = DmShared.xNull(rsTemp.getString("transactionDB")).trim();
                fileDB = DmShared.xNull(rsTemp.getString("fileDB")).trim();
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            if (DmShared.xNull(transactionDB).trim().equalsIgnoreCase("")) {
                throw new Exception("Invalid Request transaction database not found...");
            }
            if (DmShared.xNull(fileDB).trim().equalsIgnoreCase("")) {
                throw new Exception("Invalid Request file database not found...");
            }

            fileManagerMongo = DmServer.getConnectionMongo(fileDB);

            file = new File(directoryPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            for (FileItem fileItem : files) {
                if (!fileItem.isFormField()) {
                    count++;
                    attachment.setFileName(DmShared.xNull(fileItem.getName()));
                    attachment.setsNo("1");

                    serverFullFilePath = directoryPath + File.separatorChar + UUID.randomUUID().toString() + DmShared.xNull(fileItem.getName());
                    file = new File(serverFullFilePath);

                    byteBuffer = new byte[READ_BUFFER_SIZE];
                    InputStream inputStream = fileItem.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(file);
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
                        exc.printStackTrace();
                    } finally {
                        inputStream.close();
                        inputStream = null;
                        outputStream.close();
                        outputStream = null;
                    }

                    if (!file.isFile()) {
                        throw new Exception("Invalid File.");
                    }

                    try {
                        fsInputFile = fileManagerMongo.createFileOnDBFolder(ruleCode);
                        fis = new FileInputStream(file);

                        byteBuffer = new byte[READ_BUFFER_SIZE];
                        int readLen = fis.read(byteBuffer);
                        while (readLen != -1) {
                            fsInputFile.getOutputStream().write(byteBuffer, 0, readLen);
                            readLen = fis.read(byteBuffer);
                        }
                        attachment.setFileLink(fsInputFile.getId().toString());
                        attachment.setFileSize(String.valueOf(file.length() / 1024));

                        /*======================<< Insert record into linkFileCollection >>=======================*/
                        saveLinkFileCollection(attachment, transactionDB);

                        /*======================<< Save in mongodb >>=======================*/
                        fileManagerMongo.saveFileFromStream(attachment.getFileName(), fsInputFile);
                    } catch (Exception e) {
                        throw new Exception(e.getMessage());
                    } finally {
                        if (fsInputFile != null) {
                            fsInputFile.getOutputStream().close();
                        }
                        if (file != null) {
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                    }
                }
            }
            if (count == 0) {
                throw new Exception("File not found.");
            }

            response.getOutputStream().print("File uploaded successfully...");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
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

    private void saveLinkFileCollection(DmAttachment_Mdl attachment, String transactionDB) throws Exception {
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

            cnTemp = DmServer.getConnection(transactionDB);

            /*======================<< Insert record into linkFileCollection >>=======================*/
            sqlQuery = "Insert into linkFileCollection (fileLink, docId, sNo, ruleCode, fileType, fileName, fileSize, remarks, "
                    + " u_Name, u_EntDt, sessionId) "
                    + " values (:fileLink, :docId, :sNo, :ruleCode,  :fileType, :fileName, :fileSize, :remarks, "
                    + " :u_name, :u_EntDt, :sessionId)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fileLink", attachment.getFileLink());
            psTemp.setString("docId", DmShared.xNull(attachment.getDocId()));
            psTemp.setLong("sNo", DmShared.iNull(DmServer.vNull(attachment.getsNo(), "0")).longValue());
            psTemp.setString("rulecode", attachment.getRuleCode());
            psTemp.setInt("fileSize", DmShared.iNull(DmServer.vNull(attachment.getFileSize(), "0")).shortValue());
            psTemp.setString("fileName", DmShared.xNull(attachment.getFileName()));
            psTemp.setString("fileType", DmShared.xNull(attachment.getFileTypeName()));
            psTemp.setString("remarks", "");
            psTemp.setString("u_name", "SA");
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.setString("sessionId", null);
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
