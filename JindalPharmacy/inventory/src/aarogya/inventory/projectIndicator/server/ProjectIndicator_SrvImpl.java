package aarogya.inventory.projectIndicator.server;

import aarogya.inventory.projectIndicator.shared.DefaultValue_Mdl;
import aarogya.inventory.projectIndicator.shared.Find_Mdl;
import aarogya.inventory.projectIndicator.shared.ProjectIndicator_Mdl;
import aarogya.inventory.projectIndicator.shared.ProjectIndicator_Srv;
import aarogya.inventory.projectIndicator.shared.ProjectIndicator_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProjectIndicator_SrvImpl extends DmRSSPrivate implements ProjectIndicator_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCat = "'" + pvc.PROJECT_INDICATOR + "'";

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
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT docID as code FROM projectIndicator ORDER BY v_Date desc ");
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
    public DmResult<ProjectIndicator_Mdl> getMoveRec(String strKey) {
        DmResult<ProjectIndicator_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ProjectIndicator_Mdl mrTemp = new ProjectIndicator_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select pi.docId, pi.v_Type, pi.v_No, pi.recIdPrefix, pi.recId, pi.v_Prefix, pi.v_Date, pi.remark\n"
                    + " , pi.preparedBy, pi.preparedDate, pi.site_Code, pi.u_Name, pi.u_EntDt,vt.description as vTypeDescription\n"
                    + " , vt.guiTemplateCode \n"
                    + "	from projectIndicator pi\n"
                    + "	left join voucher_Type vt on pi.v_Type=vt.v_Type\n"
                    + " Where pi.docID= :docID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docID", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setvDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setvType(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setvTypeDescription(DmShared.xNull(rsTemp.getString("vTypeDescription")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                mrTemp.setvNo(DmShared.xNull(rsTemp.getString("v_No")));
                mrTemp.getRecID().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecID().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecID().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));

                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
            }
            psTemp.close();
            rsTemp.close();
            
            /*==============================<< Get GUI >>===============================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));
            
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
    public DmResult<String> deleteRecord(String strKey, String guiConfigCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);
            
            sqlQuery = "Delete From projectIndicator Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
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
    public DmResult<String> saveRecord(ProjectIndicator_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery,vNo,userName,siteCode;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            ProjectIndicator_Vld.chkSave(mrTemp);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(DmShared.xNull(mrTemp.getSharedKeyFieldName())));
            
            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);
            
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getvType()), voucherPrefix, cnTemp, this));
                mrTemp.setRecID(DmServer.getRecId("projectIndicator", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecID(), mrTemp.getvDate(), DmShared.xNull(mrTemp.getvType()), voucherPrefix, cnTemp, this, true));
            }else {
                DmServer.getRecId("projectIndicator", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecID(), mrTemp.getvDate(), DmShared.xNull(mrTemp.getvType()), voucherPrefix, cnTemp, this, true);
            }
            vNo = DmShared.iNull(mrTemp.getRecID().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "Insert Into projectIndicator(docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, remark, preparedBy, preparedDate, site_Code) Values \n "
                        + "(:docId, :v_Type, :v_No, :recIdPrefix, :recId, :v_Prefix, :v_Date, :remark, :preparedBy, :preparedDate, :site_Code) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("v_Type", DmShared.xNull(mrTemp.getvType()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recId", mrTemp.getRecID().getRecIdValue());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getvDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("site_code", siteCode);
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecID().getPrefix()));
            } else {
                sqlQuery = "Update projectIndicator Set "
                        + "  u_EntDt = :uEntDt ,"
                        + "  u_name = :u_name, "
                        + "  remark = :remark, "
                        + "  recId = :recId, "
                        + "  v_No = :v_No, "
                        + "  v_Date = :v_Date "
                        + "  WHERE docId = :docId ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("u_name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getvNo()).toString());
                psTemp.setString("recId", mrTemp.getRecID().getRecIdValue());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getvDate()));
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), siteCode, cnTemp);
            
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select pi.docId as code,pi.recId as name, pi.v_Date,vt.description as vTypeDescription\n"
                    + "	from projectIndicator pi\n"
                    + "	left join voucher_Type vt on pi.v_Type=vt.v_Type");

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setVrDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setVrTypeDescription(DmShared.xNull(rsTemp.getString("vTypeDescription")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHV_Type(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category =" + vCat + "");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("category")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rsTemp.close();
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setSiteCode(DmServer.getSsnSiteCode(this));

            /*==============================<< Get default voucher_Type >>===============================*/
            defaultValue.setVoucherType(getVoucherType("", cnTemp));

            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);
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

    private KeyFieldExtended getVoucherType(String partyType, Connection cnTemp) throws Exception {
        KeyFieldExtended lstNode = new KeyFieldExtended();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            /*==============================<< Get default voucher_Type >>===============================*/
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " , vt.category"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("category", vCat);

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("category")));
                lstNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
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

        return lstNode;
    }
    @Override
    public DmResult<String[]> getServerDateTime() {
        DmResult<String[]> dmrTemp = new DmResult<String[]>();
        try {
            dmrTemp.setData(DmServer.getServerDateTime());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        }
        return dmrTemp;
    }
    
    private String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
    }
}
