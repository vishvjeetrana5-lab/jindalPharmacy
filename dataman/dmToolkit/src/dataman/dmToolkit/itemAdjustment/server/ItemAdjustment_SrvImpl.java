package dataman.dmToolkit.itemAdjustment.server;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.dmToolkit.stock.server.Stock_Server;

public class ItemAdjustment_SrvImpl extends DmRSSPrivate implements ItemAdjustment_Srv {

    @Override
    public DmResultPageing<List<GrdItemAdjustment_Mdl>> getItem(Filter_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdItemAdjustment_Mdl>> dmrTemp = new DmResultPageing<List<GrdItemAdjustment_Mdl>>();
        List<GrdItemAdjustment_Mdl> lstTemp = new ArrayList<GrdItemAdjustment_Mdl>();
        GrdItemAdjustment_Mdl lstNode = null;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "", stockAdjustmentVType;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            stockAdjustmentVType = DmShared.xNull(DmServer.executeScalar("SELECT env.stockAdjustmentVType FROM enviro env WHERE env.site_Code = " + DmServer.getSsnSiteCode(this), cnTemp));

            if (!DmShared.xNull(stockAdjustmentVType).trim().equals("")) {
                condition += " AND s.v_Type IN (" + stockAdjustmentVType + ")";
            }
            if (!DmShared.xNull(param.getVoucherNo()).trim().equals("")) {
                condition += " AND s.recId LIKE :recId";
            }
            if (!DmShared.dNull(param.getVoucherDate()).trim().equals("")) {
                condition += " AND s.v_Date = :v_Date";
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).trim().equals("")) {
                condition += " AND s.v_Type IN (" + DmServer.getParamList(":v_Type", param.getVoucherTypeCode()) + ")";
            }
            if (!DmShared.xNull(param.getRefNo()).trim().equals("")) {
                condition += " AND s.refChallanNo = :refChallanNo";
            }
            if (!DmShared.xNull(param.getPartyCode()).trim().equals("")) {
                condition += " AND s.stockContraSubCode IN (" + DmServer.getParamList(":stockContraSubCode", param.getPartyCode()) + ")";
            }

