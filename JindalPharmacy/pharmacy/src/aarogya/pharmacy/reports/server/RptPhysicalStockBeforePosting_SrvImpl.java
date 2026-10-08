package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptPhysicalStockBeforePosting_Srv;
import aarogya.pharmacy.reports.shared.RptPhysicalStockBeforePosting_Vld;
import aarogya.pharmacy.reports.shared.TxtHDate_Mdl;
import aarogya.pharmacy.reports.shared.TxtHMedCompany_Mdl;
import aarogya.pharmacy.reports.shared.TxtHManufacturer_Mdl;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.PhysicalStockFilterType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPhysicalStockBeforePosting_SrvImpl extends DmRSSPrivate implements RptPhysicalStockBeforePosting_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint = null;
        String sqlQuery = "", condition = "";

        try {

            RptPhysicalStockBeforePosting_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("PS")) {
                /*==============================<< check for posting entry >>====================================*/
                sqlQuery = "SELECT COUNT(*) AS totalPostingEntry"
                        + " FROM stockHead sh"
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                        + " LEFT JOIN ("
                        + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                        + "     FROM stockHeadDetail shd"
                        + "     GROUP BY shd.docId"
                        + " ) AS vShd ON vShd.docId = sh.docId"
                        + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                        + " AND sh.site_Code = :site_Code"
                        + " AND sh.v_Date = :v_Date"
                        + " AND vShd.departmentCode = :departmentCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setDate("v_Date", DmServer.convertDate(filter.get("postingDate").getText()));
                psTemp.setString("departmentCode", DmShared.xNull(filter.get("departmentCode").getTag()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalPostingEntry")).intValue() > 0) {
                        throw new DmException(0, "Posting  already exist for selected date.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("SC")) {
                sqlQuery = "SELECT COUNT(*) AS totalPostingEntry"
                        + " FROM stockHead sh"
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                        + " LEFT JOIN ("
                        + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                        + "     FROM stockHeadDetail shd"
                        + "     GROUP BY shd.docId"
                        + " ) AS vShd ON vShd.docId = sh.docId"
                        + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                        + " AND sh.site_Code = :site_Code"
                        + " AND sh.v_Date = :v_Date"
                        + " AND vShd.departmentCode = :departmentCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setDate("v_Date", DmServer.convertDate(filter.get("postingDate").getText()));
                psTemp.setString("departmentCode", DmShared.xNull(filter.get("departmentCode").getTag()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalPostingEntry")).intValue() == 0) {
                        throw new DmException(0, "Posting is not exist for selected date.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                /*==============================<< check for clearance entry >>====================================*/
                sqlQuery = "SELECT COUNT(*) AS totalClearanceEntry"
                        + " FROM stockHead sh"
                        + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                        + " LEFT JOIN ("
                        + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                        + "     FROM stockHeadDetail shd"
                        + "     GROUP BY shd.docId"
                        + " ) AS vShd ON vShd.docId = sh.docId"
                        + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_CLEARANCE + "'"
                        + " AND sh.site_Code = :site_Code"
                        + " AND sh.v_Date = :v_Date"
                        + " AND vShd.departmentCode = :departmentCode";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setDate("v_Date", DmServer.convertDate(filter.get("postingDate").getText()));
                psTemp.setString("departmentCode", DmShared.xNull(filter.get("departmentCode").getTag()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalClearanceEntry")).intValue() > 0) {
                        throw new DmException(0, "Clearance already exist for selected date.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemGroup  in (" + DmServer.getParamList(":medGroup ", filter.get("medGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCategory").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode  in (" + DmServer.getParamList(":medCategory ", filter.get("medCategory").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and shd.itemCode  in (" + DmServer.getParamList(":medName ", filter.get("medName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("manufacturer").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.manufacturerCode  in (" + DmServer.getParamList(":manufacturer ", filter.get("manufacturer").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemMakeCode  in (" + DmServer.getParamList(":medCompany ", filter.get("medCompany").getTag()) + " )";
            }

            PhysicalStockFilterType filterType = PhysicalStockFilterType.get(DmShared.iNull(filter.get("filterType").getTag()).intValue());
            if (filterType != null) {
                switch (filterType) {
                    case ITEM_CATEGORY:
                        condition += " AND im.itemCategoryCode = :filterValueCode";
                        break;
                    case ITEM_GROUP:
                        condition += " AND im.itemGroup = :filterValueCode";
                        break;
                    case ITEM_MAKE:
                        condition += " AND im.itemMakeCode = :filterValueCode";
                        break;
                    case ITEM:
                        condition += " AND im.code = :filterValueCode";
                        break;
                    case RESTRICTED_GROUP:
                        condition += " AND im.restrictGroup = :filterValueCode";
                        break;
                }
            }

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("PS")) {
                sqlQuery = "SELECT   im.name AS itemName , um.unit AS skuName  ,im.itemGroup,ig.name as groupName,im.itemCategoryCode, ic.name as itemCategoryName,"
                        + " ib.batchNo , ib.expiryDate ,  ib.mrp , ib.saleRate, ISNULL(shd.quantity, 0) AS physicalQty , "
                        + " ISNULL(vSt.computerQty, 0) AS computerQty , ISNULL(shd.quantity, 0) - ISNULL(vSt.computerQty, 0) AS adjQty , "
                        + " CASE      WHEN (ISNULL(shd.quantity, 0) - ISNULL(vSt.computerQty, 0)) < 0 THEN 1     "
                        + " WHEN (ISNULL(shd.quantity, 0) - ISNULL(vSt.computerQty, 0)) > 0 THEN 2     ELSE 0   END AS EType_IR "
                        + "  FROM "
                        + "       (     "
                        + "	         SELECT shd.itemCode, shd.ibCode, shd.departmentCode, shd.site_Code, SUM(shd.quantity) AS quantity     "
                        + "			 FROM stockHeadDetail shd     "
                        + "			 INNER JOIN stockHead sh ON sh.docId = shd.docId     "
                        + "			 INNER JOIN voucher_Type vt ON vt.v_Type = sh.v_Type     "
                        + "			 WHERE vt.category ='" + pvc.PHARMACY_PHYSICAL_STOCK + "'     "
                        + "			 AND sh.v_Date = :postingDate    "
                        + "			 AND sh.site_Code = :site_Code"
                        + "			 AND shd.departmentCode = :departmentCode    "
                        + "			 GROUP BY shd.itemCode, shd.ibCode, shd.departmentCode, shd.site_Code "
                        + "	  ) AS shd "
                        + " LEFT JOIN "
                        + "         (     "
                        + "	         SELECT st.itemCode, st.ibCode, st.departmentCode, st.site_Code, ISNULL(SUM(st.recQty),0) - ISNULL(SUM(st.isuQty),0) AS computerQty     "
                        + "			 FROM stock st     "
                        + "			 WHERE st.v_Date <= :postingDate     AND st.site_Code = :site_Code    "
                        + "			 AND st.departmentCode = :departmentCode    "
                        + "			 GROUP BY st.itemCode, st.ibCode, st.departmentCode, st.site_Code "
                        + "	    ) AS vSt ON vSt.itemCode = shd.itemCode AND ISNULL(vSt.ibCode, 0) = ISNULL(shd.ibCode, 0) AND vSt.departmentCode = shd.departmentCode AND vSt.site_Code = shd.site_Code "
                        + " "
                        + " LEFT JOIN itemMast im ON im.code = shd.itemCode "
                        + " left join itemgroup as ig on im.itemGroup=ig.code"
                        + " left join itemCat as ic on im.itemCategoryCode=ic.code"
                        + " LEFT JOIN unitMast um ON um.code = im.sku "
                        + " LEFT JOIN itemBatch ib ON ib.code = shd.ibCode "
                        + " where 1=1" + condition
                        + " ORDER by itemCategoryName, itemName";
            } else if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("SC")) {
                sqlQuery = "SELECT im.name AS itemName, um.unit AS skuName,im.itemGroup,ig.name as groupName,im.itemCategoryCode, ic.name as itemCategoryName,"
                        + " ib.batchNo, ib.expiryDate, ib.saleRate, "
                        + " ib.mrp, ISNULL(vSt.computerQty, 0) AS computerQty"
                        + "  FROM ("
                        + "            SELECT st.itemCode, st.ibCode, st.departmentCode, st.site_Code, ISNULL(SUM(st.recQty),0) - ISNULL(SUM(st.isuQty),0) AS computerQty"
                        + "            FROM stock st"
                        + "            left join itemMast as im on st.itemCode=im.code"
                        + "            WHERE   (im.filterItemType = :filterItemType OR im.isCommon = 1) and st.v_Date <= :postingDate"
                        + "            AND st.site_Code = :site_Code"
                        + "            AND st.departmentCode = :departmentCode"
                        + "            GROUP BY st.itemCode, st.ibCode, st.departmentCode, st.site_Code"
                        + "            ) AS vSt"
                        + " LEFT JOIN "
                        + "        ("
                        + "            SELECT shd.itemCode, shd.ibCode, shd.departmentCode, shd.site_Code"
                        + "            FROM stockHeadDetail shd"
                        + "            INNER JOIN stockHead sh ON sh.docId = shd.docId"
                        + "            INNER JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                        + "            WHERE vt.category =  '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                        + "            AND sh.v_Date = :postingDate"
                        + "            AND sh.site_Code = :site_Code"
                        + "            AND shd.departmentCode = :departmentCode"
                        + "            GROUP BY shd.itemCode, shd.ibCode, shd.departmentCode, shd.site_Code"
                        + "        ) AS shd ON shd.itemCode = vSt.itemCode AND ISNULL(shd.ibCode, 0) = ISNULL(vSt.ibCode, 0) "
                        + "          AND shd.departmentCode = vSt.departmentCode AND shd.site_Code = vSt.site_Code"
                        + " LEFT JOIN itemMast im ON im.code = vSt.itemCode"
                        + " left join itemgroup as ig on im.itemGroup=ig.code"
                        + " left join itemCat as ic on im.itemCategoryCode=ic.code"
                        + " LEFT JOIN unitMast um ON um.code = im.sku"
                        + " LEFT JOIN itemBatch ib ON ib.code = vSt.ibCode"
                        + " WHERE shd.itemCode IS NULL "
                        + " AND ISNULL(vSt.computerQty, 0) > 0 " + condition
                        + " ORDER BY itemCategoryName,itemName ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("SC")) {
                psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            }
            if (filterType != null) {
                switch (filterType) {
                    case ITEM_CATEGORY:
                    case ITEM_GROUP:
                    case ITEM_MAKE:
                    case ITEM:
                    case RESTRICTED_GROUP:
                        psTemp.setString("filterValueCode", DmShared.iNull(filter.get("filterValue").getTag()).toString());
                        break;
                }
            }
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("PS")) {
                jasperReport = DmServer.compileJasper("Pharmacy_PhysicalStockRegister");
                jasperPrint = DmServer.fillJasper(this, new pmPharmacy().PHYSICAL_STOCK_BEFORE_POSTING_REPROT.caption, jasperReport, null, filter, rsTemp);
            }
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("SC")) {
                jasperReport = DmServer.compileJasper("Pharmacy_PhysicalStockClearanceRegister");
                jasperPrint = DmServer.fillJasper(this, new pmPharmacy().PHYSICAL_STOCK_BEFORE_POSTING_REPROT.caption, jasperReport, null, filter, rsTemp);
            }

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

            if (!DmShared.xNull(filter.get("postingDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("postingDate", DmServer.convertDate(filter.get("postingDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("departmentCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setString("departmentCode", DmShared.xNull(filter.get("departmentCode").getTag()));
            }
            if (!DmShared.xNull(filter.get("medName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medName", DmShared.xNull(filter.get("medName").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCategory", DmShared.xNull(filter.get("medCategory").getTag()));
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(filter.get("medGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("manufacturer").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("manufacturer", DmShared.xNull(filter.get("manufacturer").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCompany", DmShared.xNull(filter.get("medCompany").getTag()));
            }
        } catch (Exception e) {
        }

    }

    @Override
    public DmResultPageing<List<TxtHDate_Mdl>> getTxtHDate(String reportType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHDate_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHDate_Mdl> lstTemp = new ArrayList<>();
        TxtHDate_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", category = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(reportType).equalsIgnoreCase("")) {
                if (DmShared.xNull(reportType).equalsIgnoreCase("PS")) {
                    category = "'" + pvc.PHARMACY_PHYSICAL_STOCK + "'";
                } else if (DmShared.xNull(reportType).equalsIgnoreCase("SC")) {
                    category = "'" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'";
                }
            }
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND shd.departmentCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT"
                    + "   sh.v_Date AS date"
                    + " , shd.departmentCode"
                    + " , MAX(sg.name) AS departmentName"
                    + " , MAX(ISNULL(sh.filterType, 0)) AS filterTypeCode"
                    + " , MAX(CASE ISNULL(sh.filterType, 0)"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN '" + PhysicalStockFilterType.ITEM_CATEGORY.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN '" + PhysicalStockFilterType.ITEM_GROUP.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN '" + PhysicalStockFilterType.ITEM_MAKE.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN '" + PhysicalStockFilterType.ITEM.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN '" + PhysicalStockFilterType.RESTRICTED_GROUP.getValuePharmacy() + "'"
                    + "		ELSE '' "
                    + "	  END) AS filterTypeName"
                    + " , MAX(CASE ISNULL(sh.filterType, 0)"
                    + "         WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN ISNULL(sh.itemCategoryCode, 0)"
                    + "         WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN ISNULL(sh.itemGroupCode, 0)"
                    + "         WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN ISNULL(sh.itemMakeCode, 0)"
                    + "         WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN ISNULL(sh.itemCode, 0)"
                    + "         WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN ISNULL(sh.restrictGroupCode, 0)"
                    + "         ELSE '' "
                    + "   END) AS filterValueCode"
                    + " , MAX(CASE ISNULL(sh.filterType, 0)"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN ISNULL(ic.name, '')"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN ISNULL(ig.name, '')"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN ISNULL(mm.companyname, '')"
                    + "		WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN ISNULL(im.name, '')"
                    + "		WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN ISNULL(rgm.name, '')"
                    + "		ELSE '' "
                    + "	  END) AS filterValueName"
                    + " FROM StockHead sh"
                    + " LEFT JOIN ("
                    + "         SELECT shd.docid, MAX(shd.departmentCode) AS departmentCode"
                    + "		FROM stockHeadDetail shd"
                    + "		GROUP BY shd.docid"
                    + "	) AS shd ON shd.docid = sh.docid"
                    + " LEFT JOIN voucher_Type vt ON sh.v_Type = vt.v_Type"
                    + " LEFT JOIN itemCat ic ON ic.code = sh.itemCategoryCode"
                    + " LEFT JOIN itemGroup ig ON ig.code = sh.itemGroupCode"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = sh.itemMakeCode"
                    + " LEFT JOIN itemMast im ON im.code = sh.itemCode"
                    + " LEFT JOIN restrictedGrpMast rgm ON rgm.code = sh.restrictGroupCode"
                    + " LEFT JOIN subGroup sg ON sg.subCode = shd.departmentCode"
                    + " WHERE vt.category = " + category
                    + " AND sh.site_Code = :site_Code "
                    + " AND sh.v_prefix = :vPrefix " + condition
                    + " GROUP BY sh.v_Date, shd.departmentCode, sh.site_Code";
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHDate_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("date")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("date")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setFilterTypeCode(DmShared.xNull(rsTemp.getString("filterTypeCode")));
                listNode.setFilterTypeName(DmShared.xNull(rsTemp.getString("filterTypeName")));
                listNode.setFilterValueCode(DmShared.xNull(rsTemp.getString("filterValueCode")));
                listNode.setFilterValueName(DmShared.xNull(rsTemp.getString("filterValueName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT "
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode "
                    + " FROM itemMast AS im "
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCat(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name "
                    + "FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemgroup ig"
                    + "   WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1) ");;
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<TxtHManufacturer_Mdl>> getTxtHManufacturer(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHManufacturer_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHManufacturer_Mdl> lstTemp = new ArrayList<>();
        TxtHManufacturer_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String filterNature = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            filterNature = DmServer.getCodeViaManualCode("MF", "manualCode", "subgroupNature", "code", cnTemp);

            sqlWithParam.setSQL(" select sg.subCode,sg.name,ct.cityName,sg.manualCode,concat(add1,add2) as address,sg.filterNature"
                    + "  from subGroup as sg "
                    + " left join city as ct on sg.cityCode = ct.cityCode where filterNature = :filterNature  ");
            sqlWithParam.setString("filterNature", filterNature);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHManufacturer_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
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
    public DmResultPageing<List<TxtHMedCompany_Mdl>> getTxtHMedCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHMedCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select code,companyName,shortName from itemMakeMast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));;
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT "
                    + "   dm.subCode"
                    + " , sg.name  as departmentName "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + " LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.manualCode IN ('CM','PR')" + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("departmentName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PS");
            listNode.setSharedKeyFieldName("Physical Stock");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("SC");
            listNode.setSharedKeyFieldName("Stock Clearance");
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
