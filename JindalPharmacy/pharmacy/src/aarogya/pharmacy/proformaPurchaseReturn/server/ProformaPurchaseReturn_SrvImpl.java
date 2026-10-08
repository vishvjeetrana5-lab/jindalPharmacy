package aarogya.pharmacy.proformaPurchaseReturn.server;

import aarogya.pharmacy.proformaPurchaseReturn.shared.DefaultValue_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.Find_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.GrdItemDetail_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.ProformaPurchaseReturn_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.ProformaPurchaseReturn_Srv;
import aarogya.pharmacy.proformaPurchaseReturn.shared.ProformaPurchaseReturn_Vld;
import aarogya.pharmacy.proformaPurchaseReturn.shared.TxtHBatchNo_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.GrdBillDetail_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.TxtHItemName_Mdl;
import aarogya.pharmacy.proformaPurchaseReturn.shared.TxtHSupplier_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.ITSType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.server.Gst_Server;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProformaPurchaseReturn_SrvImpl extends DmRSSPrivate implements ProformaPurchaseReturn_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "ppr.v_Type", voucherDateFieldName = "ppr.v_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<List<KeyFieldExtended>>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
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
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "','" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED + "')"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("category")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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
        String sqlQuery, filtration = "", condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            if (flagFilterByVoucherCategory) {
//                filtration = " SELECT acf.Nature "
//                        + " FROM AcFilteration acf "
//                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
//                        + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')";
//            } else {
//                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
//            }
            filtration += " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.PHARMACY_PROFORMA_PURCHASE_RETURN)) {
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
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlWithParam.setSQL("SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition);
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
    public DmResultPageing<List<GrdBillDetail_Mdl>> getTxtHBillNo(String subCode, String voucherCategory, String v_Date, String billDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdBillDetail_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdBillDetail_Mdl> lstTemp = new ArrayList<>();
        GrdBillDetail_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subCode).trim().equals("")) {
                condition += " AND pb.subcode = :subcode \n";
            } else {
                condition += " AND 1 = 2 \n";
            }

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND pb.v_Date <= :v_Date \n";
            } else {
                condition += " AND 1 = 2 \n";
            }

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PROFORMA_PURCHASE_RETURN)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "') \n";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "') \n";
            } else if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED)) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "') \n";
            } else {
                condition += " AND 1 = 2  \n";
            }

            sqlQuery = "SELECT  \n"
                    + "   pbd.docId AS code \n"
                    + " , pbd.v_SNo AS pbd_Vsno\n"
                    + " , pb.recId AS name \n"
                    + " , pb.v_Date AS pbV_Date \n"
                    + " , pb.partyBillNo AS pbRefNo \n"
                    + " , pb.partyBillDate AS pbRefDate \n"
                    + " , im.name AS medicineName\n"
                    + " , pbd.rate \n"
                    + " , pbd.batchNo \n"
                    + " , pbd.mrp \n"
                    + " , pbd.saleRate \n"
                    + " , pbd.manufacturingDate \n"
                    + " , pbd.expiryDate , imm.companyName as medicineCompany "
                    + " FROM pBillDetail pbd\n"
                    + " LEFT JOIN pBill pb ON pb.docId = pbd.docId \n"
                    + " LEFT JOIN itemMast im on im.code = pbd.itemCode "
                    + " LEFT JOIN itemMakeMast imm ON im.itemmakeCode=imm.code  "
                    + " INNER JOIN voucher_Type vt ON pb.v_Type = vt.v_Type  \n"
                    + " INNER JOIN ( \n"
                    + "     SELECT pbd.docId \n"
                    + "     FROM pBillDetail pbd \n"
                    + "     WHERE ISNULL(pbd.isFooter, 0) = 0 \n"
                    + "     AND ISNULL(pbd.isReturn, 0) = 0 \n"
                    + "     AND (CASE  \n"
                    + "             WHEN pbd.isStatus = 0 THEN 1 \n"
                    + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0)) > 0 THEN 1 \n"
                    + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0)) > 0 THEN 1 \n"
                    + "             WHEN pbd.isStatus = 2 AND pbd.docId = :billDocId THEN 1 \n"
                    + "             ELSE 0 END) = 1 \n"
                    + "     GROUP BY pbd.docId \n"
                    + " ) AS v ON pb.docId = v.docId \n"
                    + " WHERE ISNULL(pb.approvedBy, '') <> '' "
                    + " AND ISNULL(pbd.isFooter,0) = 0 \n"
                    + " AND pb.site_Code = :site_Code \n" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(subCode).trim().equals("")) {
                sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            }
            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdBillDetail_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setBillV_SNo(DmShared.xNull(rsTemp.getString("pbd_Vsno")));
                listNode.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                listNode.setPbRefNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                listNode.setPbRefDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                listNode.setMedicineCompany(DmShared.xNull(rsTemp.getString("medicineCompany")));
                listNode.setRate(DmShared.xNull(rsTemp.getString("rate")));
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setSaleRate(DmShared.xNull(rsTemp.getString("saleRate")));
                listNode.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT im.code , im.name, im.manualCode,imm.companyName AS companyName , im.hsnCode, im.itemGSTNature, im.itsBasedOn \n"
                    + " , im.isBestBefore, im.propertyList, im.sku AS skuCode, ums.unit AS skuName \n"
                    + " , ISNULL(ums.decimalPlaces,0) AS skuDecimalPlaces, im.purchaseUnit AS purchaseUnitCode \n"
                    + " , um.unit AS purchaseUnitName, ISNULL(im.purchaseUnitConversionRate, 0) AS purchaseUnitConversionRate \n"
                    + " , ISNULL(um.decimalPlaces,0) AS purchaseUnitDecimalPlaces,  im.isIncludeFreeQtyInSaleRate \n"
                    + " , um.gstUnitCode, im.subCode as inventoryAc,sgi.name as inventoryAcName \n"
                    + " FROM itemMast im \n"
                    + " LEFT JOIN itemMakeMast imm ON im.itemmakeCode=imm.code \n"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code \n"
                    + " LEFT JOIN unitmast um ON im.purchaseUnit = um.code \n"
                    + " LEFT JOIN unitmast ums ON im.sku = ums.code \n"
                    + "	LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon = 1) \n"
                    + " AND im.isActive = 1 \n"
                    + " AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCompanyName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                listNode.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                listNode.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

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
                listNode.setIsIncludeFreeQtyInSaleRate(DmShared.iNull(rsTemp.getString("isIncludeFreeQtyInSaleRate")).toString());

                //set Inventory Accounts
                listNode.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                listNode.setInventoryAcName(DmShared.xNull(rsTemp.getString("inventoryAcName")));

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
    public DmResultPageing<List<TxtHBatchNo_Mdl>> getTxtHBatchNo(String medicineCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHBatchNo_Mdl> lstTemp = new ArrayList<>();
        TxtHBatchNo_Mdl listNode;
        String sqlQuery;
        int i = 1;
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
                    + " FROM itemBatch ib"
                    + " WHERE ib.itemCode = :itemCode"
                    + " AND ISNULL(ib.batchNo, '') <> ''";
            sqlWithParam.setSQL(sqlQuery);
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grids">
    @Override
    public DmResult<ProformaPurchaseReturn_Mdl> getItemDetail(String v_Type, List<KeyFieldExtended> svms, String location, String v_Date, String docId) {
        DmResult<ProformaPurchaseReturn_Mdl> dmrTemp = new DmResult<>();
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node = new GrdItemDetail_Mdl();
        ProformaPurchaseReturn_Mdl mrTemp = new ProformaPurchaseReturn_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", pendingQty = "", join = "", pendingFreeQty = "";
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                pendingQty = " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) + ISNULL(prd.acceptQty, 0) AS pendingQty \n";
                pendingFreeQty = " , ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) + ISNULL(prd.freeQty,0) AS pendingFreeQty \n";
                condition += " AND (CASE"
                        + "             WHEN pbd.isStatus = 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0) + ISNULL(prd.acceptQty,0)) > 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) + ISNULL(prd.freeQty,0)) > 0 THEN 1 \n"
                        + "             ELSE 0 END) = 1";

                join = " LEFT JOIN ( \n"
                        + "     SELECT prd.pbDocId,prd.pbV_SNo,prd.acceptQty,prd.freeQty \n"
                        + "     FROM pBillDetail prd \n"
                        + "     WHERE prd.docid = :docId \n"
                        + " ) AS prd ON pbd.docId = prd.pbDocId AND pbd.v_SNO = prd.pbV_SNo \n";
            } else {
                pendingQty = " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) AS pendingQty \n";
                pendingFreeQty = " , ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) AS pendingFreeQty \n";
                condition += " AND (CASE"
                        + "             WHEN pbd.isStatus = 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.acceptQty,0) - ISNULL(pbd.returnedQty,0)) > 0 THEN 1 \n"
                        + "             WHEN pbd.isStatus = 2 AND (ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0)) > 0 THEN 1 \n"
                        + "             ELSE 0 END) = 1";
            }

            condition += " AND ISNULL(pbd.isReturn, 0) = 0";

            if (svms != null) {
                String tempCondition = " ";
                for (KeyFieldExtended field : svms) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0
                            && DmShared.iNull(field.getSharedKeyFieldName()).longValue() != 0) {
                        if (!tempCondition.trim().equalsIgnoreCase("")) {
                            tempCondition += " OR ";
                        }
                        tempCondition += " ( pb.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "'";
                        tempCondition += " AND pbd.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "')";
                    }
                }
                if (!DmShared.xNull(tempCondition).trim().equalsIgnoreCase("")) {
                    condition += " AND (" + tempCondition + ") \n";
                }
            }

            sqlQuery = "SELECT"
                    + "   im.code AS itemCode\n"
                    + " , im.name AS itemName \n"
                    + " , imm.companyName AS companyName \n"
                    + " , pbd.hsnCode\n"
                    + " , im.itsBasedOn\n"
                    + " , im.isBestBefore \n"
                    + " , im.propertyList\n"
                    + " , im.itemGSTNature\n"
                    + " , pb.docId AS pbDocId\n"
                    + " , pb.recId AS pbRecId\n"
                    + " , pb.v_Type AS pbV_Type\n"
                    + " , pb.v_Date AS pbV_Date \n"
                    + " , pb.partyBillNo AS pbRefNo \n"
                    + " , pb.partyBillDate AS pbRefDate \n"
                    + " , pb.oh_Amt_Net AS pbNetAmount\n"
                    + " , pbd.v_SNo AS pbV_SNo\n"
                    + " , pc.docId AS pcDocId\n"
                    + " , pc.recId AS pcRecId\n"
                    + " , pcd.v_SNo AS pcV_SNo\n"
                    + " , po.docId AS poDocId \n"
                    + " , po.recId AS poRecId\n"
                    + " , pod.v_SNo AS poV_SNo\n"
                    + " , po.refNo AS poRefNo\n"
                    + " , ums.code AS skuCode\n"
                    + " , ums.unit AS skuName \n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces\n"
                    + " , um.code AS purchaseUnitCode\n"
                    + " , um.unit AS purchaseUnitName\n"
                    + " , um.decimalPlaces AS purcahseUnitDecimalPlaces \n"
                    + " , im.purchaseUnitConversionRate\n"
                    + " , um.gstUnitCode \n" + pendingQty + pendingFreeQty
                    + " , pbd.rate"
                    + " , pbd.mrp"
                    + " , pbd.skuMrp"
                    + " , pbd.saleRate"
                    + " , pbd.skuSaleRate"
                    + " , pbd.skuRate \n"
                    + " , isNull(pbd.oh_Amt_Gross,0) / (isNull(pbd.acceptQty,0) + isNull(pbd.freeQty,0))  as purchaseRate \n"
                    + " , pbd.batchNo"
                    + " , pbd.expiryDate"
                    + " , pbd.manufacturingDate"
                    + " , pbd.remark"
                    + " , pbd.ibCode \n"
                    + " , im.subCode as inventoryAc,sgi.name as inventoryAcName \n"
                    + " FROM pBill pb \n"
                    + " LEFT JOIN pBillDetail pbd ON pb.docId = pbd.docId \n" + join
                    + " LEFT JOIN pChallanDetail pcd ON pbd.pcDocId = pcd.docId AND pbd.pcV_SNo = pcd.v_SNo \n"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId \n"
                    + " LEFT JOIN pOrderDetail pod ON pbd.poDocId = pod.docId AND pbd.poV_SNo = pod.v_SNo \n"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId \n"
                    + " LEFT JOIN itemMast im ON pbd.itemCode = im.code \n"
                    + " LEFT JOIN itemMakeMast imm ON im.itemmakeCode=imm.code \n"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code \n"
                    + " LEFT JOIN unitMast um ON pbd.unitCode = um.code \n"
                    + "	LEFT JOIN subGroup sgi ON im.subCode = sgi.subCode \n"
                    + " WHERE  \n"
                    //  + " pb.docId = :billDocId \n"
                    + "  ISNULL(pbd.isFooter, 0) = 0 \n" + condition
                    + " ORDER BY pbd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            //    psTemp.setString("billDocId", DmShared.xNull(billDocId));
            if (!DmShared.xNull(docId).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(docId));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                node.setItemCompany(DmShared.xNull(rsTemp.getString("companyName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPbRefNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                node.setPbRefDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
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
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                double skuQty = (DmShared.vNull(rsTemp.getString("pendingQty")) + DmShared.vNull(rsTemp.getString("pendingFreeQty"))) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
//                double skuQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                String skuDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                node.setSkuQty(DmServer.vNull(DmServer.vNull(skuQty, skuDecimalFormat)));

                //Rates
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));

//======         =========================< As Suggested by Dhawan Sir >========================================
//                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("purchaseRate"), "0.00"));
//                double amount = DmShared.vNull(node.getQuantity()) * DmShared.vNull(node.getRate());
                double amount = (DmShared.vNull(node.getQuantity()) + DmShared.vNull(node.getFreeQty())) * DmShared.vNull(node.getRate());
                node.setAmount(DmServer.vNull(amount, "0.00"));
//                node.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
//                node.setLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
//                node.setpBillLandedSkuRate(DmServer.vNull(rsTemp.getString("landedSkuRate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));

                //set Inventory Accounts
                node.setInventoryAc(DmShared.xNull(rsTemp.getString("inventoryAc")));
                node.setInventoryAcName(DmShared.xNull(rsTemp.getString("inventoryAcName")));

                //Other
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult<>();
                dmrIts = getITS(node.getSharedKeyFieldCode(), node.getRate(), location, node.getItsBasedOn(), v_Date);
                node.setLstITS(dmrIts.getData());
                node.setGrossAcCode(dmrIts.getValue1());
                node.setGrossAcName(dmrIts.getValue2());

                //OHL PurchaseBill
                node.setOHLListPurchaseBill(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.xNull(node.getPbDocId()), DmShared.xNull(node.getPbV_SNo()), cnTemp));

                //Add Node
                lstNode.add(node);
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
            sqlQuery = "SELECT approvedBy,  approvalDateTime,"
                    + " (case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + " FROM proformaPurchaseReturn where docId = :docId ";
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

                        sqlQuery = " Update proformaPurchaseReturn set"
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

                            sqlQuery = " Update proformaPurchaseReturn set"
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

    private WalkThroughDefault_Mdl getWalkThroughFor(ProformaPurchaseReturn_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId, String voucherType) {
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

            STServer.sendMail(docId, voucherType, this);

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
            sqlQuery = "SELECT TOP 1 \n"
                    + "   vt.v_Type AS code \n"
                    + " , vt.description AS name \n"
                    + " , vt.category \n"
                    + " , vt.guiTemplateCode \n"
                    + " FROM voucher_Type vt \n"
                    + " WHERE vt.category = :category  \n"
                    + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")) {
                //  psTemp.setString("category", pvc.PHARMACY_PURCHASE_RETURN);
                psTemp.setString("category", pvc.PHARMACY_PROFORMA_PURCHASE_RETURN);
            } else {
                psTemp.setString("category", pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED);

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
            sqlQuery = "SELECT e.isIncludeTaxInCost,e.SinglePOFlowInPurchase \n"
                    + "FROM enviro e\n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isIncludeTaxInCost") == null) {
                    throw new DmException(0, "Include Tax in Cost is not defined in Environment Setting.");
                } else if (rsTemp.getString("singlePOFlowInPurchase") == null) {
                    throw new DmException(0, "Single PO Flow In Purchase is not defined in Environment Setting.");
                }
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
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
    public DmResult<String> saveRecord(ProformaPurchaseReturn_Mdl mrTempCurrent, ProformaPurchaseReturn_Mdl mrTempPrevious, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "",
                roundOffItemGstNature = "", roundOffRefDocId = "", roundOffRefV_Type = "", roundOffRefDocValue = "",
                invoiceCGST = "", invoiceSGST = "", invoiceIGST = "",
                invoiceUGST = "", invoiceValue = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            ProformaPurchaseReturn_Vld.chkSave(mrTempCurrent);

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);
            OverHead_Shared.chkGrid(mrTempCurrent.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTempCurrent.getListItemDetail());
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            DmServer.checkForwordVoucherDateTime(mrTempCurrent.getVoucherDate(), "");

