package dataman.dmToolkitR.dmFileManager.server;

import dataman.dmBase.self.shared.DmDateTimeFormat;
import dataman.dmToolkitR.dmFileManager.shared.DmFileManager_Srv;
import dataman.dmBase.dmFileManager.shared.DmAttachment_Mdl;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkitR.dmStatic.server.DmServer;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSize;
import dataman.dmToolkitR.self.server.DmImageResolver;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DmFileManager_SrvImpl extends DmRSSPrivate implements DmFileManager_Srv {

    private int READ_BUFFER_SIZE = 1 * 1024 * 1024;

    @Override
    public DmResult<List<DmAttachment_Mdl>> allocateResource(List<DmAttachment_Mdl> listAttachments) {
        DmResult<List<DmAttachment_Mdl>> result = new DmResult<>();

        try {
            for (int i = 0; i < listAttachments.size(); i++) {
                DmAttachment_Mdl attachment = listAttachments.get(i);
                String resourceAlocationId = UUID.randomUUID().toString();
                attachment.setResourceAlocationId(resourceAlocationId);
                this.getLocalRequest().getSession().setAttribute(resourceAlocationId, attachment);

                attachment.setU_EntDt(DmServer.getServerTimestamp());
                attachment.setUserName(DmServer.getSsnUserName(this));
                attachment.setModifyDateTime(DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new Date(attachment.getU_EntDt().getTime())));
            }
            result.setData(listAttachments);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
        }
        return result;
    }

    @Override
    public DmResult<Boolean> deleteFile(DmAttachment_Mdl fileAttachment) {
        DmResult<Boolean> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        DmFileManagerMongo mongoCon = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mongoCon = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));

            if (!mongoCon.deleteFile(fileAttachment.getRuleCode(), fileAttachment.getFileLink())) {
                throw new Exception("Unable to delete Files from Database.");
            }

            /*======================<< Delete record from linkFileCollection  >>=======================*/
            sqlQuery = "Delete from linkFileCollection where fileLink = :fileLink";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fileLink", fileAttachment.getFileLink());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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

        return dmrTemp;
    }

//    @Override
//    public DmResult<Boolean> deleteFile(DmAttachment_Mdl attachment) {
//        DmResult<Boolean> dmrTemp = new DmResult<>();
//        DmPreparedStatement psTemp = null;
//        Connection cnTemp = null;
//        DmFileManagerMongo mongoCon = null;
//        String sqlQuery;
//
//        try {
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            mongoCon = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
//
////            if (!mongoCon.deleteFile(attachment.getRuleCode(), attachment.getFileLink())) {
////                throw new Exception("Unable to delete Files from Database.");
////            }
//
//            /*======================<< Delete record from linkFileCollection  >>=======================*/
//            sqlQuery = "Delete from linkFileCollection where fileLink = :fileLink";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
////            psTemp.setString("fileLink", attachment.getFileLink());
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            dmrTemp.setStatus(true);
//        } catch (Exception ex) {
//            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
//        } finally {
//            try {
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return dmrTemp;
//    }
    @Override
    public DmResult<DmAttachment_Mdl> saveEditedFile(DmAttachment_Mdl fileAttachment, DmFile_Mdl fileModel) {
        DmResult<DmAttachment_Mdl> result = new DmResult<>();

        try {
            String fullFilePath = fileModel.getServerDirectory() + File.separatorChar + fileModel.getServerFileName();

            File file = new File(fullFilePath.trim());
            if (!file.exists()) {
                throw new Exception("File not found : " + fullFilePath);
            }

            DmFileManagerMongo mongoClient = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));

            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);

                mongoClient.deleteFile(fileAttachment.getRuleCode(), fileAttachment.getFileLink());
                String newMongoId = mongoClient.saveFileInMongo(fileAttachment.getFileLink(), fileAttachment.getRuleCode(), fileModel);
                fileAttachment.setFileLink(newMongoId);
                fileAttachment.setServerDirectory(fileModel.getServerDirectory());
                fileAttachment.setFileName(fileModel.getFileName());
            } catch (Exception ex) {
                throw ex;
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }
            result.setData(fileAttachment);
            result.setStatus(true);
        } catch (Exception ex) {
            result.setStatus(false);
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }

