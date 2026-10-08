package aarogya.selfToolkit.stImportData.server;

import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
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
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmImportData.server.DmImportData_Interface;
import dataman.dmToolkit.dmImportData.shared.CSVData;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemMaster implements DmImportData_Interface {

    private final String vCat = "'" + new LibraryVoucherCategory().INVENTORY_ITEM_MAST + "'";

    class Model {

        private String code, name, groupName, manufacturer, manufacturerCode, itemSpeciality, itemSpecialityCode, ved, vedCode,
                itemType, categoryName, itemMake, itemMakeCode, division, divisionCode, hsnName, hsnCode,
                consignmentItem, consignmentItemCode, narcotics, narcoticsCode, returnItem, returnItemCode, bestBefore, bestBeforeCode,
                flowThrough, flowThroughCode, billedItem, billedItemCode, lifeSaving, lifeSavingCode, highValue, highValueCode,
                allowNegativeStock, allowNegativeStockCode, active, SKU, purchaseUnit, purchaseUnitCode, saleUnit, saleUnitCode,
                purchaseUnitConversionRate, saleUnitConversionRate, gstNature, applicableProperty, applicablePropertyCode,
                poReq, poReqCode, indentReq, indentReqCode, quotationReq, quotationReqCode;

        public Model() {
        }

        public String getItemType() {
            return itemType;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public String getSKU() {
            return SKU;
        }

        public void setSKU(String SKU) {
            this.SKU = SKU;
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

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getItemSpeciality() {
            return itemSpeciality;
        }

        public void setItemSpeciality(String itemSpeciality) {
            this.itemSpeciality = itemSpeciality;
        }

        public String getVed() {
            return ved;
        }

        public void setVed(String ved) {
            this.ved = ved;
        }

        public String getItemMake() {
            return itemMake;
        }

        public void setItemMake(String itemMake) {
            this.itemMake = itemMake;
        }

        public String getDivision() {
            return division;
        }

        public void setDivision(String division) {
            this.division = division;
        }

        public String getConsignmentItem() {
            return consignmentItem;
        }

        public void setConsignmentItem(String consignmentItem) {
            this.consignmentItem = consignmentItem;
        }

        public String getNarcotics() {
            return narcotics;
        }

        public void setNarcotics(String narcotics) {
            this.narcotics = narcotics;
        }

        public String getReturnItem() {
            return returnItem;
        }

        public void setReturnItem(String returnItem) {
            this.returnItem = returnItem;
        }

        public String getBestBefore() {
            return bestBefore;
        }

        public void setBestBefore(String bestBefore) {
            this.bestBefore = bestBefore;
        }

        public String getFlowThrough() {
            return flowThrough;
        }

        public void setFlowThrough(String flowThrough) {
            this.flowThrough = flowThrough;
        }

        public String getBilledItem() {
            return billedItem;
        }

        public void setBilledItem(String billedItem) {
            this.billedItem = billedItem;
        }

        public String getLifeSaving() {
            return lifeSaving;
        }

        public void setLifeSaving(String lifeSaving) {
            this.lifeSaving = lifeSaving;
        }

        public String getPurchaseUnit() {
            return purchaseUnit;
        }

        public void setPurchaseUnit(String purchaseUnit) {
            this.purchaseUnit = purchaseUnit;
        }

        public String getSaleUnit() {
            return saleUnit;
        }

        public void setSaleUnit(String saleUnit) {
            this.saleUnit = saleUnit;
        }

        public String getPurchaseUnitConversionRate() {
            return purchaseUnitConversionRate;
        }

        public void setPurchaseUnitConversionRate(String purchaseUnitConversionRate) {
            this.purchaseUnitConversionRate = purchaseUnitConversionRate;
        }

        public String getSaleUnitConversionRate() {
            return saleUnitConversionRate;
        }

        public void setSaleUnitConversionRate(String saleUnitConversionRate) {
            this.saleUnitConversionRate = saleUnitConversionRate;
        }

        public String getConsignmentItemCode() {
            return consignmentItemCode;
        }

        public void setConsignmentItemCode(String consignmentItemCode) {
            this.consignmentItemCode = consignmentItemCode;
        }

        public String getNarcoticsCode() {
            return narcoticsCode;
        }

        public void setNarcoticsCode(String narcoticsCode) {
            this.narcoticsCode = narcoticsCode;
        }

        public String getReturnItemCode() {
            return returnItemCode;
        }

        public void setReturnItemCode(String returnItemCode) {
            this.returnItemCode = returnItemCode;
        }

        public String getBestBeforeCode() {
            return bestBeforeCode;
        }

        public void setBestBeforeCode(String bestBeforeCode) {
            this.bestBeforeCode = bestBeforeCode;
        }

        public String getFlowThroughCode() {
            return flowThroughCode;
        }

        public void setFlowThroughCode(String flowThroughCode) {
            this.flowThroughCode = flowThroughCode;
        }

        public String getBilledItemCode() {
            return billedItemCode;
        }

        public void setBilledItemCode(String billedItemCode) {
            this.billedItemCode = billedItemCode;
        }

        public String getLifeSavingCode() {
            return lifeSavingCode;
        }

        public void setLifeSavingCode(String lifeSavingCode) {
            this.lifeSavingCode = lifeSavingCode;
        }

        public String getHighValue() {
            return highValue;
        }

        public void setHighValue(String highValue) {
            this.highValue = highValue;
        }

        public String getHighValueCode() {
            return highValueCode;
        }

        public void setHighValueCode(String highValueCode) {
            this.highValueCode = highValueCode;
        }

        public String getAllowNegativeStock() {
            return allowNegativeStock;
        }

        public void setAllowNegativeStock(String allowNegativeStock) {
            this.allowNegativeStock = allowNegativeStock;
        }

        public String getAllowNegativeStockCode() {
            return allowNegativeStockCode;
        }

        public void setAllowNegativeStockCode(String allowNegativeStockCode) {
            this.allowNegativeStockCode = allowNegativeStockCode;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getManufacturerCode() {
            return manufacturerCode;
        }

        public void setManufacturerCode(String manufacturerCode) {
            this.manufacturerCode = manufacturerCode;
        }

        public String getItemSpecialityCode() {
            return itemSpecialityCode;
        }

        public void setItemSpecialityCode(String itemSpecialityCode) {
            this.itemSpecialityCode = itemSpecialityCode;
        }

        public String getVedCode() {
            return vedCode;
        }

        public void setVedCode(String vedCode) {
            this.vedCode = vedCode;
        }

        public String getItemMakeCode() {
            return itemMakeCode;
        }

        public void setItemMakeCode(String itemMakeCode) {
            this.itemMakeCode = itemMakeCode;
        }

        public String getDivisionCode() {
            return divisionCode;
        }

        public void setDivisionCode(String divisionCode) {
            this.divisionCode = divisionCode;
        }

        public String getPurchaseUnitCode() {
            return purchaseUnitCode;
        }

        public void setPurchaseUnitCode(String purchaseUnitCode) {
            this.purchaseUnitCode = purchaseUnitCode;
        }

        public String getSaleUnitCode() {
            return saleUnitCode;
        }

        public void setSaleUnitCode(String saleUnitCode) {
            this.saleUnitCode = saleUnitCode;
        }

        public String getGstNature() {
            return gstNature;
        }

        public void setGstNature(String gstNature) {
            this.gstNature = gstNature;
        }

        public String getHsnName() {
            return hsnName;
        }

        public void setHsnName(String hsnName) {
            this.hsnName = hsnName;
        }

        public String getApplicablePropertyCode() {
            return applicablePropertyCode;
        }

        public void setApplicablePropertyCode(String applicablePropertyCode) {
            this.applicablePropertyCode = applicablePropertyCode;
        }

        public String getApplicableProperty() {
            return applicableProperty;
        }

        public void setApplicableProperty(String applicableProperty) {
            this.applicableProperty = applicableProperty;
        }

        public String getHsnCode() {
            return hsnCode;
        }

        public void setHsnCode(String hsnCode) {
            this.hsnCode = hsnCode;
        }

        public String getPoReq() {
            return poReq;
        }

        public void setPoReq(String poReq) {
            this.poReq = poReq;
        }

        public String getPoReqCode() {
            return poReqCode;
        }

        public void setPoReqCode(String poReqCode) {
            this.poReqCode = poReqCode;
        }

        public String getIndentReq() {
            return indentReq;
        }

        public void setIndentReq(String indentReq) {
            this.indentReq = indentReq;
        }

        public String getIndentReqCode() {
            return indentReqCode;
        }

        public void setIndentReqCode(String indentReqCode) {
            this.indentReqCode = indentReqCode;
        }

        public String getQuotationReq() {
            return quotationReq;
        }

        public void setQuotationReq(String quotationReq) {
            this.quotationReq = quotationReq;
        }

        public String getQuotationReqCode() {
            return quotationReqCode;
        }

        public void setQuotationReqCode(String quotationReqCode) {
            this.quotationReqCode = quotationReqCode;
        }
    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public ItemMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Bed Sheet");
        node.setInstruction("{unique;mandatory;alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itemType");
        node.setSample("Others");
        node.setInstruction("{mandatory;30c;Chemical/Coal/Finished Mtrl./Fuel/Machine/Others/Overhead Item/Packing Mtrl./Pharmacy/Raw Mtrl./Semi Finished/Store Mtrl.}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itemGroup");
        node.setSample("Chemical");
        node.setInstruction("{mandatory;200c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itemCategory");
        node.setSample("DemoCategory");
        node.setInstruction("{mandatory;alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("hsn");
        node.setSample("hsn");
        node.setInstruction("{alphanumeric;1500c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("manufacturer");
        node.setSample("Patanjali");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itemSpeciality");
        node.setSample("Dental");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itemMake");
        node.setSample("Blanket");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("VED");
        node.setSample("Vital");
        node.setInstruction("{Vital/Essential/Desire;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("Division");
        node.setSample("Third");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("applicableProperty");
        node.setSample("Colour");
        node.setInstruction("{alphanumeric;250c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("allowNegativeStock");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("consignmentItem");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("bestBefore");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("lifesaving");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("narcotics&ControlSubstances");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("flowThrough");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("highValue");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("returnableItem");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("billedItem");
        node.setSample("No");
        node.setInstruction("{Yes/No;alphanumeric;3c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("sku");
        node.setSample("Gram");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseUnit");
        node.setSample("Gram");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseUnitConversionRate");
        node.setSample("120.89");
        node.setInstruction("{mandatory;numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleUnit");
        node.setSample("Gram");
        node.setInstruction("{mandatory;alphanumeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("saleUnitConversionRate");
        node.setSample("670.59");
        node.setInstruction("{mandatory;numeric;10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("poReq");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("indentReq");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("quotationReq");
        node.setSample("No");
        node.setInstruction("mandatory;Yes/No;3c");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("Item Master");
        fileDetail.setServerClassName(ItemMaster.class.getName());

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
        DmImportStatus importStatus = null;
        int rtnStatus = 0, status = 0;
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
                        }// as it is

                        if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemType")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 30, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItemType(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemGroup")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 200, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGroupName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemCategory")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setCategoryName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("hsn")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 1500, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setHsnName(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("manufacturer")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setManufacturer(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemSpeciality")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItemSpeciality(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemMake")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItemMake(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itemSpeciality")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItemSpeciality(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("Division")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setDivision(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("applicableProperty")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 250, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setApplicableProperty(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("allowNegativeStock")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setAllowNegativeStock(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("consignmentItem")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setConsignmentItem(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("bestBefore")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBestBefore(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("lifesaving")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setLifeSaving(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("narcotics&ControlSubstances")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setNarcotics(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("flowThrough")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setFlowThrough(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("highValue")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setHighValue(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("returnableItem")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setReturnItem(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("billedItem")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setBilledItem(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("sku")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSKU(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseUnit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseUnit(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseUnitConversionRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseUnitConversionRate(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleUnit")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleUnit(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("saleUnitConversionRate")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleUnitConversionRate(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("poReq")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPoReq(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("indentReq")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setIndentReq(DmShared.removeSpace(fieldValue));
                            }
                        } else if (headerName.trim().equalsIgnoreCase("quotationReq")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 3, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setQuotationReq(DmShared.removeSpace(fieldValue));
                            }
                        }
                    }
                    if (isFound) {
                        status = saveItemMast(mrTemp);
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

    private int saveItemMast(Model mrTemp) {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, categoryCode = "", groupCode = "", itemTypeCode = "", skuCode = "", manualCode = "",
                vType = "", propertyCode = "", propertyList = "";

        int rtnStatus = 0, status = 0;
        CallableStatement csTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoBigInt(rss, "itemMast", cnTemp));

            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            //CATEGORY CODE  
            categoryCode = DmServer.getCodeViaManualCode(mrTemp.getCategoryName(), "name", "itemCat", "code", cnTemp);

            if (categoryCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getCategoryName() + " category not exists.");
            }
            //GROUP CODE 
            groupCode = DmServer.getCodeViaManualCode(mrTemp.getGroupName(), "name", "itemGroup", "code", cnTemp);

            if (groupCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getGroupName() + " group not exists.");
            }
            //ItemType Code
            itemTypeCode = DmServer.getCodeViaManualCode(mrTemp.getItemType().trim(), "name", "itemType", "code", cnTemp);
            if (itemTypeCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getItemType() + " not Exists");
            }

            //SKU Code
            skuCode = DmServer.getCodeViaManualCode(mrTemp.getSKU(), "unit", "unitMast", "code", cnTemp);
            if (skuCode.equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, mrTemp.getSKU() + " not Exists");
            }
            //ITem Speciality Code
            if (!DmShared.xNull(mrTemp.getItemSpeciality()).equalsIgnoreCase("")) {
                mrTemp.setItemSpecialityCode(DmServer.getCodeViaManualCode(mrTemp.getItemSpeciality(), "name", "itemSpecialityMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getItemSpecialityCode()).intValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItemSpeciality() + " not inserted. Item Speciality not exists.");
                }
            }
            //Manufacturer Code
            if (!DmShared.xNull(mrTemp.getManufacturer()).equalsIgnoreCase("")) {
                mrTemp.setManufacturerCode(DmServer.getCodeViaManualCode(mrTemp.getManufacturer(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getManufacturerCode()).longValue() == 0) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getManufacturer() + " not inserted. Manufacturer not exists.");
                }
            }
            //Division Code
            if (!DmShared.xNull(mrTemp.getDivision()).equalsIgnoreCase("")) {
                mrTemp.setDivisionCode(DmServer.getCodeViaManualCode(mrTemp.getDivision(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getDivisionCode()).longValue() == 0) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getDivision() + " not inserted. Divison not exists.");
                }
            }
            //Manufacturer Code
            if (!DmShared.xNull(mrTemp.getManufacturer()).equalsIgnoreCase("")) {
                mrTemp.setManufacturerCode(DmServer.getCodeViaManualCode(mrTemp.getManufacturer(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getManufacturerCode()).longValue() == 0) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getManufacturer() + " not inserted. Manufacturer not exists.");
                }
            }

            //Purchase Unit Code
            if (!DmShared.xNull(mrTemp.getPurchaseUnit()).equalsIgnoreCase("")) {
                mrTemp.setPurchaseUnitCode(DmServer.getCodeViaManualCode(mrTemp.getPurchaseUnit(), "unit", "unitMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getPurchaseUnitCode()).longValue() == 0) {;
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getPurchaseUnit() + " not Exists");
                }
            }
            //sale Unit Code
            if (!DmShared.xNull(mrTemp.getSaleUnit()).equalsIgnoreCase("")) {
                mrTemp.setSaleUnitCode(DmServer.getCodeViaManualCode(mrTemp.getSaleUnit(), "unit", "unitMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getSaleUnitCode()).longValue() == 0) {;
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getSaleUnit() + " not Exists");
                }
            }
            //VED Code
            if (!DmShared.xNull(mrTemp.getVed()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getVed()).trim().equalsIgnoreCase("Vital")) {
                    mrTemp.setVedCode("Vital");
                } else if (DmShared.xNull(mrTemp.getVed()).trim().equalsIgnoreCase("Essential")) {
                    mrTemp.setVedCode("Essential");
                } else if (DmShared.xNull(mrTemp.getVed()).trim().equalsIgnoreCase("Desire")) {
                    mrTemp.setVedCode("Desire");
                }
                if (DmShared.xNull(mrTemp.getVedCode()).equalsIgnoreCase("")) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getVed() + " not inserted. VED not exists.");
                }
            }
            //Item Make Code
            if (!DmShared.xNull(mrTemp.getItemMake()).equalsIgnoreCase("")) {
                mrTemp.setItemMakeCode(DmServer.getCodeViaManualCode(mrTemp.getItemMake(), "companyName", "itemMakeMast", "code", cnTemp));
                if (DmShared.iNull(mrTemp.getItemMakeCode()).intValue() == 0) {;
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItemMake() + " not inserted. Item Make not exists in Item Make Mast.");
                }
            }
            if (!DmShared.xNull(mrTemp.getHsnName()).equalsIgnoreCase("")) {
                sqlQuery = "select code,itemGSTNature  "
                        + " from hsnMast hsn "
                        + " where name = :name ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getHsnName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setHsnCode(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getHsnCode()).intValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getHsnName() + " not inserted. HSN not exists.");
                }
            }
            //---------------------------------------------------------------
            sqlQuery = "SELECT COUNT(im.name) AS cnt FROM itemMast im WHERE im.name = :name AND im.code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(mrTemp.getName() + "Name already exists.");
            }
            rsTemp.close();
            psTemp.close();
            //---------------------------------------------------------------
            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = " + vCat + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            // ******************************************* END ****************************************************
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getConsignmentItem()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setConsignmentItemCode("1");
            } else if (DmShared.xNull(mrTemp.getConsignmentItem()).trim().equalsIgnoreCase("No")) {
                mrTemp.setConsignmentItemCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getBestBefore()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setBestBeforeCode("1");
            } else if (DmShared.xNull(mrTemp.getBestBefore()).trim().equalsIgnoreCase("No")) {
                mrTemp.setBestBeforeCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getLifeSaving()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setLifeSavingCode("1");
            } else if (DmShared.xNull(mrTemp.getLifeSaving()).trim().equalsIgnoreCase("No")) {
                mrTemp.setLifeSavingCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getNarcotics()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setNarcoticsCode("1");
            } else if (DmShared.xNull(mrTemp.getNarcotics()).trim().equalsIgnoreCase("No")) {
                mrTemp.setNarcoticsCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getFlowThrough()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setFlowThroughCode("1");
            } else if (DmShared.xNull(mrTemp.getFlowThrough()).trim().equalsIgnoreCase("No")) {
                mrTemp.setFlowThroughCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getHighValue()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setHighValueCode("1");
            } else if (DmShared.xNull(mrTemp.getHighValue()).trim().equalsIgnoreCase("No")) {
                mrTemp.setHighValueCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getReturnItem()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setReturnItemCode("1");
            } else if (DmShared.xNull(mrTemp.getReturnItem()).trim().equalsIgnoreCase("No")) {
                mrTemp.setReturnItemCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getBilledItem()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setBilledItemCode("1");
            } else if (DmShared.xNull(mrTemp.getBilledItem()).trim().equalsIgnoreCase("No")) {
                mrTemp.setBilledItemCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getAllowNegativeStock()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setAllowNegativeStockCode("1");
            } else if (DmShared.xNull(mrTemp.getAllowNegativeStock()).trim().equalsIgnoreCase("No")) {
                mrTemp.setAllowNegativeStockCode("0");
            }
            //---------------------------------------------------------------
            if (DmShared.xNull(mrTemp.getPoReq()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setPoReqCode("1");
            } else if (DmShared.xNull(mrTemp.getPoReq()).trim().equalsIgnoreCase("No")) {
                mrTemp.setPoReqCode("0");
            }
            if (DmShared.xNull(mrTemp.getIndentReq()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setIndentReqCode("1");
            } else if (DmShared.xNull(mrTemp.getIndentReq()).trim().equalsIgnoreCase("No")) {
                mrTemp.setIndentReqCode("0");
            }
            if (DmShared.xNull(mrTemp.getQuotationReq()).trim().equalsIgnoreCase("Yes")) {
                mrTemp.setQuotationReqCode("1");
            } else if (DmShared.xNull(mrTemp.getQuotationReq()).trim().equalsIgnoreCase("No")) {
                mrTemp.setQuotationReqCode("0");
            }
            //---------------------------------------------------------------

            if (!DmShared.xNull(mrTemp.getApplicableProperty()).equalsIgnoreCase("")) {
                String[] propertyArr;
                String property;
                property = mrTemp.getApplicableProperty();
                propertyArr = property.split(",");
                for (String propertyName : propertyArr) {
                    propertyCode = DmServer.getCodeViaManualCode(propertyName, "name", "itemProperty", "manualCode", cnTemp);
                    if (DmShared.xNull(propertyCode).equalsIgnoreCase("")) {
                        propertyList += ",";
                    }
                    propertyCode += ",";
                }

                if (DmShared.xNull(propertyList).equalsIgnoreCase("")) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + propertyList + " not inserted. Item Property not exists .");
                }
            }
            // ************************************** GET MANUAL CODE *********************************************
            HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
            fieldUDTHashMap.put("itemName", new FieldUDT_Mdl("itemName", DmShared.xNull(mrTemp.getName()), DmShared.xNull(mrTemp.getName()).trim()));
            fieldUDTHashMap.put("itemGroup", new FieldUDT_Mdl("itemGroup", DmShared.xNull(mrTemp.getGroupName()).toString(), DmShared.xNull(mrTemp.getGroupName()).trim()));
            fieldUDTHashMap.put("itemCat", new FieldUDT_Mdl("itemCat", DmShared.xNull(mrTemp.getCategoryName()).toString(), DmShared.xNull(mrTemp.getCategoryName()).trim()));
            csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getItemManualCode(?, ?)}");
            if (csTemp instanceof SQLServerCallableStatement) {
                ((SQLServerCallableStatement) csTemp).setString(1, DmShared.foriegnKey(vType));
                ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
            }
            rsTemp = csTemp.executeQuery();
            if (rsTemp.next()) {
                manualCode = DmShared.xNull(rsTemp.getString(1)).toString();
            }
            rsTemp.close();
            csTemp.close();
            // ******************************************* END ***********************************************

            sqlQuery = "INSERT INTO itemmast (CODE, NAME,displayName, itemGroup,itemType, filterItemType, sku, "
                    + " preparedBY, u_EntDt, transferDt, isActive,  itemCategoryCode,"
                    + " manualCode,isBestBefore,isLifeSaving,isHighValue,isFlowThrough,  "
                    + " isNarcotics,isConsignment,isReturnableItem,isBilledItem,itemSpecialityCode,manufacturerCode,"
                    + " divisionCode,vedCode,itemMakeCode,propertyList,itemGSTNature,purchaseUnit,saleUnit,"
                    + " purchaseUnitConversionRate,saleUnitConversionRate,allowNegativeStock,hsnCode,"
                    + " isPOMandatory, isIndentMandatory, isQuotationMandatory)  "
                    + " VALUES (:code, :name,:displayName, :itemGroup, :itemType, :filterItemType, :sku, "
                    + "    :preparedBY, :u_EntDt, NULL,  :isActive,  :itemCategoryCode,"
                    + "    :manualCode,:isBestBefore,:isLifeSaving,:isHighValue,:isFlowThrough, "
                    + "    :isNarcotics,:isConsignment,:isReturnableItem,:isBilledItem,:itemSpecialityCode,:manufacturerCode,"
                    + "    :divisionCode,:vedCode,:itemMakeCode,:propertyList,:itemGSTNature,:purchaseUnit,:saleUnit,"
                    + "    :purchaseUnitConversionRate,:saleUnitConversionRate,:allowNegativeStock,:hsnCode, "
                    + "    :isPOMandatory, :isIndentMandatory, :isQuotationMandatory) ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()).trim());
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("itemGroup", DmShared.foriegnKey(groupCode));
            psTemp.setString("manualCode", manualCode);
            psTemp.setString("itemType", DmShared.foriegnKey(itemTypeCode));
            psTemp.setString("filterItemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("sku", DmShared.foriegnKey(skuCode));
            psTemp.setString("isActive", DmShared.xNull("1"));
            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(categoryCode));
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rss));
            psTemp.setString("isBestBefore", DmServer.vNull(mrTemp.getBestBeforeCode(), "0"));
            psTemp.setString("isLifeSaving", DmServer.vNull(mrTemp.getLifeSavingCode(), "0"));
            psTemp.setString("isHighValue", DmServer.vNull(mrTemp.getHighValueCode(), "0"));
            psTemp.setString("isFlowThrough", DmServer.vNull(mrTemp.getFlowThroughCode(), "0"));
            psTemp.setString("isNarcotics", DmServer.vNull(mrTemp.getNarcoticsCode(), "0"));
            psTemp.setString("isConsignment", DmServer.vNull(mrTemp.getConsignmentItemCode(), "0"));
            psTemp.setString("isReturnableItem", DmServer.vNull(mrTemp.getReturnItemCode(), "0"));
            psTemp.setString("isBilledItem", DmServer.vNull(mrTemp.getBilledItemCode(), "0"));
            psTemp.setString("itemSpecialityCode", DmShared.foriegnKey(mrTemp.getItemSpecialityCode()));
            psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerCode()));
            psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionCode()));
            psTemp.setString("vedCode", DmShared.xNull(mrTemp.getVedCode()));
            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getItemMakeCode()));
            psTemp.setString("propertyList", DmShared.xNull(propertyCode));
            psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNature()));
            psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitCode()));
            psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitCode()));
            psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
            psTemp.setString("isPOMandatory", DmServer.vNull(mrTemp.getPoReqCode(), "0"));
            psTemp.setString("isIndentMandatory", DmServer.vNull(mrTemp.getIndentReqCode(), "0"));
            psTemp.setString("isQuotationMandatory", DmServer.vNull(mrTemp.getQuotationReqCode(), "0"));
            if ((DmShared.iNull(skuCode).intValue() == DmShared.iNull(mrTemp.getPurchaseUnitCode()).intValue())
                    && (DmShared.iNull(skuCode).intValue() == DmShared.iNull(mrTemp.getSaleUnitCode()).intValue())) {
                mrTemp.setPurchaseUnitConversionRate("1");
                mrTemp.setSaleUnitConversionRate("1");
            } else if (DmShared.iNull(skuCode).intValue() == DmShared.iNull(mrTemp.getPurchaseUnitCode()).intValue()) {
                mrTemp.setPurchaseUnitConversionRate("1");
            } else if (DmShared.iNull(skuCode).intValue() == DmShared.iNull(mrTemp.getSaleUnitCode()).intValue()) {
                mrTemp.setSaleUnitConversionRate("1");
            } else if (DmShared.iNull(mrTemp.getPurchaseUnitCode()).intValue() == DmShared.iNull(mrTemp.getSaleUnitCode()).intValue()) {
                mrTemp.setPurchaseUnitConversionRate("1");
                mrTemp.setSaleUnitConversionRate("1");
            }
            psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getPurchaseUnitConversionRate()));
            psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getSaleUnitConversionRate()));
            psTemp.setString("allowNegativeStock", DmServer.vNull(mrTemp.getAllowNegativeStockCode(), "0"));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Insert record in itemSiteList >===============================*/
            sqlQuery = "INSERT INTO itemSiteList (CODE, site_Code)  "
                    + " VALUES (:code, :site_Code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rss)));
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of itemMast >===============================*/
            if (DmShared.iNull(mrTemp.getHsnCode()).intValue() > 0) {
                sqlQuery = " Update  im set "
                        + "         im.itemGSTNature = hm.itemGSTNature,"
                        + "         im.itsBasedOn = hm.itsBasedOn"
                        + "         from itemmast as im "
                        + "         left join hsnMast as hm on im.hsnCode = hm.code "
                        + "         where im.code = :code  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getCode());
                psTemp.executeUpdate();
                psTemp.close();

            }

            /*===========================< Update record of itemITS >===============================*/
            sqlQuery = " delete from itemITSDetail where code = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            if (DmShared.iNull(mrTemp.getHsnCode()).intValue() > 0) {;
                sqlQuery = "insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                        + "                  its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) "
                        + "         		(select :code ,hid.v_SNo,hid.rateFrom,hid.rateUpto,hid.its_Code_Purchase,hid.its_Code_PurchaseRet,"
                        + "                 hid.its_Code_Sales,hid.its_Code_SalesRet,hid.purchaseAc,hid.purchaseReturnAc,hid.salesAc,hid.salesReturnAc "
                        + "         		from hsnITSDetail as hid where hid.code = :hsnCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getCode());
                psTemp.setString("hsnCode", mrTemp.getHsnCode());
                psTemp.executeUpdate();
                psTemp.close();
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
