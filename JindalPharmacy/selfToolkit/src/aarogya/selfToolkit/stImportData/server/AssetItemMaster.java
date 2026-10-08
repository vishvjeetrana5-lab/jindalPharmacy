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
import java.util.Objects;

public class AssetItemMaster implements DmImportData_Interface {

    class Model {

        private String code, name, manualCode, assetLife, propertyCode, propertyName, propertyPrintSerialNo, nature, assetGroupName, assetCategoryName, assetDepRate, depMethod;

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

        public String getAssetLife() {
            return assetLife;
        }

        public void setAssetLife(String assetLife) {
            this.assetLife = assetLife;
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

        public String getAssetGroupName() {
            return assetGroupName;
        }

        public void setAssetGroupName(String assetGroupName) {
            this.assetGroupName = assetGroupName;
        }

        public String getAssetCategoryName() {
            return assetCategoryName;
        }

        public void setAssetCategoryName(String assetCategoryName) {
            this.assetCategoryName = assetCategoryName;
        }

        public String getAssetDepRate() {
            return assetDepRate;
        }

        public void setAssetDepRate(String assetDepRate) {
            this.assetDepRate = assetDepRate;
        }

        public String getDepMethod() {
            return depMethod;
        }

        public void setDepMethod(String depMethod) {
            this.depMethod = depMethod;
        }

        public Model() {
        }

    }

    class GroupHeader_Mdl {

        private String code, name, shortName, assetLife, assetGroupName, assetGroupCode, assetCategoryName, assetCategoryCode, assetDepRate;
        private boolean depMethod = false;
        private List<GroupProperty_Mdl> lstProperty = new ArrayList<>();

