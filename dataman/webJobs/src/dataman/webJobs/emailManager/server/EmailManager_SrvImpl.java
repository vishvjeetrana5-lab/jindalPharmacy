package dataman.webJobs.emailManager.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.webJobs.emailManager.shared.*;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.server.DmAnonymousWebJobFactory;
import dataman.dmBase.dmScheduler.server.DmMapFormat;
import dataman.dmBase.dmScheduler.server.DmDynamicDataSourceRow;
import dataman.dmBase.dmScheduler.shared.SiteEnviro_Mdl;
import dataman.dmBase.dmScheduler.shared.WebJobAttachment_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class EmailManager_SrvImpl extends DmRSSPrivate implements EmailManager_Srv {

    private int READ_BUFFER_SIZE = 1 * 1024 * 1024;

    @Override
    public DmResult<String> sendEmail(Template_Mdl templateMdl, DmIFile_Mdl file) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmAnonymousWebJobFactory webJobFactory = null;
        SiteEnviro_Mdl siteEnviro;
        List<WebJobAttachment_Mdl> lstAttachments = new ArrayList<>();
        List<DmDynamicDataSourceRow> lstDynamicRows = new ArrayList<DmDynamicDataSourceRow>();
        List<Long> templateIds = new ArrayList<Long>();
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Select count(*) as cnt from " + templateMdl.getSessionTable() + " where isChecked = 1 and isFiltered = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next() && rsTemp.getLong("cnt") <= 0) {
                throw new DmException(0, "No record selected to send eMail.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select * from " + templateMdl.getSessionTable() + " where isChecked = 1 and isFiltered = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                DmDynamicDataSourceRow sourceRow = new DmDynamicDataSourceRow();
                for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
                    String colName = rsTemp.getMetaData().getColumnName(i);
                    if (colName.equalsIgnoreCase("message")) {
                        sourceRow.setFormattedMessage(rsTemp.getString(colName));
                    }
                    if (colName.equalsIgnoreCase("subject")) {
                        sourceRow.setFormattedSubject(rsTemp.getString(colName));
                    }
                    sourceRow.getCells().put(colName, rsTemp.getString(colName));
                }
                lstDynamicRows.add(sourceRow);
            }
            rsTemp.close();
            psTemp.close();

            webJobFactory = new DmAnonymousWebJobFactory();
            siteEnviro = webJobFactory.getEnviroSettings(this);
            templateIds.add(DmShared.iNull(templateMdl.getSharedKeyFieldCode()).longValue());

            WebJobAttachment_Mdl wjAttachment = new WebJobAttachment_Mdl();
            if (file != null) {
                if (!DmShared.xNull(file.getServerFileName()).trim().equalsIgnoreCase("")) {
                    wjAttachment.setTemplateId(templateIds.get(0));
                    wjAttachment.setIsManual(DmYesNo.YES);
                    wjAttachment.setDisplayFileName(file.getFileName());
                    wjAttachment.setFileDirectory(file.getServerDirectory());
                    wjAttachment.setActFileName(file.getServerFileName());
                    lstAttachments.add(wjAttachment);
                }
            }

            webJobFactory.runJobs(this, siteEnviro, templateIds, lstDynamicRows, lstAttachments);

            sqlQuery = ("update " + templateMdl.getSessionTable() + " set status = 'Send', isChecked = 0  where isChecked = 1 and isFiltered = 1 ");
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();;
                }
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<Template_Mdl>> getTxtHTempName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Template_Mdl>> rstTemp = new DmResultPageing<>();
        List<Template_Mdl> lstTemp = new ArrayList<>();
        Template_Mdl listNode;
        String sessionName;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            sessionName = (this.getLocalRequest().getSession().getId()).replaceAll("-", "_");
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  wtm.code, wtm.name, wtm.message,"
                    + "         wtm.templateGroupCode, wtg.name AS groupName,"
                    + "         wtm.dataSource, wtm.functionName, wtm.subject "
                    + "         FROM wjTemplateMast AS wtm "
                    + "         LEFT JOIN wjTemplateGroupMast AS wtg ON wtg.code = wtm.templateGroupCode "
                    + "         WHERE (wtm.templateType = :templateType)";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("templateType", DmShared.iNull(DmWebJobTypes.EMAIL.getWebJobType()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Template_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setSessionTable("##t" + sessionName + "_" + DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setTemplateGroupTag(DmShared.iNull(rsTemp.getString("templateGroupCode")).toString());
                listNode.setTemplateGroupText(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setFuncOrFileName(DmShared.xNull(rsTemp.getString("functionName")));
                listNode.setMessageTemplate(DmShared.xNull(rsTemp.getNString("message")));
                listNode.setSubjectTemplate(DmShared.xNull(rsTemp.getNString("subject")));
                listNode.setWebJobDataSource(DmWebJobDataSources.getValueFrom(DmShared.iNull(rsTemp.getString("dataSource")).intValue()));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResult<DynamicGrid_Mdl> getMetaData(Template_Mdl templateMdl) {
        DmResult<DynamicGrid_Mdl> rstTemp = new DmResult<DynamicGrid_Mdl>();
        DynamicGrid_Mdl mrTemp = new DynamicGrid_Mdl();
        String siteCode;
        Connection cnTemp = null;

        try {
            siteCode = DmServer.getSsnSiteCode(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.getColumnNameList().clear();
            mrTemp.getColumnNameList().addAll(getDefaultColumns());

            if (DmShared.iNull(templateMdl.getWebJobDataSource().getSourceType()).intValue() == DmWebJobDataSources.CSV.getSourceType()) {
                getCsv(mrTemp, templateMdl, cnTemp);
            } else {
                getSql(mrTemp, templateMdl, siteCode, cnTemp);
            }
            getColumnDataType(mrTemp, templateMdl, cnTemp);
            rstTemp.setData(mrTemp);
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private DynamicGrid_Mdl getColumnDataType(DynamicGrid_Mdl mrTemp, Template_Mdl templateMdl, Connection cnTemp) {
        String sqlQuery = "";
        List<FilterDataPopup_Mdl> lstFilterData = new ArrayList<>();
        FilterDataPopup_Mdl node;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {
            sqlQuery = "select column_name, data_type from tempdb.information_schema.columns where table_name = :tableName";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("tableName", templateMdl.getSessionTable());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new FilterDataPopup_Mdl();
                node.setColumnName(DmShared.xNull(rsTemp.getString("column_name")));
                node.setDataType(DmShared.xNull(rsTemp.getString("data_type")));

                lstFilterData.add(node);

            }
            psTemp.close();
            rsTemp.close();
            mrTemp.getFilterData_Mdls().addAll(lstFilterData);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return mrTemp;
    }

    @Override
    public DmResultPageing<List<DmDynamicGridRow>> getData(boolean isCheckedRecord, SearchValue_Mdl mdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<DmDynamicGridRow>> rstTemp = new DmResultPageing<>();
        List<DmDynamicGridRow> lstTemp = new ArrayList<>();
        rstTemp.setData(lstTemp);
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmDynamicGridRow lstNode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", chkCondition = "", sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            chkCondition = "";
            if (isCheckedRecord) {
                chkCondition += " and isChecked = 1  ";
            }

            sqlQuery = ("update " + mdl.getSessionTable() + " set isFiltered = 0 ");
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            for (FilterDataPopup_Mdl fdMdl : mdl.getFilterDataList()) {
                if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("varchar")) {
                    if (!DmShared.xNull(fdMdl.getHelpText()).trim().equalsIgnoreCase("")) {
                        condition += " AND " + DmShared.xNull(fdMdl.getColumnName()).trim() + " IN (" + DmServer.getParamList(":" + DmShared.xNull(fdMdl.getColumnName()).trim(), fdMdl.getHelpText()).trim() + " )";
                    }
                    if (!DmShared.xNull(fdMdl.getContains()).trim().equalsIgnoreCase("")) {
                        if (DmShared.xNull(fdMdl.getContains()).trim().contains("%")) {
                            condition += " AND " + DmShared.xNull(fdMdl.getColumnName()).trim() + " like '%" + DmShared.xNull(fdMdl.getContains()).trim().replaceAll("%", "") + "%'";
                        } else {
                            condition += " AND " + DmShared.xNull(fdMdl.getColumnName()).trim() + " like '" + DmShared.xNull(fdMdl.getContains()).trim().replaceAll("%", "") + "%'";
                        }
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("date") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("datetime")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        condition += " AND  " + DmShared.xNull(fdMdl.getColumnName()).trim() + " >= :" + DmShared.xNull(fdMdl.getColumnName()).trim() + "From";
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        condition += " AND " + DmShared.xNull(fdMdl.getColumnName()).trim() + " <= :" + DmShared.xNull(fdMdl.getColumnName()).trim() + "To";
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("smallint") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("tinyint") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("bigint")
                        || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("int")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        condition += " AND  " + DmShared.xNull(fdMdl.getColumnName()).trim() + " >= :" + DmShared.xNull(fdMdl.getColumnName()).trim() + "From";
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        condition += " AND " + DmShared.xNull(fdMdl.getColumnName()).trim() + " <= :" + DmShared.xNull(fdMdl.getColumnName()).trim() + "To";
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("float") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("double")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        condition += " AND  " + DmShared.xNull(fdMdl.getColumnName()).trim() + " >= :" + DmShared.xNull(fdMdl.getColumnName()).trim() + "From";
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        condition += " AND " + DmShared.xNull(fdMdl.getColumnName()).trim() + " <= :" + DmShared.xNull(fdMdl.getColumnName()).trim() + "To";
                    }
                }
            }

            sqlWithParam.setSQL("select * from " + mdl.getSessionTable() + " where 1 = 1 " + condition + chkCondition);

            for (FilterDataPopup_Mdl fdMdl : mdl.getFilterDataList()) {
                if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("varchar")) {
                    if (!DmShared.xNull(fdMdl.getHelpText()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setStringList(DmShared.xNull(fdMdl.getColumnName()), DmShared.xNull(fdMdl.getHelpText()).trim().replaceAll(", ", ","));
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("date") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("datetime")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setDate(DmShared.xNull(fdMdl.getColumnName()) + "From", DmServer.convertDate(fdMdl.getValueFrom()));
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setDate(DmShared.xNull(fdMdl.getColumnName()) + "To", DmServer.convertDate(fdMdl.getValueTo()));
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("smallint") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("tinyint") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("bigint")
                        || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("int")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setString(DmShared.xNull(fdMdl.getColumnName()) + "From", DmServer.vNull(fdMdl.getValueFrom(), "0").trim());
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setString(DmShared.xNull(fdMdl.getColumnName()) + "To", DmServer.vNull(fdMdl.getValueTo(), "0").trim());
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("float") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("double")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setString(DmShared.xNull(fdMdl.getColumnName()) + "From", DmServer.vNull(fdMdl.getValueFrom(), "0.00").trim());
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        sqlWithParam.setString(DmShared.xNull(fdMdl.getColumnName()) + "To", DmServer.vNull(fdMdl.getValueTo(), "0.00").trim());
                    }
                }
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new DmDynamicGridRow();
                for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
                    String colName = rsTemp.getMetaData().getColumnName(i);
                    lstNode.getCells().put(colName, DmShared.xNull(rsTemp.getString(colName)));
                }
                lstTemp.add(lstNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update " + mdl.getSessionTable() + " set isFiltered = 1 where  1 = 1  " + condition + chkCondition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            for (FilterDataPopup_Mdl fdMdl : mdl.getFilterDataList()) {
                if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("varchar")) {
                    if (!DmShared.xNull(fdMdl.getHelpText()).trim().equalsIgnoreCase("")) {
                        psTemp.setStringList(DmShared.xNull(fdMdl.getColumnName()), DmShared.xNull(fdMdl.getHelpText()).trim().replaceAll(", ", ","));
                    }
                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("date") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("datetime")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        psTemp.setDate(DmShared.xNull(fdMdl.getColumnName()) + "From", DmServer.convertDate(fdMdl.getValueFrom()));
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        psTemp.setDate(DmShared.xNull(fdMdl.getColumnName()) + "To", DmServer.convertDate(fdMdl.getValueTo()));
                    }

                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("smallint") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("tinyint") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("bigint")
                        || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("int")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        psTemp.setString(DmShared.xNull(fdMdl.getColumnName()) + "From", DmServer.vNull(fdMdl.getValueFrom(), "0").trim());
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        psTemp.setString(DmShared.xNull(fdMdl.getColumnName()) + "To", DmServer.vNull(fdMdl.getValueTo(), "0").trim());
                    }

                } else if (DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("float") || DmShared.xNull(fdMdl.getDataType()).trim().equalsIgnoreCase("double")) {
                    if (!DmShared.xNull(fdMdl.getValueFrom()).trim().equalsIgnoreCase("")) {
                        psTemp.setString(DmShared.xNull(fdMdl.getColumnName()) + "From", DmServer.vNull(fdMdl.getValueFrom(), "0.00").trim());
                    }
                    if (!DmShared.xNull(fdMdl.getValueTo()).trim().equalsIgnoreCase("")) {
                        psTemp.setString(DmShared.xNull(fdMdl.getColumnName()) + "To", DmServer.vNull(fdMdl.getValueTo(), "0.00").trim());
                    }

                }
            }
            psTemp.executeUpdate();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return rstTemp;
    }

    private List<String> getDefaultColumns() {
        List<String> lstTemp = new ArrayList<String>();

        lstTemp.add("isChecked");
        lstTemp.add("uniqueId");
        lstTemp.add("SubCode");
        lstTemp.add("send_To_Name");
        lstTemp.add("Send_To_Address");
        lstTemp.add("send_To_Email");
        lstTemp.add("send_BCc_Email");
        lstTemp.add("send_Cc_Email");
        lstTemp.add("messageTemplate");
        lstTemp.add("subjectTemplate");
        lstTemp.add("subject");
        lstTemp.add("message");
        lstTemp.add("size");
        lstTemp.add("status");

        return lstTemp;
    }

    private void getCsv(DynamicGrid_Mdl mrTemp, Template_Mdl templateMdl, Connection cnTemp) {
        DmCsvReader csvReader = null;
        File uploadedFile = null;
        boolean isColumnFound = false;
        DmPreparedStatement psTemp = null;
        String sqlQuery, sqlColumn = "", sqlValues = "", colMdl = "", message = "", subject = "";
        Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        try {
            uploadedFile = new File(DmServer.Path.csvTemplates_SMS_eMail + "/" + templateMdl.getSharedKeyFieldCode() + ".csv");
            if (!uploadedFile.exists()) {
                throw new DmException(0, "File not found.");
            }
            csvReader = new DmCsvReader(uploadedFile);
            csvReader.readLines();

            for (int i = 0; i < csvReader.getHeaders().size(); i++) {
                String colName = DmShared.xNull(csvReader.getHeaders().get(i)).trim();
                isColumnFound = false;
                for (String tempCol : getDefaultColumns()) {
                    if (DmShared.xNull(tempCol).trim().equalsIgnoreCase(colName)) {
                        isColumnFound = true;
                        break;
                    }
                }
                if ("".equalsIgnoreCase(colName)) {
                    isColumnFound = true;
                    break;
                }
                if (!isColumnFound) {
                    mrTemp.getColumnNameList().add(colMdl);
                    sqlColumn += ", " + colName + " varchar(100)";
                }
            }

//            sqlQuery = "IF OBJECT_ID('tempdb.." + templateMdl.getSessionTable() + "') IS NOT NULL "
//                    + " DROP TABLE " + templateMdl.getSessionTable() + " ;"
//                    + " create  table " + templateMdl.getSessionTable() + " (isChecked tinyInt,formatmsg varchar(500),"
//                    + " message varchar(500), formatSub varchar(Max), "
//                    + "subject varchar(max),uniqueId varchar(50) primary key, subCode varchar(50),"
//                    + "send_To_Name varchar(100), send_To_Address varchar(500), send_To_Email varchar(500),"
//                    + "send_BCc_Email varchar(500), send_Cc_Email varchar(500), size smallInt,isFiltered tinyint, status varchar(50) " + sqlColumn + ") ";
            sqlQuery = "IF OBJECT_ID('tempdb.." + templateMdl.getSessionTable() + "') IS NOT NULL "
                    + " DROP TABLE " + templateMdl.getSessionTable() + " ;"
                    + " create  table " + templateMdl.getSessionTable() + " (isChecked tinyInt,formatmsg varchar(500),"
                    + " send_To_Name varchar(100),send_To_Address varchar(500), send_To_Email varchar(500),"
                    + " send_BCc_Email varchar(500), send_Cc_Email varchar(500),subject varchar(max),message varchar(500), "
                    + " formatSub varchar(Max),uniqueId varchar(50) primary key, subCode varchar(50),"
                    + " size smallInt,isFiltered tinyint, status varchar(50) " + sqlColumn + ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            for (int i = 0; i < csvReader.getRows().size(); i++) {
                DmDynamicGridRow ddgr = csvReader.getRows().get(i);
                sqlColumn = "isChecked, isFiltered,formatMsg, formatSub";
                sqlValues = "1,0, '" + templateMdl.getMessageTemplate() + "', '" + templateMdl.getSubjectTemplate() + "'";
                if (!ddgr.getCells().containsKey("isChecked")) {
                    for (int j = 0; j < csvReader.getHeaders().size(); j++) {
                        String colName = csvReader.getHeaders().get(j).toLowerCase();;

                        if (!(colName.equalsIgnoreCase("isChecked")
                                || colName.equalsIgnoreCase("messageTemplate")
                                || colName.equalsIgnoreCase("message")
                                || colName.equalsIgnoreCase("subjectTemplate")
                                || colName.equalsIgnoreCase("subject")
                                || colName.equalsIgnoreCase("size")
                                || colName.equalsIgnoreCase("status"))) {
                            params.put(colName, String.valueOf(ddgr.getCells().get(colName)));
                            sqlColumn += ", " + colName;
                            sqlValues += ", '" + ddgr.getCells().get(colName) + "'";
                        }
                    }
                }

                message = DmShared.xNull(DmMapFormat.format(templateMdl.getMessageTemplate(), params));
                subject = DmShared.xNull(DmMapFormat.format(templateMdl.getSubjectTemplate(), params));

                sqlColumn += ",message,size,subject,status";
                sqlValues += ",'" + message + "','" + String.valueOf(message.length()) + "','" + subject + "','Pending'";

                sqlQuery = "insert into " + templateMdl.getSessionTable() + " (" + sqlColumn + ") values (" + sqlValues + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }
//            mrTemp.getDynamicGridRowList().clear();
//            mrTemp.getDynamicGridRowList().addAll(csvReader.getRows());

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (csvReader != null) {
                    csvReader = null;
                }
                if (uploadedFile != null) {
                    uploadedFile = null;
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

//    private void getSql(DynamicGrid_Mdl mrTemp, Template_Mdl templateMdl, String siteCode, Connection cnTemp) {
//        String sqlQuery, sqlColumn = "", sqlValues = "", subject = "", message = "";
//        DmPreparedStatement psTemp = null, psTempExecute = null;
//        boolean isColumnFound = false;
//        ResultSet rsTemp = null;
////        DmDynamicGridRow dynamicGridRow;
//        Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
//
//        try {
//            sqlQuery = ("select * from " + DmShared.xNull(templateMdl.getFuncOrFileName()) + "(" + DmShared.iNull(templateMdl.getSharedKeyFieldCode()).toString() + "," + siteCode + ") ");
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
//                String colName = DmShared.xNull(rsTemp.getMetaData().getColumnName(i)).trim();
//                String dataType = DmShared.xNull(rsTemp.getMetaData().getColumnTypeName(i)).trim();
//                isColumnFound = false;
//                for (String tempCol : getDefaultColumns()) {
//                    if (DmShared.xNull(tempCol).trim().equalsIgnoreCase(colName)) {
//                        isColumnFound = true;
//                        break;
//                    }
//                }
//                if ("".equalsIgnoreCase(colName)) {
//                    isColumnFound = true;
//                    break;
//                }
//                if (!isColumnFound) {
//                    mrTemp.getColumnNameList().add(colName);
//                    if (DmShared.xNull(dataType).equalsIgnoreCase("varchar")) {
//                        sqlColumn += ",[" + colName + "]  varchar(100)  ";
//                    } else {
//                        sqlColumn += ",[" + colName + "] " + dataType + " ";
//
//                    }
//                }
//            }
//
////            sqlQuery = "IF OBJECT_ID('tempdb.." + templateMdl.getSessionTable() + "') IS NOT NULL "
////                    + " DROP TABLE " + templateMdl.getSessionTable() + " ;"
////                    + " create  table " + templateMdl.getSessionTable() + " (isChecked tinyInt,formatmsg varchar(500),"
////                    + " message varchar(500), formatSub varchar(Max), "
////                    + "subject varchar(max),uniqueId varchar(50) primary key, subCode varchar(50),"
////                    + "send_To_Name varchar(100), send_To_Address varchar(500), send_To_Email varchar(500),"
////                    + "send_BCc_Email varchar(500), send_Cc_Email varchar(500), size smallInt,isFiltered tinyint, status varchar(50) " + sqlColumn + ") ";
//            sqlQuery = "IF OBJECT_ID('tempdb.." + templateMdl.getSessionTable() + "') IS NOT NULL "
//                    + " DROP TABLE " + templateMdl.getSessionTable() + " ;"
//                    + " create  table " + templateMdl.getSessionTable() + " (isChecked tinyInt,formatmsg varchar(500),"
//                    + " send_To_Name varchar(100),send_To_Address varchar(500), send_To_Email varchar(500),"
//                    + " send_BCc_Email varchar(500), send_Cc_Email varchar(500),subject varchar(max),message varchar(500), "
//                    + " formatSub varchar(Max),uniqueId varchar(50) primary key, subCode varchar(50),"
//                    + " size smallInt,isFiltered tinyint, status varchar(50) " + sqlColumn + ") ";
//            psTempExecute = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTempExecute.execute();
//            psTempExecute.close();
//            while (rsTemp.next()) {
//                params.clear();
//                sqlColumn = "isChecked,isFiltered, formatMsg, formatsub";
//                sqlValues = "1,0," + "'" + DmShared.xNull(templateMdl.getMessageTemplate()) + "' , " + "'" + DmShared.xNull(templateMdl.getSubjectTemplate()) + "'";
//                for (int i = 0; i < mrTemp.getColumnNameList().size(); i++) {
//                    String colName = mrTemp.getColumnNameList().get(i);
//
//                    if (!(colName.equalsIgnoreCase("isChecked")
//                            || colName.equalsIgnoreCase("messageTemplate")
//                            || colName.equalsIgnoreCase("message")
//                            || colName.equalsIgnoreCase("subjectTemplate")
//                            || colName.equalsIgnoreCase("subject")
//                            || colName.equalsIgnoreCase("size")
//                            || colName.equalsIgnoreCase("status"))) {
//
////                        dynamicGridRow = new DmDynamicGridRow();
////                        dynamicGridRow.getCells().put(colName, DmShared.xNull(rsTemp.getString(colName)));
//                        params.put(colName, DmShared.xNull(rsTemp.getString(colName)));
////                        mrTemp.getDynamicGridRowList().add(dynamicGridRow);
//                        sqlColumn += ", " + colName;
//                        sqlValues += ", '" + DmShared.xNull(rsTemp.getString(colName)) + "'";
//                    }
//                }
//
//                message = DmShared.xNull(DmMapFormat.format(templateMdl.getMessageTemplate(), params));
//                subject = DmShared.xNull(DmMapFormat.format(templateMdl.getSubjectTemplate(), params));
//
//                sqlColumn += ",message,size,subject,status";
//                sqlValues += ",'" + message + "','" + String.valueOf(message.length()) + "','" + subject + "','Pending'";
//
//                sqlQuery = "insert into " + templateMdl.getSessionTable() + " (" + sqlColumn + ") values (" + sqlValues + ")";
//                psTempExecute = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTempExecute.executeUpdate();
//                psTempExecute.close();
//            }
//        } catch (SQLException | DmException e) {
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
//    }
    private void getSql(DynamicGrid_Mdl mrTemp, Template_Mdl templateMdl, String siteCode, Connection cnTemp) {
        String sqlQuery, sqlColumn = "", sqlValues = "", subject = "", message = "";
        DmPreparedStatement psTemp = null, psMsg = null, psTempExecute = null;
        boolean isColumnFound = false;
        ResultSet rsTemp = null;
        Map<String, String> params = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        try {
            sqlQuery = ("select * from " + DmShared.xNull(templateMdl.getFuncOrFileName()) + "(" + DmShared.iNull(templateMdl.getSharedKeyFieldCode()).toString() + "," + siteCode + " ,'' ) ");
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
                String colName = DmShared.xNull(rsTemp.getMetaData().getColumnName(i)).trim();
                String dataType = DmShared.xNull(rsTemp.getMetaData().getColumnTypeName(i)).trim();
                isColumnFound = false;
                for (String tempCol : getDefaultColumns()) {
                    if (DmShared.xNull(tempCol).trim().equalsIgnoreCase(colName)) {
                        isColumnFound = true;
                        break;
                    }
                }
                if ("".equalsIgnoreCase(colName)) {
                    isColumnFound = true;
                    break;
                }
                if (!isColumnFound) {
                    mrTemp.getColumnNameList().add(colName);
                    if (DmShared.xNull(dataType).equalsIgnoreCase("varchar")) {
                        sqlColumn += ",[" + colName + "]  varchar(100)  ";
                    } else {
                        sqlColumn += ",[" + colName + "] " + dataType + " ";

                    }
                }
            }

            sqlQuery = "IF OBJECT_ID('tempdb.." + templateMdl.getSessionTable() + "') IS NOT NULL "
                    + " DROP TABLE " + templateMdl.getSessionTable() + " ;"
                    + " create  table " + templateMdl.getSessionTable() + " (isChecked tinyInt,formatmsg nvarchar(max),"
                    + " send_To_Name varchar(100),send_To_Address varchar(500), send_To_Email varchar(500),"
                    + " send_BCc_Email varchar(500), send_Cc_Email varchar(500),subject nvarchar(500),message nvarchar(max), "
                    + " formatSub nvarchar(500),uniqueId varchar(50) primary key, subCode varchar(50),"
                    + " size smallInt,isFiltered tinyint, status varchar(50) " + sqlColumn + ") ";
            psTempExecute = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTempExecute.execute();
            psTempExecute.close();

            //Prepare Table Template START
            sqlColumn = "isChecked, isFiltered, formatMsg, formatsub";
            sqlValues = ":isChecked, :isFiltered, :formatMsg, :formatsub";
            for (int i = 0; i < mrTemp.getColumnNameList().size(); i++) {
                String colName = mrTemp.getColumnNameList().get(i);

                if (!(colName.equalsIgnoreCase("isChecked")
                        || colName.equalsIgnoreCase("messageTemplate")
                        || colName.equalsIgnoreCase("message")
                        || colName.equalsIgnoreCase("subjectTemplate")
                        || colName.equalsIgnoreCase("subject")
                        || colName.equalsIgnoreCase("size")
                        || colName.equalsIgnoreCase("status"))) {

                    sqlColumn += ", " + colName;
                    sqlValues += ", :" + DmShared.xNull(colName).trim();
                }
            }
            sqlColumn += ", message, size, subject, status";
            sqlValues += ", :message, :size, :subject, :status";
            sqlQuery = "insert into " + templateMdl.getSessionTable() + " (" + sqlColumn + ") values (" + sqlValues + ")";
            //Prepare Table Template END

            while (rsTemp.next()) {
                params.clear();
//                sqlColumn = "isChecked,isFiltered, formatMsg, formatsub";
//                sqlValues = "1,0," + "'" + DmShared.xNull(templateMdl.getMessageTemplate()) + "' , " + "'" + DmShared.xNull(templateMdl.getSubjectTemplate()) + "'";

                psMsg = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psMsg.setInt("isChecked", 1);
                psMsg.setInt("isFiltered", 0);
                psMsg.setNString("formatMsg", DmShared.xNull(templateMdl.getMessageTemplate()));
                psMsg.setNString("formatsub", DmShared.xNull(templateMdl.getSubjectTemplate()));

                for (int i = 0; i < mrTemp.getColumnNameList().size(); i++) {
                    String colName = mrTemp.getColumnNameList().get(i);

                    if (!(colName.equalsIgnoreCase("isChecked")
                            || colName.equalsIgnoreCase("messageTemplate")
                            || colName.equalsIgnoreCase("message")
                            || colName.equalsIgnoreCase("subjectTemplate")
                            || colName.equalsIgnoreCase("subject")
                            || colName.equalsIgnoreCase("size")
                            || colName.equalsIgnoreCase("status"))) {

                        params.put(colName, DmShared.xNull(rsTemp.getString(colName)));

//                        sqlColumn += ", " + colName;
//                        sqlValues += ", '" + DmShared.xNull(rsTemp.getString(colName)) + "'";
                        psMsg.setNString(colName, DmShared.xNull(rsTemp.getString(colName)));
                    }
                }

                message = DmShared.xNull(DmMapFormat.format(templateMdl.getMessageTemplate(), params));
                subject = DmShared.xNull(DmMapFormat.format(templateMdl.getSubjectTemplate(), params));

//                sqlColumn += ",message,size,subject,status";
//                sqlValues += ",'" + message + "','" + String.valueOf(message.length()) + "','" + subject + "','Pending'";
                psMsg.setNString("message", message);
                psMsg.setInt("size", message.length());
                psMsg.setNString("subject", subject);
                psMsg.setString("status", "Pending");
                psMsg.executeUpdate();
                psMsg.close();

//                sqlQuery = "insert into " + templateMdl.getSessionTable() + " (" + sqlColumn + ") values (" + sqlValues + ")";
//                psTempExecute = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTempExecute.executeUpdate();
//                psTempExecute.close();
            }
        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTempExecute != null) {
                    psTempExecute.close();
                }
                if (psMsg != null) {
                    psMsg.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResult<String> updateRecord(Template_Mdl templateMdl, DmDynamicGridRow ddgr, boolean isCheckAll, boolean isCheckChange) {
        DmResult<String> rstTemp = new DmResult<String>();
        rstTemp.setData("");
        String sqlQuery, updateVal = "", condition = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {
            if (!isCheckAll) {
                if (ddgr != null) {
                    if (!DmShared.xNull(ddgr.getCells().get("uniqueId").toString()).equalsIgnoreCase("")) {
                        if (isCheckChange) {
                            updateVal = " isChecked = (case when isnull(isChecked, 0) = 0 then 1 else 0 end) ";
                        } else {
                            updateVal = " message= '" + ddgr.getCells().get("message").toString()
                                    + "' , size = '" + DmShared.iNull(ddgr.getCells().get("size")).intValue() + "' "
                                    + ", subject = '" + DmShared.xNull(ddgr.getCells().get("subject").toString()) + "'";
                        }
                        condition = " Where uniqueId= '" + ddgr.getCells().get("uniqueId").toString() + "' ";
                    }
                } else {
                    updateVal = " isChecked = 0 ";
                    condition = " Where isnull(isFiltered,1) = 1 ";
                }

            } else {
                updateVal = " isChecked = 1 ";
                condition = " Where isnull(isFiltered,1) = 1 and status <> 'Send' ";
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = ("update " + templateMdl.getSessionTable()
                    + " set " + updateVal + " " + condition);
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getHelp(String columnName, String tableName, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select distinct " + columnName + " as name from " + tableName);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setData(lstTemp);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getCountRecords(Template_Mdl template_Mdl) {
        DmResultPageing<KeyFieldExtended> rstTemp = new DmResultPageing<>();
        KeyFieldExtended node = new KeyFieldExtended();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  "
                    + " count(case  when isChecked = '1' and isFiltered = '1' then 1 else null end ) as selectedRecords, "
                    + " count(case isFiltered when '1' then 1 else null end) as filteredRecords,  "
                    + " count(uniqueId) as prepareRecords, "
                    + " count(case status when 'Send' then 1 else null end ) as sendRecords from " + template_Mdl.getSessionTable();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                node.setSharedKeyFieldCode(rsTemp.getString("prepareRecords"));
                node.setSharedKeyFieldName(rsTemp.getString("filteredRecords"));
                node.setSharedPreparedBy(rsTemp.getString("selectedRecords"));
                node.setSharedModifiedBy(rsTemp.getString("sendRecords"));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            rstTemp.setData(node);
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return rstTemp;
    }

}
