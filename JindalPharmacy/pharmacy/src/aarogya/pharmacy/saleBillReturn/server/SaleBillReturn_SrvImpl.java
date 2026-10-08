package aarogya.pharmacy.saleBillReturn.server;

import aarogya.pharmacy.ledgerMaster.server.LedgerMasterServer;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.saleBill.shared.SaleBill_Mdl;
import aarogya.pharmacy.saleBillReturn.shared.*;
import aarogya.pharmacy.self.server.Pharmacy_Server;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_Server;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import aarogya.selfToolkit.uhidPopup.shared.Layout_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SaleBillReturn_SrvImpl extends DmRSSPrivate implements SaleBillReturn_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypefieldName = "mb.V_Type", voucherDateFieldName = "mb.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(String voucherCategory, UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT mb.DocId  "
                    + "FROM saleBill mb  "
                    + "LEFT JOIN voucher_type vt ON vt.v_type = mb.v_type  "
                    + "WHERE vt.category IN ( " + getVoucherCategory(voucherCategory) + " ) and mb.site_Code=:siteCode and mb.v_Prefix =:vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += "order by mb.v_Date desc, vt.short_Name, mb.v_no desc, mb.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            psTemp.setString("vPrefix", DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).toString());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
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
    public DmResult<SaleBillReturn_Mdl> getMoveRec(String strKey) {
        DmResult<SaleBillReturn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempHIS = null;
        CardPaymentPopup_Mdl cardPaymentPopup_SaleBillReturn_Mdl = new CardPaymentPopup_Mdl();
        SaleBillReturn_Mdl mrTemp = new SaleBillReturn_Mdl();
        dmrTemp.setData(mrTemp);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TxtHMedicine_Mdl medicineMdl;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTempHIS = DmServer.getConnection(STServer.getSsnParentDateBase(this));

            sqlQuery = "SELECT mb.docId, mb.recId, mb.v_Type, pbvt.description,pbvt.short_Name,pbvt.category as voucherCategory, mb.v_Date, prvt.category,mb.subCode, mb.regDocId, pr.recId AS regNo, pr.site_Code AS regSiteCode, "
                    + "     pr.roomCode,mb.uhid, pu.recId AS UHIDNo, mb.departmentCode, sdm.name AS departmentName, lm.initial as patientInitial, lm.displayName as patientName, "
                    + "     mb.consultantCode, sgcon.name AS consultantName, mb.preparedBy, mb.u_Name, mb.remark, mb.oh_Amt_Gross, mb.oh_Amt_Net, mb.oh_Amt_LandedValue, "
                    + "     mb.oh_Amt_BAMT, mb.oh_Amt_RoundOff, mb.oh_Amt_CGST, mb.oh_Amt_SGST, mb.oh_Amt_UGST, mb.oh_Amt_Taxable, mb.oh_Amt_Discount, "
                    + "     mb.recIdPrefix, mb.v_No, prec.v_type as precVType, prec.recid as precRecID, prec.recIdPrefix as precRecIdPrefix, prec.v_No as precRecIdCounter,prec.docId as refundDocId, "
                    + "     vtprec.description as precVDescription, vtprec.Category as rcptVTypeCategory, mb.idTypeCode, mb.idNo, idType.name idType, "
                    + "     vtprec.attachPaymentMode AS attachPaymentMode, mb.isCancelled, "
                    + "     mb.mobile,mb.u_EntDt_LatestLine,mb.refNo,mb.refDate,mb.gstReasonCode,grm.name as gstReasonName "
                    + "     ,mb.partyType"
                    + "     ,mb.lmCode,lm.address,c.cityName,lm.cityCode,mb.placeOfsupply as stateCode,sm.name as stateName "
                    + "     , lm.name as lmName "
                    + "     , lm.manualCode as lmManualCode "
                    + "     , mb.oh_Amt_Discount2  , mb.paymentSource,sbr.returnDocID "
                    + "    , mb.regDocId_HIS, mb.regNo_HIS, mb.uhidNo_HIS,mb.siteCode_HIS, mb.regType_HIS "
                    + "  FROM saleBill mb "
                    + "  LEFT JOIN saleBillReturn sbr ON mb.docid=sbr.returnDocID   "
                    + "  LEFT JOIN voucher_type pbvt ON mb.v_Type=pbvt.v_Type    "
                    + "  LEFT JOIN gstReasonMast grm ON grm.code=mb.gstReasonCode    "
                    + "  LEFT JOIN ledgerMast lm ON lm.code = mb.lmCode    "
                    + "  LEFT JOIN patientregistration pr ON mb.regDocid=pr.regDocid  "
                    + "  LEFT JOIN voucher_type prvt ON pr.v_Type=prvt.v_Type    "
                    + "  LEFT JOIN SiteMast prsm ON pr.site_Code=prsm.code  "
                    + "  LEFT JOIN patientuhid pu ON mb.uhid = pu.docId  "
                    + "  LEFT JOIN subgroup sdm ON mb.departmentCode=sdm.subcode  "
                    + "  LEFT JOIN consultantmast cm ON mb.consultantCode=cm.subCode  "
                    + "  LEFT JOIN city AS c ON c.citycode = lm.citycode "
                    + "  LEFT JOIN stateMast AS sm ON sm.code = mb.placeOfsupply "
                    + "  LEFT JOIN subGroup AS sgcon ON cm.subCode = sgcon.subCode  "
                    + "  LEFT JOIN patientReceipt AS prec ON mb.DocId = prec.docId "
                    + "	 LEFT JOIN hl7Mast as idType on idType.code = mb.idTypeCode "
                    + "	 LEFT JOIN voucher_type vtprec ON prec.v_type = vtprec.v_type "
                    + "  WHERE mb.docID = :docId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdSaleBillReturn().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdSaleBillReturn().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecIdSaleBillReturn().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setVoucherShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("voucherCategory")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setRegRecID(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setRegSiteCode(DmShared.xNull(rsTemp.getString("regSiteCode")));
                mrTemp.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));
                mrTemp.setRegDocid(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setUhidDocid(DmShared.xNull(rsTemp.getString("uhid")));
                mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("UHIDNo")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setPatientIntial(DmShared.xNull(rsTemp.getString("patientInitial")));
                mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idType")));
                mrTemp.setRefundDocId(DmShared.xNull(rsTemp.getString("refundDocId")));

                mrTemp.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                mrTemp.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));

                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                mrTemp.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                mrTemp.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));

                if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() > 0) {
                    mrTemp.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(DmShared.iNull(rsTemp.getString("lmCode")).toString(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
                }

                if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() == 0 && DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() > 0) {
                        mrTemp.setTypeCode("Ledger");
                        mrTemp.setTypeName("Ledger");
                    }
                } else if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() == 0) {
                    mrTemp.setTypeCode("Walk-In");
                    mrTemp.setTypeName("Walk-In");
                }

                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));

                mrTemp.setGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.setDiscountAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                mrTemp.setDiscountAmt2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
                mrTemp.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                mrTemp.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                mrTemp.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                mrTemp.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                mrTemp.setBillAmt(DmServer.vNull(rsTemp.getString("oh_Amt_BAMT"), "0.00"));
                mrTemp.setRoundOffAmt(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                mrTemp.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                mrTemp.setLandedValue(DmServer.vNull(rsTemp.getString("oh_Amt_LandedValue"), "0.00"));
                mrTemp.setReceiptTypeCode(DmShared.xNull(rsTemp.getString("precVType")));
                mrTemp.setReceiptTypeName(DmShared.xNull(rsTemp.getString("precVDescription")));
                mrTemp.setReceiptTypeCateory(DmShared.xNull(rsTemp.getString("rcptVTypeCategory")));
                mrTemp.getRecIdRecipt().setRecIdValue(DmShared.xNull(rsTemp.getString("precRecID")));
                mrTemp.getRecIdRecipt().setPrefix(DmShared.xNull(rsTemp.getString("precRecIdPrefix")));
                mrTemp.getRecIdRecipt().setCounter(DmShared.iNull(rsTemp.getString("precRecIdCounter")).longValue());
                mrTemp.setPrecVDescription(DmShared.xNull(rsTemp.getString("precVDescription")));
                mrTemp.setPrecVType(DmShared.xNull(rsTemp.getString("precVType")));
                mrTemp.setPrecAttachPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));
                mrTemp.setCancelRecord(DmShared.iNull(rsTemp.getString("isCancelled")).toString());
                mrTemp.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
                mrTemp.setMedicineReturnRequestDocID(DmShared.xNull(rsTemp.getString("returnDocID")));

                if (!DmShared.xNull(rsTemp.getString("RegType_HIS")).equalsIgnoreCase("")) {
                    mrTemp.setRegDocId_HIS(DmShared.xNull(rsTemp.getString("regDocId_HIS")));
                    mrTemp.setRegNo_HIS(DmShared.xNull(rsTemp.getString("RegNo_HIS")));
                    mrTemp.setUhidNo_HIS(DmShared.xNull(rsTemp.getString("UhidNo_HIS")));
                    mrTemp.setSiteCode_HIS(DmShared.xNull(rsTemp.getString("SiteCode_HIS")));
                    mrTemp.setRegType_HIS(DmShared.xNull(rsTemp.getString("RegType_HIS")));

                    mrTemp.setPaymentSourceName(PaymentSource.getValueOf(DmShared.iNull(rsTemp.getString("paymentSource")).intValue()).getEnumName());

                    if (DmShared.iNull(rsTemp.getString("regDocId_HIS")).longValue() > 0) {
                        mrTemp.setAllowCredittoRoom(chekForIPDBillCreationAndAllowCTR(DmShared.iNull(rsTemp.getString("regDocId_HIS")).toString(), cnTempHIS));
                    }
                }

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select mbd.docId, mbd.v_SNo, mm.isBestBefore, mbd.hsnCode, mbd.itemGSTNature, mbd.itemCode, mm.name AS itemName, mbd.quantity, "
                    + "         mbd.skuQuantity, mbd.rate, mbd.oh_at_Discount, mbd.oh_Amt_Discount, mbd.oh_at_Discount2, mbd.oh_Amt_Discount2, um.decimalPlaces as skuDecimalPlaces, "
                    + "         mbd.oh_Amt_Net, mbd.oh_Amt_Gross, mbd.oh_at_CGST, mbd.isFooter, mm.sku, um.unit AS skuName, umsale.gstUnitCode, "
                    + "         mbd.sbDocId, mbl.recId AS saleBillNo,mbd.sbV_Sno,mbl.v_type as saleBillVType,mbl.oh_Amt_Net as netAmtSaleBill,"
                    + "         umsale.unit AS saleUnitName, mbd.saleUnitCode AS saleUnit"
                    //                    + "         , CAST(mbd.skuQuantity / mbd.quantity AS decimal(18, 7)) AS saleUnitConversionRate, "
                    + "         , mm.saleUnitConversionRate, "
                    + "         umsale.decimalPlaces, mbd.oh_Amt_CGST, mbd.oh_Amt_SGST, mbd.oh_at_SGST, mbd.oh_Amt_Taxable, mbd.oh_Amt_UGST, "
                    + "         mbd.oh_at_UGST, mbd.mrp, mbd.skuRate, mbd.skuMrp, mm.allowNegativeStock, mm.isBarcodeActive, mbd.isReturn, mbd.oh_Ac_CGST, "
                    + "         mbd.oh_Ac_SGST, mbd.oh_Ac_UGST, mbd.oh_Ac_Sales, mbd.landedSkuValue, mm.itsBasedOn, mbd.batchNo, mbd.expiryDate, mbd.manufacturingDate, "
                    + "         mbd.ibCode, mm.restrictGroup, mm.purchaseUnit, ump.unit purchaseUnitName, mbd.skuQtyForRate,mbd.freeQty as freeQtyReturned, "
                    + "         CASE WHEN IsNull(smbl.quantity,0) <> 0 THEN (IsNull(smbl.quantity,0)-IsNull(smbl.returnedQty,0)+IsNull(mbd.quantity,0)) ELSE 0 END as sbQuantity, "
                    + "         CASE WHEN IsNull(smbl.freeQty,0) <> 0 THEN (IsNull(smbl.freeQty,0)-IsNull(smbl.returnedFreeQty,0)+IsNull(mbd.freeQty,0)) ELSE 0 END as sbFreeQuantity, "
                    + "         mbd.barcode"
                    + "	from saleBillDetail AS mbd "
                    + "	left join saleBill AS mbl ON mbd.sbDocId = mbl.docId "
                    + "	left join saleBillDetail AS smbl ON mbd.sbDocId = smbl.docId and mbd.sbV_SNo=smbl.V_SNo"
                    + "	left join itemMast AS mm ON mbd.itemCode = mm.code "
                    + "	left join unitMast AS um ON mm.sku = um.code "
                    + "	left join unitMast AS umsale ON umsale.code = mbd.saleUnitCode "
                    + "	left join unitMast AS ump ON ump.code = mm.purchaseUnit "
                    + "	where mbd.docId = :docId and isnull(mbd.isFooter, 0) = 0 "
                    + " order by mbd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdMedicine_Mdl detailMdl = new GrdMedicine_Mdl();
                detailMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                detailMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                detailMdl.setvSno(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                detailMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                detailMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                detailMdl.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                detailMdl.setItemCodeCsv(DmShared.xNull(Pharmacy_Server.getItemCodeCsv(detailMdl.getBarcode(), mrTemp.getVoucherDate(), mrTemp.getDepartmentCode(), DmServer.getSsnSiteCode(this), ItemBatchType.NON_EXPIRED_BATCH, false, true, cnTemp)));
                detailMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                detailMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
//                medicineMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
//                medicineMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
//                medicineMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
//                medicineMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                detailMdl.setMedicineMdl(medicineMdl);

                detailMdl.setFreeQty(DmServer.vNull(rsTemp.getString("freeQtyReturned")));
                detailMdl.setPrvFreeQuantity(DmServer.vNull(rsTemp.getString("freeQtyReturned")));
                detailMdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setPrvQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                detailMdl.setQuantitySKU(DmServer.vNull(rsTemp.getString("skuQuantity")));
                detailMdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                detailMdl.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                detailMdl.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                detailMdl.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                detailMdl.setSbQuantity(DmServer.vNull(rsTemp.getString("sbQuantity")));
                detailMdl.setSbFreeQuantity(DmServer.vNull(rsTemp.getString("sbFreeQuantity")));
                detailMdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                detailMdl.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                detailMdl.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));
                detailMdl.setDiscount2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
                detailMdl.setTotalDiscount(DmServer.vNull((DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() + DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue()), "0.00"));
                detailMdl.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                detailMdl.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                detailMdl.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                detailMdl.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                detailMdl.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                detailMdl.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                detailMdl.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                detailMdl.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                detailMdl.setLandedSkuValue(DmServer.vNull(rsTemp.getString("landedSkuValue"), "0.00"));
                detailMdl.setSaleAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
                detailMdl.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                detailMdl.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                detailMdl.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));

                detailMdl.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
                detailMdl.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));

                if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() == 0) {
                    mrTemp.setDocIdSaleBill(DmShared.xNull(rsTemp.getString("sbDocId")));
                    mrTemp.setRecIdSaleBill(DmShared.xNull(rsTemp.getString("saleBillNo")));
                    mrTemp.setNet_AmtSaleBill(DmServer.vNull(rsTemp.getString("netAmtSaleBill"), "0.00"));
                    mrTemp.setV_TypeSaleBill(DmShared.xNull(rsTemp.getString("saleBillVType")));
                }
                detailMdl.setSbRecId(DmShared.xNull(rsTemp.getString("saleBillNo")));
                detailMdl.setSbDocId(DmShared.xNull(rsTemp.getString("sbDocId")));
                detailMdl.setSbV_Sno(DmShared.xNull(rsTemp.getString("sbV_Sno")));

                mrTemp.getGrdMediDetails().add(detailMdl);
            }
            psTemp.close();
            rsTemp.close();

            String roundOffItemCode = DmServer.getCodeViaManualCode("ROFF", "manualCode", "overheadMast", "itemCode", cnTemp);
            sqlQuery = "select mbd.oh_Ac_Sales "
                    + "	from saleBillDetail AS mbd "
                    + "	where mbd.docId = :docId and mbd.itemCode = :itemCode and isnull(mbd.isFooter, 0) = 1 "
                    + " order by mbd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.setString("itemCode", DmShared.iNull(roundOffItemCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setRoundOffAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
            }
            psTemp.close();
            rsTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Sale Bill Return Payment">
            cardPaymentPopup_SaleBillReturn_Mdl = CardPaymentPopup_Server.getCardListViaDocId(strKey, cnTemp);
            mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList().addAll(cardPaymentPopup_SaleBillReturn_Mdl.getGrdCardPaymentList());
            mrTemp.setCardPaymentSaleBillReturn(cardPaymentPopup_SaleBillReturn_Mdl);
            //</editor-fold>

            getPatientDetail(mrTemp, null, true, cnTemp);

            cnTemp.close();
            cnTempHIS.close();

            dmrTemp.setData(mrTemp);
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
                if (cnTempHIS != null) {
                    cnTempHIS.close();
                }

            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(String voucherCategory, SaleBillReturn_Mdl mrTemp, SaleBillReturn_Mdl prevMdl, String voucherPrefix, String returnRequestDocID, boolean saveCardInfo, boolean saveModifyReason) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempHsp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, cardPaymentFlag = false, creditFlag = false, allowReturnWithoutSale = false;
        String sqlQuery, userName, siteCode, saleQty = "";
        TxtHMedicine_Mdl medicineMdl;
        ResultSet rsTemp = null;
        double returnReciptAmt = 0;
        int srNo = 200;
        dmrTemp.setData("");
        try {
            if (DmShared.xNull(mrTemp.getIdTypeName()).trim().equalsIgnoreCase("Aadhaar")) {
                mrTemp.setIdNo(DmShared.xNull(mrTemp.getIdNo()).replaceAll(" ", ""));
                if (DmShared.xNull(mrTemp.getIdNo()).trim().length() != 12) {
                    throw new DmException(0, "AADHAAR No.. should be 12 digit.");
                }
                if (!DmShared.isNumeric(mrTemp.getIdNo())) {
                    throw new DmException(0, "AADHAAR No.. should be numeric.");
                }
            }

            SaleBillReturn_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="Check for SaleBill item change/cancellation">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getSbItemChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM saleBillDetail sbd " + DmShared.xNull(mrTemp.getSbItemChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Sale Bill has been changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "SELECT isCancelled FROM saleBill sb where sb.docId = '" + DmShared.xNull(mrTemp.getDocIdSaleBill()) + "'";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                        throw new DmException(0, "Sale Bill (Bill No. : " + mrTemp.getRecIdSaleBill() + ") has been Cancelled, return is not allowed.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="CHK For Incorrect Batch">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getInCorrectSelectedBatchSql()).equalsIgnoreCase("")) {
//                sqlQuery = " SELECT STUFF((SELECT char(10)+','+ vib.itemName+'-'+vib.batchNo AS [text()]"
//                        + " FROM (" + mrTemp.getInCorrectSelectedBatchSql() + ") vIb "
//                        + " LEFT JOIN itemBatch ib  ON vIb.ibCode = ib.code  AND vIb.itemCode = ib.itemCode "
//                        + " WHERE vIb.ibCode is Not Null and IB.itemCode IS NULL"
//                        + " FOR XML PATH('')), 1,2, '') "
//                        + "as incorrectMedicineBatches ";
                sqlQuery = " SELECT STUFF((SELECT char(10)+','+ vib.itemName+'-'+vib.batchNo AS [text()]"
                        + " FROM (" + mrTemp.getInCorrectSelectedBatchSql() + ") vIb "
                        + " LEFT JOIN itemBatch ib  ON vIb.ibCode = ib.code  AND vIb.itemCode = ib.itemCode "
                        + " WHERE vIb.ibCode is Not Null and IB.itemCode IS NULL"
                        + " FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,2,'') "
                        + "as incorrectMedicineBatches ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("incorrectMedicineBatches")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Selected medicines with batches are invalid : " + DmShared.xNull(rsTemp.getString("incorrectMedicineBatches")).trim());
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="CHK For Incorrect Barcode">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getInCorrectSelectedBarcodeSql()).trim().equalsIgnoreCase("")) {
                sqlQuery = " SELECT STUFF((SELECT char(10)+','+ vib.itemName + '-' + vib.batchNo + '-' + vib.barcode AS [text()]"
                        + " FROM (" + mrTemp.getInCorrectSelectedBarcodeSql() + ") vIb "
                        + " LEFT JOIN itemBatch ib  ON vIb.ibCode = ib.code AND vIb.itemCode = ib.itemCode AND ISNULL(vIb.barcode,'') = ISNULL(ib.barcode,'') "
                        + " WHERE vIb.ibCode is Not Null and IB.itemCode IS NULL"
                        + " FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,2,'') "
                        + " as incorrectMedicineBarcodes ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("incorrectMedicineBarcodes")).trim().equalsIgnoreCase("")) {
                        throw new DmException(0, "Selected medicines with barcodes are invalid : " + DmShared.xNull(rsTemp.getString("incorrectMedicineBarcodes")).trim());
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            //=======================================Sale Return Validation with HIS=================================
            if (DmShared.xNull(mrTemp.getTypeCode()).trim().equalsIgnoreCase(pvc.IPD) && DmShared.iNull(mrTemp.getSaleBillPaymentSourceCode()).intValue() == PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) {

                cnTempHsp = DmServer.getConnection(STServer.getSsnParentDateBase(this));

                String chkRegDocidInDischarge = DmServer.executeScalar("select regDocid from patientDischarge where regDocid=" + DmShared.iNull(mrTemp.getRegDocid()).toString() + "", cnTempHsp);
                if (DmShared.iNull(chkRegDocidInDischarge).longValue() == 0 && DmShared.xNull(mrTemp.getSaleReturnVTypeShortName()).equalsIgnoreCase("PHCSR")) {
                    throw new DmException(0, "Cannot refund in cash for credit Bill while patient is Admint.");
                }

                if (DmShared.iNull(chkRegDocidInDischarge).longValue() != 0 && DmShared.xNull(mrTemp.getSaleReturnVTypeShortName()).equalsIgnoreCase("PHBRT")) {
                    throw new DmException(0, "Cannot refund in credit, Patient has been Discharged");
                }

                String chkIsTPA = DmServer.executeScalar("select om.isTPA from patientRegistration as pr\n"
                        + "left join organizationMast om on om.subCode=pr.organizationCode\n"
                        + "where pr.regDocId=" + DmShared.iNull(mrTemp.getRegDocid()).toString() + "", cnTempHsp);

                if (DmShared.iNull(chkIsTPA).intValue() > 0 && DmShared.xNull(mrTemp.getSaleReturnVTypeShortName()).equalsIgnoreCase("PHCSR")) {
                    throw new DmException(0, "Cannot refund in cash for credit Bill while patient is TPA.");
                }

                cnTempHsp.close();
            }

            for (GrdMedicine_Mdl detail_Mdl : mrTemp.getGrdMediDetails()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (DmShared.convertDate(DmShared.dNull(detail_Mdl.getExpDate())).getTime() < DmShared.convertDate(DmShared.dNull(DmServer.getServerDateTime()[0])).getTime()) {
                        throw new DmException(0, "Medicine" + detail_Mdl.getSharedKeyFieldName() + "has been expired, " + "Can't be Return");
                    }
                }
            }

            //=======================================Sale Return Validation with HIS End=================================

            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select isCancelled, site_Code, v_Prefix from saleBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                    throw new DmException(0, "Record is discarded. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(siteCode).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(voucherPrefix).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdSaleBillReturn(DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdSaleBillReturn(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdSaleBillReturn().getRecIdValue()));
            } else {
                DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdSaleBillReturn(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (DmShared.xNull(mrTemp.getTypeCode()).trim().equalsIgnoreCase(pvc.IPD)) {

                String patientLMCode = DmServer.executeScalar("select top 1 code from ledgerMast where regDocId_HIS=" + mrTemp.getRegDocid() + "", cnTemp);

                if (DmShared.xNull(patientLMCode).equalsIgnoreCase("")) {
                    LedgerMaster_Mdl ledgerMdl = new LedgerMaster_Mdl();
                    ledgerMdl.setSharedKeyFieldCode(mrTemp.getLmCode());
                    ledgerMdl.setSharedKeyFieldName(mrTemp.getLmName());
                    ledgerMdl.setDisplayName(mrTemp.getPatientName());
                    ledgerMdl.setAccountCode(mrTemp.getSubCode());
                    ledgerMdl.setAddress(mrTemp.getAddress());
                    ledgerMdl.setCityCode(mrTemp.getCityCode());
                    ledgerMdl.setCityName(mrTemp.getCityName());
                    ledgerMdl.setIdTypeCode(mrTemp.getIdTypeCode());
                    ledgerMdl.setIdTypeName(mrTemp.getIdTypeName());
                    ledgerMdl.setIdNo(mrTemp.getIdNo());
                    ledgerMdl.setRegDocId(mrTemp.getRegDocid());
                    ledgerMdl.setRegType(mrTemp.getTypeCode());
                    ledgerMdl.setMobile(mrTemp.getMobile());

                    ledgerMdl.setManualCode(mrTemp.getRegRecID() + ":I");

                    LedgerMasterServer.saveRecord(ledgerMdl, cnTemp, this);

                    mrTemp.setLmCode(ledgerMdl.getSharedKeyFieldCode());
                } else {
                    mrTemp.setLmCode(patientLMCode);
                }

            } else {
                LedgerMaster_Mdl ledgerMdl = new LedgerMaster_Mdl();
                ledgerMdl.setSharedKeyFieldCode(mrTemp.getLmCode());
                ledgerMdl.setSharedKeyFieldName(mrTemp.getLmName());
                ledgerMdl.setDisplayName(mrTemp.getPatientName());
                ledgerMdl.setAccountCode(mrTemp.getSubCode());
                ledgerMdl.setAddress(mrTemp.getAddress());
                ledgerMdl.setCityCode(mrTemp.getCityCode());
                ledgerMdl.setCityName(mrTemp.getCityName());
                ledgerMdl.setIdTypeCode(mrTemp.getIdTypeCode());
                ledgerMdl.setIdTypeName(mrTemp.getIdTypeName());
                ledgerMdl.setIdNo(mrTemp.getIdNo());
                ledgerMdl.setRegDocId(mrTemp.getRegDocid());
                ledgerMdl.setRegType(mrTemp.getTypeCode());
                ledgerMdl.setMobile(mrTemp.getMobile());
                if (DmShared.iNull(mrTemp.getLmCode()).longValue() == 0) {
                    if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0 && DmShared.xNull(mrTemp.getPartyManualCode()).equalsIgnoreCase("")) {
                        ledgerMdl.setManualCode(mrTemp.getSharedKeyFieldName());
                    } else {
                        if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0) {
                            ledgerMdl.setManualCode(mrTemp.getPartyManualCode());
                        } else {
                            ledgerMdl.setManualCode(mrTemp.getRegRecID());
                        }
                    }
                } else {
                    ledgerMdl.setManualCode(mrTemp.getLmManualCode());
                }
                LedgerMasterServer.saveRecord(ledgerMdl, cnTemp, this);

                mrTemp.setLmCode(ledgerMdl.getSharedKeyFieldCode());

            }

            //<editor-fold defaultstate="collapsed" desc="IS ITEM SALE EXISTS">
            sqlQuery = "SELECT env.pharmacyAllowReturnWithoutSale "
                    + " FROM enviro as env "
                    + " Where env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                allowReturnWithoutSale = DmShared.iNull(rsTemp.getString("pharmacyAllowReturnWithoutSale")).intValue() == 1 ? true : false;
            }
            psTemp.close();
            rsTemp.close();

            if (allowReturnWithoutSale == false) {
                for (GrdMedicine_Mdl mdl : mrTemp.getGrdMediDetails()) {
                    if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                        if (DmShared.iNull(mrTemp.getRegDocid()).longValue() != 0 || DmShared.iNull(mrTemp.getUhidDocid()).longValue() != 0 || DmShared.iNull(mrTemp.getLmCode()).longValue() != 0) {
                            saleQty = checkItemSale(mdl.getSharedKeyFieldCode(), mdl.getIbCode(), mrTemp.getRegDocid(), mrTemp.getUhidDocid(), mrTemp.getLmCode(), mdl.getPrvQuantity(), mdl.getPrvFreeQuantity(), cnTemp);
                            if (DmShared.vNull(saleQty).doubleValue() > 0) {
                                if ((DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQty()).doubleValue()) > DmShared.vNull(saleQty).doubleValue()) {
                                    throw new DmException(0, mdl.getSharedKeyFieldName() + " with Batch No. '" + mdl.getBatchNo() + "' has Return Quantity : " + (DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQty()).doubleValue()) + " more than Sale Quantity :  " + saleQty);
                                }
                            } else {
                                throw new DmException(0, mdl.getSharedKeyFieldName() + " with Batch No. '" + mdl.getBatchNo() + "', never sold to/already returned by, " + mrTemp.getPatientName());
                            }
                        }
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Chk change of saleBillReturn">
            sqlQuery = "SELECT u_EntDt_LatestLine FROM saleBill WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(mrTemp.getUserEntDtLatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                        throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
