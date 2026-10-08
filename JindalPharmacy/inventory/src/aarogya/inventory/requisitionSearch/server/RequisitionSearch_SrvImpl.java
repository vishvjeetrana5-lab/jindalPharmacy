package aarogya.inventory.requisitionSearch.server;

import aarogya.inventory.requisitionSearch.shared.GrdRequisitiontDetails_Mdl;
import aarogya.inventory.requisitionSearch.shared.RequisitionSearch_Srv;
import aarogya.inventory.requisitionSearch.shared.SearchValue_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
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

public class RequisitionSearch_SrvImpl extends DmRSSPrivate implements RequisitionSearch_Srv {

    @Override
    public DmResultPageing<List<GrdRequisitiontDetails_Mdl>> getRequisitionDetail(String itemCode, String departmentToCode, SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdRequisitiontDetails_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery;
        List<GrdRequisitiontDetails_Mdl> lstTemp = new ArrayList<GrdRequisitiontDetails_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*=================================  Set Conditions ===============================================*/
            if (!DmShared.xNull(itemCode).equalsIgnoreCase("")) {
                condition += " and rqd.itemCode =:itemCode ";
            }
            if (!DmShared.dNull(searchValueMdl.getFromDate()).equalsIgnoreCase("")) {
                condition += " and rq.v_Date >= :fromDate ";
            }
            if (!DmShared.dNull(searchValueMdl.getToDate()).equalsIgnoreCase("")) {
                condition += " and rq.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentTag()).equalsIgnoreCase("")) {
                condition += " and rq.departmentCode in (" + DmServer.getParamList(":departmentCode", searchValueMdl.getDepartmentTag()) + " )";
            }

            sqlQuery = "select	rqd.docId, rqd.v_SNo, rq.recId as requisitionNo, rq.v_Date as requisitionDate, sgr.name as requestedByName, "
                    + "		sgd.name as departmentName, im.name as itemName, um.unit, rqd.reqQty as quantity, reqDate, rqd.remark "
                    + "		from requisition as rq "
                    + "		left join requisitionDetail as rqd on rq.docId = rqd.docId "
                    + "		left join subGroup as sgd on rq.departmentCode = sgd.subCode "
                    + "         left join subGroup as sgr on rq.requestedByCode = sgr.subCode "
                    + "		left join itemMast as im on rqd.itemCode = im.code "
                    + "		left join unitMast as um on im.sku = um.code "
                    + "		where rqd.site_Code = :site_Code and rqd.isStatus = 0 and rq.departmentToCode = :departmentToCode "
                    + "         and isNull(rq.approvedBy,'') <> '' " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentToCode", DmShared.iNull(departmentToCode).toString());
            if (!DmShared.xNull(itemCode).equalsIgnoreCase("")) {
                sqlWithParam.setString("itemCode", DmShared.iNull(itemCode).toString());
            }
            if (!DmShared.dNull(searchValueMdl.getFromDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("fromDate", DmServer.convertDate(searchValueMdl.getFromDate()));
            }
            if (!DmShared.dNull(searchValueMdl.getToDate()).equalsIgnoreCase("")) {
                sqlWithParam.setDate("toDate", DmServer.convertDate(searchValueMdl.getToDate()));
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentTag()).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("departmentCode", DmShared.xNull(searchValueMdl.getDepartmentTag()));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdRequisitiontDetails_Mdl objListTemp = new GrdRequisitiontDetails_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("requisitionNo")));
                objListTemp.setVoucherSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                objListTemp.setRequisitiontDate(DmShared.dNull(rsTemp.getDate("requisitionDate")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setSku(DmShared.xNull(rsTemp.getString("unit")));
                objListTemp.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                objListTemp.setRequiredDate(DmShared.dNull(rsTemp.getDate("reqDate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
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
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
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
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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
