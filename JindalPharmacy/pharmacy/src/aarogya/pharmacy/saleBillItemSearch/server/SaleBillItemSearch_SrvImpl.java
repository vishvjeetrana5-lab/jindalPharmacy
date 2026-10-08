package aarogya.pharmacy.saleBillItemSearch.server;

import aarogya.pharmacy.saleBillItemSearch.shared.GrdSaleBillItemSearch_Mdl;
import aarogya.pharmacy.saleBillItemSearch.shared.SaleBillItemSearch_Srv;
import aarogya.pharmacy.saleBillItemSearch.shared.SaleBillItemSearch_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SaleBillItemSearch_SrvImpl extends DmRSSPrivate implements SaleBillItemSearch_Srv {

    @Override
    public DmResult<List<GrdSaleBillItemSearch_Mdl>> getSaleBillItemSearchDetail(SaleBillItemSearch_Mdl searchValue_Mdl, String callFrom) {
        DmResult<List<GrdSaleBillItemSearch_Mdl>> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery = "", join = "", pendingQty = "";
        List<GrdSaleBillItemSearch_Mdl> lstTemp = new ArrayList<GrdSaleBillItemSearch_Mdl>();
        GrdSaleBillItemSearch_Mdl node;
        rstTemp.setData(lstTemp);
        String categoryCondition = "";
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            Calendar returnDate = Calendar.getInstance();
            returnDate.setTime(new Date());
            returnDate.add(Calendar.DATE, 0 - DmShared.iNull(searchValue_Mdl.getReturnDays()).intValue());

            if (DmShared.xNull(callFrom).equalsIgnoreCase("wholeSaleBillReturn")) {

                categoryCondition = "'" + new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CASH + "'" + "," + "'" + new ProjectVoucherCategory().PHARMACY_BILL_WHOLESALE_CREDIT + "'";

            } else if (DmShared.xNull(callFrom).equalsIgnoreCase("saleBillReturn")) {

                categoryCondition = "'" + new ProjectVoucherCategory().PHARMACY_BILL_CASH + "'";

            }

            if (DmShared.iNull(searchValue_Mdl.getFormDocId()).longValue() > 0) {
                join = " SELECT srd.sbDocid, srd.sbV_SNo, srd.quantity,srd.freeQty "
                        + " FROM saleBillDetail srd "
                        + " WHERE srd.docid = :srDocid ";

                pendingQty = " CASE WHEN IsNull(sbd.quantity,0) > 0 THEN (IsNull(sbd.quantity,0)-IsNull(sbd.returnedQty,0) + IsNull(vSbr.quantity,0)) ELSE 0 END";
            } else {
                pendingQty = " CASE WHEN IsNull(sbd.quantity,0) > 0 THEN (IsNull(sbd.quantity,0)-IsNull(sbd.returnedQty,0)) ELSE 0 END ";
            }

            condition = " where "
                    + " vt.category in (" + categoryCondition + ") "
                    + " and sbd.isFooter = 0 "
                    + " and im.isReturnableItem = 1 "
                    + " and ISNULL(sbd.isReturn,0) = 0 "
                    + " and ISNULL(sb.isCancelled,0) = 0 "
                    + "";

            if (!DmShared.dNull(searchValue_Mdl.getV_Date()).equalsIgnoreCase("")) {
                condition += " AND sb.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            if (DmShared.iNull(searchValue_Mdl.getReturnDays()).longValue() > 0) {
                condition += " AND sb.v_Date >= '" + DmShared.dNull(returnDate.getTime()) + "' ";
            }
            if (!DmShared.xNull(searchValue_Mdl.getDepartmentTag()).equalsIgnoreCase("")) {
                condition += " and sb.departmentCode='" + DmShared.xNull(searchValue_Mdl.getDepartmentTag()) + "' ";
            } else {
                condition += " AND 1 = 2";
            }
            if (DmShared.iNull(searchValue_Mdl.getFormDocId()).longValue() > 0) {
                condition += " and IsNull(sbd.quantity,0)-IsNull(sbd.returnedQty,0)+IsNull(vSbr.quantity,0) > 0";
            } else {
                condition += " and IsNull(sbd.quantity,0)-IsNull(sbd.returnedQty,0) > 0";
            }

            if (DmShared.iNull(searchValue_Mdl.getPatientRegDocId()).longValue() > 0) {
                condition += " and sb.regDocId = :regDocId";
            }

            if (DmShared.xNull(searchValue_Mdl.getVoucherType()).trim().equalsIgnoreCase("MRR")) {
                condition += " and vt.category = 'PHBIL' ";
            }

            if (DmShared.iNull(searchValue_Mdl.getLmCode()).longValue() > 0) {
                condition += " and sb.lmCode = :lmCode";
            }
            if (DmShared.iNull(searchValue_Mdl.getSaleBillDocId()).longValue() > 0) {
                condition += " and sbd.docId = :docId";
            }
            if (!DmShared.xNull(STServer.getSsnPharmacyAllowSaleBillForSite(this)).equalsIgnoreCase("")) {
                condition += " and sb.site_Code IN (" + STServer.getSsnPharmacyAllowSaleBillForSite(this) + ") ";
            } else {
                condition += " 1 = 2 ";
            }

            sqlQuery = " Select sbd.docid,sbd.v_SNo, sbd.v_SNo,sb.recId as billNo,sb.v_Date as billDate, " + pendingQty + " AS pendingQty, sbd.itemCode,im.name as medicine, im.allowNegativeStock, im.hsnCode, "
                    + "         im.isBestBefore, im.isReturnableItem, im.itemGSTNature, im.itsBasedOn, im.propertyList, im.saleUnitConversionRate, "
                    + "         um.unit, um.decimalPlaces AS skuDecimalPlaces, ums.decimalPlaces, im.saleUnit, ums.unit saleUnitName, im.sku, "
                    + "         um.unit skuName, hm.hsn, im.purchaseUnitConversionRate, um.gstUnitCode, sbd.skuMrp,"
                    + "         im.isSaleRateEditable, im.restrictGroup, sbd.skuRate, im.purchaseUnit, ump.unit purchaseUnitName,sbd.ibCode,sbd.batchNo,sbd.manufacturingDate,"
                    + "         sbd.expiryDate,sbd.rate,sbd.mrp, sbd.oh_at_Discount, sbd.oh_Amt_Discount, "
                    + "         sbd.oh_Amt_Gross,lm.displayName as patientName,sbd.oh_Amt_Net , sbd.grossPurchaseRate "
                    + "	from saleBillDetail as sbd "
                    + "	left JOIN saleBill as sb ON sb.docid = sbd.docid "
                    + " left JOIN voucher_Type vt On vt.v_Type=sb.v_type "
                    + "	left JOIN ledgerMast as lm ON sb.lmCode = lm.code "
                    + "	left join itemMast im on im.code = sbd.itemCode "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join unitMast ums on ums.code = im.saleUnit "
                    + "	left join unitMast ump on ump.code = im.purchaseUnit "
                    + "	left join hsnMast hm on hm.code = im.hsnCode ";
            if (DmShared.iNull(searchValue_Mdl.getFormDocId()).longValue() > 0) {
                sqlQuery += "LEFT JOIN (" + join + ") vSbr on vSbr.sbDocid = sbd.docid AND vSbr.sbV_SNo = sbd.v_SNo ";
            }

            sqlQuery += condition;
            sqlQuery += " ORDER BY im.name ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.iNull(searchValue_Mdl.getFormDocId()).longValue() > 0) {
                psTemp.setString("srDocid", searchValue_Mdl.getFormDocId());
            }
            if (DmShared.iNull(searchValue_Mdl.getSaleBillDocId()).longValue() > 0) {
                psTemp.setString("docId", searchValue_Mdl.getSaleBillDocId());
            }
            if (DmShared.iNull(searchValue_Mdl.getPatientRegDocId()).longValue() > 0) {
                psTemp.setString("regDocId", searchValue_Mdl.getPatientRegDocId());
            }
            if (DmShared.iNull(searchValue_Mdl.getLmCode()).longValue() > 0) {
                psTemp.setString("lmCode", searchValue_Mdl.getLmCode());
            }
            if (!DmShared.dNull(searchValue_Mdl.getV_Date()).equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(searchValue_Mdl.getV_Date()));
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new GrdSaleBillItemSearch_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("billNo")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setItemName(DmShared.xNull(rsTemp.getString("medicine")));
                node.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                node.setBillDate(DmShared.dNull(rsTemp.getDate("billDate")));
                node.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setConversionRateSaleUnit(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                node.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                node.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setAllowSaleUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                node.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                node.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setSkuGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                node.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                node.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(DmShared.iNull(rsTemp.getString("itemCode")).toString(), 0, DmShared.xNull(rsTemp.getString("itsBasedOn")), searchValue_Mdl.getV_Date(), cnTemp);

                node.getLstITS().addAll(dmrIts.getData());
                node.setSaleAcCode(dmrIts.getValue1());

                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                if (DmShared.vNull(rsTemp.getString("oh_at_Discount")).doubleValue() == 0
                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() > 0) {
                    double tempDiscountPer = 0;
                    tempDiscountPer = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();
                    node.setTempDiscountPer(DmServer.vNull(tempDiscountPer, "0.00"));
                }

                node.setQuantity(DmServer.vNull(DmServer.vNull(DmShared.vNull(rsTemp.getString("pendingQty")), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue()))));
                double tempQty = DmShared.vNull(rsTemp.getString("pendingQty")) * DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
                node.setQuantitySKU(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue())));

                node.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setV_Sno(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setGrossPurchaseRate(DmServer.vNull(rsTemp.getString("grossPurchaseRate"), "0.00"));

                lstTemp.add(node);
            }
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

    private DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate, Connection cnTemp) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();

        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String location = "";
        try {
            sqlQuery = "SELECT ISNULL(sm.isUt,0) AS isUt FROM stateMast sm WHERE sm.numericCode = :numericCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("numericCode", DmServer.getSsnSiteStateNumericCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("isUT")).equalsIgnoreCase("1")) {
                    location = DmShared.iNull(DmEnum.DmLocation.UnionTerritory.getId()).toString();
                } else {
                    location = DmShared.iNull(DmEnum.DmLocation.IntraState.getId()).toString();
                }
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp = STServer.getITS(itemCode, rate, itsBasedOn, STEnum.ITSType.SALES_RETURN, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentRequisitionSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 " + condition;
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
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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
    public DmResult<String> openRecord(boolean isOpen, String docId, String v_SNo) {
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

            if (isOpen) {
                sqlQuery = " Update requisitionDetail set "
                        + " isStatus=:isStatus "
                        + " where docId=:docId AND v_SNo=:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.setString("isStatus", "0");
                psTemp.executeUpdate();

                psTemp.close();
            } else {
                sqlQuery = " select Count(isStatus) as cnt from requisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 1";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is already cancelled,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " select Count(isStatus) as cnt from requisitionDetail where docId = :docId AND v_SNo=:VSNo AND isStatus = 2";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("VSNo", DmShared.iNull(v_SNo).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                        throw new DmException(0, "Record is completed,Please refresh record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = " UPDATE requisitionDetail SET "
                        + " isStatus = 1 "
                        + " WHERE docId = :docId AND v_SNo =:vSNo";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("vSNo", DmShared.iNull(v_SNo).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }
            sqlQuery = " Update requisition set "
                    + " u_EntDt_LatestLine=:uEntDt "
                    + " where docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

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
