package dataman.webJobs.smsTemplate.server;

import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.webJobs.smsTemplate.shared.SmsTemplate_Mdl;
import dataman.webJobs.smsTemplate.shared.*;
import dataman.webJobs.smsTemplate.shared.SmsTemplate_Srv;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.webJobs.self.shared.WebJobs_Shared;
import dataman.webJobs.staffInfo.server.StaffInfo_Server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SmsTemplate_SrvImpl extends DmRSSPrivate implements SmsTemplate_Srv {

    private final WebJobs_Shared wjs = new WebJobs_Shared();

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT code FROM wjTemplateMast  where templateType = :templateType ORDER BY name;");
            psTemp.setString("templateType", DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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

    @Override
    public DmResult<SmsTemplate_Mdl> getMoveRec(String strKey) {
        DmResult<SmsTemplate_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        SmsTemplate_Mdl mrTemp = new SmsTemplate_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select wjtm.code As code, wjtm.name As name ,wjtm.templateGroupCode AS templateGroupCode,wjtm.u_Name AS u_Name,wjtm.preparedBy AS preparedBy, "
                    + "  wjtm.datasource AS datasource, wjtm.functionName AS functionName,wjtm.message AS message ,tgm.name AS templateGroupName,wjtm.apiTemplateId"
                    + "  from wjTemplateMast AS wjtm"
                    + "  left join wjTemplateGroupMast AS tgm on  wjtm.templateGroupCode = tgm.code"
                    + " Where wjtm.code= :code AND wjtm.templateType= :templateType ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.setString("templateType", DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setTempName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setApiTemplateId(DmShared.xNull(rsTemp.getString("apiTemplateId")));
                mrTemp.setTemplateGroupCode(DmShared.xNull(rsTemp.getString("templateGroupCode")));
                mrTemp.setTemplateGroupName(DmShared.xNull(rsTemp.getString("templateGroupName")));
                mrTemp.setDataSource(DmWebJobDataSources.getValueFrom(DmShared.iNull(rsTemp.getString("dataSource")).intValue()));
                mrTemp.setFunctionTag(DmShared.xNull(rsTemp.getString("functionName")));
                mrTemp.setFunctionText(DmShared.xNull(rsTemp.getString("functionName")));
                mrTemp.setMsg(DmShared.xNull(rsTemp.getNString("message")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.getLstStaffInfo().addAll(StaffInfo_Server.getStafInfo(mrTemp.getSharedKeyFieldCode(), cnTemp));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select	so.name as SPECIFIC_NAME,ssm.definition as ROUTINE_DEFINITION "
                    + " from sys.objects so  "
                    + " left join sys.sql_modules ssm on so.object_id = ssm.object_id  "
                    + " where so.type = 'IF' and so.name = :functionName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("functionName", DmShared.xNull(mrTemp.getFunctionText()));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setFunctionQuery(DmShared.xNull(rsTemp.getString("ROUTINE_DEFINITION")));
            }
            psTemp.close();
            rsTemp.close();

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

    @Override
    public DmResult<String> saveRecord(SmsTemplate_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        int openBrace = 0, closeBrace = 0;
        try {
            SmsTemplate_Vld.chkSave(mrTemp);

            if (!DmShared.xNull(mrTemp.getMsg()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getMsg()).contains("{") || DmShared.xNull(mrTemp.getMsg()).contains("}")) {
                    for (int i = 0; i < mrTemp.getMsg().length(); i++) {
                        char msgchar = mrTemp.getMsg().charAt(i);
                        if (msgchar == '{') {
                            openBrace++;
                        }
                        if (msgchar == '}') {
                            closeBrace++;
                        }
                    }
                    if (openBrace != closeBrace) {
                        throw new DmException(0, "Message is in incorrect format.Did you forget to open or close any curly braces? ");
                    }
                }
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setTempName(DmShared.removeSpace(mrTemp.getTempName()));

            sqlQuery = "SELECT COUNT(name) AS cnt FROM wjTemplateMast WHERE (name = :name) AND (code <> :code) and templateType = " + DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString() + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getTempName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new DmException(0, "Template Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "wjTemplateMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (mrTemp.getDataSource().getSourceName().equalsIgnoreCase(DmWebJobDataSources.CSV.getSourceName())) {
                getSaveFile(mrTemp);

            }

            if (blnAdd) {
                sqlQuery = "INSERT INTO wjTemplateMast (code, name, templateGroupCode, dataSource, functionName, message,preparedBy,u_EntDt,templateType,apiTemplateId) "
                        + " VALUES (:code, :name, :templateGroupCode, :dataSource, :functionName, :message,:preparedBy,:uEntDt,:templateType,:apiTemplateId ) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getTempName());
                psTemp.setString("apiTemplateId", mrTemp.getApiTemplateId());
                psTemp.setString("templateGroupCode", DmShared.foriegnKey(mrTemp.getTemplateGroupCode()));
                psTemp.setString("dataSource", DmShared.iNull(mrTemp.getDataSource().getSourceType()).toString());
                psTemp.setString("templateType", DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString());
                psTemp.setString("functionName", mrTemp.getFunctionText());
                psTemp.setNString("message", mrTemp.getMsg());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                if (mrTemp.getDataSource().getSourceType() == DmWebJobDataSources.FUNCTION.getSourceType()) {
                    getDeleteFile(mrTemp);
                } else if (mrTemp.getDataSource().getSourceType() == DmWebJobDataSources.CSV.getSourceType()) {
                    getSaveFile(mrTemp);
                }

                sqlQuery = "UPDATE wjTemplateMast SET "
                        + "code = :code, "
                        + "name = :name, "
                        + "apiTemplateId = :apiTemplateId, "
                        + "templateGroupCode = :templateGroupCode, "
                        + "dataSource = :dataSource, "
                        + "functionName = :functionName, "
                        + "message = :message, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :uEntDt, "
                        + " templateType = :templateType "
                        + "WHERE code = :code ;";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("name", mrTemp.getTempName());
                psTemp.setString("apiTemplateId", mrTemp.getApiTemplateId());
                psTemp.setString("templateGroupCode", DmShared.foriegnKey(mrTemp.getTemplateGroupCode()));
                psTemp.setString("dataSource", DmShared.iNull(mrTemp.getDataSource().getSourceType()).toString());
                psTemp.setString("templateType", DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString());
                psTemp.setString("functionName", mrTemp.getFunctionText());
                psTemp.setNString("message", mrTemp.getMsg());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            StaffInfo_Server.saveStaffInfo(mrTemp.getSharedKeyFieldCode(), mrTemp.getLstStaffInfo(), cnTemp);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);
        SmsTemplate_Mdl mrTemp = new SmsTemplate_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldCode(strKey);
            getDeleteFile(mrTemp);
            StaffInfo_Server.deleteStaffInfo(strKey, cnTemp);
            sqlQuery = "Delete From wjTemplateMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
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

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            String sql = "select wjtm.code As code, wjtm.name As name ,wjtm.templateGroupCode AS templateGroupCode,"
                    + "  wjtm.datasource AS datasource, wjtm.functionName AS functionName,wjtm.message AS message ,tgm.name AS templateGroupName"
                    + "  from wjTemplateMast AS wjtm"
                    + "  left join wjTemplateGroupMast AS tgm on  wjtm.templateGroupCode = tgm.code "
                    + "  where templateType = :templateType ";

            sqlWithParam.setSQL(sql);
            sqlWithParam.setString("templateType", DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString());
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setTempName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setTemplateGroupName(DmShared.xNull(rsTemp.getString("templateGroupName")));
                listNode.setDatasource(DmWebJobDataSources.getValueFrom(DmShared.iNull(rsTemp.getString("datasource")).intValue()));
                listNode.setFunctionName(DmShared.xNull(rsTemp.getString("functionName")));
                listNode.setMsg(DmShared.xNull(rsTemp.getString("message")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUndTempName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT code, name FROM wjTemplateGroupMast ";
            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFunctionName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT name, definition, type_desc "
                    + "  FROM sys.sql_modules m "
                    + " INNER JOIN sys.objects o ON m.object_id=o.object_id "
                    + " WHERE (type_desc like '%function%') and (name like 'sms_%') ";
            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDataSource(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(DmWebJobDataSources.FUNCTION.getSourceType()));
            listNode.setSharedKeyFieldName(DmWebJobDataSources.FUNCTION.getSourceName());
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(String.valueOf(DmWebJobDataSources.CSV.getSourceType()));
            listNode.setSharedKeyFieldName(DmWebJobDataSources.CSV.getSourceName());
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<List<GrdFieldName_Mdl>> getSqlDataSource(String functionName) {
        DmResult<List<GrdFieldName_Mdl>> rstTemp = new DmResult<>();
        List<GrdFieldName_Mdl> lstTemp = new ArrayList<>();
        GrdFieldName_Mdl grdField;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, staffInfo = "";

        try {
            if (functionName.equalsIgnoreCase("")) {
                throw new DmException(0, "Select function name.");
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select * from  dbo." + functionName + "('','','') order by uniqueid offset 0 rows fetch next 1 rows only";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();

            for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
                String colName = rsTemp.getMetaData().getColumnName(i);
                grdField = new GrdFieldName_Mdl();
                grdField.setFieldName(colName);
                grdField.setSharedKeyFieldCode(colName);
                lstTemp.add(grdField);
                if (colName.equalsIgnoreCase(wjs.staffInfo)) {
                    staffInfo = colName;
                }
            }
            mandatoryField(lstTemp);
            rstTemp.setStatus(true);
            rstTemp.setValue1(staffInfo);
            rstTemp.setData(lstTemp);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    @Override
    public DmResult<List<GrdFieldName_Mdl>> getReadFile(SmsTemplate_Mdl mrTemp) {
        DmResult<List<GrdFieldName_Mdl>> rstTemp = new DmResult<>();
        List<GrdFieldName_Mdl> lstTemp = new ArrayList<>();
        GrdFieldName_Mdl grdField;
        BufferedReader br = null;
        String staffInfo = "";
        try {
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                br = new BufferedReader(new FileReader(mrTemp.getUploadFilePath() + File.separatorChar + mrTemp.getUploadFileName()));
            } else {
                File tmpFile = new File(DmServer.Path.csvTemplates_SMS_eMail + File.separatorChar + mrTemp.getSharedKeyFieldCode() + ".csv");
                if (!tmpFile.exists()) {
                    throw new Exception("File not Found to read.");
                }
                br = new BufferedReader(new FileReader(tmpFile));
            }
            String line = null;

            line = br.readLine();
            String[] values = line.split(",");
            for (String str : values) {
                str = str.trim().replace("\"", "");
                str = str.trim().replace("\'", "");
                str = str.trim().replace("$", "");
                if (!DmShared.xNull(str).trim().equalsIgnoreCase("")) {
                    grdField = new GrdFieldName_Mdl();
                    grdField.setFieldName(str);
                    grdField.setSharedKeyFieldCode(str);
                    lstTemp.add(grdField);
                    if (str.equalsIgnoreCase(wjs.staffInfo)) {
                        staffInfo = str;
                    }
                }
            }
            br.close();
            mandatoryField(lstTemp);
            rstTemp.setStatus(true);
            rstTemp.setValue1(staffInfo);
            rstTemp.setData(lstTemp);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<List<String>> getSuggetionTemplate(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT name FROM wjTemplateMast where templateType = " + DmShared.iNull(DmWebJobTypes.SMS.getWebJobType()).toString() + " ");
            helper.addSort("name", DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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

    public void getSaveFile(SmsTemplate_Mdl mrTemp) {
        DmResultPageing<SmsTemplate_Mdl> dmrTemp = new DmResultPageing();

        dmrTemp.setData(mrTemp);

        try {

            String filePath = mrTemp.getFilePath();
            String fileName = mrTemp.getFileName();

            String readFilePath = filePath + "/" + fileName;
            String writeDirectoryPath = DmServer.Path.csvTemplates_SMS_eMail;
            String writelFilePath = writeDirectoryPath + "/" + mrTemp.getSharedKeyFieldCode() + ".csv";

            File wFile = new File(writeDirectoryPath);
            if (!wFile.exists()) {
                wFile.mkdirs();
            }

            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(readFilePath);
                fos = new FileOutputStream(writelFilePath);
                byte[] bytes = new byte[2 * 1024 * 1024];
                int readLen = fis.read(bytes);

                while (readLen != -1) {
                    if (readLen >= bytes.length) {
                        fos.write(bytes);
                    } else if (readLen < bytes.length) {
                        fos.write(bytes, 0, readLen);
                    }
                    readLen = fis.read(bytes);
                }

            } catch (Exception exc) {
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            }

            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getDeleteFile(SmsTemplate_Mdl mrTemp) {
        DmResultPageing<SmsTemplate_Mdl> dmrTemp = new DmResultPageing();

        dmrTemp.setData(mrTemp);

        try {

            String deleteDirectoryPath = DmServer.Path.csvTemplates_SMS_eMail + "/" + mrTemp.getSharedKeyFieldCode() + ".csv";
            File wFile = new File(deleteDirectoryPath);
            if (wFile.exists()) {
                wFile.delete();
            }

            dmrTemp.setStatus(true);
        } catch (Exception ex) {

        }
    }

    private void mandatoryField(List<GrdFieldName_Mdl> lstTemp) throws Exception {

        ArrayList<String> listField = new ArrayList<String>();
        listField.add("UniqueId");
        listField.add("SubCode");
        listField.add("Send_To_Name");
        listField.add("Send_To_Address");
        listField.add("Send_To_Mobile");

        for (int i = 0; i < listField.size(); i++) {
            String lstField = listField.get(i);
            if (lstTemp.stream().filter(m -> m.getFieldName().equalsIgnoreCase(lstField)).count() == 0) {
                throw new DmException(i, "The Field " + lstField + " Not Found Which Is Mandatory.");
            }
        }
    }

    @Override
    public DmResult<KeyFieldExtended> exportJson(String session, SmsTemplate_Mdl mrTemp) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String sessionId = "", fileName = "";
        try {
            sessionId = session;
            fileName = mrTemp.getTempName().replaceAll(" ", "_");
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + "/" + sessionId;
            String serverFileName = fileName + ".json";
            String fullFilePath = directoryPath + "/" + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }

            KeyFieldExtended field = new KeyFieldExtended();
            Gson gson = new Gson();
            String json = gson.toJson(mrTemp);
//            String writeDirectoryPath = DmServer.Path.webJobs + "/" + mrTemp.getOverheadName() + ".json";
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName(serverFileName);
            FileWriter writer = new FileWriter(fullFilePath);
            writer.write(json);
            writer.close();

            rstTemp.setStatus(true);
            rstTemp.setData(field);
        } catch (IOException e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<SmsTemplate_Mdl> importJson(String filePath, String fileName) {
        DmResult<SmsTemplate_Mdl> dmrTemp = new DmResult<>();
        SmsTemplate_Mdl mrTemp = new SmsTemplate_Mdl();

        dmrTemp.setData(mrTemp);

        try {

            Gson gson = new Gson();
            String readDirectoryPath = filePath + "\\" + fileName;
            BufferedReader br = new BufferedReader(new FileReader(readDirectoryPath));
            mrTemp = gson.fromJson(br, SmsTemplate_Mdl.class);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    @Override
    public DmResult<SmsTemplate_Mdl> checkImportedValues(SmsTemplate_Mdl mrTemp) {
        DmResult<SmsTemplate_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery = "", prevFunction = "", newFunction;
        boolean flag = true;
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT wjtgm.code, wjtgm.name "
                    + "            FROM  wjTemplateGroupMast AS wjtgm    "
                    + "            WHERE wjtgm.name= :templateGroup ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("templateGroup", DmShared.xNull(mrTemp.getTemplateGroupName()).trim());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setTemplateGroupCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setTemplateGroupName(DmShared.xNull(rsTemp.getString("name")));
            } else {
                mrTemp.setTemplateGroupCode(DmShared.xNull(rsTemp.getString("")));
                mrTemp.setTemplateGroupName(DmShared.xNull(rsTemp.getString("")));
            }

            psTemp.close();
            rsTemp.close();

            sqlQuery = " select	count(so.name) as cnt "
                    + " from sys.objects so  "
                    + " left join sys.sql_modules ssm on so.object_id = ssm.object_id  "
                    + " where so.type = 'IF' and so.name like :functionName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("functionName", DmShared.xNull(mrTemp.getFunctionText()).trim() + "%");
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                prevFunction = DmShared.xNull(mrTemp.getFunctionText());
                if (rsTemp.getLong("cnt") >= 1) {
                    int count = 1;
                    newFunction = prevFunction + DmShared.iNull(count).toString();
                    for (int i = 0; i < rsTemp.getLong("cnt"); i++) {
                        sqlQuery = " select	so.name as functionName "
                                + "  from sys.objects so   "
                                + "  left join sys.sql_modules ssm on so.object_id = ssm.object_id   "
                                + "  where so.type = 'IF' and so.name = :functionName  ";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setString("functionName", DmShared.xNull(newFunction).trim());
                        rsTemp1 = psTemp1.executeQuery();
                        if (rsTemp1.next()) {
                            count++;
                            newFunction = prevFunction + DmShared.iNull(count).toString();
                        } else {
                            flag = true;
                            break;
                        }
                    }
                    psTemp1.close();
                    rsTemp1.close();
                    mrTemp.setFunctionTag(DmShared.xNull(newFunction));
                    mrTemp.setFunctionText(DmShared.xNull(newFunction));
                    mrTemp.setFunctionQuery(DmShared.xNull(mrTemp.getFunctionQuery().replace(prevFunction, newFunction)));
                }

            } else {
                flag = true;
            }
            rsTemp.close();
            psTemp.close();

            if (flag) {
                sqlQuery = DmShared.xNull(mrTemp.getFunctionQuery());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
                rsTemp.close();

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

}
