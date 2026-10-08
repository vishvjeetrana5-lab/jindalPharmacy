package aarogya.inventory.stockSummary.server;

import aarogya.inventory.stockSummary.shared.DefaultValue_Mdl;
import aarogya.inventory.stockSummary.shared.StockSummary_Mdl;
import aarogya.inventory.stockSummary.shared.SummarySetting_Mdl;
import aarogya.inventory.stockSummary.shared.GrdStockSummary_Mdl;
import aarogya.inventory.stockSummary.shared.StockSummary_Srv;
import aarogya.inventory.stockSummary.shared.TxtHItemName_Mdl;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StockSummary_SrvImpl extends DmRSSPrivate implements StockSummary_Srv {

    @Override
    public DmResultPageing<StockSummary_Mdl> getParticulars(KeyFieldExtended keyField, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<StockSummary_Mdl> rstTemp = new DmResultPageing<>();
        StockSummary_Mdl mrTemp = new StockSummary_Mdl();
        List<GrdStockSummary_Mdl> lstTemp = new ArrayList<GrdStockSummary_Mdl>();
        GrdStockSummary_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", tableName = "";
        rstTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            tableName = getTable(this);
            sqlQuery = "SELECT temp.orderIndex, temp.sku, temp.skuName, temp.decimalPlaces, temp.propertyList"
                    + " , temp.particularCode, temp.particulars, temp.openingStock, temp.openingValue "
                    + " , temp.purchaseQty, temp.otherReceiveQty, temp.saleQty, temp.otherIssueQty "
                    + " , temp.closingStock, temp.closingValue, temp.isItem"
                    + " FROM " + tableName + " AS temp ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdStockSummary_Mdl();
                int decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();

                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("particularCode")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("particulars")));
                node.setUnitCode(DmShared.iNull(rsTemp.getString("sku")).toString());
                node.setUnitName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

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

                node.setIsItem(DmServer.vNull(rsTemp.getString("isItem"), "0"));

                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

            mrTemp.getListStockDetail().addAll(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupBy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Department");
            listNode.setSharedKeyFieldName("Department");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Type");
            listNode.setSharedKeyFieldName("Item Type");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Category");
            listNode.setSharedKeyFieldName("Item Category");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Group");
            listNode.setSharedKeyFieldName("Item Group");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Make");
            listNode.setSharedKeyFieldName("Item Make");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Name");
            listNode.setSharedKeyFieldName("Item Name");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStockStatus(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<DefaultValue_Mdl> getDefault(SummarySetting_Mdl setting_Mdl) {
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
        String groupBy = "", addQuery = "", joinItemBatch = "", openingValueSql = "", closingValueSql = "";
        boolean flagDepartment = false;

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
            if (DmShared.xNull(setting_Mdl.getStockStatus()).equalsIgnoreCase("Zero Stock")) {
                condition = " and isNull(vIStk.closingStock,0) = 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStockStatus()).equalsIgnoreCase("Positive Stock")) {
                condition = " and isNull(vIStk.closingStock,0) > 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStockStatus()).equalsIgnoreCase("Negative Stock")) {
                condition = " and isNull(vIStk.closingStock,0) < 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStockStatus()).equalsIgnoreCase("Negative Stock,Positive Stock")) {
                condition = " and isNull(vIStk.closingStock,0) <> 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStockStatus()).equalsIgnoreCase("Negative Stock,Zero Stock")) {
                condition = " and isNull(vIStk.closingStock,0) <= 0 ";
            } else if (DmShared.xNull(setting_Mdl.getStockStatus()).equalsIgnoreCase("Zero Stock,Positive Stock")) {
                condition = " and isNull(vIStk.closingStock,0) >= 0 ";
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
            if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                condition += " and im.code in (" + DmServer.getParamList(":itemCode", setting_Mdl.getItemCode()) + " )";
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

            if (!DmShared.dNull(setting_Mdl.getToDate()).equalsIgnoreCase("")) {
                innerCondition += " and sk.v_Date <= :toDate ";
            }

            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                innerCondition += " and sk.departmentCode in (" + DmServer.getParamList(":departmentCode", setting_Mdl.getDepartmentCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                innerCondition += " and sk.site_Code in (" + DmServer.getParamList(":site_Code", setting_Mdl.getSiteCode()) + " )";
            } else {
                innerCondition += " and sk.site_Code = :site_Code ";
            }

            if (!DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("")) {
                groupBy = " , im.code ";
                particularsField = "max(im.name)";
                addQuery = " , max(im.code) as particularCode, " + particularsField + " as particulars ";
                if (DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("Item Type")) {
                    condition += " and im.itemType = :particularCode ";
                } else if (DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("Item Category")) {
                    condition += " and im.itemCategoryCode = :particularCode ";
                } else if (DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("Item Group")) {
                    condition += " and im.itemGroup = :particularCode ";
                } else if (DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("Item Make")) {
                    condition += " and im.itemMakeCode = :particularCode ";
                } else if (DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("Department")) {
                    innerCondition += " and sk.departmentCode = :particularCode ";
                }
            } else {
                if ((DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Type"))) {
                    groupBy = " , im.itemType ";
                    particularsField = "max(it.name)";
                    addQuery = " , max(im.itemType) as particularCode, " + particularsField + " as particulars ";
                } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Category")) {
                    groupBy = " , im.itemCategoryCode ";
                    particularsField = "max(icm.name)";
                    addQuery = " , max(im.itemCategoryCode) as particularCode, " + particularsField + " as particulars ";
                } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Group")) {
                    groupBy = " , im.itemGroup ";
                    particularsField = "max(ig.name)";
                    addQuery = " , max(im.itemGroup) as particularCode, " + particularsField + " as particulars ";
                } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Make")) {
                    groupBy = " , im.itemMakeCode ";
                    particularsField = "max(imm.companyName)";
                    addQuery = " , max(im.itemMakeCode) as particularCode, " + particularsField + " as particulars ";
                } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Name")) {
                    groupBy = " , im.code ";
                    particularsField = "max(im.name)";
                    addQuery = " , max(im.code) as particularCode, " + particularsField + " as particulars ";
                } else if (DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Department")) {
                    groupBy = " , im.departmentCode ";
                    particularsField = "max(sgd.name)";
                    addQuery = " , max(im.departmentCode) as particularCode, " + particularsField + " as particulars ";
                    if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                        condition += " and im.departmentCode in (" + DmServer.getParamList(":departmentCode", setting_Mdl.getDepartmentCode()) + " )";
                    }
                    flagDepartment = true;
                }
            }

