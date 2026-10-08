package aarogya.inventory.purchaseReturn.server;

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
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_SrvImpl;
import dataman.dmToolkit.accountAdjustment.shared.AdjustmentSave_Mdl;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
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
import aarogya.inventory.purchaseReturn.shared.DefaultValue_Mdl;
import aarogya.inventory.purchaseReturn.shared.Find_Mdl;
import aarogya.inventory.purchaseReturn.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Mdl;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Srv;
import aarogya.inventory.purchaseReturn.shared.PurchaseReturn_Vld;
import aarogya.inventory.purchaseReturn.shared.TxtHBillNo_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHGateEntry_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHItemName_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHTransporter_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.serviceGrid.shared.ServiceGrid_Vld;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.TransportMode;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;

public class PurchaseReturn_SrvImpl extends DmRSSPrivate implements PurchaseReturn_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "pr.v_Type", voucherDateFieldName = "pr.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, filtration = "";
        DmPreparedStatement psTemp = null;
        String condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PURCHASE_RETURN)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(String itemCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "", join = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(itemCode).longValue() > 0) {
                join = " LEFT JOIN ( "
                        + " SELECT ird.departmentCode "
                        + " FROM itemRestrictedToDepartment ird "
                        + " WHERE ird.code = :itemCode "
                        + " ) vrd ON vrd.departmentCode = dm.subCode ";

                condition += " AND vrd.departmentCode IS NULL ";
            }

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlWithParam.setSQL("SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode " + join
                    + "	WHERE sgn.manualCode IN ('CM','PR') "
                    + " and  sgsl.site_Code = :siteCode "
                    + " and sg.isActive = 1 " + condition);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            if (DmShared.iNull(itemCode).longValue() > 0) {
                sqlWithParam.setString("itemCode", itemCode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReason(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code, name FROM gstReasonMast");
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
    public DmResultPageing<List<TxtHBillNo_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBillNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBillNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBillNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subCode).trim().equals("")) {
                condition += " AND pb.subcode = :subcode";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.dNull(v_Date).trim().equals("")) {
                condition += " AND pb.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_RETURN)) {
                condition += " AND vt.category IN ('" + pvc.PURCHASE_BILL + "', '" + pvc.PURCHASE_BILL_DIRECT + "')";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND vt.category IN ('" + pvc.PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "')";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = "SELECT "
                    + "   pb.docId AS code"
                    + " , pb.recId AS name"
                    + " , pb.v_Date AS pbV_Date"
                    + " , pb.partyBillNo AS pbRefNo"
                    + " , pb.partyBillDate AS pbRefDate"
                    + " FROM pBill pb"
                    + " INNER JOIN voucher_Type vt ON pb.v_Type = vt.v_Type"
                    + " INNER JOIN ("
                    + "     SELECT pbd.docId"
                    + "     FROM pBillDetail pbd"
                    + "     WHERE ISNULL(pbd.isFooter, 0) = 0"
                    + "     AND (ISNULL(pbd.isStatus, 0) = 0 OR pbd.docId = :billDocId)"
                    + "     GROUP BY pbd.docId"
                    + " ) AS v ON pb.docId = v.docId"
                    + " WHERE ISNULL(pb.approvedBy, '') <> ''"
                    + " AND pb.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("billDocId", DmShared.iNull(billDocId).toString());
            if (!DmShared.xNull(subCode).trim().equals("")) {
                sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            }
            if (!DmShared.dNull(v_Date).trim().equals("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                listNode.setPbRefNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                listNode.setPbRefDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "", itemCatFilteration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition = " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlQuery = "SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , im.manualCode"
                    + " , im.hsnCode"
                    + " , hsn.hsn"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , im.sku AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces"
                    + " , im.purchaseUnit AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces"
                    + " , um.gstUnitCode"
                    + " , im.subCode AS iSubCode "
                    + " , im.purchaseReturnAc "
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code"
                    + " LEFT JOIN hsnMast hsn ON hsn.code = im.hsnCode "
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND im.isActive = 1"
                    + " AND sl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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

                //IAP
                listNode.setISubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
                listNode.setPurchaseReturnAcCode(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTranspostMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (TransportMode transportMode : TransportMode.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(String.valueOf(transportMode.getId())));
                listNode.setSharedKeyFieldName(DmShared.xNull(transportMode.getValue()));
                lstTemp.add(listNode);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getEwayBillReq(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
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
    public DmResult<PurchaseReturn_Mdl> getItemDetail(String v_Type, String billDocId, String location, String supplierBillDate, String docId,
            String rcmApplicable, boolean isIncludeTaxInCost, String isSiteStateUT, String purchaseAcPostingBasedOn, boolean isInventoryAccountingActive) {
        DmResult<PurchaseReturn_Mdl> dmrTemp = new DmResult();
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        PurchaseReturn_Mdl mrTemp = new PurchaseReturn_Mdl();
        GrdItemDetail_Mdl node;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", pendingQty = "", join = "", itemCatFilteration = "";
        ServiceDetail_Mdl serviceDetail_Mdl;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) + ISNULL(prd.acceptQty, 0) AS pendingQty";
                condition += " AND (CASE WHEN (pbd.isStatus = 0) THEN 1 WHEN (pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0) + ISNULL(prd.acceptQty,0)) > 0) THEN 1 ELSE 0 END) = 1";

                join = " LEFT JOIN ("
                        + "     SELECT prd.*"
                        + "     FROM pBillDetail prd"
                        + "     WHERE prd.docid = :docId"
                        + " ) AS prd ON pbd.docId = prd.pbDocId AND pbd.v_SNO = prd.pbV_SNo";
            } else {
                pendingQty = " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) AS pendingQty";
                condition += " AND ISNULL(pbd.isStatus, 0) = 0";
            }

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            //<Ag> select Accounts            
            sqlQuery = "SELECT"
                    + "   im.code AS itemCode"
                    + " , im.name AS itemName"
                    + " , im.itemGSTNature"
                    + " , im.itsBasedOn"
                    + " , pbd.hsnCode"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , icm.code AS colourCode"
                    + " , icm.name AS colourName"
                    + " , ism.code AS sizeCode"
                    + " , ism.name AS sizeName"
                    + " , imm.code AS itemMakeCode"
                    + " , imm.companyName AS itemMakeName"
                    + " , pb.docId AS pbDocId"
                    + " , pb.recId AS pbRecId"
                    + " , pb.v_Type AS pbV_Type"
                    + " , pb.v_Date AS pbV_Date"
                    + " , pb.oh_Amt_Net AS pbNetAmount"
                    + " , pbd.v_SNo AS pbV_SNo"
                    + " , pc.docId AS pcDocId"
                    + " , pc.recId AS pcRecId"
                    + " , pcd.v_SNo AS pcV_SNo"
                    + " , po.docId AS poDocId"
                    + " , po.recId AS poRecId"
                    + " , pod.v_SNo AS poV_SNo"
                    + " , po.refNo AS poRefNo"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , um.code AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , um.decimalPlaces AS purcahseUnitDecimalPlaces"
                    + " , im.purchaseUnitConversionRate"
                    + " , um.gstUnitCode" + pendingQty
                    + " , pbd.rate"
                    + " , pbd.mrp"
                    + " , pbd.skuMrp"
                    + " , pbd.batchNo"
                    + " , pbd.lotNo"
                    + " , pbd.expiryDate"
                    + " , pbd.manufacturingDate"
                    + " , pbd.remark"
                    + " , pbd.ibCode"
                    + " , hsn.hsn as hsnNo "
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
                    + " , pbd.oh_Amt_Net\n"
                    + " , pbd.oh_Ac_Item \n"
                    + " , pb.oh_At_FinalDiscount\n"
                    + " , pb.oh_Amt_FinalDiscount\n"
                    + " , pb.rcmApplicable "
                    + " , im.purchaseReturnAc "
                    + " , im.subCode AS iSubCode "
                    + " FROM pBill pb"
                    + " LEFT JOIN pBillDetail pbd ON pb.docId = pbd.docId" + join
                    + " LEFT JOIN pChallanDetail pcd ON pbd.pcDocId = pcd.docId AND pbd.pcV_SNo = pcd.v_SNo"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN pOrderDetail pod ON pbd.poDocId = pod.docId AND pbd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON pbd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON pbd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast imm ON pbd.itemMakeCode = imm.code "
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = pbd.hsnCode "
                    + " WHERE pb.docId = :billDocId"
                    + " AND ISNULL(pbd.isFooter, 0) = 0" + condition
                    + " ORDER BY pbd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(docId));
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
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsnNo")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPbNetAmount(DmShared.xNull(rsTemp.getString("pbNetAmount")));

                //Challan Details
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purcahseUnitDecimalPlaces")));
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                //Quantities
                node.setQuantity(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                double skuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                //Rates
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                double amount = DmShared.vNull(node.getQuantity()) * DmShared.vNull(node.getRate());
                node.setAmount(DmServer.vNull(amount, "0.00"));

                //Properties
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

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

                //Amount
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                node.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                node.setItemValue(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                mrTemp.setFinalDiscountPer(DmServer.vNull(rsTemp.getString("oh_At_FinalDiscount"), "0.00"));
                mrTemp.setFinalDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_FinalDiscount"), "0.00"));

                //Purchase Return A/c (Based On Item Master)
                node.setItemMastPurchaseReturnAc(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
                if (isInventoryAccountingActive) {
                    node.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
                } else {
                    if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                        if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.ITEM.getId()) {
                            node.setIsubCode(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
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

                    //Get Purchase Return A/c From GSTS
                    if (!isInventoryAccountingActive) {
                        if (!DmShared.xNull(purchaseAcPostingBasedOn).trim().equalsIgnoreCase("")) {
                            if (STEnum.PurchaseSalesAcPostingBasedOn.getViaKey(DmShared.iNull(purchaseAcPostingBasedOn).intValue()).getId() == STEnum.PurchaseSalesAcPostingBasedOn.GSTS.getId()) {
                                for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                                    if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.PURCHASE_RETURN_ACCOUNT.getShortName())) {
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

                node.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));
                mrTemp.setRcmApplicable(DmShared.xNull(rsTemp.getString("rcmApplicable")));

                //Add Node
                lstNode.add(node);
            }
            /*==============================<< Get Service detail >>===============================*/
            if (!DmShared.xNull(billDocId).equalsIgnoreCase("")) {
                sqlQuery = "SELECT\n"
                        + "  pbd.v_SNo\n"
                        + " , pbd.itemCode\n"
                        + " , pbd.hsnCode\n"
                        + " , pbd.itemGSTNature\n"
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
                        + " , um.gstUnitCode\n"
                        + " , hsn.hsn \n"
                        + " , pbd.serviceEvalTypeCode \n"
                        + " , pbd.serviceEvalRate \n"
                        + " , pbd.oh_Ac_Item \n"
                        + " FROM pBillDetail pbd\n"
                        + " LEFT JOIN itemMast im ON pbd.itemCode = im.code\n"
                        + " LEFT JOIN gstType it ON it.code = pbd.gstTypeCode \n"
                        + " LEFT JOIN hsnMast hsn ON hsn.code = pbd.hsnCode \n"
                        + " LEFT JOIN unitMast um ON pbd.unitCode = um.code\n"
                        + " WHERE pbd.docId = :docId \n"
                        + " AND ISNULL(pbd.isFooter, 0) = 2\n"
                        + " AND ISNULL(pbd.isStatus, 0) = 0\n"
                        + " ORDER BY pbd.v_SNo\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(billDocId).toString());
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

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            sqlQuery = "select approvedBy,  approvalDateTime,"
                    + " (case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + " from pBill where docId = :docId ";
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
                                    + " isPosted = case when isSystem_Generated=0 then 0 when isSystem_Generated=1 then 1 end "
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

    private WalkThroughDefault_Mdl getWalkThroughFor(PurchaseReturn_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
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
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.category"
                    + " , vt.guiTemplateCode"
                    + " , vt.balancingAc"
                    + " FROM voucher_Type vt"
                    //                    + " WHERE vt.category = '" + pvc.PURCHASE_RETURN + "'"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")) {
                psTemp.setString("category", pvc.PURCHASE_RETURN);
            } else {
                psTemp.setString("category", pvc.PURCHASE_RETURN_UNREGISTERED);
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                defaultValue.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                defaultValue.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
            }

            if (rsTemp.getString("balancingAc") == null) {
                throw new DmException(0, "Balancing A/c not defined in Voucher Type.");
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
            sqlQuery = "SELECT e.isIncludeTaxInCost, e.purchaseSalesAcPostingBasedOn,e.inv_DefaultPurchaseDepartment, \n"
                    + "sg.name as defaultPurchaseDepartmentName,e.isIncludeOverHeadsInCost \n"
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
            /*==============================<< ************************* >>====================================*/

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
    public DmResult<String> saveRecord(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "",
                roundOffItemGstNature = "", roundOffRefDocId = "", roundOffRefV_Type = "", roundOffRefDocValue = "",
                invoiceCGST = "", invoiceSGST = "", invoiceIGST = "",
                invoiceUGST = "", invoiceValue = "", roundOffItemGSTNature = "", roundOffGstTypeCode = "";
        ResultSet rsTemp = null;
        GstTrans_Mdl gstTrans_Mdl = null;
        dmrTemp.setData("");

        try {
            PurchaseReturn_Vld.chkSave(mrTempCurrent);
            ServiceGrid_Vld.chkSave(mrTempCurrent.getGrdServiceDetail());
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            DmServer.checkForwordVoucherDateTime(mrTempCurrent.getVoucherDate(), "");
            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
            }
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (!DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
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
            }

            /*==============================<< Check items for pBill item change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM pBillDetail pbd " + DmShared.xNull(mrTempCurrent.getPbItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Bill has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check for Round Off Item in Enviro >>====================================*/
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
//                sqlQuery = "SELECT ohm.itemCode, im.hsnCode, im.itemGSTNature"
//                        + " FROM overHeadMast ohm"
//                        + " LEFT JOIN itemMast im ON ohm.itemCode = im.code"
//                        + " WHERE ohm.manualCode = 'ROFF'";
                sqlQuery = "SELECT im.code as itemCode, im.hsnCode, im.itemGstNature, iid.gstTypeCode\n"
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
            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTempCurrent.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTempCurrent.setRecIdPurchaseReturn(DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTempCurrent.setSharedKeyFieldName(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getRecIdValue()));
                mrTempCurrent.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTempCurrent), DmServer.getSsnUserName(this), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), mrTempCurrent.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("pBill", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTempCurrent.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Insert / Update record into pBill >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO pBill ("
                        + "     docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix, partyBillNo, partyBillDate,"
                        + "     gstReasonCode, subCode, consignee, partyType, gsTin, gstinDate, rcmApplicable, placeOfSupply,"
                        + "     remark, wtaCode, guiConfigCode, site_Code, preparedBy, preparedDate, u_EntDt_LatestLine, transferDt,"
                        + "     gateDocId, transporterCode, EWayBillNo, EWayBillDt, isEWayBillRequired, transportMode, vehicleNo, "
                        + "     oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                        + "     oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                        + "     oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Total, \n"
                        + "     oh_Amt_RoundOff, oh_Amt_Net, oh_Amt_LandedValue, locationCode, \n"
                        + "     oh_Ac_FinalDiscount, oh_Ac_RoundOff, balancingAc) \n"
                        + " VALUES ("
                        + "     :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix, :partyBillNo, :partyBillDate,"
                        + "     :gstReasonCode, :subCode, :consignee, :partyType, :gsTin, :gstinDate, :rcmApplicable, :placeOfSupply,"
                        + "     :remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :preparedDate, :uEntDt, :transferDt,"
                        + "     :gateDocId, :transporterCode, :EWayBillNo, :EWayBillDt, :isEWayBillRequired, :transportMode, :vehicleNo, "
                        + "     :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                        + "     :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                        + "     :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Total, \n"
                        + "     :oh_Amt_RoundOff, :oh_Amt_Net, :oh_Amt_LandedValue, :locationCode, "
                        + "     :oh_Ac_FinalDiscount, :oh_Ac_RoundOff, :balancingAc)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTempCurrent.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getPartyBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getPartyBillDate()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTempCurrent.getGstReasonCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTempCurrent.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("isEWayBillRequired", DmShared.xNull(mrTempCurrent.getEwayBillRequiredCode()));
                psTemp.setString("transportMode", DmShared.foriegnKey(mrTempCurrent.getTransportModeCode()));
                psTemp.setString("vehicleNo", DmShared.xNull(mrTempCurrent.getVehicleNo()));
                psTemp.setString("gateDocId", DmShared.foriegnKey(mrTempCurrent.getGateNoTag()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTempCurrent.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTempCurrent.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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
                sqlQuery = "UPDATE pBill SET"
                        + "  v_Date = :v_Date"
                        + " ,partyBillNo = :partyBillNo"
                        + " ,partyBillDate = :partyBillDate"
                        + " ,gstReasonCode = :gstReasonCode"
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
                        + " ,transportMode = :transportMode "
                        + " ,vehicleNo = :vehicleNo "
                        + " ,isEWayBillRequired = :isEWayBillRequired "
                        + " ,gateDocId = :gateDocId "
                        + " ,transporterCode = :transporterCode "
                        + " ,u_EntDt = :uEntDt"
                        + " ,u_EntDt_LatestLine = :uEntDt"
                        + " ,u_Name = :u_Name"
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
                        + " ,transferDt = :transferDt"
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getPartyBillNo()));
                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getPartyBillDate()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTempCurrent.getGstReasonCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTempCurrent.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("EWayBillNo", DmShared.xNull(mrTempCurrent.getEwayNo()));
                psTemp.setDate("EWayBillDt", DmServer.convertDate(mrTempCurrent.getEwayDate()));
                psTemp.setString("isEWayBillRequired", DmShared.xNull(mrTempCurrent.getEwayBillRequiredCode()));
                psTemp.setString("transportMode", DmShared.foriegnKey(mrTempCurrent.getTransportModeCode()));
                psTemp.setString("vehicleNo", DmShared.xNull(mrTempCurrent.getVehicleNo()));
                psTemp.setString("gateDocId", DmShared.foriegnKey(mrTempCurrent.getGateNoTag()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTempCurrent.getTransporterCode()));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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

            /*==============================<< Manage PBILL (Returned Qty) >>====================================*/
            manageReturnedQty(DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString(), true, cnTemp);

            /*==============================<< Delete records into gstTrans >>====================================*/
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode())), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTempCurrent.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Delete records from pBillDetail >>====================================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pBillDetail", "ibCode", " AND docid = '" + DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString() + "'", cnTemp);

            sqlQuery = "Delete From pBillDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into pBillDetail >>====================================*/
            List<GrdItemDetail_Mdl> listDetail = mrTempCurrent.getListItemDetail();
            int vSNo = 0;
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    if (!detail_Mdl.isServiceRecord()) {
                        detail_Mdl.setV_SNo(String.valueOf(++vSNo));
                        sqlQuery = "INSERT INTO pBillDetail ("
                                + "     docId, v_SNo, pbDocId, pbV_SNo, pcDocId, pcV_SNo, poDocId, poV_SNo, departmentCode, itemCode, hsnCode, itemGSTNature, batchNo, lotNo, expiryDate,"
                                + "     manufacturingDate, colourCode, sizeCode,itemMakeCode, mrp, skuMrp, unitCode, docQty, acceptQty, skuQty, "
                                + "     skuQtyForRate, landedSkuValue, landedSkuRate, skuRate, rate, remark"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_at_FinalDiscount, oh_Amt_FinalDiscount, oh_Amt_Net\n"
                                + "     , oh_Ac_SGST, oh_Ac_UGST, oh_Ac_CGST, oh_Ac_IGST\n"
                                + "     , oh_Ac_SGST_RCM, oh_Ac_UGST_RCM, oh_Ac_CGST_RCM, oh_Ac_IGST_RCM\n"
                                + "     , gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                + " Values ("
                                + "     :docId, :v_SNo, :pbDocId, :pbV_SNo, :pcDocId, :pcV_SNo, :poDocId, :poV_SNo, :departmentCode, :itemCode, :hsnCode, :itemGSTNature, :batchNo, :lotNo, :expiryDate,"
                                + "     :manufacturingDate, :colourCode, :sizeCode,:itemMakeCode, :mrp, :skuMrp, :unitCode, :docQty, :acceptQty, :skuQty, "
                                + "     :skuQtyForRate, :landedSkuValue, :landedSkuRate, :skuRate, :rate, :remark"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_at_FinalDiscount, :oh_Amt_FinalDiscount, :oh_Amt_Net\n"
                                + "     , :oh_Ac_SGST, :oh_Ac_UGST, :oh_Ac_CGST, :oh_Ac_IGST\n"
                                + "     , :oh_Ac_SGST_RCM, :oh_Ac_UGST_RCM, :oh_Ac_CGST_RCM, :oh_Ac_IGST_RCM\n"
                                + "     , :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                        psTemp.setString("pbDocId", DmShared.foriegnKey(detail_Mdl.getPbDocId()));
                        psTemp.setString("pbV_SNo", DmShared.foriegnKey(detail_Mdl.getPbV_SNo()));
                        psTemp.setString("pcDocId", DmShared.foriegnKey(detail_Mdl.getPcDocId()));
                        psTemp.setString("pcV_SNo", DmShared.foriegnKey(detail_Mdl.getPcV_SNo()));
                        psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                        psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                        psTemp.setString("departmentCode", DmShared.foriegnKey(detail_Mdl.getDepartmentCode()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
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
                        psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getQuantity()));
                        psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getQuantity()));
                        psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                        psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
                        psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                        psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                        psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
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

            //========================================(Service Grid Modal)==========================================================
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                    if (detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        detail_Mdl.setV_SNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO pBillDetail ("
                                + "     docId, v_SNo, itemCode, hsnCode, docQty, acceptQty, rate, itemGSTNature ,unitCode ,isFooter"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net\n"
                                + "     , oh_Ac_SGST, oh_Ac_UGST, oh_Ac_CGST, oh_Ac_IGST\n"
                                + "     , oh_Ac_SGST_RCM, oh_Ac_UGST_RCM, oh_Ac_CGST_RCM, oh_Ac_IGST_RCM\n"
                                + "     , serviceEvalTypeCode, serviceEvalRate, gstTypeCode, isManualGstType, isManualTax, oh_Ac_Item)\n"
                                + " Values ("
                                + "     :docId, :v_SNo, :itemCode, :hsnCode, :docQty, :acceptQty, :rate, :itemGSTNature ,:unitCode , 2"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :oh_Ac_SGST, :oh_Ac_UGST, :oh_Ac_CGST, :oh_Ac_IGST\n"
                                + "     , :oh_Ac_SGST_RCM, :oh_Ac_UGST_RCM, :oh_Ac_CGST_RCM, :oh_Ac_IGST_RCM\n"
                                + "     , :serviceEvalTypeCode, :serviceEvalRate, :gstTypeCode, :isManualGstType, :isManualTax, :oh_Ac_Item)\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getV_SNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));

                        psTemp.setString("docQty", DmServer.vNull(detail_Mdl.getQuantity()));
                        psTemp.setString("acceptQty", DmServer.vNull(detail_Mdl.getQuantity()));

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
                    + " AND pBillDetail.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Manage PBILL (Returned Qty) >>==============================*/
            manageReturnedQty(DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString(), false, cnTemp);
            /*==============================<< Get header tax details >>====================================*/
            sqlQuery = "SELECT"
                    + "   pr.oh_Amt_CGST"
                    + " , pr.oh_Amt_SGST"
                    + " , pr.oh_Amt_IGST"
                    + " , pr.oh_Amt_UGST"
                    + " , pr.oh_Amt_Net"
                    + " FROM pBill pr"
                    + " WHERE pr.docId = :docId";
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
                            + "     docId, v_SNo, itemCode, hsnCode, docQty, acceptQty, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter"
                            + "     , oh_Amt_SubTotal, itemGSTNature, gstTypeCode)"
                            + " Values ("
                            + "     :docId, :v_SNo, :itemCode, :hsnCode, :docQty, :acceptQty, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, 1"
                            + "     , :oh_Amt_SubTotal, :itemGSTNature, :gstTypeCode)";
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
                    psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTempCurrent.getRoundOffAmount(), "0.00"));
                    psTemp.setString("itemGSTNature", DmShared.foriegnKey(roundOffItemGSTNature));
                    psTemp.setString("gstTypeCode", DmShared.foriegnKey(roundOffGstTypeCode));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*==============================<< Insert records into gstTrans >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    //<editor-fold defaultstate="collapsed" desc="Set gstTrans_Mdl">
                    gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(DmShared.xNull(detail_Mdl.getV_SNo()));
                    gstTrans_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTempCurrent.getSupplierCode()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTempCurrent.getSupplierCode()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                    gstTrans_Mdl.setGstinDate(DmShared.dNull(mrTempCurrent.getSupplierGstinDate()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));
                    gstTrans_Mdl.setRefDocId(DmShared.xNull(detail_Mdl.getPbDocId()));
                    gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTempCurrent.getBillNo()));
                    gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTempCurrent.getBillDate()));
                    gstTrans_Mdl.setRefPartyBillNo(DmShared.xNull(mrTempCurrent.getPartyBillNo()));
                    gstTrans_Mdl.setRefPartyBillDt(DmShared.dNull(mrTempCurrent.getPartyBillDate()));
                    gstTrans_Mdl.setRefV_Type(DmShared.xNull(detail_Mdl.getPbV_Type()));
                    gstTrans_Mdl.setRefDocValue(DmShared.xNull(detail_Mdl.getPbNetAmount()));
                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTempCurrent.getGstReasonCode()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(mrTempCurrent.getRcmApplicable()));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(detail_Mdl.getHsnCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    gstTrans_Mdl.setDepartmentCode(DmShared.xNull(detail_Mdl.getDepartmentCode()));
                    gstTrans_Mdl.setQty(DmShared.xNull(detail_Mdl.getQuantity()));
                    gstTrans_Mdl.setGstUnitCode(DmShared.xNull(detail_Mdl.getGstUnitCode()));
                    gstTrans_Mdl.setInvoiceCGST(DmShared.xNull(invoiceCGST));
                    gstTrans_Mdl.setInvoiceSGST(DmShared.xNull(invoiceSGST));
                    gstTrans_Mdl.setInvoiceIGST(DmShared.xNull(invoiceIGST));
                    gstTrans_Mdl.setInvoiceUGST(DmShared.xNull(invoiceUGST));
                    gstTrans_Mdl.setInvoiceValue(DmShared.xNull(invoiceValue));

                    gstTrans_Mdl.setTransporterCode(DmShared.xNull(mrTempCurrent.getTransporterCode()));
                    gstTrans_Mdl.setTransportMode(DmShared.xNull(mrTempCurrent.getTransportModeCode()));
                    gstTrans_Mdl.setVehicleNo(DmShared.xNull(mrTempCurrent.getVehicleNo()));
                    gstTrans_Mdl.setIsEWayBillRequired(DmShared.xNull(mrTempCurrent.getEwayBillRequiredCode()));
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
                            + "  prd.oh_Amt_Gross"
                            + " , prd.oh_Amt_Taxable"
                            + " , prd.oh_at_CGST"
                            + " , prd.oh_Amt_CGST"
                            + " , prd.oh_at_SGST"
                            + " , prd.oh_Amt_SGST"
                            + " , prd.oh_at_IGST"
                            + " , prd.oh_Amt_IGST"
                            + " , prd.oh_at_UGST"
                            + " , prd.oh_Amt_UGST"
                            + " , prd.oh_Amt_Net"
                            + " FROM pBillDetail prd"
                            + " WHERE prd.docId = :docId"
                            + " AND prd.v_Sno = :v_Sno";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    psTemp.setString("v_Sno", DmShared.xNull(detail_Mdl.getV_SNo()));
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
                    roundOffRefDocId = gstTrans_Mdl.getRefDocId();
                    roundOffRefV_Type = gstTrans_Mdl.getRefV_Type();
                    roundOffRefDocValue = gstTrans_Mdl.getRefDocValue();
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
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTempCurrent.getVoucherDate()));
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTempCurrent.getSupplierCode()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTempCurrent.getSupplierCode()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTempCurrent.getSupplierGstin()));
                    gstTrans_Mdl.setGstinDate(DmShared.dNull(mrTempCurrent.getSupplierGstinDate()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTempCurrent.getPlaceOfSupply()));
                    gstTrans_Mdl.setRefDocId(DmShared.xNull(roundOffRefDocId));
                    gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTempCurrent.getBillNo()));
                    gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTempCurrent.getBillDate()));
                    gstTrans_Mdl.setRefPartyBillNo(DmShared.xNull(mrTempCurrent.getPartyBillNo()));
                    gstTrans_Mdl.setRefPartyBillDt(DmShared.dNull(mrTempCurrent.getPartyBillDate()));
                    gstTrans_Mdl.setRefV_Type(DmShared.xNull(roundOffRefV_Type));
                    gstTrans_Mdl.setRefDocValue(DmShared.xNull(roundOffRefDocValue));
                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTempCurrent.getGstReasonCode()));
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
                    gstTrans_Mdl.setTransportMode(DmShared.xNull(mrTempCurrent.getTransportModeCode()));
                    gstTrans_Mdl.setVehicleNo(DmShared.xNull(mrTempCurrent.getVehicleNo()));
                    gstTrans_Mdl.setIsEWayBillRequired(DmShared.xNull(mrTempCurrent.getEwayBillRequiredCode()));
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
//            /*==============================<< Account Posting >>====================================*/
//            AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive(), cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", "", "", "", "", false,true, cnTemp);
/*==============================<< Account Posting Inventory to Balancing Ac>>====================================*/
            AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive(), mrTempCurrent.getDefaultValueMdl().isIncludeOverHeadsInCost(), true, cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", mrTempCurrent.getPartyBillNo(), mrTempCurrent.getPartyBillDate(), "", "", false, true, cnTemp);
            /*==============================<< Account Posting Balancing Ac to Party on Approval click>>====================================*/
            AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive(), mrTempCurrent.getDefaultValueMdl().isIncludeOverHeadsInCost(), false, cnTemp), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdPurchaseReturn(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", mrTempCurrent.getPartyBillNo(), mrTempCurrent.getPartyBillDate(), "", "", false, false, cnTemp);

            /*==============================<< Approve Document >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTempCurrent.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (mrTempCurrent.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTempCurrent.getModifyReasonMdl(), DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), userName, cnTemp);
            }

            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTempCurrent.getGuiMdl().getGuiRowList(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Save Adjustment >>====================================*/
            if (DmShared.iNull(mrTempCurrent.getBillNoTag()).longValue() != 0) {
                setAdjustments(mrTempCurrent, cnTemp);
            }

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
    public DmResult<PurchaseReturn_Mdl> getMoveRec(String strKey) {
        DmResult<PurchaseReturn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PurchaseReturn_Mdl mrTemp = new PurchaseReturn_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;
        ServiceDetail_Mdl serviceDetail_Mdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT pr.docid,pr.recId,pr.v_No, pr.recIdPrefix ,pr.v_Type, pr.v_Date, pr.partyBillNo,pr.partyBillDate \n"
                    + " , pr.subcode,pr.partyType, pr.rcmApplicable ,pr.placeOfSupply ,pr.remark, pr.preparedBy,pr.u_Name,pr.approvedBy,pr.wtaCode \n"
                    + " , pr.guiConfigCode ,pr.oh_Amt_Gross,pr.gstReasonCode, pr.u_EntDt_LatestLine,pr.preparedDate, pr.approvalDateTime ,pr.u_EntDt \n"
                    + " , vt.description AS voucherTypeName, sgs.name AS supplierName, grm.name AS gstReasonName, c.stateCode AS supplierStateCode \n"
                    + " , sgs.partyType AS supplierType, pr.gstin AS supplierGstin, pr.gstinDate AS supplierGstinDate, pr.gateDocId \n"
                    + " , gio.recId as gateRecId, pr.transporterCode, sg1.name as transporterName, pr.EWayBillNo, pr.EWayBillDt \n"
                    + " , pr.isEWayBillRequired, pr.transportMode, pr.vehicleNo, pr.oh_Amt_GrossGoods, pr.oh_Amt_GrossService \n"
                    + " , pr.oh_Amt_Gross, pr.oh_Amt_Discount, pr.oh_Amt_TaxableGoods, pr.oh_Amt_TaxableService, pr.oh_Amt_Taxable \n"
                    + " , pr.oh_Amt_SGST, pr.oh_Amt_UGST, pr.oh_Amt_CGST, pr.oh_Amt_IGST, pr.oh_Amt_SubTotal, pr.oh_at_FinalDiscount \n"
                    + " , pr.oh_Amt_FinalDiscount, pr.oh_Amt_Total, pr.oh_Amt_RoundOff, pr.oh_Amt_Net, pr.oh_Amt_LandedValue \n"
                    + " , pr.locationCode, pr.oh_Ac_FinalDiscount, pr.oh_Ac_RoundOff, pr.balancingAc \n"
                    + " FROM pBill pr \n"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type \n"
                    + " LEFT JOIN subGroup sgs ON pr.subcode = sgs.subCode \n"
                    + " LEFT JOIN city c ON sgs.cityCode = c.cityCode \n"
                    + " LEFT JOIN gstReasonMast grm ON pr.gstReasonCode = grm.code \n"
                    + " LEFT JOIN subGroup sg1 ON sg1.subCode = pr.transporterCode \n"
                    + " LEFT JOIN gateInOut gio ON gio.docId = pr.gateDocId \n"
                    + " WHERE pr.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.getRecIdPurchaseReturn().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdPurchaseReturn().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdPurchaseReturn().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));

                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setSupplierCode(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
                mrTemp.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                mrTemp.setPartyBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                mrTemp.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                mrTemp.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setGateNoTag(DmShared.xNull(rsTemp.getString("gateDocId")));
                mrTemp.setGateNoText(DmShared.xNull(rsTemp.getString("gateRecId")));
                mrTemp.setTransporterCode(DmShared.xNull(rsTemp.getString("transporterCode")));
                mrTemp.setTransporterName(DmShared.xNull(rsTemp.getString("transporterName")));
                mrTemp.setEwayNo(DmShared.xNull(rsTemp.getString("EWayBillNo")));
                mrTemp.setEwayDate(DmShared.dNull(rsTemp.getDate("EWayBillDt")));
                if (!DmShared.xNull(rsTemp.getString("transportMode")).trim().equalsIgnoreCase("")) {
                    mrTemp.setTransportModeCode(DmShared.xNull(rsTemp.getString("transportMode")));
                    mrTemp.setTransportModeName(DmShared.xNull(STEnum.TransportMode.getViaKey(DmShared.iNull(rsTemp.getString("transportMode")).intValue()).getValue()));
                }
                mrTemp.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));

                if (!DmShared.xNull(rsTemp.getString("isEWayBillRequired")).equalsIgnoreCase("")) {
                    mrTemp.setEwayBillRequiredCode(DmShared.xNull(rsTemp.getString("isEWayBillRequired")));
                    if (DmShared.iNull(rsTemp.getString("isEWayBillRequired")).intValue() == 1) {
                        mrTemp.setEwayBillRequiredName("Yes");
                    } else {
                        mrTemp.setEwayBillRequiredName("No");
                    }
                }

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

                //Accounts
                mrTemp.setFinalDiscountAcCode(rsTemp.getString("oh_Ac_FinalDiscount"));
                mrTemp.setRoundOffAcCode(rsTemp.getString("oh_Ac_RoundOff"));
                mrTemp.setBalancingAc(rsTemp.getString("balancingAc"));

                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT"
                    + "   im.code AS itemCode"
                    + " , im.name AS itemName"
                    + " , prd.hsnCode"
                    + " , hsn.hsn "
                    + " , im.itsBasedOn"
                    + " , prd.itemGSTNature"
                    + " , im.isBestBefore"
                    + " , im.propertyList"
                    + " , icm.code AS colourCode"
                    + " , icm.name AS colourName"
                    + " , ism.code AS sizeCode"
                    + " , ism.name AS sizeName"
                    + " , imm.code AS itemMakeCode"
                    + " , imm.companyName AS itemMakeName"
                    + " , pb.docId AS pbDocId"
                    + " , pb.recId AS pbRecId"
                    + " , pb.v_Type AS pbV_Type"
                    + " , pb.v_Date AS pbV_Date"
                    + " , pb.oh_Amt_Net AS pbNetAmount"
                    + " , pbd.v_SNo AS pbV_SNo"
                    + " , pc.docId AS pcDocId"
                    + " , pc.recId AS pcRecId"
                    + " , pcd.v_SNo AS pcV_SNo"
                    + " , po.docId AS poDocId"
                    + " , po.recId AS poRecId"
                    + " , pod.v_SNo AS poV_SNo"
                    + " , po.refNo AS poRefNo"
                    + " , ums.code AS skuCode"
                    + " , ums.unit AS skuName"
                    + " , ums.decimalPlaces AS skuDecimalPlaces"
                    + " , um.code AS purchaseUnitCode"
                    + " , um.unit AS purchaseUnitName"
                    + " , um.decimalPlaces AS purcahseUnitDecimalPlaces"
                    + " , im.purchaseUnitConversionRate"
                    + " , um.gstUnitCode"
                    + " , prd.acceptQty"
                    + " , prd.skuQty"
                    + " , prd.rate"
                    + " , prd.landedSkuValue"
                    + " , prd.landedSkuRate"
                    + " , prd.skuRate"
                    + " , prd.oh_Amt_Gross"
                    + " , prd.mrp"
                    + " , prd.skuMrp"
                    + " , prd.batchNo"
                    + " , prd.lotNo"
                    + " , prd.expiryDate"
                    + " , prd.manufacturingDate"
                    + " , prd.ibCode"
                    + " , prd.v_SNo"
                    + " , prd.remark"
                    + " , prd.departmentCode"
                    + " , sgd.name AS departmentName"
                    + " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) + ISNULL(prd.acceptQty, 0) AS pendingQty"
                    + " , prd.isManualTax\n"
                    + " , prd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , prd.gstTypeCode\n"
                    + " , prd.oh_Amt_Gross\n"
                    + " , prd.oh_Amt_Discount\n"
                    + " , prd.oh_at_Discount\n"
                    + " , prd.oh_Amt_Taxable\n"
                    + " , prd.oh_at_SGST\n"
                    + " , prd.oh_Amt_SGST\n"
                    + " , prd.oh_at_UGST\n"
                    + " , prd.oh_Amt_UGST\n"
                    + " , prd.oh_at_CGST\n"
                    + " , prd.oh_Amt_CGST\n"
                    + " , prd.oh_at_IGST\n"
                    + " , prd.oh_Amt_IGST\n"
                    + " , prd.oh_Amt_SubTotal\n"
                    + " , prd.oh_at_FinalDiscount\n"
                    + " , prd.oh_Amt_FinalDiscount\n"
                    + " , prd.oh_Amt_Net\n"
                    + " , prd.oh_Ac_SGST, prd.oh_Ac_UGST, prd.oh_Ac_CGST, prd.oh_Ac_IGST\n"
                    + " , prd.oh_Ac_SGST_RCM, prd.oh_Ac_UGST_RCM, prd.oh_Ac_CGST_RCM, prd.oh_Ac_IGST_RCM\n"
                    + " , prd.oh_Ac_Item\n"
                    + " , im.purchaseReturnAc AS itemMastPurchaseReturnAc \n"
                    + " FROM pBill pr"
                    + " LEFT JOIN pBillDetail prd ON pr.docId = prd.docId"
                    + " LEFT JOIN pBillDetail pbd ON prd.pbDocId = pbd.docId AND prd.pbV_SNo = pbd.v_SNo"
                    + " LEFT JOIN pBill pb ON pbd.docId = pb.docId"
                    + " LEFT JOIN pChallanDetail pcd ON prd.pcDocId = pcd.docId AND prd.pcV_SNo = pcd.v_SNo"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId"
                    + " LEFT JOIN pOrderDetail pod ON prd.poDocId = pod.docId AND prd.poV_SNo = pod.v_SNo"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId"
                    + " LEFT JOIN itemMast im ON prd.itemCode = im.code"
                    + " LEFT JOIN itemColourMast icm ON prd.colourCode = icm.code"
                    + " LEFT JOIN itemSizeMast ism ON prd.sizeCode = ism.code"
                    + " LEFT JOIN itemMakeMast imm ON prd.itemMakeCode = imm.code "
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code"
                    + " LEFT JOIN unitMast um ON prd.unitCode = um.code"
                    + " LEFT JOIN subGroup sgd ON prd.departmentCode = sgd.subCode"
                    + " LEFT JOIN gstType it ON it.code = prd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = prd.hsnCode \n"
                    + " WHERE prd.docId = :docId"
                    + " AND ISNULL(prd.isFooter, 0) = 0"
                    + " ORDER BY prd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                node.setServiceRecord(false);

                //Department
                node.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                node.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                node.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPbNetAmount(DmShared.xNull(rsTemp.getString("pbNetAmount")));
                mrTemp.setBillNo(node.getPbRecId());
                mrTemp.setBillNoTag(node.getPbDocId());
                mrTemp.setBillDate(node.getPbV_Date());

                //Challan Detail
                node.setPcDocId(DmShared.xNull(rsTemp.getString("pcDocId")));
                node.setPcRecId(DmShared.xNull(rsTemp.getString("pcRecId")));
                node.setPcV_SNo(DmShared.xNull(rsTemp.getString("pcV_SNo")));

                //Order Details
                node.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                node.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")));
                node.setPoV_SNo(DmShared.xNull(rsTemp.getString("poV_SNo")));
                node.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));

                //SKU
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("skuDecimalPlaces")));

                //Purchase Unit
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDecimalPlaces(DmServer.vNull(rsTemp.getString("purcahseUnitDecimalPlaces")));
                node.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                double skuQty = DmShared.vNull(rsTemp.getString("skuQty"));
                double acceptQty = DmShared.vNull(rsTemp.getString("acceptQty"));
                double purchaseUnitConversionRate = (skuQty / acceptQty);
                String puConversionRate = DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue()));
                node.setPurchaseUnitConversionRate(DmServer.vNull(puConversionRate));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Quantities
                node.setQuantity(DmServer.vNull(rsTemp.getString("acceptQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));

                //Rate
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

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
                node.setItemMastPurchaseReturnAc(DmShared.xNull(rsTemp.getString("itemMastPurchaseReturnAc")));

                //Accounts
                node.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                node.setSgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST_RCM")));

                node.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));
                node.setUgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST_RCM")));

                node.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                node.setCgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST_RCM")));

                node.setIgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST")));
                node.setIgstRcmAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST_RCM")));

                //Properties
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

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

                //Other
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                /*==============================<< Get OverheadLine detail >>===============================*/
                // node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp)); <TS>
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListItemDetail().addAll(lstNode);

            /*==============================<< Get Service detail >>===============================*/
            sqlQuery = "SELECT\n"
                    + "  prd.v_SNo\n"
                    + " , prd.itemCode\n"
                    + " , prd.hsnCode\n"
                    + " , hsn.hsn\n"
                    + " , prd.itemGSTNature\n"
                    + " , prd.isStatus\n"
                    + " , im.name AS itemName\n"
                    + " , im.smfCode\n"
                    + " , im.itsBasedOn\n"
                    + " , prd.unitCode \n"
                    + " , prd.acceptQty\n"
                    + " , prd.docQty\n"
                    + " , prd.rate \n"
                    + " , prd.isManualTax\n"
                    + " , prd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , prd.gstTypeCode\n"
                    + " , prd.oh_Amt_Gross\n"
                    + " , prd.oh_Amt_Taxable\n"
                    + " , prd.oh_at_SGST\n"
                    + " , prd.oh_Amt_SGST\n"
                    + " , prd.oh_at_UGST\n"
                    + " , prd.oh_Amt_UGST\n"
                    + " , prd.oh_at_CGST\n"
                    + " , prd.oh_Amt_CGST\n"
                    + " , prd.oh_at_IGST\n"
                    + " , prd.oh_Amt_IGST\n"
                    + " , prd.oh_Amt_SubTotal\n"
                    + " , prd.oh_Amt_Net \n"
                    + " , prd.oh_Ac_SGST \n"
                    + " , prd.oh_Ac_UGST \n"
                    + " , prd.oh_Ac_CGST \n"
                    + " , prd.oh_Ac_IGST \n"
                    + " , prd.oh_Ac_SGST_RCM \n"
                    + " , prd.oh_Ac_UGST_RCM \n"
                    + " , prd.oh_Ac_CGST_RCM \n"
                    + " , prd.oh_Ac_IGST_RCM \n"
                    + " , um.gstUnitCode\n"
                    + " , prd.oh_Ac_Item\n"
                    + " , prd.serviceEvalTypeCode\n"
                    + " , prd.serviceEvalRate\n"
                    + " FROM pBillDetail prd\n"
                    + " LEFT JOIN itemMast im ON prd.itemCode = im.code\n"
                    + " LEFT JOIN gstType it ON it.code = prd.gstTypeCode \n"
                    + " LEFT JOIN unitMast um ON prd.unitCode = um.code\n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = prd.hsnCode \n"
                    + " WHERE prd.docId = :docId\n"
                    + " AND ISNULL(prd.isFooter, 0) = 2\n"
                    + " ORDER BY prd.v_SNo\n";
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

            /*==============================<< Get Overhead detail >>===============================*/
            //mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp)); <TS>

            /*==============================<< Save GUI >>====================================*/
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

            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(keyCode, cnTemp);

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason & Walkthrough >>============================*/
            OverHeadLine_Server.delete(keyCode, cnTemp);
            OverHead_Server.delete(keyCode, cnTemp);
            ModifyReason_Server.delete(keyCode, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);
            Gst_Server.deleteGstTrans(keyCode, cnTemp);

            /*==============================<< Update record in GRN >>===============================*/
            manageReturnedQty(DmShared.iNull(keyCode).toString(), true, cnTemp);

            /*==============================<< Delete Stock >>====================================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Delete record from pBillDetail >>===============================*/
            String ibCodeCsv = STServer.getCsvFieldValue("pBillDetail", "ibCode", " AND docid = '" + DmShared.iNull(keyCode).toString() + "'", cnTemp);

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

            sqlQuery = " SELECT pr.docId"
                    + " FROM pBill pr"
                    + " LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')"
                    + " AND pr.site_Code = :site_Code"
                    + " AND pr.v_Prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY pr.v_Date DESC, vt.short_Name, pr.v_No DESC, pr.recId";
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

            sqlQuery = "SELECT"
                    + "   pr.docId"
                    + " , pr.recId"
                    + " , pr.v_Date"
                    + " , pr.v_No"
                    + " , sg.name AS supplierName\n"
                    + " , vt.short_Name"
                    + " , pr.approvedBy"
                    + " , format(pr.approvalDateTime,'dd-MMM-yyyy HH.mm') AS approvalDateTime\n"
                    + " , pr.partyBillNo"
                    + " , pr.partyBillDate "
                    + " , gio.recId as gateNo,sgd.name AS departmentName\n"
                    + " 	FROM pBill pr\n"
                    + " 	LEFT JOIN voucher_Type vt ON pr.v_Type = vt.v_Type\n"
                    + " 	LEFT JOIN subGroup sg ON pr.subCode = sg.subCode\n"
                    + " 	LEFT JOIN gateInOut gio ON gio.docId = pr.gateDocId\n"
                    + " LEFT JOIN subGroup sgd ON pr.departmentCode = sgd.subCode\n"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')\n"
                    + " AND pr.site_Code = :site_Code\n"
                    + " AND pr.v_Prefix = :vPrefix";
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
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                listNode.setGateNo(DmShared.xNull(rsTemp.getString("gateNo")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
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

    private CompareProperties_Mdl iniItemBatchProperties(PurchaseReturn_Mdl mrTempCurrent, PurchaseReturn_Mdl mrTempPrevious, boolean isDelete) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();
        if (!isDelete) {
            lstItemBatch_MdlCurnt.clear();
            for (GrdItemDetail_Mdl detail_MdlCurrent : mrTempCurrent.getListItemDetail()) {
                ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0) {
                    if (!detail_MdlCurrent.isServiceRecord()) {
                        batch_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        batch_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
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
                        batch_Mdl.setReceiveQty(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getSkuQty())));
                        batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                        batch_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                        batch_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                        batch_Mdl.setvType(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
                        batch_Mdl.setvSno(DmShared.xNull(detail_MdlCurrent.getV_SNo()));

                        batch_Mdl.setStockSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getAmount()), "0.00"));
                        batch_Mdl.setStockLandedSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getLandedSkuValue())));
                        batch_Mdl.setStockV_Sno(detail_MdlCurrent.getV_SNo());
                        batch_Mdl.setStockRefChallanNo(DmShared.xNull(mrTempCurrent.getPartyBillNo()));
                        batch_Mdl.setStockRefChallanDate(DmShared.dNull(mrTempCurrent.getPartyBillDate()));
                        batch_Mdl.setStockDepartmentCode(DmShared.xNull(detail_MdlCurrent.getDepartmentCode()));

                        lstItemBatch_MdlCurnt.add(batch_Mdl);
                    }
                }
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdItemDetail_Mdl detail_MdlPrev : mrTempPrevious.getListItemDetail()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
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
                        batch_MdlPrev.setReceiveQty(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getSkuQty())));
                        batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
                        batch_MdlPrev.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
                        batch_MdlPrev.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
                        batch_MdlPrev.setvType(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
                        batch_MdlPrev.setvSno(DmShared.xNull(detail_MdlPrev.getV_SNo()));

                        batch_MdlPrev.setStockSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getAmount()), "0.00"));
                        batch_MdlPrev.setStockLandedSkuValue(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getLandedSkuValue())));
                        batch_MdlPrev.setStockV_Sno(detail_MdlPrev.getV_SNo());
                        batch_MdlPrev.setStockRefChallanNo(DmShared.xNull(mrTempPrevious.getPartyBillNo()));
                        batch_MdlPrev.setStockRefChallanDate(DmShared.dNull(mrTempPrevious.getPartyBillDate()));
                        batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(detail_MdlPrev.getDepartmentCode()));

                        lstItemBatch_MdlPrev.add(batch_MdlPrev);
                    }
                }
            }
        }

        if (mrTempCurrent != null) {
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdPurchaseReturn().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierCode()));

        } else if (mrTempPrevious != null) { //Delete Case
            compareProperties_Mdl.setDocId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldCode()));
            compareProperties_Mdl.setRecId(DmShared.xNull(mrTempPrevious.getSharedKeyFieldName()));
            compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempPrevious.getRecIdPurchaseReturn().getPrefix()));
            compareProperties_Mdl.setV_No(DmShared.xNull(mrTempPrevious.getRecIdPurchaseReturn().getCounter().toString()));
            compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempPrevious.getVoucherTypeCode()));
