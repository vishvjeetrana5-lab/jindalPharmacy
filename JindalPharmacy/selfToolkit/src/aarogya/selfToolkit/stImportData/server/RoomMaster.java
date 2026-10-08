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
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoomMaster implements DmImportData_Interface {

    public RoomMaster() {
    }

    class Model extends KeyField {

        private String roomDesc, categoryText, nursingText, activeText;

        public Model() {
        }

        public String getRoomDesc() {
            return roomDesc;
        }

        public void setRoomDesc(String roomDesc) {
            this.roomDesc = roomDesc;
        }

        public String getCategoryText() {
            return categoryText;
        }

        public void setCategoryText(String categoryText) {
            this.categoryText = categoryText;
        }

        public String getNursingText() {
            return nursingText;
        }

        public void setNursingText(String nursingText) {
            this.nursingText = nursingText;
        }

        public String getActiveText() {
            return activeText;
        }

        public void setActiveText(String activeText) {
            this.activeText = activeText;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    @Override
    public ImportData_Mdl getImportDataModel() {
        final ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("roomNo");
        node.setSample("ICU");
        node.setInstruction("{unique;mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("roomDescription");
        node.setSample("Room");
        node.setInstruction("{unique;mandatory;alphanumeric;35c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("roomCategory");
        node.setSample("Room");
        node.setInstruction("{mandatory;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("nursingStation");
        node.setSample("Room101");
        node.setInstruction("{mandatory;50c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("RoomMaster");
        fileDetail.setServerClassName(RoomMaster.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {

        Model mrTemp;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound, isSuccess = true;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue;
        Object objValue;
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
                        if (headerName.trim().equalsIgnoreCase("roomNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSharedKeyFieldName(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("roomDescription")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 35, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setRoomDesc(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("roomCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCategoryText(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("nursingStation")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNursingText(fieldValue);
                            }
                        }

                    }
                    if (isFound) {
                        status = saveRoomMaster(mrTemp);
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

    private int saveRoomMaster(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, categoryCode = "", nursingCode = "", hkLocCategoryCode = "", subCode = "", roomName = "";
        int rtnStatus = 0, status = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setRoomDesc(DmShared.removeSpace(mrTemp.getRoomDesc()));
            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(rss, "roomMast", cnTemp));

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }
            //**********************************< Get  Category >***********************************
            categoryCode = DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getCategoryText()), "name", "roomCatMast", "code", cnTemp);
            if (DmShared.iNull(categoryCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getCategoryText()) + " category does not exist");
            }
            //**********************************< Get Location Category >***********************************
            hkLocCategoryCode = DmServer.getCodeViaManualCode(DmShared.xNull(categoryCode), "roomCategoryCode", "hkLocationCatMast", "code", cnTemp);
            if (DmShared.iNull(hkLocCategoryCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getCategoryText()) + " room category does not exist");
            }

            //**********************************< Get Location subCode >***********************************
            subCode = DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "roomCode", "hkLocationMast", "code", cnTemp);
            if (DmShared.iNull(subCode).longValue() == 0) {
                //**********************************< Generate subCode >***********************************
                subCode = DmServer.getSerialNoBigInt(rss, "hkLocationMast", cnTemp);
            }

            nursingCode = DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getNursingText()), "name", "nursingStationMast", "code", cnTemp);
            if (DmShared.iNull(nursingCode).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getNursingText()) + " nursing station does not exist");
            }

            sqlQuery = "SELECT COUNT(roomNo) AS cnt FROM roomMast WHERE roomNo = :roomNo AND code <> :code AND site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("roomNo", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("Room No. already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT COUNT(roomDescription) AS cnt FROM roomMast WHERE roomDescription = :roomDescription AND code <> :code AND site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("roomDescription", DmShared.xNull(mrTemp.getRoomDesc()));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException("Room Description already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "INSERT INTO roomMast (code, roomNo, roomDescription, roomCategoryCode, nursingStationCode, "
                    + " isActive,  u_EntDt, transferDt, preparedBy,site_Code)  "
                    + " VALUES (:code, :roomNo, :roomDescription, :roomCategoryCode, :nursingStationCode,  "
                    + " :isActive,  getdate(), NULL, :userName,:siteCode) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("roomNo", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("roomDescription", DmShared.xNull(mrTemp.getRoomDesc()));
            psTemp.setString("roomCategoryCode", DmShared.foriegnKey(categoryCode));
            psTemp.setString("nursingStationCode", DmShared.foriegnKey(nursingCode));
            psTemp.setString("isActive", DmShared.xNull("1"));
            psTemp.setString("userName", DmServer.getSsnUserName(rss));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(rss));
            psTemp.executeUpdate();
            psTemp.close();

            //**********************************< Insert Record into hkLocationMast & subGroup >***********************************
            sqlQuery = "insert into hkLocationMast(code, hkLocationCategoryCode, roomCode, name, isActive)  "
                    + " VALUES (:code, :hkLocationCategoryCode, :roomCode, :name, 1) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(subCode));
            psTemp.setString("hkLocationCategoryCode", DmShared.foriegnKey(hkLocCategoryCode));
            psTemp.setString("roomCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
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

                if (cnTemp != null) {
                    cnTemp.close();
                }

            } catch (Exception e) {
            }
        }
        return rtnStatus;
    }
}