//</editor-fold>

            if (!DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase("PHCSR")) {
                if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0 && DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
                    SaleBill_Mdl saleBill_Mdl = new SaleBill_Mdl();
                    saleBill_Mdl.setSharedKeyFieldCode(mrTemp.getDocIdSaleBill());
                    CardPaymentPopup_Mdl cardPaymentPopup_SaleBill_Mdl = CardPaymentPopup_Server.getCardListViaDocId(DmShared.xNull(mrTemp.getDocIdSaleBill()), cnTemp);
                    saleBill_Mdl.getCardPayment().getGrdCardPaymentList().addAll(cardPaymentPopup_SaleBill_Mdl.getGrdCardPaymentList());
                    saleBill_Mdl.setCardPayment(cardPaymentPopup_SaleBill_Mdl);

                    if (saleBill_Mdl.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION
                            || saleBill_Mdl.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {

                        sqlQuery = "SELECT COUNT(*) cnt "
                                + " FROM patientServiceDetail psd "
                                + " WHERE psd.docid = :docId "
                                + " AND psd.sbDocId IS NOT NULL "
                                + " AND psd.sbDocId <> psd.regDocId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdSaleBill()).toString());
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                                throw new DmException(0, "Patient Bill is generated, can't save this entry!");
                            }
                        }
                        rsTemp.close();
                        psTemp.close();

                        STServer.checkBillApproval(mrTemp.getRegDocid(), cnTemp);
                    }
                }
            }

            //<editor-fold defaultstate="collapsed" desc="PAYMENT CASE">
            if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION
                    || mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {

                if (DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase("")) {
                    if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                        throw new UnsupportedOperationException("If Payment Mode is Credit to Room then IPD / Day Care / Emergency Type is Mandatory.");
                    } else {
                        throw new UnsupportedOperationException("If Payment Mode is Credit to Organization then OPD Type is Mandatory.");
                    }

                } else {
                    if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0) {
                        if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                            throw new UnsupportedOperationException("If Payment Mode is Credit to Room then IPD No. / Day Care No./ Emergency No. is Mandatory.");
                        } else {
                            throw new UnsupportedOperationException("If Payment Mode is Credit to Organization then OPD No. is Mandatory.");
                        }
                    } else {
                        if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                            if (!(DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.IPD) || DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.DAY_CARE) || DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.EMERGENCY))) {
                                throw new UnsupportedOperationException("When we use card type Credit To Room then Type should be IPD / Day Care / Emergency and No. will Be IPD No. / Day Care No. / Emergency No..");
                            }
                        } else {
                            if (!DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.OPD) && !DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.PAY_CLINIC)) {
                                throw new UnsupportedOperationException("When we use card type Credit To Room then Type should be OPD / Day Care and No. will Be OPD No. / Day Care No..");
                            }
                        }
                    }
                }

                sqlQuery = "SELECT COUNT(*) cnt "
                        + " FROM patientServiceDetail psd "
                        + " WHERE psd.docid = :docId "
                        + " AND psd.sbDocId IS NOT NULL "
                        + " AND psd.sbDocId <> psd.regDocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        throw new DmException(0, "Patient Bill is generated, can't save this entry!");
                    }
                }
                rsTemp.close();
                psTemp.close();

                STServer.checkBillApproval(mrTemp.getRegDocid(), cnTemp);

                if (mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList() != null && mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList().size() > 0) {
                    if (DmShared.vNull(mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList().get(0).getAmount()).doubleValue() != 0) {
                        throw new UnsupportedOperationException("In case of CTR / CTO you have to settle full bill amount.");
                    }
                }
                if (DmShared.iNull(mrTemp.getDefaultPharmacyServiceCode()).longValue() == 0) {
                    throw new UnsupportedOperationException("Please define Pharmacy Service in Environment Settings.");
                }
                creditFlag = true;
            }

            if (creditFlag || DmShared.xNull(mrTemp.getFlagCardPayment()).trim().equalsIgnoreCase("1")) {
                CardPaymentPopup_Server.chkSave(true, mrTemp.getCardPaymentPopupNetAmount(), mrTemp.getSharedKeyFieldCode(), mrTemp.getCardPaymentSaleBillReturn(), cnTemp, true, true, true);
            }
            CardPaymentPopup_Server.delete(mrTemp.getSharedKeyFieldCode(), cnTemp);

