package aarogya.pharmacy.d365StockRecive.server;

import aarogya.pharmacy.d365StockRecive.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.D365_To_His_DataSync.server.StockReciveSync;
import aarogya.selfToolkit.itemUpdatePopup.server.ItemUpdateServer;
import aarogya.selfToolkit.itemUpdatePopup.shared.ItemUpdate_Mdl;
import com.google.gson.reflect.TypeToken;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.ITSType;
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
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import static dataman.dmToolkit.stock.server.Stock_Server.deleteUnusedItemBatches;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GRN_SrvImpl extends DmRSSPrivate implements GRN_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pc.V_Type", voucherDateFieldName = "pc.V_Date";

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
                    + " FROM voucher_Type vt"
                    + " left JOin subgroup sg on vt.balancingAc=sg.subCode "
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", filtration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PHARMACY_PURCHASE_CHALLAN)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate,sg.guiConfigCode "
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
                listNode.setGuiConfigCode(DmShared.dNull(rsTemp.getString("guiConfigCode")));
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
                    + "	LEFT JOIN subGroupSiteList sgl ON sg.subcode = sgl.subCode "
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 and sgl.site_Code=:site_Code " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
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
    public DmResultPageing<List<TxtHMedicineName_Mdl>> getTxtHMedicineName(String skuCode, String purchaseUnitCode, String restrictedDepartment, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicineName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicineName_Mdl> lstTemp = new ArrayList<TxtHMedicineName_Mdl>();
        TxtHMedicineName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            condition = " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)";
            condition += " AND im.isActive = 1 ";
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
                    + " , im.mrp"
                    + " , im.saleRate"
                    + " , im.purchaseRate"
                    + " , im.schemeOnQty"
                    + " , im.schemeFreeQty"
                    + " , im.purchaseDiscountPer"
                    + " , im.propertyList"
                    + " , im.sku AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , ISNULL(im.isIncludeFreeQtyInSaleRate,0) AS isIncludeFreeQtyInSaleRate,ic.saleRateProffitMargine "
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + "	LEFT JOIN itemCat ic ON im.itemCategoryCode = ic.code ";
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ("
                        + "         SELECT ird.code "
                        + "         FROM itemRestrictedToDepartment ird "
                        + "         WHERE ird.departmentCode = :departmentCode"
                        + "     ) vird ON vird.code = im.Code ";

            }
            sqlQuery += condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
                listNode = new TxtHMedicineName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.setMasterMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setMasterSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setMasterPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                listNode.setMasterSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                listNode.setMasterSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                listNode.setMasterPurchaseDiscountPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer")));
                listNode.setIsIncludeFreeQtyInSaleRate(DmShared.xNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")));
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
                listNode.setItemCategoryProffitMargine(DmShared.xNull(rsTemp.getString("saleRateProffitMargine")));
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
                    + " , pod.challanedQty \n"
                    + " , pod.rate"
                    + " , pod.MRP AS podMrp\n"
                    + " , pod.itemCode"
                    + " , im.name AS itemName"
                    + " , pod.unitCode"
                    + " , um.unit AS unitName" + pendingQty
                    ///////////////////////////////////////
                    + " , im.hsnCode"
                    + " , im.itemGSTNature \n"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.isBarcodeActive"
                    + " , im.mrp"
                    + " , im.saleRate"
                    + " , im.purchaseRate \n"
                    + " , im.schemeOnQty"
                    + " , im.schemeFreeQty"
                    + " , im.purchaseDiscountPer \n"
                    + " , ISNULL(im.isIncludeFreeQtyInSaleRate,0) AS isIncludeFreeQtyInSaleRate"
                    + " , im.propertyList"
                    + " , im.sku AS skuCode \n"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , pod.unitCode AS purchaseUnitCode "
                    + " , um.unit AS purchaseUnitName \n"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate \n"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces,ic.saleRateProffitMargine \n"
                    ////////////////////////////////////////
                    + " FROM POrderDetail pod \n"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId \n"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type \n"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code \n"
                    + "	LEFT JOIN itemCat ic ON im.itemCategoryCode = ic.code \n";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ( \n"
                        + "     SELECT pcd.* \n"
                        + "     FROM pChallanDetail pcd \n"
                        + "     WHERE pcd.docid = :docId \n"
                        + "     AND pcd.v_SNo = :v_SNo \n"
                        + " ) AS pcd ON pod.docId = pcd.poDocId AND pod.v_SNO = pcd.poV_SNo \n";
            }
            if (!DmShared.xNull(restrictedDepartment).trim().equals("")) {
                sqlQuery += " LEFT JOIN ( \n"
                        + "         SELECT ird.code  \n"
                        + "         FROM itemRestrictedToDepartment ird  \n"
                        + "         WHERE ird.departmentCode = :departmentCode \n"
                        + "   ) vird ON vird.code = im.Code  \n";
            }
            sqlQuery += " LEFT JOIN unitMast um ON pod.unitCode = um.code \n"
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + "	WHERE vt.category = :vCat \n"
                    + " AND po.subCode = :subCode \n"
                    + " AND ISNULL(po.approvedBy,'') <> '' \n"
                    + " AND po.site_Code = :site_Code \n"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("vCat", pvc.PHARMACY_PURCHASE_ORDER);
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
                listNode.setPodMrp(DmServer.vNull(rsTemp.getString("podMrp"), "0.00"));
                listNode.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));

                listNode.getTxtHMedName_Mdl().setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                listNode.getTxtHMedName_Mdl().setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.getTxtHMedName_Mdl().setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.getTxtHMedName_Mdl().setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.getTxtHMedName_Mdl().setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.getTxtHMedName_Mdl().setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.getTxtHMedName_Mdl().setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                listNode.getTxtHMedName_Mdl().setMasterMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.getTxtHMedName_Mdl().setMasterSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.getTxtHMedName_Mdl().setMasterPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                listNode.getTxtHMedName_Mdl().setMasterSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                listNode.getTxtHMedName_Mdl().setMasterSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                listNode.getTxtHMedName_Mdl().setMasterPurchaseDiscountPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer")));
                listNode.getTxtHMedName_Mdl().setIsIncludeFreeQtyInSaleRate(DmShared.xNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")));
                listNode.getTxtHMedName_Mdl().setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.getTxtHMedName_Mdl().setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.getTxtHMedName_Mdl().setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.getTxtHMedName_Mdl().setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                listNode.getTxtHMedName_Mdl().setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.getTxtHMedName_Mdl().setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.getTxtHMedName_Mdl().setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.getTxtHMedName_Mdl().setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.getTxtHMedName_Mdl().setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.getTxtHMedName_Mdl().setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                listNode.getTxtHMedName_Mdl().setItemCategoryProffitMargine(DmShared.xNull(rsTemp.getString("saleRateProffitMargine")));

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
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBatchNo_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ib.code"
                    + " , ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.saleRate"
                    + " , ib.expiryDate"
                    + " , ib.manufacturingDate"
                    + " , ib.barcode"
                    + " FROM itemBatch ib"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''"
                    + " AND ISNULL(ib.expiryDate, CAST(:uEntDt AS DATE)) >= CAST(:uEntDt AS DATE)";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            sqlWithParam.setString("itemCode", DmShared.xNull(medicineCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                listNode.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                listNode.setExpireDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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

            sqlQuery = "SELECT pod.mrp as poMrp,  pod.itemCode, im.name as itemName, im.sku, ums.unit as skuName, ums.decimalPlaces AS skuDecimalPlaces, \n"
                    + "		po.docId as poDocId, po.recId as poRecId, po.refNo AS poRefNo, pod.v_SNo AS poV_SNo,  pod.unitCode, um.unit as unitName, \n"
                    + "		pod.quantity as orderQty, pod.challanedQty, pod.rate, \n"
                    + "		um.decimalPlaces AS unitDecimalPlaces, im.propertyList, im.purchaseUnitConversionRate, \n"
                    + "         im.itsBasedOn, im.isBestBefore, im.isBarcodeActive, ISNULL(im.isIncludeFreeQtyInSaleRate,0) AS isIncludeFreeQtyInSaleRate, im.hsnCode, im.itemGSTNature, \n"
                    + "         im.mrp, im.saleRate, im.purchaseRate, im.schemeOnQty, im.schemeFreeQty, im.purchaseDiscountPer, \n"
                    + "         im.subCode as inventoryAc,sgi.name as inventoryAcName,ic.saleRateProffitMargine \n" + pendingQty
                    + " FROM POrderDetail pod \n"
                    + "	LEFT JOIN pOrder po ON pod.docId = po.docId \n";
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                sqlQuery += " LEFT JOIN ( \n"
                        + "     SELECT pcd.poDocId,pcd.acceptQty,pcd.poV_Sno \n"
                        + "     FROM pChallanDetail pcd \n"
                        + "     WHERE pcd.docid = :docId \n"
                        + " ) AS pcd ON pod.docId = pcd.poDocId AND pod.v_SNO = pcd.poV_SNo \n";
            }
            sqlQuery += " LEFT JOIN itemMast im ON pod.itemCode = im.code \n"
                    + "	LEFT JOIN itemCat ic ON im.itemCategoryCode = ic.code \n"
                    + "	LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + "	LEFT JOIN unitMast um ON pod.unitCode = um.code \n"
                    + "	LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                    + "	WHERE po.subCode = :subCode \n"
                    + " AND po.site_Code = :site_Code \n"
                    + " AND isNull(po.approvedBy,'') <> '' \n" + condition
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
                node.setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                node.setMasterMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));

                node.setMrp(DmServer.vNull(rsTemp.getString("poMrp"), "0.00"));

                if (DmShared.vNull(rsTemp.getString("poMrp")) > 0) {
                    node.setPoMrpExist(true);
                } else {
                    node.setPoMrpExist(false);
                }
                node.setPodMrp(DmServer.vNull(rsTemp.getString("poMrp"), "0.00"));
                node.setMasterSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setMasterPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
                node.setMasterSchemeOnQty(DmServer.vNull(rsTemp.getString("schemeOnQty")));
                node.setMasterSchemeFreeQty(DmServer.vNull(rsTemp.getString("schemeFreeQty")));
                node.setMasterPurchaseDiscountPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer")));
                node.setIsIncludeFreeQtyInSaleRate(DmShared.iNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")).toString());
                node.setSaleRateProffitMargine(DmServer.vNull(rsTemp.getString("saleRateProffitMargine")));

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
                node.setLstITS(getITS(node.getSharedKeyFieldCode(), node.getRate(), location, node.getItsBasedOn(), billDate).getData());

                //OHL PurchaseOrder
                node.setOHLListPurchaseOrder(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(node.getPoDocId()), DmShared.xNull(node.getPoV_SNo()), cnTemp));

                //set Inventory Accounts
                node.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                node.setInventoryAcName(DmShared.xNull(rsTemp.getString("inventoryAcName")));

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
                    + " , pcd.freeQty"
                    + " , pcd.rate"
                    + " , pcd.oh_At_Discount"
                    + " , pcd.saleRate"
                    + " , pcd.mrp"
                    + " , pcd.batchNo"
                    + " , pcd.expiryDate"
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
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
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
                    + " , pcd.freeQty"
                    + " , pcd.rate"
                    + " , pcd.oh_At_Discount"
                    + " , pcd.saleRate"
                    + " , pcd.mrp"
                    + " , pcd.batchNo"
                    + " , pcd.expiryDate"
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
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
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
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_Discount")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                if (DmShared.vNull(node.getRate()).doubleValue() > 0) {
                    node.setProfitPer(DmServer.vNull(((DmShared.vNull(node.getSaleRate()) - DmShared.vNull(node.getRate())) / DmShared.vNull(node.getRate())) * 100, "0.00"));
                }
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
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
            STServer.chkIfDocumentUsed("pcDocId", "pBillDetail", keyCode, cnTemp);

            sqlQuery = "select approvedBy,  approvalDateTime,"
                    + " (case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + " from pChallan where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
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

                        sqlQuery = " Update pChallan set "
                                + " u_EntDt_LatestLine=:uEntDt"
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = "UPDATE ledger SET"
                                + " isPosted = 1"
                                + " WHERE docId = :docId";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
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

                            sqlQuery = "select docId"
                                    + " from pChallanDetail"
                                    + " where docId = :docId and ISNULL(billedQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update pChallan set"
                                    + " u_EntDt_LatestLine=:uEntDt"
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = "UPDATE ledger SET"
                                    + " isPosted = 0"
                                    + " WHERE docId = :docId";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
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

    private WalkThroughDefault_Mdl getWalkThroughFor(GRN_Mdl mrTemp) {
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
                    + " , vt.balancingAc, sg.name as balancingAcName"
                    + " FROM voucher_Type vt"
                    + " left JOin subgroup sg on vt.balancingAc=sg.subCode "
                    //                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_CHALLAN + "' "
                    + " WHERE vt.category = :category "
                    + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")) {
                psTemp.setString("category", pvc.PHARMACY_PURCHASE_CHALLAN);
            } else {
                psTemp.setString("category", pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED);
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
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.SinglePOFlowInPurchase,e.ph_SRateEditInPurchase, \n"
                    + "e.inv_DefaultPurchaseDepartment,sg.name as defaultPurchaseDepartmentName,e.calculateSaleRate_On_TaxInclusiveValue \n"
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
                } else if (rsTemp.getString("singlePOFlowInPurchase") == null) {
                    throw new DmException(0, "Single PO Flow In Purchase is not defined in Environment Setting.");
                }
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setPurchaseAcPostingBasedOn(DmShared.xNull(rsTemp.getString("purchaseSalesAcPostingBasedOn")));
                defaultValue.setSinglePOFlowInPurchase(DmShared.iNull(rsTemp.getString("singlePOFlowInPurchase")).intValue() == 1);
                defaultValue.setPh_SRateEditInPurchase(DmShared.iNull(rsTemp.getString("ph_SRateEditInPurchase")).intValue() == 1);
                defaultValue.setDefaultPurchaseDepartment(DmShared.xNull(rsTemp.getString("inv_DefaultPurchaseDepartment")));
                defaultValue.setDefaultPurchaseDepartmentText(DmShared.xNull(rsTemp.getString("defaultPurchaseDepartmentName")));
                defaultValue.setCalculateSaleRate_On_TaxInclusiveValue(DmShared.iNull(rsTemp.getString("calculateSaleRate_On_TaxInclusiveValue")).intValue() == 1);
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
    public DmResult<String> saveRecord(GRN_Mdl mrTempCurrent, GRN_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");
        int sno = 0;
        String hisoryId = "";
        try {
            GRN_Vld.chkSave(mrTempCurrent);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            OverHead_Shared.chkGrid(mrTempCurrent.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTempCurrent.getGrdGrnDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTempCurrent.getVoucherDate(), "");
            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
            }

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

            //*==============================<< Check for duplicate party reference no >>====================================*/
            sqlQuery = " SELECT COUNT(pc.docid) AS cnt "
                    + " FROM pChallan pc with (nolock) "
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pc.v_Type "
                    + " WHERE  pc.pChallanNo = :pChallanNo "
                    + " AND pc.subCode=:subCode "
                    + " AND pc.docId != :docId "
                    + " AND pc.v_Prefix = :v_Prefix ";
//                    + " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
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
            sqlQuery = "SELECT ohm.itemCode, im.hsnCode"
                    + " FROM overHeadMast ohm"
                    + " LEFT JOIN itemMast im ON ohm.itemCode = im.code"
                    + " WHERE ohm.manualCode = 'ROFF'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                roundOffItemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                roundOffItemHsnCode = DmShared.xNull(rsTemp.getString("hsnCode"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(roundOffItemCode).trim().equals("")) {
                throw new DmException(0, "Round Off Item not defined in Overhead Master.");
            }

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert / Update record into pChallan >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pChallan ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, pChallanNo, pChallanDate, subCode, consignee, partyType,"
                        + "     rcmApplicable, placeOfSupply, departmentCode, remark, wtaCode, guiConfigCode, site_Code, preparedBy, u_EntDt, u_EntDt_LatestLine, transferDt)"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, :pChallanNo, :pChallanDate, :subCode, :consignee, :partyType,"
                        + "     :rcmApplicable, :placeOfSupply, :departmentCode, :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :uEntDt, :uEntDt, :transferDt)";
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
                sqlQuery = "UPDATE pChallan SET"
                        + "  v_Date = :v_Date"
                        + " ,departmentCode = :departmentCode"
                        + " ,pChallanNo = :pChallanNo"
                        + " ,pChallanDate = :pChallanDate"
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
                psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
                psTemp.setDate("pChallanDate", DmServer.convertDate(mrTempCurrent.getChallanDate()));
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
            //*==============================<< Check for duplicate party reference no >>====================================*/
            sqlQuery = " SELECT COUNT(pc.docid) AS cnt "
                    + " FROM pChallan pc with (nolock) "
                    + " INNER JOIN voucher_type vt ON vt.v_Type = pc.v_Type "
                    + " WHERE  pc.pChallanNo = :pChallanNo "
                    + " AND pc.subCode=:subCode "
                    + " AND pc.v_Prefix = :v_Prefix  "
                    + " AND pc.docId != :docId ";
