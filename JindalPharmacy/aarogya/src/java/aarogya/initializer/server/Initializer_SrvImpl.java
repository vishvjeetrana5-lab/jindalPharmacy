package aarogya.initializer.server;

import dataman.dmToolkit.dmImportData.server.ImportNumberSystemIni;
import aarogya.selfToolkit.stImportData.server.*;
import aarogya.initializer.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmToolkit.dmImportData.server.*;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Initializer_SrvImpl extends DmRSSPrivate implements Initializer_Srv {

    @Override
    public DmResult<List<GrdInitializer_Mdl>> getInitializerClass(String yearStart) {
        DmResult<List<GrdInitializer_Mdl>> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            dmrTemp.setData(new ArrayList<GrdInitializer_Mdl>());
            dmrTemp.getData().add(new GrdInitializer_Mdl("sqlProcedures", "SQL Procedures", SQLProcedureIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("tabIndex", "Tab Index", ImportTabIndex.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("menuCodification", "Menu Codification", ImportMenuCodification.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("cityMaster", "City Master", ImportCity.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("initialMaster", "Initial Master", ImportInitialMaster.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("hl7Master", "HL7 Master", ImportHL7Master.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("acGroup", "A/c Group", ImportAccountGroup.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("fileType", "File Type", FileTypeMasterIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("smsTemplates", "SMS Templates", SmsTemplate.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("eMailTemplates", "E-Mail Templates", EmailTemplate.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("guiConfig", "GUI Config", GUIConfigIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("acMaster", "A/c Master", ImportAccountMaster.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("cardTypeMaster", "Card Type Master", ImportCardType.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("voucherType", "Voucher Type", ManageVoucherTypeIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("numberSystem", "Number System", ImportNumberSystemIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("overHeadMaster", "Over Head Master", ImportOverHeadMaster.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("overHeadConfig", "Over Head Config", OverheadConfigurationIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("overHeadLineConfig", "Over Head Line Config", OverheadLineConfigurationIni.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("itemTaxStructure", "Item Tax Structure", InitializerITS.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("enviroCompany", "Environment Company", ImportEnviroCompany.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("environmentSite", "Environment Site", ImportEnviroSite.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("organizationMaster", "Organization Master", ImportOrganization.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("gstReasonMaster", "GST Reason", GSTReasonMast.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("HSNMaster", "HSNMaster", InitializerHSN.class.getName()));
//            dmrTemp.getData().add(new GrdInitializer_Mdl("medicineGroup", "Medicine Group", MedicineGroup.class.getName()));
//            dmrTemp.getData().add(new GrdInitializer_Mdl("medicineCategoryMaster", "Medicine Category Master", MedicineCategoryMaster.class.getName()));
//            dmrTemp.getData().add(new GrdInitializer_Mdl("restrictedGroupMaster", "Restricted Group Master", ImportSingleLineMaster.class.getName()));
//            dmrTemp.getData().get(dmrTemp.getData().size() - 1).setTableName("restrictedGrpMast");
//            dmrTemp.getData().add(new GrdInitializer_Mdl("medicineMakeMaster", "Medicine Make Master", MedicineMakeMaster.class.getName()));
            dmrTemp.getData().add(new GrdInitializer_Mdl("unitMaster", "Unit Master", UnitMaster.class.getName()));
//            dmrTemp.getData().add(new GrdInitializer_Mdl("medicineComposition", "Medicine Composition", MedicineComposition.class.getName()));
//            dmrTemp.getData().add(new GrdInitializer_Mdl("medicineMaster", "Medicine Master", ImportMedicine.class.getName()));

            setPath(dmrTemp.getData(), DmShared.dNull(yearStart));
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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

    private void setPath(List<GrdInitializer_Mdl> importDataLst, String yearStart) throws Exception {
        for (GrdInitializer_Mdl importData : importDataLst) {
            if (importData != null) {
                importData.setYearStart(yearStart);
                importData.setLogFilePath(DmServer.Path.logs + File.separatorChar + "initializer" + File.separatorChar + importData.getSharedKeyFieldCode() + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".log");
                new File(DmServer.Path.logs + File.separatorChar + "initializer").mkdir();

                importData.setDataPath(DmServer.Path.resources + File.separatorChar + "repairDB" + File.separatorChar + "initializer" + File.separatorChar + importData.getSharedKeyFieldCode());
                new File(importData.getDataPath()).mkdir();
            }
        }
    }

    @Override
    public DmResult<GrdInitializer_Mdl> execute(GrdInitializer_Mdl grdData) {
        DmResult<GrdInitializer_Mdl> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Class clsType;
        Class[] arrInterface;
        ImportData_Mdl importMdl = null;
        Initializer_Interface iInterface = null;
        DmImportData_Interface dInterface = null;

        File initializerPath = null, tmpFilePath = null;
        try {
            if (!DmShared.xNull(grdData.getServerClassName()).equalsIgnoreCase("")) {
                clsType = Class.forName(grdData.getServerClassName());
                arrInterface = clsType.getInterfaces();
                if (arrInterface.length > 0) {
                    for (Class clsInterface : arrInterface) {
                        if (clsInterface != null) {
                            iInterface = null;
                            dInterface = null;
                            if (clsInterface.getName().equals(DmImportData_Interface.class.getName())) {
                                dInterface = (DmImportData_Interface) clsType.newInstance();
                                if (dInterface != null) {
                                    importMdl = dInterface.getImportDataModel();
                                    initializerPath = new File(grdData.getDataPath());
                                    if (initializerPath.exists()) {
                                        if (initializerPath.list().length > 0) {
                                            importMdl.setLogFileName(grdData.getLogFilePath());
                                            importMdl.setDataFilePath(grdData.getDataPath() + File.separatorChar + initializerPath.list()[0]);
                                            if (!DmShared.xNull(grdData.getTableName()).trim().equals("")) {
                                                importMdl.setTableName(grdData.getTableName());
                                            }
//                                            status = dInterface.importCSV(this, importMdl).getId();
                                            grdData.setImportStatus(dInterface.importCSV(this, importMdl));
                                            grdData.setLogFilePath(importMdl.getLogFilePath());
                                        }
                                    }
                                }
                            } else if (clsInterface.getName().equals(Initializer_Interface.class.getName())) {
                                iInterface = (Initializer_Interface) clsType.newInstance();
                                if (iInterface != null) {
                                    grdData.setImportStatus(iInterface.start(this, grdData));
                                }
                            }

                        }
                    }
                }
            }
            dmrTemp.setData(grdData);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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
    public DmResult<String> getDefaultUHID() {
        DmResult<String> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        boolean flag = false;
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        String sqlQuery, vType = "", docId = "", genderCode = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = " select count(*) as cnt from patientUHID where recId = 'LabVerificationUHID'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") == 0) {
                flag = true;
            }
            rsTemp.close();
            psTemp.close();

            if (flag) {
                String[] currentDate = DmServer.getServerDateTime();
                vType = DmServer.getVType(pvc.UHID, cnTemp);

                docId = DmServer.getDocId(vType, DmServer.getSsnVoucherPrefix(this), "1", cnTemp);
                //========================================== GET GENDER CODE ==============================================================
                sqlQuery = "SELECT CODE FROM hl7Mast where tableName='administrativeSex' and isActive=1 and name='Male' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    genderCode = DmShared.xNull(rsTemp.getString("CODE"));
                }
                rsTemp.close();
                psTemp.close();
                if (DmShared.iNull(genderCode).intValue() == 0) {
                    throw new UnsupportedOperationException(" Male gender not found");
                }
                //========================================== END ==========================================================================

                //========================================== CHECK IF DOCID IS NULL =======================================================
                if (docId.equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Invalid key.");
                }
                //========================================== END ==========================================================================

                /*====================================< Insert record into patientUHID >====================================*/
                sqlQuery = "INSERT INTO patientUHID (docId, v_Type, v_No, recId, recIdPrefix, v_Date, v_Prefix, patientInitial, patientName, "
                        + "  u_EntDt, site_Code,  preparedBy, transferDt,patientFirstName,dob,dobDisplay,dobTemp,mobile) "
                        + " values (:docId, :v_Type, :v_No, :recId, :recIdPrefix, :v_Date, :v_Prefix, :patientInitial, :patientName,  "
                        + " :u_EntDt, :siteCode,:preparedBy,  :transferDt, "
                        + " :patientFirstName,:dob,:dobDisplay,:dobTemp,:mobile) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("v_Type", vType);
                psTemp.setString("v_No", "");
                psTemp.setString("recId", "LabVerificationUHID");
                psTemp.setString("recIdPrefix", "");
                psTemp.setDate("v_Date", DmServer.convertDate(currentDate[0]));
                psTemp.setString("v_Prefix", DmServer.getSsnVoucherPrefix(this));
                psTemp.setString("patientInitial", "");
                psTemp.setString("patientName", "Lab Verification");
                psTemp.setString("patientFirstName", "Lab Verification");
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("transferDt", null);
                psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("mobile", "1234567893");
                psTemp.setDate("dob", DmServer.convertDate(currentDate[0]));
                psTemp.setString("dobDisplay", DmShared.xNull("1"));
                psTemp.setDate("dobTemp", DmServer.convertDate(currentDate[0]));
                psTemp.executeUpdate();
                psTemp.close();

                //====================================< INSERT INTO PATIENTUHID2 >=========================================================
                sqlQuery = "INSERT INTO patientuhid2 (uhid,genderCode)"
                        + " VALUES (:uhid,:genderCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("uhid", DmShared.iNull(docId).toString());
                psTemp.setString("genderCode", DmShared.foriegnKey(genderCode));
                psTemp.executeUpdate();
                psTemp.close();
                //========================================== END ==========================================================================
            }
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setData(docId);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(e.getMessage());
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
