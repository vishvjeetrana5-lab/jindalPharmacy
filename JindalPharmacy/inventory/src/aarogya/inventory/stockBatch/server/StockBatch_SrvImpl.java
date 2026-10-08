package aarogya.inventory.stockBatch.server;

import aarogya.inventory.stockBatch.shared.BatchNo_Mdl;
import aarogya.inventory.stockBatch.shared.DefaultValue_Mdl;
import aarogya.inventory.stockBatch.shared.StockBatch_Srv;
import aarogya.inventory.stockBatch.shared.GrdStockDetail_Mdl;
import aarogya.inventory.stockBatch.shared.StockBatch_Mdl;
import aarogya.inventory.stockBatch.shared.Setting_Mdl;
import aarogya.inventory.stockBatch.shared.TxtHItemName_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StockBatch_SrvImpl extends DmRSSPrivate implements StockBatch_Srv {

    @Override
    public DmResultPageing<StockBatch_Mdl> getParticulars(KeyFieldExtended keyField, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<StockBatch_Mdl> rstTemp = new DmResultPageing<>();
        StockBatch_Mdl mrTemp = new StockBatch_Mdl();
        List<GrdStockDetail_Mdl> lstTemp = new ArrayList<GrdStockDetail_Mdl>();
        GrdStockDetail_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", tableName = "";
        rstTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            tableName = getTable(this);
            sqlQuery = "SELECT temp.orderIndex, temp.code , temp.batchNo, temp.expiryDate, temp.manufacturingDate, temp.mrp"
                    + " , temp.decimalPlaces, temp.propertyList "
                    + " , temp.openingStock, temp.openingValue, temp.purchaseQty, temp.otherReceiveQty"
                    + " , temp.saleQty, temp.otherIssueQty, temp.closingStock, temp.closingValue "
                    + " FROM " + tableName + " AS temp ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdStockDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setPropertiesDisplay(DmShared.xNull(rsTemp.getString("propertyList")));
                int decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();

                node.setOpeningQty(DmServer.vNull(rsTemp.getString("openingStock")));
                node.setOpeningValue(DmServer.vNull(rsTemp.getString("openingValue"), "0.00"));
                node.setDisplayOpeningValue(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getOpeningValue()).doubleValue())));
                node.setDisplayOpeningQty(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getOpeningQty()).doubleValue()), decimalPlaces, false));

                node.setPurchaseQty(DmServer.vNull(rsTemp.getString("purchaseQty")));
                node.setDisplayPurchaseQty(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getPurchaseQty()).doubleValue()), decimalPlaces, false));

                node.setOtherReceiveQty(DmServer.vNull(rsTemp.getString("otherReceiveQty")));
                node.setDisplayOtherReceiveQty(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getOtherReceiveQty()).doubleValue()), decimalPlaces, false));

                node.setSaleQty(DmServer.vNull(rsTemp.getString("saleQty")));
                node.setDisplaySaleQty(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getSaleQty()).doubleValue()), decimalPlaces, false));

                node.setOtherIssueQty(DmServer.vNull(rsTemp.getString("otherIssueQty")));
                node.setDisplayOtherIssueQty(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getOtherIssueQty()).doubleValue()), decimalPlaces, false));

                node.setClosingQty(DmServer.vNull(rsTemp.getString("closingStock")));
                node.setClosingValue(DmServer.vNull(rsTemp.getString("closingValue"), "0.00"));
                node.setDisplayClosingValue(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getClosingValue()).doubleValue())));
                node.setDisplayClosingQty(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(node.getClosingQty()).doubleValue()), decimalPlaces, false));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListStockDetail().addAll(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(mrTemp);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, name from itemType ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCateory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, name from itemCat ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, name from itemGroup ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemMake(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, companyName as name from itemMakeMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select im.code, im.name, im.manualCode, im.sku, um.unit as skuName "
                    + " from itemMast im  "
                    + " left join unitmast as um on um.code = im.sku  "
                    + " where im.filterItemType = :filterItemType "
                    + " and im.isActive = 1 ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("skuName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PR') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<BatchNo_Mdl>> getTxtHBatchNo(String itemCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<BatchNo_Mdl> lstTemp = new ArrayList<BatchNo_Mdl>();
        BatchNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ib.code"
                    + " , ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.expiryDate"
                    + " , ib.manufacturingDate"
                    + " , ib.colourCode"
                    + " , ib.sizeCode"
                    + " , um.unit AS skuName"
                    + " , icm.name AS colour"
                    + " , ism.name AS size"
                    + " FROM itemBatch ib"
                    + " LEFT JOIN itemColourMast icm ON ib.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON ib.sizeCode = ism.code"
                    + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE im.filterItemType = :filterItemType and ISNULL(ib.batchNo, '') <> ''";
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlQuery += " AND ib.itemCode = :itemCode ";
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlWithParam.setString("itemCode", itemCode);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));

                ItemProperty_Mdl prptyMdl = new ItemProperty_Mdl();
                prptyMdl.setColourName(DmShared.xNull(rsTemp.getString("colour")));
                prptyMdl.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                prptyMdl.setSizeName(DmShared.xNull(rsTemp.getString("size")));
                prptyMdl.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                prptyMdl.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                prptyMdl.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                listNode.setProperties(prptyMdl.getPropertiesCsv());

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Negative Stock");
            listNode.setSharedKeyFieldName("Negative Stock");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Positive Stock");
            listNode.setSharedKeyFieldName("Positive Stock");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Zero Stock");
            listNode.setSharedKeyFieldName("Zero Stock");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Landed Rate");
            listNode.setSharedKeyFieldName("Landed Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Purchase Rate");
            listNode.setSharedKeyFieldName("Purchase Rate");
            lstTemp.add(listNode);

