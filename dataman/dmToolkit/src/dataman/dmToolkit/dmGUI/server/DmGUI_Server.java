package dataman.dmToolkit.dmGUI.server;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGUI.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

public class DmGUI_Server {

    public static DmGUI_Mdl getGUIViaTemplateCode(String guiTemplateCode, String siteCode, Connection cnTemp) {
        return getGUI(false, guiTemplateCode, "", "", siteCode, cnTemp);
    }

    public static DmGUI_Mdl getGUIViaConfigCode(String guiConfigCode, String docId, String siteCode, Connection cnTemp) {
        return getGUI(true, guiConfigCode, docId, "", siteCode, cnTemp);
    }

    public static DmGUI_Mdl getGUIViaConfigCode(String guiConfigCode, String docId, String vSNo, String siteCode, Connection cnTemp) {
        return getGUI(true, guiConfigCode, docId, vSNo, siteCode, cnTemp);
    }

    private static DmGUI_Mdl getGUI(boolean searchByGUICode, String guiRelatedCode, String docId, String vSNo, String siteCode, Connection cnTemp) {
        DmGUI_Mdl gui;
        DmGUIRow_Mdl guiRow = null;
        ResultSet rsTemp = null, rsGUI = null;
        DmPreparedStatement psTemp = null, psGUI = null;
        String sqlQuery;
        int prvRowNo;

        try {
            gui = getGUIHeader(searchByGUICode, guiRelatedCode, siteCode, cnTemp);
            if (gui != null) {
                if (DmShared.iNull(docId).longValue() != 0) {
                    sqlQuery = DmShared.xNull(gui.getSqlQuery());
                    sqlQuery = "Select * from ( " + sqlQuery + " ) as t where docId = :docId ";
                    if (DmShared.iNull(vSNo).longValue() != 0) {
                        sqlQuery += " and v_Sno = :v_Sno ";
                        psGUI = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psGUI.setString("docId", docId);
                        psGUI.setString("v_SNo", vSNo);

                    } else {
                        psGUI = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psGUI.setString("docId", docId);
                    }

                    rsGUI = psGUI.executeQuery();
                    rsGUI.next();
                }

                sqlQuery = "Select  *,isNull(gcd.isCopy,1) as isCopyName "
                        + "         from guiConfigDetail gcd "
                        + "         where gcd.code = :code "
                        + "         order by gcd.guiRowNo, gcd.V_SNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", gui.getSharedKeyFieldCode());
                rsTemp = psTemp.executeQuery();
                prvRowNo = -1;
                while (rsTemp.next()) {
                    if (prvRowNo == Integer.valueOf(DmShared.xNull(rsTemp.getString("guiRowNo")))) {
                        addGUI(guiRow, rsTemp, rsGUI);
                    } else {
                        guiRow = new DmGUIRow_Mdl();
                        gui.getGuiRowList().add(guiRow);
                        addGUI(guiRow, rsTemp, rsGUI);
                    }
                    guiRow.setConfigCode(gui.getSharedKeyFieldCode());
                    prvRowNo = Integer.valueOf(DmShared.xNull(rsTemp.getString("guiRowNo")));
                }
                rsTemp.close();
                psTemp.close();
            } else {
                gui = new DmGUI_Mdl();
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsGUI != null) {
                    rsGUI.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psGUI != null) {
                    psGUI.close();
                }
            } catch (Exception e) {
            }
        }

        return gui;
    }

    private static DmGUI_Mdl getGUIHeader(boolean searchByGUICode, String guiRelatedCode, String siteCode, Connection cnTemp) {
        DmGUI_Mdl gui = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "Select  * "
                    + "         from guiConfig gc ";
            if (searchByGUICode) {
                sqlQuery += "   where gc.code = :code ";
            } else {
                sqlQuery += "   left join guiConfigApplicableTo gcat on gc.code = gcat.code "
                        + "     where gcat.guiTemplateCode = :guiTemplateCode and gcat.site_code = :siteCode ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (searchByGUICode) {
                psTemp.setString("code", guiRelatedCode);
            } else {
                psTemp.setString("guiTemplateCode", guiRelatedCode);
                psTemp.setString("siteCode", siteCode);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                gui = new DmGUI_Mdl();
                gui.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                gui.setReportNamePrefix(DmShared.xNull(rsTemp.getString("reportNamePrefix")));
                gui.setSqlQuery(DmShared.xNull(rsTemp.getString("sqlQuery")));
                gui.setTableName(DmShared.xNull(rsTemp.getString("tableName")));
            }
            rsTemp.close();
            psTemp.close();
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
        return gui;
    }

    private static void addGUI(DmGUIRow_Mdl listNode, ResultSet rsTemp, ResultSet rsGUI) throws Exception {
        DmGUIControl_Mdl guiControl;

        guiControl = new DmGUIControl_Mdl();
        guiControl.setSaveInFieldName(DmShared.xNull(rsTemp.getString("saveInFieldName")));
        guiControl.setFieldCaption(DmShared.xNull(rsTemp.getString("fieldCaption")));
        guiControl.setDataType(getDataType(Integer.valueOf(DmShared.xNull(rsTemp.getString("fieldDataType")))));
        guiControl.setCaptionWidth(DmShared.xNull(rsTemp.getString("captionWidth")));
        guiControl.setCaptionStyle(DmShared.xNull(rsTemp.getString("captionStyle")));
        guiControl.setControlWidth(DmShared.xNull(rsTemp.getString("controlWidth")));
        guiControl.setControlStyle(DmShared.xNull(rsTemp.getString("controlStyle")));
        guiControl.setMaxLength(DmShared.xNull(rsTemp.getString("maxLength")));
        guiControl.setAllowDecimalPlaces(DmShared.xNull(rsTemp.getString("allowDecimalPlaces")));

        if (DmShared.iNull(rsTemp.getString("isMandatory")).intValue() == 1) {
            guiControl.setMandatory(true);
        } else {
            guiControl.setMandatory(false);
        }

        if (DmShared.iNull(rsTemp.getString("isCopyName")).intValue() == 1) {
            guiControl.setCopy(true);
        } else {
            guiControl.setCopy(false);
        }

        if (DmShared.iNull(rsTemp.getString("isAllowNegative")).intValue() == 1) {
            guiControl.setAllowNegative(true);
        } else {
            guiControl.setAllowNegative(false);
        }

        guiControl.setViewFieldCode(DmShared.xNull(rsTemp.getString("viewFieldCode")));
        guiControl.setViewFieldText(DmShared.xNull(rsTemp.getString("viewFieldText")));
        guiControl.setHelpSQLQuery(DmShared.xNull(rsTemp.getString("helpSQLQuery")));
        guiControl.setHelpWidth(DmShared.xNull(rsTemp.getString("helpWidth")));
        guiControl.setHelpRowPerPage(DmShared.xNull(rsTemp.getString("helpRowPerPage")));
        guiControl.setCustomFieldName(DmShared.xNull(rsTemp.getString("customFieldName")));
        guiControl.setFieldName(DmShared.xNull(rsTemp.getString("fieldName")));

        if (rsGUI != null) {
            if (rsGUI.getRow() > 0) {
                if (!DmShared.xNull(guiControl.getViewFieldCode()).trim().equalsIgnoreCase("")) {
                    if (guiControl.getDataType() == DmDataType.DATE
                            || guiControl.getDataType() == DmDataType.DATETIME) {
                        guiControl.setDefaultCode(getFormatedValue(guiControl, DmShared.dNull(rsGUI.getDate(guiControl.getViewFieldCode()))));
                    } else {
                        guiControl.setDefaultCode(getFormatedValue(guiControl, rsGUI.getString(guiControl.getViewFieldCode())));
                    }
                }

                if (!DmShared.xNull(guiControl.getViewFieldText()).trim().equalsIgnoreCase("")) {
                    if (guiControl.getDataType() == DmDataType.DATE
                            || guiControl.getDataType() == DmDataType.DATETIME) {
                        guiControl.setDefaultText(getFormatedValue(guiControl, DmShared.dNull(rsGUI.getDate(guiControl.getViewFieldText()))));
                    } else {
                        guiControl.setDefaultText(getFormatedValue(guiControl, rsGUI.getString(guiControl.getViewFieldText())));
                    }
                }
            }
        } else {
            guiControl.setDefaultCode(getFormatedValue(guiControl, rsTemp.getString("defaultCode")));
            guiControl.setDefaultText(getFormatedValue(guiControl, rsTemp.getString("defaultText")));
        }
        listNode.getGUIControlList().add(guiControl);
    }

    public static String getFormatedValue(DmGUIControl_Mdl guiControl, String value) {
        String pattern = "";

        switch (guiControl.getDataType()) {
            case DATE:
                value = DmShared.dNull(value);
                break;
            case DOUBLE:
                pattern = "0";

                for (int i = 0; i < DmShared.vNull(guiControl.getAllowDecimalPlaces()); i++) {
                    if (!pattern.contains(".")) {
                        pattern += ".";
                    }
                    pattern += "0";
                }
                value = DmServer.vNull(value, pattern);
                break;
            case HELPSINGLE:
                value = DmShared.xNull(value);
                break;
            case HELPMULTI:
                value = DmShared.xNull(value);
                break;
            case INTEGER:
                value = DmServer.vNull(value, "0");
                break;
            case CHECKBOX:
                value = DmServer.vNull(value, "0");
                break;
            case CUSTOM:
                break;
            default:
                value = DmShared.xNull(value);
                break;
        }

        return value;
    }

    public static DmDataType getDataType(int dataType) {
        DmDataType dt;

        if (DmDataType.DATE.toInt() == dataType) {
            dt = DmDataType.DATE;
        } else if (DmDataType.DATETIME.toInt() == dataType) {
            dt = DmDataType.DATETIME;
        } else if (DmDataType.DOUBLE.toInt() == dataType) {
            dt = DmDataType.DOUBLE;
        } else if (DmDataType.HELPSINGLE.toInt() == dataType) {
            dt = DmDataType.HELPSINGLE;
        } else if (DmDataType.HELPMULTI.toInt() == dataType) {
            dt = DmDataType.HELPMULTI;
        } else if (DmDataType.INTEGER.toInt() == dataType) {
            dt = DmDataType.INTEGER;
        } else if (DmDataType.STRING.toInt() == dataType) {
            dt = DmDataType.STRING;
        } else if (DmDataType.HINDI.toInt() == dataType) {
            dt = DmDataType.HINDI;
        } else if (DmDataType.STRING_MULTILINE.toInt() == dataType) {
            dt = DmDataType.STRING_MULTILINE;
        } else if (DmDataType.CHECKBOX.toInt() == dataType) {
            dt = DmDataType.CHECKBOX;
        } else if (DmDataType.CUSTOM.toInt() == dataType) {
            dt = DmDataType.CUSTOM;
        } else {
            dt = DmDataType.LABEL;
        }

        return dt;
    }

    public static void deleteViaConfigCode(String guiConfigCode, String docId, String siteCode, Connection cnTemp) {
        delete(true, guiConfigCode, docId, "", siteCode, cnTemp);
    }

    public static void deleteViaConfigCode(String guiConfigCode, String docId, String vSNo, String siteCode, Connection cnTemp) {
        delete(true, guiConfigCode, docId, vSNo, siteCode, cnTemp);
    }

    private static void delete(boolean searchByGUICode, String guiRelatedCode, String docId, String vSNo, String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        DmGUI_Mdl gui;

        try {
            gui = getGUIHeader(searchByGUICode, guiRelatedCode, siteCode, cnTemp);
            if (gui != null) {
                if (DmShared.iNull(vSNo).longValue() != 0) {
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + DmShared.xNull(gui.getTableName()) + " where docId = :docId and v_Sno = :v_Sno ");
                    psTemp.setString("docId", DmShared.iNull(docId).toString());
                    psTemp.setString("v_Sno", DmShared.iNull(vSNo).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                } else {
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, "delete from " + DmShared.xNull(gui.getTableName()) + " where docId = :docId ");
                    psTemp.setString("docId", DmShared.iNull(docId).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {

            }
        }

    }

    public static void saveViaConfigCode(List<DmGUIRow_Mdl> guiRowList, String docId, String siteCode, Connection cnTemp) {
        save(guiRowList, true, "", docId, "", siteCode, cnTemp);
    }

    public static void saveViaConfigCode(List<DmGUIRow_Mdl> guiRowList, String docId, String vSNo, String siteCode, Connection cnTemp) {
        save(guiRowList, true, "", docId, vSNo, siteCode, cnTemp);
    }

    private static void save(List<DmGUIRow_Mdl> guiRowList, boolean searchByGUICode, String guiRelatedCode, String docId, String vSNo, String siteCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", fieldList = "", fieldParam = "";
        DmGUI_Mdl gui;

        try {
            if (DmShared.iNull(docId).longValue() == 0) {
                throw new DmException(0, "Invalid key field.");
            }

            if (guiRowList != null) {
                DmGUI_Vld.chkSave(guiRowList);

                for (DmGUIRow_Mdl guiRow : guiRowList) {
                    if (guiRow != null) {
                        if (DmShared.xNull(sqlQuery).trim().equalsIgnoreCase("")) {
                            if (searchByGUICode) {
                                guiRelatedCode = DmShared.iNull(guiRow.getConfigCode()).toString();
                            }
                            gui = getGUIHeader(searchByGUICode, guiRelatedCode, siteCode, cnTemp);

                            if (gui == null) {
                                return;
                            }
                            sqlQuery = "insert into " + DmShared.xNull(gui.getTableName()) + " ";
                            fieldList = "docId";
                            fieldParam = ":docId";
                            if (DmShared.iNull(vSNo).longValue() != 0) {
                                fieldList += ",v_Sno";
                                fieldParam += ",:v_Sno";
                            }

                        }

                        for (DmGUIControl_Mdl guiControl : guiRow.getGUIControlList()) {
                            if (!DmShared.xNull(guiControl.getSaveInFieldName()).trim().equalsIgnoreCase("")) {
                                fieldList += "," + DmShared.xNull(guiControl.getSaveInFieldName());
                                fieldParam += "," + ":" + DmShared.xNull(guiControl.getSaveInFieldName());
                            }
                        }
                    }
                }

                if (!fieldList.equalsIgnoreCase("")) {
                    delete(searchByGUICode, guiRelatedCode, docId, vSNo, siteCode, cnTemp);
                    sqlQuery = sqlQuery + "(" + fieldList + ") values (" + fieldParam + ") ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", docId);
                    if (DmShared.iNull(vSNo).longValue() != 0) {
                        psTemp.setString("v_Sno", vSNo);
                    }

                    for (DmGUIRow_Mdl guiRow : guiRowList) {
                        for (DmGUIControl_Mdl guiControl : guiRow.getGUIControlList()) {
                            if (!DmShared.xNull(guiControl.getSaveInFieldName()).trim().equalsIgnoreCase("")) {
                                switch (guiControl.getDataType()) {
                                    case DATE:
                                        psTemp.setDate(DmShared.xNull(guiControl.getSaveInFieldName()), DmServer.convertDate(guiControl.getDefaultText()));
                                        break;
                                    case DOUBLE:
                                        if (!DmShared.xNull(guiControl.getDefaultText()).trim().equalsIgnoreCase("")) {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), guiControl.getDefaultText());
                                        } else {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), "0");
                                        }

                                        break;
                                    case HELPSINGLE:
                                        if (!DmShared.xNull(guiControl.getDefaultCode()).trim().equalsIgnoreCase("")) {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), guiControl.getDefaultCode());
                                        } else {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), null);
                                        }
                                        break;
                                    case HELPMULTI:
                                        if (!DmShared.xNull(guiControl.getDefaultCode()).trim().equalsIgnoreCase("")) {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), guiControl.getDefaultCode());
                                        } else {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), null);
                                        }
                                        break;
                                    case INTEGER:
                                        if (!DmShared.xNull(guiControl.getDefaultText()).trim().equalsIgnoreCase("")) {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), guiControl.getDefaultText());
                                        } else {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), "0");
                                        }
                                        break;
                                    case CHECKBOX:
                                        if (!DmShared.xNull(guiControl.getDefaultCode()).trim().equalsIgnoreCase("")) {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), guiControl.getDefaultText());
                                        } else {
                                            psTemp.setString(DmShared.xNull(guiControl.getSaveInFieldName()), "0");
                                        }
                                        break;
                                    case CUSTOM:
                                        break;
                                    default:
                                        psTemp.setNString(DmShared.xNull(guiControl.getSaveInFieldName()), DmShared.xNull(guiControl.getDefaultText()));
                                        break;
                                }
                            }
                        }
                    }
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {

            }
        }
    }

}
