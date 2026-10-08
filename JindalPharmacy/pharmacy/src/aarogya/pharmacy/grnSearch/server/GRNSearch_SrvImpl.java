package aarogya.pharmacy.grnSearch.server;

import aarogya.pharmacy.grnSearch.shared.*;
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

public class GRNSearch_SrvImpl extends DmRSSPrivate implements GRNSearch_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<GrdGrnDetails_Mdl>> getItemDetail(String docId, String subCode, String v_Date,Boolean singlePOFlowInPurchase, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdGrnDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", pendingQty = "", pendingFreeQty = "",conditionHaving = "",strGroupBy="";
        List<GrdGrnDetails_Mdl> lstTemp = new ArrayList<GrdGrnDetails_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , sum(ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) AS pendingQty";
                pendingFreeQty = " , sum(ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0)) AS pendingFreeQty";
                conditionHaving += " having max((CASE"
                        + "             WHEN pcd.isStatus = 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) > 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0)) > 0 THEN 1"
                        + "             ELSE 0 END)) = 1";
            } else {
                pendingQty = " , sum(ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0)) AS pendingQty";
                pendingFreeQty = " , sum(ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0)) AS pendingFreeQty";
                conditionHaving += " having max((CASE"
                        + "             WHEN pcd.isStatus = 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0)) > 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0)) > 0 THEN 1"
                        + "             ELSE 0 END)) = 1";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pc.pChallanDate <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            
            if (!singlePOFlowInPurchase || !DmShared.xNull(docId).equalsIgnoreCase("")){
                strGroupBy=" group by pc.docId,pcd.v_SNo ";
            }else{
                strGroupBy=" group by pc.docId,po.docId ";
            }
            /*==============================<< Get Grn detail (via list) >>===============================*/

            sqlQuery = "SELECT"
                    + "   max(pc.docId) AS pcDocId"
                    + " , max(pc.recId) AS pcRecId"
                    + " , max(pc.v_Date) AS pcV_Date"
                    + " , max(pcd.v_SNo) AS pcV_Sno"
                    + " , max(im.name) AS itemName"
                    + " , max(um.unit) AS unitName"
                    + " , max(pcd.remark) as remark"
                    + " , sum(pcd.acceptQty) as acceptQty " + pendingQty + pendingFreeQty
                    + " , max(po.recId) as poNo,max(po.docid) as poDocid,max(po.v_Date) as poDate "
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN pOrder po ON pcd.poDocId = po.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.*"
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + " ) AS pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNO = pbd.pcV_SNo";
            }
            sqlQuery += " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + "	WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + " AND pc.site_Code = :site_Code"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND pc.subCode = :subCode"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + condition
                    + strGroupBy
                    + conditionHaving;
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
                GrdGrnDetails_Mdl objListTemp = new GrdGrnDetails_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("pcDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pcRecId")));
                objListTemp.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));
                objListTemp.setPcV_Date(DmShared.dNull(rsTemp.getDate("pcV_Date")));
                objListTemp.setPcQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                objListTemp.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                objListTemp.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                objListTemp.setMedicineName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setPoDocID(DmShared.xNull(rsTemp.getString("poDocid")));
                objListTemp.setPoNo(DmShared.xNull(rsTemp.getString("poNo")));
                objListTemp.setPoV_Date(DmShared.dNull(rsTemp.getDate("poDate")));
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
