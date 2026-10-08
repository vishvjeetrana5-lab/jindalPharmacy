package aarogya.inventory.purchaseBill.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.accountPosting.shared.AccountPosting_Mdl;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.purchaseBill.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseBill.shared.Find_Mdl;
import aarogya.inventory.purchaseBill.shared.GrdBillOtherDetails_Mdl;
import aarogya.inventory.purchaseBill.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Mdl;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Srv;
import aarogya.inventory.purchaseBill.shared.PurchaseBill_Vld;
import aarogya.inventory.purchaseBill.shared.TxtHGrnNo_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHHeaderGrnNo_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHVoucherType_Mdl;

import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.server.TdsCategoryPopup_SrvImpl;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;
import java.sql.SQLException;

public class PurchaseBill_SrvImpl extends DmRSSPrivate implements PurchaseBill_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pb.v_Type", voucherDateFieldName = "pb.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
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
                    + " , vt.balancingAc"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "', '" + pvc.PURCHASE_BILL_UNREGISTERED + "', '" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                listNode.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
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
        Connection cnTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            lstTemp = getSupplier("", voucherCategory, v_Type, v_Date, helper, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHGrnNo_Mdl>> getTxtHGrnNo(String v_Type, String subCode, String v_Date, String docId, String v_SNo, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHGrnNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGrnNo_Mdl> lstTemp = new ArrayList<TxtHGrnNo_Mdl>();
        TxtHGrnNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", condVoucherCat = "", pendingQty = "", itemCatFilteration = "", voucherCategory = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);
            voucherCategory = DmServer.getCodeViaManualCode(v_Type, "v_Type", "voucher_Type", "category", cnTemp);

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty";
                condition += " AND (CASE WHEN (pcd.isStatus = 0) OR (pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
            } else {
                pendingQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) AS pendingQty";
                condition += " AND ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) > 0";
                condition += " AND pcd.isStatus = 0";
            }

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_BILL)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_BILL_UNREGISTERED)) {
                condVoucherCat += " and vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_BILL)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_BILL_UNREGISTERED)) {
                condVoucherCat += " and vt.category IN ('" + pvc.SERVICE_CHALLAN + "', '" + pvc.SERVICE_CHALLAN_UNREGISTERED + "')";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pc.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlQuery = "SELECT \n"
                    + "   pc.docId AS pcDocId\n"
                    + " , pc.recId AS pcRecId\n"
                    + " , pc.v_Date AS pcV_Date\n"
                    + " , pcd.v_SNo AS pcV_Sno\n"
                    + " , pc.pChallanNo AS pcRefNo\n"
                    + " , pc.pChallanDate AS pcRefDate\n"
                    + " , pcd.departmentCode AS pcDepartmentCode\n"
                    + " , po.docId AS poDocId\n"
                    + " , po.recId AS poRecId\n"
                    + " , pod.v_Sno AS poV_SNo\n"
                    + " , im.code AS itemCode\n"
                    + " , im.name AS itemName\n"
                    + " , im.hsnCode\n"
                    + " , hsn.hsn\n"
                    + " , im.itemGSTNature\n"
                    + " , im.itsBasedOn\n"
                    + " , im.isBestBefore\n"
                    + " , im.propertyList\n"
                    + " , um.code AS purchaseUnitCode\n"
                    + " , um.unit AS purchaseUnitName\n"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces\n"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate\n"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces\n"
                    + " , ums.code AS skuCode\n"
                    + " , ums.unit AS skuName\n"
                    + " , um.gstUnitCode\n"
                    + " , pcd.mrp\n"
                    + " , pcd.skuMrp\n"
                    + " , pcd.rate\n"
                    + " , pcd.batchNo\n"
                    + " , pcd.lotNo\n"
                    + " , pcd.ibCode\n"
                    + " , pcd.manufacturingDate\n"
                    + " , pcd.expiryDate\n"
                    + " , icm.code AS colourCode\n"
                    + " , icm.name AS colourName\n"
                    + " , ism.code AS sizeCode\n"
                    + " , ism.name AS sizeName\n"
                    + " , imm.code AS itemMakeCode\n"
                    + " , imm.CompanyName AS itemMakeName\n"
                    + " , pcd.acceptQty\n" + pendingQty
                    + " , pcd.isManualTax\n"
                    + " , pcd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , pcd.gstTypeCode\n"
                    + " , pcd.oh_Amt_Gross\n"
                    + " , pcd.oh_Amt_Discount\n"
                    + " , pcd.oh_at_Discount\n"
                    + " , pcd.oh_Amt_Taxable\n"
                    + " , pcd.oh_Amt_FinalDiscount\n"
                    + " , pcd.oh_Ac_Item\n"
                    + " , im.purchaseAc\n"
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.pcDocId,pbd.pcV_SNo,pbd.acceptQty "
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + "     AND pbd.v_SNo = :v_SNo"
                        + " ) AS pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNO = pbd.pcV_SNo";
            }
            sqlQuery += " LEFT JOIN pOrderDetail pod ON pcd.poDocId = pod.docId AND pcd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON pcd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pcd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast as imm ON pcd.itemMakeCode = imm.code "
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pcd.hsnCode \n"
                    + "	WHERE pc.subCode = :subCode "
                    + condVoucherCat
                    + " AND ISNULL(pc.approvedBy,'') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " AND pc.site_Code = :site_Code"
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
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

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
                listNode.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                listNode.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                listNode.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                listNode.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                listNode.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
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
                double pendingSkuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                listNode.setPendingSkuQty(DmServer.vNull(DmServer.vNull(pendingSkuQty, skuDecimalFormat)));

                //Rate
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));

                //Amount
                listNode.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                listNode.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                listNode.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                listNode.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));

                listNode.setFinalDiscountPer("");
                listNode.setFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));

                //IAP
                listNode.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));
                listNode.setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("purchaseAc")));

                //GST
                listNode.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                listNode.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                listNode.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                listNode.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));

                if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1) {
                    if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                        listNode.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                        listNode.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                    } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                        listNode.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                        listNode.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                    }

                    listNode.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    listNode.setSgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                    listNode.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    listNode.setUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                    listNode.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                    listNode.setCgst(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                    listNode.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                    listNode.setIgst(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                }

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());

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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT it.code, it.name, it.printSrNo\n"
                    + "FROM gstType it\n"
                    + "WHERE it.isActive = 1\n";
            sqlWithParam.setSQL(sqlQuery);
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    @Override
    public DmResult<PurchaseBill_Mdl> getItemDetail(String v_Type, List<KeyFieldExtended> keyFields, String grnDocId, String isStateOrUt, String subCode, String location, String billDate, String docId, String v_Date,
            Boolean singlePOFlowInPurchase, String rcmApplicable, boolean isIncludeTaxInCost, String isSiteStateUT, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive) {
        DmResult<PurchaseBill_Mdl> dmrTemp = new DmResult();
        PurchaseBill_Mdl mrTemp = new PurchaseBill_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, conditionLines = "", condition = "", pendingAcceptQty = "", pendingDocQty = "", itemCatFilteration = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        ServiceDetail_Mdl serviceDetail_Mdl;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingAcceptQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty";
                pendingDocQty = " , ISNULL(pcd.challanQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.docQty,0) AS pendingDocQty";
                condition += " AND (CASE WHEN (pcd.isStatus = 0) OR (pcd.isStatus = 2 AND (ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
            } else {
                pendingAcceptQty = " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) AS pendingQty";
                pendingDocQty = " , ISNULL(pcd.challanQty,0) - ISNULL(pcd.billedQty,0) AS pendingDocQty";
                condition += " AND ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) > 0";
                condition += " AND pcd.isStatus = 0";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pc.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            /*==============================<< Get Grn detail (via list) >>===============================*/
            if (!DmShared.xNull(grnDocId).equalsIgnoreCase("")) {
                conditionLines += " pcd.docId in (" + DmServer.getParamList(":grnDocId", grnDocId) + " )";
            } else if (keyFields != null) {
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

            }

            if (!DmShared.xNull(conditionLines).trim().equalsIgnoreCase("")) {
                condition += " AND (" + conditionLines + ")";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlQuery = "SELECT"
                    + "  pcd.itemCode"
                    + " , pcd.mrp"
                    + " , pcd.skuMrp"
                    + " , pcd.rate"
                    + " , pcd.saleRate"
                    + " , pcd.batchNo"
                    + " , pcd.lotNo"
                    + " , pcd.ibCode"
                    + " , pcd.expiryDate"
                    + " , pcd.manufacturingDate"
                    + " , im.name AS itemName"
                    + " , im.itemGstNature"
                    + " , im.itsBasedOn"
                    + " , pcd.hsnCode"
                    + " , hsn.hsn\n"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , icm.code AS colourCode"
                    + " , icm.name AS colourName"
                    + " , ism.code AS sizeCode"
                    + " , ism.name AS sizeName"
                    + " , imm.code AS itemMakeCode"
                    + " , imm.CompanyName AS itemMakeName"
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
                    + " , pcd.departmentCode AS pcDepartmentCode"
                    + " , po.docId AS poDocId"
                    + " , po.recId AS poRecId"
                    + " , pod.v_Sno AS poV_SNo"
                    + " , po.refDate AS poRefDate" + pendingAcceptQty + pendingDocQty
                    + " , pcd.isManualTax\n"
                    + " , pcd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , pcd.gstTypeCode\n"
                    + " , pcd.oh_Amt_Gross\n"
                    + " , pcd.oh_Amt_Discount\n"
                    + " , pcd.oh_at_Discount\n"
                    + " , pcd.oh_Amt_Taxable\n"
                    + " , pcd.oh_at_SGST\n"
                    + " , pcd.oh_Amt_SGST\n"
                    + " , pcd.oh_at_UGST\n"
                    + " , pcd.oh_Amt_UGST\n"
                    + " , pcd.oh_at_CGST\n"
                    + " , pcd.oh_Amt_CGST\n"
                    + " , pcd.oh_at_IGST\n"
                    + " , pcd.oh_Amt_IGST\n"
                    + " , pcd.oh_At_FinalDiscount\n"
                    + " , pcd.oh_Amt_FinalDiscount\n"
                    + " , pcd.oh_Amt_SubTotal\n"
                    + " , pcd.oh_Amt_Net\n"
                    + " , pcd.oh_Ac_Item\n"
                    + " , im.purchaseAc "
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.pcDocId,pbd.pcV_SNo,pbd.acceptQty, pbd.docQty "
                        + "     FROM pBillDetail pbd "
                        + "     WHERE pbd.docid = :docId"
                        + " ) AS pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNO = pbd.pcV_SNo";
            }
            sqlQuery += " LEFT JOIN pOrderDetail pod ON pcd.poDocId = pod.docId AND pcd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON pcd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pcd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast as imm ON pcd.itemMakeCode = imm.code "
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pcd.hsnCode \n"
                    + "	WHERE pc.subCode = :subCode"
                    + " AND pc.site_Code = :site_Code"
                    + " AND ISNULL(pc.approvedBy, '') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0 "
                    + condition
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
            if (!DmShared.xNull(grnDocId).equalsIgnoreCase("")) {
                psTemp.setStringList("grnDocId", DmShared.xNull(grnDocId));
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
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

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
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));

                //Properties
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setBatchNo(node.getBatchNo());
                ipm.setLotNo(node.getLotNo());
                ipm.setIbCode(node.getIbCode());
                ipm.setColourName(node.getColourName());
                ipm.setSizeName(node.getSizeName());
                ipm.setItemMakeName(node.getItemMakeName());
                ipm.setMrp(node.getMrp());
                ipm.setSkuMrp(node.getSkuMrp());
                ipm.setExpiryDate(node.getExpiryDate());
                ipm.setManufacturingDate(node.getManufacturingDate());
                node.setPropertiesCsv(DmShared.xNull(ipm.getPropertiesCsv()));

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
                node.setDocQty(DmServer.vNull(rsTemp.getString("pendingDocQty")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                double skuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                //Rate
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));

                //Amount
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                node.setFinalDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_FinalDiscount"), "0.00"));
                node.setFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));
                node.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                node.setItemValue(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                //Purchase A/c (Based On Item Master)
                node.setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("purchaseAc")));
                if (isInventoryAccountingActive) {
                    //IAP
                    node.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));
                } else {
                    if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                        if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.ITEM.getId()) {
                            node.setIsubCode(DmShared.xNull(rsTemp.getString("purchaseAc")));
                        }
                    }
                }

                //GST
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);

                if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1) {
                    node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                    node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));

                    if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                        node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                        node.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                    } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                        node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                        node.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                    }

                    node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    node.setSgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                    node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    node.setUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                    node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                    node.setCgst(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                    node.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                    node.setIgst(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                }

                //GSTS
                DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(DmShared.xNull(rsTemp.getString("itemCode")),
                        DmServer.vNull(rsTemp.getString("rate"), "0.00"),
                        DmShared.xNull(rsTemp.getString("itsBasedOn")),
                        location,
                        DmShared.xNull(rsTemp.getString("gstTypeCode")),
                        (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1),
                        cnTemp);
                if (dmrIts.isStatus() && dmrIts.getData() != null) {
                    String taxPer, taxAcCode, rcmAcCode;

                    if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 0) {
                        node.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                        node.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                    }

                    //Get Purchase A/c From GSTS
                    if (!isInventoryAccountingActive) {
                        if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                            if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.GSTS.getId()) {
                                for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                                    if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.PURCHASE_ACCOUNT.getShortName())) {
                                        node.setIsubCode(iTS_Mdl.getPurchaseSalesAcCode());
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                        taxPer = "";
                        taxAcCode = "";
                        rcmAcCode = "";

                        if (isIncludeTaxInCost) {
                            if (DmShared.iNull(rcmApplicable).intValue() == 0) {
                                taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                                if (!isInventoryAccountingActive) {
                                    taxAcCode = DmShared.xNull(node.getIsubCode());
                                }
                            }
                        } else {
                            taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                            rcmAcCode = DmShared.xNull(iTS_Mdl.getRcmAcCode());
                            taxAcCode = DmShared.xNull(iTS_Mdl.getInputTaxAcCode());
                        }

                        if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            node.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setSgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setSgstAcCode(taxAcCode);
                            node.setSgstRcmAcCode(rcmAcCode);

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            node.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setUgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setUgstAcCode(taxAcCode);
                            node.setUgstRcmAcCode(rcmAcCode);

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                            node.setCgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setCgstAcCode(taxAcCode);
                            node.setCgstRcmAcCode(rcmAcCode);

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                            node.setIgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setIgstAcCode(taxAcCode);
                            node.setIgstRcmAcCode(rcmAcCode);
                        }
                    }
                }

                //Add Node
                lstNode.add(node);
            }
            /*==============================<< Get Service detail >>===============================*/
            if (!DmShared.xNull(grnDocId).equalsIgnoreCase("")) {
                condition = " pcd.docId in (" + DmServer.getParamList(":grnDocId", grnDocId) + " )";
                String iSubCodeField = "", iSubCodeGroupBy = "";
                if (isInventoryAccountingActive) {
                    iSubCodeField = "null";
                    iSubCodeGroupBy = "";
                } else {
                    iSubCodeField = "pcd.oh_Ac_Item";
                    iSubCodeGroupBy = ", pcd.oh_Ac_Item";
                }
                if (DmShared.xNull(grnDocId).split(",").length > 1) {
                    sqlQuery = " SELECT\n"
                            + " pcd.itemCode \n"
                            + ",pcd.hsnCode \n"
                            + ",pcd.itemGSTNature AS itemGSTNature \n"
                            + ", MAX(im.name) AS itemName \n"
                            + ", MAX(im.smfCode) AS smfCode\n"
                            + ", MAX(im.itsBasedOn) AS itsBasedOn \n"
                            + ", MAX(pcd.unitCode)  AS unitCode\n"
                            + ", 1 AS docQty \n"
                            + ", SUM(pcd.oh_Amt_Gross) AS rate\n"
                            + ", 0 AS isManualTax\n"
                            + ", 0 AS isManualGstType \n"
                            + ", MAX(it.name) AS gstTypeName \n"
                            + ", pcd.gstTypeCode\n"
                            + ", SUM(pcd.oh_Amt_Gross) AS oh_Amt_Gross\n"
                            + ", SUM(pcd.oh_Amt_Taxable) AS oh_Amt_Taxable\n"
                            + ", ISNULL(pcd.oh_at_SGST,0) AS oh_at_SGST\n"
                            + ", 0 AS oh_Amt_SGST\n"
                            + ", ISNULL(pcd.oh_at_UGST,0) AS  oh_at_UGST\n"
                            + ", 0  AS oh_Amt_UGST\n"
                            + ", ISNULL(pcd.oh_at_CGST,0) AS  oh_at_CGST\n"
                            + ", 0 AS oh_Amt_CGST\n"
                            + ", ISNULL(pcd.oh_at_IGST,0) AS oh_at_IGST\n"
                            + ", 0 AS oh_Amt_IGST\n"
                            + ", SUM(pcd.oh_Amt_SubTotal) AS oh_Amt_SubTotal\n"
                            + ", SUM(pcd.oh_Amt_Net) AS oh_Amt_Net\n"
                            + ", MAX(um.gstUnitCode) AS gstUnitCode\n"
                            + ", MAX(hsn.hsn) AS hsn\n"
                            + ", " + DmEnum.ServiceEvalType.FREE_VALUE.getCode() + " AS serviceEvalTypeCode \n"
                            + ", SUM(pcd.oh_Amt_Gross)  AS serviceEvalRate \n"
                            + ", " + iSubCodeField + " AS oh_Ac_Item\n"
                            + "FROM pChallanDetail pcd\n"
                            + "LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                            + "LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                            + "LEFT JOIN hsnMast hsn ON hsn.code = pcd.hsnCode \n"
                            + "LEFT JOIN unitMast um ON pcd.unitCode = um.code\n"
                            + "WHERE \n"
                            + condition
                            + " AND ISNULL(pcd.isFooter, 0) = 2\n"
                            + " AND ISNULL(pcd.isStatus,0) = 0\n"
                            + "group By pcd.itemCode, pcd.hsnCode, pcd.gstTypeCode, pcd.itemGSTNature" + iSubCodeGroupBy + ",\n"
                            + "ISNULL(pcd.oh_at_SGST,0), ISNULL(pcd.oh_at_UGST,0), ISNULL(pcd.oh_at_CGST, 0), ISNULL(pcd.oh_at_IGST,0)\n"
                            + "ORDER BY MAX(im.printSrNo)\n"
                            + "\n";
                } else {
                    sqlQuery = "SELECT\n"
                            + "  pcd.itemCode\n"
                            + " , pcd.hsnCode\n"
                            + " , pcd.itemGSTNature\n"
                            + " , im.name AS itemName\n"
                            + " , im.smfCode\n"
                            + " , im.itsBasedOn\n"
                            + " , pcd.unitCode \n"
                            + " , pcd.challanQty as docQty \n"
                            + " , pcd.rate \n"
                            + " , pcd.isManualTax\n"
                            + " , pcd.isManualGstType\n"
                            + " , it.name AS gstTypeName\n"
                            + " , pcd.gstTypeCode\n"
                            + " , pcd.oh_Amt_Gross\n"
                            + " , pcd.oh_Amt_Taxable\n"
                            + " , pcd.oh_at_SGST\n"
                            + " , pcd.oh_Amt_SGST\n"
                            + " , pcd.oh_at_UGST\n"
                            + " , pcd.oh_Amt_UGST\n"
                            + " , pcd.oh_at_CGST\n"
                            + " , pcd.oh_Amt_CGST\n"
                            + " , pcd.oh_at_IGST\n"
                            + " , pcd.oh_Amt_IGST\n"
                            + " , pcd.oh_Amt_SubTotal\n"
                            + " , pcd.oh_Amt_Net \n"
                            + " , um.gstUnitCode\n"
                            + " , hsn.hsn \n"
                            + " , pcd.serviceEvalTypeCode \n"
                            + " , pcd.serviceEvalRate \n"
                            + ", " + iSubCodeField + " AS oh_Ac_Item\n"
                            + " FROM pChallanDetail pcd\n"
                            + " LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                            + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                            + " LEFT JOIN hsnMast hsn ON hsn.code = pcd.hsnCode \n"
                            + " LEFT JOIN unitMast um ON pcd.unitCode = um.code\n"
                            + " WHERE \n"
                            + condition
                            + " AND ISNULL(pcd.isFooter, 0) = 2\n"
                            + " AND ISNULL(pcd.isStatus,0) = 0\n"
                            + " ORDER BY pcd.v_SNo\n";
                }

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (!DmShared.xNull(grnDocId).equalsIgnoreCase("")) {
                    psTemp.setStringList("grnDocId", DmShared.xNull(grnDocId));
                }
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    serviceDetail_Mdl = new ServiceDetail_Mdl();
                    serviceDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                    serviceDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                    serviceDetail_Mdl.setSmfCode(DmShared.xNull(rsTemp.getString("smfCode")));
                    serviceDetail_Mdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                    serviceDetail_Mdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                    serviceDetail_Mdl.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                    serviceDetail_Mdl.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                    serviceDetail_Mdl.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                    serviceDetail_Mdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    serviceDetail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("docQty")));
                    serviceDetail_Mdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                    serviceDetail_Mdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                    serviceDetail_Mdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                    serviceDetail_Mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                    serviceDetail_Mdl.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                    if (!DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")).equalsIgnoreCase("")) {
                        serviceDetail_Mdl.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")));
                        serviceDetail_Mdl.setServiceEvalTypeName(DmShared.xNull(DmEnum.ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypeCode")).intValue()).getName()));
                        serviceDetail_Mdl.setServiceEvalRate(DmServer.vNull(rsTemp.getString("serviceEvalRate"), "0.00"));
                    }

                    //IAP
                    serviceDetail_Mdl.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));

                    //GST
                    serviceDetail_Mdl.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                    serviceDetail_Mdl.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);

                    if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1) {
                        serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                        serviceDetail_Mdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));

                        if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                            serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                            serviceDetail_Mdl.setSgstOrUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                        } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                            serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                            serviceDetail_Mdl.setSgstOrUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                        }
                        serviceDetail_Mdl.setSgstRate(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                        serviceDetail_Mdl.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                        serviceDetail_Mdl.setUgstRate(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                        serviceDetail_Mdl.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                        serviceDetail_Mdl.setCgstRate(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                        serviceDetail_Mdl.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                        serviceDetail_Mdl.setIgstRate(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                        serviceDetail_Mdl.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                    }

                    //GSTS
                    DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(DmShared.xNull(rsTemp.getString("itemCode")),
                            DmServer.vNull(rsTemp.getString("rate"), "0.00"),
                            DmShared.xNull(rsTemp.getString("itsBasedOn")),
                            location,
                            DmShared.xNull(rsTemp.getString("gstTypeCode")),
                            (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1),
                            cnTemp);
                    if (dmrIts.isStatus() && dmrIts.getData() != null) {
                        String taxPer, taxAcCode, rcmAcCode;

                        if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 0) {
                            serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                            serviceDetail_Mdl.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                        }

                        for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                            taxPer = "";
                            taxAcCode = "";
                            rcmAcCode = "";

                            if (isIncludeTaxInCost) {
                                if (DmShared.iNull(rcmApplicable).intValue() == 0) {
                                    taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                                    if (!isInventoryAccountingActive) {
                                        taxAcCode = DmShared.xNull(serviceDetail_Mdl.getIsubCode());
                                    }
                                }
                            } else {
                                taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                                rcmAcCode = DmShared.xNull(iTS_Mdl.getRcmAcCode());
                                taxAcCode = DmShared.xNull(iTS_Mdl.getInputTaxAcCode());
                            }

                            if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                                serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(taxPer, "0.00"));
                                serviceDetail_Mdl.setSgstRate(DmServer.vNull(taxPer, "0.00"));
                                serviceDetail_Mdl.setSgstAcCode(taxAcCode);
                                serviceDetail_Mdl.setSgstRcmAcCode(rcmAcCode);

                            } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                                serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(taxPer, "0.00"));
                                serviceDetail_Mdl.setUgstRate(DmServer.vNull(taxPer, "0.00"));
                                serviceDetail_Mdl.setUgstAcCode(taxAcCode);
                                serviceDetail_Mdl.setUgstRcmAcCode(rcmAcCode);

                            } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                                serviceDetail_Mdl.setCgstRate(DmServer.vNull(taxPer, "0.00"));
                                serviceDetail_Mdl.setCgstAcCode(taxAcCode);
                                serviceDetail_Mdl.setCgstRcmAcCode(rcmAcCode);

                            } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                                serviceDetail_Mdl.setIgstRate(DmServer.vNull(taxPer, "0.00"));
                                serviceDetail_Mdl.setIgstAcCode(taxAcCode);
                                serviceDetail_Mdl.setIgstRcmAcCode(rcmAcCode);
                            }
                        }
                    }

                    serviceDetail_Mdl.setGstRate(DmShared.vNull(DmShared.vNull(serviceDetail_Mdl.getCgstRate())
                            + DmShared.vNull(serviceDetail_Mdl.getSgstOrUgstRate())
                            + DmShared.vNull(serviceDetail_Mdl.getIgstRate())).toString());

                    mrTemp.getGrdServiceDetail().add(serviceDetail_Mdl);
                }
            }
            mrTemp.getListItemDetail().addAll(lstNode);
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
                STApprovals.purchaseBillApproval(false, WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                STApprovals.purchaseBillApproval(false, WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
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
                    STApprovals.purchaseBillApproval(false, walkThroughEnum, keyCode, userName, cnTemp);
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
    //</editor-fold>

    @Override
    public DmResult<String[]> getServerDateTime() {
        DmResult<String[]> dmrTemp = new DmResult<String[]>();
        try {
            dmrTemp.setData(DmServer.getServerDateTime());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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

            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));
            /*==============================<< Get default voucher_Type >>===============================*/
            defaultValue.setVoucherType(getVoucherType("", cnTemp));

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
                    + " LEFT JOIN stateMast st ON si.stateNumericCode = st.numericCode"
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

            /*==============================<< Get System Service Accounts Code >>====================================*/
            sqlQuery = "SELECT im.uniqueId, im.subCode \n"
                    + "FROM itemMast im\n"
                    + "WHERE im.uniqueId IN ('RO', 'FD')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                switch (DmShared.xNull(rsTemp.getString("uniqueId"))) {
                    case "RO":
                        defaultValue.setRoundOffAcCode(DmShared.xNull(rsTemp.getString("subCode")));
                        break;
                    case "FD":
                        defaultValue.setFinalDicountAcCode(DmShared.xNull(rsTemp.getString("subCode")));
                        break;
                    default:
                        break;
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.SinglePOFlowInPurchase,e.isIncludeOverHeadsInCost \n"
                    + ",isNull(e.ac_TDS_Applicable_On,0) as ac_TDS_Applicable_On\n"
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
                defaultValue.setIncludeOverHeadsInCost(DmShared.iNull(rsTemp.getString("isIncludeOverHeadsInCost")).intValue() == 1);
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
            sqlQuery = " Select  REPLACE(CONVERT(VARCHAR(11), adv.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,adv.u_EntDt,100 ) ,7), 6, 0, ' ') PayDateTime, "
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
                    + "     left join subgroup sg on sg.subCode = cl.subCode                      \n"
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
            sqlQuery = " select REPLACE(CONVERT(VARCHAR(11), bh.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,bh.u_EntDt,100 ) ,7), 6, 0, ' ') dateTime, bh.recId, "
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
    public DmResult<String> saveRecord(PurchaseBill_Mdl mrTemp, String voucherPrefix, boolean isApprove, boolean updateItemPurchaseRate) {
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
        TdsCategoryPopup_Mdl tdsCatMdl;
        try {
            PurchaseBill_Vld.chkSave(mrTemp);
            ServiceGrid_Vld.chkSave(mrTemp.getGrdServiceDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");
            if (mrTemp.getDefaultValueMdl().isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getVoucherDate(), "", cnTemp);
            }
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            spBegin = new DmSavePoint(cnTemp);

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
                    + " FROM pBill pb with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pb.v_Type "
                    + " WHERE  pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode "
                    + " AND pb.v_Prefix = :v_Prefix "
                    + " AND pb.docId <> :docId " ;
//                    + " AND vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("partyBillNo", DmShared.xNull(mrTemp.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(400, "Entry already exists with this Bill Number.");
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
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
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
            }
            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert record into quotation >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pBill ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, partyBillNo, partyBillDate, "
                        + "     calcOn, subCode, consignee, partyType, gsTin, gstinDate, rcmApplicable, placeOfSupply,"
                        + "     remark, wtaCode, guiConfigCode, site_Code, preparedBy, u_EntDt_LatestLine, transferDt ,preparedDate,"
                        + "     oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                        + "     oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                        + "     oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Total, \n"
                        + "     oh_Amt_RoundOff, oh_Amt_Net, oh_Amt_LandedValue, locationCode, \n"
                        + "     oh_Ac_FinalDiscount, oh_Ac_RoundOff, balancingAc,PaymentDueDate, tdsAmt)\n"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, :partyBillNo, :partyBillDate, "
                        + "     :calcOn, :subCode, :consignee, :partyType, :gsTin, :gstinDate, :rcmApplicable, :placeOfSupply,"
                        + "     :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy , :uEntDt, :transferDt ,:preparedDate,"
                        + "     :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                        + "     :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                        + "     :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Total, \n"
                        + "     :oh_Amt_RoundOff, :oh_Amt_Net, :oh_Amt_LandedValue, :locationCode, "
                        + "     :oh_Ac_FinalDiscount, :oh_Ac_RoundOff, :balancingAc,:PaymentDueDate, :tdsAmt) ";
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
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(mrTemp.getGoodsGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_GrossService", DmServer.vNull(mrTemp.getServiceGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getGrossAmount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTemp.getDiscount(), "0.00"));
                psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(mrTemp.getGoodsTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(mrTemp.getServiceTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTemp.getSgst(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTemp.getUgst(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTemp.getCgst(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTemp.getIgst(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTemp.getSubTotal(), "0.00"));
                psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(mrTemp.getFinalDiscountPer(), "0.00"));
                psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(mrTemp.getFinalDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTemp.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("oh_Ac_FinalDiscount", DmShared.foriegnKey(mrTemp.getFinalDiscountAcCode()));
                psTemp.setString("oh_Ac_RoundOff", DmShared.foriegnKey(mrTemp.getRoundOffAcCode()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAc()));
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
                        + " ,gsTin = :gsTin"
                        + " ,gstinDate = :gstinDate"
                        + " ,rcmApplicable = :rcmApplicable"
                        + " ,placeOfSupply = :placeOfSupply"
                        + " ,remark = :remark"
                        + " ,u_EntDt = :uEntDt"
                        + " ,u_EntDt_LatestLine = :uEntDt"
                        + " ,u_Name = :u_Name"
                        + " ,transferDt = :transferDt"
                        + " , oh_Amt_GrossGoods = :oh_Amt_GrossGoods\n"
                        + " , oh_Amt_GrossService = :oh_Amt_GrossService\n"
                        + " , oh_Amt_Gross = :oh_Amt_Gross\n"
                        + " , oh_Amt_Discount = :oh_Amt_Discount\n"
                        + " , oh_Amt_TaxableGoods = :oh_Amt_TaxableGoods\n"
                        + " , oh_Amt_TaxableService = :oh_Amt_TaxableService\n"
                        + " , oh_Amt_Taxable = :oh_Amt_Taxable\n"
                        + " , oh_Amt_SGST = :oh_Amt_SGST\n"
                        + " , oh_Amt_UGST = :oh_Amt_UGST\n"
                        + " , oh_Amt_CGST = :oh_Amt_CGST\n"
                        + " , oh_Amt_IGST = :oh_Amt_IGST\n"
                        + " , oh_Amt_SubTotal = :oh_Amt_SubTotal\n"
                        + " , oh_at_FinalDiscount = :oh_at_FinalDiscount\n"
                        + " , oh_Amt_FinalDiscount = :oh_Amt_FinalDiscount\n"
                        + " , oh_Amt_Total = :oh_Amt_Total\n"
                        + " , oh_Amt_RoundOff = :oh_Amt_RoundOff\n"
                        + " , oh_Amt_Net = :oh_Amt_Net\n"
                        + " , oh_Amt_LandedValue = :oh_Amt_LandedValue\n"
                        + " , locationCode = :locationCode\n"
                        + " , oh_Ac_FinalDiscount = :oh_Ac_FinalDiscount\n"
                        + " , oh_Ac_RoundOff = :oh_Ac_RoundOff \n"
                        + " , balancingAc = :balancingAc \n"
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
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(mrTemp.getGoodsGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_GrossService", DmServer.vNull(mrTemp.getServiceGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getGrossAmount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTemp.getDiscount(), "0.00"));
                psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(mrTemp.getGoodsTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(mrTemp.getServiceTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTemp.getSgst(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTemp.getUgst(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTemp.getCgst(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTemp.getIgst(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTemp.getSubTotal(), "0.00"));
                psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(mrTemp.getFinalDiscountPer(), "0.00"));
                psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(mrTemp.getFinalDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTemp.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("oh_Ac_FinalDiscount", DmShared.foriegnKey(mrTemp.getFinalDiscountAcCode()));
                psTemp.setString("oh_Ac_RoundOff", DmShared.foriegnKey(mrTemp.getRoundOffAcCode()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAc()));
                psTemp.setDate("PaymentDueDate", DmServer.convertDate(mrTemp.getPaymentDueDate()));
                psTemp.setString("tdsAmt", DmServer.vNull(mrTemp.getTdsAmt(), "0.00"));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //====================================Re-check-PartyBillNo=================================
            /*==============================<< Check for duplicate party bill no >>====================================*/
            sqlQuery = " SELECT COUNT(pb.docid) AS cnt "
                    + " FROM pBill pb with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pb.v_Type "
                    + " WHERE  pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode"
                    + " AND pb.v_Prefix = :v_Prefix "
                    + " AND pb.docId <> :docId "
                    + " AND vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("partyBillNo", DmShared.xNull(mrTemp.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(400, "Entry already exists with this Bill Number.");
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
                    if (!detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                            detail_Mdl.setvSNo(String.valueOf(vSNo));
                            sqlQuery = "INSERT INTO pBillDetail ("
                                    + "     docId, v_SNo, pcDocId, pcV_SNo, poDocId, poV_SNo, itemCode, hsnCode, "
                                    + "     itemGSTNature, batchNo, lotNo, ibCode, colourCode, sizeCode,itemMakeCode, manufacturingDate,"
                                    + "     expiryDate, mrp, skuMrp, unitCode, docQty, recQty, acceptQty, skuQtyForRate, skuQty, rate, isStatus,"
                                    + "     landedSkuValue, landedSkuRate, skuRate\n"
                                    + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                    + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                    + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Net\n"
                                    + "     , oh_Ac_SGST, oh_Ac_UGST, oh_Ac_CGST, oh_Ac_IGST\n"
                                    + "     , oh_Ac_SGST_RCM, oh_Ac_UGST_RCM, oh_Ac_CGST_RCM, oh_Ac_IGST_RCM\n"
                                    + "     , gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                    + " Values ("
                                    + "     :docId, :v_SNo, :pcDocId, :pcV_SNo, :poDocId, :poV_SNo, :itemCode, :hsnCode,"
                                    + "     :itemGSTNature, :batchNo, :lotNo, :ibCode, :colourCode, :sizeCode, :itemMakeCode, :manufacturingDate,"
                                    + "     :expiryDate, :mrp, :skuMrp, :unitCode, :docQty, :recQty, :acceptQty, :skuQtyForRate, :skuQty, :rate, :isStatus,"
                                    + "     :landedSkuValue, :landedSkuRate, :skuRate\n"
                                    + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                    + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                    + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Net\n"
                                    + "     , :oh_Ac_SGST, :oh_Ac_UGST, :oh_Ac_CGST, :oh_Ac_IGST\n"
                                    + "     , :oh_Ac_SGST_RCM, :oh_Ac_UGST_RCM, :oh_Ac_CGST_RCM, :oh_Ac_IGST_RCM\n"
                                    + "     , :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
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
                            psTemp.setString("lotNo", DmShared.xNull(detail_Mdl.getLotNo()));
                            psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                            psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColourCode()));
                            psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(detail_Mdl.getItemMakeCode()));
                            psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                            psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                            psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                            psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                            psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                            psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getDocQty()));
                            psTemp.setString("recQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                            psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                            psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                            psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                            psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                            psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
                            psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                            psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                            psTemp.setString("isStatus", "0");
                            psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                            psTemp.setString("oh_at_Discount", DmServer.vNull(detail_Mdl.getDiscountPer(), "0.00"));
                            psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                            psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                            psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                            psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgst(), "0.00"));
                            psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                            psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgst(), "0.00"));
                            psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                            psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgst(), "0.00"));
                            psTemp.setString("oh_at_IGST", DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));
                            psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getIgst(), "0.00"));
                            psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(detail_Mdl.getSubTotal(), "0.00"));
                            psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(detail_Mdl.getFinalDiscountPer(), "0.00"));
                            psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(detail_Mdl.getFinalDiscount(), "0.00"));
                            psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getItemValue(), "0.00"));
                            psTemp.setString("oh_Ac_SGST", DmShared.foriegnKey(detail_Mdl.getSgstAcCode()));
                            psTemp.setString("oh_Ac_UGST", DmShared.foriegnKey(detail_Mdl.getUgstAcCode()));
                            psTemp.setString("oh_Ac_CGST", DmShared.foriegnKey(detail_Mdl.getCgstAcCode()));
                            psTemp.setString("oh_Ac_IGST", DmShared.foriegnKey(detail_Mdl.getIgstAcCode()));
                            psTemp.setString("oh_Ac_SGST_RCM", DmShared.foriegnKey(detail_Mdl.getSgstRcmAcCode()));
                            psTemp.setString("oh_Ac_UGST_RCM", DmShared.foriegnKey(detail_Mdl.getUgstRcmAcCode()));
                            psTemp.setString("oh_Ac_CGST_RCM", DmShared.foriegnKey(detail_Mdl.getCgstRcmAcCode()));
                            psTemp.setString("oh_Ac_IGST_RCM", DmShared.foriegnKey(detail_Mdl.getIgstRcmAcCode()));
                            psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail_Mdl.getGstTypeCode()));
                            psTemp.setString("isManualGstType", detail_Mdl.isManualGstType() ? "1" : "0");
                            psTemp.setString("isManualTax", detail_Mdl.isManualTax() ? "1" : "0");
                            psTemp.setString("oh_Ac_Item", DmShared.foriegnKey(detail_Mdl.getIsubCode()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }

            //========================================(Service Grid Modal)==========================================================
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pBillDetail (\n"
                                + "     docId, v_SNo, itemCode, hsnCode, docQty, acceptQty, rate, itemGSTNature ,unitCode ,isFooter\n"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net\n"
                                + "     , oh_Ac_SGST, oh_Ac_UGST, oh_Ac_CGST, oh_Ac_IGST\n"
                                + "     , oh_Ac_SGST_RCM, oh_Ac_UGST_RCM, oh_Ac_CGST_RCM, oh_Ac_IGST_RCM\n"
                                + "     , serviceEvalTypeCode, serviceEvalRate, gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                + " Values (\n"
                                + "     :docId, :v_SNo, :itemCode, :hsnCode, :docQty, :acceptQty, :rate, :itemGSTNature ,:unitCode, :isFooter"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :oh_Ac_SGST, :oh_Ac_UGST, :oh_Ac_CGST, :oh_Ac_IGST\n"
                                + "     , :oh_Ac_SGST_RCM, :oh_Ac_UGST_RCM, :oh_Ac_CGST_RCM, :oh_Ac_IGST_RCM\n"
                                + "     , :serviceEvalTypeCode, :serviceEvalRate, :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getDocQty()));
                        psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                        psTemp.setString("isFooter", "2");
                        psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                        psTemp.setString("oh_at_Discount", DmServer.vNull(detail_Mdl.getDiscountPer(), "0.00"));
                        psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                        psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                        psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgst(), "0.00"));
                        psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgst(), "0.00"));
                        psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgst(), "0.00"));
                        psTemp.setString("oh_at_IGST", DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getIgst(), "0.00"));
                        psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(detail_Mdl.getSubTotal(), "0.00"));
                        psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getItemValue(), "0.00"));
                        psTemp.setString("oh_Ac_SGST", DmShared.foriegnKey(detail_Mdl.getSgstAcCode()));
                        psTemp.setString("oh_Ac_UGST", DmShared.foriegnKey(detail_Mdl.getUgstAcCode()));
                        psTemp.setString("oh_Ac_CGST", DmShared.foriegnKey(detail_Mdl.getCgstAcCode()));
                        psTemp.setString("oh_Ac_IGST", DmShared.foriegnKey(detail_Mdl.getIgstAcCode()));
                        psTemp.setString("oh_Ac_SGST_RCM", DmShared.foriegnKey(detail_Mdl.getSgstRcmAcCode()));
                        psTemp.setString("oh_Ac_UGST_RCM", DmShared.foriegnKey(detail_Mdl.getUgstRcmAcCode()));
                        psTemp.setString("oh_Ac_CGST_RCM", DmShared.foriegnKey(detail_Mdl.getCgstRcmAcCode()));
                        psTemp.setString("oh_Ac_IGST_RCM", DmShared.foriegnKey(detail_Mdl.getIgstRcmAcCode()));
                        psTemp.setString("serviceEvalTypeCode", DmShared.foriegnKey(detail_Mdl.getServiceEvalTypeCode()));
                        psTemp.setString("serviceEvalRate", DmServer.vNull(detail_Mdl.getServiceEvalRate(), "0.00"));
                        psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail_Mdl.getGstTypeCode()));
                        psTemp.setString("isManualGstType", detail_Mdl.isManualGstType() ? "1" : "0");
                        psTemp.setString("isManualTax", detail_Mdl.isManualTax() ? "1" : "0");
                        psTemp.setString("oh_Ac_Item", DmShared.foriegnKey(detail_Mdl.getIsubCode()));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
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

            /*==============================<< Manage GRN (Billed Qty) >>====================================*/
            manageBilledQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), false, cnTemp);

            /*==============================<< Insert records into OH >>====================================*/
