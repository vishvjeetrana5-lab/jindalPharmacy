package aarogya.inventory.requisitionApproval.server;

import aarogya.inventory.requisitionApproval.shared.TxtHRequisitionNo_Mdl;
import aarogya.inventory.requisitionApproval.shared.RequisitionApproval_Srv;
import aarogya.inventory.requisitionApproval.shared.GrdMedicineDetail_Mdl;
import aarogya.inventory.requisitionApproval.shared.RequisitionApproval_Mdl;
import aarogya.inventory.requisitionApproval.shared.GrdRequisitionApproval_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
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

public class RequisitionApproval_SrvImpl extends DmRSSPrivate implements RequisitionApproval_Srv {

    private String v_typeShortName = new ProjectVoucherCategory().INVENTORY_REQUISITION;

    @Override
    public DmResultPageing<List<GrdRequisitionApproval_Mdl>> getFillRecords(RequisitionApproval_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdRequisitionApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdRequisitionApproval_Mdl> lstTemp = new ArrayList<>();
        GrdRequisitionApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join, rqdCondition, itemCondtion;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = "";
            rqdCondition = " ";
            itemCondtion = "";
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  rq.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  rq.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getIndenterCode()).equalsIgnoreCase("")) {
                condition += " AND rq.requestedByCode IN(" + DmServer.getParamList(":requestedByCode", param.getIndenterCode()) + " )";
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {

                condition += " AND rq.departmentCode IN(" + DmServer.getParamList(":Department_Code", param.getDepartmentCode()) + " )";
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                itemCondtion += " AND rqd.itemCode IN(" + DmServer.getParamList(":itemCode", param.getItemCode()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("C")) {
                rqdCondition += "   AND isNull(rqd.isStatus,'')=('1') \n";
                condition += "      AND isNull(rqd.isStatus,'')=('1')";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "       AND isNull(rq.approvedBy,'') = '' AND isNull(rqd.isStatus,'') = '0'";
                rqdCondition += "    AND isNull(rqd.isStatus,'') = '0' \n";

                join += "           left join walkThroughFootPrint wtfp on wtfp.wtaCode = rq.wtaCode and wtfp.isStatus = 0 \n";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "      AND isNull(rq.approvedBy,'') <> '' AND isNull(rqd.isStatus,'') = '0' \n";
                rqdCondition += "   AND isNull(rqd.isStatus,'') = '0' \n";

            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("Com")) {
                condition += "          AND   isNull(rqd.isStatus,'') = '2'";
                rqdCondition += "       AND isNull(rqd.isStatus,'') = '2' \n";
            }

            if (!DmShared.xNull(param.getRequisitionDocIds()).equalsIgnoreCase("")) {

                condition += " AND rq.docId IN(" + DmServer.getParamList(":docId", param.getRequisitionDocIds()) + " )";
            }

            condition += "      AND rq.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " 	Select   rqd.isStatus, rq.departmentCode, sdm.name AS depName, rq.requestedByCode, \n"
                    + "		sg.name AS intenderName, rq.recId, rq.docId, rq.v_Date,\n"
                    + "		STUFF(( select ', '+im.name+' QTY-'+convert(varchar,convert(decimal(8,2),rqd.reqQty))  "
                    + "         from requisitionDetail AS rqd       \n"
                    + "		LEFT JOIN itemMast AS im ON im.code=rqd.itemCode "
                    + "          where rqd.docId=rq.docId     FOR XML PATH('')  \n"
                    + "		),1,1,'') itemName ,   \n"
                    + "		rq.site_Code, sm.name AS siteName, rq.approvedBy,      \n"
                    + "		FORMAT(rq.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime, rq.wtaCode,      \n"
                    + "		rq.v_Type, vt.category AS IndType\n"
                    + "       from requisition AS rq \n"
                    + "       LEFT JOIN siteMast AS sm ON sm.code=rq.site_Code \n"
                    + "       LEFT JOIN subGroup AS sg on sg.subCode=rq.requestedByCode \n"
                    + "       LEFT JOIN subgroup AS sdm on sdm.subcode=rq.departmentCode \n"
                    + "       left join (\n"
                    + "		   select max(isNull(rqd.isStatus,''))isStatus, max(rqd.DocId)DocId \n"
                    + "                 from requisitionDetail rqd where 1=1 " + itemCondtion + " \n" + rqdCondition
                    + "                 group by docid              \n"
                    + "		   ) rqd on rqd.docid =rq.docId  \n"
                    + "       LEFT JOIN voucher_Type AS vt on vt.v_Type=rq.v_Type \n" + join
                    + "       where vt.category IN ('" + v_typeShortName + "') \n" + condition;

            sqlWithParam.setSQL(sqlQuery);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getIndenterCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("requestedByCode", DmShared.xNull(param.getIndenterCode()));
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("itemCode", DmShared.xNull(param.getItemCode()));
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("Department_Code", DmShared.xNull(param.getDepartmentCode()));            //Department
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.xNull(param.getRequisitionDocIds()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(param.getRequisitionDocIds()));
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdRequisitionApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                listNode.setIndDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("IndType")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));

                listNode.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));

                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setWtaCode(DmShared.iNull(rsTemp.getString("wtaCode")).toString());

                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