//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("Sale Rate");
//            listNode.setSharedKeyFieldName("Sale Rate");
//            lstTemp.add(listNode);
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("MRP");
            listNode.setSharedKeyFieldName("MRP");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefault(String itemCode, Setting_Mdl setting_Mdl) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        double runningOpeningValue, runningBalanceValue = 0;
        int decimalPlaces = 4;
        long orderIndex;
        String tableName;
        String sqlQuery = "", condition = "", innerCondition = "", valueOnField = "", particularsField = "";
        String openingValueSql = "", closingValueSql = "", joinItemBatch = "";

        try {
            if (DmShared.dNull(setting_Mdl.getFromDate()).trim().equals("")) {
                throw new DmException(0, "From Date can't be blank!");
            }
            if (DmShared.dNull(setting_Mdl.getToDate()).trim().equals("")) {
                throw new DmException(0, "To Date can't be blank!");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT sm.code, sm.name "
                        + " FROM siteMast sm"
                        + " where sm.code = :code";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmServer.getSsnSiteCode(this));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    setting_Mdl.setSiteCode(rsTemp.getString("code"));
                    setting_Mdl.setSiteName(rsTemp.getString("name"));
                }
                rsTemp.close();
                psTemp.close();
            }
            dvm.getSetting_Mdl().setSiteCode(setting_Mdl.getSiteCode());
            dvm.getSetting_Mdl().setSiteName(setting_Mdl.getSiteName());

            /*=================================  Set Conditions ===============================================*/
            if (DmShared.xNull(setting_Mdl.getStatus()).equalsIgnoreCase("Zero Stock")) {
                condition = " and isNull(vStk.closingStock,0) = 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStatus()).equalsIgnoreCase("Positive Stock")) {
                condition = " and isNull(vStk.closingStock,0) > 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStatus()).equalsIgnoreCase("Negative Stock")) {
                condition = " and isNull(vStk.closingStock,0) < 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStatus()).equalsIgnoreCase("Negative Stock,Positive Stock")) {
                condition = " and isNull(vStk.closingStock,0) <> 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStatus()).equalsIgnoreCase("Negative Stock,Zero Stock")) {
                condition = " and isNull(vStk.closingStock,0) <= 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStatus()).equalsIgnoreCase("Zero Stock,Positive Stock")) {
                condition = " and isNull(vStk.closingStock,0) >= 0 ";
            }

            if (!DmShared.dNull(setting_Mdl.getToDate()).equalsIgnoreCase("")) {
                innerCondition += " and sk.v_Date <= :toDate ";
            }

            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                innerCondition += " and sk.site_Code in (" + DmServer.getParamList(":site_Code", setting_Mdl.getSiteCode()) + " )";
            } else {
                innerCondition += " and sk.site_Code = :site_Code ";
            }
            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                innerCondition += " and sk.departmentCode in (" + DmServer.getParamList(":departmentCode", setting_Mdl.getDepartmentCode()) + " )";
            }

            if (!DmShared.xNull(setting_Mdl.getItemCatCode()).equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode in (" + DmServer.getParamList(":itemCategoryCode", setting_Mdl.getItemCatCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getItemGroupCode()).equalsIgnoreCase("")) {
                condition += " and im.itemGroup in (" + DmServer.getParamList(":itemGroup", setting_Mdl.getItemGroupCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getItemMakeCode()).equalsIgnoreCase("")) {
                condition += " and im.itemMakeCode in (" + DmServer.getParamList(":itemMakeCode", setting_Mdl.getItemMakeCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getItemTypeCode()).equalsIgnoreCase("")) {
                condition += " and im.itemType in (" + DmServer.getParamList(":itemType", setting_Mdl.getItemTypeCode()) + " )";
            }
            if (!DmShared.xNull(itemCode).trim().equals("")) {
                condition += " and ib.itemCode = :itemCode";
            } else {
                if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                    condition += " and im.code in (" + DmServer.getParamList(":itemCode", setting_Mdl.getItemCode()) + " )";
                }
            }
            if (!DmShared.xNull(setting_Mdl.getNarcoticsCode()).equalsIgnoreCase("")) {
                condition += " and im.isNarcotics = :narcotics";
            }
            if (!DmShared.xNull(setting_Mdl.getConsignmentItemCode()).equalsIgnoreCase("")) {
                condition += " and im.isConsignment = :consignmentItem";
            }
            if (!DmShared.xNull(setting_Mdl.getHighValueCode()).equalsIgnoreCase("")) {
                condition += " and im.isHighValue = :highValue";
            }
            if (!DmShared.xNull(setting_Mdl.getBilledItemCode()).equalsIgnoreCase("")) {
                condition += " and im.isBilledItem = :billedItem";
            }
            if (!DmShared.xNull(setting_Mdl.getFlowThroughCode()).equalsIgnoreCase("")) {
                condition += " and im.isFlowThrough = :flowThrough";
            }
            if (!DmShared.xNull(setting_Mdl.getLifeSavingCode()).equalsIgnoreCase("")) {
                condition += " and im.isLifeSaving = :lifeSaving";
            }

            if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Landed Rate")) {