//</editor-fold>

            /*====================================< Check whether posted (via EOD) or not >====================================*/
            STServer.checkEOD("saleBillDetail", "docId", mrTemp.getSharedKeyFieldCode(), cnTemp);

            if (blnAdd) {
                //<editor-fold defaultstate="collapsed" desc="INSERT">
                sqlQuery = " insert into saleBill (docId, v_Type, v_No, recId, v_Prefix, v_Date,v_Time, regDocId, "
                        + "     departmentCode, consultantCode, remark, u_EntDt, preparedDate, preparedBy, site_Code, oh_Amt_Gross, oh_Amt_Net, oh_Amt_BAMT, "
                        + "     oh_Amt_RoundOff, oh_Amt_CGST, oh_Amt_SGST, oh_Amt_Taxable, oh_Amt_Discount, recIdPrefix, oh_Amt_UGST,"
                        + "     uhid, idTypeCode, idNo, mobile,u_EntDt_LatestLine,refNo,refDate,partyType,lmCode,subCode,consignee,placeOfSupply,gstReasonCode,oh_Amt_Discount2, oh_Amt_LandedValue, paymentSource,regDocId_HIS,regNo_HIS,uhidNo_HIS,siteCode_HIS,regType_HIS)"
                        + " values (:docId, :vType, :vNo, :recId, :vPrefix, :vDate,:v_Time, :regDocId, :departmentCode,"
                        + "      :consultantCode, :remark, :uEntDt, :uEntDt, :userName, :siteCode, :ohAmtGross, :ohAmtNet, :ohAmtBAMT, :oh_Amt_RoundOff, "
                        + "      :ohAmtCGST, :ohAmtSGST, :ohAmtTaxable, :ohAmtDiscount, :recIdPrefix, :ohAmtUGST,"
                        + "      :uhid, :idTypeCode, :idNo, :mobile,:uEntDt,:refNo,:refDate,:partyType,:lmCode,:subCode,:consignee,:placeOfSupply,:gstReasonCode,:oh_Amt_Discount2, :oh_Amt_LandedValue, :paymentSource,:regDocId_HIS,:regNo_HIS,:uhidNo_HIS,:siteCode_HIS,:regType_HIS)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTemp.getGstReasonCode()));
                psTemp.setString("vNo", DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                psTemp.setString("vPrefix", voucherPrefix);

//                psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocid()));
                if (DmShared.xNull(mrTemp.getTypeCode()).trim().equalsIgnoreCase(pvc.IPD)) {
                    psTemp.setString("regDocId", DmShared.foriegnKey(""));

                    psTemp.setString("regDocId_HIS", DmShared.foriegnKey(mrTemp.getRegDocid()));
                    psTemp.setString("regNo_HIS", DmShared.xNull(mrTemp.getRegRecID()));
                    psTemp.setString("uhidNo_HIS", DmShared.xNull(mrTemp.getUhidRecId()));
                    psTemp.setString("siteCode_HIS", DmShared.xNull(mrTemp.getSiteCode_HIS()));
                    psTemp.setString("regType_HIS", DmShared.xNull(mrTemp.getTypeCode()));

                } else {
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocid()));
                    psTemp.setString("regDocId_HIS", DmShared.foriegnKey(""));

                    psTemp.setString("regNo_HIS", DmShared.foriegnKey(""));
                    psTemp.setString("uhidNo_HIS", DmShared.foriegnKey(""));

                    psTemp.setString("siteCode_HIS", DmShared.foriegnKey(""));

                    psTemp.setString("regType_HIS", DmShared.foriegnKey(""));
                }

                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("uhid", DmShared.foriegnKey(mrTemp.getUhidDocid()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("userName", DmShared.xNull(userName));
                psTemp.setString("siteCode", DmShared.xNull(siteCode));

                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyType()));
                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));

                psTemp.setString("recIdPrefix", mrTemp.getRecIdSaleBillReturn().getPrefix());
                psTemp.setString("ohAmtGross", DmServer.vNull(mrTemp.getGrossAmt(), "0.00"));
                psTemp.setString("ohAmtDiscount", DmServer.vNull(mrTemp.getDiscountAmt(), "0.00"));
                psTemp.setString("oh_Amt_Discount2", DmServer.vNull(mrTemp.getDiscountAmt2(), "0.00"));
                psTemp.setString("ohAmtCGST", DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                psTemp.setString("ohAmtSGST", DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                psTemp.setString("ohAmtUGST", DmServer.vNull(mrTemp.getUgstAmt(), "0.00"));
                psTemp.setString("ohAmtTaxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("ohAmtNet", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("ohAmtBAMT", DmServer.vNull(mrTemp.getBillAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                if (!saveCardInfo) {
                    psTemp.setString("paymentSource", DmShared.iNull(PaymentSource.NO_PAYMENT.getEnumValue()).toString());
                } else {
                    psTemp.setString("paymentSource", DmShared.iNull(mrTemp.getCardPaymentSaleBillReturn().getPaymentSource().getEnumValue()).toString());
                }
//                if (DmShared.iNull(mrTemp.getCardPaymentSaleBillReturn().getPaymentSource().getEnumValue()).intValue() != 0) {
//                    if (DmShared.iNull(mrTemp.getCardPaymentSaleBillReturn().getPaymentSource().getEnumValue()).intValue() != 0) {
//                        psTemp.setString("paymentSource", DmShared.iNull(mrTemp.getCardPaymentSaleBillReturn().getPaymentSource().getEnumValue()).toString());
//                    } else {
//                        psTemp.setString("paymentSource", DmShared.iNull(PaymentSource.NO_PAYMENT.getEnumValue()).toString());
//                    }
//                } else {
//                    psTemp.setString("paymentSource", DmShared.iNull(PaymentSource.NO_PAYMENT.getEnumValue()).toString());
//                }

//</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="UPDATE">
                sqlQuery = "UPDATE saleBill SET "
                        + " v_Date = :vDate, "
                        + " u_EntDt = :uEntDt, "
                        + " u_EntDt_LatestLine= :uEntDt ,"
                        + " departmentCode = :departmentCode, consultantCode = :consultantCode,"
                        + " lmCode= :lmCode ,"
                        + " subCode= :subCode ,"
                        + " consignee= :consignee ,"
                        + " placeOfSupply= :placeOfSupply ,"
                        + " remark=:remark,"
                        + " idTypeCode=:idTypeCode,"
                        + " idNo=:idNo,"
                        + " mobile=:mobile,"
                        + " oh_Amt_Gross=:oh_Amt_Gross,"
                        + " oh_Amt_Discount=:oh_Amt_Discount,"
                        + " oh_Amt_Discount2=:oh_Amt_Discount2,"
                        + " oh_Amt_CGST=:oh_Amt_CGST,"
                        + " oh_Amt_SGST=:oh_Amt_SGST,"
                        + " oh_Amt_UGST=:oh_Amt_UGST,"
                        + " oh_Amt_Taxable=:oh_Amt_Taxable,"
                        + " oh_Amt_Net=:oh_Amt_Net,"
                        + " oh_Amt_LandedValue=:oh_Amt_LandedValue,"
                        + " oh_Amt_BAMT=:oh_Amt_BAMT,"
                        + " oh_Amt_RoundOff=:oh_Amt_RoundOff,"
                        + " refNo=:refNo,"
                        + " refDate=:refDate,"
                        + " gstReasonCode=:gstReasonCode,"
                        + " u_Name = :userName, "
                        + " paymentSource= :paymentSource "
                        + " WHERE DocID = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTemp.getGstReasonCode()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTemp.getDiscountAmt(), "0.00"));
                psTemp.setString("oh_Amt_Discount2", DmServer.vNull(mrTemp.getDiscountAmt2(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTemp.getUgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("oh_Amt_BAMT", DmServer.vNull(mrTemp.getBillAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));

                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                if (!saveCardInfo) {
                    psTemp.setString("paymentSource", DmShared.iNull(PaymentSource.NO_PAYMENT.getEnumValue()).toString());
                } else {
                    psTemp.setString("paymentSource", DmShared.iNull(mrTemp.getCardPaymentSaleBillReturn().getPaymentSource().getEnumValue()).toString());
                }

                //</editor-fold>
            }
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Validate Removed Received Items">
            if (!blnAdd) {
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), StockEntryType.OTHER, cnTemp, this);
            }
            //</editor-fold>

            Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp, true);
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Manage SALEBILL (Returned Qty)(Delete) >>====================================*/
            manageReturnedQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), true, cnTemp);

            //<editor-fold defaultstate="collapsed" desc="INSERT IN SALEBILL DETAIL , STOCK , GST TRANS">
            sqlQuery = "Delete From saleBillDetail Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            int vSNo = 0;
            for (GrdMedicine_Mdl detail_Mdl : mrTemp.getGrdMediDetails()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    detail_Mdl.setvSno(String.valueOf(++vSNo));
                    medicineMdl = detail_Mdl.getMedicineMdl();
                    sqlQuery = "insert into saleBillDetail (docId, v_SNo, itemCode, hsnCode, saleUnitCode, quantity, "
                            + "		oh_at_Discount, oh_Amt_Discount, oh_at_Discount2, oh_Amt_Discount2, oh_Amt_Net, oh_Amt_CGST, oh_at_CGST, oh_Amt_SGST, oh_at_SGST, oh_at_UGST, "
                            + "         oh_Amt_UGST, rate, oh_Amt_Taxable, itemGSTNature, mrp, skuQuantity, oh_Ac_Sales, oh_Ac_CGST, "
                            + "         oh_Ac_UGST, oh_Ac_SGST, oh_Amt_Gross, batchNo, manufacturingDate, expiryDate, "
                            + "         ibCode, skuMrp, skuRate, skuQtyForRate,sbDocId,sbV_SNo,freeQty,landedSkuValue, barcode,grossPurchaseRate)"
                            + "         values (:docId, :v_sNo, :itemCode, :hsnCode, :saleUnitCode, :quantity,"
                            + "		:oh_at_Discount, :oh_Amt_Discount, :oh_at_Discount2, :oh_Amt_Discount2, :oh_Amt_Net, :oh_Amt_CGST, :oh_at_CGST, :oh_Amt_SGST, :oh_at_SGST, "
                            + "		:oh_at_UGST, :oh_Amt_UGST, :rate, :oh_Amt_Taxable, :itemGSTNature, :mrp, :skuQuantity, :oh_Ac_Sales, "
                            + "         :oh_Ac_CGST, :oh_Ac_UGST, :oh_Ac_SGST, :oh_Amt_Gross, :batchNo, :manufacturingDate, :expiryDate, :ibCode, :skuMrp, "
                            + "         :skuRate, :skuQtyForRate,:sbDocId,:sbV_SNo,:freeQty, :landedSkuValue, :barcode,:grossPurchaseRate)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSno()).toString());
                    psTemp.setString("itemCode", medicineMdl.getSharedKeyFieldCode());
                    psTemp.setString("hsnCode", DmShared.foriegnKey(medicineMdl.getHsnCode()));
                    psTemp.setString("itemGSTNature", DmShared.xNull(medicineMdl.getItemGstNature()));
                    psTemp.setString("saleUnitCode", DmShared.xNull(medicineMdl.getSaleUnitCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("skuQuantity", DmServer.vNull(detail_Mdl.getQuantitySKU()));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(medicineMdl.getPurchaseUnitConversionRate()));
                    psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQty()));

                    psTemp.setString("oh_Ac_SGST", DmShared.foriegnKey(detail_Mdl.getSgstAcCode()));
                    psTemp.setString("oh_Ac_UGST", DmShared.foriegnKey(detail_Mdl.getUgstAcCode()));
                    psTemp.setString("oh_Ac_CGST", DmShared.foriegnKey(detail_Mdl.getCgstAcCode()));
                    psTemp.setString("oh_Ac_Sales", DmShared.foriegnKey(detail_Mdl.getSaleAcCode()));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    psTemp.setString("oh_at_Discount", DmServer.vNull(medicineMdl.getDiscountPer(), "0.00"));
                    psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                    psTemp.setString("oh_at_Discount2", DmServer.vNull(detail_Mdl.getDiscountPer2(), "0.00"));
                    psTemp.setString("oh_Amt_Discount2", DmServer.vNull(detail_Mdl.getDiscount2(), "0.00"));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getNetAmt(), "0.00"));
                    psTemp.setString("landedSkuValue", DmServer.vNull(detail_Mdl.getLandedSkuValue(), "0.00"));
                    psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgstAmt(), "0.00"));
                    psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgstAmt(), "0.00"));
                    psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgstAmt(), "0.00"));
                    psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));

                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("sbDocId", DmShared.foriegnKey(detail_Mdl.getSbDocId()));
                    psTemp.setString("sbV_SNo", DmShared.foriegnKey(detail_Mdl.getSbV_Sno()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getMfgDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpDate()));

                    psTemp.setString("grossPurchaseRate", DmServer.vNull(0 - DmShared.vNull(detail_Mdl.getGrossPurchaseRate()), "0.00"));

                    psTemp.executeUpdate();
                    psTemp.close();

                    /*----------------------------------------------------------Stock Saving--------------------------------------------------------------*/
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setSharedKeyFieldName(detail_Mdl.getvSno());
                    stock_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdSaleBillReturn().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setDepartmentCode(DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                    stock_Mdl.setStockContraSubCode(DmShared.foriegnKey(mrTemp.getControlAccount())); //Pharmacy Control A/c
                    stock_Mdl.setRemark("Patient: " + DmShared.xNull(mrTemp.getPatientName()));

                    stock_Mdl.setItemCode(DmShared.xNull(medicineMdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(DmShared.xNull(detail_Mdl.getIbCode()));
                    stock_Mdl.setIsuQty(DmServer.vNull(0 - (DmShared.vNull(detail_Mdl.getQuantitySKU()).doubleValue())));
                    stock_Mdl.setSkuSaleValue(DmServer.vNull(0 - DmShared.vNull(detail_Mdl.getAmount()).doubleValue(), "0.00"));
                    stock_Mdl.setLandedSkuSaleValue(DmServer.vNull(0 - DmShared.vNull(detail_Mdl.getLandedSkuValue()).doubleValue(), "0.00"));

                    stock_Mdl.setGrossPurchaseRate(DmServer.vNull((0 - (DmShared.vNull(detail_Mdl.getQuantitySKU()) * DmShared.vNull(detail_Mdl.getGrossPurchaseRate()))), "0.00"));

                    Stock_Server.saveStock(stock_Mdl, StockEntryType.OTHER, cnTemp);

                    /*===================================================================================================================*/
                    GstTrans_Mdl gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(mrTemp.getSharedKeyFieldCode());
                    gstTrans_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    gstTrans_Mdl.setV_Sno(detail_Mdl.getvSno());
                    gstTrans_Mdl.setV_Type(mrTemp.getVoucherTypeCode());
                    gstTrans_Mdl.setDepartmentCode(mrTemp.getDepartmentCode());
                    gstTrans_Mdl.setGstUnitCode(medicineMdl.getSaleGSTUnit());
                    gstTrans_Mdl.setHsnCode(medicineMdl.getHsnCode());

                    gstTrans_Mdl.setPartyCode(mrTemp.getSubCode());
                    gstTrans_Mdl.setConsignee(mrTemp.getSubCode());
                    gstTrans_Mdl.setPartyType(mrTemp.getPartyType());
                    gstTrans_Mdl.setPlaceOfSupply(mrTemp.getPlaceOfSupply());

                    gstTrans_Mdl.setInvoiceCGST(DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceSGST(DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceUGST(DmServer.vNull(mrTemp.getUgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceValue(DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                    gstTrans_Mdl.setItemCgstAmount(DmServer.vNull(detail_Mdl.getCgstAmt(), "0.00"));
                    gstTrans_Mdl.setItemCgstRate(DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                    gstTrans_Mdl.setItemSgstRate(DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                    gstTrans_Mdl.setItemSgstAmout(DmServer.vNull(detail_Mdl.getSgstAmt(), "0.00"));
                    gstTrans_Mdl.setItemUgstRate(DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                    gstTrans_Mdl.setItemUgstAmount(DmServer.vNull(detail_Mdl.getUgstAmt(), "0.00"));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(medicineMdl.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(medicineMdl.getItemGstNature()));
                    gstTrans_Mdl.setItemGrossAmount(DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    gstTrans_Mdl.setItemTaxableAmount(DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                    gstTrans_Mdl.setItemNetAmount(DmServer.vNull(detail_Mdl.getNetAmt(), "0.00"));
                    gstTrans_Mdl.setQty(DmServer.vNull(DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() + DmShared.vNull(detail_Mdl.getFreeQty()).doubleValue()));
                    gstTrans_Mdl.setRecID(mrTemp.getSharedKeyFieldName());
                    gstTrans_Mdl.setRecIdPrefix(mrTemp.getRecIdSaleBillReturn().getPrefix());
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                    gstTrans_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTemp.getGstReasonCode()));

                    gstTrans_Mdl.setRefDocId(DmShared.xNull(mrTemp.getDocIdSaleBill()));
                    gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getRefNo()));
                    gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getRefDate()));
                    gstTrans_Mdl.setRefV_Type(DmShared.xNull(mrTemp.getV_TypeSaleBill()));
                    gstTrans_Mdl.setRefDocValue(DmShared.xNull(mrTemp.getNet_AmtSaleBill()));
                    gstTrans_Mdl.setIsTaxInclusive("1");
                    gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()));
                    gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
                    gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                    gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                    gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                    gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.OUTPUT.toInt()));

                    Gst_Server.saveGstTrans(gstTrans_Mdl, false, cnTemp, this);

                }

            }
//</editor-fold>
            /*==============================<< Manage saleBILL (Returned Qty)(INSERT) >>==============================*/
            manageReturnedQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), false, cnTemp);
            //<editor-fold defaultstate="collapsed" desc="ROUND OFF AMOUNT">
            if (DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue() != 0) {
                sqlQuery = " select   "
                        + "	ohm.itemCode,im.name as itemName,im.itemGSTNature,im.hsnCode, "
                        + "	im.sku,um.gstUnitCode  "
                        + "	from overHeadMast ohm "
                        + "	left join itemMast im on im.code = ohm.itemCode "
                        + "	LEFT JOIN unitMast um ON um.code = im.saleUnit "
                        + "	where ohm.manualCode = 'ROFF' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("itemCode")).longValue() == 0) {
                        throw new DmException(0, "Default Round Off medicine not fount.");
                    }

                    /*====================================< Insert into saleBillDetail >====================================*/
                    sqlQuery = "INSERT INTO saleBillDetail (docId, v_SNo, itemCode, hsnCode, quantity, oh_Amt_Gross, oh_Amt_Net, rate, isFooter,"
                            + "      oh_Amt_Taxable, itemGSTNature, oh_Ac_Sales)"
                            + "VALUES (:docId,:v_sNo,:itemCode, :hsnCode, :quantity,:roffAmount,:roffAmount,:rate, 1,:roffAmount,:itemGSTNature, :roffAcCode) ";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp1.setString("v_sNo", DmShared.iNull(++vSNo).toString());
                    psTemp1.setString("itemCode", DmShared.foriegnKey(DmShared.xNull(rsTemp.getString("itemCode"))));
                    psTemp1.setString("hsnCode", DmShared.foriegnKey(DmShared.xNull(rsTemp.getString("hsnCode"))));
                    psTemp1.setString("roffAcCode", DmShared.foriegnKey(DmShared.xNull(mrTemp.getRoundOffAcCode())));
                    psTemp1.setString("quantity", DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue() > 0 ? "1" : "-1");
                    psTemp1.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue()), "0.00"));
                    psTemp1.setString("roffAmount", DmServer.vNull(DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue(), "0.00"));
                    psTemp1.setString("itemGSTNature", DmShared.xNull(rsTemp.getString("itemGSTNature")));

                    psTemp1.executeUpdate();
                    psTemp1.close();

                    GstTrans_Mdl gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(mrTemp.getSharedKeyFieldCode());
                    gstTrans_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    gstTrans_Mdl.setV_Sno(DmShared.iNull(vSNo).toString());
                    gstTrans_Mdl.setV_Type(mrTemp.getVoucherTypeCode());
                    gstTrans_Mdl.setDepartmentCode(mrTemp.getDepartmentCode());
                    gstTrans_Mdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                    gstTrans_Mdl.setPartyCode(mrTemp.getSubCode());
                    gstTrans_Mdl.setConsignee(mrTemp.getSubCode());
                    gstTrans_Mdl.setPartyType(mrTemp.getPartyType());
                    gstTrans_Mdl.setPlaceOfSupply(mrTemp.getPlaceOfSupply());

                    gstTrans_Mdl.setInvoiceCGST(DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceSGST(DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceUGST(DmServer.vNull(mrTemp.getUgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceValue(DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                    gstTrans_Mdl.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                    gstTrans_Mdl.setItemGrossAmount(DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                    gstTrans_Mdl.setItemTaxableAmount(DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                    gstTrans_Mdl.setItemNetAmount(DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                    if (DmShared.vNull(mrTemp.getRoundOffAmt()).doubleValue() < 0) {
                        gstTrans_Mdl.setQty(DmShared.xNull("-1"));
                    } else {
                        gstTrans_Mdl.setQty(DmShared.xNull("1"));
                    }
                    gstTrans_Mdl.setRecID(mrTemp.getSharedKeyFieldName());
                    gstTrans_Mdl.setRecIdPrefix(mrTemp.getRecIdSaleBillReturn().getPrefix());
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                    gstTrans_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTemp.getGstReasonCode()));

                    gstTrans_Mdl.setRefDocId(DmShared.xNull(mrTemp.getDocIdSaleBill()));
                    gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getRefNo()));
                    gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getRefDate()));
                    gstTrans_Mdl.setRefV_Type(DmShared.xNull(mrTemp.getV_TypeSaleBill()));
                    gstTrans_Mdl.setRefDocValue(DmShared.xNull(mrTemp.getNet_AmtSaleBill()));
                    gstTrans_Mdl.setIsTaxInclusive("1");
                    gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()));
                    gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
                    gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                    gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                    gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                    gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.OUTPUT.toInt()));

                    Gst_Server.saveGstTrans(gstTrans_Mdl, false, cnTemp, this);

                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="SAVE in patient service">