//            if (mrTempCurrent.getDefaultValueMdl().isInventoryAccountingActive()) {
//                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTempCurrent.getVoucherDate(), "", cnTemp);
//            }
            if (!DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Check for Approvedby if already approved >>====================================*/
                sqlQuery = "select approvedBy from proformaPurchaseReturn where  docId = :docId ";
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
                sqlQuery = "SELECT u_EntDt_LatestLine FROM proformaPurchaseReturn WHERE docId = :docId";
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

            /*==============================<< Check items for proformaPurchaseReturn item change >>====================================*/
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

            if (DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTempCurrent.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTempCurrent.setRecIdProformaPurchaseReturn(DmServer.getRecId("proformaPurchaseReturn", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdProformaPurchaseReturn(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTempCurrent.setSharedKeyFieldName(DmShared.xNull(mrTempCurrent.getRecIdProformaPurchaseReturn().getRecIdValue()));
                mrTempCurrent.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTempCurrent), DmServer.getSsnUserName(this), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), mrTempCurrent.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("proformaPurchaseReturn", "docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), "recId", mrTempCurrent.getRecIdProformaPurchaseReturn(), mrTempCurrent.getVoucherDate(), DmShared.xNull(mrTempCurrent.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTempCurrent.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTempCurrent.getGuiConfigCode(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert / Update record into proformaPurchaseReturn >>====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO proformaPurchaseReturn ( \n"
                        + "      docId, recId, recIdPrefix, v_Type, v_No, v_Date, v_Prefix \n"
                        + "     ,gstReasonCode, subCode, consignee, partyType, rcmApplicable, placeOfSupply \n"
                        + "     ,remark, wtaCode, guiConfigCode, site_Code, preparedBy, u_EntDt, u_EntDt_LatestLine, transferDt) \n"
                        + " VALUES ( \n"
                        + "      :docId, :recId, :recIdPrefix, :v_Type, :v_No, :v_Date, :v_Prefix\n"
                        + "     ,:gstReasonCode, :subCode, :consignee, :partyType, :rcmApplicable, :placeOfSupply\n"
                        + "     ,:remark, :wtaCode, :guiConfigCode, :site_Code, :preparedBy, :uEntDt, :uEntDt, :transferDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(DmShared.xNull(mrTempCurrent.getRecIdProformaPurchaseReturn().getPrefix())));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTempCurrent.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTempCurrent.getRecIdProformaPurchaseReturn().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTempCurrent.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
//                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getPartyBillNo()));
//                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getPartyBillDate()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTempCurrent.getGstReasonCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("partyType", DmShared.xNull(mrTempCurrent.getPartyTypeForEntry()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTempCurrent.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTempCurrent.getPlaceOfSupply()));
                psTemp.setString("remark", DmShared.xNull(mrTempCurrent.getRemark()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTempCurrent.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTempCurrent.getGuiConfigCode()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("transferDt", null);
            } else {
                sqlQuery = "UPDATE proformaPurchaseReturn SET"
                        + "  v_Date = :v_Date"
                        //                        + " ,partyBillNo = :partyBillNo"
                        //                        + " ,partyBillDate = :partyBillDate"
                        + " ,gstReasonCode = :gstReasonCode"
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
//                psTemp.setString("partyBillNo", DmShared.xNull(mrTempCurrent.getPartyBillNo()));
//                psTemp.setDate("partyBillDate", DmServer.convertDate(mrTempCurrent.getPartyBillDate()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTempCurrent.getGstReasonCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTempCurrent.getSupplierCode()));
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

            sqlQuery = "Delete From proformaPurchaseReturnDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTempCurrent.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into proformaPurchaseReturnDetail >>====================================*/
            List<GrdItemDetail_Mdl> listDetail = mrTempCurrent.getListItemDetail();
            int vSNo = 0;
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                    detail_Mdl.setV_SNo(String.valueOf(++vSNo));
                    sqlQuery = "INSERT INTO proformaPurchaseReturnDetail ("
                            + "     docId, v_SNo, pbDocId, pbV_SNo,pcDocId,pcV_SNo,poDocId,poV_SNo, itemCode, hsnCode, itemGSTNature,ibCode, batchNo, expiryDate,"
                            + "     manufacturingDate, mrp, skuMrp, saleRate, skuSaleRate, unitCode,quantity, freeQty, skuQty, "
                            + "     skuQtyForRate, skuRate, rate, remark,pbRefNo,pbRefDate)"
                            + " Values ("
                            + "     :docId, :v_SNo, :pbDocId, :pbV_SNo,:pcDocId ,:pcV_SNo ,:poDocId,:poV_SNo, :itemCode, :hsnCode, :itemGSTNature,:ibCode, :batchNo, :expiryDate,"
                            + "     :manufacturingDate, :mrp, :skuMrp, :saleRate, :skuSaleRate, :unitCode,:quantity, :freeQty, :skuQty, "
                            + "     :skuQtyForRate, :skuRate, :rate, :remark,:pbRefNo,:pbRefDate)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("pbDocId", DmShared.foriegnKey(detail_Mdl.getPbDocId()));

                    psTemp.setString("pbV_SNo", DmShared.foriegnKey(detail_Mdl.getPbV_SNo()));
                    psTemp.setString("pcDocId", DmShared.foriegnKey(detail_Mdl.getPcDocId()));
                    psTemp.setString("pcV_SNo", DmShared.foriegnKey(detail_Mdl.getPcV_SNo()));
                    psTemp.setString("poDocId", DmShared.foriegnKey(detail_Mdl.getPoDocId()));
                    psTemp.setString("poV_SNo", DmShared.foriegnKey(detail_Mdl.getPoV_SNo()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                    psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setString("ibCode", DmShared.xNull(detail_Mdl.getIbCode()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("saleRate", DmServer.vNull(detail_Mdl.getSaleRate(), "0.00"));
                    psTemp.setString("skuSaleRate", DmServer.vNull(detail_Mdl.getSkuSaleRate()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQty()));
                    psTemp.setString("skuQty", DmServer.vNull(detail_Mdl.getSkuQty()));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getPurchaseUnitConversionRate()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
//                    psTemp.setString("landedSkuValue", DmShared.vNull(detail_Mdl.getLandedSkuValue()).toString());
//                    psTemp.setString("landedSkuRate", DmServer.vNull(detail_Mdl.getLandedSkuRate()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("pbRefNo", DmShared.xNull(detail_Mdl.getPbRefNo()));
                    psTemp.setDate("pbRefDate", DmServer.convertDate(detail_Mdl.getPbRefDate()));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }

            /*==============================<< Insert records into OH >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTempCurrent.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            OverHead_Server.save(mrTempCurrent.getOverHeadList(), "docId", mrTempCurrent.getSharedKeyFieldCode(), mrTempCurrent.getRecIdProformaPurchaseReturn(), mrTempCurrent.getVoucherTypeCode(), voucherPrefix, siteCode, mrTempCurrent.getVoucherDate(), "", mrTempCurrent.getRecIdProformaPurchaseReturn().getRecIdValue(), mrTempCurrent.getVoucherDate(), "", "", false, true, cnTemp);

            /*==============================<< Get header tax details >>====================================*/
            sqlQuery = "SELECT"
                    + "   ppr.oh_Amt_CGST"
                    + " , ppr.oh_Amt_SGST"
                    + " , ppr.oh_Amt_IGST"
                    + " , ppr.oh_Amt_UGST"
                    + " , ppr.oh_Amt_Net"
                    + " FROM proformaPurchaseReturn ppr"
                    + " WHERE ppr.docId = :docId";
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
                sqlQuery = "INSERT INTO proformaPurchaseReturnDetail ("
                        + "     docId, v_SNo, itemCode, hsnCode,quantity, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter)"
                        + " Values ("
                        + "     :docId, :v_SNo, :itemCode, :hsnCode,:quantity, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, 1)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
                psTemp.setString("v_SNo", String.valueOf(++vSNo));
                psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                if (DmShared.vNull(mrTempCurrent.getRoundOffAmount()) < 0) {
                    psTemp.setString("quantity", "-1");
                } else {
                    psTemp.setString("quantity", "1");
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
            if (mrTempCurrent.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTempCurrent.getModifyReasonMdl(), DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()), userName, cnTemp);
            }

            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTempCurrent.getGuiMdl().getGuiRowList(), mrTempCurrent.getSharedKeyFieldCode(), siteCode, cnTemp);

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
    public DmResult<ProformaPurchaseReturn_Mdl> getMoveRec(String strKey) {
        DmResult<ProformaPurchaseReturn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ProformaPurchaseReturn_Mdl mrTemp = new ProformaPurchaseReturn_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT "
                    + "       ppr.docId"
                    + "      ,ppr.v_Type"
                    + "      ,ppr.v_No"
                    + "      ,ppr.recId"
                    + "      ,ppr.v_Prefix"
                    + "      ,ppr.v_Date"
                    + "      ,ppr.subCode"
                    + "      ,ppr.consignee"
                    + "      ,ppr.placeOfSupply"
                    + "      ,ppr.CMN_Addition"
                    + "      ,ppr.CMN_Deduction"
                    + "      ,ppr.oh_Amt_Gross"
                    + "      ,ppr.oh_Amt_Total"
                    + "      ,ppr.oh_Amt_RoundOff"
                    + "      ,ppr.oh_Amt_Net"
                    + "      ,ppr.u_EntDt"
                    + "      ,ppr.u_Name"
                    + "      ,ppr.preparedBy"
                    + "      ,ppr.site_Code"
                    + "      ,ppr.wtaCode"
                    + "      ,ppr.gstReasonCode"
                    + "      ,ppr.remark"
                    + "      ,ppr.approvedBy"
                    + "      ,ppr.approvalDateTime"
                    + "      ,ppr.transferDt"
                    + "      ,ppr.recIdPrefix"
                    + "      ,ppr.rcmApplicable"
                    + "      ,ppr.partyType"
                    + "      ,ppr.u_EntDt_LatestLine"
                    + "      ,ppr.oh_Amt_Discount"
                    + "      ,ppr.oh_Amt_Taxable"
                    + "      ,ppr.oh_Amt_CGST"
                    + "      ,ppr.oh_Amt_SGST"
                    + "      ,ppr.oh_Amt_IGST"
                    + "      ,ppr.oh_Amt_UGST"
                    + "      ,ppr.guiConfigCode"
                    + "      ,ppr.isCancelled"
                    + " , vt.description AS voucherTypeName"
                    + " , vt.category AS voucherCategory"
                    + " , sgs.name AS supplierName"
                    + " , grm.name AS gstReasonName"
                    + " , c.stateCode AS supplierStateCode"
                    + " , sgs.partyType AS supplierType"
                    + " , sgs.gstin AS supplierGstin"
                    + " , sgs.gstinDate AS supplierGstinDate"
                    + "  ,case when pb.proformaDocid <> '' then 1 else 0 end as isMakeProforma "
                    + "  FROM proformaPurchaseReturn ppr"
                    + " left join pbill pb on pb.proformaDocid = ppr.docId"
                    + " LEFT JOIN voucher_Type vt ON ppr.v_Type = vt.v_Type"
                    + " LEFT JOIN subGroup sgs ON ppr.subcode = sgs.subCode"
                    + " LEFT JOIN city c ON sgs.cityCode = c.cityCode"
                    + " LEFT JOIN gstReasonMast grm ON ppr.gstReasonCode = grm.code"
                    + " WHERE ppr.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.getRecIdProformaPurchaseReturn().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdProformaPurchaseReturn().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdProformaPurchaseReturn().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));

                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("voucherTypeName")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("voucherCategory")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setSupplierCode(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setSupplierStateCode(DmShared.xNull(rsTemp.getString("supplierStateCode")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
//                mrTemp.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
//                mrTemp.setPartyBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                mrTemp.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                mrTemp.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.setIsCancelRecord(DmShared.iNull(rsTemp.getString("isCancelled")).toString());
                mrTemp.setIsMakeProforma(DmShared.xNull(rsTemp.getString("isMakeProforma")));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Medicine grid detail >>===============================*/
            sqlQuery = "SELECT \n"
                    + "   im.code AS itemCode\n"
                    + " , im.name AS itemName\n"
                    + " , imm.companyName AS companyName \n"
                    + " , prd.hsnCode\n"
                    + " , im.itsBasedOn\n"
                    + " , im.isBestBefore\n"
                    + " , im.propertyList\n"
                    + " , im.itemGSTNature\n"
                    + " , prd.pbDocId AS pbDocId\n"
                    + " , prd.pbV_SNo AS pbV_SNo \n"
                    + " , pb.recId AS pbRecId\n"
                    + " , pb.v_Type AS pbV_Type\n"
                    + " , pb.v_Date AS pbV_Date\n"
                    + " , prd.pbRefNo\n"
                    + " , prd.pbRefDate\n"
                    + " , pb.oh_Amt_Net AS pbNetAmount\n"
                    + " , pc.docId AS pcDocId \n"
                    + " , pc.recId AS pcRecId\n"
                    + " , pcd.v_SNo AS pcV_SNo\n"
                    + " , po.docId AS poDocId\n"
                    + " , po.recId AS poRecId\n"
                    + " , pod.v_SNo AS poV_SNo\n"
                    + " , po.refNo AS poRefNo\n"
                    + " , ums.code AS skuCode\n"
                    + " , ums.unit AS skuName\n"
                    + " , ums.decimalPlaces AS skuDecimalPlaces\n"
                    + " , um.code AS purchaseUnitCode\n"
                    + " , um.unit AS purchaseUnitName\n"
                    + " , um.decimalPlaces AS purcahseUnitDecimalPlaces\n"
                    + " , im.purchaseUnitConversionRate\n"
                    + " , um.gstUnitCode\n"
                    + " , prd.quantity\n"
                    + " , prd.freeQty\n"
                    + " , prd.skuQty\n"
                    + " , prd.rate\n"
                    + " , prd.skuRate\n"
                    + " , prd.oh_Amt_Gross\n"
                    + " , prd.mrp\n"
                    + " , prd.skuMrp\n"
                    + " , prd.saleRate\n"
                    + " , prd.skuSaleRate\n"
                    + " , prd.batchNo\n"
                    + " , prd.expiryDate\n"
                    + " , prd.manufacturingDate\n"
                    + " , prd.remark\n"
                    + " , prd.v_SNo\n"
                    + " , prd.ibCode\n"
                    + " , pbpfd.pfprDocId\n"
                    + " , ISNULL(pbd.acceptQty, 0) - ISNULL(pbd.returnedQty, 0) + ISNULL(prd.quantity, 0) AS pendingQty\n"
                    + " , ISNULL(pbd.freeQty,0) - ISNULL(pbd.returnedFreeQty,0) + ISNULL(prd.freeQty,0) AS pendingFreeQty\n"
                    + " FROM proformaPurchaseReturn ppr\n"
                    + " LEFT JOIN proformaPurchaseReturnDetail prd ON ppr.docId = prd.docId\n"
                    + " LEFT JOIN pBillDetail pbd ON prd.pbDocId = pbd.docId AND prd.pbV_SNo = pbd.v_SNo\n"
                    + " LEFT JOIN pBill pb ON pbd.docId = pb.docId\n"
                    + " LEFT JOIN pChallanDetail pcd ON prd.pcDocId = pcd.docId AND prd.pcV_SNo = pcd.v_SNo \n"
                    + " LEFT JOIN pChallan pc ON pcd.docId = pc.docId \n"
                    + " LEFT JOIN pOrderDetail pod ON prd.poDocId = pod.docId AND prd.poV_SNo = pod.v_SNo \n"
                    + " LEFT JOIN pOrder po ON pod.docId = po.docId \n"
                    + " LEFT JOIN itemMast im ON prd.itemCode = im.code\n"
                    + " LEFT JOIN itemMakeMast as imm on im.itemmakeCode=imm.code  \n"
                    + " LEFT JOIN unitMast ums ON im.sku = ums.code\n"
                    + " LEFT JOIN unitMast um ON prd.unitCode = um.code\n"
                    + " LEFT JOIN pBillDetail pbpfd on pbpfd.pfprDocId = prd.docId and pbpfd.pfprV_Sno = prd.v_SNo  \n"
                    + " WHERE prd.docId = :docId\n"
                    + " AND ISNULL(prd.isFooter, 0) = 0\n"
                    + " ORDER BY prd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();

                //Item
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));

                node.setItemCompany(DmShared.xNull(rsTemp.getString("companyName")));

                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));

                //Bill Details
                node.setPbDocId(DmShared.xNull(rsTemp.getString("pbDocId")));
                node.setPbRecId(DmShared.xNull(rsTemp.getString("pbRecId")));
                node.setPbV_Type(DmShared.xNull(rsTemp.getString("pbV_Type")));
                node.setPbV_Date(DmShared.dNull(rsTemp.getDate("pbV_Date")));
                node.setPbV_SNo(DmShared.xNull(rsTemp.getString("pbV_SNo")));
                node.setPbNetAmount(DmShared.xNull(rsTemp.getString("pbNetAmount")));
                node.setPbRefNo(DmShared.xNull(rsTemp.getString("pbRefNo")));
                node.setPbRefDate(DmShared.dNull(rsTemp.getDate("pbRefDate")));
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
                double acceptQty = DmShared.vNull(rsTemp.getString("quantity"));
                double freeQty = DmShared.vNull(rsTemp.getString("freeQty"));
                double purchaseUnitConversionRate = (skuQty / (acceptQty + freeQty));
                String puConversionRate = DmServer.vNull(purchaseUnitConversionRate, DmShared.getNumberFormat(DmShared.iNull(node.getSkuDecimalPlaces()).intValue()));
                node.setPurchaseUnitConversionRate(DmServer.vNull(puConversionRate));

                node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                if (!DmShared.xNull(rsTemp.getString("skuCode")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("purchaseUnitCode")))) {
                    node.setPurchaseUnitDisplayName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " = " + node.getPurchaseUnitConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                }

                //Quantities
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setPendingQty(DmServer.vNull(rsTemp.getString("pendingQty")));
                node.setPendingFreeQty(DmServer.vNull(rsTemp.getString("pendingFreeQty")));
                node.setSkuQty(DmServer.vNull(rsTemp.getString("skuQty")));

                //Rate
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSaleRate(DmServer.vNull(rsTemp.getString("saleRate"), "0.00"));
                node.setSkuSaleRate(DmServer.vNull(rsTemp.getString("skuSaleRate")));

                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                //Properties
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

                //Other
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                
                if(DmShared.iNull(rsTemp.getString("pfprDocId")).longValue()> 0){
                    node.setIsProformaUsed("1");
                }

                /*------------------- Calculate Landed Sku Rate of Purchase Bill--------------------------------*/
                String landedSkuRate = "";
                landedSkuRate = DmServer.executeScalar("select top 1 \n"
                        + "pbd.landedSkuRate\n"
                        + "from pBillDetail pbd \n"
                        + "left join proformaPurchaseReturn pb on pb.docid = pbd.docid\n"
                        + "left join itemMast im on im.code = pbd.itemCode\n"
                        + "left join itemBatch ib on ib.code = pbd.ibCode \n"
                        + "where im.code  = " + DmShared.iNull(node.getSharedKeyFieldCode()).toString()
                        + "and ib.code = " + DmShared.iNull(node.getIbCode()).toString()
                        + "order by pb.v_Date desc", cnTemp);
                node.setpBillLandedSkuRate(DmServer.vNull(landedSkuRate));


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
    public DmResult<String> deleteRecord(String keyCode, String guiConfigCode, ProformaPurchaseReturn_Mdl mrTempPrevious) {
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
            sqlQuery = "SELECT approvedBy, wtaCode FROM proformaPurchaseReturn WHERE docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                wtaCode = DmShared.xNull(rsTemp.getString("wtaCode"));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't discard this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Delete record from Overhead, OverheadLine, ModifyReason & Walkthrough >>============================*/
//            OverHeadLine_Server.delete(keyCode, cnTemp);
//            OverHead_Server.delete(keyCode, cnTemp);
//            ModifyReason_Server.delete(keyCode, cnTemp);
            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
//            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);
            Gst_Server.deleteGstTrans(keyCode, cnTemp);


            /*==============================<< Delete record from proformaPurchaseReturnDetail >>===============================*/
//            String ibCodeCsv = STServer.getCsvFieldValue("proformaPurchaseReturnDetail", "ibCode", " AND docid = '" + DmShared.iNull(keyCode).toString() + "'", cnTemp);
            sqlQuery = "UPDATE proformaPurchaseReturnDetail SET isCancelled = 1 WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(keyCode));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from proformaPurchaseReturn >>===============================*/
            sqlQuery = "UPDATE proformaPurchaseReturn SET isCancelled = 1  WHERE docId = :docId";
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

            sqlQuery = " SELECT ppr.docId \n"
                    + " FROM proformaPurchaseReturn ppr\n"
                    + " LEFT JOIN voucher_Type vt ON ppr.v_Type = vt.v_Type\n"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "','" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED + "')\n"
                    + " AND ppr.site_Code = :site_Code\n"
                    + " AND ppr.v_Prefix = :vPrefix ";
            condition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!condition.equalsIgnoreCase("")) {
                sqlQuery += " AND " + condition;
            }
            sqlQuery += " ORDER BY ppr.v_Date DESC, vt.short_Name, ppr.v_No DESC, ppr.recId";
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
                    + "   ppr.docId"
                    + " , ppr.recId"
                    + " , ppr.v_Date"
                    + " , ppr.v_No"
                    + " , sg.name AS supplierName\n"
                    + " , vt.short_Name"
                    + " , ppr.approvedBy\n"
                    + " , format(ppr.approvalDateTime,'dd-MMM-yyyy HH.mm') AS approvalDateTime\n"
                    + " , ppr.partyBillNo, ppr.partyBillDate,\n"
                    + " CASE WHEN ppr.isCancelled = 1 THEN 'Discarded' ELSE '' END as status \n"
                    + "FROM proformaPurchaseReturn ppr\n"
                    + " LEFT JOIN voucher_Type vt ON ppr.v_Type = vt.v_Type\n"
                    + "  LEFT JOIN subGroup sg ON ppr.subCode = sg.subCode\n"
                    + "WHERE vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "','" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED + "')"
                    + " 	AND ppr.site_Code = :site_Code"
                    + " 	AND ppr.v_Prefix = :vPrefix";
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
                listNode.setStatus(DmShared.xNull(rsTemp.getString("status")));
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

    @Override
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            dmrTemp = STServer.getITS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, ITSType.PURCHASE_RETURN, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);

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
    public DmResult<KeyFieldExtended> getLandedSkuRateForValidation(String ibCode, String itemCode) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        KeyFieldExtended keyMdl = new KeyFieldExtended();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select top 1 ib.batchNo,im.name,pb.recId,pbd.saleRate,pbd.skuSaleRate,ib.skuSaleRate,ib.saleRate,\n"
                    + "pbd.landedSkuRate\n"
                    + "from proformaPurchaseReturnDetail pbd \n"
                    + "left join pBill pb on pb.docid = pbd.docid\n"
                    + "left join itemMast im on im.code = pbd.itemCode\n"
                    + "left join itemBatch ib on ib.code = pbd.ibCode\n"
                    + "where im.code  = :itemCode\n"
                    + "and ib.code = :ibCode\n"
                    + "order by pb.v_Date desc";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.xNull(itemCode));
            psTemp.setString("ibCode", DmShared.xNull(ibCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                keyMdl.setSharedKeyFieldCode(DmShared.vNull(rsTemp.getString("landedSkuRate")).toString());
            }
            dmrTemp.setData(keyMdl);
            dmrTemp.setStatus(true);

            psTemp.close();
            rsTemp.close();
            cnTemp.close();
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

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }
}
