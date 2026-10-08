package aarogya.inventory.stockLedger.server;

import aarogya.inventory.stockLedger.shared.*;
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

public class StockLedger_SrvImpl extends DmRSSPrivate implements StockLedger_Srv {

    @Override
    public DmResultPageing<StockLedger_Mdl> getParticulars(KeyFieldExtended keyField, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<StockLedger_Mdl> rstTemp = new DmResultPageing<>();
        StockLedger_Mdl mrTemp = new StockLedger_Mdl();
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
            sqlQuery = "SELECT temp.orderIndex, temp.docId, temp.v_No, temp.v_SNo, temp.recId, temp.v_date, temp.v_Type"
                    + ", temp.v_Desc, temp.mnuText, temp.mnuAttachedInModule, temp.serialNo  "
                    + ", temp.batchNo, temp.AcContraName, temp.contraName, temp.openingQty, temp.openingValue "
                    + ", temp.receiveQty, temp.receiveValue, temp.issueQty, temp.issueValue, temp.balanceQty, temp.balanceValue, temp.remark "
                    + ", temp.decimalPlaces, temp.mrp, temp.expiryDate, temp.manufacturingDate, temp.colourName, temp.sizeName"
                    + ", temp.particulars, temp.propertyList, temp.issueReceiveValue "
                    + "FROM " + tableName + " AS temp ";
            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdStockDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setAcName(DmShared.xNull(rsTemp.getString("AcContraName")));
                node.setSubName(DmShared.xNull(rsTemp.getString("contraName")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setMnuText(DmShared.xNull(rsTemp.getString("mnuText")));
                node.setMnuAttachedInModule(DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                node.setVoucherType(DmShared.xNull(rsTemp.getString("v_Desc")));
                node.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setRecQty(DmServer.vNull(rsTemp.getString("receiveQty")));
                node.setIsuQty(DmServer.vNull(rsTemp.getString("issueQty")));
//                node.setValue(DmShared.vNull(rsTemp.getString("issueValue")).doubleValue() == 0 ? DmServer.vNull(rsTemp.getString("receiveValue")) : DmServer.vNull(rsTemp.getString("issueValue")));
                node.setValue(DmServer.vNull(rsTemp.getString("issueReceiveValue"), "0.00"));
                node.setBalance(DmServer.vNull(rsTemp.getString("balanceQty")));
                node.setBalanceValue(DmServer.vNull(rsTemp.getString("balanceValue"), "0.00"));
                node.setPropertiesDisplay(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setParticulars(DmShared.xNull(rsTemp.getString("particulars")));

                int decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();
                node.setIsuQtyDisplay(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(node.getIsuQty())).doubleValue()), decimalPlaces, false));
                node.setRecQtyDisplay(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(node.getRecQty())).doubleValue()), decimalPlaces, false));
                node.setBalanceDisplay(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(node.getBalance())).doubleValue()), decimalPlaces, false));

                node.setValueDisplay(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(node.getValue(), "0.00")).doubleValue())));
                node.setBalanceValueDisplay(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(DmServer.vNull(node.getBalanceValue(), "0.00")).doubleValue())));

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
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select code, name from siteMast ";
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
    public DmResult<DefaultValue_Mdl> getDefault(String itemCode, Setting_Mdl setting_Mdl) {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult<DefaultValue_Mdl>();
        DefaultValue_Mdl dvm = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        double netIssueQty = 0, netReceiveQty = 0, runningBalanceQty, runningBalanceValue = 0, netOpening = 0;
        int decimalPlaces = 4;
        long orderIndex;
        String tableName;
        String sqlQuery = "", condition = "", valueOnField = "";
        String openingValueSql = "", receiveValueSql = "", issueValueSql = "";
        String particulars = "";

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

            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                condition += " and sk.site_Code in (" + DmServer.getParamList(":site_Code", setting_Mdl.getSiteCode()) + " )";
            } else {
                condition += " and sk.site_Code = :site_Code ";
            }
            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " and sk.departmentCode in (" + DmServer.getParamList(":departmentCode", setting_Mdl.getDepartmentCode()) + " )";
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
                condition += " and sk.itemCode = :itemCode";
            } else {
                if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                    condition += " and sk.itemCode in (" + DmServer.getParamList(":itemCode", setting_Mdl.getItemCode()) + " )";
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

                openingValueSql = "			, SUM(CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(" + valueOnField + ",0)"
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(" + valueOnField + ",0) 	"
                        + "						WHEN (ISNULL(sk.isuQty,0) + ISNULL(sk.recQty,0) ) = 0	THEN ISNULL(" + valueOnField + ",0)"
                        + "						ELSE 0 "
                        + "					END) AS opnValue ";

                receiveValueSql = "		, CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueOnField + ",0)"
                        + "				WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0))"
                        + "							WHEN ISNULL(sk.recQty,0) > 0  THEN ISNULL(" + valueOnField + ",0)"
                        + "							WHEN (ISNULL(sk.isuQty,0) + ISNULL(sk.recQty,0) ) = 0  THEN ISNULL(" + valueOnField + ",0)"
                        + "							ELSE 0 "
                        + "					END"
                        + "				END AS recValue";
                issueValueSql = "		, CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN ISNULL(" + valueOnField + ",0)"
                        + "				WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(sk.recQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0))"
                        + "						WHEN ISNULL(sk.isuQty,0) > 0  THEN ISNULL(" + valueOnField + ",0)"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS isuValue";
            } else if (DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("Sale Rate")
                    || DmShared.xNull(setting_Mdl.getValueOn()).trim().equalsIgnoreCase("MRP")) {
                openingValueSql = "			, SUM(CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty ,0) "
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(sk.recQty,0)  "
                        + "						ELSE 0	 "
                        + "					END * ISNULL(" + valueOnField + ",0) )  AS opnValue ";

                receiveValueSql = "		, CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN ISNULL(sk.recQty,0)"
                        + "				WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0))"
                        + "					  WHEN ISNULL(sk.recQty,0) > 0  THEN ISNULL(sk.recQty,0)"
                        + "					  ELSE 0 "
                        + "                                  END  "
                        + "				END  * ISNULL(" + valueOnField + ",0) AS recValue";
                issueValueSql = "		, CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN ISNULL(sk.isuQty,0)"
                        + "				WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(sk.recQty,0) < 0 THEN ABS(ISNULL(sk.recQty,0))"
                        + "					  WHEN ISNULL(sk.isuQty,0) > 0  THEN ISNULL(sk.isuQty,0)"
                        + "					  ELSE 0"
                        + "				    END "
                        + "			END * ISNULL(" + valueOnField + ",0) AS isuValue";
            }

            tableName = getTable(this);

            sqlQuery = "IF OBJECT_ID('tempdb.." + tableName + "') IS NOT NULL "
                    + " DROP TABLE " + tableName + " ;"
                    + " create  table " + tableName + " (orderIndex bigint, docId bigInt"
                    + ", v_No bigint, v_SNo smallInt"
                    + ", recId varchar(20)"
                    + ", v_date date "
                    + ", v_Type smallInt"
                    + ", v_Desc varchar(50)"
                    + ", mnuText varchar(100)"
                    + ", mnuAttachedInModule varchar(100)"
                    + ", serialNo smallInt "
                    + ", batchNo varchar(50)"
                    + ", AcContraName varchar(100) "
                    + ", contraName varchar(100)"
                    + ", openingQty decimal(18,7)"
                    + ", openingValue float "
                    + ", receiveQty decimal(18,7)"
                    + ", receiveValue float "
                    + ", issueQty decimal(18,7)"
                    + ", issueValue float "
                    + ", issueReceiveValue float "
                    + ", balanceQty decimal(18,7)"
                    + ", balanceValue float "
                    + ", remark varchar(max)"
                    + ", decimalPlaces tinyInt"
                    + ", mrp float"
                    + ", expiryDate date "
                    + ", manufacturingDate date "
                    + ", colourName varchar(50) "
                    + ", sizeName varchar(50)"
                    + ", particulars varchar(max)"
                    + ", propertyList varchar(max))";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.execute();
            psTemp.close();

            sqlQuery = " 	select	null as docId"
                    + "		, 0 AS vrNo"
                    + "		, 0 AS v_SNo"
                    + "		, null as recId"
                    + "         , null as vrDate"
                    + "         , null as v_Type"
                    + "         , null as v_Desc"
                    + "         , null as mnuText"
                    + "         , null as mnuAttachedInModule"
                    + "         , null as batchNo"
                    + "		, 'Opening' as acName, null as sName"
                    + "		, 0 as recQty"
                    + "		, 0 AS recValue"
                    + "		, 0 as isuQty"
                    + "		, 0 AS isuValue"
                    + "		, sum(isNull(sk.recQty,0) - isNull(sk.isuQty,0)) opnQty " + openingValueSql
                    + "		, null as remark"
                    + "		, Max(um.decimalPlaces) as decimalPlaces"
                    + "		, null as mrp, null as expiryDate, null as manufacturingDate, null as colour, null as size"
                    + "		, 0 AS serialNo"
                    + "		from stock as sk "
                    + "		left join subGroup as sga on sk.acContraSubCode = sga.subCode"
                    + "		left join subGroup as sgs on sk.stockContraSubCode = sgs.subCode"
                    + "		left join voucher_Type as vt on sk.v_Type = vt.v_Type"
                    + "		left join itemBatch as ib on sk.ibCode = ib.Code"
                    + "		left join itemMast as im on sk.itemCode = im.Code"
                    + "		left join unitMast as um on im.sku = um.code"
                    + "		where (im.filterItemType = :filterItemType OR im.isCommon = 1) and sk.v_date < :fromDate " + condition
                    + "	union all "
                    + "		select	sk.docId"
                    + "		, sk.v_No as vrNo"
                    + "		, sk.v_SNo"
                    + "		, sk.recId"
                    + "         , sk.v_Date as vrDate"
                    + "         , sk.v_Type"
                    + "         , vt.short_Name as v_Desc"
                    + "         , vt.mnuText"
                    + "         , vt.mnuAttachedInModule"
                    + "         , ib.batchNo"
                    + "		, sga.name as acName, sgs.name as sName"
                    + "		, CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN ISNULL(sk.recQty,0)"
                    + "				WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0"
                    + "				ELSE CASE WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty,0) ELSE 0 END"
                    + "			END AS recQty " + receiveValueSql
                    + "		, CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN ISNULL(sk.isuQty,0)"
                    + "				WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN 0"
                    + "				ELSE CASE WHEN ISNULL(sk.recQty,0) < 0 THEN ABS(ISNULL(sk.recQty,0)) WHEN ISNULL(sk.isuQty,0) > 0 THEN ISNULL(sk.isuQty,0) ELSE 0 END"
                    + "				END AS isuQty " + issueValueSql
                    + "		, 0 as opnQty"
                    + "		, 0 as opnValue"
                    + "		, sk.remark, um.decimalPlaces"
                    + "		, ib.mrp, ib.expiryDate, ib.manufacturingDate, icm.name AS colour, ism.name AS size"
                    + "		, vt.serialNo"
                    + "		from stock as sk"
                    + "		left join subGroup as sga on sk.acContraSubCode = sga.subCode"
                    + "		left join subGroup as sgs on sk.stockContraSubCode = sgs.subCode"
                    + "		left join voucher_Type as vt on sk.v_Type = vt.v_Type"
                    + "		left join itemBatch as ib on sk.ibCode = ib.Code"
                    + "		left join itemMast as im on sk.itemCode = im.Code"
                    + "		left join unitMast as um on im.sku = um.code"
                    + "		LEFT JOIN itemColourMast icm ON ib.colourCode = icm.code"
                    + "		LEFT JOIN itemSizeMast ism ON ib.sizeCode = ism.code"
                    + "		where (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + "         and sk.v_date >= :fromDate "
                    + "         and sk.v_date <= :toDate " + condition
                    + " ORDER BY vrDate, serialNo, v_Desc, vrNo, recId, v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
            psTemp.setDate("fromDate", DmServer.convertDate(setting_Mdl.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));

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
            if (!DmShared.xNull(setting_Mdl.getSiteCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(setting_Mdl.getSiteCode()));
            } else {
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            }
            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(setting_Mdl.getDepartmentCode()));
            }

            rsTemp = psTemp.executeQuery();
            orderIndex = 0;
            netOpening = 0;
            netReceiveQty = 0;
            netIssueQty = 0;
            runningBalanceQty = 0;
            runningBalanceValue = 0;
            while (rsTemp.next()) {
                orderIndex++;
                particulars = "";

                if (DmShared.xNull(rsTemp.getString("acName")).trim().equalsIgnoreCase("Opening")) {
                    decimalPlaces = 4;
                    netOpening = DmShared.vNull(DmServer.vNull(rsTemp.getString("opnQty"), DmShared.getNumberFormat(decimalPlaces))).doubleValue();
                } else {
                    decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();
                }

                netReceiveQty += DmShared.vNull(rsTemp.getString("recQty")).doubleValue();
                netIssueQty += DmShared.vNull(rsTemp.getString("isuQty")).doubleValue();

                runningBalanceQty = runningBalanceQty
                        + (DmShared.vNull(DmServer.vNull(rsTemp.getString("opnQty"), DmShared.getNumberFormat(decimalPlaces))).doubleValue()
                        + DmShared.vNull(DmServer.vNull(rsTemp.getString("recQty"), DmShared.getNumberFormat(decimalPlaces))).doubleValue()
                        - DmShared.vNull(DmServer.vNull(rsTemp.getString("isuQty"), DmShared.getNumberFormat(decimalPlaces))).doubleValue());
                runningBalanceValue = runningBalanceValue
                        + (DmShared.vNull(DmServer.vNull(rsTemp.getString("opnValue"), "0.00")).doubleValue()
                        + DmShared.vNull(DmServer.vNull(rsTemp.getString("recValue"), "0.00")).doubleValue()
                        - DmShared.vNull(DmServer.vNull(rsTemp.getString("isuValue"), "0.00")).doubleValue());

                sqlQuery = "insert into " + tableName + " ( "
                        + "orderIndex, docId, v_No, v_SNo, recId, v_date, v_Type, v_Desc, mnuText, mnuAttachedInModule, serialNo "
                        + ", batchNo, AcContraName, contraName, openingQty, openingValue "
                        + ", receiveQty, receiveValue, issueQty, issueValue, balanceQty, balanceValue, remark "
                        + ", decimalPlaces, mrp, expiryDate, manufacturingDate, colourName, sizeName"
                        + ", particulars, propertyList, issueReceiveValue) "
                        + "values (:orderIndex, :docId, :v_No, :v_SNo, :recId, :v_date, :v_Type, :v_Desc, :mnuText, :mnuAttachedInModule, :serialNo "
                        + ", :batchNo, :AcContraName, :contraName, :openingQty, :openingValue "
                        + ", :receiveQty, :receiveValue, :issueQty, :issueValue, :balanceQty, :balanceValue, :remark "
                        + ", :decimalPlaces, :mrp, :expiryDate, :manufacturingDate, :colourName, :sizeName"
                        + ", :particulars, :propertyList, :issueReceiveValue)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("orderIndex", String.valueOf(orderIndex));
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("docId")).toString());
                psTemp1.setString("v_No", DmShared.xNull(rsTemp.getString("vrNo")));
                psTemp1.setString("v_SNo", DmShared.xNull(rsTemp.getString("v_SNo")));
                psTemp1.setString("recId", DmShared.xNull(rsTemp.getString("recId")));
                psTemp1.setDate("v_date", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("vrDate"))));
                psTemp1.setString("v_Type", DmShared.iNull(rsTemp.getString("v_Type")).toString());
                psTemp1.setString("v_Desc", DmShared.xNull(rsTemp.getString("v_Desc")));
                psTemp1.setString("mnuText", DmShared.xNull(rsTemp.getString("mnuText")));
                psTemp1.setString("mnuAttachedInModule", DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                psTemp1.setString("batchNo", DmShared.xNull(rsTemp.getString("batchNo")));
                psTemp1.setString("AcContraName", DmShared.xNull(rsTemp.getString("acName")));
                psTemp1.setString("contraName", DmShared.xNull(rsTemp.getString("sName")));
                psTemp1.setString("openingQty", DmServer.vNull(rsTemp.getString("opnQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("openingValue", DmServer.vNull(rsTemp.getString("opnValue"), "0.00"));
                psTemp1.setString("receiveQty", DmServer.vNull(rsTemp.getString("recQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("receiveValue", DmServer.vNull(rsTemp.getString("recValue"), "0.00"));
                psTemp1.setString("issueQty", DmServer.vNull(rsTemp.getString("isuQty"), DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("issueValue", DmServer.vNull(rsTemp.getString("isuValue"), "0.00"));
                psTemp1.setString("balanceQty", DmServer.vNull(runningBalanceQty, DmShared.getNumberFormat(decimalPlaces)));
                psTemp1.setString("balanceValue", DmServer.vNull(runningBalanceValue, "0.00"));

                if (DmShared.vNull(rsTemp.getString("isuValue")).doubleValue() == 0) {
                    psTemp1.setString("issueReceiveValue", DmServer.vNull(rsTemp.getString("recValue"), "0.00"));
                } else {
                    psTemp1.setString("issueReceiveValue", DmServer.vNull((0 - DmShared.vNull(rsTemp.getString("isuValue"))), "0.00"));
                }

                psTemp1.setString("remark", DmShared.xNull(rsTemp.getString("remark")));
                psTemp1.setString("serialNo", DmShared.iNull(rsTemp.getString("serialNo")).toString());
                psTemp1.setString("decimalPlaces", DmShared.iNull(decimalPlaces).toString());
                psTemp1.setString("mrp", DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                psTemp1.setDate("expiryDate", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("expiryDate"))));
                psTemp1.setDate("manufacturingDate", DmServer.convertDate(DmShared.dNull(rsTemp.getDate("manufacturingDate"))));
                psTemp1.setString("colourName", DmShared.xNull(rsTemp.getString("colour")));
                psTemp1.setString("sizeName", DmShared.xNull(rsTemp.getString("size")));

                //============================< Set particulars >============================
                if (!DmShared.xNull(rsTemp.getString("acName")).trim().equalsIgnoreCase("")) {
                    particulars += DmShared.xNull(rsTemp.getString("acName")).trim();
                }
                if (!DmShared.xNull(rsTemp.getString("sName")).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(particulars).trim().equals("")) {
                        particulars += ", ";
                    }
                    particulars += DmShared.xNull(rsTemp.getString("sName")).trim();
                }
                if (!DmShared.xNull(rsTemp.getString("remark")).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(particulars).trim().equals("")) {
                        particulars += ", ";
                    }
                    particulars += DmShared.xNull(rsTemp.getString("remark")).trim();
                }
                psTemp1.setString("particulars", DmShared.xNull(particulars));
                //============================< ******************* >============================

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

            if (DmShared.iNull(itemCode).longValue() == 0) {
                decimalPlaces = 4;
            }
            dvm.setOpeningQty(DmJasper.formatAmount(BigDecimal.valueOf(netOpening), decimalPlaces, false));
            dvm.setReceiveQty(DmJasper.formatAmount(BigDecimal.valueOf(netReceiveQty), decimalPlaces, false));
            dvm.setIssueQty(DmJasper.formatAmount(BigDecimal.valueOf(netIssueQty), decimalPlaces, false));
            dvm.setBalanceQty(DmJasper.formatAmount(BigDecimal.valueOf(runningBalanceQty), decimalPlaces, false));
            dvm.setBalanceValue(DmJasper.formatAmount(BigDecimal.valueOf(runningBalanceValue)));
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

        return "##t" + sessionName + "_" + "StockLedgerDisplay";
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