//                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
//                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
//                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
//                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
//                listNode.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
//                listNode.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
//                listNode.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
//                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
//                listNode.setSKUCode(DmShared.xNull(rsTemp.getString("sku")));
//                listNode.setSKUName(DmShared.xNull(rsTemp.getString("unit")));
//                listNode.setSKUQty(DmServer.vNull(rsTemp.getString("SKUQty")));
//                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
//                listNode.setItemCurrentPosition(DmShared.xNull(rsTemp.getString("itemCurrPosition")));
                listNode.setProperties(ipm.getPropertiesCsv());

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
    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo, String closedRequisitionRemark) {
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
            if (!DmShared.xNull(closedRequisitionRemark).equalsIgnoreCase("")) {
                sqlQuery = " Update requisitionDetail set "
                        + " statusRemark=:statusRemark "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("statusRemark", DmShared.xNull(closedRequisitionRemark));
                psTemp.executeUpdate();
            }
            if (isOpen) {
                sqlQuery = " Update RequisitionDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();
            } else {
                sqlQuery = " select Count(isStatus) as cnt from RequisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " select Count(isStatus) as cnt from RequisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is completed,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " UPDATE RequisitionDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId AND v_SNo =:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update Requisition set "
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
    public DmResult<RequisitionApproval_Mdl> fillItemGrid(String code) {
        DmResult<RequisitionApproval_Mdl> dmrTemp = new DmResult<>();
        GrdMedicineDetail_Mdl grdItemDetail_Mdl;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<>();
        RequisitionApproval_Mdl mrTemp = new RequisitionApproval_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp1 = null;
        ResultSet rsTemp1 = null;
        String sqlQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  "
                    + "  req.docId,req.recId,req.v_Date, req.approvedBy,req.departmentCode,sgd.name AS depName, "
                    + "  FORMAT(req.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,req.wtaCode, "
                    + "  req.requestedByCode AS reqByCode,sgv.name AS reqByName "
                    + "  FROM requisition AS req  "
                    + "  LEFT JOIN voucher_Type AS vt ON req.v_Type = vt.v_Type   "
                    + "  LEFT JOIN subGroup AS sgv ON req.requestedByCode = sgv.subCode "
                    + "  LEFT JOIN departmentMast AS dm on dm.subCode=req.departmentCode "
                    + "  LEFT JOIN subGroup AS sgd ON dm.subCode= sgd.subCode "
                    + " where req.docId=:docId ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            if (rsTemp1.next()) {
                mrTemp = new RequisitionApproval_Mdl();
                mrTemp.setRequitDocId(DmShared.xNull(rsTemp1.getString("docId")));
                mrTemp.setRequitRecId(DmShared.xNull(rsTemp1.getString("recId")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp1.getString("wtaCode")));
                mrTemp.setRequitDate(DmShared.dNull(rsTemp1.getDate("v_Date")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp1.getString("approvedBy")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp1.getString("depName")));
                mrTemp.setRequestedByCode(DmShared.xNull(rsTemp1.getString("reqByCode")));
                mrTemp.setRequestedByName(DmShared.xNull(rsTemp1.getString("reqByName")));
                mrTemp.setApprovedDateTime(DmShared.xNull(rsTemp1.getString("approvalDateTime")));

            }

            sqlQuery = "select reqd.docId, reqd.itemCode, im.name as itemName, reqd.reqQty,reqd.colourCode,reqd.sizeCode, "
                    + " reqd.reqDate, reqd.remark, icm.name as colourName,reqd.statusRemark,"
                    + " ism.name sizeName, "
                    + " reqd.v_SNo,reqd.isStatus "
                    + " from requisitionDetail as reqd "
                    + " left join itemMast as im on reqd.itemCode = im.code   "
                    + " LEFT JOIN itemColourMast icm ON icm.code = reqd.colourCode "
                    + " LEFT JOIN itemSizeMast ism ON ism.code = reqd.sizeCode "
                    + " where reqd.docId=:docId order by itemName Asc ";

            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp1.setString("docId", DmShared.xNull(code));
            rsTemp1 = psTemp1.executeQuery();
            while (rsTemp1.next()) {
                grdItemDetail_Mdl = new GrdMedicineDetail_Mdl();
                grdItemDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("itemCode")));
                grdItemDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("itemName")));
                grdItemDetail_Mdl.setColourName(DmShared.xNull(rsTemp1.getString("colourName")));
                grdItemDetail_Mdl.setSizeName(DmShared.xNull(rsTemp1.getString("sizeName")));
                grdItemDetail_Mdl.setvSNo(DmShared.xNull(rsTemp1.getString("v_SNo")));
                grdItemDetail_Mdl.setReqQty(DmServer.vNull(rsTemp1.getString("reqQty")));
                grdItemDetail_Mdl.setReqDate(DmShared.dNull(rsTemp1.getDate("reqDate")));
                grdItemDetail_Mdl.setRemark(DmShared.xNull(rsTemp1.getString("remark")));
                grdItemDetail_Mdl.setIsStatus(DmShared.xNull(rsTemp1.getString("isStatus")));
                grdItemDetail_Mdl.setIsChecked(DmShared.xNull(rsTemp1.getString("isStatus")));
                grdItemDetail_Mdl.setClosedRequisitionRemark(DmShared.xNull(rsTemp1.getString("statusRemark")));

                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp1.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp1.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp1.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp1.getString("sizeName")));

                grdItemDetail_Mdl.setProperties(ipm.getPropertiesCsv());

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.name, im.code "
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndenter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select max(sg.name) AS Name,max(mid.requestedByCode) AS requestedByCode "
                    + " from requisition mid "
                    + " LEFT JOIN subGroup AS sg on sg.subCode=mid.requestedByCode"
                    + " GROUP BY mid.requestedByCode";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PR') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBrand(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select max(mm.itemMakeCode) AS CODE,  max(mmm.companyName) AS NAME "
                    + "  from itemMast AS mm "
                    + "  LEFT JOIN itemMakeMast AS mmm ON mmm.code = mm.itemMakeCode "
                    + "  group by mm.itemMakeCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSKU(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select Code,unit AS Name from unitMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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

    @Override
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<>();
        TxtHRequisitionNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select	rq.docId, rq.recId as requisitionNo, rq.v_Date as requisitionDate, "
                    + "rq.v_Type,vt.category,vt.short_Name "
                    + "from requisition as rq "
                    + "left join voucher_Type as vt on rq.v_Type = vt.v_Type "
                    + "where rq.site_Code = '" + DmServer.getSsnSiteCode(this) + "' and vt.category IN ('" + v_typeShortName + "')";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHRequisitionNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("requisitionNo")));
                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("requisitionDate")));
                listNode.setReqType(DmShared.xNull(rsTemp.getString("category")));
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

    @Override
    public DmResult<Boolean> getLinkedRecords(String docId, String vSNo) {
        DmResult<Boolean> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(true);
        boolean existRecord = true;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select count(docid) as recExist \n"
                    + " 	from (\n"
                    + " 		select docId from indentDetail where requisitionDocId=:requisitionDocId and requisitionV_SNo=:requisitionV_SNo\n"
                    + " 		union all\n"
                    + " 		select docId from stockHeadDetail where requisitionDocId=:requisitionDocId and requisitionV_SNo=:requisitionV_SNo\n"
                    + " 		) a";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("requisitionDocId", DmShared.iNull(docId).toString());
            psTemp.setString("requisitionV_SNo", DmShared.iNull(vSNo).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("recExist")).intValue() == 1) {
                    existRecord = false;
                }
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            dmrTemp.setData(existRecord);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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
