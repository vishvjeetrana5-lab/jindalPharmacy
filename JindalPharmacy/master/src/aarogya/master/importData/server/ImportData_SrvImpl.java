package aarogya.master.importData.server;

import aarogya.selfToolkit.stImportData.server.*;
import aarogya.master.importData.shared.*;
import aarogya.projectMenu.self.shared.pmAssets;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmToolkit.dmImportData.server.*;
import dataman.dmToolkit.dmImportData.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import java.io.*;
import java.sql.*;
import java.util.*;

public class ImportData_SrvImpl extends DmRSSPrivate implements ImportData_Srv {

    private DmHandelFile handelFileLog;

    @Override
    public DmResult<ImportData_Mdl> importData(String startDate, ImportData_Mdl mrTemp) {
        DmResult<ImportData_Mdl> dmrTemp = new DmResult<>();
        Class<?> clsType;
        DmImportData_Interface dInterface;

        try {
            if (mrTemp != null) {
                if (!DmShared.xNull(mrTemp.getServerClassName()).trim().equalsIgnoreCase("")
                        && !DmShared.xNull(mrTemp.getDataFilePath()).equalsIgnoreCase("")) {
                    mrTemp.setStartDate(DmShared.xNull(startDate));
                    clsType = Class.forName(mrTemp.getServerClassName());
                    dInterface = (DmImportData_Interface) clsType.newInstance();

//                        if (dInterface.importCSV(this, importDataMdl)) {
                    mrTemp.setImportStatus(dInterface.importCSV(this, mrTemp));
//                    mrTemp.setStatus(importStatus.getText());
//                        } else {
//                            mrTemp.get(i).setStatus("Exception");
//                        }
                }
            }
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> getCSVFile(ImportData_Mdl mrTemp, String fileName) {
        DmResult<String> dmrTemp = new DmResult<String>();
        HashMap<String, String> hmCSV;
        String downloadFileName = "", downloadDirectoryPath, fileFullPath, sessionId;
        Class<?> clsType;
        try {

            clsType = Class.forName(mrTemp.getServerClassName());
            fileName = DmShared.xNull(fileName).trim().replace(" ", "");
            if (fileName.equals("")) {
                downloadFileName = clsType.getSimpleName() + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".csv";
            } else {
                downloadFileName = fileName + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".csv";
            }
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            downloadDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "importData";
            downloadDirectoryPath = downloadDirectoryPath + File.separatorChar + "downloads";
            new File(downloadDirectoryPath).mkdirs();
            fileFullPath = downloadDirectoryPath + File.separatorChar + downloadFileName;
            hmCSV = ImportData_Server.getCSVFormats(mrTemp.getHmCSV());
            handelFileLog = new DmHandelFile(fileFullPath, true);
            handelFileLog.writeLine(hmCSV.get("header"));
            handelFileLog.writeLine(hmCSV.get("sample"));
            handelFileLog.writeLine("");
            handelFileLog.writeLine("");
            handelFileLog.writeLine("");
            handelFileLog.writeLine(hmCSV.get("instruction"));

            dmrTemp.setData(downloadFileName);
            dmrTemp.setValue1(downloadDirectoryPath);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> readLogFile(ImportData_Mdl importData_Mdl) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String log = "";

        try {

            String fullFilePath = importData_Mdl.getLogFilePath();

            File fr = new File(fullFilePath);
            if (fr.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(fullFilePath));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                log = sb.toString();
                br.close();

            }
            KeyFieldExtended field = new KeyFieldExtended();
            field.setSharedKeyFieldName(log);
            dmrTemp.setData(field);
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
                if (handelFileLog != null) {
                    handelFileLog.close();
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
    public DmResultPageing<List<ImportData_Mdl>> getImportTo(SearchValue_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<ImportData_Mdl>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList();
        HashMap<String, Object> hmImportMdl = new HashMap<String, Object>();
        KeyFieldExtended keyField;
        List<ImportData_Mdl> lstTempImport = new ArrayList();
        ImportData_Mdl tempMdl;
        rstTemp.setData(null);
        try {
            for (ImportData_Mdl importDataMdl : param.getLstImport()) {
                if (importDataMdl != null) {
                    keyField = new KeyFieldExtended();
                    keyField.setSharedKeyFieldCode(importDataMdl.getImportTo());
                    keyField.setSharedKeyFieldName(importDataMdl.getImportTo());
                    keyField.setSharedModifiedBy(importDataMdl.getServerClassName());
                    keyField.setSharedPreparedBy(ImportData_Server.getCSVFormats(importDataMdl.getHmCSV()).get("header"));
                    hmImportMdl.put("logFile" + importDataMdl.getServerClassName(), importDataMdl.getLogFilePath());
                    hmImportMdl.put("list" + importDataMdl.getServerClassName(), importDataMdl.getHmCSV());
                    hmImportMdl.put("isCheck" + importDataMdl.getServerClassName(), importDataMdl.getIsChecked());
                    lstTemp.add(keyField);
                }
            }

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);

            for (ImportData_Mdl dataMdl : param.getLstImport()) {
                if (dataMdl != null) {
                    for (KeyFieldExtended tmpMdl : lstTemp) {
                        if (tmpMdl != null) {
                            if (DmShared.xNull(tmpMdl.getSharedKeyFieldCode()).equalsIgnoreCase(dataMdl.getImportTo())) {
                                if (param.isSearching()) {
                                    tempMdl = new ImportData_Mdl();
                                    tempMdl.setImportTo(dataMdl.getImportTo());
                                    tempMdl.setTableName(dataMdl.getTableName());
                                    tempMdl.setServerClassName(dataMdl.getServerClassName());
                                    tempMdl.setFileFormat(ImportData_Server.getCSVFormats(dataMdl.getHmCSV()).get("header"));
                                    tempMdl.setIsChecked(((String) hmImportMdl.get("isCheck" + dataMdl.getServerClassName())));
                                    tempMdl.setLogFileName(((String) hmImportMdl.get("logFile" + dataMdl.getServerClassName())));
                                    tempMdl.getHmCSV().addAll(((List<CSVData>) hmImportMdl.get("list" + dataMdl.getServerClassName())));
                                    lstTempImport.add(tempMdl);
                                    break;
                                } else if (DmShared.iNull(dataMdl.getIsChecked()).intValue() == 0) {
                                    tempMdl = new ImportData_Mdl();
                                    tempMdl.setImportTo(dataMdl.getImportTo());
                                    tempMdl.setTableName(dataMdl.getTableName());
                                    tempMdl.setServerClassName(dataMdl.getServerClassName());
                                    tempMdl.setFileFormat(ImportData_Server.getCSVFormats(dataMdl.getHmCSV()).get("header"));
                                    tempMdl.setIsChecked(((String) hmImportMdl.get("isCheck" + dataMdl.getServerClassName())));
                                    tempMdl.setLogFileName(((String) hmImportMdl.get("logFile" + dataMdl.getServerClassName())));
                                    tempMdl.getHmCSV().addAll(((List<CSVData>) hmImportMdl.get("list" + dataMdl.getServerClassName())));
                                    lstTempImport.add(tempMdl);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            rstTemp.setData(lstTempImport);
            helper.positioning(rstTemp.getData().size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<List<ImportData_Mdl>> getImportData(String yearStart) {
        DmResultPageing<List<ImportData_Mdl>> rstTemp = new DmResultPageing<>();
        String logDirectoryPath, sessionId;
        rstTemp.setData(new ArrayList<ImportData_Mdl>());

        try {
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            logDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "importData";
            logDirectoryPath = logDirectoryPath + File.separatorChar + "logs";
            new File(logDirectoryPath).mkdirs();
            rstTemp.getData().add(new PathologyService().getImportDataModel());
            rstTemp.getData().add(new HSNMaster().getImportDataModel());
            rstTemp.getData().add(new UnitMaster().getImportDataModel());
            rstTemp.getData().add(new AssetGroupMaster().getImportDataModel());
            rstTemp.getData().add(new AssetItemMaster().getImportDataModel());
            rstTemp.getData().add(new ImportCity().getImportDataModel());
            rstTemp.getData().add(new ZoneMaster().getImportDataModel());
            rstTemp.getData().add(new AllergyMaster().getImportDataModel());
            rstTemp.getData().add(new FrequencyMaster().getImportDataModel());
            rstTemp.getData().add(new DischargeCheckListMaster().getImportDataModel());
            rstTemp.getData().add(new HouseKeepingChecklist().getImportDataModel());
            rstTemp.getData().add(new NursingStationMaster().getImportDataModel());
            rstTemp.getData().add(new IcdxMaster().getImportDataModel());
            rstTemp.getData().add(new DepartmentMaster().getImportDataModel());
            rstTemp.getData().add(new CaseMaster().getImportDataModel());
            rstTemp.getData().add(new ConsultantSpeciMaster().getImportDataModel());
            rstTemp.getData().add(new RoomCategoryMaster().getImportDataModel());
            rstTemp.getData().add(new RoomMaster().getImportDataModel());
            rstTemp.getData().add(new ConsultantMaster().getImportDataModel());
            rstTemp.getData().add(new ConsultantTimeDetail().getImportDataModel());
            rstTemp.getData().add(new RefferedMaster().getImportDataModel());
            rstTemp.getData().add(new ClinicalDetail().getImportDataModel());
            rstTemp.getData().add(new EmployeeMaster().getImportDataModel());
            rstTemp.getData().add(new ImportAccountGroup().getImportDataModel());
            rstTemp.getData().add(new ImportAccountMaster().getImportDataModel());
            rstTemp.getData().add(new ItemCategory().getImportDataModel());
            rstTemp.getData().add(new ItemGroup().getImportDataModel());
            rstTemp.getData().add(new ItemMaster().getImportDataModel());
            rstTemp.getData().add(new ItemDepartmentInfo().getImportDataModel());
            rstTemp.getData().add(new MedicineCategoryMaster().getImportDataModel());
            rstTemp.getData().add(new MedicineGroup().getImportDataModel());
            rstTemp.getData().add(new MedicineComposition().getImportDataModel());
            rstTemp.getData().add(new MedicineMakeMaster().getImportDataModel());
            rstTemp.getData().add(new MedicineMaster().getImportDataModel());
            rstTemp.getData().add(new MedicineDepartmentInfo().getImportDataModel());
            rstTemp.getData().add(new ServiceCategoryMaster().getImportDataModel());
            rstTemp.getData().add(new DiagnosticCategoryMaster().getImportDataModel());
            rstTemp.getData().add(new ServiceGroupMaster().getImportDataModel());
            rstTemp.getData().add(new ServiceMaster().getImportDataModel());
            rstTemp.getData().add(new TestSensitivityMaster().getImportDataModel());
            rstTemp.getData().add(new OperationMaster().getImportDataModel());
            rstTemp.getData().add(new OperationMedicineMaster().getImportDataModel());
            rstTemp.getData().add(new UHID().getImportDataModel());
            rstTemp.getData().add(new SampleMaster().getImportDataModel());
            rstTemp.getData().add(new DiagnosticGroupMaster().getImportDataModel());
            rstTemp.getData().add(new GSTReasonMast().getImportDataModel());
            rstTemp.getData().add(new GSTUnit().getImportDataModel());
            rstTemp.getData().add(new MedicineOpening().getImportDataModel());
            rstTemp.getData().add(new ManufacturerMaster().getImportDataModel());
            rstTemp.getData().add(new DivisionMaster().getImportDataModel());
            rstTemp.getData().add(new SupplierMaster().getImportDataModel());
            rstTemp.getData().add(new ImportPrescriptionCustomGroupMast().getImportDataModel());
            rstTemp.getData().add(new ImportPrescriptionCustomItem().getImportDataModel());
            rstTemp.getData().add(new ImportPatientServicesApplicableTo().getImportDataModel());
            rstTemp.getData().add(new RadiologyService().getImportDataModel());
            rstTemp.getData().add(new ItemOpening().getImportDataModel());
            rstTemp.getData().add(new CustomerMaster().getImportDataModel());
            rstTemp.getData().add(new ImportSingleLineMaster(new pmPharmacy().RESTRICTED_GROUP_MASTER.caption).getImportDataModel());
            rstTemp.getData().get(rstTemp.getData().size() - 1).setTableName("restrictedGrpMast");
            rstTemp.getData().add(new ImportSingleLineMaster(new pmAssets().ASSETS_CATEGORY_MASTER.caption).getImportDataModel());
            rstTemp.getData().get(rstTemp.getData().size() - 1).setTableName("assetCatMast");
            rstTemp.getData().add(new ImportSingleLineMaster(new pmInventory().ITEM_SPECIALITY_MASTER.caption).getImportDataModel());
            rstTemp.getData().get(rstTemp.getData().size() - 1).setTableName("itemSpecialityMast");
            rstTemp.getData().add(new ReceiptBookMapping().getImportDataModel());
            setFileFormat(rstTemp.getData(), DmShared.dNull(yearStart), logDirectoryPath);
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    private void setFileFormat(List<ImportData_Mdl> importDataLst, String yearStart, String logDirectoryPath) throws Exception {
        Class<?> clsType;

        for (ImportData_Mdl importData : importDataLst) {
            if (importData != null) {
                if (importData.getHmCSV() != null) {
                    clsType = Class.forName(importData.getServerClassName());
                    importData.setLogFileName(logDirectoryPath + File.separatorChar + clsType.getSimpleName() + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".log");
                    importData.setFileFormat(ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header"));
                    importData.setStartDate(yearStart);
                }
            }
        }
    }
}
