package aarogya.pharmacy.purchaseBill.server;

import aarogya.pharmacy.purchaseBill.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import static aarogya.selfToolkit.stStatic.server.STApprovals.checkPBillLocationAccounts;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.server.TdsCategoryPopup_SrvImpl;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PurchaseBill_SrvImpl extends DmRSSPrivate implements PurchaseBill_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pb.v_Type", voucherDateFieldName = "pb.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<TxtHVoucher_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucher_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucher_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucher_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type as code"
                    + " , vt.description as name"
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " , vt.balancingAc, sg.name as balancingAcName"
                    + " FROM voucher_Type vt "
                    + " left JOin subgroup sg on vt.balancingAc=sg.subCode "
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucher_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                listNode.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                listNode.setBalancingAcName(DmShared.xNull(rsTemp.getString("balancingAcName")));
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, String v_Type, String v_Date, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_BILL)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate,sg.guiConfigCode "
                    + " ,dateAdd(d, isNull(sg.dueDays,0),:v_Date) as PaymentDueDate "
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            sqlWithParam.setString("v_Date", DmShared.xNull(v_Date).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                listNode.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                listNode.setSupplierGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                listNode.setDueDays(DmShared.dNull(rsTemp.getDate("PaymentDueDate")));
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
    public DmResultPageing<List<TxtHGrnNo_Mdl>> getTxtHGrnNo(String subCode, String v_Date, String docId, String v_SNo, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHGrnNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGrnNo_Mdl> lstTemp = new ArrayList<TxtHGrnNo_Mdl>();
        TxtHGrnNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", pendingQty = "", pendingFreeQty = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty";
                pendingFreeQty = " , ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0) AS pendingFreeQty";
//                condition += " AND (CASE WHEN (pcd.isStatus = 0) OR (pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
                condition += " AND (CASE"
                        + "             WHEN pcd.isStatus = 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) > 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0)) > 0 THEN 1"
                        + "             ELSE 0 END) = 1";
            } else {
                pendingQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) AS pendingQty";
                pendingFreeQty = " , ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) AS pendingFreeQty";
//                condition += " AND ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) > 0";
//                condition += " AND pcd.isStatus = 0";
                condition += " AND (CASE"
                        + "             WHEN pcd.isStatus = 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0)) > 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0)) > 0 THEN 1"
                        + "             ELSE 0 END) = 1";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pc.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT "
                    + "   pc.docId AS pcDocId"
                    + " , pc.recId AS pcRecId"
                    + " , pc.v_Date AS pcV_Date"
                    + " , pcd.v_SNo AS pcV_Sno"
                    + " , pc.pChallanNo AS pcRefNo"
                    + " , pc.pChallanDate AS pcRefDate"
                    + " , pc.departmentCode AS pcDepartmentCode"
                    + " , po.docId AS poDocId"
                    + " , po.recId AS poRecId"
                    + " , pod.v_Sno AS poV_SNo"
                    + " , im.code AS itemCode"
                    + " , im.name AS itemName"
                    + " , im.hsnCode"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , um.code AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , um.gstUnitCode"
                    + " , pcd.mrp"
                    + " , pcd.skuMrp"
                    + " , pcd.rate"
                    + " , pcd.saleRate"
                    + " , pcd.skuSaleRate"
                    + " , pcd.oh_Amt_Gross"
                    + " , pcd.batchNo"
                    + " , pcd.ibCode"
                    + " , pcd.barcode"
                    + " , pcd.manufacturingDate"
                    + " , pcd.expiryDate"
                    + " , pcd.acceptQty" + pendingQty + pendingFreeQty
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.pcDocId, pbd.pcV_SNo, pbd.acceptQty, pbd.freeQty"
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + "     AND pbd.v_SNo = :v_SNo"
                        + " ) AS pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNO = pbd.pcV_SNo";
            }
            sqlQuery += " LEFT JOIN pOrderDetail pod ON pcd.poDocId = pod.docId AND pcd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + "	WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + " AND pc.subCode = :subCode"
                    + " AND ISNULL(pc.approvedBy,'') <> ''"
                    + " AND pc.site_Code = :site_Code"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
                sqlWithParam.setString("v_SNo", DmShared.xNull(v_SNo));
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGrnNo_Mdl();

                //Item
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                //Sku
                listNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));

                //Purchase Unit
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                listNode.setPurchaseUnitConversionRate(DmShared.xNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                //Properties
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                listNode.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //Order Details
                listNode.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                listNode.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                listNode.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));

                //Challan Details
                listNode.setSharedKeyFieldCode((DmShared.xNull(rsTemp.getString("pcDocId"))));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("pcRecId")));
                listNode.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));
                listNode.setPcRefNo(DmShared.xNull(rsTemp.getString("pcRefNo")));
                listNode.setPcRefDate(DmShared.dNull(rsTemp.getDate("pcRefDate")));
                listNode.setPcDepartmentCode(DmShared.xNull(rsTemp.getString("pcDepartmentCode")));

                //Quantities
                listNode.setGrnQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                listNode.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                listNode.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                double pendingSkuQty = (DmShared.vNull(rsTemp.getString("pendingQty")) + DmShared.vNull(rsTemp.getString("pendingFreeQty"))) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                listNode.setPendingSkuQty(DmServer.vNull(DmServer.vNull(pendingSkuQty, skuDecimalFormat)));

                //Rate
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                //OHL GRN
                listNode.setOHLListGRN(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(listNode.getSharedKeyFieldCode()), DmShared.xNull(listNode.getPcV_SNo()), cnTemp));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCalcOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Accept Quantity");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Document Quantity");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    @Override
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(List<KeyFieldExtended> keyFields, String subCode, String location, String billDate,
            String docId, String v_Date, Boolean singlePOFlowInPurchase) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, conditionLines = "", condition = "", pendingQty = "", pendingFreeQty = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty";
                pendingFreeQty = " , ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0) AS pendingFreeQty";

                condition += " AND (CASE"
                        + "             WHEN pcd.isStatus = 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) > 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0)) > 0 THEN 1"
                        + "             ELSE 0 END) = 1";
            } else {
                pendingQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) AS pendingQty";
                pendingFreeQty = " , ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) AS pendingFreeQty";

                condition += " AND (CASE"
                        + "             WHEN pcd.isStatus = 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0)) > 0 THEN 1"
                        + "             WHEN pcd.isStatus = 2 AND (ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0)) > 0 THEN 1"
                        + "             ELSE 0 END) = 1";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pc.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            /*==============================<< Get Grn detail (via list) >>===============================*/
            if (!singlePOFlowInPurchase) {
                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                            && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {
                        if (!conditionLines.trim().equals("")) {
                            conditionLines += " OR ";
                        }
                        conditionLines += " ( pcd.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "'";
                        conditionLines += " AND pcd.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "')";
                    }
                }
            } else {
                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                            && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {
                        if (!conditionLines.trim().equals("")) {
                            conditionLines += " OR ";
                        }
                        conditionLines += " ( pcd.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "')";
                    }
                }
            }

            if (!DmShared.xNull(conditionLines).trim().equalsIgnoreCase("")) {
                condition += " AND (" + conditionLines + ")";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT"
                    + "  pcd.itemCode"
                    + " , pcd.oh_Amt_Net"
                    + " , pcd.mrp"
                    + " , pcd.skuMrp"
                    + " , pcd.rate"
                    + " , pcd.saleRate"
                    + " , pcd.skuSaleRate"
                    + " , pcd.batchNo"
                    + " , pcd.ibCode"
                    + " , pcd.barcode"
                    + " , pcd.expiryDate"
                    + " , pcd.manufacturingDate"
                    + " , im.name AS itemName"
                    + " , im.itemGstNature"
                    + " , im.itsBasedOn"
                    + " , pcd.hsnCode"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , um.code AS unitCode"
                    + " , um.unit AS unitName"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , im.purchaseUnitConversionRate"
                    + " , pc.docId AS pcDocId"
                    + " , pc.recId AS pcRecId"
                    + " , pcd.v_SNo AS pcV_Sno"
                    + " , pc.pChallanNo AS pcRefNo"
                    + " , pc.pChallanDate AS pcRefDate"
                    + " , pc.departmentCode AS pcDepartmentCode"
                    + " , po.docId AS poDocId"
                    + " , po.recId AS poRecId"
                    + " , pod.v_Sno AS poV_SNo"
                    + " , po.refDate AS poRefDate" + pendingQty + pendingFreeQty
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.pcDocId, pbd.pcV_SNo, pbd.acceptQty, pbd.freeQty"
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + " ) AS pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNO = pbd.pcV_SNo";
            }
            sqlQuery += " LEFT JOIN pOrderDetail pod ON pcd.poDocId = pod.docId AND pcd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + "	WHERE pc.subCode = :subCode"
                    + " AND pc.site_Code = :site_Code"
                    + " AND ISNULL(pc.approvedBy, '') <> '' AND isNull(pcd.isFooter,0)=0" + condition
                    + " ORDER BY pc.recId, im.name";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(subCode).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(docId));
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item 
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                //SKU
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purchaseUnitDecimalPlaces")));

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));

                //Challan Details
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));
                node.setPcRefNo(DmShared.xNull(rsTemp.getString("pcRefNo")));
                node.setPcRefDate(DmShared.dNull(rsTemp.getDate("pcRefDate")));
                node.setPcDepartmentCode(DmShared.xNull(rsTemp.getString("pcDepartmentCode")));

                //Quantities
                node.setDocQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                double skuQty = (DmShared.vNull(rsTemp.getString("pendingQty")) + DmShared.vNull(rsTemp.getString("pendingFreeQty"))) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                //Rate
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                //ITS
                DmResult<List<OverHeadITS_Mdl>> dmrIts;
                dmrIts = getITS(node.getSharedKeyFieldCode(), node.getRate(), location, node.getItsBasedOn(), billDate);
                node.setLstITS(dmrIts.getData());
                node.setGrossAcCode(dmrIts.getValue1());
                node.setGrossAcName(dmrIts.getValue2());

                //OHL GRN
                node.setOHLListGRN(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(node.getPcDocId()), DmShared.xNull(node.getPcV_SNo()), cnTemp));

                //Add Node
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                documentApproval(false, WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(false, WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private WalkThroughDefault_Mdl getWalkThroughFor(PurchaseBill_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(false, walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    private void documentApproval(boolean isAutoApprove, WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        boolean flag = true;
        try {

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("pbDocId", "finPayReqEnt", keyCode, cnTemp);
            if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                try {
                    checkPBillLocationAccounts(keyCode, cnTemp);
                } catch (DmException excp) {
                    if (!isAutoApprove) {
                        throw excp;
                    } else {
                        flag = false;
                    }
                }
            }
            if (flag) {

                sqlQuery = "select approvedBy,approvalDateTime,"
                        + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                        + "from pBill where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                psTemp.setString("docId", DmShared.xNull(keyCode));
                psTemp.setString("userName", userName);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                        if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                            rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                            rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = 1"
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = " Update pBill set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Document already approved.");
                        }
                    } else {
                        if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                            if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                                rsTemp.updateString("approvedBy", null);
                                rsTemp.updateDate("approvalDateTime", null);
                                rsTemp.updateRow();
                                rsTemp.close();
                                psTemp.close();

                                sqlQuery = "UPDATE ledger SET"
                                        + " isPosted = 0"
                                        + " WHERE docId = :docId";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", DmShared.xNull(keyCode));
                                psTemp.executeUpdate();
                                psTemp.close();

                                sqlQuery = " Update pBill set"
                                        + " u_EntDt_LatestLine=:uEntDt"
                                        + " where docId=:docId ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", DmShared.xNull(keyCode));
                                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                                psTemp.executeUpdate();
                                psTemp.close();

                            } else {
                                throw new Exception("Unauthorized user to unapprove.");
                            }

                        } else {
                            throw new Exception("Document already unapproved.");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            Gst_Server.manageGstTransRecord(keyCode, walkThroughMove == WalkThroughEnum.ACCEPT, cnTemp);

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
    }
    //</editor-fold>

    @Override
    public DmResult<String[]> getServerDateTime() {
        DmResult<String[]> dmrTemp = new DmResult<String[]>();
        try {
            dmrTemp.setData(DmServer.getServerDateTime());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        }
        return dmrTemp;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setSiteCode(DmServer.getSsnSiteCode(this));
            defaultValue.getLstSaleRateRule().addAll(STServer.getSaleRateRule(cnTemp));
            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equals(""));

            /*==============================<< Get default voucher_Type >>===============================*/
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " , vt.category"
                    + " , vt.balancingAc, sg.name as balancingAcName"
                    + " FROM voucher_Type vt"
                    + " left JOin subgroup sg on vt.balancingAc=sg.subCode "
                    //                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_BILL + "'"
                    + " WHERE vt.category = :category "
                    + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")) {
                psTemp.setString("category", pvc.PHARMACY_PURCHASE_BILL);
            } else {
                psTemp.setString("category", pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                defaultValue.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                defaultValue.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                defaultValue.setBalancingAcName(DmShared.xNull(rsTemp.getString("balancingAcName")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get default department >>===============================*/
            sqlQuery = "SELECT"
                    + "   dm.subCode AS code"
                    + " , sg.displayName AS name"
                    + " FROM departmentMast dm"
                    + " LEFT JOIN subGroup sg ON dm.subCode = sg.subCode"
                    + " WHERE sg.IsActive = 1"
                    + " AND dm.subCode = :depCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            /*==========================<< Get Site GST Details >>==========================*/
            sqlQuery = "SELECT "
                    + "   si.gstin AS siteGstin"
                    + " , si.gstinDate AS siteGstinDate"
                    + " , si.registrationType AS siteRegistrationType"
                    + " , st.code AS siteStateCode"
                    + " , st.isUT AS isSiteStateUT"
                    + " FROM siteMast si"
                    + " INNER JOIN stateMast st ON si.stateNumericCode = st.numericCode"
                    + " WHERE si.code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSiteGstin(DmShared.xNull(rsTemp.getString("siteGstin")));
                defaultValue.setSiteGstinDate(DmShared.dNull(rsTemp.getDate("siteGstinDate")));
                defaultValue.setSiteRegistrationType(DmShared.xNull(rsTemp.getString("siteRegistrationType")));
                defaultValue.setSiteStateCode(DmShared.xNull(rsTemp.getString("siteStateCode")));
                defaultValue.setIsSiteStateUT(DmShared.xNull(rsTemp.getString("isSiteStateUT")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.SinglePOFlowInPurchase, \n"
                    + " isNull(e.ac_TDS_Applicable_On,0) as ac_TDS_Applicable_On "
                    + "FROM enviro e\n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isIncludeTaxInCost") == null) {
                    throw new DmException(0, "Include Tax in Cost is not defined in Environment Setting.");
                } else if (rsTemp.getString("purchaseSalesAcPostingBasedOn") == null) {
                    throw new DmException(0, "Purchase/Sales A/c Posting Based On is not defined in Environment Setting.");
                } else if (rsTemp.getString("singlePOFlowInPurchase") == null) {
                    throw new DmException(0, "Single PO Flow In Purchase is not defined in Environment Setting.");
                }
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setPurchaseAcPostingBasedOn(DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")));
                defaultValue.setSinglePOFlowInPurchase(DmShared.iNull(rsTemp.getString("singlePOFlowInPurchase")).intValue() == 1);
                defaultValue.setTdsOn(DmShared.xNull(rsTemp.getString("ac_TDS_Applicable_On")));
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< Get Enviro Company Data >>====================================*/
            sqlQuery = "SELECT ec.isInventoryAccountingActive \n"
                    + "FROM enviroCompany ec\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isInventoryAccountingActive") == null) {
                    throw new DmException(0, "Inventory Accounting Active is not defined in Environment Setting (Company).");
                }
                defaultValue.setInventoryAccountingActive(DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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
    public DmResult<List<GrdBillOtherDetails_Mdl>> getBillOtherDetails(String pbDocid, String button) {
        DmResult<List<GrdBillOtherDetails_Mdl>> dmrTemp = new DmResult();
        List<GrdBillOtherDetails_Mdl> lstGrd = new ArrayList<GrdBillOtherDetails_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default department >>===============================*/
            switch (button) {
                case "Bill History":
                    billBillHistory(pbDocid, cnTemp, lstGrd);
                    break;
                case "Paym. Details":
                    billFinalPayDetail(pbDocid, cnTemp, lstGrd);
                    break;
                case "Adv. Details":
                    billAdvDetail(pbDocid, cnTemp, lstGrd);
                    break;
            }

            dmrTemp.setData(lstGrd);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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

    private void billAdvDetail(String pbDocid, Connection cnTemp, List<GrdBillOtherDetails_Mdl> lstGrd) throws SQLException, DmException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdBillOtherDetails_Mdl grdMdl = new GrdBillOtherDetails_Mdl();
        try {


            /*==============================<< Get default department >>===============================*/
//            sqlQuery = "	  Select  CONVERT( varchar,adv.u_EntDt,22) PayDateTime,      \n"
            sqlQuery = "	  Select  REPLACE(CONVERT(VARCHAR(11), adv.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,adv.u_EntDt,100 ) ,7), 6, 0, ' ') PayDateTime,      \n"
                    + " are.advanceReqAmt PayAmount, lg.chq_No, ctm.name payType,     \n"
                    + "  adv.recId payRecid, sg.name Bank\n"
                    + "     from advanceEntry adv\n"
                    + "     left join advanceReqEntry are on are.docid = adv.ReqDocId\n"
                    + "	   left join pOrder po on po.docid=are.poDocId \n"
                    + "	   left join ledger lg on lg.docid= adv.docid \n"
                    + "     \n"
                    + "     left join cardLedger cl on  LG.docId = cl.docid  And cl.V_SNo=LG.V_SNo\n"
                    + "     left join cardTypeMast ctm on ctm.code = cl.cardTypeCode\n"
                    + "     left join subgroup sg on sg.subCode = cl.subCode\n"
                    + "	\n"
                    + "	   left join\n"
                    + "	   (	\n"
                    + "	   select  max(docId) docid, max(podocid) podocId \n"
                    + "	   from  pBilldetail pbd  \n"
                    + "	   where pbd.docid=:pbDocid \n"
                    + "	   )pb on po.docId =pb.poDocId\n"
                    + "	   where pb.docid=:pbDocid and lg.isSystem_Generated=0 and  adv.approvedBy is not null     ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("pbDocid", DmShared.xNull(pbDocid));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdMdl = new GrdBillOtherDetails_Mdl();
                grdMdl.setAmount(rsTemp.getString("PayAmount"));
                grdMdl.setBank(rsTemp.getString("Bank"));
                grdMdl.setBillNo(rsTemp.getString("payRecid"));
                grdMdl.setCheqNo(rsTemp.getString("chq_No"));
                grdMdl.setDateTime(rsTemp.getString("PayDateTime"));
                grdMdl.setFinalPayReq(rsTemp.getString("payRecid"));
                grdMdl.setPayType(rsTemp.getString("payType"));
                lstGrd.add(grdMdl);
            }
            rsTemp.close();
            psTemp.close();

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

    private void billFinalPayDetail(String pbDocid, Connection cnTemp, List<GrdBillOtherDetails_Mdl> lstGrd) throws SQLException, DmException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdBillOtherDetails_Mdl grdMdl = new GrdBillOtherDetails_Mdl();
        try {


            /*==============================<< Get default department >>===============================*/
//            sqlQuery = " Select  CONVERT( varchar,fpe.u_EntDt,22) PayDateTime, "
            sqlQuery = " Select  REPLACE(CONVERT(VARCHAR(11), fpe.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,fpe.u_EntDt,100 ) ,7), 6, 0, ' ') PayDateTime, "
                    + "     fped.finReqAmt PayAmount, lg.chq_No, ctm.name payType, "
                    + "     fpe.recId payRecid, sg.name Bank                                        \n"
                    + "     from finPayReqEnt fpre                                                  \n"
                    + "     left join finPayEntDetail fped on fped.payReqDocId = fpre.docid         \n"
                    + "     left join finPayEnt fpe on fpe.docid = fped.docid                       \n"
                    + "     left join cardLedger cl on cl.docid = fped.docid                        \n"
                    + "     left join cardTypeMast ctm on ctm.code = cl.cardTypeCode                \n"
                    + "     left join ledger lg on lg.docid= fped.docid and lg.subcode =fpre.supplierCode "
                    + "     and lg.contraSub= cl.subCode                                            \n"
                    + "     left join subgroup sg on sg.subCode = cl.subCode                    \n"
                    + "     where fpre.pbDocId=:pbDocid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("pbDocid", DmShared.xNull(pbDocid));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdMdl = new GrdBillOtherDetails_Mdl();
                grdMdl.setAmount(rsTemp.getString("PayAmount"));
                grdMdl.setBank(rsTemp.getString("Bank"));
                grdMdl.setBillNo(rsTemp.getString("payRecid"));
                grdMdl.setCheqNo(rsTemp.getString("chq_No"));
                grdMdl.setDateTime(rsTemp.getString("PayDateTime"));
                grdMdl.setFinalPayReq(rsTemp.getString("payRecid"));
                grdMdl.setPayType(rsTemp.getString("payType"));
                lstGrd.add(grdMdl);
            }
            rsTemp.close();
            psTemp.close();

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

    private void billBillHistory(String pbDocid, Connection cnTemp, List<GrdBillOtherDetails_Mdl> lstGrd) throws SQLException, DmException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdBillOtherDetails_Mdl grdMdl = new GrdBillOtherDetails_Mdl();
        try {


            /*==============================<< Get default department >>===============================*/
//            sqlQuery = " select convert(varchar,bh.u_EntDt,22) dateTime,bh.recId, "
            sqlQuery = " select REPLACE(CONVERT(VARCHAR(11), bh.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,bh.u_EntDt,100 ) ,7), 6, 0, ' ') dateTime, bh.recId, "
                    //            sqlQuery = " select convert(varchar,bh.u_EntDt,22) dateTime,bh.recId, "
                    + "     bhlmf.mappMod fromLoc, "
                    + "     bhlmt.mappMod toLoc, bhd.remark                             \n"
                    + "	 from billHandleDetail bhd                                      \n"
                    + "	 left join billHandle bh on bh.docid = bhd.docId                \n"
                    + "	 left join billHandleLocationMaster bhlmf on bhlmf.code = bh.fromLocCode                \n"
                    + "	 left join billHandleLocationMaster bhlmt on bhlmt.code = bh.toLocCode              \n"
                    + "	 where modDocId=:pbDocid                            ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("pbDocid", DmShared.xNull(pbDocid));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdMdl = new GrdBillOtherDetails_Mdl();
                grdMdl.setDateTime(rsTemp.getString("dateTime"));
                grdMdl.setBillNo(rsTemp.getString("recId"));
                grdMdl.setFromLocation(rsTemp.getString("fromLoc"));
                grdMdl.setToLocation(rsTemp.getString("toLoc"));
                grdMdl.setRemark(rsTemp.getString("remark"));
                lstGrd.add(grdMdl);
            }
            rsTemp.close();
            psTemp.close();

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

    @Override
    public DmResult<String> saveRecord(PurchaseBill_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "",
                roundOffItemGstNature = "", invoiceCGST = "", invoiceSGST = "", invoiceIGST = "",
                invoiceUGST = "", invoiceValue = "";
        ResultSet rsTemp = null;
        GstTrans_Mdl gstTrans_Mdl = null;
        dmrTemp.setData("");

        try {
            PurchaseBill_Vld.chkSave(mrTemp);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getListItemDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");
            if (mrTemp.getDefaultValueMdl().isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getVoucherDate(), "", cnTemp);
            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), mrTemp.getVoucherTypeCode(), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Check for Approvedby if already approved >>====================================*/
            sqlQuery = "select approvedBy from pBill where  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't Edit this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Check for LatestEntry >>====================================*/
            sqlQuery = "SELECT u_EntDt_LatestLine FROM pBill WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(mrTemp.getU_EntDt_LatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                        throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check for duplicate party bill no >>====================================*/
            sqlQuery = " SELECT COUNT(pb.docid) AS cnt "
                    + " FROM pBill pb  with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pb.v_Type "
                    + " WHERE  pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode "
                    + " AND pb.v_Prefix = :v_Prefix "
                    + " AND pb.docId != :docId ";
//                    + " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("partyBillNo", DmShared.xNull(mrTemp.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Entry already exists with this Reference Number.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for GRN item change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM pChallanDetail pcd " + DmShared.xNull(mrTemp.getGrnItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "GRN has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check for Round Off Item in Enviro >>====================================*/
            sqlQuery = "SELECT ohm.itemCode, im.hsnCode, im.itemGSTNature"
                    + " FROM overHeadMast ohm"
                    + " LEFT JOIN itemMast im ON ohm.itemCode = im.code"
                    + " WHERE ohm.manualCode = 'ROFF'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                roundOffItemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                roundOffItemHsnCode = DmShared.xNull(rsTemp.getString("hsnCode"));
                roundOffItemGstNature = DmShared.xNull(rsTemp.getString("itemGSTNature"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(roundOffItemCode).trim().equals("")) {
                throw new DmException(0, "Round Off Item not defined in Overhead Master.");
            }

            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert record into quotation >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pBill ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, partyBillNo, partyBillDate, "
                        + "     calcOn, subCode, consignee, partyType, rcmApplicable, placeOfSupply,"
                        + "     remark, wtaCode, guiConfigCode, site_Code, preparedBy, u_EntDt, u_EntDt_LatestLine, transferDt,PaymentDueDate, tdsAmt)"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, :partyBillNo, :partyBillDate, "
                        + "     :calcOn, :subCode, :consignee, :partyType, :rcmApplicable, :placeOfSupply,"
                        + "     :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :uEntDt, :uEntDt, :transferDt,:PaymentDueDate, :tdsAmt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTemp.getObjRecId().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("partyBillNo", DmShared.xNull(mrTemp.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTemp.getSupplierBillDate()));
                psTemp.setString("calcOn", DmShared.iNull(mrTemp.getCalculateOnTag()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setDate("PaymentDueDate", DmServer.convertDate(mrTemp.getPaymentDueDate()));
                psTemp.setString("tdsAmt", DmServer.vNull(mrTemp.getTdsAmt(), "0.00"));
            } else {
                sqlQuery = "UPDATE pBill SET"
                        + "  v_Date = :v_Date"
                        + " ,partyBillNo = :partyBillNo"
                        + " ,partyBillDate = :partyBillDate"
                        + " ,calcOn = :calcOn"
                        + " ,subCode = :subCode"
                        + " ,consignee = :consignee"
                        + " ,partyType = :partyType"
                        + " ,rcmApplicable = :rcmApplicable"
                        + " ,placeOfSupply = :placeOfSupply"
                        + " ,remark = :remark"
                        + " ,u_EntDt = :uEntDt"
                        + " ,u_EntDt_LatestLine = :uEntDt"
                        + " ,u_Name = :u_Name"
                        + " ,transferDt = :transferDt"
                        + " , PaymentDueDate = :PaymentDueDate \n"
                        + "  , tdsAmt = :tdsAmt \n"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTemp.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTemp.getSupplierBillDate()));
                psTemp.setString("calcOn", DmShared.iNull(mrTemp.getCalculateOnTag()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setDate("PaymentDueDate", DmServer.convertDate(mrTemp.getPaymentDueDate()));
                psTemp.setString("tdsAmt", DmServer.vNull(mrTemp.getTdsAmt(), "0.00"));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //====================================Re-check-PartyBillNo=================================
            /*==============================<< Check for duplicate party bill no >>====================================*/
            sqlQuery = " SELECT COUNT(pb.docid) AS cnt "
                    + " FROM pBill pb  with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pb.v_Type "
                    + " WHERE  pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode"
                    + " AND pb.docId != :docId "
                    + " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("partyBillNo", DmShared.xNull(mrTemp.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierTag()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Entry already exists with this Reference Number.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Manage GRN (Billed Qty) >>====================================*/
            manageBilledQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), true, cnTemp);

            /*==============================<< Delete records from pBillDetail >>====================================*/
            sqlQuery = "Delete From pBillDetail Where docId = :docId AND isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into pBillDetail >>====================================*/
            List<GrdItemDetail_Mdl> listDetail = mrTemp.getListItemDetail();
            int vSNo = 0;
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
//                    if (DmShared.iNull(detail_Mdl.getvSNo()).intValue() > vSNo) {
//                        vSNo = DmShared.iNull(detail_Mdl.getvSNo()).intValue();
//                    } else {
//                        ++vSNo;
//                    }
                    ++vSNo;
                    if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pBillDetail ("
                                + "     docId, v_SNo, pcDocId, pcV_SNo, poDocId, poV_SNo, itemCode, hsnCode, itemGSTNature, batchNo, ibCode, barcode, manufacturingDate, expiryDate, "
                                + "     mrp, skuMrp, unitCode, docQty, recQty, acceptQty, freeQty, skuQty, skuQtyForRate, rate, saleRate, skuSaleRate)"
                                + " Values ("
                                + "     :docId, :v_SNo, :pcDocId, :pcV_SNo, :poDocId, :poV_SNo, :itemCode, :hsnCode, :itemGSTNature, :batchNo, :ibCode, :barcode, :manufacturingDate, :expiryDate, "
                                + "     :mrp, :skuMrp, :unitCode, :docQty, :recQty, :acceptQty, :freeQty, :skuQty, :skuQtyForRate, :rate, :saleRate, :skuSaleRate)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).toString());
                        psTemp.setString("pcDocId", DmShared.foriegnKey(detail_Mdl.getPcDocId()));
                        psTemp.setString("pcV_SNo", DmShared.foriegnKey(detail_Mdl.getPcV_SNo()));
                        psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                        psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                        psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                        psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                        psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                        psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                        psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                        psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                        psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getDocQty()));
                        psTemp.setString("recQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                        psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                        psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQty()));
                        psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                        psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate(), "0.00"));
                        psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }

            /*==============================<< Manage GRN (Billed Qty) >>====================================*/
            manageBilledQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), false, cnTemp);

            /*==============================<< Insert records into OH >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTemp.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
//            OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", "", "", "", "", false, true, cnTemp);
            OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", DmShared.xNull(mrTemp.getSupplierBillNo()), DmShared.dNull(mrTemp.getSupplierBillDate()), "", "", false, true, cnTemp);

            /*==============================<< Get header tax details >>====================================*/
            sqlQuery = "SELECT"
                    + "   pb.oh_Amt_CGST"
                    + " , pb.oh_Amt_SGST"
                    + " , pb.oh_Amt_IGST"
                    + " , pb.oh_Amt_UGST"
                    + " , pb.oh_Amt_Net"
                    + " FROM pBill pb"
                    + " WHERE pb.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                invoiceCGST = DmShared.xNull(rsTemp.getString("oh_Amt_CGST"));
                invoiceSGST = DmShared.xNull(rsTemp.getString("oh_Amt_SGST"));
                invoiceIGST = DmShared.xNull(rsTemp.getString("oh_Amt_IGST"));
                invoiceUGST = DmShared.xNull(rsTemp.getString("oh_Amt_UGST"));
                invoiceValue = DmShared.xNull(rsTemp.getString("oh_Amt_Net"));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Insert Round Off Line >>====================================*/
            if (DmShared.vNull(mrTemp.getRoundOffAmount()) != 0) {
                sqlQuery = "INSERT INTO pBillDetail ("
                        + "     docId, v_SNo, itemCode, hsnCode, docQty, acceptQty, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter)"
                        + " Values ("
                        + "     :docId, :v_SNo, :itemCode, :hsnCode, :docQty, :acceptQty, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, 1)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_SNo", String.valueOf(++vSNo));
                psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                if (DmShared.vNull(mrTemp.getRoundOffAmount()) < 0) {
                    psTemp.setString("docQty", "-1");
                    psTemp.setString("acceptQty", "-1");
                } else {
                    psTemp.setString("docQty", "1");
                    psTemp.setString("acceptQty", "1");
                }
                psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTemp.getRoundOffAmount())), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Update isoh_amt_netzero in pbill Table >>====================================*/
            if (DmShared.vNull(mrTemp.getNetAmount()) == 0) {
                sqlQuery = "UPDATE pBill"
                        + " SET isoh_amt_netzero = 1"
                        + " where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Delete records from gstTrans >>====================================*/
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Insert records into gstTrans >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    //<editor-fold defaultstate="collapsed" desc="Set gstTrans_Mdl">
                    gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(DmShared.xNull(detail_Mdl.getvSNo()));
                    gstTrans_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTemp.getVoucherDate()));
                    gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTemp.getSupplierBillNo()));
                    gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTemp.getSupplierBillDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTemp.getSupplierTag()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTemp.getSupplierTag()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTemp.getSupplierGstin()));
                    gstTrans_Mdl.setGstinDate(DmShared.xNull(mrTemp.getSupplierGstinDate()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTemp.getPlaceOfSupply()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTemp.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(detail_Mdl.getHsnCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    gstTrans_Mdl.setDepartmentCode(DmShared.xNull(detail_Mdl.getPcDepartmentCode()));
                    gstTrans_Mdl.setQty(DmShared.iNull(mrTemp.getCalculateOnTag()).intValue() == 1
                            ? DmServer.vNull(DmShared.vNull(detail_Mdl.getAcceptQty()) + DmShared.vNull(detail_Mdl.getFreeQty()), DmShared.getNumberFormat(DmShared.iNull(detail_Mdl.getPurchaseUnitDecimalPlaces()).intValue()))
                            : DmServer.vNull(DmShared.vNull(detail_Mdl.getDocQty()) + DmShared.vNull(detail_Mdl.getFreeQty()), DmShared.getNumberFormat(DmShared.iNull(detail_Mdl.getPurchaseUnitDecimalPlaces()).intValue())));
                    gstTrans_Mdl.setGstUnitCode(DmShared.xNull(detail_Mdl.getGstUnitCode()));
                    gstTrans_Mdl.setInvoiceCGST(DmShared.xNull(invoiceCGST));
                    gstTrans_Mdl.setInvoiceSGST(DmShared.xNull(invoiceSGST));
                    gstTrans_Mdl.setInvoiceIGST(DmShared.xNull(invoiceIGST));
                    gstTrans_Mdl.setInvoiceUGST(DmShared.xNull(invoiceUGST));
                    gstTrans_Mdl.setInvoiceValue(DmShared.xNull(invoiceValue));
                    gstTrans_Mdl.setIsTaxInclusive("0");
                    gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.INPUT.toInt()));
                    gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
                    gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                    gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                    gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                    gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.INPUT.toInt()));

                    sqlQuery = "SELECT"
                            + "   pbd.oh_Amt_Gross"
                            + " , pbd.oh_Amt_Taxable"
                            + " , pbd.oh_at_CGST"
                            + " , pbd.oh_Amt_CGST"
                            + " , pbd.oh_at_SGST"
                            + " , pbd.oh_Amt_SGST"
                            + " , pbd.oh_at_IGST"
                            + " , pbd.oh_Amt_IGST"
                            + " , pbd.oh_at_UGST"
                            + " , pbd.oh_Amt_UGST"
                            + " , pbd.oh_Amt_Net"
                            + " FROM pBillDetail pbd"
                            + " WHERE pbd.docId = :docId"
                            + " AND pbd.v_Sno = :v_Sno";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_Sno", DmShared.iNull(detail_Mdl.getvSNo()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gstTrans_Mdl.setItemGrossAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Gross")));
                        gstTrans_Mdl.setItemTaxableAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Taxable")));
                        gstTrans_Mdl.setItemCgstRate(DmShared.xNull(rsTemp.getString("oh_at_CGST")));
                        gstTrans_Mdl.setItemCgstAmount(DmShared.xNull(rsTemp.getString("oh_Amt_CGST")));
                        gstTrans_Mdl.setItemSgstRate(DmShared.xNull(rsTemp.getString("oh_at_SGST")));
                        gstTrans_Mdl.setItemSgstAmout(DmShared.xNull(rsTemp.getString("oh_Amt_SGST")));
                        gstTrans_Mdl.setItemIgstRate(DmShared.xNull(rsTemp.getString("oh_at_IGST")));
                        gstTrans_Mdl.setItemIgstAmount(DmShared.xNull(rsTemp.getString("oh_Amt_IGST")));
                        gstTrans_Mdl.setItemUgstRate(DmShared.xNull(rsTemp.getString("oh_at_UGST")));
                        gstTrans_Mdl.setItemUgstAmount(DmShared.xNull(rsTemp.getString("oh_Amt_UGST")));
                        gstTrans_Mdl.setItemNetAmount(DmShared.xNull(rsTemp.getString("oh_Amt_Net")));
                    }
                    rsTemp.close();
                    psTemp.close();
                    //</editor-fold>
                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
                }
            }

