package aarogya.pharmacy.purchaseBillSearch.server;

import aarogya.pharmacy.purchaseBillSearch.shared.GrdBillDetails_Mdl;
import aarogya.pharmacy.purchaseBillSearch.shared.PurchaseBillSearch_Srv;
import aarogya.pharmacy.purchaseBillSearch.shared.SearchValue_Mdl;
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

public class PurchaseBillSearch_SrvImpl extends DmRSSPrivate implements PurchaseBillSearch_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    private String retBillFillQuery(String docId, String subCode, String v_Date, String vCat, String poDocids, boolean isOrderBy) {
        String sqlQuery, condition = "", table = "", join = "", pendingQty = "", pendingFreeQty = "";

        if (DmShared.xNull(vCat).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN) || DmShared.xNull(vCat).trim().equalsIgnoreCase(pvc.PHARMACY_PROFORMA_PURCHASE_RETURN)) {
            condition += " AND vt.category IN( '" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "')\n";
        } else if (DmShared.xNull(vCat).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED) || DmShared.xNull(vCat).trim().equalsIgnoreCase(pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED)) {
            condition += " AND vt.category IN( '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')\n";
        }
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

        condition += " AND ISNULL(pbd.isReturn, 0) = 0 \n";
        condition += " AND pb.v_date <= :v_Date \n";
        condition += " AND ISNULL(pb.approvedBy, '') <> '' \n";

        sqlQuery = "SELECT \n"
                + "   im.code AS itemCode \n"
                + " , im.name AS itemName \n"
                + " , imm.companyName AS itemMake \n"
                + " , pbd.hsnCode"
                + " , im.itsBasedOn"
                + " , im.isBestBefore \n"
                + " , im.propertyList"
                + " , im.itemGSTNature"
                + " , pb.v_Date AS billDate \n"
                + " , pb.docId AS billDocId \n"
                + " , pb.recId AS billRecId \n"
                + " , pbd.v_SNo AS itemVSno \n"
                + " , pb.partyBillNo AS refNo \n"
                + " , pb.partyBillDate AS refDate \n"
                + " , pb.v_Type AS v_Type \n"
                + " , ums.code AS skuCode \n"
                + " , ums.unit AS skuName \n"
                + " , ums.decimalPlaces AS skuDecimalPlaces \n"
                + " , um.code AS purchaseUnitCode \n"
                + " , um.unit AS purchaseUnitName \n"
                + " , um.decimalPlaces AS purcahseUnitDecimalPlaces \n"
                + " , im.purchaseUnitConversionRate\n"
                + " , um.gstUnitCode \n" + pendingQty + pendingFreeQty
                + " , pbd.rate\n"
                + " , pbd.mrp\n"
                + " , pbd.skuMrp\n"
                + " , pbd.saleRate\n"
                + " , pbd.skuSaleRate\n"
                + " , pbd.skuRate \n"
                + " , isNull(pbd.oh_Amt_Gross,0) / (isNull(pbd.acceptQty,0) + isNull(pbd.freeQty,0))  as purchaseRate \n"
                + " , pbd.batchNo\n"
                + " , pbd.expiryDate\n"
                + " , pbd.manufacturingDate\n"
                + " , pbd.remark\n"
                + " , pbd.ibCode \n"
                + " FROM pBill pb \n"
                + " LEFT JOIN pBillDetail pbd ON pb.docId = pbd.docId \n"
                + " LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type \n" + join
                + " LEFT JOIN itemMast im ON pbd.itemCode = im.code \n"
                + " LEFT JOIN itemMakeMast  imm ON imm.code =im.itemMakeCode \n"
                + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                + " LEFT JOIN unitMast um ON pbd.unitCode = um.code \n"
                + " LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                + " WHERE  pb.v_Date IS NOT NULL \n"
                + " AND ISNULL(pbd.isFooter, 0) = 0 \n"
                + " AND pb.site_Code =:site_Code \n"
                + " AND pb.subCode = :subCode \n"
                + condition;

        return sqlQuery;
    }

    @Override
    public DmResultPageing<List<GrdBillDetails_Mdl>> getBillDetail(String docId, String subCode, String v_Date, String vCat, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdBillDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdBillDetails_Mdl> lstTemp = new ArrayList<GrdBillDetails_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(retBillFillQuery(docId, subCode, v_Date, vCat, "", false));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdBillDetails_Mdl objListTemp = new GrdBillDetails_Mdl();

                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("billDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("billRecId")));
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                // objListTemp.setVoucherSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                objListTemp.setBillDate(DmShared.dNull(rsTemp.getDate("billDate")));
                objListTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                objListTemp.setItemMake(DmShared.xNull(rsTemp.getString("itemMake")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setItemVSno(DmShared.xNull(rsTemp.getString("itemVSno")));

                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                objListTemp.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                objListTemp.setRate(DmServer.vNull(rsTemp.getString("rate")));
                objListTemp.setMrp(DmServer.vNull(rsTemp.getString("mrp")));
                objListTemp.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                objListTemp.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                objListTemp.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillListSearch(String docId, String subCode, String v_Date, String vCat, DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<List<GrdBillDetails_Mdl>> getAutoBillDetail(String pbDocId, String vDate) {
        DmResult<List<GrdBillDetails_Mdl>> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdBillDetails_Mdl> lstTemp = new ArrayList<GrdBillDetails_Mdl>();

        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, retBillFillQuery("", "", vDate, "", pbDocId, true));

            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.dNull(vDate).equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(vDate));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdBillDetails_Mdl objListTemp = new GrdBillDetails_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("orderDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("orderNo")));
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                objListTemp.setItemMake(DmShared.xNull(rsTemp.getString("itemMake")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                objListTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));

                objListTemp.setIsChecked("1");
                lstTemp.add(objListTemp);
            }

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