//            if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0 && DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
            if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION
                        || mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {

                    cnTemp.setCatalog(STServer.getSsnParentDateBase(this));

                    if (blnAdd) {

                        sqlQuery = "INSERT INTO patientService(docId,regDocid, v_No, recId, v_Type, v_Date, v_Time, v_Prefix, site_Code, preparedBy, u_EntDt, transferDt,oh_Amt_Net,oh_Amt_Gross,oh_Amt_BAMT,oh_Amt_ROFF, recIdPrefix)"
                                + " VALUES (:docId,:regDocid, :v_No, :recId, :v_Type, :v_Date, :v_Time, :v_Prefix, :site_Code, :preparedBy, :uEntDt, :transferDt, :oh_Amt_Net,:oh_Amt_Gross,:oh_Amt_BAMT,:oh_Amt_ROFF, :recIdPrefix)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString().replaceFirst("1", "2"));
                        psTemp.setString("regDocid", DmShared.iNull(mrTemp.getRegDocid()).toString());
                        psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                        psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                        psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                        psTemp.setString("recIdPrefix", mrTemp.getRecIdSaleBillReturn().getPrefix());
                        psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                        psTemp.setString("v_Prefix", voucherPrefix);
                        psTemp.setString("site_Code", mrTemp.getSiteCode_HIS());
                        psTemp.setString("preparedBy", userName);
                        psTemp.setString("transferDt", null);
                        psTemp.setString("oh_Amt_Net", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), "0.00"));
                        psTemp.setString("oh_Amt_Gross", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), "0.00"));
                        psTemp.setString("oh_Amt_BAMT", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), "0.00"));
                        psTemp.setString("oh_Amt_ROFF", "0.00");
                    } else {
                        sqlQuery = "update patientService set "
                                + " v_Date = :v_Date, "
                                + " v_Time = :v_Time, "
                                + " u_Name = :u_Name, "
                                + " u_EntDt = :uEntDt, "
                                + " oh_Amt_Net = :oh_Amt_Net, "
                                + " oh_Amt_Gross = :oh_Amt_Gross, "
                                + " oh_Amt_BAMT = :oh_Amt_BAMT, "
                                + " oh_Amt_ROFF = :oh_Amt_ROFF, "
                                + " transferDt = :transferDt "
                                + " where docId = :docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString().replaceFirst("1", "2"));
                        psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                        psTemp.setString("u_Name", userName);
                        psTemp.setString("transferDt", null);
                        psTemp.setString("oh_Amt_Net", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), "0.00"));
                        psTemp.setString("oh_Amt_Gross", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), "0.00"));
                        psTemp.setString("oh_Amt_BAMT", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), "0.00"));
                        psTemp.setString("oh_Amt_ROFF", "0.00");
                    }
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "DELETE FROM patientServiceDetail WHERE docid = :docid";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString().replaceFirst("1", "2"));
                    psTemp.executeUpdate();
                    psTemp.close();

                    sqlQuery = "INSERT INTO patientServiceDetail (DocId, v_SNo, serviceCode, quantity, rate, grossAmount, discount, "
                            + " discountPer, oh_Amt_Gross,oh_Amt_Net, referenceValue,remark, v_Type, v_Date, regDocId, site_Code, "
                            + " discountReason, discountGivenBy,collectionBoyCode,roomCode,isOutPharmacy) "
                            + "VALUES (:docId, :vSNo, :service, :quantity, :rate, :grossAmount, :discount, :discountPer,:grossAmount,:grossAmount,:grossAmount, "
                            + ":remark, :vType, :vDate, :regDocId, :siteCode,:discountReason, "
                            + ":discountGiven,:collectionBoyCode,:roomCode,:isOutPharmacy)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString().replaceFirst("1", "2"));
                    psTemp.setString("vSNo", DmShared.iNull(pvc.HIDDEN_SERVICES_START_INDEX).toString());
//                    psTemp.setString("service", DmServer.getCodeViaManualCode("PRM", "ShortName", "patientServiceMast", "Code", cnTemp));
                    psTemp.setString("service", DmShared.foriegnKey(mrTemp.getDefaultPharmacyServiceCode()));
                    psTemp.setString("quantity", DmShared.vNull("-1").toString());
                    psTemp.setString("rate", DmShared.vNull(mrTemp.getNetAmount()).toString());
                    psTemp.setString("grossAmount", DmServer.vNull(0 - DmShared.vNull(mrTemp.getNetAmount()).doubleValue()));
                    psTemp.setString("discount", DmShared.vNull("0").toString());
                    psTemp.setString("discountPer", DmServer.vNull("0", "0.00"));
                    psTemp.setString("remark", DmShared.xNull(""));
                    psTemp.setString("discountReason", DmShared.xNull(""));
                    psTemp.setString("vType", DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocid()));
                    psTemp.setString("discountGiven", DmShared.foriegnKey(""));
                    psTemp.setString("collectionBoyCode", DmShared.foriegnKey(""));
                    psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSiteCode_HIS()));
                    psTemp.setString("roomCode", DmShared.foriegnKey(mrTemp.getRoomCode()));
                    psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("isOutPharmacy", "1");
                    psTemp.executeUpdate();
                    psTemp.close();

                    cnTemp.setCatalog(DmServer.getSsnDBName(this));
                }
            }

            //</editor-fold>
            sqlQuery = "Delete From patientReceipt Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            if (saveCardInfo) {
//<editor-fold defaultstate="collapsed" desc="SAVE IN PATIENT RECEIPT AND CARD PAYMENT">
                if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.PAYMENT
                        || mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.PARTIAL_PAYMENT) {

                    if (DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()).trim().equalsIgnoreCase("")) {
                        mrTemp.setRecIdRecipt(DmServer.getRecId("patientReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdRecipt(), DmShared.dNull(mrTemp.getVoucherDate()), DmShared.xNull(mrTemp.getCardPaymentSaleBillReturn().getReceiptTypeCode()), voucherPrefix, cnTemp, this, true));
                    }
                    if (!DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()).trim().equalsIgnoreCase("")) {
                        returnReciptAmt = 0;
                        for (GrdCardPayment_Mdl cardPayment_Mdl : mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList()) {
                            if (DmShared.vNull(cardPayment_Mdl.getAmount()).doubleValue() != 0) {
                                returnReciptAmt += DmShared.vNull(cardPayment_Mdl.getAmount());
                            }
                        }
                        //<editor-fold defaultstate="collapsed" desc="Insert into patientReceipt">
                        /*====================================< Insert into patientReceipt >====================================*/
                        sqlQuery = "Insert into patientReceipt (docId, v_Type, v_No,v_Time, recId, recIdPrefix, v_Prefix, v_Date, u_EntDt, preparedBy, site_Code, amount,refDocid,lmCode,regDocId_HIS) "
                                + " values (:docId, :v_Type, :v_No, :v_Time, :recId, :recIdPrefix, :v_Prefix, :v_Date, :uEntDt, :userName, :site_Code, :amount,:refDocid,:lmCode,:regDocId_HIS)";

                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
                            psTemp.setString("refDocid", DmShared.foriegnKey(mrTemp.getDocIdSaleBill()));
                        } else {
                            psTemp.setString("refDocid", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        }
                        psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getCardPaymentSaleBillReturn().getReceiptTypeCode()));
                        psTemp.setString("v_Time", DmServer.vNull(DmServer.getServerDateTime()[1], "0.00"));
                        psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdRecipt().getPrefix()));
                        psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()));
                        psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdRecipt().getCounter()).toString());
                        psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("v_Prefix", voucherPrefix);
                        psTemp.setString("site_Code", siteCode);
                        psTemp.setString("userName", userName);
                        psTemp.setString("amount", DmServer.vNull(returnReciptAmt, "0.00"));

                        //
                        if (DmShared.xNull(mrTemp.getTypeCode()).trim().equalsIgnoreCase(pvc.IPD)) {

                            psTemp.setString("regDocId_HIS", DmShared.foriegnKey(mrTemp.getRegDocid()));

                        } else {

                            psTemp.setString("regDocId_HIS", DmShared.foriegnKey(""));

                        }

                        //
                        psTemp.executeUpdate();
                        psTemp.close();

                        cardPaymentFlag = true;
                        //</editor-fold>
                    }
                }

                if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION
                        || mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {

                    cnTemp.setCatalog(STServer.getSsnParentDateBase(this));

                    CardPaymentPopup_Server.save(true, mrTemp.getCardPaymentSaleBillReturn(), mrTemp.getRegDocid(), mrTemp.getSharedKeyFieldCode().replaceFirst("1", "2"), mrTemp.getSharedKeyFieldName(), mrTemp.getVoucherTypeCode(), siteCode, mrTemp.getVoucherDate(), DmServer.getSsnUserName(this), "", cnTemp);

                    cnTemp.setCatalog(DmServer.getSsnDBName(this));

                } else {
                    CardPaymentPopup_Server.save(true, mrTemp.getCardPaymentSaleBillReturn(), mrTemp.getRegDocid(), mrTemp.getSharedKeyFieldCode(), mrTemp.getSharedKeyFieldName(), mrTemp.getVoucherTypeCode(), siteCode, mrTemp.getVoucherDate(), DmServer.getSsnUserName(this), mrTemp.getSharedKeyFieldCode(), cnTemp);
                }

