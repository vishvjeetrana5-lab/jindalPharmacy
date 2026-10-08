package aarogya.selfToolkit.stImportData.server;

import aarogya.selfToolkit.stStatic.shared.STEnum.AssetVehicleNature;
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
import java.util.Objects;

public class AssetGroupMaster implements DmImportData_Interface {

    class Model {

        private String code, name, manualCode, assetNature, propertyCode, propertyName, propertyPrintSerialNo, nature;

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

        public String getAssetNature() {
            return assetNature;
        }

        public void setAssetNature(String assetNature) {
            this.assetNature = assetNature;
        }

        public String getPropertyCode() {
            return propertyCode;
        }

        public void setPropertyCode(String propertyCode) {
            this.propertyCode = propertyCode;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public void setPropertyName(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyPrintSerialNo() {
            return propertyPrintSerialNo;
        }

        public void setPropertyPrintSerialNo(String propertyPrintSerialNo) {
            this.propertyPrintSerialNo = propertyPrintSerialNo;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public Model() {
        }

    }

    class GroupHeader_Mdl {

        private AssetVehicleNature assetNature = AssetVehicleNature.GENERAL;
        private String code, name, shortName;
        private List<GroupProperty_Mdl> lstProperty = new ArrayList<>();

        public GroupHeader_Mdl() {
        }

        public AssetVehicleNature getAssetNature() {
            return assetNature;
        }

        public void setAssetNature(AssetVehicleNature assetNature) {
            this.assetNature = assetNature;
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

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public List<GroupProperty_Mdl> getLstProperty() {
            return lstProperty;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(this.name);
            hash = 47 * hash + Objects.hashCode(this.shortName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GroupHeader_Mdl other = (GroupHeader_Mdl) obj;
            if (!Objects.equals(this.name, other.name)) {
                return false;
            }
            if (!Objects.equals(this.shortName, other.shortName)) {
                return false;
            }
            return true;
        }

    }

    class GroupProperty_Mdl {

        private String code, name, printSno;
        private boolean exposed = false;

        public GroupProperty_Mdl() {
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

        public String getPrintSno() {
            return printSno;
        }

        public void setPrintSno(String printSno) {
            this.printSno = printSno;
        }

        public boolean isExposed() {
            return exposed;
        }

        public void setExposed(boolean exposed) {
            this.exposed = exposed;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public AssetGroupMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        List<CSVData> lstCSV = new ArrayList<CSVData>();
        CSVData node;

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Gram");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manualCode");
        node.setSample("5");
        node.setInstruction("{mandatory;alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("assetNature");
        node.setSample("General");
        node.setInstruction("{General/Vehicle/Ambulance}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("propertyName");
        node.setSample("Others");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("propertyPrintSerialNo");
        node.setSample("Others");
        node.setInstruction("{numeric;0-99c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nature");
        node.setSample("Expose");
        node.setInstruction("{Restricted/Expose}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Asset Group Master");
        fileDetail.setServerClassName(AssetGroupMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {

        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound = false;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
        String[] csvFileFormat;
        int rtnStatus = 0, status = 0;
        DmImportStatus importStatus = null;
        String prvGroup = "";
        List<GroupHeader_Mdl> lstGroup = new ArrayList<>();
        GroupHeader_Mdl mrTemp = null;
        GroupProperty_Mdl node = null;
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
                    if (mrTemp == null) {
                        mrTemp = new GroupHeader_Mdl();
                    }
                    node = new GroupProperty_Mdl();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                if (!prvGroup.equalsIgnoreCase(fieldValue)) {
//                                    if (!DmShared.xNull(mrTemp.getName()).equalsIgnoreCase("")) {
//                                        lstGroup.add(mrTemp);
//                                    }
                                    mrTemp = new GroupHeader_Mdl();
                                }
                                mrTemp.setName(fieldValue);
                                prvGroup = fieldValue;
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manualCode")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setShortName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("assetNature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssetNature(AssetVehicleNature.getViaText(fieldValue));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("propertyName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                node.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("propertyPrintSerialNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                node.setPrintSno(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                node.setExposed(fieldValue.equalsIgnoreCase("Expose"));
                                mrTemp.getLstProperty().add(node);
                            }
                        }
                    }
                    if (isFound) {
                        if (!lstGroup.contains(mrTemp)) {
                            lstGroup.add(mrTemp);
                        }
                    }

                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                }
                if (lstGroup.size() > 0) {
                    for (GroupHeader_Mdl groupHeader_Mdl : lstGroup) {
                        status = saveAssetGroup(groupHeader_Mdl);
                    }

                }
            }
            isFound = true;
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

    private int saveAssetGroup(GroupHeader_Mdl mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, gstUnitCode = "";
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "assetGroupMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select COUNT(um.name) as cnt from assetGroupMast as um  where um.name = :name  AND um.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getName()) + " Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "INSERT INTO assetGroupMast (code, name,manualCode,assetNature,preparedBY,u_EntDt)"
                    + "   VALUES(:code, :name,:manualCode,:assetNature,:preparedBY,:u_EntDt);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("manualCode", mrTemp.getShortName());
            psTemp.setInt("assetNature", mrTemp.getAssetNature().getId());
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            //delete assetGroupProperties
            sqlQuery = "DELETE FROM assetGroupProperties WHERE code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GroupProperty_Mdl node : mrTemp.getLstProperty()) {
                if (!DmShared.xNull(node.getName()).equalsIgnoreCase("")) {
                    node.setCode(DmServer.getCodeViaManualCode(node.getName(), "name", "assetPropertyMast", "Code", cnTemp));
                    if (DmShared.iNull(node.getCode()).longValue() > 0) {
                        //insert assetGroupProperties
                        sqlQuery = "insert into assetGroupProperties (code, propertyCode, SNo, nature)"
                                + " values (:code, :propertyCode, :SNo, :nature)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", mrTemp.getCode());
                        psTemp.setString("propertyCode", node.getCode());
                        psTemp.setString("SNo", node.getPrintSno());
                        psTemp.setString("nature", node.isExposed() ? "1" : "0");
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

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
