package dataman.utility.guiConfig.server;

import com.google.gson.Gson;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import dataman.utility.guiConfig.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.ZipUtils;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GUIConfig_SrvImpl extends DmRSSPrivate implements GUIConfig_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            rsTemp = cnTemp.createStatement().executeQuery("select gc.code from guiConfig gc order by gc.name");
            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstKeyField.add(lstKeyFieldTmp);
            }
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<GUIConfig_Mdl> getMoveRec(String strKey) {
        DmResult<GUIConfig_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        GUIConfig_Mdl mrTemp = new GUIConfig_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp = getMoveRec(strKey, cnTemp);
            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private GUIConfig_Mdl getMoveRec(String strKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, prvRow;
        GUIConfig_Mdl mrTemp = new GUIConfig_Mdl();
        KeyFieldExtended siteNode;
        GUIRow_Mdl rowNode = null;

        try {
            sqlQuery = "SELECT  gc.code, gc.name, gc.tableName, gc.sqlQuery, gc.preparedBy, gc.u_Name, "
                    + "         gat.guiTemplateCode, gtm.Name as guiTemplateName, gc.reportNamePrefix "
                    + "         FROM guiConfig AS gc "
                    + "         LEFT JOIN guiconfigapplicableto AS gat ON gc.code = gat.code "
                    + "         LEFT JOIN guiTemplateMast AS gtm ON gat.guiTemplateCode = gtm.code "
                    + "         WHERE gc.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSqlQuery(DmShared.xNull(rsTemp.getString("sqlQuery")));
                mrTemp.setTableName(DmShared.xNull(rsTemp.getString("tableName")));
                mrTemp.setTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                mrTemp.setTemplateName(DmShared.xNull(rsTemp.getString("guiTemplateName")));
                mrTemp.setReportPrefix(DmShared.xNull(rsTemp.getString("reportNamePrefix")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  gat.site_code, sm.name, sm.manualCode "
                    + "         from guiConfigApplicableTo AS gat "
                    + "         left join sitemast AS sm ON gat.site_code = sm.code "
                    + "         where gat.code = :code "
                    + "         order by gat.code, gat.site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                siteNode = new KeyFieldExtended();
                siteNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("site_Code")));
                siteNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.getGrdSite().add(siteNode);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  gcd.code, gcd.v_SNo, gcd.guiRowNo, gcd.saveInfieldName, gcd.fieldCaption, gcd.fieldDataType, gcd.isMandatory, "
                    + "         isNull(gcd.isCopy,1) as isCopy, gcd.maxLength, gcd.captionWidth, gcd.controlWidth, gcd.isAllowNegative, "
                    + "         gcd.allowDecimalPlaces, gcd.defaultText, gcd.defaultCode, gcd.viewFieldCode, gcd.viewFieldText, gcd.helpSqlQuery, "
                    + "         gcd.helpWidth, gcd.helpRowPerPage, gcd.customFieldName, gcd.captionStyle, gcd.controlStyle, gcd.fieldName "
                    + "         from guiConfigDetail gcd "
                    + "         where gcd.code = :code "
                    + "         order by gcd.guiRowNo, gcd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            prvRow = "";
            while (rsTemp.next()) {
                if (DmShared.vNull(prvRow).doubleValue() != DmShared.vNull(rsTemp.getString("guiRowNo")).doubleValue()) {
                    rowNode = new GUIRow_Mdl();
                    rowNode.setSrNo(DmServer.vNull(rsTemp.getString("guiRowNo"), "0"));
                    if (DmShared.xNull(rsTemp.getString("saveInfieldName")).trim().equalsIgnoreCase("")) {
                        rowNode.setDescription(DmShared.xNull(rsTemp.getString("fieldCaption")));
                    } else {
                        rowNode.setDescription(DmShared.xNull(rsTemp.getString("saveInfieldName")));
                    }
                    rowNode.getGuiCol().add(fillColumnGrid(rsTemp));
                    mrTemp.getGrdData().add(rowNode);
                } else {
                    if (DmShared.xNull(rsTemp.getString("saveInfieldName")).trim().equalsIgnoreCase("")) {
                        rowNode.setDescription(rowNode.getDescription() + "," + DmShared.xNull(rsTemp.getString("fieldCaption")));
                    } else {
                        rowNode.setDescription(rowNode.getDescription() + "," + DmShared.xNull(rsTemp.getString("saveInfieldName")));
                    }
                    rowNode.getGuiCol().add(fillColumnGrid(rsTemp));
                }
                prvRow = DmShared.xNull(rsTemp.getString("guiRowNo"));
            }
            psTemp.close();
            rsTemp.close();
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

    private GUIColumn_Mdl fillColumnGrid(ResultSet rsTemp) throws Exception {
        GUIColumn_Mdl colNode = new GUIColumn_Mdl();
        colNode.setCaptionWidth(DmServer.vNull(rsTemp.getString("captionWidth"), "0.00"));
        colNode.setControlWidth(DmServer.vNull(rsTemp.getString("controlWidth"), "0.00"));
        colNode.setDecimalText(DmServer.vNull(rsTemp.getString("allowDecimalPlaces"), "0"));
        colNode.setDefaultCode(DmShared.xNull(rsTemp.getString("defaultCode")));
        colNode.setDefaultText(DmShared.xNull(rsTemp.getString("defaultText")));
        colNode.setFieldCaption(DmShared.xNull(rsTemp.getString("fieldCaption")));
        colNode.setFieldDataTypeTag(DmServer.vNull(rsTemp.getString("fieldDataType"), "0"));
        colNode.setFieldDataTypeText(DmEnum.DmDataType.valueOf(DmShared.vNull(rsTemp.getString("fieldDataType")).intValue()));
        colNode.setHelpQuery(DmShared.xNull(rsTemp.getString("helpSqlQuery")));
        colNode.setHelpRow(DmServer.vNull(rsTemp.getString("helpRowPerPage"), "0"));
        colNode.setHelpWidth(DmServer.vNull(rsTemp.getString("helpWidth"), "0.00"));
        colNode.setMandatoryTag(DmServer.vNull(rsTemp.getString("isMandatory"), "0"));
        colNode.setMandatoryText(DmShared.vNull(rsTemp.getString("isMandatory")).intValue() == 0 ? "No" : "Yes");
        colNode.setIsCopyTag(DmServer.vNull(rsTemp.getString("isCopy"), "0"));
        colNode.setIsCopyText(DmShared.vNull(rsTemp.getString("isCopy")).intValue() == 1 ? "Always" : "Never");
        colNode.setMaxLength(DmServer.vNull(rsTemp.getString("maxLength"), "0"));
        colNode.setNegativeTag(DmServer.vNull(rsTemp.getString("isAllowNegative"), "0"));
        colNode.setNegativeText(DmShared.vNull(rsTemp.getString("isAllowNegative")).intValue() == 1 ? "Yes" : "No");
        colNode.setRowSno(DmServer.vNull(rsTemp.getString("guiRowNo"), "0"));
        colNode.setSharedKeyFieldCode(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
        colNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("saveInfieldName")));
        colNode.setViewFieldCode(DmShared.xNull(rsTemp.getString("viewFieldCode")));
        colNode.setViewFieldText(DmShared.xNull(rsTemp.getString("viewFieldText")));
        colNode.setCustomFieldName(DmShared.xNull(rsTemp.getString("customFieldName")));
        colNode.setCaptionStyle(DmShared.xNull(rsTemp.getString("captionStyle")));
        colNode.setControlStyle(DmShared.xNull(rsTemp.getString("controlStyle")));
        colNode.setFieldName(DmShared.xNull(rsTemp.getString("fieldName")));

        return colNode;
    }

    @Override
    public DmResult<String> saveRecord(GUIConfig_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, flag = false;
        String sqlQuery;
        ResultSet rsTemp = null, rsTemp1 = null;
        dmrTemp.setData("");

        try {
            GUIConfig_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (!DmShared.xNull(mrTemp.getSqlQuery()).trim().equalsIgnoreCase("")) {
                psTemp = getPSTemp(mrTemp.getSqlQuery(), cnTemp);
                psTemp.executeQuery();
                psTemp.close();
            }

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "guiConfig", cnTemp));
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==========================< Check whether Name already exist or not >============================*/
            sqlQuery = "Select code from guiConfig where code <> :code and name = :name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(100, "GUI Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (mrTemp.getGrdSite() != null) {
                if (DmShared.iNull(mrTemp.getGrdSite().get(0).getSharedKeyFieldCode()).intValue() != 0) {
                    for (KeyFieldExtended kf : mrTemp.getGrdSite()) {
                        if (DmShared.iNull(kf.getSharedKeyFieldCode()).intValue() != 0) {
                            sqlQuery = "Select code from guiConfigApplicableTo where guiTemplateCode = :guiTemplateCode and site_code = :site_code and code <> :code ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                            psTemp.setString("guiTemplateCode", DmShared.iNull(mrTemp.getTemplateCode()).toString());
                            psTemp.setString("site_code", DmShared.iNull(kf.getSharedKeyFieldCode()).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new DmException(0, "Record already exists on " + mrTemp.getTemplateName().trim() + " and " + DmShared.xNull(kf.getSharedKeyFieldName()));
                            }
                            rsTemp.close();
                            psTemp.close();
                        }
                    }
                } else {
                    sqlQuery = "Select code from guiConfigApplicableTo where guiTemplateCode = :guiTemplateCode and site_code = :site_code and code <> :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("guiTemplateCode", DmShared.iNull(mrTemp.getTemplateCode()).toString());
                    psTemp.setString("site_code", DmServer.getSsnSiteCode(this));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        throw new DmException(0, "Record already exists on " + mrTemp.getTemplateName().trim() + " and current site.");
                    }
                    rsTemp.close();
                    psTemp.close();
                }
            }

            /*====================================< Insert/Update into guiConfig >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert into guiConfig (code, name, tableName, sqlQuery, preparedBy, u_EntDt, reportNamePrefix) "
                        + " values (:code, :name, :tableName, :sqlQuery, :preparedBy, :u_EntDt, :reportNamePrefix) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("tableName", DmShared.xNull(mrTemp.getTableName()).trim());
                psTemp.setString("sqlQuery", DmShared.xNull(mrTemp.getSqlQuery()).trim());
                psTemp.setString("reportNamePrefix", DmShared.xNull(mrTemp.getReportPrefix()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "Update guiConfig set "
                        + " code = :code, "
                        + " name = :name, "
                        + " tableName = :tableName, "
                        + " sqlQuery = :sqlQuery, "
                        + " reportNamePrefix = :reportNamePrefix, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :u_EntDt, "
                        + " transferDt = NULL "
                        + " where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("tableName", DmShared.xNull(mrTemp.getTableName()).trim());
                psTemp.setString("sqlQuery", DmShared.xNull(mrTemp.getSqlQuery()).trim());
                psTemp.setString("reportNamePrefix", DmShared.xNull(mrTemp.getReportPrefix()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from guiConfigApplicableTo >====================================*/
            sqlQuery = "Delete from guiConfigApplicableTo where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from guiConfigDetail >====================================*/
            sqlQuery = "Delete from guiConfigDetail where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert records into guiConfigApplicableTo >====================================*/
            if (mrTemp.getGrdSite() != null) {
                if (!DmShared.xNull(mrTemp.getGrdSite().get(0).getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    for (KeyFieldExtended lstSite : mrTemp.getGrdSite()) {
                        if (!DmShared.xNull(lstSite.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                            sqlQuery = "Insert into guiConfigApplicableTo (code, guiTemplateCode, site_code) "
                                    + " values (:code, :guiTemplateCode, :site_code) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                            psTemp.setString("guiTemplateCode", DmShared.foriegnKey(mrTemp.getTemplateCode()));
                            psTemp.setString("site_code", DmShared.foriegnKey(lstSite.getSharedKeyFieldCode()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                } else {
                    sqlQuery = "Insert into guiConfigApplicableTo (code, guiTemplateCode, site_code) "
                            + " values (:code, :guiTemplateCode, :site_code) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("guiTemplateCode", DmShared.foriegnKey(mrTemp.getTemplateCode()));
                    psTemp.setString("site_code", DmServer.getSsnSiteCode(this));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*====================================< Insert records into guiConfigDetail >====================================*/
            if (mrTemp.getGrdData() != null) {
                for (GUIRow_Mdl rowMdl : mrTemp.getGrdData()) {
                    if (DmShared.xNull(rowMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        if (rowMdl.getGuiCol() != null) {
                            for (GUIColumn_Mdl colMdl : rowMdl.getGuiCol()) {
                                if (!DmShared.xNull(colMdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                                    sqlQuery = "Insert into guiConfigDetail (code, v_SNo, guiRowNo, saveInfieldName, fieldCaption, fieldDataType, isMandatory, isCopy, "
                                            + " maxLength, captionWidth, controlWidth, isAllowNegative, allowDecimalPlaces, defaultText, defaultCode, viewFieldCode, "
                                            + " viewFieldText, helpSqlQuery, helpWidth, helpRowPerPage, customFieldName, captionStyle, controlStyle, fieldName, "
                                            + " preparedBy, u_EntDt, transferDt) "
                                            + " values (:code, :v_SNo, :guiRowNo, :saveInfieldName, :fieldCaption, :fieldDataType, :isMandatory, :isCopy, "
                                            + " :maxLength, :captionWidth, :controlWidth, :isAllowNegative, :allowDecimalPlaces, :defaultText, :defaultCode, :viewFieldCode, "
                                            + " :viewFieldText, :helpSqlQuery, :helpWidth, :helpRowPerPage, :customFieldName, :captionStyle, :controlStyle, :fieldName, "
                                            + " :preparedBy, :u_EntDt, :transferDt) ;";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                                    psTemp.setString("v_SNo", DmServer.vNull(colMdl.getSharedKeyFieldCode(), "0"));
                                    psTemp.setString("guiRowNo", DmServer.vNull(rowMdl.getSrNo(), "0"));
                                    psTemp.setString("saveInfieldName", DmShared.xNull(colMdl.getSharedKeyFieldName()));
                                    psTemp.setString("fieldCaption", DmShared.xNull(colMdl.getFieldCaption()));
                                    psTemp.setString("fieldDataType", DmServer.vNull(colMdl.getFieldDataTypeTag(), "0"));
                                    psTemp.setString("maxLength", DmServer.vNull(colMdl.getMaxLength(), "0"));
                                    psTemp.setString("captionWidth", DmServer.vNull(colMdl.getCaptionWidth(), "0"));
                                    psTemp.setString("controlWidth", DmServer.vNull(colMdl.getControlWidth(), "0"));
                                    psTemp.setString("isMandatory", DmShared.iNull(colMdl.getMandatoryTag()).toString());
                                    psTemp.setString("isCopy", DmShared.iNull(colMdl.getIsCopyTag()).toString());
                                    if (DmShared.iNull(colMdl.getFieldDataTypeTag()).intValue() == 2 || DmShared.iNull(colMdl.getFieldDataTypeTag()).intValue() == 5) {
                                        psTemp.setString("isAllowNegative", DmShared.iNull(colMdl.getNegativeTag()).intValue() <= 0 ? "0" : "1");
                                        psTemp.setString("allowDecimalPlaces", DmServer.vNull(colMdl.getDecimalText(), "0"));
                                    } else {
                                        psTemp.setString("isAllowNegative", null);
                                        psTemp.setString("allowDecimalPlaces", null);
                                    }
                                    psTemp.setString("defaultText", DmShared.xNull(colMdl.getDefaultText()));
                                    psTemp.setString("defaultCode", DmShared.xNull(colMdl.getDefaultCode()));
                                    psTemp.setString("viewFieldCode", DmShared.xNull(colMdl.getViewFieldCode()));
                                    psTemp.setString("viewFieldText", DmShared.xNull(colMdl.getViewFieldText()));
                                    if (DmShared.xNull(colMdl.getHelpQuery()).trim().equalsIgnoreCase("")) {
                                        psTemp.setString("helpSqlQuery", null);
                                        psTemp.setString("helpWidth", null);
                                        psTemp.setString("helpRowPerPage", null);
                                    } else {
                                        psTemp.setString("helpSqlQuery", DmShared.xNull(colMdl.getHelpQuery()));
                                        psTemp.setString("helpWidth", DmServer.vNull(colMdl.getHelpWidth(), "0"));
                                        psTemp.setString("helpRowPerPage", DmServer.vNull(colMdl.getHelpRow(), "0"));
                                    }
                                    psTemp.setString("customFieldName", DmShared.xNull(colMdl.getCustomFieldName()));
                                    psTemp.setString("captionStyle", DmShared.xNull(colMdl.getCaptionStyle()));
                                    psTemp.setString("controlStyle", DmShared.xNull(colMdl.getControlStyle()));
                                    psTemp.setString("fieldName", DmShared.xNull(colMdl.getFieldName()).trim());
                                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                                    psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                                    psTemp.setString("transferDt", null);
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                        }
                    }
                }
            }

            sqlQuery = "Select 	gcd.saveInfieldName "
                    + "         from guiConfigDetail as gcd "
                    + "         left join "
                    + "                 ( "
                    + "                 Select  sc.name "
                    + "                         from " + DmServer.getSsnDBName(this) + ".sys.columns AS sc "
                    + "                         right outer join " + DmServer.getSsnDBName(this) + ".sys.objects as so on so.object_id = sc.object_id "
                    + "                         left outer join " + DmServer.getSsnDBName(this) + ".sys.types as st on sc.user_type_id = st.user_type_id "
                    + "                         where (so.name = :tableName) "
                    + "         ) as col on gcd.saveInfieldName = col.name "
                    + "         where isNull(gcd.saveInfieldName,'') <> '' and isNull(col.name,'') = '' and gcd.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("tableName", mrTemp.getTableName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Save In Field [" + DmShared.xNull(rsTemp.getString("saveInfieldName")) + "] not found in table " + mrTemp.getTableName());
            }
            rsTemp.close();
            psTemp.close();

            psTemp1 = getPSTemp(mrTemp.getSqlQuery(), cnTemp);
            rsTemp1 = psTemp1.executeQuery();
            sqlQuery = "Select  gcd.viewFieldCode,gcd.viewFieldText "
                    + "         from guiconfigdetail gcd "
                    + "         where gcd.code = :code ";
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
                throw new DmException(0, "Save In Field [" + DmShared.xNull(rsTemp.getString("saveInfieldName")) + "] conflict at row " + DmServer.vNull(rsTemp.getString("guiRowNo"), "0"));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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

        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "DELETE FROM guiconfigapplicableto WHERE code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "DELETE FROM guiconfigdetail WHERE code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "DELETE FROM guiconfigdetailhelp WHERE code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "DELETE FROM guiconfig WHERE code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public String getTempKeyCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDefault(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT sm.code, sm.name, sm.manualCode "
                    + "                 FROM sitemast sm "
                    + "                 WHERE sm.pltCode = :pltCode ");
            sqlWithParam.setString("pltCode", DmServer.getSsnPLTCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTemplate(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT Code,Name FROM guiTemplateMast");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTable(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT so.name FROM " + DmServer.getSsnDBName(this) + ".sys.objects so WHERE so.Type = 'U'");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSaveInField(DmHelper helper, boolean blnToEndOfFile, String tableName) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);

        try {
            if (DmShared.xNull(tableName).equalsIgnoreCase("")) {
                throw new DmException(0, "Select Table");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT sc.name AS columnName, st.name AS dataType, sc.max_length "
                    + "                 FROM sys.columns AS sc "
                    + "                 RIGHT OUTER JOIN " + DmServer.getSsnDBName(this) + ".sys.objects AS so ON so.object_id = sc.object_id "
                    + "                 LEFT OUTER JOIN " + DmServer.getSsnDBName(this) + ".sys.types AS st ON sc.user_type_id = st.user_type_id "
                    + "                 WHERE (so.name = '" + tableName + "') AND (sc.name not in ('docId','V_SNo')) ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("columnName")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("columnName")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("max_length")));
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
    public DmResultPageing<List<KeyFieldExtended>> getDataType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmDataType dataType : DmDataType.values()) {
                if (dataType == DmDataType.CHECKBOX
                        || dataType == DmDataType.CUSTOM
                        || dataType == DmDataType.DATE
                        || dataType == DmDataType.DOUBLE
                        || dataType == DmDataType.HELPSINGLE
                        || dataType == DmDataType.HELPMULTI
                        || dataType == DmDataType.INTEGER
                        || dataType == DmDataType.LABEL
                        || dataType == DmDataType.STRING
                        || dataType == DmDataType.STRING_MULTILINE
                        || dataType == DmDataType.HINDI) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(String.valueOf(dataType.toInt()));
                    listNode.setSharedKeyFieldName(DmDataType.valueOf(dataType.toInt()));
                    lstTemp.add(listNode);
                }
            }
            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getCopy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Always");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Never");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getViewFieldCode(DmHelper helper, boolean blnToEndOfFile, String query) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, query);
            psTemp = getPSTemp(query, cnTemp);
            rsTemp = psTemp.executeQuery();
            for (int i = 1; i <= rsTemp.getMetaData().getColumnCount(); i++) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(rsTemp.getMetaData().getColumnName(i));
                listNode.setSharedKeyFieldName(rsTemp.getMetaData().getColumnName(i));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select	gc.code, gc.name, gc.tableName, gtm.Name as guiTemplateName, sm.name as siteName, "
                    + "		gc.reportNamePrefix"
                    + "		from guiconfig gc "
                    + "		left join guiconfigapplicableto gca on gc.code = gca.code "
                    + "		left join siteMast sm on sm.code = gca.site_code "
                    + "		left join guiTemplateMast gtm on gca.guiTemplateCode = gtm.code "
                    + " where  gca.site_code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new Find_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setTableName(DmShared.xNull(rsTemp.getString("tableName")));
                node.setTemplateName(DmShared.xNull(rsTemp.getString("guiTemplateName")));
                node.setReportPrefix(DmShared.xNull(rsTemp.getString("reportNamePrefix")));
                node.setApplicableSite(DmShared.xNull(rsTemp.getString("siteName")));
                lstTemp.add(node);
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

    private DmPreparedStatement getPSTemp(String sqlQuery, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp;

        sqlQuery = "Select * from (" + sqlQuery + ") as t where docId = :docId ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", null);

        return psTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> exportJson(String sessionId, GUIConfig_Mdl mrTemp) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String fileName = "", sqlQuery, directoryPath, serverFileName, fullFilePath;
        char separator = File.separatorChar;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        KeyFieldExtended field = null;
        File sFile = null;
        FileWriter writer = null;
        ZipUtils zipUtils = null;

        try {
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "guiConfig";
            sFile = new File(directoryPath);
            sFile.delete();
            sFile.mkdirs();

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  gc.code, gc.name "
                    + "         from guiConfig gc "
                    + "         order by gc.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                fileName = DmShared.xNull(rsTemp.getString("name")).replaceAll(" ", "_");
                fileName = DmShared.xNull(fileName).replaceAll("/", "_");
                if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                    throw new DmException(0, "File Name not given.");
                }
//                directoryPath = DmServer.Path.sessions + separator + sessionId;
                serverFileName = fileName + "-GUI.json";
                fullFilePath = directoryPath + separator + serverFileName;
                mrTemp = getMoveRec(DmShared.xNull(rsTemp.getString("code")), cnTemp);
                mrTemp.getGrdSite().clear();
                Gson gson = new Gson();
                String json = gson.toJson(mrTemp);
                writer = new FileWriter(fullFilePath);
                writer.write(json);
                writer.close();
            }
            rsTemp.close();
            psTemp.close();

            zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "guiConfig.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("guiConfig.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
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
                if (writer != null) {
                    writer.close();
                }
                if (zipUtils != null) {
                    zipUtils = null;
                }
            } catch (Exception e) {
            }
        }

        return rstTemp;
    }

    @Override
    public DmResult<GUIConfig_Mdl> importJson(String filePath, String fileName) {
        DmResult<GUIConfig_Mdl> dmrTemp = new DmResult<>();
        GUIConfig_Mdl mrTemp = new GUIConfig_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            Gson gson = new Gson();
            String readDirectoryPath = filePath + "\\" + fileName;
            BufferedReader br = new BufferedReader(new FileReader(readDirectoryPath));
            mrTemp = gson.fromJson(br, GUIConfig_Mdl.class);
            mrTemp.setSharedKeyFieldCode("");
            mrTemp.setSharedPreparedBy("");
            mrTemp.setSharedModifiedBy("");

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  sm.code, sm.name, sm.manualCode "
                    + "         FROM sitemast sm "
                    + "         WHERE sm.code = :siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                KeyFieldExtended listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                mrTemp.getGrdSite().add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            dmrTemp.setData(mrTemp);
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