//    @Override
//    public DmResult<DmAttachment_Mdl> saveEditedFile(DmAttachment_Mdl attachment, DmFile_Mdl fileModel) {
//        DmResult<DmAttachment_Mdl> result = new DmResult<>();
//
//        try {
//            String fullFilePath = fileModel.getServerDirectory() + File.separatorChar + fileModel.getServerFileName();
//
//            File file = new File(fullFilePath.trim());
//            if (!file.exists()) {
//                throw new Exception("File not found : " + fullFilePath);
//            }
//
//            DmFileManagerMongo mongoClient = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
//
//            FileInputStream fis = null;
//            try {
//                fis = new FileInputStream(file);
//
////                mongoClient.deleteFile(attachment.getRuleCode(), attachment.getFileLink());
////                String newMongoId = mongoClient.saveFileInMongo(attachment.getFileLink(), attachment.getRuleCode(), fileModel);
////                attachment.setFileLink(newMongoId);
////                attachment.setServerDirectory(fileModel.getServerDirectory());
////                attachment.setFileName(fileModel.getFileName());
//            } catch (Exception ex) {
//                throw ex;
//            } finally {
//                if (fis != null) {
//                    fis.close();
//                    fis = null;
//                }
//            }
//            result.setData(attachment);
//            result.setStatus(true);
//        } catch (Exception ex) {
//            result.setStatus(false);
//            result.setErrorMessage(ex.getMessage());
//            ex.printStackTrace();
//        }
//        return result;
//    }
    @Override
    public DmResult<DmSize> getActualImageSize(DmAttachment_Mdl fileAttachment) {
        DmResult<DmSize> result = new DmResult<>();

        try {
            DmFileManagerMongo mongoCon = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            mongoCon.getFileFromFolder(fileAttachment.getRuleCode(), fileAttachment.getFileLink());

            DmImageResolver resolver = new DmImageResolver();
            resolver.setMongoFileLink(fileAttachment.getFileLink());
            resolver.setRuleCode(fileAttachment.getRuleCode());
            resolver.setDirectoryPath(fileAttachment.getServerDirectory());
            resolver.setFileName(fileAttachment.getFileName());

            result.setData(resolver.getSizeOfImage(this));
            result.setStatus(true);
        } catch (Exception ex) {
            result.setErrorMessage(ex.getMessage());
            ex.printStackTrace();
        }

        return result;
    }