            sqlQuery = "SELECT"
                    + "   s.docId"
                    + " , s.recId"
                    + " , s.v_No"
                    + " , s.v_SNo"
                    + " , s.v_Type AS v_TypeCode"
                    + " , vt.short_Name AS v_TypeName"
                    + " , vt.mnuText"
                    + " , vt.mnuAttachedInModule"
                    + " , s.v_Date"
                    + " , s.refChallanNo AS refNo"
                    + " , s.stockContraSubCode AS partyCode"
                    + " , sg.name AS partyName"
                    + " , s.itemCode"
                    + " , im.name AS itemName"
                    + " , ISNULL(s.recQty, 0) - ISNULL(s.isuQty, 0) AS skuQty"
                    + " , im.sku AS skuCode"
                    + " , um.unit AS skuName"
                    + " , CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return') THEN ISNULL(s.landedSkuValue, 0)"
                    + "        WHEN ISNULL(vt.parentVoucher,'') IN ('Sale', 'Sale Return') THEN 0 - ISNULL(s.landedSkuValue, 0)"
                    + "        WHEN ISNULL(s.recQty, 0) > 0 THEN ISNULL(s.landedSkuValue, 0)"
                    + "        WHEN ISNULL(s.recQty, 0) < 0 THEN ISNULL(s.landedSkuValue, 0)"
                    + "        WHEN ISNULL(s.isuQty, 0) > 0 THEN 0 - ISNULL(s.landedSkuValue, 0)"
                    + "        WHEN ISNULL(s.isuQty, 0) < 0 THEN ISNULL(ABS(s.landedSkuValue), 0)"
                    + "   ELSE 0 END AS landedSkuValue"
                    + " FROM stock s"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = s.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subCode = s.stockContraSubCode"
                    + " LEFT JOIN itemMast im ON im.code = s.itemCode"
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE s.site_Code = :site_Code"
                    + " AND ISNULL(s.isSystemGenerated, 0) = 0" + condition;

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(param.getVoucherNo()).trim().equals("")) {
                sqlWithParam.setString("recId", param.getVoucherNo() + "%");
            }
            if (!DmShared.dNull(param.getVoucherDate()).trim().equals("")) {
                sqlWithParam.setDate("v_Date", DmShared.convertDate(param.getVoucherDate()));
            }
            if (!DmShared.xNull(param.getVoucherTypeCode()).trim().equals("")) {
                sqlWithParam.setStringList("v_Type", param.getVoucherTypeCode());
            }
            if (!DmShared.xNull(param.getRefNo()).trim().equals("")) {
                sqlWithParam.setString("refChallanNo", DmShared.xNull(param.getRefNo()));
            }
            if (!DmShared.xNull(param.getPartyCode()).trim().equals("")) {
                sqlWithParam.setStringList("stockContraSubCode", param.getPartyCode());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdItemAdjustment_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstNode.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                lstNode.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstNode.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstNode.setV_TypeCode(DmShared.xNull(rsTemp.getString("v_TypeCode")));
                lstNode.setV_TypeName(DmShared.xNull(rsTemp.getString("v_TypeName")));
                lstNode.setV_MnuText(DmShared.xNull(rsTemp.getString("mnuText")));
                lstNode.setV_MnuAttachedInModule(DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                lstNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                lstNode.setPartyCode(DmShared.xNull(rsTemp.getString("partyCode")));
                lstNode.setPartyName(DmShared.xNull(rsTemp.getString("partyName")));
                lstNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                lstNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                lstNode.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                lstNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                lstNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                lstNode.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                lstTemp.add(lstNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());
            dmrTemp.setData(lstTemp);
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
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
            } catch (Exception ex) {

            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHLedger_Mdl>> getTxtHLedger(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLedger_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLedger_Mdl> lstTemp = new ArrayList<>();
        TxtHLedger_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   sg.subCode"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , ct.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode"
                    + " LEFT JOIN subGroupSiteList sgl ON sgl.subCode = sg.subCode"
                    + " WHERE sgl.site_Code = :site_Code"
                    + " AND sg.isActive = 1");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLedger_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("subCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
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
                    + "   vt.v_Type as code"
                    + " , vt.description as name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.isActive = 1");
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

    public static DmResult<String> saveRecord(ItemAdjustmentSave_Mdl mrTemp, Connection cnTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        int item_SNo = 0;

        try {
            item_SNo = 0;
            for (GrdItemAdjustment_Mdl gaMdl : mrTemp.getLstItemAdjustments()) {
                if (gaMdl != null) {
                    if (!DmShared.xNull(gaMdl.getSharedKeyFieldCode()).trim().equals("")) {
                        if (DmShared.vNull(gaMdl.getAdjustment()) != 0) {
                            sqlQuery = "INSERT INTO stockOtherAdjustment ("
                                    + "     docId, v_SNo, item_SNo, itemCode, stockRefDocId, stockRefV_SNo, landedValue, sno"
                                    + " ) VALUES ("
                                    + "     :docId, :v_SNo, :item_SNo, :itemCode, :stockRefDocId, :stockRefV_SNo, :landedValue, :sno"
                                    + " )";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.foriegnKey(mrTemp.getVr_DocId()));
                            psTemp.setString("v_SNo", DmShared.foriegnKey(mrTemp.getVr_V_SNo()));
                            psTemp.setString("item_SNo", DmShared.iNull(++item_SNo).toString());
                            psTemp.setString("itemCode", DmShared.foriegnKey(gaMdl.getItemCode()));
                            psTemp.setString("stockRefDocId", DmShared.foriegnKey(gaMdl.getSharedKeyFieldCode()));
                            psTemp.setString("stockRefV_SNo", DmShared.foriegnKey(gaMdl.getV_SNo()));
                            psTemp.setString("landedValue", DmServer.vNull(gaMdl.getAdjustment()));
                            if (DmShared.iNull(gaMdl.getSno()).intValue() == 0) {
                                psTemp.setString("sno", "1");
                            } else {
                                psTemp.setString("sno", DmShared.xNull(gaMdl.getSno()));
                            }
                            psTemp.executeUpdate();

                            updateStockOtherAdjustment(gaMdl.getSharedKeyFieldCode(), gaMdl.getV_SNo(), cnTemp);
                        }
                    }
                }
            }

            dmrTemp.setStatus(true);
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

        return dmrTemp;
    }

    public static List<GrdItemAdjustment_Mdl> getItemAdjustments(String vr_DocId, String vr_V_SNo, Connection cnTemp) {
        List<GrdItemAdjustment_Mdl> lstTemp = new ArrayList<GrdItemAdjustment_Mdl>();
        GrdItemAdjustment_Mdl lstNode;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "SELECT "
                    + "   s.docId"
                    + " , s.recId"
                    + " , s.v_No"
                    + " , s.v_SNo"
                    + " , s.v_Type AS v_TypeCode"
                    + " , vt.short_Name AS v_TypeName"
                    + " , vt.mnuText"
                    + " , vt.mnuAttachedInModule"
                    + " , s.v_Date"
                    + " , s.refChallanNo AS refNo"
                    + " , s.stockContraSubCode AS partyCode"
                    + " , sg.name AS partyName"
                    + " , s.itemCode"
                    + " , im.name AS itemName"
                    + " , ISNULL(s.recQty, 0) - ISNULL(s.isuQty, 0) AS skuQty"
                    + " , im.sku AS skuCode"
                    + " , um.unit AS skuName"
                    + " , s.landedSkuValue"
                    + " , soa.landedValue"
                    + " FROM stockOtherAdjustment soa"
                    + " LEFT JOIN stock s ON s.docId = soa.StockRefDocId AND s.v_SNo = soa.StockRefV_SNo"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = s.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subCode = s.stockContraSubCode"
                    + " LEFT JOIN itemMast im ON im.code = s.itemCode"
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE soa.docId = :vr_docId AND soa.v_SNo = :vr_V_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_docId", DmShared.xNull(vr_DocId));
            psTemp.setString("vr_V_SNo", DmShared.xNull(vr_V_SNo));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdItemAdjustment_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstNode.setV_No(DmShared.xNull(rsTemp.getString("v_No")));
                lstNode.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                lstNode.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                lstNode.setV_TypeCode(DmShared.xNull(rsTemp.getString("v_TypeCode")));
                lstNode.setV_TypeName(DmShared.xNull(rsTemp.getString("v_TypeName")));
                lstNode.setV_MnuText(DmShared.xNull(rsTemp.getString("mnuText")));
                lstNode.setV_MnuAttachedInModule(DmShared.xNull(rsTemp.getString("mnuAttachedInModule")));
                lstNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                lstNode.setPartyCode(DmShared.xNull(rsTemp.getString("partyCode")));
                lstNode.setPartyName(DmShared.xNull(rsTemp.getString("partyName")));
                lstNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                lstNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                lstNode.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                lstNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                lstNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                lstNode.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                lstNode.setAdjustment(DmServer.vNull(rsTemp.getString("landedValue"), "0.00"));
                lstTemp.add(lstNode);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception ex) {
            }
        }

        return lstTemp;
    }

    public static void validateItemAdjustment(String vr_DocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "SELECT"
                    + " COUNT(*) AS cnt"
                    + " FROM ledger l"
                    + " LEFT JOIN ("
                    + "     SELECT soa.docId, soa.v_SNo, ISNULL(SUM(soa.landedValue), 0) AS totalAdjustment"
                    + "     FROM stockOtherAdjustment soa"
                    + "     WHERE soa.docId = :vr_DocId"
                    + "     GROUP BY soa.docId, soa.v_SNo"
                    + " ) vSoa ON vSoa.docId = l.docId AND vSoa.v_SNo = l.v_SNo"
                    + " WHERE l.docId = :vr_DocId"
                    + " AND ABS(ISNULL(l.amtDr, 0) - ISNULL(l.amtCr, 0)) < ABS(vSoa.totalAdjustment)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_DocId", DmShared.xNull(vr_DocId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0000, "You cannot adjust excess value.");
                }
            }
            rsTemp.close();
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

    public static DmResult<String> deleteRecord(String vrDocId, Connection cnTemp) {
        DmResult<String> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        List<KeyFieldExtended> lstKeyField = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended keyField;

        try {

            sqlQuery = "SELECT DISTINCT soa.stockRefDocId, soa.stockRefV_SNo FROM stockOtherAdjustment soa WHERE soa.docId = :vr_DocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_DocId", DmShared.xNull(vrDocId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                keyField = new KeyFieldExtended();
                keyField.setSharedKeyFieldCode(rsTemp.getString("stockRefDocId"));
                keyField.setSharedKeyFieldName(rsTemp.getString("stockRefV_SNo"));
                lstKeyField.add(keyField);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "DELETE FROM stockOtherAdjustment WHERE docId = :vr_DocId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_DocId", DmShared.xNull(vrDocId));
            psTemp.executeUpdate();
            psTemp.close();

            for (KeyFieldExtended kf : lstKeyField) {
                updateStockOtherAdjustment(kf.getSharedKeyFieldCode(), kf.getSharedKeyFieldName(), cnTemp);
            }

            dmrTemp.setStatus(true);

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

        return dmrTemp;
    }

    private static void updateStockOtherAdjustment(String stkDocId, String stkV_SNo, Connection cnTemp) {
        String sqlQuery = "";
        String itemCode = "", ibCode = "", departmentCode = "", site_Code = "", stockValueDate = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            sqlQuery = "UPDATE s"
                    + " SET s.otherAdjustment = v.totalAdjustment"
                    + " FROM stock s"
                    + " LEFT JOIN ("
                    + "     SELECT soa.stockRefDocId, soa.stockRefV_SNo, SUM(soa.landedValue) AS totalAdjustment"
                    + "     FROM stockOtherAdjustment soa"
                    + "     WHERE soa.stockRefDocId = :docId AND soa.stockRefV_SNo = :v_SNo"
                    + "     GROUP BY soa.stockRefDocId, soa.stockRefV_SNo"
                    + " ) v ON v.stockRefDocId = s.docId AND v.stockRefV_SNo = s.v_SNo"
                    + " WHERE s.docId = :docId AND s.v_Sno = :v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(stkDocId));
            psTemp.setString("v_SNo", DmShared.xNull(stkV_SNo));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "SELECT s.itemCode "
                    + " , s.ibCode"
                    + " , s.departmentCode"
                    + " , s.site_Code"
                    + " , s.v_Date"
                    + " FROM stock s "
                    + " WHERE s.docId = :docId"
                    + " AND s.v_SNo = :v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(stkDocId));
            psTemp.setString("v_SNo", DmShared.xNull(stkV_SNo));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                itemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                ibCode = DmShared.xNull(rsTemp.getString("ibCode"));
                departmentCode = DmShared.xNull(rsTemp.getString("departmentCode"));
                stockValueDate = DmShared.dNull(rsTemp.getDate("v_Date"));
                Stock_Server.manageItemStock(itemCode, ibCode, departmentCode, site_Code, stockValueDate, cnTemp);
            }
            rsTemp.close();
            psTemp.close();

            
        } catch (Exception e) {
            throw new DmException(0000, e.getMessage());
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

}
