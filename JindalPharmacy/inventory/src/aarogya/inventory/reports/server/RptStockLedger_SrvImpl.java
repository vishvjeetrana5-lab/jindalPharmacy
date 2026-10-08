package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptStockLedger_Srv;
import aarogya.inventory.reports.shared.RptStockLedger_Vld;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
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

public class RptStockLedger_SrvImpl extends DmRSSPrivate implements RptStockLedger_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", condition2 = "";
        String openingValueSql = "", receiveValueSql = "", issueValueSql = "", joinItemBatch = "";
        String valueField = "";

        try {
            RptStockLedger_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vStk1.departmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND vStk1.itemCode in (" + DmServer.getParamList(":itemCode", filter.get("item").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup in (" + DmServer.getParamList(":itemGroup", filter.get("itemGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode in (" + DmServer.getParamList(":itemCat", filter.get("itemCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vStk1.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.marginRateFix IN (" + DmServer.getParamList(":rateMarginFix ", filter.get("rateMarginFix").getTag()) + " )";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")) {
//                valueField = "stk.landedSkuValue";
                valueField = " (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {
                valueField = "stk.skuValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {
                valueField = "ib.skuSaleRate";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")) {
                valueField = "ib.skuMrp";
            }
            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(" + valueField + ",0)"
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(" + valueField + ",0) 	"
                        + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL(" + valueField + ",0)"
                        + "						ELSE 0 "
                        + "					END) AS openingValue ";

                receiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueField + ",0)"
                        + "				WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(" + valueField + ",0)"
                        + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL(" + valueField + ",0)"
                        + "							ELSE 0 "
                        + "					END"
                        + "				END AS receiveValue";
                issueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(" + valueField + ",0)"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(" + valueField + ",0)"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS issueValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {

                joinItemBatch = " LEFT JOIN itemBatch ib ON ib.code = stk.ibCode ";

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(stk.recQty ,0) "
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) "
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(stk.isuQty,0)) "
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(stk.recQty,0)  "
                        + "						ELSE 0	 "
                        + "					END * ISNULL(" + valueField + ",0) )  AS openingValue ";

                receiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0)"
                        + "				WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0))"
                        + "					  WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(stk.recQty,0)"
                        + "					  ELSE 0 "
                        + "                                  END  "
                        + "				END  * ISNULL(" + valueField + ",0) AS receiveValue";
                issueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0))"
                        + "					  WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(stk.isuQty,0)"
                        + "					  ELSE 0"
                        + "				    END "
                        + "			END * ISNULL(" + valueField + ",0) AS issueValue";
            }

            sqlQuery = "SELECT vStk1.docid, vStk1.v_SNo, vStk1.v_type, vStk1.vTypeShortName, vStk1.recId, vStk1.v_No, vStk1.v_Date"
                    + ", sm.name as site_Name, vStk1.site_Code ,vStk1.departmentCode, vStk1.itemCode "
                    + ", sgd.name as departmentName"
                    + ", CASE WHEN vStk1.v_Type IS NULL THEN 'Opening Balance' ELSE sgc.name END as particulars"
                    + ", im.Name as itemName"
                    + ", um.unit as sku"
                    + ", vStk1.openingQty"
                    + ", ABS(CAST(CASE WHEN ISNULL(vStk1.openingQty,0) = 0 THEN 0 ELSE ISNULL(vStk1.openingValue,0) / vStk1.openingQty END AS decimal(18,7))) AS openingRate"
                    + ", vStk1.openingValue"
                    + ", vStk1.receiveQty"
                    + ", ABS(CAST(CASE WHEN ISNULL(vStk1.receiveQty,0) = 0 THEN 0 ELSE ISNULL(vStk1.receiveValue,0) / vStk1.receiveQty END AS decimal(18,7))) AS receiveRate"
                    + ", vStk1.receiveValue"
                    + ", vStk1.issueQty"
                    + ", ABS(CAST(CASE WHEN ISNULL(vStk1.issueQty,0) = 0 THEN 0 ELSE ISNULL(vStk1.issueValue,0) / vStk1.issueQty END AS decimal(18,7))) AS issueRate"
                    + ", vStk1.issueValue"
                    + ", im.marginRateFix"
                    + ", SUM(ISNULL(vStk1.openingQty,0) + ISNULL(vStk1.receiveQty,0) - ISNULL(vStk1.issueQty,0)) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name, vStk1.v_Date, vStk1.serialNo, vStk1.vTypeShortName, vStk1.v_No, vStk1.recId, vStk1.v_SNo) AS balanceQty"
                    + ", SUM(ISNULL(vStk1.openingValue,0) + ISNULL(vStk1.receiveValue,0) - ISNULL(vStk1.issueValue,0)) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name, vStk1.v_Date, vStk1.serialNo, vStk1.vTypeShortName, vStk1.v_No, vStk1.recId, vStk1.v_SNo) AS balanceValue"
                    + " FROM ("
                    + "     SELECT 0 AS docid, 0 AS v_SNo, null as v_Type,'Opening' as vTypeShortName, null recId, 0 as v_No"
                    + "     , null AS v_Date"
                    + "     , vStk.itemCode"
                    + "     , vStk.site_Code"
                    + "     , vStk.departmentCode"
                    + "     , null AS stockContraSubCode"
                    + "     , vStk.openingQty"
                    + "     , vStk.openingValue"
                    + "     , 0 as receiveQty"
                    + "     , 0 AS receiveValue"
                    + "     , 0 as issueQty"
                    + "     , 0 AS issueValue"
                    + "     , 0 AS serialNo"
                    + "     FROM ("
                    + "     			SELECT stk.itemCode"
                    + "     			, stk.departmentCode"
                    + "     			, stk.site_Code"
                    + "     			, SUM(ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) AS openingQty " + openingValueSql
                    + "     			FROM stock stk "
                    + "                         INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type " + joinItemBatch
                    + "     			WHERE stk.v_Date < :fromDate  " + condition2
                    + "     			GROUP BY stk.itemCode"
                    + "     			, stk.departmentCode"
                    + "     			, stk.site_Code"
                    + "     		) AS vStk"
                    + "     	UNION ALL"
                    + "     	SELECT stk.docid, stk.v_SNo, stk.v_Type,vt.short_Name as vTypeShortName, stk.recId, stk.v_No, stk.v_Date"
                    + "     	, stk.itemCode"
                    + "     	, stk.site_Code"
                    + "     	, stk.departmentCode"
                    + "     	, stk.stockContraSubCode"
                    + "     	, 0 as openingQty"
                    + "     	, 0 AS openingValue"
                    + "     	, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0)			"
                    + "     			ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END"
                    + "     		END AS receiveQty " + receiveValueSql
                    + "     	, 0 as issueQty"
                    + "     	, 0 AS issueValue"
                    + "     	, vt.serialNo"
                    + "     	FROM stock stk"
                    + "     	INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type " + joinItemBatch
                    + "     	WHERE stk.v_Date >= :fromDate "
                    + "     	AND stk.v_Date <= :toDate "
                    + "     	AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1"
                    + "     			WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0"
                    + "     			ELSE CASE WHEN (ISNULL(stk.isuQty,0) < 0 OR ISNULL(stk.recQty,0) > 0 OR (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0)) = 0 ) THEN 1 ELSE 0 END"
                    + "     		END = 1" + condition2
                    + "     	UNION ALL"
                    + "     	SELECT stk.docid, stk.v_SNo, stk.v_Type,vt.short_Name as vTypeShortName, stk.recId, stk.v_No, stk.v_Date"
                    + "     	, stk.itemCode"
                    + "     	, stk.site_Code"
                    + "     	, stk.departmentCode"
                    + "     	, stk.stockContraSubCode"
                    + "     	, 0 as openingQty"
                    + "     	, 0 AS openingValue"
                    + "     	, 0 as receiveQty"
                    + "     	, 0 AS receiveValue"
                    + "     	, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)			"
                    + "     			ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END"
                    + "     		END AS issueQty " + issueValueSql
                    + "     	, vt.serialNo"
                    + "     	FROM stock stk"
                    + "     	INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type " + joinItemBatch
                    + "     	WHERE stk.v_Date >= :fromDate"
                    + "     	AND stk.v_Date <= :toDate"
                    + "     	AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1"
                    + "     			WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                    + "     			ELSE CASE WHEN (ISNULL(stk.isuQty,0) > 0 OR ISNULL(stk.recQty,0) < 0) THEN 1 ELSE 0 END"
                    + "     		END = 1" + condition2
                    + "     	) vStk1"
                    + " LEFT JOIN itemMast im ON im.code = vStk1.itemCode"
                    + " LEFT JOIN subGroup sgd ON sgd.subCode = vStk1.departmentCode"
                    + " LEFT JOIN siteMast sm ON sm.code = vStk1.site_Code"
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " LEFT JOIN subGroup sgc ON sgc.subCode = vStk1.stockContraSubCode"
                    + " WHERE  im.filterItemType=:filterItemType  " + condition
                    + " ORDER BY sgd.name, im.Name,  vStk1.v_Date, vStk1.serialNo, vStk1.vTypeShortName, vStk1.v_No, vStk1.recId, vStk1.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("Inventory_StockLedger");
            jasperPrint = DmServer.fillJasper(this, "Stock Ledger", jasperReport, null, filter, rsTemp);
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
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(filter.get("item").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCat", DmShared.xNull(filter.get("itemCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemGroup", DmShared.xNull(filter.get("itemGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("rateMarginFix", DmShared.xNull(filter.get("rateMarginFix").getTag()));
            }
        } catch (Exception e) {
        }
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
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode"
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemGroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEvaluationStock(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("F");
            listNode.setSharedKeyFieldName("Run FIFO Process");
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateMarginFix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Rate Fix");
            listNode.setSharedKeyFieldName("Rate Fix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Margin Fix");
            listNode.setSharedKeyFieldName("Margin Fix");
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
