package aarogya.master.overHeadMaster.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.overHeadMaster.shared.*;
import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OverHeadMaster_SrvImpl extends DmRSSPrivate implements OverHeadMaster_Srv {

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
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT CODE FROM patientservicemast where serviceNature = " + ServiceNature.OVERHEAD_SERVICE.getId() + " ORDER BY NAME;");
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
    public DmResultPageing<List<String>> getSuggestion(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select name From patientservicemast where serviceNature = " + ServiceNature.OVERHEAD_SERVICE.getId() + " ");
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

    @Override
    public DmResult<OverHeadMaster_Mdl> getMoveRec(String strKey) {
        DmResult<OverHeadMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        OverHeadMaster_Mdl mrTemp = new OverHeadMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  psm.code, psm.name, psm.shortName, psm.categoryCode, scat.name AS category, psm.u_Name, psm.preparedBy"
                    + "         FROM patientServiceMast AS psm "
                    + "         LEFT JOIN patientServiceCatMast AS scat ON psm.categoryCode = scat.code "
                    + "         WHERE (psm.code = :code) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                mrTemp.setCategoryName(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
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
    public DmResult<String> saveRecord(OverHeadMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).trim().replace(" ", "").trim());
            OverHeadMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "patientServiceMast", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select psm.serviceNature from patientServiceMast psm where psm.name = :name and psm.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String msg = "";
                switch (ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue())) {
                    case SERVICE:
                        msg = "in " + new pmMaster().SERVICE_MASTER.caption;
                        break;
                    case PATHOLOGY_TEST:
                        msg = "in " + new pmDiagnostic().PATHOLOGY_TEST_MASTER.caption;
                        break;
                    case OPERATION:
                        msg = "in " + new pmIPD().OPERATION_MASTER.caption;
                        break;
                    case RADIOLOGY_TEST:
                        msg = "in " + new pmDiagnostic().RADIOLOGY_TEST_MASTER.caption;
                        break;
                    case OVERHEAD_SERVICE:
                        msg = "in " + new pmMaster().OVERHEAD_SERVICE_MASTER.caption;
                        break;
                    default:
                        msg = "";
                        break;
                }
                throw new DmException(0, "Name already exists " + msg + ".");

            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select code from patientServiceMast psm where psm.shortName = :shortName and psm.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()).trim());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert into patientServiceMast(code, name, shortName, categoryCode, groupCode, isNightShift, "
                        + " u_EntDt, preparedBy, transferDt, serviceRoutine, serviceNature, isActive, isDiscountAllow, isRateEditable) "
                        + " values (:code, :name, :shortName, :categoryCode, :groupCode, :isNightShift,  "
                        + "  :u_EntDt, :preparedBy, :transferDt, :serviceRoutine,  :serviceNature, :isActive, :isDiscountAllow, :isRateEditable);";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("categoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
                psTemp.setString("groupCode", DmShared.foriegnKey(""));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setString("serviceRoutine", "0");
                psTemp.setString("serviceNature", DmShared.iNull(ServiceNature.OVERHEAD_SERVICE.getId()).toString());
                psTemp.setString("isActive", "1");
                psTemp.setString("isRateEditable", "0");
                psTemp.setString("isNightShift", "0");
                psTemp.setString("isDiscountAllow", "0");
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

                //<editor-fold defaultstate="collapsed" desc="INSERT IN PATIENTSERVICE_SITE_LIST">
                sqlQuery = "INSERT INTO patientServiceSiteList (CODE, site_Code)  "
                        + " VALUES (:code, :site_Code)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(this)));
                //</editor-fold>
            } else {
                sqlQuery = "Update patientServiceMast set"
                        + " name = :name,"
                        + " shortName = :shortName,"
                        + " categoryCode = :categoryCode,"
                        + " u_EntDt = :u_EntDt,"
                        + " u_Name = :u_Name,"
                        + " transferDt = :transferDt"
                        + " where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("categoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of patientServiceMast >===============================*/
            sqlQuery = "Update  psm set "
                    + "         psm.its_Code = pscm.its_Code, "
                    + "         psm.serviceAc = pscm.serviceAc "
                    + "         from patientServiceMast as psm "
                    + "         left join patientServiceCatMast as pscm on psm.categoryCode = pscm.code "
                    + "         where psm.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<String> deleteRecord(String strKey) {
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

            sqlQuery = "Delete From patientServiceSiteList Where Code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From patientServiceMast Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            spBegin.commit();
            dmrTemp.setStatus(true);
            psTemp.close();
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  psm.CODE, psm.NAME , psm.shortName, scat.name AS categoryName "
                    + "         FROM patientservicemast AS psm "
                    + "         LEFT JOIN patientservicecatmast AS scat ON psm.categoryCode=scat.code "
                    + "         where serviceNature = " + ServiceNature.OVERHEAD_SERVICE.getId() + " ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("categoryName")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCategory_Mdl> lstTemp = new ArrayList<TxtHCategory_Mdl>();
        TxtHCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	pscm.code as categoryCode, pscm.name as categoryName, "
                    + "		itsSal.name as itsSaleName,sgSer.name as serviceAcName "
                    + "         from patientServiceCatMast as pscm"
                    + "         Left Join its as itsSal on pscm.its_Code = itsSal.Code "
                    + "         left join subgroup as sgSer on pscm.serviceAc=sgSer.subCode "
                    + "         where pscm.filterNature=4";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCategory_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setIts_Name(DmShared.xNull(rsTemp.getString("itsSaleName")));
                listNode.setServiceAcName(DmShared.xNull(rsTemp.getString("serviceAcName")));
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

}
