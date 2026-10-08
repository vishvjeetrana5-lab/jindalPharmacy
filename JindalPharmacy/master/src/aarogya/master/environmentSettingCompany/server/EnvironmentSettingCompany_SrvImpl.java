package aarogya.master.environmentSettingCompany.server;

import aarogya.master.environmentSettingCompany.shared.*;
import com.google.gson.Gson;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.ZipUtils;
import dataman.dmBase.self.shared.*;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnvironmentSettingCompany_SrvImpl extends DmRSSPrivate implements EnvironmentSettingCompany_Srv {

    @Override
    public DmResult<EnvironmentSettingCompany_Mdl> getMoveRec(String strKey) {
        DmResult<EnvironmentSettingCompany_Mdl> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(moveRec(cnTemp));
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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

    private EnvironmentSettingCompany_Mdl moveRec(Connection cnTemp) {
        EnvironmentSettingCompany_Mdl mrTemp = new EnvironmentSettingCompany_Mdl();
        GrdPatientInfo_Mdl node = null;
        GrdPriority_Mdl priority_Mdl = null;
        GrdPatientClass_Mdl grdPatientClass_Mdl = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "select  ec.acGroupConsultantCode, acg1.groupName as acGroupConsultantName, "
                    + "         ec.acGroupDepartmentCode, acg3.groupName as acGroupDepartmentName, "
                    + "         ec.acGroupOrganizationCode, acg4.groupName as acGroupOrganizationName, "
                    + "         ec.acGroupReferredCode, acg5.groupName as acGroupReferredName, "
                    + "         ec.acGroupEmployeeCode, acg6.groupName as acGroupEmployeeName, "
                    + "         ec.acGroupPharmacySupplierCode, acg7.groupName as acGroupPharmacySupplierName, "
                    + "         ec.acGroupInventorySupplierCode, acg11.groupName as acGroupInventorySupplierName, "
                    + "         ec.acGroupManufacturerCode, acg8.groupName as acGroupManufacturerName, "
                    + "         ec.acGroupDivisionCode, acg9.groupName as acGroupDivisionName, "
                    + "         ec.acGroupCustomerCode, acg10.groupName as acGroupCustomerName,ec.isInventoryAccountingActive "
                    + "         from enviroCompany ec "
                    + "         Left join acGroup acg1 on ec.acGroupConsultantCode =acg1.groupCode "
                    + "         Left join acGroup acg3 on ec.acGroupDepartmentCode =acg3.groupCode "
                    + "         Left join acGroup acg4 on ec.acGroupOrganizationCode =acg4.groupCode"
                    + "         Left join acGroup acg5 on ec.acGroupReferredCode =acg5.groupCode"
                    + "         Left join acGroup acg6 on ec.acGroupEmployeeCode =acg6.groupCode"
                    + "         Left join acGroup acg7 on ec.acGroupPharmacySupplierCode =acg7.groupCode"
                    + "         Left join acGroup acg8 on ec.acGroupManufacturerCode =acg8.groupCode"
                    + "         Left join acGroup acg9 on ec.acGroupDivisionCode =acg9.groupCode"
                    + "         Left join acGroup acg10 on ec.acGroupCustomerCode =acg10.groupCode"
                    + "         Left join acGroup acg11 on ec.acGroupInventorySupplierCode =acg11.groupCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                mrTemp.setAcGroupConsCode(DmShared.xNull(rsTemp.getString("acGroupConsultantCode")));
                mrTemp.setAcGroupConsName(DmShared.xNull(rsTemp.getString("acGroupConsultantName")));
                mrTemp.setAcGroupDepartCode(DmShared.xNull(rsTemp.getString("acGroupDepartmentCode")));
                mrTemp.setAcGroupDepartName(DmShared.xNull(rsTemp.getString("acGroupDepartmentName")));
                mrTemp.setAcGroupOrgCode(DmShared.xNull(rsTemp.getString("acGroupOrganizationCode")));
                mrTemp.setAcGroupOrgName(DmShared.xNull(rsTemp.getString("acGroupOrganizationName")));
                mrTemp.setAcGroupRefCode(DmShared.xNull(rsTemp.getString("acGroupReferredCode")));
                mrTemp.setAcGroupRefName(DmShared.xNull(rsTemp.getString("acGroupReferredName")));
                mrTemp.setAcGroupEmpCode(DmShared.xNull(rsTemp.getString("acGroupEmployeeCode")));
                mrTemp.setAcGroupEmpName(DmShared.xNull(rsTemp.getString("acGroupEmployeeName")));
                mrTemp.setAcGroupPharmacySupplierCode(DmShared.xNull(rsTemp.getString("acGroupPharmacySupplierCode")));
                mrTemp.setAcGroupPharmacySupplierName(DmShared.xNull(rsTemp.getString("acGroupPharmacySupplierName")));
                mrTemp.setAcGroupInventorySupplierCode(DmShared.xNull(rsTemp.getString("acGroupInventorySupplierCode")));
                mrTemp.setAcGroupInventorySupplierName(DmShared.xNull(rsTemp.getString("acGroupInventorySupplierName")));
                mrTemp.setAcGroupManufacturerCode(DmShared.xNull(rsTemp.getString("acGroupManufacturerCode")));
                mrTemp.setAcGroupManufacturerName(DmShared.xNull(rsTemp.getString("acGroupManufacturerName")));
                mrTemp.setAcGroupDivisionCode(DmShared.xNull(rsTemp.getString("acGroupDivisionCode")));
                mrTemp.setAcGroupDivisionName(DmShared.xNull(rsTemp.getString("acGroupDivisionName")));
                mrTemp.setAcGroupCustomerCode(DmShared.xNull(rsTemp.getString("acGroupCustomerCode")));
                mrTemp.setAcGroupCustomerName(DmShared.xNull(rsTemp.getString("acGroupCustomerName")));
                mrTemp.setInventoryAccountingActive(DmShared.xNull(rsTemp.getString("isInventoryAccountingActive")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select code,nature,sNo,isVisible,isMandatory from managePageLayout order by sNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPatientInfo_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("id") ? "Id"
                        : DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("registration") ? "Registration"
                        : DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("patientName") ? "Patient Name"
                        : DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("email") ? "E-Mail"
                        : DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("mobile") ? "Mobile"
                        : DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("uhid") ? "UHID"
                        : DmShared.xNull(rsTemp.getString("code")).equalsIgnoreCase("guardianName") ? "Guardian Name" : "");
                node.setNature(DmShared.xNull(rsTemp.getString("nature")));
                node.setSrNo(DmShared.iNull(rsTemp.getString("sNo")).toString());
                node.setIsMandatoryTag(DmShared.iNull(rsTemp.getString("isMandatory")).toString());
                node.setIsMandatoryText(DmShared.xNull(rsTemp.getString("isMandatory")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isMandatory")).equalsIgnoreCase("1") ? "Yes" : ""));
                node.setIsVisibleTag(DmShared.iNull(rsTemp.getString("isVisible")).toString());
                node.setIsVisibleText(DmShared.xNull(rsTemp.getString("isVisible")).equalsIgnoreCase("0") ? "No"
                        : (DmShared.xNull(rsTemp.getString("isVisible")).equalsIgnoreCase("1") ? "Yes" : ""));
                mrTemp.getGrdPatientInfoList().add(node);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  code,name as description,shortName,priorityIndex,colorName from hl7Mast where tableName='priorityType'"
                    + " order by priorityIndex desc, shortName asc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                priority_Mdl = new GrdPriority_Mdl();
                priority_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                priority_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shortName")));
                priority_Mdl.setDescription(DmShared.xNull(rsTemp.getString("description")));
                priority_Mdl.setSorting(DmShared.iNull(rsTemp.getString("priorityIndex")).toString());
                priority_Mdl.setColor(DmShared.xNull(rsTemp.getString("colorName")));
                mrTemp.getGrdPriorityList().add(priority_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  ptm.name, ptm.code, rlm.name as rateListName, ptm.rateListCode "
                    + "         from patientTypeMast ptm "
                    + "         left join rateListMast rlm on rlm.code = ptm.rateListCode "
                    + "         where ptm.isActive = 1 "
                    + "         order by ptm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdPatientClass_Mdl = new GrdPatientClass_Mdl();
                grdPatientClass_Mdl.setRateListCode(DmShared.xNull(rsTemp.getString("rateListCode")));
                grdPatientClass_Mdl.setRateListName(DmShared.xNull(rsTemp.getString("rateListName")));
                grdPatientClass_Mdl.setPatientClassCode(DmShared.xNull(rsTemp.getString("code")));
                grdPatientClass_Mdl.setPatientClassName(DmShared.xNull(rsTemp.getString("name")));

                mrTemp.getGrdPatientClassList().add(grdPatientClass_Mdl);
            }
            rsTemp.close();
            psTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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

    @Override
    public DmResult<String> saveRecord(EnvironmentSettingCompany_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        boolean isAdd = true;

        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "SELECT count(*) as cnt FROM enviroCompany";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    isAdd = false;
                }
            }

            if (isAdd) {
                sqlQuery = "INSERT INTO enviroCompany "
                        + " ("
                        + "   acGroupConsultantCode"
                        + " , acGroupDepartmentCode"
                        + " , acGroupOrganizationCode"
                        + " , acGroupReferredCode"
                        + " , acGroupEmployeeCode"
                        + " , acGroupPharmacySupplierCode"
                        + " , acGroupInventorySupplierCode"
                        + " , acGroupManufacturerCode"
                        + " , acGroupDivisionCode"
                        + " , acGroupCustomerCode"
                        + " , isInventoryAccountingActive "
                        + " ) "
                        + "VALUES "
                        + " ("
                        + "   :acgroupconsultantcode"
                        + " , :acgroupdepartmentcode"
                        + " , :acGroupOrganizationCode"
                        + " , :acGroupReferredCode"
                        + " , :acGroupEmployeeCode"
                        + " , :acGroupPharmacySupplierCode"
                        + " , :acGroupInventorySupplierCode"
                        + " , :acGroupManufacturerCode"
                        + " , :acGroupDivisionCode"
                        + " , :acGroupCustomerCode"
                        + " , :isInventoryAccountingActive "
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("acgroupconsultantcode", DmShared.foriegnKey(mrTemp.getAcGroupConsCode()));

                psTemp.setString("acgroupdepartmentcode", DmShared.foriegnKey(mrTemp.getAcGroupDepartCode()));
                psTemp.setString("acGroupOrganizationCode", DmShared.foriegnKey(mrTemp.getAcGroupOrgCode()));
                psTemp.setString("acGroupReferredCode", DmShared.foriegnKey(mrTemp.getAcGroupRefCode()));
                psTemp.setString("acGroupEmployeeCode", DmShared.foriegnKey(mrTemp.getAcGroupEmpCode()));
                psTemp.setString("acGroupPharmacySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupPharmacySupplierCode()));
                psTemp.setString("acGroupInventorySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupInventorySupplierCode()));
                psTemp.setString("acGroupManufacturerCode", DmShared.foriegnKey(mrTemp.getAcGroupManufacturerCode()));
                psTemp.setString("acGroupDivisionCode", DmShared.foriegnKey(mrTemp.getAcGroupDivisionCode()));
                psTemp.setString("acGroupCustomerCode", DmShared.foriegnKey(mrTemp.getAcGroupCustomerCode()));
                psTemp.setString("isInventoryAccountingActive", DmShared.iNull(mrTemp.getInventoryAccountingActive()).toString());
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = " update enviroCompany set "
                        + " acGroupConsultantCode = :acGroupConsultantCode, "
                        + " acgroupdepartmentcode = :acgroupdepartmentcode, "
                        + " acGroupReferredCode = :acGroupReferredCode, "
                        + " acGroupOrganizationCode = :acGroupOrganizationCode, "
                        + " acGroupPharmacySupplierCode = :acGroupPharmacySupplierCode, "
                        + " acGroupInventorySupplierCode = :acGroupInventorySupplierCode, "
                        + " acGroupEmployeeCode = :acGroupEmployeeCode, "
                        + " acGroupManufacturerCode = :acGroupManufacturerCode, "
                        + " acGroupDivisionCode = :acGroupDivisionCode, "
                        + " acGroupCustomerCode = :acGroupCustomerCode, "
                        + " isInventoryAccountingActive = :isInventoryAccountingActive ";;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("acgroupconsultantcode", DmShared.foriegnKey(mrTemp.getAcGroupConsCode()));
                psTemp.setString("acgroupdepartmentcode", DmShared.foriegnKey(mrTemp.getAcGroupDepartCode()));
                psTemp.setString("acGroupOrganizationCode", DmShared.foriegnKey(mrTemp.getAcGroupOrgCode()));
                psTemp.setString("acGroupReferredCode", DmShared.foriegnKey(mrTemp.getAcGroupRefCode()));
                psTemp.setString("acGroupEmployeeCode", DmShared.foriegnKey(mrTemp.getAcGroupEmpCode()));
                psTemp.setString("acGroupPharmacySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupPharmacySupplierCode()));
                psTemp.setString("acGroupInventorySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupInventorySupplierCode()));
                psTemp.setString("acGroupManufacturerCode", DmShared.foriegnKey(mrTemp.getAcGroupManufacturerCode()));
                psTemp.setString("acGroupDivisionCode", DmShared.foriegnKey(mrTemp.getAcGroupDivisionCode()));
                psTemp.setString("acGroupCustomerCode", DmShared.foriegnKey(mrTemp.getAcGroupCustomerCode()));
                psTemp.setString("isInventoryAccountingActive", DmShared.iNull(mrTemp.getInventoryAccountingActive()).toString());
                psTemp.executeUpdate();
                psTemp.close();

            }
//===================================================================================================
//========================== Employee will be reamain in the same group in which it is been saved. ==
//========================== To change group user have to change it from employee master itself. ====
//===================================================================================================
//            sqlQuery = "update SubGroup set "
//                    + "groupCode = :acGroupEmployeeCode "
//                    + "where  filterNature = " + DmServer.getCodeViaManualCode("EM", "manualCode", "subgroupNature", "Code", cnTemp) + "";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("acGroupEmployeeCode", DmShared.foriegnKey(mrTemp.getAcGroupEmpCode()));
//            psTemp.executeUpdate();
//            psTemp.close();
//===================================================================================================
//===================================================================================================
            sqlQuery = "update SubGroup set "
                    + "groupCode = :acgroupconsultantcode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("CN", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acgroupconsultantcode", DmShared.foriegnKey(mrTemp.getAcGroupConsCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update SubGroup set "
                    + "groupCode = :acGroupReferredCode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("RC", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupReferredCode", DmShared.foriegnKey(mrTemp.getAcGroupRefCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update SubGroup set "
                    + "groupCode = :acgroupdepartmentcode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("DP", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acgroupdepartmentcode", DmShared.foriegnKey(mrTemp.getAcGroupDepartCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update SubGroup set "
                    + "groupCode = :acGroupOrganizationCode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("OR", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupOrganizationCode", DmShared.foriegnKey(mrTemp.getAcGroupOrgCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update SubGroup set "
                    + "groupCode = :acGroupPharmacySupplierCode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("PS", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupPharmacySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupPharmacySupplierCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update SubGroup set "
                    + "groupCode = :acGroupInventorySupplierCode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("IS", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupInventorySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupInventorySupplierCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Update SubGroup set "
                    + " groupCode = :acGroupManufacturerCode "
                    + " where  filterNature = " + DmServer.getCodeViaManualCode("MF", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupManufacturerCode", DmShared.foriegnKey(mrTemp.getAcGroupManufacturerCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = " Update SubGroup set "
                    + " groupCode = :acGroupDivisionCode "
                    + " where  filterNature = " + DmServer.getCodeViaManualCode("DV", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupDivisionCode", DmShared.foriegnKey(mrTemp.getAcGroupDivisionCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From managePageLayout ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            if (mrTemp.getGrdPatientInfoList() != null) {
                for (GrdPatientInfo_Mdl grdDelivery : mrTemp.getGrdPatientInfoList()) {
                    if (!DmShared.xNull(grdDelivery.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        sqlQuery = "INSERT INTO managePageLayout (code, nature, sNo, isVisible, isMandatory) "
                                + "VALUES (:code, :nature, :sNo, :isVisible, :isMandatory);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(grdDelivery.getSharedKeyFieldCode()));
                        psTemp.setString("nature", DmShared.xNull(grdDelivery.getNature()));
                        psTemp.setString("sNo", DmShared.iNull(grdDelivery.getSrNo()).toString());
                        psTemp.setString("isVisible", DmShared.iNull(grdDelivery.getIsVisibleTag()).toString());
                        psTemp.setString("isMandatory", DmShared.iNull(grdDelivery.getIsMandatoryTag()).toString());
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            if (mrTemp.getGrdPriorityList() != null) {
                for (GrdPriority_Mdl grdPriority : mrTemp.getGrdPriorityList()) {
                    if (DmShared.iNull(grdPriority.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = " update hl7Mast set "
                                + " priorityIndex = :priorityIndex, "
                                + " colorName = :colorName "
                                + " where  code = :code ";

                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(grdPriority.getSharedKeyFieldCode()));
                        psTemp.setString("priorityIndex", DmShared.iNull(grdPriority.getSorting()).toString());
                        psTemp.setString("colorName", DmShared.xNull(grdPriority.getColor()).trim());
                        psTemp.executeUpdate();
                        psTemp.close();
                        if (mrTemp.getGrdPatientClassList() != null) {
                            for (GrdPatientClass_Mdl mdl : mrTemp.getGrdPatientClassList()) {
                                if (DmShared.iNull(mdl.getPatientClassCode()).longValue() > 0) {
                                    sqlQuery = "update patientTypeMast set "
                                            + " ratelistCode = :ratelistCode "
                                            + " where code = :code ";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setString("code", DmShared.xNull(mdl.getPatientClassCode()));
                                    psTemp.setString("ratelistCode", DmShared.foriegnKey(mdl.getRateListCode()));
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                        }
//                        sqlQuery = " update patientRegistration set "
//                                + " priorityIndex = :priorityIndex "
//                                + " where  priorityType = :priorityType ";
//                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                        psTemp.setString("priorityIndex", DmShared.iNull(grdPriority.getSorting()).toString());
//                        psTemp.setString("priorityType", DmShared.xNull(grdPriority.getSharedKeyFieldName()).trim());
//                        psTemp.executeUpdate();
//                        psTemp.close();
//
//                        sqlQuery = " update consultantDiary set "
//                                + " priorityIndex = :priorityIndex "
//                                + " where  priorityType = :priorityType ";
//                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                        psTemp.setString("priorityIndex", DmShared.iNull(grdPriority.getSorting()).toString());
//                        psTemp.setString("priorityType", DmShared.xNull(grdPriority.getSharedKeyFieldName()).trim());
//                        psTemp.executeUpdate();
//                        psTemp.close();
//                        
//                        sqlQuery = " update requisition set "
//                                + " priorityIndex = :priorityIndex "
//                                + " where  priorityType = :priorityType ";
//                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                        psTemp.setString("priorityIndex", DmShared.iNull(grdPriority.getSorting()).toString());
//                        psTemp.setString("priorityType", DmShared.xNull(grdPriority.getSharedKeyFieldName()).trim());
//                        psTemp.executeUpdate();
//                        psTemp.close();
//                        
//                        sqlQuery = " update roomDiary set "
//                                + " priorityIndex = :priorityIndex "
//                                + " where  priorityType = :priorityType ";
//                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                        psTemp.setString("priorityIndex", DmShared.iNull(grdPriority.getSorting()).toString());
//                        psTemp.setString("priorityType", DmShared.xNull(grdPriority.getSharedKeyFieldName()).trim());
//                        psTemp.executeUpdate();
//                        psTemp.close();
                    }
                }
            }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHACGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT groupCode AS code,groupName  AS Name FROM acGroup");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHYesNo(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<KeyFieldExtended> exportJSON(String session) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        String sessionId = "", fileName = "", directoryPath, fullFilePath = "", serverFileName = "";
        Connection cnTemp = null;
        EnvironmentSettingCompany_Mdl mrTemp = null;
        KeyFieldExtended field = null;
        File sFile;
        FileWriter writer;
        try {
            sessionId = session;
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }
            directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "enviroCompany";
            sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp = moveRec(cnTemp);

            fileName = "enviroCompany";
            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            serverFileName = fileName + ".json";
            fullFilePath = directoryPath + File.separatorChar + serverFileName;
            Gson gson = new Gson();
            String json = gson.toJson(mrTemp);
            writer = new FileWriter(fullFilePath);
            writer.write(json);
            writer.close();

            ZipUtils zipUtils = new ZipUtils(directoryPath, directoryPath + File.separatorChar + "enviroCompany.zip");
            zipUtils.zipIt();
            field = new KeyFieldExtended();
            field.setSharedKeyFieldCode(directoryPath);
            field.setSharedKeyFieldName("enviroCompany.zip");
            rstTemp.setStatus(true);
            rstTemp.setData(field);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateList(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT name, code FROM rateListMast where rateListType =1");
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

}
