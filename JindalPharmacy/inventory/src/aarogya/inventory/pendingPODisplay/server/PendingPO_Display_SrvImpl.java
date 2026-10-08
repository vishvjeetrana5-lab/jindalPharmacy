package aarogya.inventory.pendingPODisplay.server;

import aarogya.inventory.pendingPODisplay.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PendingPO_Display_SrvImpl extends DmRSSPrivate implements PendingPO_Display_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.PURCHASE_ORDER + "','" + pvc.SERVICE_ORDER + "'";

    @Override
    public DmResultPageing<List<GrdPendingPO_Display_Mdl>> getFillRecords(PendingPO_Dispaly_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdPendingPO_Display_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPendingPO_Display_Mdl> lstTemp = new ArrayList<>();
        GrdPendingPO_Display_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "", condition2="";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  po.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  po.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getSuppleirCode()).equalsIgnoreCase("")) {
                condition += " AND po.subCode IN(" + DmServer.getParamList(":subCode", param.getSuppleirCode()) + " )";
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                condition += " AND pod.itemCode IN(" + DmServer.getParamList(":itemCode", param.getItemCode()) + " )";
            }
            if (!DmShared.xNull(param.getPurchaseDocIds()).equalsIgnoreCase("")) {
                condition += " AND po.docId IN(" + DmServer.getParamList(":docId", param.getPurchaseDocIds()) + " )";
            }
            if (!DmShared.dNull(param.getRequiredDate()).equalsIgnoreCase("")) {
                condition2 += " where pod.requiredDate<= :requiredDate ";
            }
            condition += " AND po.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";

            sqlQuery = "SELECT "
                    + "   po.docId,max(po.recId) as recId,max(po.v_Date) as v_Date,max(po.refNo) as refNo, \n"
                    + "   max(po.RefDate) as RefDate,max(po.u_Name) as u_Name,max(po.preparedBy) as preparedBy, \n"
                    + "   max(po.v_Type) as v_Type,max(pod.itemCode) as itemCode, \n"
                    + "   max(po.currencyCode) as currencyCode,max(cr.name) as name, \n"
                    + "   max(vt.description) as description,max(po.approvedBy) as approvedBy, \n"
                    + "   max(format(po.approvalDateTime,'dd-MMM-yyyy HH.mm')) as approvalDateTime, \n"
                    + "   max(po.wtaCode) as wtaCode,max(po.subCode) AS supplierCode,max(sgv.displayName) AS supplierName, \n"
                    + "   max(po.oh_Amt_Net) as poValue,isNull(max(sgv.eMail),'') as eMail \n"
                    + "   FROM pOrder AS po \n"
                    + "   LEFT JOIN pOrderDetail AS pod ON po.docID = pod.docID \n"
                    + "   LEFT JOIN \n"
                    + "             (\n"
                    + "             select docid,sum(case when pod.isStatus=0 then 1 else 0 end) as status\n"
                    + "             	from pOrderDetail pod\n"+condition2
                    + "             	group by docId\n"
                    + "             ) as pod1 on po.docId=pod1.docID\n"
                    + "   LEFT JOIN voucher_Type AS vt ON po.v_Type = vt.v_Type \n"
                    + "   LEFT JOIN currency AS cr ON cr.code=po.currencyCode \n"
                    + "   LEFT JOIN subGroup AS sgv ON po.subCode = sgv.subCode \n" 
                    + " where vt.category in (" + vCat + ") and pod1.status>0 and po.approvedBy<>''" + condition
                    + " group by po.docId ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getSuppleirCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(param.getSuppleirCode()));
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(param.getItemCode()));
            }
            if (!DmShared.xNull(param.getPurchaseDocIds()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(param.getPurchaseDocIds()));
            }
            if (!DmShared.dNull(param.getRequiredDate()).equalsIgnoreCase("")) {
                psTemp.setDate("requiredDate", DmServer.convertDate(param.getRequiredDate()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdPendingPO_Display_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setPurchaseDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                listNode.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                listNode.setCurrentUser(DmServer.getSsnUserName(this));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setSupplierEMail(DmShared.xNull(rsTemp.getString("eMail")));

                listNode.setIsChecked("0");

                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setIsStatus("0");
                listNode.setPurchaseOrderValue(DmServer.vNull(rsTemp.getString("poValue"), "0.00"));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

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
    public DmResult<PendingPO_Dispaly_Mdl> fillItemGrid(String code) {
        DmResult<PendingPO_Dispaly_Mdl> dmrTemp = new DmResult<>();
        GrdMedicineDetail_Mdl grdItemDetail_Mdl;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<>();
        PendingPO_Dispaly_Mdl mrTemp = new PendingPO_Dispaly_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp1 = null;
        ResultSet rsTemp1 = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT "
                    + " po.docId,po.recId,po.v_Date,cr.name, po.approvedBy,\n"
                    + " FORMAT(po.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,po.wtaCode,\n"
                    + " po.subCode AS supplierCode,sgv.displayName AS supplierName, q.recId AS quotRecId\n"
                    + " FROM pOrder AS po \n"
                    + " LEFT JOIN voucher_Type AS vt ON po.v_Type = vt.v_Type  \n"
                    + " LEFT JOIN currency AS cr ON cr.code=po.currencyCode  \n"
                    + " LEFT JOIN subGroup AS sgv ON po.subCode = sgv.subCode  \n"
                    + " LEFT JOIN (\n"
                    + "         SELECT pod.docId, pod.quotationDocId\n"
                    + "         FROM pOrderDetail pod\n"
                    + "         WHERE pod.quotationDocId IS NOT NULL\n"
                    + "         GROUP BY pod.docId, pod.quotationDocId\n"
                    + " ) pod ON po.docId = pod.docId\n"
                    + " LEFT JOIN quotation AS q ON q.DocId= pod.quotationDocId\n"
                    + " where po.docId=:docId And vt.category in (" + vCat + ") ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            if (rsTemp1.next()) {
                mrTemp = new PendingPO_Dispaly_Mdl();
                mrTemp.setPurchaseDocIds(DmShared.xNull(rsTemp1.getString("docId")));
                mrTemp.setPurchaseReccIds(DmShared.xNull(rsTemp1.getString("recId")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp1.getString("wtaCode")));
                mrTemp.setPurchaseDate(DmShared.dNull(rsTemp1.getDate("v_Date")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp1.getString("approvedBy")));
                mrTemp.setQuotationNo(DmShared.xNull(rsTemp1.getString("quotRecId")));
                mrTemp.setApprovedDateTime(DmShared.xNull(rsTemp1.getString("approvalDateTime")));
                mrTemp.setSuppleirCode(DmShared.xNull(rsTemp1.getString("supplierCode")));
                mrTemp.setSuppleirName(DmShared.xNull(rsTemp1.getString("supplierName")));
                mrTemp.setCurrency(DmShared.xNull(rsTemp1.getString("name")));

            }

            sqlQuery = "select pod.docId, pod.itemCode, im.name as itemName, pod.quantity, pod.unitCode, um.unit as unitName, pod.requiredDate, pod.remark,  "
                    + " pod.rate, pod.oh_Amt_Net as amount, pod.v_SNo,pod.isStatus,pod.challanedQty "
                    + " from pOrderDetail as pod "
                    + " left join itemMast as im on pod.itemCode = im.code "
                    + " left join unitMast as um on pod.unitCode = um.code  "
                    + " where docId=:docId order by itemName Asc ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            while (rsTemp1.next()) {
                grdItemDetail_Mdl = new GrdMedicineDetail_Mdl();
                grdItemDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("itemCode")));
                grdItemDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("itemName")));
                grdItemDetail_Mdl.setvSNo(DmShared.xNull(rsTemp1.getString("v_SNo")));
                grdItemDetail_Mdl.setAmount(DmShared.xNull(rsTemp1.getString("amount")));
                grdItemDetail_Mdl.setOrderQty(DmServer.vNull(rsTemp1.getString("quantity"), ""));
                grdItemDetail_Mdl.setOrderDate(DmShared.dNull(rsTemp1.getDate("requiredDate")));
                grdItemDetail_Mdl.setUnit(DmShared.xNull(rsTemp1.getString("unitName")));
                grdItemDetail_Mdl.setRemark(DmShared.xNull(rsTemp1.getString("remark")));
                grdItemDetail_Mdl.setRate(DmServer.vNull(rsTemp1.getString("rate"), "0.00"));
                grdItemDetail_Mdl.setIsStatus(DmShared.xNull(rsTemp1.getString("isStatus")));
                grdItemDetail_Mdl.setIsChecked(DmShared.xNull(rsTemp1.getString("isStatus")));
                grdItemDetail_Mdl.setChallanQty(DmServer.vNull(rsTemp1.getString("challanedQty"), ""));
                listNode.add(grdItemDetail_Mdl);
            }
            mrTemp.getGrdMedicineDetail_Mdl().addAll(listNode);
            psTemp1.close();
            rsTemp1.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (UnsupportedOperationException | SQLException e) {
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

    @Override
    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            sqlQuery = "select approvedBy from pOrder where docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase(DmServer.getSsnUserName(this))) {
                        throw new DmException(0, "Record is not approved by this user. Please refresh data.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " select Count(isStatus) as cnt from pOrderDetail where docId = :docId AND v_SNo=:vSNo AND isStatus = 2";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("vSNo", DmShared.iNull(vSNo).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                    throw new DmException(0, "Record is completed,Please refresh record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (isOpen) {

                sqlQuery = " Update pOrderDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(vSNo).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();

            } else {
                sqlQuery = " select Count(isStatus) as cnt from pOrderDetail where docId = :docId AND v_SNo=:vSNo AND isStatus = 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(vSNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " UPDATE pOrderDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(vSNo).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update pOrder set "
                    + " u_EntDt_LatestLine=:uEntDt "
                    + " where docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResultPageing<List<TxtHPurchaseOrder_Mdl>> getTxtHPurchaseOrder(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPurchaseOrder_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPurchaseOrder_Mdl> lstTemp = new ArrayList<>();
        TxtHPurchaseOrder_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  "
                    + " po.docId, po.recId as pOrderNo, po.v_Date as pOrderDate, vt.category "
                    + " FROM pOrder AS po  "
                    + " left join voucher_Type as vt on vt.v_Type = po.v_Type "
                    + " where po.site_Code= '" + DmServer.getSsnSiteCode(this) + "' "
                    + " and  vt.Category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHPurchaseOrder_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pOrderNo")));
                listNode.setPoDate(DmShared.dNull(rsTemp.getDate("pOrderDate")));
                listNode.setPoType(DmShared.xNull(rsTemp.getString("category")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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

    public List<GrdPendingPODetail_Display_Mdl> getRecords(String docId, String vSNo) {
        List<GrdPendingPODetail_Display_Mdl> listTemp = new ArrayList<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        GrdPendingPODetail_Display_Mdl lstNode;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  ind.docID as code,indd.v_SNo,ind.recId as indentName,cast(indd.aprQty as decimal(10,0)) aprQty\n"
                    + "	from pOrderIndentDetail poi\n"
                    + "	left join indentDetail indd on poi.indentDocId=indd.DocId and poi.indentV_SNo=indd.v_SNo\n"
                    + "	left join indent ind on indd.docid=ind.docId\n"
                    + "	where poi.docId =:docId and poi.v_SNo =:vSNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("vSNo", DmShared.iNull(vSNo).toString());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNode = new GrdPendingPODetail_Display_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                lstNode.setIndentName(DmShared.xNull(rsTemp.getString("indentName")));
                lstNode.setApprovedQty(DmShared.xNull(rsTemp.getString("aprQty")));
                lstNode.setIndentV_Sno(DmShared.xNull(rsTemp.getString("v_SNo")));

                listTemp.add(lstNode);

            }

            psTemp.close();

            cnTemp.close();

        } catch (Exception e) {

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

        return listTemp;
    }

    @Override
    public DmResult<String> sendMail(String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        String v_Type = "";
        dmrTemp.setData("");
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            sqlQuery = "select v_Type from voucher_Type where category='PPOML' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                v_Type = DmShared.xNull(rsTemp.getString("v_Type"));
                STServer.sendMail(DmShared.iNull(docId).toString(), v_Type, this);
            }
            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(docId);
            spBegin.commit();
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , im.manualCode"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " WHERE im.IsActive = 1"
                    + " AND (im.filterItemType = :filterItemType or im.itemType = :itemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND im.isQuotationMandatory = 0 and im.itemType in('SM','SV')");

            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
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
    //</editor-fold>

    @Override
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<TxtHSupplier_Mdl>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT distinct sg.subCode as code, sg.name, sg.manualCode, c.cityName\n"
                    + " FROM subGroup sg\n"
                    + " LEFT JOIN pOrder po on sg.subCode=po.subCode\n"
                    + " LEFT JOIN city c on sg.cityCode=c.cityCode\n"
                    + " LEFT JOIN subGroupSiteList sl on sg.subCode=sl.subCode\n"
                    + " WHERE sg.IsActive = 1\n"
                    + " AND sl.site_Code = :site_Code");

            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSuppCity(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
