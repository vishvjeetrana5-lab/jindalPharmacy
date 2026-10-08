package aarogya.pharmacy.purchaseBillApproval.server;

import aarogya.pharmacy.purchaseBillApproval.shared.PurchaseBillApproval_Srv;
import aarogya.pharmacy.purchaseBillApproval.shared.TxtHPurchaseBillNo_Mdl;
import aarogya.pharmacy.purchaseBillApproval.shared.Setting_Mdl;
import aarogya.pharmacy.purchaseBillApproval.shared.GrdPurchaseBillApproval_Mdl;
import aarogya.pharmacy.purchaseBillApproval.shared.TxtHCategory_Mdl;
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

public class PurchaseBillApproval_SrvImpl extends DmRSSPrivate implements PurchaseBillApproval_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatBill = "'" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "'";
    private String vCat = "'" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "'";

    @Override
    public DmResultPageing<List<GrdPurchaseBillApproval_Mdl>> getFillRecords(Setting_Mdl param, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdPurchaseBillApproval_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPurchaseBillApproval_Mdl> lstTemp = new ArrayList<>();
        GrdPurchaseBillApproval_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition, join;

        try {
            rstTemp.setData(lstTemp);

            condition = " ";
            join = " ";

            if (!DmShared.xNull(param.getCategoryName()).equalsIgnoreCase("")) {
                condition += " AND vt.category IN(" + DmServer.getParamList(":category", param.getCategoryName()) + " )";
            } else {
                condition += " AND vt.category IN(" + vCat + ")";
            }
            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND  pb.v_Date >= :fromv_Date ";
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                condition += " AND  pb.v_Date <= :tov_Date ";
            }
            if (!DmShared.xNull(param.getSupplierCode()).equalsIgnoreCase("")) {

                condition += " AND pb.subCode IN(" + DmServer.getParamList(":Supplier_Code", param.getSupplierCode()) + " )";
            }
            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                condition += " AND pbd.itemCode IN(" + DmServer.getParamList(":itemCode", param.getItemCode()) + " )";
            }
            if (!DmShared.xNull(param.getGrnNoTag()).equalsIgnoreCase("")) {

                condition += " AND pc.DocId IN(" + DmServer.getParamList(":grnDocId", param.getGrnNoTag()) + " )";
            }
            if (!DmShared.xNull(param.getBillDocIds()).equalsIgnoreCase("")) {

                condition += " AND pb.docId IN(" + DmServer.getParamList(":billDocId", param.getBillDocIds()) + " )";
            }
            if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("P")) {
                condition += "    AND isNull(pb.approvedBy,'') = ''";
                join += " left join walkThroughFootPrint wtfp on wtfp.wtaCode = pb.wtaCode and wtfp.isStatus = 0 ";
                condition += "    AND isNull(wtfp.linkedUser,'" + DmServer.getSsnUserName(this) + ",') like '%" + DmServer.getSsnUserName(this) + ",%'";
            } else if (DmShared.xNull(param.getStatusCode()).equalsIgnoreCase("A")) {
                condition += "    AND isNull(pb.approvedBy,'') <> '' ";

            }

            condition += " AND pb.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  pb.docId as billDocId,pb.recId as billNo,pb.wtaCode,pb.approvedBy,format(pb.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime,\n"
                    + "   pb.preparedBy,pb.subCode,sg.name AS suppName,pb.v_Date as billDate,\n"
                    + "   pb.v_Type,vt.short_Name,vt.description, vt.category AS vCat,\n"
                    + "   pbd.docId,pbd.v_SNo,pc.DocId AS grnDocId,pc.recId AS grnNo,pc.pChallanNo,pc.v_Date AS challanDate,\n"
                    + "   po.docId AS poDocId,po.recId AS poRecId,pbd.itemCode,im.name AS itemName,pbd.docQty,pbd.acceptQty,\n"
                    + "   pbd.unitCode,um.unit AS unitName,pbd.rate,pbd.remark,pbd.oh_Amt_Gross AS amount,sm.name as siteName,\n"
                    + "   pb.partyBillNo,pb.PartyBillDate\n"
                    + " from pBill pb\n"
                    + " left join pBillDetail pbd on pbd.docId = pb.docId\n"
                    + " LEFT JOIN voucher_Type AS vt ON vt.v_Type=pb.v_Type\n"
                    + " LEFT JOIN subGroup AS sg ON sg.subCode=pb.subCode\n"
                    + " LEFT JOIN pChallan pc ON pc.docId=pbd.pcDocId\n"
                    + " LEFT JOIN pOrder po ON po.docId=pbd.poDocId\n"
                    + " LEFT JOIN itemMast im ON im.code=pbd.itemCode\n"
                    + " LEFT JOIN unitMast um ON um.code=pbd.unitCode\n"
                    + " left join siteMast sm on sm.code = pb.site_Code\n" + join
                    + " where ISNULL(pbd.isFooter, 0) = 0 " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            if (!DmShared.dNull(param.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromv_Date", DmServer.convertDate(param.getFromDate()));
            }
            if (!DmShared.dNull(param.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("tov_Date", DmServer.convertDate(param.getToDate()));
            }

            if (!DmShared.xNull(param.getItemCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(param.getItemCode()));
            }
            if (!DmShared.xNull(param.getSupplierCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("Supplier_Code", DmShared.xNull(param.getSupplierCode()));
            }
            if (!DmShared.xNull(param.getGrnNoTag()).equalsIgnoreCase("")) {
                psTemp.setStringList("grnDocId", DmShared.xNull(param.getGrnNoTag()));
            }
            if (!DmShared.xNull(param.getCategoryName()).equalsIgnoreCase("")) {
                psTemp.setStringList("category", DmShared.xNull(param.getCategoryName()));
            }
            if (!DmShared.xNull(param.getBillDocIds()).equalsIgnoreCase("")) {
                psTemp.setStringList("billDocId", DmShared.xNull(param.getBillDocIds()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdPurchaseBillApproval_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("billDocId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("billNo")));
                listNode.setSupplierTag(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSupplierText(DmShared.xNull(rsTemp.getString("suppName")));
                listNode.setBillDate(DmShared.dNull(rsTemp.getDate("billDate")));
                listNode.setVtypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setPartyBillDate(DmShared.dNull(rsTemp.getDate("PartyBillDate")));
                listNode.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                listNode.setVtypeText(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setvCat(DmShared.xNull(rsTemp.getString("vCat")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    listNode.setIsChecked("1");
                } else {
                    listNode.setIsChecked("0");
                }
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setAccQty(DmServer.vNull(rsTemp.getString("acceptQty"), "0.00"));
                listNode.setItemNameTag(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemNameText(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                listNode.setUnitTag(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitText(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setGrnNoTag(DmShared.xNull(rsTemp.getString("grnDocId")));
                listNode.setGrnNoText(DmShared.xNull(rsTemp.getString("grnNo")));
                listNode.setOrderNoTag(DmShared.xNull(rsTemp.getString("poDocId")));
                listNode.setOrderNoText(DmShared.xNull(rsTemp.getString("poRecId")));
                listNode.setDocQty(DmServer.vNull(rsTemp.getString("docQty"), "0.00"));
                listNode.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                listNode.setCurrentUser(DmServer.getSsnUserName(this));
                listNode.setWtaCode(DmShared.iNull(rsTemp.getString("wtaCode")).toString());

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
            sqlQuery = "SELECT  im.code, im.name"
                    + "	FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + "	WHERE im.isActive = 1"
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGrnNo(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlQuery = "SELECT    "
                    + "Max(pc.docId) as code, Max(pc.recId) as Name "
                    + "FROM pBill AS pb  "
                    + "left join pBillDetail pbd on pbd.docId = pb.docId   "
                    + "LEFT JOIN pChallan pc ON pc.docId=pbd.pcDocId  "
                    + "left join voucher_Type as vt on vt.v_Type = pb.v_Type "
                    + "where pb.site_Code = '" + DmServer.getSsnSiteCode(this) + "' "
                    + "group by pc.docId";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "select	sg.subCode as code, sg.name   "
                    + "         from subGroup as sg   "
                    + "         left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code "
                    + "         where sgn.manualCode = 'SU' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

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
    public DmResultPageing<List<TxtHPurchaseBillNo_Mdl>> getTxtHBillNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHPurchaseBillNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHPurchaseBillNo_Mdl> lstTemp = new ArrayList<>();
        TxtHPurchaseBillNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT   "
                    + "pb.docId, pb.recId as pbRecId, pb.v_Date as pbDate, vt.category  "
                    + "FROM pBill AS pb   "
                    + "left join voucher_Type as vt on vt.v_Type = pb.v_Type"
                    + " where vt.category in (" + vCat + ") and pb.site_Code = '" + DmServer.getSsnSiteCode(this) + "'";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHPurchaseBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pbRecId")));
                listNode.setBillType(DmShared.xNull(rsTemp.getString("category")));
                listNode.setBillDate(DmShared.dNull(rsTemp.getDate("pbDate")));
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
    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCategory_Mdl> lstTemp = new ArrayList<>();
        TxtHCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select description  ,category AS name,v_Type AS code from voucher_Type where category IN(" + vCat + ") ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCategory_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("description")));
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
