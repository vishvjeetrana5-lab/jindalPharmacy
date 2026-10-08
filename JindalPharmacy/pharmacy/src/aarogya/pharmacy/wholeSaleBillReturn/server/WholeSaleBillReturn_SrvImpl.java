package aarogya.pharmacy.wholeSaleBillReturn.server;

import aarogya.pharmacy.ledgerMaster.server.LedgerMasterServer;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.self.server.Pharmacy_Server;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.Default_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.Find_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.ItemAccountPosting_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.MedicineReturnRequest_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.SearchUHID_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHMedicine_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHSaleBillNo_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHVType_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHVendor_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.WholeSaleBillReturn_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.WholeSaleBillReturn_Srv;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_Server;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.cardPaymentPopup.shared.TxtHReceiptType_VList_Mdl;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.RoundOffService_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemBatchType;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import aarogya.selfToolkit.stStatic.shared.STEnum.WholeSalePaymentMode;
import aarogya.selfToolkit.uhidPopup.shared.Layout_Mdl;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.StockEntryType;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.eGstPopup.server.EGstPopup_Server;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.EGstDetail_Mdl;
import dataman.dmToolkit.gst.shared.EGstToken_Mdl;
import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.gst.shared.Gst_Shared;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.gst.shared.Party_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.partyDetailPopup.server.PartyDetailPopup_Server;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.accountAdjustment.shared.AdjustmentSave_Mdl;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;

public class WholeSaleBillReturn_SrvImpl extends DmRSSPrivate implements WholeSaleBillReturn_Srv {

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

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    @Override
    public DmResult<WholeSaleBillReturn_Mdl> getMoveRec(String strKey) {
        DmResult<WholeSaleBillReturn_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        WholeSaleBillReturn_Mdl mrTemp = new WholeSaleBillReturn_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = getMoveRec(strKey, cnTemp);
            dmrTemp.setData(mrTemp);

            cnTemp.close();
            dmrTemp.setStatus(true);
            DmServer.setSsnOldData(getClass().getName(), mrTemp, this);

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

    private WholeSaleBillReturn_Mdl getMoveRec(String strKey, Connection cnTemp) throws Exception {
        DmResult<WholeSaleBillReturn_Mdl> dmrTemp = new DmResult();
        Connection cnTempHIS = null;
        CardPaymentPopup_Mdl cardPaymentPopup_SaleBillReturn_Mdl = new CardPaymentPopup_Mdl();
        WholeSaleBillReturn_Mdl mrTemp = new WholeSaleBillReturn_Mdl();
        dmrTemp.setData(mrTemp);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TxtHMedicine_Mdl medicineMdl;

        try {
            sqlQuery = "SELECT mb.docId, mb.recId, mb.v_Type, pbvt.description,pbvt.short_Name,pbvt.category as voucherCategory, mb.v_Date, mb.subCode, "
                    + "     mb.departmentCode, sdm.name AS departmentName, lm.initial as patientInitial, sg.displayName as patientName, "
                    + "     mb.preparedBy, mb.u_Name, mb.remark, mb.oh_Amt_Gross, mb.oh_Amt_Net, mb.oh_Amt_LandedValue, "
                    + "     mb.oh_Amt_BAMT, mb.oh_Amt_RoundOff, mb.oh_Amt_CGST, mb.oh_Amt_SGST, mb.oh_Amt_UGST, mb.oh_Amt_IGST, mb.oh_Amt_Taxable, mb.oh_Amt_Discount, "
                    + "     mb.recIdPrefix, mb.v_No ,prec.docId as refundDocId, mb.isCancelled, "
                    + "     mb.u_EntDt_LatestLine,mb.refNo,mb.refDate,mb.gstReasonCode,grm.name as gstReasonName "
                    + "     ,mb.partyType,mb.partySubCode"
                    + "     , mb.oh_Amt_Discount2  , mb.paymentSource,sbr.returnDocID "
                    + "     , mb.vehicleNo, mb.transporterCode , sgtp.name as transporterName"
                    + "     , mb.transportMode , mb.lrNo , mb.lrDate , mb.eWayBillDt , mb.eWayBillNo "
                    + "     , mb.locationCode, lo.name as locationName, mb.vehicleTypeCode , vtm.name as vehicleTypeName, mb.placeOfsupply as stateCode \n"
                    + "     , sm.name as stateName, egt.mongoLinkId, mb.eInvIrnNo, sg.gsTin,sg.gstinDate, mb.profitPercentage "
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
                    + "  LEFT JOIN subgroup sg ON sg.subCode=mb.partySubCode  "
                    + "  LEFT JOIN consultantmast cm ON mb.consultantCode=cm.subCode  "
                    + "  LEFT JOIN city AS c ON c.citycode = lm.citycode "
                    + "  LEFT JOIN stateMast AS sm ON sm.code = mb.placeOfsupply "
                    + "  LEFT JOIN subGroup AS sgcon ON cm.subCode = sgcon.subCode  "
                    + "  LEFT JOIN patientReceipt AS prec ON mb.DocId = prec.docId "
                    + "	 LEFT JOIN hl7Mast as idType on idType.code = mb.idTypeCode "
                    + "	 LEFT JOIN voucher_type vtprec ON prec.v_type = vtprec.v_type "
                    + "  LEFT JOIN locationMast lo on lo.code=mb.locationCode \n"
                    + "	 LEFT JOIN subGroup sgtp ON sgtp.subCode = mb.transporterCode\n"
                    + "  left join vehicleTypeMast vtm on vtm.code = mb.vehicleTypeCode \n"
                    + "  left join eGst egt on egt.docid = mb.docid"
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
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setPatientIntial(DmShared.xNull(rsTemp.getString("patientInitial")));
                mrTemp.setDebtorName(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setRefundDocId(DmShared.xNull(rsTemp.getString("refundDocId")));
                mrTemp.setPartySubCode(DmShared.xNull(rsTemp.getString("partySubCode")));
                mrTemp.setPartyGstin(DmShared.xNull(rsTemp.getString("gsTin")));
                mrTemp.setPartyGstinDate(DmShared.xNull(rsTemp.getString("gstinDate")));

                mrTemp.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                mrTemp.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));

                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setPlaceOfSupplyName(DmShared.xNull(rsTemp.getString("stateName")));
                mrTemp.setMongoLinkId(DmShared.xNull(rsTemp.getString("mongoLinkId")));
                if (!DmShared.xNull(rsTemp.getString("transportMode")).trim().equalsIgnoreCase("")) {
                    mrTemp.setTransportModeName(DmShared.xNull(DmEnum.TransportMode.getViaKey(DmShared.iNull(rsTemp.getString("transportMode")).intValue()).getValue()));
                    mrTemp.setTransportModeCode(DmShared.xNull(rsTemp.getString("transportMode")));
                }
                mrTemp.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                mrTemp.setTransporterName(DmShared.xNull(rsTemp.getString("transporterName")));
                mrTemp.setTransporterCode(DmShared.xNull(rsTemp.getString("transporterCode")));
                mrTemp.seteWayBillNo(DmShared.xNull(rsTemp.getString("eWayBillNo")));
                mrTemp.setLrNo(DmShared.xNull(rsTemp.getString("lrNo")));
                mrTemp.setLrDate(DmShared.dNull(rsTemp.getDate("lrDate")));
                if (rsTemp.getTimestamp("eWayBillDt") != null) {
                    mrTemp.seteWayBillDateTime(Gst_Server.getDateTime(rsTemp.getTimestamp("eWayBillDt"), "dd/MMM/yyyy"));
                }
                mrTemp.setVehicleTypeCode(DmShared.xNull(rsTemp.getString("vehicleTypeCode")));
                mrTemp.setVehicleTypeName(DmShared.xNull(rsTemp.getString("vehicleTypeName")));
                mrTemp.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                mrTemp.setLocationName(DmShared.xNull(rsTemp.getString("locationName")));

                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.seteInvIrnNo(DmShared.xNull(rsTemp.getString("eInvIrnNo")));
                mrTemp.setProfitPercentage(DmServer.vNull(rsTemp.getString("profitPercentage")));

                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));

