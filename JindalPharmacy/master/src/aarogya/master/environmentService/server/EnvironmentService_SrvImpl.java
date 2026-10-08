package aarogya.master.environmentService.server;

import aarogya.master.environmentService.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentService_SrvImpl extends DmRSSPrivate implements EnvironmentService_Srv {

    @Override
    public DmResult<String> saveRecord(EnvironmentService_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            EnvironmentService_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "enviro", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "SELECT  site_Code FROM enviro where site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                sqlQuery = " INSERT INTO enviro(site_Code) VALUES(:site_Code) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Delete From enviroServiceCategory where site_Code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "  Delete from enviroService where site_code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdServiceCategory_Mdl categoryMdl : mrTemp.getGrdGeneral()) {

                if (!DmShared.xNull(categoryMdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "insert into enviroServiceCategory (site_code ,serviceCategoryCode ,serviceCode, "
                            + "         defaultConsultantCode ,defaultDepartmentCode ) "
                            + " values (:site_code, :serviceCategoryCode, :serviceCode, :defaultConsultantCode, :defaultDepartmentCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("site_code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(categoryMdl.getSharedKeyFieldCode()));
                    psTemp.setString("serviceCode", DmShared.foriegnKey(categoryMdl.getLinkedServiceTag()));
                    psTemp.setString("defaultConsultantCode", DmShared.foriegnKey(categoryMdl.getDefConsultantTag()));
                    psTemp.setString("defaultDepartmentCode", DmShared.foriegnKey(categoryMdl.getDefDepartmentTag()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    saveServiceGrid(categoryMdl.getSharedKeyFieldCode(), mrTemp.getSharedKeyFieldCode(), categoryMdl.getLstEnviroService(), cnTemp);
                }
            }

            for (GrdServiceCategory_Mdl diagonsticCatMdl : mrTemp.getGrdDiagnostic()) {

                if (!DmShared.xNull(diagonsticCatMdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "insert into enviroServiceCategory (site_code ,serviceCategoryCode ,serviceCode, "
                            + "         defaultConsultantCode ,defaultDepartmentCode ) "
                            + " values (:site_code, :serviceCategoryCode, :serviceCode, :defaultConsultantCode, :defaultDepartmentCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("site_code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("serviceCategoryCode", DmShared.foriegnKey(diagonsticCatMdl.getSharedKeyFieldCode()));
                    psTemp.setString("serviceCode", DmShared.foriegnKey(diagonsticCatMdl.getLinkedServiceTag()));
                    psTemp.setString("defaultConsultantCode", DmShared.foriegnKey(diagonsticCatMdl.getDefConsultantTag()));
                    psTemp.setString("defaultDepartmentCode", DmShared.foriegnKey(diagonsticCatMdl.getDefDepartmentTag()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    saveServiceGrid(diagonsticCatMdl.getSharedKeyFieldCode(), mrTemp.getSharedKeyFieldCode(), diagonsticCatMdl.getLstEnviroService(), cnTemp);
                }
            }

            //Blood Component tab
            sqlQuery = "  Delete from enviroBloodComponent where site_code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdBloodComponent_Mdl bloodComponent_mdl : mrTemp.getGrdBloodComponent()) {
                if ((DmShared.iNull(bloodComponent_mdl.getSharedKeyFieldCode()).longValue() != 0) && (DmShared.iNull(bloodComponent_mdl.getLinkedServiceTag()).longValue() != 0)) {
                    sqlQuery = "insert into enviroBloodComponent (site_code ,bloodComponentCode ,serviceCode) "
                            + " values (:site_code, :bloodComponentCode, :serviceCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("site_code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("bloodComponentCode", DmShared.foriegnKey(bloodComponent_mdl.getSharedKeyFieldCode()));
                    psTemp.setString("serviceCode", DmShared.foriegnKey(bloodComponent_mdl.getLinkedServiceTag()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT sm.code AS CODE,sm.name AS Name "
                    + "FROM sitemast sm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT cm.subcode as code,sg.name "
                    + "FROM consultantmast cm "
                    + " LEFT join subGroup as sg ON cm.subCode = sg.subCode "
                    + " LEFT join subGroupNature as sgn ON sg.nature = sgn.Code "
                    + " LEFT JOIN subGroupSiteList CSL ON cm.subcode=CSL.subcode "
                    + " WHERE CSL.site_Code=:site_Code AND sg.isActive='1' AND sgn.manualCode='CN' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmShared.xNull(siteCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResult<EnvironmentService_Mdl> getMoveRec(String strKey) {
        DmResult<EnvironmentService_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        EnvironmentService_Mdl mrTemp = new EnvironmentService_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp.getGrdGeneral().addAll(EnviroService_Server.getServiceCategory(strKey, "0", cnTemp));
            mrTemp.getGrdDiagnostic().addAll(EnviroService_Server.getServiceCategory(strKey, "1", cnTemp));
            mrTemp.getGrdBloodComponent().addAll(EnviroService_Server.getBloodComponent(strKey, cnTemp));

            dmrTemp.setData(mrTemp);
            cnTemp.close();
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

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String siteCode, sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            siteCode = DmServer.getSsnSiteCode(this);
            sqlQuery = "SELECT sm.name AS site_name from sitemast sm "
                    + " WHERE sm.Code=:site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", siteCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSiteCode(DmShared.xNull(siteCode));
                mrTemp.setSiteName(DmShared.xNull(rsTemp.getString("site_name")));
            }
            psTemp.close();
            rsTemp.close();

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select sg.subCode, sg.name "
                    + "  from departmentMast dep"
                    + "  left join subgroup sg on sg.subCode = dep.subCode"
                    + "  left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "  left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode"
                    + "  where sg.isActive = 1 and sgsl.site_Code = :siteCode and sgn.manualCode in ('PH')");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.foriegnKey(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.foriegnKey(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT dm.subcode AS code, dm.name FROM subgroup AS dm  ");
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
            rsTemp.close();

            psTemp.close();
            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getServices(DmHelper helper, boolean blnToEndOfFile, ServiceNature[] serviceNatures, String serviceCategory) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "", filterNature = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            if (serviceNatures != null) {
                if (serviceNatures.length > 0) {
                    for (ServiceNature nature : serviceNatures) {
                        if (filterNature.equalsIgnoreCase("")) {
                            filterNature = DmShared.iNull(nature.getId()).toString();
                        } else {
                            filterNature = "," + DmShared.iNull(nature.getId()).toString();
                        }
                    }
                }
            }
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!filterNature.equalsIgnoreCase("")) {
                condition += " and psm.serviceNature in (" + filterNature + ")";
            }
            if (DmShared.iNull(serviceCategory).longValue() > 0) {
                condition += " and psm.categoryCode = :categoryCode";
            }
            sqlWithParam.setSQL("select  psm.code, psm.name "
                    + "		from patientServiceMast psm "
                    + "		left join patientServiceSiteList pssl on pssl.code = psm.code "
                    + "		where pssl.site_Code = :siteCode and psm.isActive = 1" + condition);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(serviceCategory).longValue() > 0) {
                sqlWithParam.setString("categoryCode", serviceCategory);
            }
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
            rsTemp.close();

            psTemp.close();
            rstTemp.setStatus(true);
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

    private void saveServiceGrid(String serviceCategory, String siteCode, List<GrdEnviroSerive_Mdl> grdService, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            for (GrdEnviroSerive_Mdl item_Mdl : grdService) {
                if (DmShared.iNull(item_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    sqlQuery = "insert into enviroService(serviceCode,defaultConsultantCode , site_code )  "
                            + " VALUES (:serviceCode,:defaultConsultantCode ,  :siteCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCode", DmShared.foriegnKey(item_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("defaultConsultantCode", DmShared.foriegnKey(item_Mdl.getDefConsultantTag()));
                    psTemp.setString("siteCode", DmShared.xNull(siteCode));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
