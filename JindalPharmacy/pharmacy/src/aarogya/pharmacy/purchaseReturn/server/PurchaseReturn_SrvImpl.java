package aarogya.pharmacy.purchaseReturn.server;

import aarogya.pharmacy.purchaseReturn.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.ITSType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.accountAdjustment.shared.AdjustmentSave_Mdl;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;

public class PurchaseReturn_SrvImpl extends DmRSSPrivate implements PurchaseReturn_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pr.v_Type", voucherDateFieldName = "pr.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<List<KeyFieldExtended>>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
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
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("category")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, filtration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!flagFilterByVoucherCategory) {
                sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                listNode.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlWithParam.setSQL("SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, name FROM gstReasonMast");
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
    public DmResultPageing<List<TxtHProformaBillNo_Mdl>> getTxtHProformaBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHProformaBillNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHProformaBillNo_Mdl> lstTemp = new ArrayList<>();
        TxtHProformaBillNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subCode).trim().equals("")) {
                condition += " AND pfpr.subcode = :subcode \n";
            } else {
                condition += " AND 1 = 2 \n";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pfpr.v_Date <= :v_Date \n";
            } else {
                condition += " AND 1 = 2 \n";
            }

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN)) {
//                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "')";
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "') \n";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED + "') \n";
            } else {
                condition += " AND 1 = 2 \n";
            }

            sqlQuery = "SELECT \n"
                    + " pfpr.docId AS  code \n"
                    + " ,pfpr.recId AS name\n "
                    + ", pfpr.V_Date AS pfprV_Date \n"
                    + "  , max(pfpr.remark) as remark,max(grm.code) as gstReasonCode,max(grm.name) as gstReasonName "
                    + "FROM proformaPurchaseReturn pfpr\n"
                    + "LEFT JOIN  voucher_Type vt ON vt.v_Type = pfpr.v_Type \n"
                    + "LEFT JOIN pBillDetail pbd on pbd.pfprDocId = pfpr.docId  \n"
                    + "LEFT JOIN pBill pb on pb.DocId = pbd.docId \n"
                    + "  LEFT JOIN gstReasonMast grm ON pfpr.gstReasonCode = grm.code  "
                    + " WHERE ISNULL(pfpr.approvedBy, '') <> ''  and pfpr.isCancelled = 0 \n"
                    + " AND pfpr.site_Code = :site_Code AND isnull(pb.isProformaReturn,0) = 0  \n" + condition
                    + "GROUP BY pfpr.docId, pfpr.recId, pfpr.V_Date \n";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            //   sqlWithParam.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(subCode).trim().equals("")) {
                sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHProformaBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setPfprV_Date(DmShared.dNull(rsTemp.getDate("pfprV_Date")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                listNode.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                listNode.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));

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
    public DmResultPageing<List<TxtHGrdBillDetail_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHGrdBillDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGrdBillDetail_Mdl> lstTemp = new ArrayList<>();
        TxtHGrdBillDetail_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subCode).trim().equals("")) {
                condition += " AND pb.subcode = :subcode";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pb.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "')";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT \n"
                    + "   pbd.docId AS code \n"
                    + " , pbd.v_SNo AS pbd_Vsno\n"
                    + " , pb.recId AS name \n"
                    + " , pb.v_Date AS pbV_Date \n"
                    + " , pb.partyBillNo AS pbRefNo \n"
                    + " , pb.partyBillDate AS pbRefDate \n"
                    + " , im.name AS medicineName\n"
                    + " , pbd.rate \n"
                    + " , pbd.batchNo \n"
                    + " , pbd.mrp \n"
                    + " , pbd.saleRate \n"
                    + " , pbd.manufacturingDate \n"
                    + " , pbd.expiryDate "
                    + " FROM pBillDetail pbd\n"
                    + " LEFT JOIN pBill pb ON pb.docId = pbd.docId \n"
                    + " LEFT JOIN itemMast im on im.code = pbd.itemCode "
                    + " INNER JOIN voucher_Type vt ON pb.v_Type = vt.v_Type  \n"
                    + " INNER JOIN (  \n"
                    + "     SELECT pbd.docId \n"
                    + "     FROM pBillDetail pbd \n"
                    + "     WHERE ISNULL(pbd.isFooter, 0) = 0 \n"
                    + "     AND ISNULL(pbd.isReturn, 0) = 0 \n"
                    //                    + "     AND (ISNULL(pbd.isStatus, 0) = 0 OR pbd.docId = :billDocId)"
                    + "     AND (CASE \n"
                    + "             WHEN pbd.isStatus = 0 THEN 1 \n"
                    + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0)) > 0 THEN 1 \n"
                    + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0)) > 0 THEN 1 \n"
                    + "             WHEN pbd.isStatus = 2 AND pbd.docId = :billDocId THEN 1 \n"
                    + "             ELSE 0 END) = 1 \n"
                    + "     GROUP BY pbd.docId \n"
                    + " ) AS v ON pb.docId = v.docId \n"
                    + " WHERE ISNULL(pb.approvedBy, '') <> '' \n"
                    + "        AND ISNULL(pbd.isFooter, 0) = 0 \n"
                    + " AND pb.site_Code = :site_Code \n" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(subCode).trim().equals("")) {
                sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGrdBillDetail_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setBillV_SNo(DmShared.xNull(rsTemp.getString("pbd_Vsno")));
                listNode.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                listNode.setPbRefNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                listNode.setPbRefDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                listNode.setRate(DmShared.xNull(rsTemp.getString("rate")));
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setSaleRate(DmShared.xNull(rsTemp.getString("saleRate")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
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

//<editor-fold defaultstate="collapsed" desc="comment- getTxtHBillNo">
/*
    @Override
    public DmResultPageing<List<TxtHBillNo_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBillNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBillNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBillNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subCode).trim().equals("")) {
                condition += " AND pb.subcode = :subcode";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pb.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "')";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT"
                    + "   pb.docId AS code"
                    + " , pb.recId AS name"
                    + " , pb.v_Date AS pbV_Date"
                    + " , pb.partyBillNo AS pbRefNo"
                    + " , pb.partyBillDate AS pbRefDate"
                    + " FROM pBill pb"
                    + " INNER JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " INNER JOIN ("
                    + "     SELECT pbd.docId"
                    + "     FROM pBillDetail pbd"
                    + "     WHERE ISNULL(pbd.isFooter, 0) = 0"
                    + "     AND ISNULL(pbd.isReturn, 0) = 0"
                    //                    + "     AND (ISNULL(pbd.isStatus, 0) = 0 OR pbd.docId = :billDocId)"
                    + "     AND (CASE"
                    + "             WHEN pbd.isStatus = 0 THEN 1"
                    + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0)) > 0 THEN 1"
                    + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0)) > 0 THEN 1"
                    + "             WHEN pbd.isStatus = 2 AND pbd.docId = :billDocId THEN 1"
                    + "             ELSE 0 END) = 1"
                    + "     GROUP BY pbd.docId"
                    + " ) AS v ON pb.docId = v.docId"
                    + " WHERE ISNULL(pb.approvedBy, '') <> ''"
                    + " AND pb.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(subCode).trim().equals("")) {
                sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                listNode.setPbRefNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                listNode.setPbRefDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
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

     */
//</editor-fold>
    @Override
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.code , im.name, im.manualCode, im.hsnCode, im.itemGSTNature, im.itsBasedOn \n"
                    + " , im.isBestBefore, im.propertyList, im.sku AS skuCode, ums.unit AS skuName \n"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces, im.purchaseUnit AS purchaseUnitCode \n"
                    + " , um.unit AS purchaseUnitName, ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate \n"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces,  im.isIncludeFreeQtyInSaleRate \n"
                    + " , um.gstUnitCode, im.subCode as inventoryAc,sgi.name as inventoryAcName \n"
                    + " FROM itemMast im \n"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code \n"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code \n"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code \n"
                    + "	LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) \n"
                    + " AND im.isActive = 1 \n"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                listNode.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                listNode.setIsIncludeFreeQtyInSaleRate(DmShared.iNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")).toString());

                //set Inventory Accounts
                listNode.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                listNode.setInventoryAcName(DmShared.xNull(rsTemp.getString("inventoryAcName")));

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
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBatchNo_Mdl listNode;
        String sqlQuery;
        int i = 1;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ib.code"
                    + " , ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.saleRate"
                    + " , ib.expiryDate"
                    + " , ib.manufacturingDate"
                    + " FROM itemBatch ib"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", DmShared.xNull(medicineCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setExpireDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
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
    //<editor-fold defaultstate="collapsed" desc="Grids">
    @Override
    public DmResult<List<GrdItemDetail_Mdl>> getProformaPurchaseReturnItemDetail(String pfReturnDocId, String location, String v_Date) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult<>();
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", pendingQty = "", join = "", pendingFreeQty = "";
        dmrTemp.setData(lstNode);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT   \n"
                    + "   im.code AS itemCode , im.name AS itemName , pprd.hsnCode , im.itsBasedOn \n"
                    + " , im.isBestBefore , im.propertyList , im.itemGSTNature \n"
                    + " , ppr.docId AS pfprDocId  ,  ppr.recId AS pfprRecId ,ppr.v_Type AS pfprV_Type \n"
                    + " , ppr.v_Date AS pfprV_Date ,pprd.v_SNo AS pfprV_SNo ,ppr.oh_Amt_Net AS pfprNetAmount \n"
                    + " , pprd.pbDocId AS pbDocId , pb.recId AS pbRecId \n"
                    + " , pb.v_Type AS pbV_Type , pb.v_Date AS pbV_Date \n"
                    + " , pb.oh_Amt_Net AS pbNetAmount , pbd.v_SNo AS pbV_SNo \n"
                    + " ,  pprd.pbRefNo as partyBillNo, pprd.pbRefDate as partyBillDate  \n"
                    + " , pc.docId AS pcDocId , pc.recId AS pcRecId , pcd.v_SNo AS pcV_SNo , po.docId AS poDocId \n"
                    + " , po.recId AS poRecId , pod.v_SNo AS poV_SNo , po.refNo AS poRefNo \n"
                    + " , ums.code AS skuCode , ums.unit AS skuName \n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces , um.code AS purchaseUnitCode \n"
                    + " , um.unit AS purchaseUnitName , um.decimalPlaces AS purcahseUnitDecimalPlaces \n"
                    + " , im.purchaseUnitConversionRate , um.gstUnitCode \n"
                    + " , pprd.rate , pprd.mrp , pprd.skuMrp , pprd.saleRate , pprd.skuSaleRate \n"
                    + " , 0 as landedSkuValue , 0 as landedSkuRate , pprd.skuRate \n"
                    + " , ISNULL(pprd.quantity,0) AS pendingQty ,ISNULL(pprd.freeQty,0) AS pendingFreeQty \n"
                    + " , ISNULL(pprd.oh_Amt_Gross,0) / (isNull(pprd.quantity,0) + isNull(pprd.freeQty,0))  as purchaseRate \n"
                    + " , pprd.batchNo , pprd.expiryDate , pprd.manufacturingDate , pprd.remark , pprd.ibCode \n"
                    + " , im.subCode as inventoryAc,sgi.name as inventoryAcName \n"
                    + " \n"
                    + " FROM proformaPurchaseReturn ppr\n"
                    + " LEFT JOIN proformaPurchaseReturnDetail pprd ON pprd.docId = ppr.docId\n"
                    + " LEFT JOIN pBillDetail pbd ON pbd.DocId = pprd.pbDocId AND pbd.V_SNo = pprd.pbV_SNo \n"
                    + " LEFT JOIN pBill pb ON pb.docId = pbd.docId\n"
                    + " LEFT JOIN pChallanDetail pcd ON pcd.docId = pprd.pcDocId AND pcd.v_SNo =pprd.pcV_SNo\n"
                    + " LEFT JOIN pChallan pc ON pc.docId = pprd.docId \n"
                    + " LEFT JOIN pOrderDetail pod ON pod.docId = pprd.poDocId AND pod.v_SNo = pprd.poV_SNo\n"
                    + " LEFT JOIN pOrder po ON po.docId = pprd.docId \n"
                    + " LEFT JOIN itemMast im ON pprd.itemCode = im.code \n"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN unitMast um ON pprd.unitCode = um.code \n"
                    + " LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                    + " WHERE ppr.docId = :billDocId \n"
                    + " AND ISNULL(pprd.isFooter, 0) = 0 \n" + condition
                    + " ORDER BY pprd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("billDocId", DmShared.xNull(pfReturnDocId));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                //Proforma Return  Details
                node.setPfprDocId(DmShared.xNull(rsTemp.getString("pfprDocId")));
                node.setPfprRecId(DmShared.xNull(rsTemp.getString("pfprRecId")));
                node.setPfprV_Type(DmShared.xNull(rsTemp.getString("pfprV_Type")));
                node.setPfprV_Date(DmShared.dNull(rsTemp.getDate("pfprV_Date")));
                node.setPfprV_SNo(DmShared.xNull(rsTemp.getString("pfprV_SNo")));
                node.setPfprNetAmount(DmShared.xNull(rsTemp.getString("pfprNetAmount")));

                // PartyRefNo. and PartyRefDate
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setPartyBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPbNetAmount(DmShared.xNull(rsTemp.getString("pbNetAmount")));

                //Challan Details
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purcahseUnitDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                //Quantities
                node.setQuantity(DmServer.vNull(rsTemp.getString("pendingQty")));

                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));

                node.setFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                double skuQty = (DmShared.vNull(rsTemp.getString("pendingQty")) + DmShared.vNull(rsTemp.getString("pendingFreeQty"))) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
//                double skuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                //Rates
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
//===============================< As Suggested by Dhawan Sir >========================================
//                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
//                double amount = DmShared.vNull(node.getQuantity()) * DmShared.vNull(node.getRate());
                double amount = (DmShared.vNull(node.getQuantity()) + DmShared.vNull(node.getFreeQty())) * DmShared.vNull(node.getRate());
                node.setAmount(DmServer.vNull(amount, "0.00"));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setpBillLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //set Inventory Accounts
                node.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                node.setInventoryAcName(DmShared.xNull(rsTemp.getString("inventoryAcName")));

                //Other
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult<>();
                dmrIts = getITS(node.getSharedKeyFieldCode(), node.getRate(), location, node.getItsBasedOn(), v_Date);
                node.setLstITS(dmrIts.getData());
                node.setGrossAcCode(dmrIts.getValue1());
                node.setGrossAcName(dmrIts.getValue2());

                //OHL PurchaseBill
                node.setOHLListPurchaseBill(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(node.getPfprDocId()), DmShared.xNull(node.getPfprV_SNo()), cnTemp));

                //Add Node
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResult<PurchaseReturn_Mdl> getItemDetail(String v_Type, List<KeyFieldExtended> svms, String location, String v_Date, String docId) {
        DmResult<PurchaseReturn_Mdl> dmrTemp = new DmResult<>();
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node = new GrdItemDetail_Mdl();
        PurchaseReturn_Mdl mrTemp = new PurchaseReturn_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", pendingQty = "", join = "", pendingFreeQty = "";
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) + ISNULL(prd.acceptQty, 0) AS pendingQty \n";
                pendingFreeQty = " , ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) + ISNULL(prd.freeQty,0) AS pendingFreeQty \n";
                condition += " AND (CASE"
                        + "             WHEN pbd.isStatus = 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0) + ISNULL(prd.acceptQty,0)) > 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) + ISNULL(prd.freeQty,0)) > 0 THEN 1 \n"
                        + "             ELSE 0 END) = 1";

                join = " LEFT JOIN ( \n"
                        + "     SELECT prd.pbDocId,prd.pbV_SNo,prd.acceptQty,prd.freeQty \n"
                        + "     FROM pBillDetail prd \n"
                        + "     WHERE prd.docid = :docId \n"
                        + " ) AS prd ON pbd.docId = prd.pbDocId AND pbd.v_SNO = prd.pbV_SNo \n";
            } else {
                pendingQty = " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) AS pendingQty \n";
                pendingFreeQty = " , ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) AS pendingFreeQty \n";
                condition += " AND (CASE"
                        + "             WHEN pbd.isStatus = 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0)) > 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0)) > 0 THEN 1 \n"
                        + "             ELSE 0 END) = 1";
            }

            condition += " AND ISNULL(pbd.isReturn, 0) = 0";

            if (svms != null) {
                String tempCondition = " ";
                for (KeyFieldExtended field : svms) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                            && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {
                        if (!tempCondition.trim().equalsIgnoreCase("")) {
                            tempCondition += " OR ";
                        }
                        tempCondition += " ( pb.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "'";
                        tempCondition += " AND pbd.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "')";
                    }
                }
                if (!DmShared.xNull(tempCondition).trim().equalsIgnoreCase("")) {
                    condition += " AND (" + tempCondition + ") \n";
                }
            }

            sqlQuery = "SELECT"
                    + "   im.code AS itemCode"
                    + " , im.name AS itemName"
                    + " , pbd.hsnCode"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore \n"
                    + " , im.propertyList"
                    + " , im.itemGSTNature"
                    + " , pb.docId AS pbDocId"
                    + " , pb.recId AS pbRecId"
                    + " , pb.v_Type AS pbV_Type"
                    + " , pb.v_Date AS pbV_Date \n"
                    + " , pb.partyBillNo AS pbRefNo \n"
                    + " , pb.partyBillDate AS pbRefDate \n"
                    + " , pb.oh_Amt_Net AS pbNetAmount"
                    + " , pbd.v_SNo AS pbV_SNo"
                    + " , pc.docId AS pcDocId"
                    + " , pc.recId AS pcRecId"
                    + " , pcd.v_SNo AS pcV_SNo"
                    + " , po.docId AS poDocId \n"
                    + " , po.recId AS poRecId"
                    + " , pod.v_SNo AS poV_SNo"
                    + " , po.refNo AS poRefNo"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName \n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , um.code AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , um.decimalPlaces AS purcahseUnitDecimalPlaces \n"
                    + " , im.purchaseUnitConversionRate"
                    + " , um.gstUnitCode \n" + pendingQty + pendingFreeQty
                    + " , pbd.rate"
                    + " , pbd.mrp"
                    + " , pbd.skuMrp"
                    + " , pbd.saleRate"
                    + " ,  pbd.landedSkuValue"
                    + " ,  pbd.landedSkuRate"
                    + " , pbd.skuSaleRate"
                    + " , pbd.skuRate \n"
                    + " , isNull(pbd.oh_Amt_Gross,0) / (isNull(pbd.acceptQty,0) + isNull(pbd.freeQty,0))  as purchaseRate \n"
                    + " , pbd.batchNo"
                    + " , pbd.expiryDate"
                    + " , pbd.manufacturingDate"
                    + " , pbd.remark"
                    + " , pbd.ibCode \n"
                    + " , im.subCode as inventoryAc,sgi.name as inventoryAcName \n"
                    + " FROM pBill pb \n"
                    + " LEFT JOIN pBillDetail pbd ON pb.docId = pbd.docId \n" + join
                    + " LEFT JOIN pChallanDetail pcd ON pbd.pcDocId = pcd.docId AND pbd.pcV_SNo = pcd.v_SNo \n"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId \n"
                    + " LEFT JOIN pOrderDetail pod ON pbd.poDocId = pod.docId AND pbd.poV_SNo = pod.v_SNo \n"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId \n"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code \n"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code \n"
                    + "	LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                    + " WHERE  \n"
                    //  + " pb.docId = :billDocId \n"
                    + "  ISNULL(pbd.isFooter, 0) = 0 \n" + condition
                    + " ORDER BY pbd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            //    psTemp.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(docId));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                node.setPartyBillDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
                node.setPbNetAmount(DmShared.xNull(rsTemp.getString("pbNetAmount")));

                //Challan Details
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purcahseUnitDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                //Quantities
                node.setQuantity(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                double skuQty = (DmShared.vNull(rsTemp.getString("pendingQty")) + DmShared.vNull(rsTemp.getString("pendingFreeQty"))) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
//                double skuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                //Rates
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));

