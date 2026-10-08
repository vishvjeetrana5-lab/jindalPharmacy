package aarogya.master.serviceMaster.server;

import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import aarogya.selfToolkit.stImportData.server.ServiceMaster;
import aarogya.master.serviceMaster.shared.*;
import aarogya.projectMenu.self.shared.pmDiagnostic;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceMaster_SrvImpl extends DmRSSPrivate implements ServiceMaster_Srv {

    private final String serviceNature = ServiceNature.SERVICE.getId() + "," + ServiceNature.PACKAGE.getId();

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
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select code from patientservicemast where serviceNature in (" + serviceNature + ") ORDER BY NAME;");
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

            sqlWithParam.setSQL("Select name From patientservicemast where serviceNature in(" + serviceNature + ") ");
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
    public DmResult<ServiceMaster_Mdl> getMoveRec(String strKey) {
        DmResult<ServiceMaster_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ServiceMaster_Mdl mrTemp = new ServiceMaster_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  psm.code, psm.name, psm.shortName,pk.nature as packageNature, psm.categoryCode, scat.name AS category, psm.printSNo, psm.u_Name, psm.preparedBy,psm.serviceNature,"
                    + "         psm.groupCode, sg.name AS groupName, psm.serviceRoutine, psm.isActive, psm.isRateEditable, psm.isNightShift, "
                    + "         psm.nightShiftAmount, psm.isDiscountAllow, psm.nightShiftType "
                    + "         from patientServiceMast AS psm "
                    + "         left join patientServiceCatMast AS scat ON psm.categoryCode = scat.code "
                    + "         left join packageMast AS pk ON pk.code = psm.code "
                    + "         left join serviceGroup AS sg ON psm.groupCode = sg.code "
                    + "         where (psm.code = :code) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                mrTemp.setPrnSrNo(DmServer.vNull(rsTemp.getString("printSNo"), "0"));
                mrTemp.setCategoryCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                mrTemp.setCategoryName(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                mrTemp.setActiveTag(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveText(DmShared.xNull(rsTemp.getString("isActive")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setRateEditableTag(DmShared.xNull(rsTemp.getString("isRateEditable")));
                mrTemp.setRateEditableText(DmShared.xNull(rsTemp.getString("isRateEditable")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setNightShiftTag(DmShared.xNull(rsTemp.getString("isNightShift")));
                mrTemp.setNightShiftText(DmShared.xNull(rsTemp.getString("isNightShift")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("nightShiftType")));
                mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("nightShiftType")).equalsIgnoreCase("1") ? "Amt" : "%");
                mrTemp.setServiceNatureCode(DmShared.iNull(rsTemp.getString("serviceNature")).toString());
                if (DmShared.iNull(rsTemp.getString("serviceNature")).intValue() == ServiceNature.PACKAGE.getId()) {
                    if (DmShared.iNull(rsTemp.getString("packageNature")).intValue() == 0) {
                        mrTemp.setServiceNatureName("Hospital Package");
                    } else if (DmShared.iNull(rsTemp.getString("packageNature")).intValue() == 1) {
                        mrTemp.setServiceNatureName("Day Care Package");
                    } else if (DmShared.iNull(rsTemp.getString("packageNature")).intValue() == 2) {
                        mrTemp.setServiceNatureName("Diagnostic Package");
                    }

                } else if (DmShared.iNull(rsTemp.getString("serviceNature")).intValue() == ServiceNature.SERVICE.getId()) {
                    mrTemp.setServiceNatureName(ServiceNature.SERVICE.getValue());
                }
                mrTemp.setNightShiftAmount(DmShared.xNull(rsTemp.getString("nightShiftAmount")));
                mrTemp.setIsDiscountAllowTag(DmShared.xNull(rsTemp.getString("isDiscountAllow")));
                mrTemp.setIsDiscountAllowText(DmShared.xNull(rsTemp.getString("isDiscountAllow")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setRoutineTag(DmShared.xNull(rsTemp.getString("serviceRoutine")));
                mrTemp.setRoutineText(DmShared.xNull(rsTemp.getString("serviceRoutine")).equalsIgnoreCase("0") ? "As Applicable"
                        : DmShared.xNull(rsTemp.getString("serviceRoutine")).equalsIgnoreCase("1") ? "Daily"
                        : DmShared.xNull(rsTemp.getString("serviceRoutine")).equalsIgnoreCase("2") ? "Hourly" : "");
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " select subCode,name from subGroup sg left join serviceLinkDepartment cld on cld.departmentCode = sg.subCode "
                    + " where serviceCode = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            String linkDepartmentName = "";
            String linkDepartmentCode = "";
            while (rsTemp.next()) {
                linkDepartmentName += DmShared.xNull(rsTemp.getString("name")) + ",";
                linkDepartmentCode += DmShared.xNull(rsTemp.getString("subCode")) + ",";
            }
            if (!linkDepartmentCode.equals("")) {
                linkDepartmentCode = linkDepartmentCode.substring(0, linkDepartmentCode.length() - 1);
                linkDepartmentName = linkDepartmentName.substring(0, linkDepartmentName.length() - 1);
            }
            mrTemp.setLinkDepartmentName(linkDepartmentName);
            mrTemp.setLinkDepartmentCode(linkDepartmentCode);
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
    public DmResult<String> saveRecord(ServiceMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, serviceUsed = false, packageDetailDefine = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            mrTemp.setShortName(DmShared.xNull(mrTemp.getShortName()).trim().replace(" ", "").trim());
            ServiceMaster_Vld.chkSave(mrTemp);

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

            /*====================================< Check whether Name already exists or not >====================================*/
            sqlQuery = "Select psm.serviceNature from patientServiceMast psm where psm.name = :name and psm.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String msg = "";
                switch (ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue())) {
                    case SERVICE:
                    case PACKAGE:
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

            /*====================================< Check whether Short name already exists or not >====================================*/
            sqlQuery = "Select serviceNature From patientServiceMast Where shortName =:shortName and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                String msg = "Short Name already exists";
                switch (ServiceNature.getKeyOf(DmShared.iNull(rsTemp.getString("serviceNature")).intValue())) {
                    case PACKAGE:
                    case SERVICE:
                        msg += " in " + new pmMaster().SERVICE_MASTER.caption;
                        break;
                    case OPERATION:
                        msg += " in " + new pmIPD().OPERATION_MASTER.caption;
                        break;
                    case RADIOLOGY_TEST:
                        msg += " in " + new pmDiagnostic().RADIOLOGY_TEST_MASTER.caption;
                        break;
                    case OVERHEAD_SERVICE:
                        msg += " in " + new pmMaster().OVERHEAD_SERVICE_MASTER.caption;
                        break;
                    default:
                        msg += " ";
                        break;
                }
                msg += ".";
                throw new UnsupportedOperationException(msg);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select serviceCode from patientServiceDetail where serviceCode = :serviceCode and isCancelled = 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            serviceUsed = rsTemp.next();
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select packageCode from packageDetail where packageCode = :serviceCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            packageDetailDefine = rsTemp.next();
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getOrganizationCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid CP Code.");
            }

            if (blnAdd) {
                sqlQuery = "Insert into patientServiceMast(code, name, shortName, categoryCode, groupCode, isNightShift, nightShiftAmount, "
                        + " printSNo, u_EntDt, preparedBy, transferDt, serviceRoutine, serviceNature, isActive, isDiscountAllow, isRateEditable ,nightShiftType ) "
                        + " values (:code, :name, :shortName, :categoryCode, :groupCode, :isNightShift, :nightShiftAmount, "
                        + " :printSNo, :u_EntDt, :preparedBy, :transferDt, :serviceRoutine,  :serviceNature, :isActive, :isDiscountAllow, :isRateEditable ,:nightShiftType );";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("categoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getGroupCode()));
                psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrnSrNo(), "0"));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setString("serviceRoutine", DmServer.vNull(mrTemp.getRoutineTag(), "0"));
//                psTemp.setString("serviceNature", DmShared.iNull(ServiceNature.SERVICE.getId()).toString());
                psTemp.setString("isActive", DmServer.vNull(mrTemp.getActiveTag(), "0"));
                psTemp.setString("isRateEditable", DmServer.vNull(mrTemp.getRateEditableTag(), "0"));
                psTemp.setString("isNightShift", DmServer.vNull(mrTemp.getNightShiftTag(), "0"));
                psTemp.setString("nightShiftAmount", DmShared.xNull(mrTemp.getNightShiftAmount()));
                psTemp.setString("isDiscountAllow", DmServer.vNull(mrTemp.getIsDiscountAllowTag(), "0"));
                psTemp.setString("nightShiftType", DmServer.vNull(mrTemp.getTypeCode(), "0"));
                psTemp.setInt("serviceNature", ServiceNature.getValueOf(DmShared.xNull(mrTemp.getServiceNatureName())) == ServiceNature.SERVICE
                        ? ServiceNature.SERVICE.getId() : ServiceNature.PACKAGE.getId());
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
                        + " groupCode=:groupCode,"
                        + " printSNo = :printSNo,"
                        + " u_EntDt = :u_EntDt,"
                        + " u_Name = :u_Name,"
                        + " transferDt = :transferDt,"
                        + " serviceRoutine = :serviceRoutine,"
                        + " isActive = :isActive, "
                        + " isNightShift = :isNightShift, "
                        + " nightShiftType = :nightShiftType, "
                        + " nightShiftAmount = :nightShiftAmount, "
                        + " isRateEditable = :isRateEditable, ";
                if (!serviceUsed && !packageDetailDefine) {
                    sqlQuery += " serviceNature = :serviceNature, ";
                }
                sqlQuery += " isDiscountAllow = :isDiscountAllow "
                        + " where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("groupCode", DmShared.foriegnKey(mrTemp.getGroupCode()));
                psTemp.setString("categoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
                psTemp.setString("printSNo", DmServer.vNull(mrTemp.getPrnSrNo(), "0"));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setString("serviceRoutine", DmServer.vNull(mrTemp.getRoutineTag(), "0"));
                psTemp.setString("isActive", DmServer.vNull(mrTemp.getActiveTag(), "0"));
                psTemp.setString("isRateEditable", DmServer.vNull(mrTemp.getRateEditableTag(), "0"));
                psTemp.setString("isDiscountAllow", DmServer.vNull(mrTemp.getIsDiscountAllowTag(), "0"));
                if (!serviceUsed && !packageDetailDefine) {
                    psTemp.setInt("serviceNature", ServiceNature.getValueOf(DmShared.xNull(mrTemp.getServiceNatureName())) == ServiceNature.SERVICE
                            ? ServiceNature.SERVICE.getId() : ServiceNature.PACKAGE.getId());
                }
                psTemp.setString("isNightShift", DmServer.vNull(mrTemp.getNightShiftTag(), "0"));
                psTemp.setString("nightShiftType", DmServer.vNull(mrTemp.getTypeCode(), "0"));
                psTemp.setString("nightShiftAmount", DmShared.xNull(mrTemp.getNightShiftAmount()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " delete from serviceLinkDepartment where serviceCode  = :serviceCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("serviceCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getLinkDepartmentCode()).equalsIgnoreCase("")) {
                String[] linkDepartmentList = mrTemp.getLinkDepartmentCode().split(",");
                for (String linkDepartment : linkDepartmentList) {
                    sqlQuery = "insert into serviceLinkDepartment(serviceCode, departmentCode) values (:serviceCode, :departmentCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("serviceCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("departmentCode", DmShared.xNull(linkDepartment));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

            }
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

            if (!packageDetailDefine) {
                sqlQuery = "Delete From packageMast Where code= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (ServiceNature.getValueOf(DmShared.xNull(mrTemp.getServiceNatureName())) != ServiceNature.SERVICE && !serviceUsed) {
                sqlQuery = "Insert into packageMast(code, name, shortName, nature, preparedBy, u_EntDt) "
                        + " values (:code, :name, :shortName, :nature, :preparedBy, :u_EntDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
                psTemp.setString("nature", DmShared.iNull(mrTemp.getServiceNatureCode()).toString());
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

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
    public DmResult<String> deleteRecord(String organizationCode, String siteCode, String strKey) {
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

            sqlQuery = "Select serviceCode from patientServicePriceList where serviceCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Service is used in price list,coud not delete.");
            } else {
                sqlQuery = "Delete From patientServiceSiteList Where Code= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", strKey);
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Delete From patientServiceMast Where code= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
                psTemp.executeUpdate();

                sqlQuery = "Delete From packageMast Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(strKey).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp.close();
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

            sqlQuery = "SELECT  psm.CODE, psm.NAME, psm.shortName, scat.name AS categoryName, psm.printSNo, sg.name AS groupName, "
                    + "         psm.nightShiftAmount, "
                    + "         case psm.isDiscountAllow when '1' then 'Yes' else 'No' end as isDiscountAllow,  "
                    + "         case psm.isRateEditable when '1' then 'Yes' else 'No' end as isRateEditable, "
                    + "         case psm.nightShiftType when '1' then 'Amt' else '%' end as nightShiftType, "
                    + "         case psm.isNightShift when '1' then 'Yes' else 'No' end as isNightShift, "
                    + "         case psm.serviceRoutine when '0' then 'As Applicable' when '1' then 'Daily' when '2' then 'Hourly' end as serviceRoutine, "
                    + "         case psm.serviceNature when " + ServiceNature.SERVICE.getId() + " then '" + ServiceNature.SERVICE.getValue() + "' "
                    + "         when " + ServiceNature.PACKAGE.getId() + " then '" + ServiceNature.PACKAGE.getValue() + "' end as serviceNature, "
                    + "         case psm.isActive when '1' then 'Yes' else 'No' end as isActive "
                    + "         FROM patientServiceMast AS psm "
                    + "         LEFT JOIN patientServiceCatMast AS scat ON psm.categoryCode = scat.code "
                    + "         LEFT JOIN serviceGroup AS sg ON psm.groupCode = sg.code "
                    + "         where serviceNature in (" + serviceNature + ") ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setCategoryName(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setPrintSNo(DmServer.vNull(rsTemp.getString("printSNo"), "0"));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setIsDiscountAllow(DmShared.xNull(rsTemp.getString("isDiscountAllow")));
                listNode.setServiceRoutine(DmShared.xNull(rsTemp.getString("serviceRoutine")));
                listNode.setIsType(DmShared.xNull(rsTemp.getString("nightShiftType")));
                listNode.setServiceNatureName(DmShared.xNull(rsTemp.getString("serviceNature")));
                listNode.setIsNightShift(DmShared.xNull(rsTemp.getString("isNightShift")));
                listNode.setNightShiftAmount(DmServer.vNull(rsTemp.getString("nightShiftAmount"), "0.00"));
                listNode.setIsRateEditable(DmShared.xNull(rsTemp.getString("isRateEditable")));
                listNode.setIsActive(DmShared.xNull(rsTemp.getString("isActive")));
//                listNode.setServiceNatureName(ServiceNature.getKeyOf((rsTemp.getInt("serviceNature"))).getValue());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Amt.");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("%");
            lstTemp.add(listNode);

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
                    + "		from patientServiceCatMast as pscm "
                    + "         Left Join its as itsSal on pscm.its_Code = itsSal.Code "
                    + "         left join subgroup as sgSer on pscm.serviceAc=sgSer.subCode"
                    + "         where pscm.filterNature=0 ";
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("SELECT sg.code, sg.name FROM serviceGroup sg ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHServiceRoutine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("As Applicable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Daily");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Hourly");
            lstTemp.add(listNode);

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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<>();
        DefaultValue_Mdl defaultValueMdl = new DefaultValue_Mdl();
        Connection cnTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            defaultValueMdl.setOrganizationCode(DmShared.xNull(DmServer.getCodeViaManualCode("CP", "shortName", "organizationmast", "subCode", cnTemp)));

            if (DmShared.xNull(defaultValueMdl.getOrganizationCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid CP Code. Create 'CP' code.");
            }

            sqlQuery = "select  pcm.name ,pcm.code"
                    + "         from patientServiceCatMast pcm  where name = 'Diagnostic Package'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValueMdl.setDiagnosticCatCode(rsTemp.getString("code"));
                defaultValueMdl.setDiagnosticCatName(rsTemp.getString("name"));

            }
            psTemp.close();
            rsTemp.close();

            defaultValueMdl.setSiteCode(DmServer.getSsnSiteCode(this));

            dmrTemp.setData(defaultValueMdl);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        char separator = File.separatorChar;
        String csvHeader = ImportData_Server.getCSVFormats(new ServiceMaster().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "serviceMaster";
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  psm.name as serviceName, psm.shortName, psm.printSNo,psm.isActive, pscm.name as category, psg.name as groupName, psm.nightShiftAmount,psm.serviceNature, "
                    + "		case psm.serviceRoutine when 0 then 'As Applicable' when 1 then 'Daily' when 2 then 'Hourly' end as serviceRoutine, "
                    + "		case psm.isDiscountAllow when 0 then 'No' else 'Yes' end as isDiscountAllow,"
                    + "		case psm.isNightShift when 0 then 'No' else 'Yes' end as isNightShift, "
                    + "		case psm.isRateEditable when 0 then 'No' else 'Yes' end as isRateEditable "
                    + "		from patientServiceMast psm "
                    + "		left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "		left join serviceGroup psg on psg.code = psm.groupCode "
                    + "		where serviceNature in (  " + ServiceNature.SERVICE.getId() + "," + ServiceNature.PACKAGE.getId() + " )"
                    + "		order by psm.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("serviceName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shortName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("category")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("printSNo")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("groupName")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("serviceRoutine")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isDiscountAllow")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isRateEditable")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("nightShiftAmount")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isNightShift")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(ServiceNature.getKeyOf(rsTemp.getInt("serviceNature")).getValue()) + "\"";
                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLinkDept(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" select sg.subCode as CODE, name as Name from  departmentMast dm left join subGroup sg on  sg.subCode = dm.subCode ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
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
    public DmResultPageing<List<KeyFieldExtended>> getHServiceType(DmHelper helper, boolean blnToEndOfFile
    ) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        rstTemp.setData(lstTemp);
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(ServiceNature.SERVICE.getId()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(ServiceNature.SERVICE.getValue()));
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Hospital Package");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Day Care Package");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Diagnostic Package");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