//            addQuery += " , sum(vIStk.amount) as amount ";
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
                    + " create  table " + tableName + " (orderIndex bigint, sku int"
                    + " , skuName varchar(10)"
                    + " , decimalPlaces tinyInt"
                    + " , propertyList varchar(max) "
                    + " , particularCode bigInt "
                    + " , particulars varchar(max)"
                    + " , openingStock decimal(18,7)"
                    + " , openingValue float"
                    + " , purchaseQty decimal(18,7)"
                    + " , otherReceiveQty decimal(18,7)"
                    + " , saleQty decimal(18,7)"
                    + " , otherIssueQty decimal(18,7)"
                    + " , closingStock decimal(18,7)"
                    + " , closingValue float"
                    + " , isItem tinyInt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            sqlQuery = "select im.sku, max(um.unit) as skuName "
                    + " , max(um.decimalPlaces) as decimalPlaces,max(im.propertyList) as propertyList "
                    + " , sum(vIStk.openingStock) as openingStock, sum(vIStk.openingValue) as openingValue   "
                    + " , sum(vIStk.purchaseQty) as purchaseQty, sum(vIStk.otherReceiveQty) as otherReceiveQty   "
                    + " , sum(vIStk.saleQty) as saleQty, sum(vIStk.otherIssueQty) as otherIssueQty   "
                    + " , sum(vIStk.closingStock) as closingStock, sum(vIStk.closingValue) as closingValue   " + addQuery;
            if (flagDepartment) {
                sqlQuery += " FROM ( "
                        + "   	SELECT im1.code, im1.name, dm1.subCode AS departmentCode, im1.sku,       "
                        + " 	im1.itemCategoryCode, im1.itemGroup, im1.itemMakeCode, im1.itemType, "
                        + "     im1.isNarcotics, im1.isConsignment, im1.isHighValue, im1.isBilledItem, "
                        + "     im1.isFlowThrough, im1.isLifeSaving,       "
                        + " 	im1.propertyList, im1.filterItemType, im1.isCommon	 "
                        + " 	FROM itemMast im1, departmentMast dm1  	 "
                        + " 	) AS im   "
                        + " LEFT JOIN subGroup sgd ON sgd.subCode = im.departmentCode   ";
            } else {
                sqlQuery += " from itemMast as im ";
            }
            sqlQuery += " left join unitMast as um on im.sku = um.code    "
                    + " left join itemCat as icm on im.itemCategoryCode = icm.code    "
                    + " left join itemGroup as ig on im.itemGroup = ig.code    "
                    + " left join itemMakeMast as imm on im.itemMakeCode = imm.code    "
                    + " left join itemType as it on im.itemType = it.code    "
                    + " left join ( "
                    + " 			SELECT sk.itemCode " + (flagDepartment ? ", sk.departmentCode" : "") + " "
                    + " 			, SUM(CASE WHEN sk.v_Date < :fromDate THEN isNull(sk.recQty,0) - isNull(sk.isuQty,0) ELSE 0 END) openingStock " + openingValueSql
                    + "                         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + "                                         CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN ISNULL(sk.recQty,0) ELSE 0 END "
                    + "                                     ELSE 0  "
                    + "                                 END) AS purchaseQty "
                    + "                         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + "                                     CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0 "
                    + "                                         WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0 "
                    + "						ELSE CASE WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty,0) ELSE 0 END "
                    + "                                     END "
                    + "					ELSE 0  "
                    + "				END) AS otherReceiveQty "
                    + "                         , SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + " 					CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN ISNULL(sk.isuQty,0) ELSE 0 END  "
                    + "                                     ELSE 0  "
                    + "                                 END) AS saleQty "
                    + " 			, SUM(CASE WHEN sk.v_Date BETWEEN :fromDate AND :toDate THEN  "
                    + " 							CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0 "
                    + " 								WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0 "
                    + " 								ELSE CASE WHEN ISNULL(sk.recQty,0) < 0 THEN ABS(ISNULL(sk.recQty,0)) WHEN ISNULL(sk.isuQty,0) > 0 THEN ISNULL(sk.isuQty,0) ELSE 0 END "
                    + " 							END  "
                    + " 						ELSE 0  "
                    + " 					END) AS otherIssueQty "
                    + " 			, SUM(isNull(sk.recQty,0) - isNull(sk.isuQty,0)) closingStock " + closingValueSql
                    + " 			FROM stock as sk "
                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sk.v_Type " + joinItemBatch
                    + " 			WHERE 1 = 1   " + innerCondition
                    + " 			GROUP BY sk.itemCode " + (flagDepartment ? ", sk.departmentCode" : "") + " "
                    + " 		) as vIStk on im.code = vIStk.itemCode " + (flagDepartment ? " AND im.departmentCode = vIStk.departmentCode " : "")
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) " + condition
                    + " GROUP BY im.sku " + groupBy;
            String orderByClause = "";
            if (DmShared.xNull(setting_Mdl.getSorting()).equalsIgnoreCase("Quantity Ascending")) {
                orderByClause = " ORDER BY  sum(vIStk.closingStock)";
            } else if (DmShared.xNull(setting_Mdl.getSorting()).equalsIgnoreCase("Quantity Descending")) {
                orderByClause = " ORDER BY  sum(vIStk.closingStock) DESC ";
            } else if (DmShared.xNull(setting_Mdl.getSorting()).equalsIgnoreCase("Value Ascending")) {
                orderByClause = " ORDER BY  sum(vIStk.closingValue) ";
            } else if (DmShared.xNull(setting_Mdl.getSorting()).equalsIgnoreCase("Value Descending")) {
                orderByClause = " ORDER BY  sum(vIStk.closingValue) DESC ";
            } else {
                orderByClause = " ORDER BY " + particularsField + ", max(um.unit) ";
            }
            sqlQuery += orderByClause;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.dNull(setting_Mdl.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(setting_Mdl.getFromDate())));
            }
            if (!DmShared.dNull(setting_Mdl.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(setting_Mdl.getToDate())));
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
            if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(setting_Mdl.getItemCode()));
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
            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(setting_Mdl.getSiteCode()));
            } else {
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            }
            if (!DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase("")) {
                psTemp.setString("particularCode", DmShared.iNull(setting_Mdl.getParticularCode()).toString());
            }
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));

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
                        + " orderIndex, sku, skuName, decimalPlaces, propertyList"
                        + " , particularCode, particulars, openingStock, openingValue"
                        + " , purchaseQty, otherReceiveQty, saleQty, otherIssueQty "
                        + " , closingStock, closingValue, isItem"
                        + " ) "
                        + " VALUES (:orderIndex, :sku, :skuName, :decimalPlaces, :propertyList"
                        + " , :particularCode, :particulars, :openingStock, :openingValue "
                        + " , :purchaseQty, :otherReceiveQty, :saleQty, :otherIssueQty "
                        + " , :closingStock, :closingValue, :isItem"
                        + ")";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                psTemp1.setString("sku", DmShared.xNull(rsTemp.getString("sku")));
                psTemp1.setString("skuName", DmShared.xNull(rsTemp.getString("skuName")));
                psTemp1.setString("decimalPlaces", DmServer.vNull(rsTemp.getString("decimalPlaces"), "0"));
                psTemp1.setString("propertyList", DmShared.xNull(rsTemp.getString("propertyList")));
                psTemp1.setString("particularCode", DmShared.xNull(rsTemp.getString("particularCode")));
                psTemp1.setString("particulars", DmShared.xNull(rsTemp.getString("particulars")));
                psTemp1.setString("openingValue", DmServer.vNull(rsTemp.getString("openingValue"), "0.00"));
                psTemp1.setString("openingStock", DmServer.vNull(rsTemp.getString("openingStock"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("purchaseQty", DmServer.vNull(rsTemp.getString("purchaseQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("otherReceiveQty", DmServer.vNull(rsTemp.getString("otherReceiveQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("saleQty", DmServer.vNull(rsTemp.getString("saleQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("otherIssueQty", DmServer.vNull(rsTemp.getString("otherIssueQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("closingValue", DmServer.vNull(rsTemp.getString("closingValue"), "0.00"));
                psTemp1.setString("closingStock", DmServer.vNull(rsTemp.getString("closingStock"), DmShared.getNumberFormat(decimalPlaces)));
                if ((DmShared.xNull(setting_Mdl.getGroupBy()).trim().equalsIgnoreCase("Item Name")) || (!DmShared.xNull(setting_Mdl.getParticularName()).trim().equalsIgnoreCase(""))) {
                    psTemp1.setString("isItem", "1");
                } else {
                    psTemp1.setString("isItem", "0");
                }
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

        return "##t" + sessionName + "_" + "StockSummaryDisplay";
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSorting(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Quantity Ascending");
            listNode.setSharedKeyFieldName("Quantity Ascending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Quantity Descending");
            listNode.setSharedKeyFieldName("Quantity Descending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Value Ascending");
            listNode.setSharedKeyFieldName("Value Ascending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Value Descending");
            listNode.setSharedKeyFieldName("Value Descending");
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