//======         =========================< As Suggested by Dhawan Sir >========================================
//                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
//                double amount = DmShared.vNull(node.getQuantity()) * DmShared.vNull(node.getRate());
                double amount = (DmShared.vNull(node.getQuantity()) + DmShared.vNull(node.getFreeQty())) * DmShared.vNull(node.getRate());
                node.setAmount(DmServer.vNull(amount, "0.00"));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setpBillLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //set Inventory Accounts
                node.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                node.setInventoryAcName(DmShared.xNull(rsTemp.getString("inventoryAcName")));

                //Other
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult<>();
                dmrIts = getITS(node.getSharedKeyFieldCode(), node.getRate(), location, node.getItsBasedOn(), v_Date);
                node.setLstITS(dmrIts.getData());
                node.setGrossAcCode(dmrIts.getValue1());
                node.setGrossAcName(dmrIts.getValue2());

                //OHL PurchaseBill
                node.setOHLListPurchaseBill(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(node.getPbDocId()), DmShared.xNull(node.getPbV_SNo()), cnTemp));

                //Add Node
                lstNode.add(node);
            }
            mrTemp.getListItemDetail().addAll(lstNode);
            rsTemp.close();
            psTemp.close();

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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Approval">
    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "select approvedBy,  approvalDateTime,"
                    + " (case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + " from pBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = " Update pBill set"
                                + " u_EntDt_LatestLine=:uEntDt"
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = case when isNull(isPartialPosting,0)=1 then 1 when isNull(isPartialPosting,0)=0 then 0 end "
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = " Update pBill set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            Gst_Server.manageGstTransRecord(keyCode, walkThroughMove == WalkThroughEnum.ACCEPT, cnTemp);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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

    private WalkThroughDefault_Mdl getWalkThroughFor(PurchaseReturn_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode(mrTemp.getDepartmentCode());
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
            dmrTemp.setData(docId);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
    //</editor-fold>

    @Override
    public DmResult<String[]> getServerDateTime() {
        DmResult<String[]> dmrTemp = new DmResult<String[]>();
        try {
            dmrTemp.setData(DmServer.getServerDateTime());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        }
        return dmrTemp;
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

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setSiteCode(DmServer.getSsnSiteCode(this));
            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));

            /*==============================<< Get default voucher_Type >>===============================*/
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " , vt.balancingAc, sg.name as balancingAcName"
                    + " FROM voucher_Type vt"
                    + " left JOin subgroup sg on vt.balancingAc=sg.subCode "
                    //                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_RETURN + "'"
                    + " WHERE vt.category = :category "
                    + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")) {
                psTemp.setString("category", pvc.PHARMACY_PURCHASE_RETURN);
            } else {
                psTemp.setString("category", pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                defaultValue.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                defaultValue.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                defaultValue.setBalancingAcName(DmShared.xNull(rsTemp.getString("balancingAcName")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get default department >>===============================*/
            sqlQuery = "SELECT"
                    + "   dm.subCode AS code"
                    + " , sg.displayName AS name"
                    + " FROM departmentMast dm"
                    + " LEFT JOIN subGroup sg ON dm.subCode = sg.subCode"
                    + " WHERE sg.IsActive = 1"
                    + " AND dm.subCode = :depCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            /*==========================<< Get Site GST Details >>==========================*/
            sqlQuery = "SELECT "
                    + "   si.gstin AS siteGstin"
                    + " , si.gstinDate AS siteGstinDate"
                    + " , si.registrationType AS siteRegistrationType"
                    + " , st.code AS siteStateCode"
                    + " , st.isUT AS isSiteStateUT"
                    + " FROM siteMast si"
                    + " LEFT JOIN stateMast st ON si.stateNumericCode = st.numericCode"
                    + " WHERE si.code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSiteGstin(DmShared.xNull(rsTemp.getString("siteGstin")));
                defaultValue.setSiteGstinDate(DmShared.dNull(rsTemp.getDate("siteGstinDate")));
                defaultValue.setSiteRegistrationType(DmShared.xNull(rsTemp.getString("siteRegistrationType")));
                defaultValue.setSiteStateCode(DmShared.xNull(rsTemp.getString("siteStateCode")));
                defaultValue.setIsSiteStateUT(DmShared.xNull(rsTemp.getString("isSiteStateUT")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.SinglePOFlowInPurchase,e.inv_DefaultPurchaseDepartment, \n"
                    + "sg.name as defaultPurchaseDepartmentName \n"
                    + "FROM enviro e\n"
                    + "left join subGroup sg on e.inv_DefaultPurchaseDepartment=sg.subCode \n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isIncludeTaxInCost") == null) {
                    throw new DmException(0, "Include Tax in Cost is not defined in Environment Setting.");
                } else if (rsTemp.getString("purchaseSalesAcPostingBasedOn") == null) {
                    throw new DmException(0, "Purchase/Sales A/c Posting Based On is not defined in Environment Setting.");
                } else if (rsTemp.getString("singlePOFlowInPurchase") == null) {
                    throw new DmException(0, "Single PO Flow In Purchase is not defined in Environment Setting.");
                }
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setPurchaseAcPostingBasedOn(DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")));

                defaultValue.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                defaultValue.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< Get Enviro Company Data >>====================================*/
            sqlQuery = "SELECT ec.isInventoryAccountingActive \n"
                    + "FROM enviroCompany ec\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isInventoryAccountingActive") == null) {
                    throw new DmException(0, "Inventory Accounting Active is not defined in Environment Setting (Company).");
                }
                defaultValue.setInventoryAccountingActive(DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
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
    public DmResult<String> saveRecord(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "",
                roundOffItemGstNature = "", roundOffRefDocId = "", roundOffRefV_Type = "", roundOffRefDocValue = "",
                invoiceCGST = "", invoiceSGST = "", invoiceIGST = "",
                invoiceUGST = "", invoiceValue = "";
        ResultSet rsTemp = null;
        GstTrans_Mdl gstTrans_Mdl = null;
        dmrTemp.setData("");

        try {
            PurchaseReturn_Vld.chkSave(mrTempCurrent);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            OverHead_Shared.chkGrid(mrTempCurrent.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTempCurrent.getListItemDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTempCurrent.getVoucherDate(), "");
            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
            }

            if (!DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Check for Approvedby if already approved >>====================================*/
                sqlQuery = "select approvedBy from pBill where  docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                        throw new DmException(0, "Record is approved. You can't Edit this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                /*==============================<< Check for LatestEntry >>====================================*/
                sqlQuery = "SELECT u_EntDt_LatestLine FROM pBill WHERE docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                        if (DmShared.iNull(mrTempCurrent.getU_EntDt_LatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                            throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< Check items for pBill item change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM pBillDetail pbd " + DmShared.xNull(mrTempCurrent.getPbItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Bill has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check for Round Off Item in Enviro >>====================================*/
            sqlQuery = "SELECT ohm.itemCode, im.hsnCode, im.itemGSTNature"
                    + " FROM overHeadMast ohm"
                    + " LEFT JOIN itemMast im ON ohm.itemCode = im.code"
                    + " WHERE ohm.manualCode = 'ROFF'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                roundOffItemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                roundOffItemHsnCode = DmShared.xNull(rsTemp.getString("hsnCode"));
                roundOffItemGstNature = DmShared.xNull(rsTemp.getString("itemGSTNature"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(roundOffItemCode).trim().equals("")) {
                throw new DmException(0, "Round Off Item not defined in Overhead Master.");
            }

            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTempCurrent.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTempCurrent.setRecIdPurchaseReturn(DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTempCurrent.setSharedKeyFieldName(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getRecIdValue()));
                mrTempCurrent.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTempCurrent), DmServer.getSsnUserName(this), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), mrTempCurrent.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTempCurrent.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            String drAdjustDocId = DmServer.executeScalar("select finPayDocId from ledgerAdj where vr_DocId = " + mrTempCurrent.getSharedKeyFieldCode() + "", cnTemp);
            if (!DmShared.xNull(drAdjustDocId).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Edit This Record.");
            }

            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Insert / Update record into pBill >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pBill ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix,"
                        + "     departmentCode, gstReasonCode, subCode, consignee, partyType, rcmApplicable, placeOfSupply,"
                        + "     remark, wtaCode, guiConfigCode, site_Code, preparedBy, u_EntDt, u_EntDt_LatestLine, transferDt,proformaDocid)"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, "
                        + "     :departmentCode, :gstReasonCode, :subCode, :consignee, :partyType, :rcmApplicable, :placeOfSupply,"
                        + "     :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :uEntDt, :uEntDt, :transferDt,:proformaDocid)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTempCurrent.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTempCurrent.getDepartmentCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTempCurrent.getGstReasonCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTempCurrent.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTempCurrent.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setString("proformaDocid", DmShared.iNull(mrTempCurrent.getProformaBillNoTag()).toString());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "UPDATE pBill SET \n"
                        + "  v_Date = :v_Date"
                        + " ,departmentCode = :departmentCode"
                        + " ,gstReasonCode = :gstReasonCode"
                        + " ,subCode = :subCode"
                        + " ,consignee = :consignee"
                        + " ,partyType = :partyType"
                        + " ,rcmApplicable = :rcmApplicable"
                        + " ,placeOfSupply = :placeOfSupply"
                        + " ,remark = :remark"
                        + " ,u_EntDt = :uEntDt"
                        + " ,u_EntDt_LatestLine = :uEntDt"
                        + " ,u_Name = :u_Name"
                        + " ,transferDt = :transferDt"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTempCurrent.getDepartmentCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTempCurrent.getGstReasonCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Manage PBILL (Returned Qty) >>====================================*/
            manageReturnedQty(DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString(), true, cnTemp);

            /*==============================<< Delete records into gstTrans >>====================================*/
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode())), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Delete records from pBillDetail >>====================================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pBillDetail", "ibCode", " AND docid = '" + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "'", cnTemp);

            sqlQuery = "Delete From pBillDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into pBillDetail >>====================================*/
            List<GrdItemDetail_Mdl> listDetail = mrTempCurrent.getListItemDetail();
            int vSNo = 0;
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    detail_Mdl.setV_SNo(String.valueOf(++vSNo));
                    sqlQuery = "INSERT INTO pBillDetail ("
                            + "     docId, v_SNo,pfprDocId,pfprV_SNo , pbDocId, pbV_SNo, pcDocId, pcV_SNo, poDocId, poV_SNo, itemCode, hsnCode, itemGSTNature, batchNo, expiryDate,"
                            + "     manufacturingDate, mrp, skuMrp, saleRate, skuSaleRate, unitCode, docQty, acceptQty, freeQty, skuQty, "
                            + "     skuQtyForRate, landedSkuValue, landedSkuRate, skuRate, rate, remark,pbRefNo,pbRefDate)"
                            + " Values ("
                            + "     :docId, :v_SNo,:pfprDocId,:pfprV_SNo, :pbDocId, :pbV_SNo, :pcDocId, :pcV_SNo, :poDocId, :poV_SNo, :itemCode, :hsnCode, :itemGSTNature, :batchNo, :expiryDate,"
                            + "     :manufacturingDate, :mrp, :skuMrp, :saleRate, :skuSaleRate, :unitCode, :docQty, :acceptQty, :freeQty, :skuQty, "
                            + "     :skuQtyForRate, :landedSkuValue, :landedSkuRate, :skuRate, :rate, :remark,:pbRefNo,:pbRefDate)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    //=====================================<MJ>=====================================//
                    psTemp.setString("pfprDocId", DmShared.foriegnKey(detail_Mdl.getPfprDocId()));
                    psTemp.setString("pfprV_SNo", DmShared.foriegnKey(detail_Mdl.getPfprV_SNo()));
                    //==================================<********>==================================//

                    psTemp.setString("pbDocId", DmShared.foriegnKey(detail_Mdl.getPbDocId()));
                    psTemp.setString("pbV_SNo", DmShared.foriegnKey(detail_Mdl.getPbV_SNo()));
                    psTemp.setString("pcDocId", DmShared.foriegnKey(detail_Mdl.getPcDocId()));
                    psTemp.setString("pcV_SNo", DmShared.foriegnKey(detail_Mdl.getPcV_SNo()));
                    psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                    psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                    psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate(), "0.00"));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQty()));
                    psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
                    psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("pbRefNo", DmShared.xNull(detail_Mdl.getPartyBillNo()));
                    psTemp.setDate("pbRefDate", DmServer.convertDate(detail_Mdl.getPartyBillDate()));

                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "UPDATE itemDepartmentInfo SET isHoldRequirement = 0 "
                            + " WHERE itemCode = " + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode())
                            + " AND departmentCode = " + DmShared.xNull(mrTempCurrent.getDepartmentCode())
                            + " AND site_Code = " + DmShared.xNull(DmServer.getSsnSiteCode(this))
                            + " AND ISNULL(isHoldRequirement,0) <> 0 ";
                    DmServer.executeSQL(sqlQuery, cnTemp);
                }
            }

            /*==============================<< Insert records into OH >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTempCurrent.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            OverHead_Server.save(mrTempCurrent.getOverHeadList(), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", "", "", "", "", false, true, cnTemp);


            /*==============================<< Insert records into stock >>====================================*/
            ItemBatch_Server.compareProperties(iniItemBatchProperties(mrTempCurrent, mrTempPrevious, false), cnTemp, this);

            /*==============================<< Update ibCode From Stock Table >>====================================*/
            sqlQuery = "UPDATE pBillDetail"
                    + " SET pBillDetail.ibCode = stock.ibCode"
                    + " FROM stock"
                    + " WHERE stock.docid = pBillDetail.docId"
                    + " AND stock.v_SNo = pBillDetail.v_SNo"
                    + " AND pBillDetail.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Manage PBILL (Returned Qty) >>==============================*/
            manageReturnedQty(DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString(), false, cnTemp);

            /*==============================<< Get header tax details >>====================================*/
            sqlQuery = "SELECT"
                    + "   pr.oh_Amt_CGST"
                    + " , pr.oh_Amt_SGST"
                    + " , pr.oh_Amt_IGST"
                    + " , pr.oh_Amt_UGST"
                    + " , pr.oh_Amt_Net"
                    + " FROM pBill pr"
                    + " WHERE pr.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                invoiceCGST = DmShared.xNull(rsTemp.getString("oh_Amt_CGST"));
                invoiceSGST = DmShared.xNull(rsTemp.getString("oh_Amt_SGST"));
                invoiceIGST = DmShared.xNull(rsTemp.getString("oh_Amt_IGST"));
                invoiceUGST = DmShared.xNull(rsTemp.getString("oh_Amt_UGST"));
                invoiceValue = DmShared.xNull(rsTemp.getString("oh_Amt_Net"));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Insert records into gstTrans >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    //<editor-fold defaultstate="collapsed" desc="Set gstTrans_Mdl">
                    gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(DmShared.xNull(detail_Mdl.getV_SNo()));
                    gstTrans_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTempCurrent.getVoucherDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTempCurrent.getSupplierCode()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTempCurrent.getSupplierCode()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));

                    //<editor-fold defaultstate="collapsed" desc="Reference Detail">
                    gstTrans_Mdl.setRefDocId(DmShared.xNull(detail_Mdl.getPbDocId()));
                    gstTrans_Mdl.setRefRecId(DmShared.xNull(detail_Mdl.getPbRecId()));
                    gstTrans_Mdl.setRefV_Date(DmShared.dNull(detail_Mdl.getPbV_Date()));
                    gstTrans_Mdl.setRefPartyBillNo(DmShared.xNull(detail_Mdl.getPartyBillNo()));
                    gstTrans_Mdl.setRefPartyBillDt(DmShared.dNull(detail_Mdl.getPartyBillDate()));
                    gstTrans_Mdl.setRefV_Type(DmShared.xNull(detail_Mdl.getPbV_Type()));
                    gstTrans_Mdl.setRefDocValue(DmShared.xNull(detail_Mdl.getPbNetAmount()));

                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTempCurrent.getGstReasonCode()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTempCurrent.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(detail_Mdl.getHsnCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    gstTrans_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
                    gstTrans_Mdl.setQty(DmServer.vNull(DmShared.vNull(detail_Mdl.getQuantity()) + DmShared.vNull(detail_Mdl.getFreeQty()), DmShared.getNumberFormat(DmShared.iNull(detail_Mdl.getPurchaseUnitDecimalPlaces()).intValue())));
                    gstTrans_Mdl.setGstUnitCode(DmShared.xNull(detail_Mdl.getGstUnitCode()));
                    gstTrans_Mdl.setInvoiceCGST(DmShared.xNull(invoiceCGST));
                    gstTrans_Mdl.setInvoiceSGST(DmShared.xNull(invoiceSGST));
                    gstTrans_Mdl.setInvoiceIGST(DmShared.xNull(invoiceIGST));
                    gstTrans_Mdl.setInvoiceUGST(DmShared.xNull(invoiceUGST));
                    gstTrans_Mdl.setInvoiceValue(DmShared.xNull(invoiceValue));
                    gstTrans_Mdl.setIsTaxInclusive("0");
                    gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.INPUT.toInt()));
                    gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));  //<MJ>
                    gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                    gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                    gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                    gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.INPUT.toInt()));

                    //<editor-fold defaultstate="collapsed" desc="Get ItemTaxDetail">
                    sqlQuery = "SELECT"
                            + "   prd.oh_Amt_Gross"
                            + " , prd.oh_Amt_Taxable"
                            + " , prd.oh_at_CGST"
                            + " , prd.oh_Amt_CGST"
                            + " , prd.oh_at_SGST"
                            + " , prd.oh_Amt_SGST"
                            + " , prd.oh_at_IGST"
                            + " , prd.oh_Amt_IGST"
                            + " , prd.oh_at_UGST"
                            + " , prd.oh_Amt_UGST"
                            + " , prd.oh_Amt_Net"
                            + " FROM pBillDetail prd"
                            + " WHERE prd.docId = :docId"
                            + " AND prd.v_Sno = :v_Sno";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    psTemp.setString("v_Sno", DmShared.xNull(detail_Mdl.getV_SNo()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gstTrans_Mdl.setItemGrossAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Gross")));
                        gstTrans_Mdl.setItemTaxableAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Taxable")));
                        gstTrans_Mdl.setItemCgstRate(DmShared.xNull(rsTemp.getString("oh_at_CGST")));
                        gstTrans_Mdl.setItemCgstAmount(DmShared.xNull(rsTemp.getString("oh_Amt_CGST")));
                        gstTrans_Mdl.setItemSgstRate(DmShared.xNull(rsTemp.getString("oh_at_SGST")));
                        gstTrans_Mdl.setItemSgstAmout(DmShared.xNull(rsTemp.getString("oh_Amt_SGST")));
                        gstTrans_Mdl.setItemIgstRate(DmShared.xNull(rsTemp.getString("oh_at_IGST")));
                        gstTrans_Mdl.setItemIgstAmount(DmShared.xNull(rsTemp.getString("oh_Amt_IGST")));
                        gstTrans_Mdl.setItemUgstRate(DmShared.xNull(rsTemp.getString("oh_at_UGST")));
                        gstTrans_Mdl.setItemUgstAmount(DmShared.xNull(rsTemp.getString("oh_Amt_UGST")));
                        gstTrans_Mdl.setItemNetAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Net")));
                    }
                    rsTemp.close();
                    psTemp.close();
                    //</editor-fold>
                    //</editor-fold>
                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);

                }
            }


            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (mrTempCurrent.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTempCurrent.getModifyReasonMdl(), DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), userName, cnTemp);
            }
            /*==============================<< Update isProformaReturn in pBillDetail >>====================================*/
            sqlQuery = " UPDATE pBill SET "
                    + "isProformaReturn =  1 "
                    + "WHERE docId = " + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTempCurrent.getGuiMdl().getGuiRowList(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            //Delete Unused Item Batches
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                Stock_Server.deleteUnusedItemBatches(ibCodeCsv, "", cnTemp);
            }

            dmrTemp.setData(mrTempCurrent.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getVoucherTypeCode(), this);
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
    public DmResult<PurchaseReturn_Mdl> getMoveRec(String strKey) {
        DmResult<PurchaseReturn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PurchaseReturn_Mdl mrTemp = new PurchaseReturn_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT "
                    + "       pr.docId"
                    + "      ,pr.v_Type"
                    + "      ,pr.v_No"
                    + "      ,pr.recId"
                    + "      ,pr.v_Prefix"
                    + "      ,pr.v_Date"
                    + "      ,pr.subCode"
                    + "      ,pr.consignee"
                    + "      ,pr.placeOfSupply"
                    + "      ,pr.partyBillNo"
                    + "      ,pr.partyBillDate"
                    + "      ,pr.calcOn"
                    + "      ,pr.CMN_Addition"
                    + "      ,pr.CMN_Deduction"
                    + "      ,pr.oh_Amt_Gross"
                    + "      ,pr.oh_Amt_Total"
                    + "      ,pr.oh_Amt_RoundOff"
                    + "      ,pr.oh_Amt_Net"
                    + "      ,pr.u_EntDt"
                    + "      ,pr.u_Name"
                    + "      ,pr.preparedBy"
                    + "      ,pr.site_Code"
                    + "      ,pr.wtaCode"
                    + "      ,pr.gstReasonCode"
                    + "      ,pr.departmentCode"
                    + "      ,pr.remark"
                    + "      ,pr.approvedBy"
                    + "      ,pr.approvalDateTime"
                    + "      ,pr.transferDt"
                    + "      ,pr.recIdPrefix"
                    + "      ,pr.rcmApplicable"
                    + "      ,pr.partyType"
                    + "      ,pr.u_EntDt_LatestLine"
                    + "      ,pr.oh_Amt_Discount"
                    + "      ,pr.oh_Amt_Taxable"
                    + "      ,pr.oh_Amt_CGST"
                    + "      ,pr.oh_Amt_SGST"
                    + "      ,pr.oh_Amt_IGST"
                    + "      ,pr.oh_Amt_UGST"
                    + "      ,pr.oh_Amt_FinalDiscount"
                    + "      ,pr.guiConfigCode , ppr.docid as pprDocid , ppr.recId as pprRecId ,ppr.v_Date as pprDate "
                    + " , vt.description AS voucherTypeName"
                    + " , vt.category AS voucherCategory"
                    + " , sgs.name AS supplierName"
                    + " , sgd.name AS departmentName"
                    + " , grm.name AS gstReasonName"
                    + " , c.stateCode AS supplierStateCode"
                    + " , sgs.partyType AS supplierType"
                    + " , sgs.gstin AS supplierGstin"
                    + " , sgs.gstinDate AS supplierGstinDate"
                    + " FROM pBill pr"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgs ON pr.subcode = sgs.subCode"
                    + " LEFT JOIN subGroup sgd ON pr.departmentCode = sgd.subCode"
                    + " LEFT JOIN city c ON sgs.cityCode = c.cityCode"
                    + " LEFT JOIN gstReasonMast grm ON pr.gstReasonCode = grm.code  "
                    + " LEFT JOIN proformaPurchaseReturn ppr ON ppr.docid = pr.proformaDocid  "
                    + " WHERE pr.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.setProformaBillNoTag(DmShared.xNull(rsTemp.getString("pprDocid")));
                mrTemp.setProformaBillNo(DmShared.xNull(rsTemp.getString("pprRecId")));
                mrTemp.setProformaBillDate(DmShared.dNull(rsTemp.getDate("pprDate")));

                mrTemp.getRecIdPurchaseReturn().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdPurchaseReturn().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdPurchaseReturn().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));

                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("voucherCategory")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setSupplierCode(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
//                mrTemp.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
//                mrTemp.setPartyBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                mrTemp.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Medicine grid detail >>===============================*/
            sqlQuery = "SELECT pbd.landedSkuRate as pBillLandedSkuRate \n"
                    + " , im.code AS itemCode \n"
                    + " , im.name AS itemName \n"
                    + " , prd.hsnCode \n"
                    + " , im.itsBasedOn \n"
                    + " , im.isBestBefore \n"
                    + " , im.propertyList \n"
                    + " , im.itemGSTNature \n"
                    + " , pfpr.docId AS pfprDocId \n"
                    + " , pfpr.recId AS pfprRecId \n"
                    + " , pfpr.v_Type AS pfprV_Type \n"
                    + " , pfpr.v_Date AS pfprV_Date \n"
                    + " , pfpr.v_Date AS pfprV_SNo \n"
                    + " , pfpr.oh_Amt_Net AS pfprNetAmount  \n"
                    + " , pb.docId AS pbDocId \n"
                    + " , pb.recId AS pbRecId \n"
                    + " , pb.v_Type AS pbV_Type \n"
                    + " , pb.v_Date AS pbV_Date \n"
                    + " , pb.oh_Amt_Net AS pbNetAmount \n"
                    + " , pbd.v_SNo AS pbV_SNo \n"
                    + " , prd.pbRefNo as partyBillNo ,prd.pbRefDate as partyBillDate \n"
                    + " , pc.docId AS pcDocId \n"
                    + " , pc.recId AS pcRecId \n"
                    + " , pcd.v_SNo AS pcV_SNo \n"
                    + " , po.docId AS poDocId \n"
                    + " , po.recId AS poRecId \n"
                    + " , pod.v_SNo AS poV_SNo \n"
                    + " , po.refNo AS poRefNo \n"
                    + " , ums.code AS skuCode \n"
                    + " , ums.unit AS skuName \n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces \n"
                    + " , um.code AS purchaseUnitCode \n"
                    + " , um.unit AS purchaseUnitName \n"
                    + " , um.decimalPlaces AS purcahseUnitDecimalPlaces \n"
                    + " , im.purchaseUnitConversionRate \n"
                    + " , um.gstUnitCode \n"
                    + " , prd.acceptQty \n"
                    + " , prd.freeQty \n"
                    + " , prd.skuQty \n"
                    + " , prd.rate \n"
                    + " , prd.landedSkuValue \n"
                    + " , prd.landedSkuRate \n"
                    + " , prd.skuRate \n"
                    + " , prd.oh_Amt_Gross \n"
                    + " , prd.mrp \n"
                    + " , prd.skuMrp \n"
                    + " , prd.saleRate \n"
                    + " , prd.skuSaleRate \n"
                    + " , prd.batchNo \n"
                    + " , prd.expiryDate \n"
                    + " , prd.manufacturingDate \n"
                    + " , prd.remark \n"
                    + " , prd.v_SNo \n"
                    + " , prd.ibCode \n"
                    + " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) + ISNULL(prd.acceptQty, 0) AS pendingQty \n"
                    + " , ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) + ISNULL(prd.freeQty,0) AS pendingFreeQty \n"
                    + " FROM pBill pr  \n"
                    + " LEFT JOIN pBillDetail prd ON pr.docId = prd.docId \n"
                    + " LEFT JOIN proformaPurchaseReturnDetail pfprd ON pfprd.docId = prd.docId AND pfprd.v_SNo = prd.v_SNo \n"
                    + " LEFT JOIN proformaPurchaseReturn pfpr ON pfpr.docId = prd.docId \n"
                    + " LEFT JOIN pBillDetail pbd ON prd.pbDocId = pbd.docId AND prd.pbV_SNo = pbd.v_SNo \n"
                    + " LEFT JOIN pBill pb ON pbd.docId = pb.docId \n"
                    + " LEFT JOIN pChallanDetail pcd ON prd.pcDocId = pcd.docId AND prd.pcV_SNo = pcd.v_SNo \n"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId \n"
                    + " LEFT JOIN pOrderDetail pod ON prd.poDocId = pod.docId AND prd.poV_SNo = pod.v_SNo \n"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId \n"
                    + " LEFT JOIN itemMast im ON prd.itemCode = im.code \n"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN unitMast um ON prd.unitCode = um.code \n"
                    + " WHERE prd.docId = :docId \n"
                    + " AND ISNULL(prd.isFooter, 0) = 0 \n"
                    + " ORDER BY prd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                //proforma Purchase Return Details
                node.setPfprDocId(DmShared.xNull(rsTemp.getString("pfprDocId")));
                node.setPfprRecId(DmShared.xNull(rsTemp.getString("pfprRecId")));
                node.setPfprV_Type(DmShared.xNull(rsTemp.getString("pfprV_Type")));
                node.setPfprV_Date(DmShared.dNull(rsTemp.getDate("pfprV_Date")));
                node.setPfprV_SNo(DmShared.xNull(rsTemp.getString("pfprV_SNo")));
                node.setPfprNetAmount(DmShared.xNull(rsTemp.getString("pfprNetAmount")));

//                mrTemp.setProformaBillNo(node.getPfprRecId());
//                mrTemp.setProformaBillNoTag(node.getPfprDocId());
//                mrTemp.setProformaBillDate(node.getPfprV_Date());
                //party Ref No. and Party RefDate
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setPartyBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPbNetAmount(DmShared.xNull(rsTemp.getString("pbNetAmount")));

                //Challan Detail
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purcahseUnitDecimalPlaces")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double acceptQty = DmShared.vNull(rsTemp.getString("acceptQty"));
                double freeQty = DmShared.vNull(rsTemp.getString("freeQty"));
                double purchaseUnitConversionRate = (skuQty / (acceptQty + freeQty));
                String puConversionRate = DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue()));
                node.setPurchaseUnitConversionRate(DmServer.vNull(puConversionRate));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Quantities
                node.setQuantity(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));

                //Rate
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));

                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

                //Other
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

                /*------------------- Calculate Landed Sku Rate of Purchase Bill--------------------------------*/
                String landedSkuRate = "";
                landedSkuRate = DmServer.executeScalar("select top 1 \n"
                        + "pbd.landedSkuRate\n"
                        + "from pBillDetail pbd \n"
                        + "left join pBill pb on pb.docid = pbd.docid\n"
                        + "left join itemMast im on im.code = pbd.itemCode\n"
                        + "left join itemBatch ib on ib.code = pbd.ibCode \n"
                        + "where im.code  = " + DmShared.iNull(node.getSharedKeyFieldCode()).toString()
                        + "and ib.code = " + DmShared.iNull(node.getIbCode()).toString()
                        + "order by pb.v_Date desc", cnTemp);
                node.setpBillLandedSkuRate(DmServer.vNull(landedSkuRate));


                /*==============================<< Get OverheadLine detail >>===============================*/
                node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListItemDetail().addAll(lstNode);
            /*==============================<< Get Overhead detail >>===============================*/
            mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));

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
    public DmResult<String> deleteRecord(String keyCode, String guiConfigCode, PurchaseReturn_Mdl mrTempPrevious) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check for approvedBy before deleting data >>===============================*/
            sqlQuery = "SELECT approvedBy, wtaCode FROM pBill WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                wtaCode = DmShared.xNull(rsTemp.getString("wtaCode"));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            String drAdjustDocId = DmServer.executeScalar("select finPayDocId from ledgerAdj where vr_DocId = " + DmShared.xNull(keyCode) + "", cnTemp);
            if (!DmShared.xNull(drAdjustDocId).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Edit This Record.");
            }

            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(keyCode, cnTemp);

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason & Walkthrough >>============================*/
            OverHeadLine_Server.delete(keyCode, cnTemp);
            OverHead_Server.delete(keyCode, cnTemp);
            ModifyReason_Server.delete(keyCode, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);
            Gst_Server.deleteGstTrans(keyCode, cnTemp);

            /*==============================<< Update record in GRN >>===============================*/
            manageReturnedQty(DmShared.iNull(keyCode).toString(), true, cnTemp);

            /*==============================<< Delete Stock >>====================================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Delete record from pBillDetail >>===============================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pBillDetail", "ibCode", " AND docid = '" + DmShared.iNull(keyCode).toString() + "'", cnTemp);

            sqlQuery = "DELETE FROM pBillDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pBill >>===============================*/
            sqlQuery = "DELETE FROM pBill WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.executeUpdate();
            psTemp.close();

            //Delete Unused Item Batches
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                Stock_Server.deleteUnusedItemBatches(ibCodeCsv, "", cnTemp);
            }

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
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT pr.docId"
                    + " FROM pBill pr"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')"
                    + " AND pr.site_Code = :site_Code"
                    + " AND pr.v_Prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY pr.v_Date DESC, vt.short_Name, pr.v_No DESC, pr.recId";
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   pr.docId"
                    + " , pr.recId"
                    + " , pr.v_Date"
                    + " , pr.v_No"
                    + " , sg.name AS supplierName\n"
                    + " , sgd.name AS departmentName"
                    + " , vt.short_Name"
                    + " , pr.approvedBy\n"
                    + " , format(pr.approvalDateTime,'dd-MMM-yyyy HH.mm') AS approvalDateTime, pr.partyBillNo, \n"
                    + "pr.partyBillDate, gio.recId as gateNo \n"
                    + " 	FROM pBill pr\n"
                    + " 	LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type\n"
                    + " 	LEFT JOIN subGroup sg ON pr.subCode = sg.subCode\n"
                    + " 	LEFT JOIN subGroup sgd ON pr.departmentCode = sgd.subCode\n"
                    + "		LEFT JOIN gateInOut gio ON gio.docId = pr.gateDocId \n"
                    + " 	WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')"
                    + " 	AND pr.site_Code = :site_Code"
                    + " 	AND pr.v_Prefix = :vPrefix";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                listNode.setGateNo(DmShared.xNull(rsTemp.getString("gateNo")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
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
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            dmrTemp = STServer.getITS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, ITSType.PURCHASE_RETURN, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private CompareProperties_Mdl iniItemBatchProperties(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevious, boolean isDelete) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        if (!isDelete) {
            lstItemBatch_MdlCurnt.clear();
            for (GrdItemDetail_Mdl detail_MdlCurrent : mrTempCurrent.getListItemDetail()) {
                ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0) {
                    OverHeadLine_Mdl ohMdlLn = OverHeadLine_Shared.getOHByManualCode("TV", detail_MdlCurrent.getOHLList());
                    double purchaseTaxableRate = 0;
                    if (DmShared.vNull(detail_MdlCurrent.getQuantity()) != 0) {
                        purchaseTaxableRate = DmShared.vNull(ohMdlLn.getAmount()) / DmShared.vNull(detail_MdlCurrent.getQuantity());
                    }
                    batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                    batch_Mdl.getIpm().setBatchNo(DmShared.xNull(detail_MdlCurrent.getBatchNo()));
                    batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(detail_MdlCurrent.getExpiryDate()));
                    batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlCurrent.getManufacturingDate()));
                    batch_Mdl.getIpm().setMrp(DmShared.xNull(detail_MdlCurrent.getMrp()));
                    batch_Mdl.getIpm().setSkuMrp(DmServer.vNull(detail_MdlCurrent.getSkuMrp()));

                    batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                    batch_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    batch_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                    batch_Mdl.setvType(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                    batch_Mdl.setvSno(DmShared.xNull(detail_MdlCurrent.getV_SNo()));
                    batch_Mdl.setPurchaseRateExFree(DmShared.xNull(detail_MdlCurrent.getRate()));
                    batch_Mdl.setPurchaseTaxableRateExFree(DmServer.vNull(purchaseTaxableRate));
                    batch_Mdl.setSaleRate(DmShared.xNull(detail_MdlCurrent.getSaleRate()));
                    batch_Mdl.setSkuSaleRate(DmShared.xNull(detail_MdlCurrent.getSkuSaleRate()));
                    batch_Mdl.setReceiveQty(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getSkuQty())));
                    batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));

                    batch_Mdl.setStockSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getAmount()), "0.00"));
                    batch_Mdl.setStockLandedSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getLandedSkuValue())));
                    batch_Mdl.setStaffSaleRate(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getStaffSkuValue())));
                    batch_Mdl.setStockV_Sno(detail_MdlCurrent.getV_SNo());
                    batch_Mdl.setStockRefChallanNo(DmShared.xNull(detail_MdlCurrent.getPartyBillNo()));
                    batch_Mdl.setStockRefChallanDate(DmShared.dNull(detail_MdlCurrent.getPartyBillDate()));
                    batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));

                    lstItemBatch_MdlCurnt.add(batch_Mdl);
                }
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdItemDetail_Mdl detail_MdlPrev : mrTempPrevious.getListItemDetail()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
                    OverHeadLine_Mdl ohMdlLn = OverHeadLine_Shared.getOHByManualCode("TV", detail_MdlPrev.getOHLList());
                    double purchaseTaxableRate = 0;
                    if (DmShared.vNull(detail_MdlPrev.getQuantity()) != 0) {
                        purchaseTaxableRate = DmShared.vNull(ohMdlLn.getAmount()) / DmShared.vNull(detail_MdlPrev.getQuantity());
                    }
                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                    batch_MdlPrev.getIpm().setBatchNo(DmShared.xNull(detail_MdlPrev.getBatchNo()));
                    batch_MdlPrev.getIpm().setExpiryDate(DmShared.dNull(detail_MdlPrev.getExpiryDate()));
                    batch_MdlPrev.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlPrev.getManufacturingDate()));
                    batch_MdlPrev.getIpm().setMrp(DmShared.xNull(detail_MdlPrev.getMrp()));
                    batch_MdlPrev.getIpm().setSkuMrp(DmServer.vNull(detail_MdlPrev.getSkuMrp()));

                    batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                    batch_MdlPrev.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
                    batch_MdlPrev.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
                    batch_MdlPrev.setvType(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
                    batch_MdlPrev.setvSno(DmShared.xNull(detail_MdlPrev.getV_SNo()));
                    batch_MdlPrev.setPurchaseRateExFree(DmShared.xNull(detail_MdlPrev.getRate()));
                    batch_MdlPrev.setPurchaseTaxableRateExFree(DmServer.vNull(purchaseTaxableRate));
                    batch_MdlPrev.setSaleRate(DmShared.xNull(detail_MdlPrev.getSaleRate()));
                    batch_MdlPrev.setSkuSaleRate(DmShared.xNull(detail_MdlPrev.getSkuSaleRate()));
                    batch_MdlPrev.setReceiveQty(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getSkuQty())));
                    batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));

                    batch_MdlPrev.setStockSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getAmount()), "0.00"));
                    batch_MdlPrev.setStockLandedSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getLandedSkuValue())));

                    batch_MdlPrev.setStaffSaleRate(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getStaffSkuValue())));

                    batch_MdlPrev.setStockV_Sno(detail_MdlPrev.getV_SNo());
                    batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(detail_MdlPrev.getPartyBillNo()));
                    batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(detail_MdlPrev.getPartyBillDate()));
                    batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));

                    lstItemBatch_MdlPrev.add(batch_MdlPrev);
                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierCode()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getRecIdPurchaseReturn().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getRecIdPurchaseReturn().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempPrevious.getSupplierCode()));

        }
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
        }
        return compareProperties_Mdl;
    }

    private void manageReturnedQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, pbDocId = "";
        double prAcceptQty, prFreeQty, pbReturnedQty, pbReturnedFreeQty, pbAcceptQty, pbFreeQty;
        Integer isStatus;

        try {
            sqlQuery = "SELECT"
                    + "   pb.docId AS pbDocId"
                    + " , prd.pbV_SNo"
                    + " , prd.acceptQty AS prAcceptQty"
                    + " , prd.freeQty AS prFreeQty"
                    + " , pbd.acceptQty AS pbAcceptQty"
                    + " , pbd.freeQty AS pbFreeQty"
                    + " , pbd.returnedQty AS pbReturnedQty"
                    + " , pbd.returnedFreeQty AS pbReturnedFreeQty"
                    + " , pbd.isStatus"
                    + " , pb.approvedBy"
                    + " , pb.recId AS pbNo"
                    + " , vt.short_name AS pbType"
                    + " FROM pBillDetail pbd"
                    + " LEFT JOIN pBill pb ON pbd.docId = pb.docId"
                    + " LEFT JOIN voucher_type vt ON pb.v_Type = vt.v_type"
                    + " INNER JOIN ("
                    + "     SELECT prd.pbDocId, prd.pbV_SNo, sum(prd.acceptQty) AS acceptQty, sum(prd.freeQty) AS freeQty"
                    + "     FROM pBillDetail prd"
                    + "     WHERE prd.docId = :docId"
                    + "     GROUP BY prd.pbDocId, prd.pbV_SNo"
                    + " ) prd ON pbd.docId = prd.pbDocId AND pbd.v_SNo = prd.pbV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill is Cancelled");
                }

                pbReturnedQty = rsTemp.getDouble("pbReturnedQty");
                pbReturnedFreeQty = rsTemp.getDouble("pbReturnedFreeQty");
                prAcceptQty = rsTemp.getDouble("prAcceptQty");
                prFreeQty = rsTemp.getDouble("prFreeQty");
                pbAcceptQty = rsTemp.getDouble("pbAcceptQty");
                pbFreeQty = rsTemp.getDouble("pbFreeQty");

                if (isDelete) {
                    pbReturnedQty -= prAcceptQty;
                    pbReturnedFreeQty -= prFreeQty;
                } else {
                    pbReturnedQty += prAcceptQty;
                    pbReturnedFreeQty += prFreeQty;
                }

                if (pbReturnedQty > pbAcceptQty) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill overflows by " + DmServer.vNull(pbReturnedQty - pbAcceptQty));
                } else if (pbReturnedQty < 0) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill overflows by " + DmServer.vNull(pbReturnedQty));
                } else if (pbReturnedQty == pbAcceptQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (pbReturnedFreeQty > pbFreeQty) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill overflows free qty by " + DmServer.vNull(pbReturnedFreeQty - pbFreeQty));
                } else if (pbReturnedFreeQty < 0) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill overflows free qty by " + DmServer.vNull(pbReturnedFreeQty));
                }

                if (!pbDocId.equalsIgnoreCase("")) {
                    pbDocId += ",";
                }
                pbDocId += DmShared.iNull(rsTemp.getString("pbDocId")).toString();

                sqlQuery = "UPDATE pBillDetail SET"
                        + " isStatus = :isStatus,"
                        + " returnedQty = :returnedQty,"
                        + " returnedFreeQty = :returnedFreeQty"
                        + " WHERE docId = :pbDocId AND v_SNo = :pbV_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("pbDocId", DmShared.xNull(rsTemp.getString("pbDocId")));
                psTemp1.setString("pbV_SNo", DmShared.xNull(rsTemp.getString("pbV_SNo")));
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("returnedQty", DmServer.vNull(pbReturnedQty, "0.0000000"));
                psTemp1.setString("returnedFreeQty", DmServer.vNull(pbReturnedFreeQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!pbDocId.equalsIgnoreCase("")) {
                sqlQuery = "UPDATE pBill SET u_EntDt_LatestLine = :uEntDt where docId in (" + pbDocId + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    @Override
    public DmResult<String> processAutoVoucherAdjustment(String strKey, String supplierCode) {
        DmResult<String> dmrTemp = new DmResult();
        dmrTemp.setData(strKey);
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            processAutoVoucherAdjustment(strKey, supplierCode, cnTemp);

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);

            //       saveLog(strKey, null, null, DmEnum.DmTransactionMode.VOUCHER_ADJUSTMENT, null, null);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void processAutoVoucherAdjustment(String strKey, String supplierCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", csvPbDocIds = "";
        AdjustmentSave_Mdl adjSaveMdl = null;
        GrdAdjustment_Mdl gaMdl = null;
        double amtDr = 0, balanceAmt = 0, adjustmentAmt = 0;
        boolean flagAdjustment = false, flagPartialAdjustment = false, flagSinglePBill = false;
        HashMap<String, String> hmAdj = new HashMap<>();
        try {
            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(strKey, cnTemp);

            /*=======================================<< Save Adjustment >>=============================================*/
            sqlQuery = "SELECT STRING_AGG (CONVERT(NVARCHAR(max),vPbd.pbDocId), ',') AS csv \n"
                    + " FROM ( \n"
                    + "     SELECT DISTINCT pbd.pbDocId\n"
                    + "     FROM pBillDetail pbd \n"
                    + "     WHERE pbd.docId = :docId \n"
                    + "         AND pbd.pbDocId IS NOT NULL\n"
                    + " ) vPbd \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                csvPbDocIds = DmShared.xNull(rsTemp.getString("csv"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(csvPbDocIds).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "No any purchase bill found for adjustment!");
            } else {
                flagSinglePBill = csvPbDocIds.split(",").length == 1;
            }

            sqlQuery = "SELECT COUNT(*) AS cnt\n"
                    + "FROM pBillDetail pbd\n"
                    + "WHERE pbd.docId = :docId \n"
                    + "AND pbd.isFooter = 0 \n"
                    + "AND pbd.pbDocId IS NULL\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    flagSinglePBill = false;
                }
            }
            rsTemp.close();
            psTemp.close();

            if (flagSinglePBill == false) {
                throw new DmException(0, "Auto voucher adjustment can be processed, if return is done only for one purchase bill!");
            }

            sqlQuery = "SELECT TOP 1 lv.docId, lv.v_SNo, lv.v_Date, lv.subCode, lv.amtDr"
                    + " FROM ledger lv"
                    + " WHERE lv.docId = :docId"
                    + " AND lv.subCode = :subCode"
                    + " AND ISNULL(lv.amtDr, 0) > 0"
                    + " ORDER BY lv.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            psTemp.setString("subCode", DmShared.xNull(supplierCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                adjSaveMdl = new AdjustmentSave_Mdl();
                adjSaveMdl.setVr_DocId(DmShared.xNull(rsTemp.getString("docId")));
                adjSaveMdl.setVr_V_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                adjSaveMdl.setVr_V_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                adjSaveMdl.setVr_SubCode(DmShared.xNull(rsTemp.getString("subCode")));
                amtDr = DmShared.vNull(rsTemp.getString("amtDr"));
            }
            rsTemp.close();
            psTemp.close();

            if (adjSaveMdl == null) {
                return;
            }

            balanceAmt = DmShared.vNull(amtDr).doubleValue();
            for (String pbDocId : csvPbDocIds.split(",")) {
                adjustmentAmt = 0;
                if (DmShared.vNull(balanceAmt).doubleValue() > 0) {
                    sqlQuery = "SELECT TOP 1 lb.docId, lb.v_SNo, lb.v_Date, lb.subCode, lb.amtCr"
                            + " FROM ledger lb"
                            + " WHERE lb.docId = :docId"
                            + " AND lb.subCode = :subCode"
                            + " AND ISNULL(lb.amtCr, 0) > 0"
                            + " ORDER BY lb.v_SNo";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(pbDocId));
                    psTemp.setString("subCode", DmShared.xNull(supplierCode));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gaMdl = new GrdAdjustment_Mdl();
                        gaMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                        gaMdl.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                        gaMdl.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                        gaMdl.setAcCode(DmShared.xNull(rsTemp.getString("subCode")));
                        if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() > DmShared.vNull(balanceAmt).doubleValue()) {
                            adjustmentAmt = DmShared.vNull(balanceAmt).doubleValue();
                        } else {
                            adjustmentAmt = DmShared.vNull(rsTemp.getString("amtCr")).doubleValue();
                        }
                        balanceAmt -= adjustmentAmt;
                        gaMdl.setAdjustment(DmServer.vNull(adjustmentAmt, "0.00"));
                        gaMdl.setAdjustmentDrCr("Dr");
                        adjSaveMdl.getLstAdjustments().add(gaMdl);
                        flagAdjustment = true;
                    }
                    rsTemp.close();
                    psTemp.close();
                } else {
                    flagPartialAdjustment = true;
                }
            }

            if (flagPartialAdjustment) {
                throw new DmException(0000, "Unable to save adjustment for all bill.");
            } else {
                if (flagAdjustment) {
                    AccountAdjustment_Server.saveRecord(adjSaveMdl, cnTemp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DmException(0000, ex.getMessage());
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

    @Override
    public DmResult<List<OverHeadITS_Mdl>> getStockPostingLedgers(String itemCode
    ) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        List<OverHeadITS_Mdl> overHeadITSList = new ArrayList<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select im.subcode as acCode "
                    + " , sg.name as acName "
                    + " FROM itemMast im   "
                    + " LEFT JOIN subgroup sg on sg.subCode = im.subcode "
                    + " WHERE im.code = :itemCode ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dmrTemp.setValue1(DmShared.xNull(rsTemp.getString("acCode")));
                dmrTemp.setValue2(DmShared.xNull(rsTemp.getString("acName")));
            }
            dmrTemp.setData(overHeadITSList);
            dmrTemp.setStatus(true);

            psTemp.close();
            rsTemp.close();
            cnTemp.close();
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

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getLandedSkuRateForValidation(String ibCode, String itemCode
    ) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended keyMdl = new KeyFieldExtended();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select top 1 ib.batchNo,im.name,pb.recId,pbd.saleRate,pbd.skuSaleRate,ib.skuSaleRate,ib.saleRate,\n"
                    + "pbd.landedSkuRate\n"
                    + "from pBillDetail pbd \n"
                    + "left join pBill pb on pb.docid = pbd.docid\n"
                    + "left join itemMast im on im.code = pbd.itemCode\n"
                    + "left join itemBatch ib on ib.code = pbd.ibCode\n"
                    + "where im.code  = :itemCode\n"
                    + "and ib.code = :ibCode\n"
                    + "order by pb.v_Date desc";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            psTemp.setString("ibCode", DmShared.xNull(ibCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                keyMdl.setSharedKeyFieldCode(DmShared.vNull(rsTemp.getString("landedSkuRate")).toString());
            }
            dmrTemp.setData(keyMdl);
            dmrTemp.setStatus(true);

            psTemp.close();
            rsTemp.close();
            cnTemp.close();
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

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

}
