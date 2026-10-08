package aarogya.selfToolkit.stImportData.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.SaleRateRule_Mdl;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ItemOpening implements DmImportData_Interface {

    class HeaderModel {

        private String receivedFromCode, receivedFromName, departmentName, departmentCode;
        private int recordId;
        private String userName, siteCode, voucherPrefix, docId;

        private List<LineModel> lineModels = new ArrayList<LineModel>();

        public HeaderModel() {
        }

        public String getReceivedFromCode() {
            return receivedFromCode;
        }

        public void setReceivedFromCode(String receivedFromCode) {
            this.receivedFromCode = receivedFromCode;
        }

        public String getReceivedFromName() {
            return receivedFromName;
        }

        public void setReceivedFromName(String receivedFromName) {
            this.receivedFromName = receivedFromName;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getDepartmentCode() {
            return departmentCode;
        }

        public void setDepartmentCode(String departmentCode) {
            this.departmentCode = departmentCode;
        }

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getSiteCode() {
            return siteCode;
        }

        public void setSiteCode(String siteCode) {
            this.siteCode = siteCode;
        }

        public String getVoucherPrefix() {
            return voucherPrefix;
        }

        public void setVoucherPrefix(String voucherPrefix) {
            this.voucherPrefix = voucherPrefix;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public List<LineModel> getLineModels() {
            return lineModels;
        }

        public void setLineModels(List<LineModel> lineModels) {
            this.lineModels = lineModels;
        }

    }

    class LineModel {

        private String itemName, itemCode, batchNo, quantity,
                packRate, mrpPack, manufacturerDate, bestBefore, expiryDate, refNo, refDate, remark, v_SNo, skuRate, skuMRP, amount,
                purchaseUnitCode, skuQtyForRate, unitQuantity, colourCode, colourName, sizeCode, sizeName;
        private String lineNoDisplay;

        public LineModel() {
        }

        public String getLineNoDisplay() {
            return lineNoDisplay;
        }

        public void setLineNoDisplay(String lineNoDisplay) {
            this.lineNoDisplay = lineNoDisplay;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getPackRate() {
            return packRate;
        }

        public void setPackRate(String packRate) {
            this.packRate = packRate;
        }

        public String getMrpPack() {
            return mrpPack;
        }

        public void setMrpPack(String mrpPack) {
            this.mrpPack = mrpPack;
        }

        public String getManufacturerDate() {
            return manufacturerDate;
        }

        public void setManufacturerDate(String manufacturerDate) {
            this.manufacturerDate = manufacturerDate;
        }

        public String getBestBefore() {
            return bestBefore;
        }

        public void setBestBefore(String bestBefore) {
            this.bestBefore = bestBefore;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getRefDate() {
            return refDate;
        }

        public void setRefDate(String refDate) {
            this.refDate = refDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getV_SNo() {
            return v_SNo;
        }

        public void setV_SNo(String v_SNo) {
            this.v_SNo = v_SNo;
        }

        public String getSkuRate() {
            return skuRate;
        }

        public void setSkuRate(String skuRate) {
            this.skuRate = skuRate;
        }

        public String getSkuMRP() {
            return skuMRP;
        }

        public void setSkuMRP(String skuMRP) {
            this.skuMRP = skuMRP;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPurchaseUnitCode() {
            return purchaseUnitCode;
        }

        public void setPurchaseUnitCode(String purchaseUnitCode) {
            this.purchaseUnitCode = purchaseUnitCode;
        }

        public String getSkuQtyForRate() {
            return skuQtyForRate;
        }

        public void setSkuQtyForRate(String skuQtyForRate) {
            this.skuQtyForRate = skuQtyForRate;
        }

        public String getUnitQuantity() {
            return unitQuantity;
        }

        public void setUnitQuantity(String unitQuantity) {
            this.unitQuantity = unitQuantity;
        }

        public String getColourCode() {
            return colourCode;
        }

        public void setColourCode(String colourCode) {
            this.colourCode = colourCode;
        }

        public String getColourName() {
            return colourName;
        }

        public void setColourName(String colourName) {
            this.colourName = colourName;
        }

        public String getSizeCode() {
            return sizeCode;
        }

        public void setSizeCode(String sizeCode) {
            this.sizeCode = sizeCode;
        }

        public String getSizeName() {
            return sizeName;
        }

        public void setSizeName(String sizeName) {
            this.sizeName = sizeName;
        }

    }

    private DmRSSPrivate rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";
    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.ITEM_OPENING + "'";
    private String startDate = "";

    public ItemOpening() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("receivedFrom");
        node.setSample("RAVI KUMAR");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("departmentIn");
        node.setSample("STORE");
        node.setInstruction("{mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itemName");
        node.setSample("AC REMOTE");
        node.setInstruction("{mandatory;alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Quantity");
        node.setSample("50");
        node.setInstruction("{mandatory;numeric;12}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("packRate");
        node.setSample("200");
        node.setInstruction("{mandatory;numeric;10}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("batchNo");
        node.setSample("1309");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("MRP/Pack");
        node.setSample("10");
        node.setInstruction("{numeric;12}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manufacturingDate");
        node.setSample("12-may-2018");
        node.setInstruction("{alphanumeric;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("bestBeforeDays");
        node.setSample("12");
        node.setInstruction("{numeric;6}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("expiryDate");
        node.setSample("12-May-2019");
        node.setInstruction("{alphanumeric;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("colourName");
        node.setSample("RED");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("sizeName");
        node.setSample("20");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("refNo");
        node.setSample("REF001");
        node.setInstruction("{alphanumeric;20c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("refDate");
        node.setSample("12-May-2018");
        node.setInstruction("{alphanumeric;11c;dd-MMM-yyyy}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("remark");
        node.setSample("opening entry");
        node.setInstruction("{alphanumeric;200c");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Item Opening");
        fileDetail.setServerClassName(ItemOpening.class.getName());

        return fileDetail;
    }

    @Override
    public DmImportStatus importCSV(DmRSSPrivate rss, ImportData_Mdl importData) {
        LineModel mrTempLine;
        DmCsvReader csvReader = null;
        File dataFile = null;
        boolean isFound, isSuccess = true;
        DmDynamicGridRow dynamicGridRow;
        String fieldValue, sqlQuery, vType = "";
        Object objValue;
        String[] csvFileFormat;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String previousDepartmentCode = "", previousReceiveFromCode = "", previousDepartmentName = "", previousReceiveFromName = "";
        String currentDepartmentName = "", currentDepartmentCode = "", currentReceiveFromName = "", currentReceiveFromCode = "";
        int lineNo = 0;
        DmImportStatus importStatus = null;
        int status = 0, rtnStatus = 0;

        int recordId = -1;
        List<HeaderModel> lstHeader = new ArrayList<HeaderModel>();
        try {
            this.rss = rss;
            if (DmShared.xNull(importData.getLogFilePath()).trim().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new Exception("Log file path can not be blank.");
            }
            handelFileLog = new DmHandelFile(importData.getLogFilePath(), true);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            startDate = importData.getStartDate();
            /*====================================< Get Voucher Type >====================================*/
            sqlQuery = "SELECT v_Type FROM voucher_type  WHERE  short_Name = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();
            /*==========================================================================================*/
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

                previousDepartmentCode = "";
                previousReceiveFromCode = "";
                recordId = -1;
                lineNo = 0;

                for (int i = 0; i < csvReader.getRows().size(); i++) {
                    lineNoDisplay = "Line " + String.valueOf(++lineNo) + ": ";
                    dynamicGridRow = csvReader.getRows().get(i);
                    mrTempLine = new LineModel();

                    isFound = true;

                    for (String headerName : csvFileFormat) {
                        objValue = dynamicGridRow.getCells().get(headerName.toLowerCase());
                        if (objValue == null) {
                            fieldValue = "";
                        } else {
                            fieldValue = String.valueOf(objValue);
                        }
                        if (headerName.trim().equalsIgnoreCase("receivedFrom")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                currentReceiveFromName = DmShared.xNull(fieldValue).trim();
                            }
                        } else if (headerName.trim().equalsIgnoreCase("departmentIn")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                currentDepartmentName = DmShared.xNull(fieldValue).trim();
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setItemName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("batchNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setBatchNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("colourName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setColourName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("sizeName")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setSizeName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Quantity")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 12, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setQuantity(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("packRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setPackRate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("MRP/Pack")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setMrpPack(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manufacturingDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setManufacturerDate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("bestBeforeDays")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, true, false, false, false, false, 6, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setBestBefore(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("expiryDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setExpiryDate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("refNo")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 20, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setRefNo(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("refDate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, true, false, 11, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setRefDate(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("remark")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTempLine.setRemark(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        //========================================== GET DEPARTMENT CODE =======================================================

                        currentDepartmentCode = DmServer.getCodeViaManualCode(currentDepartmentName, "name", "subgroup", "subCode", cnTemp);
                        if (DmShared.iNull(currentDepartmentCode).longValue() == 0) {
                            status = DmImportStatus.WARNING.getId();
                            throw new DmException(0, DmShared.xNull(currentDepartmentName) + " Department  does not exist");

                        }
                        //========================================== END ==========================================================================

                        //========================================== GET RECEIVE FROM CODE =======================================================
                        if (!DmShared.xNull(currentReceiveFromName).trim().equalsIgnoreCase("")) {
                            currentReceiveFromCode = DmServer.getCodeViaManualCode(currentReceiveFromName, "name", "subgroup", "subcode", cnTemp);
                            if (DmShared.iNull(currentReceiveFromCode).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                handelFileLog.writeLine(lineNoDisplay + currentReceiveFromName + " not inserted. Received From not exists.");
                            }
                        }
                        //========================================== END ==========================================================================
                        LineModel lineMdl = new LineModel();
                        lineMdl.setLineNoDisplay(lineNoDisplay);
                        lineMdl.setItemName(DmShared.xNull(mrTempLine.getItemName()));
                        lineMdl.setBatchNo(DmShared.xNull(mrTempLine.getBatchNo()));
                        lineMdl.setColourName(DmShared.xNull(mrTempLine.getColourName()));
                        lineMdl.setSizeName(DmShared.xNull(mrTempLine.getSizeName()));
                        lineMdl.setQuantity(DmShared.xNull(mrTempLine.getQuantity()));
                        lineMdl.setPackRate(DmShared.xNull(mrTempLine.getPackRate()));
                        lineMdl.setMrpPack(DmShared.xNull(mrTempLine.getMrpPack()));
                        lineMdl.setManufacturerDate(DmShared.xNull(mrTempLine.getManufacturerDate()));
                        lineMdl.setBestBefore(DmShared.xNull(mrTempLine.getBestBefore()));
                        lineMdl.setExpiryDate(DmShared.xNull(mrTempLine.getExpiryDate()));
                        lineMdl.setRefNo(DmShared.xNull(mrTempLine.getRefNo()));
                        lineMdl.setRefDate(DmShared.xNull(mrTempLine.getRefDate()));
                        lineMdl.setRemark(DmShared.xNull(mrTempLine.getRemark()));
                        if (DmShared.iNull(currentDepartmentCode).longValue() != DmShared.iNull(previousDepartmentCode).longValue()
                                || DmShared.iNull(currentReceiveFromCode).longValue() != DmShared.iNull(previousReceiveFromCode).longValue()) {
                            HeaderModel headerModel = new HeaderModel();
                            headerModel.setDepartmentCode(currentDepartmentCode);
                            headerModel.setDepartmentName(currentDepartmentName);
                            headerModel.setReceivedFromCode(currentReceiveFromCode);
                            headerModel.setReceivedFromName(currentReceiveFromName);
                            headerModel.getLineModels().add(lineMdl);
                            lstHeader.add(headerModel);

                            ++recordId;
                            previousDepartmentCode = currentDepartmentCode;
                            previousReceiveFromCode = currentReceiveFromCode;

                        } else {
                            lstHeader.get(recordId).getLineModels().add(lineMdl);
                        }
                    } else {
                        isSuccess = false;
                    }
                }
                if (isSuccess) {
                    status = saveItemOpening(vType, lstHeader);
                }

                if (status > rtnStatus) {
                    rtnStatus = status;
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

    private int saveItemOpening(String vType, List<HeaderModel> lstHeader) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, userName = "", siteCode = "", voucherPrefix = "", isBestBefore = "", condition = "";
        String propertyList = "";
        int purchaseUnitDecimalPlaces = 0, skuDecimalPlaces = 7;
        boolean isAllRecordImported = true, flagContinue = false;
        boolean isActive_BatchNo = false, isActive_ExpiryDate = false, isActive_MRP = false, isActive_Colour = false, isActive_Size = false;
        int status = 0, rtnStatus = 0;
        List<SaleRateRule_Mdl> lstSaleRateRule = new ArrayList<>();

        try {
            userName = DmServer.getSsnUserName(rss);
            siteCode = DmServer.getSsnSiteCode(rss);
            voucherPrefix = DmServer.getSsnVoucherPrefix(rss);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            lstSaleRateRule.addAll(STServer.getSaleRateRule(cnTemp));
            cnTemp.close();

            for (HeaderModel headerModel : lstHeader) {
                flagContinue = false;
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));

                spBegin = new DmSavePoint(cnTemp);
                RecId recid = new RecId();

                //========================================== GET DOCID & RECID ============================================================
                headerModel.setDocId(DmServer.getDocId(DmShared.xNull(vType), voucherPrefix, cnTemp, rss));
                recid = (DmServer.getRecId("stockHead", "docId", DmShared.xNull(headerModel.getDocId()), "recId", recid, startDate, DmShared.xNull(vType), voucherPrefix, cnTemp, rss, true));
                //==============================================END ==========================================================================
                //========================================== CHECK IF DOCID IS NULL =======================================================
                if (headerModel.getDocId().equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new UnsupportedOperationException("Invalid key.");
                }
                //========================================== END ==========================================================================

                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId, "
                        + " site_Code, transferDt, preparedBy, u_EntDt,recIdPrefix) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId, "
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt,:recIdPrefix) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(headerModel.getDocId()).toString());
                psTemp.setString("recId", DmShared.xNull(recid.getRecIdValue()));
                psTemp.setString("v_Type", DmShared.foriegnKey(vType));
                psTemp.setDate("v_Date", DmServer.convertDate(startDate));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(recid.getCounter()).toString());
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("recIdPrefix", DmShared.xNull(recid.getPrefix()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();

                /*==============================<< Delete records from stockHeadDetail >>====================================*/
                sqlQuery = "Delete From stockHeadDetail Where docId= :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(headerModel.getDocId()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                int v_SNo = 0;
                for (LineModel lineModel : headerModel.getLineModels()) {
                    condition = "";
                    lineNoDisplay = DmShared.xNull(lineModel.getLineNoDisplay());
                    lineModel.setItemCode(DmServer.getCodeViaManualCode(lineModel.getItemName(), "name", "itemMast", "code", cnTemp));
                    if (DmShared.iNull(lineModel.getItemCode()).longValue() == 0) {
                        status = DmImportStatus.WARNING.getId();
                        handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. item not exists.");
                        flagContinue = true;
                        break;
                    }

                    sqlQuery = "SELECT  um.decimalPlaces as skuDecimalPlaces, ump.decimalPlaces as purchaseUnitDecimalPlaces, "
                            + " im.purchaseUnitConversionRate, im.purchaseUnit as purchaseUnitCode,"
                            + " im.isBestBefore, im.propertyList "
                            + " FROM itemMast as im"
                            + "	LEFT JOIN unitmast um ON im.sku = um.code"
                            + "	LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code"
                            + " WHERE  im.Code =:medCode  ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("medCode", DmShared.iNull(lineModel.getItemCode()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        skuDecimalPlaces = DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue();
                        purchaseUnitDecimalPlaces = DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).intValue();
                        lineModel.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                        lineModel.setPurchaseUnitCode(DmShared.iNull(rsTemp.getString("purchaseUnitCode")).toString());
                        isBestBefore = DmShared.iNull(rsTemp.getString("isBestBefore")).toString();
                        propertyList = DmShared.xNull(rsTemp.getString("propertyList"));
                    }
                    rsTemp.close();
                    psTemp.close();

                    if (DmShared.iNull(isBestBefore).intValue() == 1) {
                        if (!DmShared.xNull(lineModel.getBestBefore()).equalsIgnoreCase("") && !DmShared.dNull(lineModel.getManufacturerDate()).equalsIgnoreCase("")) {
                            Date tempDate = DmShared.convertDate(DmShared.dNull(lineModel.getManufacturerDate()));
                            CalendarUtil.addDaysToDate(tempDate, DmShared.iNull(lineModel.getBestBefore()).intValue());
                            lineModel.setExpiryDate(DmShared.dNull(tempDate));
                        } else {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted because Manufacturing Date & Best Before is mandatory");
                            flagContinue = true;
                            break;
                        }
                    } else {
                        lineModel.setManufacturerDate("");
                        lineModel.setBestBefore("");
//                        if (!DmShared.dNull(lineModel.getExpiryDate()).equalsIgnoreCase("")) {
//                            lineModel.setManufacturerDate("");
//                            lineModel.setBestBefore("");
//                        } else {
//                            status = DmImportStatus.WARNING.getId();
//                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. Expiry Date is Mandatory");
//                            flagContinue = true;
//                            break;
//                        }
                    }

                    //Get applicable properties
                    isActive_BatchNo = false;
                    isActive_ExpiryDate = false;
                    isActive_MRP = false;
                    isActive_Colour = false;
                    isActive_Size = false;
                    if (!DmShared.xNull(propertyList).equalsIgnoreCase("")) {
                        for (String prop : DmShared.xNull(propertyList).split(",")) {
                            if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.BATCH_NO.getPropertyManualCode())) {
                                isActive_BatchNo = true;
                            } else if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.EXPIRY_DATE.getPropertyManualCode())) {
                                isActive_ExpiryDate = true;
                            } else if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.MRP.getPropertyManualCode())) {
                                isActive_MRP = true;
                            } else if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())) {
                                isActive_Colour = true;
                            } else if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
                                isActive_Size = true;
                            }
                        }
                    }

                    /*=========<Get Batch No>=======*/
                    if (isActive_BatchNo) {
                        if (DmShared.xNull(lineModel.getBatchNo()).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. Batch No is blank.");
                            flagContinue = true;
                            break;
                        }
                    } else {
                        if (!DmShared.xNull(lineModel.getBatchNo()).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. Batch No property is not active.");
                            flagContinue = true;
                            break;
                        }
                    }

                    /*=========<Get Expiry date>=======*/
                    if (isActive_ExpiryDate) {
                        if (DmShared.dNull(lineModel.getExpiryDate()).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. Expiry Date is blank.");
                            flagContinue = true;
                            break;
                        }
                    } else {
                        if (!DmShared.dNull(lineModel.getExpiryDate()).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. Expiry Date property is not active.");
                            flagContinue = true;
                            break;
                        }
                    }

                    /*=========<Get Mrp >=======*/
                    if (isActive_MRP) {
                        if (DmShared.vNull(lineModel.getMrpPack()) <= 0) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. MRP is blank.");
                            flagContinue = true;
                            break;
                        }
                    } else {
                        if (DmShared.vNull(lineModel.getMrpPack()) > 0) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. MRP property is not active.");
                            flagContinue = true;
                            break;
                        }
                    }

                    /*=========<Get Colour Code>=======*/
                    if (isActive_Colour) {
                        if (!DmShared.xNull(lineModel.getColourName()).trim().equalsIgnoreCase("")) {
                            lineModel.setColourCode(DmServer.getCodeViaManualCode(lineModel.getColourName(), "name", "itemColourMast", "code", cnTemp));
                            if (DmShared.iNull(lineModel.getColourCode()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. colour not exists.");
                                flagContinue = true;
                                break;
                            }
                        } else {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. colour is blank.");
                            flagContinue = true;
                            break;
                        }
                    } else {
                        if (!DmShared.xNull(lineModel.getColourName()).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. colour property is not active.");
                            flagContinue = true;
                            break;
                        }
                    }

                    /*=========<Get Size Code>=======*/
                    if (isActive_Size) {
                        if (!DmShared.xNull(lineModel.getSizeName()).trim().equalsIgnoreCase("")) {
                            lineModel.setSizeCode(DmServer.getCodeViaManualCode(lineModel.getSizeName(), "name", "itemSizeMast", "code", cnTemp));
                            if (DmShared.iNull(lineModel.getSizeCode()).longValue() == 0) {
                                status = DmImportStatus.WARNING.getId();
                                handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. size not exists.");
                                flagContinue = true;
                                break;
                            }
                        } else {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. size is blank.");
                            flagContinue = true;
                            break;
                        }
                    } else {
                        if (!DmShared.xNull(lineModel.getSizeName()).trim().equalsIgnoreCase("")) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + lineModel.getItemName() + " not inserted. size property is not active.");
                            flagContinue = true;
                            break;
                        }
                    }
                    /*--------------------<*********>-------------------*/

                    if (!DmShared.dNull(lineModel.getExpiryDate()).equalsIgnoreCase("")) {
                        condition += " and shd.expiryDate=:expiryDate  ";
                    } else {
                        condition += " and shd.expiryDate IS NULL ";
                    }

                    if (!DmShared.dNull(lineModel.getManufacturerDate()).equalsIgnoreCase("")) {
                        condition += " and shd.manufacturingDate=:manufacturingDate  ";
                    } else {
                        condition += " and shd.manufacturingDate IS NULL ";
                    }
                    sqlQuery = "SELECT count(*) as cnt "
                            + " FROM stockHead as sh "
                            + "	LEFT JOIN stockHeadDetail as shd ON sh.docId = shd.docId "
                            + " WHERE sh.site_Code = :site_Code "
                            + " and sh.v_Date=:v_Date "
                            + " and isNull(shd.departmentCode,0)=:departmentCode "
                            + " and isNull(shd.subCode,0)=:receivedFrom "
                            + " and isNull(shd.itemCode,0)=:medCode "
                            + " and isNull(shd.batchNo,'')= :batchNo "
                            + " and isNull(shd.colourCode,0)= :colourCode "
                            + " and isNull(shd.sizeCode,0)=:sizeCode "
                            + " and isNull(shd.mrp,0)=:mrp "
                            + " and isNull(shd.rate,0)=:rate "
                            + " " + condition;
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(rss)));
                    psTemp.setString("v_Date", DmShared.xNull(startDate));
                    psTemp.setString("departmentCode", DmShared.iNull(headerModel.getDepartmentCode()).toString());
                    psTemp.setString("receivedFrom", DmShared.iNull(headerModel.getReceivedFromCode()).toString());
                    psTemp.setString("medCode", DmShared.iNull(lineModel.getItemCode()).toString());
                    psTemp.setString("batchNo", DmShared.xNull(lineModel.getBatchNo()));
                    psTemp.setString("colourCode", DmShared.iNull(lineModel.getColourCode()).toString());
                    psTemp.setString("sizeCode", DmShared.iNull(lineModel.getSizeCode()).toString());
                    psTemp.setString("mrp", DmServer.vNull(lineModel.getMrpPack()));
                    psTemp.setString("rate", DmServer.vNull(lineModel.getPackRate()));
                    if (!DmShared.dNull(lineModel.getExpiryDate()).equalsIgnoreCase("")) {
                        psTemp.setDate("expiryDate", DmServer.convertDate(lineModel.getExpiryDate()));
                    }
                    if (!DmShared.dNull(lineModel.getManufacturerDate()).equalsIgnoreCase("")) {
                        psTemp.setDate("manufacturingDate", DmServer.convertDate(lineModel.getManufacturerDate()));
                    }

                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        if (rsTemp.getLong("Cnt") > 0) {
                            status = DmImportStatus.WARNING.getId();
                            handelFileLog.writeLine(lineNoDisplay + " Opening already exists for Department (" + DmShared.xNull(headerModel.getDepartmentName())
                                    + ") , Received From (" + DmShared.xNull(headerModel.getReceivedFromName())
                                    + ") , Item Name " + DmShared.xNull(lineModel.getItemName()));
                            flagContinue = true;
                            break;
                        }
                    }
                    rsTemp.close();
                    psTemp.close();


                    /*==============================<< Insert records into stockHeadDetail & stockHeadBatchDetail >>====================================*/
                    if (DmShared.iNull(lineModel.getItemCode()).longValue() > 0) {
                        lineModel.setSkuRate(DmServer.vNull(DmShared.vNull(lineModel.getPackRate()).doubleValue() / DmShared.vNull(lineModel.getSkuQtyForRate()).doubleValue(), DmShared.getNumberFormat(7)));
                        lineModel.setAmount(DmServer.vNull(DmShared.vNull(lineModel.getQuantity()).doubleValue() * DmShared.vNull(lineModel.getSkuRate()).doubleValue(), "0.00"));
                        lineModel.setSkuMRP(DmServer.vNull(DmShared.vNull(lineModel.getMrpPack()).doubleValue() / DmShared.vNull(lineModel.getSkuQtyForRate()).doubleValue(), DmShared.getNumberFormat(7)));
                        lineModel.setUnitQuantity(DmServer.vNull(DmShared.vNull(lineModel.getQuantity()).doubleValue() / DmShared.vNull(lineModel.getSkuQtyForRate()).doubleValue(), DmShared.getNumberFormat(purchaseUnitDecimalPlaces)));

                        lineModel.setV_SNo(String.valueOf(++v_SNo));
                        sqlQuery = " insert into stockHeadDetail(docId, v_SNo, departmentCode, refNo,refDate,colourCode,sizeCode,"
                                + " itemCode, remark,subCode,site_Code,rate,skuRate,skuValue,skuQtyForRate,"
                                + " batchNo, manufacturingDate,expiryDate, mrp,skuMrp,unitCode,unitQuantity,quantity)"
                                + " values (:docId, :v_SNo, :departmentCode, :refNo, :refDate,:colourCode,:sizeCode,"
                                + " :itemCode, :remark,:subCode,:site_Code,:rate,:skuRate,:skuValue,:skuQtyForRate,"
                                + " :batchNo, :manufacturingDate,:expiryDate, :mrp,:skuMrp,:unitCode,:unitQuantity,:quantity) ";;
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(headerModel.getDocId()).toString());
                        psTemp.setString("v_SNo", DmShared.iNull(lineModel.getV_SNo()).toString());
                        psTemp.setString("departmentCode", DmShared.foriegnKey(headerModel.getDepartmentCode()));
                        psTemp.setString("refNo", DmShared.xNull(lineModel.getRefNo()));
                        psTemp.setDate("refDate", DmServer.convertDate(lineModel.getRefDate()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(lineModel.getItemCode()));
                        psTemp.setString("quantity", DmServer.vNull(lineModel.getQuantity()));
                        psTemp.setString("rate", DmServer.vNull(lineModel.getPackRate()));
                        psTemp.setString("skuRate", DmServer.vNull(lineModel.getSkuRate()));
                        psTemp.setString("skuValue", DmServer.vNull(lineModel.getAmount()));
                        psTemp.setString("skuQtyForRate", DmServer.vNull(lineModel.getSkuQtyForRate()));
                        psTemp.setString("remark", DmShared.xNull(lineModel.getRemark()));
                        psTemp.setString("subCode", DmShared.foriegnKey(headerModel.getReceivedFromCode()));
                        psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                        psTemp.setString("batchNo", DmShared.xNull(lineModel.getBatchNo()));
                        psTemp.setString("colourCode", DmShared.foriegnKey(lineModel.getColourCode()));
                        psTemp.setString("sizeCode", DmShared.foriegnKey(lineModel.getSizeCode()));
                        psTemp.setDate("manufacturingDate", DmServer.convertDate(lineModel.getManufacturerDate()));
                        psTemp.setDate("expiryDate", DmServer.convertDate(lineModel.getExpiryDate()));
                        psTemp.setString("mrp", DmServer.vNull(lineModel.getMrpPack()));
                        psTemp.setString("skuMrp", DmServer.vNull(lineModel.getSkuMRP()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(lineModel.getPurchaseUnitCode()));
                        psTemp.setString("unitQuantity", DmServer.vNull(lineModel.getUnitQuantity()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }

                if (flagContinue) {
                    isAllRecordImported = false;
                    if (spBegin != null) {
                        spBegin.rollBack();
                    }
                    continue;
                } else {
                    /*==============================<< Insert records into stock >>====================================*/
                    ItemBatch_Server.compareProperties(iniItemBatchProperties(headerModel, vType, startDate, recid), cnTemp, rss);

                    /*==============================<< Update ibCode From Stock Table >>====================================*/
                    sqlQuery = "UPDATE stockHeadDetail "
                            + " SET stockHeadDetail.ibCode = stock.ibCode "
                            + " FROM stock "
                            + " WHERE stock.docid = stockHeadDetail.docId "
                            + " AND stock.v_SNo = stockHeadDetail.v_SNo "
                            + " AND stockHeadDetail.docid = :docId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(headerModel.getDocId()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                    /*==============================<< ************************* >>====================================*/
                    spBegin.commit();
                }
            }

            cnTemp.close();
            if (isAllRecordImported) {
                rtnStatus = DmImportStatus.SUCCESS.getId();
            } else {
                rtnStatus = DmImportStatus.WARNING.getId();
            }
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

    private CompareProperties_Mdl iniItemBatchProperties(HeaderModel headerModel, String vType, String date, RecId recId) {
        String stockVoucherDate = "";
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DmShared.convertDate(date));
        calendar.add(Calendar.DATE, -1);
        stockVoucherDate = DmShared.dNull(calendar.getTime());

        lstItemBatch_MdlCurnt.clear();

        for (LineModel lineModel : headerModel.getLineModels()) {

            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(lineModel.getItemCode()).longValue() != 0) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(lineModel.getItemCode()));
                batch_Mdl.getIpm().setBatchNo(DmShared.xNull(lineModel.getBatchNo()));
                batch_Mdl.getIpm().setColourCode(DmShared.xNull(lineModel.getColourCode()));
                batch_Mdl.getIpm().setSizeCode(DmShared.xNull(lineModel.getSizeCode()));
                batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(lineModel.getExpiryDate()));
                batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(lineModel.getManufacturerDate()));
                batch_Mdl.getIpm().setMrp(DmShared.xNull(lineModel.getMrpPack()));
                batch_Mdl.getIpm().setSkuMrp(DmShared.xNull(lineModel.getSkuMRP()));
                batch_Mdl.setDocId(DmShared.xNull(headerModel.getDocId()));
                batch_Mdl.setRecId(DmShared.xNull(recId.getRecIdValue()));
                batch_Mdl.setvType(DmShared.xNull(vType));
                batch_Mdl.setvSno(DmShared.xNull(lineModel.getV_SNo()));

                batch_Mdl.setCode(DmShared.xNull(""));
                batch_Mdl.setReceiveQty(DmShared.xNull(lineModel.getQuantity()));
                batch_Mdl.setReceiveDate(DmShared.dNull(stockVoucherDate));

                batch_Mdl.setStockSkuValue(DmServer.vNull(lineModel.getAmount(), "0.00"));
                batch_Mdl.setStockLandedSkuValue(DmServer.vNull(lineModel.getAmount()));
                batch_Mdl.setStockV_Sno(lineModel.getV_SNo());
                batch_Mdl.setStockRefChallanNo(DmShared.xNull(lineModel.getRefNo()));
                batch_Mdl.setStockRefChallanDate(DmShared.dNull(lineModel.getRefDate()));
                batch_Mdl.setStockRemark(DmShared.dNull(lineModel.getRemark()));

                batch_Mdl.setStockDepartmentCode(DmShared.xNull(headerModel.getDepartmentCode()));

                lstItemBatch_MdlCurnt.add(batch_Mdl);
            }
        }

        if (headerModel.getLineModels() != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(headerModel.getDocId()));
            compareProperties_Mdl.setRecId(DmShared.xNull(recId.getRecIdValue()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(recId.getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(recId.getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(vType));
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(headerModel.getDepartmentCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(stockVoucherDate));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(headerModel.getReceivedFromCode()));
        }
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(rss));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        return compareProperties_Mdl;
    }
}
