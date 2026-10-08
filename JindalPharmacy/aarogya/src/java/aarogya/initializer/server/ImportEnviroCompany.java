package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import aarogya.master.environmentSettingCompany.shared.*;
import com.google.gson.Gson;
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

public class ImportEnviroCompany implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            Gson gson = new Gson();
            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    status = save(gson.fromJson(br, EnvironmentSettingCompany_Mdl.class));
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

    private int save(EnvironmentSettingCompany_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        boolean isAdd = false;
        int rtnStatus = 0, status = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "SELECT count(*) as cnt FROM enviroCompany";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                isAdd = DmShared.iNull(rsTemp.getString("cnt")).doubleValue() == 0;
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setAcGroupConsCode("");
            mrTemp.setAcGroupConsCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupConsName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupConsCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupConsName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupDepartCode("");
            mrTemp.setAcGroupDepartCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupDepartName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupDepartCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupDepartName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupOrgCode("");
            mrTemp.setAcGroupOrgCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupOrgName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupOrgCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupOrgName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupRefCode("");
            mrTemp.setAcGroupRefCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupRefName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupRefCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupRefName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupEmpCode("");
            mrTemp.setAcGroupEmpCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupEmpName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupEmpCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupEmpName()) + " A/c group does not exist");
            }

            mrTemp.setAcGroupPharmacySupplierCode("");
            mrTemp.setAcGroupPharmacySupplierCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupPharmacySupplierName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupPharmacySupplierCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupPharmacySupplierName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupInventorySupplierCode("");
            mrTemp.setAcGroupInventorySupplierCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupInventorySupplierName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupInventorySupplierCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupInventorySupplierName()) + " A/c group does not exist");
            }

            mrTemp.setAcGroupManufacturerCode("");
            mrTemp.setAcGroupManufacturerCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupManufacturerName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupManufacturerCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupManufacturerName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupDivisionCode("");
            mrTemp.setAcGroupDivisionCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupDivisionName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupDivisionCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupDivisionName()) + " A/c group does not exist");
            }
            mrTemp.setAcGroupCustomerCode("");
            mrTemp.setAcGroupCustomerCode(DmServer.getCodeViaManualCode(mrTemp.getAcGroupCustomerName(), "groupName", "acGroup", "groupCode", cnTemp));
            if (DmShared.iNull(mrTemp.getAcGroupCustomerCode()).longValue() == 0) {
                status = DmImportStatus.WARNING.getId();
                refreshDataStructure.writeLine(DmShared.xNull(mrTemp.getAcGroupCustomerName()) + " A/c group does not exist");
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
                        + " acGroupCustomerCode = :acGroupCustomerCode ";
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
                psTemp.executeUpdate();
                psTemp.close();

            }
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
                    + "groupCode = :acGroupInventorySupplierCode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("IS", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupInventorySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupInventorySupplierCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update SubGroup set "
                    + "groupCode = :acGroupPharmacySupplierCode "
                    + "where  filterNature = " + DmServer.getCodeViaManualCode("PS", "manualCode", "subgroupNature", "Code", cnTemp) + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("acGroupPharmacySupplierCode", DmShared.foriegnKey(mrTemp.getAcGroupInventorySupplierCode()));
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
                    }
                }
            }

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