        public GroupHeader_Mdl() {
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

        public String getAssetGroupName() {
            return assetGroupName;
        }

        public void setAssetGroupName(String assetGroupName) {
            this.assetGroupName = assetGroupName;
        }

        public String getAssetGroupCode() {
            return assetGroupCode;
        }

        public void setAssetGroupCode(String assetGroupCode) {
            this.assetGroupCode = assetGroupCode;
        }

        public String getAssetCategoryName() {
            return assetCategoryName;
        }

        public void setAssetCategoryName(String assetCategoryName) {
            this.assetCategoryName = assetCategoryName;
        }

        public String getAssetCategoryCode() {
            return assetCategoryCode;
        }

        public void setAssetCategoryCode(String assetCategoryCode) {
            this.assetCategoryCode = assetCategoryCode;
        }

        public String getAssetDepRate() {
            return assetDepRate;
        }

        public void setAssetDepRate(String assetDepRate) {
            this.assetDepRate = assetDepRate;
        }

        public List<GroupProperty_Mdl> getLstProperty() {
            return lstProperty;
        }

        public void setLstProperty(List<GroupProperty_Mdl> lstProperty) {
            this.lstProperty = lstProperty;
        }

        public String getAssetLife() {
            return assetLife;
        }

        public void setAssetLife(String assetLife) {
            this.assetLife = assetLife;
        }

        public boolean isDepMethod() {
            return depMethod;
        }

        public void setDepMethod(boolean depMethod) {
            this.depMethod = depMethod;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.name);
            hash = 29 * hash + Objects.hashCode(this.shortName);
            hash = 29 * hash + Objects.hashCode(this.assetGroupName);
            hash = 29 * hash + Objects.hashCode(this.assetCategoryName);
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
            if (!Objects.equals(this.assetGroupName, other.assetGroupName)) {
                return false;
            }
            if (!Objects.equals(this.assetCategoryName, other.assetCategoryName)) {
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

    public AssetItemMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        List<CSVData> lstCSV = new ArrayList<CSVData>();
        CSVData node;

        node = new CSVData();
        node.setHeader("Name");
        node.setSample("Gram");
        node.setInstruction("{unique;mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("ManualCode");
        node.setSample("5");
        node.setInstruction("{mandatory;alphanumeric;20c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("AssetLife");
        node.setSample("General");
        node.setInstruction("{numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("AssetGroupName");
        node.setSample("General");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("AssetCategoryName");
        node.setSample("General");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("DepreciationMethod");
        node.setSample("General");
        node.setInstruction("{WDV/None}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("DepreciationRate");
        node.setSample("General");
        node.setInstruction("{numeric;8c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("PropertyName");
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
        fileDetail.setImportTo("AssetItemMaster");
        fileDetail.setServerClassName(AssetItemMaster.class.getName());

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
                        } else if (headerName.trim().equalsIgnoreCase("assetLife")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssetLife(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("assetGroupName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssetGroupName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("assetCategoryName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssetCategoryName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("depMethod")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDepMethod(fieldValue.equalsIgnoreCase("WDV"));
                            }

                        } else if (headerName.trim().equalsIgnoreCase("assetDepRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAssetDepRate(fieldValue);
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
                        status = saveAssetItem(groupHeader_Mdl);
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

    private int saveAssetItem(GroupHeader_Mdl mrTemp) {
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
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "assetItemMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = "select COUNT(um.name) as cnt from assetItemMast as um  where um.name = :name  AND um.code <> :code";
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

            if (!DmShared.xNull(mrTemp.getAssetGroupName()).equalsIgnoreCase("")) {
                mrTemp.setAssetGroupCode(DmServer.getCodeViaManualCode(mrTemp.getAssetGroupName(), "name", "assetGroupMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getAssetGroupCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getAssetGroupName() + " is not found in Group Master for item: " + DmShared.xNull(mrTemp.getName()) + ".");
                }
            }
            if (!DmShared.xNull(mrTemp.getAssetCategoryName()).equalsIgnoreCase("")) {
                mrTemp.setAssetCategoryCode(DmServer.getCodeViaManualCode(mrTemp.getAssetCategoryName(), "name", "assetCatMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getAssetCategoryCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new DmException(0, mrTemp.getAssetCategoryName() + " is not found in Cateogry Master for item: " + DmShared.xNull(mrTemp.getName()) + ".");
                }
            }

            sqlQuery = " insert Into assetItemMast(code, name, manualCode, assetGroupCode, assetCatCode, assetLife, assetDepRate, depMethod, "
                    + "  preparedBy, u_EntDt)  "
                    + "  values (:code, :name, :manualCode, :assetGroupCode, :assetCatCode, :assetLife, :assetDepRate, :depMethod, :preparedBy, "
                    + "  :u_EntDt)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.setString("name", mrTemp.getName());
            psTemp.setString("manualCode", mrTemp.getShortName());
            psTemp.setString("assetGroupCode", mrTemp.getAssetGroupCode());
            psTemp.setString("assetCatCode", mrTemp.getAssetCategoryCode());
            psTemp.setString("assetLife", mrTemp.getAssetLife());
            psTemp.setString("assetDepRate", mrTemp.getAssetDepRate());
            psTemp.setString("depMethod", mrTemp.isDepMethod() ? "0" : "1");
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            //delete assetGroupProperties
            sqlQuery = "DELETE FROM assetItemProperties WHERE code = :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (GroupProperty_Mdl node : mrTemp.getLstProperty()) {
                if (!DmShared.xNull(node.getName()).equalsIgnoreCase("")) {
                    node.setCode(DmServer.getCodeViaManualCode(node.getName(), "name", "assetPropertyMast", "Code", cnTemp));
                    if (DmShared.iNull(node.getCode()).longValue() > 0) {

                        sqlQuery = "INSERT INTO assetItemProperties (code, propertyCode, sno, nature) "
                                + " VALUES (:code, :propertyCode, :sno, :nature) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", mrTemp.getCode());
                        psTemp.setString("propertyCode", node.getCode());
                        psTemp.setString("sno", node.getPrintSno());
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
