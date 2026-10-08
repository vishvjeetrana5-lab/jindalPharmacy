package dataman.dmToolkit.dmImportData.server;

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

public class ImportAccountGroup implements DmImportData_Interface {

    class Model {

        private String Code, groupName, contraGroupName, groupUnder, groupUnderName, groupNature, groupNatureName, natureCode, natureName;

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getContraGroupName() {
            return contraGroupName;
        }

        public void setContraGroupName(String contraGroupName) {
            this.contraGroupName = contraGroupName;
        }

        public String getGroupUnder() {
            return groupUnder;
        }

        public void setGroupUnder(String groupUnder) {
            this.groupUnder = groupUnder;
        }

        public String getGroupUnderName() {
            return groupUnderName;
        }

        public void setGroupUnderName(String groupUnderName) {
            this.groupUnderName = groupUnderName;
        }

        public String getGroupNature() {
            return groupNature;
        }

        public void setGroupNature(String groupNature) {
            this.groupNature = groupNature;
        }

        public String getGroupNatureName() {
            return groupNatureName;
        }

        public void setGroupNatureName(String groupNatureName) {
            this.groupNatureName = groupNatureName;
        }

        public String getNatureCode() {
            return natureCode;
        }

        public void setNatureCode(String natureCode) {
            this.natureCode = natureCode;
        }

        public String getNatureName() {
            return natureName;
        }

        public void setNatureName(String natureName) {
            this.natureName = natureName;
        }