//</editor-fold>
            }

            if (!DmShared.xNull(returnRequestDocID).toString().trim().equalsIgnoreCase("")) {
                sqlQuery = "update saleBillReturn set returnDocID=:docId where docid=:returnRequestDocID";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("returnRequestDocID", DmShared.iNull(returnRequestDocID).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (!blnAdd) {
                if (saveModifyReason) {
                    ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
                }
            }
            //<editor-fold defaultstate="collapsed" desc="CHK of excess return">

            if (cardPaymentFlag) {
//                if (STServer.getBalanceLedgerMast(mrTemp.getLmCode(), DmServer.getSsnSiteCode(this), cnTemp) > 0) {
//                    throw new UnsupportedOperationException("Cannot Return Excess Amount.");
//                }
//                double netReceivedAmount = STServer.getReceievedAmt(mrTemp.getLmCode(), cnTemp) + Math.abs(returnReciptAmt);
//                double excessAmount = (Math.abs(returnReciptAmt) - netReceivedAmount);
//                if (DmShared.vNull(excessAmount).doubleValue() > 0) {
//                    throw new UnsupportedOperationException("Cannot Return Excess Amount. Excess Amount : " + DmServer.vNull(excessAmount, "0.00"));
//                }
            }

//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="UPDATE SALE BILL ENTDT">
            sqlQuery = "UPDATE saleBill "
                    + " SET saleBill.u_EntDt_LatestLine = :uEntDt "
                    + " FROM ( "
                    + " 	SELECT sbd.sbDocid "
                    + " 	FROM saleBillDetail sbd "
                    + " 	WHERE sbd.docId = :docId "
                    + " 	AND sbd.sbDocId IS NOT NULL "
                    + " 	GROUP BY sbd.sbDocid "
                    + " 	) vSbd "
                    + " WHERE vSbd.sbDocId = saleBill.docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Check of sale bill Returnable item">
            sqlQuery = " SELECT COUNT(*) AS errCnt"
                    + " FROM saleBillDetail sbd "
                    + " INNER JOIN itemMast im ON im.code=sbd.itemCode "
                    + " WHERE sbd.docId = :docId"
                    + " AND im.isReturnableItem = 0"
                    + " AND sbd.isFooter = 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("errCnt")).intValue() > 0) {
                    throw new DmException(0, "Record contains non-returnable item.");
                }
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Check of CTO">
            if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
//                sqlQuery = "  select count(*) as errCnt from "
//                        + " patientLimitApproval pla "
//                        + " WHERE pla.docid=:regDocId "
//                        + " AND pla.nature=:nature ";
                sqlQuery = "  select count(*) as errCnt from "
                        + " patientRegistration pr "
                        + " WHERE pr.regDocId=:regDocId "
                        + " AND pr.paymentSource=:nature ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocid()).toString());
                psTemp.setString("nature", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("errCnt")).intValue() == 0) {
                        throw new DmException(0, "Credit To Organisation(CTO) cannot be done.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

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
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (cnTempHsp != null) {
                    cnTempHsp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode, ModifyReason_Mdl reason_Mdl, String regDocid, String sbDocId, String voucherShortName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);
        SaleBillReturn_Mdl mrTemp;
        String regDocid_HIS = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select regDocid_HIS,isCancelled, site_Code, v_Prefix from saleBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                regDocid_HIS = DmShared.xNull(rsTemp.getString("regDocid_HIS"));

                if (DmShared.iNull(rsTemp.getString("isCancelled")).intValue() == 1) {
                    throw new DmException(0, "Record is discarded. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(DmServer.getSsnSiteCode(this)).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Approval Check">
            if (!DmShared.xNull(voucherShortName).trim().equalsIgnoreCase("PHCSR")) {
                if (DmShared.iNull(regDocid).longValue() > 0) {
                    if (DmShared.iNull(sbDocId).longValue() > 0) {
                        SaleBill_Mdl saleBill_Mdl = new SaleBill_Mdl();
                        saleBill_Mdl.setSharedKeyFieldCode(sbDocId);
                        CardPaymentPopup_Mdl cardPaymentPopup_SaleBill_Mdl = CardPaymentPopup_Server.getCardListViaDocId(DmShared.xNull(sbDocId), cnTemp);
                        saleBill_Mdl.getCardPayment().getGrdCardPaymentList().addAll(cardPaymentPopup_SaleBill_Mdl.getGrdCardPaymentList());
                        saleBill_Mdl.setCardPayment(cardPaymentPopup_SaleBill_Mdl);
                        if (saleBill_Mdl.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION
                                || saleBill_Mdl.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {

                            sqlQuery = "SELECT COUNT(*) cnt "
                                    + " FROM patientServiceDetail psd "
                                    + " WHERE psd.docid = :docId "
                                    + " AND psd.sbDocId IS NOT NULL "
                                    + " AND psd.sbDocId <> psd.regDocId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(sbDocId).toString());
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                                    throw new DmException(0, "Patient Bill is generated, can't discard this entry!");
                                }
                            }
                            rsTemp.close();
                            psTemp.close();

                            STServer.checkBillApproval(regDocid, cnTemp);
                        }
                    }

                    mrTemp = new SaleBillReturn_Mdl();
                    mrTemp.setSharedKeyFieldCode(keyCode);
                    CardPaymentPopup_Mdl cardPaymentPopup_Mdl = CardPaymentPopup_Server.getCardListViaDocId(DmShared.xNull(keyCode), cnTemp);
                    mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList().addAll(cardPaymentPopup_Mdl.getGrdCardPaymentList());
                    mrTemp.setCardPaymentSaleBillReturn(cardPaymentPopup_Mdl);
                    if (mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION
                            || mrTemp.getCardPaymentSaleBillReturn().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {

                        sqlQuery = "SELECT COUNT(*) cnt "
                                + " FROM patientServiceDetail psd "
                                + " WHERE psd.docid = :docId "
                                + " AND psd.sbDocId IS NOT NULL "
                                + " AND psd.sbDocId <> psd.regDocId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                                throw new DmException(0, "Patient Bill is generated, can't discard this entry!");
                            }
                        }
                        rsTemp.close();
                        psTemp.close();

                        STServer.checkBillApproval(regDocid, cnTemp);
                    }
                }
            }

            //</editor-fold>

            /*====================================< Check whether posted (via EOD) or not >====================================*/
            STServer.checkEOD("saleBillDetail", "docId", keyCode, cnTemp);

            //<editor-fold defaultstate="collapsed" desc="UPDATE SALE BILL ENTDT">
            sqlQuery = "UPDATE saleBill "
                    + " SET saleBill.u_EntDt_LatestLine = :uEntDt "
                    + " FROM ( "
                    + " 	SELECT sbd.sbDocid "
                    + " 	FROM saleBillDetail sbd "
                    + " 	WHERE sbd.docId = :docId "
                    + " 	AND sbd.sbDocId IS NOT NULL "
                    + " 	GROUP BY sbd.sbDocid "
                    + " 	) vSbd "
                    + " WHERE vSbd.sbDocId = saleBill.docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("docId", keyCode);
            psTemp.executeUpdate();
            psTemp.close();
            //</editor-fold>


            /*==============================<< Update record in Sale Bill >>===============================*/
            manageReturnedQty(DmShared.iNull(keyCode).toString(), true, cnTemp);

            Stock_Server.deleteStock(keyCode, cnTemp);

            Gst_Server.deleteGstTrans(keyCode, cnTemp);

            sqlQuery = "update patientReceipt set isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            if (!regDocid_HIS.equalsIgnoreCase("")) {

                cnTemp.setCatalog(STServer.getSsnParentDateBase(this));

                sqlQuery = "update patientServiceDetail set isCancelled = 1 Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode.replaceFirst("1", "2"));
                psTemp.executeUpdate();

                sqlQuery = "update saleBill set isCancelled = 1 Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode.replaceFirst("1", "2"));
                psTemp.executeUpdate();

                cnTemp.setCatalog(DmServer.getSsnDBName(this));

            } else {
                sqlQuery = "update patientServiceDetail set isCancelled = 1 Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode);
                psTemp.executeUpdate();

                sqlQuery = "update saleBill set isCancelled = 1 Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode);
                psTemp.executeUpdate();
            }

            sqlQuery = "update saleBillReturn set returnDocID = null Where returnDocID= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            CardPaymentPopup_Server.cancel(keyCode, cnTemp);

            if (reason_Mdl != null) {
                ModifyReason_Server.save(reason_Mdl, keyCode, DmServer.getSsnUserName(this), cnTemp);
            }

            spBegin.commit();
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
    public DmResultPageing<List<Find_Mdl>> getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT mb.DocId, mb.recId AS billNo,"
                    + "    mb.v_Date, pr.recId AS regNo, pu.recId AS UHIDNo,"
                    + "    puvt.short_Name, mb.v_no, mb.recId, pbvt.description as saleBillReturnType, "
                    + "    sdm.name AS departmentName, lm.displayName as patientName, "
                    + "    sgcon.name AS consultantName,"
                    + "    CASE WHEN mb.isCancelled = 1 THEN 'Discarded' ELSE '' END as status, "
                    + "    CAST(mb.preparedDate AS DATE) as preparedDate, "
                    + "    CAST(REPLACE(LEFT(CONVERT(varchar, mb.preparedDate, 8),5),':','.') AS NUMERIC(18,2)) as preparedTime, "
                    + "    case when mb.paymentSource = " + PaymentSource.NO_PAYMENT.getEnumValue() + " then '" + PaymentSource.NO_PAYMENT.getEnumName() + "' end as paymentMode, "
                    + "     mb.preparedBy "
                    + "    FROM saleBill mb "
                    + "    LEFT JOIN voucher_type AS pbvt ON mb.v_Type = pbvt.v_Type "
                    + "    LEFT JOIN ledgerMast lm on lm.code = mb.lmCode "
                    + "    LEFT JOIN patientregistration pr ON mb.regDocid = pr.regDocid "
                    + "    LEFT JOIN voucher_type AS prvt ON pr.v_Type = prvt.v_Type "
                    + "    LEFT JOIN patientuhid pu ON pr.UHID = pu.docId "
                    + "    LEFT JOIN voucher_type AS puvt ON pu.v_Type = puvt.v_Type "
                    + "    LEFT JOIN subgroup sdm ON mb.departmentCode = sdm.subcode "
                    + "    LEFT JOIN subGroup AS sgcon ON mb.consultantCode  = sgcon.subCode "
                    + " WHERE mb.site_Code =:siteCode   "
                    + " AND pbvt.category IN (" + getVoucherCategory(voucherCategory) + ")"
                    + " AND mb.v_prefix =:vPrefix ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            sqlWithParam.setString("vPrefix", DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).toString());
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("BillNo")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setUhidNo(DmShared.xNull(rsTemp.getString("UHIDNo")));
                listNode.setOpdIPDNO(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setDeptName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setConsDocName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setStatus(DmShared.xNull(rsTemp.getString("status")));
                listNode.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                listNode.setPreparedDate(DmShared.dNull(rsTemp.getDate("preparedDate")));
                listNode.setPreparedTime(DmShared.xNull(rsTemp.getString("preparedTime")));
                listNode.setSaleBillReturnType(DmShared.xNull(rsTemp.getString("saleBillReturnType")));
                listNode.setPaymentMode(DmShared.xNull(rsTemp.getString("paymentMode")));
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
    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVType(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVType_Mdl> lstTemp = new ArrayList<>();
        TxtHVType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmServer.getUserVoucherTypes(userPermission).equalsIgnoreCase("")) {
                condition = " and vt.v_Type in (" + DmServer.getUserVoucherTypes(userPermission) + ")";
            }
            sqlWithParam.setSQL(" SELECT VT.V_Type, VT.Description, vt.documentNatureCode, vt.documentTypeCode,vt.short_Name, vt.category , vt.paymentSource \n"
                    + "from voucher_Type vt \n"
                    + "where vt.category IN (" + getVoucherCategory(voucherCategory) + ") \n"
                    + "and vt.isActive = 1 " + condition);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                listNode.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                listNode.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));
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
    public DmResult<SaleBillReturn_Mdl> getUHIDDetails(SearchUHID_Mdl searchMdl) {
        DmResult<SaleBillReturn_Mdl> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Connection cnTemp = null;
        String sqlQuery = "", conditionIn = "", conditionOut = "", category = "";
        SaleBillReturn_Mdl mrTemp = new SaleBillReturn_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.IPD)) {
                category = "'" + pvc.IPD + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.OPD)) {
                category = "'" + pvc.OPD + "'" + "," + "'" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.DAY_CARE)) {
                category = "'" + pvc.DAY_CARE + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.EMERGENCY)) {
                category = "'" + pvc.EMERGENCY + "'";
            }

            if (DmShared.xNull(category).trim().equalsIgnoreCase("")) {
                category = "'" + category + "'";
            } else {
                conditionOut += "  AND (pr.regDocId IS NOT NULL) ";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.iNull(searchMdl.getDocIdReg()).longValue() > 0) {
                conditionIn += "  AND (pr.regDocId = :regDocId) ";
                conditionOut += "  AND (pr.regDocId = :regDocId) ";
            } else if (!DmShared.xNull(searchMdl.getRecIdReg()).equalsIgnoreCase("")) {
                conditionIn += "  AND (pr.recId = :recId) ";
            } else if (DmShared.iNull(searchMdl.getDocIdUHID()).longValue() > 0) {
                conditionIn += "  AND (pr.uhid = :docId) ";
                conditionOut += "  AND (pu.docId = :docId) ";
            } else if (!DmShared.xNull(searchMdl.getRecIdUHID()).equalsIgnoreCase("")) {
                conditionIn += "  AND (pu.recId = :recIdUHID) ";
                conditionOut += "  AND (pu.recId = :recIdUHID) ";
            } else {
                conditionIn += " and 1 = 0 ";
            }

            if (DmShared.iNull(searchMdl.getDocIdSaleBill()).longValue() == 0) {
                sqlQuery = "select pu.docId, pr.regDocId, pr.category "
                        + "	from patientUHID pu "
                        + "	left join ( "
                        + "                 select top 1 pr.regDocId, pr.uhid, vt.category"
                        + "                     from patientRegistration pr"
                        + "			left join voucher_Type vt on vt.v_Type = pr.v_Type"
                        + "			left join patientUHID pu on pr.uhid = pu.docId"
                        + "			where pr.site_Code IN (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ") and pr.isCancelled = 0 and vt.category  in (" + category + ") " + conditionIn
                        + "			order by pr.v_Date desc, pr.regTime desc, vt.short_Name, pr.v_No desc, pr.recId"
                        + "         ) pr on pr.uhid = pu.docId"
                        + "	where pu.isCancelled = 0 and pu.isExpired = 0 and isnull(pu.approvedBy,'') <> '' " + conditionOut;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                if (DmShared.iNull(searchMdl.getDocIdReg()).longValue() > 0) {
                    psTemp.setString("regDocId", DmShared.iNull(searchMdl.getDocIdReg()).toString());
                } else if (!DmShared.xNull(searchMdl.getRecIdReg()).equalsIgnoreCase("")) {
                    psTemp.setString("recId", DmShared.xNull(searchMdl.getRecIdReg()).trim());
                } else if (DmShared.iNull(searchMdl.getDocIdUHID()).longValue() > 0) {
                    psTemp.setString("docId", DmShared.iNull(searchMdl.getDocIdUHID()).toString());
                } else if (!DmShared.xNull(searchMdl.getRecIdUHID()).equalsIgnoreCase("")) {
                    psTemp.setString("recIdUHID", DmShared.xNull(searchMdl.getRecIdUHID()).trim());
                }

                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setRegDocid(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                    mrTemp.setUhidDocid(DmShared.iNull(rsTemp.getString("docId")).toString());
                } else {
                    throw new DmException(0, "No record found. Try Again.");
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0 && DmShared.iNull(mrTemp.getUhidDocid()).longValue() == 0) {
                    throw new DmException(0, "No record found. Try Again.");
                }
            } else if (DmShared.iNull(searchMdl.getDocIdSaleBill()).longValue() != 0) {
                sqlQuery = " SELECT "
                        + "     sb.docId "
                        + "   , sb.recId "
                        + "   , sb.regDocId "
                        + "   , sb.uhid "
                        + "   , sb.lmCode "
                        + "   , sb.consultantCode"
                        + "   , sgcon.name AS consultantName "
                        + "	from saleBill sb  "
                        + "	left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                        + "	left join subGroup dep on dep.subCode = sb.departmentCode  "
                        + "     LEFT JOIN consultantmast cm ON sb.consultantCode=cm.subCode  "
                        + "     LEFT JOIN subGroup AS sgcon ON cm.subCode = sgcon.subCode  "
                        + "     where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + "," + getVoucherCategory(pvc.PHARMACY_BILL_CASH) + ") "
                        + "     and sb.isCancelled = 0  "
                        + "	and sb.docId = :docId "
                        + "     and sb.v_date <= :v_date ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(searchMdl.getDocIdSaleBill()).toString());
                psTemp.setDate("v_date", DmServer.convertDate(searchMdl.getVoucherDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setDocIdSaleBill(DmShared.xNull(rsTemp.getString("docId")));
                    mrTemp.setRecIdSaleBill(DmShared.xNull(rsTemp.getString("recId")));
                    mrTemp.setRegDocid(DmShared.xNull(rsTemp.getString("regDocId")));
                    mrTemp.setUhidDocid(DmShared.xNull(rsTemp.getString("uhid")));
                    mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                    mrTemp.setVoucherDate(DmShared.dNull(searchMdl.getVoucherDate()));
                    mrTemp.setConsultantCode(DmShared.iNull(rsTemp.getString("consultantCode")).toString());
                    mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));

                } else {
                    throw new DmException(0, "No record found. Try Again.");
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() == 0) {
                    throw new DmException(0, "No record found. Try Again.");
                }
            }

            getPatientDetail(mrTemp, searchMdl, false, cnTemp);
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (SQLException | DmException e) {
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

    private void getPatientDetail(SaleBillReturn_Mdl mrTemp, SearchUHID_Mdl searchMdl, boolean isFromMoveRec, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Organization_Mdl organizationMdl;
        String sqlQuery = "";
        try {
            if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0 || DmShared.iNull(mrTemp.getUhidDocid()).longValue() > 0) {
                sqlQuery = "SELECT lm.code as lmCode,lm.subCode as lmSubCode,pr.regDocId, pr.recId AS regNo, pr.category, pu.docId, pu.recId AS uhidNo, "
                        + "         lm.name as lmName, lm.manualCode as lmManualCode, pu.patientName as patientName, pr.v_Date, pr.site_Code,"
                        + "         CONCAT(pu.address1,' ',pu.address2) as address, pu.cityCode,pu.mobile,c.cityName, pu.idNo,pu2.idTypeCode,idType.name as idTypeName, "
                        + "         pr.consultantCode, sg.name AS consultant, sgo.name organization, rlm.medicineSaleOn, om.isTPA, om.shortName, cem.empName, "
                        + "         est.docId docIdSaleBill, est.recId saleBillNo, "
                        + "         case pr.category when " + getVoucherCategory(pvc.DAY_CARE) + " then 'Day Care' when " + getVoucherCategory(pvc.EMERGENCY) + " "
                        + "         then 'Emergency'  else pr.category end regType, "
                        + "		case when pr.regDocId is null then pu.corporateEmployeeCode else pr.corporateEmployeeCode end corporateEmployeeCode,"
                        + "		case when pr.regDocId is null then pu.organizationCode else pr.organizationCode end organizationCode,"
                        + "		case when pr.regDocId is null then pu.orgPolicyNo else pr.orgPolicyNo end orgPolicyNo,"
                        + "		case when pr.regDocId is null then pu.rankNo else pr.rankNo end rankNo,"
                        + "		case when pr.regDocId is null then pu.orgHolderName else pr.orgHolderName end orgHolderName,"
                        + "		case when pr.regDocId is null then pu.orgRelationShip else pr.orgRelationShip end orgRelationShip, pp.prescDocId,pr.roomCode"
                        + "	FROM patientUHID AS pu "
                        + "	left join patientUHID2 AS pu2 ON pu2.uhid = pu.docId "
                        + "	left join ( "
                        + "             select top 1 pr.regDocId, pr.uhid, vt.category, pr.consultantCode, pr.organizationCode, pr.corporateEmployeeCode, "
                        + "                     pr.recId, pr.v_Date, pr.orgPolicyNo, pr.rankNo, pr.orgHolderName, pr.orgRelationShip, pr.site_Code,pr.roomCode "
                        + "			from patientRegistration pr "
                        + "			left join voucher_Type vt on vt.v_Type = pr.v_Type "
                        //                        + "			where pr.site_Code = :siteCode and pr.isCancelled = 0 ";
                        + "			where pr.regDocId = :regDocId";
                sqlQuery += " ) pr ON pu.docId = pr.uhid "
                        + "	left join subGroup as sg ON pr.consultantCode = sg.subCode "
                        + "	 LEFT JOIN hl7Mast as idType on idType.code = pu2.idTypeCode "
                        + "     left join city c ON c.cityCode = pu.cityCode "
                        + "     left join ledgerMast lm ON lm.regDocId = pr.regDocId "
                        + "	left join organizationMast om ON om.subCode = (case when pr.regDocId is null then pu.organizationCode else pr.organizationCode end) "
                        + "     Left JOin rateListMast rlm on om.rateListcode=rlm.code "
                        + "	left join subGroup as sgo ON om.subCode = sgo.subCode "
                        + "	left join corporateEmployeeMast cem ON cem.code = (case when pr.regDocId is null then pu.corporateEmployeeCode else pr.corporateEmployeeCode end) "
                        + "     left join saleBill est ON (case when pr.regDocId is null then est.uhid else est.regDocId end) = (case when pr.regDocId is null then pu.docId else pr.regDocId end) "
                        + "     left join (select top 1 pp.regDocId,pp.docId as prescDocId  "
                        + "			from patientPrescription pp  "
                        + "			where pp.regDocId = :regDocId  "
                        + "			order by pp.v_Date desc, pp.v_Time desc  "
                        + "         ) as pp on pp.regDocId = pr.regDocId "
                        + "	where pu.isCancelled = 0 ";
                if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                    sqlQuery += " and pr.regDocID =  :regDocId ";
                }
                if (DmShared.iNull(mrTemp.getUhidDocid()).longValue() > 0) {
                    sqlQuery += " and pu.docId =  :uhid ";
                }
                if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
                    sqlQuery += " and est.docId =  :saleBillDocId ";
                }
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                if (DmShared.iNull(mrTemp.getUhidDocid()).longValue() > 0) {
                    psTemp.setString("uhid", DmShared.iNull(mrTemp.getUhidDocid()).toString());
                }
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocid()).toString());
                if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
                    psTemp.setString("saleBillDocId", DmShared.iNull(mrTemp.getDocIdSaleBill()).toString());
                }
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    organizationMdl = new Organization_Mdl();
                    organizationMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("organizationCode")));
                    organizationMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("organization")));
                    organizationMdl.setCorporateEmployeeCode(DmShared.xNull(rsTemp.getString("corporateEmployeeCode")));
                    organizationMdl.setCorporateEmployeeName(DmShared.xNull(rsTemp.getString("empName")));
                    organizationMdl.setHolderName(DmShared.xNull(rsTemp.getString("orgHolderName")));
                    organizationMdl.setOrgShortName(DmShared.xNull(rsTemp.getString("shortName")));
                    organizationMdl.setPolicyNo(DmShared.xNull(rsTemp.getString("orgPolicyNo")));
                    organizationMdl.setRankNo(DmShared.xNull(rsTemp.getString("rankNo")));
                    organizationMdl.setRelationshipCode(DmShared.xNull(rsTemp.getString("orgRelationShip")));
                    organizationMdl.setRelationshipName(DmShared.xNull(rsTemp.getString("orgRelationShip")));
                    organizationMdl.setTpa(DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 1);
                    mrTemp.setOrganizationMdl(organizationMdl);

                    if (!isFromMoveRec) {
                        mrTemp.setRegDocid(DmShared.iNull(mrTemp.getRegDocid()).toString());
                        mrTemp.setRegSiteCode(DmShared.iNull(rsTemp.getString("site_Code")).toString());
                        mrTemp.setRoomCode(DmShared.iNull(rsTemp.getString("roomCode")).toString());
                        mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                        mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo")));
                        mrTemp.setRegRecID(DmShared.xNull(rsTemp.getString("regNo")));
                        if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() == 0) {
                            mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                            mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultant")));
                        }
                        if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
                            mrTemp.setRecIdSaleBill(DmShared.xNull(rsTemp.getString("saleBillNo")));
                            mrTemp.setDocIdSaleBill(DmShared.xNull(rsTemp.getString("docIdSaleBill")));
                        }

                        mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                        mrTemp.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                        mrTemp.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                        mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("lmSubCode")));
                        mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                        mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                        mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                        mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                        mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                        mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                        mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                        mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("category")));
                        mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("regType")));
                        if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0 && DmShared.iNull(mrTemp.getSubCode()).longValue() == 0) {
                            mrTemp.setTypeCode("Walk-In");
                            mrTemp.setTypeName("Walk-In");
                        }

                        mrTemp.setSaleMedicine(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));
                        mrTemp.setUhidDocid(DmShared.iNull(rsTemp.getString("docId")).toString());
                    }
                }
                rsTemp.close();
                psTemp.close();
            } else if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() > 0) {
                sqlQuery = " SELECT "
                        + "     lm.displayName as patientName"
                        + "     , sb.lmCode"
                        + "     ,lm.address"
                        + "     ,sb.idNo"
                        + "     ,sb.idTypeCode"
                        + "     ,idType.name as  idTypeName"
                        + "     ,lm.mobile "
                        + "     ,lm.cityCode"
                        + "     ,c.cityName "
                        + "     , lm.name as lmName "
                        + "     , lm.manualCode as lmManualCode "
                        + "	fROM saleBill sb  "
                        + "	LEFT JOIN voucher_Type vt on vt.v_Type = sb.v_Type  "
                        + "     LEFT JOIN ledgerMast lm on lm.code = sb.lmCode "
                        + "	LEFT JOIN hl7Mast as idType on idType.code = sb.idTypeCode "
                        + "     LEFT JOIN city c ON c.cityCode = lm.cityCode "
                        + "     WHERE vt.category IN (" + getVoucherCategory(pvc.PHARMACY_BILL) + "," + getVoucherCategory(pvc.PHARMACY_BILL_CASH) + ") "
                        + "     AND sb.isCancelled = 0  "
                        + "	AND sb.docId = :docId "
                        + "     AND sb.v_Date <= :v_Date "
                        + "     AND sb.regDocId is null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdSaleBill()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                    mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));

                    mrTemp.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                    mrTemp.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                    mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                    mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                    mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                    mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                    mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                    mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                    mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                    if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0) {
                        mrTemp.setTypeCode("Walk-In");
                        mrTemp.setTypeName("Walk-In");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

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
    }

    @Override
    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory, UserPermission userPermission) {
        DmResult<Default_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Default_Mdl mrTemp = new Default_Mdl();
        Layout_Mdl layout;
        String userPermissionShortName = "", condition = "";
        List<TxtHReceiptType_VList_Mdl> reciptList = new ArrayList<TxtHReceiptType_VList_Mdl>();
        String defaultMode;
        List<String> PayMode = new ArrayList<String>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setCardPaymentReturn(new CardPaymentPopup_Mdl());

            mrTemp.setPartyType(DmEnum.DmPartyType.Unregistered.getId());

            if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("Super"))) {
                sqlQuery = "select paymentTypeCode \n"
                        + " from " + DmServer.dbCompany + ".dbo.userPayModePermission upm \n"
                        + "left join voucher_Type as vt on vt.v_Type =  upm.v_Type\n"
                        + "where  isnull(upm.isDefault,0)=1 and  upm.userName = '" + DmServer.getSsnUserName(this) + "' \n"
                        + "And vt.category = '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "'";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    String code = DmShared.xNull(rsTemp.getString("paymentTypeCode"));
                    PayMode.add(code);
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = "SELECT  vt.v_Type, vt.description, vt.attachPaymentMode, vt.isSystemDefine"
                    + "     FROM voucher_Type vt "
                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "' and vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                TxtHReceiptType_VList_Mdl rtvMdl = new TxtHReceiptType_VList_Mdl();
                rtvMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("v_Type")));
                rtvMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                rtvMdl.setV_type(DmShared.xNull(rsTemp.getString("v_Type")));
                rtvMdl.setDescription(DmShared.xNull(rsTemp.getString("description")));
                rtvMdl.setPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));
                rtvMdl.setIsDefault(DmShared.xNull(rsTemp.getString("isSystemDefine")));
                if (!DmShared.xNull(rtvMdl.getPaymentMode()).equalsIgnoreCase("")) {
                    String[] pmArray = DmShared.xNull(rtvMdl.getPaymentMode()).split("\\|");

                    if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("Super"))) {

                        defaultMode = pmArray[0];

                        if (PayMode != null && PayMode.size() > 0) {

                            for (String str : pmArray) {
                                if (DmShared.xNull(PayMode.get(0)).equalsIgnoreCase(str)) {
                                    rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(str)}, cnTemp).get(0));
                                    break;
                                }
                            }

                        } else {
                            rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(pmArray[0])}, cnTemp).get(0));
                        }

                    } else {
                        rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(pmArray[0])}, cnTemp).get(0));
                    }
                }
                reciptList.add(rtvMdl);
            }
            rsTemp.close();
            psTemp.close();
            mrTemp.getCardPaymentReturn().getTxtHReceiptTypeVList().addAll(reciptList);

            /*======================UserPermissionShortName=====================================*/