//            OverHeadLine_Server.save(listDetail, "docId", mrTemp.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp); //<Ag> TBR
//            OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", DmShared.xNull(mrTemp.getSupplierBillNo()), DmShared.dNull(mrTemp.getSupplierBillDate()), "", "", false, true, cnTemp); //<Ag> TBR

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
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
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
            }
            /*==============================<< Delete records from gstTrans >>====================================*/
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Insert records into gstTrans >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
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
                    gstTrans_Mdl.setGstinDate(DmShared.dNull(mrTemp.getSupplierGstinDate()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTemp.getPlaceOfSupply()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTemp.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(detail_Mdl.getHsnCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    gstTrans_Mdl.setDepartmentCode(DmShared.xNull(detail_Mdl.getPcDepartmentCode()));
                    gstTrans_Mdl.setQty(DmShared.iNull(mrTemp.getCalculateOnTag()).intValue() == 1 ? DmShared.xNull(detail_Mdl.getAcceptQty()) : DmShared.xNull(detail_Mdl.getDocQty()));
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

                    //<editor-fold defaultstate="collapsed" desc="Get ItemTaxDetail">
                    sqlQuery = "SELECT"
                            + " pbd.oh_Amt_Gross"
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
                            + " FROM pBillDetail pbd with (nolock)"
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
                    //</editor-fold>
                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
                }
            }

            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                if (DmShared.vNull(mrTemp.getRoundOffAmount()) != 0) {
                    //<editor-fold defaultstate="collapsed" desc="Insert RoundOff line in gstTrans">
                    gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(String.valueOf(vSNo));
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
                    gstTrans_Mdl.setItemCode(DmShared.foriegnKey(roundOffItemCode));
                    gstTrans_Mdl.setHsnCode(DmShared.foriegnKey(roundOffItemHsnCode));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(roundOffItemGstNature));
                    if (DmShared.vNull(mrTemp.getRoundOffAmount()) < 0) {
                        gstTrans_Mdl.setQty("-1");
                    } else {
                        gstTrans_Mdl.setQty("1");
                    }
                    gstTrans_Mdl.setItemGrossAmount(DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    gstTrans_Mdl.setItemTaxableAmount(DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    gstTrans_Mdl.setItemNetAmount(DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
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
                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
                    //</editor-fold> 
                }
            }

            /*==============================<< Account Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(mrTemp.getSharedKeyFieldCode(), cnTemp);
            AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTemp.getSharedKeyFieldCode(), mrTemp.getDefaultValueMdl().isIncludeTaxInCost(), mrTemp.getDefaultValueMdl().isIncludeOverHeadsInCost(), mrTemp.getDefaultValueMdl().isInventoryAccountingActive(), cnTemp), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", mrTemp.getSupplierBillNo(), mrTemp.getSupplierBillDate(), "", "", false, false, cnTemp);

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                STApprovals.purchaseBillApproval(true, WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (!blnAdd && mrTemp.getModifyReasonMdl() != null) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
            }

            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

//            /*===============================<<update purchase Rate in itemMaster>>=========================*/
//            if (updateItemPurchaseRate){
//                sqlQuery = "update itemMast set"
//                            + "  update im set purchaseRate=pbd.rate "
//                            + " from itemMast im "
//                            + " Left join pBillDetail pbd on im.code=pbd.itemCode "
//                            + "where docid =:docid and pbd.isFooter<>2";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                    psTemp.executeUpdate();
//                    psTemp.close();
//            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;
        ServiceDetail_Mdl serviceDetail_Mdl;

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
            sqlQuery = " SELECT  pb.docid,pb.recId,pb.v_No,pb.recIdPrefix ,pb.v_Type,pb.v_Date"
                    + " , pb.partyBillNo,pb.partyBillDate,pb.subcode,pb.partyType"
                    + " , pb.rcmApplicable ,pb.placeOfSupply ,pb.remark ,pb.preparedBy"
                    + " , pb.u_Name,pb.approvedBy,pb.wtaCode"
                    + " , pb.guiConfigCode ,pb.calcOn,pb.paymentDueDate"
                    + " , pb.u_EntDt_LatestLine,pb.preparedDate"
                    + " , pb.approvalDateTime ,pb.u_EntDt"
                    + " , vt.description AS voucherTypeName"
                    + " , vt.category"
                    + " , sgs.name AS supplierName"
                    + " , c.stateCode AS supplierStateCode"
                    + " , sgs.partyType AS supplierType"
                    + " , pb.gstin AS supplierGstin"
                    + " , pb.gstinDate AS supplierGstinDate"
                    + " , pb.oh_Amt_GrossGoods\n"
                    + " , pb.oh_Amt_GrossService\n"
                    + " , pb.oh_Amt_Gross\n"
                    + " , pb.oh_Amt_Discount\n"
                    + " , pb.oh_Amt_TaxableGoods\n"
                    + " , pb.oh_Amt_TaxableService\n"
                    + " , pb.oh_Amt_Taxable\n"
                    + " , pb.oh_Amt_SGST\n"
                    + " , pb.oh_Amt_UGST\n"
                    + " , pb.oh_Amt_CGST\n"
                    + " , pb.oh_Amt_IGST\n"
                    + " , pb.oh_Amt_SubTotal\n"
                    + " , pb.oh_at_FinalDiscount\n"
                    + " , pb.oh_Amt_FinalDiscount\n"
                    + " , pb.oh_Amt_Total\n"
                    + " , pb.oh_Amt_RoundOff\n"
                    + " , pb.oh_Amt_Net\n"
                    + " , pb.oh_Amt_LandedValue\n"
                    + " , pb.locationCode\n"
                    + " , pb.oh_Ac_FinalDiscount\n"
                    + " , pb.oh_Ac_RoundOff\n"
                    + " , pb.balancingAc\n"
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
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setCalculateOnTag(DmServer.vNull(rsTemp.getString("calcOn"), "0"));
                mrTemp.setCalculateOnText(DmShared.xNull(rsTemp.getString("calcOn")).equalsIgnoreCase("1") ? "Accept Quantity"
                        : DmShared.xNull(rsTemp.getString("calcOn")).equalsIgnoreCase("0") ? "Document Quantity" : "");

                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.setGoodsGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_GrossGoods"), "0.00"));
                mrTemp.setServiceGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_GrossService"), "0.00"));
                mrTemp.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                mrTemp.setGoodsTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_TaxableGoods"), "0.00"));
                mrTemp.setServiceTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_TaxableService"), "0.00"));
                mrTemp.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                if (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() > 0) {
                    mrTemp.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                } else if (DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue() > 0) {
                    mrTemp.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                }
                mrTemp.setSgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                mrTemp.setUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                mrTemp.setCgst(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                mrTemp.setIgst(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                mrTemp.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                mrTemp.setFinalDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_FinalDiscount"), "0.00"));
                mrTemp.setFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));
                mrTemp.setTotalAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Total"), "0.00"));
                mrTemp.setRoundOffAmount(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                mrTemp.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                mrTemp.setLandedValue(DmServer.vNull(rsTemp.getString("oh_Amt_LandedValue"), "0.00"));

                //Accounts
                mrTemp.setFinalDiscountAcCode(rsTemp.getString("oh_Ac_FinalDiscount"));
                mrTemp.setRoundOffAcCode(rsTemp.getString("oh_Ac_RoundOff"));
                mrTemp.setBalancingAc(rsTemp.getString("balancingAc"));

                //Users
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_Name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   pbd.docId  ,pbd.v_SNo ,pbd.pcDocId ,pbd.pcV_SNo ,pbd.poDocId ,pbd.poV_SNo  ,pbd.itemCode"
                    + " , pbd.unitCode ,pbd.hsnCode,hsn.hsn,pbd.docQty,pbd.acceptQty ,pbd.skuQty  ,pbd.returnedQty"
                    + " , pbd.rate"
                    + " , pbd.landedSkuValue\n"
                    + " , pbd.landedSkuRate\n"
                    + " , pbd.skuRate\n"
                    + " , pbd.oh_Amt_Gross ,pbd.mrp"
                    + " , pbd.skuMrp ,pbd.batchNo, pbd.lotNo, pbd.manufacturingDate ,pbd.expiryDate "
                    + " , pbd.colourCode ,pbd.sizeCode,pbd.itemMakeCode   "
                    + " , pbd.itemGSTNature ,pbd.isStatus  ,pbd.ibCode"
                    + " , po.recId AS poRecId"
                    + " , pc.docId AS pcDocId"
                    + " , pc.RecId AS pcRecid"
                    + " , pc.pChallanNo AS pcRefNo"
                    + " , pc.pChallanDate AS pcRefDate"
                    + " , pcd.departmentCode AS pcDepartmentCode"
                    + " , pcd.V_SNo AS pcV_SNo"
                    + " , im.name AS itemName"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , icm.code AS colourCode"
                    + " , icm.name AS colourName"
                    + " , ism.code AS sizeCode"
                    + " , ism.name AS sizeName"
                    + " , imm.code AS itemMakeCode"
                    + " , imm.CompanyName AS itemMakeName"
                    + " , ums.decimalPlaces AS skuAllowDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , um.unit AS unitName"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , ISNULL(pcd.acceptQty,0) - ISNULL(pcd.billedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty"
                    + " , pbd.isManualTax\n"
                    + " , pbd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , pbd.gstTypeCode\n"
                    + " , pbd.oh_Amt_Gross\n"
                    + " , pbd.oh_Amt_Discount\n"
                    + " , pbd.oh_at_Discount\n"
                    + " , pbd.oh_Amt_Taxable\n"
                    + " , pbd.oh_at_SGST\n"
                    + " , pbd.oh_Amt_SGST\n"
                    + " , pbd.oh_at_UGST\n"
                    + " , pbd.oh_Amt_UGST\n"
                    + " , pbd.oh_at_CGST\n"
                    + " , pbd.oh_Amt_CGST\n"
                    + " , pbd.oh_at_IGST\n"
                    + " , pbd.oh_Amt_IGST\n"
                    + " , pbd.oh_Amt_SubTotal\n"
                    + " , pbd.oh_at_FinalDiscount\n"
                    + " , pbd.oh_Amt_FinalDiscount\n"
                    + " , pbd.oh_Amt_Net\n"
                    + " , pbd.oh_Ac_SGST, pbd.oh_Ac_UGST, pbd.oh_Ac_CGST, pbd.oh_Ac_IGST\n"
                    + " , pbd.oh_Ac_SGST_RCM, pbd.oh_Ac_UGST_RCM, pbd.oh_Ac_CGST_RCM, pbd.oh_Ac_IGST_RCM\n"
                    + " , pbd.oh_Ac_Item\n"
                    + " , im.purchaseAc AS imPurchaseAc\n"
                    + " FROM pBillDetail pbd"
                    + " LEFT JOIN pOrder po ON pbd.poDocId = po.docId"
                    + " LEFT JOIN pOrderDetail pod ON pbd.poDocId = pod.docId AND pbd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pChallan pc ON pbd.pcDocId = pc.docId"
                    + " LEFT JOIN pChallanDetail pcd ON pbd.pcDocId = pcd.docId AND pbd.pcV_SNo = pcd.v_SNo"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON pbd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pbd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast as imm ON pbd.itemMakeCode = imm.code "
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pcd.hsnCode \n"
                    + " WHERE pbd.docId = :docId"
                    + " AND ISNULL(pbd.isFooter, 0) = 0"
                    + " ORDER BY pbd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                node.setServiceRecord(false);
                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

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
                double purchaseUnitConversionRate = (skuQty / acceptQty);       //TBC
                node.setPurchaseUnitConversionRate(DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
                node.setPurchaseUnitConversionRate(DmServer.vNull(node.getPurchaseUnitConversionRate()));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setBatchNo(node.getBatchNo());
                ipm.setLotNo(node.getLotNo());
                ipm.setIbCode(node.getIbCode());
                ipm.setColourName(node.getColourName());
                ipm.setSizeName(node.getSizeName());
                ipm.setItemMakeCode(node.getItemMakeName());
                ipm.setMrp(node.getMrp());
                ipm.setSkuMrp(node.getSkuMrp());
                ipm.setExpiryDate(node.getExpiryDate());
                ipm.setManufacturingDate(node.getManufacturingDate());
                node.setPropertiesCsv(DmShared.xNull(ipm.getPropertiesCsv()));

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
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setReturnedQty(DmServer.vNull(rsTemp.getString("returnedQty")));

                //Rate
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));

                //Amount
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                    node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    node.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                    node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    node.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                }
                node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                node.setSgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                node.setUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                node.setCgst(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                node.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                node.setIgst(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                node.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                node.setFinalDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_FinalDiscount"), "0.00"));
                node.setFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));
                node.setItemValue(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                //IAP
                node.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));
                node.setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("imPurchaseAc")));

                //Accounts
                node.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                node.setSgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST_RCM")));

                node.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));
                node.setUgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST_RCM")));

                node.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                node.setCgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST_RCM")));

                node.setIgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST")));
                node.setIgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST_RCM")));

                //Other
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListItemDetail().addAll(lstNode);

            /*==============================<< Get Service detail >>===============================*/
            sqlQuery = "SELECT\n"
                    + "  pbd.v_SNo\n"
                    + " , pbd.itemCode\n"
                    + " , pbd.hsnCode\n"
                    + " , hsn.hsn\n"
                    + " , pbd.itemGSTNature\n"
                    + " , pbd.isStatus\n"
                    + " , im.name AS itemName\n"
                    + " , im.smfCode\n"
                    + " , im.itsBasedOn\n"
                    + " , pbd.unitCode \n"
                    + " , pbd.docQty\n"
                    + " , pbd.rate \n"
                    + " , pbd.isManualTax\n"
                    + " , pbd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , pbd.gstTypeCode\n"
                    + " , pbd.oh_Amt_Gross\n"
                    + " , pbd.oh_Amt_Taxable\n"
                    + " , pbd.oh_at_SGST\n"
                    + " , pbd.oh_Amt_SGST\n"
                    + " , pbd.oh_at_UGST\n"
                    + " , pbd.oh_Amt_UGST\n"
                    + " , pbd.oh_at_CGST\n"
                    + " , pbd.oh_Amt_CGST\n"
                    + " , pbd.oh_at_IGST\n"
                    + " , pbd.oh_Amt_IGST\n"
                    + " , pbd.oh_Amt_SubTotal\n"
                    + " , pbd.oh_Amt_Net \n"
                    + " , pbd.oh_Ac_SGST \n"
                    + " , pbd.oh_Ac_UGST \n"
                    + " , pbd.oh_Ac_CGST \n"
                    + " , pbd.oh_Ac_IGST \n"
                    + " , pbd.oh_Ac_SGST_RCM \n"
                    + " , pbd.oh_Ac_UGST_RCM \n"
                    + " , pbd.oh_Ac_CGST_RCM \n"
                    + " , pbd.oh_Ac_IGST_RCM \n"
                    + " , um.gstUnitCode\n"
                    + " , pbd.oh_Ac_Item\n"
                    + " , pbd.serviceEvalTypeCode\n"
                    + " , pbd.serviceEvalRate\n"
                    + " FROM pBillDetail pbd\n"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code\n"
                    + " LEFT JOIN gstType it ON it.code = pbd.gstTypeCode \n"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code\n"
                    + " LEFT JOIN subGroup sgs ON sgs.subCode = pbd.oh_Ac_SGST \n"
                    + " LEFT JOIN subGroup sgsr ON sgsr.subCode = pbd.oh_Ac_SGST_RCM \n"
                    + " LEFT JOIN subGroup sgu ON sgs.subCode = pbd.oh_Ac_UGST \n"
                    + " LEFT JOIN subGroup sgur ON sgsr.subCode = pbd.oh_Ac_UGST_RCM \n"
                    + " LEFT JOIN subGroup sgc ON sgs.subCode = pbd.oh_Ac_CGST \n"
                    + " LEFT JOIN subGroup sgcr ON sgcr.subCode = pbd.oh_Ac_CGST_RCM \n"
                    + " LEFT JOIN subGroup sgi ON sgi.subCode = pbd.oh_Ac_IGST \n"
                    + " LEFT JOIN subGroup sgir ON sgsr.subCode = pbd.oh_Ac_IGST_RCM \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pbd.hsnCode \n"
                    + " WHERE pbd.docId = :docId\n"
                    + " AND ISNULL(pbd.isFooter, 0) = 2\n"
                    + " ORDER BY pbd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                serviceDetail_Mdl = new ServiceDetail_Mdl();
                serviceDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                serviceDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                serviceDetail_Mdl.setSmfCode(DmShared.xNull(rsTemp.getString("smfCode")));
                serviceDetail_Mdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                serviceDetail_Mdl.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                serviceDetail_Mdl.setV_Sno(DmShared.xNull(rsTemp.getString("v_SNo")));
                serviceDetail_Mdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                serviceDetail_Mdl.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                serviceDetail_Mdl.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                serviceDetail_Mdl.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                serviceDetail_Mdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                serviceDetail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("docQty")));
                serviceDetail_Mdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                serviceDetail_Mdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                serviceDetail_Mdl.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                serviceDetail_Mdl.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                serviceDetail_Mdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                serviceDetail_Mdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                serviceDetail_Mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (!DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")).trim().equalsIgnoreCase("")) {
                    serviceDetail_Mdl.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")));
                    serviceDetail_Mdl.setServiceEvalTypeName(DmEnum.ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypeCode")).intValue()).getName());
                }
                serviceDetail_Mdl.setServiceEvalRate(DmServer.vNull(rsTemp.getString("serviceEvalRate"), "0.00"));

                //GST
                if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                    serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    serviceDetail_Mdl.setSgstOrUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));

                } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                    serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    serviceDetail_Mdl.setSgstOrUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));

                }
                serviceDetail_Mdl.setSgstRate(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                serviceDetail_Mdl.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                serviceDetail_Mdl.setUgstRate(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                serviceDetail_Mdl.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                serviceDetail_Mdl.setCgstRate(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                serviceDetail_Mdl.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                serviceDetail_Mdl.setIgstRate(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                serviceDetail_Mdl.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                serviceDetail_Mdl.setGstRate(DmShared.vNull(DmShared.vNull(serviceDetail_Mdl.getCgstRate())
                        + DmShared.vNull(serviceDetail_Mdl.getSgstOrUgstRate())
                        + DmShared.vNull(serviceDetail_Mdl.getIgstRate())).toString());

                //IAP
                serviceDetail_Mdl.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));

                //Accounts
                serviceDetail_Mdl.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                serviceDetail_Mdl.setSgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST_RCM")));

                serviceDetail_Mdl.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));
                serviceDetail_Mdl.setUgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST_RCM")));

                serviceDetail_Mdl.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                serviceDetail_Mdl.setCgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST_RCM")));

                serviceDetail_Mdl.setIgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST")));
                serviceDetail_Mdl.setIgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST_RCM")));

                mrTemp.getGrdServiceDetail().add(serviceDetail_Mdl);
            }

            /*==============================<< Get GUI >>====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));
            TdsCategoryPopup_SrvImpl.getTDSDetail(strKey, mrTemp.getTdsCatMdl(), cnTemp);
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

    private String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
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

            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(keyCode, cnTemp);

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
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "', '" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "')"
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
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL + "', '" + pvc.PURCHASE_BILL_UNREGISTERED + "', '" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "')"
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

    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst) {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = getGSTS(itemCode, itemRate, itsBasedOn, locationCode, gstTypeCode, isManualGst, cnTemp);
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

    private DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst, Connection cnTemp) throws Exception {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        try {

            dmrTemp = STServer.getGSTS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, locationCode, gstTypeCode, isManualGst, cnTemp);

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            throw e;
        }

        return dmrTemp;
    }

    private void manageBilledQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, pcDocId = "";
        double pbAcceptQty, pcBilledQty, pcAcceptQty;
        Integer isStatus;

        try {

            sqlQuery = "SELECT"
                    + "   pc.docId AS pcDocId"
                    + " , pbd.pcV_SNo"
                    + " , pbd.acceptQty AS pbAcceptQty"
                    + " , pcd.acceptQty AS pcAcceptQty"
                    + " , pcd.billedQty AS pcBilledQty"
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
                    + " ) pbd ON pcd.docId = pbd.pcDocId AND pcd.v_SNo = pbd.pcV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN is Cancelled");
                }
                pcBilledQty = rsTemp.getDouble("pcBilledQty");
                pbAcceptQty = rsTemp.getDouble("pbAcceptQty");
                pcAcceptQty = rsTemp.getDouble("pcAcceptQty");

                if (isDelete) {
                    pcBilledQty -= pbAcceptQty;
                } else {
                    pcBilledQty += pbAcceptQty;
                }

                if (pcBilledQty > pcAcceptQty) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN overflows by " + DmServer.vNull(pcBilledQty - pcAcceptQty));
                } else if (pcBilledQty < 0) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("pcDocId")), DmShared.xNull(rsTemp.getString("pcV_SNo")), "GRN overflows by " + DmServer.vNull(0 - pcBilledQty));
                } else if (pcBilledQty == pcAcceptQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (!pcDocId.equalsIgnoreCase("")) {
                    pcDocId += ",";
                }
                pcDocId += DmShared.iNull(rsTemp.getString("pcDocId")).toString();

                sqlQuery = "UPDATE pChallanDetail SET"
                        + " isStatus = :isStatus,"
                        + " billedQty = :billedQty"
                        + " WHERE docId = :pcDocId AND v_SNo = :pcV_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("pcDocId", DmShared.xNull(rsTemp.getString("pcDocId")));
                psTemp1.setString("pcV_SNo", DmShared.xNull(rsTemp.getString("pcV_SNo")));
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("billedQty", DmServer.vNull(pcBilledQty, "0.0000000"));
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
    public DmResult<TxtHSupplier_Mdl> getSupplierDetails(String supplierCode, String voucherCategory, String v_Type, String v_Date, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<TxtHSupplier_Mdl> rstTemp = new DmResult();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl mrTemp = new TxtHSupplier_Mdl();
        Connection cnTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSupplier(supplierCode, voucherCategory, v_Type, v_Date, helper, blnToEndOfFile, cnTemp);
            if (lstTemp != null) {
                if (lstTemp.size() > 0 && lstTemp.get(0) != null) {
                    mrTemp = lstTemp.get(0);
                }
            }
            rstTemp.setData(mrTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<TxtHSupplier_Mdl> getSupplier(String supplierCode, String voucherCategory, String v_Type, String v_Date, DmHelper helper, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        String condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (!DmShared.xNull(supplierCode).equalsIgnoreCase("")) {
                condition += " AND sg.subCode=:supplierCode";
            }
            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate "
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
            sqlWithParam.setString("v_Date", DmShared.xNull(v_Date).toString());
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            if (!DmShared.xNull(supplierCode).equalsIgnoreCase("")) {
                sqlWithParam.setString("supplierCode", DmShared.xNull(supplierCode));
            }
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
                listNode.setDueDays(DmShared.dNull(rsTemp.getDate("PaymentDueDate")));
                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();

        } catch (Exception ex) {
            throw ex;
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
        return lstTemp;
    }

    @Override
    public DmResult<TxtHVoucherType_Mdl> getVoucherType(String partyType) {
        DmResult<TxtHVoucherType_Mdl> dmrTemp = new DmResult();
        TxtHVoucherType_Mdl voucherType = new TxtHVoucherType_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            /*==============================<< Get default voucher_Type >>===============================*/
            voucherType = getVoucherType(partyType, cnTemp);

            dmrTemp.setData(voucherType);
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

    private TxtHVoucherType_Mdl getVoucherType(String partyType, Connection cnTemp) throws Exception {
        TxtHVoucherType_Mdl voucherType_Mdl = new TxtHVoucherType_Mdl();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            /*==============================<< Get default voucher_Type >>===============================*/

            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " , vt.balancingAc"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(partyType).trim().equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                psTemp.setString("category", pvc.PURCHASE_BILL);

            } else {
                psTemp.setString("category", pvc.PURCHASE_BILL_UNREGISTERED);
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                voucherType_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                voucherType_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                voucherType_Mdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                voucherType_Mdl.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                voucherType_Mdl.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
            }
            rsTemp.close();
            psTemp.close();
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

        return voucherType_Mdl;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
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

    @Override
    public DmResultPageing<List<TxtHHeaderGrnNo_Mdl>> getTxtHHeaderGrnNo(String voucherCategory, String txtHSupplierTag, String txtSupplierBillDate, String docId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHHeaderGrnNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHHeaderGrnNo_Mdl> lstTemp = new ArrayList<>();
        TxtHHeaderGrnNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "", sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL_UNREGISTERED)) {
                condition += " AND vt.Category = '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL)) {
                condition += " AND vt.Category  = '" + pvc.PURCHASE_CHALLAN + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.SERVICE_BILL_UNREGISTERED)) {
                condition += " AND vt.Category = '" + pvc.SERVICE_CHALLAN_UNREGISTERED + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.SERVICE_BILL)) {
                condition += " AND vt.Category  = '" + pvc.SERVICE_CHALLAN + "'";
            }

            if (DmShared.xNull(txtHSupplierTag).equalsIgnoreCase("") || DmShared.xNull(txtSupplierBillDate).equalsIgnoreCase("")) {
                condition += " and  1 = 2";
            }
            if (!DmShared.xNull(txtHSupplierTag).equalsIgnoreCase("")) {
                condition += " and  pc.subCode = :supplierTag ";
            }
            if (!DmShared.xNull(txtSupplierBillDate).equalsIgnoreCase("")) {
                condition += " and  pc.v_Date <= :supplierBillDate ";
            }

            sqlQuery = "SELECT distinct pc.docId AS code, pc.recId, pc.v_Date, "
                    + " Pc.pChallanNo as refNo, pc.pChallanDate as refDate,po.recId as poRecID,po.docID as poDocID "
                    + " FROM pChallan pc "
                    + "	LEFT JOIN pChallanDetail pcd ON pc.docid= pcd.docId "
                    + "	LEFT JOIN pOrder po ON pcd.poDocId= po.docId "
                    + "	LEFT JOIN voucher_Type vt ON vt.v_Type = pc.v_Type "
                    + " LEFT JOIN (SELECT DISTINCT pbd.pcDocId  "
                    + "            FROM pBillDetail pbd"
                    + "            LEFT JOIN pBill pb ON pb.DocId = pbd.docId "
                    + "            LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type"
                    + "            WHERE vt.Category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.SERVICE_BILL + "','" + pvc.SERVICE_BILL_UNREGISTERED + "')"
                    + "                   AND pbd.pcDocId IS NOT NULL "
                    + "                   AND pbd.docId <> :docId  )  vpB on  vpB.pcDocId = pc.docId  "
                    + " WHERE ISNULL(pc.approvedBy, '') <> '' "
                    + " and vpb.pcDocId IS NULL "
                    + " and pc.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("docId", DmShared.iNull("").toString());

            if (!DmShared.xNull(txtHSupplierTag).equalsIgnoreCase("")) {
                sqlWithParam.setString("supplierTag", DmShared.xNull(txtHSupplierTag));
            }
            if (!DmShared.xNull(txtSupplierBillDate).equalsIgnoreCase("")) {
                sqlWithParam.setDate("supplierBillDate", DmServer.convertDate(DmShared.dNull(txtSupplierBillDate)));

            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHeaderGrnNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setChallanNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setChallanDate(DmShared.dNull(rsTemp.getDate("refDate")));
                listNode.setGrnDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setPoNo(DmShared.xNull(rsTemp.getString("poRecID")));
                listNode.setPoDocID(DmShared.xNull(rsTemp.getString("poDocID")));
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

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, boolean includeTaxInCost, boolean includeOverHeadsInCost, boolean isInventoryAccountingActive, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", balancingAcSql = "", serviceSql = "", roundOffSql = "", baseSql = "", partySql = "", taxSql = "", finalDiscountSql = "", purchaseAcSql = "", taxBalancingAcSql = "";
        String topInventoryAc = "", tempBalancingAc = "";
        String sgstContraAc = "", ugstContraAc = "", cgstContraAc = "", igstContraAc = "";
        boolean flag = false;
        String rcmApplicable = "";
        int sr = 0;

        try {
            //<editor-fold defaultstate="collapsed" desc="Prepare Account Posting SQL">
            //Note: Dr: Positive Value, Cr: Negative Value
            sqlQuery = " SELECT \n"
                    + "		pb.rcmApplicable\n"
                    + "	FROM pBill pb\n"
                    + "	WHERE pb.docId = :docId\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                rcmApplicable = DmShared.iNull(rsTemp.getString("rcmApplicable")).toString();
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT \n"
                    + "		TOP 1 pbd.oh_Ac_Item\n"
                    + "	FROM pBill pb\n"
                    + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                    + "	WHERE pb.docId = :docId\n"
                    + "	AND pbd.isFooter = 0\n"
                    + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                    + " ORDER BY pbd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                topInventoryAc = DmShared.xNull(rsTemp.getString("oh_Ac_Item"));
            }
            rsTemp.close();
            psTemp.close();

            tempBalancingAc = isInventoryAccountingActive ? "pb.balancingAc" : ":topInventoryAc";

            ++sr;
            partySql = "-- Net Amount Posting\n"
                    + "SELECT \n"
                    + "	pb.subCode\n"
                    + "	, " + tempBalancingAc + " AS contraSub\n"
                    + "	, -ISNULL(pb.oh_Amt_Net, 0) AS amount\n"
                    + "	, " + sr + " AS sr\n"
                    + "FROM pBill pb\n"
                    + "WHERE pb.docId = :docId\n"
                    + "AND pb.subCode IS NOT NULL\n"
                    + "AND ISNULL(pb.oh_Amt_Net, 0) <> 0\n";

            if (isInventoryAccountingActive) {
                if (includeOverHeadsInCost) {
                    ++sr;
                    inventorySql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                            + "FROM (\n"
                            + "	-- Inventory Posting\n"
                            + "	SELECT \n"
                            + "		pbd.oh_Ac_Item AS subCode\n"
                            + "		, pb.subCode AS contraSub\n"
                            + "		, (IsNull(pbd.landedSkuValue, 0) - IsNull(pcd.landedSkuValue, 0)) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                            + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pbd.pcDocId AND pcd.v_SNo = pbd.pcV_SNo \n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pbd.isFooter = 0\n"
                            + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                            + "	AND (IsNull(pbd.landedSkuValue, 0) - IsNull(pcd.landedSkuValue, 0)) <> 0\n"
                            + "\n"
                            + "	) t\n"
                            + "GROUP BY t.subCode\n";

                    ++sr;
                    serviceSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                            + "FROM (\n"
                            + "	-- Service Posting\n"
                            + "	SELECT \n"
                            + "		pcd.oh_Ac_Item AS subCode\n"
                            + "		, pb.subCode AS contraSub\n"
                            + "		, ISNULL(pcd.oh_Amt_Taxable, 0) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN ( \n"
                            + "         SELECT DISTINCT pbd.docId, pbd.pcDocId \n"
                            + "         FROM pBillDetail pbd \n"
                            + "         WHERE pbd.docId = :docId \n"
                            + "         AND pbd.isFooter = 0\n"
                            + "         AND pbd.pcDocId IS NOT NULL\n"
                            + "         ) pbd ON pbd.docId = pb.docId \n"
                            + "     LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId\n"
                            + "     LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId\n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pcd.isFooter = 2\n"
                            + "	AND pcd.oh_Ac_Item IS NOT NULL\n"
                            + "	AND ISNULL(pcd.oh_Amt_Taxable, 0) <> 0\n"
                            + "	) t\n"
                            + "GROUP BY t.subCode\n";
                } else {
//                    Service posting in case of Overhead Not Included In Cost
                    ++sr;
                    serviceSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                            + "FROM (\n"
                            + "	-- Inventory Posting\n"
                            + "	SELECT \n"
                            + "		pbd.oh_Ac_Item AS subCode\n"
                            + "		, pb.subCode AS contraSub\n"
                            + "		, (IsNull(pbd.oh_Amt_Gross, 0)) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pbd.isFooter = 2\n"
                            + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                            //                            + "	AND (IsNull(pbd.landedSkuValue, 0) - IsNull(pcd.landedSkuValue, 0)) <> 0\n"
                            + "\n"
                            + "	) t\n"
                            + "GROUP BY t.subCode\n";
                }

                if (includeTaxInCost) {
                    sgstContraAc = "pb.subCode";
                    ugstContraAc = "pb.subCode";
                    cgstContraAc = "pb.subCode";
                    igstContraAc = "pb.subCode";

                    taxBalancingAcSql = "\n"
                            + "	UNION ALL\n"
                            + "\n"
                            + "	-- SGST Posting\n"
                            + getTaxPostingSql("pb.balancingAc", sgstContraAc, "pcd.oh_Amt_SGST", true, includeOverHeadsInCost)
                            + "\n"
                            + "	UNION ALL\n"
                            + "\n"
                            + "	-- UGST Posting\n"
                            + getTaxPostingSql("pb.balancingAc", ugstContraAc, "pcd.oh_Amt_UGST", true, includeOverHeadsInCost)
                            + "\n"
                            + "	UNION ALL \n"
                            + "\n"
                            + "	-- CGST Posting\n"
                            + getTaxPostingSql("pb.balancingAc", cgstContraAc, "pcd.oh_Amt_CGST", true, includeOverHeadsInCost)
                            + "\n"
                            + "	UNION ALL\n"
                            + "\n"
                            + "	-- IGST Posting\n"
                            + getTaxPostingSql("pb.balancingAc", igstContraAc, "pcd.oh_Amt_IGST", true, includeOverHeadsInCost);
                }

                ++sr;
                balancingAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Balancing A/c Posting\n"
                        + "	SELECT \n"
                        + "		pb.balancingAc AS subCode\n"
                        + "		, pb.subCode AS contraSub\n"
                        + "		, ISNULL(pcd.oh_Amt_Taxable, 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN ( \n"
                        + "         SELECT DISTINCT pbd.docId, pbd.pcDocId \n"
                        + "         FROM pBillDetail pbd \n"
                        + "         WHERE pbd.docId = :docId \n"
                        + "         AND pbd.isFooter = 0\n"
                        + "         AND pbd.pcDocId IS NOT NULL\n"
                        + "         ) pbd ON pbd.docId = pb.docId \n"
                        + "     LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId\n"
                        + "     LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId\n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pcd.isFooter = 0\n"
                        + "	AND pb.balancingAc IS NOT NULL\n"
                        + "	AND ISNULL(pcd.oh_Amt_Taxable, 0) <> 0\n"
                        + "" + taxBalancingAcSql
                        + "	) t\n"
                        + "GROUP BY t.subCode\n";

                if (includeOverHeadsInCost) {
                    ++sr;
                    finalDiscountSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                            + "FROM (\n"
                            + "	-- Final Discount Posting\n"
                            + "	SELECT \n"
                            + "		pc.oh_Ac_FinalDiscount AS subCode\n"
                            + "		, pb.balancingAc AS contraSub\n"
                            + "		, -ISNULL(pc.oh_Amt_FinalDiscount, 0) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN ( \n"
                            + "         SELECT DISTINCT pbd.docId, pbd.pcDocId \n"
                            + "         FROM pBillDetail pbd \n"
                            + "         WHERE pbd.docId = :docId \n"
                            + "         AND pbd.isFooter = 0\n"
                            + "         AND pbd.pcDocId IS NOT NULL\n"
                            + "         ) pbd ON pbd.docId = pb.docId \n"
                            + "     LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId\n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pc.oh_Ac_FinalDiscount IS NOT NULL\n"
                            + "	AND ISNULL(pc.oh_Amt_FinalDiscount, 0) <> 0\n"
                            + "	) t\n"
                            + "GROUP BY t.subCode\n";

                    ++sr;
                    roundOffSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                            + "FROM (\n"
                            + "	-- Roundoff Posting\n"
                            + "	SELECT \n"
                            + "		pc.oh_Ac_RoundOff AS subCode\n"
                            + "		, CASE WHEN ISNULL(pc.oh_Amt_RoundOff, 0) > 0 THEN pb.subCode ELSE pb.balancingAc END AS contraSub\n"
                            + "		, ISNULL(pc.oh_Amt_RoundOff, 0) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN ( \n"
                            + "         SELECT DISTINCT pbd.docId, pbd.pcDocId \n"
                            + "         FROM pBillDetail pbd \n"
                            + "         WHERE pbd.docId = :docId \n"
                            + "         AND pbd.isFooter = 0\n"
                            + "         AND pbd.pcDocId IS NOT NULL\n"
                            + "         ) pbd ON pbd.docId = pb.docId \n"
                            + "     LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId\n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pc.oh_Ac_RoundOff IS NOT NULL\n"
                            + "	AND ISNULL(pc.oh_Amt_RoundOff, 0) <> 0\n"
                            + "	) t\n"
                            + "GROUP BY t.subCode\n";
                } else {
                    ++sr;
                    finalDiscountSql = "-- Final Discount Posting\n"
                            + "SELECT \n"
                            + "	pb.oh_Ac_FinalDiscount AS subCode\n"
                            + "	, pb.subCode AS contraSub\n"
                            + "	, -ISNULL(pb.oh_Amt_FinalDiscount, 0) AS amount\n"
                            + "	, " + sr + " AS sr\n"
                            + "FROM pBill pb\n"
                            + "WHERE pb.docId = :docId\n"
                            + "AND pb.oh_Ac_FinalDiscount IS NOT NULL\n"
                            + "AND ISNULL(pb.oh_Amt_FinalDiscount, 0) <> 0\n";

                    ++sr;
                    roundOffSql = "-- Roundoff Posting\n"
                            + "SELECT \n"
                            + "	pb.oh_Ac_RoundOff AS subCode\n"
                            + "	, pb.subCode AS contraSub\n"
                            + "	, ISNULL(pb.oh_Amt_RoundOff, 0) AS amount\n"
                            + "	, " + sr + " AS sr\n"
                            + "FROM pBill pb\n"
                            + "WHERE pb.docId = :docId \n"
                            + "AND pb.oh_Ac_RoundOff IS NOT NULL\n"
                            + "AND ISNULL(pb.oh_Amt_RoundOff, 0) <> 0\n";
                }

            } else {
                ++sr;
                purchaseAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Purchase A/c Posting\n"
                        + "	SELECT \n"
                        + "		pbd.oh_Ac_Item AS subCode\n"
                        + "		, pb.subCode AS contraSub\n"
                        + "		, IsNull(pbd.oh_Amt_Taxable, 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pbd.isFooter = 0\n"
                        + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                        + "	AND ISNULL(pbd.oh_Amt_Taxable, 0) <> 0\n"
                        + "\n"
                        + "	) t\n"
                        + "GROUP BY t.subCode\n";

                ++sr;
                serviceSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Service Posting\n"
                        + "	SELECT \n"
                        + "		pbd.oh_Ac_Item AS subCode\n"
                        + "		, pb.subCode AS contraSub\n"
                        + "		, ISNULL(pbd.oh_Amt_Taxable, 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pbd.isFooter = 2\n"
                        + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                        + "	AND ISNULL(pbd.oh_Amt_Taxable, 0) <> 0\n"
                        + "	) t\n"
                        + "GROUP BY t.subCode\n";

                ++sr;
                finalDiscountSql = "-- Final Discount Posting\n"
                        + "SELECT \n"
                        + "	pb.oh_Ac_FinalDiscount AS subCode\n"
                        + "	, :topInventoryAc AS contraSub\n"
                        + "	, -ISNULL(pb.oh_Amt_FinalDiscount, 0) AS amount\n"
                        + "	, " + sr + " AS sr\n"
                        + "FROM pBill pb\n"
                        + "WHERE pb.docId = :docId\n"
                        + "AND pb.oh_Ac_FinalDiscount IS NOT NULL\n"
                        + "AND ISNULL(pb.oh_Amt_FinalDiscount, 0) <> 0\n";

                ++sr;
                roundOffSql = "-- Roundoff Posting\n"
                        + "SELECT \n"
                        + "	pb.oh_Ac_RoundOff AS subCode\n"
                        + "	, CASE WHEN ISNULL(pb.oh_Amt_RoundOff, 0) > 0 THEN pb.subCode ELSE :topInventoryAc END AS contraSub\n"
                        + "	, ISNULL(pb.oh_Amt_RoundOff, 0) AS amount\n"
                        + "	, " + sr + " AS sr\n"
                        + "FROM pBill pb\n"
                        + "WHERE pb.docId = :docId\n"
                        + "AND pb.oh_Ac_RoundOff IS NOT NULL\n"
                        + "AND ISNULL(pb.oh_Amt_RoundOff, 0) <> 0\n";

            }

            if (DmShared.iNull(rcmApplicable).intValue() == 1) {
                sgstContraAc = "pbd.oh_Ac_SGST_RCM";
                ugstContraAc = "pbd.oh_Ac_UGST_RCM";
                cgstContraAc = "pbd.oh_Ac_CGST_RCM";
                igstContraAc = "pbd.oh_Ac_IGST_RCM";
            } else {
                sgstContraAc = "pb.subCode";
                ugstContraAc = "pb.subCode";
                cgstContraAc = "pb.subCode";
                igstContraAc = "pb.subCode";
            }

            ++sr;
            taxSql = "-- Tax Posting\n"
                    + "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- SGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_SGST", sgstContraAc, "pbd.oh_Amt_SGST", false, includeOverHeadsInCost)
                    + "\n"
                    + "	UNION ALL\n"
                    + "\n"
                    + "	-- UGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_UGST", ugstContraAc, "pbd.oh_Amt_UGST", false, includeOverHeadsInCost)
                    + "\n"
                    + "	UNION ALL \n"
                    + "\n"
                    + "	-- CGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_CGST", cgstContraAc, "pbd.oh_Amt_CGST", false, includeOverHeadsInCost)
                    + "\n"
                    + "	UNION ALL\n"
                    + "\n"
                    + "	-- IGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_IGST", igstContraAc, "pbd.oh_Amt_IGST", false, includeOverHeadsInCost)
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";

            baseSql = partySql
                    + "\n"
                    + "UNION ALL\n"
                    + "\n"
                    + serviceSql
                    + "\n"
                    + "UNION ALL\n"
                    + "\n"
                    + taxSql
                    + "\n"
                    + "UNION ALL\n"
                    + "\n"
                    + finalDiscountSql
                    + "\n"
                    + "UNION ALL\n"
                    + "\n"
                    + roundOffSql;
            if (isInventoryAccountingActive && !includeOverHeadsInCost) {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + balancingAcSql
                        + "\n";

            } else if (isInventoryAccountingActive && includeOverHeadsInCost) {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + inventorySql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + balancingAcSql
                        + "\n";

            } else {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + purchaseAcSql
                        + "\n";
            }
            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            if (!isInventoryAccountingActive) {
                psTemp.setString("topInventoryAc", DmShared.xNull(topInventoryAc));
            }
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() != 0) {
                    throw new DmException(0, "Error in ledger posting.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT t.subCode, t.contraSub, t.amount, sgc.name as contraSubName\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n"
                    + "LEFT JOIN subGroup sgc ON sgc.subCode = t.contraSub\n"
                    + "ORDER BY sr";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            if (!isInventoryAccountingActive) {
                psTemp.setString("topInventoryAc", DmShared.xNull(topInventoryAc));
            }
            rsTemp = psTemp.executeQuery();
            //</editor-fold>
            flag = false;
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (!flag) {
                        flag = true;
                    }
                    apm = new AccountPosting_Mdl();
                    apm.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                    apm.setContraCode(DmShared.xNull(rsTemp.getString("contraSub")));
                    apm.setContraText(DmShared.xNull(rsTemp.getString("contraSubName")));
                    if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() > 0) {
                        apm.setAmtDr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtCr("");
                    } else if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() < 0) {
                        apm.setAmtCr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtDr("");
                    }
                    lstApm.add(apm);
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!flag) {
                throw new DmException(0, "Error in ledger posting.");
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

        return lstApm;
    }

    private String getTaxPostingSql(String subCodeField, String contraAcField, String amountField, boolean isPurchaseChallanTax, boolean includeOverHeadsInCost) throws Exception {
        String returnValue = "";
        try {
            if (isPurchaseChallanTax == false) {
                returnValue = " SELECT \n"
                        + "		" + subCodeField + " AS subCode\n"
                        + "		, " + contraAcField + " AS contraSub\n"
                        + "		, ISNULL(" + amountField + ", 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pbd.isFooter <> 1\n"
                        + "	AND " + subCodeField + " IS NOT NULL\n"
                        + "	AND ISNULL(" + amountField + ", 0) <> 0\n";
                returnValue += " UNION ALL \n"
                        + "	SELECT \n"
                        + "		" + contraAcField + " AS subCode\n"
                        + "		, " + subCodeField + " AS contraSub\n"
                        + "		, -ISNULL(" + amountField + ", 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pbd.isFooter <> 1\n"
                        + "	AND " + contraAcField + " IS NOT NULL\n"
                        + "	AND ISNULL(" + amountField + ", 0) <> 0\n"
                        + "	AND ISNULL(pb.rcmApplicable, 0) = 1\n"
                        + "";
            } else {
                returnValue = " SELECT \n"
                        + "		" + subCodeField + " AS subCode\n"
                        + "		, " + contraAcField + " AS contraSub\n"
                        + "		, ISNULL(" + amountField + ", 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN ( \n"
                        + "         SELECT DISTINCT pbd.docId, pbd.pcDocId \n"
                        + "         FROM pBillDetail pbd \n"
                        + "         WHERE pbd.docId = :docId \n"
                        + "         AND pbd.isFooter = 0\n"
                        + "         AND pbd.pcDocId IS NOT NULL\n"
                        + "         ) pbd ON pbd.docId = pb.docId \n"
                        + " LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId\n"
                        + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        //                        + "	AND pcd.isFooter <> 1\n"
                        + (includeOverHeadsInCost ? "AND pcd.isFooter <> 1\n" : "AND pcd.isFooter =0 \n")
                        + "	AND " + subCodeField + " IS NOT NULL\n"
                        + "	AND ISNULL(" + amountField + ", 0) <> 0\n";
            }
        } catch (Exception e) {
            throw e;
        }
        return returnValue;
    }

    //</editor-fold>    
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

//
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
                    STApprovals.purchaseBillApproval(true, WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
