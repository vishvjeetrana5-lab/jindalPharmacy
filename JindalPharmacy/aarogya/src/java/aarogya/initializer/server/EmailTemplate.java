package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum.DmWebJobTypes;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.webJobs.emailTemplateMast.shared.EmailTemplateMast_Mdl;
import dataman.webJobs.emailTemplateMast.shared.GrdEmail_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;

public class EmailTemplate implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public EmailTemplate() {
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
            refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            sessionDir = new File(model.getDataPath());
            if (sessionDir.list() != null && sessionDir.list().length > 0) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, EmailTemplateMast_Mdl.class));
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
            groupCode = DmServer.getCodeViaManualCode(groupName, "name", "wjTemplateGroupMast", "code", cnTemp);
            if (DmShared.iNull(groupCode).longValue() == 0) {
                groupCode = DmServer.getSerialNoInteger(rssTemp, "wjTemplateGroupMast", cnTemp);
                if (DmShared.iNull(groupCode).longValue() == 0) {
                    throw new UnsupportedOperationException("Invalid key.");
                }

                sqlQuery = "insert into wjTemplateGroupMast(code, name, preparedBy, u_EntDt) "
                        + " values (:code, :name, :preparedBy, :uEntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(groupCode));
                psTemp.setString("name", DmShared.xNull(groupName));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }
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

    private int save(EmailTemplateMast_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        int srNo = 0, status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(rssTemp, "wjTemplateMast", cnTemp));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(name) AS cnt FROM wjTemplateMast WHERE (name = :name) AND (code <> :code) and templateType = " + DmShared.iNull(DmWebJobTypes.EMAIL.getWebJobType()).toString() + "";
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
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = DmShared.xNull(mrTemp.getFunctionQuery());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            mrTemp.setTempGroupCode(getGroupCode(DmShared.xNull(mrTemp.getTempGroupName()), cnTemp));

            sqlQuery = "INSERT INTO wjTemplateMast (code, name, templateGroupCode, dataSource, functionName, message,preparedBy,u_EntDt,templateType,subject) "
                    + " VALUES (:code, :name, :templateGroupCode, :dataSource, :functionName, :message,:preparedBy,:uEntDt,:templateType,:subject )";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getTempName());
            psTemp.setString("templateGroupCode", DmShared.foriegnKey(mrTemp.getTempGroupCode()));
            psTemp.setString("dataSource", DmShared.iNull(mrTemp.getDataSource().getSourceType()).toString());
            psTemp.setString("templateType", DmShared.iNull(DmWebJobTypes.EMAIL.getWebJobType()).toString());
            psTemp.setString("functionName", mrTemp.getFunctionText());
            psTemp.setString("message", mrTemp.getMsg());
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
            psTemp.setString("subject", mrTemp.getSubject());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From wjTemplateAttachment Where templateCode= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
            srNo = 0;
            for (int i = 0; i < mrTemp.getGrdEmail().size(); i++) {
                srNo++;
                GrdEmail_Mdl grd = mrTemp.getGrdEmail().get(i);
                if (!DmShared.xNull(grd.getFileName()).equalsIgnoreCase("") && !DmShared.xNull(grd.getProcName()).equalsIgnoreCase("") && !DmShared.xNull(grd.getRptName()).equalsIgnoreCase("")) {
                    sqlQuery = "INSERT INTO wjTemplateAttachment (templateCode, v_SNo, procedureName, rptName, fileName) "
                            + "VALUES (:code, :vSno, :procedureName, :rptName, :fileName);";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setInt("vSno", srNo);
                    psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("procedureName", DmShared.xNull(grd.getProcName()));
                    psTemp.setString("rptName", DmShared.xNull(grd.getRptName()));
                    psTemp.setString("fileName", DmShared.xNull(grd.getFileName()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            spBegin.commit();
            cnTemp.close();
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
