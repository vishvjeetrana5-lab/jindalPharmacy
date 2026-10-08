package aarogya.inventory.purchaseOrder.server;

import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.purchaseOrder.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseOrder.shared.Find_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Mdl;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Vld;
import aarogya.inventory.purchaseOrder.shared.TxtHAmendmentOrder_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHItemName_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHQuotationNo_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHSupplierPT_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STShared;

public class PurchaseOrder_SrvImpl extends DmRSSPrivate implements PurchaseOrder_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCat = "'" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','" + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "'";
    private final String voucherTypeFieldName = "po.v_Type", voucherDateFieldName = "po.v_Date";
    private String indentRequired;
    String isSystemAdminUser = "";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_ORDER + "', '" + pvc.PURCHASE_ORDER_UNREGISTERED + "', '" + pvc.SERVICE_ORDER + "', '" + pvc.SERVICE_ORDER_UNREGISTERED + "')"
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
    public DmResultPageing<List<TxtHSupplierPT_Mdl>> getTxtHSupplierPT(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHSupplierPT_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplierPT_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSupplierDetail("", voucherCategory, flagFilterByVoucherCategory, v_Type, pageing, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            cnTemp.close();
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
    public DmResultPageing<List<TxtHQuotationNo_Mdl>> getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date, String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHQuotationNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHQuotationNo_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getQuotationDetail("", isShowUsedQuotationsAlso, supplierCode, v_Date, voucherCategory, helper, blnToEndOfFile, cnTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setData(lstTemp);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            cnTemp.close();
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
    public DmResultPageing<List<TxtHAmendmentOrder_Mdl>> getTxtHAmendmentOrder(String v_Date, String subCode, String amdOrdDocId, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAmendmentOrder_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAmendmentOrder_Mdl> lstTemp = new ArrayList<>();
        TxtHAmendmentOrder_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(subCode).longValue() > 0) {
                condition += " and po.subCode= :subCode ";
            }

            sqlWithParam.setSQL(" Select MAX(po.docId) as docId, MAX(po.recId) as recId, MAX(po.v_Date) as v_Date, "
                    + " MAX(po.subCode) as subCode, MAX(po.refNo) as refNo, MAX(po.refDate) as refDate, "
                    + " MAX(po.amdOrdDocId) as amdOrdDocId "
                    + " from pOrder po "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type "
                    + " LEFT JOIN pOrderDetail pod ON pod.docId = po.docId "
                    + " where po.approvedBy <> '' "
                    + " and po.v_Date <= :v_Date "
                    + " and (po.amdOrdDocId = :amdOrdDocId  "
                    + "	OR po.amdOrdDocId IS  NULL ) "
                    + " and vt.category IN ( " + vCat + " ) " + condition
                    + " and pod.isStatus = '0' "
                    + " and (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0)) > 0 "
                    + " group by po.docId ");
            sqlWithParam.setString("v_Date", DmShared.dNull(v_Date));
            sqlWithParam.setString("amdOrdDocId", DmShared.xNull(amdOrdDocId));
            if (DmShared.iNull(subCode).longValue() > 0) {
                sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            }
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAmendmentOrder_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setSharedPreparedBy(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setPartyCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setPartyOrdNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setPartyOrdDt(DmShared.dNull(rsTemp.getDate("refDate")));
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

    @Override
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type, String voucherCategory, String vDate, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", itemCatFilteration = "";
        String filterItemType = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }
            /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            indentRequired==(0) for indent condition depend on Item Master, 
            (1) for Indent of all items is mandatory , 
            (2) for indent is not madatory for any item whether it is in item mast or not 
            ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
            if (DmShared.xNull(indentRequired).equalsIgnoreCase("0")) {
                condition += " AND im.isIndentMandatory =0 ";
            } else if (DmShared.xNull(indentRequired).equalsIgnoreCase("1")) {
                condition += " and 1=2 ";
            }

            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , im.manualCode"
                    + " , im.hsnCode"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn, im.purchaseRate"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , im.propertyList"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , ISNULL(im.isBestBefore, 0) AS isBestBefore"
                    + " , im.tl_ExcessPer"
                    + " , im.tl_ShortagePer"
                    + " , hsn.hsn,pl.rate as approvedPurchaseRate,im.remark "
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN priceList pl ON pl.itemCode = im.code and pl.site_Code=:site_Code and :vDate between applicableFrom and applicableTo "
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + " LEFT JOIN hsnMast hsn ON hsn.code = im.hsnCode"
                    + " WHERE im.IsActive = 1"
                    + " AND (im.filterItemType = :filterItemType or im.itemType = :itemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND im.isQuotationMandatory = 0 " + condition);

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_ORDER)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_ORDER_UNREGISTERED)) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                sqlWithParam.setString("itemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER_UNREGISTERED)) {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
                sqlWithParam.setString("itemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
            }

            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setDate("vDate", DmServer.convertDate(vDate));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));

                if (DmShared.vNull(rsTemp.getString("approvedPurchaseRate")).doubleValue() > 0) {
                    listNode.setPurchaseRate(DmShared.xNull(rsTemp.getString("approvedPurchaseRate")));
                    listNode.setApprovedRateYN("1");
                } else {
                    listNode.setPurchaseRate(DmShared.xNull(rsTemp.getString("purchaseRate")));
                }

                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.getToleranceMdl().setTl_ExcessPer(DmServer.vNull(rsTemp.getString("tl_ExcessPer")));
                listNode.getToleranceMdl().setTl_ShortagePer(DmServer.vNull(rsTemp.getString("tl_ShortagePer")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    @Override
    public DmResult<PurchaseOrder_Mdl> getItemDetail(String v_Type, String QuotationDocId, String voucherCategory, String location, String billDate, String isStateOrUt, List<KeyFieldExtended> keyFields) {
        DmResult<PurchaseOrder_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", condition1 = "", condition2 = "", itemCatFilteration = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        PurchaseOrder_Mdl mrTemp = new PurchaseOrder_Mdl();
        GrdItemDetail_Mdl node;
        ServiceDetail_Mdl serviceDetail_Mdl;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition2 += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                sqlQuery = " SELECT "
                        + "   qd.itemCode"
                        + " , im.name AS itemName"
                        + " , qd.quantity"
                        + " , qd.unitCode AS purchaseUnitCode \n"
                        + " , um.unit AS purchaseUnitName"
                        + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces \n"
                        + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate \n"
                        + " , ums.unit AS skuCode"
                        + " , ums.unit AS skuName"
                        + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces \n"
                        + " , qd.deliveryDate AS reqDate"
                        + " , qd.rate"
                        + " , qd.oh_Amt_Gross"
                        + " , im.propertyList \n"
                        + " , im.tl_ExcessPer"
                        + " , im.tl_ShortagePer"
                        + " , im.isBestBefore"
                        + " , im.itsBasedOn"
                        + " , qd.colourCode"
                        + " , qd.sizeCode \n"
                        + " , icm.name AS colourName"
                        + " , ism.name AS sizeName"
                        + " , qd.docId AS quotationDocId"
                        + " , qd.v_SNo AS quotationV_SNo \n"
                        + " , qd.itemMakeCode, imm.companyName as itemMakeName"
                        + " , qd.hsnCode, im.itemGSTNature  \n"
                        + " , hsn.hsn as hsnNo "
                        + " , qd.isManualGstType "
                        + " , qd.isManualTax "
                        + " , qd.oh_Amt_Gross\n"
                        + " , qd.oh_Amt_Discount "
                        + " , qd.oh_at_Discount "
                        + " , qd.oh_Amt_Taxable "
                        + " , qd.oh_at_SGST\n"
                        + " , qd.oh_Amt_SGST "
                        + " , qd.oh_at_UGST "
                        + " , qd.oh_Amt_UGST "
                        + " , qd.oh_at_CGST "
                        + " , qd.oh_Amt_CGST \n"
                        + " , qd.oh_at_IGST "
                        + " , qd.oh_Amt_IGST "
                        + " , qd.oh_Amt_SubTotal "
                        + " , qd.oh_Amt_Net "
                        + " FROM quotationDetail qd \n"
                        + " LEFT JOIN itemMast im ON qd.itemCode = im.code \n"
                        + " LEFT JOIN unitMast um ON qd.unitCode = um.code \n"
                        + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                        + " LEFT JOIN itemColourMast icm ON icm.code = qd.colourCode \n"
                        + " LEFT JOIN itemSizeMast ism ON ism.code = qd.sizeCode \n"
                        + " LEFT JOIN itemMakeMast imm ON imm.code = qd.itemMakeCode \n "
                        + " LEFT JOIN hsnMast hsn on hsn.code = qd.hsnCode \n "
                        + " WHERE qd.docId = :docId and (im.filterItemType=:filterItemType or im.itemType=:itemType) \n "
                        + " AND ISNULL(qd.isFooter, 0) = 0 \n"
                        + condition2
                        + " ORDER BY im.name ";
            } else {
                //<editor-fold defaultstate="collapsed" desc="from Indent">
                condition = "";
                condition1 = "";
                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                            && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {

                        if (!condition.trim().equals("")) {
                            condition += " OR ";
                        }
                        condition += "  (id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "'";
                        condition += "  AND id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "')";

                        if (!condition1.trim().equals("")) {
                            condition1 += " OR ";
                        } else {
                            condition1 += " WHERE ";
                        }
                        condition1 += " (pd.indentDocId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "')";
                    }
                }

                sqlQuery = "SELECT "
                        + " id.itemCode,"
                        + " MAX(im.tl_ExcessPer) as tl_ExcessPer, "
                        + " MAX(im.tl_ShortagePer) as tl_ShortagePer, \n"
                        + " MAX(im.name) AS itemName,"
                        + " MAX(im.purchaseUnit) AS purchaseUnitCode,"
                        + " MAX(um.unit) AS purchaseUnitName, \n"
                        + " ISNULL(MAX(um.decimalPlaces),0) AS purchaseUnitDecimalPlaces,"
                        + " ISNULL(MAX(im.purchaseUnitConversionRate), 0) AS purchaseUnitConversionRate, \n"
                        + " MAX(ums.code) AS skuCode,"
                        + " MAX(ums.unit) AS skuName,"
                        + " ISNULL(MAX(ums.decimalPlaces),0) AS skuDecimalPlaces, \n"
                        + " Max(im.propertyList) AS propertyList,"
                        + " Max(im.isBestBefore) AS isBestBefore,"
                        + " MAX(id.reqDate) AS reqDate,"
                        + " MAX(id.v_SNo) AS v_SNo, \n"
                        + " MAX(im.hsnCode) as hsnCode, "
                        + " MAX(hsn.hsn) as hsnNo, "
                        + " MAX(im.itemGSTNature) as itemGSTNature, "
                        + " MAX(im.itsBasedOn) as itsBasedOn, "
                        + " max(im.purchaseRate) AS rate, \n"
                        + " 0 AS oh_Amt_Gross,"
                        + " NULL AS quotationDocId,"
                        + " NULL AS quotationV_SNo, \n"
                        + " SUM(CASE WHEN ISNULL(pid.adjQty, 0) = 0 THEN ISNULL(id.aprQty, 0) ELSE ISNULL(id.aprQty, 0) - ISNULL(pid.adjQty, 0) END) AS quantity, \n "
                        + " MAX(id.colourCode) as colourCode,"
                        + " MAX(icm.name) as colourName,"
                        + " MAX(id.sizeCode) as sizeCode,"
                        + " MAX(ism.name) as sizeName, \n "
                        + " max(id.itemMakeCode) as itemMakeCode, max(imm.companyName) as itemMakeName,max(pl.rate) as approvedPurchaseRate, \n"
                        + " MAX(isNull(id.remark,'')+' '+isNull(im.remark,'')) as remark \n "
                        + " FROM indentDetail id \n"
                        + " LEFT JOIN indent i ON id.docId = i.docId \n"
                        + " LEFT JOIN( \n"
                        + "        SELECT SUM(pd.orderedQty) AS adjQty, pd.indentDocId, pd.indentV_SNo \n"
                        + "        FROM pOrderIndentDetail pd" + condition1
                        + "        GROUP BY pd.indentDocId, pd.indentV_SNo \n"
                        + " ) AS pid ON id.docId = pid.indentDocId AND id.v_Sno = pid.indentV_SNo \n"
                        + " LEFT JOIN itemMast AS im ON id.itemCode = im.code \n "
                        + " LEFT JOIN priceList pl ON pl.itemCode = im.code and pl.site_Code=:site_Code and :vDate between applicableFrom and applicableTo "
                        + " LEFT JOIN unitMast AS um ON im.purchaseUnit = um.code \n "
                        + " LEFT JOIN unitMast AS ums ON im.sku = ums.code \n "
                        + " LEFT JOIN itemColourMast icm ON icm.code = id.colourCode \n "
                        + " LEFT JOIN itemSizeMast ism ON ism.code = id.sizeCode \n "
                        //                        + " LEFT JOIN varietyMast vm ON vm.code = id.varietyCode "
                        //                        + " LEFT JOIN ClassificationMast cm ON cm.code = id.classificationCode "
                        + " LEFT JOIN itemMakeMast imm ON imm.code = id.itemMakeCode \n "
                        + " LEFT JOIN hsnMast hsn on hsn.code = im.hsnCode \n"
                        + " WHERE id.isStatus <> 1  and (im.filterItemType=:filterItemType or im.itemType=:itemType) \n "
                        + " AND ISNULL(i.approvedBy, '') <> '' \n"
                        + " AND (" + condition + ")" + condition2
                        + " GROUP BY id.itemCode, id.colourCode, id.sizeCode,  id.itemMakeCode \n "
                        + " ORDER BY itemName ";
                //</editor-fold>
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.iNull(QuotationDocId).toString());
            } else {
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setDate("vDate", DmServer.convertDate(billDate));
            }
            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_ORDER)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_ORDER_UNREGISTERED)) {
                psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("itemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER_UNREGISTERED)) {
                psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("itemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));

                node.getToleranceMdl().setTl_ExcessPer(DmServer.vNull(rsTemp.getString("tl_ExcessPer")));
                node.getToleranceMdl().setTl_ShortagePer(DmServer.vNull(rsTemp.getString("tl_ShortagePer")));

                //Item Detail
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsnNo(DmShared.xNull(rsTemp.getString("hsnNo")));

                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                if (DmShared.vNull(rsTemp.getString("approvedPurchaseRate")).doubleValue() > 0) {
                    node.setRate(DmServer.vNull(rsTemp.getString("approvedPurchaseRate"), "0.00"));
                    node.setApprovedRateYN("1");
                } else {
                    node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                }
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setQuotationDocId(DmShared.xNull(rsTemp.getString("quotationDocId")));
                node.setQuotationV_SNo(DmShared.xNull(rsTemp.getString("quotationV_SNo")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                double purchaseUnitConversionRate = DmShared.xNull(QuotationDocId).trim().equalsIgnoreCase("") ? DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate")) : 1;
                double orderQty = DmShared.vNull(rsTemp.getString("quantity")) / purchaseUnitConversionRate;
                String decimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).intValue());
                node.setOrderQuantity(DmServer.vNull(DmServer.vNull(orderQty, decimalFormat)));

                double skuQty = DmShared.vNull(rsTemp.getString("quantity")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                ItemProperty_Mdl ippm_Mdl = new ItemProperty_Mdl();
                ippm_Mdl.setItemCode(node.getSharedKeyFieldCode());
                ippm_Mdl.setColourCode(node.getColorCode());
                ippm_Mdl.setColourName(node.getColor());
                ippm_Mdl.setSizeCode(node.getSizeCode());
                ippm_Mdl.setSizeName(node.getSize());
                ippm_Mdl.setItemMakeCode(node.getItemMakeCode());
                ippm_Mdl.setItemMakeName(node.getItemMakeName());

                node.setProperties(DmShared.xNull(ippm_Mdl.getPropertiesCsv()));

                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail;
                if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                    /*==============================<< Get Indent detail (via enquiryDocId & v_SNo) >>===============================*/
                    resultIndentDetail = getIndentDetail(false, DmShared.xNull(QuotationDocId), DmShared.iNull(rsTemp.getString("quotationV_SNo")).toString(), null, ippm_Mdl, cnTemp);
                } else {
                    /*==============================<< Get Indent detail (via list & ItemCode ) >>===============================*/
                    resultIndentDetail = getIndentDetail(false, "", "", keyFields, ippm_Mdl, cnTemp);
                }

                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
                }

                if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                    //OH
                    node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                    node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                    node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                    node.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                    node.setItemValue(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                    //GST
                    node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                    node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                }

                //ITS
                if (!node.isManualGstType()) {
                    DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(node.getSharedKeyFieldCode(), node.getRate(), node.getItsBasedOn(), DmShared.xNull(location), "", false, cnTemp);
                    if (dmrIts != null) {
                        for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                            String taxPer = "";

                            taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                            if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isStateOrUt).intValue() == 1)) {
                                node.setSgstUgstPer(DmShared.xNull(taxPer));
                                node.setSgstPer(DmShared.xNull(taxPer));

                            } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isStateOrUt).intValue() == 1)) {
                                node.setSgstUgstPer(DmShared.xNull(taxPer));
                                node.setUgstPer(DmShared.xNull(taxPer));

                            } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                                node.setCgstPer(DmShared.xNull(taxPer));

                            } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                                node.setIgstPer(DmShared.xNull(taxPer));

                            }
                        }
                        node.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                        node.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                    }
                } else {
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
                lstNode.add(node);
            }
            /*==============================<< Get Service detail >>===============================*/
            if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                sqlQuery = "SELECT\n"
                        + "  pcd.v_SNo\n"
                        + " , pcd.itemCode\n"
                        + " , pcd.hsnCode\n"
                        + " , pcd.itemGSTNature\n"
                        + " , im.name AS itemName\n"
                        + " , im.smfCode\n"
                        + " , im.itsBasedOn\n"
                        + " , pcd.unitCode \n"
                        + " , pcd.quantity\n"
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
                        + " FROM quotationDetail pcd\n"
                        + " LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                        + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                        + " LEFT JOIN hsnMast hsn ON hsn.code = pcd.hsnCode \n"
                        + " LEFT JOIN unitMast um ON pcd.unitCode = um.code\n"
                        + " WHERE pcd.docId = :docId \n"
                        + " AND ISNULL(pcd.isFooter, 0) = 2\n"
                        + " ORDER BY pcd.v_SNo\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(QuotationDocId).toString());
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    serviceDetail_Mdl = new ServiceDetail_Mdl();
                    serviceDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                    serviceDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                    serviceDetail_Mdl.setSmfCode(DmShared.xNull(rsTemp.getString("smfCode")));
                    serviceDetail_Mdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                    serviceDetail_Mdl.setV_Sno(DmShared.xNull(rsTemp.getString("v_SNo")));
                    serviceDetail_Mdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                    serviceDetail_Mdl.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                    serviceDetail_Mdl.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                    serviceDetail_Mdl.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                    serviceDetail_Mdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                    serviceDetail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                    serviceDetail_Mdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                    serviceDetail_Mdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                    serviceDetail_Mdl.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                    serviceDetail_Mdl.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                    serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                    serviceDetail_Mdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                    serviceDetail_Mdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                    serviceDetail_Mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                    serviceDetail_Mdl.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                    if (!DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")).equalsIgnoreCase("")) {
                        serviceDetail_Mdl.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")));
                        serviceDetail_Mdl.setServiceEvalTypeName(DmShared.xNull(DmEnum.ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypeCode")).intValue()).getName()));
                        serviceDetail_Mdl.setServiceEvalRate(DmServer.vNull(rsTemp.getString("serviceEvalRate"), "0.00"));
                    }

                    //ITS
                    if (!serviceDetail_Mdl.isManualGstType()) {
                        DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(serviceDetail_Mdl.getSharedKeyFieldCode(), serviceDetail_Mdl.getRate(), serviceDetail_Mdl.getItsBasedOn(), DmShared.xNull(location), "", false, cnTemp);
                        if (dmrIts != null) {
                            for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                                String taxPer = "";

                                taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isStateOrUt).intValue() == 1)) {
                                    serviceDetail_Mdl.setSgstOrUgstRate(DmShared.xNull(taxPer));
                                    serviceDetail_Mdl.setSgstRate(DmShared.xNull(taxPer));

                                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isStateOrUt).intValue() == 1)) {
                                    serviceDetail_Mdl.setSgstOrUgstRate(DmShared.xNull(taxPer));
                                    serviceDetail_Mdl.setUgstRate(DmShared.xNull(taxPer));

                                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                                    serviceDetail_Mdl.setCgstRate(DmShared.xNull(taxPer));

                                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                                    serviceDetail_Mdl.setIgstRate(DmShared.xNull(taxPer));

                                }
                            }
                            serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                            serviceDetail_Mdl.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                        }
                    } else {
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

                    serviceDetail_Mdl.setGstRate(DmShared.vNull(DmShared.vNull(serviceDetail_Mdl.getCgstRate())
                            + DmShared.vNull(serviceDetail_Mdl.getSgstOrUgstRate())
                            + DmShared.vNull(serviceDetail_Mdl.getIgstRate())).toString());

                    mrTemp.getGrdServiceDetail().add(serviceDetail_Mdl);
                }
            }
            mrTemp.getGrdItemDetail().addAll(lstNode);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm_Mdl) {
        DmResult<List<GrdIndentDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Indent detail (via list) >>===============================*/
            DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(false, "", "", keyFields, ippm_Mdl, cnTemp);
            if (resultIndentDetail.isStatus()) {
                dmrTemp.setData(resultIndentDetail.getData());
            } else {
                throw new DmException(0, "No record found.");
            }

            dmrTemp.setStatus(true);
            cnTemp.close();
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

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(boolean isMoveRec, String docId, String v_SNo, List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm_Mdl, Connection cnTemp) {
        DmResult<List<GrdIndentDetail_Mdl>> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        ItemProperty_Mdl ipm;
        String sqlQuery = "", conditionLines = "", condition = "", table = "", pendingQty = "", adjustedQty = "";
        List<GrdIndentDetail_Mdl> lstNode = new ArrayList<>();
        GrdIndentDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {

            if (!DmShared.xNull(docId).equalsIgnoreCase("") && !DmShared.xNull(v_SNo).equalsIgnoreCase("")) {

                if (isMoveRec) {
                    table = " pOrderIndentDetail ";
                    pendingQty = " , ISNULL(tb.orderedQty, 0) AS pendingQty";
                    adjustedQty = " , ISNULL(id.orderedQty, 0) - ISNULL(tb.orderedQty, 0) AS adjQty";
                } else {
                    table = " quotationIndentDetail ";
                    pendingQty = " , ISNULL(id.aprQty, 0) - ISNULL(id.orderedQty, 0) AS pendingQty";
                    adjustedQty = " , ISNULL(id.orderedQty, 0) AS adjQty";
                    condition += " AND ISNULL(id.isStatus, 0) = 0";
                }

                sqlQuery = "SELECT"
                        + "   id.docId AS indentDocId"
                        + " , id.v_SNo AS indentV_Sno"
                        + " , id.reqDate"
                        + " , id.remark"
                        + " , id.aprQty" + adjustedQty + pendingQty
                        + " , i.recId AS indentNo"
                        + " , i.v_Date AS indentDate"
                        + " , um.unit AS indentSkuName"
                        + " , ISNULL(um.decimalPlaces,0) AS indentSkuDecimalPlaces"
                        + " , id.colourCode"
                        + " , icm.name AS colourName"
                        + " , id.sizeCode"
                        + " , ism.name AS sizeName"
                        //                        + " , id.varietyCode as varietyCode "
                        //                        + " , vm.name as varietyName"
                        //                        + " , id.classificationCode as classificationCode "
                        //                        + " , cm.name as classificationName "
                        + " , id.itemMakeCode as itemMakeCode "
                        + " , imm.companyName as itemMakeName,im.purchaseRate,0 as purchaseDiscountPer "
                        + " FROM " + table + " tb"
                        + " LEFT JOIN indentDetail id ON tb.indentDocId = id.docId AND tb.indentV_SNo = id.v_SNo"
                        + " LEFT JOIN itemMast AS im ON id.itemCode = im.code"
                        + " LEFT JOIN unitMast AS um ON im.sku = um.code"
                        + " LEFT JOIN itemColourMast icm ON id.colourCode = icm.code"
                        + " LEFT JOIN itemSizeMast ism ON id.sizeCode = ism.code"
                        + " LEFT JOIN indent i ON id.docId = i.docId"
                        //                        + " LEFT JOIN varietyMast vm ON vm.code = id.varietyCode "
                        //                        + " LEFT JOIN ClassificationMast cm ON cm.code = id.classificationCode "
                        + " LEFT JOIN itemMakeMast imm ON imm.code = id.itemMakeCode "
                        + " WHERE tb.docId = :docId"
                        + " AND tb.v_SNo = :v_SNo" + condition
                        + " ORDER BY id.v_Sno";

            } else if (keyFields != null && keyFields.size() > 0) {

                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                            && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {
                        if (!conditionLines.trim().equals("")) {
                            conditionLines += " OR ";
                        }
                        conditionLines += " ( id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "'";
                        conditionLines += " AND id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "')";
                    }
                }
                if (!DmShared.xNull(conditionLines).trim().equalsIgnoreCase("")) {
                    condition += " AND (" + conditionLines + ")";
                } else {
                    condition += " AND 1 = 2";
                }

                if (!DmShared.xNull(ippm_Mdl.getColourCode()).equalsIgnoreCase("")) {
                    condition += " AND id.colourCode = '" + DmShared.xNull(ippm_Mdl.getColourCode()) + "'";
                }

                if (!DmShared.xNull(ippm_Mdl.getSizeCode()).equalsIgnoreCase("")) {
                    condition += " AND id.sizeCode = '" + DmShared.xNull(ippm_Mdl.getSizeCode()) + "'";
                }

//                if (!DmShared.xNull(ippm_Mdl.getClassificationCode()).equalsIgnoreCase("")) {
//                    condition += " AND id.classificationCode = '" + DmShared.xNull(ippm_Mdl.getClassificationCode()) + "'";
//                }
//
//                if (!DmShared.xNull(ippm_Mdl.getVarietyCode()).equalsIgnoreCase("")) {
//                    condition += " AND id.varietyCode = '" + DmShared.xNull(ippm_Mdl.getVarietyCode()) + "'";
//                }
                if (!DmShared.xNull(ippm_Mdl.getItemMakeCode()).equalsIgnoreCase("")) {
                    condition += " AND id.itemMakeCode = '" + DmShared.xNull(ippm_Mdl.getItemMakeCode()) + "'";
                }

                sqlQuery = "SELECT"
                        + "   id.docId AS indentDocId"
                        + " , id.v_SNo AS indentV_Sno"
                        + " , id.reqDate"
                        + " , id.remark"
                        + " , id.aprQty"
                        + " , id.orderedQty AS adjQty"
                        + " , ISNULL(id.aprQty, 0) - ISNULL(id.orderedQty, 0) AS pendingQty"
                        + " , i.recId AS indentNo"
                        + " , i.v_Date AS indentDate"
                        + " , um.unit AS indentSkuName"
                        + " , ISNULL(um.decimalPlaces,0) AS indentSkuDecimalPlaces"
                        + " , id.colourCode"
                        + " , icm.name AS colourName"
                        + " , id.sizeCode "
                        + " , ism.name AS sizeName "
                        //                        + " , id.classificationCode, cm.name as classificationName "
                        //                        + " , id.varietyCode, vm.name as varietyName "
                        + " , id.itemMakeCode, imm.companyName as itemMakeName,im.purchaseRate,0 as purchaseDiscountPer "
                        + " FROM indentDetail id "
                        + " LEFT JOIN indent i ON id.docId = i.docId "
                        + " LEFT JOIN itemMast AS im ON id.itemCode = im.code "
                        + " LEFT JOIN unitMast AS um ON im.sku = um.code "
                        + " LEFT JOIN itemColourMast icm ON id.colourCode = icm.code "
                        + " LEFT JOIN itemSizeMast ism ON id.sizeCode = ism.code "
                        //                        + " LEFT JOIN varietyMast vm ON vm.code = id.varietyCode "
                        //                        + " LEFT JOIN ClassificationMast cm ON cm.code = id.classificationCode "
                        + " LEFT JOIN itemMakeMast imm ON imm.code = id.itemMakeCode "
                        + " WHERE id.itemCode = :itemCode"
                        + " AND ISNULL(id.isStatus, 0) = 0" + condition
                        + " ORDER BY id.v_Sno";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(docId).equalsIgnoreCase("") && !DmShared.xNull(v_SNo).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(docId));
                psTemp.setString("v_SNo", DmShared.xNull(v_SNo));
            }
            if (keyFields != null && keyFields.size() > 0) {
                psTemp.setString("itemCode", DmShared.xNull(ippm_Mdl.getItemCode()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdIndentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indentNo")));
                node.setIndentVSNo(DmShared.xNull(rsTemp.getString("indentV_Sno")));
                node.setIndentDate(DmShared.dNull(rsTemp.getDate("indentDate")));
                node.setRequiredDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setIndentSKU(DmShared.xNull(rsTemp.getString("indentSkuName")));
                node.setDecimalPlaces(DmShared.xNull(rsTemp.getString("indentSkuDecimalPlaces")));
                node.setOrderQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setAdjustedQty(DmServer.vNull(rsTemp.getString("adjQty")));
                node.setIndentQty(DmServer.vNull(rsTemp.getString("aprQty")));
                ipm = new ItemProperty_Mdl();
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setProperties(DmShared.xNull(ipm.getPropertiesCsv()));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setLastPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate")));
                node.setLastPurchaseDiscount(DmServer.vNull(rsTemp.getString("purchaseDiscountPer")));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Approval">
    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, boolean isUnApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        boolean isAdvanceReqGen = false;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            //-------------------< Check If Advance Entry Is Created >-----------------------------------
            isAdvanceReqGen = DmShared.iNull(DmServer.executeScalar("select docId from advanceReqEntry "
                    + "where podocid = " + DmShared.iNull(docId).toString() + " and isNull(isStatus,'')='' ",
                    cnTemp)).longValue() > 0 ? true : false;

            if (isAdvanceReqGen) {
                throw new Exception("Advanced Request Entry generated. you can not unapprove.");
            }
            ////////////////////////////////////////////////////////////////////////////////////////////

            if (isApprove) {
                STApprovals.purchaseOrderApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp, this);
            } else if (isUnApprove) {

                STServer.checkUnapproveUptoDays(DmServer.getSsnSiteCode(this), docId, cnTemp);

                STApprovals.purchaseOrderApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp, this);
            } else {
                throw new Exception("Unauthorized user to unapprove.");
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

    private WalkThroughDefault_Mdl getWalkThroughFor(PurchaseOrder_Mdl mrTemp) {
        double netAmount = 0.0;
        //OverHead_Mdl ohMdl = OverHead_Shared.getOHByUniqueId("PT", mrTemp.getOverHeadList()); <AG> TBR
//        if (ohMdl != null) {
//            netAmount = DmShared.vNull(ohMdl.getAmount());
//        }
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();
        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto(DmServer.vNull(netAmount, "0.00"));
        walkThroughDefault.setvType(mrTemp.getVoucherTypeTag());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    STApprovals.purchaseOrderApproval(walkThroughEnum, keyCode, userName, cnTemp, PurchaseOrder_SrvImpl.this);
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
            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));
            /*==============================<< Get default voucher_Type >>===============================*/
            defaultValue.setVoucherType(getVoucherType("", cnTemp));

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

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.isIncludeTaxInCost,isNull(e.indentRequired,0) as indentRequired \n"
                    + "FROM enviro e\n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                indentRequired = DmShared.xNull(rsTemp.getString("indentRequired"));
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

            isSystemAdminUser = DmServer.executeScalar("select isAdmin from " + DmServer.dbCompany + ".dbo.userMast where user_Name =  '" + DmServer.getSsnUserName(this) + "' ", cnTemp);

            defaultValue.setIsSystemAdminUser(isSystemAdminUser);

            defaultValue.setUserName(DmServer.getSsnUserName(this));
            /*==============================<< ************************* >>====================================*/

            dmrTemp.setData(defaultValue);
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
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(partyType).trim().equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                psTemp.setString("category", pvc.PURCHASE_ORDER);
            } else {
                psTemp.setString("category", pvc.PURCHASE_ORDER_UNREGISTERED);
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                voucherType_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                voucherType_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                voucherType_Mdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResult<String> saveRecord(PurchaseOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        boolean blnAdd = false;
        DmSavePoint spBegin = null;
        String sqlQuery, userName = "", siteCode = "", roundOffItemCode = "", roundOffItemHsnCode = "", roundOffItemGSTNature = "", roundOffGstTypeCode = "";
        ResultSet rsTemp = null;
        ServiceDetail_Mdl serviceDetail_Mdl;
        dmrTemp.setData("");

        try {
            PurchaseOrder_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0]);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Check for Approvedby if already approved >>====================================*/
                sqlQuery = "select approvedBy  FROM pOrder WHERE  docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                        throw new DmException(0, "Record is approved. You can't edit this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                /*==============================<< Check if document is in use >>====================================*/
                STServer.chkIfDocumentUsed("poDocId", "pChallanDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                STServer.chkIfDocumentUsed("poDocId", "pBillDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);
                STServer.chkIfDocumentUsed("poDocId", "gateAdviceDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                /*==============================<< Check for LatestEntry >>====================================*/
                sqlQuery = "SELECT u_EntDt_LatestLine FROM pOrder WHERE docId = :docId";
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
            }

            /*==============================<< Check for duplicate party order no >>====================================*/
            if (!DmShared.xNull(mrTemp.getRefNo()).trim().equalsIgnoreCase("")) {
                sqlQuery = " SELECT COUNT(po.docId) AS cnt "
                        + " FROM pOrder po with (nolock) "
                        + " INNER JOIN voucher_type vt ON vt.v_Type = po.v_Type "
                        + " WHERE po.refNo = :refNo "
                        + " AND po.subCode=:subCode"
                        + " AND po.v_Prefix = :v_Prefix "
                        + " AND po.docId <> :docId "
                        + " AND vt.category IN ('" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "')";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierTag()));
                psTemp.setString("v_Prefix", voucherPrefix);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        throw new DmException(400, "Entry already exists with this Ref. No.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< Check if Quotation deleted >>====================================*/
            if (!DmShared.xNull(mrTemp.getQuotationNoTag()).equalsIgnoreCase("")) {
                sqlQuery = " select count(DocId) as cnt from quotation where docId=:docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getQuotationNoTag()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (rsTemp.getLong("cnt") == 0) {
                        throw new DmException(100, "Quotation has been deleted,please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< Check items for Quotation item change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM quotationDetail qd " + DmShared.xNull(mrTemp.getQuotationItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Quotation has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check if order used >>====================================*/
            sqlQuery = "select docId "
                    + "from pOrderDetail "
                    + "where docId = :docId and isNull(challanedQty,0) <> 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new Exception("Document is in use. you can not unapprove.");
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for INDENT Item change >>====================================*/
            if (!DmShared.xNull(mrTemp.getIndItemChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM indentDetail ind " + DmShared.xNull(mrTemp.getIndItemChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Indent has been changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< SAVE & UPDATE >>====================================*/
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdOrder(DmServer.getRecId("pOrder", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdOrder(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdOrder().getRecIdValue()));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), mrTemp.getVoucherTypeTag(), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
                /*----- Save mongoDB files -----*/
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
            } else {
                DmServer.getRecId("pOrder", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdOrder(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

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
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

            if (blnAdd) {
                sqlQuery = "INSERT INTO pOrder ("
                        + "       docId, recId, recIdPrefix, v_Type, v_No, v_Prefix, v_Date, refNo, refDate, subCode, \n"
                        + "       consignee, partyType, gsTin, gstinDate,rcmApplicable, placeOfSupply, amdOrdDocId, \n"
                        + "       isShowUsedQuotationsAlso, site_Code, wtaCode, guiConfigCode, preparedBy, preparedDate, \n"
                        + "       u_EntDt_LatestLine, transferDt, \n"
                        + "       oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                        + "       oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                        + "       oh_Amt_SubTotal, oh_Amt_Total, oh_Amt_RoundOff, oh_Amt_Net, locationCode \n"
                        + " ) Values ("
                        + "       :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Prefix, :v_Date, :refNo, :refDate, :subCode, \n"
                        + "       :consignee, :partyType, :gsTin, :gstinDate, :rcmApplicable, :placeOfSupply, :amdOrdDocId, \n"
                        + "       :isShowUsedQuotationsAlso, :site_Code, :wtaCode, :guiConfigCode, :preparedBy, :preparedDate, \n"
                        + "       :uEntDt, :transferDt, \n"
                        + "       :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                        + "       :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                        + "       :oh_Amt_SubTotal, :oh_Amt_Total, :oh_Amt_RoundOff, :oh_Amt_Net,  :locationCode \n"
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTemp.getRecIdOrder().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeTag()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdOrder().getCounter()).toString());
                psTemp.setString("v_Prefix", DmShared.xNull(voucherPrefix));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("isShowUsedQuotationsAlso", DmShared.iNull(mrTemp.getIsShowUsedQuotationsAlso()).toString());
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("amdOrdDocId", DmShared.foriegnKey(mrTemp.getAmdOrdDocId()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
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
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTemp.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));

            } else {
                if (DmShared.xNull(isSystemAdminUser).equalsIgnoreCase("0")) {
                    STServer.calculateEditUptoDays(DmServer.getSsnSiteCode(this), DmShared.xNull(mrTemp.getSharedKeyFieldCode()), cnTemp);
                }
                sqlQuery = "UPDATE pOrder SET"
                        + "   v_Date = :v_Date \n"
                        + " , refNo = :refNo \n"
                        + " , refDate = :refDate \n"
                        + " , subCode = :subCode \n"
                        + " , consignee = :consignee \n"
                        + " , partyType = :partyType \n"
                        + " , gsTin = :gsTin \n"
                        + " , gstinDate = :gstinDate \n"
                        + " , amdOrdDocId = :amdOrdDocId \n"
                        + " , rcmApplicable = :rcmApplicable \n"
                        + " , placeOfSupply = :placeOfSupply \n"
                        + " , isShowUsedQuotationsAlso = :isShowUsedQuotationsAlso \n"
                        + " , u_Name = :u_name \n"
                        + " , u_EntDt = :uEntDt \n"
                        + " , u_EntDt_LatestLine = :uEntDt \n"
                        + " , transferDt = :transferDt \n"
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
                        + " , oh_Amt_Total = :oh_Amt_Total\n"
                        + " , oh_Amt_RoundOff = :oh_Amt_RoundOff\n"
                        + " , oh_Amt_Net = :oh_Amt_Net\n"
                        + " , locationCode = :locationCode\n"
                        + " Where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getSupplierGstin()));
                psTemp.setString("amdOrdDocId", DmShared.foriegnKey(mrTemp.getAmdOrdDocId()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("isShowUsedQuotationsAlso", DmShared.iNull(mrTemp.getIsShowUsedQuotationsAlso()).toString());
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
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
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTemp.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));

            }
            psTemp.executeUpdate();
            psTemp.close();

            //====================================Re-check-PartyBillNo=================================
            /*==============================<< Check for duplicate party order no >>====================================*/
            if (!DmShared.xNull(mrTemp.getRefNo()).trim().equalsIgnoreCase("")) {
                sqlQuery = " SELECT COUNT(po.docId) AS cnt "
                        + " FROM pOrder po with (nolock) "
                        + " INNER JOIN voucher_type vt ON vt.v_Type = po.v_Type "
                        + " WHERE po.refNo = :refNo "
                        + " AND po.subCode=:subCode"
                        + " AND po.v_Prefix = :v_Prefix "
                        + " AND po.docId <> :docId "
                        + " AND vt.category IN ('" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "')";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierTag()));
                psTemp.setString("v_Prefix", voucherPrefix);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        throw new DmException(400, "Entry already exists with this Ref. No.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< Delete records from pOrderIndentDetail >>====================================*/
            manageIndentStatus(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), true, cnTemp);
            sqlQuery = "DELETE  FROM pOrderIndentDetail WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from pOrderDetail >>====================================*/
            sqlQuery = "DELETE FROM pOrderDetail WHERE docId = :docId AND isStatus NOT IN ('1','2')";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into pOrderDetail >>====================================*/
            List<GrdItemDetail_Mdl> listDetail = mrTemp.getGrdItemDetail();
            int vSNo = 0, previous_VSno = 0;
            for (int i = 0; i < mrTemp.getGrdItemDetail().size(); i++) {
                GrdItemDetail_Mdl detail_Mdl = mrTemp.getGrdItemDetail().get(i);
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    previous_VSno = DmShared.iNull(detail_Mdl.getvSNo()).intValue();
                    if (!detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        if (DmShared.iNull(detail_Mdl.getIsStatus()).intValue() != 1 && DmShared.iNull(detail_Mdl.getIsStatus()).intValue() != 2) {
                            detail_Mdl.setvSNo(String.valueOf(vSNo));
                            sqlQuery = "INSERT INTO pOrderDetail ("
                                    + "     docId, v_SNo, quotationDocId, quotationV_SNo, itemCode, hsnCode, itemGSTNature, colourCode, sizeCode, itemMakeCode, requiredDate, \n"
                                    + "     unitCode, quantity, skuQty, tl_ExcessPer, tl_ExcessQty, tl_ShortagePer, tl_ShortageQty, \n"
                                    + "     rate, remark, isStatus \n"
                                    + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                    + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                    + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net \n"
                                    + "     , gstTypeCode, isManualGstType, isManualTax,lastRateComparison,approvedRateYN \n"
                                    + " ) VALUES ("
                                    + "     :docId, :v_SNo, :quotationDocId, :quotationV_SNo, :itemCode, :hsnCode, :itemGSTNature, :colourCode, :sizeCode, :itemMakeCode, :requiredDate, \n"
                                    + "     :unitCode, :quantity, :skuQty, :tl_ExcessPer, :tl_ExcessQty, :tl_ShortagePer, :tl_ShortageQty,\n"
                                    + "     :rate, :remark, :isStatus \n"
                                    + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                    + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                    + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                    + "     , :gstTypeCode, :isManualGstType, :isManualTax,:lastRateComparison,:approvedRateYN \n"
                                    + " )";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                            psTemp.setInt("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).intValue());
                            psTemp.setString("quotationDocId", DmShared.foriegnKey(detail_Mdl.getQuotationDocId()));
                            psTemp.setString("quotationV_SNo", DmShared.foriegnKey(detail_Mdl.getQuotationV_SNo()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                            psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                            psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColorCode()));
                            psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                            psTemp.setString("itemMakeCode", DmShared.foriegnKey(detail_Mdl.getItemMakeCode()));
                            psTemp.setDate("requiredDate", DmServer.convertDate(detail_Mdl.getReqDate()));
                            psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                            psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getOrderQuantity()));
                            psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                            psTemp.setString("tl_ExcessPer", DmServer.vNull(detail_Mdl.getToleranceMdl().getTl_ExcessPer()));
                            psTemp.setString("tl_ExcessQty", DmServer.vNull(detail_Mdl.getToleranceMdl().getTl_ExcessQty()));
                            psTemp.setString("tl_ShortagePer", DmServer.vNull(detail_Mdl.getToleranceMdl().getTl_ShortagePer()));
                            psTemp.setString("tl_ShortageQty", DmServer.vNull(detail_Mdl.getToleranceMdl().getTl_ShortageQty()));
                            psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                            psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
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
                            psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getItemValue(), "0.00"));
                            psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail_Mdl.getGstTypeCode()));
                            psTemp.setString("isManualGstType", detail_Mdl.isManualGstType() ? "1" : "0");
                            psTemp.setString("isManualTax", detail_Mdl.isManualTax() ? "1" : "0");
                            psTemp.setString("lastRateComparison", detail_Mdl.getLastRateComparison());
                            psTemp.setString("approvedRateYN", DmShared.xNull(detail_Mdl.getApprovedRateYN()));

                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            if (DmShared.iNull(detail_Mdl.getIsStatus()).intValue() == 1 && previous_VSno > 0) {
                                //Update New v_SNo For Cancelled record
                                detail_Mdl.setvSNo(String.valueOf(vSNo));
                                sqlQuery = "UPDATE pOrderDetail "
                                        + " SET v_SNo = :v_SNo "
                                        + " WHERE docid = :docId"
                                        + " AND  v_SNo = :previous_VSno ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                                psTemp.setInt("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).intValue());
                                psTemp.setInt("previous_VSno", previous_VSno);
                                psTemp.executeUpdate();
                                psTemp.close();

                            }
                        }
                    }
                }
            }

            if (DmShared.iNull(mrTemp.getAmdOrdDocId()).longValue() > 0) {
                sqlQuery = "UPDATE pOrderDetail "
                        + " SET isAmended = :isAmended,"
                        + " isStatus= :isStatus "
                        + " WHERE docid = :amdOrdDocId"
                        + " AND isStatus = 0 ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("amdOrdDocId", DmShared.xNull(mrTemp.getAmdOrdDocId()));
                psTemp.setString("isAmended", "1");
                psTemp.setString("isStatus", "1");
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Insert records into pOrderIndentDetail  >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    for (GrdIndentDetail_Mdl indentDetail_Mdl : detail_Mdl.getListIndentDetail()) {
                        if (!DmShared.xNull(indentDetail_Mdl.getOrderQty()).equalsIgnoreCase("")) {
                            sqlQuery = " INSERT INTO pOrderIndentDetail("
                                    + "     docId, v_SNo, indentDocId,indentV_SNo, itemCode,orderedQty"
                                    + " ) VALUES ("
                                    + "     :docId, :v_SNo, :indentDocId,:indentV_SNo, :itemCode,:orderedQty"
                                    + " )";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                            psTemp.setString("indentDocId", DmShared.foriegnKey(indentDetail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("indentV_SNo", DmShared.foriegnKey(indentDetail_Mdl.getIndentVSNo()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("orderedQty", DmServer.vNull(indentDetail_Mdl.getOrderQty()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }
            manageIndentStatus(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), false, cnTemp);

            /*==============================<< Check for duplicate indent entry >>====================================*/
            STServer.checkForDuplicateIndent("pOrderIndentDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                STApprovals.purchaseOrderApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp, this);
            }

            //========================================(Service Grid Modal)==========================================================
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                    if (detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pOrderDetail (\n"
                                + "     docId, v_SNo, itemCode, hsnCode, quantity,  rate, itemGSTNature ,unitCode ,isFooter\n"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net\n"
                                + "     , serviceEvalTypeCode, serviceEvalRate, gstTypeCode, isManualGstType, isManualTax)\n"
                                + " Values (\n"
                                + "     :docId, :v_SNo, :itemCode, :hsnCode, :quantity,  :rate, :itemGSTNature ,:unitCode, :isFooter"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :serviceEvalTypeCode, :serviceEvalRate, :gstTypeCode, :isManualGstType, :isManualTax )\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getOrderQuantity()));
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

                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
            }

            /*==============================<< Insert Round Off Line >>====================================*/
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                if (DmShared.vNull(mrTemp.getRoundOffAmount()) != 0) {
                    sqlQuery = "INSERT INTO pOrderDetail ("
                            + "     docId, v_SNo, itemCode, hsnCode, quantity, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter"
                            + "     , oh_Amt_SubTotal, itemGSTNature, gstTypeCode)"
                            + " Values ("
                            + "     :docId, :v_SNo, :itemCode, :hsnCode, :quantity, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, :isFooter"
                            + "     , :oh_Amt_SubTotal, :itemGSTNature, :gstTypeCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", String.valueOf(++vSNo));
                    psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                    psTemp.setString("itemGSTNature", DmShared.foriegnKey(roundOffItemGSTNature));
                    psTemp.setString("gstTypeCode", DmShared.foriegnKey(roundOffGstTypeCode));
                    if (DmShared.vNull(mrTemp.getRoundOffAmount()) < 0) {
                        psTemp.setString("quantity", "-1");
                    } else {
                        psTemp.setString("quantity", "1");
                    }
                    psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTemp.getRoundOffAmount())), "0.00"));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("isFooter", "1");
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*==============================<< Insert records of Overhead and OverheadLine  >>====================================*/
            //OverHeadLine_Server.save(listDetail, "docId", mrTemp.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            //OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getRecIdOrder(), mrTemp.getVoucherTypeTag(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", "", "", "", "", true, false, cnTemp);
            /*====================================< Save GUI >====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmServer.getSsnSiteCode(this), cnTemp);
            //========================== END====================================================
            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getVoucherTypeTag(), this);
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
    public DmResult<PurchaseOrder_Mdl> getMoveRec(String strKey) {
        DmResult<PurchaseOrder_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PurchaseOrder_Mdl mrTemp = new PurchaseOrder_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        ServiceDetail_Mdl serviceDetail_Mdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   po.docId"
                    + " , po.v_Type"
                    + " , po.v_No"
                    + " , po.recId"
                    + " , po.v_Date"
                    + " , po.refNo"
                    + " , po.refDate"
                    + " , po.oh_Amt_Gross"
                    + " , po.u_EntDt"
                    + " , po.u_Name"
                    + " , po.preparedBy"
                    + " , po.wtaCode"
                    + " , po.approvedBy"
                    + " , po.approvalDateTime"
                    + " , po.u_EntDt_LatestLine"
                    + " , po.recIdPrefix"
                    + " , po.isShowUsedQuotationsAlso"
                    + " , po.guiConfigCode"
                    + " , po.preparedDate"
                    + " , vt.description AS voucherTypeText"
                    + " , vt.category AS voucherCategory"
                    + " , po.gstin AS supplierGstin "
                    + " , po.gstinDate AS supplierGstinDate "
                    + " , sg.subCode AS supplierCode"
                    + " , sg.name AS supplierName"
                    + " , c.stateCode AS supplierStateCode "
                    + " , sg.partyType AS supplierPartyType "
                    + " , po.partyType "
                    + " , po.rcmApplicable "
                    + " , po.placeOfSupply "
                    + " , po.amdOrdDocId, po1.recId as amdOrdNo, po1.v_Date as amdDate "
                    + " , q.docid AS quotationDocId"
                    + " , q.recId AS quotationRecId"
                    + " , q.v_Date AS quotationDate"
                    + " , enq.docId AS enquiryDocId"
                    + " , enq.recId AS enquiryRecId"
                    + " , po.oh_Amt_GrossGoods\n"
                    + " , po.oh_Amt_GrossService\n"
                    + " , po.oh_Amt_Gross\n"
                    + " , po.oh_Amt_Discount\n"
                    + " , po.oh_Amt_TaxableGoods\n"
                    + " , po.oh_Amt_TaxableService\n"
                    + " , po.oh_Amt_Taxable\n"
                    + " , po.oh_Amt_SGST\n"
                    + " , po.oh_Amt_UGST\n"
                    + " , po.oh_Amt_CGST\n"
                    + " , po.oh_Amt_IGST\n"
                    + " , po.oh_Amt_SubTotal\n"
                    + " , po.oh_Amt_Total\n"
                    + " , po.oh_Amt_RoundOff\n"
                    + " , po.oh_Amt_Net\n"
                    + " , po.locationCode\n"
                    + " FROM pOrder po"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sg ON po.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode "
                    + " LEFT JOIN ("
                    + "     SELECT pod.docId, pod.quotationDocId"
                    + "     FROM pOrderDetail pod"
                    + "     WHERE pod.quotationDocId IS NOT NULL"
                    + "     GROUP BY pod.docId, pod.quotationDocId"
                    + " ) pod ON po.docId = pod.docId"
                    + " LEFT JOIN quotation AS q ON q.DocId= pod.quotationDocId"
                    + " LEFT JOIN ("
                    + "     SELECT qd.docId, qd.enquiryDocId"
                    + "     FROM quotationDetail qd"
                    + "     WHERE qd.enquiryDocId IS NOT NULL"
                    + "     GROUP BY qd.docId, qd.enquiryDocId"
                    + " ) qd ON q.docId = qd.docId"
                    + " LEFT JOIN enquiry enq ON qd.enquiryDocId = enq.docId"
                    + " LEFT JOIN pOrder po1 ON po1.docId = po.amdOrdDocId "
                    + " WHERE po.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdOrder().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdOrder().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdOrder().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setQuotationDate(DmShared.dNull(rsTemp.getDate("quotationDate")));
                mrTemp.setQuotationNoTag(DmShared.xNull(rsTemp.getString("quotationDocId")));
                mrTemp.setQuotationNoText(DmShared.xNull(rsTemp.getString("quotationRecId")));
                mrTemp.setEnquiryTag(DmShared.xNull(rsTemp.getString("enquiryDocId")));
                mrTemp.setEnquiryText(DmShared.xNull(rsTemp.getString("enquiryRecid")));
                mrTemp.setVoucherTypeTag(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("voucherCategory")));
                mrTemp.setVoucherTypeText(DmShared.xNull(rsTemp.getString("voucherTypeText")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                mrTemp.setAmdOrdDocId(DmShared.xNull(rsTemp.getString("amdOrdDocId")));
                mrTemp.setAmdOrdNo(DmShared.xNull(rsTemp.getString("amdOrdNo")));
                mrTemp.setAmdOrdDate(DmShared.xNull(rsTemp.getString("amdDate")));

                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierPartyType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
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
                mrTemp.setTotalAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Total"), "0.00"));
                mrTemp.setRoundOffAmount(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                mrTemp.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
                mrTemp.setIsShowUsedQuotationsAlso(DmShared.iNull(rsTemp.getString("isShowUsedQuotationsAlso")).toString());
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            mrTemp.getGrdItemDetail().addAll(getMoveRecGridDetails(strKey, cnTemp));

            /*==============================<< Get Service detail >>===============================*/
            sqlQuery = "SELECT pcd.v_SNo, pcd.itemCode, pcd.hsnCode, pcd.itemGSTNature\n"
                    + " , pcd.isStatus, im.name AS itemName, im.smfCode, im.itsBasedOn\n"
                    + " , pcd.unitCode , pcd.quantity, pcd.rate , pcd.isManualTax\n"
                    + " , pcd.isManualGstType, it.name AS gstTypeName, pcd.gstTypeCode, pcd.oh_Amt_Gross\n"
                    + " , pcd.oh_Amt_Taxable, pcd.oh_at_SGST, pcd.oh_Amt_SGST, pcd.oh_at_UGST\n"
                    + " , pcd.oh_Amt_UGST, pcd.oh_at_CGST, pcd.oh_Amt_CGST, pcd.oh_at_IGST\n"
                    + " , pcd.oh_Amt_IGST, pcd.oh_Amt_SubTotal, pcd.oh_Amt_Net , um.gstUnitCode\n"
                    + " , hsn.hsn , pcd.serviceEvalTypeCode, pcd.serviceEvalRate,pcd.lastRateComparison \n"
                    + " FROM pOrderDetail pcd\n"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn ON hsn.code = pcd.hsnCode \n"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code\n"
                    + " WHERE pcd.docId = :docId \n"
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
                serviceDetail_Mdl.setV_Sno(DmShared.xNull(rsTemp.getString("v_SNo")));
                serviceDetail_Mdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                serviceDetail_Mdl.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                serviceDetail_Mdl.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                serviceDetail_Mdl.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                serviceDetail_Mdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                serviceDetail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                serviceDetail_Mdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                serviceDetail_Mdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                serviceDetail_Mdl.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                serviceDetail_Mdl.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                serviceDetail_Mdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                serviceDetail_Mdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                serviceDetail_Mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                serviceDetail_Mdl.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                if (!DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")).equalsIgnoreCase("")) {
                    serviceDetail_Mdl.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")));
                    serviceDetail_Mdl.setServiceEvalTypeName(DmShared.xNull(DmEnum.ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypeCode")).intValue()).getName()));
                    serviceDetail_Mdl.setServiceEvalRate(DmServer.vNull(rsTemp.getString("serviceEvalRate"), "0.00"));
                }

                //ITS
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

            /*====================================< Get GUI detail >====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), DmShared.xNull(strKey), DmServer.getSsnSiteCode(this), cnTemp));

            /*==============================<< Get Overhead detail >>===============================*/
            // mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));
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

    private List<GrdItemDetail_Mdl> getMoveRecGridDetails(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        String sqlQuery = "";
        try {

            sqlQuery = "SELECT im.purchaseRate,pod.v_SNo , pod.itemCode, pod.requiredDate, pod.quantity, pod.challanedQty \n"
                    + " , pod.rate, pod.oh_Amt_Gross, pod.remark, pod.isStatus, pod.quotationDocId \n"
                    + " , pod.quotationV_SNo, pod.colourCode, pod.sizeCode, pod.itemMakeCode \n"
                    + " , pod.skuQty, pod.tl_ExcessPer, pod.tl_ExcessQty, pod.tl_ShortagePer \n"
                    + " , pod.tl_ShortageQty, im.name as itemName, im.itsBasedOn, im.propertyList \n"
                    + " , im.isBestBefore, pod.itemGSTNature, pod.hsnCode, hsn.hsn as hsnNo \n"
                    + " , um.code AS purchaseUnitCode, um.unit AS purchaseUnitName, um.decimalPlaces AS purchaseUnitDecimalPlaces \n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces, ums.code AS skuCode, ums.unit AS skuName \n"
                    + " , icm.name as colourName, ism.name sizeName,imm.companyName as itemMakeName, pod.isManualTax\n"
                    + " , pod.isManualGstType, it.name AS gstTypeName, pod.gstTypeCode, pod.oh_Amt_Gross\n"
                    + " , pod.oh_Amt_Discount, pod.oh_at_Discount, pod.oh_Amt_Taxable, pod.oh_at_SGST\n"
                    + " , pod.oh_Amt_SGST, pod.oh_at_UGST, pod.oh_Amt_UGST, pod.oh_at_CGST\n"
                    + " , pod.oh_Amt_CGST, pod.oh_at_IGST, pod.oh_Amt_IGST, pod.oh_Amt_SubTotal\n"
                    + " , pod.oh_Amt_Net,pod.lastRateComparison,pod.approvedRateYN"
                    + " FROM pOrderDetail pod \n"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code \n"
                    + " LEFT JOIN unitMast um ON pod.unitCode = um.code \n"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode \n"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode \n"
                    + " LEFT JOIN itemMakeMast imm ON imm.code = pod.itemMakeCode \n"
                    + " LEFT JOIN gstType it ON it.code = pod.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pod.hsnCode \n"
                    + " WHERE pod.docId = :docId \n"
                    + " AND ISNULL(pod.isFooter, 0) = 0\n"
                    + " ORDER BY pod.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQuotationDocId(DmShared.xNull(rsTemp.getString("quotationDocId")));
                node.setQuotationV_SNo(DmShared.xNull(rsTemp.getString("quotationV_SNo")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsnNo(DmShared.xNull(rsTemp.getString("hsnNo")));

                node.getToleranceMdl().setOrderQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.getToleranceMdl().setTl_ExcessPer(DmServer.vNull(rsTemp.getString("tl_ExcessPer")));
                node.getToleranceMdl().setTl_ExcessQty(DmServer.vNull(rsTemp.getString("tl_ExcessQty")));
                node.getToleranceMdl().setTl_ShortagePer(DmServer.vNull(rsTemp.getString("tl_ShortagePer")));
                node.getToleranceMdl().setTl_ShortageQty(DmServer.vNull(rsTemp.getString("tl_ShortageQty")));

                node.setOrderQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setApprovedRateYN(DmShared.xNull(rsTemp.getString("approvedRateYN")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setPurchaseRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));

                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double purchaseUnitQty = DmShared.vNull(rsTemp.getString("quantity"));
                double convRate = skuQty / purchaseUnitQty;
                node.setPurchaseUnitConversionRate(DmServer.vNull(convRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
//                node.setPurchaseUnitConversionRate(DmServer.vNull(node.getPurchaseUnitConversionRate()));

                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                node.setReqDate(DmShared.dNull(rsTemp.getDate("requiredDate")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setChallanQyt(DmShared.xNull(rsTemp.getString("challanedQty")));
                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setLastRateComparison(DmShared.xNull(rsTemp.getString("lastRateComparison")));

                if (DmShared.xNull(rsTemp.getString("lastRateComparison")).trim().equalsIgnoreCase("0")) {
                    node.setLastRateComparisonName("NP");// New Purchase
                } else if (DmShared.xNull(rsTemp.getString("lastRateComparison")).trim().equalsIgnoreCase("1")) {
                    node.setLastRateComparisonName("SR");// Same Rate from Previous
                }
                if (DmShared.xNull(rsTemp.getString("lastRateComparison")).trim().equalsIgnoreCase("2")) {
                    node.setLastRateComparisonName("HR");// Heigher Rate from Previous
                }
                if (DmShared.xNull(rsTemp.getString("lastRateComparison")).trim().equalsIgnoreCase("3")) {
                    node.setLastRateComparisonName("LR");// Low Rate from Previous
                }

                ItemProperty_Mdl ippm_Mdl = new ItemProperty_Mdl();
                ippm_Mdl.setItemCode(node.getSharedKeyFieldCode());
                ippm_Mdl.setColourCode(node.getColorCode());
                ippm_Mdl.setColourName(node.getColor());
                ippm_Mdl.setSizeCode(node.getSizeCode());
                ippm_Mdl.setSizeName(node.getSize());
                ippm_Mdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ippm_Mdl.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setProperties(DmShared.xNull(ippm_Mdl.getPropertiesCsv()));

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
                node.setItemValue(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                /*==============================<< Get Indent detail >>===============================*/
                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(true, DmShared.xNull(docId), DmShared.xNull(rsTemp.getString("v_SNo")), null, ippm_Mdl, cnTemp);
                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
//                    throw new DmException(0, "No record found.");
                }

                lstNode.add(node);
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
        return lstNode;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode, String amdOrdDocId, String guiConfigCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            sqlQuery = "select wtaCode,approvedBy  FROM pOrder WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
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
            STServer.chkIfDocumentUsed("poDocId", "pChallanDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("poDocId", "pBillDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("poDocId", "gateAdviceDetail", keyCode, cnTemp);

            /*==============================<< Delete record from Overhead & OverheadLine >>============================*/
            OverHeadLine_Server.delete(keyCode, cnTemp);
            OverHead_Server.delete(keyCode, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            ModifyReason_Server.delete(keyCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);

            /*==============================<< Delete record from quotationIndentDetail >>===============================*/
            manageIndentStatus(DmShared.iNull(keyCode).toString(), true, cnTemp);
            sqlQuery = " Delete From pOrderIndentDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //==============================<<  >>==============================
            if (DmShared.iNull(amdOrdDocId).longValue() > 0) {
                sqlQuery = "UPDATE pOrderDetail "
                        + " SET isAmended = :isAmended,"
                        + " isStatus= :isStatus "
                        + " WHERE docid = :amdOrdDocId"
                        + " AND isAmended = 1 ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("amdOrdDocId", DmShared.iNull(amdOrdDocId).toString());
                psTemp.setString("isAmended", "0");
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();
                psTemp.close();
            }

            /*==============================<< Delete record from pOrderDetail >>===============================*/
            sqlQuery = " Delete From pOrderDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from pOrder >>===============================*/
            sqlQuery = " Delete From pOrder Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from mongoDb >>===============================*/
            //<editor-fold defaultstate="collapsed" desc="delete attachments">
            sqlQuery = "SELECT lfc.sNo, lfc.ruleCode FROM linkFileCollection lfc WHERE docid = :docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            DmFileManagerMongo dmFileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            while (rsTemp.next()) {
                dmFileManagerMongo.deleteFiles(keyCode, DmShared.iNull(rsTemp.getString("sNo")).toString(), DmShared.xNull(rsTemp.getString("ruleCode")), cnTemp);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>    

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
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT po.docId"
                    + " FROM pOrder po"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type  "
                    + " WHERE vt.category IN (" + vCat + ") "
                    + " AND po.site_Code = :site_Code "
                    + " AND po.v_prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY po.v_Date DESC, vt.short_Name, po.v_No DESC, po.recId";
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

            sqlQuery = " SELECT "
                    + "    po.docId,"
                    + "    po.recId,"
                    + "    po.v_Date,"
                    + "    po.refNo,po.v_No,"
                    + "    po.RefDate,"
                    + "    sgv.displayName AS vendorName,"
                    + "    en.recId AS enquiryNo,q.recId AS qRecId,"
                    + "    vt.short_Name,po.approvedBy,format(po.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime "
                    + "  FROM pOrder AS po "
                    + "  LEFT JOIN voucher_Type AS vt ON po.v_Type = vt.v_Type "
                    + "  LEFT JOIN subGroup AS sgv ON po.subCode = sgv.subCode "
                    + "  LEFT JOIN ("
                    + "         SELECT pod.docId, pod.quotationDocId"
                    + "         FROM pOrderDetail pod"
                    + "         WHERE pod.quotationDocId IS NOT NULL"
                    + "         GROUP BY pod.docId, pod.quotationDocId"
                    + "  ) pod ON po.docId = pod.docId"
                    + "  LEFT JOIN quotation AS q ON pod.quotationDocId = q.docId"
                    + "  LEFT JOIN ("
                    + "         SELECT qd.docId, qd.enquiryDocId"
                    + "         FROM quotationDetail qd"
                    + "         WHERE qd.enquiryDocId IS NOT NULL"
                    + "         GROUP BY qd.docId, qd.enquiryDocId"
                    + "  ) qd ON q.docId = qd.docId"
                    + "  LEFT JOIN enquiry en ON qd.enquiryDocId = en.docId"
                    + "	 WHERE vt.category in (" + vCat + ") "
                    + "  AND po.site_Code = :site_Code "
                    + "  AND po.v_prefix = :vPrefix";
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
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setOrderNoDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRefNoDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                listNode.setEnquiryNo(DmShared.xNull(rsTemp.getString("enquiryNo")));
                listNode.setPurchaseType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setSupplierPT(DmShared.xNull(rsTemp.getString("vendorName")));
                listNode.setQuotationNo(DmShared.xNull(rsTemp.getString("qRecId")));
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

    private void manageIndentStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String strSQL, indentDocId = "";
        double aprQty, ordedQty, ordQty;
        Integer isStatus;

        try {
            strSQL = "SELECT "
                    + "   i.docId AS indentDocId"
                    + " , id.v_SNo AS indentVSno"
                    + " , pid.orderedQty AS orderQty"
                    + " , id.aprQty"
                    + " , id.orderedQty "
                    + " , id.isStatus"
                    + " , i.approvedBy"
                    + " , i.recId AS indentNo"
                    + " , vt.short_name AS intentType"
                    + " FROM indentDetail id"
                    + " LEFT JOIN indent i ON i.docId = id.docId"
                    + " LEFT JOIN voucher_type vt ON vt.v_type = i.v_Type"
                    + " INNER JOIN ("
                    + "     SELECT pid.indentDocId, pid.indentV_SNo, SUM(pid.orderedQty) AS orderedQty"
                    + "     FROM pOrderIndentDetail pid"
                    + "     LEFT JOIN pOrderDetail pod ON pod.docId = pid.docId AND pod.v_SNo = pid.v_SNo "
                    + "     WHERE pid.docId = :docId AND pod.isStatus <> 1  "
                    + "     GROUP BY pid.indentDocId, pid.indentV_SNo"
                    + " ) pid ON id.docId = pid.indentDocId AND id.v_Sno = pid.indentV_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent is Cancelled");
                }
                aprQty = rsTemp.getDouble("aprQty");
                ordedQty = rsTemp.getDouble("orderedQty");
                ordQty = rsTemp.getDouble("orderQty");

                if (isDelete) {
                    ordedQty -= ordQty;
                } else {
                    ordedQty += ordQty;
                }

                if (ordedQty > aprQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent overflows by " + DmServer.vNull(ordedQty - aprQty));
                } else if (ordedQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent overflows by " + DmServer.vNull(ordedQty));
                } else if (ordedQty == aprQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (!indentDocId.equalsIgnoreCase("")) {
                    indentDocId += ",";
                }

                indentDocId += DmShared.iNull(rsTemp.getString("indentDocId")).toString();

                strSQL = "UPDATE IndentDetail SET"
                        + "   isStatus = :isStatus"
                        + " , orderedQty = :orderedQty"
                        + " where docId = :docId AND v_SNo = :v_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("indentDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("indentVSno")).toString());
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("orderedQty", DmServer.vNull(ordedQty));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!indentDocId.equalsIgnoreCase("")) {
                strSQL = "update indent set u_EntDt_LatestLine = :uEntDt where docId in (" + indentDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
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
    public String getTempKeyCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public DmResult<List<TxtHQuotationNo_Mdl>> getQuotationDetail(KeyFieldExtended keyField, String supplierCode, String v_Date, String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<List<TxtHQuotationNo_Mdl>> rstTemp = new DmResult();
        List<TxtHQuotationNo_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            lstTemp = getQuotationDetail(keyField.getSharedKeyFieldCode(), false, supplierCode, v_Date, voucherCategory, helper, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);

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

    private List<TxtHQuotationNo_Mdl> getQuotationDetail(String quoDocId, boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date, String voucherCategory, DmHelper helper, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        List<TxtHQuotationNo_Mdl> lstTemp = new ArrayList<>();
        TxtHQuotationNo_Mdl listNode;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND q.v_Date <= :v_Date";
                condition += " AND ISNULL(q.validUpto,:v_Date) >= :v_Date ";
            } else {
                condition += " AND 1 = 2"; //TBC : As commented by Satyam
            }
            if (!isShowUsedQuotationsAlso) {
                condition += " AND pod.quotationDocId IS NULL";
            }
            if (DmShared.iNull(quoDocId).longValue() != 0) {
                condition += " AND q.docId = :docId ";
            }

            /*++++++++++++++++++++++++Quotation not Works in Case of Service+++++++++++++++++++++++++*/
            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER)
                    || DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_ORDER_UNREGISTERED)) {
                condition += " AND 1=2 ";
            }
            /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

            sqlQuery = "SELECT "
                    + "   q.docId AS code"
                    + " , q.recId AS name"
                    + " , q.v_Date AS quotationDate"
                    + " , e.docId AS enquiryDocId"
                    + " , e.recId AS enquiryRecId"
                    + " , q.vendorRefNo AS supplierRefNo"
                    + " , q.vendorRefDate AS supplierRefDate"
                    + " , q.guiConfigCode"
                    + " , q.validUpto"
                    + " FROM quotation q"
                    + " LEFT JOIN voucher_Type vt ON q.v_Type = vt.v_Type"
                    + " LEFT JOIN ("
                    + "         SELECT qd.docId, qd.enquiryDocId"
                    + "         FROM quotationDetail qd"
                    + "         WHERE qd.enquiryDocId IS NOT NULL"
                    + "         GROUP BY qd.docId, qd.enquiryDocId"
                    + " ) qd ON q.docId = qd.docId"
                    + " LEFT JOIN enquiry e ON qd.enquiryDocId = e.docId"
                    + "	LEFT JOIN ("
                    + "         SELECT pod.quotationDocId"
                    + "         FROM pOrderDetail pod"
                    + "         WHERE pod.quotationDocId IS NOT NULL"
                    + "         GROUP BY pod.quotationDocId"
                    + " ) pod ON q.docId = pod.quotationDocId"
                    + " WHERE vt.Category IN ('" + pvc.QUOTATION + "','" + pvc.QUOTATION_UNREGISTERED + "')"
                    + " AND q.site_Code = :site_Code"
                    + " AND q.subCode = :subCode"
                    + condition;

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("subCode", DmShared.iNull(supplierCode).toString());
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            if (DmShared.iNull(quoDocId).longValue() != 0) {
                sqlWithParam.setString("docId", DmShared.iNull(quoDocId).toString());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHQuotationNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setQuotationDate(DmShared.dNull(rsTemp.getDate("quotationDate")));
                listNode.setEnquiryRefNo(DmShared.xNull(rsTemp.getString("enquiryRecId")));
                listNode.setEnquiryDocId(DmShared.xNull(rsTemp.getString("enquiryDocId")));
                listNode.setSupplierRefDate(DmShared.dNull(rsTemp.getDate("supplierRefDate")));
                listNode.setSupplierRefNo(DmShared.xNull(rsTemp.getString("supplierRefNo")));
                listNode.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                listNode.setQuotationValidUpto(DmShared.dNull(rsTemp.getDate("validUpto")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            new DmException(0, e.getMessage());
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
        return lstTemp;
    }

    @Override
    public DmResult<TxtHSupplierPT_Mdl> getSupplierDetail(String supplierCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<TxtHSupplierPT_Mdl> rstTemp = new DmResult();
        List<TxtHSupplierPT_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplierPT_Mdl mrTemp = new TxtHSupplierPT_Mdl();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSupplierDetail(supplierCode, voucherCategory, flagFilterByVoucherCategory, v_Type, helper, blnToEndOfFile, cnTemp);
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

    private List<TxtHSupplierPT_Mdl> getSupplierDetail(String vendorCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHSupplierPT_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplierPT_Mdl listNode;
        String condition = "", filtration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (DmShared.iNull(vendorCode).longValue() > 0) {
                condition += " AND  sg.subCode = :vendorCode ";
            }

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_ORDER_UNREGISTERED)
                    || DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.SERVICE_ORDER_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_ORDER)
                    || DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.SERVICE_ORDER)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            }

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "','" + pvc.SERVICE_ORDER + "','" + pvc.SERVICE_ORDER_UNREGISTERED + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, "
                    + " c.stateCode, sg.partyType, sg.gstin, sg.gstinDate "
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(vendorCode).longValue() > 0) {
                sqlWithParam.setString("vendorCode", vendorCode);
            }
            if (!flagFilterByVoucherCategory) {
                sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplierPT_Mdl();
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
            new DmException(0, ex.getMessage());
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
    public DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> getPrevGrn(ItemProperty_Mdl itemProperty, String itemCode, String supplierCode, boolean flagCurrentSupplier, DmHelper helper, boolean blnToEndOfFile, String param) {
        DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> dmrTemp = new DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>();
        List<GrdGrnPurchaseHistory_Mdl> lstTemp = new ArrayList<GrdGrnPurchaseHistory_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        GrdGrnPurchaseHistory_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "", supplierCondition = "";
        dmrTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(supplierCode).longValue() != 0) {
                if (flagCurrentSupplier) {
                    supplierCondition += " AND pc.subcode = :supplierCode";
                } else {
                    supplierCondition += " AND pc.subcode != :supplierCode";
                }
            }
            if (!DmShared.xNull(itemProperty.getColourCode()).equalsIgnoreCase("")) {
                condition += " AND pcd.colourCode = :colourCode";
            }
            if (!DmShared.xNull(itemProperty.getSizeCode()).equalsIgnoreCase("")) {
                condition += " AND pcd.sizeCode = :sizeCode";
            }
            if (!DmShared.xNull(itemProperty.getItemMakeCode()).equalsIgnoreCase("")) {
                condition += " AND pcd.itemMakeCode = :itemMakeCode";
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
                    + " LEFT JOIN itemColourMast icm ON icm.code= pcd.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code= pcd.sizeCode"
                    + " LEFT JOIN itemMakeMast imm ON imm.code= pcd.itemMakeCode"
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
                    + " , pcd.freeQty"
                    + " , pcd.rate"
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
                    + " LEFT JOIN itemColourMast icm ON icm.code= pcd.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code= pcd.sizeCode"
                    + " LEFT JOIN itemMakeMast imm ON imm.code= pcd.itemMakeCode"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND isNull(pc.approvedBy,'') <> ''"
                    + " AND ISNULL(pcd.isFooter, 0) = 0"
                    + " AND pcd.itemCode = :itemCode" + supplierCondition + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("itemCode", DmShared.xNull(itemCode));
            if (DmShared.iNull(supplierCode).longValue() != 0) {
                sqlWithParam.setString("supplierCode", DmShared.xNull(supplierCode));
            }
            if (!DmShared.xNull(itemProperty.getColourCode()).equalsIgnoreCase("")) {
                sqlWithParam.setString("colourCode", DmShared.xNull(itemProperty.getColourCode()));
            }
            if (!DmShared.xNull(itemProperty.getSizeCode()).equalsIgnoreCase("")) {
                sqlWithParam.setString("sizeCode", DmShared.xNull(itemProperty.getSizeCode()));
            }
            if (!DmShared.xNull(itemProperty.getItemMakeCode()).equalsIgnoreCase("")) {
                sqlWithParam.setString("itemMakeCode", DmShared.xNull(itemProperty.getItemMakeCode()));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGrnPurchaseHistory_Mdl();
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

    @Override
    public DmResult<List<GrdItemDetail_Mdl>> fillAmendmentGridDetail(String amdOrdDocId) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();
        List<GrdItemDetail_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        dmrTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            lstTemp.addAll(fillAmendmentGridDetail(amdOrdDocId, cnTemp));

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

    private List<GrdItemDetail_Mdl> fillAmendmentGridDetail(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        String sqlQuery = "";
        try {

            sqlQuery = "SELECT"
                    + "   pod.v_SNo"
                    + " , pod.itemCode"
                    + " , pod.requiredDate"
                    + " , (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0)) as quantity"
                    + " , 0 as challanedQty"
                    + " , pod.rate"
                    + " , pod.oh_Amt_Gross"
                    + " , pod.remark"
                    + " , pod.isStatus"
                    + " , pod.quotationDocId"
                    + " , pod.quotationV_SNo"
                    + " , pod.colourCode"
                    + " , pod.sizeCode"
                    + " , pod.itemMakeCode"
                    + " , pod.skuQty"
                    + " , pod.tl_ExcessPer"
                    //                    + " , pod.tl_ExcessQty"
                    + " , pod.tl_ShortagePer"
                    //                    + " , pod.tl_ShortageQty"
                    + " , im.name as itemName"
                    + " , im.itsBasedOn"
                    + " , im.propertyList"
                    + " , im.isBestBefore"
                    + " , pod.itemGSTNature "
                    + " , pod.hsnCode"
                    + " , um.code AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , um.decimalPlaces AS purchaseUnitDecimalPlaces"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , icm.name as colourName"
                    + " , ism.name sizeName"
                    + " ,imm.companyName as itemMakeName"
                    + " FROM pOrderDetail pod"
                    + " LEFT JOIN pOrder po ON po.docId = pod.docId"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code"
                    + " LEFT JOIN unitMast um ON pod.unitCode = um.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode"
                    + " LEFT JOIN itemMakeMast imm ON imm.code = pod.itemMakeCode "
                    + " WHERE pod.docId = :docId "
                    + " AND ISNULL(po.approvedBy,'')<>'' "
                    + " AND pod.isStatus = '0' "
                    + " AND (ISNULL(pod.quantity,0) - ISNULL(pod.challanedQty,0)) > 0 "
                    + " ORDER BY pod.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQuotationDocId(DmShared.xNull(rsTemp.getString("quotationDocId")));
                node.setQuotationV_SNo(DmShared.xNull(rsTemp.getString("quotationV_SNo")));

                //Item Details
//                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                node.getToleranceMdl().setOrderQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.getToleranceMdl().setTl_ExcessPer(DmServer.vNull(rsTemp.getString("tl_ExcessPer")));
//                node.getToleranceMdl().setTl_ExcessQty(DmServer.vNull(rsTemp.getString("tl_ExcessQty")));
                node.getToleranceMdl().setTl_ShortagePer(DmServer.vNull(rsTemp.getString("tl_ShortagePer")));
//                node.getToleranceMdl().setTl_ShortageQty(DmServer.vNull(rsTemp.getString("tl_ShortageQty")));

                node.setOrderQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double purchaseUnitQty = DmShared.vNull(rsTemp.getString("quantity"));
                double convRate = skuQty / purchaseUnitQty;
                node.setPurchaseUnitConversionRate(DmServer.vNull(convRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue())));
                node.setPurchaseUnitConversionRate(DmServer.vNull(node.getPurchaseUnitConversionRate()));

                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                node.setReqDate(DmShared.dNull(rsTemp.getDate("requiredDate")));
                node.setIsStatus(DmShared.iNull(rsTemp.getString("isStatus")).toString());
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setChallanQyt(DmShared.xNull(rsTemp.getString("challanedQty")));
                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));

                ItemProperty_Mdl ippm_Mdl = new ItemProperty_Mdl();
                ippm_Mdl.setItemCode(node.getSharedKeyFieldCode());
                ippm_Mdl.setColourCode(node.getColorCode());
                ippm_Mdl.setColourName(node.getColor());
                ippm_Mdl.setSizeCode(node.getSizeCode());
                ippm_Mdl.setSizeName(node.getSize());
                ippm_Mdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ippm_Mdl.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setProperties(DmShared.xNull(ippm_Mdl.getPropertiesCsv()));

                /*==============================<< Get OverheadLine detail >>===============================*/
                //  node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(docId).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));

                /*==============================<< Get Indent detail >>===============================*/
                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(true, DmShared.xNull(docId), DmShared.xNull(rsTemp.getString("v_SNo")), null, ippm_Mdl, cnTemp);
                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
                }

                lstNode.add(node);
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
        return lstNode;
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
    public DmResult<String> getLastRate(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, strLastPurchaseRate = "";
        ResultSet rsTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select top 1 isNull(pbd.rate,0) as lastPurchaseRate "
                    + " from itemMast im "
                    + " left join pBillDetail pbd on pbd.itemCode=im.code "
                    + " left join pBill pb on pbd.docID=pb.docID "
                    + " where im.Code=" + DmShared.xNull(strKey) + " and pb.site_Code=" + DmServer.getSsnSiteCode(this)
                    + " order by pb.v_Date desc ";

            strLastPurchaseRate = DmServer.executeScalar(sqlQuery, cnTemp);
            dmrTemp.setStatus(true);
            dmrTemp.setData(strLastPurchaseRate);

            cnTemp.close();
        } catch (Exception e) {

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
