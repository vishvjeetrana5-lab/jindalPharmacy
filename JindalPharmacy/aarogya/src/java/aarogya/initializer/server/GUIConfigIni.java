package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import com.google.gson.Gson;
import dataman.utility.guiConfig.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;

public class GUIConfigIni implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    public GUIConfigIni() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        String fileName;
        BufferedReader br = null;
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
                    status = save(gson.fromJson(br, GUIConfig_Mdl.class));
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

    private int save(GUIConfig_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean flag = false;
        String sqlQuery;
        ResultSet rsTemp = null, rsTemp1 = null;
        int srNo = 0, status = 0, rtnStatus = 0;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);
            if (!mrTemp.getSqlQuery().equalsIgnoreCase("")) {
                psTemp = getPSTemp(mrTemp.getSqlQuery(), cnTemp);
                psTemp.executeQuery();
                psTemp.close();
            } else {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Query not found in " + mrTemp.getSharedKeyFieldName());
            }

            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rssTemp, "guiconfig", cnTemp));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT COUNT(gc.name) AS cnt FROM guiConfig gc WHERE gc.code <> :code AND gc.name = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getSharedKeyFieldName().toLowerCase().trim());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, mrTemp.getSharedKeyFieldName() + " gui is already exists.");
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setTemplateCode(getTemplateCode(DmShared.xNull(mrTemp.getTemplateName()), cnTemp));

            if (DmShared.xNull(mrTemp.getTemplateName()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Template Name not found for GUI: " + mrTemp.getSharedKeyFieldName());
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException(" GUI name not found for: " + mrTemp.getTemplateName());
            }

            if (DmShared.xNull(mrTemp.getTableName()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException(" Table name not found for: " + mrTemp.getTemplateName());
            }

            sqlQuery = "INSERT INTO guiconfig (code, name, tableName, sqlQuery, preparedBy, u_EntDt, reportNamePrefix) "
                    + "VALUES (:code, :name, :tableName, :sqlQuery, :preparedBy, :uEntDt, :reportNamePrefix) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("tableName", mrTemp.getTableName());
            psTemp.setString("reportNamePrefix", mrTemp.getReportPrefix());
            psTemp.setString("sqlQuery", mrTemp.getSqlQuery());
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "INSERT INTO guiconfigapplicableto (code, guiTemplateCode, site_code) "
                    + "VALUES (:code, :guiTemplateCode, :siteCode) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("guiTemplateCode", mrTemp.getTemplateCode());
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rssTemp));
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdData() != null) {
                sqlQuery = "delete from guiConfigDetail where code = :code;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();

                for (GUIRow_Mdl rowMdl : mrTemp.getGrdData()) {
                    if (DmShared.xNull(rowMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        if (rowMdl.getGuiCol() != null) {
                            srNo = 0;
                            for (GUIColumn_Mdl colMdl : rowMdl.getGuiCol()) {
                                if (!DmShared.xNull(colMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                                    srNo++;
                                    sqlQuery = "insert into guiconfigdetail (code, v_SNo, guiRowNo, saveInfieldName, fieldCaption, fieldDataType, "
                                            + "         isMandatory, maxLength,captionWidth, controlWidth, isAllowNegative, allowDecimalPlaces, "
                                            + "         defaultText, defaultCode, viewFieldCode, viewFieldText, helpSqlQuery, helpWidth, helpRowPerPage, "
                                            + "         preparedBy, u_EntDt , customFieldName,captionStyle,controlStyle,fieldName , isCopy) "
                                            + "values (:code, :v_SNo, :guiRowNo, :saveInfieldName, :fieldCaption, :fieldDataType, :isMandatory, "
                                            + "         :maxLength, :captionWidth, :controlWidth, :isAllowNegative, :allowDecimalPlaces, :defaultText, "
                                            + "         :defaultCode, :viewFieldCode, :viewFieldText, :helpSqlQuery, :helpWidth, :helpRowPerPage, "
                                            + "         :preparedBy, :uEntDt , :customFieldName,:captionStyle,:controlStyle,:fieldName,:isCopy) ;";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                                    psTemp.setString("preparedBy", DmServer.getSsnUserName(rssTemp));
                                    psTemp.setString("v_SNo", DmServer.vNull(srNo, "0"));
                                    psTemp.setString("guiRowNo", DmServer.vNull(rowMdl.getSrNo(), "0"));
                                    psTemp.setString("saveInfieldName", DmShared.xNull(colMdl.getSharedKeyFieldName()));
                                    psTemp.setString("fieldCaption", DmShared.xNull(colMdl.getFieldCaption()));
                                    psTemp.setString("fieldDataType", DmServer.vNull(colMdl.getFieldDataTypeTag(), "0"));
                                    psTemp.setString("maxLength", DmServer.vNull(colMdl.getMaxLength(), "0"));
                                    psTemp.setString("customFieldName", DmShared.xNull(colMdl.getCustomFieldName()));
                                    psTemp.setString("captionStyle", DmShared.xNull(colMdl.getCaptionStyle()));
                                    psTemp.setString("controlStyle", DmShared.xNull(colMdl.getControlStyle()));
                                    psTemp.setString("fieldName", DmShared.xNull(colMdl.getFieldName()));
                                    psTemp.setString("isCopy", DmShared.xNull(colMdl.getIsCopyTag()));
                                    psTemp.setString("captionWidth", DmServer.vNull(colMdl.getCaptionWidth(), "0"));
                                    psTemp.setString("controlWidth", DmServer.vNull(colMdl.getControlWidth(), "0"));
                                    psTemp.setString("isMandatory", DmShared.vNull(colMdl.getMandatoryTag()).doubleValue() <= 0 ? "0" : "1");
                                    if (DmShared.vNull(colMdl.getFieldDataTypeTag()).intValue() == 2 || DmShared.vNull(colMdl.getFieldDataTypeTag()).intValue() == 5) {
                                        psTemp.setString("isAllowNegative", DmShared.vNull(colMdl.getNegativeTag()).doubleValue() <= 0 ? "0" : "1");
                                        psTemp.setString("allowDecimalPlaces", DmServer.vNull(colMdl.getDecimalText(), "0"));
                                    } else {
                                        psTemp.setString("isAllowNegative", null);
                                        psTemp.setString("allowDecimalPlaces", null);
                                    }
                                    psTemp.setString("defaultText", DmShared.xNull(colMdl.getDefaultText()));
                                    psTemp.setString("defaultCode", DmShared.xNull(colMdl.getDefaultCode()));
                                    psTemp.setString("viewFieldCode", DmShared.xNull(colMdl.getViewFieldCode()));
                                    psTemp.setString("viewFieldText", DmShared.xNull(colMdl.getViewFieldText()));
                                    if (DmShared.xNull(colMdl.getHelpQuery()).equalsIgnoreCase("")) {
                                        psTemp.setString("helpSqlQuery", null);
                                        psTemp.setString("helpWidth", null);
                                        psTemp.setString("helpRowPerPage", null);
                                    } else {
                                        psTemp.setString("helpSqlQuery", DmShared.xNull(colMdl.getHelpQuery()));
                                        psTemp.setString("helpWidth", DmServer.vNull(colMdl.getHelpWidth(), "0"));
                                        psTemp.setString("helpRowPerPage", DmServer.vNull(colMdl.getHelpRow(), "0"));
                                    }
                                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                        }
                    }
                }
            }

            sqlQuery = "select 	gcd.saveInfieldName "
                    + "         from guiConfigDetail gcd "
                    + "         left join ( "
                    + "             select  sc.name "
                    + "                     from " + DmServer.getSsnDBName(rssTemp) + ".sys.columns sc "
                    + "                     right join " + DmServer.getSsnDBName(rssTemp) + ".sys.objects so on so.object_id = sc.object_id "
                    + "                     left join " + DmServer.getSsnDBName(rssTemp) + ".sys.types st on sc.user_type_id = st.user_type_id "
                    + "                     where (so.name = :tableName) "
                    + "         ) as col on gcd.saveInfieldName = col.name "
                    + "         where isNull(gcd.saveInfieldName,'') <> '' and isNull(col.name,'') = '' "
                    + "             and gcd.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("tableName", mrTemp.getTableName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Save In Field [" + DmShared.xNull(rsTemp.getString("saveInfieldName")) + "] not found in table " + mrTemp.getTableName());
            }
            rsTemp.close();
            psTemp.close();

            psTemp1 = getPSTemp(mrTemp.getSqlQuery(), cnTemp);
            rsTemp1 = psTemp1.executeQuery();
            sqlQuery = "select  gcd.viewFieldCode,gcd.viewFieldText "
                    + "         from guiconfigdetail gcd "
                    + "         where gcd.code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                if (!DmShared.xNull(rsTemp.getString("viewFieldText")).trim().equalsIgnoreCase("")) {
                    flag = false;
                    for (int i = 1; i <= rsTemp1.getMetaData().getColumnCount(); i++) {
                        String colName = rsTemp1.getMetaData().getColumnName(i).trim();

                        if (DmShared.xNull(rsTemp.getString("viewFieldText")).trim().equalsIgnoreCase(colName)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, "View Field Text [" + DmShared.xNull(rsTemp.getString("viewFieldText")) + "] not exists in query.");
                    }
                }

                if (!DmShared.xNull(rsTemp.getString("viewFieldCode")).trim().equalsIgnoreCase("")) {
                    flag = false;
                    for (int i = 1; i <= rsTemp1.getMetaData().getColumnCount(); i++) {
                        String colName = rsTemp1.getMetaData().getColumnName(i).trim();

                        if (DmShared.xNull(rsTemp.getString("viewFieldCode")).trim().equalsIgnoreCase(colName)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        status = DmImportStatus.ERROR.getId();
                        throw new DmException(0, "View Field Code [" + DmShared.xNull(rsTemp.getString("viewFieldCode")) + "] not exists in query.");
                    }
                }

            }

            rsTemp.close();
            psTemp.close();
            rsTemp1.close();
            psTemp1.close();

            sqlQuery = "select 	gcd.saveInfieldName,max(gcd.guiRowNo) guiRowNo "
                    + "         from guiconfigdetail gcd  "
                    + "         where gcd.code = :code  and isNull(gcd.saveInfieldName,'') <> ''  "
                    + "         group by gcd.saveInfieldName "
                    + "         having count(*) > 1 ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Save In Field [" + DmShared.xNull(rsTemp.getString("saveInfieldName")) + "] conflict at row " + DmServer.vNull(rsTemp.getString("guiRowNo"), "0"));
            }
            rsTemp.close();
            psTemp.close();

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

                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }

    private String getTemplateCode(String groupName, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, groupCode = "";
        try {
            groupName = DmShared.removeSpace(groupName);

            groupCode = DmServer.getCodeViaManualCode(groupName, "name", "guiTemplateMast", "code", cnTemp);
            if (DmShared.iNull(groupCode).longValue() == 0) {
                groupCode = DmServer.getSerialNoInteger(rssTemp, "guiTemplateMast", cnTemp);
                if (DmShared.iNull(groupCode).longValue() == 0) {
                    throw new UnsupportedOperationException("Invalid key.");
                }
                sqlQuery = "Insert Into guiTemplateMast(code,name,preparedBy,U_EntDt) Values  "
                        + "(:code ,:name,:preparedBy,:uEntDt) ";
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
                throw new DmException(0, e.getMessage());
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

    private DmPreparedStatement getPSTemp(String sqlQuery, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp;

        sqlQuery = "select * from (" + sqlQuery + ") as t where docId = :docId ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", null);

        return psTemp;
    }
}