//            compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempPrevious.getDepartmentCode()));
            compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempPrevious.getSupplierCode()));

        }
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
        }
        return compareProperties_Mdl;
    }

    private void manageReturnedQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, pbDocId = "";
        double prAcceptQty, pbReturnedQty, pbAcceptQty;
        Integer isStatus;

        try {
            sqlQuery = "SELECT"
                    + "   pb.docId AS pbDocId"
                    + " , prd.pbV_SNo"
                    + " , prd.acceptQty AS prAcceptQty"
                    + " , pbd.acceptQty AS pbAcceptQty"
                    + " , pbd.returnedQty AS pbReturnedQty"
                    + " , pbd.isStatus"
                    + " , pb.approvedBy"
                    + " , pb.recId AS pbNo"
                    + " , vt.short_name AS pbType"
                    + " FROM pBillDetail pbd"
                    + " LEFT JOIN pBill pb ON pbd.docId = pb.docId"
                    + " LEFT JOIN voucher_type vt ON pb.v_Type = vt.v_type"
                    + " INNER JOIN ("
                    + "     SELECT prd.pbDocId, prd.pbV_SNo, sum(prd.acceptQty) AS acceptQty"
                    + "     FROM pBillDetail prd"
                    + "     WHERE prd.docId = :docId"
                    + "     GROUP BY prd.pbDocId, prd.pbV_SNo"
                    + " ) prd ON pbd.docId = prd.pbDocId AND pbd.v_SNo = prd.pbV_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill is Cancelled");
                }
                prAcceptQty = rsTemp.getDouble("prAcceptQty");
                pbAcceptQty = rsTemp.getDouble("pbAcceptQty");
                pbReturnedQty = rsTemp.getDouble("pbReturnedQty");

                if (isDelete) {
                    pbReturnedQty -= prAcceptQty;
                } else {
                    pbReturnedQty += prAcceptQty;
                }

                if (pbReturnedQty > pbAcceptQty) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill overflows by " + DmServer.vNull(pbReturnedQty - pbAcceptQty));
                } else if (pbReturnedQty < 0) {
                    throw new DmException(800, DmShared.xNull(rsTemp.getString("pbDocId")), DmShared.xNull(rsTemp.getString("pbV_SNo")), "Bill overflows by " + DmServer.vNull(pbReturnedQty));
                } else if (pbReturnedQty == pbAcceptQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (!pbDocId.equalsIgnoreCase("")) {
                    pbDocId += ",";
                }
                pbDocId += DmShared.iNull(rsTemp.getString("pbDocId")).toString();

                sqlQuery = "UPDATE pBillDetail SET"
                        + " isStatus = :isStatus,"
                        + " returnedQty = :returnedQty"
                        + " WHERE docId = :pbDocId AND v_SNo = :pbV_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("pbDocId", DmShared.xNull(rsTemp.getString("pbDocId")));
                psTemp1.setString("pbV_SNo", DmShared.xNull(rsTemp.getString("pbV_SNo")));
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("returnedQty", DmServer.vNull(pbReturnedQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!pbDocId.equalsIgnoreCase("")) {
                sqlQuery = "UPDATE pBill SET u_EntDt_LatestLine = :uEntDt where docId in (" + pbDocId + ")";
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

    private void setAdjustments(PurchaseReturn_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        AdjustmentSave_Mdl adjSaveMdl = null;
        GrdAdjustment_Mdl gaMdl = null;
        double amtDr = 0;

        try {

            sqlQuery = "SELECT TOP 1 lv.docId, lv.v_SNo, lv.v_Date, lv.subCode, lv.amtDr"
                    + " FROM ledger lv"
                    + " WHERE lv.docId = :docId"
                    + " AND lv.subCode = :subCode"
                    + " AND ISNULL(lv.amtDr, 0) > 0"
                    + " ORDER BY lv.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                adjSaveMdl = new AdjustmentSave_Mdl();
                adjSaveMdl.setVr_DocId(DmShared.xNull(rsTemp.getString("docId")));
                adjSaveMdl.setVr_V_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                adjSaveMdl.setVr_V_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                adjSaveMdl.setVr_SubCode(DmShared.xNull(rsTemp.getString("subCode")));
                amtDr = DmShared.vNull(rsTemp.getString("amtDr"));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT TOP 1 lb.docId, lb.v_SNo, lb.v_Date, lb.subCode"
                    + " FROM ledger lb"
                    + " WHERE lb.docId = :docId"
                    + " AND lb.subCode = :subCode"
                    + " AND ISNULL(lb.amtCr, 0) > 0"
                    + " ORDER BY lb.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getBillNoTag()));
            psTemp.setString("subCode", DmShared.xNull(mrTemp.getSupplierCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                gaMdl = new GrdAdjustment_Mdl();
                gaMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                gaMdl.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                gaMdl.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                gaMdl.setAcCode(DmShared.xNull(rsTemp.getString("subCode")));
                gaMdl.setAdjustment(DmServer.vNull(amtDr, "0.00"));
                gaMdl.setAdjustmentDrCr("Dr");
            }
            rsTemp.close();
            psTemp.close();

            if (adjSaveMdl != null && gaMdl != null) {
                adjSaveMdl.getLstAdjustments().add(gaMdl);
                AccountAdjustment_Server.saveRecord(adjSaveMdl, cnTemp);
            } else {
                throw new DmException(0000, "Unable to save adjustments.");
            }

        } catch (Exception ex) {
            throw new DmException(0000, ex.getMessage());
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
    public DmResultPageing<List<TxtHGateEntry_Mdl>> getTxtHGateEntryNo(String pBillDocId, String supplierCode, String returnDate, String refDate, DmHelper helper, boolean blnToEndOfFile) {
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

            if (DmShared.dNull(returnDate).trim().equalsIgnoreCase("") || DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                condition += " AND 1=2";
            }
            if (!DmShared.dNull(returnDate).trim().equalsIgnoreCase("")) {
                condition += " AND gio.v_Date <= :returnDate ";
            }
            if (!DmShared.dNull(refDate).trim().equalsIgnoreCase("")) {
                condition += " AND gio.v_Date >= :refDate ";
            }
            sqlWithParam.setSQL(" Select gio.docId, gio.recId, gio.v_Date as gateEntryDate, gio.partyCode, "
                    + " gio.transporterCode, "
                    + " sg1.name as transporterName,gio.vehicleNo, gio.v_No, vt.short_Name "
                    + " from gateInOut gio "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = gio.v_Type "
                    + " LEFT JOIN weighment wgt ON wgt.gateDocId = gio.docId "
                    + " LEFT JOIN subGroup sg1 ON sg1.subCode = gio.transporterCode "
                    + " LEFT JOIN ("
                    + "			SELECT pb.gateDocId "
                    + "			FROM pBill pb "
                    + "			LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type "
                    + "			WHERE pb.gateDocId IS NOT NULL "
                    + "			AND  vt.category IN (' " + pvc.PURCHASE_RETURN + " ', ' " + pvc.PURCHASE_RETURN_UNREGISTERED + " ')"
                    + "			AND pb.docId <> :pBillDocId "
                    + "		) vG ON vG.gateDocId = gio.docId "
                    + " WHERE ISNULL(wgt.outWeighBy,'') = '' "
                    + " AND gio.partyCode = :partyCode "
                    + " AND vG.gateDocId IS NULL"
                    + " AND gio.loadUnload = :loadUnload  " + condition);
            sqlWithParam.setString("partyCode", DmShared.iNull(supplierCode).toString());
            sqlWithParam.setString("pBillDocId", DmShared.iNull(pBillDocId).toString());
            sqlWithParam.setString("loadUnload", DmShared.iNull(STEnum.GatePurpose.LOADING.getId()).toString());
            if (!DmShared.dNull(returnDate).trim().equalsIgnoreCase("")) {
                sqlWithParam.setDate("returnDate", DmServer.convertDate(returnDate));
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

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, boolean isInventoryAccountingActive, boolean includeOverHeadsInCost, boolean isSystemGenerated, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", serviceSql = "", roundOffSql = "", baseSql = "", partySql = "", taxSql = "", finalDiscountSql = "", purchaseAcSql = "";
        String sgstContraAc = "", ugstContraAc = "", cgstContraAc = "", igstContraAc = "";
        boolean flag = false;
        String rcmApplicable = "", topInventoryAc = "";
        int sr = 0;

        try {
            //<editor-fold defaultstate="collapsed" desc="Prepare Account Posting SQL">
            //Note: Dr: Positive Value, Cr: Negative Value
            sqlQuery = " SELECT \n"
                    + "		TOP 1 pb.rcmApplicable\n"
                    + "		," + (isSystemGenerated ? "pbd.oh_Ac_Item AS subCode\n" : " pb.balancingAc as subCode \n")
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
                rcmApplicable = DmShared.iNull(rsTemp.getString("rcmApplicable")).toString();
                topInventoryAc = DmShared.xNull(rsTemp.getString("subCode"));
            }
            rsTemp.close();
            psTemp.close();

            ++sr;
            partySql = "-- Net Amount Posting\n"
                    + "SELECT \n"
                    + (isSystemGenerated ? "	pb.balancingAc as subCode \n" : "	pb.subCode as subCode \n")
                    + "	, :topInventoryAc AS contraSub\n"
                    + "	, ISNULL(pb.oh_Amt_Net, 0) AS amount\n"
                    + "	, " + sr + " AS sr\n"
                    + "FROM pBill pb\n"
                    + "WHERE pb.docId = :docId\n"
                    + "AND pb.subCode IS NOT NULL\n"
                    + "AND ISNULL(pb.oh_Amt_Net, 0) <> 0\n";

            if (isInventoryAccountingActive) {
                ++sr;
                inventorySql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                        + "FROM (\n"
                        + "	-- Inventory Posting\n"
                        + "	SELECT \n"
                        + (isSystemGenerated ? "		pbd.oh_Ac_Item AS subCode\n" : "		pb.balancingAc AS subCode\n")
                        + (isSystemGenerated ? "		, pb.balancingAc AS contraSub\n" : "		, pb.subCode AS contraSub\n")
                        + "		, -IsNull(pbd.landedSkuValue, 0) AS amount\n"
                        + "	FROM pBill pb\n"
                        + "	LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                        + "	WHERE pb.docId = :docId\n"
                        + "	AND pbd.isFooter = 0\n"
                        + "	AND pbd.oh_Ac_Item IS NOT NULL\n"
                        + "	AND ISNULL(pbd.landedSkuValue, 0) <> 0\n"
                        + "\n"
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
                            + "		, -ISNULL(pbd.oh_Amt_Net, 0) AS amount\n"
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
                        + "		, -IsNull(pbd.oh_Amt_Taxable, 0) AS amount\n"
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
                        + "		, -ISNULL(pbd.oh_Amt_Taxable, 0) AS amount\n"
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
                        + "	, ISNULL(pb.oh_Amt_FinalDiscount, 0) AS amount\n"
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
                        + "	, -ISNULL(pb.oh_Amt_RoundOff, 0) AS amount\n"
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
                    + (isSystemGenerated ? "" : "UNION ALL\n")
                    + "\n"
                    + (isSystemGenerated ? "" : taxSql)
                    + "\n";
            if (isInventoryAccountingActive) {
                baseSql += "UNION ALL\n"
                        + "\n"
                        + inventorySql
                        + "\n"
                        + (!includeOverHeadsInCost ? " UNION ALL\n " + serviceSql + "\n" : "");

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
                    + "		, -ISNULL(" + amountField + ", 0) AS amount\n"
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
                    + "		, ISNULL(" + amountField + ", 0) AS amount\n"
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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        ResultSet rsTemp = null;
        Connection cnTemp = null;
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
                    + " , vt.balancingAc "
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "')"
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
}
