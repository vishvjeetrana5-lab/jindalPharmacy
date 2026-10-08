package aarogya.inventory.purchaseOrderApproval.server;

import aarogya.inventory.purchaseOrderApproval.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderApproval_SrvImpl extends DmRSSPrivate implements PurchaseApproval_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','" + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "'";

    @Override
    public DmResultPageing<List<GrdPurchaseOrderApproval_Mdl>> getFillRecords(PurchaseOrderApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdPurchaseOrderApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPurchaseOrderApproval_Mdl> lstTemp = new ArrayList<>();
        GrdPurchaseOrderApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "", join = "";

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

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("C")) {
                condition += " AND isNull(pod.isStatus,0)= 1 ";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(po.approvedBy,'') = '' AND isNull(pod.isStatus,0) = 0";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = po.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(po.approvedBy,'') <> '' AND isNull(pod.isStatus,0) = 0";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("Com")) {
                condition += "  AND   isNull(pod.isStatus,0) = 2";
            }

            condition += " AND po.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";

            sqlQuery = "SELECT "
                    + "   po.docId,max(po.recId) as recId,max(po.v_Date) as v_Date,max(po.refNo) as refNo, \n"
                    + "   max(po.RefDate) as RefDate,max(po.u_Name) as u_Name,max(po.preparedBy) as preparedBy, \n"
                    + "   max(po.v_Type) as v_Type,max(pod.itemCode) as itemCode, max(im.name) as itemName, \n"
                    + "   max(po.currencyCode) as currencyCode,max(cr.name) as name,max(pod.quantity) AS orderQty, \n"
                    + "   max(um.unit) as unitName, max(pod.requiredDate) AS requiredDate, max(pod.remark) AS remark,  max(pod.rate) as rate, \n"
                    + "   max(pod.v_SNo) as v_SNo,max(vt.description) as description,max(po.approvedBy) as approvedBy, \n"
                    + "   max(format(po.approvalDateTime,'dd-MMM-yyyy HH.mm')) as approvalDateTime,max(pod.isStatus) as isStatus, \n"
                    + "   max(po.wtaCode) as wtaCode,max(po.subCode) AS supplierCode,max(sgv.displayName) AS supplierName, \n"
                    + "   max(q.recId) AS quotRecId,max(ig.name) AS itemGroup,max(pod.colourCode) as colourCode, \n"
                    + "   max(icm.name) as colourName, max(pod.sizeCode) as sizeCode,max(ism.name) sizeName, \n"
                    + "   max(im.propertyList) as propertyList,max(po.oh_Amt_Net) as poValue \n"   
                    + "   FROM pOrder AS po \n"
                    + "   LEFT JOIN pOrderDetail AS pod ON pod.docId = po.docId \n"
                    + "   left join itemMast as im on pod.itemCode = im.code \n"
                    + "   LEFT JOIN itemGroup AS ig ON ig.code=im.itemGroup \n"
                    + "   left join unitMast as um on pod.unitCode = um.code \n"
                    + "   LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode \n"
                    + "   LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode \n"
                    + "   LEFT JOIN voucher_Type AS vt ON po.v_Type = vt.v_Type \n"
                    + "   LEFT JOIN currency AS cr ON cr.code=po.currencyCode \n"
                    + "   LEFT JOIN subGroup AS sgv ON po.subCode = sgv.subCode \n"
                    + "   LEFT JOIN quotation AS q ON q.DocId = pod.quotationDocId \n" + join
                    + " where vt.category in (" + vCat + ") " + condition
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
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdPurchaseOrderApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setPurchaseDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                listNode.setOrderQty(DmServer.vNull(rsTemp.getString("orderQty")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("requiredDate")));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setQuotationNo(DmShared.xNull(rsTemp.getString("quotRecId")));
                listNode.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                listNode.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                listNode.setCurrentUser(DmServer.getSsnUserName(this));

                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));

                listNode.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                listNode.setColor(DmShared.xNull(rsTemp.getString("colourName")));

                listNode.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setPurchaseOrderValue(DmServer.vNull(rsTemp.getString("poValue"), "0.00"));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourName(listNode.getColor());
                ipm.setSizeName(listNode.getSize());
                listNode.setPropertiesCsv(DmShared.xNull(ipm.getPropertiesCsv()));

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
    public DmResult<PurchaseOrderApproval_Mdl> fillItemGrid(String code) {
        DmResult<PurchaseOrderApproval_Mdl> dmrTemp = new DmResult<>();
        GrdItemDetail_Mdl grdItemDetail_Mdl;
        List<GrdItemDetail_Mdl> listNode = new ArrayList<>();
        PurchaseOrderApproval_Mdl mrTemp = new PurchaseOrderApproval_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp1 = null;
        ResultSet rsTemp1 = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT "
                    + " po.docId,po.recId,po.v_Date,cr.name, po.approvedBy,"
                    + " FORMAT(po.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,po.wtaCode,"
                    + " po.subCode AS supplierCode,sgv.displayName AS supplierName, q.recId AS quotRecId"
                    + " FROM pOrder AS po "
                    + " LEFT JOIN voucher_Type AS vt ON po.v_Type = vt.v_Type  "
                    + " LEFT JOIN currency AS cr ON cr.code=po.currencyCode  "
                    + " LEFT JOIN subGroup AS sgv ON po.subCode = sgv.subCode  "
                    + " LEFT JOIN ("
                    + "         SELECT pod.docId, pod.quotationDocId"
                    + "         FROM pOrderDetail pod"
                    + "         WHERE pod.quotationDocId IS NOT NULL"
                    + "         GROUP BY pod.docId, pod.quotationDocId"
                    + " ) pod ON po.docId = pod.docId"
                    + " LEFT JOIN quotation AS q ON q.DocId= pod.quotationDocId"
                    + " where po.docId=:docId And vt.category  in (" + vCat + ") ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            if (rsTemp1.next()) {
                mrTemp = new PurchaseOrderApproval_Mdl();
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
                    + " pod.rate, pod.oh_Amt_Net as amount, pod.v_SNo,pod.isStatus,pod.colourCode,icm.name as colourName,"
                    + " pod.sizeCode as sizeCode,ism.name sizeName,im.propertyList,pod.statusRemark  "
                    + " from pOrderDetail as pod "
                    + " left join itemMast as im on pod.itemCode = im.code "
                    + " left join unitMast as um on pod.unitCode = um.code  "
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode "
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode "
                    + " where docId=:docId order by itemName Asc ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            while (rsTemp1.next()) {
                grdItemDetail_Mdl = new GrdItemDetail_Mdl();
                grdItemDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("itemCode")));
                grdItemDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("itemName")));
                grdItemDetail_Mdl.setvSNo(DmShared.xNull(rsTemp1.getString("v_SNo")));
                grdItemDetail_Mdl.setAmount(DmShared.xNull(rsTemp1.getString("amount")));
                grdItemDetail_Mdl.setOrderQty(DmServer.vNull(rsTemp1.getString("quantity")));
                grdItemDetail_Mdl.setOrderDate(DmShared.dNull(rsTemp1.getDate("requiredDate")));
                grdItemDetail_Mdl.setUnit(DmShared.xNull(rsTemp1.getString("unitName")));
                grdItemDetail_Mdl.setRemark(DmShared.xNull(rsTemp1.getString("remark")));
                grdItemDetail_Mdl.setRate(DmServer.vNull(rsTemp1.getString("rate"), "0.00"));
                grdItemDetail_Mdl.setIsStatus(DmShared.xNull(rsTemp1.getString("isStatus")));
                grdItemDetail_Mdl.setIsChecked(DmShared.xNull(rsTemp1.getString("isStatus")));

                grdItemDetail_Mdl.setColorCode(DmShared.xNull(rsTemp1.getString("colourCode")));
                grdItemDetail_Mdl.setColor(DmShared.xNull(rsTemp1.getString("colourName")));

                grdItemDetail_Mdl.setSizeCode(DmShared.xNull(rsTemp1.getString("sizeCode")));
                grdItemDetail_Mdl.setSize(DmShared.xNull(rsTemp1.getString("sizeName")));
                grdItemDetail_Mdl.setClosedPurchaseOrderApproval(DmShared.xNull(rsTemp1.getString("statusRemark")));

                listNode.add(grdItemDetail_Mdl);
            }
            mrTemp.getGrdItemDetail_Mdl().addAll(listNode);
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String vSNo,String closedPurchaseOrderApproval) {
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
            
                  if (!DmShared.xNull(closedPurchaseOrderApproval).equalsIgnoreCase("")) {
                sqlQuery = " Update pOrderDetail set "
                        + " statusRemark=:statusRemark "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(vSNo).toString());
                psTemp.setString("statusRemark", DmShared.xNull(closedPurchaseOrderApproval));
                psTemp.executeUpdate();
            }
            
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
                manageIndentStatus(docId, vSNo, false, cnTemp);

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

                manageIndentStatus(docId, vSNo, true, cnTemp);

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
                    + " u_EntDt_LatestLine=:uEntDt"
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

    private void manageIndentStatus(String docId, String vSno, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp;
        String sqlQuery, indentDocId = "";
        double aprQty, ordedQty, ordQty;
        Integer isStatus;

        sqlQuery = "select i.docId as indentDocId, id.v_SNo as indentVSno,pid.orderedQty as orderQty,id.aprQty,id.orderedQty ,id.isStatus,i.approvedBy,"
                + "i.recId as indentNo,vt.short_name intentType "
                + "from indentDetail id "
                + "left join indent i on i.docId = id.docId "
                + "left join voucher_type vt on vt.v_type = i.v_Type "
                + "inner join ( "
                + "     select pid.indentDocId, pid.indentV_SNo, sum(orderedQty) as orderedQty "
                + "     from pOrderIndentDetail pid "
                + "     where pid.docId = :docId and pid.v_Sno = :v_Sno "
                + "     group by pid.indentDocId, pid.indentV_SNo "
                + ") pid on id.docId = pid.indentDocId and id.v_Sno = pid.indentV_SNo";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", DmShared.iNull(docId).toString());
        psTemp.setString("v_Sno", DmShared.iNull(vSno).toString());
        rsTemp = psTemp.executeQuery();
        while (rsTemp.next()) {
            isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
            if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                throw new Exception("[" + DmShared.xNull(rsTemp.getString("indentNo")) + "-" + DmShared.xNull(rsTemp.getString("intentType")) + "] indent is not Approved.");
            }

            if (isStatus.intValue() == 1) {
                throw new Exception("[" + DmShared.xNull(rsTemp.getString("indentNo")) + "-" + DmShared.xNull(rsTemp.getString("intentType")) + "] indent is Cancelled.");
            }
            aprQty = rsTemp.getDouble("aprQty");
            ordedQty = rsTemp.getDouble("orderedQty");
            ordQty = rsTemp.getDouble("orderQty");

            if (isDelete) {
                ordedQty -= ordQty;
            } else {
                ordedQty += ordQty;
            }

            if (ordedQty > aprQty || ordedQty < 0) {
                throw new Exception("[" + DmShared.xNull(rsTemp.getString("indentNo")) + "-" + DmShared.xNull(rsTemp.getString("intentType")) + "] indent overflows Approval Quantity.");
            } else if (ordedQty == aprQty) {
                isStatus = 2;
            } else {
                isStatus = 0;
            }

            if (!indentDocId.equalsIgnoreCase("")) {
                indentDocId += ",";
            }

            indentDocId += DmShared.iNull(rsTemp.getString("indentDocId")).toString();

            sqlQuery = "update IndentDetail set "
                    + "isStatus = :isStatus,"
                    + " orderedQty=:orderedQty "
                    + "where docId = :docId and v_SNo = :v_SNo ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("indentDocId")).toString());
            psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("indentVSno")).toString());
            psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
            psTemp1.setString("orderedQty", DmServer.vNull(ordedQty));
            psTemp1.execute();
            psTemp1.close();
        }
        rsTemp.close();
        psTemp.close();

        if (!indentDocId.equalsIgnoreCase("")) {
            sqlQuery = "update indent set u_EntDt_LatestLine = :uEntDt where docId in (" + indentDocId + ")   ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Pending");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Cancelled");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Com");
            listNode.setSharedKeyFieldName("Completed");
            lstTemp.add(listNode);
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
                    + " and  vt.Category in(" + vCat + ")";
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
}
