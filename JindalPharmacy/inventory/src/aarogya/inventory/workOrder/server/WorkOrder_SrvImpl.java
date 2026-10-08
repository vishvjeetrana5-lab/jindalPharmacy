package aarogya.inventory.workOrder.server;

import aarogya.inventory.workOrder.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.overHead.shared.OverHead_Mdl;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Timestamp;

public class WorkOrder_SrvImpl extends DmRSSPrivate implements WorkOrder_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCat = "'" + pvc.WORK_ORDER + "'";
    private final String voucherTypeFieldName = "wo.v_Type", voucherDateFieldName = "wo.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   vt.v_Type"
                    + " , vt.description AS name "
                    + " , vt.guiTemplateCode"
                    + " FROM voucher_Type vt "
                    + " WHERE vt.category = " + vCat
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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
    public DmResultPageing<List<TxtHSupplierPT_Mdl>> getTxtHSupplierPT(boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHSupplierPT_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplierPT_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplierPT_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, filtration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.WORK_ORDER + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName,sg.guiConfigCode "
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!flagFilterByVoucherCategory) {
                sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplierPT_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
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
    public DmResultPageing<List<TxtHQuotationNo_Mdl>> getTxtHQuotationNo(boolean isShowUsedQuotationsAlso, String supplierCode, String v_Date, DmHelper helper, boolean blnToEndOfFile) {
    DmResultPageing<List<TxtHQuotationNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHQuotationNo_Mdl> lstTemp = new ArrayList<>();
        TxtHQuotationNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND q.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (!isShowUsedQuotationsAlso) {
                condition += " AND pod.quotationDocId IS NULL";
            }

            sqlQuery = "SELECT "
                    + "   q.docId AS code"
                    + " , q.recId AS name"
                    + " , q.v_Date AS quotationDate"
                    + " , e.docId AS enquiryDocId"
                    + " , e.recId AS enquiryRecId"
                    + " , q.vendorRefNo AS supplierRefNo"
                    + " , q.vendorRefDate AS supplierRefDate"
                    + " , q.guiConfigCode"
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
                    + " WHERE vt.Category = '" + pvc.QUOTATION + "'"
                    + " AND q.site_Code = :site_Code"
                    + " AND q.subCode = :subCode"
                    + condition;

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("subCode", DmShared.iNull(supplierCode).toString());
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile,boolean  blnAllItemHelp) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , im.manualCode"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , im.propertyList"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , ISNULL(im.isBestBefore, 0) AS isBestBefore"
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + " WHERE im.IsActive = 1 "
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code"
                    + " AND im.isQuotationMandatory = 0 ");

            if (blnAllItemHelp==false){
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SV", "manualCode", "itemType", "code", cnTemp)));
            }else {
                sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            }

            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    listNode.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.xNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
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
    public DmResult<WorkOrder_Mdl> getItemDetail(String QuotationDocId, List<KeyFieldExtended> keyFields) {
        DmResult<WorkOrder_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", condition1 = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        WorkOrder_Mdl mrTemp = new WorkOrder_Mdl();
        GrdItemDetail_Mdl node;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                sqlQuery = " SELECT "
                        + "   qd.itemCode"
                        + " , im.name AS itemName"
                        + " , qd.quantity"
                        + " , qd.unitCode AS purchaseUnitCode"
                        + " , um.unit AS purchaseUnitName"
                        + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                        + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                        + " , ums.unit AS skuCode"
                        + " , ums.unit AS skuName"
                        + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                        + " , qd.deliveryDate AS reqDate"
                        + " , qd.rate"
                        + " , qd.oh_Amt_Gross"
                        + " , im.propertyList"
                        + " , im.isBestBefore"
                        + " , qd.colourCode"
                        + " , qd.sizeCode"
                        + " , icm.name AS colourName"
                        + " , ism.name AS sizeName"
                        + " , qd.docId AS quotationDocId"
                        + " , qd.v_SNo AS quotationV_SNo"
                        + " FROM quotationDetail qd"
                        + " LEFT JOIN itemMast im ON qd.itemCode = im.code"
                        + " LEFT JOIN unitMast um ON qd.unitCode = um.code"
                        + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                        + " LEFT JOIN itemColourMast icm ON icm.code = qd.colourCode"
                        + " LEFT JOIN itemSizeMast ism ON ism.code = qd.sizeCode"
                        + " WHERE qd.docId = :docId"
                        + " ORDER BY im.name";

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
                        + " MAX(im.name) AS itemName,"
                        + " MAX(im.purchaseUnit) AS purchaseUnitCode,"
                        + " MAX(um.unit) AS purchaseUnitName,"
                        + " ISNULL(MAX(um.decimalPlaces),0) AS purchaseUnitDecimalPlaces,"
                        + " ISNULL(MAX(im.purchaseUnitConversionRate), 0) AS purchaseUnitConversionRate,"
                        + " MAX(ums.code) AS skuCode,"
                        + " MAX(ums.unit) AS skuName,"
                        + " ISNULL(MAX(ums.decimalPlaces),0) AS skuDecimalPlaces,"
                        + " Max(im.propertyList) AS propertyList,"
                        + " Max(im.isBestBefore) AS isBestBefore,"
                        + " MAX(id.reqDate) AS reqDate,"
                        + " MAX(id.v_SNo) AS v_SNo,"
                        + " 0 AS rate,"
                        + " 0 AS oh_Amt_Gross,"
                        + " NULL AS quotationDocId,"
                        + " NULL AS quotationV_SNo,"
                        + " SUM(CASE WHEN ISNULL(pid.adjQty, 0) = 0 THEN ISNULL(id.aprQty, 0) ELSE ISNULL(id.aprQty, 0) - ISNULL(pid.adjQty, 0) END) AS quantity, "
                        + " MAX(id.colourCode) as colourCode,"
                        + " MAX(icm.name) as colourName,"
                        + " MAX(id.sizeCode) as sizeCode,"
                        + " MAX(ism.name) as sizeName"
                        + " FROM indentDetail id"
                        + " LEFT JOIN indent i ON id.docId = i.docId"
                        + " LEFT JOIN("
                        + "        SELECT SUM(pd.orderedQty) AS adjQty, pd.indentDocId, pd.indentV_SNo"
                        + "        FROM pOrderIndentDetail pd" + condition1
                        + "        GROUP BY pd.indentDocId, pd.indentV_SNo"
                        + " ) AS pid ON id.docId = pid.indentDocId AND id.v_Sno = pid.indentV_SNo"
                        + " LEFT JOIN itemMast AS im ON id.itemCode = im.code "
                        + " LEFT JOIN unitMast AS um ON im.purchaseUnit = um.code "
                        + " LEFT JOIN unitMast AS ums ON im.sku = ums.code "
                        + " LEFT JOIN itemColourMast icm ON icm.code = id.colourCode "
                        + " LEFT JOIN itemSizeMast ism ON ism.code = id.sizeCode "
                        + " WHERE id.isStatus <> 1 "
                        + " AND ISNULL(i.approvedBy, '') <> ''"
                        + " AND (" + condition + ")"
                        + " GROUP BY id.itemCode, id.colourCode, id.sizeCode"
                        + " ORDER BY itemName ";
                //</editor-fold>
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(QuotationDocId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.iNull(QuotationDocId).toString());
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

                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setColorCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColor(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSize(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setQuotationDocId(DmShared.xNull(rsTemp.getString("quotationDocId")));
                node.setQuotationV_SNo(DmShared.xNull(rsTemp.getString("quotationV_SNo")));

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

                /*==============================<< Get OverheadLine detail >>===============================*/
                node.setOHLListQuotation(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(node.getQuotationDocId()).toString(), DmShared.iNull(node.getQuotationV_SNo()).toString(), cnTemp));

                lstNode.add(node);
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
                        + " FROM " + table + " tb"
                        + " LEFT JOIN indentDetail id ON tb.indentDocId = id.docId AND tb.indentV_SNo = id.v_SNo"
                        + " LEFT JOIN itemMast AS im ON id.itemCode = im.code"
                        + " LEFT JOIN unitMast AS um ON im.sku = um.code"
                        + " LEFT JOIN itemColourMast icm ON id.colourCode = icm.code"
                        + " LEFT JOIN itemSizeMast ism ON id.sizeCode = ism.code"
                        + " LEFT JOIN indent i ON id.docId = i.docId"
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
                        + " , id.sizeCode"
                        + " , ism.name AS sizeName"
                        + " FROM indentDetail id"
                        + " LEFT JOIN indent i ON id.docId = i.docId"
                        + " LEFT JOIN itemMast AS im ON id.itemCode = im.code"
                        + " LEFT JOIN unitMast AS um ON im.sku = um.code"
                        + " LEFT JOIN itemColourMast icm ON id.colourCode = icm.code"
                        + " LEFT JOIN itemSizeMast ism ON id.sizeCode = ism.code"
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
                node.setProperties(DmShared.xNull(ipm.getPropertiesCsv()));

                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
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
            STServer.chkIfDocumentUsed("poDocId", "pChallanDetail", keyCode, cnTemp);
            STServer.chkIfDocumentUsed("poDocId", "pBillDetail", keyCode, cnTemp);

            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from pOrder where docId = :docId ";
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

                        sqlQuery = " Update pOrder set "
                                + " u_EntDt_LatestLine=:uEntDt "
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

                            sqlQuery = "select docId "
                                    + "from pOrderDetail "
                                    + "where docId = :docId and isNull(challanedQty,0) <> 0 ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                throw new Exception("Document is in use. you can not unapprove.");
                            }
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update pOrder set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
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

    private WalkThroughDefault_Mdl getWalkThroughFor(WorkOrder_Mdl mrTemp) {
        double netAmount = 0.0;
        OverHead_Mdl ohMdl = OverHead_Shared.getOHByUniqueId("PT", mrTemp.getOverHeadList());
        if (ohMdl != null) {
            netAmount = DmShared.vNull(ohMdl.getAmount());
        }
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
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = " + vCat
                    + " AND vt.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);

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

    @Override
    public DmResult<String> saveRecord(WorkOrder_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        boolean blnAdd = false;
        DmSavePoint spBegin = null;
        String sqlQuery, userName = "", siteCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            WorkOrder_Vld.chkSave(mrTemp, DmServer.getServerDateTime()[0]);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getGrdItemDetail());
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
            } else {
                DmServer.getRecId("pOrder", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdOrder(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

            if (blnAdd) {
                sqlQuery = "INSERT INTO pOrder ("
                        + "       docId, recId, recIdPrefix, v_Type, v_No, v_Prefix, v_Date, refNo, refDate, subCode,"
                        + "       isShowUsedQuotationsAlso, site_Code, wtaCode, guiConfigCode, preparedBy, u_EntDt,"
                        + "       u_EntDt_LatestLine, transferDt"
                        + " ) Values ("
                        + "       :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Prefix, :v_Date, :refNo, :refDate, :subCode,"
                        + "       :isShowUsedQuotationsAlso, :site_Code, :wtaCode, :guiConfigCode, :preparedBy, :uentDt,"
                        + "       :uentDt, :transferDt"
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
                psTemp.setString("isShowUsedQuotationsAlso", DmShared.iNull(mrTemp.getIsShowUsedQuotationsAlso()).toString());
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("preparedBy", DmShared.xNull(userName));
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "UPDATE pOrder SET"
                        + "   v_Date = :v_Date"
                        + " , refNo = :refNo"
                        + " , refDate = :refDate"
                        + " , subCode = :subCode"
                        + " , isShowUsedQuotationsAlso = :isShowUsedQuotationsAlso"
                        + " , u_Name = :u_name"
                        + " , u_EntDt = :uEntDt"
                        + " , u_EntDt_LatestLine = :uEntDt"
                        + " , transferDt = :transferDt "
                        + " Where docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("isShowUsedQuotationsAlso", DmShared.iNull(mrTemp.getIsShowUsedQuotationsAlso()).toString());
                psTemp.setString("u_Name", DmShared.xNull(userName));
                psTemp.setString("transferDt", null);
            }
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
//                    if (DmShared.iNull(detail_Mdl.getvSNo()).intValue() > vSNo) {
//                        vSNo = DmShared.iNull(detail_Mdl.getvSNo()).intValue();
//                    } else {
//                        ++vSNo;
//                    }
                    previous_VSno = DmShared.iNull(detail_Mdl.getvSNo()).intValue();
                    ++vSNo;
                    if (!DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && !DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("2")) {
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pOrderDetail ("
                                + "     docId, v_SNo, quotationDocId, quotationV_SNo, itemCode, colourCode, sizeCode, requiredDate,"
                                + "     unitCode, quantity, skuQty, tl_ExcessPer, tl_ExcessQty, tl_ShortagePer, tl_ShortageQty,"
                                + "     rate, remark, isStatus"
                                + " ) VALUES ("
                                + "     :docId, :v_SNo, :quotationDocId, :quotationV_SNo, :itemCode, :colourCode, :sizeCode, :requiredDate,"
                                + "     :unitCode, :quantity, :skuQty, :tl_ExcessPer, :tl_ExcessQty, :tl_ShortagePer, :tl_ShortageQty,"
                                + "     :rate, :remark, :isStatus"
                                + " )";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setInt("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).intValue());
                        psTemp.setString("quotationDocId", DmShared.foriegnKey(detail_Mdl.getQuotationDocId()));
                        psTemp.setString("quotationV_SNo", DmShared.foriegnKey(detail_Mdl.getQuotationV_SNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColorCode()));
                        psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
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
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        if (DmShared.xNull(detail_Mdl.getIsStatus()).equalsIgnoreCase("1") && previous_VSno > 0) {
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

            /*==============================<< Delete records from pOrderIndentDetail  >>====================================*/
            manageIndentStatus(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), true, cnTemp);
            sqlQuery = "DELETE FROM pOrderIndentDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

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
                documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Insert records of Overhead and OverheadLine  >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTemp.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getRecIdOrder(), mrTemp.getVoucherTypeTag(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", "", "", "", "", true, false, cnTemp);
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
    public DmResult<WorkOrder_Mdl> getMoveRec(String strKey) {
        DmResult<WorkOrder_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        WorkOrder_Mdl mrTemp = new WorkOrder_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   po.*"
                    + " , vt.description AS voucherTypeText"
                    + " , sg.subCode AS supplierCode"
                    + " , sg.name AS supplierName"
                    + " , q.docid AS quotationDocId"
                    + " , q.recId AS quotationRecId"
                    + " , q.v_Date AS quotationDate"
                    + " , enq.docId AS enquiryDocId"
                    + " , enq.recId AS enquiryRecId"
                    + " FROM pOrder po"
                    + " LEFT JOIN voucher_Type vt ON po.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sg ON po.subCode = sg.subCode"
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
                mrTemp.setVoucherTypeText(DmShared.xNull(rsTemp.getString("voucherTypeText")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("RefDate")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
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
            sqlQuery = "SELECT"
                    + "   pod.*"
                    + " , im.name as itemName"
                    + " , im.propertyList"
                    + " , im.isBestBefore"
                    + " , um.code AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , um.decimalPlaces AS purchaseUnitDecimalPlaces"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , icm.name as colourName"
                    + " , ism.name sizeName"
                    + " FROM pOrderDetail pod"
                    + " LEFT JOIN itemMast im ON pod.itemCode = im.code"
                    + " LEFT JOIN unitMast um ON pod.unitCode = um.code"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN itemColourMast icm ON icm.code = pod.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pod.sizeCode"
                    + " WHERE pod.docId = :docId "
                    + " ORDER BY pod.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQuotationDocId(DmShared.xNull(rsTemp.getString("quotationDocId")));
                node.setQuotationV_SNo(DmShared.xNull(rsTemp.getString("quotationV_SNo")));

                node.getToleranceMdl().setOrderQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.getToleranceMdl().setTl_ExcessPer(DmServer.vNull(rsTemp.getString("tl_ExcessPer")));
                node.getToleranceMdl().setTl_ExcessQty(DmServer.vNull(rsTemp.getString("tl_ExcessQty")));
                node.getToleranceMdl().setTl_ShortagePer(DmServer.vNull(rsTemp.getString("tl_ShortagePer")));
                node.getToleranceMdl().setTl_ShortageQty(DmServer.vNull(rsTemp.getString("tl_ShortageQty")));

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

                ItemProperty_Mdl ippm_Mdl = new ItemProperty_Mdl();
                ippm_Mdl.setItemCode(node.getSharedKeyFieldCode());
                ippm_Mdl.setColourCode(node.getColorCode());
                ippm_Mdl.setColourName(node.getColor());
                ippm_Mdl.setSizeCode(node.getSizeCode());
                ippm_Mdl.setSizeName(node.getSize());
                node.setProperties(DmShared.xNull(ippm_Mdl.getPropertiesCsv()));

                /*==============================<< Get OverheadLine detail >>===============================*/
                node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));

                /*==============================<< Get Indent detail >>===============================*/
                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(true, DmShared.xNull(strKey), DmShared.xNull(rsTemp.getString("v_SNo")), null, ippm_Mdl, cnTemp);
                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
                }

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdItemDetail().addAll(lstNode);
            /*====================================< Get GUI detail >====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), DmShared.xNull(strKey), DmServer.getSsnSiteCode(this), cnTemp));

            /*==============================<< Get Overhead detail >>===============================*/
            mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));

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

            /*==============================<< Delete record from quotationDetail >>===============================*/
            sqlQuery = " Delete From pOrderDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from quotation >>===============================*/
            sqlQuery = " Delete From pOrder Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
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
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT wo.docId"
                    + " FROM pOrder wo"
                    + " LEFT JOIN voucher_Type vt ON wo.v_Type = vt.v_Type  "
                    + " WHERE vt.category IN (" + vCat + ") "
                    + " AND wo.site_Code = :site_Code "
                    + " AND wo.v_prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY wo.v_Date DESC, vt.short_Name, wo.v_No DESC, wo.recId";
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
                    + "    wo.docId,"
                    + "    wo.recId,"
                    + "    wo.v_Date,"
                    + "    wo.refNo,wo.v_No,"
                    + "    wo.RefDate,"
                    + "    sgv.displayName AS vendorName,"
                    + "    en.recId AS enquiryNo,q.recId AS qRecId,"
                    + "    vt.short_Name,wo.approvedBy,format(wo.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime "
                    + "  FROM pOrder AS wo "
                    + "  LEFT JOIN voucher_Type AS vt ON wo.v_Type = vt.v_Type "
                    + "  LEFT JOIN subGroup AS sgv ON wo.subCode = sgv.subCode "
                    + "  LEFT JOIN ("
                    + "         SELECT wod.docId, wod.quotationDocId"
                    + "         FROM pOrderDetail wod"
                    + "         WHERE wod.quotationDocId IS NOT NULL"
                    + "         GROUP BY wod.docId, wod.quotationDocId"
                    + "  ) pod ON wo.docId = wod.docId"
                    + "  LEFT JOIN quotation AS q ON wod.quotationDocId = q.docId"
                    + "  LEFT JOIN ("
                    + "         SELECT qd.docId, qd.enquiryDocId"
                    + "         FROM quotationDetail qd"
                    + "         WHERE qd.enquiryDocId IS NOT NULL"
                    + "         GROUP BY qd.docId, qd.enquiryDocId"
                    + "  ) qd ON q.docId = qd.docId"
                    + "  LEFT JOIN enquiry en ON qd.enquiryDocId = en.docId"
                    + "	 WHERE vt.category in (" + vCat + ") "
                    + "  AND wo.site_Code = :site_Code "
                    + "  AND wo.v_prefix = :vPrefix";
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

//    private void manageIndentStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
//        DmPreparedStatement psTemp = null, psTemp1 = null;
//        ResultSet rsTemp = null;
//        String sqlQuery, indentDocId = "";
//        double aprQty, ordedQty, ordQty;
//        Integer isStatus;
//
//        try {
//            sqlQuery = "SELECT "
//                    + "   i.docId AS indentDocId"
//                    + " , id.v_SNo AS indentVSno"
//                    + " , pid.orderedQty AS orderQty"
//                    + " , id.aprQty"
//                    + " , id.orderedQty "
//                    + " , id.isStatus"
//                    + " , i.approvedBy"
//                    + " , i.recId AS indentNo"
//                    + " , vt.short_name AS intentType"
//                    + " FROM indentDetail id"
//                    + " LEFT JOIN indent i ON i.docId = id.docId"
//                    + " LEFT JOIN voucher_type vt ON vt.v_type = i.v_Type"
//                    + " INNER JOIN ("
//                    + "     SELECT pid.indentDocId, pid.indentV_SNo, SUM(pid.orderedQty) AS orderedQty"
//                    + "     FROM pOrderIndentDetail pid"
//                    + "     WHERE pid.docId = :docId"
//                    + "     GROUP BY pid.indentDocId, pid.indentV_SNo"
//                    + " ) pid ON id.docId = pid.indentDocId AND id.v_Sno = pid.indentV_SNo";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(docId).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
//                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
//                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent is not Approved");
//                }
//                if (isStatus.intValue() == 1) {
//                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent is Cancelled");
//                }
//                aprQty = rsTemp.getDouble("aprQty");
//                ordedQty = rsTemp.getDouble("orderedQty");
//                ordQty = rsTemp.getDouble("orderQty");
//
//                if (isDelete) {
//                    ordedQty -= ordQty;
//                } else {
//                    ordedQty += ordQty;
//                }
//
//                if (ordedQty > aprQty) {
//                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent overflows by " + DmServer.vNull(ordedQty - aprQty));
//                } else if (ordedQty < 0) {
//                    throw new DmException(500, DmShared.xNull(rsTemp.getString("indentDocId")), DmShared.xNull(rsTemp.getString("indentVSno")), "Indent overflows by " + DmServer.vNull(ordedQty));
//                } else if (ordedQty == aprQty) {
//                    isStatus = 2;
//                } else {
//                    isStatus = 0;
//                }
//
//                if (!indentDocId.equalsIgnoreCase("")) {
//                    indentDocId += ",";
//                }
//
//                indentDocId += DmShared.iNull(rsTemp.getString("indentDocId")).toString();
//
//                sqlQuery = "UPDATE IndentDetail SET"
//                        + "   isStatus = :isStatus"
//                        + " , orderedQty = :orderedQty"
//                        + " where docId = :docId AND v_SNo = :v_SNo";
//                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("indentDocId")).toString());
//                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("indentVSno")).toString());
//                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
//                psTemp1.setString("orderedQty", DmServer.vNull(ordedQty));
//                psTemp1.execute();
//                psTemp1.close();
//            }
//            rsTemp.close();
//            psTemp.close();
//
//            if (!indentDocId.equalsIgnoreCase("")) {
//                sqlQuery = "update indent set u_EntDt_LatestLine = :uEntDt where docId in (" + indentDocId + ") ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
//                psTemp.executeUpdate();
//            }
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp1 != null) {
//                    psTemp1.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//    }
//
    private void manageIndentStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, indentDocId = "";
        double aprQty, ordedQty, ordQty;
        Integer isStatus;

        try {
            sqlQuery = "SELECT "
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
                    + "     SELECT pid.indentDocId, pid.indentV_SNo, "
                    + "         SUM(pid.orderedQty) AS orderedQty"
                    + "     FROM pOrderIndentDetail pid"
                    + "     LEFT JOIN pOrderDetail pd ON pd.docId = pid.docId AND pd.v_SNo = pid.v_SNo "
                    + "     WHERE pid.docId = :docId  AND pd.isStatus <> 1 "
                    + "     GROUP BY pid.indentDocId, pid.indentV_SNo"
                    + " ) pid ON id.docId = pid.indentDocId AND id.v_Sno = pid.indentV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

                sqlQuery = "UPDATE IndentDetail SET"
                        + "   isStatus = :isStatus"
                        + " , orderedQty = :orderedQty"
                        + " where docId = :docId AND v_SNo = :v_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
                sqlQuery = "update indent set u_EntDt_LatestLine = :uEntDt where docId in (" + indentDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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

}