//                valueOnField = "sk.landedSkuValue";
                valueOnField = " (ISNULL(sk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(sk.otherAdjustment,0) ELSE 0 END)) ";
            } else if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Purchase Rate")) {
                valueOnField = "sk.skuValue";
            } else if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Sale Rate")) {
                valueOnField = "ib.skuSaleRate";
            } else if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("MRP")) {
                valueOnField = "ib.skuMrp";

            }

            if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Landed Rate")
                    || DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Purchase Rate")) {

                openingValueSql = "			, SUM(CASE WHEN sk.v_Date < :fromDate THEN "
                        + "                                     CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(" + valueOnField + ",0)"
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(" + valueOnField + ",0) 	"
                        + "						WHEN (ISNULL(sk.isuQty,0) + ISNULL(sk.recQty,0) ) = 0	THEN ISNULL(" + valueOnField + ",0)"
                        + "						ELSE 0 "
                        + "                                         END "
                        + "					ELSE 0 "
                        + "                                 END) AS openingValue ";
                closingValueSql = "			, SUM(CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(" + valueOnField + ",0)"
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(" + valueOnField + ",0) 	"
                        + "						WHEN (ISNULL(sk.isuQty,0) + ISNULL(sk.recQty,0) ) = 0	THEN ISNULL(" + valueOnField + ",0)"
                        + "						ELSE 0 "
                        + "					END) AS closingValue ";
            } else if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Sale Rate")
                    || DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("MRP")) {
                joinItemBatch = " 			LEFT JOIN itemBatch ib ON ib.code = sk.ibCode";
                openingValueSql = "			, SUM(CASE WHEN sk.v_Date < :fromDate THEN "
                        + "                                         CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty ,0) "
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(sk.recQty,0)  "
                        + "						ELSE 0	 "
                        + "                                         END	 "
                        + "					ELSE 0	 "
                        + "                                 END * ISNULL(" + valueOnField + ",0) )  AS openingValue ";
                closingValueSql = "			, SUM(CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty ,0) "
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(sk.recQty,0)  "
                        + "						ELSE 0	 "
                        + "					END * ISNULL(" + valueOnField + ",0) )  AS closingValue ";
            }

            //=========================================
            tableName = getTable(this);

            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (orderIndex bigint "
                    + " , code bigInt "
                    + " , batchNo varchar(50)"
                    + " , expiryDate date"
                    + " , manufacturingDate date"
                    + " , mrp float"
                    + " , colour varchar(50) "
                    + " , size varchar(50)"
                    + " , decimalPlaces tinyInt"
                    + " , propertyList varchar(max) "
                    + " , openingStock decimal(18,7)"
                    + " , openingValue float"
                    + " , purchaseQty decimal(18,7)"
                    + " , otherReceiveQty decimal(18,7)"
                    + " , saleQty decimal(18,7)"
                    + " , otherIssueQty decimal(18,7)"
                    + " , closingStock decimal(18,7)"
                    + " , closingValue float"
                    + " ) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            sqlQuery = "select	ib.code, ib.batchNo, ib.expiryDate, ib.mrp"
                    + " , vStk.openingStock, vStk.openingValue "
                    + " , vStk.purchaseQty, vStk.otherReceiveQty "
                    + " , vStk.saleQty, vStk.otherIssueQty "
                    + " , vStk.closingStock, vStk.closingValue "
                    + " , um.decimalPlaces as decimalPlaces"
                    + " , icm.name AS colour, ism.name AS size"
                    + " , ib.manufacturingDate"
                    + " FROM itemBatch as ib"
                    + " LEFT JOIN ("
                    + "     SELECT sk.ibCode "
                    + "         , SUM(CASE WHEN sk.v_Date < :fromDate THEN isNull(sk.recQty,0) - isNull(sk.isuQty,0) ELSE 0 END) openingStock " + openingValueSql
                    + "         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + "                         CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN ISNULL(sk.recQty,0) ELSE 0 END "
                    + "                     ELSE 0  "
                    + "                 END) AS purchaseQty "
                    + "         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + "                       CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0 "
                    + "                           WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0 "
                    + "     			ELSE CASE WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty,0) ELSE 0 END "
                    + "                       END "
                    + "     		ELSE 0  "
                    + "     	END) AS otherReceiveQty "
                    + "         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + "     		CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN ISNULL(sk.isuQty,0) ELSE 0 END  "
                    + "                       ELSE 0  "
                    + "                   END) AS saleQty "
                    + "         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + "     				CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0 "
                    + "     					WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0 "
                    + "     					ELSE CASE WHEN ISNULL(sk.recQty,0) < 0 THEN ABS(ISNULL(sk.recQty,0)) WHEN ISNULL(sk.isuQty,0) > 0 THEN ISNULL(sk.isuQty,0) ELSE 0 END "
                    + "     				END  "
                    + "     			ELSE 0  "
                    + "     		END) AS otherIssueQty "
                    + "         , SUM(isNull(sk.recQty,0) - isNull(sk.isuQty,0)) closingStock " + closingValueSql
                    + "         FROM stock sk "
                    + "         LEFT JOIN voucher_Type vt ON vt.v_Type = sk.v_Type " + joinItemBatch
                    + "         WHERE 1=1 " + innerCondition
                    + "         GROUP BY sk.ibCode "
                    + "     ) AS vStk  ON vStk.ibCode = ib.code "
                    + " left join itemMast as im on ib.itemCode = im.code"
                    + " left join unitMast as um on im.sku = um.code"
                    + " LEFT JOIN itemColourMast icm ON ib.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON ib.sizeCode = ism.code"
                    + " where (im.filterItemType = :filterItemType OR im.isCommon = 1) " + condition
                    + " ORDER BY ib.expiryDate, ib.batchNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
            if (!DmShared.dNull(setting_Mdl.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(setting_Mdl.getFromDate())));
            }
            if (!DmShared.dNull(setting_Mdl.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(setting_Mdl.getToDate())));
            }
            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(setting_Mdl.getSiteCode()));
            } else {
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            }
            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(setting_Mdl.getDepartmentCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getItemCatCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemCategoryCode", DmShared.xNull(setting_Mdl.getItemCatCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getItemGroupCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemGroup", DmShared.xNull(setting_Mdl.getItemGroupCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getItemMakeCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemMakeCode", DmShared.xNull(setting_Mdl.getItemMakeCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getItemTypeCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemType", DmShared.xNull(setting_Mdl.getItemTypeCode()));
            }
            if (!DmShared.xNull(itemCode).trim().equals("")) {
                psTemp.setString("itemCode", DmShared.xNull(itemCode));
            } else {
                if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                    psTemp.setStringList("itemCode", DmShared.xNull(setting_Mdl.getItemCode()));
                }
            }
            if (!DmShared.xNull(setting_Mdl.getNarcoticsCode()).equalsIgnoreCase("")) {
                psTemp.setString("narcotics", DmShared.xNull(setting_Mdl.getNarcoticsCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getConsignmentItemCode()).equalsIgnoreCase("")) {
                psTemp.setString("consignmentItem", DmShared.xNull(setting_Mdl.getConsignmentItemCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getHighValueCode()).equalsIgnoreCase("")) {
                psTemp.setString("highValue", DmShared.xNull(setting_Mdl.getHighValueCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getBilledItemCode()).equalsIgnoreCase("")) {
                psTemp.setString("billedItem", DmShared.xNull(setting_Mdl.getBilledItemCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getFlowThroughCode()).equalsIgnoreCase("")) {
                psTemp.setString("flowThrough", DmShared.xNull(setting_Mdl.getFlowThroughCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getLifeSavingCode()).equalsIgnoreCase("")) {
                psTemp.setString("lifeSaving", DmShared.xNull(setting_Mdl.getLifeSavingCode()));
            }

            rsTemp = psTemp.executeQuery();
            orderIndex = 0;
            runningOpeningValue = 0;
            runningBalanceValue = 0;
            while (rsTemp.next()) {
                orderIndex++;

                decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();

                runningOpeningValue += DmShared.vNull(rsTemp.getString("openingValue")).doubleValue();
                runningBalanceValue += DmShared.vNull(rsTemp.getString("closingValue")).doubleValue();

                sqlQuery = "INSERT INTO " + tableName + " ( "
                        + " orderIndex, code , batchNo, expiryDate, manufacturingDate, mrp"
                        + " , colour, size, decimalPlaces, propertyList "
                        + " , openingStock, openingValue, purchaseQty, otherReceiveQty"
                        + " , saleQty, otherIssueQty, closingStock, closingValue) "
                        + " VALUES (:orderIndex, :code, :batchNo, :expiryDate, :manufacturingDate, :mrp"
                        + " , :colour, :size, :decimalPlaces, :propertyList"
                        + " , :openingStock, :openingValue, :purchaseQty, :otherReceiveQty"
                        + " , :saleQty, :otherIssueQty, :closingStock, :closingValue) ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                psTemp1.setString("code", DmShared.xNull(rsTemp.getString("code")));
                psTemp1.setString("batchNo", DmShared.xNull(rsTemp.getString("batchNo")));
                psTemp1.setDate("expiryDate", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("expiryDate"))));
                psTemp1.setDate("manufacturingDate", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("manufacturingDate"))));
                psTemp1.setString("mrp", DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                psTemp1.setString("colour", DmShared.xNull(rsTemp.getString("colour")));
                psTemp1.setString("size", DmShared.xNull(rsTemp.getString("size")));
                psTemp1.setString("decimalPlaces", DmServer.vNull(rsTemp.getString("decimalPlaces"), "0"));
                psTemp1.setString("openingValue", DmServer.vNull(rsTemp.getString("openingValue"), "0.00"));
                psTemp1.setString("openingStock", DmServer.vNull(rsTemp.getString("openingStock"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("purchaseQty", DmServer.vNull(rsTemp.getString("purchaseQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("otherReceiveQty", DmServer.vNull(rsTemp.getString("otherReceiveQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("saleQty", DmServer.vNull(rsTemp.getString("saleQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("otherIssueQty", DmServer.vNull(rsTemp.getString("otherIssueQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("closingValue", DmServer.vNull(rsTemp.getString("closingValue"), "0.00"));
                psTemp1.setString("closingStock", DmServer.vNull(rsTemp.getString("closingStock"), DmShared.getNumberFormat(decimalPlaces)));

                //============================< Set Item Properties >============================
                ItemProperty_Mdl prptyMdl = new ItemProperty_Mdl();
                prptyMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                prptyMdl.setColourName(DmShared.xNull(rsTemp.getString("colour")));
                prptyMdl.setSizeName(DmShared.xNull(rsTemp.getString("size")));
                prptyMdl.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                prptyMdl.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                prptyMdl.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

                psTemp1.setString("propertyList", DmShared.xNull(prptyMdl.getPropertiesCsv()));
                //============================< ******************* >============================

                psTemp1.executeUpdate();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            dvm.setOpeningValue(DmServer.vNull(runningOpeningValue, "0.00"));
            dvm.setBalanceValue(DmServer.vNull(runningBalanceValue, "0.00"));

            dvm.setDisplayOpeningValue(DmJasper.formatAmount(BigDecimal.valueOf(runningOpeningValue)));
            dvm.setDisplayBalanceValue(DmJasper.formatAmount(BigDecimal.valueOf(runningBalanceValue)));

            cnTemp.close();

            dmrTemp.setData(dvm);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }

        }

        return dmrTemp;
    }

    private String getTable(DmRSSPrivate rssPrivate) {
        String sessionName;

        sessionName = (rssPrivate.getLocalRequest().getSession().getId()).replaceAll("-", "_");

        return "##t" + sessionName + "_" + "StockBatchDisplay";
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
