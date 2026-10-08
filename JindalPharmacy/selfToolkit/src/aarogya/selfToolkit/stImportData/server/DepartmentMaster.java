package aarogya.selfToolkit.stImportData.server;

import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentMaster implements DmImportData_Interface {

    class Model {

        private String subCode, name, nature, shortName;

        public Model() {
        }

        public String getSubCode() {
            return subCode;
        }

        public void setSubCode(String subCode) {
            this.subCode = subCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "", acDepartmentCode = "", filterNature = "";

    public DepartmentMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("departmentName");
        node.setSample("PATHOLOGY");
        node.setInstruction("{unique,mandatory,alphanumeric,100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("HOSPITAL");
        node.setInstruction("{Common/Hospital/Pharmacy/Purchase}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("shortName");
        node.setSample("PH");
        node.setInstruction("{unique,mandatory,alphanumeric,25c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Department Master");
        fileDetail.setServerClassName(DepartmentMaster.class.getName());

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
        Object objValue;
        Connection cnTemp = null;
        String[] csvFileFormat;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;
        try {
            this.rss = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));

            acDepartmentCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupDepartmentCode", cnTemp);

            if (DmShared.iNull(acDepartmentCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Define Department A/c Group in enviro.");
            }
            //======================================END======================================

            //==================== FILTER NATURE ============================
            filterNature = DmServer.getCodeViaManualCode("DP", "manualCode", "subgroupNature", "Code", cnTemp);
            if (DmShared.iNull(filterNature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, "Department 'DP' nature not exists.");
            }
            //==========================================================
            cnTemp.close();

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

                        if (headerName.trim().equalsIgnoreCase("departmentName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("shortName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNature(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveDepartmentMaster(mrTemp);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
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

    private int saveDepartmentMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, nature;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setSubCode(DmServer.getSerialNoBigInt(rss, "subGroup", cnTemp));
            if (DmShared.iNull(mrTemp.getSubCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            nature = DmServer.getCodeViaManualCode(mrTemp.getNature(), "name", "subGroupNature", "code", cnTemp);
            if (DmShared.iNull(nature).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getNature() + " nature not exists.");
            }

            sqlQuery = "select  count(sg.name) as cnt "
                    + "         from subgroup sg "
                    + "         where sg.name = :name and sg.subCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSubCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            //======================
            sqlQuery = "select  count(sg.manualCode) as cnt "
                    + "         from subgroup sg "
                    + "         where sg.manualCode = :shortName and sg.subCode <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("shortName", DmShared.xNull(mrTemp.getShortName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSubCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " short Name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            //==========================================================================

            //======================= INSERT IN SUBGROUP TABLE  ====================================
            sqlQuery = "insert into subGroup(subcode, name, displayname, nature, groupcode, filterNature, manualCode, site_Code, preparedBy, u_EntDt) "
                    + " values (:subcode, :name, :displayname, :nature, :groupcode, :filterNature, :manualCode, :sitecode, :uName, :uEntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", mrTemp.getSubCode());
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("displayname", mrTemp.getName());
            psTemp.setString("nature", DmShared.foriegnKey(nature));
            psTemp.setString("groupcode", acDepartmentCode);
            psTemp.setString("filterNature", filterNature);
            psTemp.setString("manualCode", mrTemp.getShortName());
            psTemp.setString("sitecode", DmServer.getSsnSiteCode(rss));
            psTemp.setString("uName", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            //======================= INSERT IN DEPARTMENTMAST TABLE  ===================
            sqlQuery = "insert into departmentmast(subCode,nature) values (:subcode, :nature) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", mrTemp.getSubCode());
            psTemp.setString("nature", DmShared.foriegnKey(mrTemp.getNature()));
            psTemp.executeUpdate();
            psTemp.close();

            //======================= INSERT IN subgroupsitelist TABLE  ===================
            sqlQuery = "insert into subGroupSiteList(subCode, site_Code) values (:subcode, :siteCode );";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subcode", DmShared.foriegnKey(mrTemp.getSubCode()));
            psTemp.setString("siteCode", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
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
