package aarogya.pharmacy.purchaseReturnApproval.server;

import aarogya.pharmacy.purchaseReturnApproval.shared.PurchaseReturnApproval_Srv;
import aarogya.pharmacy.purchaseReturnApproval.shared.TxtHGrnNo_Mdl;
import aarogya.pharmacy.purchaseReturnApproval.shared.Setting_Mdl;
import aarogya.pharmacy.purchaseReturnApproval.shared.GrdPurchaseReturnApproval_Mdl;
import aarogya.pharmacy.purchaseReturnApproval.shared.TxtHBillNo_Mdl;
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

public class PurchaseReturnApproval_SrvImpl extends DmRSSPrivate implements PurchaseReturnApproval_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<GrdPurchaseReturnApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdPurchaseReturnApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPurchaseReturnApproval_Mdl> lstTemp = new ArrayList<>();
        GrdPurchaseReturnApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;
        condition = "";
        join = "";

        try {
            rstTemp.setData(lstTemp);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  pb.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  pb.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " AND pb.departmentCode IN(" + DmServer.getParamList(":Department_Code", param.getDepartmentCode()) + " )";
            }
            if (!DmShared.xNull(param.getSupplierCode()).equalsIgnoreCase("")) {
                condition += " AND pb.subcode IN(" + DmServer.getParamList(":subcode", param.getSupplierCode()) + " )";
            }
            if (!DmShared.xNull(param.getBillnoDocId()).equalsIgnoreCase("")) {
                condition += " AND pbd.pbDocId IN(" + DmServer.getParamList(":pbDocId", param.getBillnoDocId()) + " )";
            }
            if (!DmShared.xNull(param.getGrnDocId()).equalsIgnoreCase("")) {
                condition += " AND pc.docId IN(" + DmServer.getParamList(":docId", param.getGrnDocId()) + " )";
            }
            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += " AND isNull(pb.approvedBy,'') = '' ";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = pb.wtaCode and wtfp.isStatus = 0 \n";
                condition += "  \n  AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += " AND isNull(pb.approvedBy,'') <> '' ";
            }
            condition += " AND pb.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  pb.departmentCode,sgdm.name AS depName,pb.recId ,pb2.recId AS Billno, pc.docId AS grnDocId, pc.recId AS grnNo, pb.docId, \n"
                    + "         pbd.itemCode,im.name AS itemName,vt.short_Name, po.RecId as orderNo,pb.v_Date,pb.wtaCode, \n"
                    + "         sm.name AS siteName, pbd.rate, pbd.oh_Amt_Net as amount, sgv.name AS suppName, um.unit, pbd.skuQTy, im.sku, umsku.unit AS skuUnit,\n"
                    + "         pbd.batchNo, pbd.ibCode, pbd.acceptQty AS quantity, pbd.expiryDate, pbd.mrp, pb2.docId AS pbDocId, pbd.v_SNo, \n"
                    + "         concat(pb.approvedBy, ' ',format(pb.approvalDateTime, 'dd-MMM-yyyy HH.mm')) as approval, pb.approvedBy \n"
                    + " FROM pBill pb \n"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type\n"
                    + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId\n"
                    + "	LEFT JOIN pBill pb2 ON pb2.docId = pbd.pbDocId\n"
                    + "	LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId\n"
                    + "	LEFT JOIN pOrder po ON po.docId = pbd.poDocId\n"
                    + "	LEFT JOIN itemMast im ON im.code = pbd.itemCode\n"
                    + "	LEFT JOIN unitMast um ON um.code = pbd.unitCode\n"
                    + "	LEFT JOIN unitMast umsku ON umsku.code = im.sku\n"
                    + " LEFT JOIN siteMast sm ON sm.code = pb.site_Code\n"
                    + " LEFT JOIN subGroup sgv ON pb.subCode = sgv.subCode\n"
                    + " LEFT JOIN subGroup sgdm ON pb.departmentCode = sgdm.subCode\n" + join
                    + " WHERE ISNULL(pbd.isFooter, 0) = 0 AND vt.category in( '" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')" + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }
            if (!DmShared.xNull(param.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("Department_Code", DmShared.xNull(param.getDepartmentCode()));
            }
            if (!DmShared.xNull(param.getSupplierCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("subcode", DmShared.xNull(param.getSupplierCode()));
            }
            if (!DmShared.xNull(param.getBillnoDocId()).equalsIgnoreCase("")) {
                psTemp.setStringList("pbDocId", DmShared.xNull(param.getBillnoDocId()));
            }
            if (!DmShared.xNull(param.getGrnDocId()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(param.getGrnDocId()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdPurchaseReturnApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("depName")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("suppName")));
                listNode.setEntryDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setReturnCatName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approval")));
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setBatchCode(DmShared.xNull(rsTemp.getString("ibCode")));
                listNode.setBatchName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setGrnNo(DmShared.xNull(rsTemp.getString("grnNo")));
                listNode.setBillNo(DmShared.xNull(rsTemp.getString("Billno")));
                listNode.setOrderNo(DmShared.xNull(rsTemp.getString("orderNo")));
                listNode.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setQty(DmServer.vNull(rsTemp.getString("quantity"), "0.00"));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuUnit")));
                listNode.setSkuQty(DmServer.vNull(rsTemp.getString("skuQTy"), "0.00"));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setCurrentUser(DmServer.getSsnUserName(this));
                listNode.setWtaCode(DmShared.iNull(rsTemp.getString("wtaCode")).toString());
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
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
    public DmResultPageing<List<TxtHGrnNo_Mdl>> getTxtHGrnNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHGrnNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGrnNo_Mdl> lstTemp = new ArrayList<>();
        TxtHGrnNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select docId, recId, pChallanNo, pChallanDate from pChallan where site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGrnNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                listNode.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
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
    public DmResultPageing<List<TxtHBillNo_Mdl>> getTxtHBillNo(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBillNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBillNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBillNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT pb.docId, pb.recId, pb.v_Date, vt.category"
                    + "  FROM pBill AS pb "
                    + "  LEFT JOIN voucher_Type as vt on vt.v_Type = pb.v_Type"
                    + "  WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "') and pb.site_Code = '" + DmServer.getSsnSiteCode(this) + "'";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setType(DmShared.xNull(rsTemp.getString("category")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
