package dataman.dmToolkit.dmImportData.server;

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
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ImportNumberSystemIni implements DmImportData_Interface {

    private DmRSS rssTemp;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ImportNumberSystemIni() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("nsGroup");
        node.setSample("UMAST");
        node.setInstruction("{unique,mandatory,alphanumeric,5c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nsFormat");
        node.setSample("{counter}/{vouchersname}{voucherprefix}{sitesname}");
        node.setInstruction("{mandatory,({counter},{company},{vouchersname},{voucher},{voucherprefix},{day},{month},{yearfull},{year},{sitesname},{site})}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("counterFormat");
        node.setSample("0");
        node.setInstruction("{mandatory,numeric,only 0(zero char)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isYearWise");
        node.setSample("Yes");
        node.setInstruction("{mandatory,(Yes/No)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("isSiteWise");
        node.setSample("Yes");
        node.setInstruction("{mandatory,(Yes/No)}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("includeV_Type");
        node.setSample("UMAST");
        node.setInstruction("{unique,mandatory,alphanumeric,5c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Number System");
        fileDetail.setServerClassName(ImportNumberSystemIni.class.getName());

        return fileDetail;
    }

    class NumberSystemMdl {

        private String includev_Typecode;
        private String includev_Type;
        private String nsFormat;
        private String counterFormat;
        private String nsGroup;
        private String nsGroupCode;
        private boolean yearWise = false, siteWise = false;

        public NumberSystemMdl() {
        }

        public String getIncludev_Type() {
            return includev_Type;
        }

        public void setIncludev_Type(String includev_Type) {
            this.includev_Type = includev_Type;
        }

        public String getNsFormat() {
            return nsFormat;
        }

        public void setNsFormat(String nsFormat) {
            this.nsFormat = nsFormat;
        }

        public String getCounterFormat() {
            return counterFormat;
        }

        public void setCounterFormat(String counterFormat) {
            this.counterFormat = counterFormat;
        }

        public String getIncludev_Typecode() {
            return includev_Typecode;
        }

        public void setIncludev_Typecode(String includev_Typecode) {
            this.includev_Typecode = includev_Typecode;
        }

        public String getNsGroup() {
            return nsGroup;
        }

        public void setNsGroup(String nsGroup) {
            this.nsGroup = nsGroup;
        }

        public String getNsGroupCode() {
            return nsGroupCode;
        }

        public void setNsGroupCode(String nsGroupCode) {
            this.nsGroupCode = nsGroupCode;
        }

        public boolean isYearWise() {
            return yearWise;
        }

        public void setYearWise(boolean yearWise) {
            this.yearWise = yearWise;
        }

        public boolean isSiteWise() {
            return siteWise;
        }

        public void setSiteWise(boolean siteWise) {
            this.siteWise = siteWise;
        }

    }


    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        NumberSystemMdl mrTemp;
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
            this.rssTemp = rss;

            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
//
//            acDepartmentCode = DmServer.getCodeViaManualCode("1", "1", "enviroCompany", "acGroupDepartmentCode", cnTemp);
//
//            if (DmShared.iNull(acDepartmentCode).longValue() == 0) {
//                status = DmImportStatus.ERROR.getId();
//                throw new DmException(0, "Define Department A/c Group in enviro.");
//            }
//            //======================================END======================================
//
//            //==================== FILTER NATURE ============================
//            filterNature = DmServer.getCodeViaManualCode("DP", "manualCode", "subgroupNature", "Code", cnTemp);
//            if (DmShared.iNull(filterNature).longValue() == 0) {
//                status = DmImportStatus.ERROR.getId();
//                throw new DmException(0, "Department 'DP' nature not exists.");
//            }
//            //==========================================================
//            cnTemp.close();

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
                    mrTemp = new NumberSystemMdl();
                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }

                        if (headerName.trim().equalsIgnoreCase("nsGroup")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNsGroup(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nsFormat")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNsFormat(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("counterFormat")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 25, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCounterFormat(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isYearWise")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setYearWise(fieldValue.equalsIgnoreCase("Yes"));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("isSiteWise")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSiteWise(fieldValue.equalsIgnoreCase("Yes"));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("includeV_Type")) {
                            if (!DmImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 5, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIncludev_Type(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = save(mrTemp);
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

    public int save(NumberSystemMdl csv) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, incv_Type = "";
        String[] includeVTypeList;
        ResultSet rsTemp = null;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(csv.getIncludev_Type()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Include Voucher for " + csv.getNsGroup() + " not found.");

            } else {

                includeVTypeList = csv.getIncludev_Type().split(",");  // To split comma from csv
                for (int i = 0; i < includeVTypeList.length; i++) {

                    csv.setIncludev_Typecode(DmServer.getVType((includeVTypeList[i]).trim(), cnTemp));
                    if (!DmShared.xNull(csv.getIncludev_Typecode()).trim().equalsIgnoreCase("")) {
                        if (!incv_Type.equalsIgnoreCase("")) {
                            incv_Type += ",";
                        }
                        incv_Type += DmShared.xNull(csv.getIncludev_Typecode());
                    } else {
                        status = DmImportStatus.WARNING.getId();
                        throw new UnsupportedOperationException("Include Voucher Code for " + includeVTypeList[i] + " not found.");
                    }
                }
            }

            csv.setNsGroupCode(DmServer.getVType(csv.getNsGroup().trim(), cnTemp));

            if (DmShared.xNull(csv.getNsGroupCode()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException(" nsGroup Code for " + csv.getNsGroup() + " not found.");
            }

            sqlQuery = "SELECT COUNT(nsgroup) AS cnt FROM numbersystemformat WHERE nsgroup = :nsgroup  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsgroup", DmShared.xNull(csv.getNsGroupCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("nsGroup " + csv.getNsGroup() + " already exists.");
            }
            rsTemp.close();
            psTemp.close();

            includeVTypeList = incv_Type.split(",");
            for (int i = 0; i < includeVTypeList.length; i++) {

                sqlQuery = "select count(v_Type) AS cnt from numbersystem WHERE v_Type = :v_Type  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("v_Type", DmShared.xNull(includeVTypeList[i]));
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("nsGroup:" + csv.getNsGroup() + " voucher Type: " + includeVTypeList[i] + " exists.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " Insert Into numberSystem(nsGroup,v_type,u_Name,u_EntDt, includeV_Type) "
                        + "Values (:nsGroup,:v_type,:u_Name,GETDATE(), :includeV_Type)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("nsGroup", DmShared.xNull(csv.getNsGroupCode()));
                psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(rssTemp)));
                psTemp.setString("v_type", DmShared.xNull(includeVTypeList[i]));
                psTemp.setString("includeV_Type", DmShared.xNull(incv_Type));
                psTemp.executeUpdate();
                psTemp.close();
            }

            sqlQuery = " Insert Into numbersystemformat(nsGroup,nsFormat,counterFormat,isYearWise,isSiteWise) "
                    + "Values (:nsGroup,:nsFormat,:counterFormat,:isYearWise,:isSiteWise)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("nsGroup", DmShared.xNull(csv.getNsGroupCode()));
            psTemp.setString("nsFormat", DmShared.xNull(csv.getNsFormat()));
            psTemp.setString("counterFormat", DmShared.xNull(csv.getCounterFormat()));
            psTemp.setString("isYearWise", csv.isYearWise() ? "1" : "0");
            psTemp.setString("isSiteWise", csv.isSiteWise() ? "1" : "0");
            psTemp.executeUpdate();
            psTemp.close();

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
                handelFileLog.writeLine(e.getMessage());
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
