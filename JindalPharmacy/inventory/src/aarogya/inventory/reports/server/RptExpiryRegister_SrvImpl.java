package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptExpiryRegister_Srv;
import aarogya.inventory.reports.shared.RptExpiryRegister_Vld;
import aarogya.inventory.reports.shared.TxtHBatchNo_Mdl;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptExpiryRegister_SrvImpl extends DmRSSPrivate implements RptExpiryRegister_Srv {

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", mainCondition = "", subQueryCondition = "", valueOnField = "", expiryCondition = "", groupByFieldCode = "", groupByFieldName = "";

        try {
            RptExpiryRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                mainCondition += " and  temp.expiryDate >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                mainCondition += " and  temp.expiryDate <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " AND  sk.departmentCode IN (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and sk.ibCode in (" + DmServer.getParamList(":batchNo", filter.get("batchNo").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and sk.itemCode in (" + DmServer.getParamList(":item", filter.get("item").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and im.itemGroup in (" + DmServer.getParamList(":itemGroup", filter.get("itemGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemCat").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and im.itemCategoryCode in (" + DmServer.getParamList(":itemCat", filter.get("itemCat").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Item Category")) {
                groupByFieldCode = " max(im.itemCategoryCode) ";
                groupByFieldName = " max(icm.name)  ";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Item Group")) {
                groupByFieldCode = "  max(im.itemGroup) ";
                groupByFieldName = " max(ig.name)  ";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Item Company")) {
                groupByFieldCode = "  max(im.itemMakeCode) ";
                groupByFieldName = " isNull(max(imm.companyName),'-')";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Item Name")) {
                groupByFieldCode = "  max(sk.itemcode) ";
                groupByFieldName = " max(im.name)";
            }
            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")) {
//                valueOnField = "sk.landedSkuValue";
                valueOnField = " (ISNULL(sk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(sk.otherAdjustment,0) ELSE 0 END)) ";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {
                valueOnField = "sk.skuValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")) {
                valueOnField = "ib.skuMrp";
            }
            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {
                expiryCondition = "	, SUM("
                        + "			CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(" + valueOnField + ",0)						"
                        + "					WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0))						"
                        + "					WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueOnField + ",0))						"
                        + "					WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(" + valueOnField + ",0) 							"
                        + "					WHEN (ISNULL(sk.isuQty,0) + ISNULL(sk.recQty,0) ) = 0	THEN ISNULL(" + valueOnField + ",0)						"
                        + "					ELSE 0 					"
                        + "			END"
                        + "		) AS expiryValue ";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")) {
                expiryCondition = "			, SUM(CASE WHEN ISNULL(sk.recQty,0) > 0 THEN ISNULL(sk.recQty ,0) "
                        + "						WHEN ISNULL(sk.isuQty,0) < 0 THEN ABS(ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.isuQty,0) > 0 THEN (0 - ISNULL(sk.isuQty,0)) "
                        + "						WHEN ISNULL(sk.recQty,0) < 0 THEN ISNULL(sk.recQty,0)  "
                        + "						ELSE 0	 "
                        + "					END * ISNULL(" + valueOnField + ",0) )  AS expiryValue ";
            }

            sqlQuery = "select temp.*"
                    + " from ("
                    + "     select ib.code"
                    + "     	, MAX(sk.itemCode) AS itemCode ," + groupByFieldCode + "      as groupCode," + groupByFieldName + "     as groupName"
                    + "     	, MAX(im.name) AS itemName"
                    + "     	, MAX(ib.batchNo) AS batchNo"
                    + "     	, sum(isNull(sk.recQty,0) - isNull(sk.isuQty,0)) balanceQty" + expiryCondition
                    + "     	, Max(um.decimalPlaces) as decimalPlaces		"
                    + "     	, MAX(ib.mrp) AS mrp"
                    + "     	, MAX(ib.saleRate) AS saleRate"
                    + "     	, MAX(ib.skuMrp) AS skuMrp"
                    + "     	, MAX(ib.skuSaleRate) AS skuSaleRate"
                    + "     	, MAX(ib.expiryDate) AS expiryDate"
                    + "     	, MAX(ib.manufacturingDate) AS manufacturingDate"
                    + "     	from stock as sk 		"
                    + "     	left join subGroup as sga on sk.acContraSubCode = sga.subCode		"
                    + "     	left join subGroup as sgs on sk.stockContraSubCode = sgs.subCode		"
                    + "     	left join voucher_Type as vt on sk.v_Type = vt.v_Type		"
                    + "     	left join itemBatch as ib on sk.ibCode = ib.Code		"
                    + "     	left join itemMast as im on sk.itemCode = im.Code		"
                    + "         left join itemCat as icm on im.itemcategoryCode=icm.code"
                    + "         left join itemGroup as ig on im.itemGroup=ig.code "
                    + "         left join itemMakeMast as imm on im.itemmakeCode=imm.code"
                    + "     	left join unitMast as um on im.sku = um.code		"
                    + "     	where im.filterItemType = :filterItemType " + subQueryCondition
                    + "     	and sk.site_Code = :site_Code"
                    + "     	GROUP BY ib.code "
                    + "     	HAVING sum(isNull(sk.recQty,0) - isNull(sk.isuQty,0)) > 0"
                    + "     ) as temp  "
                    + " where 1=1 " + mainCondition
                    + " order by temp.groupName, temp.expiryDate";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("Inventory_ExpiryRegister");
            jasperPrint = DmServer.fillJasper(this, "Expiry Register", jasperReport, null, filter, rsTemp);

            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("batchNo", DmShared.xNull(filter.get("batchNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("item", DmShared.xNull(filter.get("item").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCat", DmShared.xNull(filter.get("itemCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemGroup", DmShared.xNull(filter.get("itemGroup").getTag()));
            }
        } catch (Exception e) {
        }

    }

    @Override
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" select im.code,im.name,um.code as unitCode,um.unit,im.manualCode "
                    + " from itemMast as im "
                    + " left join unitmast as um on im.sku=um.code "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCat(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select ic.code, ic.name "
                    + " from itemcat ic "
                    + " where (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT CODE, NAME, filteritemType "
                    + " FROM itemgroup  "
                    + " where (filterItemType = :filterItemType or  isCommon=1");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Landed Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Purchase Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("M");
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
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<TxtHBatchNo_Mdl>();
        TxtHBatchNo_Mdl listNode;
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
                    + " , ib.saleRate"
                    + " , ib.expiryDate"
                    + " , um.unit AS skuName,im.name as itemName "
                    + " FROM itemBatch ib"
                    + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE  im.filterItemType = :filterItemType and ISNULL(ib.batchNo, '') <> ''";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));

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
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PR') and sg.isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupBy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Category");
            listNode.setSharedKeyFieldName("Item Category");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Group");
            listNode.setSharedKeyFieldName("Item Group");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Item Company");
            listNode.setSharedKeyFieldName("Item Company");
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

}
