package aarogya.selfToolkit.radiologyDocuments.server;

import aarogya.projectMenu.radiologyDoc.jvm.RadiologyDocumentRequest_Mdl;
import com.google.gson.reflect.TypeToken;
import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RadiologyDocumentsDownload_Srvlt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        InputStream ins = null;
        try {
            String requestModelJson = "";
            ins = req.getInputStream();
            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLen = ins.read(BUFFEREDBYTES);
            while (readLen != -1) {
                requestModelJson += new String(BUFFEREDBYTES, 0, readLen, "UTF-8");
                readLen = ins.read(BUFFEREDBYTES);
            }

            RadiologyDocumentRequest_Mdl requestModel = DmServer.convertFromJson(requestModelJson, new TypeToken<RadiologyDocumentRequest_Mdl>() {
            });

            downloadFile(requestModel, req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void downloadFile(RadiologyDocumentRequest_Mdl requestModel, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            String fileType = DmShared.xNull(requestModel.fileType).trim();
            String ruleCode = DmShared.xNull(requestModel.ruleCode).trim();
            String documentId = "";
            String voucherLineItemSerialNo = "";

            System.out.println("Called For Header : " + requestModel.calledForHeader.toString());

            if (requestModel.calledForHeader) {
                documentId = DmShared.xNull(requestModel.headerDocumentId).trim();
                voucherLineItemSerialNo = DmShared.xNull(requestModel.headerVoucherLineItemSerialNo).trim();
            } else if (!requestModel.calledForHeader) {
                documentId = DmShared.xNull(requestModel.contentDocumentId).trim();
                voucherLineItemSerialNo = DmShared.xNull(requestModel.contentVoucherLineItemSerialNo).trim();
            }

            if (!documentId.equals("")
                    && !DmShared.xNull(requestModel.fileType).equals("")
                    && !DmShared.xNull(requestModel.ruleCode).equals("")) {

                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(request));

                String sql = "SELECT * FROM LINKFILECOLLECTION WHERE (DOCID=:docId) "
                        + " AND (SNO=:sNo) AND (FILETYPE=:fileType) AND (RULECODE=:ruleCode)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
                psTemp.setString("docId", DmShared.xNull(documentId).trim());
                psTemp.setString("sNo", DmShared.xNull(voucherLineItemSerialNo).trim());
                psTemp.setString("fileType", DmShared.xNull(fileType).trim());
                psTemp.setString("ruleCode", DmShared.xNull(ruleCode).trim());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    DmFileManagerMongo mongoManager = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(request));
                    GridFSDBFile mongoFile = mongoManager.getFileFromFolder(ruleCode, DmShared.xNull(rsTemp.getString("fileLink")).trim());
                    sendFileToDownload(mongoFile, response);
                }
            }
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
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
        }
    }

    private void sendFileToDownload(GridFSDBFile mongoFile, HttpServletResponse response) throws Exception {
        InputStream mongoInputStream = mongoFile.getInputStream();
        OutputStream downloadSendStream = response.getOutputStream();
        try {
            byte[] BUFFEREDBYTES = new byte[1024 * 1024];
            int readLen = mongoInputStream.read(BUFFEREDBYTES);
            while (readLen != -1) {
                downloadSendStream.write(BUFFEREDBYTES, 0, readLen);
                readLen = mongoInputStream.read(BUFFEREDBYTES);
            }
        } finally {
            if (mongoInputStream != null) {
                mongoInputStream.close();
                mongoInputStream = null;
            }
        }
    }

}
