package aarogya.inventory.grnSearch.server;

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
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.grnSearch.shared.GrdGrnDetails_Mdl;
import aarogya.inventory.grnSearch.shared.GrnSearch_Srv;
import aarogya.inventory.grnSearch.shared.SearchValue_Mdl;

public class GrnSearch_SrvImpl extends DmRSSPrivate implements GrnSearch_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory(); 

    @Override
    public DmResultPageing<List<GrdGrnDetails_Mdl>> getItemDetail(String v_Type, String docId, String subCode, String v_Date,Boolean singlePOFlowInPurchase, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdGrnDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "",conditionHaving = "", pendingQty = "", itemCatFilteration = "",strGroupBy="";
        List<GrdGrnDetails_Mdl> lstTemp = new ArrayList<GrdGrnDetails_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            
            String voucherCategory=DmServer.getCodeViaManualCode(v_Type, "v_Type", "voucher_Type", "category", cnTemp);
            
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , sum(ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) AS pendingQty";
                conditionHaving += " having (CASE WHEN (pcd.isStatus = 0) OR (pcd.isStatus = 2 AND sum((ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0))) > 0) THEN 1 ELSE 0 END) = 1";
            } else {
                pendingQty = " , sum(ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0)) AS pendingQty";
                conditionHaving += " having sum(ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0)) > 0";
                condition += " AND pcd.isStatus = 0";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pc.pChallanDate <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }
            
            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_BILL) 
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_BILL_UNREGISTERED)){
                condition += " and vt.category IN ('" + pvc.SERVICE_CHALLAN + "', '" + pvc.SERVICE_CHALLAN_UNREGISTERED + "') ";
            }else {
                condition += " and vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "') ";
            }
            /*==============================<< Get Grn detail (via list) >>===============================*/

            if (!singlePOFlowInPurchase || !DmShared.xNull(docId).equalsIgnoreCase("")){
                strGroupBy=" group by pc.docId,pcd.v_SNo ";
            }else{
                strGroupBy=" group by pc.docId,po.docId ";
            }
                
            
            sqlQuery = "SELECT"
                    + "   pc.docId AS pcDocId"
                    + " , max(pc.recId) AS pcRecId"
                    + " , max(pc.v_Date) AS pcV_Date"
                    + " , max(pcd.v_SNo) AS pcV_Sno"
                    + " , max(im.name) AS itemName"
                    + " , max(um.unit) AS unitName"
                    + " , max(pcd.remark) as remark"
                    + " , sum(pcd.acceptQty) as acceptQty " + pendingQty
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
                    + " WHERE pc.site_Code = :site_Code"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND pc.subCode = :subCode"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + condition
                    +strGroupBy
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
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
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
