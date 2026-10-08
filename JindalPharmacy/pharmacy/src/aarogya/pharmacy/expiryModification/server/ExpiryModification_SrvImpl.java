package aarogya.pharmacy.expiryModification.server;

import aarogya.pharmacy.expiryModification.shared.ExpiryModification_Srv;
import aarogya.pharmacy.expiryModification.shared.ExpiryModification_Mdl;
import aarogya.pharmacy.expiryModification.shared.TxtHMedicineName_Mdl;
import aarogya.pharmacy.expiryModification.shared.DefaultValue_Mdl;
import aarogya.pharmacy.expiryModification.shared.ExpiryModification_Vld;
import aarogya.pharmacy.expiryModification.shared.TxtHBatchNo_Mdl;
import aarogya.pharmacy.expiryModification.shared.dmFind_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.PhysicalStockFilterType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ExpiryModification_SrvImpl extends DmRSSPrivate implements ExpiryModification_Srv {

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
    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicineName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicineName_Mdl> lstTemp = new ArrayList<TxtHMedicineName_Mdl>();
        TxtHMedicineName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

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
                    //                    + "     WHERE irtd.departmentCode = :departmentCode"
                    + "	) virtd ON virtd.code = im.code"
                    + "	WHERE im.isActive = 1"
                    + "	AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND virtd.code IS NULL"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

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
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBatchNo_Mdl listNode;
        String sqlQuery, condition = "";
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
                    + " FROM itemBatch ib \n"
                    + " LEFT JOIN (\n"
                    + " select sum(balanceQty) as balanceQty,max(ibCode) as ibCode,max(itemCode) as itemCode\n"
                    + " from itemBatchStock ibs\n"
                    + " where ibs.site_Code = :siteCode\n"
                    + " group by ibs.ibCode,ibs.itemCode\n"
                    + " ) ibs ON ibs.ibCode = ib.code and ibs.itemCode = ib.itemCode\n"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''\n"
                    //                    + " AND ISNULL(ib.expiryDate, CAST(:uEntDt AS DATE)) >= CAST(:uEntDt AS DATE)"
                    + " AND ISNULL(ibs.balanceQty,0) > 0 " + condition;
            sqlWithParam.setSQL(sqlQuery);
//            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            sqlWithParam.setString("itemCode", DmShared.xNull(medicineCode));
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
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
    public DmResult<String> saveRecord(ExpiryModification_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        String pBillDocid = "";
        ResultSet rsTemp = null;
        int rowsAffected_ib, rowsAffected_pBill = 0, rowsAffected_shd = 0;
        dmrTemp.setData("");

        try {
            ExpiryModification_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(siteCode,DmServer.getSsnVoucherPrefix(this),"expiryModificationLog", cnTemp));
            }
            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (blnAdd) {
                sqlQuery = "Insert Into expiryModificationLog(code ,itemCode, ibCode, newExpiryDate, skuName, purchaseUnitName,saleRate, mrp, "
                        + " preparedBy, u_EntDt,site_Code,oldExpiryDate,v_Date) Values  "
                        + "(:code, :itemCode, :ibCode, :newExpiryDate,:skuName,:purchaseUnitName,:saleRate, :mrp,"
                        + ":preparedBy,:u_EntDt,:site_Code,:oldExpiryDate,:v_Date) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getItemCode()));
                psTemp.setString("ibCode", DmShared.foriegnKey(mrTemp.getIbCode()));
                psTemp.setDate("oldExpiryDate", DmServer.convertDate(mrTemp.getOldExpiryDate()));
                psTemp.setDate("newExpiryDate", DmServer.convertDate(mrTemp.getNewExpiryDate()));
                psTemp.setDate("v_Date", DmServer.convertDate(DmServer.getServerDateTime()[0]));
                psTemp.setString("skuName", DmShared.xNull(mrTemp.getSkuName()));
                psTemp.setString("purchaseUnitName", DmShared.xNull(mrTemp.getPurchaseUnitName()));
                psTemp.setString("saleRate", DmServer.vNull(mrTemp.getSaleRate(), "0.00"));
                psTemp.setString("mrp", DmServer.vNull(mrTemp.getMRP(), "0.00"));
                psTemp.setString("preparedBy", userName);
                psTemp.setString("site_Code", siteCode);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();

            } else {
                sqlQuery = "Update expiryModificationLog Set "
                        + " itemCode = :itemCode, "
                        + " ibCode = :ibCode,"
                        + " newExpiryDate = :newExpiryDate, "
                        + " purchaseUnitName = :purchaseUnitName, "
                        + " saleRate = :saleRate, "
                        + " mrp = :mrp, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :u_EntDt, "
                        + " site_Code = :site_Code, "
                        + " skuName = :skuName "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("itemCode", DmShared.foriegnKey(mrTemp.getItemCode()));
                psTemp.setString("ibCode", DmShared.foriegnKey(mrTemp.getIbCode()));
                psTemp.setDate("newExpiryDate", DmServer.convertDate(mrTemp.getNewExpiryDate()));
                psTemp.setString("skuName", DmShared.xNull(mrTemp.getSkuName()));
                psTemp.setString("purchaseUnitName", DmShared.xNull(mrTemp.getPurchaseUnitName()));
                psTemp.setString("saleRate", DmServer.vNull(mrTemp.getSaleRate(), "0.00"));
                psTemp.setString("mrp", DmServer.vNull(mrTemp.getMRP(), "0.00"));
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            }

//            //===========================< update itemBatch Table >==================================================
            sqlQuery = "update ib set ib.expiryDate = :newExpiryDt\n"
                    + "from itemBatch ib \n"
                    + "left join itemBatchStock ibs on ibs.ibcode = ib.code\n"
                    + "where ib.itemCode = :itemCode\n"
                    + "and ibs.ibCode = :ibCode\n"
                    + "and ib.batchNo = :batchNo\n"
                    + "and ib.expiryDate = :oldExpiryDt";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("oldExpiryDt", DmServer.convertDate(mrTemp.getOldExpiryDate()));
            psTemp.setDate("newExpiryDt", DmServer.convertDate(mrTemp.getNewExpiryDate()));
            psTemp.setString("itemCode", DmShared.xNull((mrTemp.getItemCode())));
            psTemp.setString("ibCode", DmShared.xNull(mrTemp.getIbCode()));
            psTemp.setString("batchNo", DmShared.xNull(mrTemp.getBatchNo()));
            rowsAffected_ib = psTemp.executeUpdate();
            psTemp.close();

            //==============================< purchase Bill docid >==================================
            sqlQuery = "select sg.name,vt.description,vt.short_Name,stk.docId\n"
                    + "from stock stk \n"
                    + "left join voucher_Type vt on vt.v_Type = stk.v_Type\n"
                    + "left join subGroup sg on sg.subCode = stk.departmentCode\n"
                    + "where \n"
                    + "stk.itemCode = :itemCode\n"
                    + "and stk.ibCode = :ibCode\n"
                    + "and vt.short_Name = '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "'\n"
                    + "order by v_Date desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull((mrTemp.getItemCode())));
            psTemp.setString("ibCode", DmShared.xNull(mrTemp.getIbCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                pBillDocid = DmShared.xNull(rsTemp.getString("docId"));
            }

            //=====================< update pBillDetail table >===================================
            if (DmShared.iNull(rowsAffected_ib).intValue() > 0) {
                sqlQuery = "update pbd set  pbd.expiryDate = ib.expiryDate\n"
                        + "from pBill pb\n"
                        + "left join pBillDetail pbd on pbd.docid = pb.docId\n"
                        + "left join itemBatch ib on ib.code = pbd.ibCode and ib.itemCode = pbd.itemCode and ib.batchNo = pbd.batchNo\n"
                        + "left join voucher_Type vt on vt.v_Type = pb.v_Type\n"
                        + "where 1=1\n"
                        + "and pbd.itemCode = :itemCode\n"
                        + "and pbd.ibCode = :ibCode\n"
                        + "and pbd.batchNo = :batchNo\n"
                        + "and pbd.expiryDate = :oldExpiryDt\n"
                        + "and vt.short_Name in ('PHPBD','PHPBR')";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("oldExpiryDt", DmServer.convertDate(mrTemp.getOldExpiryDate()));
                psTemp.setString("itemCode", DmShared.xNull((mrTemp.getItemCode())));
                psTemp.setString("ibCode", DmShared.xNull(mrTemp.getIbCode()));
                psTemp.setString("batchNo", DmShared.xNull(mrTemp.getBatchNo()));
                rowsAffected_pBill = psTemp.executeUpdate();
                psTemp.close();
            }

            //==============================< update stockHeadDetail >=========================================================
            if (DmShared.iNull(rowsAffected_ib).intValue() > 0 && DmShared.iNull(rowsAffected_pBill).intValue() > 0) {
                sqlQuery = "update shd set shd.expiryDate = ib.expiryDate\n"
                        + "from stockHead sh\n"
                        + "left join stockHeadDetail shd on shd.docId = sh.docId\n"
                        + "left join itemMast im on im.code = shd.itemCode\n"
                        + "left join itemBatch ib on ib.code = shd.ibCode and ib.itemCode = shd.itemCode and ib.batchNo = shd.batchNo\n"
                        + "where 1=1\n"
                        + "and shd.itemCode = :itemCode\n"
                        + "and shd.ibCode = :ibCode\n"
                        + "and ib.batchNo = :batchNo\n"
                        + "and shd.expiryDate = :oldExpiryDt";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("oldExpiryDt", DmServer.convertDate(mrTemp.getOldExpiryDate()));
                psTemp.setString("itemCode", DmShared.xNull((mrTemp.getItemCode())));
                psTemp.setString("ibCode", DmShared.xNull(mrTemp.getIbCode()));
                psTemp.setString("batchNo", DmShared.xNull(mrTemp.getBatchNo()));
                rowsAffected_shd = psTemp.executeUpdate();
                psTemp.close();

                //==========================< Update saleBillDetail >=======================================================
                sqlQuery = "update sbd set sbd.expiryDate = ib.expiryDate\n"
                        + "from saleBill sb \n"
                        + "left join saleBillDetail sbd on sbd.docId = sb.docId\n"
                        + "left  join itemBatch ib on ib.code = sbd.ibCode and sbd.itemCode = ib.itemCode and sbd.batchNo = ib.batchNo\n"
                        + "where\n"
                        + "1=1\n"
                        + "and sbd.itemCode = :itemCode\n"
                        + "and sbd.ibCode = :ibCode\n"
                        + "and sbd.batchNo = :batchNo\n"
                        + "and sbd.expiryDate = :oldExpiryDt";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("oldExpiryDt", DmServer.convertDate(mrTemp.getOldExpiryDate()));
                psTemp.setString("itemCode", DmShared.xNull((mrTemp.getItemCode())));
                psTemp.setString("ibCode", DmShared.xNull(mrTemp.getIbCode()));
                psTemp.setString("batchNo", DmShared.xNull(mrTemp.getBatchNo()));
                rowsAffected_shd = psTemp.executeUpdate();
                psTemp.close();
            }
            spBegin.commit();
            dmrTemp.setStatus(true);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            cnTemp.close();

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

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT eml.code \n"
                    + "FROM expiryModificationLog eml \n"
                    + "WHERE eml.site_Code =:site_Code \n";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " Order By eml.code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<ExpiryModification_Mdl> getMoveRec(String strKey) {
        DmResult<ExpiryModification_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ExpiryModification_Mdl mrTemp = new ExpiryModification_Mdl();
        dmrTemp.setData(mrTemp);
        dmrTemp.setData(mrTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT \n"
                    + "eml.code\n"
                    + ",eml.itemCode\n"
                    + ",im.name as itemName\n"
                    + ",eml.ibCode\n"
                    + ",ib.batchNo\n"
                    + ",eml.newExpiryDate\n"
                    + ",eml.skuName\n"
                    + ",eml.purchaseUnitName\n"
                    + ",eml.saleRate\n"
                    + ",eml.mrp\n"
                    + ",eml.preparedBy\n"
                    + ",eml.u_EntDt\n"
                    + ",eml.u_Name\n"
                    + "FROM expiryModificationLog eml\n"
                    + "LEFT JOIN itemMast im on im.code=eml.itemCode\n"
                    + "LEFT JOIN itemBatch ib on ib.code=eml.ibCode"
                    + " WHERE eml.code = :code AND eml.site_Code =:site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strKey).toString());
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                mrTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                mrTemp.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                mrTemp.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                mrTemp.setNewExpiryDate(DmShared.dNull(rsTemp.getDate("newExpiryDate")));
                mrTemp.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                mrTemp.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                mrTemp.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                mrTemp.setMRP(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedDate(DmShared.dNull(rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setSharedModifyDate(DmShared.dNull(rsTemp.getTimestamp("u_EntDt")));

            }
            psTemp.close();
            rsTemp.close();

            //====================== END ==============================================
            psTemp.close();
            rsTemp.close();
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
    public DmResultPageing<List<dmFind_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<dmFind_Mdl>> rstTemp = new DmResultPageing<>();
        List<dmFind_Mdl> lstTemp = new ArrayList<>();
        dmFind_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", sqlCondition = "";

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT \n"
                    + "eml.code\n"
                    + ",eml.itemCode\n"
                    + ",im.name as itemName\n"
                    + ",eml.ibCode\n"
                    + ",ib.batchNo\n"
                   
                    + ",eml.skuName\n"
                    + ",eml.purchaseUnitName\n"
                    + ",eml.saleRate\n"
                    + ",eml.mrp\n"
                    + ",eml.preparedBy\n"
                    + ",eml.u_EntDt\n"
                    + ",eml.u_Name\n  "
                    + "  ,format(eml.v_Date,'dd-MMM-yyyy') v_Date,format(eml.oldexpiryDate,'dd-MMM-yyyy') oldexpiryDate,format(eml.newExpiryDate,'dd-MMM-yyyy') newExpiryDate "
                    + "FROM expiryModificationLog eml\n"
                    + "LEFT JOIN itemMast im on im.code=eml.itemCode\n"
                    + "LEFT JOIN itemBatch ib on ib.code=eml.ibCode\n"
                    + "WHERE eml.site_Code =:site_Code";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new dmFind_Mdl();

                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setVoucherDate(DmShared.xNull(rsTemp.getString("v_Date")));
                listNode.setOldExpiryDate(DmShared.xNull(rsTemp.getString("oldexpiryDate")));
                listNode.setNewExpiryDate(DmShared.xNull(rsTemp.getString("newExpiryDate")));

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

}
