package aarogya.inventory.purchaseBillDirect.server;

import static dataman.dmToolkit.stock.server.Stock_Server.deleteUnusedItemBatches;

import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.accountPosting.shared.AccountPosting_Mdl;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.purchaseBillDirect.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.Find_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.GrdBillOtherDetails_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.GrdImpurity_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.GrdLabEntry_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_Srv;
import aarogya.inventory.purchaseBillDirect.shared.PurchaseBillDirect_Vld;
import aarogya.inventory.purchaseBillDirect.shared.TxtHGateEntry_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHItemName_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHOrderNo_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHTransporter_Mdl;
import aarogya.inventory.purchaseBillDirect.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.server.ItemUpdateServer;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdate_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ImpurityNature;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.tdsCategoryPopup.server.TdsCategoryPopup_SrvImpl;
import dataman.dmToolkit.tdsCategoryPopup.shared.TdsCategoryPopup_Mdl;
import java.sql.SQLException;

public class PurchaseBillDirect_SrvImpl extends DmRSSPrivate implements PurchaseBillDirect_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pb.V_Type", voucherDateFieldName = "pb.V_Date";

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
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " LEFT JOIN subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "	WHERE sgsl.site_Code = :siteCode and sgn.manualCode IN ('CM','PR')"
                    + " and sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCalculateOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Accept Qty.");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Doc. Qty.");
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type, String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", itemCatFilteration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            condition += " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)";
            condition += " AND im.isActive = 1";
            condition += " AND sl.site_Code = :site_Code";

            if (!DmShared.xNull(restrictedDepartment).equalsIgnoreCase("")) {
                condition += " AND vird.code IS NULL ";
            } else {
                condition += " AND 1 = 2";
            }
            if (!DmShared.xNull(skuCode).equalsIgnoreCase("")
                    && !DmShared.xNull(purchaseUnitCode).equalsIgnoreCase("")) {
                condition += " AND im.sku = :skuCode";
                condition += " AND im.purchaseUnit = :purchaseUnitCode";
            } else {
                condition += " AND ISNULL(im.isPOMandatory, 0) = 0";
            }

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlQuery = "SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , im.manualCode"
                    + " , im.hsnCode"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , im.sku AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , im.isLabRequired, im.purchaseRate "
                    + " , im.subCode AS iSubCode "
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , hsn.hsn "
                    + " , im.purchaseAc "
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + " LEFT JOIN hsnMast hsn on hsn.code = im.hsnCode";
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ("
                        + "         SELECT ird.code "
                        + "         FROM itemRestrictedToDepartment ird "
                        + "         WHERE ird.departmentCode = :departmentCode"
                        + "     ) vird ON vird.code = im.Code ";
            }
            sqlQuery += condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlWithParam.setString("departmentCode", DmShared.xNull(restrictedDepartment));
            }
            if (!DmShared.xNull(skuCode).equalsIgnoreCase("")
                    && !DmShared.xNull(purchaseUnitCode).equalsIgnoreCase("")) {
                sqlWithParam.setString("skuCode", DmShared.xNull(skuCode));
                sqlWithParam.setString("purchaseUnitCode", DmShared.xNull(purchaseUnitCode));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));

                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                listNode.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                listNode.setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));
                listNode.setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));

                //IAP
                listNode.setiSubcode(DmShared.xNull(rsTemp.getString("iSubCode")));
                listNode.setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("purchaseAc")));

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
    public DmResultPageing<List<TxtHOrderNo_Mdl>> getTxtHOrderNo(String subCode, String v_Date, String docId, String v_SNo, String restrictedDepartment, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHOrderNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHOrderNo_Mdl> lstTemp = new ArrayList<TxtHOrderNo_Mdl>();
        TxtHOrderNo_Mdl listNode;
        ItemProperty_Mdl ipm;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", pendingQty = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty";
                condition += " AND (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pbd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
            } else {
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) AS pendingQty";
                condition += " AND ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) > 0";
                condition += " AND pod.isStatus = 0";
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND po.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            if (!DmShared.xNull(restrictedDepartment).equalsIgnoreCase("")) {
                condition += " AND vird.code IS NULL ";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT "
                    + "   po.docId AS poDocId"
                    + " , po.recId AS poRecId"
                    + " , po.refNo AS poRefNo"
                    + " , pod.v_SNo AS poV_SNo"
                    + " , pod.quantity AS orderQty"
                    + " , pod.challanedQty"
                    + " , pod.rate"
                    + " , pod.itemCode"
                    + " , im.name AS itemName"
                    + " , pod.colourCode"
                    + " , icm.name AS colourName"
                    + " , pod.sizeCode"
                    + " , ism.name AS sizeName"
                    + " , pod.itemMakeCode"
                    + " , imm.CompanyName AS itemMakeName"
                    + " , pod.unitCode"
                    + " , um.unit AS unitName" + pendingQty
                    ///////////////////////////////////////
                    + " , im.hsnCode"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.isLabRequired "
                    + " , im.propertyList"
                    + " , im.sku AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , pod.unitCode AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , pod.oh_At_Discount, pod.oh_Amt_Discount"
                    + " , im.subCode AS iSubCode \n"
                    + " , hsn.hsn, pod.isManualGstType \n"
                    + " , it.name AS gstTypeName\n"
                    + " , pod.gstTypeCode\n"
                    + " , im.purchaseAc "
                    ////////////////////////////////////////
                    + " FROM POrderDetail pod"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.poDocId,pbd.poV_SNo,pbd.acceptQty "
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + "     AND pbd.v_SNo = :v_SNo"
                        + " ) AS pbd ON pod.docId = pbd.poDocId AND pod.v_SNO = pbd.poV_SNo";
            }
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ("
                        + "         SELECT ird.code "
                        + "         FROM itemRestrictedToDepartment ird "
                        + "         WHERE ird.departmentCode = :departmentCode"
                        + "   ) vird ON vird.code = im.Code ";
            }
            sqlQuery += " LEFT JOIN itemColourMast icm ON pod.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pod.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast as imm ON pod.itemMakeCode = imm.code "
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code"
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN hsnMast hsn on hsn.code = im.hsnCode \n"
                    + " LEFT JOIN gstType it ON it.code = pod.gstTypeCode \n"
                    + "	WHERE vt.category IN (:vCat,:vCatU) "
                    + " AND po.subCode = :subCode"
                    + " AND isNull(pod.isFooter,0) = 0 "
                    + " AND ISNULL(po.approvedBy,'') <> ''"
                    + " AND po.site_Code = :site_Code"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("vCat", pvc.PURCHASE_ORDER);
            sqlWithParam.setString("vCatU", pvc.PURCHASE_ORDER_UNREGISTERED);
            sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
                sqlWithParam.setString("v_SNo", DmShared.xNull(v_SNo));
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlWithParam.setString("departmentCode", restrictedDepartment);
            }
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHOrderNo_Mdl();
                listNode.setSharedKeyFieldCode((DmShared.iNull(rsTemp.getString("poDocId")).toString()));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("poRecId")));
                listNode.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                listNode.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));
                listNode.setOrderQty(DmServer.vNull(rsTemp.getString("orderQty")));
                listNode.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));

                double skuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                listNode.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                listNode.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                listNode.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                listNode.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));

                ipm = new ItemProperty_Mdl();
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                listNode.setProperties(DmShared.xNull(ipm.getPropertiesCsv()));

                listNode.getTxtHItemName_Mdl().setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.getTxtHItemName_Mdl().setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.getTxtHItemName_Mdl().setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.getTxtHItemName_Mdl().setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.getTxtHItemName_Mdl().setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.getTxtHItemName_Mdl().setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.getTxtHItemName_Mdl().setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.getTxtHItemName_Mdl().setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));
                listNode.getTxtHItemName_Mdl().setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.getTxtHItemName_Mdl().setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.getTxtHItemName_Mdl().setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.getTxtHItemName_Mdl().setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                listNode.getTxtHItemName_Mdl().setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.getTxtHItemName_Mdl().setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.getTxtHItemName_Mdl().setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.getTxtHItemName_Mdl().setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.getTxtHItemName_Mdl().setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.getTxtHItemName_Mdl().setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                listNode.getTxtHItemName_Mdl().setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                //IAP
                listNode.getTxtHItemName_Mdl().setiSubcode(DmShared.xNull(rsTemp.getString("iSubCode")));
                listNode.getTxtHItemName_Mdl().setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("purchaseAc")));

                //Rate
                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));

                //Amount
                double amount = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("rate"));
                listNode.setAmount(DmServer.vNull(amount, "0.00"));
                listNode.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_Discount"), "0.00"));
                listNode.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));

                //GST
                listNode.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                listNode.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                listNode.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHImpurity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT im.code, im.name "
                    + " FROM impurityMast im "
                    + " where im.nature = " + STEnum.ImpurityNature.QC.getId() + " ";
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
    public DmResult<List<GrdItemDetail_Mdl>> getOrderDetail(String v_Type, List<KeyFieldExtended> keyFields, String subCode, String location, String billDate,
            String docId, String v_Date, String isSiteStateUT, String rcmApplicable, boolean isIncludeTaxInCost, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        GrdLabEntry_Mdl grdLabEntry_Mdl;
        String sqlQuery, conditionLines = "", condition = "", pendingQty = "", itemCatFilteration = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pbd.acceptQty,0) AS pendingQty";
                condition += " AND (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pbd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
            } else {
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) AS pendingQty";
                condition += " AND ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) > 0";
                condition += " AND pod.isStatus = 0";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND po.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            /*==============================<< Get Order detail (via list) >>===============================*/
            for (KeyFieldExtended field : keyFields) {
                if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                        && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {
                    if (!conditionLines.trim().equals("")) {
                        conditionLines += " OR ";
                    }
                    conditionLines += " ( pod.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "'";
                    conditionLines += " AND pod.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "')";
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

            sqlQuery = "SELECT  pod.itemCode, im.name as itemName, im.sku, ums.unit as skuName, ums.decimalPlaces AS skuDecimalPlaces, "
                    + "		po.docId as poDocId, po.recId as poRecId, po.refNo AS poRefNo, pod.v_SNo AS poV_SNo,  pod.unitCode,"
                    + "		um.unit as unitName, um.gstUnitCode, pod.quantity as orderQty, pod.challanedQty, pod.rate,"
                    + "		um.decimalPlaces AS unitDecimalPlaces, im.propertyList, im.purchaseUnitConversionRate,"
                    + "         pod.colourCode, icm.name AS colourName, pod.sizeCode, ism.name AS sizeName,"
                    + "         pod.itemMakeCode,imm.CompanyName as itemMakeName,im.isLabRequired,im.purchaseRate,"
                    + "         im.itsBasedOn, im.isBestBefore ,im.subCode AS iSubCode, pod.hsnCode, hsn.hsn, pod.itemGSTNature, \n"
                    + "         pod.isManualGstType, pod.gstTypeCode, it.name AS gstTypeName\n "
                    + "         , pod.oh_Amt_Gross\n"
                    + "         , pod.oh_Amt_Discount\n"
                    + "         , pod.oh_at_Discount\n"
                    + "         , pod.oh_Amt_Taxable\n"
                    + "         , pod.oh_at_SGST\n"
                    + "         , pod.oh_at_UGST\n"
                    + "         , pod.oh_at_CGST\n"
                    + "         , pod.oh_at_IGST\n"
                    + "         , im.purchaseAc\n "
                    + pendingQty
                    + " FROM POrderDetail pod"
                    + "	LEFT JOIN pOrder po ON pod.docId = po.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.poDocId,pbd.poV_SNo,pbd.acceptQty "
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + " ) AS pbd ON pod.docId = pbd.poDocId AND pod.v_SNO = pbd.poV_SNo";
            }
            sqlQuery += " LEFT JOIN itemMast im ON pod.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode"
                    + " LEFT JOIN itemMakeMast as imm ON imm.code = pod.itemMakeCode "
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code"
                    + " LEFT JOIN gstType it ON it.code = pod.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pod.hsnCode \n"
                    + "	WHERE po.subCode = :subCode"
                    + " AND po.site_Code = :site_Code"
                    + " AND isNull(pod.isFooter,0) = 0 "
                    + " AND isNull(po.approvedBy,'') <> ''" + condition
                    + "	ORDER BY po.recId, im.name";
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
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                node.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                node.setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));

                //Properties
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourName(node.getColourName());
                ipm.setSizeName(node.getSizeName());
                ipm.setItemMakeName(node.getItemMakeName());
                node.setPropertiesCsv(DmShared.xNull(ipm.getPropertiesCsv()));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("sku")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("unitDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                node.setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //Quantities
                node.setChallanQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setRecQty(DmServer.vNull(rsTemp.getString("pendingQty")));
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

                //Purchase A/c (Based On Item Master)
                node.setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("purchaseAc")));
                if (isInventoryAccountingActive) {
                    //IAP
                    node.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
                } else {
                    if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                        if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.ITEM.getId()) {
                            node.setIsubCode(DmShared.xNull(rsTemp.getString("purchaseAc")));
                        }
                    }
                }
                if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1) {
                    node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                    node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));

                    if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                        node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                        node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    }

                    node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                    node.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
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

                //fill QC Parameter Info
                node.getLstImpurity().addAll(getQCPaparameter(node.getSharedKeyFieldCode(), cnTemp));

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

    @Override
    public DmResultPageing<List<GrdItemDetail_Mdl>> getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param) {
        DmResultPageing<List<GrdItemDetail_Mdl>> dmrTemp = new DmResultPageing<List<GrdItemDetail_Mdl>>();
        List<GrdItemDetail_Mdl> lstTemp = new ArrayList<GrdItemDetail_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        GrdItemDetail_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "", supplierCondition = "";
        dmrTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).trim().equals("")) {
                condition += " AND pcd.docId != :docId";
            }
            if (!DmShared.xNull(v_SNo).trim().equals("")) {
                condition += " AND pcd.v_SNo != :v_SNo";
            }
            if (DmShared.iNull(supplierCode).longValue() != 0) {
                if (flagCurrentSupplier) {
                    supplierCondition += " AND pc.subcode = :supplierCode";
                } else {
                    supplierCondition += " AND pc.subcode != :supplierCode";
                }
            }

            /*==============================<< Get Previous Grn detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   pc.pChallanDate AS refDate"
                    + " , pc.pChallanNo AS refNo"
                    + " , sgs.name AS supplierName"
                    + " , ct.cityName"
                    + " , um.unit AS purchaseUnitName"
                    + " , pcd.acceptQty"
                    + " , pcd.rate"
                    + " , pcd.oh_At_Discount"
                    + " , ums.unit AS skuName"
                    + " , pcd.skuQty"
                    + " , pcd.skuRate"
                    + " , pcd.landedSkuValue"
                    + " , pcd.v_SNo"
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN subGroup sgs ON pc.subcode = sgs.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sgs.cityCode"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " AND pcd.itemCode = :itemCode" + supplierCondition + condition;
            sqlQuery += " UNION ALL ";
            sqlQuery += "SELECT"
                    + "   pc.partyBillDate AS refDate"
                    + " , pc.partyBillNo AS refNo"
                    + " , sgs.name AS supplierName"
                    + " , ct.cityName"
                    + " , um.unit AS purchaseUnitName"
                    + " , pcd.acceptQty"
                    + " , pcd.rate"
                    + " , pcd.oh_At_Discount"
                    + " , ums.unit AS skuName"
                    + " , pcd.skuQty"
                    + " , pcd.skuRate"
                    + " , pcd.landedSkuValue"
                    + " , pcd.v_SNo"
                    + " FROM pBillDetail pcd"
                    + " LEFT JOIN pBill pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN subGroup sgs ON pc.subcode = sgs.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sgs.cityCode"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " AND pcd.itemCode = :itemCode" + supplierCondition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", DmShared.xNull(itemCode));
            if (DmShared.iNull(supplierCode).longValue() != 0) {
                sqlWithParam.setString("supplierCode", DmShared.xNull(supplierCode));
            }
            if (!DmShared.xNull(docId).trim().equals("")) {
                sqlWithParam.setString("docId", DmShared.xNull(docId));
            }
            if (!DmShared.xNull(v_SNo).trim().equals("")) {
                sqlWithParam.setString("v_SNo", DmShared.xNull(v_SNo));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                node.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                node.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                if (!DmShared.xNull(rsTemp.getString("cityName")).trim().equals("")) {
                    node.setSupplierName(node.getSupplierName() + ", " + DmShared.xNull(rsTemp.getString("cityName")));
                }
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("PurchaseUnitName")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(helper.getPaging());

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

    private WalkThroughDefault_Mdl getWalkThroughFor(PurchaseBillDirect_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
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
//            defaultValue.getLstSaleRateRule().addAll(STServer.getSaleRateRule(cnTemp));
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
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.SinglePOFlowInPurchase,e.inv_DefaultPurchaseDepartment, \n"
                    + "sg.name as defaultPurchaseDepartmentName,e.isIncludeOverHeadsInCost,isNull(e.ac_TDS_Applicable_On,0) as ac_TDS_Applicable_On \n"
                    + "FROM enviro e\n"
                    + "left join subGroup sg on e.inv_DefaultPurchaseDepartment=sg.subCode \n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isIncludeTaxInCost") == null) {
                    throw new DmException(0, "Include Tax in Cost is not defined in Environment Setting.");
                } else if (rsTemp.getString("purchaseSalesAcPostingBasedOn") == null) {
                    throw new DmException(0, "Purchase/Sales A/c Posting Based On is not defined in Environment Setting.");
                }

                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setIncludeOverHeadsInCost(DmShared.iNull(rsTemp.getString("isIncludeOverHeadsInCost")).intValue() == 1);
                defaultValue.setPurchaseAcPostingBasedOn(DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")));
                defaultValue.setSinglePOFlowInPurchase(DmShared.iNull(rsTemp.getString("SinglePOFlowInPurchase")).intValue() == 1);
                defaultValue.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                defaultValue.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
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
    public DmResult<String> saveRecord(PurchaseBillDirect_Mdl mrTempCurrent, PurchaseBillDirect_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove, boolean updateItemPurchaseRate) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String hisoryId = "";
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "",
                roundOffItemGstNature = "", invoiceCGST = "", invoiceSGST = "", invoiceIGST = "",
                invoiceUGST = "", invoiceValue = "", labDocId = "", roundOffGstTypeCode = "";
        ResultSet rsTemp = null;
        GstTrans_Mdl gstTrans_Mdl = null;
        dmrTemp.setData("");
        int sno = 0;

        TdsCategoryPopup_Mdl tdsCatMdl;
        try {
            PurchaseBillDirect_Vld.chkSave(mrTempCurrent);
            ServiceGrid_Vld.chkSave(mrTempCurrent.getGrdServiceDetail());
            // OverHead_Shared.chkGrid(mrTempCurrent.getOverHeadList());
            //OverHeadLine_Shared.chkGrid(mrTempCurrent.getGrdGrnDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            DmServer.checkForwordVoucherDateTime(mrTempCurrent.getVoucherDate(), "");
            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
            }
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTempCurrent.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTempCurrent.setObjRecId(DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTempCurrent.setSharedKeyFieldName(DmShared.xNull(mrTempCurrent.getObjRecId().getRecIdValue()));
                mrTempCurrent.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTempCurrent), DmServer.getSsnUserName(this), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), mrTempCurrent.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTempCurrent.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Check for Approvedby if already approved >>====================================*/
            sqlQuery = "select approvedBy from pBill where  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't Edit this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="Check gate">
            /*==============================<< Check gate in pBill >>============================*/
            sqlQuery = "SELECT Count(*) cnt "
                    + "		FROM pBill pb "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                    + "		WHERE pb.gateDocId IS NOT NULL "
                    + "		AND vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + "		AND pb.docId <> :docId "
                    + "		AND pb.gateDocId = :gateDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.setString("gateDocId", DmShared.xNull(mrTempCurrent.getGateNoTag()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Gate has been used in Purchase Bill Direct.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check gate in pChallan >>============================*/
            sqlQuery = "SELECT Count(*) cnt "
                    + "		FROM pChallan pc "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pc.v_Type "
                    + "		WHERE pc.gateDocId IS NOT NULL "
                    + "		AND vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + "		AND pc.gateDocId = :gateDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("gateDocId", DmShared.xNull(mrTempCurrent.getGateNoTag()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Gate has been used in GRN.");
                }
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>

            /*==============================<< Check for LatestEntry >>====================================*/
            sqlQuery = "SELECT u_EntDt_LatestLine FROM pBill WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(mrTempCurrent.getU_EntDt_LatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
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
                    + " WHERE pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode "
                    + " AND pb.v_Prefix = :v_Prefix "
                    + " AND pb.docId <> :docId " ;
//                    + " AND vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(400, "Entry already exists with this Bill Number.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for PO item change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM pOrderDetail pod " + DmShared.xNull(mrTempCurrent.getPoItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Order has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check for Round Off Item in Enviro >>====================================*/
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                sqlQuery = "SELECT im.code, im.hsnCode, im.itemGstNature, iid.gstTypeCode\n"
                        + "FROM itemMast im\n"
                        + "LEFT JOIN itemItsDetail iid ON iid.code = im.code\n"
                        + "WHERE im.uniqueId = 'RO'";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    roundOffItemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                    roundOffItemHsnCode = DmShared.xNull(rsTemp.getString("hsnCode"));
                    roundOffItemGstNature = DmShared.xNull(rsTemp.getString("itemGSTNature"));
                    roundOffGstTypeCode = DmShared.xNull(rsTemp.getString("gstTypeCode"));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.xNull(roundOffItemCode).trim().equals("")) {
                    throw new DmException(0, "Round Off Item not defined in Overhead Master.");
                }
            }
            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert / Update record into pBill >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pBill ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, partyBillNo, partyBillDate, \n"
                        + "     calcOn, subCode, consignee, partyType, gsTin, gstinDate, rcmApplicable, placeOfSupply, \n"
                        + "     remark, wtaCode, guiConfigCode, site_Code, preparedBy, preparedDate, u_EntDt_LatestLine, transferDt,\n"
                        + "     gateDocId, transporterCode, EWayBillNo, EWayBillDt, LRNo, LRDate , \n"
                        + "     oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                        + "     oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                        + "     oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Total, \n"
                        + "     oh_Amt_RoundOff, oh_Amt_Net, oh_Amt_LandedValue, locationCode, \n"
                        + "     oh_Ac_FinalDiscount, oh_Ac_RoundOff,PaymentDueDate,balancingAc, tdsAmt)\n"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, :partyBillNo, :partyBillDate,\n "
                        + "     :calcOn, :subCode, :consignee, :partyType, :gsTin, :gstinDate, :rcmApplicable, :placeOfSupply, \n"
                        + "     :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :preparedDate, :uEntDt, :transferDt,\n"
                        + "     :gateDocId, :transporterCode, :EWayBillNo, :EWayBillDt, :LRNo, :LRDate, \n"
                        + "     :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                        + "     :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                        + "     :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Total, \n"
                        + "     :oh_Amt_RoundOff, :oh_Amt_Net, :oh_Amt_LandedValue, :locationCode, \n"
                        + "     :oh_Ac_FinalDiscount, :oh_Ac_RoundOff,:PaymentDueDate, :balancingAc, :tdsAmt) \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTempCurrent.getObjRecId().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTempCurrent.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTempCurrent.getObjRecId().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getSupplierBillDate()));
                psTemp.setString("calcOn", DmShared.iNull(mrTempCurrent.getCalculateOnTag()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTempCurrent.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("LRNo", DmShared.xNull(mrTempCurrent.getLrNo()));
                psTemp.setDate("LRDate", DmServer.convertDate(mrTempCurrent.getLrDate()));
                psTemp.setString("gateDocId", DmShared.foriegnKey(mrTempCurrent.getGateNoTag()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTempCurrent.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTempCurrent.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(mrTempCurrent.getGoodsGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_GrossService", DmServer.vNull(mrTempCurrent.getServiceGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTempCurrent.getGrossAmount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTempCurrent.getDiscount(), "0.00"));
                psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(mrTempCurrent.getGoodsTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(mrTempCurrent.getServiceTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTempCurrent.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTempCurrent.getSgst(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTempCurrent.getUgst(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTempCurrent.getCgst(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTempCurrent.getIgst(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTempCurrent.getSubTotal(), "0.00"));
                psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(mrTempCurrent.getFinalDiscountPer(), "0.00"));
                psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(mrTempCurrent.getFinalDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTempCurrent.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTempCurrent.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTempCurrent.getLandedValue(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTempCurrent.getLocationCode()));
                psTemp.setString("oh_Ac_FinalDiscount", DmShared.foriegnKey(mrTempCurrent.getFinalDiscountAcCode()));
                psTemp.setString("oh_Ac_RoundOff", DmShared.foriegnKey(mrTempCurrent.getRoundOffAcCode()));
                psTemp.setDate("PaymentDueDate", DmServer.convertDate(mrTempCurrent.getPaymentDueDate()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTempCurrent.getBalancingAc()));
                psTemp.setString("tdsAmt", DmServer.vNull(mrTempCurrent.getTdsAmt(), "0.00"));
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
                        + " ,EWayBillNo = :EWayBillNo "
                        + " ,EWayBillDt = :EWayBillDt "
                        + " ,LRNo = :LRNo "
                        + " ,LRDate = :LRDate "
                        + " ,gateDocId = :gateDocId "
                        + " ,transporterCode = :transporterCode "
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
                        + " , PaymentDueDate = :PaymentDueDate \n"
                        + " , balancingAc = :balancingAc \n"
                        + "  , tdsAmt = :tdsAmt \n"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getSupplierBillDate()));
                psTemp.setString("calcOn", DmShared.iNull(mrTempCurrent.getCalculateOnTag()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTempCurrent.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("LRNo", DmShared.xNull(mrTempCurrent.getLrNo()));
                psTemp.setDate("LRDate", DmServer.convertDate(mrTempCurrent.getLrDate()));
                psTemp.setString("gateDocId", DmShared.foriegnKey(mrTempCurrent.getGateNoTag()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
                psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(mrTempCurrent.getGoodsGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_GrossService", DmServer.vNull(mrTempCurrent.getServiceGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTempCurrent.getGrossAmount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTempCurrent.getDiscount(), "0.00"));
                psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(mrTempCurrent.getGoodsTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(mrTempCurrent.getServiceTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTempCurrent.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTempCurrent.getSgst(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTempCurrent.getUgst(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTempCurrent.getCgst(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTempCurrent.getIgst(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTempCurrent.getSubTotal(), "0.00"));
                psTemp.setString("oh_at_FinalDiscount", DmServer.vNull(mrTempCurrent.getFinalDiscountPer(), "0.00"));
                psTemp.setString("oh_Amt_FinalDiscount", DmServer.vNull(mrTempCurrent.getFinalDiscount(), "0.00"));
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTempCurrent.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTempCurrent.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTempCurrent.getLandedValue(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTempCurrent.getLocationCode()));
                psTemp.setString("oh_Ac_FinalDiscount", DmShared.foriegnKey(mrTempCurrent.getFinalDiscountAcCode()));
                psTemp.setString("oh_Ac_RoundOff", DmShared.foriegnKey(mrTempCurrent.getRoundOffAcCode()));
                psTemp.setDate("PaymentDueDate", DmServer.convertDate(mrTempCurrent.getPaymentDueDate()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTempCurrent.getBalancingAc()));
                psTemp.setString("tdsAmt", DmServer.vNull(mrTempCurrent.getTdsAmt(), "0.00"));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //====================================Re-check-PartyBillNo=================================
            /*==============================<< Check for duplicate party bill no >>====================================*/
            sqlQuery = " SELECT COUNT(pb.docid) AS cnt "
                    + " FROM pBill pb  with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pb.v_Type "
                    + " WHERE pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode "
                    + " AND pb.v_Prefix = :v_Prefix "
                    + " AND pb.docId <> :docId "
                    + " AND vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(400, "Entry already exists with this Bill Number.");
                }
            }
            rsTemp.close();
            psTemp.close();


            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), true, cnTemp);

            /*==============================<< Delete records from pBillImpurityDetail >>====================================*/
            sqlQuery = "DELETE pBillImpurityDetail"
                    + " FROM pBillImpurityDetail AS pid"
                    + " INNER JOIN pBillDetail pbd ON pid.docId = pbd.docId AND pid.v_Sno = pbd.v_Sno"
                    + " WHERE pbd.docId = :docId AND pbd.isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Get itemUpdateHistory ID >>====================================*/
            hisoryId = DmServer.getCodeViaManualCode(mrTempCurrent.getSharedKeyFieldCode(), "docId", "itemUpdateHistory", "(ISNULL(MAX(id),0) + 1)", cnTemp);

            /*==============================<< Delete records from pBillDetail >>====================================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pBillDetail", "ibCode", " AND docid = '" + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "'", cnTemp);

            sqlQuery = "Delete From pBillDetail Where docId = :docId AND isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            // update null in lab, before delete pBillDetail
            manageLabData(mrTempCurrent.getSharedKeyFieldCode(), true, cnTemp);

            /*==============================<< Insert records into pBillDetail >>====================================*/
            List<GrdItemDetail_Mdl> listDetail = mrTempCurrent.getGrdItemDetail();
            int vSNo = 0;
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    if (!detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                            detail_Mdl.setvSNo(String.valueOf(vSNo));
                            sqlQuery = "INSERT INTO pBillDetail ("
                                    + "     docId, v_SNo, poDocId, poV_SNo, itemCode, departmentCode, hsnCode, itemGSTNature, batchNo, lotNo, manufacturingDate, expiryDate, colourCode,\n"
                                    + "     sizeCode,itemMakeCode, mrp, skuMrp, unitCode, docQty, recQty, rejQty, acceptQty, skuDifference, skuQty, rate,\n"
                                    + "     landedSkuValue, landedSkuRate, skuQtyForRate, skuRate, remark, labRejQty, qcRejQty, drCrNoteAmt, unloadingDocId, unloadingV_Sno, shortageQty\n"
                                    + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                    + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                    + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Net\n"
                                    + "     , oh_Ac_SGST, oh_Ac_UGST, oh_Ac_CGST, oh_Ac_IGST\n"
                                    + "     , oh_Ac_SGST_RCM, oh_Ac_UGST_RCM, oh_Ac_CGST_RCM, oh_Ac_IGST_RCM\n"
                                    + "     , gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                    + " Values ("
                                    + "     :docId, :v_SNo, :poDocId, :poV_SNo, :itemCode, :departmentCode, :hsnCode, :itemGSTNature, :batchNo, :lotNo, :manufacturingDate, :expiryDate, :colourCode,\n"
                                    + "     :sizeCode, :itemMakeCode, :mrp, :skuMrp, :unitCode, :docQty, :recQty, :rejQty, :acceptQty, :skuDifference, :skuQty, :rate,\n"
                                    + "     :landedSkuValue, :landedSkuRate, :skuQtyForRate, :skuRate, :remark, :labRejQty, :qcRejQty, :drCrNoteAmt, :unloadingDocId, :unloadingV_Sno, :shortageQty\n"
                                    + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                    + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                    + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Net\n"
                                    + "     , :oh_Ac_SGST, :oh_Ac_UGST, :oh_Ac_CGST, :oh_Ac_IGST\n"
                                    + "     , :oh_Ac_SGST_RCM, :oh_Ac_UGST_RCM, :oh_Ac_CGST_RCM, :oh_Ac_IGST_RCM\n"
                                    + "     , :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                            psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).toString());
                            psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                            psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("departmentCode", DmShared.foriegnKey(detail_Mdl.getDepartmentCode()));
                            psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                            psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                            psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                            psTemp.setString("lotNo", DmShared.xNull(detail_Mdl.getLotNo()));
                            psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                            psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                            psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColourCode()));
                            psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(detail_Mdl.getItemMakeCode()));
                            psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                            psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                            psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                            psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getChallanQty()));
                            psTemp.setString("recQty", DmServer.vNull(detail_Mdl.getRecQty()));

                            psTemp.setString("shortageQty", DmServer.vNull(detail_Mdl.getShortageQty()));

                            psTemp.setString("labRejQty", DmServer.vNull(detail_Mdl.getLabRejectedQty()));
                            psTemp.setString("qcRejQty", DmServer.vNull(detail_Mdl.getQcRejectedQty()));
                            psTemp.setString("rejQty", DmServer.vNull(detail_Mdl.getRejQty()));
                            psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                            psTemp.setString("skuDifference", DmServer.vNull(detail_Mdl.getSkuDifferenceQty()));
                            psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                            psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                            psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
                            psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                            psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                            psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                            psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                            psTemp.setString("drCrNoteAmt", DmServer.vNull(detail_Mdl.getDrCrNoteAmt(), "0.00"));
                            psTemp.setString("unloadingDocId", DmShared.foriegnKey(detail_Mdl.getUnloadingDocId()));
                            psTemp.setString("unloadingV_Sno", DmShared.foriegnKey(detail_Mdl.getUnloadingV_Sno()));
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

                            /*==============================<< Insert records into pBillImpurityDetail >>====================================*/
                            sno = 0;
                            for (GrdImpurity_Mdl grdIm : detail_Mdl.getLstImpurity()) {
                                if (DmShared.iNull(grdIm.getSharedKeyFieldCode()).longValue() != 0) {
                                    sqlQuery = "INSERT INTO pBillImpurityDetail ("
                                            + "     docId, v_SNo, sno, impurityCode, foundValue, acceptPer, netPer, netQty, supplierQty, nature)"
                                            + " VALUES ("
                                            + "     :docId, :v_SNo, :sno, :impurityCode, :foundValue, :acceptPer, :netPer, :netQty, :supplierQty, :nature)";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                                    psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                                    psTemp.setString("sno", String.valueOf(++sno));
                                    psTemp.setString("impurityCode", DmShared.foriegnKey(grdIm.getSharedKeyFieldCode()));
                                    psTemp.setString("foundValue", DmShared.vNull(grdIm.getFoundPer()).toString());
                                    psTemp.setString("acceptPer", DmShared.vNull(grdIm.getAcceptPer()).toString());
                                    psTemp.setString("netPer", DmShared.vNull(grdIm.getNetPer()).toString());
                                    psTemp.setString("netQty", DmServer.vNull(grdIm.getNetQty()));
                                    psTemp.setString("supplierQty", DmServer.vNull(grdIm.getSupplierQty()));
                                    psTemp.setString("nature", DmShared.iNull(ImpurityNature.QC.getId()).toString());
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }

                            // for lab details.
                            for (GrdLabEntry_Mdl grdLab : detail_Mdl.getLstLabEntry()) {
                                if (DmShared.iNull(grdLab.getSharedKeyFieldCode()).longValue() != 0) {
                                    sqlQuery = "INSERT INTO pBillImpurityDetail ( "
                                            + " docId, v_SNo, sno, impurityCode, range1, range2, "
                                            + " foundValue, netQty, amount, nature ) "
                                            + " VALUES ( "
                                            + " :docId, :v_SNo, :sno, :impurityCode, :range1, :range2, "
                                            + " :foundValue, :netQty, :amount, :nature ) ";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                                    psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                                    psTemp.setString("sno", String.valueOf(++sno));
                                    psTemp.setString("impurityCode", DmShared.foriegnKey(grdLab.getSharedKeyFieldCode()));
                                    psTemp.setString("range1", DmShared.foriegnKey(grdLab.getRange1()));
                                    psTemp.setString("range2", DmShared.foriegnKey(grdLab.getRange2()));
                                    psTemp.setString("foundValue", DmShared.foriegnKey(grdLab.getMainFoundValue()));
                                    psTemp.setString("netQty", DmShared.foriegnKey(grdLab.getLabQty()));
                                    psTemp.setString("amount", DmShared.vNull(grdLab.getLabAmt()).toString());
                                    psTemp.setString("nature", DmShared.iNull(ImpurityNature.LAB.getId()).toString());
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                            /*==========================<< **** >>===========================*/
                        }
                    }
                }
            }

            // update values in lab, after delete pBillDetail
            manageLabData(mrTempCurrent.getSharedKeyFieldCode(), false, cnTemp);

            //========================================(Service Grid Modal)==========================================================
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                    if (detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pBillDetail ("
                                + "     docId, v_SNo, itemCode, hsnCode, docQty, acceptQty, rate, itemGSTNature ,unitCode ,isFooter\n"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net\n"
                                + "     , oh_Ac_SGST, oh_Ac_UGST, oh_Ac_CGST, oh_Ac_IGST\n"
                                + "     , oh_Ac_SGST_RCM, oh_Ac_UGST_RCM, oh_Ac_CGST_RCM, oh_Ac_IGST_RCM\n"
                                + "     , serviceEvalTypeCode, serviceEvalRate, gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                + " Values ("
                                + "     :docId, :v_SNo, :itemCode, :hsnCode, :docQty, :acceptQty, :rate, :itemGSTNature ,:unitCode ,  2 \n"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :oh_Ac_SGST, :oh_Ac_UGST, :oh_Ac_CGST, :oh_Ac_IGST\n"
                                + "     , :oh_Ac_SGST_RCM, :oh_Ac_UGST_RCM, :oh_Ac_CGST_RCM, :oh_Ac_IGST_RCM\n"
                                + "     , :serviceEvalTypeCode, :serviceEvalRate, :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));

                        psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getChallanQty()));
                        psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
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
                    /*==============================<< Update Item Master >>====================================*/
                    if (DmShared.iNull(detail_Mdl.getLstItemUpdateFields().size()).intValue() > 0) {
                        ItemUpdate_Mdl iuMdl = new ItemUpdate_Mdl();
                        iuMdl.setId(hisoryId);
                        iuMdl.setDocId(mrTempCurrent.getSharedKeyFieldCode());
                        iuMdl.setV_SNo(detail_Mdl.getvSNo());
                        iuMdl.setV_Type(mrTempCurrent.getVoucherTypeCode());
                        iuMdl.setV_No(DmShared.iNull(mrTempCurrent.getObjRecId().getCounter()).toString());
                        iuMdl.setV_Date(mrTempCurrent.getVoucherDate());
                        iuMdl.setRecId(mrTempCurrent.getSharedKeyFieldName());
                        iuMdl.setSubcode(mrTempCurrent.getSupplierTag());
                        iuMdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                        iuMdl.setUnitCode(detail_Mdl.getPurchaseUnitCode());
                        iuMdl.setLstItemUpdateFields(detail_Mdl.getLstItemUpdateFields());
                        ItemUpdateServer.updateItem(iuMdl, cnTemp, this);
                    }
                }
            }

            /*==============================<< Insert records into stock >>====================================*/
            ItemBatch_Server.compareProperties(iniItemBatchProperties(mrTempCurrent, mrTempPrevious, false), cnTemp, this);

            /*==============================<< Update ibCode From Stock Table >>====================================*/
            sqlQuery = "UPDATE pBillDetail"
                    + " SET pBillDetail.ibCode = stock.ibCode"
                    + " FROM stock"
                    + " WHERE stock.docid = pBillDetail.docId"
                    + " AND stock.v_SNo = pBillDetail.v_SNo"
                    + " AND pBillDetail.docid = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Update isoh_amt_netzero in pbill Table >>====================================*/
            if (DmShared.vNull(mrTempCurrent.getNetAmount()) == 0) {
                sqlQuery = "UPDATE pBill"
                        + " SET isoh_amt_netzero = 1"
                        + " where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), false, cnTemp);

            /*==============================<< Insert records into OH >>====================================*/
            //OverHeadLine_Server.save(listDetail, "docId", mrTempCurrent.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            //OverHead_Server.save(mrTempCurrent.getOverHeadList(), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", DmShared.xNull(mrTempCurrent.getSupplierBillNo()), DmShared.dNull(mrTempCurrent.getSupplierBillDate()), "", "", false, true, cnTemp);

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
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
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
                if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) != 0) {
                    sqlQuery = "INSERT INTO pBillDetail ("
                            + "     docId, v_SNo, itemCode, hsnCode, docQty, acceptQty, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter)"
                            + " Values ("
                            + "     :docId, :v_SNo, :itemCode, :hsnCode, :docQty, :acceptQty, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, 1)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", String.valueOf(++vSNo));
                    psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                    if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) < 0) {
                        psTemp.setString("docQty", "-1");
                        psTemp.setString("acceptQty", "-1");
                    } else {
                        psTemp.setString("docQty", "1");
                        psTemp.setString("acceptQty", "1");
                    }
                    psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTempCurrent.getRoundOffAmount())), "0.00"));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*==============================<< Delete records into gstTrans >>====================================*/
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode())), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Insert records into gstTrans >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    //<editor-fold defaultstate="collapsed" desc="Set gstTrans_Mdl">
                    gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(DmShared.xNull(detail_Mdl.getvSNo()));
                    gstTrans_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getObjRecId().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTempCurrent.getObjRecId().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                    gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                    gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTempCurrent.getSupplierBillDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTempCurrent.getSupplierTag()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTempCurrent.getSupplierTag()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                    gstTrans_Mdl.setGstinDate(DmShared.dNull(mrTempCurrent.getSupplierGstinDate()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTempCurrent.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(detail_Mdl.getHsnCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    gstTrans_Mdl.setDepartmentCode(DmShared.xNull(detail_Mdl.getDepartmentCode()));
                    gstTrans_Mdl.setQty(DmShared.iNull(mrTempCurrent.getCalculateOnTag()).intValue() == 1 ? DmShared.xNull(detail_Mdl.getAcceptQty()) : DmShared.xNull(detail_Mdl.getChallanQty()));
                    gstTrans_Mdl.setGstUnitCode(DmShared.xNull(detail_Mdl.getGstUnitCode()));
                    gstTrans_Mdl.setInvoiceCGST(DmShared.xNull(invoiceCGST));
                    gstTrans_Mdl.setInvoiceSGST(DmShared.xNull(invoiceSGST));
                    gstTrans_Mdl.setInvoiceIGST(DmShared.xNull(invoiceIGST));
                    gstTrans_Mdl.setInvoiceUGST(DmShared.xNull(invoiceUGST));
                    gstTrans_Mdl.setInvoiceValue(DmShared.xNull(invoiceValue));
                    gstTrans_Mdl.setTransporterCode(DmShared.xNull(mrTempCurrent.getTransporterCode()));
                    gstTrans_Mdl.setLrNo(DmShared.xNull(mrTempCurrent.getLrNo()));
                    gstTrans_Mdl.setLrDate(DmShared.dNull(mrTempCurrent.getLrDate()));
                    gstTrans_Mdl.setEwayBillNo(DmShared.xNull(mrTempCurrent.getEwayNo()));
                    gstTrans_Mdl.setEwayBillDt(DmShared.dNull(mrTempCurrent.getEwayDate()));
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
                            + " FROM pBillDetail pbd"
                            + " WHERE pbd.docId = :docId"
                            + " AND pbd.v_Sno = :v_Sno";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
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
                    //</editor-fold>
                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
                }
            }
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) != 0) {
                    //<editor-fold defaultstate="collapsed" desc="Insert RoundOff line in gstTrans">
                    gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(String.valueOf(vSNo));
                    gstTrans_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getObjRecId().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTempCurrent.getObjRecId().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTempCurrent.getVoucherDate()));
                    gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                    gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTempCurrent.getSupplierBillDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTempCurrent.getSupplierTag()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTempCurrent.getSupplierTag()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                    gstTrans_Mdl.setGstinDate(DmShared.dNull(mrTempCurrent.getSupplierGstinDate()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTempCurrent.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.foriegnKey(roundOffItemCode));
                    gstTrans_Mdl.setHsnCode(DmShared.foriegnKey(roundOffItemHsnCode));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(roundOffItemGstNature));
                    gstTrans_Mdl.setDepartmentCode("");
                    if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) < 0) {
                        gstTrans_Mdl.setQty("-1");
                    } else {
                        gstTrans_Mdl.setQty("1");
                    }
                    gstTrans_Mdl.setItemGrossAmount(DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    gstTrans_Mdl.setItemTaxableAmount(DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    gstTrans_Mdl.setItemNetAmount(DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    gstTrans_Mdl.setInvoiceCGST(DmShared.xNull(invoiceCGST));
                    gstTrans_Mdl.setInvoiceSGST(DmShared.xNull(invoiceSGST));
                    gstTrans_Mdl.setInvoiceIGST(DmShared.xNull(invoiceIGST));
                    gstTrans_Mdl.setInvoiceUGST(DmShared.xNull(invoiceUGST));
                    gstTrans_Mdl.setInvoiceValue(DmShared.xNull(invoiceValue));
                    gstTrans_Mdl.setTransporterCode(DmShared.xNull(mrTempCurrent.getTransporterCode()));
                    gstTrans_Mdl.setLrNo(DmShared.xNull(mrTempCurrent.getLrNo()));
                    gstTrans_Mdl.setLrDate(DmShared.dNull(mrTempCurrent.getLrDate()));
                    gstTrans_Mdl.setEwayBillNo(DmShared.xNull(mrTempCurrent.getEwayNo()));
                    gstTrans_Mdl.setEwayBillDt(DmShared.dNull(mrTempCurrent.getEwayDate()));
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

            AccountPosting_Server.deleteAccountPosting(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
                /*==============================<< Account Posting Inventory to Balancing Ac>>====================================*/
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive(), mrTempCurrent.getDefaultValueMdl().isIncludeOverHeadsInCost(), true, cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", mrTempCurrent.getSupplierBillNo(), mrTempCurrent.getSupplierBillDate(), "", "", false, true, cnTemp);
                /*==============================<< Account Posting Balancing Ac to Party on Approval click>>====================================*/
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive(), mrTempCurrent.getDefaultValueMdl().isIncludeOverHeadsInCost(), false, cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", mrTempCurrent.getSupplierBillNo(), mrTempCurrent.getSupplierBillDate(), "", "", false, false, cnTemp);
            } else {
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive(), cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", mrTempCurrent.getSupplierBillNo(), mrTempCurrent.getSupplierBillDate(), "", "", false, false, cnTemp);
            }


            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                STApprovals.purchaseBillApproval(true, WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (mrTempCurrent.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTempCurrent.getModifyReasonMdl(), DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), userName, cnTemp);
            }

            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTempCurrent.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString(), siteCode, cnTemp);

            //Delete Unused Item Batches
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                deleteUnusedItemBatches(ibCodeCsv, "", cnTemp);
            }
            //===================================================== << TDS SAVE >>========================================================
            if (mrTempCurrent.getTdsCatMdl() != null) {
                mrTempCurrent.getTdsCatMdl().setSharedKeyFieldCode(mrTempCurrent.getSharedKeyFieldCode());
                mrTempCurrent.getTdsCatMdl().setRecId(mrTempCurrent.getObjRecId());
                mrTempCurrent.getTdsCatMdl().setVoucherDate(mrTempCurrent.getVoucherDate());
                mrTempCurrent.getTdsCatMdl().setVoucherPrefix(voucherPrefix);
                mrTempCurrent.getTdsCatMdl().setVoucherTypeCode(mrTempCurrent.getVoucherTypeCode());
                TdsCategoryPopup_SrvImpl.saveTDS(mrTempCurrent.getTdsCatMdl(), siteCode, cnTemp);

            }

            dmrTemp.setData(mrTempCurrent.getSharedKeyFieldCode());

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getVoucherTypeCode(), this);
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
    public DmResult<PurchaseBillDirect_Mdl> getMoveRec(String strKey) {
        DmResult<PurchaseBillDirect_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery, labDocId = "";
        PurchaseBillDirect_Mdl mrTemp = new PurchaseBillDirect_Mdl();
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;
        GrdImpurity_Mdl impurityNode;
        GrdLabEntry_Mdl labNode;
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
            sqlQuery = "SELECT pb.docid,pb.recId,pb.v_No, pb.recIdPrefix ,pb.v_Type,pb.v_Date, pb.partyBillNo \n"
                    + " , pb.partyBillDate,pb.subcode,pb.partyType, pb.rcmApplicable ,pb.placeOfSupply, pb.remark ,pb.preparedBy,pb.u_Name \n"
                    + " , pb.approvedBy,pb.wtaCode, pb.guiConfigCode ,pb.calcOn,pb.paymentDueDate, pb.oh_Amt_Gross,pb.u_EntDt_LatestLine \n"
                    + " , pb.preparedDate,pb.approvalDateTime, pb.u_EntDt, vt.description AS voucherTypeName, vt.category \n"
                    + " , sgs.name AS supplierName, c.stateCode AS supplierStateCode, sgs.partyType AS supplierType \n"
                    + " , pb.gstin AS supplierGstin, pb.gstinDate AS supplierGstinDate, pb.gateDocId, gio.recId as gateRecId \n"
                    + " , pb.transporterCode, sg1.name as transporterName, pb.EWayBillNo, pb.EWayBillDt, pb.LRNo \n"
                    + " , pb.LRDate, pb.oh_Amt_GrossGoods, pb.oh_Amt_GrossService, pb.oh_Amt_Gross, pb.oh_Amt_Discount \n"
                    + " , pb.oh_Amt_TaxableGoods, pb.oh_Amt_TaxableService, pb.oh_Amt_Taxable, pb.oh_Amt_SGST, pb.oh_Amt_UGST \n"
                    + " , pb.oh_Amt_CGST, pb.oh_Amt_IGST, pb.oh_Amt_SubTotal, pb.oh_at_FinalDiscount, pb.oh_Amt_FinalDiscount \n"
                    + " , pb.oh_Amt_Total, pb.oh_Amt_RoundOff, pb.oh_Amt_Net, pb.oh_Amt_LandedValue, pb.locationCode \n"
                    + " , pb.oh_Ac_FinalDiscount, pb.oh_Ac_RoundOff,pb.balancingAc \n"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode"
                    + " LEFT JOIN subGroup sg1 ON sg1.subCode = pb.transporterCode "
                    + " LEFT JOIN city c ON c.cityCode = sgs.cityCode"
                    + " LEFT JOIN gateInOut gio ON gio.docId = pb.gateDocId "
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
                mrTemp.setCalculateOnText(DmServer.vNull(rsTemp.getString("calcOn"), "0").equalsIgnoreCase("1") ? "Accept Qty." : "Doc. Qty.");
                mrTemp.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
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

                mrTemp.setGateNoTag(DmShared.xNull(rsTemp.getString("gateDocId")));
                mrTemp.setGateNoText(DmShared.xNull(rsTemp.getString("gateRecId")));
                mrTemp.setTransporterCode(DmShared.xNull(rsTemp.getString("transporterCode")));
                mrTemp.setTransporterName(DmShared.xNull(rsTemp.getString("transporterName")));
                mrTemp.setEwayNo(DmShared.xNull(rsTemp.getString("EWayBillNo")));
                mrTemp.setEwayDate(DmShared.dNull(rsTemp.getDate("EWayBillDt")));
                mrTemp.setLrNo(DmShared.xNull(rsTemp.getString("LRNo")));
                mrTemp.setLrDate(DmShared.dNull(rsTemp.getDate("LRDate")));

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }

                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   pbd.itemCode,pbd.hsnCode,pbd.skuQty,pbd.skuDifference"
                    + " , pbd.acceptQty,pbd.batchNo,pbd.lotNo,pbd.colourCode,pbd.sizeCode"
                    + " , pbd.itemMakeCode,pbd.mrp,pbd.skuMrp "
                    + " , pbd.docQty,pbd. recQty,pbd.rejQty "
                    + " , pbd.shortageQty "
                    + " , pbd.rate,pbd.landedSkuValue,pbd.landedSkuRate"
                    + " , pbd.skuRate,pbd.oh_Amt_Gross,pbd.remark"
                    + " , pbd.v_SNo,pbd.isStatus,pbd.ibCode ,pbd.unitCode"
                    + " , pbd.labRejQty "
                    + " , pbd.qcRejQty "
                    + " , pbd.drCrNoteAmt "
                    + " , pbd.unloadingDocId "
                    + " , pbd.unloadingV_Sno "
                    + " , pbd.departmentCode"
                    + " , sgd.name AS departmentName"
                    + " , po.recId AS poRecId,pbd.expiryDate,pbd.manufacturingDate"
                    + " , po.refNo AS poRefNo,pbd.poDocId ,pbd.poV_SNo,pbd.returnedQty"
                    + " , im.name AS itemName"
                    + " , im.propertyList"
                    + " , pbd.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.isLabRequired"
                    + " , im.sku AS skuCode, im.purchaseRate "
                    + " , ums.unit AS skuName"
                    + " , ums.decimalPlaces AS skuAllowDecimalPlaces"
                    + " , um.unit AS unitName"
                    + " , um.decimalPlaces AS unitAllowDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , icm.name AS colourName"
                    + " , ism.name AS sizeName"
                    + " ,imm.CompanyName as itemMakeName"
                    + " , DATEDIFF(day, pbd.manufacturingDate, pbd.expiryDate) AS bestBeforeDays"
                    + " , ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) + ISNULL(pbd.acceptQty, 0) AS pendingQty"
                    + " , lab.docId as labDocId "
                    + " , hsn.hsn "
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
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code"
                    + " LEFT JOIN itemColourMast icm ON pbd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pbd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast as imm ON pbd.itemMakeCode = imm.code "
                    + " LEFT JOIN lab ON lab.usedDocId = pbd.docId AND lab.usedV_Sno = pbd.v_SNo "
                    + " LEFT JOIN subGroup sgd ON pbd.departmentCode = sgd.subCode"
                    + " LEFT JOIN gstType it ON it.code = pbd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pbd.hsnCode \n"
                    + " WHERE pbd.docId = :docId"
                    + " AND ISNULL(pbd.isFooter, 0) = 0"
                    + " ORDER BY pbd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setServiceRecord(false);
                //Unloading
                node.setUnloadingDocId(DmShared.xNull(rsTemp.getString("unloadingDocId")));
                node.setUnloadingV_Sno(DmShared.xNull(rsTemp.getString("unloadingV_Sno")));
                labDocId = DmShared.xNull(rsTemp.getString("labDocId"));

                //Department
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuAllowDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("unitAllowDecimalPlaces")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double skuDifferenceQty = DmShared.vNull(rsTemp.getString("skuDifference"));
                double acceptQty = DmShared.vNull(rsTemp.getString("acceptQty"));
                double purchaseUnitConversionRate = ((skuQty - skuDifferenceQty) / acceptQty);
                node.setPurchaseUnitConversionRate(DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
                node.setPurchaseUnitConversionRate(DmServer.vNull(node.getPurchaseUnitConversionRate()));
                node.setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
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
                node.setBestBeforeDays(DmShared.xNull(rsTemp.getString("bestBeforeDays")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setBatchNo(node.getBatchNo());
                ipm.setLotNo(node.getLotNo());
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
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));
                if (DmShared.iNull(rsTemp.getString("poDocId")).longValue() != 0) {
                    if (!mrTemp.getIsPurchaseOrderExists()) {
                        mrTemp.setIsPurchaseOrderExists(true);
                    }
                }

                //Quantities
                node.setChallanQty(DmServer.vNull(rsTemp.getString("docQty")));
                node.setRecQty(DmServer.vNull(rsTemp.getString("recQty")));
                node.setShortageQty(DmServer.vNull(rsTemp.getString("shortageQty")));

                node.setLabRejectedQty(DmServer.vNull(rsTemp.getString("labRejQty")));
                node.setQcRejectedQty(DmServer.vNull(rsTemp.getString("qcRejQty")));
                node.setRejQty(DmServer.vNull(rsTemp.getString("rejQty")));
                node.setSkuDifferenceQty(DmServer.vNull(rsTemp.getString("skuDifference")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setReturnedQty(DmServer.vNull(rsTemp.getString("returnedQty")));

                //Rate
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setDrCrNoteAmt(DmServer.vNull(rsTemp.getString("drCrNoteAmt"), "0.00"));

                //Other
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

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

                /*==============================<< Get Impurity detail >>========================*/
                sqlQuery = " SELECT im.name AS impurityName, pbid.impurityCode, pbid.foundValue, pbid.acceptPer, "
                        + " pbid.netPer, pbid.netQty, pbid.supplierQty "
                        + " FROM pBillImpurityDetail pbid "
                        + " LEFT JOIN impurityMast im ON im.code = pbid.impurityCode "
                        + " WHERE pbid.docId =:docId "
                        + " AND pbid.v_SNo =:v_SNo "
                        + " AND pbid.nature = :nature "
                        + " UNION ALL "
                        + " select im.name AS impurityName, iid.impurityCode, null as foundValue, null as acceptPer, "
                        + " null as netPer, null as netQty, null as supplierQty "
                        + " from itemImpurityDetail iid "
                        + " LEFT JOIN itemImpurityMast iim ON iim.code = iid.code "
                        + " LEFT JOIN impurityMast im ON im.code = iid.impurityCode "
                        + " LEFT JOIN ( "
                        + "			select pbid.impurityCode "
                        + "			from pBillImpurityDetail pbid "
                        + "			where pbid.docId =:docId AND pbid.v_SNo =:v_SNo "
                        + "                     AND pbid.nature = :nature "
                        + "		  ) as vPcid ON vPcid.impurityCode = iid.impurityCode "
                        + " where vPcid.impurityCode IS NULL "
                        + " AND iim.testTemplateCode IS NULL "
                        + " AND iim.itemCode =:itemCode ";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("docId", DmShared.iNull(strKey).toString());
                psTempInner.setString("v_SNo", DmShared.iNull(node.getvSNo()).toString());
                psTempInner.setString("itemCode", DmShared.xNull(node.getSharedKeyFieldCode()));
                psTempInner.setString("nature", DmShared.iNull(ImpurityNature.QC.getId()).toString());
                rsTempInner = psTempInner.executeQuery();
                while (rsTempInner.next()) {
                    impurityNode = new GrdImpurity_Mdl();
                    impurityNode.setSharedKeyFieldCode(DmShared.xNull(rsTempInner.getString("impurityCode")));
                    impurityNode.setSharedKeyFieldName(DmShared.xNull(rsTempInner.getString("impurityName")));
                    impurityNode.setFoundPer(DmShared.xNull(rsTempInner.getString("foundValue")).toString());
                    impurityNode.setAcceptPer(DmShared.vNull(rsTempInner.getString("acceptPer")).toString());
                    impurityNode.setNetPer(DmShared.vNull(rsTempInner.getString("netPer")).toString());
                    impurityNode.setNetQty(DmServer.vNull(rsTempInner.getString("netQty")));
                    impurityNode.setSupplierQty(DmServer.vNull(rsTempInner.getString("supplierQty")));
                    node.getLstImpurity().add(impurityNode);
                }
                rsTempInner.close();
                psTempInner.close();

                /*==============================<< Get Lab detail >>===============================*/
                sqlQuery = " select pbd.impurityCode, im.name as impurityName, pbd.range1, pbd.range2 "
                        + " , pbd.foundValue, pbd.netQty, pbd.amount, pbd.nature, im.valueType, um.unit, im.manualCode "
                        + " , ld.formulaForQuantity, ld.formulaForAmount "
                        + " from pBillImpurityDetail pbd "
                        + " LEFT JOIN impurityMast im ON im.code = pbd.impurityCode "
                        + " LEFT JOIN unitMast um ON um.code = im.unitCode "
                        + " LEFT JOIN labDetail ld ON ld.impurityCode = pbd.impurityCode AND ld.docId = :labDocId "
                        + " WHERE pbd.docId = :docId "
                        + " AND pbd.v_SNo = :v_SNo "
                        + " AND pbd.nature = :nature "
                        + " ORDER BY pbd.sno ";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("labDocId", DmShared.xNull(labDocId));
                psTempInner.setString("docId", DmShared.xNull(strKey));
                psTempInner.setString("v_SNo", DmShared.xNull(node.getvSNo()));
                psTempInner.setString("nature", DmShared.iNull(ImpurityNature.LAB.getId()).toString());
                rsTempInner = psTempInner.executeQuery();
                while (rsTempInner.next()) {
                    labNode = new GrdLabEntry_Mdl();
                    labNode.setSharedKeyFieldCode(DmShared.xNull(rsTempInner.getString("impurityCode")));
                    labNode.setSharedKeyFieldName(DmShared.xNull(rsTempInner.getString("impurityName")));
                    labNode.setRange1(DmServer.vNull(rsTempInner.getString("range1")));
                    labNode.setRange2(DmServer.vNull(rsTempInner.getString("range2")));
                    labNode.setFormulaForQty(DmShared.xNull(rsTempInner.getString("formulaForQuantity")));
                    labNode.setFormulaForAmt(DmShared.xNull(rsTempInner.getString("formulaForAmount")));
                    labNode.setMainFoundValue(DmShared.xNull(rsTempInner.getString("foundValue")));
                    labNode.setLabQty(DmServer.vNull(rsTempInner.getString("netQty")));
                    labNode.setLabAmt(DmServer.vNull(rsTempInner.getString("amount"), "0.00"));
                    labNode.setValueType(DmShared.xNull(rsTempInner.getString("valueType")));
                    labNode.setRangeUnit(DmShared.xNull(rsTempInner.getString("unit")));
                    labNode.setImpurityManualCode(DmShared.xNull(rsTempInner.getString("manualCode")));
                    node.getLstLabEntry().add(labNode);
                }
                rsTempInner.close();
                psTempInner.close();

                //=================== << check is lab exist or not >> ===================//                
                if (node.getLstLabEntry().size() > 0) {
                    if (DmShared.iNull(node.getLstLabEntry().get(0).getSharedKeyFieldCode()).intValue() > 0) {
                        node.setLabStatus(true);
                    }
                }
                //=================== <<*****>> ===================//

                /*==============================<< Get OverheadLine detail >>===============================*/
                // node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdItemDetail().addAll(lstNode);

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
                    + " LEFT JOIN subGroup sgu ON sgu.subCode = pbd.oh_Ac_UGST \n"
                    + " LEFT JOIN subGroup sgur ON sgur.subCode = pbd.oh_Ac_UGST_RCM \n"
                    + " LEFT JOIN subGroup sgc ON sgc.subCode = pbd.oh_Ac_CGST \n"
                    + " LEFT JOIN subGroup sgcr ON sgcr.subCode = pbd.oh_Ac_CGST_RCM \n"
                    + " LEFT JOIN subGroup sgi ON sgi.subCode = pbd.oh_Ac_IGST \n"
                    + " LEFT JOIN subGroup sgir ON sgir.subCode = pbd.oh_Ac_IGST_RCM \n"
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

                //IAP
                serviceDetail_Mdl.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));

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

            /*==============================<< Get Overhead detail >>===============================*/
            //mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));

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
                if (rsTempInner != null) {
                    rsTempInner.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTempInner != null) {
                    psTempInner.close();
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
    public DmResult<String> deleteRecord(String strKey, String guiConfigCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check for approvedBy before deleting data >>===============================*/
            sqlQuery = " select approvedBy, wtaCode from pBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
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
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", strKey, cnTemp);

            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(strKey, cnTemp);

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason, Walkthrough & gstTrans >>============================*/
            OverHeadLine_Server.delete(strKey, cnTemp);
            OverHead_Server.delete(strKey, cnTemp);
            ModifyReason_Server.delete(strKey, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);
            Gst_Server.deleteGstTrans(strKey, cnTemp);

            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(strKey, cnTemp);

            /*==============================<< update null in lab, before delete pBillDetail >>====================================*/
            manageLabData(strKey, true, cnTemp);

            /*==============================<< Manage Batch Detail >>====================================*/
            Stock_Server.deleteStock(strKey, cnTemp);

            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(strKey, true, cnTemp);

            /*==============================<< Delete records from pBillImpurityDetail >>====================================*/
            sqlQuery = "Delete From pBillImpurityDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pBillDetail >>===============================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pBillDetail", "ibCode", " AND docid = '" + DmShared.iNull(strKey).toString() + "'", cnTemp);
            sqlQuery = " Delete From pBillDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pBill >>===============================*/
            sqlQuery = " Delete From pBill Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //Delete Unused Item Batches
            if (!DmShared.xNull(ibCodeCsv).trim().equalsIgnoreCase("")) {
                deleteUnusedItemBatches(ibCodeCsv, "", cnTemp);
            }

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
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND pb.site_Code = :siteCode"
                    + " AND pb.v_prefix = :vPrefix";

            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY pb.v_Date DESC, vt.short_Name, pb.v_No DESC, pb.recId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile) {
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

            sqlQuery = " SELECT pb.docId, pb.recId, pb.v_Date, pb.subcode,"
                    + " sgs.name as supName, pb.partyBillNo, pb.partyBillDate, "
                    + " vt.short_Name, pb.v_No"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgs ON pb.subcode = sgs.subCode"
                    + "	WHERE vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND pb.site_Code = :site_Code "
                    + " AND pb.v_Prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setSupplierText(DmShared.xNull(rsTemp.getString("supName")));
                listNode.setSupplierBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                listNode.setSupplierBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
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

    private CompareProperties_Mdl iniItemBatchProperties(PurchaseBillDirect_Mdl mrTempCurrent, PurchaseBillDirect_Mdl mrTempPrevious, boolean isDelete) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        if (!isDelete) {
            lstItemBatch_MdlCurnt.clear();
            for (GrdItemDetail_Mdl detail_MdlCurrent : mrTempCurrent.getGrdItemDetail()) {
                ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() > 0) {
                    if (!detail_MdlCurrent.isServiceRecord()) {
                        batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                        batch_Mdl.getIpm().setBatchNo(DmShared.xNull(detail_MdlCurrent.getBatchNo()));
                        batch_Mdl.getIpm().setLotNo(DmShared.xNull(detail_MdlCurrent.getLotNo()));
                        batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(detail_MdlCurrent.getExpiryDate()));
                        batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlCurrent.getManufacturingDate()));
                        batch_Mdl.getIpm().setMrp(DmShared.xNull(detail_MdlCurrent.getMrp()));
                        batch_Mdl.getIpm().setSkuMrp(DmShared.xNull(detail_MdlCurrent.getSkuMrp()));
                        batch_Mdl.getIpm().setColourCode(DmShared.xNull(detail_MdlCurrent.getColourCode()));
                        batch_Mdl.getIpm().setSizeCode(DmShared.xNull(detail_MdlCurrent.getSizeCode()));
                        batch_Mdl.getIpm().setItemMakeCode(DmShared.xNull(detail_MdlCurrent.getItemMakeCode()));

                        batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                        batch_Mdl.setReceiveQty(DmShared.xNull(detail_MdlCurrent.getSkuQty()));
                        batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                        batch_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        batch_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                        batch_Mdl.setvType(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                        batch_Mdl.setvSno(DmShared.xNull(detail_MdlCurrent.getvSNo()));

                        batch_Mdl.setStockSkuValue(DmServer.vNull(detail_MdlCurrent.getAmount(), "0.00"));
                        batch_Mdl.setStockLandedSkuValue(DmServer.vNull(detail_MdlCurrent.getLandedSkuValue()));
                        batch_Mdl.setStockV_Sno(detail_MdlCurrent.getvSNo());
                        batch_Mdl.setStockRefChallanNo(DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                        batch_Mdl.setStockRefChallanDate(DmShared.dNull(mrTempCurrent.getSupplierBillDate()));
                        batch_Mdl.setStockDepartmentCode(DmShared.xNull(detail_MdlCurrent.getDepartmentCode()));

                        lstItemBatch_MdlCurnt.add(batch_Mdl);
                    }
                }
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdItemDetail_Mdl detail_MdlPrev : mrTempPrevious.getGrdItemDetail()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
                    if (!detail_MdlPrev.isServiceRecord()) {
                        batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                        batch_MdlPrev.getIpm().setBatchNo(DmShared.xNull(detail_MdlPrev.getBatchNo()));
                        batch_MdlPrev.getIpm().setLotNo(DmShared.xNull(detail_MdlPrev.getLotNo()));
                        batch_MdlPrev.getIpm().setExpiryDate(DmShared.dNull(detail_MdlPrev.getExpiryDate()));
                        batch_MdlPrev.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlPrev.getManufacturingDate()));
                        batch_MdlPrev.getIpm().setMrp(DmShared.xNull(detail_MdlPrev.getMrp()));
                        batch_MdlPrev.getIpm().setSkuMrp(DmShared.xNull(detail_MdlPrev.getSkuMrp()));
                        batch_MdlPrev.getIpm().setColourCode(DmShared.xNull(detail_MdlPrev.getColourCode()));
                        batch_MdlPrev.getIpm().setSizeCode(DmShared.xNull(detail_MdlPrev.getSizeCode()));
                        batch_MdlPrev.getIpm().setItemMakeCode(DmShared.xNull(detail_MdlPrev.getItemMakeCode()));

                        batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                        batch_MdlPrev.setReceiveQty(DmShared.xNull(detail_MdlPrev.getSkuQty()));
                        batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
                        batch_MdlPrev.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
                        batch_MdlPrev.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
                        batch_MdlPrev.setvType(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
                        batch_MdlPrev.setvSno(DmShared.xNull(detail_MdlPrev.getvSNo()));

                        batch_MdlPrev.setStockSkuValue(DmServer.vNull(detail_MdlPrev.getAmount(), "0.00"));
                        batch_MdlPrev.setStockLandedSkuValue(DmServer.vNull(detail_MdlPrev.getLandedSkuValue()));
                        batch_MdlPrev.setStockV_Sno(detail_MdlPrev.getvSNo());
                        batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(mrTempPrevious.getSupplierBillNo()));
                        batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(mrTempPrevious.getSupplierBillDate()));
                        batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(detail_MdlPrev.getDepartmentCode()));

                        lstItemBatch_MdlPrev.add(batch_MdlPrev);
                    }
                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getObjRecId().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getObjRecId().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierTag()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getObjRecId().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getObjRecId().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempPrevious.getSupplierTag()));
        }
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
        }
        return compareProperties_Mdl;
    }

    private void managePOrderStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, poDocId = "";
        double poQty, challanedQty, grnQty, tl_ExcessQty, tl_ShortageQty;
        Integer isStatus;

        try {

            sqlQuery = "select	p.docId as poDocId, pbd.grnQty, pd.quantity as poQty, pd.challanedQty, pd.isStatus, p.approvedBy, "
                    + "		pd.tl_ExcessQty, pd.tl_ShortageQty, p.recId as poNo, vt.short_name as poType, pd.itemCode as medicineCode, pd.v_Sno as poV_SNo "
                    + "		from pOrderDetail pd "
                    + "		left join pOrder p on p.docId = pd.docId "
                    + "		left join voucher_type vt on vt.v_type = p.v_Type "
                    + "		inner join ( "
                    + "				select	pbd.poDocId, pbd.poV_SNo, sum(pbd.acceptQty) as grnQty "
                    + "				from	pBillDetail pbd "
                    + "				where pbd.docId = :docId "
                    + "				group by pbd.poDocId, pbd.poV_SNo "
                    + "		) pbd on pd.docId = pbd.poDocId and pd.v_SNo = pbd.poV_SNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("poDocId")), DmShared.xNull(rsTemp.getString("poV_SNo")), "Order is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("poDocId")), DmShared.xNull(rsTemp.getString("poV_SNo")), "Order is Cancelled");
                }
                poQty = rsTemp.getDouble("poQty");
                challanedQty = rsTemp.getDouble("challanedQty");
                grnQty = rsTemp.getDouble("grnQty");
                tl_ExcessQty = rsTemp.getDouble("tl_ExcessQty");
                tl_ShortageQty = rsTemp.getDouble("tl_ShortageQty");

                if (isDelete) {
                    challanedQty -= grnQty;
                } else {
                    challanedQty += grnQty;
                }

                if ((challanedQty + tl_ShortageQty == poQty) || (challanedQty - tl_ExcessQty == poQty)) {
                    isStatus = 2;
                } else if (challanedQty > poQty) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("poDocId")), DmShared.xNull(rsTemp.getString("poV_SNo")), "Order overflows by " + DmServer.vNull(challanedQty - poQty));
                } else if (challanedQty < 0) {
                    throw new DmException(700, DmShared.xNull(rsTemp.getString("poDocId")), DmShared.xNull(rsTemp.getString("poV_SNo")), "Order overflows by " + DmServer.vNull(challanedQty));
                } else {
                    isStatus = 0;
                }

                if (!poDocId.equalsIgnoreCase("")) {
                    poDocId += ",";
                }
                poDocId += DmShared.iNull(rsTemp.getString("poDocId")).toString();

                sqlQuery = "update pOrderDetail set "
                        + " isStatus = :isStatus,"
                        + " challanedQty = :challanedQty "
                        + " where docId = :docId "
                        + " and v_SNo = :v_SNo ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("poDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("poV_SNo")).toString());
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("challanedQty", DmServer.vNull(challanedQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!poDocId.equalsIgnoreCase("")) {
                sqlQuery = " update pOrder set u_EntDt_LatestLine = :uEntDt where docId in (" + poDocId + ") ";
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
            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate"
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
                    + " , vt.guiTemplateCode"
                    + " , vt.balancingAc"
                    + " , vt.category"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(partyType).trim().equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                psTemp.setString("category", pvc.PURCHASE_BILL_DIRECT);
            } else {
                psTemp.setString("category", pvc.PURCHASE_BILL_DIRECT_UNREGISTERED);
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

    private List<GrdImpurity_Mdl> getQCPaparameter(String itemCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdImpurity_Mdl> lstTemp = new ArrayList<>();
        GrdImpurity_Mdl grdImpurity_Mdl;

        try {
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, " select iid.impurityCode, "
                    + " im.name as impurityName "
                    + " from itemImpurityDetail iid "
                    + " LEFT JOIN itemImpurityMast iim ON iim.code = iid.code "
                    + " LEFT JOIN impurityMast im ON im.code = iid.impurityCode "
                    + " where iim.testTemplateCode IS NULL "
                    + " AND iim.itemCode = :itemCode ");
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                grdImpurity_Mdl = new GrdImpurity_Mdl();
                grdImpurity_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("impurityCode")));
                grdImpurity_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("impurityName")));
                lstTemp.add(grdImpurity_Mdl);
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

        return lstTemp;
    }

    @Override
    public DmResult<List<GrdImpurity_Mdl>> getQCPaparameter(String itemCode) {
        DmResult<List<GrdImpurity_Mdl>> dmrTemp = new DmResult();
        List<GrdImpurity_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        dmrTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp.addAll(getQCPaparameter(itemCode, cnTemp));
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

    @Override
    public DmResultPageing<List<TxtHTransporter_Mdl>> getTxtHTransporter(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTransporter_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTransporter_Mdl> lstTemp = new ArrayList<>();
        TxtHTransporter_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + " WHERE sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code"
                    + " AND sg.nature = :nature ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("nature", DmServer.getCodeViaManualCode("TP", "manualCode", "subGroupNature", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTransporter_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
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
    public DmResultPageing<List<TxtHGateEntry_Mdl>> getTxtHGateEntryNo(String pBillDocId, String supplierCode, String billDate, String refDate, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHGateEntry_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHGateEntry_Mdl> lstTemp = new ArrayList<>();
        TxtHGateEntry_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.dNull(billDate).trim().equalsIgnoreCase("") || DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                condition += " AND 1=2";
            }
            if (!DmShared.dNull(billDate).trim().equalsIgnoreCase("")) {
                condition += " AND gio.v_Date <= :billDate ";
            }
            if (!DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                condition += " AND gio.v_Date >= :refDate ";
            }

            sqlWithParam.setSQL(" Select gio.docId, gio.recId, gio.v_Date as gateEntryDate, gio.partyCode, "
                    + " gio.transporterCode, "
                    + " sg1.name as transporterName,gio.vehicleNo, gio.v_No, vt.short_Name, gio.isWeighmentRequired  "
                    + " from gateInOut gio "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = gio.v_Type"
                    + " LEFT JOIN subGroup sg ON sg.subCode = gio.partyCode "
                    + " LEFT JOIN subGroup sg1 ON sg1.subCode = gio.transporterCode "
                    + " LEFT JOIN ( "
                    + "     SELECT pc.gateDocId "
                    + "		FROM pChallan pc "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pc.v_Type "
                    + "		WHERE pc.gateDocId IS NOT NULL "
                    + "     UNION"
                    + "     SELECT pb.gateDocId "
                    + "		FROM pBill pb"
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                    + "		WHERE pb.gateDocId IS NOT NULL "
                    + "		AND  vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + "		AND pb.docId <> :pBillDocId "
                    + "     ) vG ON vG.gateDocId = gio.docId "
                    + " WHERE ISNULL(gio.partyCode,:partyCode) = :partyCode "
                    + " AND gio.loadUnload = :loadUnload "
                    + " AND vG.gateDocId IS NULL " + condition);
            sqlWithParam.setString("partyCode", DmShared.iNull(supplierCode).toString());
            sqlWithParam.setString("pBillDocId", DmShared.iNull(pBillDocId).toString());
            sqlWithParam.setString("loadUnload", DmShared.iNull(STEnum.GatePurpose.UNLOADING.getId()).toString());
            if (!DmShared.dNull(billDate).trim().equalsIgnoreCase("")) {
                sqlWithParam.setDate("billDate", DmServer.convertDate(billDate));
            }
            if (!DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                sqlWithParam.setDate("refDate", DmServer.convertDate(refDate));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHGateEntry_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setGateEntryDate(DmShared.dNull(rsTemp.getDate("gateEntryDate")));
                listNode.setTransporterCode(DmShared.xNull(rsTemp.getString("transporterCode")));
                listNode.setTransportername(DmShared.xNull(rsTemp.getString("transporterName")));
                listNode.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                listNode.setWeighmentType(DmShared.iNull(rsTemp.getString("isWeighmentRequired")).intValue() == 1);
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
    public DmResult<List<GrdItemDetail_Mdl>> fillGateEntryDetail(String v_Type, String gateDocId, String partyCode, String location, String billDate, String v_Date,
            String isSiteStateUT, String rcmApplicable, boolean isIncludeTaxInCost, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive) {
        DmResult<List<GrdItemDetail_Mdl>> rstTemp = new DmResult<>();
        List<GrdItemDetail_Mdl> lstTemp = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl listNode;
        ItemProperty_Mdl ipm;
        GrdLabEntry_Mdl grdLabEntry_Mdl;
        Connection cnTemp = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        String sqlQuery, labDocId = "", itemCatFilteration = "", condition = "";
        double skuQty, amount, tempSkuQty = 0;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }
            condition += " AND ISNULL(po.subCode,:partyCode) = :partyCode";

            sqlQuery = "  select unld.docId "
                    + " , unld.v_SNo"
                    + " , unld.itemCode"
                    + " , im.name as itemName"
                    + " , im.propertyList"
                    + " , unld.netWeight AS unloadWt"
                    + " , unld.unitCode as unloadUnitCode"
                    + " , unld.poDocId"
                    + " , po.recId as poRecId"
                    + " , unld.poV_SNo"
                    + " , po.refNo AS poRefNo"
                    + " , pod.rate"
                    + " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) AS poPendingQty"
                    + " , lab.docId as labDocId"
                    + " , im.isLabRequired"
                    + " , pod.colourCode, icm.name AS colourName"
                    + " , pod.itemMakeCode, imm.companyName AS itemMakeName"
                    + " , pod.sizeCode, ism.name AS sizeName  "
                    + " , im.sku AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces  "
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , im.purchaseUnitConversionRate"
                    + " , um.decimalPlaces AS purchaseUnitDecimalPlaces "
                    + " , unld.departmentCode, sgd.name as departmentName "
                    + " , im.itsBasedOn, im.isBestBefore ,im.subCode AS iSubCode\n"
                    + " , im.hsnCode AS masterHsnCode, hm.hsn masterHsn, im.itemGSTNature AS masterItemGSTNature \n"
                    + " , pod.hsnCode, hsn.hsn, pod.itemGSTNature, pod.hsnCode AS hsnCode, hsn.hsn \n"
                    + " , pod.isManualGstType, pod.gstTypeCode, it.name AS gstTypeName\n "
                    + " , pod.oh_Amt_Discount\n"
                    + " , pod.oh_at_Discount\n"
                    + " , pod.oh_at_SGST\n"
                    + " , pod.oh_at_UGST\n"
                    + " , pod.oh_at_CGST\n"
                    + " , pod.oh_at_IGST\n"
                    + " , im.purchaseAc\n "
                    + " from unloadingDetail unld  "
                    + " LEFT JOIN unloading ul ON ul.docId = unld.docId  "
                    + " LEFT JOIN itemMast im ON im.code = unld.itemCode  "
                    + " LEFT JOIN pOrderDetail pod ON pod.docId = unld.poDocId AND pod.v_SNo = unld.poV_SNo AND pod.itemCode = unld.itemCode"
                    + " LEFT JOIN pOrder po ON po.docId = pod.docId  "
                    + " LEFT JOIN unitMast um ON um.code = im.purchaseUnit"
                    + " LEFT JOIN unitMast ums ON ums.code = im.sku"
                    + " Left JOIN labSample ls ON ls.refDocId = unld.docId and ls.refV_Sno = unld.v_SNo and ls.itemCode = unld.itemCode "
                    + " Left JOIN lab ON lab.labSampleDocId = ls.docId "
                    + " LEFT JOIN itemColourMast icm ON pod.colourCode = icm.code "
                    + " LEFT JOIN itemSizeMast ism ON pod.sizeCode = ism.code  "
                    + " LEFT JOIN itemMakemast imm ON pod.itemMakeCode=imm.code "
                    + " LEFT JOIN subGroup sgd ON sgd.subCode = unld.departmentCode "
                    + " LEFT JOIN gstType it ON it.code = pod.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pod.hsnCode \n"
                    + " LEFT JOIN hsnMast hm on hm.code = im.hsnCode \n"
                    + " WHERE unld.gateDocId = :gateDocId " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("gateDocId", DmShared.iNull(gateDocId).toString());
            psTemp.setString("partyCode", DmShared.xNull(partyCode));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdItemDetail_Mdl();

                listNode.setUnloadingDocId(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setUnloadingV_Sno(DmShared.iNull(rsTemp.getString("v_SNo")).toString());

                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));

                listNode.setPoDocId((DmShared.iNull(rsTemp.getString("poDocId")).toString()));
                listNode.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                listNode.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                listNode.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //Item 
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));

                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());

                //Purchase A/c (Based On Item Master)
                listNode.setItemMastPurchaseAc(DmShared.xNull(rsTemp.getString("purchaseAc")));
                if (isInventoryAccountingActive) {
                    //IAP
                    listNode.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
                } else {
                    if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                        if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.ITEM.getId()) {
                            listNode.setIsubCode(DmShared.xNull(rsTemp.getString("purchaseAc")));
                        }
                    }
                }

                if (DmShared.iNull(rsTemp.getString("poDocId")).longValue() > 0) {
                    //=============== When PO Exists =================================//
                    //Item Details (PO)           
                    listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                    listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                    listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                    listNode.setPendingQty(DmServer.vNull(rsTemp.getString("poPendingQty")));
                    listNode.setChallanQty(DmServer.vNull(rsTemp.getString("poPendingQty")));
                    listNode.setRecQty(DmServer.vNull(rsTemp.getString("poPendingQty")));
                    listNode.setAcceptQty(DmServer.vNull(rsTemp.getString("poPendingQty")));

                    skuQty = DmShared.vNull(rsTemp.getString("poPendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                    listNode.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                    listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    //Amount
                    amount = DmShared.vNull(rsTemp.getString("poPendingQty")) * DmShared.vNull(rsTemp.getString("rate"));
                    listNode.setAmount(DmServer.vNull(amount, "0.00"));
                    listNode.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                    listNode.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));

                    if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1) {
                        listNode.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                        listNode.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));

                        if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                            listNode.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                        } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                            listNode.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                        }

                        listNode.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                        listNode.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                        listNode.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                        listNode.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                    }

                } else {
                    //============================== When PO Not Exists =================================//
                    //Item Details (Master)             
                    listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("masterItemGSTNature")));
                    listNode.setHsnCode(DmShared.xNull(rsTemp.getString("masterHsnCode")));
                    listNode.setHsn(DmShared.xNull(rsTemp.getString("masterHsn")));

                    if (DmShared.iNull(rsTemp.getString("unloadUnitCode")).intValue() == DmShared.iNull(rsTemp.getString("purchaseUnitCode")).intValue()) {
                        //=============== When Unloading Weighment Unit = Item Purchase Unit =================================//
                        listNode.setPendingQty(DmServer.vNull(rsTemp.getString("unloadWt")));
                        listNode.setChallanQty(DmServer.vNull(rsTemp.getString("unloadWt")));
                        listNode.setRecQty(DmServer.vNull(rsTemp.getString("unloadWt")));
                        listNode.setAcceptQty(DmServer.vNull(rsTemp.getString("unloadWt")));

                        skuQty = DmShared.vNull(rsTemp.getString("unloadWt")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                        listNode.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));
                    } else if (DmShared.iNull(rsTemp.getString("unloadUnitCode")).intValue() == DmShared.iNull(rsTemp.getString("skuCode")).intValue()) {
                        //=============== When Unloading Weighment Unit = Item SKU =================================//
                        skuQty = DmShared.vNull(rsTemp.getString("unloadWt"));
                        String puQty = DmServer.vNull(tempSkuQty / DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate")).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).intValue()));
                        listNode.setPendingQty(DmServer.vNull(puQty));
                        listNode.setChallanQty(DmServer.vNull(puQty));
                        listNode.setRecQty(DmServer.vNull(puQty));
                        listNode.setAcceptQty(DmServer.vNull(puQty));

                        tempSkuQty = DmShared.vNull(puQty) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                        if (skuQty != tempSkuQty) {
                            listNode.setSkuDifferenceQty(DmServer.vNull(DmServer.vNull((skuQty - tempSkuQty), skuDecimalFormat)));
                        }
                        listNode.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));
                    }
                }

                labDocId = DmShared.xNull(rsTemp.getString("labDocId"));
                listNode.setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));

                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());

                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                listNode.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                listNode.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                listNode.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                listNode.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                ipm = new ItemProperty_Mdl();
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setPropertiesCsv(DmShared.xNull(ipm.getPropertiesCsv()));

                /*==============================<< Get lab detail >>===============================*/
                sqlQuery = " select ld.docId, ld.v_SNo, ld.impurityCode, im.name as impurityName,im.manualCode, "
                        + " ld.range1, ld.range2, um.unit,im.valueType,"
                        + " ld.formulaForQuantity, ld.formulaForAmount,ld.helpSource, "
                        + " ld.foundValue, ld.formulaForValue "
                        + " from labDetail ld "
                        + " LEFT JOIN impurityMast im ON im.code = ld.impurityCode "
                        + " LEFT JOIN unitMast um ON um.code = im.unitCode "
                        + " where ld.docId = :docId ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.xNull(labDocId));
                rsTemp1 = psTemp1.executeQuery();
                while (rsTemp1.next()) {
                    grdLabEntry_Mdl = new GrdLabEntry_Mdl();
                    grdLabEntry_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp1.getString("impurityCode")));
                    grdLabEntry_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp1.getString("impurityName")));
                    grdLabEntry_Mdl.setRange1(DmServer.vNull(rsTemp1.getString("range1")));
                    grdLabEntry_Mdl.setRange2(DmServer.vNull(rsTemp1.getString("range2")));
                    grdLabEntry_Mdl.setFormulaForQty(DmShared.xNull(rsTemp1.getString("formulaForQuantity")));
                    grdLabEntry_Mdl.setFormulaForAmt(DmShared.xNull(rsTemp1.getString("formulaForAmount")));
                    grdLabEntry_Mdl.setFormulaForValue(DmShared.xNull(rsTemp1.getString("formulaForValue")));
                    grdLabEntry_Mdl.setHelpSource(DmShared.xNull(rsTemp1.getString("helpSource")));
                    grdLabEntry_Mdl.setMainFoundValue(DmShared.xNull(rsTemp1.getString("foundValue")));
                    grdLabEntry_Mdl.setRangeUnit(DmShared.xNull(rsTemp1.getString("unit")));
                    grdLabEntry_Mdl.setValueType(DmShared.xNull(rsTemp1.getString("valueType")));
                    grdLabEntry_Mdl.setImpurityManualCode(DmShared.xNull(rsTemp1.getString("manualCode")));
                    listNode.getLstLabEntry().add(grdLabEntry_Mdl);
                }
                rsTemp1.close();
                psTemp1.close();

                //=================== <<check is lab exist or not >> ===================//                
                if (listNode.getLstLabEntry().size() > 0) {
                    if (DmShared.iNull(listNode.getLstLabEntry().get(0).getSharedKeyFieldCode()).intValue() > 0) {
                        listNode.setLabStatus(true);
                    }
                }
                //=================== <<*****>> ===================//

                //fill QC Parameter Info
                listNode.getLstImpurity().addAll(getQCPaparameter(listNode.getSharedKeyFieldCode(), cnTemp));

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
                        listNode.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                        listNode.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                    }

                    //Get Purchase A/c From GSTS
                    if (!isInventoryAccountingActive) {
                        if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                            if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.GSTS.getId()) {
                                for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                                    if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.PURCHASE_ACCOUNT.getShortName())) {
                                        listNode.setIsubCode(iTS_Mdl.getPurchaseSalesAcCode());
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
                                    taxAcCode = DmShared.xNull(listNode.getIsubCode());
                                }
                            }
                        } else {
                            taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                            rcmAcCode = DmShared.xNull(iTS_Mdl.getRcmAcCode());
                            taxAcCode = DmShared.xNull(iTS_Mdl.getInputTaxAcCode());
                        }

                        if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            listNode.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setSgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setSgstAcCode(taxAcCode);
                            listNode.setSgstRcmAcCode(rcmAcCode);

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            listNode.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setUgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setUgstAcCode(taxAcCode);
                            listNode.setUgstRcmAcCode(rcmAcCode);

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                            listNode.setCgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setCgstAcCode(taxAcCode);
                            listNode.setCgstRcmAcCode(rcmAcCode);

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                            listNode.setIgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setIgstAcCode(taxAcCode);
                            listNode.setIgstRcmAcCode(rcmAcCode);
                        }
                    }
                }

                //================<< ******* >>==================
                lstTemp.add(listNode);
            }
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
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getCreateHelp(String helpSource, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            if (!DmShared.xNull(helpSource).equalsIgnoreCase("")) {
                String[] str = DmShared.xNull(helpSource).split("\\|");
                for (String data : str) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(data);
                    listNode.setSharedKeyFieldName(data);
                    lstTemp.add(listNode);
                }
            }
            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    private void manageLabData(String docId, boolean deleteFlag, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String strSQL, fields = "";
        ResultSet rsTemp = null;
        try {

            if (deleteFlag) {
                fields = " usedDocId = null "
                        + " , usedV_Sno = null ";
            } else {
                fields = " usedDocId = vpb.usedDocId "
                        + " , usedV_Sno = vpb.usedV_Sno ";
            }

            strSQL = " update lab set "
                    + fields
                    + " , u_EntDt_LatestLine = :uEntDt "
                    + " from  ( "
                    + "     select pbd.docId as usedDocId, pbd.v_SNo as usedV_Sno, lb.docId as labDocId "
                    + "     from pBillDetail pbd "
                    + "     LEFT JOIN labSample ls ON ls.refDocId = pbd.unloadingDocId AND ls.refV_Sno = pbd.unloadingV_Sno "
                    + "     LEFT JOIN lab lb ON lb.labSampleDocId = ls.docId "
                    + "     WHERE pbd.docId = :docId "
                    + "     AND lb.docId IS NOT NULL "
                    + "     ) as vpb "
                    + " where lab.docId = vpb.labDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
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

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, boolean isInventoryAccountingActive, boolean includeOverHeadsInCost, boolean isSystemGenerated, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", serviceSql = "", roundOffSql = "", baseSql = "", partySql = "", taxSql = "", finalDiscountSql = "", purchaseAcSql = "";
        String topInventoryAc = "";
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
                    + "		TOP 1 " + (isSystemGenerated ? " pbd.oh_Ac_Item as topAc \n" : " pb.balancingAc as topAc \n")
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
                topInventoryAc = DmShared.xNull(rsTemp.getString("topAc"));
            }
            rsTemp.close();
            psTemp.close();

            ++sr;
            partySql = "-- Net Amount Posting\n"
                    + "SELECT \n"
                    + (isSystemGenerated ? "	pb.balancingAc as subCode \n" : "	pb.subCode as subCode \n")
                    + "	, :topInventoryAc AS contraSub\n"
                    + "	, -ISNULL(pb.oh_Amt_Net, 0) AS amount\n"
                    + "	, " + sr + " AS sr\n"
                    + "FROM pBill pb\n"
                    + "WHERE pb.docId = :docId\n"
                    + "AND pb.subCode IS NOT NULL\n"
                    + "AND ISNULL(pb.oh_Amt_Net, 0) <> 0\n";

            if (isInventoryAccountingActive) {
                ++sr;
                String serviceUnion = "";
                if (!isSystemGenerated) {
                    serviceUnion += "	UNION ALL \n"
                            + "SELECT \n"
                            + (isSystemGenerated ? "		pbd.oh_Ac_Item AS subCode\n" : "		pb.balancingAc AS subCode\n")
                            + (isSystemGenerated ? "		, pb.balancingAc AS contraSub\n" : "		, pb.subCode AS contraSub\n")
                            + "		, ISNULL(pbd.oh_Amt_Net, 0) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pbd.isFooter = 2\n"
                            + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                            + "	AND ISNULL(pbd.oh_Amt_Taxable, 0) <> 0\n";
                }
                inventorySql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Inventory Posting\n"
                        + "	SELECT \n"
                        + (isSystemGenerated ? "		pbd.oh_Ac_Item AS subCode\n" : "		pb.balancingAc AS subCode\n")
                        + (isSystemGenerated ? "		, pb.balancingAc AS contraSub\n" : "		, pb.subCode AS contraSub\n")
                        + (isSystemGenerated ? "		, pbd.landedSkuValue AS amount\n" : "		, isNull(pbd.oh_Amt_Net,0)+ISNULL(pb.oh_Amt_RoundOff,0) AS amount\n")
                        // +  "	,  pbd.landedSkuValue AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + (includeOverHeadsInCost ? "	AND pbd.isFooter <> 1\n" : "	AND pbd.isFooter = 0\n")
                        + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                        + "	AND IsNull(pbd.landedSkuValue, 0) <> 0\n"
                        + "\n" + serviceUnion
                        + "	) t\n"
                        + "GROUP BY t.subCode\n";
                if (!includeOverHeadsInCost) {
                    ++sr;
                    serviceSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                            + "FROM (\n"
                            + "	-- Service Posting\n"
                            + "	SELECT \n"
                            + (isSystemGenerated ? "		pbd.oh_Ac_Item AS subCode\n" : "		pb.balancingAc AS subCode\n")
                            + (isSystemGenerated ? "		, pb.balancingAc AS contraSub\n" : "		, pb.subCode AS contraSub\n")
                            + "		, ISNULL(pbd.oh_Amt_Net, 0) AS amount\n"
                            + "	FROM pBill pb\n"
                            + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                            + "	WHERE pb.docId = :docId\n"
                            + "	AND pbd.isFooter = 2\n"
                            + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                            + "	AND ISNULL(pbd.oh_Amt_Taxable, 0) <> 0\n"
                            + "	) t\n"
                            + "GROUP BY t.subCode\n";
                }
            } else {
                ++sr;
                purchaseAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Purchase A/c Posting\n"
                        + "	SELECT \n"
                        + (isSystemGenerated ? "		pbd.oh_Ac_Item AS subCode\n" : "		pb.balancingAc AS subCode\n")
                        + (isSystemGenerated ? "		, pb.balancingAc AS contraSub\n" : "		, pb.subCode AS contraSub\n")
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
                        + (isSystemGenerated ? "		pbd.oh_Ac_Item AS subCode\n" : "		pb.balancingAc AS subCode\n")
                        + (isSystemGenerated ? "		, pb.balancingAc AS contraSub\n" : "		, pb.subCode AS contraSub\n")
                        + "		, ISNULL(pbd.oh_Amt_Taxable, 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pbd.isFooter = 2\n"
                        + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                        + "	AND ISNULL(pbd.oh_Amt_Taxable, 0) <> 0\n"
                        + "	) t\n"
                        + "GROUP BY t.subCode\n";

                if (isSystemGenerated) {
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
                    + getTaxPostingSql("pbd.oh_Ac_SGST", sgstContraAc, "pbd.oh_Amt_SGST")
                    + "\n"
                    + "	UNION ALL\n"
                    + "\n"
                    + "	-- UGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_UGST", ugstContraAc, "pbd.oh_Amt_UGST")
                    + "\n"
                    + "	UNION ALL \n"
                    + "\n"
                    + "	-- CGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_CGST", cgstContraAc, "pbd.oh_Amt_CGST")
                    + "\n"
                    + "	UNION ALL\n"
                    + "\n"
                    + "	-- IGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_IGST", igstContraAc, "pbd.oh_Amt_IGST")
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";

            baseSql = partySql
                    + "\n"
                    //                    + (isSystemGenerated && includeOverHeadsInCost ? "" : "UNION ALL\n")
                    + (isSystemGenerated && includeOverHeadsInCost ? "" : (isSystemGenerated && !includeOverHeadsInCost ? "UNION ALL\n" : ""))
                    + "\n"
                    //                    + (isSystemGenerated && includeOverHeadsInCost? "" : taxSql)
                    + (isSystemGenerated && includeOverHeadsInCost ? "" : (isSystemGenerated && !includeOverHeadsInCost ? taxSql : ""))
                    + "\n";
            if (isInventoryAccountingActive) {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + inventorySql
                        + "\n";
                //=====================< 17-Jun-2023 , Due to error in Legder Psoting, Due to service value also included in landed value >=====================================================
//                        + (!includeOverHeadsInCost?" UNION ALL\n "+serviceSql+"\n":"");
            } else {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + purchaseAcSql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + serviceSql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + finalDiscountSql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + roundOffSql;
            }
            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("topInventoryAc", DmShared.xNull(topInventoryAc));
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
            psTemp.setString("topInventoryAc", DmShared.xNull(topInventoryAc));
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

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, boolean isInventoryAccountingActive, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", serviceSql = "", roundOffSql = "", baseSql = "", partySql = "", taxSql = "", finalDiscountSql = "", purchaseAcSql = "";
        String topInventoryAc = "";
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
                    + "		TOP 1 pbd.oh_Ac_Item as topAc \n"
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
                topInventoryAc = DmShared.xNull(rsTemp.getString("topAc"));
            }
            rsTemp.close();
            psTemp.close();

            ++sr;
            partySql = "-- Net Amount Posting\n"
                    + "SELECT \n"
                    + "	pb.subCode as subCode \n"
                    + "	, :topInventoryAc AS contraSub\n"
                    + "	, -ISNULL(pb.oh_Amt_Net, 0) AS amount\n"
                    + "	, " + sr + " AS sr\n"
                    + "FROM pBill pb\n"
                    + "WHERE pb.docId = :docId\n"
                    + "AND pb.subCode IS NOT NULL\n"
                    + "AND ISNULL(pb.oh_Amt_Net, 0) <> 0\n";

            ++sr;
            purchaseAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Purchase A/c Posting\n"
                    + "	SELECT \n"
                    + "     pbd.oh_Ac_Item AS subCode\n"
                    + "  , pb.subCode AS contraSub\n"
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
                    + "     pbd.oh_Ac_Item AS subCode \n"
                    + "	, pb.balancingAc AS contraSub \n"
                    + "		, ISNULL(pbd.oh_Amt_Taxable, 0) AS amount \n"
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
                    + getTaxPostingSql("pbd.oh_Ac_SGST", sgstContraAc, "pbd.oh_Amt_SGST")
                    + "\n"
                    + "	UNION ALL\n"
                    + "\n"
                    + "	-- UGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_UGST", ugstContraAc, "pbd.oh_Amt_UGST")
                    + "\n"
                    + "	UNION ALL \n"
                    + "\n"
                    + "	-- CGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_CGST", cgstContraAc, "pbd.oh_Amt_CGST")
                    + "\n"
                    + "	UNION ALL\n"
                    + "\n"
                    + "	-- IGST Posting\n"
                    + getTaxPostingSql("pbd.oh_Ac_IGST", igstContraAc, "pbd.oh_Amt_IGST")
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";

            baseSql = partySql
                    + "\n"
                    + "UNION ALL\n"
                    + "\n"
                    + taxSql
                    + "\n";
            if (isInventoryAccountingActive) {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + inventorySql
                        + "\n";
            } else {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + purchaseAcSql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + serviceSql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + finalDiscountSql
                        + "\n"
                        + "UNION ALL\n"
                        + "\n"
                        + roundOffSql;
            }
            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("topInventoryAc", DmShared.xNull(topInventoryAc));
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
            psTemp.setString("topInventoryAc", DmShared.xNull(topInventoryAc));
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

    private String getTaxPostingSql(String subCodeField, String contraAcField, String amountField) throws Exception {
        String returnValue = "";
        try {
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
        } catch (Exception e) {
            throw e;
        }
        return returnValue;
    }

    //</editor-fold>   
    @Override
    public DmResult<String> updateRecord(PurchaseBillDirect_Mdl mrTempCurrent, boolean isApprove) {
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
                        + " ,EWayBillNo = :EWayBillNo "
                        + " ,EWayBillDt = :EWayBillDt "
                        + " ,LRNo = :LRNo "
                        + " ,LRDate = :LRDate "
                        + " ,transporterCode = :transporterCode "
                        + " , PaymentDueDate = :PaymentDueDate \n"
                        + " , u_Name = :u_Name \n"
                        + " , u_EntDt = :uEntDt \n"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getSupplierBillDate()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("LRNo", DmShared.xNull(mrTempCurrent.getLrNo()));
                psTemp.setDate("LRDate", DmServer.convertDate(mrTempCurrent.getLrDate()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
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

    private void billBillHistory(String pbDocid, Connection cnTemp, List<GrdBillOtherDetails_Mdl> lstGrd) throws SQLException, DmException {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GrdBillOtherDetails_Mdl grdMdl = new GrdBillOtherDetails_Mdl();
        try {


            /*==============================<< Get default department >>===============================*/
//            sqlQuery = " select convert(varchar,bh.u_EntDt,22) dateTime,bh.recId, "
            sqlQuery = "	  Select  REPLACE(CONVERT(VARCHAR(11), bh.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,bh.u_EntDt,100 ) ,7), 6, 0, ' ') dateTime,bh.recId,      \n"
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
            sqlQuery = "	  Select  REPLACE(CONVERT(VARCHAR(11), fpe.u_EntDt, 106), ' ', '-') +' '+STUFF(RIGHT( CONVERT(VARCHAR,fpe.u_EntDt,100 ) ,7), 6, 0, ' ') PayDateTime,      \n"
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
}