//             sqlQuery = "SELECT  uvp.vTypeSName"
//                        + "     FROM "+dbCompany.trim()+".dbo.user_Voucher_Permission uvp  \n"
//                        + "     left join voucher_Type VT on vt.short_name  = uvp.vTypeSName "
//                        + "     WHERE  uvp.user_Name = '" + DmServer.getSsnUserName(this) + "' ";
//             
//             psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                userPermissionShortName= DmShared.xNull(rsTemp.getString("vTypeSName"));
//            }
//            psTemp.close();
//            rsTemp.close();
//
//            String userName = DmServer.getSsnUserName(this);
////            if ((!getVoucherCategory(voucherCategory).equalsIgnoreCase(userPermissionShortName)) ||(userPermissionShortName.equalsIgnoreCase("")) || userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
//            if ((userPermissionShortName.equalsIgnoreCase("")) || userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
////            if (DmShared.xNull(rsTemp.getString("vTypeSName")).equalsIgnoreCase("") || userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
//                sqlQuery = " select TOP 1 vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode,vt.Short_Name "
//                        + " FROM voucher_Type vt"
//                        + " WHERE vt.category IN (" + getVoucherCategory(voucherCategory) + ") AND vt.isActive = 1 ";
////                        + " WHERE vt.category = " + getVoucherCategory(voucherCategory) + " AND vt.isActive = 1 ";
//            } else {
//                sqlQuery = "SELECT  vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode,vt.Short_Name"
//                        + "     FROM "+dbCompany.trim()+".dbo.user_Voucher_Permission uvp  \n"
//                        + "     left join voucher_Type VT on vt.short_name  = uvp.vTypeSName "
//                        + "     WHERE vt.Short_Name IN ('" + userPermissionShortName + "')  and vt.isActive = 1 "
////                        + "     WHERE vt.Short_Name = " + getVoucherCategory(voucherCategory) + "  and vt.isActive = 1 "
//                        + "     and uvp.user_Name = '" + DmServer.getSsnUserName(this) + "' ";
//
//            }
//            sqlQuery = " select TOP 1 vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode,vt.short_Name "
//                    + " FROM voucher_Type vt"
//                    + " WHERE vt.category IN (" + getVoucherCategory(voucherCategory) + ")"
//                    + " AND vt.isActive = 1 ";
            if (!DmServer.getUserVoucherTypes(userPermission).equalsIgnoreCase("")) {
                condition = " and vt.v_Type in (" + DmServer.getUserVoucherTypes(userPermission) + ")";
            }
            sqlQuery = " select vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode,vt.Short_Name, vt.category ,vt.paymentSource  \n"
                    + "from voucher_Type vt \n"
                    + "where vt.category in( '" + voucherCategory + "') \n"
                    + "and vt.isActive = 1 " + condition + " order by vt.description";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                TxtHVType_Mdl vTypeMdl = new TxtHVType_Mdl();
                vTypeMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                vTypeMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                vTypeMdl.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                vTypeMdl.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                vTypeMdl.setShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                vTypeMdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                vTypeMdl.setUserPermissionShortName(userPermissionShortName);
                vTypeMdl.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));
                mrTemp.setVoucherTypeMdl(vTypeMdl);
            }
            psTemp.close();
            rsTemp.close();

            mrTemp.setUnionTerritory(DmShared.iNull(DmServer.getCodeViaManualCode(DmServer.getSsnSiteStateNumericCode(this), "numericCode", "stateMast", "isUt", cnTemp)).intValue() == 1);
            mrTemp.setControlAccount(DmServer.getCodeViaManualCode(DmServer.getSsnSiteCode(this), "site_Code", "enviro", "pharmacyControlAc", cnTemp));

            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast dm "
                    + "		left join subGroup sg on dm.subCode = sg.subCode "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode and sgsl.site_Code = :siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT  "
                    + "    env.expiryAlertBeforeDays "
                    + "  , env.medicineSaleOn "
                    + "  , env.alphCase_Patient "
                    + "  , env.alphaCase_Address "
                    + "  , env.defaultPharmacyServiceCode "
                    + "  , env.pharmacyAllowReturnWithoutSale "
                    + "  , env.pharmacyReturnDays "
                    + "  , env.isIncludeTaxInCost "
                    + "  , env.pharmacyBarcodeActive "
                    + "  ,env.pharmacyRequisitionWithoutUHIDInBill "
                    + "  ,env.salesReturnAgainstRequstInIPD "
                    + "  ,env.approvalInPharmacyCashSaleReturn "
                    + "  FROM enviro as env "
                    + "  Where env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setExpiryAlertBeforeDays(DmShared.xNull(rsTemp.getString("expiryAlertBeforeDays")));
                mrTemp.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));
                mrTemp.setPatientAlphabetCase(DmShared.xNull(rsTemp.getString("alphCase_Patient")));
                mrTemp.setAddressAlphabetCase(DmShared.xNull(rsTemp.getString("alphaCase_Address")));
                mrTemp.setDefaultPharmacyServiceCode(DmShared.xNull(rsTemp.getString("defaultPharmacyServiceCode")));
                mrTemp.setAllowReturnWithoutSale(DmShared.iNull(rsTemp.getString("pharmacyAllowReturnWithoutSale")).toString());
                mrTemp.setPharamcyReturnDays(DmShared.iNull(rsTemp.getString("pharmacyReturnDays")).toString());
                mrTemp.setPharmacyBarcodeActive(DmShared.iNull(rsTemp.getString("pharmacyBarcodeActive")).toString());
                mrTemp.setBillWithoutUHID(DmShared.xNull(rsTemp.getString("pharmacyRequisitionWithoutUHIDInBill")));
                mrTemp.setApprovalRequired(DmShared.xNull(rsTemp.getString("approvalInPharmacyCashSaleReturn")));

                if (DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).trim().equals("")) {
                    throw new DmException(0, "Please set 'Include Tax In Cost' in environment settings.");
                } else {
                    mrTemp.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                }
                mrTemp.setSalesReturnAgainstRequstInIPD(DmShared.iNull(rsTemp.getString("salesReturnAgainstRequstInIPD")).intValue() == 1);

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "Select  code, nature, sNo, isVisible, isMandatory "
                    + "         from managePageLayout "
                    + "         where nature = 'patientInfoPopup' and isVisible = 1 "
                    + "         order by sNo, code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("code")).trim().equalsIgnoreCase("")) {
                    layout = new Layout_Mdl();
                    layout.setCode(DmShared.xNull(rsTemp.getString("code")));
                    layout.setIsMandatory(DmShared.iNull(rsTemp.getString("isMandatory")).intValue() == 1);
                    layout.setIsVisible(DmShared.iNull(rsTemp.getString("isVisible")).intValue() == 1);
                    layout.setSerial(DmShared.iNull(rsTemp.getString("sNo")).intValue());
                    mrTemp.getLstLayout().add(layout);
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==========================<< Get Site GST Details >>==========================*/
            sqlQuery = "SELECT "
                    + "   sm.code "
                    + " FROM stateMast sm"
                    + " WHERE sm.numericCode = :numericCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("numericCode", DmShared.xNull(DmServer.getSsnSiteStateNumericCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setStateCode(DmShared.xNull(rsTemp.getString("code")));
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.setRoundOffAcCode(getRoundOffAcCode(DmServer.getServerDateTime()[0], cnTemp));
            mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            mrTemp.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            if (ipdAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.IPD);
                listNode.setSharedKeyFieldName(pvc.IPD);
                lstTemp.add(listNode);
            }

            if (opdAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.OPD);
                listNode.setSharedKeyFieldName(pvc.OPD);
                lstTemp.add(listNode);
            }
            if (dayCareAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
                listNode.setSharedKeyFieldName("Day Care");
                lstTemp.add(listNode);
            }
            if (emergencyAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
                listNode.setSharedKeyFieldName("Emergency");
                lstTemp.add(listNode);
            }
            if (ledgerAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Ledger");
                listNode.setSharedKeyFieldName("Ledger");
                lstTemp.add(listNode);
            }
            if (walkInAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Walk-In");
                listNode.setSharedKeyFieldName("Walk-In");
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
            if (!STServer.getSsnDepartmentCodeList(this).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":subCode", STServer.getSsnDepartmentCodeList(this)) + ")";

            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select dm.subCode, sg.name "
                    + "	from departmentMast dm "
                    + "	left join subGroup sg on sg.subCode = dm.subCode "
                    + "	left join subGroupSiteList sgsl on sg.subCode = sgsl.subCode "
                    + "	left join subGroupNature sgn on sgn.code = sg.nature "
                    + "	where sgn.manualCode in ('PH','CM') and sg.isActive = 1 and sgsl.site_Code = :siteCode " + condition);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (!STServer.getSsnDepartmentCodeList(this).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("subCode", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsutant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name  "
                    + "		from consultantMast as cm  "
                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = getITS(itemCode, rate, itsBasedOn, billDate, cnTemp);
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
                    location = DmShared.iNull(DmLocation.UnionTerritory.getId()).toString();
                } else {
                    location = DmShared.iNull(DmLocation.IntraState.getId()).toString();
                }
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp = STServer.getITS(itemCode, rate, itsBasedOn, ITSType.SALES_RETURN, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
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

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    @Override
    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHSaleBillNo(DmHelper helper, String paymentSource, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, String docIdSaleReturn, String lmCode, String typeCode, String pharmacyReturnDays, String vTypeCode, String uhidDocID) {
        DmResultPageing<List<TxtHSaleBillNo_Mdl>> rstTemp = new DmResultPageing<List<TxtHSaleBillNo_Mdl>>();
        List<TxtHSaleBillNo_Mdl> lstTemp = new ArrayList<TxtHSaleBillNo_Mdl>();
        TxtHSaleBillNo_Mdl listNode;
        Connection cnTemp = null;
        Connection cnTempHIS = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", innerCondition = "", join = "", innerHelpCondition = "", innerHelpCondition1 = "";
        CardPaymentPopup_Mdl cardPaymentPopup_Mdl = new CardPaymentPopup_Mdl();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            Calendar cal;
            cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, 0 - DmShared.iNull(pharmacyReturnDays).intValue());
            condition += " and (sb.v_Date <= '" + DmShared.dNull(cal.getTime()) + "') ";

            if (DmShared.iNull(docIdSaleReturn).longValue() > 0) {
                join = " LEFT JOIN ("
                        + "             SELECT srd.sbDocid, srd.sbV_SNo, srd.quantity, srd.freeQty "
                        + "             FROM saleBillDetail srd "
                        + "             WHERE srd.docid = :srDocid "
                        + "     ) vSbr ON sbd1.docid =  vSbr.sbDocid AND sbd1.v_SNo = vSbr.sbV_SNo ";

                innerCondition = "  AND (IsNull(sbd1.quantity,0)-IsNull(sbd1.returnedQty,0)) + IsNull(vSbr.quantity,0) > 0 ";
            } else {
                innerCondition = "  AND IsNull(sbd1.quantity,0)-IsNull(sbd1.returnedQty,0) > 0 ";
            }
            if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase("PHBRT")) {
//                innerHelpCondition = " and vt1.short_Name = 'PHBIL'";
                innerHelpCondition = " and vt1.category = 'PHBCS'";
            } else if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase(pvc.PHARMACY_CASH_SALE_RETURN) && DmShared.xNull(typeCode).equalsIgnoreCase("Walk-In")) {
                innerHelpCondition = " and vt1.category in('PHBCS','PHBIL') ";
            } else if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase(pvc.PHARMACY_CASH_SALE_RETURN)) {
                innerHelpCondition = " and vt1.category ='PHBCS'";
            }

            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                condition += " and 1 = 2";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition += " and sb.departmentCode = :departmentCode ";
            } else {
                condition += " and 1 = 2";
            }
            if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase("PHBRT")) {
//                innerHelpCondition1 += " and vt.category = 'PHBIL' ";
                innerHelpCondition1 += " and vt.category = 'PHBCS' ";
            } else if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase(pvc.PHARMACY_CASH_SALE_RETURN) && DmShared.xNull(typeCode).equalsIgnoreCase("Walk-In")) {
                innerHelpCondition1 = " and vt.category in('PHBCS','PHBIL') ";
            } else if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase(pvc.PHARMACY_CASH_SALE_RETURN)) {
                innerHelpCondition1 = " and vt.category ='PHBCS'";
            }
            if (DmShared.xNull(vTypeCode).trim().equalsIgnoreCase("PHCSR") && DmShared.xNull(typeCode).equalsIgnoreCase("Walk-In")
                    && !DmShared.xNull(uhidDocID).equalsIgnoreCase("")) {
                condition += " and sb.regDocId in(select distinct regDocID from patientRegistration where uhid=" + DmShared.xNull(uhidDocID).trim() + ") ";
            }
            if (DmShared.iNull(regDocId).longValue() > 0) {
                condition += " and sb.regDocId = :regDocId ";
            }

            if (DmShared.iNull(lmCode).longValue() > 0) {
                condition += " and sb.lmCode = :lmCode ";
            }

            if (DmShared.xNull(typeCode).equalsIgnoreCase("Walk-In") && DmShared.xNull(uhidDocID).equalsIgnoreCase("")) {
                condition += " and ISNULL(lm.subCode,'') = '' and ISNULL(lm.regDocId,'') = '' ";
            }
            if (!DmShared.xNull(STServer.getSsnPharmacyAllowSaleBillForSite(this)).equalsIgnoreCase("")) {
                condition += " and sb.site_Code IN (" + STServer.getSsnPharmacyAllowSaleBillForSite(this) + ") ";
            } else {
                condition += " and 1 = 2";
            }

            if (DmShared.iNull(paymentSource).intValue() != PaymentSource.PAYMENT.getEnumValue()) {
                condition += "  and  sb.paymentSource = :paymentSource ";
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            cnTempHIS = DmServer.getConnection(STServer.getSsnParentDateBase(this));
            sqlQuery = "  Select  sb.paymentSource ,sb.regType_HIS, sb.regNo_HIS ,sb.uhidNo_HIS , sb.regDocId_HIS ,sb.siteCode_HIS  , "
                    + "     sb.docid "
                    + "   , sb.recID "
                    + "   , sb.v_Date"
                    + "   , sb.departmentCode"
                    + "   , dep.name as departmentName"
                    + "   , sb.v_type \n"
                    + "   , vt.short_Name"
                    + "   , sb.v_No"
                    + "   , sb.lmCode"
                    + "   , lm.displayName as patientName \n"
                    + "   , sb.oh_Amt_Net"
                    + "   , sb.consultantCode"
                    + "   , sgcon.name AS consultantName "
                    + "   , lm.name as lmName "
                    + "   , lm.manualCode as lmManualCode \n"
                    + "   FROM saleBill sb \n"
                    + "   INNER JOIN voucher_Type vt On vt.v_Type=sb.v_type \n"
                    + "   INNER JOIN subGroup dep on dep.subCode = sb.departmentCode \n"
                    + "   LEFT JOIN ledgerMast lm on lm.code = sb.lmCode \n"
                    + "   LEFT JOIN consultantmast cm ON sb.consultantCode=cm.subCode  \n"
                    + "   LEFT JOIN subGroup AS sgcon ON cm.subCode = sgcon.subCode  \n"
                    + "   INNER JOIN ( \n"
                    + "		Select sb1.docid "
                    + "		from saleBill sb1 \n "
                    + "		INNER JOIN saleBillDetail sbd1 ON sb1.docId=sbd1.docid \n"
                    + "		INNER JOIN voucher_Type vt1 On vt1.v_Type=sb1.v_type \n" + join
                    + "		where    sbd1.isFooter=0 and vt1.category in( :category )\n" + innerHelpCondition
                    + "		and sb1.isCancelled = 0 \n" + innerCondition
                    + "         Group By sb1.docId \n"
                    + "		) vw On vw.docid=sb.docid \n"
                    + "    where vt.category in( :category ) " + innerHelpCondition1
                    //                    + "    and  vt.v_Type = :v_Type"
                    + "    and sb.v_date <= :v_Date "
                    + "    and sb.isCancelled = 0  "
                    + "    " + condition;
//            sqlQuery="select gstinDate as v_Date ,manualCode as short_Name ,manualCode as v_No ,manualCode as recID,* from subGroup";
            sqlWithParam.setSQL(sqlQuery);
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
//            sqlWithParam.setString("category", pvc.PHARMACY_BILL+"','"+pvc.PHARMACY_BILL_CASH);
            sqlWithParam.setString("category", pvc.PHARMACY_BILL_CASH);

            if (DmShared.iNull(paymentSource).intValue() != PaymentSource.PAYMENT.getEnumValue()) {
                sqlWithParam.setString("paymentSource", DmShared.xNull(paymentSource));
            }

            if (DmShared.iNull(docIdSaleReturn).longValue() > 0) {
                sqlWithParam.setString("srDocid", DmShared.xNull(docIdSaleReturn));
            }

            if (DmShared.iNull(regDocId).longValue() > 0) {
                sqlWithParam.setString("regDocId", DmShared.iNull(regDocId).toString());
            }
            if (DmShared.iNull(lmCode).longValue() > 0) {
                sqlWithParam.setString("lmCode", DmShared.iNull(lmCode).toString());
            }
            if (DmShared.dNull(voucherDate) != null) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(voucherDate));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSaleBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                listNode.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                listNode.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                listNode.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                listNode.setV_Type(DmShared.xNull(rsTemp.getString("v_type")));
                listNode.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                listNode.setSaleBillDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setConsultantCode(DmShared.iNull(rsTemp.getString("consultantCode")).toString());
                listNode.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.setPaymentSourceCode(DmShared.xNull(rsTemp.getString("paymentSource")));
                listNode.setRegTypeHIS(DmShared.xNull(rsTemp.getString("regType_HIS")));

                listNode.setRegNo_HIS(DmShared.xNull(rsTemp.getString("regNo_HIS")));
                listNode.setUhidNo_HIS(DmShared.xNull(rsTemp.getString("uhidNo_HIS")));
                listNode.setRegDocId_HIS(DmShared.xNull(rsTemp.getString("regDocId_HIS")));
                listNode.setSiteCode_HIS(DmShared.xNull(rsTemp.getString("siteCode_HIS")));

                listNode.setPaymentSourceName(PaymentSource.getValueOf(DmShared.iNull(rsTemp.getString("paymentSource")).intValue()).getEnumName());

