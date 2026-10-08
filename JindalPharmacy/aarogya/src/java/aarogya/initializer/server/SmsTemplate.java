package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.webJobs.smsTemplate.shared.SmsTemplate_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;

public class SmsTemplate implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public SmsTemplate() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            Gson gson = new Gson();
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, SmsTemplate_Mdl.class));
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    br.close();
                    br = null;
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (sessionDir != null) {
                    sessionDir = null;
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private String getGroupCode(String groupName, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, groupCode = "";

        try {
            groupName = DmShared.removeSpace(groupName);

            groupCode = DmServer.getSerialNoInteger(rssTemp, "wjTemplateGroupMast", cnTemp);
            if (DmShared.iNull(groupCode).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Insert Into wjTemplateGroupMast(code,name,preparedBy,U_EntDt) Values  "
                    + "(:code ,:name,:preparedBy,:uEntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(groupCode));
            psTemp.setString("name", DmShared.xNull(groupName));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            try {

            } catch (Exception ex) {
            }
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
        return groupCode;

    }

    private int save(SmsTemplate_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, sqlQueryError = "";
        ResultSet rsTemp = null;
        int status = 0, rtnStatus = 0;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(rssTemp, "wjTemplateMast", cnTemp));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(name) AS cnt FROM wjTemplateMast WHERE (name = :name) AND (code <> :code) and templateType = " + DmShared.iNull(DmEnum.DmWebJobTypes.SMS.getWebJobType()).toString() + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getTempName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, "Template Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " if object_id('" + DmShared.xNull(mrTemp.getFunctionText()) + "') is not NULL "
                    + "   DROP FUNCTION  " + DmShared.xNull(mrTemp.getFunctionText()) + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            sqlQueryError = psTemp.getQuery();
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = DmShared.xNull(mrTemp.getFunctionQuery());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            sqlQueryError = psTemp.getQuery();
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "SELECT wjtgm.code, wjtgm.name "
                    + "            FROM  wjTemplateGroupMast AS wjtgm    "
                    + "            WHERE wjtgm.name = :templateGroup ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("templateGroup", DmShared.xNull(mrTemp.getTemplateGroupName()).trim());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setTemplateGroupCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setTemplateGroupName(DmShared.xNull(rsTemp.getString("name")));
            } else {
                mrTemp.setTemplateGroupCode(getGroupCode(DmShared.xNull(mrTemp.getTemplateGroupName()), cnTemp));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "INSERT INTO wjTemplateMast (code, name, templateGroupCode, dataSource, functionName, message,preparedBy,u_EntDt,templateType) "
                    + " VALUES (:code, :name, :templateGroupCode, :dataSource, :functionName, :message,:preparedBy,:uEntDt,:templateType )";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getTempName());
            psTemp.setString("templateGroupCode", DmShared.foriegnKey(mrTemp.getTemplateGroupCode()));
            psTemp.setString("dataSource", DmShared.iNull(mrTemp.getDataSource().getSourceType()).toString());
            psTemp.setString("templateType", DmShared.iNull(DmEnum.DmWebJobTypes.SMS.getWebJobType()).toString());
            psTemp.setString("functionName", mrTemp.getFunctionText());
            psTemp.setString("message", mrTemp.getMsg());
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            sqlQueryError = psTemp.getQuery();
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
        return rtnStatus;
    }
}