//            if (DmShared.vNull(mrTemp.getRoundOffAmount()) != 0) {
//                //<editor-fold defaultstate="collapsed" desc="Insert RoundOff line in gstTrans">
//                gstTrans_Mdl = new GstTrans_Mdl();
//                gstTrans_Mdl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                gstTrans_Mdl.setV_Sno(String.valueOf(vSNo));
//                gstTrans_Mdl.setSite_Code(DmShared.xNull(siteCode));
//                gstTrans_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
//                gstTrans_Mdl.setRecID(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
//                gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
//                gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
//                gstTrans_Mdl.setV_Date(DmShared.xNull(mrTemp.getVoucherDate()));
//                gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTemp.getSupplierBillNo()));
//                gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTemp.getSupplierBillDate()));
//                gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTemp.getSupplierTag()));
//                gstTrans_Mdl.setConsignee(DmShared.xNull(mrTemp.getSupplierTag()));
//                gstTrans_Mdl.setPartyType(DmShared.xNull(mrTemp.getPartyTypeForEntry()));
//                gstTrans_Mdl.setGstin(DmShared.xNull(mrTemp.getSupplierGstin()));
//                gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTemp.getPlaceOfSupply()));
//                gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTemp.getRcmApplicable()));
//                gstTrans_Mdl.setItemCode(DmShared.xNull(roundOffItemCode));
//                gstTrans_Mdl.setHsnCode(DmShared.xNull(roundOffItemHsnCode));
//                gstTrans_Mdl.setItemGSTNature(DmShared.xNull(roundOffItemGstNature));
//                if (DmShared.vNull(mrTemp.getRoundOffAmount()) < 0) {
//                    gstTrans_Mdl.setQty("-1");
//                } else {
//                    gstTrans_Mdl.setQty("1");
//                }
//                gstTrans_Mdl.setItemGrossAmount(DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
//                gstTrans_Mdl.setItemTaxableAmount(DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
//                gstTrans_Mdl.setItemNetAmount(DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
//                gstTrans_Mdl.setInvoiceCGST(DmShared.xNull(invoiceCGST));
//                gstTrans_Mdl.setInvoiceSGST(DmShared.xNull(invoiceSGST));
//                gstTrans_Mdl.setInvoiceIGST(DmShared.xNull(invoiceIGST));
//                gstTrans_Mdl.setInvoiceUGST(DmShared.xNull(invoiceUGST));
//                gstTrans_Mdl.setInvoiceValue(DmShared.xNull(invoiceValue));
//                gstTrans_Mdl.setIsTaxInclusive("0");
//                gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.INPUT.toInt()));
//                gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
//                gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
//                gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
//                gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
//                gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.INPUT.toInt()));
//                Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
//                //</editor-fold>
//            }

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(true, WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (!blnAdd && mrTemp.getModifyReasonMdl() != null) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
            }

            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            //=================================================<TDS>=========================
            if (mrTemp.getTdsCatMdl() != null) {
                mrTemp.getTdsCatMdl().setSharedKeyFieldCode(mrTemp.getSharedKeyFieldCode());
                mrTemp.getTdsCatMdl().setRecId(mrTemp.getObjRecId());
                mrTemp.getTdsCatMdl().setVoucherDate(mrTemp.getVoucherDate());
                mrTemp.getTdsCatMdl().setVoucherPrefix(voucherPrefix);
                mrTemp.getTdsCatMdl().setVoucherTypeCode(mrTemp.getVoucherTypeCode());
                TdsCategoryPopup_SrvImpl.saveTDS(mrTemp.getTdsCatMdl(), siteCode, cnTemp);

            }

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getVoucherTypeCode(), this);
            }

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
    public DmResult<PurchaseBill_Mdl> getMoveRec(String strKey) {
        DmResult<PurchaseBill_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PurchaseBill_Mdl mrTemp = new PurchaseBill_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT MAX(v_Date) as lastTransactionDate FROM stock ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setLastTransactionDate(DmShared.dNull(rsTemp.getDate("lastTransactionDate")));
            }

            rsTemp.close();
            psTemp.close();

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT pb.*"
                    + " , vt.description AS voucherTypeName"
                    + " , vt.category"
                    + " , sgs.name AS supplierName"
                    + " , c.stateCode AS supplierStateCode"
                    + " , sgs.partyType AS supplierType"
                    + " , sgs.gstin AS supplierGstin"
                    + " , sgs.gstinDate AS supplierGstinDate"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sgs.cityCode"
                    + " WHERE pb.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));

                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setPaymentDueDate(DmShared.dNull(rsTemp.getDate("paymentDueDate")));
                mrTemp.setSupplierBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                mrTemp.setSupplierBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setCalculateOnTag(DmServer.vNull(rsTemp.getString("calcOn"), "0"));
                mrTemp.setCalculateOnText(DmShared.xNull(rsTemp.getString("calcOn")).equalsIgnoreCase("1") ? "Accept Quantity"
                        : DmShared.xNull(rsTemp.getString("calcOn")).equalsIgnoreCase("0") ? "Document Quantity" : "");
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Medicine grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   pbd.*"
                    + " , po.recId AS poRecId"
                    + " , pc.docId AS pcDocId"
                    + " , pc.RecId AS pcRecid"
                    + " , pc.pChallanNo AS pcRefNo"
                    + " , pc.pChallanDate AS pcRefDate"
                    + " , pc.departmentCode AS pcDepartmentCode"
                    + " , pcd.V_SNo AS pcV_SNo"
                    + " , im.name AS itemName"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , ums.decimalPlaces AS skuAllowDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , um.unit AS unitName"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty"
                    + " , ISNULL(pcd.freeQty,0) - ISNULL(pcd.billedFreeQty,0) + ISNULL(pbd.freeQty,0) AS pendingFreeQty"
                    + " FROM pBillDetail pbd"
                    + " LEFT JOIN pOrder po ON pbd.poDocId = po.docId"
                    + " LEFT JOIN pOrderDetail pod ON pbd.poDocId = pod.docId AND pbd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pChallan pc ON pbd.pcDocId = pc.docId"
                    + " LEFT JOIN pChallanDetail pcd ON pbd.pcDocId = pcd.docId AND pbd.pcV_SNo = pcd.v_SNo"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code"
                    + " WHERE pbd.docId = :docId"
                    + " AND ISNULL(pbd.isFooter, 0) = 0"
                    + " ORDER BY pbd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                //SKU
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuAllowDecimalPlaces")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double acceptQty = DmShared.vNull(rsTemp.getString("acceptQty"));
                double freeQty = DmShared.vNull(rsTemp.getString("freeQty"));
                double purchaseUnitConversionRate = (skuQty / (acceptQty + freeQty));
                node.setPurchaseUnitConversionRate(DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
                node.setPurchaseUnitConversionRate(DmServer.vNull(node.getPurchaseUnitConversionRate()));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));

                //Challan Detail
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));
                node.setPcRefNo(DmShared.xNull(rsTemp.getString("pcRefNo")));
                node.setPcRefDate(DmShared.dNull(rsTemp.getDate("pcRefDate")));
                node.setPcDepartmentCode(DmShared.xNull(rsTemp.getString("pcDepartmentCode")));

                //Quantities
                node.setDocQty(DmServer.vNull(rsTemp.getString("docQty")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setReturnedQty(DmServer.vNull(rsTemp.getString("returnedQty")));

                //Rate
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                //Other
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());

                /*==============================<< Get OverheadLine detail >>===============================*/
                node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListItemDetail().addAll(lstNode);

            /*==============================<< Get Overhead detail >>===============================*/
            mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));

            //========================================<<TDS Detail>>===========================
            TdsCategoryPopup_SrvImpl.getTDSDetail(strKey, mrTemp.getTdsCatMdl(), cnTemp);

            //=========================================================================================
            dmrTemp.setData(mrTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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
    public DmResult<String> deleteRecord(String keyCode, String guiConfigCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check for approvedBy before deleting data >>===============================*/
            sqlQuery = "SELECT approvedBy, wtaCode FROM pBill WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                wtaCode = DmShared.xNull(rsTemp.getString("wtaCode"));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", keyCode, cnTemp);

            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(keyCode, cnTemp);

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason & Walkthrough >>============================*/
            OverHeadLine_Server.delete(keyCode, cnTemp);
            OverHead_Server.delete(keyCode, cnTemp);
            ModifyReason_Server.delete(keyCode, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);
            Gst_Server.deleteGstTrans(keyCode, cnTemp);

            /*==============================<< Update record in GRN>>===============================*/
            manageBilledQty(DmShared.iNull(keyCode).toString(), true, cnTemp);

            /*==============================<< Delete record from pBillDetail >>===============================*/
            sqlQuery = "DELETE FROM pBillDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pBill >>===============================*/
            sqlQuery = "DELETE FROM pBill WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT pb.docId"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "')"
                    + " AND pb.site_Code = :site_Code"
                    + " AND pb.v_Prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY pb.v_Date DESC, vt.short_Name, pb.v_No DESC, pb.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   pb.docId"
                    + " , pb.recId"
                    + " , pb.v_Date"
                    + " , pb.v_No"
                    + " , sg.name AS supplierName"
                    + " , vt.short_Name"
                    + " , pb.partyBillNo"
                    + " , pb.partyBillDate"
                    + " , pb.approvedBy"
                    + " , format(pb.approvalDateTime,'dd-MMM-yyyy HH.mm') AS approvalDateTime"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sg ON pb.subCode = sg.subCode"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "')"
                    + " AND pb.site_Code = :site_Code"
                    + " AND pb.v_Prefix = :vPrefix";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setBillDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setBillType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setSupplierBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                listNode.setSupplierBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
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
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            dmrTemp = STServer.getITS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, STEnum.ITSType.PURCHASE, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
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

    private void manageBilledQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, pcDocId = "";
        double pbAcceptQty, pbFreeQty, pcBilledQty, pcBilledFreeQty, pcAcceptQty, pcFreeQty;
        Integer isStatus;

        try {
            sqlQuery = "SELECT"
                    + "   pc.docId AS pcDocId"
                    + " , pbd.pcV_SNo"
                    + " , pbd.acceptQty AS pbAcceptQty"
                    + " , pbd1.freeQty AS pbFreeQty"
                    + " , pcd.acceptQty AS pcAcceptQty"
                    + " , pcd.freeQty AS pcFreeQty"
                    + " , pcd.billedQty AS pcBilledQty"
                    + " , pcd.billedFreeQty AS pcBilledFreeQty"
                    + " , pcd.isStatus"
                    + " , pc.approvedBy"
                    + " , pc.recId AS pcNo"
                    + " , vt.short_name AS pcType"
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN voucher_type vt ON pc.v_Type = vt.v_type"
                    + " INNER JOIN ("
                    + "     SELECT pbd.pcDocId, pbd.pcV_SNo, sum(pbd.acceptQty) AS acceptQty"
                    + "     FROM pBillDetail pbd"
                    + "     WHERE pbd.docId = :docId"
                    + "     GROUP BY pbd.pcDocId, pbd.pcV_SNo"
                    + " ) pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNo = pbd.pcV_SNo"
                    + " INNER JOIN ("
                    + "     SELECT pbd.pcDocId, pbd.pcV_SNo, sum(pbd.freeQty) AS freeQty"
                    + "     FROM pBillDetail pbd"
                    + "     WHERE pbd.docId = :docId"
                    + "     GROUP BY pbd.pcDocId, pbd.pcV_SNo"
                    + " ) pbd1 ON pcd.docId = pbd1.pcDocId AND pcd.v_SNo = pbd1.pcV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN is Approved");
                }
                pcBilledQty = rsTemp.getDouble("pcBilledQty");
                pcBilledFreeQty = rsTemp.getDouble("pcBilledFreeQty");
                pbAcceptQty = rsTemp.getDouble("pbAcceptQty");
                pbFreeQty = rsTemp.getDouble("pbFreeQty");
                pcAcceptQty = rsTemp.getDouble("pcAcceptQty");
                pcFreeQty = rsTemp.getDouble("pcFreeQty");

                if (isDelete) {
                    pcBilledQty -= pbAcceptQty;
                    pcBilledFreeQty -= pbFreeQty;
                } else {
                    pcBilledQty += pbAcceptQty;
                    pcBilledFreeQty += pbFreeQty;
                }

                if (pcBilledQty > pcAcceptQty) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN overflows by " + DmServer.vNull(pcBilledQty - pcAcceptQty));
                } else if (pcBilledQty < 0) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN overflows by " + DmServer.vNull(pcBilledQty));
                } else if (pcBilledQty == pcAcceptQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (pcBilledFreeQty > pcFreeQty) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN overflows free qty by " + DmServer.vNull(pcBilledFreeQty - pcFreeQty));
                } else if (pcBilledFreeQty < 0) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN overflows free qty by " + DmServer.vNull(pcBilledFreeQty));
                }

                if (!pcDocId.equalsIgnoreCase("")) {
                    pcDocId += ",";
                }
                pcDocId += DmShared.iNull(rsTemp.getString("pcDocId")).toString();

                sqlQuery = "UPDATE pChallanDetail SET"
                        + " isStatus = :isStatus,"
                        + " billedQty = :billedQty,"
                        + " billedFreeQty = :billedFreeQty"
                        + " WHERE docId = :pcDocId AND v_SNo = :pcV_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("pcDocId", DmShared.xNull(rsTemp.getString("pcDocId")));
                psTemp1.setString("pcV_SNo", DmShared.xNull(rsTemp.getString("pcV_SNo")));
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("billedQty", DmServer.vNull(pcBilledQty, "0.0000000"));
                psTemp1.setString("billedFreeQty", DmServer.vNull(pcBilledFreeQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!pcDocId.equalsIgnoreCase("")) {
                sqlQuery = "UPDATE pChallan SET u_EntDt_LatestLine = :uEntDt where docId in (" + pcDocId + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    @Override
    public DmResult<String> updateRecord(PurchaseBill_Mdl mrTempCurrent, boolean isApprove) {
        {
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

                /*==============================<< Check for duplicate party bill no >>====================================*/
                sqlQuery = " SELECT COUNT(pb.docid) AS cnt "
                        + " FROM pBill pb with (nolock) "
                        + " INNER JOIN voucher_type vt ON vt.v_Type = pb.v_Type "
                        + " WHERE  pb.partyBillNo = :partyBillNo "
                        + " AND pb.subCode=:subCode"
                        + " AND pb.docId != :docId and pb.v_Prefix= :v_Prefix "
                        + " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
                psTemp.setString("v_Prefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        throw new DmException(0, "Entry already exists with this Reference Number.");
                    }
                }
                rsTemp.close();
                psTemp.close();

//            /*==============================<<  Update record into pBill >>====================================*/
                sqlQuery = "UPDATE pBill SET"
                        + " partyBillNo = :partyBillNo"
                        + " ,partyBillDate = :partyBillDate"
                        + " ,remark = :remark"
                        + " , PaymentDueDate = :PaymentDueDate \n"
                        + " , u_Name = :u_Name \n"
                        + " , u_EntDt = :uEntDt \n"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getSupplierBillDate()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setDate("PaymentDueDate", DmServer.convertDate(mrTempCurrent.getPaymentDueDate()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                psTemp.executeUpdate();
                psTemp.close();

                /*==============================<< Approve Document >>====================================*/
                if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                    documentApproval(true, WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
                }

                dmrTemp.setData(mrTempCurrent.getSharedKeyFieldCode());

                spBegin.commit();
                cnTemp.close();
                dmrTemp.setStatus(true);

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
    }
}