//                if (DmShared.iNull(rsTemp.getString("regDocId_HIS")).longValue() > 0) {
//                    listNode.setAllowCredittoRoom(chekForIPDBillCreationAndAllowCTR(DmShared.iNull(rsTemp.getString("regDocId_HIS")).toString(), cnTempHIS));
//                }
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
            cnTemp.close();
//            cnTempHIS.close();
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
//                if (cnTempHIS != null) {
//                    cnTempHIS.close();
//                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private boolean chekForIPDBillCreationAndAllowCTR(String regDocid, Connection cnTempHIS) {

        boolean isAllowCredittoRoom = true;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            /*====================================< Check JINDAL PHARMACY OUTSOURCE >====================================*/
            sqlQuery = " select  sb.recId "
                    + " from saleBill sb \n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "\n"
                    + "where sb.regDocId= :regDocId  and vt.short_Name='IPDBL' and isnull(sb.isCancelled,0)=0 ";

            psTemp = DmPreparedStatement.prepareStatement(cnTempHIS, sqlQuery);
            psTemp.setString("regDocId", DmShared.iNull(regDocid).toString());

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                isAllowCredittoRoom = false;
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
        }

        return isAllowCredittoRoom;
    }

    @Override
    public DmResult<String> getRoundOffAcCode(String vDate) {
        DmResult<String> dmrtemp = new DmResult<String>();
        String roundOffAcCode = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            roundOffAcCode = getRoundOffAcCode(vDate, cnTemp);
            dmrtemp.setData(roundOffAcCode);
            dmrtemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            dmrtemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrtemp;
    }

    private String getRoundOffAcCode(String vDate, Connection cnTemp) {
        String roundOffItemCode = "", itsBasedOn = "", roundOffAcCode = "";
        List<OverHeadITS_Mdl> lstITS = new ArrayList<OverHeadITS_Mdl>();
        try {
            roundOffItemCode = DmServer.getCodeViaManualCode("ROFF", "manualCode", "overheadMast", "itemCode", cnTemp);
            itsBasedOn = DmServer.getCodeViaManualCode(roundOffItemCode, "code", "itemMast", "itsBasedOn", cnTemp);
            if (DmShared.iNull(roundOffItemCode).longValue() > 0) {
                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(roundOffItemCode, 0, itsBasedOn, vDate, cnTemp);
                lstITS.addAll(dmrIts.getData());
                if (lstITS != null && lstITS.size() > 0) {
                    for (OverHeadITS_Mdl itsMdl : lstITS) {
                        if (DmShared.xNull(itsMdl.getOverHeadManualCode()).equalsIgnoreCase("ROFF")) {
                            roundOffAcCode = DmShared.xNull(itsMdl.getAccountCode());
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        }
        return roundOffAcCode;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIdType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select code, name from hl7Mast where tableName='identifierType' and isActive=1");
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

    private void manageReturnedQty(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, sbDocId = "";
        double srQty, sbReturnedQty, sbReturnedFreeQty, sbQty, sbFreeQty, srFreeQty;
        Integer isCancelled;

        try {
            sqlQuery = " SELECT "
                    + "   sb.docId AS sbDocId "
                    + " , srd.sbV_SNo "
                    + " , srd.quantity AS srQty "
                    + " , srd.freeQty AS srFreeQty "
                    + " , sbd.quantity AS sbQty "
                    + " , sbd.freeQty AS sbFreeQty "
                    + " , sbd.returnedQty AS sbReturnedQty "
                    + " , sbd.returnedFreeQty AS sbReturnedFreeQty "
                    + " , sb.isCancelled "
                    + " , sb.recId AS sbNo "
                    + " , vt.short_name AS sbType "
                    + " FROM saleBillDetail sbd "
                    + " LEFT JOIN saleBill sb ON sbd.docId = sb.docId "
                    + " LEFT JOIN voucher_type vt ON sb.v_Type = vt.v_type "
                    + " INNER JOIN ( "
                    + "     SELECT "
                    + "         srd.sbDocId "
                    + "       , srd.sbV_SNo "
                    + "       , SUM(srd.quantity) AS quantity "
                    + "       , SUM(srd.freeQty) AS freeQty "
                    + "     FROM saleBillDetail srd "
                    + "     WHERE srd.docId = :docId "
                    + "     GROUP BY srd.sbDocId, srd.sbV_SNo "
                    + " ) srd ON sbd.docId = srd.sbDocId AND sbd.v_SNo = srd.sbV_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isCancelled = DmShared.iNull(rsTemp.getString("isCancelled")).intValue();
                if (isCancelled.intValue() == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sbDocId")), DmShared.xNull(rsTemp.getString("sbV_SNo")), "Bill is Cancelled");
                }
                srQty = rsTemp.getDouble("srQty");
                sbQty = rsTemp.getDouble("sbQty");
                srFreeQty = rsTemp.getDouble("srFreeQty");
                sbFreeQty = rsTemp.getDouble("sbFreeQty");
                sbReturnedQty = rsTemp.getDouble("sbReturnedQty");
                sbReturnedFreeQty = rsTemp.getDouble("sbReturnedFreeQty");

                if (isDelete) {
                    if (srQty > 0) {
                        sbReturnedQty -= srQty;
                    } else if (srFreeQty > 0) {
                        sbReturnedFreeQty -= srFreeQty;
                    }
                } else {
                    if (srQty > 0) {
                        sbReturnedQty += srQty;
                    } else if (srFreeQty > 0) {
                        sbReturnedFreeQty += srFreeQty;
                    }

                }

                if (sbReturnedQty > sbQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sbDocId")), DmShared.xNull(rsTemp.getString("sbV_SNo")), "Bill overflows by " + DmServer.vNull(sbReturnedQty - sbQty));
                } else if (sbReturnedQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sbDocId")), DmShared.xNull(rsTemp.getString("sbV_SNo")), "Bill overflows by " + DmServer.vNull(sbReturnedQty));
                }
                if (sbReturnedFreeQty > sbFreeQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sbDocId")), DmShared.xNull(rsTemp.getString("sbV_SNo")), "Bill overflows by " + DmServer.vNull(sbReturnedFreeQty - sbFreeQty));
                } else if (sbReturnedFreeQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("sbDocId")), DmShared.xNull(rsTemp.getString("sbV_SNo")), "Bill overflows by " + DmServer.vNull(sbReturnedFreeQty));
                }

                if (!sbDocId.equalsIgnoreCase("")) {
                    sbDocId += ",";
                }
                sbDocId += DmShared.iNull(rsTemp.getString("sbDocId")).toString();

                sqlQuery = "UPDATE saleBillDetail SET"
                        + " returnedQty = :returnedQty"
                        + " ,returnedFreeQty = :returnedFreeQty "
                        + " WHERE docId = :sbDocId AND v_SNo = :sbV_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("sbDocId", DmShared.xNull(rsTemp.getString("sbDocId")));
                psTemp1.setString("sbV_SNo", DmShared.xNull(rsTemp.getString("sbV_SNo")));
                psTemp1.setString("returnedQty", DmServer.vNull(sbReturnedQty, "0.0000000"));
                psTemp1.setString("returnedFreeQty", DmServer.vNull(sbReturnedFreeQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!sbDocId.equalsIgnoreCase("")) {
                sqlQuery = "UPDATE saleBill SET u_EntDt_LatestLine = :uEntDt where docId in (" + sbDocId + ")";
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
    public DmResult<String> getBalanceSaleBill(String lmCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        String returnValue = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            returnValue = DmServer.vNull(STServer.getBalanceLedgerMast(lmCode, DmServer.getSsnSiteCode(this), cnTemp));
            dmrTemp.setStatus(true);
            dmrTemp.setData(returnValue);
            cnTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
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
    public DmResult<List<GrdMedicine_Mdl>> getSaleBillItemDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", conditionUnion = "", sqlSubQuery = "", joinSubQuery = "", quantityField = "";
        List<GrdMedicine_Mdl> listDetail = new ArrayList<>();
        List<GrdMedicine_Mdl> listDetailFinal = new ArrayList<>();
        GrdMedicine_Mdl node;
        TxtHMedicine_Mdl medicineMdl;
        boolean blnreturnRequest = false;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            /*==============================<< Get Item detail (via list) >>===============================*/
            for (KeyFieldExtended field : keyFields) {
                if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " or ";
                        sqlSubQuery += " union All \n";
                    }
                    condition += "  (sbd.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                            + " and  sbd.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "') ";

                    if (!conditionUnion.trim().equalsIgnoreCase("")) {
                        conditionUnion += " or ";
                    }
                    conditionUnion += "  (sbd.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                            + " and  sbd.freeWithItemCode = '" + DmShared.iNull(field.getSharedModifiedBy()).toString() + "' "
                            + " and (ISNULL(sbd.freeQty,0) - ISNULL(sbd.returnedFreeQty,0)) > 0 )";
                }

                if (!field.getSharedModifiedBy().toString().trim().equalsIgnoreCase("")) {
                    sqlSubQuery += "select " + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + " as docID, ";
                    sqlSubQuery += DmShared.iNull(field.getSharedKeyFieldName()).toString() + " as v_Sno, ";
                    sqlSubQuery += DmShared.vNull(field.getSharedModifiedBy()) + " as returnedQty \n";
                    blnreturnRequest = true;
                }

            }

            if (blnreturnRequest) {
                quantityField = " ,isNull(ret.returnedQty,0) ";
                joinSubQuery = " left Join ( "
                        + sqlSubQuery
                        + " ) as ret on sbd.docid=ret.docID and sbd.v_Sno=ret.v_Sno  ";
            } else {
                joinSubQuery = "";
                quantityField = " , (ISNULL(sbd.quantity,0) - ISNULL(sbd.returnedQty,0)) ";
            }

            sqlQuery = " SELECT  "
                    + "   sbd.itemCode as itemCode "
                    + " , im.name as itemName "
                    + " , sbd.ibCode as ibCode "
                    + " , sbd.batchNo as batchNo "
                    + " , sbd.expiryDate as expiryDate "
                    + " , sbd.rate as rate "
                    + " , sbd.mrp as mrp "
                    + " , sbd.skuMrp as skuMrp "
                    + " , sbd.skuRate as skuRate "
                    + " , sbd.manufacturingDate as manufacturingDate "
                    + " , sbd.expiryDate as expiryDate "
                    + " , sbd.oh_at_Discount as oh_at_Discount "
                    + " , sbd.oh_Amt_Discount as oh_Amt_Discount "
                    + " , sbd.oh_at_Discount2 as oh_at_Discount2 "
                    + " , sbd.oh_Amt_Discount2 as oh_Amt_Discount2 "
                    + " , sbd.oh_Amt_Gross as  oh_Amt_Gross "
                    + " , sbd.oh_Ac_SGST as  oh_Ac_SGST "
                    + " , sbd.oh_Ac_UGST as  oh_Ac_UGST "
                    + " , sbd.oh_Ac_CGST as  oh_Ac_CGST "
                    + " , sbd.oh_Ac_Sales as  oh_Ac_Sales "
                    + " , sbd.oh_Amt_Net as  oh_Amt_Net "
                    + " , sbd.oh_at_CGST as  oh_at_CGST "
                    + " , sbd.oh_Amt_CGST as  oh_Amt_CGST "
                    + " , sbd.oh_Amt_SGST as  oh_Amt_SGST "
                    + " , sbd.oh_at_SGST as  oh_at_SGST "
                    + " , sbd.oh_Amt_UGST as  oh_Amt_UGST "
                    + " , sbd.oh_at_UGST as  oh_at_UGST "
                    + " , sbd.oh_Amt_Taxable as  oh_Amt_Taxable "
                    + " , sbd.isReturn as  isReturn "
                    + " , sbd.docId as sbDocid "
                    + " , sbd.v_SNo as sbV_SNo "
                    + " , sb.recId as saleBillNo "
                    + quantityField + "  as quantity "
                    + " , im.sku as skuCode "
                    + " , um.unit as skuName "
                    + " , im.purchaseUnit as purchaseUnitCode"
                    + " , ump.unit as purchaseUnitName"
                    + " , im.saleUnit as saleUnitCode"
                    + " , ums.unit as saleUnitName"
                    + " , ump.decimalPlaces as allowedPurchaseDecimalPlaces "
                    + " , ums.decimalPlaces as allowedSaleDecimalPlaces "
                    + " , um.decimalPlaces as allowedSkuDecimalPlaces"
                    + " , im.purchaseUnitConversionRate as purchaseUnitConversionRate "
                    + " , im.saleUnitConversionRate as saleUnitConversionRate "
                    + " , im.isReturnableItem as isReturnableItem "
                    + " , im.allowNegativeStock as allowNegativeStock "
                    + " , im.isBarcodeActive "
                    + " , im.isSaleRateEditable as isSaleRateEditable "
                    + " , im.hsnCode as hsnCode"
                    + " , hm.hsn as hsnName "
                    + " , im.isBestBefore as isBestBefore"
                    + " , im.restrictGroup as restrictGroup"
                    + " , im.itemGSTNature as itemGSTNature"
                    + " , im.itsBasedOn as itsBasedOn"
                    + " , im.isDiscountAllow as isDiscountAllow"
                    + " , ums.gstUnitCode as gstUnitCode"
                    + " , sbd.barcode ,  sbd.grossPurchaseRate "
                    + "   from saleBill as sb "
                    + "   left join saleBillDetail as sbd on sb.docId = sbd.docId "
                    + "   left join itemMast as im on sbd.itemCode = im.code "
                    + "   left join unitMast as um on im.sku = um.code "
                    + "   left join unitMast as ump on im.purchaseUnit = ump.code   "
                    + "   left join unitMast as ums on im.saleUnit = ums.code   "
                    + "   left join hsnMast hm on hm.code = im.hsnCode "
                    + "   left join itemGroup ig on ig.code = im.itemGroup "
                    + joinSubQuery
                    + "   where sbd.isReturn = 0 "
                    + "   and sbd.isFooter = 0 "
                    + "   and im.isReturnableItem = 1 "
                    + "   and ISNULL(sbd.quantity,0) - ISNULL(sbd.returnedQty,0) > 0 "
                    + "   and sb.site_Code IN (" + STServer.getSsnPharmacyAllowSaleBillForSite(this) + ") "
                    + "   and sb.departmentCode= :departmentCode "
                    + "   and " + condition
                    + "   order by itemName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                medicineMdl = new TxtHMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setSbDocId(DmShared.xNull(rsTemp.getString("sbDocid")));
                node.setSbV_Sno(DmShared.xNull(rsTemp.getString("sbV_SNo")));
                node.setSbRecId(DmShared.xNull(rsTemp.getString("saleBillNo")));
                //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

//                medicineMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
//                medicineMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
//                medicineMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
//                medicineMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="TAXATION">
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setDiscount2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
                node.setTotalDiscount(DmServer.vNull((DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() + DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue()), "0.00"));
                node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                node.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                node.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                node.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                node.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                node.setSaleAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
                node.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                node.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                node.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                node.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
                node.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(DmShared.iNull(rsTemp.getString("itemCode")).toString(), 0, DmShared.xNull(rsTemp.getString("itsBasedOn")), v_Date, cnTemp);
                if (!DmShared.xNull(dmrIts.getValue1()).equalsIgnoreCase("")) {
                    node.setSaleAcCode(dmrIts.getValue1());
                }
//</editor-fold>
                if (DmShared.vNull(rsTemp.getString("oh_at_Discount")).doubleValue() == 0
                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() > 0) {
                    double tempDiscountPer = 0;
                    tempDiscountPer = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();
                    medicineMdl.setTempDiscountPer(DmServer.vNull(tempDiscountPer));
                }

                if (DmShared.vNull(rsTemp.getString("oh_at_Discount2")).doubleValue() == 0
                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue() > 0) {
                    double tempDiscountPer2 = 0;
                    tempDiscountPer2 = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();
                    node.setTempDiscountPer2(DmServer.vNull(tempDiscountPer2));
                }
                node.setQuantity(DmServer.vNull(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSaleDecimalPlaces")).intValue()))));
                node.setSbQuantity(DmServer.vNull(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSaleDecimalPlaces")).intValue()))));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue());
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSaleDecimalPlaces")).intValue());

                node.setMedicineMdl(medicineMdl);
                node.setGrossPurchaseRate(DmShared.xNull(rsTemp.getString("grossPurchaseRate")));

                listDetail.add(node);

            }
            rsTemp.close();
            psTemp.close();

            listDetailFinal.addAll(listDetail);
            listDetail = new ArrayList<>();

            sqlQuery = " SELECT  "
                    + "   sbd.itemCode as itemCode "
                    + " , im.name as itemName "
                    + " , sbd.ibCode as ibCode "
                    + " , sbd.batchNo as batchNo "
                    + " , sbd.expiryDate as expiryDate "
                    + " , (ISNULL(sbd.freeQty,0)- ISNULL(sbd.returnedFreeQty,0)) as freeQty "
                    + " , sbd.freeWithItemCode "
                    + " , sbd.rate as rate "
                    + " , sbd.mrp as mrp "
                    + " , sbd.skuMrp as skuMrp "
                    + " , sbd.skuRate as skuRate "
                    + " , sbd.manufacturingDate as manufacturingDate "
                    + " , sbd.expiryDate as expiryDate "
                    + " , sbd.oh_at_Discount as oh_at_Discount "
                    + " , sbd.oh_Amt_Discount as oh_Amt_Discount "
                    + " , sbd.oh_at_Discount2 as oh_at_Discount2 "
                    + " , sbd.oh_Amt_Discount2 as oh_Amt_Discount2 "
                    + " , sbd.oh_Amt_Gross as  oh_Amt_Gross "
                    + " , sbd.oh_Ac_SGST as  oh_Ac_SGST "
                    + " , sbd.oh_Ac_UGST as  oh_Ac_UGST "
                    + " , sbd.oh_Ac_CGST as  oh_Ac_CGST "
                    + " , sbd.oh_Ac_Sales as  oh_Ac_Sales "
                    + " , sbd.oh_Amt_Net as  oh_Amt_Net "
                    + " , sbd.oh_at_CGST as  oh_at_CGST "
                    + " , sbd.oh_Amt_CGST as  oh_Amt_CGST "
                    + " , sbd.oh_Amt_SGST as  oh_Amt_SGST "
                    + " , sbd.oh_at_SGST as  oh_at_SGST "
                    + " , sbd.oh_Amt_UGST as  oh_Amt_UGST "
                    + " , sbd.oh_at_UGST as  oh_at_UGST "
                    + " , sbd.oh_Amt_Taxable as  oh_Amt_Taxable "
                    + " , sbd.isReturn as  isReturn "
                    + " , sbd.docId as sbDocid "
                    + " , sbd.v_SNo as sbV_SNo "
                    + " , sb.recId as saleBillNo "
                    + " , 0 as quantity "
                    + " , im.sku as skuCode "
                    + " , um.unit as skuName "
                    + " , im.purchaseUnit as purchaseUnitCode"
                    + " , ump.unit as purchaseUnitName"
                    + " , im.saleUnit as saleUnitCode"
                    + " , ums.unit as saleUnitName"
                    + " , ump.decimalPlaces as allowedPurchaseDecimalPlaces "
                    + " , ums.decimalPlaces as allowedSaleDecimalPlaces "
                    + " , um.decimalPlaces as allowedSkuDecimalPlaces"
                    + " , im.purchaseUnitConversionRate as purchaseUnitConversionRate "
                    + " , im.saleUnitConversionRate as saleUnitConversionRate "
                    + " , im.isReturnableItem as isReturnableItem "
                    + " , im.allowNegativeStock as allowNegativeStock "
                    + " , im.isBarcodeActive "
                    + " , im.isSaleRateEditable as isSaleRateEditable "
                    + " , im.hsnCode as hsnCode"
                    + " , hm.hsn as hsnName "
                    + " , im.isBestBefore as isBestBefore"
                    + " , im.restrictGroup as restrictGroup"
                    + " , im.itemGSTNature as itemGSTNature"
                    + " , im.itsBasedOn as itsBasedOn"
                    + " , im.isDiscountAllow as isDiscountAllow"
                    + " , ums.gstUnitCode as gstUnitCode"
                    + " , sbd.barcode "
                    + "   from saleBill as sb "
                    + "   left join saleBillDetail as sbd on sb.docId = sbd.docId "
                    + "   left join itemMast as im on sbd.itemCode = im.code "
                    + "   left join unitMast as um on im.sku = um.code "
                    + "   left join unitMast as ump on im.purchaseUnit = ump.code   "
                    + "   left join unitMast as ums on im.saleUnit = ums.code   "
                    + "   left join hsnMast hm on hm.code = im.hsnCode "
                    + "   left join itemGroup ig on ig.code = im.itemGroup "
                    + "   where sbd.isReturn = 0 "
                    + "   and sbd.isFooter = 0 "
                    + "   and sb.site_Code IN (" + STServer.getSsnPharmacyAllowSaleBillForSite(this) + ") "
                    + "   and sb.departmentCode= :departmentCode "
                    + "   and " + conditionUnion
                    + "   order by itemName ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                medicineMdl = new TxtHMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setSbDocId(DmShared.xNull(rsTemp.getString("sbDocid")));
                node.setSbV_Sno(DmShared.xNull(rsTemp.getString("sbV_SNo")));
                node.setSbRecId(DmShared.xNull(rsTemp.getString("saleBillNo")));
                node.setSbFreeQuantity(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));

                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));

//                medicineMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
//                medicineMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
//                medicineMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
//                medicineMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="TAXATION">
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setDiscount2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
                node.setTotalDiscount(DmServer.vNull((DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() + DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue()), "0.00"));
                node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                node.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                node.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                node.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                node.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                node.setSaleAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
                node.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                node.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                node.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));
                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                node.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
                node.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(DmShared.iNull(rsTemp.getString("itemCode")).toString(), 0, DmShared.xNull(rsTemp.getString("itsBasedOn")), v_Date, cnTemp);
                if (!DmShared.xNull(dmrIts.getValue1()).equalsIgnoreCase("")) {
                    node.setSaleAcCode(dmrIts.getValue1());
                }
//</editor-fold>
                if (DmShared.vNull(rsTemp.getString("oh_at_Discount")).doubleValue() == 0
                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() > 0) {
                    double tempDiscountPer = 0;
                    tempDiscountPer = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();
                    medicineMdl.setTempDiscountPer(DmServer.vNull(tempDiscountPer, "0.00"));
                }
                if (DmShared.vNull(rsTemp.getString("oh_at_Discount2")).doubleValue() == 0
                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue() > 0) {
                    double tempDiscountPer2 = 0;
                    tempDiscountPer2 = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross2")).doubleValue();
                    node.setTempDiscountPer2(DmServer.vNull(tempDiscountPer2, "0.00"));
                }

                node.setQuantity(DmServer.vNull(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSaleDecimalPlaces")).intValue()))));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue());
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSaleDecimalPlaces")).intValue());
                node.setMedicineMdl(medicineMdl);

                listDetail.add(node);

            }
            rsTemp.close();
            psTemp.close();

            if (!listDetail.isEmpty()) {
                listDetailFinal.addAll(listDetail);
            }

            dmrTemp.setData(listDetailFinal);

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
    public DmResultPageing<List<TxtHCity_Mdl>> getTxtHCity(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCity_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCity_Mdl> lstTemp = new ArrayList<>();
        TxtHCity_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT "
                    + "   ct.cityCode"
                    + " , ct.cityName "
                    + " , sm.name AS stateName"
                    + " , sm.code AS stateCode"
                    + " , ctr.name AS countryName"
                    + " FROM city ct "
                    + " LEFT JOIN stateMast sm ON sm.code = ct.stateCode"
                    + " LEFT JOIN country ctr ON ctr.code = ct.countryCode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCity_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setCountryName(DmShared.xNull(rsTemp.getString("countryName")));
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
    public DmResult<PartySearch_Mdl> getLedger(String manualCode, String v_Date) {
        DmResult<PartySearch_Mdl> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        PartySearch_Mdl partyMdl;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            LedgerMaster_Mdl ledgerMast_Mdl = LedgerMasterServer.getRecordByManualCode(manualCode, DmServer.getSsnSiteCode(this), v_Date, cnTemp);
            partyMdl = new PartySearch_Mdl();
            partyMdl.setAddress(ledgerMast_Mdl.getAddress());
            partyMdl.setCityCode(ledgerMast_Mdl.getCityCode());
            partyMdl.setCityName(ledgerMast_Mdl.getCityName());
            partyMdl.setLmCode(ledgerMast_Mdl.getSharedKeyFieldCode());
            partyMdl.setLmName(ledgerMast_Mdl.getSharedKeyFieldName());
            partyMdl.setLmManualCode(ledgerMast_Mdl.getManualCode());
            partyMdl.setManualCode(ledgerMast_Mdl.getPartyManualCode());
            partyMdl.setMobile(ledgerMast_Mdl.getMobile());
            partyMdl.setPartTypeCode(ledgerMast_Mdl.getPartyTypeCode());
            partyMdl.setSharedKeyFieldName(ledgerMast_Mdl.getDisplayName());
            partyMdl.setSharedKeyFieldCode(ledgerMast_Mdl.getSubCode());
            partyMdl.setStateCode(ledgerMast_Mdl.getStateCode());
            partyMdl.setStateName(ledgerMast_Mdl.getStateName());
            partyMdl.setLmIdTypeCode(ledgerMast_Mdl.getIdTypeCode());
            partyMdl.setLmIdTypeName(ledgerMast_Mdl.getIdTypeName());
            partyMdl.setLmIdNo(ledgerMast_Mdl.getIdNo());

            dmrTemp.setData(partyMdl);

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
    public DmResult<String> getPharmacyBalance(String lmCode) {
        DmResult<String> dmrtemp = new DmResult<String>();
        String balance = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            balance = DmServer.vNull(STServer.getBalanceLedgerMast(lmCode, DmServer.getSsnSiteCode(this), cnTemp), "0.00");
            dmrtemp.setData(balance);
            dmrtemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            dmrtemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrtemp;
    }

    private String checkItemSale(String itemCode, String ibCode, String regDocid, String uhid, String lmCode, String prvQty, String prvFreeQty, Connection cnTemp) throws SQLException {
        DmPreparedStatement psTemp = null;
        String sqlQuery, returnValue = "";
        ResultSet rsTemp = null;

        sqlQuery = " Select sbd.ibCode ";
        if (DmShared.vNull(prvQty).doubleValue() == 0) {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ", " + getVoucherCategory(pvc.PHARMACY_CASH_SALE_RETURN) + ") THEN 0 - ISNULL(sbd.quantity,0) ELSE sbd.quantity END) as quantity ";
        } else {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ", " + getVoucherCategory(pvc.PHARMACY_CASH_SALE_RETURN) + ") THEN 0 - ISNULL(sbd.quantity,0) ELSE sbd.quantity END)  + " + Math.abs(DmShared.vNull(prvQty).doubleValue()) + " as quantity ";
        }
        if (DmShared.vNull(prvFreeQty).doubleValue() == 0) {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ", " + getVoucherCategory(pvc.PHARMACY_CASH_SALE_RETURN) + ") THEN 0 - ISNULL(sbd.freeQty,0) ELSE sbd.freeQty END) as freeQty ";
        } else {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ", " + getVoucherCategory(pvc.PHARMACY_CASH_SALE_RETURN) + ") THEN 0 - ISNULL(sbd.freeQty,0) ELSE sbd.freeQty END)  + " + Math.abs(DmShared.vNull(prvFreeQty).doubleValue()) + " as freeQty ";
        }

        sqlQuery += " FROM SaleBill sb "
                + " INNER JOIN saleBillDetail sbd On sb.docid=sbd.docid "
                + " INNER JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
                + " WHERE sbd.itemCode = :itemCode "
                + " AND sbd.ibCode = :ibCode "
                + " AND sb.isCancelled = 0 ";
//        if (DmShared.iNull(regDocid).longValue() != 0) {
//            sqlQuery += " AND sb.regDocId = :regDocId ";
//        } else 
        if (DmShared.iNull(uhid).longValue() != 0) {
            sqlQuery += " AND sb.uhid = :uhid ";
        } else {
            sqlQuery += " AND sb.lmCode = :lmCode ";
        }
        sqlQuery += " AND vt.category IN (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_CASH) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ") "
                + " GROUP BY sbd.ibCode ";
//        if (DmShared.iNull(regDocid).longValue() != 0) {
//            sqlQuery += " , sb.regDocId ";
//        } else 
        if (DmShared.iNull(uhid).longValue() != 0) {
            sqlQuery += " , sb.uhid ";
        } else {
            sqlQuery += " , sb.lmCode ";
        }
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
        psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
//        if (DmShared.iNull(regDocid).longValue() != 0) {
//            psTemp.setString("regDocId", DmShared.iNull(regDocid).toString());
//        } else 
        if (DmShared.iNull(uhid).longValue() != 0) {
            psTemp.setString("uhid", DmShared.iNull(uhid).toString());
        } else {
            psTemp.setString("lmCode", DmShared.iNull(lmCode).toString());
        }
        rsTemp = psTemp.executeQuery();
        if (rsTemp.next()) {
            returnValue = DmShared.vNull(rsTemp.getDouble("quantity") + rsTemp.getDouble("freeQty")).toString();
        }
        rsTemp.close();
        psTemp.close();

        return returnValue;
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

    private CompareProperties_Mdl iniItemBatchProperties(SaleBillReturn_Mdl mrTempCurrent, SaleBillReturn_Mdl mrTempPrevious) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();

        lstItemBatch_MdlCurnt.clear();
        for (GrdMedicine_Mdl detail_MdlCurrent : mrTempCurrent.getGrdMediDetails()) {
            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                batch_Mdl.setReceiveQty(DmServer.vNull(DmShared.vNull(detail_MdlCurrent.getQuantitySKU())));
                batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
                lstItemBatch_MdlCurnt.add(batch_Mdl);
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdMedicine_Mdl detail_MdlPrev : mrTempPrevious.getGrdMediDetails()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                    batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                    batch_MdlPrev.setReceiveQty(DmServer.vNull(DmShared.vNull(detail_MdlPrev.getQuantitySKU())));
                    batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
                    batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
                    lstItemBatch_MdlPrev.add(batch_MdlPrev);
                }
            }
        }

        compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
        compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
        compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdSaleBillReturn().getPrefix()));
        compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdSaleBillReturn().getCounter().toString()));
        compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
//        compareProperties_Mdl.setDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
        compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
        }
        return compareProperties_Mdl;
    }

    @Override
    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, MedicineSaleOn saleOn) {
        DmResult<BarCode_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(Pharmacy_Server.barCodeSearch(barCode, voucherDate, departmentCode, siteCode, ItemBatchType.NON_EXPIRED_BATCH, false, cnTemp, saleOn));
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
    public DmResultPageing<List<MedicineReturnRequest_Mdl>> getMedicineReturnRequests(String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<MedicineReturnRequest_Mdl>> rstTemp = new DmResultPageing<>();
        List<MedicineReturnRequest_Mdl> lstTemp = new ArrayList<>();
        MedicineReturnRequest_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select sbr.docID,sbr.recId,sbr.v_Date,pu.patientName,pr.recId as registrationNo, \n"
                    + "         pu.recID as uhID,sbr.oh_Amt_Net as amount,pr.regDocId,pu.docId as uhidDocID, \n"
                    + "         sbr.gstReasonCode,grm.name as gstReasonName \n "
                    + "         from saleBillReturn sbr  \n"
                    + "         left join patientRegistration pr on sbr.regDocId=pr.regDocId \n"
                    + "         left join patientUHID pu on pr.uhid=pu.docid  \n"
                    + "         left join gstReasonMast grm on sbr.gstReasonCode=grm.code \n"
                    + "         where isNull(sbr.returnDocID,0)=0 "
                    + "         and sbr.departmentCode=:departmentCode \n"
                    + "         and sbr.site_Code= :site_Code "
                    + "         AND  isNull(SBR.isCancelled,0)<>1 ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("departmentCode", DmShared.xNull(departmentCode));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new MedicineReturnRequest_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docID")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setReturnDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setPatientRegID(DmShared.xNull(rsTemp.getString("registrationNo")));
                listNode.setPatientRegDocID(DmShared.xNull(rsTemp.getString("regDocId")));
                listNode.setPatientUHID(DmShared.xNull(rsTemp.getString("uhID")));
                listNode.setPatientUHIDDocID(DmShared.xNull(rsTemp.getString("uhidDocID")));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                listNode.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                listNode.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<List<KeyFieldExtended>> getReturnRequestDetail(String returnRequestDocID) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(returnRequestDocID).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Invalid Data..");
            }

            sqlQuery = " select sb.docID as returnRequestDocID,sbd.sbDocID,sbd.sbV_Sno,sbd.skuQuantity,sb.regDocID \n "
                    + "		from saleBillReturn sb \n "
                    + "		left join saleBillReturnDetail sbd on sb.docID=sbd.DocID \n "
                    + "		left join gstReasonMast grm on sb.gstReasonCode=grm.Code \n "
                    + "		where sb.docID=:returnRequestDocID ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            psTemp.setString("returnRequestDocID", DmShared.xNull(returnRequestDocID));

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("sbDocID")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("sbV_Sno")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("regDocID")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("skuQuantity")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<Boolean> getrefunddocId(String refunddocId) {
        DmResult<Boolean> dmrtemp = new DmResult<Boolean>();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(refunddocId).trim().equalsIgnoreCase("")) {
                dmrtemp.setData(true);
            } else {
                dmrtemp.setData(false);
            }

            dmrtemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            dmrtemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrtemp;
    }

    @Override
    public DmResult<Boolean> isEodExist(String entryDate) {
        /*================< Check whether eod has been initialized or not >================*/
        DmResult<Boolean> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        String sqlQuery;
        Boolean existEod = false;
        dmrTemp.setData(false);
        try {
            sqlQuery = "Select count(sb.docid) as cnt "
                    + " from saleBill sb "
                    + " left join saleBillDetail sbd on sbd.docid=sb.docid "
                    + " left join voucher_Type vt on sb.v_Type=vt.v_Type "
                    + " where isNull(sbd.eodSbDocId,'')<>'' "
                    + " and v_Date='" + DmServer.convertDate(entryDate) + "'"
                    + " and site_Code=" + DmServer.getSsnSiteCode(this) + " and isCancelled=0 "
                    + " and vt.category IN(" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + " ) ";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    existEod = true;
                }

            }
            rsTemp.close();
            psTemp.close();
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setData(existEod);
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
