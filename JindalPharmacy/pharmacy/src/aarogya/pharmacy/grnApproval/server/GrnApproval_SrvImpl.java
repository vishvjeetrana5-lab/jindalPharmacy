package aarogya.pharmacy.grnApproval.server;

import aarogya.pharmacy.grnApproval.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GrnApproval_SrvImpl extends DmRSSPrivate implements GrnApproval_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<GrdGrnApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdGrnApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdGrnApproval_Mdl> lstTemp = new ArrayList<>();
        GrdGrnApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = " ";
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  pc.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  pc.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getSupplierCode()).equalsIgnoreCase("")) {
                condition += " AND pc.subcode IN(" + DmServer.getParamList(":supllierCode", param.getSupplierCode()) + " )";
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {

                condition += " AND pc.departmentCode IN(" + DmServer.getParamList(":Department_Code", param.getDepartmentCode()) + " )";
            }
            if (!DmShared.xNull(param.getOrderCode()).equalsIgnoreCase("")) {
                condition += " AND pcd.poDocId IN(" + DmServer.getParamList(":orderCode", param.getOrderCode()) + " )";
            }

            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(pc.approvedBy,'') = '' ";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = pc.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(pc.approvedBy,'') <> '' ";

            }

            condition += " AND pc.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT pc.docId,pc.recId,pcd.challanQty,pcd.unitCode,um.unit as unitName, pcd.acceptQty,pcd.skuQty,im.sku as skuCode, "
                    + " ums.unit as skuName,pcd.rate, pcd.oh_Amt_Gross as amount,pcd.itemCode, im.name as itemName, pcd.poDocId as orderCode, po.recId as orderNo, "
                    + " pcd.batchNo, pc.v_Type, vt.short_Name,pc.v_Date,pc.subcode as supCode, sgs.name as supName ,"
                    + " pc.pChallanNo, pc.pChallanDate,pc.departmentCode, sgd.name as departmentName,icm.name as colourName,"
                    + " ism.name sizeName,pcd.freeQty,"
                    + " pc.remark, pc.approvedBy,FORMAT(pc.approvalDateTime,'dd-MMM-yyyy HH:mm') AS approvalDateTime,pc.wtaCode,pc.site_Code, sm.name AS siteName "
                    + " from pChallan AS pc "
                    + " left join pChallanDetail  as pcd on pc.docId = pcd.docId "
                    + " left join voucher_Type as vt on pc.v_Type = vt.v_Type  "
                    + " left join subGroup as sgd on pc.departmentCode = sgd.subCode  "
                    + " left join subGroup as sgs on pc.subcode = sgs.subCode"
                    + " left join unitMast as um on pcd.unitCode = um.code "
                    + " left join itemColourMast icm ON icm.code = pcd.colourCode "
                    + " left join itemSizeMast ism ON ism.code = pcd.sizeCode "
                    + " left join itemMast as im on pcd.itemCode = im.code "
                    + " left join unitMast as ums on im.sku = ums.code "
                    + " LEFT JOIN siteMast AS sm ON sm.code=pc.site_Code"
                    + " left join pOrder as po on pcd.poDocId = po.docId "
                    + " left join pOrderDetail as pod on pcd.poDocId = pod.docId and pcd.poV_Sno = pod.v_Sno  "
                    + join
                    + " where ISNULL(pcd.isFooter, 0) = 0 and vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getSupplierCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("supllierCode", DmShared.xNull(param.getSupplierCode()));
            }

            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("Department_Code", DmShared.xNull(param.getDepartmentCode()));            //Department
            }
            if (!DmShared.xNull(param.getOrderCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("orderCode", DmShared.xNull(param.getOrderCode()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdGrnApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setChallanQty(DmServer.vNull(rsTemp.getString("challanQty"), "0.00"));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                listNode.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty"), ""));
                listNode.setAcptQty(DmServer.vNull(rsTemp.getString("acceptQty"), ""));
                listNode.setSKUQty(DmServer.vNull(rsTemp.getString("skuQty"), ""));
                listNode.setSKUUnit(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSKUUnitCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setOrderNo(DmShared.xNull(rsTemp.getString("orderNo")));
                listNode.setOrderCode(DmShared.xNull(rsTemp.getString("orderCode")));
//                listNode.setBatchCode(DmShared.xNull(rsTemp.getString("batchCode")));

                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setSupplierCode(DmShared.xNull(rsTemp.getString("supCode")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supName")));
                listNode.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                listNode.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                listNode.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setCurrentUser(DmServer.getSsnUserName(this));
                listNode.setWtaCode(DmShared.iNull(rsTemp.getString("wtaCode")).toString());

                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");

                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));

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
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrderNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select  po.docId, po.recId as orderNo"
                    + " from pOrder as po  "
                    + " LEFT JOIN voucher_Type as vt on po.v_Type = vt.v_Type  "
                    + " where  isNull(po.approvedBy,'') <> '' and  po.site_Code = :site_Code AND vt.category in ('" + pvc.PHARMACY_PURCHASE_ORDER + "') ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode((DmShared.iNull(rsTemp.getString("docId")).toString()));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("orderNo")));
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

}