//                    + " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
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
//            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), true, cnTemp);

            /*==============================<< Delete records from pChallanImpurityDetail >>====================================*/
            sqlQuery = " DELETE pChallanImpurityDetail"
                    + " FROM pChallanImpurityDetail AS pid"
                    + " INNER JOIN pChallanDetail pcd ON pid.docId = pcd.docId AND pid.v_Sno = pcd.v_Sno"
                    + " WHERE pcd.docId = :docId AND pcd.isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from pChallanDetail >>====================================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pChallanDetail", "ibCode", " AND docid = '" + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "'", cnTemp);

            /*==============================<< Get itemUpdateHistory ID >>====================================*/
            hisoryId = DmServer.getCodeViaManualCode(mrTempCurrent.getSharedKeyFieldCode(), "docId", "itemUpdateHistory", "(ISNULL(MAX(id),0) + 1)", cnTemp);

            sqlQuery = "Delete From pChallanDetail Where docId = :docId AND isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into pChallanDetail >>====================================*/
            List<GrdGrnDetail_Mdl> listDetail = mrTempCurrent.getGrdGrnDetail();
            int vSNo = 0;
            for (GrdGrnDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    ++vSNo;
                    if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pChallanDetail ("
                                + "     docId, v_SNo, poDocId, poV_SNo, itemCode, hsnCode, itemGSTNature, batchNo, barcode, manufacturingDate, expiryDate, "
                                + "     mrp, skuMrp, unitCode, challanQty, recQty, rejQty, acceptQty, freeQty, skuDifference, skuQty, rate,"
                                + "     saleRate, skuSaleRate, staffSaleRate, skuStaffSaleRate, landedSkuValue, landedSkuRate, skuQtyForRate, skuRate, remark, isStatus)"
                                + " Values ("
                                + "     :docId, :v_SNo, :poDocId, :poV_SNo, :itemCode, :hsnCode, :itemGSTNature, :batchNo, :barcode, :manufacturingDate, :expiryDate, "
                                + "     :mrp, :skuMrp, :unitCode, :challanQty, :recQty, :rejQty, :acceptQty, :freeQty, :skuDifference, :skuQty, :rate,"
                                + "     :saleRate, :skuSaleRate, :staffSaleRate, :skuStaffSaleRate, :landedSkuValue, :landedSkuRate, :skuQtyForRate, :skuRate, :remark, 0)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).toString());
                        psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                        psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                        psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                        psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                        psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                        psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                        psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                        psTemp.setString("challanQty", DmServer.vNull(detail_Mdl.getChallanQty()));
                        psTemp.setString("recQty", DmServer.vNull(detail_Mdl.getRecQty()));
                        psTemp.setString("rejQty", DmServer.vNull(detail_Mdl.getRejQty()));
                        psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getAcceptQty()));
                        psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQty()));
                        psTemp.setString("skuDifference", DmServer.vNull(detail_Mdl.getSkuDifferenceQty()));
                        psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate(), "0.00"));
                        psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                        psTemp.setString("staffSaleRate", DmServer.vNull(detail_Mdl.getStaffSaleRate(), "0.00"));
                        psTemp.setString("skuStaffSaleRate", DmServer.vNull(detail_Mdl.getSkuStaffSaleRate()));
                        psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
                        psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                        psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                        psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                        psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        /*==============================<< Insert records into pChallanImpurityDetail >>====================================*/
                        sno = 0;
                        for (GrdImpurity_Mdl grdIm : detail_Mdl.getLstImpurity()) {
                            if (DmShared.iNull(grdIm.getSharedKeyFieldCode()).longValue() != 0) {
                                sqlQuery = "INSERT INTO pChallanImpurityDetail ("
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
                            iuMdl.setV_No(DmShared.iNull(mrTempCurrent.getRecIdGRN().getCounter()).toString());
                            iuMdl.setV_Date(mrTempCurrent.getVoucherDate());
                            iuMdl.setRecId(mrTempCurrent.getSharedKeyFieldName());
                            iuMdl.setSubcode(mrTempCurrent.getSupplierTag());
                            iuMdl.setItemCode(detail_Mdl.getSharedKeyFieldCode());
                            iuMdl.setUnitCode(detail_Mdl.getPurchaseUnitCode());
                            iuMdl.setLstItemUpdateFields(detail_Mdl.getLstItemUpdateFields());
                            ItemUpdateServer.updateItem(iuMdl, cnTemp, this);
                        }
                    }
                    /*=============================<< update isCreated in D365_PurchaseOrderDetails >>========*/

                    sqlQuery = "update D365_PurchaseOrderDetails set "
                            + "isCreated = 1 "
                            + "where docId = :docId";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(detail_Mdl.getPoDocId()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            /*==============================<< Insert records into stock >>====================================*/
//            ItemBatch_Server.compareProperties(iniItemBatchProperties(mrTempCurrent, mrTempPrevious, false), cnTemp, this);

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
//            managePOrderStatus(mrTempCurrent.getSharedKeyFieldCode(), false, cnTemp);

            /*==============================<< Insert records into OH >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTempCurrent.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            OverHead_Server.save(mrTempCurrent.getOverHeadList(), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdGRN(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", "", "", "", "", true, true, cnTemp);

            /*==============================<< Insert Round Off Line >>====================================*/
            if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) != 0) {
                sqlQuery = "INSERT INTO pChallanDetail ("
                        + "     docId, v_SNo, itemCode, hsnCode, challanQty, acceptQty, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter)"
                        + " Values ("
                        + "     :docId, :v_SNo, :itemCode, :hsnCode, :challanQty, :acceptQty, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, 1)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("v_SNo", String.valueOf(++vSNo));
                psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) < 0) {
                    psTemp.setString("challanQty", "-1");
                    psTemp.setString("acceptQty", "-1");
                } else {
                    psTemp.setString("challanQty", "1");
                    psTemp.setString("acceptQty", "1");
                }
                psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTempCurrent.getRoundOffAmount())), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            mrTempCurrent.setSharedModifyDate(DmServer.formatTimestamp(DmServer.getServerTimestamp()));
            if (!blnAdd && mrTempCurrent.getModifyReasonMdl() != null) {
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(mrTempCurrent.getSharedKeyFieldCode());
                modifyData_Mdl.setKeyName_1(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                modifyData_Mdl.setKeyDescription_1(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                modifyData_Mdl.setModuleName(new pmPharmacy().moduleName);
                modifyData_Mdl.setMenuName(new pmPharmacy().GRN.caption);
                modifyData_Mdl.setModelClassName(GRN_Mdl.class.getName());
                modifyData_Mdl.setTableName("enquiry");

                modifyData_Mdl.setOldData(DmServer.convertToJson((GRN_Mdl) DmServer.getSsnOldData(getClass().getName(), this), new TypeToken<GRN_Mdl>() {
                }));
                modifyData_Mdl.setNewData(DmServer.convertToJson(mrTempCurrent, new TypeToken<GRN_Mdl>() {
                }));
                ModifyReason_Server.saveNew(modifyData_Mdl, mrTempCurrent.getModifyReasonMdl(), this);
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
    public DmResult<GRN_Mdl> getMoveRec(String strKey) {
        DmResult<GRN_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmPreparedStatement psTempInner = null;
        ResultSet rsTemp = null;
        ResultSet rsTempInner = null;
        String sqlQuery;
        GRN_Mdl mrTemp = new GRN_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdGrnDetail_Mdl> lstNode = new ArrayList<GrdGrnDetail_Mdl>();
        GrdGrnDetail_Mdl node;
        GrdImpurity_Mdl impurityNode;

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
            sqlQuery = "SELECT pc.docId"
                    + " , pc.v_Type"
                    + " , pc.v_No"
                    + " , pc.recId"
                    + " , pc.v_Prefix"
                    + " , pc.v_Date"
                    + " , pc.departmentCode"
                    + " , pc.subCode"
                    + " , pc.consignee"
                    + " , pc.placeOfSupply"
                    + " , pc.remark"
                    + " , pc.pChallanNo"
                    + " , pc.pChallanDate"
                    + " , pc.CMN_Addition"
                    + " , pc.CMN_Deduction"
                    + " , pc.oh_Amt_Gross"
                    + " , pc.oh_Amt_Total"
                    + " , pc.oh_Amt_RoundOff"
                    + " , pc.oh_Amt_Net"
                    + " , pc.u_EntDt"
                    + " , pc.u_Name"
                    + " , pc.preparedBy"
                    + " , pc.approvedBy"
                    + " , pc.site_Code"
                    + " , pc.wtaCode"
                    + " , pc.approvalDateTime"
                    + " , pc.transferDt"
                    + " , pc.recIdPrefix"
                    + " , pc.rcmApplicable"
                    + " , pc.partyType"
                    + " , pc.u_EntDt_LatestLine"
                    + " , pc.guiConfigCode"
                    + " , pc.oh_Amt_LandedValue"
                    + " , vt.category"
                    + " , vt.description AS voucherTypeName"
                    + " , sgd.name AS departmentName"
                    + " , sgs.name AS supplierName"
                    + " , c.stateCode AS supplierStateCode"
                    + " , sgs.partyType AS supplierPartyType"
                    + " , sgs.gstin AS supplierGstin"
                    + " , sgs.gstinDate AS supplierGstinDate"
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
                    + " FROM pChallan pc"
                    + " LEFT JOIN voucher_Type vt ON pc.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgd ON pc.departmentCode = sgd.subCode"
                    + " LEFT JOIN subGroup sgs ON pc.subcode = sgs.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sgs.cityCode"
                    + " WHERE pc.docId = :docId";
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
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                mrTemp.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierPartyType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
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
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Medicine grid detail >>===============================*/
            sqlQuery = "SELECT pcd.docId"
                    + " , pcd.v_SNo"
                    + " , pcd.poDocId"
                    + " , pcd.poV_SNo"
                    + " , pcd.itemCode"
                    + " , pcd.unitCode"
                    + " , pcd.challanQty"
                    + " , pcd.recQty"
                    + " , pcd.rejQty"
                    + " , pcd.acceptQty"
                    + " , pcd.skuQty"
                    + " , pcd.billedQty"
                    + " , pcd.rate"
                    + " , pcd.oh_Amt_Gross"
                    + " , pcd.oh_Amt_Net"
                    + " , pcd.remark"
                    + " , pcd.freeQty"
                    + " , pcd.mrp"
                    + " , pcd.skuMrp"
                    + " , pcd.saleRate"
                    + " , pcd.skuSaleRate"
                    + " , pcd.staffSaleRate"
                    + " , pcd.skuStaffSaleRate"
                    + " , pcd.batchNo"
                    + " , pcd.manufacturingDate"
                    + " , pcd.expiryDate"
                    + " , pcd.colourCode"
                    + " , pcd.sizeCode"
                    + " , pcd.oh_Amt_Discount"
                    + " , pcd.oh_at_Discount"
                    + " , pcd.oh_Amt_Taxable"
                    + " , pcd.oh_at_CGST"
                    + " , pcd.oh_Amt_CGST"
                    + " , pcd.oh_at_SGST"
                    + " , pcd.oh_Amt_SGST"
                    + " , pcd.oh_at_IGST"
                    + " , pcd.oh_Amt_IGST"
                    + " , pcd.oh_at_UGST"
                    + " , pcd.oh_Amt_UGST"
                    + " , pcd.skuDifference"
                    + " , pcd.skuRate"
                    + " , pcd.landedSkuValue"
                    + " , pcd.landedSkuRate"
                    + " , pcd.itemGSTNature"
                    + " , pcd.isFooter"
                    + " , pcd.isStatus"
                    + " , pcd.skuQtyForRate"
                    + " , pcd.ibCode"
                    + " , pcd.isIncludeFreeQtyInSaleRate"
                    + " , pcd.billedFreeQty"
                    + " , pcd.hsnCode"
                    + " , pcd.barcode"
                    + " , po.recId AS poRecId"
                    + " , po.refNo AS poRefNo"
                    + " , im.name AS itemName"
                    + " , im.propertyList"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.isBarcodeActive"
                    + " , im.mrp AS masterMrp"
                    + " , im.saleRate AS masterSaleRate"
                    + " , im.purchaseRate AS masterPurchaseRate"
                    + " , im.schemeOnQty AS masterSchemeOnQty"
                    + " , im.schemeFreeQty AS masterSchemeFreeQty"
                    + " , im.purchaseDiscountPer AS masterPurchaseDiscountPer"
                    + " , im.sku AS skuCode"
                    + " , ump.unit AS skuName"
                    + " , ump.decimalPlaces AS skuAllowDecimalPlaces"
                    + " , ums.unit AS unitName"
                    + " , ums.decimalPlaces AS unitAllowDecimalPlaces"
                    + " , DATEDIFF(day, pcd.manufacturingDate, pcd.expiryDate) AS bestBeforeDays"
                    + " , pod.mrp AS podMrp \n"
                    + " , ISNULL(pod.quantity, 0) - ISNULL(pod.challanedQty, 0) + ISNULL(pcd.acceptQty, 0) AS pendingQty,ic.saleRateProffitMargine"
                    + " FROM pChallanDetail pcd"
                    + " LEFT JOIN pOrder po ON pcd.poDocId = po.docId"
                    + " LEFT JOIN pOrderDetail pod ON pcd.poDocId = pod.docId AND pcd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code"
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode"
                    + " LEFT JOIN unitMast ump ON im.sku = ump.code"
                    + " LEFT JOIN unitMast ums ON pcd.unitCode = ums.code"
                    + " WHERE pcd.docId = :docId"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " ORDER BY pcd.v_SNo";
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
                node.setIsBarcodeActive(DmShared.xNull(rsTemp.getString("isBarcodeActive")));
                node.setMasterMrp(DmServer.vNull(rsTemp.getString("masterMrp"), "0.00"));
                node.setMasterSaleRate(DmServer.vNull(rsTemp.getString("masterSaleRate"), "0.00"));
                node.setMasterPurchaseRate(DmServer.vNull(rsTemp.getString("masterPurchaseRate"), "0.00"));
                node.setMasterSchemeOnQty(DmServer.vNull(rsTemp.getString("masterSchemeOnQty")));
                node.setMasterSchemeFreeQty(DmServer.vNull(rsTemp.getString("masterSchemeFreeQty")));
                node.setMasterPurchaseDiscountPer(DmServer.vNull(rsTemp.getString("masterPurchaseDiscountPer")));
                node.setIsIncludeFreeQtyInSaleRate(DmShared.iNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")).toString());
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuAllowDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("unitAllowDecimalPlaces")));

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double skuDifference = DmShared.vNull(rsTemp.getString("skuDifference"));
                double freeQty = DmShared.vNull(rsTemp.getString("freeQty"));
                double acceptQty = DmShared.vNull(rsTemp.getString("acceptQty"));
                double purchaseUnitConversionRate = (skuQty - skuDifference) / (acceptQty + freeQty);
                node.setPurchaseUnitConversionRate(DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
                node.setPurchaseUnitConversionRate(DmServer.vNull(node.getPurchaseUnitConversionRate()));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("unitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("unitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setBestBeforeDays(DmShared.xNull(rsTemp.getString("bestBeforeDays")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

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
                node.setPodMrp(DmShared.xNull(rsTemp.getString("podMrp")));

                //Quantities
                node.setChallanQty(DmServer.vNull(rsTemp.getString("challanQty")));
                node.setRecQty(DmServer.vNull(rsTemp.getString("recQty")));
                node.setRejQty(DmServer.vNull(rsTemp.getString("rejQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setSkuDifferenceQty(DmServer.vNull(rsTemp.getString("skuDifference")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setAcceptQty(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setBilledQty(DmServer.vNull(rsTemp.getString("billedQty")));

                //Rate
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));
                node.setStaffSaleRate(DmServer.vNull(rsTemp.getString("staffSaleRate"), "0.00"));
                node.setSkuStaffSaleRate(DmServer.vNull(rsTemp.getString("skuStaffSaleRate")));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setSaleRateProffitMargine(DmShared.xNull(rsTemp.getString("saleRateProffitMargine")));

                //Other
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());

                /*==============================<< Get Impurity detail >>===============================*/
                sqlQuery = " SELECT im.name AS impurityName, pcid.impurityCode, pcid.foundPer, pcid.acceptPer,"
                        + "  pcid.netPer, pcid.netQty, pcid.supplierQty"
                        + "  FROM pChallanImpurityDetail pcid"
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

            /*==============================<< Get GUI >>===============================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));

            dmrTemp.setData(mrTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
            DmServer.setSsnOldData(getClass().getName(), mrTemp, this);
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
    public DmResult<String> deleteRecord(String strKey, String recId, String guiConfigCode, ModifyReason_Mdl modifyReasonMdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            Timestamp timestamp = DmServer.getServerTimestamp();
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

            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);

            /*==============================<< Delete record from itemUpdateHistory >>===============================*/
            ItemUpdateServer.deleteItemUpdateHistory(strKey, cnTemp);

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
            saveLog(strKey, recId, modifyReasonMdl, null, DmEnum.DmTransactionMode.DELETE, null, timestamp);
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
        String sqlQuery, sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select pc.docId "
                    + "         from pChallan as pc "
                    + "         left join voucher_Type as vt on pc.v_Type = vt.v_Type  "
                    + "         where vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + "         and pc.site_Code = :siteCode and pc.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
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
                    + "	        sgd.name as departmentName, vt.short_Name, pc.v_No "
                    + "		from pChallan as pc "
                    + "		left join voucher_Type as vt on pc.v_Type = vt.v_Type "
                    + "         left join subGroup as sgd on pc.departmentCode = sgd.subCode "
                    + "         left join subGroup as sgs on pc.subcode = sgs.subCode"
                    + "		where vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "','PHCHL','PRTN','PHWBC')"
                    + "         and pc.site_Code = :site_Code and pc.v_Prefix=:vPrefix";
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
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = STServer.getITS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, ITSType.PURCHASE, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
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

    private void managePOrderStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, poDocId = "";
        double poQty, challanedQty, grnQty, tl_ExcessQty, tl_ShortageQty;
        Integer isStatus;

        try {

            sqlQuery = "select	p.docId as poDocId, pcd.grnQty, pd.quantity as poQty, pd.challanedQty, pd.isStatus, p.approvedBy, "
                    + "		pd.tl_ExcessQty, pd.tl_ShortageQty, p.recId as poNo, vt.short_name as poType, pd.itemCode as medicineCode, pd.v_Sno as poV_SNo "
                    + "		from pOrderDetail pd "
                    + "		left join pOrder p on p.docId = pd.docId "
                    + "		left join voucher_type vt on vt.v_type = p.v_Type "
                    + "		inner join ( "
                    + "				select	pcd.poDocId, pcd.poV_SNo, sum(pcd.acceptQty) as grnQty "
                    + "				from	pChallanDetail pcd "
                    + "				where pcd.docId = :docId "
                    + "				group by pcd.poDocId, pcd.poV_SNo "
                    + "		) pcd on pd.docId = pcd.poDocId and pd.v_SNo = pcd.poV_SNo  ";
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
                        + " where docId = :docId and v_SNo = :v_SNo ";
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
    public DmResult<List<OverHeadITS_Mdl>> getStockPostingLedgers(String itemCode) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        List<OverHeadITS_Mdl> overHeadITSList = new ArrayList<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select im.subcode as acCode "
                    + " , sg.name as acName "
                    + " FROM itemMast im   "
                    + " LEFT JOIN subgroup sg on sg.subCode = im.subcode "
                    + " WHERE im.code = :itemCode ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                dmrTemp.setValue1(DmShared.xNull(rsTemp.getString("acCode")));
                dmrTemp.setValue2(DmShared.xNull(rsTemp.getString("acName")));
            }
            dmrTemp.setData(overHeadITSList);
            dmrTemp.setStatus(true);

            psTemp.close();
            rsTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

        return dmrTemp;
    }

    @Override
    public DmResult<String> updateRecord(GRN_Mdl mrTempCurrent, boolean isApprove) {
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
//            /*==============================<<  Update record into pChallan >>====================================*/
            if ((DmServer.convertDate(mrTempCurrent.getLastTransactionDate()).getTime() > DmServer.convertDate(mrTempCurrent.getVoucherDate()).getTime())) {
                sqlQuery = "UPDATE pChallan SET"
                        + "  pChallanNo = :pChallanNo \n"
                        + " , pChallanDate = :pChallanDate \n"
                        + " ,remark = :remark"
                        + " , u_Name = :u_Name \n"
                        + " , u_EntDt = :uEntDt \n"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("pChallanNo", DmShared.xNull(mrTempCurrent.getChallanNo()));
                psTemp.setDate("pChallanDate", DmServer.convertDate(mrTempCurrent.getChallanDate()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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

    private void saveLog(String strKey, String recId, ModifyReason_Mdl modifyReasonMdl, GRN_Mdl mrTemp, DmEnum.DmTransactionMode transactionMode, DmEnum.DmTransactionMode controlText, Timestamp discardTimestamp) {
        String description = "", shortName = "";
        boolean blnLog = false;
        String strSQL = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            //<editor-fold defaultstate="collapsed" desc="Validate Key">
            if (!DmShared.xNull(strKey).trim().equalsIgnoreCase("")) {
                if (mrTemp != null) {
                    if (!DmShared.xNull(strKey).equalsIgnoreCase(DmShared.xNull(mrTemp.getSharedKeyFieldCode()))) {
                        throw new DmException(0, "Key is not vaild.");
                    }
                }
            } else {
                throw new DmException(0, "Key is requied for saving log.");
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Validate ModifyReasonMdl">
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.PRINT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.APPROVE.getValue())) {
                blnLog = true;
                modifyReasonMdl = new ModifyReason_Mdl();
                modifyReasonMdl.setSharedKeyFieldCode(DmShared.xNull(DmServer.getCodeViaManualCode(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName(), "name", "modifyReasonMast", "code", cnTemp)));
                modifyReasonMdl.setSharedKeyFieldName(DmShared.xNull(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName()));

            } else {
                if (modifyReasonMdl == null) {
                    if (mrTemp != null) {
                        modifyReasonMdl = mrTemp.getModifyReasonMdl();
                    }
                }
                if (modifyReasonMdl != null) {
                    if (mrTemp != null) {
                        if (!DmShared.xNull(modifyReasonMdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(mrTemp.getModifyReasonMdl().getSharedKeyFieldCode()))) {
                            throw new DmException(0, "ModifyReasonMdl is not vaild.");
                        }
                    }
                }
            }
            if (modifyReasonMdl == null) {
                throw new DmException(0, "Key is requied for saving log.");
            }
            //</editor-fold>
            modifyReasonMdl.setMode(DmShared.xNull(transactionMode.getValue()));
            strSQL = "SELECT vt.description, vt.short_Name \n"
                    + "FROM pChallan \n"
                    + "LEFT JOIN voucher_Type vt on vt.v_Type = pChallan.v_Type \n"
                    + "WHERE pChallan.docId = :docId \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                description = DmShared.xNull(rsTemp.getString("description"));
                shortName = DmShared.xNull(rsTemp.getString("short_Name"));
            }
            rsTemp.close();
            psTemp.close();
            //<editor-fold defaultstate="collapsed" desc="Save Log">
            GRN_Mdl oldMdl = null;
            //create new reason mode in case of print, new
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                //check if latest record's old data is saved in moverec
                oldMdl = (GRN_Mdl) DmServer.getSsnOldData(getClass().getName(), this);
                if (oldMdl == null || !DmShared.xNull(oldMdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(strKey))) {
                    throw new DmException(0, "Old Model data is invalid");
                } else {
                    if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                        mrTemp.setSharedPreparedBy(DmShared.xNull(oldMdl.getSharedPreparedBy()));
                        mrTemp.setSharedPreparedDate(DmShared.xNull(oldMdl.getSharedPreparedDate()));
                        mrTemp.setSharedPreparedIP(DmShared.xNull(oldMdl.getSharedPreparedIP()));
                    }
                    blnLog = true;
                }
            }
            if (blnLog) {

                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(DmShared.xNull(strKey));
                modifyData_Mdl.setKeyName_1(DmShared.xNull(recId));
                modifyData_Mdl.setKeyDescription_1(DmShared.xNull(recId));
                modifyData_Mdl.setModuleName(new pmPharmacy().moduleName);
                modifyData_Mdl.setMenuName(new pmPharmacy().GRN.caption);
                if (controlText != null) {
                    modifyData_Mdl.setControlText(controlText.getValue());
                }
                modifyData_Mdl.setModelClassName(GRN_Mdl.class.getName());
                modifyData_Mdl.setVrTypeDescription(description);
                modifyData_Mdl.setVrTypeSName(DmShared.xNull(shortName));
                modifyData_Mdl.setTableName("pChallan");
                //saving old record for edit, delete
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    modifyData_Mdl.setOldData(DmServer.convertToJson(oldMdl, new TypeToken<GRN_Mdl>() {
                    }));
                }
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                    modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<GRN_Mdl>() {
                    }));
                }
//                else if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
//                    oldMdl.setSharedCancelledBy(DmServer.getSsnUserName(this));
//                    oldMdl.setSharedCancelledDate(DmServer.formatTimestamp(discardTimestamp));
//                    modifyData_Mdl.setNewData(DmServer.convertToJson(oldMdl, new TypeToken<GRN_Mdl>() {
//                    }));
//                }
                dataman.dmToolkit.modifyReason.server.ModifyReason_Server.saveNew(modifyData_Mdl, modifyReasonMdl, this);
            }
            cnTemp.close();
            //</editor-fold>

        } catch (Exception e) {
            System.out.println("saveLog: Exception - " + this.getClass());
            e.printStackTrace();
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

    }

    @Override
    public DmResult<String> getD65DataDeatails() {
        DmResult<String> dmrTemp = new DmResult();
        String sqlQuery = "";
        Connection cnTemp = null;
        Connection stagingCnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String databaseName = DmServer.executeScalar("select bc_databaseName from enviroCompany ", cnTemp);
            stagingCnTemp = DmServer.getConnection(databaseName);
            dmrTemp.setData(StockReciveSync.saveStockReciveSync(cnTemp, stagingCnTemp));
            cnTemp.close();
            stagingCnTemp.close();
        } catch (Exception e) {
            try {
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
    public DmResult<String>  getStockIssueData() {
         DmResult<String> dmrTemp = new DmResult();
        String sqlQuery = "";
        Connection cnTemp = null;
        Connection stagingCnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String databaseName = DmServer.executeScalar("select bc_databaseName from enviroCompany ", cnTemp);
            stagingCnTemp = DmServer.getConnection(databaseName);
            dmrTemp.setData(StockReciveSync.saveStockIssueSync(cnTemp, stagingCnTemp));
            cnTemp.close();
            stagingCnTemp.close();
        } catch (Exception e) {
            try {
                
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

}
