package aarogya.inventory.grn.server;

import aarogya.inventory.grn.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import static dataman.dmToolkit.stock.server.Stock_Server.deleteUnusedItemBatches;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.itemUpdatePopup.server.ItemUpdateServer;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdate_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
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
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ImpurityNature;
import aarogya.selfToolkit.stStatic.shared.STShared;

public class Grn_SrvImpl extends DmRSSPrivate implements Grn_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pc.V_Type", voucherDateFieldName = "pc.V_Date";

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
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " , vt.balancingAc"
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "', '" + pvc.SERVICE_CHALLAN + "', '" + pvc.SERVICE_CHALLAN_UNREGISTERED + "')"
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSupplier("", voucherCategory, flagFilterByVoucherCategory, v_Type, helper, blnToEndOfFile, cnTemp);
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
                    + " Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "	WHERE sgsl.site_Code = :siteCode and sgn.manualCode IN ('CM','PR') "
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

            condition = " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)";
            condition += " AND im.isActive = 1";
            condition += " AND sl.site_Code = :site_Code";
            condition += " AND ISNULL(im.isBilledItem, 0) = 0";

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

            sqlQuery = "SELECT \n"
                    + "   im.code \n"
                    + " , im.name \n"
                    + " , im.manualCode \n"
                    + " , im.hsnCode \n"
                    + " , im.itemGSTNature \n"
                    + " , im.itsBasedOn \n"
                    + " , im.isBestBefore \n"
                    + " , im.propertyList \n"
                    + " , im.sku AS skuCode \n"
                    + " , ums.unit AS skuName \n"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces \n"
                    + " , im.purchaseUnit AS purchaseUnitCode \n"
                    + " , um.unit AS purchaseUnitName \n"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate \n"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces \n"
                    + " , im.isLabRequired,im.purchaseRate \n"
                    + " , im.subCode AS iSubCode \n"
                    + " , hsn.hsn \n"
                    + " FROM itemMast im \n"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code \n"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code \n"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = im.hsnCode \n";
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ( \n"
                        + "         SELECT ird.code  \n"
                        + "         FROM itemRestrictedToDepartment ird  \n"
                        + "         WHERE ird.departmentCode = :departmentCode \n"
                        + "     ) vird ON vird.code = im.Code  \n";

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
                listNode.setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));
                listNode.setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));

                //IAP
                listNode.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<TxtHOrderNo_Mdl>> getTxtHOrderNo(String subCode, String v_Date, String docId, String v_SNo, String restrictedDepartment, String voucherCategory, DmHelper pageing, boolean blnToEndOfFile) {
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
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0) AS pendingQty";
                condition += " AND (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
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
                    + " , pod.itemMakeCode"
                    + " , imm.companyName AS itemMakeName"
                    + " , pod.sizeCode"
                    + " , ism.name AS sizeName"
                    + " , pod.unitCode"
                    + " , um.unit AS unitName" + pendingQty
                    ///////////////////////////////////////
                    + " , im.hsnCode"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , im.sku AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , pod.unitCode AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , im.isLabRequired "
                    + " , pod.oh_At_Discount, pod.oh_Amt_Discount"
                    + " , im.subCode AS iSubCode \n"
                    + " , hsn.hsn, pod.isManualGstType \n"
                    + " , it.name AS gstTypeName\n"
                    + " , pod.gstTypeCode\n"
                    ////////////////////////////////////////
                    + " FROM POrderDetail pod"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pcd.poDocId,pcd.poV_SNo,pcd.acceptQty "
                        + "     FROM pChallanDetail pcd"
                        + "     WHERE pcd.docid = :docId"
                        + "     AND pcd.v_SNo = :v_SNo"
                        + " ) AS pcd ON pod.docId = pcd.poDocId AND pod.v_SNO = pcd.poV_SNo";
            }
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ("
                        + "         SELECT ird.code "
                        + "         FROM itemRestrictedToDepartment ird "
                        + "         WHERE ird.departmentCode = :departmentCode"
                        + "   ) vird ON vird.code = im.Code ";
            }
            sqlQuery += " LEFT JOIN itemColourMast icm ON pod.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pod.sizeCode = ism.code "
                    + " Left join itemMakemast imm ON pod.itemMakeCode=imm.code "
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code "
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code "
                    + " LEFT JOIN hsnMast hsn on hsn.code = im.hsnCode \n"
                    + " LEFT JOIN gstType it ON it.code = pod.gstTypeCode \n"
                    + "	WHERE vt.category IN (:vCat,:vCatU) "
                    + " AND po.subCode = :subCode "
                    + " AND isNull(pod.isFooter,0) = 0 "
                    + " AND ISNULL(po.approvedBy,'') <> ''"
                    + " AND po.site_Code = :site_Code"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_CHALLAN_UNREGISTERED)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_CHALLAN)) {
                sqlWithParam.setString("vCat", pvc.PURCHASE_ORDER);
                sqlWithParam.setString("vCatU", pvc.PURCHASE_ORDER_UNREGISTERED);
            } else {
                sqlWithParam.setString("vCat", pvc.SERVICE_ORDER);
                sqlWithParam.setString("vCatU", pvc.SERVICE_ORDER_UNREGISTERED);
            }

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
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                listNode.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                listNode.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                ipm = new ItemProperty_Mdl();
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setProperties(DmShared.xNull(ipm.getPropertiesCsv()));

                listNode.getTxtHItemName_Mdl().setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.getTxtHItemName_Mdl().setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.getTxtHItemName_Mdl().setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.getTxtHItemName_Mdl().setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.getTxtHItemName_Mdl().setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.getTxtHItemName_Mdl().setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.getTxtHItemName_Mdl().setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
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
                listNode.getTxtHItemName_Mdl().setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));
                listNode.getTxtHItemName_Mdl().setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));
                //IAP
                listNode.getTxtHItemName_Mdl().setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));

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

    @Override
    public DmResultPageing<List<TxtHGateEntry_Mdl>> getTxtHGateEntryNo(String pChallanDocId, String supplierCode, String grnDate, String refDate, DmHelper helper, boolean blnToEndOfFile) {
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

            if (DmShared.dNull(grnDate).trim().equalsIgnoreCase("") || DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                condition += " AND 1=2";
            }
            if (!DmShared.dNull(grnDate).trim().equalsIgnoreCase("")) {
                condition += " AND gio.v_Date <= :grnDate ";
            }
            if (!DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                condition += " AND gio.v_Date >= :refDate ";
            }
            sqlWithParam.setSQL(" Select gio.docId, gio.recId, gio.v_Date as gateEntryDate, gio.partyCode, "
                    + " gio.transporterCode, "
                    + " sg1.name as transporterName,gio.vehicleNo, gio.v_No, vt.short_Name  "
                    + " from gateInOut gio "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = gio.v_Type"
                    //                    + " LEFT JOIN weighment wgt ON wgt.gateDocId = gio.docId "
                    //                    + " LEFT JOIN unloading unld ON unld.weighmentDocId = wgt.docid "
                    + " LEFT JOIN subGroup sg1 ON sg1.subCode = gio.transporterCode "
                    + " LEFT JOIN ( "
                    + "     SELECT pc.gateDocId "
                    + "		FROM pChallan pc "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pc.v_Type "
                    + "		WHERE pc.gateDocId IS NOT NULL "
                    + "		AND  vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + "		AND pc.docId <> :pChallanDocId "
                    + "     UNION"
                    + "     SELECT pb.gateDocId "
                    + "		FROM pBill pb"
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                    + "		WHERE pb.gateDocId IS NOT NULL "
                    + "     ) vG ON vG.gateDocId = gio.docId "
                    + " WHERE gio.partyCode = :partyCode " // AND ISNULL(wgt.outWeighBy,'') <> ''
                    + " AND vG.gateDocId IS NULL " + condition);
            sqlWithParam.setString("partyCode", DmShared.iNull(supplierCode).toString());
            sqlWithParam.setString("pChallanDocId", DmShared.iNull(pChallanDocId).toString());
            if (!DmShared.dNull(grnDate).trim().equalsIgnoreCase("")) {
                sqlWithParam.setDate("grnDate", DmServer.convertDate(grnDate));
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

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Grids">
    @Override
    public DmResult<List<GrdGrnDetail_Mdl>> getGrnDetail(String v_Type, List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String docId, String v_Date, String isSiteStateUT) {
        DmResult<List<GrdGrnDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, conditionLines = "", condition = "", pendingQty = "", itemCatFilteration = "";
        List<GrdGrnDetail_Mdl> lstNode = new ArrayList<GrdGrnDetail_Mdl>();
        GrdGrnDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0) AS pendingQty";
                condition += " AND (CASE WHEN (pod.isStatus = 0) OR (pod.isStatus = 2 AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0) + ISNULL(pcd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";
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

            /*==============================<< Get Grn detail (via list) >>===============================*/
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

            sqlQuery = "SELECT  pod.itemCode, im.name as itemName, im.sku, ums.unit as skuName, "
                    + "         ums.decimalPlaces AS skuDecimalPlaces, "
                    + "		po.docId as poDocId, po.recId as poRecId, po.refNo AS poRefNo, "
                    + "         pod.v_SNo AS poV_SNo,  pod.unitCode, um.unit as unitName, "
                    + "		pod.quantity as orderQty, pod.challanedQty, pod.rate, "
                    + "		pod.colourCode,icm.name as colourName,pod.itemMakeCode ,"
                    + "         imm.companyName as itemMakeName, um.decimalPlaces AS unitDecimalPlaces, "
                    + "         pod.sizeCode as sizeCode, im.isLabRequired, pod.oh_At_Discount, pod.oh_Amt_Discount, "
                    + "         ism.name sizeName,im.propertyList, im.purchaseUnitConversionRate,im.purchaseRate,"
                    + "         im.itsBasedOn, im.isBestBefore, im.subCode AS iSubCode, pod.hsnCode, hsn.hsn, pod.itemGSTNature, \n"
                    + "         pod.isManualGstType, pod.gstTypeCode, it.name AS gstTypeName\n " + pendingQty
                    + " FROM POrderDetail pod"
                    + "	LEFT JOIN pOrder po ON pod.docId = po.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pcd.*"
                        + "     FROM pChallanDetail pcd"
                        + "     WHERE pcd.docid = :docId"
                        + " ) AS pcd ON pod.docId = pcd.poDocId AND pod.v_SNO = pcd.poV_SNo ";
            }
            sqlQuery += " LEFT JOIN itemMast im ON pod.itemCode = im.code "
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode "
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode "
                    + " LEFT JOIN itemMakemast imm ON imm.code=pod.itemMakeCode "
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code "
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code "
                    + " LEFT JOIN gstType it ON it.code = pod.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pod.hsnCode \n"
                    + "	WHERE po.subCode = :subCode "
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
                node = new GrdGrnDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Detail
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
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourName(node.getColourName());
                ipm.setItemMakeName(node.getItemMakeName());
                ipm.setSizeName(node.getSizeName());
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
                node.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate")));

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
                double amount = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("rate"));
                node.setAmount(DmServer.vNull(amount, "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_Discount"), "0.00"));
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));

                //IAP
                node.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));

                //GST
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));

                DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(DmShared.xNull(rsTemp.getString("itemCode")),
                        DmServer.vNull(rsTemp.getString("rate"), "0.00"),
                        DmShared.xNull(rsTemp.getString("itsBasedOn")),
                        location,
                        DmShared.xNull(rsTemp.getString("gstTypeCode")),
                        (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1), cnTemp);

                if (dmrIts.isStatus() && dmrIts.getData() != null) {
                    String taxPer;

                    if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 0) {
                        node.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                        node.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                    }

                    for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                        taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");

                        if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            node.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setSgstPer(DmServer.vNull(taxPer, "0.00"));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            node.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            node.setUgstPer(DmServer.vNull(taxPer, "0.00"));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                            node.setCgstPer(DmServer.vNull(taxPer, "0.00"));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                            node.setIgstPer(DmServer.vNull(taxPer, "0.00"));

                        }
                    }
                }

                //fill QC Parameter Info
                node.getLstImpurity().addAll(getQCPaparameter(node.getSharedKeyFieldCode(), cnTemp));

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
    public DmResultPageing<List<GrdGrnDetail_Mdl>> getPrevGrn(String itemCode, String docId, String v_SNo, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param) {
        DmResultPageing<List<GrdGrnDetail_Mdl>> dmrTemp = new DmResultPageing<List<GrdGrnDetail_Mdl>>();
        List<GrdGrnDetail_Mdl> lstTemp = new ArrayList<GrdGrnDetail_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        GrdGrnDetail_Mdl node;
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
                node = new GrdGrnDetail_Mdl();
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

    @Override
//    public DmResult<List<GrdGrnDetail_Mdl>> fillGateEntryDetail(String v_Type, String gateDocId, String restrictedDepartment, String subCode, String location, String billDate, String v_Date, String isSiteStateUT) {
    public DmResult<List<GrdGrnDetail_Mdl>> fillGateEntryDetail(String v_Type, String gateDocId, String partyCode, String location, String billDate, String v_Date, String isSiteStateUT) {
        DmResult<List<GrdGrnDetail_Mdl>> rstTemp = new DmResult<>();
        List<GrdGrnDetail_Mdl> lstTemp = new ArrayList<GrdGrnDetail_Mdl>();
        GrdGrnDetail_Mdl listNode;
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
                    + " , um.decimalPlaces AS purchaseUnitDecimalPlaces"
                    + " , unld.departmentCode, sgd.name as departmentName  "
                    + " , im.itsBasedOn, im.isBestBefore, im.subCode AS iSubCode\n"
                    + " , im.hsnCode AS masterHsnCode, hm.hsn masterHsn, im.itemGSTNature AS masterItemGSTNature \n"
                    + " , pod.hsnCode, hsn.hsn, pod.itemGSTNature, pod.hsnCode AS hsnCode, hsn.hsn \n"
                    + " , pod.oh_At_Discount, pod.oh_Amt_Discount"
                    + " , pod.isManualGstType, pod.gstTypeCode, it.name AS gstTypeName\n "
                    + " , pod.oh_Amt_Discount\n"
                    + " , pod.oh_at_Discount\n"
                    + " , pod.oh_at_SGST\n"
                    + " , pod.oh_at_UGST\n"
                    + " , pod.oh_at_CGST\n"
                    + " , pod.oh_at_IGST\n"
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
                listNode = new GrdGrnDetail_Mdl();

                //Unloading
                listNode.setUnloadingDocId(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setUnloadingV_Sno(DmShared.iNull(rsTemp.getString("v_SNo")).toString());

                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));

                listNode.setPoDocId((DmShared.iNull(rsTemp.getString("poDocId")).toString()));
                listNode.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                listNode.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                listNode.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //Item Detail
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                //IAP
                listNode.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());

                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
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

                    listNode.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_Discount"), "0.00"));
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
                        (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1), cnTemp);

                if (dmrIts.isStatus() && dmrIts.getData() != null) {
                    String taxPer;

                    if (DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 0) {
                        listNode.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                        listNode.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                    }

                    for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                        taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");

                        if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            listNode.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setSgstPer(DmServer.vNull(taxPer, "0.00"));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isSiteStateUT).intValue() == 1)) {
                            listNode.setSgstUgstPer(DmServer.vNull(taxPer, "0.00"));
                            listNode.setUgstPer(DmServer.vNull(taxPer, "0.00"));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                            listNode.setCgstPer(DmServer.vNull(taxPer, "0.00"));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                            listNode.setIgstPer(DmServer.vNull(taxPer, "0.00"));

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
                STApprovals.grnApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                STApprovals.grnApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
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

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    STApprovals.grnApproval(walkThroughEnum, keyCode, userName, cnTemp);
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

    private WalkThroughDefault_Mdl getWalkThroughFor(Grn_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

//        walkThroughDefault.setDepartmentCode(mrTemp.getDepartmentTag());
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
            // defaultValue.getLstSaleRateRule().addAll(STServer.getSaleRateRule(cnTemp));
            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));
            /*==============================<< Get default voucher_Type >>===============================*/
            defaultValue.setVoucherType(getVoucherType("", cnTemp));

            /*==============================<< Get default department >>===============================*/
            sqlQuery = "SELECT"
                    + "   dm.subCode AS code"
                    + " , sg.name"
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
            sqlQuery = "SELECT e.isIncludeTaxInCost,e.SinglePOFlowInPurchase,e.inv_DefaultPurchaseDepartment, \n"
                    + "sg.name as defaultPurchaseDepartmentName,e.isIncludeOverHeadsInCost,e.serviceGridActiveInGrn \n"
                    + "FROM enviro e\n"
                    + "left join subGroup sg on e.inv_DefaultPurchaseDepartment=sg.subCode \n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isIncludeTaxInCost") == null) {
                    throw new DmException(0, "Include Tax in Cost is not defined in Environment Setting.");
                }
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setIncludeOverHeadsInCost(DmShared.iNull(rsTemp.getString("isIncludeOverHeadsInCost")).intValue() == 1);
                defaultValue.setSinglePOFlowInPurchase(DmShared.iNull(rsTemp.getString("SinglePOFlowInPurchase")).intValue() == 1);
                defaultValue.setIsServiceGridActiveInGrn(DmShared.iNull(rsTemp.getString("serviceGridActiveInGrn")).intValue() == 1);

                defaultValue.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                defaultValue.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
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
    public DmResult<String> saveRecord(Grn_Mdl mrTempCurrent, Grn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "", roundOffItemGSTNature = "", roundOffGstTypeCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        String hisoryId = "";
        int sno = 0, vSNo = 0;
        try {
            Grn_Vld.chkSave(mrTempCurrent);
            ServiceGrid_Vld.chkSave(mrTempCurrent.getGrdServiceDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            DmServer.checkForwordVoucherDateTime(mrTempCurrent.getVoucherDate(), "");

            //=====================< AS Suggested By Dhawan Sir >============================================>
            //=====================< 15-Jun-2023 >===========================================================>
            cnTemp.setCatalog(DmServer.dbCompany);
            String fyEndDt = DmServer.executeScalar("select format(end_Dt,'dd-MMM-yyyy')  from company\n"
                    + "where v_Prefix = " + voucherPrefix, cnTemp);
            cnTemp.setCatalog(DmServer.getSsnDBName(this));
            if (DmShared.convertDate(mrTempCurrent.getVoucherDate()).getTime() == (DmShared.convertDate(fyEndDt).getTime())) {

            } else {
                if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
                    DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
                }
            }
//            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
//                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
//            }
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTempCurrent.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTempCurrent.setRecIdGRN(DmServer.getRecId("pChallan", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdGRN(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTempCurrent.setSharedKeyFieldName(DmShared.xNull(mrTempCurrent.getRecIdGRN().getRecIdValue()));
                mrTempCurrent.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTempCurrent), DmServer.getSsnUserName(this), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), mrTempCurrent.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("pChallan", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdGRN(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Check for Approvedby if already approved >>====================================*/
            sqlQuery = "select approvedBy from pChallan where  docId = :docId ";
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
            STServer.chkIfDocumentUsed("pcDocId", "pBillDetail", mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="Check gate">
            if (DmShared.iNull(mrTempCurrent.getGateNoTag()).longValue() > 0) {
                /*==============================<< Check gate >>============================*/
                sqlQuery = "SELECT Count(*) cnt "
                        + "		FROM pBill pb "
                        + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                        + "		WHERE pb.gateDocId IS NOT NULL "
                        + "		AND vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                        + "		AND pb.gateDocId = :gateDocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
                        + "		AND pc.docId <> :docId "
                        + "		AND pc.gateDocId = :gateDocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("gateDocId", DmShared.xNull(mrTempCurrent.getGateNoTag()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        throw new DmException(0, "Gate has been used in GRN.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            //</editor-fold>

            /*==============================<< Check for LatestEntry >>====================================*/
            sqlQuery = "SELECT u_EntDt_LatestLine FROM pChallan WHERE docId = :docId";
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

            //*==============================<< Check for duplicate party bill no >>====================================*/
            sqlQuery = " SELECT COUNT(pc.docid) AS cnt "
                    + " FROM pChallan pc with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pc.v_Type "
                    + " WHERE  pc.pChallanNo = :pChallanNo "
                    + " AND pc.subCode=:subCode "
                    + " AND pc.v_Prefix = :v_Prefix "
                    + " AND pc.docId != :docId ";
//                    + " AND vt.category IN ('" + pvc.PURCHASE_CHALLAN + "','" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Entry already exists with this Reference Number.");
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
                    roundOffItemGSTNature = DmShared.xNull(rsTemp.getString("itemGstNature"));
                    roundOffGstTypeCode = DmShared.xNull(rsTemp.getString("gstTypeCode"));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.xNull(roundOffItemCode).trim().equals("")) {
                    throw new DmException(0, "Round Off Item not defined in Overhead Master.");
                }

            }


            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert / Update record into pChallan >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pChallan ( \n"
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, \n"
                        + "     pChallanNo, pChallanDate, subCode, consignee, partyType, gsTin, gstinDate, \n"
                        + "     rcmApplicable, placeOfSupply, remark, wtaCode, guiConfigCode, \n"
                        + "     site_Code, preparedBy, preparedDate, u_EntDt_LatestLine, transferDt, \n"
                        + "     gateDocId, transporterCode, EWayBillNo, EWayBillDt, LRNo, LRDate, \n"
                        + "     oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                        + "     oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                        + "     oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Total, \n"
                        + "     oh_Amt_RoundOff, oh_Amt_Net, oh_Amt_LandedValue, locationCode, \n"
                        + "     oh_Ac_FinalDiscount, oh_Ac_RoundOff, balancingAc) \n"
                        + " VALUES ( \n"
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, \n"
                        + "     :pChallanNo, :pChallanDate, :subCode, :consignee, :partyType, :gsTin, :gstinDate, \n"
                        + "     :rcmApplicable, :placeOfSupply, :remark, :wtaCode, :guiConfigCode, \n"
                        + "     :site_Code, :preparedBy, :preparedDate, :uEntDt, :transferDt, \n"
                        + "     :gateDocId, :transporterCode, :EWayBillNo, :EWayBillDt, :LRNo, :LRDate, \n"
                        + "     :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                        + "     :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                        + "     :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Total, \n"
                        + "     :oh_Amt_RoundOff, :oh_Amt_Net, :oh_Amt_LandedValue, :locationCode, "
                        + "     :oh_Ac_FinalDiscount, :oh_Ac_RoundOff, :balancingAc)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTempCurrent.getRecIdGRN().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTempCurrent.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTempCurrent.getRecIdGRN().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
                psTemp.setDate("pChallanDate", DmServer.convertDate(mrTempCurrent.getChallanDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTempCurrent.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTempCurrent.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTempCurrent.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("LRNo", DmShared.xNull(mrTempCurrent.getLrNo()));
                psTemp.setDate("LRDate", DmServer.convertDate(mrTempCurrent.getLrDate()));
                psTemp.setString("gateDocId", DmShared.foriegnKey(mrTempCurrent.getGateNoTag()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
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
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTempCurrent.getBalancingAc()));
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "UPDATE pChallan SET \n"
                        + "  v_Date = :v_Date \n"
                        + " , pChallanNo = :pChallanNo \n"
                        + " , pChallanDate = :pChallanDate \n"
                        + " , subCode = :subCode \n"
                        + " , consignee = :consignee \n"
                        + " , partyType = :partyType \n"
                        + " , gsTin = :gsTin\n"
                        + " , gstinDate = :gstinDate\n"
                        + " , rcmApplicable = :rcmApplicable \n"
                        + " , placeOfSupply = :placeOfSupply \n"
                        + " , remark = :remark \n"
                        + " , u_EntDt = :uEntDt \n"
                        + " , u_EntDt_LatestLine = :uEntDt \n"
                        + " , u_Name = :u_Name \n"
                        + " , transferDt = :transferDt \n"
                        + " , EWayBillNo = :EWayBillNo \n"
                        + " , EWayBillDt = :EWayBillDt \n"
                        + " , LRNo = :LRNo \n"
                        + " , LRDate = :LRDate \n"
                        + " , gateDocId = :gateDocId \n"
                        + " , transporterCode = :transporterCode \n"
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
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
                psTemp.setDate("pChallanDate", DmServer.convertDate(mrTempCurrent.getChallanDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTempCurrent.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("LRNo", DmShared.xNull(mrTempCurrent.getLrNo()));
                psTemp.setDate("LRDate", DmServer.convertDate(mrTempCurrent.getLrDate()));
                psTemp.setString("gateDocId", DmShared.foriegnKey(mrTempCurrent.getGateNoTag()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
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
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTempCurrent.getBalancingAc()));
                psTemp.setString("transferDt", null);
            }
            psTemp.executeUpdate();
            psTemp.close();

            //====================================Re-check-PartyBillNo=================================
            //*==============================<< Check for duplicate party bill no >>====================================*/
            sqlQuery = " SELECT COUNT(pc.docid) AS cnt "
                    + " FROM pChallan pc with (nolock)"
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pc.v_Type "
                    + " WHERE  pc.pChallanNo = :pChallanNo "
                    + " AND pc.subCode=:subCode "
                    + " AND pc.v_Prefix = :v_Prefix "
                    + " AND pc.docId != :docId "
                    + " AND vt.category IN ('" + pvc.PURCHASE_CHALLAN + "','" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
            psTemp.setString("v_Prefix", voucherPrefix);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Entry already exists with this Reference Number.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), true, cnTemp);

            /*==============================<< Delete records from pChallanImpurityDetail >>====================================*/
            sqlQuery = " DELETE pChallanImpurityDetail"
                    + " FROM pChallanImpurityDetail AS pid"
                    + " INNER JOIN pChallanDetail pcd ON pid.docId = pcd.docId AND pid.v_Sno = pcd.v_Sno "
                    + " WHERE pcd.docId = :docId AND pcd.isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from pChallanDetail >>====================================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pChallanDetail", "ibCode", " AND docid = '" + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "'", cnTemp);
            sqlQuery = "Delete From pChallanDetail Where docId = :docId AND isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Get itemUpdateHistory ID >>====================================*/
            hisoryId = DmServer.getCodeViaManualCode(mrTempCurrent.getSharedKeyFieldCode(), "docId", "itemUpdateHistory", "(ISNULL(MAX(id),0) + 1)", cnTemp);

            /*==============================<< Insert records into pChallanDetail >>====================================*/
            List<GrdGrnDetail_Mdl> listDetail = mrTempCurrent.getGrdGrnDetail();
            vSNo = 0;
            for (GrdGrnDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    if (!detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                            detail_Mdl.setvSNo(String.valueOf(vSNo));
                            sqlQuery = "INSERT INTO pChallanDetail ("
                                    + "     docId, v_SNo, poDocId, poV_SNo, itemCode, departmentCode, hsnCode, itemGSTNature, batchNo, lotNo, manufacturingDate, expiryDate, colourCode,\n"
                                    + "     sizeCode,itemMakeCode, mrp, skuMrp, unitCode, challanQty, recQty, rejQty, acceptQty, skuDifference, skuQty, rate,\n"
                                    + "     landedSkuValue, landedSkuRate, skuQtyForRate, skuRate, remark, isStatus,\n"
                                    + "     labRejQty ,qcRejQty, drCrNoteAmt, unloadingDocId, unloadingV_Sno, shortageQty\n"
                                    + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                    + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                    + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Net\n"
                                    + "     , gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                    + " Values (\n"
                                    + "     :docId, :v_SNo, :poDocId, :poV_SNo, :itemCode, :departmentCode, :hsnCode, :itemGSTNature, :batchNo, :lotNo, :manufacturingDate, :expiryDate, :colourCode,\n"
                                    + "     :sizeCode,:itemMakeCode, :mrp, :skuMrp, :unitCode, :challanQty, :recQty, :rejQty, :acceptQty, :skuDifference, :skuQty, :rate,\n"
                                    + "     :landedSkuValue, :landedSkuRate, :skuQtyForRate, :skuRate, :remark, 0,\n"
                                    + "     :labRejQty , :qcRejQty, :drCrNoteAmt, :unloadingDocId, :unloadingV_Sno, :shortageQty \n"
                                    + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                    + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                    + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Net\n"
                                    + "     , :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                            psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
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
                            psTemp.setString("challanQty", DmServer.vNull(detail_Mdl.getChallanQty()));
                            psTemp.setString("recQty", DmServer.vNull(detail_Mdl.getRecQty()));
                            psTemp.setString("shortageQty", DmServer.vNull(detail_Mdl.getShortageQty()));
                            psTemp.setString("rejQty", DmServer.vNull(detail_Mdl.getRejQty()));
                            psTemp.setString("labRejQty", DmServer.vNull(detail_Mdl.getLabRejectedQty()));
                            psTemp.setString("qcRejQty", DmServer.vNull(detail_Mdl.getQcRejectedQty()));
                            psTemp.setString("drCrNoteAmt", DmServer.vNull(detail_Mdl.getDrCrNoteAmt(), "0.00"));
                            psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                            psTemp.setString("skuDifference", DmServer.vNull(detail_Mdl.getSkuDifferenceQty()));
                            psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                            psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                            psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
                            psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                            psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                            psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                            psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
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
                            psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail_Mdl.getGstTypeCode()));
                            psTemp.setString("isManualGstType", detail_Mdl.isManualGstType() ? "1" : "0");
                            psTemp.setString("isManualTax", detail_Mdl.isManualTax() ? "1" : "0");
                            psTemp.setString("oh_Ac_Item", DmShared.foriegnKey(detail_Mdl.getIsubCode()));
                            psTemp.executeUpdate();
                            psTemp.close();

                            /*==============================<< Insert records into pChallanImpurityDetail >>====================================*/
                            sno = 0;
                            for (GrdImpurity_Mdl grdIm : detail_Mdl.getLstImpurity()) {
                                if (DmShared.iNull(grdIm.getSharedKeyFieldCode()).longValue() != 0 && DmShared.vNull(grdIm.getNetQty()).doubleValue() > 0) {
                                    sqlQuery = "INSERT INTO pChallanImpurityDetail ("
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
                                    sqlQuery = "INSERT INTO pChallanImpurityDetail ( "
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
                                    psTemp.setString("range1", DmShared.xNull(grdLab.getRange1()));
                                    psTemp.setString("range2", DmShared.xNull(grdLab.getRange2()));
                                    psTemp.setString("foundValue", DmShared.xNull(grdLab.getMainFoundValue()));
                                    psTemp.setString("netQty", DmServer.vNull(grdLab.getLabQty()));
                                    psTemp.setString("amount", DmShared.vNull(grdLab.getLabAmt()).toString());
                                    psTemp.setString("nature", DmShared.iNull(ImpurityNature.LAB.getId()).toString());
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                            /*==========================<< **** >>===========================*/
 /*==============================<< Update Item Master >>====================================*/
                            if (DmShared.iNull(detail_Mdl.getLstItemUpdateFields().size()).intValue() > 0) {
                                ItemUpdate_Mdl iuMdl = new ItemUpdate_Mdl();
                                iuMdl.setId(hisoryId);
                                iuMdl.setDocId(mrTempCurrent.getSharedKeyFieldCode());
                                iuMdl.setV_SNo(detail_Mdl.getvSNo());
                                iuMdl.setV_Type(mrTempCurrent.getVoucherTypeCode());
                                iuMdl.setV_No(DmShared.iNull(mrTempCurrent.getRecIdGRN().getCounter()).toString());
                                iuMdl.setV_Date(mrTempCurrent.getVoucherDate());
                                iuMdl.setRecId(mrTempCurrent.getSharedKeyFieldName());
                                iuMdl.setSubcode(mrTempCurrent.getSupplierTag());
                                iuMdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                                iuMdl.setUnitCode(detail_Mdl.getPurchaseUnitCode());
                                iuMdl.setLstItemUpdateFields(detail_Mdl.getLstItemUpdateFields());
                                ItemUpdateServer.updateItem(iuMdl, cnTemp, this);
                            }
                            /*==========================<< **** >>===========================*/
                        }
                    }
                }
            }

            //========================================(Service Grid Modal)==========================================================
            for (GrdGrnDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                    if (detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pChallanDetail (\n"
                                + "     docId, v_SNo, itemCode, hsnCode, challanQty, acceptQty, rate, itemGSTNature ,unitCode ,isFooter\n"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net\n"
                                + "     , serviceEvalTypeCode, serviceEvalRate, gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                + " Values (\n"
                                + "     :docId, :v_SNo, :itemCode, :hsnCode, :challanQty, :acceptQty, :rate, :itemGSTNature ,:unitCode, :isFooter"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :serviceEvalTypeCode, :serviceEvalRate, :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("challanQty", DmServer.vNull(detail_Mdl.getChallanQty()));
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
            /*==============================<< Insert records into stock >>====================================*/
            ItemBatch_Server.compareProperties(iniItemBatchProperties(mrTempCurrent, mrTempPrevious, false), cnTemp, this);

            /*==============================<< Update ibCode From Stock Table >>====================================*/
            sqlQuery = "UPDATE pChallanDetail"
                    + " SET pChallanDetail.ibCode = stock.ibCode"
                    + " FROM stock"
                    + " WHERE stock.docid = pChallanDetail.docId"
                    + " AND stock.v_SNo = pChallanDetail.v_SNo"
                    + " AND pChallanDetail.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), false, cnTemp);


            /*==============================<< Insert Round Off Line >>====================================*/
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) != 0) {
                    sqlQuery = "INSERT INTO pChallanDetail ("
                            + "     docId, v_SNo, itemCode, hsnCode, challanQty, acceptQty, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter"
                            + "     , oh_Amt_SubTotal, itemGSTNature, gstTypeCode)"
                            + " Values ("
                            + "     :docId, :v_SNo, :itemCode, :hsnCode, :challanQty, :acceptQty, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, :isFooter"
                            + "     , :oh_Amt_SubTotal, :itemGSTNature, :gstTypeCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", String.valueOf(++vSNo));
                    psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                    psTemp.setString("itemGSTNature", DmShared.foriegnKey(roundOffItemGSTNature));
                    psTemp.setString("gstTypeCode", DmShared.foriegnKey(roundOffGstTypeCode));
                    if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) < 0) {
                        psTemp.setString("challanQty", "-1");
                        psTemp.setString("acceptQty", "-1");
                    } else {
                        psTemp.setString("challanQty", "1");
                        psTemp.setString("acceptQty", "1");
                    }
                    psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTempCurrent.getRoundOffAmount())), "0.00"));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("isFooter", "1");
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*==============================<< Account Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);
            if (mrTempCurrent.isInventoryAccountingActive()) {
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isIncludeTaxInCost(), mrTempCurrent.getDefaultValueMdl().isIncludeOverHeadsInCost(), cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdGRN(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", "", "", "", "", false, true, cnTemp);
            }

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                STApprovals.grnApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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

            dmrTemp.setData(mrTempCurrent.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

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
    public DmResult<Grn_Mdl> getMoveRec(String strKey) {
        DmResult<Grn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery, labDocId = "";
        Grn_Mdl mrTemp = new Grn_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdGrnDetail_Mdl> lstNode = new ArrayList<GrdGrnDetail_Mdl>();
        GrdGrnDetail_Mdl node;
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
            sqlQuery = "SELECT pc.docId "
                    + "      ,pc.v_Type "
                    + "      ,pc.v_No "
                    + "      ,pc.recId "
                    + "      ,pc.v_Date "
                    + "      ,pc.subCode "
                    + "      ,pc.placeOfSupply "
                    + "      ,pc.remark "
                    + "      ,pc.pChallanNo "
                    + "      ,pc.pChallanDate "
                    + "      ,pc.u_EntDt "
                    + "      ,pc.u_Name "
                    + "      ,pc.preparedBy "
                    + "      ,pc.approvedBy "
                    + "      ,pc.wtaCode "
                    + "      ,pc.approvalDateTime "
                    + "      ,pc.recIdPrefix "
                    + "      ,pc.rcmApplicable "
                    + "      ,pc.partyType "
                    + "      ,pc.u_EntDt_LatestLine "
                    + "      ,pc.guiConfigCode "
                    + "      ,pc.gateDocId "
                    + "      ,gio.recId as gateRecId "
                    + "      ,pc.transporterCode "
                    + "      ,sg1.name as transporterName "
                    + "      ,pc.EWayBillNo "
                    + "      ,pc.EWayBillDt "
                    + "      ,pc.LRNo "
                    + "      ,pc.LRDate "
                    + "      ,pc.preparedDate "
                    + "     , vt.description AS voucherTypeName "
                    + "     , vt.category "
                    + "     , sgs.name AS supplierName "
                    + "     , c.stateCode AS supplierStateCode "
                    + "     , sgs.partyType AS supplierPartyType "
                    + "     , pc.gstin AS supplierGstin "
                    + "     , pc.gstinDate AS supplierGstinDate"
                    + "     , pc.oh_Amt_GrossGoods\n"
                    + "     , pc.oh_Amt_GrossService\n"
                    + "     , pc.oh_Amt_Gross\n"
                    + "     , pc.oh_Amt_Discount\n"
                    + "     , pc.oh_Amt_TaxableGoods\n"
                    + "     , pc.oh_Amt_TaxableService\n"
                    + "     , pc.oh_Amt_Taxable\n"
                    + "     , pc.oh_Amt_SGST\n"
                    + "     , pc.oh_Amt_UGST\n"
                    + "     , pc.oh_Amt_CGST\n"
                    + "     , pc.oh_Amt_IGST\n"
                    + "     , pc.oh_Amt_SubTotal\n"
                    + "     , pc.oh_at_FinalDiscount\n"
                    + "     , pc.oh_Amt_FinalDiscount\n"
                    + "     , pc.oh_Amt_Total\n"
                    + "     , pc.oh_Amt_RoundOff\n"
                    + "     , pc.oh_Amt_Net\n"
                    + "     , pc.oh_Amt_LandedValue\n"
                    + "     , pc.locationCode\n"
                    + "     , pc.oh_Ac_FinalDiscount\n"
                    + "     , pc.oh_Ac_RoundOff\n"
                    + "     , pc.balancingAc\n"
                    + " FROM pChallan pc "
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type "
                    + " LEFT JOIN subGroup sgs ON pc.subcode = sgs.subCode "
                    + " LEFT JOIN subGroup sg1 ON sg1.subCode = pc.transporterCode "
                    + " LEFT JOIN city c ON c.cityCode = sgs.cityCode "
                    + " LEFT JOIN gateInOut gio ON gio.docId = pc.gateDocId "
                    + " WHERE pc.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdGRN().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdGRN().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdGRN().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                mrTemp.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));

                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierPartyType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());

                mrTemp.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));

                mrTemp.setGateNoTag(DmShared.xNull(rsTemp.getString("gateDocId")));
                mrTemp.setGateNoText(DmShared.xNull(rsTemp.getString("gateRecId")));
                mrTemp.setTransporterCode(DmShared.xNull(rsTemp.getString("transporterCode")));
                mrTemp.setTransporterName(DmShared.xNull(rsTemp.getString("transporterName")));
                mrTemp.setEwayNo(DmShared.xNull(rsTemp.getString("EWayBillNo")));
                mrTemp.setEwayDate(DmShared.dNull(rsTemp.getDate("EWayBillDt")));
                mrTemp.setLrNo(DmShared.xNull(rsTemp.getString("LRNo")));
                mrTemp.setLrDate(DmShared.dNull(rsTemp.getDate("LRDate")));

                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));

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

                //Users
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }

                mrTemp.setSharedPreparedBy(STServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(STServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(STServer.getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "  pcd.v_SNo\n"
                    + " ,pcd.poDocId\n"
                    + " ,pcd.poV_SNo\n"
                    + " ,pcd.itemCode\n"
                    + " ,pcd.departmentCode\n"
                    + " ,pcd.unitCode\n"
                    + " ,pcd.hsnCode\n"
                    + " ,hsn.hsn\n"
                    + " ,pcd.challanQty\n"
                    + " ,pcd.recQty\n"
                    + " ,pcd.shortageQty\n"
                    + " ,pcd.rejQty\n"
                    + " ,pcd.acceptQty\n"
                    + " ,pcd.skuQty\n"
                    + " ,pcd.billedQty\n"
                    + " ,pcd.rate\n"
                    + " ,pcd.remark\n"
                    + " ,pcd.mrp\n"
                    + " ,pcd.skuMrp\n"
                    + " ,pcd.batchNo\n"
                    + " ,pcd.lotNo\n"
                    + " ,pcd.manufacturingDate\n"
                    + " ,pcd.expiryDate\n"
                    + " ,pcd.colourCode\n"
                    + " ,pcd.sizeCode\n"
                    + " ,pcd.itemMakeCode\n"
                    + " ,pcd.skuDifference\n"
                    + " ,pcd.skuRate\n"
                    + " ,pcd.landedSkuValue\n"
                    + " ,pcd.landedSkuRate\n"
                    + " ,pcd.itemGSTNature\n"
                    + " ,pcd.isStatus\n"
                    + " ,pcd.ibCode\n"
                    + " ,pcd.skuQtyForRate\n"
                    + " , po.recId AS poRecId\n"
                    + " , po.refNo AS poRefNo\n"
                    + " , sgd.name AS departmentName\n"
                    + " , im.name AS itemName\n"
                    + " , im.propertyList\n"
                    + " , im.itsBasedOn\n"
                    + " , im.isBestBefore\n"
                    + " , im.isLabRequired\n"
                    + " , im.sku AS skuCode, im.purchaseRate \n"
                    + " , ump.unit AS skuName\n"
                    + " , ump.decimalPlaces AS skuAllowDecimalPlaces\n"
                    + " , ums.unit AS unitName\n"
                    + " , ums.decimalPlaces AS unitAllowDecimalPlaces\n"
                    + " , icm.name AS colourName\n"
                    + " , ism.name AS sizeName\n"
                    + " , imm.companyName as itemMakeName\n"
                    + " , pcd.itemMakeCode \n"
                    + " , pcd.labRejQty\n"
                    + " , pcd.qcRejQty\n"
                    + " , pcd.drCrNoteAmt\n"
                    + " , pcd.unloadingDocId \n"
                    + " , pcd.unloadingV_Sno \n"
                    + " , DATEDIFF(day, pcd.manufacturingDate, pcd.expiryDate) AS bestBeforeDays\n"
                    + " , ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) + ISNULL(pcd.acceptQty, 0) AS pendingQty\n"
                    + " , lab.docId as labDocId\n"
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
                    + " , pcd.oh_Amt_SubTotal\n"
                    + " , pcd.oh_at_FinalDiscount\n"
                    + " , pcd.oh_Amt_FinalDiscount\n"
                    + " , pcd.oh_Amt_Net\n"
                    + " , pcd.oh_Ac_Item\n"
                    + " FROM pChallanDetail pcd\n"
                    + " LEFT JOIN pOrder po ON pcd.poDocId = po.docId\n"
                    + " LEFT JOIN pOrderDetail pod ON pcd.poDocId = pod.docId AND pcd.poV_SNo = pod.v_SNo\n"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                    + " LEFT JOIN unitMast ump ON im.sku = ump.code\n"
                    + " LEFT JOIN unitMast ums ON pcd.unitCode = ums.code\n"
                    + " LEFT JOIN itemColourMast icm ON pcd.colourCode = icm.code\n"
                    + " LEFT JOIN itemSizeMast ism ON pcd.sizeCode = ism.code\n"
                    + " LEFT JOIN itemMakemast imm ON imm.code=pcd.itemMakeCode\n"
                    + " LEFT JOIN subGroup sgd ON pcd.departmentCode = sgd.subCode\n"
                    + " LEFT JOIN labSample ls ON ls.refDocId = pcd.unloadingDocId AND ls.refV_Sno = pcd.unloadingV_Sno \n"
                    + " LEFT JOIN lab lab ON lab.labSampleDocId = ls.docId \n"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pcd.hsnCode \n"
                    + " WHERE pcd.docId = :docId\n"
                    + " AND ISNULL(pcd.isFooter, 0) = 0\n"
                    + " ORDER BY pcd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGrnDetail_Mdl();

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
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setIsLabRequired(DmShared.xNull(rsTemp.getString("isLabRequired")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuAllowDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("unitAllowDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
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
                ipm.setItemName(node.getItemMakeName());
                ipm.setMrp(node.getMrp());
                ipm.setMrp(node.getSkuMrp());
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
                node.setChallanQty(DmServer.vNull(rsTemp.getString("challanQty")));
                node.setRecQty(DmServer.vNull(rsTemp.getString("recQty")));
                node.setShortageQty(DmServer.vNull(rsTemp.getString("shortageQty")));
                node.setRejQty(DmServer.vNull(rsTemp.getString("rejQty")));

                node.setLabRejectedQty(DmServer.vNull(rsTemp.getString("labRejQty")));
                node.setQcRejectedQty(DmServer.vNull(rsTemp.getString("qcRejQty")));
                node.setDrCrNoteAmt(DmServer.vNull(rsTemp.getString("drCrNoteAmt"), "0.00"));

                node.setSkuDifferenceQty(DmServer.vNull(rsTemp.getString("skuDifference")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setBilledQty(DmServer.vNull(rsTemp.getString("billedQty")));

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

                //Other
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());

                /*==============================<< Get Impurity detail >>========================*/
                sqlQuery = " SELECT im.name AS impurityName, pcid.impurityCode, pcid.foundValue, pcid.acceptPer, "
                        + " pcid.netPer, pcid.netQty, pcid.supplierQty "
                        + " FROM pChallanImpurityDetail pcid "
                        + " LEFT JOIN impurityMast im ON im.code = pcid.impurityCode "
                        + " WHERE pcid.docId =:docId "
                        + " AND pcid.v_SNo =:v_SNo "
                        + " AND pcid.nature = :nature "
                        + " UNION ALL "
                        + " select im.name AS impurityName, iid.impurityCode, null as foundValue, null as acceptPer, "
                        + " null as netPer, null as netQty, null as supplierQty "
                        + " from itemImpurityDetail iid "
                        + " LEFT JOIN itemImpurityMast iim ON iim.code = iid.code "
                        + " LEFT JOIN impurityMast im ON im.code = iid.impurityCode "
                        + " LEFT JOIN ( "
                        + "			select pcid.impurityCode "
                        + "			from pChallanImpurityDetail pcid "
                        + "			where pcid.docId =:docId AND pcid.v_SNo =:v_SNo "
                        + "                     AND pcid.nature = :nature "
                        + "		  ) as vPcid ON vPcid.impurityCode = iid.impurityCode "
                        + " where vPcid.impurityCode IS NULL "
                        + " AND iim.testTemplateCode IS NULL "
                        + " AND iim.itemCode =:itemCode ";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("docId", DmShared.xNull(strKey));
                psTempInner.setString("v_SNo", DmShared.xNull(node.getvSNo()));
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
                sqlQuery = " select pcd.impurityCode, im.name as impurityName, pcd.range1, pcd.range2 "
                        + " , pcd.foundValue, pcd.netQty, pcd.amount, pcd.nature, im.valueType, um.unit, im.manualCode"
                        + " , ld.formulaForQuantity, ld.formulaForAmount "
                        + " from pChallanImpurityDetail pcd "
                        + " LEFT JOIN impurityMast im ON im.code = pcd.impurityCode "
                        + " LEFT JOIN unitMast um ON um.code = im.unitCode "
                        + " LEFT JOIN labDetail ld ON ld.impurityCode = pcd.impurityCode AND ld.docId = :labDocId "
                        + " WHERE pcd.docId = :docId "
                        + " AND pcd.v_SNo = :v_SNo "
                        + " AND pcd.nature = :nature "
                        + " ORDER BY pcd.sno ";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("docId", DmShared.xNull(strKey));
                psTempInner.setString("v_SNo", DmShared.xNull(node.getvSNo()));
                psTempInner.setString("nature", DmShared.iNull(ImpurityNature.LAB.getId()).toString());
                psTempInner.setString("labDocId", DmShared.iNull(labDocId).toString());
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

                //=================== <<check is lab exist or not >> ===================//                
                if (node.getLstLabEntry().size() > 0) {
                    if (DmShared.iNull(node.getLstLabEntry().get(0).getSharedKeyFieldCode()).intValue() > 0) {
                        node.setLabStatus(true);
                    }
                }
                //=================== <<*****>> ===================//

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdGrnDetail().addAll(lstNode);

            /*==============================<< Get Service detail >>===============================*/
            sqlQuery = "SELECT\n"
                    + "  pcd.v_SNo\n"
                    + " , pcd.itemCode\n"
                    + " , pcd.hsnCode\n"
                    + " , hsn.hsn\n"
                    + " , pcd.itemGSTNature\n"
                    + " , pcd.isStatus\n"
                    + " , im.name AS itemName\n"
                    + " , im.smfCode\n"
                    + " , im.itsBasedOn\n"
                    + " , pcd.unitCode \n"
                    + " , pcd.challanQty\n"
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
                    + " , pcd.oh_Ac_Item\n"
                    + " , pcd.serviceEvalTypeCode\n"
                    + " , pcd.serviceEvalRate\n"
                    + " FROM pChallanDetail pcd\n"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code\n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pcd.hsnCode \n"
                    + " WHERE pcd.docId = :docId\n"
                    + " AND ISNULL(pcd.isFooter, 0) = 2\n"
                    + " ORDER BY pcd.v_SNo\n";
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
                serviceDetail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("challanQty")));
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

                mrTemp.getGrdServiceDetail().add(serviceDetail_Mdl);
            }

            /*==============================<< Get GUI >>===============================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));
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
            sqlQuery = " select approvedBy, wtaCode from pChallan where docId = :docId ";
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
            STServer.chkIfDocumentUsed("pcDocId", "pBillDetail", strKey, cnTemp);

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason & Walkthrough >>============================*/
            OverHeadLine_Server.delete(strKey, cnTemp);
            OverHead_Server.delete(strKey, cnTemp);
            ModifyReason_Server.delete(strKey, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);

            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(strKey, cnTemp);

            /*==============================<< Manage Batch Detail >>====================================*/
            Stock_Server.deleteStock(strKey, cnTemp);

            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(strKey, true, cnTemp);

            /*==============================<< Delete records from pChallanImpurityDetail >>====================================*/
            sqlQuery = "Delete From pChallanImpurityDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pChallanDetail >>===============================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pChallanDetail", "ibCode", " AND docid = '" + DmShared.iNull(strKey).toString() + "'", cnTemp);
            sqlQuery = " Delete From pChallanDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pChallan >>===============================*/
            sqlQuery = " Delete From pChallan Where docId= :docId ";
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
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select pc.docId "
                    + "         from pChallan as pc "
                    + "         left join voucher_Type as vt on pc.v_Type = vt.v_Type  "
                    + "         where vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "','" + pvc.SERVICE_CHALLAN + "', '" + pvc.SERVICE_CHALLAN_UNREGISTERED + "')"
                    + "         and pc.site_Code = :siteCode and pc.v_prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += "and " + condition;
            }
            sqlQuery += " order by pc.v_Date desc, vt.short_Name, pc.v_No desc, pc.recId  ";
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

            sqlQuery = " select	pc.docId, pc.recId, pc.v_Date, pc.subcode, sgs.name as supName, pc.pChallanNo, pc.pChallanDate, "
                    + "	        vt.short_Name, pc.v_No "
                    + "		from pChallan as pc "
                    + "		left join voucher_Type as vt on pc.v_Type = vt.v_Type "
                    + "         left join subGroup as sgs on pc.subcode = sgs.subCode"
                    + "		where vt.category IN ('" + pvc.PURCHASE_CHALLAN + "', '" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "','" + pvc.SERVICE_CHALLAN + "', '" + pvc.SERVICE_CHALLAN_UNREGISTERED + "')"
                    + "         and pc.site_Code = :site_Code and pc.v_Prefix=:vPrefix";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += "and " + condition;
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
                listNode.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
                listNode.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
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
    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst) {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            dmrTemp = STServer.getGSTS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, ITSType.PURCHASE, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
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

    private CompareProperties_Mdl iniItemBatchProperties(Grn_Mdl mrTempCurrent, Grn_Mdl mrTempPrevious, boolean isDelete) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        if (!isDelete) {
            lstItemBatch_MdlCurnt.clear();
            for (GrdGrnDetail_Mdl detail_MdlCurrent : mrTempCurrent.getGrdGrnDetail()) {
                ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() > 0) {
                    if (!detail_MdlCurrent.isServiceRecord()) {
                        batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                        batch_Mdl.getIpm().setBatchNo(DmShared.xNull(detail_MdlCurrent.getBatchNo()));
                        batch_Mdl.getIpm().setLotNo(DmShared.xNull(detail_MdlCurrent.getLotNo()));
                        batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(detail_MdlCurrent.getExpiryDate()));
                        batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlCurrent.getManufacturingDate()));
                        batch_Mdl.getIpm().setMrp(DmShared.xNull(detail_MdlCurrent.getMrp()));
                        batch_Mdl.getIpm().setSkuMrp(DmServer.vNull(detail_MdlCurrent.getSkuMrp()));
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
                        batch_Mdl.setStockRefChallanNo(DmShared.xNull(mrTempCurrent.getChallanNo()));
                        batch_Mdl.setStockRefChallanDate(DmShared.dNull(mrTempCurrent.getChallanDate()));
                        batch_Mdl.setStockDepartmentCode(DmShared.xNull(detail_MdlCurrent.getDepartmentCode()));

                        lstItemBatch_MdlCurnt.add(batch_Mdl);
                    }
                }
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdGrnDetail_Mdl detail_MdlPrev : mrTempPrevious.getGrdGrnDetail()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() > 0) {
                    if (!detail_MdlPrev.isServiceRecord()) {

                        batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                        batch_MdlPrev.getIpm().setBatchNo(DmShared.xNull(detail_MdlPrev.getBatchNo()));
                        batch_MdlPrev.getIpm().setLotNo(DmShared.xNull(detail_MdlPrev.getLotNo()));
                        batch_MdlPrev.getIpm().setExpiryDate(DmShared.dNull(detail_MdlPrev.getExpiryDate()));
                        batch_MdlPrev.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlPrev.getManufacturingDate()));
                        batch_MdlPrev.getIpm().setMrp(DmShared.xNull(detail_MdlPrev.getMrp()));
                        batch_MdlPrev.getIpm().setSkuMrp(DmServer.vNull(detail_MdlPrev.getSkuMrp()));
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
                        batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(mrTempPrevious.getChallanNo()));
                        batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(mrTempPrevious.getChallanDate()));
                        batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(detail_MdlPrev.getDepartmentCode()));

                        lstItemBatch_MdlPrev.add(batch_MdlPrev);
                    }
                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdGRN().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdGRN().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierTag()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getRecIdGRN().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getRecIdGRN().getCounter().toString()));
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
            sqlQuery = "SELECT po.docId AS poDocId, pcd.grnQty, pod.quantity AS poQty, pod.challanedQty, pod.isStatus, po.approvedBy,"
                    + "	pod.tl_ExcessQty, pod.tl_ShortageQty, po.recId AS poNo, vt.short_name AS poType, pod.itemCode, pod.v_Sno AS poV_SNo"
                    + "	FROM pOrderDetail pod"
                    + "	LEFT JOIN pOrder po ON po.docId = pod.docId"
                    + "	LEFT JOIN voucher_type vt ON vt.v_type = po.v_Type"
                    + "	INNER JOIN ("
                    + "     SELECT pcd.poDocId, pcd.poV_SNo, sum(pcd.acceptQty) as grnQty"
                    + "     FROM pChallanDetail pcd"
                    + "     WHERE pcd.docId = :docId"
                    + "     GROUP BY pcd.poDocId, pcd.poV_SNo"
                    + "	) pcd ON pod.docId = pcd.poDocId AND pod.v_SNo = pcd.poV_SNo";
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

                sqlQuery = "UPDATE pOrderDetail SET"
                        + "   isStatus = :isStatus"
                        + " , challanedQty = :challanedQty"
                        + " WHERE docId = :docId"
                        + " AND v_SNo = :v_SNo";
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
                sqlQuery = "UPDATE pOrder SET u_EntDt_LatestLine = :uEntDt WHERE docId IN (" + poDocId + ") ";
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
    public DmResult<TxtHSupplier_Mdl> getSupplierDetails(String supplierCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<TxtHSupplier_Mdl> rstTemp = new DmResult();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl mrTemp = new TxtHSupplier_Mdl();
        Connection cnTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSupplier(supplierCode, voucherCategory, flagFilterByVoucherCategory, v_Type, helper, blnToEndOfFile, cnTemp);
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

    private List<TxtHSupplier_Mdl> getSupplier(String supplierCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        String condition = "", filtration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (!DmShared.xNull(supplierCode).equalsIgnoreCase("")) {
                condition += " AND sg.subCode=:supplierCode";
            }
            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_CHALLAN_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_CHALLAN)) {
                //condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Registered.getId() + "'";
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PURCHASE_CHALLAN + "','" + pvc.PURCHASE_CHALLAN_UNREGISTERED + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!flagFilterByVoucherCategory) {
                sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
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
                    + " , vt.guiTemplateCode"
                    + " , vt.category"
                    + " , vt.balancingAc"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(partyType).trim().equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                psTemp.setString("category", pvc.PURCHASE_CHALLAN);
            } else {
                psTemp.setString("category", pvc.PURCHASE_CHALLAN_UNREGISTERED);
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                voucherType_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                voucherType_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                voucherType_Mdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                voucherType_Mdl.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                voucherType_Mdl.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, boolean includeTaxInCost, boolean includeOverHeadsInCost, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", balancingAcSql = "", serviceSql = "", finalDiscountSql = "", roundOffSql = "", baseSql = "", taxBalancingAcSql = "";
        String sgstContraAc = "", ugstContraAc = "", cgstContraAc = "", igstContraAc = "";
        String topInventoryAc = "";
        boolean flag = false;
        int sr = 0;

        try {
            //<editor-fold defaultstate="collapsed" desc="Prepare Account Posting SQL">
            sqlQuery = " SELECT \n"
                    + "		TOP 1 pcd.oh_Ac_Item AS subCode\n"
                    + "	FROM pChallan pc\n"
                    + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId \n"
                    + "	WHERE pc.docId = :docId\n"
                    + "	AND pcd.isFooter = 0\n"
                    + "	AND pcd.oh_Ac_Item IS NOT NULL\n"
                    + " ORDER BY pcd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                topInventoryAc = DmShared.xNull(rsTemp.getString("subCode"));
            }
            rsTemp.close();
            psTemp.close();

            ++sr;
            inventorySql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Inventory Posting\n"
                    + "	SELECT \n"
                    + "		pcd.oh_Ac_Item AS subCode\n"
                    + "		, pc.balancingAc AS contraSub\n"
                    + "		, pcd.landedSkuValue AS amount\n"
                    + "	FROM pChallan pc\n"
                    + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId \n"
                    + "	WHERE pc.docId = :docId\n"
                    + "	AND pcd.isFooter = 0\n"
                    + "	AND pcd.oh_Ac_Item IS NOT NULL\n"
                    + "	AND ISNULL(pcd.landedSkuValue, 0) <> 0\n"
                    + "\n"
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";

            if (includeTaxInCost) {
                sgstContraAc = ":topInventoryAc";
                ugstContraAc = ":topInventoryAc";
                cgstContraAc = ":topInventoryAc";
                igstContraAc = ":topInventoryAc";

                taxBalancingAcSql = "\n"
                        + "	UNION ALL\n"
                        + "\n"
                        + "	-- SGST Posting\n"
                        + getTaxPostingSql("pc.balancingAc", sgstContraAc, "pcd.oh_Amt_SGST", includeOverHeadsInCost)
                        + "\n"
                        + "	UNION ALL\n"
                        + "\n"
                        + "	-- UGST Posting\n"
                        + getTaxPostingSql("pc.balancingAc", ugstContraAc, "pcd.oh_Amt_UGST", includeOverHeadsInCost)
                        + "\n"
                        + "	UNION ALL \n"
                        + "\n"
                        + "	-- CGST Posting\n"
                        + getTaxPostingSql("pc.balancingAc", cgstContraAc, "pcd.oh_Amt_CGST", includeOverHeadsInCost)
                        + "\n"
                        + "	UNION ALL\n"
                        + "\n"
                        + "	-- IGST Posting\n"
                        + getTaxPostingSql("pc.balancingAc", igstContraAc, "pcd.oh_Amt_IGST", includeOverHeadsInCost);
            }

            ++sr;
            balancingAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Balancing A/c Posting\n"
                    + "	SELECT \n"
                    + "		pc.balancingAc AS subCode\n"
                    + "		, :topInventoryAc AS contraSub\n"
                    + "		, -ISNULL(pcd.oh_Amt_Taxable, 0) AS amount\n"
                    + "	FROM pChallan pc\n"
                    + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId \n"
                    + "	WHERE pc.docId = :docId\n"
                    + "	AND pcd.isFooter = 0\n"
                    + "	AND pc.balancingAc IS NOT NULL\n"
                    + "	AND ISNULL(pcd.oh_Amt_Taxable, 0) <> 0\n"
                    + "" + taxBalancingAcSql
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";

            if (includeOverHeadsInCost) {
                ++sr;
                serviceSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Service Posting\n"
                        + "	SELECT \n"
                        + "		pcd.oh_Ac_Item AS subCode\n"
                        + "		, :topInventoryAc AS contraSub\n"
                        + "		, -ISNULL(pcd.oh_Amt_Taxable, 0) AS amount\n"
                        + "	FROM pChallan pc\n"
                        + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId \n"
                        + "	WHERE pc.docId = :docId\n"
                        + "	AND pcd.isFooter = 2\n"
                        + "	AND pcd.oh_Ac_Item IS NOT NULL\n"
                        + "	AND ISNULL(pcd.oh_Amt_Taxable, 0) <> 0\n"
                        + "	) t\n"
                        + "GROUP BY t.subCode\n";
            }

            ++sr;
            finalDiscountSql = "-- Final Discount Posting\n"
                    + "SELECT \n"
                    + "	pc.oh_Ac_FinalDiscount AS subCode\n"
                    + "	, pc.balancingAc AS contraSub\n"
                    + "	, ISNULL(pc.oh_Amt_FinalDiscount, 0) AS amount\n"
                    + "	, " + sr + " AS sr\n"
                    + "FROM pChallan pc\n"
                    + "WHERE pc.docId = :docId\n"
                    + "AND pc.oh_Ac_FinalDiscount IS NOT NULL\n"
                    + "AND ISNULL(pc.oh_Amt_FinalDiscount, 0) <> 0\n";

            ++sr;
            roundOffSql = "-- Roundoff Posting\n"
                    + "SELECT \n"
                    + "	pc.oh_Ac_RoundOff AS subCode\n"
                    + "	, CASE WHEN ISNULL(pc.oh_Amt_RoundOff, 0) > 0 THEN :topInventoryAc ELSE pc.balancingAc END AS contraSub\n"
                    + "	, -ISNULL(pc.oh_Amt_RoundOff, 0) AS amount\n"
                    + "	, " + sr + " AS sr\n"
                    + "FROM pChallan pc\n"
                    + "WHERE pc.docId = :docId\n"
                    + "AND pc.oh_Ac_RoundOff IS NOT NULL\n"
                    + "AND ISNULL(pc.oh_Amt_RoundOff, 0) <> 0\n";

            baseSql = inventorySql
                    + "UNION ALL\n"
                    + "\n"
                    + balancingAcSql
                    + "\n"
                    + "UNION ALL\n";
            if (includeOverHeadsInCost) {
                baseSql += "\n " + serviceSql + " \n UNION ALL \n ";
            }
            baseSql += finalDiscountSql
                    + "\n"
                    + "UNION ALL\n"
                    + "\n"
                    + roundOffSql;

            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("topInventoryAc", DmShared.iNull(topInventoryAc).toString());
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
            psTemp.setString("topInventoryAc", DmShared.iNull(topInventoryAc).toString());
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

    private String getTaxPostingSql(String subCodeField, String contraAcField, String amountField, boolean includeOverHeadsInCost) throws Exception {
        String returnValue = "";
        try {
            returnValue = " SELECT \n"
                    + "		" + subCodeField + " AS subCode\n"
                    + "		, " + contraAcField + " AS contraSub\n"
                    + "		, -ISNULL(" + amountField + ", 0) AS amount\n"
                    + "	FROM pChallan pc\n"
                    + "	LEFT JOIN pChallanDetail pcd ON pcd.docId = pc.docId \n"
                    + "	WHERE pc.docId = :docId\n"
                    //                    + "	AND pcd.isFooter <> 1\n"
                    + (includeOverHeadsInCost ? " AND pcd.isFooter <> 1 " : " AND pcd.isFooter = 0 ")
                    + "	AND " + subCodeField + " IS NOT NULL\n"
                    + "	AND ISNULL(" + amountField + ", 0) <> 0\n";
        } catch (Exception e) {
            throw e;
        }
        return returnValue;
    }

    //</editor-fold>
    @Override
    public DmResult<String> updateRecord(Grn_Mdl mrTempCurrent, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

//
//            /*==============================<<Update record into pBill >>====================================*/
            sqlQuery = "UPDATE pChallan SET"
                    + "  pChallanNo = :pChallanNo \n"
                    + " , pChallanDate = :pChallanDate \n"
                    + " ,remark = :remark"
                    + " ,EWayBillNo = :EWayBillNo "
                    + " ,EWayBillDt = :EWayBillDt "
                    + " ,LRNo = :LRNo "
                    + " ,LRDate = :LRDate "
                    + " ,transporterCode = :transporterCode "
                    + " , u_Name = :u_Name \n"
                    + " , u_EntDt = :uEntDt \n"
                    + " WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
            psTemp.setDate("pChallanDate", DmServer.convertDate(mrTempCurrent.getChallanDate()));
            psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
            psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
            psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
            psTemp.setString("LRNo", DmShared.xNull(mrTempCurrent.getLrNo()));
            psTemp.setDate("LRDate", DmServer.convertDate(mrTempCurrent.getLrDate()));
            psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
            psTemp.setString("u_Name", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
//               
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                STApprovals.grnApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
