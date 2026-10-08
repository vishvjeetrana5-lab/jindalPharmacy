package dataman.dmBase.dmScheduler.server;

import com.mongodb.gridfs.GridFSDBFile;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.TemplateMast_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmScheduler.shared.GrdImage_Mdl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class DmAutoScheduler extends DmScheduler {

    private List<GrdImage_Mdl> listImageMap = new ArrayList<GrdImage_Mdl>();
    private int READ_BUFFER_SIZE = 1 * 1024 * 1024;

    public DmAutoScheduler(SchedulerMast_Mdl model, Date dtTemp) {
        super(model, dtTemp);
    }

    @Override
    public boolean shouldExecute(Calendar dtPrevious, Calendar dtNow) {
        boolean bool = false;

        try {
            this.dtCmp = new Date();//dtNow;

            Calendar cal = new GregorianCalendar();
            cal.setTime(dtCmp);

            if (this.getSchedularModel().getSchType() == DmEnum.DmSchedularType.ONCE) {
                bool = (this.getSchedularModel().getLastExecutionTime() == null) && (this.getSchedularModel().getStartDateTime().before(dtCmp)) && (this.getSchedularModel().getStartDateTime().after(dtPrevious.getTime()));
            } else if (this.getSchedularModel().getSchType() == DmEnum.DmSchedularType.DAILY) {
                bool = processForDaily(dtPrevious, dtNow);
            } else if (this.getSchedularModel().getSchType() == DmEnum.DmSchedularType.WEEKLY) {
                bool = processForWeekly(dtPrevious, dtNow);
            } else if (this.getSchedularModel().getSchType() == DmEnum.DmSchedularType.MONTHLY) {
                bool = processForMonthly(dtPrevious, dtNow);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bool;
    }

    @Override
    public void fillSiteCodes(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            this.getEnviros().clear();

            sqlQuery = "SELECT  SM.PLTCODE, EN.SITE_CODE, EN.wj_SmtpServer, EN.wj_SmtpPort, EN.wj_SmtpSsl, EN.wj_Url, EN.wj_AcName, "
                    + "         EN.wj_Password, EN.wj_Format, EN.wj_MsgLength "
                    + "         FROM ENVIRO EN, SITEMAST SM "
                    + "         WHERE (SM.code = EN.SITE_CODE) AND (SM.PLTCODE = :pltCode) and (EN.SITE_CODE IN (";
            String inParamsSQL = "";
            String inValuesSQL = "";

            for (int i = 0; i < this.getSchedularModel().getLstSites().size(); i++) {
                inParamsSQL += ":siteCode" + String.valueOf(i) + ",";
            }

            if (inParamsSQL.endsWith(",")) {
                inParamsSQL = inParamsSQL.substring(0, inParamsSQL.length() - 1);
            }

            if (inValuesSQL.endsWith(",")) {
                inValuesSQL = inValuesSQL.substring(0, inValuesSQL.length() - 1);
            }

            sqlQuery += inParamsSQL + "))";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("pltCode", this.getSchedularModel().getPlt().getPLTCode());

            for (int i = 0; i < this.getSchedularModel().getLstSites().size(); i++) {
                psTemp.setString("siteCode" + String.valueOf(i), this.getSchedularModel().getLstSites().get(i).toString());
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                SiteEnviro_Mdl site = new SiteEnviro_Mdl();
                site.setSiteCode(DmBaseShared.iNull(rsTemp.getShort("site_Code")).intValue());
                if (!this.getEnviros().containsKey(site.getSiteCode())) {
                    site.setPltCode(DmBaseShared.iNull(rsTemp.getObject("pltCode")).intValue());
                    site.setWebJobAccName(DmBaseShared.xNull(rsTemp.getString("wj_AcName")));
                    site.setWebJobFormat(DmBaseShared.xNull(rsTemp.getString("wj_Format")));
                    site.setWebJobPassword(DmBaseShared.xNull(rsTemp.getString("wj_Password")));
                    site.setWebJobSMTPServer(DmBaseShared.xNull(rsTemp.getString("wj_SMTPServer")));
                    site.setWebJobSMTPPort(DmBaseShared.xNull(rsTemp.getString("wj_SMTPPort")));
                    site.setWebJobSMSUrl(DmBaseShared.xNull(rsTemp.getString("wj_Url")));
                    site.setSslRequired(DmEnum.DmYesNo.valueOf(DmBaseShared.iNull(rsTemp.getShort("wj_SMTPSSL")).intValue()));
                    site.setMessageLength(DmBaseShared.iNull(rsTemp.getInt("wj_MsgLength")).longValue());
                    if (!this.getSchedularModel().getEmailSenderUserName().equals("")) {
                        site.setSenderEmailUserName(this.getSchedularModel().getEmailId());
                        site.setSenderEmailPassword(this.getSchedularModel().getEmailSenderPassword());
                        site.getLstEmailBcc().addAll(this.getSchedularModel().getLstEmailBcc());
                        site.getLstEmailCc().addAll(this.getSchedularModel().getLstEmailCc());
                    }
                    this.getEnviros().put(site.getSiteCode(), site);
                }
            }

        } catch (Exception ex) {
            throw ex;
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
            } catch (Exception exc) {
            }
        }
    }

    @Override
    public void fillJobDetails(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            String sql = "SELECT STEMP.CODE,STEMP.TEMPLATETYPE,STEMP.NAME,STEMP.DATASOURCE,STEMP.FUNCTIONNAME,STEMP.MESSAGE,UTEMP.NAME,STEMP.SUBJECT FROM WJTEMPLATEMAST STEMP LEFT JOIN WJTEMPLATEGROUPMAST UTEMP ON (STEMP.templategroupcode=UTEMP.CODE) WHERE (STEMP.CODE=:tempCode)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
            psTemp.setLong("tempCode", this.getSchedularModel().getWebJobTemplateId());
            TemplateMast_Mdl tModel = new TemplateMast_Mdl();
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                tModel.setTemplateId(DmBaseShared.iNull(rsTemp.getLong("CODE")).longValue());
                tModel.setTemplateName(DmBaseShared.xNull(rsTemp.getString("NAME")));
                DmEnum.DmWebJobTypes wjType = DmEnum.DmWebJobTypes.getValueFrom(DmBaseShared.iNull(rsTemp.getShort("TEMPLATETYPE")).intValue());
                tModel.setWebJobType(wjType);
                if (wjType == DmEnum.DmWebJobTypes.EVENT) {
                    tModel.setWebJobDataSource(null);
                } else {
                    tModel.setWebJobDataSource(DmEnum.DmWebJobDataSources.getValueFrom(DmBaseShared.iNull(rsTemp.getShort("DATASOURCE")).intValue()));
                }
                tModel.setMessageFormat(DmBaseShared.xNull(rsTemp.getString("MESSAGE")));
                tModel.setFileOrFuncName(DmBaseShared.xNull(rsTemp.getString("FUNCTIONNAME")));
                tModel.setSubjectFormat(DmBaseShared.xNull(rsTemp.getString("SUBJECT")));
            }

            rsTemp.close();
            psTemp.close();

            this.setTemplateModel(tModel);

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }

            if (rsTemp != null) {
                rsTemp.close();
                rsTemp = null;
            }
        }
    }

    @Override
    public void fillAttachmentDetails(Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            if (this.getWebJobType() == DmEnum.DmWebJobTypes.EMAIL) {
                String sqlQuery, directoryPath, mongoDB = "";

                sqlQuery = "SELECT * FROM wjTemplateAttachment WHERE (TEMPLATECODE=:templateId) ORDER BY V_SNO";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("templateId", String.valueOf(this.getTemplateId()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    WebJobAttachment_Mdl attachment = new WebJobAttachment_Mdl();
                    attachment.setTemplateId(this.getTemplateId());
                    attachment.setProcName(DmBaseShared.xNull(rsTemp.getString("procedureName")));
                    attachment.setRptName(DmBaseShared.xNull(rsTemp.getString("rptName")));
                    attachment.setActFileName(DmBaseShared.xNull(rsTemp.getString("fileName")));
                    this.getAttachments().add(attachment);
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Select  fileCode, ruleCode from wjTemplateFileDetail where templateCode = :templateCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("templateCode", String.valueOf(this.getTemplateId()));
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    GrdImage_Mdl node = new GrdImage_Mdl();
                    node.setImageCode(DmBaseShared.xNull(rsTemp.getString("fileCode")));
                    node.setRuleCode(DmBaseShared.xNull(rsTemp.getString("ruleCode")));
                    listImageMap.add(node);
                }
                psTemp.close();
                rsTemp.close();

                if (!DmBaseShared.xNull(getSchedularModel().getActiveFileDbName()).equals("")) {
                    DmFileManagerMongo fileManagerMongo = DmBaseServer.getConnectionMongo(getSchedularModel().getActiveFileDbName());
                    directoryPath = DmBaseServer.Path.sessions + File.separatorChar + DmBaseShared.iNull(this.getTemplateId()).toString();

                    this.getAttachments().addAll(getDataFromMongoDB(false, directoryPath, String.valueOf(this.getTemplateId()), "EMT", fileManagerMongo, cnTemp));
                }
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
                rsTemp = null;
            }
            if (psTemp != null) {
                psTemp.close();
                psTemp = null;
            }
        }
    }

    @Override
    public void fillDataAndParametersFromDB(SiteEnviro_Mdl siteEnviro, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            String sqlQuery = "SELECT * FROM " + this.getFileOrFunctionName() + "(:templateId,:siteCode,:uniqueKey)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("templateId", DmBaseShared.iNull(this.getTemplateId()).toString());
            psTemp.setString("siteCode", DmBaseShared.iNull(siteEnviro.getSiteCode()).toString());
            psTemp.setString("uniqueKey", "");
            rsTemp = psTemp.executeQuery();
            ResultSetMetaData colMetaData = rsTemp.getMetaData();
            for (int i = 1; i <= colMetaData.getColumnCount(); i++) {
                String colName = colMetaData.getColumnName(i);
                DmMessageParam param = new DmMessageParam(colName);
                if (!this.getMessageParams().containsKey(param.getColumnName())) {
                    this.getMessageParams().put(param.getColumnName(), param);
                }
            }

            while (rsTemp.next()) {
                DmWebJobDataRow wjRow = new DmWebJobDataRow();

                Iterator<String> keys = this.getMessageParams().keySet().iterator();
                while (keys.hasNext()) {
                    String key = keys.next();
                    wjRow.getDataRow().put(key, DmBaseShared.xNull(rsTemp.getString(key)));
                }
                wjRow.setMessgageFormat(this.getMessageFormat());
                wjRow.setFormattedMessage(wjRow.getMessageFromUnformatted(this.getMessageFormat()));

                if (!DmBaseShared.xNull(getSchedularModel().getActiveFileDbName()).equals("")) {
                    String directoryPath;
                    DmFileManagerMongo fileManagerMongo = DmBaseServer.getConnectionMongo(DmBaseShared.xNull(getSchedularModel().getActiveFileDbName()).trim());
                    directoryPath = DmBaseServer.Path.sessions + File.separatorChar + DmBaseShared.iNull(this.getTemplateId()).toString();

                    if (listImageMap != null && listImageMap.size() > 0) {
                        for (GrdImage_Mdl mdl : listImageMap) {
                            if (!DmBaseShared.xNull(mdl.getImageCode()).trim().equalsIgnoreCase("")) {
                                mdl.setImageCodeValue(wjRow.getDataRow().get(DmBaseShared.xNull(mdl.getImageCode()).trim()));
                                mdl.setRuleCodeValue(wjRow.getDataRow().get(DmBaseShared.xNull(mdl.getRuleCode()).trim()));

                                if ((!DmBaseShared.xNull(mdl.getImageCodeValue()).trim().equalsIgnoreCase(""))
                                        && (!DmBaseShared.xNull(mdl.getImageCodeValue()).trim().equalsIgnoreCase(""))) {
                                    wjRow.getLstAttachments().addAll(getDataFromMongoDB(true, directoryPath, mdl.getImageCodeValue(), mdl.getRuleCodeValue(), fileManagerMongo, cnTemp));
                                }
                            }
                        }
                    }
                }

                this.addWebJobRow(wjRow);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw ex;
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
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public void fillDataAndParametersFromFile(SiteEnviro_Mdl siteEnviro) throws Exception {
        String readDirectoryPath = DmBaseServer.Path.csvTemplates_SMS_eMail;
        String fullFilePath = readDirectoryPath + "/" + String.valueOf(this.getTemplateId()) + ".csv";

        DmCsvReader reader = new DmCsvReader(fullFilePath);
        reader.readLines();
        for (int i = 0; i < reader.getHeaders().size(); i++) {
            String key = reader.getHeaders().get(i);
            if (!this.getMessageParams().containsKey(key)) {
                DmMessageParam param = new DmMessageParam(key);
                this.getMessageParams().put(key, param);
            }
        }

        for (int i = 0; i < reader.getRows().size(); i++) {
            DmWebJobDataRow wjRow = new DmWebJobDataRow();
            Iterator<String> keys = this.getMessageParams().keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = (String) reader.getRows().get(i).getCells().get(key);
                wjRow.getDataRow().put(key, value);
            }
            this.addWebJobRow(wjRow);
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

            sqlQuery = "Select fileLink, fileType from linkFileCollection where ruleCode = :ruleCode " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmBaseShared.xNull(docId).trim());
            psTemp.setString("ruleCode", DmBaseShared.xNull(ruleCode).trim());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new WebJobAttachment_Mdl();
                node.setMongoFileLink(DmBaseShared.xNull(rsTemp.getString("fileLink")));
                node.setFileRuleCode(DmBaseShared.xNull(ruleCode).trim());
                node.setFileType(DmBaseShared.xNull(rsTemp.getString("fileType")));
                node.setIsManual(DmEnum.DmYesNo.NO);

                fsDBFile = fileManagerMongo.getFileFromFolder((DmBaseShared.xNull(ruleCode).trim()), DmBaseShared.xNull(rsTemp.getString("fileLink")));
                InputStream inputStream = fsDBFile.getInputStream();
                node.setActFileName(fsDBFile.getFilename());

                File sFile = new File(directoryPath);
                if (!sFile.exists()) {
                    sFile.mkdirs();
                }

                fullFilePath = directoryPath + File.separatorChar + DmBaseShared.xNull(rsTemp.getString("fileLink")) + DmBaseShared.xNull(fsDBFile.getFilename());

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
                    node.setFileDirectory(fullFilePath);
                    node.setDisplayFileName(fullFilePath);
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
