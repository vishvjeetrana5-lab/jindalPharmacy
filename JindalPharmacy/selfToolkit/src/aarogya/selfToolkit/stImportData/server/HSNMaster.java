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
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HSNMaster implements DmImportData_Interface {

    class Model {

        private String code, name, hsn, goodServiceNature, gstNature, itsOnPurchaseCode, itsOnPurchaseName, itsOnSaleCode, itsOnSaleName,
                itsOnPurchaseReturnCode, itsOnPurchaseReturnName, itsOnSaleReturnCode, itsOnSaleReturnName, purchaseAcCode, purchaseAcName, saleAcCode, saleAcName,
                purchaseReturnCode, purchaseReturnName, salesReturnAcCode, salesReturnAcName, vSno;

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

        public String getHsn() {
            return hsn;
        }

        public void setHsn(String hsn) {
            this.hsn = hsn;
        }

        public String getGoodServiceNature() {
            return goodServiceNature;
        }

        public void setGoodServiceNature(String goodServiceNature) {
            this.goodServiceNature = goodServiceNature;
        }

        public String getGstNature() {
            return gstNature;
        }

        public void setGstNature(String gstNature) {
            this.gstNature = gstNature;
        }

        public String getItsOnPurchaseCode() {
            return itsOnPurchaseCode;
        }

        public void setItsOnPurchaseCode(String itsOnPurchaseCode) {
            this.itsOnPurchaseCode = itsOnPurchaseCode;
        }

        public String getItsOnPurchaseName() {
            return itsOnPurchaseName;
        }

        public void setItsOnPurchaseName(String itsOnPurchaseName) {
            this.itsOnPurchaseName = itsOnPurchaseName;
        }

        public String getItsOnSaleCode() {
            return itsOnSaleCode;
        }

        public void setItsOnSaleCode(String itsOnSaleCode) {
            this.itsOnSaleCode = itsOnSaleCode;
        }

        public String getItsOnSaleName() {
            return itsOnSaleName;
        }

        public void setItsOnSaleName(String itsOnSaleName) {
            this.itsOnSaleName = itsOnSaleName;
        }

        public String getItsOnPurchaseReturnCode() {
            return itsOnPurchaseReturnCode;
        }

        public void setItsOnPurchaseReturnCode(String itsOnPurchaseReturnCode) {
            this.itsOnPurchaseReturnCode = itsOnPurchaseReturnCode;
        }

        public String getItsOnPurchaseReturnName() {
            return itsOnPurchaseReturnName;
        }

        public void setItsOnPurchaseReturnName(String itsOnPurchaseReturnName) {
            this.itsOnPurchaseReturnName = itsOnPurchaseReturnName;
        }

        public String getItsOnSaleReturnCode() {
            return itsOnSaleReturnCode;
        }

        public void setItsOnSaleReturnCode(String itsOnSaleReturnCode) {
            this.itsOnSaleReturnCode = itsOnSaleReturnCode;
        }

        public String getItsOnSaleReturnName() {
            return itsOnSaleReturnName;
        }

        public void setItsOnSaleReturnName(String itsOnSaleReturnName) {
            this.itsOnSaleReturnName = itsOnSaleReturnName;
        }

        public String getPurchaseAcCode() {
            return purchaseAcCode;
        }

        public void setPurchaseAcCode(String purchaseAcCode) {
            this.purchaseAcCode = purchaseAcCode;
        }

        public String getPurchaseAcName() {
            return purchaseAcName;
        }

        public void setPurchaseAcName(String purchaseAcName) {
            this.purchaseAcName = purchaseAcName;
        }

        public String getSaleAcCode() {
            return saleAcCode;
        }

        public void setSaleAcCode(String saleAcCode) {
            this.saleAcCode = saleAcCode;
        }

        public String getSaleAcName() {
            return saleAcName;
        }

        public void setSaleAcName(String saleAcName) {
            this.saleAcName = saleAcName;
        }

        public String getPurchaseReturnCode() {
            return purchaseReturnCode;
        }

        public void setPurchaseReturnCode(String purchaseReturnCode) {
            this.purchaseReturnCode = purchaseReturnCode;
        }

        public String getPurchaseReturnName() {
            return purchaseReturnName;
        }

        public void setPurchaseReturnName(String purchaseReturnName) {
            this.purchaseReturnName = purchaseReturnName;
        }

        public String getSalesReturnAcCode() {
            return salesReturnAcCode;
        }

        public void setSalesReturnAcCode(String salesReturnAcCode) {
            this.salesReturnAcCode = salesReturnAcCode;
        }

        public String getSalesReturnAcName() {
            return salesReturnAcName;
        }

        public void setSalesReturnAcName(String salesReturnAcName) {
            this.salesReturnAcName = salesReturnAcName;
        }

        public String getvSno() {
            return vSno;
        }

        public void setvSno(String vSno) {
            this.vSno = vSno;
        }

    }

    private DmRSS rss;
    private DmHandelFile handelFileLog;
    private String lineNoDisplay = "";

    public HSNMaster() {
    }

    @Override
    public ImportData_Mdl getImportDataModel() {
        ImportData_Mdl fileDetail = new ImportData_Mdl();
        CSVData node;
        List<CSVData> lstCSV = new ArrayList<CSVData>();

        node = new CSVData();
        node.setHeader("name");
        node.setSample("Goods & Service Tax");
        node.setInstruction("{unique;mandatory;alphanumeric;1500c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("HSN/SAC");
        node.setSample("12345678");
        node.setInstruction("{mandatory;numeric;8}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("goodsServiceNature");
        node.setSample("Goods");
        node.setInstruction("{mandatory;Goods/Service;7c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("gstNature");
        node.setSample("GST");
        node.setInstruction("{mandatory;(GST/Non GST/Exempted/Nill Rated);10c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itsOnPurchase");
        node.setSample("GST Purchase");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itsOnSale");
        node.setSample("GST Sales");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itsOnPurchaseReturn");
        node.setSample("GST Purchase");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("itsOnSaleReturn");
        node.setSample("GST Sales");
        node.setInstruction("{alphanumeric;50c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseAc");
        node.setSample("Computer Expenses");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("salesAc");
        node.setSample("Computer Expenses");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("purchaseReturnAc");
        node.setSample("Computer Expenses");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        node = new CSVData();
        node.setHeader("salesReturneAc");
        node.setSample("Computer Expenses");
        node.setInstruction("{alphanumeric;100c}");
        lstCSV.add(node);

        fileDetail.getHmCSV().addAll(lstCSV);
        fileDetail.setImportTo("HSN Master");
        fileDetail.setServerClassName(HSNMaster.class.getName());

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
        int rtnStatus = 0, status = 0;
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
                        if (headerName.trim().equalsIgnoreCase("name")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 1500, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("HSN/SAC")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, true, false, false, false, false, 8, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setHsn(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("goodsServiceNature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 7, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGoodServiceNature(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("gstNature")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, true, false, false, false, false, false, 10, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setGstNature(fieldValue);
                            }

                        } else if (headerName.trim().equalsIgnoreCase("itsOnPurchase")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItsOnPurchaseName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itsOnSale")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItsOnSaleName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itsOnPurchaseReturn")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItsOnPurchaseReturnName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("itsOnSaleReturn")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 50, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setItsOnSaleReturnName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseAc")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseAcName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("salesAc")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSaleAcName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("purchaseReturnAc")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setPurchaseReturnName(fieldValue);
                            }
                        } else if (headerName.trim().equalsIgnoreCase("salesReturneAc")) {
                            if (!ImportData_Server.validateField(headerName, fieldValue, false, false, false, false, false, false, 100, lineNoDisplay, handelFileLog)) {
                                status = DmImportStatus.ERROR.getId();
                                isFound = false;
                            } else {
                                mrTemp.setSalesReturnAcName(fieldValue);
                            }
                        }
                    }
                    if (isFound) {
                        status = saveHSNMast(mrTemp);
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

    private int saveHSNMast(Model mrTemp) throws Exception {
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, goodsServiceNatureCode = "", gstNatureCode = "";
        int rtnStatus = 0, status = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rss));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setName(DmShared.removeSpace(mrTemp.getName()));
            mrTemp.setCode(DmServer.getSerialNoInteger(rss, "hsnMast", cnTemp));
            if (DmShared.iNull(mrTemp.getCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            //GET GOODS SERVICE NATURE Code
            if (!DmShared.xNull(mrTemp.getGoodServiceNature()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getGoodServiceNature()).trim().equalsIgnoreCase("Goods")) {
                    goodsServiceNatureCode = "1";
                } else if (DmShared.xNull(mrTemp.getGoodServiceNature()).trim().equalsIgnoreCase("Service")) {
                    goodsServiceNatureCode = "0";
                }
                if (DmShared.xNull(goodsServiceNatureCode).equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getGoodServiceNature() + " not Exists.Only (Goods/Service) Allow.");
                }
            }

            //GET GST NATURE Code
            if (!DmShared.xNull(mrTemp.getGstNature()).equalsIgnoreCase("")) {
                if (DmShared.xNull(mrTemp.getGstNature()).trim().equalsIgnoreCase(DmEnum.GSTNature.GST_ITEM.getValue())) {
                    gstNatureCode = DmEnum.GSTNature.GST_ITEM.getId();
                } else if (DmShared.xNull(mrTemp.getGstNature()).trim().equalsIgnoreCase(DmEnum.GSTNature.NON_GST_ITEM.getValue())) {
                    gstNatureCode = DmEnum.GSTNature.NON_GST_ITEM.getId();
                } else if (DmShared.xNull(mrTemp.getGstNature()).trim().equalsIgnoreCase(DmEnum.GSTNature.EXEMPTED_ITEM.getValue())) {
                    gstNatureCode = DmEnum.GSTNature.EXEMPTED_ITEM.getId();
                } else if (DmShared.xNull(mrTemp.getGstNature()).trim().equalsIgnoreCase(DmEnum.GSTNature.NILL_RATED.getValue())) {
                    gstNatureCode = DmEnum.GSTNature.NILL_RATED.getId();
                }
                if (DmShared.xNull(gstNatureCode).equalsIgnoreCase("")) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, mrTemp.getGstNature() + " not Allow.Only (GST/Non GST/Exempted/Nill Rated) Allow.");
                }
            }
            //================================< GET ITS ON PURCHASE  CODE >====================================================
            if (!DmShared.xNull(mrTemp.getItsOnPurchaseName()).trim().equals("")) {
                mrTemp.setItsOnPurchaseCode(DmServer.getCodeViaManualCode(mrTemp.getItsOnPurchaseName(), "name", "its", "Code", cnTemp));
                if (DmShared.iNull(mrTemp.getItsOnPurchaseCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItsOnPurchaseName() + " not inserted. ITS not exists in Item Tax Structure.");
                }
            }
            //=========================================< END >=============================================================

            //=================================< GET ITS ON SALES CODE >======================================================
            if (!DmShared.xNull(mrTemp.getItsOnSaleName()).trim().equals("")) {
                mrTemp.setItsOnSaleCode(DmServer.getCodeViaManualCode(mrTemp.getItsOnSaleName(), "name", "its", "Code", cnTemp));
                if (DmShared.iNull(mrTemp.getItsOnSaleCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItsOnSaleName() + " not inserted.ITS not exists in Item Tax Structure.");
                }
            }
            //=========================================< END >=============================================================
            //================================< GET ITS ON PURCHASE RETURN  CODE >====================================================
            if (!DmShared.xNull(mrTemp.getItsOnPurchaseReturnName()).trim().equals("")) {
                mrTemp.setItsOnPurchaseReturnCode(DmServer.getCodeViaManualCode(mrTemp.getItsOnPurchaseReturnName(), "name", "its", "Code", cnTemp));
                if (DmShared.iNull(mrTemp.getItsOnPurchaseReturnCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItsOnPurchaseReturnName() + " not inserted. ITS not exists in Item Tax Structure.");
                }
            }
            //=========================================< END >=============================================================

            //=================================< GET ITS ON SALES RETURN CODE >======================================================
            if (!DmShared.xNull(mrTemp.getItsOnSaleReturnName()).trim().equals("")) {
                mrTemp.setItsOnSaleReturnCode(DmServer.getCodeViaManualCode(mrTemp.getItsOnSaleReturnName(), "name", "its", "Code", cnTemp));
                if (DmShared.iNull(mrTemp.getItsOnSaleReturnCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getItsOnSaleReturnName() + " not inserted.ITS not exists in Item Tax Structure.");
                }
            }
            //=========================================< END >=============================================================
            //================================< GET PURCHASE A/C CODE >====================================================
            if (!DmShared.xNull(mrTemp.getPurchaseAcName()).trim().equals("")) {
                mrTemp.setPurchaseAcCode(DmServer.getCodeViaManualCode(mrTemp.getPurchaseAcName(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getPurchaseAcCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getPurchaseAcName() + " not inserted. A/c not exists in Account Master.");
                }
            }
            //=========================================< END >=============================================================

            //=================================< GET SALES A/C CODE >======================================================
            if (!DmShared.xNull(mrTemp.getSaleAcName()).trim().equals("")) {
                mrTemp.setSaleAcCode(DmServer.getCodeViaManualCode(mrTemp.getSaleAcName(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getSaleAcCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getSaleAcName() + " not inserted. A/c not exists in Account Master.");
                }
            }
            //=========================================< END >=============================================================
            //================================< GET PURCHASE Return A/C CODE >====================================================
            if (!DmShared.xNull(mrTemp.getPurchaseReturnName()).trim().equals("")) {
                mrTemp.setPurchaseReturnCode(DmServer.getCodeViaManualCode(mrTemp.getPurchaseReturnName(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getPurchaseReturnCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getPurchaseReturnName() + " not inserted. A/c not exists in Account Master.");
                }
            }
            //=========================================< END >=============================================================

            //=================================< GET SALES Return A/C CODE >======================================================
            if (!DmShared.xNull(mrTemp.getSalesReturnAcName()).trim().equals("")) {
                mrTemp.setSalesReturnAcCode(DmServer.getCodeViaManualCode(mrTemp.getSalesReturnAcName(), "name", "subGroup", "subCode", cnTemp));
                if (DmShared.iNull(mrTemp.getSalesReturnAcCode()).longValue() == 0) {
                    status = DmImportStatus.WARNING.getId();
                    handelFileLog.writeLine(lineNoDisplay + mrTemp.getSalesReturnAcName() + " not inserted. A/c not exists in Account Master.");
                }
            }
            //=========================================< END >=============================================================

            //===========================< CHECK WHETHER NAME EXISTS OR NOT >===============================================
            sqlQuery = "SELECT COUNT(name) AS cnt FROM hsnMast WHERE name = :name AND code <> :code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()));
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException("Name already exists.");

            }
            rsTemp.close();
            psTemp.close();
            //=========================================< END >===============================================================

            //===========================< INSERT INTO HSN MAST TABLE >======================================================
            sqlQuery = "Insert Into hsnMast(code ,name,hsn,itsBasedOn,itemGSTNature, "
                    + "preparedBy,u_EntDt,transferDt,nature) Values  "
                    + "(:code ,:name,:hsn,:itsBasedOn,:itemGSTNature,:u_Name,:u_EntDt,NULL,:nature) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getName()).trim());
            psTemp.setString("hsn", DmShared.iNull(mrTemp.getHsn()).toString());
            psTemp.setString("u_Name", DmServer.getSsnUserName(rss));
            psTemp.setString("itsBasedOn", DmShared.xNull("0"));
            psTemp.setString("itemGSTNature", DmShared.xNull(gstNatureCode));
            psTemp.setString("nature", DmShared.xNull(goodsServiceNatureCode));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();
//            //=========================================< END >===============================================================
            sqlQuery = "Delete From hsnITSDetail Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getCode()).toString());
            psTemp.executeUpdate();
//
            int vSNo = 0;

            mrTemp.setvSno(String.valueOf(++vSNo));
            sqlQuery = "Insert Into hsnITSDetail(code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                    + " its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) Values  "
                    + "(:code ,:v_SNo,:rateFrom,:rateUpto,:its_Code_Purchase,:its_Code_PurchaseRet,"
                    + " :its_Code_Sales,:its_Code_SalesRet,:purchaseAc,:purchaseReturnAc,:salesAc,:salesReturnAc) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.foriegnKey(mrTemp.getCode()));
            psTemp.setString("v_SNo", DmShared.iNull(mrTemp.getvSno()).toString());
            psTemp.setString("rateFrom", "0.00");
            psTemp.setString("rateUpto", "0.00");
            psTemp.setString("its_Code_Purchase", DmShared.foriegnKey(mrTemp.getItsOnPurchaseCode()));
            psTemp.setString("its_Code_PurchaseRet", DmShared.foriegnKey(mrTemp.getItsOnPurchaseReturnCode()));
            psTemp.setString("its_Code_Sales", DmShared.foriegnKey(mrTemp.getItsOnSaleCode()));
            psTemp.setString("its_Code_SalesRet", DmShared.foriegnKey(mrTemp.getItsOnSaleReturnCode()));
            psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcCode()));
            psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnCode()));
            psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSaleAcCode()));
            psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of itemMast >===============================*/
            sqlQuery = "Update  itemmast  set "
                    + "         itemGSTNature = :itemGSTNature"
                    + "         where hsnCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getCode()));
            psTemp.setString("itemGSTNature", DmShared.xNull(gstNatureCode));
            psTemp.executeUpdate();
            psTemp.close();
            /*===========================<  >===============================*/
            sqlQuery = "DELETE itemITSDetail "
                    + " FROM itemMast im "
                    + " INNER JOIN itemItsDetail iid ON iid.code = im.code "
                    + " WHERE im.hsnCode = :hsnCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("hsnCode", mrTemp.getCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "INSERT INTO itemITSDetail (code,v_SNo,rateFrom ,rateUpto,its_Code_Purchase,its_Code_PurchaseRet ,its_Code_Sales "
                    + ",its_Code_SalesRet ,purchaseAc ,purchaseReturnAc,salesAc,salesReturnAc) "
                    + "  (   "
                    + "      SELECT im.code , hid.v_SNo, hid.rateFrom, hid.rateUpto, hid.its_Code_Purchase , hid.its_Code_PurchaseRet  , hid.its_Code_Sales"
                    + "      , hid.its_Code_SalesRet , hid.purchaseAc, hid.purchaseReturnAc, hid.salesAc, hid.salesReturnAc "
                    + "      FROM itemMast im "
                    + "      INNER JOIN hsnMast hm ON hm.code = im.hsnCode "
                    + "      LEFT JOIN hsnITSDetail hid ON hid.code = hm.code "
                    + "      WHERE im.hsnCode = :hsnCode "
                    + " ) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("hsnCode", mrTemp.getCode());
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
