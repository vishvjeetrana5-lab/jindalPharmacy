package dataman.dmToolkit.stock.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmEnum;
import static dataman.dmBase.self.shared.DmEnum.StockEntryType.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmToolkit.stock.shared.ItemStock_Mdl;
import dataman.dmToolkit.stock.shared.LogFile_Mdl;
import dataman.dmToolkit.stock.shared.StockFifo_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock_Server {

    private static boolean flagRunFifoProcess = false;
//    private static String fullFilePath = "";

    public static void saveStock(Stock_Mdl stock_Mdl, DmEnum.StockEntryType entryType, Connection cnTemp) {
        Stock_Mdl smBase = null;
        String valuationType = "";
        boolean isStockTransferReceiveRecord = false;
        boolean isPhysicalStockRecord = false;
        boolean isItemReceive = false;
        try {
            smBase = new Stock_Mdl();
            stock_Mdl.copy(smBase);

            switch (entryType) {
                case STOCK_TRANSFER_RECEIVE:
                    isStockTransferReceiveRecord = true;
                    break;
                case ITEM_RECEIVE:
                    isItemReceive = true;
                    break;
                case PHYSICAL_STOCK_POSTING:
                case PHYSICAL_STOCK_CLEARANCE:
                    isPhysicalStockRecord = true;
                    break;
                default:
                    break;
            }
            if (isStockTransferReceiveRecord) {
                valuationType = "A";
            } else if (isItemReceive) {
                valuationType = "A";
            } else {
                smBase.setStockFifo_Mdl(null);
                if (DmShared.vNull(smBase.getIsuQty()).doubleValue() < 0 || DmShared.vNull(smBase.getRecQty()).doubleValue() > 0) {
                    valuationType = "RF";
                } else if (DmShared.vNull(smBase.getRecQty()).doubleValue() < 0 || DmShared.vNull(smBase.getIsuQty()).doubleValue() > 0) {
                    valuationType = "IF";
                }
            }

            if (!valuationType.trim().equals("")) {
//                saveStockRecord(smBase, valuationType, isStockTransferReceiveRecord, isPhysicalStockRecord, cnTemp);
                saveStockRecord(smBase, valuationType, isStockTransferReceiveRecord, cnTemp);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void deleteStock(String stockDocid, Connection cnTemp) {
        deleteStock(stockDocid, cnTemp, false);
    }

    public static void deleteStock(String stockDocid, Connection cnTemp, boolean flagIsDeleteFromEdit) {
        String sqlQuery, tempVDate = "";
        DmPreparedStatement psTemp = null, psTemp1 = null, psTempData = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        HashMap<Integer, String[]> params = new HashMap<>();
        int sr = 0, index_ItemCode = 0, index_DepartmentCode = 1, index_SiteCode = 2, index_IbCode = 3;
        double tempReceiveQty = 0;
        try {
            if (!flagIsDeleteFromEdit) {
                //Select Receive Qty in stock item/ibCode Wise
                sqlQuery = "SELECT MAX(stk.v_Date) AS v_Date, stk.itemCode, stk.ibCode, stk.site_Code, stk.departmentCode "
                        + " , CASE WHEN ISNULL(SUM(stk.recQty),0) > 0 THEN ISNULL(SUM(stk.recQty),0) ELSE 0 END "
                        + " 	+ CASE WHEN ISNULL(SUM(stk.IsuQty),0) < 0 THEN ABS(ISNULL(SUM(stk.IsuQty),0)) ELSE 0 END as receiveQty"
                        + " , CASE WHEN (ISNULL(MAX(vt.parentVoucher),'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive')) THEN 'A'"
                        + "         WHEN ISNULL(MAX(vt.parentVoucher),'') = 'Stock Transfer' THEN CASE WHEN ISNULL(MAX(stk.isSystemGenerated),0) = 0 THEN NULL ELSE 'A' END "
                        + "         WHEN ISNULL(MAX(vt.parentVoucher),'') = 'Physical Stock' THEN CASE WHEN (ISNULL(SUM(stk.recQty),0) > 0 OR ISNULL(SUM(stk.isuQty),0) < 0) THEN 'A' ELSE NULL END "
                        + "         ELSE 'RF' "
                        + "         END AS valuationType "
                        + " FROM stock stk "
                        + " INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type "
                        + " WHERE (ISNULL(stk.recQty,0) > 0 OR ISNULL(stk.isuQty,0) < 0) "
                        + " AND stk.docId = :docId "
                        + " GROUP BY stk.itemCode, stk.ibCode, stk.site_Code, stk.departmentCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(stockDocid).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    tempVDate = DmShared.dNull(rsTemp.getDate("v_Date"));
                    if (DmShared.xNull(rsTemp.getString("valuationType")).equalsIgnoreCase("A")) {
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
                        psTemp1.setString("site_Code", DmShared.xNull(rsTemp.getString("site_Code")));
                        psTemp1.setDate("v_Date", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("v_Date"))));
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
                    psTemp1.setString("site_Code", DmShared.xNull(rsTemp.getString("site_Code")));
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

                //Select Receive Qty in stock item Batch Wise
                sqlQuery = "SELECT stk.itemCode, stk.site_Code, stk.departmentCode, stk.ibCode, stk.recQty, stk.IsuQty "
                        + " FROM stock stk "
                        + " WHERE (ISNULL(stk.recQty,0) > 0 OR ISNULL(stk.isuQty,0) < 0) "
                        + " AND stk.docId = :docId "
                        + " AND stk.ibCode IS NOT NULL ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(stockDocid).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("ibCode")).longValue() > 0) {
                        tempReceiveQty = 0;

                        if (DmShared.vNull(rsTemp.getString("recQty")).doubleValue() > 0) {
                            tempReceiveQty = DmShared.vNull(rsTemp.getString("recQty")).doubleValue();
                        } else if (DmShared.vNull(rsTemp.getString("IsuQty")).doubleValue() < 0) {
                            tempReceiveQty = Math.abs(DmShared.vNull(rsTemp.getString("IsuQty")).doubleValue());
                        }

                        if (tempReceiveQty > 0) {
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
                            psTemp1.setString("site_Code", DmShared.xNull(rsTemp.getString("site_Code")));
                            psTemp1.setString("departmentCode", DmShared.xNull(rsTemp.getString("departmentCode")));
                            psTemp1.setString("ibCode", DmShared.xNull(rsTemp.getString("ibCode")));
                            psTemp1.setDouble("receiveQty", tempReceiveQty);
                            rsTemp1 = psTemp1.executeQuery();
                            if (rsTemp1.next()) {
                                if (DmShared.vNull(rsTemp1.getString("negativeQty")).doubleValue() > 0) {
                                    throw new DmException(0, "Transaction not allowed as this is generating negative stock for " + DmShared.xNull(rsTemp1.getString("itemName")) + " by  " + DmServer.vNull(rsTemp1.getString("negativeQty")) + "  " + DmShared.xNull(rsTemp1.getString("skuName")) + ".");
                                }
                            }
                            rsTemp1.close();
                            psTemp1.close();
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = "SELECT stk.itemCode, stk.ibCode, stk.departmentCode, stk.site_Code, sf.stockValueDate "
                    + " FROM stock stk "
                    + " LEFT JOIN stockFifo sf ON sf.docId = stk.docId AND sf.v_SNo = stk.v_Sno "
                    + " WHERE stk.docId = :docId "
                    + " ORDER BY stk.v_SNo ";
            psTempData = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psTempData.setString("docId", DmShared.iNull(stockDocid).toString());
            rsTemp = psTempData.executeQuery();

            sr = 0;
            while (rsTemp.next()) {
                //Insert Record In itemStock Table
                manageItemStock(DmShared.xNull(rsTemp.getString("itemCode")), DmShared.xNull(rsTemp.getString("ibCode")), DmShared.xNull(rsTemp.getString("departmentCode")), DmShared.xNull(rsTemp.getString("site_Code")), DmShared.dNull(rsTemp.getDate("stockValueDate")), stockDocid, cnTemp);

                //Update itemBatchStock Table
                updateItemBatchStock(DmShared.xNull(rsTemp.getString("ibCode")), DmShared.xNull(rsTemp.getString("itemCode")), DmShared.xNull(rsTemp.getString("departmentCode")), DmShared.xNull(rsTemp.getString("site_Code")), stockDocid, cnTemp);

                //Fill Values in Hashmap
                String[] stockValues = new String[4];
                stockValues[index_ItemCode] = DmShared.xNull(rsTemp.getString("itemCode"));
                stockValues[index_IbCode] = DmShared.xNull(rsTemp.getString("ibCode"));
                stockValues[index_DepartmentCode] = DmShared.xNull(rsTemp.getString("departmentCode"));
                stockValues[index_SiteCode] = DmShared.xNull(rsTemp.getString("site_Code"));
                params.put(++sr, stockValues);
            }

            sqlQuery = "Delete From stockFifo "
                    + " Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(stockDocid).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From stock "
                    + " Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(stockDocid).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //Run FIFO Process            
            if (!flagIsDeleteFromEdit) {
                if (flagRunFifoProcess) {
                    if (params != null) {
                        for (Map.Entry<Integer, String[]> param : params.entrySet()) {
//                            runStockFIFO_Process("", DmShared.xNull(param.getValue()[index_ItemCode]), DmShared.xNull(param.getValue()[index_IbCode]), DmShared.xNull(param.getValue()[index_DepartmentCode]), DmShared.xNull(param.getValue()[index_SiteCode]), cnTemp, true);
                            runStockFIFO_Process("", DmShared.xNull(param.getValue()[index_ItemCode]), DmShared.xNull(param.getValue()[index_IbCode]), DmShared.xNull(param.getValue()[index_DepartmentCode]), DmShared.xNull(param.getValue()[index_SiteCode]), cnTemp, true, "");
                        }
                    }
                }
            }
            rsTemp.close();
            psTempData.close();

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
                if (psTempData != null) {
                    psTempData.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void issueItemStock(Stock_Mdl smBase, Connection cnTemp, boolean showNegativeError, String logFilePath) {
        String sqlQuery, itemName = "", skuName = "";
        int decimalPlaces = 4;
        DmPreparedStatement psTemp = null, psStock = null;
        ResultSet rsTemp = null, rsStock = null;
        StockFifo_Mdl sfNode;
        List<StockFifo_Mdl> listStockFifo = new ArrayList<>();
        double totalAdjustQty = 0, totalBalanceQty = 0, currentIssueQty = 0, previousIssuedQty = 0;
        double currentAdjustQty = 0, previousAdjustQty = 0, currentBalanceQty = 0, previousBalanceQty = 0;
        double receiveQty = 0, tempReceiveQty = 0;
        boolean isNegativeStockAllow = false, flagReceiveQty = false;

        try {

            if (DmShared.vNull(smBase.getIsuQty()).doubleValue() > 0) {
                currentIssueQty = DmShared.vNull(smBase.getIsuQty()).doubleValue();
            } else if (DmShared.vNull(smBase.getRecQty()).doubleValue() < 0) {
                flagReceiveQty = true;
                currentIssueQty = Math.abs(DmShared.vNull(smBase.getRecQty()).doubleValue());
            }

            sqlQuery = "SELECT SUM(iStk.balanceQty) AS issuedQty"
                    + "  FROM itemStock iStk "
                    + " WHERE iStk.stockValueDate <= :stockValueDate"
                    + " AND iStk.itemCode = :itemCode "
                    + " AND iStk.ibCode = :ibCode "
                    + " AND iStk.departmentCode = :departmentCode "
                    + " AND iStk.site_Code = :site_Code "
                    + "	AND ISNULL(iStk.receiveQty,0) < 0 "
                    + " AND ISNULL(iStk.balanceQty,0) < 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("stockValueDate", DmServer.convertDate(smBase.getV_Date()));
            psTemp.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
            psTemp.setString("ibCode", DmShared.xNull(smBase.getIbCode()));
            psTemp.setString("departmentCode", DmShared.xNull(smBase.getDepartmentCode()));
            psTemp.setString("site_Code", DmShared.xNull(smBase.getSite_Code()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                previousIssuedQty = Math.abs(DmShared.vNull(rsTemp.getString("issuedQty")).doubleValue());
            }

            currentBalanceQty = currentIssueQty;
            previousBalanceQty = previousIssuedQty;
            totalBalanceQty = currentIssueQty + previousIssuedQty;

            sqlQuery = "SELECT viStk.stockValueDate, viStk.balanceQty, viStk.avgSKURate, viStk.avgLandedSKURate "
                    + " FROM ( "
                    + " 	SELECT iStk.stockValueDate, iStk.balanceQty, iStk.avgSKURate, iStk.avgLandedSKURate "
                    + " 	, SUM(iStk.balanceQty) OVER (ORDER BY iStk.StockValueDate) AS runningTotal "
                    + " 	FROM itemStock iStk "
                    + " 	WHERE iStk.stockValueDate <= :stockValueDate "
                    + " 	AND iStk.itemCode = :itemCode "
                    + " 	AND iStk.ibCode = :ibCode "
                    + " 	AND iStk.departmentCode = :departmentCode "
                    + " 	AND iStk.site_Code = :site_Code "
                    + " 	AND ISNULL(iStk.balanceQty,0) > 0 "
                    + " 	) viStk "
                    + " WHERE ISNULL(viStk.runningTotal,0) <= (ISNULL(viStk.balanceQty,0) + :isuQty) "
                    + " ORDER BY viStk.stockValueDate ";
            psStock = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psStock.setDate("stockValueDate", DmServer.convertDate(smBase.getV_Date()));
            psStock.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
            psStock.setString("ibCode", DmShared.xNull(smBase.getIbCode()));
            psStock.setString("departmentCode", DmShared.xNull(smBase.getDepartmentCode()));
            psStock.setString("site_Code", DmShared.xNull(smBase.getSite_Code()));
            psStock.setDouble("isuQty", totalBalanceQty);
            rsStock = psStock.executeQuery();

            while (rsStock.next()) {
                receiveQty = DmShared.vNull(rsStock.getString("balanceQty")).doubleValue();
                tempReceiveQty = DmShared.vNull(rsStock.getString("balanceQty")).doubleValue();

                while (totalBalanceQty > 0 && receiveQty > 0) {
                    if (receiveQty >= totalBalanceQty) {
                        totalAdjustQty = totalBalanceQty;
                        receiveQty -= totalAdjustQty;
                        totalBalanceQty = 0;
                    } else {
                        totalAdjustQty = receiveQty;
                        receiveQty -= totalAdjustQty;
                        totalBalanceQty -= totalAdjustQty;
                    }

                    if (previousBalanceQty > 0 && tempReceiveQty > 0) {
                        if (tempReceiveQty >= previousBalanceQty) {
                            previousAdjustQty = previousBalanceQty;
                            tempReceiveQty -= previousAdjustQty;
                            previousBalanceQty = 0;
                        } else {
                            previousAdjustQty = tempReceiveQty;
                            tempReceiveQty -= previousAdjustQty;
                            previousBalanceQty -= previousAdjustQty;
                        }
                    } else {
                        previousAdjustQty = 0;
                    }

                    currentAdjustQty = totalAdjustQty - previousAdjustQty;

                    if (currentAdjustQty > 0) {
                        sfNode = new StockFifo_Mdl();
                        sfNode.setSharedKeyFieldCode(smBase.getSharedKeyFieldCode());
                        sfNode.setSharedKeyFieldName(smBase.getSharedKeyFieldName());
                        sfNode.setSno("");
                        sfNode.setStockValueDate(DmShared.dNull(rsStock.getDate("stockValueDate")));
                        sfNode.setAvgSKURate(DmServer.vNull(rsStock.getString("avgSKURate")));
                        sfNode.setAvgLandedSKURate(DmServer.vNull(rsStock.getString("avgLandedSKURate")));
                        if (flagReceiveQty) {
                            sfNode.setRecQty(DmServer.vNull(currentAdjustQty * -1));
                        } else {
                            sfNode.setIsuQty(DmServer.vNull(currentAdjustQty));
                        }

                        listStockFifo.add(sfNode);
                    }
                }
            }

            currentBalanceQty = totalBalanceQty - previousBalanceQty;

            if (currentBalanceQty > 0) {
                sqlQuery = "SELECT im.name AS itemName, um.unit as skuName, um.decimalPlaces"
                        + ", ISNULL(im.allowNegativeStock,0) As allowNegativeStock"
                        + " FROM itemMast im "
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " WHERE im.code = :itemCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    itemName = DmShared.xNull(rsTemp.getString("itemName"));
                    skuName = DmShared.xNull(rsTemp.getString("skuName"));
                    decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();
                    if (DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1) {
                        isNegativeStockAllow = true;
                    }
                }
                rsTemp.close();
                psTemp.close();

                boolean stockFifoFlag = true;
                if (!isNegativeStockAllow) {
                    if (showNegativeError) {
                        stockFifoFlag = false;
                        throw new DmException(0, "Entry not allowed!<br><b>" + itemName + "</b> is generating negative stock by " + DmServer.vNull(DmServer.vNull(totalBalanceQty, DmShared.getNumberFormat(decimalPlaces))) + " " + skuName + ".");
                    } else {
                        stockFifoFlag = true;
                        if (!DmShared.xNull(logFilePath).trim().equalsIgnoreCase("")) {
                            String message = itemName + ": Transaction No. " + smBase.getRecId() + " of dated " + smBase.getV_Date() + " is generating negative stock by " + DmServer.vNull(DmServer.vNull(totalBalanceQty, DmShared.getNumberFormat(decimalPlaces))) + " " + skuName + ".";
                            DmHandelFile handelFileLog = new DmHandelFile(logFilePath, true);
                            handelFileLog.writeLine(message);
                            handelFileLog.writeLine("");
                            handelFileLog.close();
                        }

                    }
                } else {
                    stockFifoFlag = true;
                }

                if (stockFifoFlag) {
                    sqlQuery = "SELECT iStk.stockValueDate, iStk.balanceQty, iStk.avgSKURate, iStk.avgLandedSKURate, iStk.receiveQty "
                            + " FROM itemStock iStk "
                            + " WHERE iStk.stockValueDate = :stockValueDate "
                            + " AND iStk.itemCode = :itemCode "
                            + " AND iStk.ibCode = :ibCode "
                            + " AND iStk.departmentCode = :departmentCode "
                            + " AND iStk.site_Code = :site_Code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("stockValueDate", DmServer.convertDate(smBase.getV_Date()));
                    psTemp.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
                    psTemp.setString("ibCode", DmShared.xNull(smBase.getIbCode()));
                    psTemp.setString("departmentCode", DmShared.xNull(smBase.getDepartmentCode()));
                    psTemp.setString("site_Code", DmShared.xNull(smBase.getSite_Code()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        sfNode = new StockFifo_Mdl();
                        sfNode.setSharedKeyFieldCode(smBase.getSharedKeyFieldCode());
                        sfNode.setSharedKeyFieldName(smBase.getSharedKeyFieldName());
                        sfNode.setSno("");

                        //if (DmShared.vNull(rsTemp.getString("receiveQty")) == 0) {
                        sfNode.setStockValueDate(DmShared.dNull(rsTemp.getDate("stockValueDate")));
                        sfNode.setAvgSKURate(DmServer.vNull(rsTemp.getString("avgSKURate")));
                        sfNode.setAvgLandedSKURate(DmServer.vNull(rsTemp.getString("avgLandedSKURate")));
                        //}
                        if (flagReceiveQty) {
                            sfNode.setRecQty(DmServer.vNull(currentBalanceQty * -1));
                        } else {
                            sfNode.setIsuQty(DmServer.vNull(currentBalanceQty));
                        }
                        listStockFifo.add(sfNode);

                    } else {
                        //Create New ItemStock
                        ItemStock_Mdl itemStock_Mdl = new ItemStock_Mdl();

                        itemStock_Mdl.setItemCode(DmShared.xNull(smBase.getItemCode()));
                        itemStock_Mdl.setIbCode(DmShared.xNull(smBase.getIbCode()));
                        itemStock_Mdl.setDepartmentCode(DmShared.xNull(smBase.getDepartmentCode()));
                        itemStock_Mdl.setSite_Code(DmShared.xNull(smBase.getSite_Code()));
                        itemStock_Mdl.setStockValueDate(DmShared.dNull(smBase.getV_Date()));
                        saveItemStock(itemStock_Mdl, cnTemp);

                        sfNode = new StockFifo_Mdl();
                        sfNode.setSharedKeyFieldCode(smBase.getSharedKeyFieldCode());
                        sfNode.setSharedKeyFieldName(smBase.getSharedKeyFieldName());
                        sfNode.setSno("");
                        sfNode.setStockValueDate(smBase.getV_Date());
                        if (flagReceiveQty) {
                            sfNode.setRecQty(DmServer.vNull(currentBalanceQty * -1));
                        } else {
                            sfNode.setIsuQty(DmServer.vNull(currentBalanceQty));
                        }

                        listStockFifo.add(sfNode);
                    }
                    rsTemp.close();
                    psTemp.close();
                }
            }

            Stock_Mdl smNode = new Stock_Mdl();
            smBase.copy(smNode);

            for (StockFifo_Mdl stockFifo_Mdl : listStockFifo) {
                if (DmShared.vNull(stockFifo_Mdl.getIsuQty()).doubleValue() != 0 || DmShared.vNull(stockFifo_Mdl.getRecQty()).doubleValue() != 0) {
                    saveStockFifo(stockFifo_Mdl, cnTemp);

                    //manage itemStock Table
                    manageItemStock(DmShared.xNull(smBase.getItemCode()), DmShared.xNull(smBase.getIbCode()), DmShared.xNull(smBase.getDepartmentCode()), DmShared.xNull(smBase.getSite_Code()), DmShared.dNull(stockFifo_Mdl.getStockValueDate()), "", cnTemp);
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psStock != null) {
                    psStock.close();
                }
            } catch (Exception e) {
            }
        }
    }

//    private static void receiveItemStock(Stock_Mdl smBase, Connection cnTemp) {
    private static void receiveItemStock(Stock_Mdl smBase, Connection cnTemp, String logFilePath) {
        String sqlQuery;
        DmPreparedStatement psTemp = null, psStock = null;
        ResultSet rsTemp = null, rsStock = null;
        StockFifo_Mdl sfNode;
        List<StockFifo_Mdl> listStockFifo = new ArrayList<>();
        double adjustQty = 0, balanceQty = 0, issueQty = 0, recQty = 0;
        boolean flagIssueQty = false, flagIsItemStockRecord = false, flagLogFile = false;
        String itemName = "", sku = "", decimalPlaces = "";
        try {

            if (DmShared.vNull(smBase.getRecQty()).doubleValue() > 0) {
                recQty = DmShared.vNull(smBase.getRecQty()).doubleValue();
            } else if (DmShared.vNull(smBase.getIsuQty()).doubleValue() < 0) {
                flagIssueQty = true;
                recQty = Math.abs(DmShared.vNull(smBase.getIsuQty()).doubleValue());
            }

            balanceQty = recQty;

            sqlQuery = "SELECT viStk.stockValueDate, viStk.issueQty, viStk.avgSKURate, viStk.avgLandedSKURate "
                    + " FROM (  "
                    + "    SELECT iStk.stockValueDate "
                    + "     , ISNULL(iStk.receiveQty,0) - ISNULL(iStk.balanceQty,0) AS issueQty "
                    + "     , iStk.avgSKURate, iStk.avgLandedSKURate  "
                    + "     , SUM((ISNULL(iStk.receiveQty,0) - ISNULL(iStk.balanceQty,0))) OVER (ORDER BY stk.v_Date DESC, vt.serialNo DESC, vt.short_Name DESC, stk.v_NO DESC, stk.recId DESC, stk.v_SNo DESC) AS runningTotal  "
                    + "     FROM stock stk "
                    + "     INNER JOIN ("
                    + "                 SELECT sf.docId, sf.v_SNo, sf.stockValueDate "
                    + "                 FROM stockFifo sf "
                    + "                 GROUP BY sf.docId, sf.v_SNo, sf.stockValueDate "
                    + "                 ) vSf ON vSf.docId = stk.docId AND vSf.v_SNo = stk.v_SNo "
                    + "     INNER JOIN itemStock iStk ON iStk.itemCode = stk.itemCode "
                    + "                                 AND iStk.ibCode = stk.ibCode "
                    + "                                 AND iStk.departmentCode = stk.departmentCode "
                    + "                                 AND iStk.site_Code = stk.site_Code "
                    + "                                 AND vSf.stockValueDate = iStk.stockValueDate  "
                    + "     INNER JOIN voucher_Type vt  ON vt.v_Type = stk.v_Type "
                    + "     WHERE stk.v_Date <= :v_Date  "
                    + "     AND iStk.itemCode = :itemCode "
                    + "     AND iStk.ibCode = :ibCode "
                    + "     AND iStk.departmentCode = :departmentCode "
                    + "     AND iStk.site_Code = :site_Code"
                    + "     AND (ISNULL(stk.recQty,0) < 0 OR ISNULL(stk.isuQty,0) > 0) "
                    + "     AND (ISNULL(iStk.receiveQty,0) - ISNULL(iStk.balanceQty,0)) > 0  "
                    + "    ) viStk  "
                    + " WHERE ISNULL(viStk.runningTotal,0) <= (ISNULL(viStk.issueQty,0) +  :recQty)"
                    + " ORDER BY viStk.stockValueDate DESC ";

            psStock = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psStock.setDate("v_Date", DmServer.convertDate(smBase.getV_Date()));
            psStock.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
            psStock.setString("ibCode", DmShared.xNull(smBase.getIbCode()));
            psStock.setString("departmentCode", DmShared.xNull(smBase.getDepartmentCode()));
            psStock.setString("site_Code", DmShared.xNull(smBase.getSite_Code()));
            psStock.setDouble("recQty", recQty);
            rsStock = psStock.executeQuery();

            while (rsStock.next()) {
                issueQty = DmShared.vNull(rsStock.getString("issueQty")).doubleValue();

                while (balanceQty > 0 && issueQty > 0) {
                    if (issueQty >= balanceQty) {
                        adjustQty = balanceQty;
                        issueQty -= adjustQty;
                        balanceQty = 0;
                    } else {
                        adjustQty = issueQty;
                        issueQty -= adjustQty;
                        balanceQty -= adjustQty;
                    }

                    sfNode = new StockFifo_Mdl();
                    sfNode.setSharedKeyFieldCode(smBase.getSharedKeyFieldCode());
                    sfNode.setSharedKeyFieldName(smBase.getSharedKeyFieldName());
                    sfNode.setSno("");
                    sfNode.setStockValueDate(DmShared.dNull(rsStock.getDate("stockValueDate")));
                    sfNode.setAvgSKURate(DmServer.vNull(rsStock.getString("avgSKURate")));
                    sfNode.setAvgLandedSKURate(DmServer.vNull(rsStock.getString("avgLandedSKURate")));
                    if (flagIssueQty) {
                        sfNode.setIsuQty(DmServer.vNull(adjustQty * -1));
                    } else {
                        sfNode.setRecQty(DmServer.vNull(adjustQty));
                    }

                    listStockFifo.add(sfNode);
                }
            }

            if (balanceQty > 0) {
                double tempSkuSaleRate = 0, tempLandedSkuSaleRate = 0, tempSkuSaleValue = 0, tempLandedSkuSaleValue = 0;
                double tempAvgSKURate = 0, tempAvgLandedSKURate = 0, tempReceiveQty = 0;
                flagIsItemStockRecord = false;
                if (recQty > 0) {
                    if (Math.abs(DmShared.vNull(smBase.getSkuSaleValue()).doubleValue()) > 0
                            || Math.abs(DmShared.vNull(smBase.getLandedSkuSaleValue()).doubleValue()) > 0) {
                        tempSkuSaleRate = Math.abs(DmShared.vNull(smBase.getSkuSaleValue()).doubleValue()) / recQty;
                        tempLandedSkuSaleRate = Math.abs(DmShared.vNull(smBase.getLandedSkuSaleValue()).doubleValue()) / recQty;
                        tempSkuSaleValue = balanceQty * tempSkuSaleRate;
                        tempLandedSkuSaleValue = balanceQty * tempLandedSkuSaleRate;
                        flagIsItemStockRecord = true;
                    }
                }

                sqlQuery = "SELECT im.name as itemName, um.unit as skuName, um.decimalPlaces "
                        + " FROM itemMast im "
                        + " LEFT JOIN unitMast um  ON um.code = im.sku "
                        + " WHERE im.code = :itemCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    itemName = DmShared.xNull(rsTemp.getString("itemName"));
                    sku = DmShared.xNull(rsTemp.getString("skuName"));
                    decimalPlaces = DmShared.xNull(rsTemp.getString("decimalPlaces"));
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "SELECT iStk.stockValueDate, iStk.avgSKURate, iStk.avgLandedSKURate, iStk.receiveQty, iStk.isReCalculate "
                        + " FROM itemStock iStk "
                        + " WHERE iStk.stockValueDate = :stockValueDate "
                        + " AND iStk.itemCode = :itemCode "
                        + " AND iStk.ibCode = :ibCode "
                        + " AND iStk.departmentCode = :departmentCode  "
                        + " AND iStk.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                psTemp.setDate("stockValueDate", DmServer.convertDate(smBase.getV_Date()));
                psTemp.setString("itemCode", DmShared.xNull(smBase.getItemCode()));
                psTemp.setString("ibCode", DmShared.xNull(smBase.getIbCode()));
                psTemp.setString("departmentCode", DmShared.xNull(smBase.getDepartmentCode()));
                psTemp.setString("site_Code", DmShared.xNull(smBase.getSite_Code()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {

                    if (DmShared.vNull(rsTemp.getString("receiveQty")) == 0) {
                        flagLogFile = true;
                    }

                    if (Math.abs(DmShared.vNull(smBase.getSkuSaleValue()).doubleValue()) > 0
                            || Math.abs(DmShared.vNull(smBase.getLandedSkuSaleValue()).doubleValue()) > 0) {

                        tempReceiveQty = balanceQty + DmShared.vNull(rsTemp.getString("receiveQty")).doubleValue();

                        if (DmShared.vNull(rsTemp.getString("receiveQty")).doubleValue() > 0) {
                            tempAvgSKURate = (tempSkuSaleValue + (DmShared.vNull(rsTemp.getString("avgSKURate")).doubleValue() * DmShared.vNull(rsTemp.getString("receiveQty")).doubleValue())) / tempReceiveQty;
                            tempAvgLandedSKURate = (tempLandedSkuSaleValue + (DmShared.vNull(rsTemp.getString("avgLandedSKURate")).doubleValue() * DmShared.vNull(rsTemp.getString("receiveQty")).doubleValue())) / tempReceiveQty;
                        } else {
                            tempAvgSKURate = tempSkuSaleRate;
                            tempAvgLandedSKURate = tempLandedSkuSaleRate;
                        }

                        rsTemp.updateDouble("receiveQty", tempReceiveQty);
                        rsTemp.updateDouble("avgSKURate", tempAvgSKURate);
                        rsTemp.updateDouble("avgLandedSKURate", tempAvgLandedSKURate);
                        rsTemp.updateInt("isReCalculate", 1);
                        rsTemp.updateRow();
                    } else {
                        tempAvgSKURate = DmShared.vNull(rsTemp.getString("avgSKURate")).doubleValue();
                        tempAvgLandedSKURate = DmShared.vNull(rsTemp.getString("avgLandedSKURate")).doubleValue();
                    }

                    sfNode = new StockFifo_Mdl();
                    sfNode.setSharedKeyFieldCode(smBase.getSharedKeyFieldCode());
                    sfNode.setSharedKeyFieldName(smBase.getSharedKeyFieldName());
                    sfNode.setSno("");
                    sfNode.setStockValueDate(DmShared.dNull(rsTemp.getDate("stockValueDate")));
                    sfNode.setAvgSKURate(DmServer.vNull(tempAvgSKURate));
                    sfNode.setAvgLandedSKURate(DmServer.vNull(tempAvgLandedSKURate));

                    if (flagIssueQty) {
                        sfNode.setIsuQty(DmServer.vNull(balanceQty * -1));
                    } else {
                        sfNode.setRecQty(DmServer.vNull(balanceQty));
                    }

                    if (flagIsItemStockRecord) {
                        sfNode.setIsItemStockRecord("1");
                    } else {
                        sfNode.setIsItemStockRecord("0");
                    }

                    listStockFifo.add(sfNode);
                } else {

                    flagLogFile = true;
                    //Create New ItemStock
                    ItemStock_Mdl itemStock_Mdl = new ItemStock_Mdl();

                    itemStock_Mdl.setItemCode(DmShared.xNull(smBase.getItemCode()));
                    itemStock_Mdl.setIbCode(DmShared.xNull(smBase.getIbCode()));
                    itemStock_Mdl.setDepartmentCode(DmShared.xNull(smBase.getDepartmentCode()));
                    itemStock_Mdl.setSite_Code(DmShared.xNull(smBase.getSite_Code()));
                    itemStock_Mdl.setStockValueDate(DmShared.dNull(smBase.getV_Date()));
                    itemStock_Mdl.setReceiveQty(DmServer.vNull(balanceQty));
                    itemStock_Mdl.setAvgSKURate(DmServer.vNull(tempSkuSaleRate));
                    itemStock_Mdl.setAvgLandedSKURate(DmServer.vNull(tempLandedSkuSaleRate));
                    saveItemStock(itemStock_Mdl, cnTemp);

                    sfNode = new StockFifo_Mdl();
                    sfNode.setSharedKeyFieldCode(smBase.getSharedKeyFieldCode());
                    sfNode.setSharedKeyFieldName(smBase.getSharedKeyFieldName());
                    sfNode.setSno("");
                    sfNode.setStockValueDate(smBase.getV_Date());
                    sfNode.setAvgSKURate(DmServer.vNull(tempSkuSaleRate));
                    sfNode.setAvgLandedSKURate(DmServer.vNull(tempLandedSkuSaleRate));

                    if (flagIssueQty) {
                        sfNode.setIsuQty(DmServer.vNull(balanceQty * -1));
                    } else {
                        sfNode.setRecQty(DmServer.vNull(balanceQty));
                    }

                    if (flagIsItemStockRecord) {
                        sfNode.setIsItemStockRecord("1");
                    } else {
                        sfNode.setIsItemStockRecord("0");
                    }

                    listStockFifo.add(sfNode);
                }
                rsTemp.close();
                psTemp.close();

                if (flagLogFile) {
                    if (!DmShared.xNull(logFilePath).trim().equalsIgnoreCase("")) {
                        String message = itemName + ": Issue qty  " + DmServer.vNull(DmServer.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(decimalPlaces).intValue()))) + " " + sku + " not exist for transaction No. " + smBase.getRecId() + ", dated " + smBase.getV_Date() + ".";
                        DmHandelFile file = new DmHandelFile(logFilePath, true);
                        file.writeLine(message);
                        file.writeLine("");
                        file.close();
                    }
                }
            }

            Stock_Mdl smNode = new Stock_Mdl();
            smBase.copy(smNode);

            for (StockFifo_Mdl stockFifo_Mdl : listStockFifo) {
                if (DmShared.vNull(stockFifo_Mdl.getIsuQty()).doubleValue() != 0 || DmShared.vNull(stockFifo_Mdl.getRecQty()).doubleValue() != 0) {
                    saveStockFifo(stockFifo_Mdl, cnTemp);

                    //manage itemStock Table
                    manageItemStock(DmShared.xNull(smBase.getItemCode()), DmShared.xNull(smBase.getIbCode()), DmShared.xNull(smBase.getDepartmentCode()), DmShared.xNull(smBase.getSite_Code()), DmShared.dNull(stockFifo_Mdl.getStockValueDate()), "", cnTemp);
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psStock != null) {
                    psStock.close();
                }
            } catch (Exception e) {
            }
        }
    }

//    public static void saveStockRecord(Stock_Mdl stock_Mdl, String valuationType, boolean isStockTransferReceiveRecord, boolean isPhysicalStockRecord, Connection cnTemp) throws Exception {
    public static void saveStockRecord(Stock_Mdl stock_Mdl, String valuationType, boolean isStockTransferReceiveRecord, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double tempIssueQty = 0;
        try {

            tempIssueQty = 0;

            if (DmShared.vNull(stock_Mdl.getRecQty()).doubleValue() < 0) {
                tempIssueQty = Math.abs(DmShared.vNull(stock_Mdl.getRecQty()).doubleValue());
            } else if (DmShared.vNull(stock_Mdl.getIsuQty()).doubleValue() > 0) {
                tempIssueQty = DmShared.vNull(stock_Mdl.getIsuQty()).doubleValue();
            }

            if (tempIssueQty > 0) {
                //itemStock:
                sqlQuery = "SELECT :issueQty - ISNULL(SUM(vIStk.balanceQty),0) negativeQty, MAX(im.Name) AS itemName, MAX(um.unit) AS skuName "
                        + " FROM itemMast im "
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " LEFT JOIN ("
                        + "     SELECT iStk.itemCode, ISNULL(SUM(iStk.balanceQty),0) balanceQty"
                        + "     FROM itemStock iStk "
                        + "     WHERE iStk.itemCode = :itemCode "
                        + "     AND iStk.ibCode = :ibCode "
                        + "     AND iStk.stockValueDate <= :v_Date "
                        + "     AND iStk.departmentCode = :departmentCode "
                        + "     AND iStk.site_Code = :site_Code "
                        + "     GROUP BY iStk.itemCode, iStk.ibCode, iStk.departmentCode, iStk.site_Code "
                        + "     ) vIStk ON im.code = vIStk.itemCode "
                        + " WHERE im.code = :itemCode  "
                        + " GROUP BY im.code "
                        + " HAVING CASE WHEN ISNULL(MAX(im.allowNegativeStock),0) = 0 AND ISNULL(SUM(vIStk.balanceQty),0) < :issueQty THEN 1 ELSE 0 END = 1 ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.xNull(stock_Mdl.getItemCode()));
                psTemp.setString("ibCode", DmShared.xNull(stock_Mdl.getIbCode()));
                psTemp.setString("site_Code", DmShared.xNull(stock_Mdl.getSite_Code()));
                psTemp.setDate("v_Date", DmServer.convertDate(stock_Mdl.getV_Date()));
                psTemp.setString("departmentCode", DmShared.xNull(stock_Mdl.getDepartmentCode()));
                psTemp.setDouble("issueQty", tempIssueQty);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("negativeQty")).doubleValue() > 0) {
                        throw new DmException(0, "Entry not allowed as this is generating negative stock for " + DmShared.xNull(rsTemp.getString("itemName")) + " by  " + DmServer.vNull(rsTemp.getString("negativeQty")) + "  " + DmShared.xNull(rsTemp.getString("skuName")) + ".");
                    }
                }
                rsTemp.close();
                psTemp.close();

                //itemBatchStock:
                sqlQuery = "SELECT :issueQty - ISNULL(SUM(vIbs.balanceQty),0) negativeQty, MAX(im.Name) AS itemName, MAX(um.unit) AS skuName "
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
                        + " HAVING CASE WHEN ISNULL(MAX(im.allowNegativeStock),0) = 0 AND ISNULL(SUM(vIbs.balanceQty),0) < :issueQty THEN 1 ELSE 0 END = 1 ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.xNull(stock_Mdl.getItemCode()));
                psTemp.setString("site_Code", DmShared.xNull(stock_Mdl.getSite_Code()));
                psTemp.setString("ibCode", DmShared.xNull(stock_Mdl.getIbCode()));
                psTemp.setString("departmentCode", DmShared.xNull(stock_Mdl.getDepartmentCode()));
                psTemp.setDouble("issueQty", tempIssueQty);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("negativeQty")).doubleValue() > 0) {
                        throw new DmException(0, "Entry not allowed as this is generating negative stock for " + DmShared.xNull(rsTemp.getString("itemName")) + " by  " + DmServer.vNull(rsTemp.getString("negativeQty")) + "  " + DmShared.xNull(rsTemp.getString("skuName")) + ".");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            if (DmShared.iNull(stock_Mdl.getSharedKeyFieldName()).intValue() == 0) {
                sqlQuery = "select isNull(max(v_SNo),0)+1 as vSno from stock where docId = :docId  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(stock_Mdl.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    stock_Mdl.setSharedKeyFieldName(DmShared.iNull(rsTemp.getString("vSno")).toString());
                }
                rsTemp.close();
                psTemp.close();
            }

            String totalTaxPerc = "", v_TypeParent = "";

            v_TypeParent = DmServer.executeScalar("select parentVoucher from voucher_Type where v_Type=" + stock_Mdl.getV_Type() + "", cnTemp);

            if (DmShared.xNull(v_TypeParent).equalsIgnoreCase("Purchase")) {
                totalTaxPerc = DmServer.executeScalar("select TaxPerc from itemMast where code=" + stock_Mdl.getItemCode() + "", cnTemp);
            } else {
                totalTaxPerc = DmServer.executeScalar("select top 1  (isnull(pbd.oh_at_CGST,0)+isnull(pbd.oh_at_SGST,0)+isnull(pbd.oh_at_IGST,0)) as totalTaxPercent \n"
                        + "from pBillDetail as pbd\n"
                        + "left join pBill as pb on pb.docId=pbd.docId\n"
                        + "left join voucher_Type vt on vt.v_Type=pb.v_Type  \n"
                        + "where vt.parentVoucher in ('Purchase') and pbd.isFooter=0  "
                        + " and pbd.itemCode=" + stock_Mdl.getItemCode() + " and pbd.ibCode=" + stock_Mdl.getIbCode() + "", cnTemp);

            }

            sqlQuery = " Insert into stock(docId, v_SNo, v_Type, recId, recIdPrefix, v_No, v_Date, departmentCode, stockContraSubCode,acContraSubCode,refDocId,refV_Type, refV_SNo, "
                    + " refChallanNo, refChallanDate, itemCode, ibCode, recQty, isuQty, skuValue, landedSkuValue, otherAdjustment, skuSaleValue, landedSkuSaleValue, remark, site_Code,isSystemGenerated,grossPurchaseRate,staffSkuValue,totalTaxPerc)"
                    + " values (:docId, :v_SNo, :v_Type, :recId, :recIdPrefix, :v_No, :v_Date, :departmentCode, :stockContraSubCode,:acContraSubCode,:refDocId,:refV_Type, :refV_SNo, "
                    + " :refChallanNo, :refChallanDate, :itemCode, :ibCode, :recQty, :isuQty, :skuValue, :landedSkuValue, :otherAdjustment, :skuSaleValue, :landedSkuSaleValue, :remark, :site_Code, :isSystemGenerated,:grossPurchaseRate,:staffSkuValue,:totalTaxPerc) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(stock_Mdl.getSharedKeyFieldCode()).toString());
            psTemp.setString("site_Code", DmShared.foriegnKey(stock_Mdl.getSite_Code()));
            psTemp.setString("v_SNo", stock_Mdl.getSharedKeyFieldName());
            psTemp.setString("recId", DmShared.xNull(stock_Mdl.getRecId()));
            psTemp.setString("recIdPrefix", DmShared.xNull(stock_Mdl.getRecIdPrefix()));
            psTemp.setString("v_No", DmShared.xNull(stock_Mdl.getV_No()));
            psTemp.setString("v_Type", DmShared.foriegnKey(stock_Mdl.getV_Type()));
            psTemp.setDate("v_Date", DmServer.convertDate(stock_Mdl.getV_Date()));
            psTemp.setString("departmentCode", DmShared.foriegnKey(stock_Mdl.getDepartmentCode()));
            psTemp.setString("stockContraSubCode", DmShared.foriegnKey(stock_Mdl.getStockContraSubCode()));
            psTemp.setString("refDocId", DmShared.foriegnKey(stock_Mdl.getRefDocId()));
            psTemp.setString("refV_Type", DmShared.foriegnKey(stock_Mdl.getRefV_Type()));
            psTemp.setString("refV_SNo", DmShared.foriegnKey(stock_Mdl.getRefV_SNo()));
            psTemp.setString("acContraSubCode", DmShared.foriegnKey(stock_Mdl.getAcContraSubCode()));
            psTemp.setString("refChallanNo", DmShared.xNull(stock_Mdl.getRefChallanNo()));
            psTemp.setDate("refChallanDate", DmServer.convertDate(stock_Mdl.getRefChallanDate()));
            psTemp.setString("itemCode", DmShared.foriegnKey(stock_Mdl.getItemCode()));
            psTemp.setString("ibCode", DmShared.foriegnKey(stock_Mdl.getIbCode()));
            psTemp.setString("recQty", DmServer.vNull(stock_Mdl.getRecQty()));
            psTemp.setString("isuQty", DmServer.vNull(stock_Mdl.getIsuQty()));
            psTemp.setString("skuValue", DmServer.vNull(stock_Mdl.getSkuValue(), "0.00"));
            psTemp.setString("landedSkuValue", DmServer.vNull(stock_Mdl.getLandedSkuValue(), "0.00"));
            psTemp.setString("staffSkuValue", DmServer.vNull(stock_Mdl.getStaffSkuValue(), "0.00"));
            psTemp.setString("otherAdjustment", DmServer.vNull(stock_Mdl.getOtherAdjustment(), "0.00"));
            psTemp.setString("skuSaleValue", DmServer.vNull(stock_Mdl.getSkuSaleValue(), "0.00"));
            psTemp.setString("landedSkuSaleValue", DmServer.vNull(stock_Mdl.getLandedSkuSaleValue(), "0.00"));
            psTemp.setString("remark", DmShared.xNull(stock_Mdl.getRemark()));
            psTemp.setString("isSystemGenerated", DmShared.iNull(stock_Mdl.getIsSystemGenerated()).toString());
            psTemp.setString("grossPurchaseRate", DmShared.vNull(0).toString());
            psTemp.setString("totalTaxPerc", DmShared.vNull(totalTaxPerc).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //Insert Record In StockFIFO Table
            if (DmShared.xNull(valuationType).equalsIgnoreCase("A")) {
                if (isStockTransferReceiveRecord && DmShared.iNull(stock_Mdl.getIsSystemGenerated()).intValue() == 1) {
                    updateStockValueForStockTransferReceiveRecord("", DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()), DmShared.xNull(stock_Mdl.getSharedKeyFieldName()), DmShared.xNull(stock_Mdl.getRefV_SNo()), cnTemp);
                }

                stock_Mdl.getStockFifo_Mdl().setSharedKeyFieldCode(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()));
                stock_Mdl.getStockFifo_Mdl().setSharedKeyFieldName(DmShared.xNull(stock_Mdl.getSharedKeyFieldName()));

                saveStockFifo(stock_Mdl.getStockFifo_Mdl(), cnTemp);

                //manage itemStock Table
                manageItemStock(DmShared.xNull(stock_Mdl.getItemCode()), DmShared.xNull(stock_Mdl.getIbCode()), DmShared.xNull(stock_Mdl.getDepartmentCode()), DmShared.xNull(stock_Mdl.getSite_Code()), DmShared.dNull(stock_Mdl.getV_Date()), "", cnTemp);

            } else if (DmShared.xNull(valuationType).equalsIgnoreCase("IF")) {
//                issueItemStock(stock_Mdl, cnTemp, isPhysicalStockRecord ? false : true);
                issueItemStock(stock_Mdl, cnTemp, true, "");
            } else if (DmShared.xNull(valuationType).equalsIgnoreCase("RF")) {
                receiveItemStock(stock_Mdl, cnTemp, "");
            }

            if (DmShared.xNull(valuationType).equalsIgnoreCase("IF") || DmShared.xNull(valuationType).equalsIgnoreCase("RF")) {
                updateStockValueFromStockFifo(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()), DmShared.xNull(stock_Mdl.getSharedKeyFieldName()), cnTemp);
            }

            //Update itemBatchStock Table
            updateItemBatchStock(DmShared.xNull(stock_Mdl.getIbCode()), DmShared.xNull(stock_Mdl.getItemCode()), DmShared.xNull(stock_Mdl.getDepartmentCode()), DmShared.xNull(stock_Mdl.getSite_Code()), "", cnTemp);

            //Run FIFO Process
            if (flagRunFifoProcess) {
//                runStockFIFO_Process("", DmShared.xNull(stock_Mdl.getItemCode()), DmShared.xNull(stock_Mdl.getIbCode()), DmShared.xNull(stock_Mdl.getDepartmentCode()), DmShared.xNull(stock_Mdl.getSite_Code()), cnTemp, true);
                runStockFIFO_Process("", DmShared.xNull(stock_Mdl.getItemCode()), DmShared.xNull(stock_Mdl.getIbCode()), DmShared.xNull(stock_Mdl.getDepartmentCode()), DmShared.xNull(stock_Mdl.getSite_Code()), cnTemp, true, "");
            }
            DmServer.executeSQL(" \n"
                    + " update stock set grossPurchaseRate=format(landedSkuValue*100/(100+totalTaxPerc),'0.00')\n"
                    + " where docId=" + DmShared.iNull(stock_Mdl.getSharedKeyFieldCode()).toString() + "  ", cnTemp);

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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

    private static void saveStockFifo(StockFifo_Mdl stockFifo_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            //Get SNo
            sqlQuery = "SELECT isNull(max(sno),0)+1 as sno "
                    + " FROM stockFifo "
                    + " WHERE docId = :docId "
                    + " AND v_SNo = :v_Sno";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(stockFifo_Mdl.getSharedKeyFieldCode()).toString());
            psTemp.setString("v_SNo", stockFifo_Mdl.getSharedKeyFieldName());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                stockFifo_Mdl.setSno(DmShared.xNull(rsTemp.getString("sno")));
            }
            rsTemp.close();
            psTemp.close();

            //Insert Record In stockFifo Table
            sqlQuery = "INSERT INTO stockFifo ";
            sqlQuery += "(docId ";
            sqlQuery += ",v_SNo ";
            sqlQuery += ",sno ";
            sqlQuery += ",recQty ";
            sqlQuery += ",isuQty ";
            sqlQuery += ",stockValueDate ";
            sqlQuery += ",avgSKURate ";
            sqlQuery += ",avgLandedSKURate ";
            sqlQuery += ",isItemStockRecord) ";
            sqlQuery += "VALUES ";
            sqlQuery += "( :docId ";
            sqlQuery += ", :v_SNo ";
            sqlQuery += ", :sno ";
            sqlQuery += ", :recQty ";
            sqlQuery += ", :isuQty ";
            sqlQuery += ", :stockValueDate ";
            sqlQuery += ", :avgSKURate ";
            sqlQuery += ", :avgLandedSKURate ";
            sqlQuery += ", :isItemStockRecord ";
            sqlQuery += ") ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", stockFifo_Mdl.getSharedKeyFieldCode());
            psTemp.setString("v_SNo", stockFifo_Mdl.getSharedKeyFieldName());
            psTemp.setString("sno", stockFifo_Mdl.getSno());
            psTemp.setString("recQty", DmServer.vNull(stockFifo_Mdl.getRecQty()));
            psTemp.setString("isuQty", DmServer.vNull(stockFifo_Mdl.getIsuQty()));
            psTemp.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(stockFifo_Mdl.getStockValueDate())));
            psTemp.setString("avgSKURate", DmServer.vNull(stockFifo_Mdl.getAvgSKURate()));
            psTemp.setString("avgLandedSKURate", DmServer.vNull(stockFifo_Mdl.getAvgLandedSKURate()));
            psTemp.setString("isItemStockRecord", DmShared.iNull(stockFifo_Mdl.getIsItemStockRecord()).toString());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
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

    }

    private static void saveItemStock(ItemStock_Mdl itemStock_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            //SELECT iStk.receiveQty, iStk.avgSKURate, iStk.avgLandedSKURate FROM itemStock istk
            sqlQuery = "INSERT INTO itemStock(itemCode, ibCode, departmentCode, site_Code, stockValueDate"
                    + ", receiveQty, avgSKURate, avgLandedSKURate, isRecalculate) "
                    + "VALUES (:itemCode, :ibCode, :departmentCode, :site_Code, :stockValueDate"
                    + ", :receiveQty, :avgSKURate, :avgLandedSKURate, :isRecalculate); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("isRecalculate", "1");
            psTemp.setString("itemCode", DmShared.foriegnKey(itemStock_Mdl.getItemCode()));
            psTemp.setString("ibCode", DmShared.foriegnKey(itemStock_Mdl.getIbCode()));
            psTemp.setString("departmentCode", DmShared.foriegnKey(itemStock_Mdl.getDepartmentCode()));
            psTemp.setString("site_Code", DmShared.foriegnKey(itemStock_Mdl.getSite_Code()));
            psTemp.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(itemStock_Mdl.getStockValueDate())));
            psTemp.setString("receiveQty", DmServer.vNull(itemStock_Mdl.getReceiveQty()));
            psTemp.setString("avgSKURate", DmServer.vNull(itemStock_Mdl.getAvgSKURate()));
            psTemp.setString("avgLandedSKURate", DmServer.vNull(itemStock_Mdl.getAvgLandedSKURate()));
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    public static void manageItemStock(String itemCode, String ibCode, String departmentCode, String site_Code, String stockValueDate, Connection cnTemp) throws Exception {
        try {
            manageItemStock(itemCode, ibCode, departmentCode, site_Code, stockValueDate, "", cnTemp);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    private static void manageItemStock(String itemCode, String ibCode, String departmentCode, String site_Code, String stockValueDate, String deleteDocId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery, sqlStockReceive, condition = "";
        String receiveQuantity = "", receiveSkuValue = "", receiveLandedSkuValue = "", staffSkuValue = "";

        try {

            if (DmShared.iNull(deleteDocId).longValue() == 0) {
                receiveQuantity = " ISNULL(SUM(vStk.recQty),0) + SUM(CASE WHEN ISNULL(vStk.isuQty,0) < 0 THEN ABS(vStk.isuQty) ELSE ISNULL(vStk.isuQty,0) * -1 END) ";
                receiveSkuValue = " ISNULL(SUM(vStk.skuValue),0) ";
                receiveLandedSkuValue = " ISNULL(SUM(vStk.landedSkuValue),0) ";
                staffSkuValue = " ISNULL(SUM(vStk.staffSkuValue),0) ";
            } else {
                receiveQuantity = " SUM( CASE WHEN vStk.docId = :docId THEN 0 ELSE 1 END * ISNULL(vStk.recQty,0) ) + SUM( CASE WHEN vStk.docId = :docId THEN 0 ELSE 1 END * CASE WHEN ISNULL(vStk.isuQty,0) < 0 THEN ABS(vStk.isuQty) ELSE ISNULL(vStk.isuQty,0) * -1 END) ";
                receiveSkuValue = " SUM( CASE WHEN vStk.docId = :docId THEN 0 ELSE 1 END * ISNULL(vStk.skuValue,0)) ";
                receiveLandedSkuValue = " SUM( CASE WHEN vStk.docId = :docId THEN 0 ELSE 1 END * ISNULL(vStk.landedSkuValue,0)) ";
                staffSkuValue = " SUM( CASE WHEN vStk.docId = :docId THEN 0 ELSE 1 END * ISNULL(vStk.staffSkuValue,0)) ";
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                condition += "	AND stk.itemCode = :itemCode ";
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                condition += "	AND stk.ibCode = :ibCode ";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition += "	AND stk.departmentCode = :departmentCode ";
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                condition += "	AND stk.site_Code = :site_Code ";
            }
            if (!DmShared.dNull(stockValueDate).trim().equals("")) {
                condition += "	AND stk.v_Date = :stockValueDate ";
            }
            sqlQuery = "SELECT stk.docId, stk.itemCode, stk.ibCode, stk.departmentCode, stk.site_Code, stk.v_Date "
                    + "	, stk.recQty, stk.isuQty, stk.skuValue"
                    + " , (ISNULL(stk.landedSkuValue, 0) + ISNULL(stk.otherAdjustment, 0)) AS landedSkuValue , ISNULL(stk.staffSkuValue, 0) as staffSkuValue  "
                    + "	FROM stock stk "
                    + "	INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type "
                    + "	WHERE (ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') OR (ISNULL(stk.recQty,0) + ISNULL(stk.isuQty,0)) = 0) "
                    + " AND (CASE WHEN ISNULL(vt.parentVoucher,'') = 'Stock Transfer' AND ISNULL(stk.isSystemGenerated,0) = 0 THEN 0 ELSE 1 END = 1 "
                    + "         OR CASE WHEN ISNULL(vt.parentVoucher,'') = 'Physical Stock' AND (ISNULL(stk.recQty,0) > 0 OR ISNULL(stk.isuQty,0) < 0) THEN 1 ELSE 0 END = 1) "
                    + "" + condition
                    + " UNION ALL "
                    + " SELECT stk.docId, stk.itemCode, stk.ibCode, stk.departmentCode, stk.site_Code, stk.v_Date "
                    + "	, sf.recQty, sf.isuQty"
                    + " , CASE WHEN ABS(ISNULL(stk.skuSaleValue,0)) > 0 THEN "
                    + "             CASE WHEN (ISNULL(sf.recQty,0) - ISNULL(sf.isuQty,0)) > 0 THEN "
                    + "                     (ABS(ISNULL(stk.skuSaleValue,0)) * (ISNULL(sf.recQty,0) - ISNULL(sf.isuQty,0))) / (ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) "
                    + "             ELSE 0 END "
                    + "   ELSE 0 END AS skuValue "
                    + " , CASE WHEN ABS(ISNULL(stk.landedSkuSaleValue,0)) > 0 THEN "
                    + "             CASE WHEN (ISNULL(sf.recQty,0) - ISNULL(sf.isuQty,0)) > 0 THEN "
                    + "                     ((ABS(ISNULL(stk.landedSkuSaleValue,0)) + ISNULL(stk.otherAdjustment, 0)) * (ISNULL(sf.recQty,0) - ISNULL(sf.isuQty,0))) / (ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) "
                    + "             ELSE 0 END "
                    + "   ELSE 0 END AS landedSkuValue "
                    + "                             "
                    + " , CASE WHEN ABS(ISNULL(stk.staffSkuValue,0)) > 0 THEN "
                    + "             CASE WHEN (ISNULL(sf.recQty,0) - ISNULL(sf.isuQty,0)) > 0 THEN "
                    + "                     ((ABS(ISNULL(stk.staffSkuValue,0)) ) * (ISNULL(sf.recQty,0) - ISNULL(sf.isuQty,0))) / (ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) "
                    + "             ELSE 0 END "
                    + "   ELSE 0 END AS staffSkuValue "
                    + "	FROM stockFifo sf "
                    + " INNER JOIN stock stk ON stk.docId = sf.docId AND stk.v_SNo = sf.v_SNo "
                    + "	INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type "
                    + " WHERE CASE WHEN (ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive') OR (ISNULL(stk.recQty,0) + ISNULL(stk.isuQty,0)) = 0) THEN 1 ELSE 0 END = 0 "
                    + " AND CASE WHEN ISNULL(vt.parentVoucher,'') = 'Stock Transfer' AND ISNULL(stk.isSystemGenerated,0) = 1 THEN 0 ELSE 1 END = 1 "
                    + " AND CASE WHEN ISNULL(vt.parentVoucher,'') = 'Physical Stock' AND (ISNULL(stk.recQty,0) > 0 OR ISNULL(stk.isuQty,0) < 0) THEN 1 ELSE 0 END = 0 "
                    + "	AND sf.isItemStockRecord = 1 "
                    + " AND CASE WHEN ISNULL(sf.recQty,0) > 0 THEN 1 WHEN ISNULL(sf.isuQty,0) < 0 THEN 1 ELSE 0 END = 1 " + condition;

            sqlStockReceive = "	SELECT vStk.itemCode, vStk.ibCode, vStk.departmentCode, vStk.site_Code, vStk.v_Date "
                    + "	, " + receiveQuantity + " AS receiveQty "
                    + "	, CAST((CASE WHEN (" + receiveQuantity + ") = 0 THEN 0 ELSE " + receiveSkuValue + " / (" + receiveQuantity + ") END) AS DECIMAL(18,7) ) AS avgSkuRate "
                    + "	, CAST((CASE WHEN (" + receiveQuantity + ") = 0 THEN 0 ELSE " + receiveLandedSkuValue + " / (" + receiveQuantity + ") END) AS DECIMAL(18,7) ) AS avgLandedSkuRate "
                    + "	, CAST((CASE WHEN (" + receiveQuantity + ") = 0 THEN 0 ELSE " + staffSkuValue + " / (" + receiveQuantity + ") END) AS DECIMAL(18,7) ) AS staffSkuRate "
                    + "	FROM (" + sqlQuery + ") AS vStk ";

            sqlQuery = "MERGE itemStock AS Target "
                    + "USING ( " + sqlStockReceive + "  "
                    + "         GROUP BY vStk.itemCode, vStk.ibCode, vStk.departmentCode, vStk.site_Code, vStk.v_Date "
                    + "	) AS Source "
                    + " ON Target.itemCode = Source.itemCode "
                    + " AND Target.ibCode = Source.ibCode "
                    + "	AND Target.departmentCode = Source.departmentCode "
                    + "	AND Target.site_Code = Source.site_Code "
                    + "	AND Target.stockValueDate = Source.v_Date ";
            sqlQuery += " WHEN MATCHED THEN "
                    + "    UPDATE SET Target.receiveQty = Source.receiveQty "
                    + "             , Target.avgSkuRate = Source.avgSkuRate "
                    + "             , Target.avgLandedSkuRate = Source.avgLandedSkuRate "
                    + "             , Target.staffSkuRate = Source.staffSkuRate "
                    + "             , Target.isReCalculate = CASE WHEN ISNULL(Source.receiveQty,0) != ISNULL(Target.receiveQty,0) THEN 1 ELSE CASE WHEN Target.isReCalculate = 1 THEN 1 ELSE 0 END END "
                    + " WHEN NOT MATCHED BY TARGET THEN "
                    + "    INSERT (itemCode, ibCode, departmentCode, site_Code, stockValueDate, receiveQty, avgSkuRate, avgLandedSkuRate, isReCalculate,staffSkuRate) "
                    + "    VALUES (Source.itemCode, Source.ibCode, Source.departmentCode, Source.site_Code, Source.v_Date, Source.receiveQty, Source.avgSkuRate, Source.avgLandedSkuRate, 1,Source.staffSkuRate);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(deleteDocId).longValue() > 0) {
                psTemp.setString("docid", deleteDocId);
            }

            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", ibCode);
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", departmentCode);
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                psTemp.setString("site_Code", site_Code);
            }
            if (!DmShared.dNull(stockValueDate).trim().equals("")) {
                psTemp.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(stockValueDate)));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //Manage Balance Qty in Item Stock Table
            manageItemStockBalance(itemCode, ibCode, departmentCode, site_Code, stockValueDate, false, deleteDocId, cnTemp);

            //Update avgSkuRate and avgLandedSkuRate in Stock FIFO table
            sqlQuery = "UPDATE stockFifo "
                    + "SET stockFifo.avgSKURate = itemStock.avgSKURate "
                    + ", stockFifo.avgLandedSKURate = itemStock.avgLandedSKURate "
                    + "FROM stock, itemStock "
                    + "WHERE stockFifo.docId = stock.docId  "
                    + "AND stockFifo.v_SNo = stock.v_SNo "
                    + "AND stockFifo.stockValueDate = itemStock.stockValueDate "
                    + "AND itemStock.itemCode = stock.itemCode "
                    + "AND itemStock.ibCode = stock.ibCode "
                    + "AND itemStock.departmentCode = stock.departmentCode "
                    + "AND itemStock.site_Code = stock.site_Code ";
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlQuery += "	AND stock.itemCode = :itemCode ";
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += "	AND stock.ibCode = :ibCode ";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlQuery += "	AND stock.departmentCode = :departmentCode ";
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                sqlQuery += "	AND stock.site_Code = :site_Code ";
            }
            if (!DmShared.dNull(stockValueDate).trim().equals("")) {
                sqlQuery += "	AND stockFifo.stockValueDate = :stockValueDate ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", ibCode);
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", departmentCode);
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                psTemp.setString("site_Code", site_Code);
            }
            if (!DmShared.dNull(stockValueDate).trim().equals("")) {
                psTemp.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(stockValueDate)));
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    private static void manageItemStockBalance(String itemCode, String ibCode, String departmentCode, String site_Code, String stockValueDate, boolean checkGreaterStockValueDate, String deleteDocId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        String balanceQuantity = "";

        try {
            //Manage Balance Qty in Item Stock Table
            if (DmShared.iNull(deleteDocId).longValue() == 0) {
                balanceQuantity = " ISNULL(SUM(sf.recQty),0) - ISNULL(SUM(sf.isuQty),0) ";
            } else {
                balanceQuantity = " SUM(CASE WHEN sf.docId = :docId THEN 0 ELSE 1 END * ISNULL(sf.recQty,0)) - SUM(CASE WHEN sf.docId = :docId THEN 0 ELSE 1 END * ISNULL(sf.isuQty,0)) ";
            }

            sqlQuery = "MERGE itemStock AS Target "
                    + "USING ( "
                    + "		SELECT stk1.itemCode, stk1.ibCode, stk1.departmentCode, stk1.site_Code, sf.stockValueDate "
                    + "		, " + balanceQuantity + " AS balanceQty "
                    + "		FROM stock stk1 "
                    + "		INNER JOIN stockFifo sf ON sf.docId = stk1.docId AND sf.v_SNo = stk1.v_SNo "
                    + "		WHERE sf.stockValueDate IS NOT NULL ";
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlQuery += "		AND stk1.itemCode = :itemCode ";
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += "		AND stk1.ibCode = :ibCode ";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlQuery += "		AND stk1.departmentCode = :departmentCode ";
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                sqlQuery += "		AND stk1.site_Code = :site_Code ";
            }
            if (!DmShared.dNull(stockValueDate).trim().equals("")) {
                if (checkGreaterStockValueDate) {
                    sqlQuery += "		AND sf.stockValueDate >= :stockValueDate ";
                } else {
                    sqlQuery += "		AND sf.stockValueDate = :stockValueDate ";
                }
            }
            sqlQuery += "		GROUP BY stk1.itemCode, stk1.ibCode, stk1.departmentCode, stk1.site_Code, sf.stockValueDate "
                    + "     ) Source "
                    + " ON Target.itemCode = Source.itemCode "
                    + " AND Target.ibCode = Source.ibCode "
                    + "	AND Target.departmentCode = Source.departmentCode "
                    + "	AND Target.site_Code = Source.site_Code "
                    + "	AND Target.stockValueDate = Source.stockValueDate "
                    + " WHEN MATCHED THEN "
                    + "    UPDATE SET Target.balanceQty = Source.balanceQty "
                    + "     , Target.isReCalculate = CASE WHEN ISNULL(Source.balanceQty,0) != ISNULL(Target.balanceQty,0) THEN 1 ELSE CASE WHEN Target.isReCalculate = 1 THEN 1 ELSE 0 END END "
                    + " WHEN NOT MATCHED BY TARGET THEN "
                    + "    INSERT (itemCode, ibCode, departmentCode, site_Code, stockValueDate, balanceQty, isReCalculate) "
                    + "    VALUES (Source.itemCode, Source.ibCode, Source.departmentCode, Source.site_Code, Source.stockValueDate, Source.balanceQty, 1); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(deleteDocId).longValue() > 0) {
                psTemp.setString("docid", deleteDocId);
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", ibCode);
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", departmentCode);
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                psTemp.setString("site_Code", site_Code);
            }
            if (!DmShared.dNull(stockValueDate).trim().equals("")) {
                psTemp.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(stockValueDate)));
            }
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void runStockFIFO_Process(String filterItemType, String itemCode, String ibCode, String departmentCode, String site_Code, Connection cnTemp, boolean isAutoRun, String logFilePath) throws Exception {
        DmPreparedStatement psTemp = null, psItemStock = null, psStock = null;
        ResultSet rsTemp = null, rsItemStock = null, rsStock = null;
        String sqlQuery, joinItem = "";
        Stock_Mdl stock_Mdl;
        try {
            if (!DmShared.xNull(filterItemType).trim().equals("")) {
                joinItem = "	INNER JOIN itemMast im ON im.code = iStk.itemCode ";
            }

            sqlQuery = "    SELECT iStk.itemCode, iStk.ibCode, iStk.departmentCode, iStk.site_Code, MIN(iStk.stockValueDate) AS stockValueDate "
                    + "     FROM itemStock iStk " + joinItem
                    + "     WHERE iStk.isReCalculate = 1 ";
            if (!DmShared.xNull(filterItemType).trim().equals("")) {
                sqlQuery += "   AND im.filterItemType = :filterItemType ";
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlQuery += "	AND iStk.itemCode = :itemCode ";
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += "	AND iStk.ibCode = :ibCode ";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlQuery += "	AND iStk.departmentCode = :departmentCode ";
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                sqlQuery += "	AND iStk.site_Code = :site_Code ";
            }
            sqlQuery += " GROUP BY iStk.itemCode, iStk.ibCode, iStk.departmentCode, iStk.site_Code "
                    + " ORDER BY MIN(iStk.stockValueDate), iStk.itemCode, iStk.ibCode, iStk.departmentCode, iStk.site_Code  ";
            psItemStock = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(filterItemType).trim().equals("")) {
                psItemStock.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode(filterItemType, "manualCode", "itemType", "code", cnTemp)));
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psItemStock.setString("itemCode", itemCode);
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psItemStock.setString("ibCode", ibCode);
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psItemStock.setString("departmentCode", departmentCode);
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                psItemStock.setString("site_Code", site_Code);
            }
            rsItemStock = psItemStock.executeQuery();
            while (rsItemStock.next()) {
                if (!flagRunFifoProcess) {
                    //manage itemStock Table
                    manageItemStock(DmShared.xNull(rsItemStock.getString("itemCode")), DmShared.xNull(rsItemStock.getString("ibCode")), DmShared.xNull(rsItemStock.getString("departmentCode")), DmShared.xNull(rsItemStock.getString("site_Code")), DmShared.dNull(rsItemStock.getDate("stockValueDate")), "", cnTemp);
                }

                sqlQuery = "DELETE stockFifo "
                        + " FROM stockFifo sf "
                        + " INNER JOIN stock stk ON stk.docId = sf.docId AND stk.v_SNo = sf.v_SNo "
                        + " INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type "
                        + " WHERE CASE WHEN (ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive') OR (ISNULL(stk.recQty,0) + ISNULL(stk.isuQty,0)) = 0) THEN 1 ELSE 0 END = 0 "
                        + " AND CASE WHEN ISNULL(vt.parentVoucher,'') = 'Stock Transfer' AND ISNULL(stk.isSystemGenerated,0) = 1 THEN 0 ELSE 1 END = 1 "
                        + " AND CASE WHEN ISNULL(vt.parentVoucher,'') = 'Physical Stock' AND (ISNULL(stk.recQty,0) > 0 OR ISNULL(stk.isuQty,0) < 0) THEN 1 ELSE 0 END = 0 "
                        + " AND sf.stockValueDate >=  :stockValueDate "
                        + " AND stk.departmentCode = :departmentCode "
                        + " AND stk.itemCode = :itemCode "
                        + " AND stk.ibCode = :ibCode "
                        + " AND stk.site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(rsItemStock.getDate("stockValueDate"))));
                psTemp.setString("itemCode", DmShared.iNull(rsItemStock.getString("itemCode")).toString());
                psTemp.setString("ibCode", DmShared.iNull(rsItemStock.getString("ibCode")).toString());
                psTemp.setString("departmentCode", DmShared.iNull(rsItemStock.getString("departmentCode")).toString());
                psTemp.setString("site_Code", DmShared.iNull(rsItemStock.getString("site_Code")).toString());
                psTemp.executeUpdate();
                psTemp.close();

                manageItemStockBalance(DmShared.xNull(rsItemStock.getString("itemCode")), DmShared.xNull(rsItemStock.getString("ibCode")), DmShared.xNull(rsItemStock.getString("departmentCode")), DmShared.xNull(rsItemStock.getString("site_Code")), DmShared.dNull(rsItemStock.getDate("stockValueDate")), true, "", cnTemp);

                updateStockValueFromStockFifo("", "", cnTemp);

                //Manage Issue/Receive Qty
                sqlQuery = " 	SELECT stk.v_Date, stk.docId, stk.v_No, stk.v_SNo, stk.itemCode, stk.ibCode, stk.departmentCode, stk.site_Code "
                        + " 	, ISNULL(stk.recQty,0) - ISNULL(vSf.recQty,0) AS recQty "
                        + " 	, ISNULL(stk.isuQty,0) - ISNULL(vSf.isuQty,0) AS isuQty "
                        + " 	, stk.v_Type, stk.recId, stk.recIdPrefix, stk.stockContraSubCode, stk.acContraSubCode "
                        + " 	, stk.refDocId, stk.refV_SNo, stk.refV_Type, stk.refChallanNo "
                        + " 	, stk.refChallanDate, stk.isSystemGenerated, stk.remark "
                        + " 	, CASE WHEN ISNULL(stk.skuSaleValue,0) <> 0 THEN "
                        + "                 CASE WHEN (ISNULL(stk.recQty,0) - ISNULL(vSf.recQty,0)) <> 0 THEN "
                        + "                         (ISNULL(stk.skuSaleValue,0) * (ISNULL(stk.recQty,0) - ISNULL(vSf.recQty,0))) / ISNULL(stk.recQty,0) "
                        + "                 WHEN (ISNULL(stk.isuQty,0) - ISNULL(vSf.isuQty,0)) <> 0 THEN "
                        + "                         (ISNULL(stk.skuSaleValue,0) * (ISNULL(stk.isuQty,0) - ISNULL(vSf.isuQty,0))) / ISNULL(stk.isuQty,0) "
                        + "                 ELSE 0 END "
                        + "       ELSE 0 END AS skuSaleValue "
                        + " 	, CASE WHEN ISNULL(stk.landedSkuSaleValue,0) <> 0 THEN "
                        + "                 CASE WHEN (ISNULL(stk.recQty,0) - ISNULL(vSf.recQty,0)) <> 0 THEN "
                        + "                         (ISNULL(stk.landedSkuSaleValue,0) * (ISNULL(stk.recQty,0) - ISNULL(vSf.recQty,0))) / ISNULL(stk.recQty,0) "
                        + "                 WHEN (ISNULL(stk.isuQty,0) - ISNULL(vSf.isuQty,0)) <> 0 THEN "
                        + "                         (ISNULL(stk.landedSkuSaleValue,0) * (ISNULL(stk.isuQty,0) - ISNULL(vSf.isuQty,0))) / ISNULL(stk.isuQty,0) "
                        + "                 ELSE 0 END "
                        + "       ELSE 0 END AS landedSkuSaleValue "
                        + " 	FROM stock stk  "
                        + " 	INNER JOIN voucher_Type vt  ON vt.v_Type = stk.v_Type "
                        + " 	LEFT JOIN ( "
                        + " 				SELECT sf.docId, sf.v_SNo, SUM(sf.recQty) AS recQty, SUM(sf.isuQty) AS isuQty "
                        + " 				FROM stockFifo sf  "
                        + " 				GROUP BY sf.docId, sf.v_SNo "
                        + " 				) vSf ON vSf.docId = stk.docId AND vSf.v_SNo = stk.v_SNo "
                        + " 	WHERE CASE WHEN (ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive') OR (ISNULL(stk.recQty,0) + ISNULL(stk.isuQty,0)) = 0) THEN 1 ELSE 0 END = 0 "
                        + "     AND CASE WHEN ISNULL(vt.parentVoucher,'') = 'Stock Transfer' AND ISNULL(stk.isSystemGenerated,0) = 1 THEN 0 ELSE 1 END = 1 "
                        + "     AND CASE WHEN ISNULL(vt.parentVoucher,'') = 'Physical Stock' AND (ISNULL(stk.recQty,0) > 0 OR ISNULL(stk.isuQty,0) < 0) THEN 1 ELSE 0 END = 0 "
                        + " 	AND stk.itemCode = :itemCode "
                        + " 	AND stk.ibCode = :ibCode "
                        + " 	AND stk.departmentCode = :departmentCode "
                        + " 	AND stk.site_Code = :site_Code "
                        + " 	AND stk.v_Date >= :stockValueDate "
                        + "         AND (ISNULL(stk.recQty,0) - ISNULL(vSf.recQty,0)) - (ISNULL(stk.isuQty,0) - ISNULL(vSf.isuQty,0)) <> 0 "
                        + " 	ORDER BY stk.v_Date, vt.serialNo, vt.short_Name, stk.v_NO, stk.recId, stk.v_SNo "
                        + "";
                psStock = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psStock.setDate("stockValueDate", DmServer.convertDate(DmShared.dNull(rsItemStock.getDate("stockValueDate"))));
                psStock.setString("itemCode", DmShared.iNull(rsItemStock.getString("itemCode")).toString());
                psStock.setString("ibCode", DmShared.iNull(rsItemStock.getString("ibCode")).toString());
                psStock.setString("departmentCode", DmShared.iNull(rsItemStock.getString("departmentCode")).toString());
                psStock.setString("site_Code", DmShared.iNull(rsItemStock.getString("site_Code")).toString());
                rsStock = psStock.executeQuery();
                boolean flagReceiveData = false, flagIssueData = false;
                while (rsStock.next()) {
                    flagIssueData = false;
                    flagReceiveData = false;
                    if (DmShared.vNull(rsStock.getString("isuQty")).doubleValue() > 0
                            || DmShared.vNull(rsStock.getString("recQty")).doubleValue() < 0) {
                        flagIssueData = true;

                    } else if (DmShared.vNull(rsStock.getString("recQty")).doubleValue() > 0
                            || DmShared.vNull(rsStock.getString("isuQty")).doubleValue() < 0) {
                        flagReceiveData = true;

                    }

                    if (flagReceiveData || flagIssueData) {
                        stock_Mdl = new Stock_Mdl();
                        stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsStock.getString("docId")));
                        stock_Mdl.setSharedKeyFieldName(DmShared.xNull(rsStock.getString("v_SNO")));
                        stock_Mdl.setRecQty(DmServer.vNull(rsStock.getString("recQty")));
                        stock_Mdl.setIsuQty(DmServer.vNull(rsStock.getString("isuQty")));
                        stock_Mdl.setV_Date(DmShared.dNull(rsStock.getDate("v_Date")));
                        stock_Mdl.setSite_Code(DmShared.xNull(rsStock.getString("site_Code")));
                        stock_Mdl.setV_Type(DmShared.xNull(rsStock.getString("v_Type")));
                        stock_Mdl.setV_No(DmShared.xNull(rsStock.getString("v_No")));
                        stock_Mdl.setRecId(DmShared.xNull(rsStock.getString("recId")));
                        stock_Mdl.setRecIdPrefix(DmShared.xNull(rsStock.getString("recIdPrefix")));
                        stock_Mdl.setDepartmentCode(DmShared.xNull(rsStock.getString("departmentCode")));
                        stock_Mdl.setStockContraSubCode(DmShared.xNull(rsStock.getString("stockContraSubCode")));
                        stock_Mdl.setAcContraSubCode(DmShared.xNull(rsStock.getString("acContraSubCode")));
                        stock_Mdl.setRefDocId(DmShared.xNull(rsStock.getString("refDocId")));
                        stock_Mdl.setRefV_SNo(DmShared.xNull(rsStock.getString("refV_SNo")));
                        stock_Mdl.setRefV_Type(DmShared.xNull(rsStock.getString("refV_Type")));
                        stock_Mdl.setRefChallanNo(DmShared.xNull(rsStock.getString("refChallanNo")));
                        stock_Mdl.setRefChallanDate(DmShared.dNull(rsStock.getDate("refChallanDate")));
                        stock_Mdl.setItemCode(DmShared.xNull(rsStock.getString("itemCode")));
                        stock_Mdl.setIbCode(DmShared.xNull(rsStock.getString("ibCode")));
                        stock_Mdl.setIsSystemGenerated(DmShared.iNull(rsStock.getString("isSystemGenerated")).toString());
                        stock_Mdl.setRemark(DmShared.xNull(rsStock.getString("remark")));
                        stock_Mdl.setSkuSaleValue(DmServer.vNull(rsStock.getString("skuSaleValue"), "0.00"));
                        stock_Mdl.setLandedSkuSaleValue(DmServer.vNull(rsStock.getString("landedSkuSaleValue"), "0.00"));

                        if (flagIssueData) {

//                            issueItemStock(stock_Mdl, cnTemp, isAutoRun ? true : false);
                            issueItemStock(stock_Mdl, cnTemp, false, logFilePath);

                            updateStockValueForStockTransferReceiveRecord("", DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()), DmShared.xNull(stock_Mdl.getSharedKeyFieldName()), DmShared.xNull(stock_Mdl.getRefV_SNo()), cnTemp);
                        } else if (flagReceiveData) {
                            receiveItemStock(stock_Mdl, cnTemp, logFilePath);
                        }

                        updateStockValueFromStockFifo(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()), DmShared.xNull(stock_Mdl.getSharedKeyFieldName()), cnTemp);

                    }
                }
                rsStock.close();
                psStock.close();

                sqlQuery = "UPDATE itemStock "
                        + " SET isRecalculate = 0 "
                        + " where itemCode = :itemCode "
                        + " and ibCode = :ibCode "
                        + " and departmentCode = :departmentCode "
                        + " and site_Code = :site_Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("itemCode", DmShared.iNull(rsItemStock.getString("itemCode")).toString());
                psTemp.setString("ibCode", DmShared.iNull(rsItemStock.getString("ibCode")).toString());
                psTemp.setString("departmentCode", DmShared.iNull(rsItemStock.getString("departmentCode")).toString());
                psTemp.setString("site_Code", DmShared.iNull(rsItemStock.getString("site_Code")).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            psItemStock.close();
            rsItemStock.close();

            manageItemStockBalance(itemCode, ibCode, departmentCode, site_Code, "", false, "", cnTemp);

            sqlQuery = "DELETE itemStock "
                    + " FROM itemStock iStk "
                    + " LEFT JOIN ( "
                    + " 		SELECT stk.itemCode, stk.ibCode, stk.departmentCode, stk.site_Code, sf.stockValueDate "
                    + " 		FROM stock stk "
                    + " 		LEFT JOIN stockFifo sf ON stk.docId = sf.docId AND stk.v_SNo = sf.v_SNo "
                    + " 	) vStk ON vStk.itemCode = iStk.itemCode "
                    + "                     AND vStk.ibCode = iStk.ibCode "
                    + "                     AND vStk.departmentCode = iStk.departmentCode "
                    + "                     AND vStk.site_Code = iStk.site_Code "
                    + "                     AND vStk.stockValueDate = iStk.stockValueDate "
                    + " WHERE vStk.stockValueDate IS NULL ";
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlQuery += " AND iStk.itemCode = :itemCode ";
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += " AND iStk.ibCode = :ibCode ";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlQuery += " AND iStk.departmentCode = :departmentCode ";
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                sqlQuery += " AND iStk.site_Code = :site_Code ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", itemCode);
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", departmentCode);
            }
            if (DmShared.iNull(site_Code).intValue() > 0) {
                psTemp.setString("site_Code", site_Code);
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (rsItemStock != null) {
                    rsItemStock.close();
                }
                if (rsStock != null) {
                    rsStock.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psStock != null) {
                    psStock.close();
                }
                if (psItemStock != null) {
                    psItemStock.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void updateStockValueFromStockFifo(String docId, String v_SNo, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            sqlQuery = "UPDATE stock "
                    + " SET stock.skuValue = vSf.skuValue "
                    + " , stock.landedSkuValue = vSf.landedSkuValue "
                    + " FROM voucher_Type, "
                    + "  ( "
                    + "     SELECT stk.docId, stk.v_SNo "
                    + "     , CAST(SUM((CASE WHEN ISNULL(sf.recQty,0) <> 0 THEN sf.recQty ELSE ISNULL(sf.isuQty,0) END ) "
                    + "                 * (CASE WHEN ISNULL(sf.isItemStockRecord,0) = 0 "
                    + "                         THEN ISNULL(sf.avgSKURate,0) "
                    + "                         ELSE "
                    + "                             CASE WHEN ISNULL(stk.skuSaleValue,0) = 0 "
                    + "                                     THEN ISNULL(sf.avgSKURate,0) "
                    + "                                     ELSE "
                    + "                                         ISNULL(stk.skuSaleValue,0) / CASE WHEN ISNULL(stk.recQty,0) <> 0 THEN stk.recQty ELSE ISNULL(stk.isuQty,0) END "
                    + "                                     END "
                    + "                         END) "
                    + "               ) AS decimal(18,2)) AS skuValue "
                    + "     , CAST(SUM((CASE WHEN ISNULL(sf.recQty,0) <> 0 THEN sf.recQty ELSE ISNULL(sf.isuQty,0) END ) "
                    + "                 * (CASE WHEN ISNULL(sf.isItemStockRecord,0) = 0 "
                    + "                         THEN ISNULL(sf.avgLandedSKURate,0) "
                    + "                         ELSE "
                    + "                             CASE WHEN ISNULL(stk.landedSkuSaleValue,0) = 0 "
                    + "                                     THEN ISNULL(sf.avgLandedSKURate,0) "
                    + "                                     ELSE "
                    + "                                         ISNULL(stk.landedSkuSaleValue,0) / CASE WHEN ISNULL(stk.recQty,0) <> 0 THEN stk.recQty ELSE ISNULL(stk.isuQty,0) END "
                    + "                                     END "
                    + "                         END) "
                    + "               ) AS decimal(18,2)) AS landedSkuValue "
                    + "     FROM stock stk  "
                    + "     LEFT JOIN stockFifo sf ON sf.docid = stk.docid AND sf.v_SNo = stk.v_SNo  ";
            if (DmShared.iNull(docId).longValue() > 0) {
                sqlQuery += "	WHERE stk.docId = :docId ";
                if (DmShared.iNull(v_SNo).intValue() > 0) {
                    sqlQuery += "	AND stk.v_SNo = :v_SNo ";
                }
            }
            sqlQuery += "   GROUP BY stk.docId, stk.v_SNo "
                    + "	) vSf "
                    + " WHERE stock.docId = vSf.docId "
                    + " AND stock.v_SNo = vSf.v_SNo "
                    + " AND voucher_Type.v_Type = stock.v_Type "
                    + " AND CASE WHEN (ISNULL(voucher_Type.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive') OR (ISNULL(stock.recQty,0) + ISNULL(stock.isuQty,0)) = 0) THEN 1 ELSE 0 END = 0 "
                    + " AND CASE WHEN ISNULL(voucher_Type.parentVoucher,'') = 'Stock Transfer' AND ISNULL(stock.isSystemGenerated,0) = 1 THEN 0 ELSE 1 END = 1 "
                    + " AND CASE WHEN ISNULL(voucher_Type.parentVoucher,'') = 'Physical Stock' AND (ISNULL(stock.recQty,0) > 0 OR ISNULL(stock.isuQty,0) < 0) THEN 1 ELSE 0 END = 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(docId).longValue() > 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                if (DmShared.iNull(v_SNo).intValue() > 0) {
                    psTemp.setString("v_SNo", DmShared.iNull(v_SNo).toString());
                }
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void updateStockValueForStockTransferReceiveRecord(String filterItemType, String docId, String v_SNo, String refV_SNo, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", innerCondition = "", outerCondition = "", joinItem = "";;

        try {
            if (!DmShared.xNull(filterItemType).trim().equals("")) {
                joinItem = "	INNER JOIN itemMast im ON im.code = stk.itemCode ";
                innerCondition += "   AND im.filterItemType = :filterItemType ";
            }
            if (DmShared.iNull(docId).longValue() > 0) {
                innerCondition += "         AND stk.docid = :docid ";
            }
            if (DmShared.iNull(refV_SNo).intValue() > 0) {
                innerCondition += " 	AND stk.v_SNo = :refV_SNo ";
            }
            if (DmShared.iNull(v_SNo).intValue() > 0) {
                outerCondition += " AND stock.v_SNo = :v_SNo ";
            }
            sqlQuery = "UPDATE stock "
                    + " SET stock.skuValue = vStk.skuValue "
                    + " , stock.landedSkuValue = vStk.landedSkuValue "
                    + " FROM voucher_Type, ( "
                    + " 	SELECT stk.docId, stk.v_SNo, stk.skuValue, "
                    + "         (ISNULL(stk.landedSkuValue, 0) + (0 - ISNULL(stk.otherAdjustment, 0))) AS landedSkuValue "
                    + " 	FROM stock stk " + joinItem
                    + "         INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type "
                    + " 	WHERE ISNULL(vt.parentVoucher,'') = 'Stock Transfer' "
                    + " 	AND stk.isSystemGenerated = 0 "
                    + "         " + innerCondition + ""
                    + " 	) vStk "
                    + " WHERE stock.docId = vStk.docId "
                    + " AND stock.refV_SNo = vStk.v_SNo "
                    + " AND stock.v_Type = voucher_Type.v_Type "
                    + " AND ISNULL(voucher_Type.parentVoucher,'') = 'Stock Transfer' "
                    + " AND stock.isSystemGenerated = 1 "
                    + " " + outerCondition + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(filterItemType).trim().equals("")) {
                psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode(filterItemType, "manualCode", "itemType", "code", cnTemp)));
            }
            if (DmShared.iNull(docId).longValue() > 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
            }
            if (DmShared.iNull(refV_SNo).intValue() > 0) {
                psTemp.setString("refV_SNo", DmShared.iNull(refV_SNo).toString());
            }
            if (DmShared.iNull(v_SNo).intValue() > 0) {
                psTemp.setString("v_SNo", v_SNo);
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private static void updateItemBatchStock(String ibCode, String itemCode, String departmentCode, String site_Code, String deleteDocId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery, balanceQuantity = "";

        try {
            if (DmShared.iNull(deleteDocId).longValue() == 0) {
                balanceQuantity = " ISNULL(SUM(stk.recQty),0) - ISNULL(SUM(stk.isuQty),0) ";
            } else {
                balanceQuantity = " SUM(CASE WHEN stk.docId = :docId THEN 0 ELSE 1 END * ISNULL(stk.recQty,0)) - SUM(CASE WHEN stk.docId = :docId THEN 0 ELSE 1 END * ISNULL(stk.isuQty,0)) ";
            }

            sqlQuery = "MERGE itemBatchStock AS Target "
                    + " USING ( "
                    + "	SELECT stk.ibCode, stk.itemCode, stk.departmentCode, stk.site_Code "
                    + "	, " + balanceQuantity + " AS balanceQty "
                    + "	FROM stock stk "
                    + "	INNER JOIN itemBatch ib ON ib.code = stk.ibCode "
                    + "	WHERE 1 = 1 ";
            if (DmShared.iNull(ibCode).longValue() > 0) {
                sqlQuery += "	AND stk.ibCode = :ibCode ";
            }

            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlQuery += "	AND stk.itemCode = :itemCode ";
            }

            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlQuery += "	AND stk.departmentCode = :departmentCode ";
            }

            if (DmShared.iNull(site_Code).intValue() > 0) {
                sqlQuery += "	AND stk.site_Code = :site_Code ";
            }

            sqlQuery += " 	GROUP BY stk.ibCode, stk.itemCode, stk.departmentCode, stk.site_Code "
                    + " 	) as Source "
                    + " ON Target.itemCode = Source.itemCode  "
                    + " AND Target.departmentCode = Source.departmentCode "
                    + " AND Target.site_Code = Source.site_Code "
                    + " AND Target.ibCode = Source.ibCode ";
            sqlQuery += " WHEN MATCHED THEN "
                    + "     UPDATE SET Target.balanceQty = Source.balanceQty "
                    + " WHEN NOT MATCHED BY TARGET THEN "
                    + "     INSERT (ibCode, itemCode, departmentCode, site_Code, balanceQty) "
                    + "    VALUES (Source.ibCode, Source.itemCode, Source.departmentCode, Source.site_Code, Source.balanceQty);";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(deleteDocId).longValue() > 0) {
                psTemp.setString("docid", deleteDocId);
            }

            if (DmShared.iNull(ibCode).longValue() > 0) {
                psTemp.setString("ibCode", ibCode);
            }

            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", departmentCode);
            }

            if (DmShared.iNull(site_Code).longValue() > 0) {
                psTemp.setString("site_Code", site_Code);
            }

            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }

            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static void deleteUnusedItemBatches(String ibCodeCsv, String itemCode, Connection cnTemp) throws Exception {
        String newCSVibCode = "";
        String[] ibCodeArr = null, arrIBCodeCsv = null;
        int i = 0, j = 0, k = 0;
        try {

            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                arrIBCodeCsv = ibCodeCsv.split(",");
                j = 0;
                k = 0;
                ibCodeArr = new String[DmShared.iNull((arrIBCodeCsv.length / 2000)).intValue() + 1];
                while (j < arrIBCodeCsv.length) {
                    for (i = j; (i < (j + 2000) && i < arrIBCodeCsv.length); i++) {
                        if (newCSVibCode.equalsIgnoreCase("")) {
                            newCSVibCode = arrIBCodeCsv[i];
                        } else {
                            newCSVibCode += "," + arrIBCodeCsv[i];
                        }
                    }
                    ibCodeArr[k] = newCSVibCode;
                    k++;
                    newCSVibCode = "";
                    j = i;
                }
                if (ibCodeArr.length > 0) {
                    for (int l = 0; l < ibCodeArr.length; l++) {
                        if (ibCodeArr[l] != null) {
                            deleteUnusedItemBatchesWithBatchNo(ibCodeArr[l], itemCode, cnTemp);
                        }
                    }
                }
            } else {
                deleteUnusedItemBatchesWithBatchNo("", itemCode, cnTemp);
            }

//            //Delete Unused ItemBatches From ItemBatchStock Table
//            sqlQuery = "DELETE FROM ItemBatchStock "
//                    + " WHERE ibCode IN ( "
//                    + " 	SELECT ib.code "
//                    + " 	FROM itemBatch ib "
//                    + " 	LEFT JOIN stock stk ON ib.code = stk.ibCode "
//                    + " 	WHERE stk.ibCode IS NULL " + condition
//                    + " 	)";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
//                if (arrIBCodeCsv.length > 2000) {
//                    if (ibCodeArr.length > 0) {
//                        for (int l = 0; l < ibCodeArr.length; l++) {
//                            psTemp.setStringList("ibCode" + DmShared.iNull(l).toString(), DmShared.xNull(ibCodeArr[l]));
//                        }
//                    }
//
//                } else {
//                    psTemp.setStringList("ibCode", DmShared.xNull(ibCodeCsv));
//                }
//            }
//            if (DmShared.iNull(itemCode).longValue() > 0) {
//                psTemp.setString("itemCode", itemCode);
//            }
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            //Delete Unused ItemBatches From ItemStock Table
//            sqlQuery = "DELETE FROM ItemStock "
//                    + " WHERE ibCode IN ( "
//                    + " 	SELECT ib.code "
//                    + " 	FROM itemBatch ib "
//                    + " 	LEFT JOIN stock stk ON ib.code = stk.ibCode "
//                    + " 	WHERE stk.ibCode IS NULL " + condition
//                    + " 	)";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
//                if (arrIBCodeCsv.length > 2100) {
//                    if (ibCodeArr.length > 0) {
//                        for (int l = 0; l < ibCodeArr.length; l++) {
//                            psTemp.setStringList("ibCode" + DmShared.iNull(l).toString(), DmShared.xNull(ibCodeArr[l]));
//                        }
//                    }
//
//                } else {
//                    psTemp.setStringList("ibCode", DmShared.xNull(ibCodeCsv));
//                }
//            }
//            if (DmShared.iNull(itemCode).longValue() > 0) {
//                psTemp.setString("itemCode", itemCode);
//            }
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            //Delete Unused ItemBatches From ItemBatch Table
//            sqlQuery = "DELETE itemBatch "
//                    + " FROM itemBatch ib "
//                    + " LEFT JOIN stock stk ON ib.code = stk.ibCode "
//                    + " WHERE stk.ibCode IS NULL " + condition;
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("ibCode", DmShared.xNull(ibCodeCsv));
//            }
//            if (DmShared.iNull(itemCode).longValue() > 0) {
//                psTemp.setString("itemCode", itemCode);
//            }
//            psTemp.executeUpdate();
//            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    private static void deleteUnusedItemBatchesWithBatchNo(String ibCodeCsv, String itemCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";

        try {
            if (DmShared.iNull(itemCode).longValue() > 0) {
                condition += " and ib.itemCode = :itemCode";
            }

            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                condition += " and ib.code in (" + DmServer.getParamList(":ibCode", ibCodeCsv) + " )";
            }

            //Delete Unused ItemBatches From ItemBatchStock Table
            sqlQuery = "DELETE FROM ItemBatchStock "
                    + " WHERE ibCode IN ( "
                    + " 	SELECT ib.code "
                    + " 	FROM itemBatch ib "
                    + " 	LEFT JOIN stock stk ON ib.code = stk.ibCode "
                    + " 	WHERE stk.ibCode IS NULL " + condition
                    + " 	)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("ibCode", DmShared.xNull(ibCodeCsv));
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            psTemp.executeUpdate();
            psTemp.close();

            //Delete Unused ItemBatches From ItemStock Table
            sqlQuery = "DELETE FROM ItemStock "
                    + " WHERE ibCode IN ( "
                    + " 	SELECT ib.code "
                    + " 	FROM itemBatch ib "
                    + " 	LEFT JOIN stock stk ON ib.code = stk.ibCode "
                    + " 	WHERE stk.ibCode IS NULL " + condition
                    + " 	)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("ibCode", DmShared.xNull(ibCodeCsv));
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            psTemp.executeUpdate();
            psTemp.close();

            //Delete Unused ItemBatches From ItemBatch Table
            sqlQuery = "DELETE itemBatch "
                    + " FROM itemBatch ib "
                    + " LEFT JOIN stock stk ON ib.code = stk.ibCode "
                    + " WHERE stk.ibCode IS NULL " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("ibCode", DmShared.xNull(ibCodeCsv));
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                psTemp.setString("itemCode", itemCode);
            }
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static LogFile_Mdl getFile(DmRSSPrivate dmRss) throws Exception {
        DmHandelFile handelFile = null;
        LogFile_Mdl logFile_Mdl = new LogFile_Mdl();
        String fullFilePath = "";
        try {

            fullFilePath = filePath(dmRss);
            handelFile = new DmHandelFile(fullFilePath, true);

            logFile_Mdl.setFilePath(fullFilePath);
            logFile_Mdl.setHandelFile(handelFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
            } catch (Exception e) {
            }
        }
        return logFile_Mdl;
    }

    private static String filePath(DmRSSPrivate dmRss) {
        String downloadFileName, downloadDirectoryPath, fileFullPath, sessionId;
        downloadFileName = "fifoError" + String.valueOf(Calendar.getInstance().getTime().getTime()) + ".csv";

        sessionId = ((DmRequestWrapper) dmRss.getLocalRequest()).getSession().getId();
        downloadDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "fifo";
        downloadDirectoryPath = downloadDirectoryPath + File.separatorChar + "downloads";
        new File(downloadDirectoryPath).mkdirs();
        fileFullPath = downloadDirectoryPath + File.separatorChar + downloadFileName;

        return fileFullPath;
    }
}
