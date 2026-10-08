package aarogya.pharmacy.orderSearch.server;

import aarogya.pharmacy.orderSearch.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderSearch_SrvImpl extends DmRSSPrivate implements OrderSearch_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    private String retOredrFillQuery(String docId, String subCode, String v_Date, String vCat, String poDocids, boolean isOrderBy) {
        String sqlQuery, condition = "", table = "", pendingQty = "";
        if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_CHALLAN)) {
            table = " pChallanDetail ";
            condition += " AND ISNULL(im.isBilledItem, 0) = 0 ";
        } else if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL_DIRECT)) {
            table = " pBillDetail ";
        }
        if (DmShared.iNull(subCode).longValue() > 0) {
            condition += " AND po.subCode = :subCode";
        }
        if (!DmShared.xNull(poDocids).equalsIgnoreCase("")) {
            condition += " AND po.docId in (" + poDocids + ")";
        }

        if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
            pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(tb.acceptQty,0) AS pendingQty";
            condition += " AND (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
        } else {
            pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) AS pendingQty";
            condition += " AND ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) > 0";
            condition += " AND pod.isStatus = 0";
        }

        if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
            condition += " AND po.v_Date <= :v_Date";
        } else {
            condition += " AND 1 = 2";
        }

        sqlQuery = "SELECT  pod.docId as orderDocId, pod.v_SNo, po.recId as orderNo, pod.challanedQty as grnQty, po.v_Date as orderDate, "
                + "         pod.itemCode, pod.rate, im.name as itemName, pod.quantity, po.refNo, pod.requiredDate, um.unit as unitName, pod.remark,"
                + "         pod.isStatus, po.approvedBy " + pendingQty
                + " FROM pOrderDetail pod"
                + " LEFT JOIN pOrder po ON pod.docId = po.docId";
        if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
            sqlQuery += " LEFT JOIN ("
                    + "     SELECT tb.*"
                    + "     FROM " + table + " tb"
                    + "     WHERE tb.docid = :docId"
                    + " ) AS tb ON pod.docId = tb.poDocId AND pod.v_SNO = tb.poV_SNo";
        }
        sqlQuery += " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
                + " LEFT JOIN itemMast im ON pod.itemCode = im.code"
                + " LEFT JOIN unitMast um ON pod.unitCode = um.code"
                + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'"
                + " AND po.site_Code = :site_Code"
                + " AND isNull(po.approvedBy,'') <> ''"
                + condition;

        return sqlQuery;
    }

    @Override
    public DmResultPageing<List<GrdOrdertDetails_Mdl>> getOrderDetail(String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdOrdertDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
//        String sqlQuery, condition = "", table = "", pendingQty = "";
        List<GrdOrdertDetails_Mdl> lstTemp = new ArrayList<GrdOrdertDetails_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_CHALLAN)) {
//                table = " pChallanDetail ";
//                condition += " AND ISNULL(im.isBilledItem, 0) = 0 ";
//            } else if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL_DIRECT)) {
//                table = " pBillDetail ";
//            }
//
//            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
//                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(tb.acceptQty,0) AS pendingQty";
//                condition += " AND (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
//            } else {
//                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) AS pendingQty";
//                condition += " AND ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) > 0";
//                condition += " AND pod.isStatus = 0";
//            }
//
//            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
//                condition += " AND po.v_Date <= :v_Date";
//            } else {
//                condition += " AND 1 = 2";
//            }
//
//            sqlQuery = "SELECT  pod.docId as orderDocId, pod.v_SNo, po.recId as orderNo, pod.challanedQty as grnQty, po.v_Date as orderDate, "
//                    + "         pod.itemCode, pod.rate, im.name as itemName, pod.quantity, po.refNo, pod.requiredDate, um.unit as unitName, pod.remark,"
//                    + "         pod.isStatus, po.approvedBy " + pendingQty
//                    + " FROM pOrderDetail pod"
//                    + " LEFT JOIN pOrder po ON pod.docId = po.docId";
//            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
//                sqlQuery += " LEFT JOIN ("
//                        + "     SELECT tb.*"
//                        + "     FROM " + table + " tb"
//                        + "     WHERE tb.docid = :docId"
//                        + " ) AS tb ON pod.docId = tb.poDocId AND pod.v_SNO = tb.poV_SNo";
//            }
//            sqlQuery += " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
//                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code"
//                    + " LEFT JOIN unitMast um ON pod.unitCode = um.code"
//                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'"
//                    + " AND po.site_Code = :site_Code"
//                    + " AND isNull(po.approvedBy,'') <> ''"
//                    + " AND po.subCode = :subCode"
//                    + condition;
//            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setSQL(retOredrFillQuery(docId, subCode, v_Date, vCat, "", false));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdOrdertDetails_Mdl objListTemp = new GrdOrdertDetails_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("orderDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("orderNo")));
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                objListTemp.setVoucherSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                objListTemp.setOrderDate(DmShared.dNull(rsTemp.getDate("orderDate")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                objListTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                objListTemp.setOrderQty(DmServer.vNull(rsTemp.getString("quantity")));
                objListTemp.setRequiredDate(DmShared.dNull(rsTemp.getDate("requiredDate")));
                objListTemp.setIsChecked("0");
                lstTemp.add(objListTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrderListSearch(String docId, String subCode, String v_Date, String vCat, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", havingCondition = "", table = "", pendingQty = "";
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        KeyFieldExtended listNode;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_CHALLAN)) {
                table = " pChallanDetail ";
            } else if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL_DIRECT)) {
                table = " pBillDetail ";
            }

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(Sum(pod.quantity),0) - ISNULL(Sum(pod.challanedQty),0)+ Max(ISNULL(tb.acceptQty,0)) AS pendingQty ";
                havingCondition += " Having (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(Sum(pod.quantity),0) - ISNULL(Sum(pod.challanedQty),0) + ISNULL(Sum(pcd.acceptQty),0)) > 0) THEN 1 ELSE 0 END) = 1";
            } else {
                pendingQty = " , ISNULL(Sum(pod.quantity),0) - ISNULL(Sum(pod.challanedQty),0) AS pendingQty";
                havingCondition += " Having ISNULL(Sum(pod.quantity),0) - ISNULL(Sum(pod.challanedQty),0) > 0";
                condition += " AND pod.isStatus = 0";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND po.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT  pod.docId as orderDocId, Max(po.recId) as orderNo, Sum(pod.challanedQty) as grnQty, Max(po.v_Date) as orderDate, "
                    + "         Sum(pod.quantity) as quantity " + pendingQty
                    + " FROM pOrderDetail pod"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT tb.docid,ISNULL(Sum(tb.acceptQty),0) as acceptQty "
                        + "     FROM " + table + " tb"
                        + "     WHERE tb.docid = :docId"
                        + " Group By tb.DocID) AS tb ON pod.docId = tb.poDocId ";
            }
            sqlQuery += " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type "
                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'"
                    + " AND po.site_Code = :site_Code"
                    + " AND isNull(po.approvedBy,'') <> ''"
                    + " AND po.subCode = :subCode"
                    + condition
                    + " Group By pod.docId  "
                    + havingCondition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("orderDocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("orderNo")));
                listNode.setSharedModifiedBy(DmShared.dNull(rsTemp.getString("orderDate")));
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
    public DmResult<List<GrdOrdertDetails_Mdl>> getAutoOrderDetail(String poDocIds, String vDate) {
        DmResult<List<GrdOrdertDetails_Mdl>> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
//        String sqlQuery, condition = "", table = "", pendingQty = "";
        List<GrdOrdertDetails_Mdl> lstTemp = new ArrayList<GrdOrdertDetails_Mdl>();

        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, retOredrFillQuery("", "", vDate, "", poDocIds, true));

            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
//            psTemp.setString("subCode", DmShared.iNull(subCode).toString());
            
            if (!DmShared.dNull(vDate).equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(vDate));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdOrdertDetails_Mdl objListTemp = new GrdOrdertDetails_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("orderDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("orderNo")));
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                objListTemp.setVoucherSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                objListTemp.setOrderDate(DmShared.dNull(rsTemp.getDate("orderDate")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                objListTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                objListTemp.setOrderQty(DmServer.vNull(rsTemp.getString("quantity")));
                objListTemp.setRequiredDate(DmShared.dNull(rsTemp.getDate("requiredDate")));
                objListTemp.setIsChecked("1");
                lstTemp.add(objListTemp);
            }
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
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
}
