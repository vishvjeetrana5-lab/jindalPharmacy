package aarogya.pharmacy.physicalStock.server;

import aarogya.pharmacy.physicalStock.shared.PhysicalStock_Srv;
import aarogya.pharmacy.physicalStock.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.physicalStock.shared.PhysicalStock_Mdl;
import aarogya.pharmacy.physicalStock.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.physicalStock.shared.PhysicalStock_Vld;
import aarogya.pharmacy.physicalStock.shared.Find_Mdl;
import aarogya.pharmacy.physicalStock.shared.DefaultValue_Mdl;
import aarogya.pharmacy.physicalStock.shared.TxtHBatchNo_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.PhysicalStockFilterType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhysicalStock_SrvImpl extends DmRSSPrivate implements PhysicalStock_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypefieldName = "sh.v_Type", voucherDateFieldName = "sh.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelpEvents">
    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
                    + " AND vt.isActive = 1");
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
    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String departmentCode, String filterTypeCode, String filterValueCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicineName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicineName_Mdl> lstTemp = new ArrayList<TxtHMedicineName_Mdl>();
        TxtHMedicineName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        PhysicalStockFilterType filterType;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(departmentCode).equalsIgnoreCase("")) {
                condition = " AND 1 = 2 ";
            }

            filterType = PhysicalStockFilterType.get(DmShared.iNull(filterTypeCode).intValue());
            if (filterType != null) {
                switch (filterType) {
                    case ITEM_CATEGORY:
                        condition += " AND im.itemCategoryCode = :filterValue";
                        break;
                    case ITEM_GROUP:
                        condition += " AND im.itemGroup = :filterValue";
                        break;
                    case ITEM_MAKE:
                        condition += " AND im.itemMakeCode = :filterValue";
                        break;
                    case ITEM:
                        condition += " AND im.code = :filterValue";
                        break;
                    case RESTRICTED_GROUP:
                        condition += " AND im.restrictGroup = :filterValue";
                        break;
                }
            }

            sqlQuery = "SELECT  im.code, im.name, im.manualCode, um.code AS skuCode, um.unit AS skuName "
                    + "         , im.propertyList, im.isBestBefore, um.decimalPlaces, im.purchaseUnitConversionRate"
                    + "         , ump.decimalPlaces as purchaseUnitDecimalPlaces, ump.unit as purchaseUnitName, im.purchaseUnit as purchaseUnitCode "
                    + "	FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + "	LEFT JOIN unitmast um ON im.sku = um.code"
                    + "	LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code"
                    + " LEFT JOIN ("
                    + "     SELECT irtd.code"
                    + "     FROM itemRestrictedToDepartment irtd"
                    + "     WHERE irtd.departmentCode = :departmentCode"
                    + "	) virtd ON virtd.code = im.code"
                    + "	WHERE im.isActive = 1"
                    + "	AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND virtd.code IS NULL"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());

            if (filterType != null) {
                switch (filterType) {
                    case ITEM_CATEGORY:
                    case ITEM_GROUP:
                    case ITEM_MAKE:
                    case ITEM:
                    case RESTRICTED_GROUP:
                        sqlWithParam.setString("filterValue", DmShared.iNull(filterValueCode).toString());
                        break;
                }
            }

            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicineName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                listNode.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
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
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "SELECT "
                    + "   dm.subCode AS code"
                    + " , sg.name"
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + " LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.manualCode IN ('CM','PH')"
                    + " AND sg.isActive = 1 " + condition;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFilterType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            for (PhysicalStockFilterType filterType : PhysicalStockFilterType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(filterType.getId()).toString());
                listNode.setSharedKeyFieldName(filterType.getValuePharmacy());
                lstTemp.add(listNode);
            }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFilterValue(String filterTypeCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        PhysicalStockFilterType filterType;
        boolean flagFilterItemType = true;

        try {
            rstTemp.setData(lstTemp);
            filterType = PhysicalStockFilterType.get(DmShared.iNull(filterTypeCode).intValue());

            if (filterType != null) {
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

                switch (filterType) {
                    case ITEM_CATEGORY:
                        sqlQuery = "SELECT ic.code, ic.name"
                                + " FROM itemcat ic"
                                + " WHERE (ic.filterItemType = :filterItemType OR ic.isCommon = 1)";
                        break;
                    case ITEM_GROUP:
                        sqlQuery = "SELECT ig.code, ig.name"
                                + " FROM itemGroup ig"
                                + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)";
                        break;
                    case ITEM_MAKE:
                        sqlQuery = "SELECT im.code, im.companyName AS name"
                                + " FROM itemMakeMast im"
                                + " WHERE (im.filterItemType = :filterItemType)";
                        break;
                    case ITEM:
                        sqlQuery = "SELECT im.code, im.name"
                                + " FROM itemMast im"
                                + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)";
                        break;
                    case RESTRICTED_GROUP:
                        sqlQuery = " SELECT rgm.code, rgm.name  "
                                + "FROM restrictedGrpMast rgm";
                        flagFilterItemType = false;
                        break;
                    default:
                        throw new UnsupportedOperationException("Unable to recognize filter type.");
                }

                sqlWithParam.setSQL(sqlQuery);
                if (flagFilterItemType) {
                    sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
                }
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
                cnTemp.close();
            }

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
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBatchNo_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ib.code"
                    + " , ib.batchNo"
                    + " , ib.expiryDate"
                    + " , ib.manufacturingDate"
                    + " , ib.mrp"
                    + " , ib.skuMrp"
                    + " , ib.saleRate"
                    + " , ib.skuSaleRate"
                    + " , ib.barcode"
                    + " FROM itemBatch ib"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''"
                    + " AND ISNULL(ib.expiryDate, CAST(:uEntDt AS DATE)) >= CAST(:uEntDt AS DATE)";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            sqlWithParam.setString("itemCode", DmShared.xNull(medicineCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), "0.0000000"));
                listNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), "0.0000000"));
                listNode.setExpireDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
                    + " AND vt.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT"
                    + "   dm.subCode AS code"
                    + " , sg.displayName AS name"
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + " WHERE sg.IsActive = 1"
                    + " AND dm.subCode = :depCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentTag(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);

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
    public DmResult<String> saveRecord(PhysicalStock_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            PhysicalStock_Vld.chkSave(mrTemp);
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
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
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
                    throw new UnsupportedOperationException("Posting entry already exist " + DmShared.dNull(mrTemp.getVoucherDate()) + " in department " + DmShared.xNull(mrTemp.getDepartmentName()) + ", can't save record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< check for same filter in one date & department >>====================================*/
            sqlQuery = "SELECT TOP 1 sh.filterType AS filterTypeCode"
                    + " , CASE ISNULL(sh.filterType, 0)"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN '" + PhysicalStockFilterType.ITEM_CATEGORY.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN '" + PhysicalStockFilterType.ITEM_GROUP.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN '" + PhysicalStockFilterType.ITEM_MAKE.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN '" + PhysicalStockFilterType.ITEM.getValuePharmacy() + "'"
                    + "		WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN '" + PhysicalStockFilterType.RESTRICTED_GROUP.getValuePharmacy() + "'"
                    + "		ELSE ''"
                    + "	  END AS filterTypeName"
                    + " , CASE ISNULL(sh.filterType, 0) "
                    + "         WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN sh.itemCategoryCode"
                    + "         WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN sh.itemGroupCode"
                    + "         WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN sh.itemMakeCode"
                    + "         WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN sh.itemCode"
                    + "         WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN sh.restrictGroupCode"
                    + "         ELSE '' "
                    + "   END AS filterValueCode"
                    + " , CASE ISNULL(sh.filterType, 0)"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_CATEGORY.getId() + " THEN ic.name"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_GROUP.getId() + " THEN ig.name"
                    + "		WHEN " + PhysicalStockFilterType.ITEM_MAKE.getId() + " THEN mm.companyname"
                    + "		WHEN " + PhysicalStockFilterType.ITEM.getId() + " THEN im.name"
                    + "		WHEN " + PhysicalStockFilterType.RESTRICTED_GROUP.getId() + " THEN rgm.name"
                    + "		ELSE ''"
                    + "	  END AS filterValueName"
                    + " FROM stockHead sh"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN itemCat ic ON ic.code = sh.itemCategoryCode"
                    + " LEFT JOIN itemGroup ig ON ig.code = sh.itemGroupCode"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = sh.itemMakeCode"
                    + " LEFT JOIN itemMast im ON im.code = sh.itemCode"
                    + " LEFT JOIN restrictedGrpMast rgm ON rgm.code = sh.restrictGroupCode "
                    + " LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     GROUP BY shd.docId"
                    + " ) AS vShd ON vShd.docId = sh.docId"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
                    + " AND sh.site_Code = :site_Code"
                    + " AND sh.v_Date = :v_Date"
                    + " AND vShd.departmentCode = :departmentCode"
                    + " AND sh.docId != :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("departmentCode", DmShared.xNull(mrTemp.getDepartmentCode()));
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!(DmShared.iNull(rsTemp.getString("filterTypeCode")).longValue() == DmShared.iNull(mrTemp.getFilterTypeCode()).longValue()
                        && DmShared.iNull(rsTemp.getString("filterValueCode")).longValue() == DmShared.iNull(mrTemp.getFilterValueCode()).longValue())) {
                    String concatError = "";
                    if (DmShared.iNull(rsTemp.getString("filterTypeCode")).longValue() == 0) {
                        concatError = " no filter ";
                    } else {
                        concatError = " filter type '" + DmShared.xNull(rsTemp.getString("filterTypeName")) + "' and filter Value '" + DmShared.xNull(rsTemp.getString("filterValueName") + "'");
                    }
                    throw new UnsupportedOperationException("Physical stock entry already exists with " + concatError + " for date '" + DmShared.dNull(mrTemp.getVoucherDate()) + "' in department '" + DmShared.xNull(mrTemp.getDepartmentName()) + "', can't save record.");
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
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdObj(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);

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
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId,"
                        + " site_Code, transferDt, preparedBy, u_EntDt, u_EntDt_LatestLine, recIdPrefix, isAllowDuplicate, filterType,"
                        + " itemCategoryCode, itemGroupCode, itemMakeCode, itemCode, restrictGroupCode) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId,"
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt, :uEntDt, :recIdPrefix, :isAllowDuplicate, :filterType,"
                        + " :itemCategoryCode, :itemGroupCode, :itemMakeCode, :itemCode, :restrictGroupCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdObj().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdObj().getPrefix()));
                psTemp.setString("isAllowDuplicate", DmShared.iNull(mrTemp.getIsAllowDuplicate()).toString());
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
                        + " isAllowDuplicate = :isAllowDuplicate, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :uEntDt, "
                        + " u_EntDt_LatestLine = :uEntDt,"
                        + " filterType = :filterType,"
                        + " itemCategoryCode = :itemCategoryCode,"
                        + " itemGroupCode = :itemGroupCode,"
                        + " itemMakeCode = :itemMakeCode,"
                        + " itemCode = :itemCode,"
                        + " restrictGroupCode = :restrictGroupCode"
                        + " Where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("isAllowDuplicate", DmShared.iNull(mrTemp.getIsAllowDuplicate()).toString());
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
            List<GrdMedicineDetail_Mdl> listDetail = mrTemp.getListMedicineDetail();
            for (GrdMedicineDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = "INSERT INTO stockHeadDetail("
                            + "     docId, v_SNo, departmentCode, itemCode, quantity, site_Code,"
                            + "     ibCode, batchNo, manufacturingDate, expiryDate, mrp, skuMrp, saleRate, skuSaleRate, "
                            + "     rate, skuRate, unitCode , barcode"
                            + " ) values ("
                            + "     :docId, :v_SNo, :departmentCode, :itemCode, :quantity, :site_Code,"
                            + "     :ibCode,:batchNo, :manufacturingDate, :expiryDate, :mrp, :skuMrp, :saleRate, :skuSaleRate,"
                            + "     :rate, :skuRate, :unitCode  , :barcode"
                            + " )";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.foriegnKey(detail_Mdl.getBatchNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate()));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            //<editor-fold defaultstate="collapsed" desc="Validate Duplicacy">
            if (DmShared.iNull(mrTemp.getIsAllowDuplicate()).intValue() != 1) {
                sqlQuery = "SELECT MAX(im.name) AS itemName, COUNT(*) AS totalRecords"
                        + " FROM stockHeadDetail shd"
                        + " LEFT JOIN itemMast im ON im.code = shd.itemCode"
                        + " WHERE shd.docId = :docId"
                        + " GROUP BY shd.itemCode, shd.ibCode,shd.rate"
                        + " HAVING COUNT(*) > 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new UnsupportedOperationException("Duplicate entry not allowed! Total " + DmShared.iNull(rsTemp.getString("totalRecords")).intValue() + " similar records exist for '" + DmShared.xNull(rsTemp.getString("itemName")) + "'");
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
    public DmResult<PhysicalStock_Mdl> getMoveRec(String strKey) {
        DmResult<PhysicalStock_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PhysicalStock_Mdl mrTemp = new PhysicalStock_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdMedicineDetail_Mdl> lstNode = new ArrayList<GrdMedicineDetail_Mdl>();
        GrdMedicineDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	"
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
                    + " , sh.u_EntDt_LatestLine"
                    + " , sh.isAllowDuplicate"
                    + " , sh.filterType"
                    + " , sh.itemCategoryCode"
                    + " , ic.name AS itemCategoryName"
                    + " , sh.itemGroupCode"
                    + " , ig.name AS itemGroupName"
                    + " , sh.itemMakeCode"
                    + " , mm.companyName AS itemMakeName"
                    + " , sh.itemCode"
                    + " , im.name AS itemName"
                    + " , rgm.name AS restrictGroupName"
                    + " , sh.restrictGroupCode"
                    + "	FROM stockHead sh"
                    + "	LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN itemCat ic ON ic.code = sh.itemCategoryCode"
                    + " LEFT JOIN itemGroup ig ON ig.code = sh.itemGroupCode"
                    + " LEFT JOIN itemMakeMast mm ON mm.code = sh.itemMakeCode"
                    + " LEFT JOIN itemMast im ON im.code = sh.itemCode"
                    + " LEFT JOIN restrictedGrpMast rgm ON rgm.code = sh.restrictGroupCode "
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
                mrTemp.setIsAllowDuplicate(DmShared.iNull(rsTemp.getString("isAllowDuplicate")).toString());
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
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

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   im.code AS itemCode"
                    + " , im.name AS itemName"
                    + " , um.code AS skuCode"
                    + " , um.unit AS skuName"
                    + " , shd.ibCode"
                    + " , shd.skuValue"
                    + " , shd.quantity"
                    + " , shd.batchNo"
                    + " , shd.expiryDate"
                    + " , shd.manufacturingDate"
                    + " , shd.mrp"
                    + " , shd.skuMrp"
                    + " , shd.rate"
                    + " , shd.skuRate"
                    + " , shd.unitCode as purchaseUnitCode"
                    + " , ump.unit as purchaseUnitName"
                    + " , shd.saleRate"
                    + " , shd.skuSaleRate"
                    + " , im.propertyList"
                    + " , im.purchaseUnitConversionRate"
                    + " , ump.decimalPlaces as purchaseUnitDecimalPlaces"
                    + " , um.decimalPlaces"
                    + " , im.isBestBefore"
                    + " , shd.v_SNo"
                    + " , shd.barcode"
                    + " FROM stockHeadDetail shd"
                    + " LEFT JOIN itemMast im ON im.code = shd.itemCode"
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " LEFT JOIN unitMast ump ON ump.code = shd.unitCode"
                    + " WHERE shd.docId = :docId"
                    + " ORDER BY shd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicineDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate"), "0.0000000"));
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));

                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), "0.0000000"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate"), "0.0000000"));
                node.setPurchaseUnitConverionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListMedicineDetail().addAll(lstNode);
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
    public DmResult<String> deleteRecord(String docId, String vDate, String departmentCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

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
            psTemp.setDate("v_Date", DmServer.convertDate(vDate));
            psTemp.setString("departmentCode", DmShared.xNull(departmentCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalPostingEntry")).intValue() > 0) {
                    throw new UnsupportedOperationException("Posting entry already exist for selected date, can't delete record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Delete record from stockHeadDetail >>===============================*/
            sqlQuery = " Delete From stockHeadDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from stockHead >>===============================*/
            sqlQuery = " Delete From stockHead Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
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
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
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
                    + " FROM stockHead sh"
                    + "	LEFT JOIN voucher_Type vt ON vt.v_Type = sh.v_Type"
                    + " LEFT JOIN ("
                    + "     SELECT shd.docId, max(shd.departmentCode) AS departmentCode"
                    + "     FROM stockHeadDetail shd"
                    + "     GROUP BY shd.docId"
                    + "	) vShd ON vShd.docId = sh.docId"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = vShd.departmentCode"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'"
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
    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date) {
        DmResult<String> dmrtemp = new DmResult<String>();
        String skuLandedRate = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            skuLandedRate = DmServer.vNull(STServer.getSkuLandedRate(itemCode, ibCode, departmentCode, DmServer.getSsnSiteCode(this), v_Date, cnTemp), "0.00");
            dmrtemp.setData(skuLandedRate);
            dmrtemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            dmrtemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrtemp;
    }

}