//    @Override
//    public DmResult<DmSize> getActualImageSize(DmAttachment_Mdl attachment) {
//        DmResult<DmSize> result = new DmResult<>();
//
//        try {
//            DmFileManagerMongo mongoCon = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
////            mongoCon.getFileFromFolder(attachment.getRuleCode(), attachment.getFileLink());
////
////            DmImageResolver resolver = new DmImageResolver();
////            resolver.setMongoFileLink(attachment.getFileLink());
////            resolver.setRuleCode(attachment.getRuleCode());
////            resolver.setDirectoryPath(attachment.getServerDirectory());
////            resolver.setFileName(attachment.getFileName());
//
////            result.setData(resolver.getSizeOfImage(this));
//            result.setStatus(true);
//        } catch (Exception ex) {
//            result.setErrorMessage(ex.getMessage());
//            ex.printStackTrace();
//        }
//
//        return result;
//    }
    @Override
    public DmResult<List<DmFileRuleType>> getFileType(String keyCode, String tempKeyCode, String sNo, String ruleCode, String fileTypeName, HashMap<String, String> multiDocIdWithRuleCode) {
        DmResult<List<DmFileRuleType>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<DmFileRuleType> listNode = new ArrayList<DmFileRuleType>();
        DmFileRuleType node;
        String sqlQuery, sessionId, multiRuleCode = "";
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sessionId = (this.getLocalRequest().getSession().getId());

            multiRuleCode = "'" + DmShared.xNull(ruleCode).trim() + "'";

            if (multiDocIdWithRuleCode != null) {
                for (Map.Entry<String, String> entry : multiDocIdWithRuleCode.entrySet()) {
                    if (!DmShared.xNull(entry.getKey()).trim().equalsIgnoreCase("")) {
                        if (DmShared.xNull(multiRuleCode).trim().equalsIgnoreCase("")) {
                            multiRuleCode = "'" + DmShared.xNull(entry.getKey()).trim() + "'";
                        } else {
                            multiRuleCode += ",'" + DmShared.xNull(entry.getKey()).trim() + "'";
                        }

                        if (DmShared.xNull(entry.getKey()).trim().equalsIgnoreCase(DmShared.xNull(ruleCode).trim())) {
                            if (DmShared.xNull(entry.getValue()).trim().contains("'" + DmShared.xNull(keyCode).trim() + "',")) {
                                multiDocIdWithRuleCode.replace(DmShared.xNull(entry.getKey()).trim(), entry.getValue(), entry.getValue().replace("'" + DmShared.xNull(keyCode).trim() + "',", ""));
                            }
                            if (DmShared.xNull(entry.getValue()).trim().contains("'" + DmShared.xNull(keyCode).trim() + "'")) {
                                multiDocIdWithRuleCode.replace(DmShared.xNull(entry.getKey()).trim(), entry.getValue(), entry.getValue().replace("'" + DmShared.xNull(keyCode).trim() + "'", ""));
                            }
                        }
                    }
                }
            }

            fileTypeName = DmShared.xNull(fileTypeName).trim();
            /*======================<< Get File type details >>=======================*/
            sqlQuery = "Select  ftm.fileType, max(ftm.maxSize) as maxSize, max(ftm.extFilters) as extFilters "
                    + "         from fileTypeMast as ftm "
                    + "         where ftm.ruleCode in (" + multiRuleCode + ") " + (fileTypeName.equals("") ? "" : " AND (ftm.fileType=:fileTypeName)")
                    + "         group by ftm.fileType "
                    + "		order by ftm.fileType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!fileTypeName.equals("")) {
                psTemp.setString("fileTypeName", fileTypeName);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new DmFileRuleType();
                node.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
                node.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
                node.setFileSizeLimit(DmShared.xNull(rsTemp.getString("maxSize")));
                node.setFilters(DmShared.xNull(rsTemp.getString("extFilters")));
                node.setRuleCode(ruleCode);
                node.setSessionId(sessionId);
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            /*======================<< Get File type details >>=======================*/
            List<DmAttachment_Mdl> attachments = getAttachedFiles(keyCode, tempKeyCode, sNo, ruleCode, sessionId, cnTemp);

            if (multiDocIdWithRuleCode != null) {
                for (Map.Entry<String, String> entry : multiDocIdWithRuleCode.entrySet()) {
                    if (!DmShared.xNull(entry.getValue()).trim().equalsIgnoreCase("")) {
                        attachments.addAll(getAttachedFilesOther(entry.getValue(), entry.getKey(), cnTemp));
                    }
                }
            }

            attachments.sort(Comparator.comparing(DmAttachment_Mdl::getU_EntDt).reversed());

            for (DmFileRuleType ruleTypeMdl : listNode) {
                if (!DmShared.xNull(ruleTypeMdl.getFileTypeName()).trim().equalsIgnoreCase("")) {
                    ruleTypeMdl.getListFiles().addAll(attachments.stream().filter(p -> p.getFileTypeName().equalsIgnoreCase(ruleTypeMdl.getFileTypeName())).collect(Collectors.toList()));
                }
            }
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

//    @Override
//    public DmResult<List<DmFileRuleType>> getFileType(String keyCode, String tempKeyCode, String sNo, String ruleCode, HashMap<String, String> multiDocIdWithRuleCode) {
//        DmResult<List<DmFileRuleType>> dmrTemp = new DmResult();
//        Connection cnTemp = null, cnTempTrans = null;
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        List<DmFileRuleType> listNode = new ArrayList<DmFileRuleType>();
//        DmFileRuleType node;
//        String sqlQuery, sessionId, multiRuleCode = "";
//        dmrTemp.setData(listNode);
//
//        try {
//            cnTemp = DmServer.getConnection(DmServer.dbCompany);
//            cnTempTrans = DmServer.getConnection(DmServer.getSsnDBName(this));
//
//            sessionId = (this.getLocalRequest().getSession().getId());
//
//            multiRuleCode = "'" + DmShared.xNull(ruleCode).trim() + "'";
//
//            if (multiDocIdWithRuleCode != null) {
//                for (Map.Entry<String, String> entry : multiDocIdWithRuleCode.entrySet()) {
//                    if (!DmShared.xNull(entry.getKey()).trim().equalsIgnoreCase("")) {
//                        if (DmShared.xNull(multiRuleCode).trim().equalsIgnoreCase("")) {
//                            multiRuleCode = "'" + DmShared.xNull(entry.getKey()).trim() + "'";
//                        } else {
//                            multiRuleCode += ",'" + DmShared.xNull(entry.getKey()).trim() + "'";
//                        }
//
//                        if (DmShared.xNull(entry.getKey()).trim().equalsIgnoreCase(DmShared.xNull(ruleCode).trim())) {
//                            if (DmShared.xNull(entry.getValue()).trim().contains("'" + DmShared.xNull(keyCode).trim() + "',")) {
//                                multiDocIdWithRuleCode.replace(DmShared.xNull(entry.getKey()).trim(), entry.getValue(), entry.getValue().replace("'" + DmShared.xNull(keyCode).trim() + "',", ""));
//                            }
//                            if (DmShared.xNull(entry.getValue()).trim().contains("'" + DmShared.xNull(keyCode).trim() + "'")) {
//                                multiDocIdWithRuleCode.replace(DmShared.xNull(entry.getKey()).trim(), entry.getValue(), entry.getValue().replace("'" + DmShared.xNull(keyCode).trim() + "'", ""));
//                            }
//                        }
//                    }
//                }
//            }
//
//
//            /*======================<< Get File type details >>=======================*/
//            sqlQuery = "Select  ftm.fileType, max(ftm.maxSize) as maxSize, max(ftm.extFilters) as extFilters "
//                    + "         from fileTypeMast as ftm "
//                    + "         where ftm.ruleCode in (" + multiRuleCode + ") "
//                    + "         group by ftm.fileType "
//                    + "		order by ftm.fileType ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                node = new DmFileRuleType();
//                node.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
//                node.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
//                node.setFileSizeLimit(DmShared.xNull(rsTemp.getString("maxSize")));
//                node.setFilters(DmShared.xNull(rsTemp.getString("extFilters")));
//                node.setRuleCode(ruleCode);
//                node.setSessionId(sessionId);
//                listNode.add(node);
//            }
//            rsTemp.close();
//            psTemp.close();
//
//            /*======================<< Get File type details >>=======================*/
//            List<DmAttachment_Mdl> listAttachment = getAttachedFiles(keyCode, tempKeyCode, sNo, ruleCode, sessionId, cnTempTrans);
//
//            if (multiDocIdWithRuleCode != null) {
//                for (Map.Entry<String, String> entry : multiDocIdWithRuleCode.entrySet()) {
//                    if (!DmShared.xNull(entry.getValue()).trim().equalsIgnoreCase("")) {
//                        listAttachment.addAll(getAttachedFilesOther(entry.getValue(), entry.getKey(), cnTempTrans));
//                    }
//                }
//            }
//
////            listAttachment.sort(Comparator.comparing(DmAttachment_Mdl::getU_EntDt).reversed());
////
////            for (DmFileRuleType ruleTypeMdl : listNode) {
////                if (!DmShared.xNull(ruleTypeMdl.getFileTypeName()).trim().equalsIgnoreCase("")) {
////                    ruleTypeMdl.getListAttachment().addAll(listAttachment.stream().filter(p -> p.getFileTypeName().equalsIgnoreCase(ruleTypeMdl.getFileTypeName())).collect(Collectors.toList()));
////                }
////            }
//            cnTemp.close();
//            cnTempTrans.close();
//            dmrTemp.setStatus(true);
//        } catch (Exception e) {
//            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTempTrans != null) {
//                    cnTempTrans.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return dmrTemp;
//    }
    public List<DmAttachment_Mdl> getAttachedFiles(String keyCode, String tempKeyCode, String sNo, String ruleCode, String sessionId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<DmAttachment_Mdl> attachments = new ArrayList<>();
        String sqlQuery;

        try {
            cnTemp.setCatalog(DmServer.getSsnDBName(this));

            if ((!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) || (!DmShared.xNull(tempKeyCode).trim().equalsIgnoreCase(""))) {

                sqlQuery = "Select  lfc.fileLink, lfc.ruleCode, lfc.fileType, lfc.fileName, "
                        + "         lfc.fileSize, lfc.remarks, lfc.u_Name, lfc.u_EntDt "
                        + "         from linkFileCollection as lfc "
                        + "         where lfc.sNo = :sNo and lfc.ruleCode = :ruleCode ";
                if (!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) {
                    sqlQuery += "   and lfc.docId = :docId ";
                } else {
                    sqlQuery += "   and lfc.tempDocId = :tempDocId and lfc.sessionId = :sessionId ";
                }
                sqlQuery += "       order by lfc.u_EntDt desc ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) {
                    psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                } else {
                    psTemp.setString("tempDocId", tempKeyCode);
                    psTemp.setString("sessionId", sessionId);
                }
                psTemp.setString("sNo", DmShared.iNull(sNo).toString());
                psTemp.setString("ruleCode", ruleCode);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    DmAttachment_Mdl fileModel = new DmAttachment_Mdl();
                    fileModel.setTempDocId(tempKeyCode);
                    fileModel.setSessionId(sessionId);
                    fileModel.setDocId(keyCode);
                    fileModel.setsNo(DmServer.vNull(sNo, "0"));
                    fileModel.setFileLink(DmShared.xNull(rsTemp.getString("fileLink")));
                    fileModel.setRuleCode(DmShared.xNull(rsTemp.getString("ruleCode")));
                    fileModel.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
                    fileModel.setFileName(DmShared.xNull(rsTemp.getString("fileName")));
                    fileModel.setFileSize(DmShared.xNull(rsTemp.getString("fileSize")));
                    fileModel.setRemarks(DmShared.xNull(rsTemp.getString("remarks")));
                    fileModel.setUserName(DmShared.xNull(rsTemp.getString("u_Name")));
                    fileModel.setU_EntDt(rsTemp.getTimestamp("u_EntDt"));

                    Timestamp value = rsTemp.getTimestamp("u_EntDt");
                    if (value != null) {
                        fileModel.setModifyDateTime(DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new Date(value.getTime())));
                    } else {
                        fileModel.setModifyDateTime("");
                    }
                    attachments.add(fileModel);
                }
                psTemp.close();
                rsTemp.close();
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                cnTemp.setCatalog(DmServer.dbCompany);
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return attachments;
    }

    public List<DmAttachment_Mdl> getAttachedFilesOther(String docIds, String ruleCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<DmAttachment_Mdl> listFiles = new ArrayList<>();
        String sqlQuery;

        try {
            cnTemp.setCatalog(DmServer.getSsnDBName(this));

            sqlQuery = "Select  lfc.docId, lfc.sNo, lfc.sessionId, lfc.fileLink, lfc.ruleCode, lfc.fileType, lfc.fileName, "
                    + "         lfc.fileSize, lfc.remarks, lfc.u_Name, lfc.u_EntDt "
                    + "         from linkFileCollection as lfc "
                    + "         where lfc.ruleCode = :ruleCode and lfc.docId in (" + DmServer.getParamList(":docId", docIds) + " ) "
                    + "         order by lfc.u_EntDt desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("docId", DmShared.xNull(docIds));
            psTemp.setString("ruleCode", ruleCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                DmAttachment_Mdl fileModel = new DmAttachment_Mdl();
                fileModel.setDocId(DmShared.xNull(rsTemp.getString("docId")));
                fileModel.setsNo(DmShared.xNull(rsTemp.getString("sNo")));
                fileModel.setSessionId(DmShared.xNull(rsTemp.getString("sessionId")));
                fileModel.setFileLink(DmShared.xNull(rsTemp.getString("fileLink")));
                fileModel.setRuleCode(DmShared.xNull(rsTemp.getString("ruleCode")));
                fileModel.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
                fileModel.setFileName(DmShared.xNull(rsTemp.getString("fileName")));
                fileModel.setFileSize(DmShared.xNull(rsTemp.getString("fileSize")));
                fileModel.setRemarks(DmShared.xNull(rsTemp.getString("remarks")));
                fileModel.setUserName(DmShared.xNull(rsTemp.getString("u_Name")));
                fileModel.setU_EntDt(rsTemp.getTimestamp("u_EntDt"));

                Timestamp value = rsTemp.getTimestamp("u_EntDt");
                if (value != null) {
                    fileModel.setModifyDateTime(DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new Date(value.getTime())));
                } else {
                    fileModel.setModifyDateTime("");
                }
                listFiles.add(fileModel);
            }
            psTemp.close();
            rsTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                cnTemp.setCatalog(DmServer.dbCompany);
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return listFiles;
    }

