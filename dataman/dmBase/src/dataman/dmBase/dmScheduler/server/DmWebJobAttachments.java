package dataman.dmBase.dmScheduler.server;

import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.GrdImage_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DmWebJobAttachments {

    private int READ_BUFFER_SIZE = 1 * 1024 * 1024;
    private String servletSessionId;
    private String servletSessionDbName;
    private String servletFileDbName;

    public DmWebJobAttachments(String sessionId, String dbName, String fileDbName) {
        this.servletSessionId = DmBaseShared.xNull(sessionId).trim();
        this.servletSessionDbName = DmBaseShared.xNull(dbName).trim();
        this.servletFileDbName = DmBaseShared.xNull(fileDbName).trim();
    }

    protected void getAttachments(Long templateId, DmDynamicDataSourceRow dataRow, List<WebJobAttachment_Mdl> rowEmailAttachments) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdImage_Mdl> listImage_Mdl = new ArrayList<GrdImage_Mdl>();
        GrdImage_Mdl image_Mdl;
        String sqlQuery, directoryPath = "";

        try {
//            directoryPath = DmBaseServer.Path.sessions + File.separatorChar + DmBaseServer.getSsnUserName(servletInstance).trim();
            directoryPath = DmBaseServer.Path.sessions + File.separatorChar + servletSessionId;

            cnTemp = DmBaseServer.getConnection(servletSessionDbName);

            sqlQuery = "Select  fileCode, ruleCode from wjTemplateFileDetail where templateCode = :templateCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("templateCode", DmBaseShared.iNull(templateId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                image_Mdl = new GrdImage_Mdl();
                image_Mdl.setImageCode(DmBaseShared.xNull(rsTemp.getString("fileCode")));
                image_Mdl.setRuleCode(DmBaseShared.xNull(rsTemp.getString("ruleCode")));
                listImage_Mdl.add(image_Mdl);
            }
            psTemp.close();
            rsTemp.close();

            if (!DmBaseShared.xNull(servletFileDbName).trim().equalsIgnoreCase("")) {
                DmFileManagerMongo fileManagerMongo = DmBaseServer.getConnectionMongo(servletFileDbName);

                if (templateId != null) {
//                    for (Long templateId : templateIds) {
                    if (DmBaseShared.iNull(templateId).longValue() != 0) {
                        rowEmailAttachments.addAll(getDataFromMongoDB(false, directoryPath, DmBaseShared.iNull(templateId).toString(), "EMT", fileManagerMongo, cnTemp));
                    }
//                    }
                }

                if (dataRow != null) {
//                    for (DmDynamicDataSourceRow dataRow : dataRow) {
                    for (GrdImage_Mdl mdl : listImage_Mdl) {
                        if (!DmBaseShared.xNull(mdl.getImageCode()).trim().equalsIgnoreCase("")) {
                            mdl.setImageCodeValue(dataRow.getCells().get(DmBaseShared.xNull(mdl.getImageCode()).trim()));
                            mdl.setRuleCodeValue(dataRow.getCells().get(DmBaseShared.xNull(mdl.getRuleCode()).trim()));

                            if ((!DmBaseShared.xNull(mdl.getImageCodeValue()).trim().equalsIgnoreCase(""))
                                    && (!DmBaseShared.xNull(mdl.getImageCodeValue()).trim().equalsIgnoreCase(""))) {
                                dataRow.getLstAttachments().addAll(getDataFromMongoDB(true, directoryPath, mdl.getImageCodeValue(), mdl.getRuleCodeValue(), fileManagerMongo, cnTemp));
                            }
                        }
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception ee) {

            }
        }
    }

    private List<WebJobAttachment_Mdl> getDataFromMongoDB(boolean isDynamic, String directoryPath, String docId, String ruleCode, DmFileManagerMongo fileManagerMongo, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        byte[] byteBuffer;
        GridFSDBFile fsDBFile = null;
        List<WebJobAttachment_Mdl> lstAttachments = new ArrayList<WebJobAttachment_Mdl>();
        WebJobAttachment_Mdl node;
        String sqlQuery, fullFilePath, condition = "";

        try {
            directoryPath += File.separatorChar + DmBaseShared.xNull(docId).trim();

            if (isDynamic) {
                condition = " and fileLink = :docId ";
            } else {
                condition = " and docId = :docId ";
            }

            sqlQuery = "Select fileLink, fileType, fileName from linkFileCollection where ruleCode = :ruleCode " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmBaseShared.xNull(docId).trim());
            psTemp.setString("ruleCode", DmBaseShared.xNull(ruleCode).trim());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new WebJobAttachment_Mdl();
                node.setMongoFileLink(DmBaseShared.xNull(rsTemp.getString("fileLink")));
                node.setFileRuleCode(DmBaseShared.xNull(ruleCode).trim());
                node.setFileType(DmBaseShared.xNull(rsTemp.getString("fileType")));
                node.setDisplayFileName(DmBaseShared.xNull(rsTemp.getString("fileName")));
                node.setIsManual(DmEnum.DmYesNo.YES);

                fsDBFile = fileManagerMongo.getFileFromFolder((DmBaseShared.xNull(ruleCode).trim()), DmBaseShared.xNull(rsTemp.getString("fileLink")));
                InputStream inputStream = fsDBFile.getInputStream();
//                node.setActFileName(fsDBFile.getFilename());

                File sFile = new File(directoryPath);
                if (!sFile.exists()) {
                    sFile.mkdirs();
                }

                fullFilePath = directoryPath + File.separatorChar + DmBaseShared.xNull(rsTemp.getString("fileLink")) + DmBaseShared.xNull(fsDBFile.getFilename());
                
                File fullFile=new File(fullFilePath);
                

                sFile = new File(fullFilePath);
                byteBuffer = new byte[READ_BUFFER_SIZE];

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
                    node.setFileDirectory(fullFile.getParent());
                    node.setActFileName(fullFile.getName());
                } catch (Exception exc) {
                } finally {
                    inputStream.close();
                    outputStream.close();
                }
                lstAttachments.add(node);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();;
                }
                if (psTemp != null) {
                    psTemp.close();;
                }
            } catch (Exception e) {
            }
        }

        return lstAttachments;
    }
}
