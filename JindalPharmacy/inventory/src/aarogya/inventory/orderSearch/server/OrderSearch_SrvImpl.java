package aarogya.inventory.orderSearch.server;

import aarogya.inventory.orderSearch.shared.GrdOrdertDetails_Mdl;
import aarogya.inventory.orderSearch.shared.SearchValue_Mdl;
import aarogya.inventory.orderSearch.shared.OrderSearch_Srv;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
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

    @Override
    public DmResultPageing<List<GrdOrdertDetails_Mdl>> getOrderDetail(String v_Type, String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdOrdertDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", condVoucherCat = "", table = "", pendingQty = "", itemCatFilteration = "",singlePOFlowInPurchase="";
        List<GrdOrdertDetails_Mdl> lstTemp = new ArrayList<GrdOrdertDetails_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PURCHASE_CHALLAN)) {
                table = " pChallanDetail ";
                condition += " AND ISNULL(im.isBilledItem, 0) = 0 ";
            } else if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT)) {
                table = " pBillDetail ";
            }
            if (DmShared.xNull(vCat).equalsIgnoreCase(pvc.SERVICE_BILL)
                    || DmShared.xNull(vCat).equalsIgnoreCase(pvc.SERVICE_BILL_UNREGISTERED)
                    || DmShared.xNull(vCat).equalsIgnoreCase(pvc.SERVICE_CHALLAN)
                    || DmShared.xNull(vCat).equalsIgnoreCase(pvc.SERVICE_CHALLAN_UNREGISTERED)) {
                condVoucherCat += " and vt.category IN ('" + pvc.SERVICE_ORDER + "', '" + pvc.SERVICE_ORDER_UNREGISTERED + "')";
            } else {
                condVoucherCat += " and vt.category IN ('" + pvc.PURCHASE_ORDER + "', '" + pvc.PURCHASE_ORDER_UNREGISTERED + "')";
            }

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0) AS pendingQty";
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

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
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
                    + " WHERE po.site_Code = :site_Code"
                    + condVoucherCat
                    + " AND isNull(pod.isFooter,0) = 0 "
                    + " AND isNull(po.approvedBy,'') <> ''"
                    + " AND po.subCode = :subCode"
                    + condition;
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

}
