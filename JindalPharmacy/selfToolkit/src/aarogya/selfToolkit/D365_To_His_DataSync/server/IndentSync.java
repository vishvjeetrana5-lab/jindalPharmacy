package aarogya.selfToolkit.D365_To_His_DataSync.server;

import aarogya.selfToolkit.D365_To_His_DataSync.shared.GrdMedicineDetail_Mdl;
import aarogya.selfToolkit.D365_To_His_DataSync.shared.Indent_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IndentSync {

    public static void insertIntoD365DataBase(Indent_Mdl mrTemp, Connection cnTemp, DmRSS rssTemp) throws Exception {
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {

            sqlQuery = "INSERT INTO indentheader ("
                    + "docId, IndentNo, v_Date, dm_indentorCode, dm_departmentCode, departmentCode, DepartmentName, "
                    + "BusinessUnitCode, BusinessUnitName, preparedBy, preparedDate, dm_toDepartmentCode, "
                    + "ToDepartmentName, toDepartmentCode, approvedBy, approvalDateTime, remark, "
                    + "D365_Status, dm_timestamp, "
                    + "VoucherTypeCode,VoucherTypeName, indentorCode, IndentorName,D365_ErrorRemarks"
                    + ") VALUES ("
                    + ":docId, :IndentNo, :v_Date, :dm_indentorCode, :dm_departmentCode, :departmentCode, :DepartmentName, "
                    + ":BusinessUnitCode, :BusinessUnitName, :preparedBy, :preparedDate, :dm_toDepartmentCode, "
                    + ":ToDepartmentName, :toDepartmentCode, :approvedBy, :approvalDateTime, :remark, "
                    + ":D365_Status, :dm_timestamp, "
                    + ":VoucherTypeCode,:VoucherTypeName ,:indentorCode, :IndentorName,:D365_ErrorRemarks"
                    + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("IndentNo", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getDtIndent()));
            psTemp.setString("dm_indentorCode", DmShared.xNull(DmShared.foriegnKey(mrTemp.getIndentorTag())));
            psTemp.setString("dm_departmentCode", DmShared.xNull(DmShared.foriegnKey(mrTemp.getDepartmentTag())));
            psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getD365DepartmentTag()));
            psTemp.setString("DepartmentName", DmShared.xNull(mrTemp.getDepartmentText()));
            psTemp.setString("BusinessUnitCode", DmShared.xNull(mrTemp.getBusinessUnitCode()));
            psTemp.setString("BusinessUnitName", DmShared.xNull(mrTemp.getBusinessUnitName()));
            psTemp.setString("preparedBy", DmShared.xNull(DmServer.getSsnUserName(rssTemp)));
            psTemp.setDate("preparedDate", DmServer.convertDate(mrTemp.getDtIndent()));
            psTemp.setString("dm_toDepartmentCode", DmShared.xNull(mrTemp.getToDepartmentTag()));
            psTemp.setString("ToDepartmentName", DmShared.xNull(mrTemp.getToDepartmentText()));
            psTemp.setString("toDepartmentCode", DmShared.xNull(mrTemp.getD365toDepartmentTag()));
            psTemp.setString("approvedBy", DmShared.xNull(DmServer.getSsnUserName(rssTemp)));
            psTemp.setTimestamp("approvalDateTime", DmServer.getServerTimestamp());
            psTemp.setString("D365_ErrorRemarks", "");
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("D365_Status", "");
            psTemp.setTimestamp("dm_timestamp", DmServer.getServerTimestamp());
            psTemp.setString("VoucherTypeCode", DmShared.xNull(mrTemp.getIndentTypeTag()));
            psTemp.setString("VoucherTypeName", DmShared.xNull(mrTemp.getIndentTypeText()));
            psTemp.setString("indentorCode", DmShared.foriegnKey(mrTemp.getD365IndentorTag()));
            psTemp.setString("IndentorName", DmShared.xNull(mrTemp.getIndentorText()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM indentLine WHERE docId = :docId  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            for (int i = 0; i < mrTemp.getGrdItemDetails().size(); i++) {
                GrdMedicineDetail_Mdl detail = mrTemp.getGrdItemDetails().get(i);

                detail.setDmStatus(DmShared.xNull(mrTemp.getGrdItemDetails().get(i).getSharedKeyFieldCode()).equalsIgnoreCase("")
                        ? ("itemCode not found for this item.") : (""));
                sqlQuery = "INSERT INTO indentLine ("
                        + "v_SNo, itemCode, dm_itemCode, dm_ItemName, UOM, aprQty, remark, reqDate, "
                        + "docId, dm_timestamp, BusinessUnitCode, BusinessUnitName,D365_Status,dm_Status "
                        + ") VALUES ("
                        + ":v_SNo, :itemCode, :dm_itemCode, :dm_ItemName, :UOM, :aprQty, :remark, :reqDate, "
                        + ":docId, :dm_timestamp, :BusinessUnitCode, :BusinessUnitName,:D365_Status,:dm_Status"
                        + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("v_SNo", DmShared.iNull(detail.getvSno()).toString());
                psTemp.setString("itemCode", DmShared.foriegnKey(detail.getSharedKeyFieldCode()));
                psTemp.setString("dm_itemCode", DmShared.xNull(detail.getDm_itemCode()));
                psTemp.setString("dm_ItemName", DmShared.xNull(detail.getSharedKeyFieldName()));
                psTemp.setString("aprQty", DmServer.vNull(detail.getQty()));
                psTemp.setString("UOM", DmShared.xNull(detail.getUnitName()));
                psTemp.setString("remark", DmShared.xNull(detail.getRemarks())); 
                psTemp.setDate("reqDate", DmServer.convertDate(detail.getReqDate()));
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setTimestamp("dm_timestamp", DmServer.getServerTimestamp());
                psTemp.setString("BusinessUnitCode", DmShared.xNull(mrTemp.getBusinessUnitCode()));
                psTemp.setString("BusinessUnitName", DmShared.xNull(mrTemp.getBusinessUnitName()));
                psTemp.setString("D365_Status", "");
                psTemp.setString("dm_Status", detail.getDmStatus());
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
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static Indent_Mdl getIndentData(Connection cnTemp, String docId, String callFrom) throws Exception {
        Indent_Mdl indent_Mdl = new Indent_Mdl();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {

            sqlQuery = "SELECT \n"
                    + "H.docId, H.recId as IndentNo, H.v_Date, H.indentorCode as dmIndentorCode,\n"
                    + "H.departmentCode as dmDepartmentCode,sbi.d365_code as indentorCode , \n"
                    + "sb.d365_code as departmentCode,sb.name as DepartmentName, \n"
                    + "sm.BusinessUnit as BusinessUnitCode,  sm.businessUnitname as BusinessUnitName, \n"
                    + "sbi.name as IndentorName, \n"
                    + "H.v_Type as VoucherTypeCode,vt.description as VoucherTypeName,"
                    + "H.remark AS headerRemark, \n"
                    + "L.v_SNo, im.D365_code as itemCode,im.code as dm_itemCode ,  im.name as dm_ItemName, "
                    + " um.D365_code as UOM,\n"
                    + "L.aprQty,L.remark AS lineRemark, L.reqDate ,\n"
                    + "h.toDepartmentCode as dm_todepartmentCode,sbto.d365_code as toDepartmentCode,sbto.name as todepatementName \n"
                    + "FROM indent H \n"
                    + "left join voucher_Type vt on vt.v_Type = h.v_Type \n"
                    + "left join siteMast sm on sm.code = h.site_Code\n"
                    + "left JOIN indentDetail L ON H.docId = L.docId \n"
                    + "left join subGroup sb on sb.subcode = h.departmentCode\n"
                    + "left join subGroup sbi on sbi.subcode = h.indentorCode\n"
                    + "left join subGroup sbto on sbto.subcode = h.toDepartmentCode \n"
                    + "left join itemMast im on im.code = l.itemCode \n"
                    + "left join unitMast um on um.code=im.sku \n"
                    + "WHERE H.docId = :docId \n"
                    + "ORDER BY L.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            boolean headerSet = false;
            while (rsTemp.next()) {
                if (!headerSet) {
                    indent_Mdl.setSharedKeyFieldCode(rsTemp.getString("docId"));
                    indent_Mdl.setSharedKeyFieldName(rsTemp.getString("IndentNo"));
                    indent_Mdl.setDtIndent(DmShared.dNull(rsTemp.getDate("v_Date")));
                    indent_Mdl.setDepartmentTag(rsTemp.getString("dmDepartmentCode"));
                    indent_Mdl.setDepartmentText(rsTemp.getString("DepartmentName"));
                    indent_Mdl.setD365DepartmentTag(rsTemp.getString("departmentCode"));
                    indent_Mdl.setBusinessUnitCode(rsTemp.getString("BusinessUnitCode"));
                    indent_Mdl.setBusinessUnitName(rsTemp.getString("BusinessUnitName"));
                    indent_Mdl.setIndentorTag(rsTemp.getString("dmIndentorCode"));
                    indent_Mdl.setD365IndentorTag(rsTemp.getString("indentorCode"));
                    indent_Mdl.setIndentorText(rsTemp.getString("IndentorName"));
                    indent_Mdl.setIndentTypeTag(rsTemp.getString("VoucherTypeCode"));
                    indent_Mdl.setIndentTypeText(rsTemp.getString("VoucherTypeName"));
                    indent_Mdl.setRemark(rsTemp.getString("headerRemark"));
                    indent_Mdl.setRemark(rsTemp.getString("headerRemark"));
                    if (callFrom.equalsIgnoreCase("Inventory")) {
                        indent_Mdl.setD365toDepartmentTag(rsTemp.getString("toDepartmentCode"));
                        indent_Mdl.setToDepartmentTag(rsTemp.getString("dm_todepartmentCode"));
                        indent_Mdl.setToDepartmentText(rsTemp.getString("todepatementName"));
                    } else {
                        indent_Mdl.setD365toDepartmentTag(rsTemp.getString("departmentCode"));
                        indent_Mdl.setToDepartmentTag(rsTemp.getString("dmDepartmentCode"));
                        indent_Mdl.setToDepartmentText(rsTemp.getString("DepartmentName"));
                    }

                    headerSet = true;
                }
                // ===== LINE DATA =====
                GrdMedicineDetail_Mdl line = new GrdMedicineDetail_Mdl();
                line.setvSno(rsTemp.getString("v_SNo"));
                line.setSharedKeyFieldCode(rsTemp.getString("itemCode"));
                line.setDm_itemCode(rsTemp.getString("dm_itemCode"));
                line.setSharedKeyFieldName(rsTemp.getString("dm_ItemName"));
                line.setUnitName(rsTemp.getString("UOM"));
                line.setQty(rsTemp.getString("aprQty"));
                line.setRemarks(rsTemp.getString("lineRemark"));
                line.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                indent_Mdl.getGrdItemDetails().add(line);
            }
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
        return indent_Mdl;
    }
}