                mrTemp.setGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.setDiscountAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                mrTemp.setDiscountAmt2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
                mrTemp.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                mrTemp.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                mrTemp.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                mrTemp.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                mrTemp.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                mrTemp.setGstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_IGST")).doubleValue() + DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));
                mrTemp.setBillAmt(DmServer.vNull(rsTemp.getString("oh_Amt_BAMT"), "0.00"));
                mrTemp.setRoundOffAmt(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                mrTemp.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                mrTemp.setLandedValue(DmServer.vNull(rsTemp.getString("oh_Amt_LandedValue"), "0.00"));
                mrTemp.setCancelRecord(DmShared.iNull(rsTemp.getString("isCancelled")).toString());
                mrTemp.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
                mrTemp.setMedicineReturnRequestDocID(DmShared.xNull(rsTemp.getString("returnDocID")));

                mrTemp.setPaymentSourceName(PaymentSource.getValueOf(DmShared.iNull(rsTemp.getString("paymentSource")).intValue()).getEnumName());

            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select mbd.docId, mbd.v_SNo, mm.isBestBefore, mbd.hsnCode,hsn.hsn AS hsnNo, mbd.itemGSTNature, mbd.itemCode, mm.name AS itemName, mbd.quantity, "
                    + "         mbd.skuQuantity, mbd.rate, mbd.oh_at_Discount, mbd.oh_Amt_Discount, mbd.oh_at_Discount2, mbd.oh_Amt_Discount2, um.decimalPlaces as skuDecimalPlaces, "
                    + "         mbd.oh_Amt_Net, mbd.oh_Amt_Gross, mbd.oh_at_CGST, mbd.isFooter, mm.sku, um.unit AS skuName, ump.gstUnitCode, "
                    + "         mbd.sbDocId, mbl.recId AS saleBillNo,mbd.sbV_Sno,mbl.v_type as saleBillVType,mbl.oh_Amt_Net as netAmtSaleBill,"
                    + "         mbd.saleUnitCode AS purchaseUnit, "
                    + "         ump.decimalPlaces, mbd.oh_Amt_CGST, mbd.oh_Amt_SGST, mbd.oh_at_SGST, mbd.oh_Amt_Taxable, mbd.oh_Amt_UGST, "
                    + "         mbd.oh_at_UGST, mbd.mrp, mbd.skuRate, mbd.skuMrp, mm.allowNegativeStock, mm.isBarcodeActive, mbd.isReturn, mbd.oh_Ac_CGST, "
                    + "         mbd.oh_Ac_SGST, mbd.oh_Ac_UGST, mbd.oh_Ac_Sales, mbd.landedSkuValue, mm.itsBasedOn, mbd.batchNo, mbd.expiryDate, mbd.manufacturingDate, "
                    + "         mbd.ibCode, mm.restrictGroup, ump.unit purchaseUnitName, mbd.skuQtyForRate,mbd.freeQty as freeQtyReturned, "
                    + "         CASE WHEN IsNull(smbl.quantity,0) <> 0 THEN (IsNull(smbl.quantity,0)-IsNull(smbl.returnedQty,0)+IsNull(mbd.quantity,0)) ELSE 0 END as sbQuantity, "
                    + "         CASE WHEN IsNull(smbl.freeQty,0) <> 0 THEN (IsNull(smbl.freeQty,0)-IsNull(smbl.returnedFreeQty,0)+IsNull(mbd.freeQty,0)) ELSE 0 END as sbFreeQuantity, "
                    + "         mbd.barcode, mbd.oh_Ac_IGST, mbd.oh_Amt_IGST, mbd.oh_at_IGST,mbd.wholeSaleRate , mbd.wholeSaleRateActual "
                    + "	from saleBillDetail AS mbd "
                    + "	left join saleBill AS mbl ON mbd.sbDocId = mbl.docId "
                    + "	left join saleBillDetail AS smbl ON mbd.sbDocId = smbl.docId and mbd.sbV_SNo=smbl.V_SNo"
                    + "	left join itemMast AS mm ON mbd.itemCode = mm.code "
                    + "	left join hsnMast AS hsn ON hsn.code = mbd.hsnCode "
                    + "	left join unitMast AS um ON mm.sku = um.code "
                    + "	left join unitMast AS ump ON ump.code = mbd.saleUnitCode "
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
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsnNo")));
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setGstUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                if (DmShared.vNull(rsTemp.getString("oh_at_Discount")).doubleValue() == 0
                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() > 0) {
                    double DiscountPer = 0;
                    DiscountPer = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();
                    medicineMdl.setDiscountPer(DmServer.vNull(DiscountPer));
                }
//                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                detailMdl.setMedicineMdl(medicineMdl);

                detailMdl.setFreeQty(DmServer.vNull(rsTemp.getString("freeQtyReturned")));
                detailMdl.setPrvFreeQuantity(DmServer.vNull(rsTemp.getString("freeQtyReturned")));
                detailMdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setPrvQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
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

                detailMdl.setIgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST")));

                detailMdl.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                detailMdl.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                detailMdl.setWholeSaleRate(DmServer.vNull(rsTemp.getString("wholeSaleRate")));
                detailMdl.setWholeSaleRateActual(DmServer.vNull(rsTemp.getString("wholeSaleRateActual")));

                detailMdl.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()) + DmShared.vNull(rsTemp.getString("oh_at_IGST")).doubleValue(), "0.00"));
                detailMdl.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()) + DmShared.vNull(rsTemp.getString("oh_Amt_IGST")).doubleValue(), "0.00"));

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

            /*==========================Get transPartyInfo Details=============================*/
            mrTemp.getPartyDetail_Mdl().putAll(PartyDetailPopup_Server.getPartyTransRecord(strKey, cnTemp));

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            DmServer.setSsnOldData(getClass().getName(), mrTemp, this);

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
        return mrTemp;

    }

    @Override
    public DmResult<String> deleteRecord(String keyCode, ModifyReason_Mdl reason_Mdl, String sbDocId, String voucherShortName) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);
        WholeSaleBillReturn_Mdl mrTemp;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select isCancelled, site_Code, v_Prefix from saleBill where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

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
            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(keyCode, cnTemp);
            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(keyCode, cnTemp);

            sqlQuery = "update patientReceipt set isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            sqlQuery = "update patientServiceDetail set isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            sqlQuery = "update saleBill set cancelledBy = :cancelledBy, cancelledDt = :cancelledDt, isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.setString("cancelledBy", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("cancelledDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();

            sqlQuery = "update saleBillReturn set returnDocID = null Where returnDocID= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            sqlQuery = "update saleBillDetail set sbDocId = null,sbV_Sno = null Where docId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            CardPaymentPopup_Server.cancel(keyCode, cnTemp);

            if (reason_Mdl != null) {
                saveLog(keyCode, reason_Mdl, null, DmEnum.DmTransactionMode.DELETE, null);
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
                    }
                    if (srFreeQty > 0) {
                        sbReturnedFreeQty -= srFreeQty;
                    }
                } else {
                    if (srQty > 0) {
                        sbReturnedQty += srQty;
                    }
                    if (srFreeQty > 0) {
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
    public DmResult<String> saveRecord(String voucherCategory, WholeSaleBillReturn_Mdl mrTemp, WholeSaleBillReturn_Mdl prevMdl, String voucherPrefix, String returnRequestDocID, boolean saveCardInfo, boolean saveModifyReason) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;

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
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="Check for SaleBill item change/cancellation">
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
            //</editor-fold>

            //</editor-fold>
            for (GrdMedicine_Mdl detail_Mdl : mrTemp.getGrdMediDetails()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    if (DmShared.convertDate(DmShared.dNull(detail_Mdl.getExpDate())).getTime() < DmShared.convertDate(DmShared.dNull(DmServer.getServerDateTime()[0])).getTime()) {
                        throw new DmException(0, "Medicine" + detail_Mdl.getSharedKeyFieldName() + "has been expired, " + "Can't be Return");
                    }
                }
            }

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

            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(mrTemp.getSharedKeyFieldCode(), cnTemp);

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
                        saleQty = checkItemSale(mdl.getSharedKeyFieldCode(), mdl.getIbCode(), mdl.getPrvQuantity(), mdl.getPrvFreeQuantity(), cnTemp);
                        if (DmShared.vNull(saleQty).doubleValue() > 0) {
                            if ((DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQty()).doubleValue()) > DmShared.vNull(saleQty).doubleValue()) {
                                throw new DmException(0, mdl.getSharedKeyFieldName() + " with Batch No. '" + mdl.getBatchNo() + "' has Return Quantity : " + (DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQty()).doubleValue()) + " more than Sale Quantity :  " + saleQty);
                            }
                        } else {
                            throw new DmException(0, mdl.getSharedKeyFieldName() + " with Batch No. '" + mdl.getBatchNo() + "', never sold to/already returned by, " + mrTemp.getDebtorName());
                        }
                    }
                }
            }
            //</editor-fold>

            if (blnAdd) {
                //<editor-fold defaultstate="collapsed" desc="INSERT">
                sqlQuery = " insert into saleBill (docId, v_Type, v_No, recId, v_Prefix, v_Date,v_Time, "
                        + "     departmentCode, remark, u_EntDt, preparedDate, preparedBy, site_Code, oh_Amt_Gross, oh_Amt_Net, oh_Amt_BAMT, "
                        + "     oh_Amt_RoundOff, oh_Amt_CGST, oh_Amt_SGST, oh_Amt_Taxable, oh_Amt_Discount, recIdPrefix, oh_Amt_UGST,"
                        + "     u_EntDt_LatestLine,refNo,refDate,partyType,subCode,consignee,"
                        + "     placeOfSupply,gstReasonCode,oh_Amt_Discount2, oh_Amt_LandedValue,"
                        + "     sBillType, transporterCode, transportMode,"
                        + "     lrNo, lrDate, vehicleNo, vehicleTypeCode, locationCode, oh_Amt_IGST ,partySubCode, profitPercentage)"
                        + " values (:docId, :vType, :vNo, :recId, :vPrefix, :vDate,:v_Time, :departmentCode,"
                        + "      :remark, :uEntDt, :uEntDt, :userName, :siteCode, :ohAmtGross, :ohAmtNet, :ohAmtBAMT, :oh_Amt_RoundOff, "
                        + "      :ohAmtCGST, :ohAmtSGST, :ohAmtTaxable, :ohAmtDiscount, :recIdPrefix, :ohAmtUGST,"
                        + "      :uEntDt,:refNo,:refDate,:partyType,:subCode,:consignee,"
                        + "      :placeOfSupply,:gstReasonCode,:oh_Amt_Discount2, :oh_Amt_LandedValue,"
                        + "      :sBillType, :transporterCode, :transportMode,"
                        + "      :lrNo, :lrDate, :vehicleNo, :vehicleTypeCode, :locationCode, :oh_Amt_IGST,:partySubCode, :profitPercentage)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTemp.getGstReasonCode()));
                psTemp.setString("vNo", DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                psTemp.setString("vPrefix", voucherPrefix);

                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("userName", DmShared.xNull(userName));
                psTemp.setString("siteCode", DmShared.xNull(siteCode));

                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyType()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTemp.getTransporterCode()));
                psTemp.setString("lrNo", DmShared.foriegnKey(mrTemp.getLrNo()));
                psTemp.setDate("lrDate", DmServer.convertDate(mrTemp.getLrDate()));
                psTemp.setString("vehicleNo", DmShared.foriegnKey(mrTemp.getVehicleNo()));
                psTemp.setString("transportMode", DmShared.foriegnKey(mrTemp.getTransportModeCode()));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("vehicleTypeCode", DmShared.foriegnKey(mrTemp.getVehicleTypeCode()));
                psTemp.setString("sBillType", DmShared.xNull(mrTemp.getsBillType()));

                psTemp.setString("recIdPrefix", mrTemp.getRecIdSaleBillReturn().getPrefix());
                psTemp.setString("ohAmtGross", DmServer.vNull(mrTemp.getGrossAmt(), "0.00"));
                psTemp.setString("ohAmtDiscount", DmServer.vNull(mrTemp.getDiscountAmt(), "0.00"));
                psTemp.setString("oh_Amt_Discount2", DmServer.vNull(mrTemp.getDiscountAmt2(), "0.00"));
                psTemp.setString("ohAmtCGST", DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                psTemp.setString("ohAmtSGST", DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                psTemp.setString("ohAmtUGST", DmServer.vNull(mrTemp.getUgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTemp.getIgstAmt(), "0.00"));
                psTemp.setString("ohAmtTaxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("ohAmtNet", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("ohAmtBAMT", DmServer.vNull(mrTemp.getBillAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("partySubCode", DmShared.xNull(mrTemp.getPartySubCode()));
                psTemp.setString("profitPercentage", DmServer.vNull(mrTemp.getProfitPercentage()));

//</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="UPDATE">
                sqlQuery = "UPDATE saleBill SET "
                        + " v_Date = :vDate, "
                        + " u_EntDt = :uEntDt, "
                        + " u_EntDt_LatestLine= :uEntDt ,"
                        + " departmentCode = :departmentCode,"
                        + " subCode= :subCode ,"
                        + " consignee= :consignee ,"
                        + " placeOfSupply= :placeOfSupply ,"
                        + " remark=:remark,"
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
                        + " oh_Amt_IGST=:oh_Amt_IGST,"
                        + " refNo=:refNo,"
                        + " partySubCode=:partySubCode,"
                        + " refDate=:refDate,"
                        + " gstReasonCode=:gstReasonCode,"
                        + " u_Name = :userName, "
                        + " transporterCode=:transporterCode,"
                        + " transportMode=:transportMode,"
                        + " lrNo=:lrNo,"
                        + " lrDate=:lrDate,"
                        + " vehicleNo=:vehicleNo,"
                        + " vehicleTypeCode=:vehicleTypeCode,"
                        + " profitPercentage=:profitPercentage,"
                        + " locationCode=:locationCode "
                        + " WHERE DocID = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTemp.getGstReasonCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTemp.getDiscountAmt(), "0.00"));
                psTemp.setString("oh_Amt_Discount2", DmServer.vNull(mrTemp.getDiscountAmt2(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTemp.getUgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTemp.getIgstAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("oh_Amt_BAMT", DmServer.vNull(mrTemp.getBillAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTemp.getTransporterCode()));
                psTemp.setString("lrNo", DmShared.foriegnKey(mrTemp.getLrNo()));
                psTemp.setDate("lrDate", DmServer.convertDate(mrTemp.getLrDate()));
                psTemp.setString("vehicleNo", DmShared.foriegnKey(mrTemp.getVehicleNo()));
                psTemp.setString("transportMode", DmShared.foriegnKey(mrTemp.getTransportModeCode()));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("vehicleTypeCode", DmShared.foriegnKey(mrTemp.getVehicleTypeCode()));
                psTemp.setString("partySubCode", DmShared.xNull(mrTemp.getPartySubCode()));
                psTemp.setString("profitPercentage", DmServer.vNull(mrTemp.getProfitPercentage()));

                //</editor-fold>
            }
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Validate Removed Received Items">
            if (!blnAdd) {
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), StockEntryType.OTHER, cnTemp, this);
            }
            //</editor-fold>

            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp, true);
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);

            /*==============================<< Manage saleBILL (Returned Qty) >>==============================*/
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
                            + "         ibCode, skuMrp, skuRate, skuQtyForRate,sbDocId,sbV_SNo,freeQty,landedSkuValue, barcode,grossPurchaseRate,oh_Ac_IGST,oh_Amt_IGST,oh_at_IGST,wholeSaleRate,wholeSaleRateActual)"
                            + "         values (:docId, :v_sNo, :itemCode, :hsnCode, :saleUnitCode, :quantity,"
                            + "		:oh_at_Discount, :oh_Amt_Discount, :oh_at_Discount2, :oh_Amt_Discount2, :oh_Amt_Net, :oh_Amt_CGST, :oh_at_CGST, :oh_Amt_SGST, :oh_at_SGST, "
                            + "		:oh_at_UGST, :oh_Amt_UGST, :rate, :oh_Amt_Taxable, :itemGSTNature, :mrp, :skuQuantity, :oh_Ac_Sales, "
                            + "         :oh_Ac_CGST, :oh_Ac_UGST, :oh_Ac_SGST, :oh_Amt_Gross, :batchNo, :manufacturingDate, :expiryDate, :ibCode, :skuMrp, "
                            + "         :skuRate, :skuQtyForRate,:sbDocId,:sbV_SNo,:freeQty, :landedSkuValue, :barcode,:grossPurchaseRate,:oh_Ac_IGST,:oh_Amt_IGST,:oh_at_IGST,:wholeSaleRate,:wholeSaleRateActual)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSno()).toString());
                    psTemp.setString("itemCode", medicineMdl.getSharedKeyFieldCode());
                    psTemp.setString("hsnCode", DmShared.foriegnKey(medicineMdl.getHsnCode()));
                    psTemp.setString("itemGSTNature", DmShared.xNull(medicineMdl.getItemGstNature()));
                    psTemp.setString("saleUnitCode", DmShared.xNull(medicineMdl.getPurchaseUnit()));
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

                    psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getIgstAmt(), "0.00"));
                    psTemp.setString("oh_at_IGST", DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));
                    psTemp.setString("oh_Ac_IGST", DmShared.foriegnKey(detail_Mdl.getIgstAcCode()));
                    psTemp.setString("wholeSaleRate", DmShared.xNull(detail_Mdl.getWholeSaleRate()));
                    psTemp.setString("wholeSaleRateActual", DmShared.xNull(detail_Mdl.getWholeSaleRateActual()));

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
                    stock_Mdl.setRemark("Patient: " + DmShared.xNull(mrTemp.getDebtorName()));

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
                    gstTrans_Mdl.setGstUnitCode(medicineMdl.getGstUnit());
                    gstTrans_Mdl.setHsnCode(medicineMdl.getHsnCode());

                    gstTrans_Mdl.setPartyCode(mrTemp.getPartySubCode());
                    gstTrans_Mdl.setConsignee(mrTemp.getPartySubCode());
                    gstTrans_Mdl.setPartyType(mrTemp.getPartyType());
                    gstTrans_Mdl.setGstin(mrTemp.getPartyGstin());
                    gstTrans_Mdl.setGstinDate(mrTemp.getPartyGstinDate());
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
                    gstTrans_Mdl.setItemIgstRate(DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));
                    gstTrans_Mdl.setItemIgstAmount(DmServer.vNull(detail_Mdl.getIgstAmt(), "0.00"));
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
            //<editor-fold defaultstate="collapsed" desc="SavePartyTransRecord">
            if (mrTemp.getPartyDetail_Mdl() != null) {
                mrTemp.getPartyDetail_Mdl().put(DmEnum.PartyTransRecordType.TRANSACTION_SITE.getCode(), PartyDetailPopup_Server.getSiteDetailPopup_Mdl(DmServer.getSsnSiteCode(this), cnTemp));
                mrTemp.getPartyDetail_Mdl().put(DmEnum.PartyTransRecordType.DISPATCH_FROM.getCode(), PartyDetailPopup_Server.getSiteDetailPopup_Mdl(DmServer.getSsnSiteCode(this), cnTemp));
                mrTemp.getPartyDetail_Mdl().put(DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), PartyDetailPopup_Server.getSiteDetailPopup_Mdl(DmServer.getSsnSiteCode(this), cnTemp));
                for (int transactionalPartyNature : mrTemp.getPartyDetail_Mdl().keySet()) {
                    PartyDetailPopup_Mdl partyDetailPopup_Mdl = mrTemp.getPartyDetail_Mdl().get(transactionalPartyNature);
                    if (partyDetailPopup_Mdl != null && !DmShared.xNull(partyDetailPopup_Mdl.getSubCode()).trim().equalsIgnoreCase("")) {
                        partyDetailPopup_Mdl.setSharedKeyFieldCode(mrTemp.getSharedKeyFieldCode());
                        partyDetailPopup_Mdl.setSharedKeyFieldName(DmShared.iNull(transactionalPartyNature).toString());
                        PartyDetailPopup_Server.savePartyTransRecord(partyDetailPopup_Mdl, this, cnTemp);
                    }
                }
            }

            //</editor-fold>
            /*==============================<< Manage saleBILL (Returned Qty) >>==============================*/
            manageReturnedQty(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), false, cnTemp);

            if (!DmShared.xNull(returnRequestDocID).toString().trim().equalsIgnoreCase("")) {
                sqlQuery = "update saleBillReturn set returnDocID=:docId where docid=:returnRequestDocID";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("returnRequestDocID", DmShared.iNull(returnRequestDocID).toString());
                psTemp.executeUpdate();
                psTemp.close();
            }

            saveLog(mrTemp.getSharedKeyFieldCode(), mrTemp.getModifyReasonMdl(), mrTemp, blnAdd ? DmEnum.DmTransactionMode.NEW : DmEnum.DmTransactionMode.EDIT, null);

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

            /*====================================< Data Fetch For Account Posting >====================================*/
            List<ItemAccountPosting_Mdl> itemList = new ArrayList<>();

            String roundOffSubCode = DmServer.getCodeViaManualCode("RoInLe1", "manualCode", "SubGroup", "SubCode", cnTemp);

            sqlQuery = "select temp.* from (\n"
                    + "SELECT  max(sg.name) as acName, sbd.oh_Ac_CGST as subCode ,\n"
                    + " sum(sbd.oh_Amt_SGST) as amtDr , 0 as amtCr\n"
                    + "FROM saleBill sb \n"
                    + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_CGST \n"
                    + "WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                    + "group by sbd.oh_Ac_CGST\n"
                    + "\n"
                    + "union all  \n"
                    + "\n"
                    + "SELECT  max(sg.name) as acName, sbd.oh_Ac_SGST as subCode ,\n"
                    + " sum(sbd.oh_Amt_SGST) as amtDr ,0 as amtCr  \n"
                    + "FROM saleBill sb \n"
                    + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_SGST \n"
                    + "WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                    + "group by sbd.oh_Ac_SGST\n"
                    + "\n"
                    + "Union all  \n"
                    + "\n"
                    + "		SELECT  max(sg.name) as acName, sbd.oh_Ac_IGST as subCode ,\n"
                    + "		0 as amtDr, sum(sbd.oh_Amt_IGST)  as amtCr  \n"
                    + "		FROM saleBill sb \n"
                    + "		LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                    + "		LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_IGST \n"
                    + "		WHERE sb.docId = :docId"
                    + " and sbd.isFooter = 0\n"
                    + "		group by sbd.oh_Ac_IGST\n"
                    + "\n"
                    + "union all \n"
                    + "\n"
                    + "SELECT  max(sg.name) as acName, sbd.oh_Ac_Sales as subCode ,\n"
                    + " sum(sbd.oh_Amt_Taxable) as amtDr , 0 as amtCr\n"
                    + "FROM saleBill sb \n"
                    + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_Sales \n"
                    + "WHERE sb.docId = :docId  and sbd.isFooter = 0\n"
                    + "group by sbd.oh_Ac_Sales\n"
                    + "\n"
                    + ""
                    + "union all\n"
                    + "\n"
                    + "	SELECT  max(sg.name) as acName, sbd.oh_Ac_Sales as subCode ,\n"
                    + "	case when sum(sbd.oh_Amt_Taxable)>0 then abs(sum(sbd.oh_Amt_Taxable)) else      0  end as amtDr, \n"
                    + "	case when sum(sbd.oh_Amt_Taxable)<0 then abs(sum(sbd.oh_Amt_Taxable)) else     0  end   as amtCr  \n"
                    + "	FROM saleBill sb \n"
                    + "	LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_Sales \n"
                    + "	WHERE sb.docId = :docId  and sbd.isFooter = 1\n"
                    + "	group by sbd.oh_Ac_Sales  "
                    + ""
                    + "union all \n"
                    + "\n"
                    + "\n"
                    + "SELECT sg.name AS acName, \n"
                    + "       partySubCode AS subCode, \n"
                    + "       0 AS amtDr , \n"
                    + "	   sb.oh_Amt_Net AS amtCr\n"
                    + "FROM saleBill sb \n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = sb.partySubCode  \n"
                    + "WHERE sb.docId = :docId \n"
                    + "\n"
                    + "union all\n"
                    + "\n"
                    + " select max(sg.name) as acName,sg.subCode ,abs(sum(stk.grossPurchaseRate)) as amtDr,0 as amtCr from stock stk \n"
                    + "left join itemMast im on im.code=stk.itemCode\n"
                    + "left join subGroup sg on sg.subCode=im.subCode\n"
                    + "where stk.docid=:docId \n"
                    + "group by sg.subCode\n"
                    + "\n"
                    + "union all\n"
                    + "\n"
                    + "select max(sg.name) as acName,sg.subCode ,0 as amtDr,abs(sum(stk.grossPurchaseRate)) as amtCr  from stock stk \n"
                    + "left join itemMast im on im.code=stk.itemCode\n"
                    + "left join subGroup sg on sg.subCode=im.cogsAc\n"
                    + "where stk.docid=:docId \n"
                    + "group by sg.subCode\n"
                    + "\n"
                    + "union all \n"
                    + "\n"
                    + "select 'Round Off A/c' as acName , " + roundOffSubCode + " as subCode , \n"
                    + "case when sb.oh_Amt_RoundOff > 0 then abs(sb.oh_Amt_RoundOff) else 0 end as amtDr,\n"
                    + "case when sb.oh_Amt_RoundOff < 0 then abs(sb.oh_Amt_RoundOff) else 0 end as amtcr\n"
                    + "from saleBill sb\n"
                    + "where sb.docId = :docId \n"
                    + "\n"
                    + ") as temp  \n"
                    + "where temp.subCode is not null ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                ItemAccountPosting_Mdl listNode = new ItemAccountPosting_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                listNode.setSharedKeyFieldName(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                listNode.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdSaleBillReturn().getPrefix()));
                listNode.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                listNode.setV_Prefix(DmShared.xNull(voucherPrefix));
                listNode.setV_Date(DmShared.xNull(mrTemp.getVoucherDate()));
                listNode.setV_No(DmShared.iNull(mrTemp.getRecIdSaleBillReturn().getCounter()).toString());
                listNode.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setContraText(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setAmtdr(DmShared.xNull(rsTemp.getString("amtDr")));
                listNode.setAmtcr(DmShared.xNull(rsTemp.getString("amtCr")));

                itemList.add(listNode);

            }

            saveAccountPosting(itemList, mrTemp.getSharedKeyFieldCode(), cnTemp);

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

            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private CompareProperties_Mdl iniItemBatchProperties(WholeSaleBillReturn_Mdl mrTempCurrent, WholeSaleBillReturn_Mdl mrTempPrevious) {
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
        compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        if (mrTempPrevious != null) { //Edit Case
            compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
        }
        return compareProperties_Mdl;
    }

    private String checkItemSale(String itemCode, String ibCode, String prvQty, String prvFreeQty, Connection cnTemp) throws SQLException {
        DmPreparedStatement psTemp = null;
        String sqlQuery, returnValue = "";
        ResultSet rsTemp = null;

        sqlQuery = " Select sbd.ibCode ";
        if (DmShared.vNull(prvQty).doubleValue() == 0) {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.WHOLE_SALE_RETURN_CREDIT) + ") THEN 0 - ISNULL(sbd.quantity,0) ELSE sbd.quantity END) as quantity ";
        } else {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.WHOLE_SALE_RETURN_CREDIT) + ") THEN 0 - ISNULL(sbd.quantity,0) ELSE sbd.quantity END)  + " + Math.abs(DmShared.vNull(prvQty).doubleValue()) + " as quantity ";
        }
        if (DmShared.vNull(prvFreeQty).doubleValue() == 0) {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.WHOLE_SALE_RETURN_CREDIT) + ") THEN 0 - ISNULL(sbd.freeQty,0) ELSE sbd.freeQty END) as freeQty ";
        } else {
            sqlQuery += " , SUM( CASE WHEN vt.category in (" + getVoucherCategory(pvc.WHOLE_SALE_RETURN_CREDIT) + ") THEN 0 - ISNULL(sbd.freeQty,0) ELSE sbd.freeQty END)  + " + Math.abs(DmShared.vNull(prvFreeQty).doubleValue()) + " as freeQty ";
        }

        sqlQuery += " FROM SaleBill sb "
                + " INNER JOIN saleBillDetail sbd On sb.docid=sbd.docid "
                + " INNER JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
                + " WHERE sbd.itemCode = :itemCode "
                + " AND sbd.ibCode = :ibCode "
                + " AND sb.isCancelled = 0 ";

        sqlQuery += " AND vt.category IN (" + getVoucherCategory(pvc.PHARMACY_BILL_WHOLESALE_CASH) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_WHOLESALE_CREDIT) + " ) "
                + " GROUP BY sbd.ibCode ";

        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
        psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());

        rsTemp = psTemp.executeQuery();
        if (rsTemp.next()) {
            returnValue = DmShared.vNull(rsTemp.getDouble("quantity") + rsTemp.getDouble("freeQty")).toString();
        }
        rsTemp.close();
        psTemp.close();

        return returnValue;
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
                    + "    sdm.name AS departmentName, sg.displayName as patientName, "
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
                    + "  LEFT JOIN subgroup sg ON sg.subCode=mb.partySubCode  "
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
    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHSaleBillNo(DmHelper helper, String paymentSource, String partySubcode, boolean blnToEndOfFile, String departmentCode, String voucherDate, String docIdSaleReturn, String pharmacyReturnDays, String vTypeCode) {
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

            innerHelpCondition = " and vt1.category in('PHWBL','PHWBC') ";
            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                condition += " and 1 = 2";
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition += " and sb.departmentCode = :departmentCode ";
            } else {
                condition += " and 1 = 2";
            }
            if (DmShared.iNull(partySubcode).longValue() > 0) {
                condition += " and sb.partySubcode = :partySubcode ";
            }

            if (!DmShared.xNull(STServer.getSsnPharmacyAllowSaleBillForSite(this)).equalsIgnoreCase("")) {
                condition += " and sb.site_Code IN (" + STServer.getSsnPharmacyAllowSaleBillForSite(this) + ") ";
            } else {
                condition += " and 1 = 2";
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "  Select  sb.paymentMode ,sb.paymentSource, "
                    + "     sb.docid "
                    + "   , sb.recID "
                    + "   , sb.v_Date"
                    + "   , sb.departmentCode"
                    + "   , dep.name as departmentName"
                    + "   , sb.v_type \n"
                    + "   , vt.short_Name"
                    + "   , sb.v_No"
                    + "   , lm.displayName as patientName \n"
                    + "   , sb.oh_Amt_Net"
                    + "   , sb.consultantCode"
                    + "   , sgcon.name AS consultantName "
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
                    + "		where    sbd1.isFooter=0 and vt1.category in( 'PHWBL','PHWBC' )\n" + innerHelpCondition
                    + "		and sb1.isCancelled = 0 \n" + innerCondition
                    + "         Group By sb1.docId \n"
                    + "		) vw On vw.docid=sb.docid \n"
                    + "    where vt.category in( 'PHWBL','PHWBC' ) " + innerHelpCondition1
                    + "    and sb.v_date <= :v_Date "
                    + "    and sb.isCancelled = 0   "
                    + "    " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
            if (DmShared.iNull(partySubcode).longValue() > 0) {
                sqlWithParam.setString("partySubcode", DmShared.iNull(partySubcode).toString());
            }

            if (DmShared.iNull(docIdSaleReturn).longValue() > 0) {
                sqlWithParam.setString("srDocid", DmShared.xNull(docIdSaleReturn));
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
                listNode.setV_Type(DmShared.xNull(rsTemp.getString("v_type")));
                listNode.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                listNode.setSaleBillDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setConsultantCode(DmShared.iNull(rsTemp.getString("consultantCode")).toString());
                listNode.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                listNode.setPaymentSourceCode(DmShared.xNull(rsTemp.getString("paymentSource")));

                listNode.setPaymentSourceName(WholeSalePaymentMode.getViaKey(DmShared.iNull(rsTemp.getString("paymentMode")).intValue()).getValue());

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
            cnTemp.close();
            cnTempHIS.close();
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
                if (cnTempHIS != null) {
                    cnTempHIS.close();
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
    public DmResult<WholeSaleBillReturn_Mdl> getUHIDDetails(SearchUHID_Mdl searchMdl) {
        DmResult<WholeSaleBillReturn_Mdl> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Connection cnTemp = null;
        String sqlQuery = "";
        WholeSaleBillReturn_Mdl mrTemp = new WholeSaleBillReturn_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT "
                    + "     sb.docId "
                    + "   , sb.recId "
                    + "   , sb.sBillType "
                    + "   , sg.subCode AS partyCode, c.stateCode, sm.name as stateName"
                    + "   , sg.displayName"
                    + "	from saleBill sb  "
                    + "	left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                    + "	left join subGroup sg on sg.subCode = sb.partySubCode  "
                    + "     LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                    + "     LEFT JOIN stateMast sm ON sm.code = c.stateCode \n"
                    + "     where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL_WHOLESALE_CASH) + "," + getVoucherCategory(pvc.PHARMACY_BILL_WHOLESALE_CREDIT) + ") "
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
                mrTemp.setVoucherDate(DmShared.dNull(searchMdl.getVoucherDate()));
                mrTemp.setsBillType(DmShared.xNull(rsTemp.getString("sBillType")));
                mrTemp.setDebtorName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setDebtorCode(DmShared.xNull(rsTemp.getString("partyCode")));
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setPlaceOfSupplyName(DmShared.xNull(rsTemp.getString("stateName")));
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getDocIdSaleBill()).longValue() == 0) {
                throw new DmException(0, "No record found. Try Again.");
            }

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
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setCardPaymentReturn(new CardPaymentPopup_Mdl());

            mrTemp.setPartyType(DmEnum.DmPartyType.Unregistered.getId());

            sqlQuery = "SELECT  vt.v_Type, vt.description, vt.attachPaymentMode, vt.isSystemDefine"
                    + "     FROM voucher_Type vt "
                    + "     WHERE vt.category = '" + pvc.WHOLE_SALE_RETURN_CREDIT + "' and vt.isActive = 1 ";
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
                    rtvMdl.setCardPaymentDefaultMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(pmArray[0])}, cnTemp).get(0));
                }
                reciptList.add(rtvMdl);
            }
            rsTemp.close();
            psTemp.close();
            mrTemp.getCardPaymentReturn().getTxtHReceiptTypeVList().addAll(reciptList);

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
                    + "  where sg.IsActive = 1 and dm.subCode= '1000100000000000267' and sgsl.site_Code = :siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT env.expiryAlertBeforeDays "
                    + "  , env.medicineSaleOn "
                    + "  , env.alphCase_Patient "
                    + "  , env.alphaCase_Address "
                    + "  , env.defaultPharmacyServiceCode "
                    + "  , env.pharmacyAllowReturnWithoutSale "
                    + "  , env.pharmacyReturnDays "
                    + "  , env.isIncludeTaxInCost "
                    + "  , env.pharmacyBarcodeActive "
                    + "  , env.approvalInPharmacyCashSaleReturn "
                    + "  FROM enviro as env "
                    + "  WHERE env.site_Code = :site_Code ";
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
                mrTemp.setApprovalRequired(DmShared.xNull(rsTemp.getString("approvalInPharmacyCashSaleReturn")));

                if (DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).trim().equals("")) {
                    throw new DmException(0, "Please set 'Include Tax In Cost' in environment settings.");
                } else {
                    mrTemp.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                }
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
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
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
                            //                            + " and  sbd.freeWithItemCode = '" + DmShared.iNull(field.getSharedModifiedBy()).toString() + "' "
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

            sqlQuery = " SELECT  sbd.oh_Ac_IGST,sbd.oh_Amt_IGST,sbd.oh_at_IGST,"
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
                    + " , (ISNULL(sbd.freeQty,0)- ISNULL(sbd.returnedFreeQty,0)) as freeQty "
                    + quantityField + "  as quantity "
                    + " , im.sku as skuCode "
                    + " , um.unit as skuName "
                    + " , im.purchaseUnit as purchaseUnitCode"
                    + " , ump.unit as purchaseUnitName"
                    + " , ump.decimalPlaces as allowedPurchaseDecimalPlaces "
                    + " , um.decimalPlaces as allowedSkuDecimalPlaces"
                    + " , im.purchaseUnitConversionRate as purchaseUnitConversionRate "
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
                    + " , ump.gstUnitCode as gstUnitCode"
                    + " , sbd.barcode ,  sbd.grossPurchaseRate,sbd.wholeSaleRate,sbd.wholeSaleRateActual "
                    + "   from saleBill as sb "
                    + "   left join saleBillDetail as sbd on sb.docId = sbd.docId "
                    + "   left join itemMast as im on sbd.itemCode = im.code "
                    + "   left join unitMast as um on im.sku = um.code "
                    + "   left join unitMast as ump on im.purchaseUnit = ump.code   "
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
                node.setUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
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
                node.setWholeSaleRate(DmShared.xNull(rsTemp.getString("wholeSaleRate")));
                node.setWholeSaleRateActual(DmShared.xNull(rsTemp.getString("wholeSaleRateActual")));
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

                node.setIgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST")));
                node.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                node.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));

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
                node.setSbFreeQuantity(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                node.setQuantity(DmServer.vNull(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedPurchaseDecimalPlaces")).intValue()))));
                node.setSbQuantity(DmServer.vNull(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedPurchaseDecimalPlaces")).intValue()))));
//                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue());
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setGstUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedPurchaseDecimalPlaces")).intValue());

                node.setMedicineMdl(medicineMdl);
                node.setGrossPurchaseRate(DmShared.xNull(rsTemp.getString("grossPurchaseRate")));

                listDetail.add(node);

            }
            rsTemp.close();
            psTemp.close();

            listDetailFinal.addAll(listDetail);
            listDetail = new ArrayList<>();

//            sqlQuery = " SELECT  sbd.oh_Ac_IGST,sbd.oh_Amt_IGST,sbd.oh_at_IGST,  "
//                    + "   sbd.itemCode as itemCode "
//                    + " , im.name as itemName "
//                    + " , sbd.ibCode as ibCode "
//                    + " , sbd.batchNo as batchNo "
//                    + " , sbd.expiryDate as expiryDate "
//                    + " , (ISNULL(sbd.freeQty,0)- ISNULL(sbd.returnedFreeQty,0)) as freeQty "
//                    + " , sbd.freeWithItemCode "
//                    + " , sbd.rate as rate "
//                    + " , sbd.mrp as mrp "
//                    + " , sbd.skuMrp as skuMrp "
//                    + " , sbd.skuRate as skuRate "
//                    + " , sbd.manufacturingDate as manufacturingDate "
//                    + " , sbd.expiryDate as expiryDate "
//                    + " , sbd.oh_at_Discount as oh_at_Discount "
//                    + " , sbd.oh_Amt_Discount as oh_Amt_Discount "
//                    + " , sbd.oh_at_Discount2 as oh_at_Discount2 "
//                    + " , sbd.oh_Amt_Discount2 as oh_Amt_Discount2 "
//                    + " , sbd.oh_Amt_Gross as  oh_Amt_Gross "
//                    + " , sbd.oh_Ac_SGST as  oh_Ac_SGST "
//                    + " , sbd.oh_Ac_UGST as  oh_Ac_UGST "
//                    + " , sbd.oh_Ac_CGST as  oh_Ac_CGST "
//                    + " , sbd.oh_Ac_Sales as  oh_Ac_Sales "
//                    + " , sbd.oh_Amt_Net as  oh_Amt_Net "
//                    + " , sbd.oh_at_CGST as  oh_at_CGST "
//                    + " , sbd.oh_Amt_CGST as  oh_Amt_CGST "
//                    + " , sbd.oh_Amt_SGST as  oh_Amt_SGST "
//                    + " , sbd.oh_at_SGST as  oh_at_SGST "
//                    + " , sbd.oh_Amt_UGST as  oh_Amt_UGST "
//                    + " , sbd.oh_at_UGST as  oh_at_UGST "
//                    + " , sbd.oh_Amt_Taxable as  oh_Amt_Taxable "
//                    + " , sbd.isReturn as  isReturn "
//                    + " , sbd.docId as sbDocid "
//                    + " , sbd.v_SNo as sbV_SNo "
//                    + " , sb.recId as saleBillNo "
//                    + " , 0 as quantity "
//                    + " , im.sku as skuCode "
//                    + " , um.unit as skuName "
//                    + " , im.purchaseUnit as purchaseUnitCode"
//                    + " , ump.unit as purchaseUnitName"
//                    + " , ump.decimalPlaces as allowedPurchaseDecimalPlaces "
//                    + " , um.decimalPlaces as allowedSkuDecimalPlaces"
//                    + " , im.purchaseUnitConversionRate as purchaseUnitConversionRate "
//                    + " , im.isReturnableItem as isReturnableItem "
//                    + " , im.allowNegativeStock as allowNegativeStock "
//                    + " , im.isBarcodeActive "
//                    + " , im.isSaleRateEditable as isSaleRateEditable "
//                    + " , im.hsnCode as hsnCode"
//                    + " , hm.hsn as hsnName "
//                    + " , im.isBestBefore as isBestBefore"
//                    + " , im.restrictGroup as restrictGroup"
//                    + " , im.itemGSTNature as itemGSTNature"
//                    + " , im.itsBasedOn as itsBasedOn"
//                    + " , im.isDiscountAllow as isDiscountAllow"
//                    + " , ump.gstUnitCode as gstUnitCode"
//                    + " , sbd.barcode "
//                    + "   from saleBill as sb "
//                    + "   left join saleBillDetail as sbd on sb.docId = sbd.docId "
//                    + "   left join itemMast as im on sbd.itemCode = im.code "
//                    + "   left join unitMast as um on im.sku = um.code "
//                    + "   left join unitMast as ump on im.purchaseUnit = ump.code   "
//                    + "   left join hsnMast hm on hm.code = im.hsnCode "
//                    + "   left join itemGroup ig on ig.code = im.itemGroup "
//                    + "   where sbd.isReturn = 0 "
//                    + "   and sbd.isFooter = 0 "
//                    + "   and sb.site_Code IN (" + STServer.getSsnPharmacyAllowSaleBillForSite(this) + ") "
//                    + "   and sb.departmentCode= :departmentCode "
//                    + "   and " + conditionUnion
//                    + "   order by itemName ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                node = new GrdMedicine_Mdl();
//                medicineMdl = new TxtHMedicine_Mdl();
//                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
//                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
//                node.setUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
//                node.setSbDocId(DmShared.xNull(rsTemp.getString("sbDocid")));
//                node.setSbV_Sno(DmShared.xNull(rsTemp.getString("sbV_SNo")));
//                node.setSbRecId(DmShared.xNull(rsTemp.getString("saleBillNo")));
//                node.setSbFreeQuantity(DmServer.vNull(rsTemp.getString("freeQty")));
//                node.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
//                //<editor-fold defaultstate="collapsed" desc="PROPERTIES">
//                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
//                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
//                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
//
//                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
//                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
//                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
//                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
//                node.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
//                node.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
//
////                medicineMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
////                medicineMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
////                medicineMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
////                medicineMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
//                //</editor-fold>
//                //<editor-fold defaultstate="collapsed" desc="TAXATION">
//                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
//                node.setDiscount2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
//                node.setTotalDiscount(DmServer.vNull((DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() + DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue()), "0.00"));
//                node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
//                node.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
//                node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
//                node.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
//                node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
//                node.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
//                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
//                node.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));
//                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
//                node.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
//                node.setSaleAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
//                node.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
//                node.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
//                node.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));
//                node.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
//
//                node.setIgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_IGST")));
//                node.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
//                node.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
//
//                node.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
//                node.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));
//
//                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
//                dmrIts = getITS(DmShared.iNull(rsTemp.getString("itemCode")).toString(), 0, DmShared.xNull(rsTemp.getString("itsBasedOn")), v_Date, cnTemp);
//                if (!DmShared.xNull(dmrIts.getValue1()).equalsIgnoreCase("")) {
//                    node.setSaleAcCode(dmrIts.getValue1());
//                }
////</editor-fold>
//                if (DmShared.vNull(rsTemp.getString("oh_at_Discount")).doubleValue() == 0
//                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() > 0) {
//                    double tempDiscountPer = 0;
//                    tempDiscountPer = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();
//                    medicineMdl.setTempDiscountPer(DmServer.vNull(tempDiscountPer, "0.00"));
//                }
//                if (DmShared.vNull(rsTemp.getString("oh_at_Discount2")).doubleValue() == 0
//                        && DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue() > 0) {
//                    double tempDiscountPer2 = 0;
//                    tempDiscountPer2 = (DmShared.vNull(rsTemp.getString("oh_Amt_Discount2")).doubleValue() * 100) / DmShared.vNull(rsTemp.getString("oh_Amt_Gross2")).doubleValue();
//                    node.setTempDiscountPer2(DmServer.vNull(tempDiscountPer2, "0.00"));
//                }
//
//                node.setQuantity(DmServer.vNull(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedPurchaseDecimalPlaces")).intValue()))));
//                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
//                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
//                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
//                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
//                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
//                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue());
//                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
//                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
//                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
//                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
//                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
//                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
//                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
//                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
//                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
//                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
//                medicineMdl.setGstUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
//                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
//                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
//                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedPurchaseDecimalPlaces")).intValue());
//                node.setMedicineMdl(medicineMdl);
//
//                listDetail.add(node);
//
//            }
//            rsTemp.close();
//            psTemp.close();

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
    public DmResult<BarCode_Mdl> getBarCodeSearch(String barCode, String voucherDate, String departmentCode, String siteCode, STEnum.MedicineSaleOn saleOn) {
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDebtorsName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        KeyFieldExtended node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select sg.name AS Name,sg.subCode AS code, sg.gsTin, sg.profitPercentage  from subGroup sg \n"
                    + " left join subGroupNature sgn on sgn.code = sg.nature \n"
                    + " WHERE sgn.manualCode = 'CT' ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("gsTin")));
                node.setSharedCancelledBy(DmServer.vNull(rsTemp.getString("profitPercentage")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTransportMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.TransportMode transportMode : DmEnum.TransportMode.values()) {
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
    public DmResultPageing<List<TxtHVendor_Mdl>> getTxtHTransporter(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVendor_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVendor_Mdl> lstTemp = new ArrayList<>();
        TxtHVendor_Mdl listNode;
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

            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new TxtHVendor_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));

                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHLocation(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        boolean isSiteUT = false;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT s.isUt "
                    + "FROM siteMast sm \n"
                    + "LEFT JOIN stateMast s on s.numericCode = sm.stateNumericCode\n"
                    + "WHERE sm.code = :siteCode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                isSiteUT = (DmShared.iNull(rsTemp.getString("isUt")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = " SELECT lm.code, lm.name \n"
                    + " from locationMast lm \n"
                    + " WHERE lm.isActive = 1 \n"
                    + " AND lm.manualCode <> :manualCode";

            sqlWithParam.setSQL(sqlQuery);
            if (isSiteUT) {
                sqlWithParam.setString("manualCode", DmEnum.DmGstLocation_SystemDefined.INTRA_STATE.getManualCode());
            } else {
                sqlWithParam.setString("manualCode", DmEnum.DmGstLocation_SystemDefined.UNION_TERRITORY.getManualCode());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(node);
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
    public DmResultPageing<List<KeyFieldExtended>> getHVehicleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT vt.code AS code , vt.name AS name "
                    + " FROM vehicleTypeMast vt ";
            sqlWithParam.setSQL(sqlQuery);
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

    @Override
    public DmResult<PartyDetailPopup_Mdl> fillVendorDetails(String subCode, int recordTypeCode) {
        DmResult<PartyDetailPopup_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PartyDetailPopup_Mdl mrTemp = new PartyDetailPopup_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = PartyDetailPopup_Server.getPartyDetailPopup_Mdl(subCode, cnTemp);

            String debtorlocation = "";
            String Sitelocation = "";
            String location = "";
            sqlQuery = "select sg.cityCode,ct.stateCode ,sm.name AS StateName from subGroup sg \n"
                    + "left join city ct on ct.cityCode = sg.cityCode \n"
                    + "left join stateMast  sm on sm.code = ct.stateCode \n"
                    + "where sg.subCode = :subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", subCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                debtorlocation = DmShared.xNull(rsTemp.getString("stateCode"));
            }

            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT ISNULL(sm.isUt,0) as isUt, sm.code As stateCode,sm.name As stateName FROM stateMast sm WHERE sm.numericCode = :numericCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("numericCode", DmServer.getSsnSiteStateNumericCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.xNull(rsTemp.getString("isUT")).equalsIgnoreCase("1")) {
                    location = DmShared.iNull(DmEnum.DmLocation.UnionTerritory.getId()).toString();
                } else {
                    Sitelocation = DmShared.xNull(rsTemp.getString("stateCode"));
                }

            }

            psTemp.close();
            rsTemp.close();

            if (DmShared.xNull(debtorlocation).equalsIgnoreCase(DmShared.xNull(Sitelocation))) {
                mrTemp.setSharedPreparedIP(DmShared.iNull(DmEnum.DmLocation.IntraState.getId()).toString());
                mrTemp.setSharedPreparedBy(DmShared.xNull(DmEnum.DmLocation.IntraState.getValue()));
            } else if (!DmShared.xNull(location).equalsIgnoreCase("")) {
                mrTemp.setSharedPreparedIP(DmShared.iNull(DmEnum.DmLocation.UnionTerritory.getId()).toString());
                mrTemp.setSharedPreparedBy(DmShared.xNull(DmEnum.DmLocation.UnionTerritory.getValue()));
            } else {
                mrTemp.setSharedPreparedIP(DmShared.iNull(DmEnum.DmLocation.InterState.getId()).toString());
                mrTemp.setSharedPreparedBy(DmShared.xNull(DmEnum.DmLocation.InterState.getValue()));
            }

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);

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

    public void saveAccountPosting(List<ItemAccountPosting_Mdl> acLst, String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            sqlQuery = "Delete From ledger Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int count = 0;
            double amount = 0;
//            String subCode, subCodeName, contraCode, contraName;

            for (ItemAccountPosting_Mdl node : acLst) {
                /*====================================< Insert records into ledger >====================================*/
                sqlQuery = "Insert into ledger (docId, v_Sno, v_Type, v_Prefix, v_Date, v_No, recId, recIdPrefix, narration, "
                        + " subCode, contraSub, contraText, amtDr, amtCr, site_Code, isPosted, chq_No, chq_Date, favourOf) "
                        + " values (:docId, :v_Sno, :v_Type, :v_Prefix, :v_Date, :v_No, :recId, :recIdPrefix, :narration, "
                        + " :subCode, :contraSub, :contraText, :amtDr, :amtCr, :site_Code, :isPosted, :chq_No, :chq_Date, :favourOf) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(node.getSharedKeyFieldCode()).toString());
                psTemp.setInt("v_Sno", ++count);
                psTemp.setString("v_Type", DmShared.foriegnKey(node.getV_Type()));
                psTemp.setString("v_Prefix", DmShared.xNull(node.getV_Prefix()));
                psTemp.setDate("v_Date", DmServer.convertDate(node.getV_Date()));
                psTemp.setString("v_No", DmShared.iNull(node.getV_No()).toString());
                psTemp.setString("recId", DmShared.xNull(node.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(node.getRecIdPrefix()));
                psTemp.setString("narration", DmShared.xNull(""));
                psTemp.setDate("chq_Date", DmServer.convertDate(""));
                psTemp.setString("chq_No", DmShared.xNull(""));
                psTemp.setString("favourOf", DmShared.xNull(""));

                psTemp.setString("subCode", DmShared.foriegnKey(node.getSubCode()));
                psTemp.setString("contraSub", DmShared.foriegnKey(""));
                psTemp.setString("contraText", DmShared.foriegnKey(node.getContraText()));
                psTemp.setString("amtDr", DmServer.vNull(node.getAmtdr(), "0.00"));
                psTemp.setString("amtCr", DmServer.vNull(node.getAmtcr(), "0.00"));

                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

                psTemp.setString("isPosted", "1");
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {

                if (psTemp != null) {
                    psTemp.close();
                }

            } catch (Exception e) {
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="eGST">
    @Override
    public DmResult<MasterEGst_Mdl> processEGst(MasterEGst_Mdl masterEGst_Mdl, int actionCode) {
        DmResult<MasterEGst_Mdl> dmrTemp = new DmResult<MasterEGst_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Check record's siteCode with current siteCode>>====================================*/
            DmServer.checkCurrentSiteRecord("saleBill", "site_Code", masterEGst_Mdl.geteGst_Mdl().getDocid(), "docId", cnTemp, this, "process E-GST");

            if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
//                checkIsDocumentInUseBeforeDiscard(masterEGst_Mdl.geteGst_Mdl().getDocid(), true, cnTemp);
            }

            /*==============================<< Check for LatestEntry >>====================================*/
            sqlQuery = "SELECT u_EntDt_LatestLine, approvedBy, cancelledBy FROM saleBill WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(masterEGst_Mdl.geteGst_Mdl().getDocid()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    if (DmShared.iNull(masterEGst_Mdl.geteGst_Mdl().getU_EntDt_LatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                        throw new DmException(0, "Record has been changed, can't process this action, refresh record and retake action.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            EGstPopup_Server.processEGst(masterEGst_Mdl, actionCode, this, new IEGstPopup() {
                @Override
                public void updateEGstDetail(EGst_Mdl eGst_Mdl, int actionCode, Connection cnTemp) throws Exception {
                    finalizeEGstProcess(eGst_Mdl, actionCode, cnTemp);
                }

                @Override
                public void finalizeGstDocumentUpdate(GstDocument_Mdl gst_Mdl, int actionCode, Connection cnTemp) throws Exception {
                    finalizeGstProcess(gst_Mdl, actionCode, cnTemp);
                }
            });
            dmrTemp.setData(masterEGst_Mdl);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void finalizeEGstProcess(EGst_Mdl eGst_Mdl, int actionCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        try {
            Timestamp timestamp = DmServer.getServerTimestamp();

            //Egst Table
            sqlQuery = "update "
                    + " eGst set "
                    + " mongoLinkId=:mongoLinkId, \n"
                    + " u_EntDt_LatestLine = :uEntDt \n"
                    + " where docId=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", eGst_Mdl.getDocid());
            psTemp.setString("mongoLinkId", DmShared.foriegnKey(eGst_Mdl.getMongoLinkId()));
            psTemp.setTimestamp("uEntDt", timestamp);
            psTemp.executeUpdate();
            psTemp.close();

            //Transaction Table
            sqlQuery = "UPDATE "
                    + " saleBill SET "
                    + " eInvIrnNo=:eInvIrnNo, "
                    + " eInvAckDt=:eInvAckDt, "
                    + " eInvAckNo=:eInvAckNo, "
                    + " eInvMongoLinkId=:eInvMongoLinkId, "
                    + " u_EntDt_LatestLine = :uEntDt \n"
                    + " where docId=:docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", eGst_Mdl.getDocid());
            psTemp.setString("eInvIrnNo", DmShared.foriegnKey(eGst_Mdl.getEinv_IrnNo()));
            psTemp.setTimestamp("eInvAckDt", Gst_Server.convertTimestamp(eGst_Mdl.getEinv_ackDt()));
            psTemp.setString("eInvAckNo", DmShared.foriegnKey(eGst_Mdl.getEinv_ackNo()));
            psTemp.setString("eInvMongoLinkId", DmShared.foriegnKey(eGst_Mdl.getMongoLinkId()));
            psTemp.setTimestamp("uEntDt", timestamp);
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="UPDATE Transporter Detail in party Trans Info">
            if (eGst_Mdl.isEwayBillRequired() && !DmShared.xNull(eGst_Mdl.getEway_BillNo()).trim().equalsIgnoreCase("")) {
                PartyDetailPopup_Server.deletePartyTransRecord("partyTransInfo", eGst_Mdl.getDocid(), DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), cnTemp);
                /*==============================<< save transporter into transPartyInfo >>====================================*/
                PartyDetailPopup_Mdl partyDetailPopup_Mdl = PartyDetailPopup_Server.getPartyDetailPopup_Mdl(eGst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode(), cnTemp);
                partyDetailPopup_Mdl.setSharedKeyFieldCode(DmShared.xNull(eGst_Mdl.getDocid()));
                partyDetailPopup_Mdl.setSharedKeyFieldName(DmShared.iNull(DmEnum.PartyTransRecordType.TRANSPORTER.getCode()).toString());
                PartyDetailPopup_Server.savePartyTransRecord(partyDetailPopup_Mdl, this, cnTemp);
            }
            //</editor-fold>

            PartyDetailPopup_Server.updatePartyTransRecord(DmShared.xNull(eGst_Mdl.getDocid()), DmEnum.PartyTransRecordType.TRANSACTION_SITE.getCode(), eGst_Mdl.getConsigner_Mdl(), false, cnTemp);
            PartyDetailPopup_Server.updatePartyTransRecord(DmShared.xNull(eGst_Mdl.getDocid()), DmEnum.PartyTransRecordType.PARTY.getCode(), eGst_Mdl.getBillTo_Mdl(), false, cnTemp);

            if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
//                STApprovals.salesReturnApproval(WalkThroughEnum.DECLINE, eGst_Mdl.getDocid(), DmServer.getSsnUserName(this), cnTemp, this, true, true, new pmSales().SALES_RETURN.caption, timestamp);
                deleteRecord(eGst_Mdl.getDocid(), new ModifyReason_Mdl(), "", "");
//                //==============================<< Log Save >>====================================
//                saveLog(eGst_Mdl.getDocid(), null, null, DmEnum.DmTransactionMode.DISCARD, null, timestamp, true, cnTemp);
//                ///*==============================<< ************** >>====================================*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void finalizeGstProcess(GstDocument_Mdl gst_Mdl, int actionCode, Connection cnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            // Use When Required
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    @Override
    public DmResult<MasterEGst_Mdl> getEgstDetail(String strKey, String vTypeCode) {
        DmResult<MasterEGst_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        MasterEGst_Mdl masterEgst = new MasterEGst_Mdl();
        EGst_Mdl eGst_Mdl = new EGst_Mdl();
        GstDocument_Mdl gst_Mdl = new GstDocument_Mdl();
        EGstToken_Mdl eGstToken_Mdl = new EGstToken_Mdl();
        dmrTemp.setData(masterEgst);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            masterEgst = Gst_Server.getEGstRecord(strKey, cnTemp, this);
            if (masterEgst != null) {
                eGst_Mdl = masterEgst.geteGst_Mdl();
                gst_Mdl = masterEgst.getGst_Mdl();
                if (eGst_Mdl != null) {
                    if (DmShared.xNull(eGst_Mdl.getDocid()).trim().equalsIgnoreCase("")) {
                        eGst_Mdl = getEgstDetail(getMoveRec(strKey, cnTemp), cnTemp);
                    }
                } else {
                    eGst_Mdl = getEgstDetail(getMoveRec(strKey, cnTemp), cnTemp);
                }
                if (gst_Mdl != null) {
                    if (DmShared.xNull(gst_Mdl.getDocid()).trim().equalsIgnoreCase("")) {
                        gst_Mdl = getGstDetail(vTypeCode, cnTemp);
                    }
                } else {
                    gst_Mdl = getGstDetail(vTypeCode, cnTemp);
                }
            } else {
                eGst_Mdl = getEgstDetail(getMoveRec(strKey, cnTemp), cnTemp);
                gst_Mdl = getGstDetail(vTypeCode, cnTemp);
            }
            masterEgst.seteGst_Mdl(eGst_Mdl);
            masterEgst.setGst_Mdl(gst_Mdl);
            masterEgst.seteGstToken_Mdl(Gst_Server.getToken(cnTemp, this));
            dmrTemp.setData(masterEgst);
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

    private EGst_Mdl getEgstDetail(WholeSaleBillReturn_Mdl mrTemp, Connection cnTemp) throws Exception {

        EGst_Mdl egst_Mdl = new EGst_Mdl();
        String siteCode = DmServer.getSsnSiteCode(this);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            egst_Mdl = new EGst_Mdl();
            egst_Mdl.setDocid(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            egst_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
            egst_Mdl.setV_Prefix(DmShared.xNull(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "docid", "saleBill", "v_Prefix", cnTemp)));
            egst_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
            egst_Mdl.setSite_Code(DmShared.xNull(siteCode));
            egst_Mdl.setRecId(mrTemp.getRecIdSaleBillReturn());
            egst_Mdl.setPartyType(DmShared.xNull(mrTemp.getPartyType()));
            egst_Mdl.setLocationCode(DmShared.xNull(mrTemp.getLocationCode()));
//            egst_Mdl.setGstType(String.valueOf((DmShared.iNull(mrTemp.getItcCode()).intValue() == 1) ? (DmEnum.DmGSTType.OUTPUT.toInt()) : (DmEnum.DmGSTType.VOID.toInt())));
            egst_Mdl.setPosStateNumericCode(DmShared.foriegnKey(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getPlaceOfSupply()), "code", "stateMast", "numericCode", cnTemp)));
            egst_Mdl.setPosStateName(DmShared.foriegnKey(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getPlaceOfSupply()), "code", "stateMast", "name", cnTemp)));
            egst_Mdl.setEinvoiceRequired(true);
            egst_Mdl.setEwayBillRequired(false);
            egst_Mdl.setEway_GenerationMode("");
            egst_Mdl.setU_EntDt_LatestLine(DmShared.xNull(mrTemp.getUserEntDtLatestLine()));

            egst_Mdl.setCgstValue(DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
            double sgstUGstValue = DmShared.vNull(mrTemp.getSgstAmt()).doubleValue() + DmShared.vNull(mrTemp.getUgstAmt()).doubleValue();
            egst_Mdl.setSgstValue(DmServer.vNull(sgstUGstValue, "0.00"));
            egst_Mdl.setIgstValue(DmServer.vNull(mrTemp.getIgstAmt(), "0.00"));
//            eGst_Mdl.setCessValue(DmServer.vNull(mrTemp.getCessAmount(), "0.00"));
//            eGst_Mdl.setCessNonAdvolValue(DmServer.vNull(mrTemp.getCessNaAmount(), "0.00"));
            egst_Mdl.setTaxableValue(DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
//            eGst_Mdl.setTotInvValue(DmServer.vNull(mrTemp.gett(), "0.00"));
            egst_Mdl.setNetInvAmount(DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
            egst_Mdl.setOtherAmount("0.00");

            //<editor-fold defaultstate="collapsed" desc="From & DispatchFrom">
            Party_Mdl consigner_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.TRANSACTION_SITE.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, false, cnTemp);
            Party_Mdl dispatchFrom_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.DISPATCH_FROM.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, false, cnTemp);
            if (consigner_Mdl == null || DmShared.xNull(consigner_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Consigner is mandatory!");
            } else {
                if (dispatchFrom_Mdl == null || DmShared.xNull(dispatchFrom_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    dispatchFrom_Mdl = consigner_Mdl.clone();
                }
            }
            egst_Mdl.setConsigner_Mdl(consigner_Mdl.clone());
            egst_Mdl.setActFromStateNumericCode(DmShared.xNull(dispatchFrom_Mdl.getStateNumericCode()));
            egst_Mdl.setDispatchFrom_Mdl(dispatchFrom_Mdl.clone());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Bill To, Consignee & shipTo">
            Party_Mdl billTo_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.PARTY.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), DmEnum.DmPartyType.getValueOf(mrTemp.getPartyType()), false, cnTemp);
            Party_Mdl consignee_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.PARTY.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, false, cnTemp);
            Party_Mdl shipTo_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.DELIVERY_TO_PARTY.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, false, cnTemp);
//            if (billTo_Mdl == null || DmShared.xNull(billTo_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
//                throw new UnsupportedOperationException("Bill To is mandatory!");
//            } else {
            if (consignee_Mdl != null
                    && !DmShared.xNull(consignee_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                if (shipTo_Mdl == null || DmShared.xNull(shipTo_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    shipTo_Mdl = consignee_Mdl.clone();
                }
            } else {
                consignee_Mdl = new Party_Mdl();
            }
            if (shipTo_Mdl == null || DmShared.xNull(shipTo_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                shipTo_Mdl = billTo_Mdl.clone();
            }
//            }
            egst_Mdl.setBillTo_Mdl(billTo_Mdl.clone());
            egst_Mdl.setConsignee_Mdl(consignee_Mdl.clone());
            egst_Mdl.setActToStateNumericCode(DmShared.xNull(shipTo_Mdl.getStateNumericCode()));
            egst_Mdl.setShipTo_Mdl(shipTo_Mdl.clone());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Transporter">
            Party_Mdl transporterMdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, true, cnTemp);
            egst_Mdl.setTransporter_Mdl(transporterMdl.clone());
            egst_Mdl.setTransportModeCode(DmShared.foriegnKey(mrTemp.getTransportModeCode()));
            egst_Mdl.setTransportModeName(DmShared.foriegnKey(mrTemp.getTransportModeName()));
            egst_Mdl.setTransportLrNo(DmShared.foriegnKey(mrTemp.getLrNo()));
            egst_Mdl.setTransportLrDate(DmShared.foriegnKey(mrTemp.getLrDate()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="GstVehicleType">
            egst_Mdl.setVehicleNo(DmShared.foriegnKey(mrTemp.getVehicleNo()));
            egst_Mdl.setVehicleTypeCode(DmShared.foriegnKey(mrTemp.getVehicleTypeCode()));
            egst_Mdl.setVehicleTypeName(DmShared.foriegnKey(mrTemp.getVehicleTypeName()));
            if (!DmShared.xNull(mrTemp.getVehicleTypeCode()).trim().equalsIgnoreCase("")) {
                sqlQuery = "select vtm.gstVehicleType \n"
                        + " from vehicleTypeMast vtm \n"
                        + " where vtm.code = :code \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getVehicleTypeCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    egst_Mdl.setGstVehicleType(DmShared.foriegnKey(rsTemp.getString("gstVehicleType")));
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Transaction Type">
            int transTypeCode = DmEnum.Eway_TransactionType.Regular.getCode();
            String dispatchFromInfo = Gst_Shared.concatPartyInfo(dispatchFrom_Mdl.getGstin(), dispatchFrom_Mdl.getLegalName(), dispatchFrom_Mdl.getAddress1(), dispatchFrom_Mdl.getAddress2(), dispatchFrom_Mdl.getCityName(), dispatchFrom_Mdl.getPinCode(), dispatchFrom_Mdl.getStateName(), dispatchFrom_Mdl.getCountryName()), consignerInfo = Gst_Shared.concatPartyInfo(consigner_Mdl.getGstin(), consigner_Mdl.getLegalName(), consigner_Mdl.getAddress1(), consigner_Mdl.getAddress2(), consigner_Mdl.getCityName(), consigner_Mdl.getPinCode(), consigner_Mdl.getStateName(), consigner_Mdl.getCountryName());
            if (!DmShared.xNull(dispatchFromInfo).equalsIgnoreCase(consignerInfo)) {
                transTypeCode = DmEnum.Eway_TransactionType.Bill_From_Ship_From.getCode();
            }
            String billToInfo = Gst_Shared.concatPartyInfo(billTo_Mdl.getGstin(), billTo_Mdl.getLegalName(), billTo_Mdl.getAddress1(), billTo_Mdl.getAddress2(), billTo_Mdl.getCityName(), billTo_Mdl.getPinCode(), billTo_Mdl.getStateName(), billTo_Mdl.getCountryName()), shipToInfo = Gst_Shared.concatPartyInfo(shipTo_Mdl.getGstin(), shipTo_Mdl.getLegalName(), shipTo_Mdl.getAddress1(), shipTo_Mdl.getAddress2(), shipTo_Mdl.getCityName(), shipTo_Mdl.getPinCode(), shipTo_Mdl.getStateName(), shipTo_Mdl.getCountryName());
            if (!DmShared.xNull(billToInfo).equalsIgnoreCase(shipToInfo)) {
                if (transTypeCode == DmEnum.Eway_TransactionType.Regular.getCode()) {
                    transTypeCode = DmEnum.Eway_TransactionType.Bill_To_Ship_To.getCode();
                } else {
                    transTypeCode = DmEnum.Eway_TransactionType.Combination_OF_2_And_3.getCode();
                }
            }
            egst_Mdl.setEwayTransTypeCode(String.valueOf(transTypeCode));
            egst_Mdl.setEwayTransTypeName(DmEnum.Eway_TransactionType.getViaCode(transTypeCode));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="supply Type, subSupplyType, DocType">
            double totalTaxValue = DmShared.vNull(mrTemp.getSgstAmt()).doubleValue()
                    + DmShared.vNull(mrTemp.getUgstAmt()).doubleValue()
                    + DmShared.vNull(mrTemp.getCgstAmt()).doubleValue()
                    + DmShared.vNull(mrTemp.getIgstAmt()).doubleValue();
//                    + DmShared.vNull(mrTemp.getCessAmount()).doubleValue()
//                    + DmShared.vNull(mrTemp.getCessNaAmount()).doubleValue();

            String einvSupplyTypeCode = "",
                    einvSupplyTypeName = "",
                    einvDocTypeCode = DmShared.xNull(DmEnum.Einv_DocTypeCode.CREDIT_NOTE.getCode()),
                    einvDocTypeName = DmShared.xNull(DmEnum.Einv_DocTypeCode.CREDIT_NOTE.getValue());

            if (DmShared.xNull(egst_Mdl.getBillTo_Mdl().getGstin()).equalsIgnoreCase("URP")) {
                einvSupplyTypeCode = DmShared.xNull(DmEnum.Einv_SupplyTypeCode.Business_to_Consumer.getCode());
                einvSupplyTypeName = DmShared.xNull(DmEnum.Einv_SupplyTypeCode.Business_to_Consumer.getValue());

            } else {
                einvSupplyTypeCode = DmShared.xNull(DmEnum.Einv_SupplyTypeCode.Business_to_Business.getCode());
                einvSupplyTypeName = DmShared.xNull(DmEnum.Einv_SupplyTypeCode.Business_to_Business.getValue());
            }

            sqlQuery = "select vt.einvSupplyTypeCode \n"
                    + " from voucher_Type vt \n"
                    + " where vt.v_Type = :v_Type \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.xNull(mrTemp.getVoucherTypeCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                if (!DmShared.xNull(billTo_Mdl.getGstin()).equalsIgnoreCase("URP")) {
                    if (!DmShared.xNull(rsTemp.getString("einvSupplyTypeCode")).trim().equalsIgnoreCase("")) {
                        einvSupplyTypeCode = DmShared.xNull(rsTemp.getString("einvSupplyTypeCode"));
                        einvSupplyTypeName = DmEnum.Einv_SupplyTypeCode.getValue(DmShared.xNull(einvSupplyTypeCode));
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            egst_Mdl.setEwaySupplyTypeCode(DmShared.xNull(DmEnum.Eway_SupplyType.OUTWARD.getCode()));
            egst_Mdl.setEwaySupplyTypeName(DmShared.xNull(DmEnum.Eway_SupplyType.OUTWARD.getName()));

            if (1 == 1) {
                egst_Mdl.setEwaySupplyTypeCode(null);
                egst_Mdl.setEwaySupplyTypeName(null);
                egst_Mdl.setEwaySubSupplyTypeCode(null);
                egst_Mdl.setEwaySubSupplyTypeName(null);
                egst_Mdl.setEwaySubSupplyDesc(null);
                egst_Mdl.setEwayDocTypeCode(null);
                egst_Mdl.setEwayDocTypeName(null);
                egst_Mdl.setEinvSupplyTypeCode(DmShared.foriegnKey(einvSupplyTypeCode));
                egst_Mdl.setEinvSupplyTypeName(DmShared.foriegnKey(einvSupplyTypeName));
                egst_Mdl.setEinvDocTypeCode(DmShared.foriegnKey(einvDocTypeCode));
                egst_Mdl.setEinvDocTypeName(DmShared.foriegnKey(einvDocTypeName));
            }

            //</editor-fold>
            for (GrdMedicine_Mdl itemDetail_Mdl : mrTemp.getGrdMediDetails()) {
                EGstDetail_Mdl detail_Mdl = new EGstDetail_Mdl();
                //<editor-fold defaultstate="collapsed" desc="e-invoice">
                detail_Mdl.setvSno(DmShared.xNull(itemDetail_Mdl.getvSno()));
                //<editor-fold defaultstate="collapsed" desc="Service Record Code">
//                String serviceRecordCode = DmEnum.DmYesNo.NO.getEnumShortName();
                String serviceRecordCode = "N";
//                if (itemDetail_Mdl.isServiceRecord()) {
//                    serviceRecordCode = DmEnum.DmYesNo.YES.getEnumShortName();
//                }
                detail_Mdl.setIsService(DmShared.xNull(serviceRecordCode));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Batch Details">
                detail_Mdl.setBatchNo(DmShared.foriegnKey(null));
                detail_Mdl.setExpiryDate("");
                //</editor-fold>
//                detail_Mdl.setRate(DmServer.vNull(itemDetail_Mdl.getRate(), "0.00")); Pankaj Sir
                detail_Mdl.setRate(DmServer.vNull(itemDetail_Mdl.getTaxableAmt(), "0.00"));
                //<editor-fold defaultstate="collapsed" desc="GST Rate">
                detail_Mdl.setGstRate(DmServer.vNull(
                        DmShared.vNull(itemDetail_Mdl.getCgstPer()).doubleValue()
                        + DmShared.vNull(itemDetail_Mdl.getSgstPer()).doubleValue()
                        + DmShared.vNull(itemDetail_Mdl.getIgstPer()).doubleValue()
                        + DmShared.vNull(itemDetail_Mdl.getUgstPer()).doubleValue()));
                //</editor-fold>
                //</editor-fold>
                detail_Mdl.setIbCode(DmShared.xNull(itemDetail_Mdl.getIbCode()));
                detail_Mdl.setItemCode(DmShared.xNull(itemDetail_Mdl.getSharedKeyFieldCode()));
                detail_Mdl.setItemName(DmShared.foriegnKey(itemDetail_Mdl.getSharedKeyFieldName()));
//                detail_Mdl.setItemDesc(DmShared.foriegnKey(itemDetail_Mdl.getItemDesc()));
                detail_Mdl.setHsn(DmShared.foriegnKey(itemDetail_Mdl.getMedicineMdl().getHsnName()));
                detail_Mdl.setQuantity(DmServer.vNull(itemDetail_Mdl.getQuantity()));
                detail_Mdl.setUnitCode(DmShared.foriegnKey(itemDetail_Mdl.getMedicineMdl().getPurchaseUnit()));
                detail_Mdl.setUnitName(DmShared.foriegnKey(itemDetail_Mdl.getMedicineMdl().getPurchaseUnitName()));
                //<editor-fold defaultstate="collapsed" desc="GST Unit Code">
                String gstUnitCode = "";
                if (!DmShared.xNull(itemDetail_Mdl.getMedicineMdl().getGstUnit()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "SELECT gum.manualCode as gstUnitCode from gstUnitMast gum \n"
                            + " WHERE gum.code = :code \n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(itemDetail_Mdl.getMedicineMdl().getGstUnit()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gstUnitCode = DmShared.xNull(rsTemp.getString("gstUnitCode"));
                    }
                    rsTemp.close();
                    psTemp.close();
                }
                if (DmShared.xNull(gstUnitCode).trim().equalsIgnoreCase("")) {
                    sqlQuery = "SELECT gum.manualCode as gstUnitCode from unitMast um \n"
                            + " LEFT JOIN gstUnitMast gum on gum.code = um.gstUnitCode \n"
                            + " WHERE um.code = :code \n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(itemDetail_Mdl.getUnit()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gstUnitCode = DmShared.xNull(rsTemp.getString("gstUnitCode"));
                    }
                    rsTemp.close();
                    psTemp.close();
                }
                if (DmShared.xNull(gstUnitCode).trim().equalsIgnoreCase("")) {
                    throw new Exception("gst unit code is mandatory for item: " + DmShared.xNull(itemDetail_Mdl.getSharedKeyFieldName()) + ".");
                }
                detail_Mdl.setGstUnit(DmShared.foriegnKey(gstUnitCode));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Item GST Nature">
                if (DmShared.xNull(itemDetail_Mdl.getMedicineMdl().getItemGstNature()).trim().equalsIgnoreCase("")) {
                    throw new Exception("Item GST nature is mandatory for item: " + DmShared.xNull(itemDetail_Mdl.getSharedKeyFieldName()) + ".");
                }
                detail_Mdl.setItemGstNature(DmShared.foriegnKey(itemDetail_Mdl.getMedicineMdl().getItemGstNature()));
                //</editor-fold>                
                detail_Mdl.setCgstRate(DmServer.vNull(itemDetail_Mdl.getCgstPer()));
                double lineSgstUGstRate = DmShared.vNull(itemDetail_Mdl.getSgstPer()).doubleValue() + DmShared.vNull(itemDetail_Mdl.getUgstPer()).doubleValue();
                detail_Mdl.setSgstRate(DmServer.vNull(lineSgstUGstRate));
                detail_Mdl.setIgstRate(DmServer.vNull(itemDetail_Mdl.getIgstPer()));
                detail_Mdl.setTaxableAmount(DmServer.vNull(itemDetail_Mdl.getTaxableAmt(), "0.00"));
                double grossAmount = DmShared.vNull(itemDetail_Mdl.getAmount());
//                detail_Mdl.setGrossAmount(DmServer.vNull(grossAmount, "0.00")); Pankaj Sir
                detail_Mdl.setGrossAmount(DmServer.vNull(itemDetail_Mdl.getTaxableAmt(), "0.00"));
                double lineSgstUGstValue = DmShared.vNull(itemDetail_Mdl.getSgstAmt()).doubleValue() + DmShared.vNull(itemDetail_Mdl.getUgstAmt()).doubleValue();
                detail_Mdl.setSgstAmount(DmServer.vNull(lineSgstUGstValue, "0.00"));
                detail_Mdl.setCgstAmount(DmServer.vNull(itemDetail_Mdl.getCgstAmt(), "0.00"));
                detail_Mdl.setIgstAmount(DmServer.vNull(itemDetail_Mdl.getIgstAmt(), "0.00"));
                detail_Mdl.setCessAmount("0.00");
                detail_Mdl.setCessNaAmount("0.00");
                double totalDiscount = DmShared.vNull(itemDetail_Mdl.getDiscount()).doubleValue();
                detail_Mdl.setDiscountAmount(DmServer.vNull(totalDiscount, "0.00"));
                detail_Mdl.setOtherCharges("0.00");
                detail_Mdl.setNetAmount(DmServer.vNull(itemDetail_Mdl.getNetAmt(), "0.00"));
                egst_Mdl.getItemList().add(detail_Mdl);
            }
            //<editor-fold defaultstate="collapsed" desc="Invoice Fields">
            egst_Mdl.setEinv_version("1.1"); //<Ag> TBD is it always remain 1.1
            egst_Mdl.setTaxSchemeCode(String.valueOf(DmEnum.Egst_TaxScheme.GST.getCode()));
            egst_Mdl.setTaxSchemeName(DmEnum.Egst_TaxScheme.GST.toString());  //<Ag> TBR
            egst_Mdl.setReverseCharge(DmEnum.DmYesNo.NO.getEnumShortName());
            egst_Mdl.setIgstOnIntra(DmEnum.DmYesNo.NO.getEnumShortName());  //<Ag> TBD
            egst_Mdl.setRoundOfAmount(DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
            egst_Mdl.setDiscountAmount(DmServer.vNull(mrTemp.getDiscountAmt(), "0.00"));
            egst_Mdl.setTotalInvAmtInFc("0.00");
            egst_Mdl.setOtherAmount("0.00");
            //<editor-fold defaultstate="collapsed" desc="Paye Details">
            egst_Mdl.setPayee_Name(null);
            egst_Mdl.setPayee_AccountNo(null);
            egst_Mdl.setPayee_ModeOfPayment(null);
            egst_Mdl.setPayee_BranchCode(null);
            egst_Mdl.setPayee_CrTrn(null);
            egst_Mdl.setPayee_PaymentInstruction(null);
            egst_Mdl.setPayee_PaymentTerms(null);
            egst_Mdl.setPayee_DirectDebit(null);
            egst_Mdl.setPayee_CreditDays(null);
            egst_Mdl.setPayee_PaidAmount(null);
            egst_Mdl.setPayee_PaymentDue(null);
            //</editor-fold>
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return egst_Mdl;
    }

    private GstDocument_Mdl getGstDetail(String vTypeCode, Connection cnTemp) { //<Ag> TBR: procedureName- getGstDocumentDetail
        GstDocument_Mdl gst_Mdl = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            sqlQuery = "select \n"
                    + " vt.documentTypeCode, vt.documentNatureCode \n"
                    + " from voucher_Type vt \n"
                    + " where vt.v_Type = :v_Type \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_Type", DmShared.xNull(vTypeCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                gst_Mdl = new GstDocument_Mdl();
                if (!DmShared.xNull(rsTemp.getString("documentTypeCode")).trim().equalsIgnoreCase("")) {
                    gst_Mdl.setGstDocTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                    gst_Mdl.setGstDocTypeName(DmEnum.GstDocumentType.getViaShortName(DmShared.xNull(rsTemp.getString("documentTypeCode"))).getValue());
                }
                if (!DmShared.xNull(rsTemp.getString("documentNatureCode")).trim().equalsIgnoreCase("")) {
                    gst_Mdl.setGstDocNatureCode(DmShared.foriegnKey(DmShared.xNull(rsTemp.getString("documentNatureCode"))));
                    gst_Mdl.setGstDocNatureName(DmShared.foriegnKey(DmEnum.GstDocumentNature.getViaShortName(DmShared.xNull(rsTemp.getString("documentNatureCode"))).getValue()));
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
        }
        return gst_Mdl;
    }

//</editor-fold>  
    @Override
    public DmResult<String> processAutoVoucherAdjustment(String strKey, String partyCode) {
        DmResult<String> dmrTemp = new DmResult();
        dmrTemp.setData(strKey);
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            processAutoVoucherAdjustment(strKey, partyCode, cnTemp);

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);

//            saveLog(strKey, null, null, DmEnum.DmTransactionMode.VOUCHER_ADJUSTMENT, null, null, false, null);
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void processAutoVoucherAdjustment(String strKey, String partyCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", csvSbDocIds = "";
        AdjustmentSave_Mdl adjSaveMdl = null;
        GrdAdjustment_Mdl gaMdl = null;
        double amtCr = 0, balanceAmt = 0, adjustmentAmt = 0;
        boolean flagAdjustment = false, flagPartialAdjustment = false, flagSingleSBill = false;

        try {
            /*=======================================<< Delete Adjustment >>=============================================*/
            AccountAdjustment_Server.deleteRecord(strKey, cnTemp);

            /*=======================================<< Save Adjustment >>=============================================*/
            sqlQuery = "SELECT STRING_AGG (CONVERT(NVARCHAR(max),vSbd.sbDocId), ',') AS csv \n"
                    + " FROM ( \n"
                    + "     SELECT DISTINCT sbd.sbDocId\n"
                    + "     FROM saleBillDetail sbd \n"
                    + "     WHERE sbd.docId = :docId \n"
                    + "         AND sbd.sbDocId IS NOT NULL\n"
                    + " ) vSbd \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                csvSbDocIds = DmShared.xNull(rsTemp.getString("csv"));
            }
            rsTemp.close();
            psTemp.close();

            if (DmShared.xNull(csvSbDocIds).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "No any sales bill found for adjustment!");
            } else {
                flagSingleSBill = csvSbDocIds.split(",").length == 1;
            }

            sqlQuery = "SELECT COUNT(*) AS cnt\n"
                    + "FROM saleBillDetail sbd\n"
                    + "WHERE sbd.docId = :docId \n"
                    + "AND ISNULL(sbd.isFooter, 0) IN (0, 3) \n"
                    + "AND sbd.sbDocId IS NULL\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    flagSingleSBill = false;
                }
            }
            rsTemp.close();
            psTemp.close();

            if (flagSingleSBill == false) {
                throw new DmException(0, "Auto voucher adjustment can be processed, if return is done only for one sales bill!");
            }

            sqlQuery = "SELECT TOP 1 lv.docId, lv.v_SNo, lv.v_Date, lv.subCode, lv.amtCr"
                    + " FROM ledger lv"
                    + " WHERE lv.docId = :docId"
                    + " AND lv.subCode = :subCode"
                    + " AND ISNULL(lv.amtCr, 0) > 0"
                    + " ORDER BY lv.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(strKey));
            psTemp.setString("subCode", DmShared.xNull(partyCode));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                adjSaveMdl = new AdjustmentSave_Mdl();
                adjSaveMdl.setVr_DocId(DmShared.xNull(rsTemp.getString("docId")));
                adjSaveMdl.setVr_V_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                adjSaveMdl.setVr_V_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                adjSaveMdl.setVr_SubCode(DmShared.xNull(rsTemp.getString("subCode")));
                amtCr = DmShared.vNull(rsTemp.getString("amtCr"));
            }
            rsTemp.close();
            psTemp.close();

            if (adjSaveMdl == null) {
                return;
            }

            balanceAmt = DmShared.vNull(amtCr).doubleValue();
            for (String sbDocId : csvSbDocIds.split(",")) {
                adjustmentAmt = 0;
                if (DmShared.vNull(balanceAmt).doubleValue() > 0) {
                    sqlQuery = "SELECT TOP 1 lb.docId, lb.v_SNo, lb.v_Date, lb.subCode, lb.amtDr"
                            + " FROM ledger lb"
                            + " WHERE lb.docId = :docId"
                            + " AND lb.subCode = :subCode"
                            + " AND ISNULL(lb.amtDr, 0) > 0"
                            + " ORDER BY lb.v_SNo";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(sbDocId));
                    psTemp.setString("subCode", DmShared.xNull(partyCode));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gaMdl = new GrdAdjustment_Mdl();
                        gaMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                        gaMdl.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                        gaMdl.setV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                        gaMdl.setAcCode(DmShared.xNull(rsTemp.getString("subCode")));
                        if (DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() > DmShared.vNull(balanceAmt).doubleValue()) {
                            adjustmentAmt = DmShared.vNull(balanceAmt).doubleValue();
                        } else {
                            adjustmentAmt = DmShared.vNull(rsTemp.getString("amtDr")).doubleValue();
                        }
                        balanceAmt -= adjustmentAmt;
                        gaMdl.setAdjustment(DmServer.vNull(adjustmentAmt, "0.00"));
                        gaMdl.setAdjustmentDrCr("Cr");
                        adjSaveMdl.getLstAdjustments().add(gaMdl);
                        flagAdjustment = true;
                    }
                    rsTemp.close();
                    psTemp.close();
                } else {
                    flagPartialAdjustment = true;
                }
            }

            if (flagPartialAdjustment) {
                throw new DmException(0000, "Unable to save adjustment for all bill.");
            } else {
                if (flagAdjustment) {
                    AccountAdjustment_Server.saveRecord(adjSaveMdl, cnTemp);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void saveLog(String strKey, ModifyReason_Mdl modifyReasonMdl, WholeSaleBillReturn_Mdl mrTemp, DmEnum.DmTransactionMode transactionMode, DmEnum.DmTransactionMode controlText) {
        String keyName_1 = "", description = "", shortName = "";
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
                throw new DmException(0, "ModifyReasonMdl is requied for saving log.");
            }
            //</editor-fold>
            modifyReasonMdl.setMode(DmShared.xNull(transactionMode.getValue()));
            //<editor-fold defaultstate="collapsed" desc="Save Log">
            WholeSaleBillReturn_Mdl oldMdl = null;
            //create new reason mode in case of print, new
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DISCARD.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                //check if latest record's old data is saved in moverec
                oldMdl = (WholeSaleBillReturn_Mdl) DmServer.getSsnOldData(getClass().getName(), this);
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
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    keyName_1 = DmShared.xNull(oldMdl.getSharedKeyFieldName());
                } else {
                    keyName_1 = DmShared.xNull(mrTemp.getSharedKeyFieldName());
                }
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(DmShared.xNull(strKey));
                modifyData_Mdl.setKeyName_1(DmShared.xNull(keyName_1));
                modifyData_Mdl.setKeyDescription_1(DmShared.xNull(keyName_1));
                modifyData_Mdl.setModuleName(new pmPharmacy().moduleName);
                modifyData_Mdl.setMenuName(new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption);
                if (controlText != null) {
                    modifyData_Mdl.setControlText(controlText.getValue());
                }
                modifyData_Mdl.setModelClassName(WholeSaleBillReturn_Mdl.class.getName());
                modifyData_Mdl.setVrTypeDescription(description);
                modifyData_Mdl.setVrTypeSName(DmShared.xNull(shortName));
                modifyData_Mdl.setTableName("saleBill");
                //saving old record for edit, delete
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DISCARD.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    modifyData_Mdl.setOldData(DmServer.convertToJson(oldMdl, new TypeToken<WholeSaleBillReturn_Mdl>() {
                    }));
                }
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                    modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<WholeSaleBillReturn_Mdl>() {
                    }));
                }
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

}
