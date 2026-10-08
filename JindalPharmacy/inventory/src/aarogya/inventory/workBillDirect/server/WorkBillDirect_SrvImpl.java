package aarogya.inventory.workBillDirect.server;

import aarogya.inventory.workBillDirect.shared.GrdImpurity_Mdl;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Srv;
import aarogya.inventory.workBillDirect.shared.Find_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHVoucher_Mdl;
import aarogya.inventory.workBillDirect.shared.GrdGrnDetail_Mdl;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Vld;
import aarogya.inventory.workBillDirect.shared.WorkBillDirect_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHOrderNo_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHSupplier_Mdl;
import aarogya.inventory.workBillDirect.shared.DefaultValue_Mdl;
import aarogya.inventory.workBillDirect.shared.TxtHItemName_Mdl;
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
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WorkBillDirect_SrvImpl extends DmRSSPrivate implements WorkBillDirect_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pb.V_Type", voucherDateFieldName = "pb.V_Date";
    String serviceItemType = "";

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
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category IN ('" + pvc.WORK_BILL + "', '" + pvc.WORK_BILL_UNREGISTERED + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucher_Mdl();
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
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

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_BILL_DIRECT)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate"
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper helper, boolean blnToEndOfFile, boolean blnAllItemHelp) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

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
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
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
                    + " , ums.unit AS skuName,im.itemType"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code";
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ("
                        + "         SELECT ird.code "
                        + "         FROM itemRestrictedToDepartment ird "
                        + "         WHERE ird.departmentCode = :departmentCode"
                        + "     ) vird ON vird.code = im.Code ";

            }
            sqlQuery += condition;
            sqlWithParam.setSQL(sqlQuery);
            if (blnAllItemHelp == false) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
            } else {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            }

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
                listNode.setItemTypeCode(DmShared.xNull(rsTemp.getString("itemType")));

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
                    + " , um.gstUnitCode"
                    ////////////////////////////////////////
                    + " FROM POrderDetail pod"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.*"
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
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code"
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + "	WHERE vt.category = :vCat"
                    + " AND po.subCode = :subCode"
                    + " AND ISNULL(po.approvedBy,'') <> ''"
                    + " AND po.site_Code = :site_Code"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("vCat", pvc.WORK_ORDER);
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

                double amount = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("rate"));
                listNode.setAmount(DmServer.vNull(amount, "0.00"));

                listNode.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                listNode.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                listNode.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));

                ipm = new ItemProperty_Mdl();
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                listNode.setProperties(DmShared.xNull(ipm.getPropertiesCsv()));

                listNode.getTxtHItemName_Mdl().setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.getTxtHItemName_Mdl().setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.getTxtHItemName_Mdl().setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
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
                listNode.getTxtHItemName_Mdl().setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                //OHL PurchaseOrder
                listNode.setOHLListPurchaseOrder(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(listNode.getSharedKeyFieldCode()), DmShared.xNull(listNode.getPoV_SNo()), cnTemp));

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

            sqlQuery = "SELECT im.code, im.name FROM impurityMast im";
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
    public DmResult<List<GrdGrnDetail_Mdl>> getGrnDetail(List<KeyFieldExtended> keyFields, String subCode, String location, String billDate, String docId, String v_Date) {
        DmResult<List<GrdGrnDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, conditionLines = "", condition = "", pendingQty = "";
        List<GrdGrnDetail_Mdl> lstNode = new ArrayList<GrdGrnDetail_Mdl>();
        GrdGrnDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {
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

            sqlQuery = "SELECT  pod.itemCode, im.name as itemName, im.sku, ums.unit as skuName, ums.decimalPlaces AS skuDecimalPlaces, "
                    + "		po.docId as poDocId, po.recId as poRecId, po.refNo AS poRefNo, pod.v_SNo AS poV_SNo,  pod.unitCode,"
                    + "		um.unit as unitName, um.gstUnitCode, pod.quantity as orderQty, pod.challanedQty, pod.rate,"
                    + "		um.decimalPlaces AS unitDecimalPlaces, im.propertyList, im.purchaseUnitConversionRate,im.itemType,"
                    + "         pod.colourCode, icm.name AS colourName, pod.sizeCode, ism.name AS sizeName,"
                    + "         im.itsBasedOn, im.isBestBefore, im.hsnCode, im.itemGSTNature" + pendingQty
                    + " FROM POrderDetail pod"
                    + "	LEFT JOIN pOrder po ON pod.docId = po.docId";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ("
                        + "     SELECT pbd.*"
                        + "     FROM pBillDetail pbd"
                        + "     WHERE pbd.docid = :docId"
                        + " ) AS pbd ON pod.docId = pbd.poDocId AND pod.v_SNO = pbd.poV_SNo";
            }
            sqlQuery += " LEFT JOIN itemMast im ON pod.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode"
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code"
                    + "	WHERE po.subCode = :subCode"
                    + " AND po.site_Code = :site_Code"
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

                //Item Details
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                node.setItemTypeCode(DmShared.iNull(rsTemp.getString("itemType")).toString());

                //Properties
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));

                //PropertyCsv
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourName(node.getColourName());
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
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

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

                //ITS
                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(node.getSharedKeyFieldCode(), node.getRate(), location, node.getItsBasedOn(), billDate);
                node.setLstITS(dmrIts.getData());
                node.setGrossAcCode(dmrIts.getValue1());
                node.setGrossAcName(dmrIts.getValue2());

                //OHL PurchaseOrder
                node.setOHLListPurchaseOrder(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(node.getPoDocId()), DmShared.xNull(node.getPoV_SNo()), cnTemp));

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
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_Discount")));
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
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
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
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
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

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            STServer.chkIfDocumentUsed("pbDocId", "pBillDetail", keyCode, cnTemp);

            /*==============================<< Check for Bill generated or not >>====================================*/
            sqlQuery = "select approvedBy, approvalDateTime,"
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

    private WalkThroughDefault_Mdl getWalkThroughFor(WorkBillDirect_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode(mrTemp.getDepartmentTag());
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
            defaultValue.getLstSaleRateRule().addAll(STServer.getSaleRateRule(cnTemp));
            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));
            /*==============================<< Get default voucher_Type >>===============================*/
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " , vt.category"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")) {
                psTemp.setString("category", pvc.WORK_BILL);
            } else {
                psTemp.setString("category", pvc.WORK_BILL_UNREGISTERED);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                defaultValue.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.SinglePOFlowInPurchase \n"
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
                }

                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setPurchaseAcPostingBasedOn(DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")));
                defaultValue.setSinglePOFlowInPurchase(DmShared.iNull(rsTemp.getString("SinglePOFlowInPurchase")).intValue() == 1);
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
    public DmResult<String> saveRecord(WorkBillDirect_Mdl mrTempCurrent, WorkBillDirect_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String hisoryId = "";
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "",
                roundOffItemGstNature = "", invoiceCGST = "", invoiceSGST = "", invoiceIGST = "",
                invoiceUGST = "", invoiceValue = "";
        ResultSet rsTemp = null;
        GstTrans_Mdl gstTrans_Mdl = null;
        dmrTemp.setData("");
        int sno = 0;

        try {
            WorkBillDirect_Vld.chkSave(mrTempCurrent);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            OverHead_Shared.chkGrid(mrTempCurrent.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTempCurrent.getGrdGrnDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

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
                    + " WHERE  pb.partyBillNo = :partyBillNo "
                    + " AND pb.subCode=:subCode"
                    + " AND pb.docId != :docId "
                    + " AND vt.category IN ('" + pvc.WORK_BILL + "','" + pvc.WORK_BILL_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "Entry already exists with this Reference Number.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for restricted department >>====================================*/
            STServer.checkItemsRestrictedDepartment(mrTempCurrent.getLineItemCodesCsv(), mrTempCurrent.getDepartmentTag(), mrTempCurrent.getDepartmentText(), cnTemp);

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

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert / Update record into pBill >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pBill ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, partyBillNo, partyBillDate, "
                        + "     calcOn, subCode, consignee, partyType, rcmApplicable, placeOfSupply, departmentCode, "
                        + "     remark, wtaCode, guiConfigCode, site_Code, preparedBy, u_EntDt, u_EntDt_LatestLine, transferDt)"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, :partyBillNo, :partyBillDate, "
                        + "     :calcOn, :subCode, :consignee, :partyType, :rcmApplicable, :placeOfSupply, :departmentCode, "
                        + "     :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :uEntDt, :uEntDt, :transferDt)";
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
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTempCurrent.getDepartmentTag()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTempCurrent.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTempCurrent.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);

            } else {
                sqlQuery = "UPDATE pBill SET"
                        + "  v_Date = :v_Date"
                        + " ,departmentCode = :departmentCode"
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
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTempCurrent.getDepartmentTag()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getSupplierBillDate()));
                psTemp.setString("calcOn", DmShared.iNull(mrTempCurrent.getCalculateOnTag()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
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
                    + " AND vt.category IN ('" + pvc.WORK_BILL + "','" + pvc.WORK_BILL_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
            psTemp.setString("subCode", DmShared.xNull(mrTempCurrent.getSupplierTag()));
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

            /*==============================<< Delete records from pBillImpurityDetail >>====================================*/
            sqlQuery = " DELETE pBillImpurityDetail"
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

            /*==============================<< Insert records into pBillDetail >>====================================*/
            List<GrdGrnDetail_Mdl> listDetail = mrTempCurrent.getGrdGrnDetail();
            int vSNo = 0;
            for (GrdGrnDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    ++vSNo;
                    if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pBillDetail ("
                                + "     docId, v_SNo, poDocId, poV_SNo, itemCode, hsnCode, itemGSTNature, batchNo, manufacturingDate, expiryDate, colourCode,"
                                + "     sizeCode, mrp, skuMrp, unitCode, docQty, recQty, rejQty, acceptQty, skuDifference, skuQty, rate,"
                                + "     landedSkuValue, landedSkuRate, skuQtyForRate, skuRate, remark)"
                                + " Values ("
                                + "     :docId, :v_SNo, :poDocId, :poV_SNo, :itemCode, :hsnCode, :itemGSTNature, :batchNo, :manufacturingDate, :expiryDate, :colourCode,"
                                + "     :sizeCode, :mrp, :skuMrp, :unitCode, :docQty, :recQty, :rejQty, :acceptQty, :skuDifference, :skuQty, :rate,"
                                + "     :landedSkuValue, :landedSkuRate, :skuQtyForRate, :skuRate, :remark)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).toString());
                        psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                        psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                        psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                        psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                        psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColourCode()));
                        psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                        psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                        psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                        psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getChallanQty()));
                        psTemp.setString("recQty", DmServer.vNull(detail_Mdl.getRecQty()));
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
                        psTemp.executeUpdate();
                        psTemp.close();

                        /*==============================<< Insert records into pBillImpurityDetail >>====================================*/
                        sno = 0;
                        for (GrdImpurity_Mdl grdIm : detail_Mdl.getLstImpurity()) {
                            if (DmShared.iNull(grdIm.getSharedKeyFieldCode()).longValue() != 0) {
                                sqlQuery = "INSERT INTO pBillImpurityDetail ("
                                        + "     docId, v_SNo, sno, impurityCode, foundPer, acceptPer, netPer, netQty, supplierQty)"
                                        + " VALUES ("
                                        + "     :docId, :v_SNo, :sno, :impurityCode, :foundPer, :acceptPer, :netPer, :netQty, :supplierQty)";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                                psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                                psTemp.setString("sno", String.valueOf(++sno));
                                psTemp.setString("impurityCode", DmShared.foriegnKey(grdIm.getSharedKeyFieldCode()));
                                psTemp.setString("foundPer", DmShared.vNull(grdIm.getFoundPer()).toString());
                                psTemp.setString("acceptPer", DmShared.vNull(grdIm.getAcceptPer()).toString());
                                psTemp.setString("netPer", DmShared.vNull(grdIm.getNetPer()).toString());
                                psTemp.setString("netQty", DmServer.vNull(grdIm.getNetQty()));
                                psTemp.setString("supplierQty", DmServer.vNull(grdIm.getSupplierQty()));
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
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
            }
            /*==============================<< Insert records into stock >>====================================*/
            serviceItemType = DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp));
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

            /*==============================<< Manage Purchase Order (Billed Qty) >>====================================*/
            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), false, cnTemp);

            /*==============================<< Insert records into OH >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTempCurrent.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
//            OverHead_Server.save(mrTempCurrent.getOverHeadList(), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", "", "", "", "", false, true, cnTemp);
            OverHead_Server.save(mrTempCurrent.getOverHeadList(), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getObjRecId(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", DmShared.xNull(mrTempCurrent.getSupplierBillNo()), DmShared.dNull(mrTempCurrent.getSupplierBillDate()), "", "", false, true, cnTemp);

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

            /*==============================<< Delete records into gstTrans >>====================================*/
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode())), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Insert records into gstTrans >>====================================*/
            for (GrdGrnDetail_Mdl detail_Mdl : listDetail) {
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
                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTempCurrent.getVoucherDate()));
                    gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTempCurrent.getSupplierBillNo()));
                    gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTempCurrent.getSupplierBillDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTempCurrent.getSupplierTag()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTempCurrent.getSupplierTag()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTempCurrent.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(detail_Mdl.getHsnCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    gstTrans_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
                    gstTrans_Mdl.setQty(DmShared.iNull(mrTempCurrent.getCalculateOnTag()).intValue() == 1 ? DmShared.xNull(detail_Mdl.getAcceptQty()) : DmShared.xNull(detail_Mdl.getChallanQty()));
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
                gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));
                gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTempCurrent.getRcmApplicable()));
                gstTrans_Mdl.setItemCode(DmShared.foriegnKey(roundOffItemCode));
                gstTrans_Mdl.setHsnCode(DmShared.foriegnKey(roundOffItemHsnCode));
                gstTrans_Mdl.setItemGSTNature(DmShared.xNull(roundOffItemGstNature));
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
            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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
    public DmResult<WorkBillDirect_Mdl> getMoveRec(String strKey) {
        DmResult<WorkBillDirect_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery;
        WorkBillDirect_Mdl mrTemp = new WorkBillDirect_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdGrnDetail_Mdl> lstNode = new ArrayList<GrdGrnDetail_Mdl>();
        GrdGrnDetail_Mdl node;
        GrdImpurity_Mdl impurityNode;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT pb.*"
                    + " , vt.description AS voucherTypeName"
                    + " , vt.category"
                    + " , sgd.name AS departmentName"
                    + " , sgs.name AS supplierName"
                    + " , c.stateCode AS supplierStateCode"
                    + " , sgs.partyType AS supplierType"
                    + " , sgs.gstin AS supplierGstin"
                    + " , sgs.gstinDate AS supplierGstinDate"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgd ON pb.departmentCode = sgd.subCode"
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
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
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
                mrTemp.setCalculateOnText(DmServer.vNull(rsTemp.getString("calcOn"), "0").equalsIgnoreCase("1") ? "Accept Qty." : "Doc. Qty.");
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   pbd.*"
                    + " , po.recId AS poRecId"
                    + " , po.refNo AS poRefNo"
                    + " , im.name AS itemName"
                    + " , im.propertyList"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.sku AS skuCode,im.itemtype"
                    + " , ums.unit AS skuName"
                    + " , ums.decimalPlaces AS skuAllowDecimalPlaces"
                    + " , um.unit AS unitName"
                    + " , um.decimalPlaces AS unitAllowDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , icm.name AS colourName"
                    + " , ism.name AS sizeName"
                    + " , DATEDIFF(day, pbd.manufacturingDate, pbd.expiryDate) AS bestBeforeDays"
                    + " , ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) + ISNULL(pbd.acceptQty, 0) AS pendingQty"
                    + " FROM pBillDetail pbd"
                    + " LEFT JOIN pOrder po ON pbd.poDocId = po.docId"
                    + " LEFT JOIN pOrderDetail pod ON pbd.poDocId = pod.docId AND pbd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code"
                    + " LEFT JOIN itemColourMast icm ON pbd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pbd.sizeCode = ism.code"
                    + " WHERE pbd.docId = :docId"
                    + " AND ISNULL(pbd.isFooter, 0) = 0"
                    + " ORDER BY pbd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGrnDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setItemTypeCode(DmShared.xNull(rsTemp.getString("itemType")));

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

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
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
                ipm.setColourName(node.getColourName());
                ipm.setSizeName(node.getSizeName());
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

                //Other
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

                /*==============================<< Get Impurity detail >>===============================*/
                sqlQuery = " SELECT im.name AS impurityName, pcid.impurityCode, pcid.foundPer, pcid.acceptPer,"
                        + "  pcid.netPer, pcid.netQty, pcid.supplierQty"
                        + "  FROM pBillImpurityDetail pcid"
                        + "  INNER JOIN impurityMast im ON im.code = pcid.impurityCode"
                        + "  WHERE pcid.docId = :docId"
                        + "  AND pcid.v_SNo = :v_SNo"
                        + "  ORDER BY pcid.sno";
                psTempInner = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTempInner.setString("docId", DmShared.iNull(strKey).toString());
                psTempInner.setString("v_SNo", DmShared.iNull(node.getvSNo()).toString());
                rsTempInner = psTempInner.executeQuery();
                while (rsTempInner.next()) {
                    impurityNode = new GrdImpurity_Mdl();
                    impurityNode.setSharedKeyFieldCode(DmShared.xNull(rsTempInner.getString("impurityCode")));
                    impurityNode.setSharedKeyFieldName(DmShared.xNull(rsTempInner.getString("impurityName")));
                    impurityNode.setFoundPer(DmShared.vNull(rsTempInner.getString("foundPer")).toString());
                    impurityNode.setAcceptPer(DmShared.vNull(rsTempInner.getString("acceptPer")).toString());
                    impurityNode.setNetPer(DmShared.vNull(rsTempInner.getString("netPer")).toString());
                    impurityNode.setNetQty(DmServer.vNull(rsTempInner.getString("netQty")));
                    impurityNode.setSupplierQty(DmServer.vNull(rsTempInner.getString("supplierQty")));
                    node.getLstImpurity().add(impurityNode);
                }

                /*==============================<< Get OverheadLine detail >>===============================*/
                node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdGrnDetail().addAll(lstNode);

            /*==============================<< Get Overhead detail >>===============================*/
            mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));

            /*==============================<< Get GUI >>====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));

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

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason, Walkthrough & gstTrans >>============================*/
            OverHeadLine_Server.delete(strKey, cnTemp);
            OverHead_Server.delete(strKey, cnTemp);
            ModifyReason_Server.delete(strKey, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);
            Gst_Server.deleteGstTrans(strKey, cnTemp);

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
                    + " WHERE vt.category IN ('" + pvc.WORK_BILL + "', '" + pvc.WORK_BILL_UNREGISTERED + "')"
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

            sqlQuery = "SELECT pb.docId, pb.recId, pb.v_Date, pb.subcode, sgs.name as supName, pb.partyBillNo, pb.partyBillDate, "
                    + " sgd.name as departmentName, vt.short_Name, pb.v_No"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgd ON pb.departmentCode = sgd.subCode"
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
                listNode.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
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

    private CompareProperties_Mdl iniItemBatchProperties(WorkBillDirect_Mdl mrTempCurrent, WorkBillDirect_Mdl mrTempPrevious, boolean isDelete) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();

        if (!isDelete) {
            lstItemBatch_MdlCurnt.clear();
            for (GrdGrnDetail_Mdl detail_MdlCurrent : mrTempCurrent.getGrdGrnDetail()) {
                ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0 && !DmShared.xNull(detail_MdlCurrent.getItemTypeCode()).equalsIgnoreCase(serviceItemType)) {
                    batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                    batch_Mdl.getIpm().setBatchNo(DmShared.xNull(detail_MdlCurrent.getBatchNo()));
                    batch_Mdl.getIpm().setExpiryDate(DmShared.dNull(detail_MdlCurrent.getExpiryDate()));
                    batch_Mdl.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlCurrent.getManufacturingDate()));
                    batch_Mdl.getIpm().setMrp(DmShared.xNull(detail_MdlCurrent.getMrp()));
                    batch_Mdl.getIpm().setSkuMrp(DmShared.xNull(detail_MdlCurrent.getSkuMrp()));
                    batch_Mdl.getIpm().setColourCode(DmShared.xNull(detail_MdlCurrent.getColourCode()));
                    batch_Mdl.getIpm().setSizeCode(DmShared.xNull(detail_MdlCurrent.getSizeCode()));

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

                    lstItemBatch_MdlCurnt.add(batch_Mdl);
                }
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdGrnDetail_Mdl detail_MdlPrev : mrTempPrevious.getGrdGrnDetail()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0 && !DmShared.xNull(detail_MdlPrev.getItemTypeCode()).equalsIgnoreCase(serviceItemType)) {
                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                    batch_MdlPrev.getIpm().setBatchNo(DmShared.xNull(detail_MdlPrev.getBatchNo()));
                    batch_MdlPrev.getIpm().setExpiryDate(DmShared.dNull(detail_MdlPrev.getExpiryDate()));
                    batch_MdlPrev.getIpm().setManufacturingDate(DmShared.dNull(detail_MdlPrev.getManufacturingDate()));
                    batch_MdlPrev.getIpm().setMrp(DmShared.xNull(detail_MdlPrev.getMrp()));
                    batch_MdlPrev.getIpm().setSkuMrp(DmShared.xNull(detail_MdlPrev.getSkuMrp()));
                    batch_MdlPrev.getIpm().setColourCode(DmShared.xNull(detail_MdlPrev.getColourCode()));
                    batch_MdlPrev.getIpm().setSizeCode(DmShared.xNull(detail_MdlPrev.getSizeCode()));

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

                    lstItemBatch_MdlPrev.add(batch_MdlPrev);
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
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempPrevious.getDepartmentTag()));
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
}
