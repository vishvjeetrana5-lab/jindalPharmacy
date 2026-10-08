package aarogya.selfToolkit.itemBatch.server;

import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum.StockEntryType;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ItemBatch_Server {

    public static void compareProperties(CompareProperties_Mdl compareProperties_Mdl, Connection cnTemp, String siteCode, String v_Prefix) throws DmException {
        double diffQty;
        List<ItemBatch_Mdl> ibmList = new ArrayList<>();
        String currentReceiveDate, previousReceiveDate;
        String sqlPreviousItems = "", sqlCurrentItems = "";
        String sqlPreviousItemBatch = "", sqlCurrentItemBatch = "";
        boolean flagManufacturingDate = false, flagExpiryDate = false, flagCompare = false, flagNonBatch = false;
        try {
            currentReceiveDate = DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate());
            previousReceiveDate = DmShared.dNull(compareProperties_Mdl.getPreviousReceiveDate());

            sqlCurrentItems = "";
            sqlCurrentItemBatch = "";
            for (ItemBatch_Mdl cibm : compareProperties_Mdl.getCurrentIbm()) {
                if (!DmShared.xNull(cibm.getIpm().getItemCode()).trim().equals("")) {
                    flagCompare = false;
                    flagExpiryDate = false;
                    flagManufacturingDate = false;
                    flagNonBatch = false;

                    if (!DmShared.xNull(previousReceiveDate).trim().equalsIgnoreCase("")) {
                        if (DmShared.vNull(cibm.getReceiveQty()).doubleValue() > 0) {
                            if (!DmShared.xNull(sqlCurrentItems).trim().equalsIgnoreCase("")) {
                                sqlCurrentItems += " UNION ALL ";
                            }
                            sqlCurrentItems += "SELECT " + DmShared.foriegnKey(cibm.getIpm().getItemCode()) + " AS itemCode "
                                    + ", " + DmServer.vNull(cibm.getReceiveQty()) + " AS receiveQty "
                                    + ", " + DmShared.iNull(cibm.getStockDepartmentCode()).toString() + " AS departmentCode ";

                            if (!DmShared.xNull(sqlCurrentItemBatch).trim().equalsIgnoreCase("")) {
                                sqlCurrentItemBatch += " UNION ALL ";
                            }
                            sqlCurrentItemBatch += "SELECT " + DmShared.iNull(cibm.getIpm().getItemCode()).longValue() + " AS itemCode "
                                    + ", " + DmShared.iNull(cibm.getStockDepartmentCode()).toString() + " AS departmentCode "
                                    + ", '" + DmShared.xNull(cibm.getBarcode()) + "' AS barcode "
                                    + ", '" + DmShared.xNull(cibm.getIpm().getLotNo()) + "' AS lotNo "
                                    + ", '" + DmShared.xNull(cibm.getIpm().getBatchNo()) + "' AS batchNo "
                                    + ", " + DmShared.iNull(cibm.getIpm().getColourCode()).intValue() + " AS colourCode "
                                    + ", " + DmShared.iNull(cibm.getIpm().getSizeCode()).intValue() + " AS sizeCode "
                                    + ", " + DmShared.vNull(cibm.getIpm().getMrp()).doubleValue() + " AS mrp "
                                    + ", " + DmShared.vNull(cibm.getIpm().getSkuMrp()).doubleValue() + " AS skuMrp "
                                    + ", " + DmShared.vNull(cibm.getSaleRate()).doubleValue() + " AS saleRate "
                                    + ", " + DmShared.vNull(cibm.getSkuSaleRate()).doubleValue() + " AS skuSaleRate "
                                    + ", " + DmShared.vNull(cibm.getStaffSaleRate()).doubleValue() + " AS staffSaleRate "
                                    + ", " + DmShared.vNull(cibm.getSkuStaffSaleRate()).doubleValue() + " AS skuStaffSaleRate ";
                            if (DmShared.dNull(cibm.getIpm().getExpiryDate()).trim().equals("")) {
                                sqlCurrentItemBatch += ", CAST (NULL AS DATE) AS expiryDate ";
                            } else {
                                sqlCurrentItemBatch += ", CAST ('" + DmShared.dNull(cibm.getIpm().getExpiryDate()) + "' AS DATE) AS expiryDate ";
                            }
                            if (DmShared.dNull(cibm.getIpm().getManufacturingDate()).trim().equals("")) {
                                sqlCurrentItemBatch += ", CAST (NULL AS DATE) AS manufacturingDate ";
                            } else {
                                sqlCurrentItemBatch += ", CAST ('" + DmShared.dNull(cibm.getIpm().getManufacturingDate()) + "' AS DATE) AS manufacturingDate ";
                            }
                            sqlCurrentItemBatch += ", " + DmServer.vNull(cibm.getReceiveQty()) + " AS receiveQty ";
                        }
                    }

                    if (DmShared.xNull(cibm.getBarcode()).trim().equals("")
                            && DmShared.xNull(cibm.getIpm().getBatchNo()).trim().equals("")
                            && DmShared.xNull(cibm.getIpm().getLotNo()).trim().equals("")
                            && DmShared.iNull(cibm.getIpm().getColourCode()).intValue() == 0
                            && DmShared.iNull(cibm.getIpm().getSizeCode()).intValue() == 0
                            && DmShared.vNull(cibm.getIpm().getMrp()).doubleValue() == 0
                            && DmShared.vNull(cibm.getSaleRate()).doubleValue() == 0
                            && DmShared.vNull(cibm.getStaffSaleRate()).doubleValue() == 0
                            && DmShared.dNull(cibm.getIpm().getExpiryDate()).trim().equals("")) {
                        cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH);
                        flagNonBatch = true;
                    } else {
                        if (!DmShared.xNull(cibm.getCode()).trim().equals("")) {
                            cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.EXISTING_BATCH);
                        } else {
                            cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NEW_BATCH);
                        }
                    }

                    //Check Whether Previous Item Batch Exists Or Not
                    if (compareProperties_Mdl.getPreviousIbm() != null) {
                        for (ItemBatch_Mdl pibm : compareProperties_Mdl.getPreviousIbm()) {
                            if (DmShared.xNull(pibm.getStatus()).trim().equals("")) {
                                if (!DmShared.xNull(cibm.getCode()).trim().equals("")) {
                                    if (DmShared.xNull(pibm.getCode()).trim().equalsIgnoreCase("")) {
                                        if (flagNonBatch) {
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH);
                                        } else {
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.EXISTING_BATCH);
                                        }

                                    } else if (cibm.getCode().equalsIgnoreCase(DmShared.xNull(pibm.getCode()))) {
                                        if (flagNonBatch) {
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH);

                                        } else {
                                            cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.CHANGED);
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.CHANGED);

                                            //Compare Item Code
                                            if (cibm.getIpm().getItemCode().equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getItemCode()))) {
                                                //Compare Barcode
                                                if (DmShared.xNull(cibm.getBarcode()).equalsIgnoreCase(DmShared.xNull(pibm.getBarcode()))) {
                                                    //Compare Batch No    
                                                    if (DmShared.xNull(cibm.getIpm().getBatchNo()).equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getBatchNo()))) {
                                                        //Compare Colour Code
                                                        if (DmShared.xNull(cibm.getIpm().getColourCode()).equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getColourCode()))) {
                                                            //Compare Size Code
                                                            if (DmShared.xNull(cibm.getIpm().getSizeCode()).equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getSizeCode()))) {
                                                                //Compare MRP
                                                                if (DmShared.vNull(cibm.getIpm().getMrp()).doubleValue() == DmShared.vNull(pibm.getIpm().getMrp()).doubleValue()) {
                                                                    //Compare Sale Rate
                                                                    if (DmShared.vNull(cibm.getSaleRate()).doubleValue() == DmShared.vNull(pibm.getSaleRate()).doubleValue()) {
                                                                        //Compare Staff Rate
                                                                        if (DmShared.vNull(cibm.getStaffSaleRate()).doubleValue() == DmShared.vNull(pibm.getStaffSaleRate()).doubleValue()) {
                                                                            //Compare SKU MRP
                                                                            if (DmShared.vNull(cibm.getIpm().getSkuMrp()).doubleValue() == DmShared.vNull(pibm.getIpm().getSkuMrp()).doubleValue()) {
                                                                                //Compare SKU Sale Rate
                                                                                if (DmShared.vNull(cibm.getSkuSaleRate()).doubleValue() == DmShared.vNull(pibm.getSkuSaleRate()).doubleValue()) {
                                                                                    //Compare SKU Staff Rate
                                                                                    if (DmShared.vNull(cibm.getSkuStaffSaleRate()).doubleValue() == DmShared.vNull(pibm.getSkuStaffSaleRate()).doubleValue()) {
                                                                                        //Compare Expiry Date
                                                                                        if (DmShared.dNull(cibm.getIpm().getExpiryDate()).equalsIgnoreCase(DmShared.dNull(pibm.getIpm().getExpiryDate()))) {
                                                                                            flagCompare = true;
                                                                                            if (!DmShared.dNull(cibm.getIpm().getExpiryDate()).trim().equals("") && !DmShared.dNull(pibm.getIpm().getExpiryDate()).trim().equals("")) {
                                                                                                if (DmServer.convertDate(cibm.getIpm().getExpiryDate()).getTime() == DmServer.convertDate(pibm.getIpm().getExpiryDate()).getTime()) {
                                                                                                    flagExpiryDate = true;
                                                                                                } else {
                                                                                                    flagCompare = false;
                                                                                                    flagExpiryDate = false;
                                                                                                }

                                                                                                if (flagExpiryDate) {
                                                                                                    if (DmShared.dNull(cibm.getIpm().getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(pibm.getIpm().getManufacturingDate()))) {
                                                                                                        if (!DmShared.dNull(cibm.getIpm().getManufacturingDate()).trim().equals("") && !DmShared.dNull(pibm.getIpm().getManufacturingDate()).trim().equals("")) {
                                                                                                            if (DmServer.convertDate(cibm.getIpm().getManufacturingDate()).getTime() == DmServer.convertDate(pibm.getIpm().getManufacturingDate()).getTime()) {
                                                                                                                flagManufacturingDate = true;
                                                                                                            } else {
                                                                                                                flagCompare = false;
                                                                                                                flagManufacturingDate = false;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            if (flagCompare) {
                                                                                                //Manufacturing Date : TBC

                                                                                                cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.MATCHED);
                                                                                                pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.MATCHED);

                                                                                                diffQty = DmShared.vNull(cibm.getReceiveQty()).doubleValue() - DmShared.vNull(pibm.getReceiveQty()).doubleValue();
                                                                                                if (diffQty < 0) {
                                                                                                    cibm.setDecreaseQty(String.valueOf(Math.abs(diffQty)));
                                                                                                    cibm.setStatusQuantity("-1");
                                                                                                } else if (diffQty > 0) {
                                                                                                    cibm.setIncreaseQty(String.valueOf(diffQty));
                                                                                                    cibm.setStatusQuantity("1");
                                                                                                }

                                                                                                cibm.setPreviousDate(DmShared.dNull(pibm.getReceiveDate()));
                                                                                                if (DmServer.convertDate(DmShared.dNull(cibm.getReceiveDate())).getTime() < DmServer.convertDate(DmShared.dNull(pibm.getReceiveDate())).getTime()) {
                                                                                                    cibm.setStatusDate("-1");
                                                                                                } else if (DmServer.convertDate(DmShared.dNull(cibm.getReceiveDate())).getTime() > DmServer.convertDate(DmShared.dNull(pibm.getReceiveDate())).getTime()) {
                                                                                                    cibm.setStatusDate("1");
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ibmList.add(cibm);
                }
            }

            sqlPreviousItems = "";
            sqlPreviousItemBatch = "";
            if (!DmShared.xNull(previousReceiveDate).trim().equalsIgnoreCase("")) {
                for (ItemBatch_Mdl pibm : compareProperties_Mdl.getPreviousIbm()) {
                    if (DmShared.vNull(pibm.getReceiveQty()).doubleValue() > 0) {
                        if (!DmShared.xNull(sqlPreviousItems).trim().equalsIgnoreCase("")) {
                            sqlPreviousItems += " UNION ALL ";
                        }
                        sqlPreviousItems += "SELECT " + DmShared.foriegnKey(pibm.getIpm().getItemCode()) + " AS itemCode "
                                + ", " + DmServer.vNull(pibm.getReceiveQty()) + " AS receiveQty "
                                + ", " + DmShared.iNull(pibm.getStockDepartmentCode()).toString() + " AS departmentCode ";

                        if (!DmShared.xNull(sqlPreviousItemBatch).trim().equalsIgnoreCase("")) {
                            sqlPreviousItemBatch += " UNION ALL ";
                        }
                        sqlPreviousItemBatch += "SELECT " + DmShared.iNull(pibm.getIpm().getItemCode()).longValue() + " AS itemCode "
                                + ", " + DmShared.iNull(pibm.getStockDepartmentCode()).toString() + " AS departmentCode "
                                + ", '" + DmShared.xNull(pibm.getBarcode()) + "' AS barcode "
                                + ", '" + DmShared.xNull(pibm.getIpm().getLotNo()) + "' AS lotNo "
                                + ", '" + DmShared.xNull(pibm.getIpm().getBatchNo()) + "' AS batchNo "
                                + ", " + DmShared.iNull(pibm.getIpm().getColourCode()).intValue() + " AS colourCode "
                                + ", " + DmShared.iNull(pibm.getIpm().getSizeCode()).intValue() + " AS sizeCode "
                                + ", " + DmShared.vNull(pibm.getIpm().getMrp()).doubleValue() + " AS mrp "
                                + ", " + DmShared.vNull(pibm.getIpm().getSkuMrp()).doubleValue() + " AS skuMrp "
                                + ", " + DmShared.vNull(pibm.getSaleRate()).doubleValue() + " AS saleRate "
                                + ", " + DmShared.vNull(pibm.getSkuSaleRate()).doubleValue() + " AS skuSaleRate "
                                + ", " + DmShared.vNull(pibm.getStaffSaleRate()).doubleValue() + " AS staffSaleRate "
                                + ", " + DmShared.vNull(pibm.getSkuStaffSaleRate()).doubleValue() + " AS skuStaffSaleRate ";
                        if (DmShared.dNull(pibm.getIpm().getExpiryDate()).trim().equals("")) {
                            sqlPreviousItemBatch += ", CAST(NULL AS DATE) AS expiryDate ";
                        } else {
                            sqlPreviousItemBatch += ", CAST('" + DmShared.dNull(pibm.getIpm().getExpiryDate()) + "' AS DATE) AS expiryDate ";
                        }
                        if (DmShared.dNull(pibm.getIpm().getManufacturingDate()).trim().equals("")) {
                            sqlPreviousItemBatch += ", CAST(NULL AS DATE) AS manufacturingDate ";
                        } else {
                            sqlPreviousItemBatch += ", CAST('" + DmShared.dNull(pibm.getIpm().getManufacturingDate()) + "' AS DATE) AS manufacturingDate ";
                        }
                        sqlPreviousItemBatch += ", " + DmServer.vNull(pibm.getReceiveQty()) + " AS receiveQty ";
                    }
                }

                if (!DmShared.xNull(sqlPreviousItems).trim().equals("")) {
                    if (DmShared.xNull(sqlCurrentItems).trim().equals("")) {
                        sqlCurrentItems = "SELECT Null AS itemCode "
                                + ", 0 AS receiveQty ";
                    }
//                    validateReceivedItems(sqlPreviousItems, sqlCurrentItems, "A", compareProperties_Mdl.getDocId(), compareProperties_Mdl.getSite_Code(), compareProperties_Mdl.getDepartmentCode(), previousReceiveDate, cnTemp);
                    validateReceivedItems(sqlPreviousItems, sqlCurrentItems, "A", compareProperties_Mdl.getSite_Code(), previousReceiveDate, cnTemp);

                }

                if (!DmShared.xNull(sqlPreviousItemBatch).trim().equals("")) {
                    if (DmShared.xNull(sqlCurrentItemBatch).trim().equals("")) {
                        sqlCurrentItemBatch = "SELECT Null AS itemCode "
                                + ", Null AS barcode "
                                + ", Null AS lotNo "
                                + ", Null AS batchNo "
                                + ", Null AS colourCode "
                                + ", Null AS sizeCode "
                                + ", 0 AS mrp "
                                + ", 0 AS skuMrp "
                                + ", 0 AS saleRate "
                                + ", 0 AS skuSaleRate "
                                + ", 0 AS staffSaleRate "
                                + ", 0 AS skuStaffSaleRate "
                                + ", CAST(Null AS DATE) AS expiryDate "
                                + ", CAST(Null AS DATE) AS manufacturingDate "
                                + ", 0 AS receiveQty ";

                    }
//                    validateReceivedItemBatch(false, sqlPreviousItemBatch, sqlCurrentItemBatch, "A", compareProperties_Mdl.getDocId(), compareProperties_Mdl.getSite_Code(), compareProperties_Mdl.getDepartmentCode(), previousReceiveDate, cnTemp);
                    validateReceivedItemBatch(false, sqlPreviousItemBatch, sqlCurrentItemBatch, "A", compareProperties_Mdl.getSite_Code(), previousReceiveDate, cnTemp);

                }

                //Delete Stock
                Stock_Server.deleteStock(compareProperties_Mdl.getDocId(), cnTemp, true);
            }

            for (ItemBatch_Mdl ibm : ibmList) {
                //Get Item Batch
                String ibCode;
                if (DmShared.xNull(ibm.getStatus()).equalsIgnoreCase(ItemBatch_Mdl.ItemBatchStatus.NEW_BATCH)
                        || DmShared.xNull(ibm.getStatus()).equalsIgnoreCase(ItemBatch_Mdl.ItemBatchStatus.CHANGED)
                        || DmShared.xNull(ibm.getStatus()).equalsIgnoreCase(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH)) {
                    ItemBatch_Mdl itemBatch_Mdl = new ItemBatch_Mdl();
                    itemBatch_Mdl.setCode("");
                    itemBatch_Mdl.setSaleRate(DmServer.vNull(ibm.getSaleRate(), "0.00"));
                    itemBatch_Mdl.setSkuSaleRate(DmServer.vNull(ibm.getSkuSaleRate()));
                    itemBatch_Mdl.setStaffSaleRate(DmServer.vNull(ibm.getStaffSaleRate(), "0.00"));
                    itemBatch_Mdl.setSkuStaffSaleRate(DmServer.vNull(ibm.getSkuStaffSaleRate()));
                    itemBatch_Mdl.setBarcode(DmShared.xNull(ibm.getBarcode()));
                    itemBatch_Mdl.setIpm(ibm.getIpm());

//                    ibCode = getItemBatch(itemBatch_Mdl, cnTemp);
                    ibCode = createItemBatch(itemBatch_Mdl, cnTemp, siteCode, v_Prefix);

                } else {
                    ibCode = ibm.getCode();
                }

                //Save Stock Record
                Stock_Mdl stock_Mdl = new Stock_Mdl();
                stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(compareProperties_Mdl.getDocId()));
                stock_Mdl.setSharedKeyFieldName(DmShared.xNull(ibm.getStockV_Sno()));
                stock_Mdl.setItemCode(DmShared.xNull(ibm.getIpm().getItemCode()));
                stock_Mdl.setRecQty(DmShared.xNull(ibm.getReceiveQty()));
                stock_Mdl.setIbCode(ibCode);

                stock_Mdl.setRecId(DmShared.xNull(compareProperties_Mdl.getRecId()));
                stock_Mdl.setRecIdPrefix(DmShared.xNull(compareProperties_Mdl.getRecIdPrefix()));
                stock_Mdl.setV_Type(DmShared.xNull(compareProperties_Mdl.getV_Type()));
                stock_Mdl.setV_No(DmShared.xNull(compareProperties_Mdl.getV_No()));
                stock_Mdl.setV_Date(DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate()));
//                stock_Mdl.setDepartmentCode(DmShared.xNull(compareProperties_Mdl.getDepartmentCode()));
                stock_Mdl.setDepartmentCode(DmShared.xNull(ibm.getStockDepartmentCode()));

                stock_Mdl.setSite_Code(DmShared.xNull(compareProperties_Mdl.getSite_Code()));
                stock_Mdl.setStockContraSubCode(DmShared.xNull(compareProperties_Mdl.getContraSubCode()));
                stock_Mdl.setRefChallanNo(DmShared.xNull(ibm.getStockRefChallanNo()));
                stock_Mdl.setRefChallanDate(DmShared.dNull(ibm.getStockRefChallanDate()));
                stock_Mdl.setRefDocId(DmShared.xNull(ibm.getStockRefDocId()));
                stock_Mdl.setRefV_Type(DmShared.xNull(ibm.getStockRefV_Type()));
                stock_Mdl.setRefV_SNo(DmShared.xNull(ibm.getStockRefV_SNo()));
                stock_Mdl.setSkuValue(DmServer.vNull(ibm.getStockSkuValue(), "0.00"));
                stock_Mdl.setLandedSkuValue(DmServer.vNull(ibm.getStockLandedSkuValue(), "0.00"));
                stock_Mdl.setRemark(DmShared.xNull(ibm.getStockRemark()));

                //Initialize StockFIFO Model
                stock_Mdl.getStockFifo_Mdl().setSharedKeyFieldCode(DmShared.iNull(compareProperties_Mdl.getDocId()).toString());
                stock_Mdl.getStockFifo_Mdl().setSharedKeyFieldName(DmShared.iNull(ibm.getStockV_Sno()).toString());
                stock_Mdl.getStockFifo_Mdl().setSno("");
                stock_Mdl.getStockFifo_Mdl().setRecQty(DmShared.xNull(ibm.getReceiveQty()));
                stock_Mdl.getStockFifo_Mdl().setStockValueDate(DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate()));
                stock_Mdl.getStockFifo_Mdl().setAvgSKURate("");         //TBC
                stock_Mdl.getStockFifo_Mdl().setAvgLandedSKURate(""); //TBC

//                Stock_Server.saveStockRecord(stock_Mdl, "A", false, false, cnTemp);
                Stock_Server.saveStockRecord(stock_Mdl, "A", false, cnTemp);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void compareProperties(CompareProperties_Mdl compareProperties_Mdl, Connection cnTemp, DmRSSPrivate rssTemp) throws DmException {
        double diffQty;
        List<ItemBatch_Mdl> ibmList = new ArrayList<>();
        String currentReceiveDate, previousReceiveDate;
        String sqlPreviousItems = "", sqlCurrentItems = "";
        String sqlPreviousItemBatch = "", sqlCurrentItemBatch = "";
        boolean flagManufacturingDate = false, flagExpiryDate = false, flagCompare = false, flagNonBatch = false;
        try {
            currentReceiveDate = DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate());
            previousReceiveDate = DmShared.dNull(compareProperties_Mdl.getPreviousReceiveDate());

            sqlCurrentItems = "";
            sqlCurrentItemBatch = "";
            for (ItemBatch_Mdl cibm : compareProperties_Mdl.getCurrentIbm()) {
                if (!DmShared.xNull(cibm.getIpm().getItemCode()).trim().equals("")) {
                    flagCompare = false;
                    flagExpiryDate = false;
                    flagManufacturingDate = false;
                    flagNonBatch = false;

                    if (!DmShared.xNull(previousReceiveDate).trim().equalsIgnoreCase("")) {
                        if (DmShared.vNull(cibm.getReceiveQty()).doubleValue() > 0) {
                            if (!DmShared.xNull(sqlCurrentItems).trim().equalsIgnoreCase("")) {
                                sqlCurrentItems += " UNION ALL ";
                            }
                            sqlCurrentItems += "SELECT " + DmShared.foriegnKey(cibm.getIpm().getItemCode()) + " AS itemCode "
                                    + ", " + DmServer.vNull(cibm.getReceiveQty()) + " AS receiveQty "
                                    + ", " + DmShared.iNull(cibm.getStockDepartmentCode()).toString() + " AS departmentCode ";

                            if (!DmShared.xNull(sqlCurrentItemBatch).trim().equalsIgnoreCase("")) {
                                sqlCurrentItemBatch += " UNION ALL ";
                            }
                            sqlCurrentItemBatch += "SELECT " + DmShared.iNull(cibm.getIpm().getItemCode()).longValue() + " AS itemCode "
                                    + ", " + DmShared.iNull(cibm.getStockDepartmentCode()).toString() + " AS departmentCode "
                                    + ", '" + DmShared.xNull(cibm.getBarcode()) + "' AS barcode "
                                    + ", '" + DmShared.xNull(cibm.getIpm().getLotNo()) + "' AS lotNo "
                                    + ", '" + DmShared.xNull(cibm.getIpm().getBatchNo()) + "' AS batchNo "
                                    + ", " + DmShared.iNull(cibm.getIpm().getColourCode()).intValue() + " AS colourCode "
                                    + ", " + DmShared.iNull(cibm.getIpm().getSizeCode()).intValue() + " AS sizeCode "
                                    + ", " + DmShared.vNull(cibm.getIpm().getMrp()).doubleValue() + " AS mrp "
                                    + ", " + DmShared.vNull(cibm.getIpm().getSkuMrp()).doubleValue() + " AS skuMrp "
                                    + ", " + DmShared.vNull(cibm.getSaleRate()).doubleValue() + " AS saleRate "
                                    + ", " + DmShared.vNull(cibm.getSkuSaleRate()).doubleValue() + " AS skuSaleRate "
                                    + ", " + DmShared.vNull(cibm.getStaffSaleRate()).doubleValue() + " AS staffSaleRate "
                                    + ", " + DmShared.vNull(cibm.getSkuStaffSaleRate()).doubleValue() + " AS skuStaffSaleRate ";
                            if (DmShared.dNull(cibm.getIpm().getExpiryDate()).trim().equals("")) {
                                sqlCurrentItemBatch += ", CAST (NULL AS DATE) AS expiryDate ";
                            } else {
                                sqlCurrentItemBatch += ", CAST ('" + DmShared.dNull(cibm.getIpm().getExpiryDate()) + "' AS DATE) AS expiryDate ";
                            }
                            if (DmShared.dNull(cibm.getIpm().getManufacturingDate()).trim().equals("")) {
                                sqlCurrentItemBatch += ", CAST (NULL AS DATE) AS manufacturingDate ";
                            } else {
                                sqlCurrentItemBatch += ", CAST ('" + DmShared.dNull(cibm.getIpm().getManufacturingDate()) + "' AS DATE) AS manufacturingDate ";
                            }
                            sqlCurrentItemBatch += ", " + DmServer.vNull(cibm.getReceiveQty()) + " AS receiveQty ";
                        }
                    }

                    if (DmShared.xNull(cibm.getBarcode()).trim().equals("")
                            && DmShared.xNull(cibm.getIpm().getBatchNo()).trim().equals("")
                            && DmShared.xNull(cibm.getIpm().getLotNo()).trim().equals("")
                            && DmShared.iNull(cibm.getIpm().getColourCode()).intValue() == 0
                            && DmShared.iNull(cibm.getIpm().getSizeCode()).intValue() == 0
                            && DmShared.vNull(cibm.getIpm().getMrp()).doubleValue() == 0
                            && DmShared.vNull(cibm.getSaleRate()).doubleValue() == 0
                            && DmShared.vNull(cibm.getStaffSaleRate()).doubleValue() == 0
                            && DmShared.dNull(cibm.getIpm().getExpiryDate()).trim().equals("")) {
                        cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH);
                        flagNonBatch = true;
                    } else {
                        if (!DmShared.xNull(cibm.getCode()).trim().equals("")) {
                            cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.EXISTING_BATCH);
                        } else {
                            cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NEW_BATCH);
                        }
                    }

                    //Check Whether Previous Item Batch Exists Or Not
                    if (compareProperties_Mdl.getPreviousIbm() != null) {
                        for (ItemBatch_Mdl pibm : compareProperties_Mdl.getPreviousIbm()) {
                            if (DmShared.xNull(pibm.getStatus()).trim().equals("")) {
                                if (!DmShared.xNull(cibm.getCode()).trim().equals("")) {
                                    if (DmShared.xNull(pibm.getCode()).trim().equalsIgnoreCase("")) {
                                        if (flagNonBatch) {
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH);
                                        } else {
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.EXISTING_BATCH);
                                        }

                                    } else if (cibm.getCode().equalsIgnoreCase(DmShared.xNull(pibm.getCode()))) {
                                        if (flagNonBatch) {
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH);

                                        } else {
                                            cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.CHANGED);
                                            pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.CHANGED);

                                            //Compare Item Code
                                            if (cibm.getIpm().getItemCode().equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getItemCode()))) {
                                                //Compare Barcode
                                                if (DmShared.xNull(cibm.getBarcode()).equalsIgnoreCase(DmShared.xNull(pibm.getBarcode()))) {
                                                    //Compare Batch No    
                                                    if (DmShared.xNull(cibm.getIpm().getBatchNo()).equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getBatchNo()))) {
                                                        //Compare Colour Code
                                                        if (DmShared.xNull(cibm.getIpm().getColourCode()).equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getColourCode()))) {
                                                            //Compare Size Code
                                                            if (DmShared.xNull(cibm.getIpm().getSizeCode()).equalsIgnoreCase(DmShared.xNull(pibm.getIpm().getSizeCode()))) {
                                                                //Compare MRP
                                                                if (DmShared.vNull(cibm.getIpm().getMrp()).doubleValue() == DmShared.vNull(pibm.getIpm().getMrp()).doubleValue()) {
                                                                    //Compare Sale Rate
                                                                    if (DmShared.vNull(cibm.getSaleRate()).doubleValue() == DmShared.vNull(pibm.getSaleRate()).doubleValue()) {
                                                                        //Compare Staff Rate
                                                                        if (DmShared.vNull(cibm.getStaffSaleRate()).doubleValue() == DmShared.vNull(pibm.getStaffSaleRate()).doubleValue()) {
                                                                            //Compare SKU MRP
                                                                            if (DmShared.vNull(cibm.getIpm().getSkuMrp()).doubleValue() == DmShared.vNull(pibm.getIpm().getSkuMrp()).doubleValue()) {
                                                                                //Compare SKU Sale Rate
                                                                                if (DmShared.vNull(cibm.getSkuSaleRate()).doubleValue() == DmShared.vNull(pibm.getSkuSaleRate()).doubleValue()) {
                                                                                    //Compare SKU Staff Rate
                                                                                    if (DmShared.vNull(cibm.getSkuStaffSaleRate()).doubleValue() == DmShared.vNull(pibm.getSkuStaffSaleRate()).doubleValue()) {
                                                                                        //Compare Expiry Date
                                                                                        if (DmShared.dNull(cibm.getIpm().getExpiryDate()).equalsIgnoreCase(DmShared.dNull(pibm.getIpm().getExpiryDate()))) {
                                                                                            flagCompare = true;
                                                                                            if (!DmShared.dNull(cibm.getIpm().getExpiryDate()).trim().equals("") && !DmShared.dNull(pibm.getIpm().getExpiryDate()).trim().equals("")) {
                                                                                                if (DmServer.convertDate(cibm.getIpm().getExpiryDate()).getTime() == DmServer.convertDate(pibm.getIpm().getExpiryDate()).getTime()) {
                                                                                                    flagExpiryDate = true;
                                                                                                } else {
                                                                                                    flagCompare = false;
                                                                                                    flagExpiryDate = false;
                                                                                                }

                                                                                                if (flagExpiryDate) {
                                                                                                    if (DmShared.dNull(cibm.getIpm().getManufacturingDate()).equalsIgnoreCase(DmShared.dNull(pibm.getIpm().getManufacturingDate()))) {
                                                                                                        if (!DmShared.dNull(cibm.getIpm().getManufacturingDate()).trim().equals("") && !DmShared.dNull(pibm.getIpm().getManufacturingDate()).trim().equals("")) {
                                                                                                            if (DmServer.convertDate(cibm.getIpm().getManufacturingDate()).getTime() == DmServer.convertDate(pibm.getIpm().getManufacturingDate()).getTime()) {
                                                                                                                flagManufacturingDate = true;
                                                                                                            } else {
                                                                                                                flagCompare = false;
                                                                                                                flagManufacturingDate = false;
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            if (flagCompare) {
                                                                                                //Manufacturing Date : TBC

                                                                                                cibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.MATCHED);
                                                                                                pibm.setStatus(ItemBatch_Mdl.ItemBatchStatus.MATCHED);

                                                                                                diffQty = DmShared.vNull(cibm.getReceiveQty()).doubleValue() - DmShared.vNull(pibm.getReceiveQty()).doubleValue();
                                                                                                if (diffQty < 0) {
                                                                                                    cibm.setDecreaseQty(String.valueOf(Math.abs(diffQty)));
                                                                                                    cibm.setStatusQuantity("-1");
                                                                                                } else if (diffQty > 0) {
                                                                                                    cibm.setIncreaseQty(String.valueOf(diffQty));
                                                                                                    cibm.setStatusQuantity("1");
                                                                                                }

                                                                                                cibm.setPreviousDate(DmShared.dNull(pibm.getReceiveDate()));
                                                                                                if (DmServer.convertDate(DmShared.dNull(cibm.getReceiveDate())).getTime() < DmServer.convertDate(DmShared.dNull(pibm.getReceiveDate())).getTime()) {
                                                                                                    cibm.setStatusDate("-1");
                                                                                                } else if (DmServer.convertDate(DmShared.dNull(cibm.getReceiveDate())).getTime() > DmServer.convertDate(DmShared.dNull(pibm.getReceiveDate())).getTime()) {
                                                                                                    cibm.setStatusDate("1");
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ibmList.add(cibm);
                }
            }

            sqlPreviousItems = "";
            sqlPreviousItemBatch = "";
            if (!DmShared.xNull(previousReceiveDate).trim().equalsIgnoreCase("")) {
                for (ItemBatch_Mdl pibm : compareProperties_Mdl.getPreviousIbm()) {
                    if (DmShared.vNull(pibm.getReceiveQty()).doubleValue() > 0) {
                        if (!DmShared.xNull(sqlPreviousItems).trim().equalsIgnoreCase("")) {
                            sqlPreviousItems += " UNION ALL ";
                        }
                        sqlPreviousItems += "SELECT " + DmShared.foriegnKey(pibm.getIpm().getItemCode()) + " AS itemCode "
                                + ", " + DmServer.vNull(pibm.getReceiveQty()) + " AS receiveQty "
                                + ", " + DmShared.iNull(pibm.getStockDepartmentCode()).toString() + " AS departmentCode ";

                        if (!DmShared.xNull(sqlPreviousItemBatch).trim().equalsIgnoreCase("")) {
                            sqlPreviousItemBatch += " UNION ALL ";
                        }
                        sqlPreviousItemBatch += "SELECT " + DmShared.iNull(pibm.getIpm().getItemCode()).longValue() + " AS itemCode "
                                + ", " + DmShared.iNull(pibm.getStockDepartmentCode()).toString() + " AS departmentCode "
                                + ", '" + DmShared.xNull(pibm.getBarcode()) + "' AS barcode "
                                + ", '" + DmShared.xNull(pibm.getIpm().getLotNo()) + "' AS lotNo "
                                + ", '" + DmShared.xNull(pibm.getIpm().getBatchNo()) + "' AS batchNo "
                                + ", " + DmShared.iNull(pibm.getIpm().getColourCode()).intValue() + " AS colourCode "
                                + ", " + DmShared.iNull(pibm.getIpm().getSizeCode()).intValue() + " AS sizeCode "
                                + ", " + DmShared.vNull(pibm.getIpm().getMrp()).doubleValue() + " AS mrp "
                                + ", " + DmShared.vNull(pibm.getIpm().getSkuMrp()).doubleValue() + " AS skuMrp "
                                + ", " + DmShared.vNull(pibm.getSaleRate()).doubleValue() + " AS saleRate "
                                + ", " + DmShared.vNull(pibm.getSkuSaleRate()).doubleValue() + " AS skuSaleRate "
                                + ", " + DmShared.vNull(pibm.getStaffSaleRate()).doubleValue() + " AS staffSaleRate "
                                + ", " + DmShared.vNull(pibm.getSkuStaffSaleRate()).doubleValue() + " AS skuStaffSaleRate ";
                        if (DmShared.dNull(pibm.getIpm().getExpiryDate()).trim().equals("")) {
                            sqlPreviousItemBatch += ", CAST(NULL AS DATE) AS expiryDate ";
                        } else {
                            sqlPreviousItemBatch += ", CAST('" + DmShared.dNull(pibm.getIpm().getExpiryDate()) + "' AS DATE) AS expiryDate ";
                        }
                        if (DmShared.dNull(pibm.getIpm().getManufacturingDate()).trim().equals("")) {
                            sqlPreviousItemBatch += ", CAST(NULL AS DATE) AS manufacturingDate ";
                        } else {
                            sqlPreviousItemBatch += ", CAST('" + DmShared.dNull(pibm.getIpm().getManufacturingDate()) + "' AS DATE) AS manufacturingDate ";
                        }
                        sqlPreviousItemBatch += ", " + DmServer.vNull(pibm.getReceiveQty()) + " AS receiveQty ";
                    }
                }

                if (!DmShared.xNull(sqlPreviousItems).trim().equals("")) {
                    if (DmShared.xNull(sqlCurrentItems).trim().equals("")) {
                        sqlCurrentItems = "SELECT Null AS itemCode "
                                + ", 0 AS receiveQty ";
                    }
//                    validateReceivedItems(sqlPreviousItems, sqlCurrentItems, "A", compareProperties_Mdl.getDocId(), compareProperties_Mdl.getSite_Code(), compareProperties_Mdl.getDepartmentCode(), previousReceiveDate, cnTemp);
                    validateReceivedItems(sqlPreviousItems, sqlCurrentItems, "A", compareProperties_Mdl.getSite_Code(), previousReceiveDate, cnTemp);

                }

                if (!DmShared.xNull(sqlPreviousItemBatch).trim().equals("")) {
                    if (DmShared.xNull(sqlCurrentItemBatch).trim().equals("")) {
                        sqlCurrentItemBatch = "SELECT Null AS itemCode "
                                + ", Null AS barcode "
                                + ", Null AS lotNo "
                                + ", Null AS batchNo "
                                + ", Null AS colourCode "
                                + ", Null AS sizeCode "
                                + ", 0 AS mrp "
                                + ", 0 AS skuMrp "
                                + ", 0 AS saleRate "
                                + ", 0 AS skuSaleRate "
                                + ", 0 AS staffSaleRate "
                                + ", 0 AS skuStaffSaleRate "
                                + ", CAST(Null AS DATE) AS expiryDate "
                                + ", CAST(Null AS DATE) AS manufacturingDate "
                                + ", 0 AS receiveQty ";

                    }
//                    validateReceivedItemBatch(false, sqlPreviousItemBatch, sqlCurrentItemBatch, "A", compareProperties_Mdl.getDocId(), compareProperties_Mdl.getSite_Code(), compareProperties_Mdl.getDepartmentCode(), previousReceiveDate, cnTemp);
                    validateReceivedItemBatch(false, sqlPreviousItemBatch, sqlCurrentItemBatch, "A", compareProperties_Mdl.getSite_Code(), previousReceiveDate, cnTemp);

                }

                //Delete Stock
                Stock_Server.deleteStock(compareProperties_Mdl.getDocId(), cnTemp, true);
            }

            for (ItemBatch_Mdl ibm : ibmList) {
                //Get Item Batch
                String ibCode;
                if (DmShared.xNull(ibm.getStatus()).equalsIgnoreCase(ItemBatch_Mdl.ItemBatchStatus.NEW_BATCH)
                        || DmShared.xNull(ibm.getStatus()).equalsIgnoreCase(ItemBatch_Mdl.ItemBatchStatus.CHANGED)
                        || DmShared.xNull(ibm.getStatus()).equalsIgnoreCase(ItemBatch_Mdl.ItemBatchStatus.NON_BATCH)) {
                    ItemBatch_Mdl itemBatch_Mdl = new ItemBatch_Mdl();
                    itemBatch_Mdl.setCode("");
                    itemBatch_Mdl.setSaleRate(DmServer.vNull(ibm.getSaleRate(), "0.00"));
                    itemBatch_Mdl.setSkuSaleRate(DmServer.vNull(ibm.getSkuSaleRate()));
                    itemBatch_Mdl.setStaffSaleRate(DmServer.vNull(ibm.getStaffSaleRate(), "0.00"));
                    itemBatch_Mdl.setSkuStaffSaleRate(DmServer.vNull(ibm.getSkuStaffSaleRate()));
                    itemBatch_Mdl.setBarcode(DmShared.xNull(ibm.getBarcode()));
                    itemBatch_Mdl.setIpm(ibm.getIpm());

//                    ibCode = getItemBatch(itemBatch_Mdl, cnTemp);
                    ibCode = createItemBatch(itemBatch_Mdl, cnTemp, rssTemp);

                } else {
                    ibCode = ibm.getCode();
                }

                //Save Stock Record
                Stock_Mdl stock_Mdl = new Stock_Mdl();
                stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(compareProperties_Mdl.getDocId()));
                stock_Mdl.setSharedKeyFieldName(DmShared.xNull(ibm.getStockV_Sno()));
                stock_Mdl.setItemCode(DmShared.xNull(ibm.getIpm().getItemCode()));
                stock_Mdl.setRecQty(DmShared.xNull(ibm.getReceiveQty()));
                stock_Mdl.setIbCode(ibCode);

                stock_Mdl.setRecId(DmShared.xNull(compareProperties_Mdl.getRecId()));
                stock_Mdl.setRecIdPrefix(DmShared.xNull(compareProperties_Mdl.getRecIdPrefix()));
                stock_Mdl.setV_Type(DmShared.xNull(compareProperties_Mdl.getV_Type()));
                stock_Mdl.setV_No(DmShared.xNull(compareProperties_Mdl.getV_No()));
                stock_Mdl.setV_Date(DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate()));
//                stock_Mdl.setDepartmentCode(DmShared.xNull(compareProperties_Mdl.getDepartmentCode()));
                stock_Mdl.setDepartmentCode(DmShared.xNull(ibm.getStockDepartmentCode()));

                stock_Mdl.setSite_Code(DmShared.xNull(compareProperties_Mdl.getSite_Code()));
                stock_Mdl.setStockContraSubCode(DmShared.xNull(compareProperties_Mdl.getContraSubCode()));
                stock_Mdl.setRefChallanNo(DmShared.xNull(ibm.getStockRefChallanNo()));
                stock_Mdl.setRefChallanDate(DmShared.dNull(ibm.getStockRefChallanDate()));
                stock_Mdl.setRefDocId(DmShared.xNull(ibm.getStockRefDocId()));
                stock_Mdl.setRefV_Type(DmShared.xNull(ibm.getStockRefV_Type()));
                stock_Mdl.setRefV_SNo(DmShared.xNull(ibm.getStockRefV_SNo()));
                stock_Mdl.setSkuValue(DmServer.vNull(ibm.getStockSkuValue(), "0.00"));
                stock_Mdl.setLandedSkuValue(DmServer.vNull(ibm.getStockLandedSkuValue(), "0.00"));
                stock_Mdl.setRemark(DmShared.xNull(ibm.getStockRemark()));

                //Initialize StockFIFO Model
                stock_Mdl.getStockFifo_Mdl().setSharedKeyFieldCode(DmShared.iNull(compareProperties_Mdl.getDocId()).toString());
                stock_Mdl.getStockFifo_Mdl().setSharedKeyFieldName(DmShared.iNull(ibm.getStockV_Sno()).toString());
                stock_Mdl.getStockFifo_Mdl().setSno("");
                stock_Mdl.getStockFifo_Mdl().setRecQty(DmShared.xNull(ibm.getReceiveQty()));
                stock_Mdl.getStockFifo_Mdl().setStockValueDate(DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate()));
                stock_Mdl.getStockFifo_Mdl().setAvgSKURate("");         //TBC
                stock_Mdl.getStockFifo_Mdl().setAvgLandedSKURate(""); //TBC

//                Stock_Server.saveStockRecord(stock_Mdl, "A", false, false, cnTemp);
                Stock_Server.saveStockRecord(stock_Mdl, "A", false, cnTemp);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static String getItemBatch(ItemBatch_Mdl itemBatch_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";
        try {

            //Select Existing Item Batch Code
            sqlQuery = "SELECT ib.code "
                    + " FROM itemBatch ib "
                    + " WHERE itemCode = :itemCode "
                    + " AND ISNULL(barcode,'') = :barcode "
                    + " AND ISNULL(batchNo,'') = :batchNo ";
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getManufacturingDate()).trim().equals("")) {
                sqlQuery += " AND manufacturingDate = :manufacturingDate ";
            } else {
                sqlQuery += " AND manufacturingDate IS NULL ";
            }
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                sqlQuery += " AND expiryDate = :expiryDate ";
            } else {
                sqlQuery += " AND expiryDate IS NULL ";
            }
            sqlQuery += " AND ISNULL(colourCode,0) = :colourCode "
                    + " AND ISNULL(sizeCode,0) = :sizeCode "
                    + " AND ISNULL(lotNo,'') = :lotNo "
                    + " AND ISNULL(mrp,0) = :mrp "
                    + " AND ISNULL(saleRate,0) = :saleRate "
                    + " AND ISNULL(staffSaleRate,0) = :staffSaleRate "
                    + " AND ISNULL(skuMrp,0) = :skuMrp "
                    + " AND ISNULL(skuSaleRate,0) = :skuSaleRate "
                    + " AND ISNULL(skuStaffSaleRate,0) = :skuStaffSaleRate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemBatch_Mdl.getIpm().getItemCode()));
            psTemp.setString("barcode", DmShared.xNull(itemBatch_Mdl.getBarcode()));
            psTemp.setString("batchNo", DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()));
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getManufacturingDate()).trim().equals("")) {
                psTemp.setDate("manufacturingDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getManufacturingDate()));
            }
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                psTemp.setDate("expiryDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getExpiryDate()));
            }
            psTemp.setString("colourCode", DmShared.iNull(itemBatch_Mdl.getIpm().getColourCode()).toString());
            psTemp.setString("sizeCode", DmShared.iNull(itemBatch_Mdl.getIpm().getSizeCode()).toString());
            psTemp.setString("lotNo", DmShared.xNull(itemBatch_Mdl.getIpm().getLotNo()));
            psTemp.setString("mrp", DmServer.vNull(itemBatch_Mdl.getIpm().getMrp(), "0.00"));
            psTemp.setString("saleRate", DmServer.vNull(itemBatch_Mdl.getSaleRate(), "0.00"));
            psTemp.setString("staffSaleRate", DmServer.vNull(itemBatch_Mdl.getStaffSaleRate(), "0.00"));
            psTemp.setString("skuMrp", DmServer.vNull(itemBatch_Mdl.getIpm().getSkuMrp()));
            psTemp.setString("skuSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuSaleRate()));
            psTemp.setString("skuStaffSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuStaffSaleRate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnValue = DmShared.xNull(rsTemp.getString("code"));
                if (!returnValue.trim().equals("")) {
                    rsTemp.close();
                    psTemp.close();
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            returnValue = "";
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

    public static String getItemBatchForAutoIssue(ItemBatch_Mdl itemBatch_Mdl, String siteCode, String departmentCode, String IssueQty, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";
        try {

       
            sqlQuery = "SELECT top 1 ib.code   FROM itemBatch ib \n"
                    + "left join itemBatchStock ibs on ibs.ibCode=ib.code\n"
                    + "WHERE ib.itemCode = :itemCode  \n"
                    + "AND ISNULL(ib.barcode,'') = :barcode  \n"
                    + "AND ISNULL(ib.batchNo,'') = :batchNo  \n"
                    + "   AND ib.expiryDate = :expiryDate  \n"
                    + "AND ISNULL(ib.colourCode,0) = :colourCode  \n"
                    + "AND ISNULL(ib.sizeCode,0) = :sizeCode    \n"
                    + "AND ISNULL(ib.mrp,0) = :mrp  AND ISNULL(saleRate,0) = :saleRate  \n"
                    + "AND ISNULL(ib.staffSaleRate,0) = :staffSaleRate  AND ISNULL(skuMrp,0) = :skuMrp \n"
                    + "AND ISNULL(ib.skuSaleRate,0) = :skuSaleRate  AND ISNULL(skuStaffSaleRate,0) = :skuStaffSaleRate \n"
                    + "AND ibs.itemCode = :itemCode               \n"
                    + "AND ibs.departmentCode = :departmentCode      \n"
                    + "AND ibs.site_Code = :siteCode and ibs.balanceQty>= :IssueQty \n"
                    + "order by ib.expiryDate  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemBatch_Mdl.getIpm().getItemCode()));
            psTemp.setString("barcode", DmShared.xNull(itemBatch_Mdl.getBarcode()));
            psTemp.setString("batchNo", DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()));
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getManufacturingDate()).trim().equals("")) {
                psTemp.setDate("manufacturingDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getManufacturingDate()));
            }
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                psTemp.setDate("expiryDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getExpiryDate()));
            }
            psTemp.setString("colourCode", DmShared.iNull(itemBatch_Mdl.getIpm().getColourCode()).toString());
            psTemp.setString("sizeCode", DmShared.iNull(itemBatch_Mdl.getIpm().getSizeCode()).toString());

            psTemp.setString("mrp", DmServer.vNull(itemBatch_Mdl.getIpm().getMrp(), "0.00"));
            psTemp.setString("saleRate", DmServer.vNull(itemBatch_Mdl.getSaleRate(), "0.00"));
            psTemp.setString("staffSaleRate", DmServer.vNull(itemBatch_Mdl.getStaffSaleRate(), "0.00"));
            psTemp.setString("skuMrp", DmServer.vNull(itemBatch_Mdl.getIpm().getSkuMrp()));
            psTemp.setString("skuSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuSaleRate()));
            psTemp.setString("skuStaffSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuStaffSaleRate()));

            psTemp.setString("departmentCode", DmShared.xNull(departmentCode));
            psTemp.setString("siteCode", DmShared.xNull(siteCode));
            psTemp.setString("IssueQty", DmShared.xNull(IssueQty));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnValue = DmShared.xNull(rsTemp.getString("code"));
              
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            returnValue = "";
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

    public static void validateRemovedReceivedItems(CompareProperties_Mdl compareProperties_Mdl, StockEntryType entryType, Connection cnTemp, DmRSSPrivate rssTemp) throws DmException {
        String currentReceiveDate, previousReceiveDate;
        String sqlPreviousItems = "", sqlCurrentItems = "";
        String sqlPreviousItemBatch = "", sqlCurrentItemBatch = "";
        String valuationType = "";
        boolean isStockTransferReceiveRecord = false;
        boolean isItemReceive = false;
        try {
            currentReceiveDate = DmShared.dNull(compareProperties_Mdl.getCurrentReceiveDate());
            previousReceiveDate = DmShared.dNull(compareProperties_Mdl.getPreviousReceiveDate());
            if (DmShared.xNull(previousReceiveDate).trim().equalsIgnoreCase("")) {
                return;
            }

            switch (entryType) {
                case STOCK_TRANSFER_RECEIVE:
                    isStockTransferReceiveRecord = true;
                    break;
                case ITEM_RECEIVE:
                    isItemReceive = true;
                    break;
                default:
                    break;
            }

            if (isStockTransferReceiveRecord) {
                valuationType = "A";
            } else if (isItemReceive) {
                valuationType = "A";
            } else {
                valuationType = "RF"; //As we are handling only receive record here.
            }
            sqlCurrentItems = "";
            sqlCurrentItemBatch = "";
            for (ItemBatch_Mdl cibm : compareProperties_Mdl.getCurrentIbm()) {
                if (DmShared.vNull(cibm.getReceiveQty()).doubleValue() > 0) {
                    if (!DmShared.xNull(sqlCurrentItems).trim().equalsIgnoreCase("")) {
                        sqlCurrentItems += " UNION ALL ";
                    }
                    sqlCurrentItems += "SELECT " + DmShared.foriegnKey(cibm.getIpm().getItemCode()) + " AS itemCode "
                            + ", " + DmServer.vNull(cibm.getReceiveQty()) + " AS receiveQty "
                            + ", " + DmShared.iNull(cibm.getStockDepartmentCode()) + " AS departmentCode ";

                    if (!DmShared.xNull(sqlCurrentItemBatch).trim().equalsIgnoreCase("")) {
                        sqlCurrentItemBatch += " UNION ALL ";
                    }
                    sqlCurrentItemBatch += "SELECT " + DmShared.iNull(cibm.getIpm().getItemCode()).longValue() + " AS itemCode "
                            + ", " + DmShared.foriegnKey(DmShared.xNull(cibm.getCode())) + " AS ibCode "
                            + ", " + DmServer.vNull(cibm.getReceiveQty()) + " AS receiveQty "
                            + ", " + DmShared.iNull(cibm.getStockDepartmentCode()) + " AS departmentCode ";

                }
            }

            sqlPreviousItems = "";
            sqlPreviousItemBatch = "";
            for (ItemBatch_Mdl pibm : compareProperties_Mdl.getPreviousIbm()) {
                if (DmShared.vNull(pibm.getReceiveQty()).doubleValue() > 0) {
                    if (!DmShared.xNull(sqlPreviousItems).trim().equalsIgnoreCase("")) {
                        sqlPreviousItems += " UNION ALL ";
                    }
                    sqlPreviousItems += "SELECT " + DmShared.foriegnKey(pibm.getIpm().getItemCode()) + " AS itemCode "
                            + ", " + DmServer.vNull(pibm.getReceiveQty()) + " AS receiveQty "
                            + ", " + DmShared.iNull(pibm.getStockDepartmentCode()) + " AS departmentCode ";

                    if (!DmShared.xNull(sqlPreviousItemBatch).trim().equalsIgnoreCase("")) {
                        sqlPreviousItemBatch += " UNION ALL ";
                    }
                    sqlPreviousItemBatch += "SELECT " + DmShared.iNull(pibm.getIpm().getItemCode()).longValue() + " AS itemCode "
                            + ", " + DmShared.foriegnKey(DmShared.xNull(pibm.getCode())) + " AS ibCode "
                            + ", " + DmServer.vNull(pibm.getReceiveQty()) + " AS receiveQty "
                            + ", " + DmShared.iNull(pibm.getStockDepartmentCode()) + " AS departmentCode ";

                }
            }

            if (!DmShared.xNull(sqlPreviousItems).trim().equals("")) {
                if (DmShared.xNull(sqlCurrentItems).trim().equals("")) {
                    sqlCurrentItems += "SELECT Null AS itemCode "
                            + ", 0 AS receiveQty "
                            + ", NULL AS departmentCode  ";

                }
//                validateReceivedItems(sqlPreviousItems, sqlCurrentItems, valuationType, compareProperties_Mdl.getDocId(), compareProperties_Mdl.getSite_Code(), compareProperties_Mdl.getDepartmentCode(), previousReceiveDate, cnTemp);
                validateReceivedItems(sqlPreviousItems, sqlCurrentItems, valuationType, compareProperties_Mdl.getSite_Code(), previousReceiveDate, cnTemp);

            }

            if (!DmShared.xNull(sqlPreviousItemBatch).trim().equals("")) {
                if (DmShared.xNull(sqlCurrentItemBatch).trim().equals("")) {
                    sqlCurrentItemBatch = "SELECT Null AS itemCode "
                            + ", Null AS ibCode "
                            + ", 0 AS receiveQty "
                            + ", NULL AS departmentCode  ";

                }
//                validateReceivedItemBatch(true, sqlPreviousItemBatch, sqlCurrentItemBatch, valuationType, compareProperties_Mdl.getDocId(), compareProperties_Mdl.getSite_Code(), compareProperties_Mdl.getDepartmentCode(), previousReceiveDate, cnTemp);
                validateReceivedItemBatch(true, sqlPreviousItemBatch, sqlCurrentItemBatch, valuationType, compareProperties_Mdl.getSite_Code(), previousReceiveDate, cnTemp);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    private static void validateReceivedItems(String sqlPreviousItems, String sqlCurrentItems, String valuationType, String site_Code, String previousVDate, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery, tempVDate = previousVDate;
        try {
            //Select Decreased Receive Qty item Wise
            sqlQuery = "SELECT CASE WHEN vPi.itemCode IS NULL THEN vCi.itemCode ELSE vPi.itemCode END AS itemCode"
                    + " , CASE WHEN vPi.departmentCode IS NULL THEN vCi.departmentCode ELSE vPi.departmentCode END AS departmentCode "
                    + " , ISNULL(vPi.receiveQty,0) - ISNULL(vCi.receiveQty,0) AS receiveQty "
                    + " FROM ("
                    + "     SELECT pi.itemCode,pi.departmentCode ,SUM(pi.receiveQty) AS receiveQty "
                    + "     FROM (" + sqlPreviousItems + ") pi "
                    + "     GROUP BY pi.itemCode , pi.departmentCode"
                    + "     ) vPi "
                    + " FULL JOIN ("
                    + "     SELECT ci.itemCode,ci.departmentCode, SUM(ci.receiveQty) AS receiveQty "
                    + "     FROM (" + sqlCurrentItems + ") ci "
                    + "     GROUP BY ci.itemCode , ci.departmentCode "
                    + "     ) vCi ON vCi.itemCode = vPi.itemCode AND vCi.departmentCode = vPi.departmentCode  "
                    + " WHERE ISNULL(vPi.receiveQty,0) > ISNULL(vCi.receiveQty,0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(valuationType).equalsIgnoreCase("A")) {
                    sqlQuery = "SELECT MAX(stk.v_Date) AS v_Date "
                            + " FROM stock stk "
                            + " INNER JOIN stockFifo SF on sf.docId = stk.docId and sf.v_SNo = stk.v_SNo "
                            + " WHERE stk.itemCode = :itemCode "
                            + " AND stk.departmentCode = :departmentCode "
                            + " AND stk.site_Code = :site_Code "
                            + " AND sf.stockValueDate = :v_Date "
                            + " AND CASE WHEN ISNULL(sf.isuQty,0) > 0 OR ISNULL(sf.recQty,0) < 0  THEN 1 ELSE 0 END = 1 ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("itemCode", DmShared.xNull(rsTemp.getString("itemCode")));
                    psTemp1.setString("site_Code", DmShared.xNull(site_Code));
                    psTemp1.setDate("v_Date", DmServer.convertDate(DmShared.dNull(previousVDate)));
                    psTemp1.setString("departmentCode", DmShared.xNull(rsTemp.getString("departmentCode")));
                    rsTemp1 = psTemp1.executeQuery();
                    if (rsTemp1.next()) {
                        if (!DmShared.dNull(rsTemp1.getDate("v_Date")).equals("")) {
                            tempVDate = DmShared.dNull(rsTemp1.getDate("v_Date"));
                        }
                    }
                    rsTemp1.close();
                    psTemp1.close();
                }
                sqlQuery = "SELECT :receiveQty - ISNULL(SUM(vIStk.balanceQty),0) negativeQty, MAX(im.Name) AS itemName, MAX(um.unit) AS skuName "
                        + " FROM itemMast im "
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " LEFT JOIN ( "
                        + "     SELECT iStk.itemCode, ISNULL(SUM(iStk.balanceQty),0) balanceQty "
                        + "     FROM itemStock iStk  "
                        + "     WHERE iStk.itemCode = :itemCode "
                        + "     AND iStk.stockValueDate <= :v_Date "
                        + "     AND iStk.departmentCode = :departmentCode "
                        + "     AND iStk.site_Code = :site_Code "
                        + "     GROUP BY iStk.itemCode "
                        + "     ) vIStk ON im.code = vIStk.itemCode  "
                        + " WHERE im.code = :itemCode  "
                        + " GROUP BY im.code  "
                        + " HAVING CASE WHEN ISNULL(MAX(im.allowNegativeStock),0) = 0 AND ISNULL(SUM(vIStk.balanceQty),0) < :receiveQty THEN 1 ELSE 0 END = 1; ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("itemCode", DmShared.xNull(rsTemp.getString("itemCode")));
                psTemp1.setString("site_Code", DmShared.xNull(site_Code));
                psTemp1.setDate("v_Date", DmServer.convertDate(DmShared.dNull(tempVDate)));
                psTemp1.setString("departmentCode", DmShared.xNull(rsTemp.getString("departmentCode")));
                psTemp1.setDouble("receiveQty", DmShared.vNull(rsTemp.getString("receiveQty")));
                rsTemp1 = psTemp1.executeQuery();
                if (rsTemp1.next()) {
                    if (DmShared.vNull(rsTemp1.getString("negativeQty")).doubleValue() > 0) {
                        throw new DmException(0, "Transaction not allowed as this is generating negative stock for " + DmShared.xNull(rsTemp1.getString("itemName")) + " by  " + DmServer.vNull(rsTemp1.getString("negativeQty")) + "  " + DmShared.xNull(rsTemp1.getString("skuName")) + ".");
                    }
                }
                rsTemp1.close();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void validateReceivedItemBatch(boolean flagIbCode, String sqlPreviousItemBatch, String sqlCurrentItemBatch, String valuationType, String site_Code, String previousVDate, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery, tempVDate = previousVDate;

        try {
            //Select Decreased Receive Qty item batch Wise
            if (flagIbCode) {
                sqlQuery = "SELECT CASE WHEN vPi.itemCode IS NULL THEN vCi.itemCode ELSE vPi.itemCode END AS itemCode"
                        + " , CASE WHEN vPi.itemCode IS NULL THEN vCi.ibCode ELSE vPi.ibCode END AS ibCode "
                        + " , CASE WHEN vPi.departmentCode IS NULL THEN vCi.departmentCode ELSE vPi.departmentCode END AS departmentCode"
                        + " , ISNULL(vPi.receiveQty,0) - ISNULL(vCi.receiveQty,0) AS receiveQty "
                        + " FROM ("
                        + "     SELECT pi.itemCode,pi.departmentCode, pi.ibCode, SUM(pi.receiveQty) AS receiveQty "
                        + "     FROM (" + sqlPreviousItemBatch + ") pi "
                        + "     GROUP BY pi.departmentCode, pi.itemCode, pi.ibCode"
                        + "     ) vPi "
                        + " FULL JOIN ("
                        + "     SELECT ci.itemCode, ci.ibCode,ci.departmentCode, SUM(ci.receiveQty) AS receiveQty "
                        + "     FROM (" + sqlCurrentItemBatch + ") ci "
                        + "     GROUP BY ci.departmentCode, ci.itemCode, ci.ibCode"
                        + "     ) vCi ON vCi.ibCode = vPi.ibCode AND vCi.departmentCode = vPi.departmentCode "
                        + " WHERE ISNULL(vPi.receiveQty,0) > ISNULL(vCi.receiveQty,0) "
                        + " AND CASE WHEN vPi.itemCode IS NULL THEN vCi.ibCode ELSE vPi.ibCode END IS NOT NULL ";
            } else {
                sqlQuery = "SELECT CASE WHEN vPi.itemCode IS NULL THEN vCi.itemCode ELSE vPi.itemCode END AS itemCode"
                        + " , CASE WHEN vPi.departmentCode IS NULL THEN vCi.departmentCode ELSE vPi.departmentCode END AS departmentCode "
                        + " , ib.code AS ibCode "
                        + " , ISNULL(vPi.receiveQty,0) - ISNULL(vCi.receiveQty,0) AS receiveQty "
                        + " FROM ("
                        + "     SELECT pi.itemCode ,pi.barcode, pi.batchNo, pi.colourCode, pi.sizeCode "
                        + "     , pi.lotNo , pi.mrp, pi.skuMrp, pi.saleRate, pi.skuSaleRate, pi.staffSaleRate, pi.skuStaffSaleRate"
                        + "     , pi.expiryDate, pi.manufacturingDate"
                        + "     , pi.departmentCode "
                        + "     , SUM(pi.receiveQty) AS receiveQty "
                        + "     FROM (" + sqlPreviousItemBatch + ") pi "
                        + "     GROUP BY pi.departmentCode, pi.itemCode, pi.barcode, pi.batchNo, pi.colourCode, pi.sizeCode,pi.lotNo "
                        + "     , pi.mrp, pi.skuMrp, pi.saleRate, pi.skuSaleRate, pi.staffSaleRate, pi.skuStaffSaleRate"
                        + "     , pi.expiryDate, pi.manufacturingDate  "
                        + "     ) vPi "
                        + " FULL JOIN ("
                        + "     SELECT ci.itemCode, ci.barcode, ci.batchNo, ci.colourCode, ci.sizeCode "
                        + "     , ci.lotNo, ci.mrp, ci.skuMrp, ci.saleRate, ci.skuSaleRate, ci.staffSaleRate, ci.skuStaffSaleRate"
                        + "     , ci.expiryDate, ci.manufacturingDate"
                        + "     , ci.departmentCode "
                        + "     , SUM(ci.receiveQty) AS receiveQty "
                        + "     FROM (" + sqlCurrentItemBatch + ") ci "
                        + "     GROUP BY ci.departmentCode, ci.itemCode, ci.barcode, ci.batchNo, ci.colourCode, ci.sizeCode "
                        + "     , ci.lotNo, ci.mrp, ci.skuMrp, ci.saleRate, ci.skuSaleRate, ci.staffSaleRate, ci.skuStaffSaleRate"
                        + "     , ci.expiryDate, ci.manufacturingDate"
                        + "     ) vCi ON vCi.itemCode = vPi.itemCode AND vCi.departmentCode = vPi.departmentCode"
                        + "             AND ISNULL(vCi.barcode,'') = ISNULL(vPi.barcode,'') "
                        + "             AND ISNULL(vCi.batchNo,'') = ISNULL(vPi.batchNo,'') "
                        + "             AND ISNULL(vCi.colourCode,0) = ISNULL(vPi.colourCode,0) "
                        + "             AND ISNULL(vCi.sizeCode,0) = ISNULL(vPi.sizeCode,0) "
                        + "             AND ISNULL(vCi.LotNo,'') = ISNULL(vPi.LotNo,'') "
                        + "             AND ISNULL(vCi.mrp,0) = ISNULL(vPi.mrp,0) "
                        + "             AND ISNULL(vCi.skuMrp,0) = ISNULL(vPi.skuMrp,0) "
                        + "             AND ISNULL(vCi.saleRate,0) = ISNULL(vPi.saleRate,0) "
                        + "             AND ISNULL(vCi.skuSaleRate,0) = ISNULL(vPi.skuSaleRate,0) "
                        + "             AND ISNULL(vCi.staffSaleRate,0) = ISNULL(vPi.staffSaleRate,0) "
                        + "             AND ISNULL(vCi.skuStaffSaleRate,0) = ISNULL(vPi.skuStaffSaleRate,0) "
                        + "             AND ISNULL(vCi.expiryDate, CAST('01-Jan-1900' AS DATE)) = ISNULL(vPi.expiryDate, CAST('01-Jan-1900' AS DATE)) "
                        + "             AND ISNULL(vCi.manufacturingDate, CAST('01-Jan-1900' AS DATE)) = ISNULL(vPi.manufacturingDate, CAST('01-Jan-1900' AS DATE)) "
                        + " LEFT JOIN itemBatch ib ON ib.itemCode = CASE WHEN vPi.itemCode IS NULL THEN vCi.itemCode ELSE VPi.itemCode END "
                        + "             AND ISNULL(ib.barcode,'') = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.barcode,'') ELSE ISNULL(vPi.barcode,'') END "
                        + "             AND ISNULL(ib.batchNo,'') = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.batchNo,'') ELSE ISNULL(vPi.batchNo,'') END "
                        + "             AND ISNULL(ib.colourCode,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.colourCode,0) ELSE ISNULL(VPi.colourCode,0) END "
                        + "             AND ISNULL(ib.sizeCode,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.sizeCode,0) ELSE ISNULL(VPi.sizeCode,0) END "
                        + "             AND ISNULL(ib.LotNo,'') = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.LotNo,'') ELSE ISNULL(VPi.LotNo,'') END "
                        + "             AND ISNULL(ib.mrp,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.mrp,0) ELSE ISNULL(vPi.mrp,0) END "
                        + "             AND ISNULL(ib.skuMrp,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.skuMrp,0) ELSE ISNULL(vPi.skuMrp,0) END "
                        + "             AND ISNULL(ib.saleRate,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.saleRate,0) ELSE ISNULL(vPi.saleRate,0) END "
                        + "             AND ISNULL(ib.skuSaleRate,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.skuSaleRate,0) ELSE ISNULL(vPi.skuSaleRate,0) END "
                        + "             AND ISNULL(ib.staffSaleRate,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.staffSaleRate,0) ELSE ISNULL(vPi.staffSaleRate,0) END "
                        + "             AND ISNULL(ib.skuStaffSaleRate,0) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.skuStaffSaleRate,0) ELSE ISNULL(vPi.skuStaffSaleRate,0) END "
                        + "             AND ISNULL(ib.expiryDate,CAST('01-Jan-1900' AS DATE)) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.expiryDate, CAST('01-Jan-1900' AS DATE)) ELSE ISNULL(VPi.expiryDate, CAST('01-Jan-1900' AS DATE)) END "
                        + "             AND ISNULL(ib.manufacturingDate,CAST('01-Jan-1900' AS DATE)) = CASE WHEN vPi.itemCode IS NULL THEN ISNULL(vCi.manufacturingDate, CAST('01-Jan-1900' AS DATE)) ELSE ISNULL(VPi.manufacturingDate, CAST('01-Jan-1900' AS DATE)) END "
                        + " WHERE ISNULL(vPi.receiveQty,0) > ISNULL(vCi.receiveQty,0)"
                        + " AND ib.code IS NOT NULL ";
//
//             

            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                //itemStock:
                if (DmShared.xNull(valuationType).equalsIgnoreCase("A")) {
                    sqlQuery = "SELECT MAX(stk.v_Date) AS v_Date "
                            + " FROM stock stk "
                            + " INNER JOIN stockFifo SF on sf.docId = stk.docId and sf.v_SNo = stk.v_SNo "
                            + " WHERE stk.itemCode = :itemCode "
                            + " AND stk.ibCode = :ibCode "
                            + " AND stk.departmentCode = :departmentCode "
                            + " AND stk.site_Code = :site_Code "
                            + " AND sf.stockValueDate = :v_Date "
                            + " AND CASE WHEN ISNULL(sf.isuQty,0) > 0 OR ISNULL(sf.recQty,0) < 0  THEN 1 ELSE 0 END = 1 ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("itemCode", DmShared.xNull(rsTemp.getString("itemCode")));
                    psTemp1.setString("ibCode", DmShared.xNull(rsTemp.getString("ibCode")));
                    psTemp1.setString("site_Code", DmShared.xNull(site_Code));
                    psTemp1.setDate("v_Date", DmServer.convertDate(DmShared.dNull(previousVDate)));
                    psTemp1.setString("departmentCode", DmShared.xNull(rsTemp.getString("departmentCode")));
                    rsTemp1 = psTemp1.executeQuery();
                    if (rsTemp1.next()) {
                        if (!DmShared.dNull(rsTemp1.getDate("v_Date")).equals("")) {
                            tempVDate = DmShared.dNull(rsTemp1.getDate("v_Date"));
                        }
                    }
                    rsTemp1.close();
                    psTemp1.close();
                }
                sqlQuery = "SELECT :receiveQty - ISNULL(SUM(vIStk.balanceQty),0) negativeQty, MAX(im.Name) AS itemName, MAX(um.unit) AS skuName "
                        + " FROM itemMast im "
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " LEFT JOIN ( "
                        + "     SELECT iStk.itemCode, ISNULL(SUM(iStk.balanceQty),0) balanceQty "
                        + "     FROM itemStock iStk  "
                        + "     WHERE iStk.itemCode = :itemCode "
                        + "     AND iStk.ibCode = :ibCode "
                        + "     AND iStk.stockValueDate <= :v_Date "
                        + "     AND iStk.departmentCode = :departmentCode "
                        + "     AND iStk.site_Code = :site_Code "
                        + "     GROUP BY iStk.itemCode, iStk.ibCode "
                        + "     ) vIStk ON im.code = vIStk.itemCode  "
                        + " WHERE im.code = :itemCode  "
                        + " GROUP BY im.code  "
                        + " HAVING CASE WHEN ISNULL(MAX(im.allowNegativeStock),0) = 0 AND ISNULL(SUM(vIStk.balanceQty),0) < :receiveQty THEN 1 ELSE 0 END = 1; ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("itemCode", DmShared.xNull(rsTemp.getString("itemCode")));
                psTemp1.setString("ibCode", DmShared.xNull(rsTemp.getString("ibCode")));
                psTemp1.setString("site_Code", DmShared.xNull(site_Code));
                psTemp1.setDate("v_Date", DmServer.convertDate(DmShared.dNull(tempVDate)));
                psTemp1.setString("departmentCode", DmShared.xNull(rsTemp.getString("departmentCode")));
                psTemp1.setDouble("receiveQty", DmShared.vNull(rsTemp.getString("receiveQty")));
                rsTemp1 = psTemp1.executeQuery();
                if (rsTemp1.next()) {
                    if (DmShared.vNull(rsTemp1.getString("negativeQty")).doubleValue() > 0) {
                        throw new DmException(0, "Transaction not allowed as this is generating negative stock for " + DmShared.xNull(rsTemp1.getString("itemName")) + " by  " + DmServer.vNull(rsTemp1.getString("negativeQty")) + "  " + DmShared.xNull(rsTemp1.getString("skuName")) + ".");
                    }
                }
                rsTemp1.close();
                psTemp1.close();

                //itemBatch:
                sqlQuery = "SELECT :receiveQty - ISNULL(SUM(vIbs.balanceQty),0) negativeQty, MAX(im.Name) AS itemName, MAX(um.unit) AS skuName "
                        + " FROM itemMast im  "
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " LEFT JOIN ( "
                        + " 	SELECT ibs.itemCode, ISNULL(SUM(ibs.balanceQty),0) AS balanceQty "
                        + " 	FROM itemBatchStock ibs "
                        + " 	WHERE ibs.itemCode = :itemCode   "
                        + " 	AND ibs.departmentCode = :departmentCode   "
                        + " 	AND ibs.site_Code = :site_Code   "
                        + " 	AND ibs.ibCode = :ibCode  "
                        + " 	GROUP BY ibs.itemCode, ibs.departmentCode, ibs.site_Code, ibs.ibCode "
                        + " 	)  vIbs ON im.code = vIbs.itemCode   "
                        + " WHERE im.code = :itemCode  "
                        + " GROUP BY im.code "
                        + " HAVING CASE WHEN ISNULL(MAX(im.allowNegativeStock),0) = 0 AND ISNULL(SUM(vIbs.balanceQty),0) < :receiveQty THEN 1 ELSE 0 END = 1 ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("itemCode", DmShared.xNull(rsTemp.getString("itemCode")));
                psTemp1.setString("site_Code", DmShared.xNull(site_Code));
                psTemp1.setString("departmentCode", DmShared.xNull(rsTemp.getString("departmentCode")));
                psTemp1.setString("ibCode", DmShared.xNull(rsTemp.getString("ibCode")));
                psTemp1.setDouble("receiveQty", DmShared.vNull(rsTemp.getString("receiveQty")));
                rsTemp1 = psTemp1.executeQuery();
                if (rsTemp1.next()) {
                    if (DmShared.vNull(rsTemp1.getString("negativeQty")).doubleValue() > 0) {
                        throw new DmException(0, "Transaction not allowed as this is generating negative stock for " + DmShared.xNull(rsTemp1.getString("itemName")) + " by  " + DmServer.vNull(rsTemp1.getString("negativeQty")) + "  " + DmShared.xNull(rsTemp1.getString("skuName")) + ".");
                    }
                }
                rsTemp1.close();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String createItemBatch(ItemBatch_Mdl itemBatch_Mdl, Connection cnTemp, DmRSSPrivate rssTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";
        boolean isBatchable = false;
        try {
            returnValue = getItemBatch(itemBatch_Mdl, cnTemp);
            if (returnValue.trim().equals("")) {
                //Create New Item Batch
                if (DmShared.xNull(itemBatch_Mdl.getCode()).equalsIgnoreCase("")) {
                    itemBatch_Mdl.setCode(DmServer.getSerialNoBigInt(DmServer.getSsnSiteCode(rssTemp), DmServer.getSsnVoucherPrefix(rssTemp), "itemBatch", cnTemp));
                }
                if (itemBatch_Mdl.getCode().equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Invalid key.");
                }

                if (DmShared.xNull(itemBatch_Mdl.getBarcode()).trim().equals("")
                        && DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()).trim().equals("")
                        && DmShared.iNull(itemBatch_Mdl.getIpm().getColourCode()).intValue() == 0
                        && DmShared.iNull(itemBatch_Mdl.getIpm().getSizeCode()).intValue() == 0
                        && DmShared.xNull(itemBatch_Mdl.getIpm().getLotNo()).trim().equals("")
                        && DmShared.vNull(itemBatch_Mdl.getIpm().getMrp()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getIpm().getSkuMrp()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getSaleRate()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getSkuSaleRate()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getStaffSaleRate()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getSkuStaffSaleRate()).doubleValue() == 0
                        && DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                    isBatchable = false;
                } else {
                    isBatchable = true;
                }

                sqlQuery = "INSERT INTO itemBatch "
                        + "(code "
                        + ", itemCode "
                        + ", barcode "
                        + ", batchNo "
                        + ", manufacturingDate "
                        + ", expiryDate "
                        + ", colourCode "
                        + ", sizeCode "
                        + ", lotNo"
                        + ", mrp "
                        + ", saleRate "
                        + ", staffSaleRate "
                        + ", skuMrp "
                        + ", skuSaleRate"
                        + ", skuStaffSaleRate"
                        + ", isBatchable"
                        + " ) "
                        + "VALUES "
                        + "(:code "
                        + ", :itemCode "
                        + ", :barcode "
                        + ", :batchNo "
                        + ", :manufacturingDate "
                        + ", :expiryDate "
                        + ", :colourCode "
                        + ", :sizeCode "
                        + ", :lotNo"
                        + ", :mrp "
                        + ", :saleRate "
                        + ", :staffSaleRate "
                        + ", :skuMrp "
                        + ", :skuSaleRate "
                        + ", :skuStaffSaleRate "
                        + ", :isBatchable "
                        + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(itemBatch_Mdl.getCode()));
                psTemp.setString("itemCode", DmShared.foriegnKey(itemBatch_Mdl.getIpm().getItemCode()));
                psTemp.setString("barcode", DmShared.xNull(itemBatch_Mdl.getBarcode()));
                psTemp.setString("batchNo", DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()));
                psTemp.setDate("manufacturingDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getManufacturingDate()));
                psTemp.setDate("expiryDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getExpiryDate()));
                psTemp.setString("colourCode", DmShared.foriegnKey(itemBatch_Mdl.getIpm().getColourCode()));
                psTemp.setString("sizeCode", DmShared.foriegnKey(itemBatch_Mdl.getIpm().getSizeCode()));
                psTemp.setString("lotNo", DmShared.xNull(itemBatch_Mdl.getIpm().getLotNo()));
                psTemp.setString("mrp", DmServer.vNull(itemBatch_Mdl.getIpm().getMrp(), "0.00"));
                psTemp.setString("saleRate", DmServer.vNull(itemBatch_Mdl.getSaleRate(), "0.00"));
                psTemp.setString("staffSaleRate", DmServer.vNull(itemBatch_Mdl.getStaffSaleRate(), "0.00"));
                psTemp.setString("skuMrp", DmServer.vNull(itemBatch_Mdl.getIpm().getSkuMrp()));
                psTemp.setString("skuSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuSaleRate()));
                psTemp.setString("skuStaffSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuStaffSaleRate()));
                if (isBatchable) {
                    psTemp.setString("isBatchable", "1");
                } else {
                    psTemp.setString("isBatchable", "0");
                }
                psTemp.executeUpdate();
                psTemp.close();

                returnValue = DmShared.xNull(itemBatch_Mdl.getCode());
            }
        } catch (Exception e) {
            returnValue = "";
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

    public static String createItemBatch(ItemBatch_Mdl itemBatch_Mdl, Connection cnTemp, String siteCode, String v_Prefix) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";
        boolean isBatchable = false;
        try {
            returnValue = getItemBatch(itemBatch_Mdl, cnTemp);
            if (returnValue.trim().equals("")) {
                //Create New Item Batch
                if (DmShared.xNull(itemBatch_Mdl.getCode()).equalsIgnoreCase("")) {
                    itemBatch_Mdl.setCode(DmServer.getSerialNoBigInt(siteCode, v_Prefix, "itemBatch", cnTemp));
                }
                if (itemBatch_Mdl.getCode().equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Invalid key.");
                }

                if (DmShared.xNull(itemBatch_Mdl.getBarcode()).trim().equals("")
                        && DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()).trim().equals("")
                        && DmShared.iNull(itemBatch_Mdl.getIpm().getColourCode()).intValue() == 0
                        && DmShared.iNull(itemBatch_Mdl.getIpm().getSizeCode()).intValue() == 0
                        && DmShared.xNull(itemBatch_Mdl.getIpm().getLotNo()).trim().equals("")
                        && DmShared.vNull(itemBatch_Mdl.getIpm().getMrp()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getIpm().getSkuMrp()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getSaleRate()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getSkuSaleRate()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getStaffSaleRate()).doubleValue() == 0
                        && DmShared.vNull(itemBatch_Mdl.getSkuStaffSaleRate()).doubleValue() == 0
                        && DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                    isBatchable = false;
                } else {
                    isBatchable = true;
                }

                sqlQuery = "INSERT INTO itemBatch "
                        + "(code "
                        + ", itemCode "
                        + ", barcode "
                        + ", batchNo "
                        + ", manufacturingDate "
                        + ", expiryDate "
                        + ", colourCode "
                        + ", sizeCode "
                        + ", lotNo"
                        + ", mrp "
                        + ", saleRate "
                        + ", staffSaleRate "
                        + ", skuMrp "
                        + ", skuSaleRate"
                        + ", skuStaffSaleRate"
                        + ", isBatchable"
                        + " ) "
                        + "VALUES "
                        + "(:code "
                        + ", :itemCode "
                        + ", :barcode "
                        + ", :batchNo "
                        + ", :manufacturingDate "
                        + ", :expiryDate "
                        + ", :colourCode "
                        + ", :sizeCode "
                        + ", :lotNo"
                        + ", :mrp "
                        + ", :saleRate "
                        + ", :staffSaleRate "
                        + ", :skuMrp "
                        + ", :skuSaleRate "
                        + ", :skuStaffSaleRate "
                        + ", :isBatchable "
                        + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(itemBatch_Mdl.getCode()));
                psTemp.setString("itemCode", DmShared.foriegnKey(itemBatch_Mdl.getIpm().getItemCode()));
                psTemp.setString("barcode", DmShared.xNull(itemBatch_Mdl.getBarcode()));
                psTemp.setString("batchNo", DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()));
                psTemp.setDate("manufacturingDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getManufacturingDate()));
                psTemp.setDate("expiryDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getExpiryDate()));
                psTemp.setString("colourCode", DmShared.foriegnKey(itemBatch_Mdl.getIpm().getColourCode()));
                psTemp.setString("sizeCode", DmShared.foriegnKey(itemBatch_Mdl.getIpm().getSizeCode()));
                psTemp.setString("lotNo", DmShared.xNull(itemBatch_Mdl.getIpm().getLotNo()));
                psTemp.setString("mrp", DmServer.vNull(itemBatch_Mdl.getIpm().getMrp(), "0.00"));
                psTemp.setString("saleRate", DmServer.vNull(itemBatch_Mdl.getSaleRate(), "0.00"));
                psTemp.setString("staffSaleRate", DmServer.vNull(itemBatch_Mdl.getStaffSaleRate(), "0.00"));
                psTemp.setString("skuMrp", DmServer.vNull(itemBatch_Mdl.getIpm().getSkuMrp()));
                psTemp.setString("skuSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuSaleRate()));
                psTemp.setString("skuStaffSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuStaffSaleRate()));
                if (isBatchable) {
                    psTemp.setString("isBatchable", "1");
                } else {
                    psTemp.setString("isBatchable", "0");
                }
                psTemp.executeUpdate();
                psTemp.close();

                returnValue = DmShared.xNull(itemBatch_Mdl.getCode());
            }
        } catch (Exception e) {
            returnValue = "";
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

    public static String getItemBatchForIssue(ItemBatch_Mdl itemBatch_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, returnValue = "";
        try {

            //Select Existing Item Batch Code
            sqlQuery = "SELECT ib.code "
                    + " FROM itemBatch ib "
                    + " WHERE itemCode = :itemCode "
                    + " AND ISNULL(barcode,'') = :barcode "
                    + " AND ISNULL(batchNo,'') = :batchNo ";
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getManufacturingDate()).trim().equals("")) {
                sqlQuery += " AND manufacturingDate = :manufacturingDate ";
            } else {
                sqlQuery += " AND manufacturingDate IS NULL ";
            }
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                sqlQuery += " AND expiryDate = :expiryDate ";
            } else {
                sqlQuery += " AND expiryDate IS NULL ";
            }
            sqlQuery += " AND ISNULL(colourCode,0) = :colourCode "
                    + " AND ISNULL(sizeCode,0) = :sizeCode "
                    + " AND ISNULL(lotNo,'') = :lotNo "
                    + " AND ISNULL(mrp,0) = :mrp "
                    //                    + " AND ISNULL(docId,'') = :docId "
                    //                    + " AND ISNULL(v_SNo,0) = :v_SNo "
                    + " AND ISNULL(purchaseRateExFree,0) = :purchaseRateExFree "
                    + " AND ISNULL(purchaseTaxableRateExFree,0) = :purchaseTaxableRateExFree "
                    + " AND ISNULL(saleRate,0) = :saleRate "
                    + " AND ISNULL(staffSaleRate,0) = :staffSaleRate "
                    + " AND ISNULL(skuMrp,0) = :skuMrp "
                    + " AND ISNULL(skuSaleRate,0) = :skuSaleRate "
                    + " AND ISNULL(skuStaffSaleRate,0) = :skuStaffSaleRate ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemBatch_Mdl.getIpm().getItemCode()));
            psTemp.setString("barcode", DmShared.xNull(itemBatch_Mdl.getBarcode()));
            psTemp.setString("batchNo", DmShared.xNull(itemBatch_Mdl.getIpm().getBatchNo()));
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getManufacturingDate()).trim().equals("")) {
                psTemp.setDate("manufacturingDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getManufacturingDate()));
            }
            if (!DmShared.dNull(itemBatch_Mdl.getIpm().getExpiryDate()).trim().equals("")) {
                psTemp.setDate("expiryDate", DmServer.convertDate(itemBatch_Mdl.getIpm().getExpiryDate()));
            }
            psTemp.setString("colourCode", DmShared.iNull(itemBatch_Mdl.getIpm().getColourCode()).toString());
            psTemp.setString("sizeCode", DmShared.iNull(itemBatch_Mdl.getIpm().getSizeCode()).toString());
            psTemp.setString("lotNo", DmShared.xNull(itemBatch_Mdl.getIpm().getLotNo()));
            psTemp.setString("mrp", DmServer.vNull(itemBatch_Mdl.getIpm().getMrp(), "0.00"));
//            psTemp.setString("docId", DmShared.xNull(itemBatch_Mdl.getDocId()));
//            psTemp.setString("v_SNo", DmShared.iNull(itemBatch_Mdl.getvSno()).toString());
            psTemp.setString("purchaseRateExFree", DmServer.vNull(itemBatch_Mdl.getPurchaseRateExFree(), "0.00"));
            psTemp.setString("purchaseTaxableRateExFree", DmServer.vNull(itemBatch_Mdl.getPurchaseTaxableRateExFree(), "0.00"));
            psTemp.setString("saleRate", DmServer.vNull(itemBatch_Mdl.getSaleRate(), "0.00"));
            psTemp.setString("staffSaleRate", DmServer.vNull(itemBatch_Mdl.getStaffSaleRate(), "0.00"));
            psTemp.setString("skuMrp", DmServer.vNull(itemBatch_Mdl.getIpm().getSkuMrp()));
            psTemp.setString("skuSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuSaleRate()));
            psTemp.setString("skuStaffSaleRate", DmServer.vNull(itemBatch_Mdl.getSkuStaffSaleRate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                returnValue = DmShared.xNull(rsTemp.getString("code"));
                if (!returnValue.trim().equals("")) {
                    rsTemp.close();
                    psTemp.close();
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            returnValue = "";
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

}
