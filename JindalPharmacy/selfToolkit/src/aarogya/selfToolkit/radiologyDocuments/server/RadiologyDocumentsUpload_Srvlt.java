package aarogya.selfToolkit.radiologyDocuments.server;

import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentUploadRequest_Mdl;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.thirdparty.guava.common.io.Files;
import com.mongodb.gridfs.GridFSInputFile;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Base64;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RadiologyDocumentsUpload_Srvlt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream ins = req.getInputStream();
        String dataToSend = "";
        try {
            StringBuilder jsonBuilder = new StringBuilder();
            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLen = ins.read(BUFFEREDBYTES);
            while (readLen != -1) {
                jsonBuilder.append(new String(BUFFEREDBYTES, 0, readLen, "UTF-8"));
                readLen = ins.read(BUFFEREDBYTES);
            }

            RadiologyDocumentUploadRequest_Mdl uploadRequestModel = DmServer.convertFromJson(jsonBuilder.toString(), new TypeToken<RadiologyDocumentUploadRequest_Mdl>() {
            });

            uploadFileToMongo(req, uploadRequestModel, DmServer.getConnectionMongo(DmServer.getSsnFileDBName(req)));

            dataToSend = DmServer.convertToJson(uploadRequestModel, new TypeToken<RadiologyDocumentUploadRequest_Mdl>() {
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resp.getOutputStream().write(dataToSend.getBytes("UTF-8"));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void uploadFileToMongo(HttpServletRequest request, RadiologyDocumentUploadRequest_Mdl uploadModel, DmFileManagerMongo fileManagerMongo) throws Exception {
        File inputFilePath = null;
        FileInputStream fis = null;
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        try {
            inputFilePath = File.createTempFile("rpt", ".docx");

            Files.write(Base64.getDecoder().decode(uploadModel.documentContent), inputFilePath);

            fis = new FileInputStream(inputFilePath);

            GridFSInputFile inputFile = fileManagerMongo.createFileOnDBFolder(uploadModel.requestModel.ruleCode);
            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLen = fis.read(BUFFEREDBYTES);
            long fileLength = 0;
            while (readLen != -1) {
                fileLength += readLen;
                inputFile.getOutputStream().write(BUFFEREDBYTES, 0, readLen);
                readLen = fis.read(BUFFEREDBYTES);
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(request));

            String sql = "DELETE FROM LINKFILECOLLECTION WHERE (DOCID=:docId)"
                    + " AND (SNO=:sNo) AND (RULECODE=:ruleCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("docId", uploadModel.requestModel.contentDocumentId);
            psTemp.setString("ruleCode", uploadModel.requestModel.ruleCode);
            psTemp.setString("sNo", uploadModel.requestModel.contentVoucherLineItemSerialNo);

            psTemp.executeUpdate();
            psTemp.close();

            sql = "INSERT INTO LINKFILECOLLECTION (FILELINK,DOCID,SNO,"
                    + "RULECODE,FILETYPE,FILENAME,FILESIZE,SESSIONID,U_NAME,U_ENTDT)"
                    + " VALUES (:fileLink,:docId,:sNo,"
                    + ":ruleCode,:fileType,:fileName,:fileSize,:sessionId,:u_Name,:u_EntDt)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setString("fileLink", inputFile.getId().toString());
            psTemp.setString("docId", uploadModel.requestModel.contentDocumentId);
            psTemp.setString("sNo", uploadModel.requestModel.contentVoucherLineItemSerialNo);
            psTemp.setString("ruleCode", uploadModel.requestModel.ruleCode);
            psTemp.setString("fileType", DmShared.xNull(uploadModel.requestModel.fileType).trim());
            psTemp.setString("fileName", inputFilePath.getName());
            psTemp.setString("fileSize", String.valueOf(fileLength));
            psTemp.setString("sessionId", uploadModel.requestModel.clientSessionId);
            psTemp.setString("u_Name", DmServer.getSsnUserName(request));
            psTemp.setDate("u_EntDt", new Date(new java.util.Date().getTime()));

            psTemp.executeUpdate();

            inputFile.getOutputStream().close();

            uploadModel.mongoFileLineId = inputFile.getId().toString();

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (inputFilePath != null) {
                if (inputFilePath.exists()) {
                    inputFilePath.delete();
                }
            }
        }
    }

}
