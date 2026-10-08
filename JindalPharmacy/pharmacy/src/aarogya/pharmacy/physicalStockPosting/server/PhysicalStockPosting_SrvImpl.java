package aarogya.pharmacy.physicalStockPosting.server;

import aarogya.pharmacy.physicalStockPosting.shared.PhysicalStockPosting_Srv;
import aarogya.pharmacy.physicalStockPosting.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.physicalStockPosting.shared.PhysicalStockPosting_Mdl;
import aarogya.pharmacy.physicalStockPosting.shared.PhysicalStockPosting_Vld;
import aarogya.pharmacy.physicalStockPosting.shared.Find_Mdl;
import aarogya.pharmacy.physicalStockPosting.shared.DefaultValue_Mdl;
import aarogya.pharmacy.physicalStockPosting.shared.TxtHDateOn_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import dataman.dmToolkit.stock.shared.StockFifo_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.PhysicalStockFilterType;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PhysicalStockPosting_SrvImpl extends DmRSSPrivate implements PhysicalStockPosting_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypefieldName = "sh.v_Type", voucherDateFieldName = "sh.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    @Override
    public DmResultPageing<List<TxtHDateOn_Mdl>> getTxtHDateOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHDateOn_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHDateOn_Mdl> lstTemp = new ArrayList<>();
        TxtHDateOn_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND shd.departmentCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "SELECT "
                    + "   pst.v_Date AS date"
                    + " , pst.departmentCode"
                    + " , sg.name AS departmentName"
                    + " , pst.filterType AS filterTypeCode"
                    + " , CASE ISNULL(pst.filterType, 0)"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN '" + PhysicalStockFilterType.ITEM_CATEGORY.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN '" + PhysicalStockFilterType.ITEM_GROUP.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN '" + PhysicalStockFilterType.ITEM_MAKE.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN '" + PhysicalStockFilterType.ITEM.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN '" + PhysicalStockFilterType.RESTRICTED_GROUP.getValuePharmacy() + "'"
                    + "		ELSE NULL"
                    + "	  END AS filterTypeName"
                    + " , pst.filterValue AS filterValueCode"
                    + " , CASE ISNULL(pst.filterType, 0)"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN ic.name"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN ig.name"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN mm.companyname"
                    + "		WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN im.name"
                    + "		WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN rgm.name"
                    + "		ELSE NULL"
                    + "	  END AS filterValueName"
                    + " FROM ("
                    + "     SELECT sh.v_Date, shd.departmentCode, sh.site_Code, sh.filterType"
                    + "            , CASE ISNULL(sh.filterType, 0) "
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN sh.itemCategoryCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN sh.itemGroupCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN sh.itemMakeCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN sh.itemCode"
                    + "                     WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN sh.restrictGroupCode"
                    + "                     ELSE '' "
                    + "              END AS filterValue"
                    + "     FROM stockHead sh"
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + "     LEFT JOIN ("
                    + "		SELECT shd1.docid, MAX(shd1.departmentCode) AS departmentCode"
                    + "		FROM stockHeadDetail shd1"
                    + "		GROUP BY shd1.docid"
                    + "     ) AS shd ON shd.docid = sh.docid"
                    + "     WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
                    + "     AND sh.site_Code = :site_Code" + condition
                    + "     GROUP BY sh.v_Date, shd.departmentCode, sh.site_Code, sh.filterType"
                    + "              , CASE ISNULL(sh.filterType, 0) "
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN sh.itemCategoryCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN sh.itemGroupCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN sh.itemMakeCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN sh.itemCode"
                    + "                     WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN sh.restrictGroupCode"
                    + "                     ELSE '' "
                    + "                END "
                    + " ) pst"
                    + " LEFT JOIN ("
                    + "     SELECT sh.v_Date, shd.departmentCode, sh.site_Code, sh.filterType"
                    + "            , CASE ISNULL(sh.filterType, 0) "
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN sh.itemCategoryCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN sh.itemGroupCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN sh.itemMakeCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN sh.itemCode"
                    + "                     WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN sh.restrictGroupCode"
                    + "                     ELSE '' "
                    + "              END AS filterValue"
                    + "     FROM stockHead sh"
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + "     LEFT JOIN ("
                    + "		SELECT shd1.docid, MAX(shd1.departmentCode) AS departmentCode"
                    + "		FROM stockHeadDetail shd1"
                    + "		GROUP BY shd1.docid"
                    + "     ) AS shd ON shd.docid = sh.docid"
                    + "     WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + "     AND sh.site_Code = :site_Code" + condition
                    + "     GROUP BY sh.v_Date, shd.departmentCode, sh.site_Code, sh.filterType"
                    + "              , CASE ISNULL(sh.filterType, 0) "
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN sh.itemCategoryCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN sh.itemGroupCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN sh.itemMakeCode"
                    + "                     WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN sh.itemCode"
                    + "                     WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN sh.restrictGroupCode"
                    + "                     ELSE '' "
                    + "                END "
                    + " ) AS pstp ON pstp.v_Date = pst.v_Date "
                    + "             AND pstp.departmentCode = pst.departmentCode"
                    + "             AND pstp.site_Code = pst.site_Code"
                    + "             AND ISNULL(pstp.filterType, 0) = ISNULL(pst.filterType, 0)"
                    + "             AND ISNULL(pstp.filterValue, 0) = ISNULL(pst.filterValue, 0)"
                    + " LEFT JOIN subGroup sg ON sg.subCode = pst.departmentCode"
                    + " LEFT JOIN itemCat ic ON ic.code = pst.filterValue"
                    + " LEFT JOIN itemGroup ig ON ig.code = pst.filterValue"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = pst.filterValue"
                    + " LEFT JOIN itemMast im ON im.code = pst.filterValue"
                    + " LEFT JOIN restrictedGrpMast rgm ON rgm.code = pst.filterValue"
                    + " WHERE pstp.v_Date IS NULL";

            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHDateOn_Mdl();
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    private List<GrdMedicineDetail_Mdl> getMedicineDetails(String dateOn, String departmentCode, String filterTypeCode, String filterValueCode, Connection cnTemp) throws Exception {
        List<GrdMedicineDetail_Mdl> lstTemp = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl lstNode = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery = "", condition = "";

        try {

            PhysicalStockFilterType filterType = PhysicalStockFilterType.get(DmShared.iNull(filterTypeCode).intValue());
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

            sqlQuery = "SELECT"
                    + "   im.code AS itemCode"
                    + " , im.name AS itemName"
                    + " , im.propertyList"
                    + " , vShd.ibCode"
                    + " , ib.batchNo"
                    + " , ib.manufacturingDate"
                    + " , ib.expiryDate"
                    + " , ib.mrp"
                    + " , ib.skuMrp"
                    + " , ib.saleRate"
                    + " , ib.skuSaleRate"
                    + " , CASE WHEN ISNULL(vShd.quantity, 0) = 0 THEN 0 ELSE ISNULL(vShd.totalSkuValue, 0) /  ISNULL(vShd.quantity, 0) END AS physicalSkuRate "
                    + " , ISNULL(vShd.quantity, 0) AS physicalQty"
                    + " , ISNULL(vSt.computerQty, 0) AS computerQty"
                    + " , ISNULL(vShd.quantity, 0) - ISNULL(vSt.computerQty, 0) AS adjQty"
                    + " , CASE "
                    + "     WHEN (ISNULL(vShd.quantity, 0) - ISNULL(vSt.computerQty, 0)) < 0 THEN 1"
                    + "     WHEN (ISNULL(vShd.quantity, 0) - ISNULL(vSt.computerQty, 0)) > 0 THEN 2"
                    + "     ELSE 0"
                    + "   END AS EType_IR"
                    + " FROM ("
                    + "     SELECT shd.itemCode, shd.ibCode, shd.departmentCode, shd.site_Code, SUM(shd.quantity) AS quantity"
                    + "     , SUM(ISNULL(shd.quantity,0) * ISNULL(shd.skuRate,0)) AS totalSkuValue  "
                    + "     FROM stockHeadDetail shd"
                    + "     INNER JOIN stockHead sh ON sh.docId = shd.docId"
                    + "     INNER JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + "     LEFT JOIN itemMast im ON im.code = shd.itemCode"
                    + "     WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
                    + "     AND sh.v_Date = :dateOn"
                    + "     AND sh.site_Code = :site_Code"
                    + "     AND shd.departmentCode = :departmentCode" + condition
                    + "     GROUP BY shd.itemCode, shd.ibCode, shd.departmentCode, shd.site_Code"
                    + " ) AS vShd"
                    + " LEFT JOIN ("
                    + "     SELECT st.itemCode, st.ibCode, st.departmentCode, st.site_Code, ISNULL(SUM(st.recQty),0) - ISNULL(SUM(st.isuQty),0) AS computerQty"
                    + "     FROM stock st"
                    + "     LEFT JOIN itemMast im ON im.code = st.itemCode"
                    + "     WHERE st.v_Date <= :dateOn"
                    + "     AND st.site_Code = :site_Code"
                    + "     AND st.departmentCode = :departmentCode" + condition
                    + "     GROUP BY st.itemCode, st.ibCode, st.departmentCode, st.site_Code"
                    + " ) AS vSt ON vSt.itemCode = vShd.itemCode AND ISNULL(vSt.ibCode, 0) = ISNULL(vShd.ibCode, 0) AND vSt.departmentCode = vShd.departmentCode AND vSt.site_Code = vShd.site_Code"
                    + " LEFT JOIN itemMast im ON im.code = vShd.itemCode"
                    + " LEFT JOIN itemBatch ib ON ib.code = vShd.ibCode"
                    + " ORDER BY im.name ASC";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("dateOn", DmServer.convertDate(dateOn));
            psTemp.setString("departmentCode", DmShared.xNull(departmentCode));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (filterType != null) {
                switch (filterType) {
                    case ITEM_CATEGORY:
                    case ITEM_GROUP:
                    case ITEM_MAKE:
                    case ITEM:
                    case RESTRICTED_GROUP:
                        psTemp.setString("filterValueCode", DmShared.iNull(filterValueCode).toString());
                        break;
                }
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdMedicineDetail_Mdl();

                //Item
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                lstNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                //Quantity
                lstNode.setPhysicalQty(DmServer.vNull(rsTemp.getString("physicalQty")));
                lstNode.setComputerQty(DmServer.vNull(rsTemp.getString("computerQty")));
                lstNode.setAdjQty(DmServer.vNull(rsTemp.getString("adjQty")));

                //TBC Start: Receive Value 
                if (DmShared.iNull(rsTemp.getString("EType_IR")).intValue() == 2) {
                    lstNode.setSkuRate(DmServer.vNull(rsTemp.getString("physicalSkuRate"), "0.0000000"));
                    lstNode.setSkuValue(DmServer.vNull((DmShared.vNull(rsTemp.getString("physicalSkuRate")).doubleValue() * DmShared.vNull(rsTemp.getString("adjQty")).doubleValue()), "0.00"));
                }
                //TBC End: Receive Value 

                //Properties
                lstNode.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                lstNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                lstNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                lstNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                lstNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                lstNode.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), "0.0000000"));
                lstNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                lstNode.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), "0.0000000"));

                //Other
                lstNode.setEType_IR(rsTemp.getString("EType_IR"));

                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
            } catch (Exception ex) {
            }
        }

        return lstTemp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Posting">
    @Override
    public DmResult<String> setPosted(PhysicalStockPosting_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        DmSavePoint spBegin = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*==============================<< Check v_Date sequence >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords"
                    + " FROM stockHead sh"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     GROUP BY shd.docId"
                    + " ) AS vShd ON vShd.docId = sh.docId"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + " AND sh.site_Code = :site_Code"
                    + " AND sh.v_Date > :v_Date"
                    + " AND vShd.departmentCode = :departmentCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new UnsupportedOperationException("Record already exists for dates greater than " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't post/unpost record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check for LatestEntry >>====================================*/
            sqlQuery = "SELECT sh.u_EntDt_LatestLine FROM stockHead sh WHERE sh.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(mrTemp.getU_EntDt_LatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                        throw new UnsupportedOperationException("Record has been changed, can't update record, refresh record then update");
                    }
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
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalClearanceEntry")).intValue() > 0) {
                    throw new UnsupportedOperationException("Clearance entry already exist for " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't post/unpost record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< post/unpost entry >>====================================*/
            sqlQuery = "SELECT sh.postedBy"
                    + " , sh.postedDateTime"
                    + " , (CASE WHEN :userName IN (ISNULL(sh.postedBy,''),'SA','SUPER') THEN 1 ELSE 0 END) canUnpost"
                    + " FROM stockHead sh"
                    + " WHERE sh.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("userName", DmShared.xNull(DmServer.getSsnUserName(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("postedBy")).trim().equals("")) {
                    sqlQuery = "SELECT"
                            + "   shd.itemCode"
                            + " , shd.ibCode"
                            + " , ABS(shd.quantity) AS adjustQty"
                            + " , shd.v_SNo"
                            + " , shd.skuValue"
                            + " , CASE "
                            + "     WHEN (ISNULL(shd.physicalQty, 0) - ISNULL(shd.computerQty, 0)) < 0 THEN 1"
                            + "     WHEN (ISNULL(shd.physicalQty, 0) - ISNULL(shd.computerQty, 0)) > 0 THEN 2"
                            + "     ELSE 0"
                            + "   END AS EType_IR"
                            + " FROM stockHeadDetail shd"
                            + " WHERE shd.docId = :docId"
                            + " ORDER BY shd.v_SNo";
                    psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTempInner.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    rsTempInner = psTempInner.executeQuery();
                    while (rsTempInner.next()) {
                        if (DmShared.iNull(rsTempInner.getString("itemCode")).longValue() != 0 && DmShared.iNull(rsTempInner.getString("EType_IR")).intValue() != 0) {
                            Stock_Mdl stock_Mdl = new Stock_Mdl();
                            stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            stock_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTempInner.getString("v_SNo")));
                            stock_Mdl.setSite_Code(DmShared.xNull(DmServer.getSsnSiteCode(this)));
                            stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                            stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdObj().getCounter()).toString());
                            stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                            stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdObj().getPrefix()));
                            stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                            stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentCode()));
                            stock_Mdl.setItemCode(DmShared.xNull(rsTempInner.getString("itemCode")));
                            stock_Mdl.setIbCode(DmShared.xNull(rsTempInner.getString("ibCode")));

                            //At Last:
                            switch (DmShared.iNull(rsTempInner.getString("EType_IR")).intValue()) {
                                case 1:
                                    stock_Mdl.setIsuQty(DmServer.vNull(rsTempInner.getString("adjustQty")));
                                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.PHYSICAL_STOCK_POSTING, cnTemp);
                                    break;
                                case 2:
                                    stock_Mdl.setRecQty(DmServer.vNull(rsTempInner.getString("adjustQty")));
                                    stock_Mdl.setSkuValue(DmServer.vNull(rsTempInner.getString("skuValue"), "0.00"));
                                    stock_Mdl.setLandedSkuValue(DmServer.vNull(rsTempInner.getString("skuValue"), "0.00"));

                                    StockFifo_Mdl stockFifo_Mdl = new StockFifo_Mdl();
                                    stockFifo_Mdl.setSharedKeyFieldCode(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()));
                                    stockFifo_Mdl.setSharedKeyFieldName(DmShared.xNull(stock_Mdl.getSharedKeyFieldName()));
                                    stockFifo_Mdl.setRecQty(DmServer.vNull(rsTempInner.getString("adjustQty")));
                                    stockFifo_Mdl.setStockValueDate(DmShared.dNull(stock_Mdl.getV_Date()));

                                    stock_Mdl.setStockFifo_Mdl(stockFifo_Mdl);

                                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.ITEM_RECEIVE, cnTemp);
                                    break;
                                default:
                                    throw new UnsupportedOperationException("Error in stock posting...");
                            }
                        }
                    }
                    rsTempInner.close();
                    psTempInner.close();

                    rsTemp.updateString("postedBy", DmShared.xNull(DmServer.getSsnUserName(this)).trim());
                    rsTemp.updateTimestamp("postedDateTime", new Timestamp(new java.util.Date().getTime()));
                    rsTemp.updateRow();
                    rsTemp.close();
                } else {
                    if (DmShared.iNull(rsTemp.getString("canUnpost")).longValue() == 1) {
                        Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp);

                        rsTemp.updateString("postedBy", null);
                        rsTemp.updateDate("postedDateTime", null);
                        rsTemp.updateRow();
                        rsTemp.close();
                    } else {
                        throw new UnsupportedOperationException("You are not authorized to unpost.");
                    }
                }
                updateUEntDtLastestLine(cnTemp, mrTemp.getSharedKeyFieldCode());
            }
            rsTemp.close();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
            try {
                if (rsTempInner != null) {
                    rsTempInner.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
                }
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
    //</editor-fold>

    private void updateUEntDtLastestLine(Connection cnTemp, String docId) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            sqlQuery = "UPDATE stockHead SET"
                    + " u_EntDt_LatestLine = :uEntDt"
                    + " WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default voucher+_Type >>===============================*/
            sqlQuery = "SELECT"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + " AND vt.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
            cnTemp.close();
            dmrTemp.setStatus(true);
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
    public DmResult<String> saveRecord(PhysicalStockPosting_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            PhysicalStockPosting_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*==============================<< Check v_Date sequence >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords"
                    + " FROM stockHead sh"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     GROUP BY shd.docId"
                    + " ) AS vShd ON vShd.docId = sh.docId"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + " AND sh.site_Code = :site_Code"
                    + " AND sh.v_Date > :v_Date"
                    + " AND vShd.departmentCode = :departmentCode"
                    + " AND sh.docId != :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new UnsupportedOperationException("Record already exists for dates greater than " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't save record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check all posted >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords"
                    + " FROM stockHead sh"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     GROUP BY shd.docId"
                    + " ) AS vShd ON vShd.docId = sh.docId"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + " AND sh.site_Code = :site_Code"
                    + " AND sh.v_Date < :v_Date"
                    + " AND vShd.departmentCode = :departmentCode"
                    + " AND ISNULL(sh.postedBy, '') = ''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new UnsupportedOperationException("Unposted records already exist for dates less than " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't save record.");
                }
            }
            rsTemp.close();
            psTemp.close();

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
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalPostingEntry")).intValue() > 0) {
                    throw new UnsupportedOperationException("Posting entry already exist for " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't save record.");
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
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalClearanceEntry")).intValue() > 0) {
                    throw new UnsupportedOperationException("Clearance entry already exist for " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't save record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdObj(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdObj(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdObj().getRecIdValue()));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId, "
                        + " site_Code, transferDt, preparedBy, u_EntDt, u_EntDt_LatestLine, recIdPrefix, filterType, itemCategoryCode, itemGroupCode, itemMakeCode, itemCode, restrictGroupCode) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId, "
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt, :uEntDt, :recIdPrefix, :filterType, :itemCategoryCode, :itemGroupCode, :itemMakeCode, :itemCode, :restrictGroupCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdObj().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdObj().getPrefix()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("filterType", DmShared.foriegnKey(mrTemp.getFilterTypeCode()));

                PhysicalStockFilterType filterType = PhysicalStockFilterType.get(DmShared.iNull(mrTemp.getFilterTypeCode()).intValue());
                if (filterType != null) {
                    switch (filterType) {
                        case ITEM_CATEGORY:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case ITEM_GROUP:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case ITEM_MAKE:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case ITEM:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case RESTRICTED_GROUP:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            break;
                    }
                } else {
                    psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                    psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                    psTemp.setString("itemCode", DmShared.foriegnKey(""));
                    psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                }
            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :v_Date, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :uEntDt, "
                        + " u_EntDt_LatestLine = :uEntDt, "
                        + " filterType = :filterType, "
                        + " itemCategoryCode = :itemCategoryCode,"
                        + " itemGroupCode = :itemGroupCode,"
                        + " itemMakeCode = :itemMakeCode,"
                        + " itemCode = :itemCode,"
                        + " restrictGroupCode = :restrictGroupCode"
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("transferDt", null);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", userName);
                psTemp.setString("filterType", DmShared.foriegnKey(mrTemp.getFilterTypeCode()));

                PhysicalStockFilterType filterType = PhysicalStockFilterType.get(DmShared.iNull(mrTemp.getFilterTypeCode()).intValue());
                if (filterType != null) {
                    switch (filterType) {
                        case ITEM_CATEGORY:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case ITEM_GROUP:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case ITEM_MAKE:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case ITEM:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                            break;
                        case RESTRICTED_GROUP:
                            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                            psTemp.setString("itemCode", DmShared.foriegnKey(""));
                            psTemp.setString("restrictGroupCode", DmShared.foriegnKey(mrTemp.getFilterValueCode()));
                            break;
                    }
                } else {
                    psTemp.setString("itemCategoryCode", DmShared.foriegnKey(""));
                    psTemp.setString("itemGroupCode", DmShared.foriegnKey(""));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(""));
                    psTemp.setString("itemCode", DmShared.foriegnKey(""));
                    psTemp.setString("restrictGroupCode", DmShared.foriegnKey(""));
                }
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from stockHeadDetail >>====================================*/
            sqlQuery = "DELETE FROM stockHeadDetail WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into stockHeadDetail & stockHeadBatchDetail >>====================================*/
            int v_SNo = 0;
            List<GrdMedicineDetail_Mdl> listDetail = getMedicineDetails(mrTemp.getVoucherDate(), mrTemp.getDepartmentCode(), mrTemp.getFilterTypeCode(), mrTemp.getFilterValueCode(), cnTemp);
            for (GrdMedicineDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = "INSERT INTO stockHeadDetail("
                            + "     docId, v_SNo, departmentCode, itemCode, physicalQty, computerQty, quantity, site_Code,"
                            + "     ibCode, batchNo, manufacturingDate, expiryDate, mrp, skuMrp, saleRate, skuSaleRate, "
                            + "     skuRate, skuValue "
                            + " ) values ("
                            + "     :docId, :v_SNo, :departmentCode, :itemCode, :physicalQty, :computerQty, :quantity, :site_Code,"
                            + "     :ibCode,:batchNo, :manufacturingDate, :expiryDate, :mrp, :skuMrp, :saleRate, :skuSaleRate, "
                            + "     :skuRate, :skuValue "
                            + " )";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("physicalQty", DmServer.vNull(detail_Mdl.getPhysicalQty()));
                    psTemp.setString("computerQty", DmServer.vNull(detail_Mdl.getComputerQty()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getAdjQty()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.foriegnKey(detail_Mdl.getBatchNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate()));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("skuValue", DmServer.vNull(detail_Mdl.getSkuValue()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            if (v_SNo == 0) {
                throw new UnsupportedOperationException("Items not found to save...");
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getVoucherTypeCode(), this);
            }
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResult<PhysicalStockPosting_Mdl> getMoveRec(String strKey) {
        DmResult<PhysicalStockPosting_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PhysicalStockPosting_Mdl mrTemp = new PhysicalStockPosting_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdMedicineDetail_Mdl> lstNode = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   sh.docId"
                    + " , sh.recId"
                    + " , sh.v_No"
                    + " , sh.recIdPrefix"
                    + " , sh.v_Date"
                    + " , sh.v_Type"
                    + " , vt.description"
                    + " , vShd.departmentCode"
                    + " , sg.name AS departmentName"
                    + " , sh.preparedBy"
                    + " , sh.u_Name"
                    + " , sh.postedBy"
                    + " , sh.u_EntDt_LatestLine"
                    + " , sh.filterType"
                    + " , sh.itemCategoryCode"
                    + " , ic.name AS itemCategoryName"
                    + " , sh.itemGroupCode"
                    + " , ig.name AS itemGroupName"
                    + " , sh.itemMakeCode"
                    + " , mm.companyName AS itemMakeName"
                    + " , sh.itemCode"
                    + " , im.name AS itemName"
                    + " , sh.restrictGroupCode"
                    + " , rgm.name AS restrictGroupName"
                    + "	FROM stockHead sh"
                    + "	LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN itemCat ic ON ic.code = sh.itemCategoryCode"
                    + " LEFT JOIN itemGroup ig ON ig.code = sh.itemGroupCode"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = sh.itemMakeCode"
                    + " LEFT JOIN itemMast im ON im.code = sh.itemCode"
                    + " LEFT JOIN restrictedGrpMast rgm ON rgm.code = sh.restrictGroupCode"
                    + "	LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     WHERE shd.docId = :docId "
                    + "     GROUP BY shd.docId "
                    + "	) AS vShd ON vShd.docId = sh.docId"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = vShd.departmentCode"
                    + "	WHERE sh.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdObj().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdObj().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdObj().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setPostedBy(DmShared.xNull(rsTemp.getString("postedBy")));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }

                mrTemp.setFilterTypeCode(DmShared.xNull(rsTemp.getString("filterType")));

                PhysicalStockFilterType filterType = PhysicalStockFilterType.get(DmShared.iNull(mrTemp.getFilterTypeCode()).intValue());
                if (filterType != null) {
                    mrTemp.setFilterTypeName(filterType.getValuePharmacy());

                    switch (filterType) {
                        case ITEM_CATEGORY:
                            mrTemp.setFilterValueCode(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                            mrTemp.setFilterValueName(DmShared.xNull(rsTemp.getString("itemCategoryName")));
                            break;
                        case ITEM_GROUP:
                            mrTemp.setFilterValueCode(DmShared.xNull(rsTemp.getString("itemGroupCode")));
                            mrTemp.setFilterValueName(DmShared.xNull(rsTemp.getString("itemGroupName")));
                            break;
                        case ITEM_MAKE:
                            mrTemp.setFilterValueCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                            mrTemp.setFilterValueName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                            break;
                        case ITEM:
                            mrTemp.setFilterValueCode(DmShared.xNull(rsTemp.getString("itemCode")));
                            mrTemp.setFilterValueName(DmShared.xNull(rsTemp.getString("itemName")));
                            break;
                        case RESTRICTED_GROUP:
                            mrTemp.setFilterValueCode(DmShared.xNull(rsTemp.getString("restrictGroupCode")));
                            mrTemp.setFilterValueName(DmShared.xNull(rsTemp.getString("restrictGroupName")));
                            break;
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(mrTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
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
    public DmResult<String> deleteRecord(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*==============================<< Check for postedBy before deleting data >>===============================*/
            sqlQuery = "SELECT sh.postedBy FROM stockHead sh WHERE sh.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("postedBy")).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Record is posted. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Delete record from stockHeadDetail >>===============================*/
            sqlQuery = " Delete From stockHeadDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from stockHead >>===============================*/
            sqlQuery = " Delete From stockHead Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery = "", sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT sh.docId"
                    + " FROM stockHead sh"
                    + " LEFT JOIN voucher_Type vt ON sh.v_Type = vt.v_Type"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + " AND sh.site_Code = :site_Code "
                    + " AND sh.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + sqlCondition;
            }
            sqlQuery += " ORDER BY sh.v_Date DESC, vt.short_Name, sh.v_No DESC, sh.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   sh.docId"
                    + " , sh.recId"
                    + " , sh.v_Date"
                    + " , sg.name AS departmentName"
                    + " , vt.short_Name"
                    + " , sh.v_No"
                    + " , sh.postedBy"
                    + " , format(sh.postedDateTime,'dd-MMM-yyyy HH.mm') AS postedDateTime"
                    + " FROM stockHead sh"
                    + "	LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     GROUP BY shd.docId"
                    + "	) vShd ON vShd.docId = sh.docId"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = vShd.departmentCode"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'"
                    + " AND sh.site_Code = :site_Code"
                    + " AND sh.v_prefix = :vPrefix";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setPosting(DmShared.xNull(rsTemp.getString("postedBy")) + " " + DmShared.xNull(rsTemp.getString("postedDateTime")));
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

}
