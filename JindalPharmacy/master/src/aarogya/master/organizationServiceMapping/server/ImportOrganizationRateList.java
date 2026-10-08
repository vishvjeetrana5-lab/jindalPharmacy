package aarogya.master.organizationServiceMapping.server;

import aarogya.master.organizationServiceMapping.shared.GrdService_Mdl;
import aarogya.selfToolkit.stImportData.server.ImportData_Server;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ImportOrganizationRateList {

    static class Model {

        private String code, organizationService, orgSrvManualCode, orgSrvGroup, mapService, rate;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOrganizationService() {
            return organizationService;
        }

        public void setOrganizationService(String organizationService) {
            this.organizationService = organizationService;
        }

        public String getOrgSrvManualCode() {
            return orgSrvManualCode;
        }

        public void setOrgSrvManualCode(String orgSrvManualCode) {
            this.orgSrvManualCode = orgSrvManualCode;
        }

        public String getMapService() {
            return mapService;
        }

        public void setMapService(String mapService) {
            this.mapService = mapService;
        }

        public String getOrgSrvGroup() {
            return orgSrvGroup;
        }

        public void setOrgSrvGroup(String orgSrvGroup) {
            this.orgSrvGroup = orgSrvGroup;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }
    }

    private static DmHandelFile handelFileLog;
    private static String lineNoDisplay = "";

    public ImportOrganizationRateList() {
    }

    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("organizationService");
        node.setSample("CBC");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("organizationServiceCode");
        node.setSample("C101");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("organizationServiceGroup");
        node.setSample("Pathlogy");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("rate");
        node.setSample("100.00");
        node.setInstruction("{mandatory;numeric;}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("serviceMapped");
        node.setSample("CBC");
        node.setInstruction("{mandatory;alphanumeric;60c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Organization Rate List");
        fileDetail.setServerClassName(ImportOrganizationRateList.class.getName());

        return fileDetail;
    }

    public static List<GrdService_Mdl> importData(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        List<GrdService_Mdl> lstTemp = new ArrayList<>();
        GrdService_Mdl node = null;
        String fieldValue, prevOrgService = "";
        Object objValue;
        String[] csvFileFormat;
        try {

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                throw new Exception("Data file path can not be blank.");
            } else {
                csvFileFormat = ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    DmImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                } else {
                    throw new Exception("Data file not found.");
                }

                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(i + 1) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTemp = new Model();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("organizationService")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                isFound = false;
                            } else {
                                mrTemp.setOrganizationService(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("organizationServiceCode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                isFound = false;
                            } else {
                                mrTemp.setOrgSrvManualCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("organizationServiceGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                isFound = false;
                            } else {
                                mrTemp.setOrgSrvGroup(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("rate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                isFound = false;
                            } else {
                                mrTemp.setRate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("serviceMapped")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 60, lineNoDisplay, handelFileLog)) {
                                isFound = false;
                            } else {
                                mrTemp.setMapService(DmShared.removeSpace(DmShared.xNull(fieldValue)));
                            }
                        }
                    }
                    if (isFound) {
                        node = null;
                        if (DmShared.xNull(prevOrgService).equalsIgnoreCase(mrTemp.getOrganizationService())) {
                            node = lstTemp.get(lstTemp.size() - 1);
                            saveRecord(rss, node, mrTemp);
                        } else {
                            node = saveRecord(rss, new GrdService_Mdl(), mrTemp);
                            if (!DmShared.xNull(node.getOrgServiceName()).equalsIgnoreCase("")) {
                                lstTemp.add(node);
                            }
                        }

                    }
                    prevOrgService = mrTemp.getOrganizationService();
                }
            }
        } catch (Exception e) {
            try {
                throw new DmException(0, e.getMessage());
            } catch (Exception ex) {

            }
        } finally {
            try {
                if (csvReader != null) {
                    csvReader = null;
                }
                if (dataFile != null) {
                    dataFile = null;
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception e) {
            }
        }
        return lstTemp;
    }

    private static GrdService_Mdl saveRecord(DmRSSPrivate rss, GrdService_Mdl node, Model mrTemp) {
        String serviceCode;
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            serviceCode = DmServer.getCodeViaManualCode(mrTemp.getMapService(), "name", "patientServiceMast", "code", cnTemp);
            mrTemp.setCode(DmServer.getCodeViaManualCode(mrTemp.getOrganizationService(), "organizationServiceName", "organizationServiceMast", "code", cnTemp));
            cnTemp.close();

            if (DmShared.iNull(serviceCode).longValue() > 0) {
                if (DmShared.xNull(node.getMappedServiceCode()).equalsIgnoreCase("")) {
                    node.setMappedServiceCode(DmShared.xNull(serviceCode));
                    node.setMappedServiceName(DmShared.xNull(mrTemp.getMapService()));
                } else {
                    node.setMappedServiceCode(DmShared.xNull(node.getMappedServiceCode()) + "," + DmShared.xNull(serviceCode));
                    node.setMappedServiceName(DmShared.xNull(node.getMappedServiceName()) + "," + DmShared.xNull(mrTemp.getMapService()));
                }
            }
            node.setSharedKeyFieldCode(mrTemp.getCode());
            node.setOrgGroupName(mrTemp.getOrgSrvGroup());
            node.setOrgServiceName(mrTemp.getOrganizationService());
            node.setOrgServiceCode(mrTemp.getOrgSrvManualCode());
            node.setRate(mrTemp.getRate());
        } catch (Exception e) {
            try {
                throw new DmException(0, e.getMessage());
            } catch (Exception ex) {
            }
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return node;
    }
}