        public Model() {
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;

    private String lineNoDisplay = "";

    public ImportAccountGroup() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("groupName");
        node.setSample("Sundry Creditors");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("contraGroupName");
        node.setSample("Sundry Creditors");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("groupUnderName");
        node.setSample("Current Liabilities");
        node.setInstruction("{alphanumeric;mandatory}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("Supplier");
        node.setInstruction("{mandatory;alphanumeric;mandatory;50c}");
        lstCSV.add(node);
        
        node = new CSVData();
        node.setHeader("mainGroup");
        node.setSample("Assets");
        node.setInstruction("{mandatory incase of blank underGroupName;alphanumeric 50c;Assets/Expenses Direct/Liabilities/Revenue Direct/Expenses InDirect/Revenue InDirect}}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Account Group");
        fileDetail.setServerClassName(ImportAccountGroup.class.getName());

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
        String[] csvFileFormat;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;

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
                csvFileFormat = DmImportData_Server.getCSVFormats(importData.getHmCSV()).get("header").split(",");
                dataFile = new File(importData.getDataFilePath());
                if (dataFile.exists()) {
                    csvReader = new DmCsvReader(dataFile);
                    csvReader.readLines();
                    status = DmImportData_Server.checkHeader(csvFileFormat, csvReader, handelFileLog);
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
                            status = DmImportStatus.ERROR.getId();
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("groupName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("contraGroupName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setContraGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("groupUnderName")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupUnderName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNatureName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("mainGroup")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupNatureName(fieldValue);
                            }
                        }

                    }
                    if (isFound) {
                        status = saveAccountGroup(mrTemp);
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

    private int saveAccountGroup(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery;
        int intSNo = 0, status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setGroupName(DmShared.removeSpace(mrTemp.getGroupName()));
            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "AcGroup", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "Select Count(GroupName) As Cnt From AcGroup Where GroupName = :groupName And GroupCode <> :groupCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("groupName", mrTemp.getGroupName());
            psTemp.setString("groupCode", mrTemp.getCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(mrTemp.getGroupName() + " Group Name Already Exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(mrTemp.getGroupUnderName()).trim().equalsIgnoreCase("")) {
                mrTemp.setGroupUnder("");
                mrTemp.setGroupNature("");
                sqlQuery = "Select groupCode,groupNature from AcGroup Where groupName = :groupUnderName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("groupUnderName", mrTemp.getGroupUnderName());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setGroupUnder(DmShared.iNull(rsTemp.getString("groupCode")).toString());
                    mrTemp.setGroupNature(DmShared.iNull(rsTemp.getString("groupNature")).toString());
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getGroupUnder()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();

                    throw new DmException(0, mrTemp.getGroupUnderName() + " not exists.");
                }
            } else {
                mrTemp.setGroupUnder("");
                mrTemp.setGroupNature("");
                switch (DmShared.xNull(mrTemp.getGroupNatureName()).trim()) {
                    case "Liabilities":
                        mrTemp.setGroupNature("0");
                        break;
                    case "Assets":
                        mrTemp.setGroupNature("1");
                        break;
                    case "Expenses Direct":
                        mrTemp.setGroupNature("2");
                        break;
                    case "Expenses Indirect":
                        mrTemp.setGroupNature("3");
                        break;
                    case "Revenue Direct":
                        mrTemp.setGroupNature("4");
                        break;
                    case "Revenue Indirect":
                        mrTemp.setGroupNature("5");
                        break;
                }
                if (DmShared.iNull(mrTemp.getGroupNature()).longValue() != 0 && DmShared.iNull(mrTemp.getGroupNature()).longValue() != 1
                        && DmShared.iNull(mrTemp.getGroupNature()).longValue() != 2 && DmShared.iNull(mrTemp.getGroupNature()).longValue() != 3
                        && DmShared.iNull(mrTemp.getGroupNature()).longValue() != 4 && DmShared.iNull(mrTemp.getGroupNature()).longValue() != 5) {
                    status = DmImportStatus.WARNING.getId();

                    throw new DmException(0, mrTemp.getGroupNatureName()+ " not exists.");
                }
            }

            mrTemp.setNatureCode(DmServer.getCodeViaManualCode(mrTemp.getNatureName(), "name", "subGroupNature", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getNatureCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getNatureName() + " not exists.");
            }

            //* Fetching Group Path Of Particular Group Under For Later Use ********************
            sqlQuery = "Select GroupCode,SNo,GroupUnder From AcGroupPath Where GroupCode = :groupCode Order By SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("groupCode", mrTemp.getGroupUnder());
            rsTemp1 = psTemp1.executeQuery();

            sqlQuery = "Insert Into AcGroup(GroupCode,GroupName, "
                    + "GroupUnder, "
                    + "Nature,preparedBY,isSysGroup, "
                    + "U_EntDt,GroupNature,ContraGroupName) Values  "
                    + "(:groupCode,:groupName,:groupUnder,:nature,:preparedBY,:sysgroup,getdate(),:groupNature,:contraGroupName) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("groupCode", mrTemp.getCode());
            psTemp.setString("groupName", mrTemp.getGroupName());
            psTemp.setString("groupUnder", DmShared.foriegnKey(mrTemp.getGroupUnder()));
            psTemp.setString("nature", mrTemp.getNatureCode());
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setString("sysgroup", "0");
            psTemp.setString("groupNature", mrTemp.getGroupNature());
            psTemp.setString("contraGroupName", mrTemp.getContraGroupName());
            psTemp.executeUpdate();
            psTemp.close();

            intSNo = 0;
            //* Inserting Path For Particular Group Under Which Is Already Fetched Above ************
            if (DmShared.iNull(mrTemp.getGroupUnder()).longValue() > 0) {
                while (rsTemp1.next()) {
                    intSNo++;
                    sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("groupCode", mrTemp.getCode());
                    psTemp.setInt("sno", intSNo);
                    psTemp.setString("groupUnder", rsTemp1.getString("GroupUnder"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
                //* Inserting Path For Particular Group Under *******************
                intSNo++;
                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) Values (:groupCode,:sno,:groupUnder) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("groupCode", mrTemp.getCode());
                psTemp.setInt("sno", intSNo);
                psTemp.setString("groupUnder", mrTemp.getGroupUnder());
                psTemp.executeUpdate();
                psTemp.close();
            }
            rsTemp1.close();
            psTemp1.close();

            spBegin.commit();
            cnTemp.close();
            rtnStatus = DmImportStatus.SUCCESS.getId();;
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