//    public List<DmAttachment_Mdl> getAttachedFiles(String keyCode, String tempKeyCode, String sNo, String ruleCode, String sessionId, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        List<DmAttachment_Mdl> listNode = new ArrayList<DmAttachment_Mdl>();
//        DmAttachment_Mdl node;
//        String sqlQuery;
//
//        try {
//            if ((!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) || (!DmShared.xNull(tempKeyCode).trim().equalsIgnoreCase(""))) {
//
//                sqlQuery = "Select  lfc.fileLink, lfc.ruleCode, lfc.fileType, lfc.fileName, "
//                        + "         lfc.fileSize, lfc.remarks, lfc.u_Name, lfc.u_EntDt "
//                        + "         from linkFileCollection as lfc "
//                        + "         where lfc.sNo = :sNo and lfc.ruleCode = :ruleCode ";
//                if (!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) {
//                    sqlQuery += "   and lfc.docId = :docId ";
//                } else {
//                    sqlQuery += "   and lfc.tempDocId = :tempDocId and lfc.sessionId = :sessionId ";
//                }
//                sqlQuery += "       order by lfc.u_EntDt desc ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                if (!DmShared.xNull(keyCode).trim().equalsIgnoreCase("")) {
//                    psTemp.setLong("docId", DmShared.iNull(keyCode).longValue());
//                } else {
//                    psTemp.setString("tempDocId", tempKeyCode);
//                    psTemp.setString("sessionId", sessionId);
//                }
//                psTemp.setInt("sNo", DmShared.iNull(sNo).shortValue());
//                psTemp.setString("ruleCode", ruleCode);
//                rsTemp = psTemp.executeQuery();
//                while (rsTemp.next()) {
//                    node = new DmAttachment_Mdl();
////                    node.setTempDocId(tempKeyCode);
////                    node.setSessionId(sessionId);
////                    node.setDocId(keyCode);
////                    node.setsNo(DmServer.vNull(sNo, "0"));
////                    node.setFileLink(DmShared.xNull(rsTemp.getString("fileLink")));
////                    node.setRuleCode(DmShared.xNull(rsTemp.getString("ruleCode")));
////                    node.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
////                    node.setFileName(DmShared.xNull(rsTemp.getString("fileName")));
////                    node.setFileSize(DmShared.xNull(rsTemp.getString("fileSize")));
////                    node.setRemarks(DmShared.xNull(rsTemp.getString("remarks")));
////                    node.setUserName(DmShared.xNull(rsTemp.getString("u_Name")));
////                    node.setU_EntDt(rsTemp.getTimestamp("u_EntDt"));
//
//                    Timestamp value = rsTemp.getTimestamp("u_EntDt");
//                    if (value != null) {
//                        node.setModifyDateTime(DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new Date(value.getTime())));
//                    } else {
//                        node.setModifyDateTime("");
//                    }
//                    listNode.add(node);
//                }
//                psTemp.close();
//                rsTemp.close();
//            }
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return listNode;
//    }
//    public List<DmAttachment_Mdl> getAttachedFilesOther(String docIds, String ruleCode, Connection cnTemp) {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        List<DmAttachment_Mdl> listNode = new ArrayList<DmAttachment_Mdl>();
//        DmAttachment_Mdl node;
//        String sqlQuery;
//
//        try {
//            sqlQuery = "Select  lfc.docId, lfc.sNo, lfc.sessionId, lfc.fileLink, lfc.ruleCode, lfc.fileType, lfc.fileName, "
//                    + "         lfc.fileSize, lfc.remarks, lfc.u_Name, lfc.u_EntDt "
//                    + "         from linkFileCollection as lfc "
//                    + "         where lfc.ruleCode = :ruleCode and CAST(lfc.docId AS VARCHAR) in (" + DmServer.getParamList(":docId", docIds) + " ) "
//                    + "         order by lfc.u_EntDt desc ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setStringList("docId", DmShared.xNull(docIds));
//            psTemp.setString("ruleCode", ruleCode);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                node = new DmAttachment_Mdl();
////                node.setDocId(DmShared.xNull(rsTemp.getString("docId")));
////                node.setsNo(DmShared.xNull(rsTemp.getString("sNo")));
////                node.setSessionId(DmShared.xNull(rsTemp.getString("sessionId")));
////                node.setFileLink(DmShared.xNull(rsTemp.getString("fileLink")));
////                node.setRuleCode(DmShared.xNull(rsTemp.getString("ruleCode")));
////                node.setFileTypeName(DmShared.xNull(rsTemp.getString("fileType")));
////                node.setFileName(DmShared.xNull(rsTemp.getString("fileName")));
////                node.setFileSize(DmShared.xNull(rsTemp.getString("fileSize")));
////                node.setRemarks(DmShared.xNull(rsTemp.getString("remarks")));
////                node.setUserName(DmShared.xNull(rsTemp.getString("u_Name")));
////                node.setU_EntDt(rsTemp.getTimestamp("u_EntDt"));
//
//                Timestamp value = rsTemp.getTimestamp("u_EntDt");
//                if (value != null) {
//                    node.setModifyDateTime(DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new Date(value.getTime())));
//                } else {
//                    node.setModifyDateTime("");
//                }
//                listNode.add(node);
//            }
//            psTemp.close();
//            rsTemp.close();
//        } catch (Exception e) {
//            throw new DmException(0, e.getMessage());
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return listNode;
//    }
    @Override
    public DmResult<String> updateRemarks(DmAttachment_Mdl fileAttachment, String remarks) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(remarks);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Update linkFileCollection set remarks = :remarks where fileLink = :fileLink ";
            if (!DmShared.xNull(fileAttachment.getDocId()).trim().equalsIgnoreCase("")) {
                sqlQuery += "   and docId = :docId ";
            } else {
                sqlQuery += "   and tempDocId = :tempDocId and sessionId = :sessionId ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("fileLink", fileAttachment.getFileLink());
            if (!DmShared.xNull(fileAttachment.getDocId()).trim().equalsIgnoreCase("")) {
                psTemp.setString("docId", fileAttachment.getDocId());
            } else {
                psTemp.setString("tempDocId", fileAttachment.getTempDocId());
                psTemp.setString("sessionId", fileAttachment.getSessionId());
            }
            psTemp.setString("remarks", remarks);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

//    @Override
//    public DmResult<String> updateRemarks(DmAttachment_Mdl attachment_Mdl, String remarks) {
//        DmResult<String> dmrTemp = new DmResult();
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//        DmSavePoint spBegin = null;
//        String sqlQuery;
//        dmrTemp.setData(remarks);
//
//        try {
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            spBegin = new DmSavePoint(cnTemp);
//
//            sqlQuery = "Update linkFileCollection set remarks = :remarks where fileLink = :fileLink ";
////            if (!DmShared.xNull(attachment_Mdl.getDocId()).trim().equalsIgnoreCase("")) {
////                sqlQuery += "   and docId = :docId ";
////            } else {
////                sqlQuery += "   and tempDocId = :tempDocId and sessionId = :sessionId ";
////            }
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
////            psTemp.setString("fileLink", attachment_Mdl.getFileLink());
////            if (!DmShared.xNull(attachment_Mdl.getDocId()).trim().equalsIgnoreCase("")) {
////                psTemp.setString("docId", attachment_Mdl.getDocId());
////            } else {
////                psTemp.setString("tempDocId", attachment_Mdl.getTempDocId());
////                psTemp.setString("sessionId", attachment_Mdl.getSessionId());
////            }
//            psTemp.setString("remarks", remarks);
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            spBegin.commit();
//            cnTemp.close();
//            dmrTemp.setStatus(true);
//        } catch (Exception e) {
//            try {
//                if (spBegin != null) {
//                    spBegin.rollBack();
//                }
//            } catch (Exception ex) {
//            }
//            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//
//        return dmrTemp;
//    }
}
