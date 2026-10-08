package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.*;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrganizationService implements DmImportData_Interface {

    class Model {

        private String code, name, manualCode, groupName, organization, printSNo;

        public Model() {
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getManualCode() {
            return manualCode;
        }

        public void setManualCode(String manualCode) {
            this.manualCode = manualCode;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getOrganization() {
            return organization;
        }

        public void setOrganization(String organization) {
            this.organization = organization;
        }

        public String getPrintSNo() {
            return printSNo;
        }

        public void setPrintSNo(String printSNo) {
            this.printSNo = printSNo;
        }
    }
    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public OrganizationService() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("organization");
        node.setSample("Blood");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("serviceName");
        node.setSample("CBC");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manualCode");
        node.setSample("CBC");
        node.setInstruction("{mandatory;alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("groupName");
        node.setSample("CT SCAN");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("printSNo");
        node.setSample("1");
        node.setInstruction("{numeric;3c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Organization Service Master");
        fileDetail.setServerClassName(OrganizationService.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        String[] csvFileFormat;
        Object objValue;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;
        try {
            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);

            if (DmShared.xNull(importData.getDataFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Data file path can not be blank.");
            } else {
                csvFileFormat = ImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = ImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
                    if (status == DmImportStatus.ERROR.getId()) {
                        throw new DmException(0, "");
                    }
                } else {
                    status = DmImportStatus.ERROR.getId();
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
                        if (headerName.trim().equalsIgnoreCase("organization")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setOrganization(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("serviceName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manualCode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setManualCode(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("groupName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("printSno")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPrintSNo(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveRecord(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
            }
        } catch (Exception e) {

            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(e.getMessage());
            } catch (Exception ex) {

            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
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
        return importStatus;
    }

    private int saveRecord(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, organizationCode;
        int rtnStatus = 0, status = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setOrganization(DmShared.removeSpace(mrTemp.getOrganization()));
            mrTemp.setGroupName(DmShared.removeSpace(mrTemp.getGroupName()));
            mrTemp.setManualCode(DmShared.removeSpace(mrTemp.getManualCode()));

            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "organizationServiceMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }
            organizationCode = DmServer.getCodeViaManualCode(mrTemp.getOrganization(), "name", "subGroup", "subCode", cnTemp);
            if (DmShared.iNull(organizationCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getOrganization() + " organization not found.");
            }

            /*====================================< Check whether record already exist or not >====================================*/
            sqlQuery = "select  osm.code, org.name as organization "
                    + "         from organizationServiceMast osm "
                    + "         left join subGroup org on org.subCode = osm.organizationCode "
                    + "         where osm.organizationCode = :organizationCode and osm.organizationServiceName = :organizationServiceName "
                    + "             and osm.code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.setString("organizationCode", DmShared.iNull(mrTemp.getOrganization()).toString());
            psTemp.setString("organizationServiceName", DmShared.xNull(mrTemp.getName()).trim());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(300, mrTemp.getName() + " service name already exists with " + DmShared.xNull(rsTemp.getString("organization")) + ".");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Insert Into organizationServiceMast(code, organizationServiceName, organizationManualCode, organizationServiceGroupName, "
                    + " organizationCode, printSNo, u_EntDt, preparedBy) "
                    + " values(:code, :organizationServiceName, :organizationManualCode, :organizationServiceGroupName, :organizationCode, "
                    + " :printSNo, :u_EntDt, :preparedBy)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("organizationCode", DmShared.foriegnKey(organizationCode));
            psTemp.setString("organizationServiceName", DmShared.xNull(mrTemp.getName()).trim());
            psTemp.setString("organizationManualCode", DmShared.xNull(mrTemp.getManualCode()).trim());
            psTemp.setString("organizationServiceGroupName", DmShared.xNull(mrTemp.getGroupName()).trim());
            psTemp.setString("printSNo", DmShared.xNull(mrTemp.getPrintSNo()));
            psTemp.setString("preparedBy", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                handelFileLog.writeLine(lineNoDisplay + e.getMessage());
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
