package aarogya.pharmacy.saleBillWholeSale.server;

import aarogya.pharmacy.saleBillWholeSale.shared.*;
import aarogya.pharmacy.self.server.Pharmacy_Server;
import aarogya.pharmacy.self.shared.BarCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_Server;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ITSType;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemBatchType;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import aarogya.selfToolkit.uhidPopup.shared.Layout_Mdl;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmPrepareStatement.server.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum.DmLocation;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.eGstPopup.server.EGstPopup_Server;
import dataman.dmToolkit.eGstPopup.shared.IEGstPopup;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.EGstDetail_Mdl;
import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.gst.shared.Gst_Shared;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.gst.shared.Party_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.partyDetailPopup.server.PartyDetailPopup_Server;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SaleBillWholeSale_SrvImpl extends DmRSSPrivate implements SaleBillWholeSale_Srv {

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

            sqlQuery = " SELECT mb.DocId  "
                    + " FROM saleBill mb  "
                    + " LEFT JOIN voucher_type vt ON vt.v_type = mb.v_type  "
                    + " WHERE vt.category IN ( " + getVoucherCategory(voucherCategory) + " ) "
                    + " and mb.site_Code=:siteCode "
                    + " and mb.v_Prefix =:vPrefix ";
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

    private SaleBillWholeSale_Mdl getMoveRec(String strKey, Connection cnTemp) throws Exception {
        DmResult<SaleBillWholeSale_Mdl> dmrTemp = new DmResult();
        CardPaymentPopup_Mdl cardPaymentPopup_Mdl = new CardPaymentPopup_Mdl();
        SaleBillWholeSale_Mdl mrTemp = new SaleBillWholeSale_Mdl();
        dmrTemp.setData(mrTemp);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TxtHMedicine_Mdl medicineMdl;
        SearchUHID_Mdl searchMdl = new SearchUHID_Mdl();

        try {
            sqlQuery = "SELECT mb.docId, mb.recId, mb.v_Type, pbvt.description, mb.v_Date, prvt.category, "
                    + "     pr.roomCode, mb.departmentCode, sdm.name AS departmentName, sg.name as patientName, "
                    + "     mb.consultantCode, sgcon.name AS consultantName, mb.preparedBy, mb.u_Name, mb.remark, mb.oh_Amt_Gross, mb.oh_Amt_Net, mb.oh_Amt_LandedValue, "
                    + "     mb.oh_Amt_BAMT, mb.oh_Amt_RoundOff, mb.oh_Amt_CGST, mb.oh_Amt_SGST, mb.oh_Amt_UGST, mb.oh_Amt_Taxable, mb.oh_Amt_Discount,mb.oh_Amt_Discount2, "
                    + "     mb.recIdPrefix, mb.v_No, prec.v_type as precVType, prec.recid as precRecID, prec.recIdPrefix as precRecIdPrefix, prec.v_No as precRecIdCounter, "
                    + "     vtprec.description as precVDescription, vtprec.Category as rcptVTypeCategory, "
                    + "     vtprec.attachPaymentMode AS attachPaymentMode, mb.isCancelled, "
                    + "     mb.u_EntDt_LatestLine,mb.sBillType"
                    + "     ,mb.partyType,mb.partySubCode"
                    + "     , mb.paymentSource , sg.gstin ,sg.gstinDate  "
                    + "     , mb.vehicleNo, mb.transporterCode , sgtp.name as transporterName"
                    + "     , mb.transportMode , mb.lrNo , mb.lrDate , mb.isEWayBillRequired , mb.eWayBillDt , mb.eWayBillNo "
                    + "     , mb.locationCode, lo.name as locationName, mb.vehicleTypeCode , vtm.name as vehicleTypeName, mb.placeOfsupply as stateCode \n"
                    + "     , sm.name as stateName ,mb.eInvIrnNo,mb.oh_Amt_IGST,pbvt.category as billCategory , egt.mongoLinkId \n"
                    + "     , sg.dueDays, mb.dueDate,wsgps.docId As gatePassDocId,wsgps.approvedBy, mb.profitPercentage \n"
                    + "     , CASE  WHEN wsgps.approvedBy IS NULL THEN ''  ELSE wsgps.recId  END AS gatePassRecId  , mb.partyPODate , mb.partyPONO \n"
                    + "  FROM saleBill mb "
                    + "  LEFT JOIN voucher_type pbvt ON mb.v_Type = pbvt.v_Type    "
                    + "  LEFT JOIN wholeSaleGatePass wsgps ON mb.docId = wsgps.sbDocId    "
                    + "  LEFT JOIN patientregistration pr ON mb.regDocid=pr.regDocid  "
                    + "  LEFT JOIN subgroup sg ON sg.subCode= mb.partySubCode  "
                    + "  LEFT JOIN voucher_type prvt ON pr.v_Type=prvt.v_Type    "
                    + "  LEFT JOIN SiteMast prsm ON pr.site_Code=prsm.code  "
                    + "  LEFT JOIN patientuhid pu ON mb.uhid = pu.docId  "
                    + "  LEFT JOIN subgroup sdm ON mb.departmentCode=sdm.subcode  "
                    + "  LEFT JOIN consultantmast cm ON mb.consultantCode=cm.subCode  "
                    + "  LEFT JOIN subGroup AS sgcon ON cm.subCode = sgcon.subCode  "
                    + "  LEFT JOIN patientReceipt AS prec ON mb.DocId = prec.docId "
                    + "  LEFT JOIN stateMast AS sm ON sm.code = mb.placeOfsupply "
                    + "	 LEFT JOIN hl7Mast as idType on idType.code = mb.idTypeCode "
                    + "	 LEFT JOIN voucher_type vtprec ON prec.v_type = vtprec.v_type "
                    + "	 LEFT JOIN saleBill est ON est.docId = mb.estimateDocId "
                    + "  LEFT JOIN locationMast lo on lo.code=mb.locationCode \n"
                    + "	 LEFT JOIN subGroup sgtp ON sgtp.subCode = mb.transporterCode\n"
                    + "  left join vehicleTypeMast vtm on vtm.code = mb.vehicleTypeCode \n"
                    + "  left join eGst egt on egt.docid = mb.docid \n"
                    + "  WHERE mb.docID = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdSaleBill().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdSaleBill().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecIdSaleBill().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setGatePassRecId(DmShared.xNull(rsTemp.getString("gatePassRecId")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setInvoiceDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setPartySubCode(DmShared.xNull(rsTemp.getString("partySubCode")));
                mrTemp.seteInvIrnNo(DmShared.xNull(rsTemp.getString("eInvIrnNo")));
                mrTemp.setPartyGstIn(DmShared.xNull(rsTemp.getString("gstIn")));
                mrTemp.setPartyGstinDate(DmShared.xNull(rsTemp.getString("gstinDate")));

                if (DmShared.xNull(rsTemp.getString("sBillType")).equalsIgnoreCase(DmShared.iNull(STEnum.WholeSaleType.WALK_IN.getId()).toString())) {
                    mrTemp.setTypeName(DmShared.xNull(STEnum.WholeSaleType.WALK_IN.getValue()));
                    mrTemp.setTypeCode(DmShared.iNull(STEnum.WholeSaleType.WALK_IN.getId()).toString());
                } else {
                    mrTemp.setTypeName(DmShared.xNull(STEnum.WholeSaleType.DEBTOR.getValue()));
                    mrTemp.setTypeCode(DmShared.iNull(STEnum.WholeSaleType.DEBTOR.getId()).toString());
                }
                //===================================<MJ>==============================//
                mrTemp.setDueDaysMaster(DmShared.xNull(rsTemp.getString("dueDays")));
                mrTemp.setDueDate(DmShared.dNull(rsTemp.getDate("dueDate")));
                //===================================<****>==============================//

                mrTemp.setPartyPODate(DmShared.dNull(rsTemp.getDate("partyPODate")));
                mrTemp.setPartyPONO(DmShared.xNull(rsTemp.getString("partyPONO")));

                mrTemp.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));

                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));

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
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setGatePassDocId(DmShared.xNull(rsTemp.getString("gatePassDocId")));
                searchMdl.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                searchMdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("billCategory")));

                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setPlaceOfSupplyName(DmShared.xNull(rsTemp.getString("stateName")));
                if (!DmShared.xNull(rsTemp.getString("transportMode")).trim().equalsIgnoreCase("")) {
                    mrTemp.setTransportModeName(DmShared.xNull(DmEnum.TransportMode.getViaKey(DmShared.iNull(rsTemp.getString("transportMode")).intValue()).getValue()));
                    mrTemp.setTransportModeCode(DmShared.xNull(rsTemp.getString("transportMode")));
                }
                mrTemp.seteWayBillRequiredCode(DmShared.xNull(rsTemp.getString("isEWayBillRequired")));
                mrTemp.seteWayBillRequiredName(DmShared.iNull(rsTemp.getString("isEWayBillRequired")).intValue() == 1 ? "Yes" : "No");
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
                mrTemp.setMongoLinkId(DmShared.xNull(rsTemp.getString("mongoLinkId")));
                mrTemp.setProfitPercentage(DmServer.vNull(rsTemp.getString("profitPercentage")));

                mrTemp.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }

                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("billCategory")));
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select mbd.docId, mbd.v_SNo, mm.isBestBefore, mbd.hsnCode, mbd.itemGSTNature, mbd.itemCode, mm.name AS itemName, mbd.quantity, "
                    + "         mbd.skuQuantity as totalSkuQty, mbd.rate, ic.name as itemCategory, mbd.oh_at_Discount, mbd.oh_at_Discount2, mbd.oh_Amt_Discount, mbd.oh_Amt_Discount2, um.decimalPlaces as skuDecimalPlaces, "
                    + "         mbd.oh_Amt_Net, mbd.landedSkuValue, mbd.oh_Amt_Gross, mbd.oh_at_CGST, mbd.isFooter, mm.sku, um.unit AS skuName, ump.gstUnitCode, "
                    + "         mbd.sbDocId, mbl.recId AS mBillNo "
                    + "         ,CAST(mm.purchaseUnitConversionRate * mbd.quantity AS decimal(18, 7)) AS skuQuantity "
                    + "         ,mm.purchaseUnitConversionRate, "
                    + "         ump.decimalPlaces as purchaseUnitDecimalPlaces, mbd.oh_Amt_CGST, mbd.oh_Amt_SGST, mbd.oh_at_SGST, mbd.oh_Amt_Taxable, mbd.oh_Amt_UGST, "
                    + "         mbd.oh_at_UGST, mbd.mrp, mbd.skuRate, mbd.skuMrp, mm.allowNegativeStock, mm.isBarcodeActive, mbd.isReturn, mbd.oh_Ac_CGST, "
                    + "         mbd.oh_Ac_SGST, mbd.oh_Ac_UGST, mbd.oh_Ac_Sales, mm.itsBasedOn, mbd.batchNo, mbd.expiryDate, mbd.manufacturingDate, "
                    + "         mbd.ibCode, mm.restrictGroup, mm.purchaseUnit, ump.unit purchaseUnitName, mbd.skuQtyForRate,"
                    + "         (IsNull(mbd.quantity,0)-IsNull(mbd.returnedQty,0)) as sbQuantity ,mbd.returnedQty"
                    + "         ,ig.code as groupCode,ig.name as groupName,mbd.discountGivenBy,mbd.discountReason,hm.hsn AS hsnNo "
                    + "         ,mbd.freeWithItemCode,mbd.freeQty,mm.isReturnableItem, mm.isDiscountAllow, mm.isSaleRateEditable, mm.itsBasedOn, mbd.barcode "
                    + "            ,mbd.oh_Ac_IGST,mbd.oh_Amt_IGST,mbd.oh_at_IGST, mbd.discountPerUnit , mbd.wholeSaleRate , mbd.wholeSaleRateActual   "
                    + "	from saleBillDetail AS mbd "
                    + "	left join saleBill AS mbl ON mbd.sbDocId = mbl.docId "
                    + "	left join itemMast AS mm ON mbd.itemCode = mm.code "
                    + "	left join hsnMast AS hm ON hm.code = mm.hsnCode "
                    + " left join itemGroup ig on ig.code = mm.itemGroup "
                    + " left join itemCat ic on ic.code = mm.itemCategoryCode "
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
                detailMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                detailMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                detailMdl.setDiscountGivenByTag(DmShared.xNull(rsTemp.getString("discountGivenBy")));
                detailMdl.setDiscountReasonName(DmShared.xNull(rsTemp.getString("discountReason")));
                detailMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                detailMdl.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));
                detailMdl.setDiscountPerUnit(DmServer.vNull(rsTemp.getString("discountPerUnit")));

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).intValue());
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).intValue() == 1);
                medicineMdl.setGstUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setHsnNo(DmShared.xNull(rsTemp.getString("hsnNo")));
                detailMdl.setMedicineMdl(medicineMdl);

                detailMdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                detailMdl.setWholeSaleRate(DmServer.vNull(rsTemp.getString("wholeSaleRate")));
                detailMdl.setWholeSaleRateActual(DmServer.vNull(rsTemp.getString("wholeSaleRateActual")));
                detailMdl.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));

                if (DmShared.vNull(rsTemp.getString("returnedQty")).doubleValue() == 0) {
                    detailMdl.setStatus("0");//no Color
                } else if (DmShared.vNull(rsTemp.getString("sbQuantity")).doubleValue() == 0) {
                    detailMdl.setStatus("2");//green color
                } else if (DmShared.vNull(rsTemp.getString("returnedQty")).doubleValue() != 0) {
                    detailMdl.setStatus("1");//light gray
                }

                detailMdl.setFreeQuantityInPurchaseUnit(DmServer.vNull(rsTemp.getString("freeQty")));
                detailMdl.setPrvFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                detailMdl.setFreeWithItemCode(DmShared.xNull(rsTemp.getString("freeWithItemCode")));
                mrTemp.getLstFreeWithItemCodes().add(DmShared.xNull(rsTemp.getString("freeWithItemCode")));

                if (Math.abs(DmShared.vNull(rsTemp.getString("freeQty")).doubleValue()) > 0) {
                    detailMdl.setSchemeItem(true);
                }

                detailMdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setPrvQty(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                detailMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                detailMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                detailMdl.setUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                detailMdl.setQuantitySKU(DmServer.vNull(rsTemp.getString("skuQuantity")));
                detailMdl.setTotalQuantitySKU(DmServer.vNull(rsTemp.getString("totalSkuQty")));
                detailMdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                detailMdl.setReturnSale(DmShared.iNull(rsTemp.getString("isReturn")).intValue() == 1);

                detailMdl.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
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

                detailMdl.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()) + DmShared.vNull(rsTemp.getString("oh_at_IGST")).doubleValue(), "0.00"));
                detailMdl.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()) + DmShared.vNull(rsTemp.getString("oh_Amt_IGST")).doubleValue(), "0.00"));

                detailMdl.setSbDocId(DmShared.xNull(rsTemp.getString("sbDocId")));

                detailMdl.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                detailMdl.setItemCodeCsv(DmShared.xNull(Pharmacy_Server.getItemCodeCsv(detailMdl.getBarcode(), mrTemp.getVoucherDate(), mrTemp.getDepartmentCode(), DmServer.getSsnSiteCode(this), ItemBatchType.NON_EXPIRED_BATCH, false, true, cnTemp)));

                mrTemp.getGrdMediDetails().add(detailMdl);
            }
            psTemp.close();
            rsTemp.close();

            if (isReturnedFromSaleBillReturn(mrTemp.getSharedKeyFieldCode(), true, cnTemp)) {
                mrTemp.setIsReturnExist("1");
            } else {
                mrTemp.setIsReturnExist("0");
            }

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

            cardPaymentPopup_Mdl = CardPaymentPopup_Server.getCardListViaDocId(strKey, cnTemp);
            mrTemp.getCardPayment().getGrdCardPaymentList().addAll(cardPaymentPopup_Mdl.getGrdCardPaymentList());
            mrTemp.setCardPayment(cardPaymentPopup_Mdl);

            /*==========================Get transPartyInfo Details=============================*/
            mrTemp.getPartyDetail_Mdl().putAll(PartyDetailPopup_Server.getPartyTransRecord(strKey, cnTemp));
//            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);

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
    public DmResult<SaleBillWholeSale_Mdl> getMoveRec(String strKey) {
        DmResult<SaleBillWholeSale_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        SaleBillWholeSale_Mdl mrTemp = new SaleBillWholeSale_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = getMoveRec(strKey, cnTemp);
            dmrTemp.setData(mrTemp);

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
    public DmResult<String> saveRecord(String voucherCategory, SaleBillWholeSale_Mdl mrTemp, SaleBillWholeSale_Mdl prevMdl, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, isEstimateBill = false, isEstimate = false, xisCTPAllow = false, allowReturnWithoutSale = false; //, allowReturnValue = false
        String sqlQuery, userName, siteCode, receiptVType = "", saleQty = "";
        TxtHMedicine_Mdl medicineMdl;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        CardType cardType = null;
        dmrTemp.setData("");

        try {

            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            isEstimateBill = voucherCategory.equalsIgnoreCase(pvc.PHARMACY_ESTIMATE_BILL);
            isEstimate = voucherCategory.equalsIgnoreCase(pvc.PHARMACY_ESTIMATE);

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

            DmServer.chkIfDocumentUsed("docId", "eGst", mrTemp.getSharedKeyFieldCode(), cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                //<editor-fold defaultstate="collapsed" desc="New Case and DocId Formation">
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdSaleBill(DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdSaleBill(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdSaleBill().getRecIdValue()));
                //</editor-fold>

                DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdSaleBill(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //<editor-fold defaultstate="collapsed" desc="chk Return">
            if (!isEstimate) {
                isReturnedFromSaleBillReturn(mrTemp.getSharedKeyFieldCode(), false, cnTemp);
            }
            //</editor-fold>

            /*==============================<< Check for Credit Limit >>====================================*/
            STServer.checkCustomerCreditLimit(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), DmShared.xNull(mrTemp.getPartySubCode()), DmShared.vNull(mrTemp.getNetAmount()).doubleValue(), cnTemp);

            if (blnAdd) {
                //<editor-fold defaultstate="collapsed" desc="Insert SaleBill">
                sqlQuery = " insert into saleBill (docId, v_Type, v_No, recId, v_Prefix, v_Date, "
                        + "     departmentCode, consultantCode, remark, u_EntDt, preparedDate, preparedBy, site_Code, oh_Amt_Gross, oh_Amt_Net, oh_Amt_BAMT, "
                        + "     oh_Amt_RoundOff, oh_Amt_CGST, oh_Amt_SGST, oh_Amt_Taxable, oh_Amt_Discount, oh_Amt_Discount2, recIdPrefix, oh_Amt_UGST,"
                        + "     u_EntDt_LatestLine,partyType,placeOfSupply,\n"
                        + "     oh_Amt_LandedValue, paymentSource,paymentMode ,sBillType, partySubCode, transporterCode, transportMode,"
                        + "     lrNo, lrDate, IsEWayBillRequired, vehicleNo, vehicleTypeCode, locationCode,oh_Amt_IGST ,dueDate, profitPercentage , partyPODate , partyPONO )"
                        + " values (:docId, :vType, :vNo, :recId, :vPrefix, :vDate, :departmentCode,"
                        + "      :consultantCode, :remark, :uEntDt, :uEntDt, :userName, :siteCode, :ohAmtGross, :ohAmtNet, :ohAmtBAMT, :oh_Amt_RoundOff, "
                        + "      :ohAmtCGST, :ohAmtSGST, :ohAmtTaxable, :ohAmtDiscount, :ohAmtDiscount2, :recIdPrefix, :ohAmtUGST,"
                        + "      :uEntDt,:partyType,:placeOfSupply, "
                        + "      :oh_Amt_LandedValue,:paymentSource,:paymentMode, :sBillType, :partySubCode, :transporterCode, :transportMode,"
                        + "      :lrNo, :lrDate, :IsEWayBillRequired, :vehicleNo, :vehicleTypeCode, :locationCode,:oh_Amt_IGST ,:dueDate, :profitPercentage , :partyPODate , :partyPONO )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTemp.getTransporterCode()));
                psTemp.setString("lrNo", DmShared.foriegnKey(mrTemp.getLrNo()));
                psTemp.setDate("lrDate", DmServer.convertDate(mrTemp.getLrDate()));
                psTemp.setString("vehicleNo", DmShared.foriegnKey(mrTemp.getVehicleNo()));
                psTemp.setString("transportMode", DmShared.foriegnKey(mrTemp.getTransportModeCode()));
                psTemp.setInt("IsEWayBillRequired", DmShared.iNull(mrTemp.geteWayBillRequiredCode()).intValue());
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("vehicleTypeCode", DmShared.foriegnKey(mrTemp.getVehicleTypeCode()));
                psTemp.setString("vNo", DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("vPrefix", voucherPrefix);
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("userName", DmShared.xNull(userName));
                psTemp.setString("siteCode", DmShared.xNull(siteCode));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyType()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdSaleBill().getPrefix()));
                psTemp.setString("ohAmtGross", DmServer.vNull(mrTemp.getGrossAmt(), "0.00"));
                psTemp.setString("ohAmtDiscount", DmServer.vNull(mrTemp.getDiscountAmt(), "0.00"));
                psTemp.setString("ohAmtDiscount2", DmServer.vNull(mrTemp.getDiscountAmt2(), "0.00"));
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
                psTemp.setString("paymentMode", DmShared.xNull(mrTemp.getPaymentMode()));
                psTemp.setString("sBillType", DmShared.xNull(mrTemp.getTypeCode()));
                psTemp.setString("partySubCode", DmShared.xNull(mrTemp.getPartySubCode()));
                psTemp.setString("paymentSource", mrTemp.getCardPayment().getPaymentSource() == null ? null : DmShared.iNull(mrTemp.getCardPayment().getPaymentSource().getEnumValue()).toString());
                psTemp.setDate("dueDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDueDate())));
                psTemp.setString("profitPercentage", DmServer.vNull(mrTemp.getProfitPercentage()));
                psTemp.setDate("partyPODate", DmServer.convertDate(DmShared.dNull(mrTemp.getPartyPODate())));
                psTemp.setString("partyPONO", DmShared.xNull(mrTemp.getPartyPONO()));

//</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="UPDATE saleBill">
                sqlQuery = "UPDATE saleBill SET "
                        + " v_Date = :vDate, "
                        + " u_EntDt = :uEntDt, "
                        + " u_EntDt_LatestLine= :uEntDt ,"
                        + " placeOfSupply= :placeOfSupply ,"
                        + " departmentCode = :departmentCode, consultantCode = :consultantCode,"
                        + " remark=:remark,"
                        + " oh_Amt_Gross=:oh_Amt_Gross,"
                        + " oh_Amt_Discount=:oh_Amt_Discount,"
                        + " oh_Amt_Discount2=:oh_Amt_Discount2,"
                        + " oh_Amt_CGST=:oh_Amt_CGST,"
                        + " oh_Amt_SGST=:oh_Amt_SGST,"
                        + " oh_Amt_UGST=:oh_Amt_UGST,"
                        + " oh_Amt_IGST=:oh_Amt_IGST,"
                        + " oh_Amt_Taxable=:oh_Amt_Taxable,"
                        + " oh_Amt_Net=:oh_Amt_Net,"
                        + " oh_Amt_LandedValue=:oh_Amt_LandedValue,"
                        + " oh_Amt_BAMT=:oh_Amt_BAMT,"
                        + " oh_Amt_RoundOff=:oh_Amt_RoundOff,"
                        + " sBillType=:sBillType,"
                        + " partySubCode=:partySubCode,"
                        + " transporterCode=:transporterCode,"
                        + " transportMode=:transportMode,"
                        + " lrNo=:lrNo,"
                        + " lrDate=:lrDate,"
                        + " IsEWayBillRequired=:IsEWayBillRequired,"
                        + " vehicleNo=:vehicleNo,"
                        + " vehicleTypeCode=:vehicleTypeCode,"
                        + " locationCode=:locationCode,"
                        + " dueDate=:dueDate,"
                        + " profitPercentage=:profitPercentage,"
                        + " u_Name = :userName , "
                        + " partyPODate=:partyPODate ,"
                        + " partyPONO = :partyPONO "
                        + " WHERE DocID = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("transporterCode", DmShared.foriegnKey(mrTemp.getTransporterCode()));
                psTemp.setString("lrNo", DmShared.foriegnKey(mrTemp.getLrNo()));
                psTemp.setDate("lrDate", DmServer.convertDate(mrTemp.getLrDate()));
                psTemp.setString("vehicleNo", DmShared.foriegnKey(mrTemp.getVehicleNo()));
                psTemp.setString("transportMode", DmShared.foriegnKey(mrTemp.getTransportModeCode()));
                psTemp.setInt("IsEWayBillRequired", DmShared.iNull(mrTemp.geteWayBillRequiredCode()).intValue());
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("vehicleTypeCode", DmShared.foriegnKey(mrTemp.getVehicleTypeCode()));
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
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("sBillType", DmShared.xNull(mrTemp.getTypeCode()));
                psTemp.setString("partySubCode", DmShared.xNull(mrTemp.getPartySubCode()));
                psTemp.setDate("dueDate", DmServer.convertDate(DmShared.dNull(mrTemp.getDueDate())));
                psTemp.setString("profitPercentage", DmServer.vNull(mrTemp.getProfitPercentage()));

                psTemp.setDate("partyPODate", DmServer.convertDate(DmShared.dNull(mrTemp.getPartyPODate())));
                psTemp.setString("partyPONO", DmShared.xNull(mrTemp.getPartyPONO()));

                //</editor-fold>
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*=============================<< Validate Removed Received Items >>================================*/
            ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), DmEnum.StockEntryType.OTHER, cnTemp, this);
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp, true);
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="INSERT IN SALE BILL DETAIL">
            sqlQuery = "Delete From saleBillDetail Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from transPartyInfo >>====================================*/
            PartyDetailPopup_Server.deletePartyTransRecord(mrTemp.getSharedKeyFieldCode(), cnTemp);

            //<editor-fold defaultstate="collapsed" desc="SavePartyTransRecord">
            if (mrTemp.getPartyDetail_Mdl() != null) {
                mrTemp.getPartyDetail_Mdl().put(DmEnum.PartyTransRecordType.TRANSACTION_SITE.getCode(), PartyDetailPopup_Server.getSiteDetailPopup_Mdl(DmServer.getSsnSiteCode(this), cnTemp));
                mrTemp.getPartyDetail_Mdl().put(DmEnum.PartyTransRecordType.DISPATCH_FROM.getCode(), PartyDetailPopup_Server.getSiteDetailPopup_Mdl(DmServer.getSsnSiteCode(this), cnTemp));
                mrTemp.getPartyDetail_Mdl().put(DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), PartyDetailPopup_Server.getPartyDetailPopup_Mdl(mrTemp.getTransporterCode(), cnTemp));
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
            int vSNo = 0;
            for (GrdMedicine_Mdl detail_Mdl : mrTemp.getGrdMediDetails()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    detail_Mdl.setvSno(String.valueOf(++vSNo));
                    medicineMdl = detail_Mdl.getMedicineMdl();
                    sqlQuery = "insert into saleBillDetail (docId, v_SNo, itemCode, hsnCode, saleUnitCode, quantity, "
                            + "		oh_at_Discount, oh_at_Discount2, oh_Amt_Discount, oh_Amt_Discount2, oh_Amt_Net, oh_Amt_CGST, oh_at_CGST, oh_Amt_SGST, oh_at_SGST, oh_at_UGST, "
                            + "         oh_Amt_UGST, rate, oh_Amt_Taxable, itemGSTNature, mrp, isReturn, skuQuantity, oh_Ac_Sales, oh_Ac_CGST, "
                            + "         oh_Ac_UGST, oh_Ac_SGST, oh_Amt_Gross, batchNo, manufacturingDate, expiryDate, ibCode, "
                            + "         skuQtyForRate,discountGivenBy,discountReason,freeWithItemCode,freeQty, landedSkuValue, barcode,oh_Ac_IGST,oh_Amt_IGST,oh_at_IGST, discountPerUnit,wholeSaleRate,wholeSaleRateActual)"
                            + "         values (:docId, :v_sNo, :itemCode, :hsnCode, :saleUnitCode, :quantity,"
                            + "		:oh_at_Discount, :oh_at_Discount2, :oh_Amt_Discount, :oh_Amt_Discount2, :oh_Amt_Net, :oh_Amt_CGST, :oh_at_CGST, :oh_Amt_SGST, :oh_at_SGST, "
                            + "		:oh_at_UGST, :oh_Amt_UGST, :rate, :oh_Amt_Taxable, :itemGSTNature, :mrp, :isReturn, :skuQuantity, :oh_Ac_Sales, "
                            + "         :oh_Ac_CGST, :oh_Ac_UGST, :oh_Ac_SGST, :oh_Amt_Gross, :batchNo, :manufacturingDate, :expiryDate, :ibCode, "
                            + "         :skuQtyForRate,:discountGivenBy,:discountReason,:freeWithItemCode,:freeQty, :landedSkuValue, :barcode,:oh_Ac_IGST,:oh_Amt_IGST,:oh_at_IGST, :discountPerUnit,:wholeSaleRate,:wholeSaleRateActual)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSno()).toString());
                    psTemp.setString("itemCode", medicineMdl.getSharedKeyFieldCode());
                    psTemp.setString("hsnCode", DmShared.foriegnKey(medicineMdl.getHsnCode()));
                    psTemp.setString("itemGSTNature", DmShared.xNull(medicineMdl.getItemGstNature()));
                    psTemp.setString("saleUnitCode", DmShared.foriegnKey(medicineMdl.getPurchaseUnit()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQuantityInPurchaseUnit()));
                    psTemp.setString("skuQuantity", DmServer.vNull(detail_Mdl.getTotalQuantitySKU()));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(medicineMdl.getPurchaseUnitConversionRate()));
                    psTemp.setString("freeWithItemCode", DmShared.foriegnKey(detail_Mdl.getFreeWithItemCode()));
                    psTemp.setString("oh_Ac_SGST", DmShared.foriegnKey(detail_Mdl.getSgstAcCode()));
                    psTemp.setString("oh_Ac_UGST", DmShared.foriegnKey(detail_Mdl.getUgstAcCode()));
                    psTemp.setString("oh_Ac_CGST", DmShared.foriegnKey(detail_Mdl.getCgstAcCode()));
                    psTemp.setString("oh_Ac_IGST", DmShared.foriegnKey(detail_Mdl.getIgstAcCode()));
                    psTemp.setString("oh_Ac_Sales", DmShared.foriegnKey(detail_Mdl.getSaleAcCode()));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    psTemp.setString("oh_at_Discount", DmServer.vNull(medicineMdl.getDiscountPer(), "0.00"));
                    psTemp.setString("oh_at_Discount2", DmServer.vNull(detail_Mdl.getDiscountPer2(), "0.00"));
                    psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                    psTemp.setString("oh_Amt_Discount2", DmServer.vNull(detail_Mdl.getDiscount2(), "0.00"));
                    psTemp.setString("discountPerUnit", DmServer.vNull(detail_Mdl.getDiscountPerUnit()));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getNetAmt(), "0.00"));
                    psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgstAmt(), "0.00"));
                    psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgstAmt(), "0.00"));
                    psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgstAmt(), "0.00"));
                    psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));

                    psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getIgstAmt(), "0.00"));
                    psTemp.setString("oh_at_IGST", DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));

                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                    psTemp.setString("isReturn", detail_Mdl.isReturnSale() ? "1" : "0");
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setString("discountGivenBy", DmShared.foriegnKey(detail_Mdl.getDiscountGivenByTag()));
                    psTemp.setString("discountReason", DmShared.xNull(detail_Mdl.getDiscountReasonName()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getMfgDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpDate()));
                    psTemp.setString("landedSkuValue", DmServer.vNull(detail_Mdl.getLandedSkuValue(), "0.00"));
                    psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                    psTemp.setString("wholeSaleRate", DmShared.xNull(detail_Mdl.getWholeSaleRate()));
                    psTemp.setString("wholeSaleRateActual", DmShared.xNull(detail_Mdl.getWholeSaleRateActual()));

                    psTemp.executeUpdate();
                    psTemp.close();

                    //<editor-fold defaultstate="collapsed" desc="Stock Saving">
                    /*------------------------------------------------------------------------------------------------------------------------*/
                    if (!isEstimate) {
                        Stock_Mdl stock_Mdl = new Stock_Mdl();
                        stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        stock_Mdl.setSharedKeyFieldName(detail_Mdl.getvSno());
                        stock_Mdl.setSite_Code(DmShared.xNull(siteCode));
                        stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                        stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
                        stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                        stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdSaleBill().getPrefix()));
                        stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                        stock_Mdl.setDepartmentCode(DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                        stock_Mdl.setStockContraSubCode(DmShared.foriegnKey(mrTemp.getControlAccount())); //Pharmacy Control A/c
                        stock_Mdl.setRemark("Patient: " + DmShared.xNull(mrTemp.getPatientName()));

                        stock_Mdl.setItemCode(DmShared.xNull(medicineMdl.getSharedKeyFieldCode()));
                        stock_Mdl.setIbCode(DmShared.xNull(detail_Mdl.getIbCode()));
                        stock_Mdl.setIsuQty(DmServer.vNull(DmShared.vNull(detail_Mdl.getTotalQuantitySKU()).doubleValue()));
                        stock_Mdl.setSkuSaleValue(DmServer.vNull(DmShared.vNull(detail_Mdl.getAmount()).doubleValue(), "0.00"));
                        stock_Mdl.setLandedSkuSaleValue(DmServer.vNull(DmShared.vNull(detail_Mdl.getLandedSkuValue()).doubleValue(), "0.00"));

                        Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.OTHER, cnTemp);

                        //<editor-fold defaultstate="collapsed" desc="Unhold item in ItemDepartmentInfo">
                        if (!detail_Mdl.isReturnSale()) {
                            sqlQuery = "UPDATE itemDepartmentInfo SET isHoldRequirement = 0 "
                                    + " WHERE itemCode = " + DmShared.xNull(detail_Mdl.getSharedKeyFieldCode())
                                    + " AND departmentCode = " + DmShared.xNull(mrTemp.getDepartmentCode())
                                    + " AND site_Code = " + DmShared.xNull(DmServer.getSsnSiteCode(this))
                                    + " AND ISNULL(isHoldRequirement,0) <> 0 ";
                            DmServer.executeSQL(sqlQuery, cnTemp);
                        }
                        //</editor-fold>
                    }

                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Save in GST Trans">
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
                    gstTrans_Mdl.setConsigneeType(mrTemp.getPartyType());
                    gstTrans_Mdl.setPlaceOfSupply(mrTemp.getPlaceOfSupply());
                    gstTrans_Mdl.setInvoiceCGST(DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceSGST(DmServer.vNull(mrTemp.getSgstAmt(), "0.00"));
                    gstTrans_Mdl.setInvoiceIGST(DmServer.vNull(mrTemp.getIgstAmt(), "0.00"));
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
                    gstTrans_Mdl.setQty(DmServer.vNull(DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() + DmShared.vNull(detail_Mdl.getFreeQuantityInPurchaseUnit()).doubleValue()));
                    gstTrans_Mdl.setRecID(mrTemp.getSharedKeyFieldName());
                    gstTrans_Mdl.setRecIdPrefix(mrTemp.getRecIdSaleBill().getPrefix());
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
                    gstTrans_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
                    gstTrans_Mdl.setIsTaxInclusive("0");
                    gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()));
                    gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
                    gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                    gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                    gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                    gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.OUTPUT.toInt()));
                    gstTrans_Mdl.setGstin(DmShared.xNull(mrTemp.getPartyGstIn()));
                    gstTrans_Mdl.setGstinDate(DmShared.xNull(mrTemp.getPartyGstinDate()));
                    gstTrans_Mdl.setPartyType(DmShared.xNull(mrTemp.getPartyType()));

                    Gst_Server.saveGstTrans(gstTrans_Mdl, false, cnTemp, this);
                }

            }

            //<editor-fold defaultstate="collapsed" desc="Insert into patientReceipt">
            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CASH)) {

                if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.PAYMENT) {

                    if (DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()).trim().equalsIgnoreCase("")) {
                        mrTemp.setRecIdRecipt(DmServer.getRecId("patientReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdRecipt(), DmShared.dNull(mrTemp.getVoucherDate()), DmShared.xNull(mrTemp.getCardPayment().getReceiptTypeCode()), voucherPrefix, cnTemp, this, true));
                    }

                    if (!DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()).trim().equalsIgnoreCase("")) {

                        double reciptAmt = 0;
                        for (GrdCardPayment_Mdl cardPayment_Mdl : mrTemp.getCardPayment().getGrdCardPaymentList()) {
                            if (DmShared.vNull(cardPayment_Mdl.getAmount()).doubleValue() != 0) {
                                reciptAmt += DmShared.vNull(cardPayment_Mdl.getAmount());
                            }
                        }

                        if (blnAdd) {
                            /*====================================< Insert into patientReceipt >====================================*/
                            sqlQuery = "Insert into patientReceipt (docId, v_Type, v_No, recId, recIdPrefix, v_Prefix, v_Date, u_EntDt, preparedBy, site_Code, amount,sbDocid) "
                                    + " values (:docId, :v_Type, :v_No, :recId, :recIdPrefix, :v_Prefix, :v_Date, :uEntDt, :userName, :site_Code, :amount,:sbDocid)";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                            psTemp.setString("sbDocid", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getCardPayment().getReceiptTypeCode()));
                            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdRecipt().getPrefix()));
                            psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()));
                            psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdRecipt().getCounter()).toString());
                            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                            psTemp.setString("v_Prefix", voucherPrefix);
                            psTemp.setString("site_Code", siteCode);
                            psTemp.setString("userName", userName);
                            psTemp.setString("amount", DmServer.vNull(reciptAmt, "0.00"));

                        } else {
                            sqlQuery = "update patientReceipt set "
                                    + " u_Name = :userName, "
                                    + " u_EntDt = :uEntDt, "
                                    + " amount = :amount "
                                    + " where docId = :docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                            psTemp.setString("userName", userName);
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.setString("amount", DmServer.vNull(reciptAmt, "0.00"));
                        }

                        psTemp.executeUpdate();
                        psTemp.close();

                    }

                    /*====================================< Delete CardLedger >====================================*/
                    sqlQuery = "Delete From cardLedger Where docId = :docId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.executeUpdate();
                    psTemp.close();
                    /*====================================< Save CardLedger >====================================*/
                    CardPaymentPopup_Server.save(true, mrTemp.getCardPayment(), "", mrTemp.getSharedKeyFieldCode(), mrTemp.getSharedKeyFieldName(), mrTemp.getVoucherTypeCode(), siteCode, mrTemp.getVoucherDate(), DmServer.getSsnUserName(this), mrTemp.getSharedKeyFieldCode(), cnTemp);

                }

            }

            //</editor-fold>
            /*====================================< Data Fetch For Account Posting >====================================*/
            List<ItemAccountPosting_Mdl> itemList = new ArrayList<>();

            String roundOffSubCode = DmServer.getCodeViaManualCode("RoInLe1", "manualCode", "SubGroup", "SubCode", cnTemp);

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CREDIT)) {
                sqlQuery = ""
                        + "	select tmp.*, :dueDate as dueDate from ( "
                        + "SELECT sg.name AS acName, \n"
                        + "       sb.partySubCode AS subCode,\n"
                        + "       sb.oh_Amt_Net AS amtDr, \n"
                        + "       0 AS amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sb.partySubCode  \n"
                        + "WHERE sb.docId = :docId \n"
                        + "union all\n"
                        + "\n"
                        + "SELECT  max(sg.name) as acName, sbd.oh_Ac_Sales as subCode ,\n"
                        + "0 as amtDr, sum(sbd.oh_Amt_Taxable) as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_Sales \n"
                        + "WHERE sb.docId = :docId  and sbd.isFooter = 0\n"
                        + "group by sbd.oh_Ac_Sales\n"
                        + "\n"
                        + "Union all\n "
                        + "\n"
                        + "SELECT  max(sg.name) as acName, sbd.oh_Ac_CGST as subCode ,\n"
                        + "0 as amtDr, sum(sbd.oh_Amt_SGST) as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_CGST \n"
                        + "WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                        + "group by sbd.oh_Ac_CGST\n"
                        + "\n"
                        + "Union all \n"
                        + "\n"
                        + "SELECT  max(sg.name) as acName, sbd.oh_Ac_SGST as subCode ,\n"
                        + "0 as amtDr, sum(sbd.oh_Amt_SGST) as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_SGST \n"
                        + "WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                        + "group by sbd.oh_Ac_SGST\n"
                        + "\n"
                        + ""
                        + "Union all  \n"
                        + "\n"
                        + "		SELECT  max(sg.name) as acName, sbd.oh_Ac_IGST as subCode ,\n"
                        + "		0 as amtDr, sum(sbd.oh_Amt_IGST)  as amtCr  \n"
                        + "		FROM saleBill sb \n"
                        + "		LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "		LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_IGST \n"
                        + "		WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                        + "		group by sbd.oh_Ac_IGST"
                        + "         "
                        + "    Union all\n"
                        + "\n"
                        + "   SELECT  max(sg.name) as acName, sbd.oh_Ac_Sales as subCode ,\n"
                        + "      case when sum(sbd.oh_Amt_Taxable)<0 then abs(sum(sbd.oh_Amt_Taxable)) else      0  end as amtDr, \n"
                        + "	   case when sum(sbd.oh_Amt_Taxable)>0 then abs(sum(sbd.oh_Amt_Taxable)) else     0  end   as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_Sales \n"
                        + "WHERE sb.docId = :docId  and sbd.isFooter = 1\n"
                        + "group by sbd.oh_Ac_Sales    "
                        + " \n"
                        + ""
                        + "  Union all\n"
                        + "\n"
                        + "\n"
                        + " select max(sg.name) as acName,sg.subCode ,0 as amtDr,abs(sum(stk.grossPurchaseRate)) as amtCr from stock stk \n"
                        + "left join itemMast im on im.code=stk.itemCode\n"
                        + "left join subGroup sg on sg.subCode=im.subCode\n"
                        + "where stk.docid=:docId \n"
                        + "group by sg.subCode\n"
                        + "\n"
                        + "union all\n"
                        + "\n"
                        + "select max(sg.name) as acName,sg.subCode ,abs(sum(stk.grossPurchaseRate)) as amtDr,0 as amtCr  from stock stk \n"
                        + "left join itemMast im on im.code=stk.itemCode\n"
                        + "left join subGroup sg on sg.subCode=im.cogsAc\n"
                        + "where stk.docid=:docId \n"
                        + "group by sg.subCode"
                        + ""
                        + "    UNION ALL\n"
                        + "    SELECT 'Round Off A/c' as acName, " + roundOffSubCode + " as subCode,\n"
                        + "        CASE WHEN sb.oh_Amt_RoundOff < 0 THEN Abs(sb.oh_Amt_RoundOff) ELSE 0 END AS amtDr,\n"
                        + "        CASE WHEN sb.oh_Amt_RoundOff > 0 THEN Abs(sb.oh_Amt_RoundOff) ELSE 0 END AS amtCr\n"
                        + "    FROM saleBill sb \n"
                        + "    WHERE sb.docId = :docId"
                        + ") as tmp\n"
                        + "\n"
                        + "  where tmp.subCode is not null  ";
            } else if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CASH)) {
                sqlQuery = ""
                        + "	select tmp.*, :dueDate as dueDate from ( "
                        + "\n"
                        + "SELECT sg.name AS acName, \n"
                        + "       partySubCode AS subCode,\n"
                        + "       sb.oh_Amt_Net AS amtDr, \n"
                        + "       0 AS amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sb.partySubCode  \n"
                        + "WHERE sb.docId = :docId \n"
                        + "union all\n"
                        + "SELECT  max(sg.name) as acName, sbd.oh_Ac_CGST as subCode ,\n"
                        + "0 as amtDr, sum(sbd.oh_Amt_SGST) as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_CGST \n"
                        + "WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                        + "group by sbd.oh_Ac_CGST\n"
                        + "\n"
                        + "Union all \n"
                        + "\n"
                        + "SELECT  max(sg.name) as acName, sbd.oh_Ac_SGST as subCode ,\n"
                        + "0 as amtDr, sum(sbd.oh_Amt_SGST) as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_SGST \n"
                        + "WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                        + "group by sbd.oh_Ac_SGST\n"
                        + "\n"
                        + ""
                        + "Union all  \n"
                        + "\n"
                        + "		SELECT  max(sg.name) as acName, sbd.oh_Ac_IGST as subCode ,\n"
                        + "		0 as amtDr, sum(sbd.oh_Amt_IGST)  as amtCr  \n"
                        + "		FROM saleBill sb \n"
                        + "		LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "		LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_IGST \n"
                        + "		WHERE sb.docId = :docId and sbd.isFooter = 0\n"
                        + "		group by sbd.oh_Ac_IGST"
                        + "         "
                        + "    Union all\n"
                        + "\n"
                        + "SELECT  max(sg.name) as acName, sbd.oh_Ac_Sales as subCode ,\n"
                        + "0 as amtDr, sum(sbd.oh_Amt_Taxable) as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_Sales \n"
                        + "WHERE sb.docId = :docId  and sbd.isFooter = 0\n"
                        + "group by sbd.oh_Ac_Sales\n"
                        + "\n"
                        + ""
                        + ""
                        + "Union all\n "
                        + ""
                        + ""
                        + "\n"
                        + "\n"
                        + "   SELECT  max(sg.name) as acName, sbd.oh_Ac_Sales as subCode ,\n"
                        + "      case when sum(sbd.oh_Amt_Taxable)<0 then abs(sum(sbd.oh_Amt_Taxable)) else      0  end as amtDr, \n"
                        + "	   case when sum(sbd.oh_Amt_Taxable)>0 then abs(sum(sbd.oh_Amt_Taxable)) else     0  end   as amtCr  \n"
                        + "FROM saleBill sb \n"
                        + "LEFT JOIN saleBillDetail sbd ON sb.docId = sbd.docId \n"
                        + "LEFT JOIN subGroup sg ON sg.subCode = sbd.oh_Ac_Sales \n"
                        + "WHERE sb.docId = :docId  and sbd.isFooter = 1\n"
                        + "group by sbd.oh_Ac_Sales    "
                        + " \n"
                        + ""
                        + ""
                        + ""
                        + "Union all\n"
                        + "\n"
                        + "\n"
                        + " select max(sg.name) as acName,sg.subCode ,0 as amtDr,abs(sum(stk.grossPurchaseRate)) as amtCr from stock stk \n"
                        + "left join itemMast im on im.code=stk.itemCode\n"
                        + "left join subGroup sg on sg.subCode=im.subCode\n"
                        + "where stk.docid=:docId \n"
                        + "group by sg.subCode\n"
                        + "\n"
                        + "union all\n"
                        + "\n"
                        + "select max(sg.name) as acName,sg.subCode ,abs(sum(stk.grossPurchaseRate)) as amtDr,0 as amtCr  from stock stk \n"
                        + "left join itemMast im on im.code=stk.itemCode\n"
                        + "left join subGroup sg on sg.subCode=im.cogsAc\n"
                        + "where stk.docid=:docId \n"
                        + "group by sg.subCode"
                        + ""
                        + "          "
                        + "    union all \n"
                        + "\n"
                        + "select max(sg.name) as acName,sg.subCode ,abs(sum(cl.amtDr)) as amtDr,0 as amtCr  \n"
                        + "from patientReceipt prc \n"
                        + "left join saleBill sb on sb.docId=prc.sbDocId\n"
                        + "left join cardLedger cl on cl.docid=prc.docid\n"
                        + "left join subGroup sg on sg.subCode=cl.subcode\n"
                        + "where sb.docId=:docId\n"
                        + "group by sg.subCode\n"
                        + "\n"
                        + "union all \n"
                        + "\n"
                        + "select max(sg.name) as acName,sg.subCode ,0 as amtDr,abs(sum(cl.amtDr)) as amtCr  \n"
                        + "from patientReceipt prc \n"
                        + "left join saleBill sb on sb.docId=prc.sbDocId\n"
                        + "left join cardLedger cl on cl.docid=prc.docid\n"
                        + "left join subGroup sg on sg.subCode=sb.partySubCode\n"
                        + "where sb.docId=:docId\n"
                        + "group by sg.subCode        "
                        + "           "
                        + " UNION ALL\n"
                        + "\n"
                        + "    SELECT 'Round Off A/c' as acName, " + roundOffSubCode + " as subCode,\n"
                        + "        CASE WHEN sb.oh_Amt_RoundOff < 0 THEN Abs(sb.oh_Amt_RoundOff) ELSE 0 END AS amtDr,\n"
                        + "        CASE WHEN sb.oh_Amt_RoundOff > 0 THEN Abs(sb.oh_Amt_RoundOff) ELSE 0 END AS amtCr\n"
                        + "    FROM saleBill sb \n"
                        + "    WHERE sb.docId = :docId \n "
                        + ") as tmp\n"
                        + "\n"
                        + "  where tmp.subCode is not null  ";

            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setDate("dueDate", DmServer.convertDate(mrTemp.getDueDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                ItemAccountPosting_Mdl listNode = new ItemAccountPosting_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                listNode.setSharedKeyFieldName(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                listNode.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdSaleBill().getPrefix()));
                listNode.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                listNode.setV_Prefix(DmShared.xNull(voucherPrefix));
                listNode.setV_Date(DmShared.xNull(mrTemp.getVoucherDate()));
                listNode.setV_No(DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());

                listNode.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
//                listNode.setContraText(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setAmtdr(DmShared.xNull(rsTemp.getString("amtDr")));
                listNode.setAmtcr(DmShared.xNull(rsTemp.getString("amtCr")));
                listNode.setDueDate(DmShared.dNull(rsTemp.getString("dueDate")));

                itemList.add(listNode);

            }

            saveAccountPosting(itemList, mrTemp.getSharedKeyFieldCode(), cnTemp);

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CASH)) {
                String sqlQuery1 = "insert into ledgerAdj \n"
                        + "select cl.docId as vr_docId ,lg.v_SNo as vr_V_SNo,\n"
                        + "" + DmShared.xNull(mrTemp.getPartySubCode()) + " as vr_SubCode,lg.v_Date as vr_V_Date,cl.docId as adj_DocId,\n"
                        + "lg1.v_SNo as adj_V_SNo ," + DmShared.xNull(mrTemp.getPartySubCode()) + " as adj_SubCode,lg.v_Date as adj_V_Date,\n"
                        + "isNull(-lg.amtDr,0.00) as adjustment, NULL as finPayDocId \n"
                        + "from cardLedger cl \n"
                        + "left join ledger lg on lg.docId = cl.docId and cl.subCode = lg.subcode\n"
                        + "left join ledger lg1 on lg1.docId = cl.docId and \n"
                        + "lg1.subcode = " + DmShared.xNull(mrTemp.getPartySubCode()) + " and isNull(lg1.amtDr,0.00) >0\n"
                        + "where cl.docId = " + mrTemp.getSharedKeyFieldCode() + "";
                DmServer.executeSQL(sqlQuery1, cnTemp);
            }


            /*====================================< End >====================================*/
            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                saveLog(mrTemp.getSharedKeyFieldCode(), mrTemp.getModifyReasonMdl(), mrTemp, blnAdd ? DmEnum.DmTransactionMode.NEW : DmEnum.DmTransactionMode.EDIT, null);
            }
            /*====================================< save Records in WholeSale Gate Pass >====================================*/
            saveGatePass(mrTemp, blnAdd, voucherPrefix, cnTemp);

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

    private boolean isReturnedFromSaleBillReturn(String docId, boolean isMoveRec, Connection cnTemp) {
        boolean returnValue = false;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        String sqlQuery = "";

        try {
            sqlQuery = "SELECT COUNT(*) as cnt "
                    + " FROM saleBill sb  "
                    + " LEFT JOIN saleBillDetail sbd ON sbd.docId = sb.docId "
                    + " WHERE sbd.sbdocId = :docId"
                    + " AND sb.isCancelled = 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    returnValue = true;
                    if (!isMoveRec) {
                        throw new DmException(0, "Return has been done, can't update record");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            if (!isMoveRec) {
                throw new DmException(0, e.getMessage());
            } else {
                if (!returnValue) {
                    throw new DmException(0, e.getMessage());
                }
            }

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode, ModifyReason_Mdl reason_Mdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            /*==============================<< Check if document is in use >>====================================*/
            checkIsDocumentInUseBeforeDiscard(keyCode, false, cnTemp);
            deleteRecord(keyCode, cnTemp);
            if (reason_Mdl != null) {
                saveLog(keyCode, reason_Mdl, null, DmEnum.DmTransactionMode.DELETE, null);
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

    private String deleteRecord(String keyCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*==============================<< Delete GstTrans >>====================================*/
            Gst_Server.deleteGstTrans(keyCode, cnTemp);
            CardPaymentPopup_Server.cancel(keyCode, cnTemp);

            Stock_Server.deleteStock(keyCode, cnTemp);

            Gst_Server.deleteGstTrans(keyCode, cnTemp);

            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(keyCode, cnTemp);

            sqlQuery = "delete from ledgerAdj Where vr_DocId= :vr_DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("vr_DocId", keyCode);
            psTemp.executeUpdate();

            sqlQuery = "update patientReceipt set isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            sqlQuery = "update patientServiceDetail set isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.executeUpdate();

            sqlQuery = "UPDATE saleBill "
                    + " SET saleBill.u_EntDt_LatestLine = :uEntDt "
                    + " FROM ( "
                    + " 	SELECT sb.estimateDocId "
                    + " 	FROM saleBill sb "
                    + " 	WHERE sb.docId = :docId "
                    + " 	AND sb.estimateDocId IS NOT NULL "
                    + " 	) vSb "
                    + " WHERE vSb.estimateDocId = saleBill.docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("docId", keyCode);
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "update saleBill set cancelledBy = :cancelledBy,cancelledDt = :cancelledDt, isCancelled = 1 Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", keyCode);
            psTemp.setString("cancelledBy", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("cancelledDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
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
        return keyCode;
    }

    private String checkIsDocumentInUseBeforeDiscard(String keyCode, boolean isEInvoiceCancel, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        SaleBillWholeSale_Mdl mrTemp;
        String regDocid = "";

        try {
            if (!isEInvoiceCancel) {
                DmServer.chkIfDocumentUsed("docId", "eGst", keyCode, cnTemp);
            }
            /*================< Check whether patient discarded/ approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select isCancelled, site_Code, regDocId, v_Prefix from saleBill where docId = :docId ";
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
                regDocid = DmShared.xNull(rsTemp.getString("regDocId"));
            }
            rsTemp.close();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Approval Check">
            if (DmShared.iNull(regDocid).longValue() > 0) {
                mrTemp = new SaleBillWholeSale_Mdl();
                mrTemp.setSharedKeyFieldCode(keyCode);
                CardPaymentPopup_Mdl cardPaymentPopup_Mdl = CardPaymentPopup_Server.getCardListViaDocId(DmShared.xNull(keyCode), cnTemp);
                mrTemp.getCardPayment().getGrdCardPaymentList().addAll(cardPaymentPopup_Mdl.getGrdCardPaymentList());
                mrTemp.setCardPayment(cardPaymentPopup_Mdl);
                if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION
                        || mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {

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
                            throw new DmException(0, "Patient Bill is generated, can't update this entry!");
                        }
                    }
                    rsTemp.close();
                    psTemp.close();

                    STServer.checkBillApproval(regDocid, cnTemp);
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Check whether posted (via EOD) or not">
            STServer.checkEOD("saleBillDetail", "docId", keyCode, cnTemp);
            //</editor-fold>

            isReturnedFromSaleBillReturn(keyCode, false, cnTemp);

            ///*==============================<< ************** >>====================================*/
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
        return keyCode;
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
                    + "    puvt.short_Name, mb.v_no, mb.recId, "
                    + "    sdm.name AS departmentName, sg.name as patientName, "
                    + "    sgcon.name AS consultantName, "
                    + "    CASE WHEN mb.isCancelled = 1 THEN 'Discarded' ELSE '' END as status, "
                    + "    CAST(mb.preparedDate AS DATE) as preparedDate,"
                    + "    CASE  WHEN wsgps.approvedBy IS NULL THEN ''  ELSE wsgps.recId  END AS gatePassRecId ,\n "
                    + "    CAST(REPLACE(LEFT(CONVERT(varchar, mb.preparedDate, 8),5),':','.') AS NUMERIC(18,2)) as preparedTime "
                    + "    FROM saleBill mb "
                    + "    LEFT JOIN voucher_type AS pbvt ON mb.v_Type = pbvt.v_Type "
                    + "    LEFT JOIN wholeSaleGatePass wsgps ON mb.docId = wsgps.sbDocId "
                    + "    LEFT JOIN patientregistration pr ON mb.regDocid = pr.regDocid "
                    + "    LEFT JOIN voucher_type AS prvt ON pr.v_Type = prvt.v_Type "
                    + "    LEFT JOIN patientuhid pu ON pr.UHID = pu.docId "
                    + "    LEFT JOIN ledgerMast lm ON lm.code = mb.lmCode "
                    + "    LEFT JOIN voucher_type AS puvt ON pu.v_Type = puvt.v_Type "
                    + "    LEFT JOIN subgroup sdm ON mb.departmentCode = sdm.subcode "
                    + "    LEFT JOIN subgroup sg ON sg.subCode= mb.partySubCode  "
                    + "    LEFT JOIN subGroup AS sgcon ON mb.consultantCode  = sgcon.subCode "
                    + " WHERE mb.site_Code =:siteCode   "
                    + " AND pbvt.category IN ('PHWBL','PHWBC')"
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
                listNode.setPartyName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setStatus(DmShared.xNull(rsTemp.getString("status")));
                listNode.setPreparedDate(DmShared.dNull(rsTemp.getDate("preparedDate")));
                listNode.setPreparedTime(DmShared.xNull(rsTemp.getString("preparedTime")));
                listNode.setGatePassNo(DmShared.xNull(rsTemp.getString("gatePassRecId")));
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
    public DmResultPageing<List<TxtHVType_Mdl>> getTxtHVType(String voucherCategory, String voucherCategory1, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVType_Mdl> lstTemp = new ArrayList<>();
        TxtHVType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT VT.V_Type, VT.Description, vt.documentNatureCode, vt.documentTypeCode,VT.category "
                    + "FROM Voucher_Type VT  "
                    + "WHERE VT.Category IN (" + getVoucherCategory(voucherCategory) + ", " + getVoucherCategory(voucherCategory1) + ") AND VT.isActive =1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                listNode.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                listNode.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory) {
        DmResult<Default_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHReceiptType_VList_Mdl> reciptList = new ArrayList<TxtHReceiptType_VList_Mdl>();
        Default_Mdl mrTemp = new Default_Mdl();
        Layout_Mdl layout;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setCardPayment(new CardPaymentPopup_Mdl());

            mrTemp.setPartyType(DmEnum.DmPartyType.Unregistered.getId());

            sqlQuery = "SELECT  vt.v_Type, vt.description, vt.attachPaymentMode, vt.isSystemDefine"
                    + "     FROM voucher_Type vt "
                    + "     WHERE vt.category = '" + pvc.PHARMACY_WHOLE_SALE_BILL_RECEIPT + "' and vt.isActive = 1 ";
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
            mrTemp.getCardPayment().getTxtHReceiptTypeVList().addAll(reciptList);

            sqlQuery = " select TOP 1 vt.v_Type, vt.description, vt.documentNatureCode,vt.category, vt.documentTypeCode "
                    + " FROM voucher_Type vt"
                    + " WHERE vt.Short_Name = " + getVoucherCategory(voucherCategory) + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                TxtHVType_Mdl vTypeMdl = new TxtHVType_Mdl();
                vTypeMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                vTypeMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                vTypeMdl.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                vTypeMdl.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                vTypeMdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
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

            sqlQuery = "SELECT  "
                    + "    env.expiryAlertBeforeDays "
                    + "  , env.medicineSaleOn "
                    + "  , env.alphCase_Patient "
                    + "  , env.alphaCase_Address "
                    + "  , env.defaultPharmacyServiceCode "
                    + "  , env.pharmacyAllowReturnWithoutSale "
                    + "  , env.isIncludeTaxInCost "
                    + "  , env.pharmacyBarcodeActive \n"
                    + "  , env.pharmacyBarcodeActive \n"
                    + "  , env.pharmacyCentralStore as pharmacyCentralStoreCode,sg.name AS pharmacyCentralStoreName, env.ewayLimit "
                    + "  FROM enviro as env "
                    + " left join subGroup sg on env.pharmacyCentralStore=sg.subCode \n"
                    + " Where env.site_Code = :site_Code ";
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
                mrTemp.setPharmacyBarcodeActive(DmShared.xNull(rsTemp.getString("pharmacyBarcodeActive")));
                if (DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).trim().equals("")) {
                    throw new DmException(0, "Please set 'Include Tax In Cost' in environment settings.");
                } else {
                    mrTemp.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                }
                mrTemp.setPharmacyCentralStoreCode(DmShared.xNull(rsTemp.getString("pharmacyCentralStoreCode")));
                mrTemp.setPharmacyCentralStoreName(DmShared.xNull(rsTemp.getString("pharmacyCentralStoreName")));
                mrTemp.setEwayBillLimit(DmServer.vNull(rsTemp.getString("ewayLimit"), "0.00"));
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

            ///*==========================<<  Get GST Location  >>=================================*/
            mrTemp.setGstLocationMap(STServer.getSystemDefinedGstLocationMap(cnTemp));

            mrTemp.setSiteCode(DmServer.getSsnSiteCode(this));
            mrTemp.setSiteShortName(DmServer.getSsnSiteSName(this));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean vendorAllow, boolean walkInAllow) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
//            if (ipdAllow) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode(pvc.IPD);
//                listNode.setSharedKeyFieldName(pvc.IPD);
//                lstTemp.add(listNode);
//            }
//
//            if (opdAllow) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode(pvc.OPD);
//                listNode.setSharedKeyFieldName(pvc.OPD);
//                lstTemp.add(listNode);
//            }
//            if (dayCareAllow) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
//                listNode.setSharedKeyFieldName("Day Care");
//                lstTemp.add(listNode);
//            }
            if (vendorAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("Debtor");
                listNode.setSharedKeyFieldName("Debtor");
                lstTemp.add(listNode);
            }
//            if (ledgerAllow) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode("Ledger");
//                listNode.setSharedKeyFieldName("Ledger");
//                lstTemp.add(listNode);
//            }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOperation(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select psm.code, psm.name "
                    + "	from patientServiceMast psm "
                    + "	LEFT JOIN ( "
                    + "		SELECT omd.serviceCode "
                    + "		FROM operationMediDetail omd "
                    + "		GROUP BY omd.serviceCode "
                    + "		) vomd ON vomd.serviceCode = psm.code "
                    + "	where psm.isActive = 1 "
                    + " and serviceNature = :serviceNature"
                    + " and vomd.serviceCode is not null ");
            sqlWithParam.setString("serviceNature", DmShared.iNull(ServiceNature.OPERATION.getId()).toString());
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
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate, String location) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = getITS(itemCode, rate, itsBasedOn, billDate, location, cnTemp);
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
// yha change karna hai 

    private DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, double rate, String itsBasedOn, String billDate, String location, Connection cnTemp) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();

        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
//            sqlQuery = "SELECT ISNULL(sm.isUt,0) AS isUt FROM stateMast sm WHERE sm.numericCode = :numericCode";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("numericCode", DmServer.getSsnSiteStateNumericCode(this));
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.xNull(rsTemp.getString("isUT")).equalsIgnoreCase("1")) {
////                    location = DmShared.iNull(DmLocation.UnionTerritory.getId()).toString();
//                    location = DmShared.iNull(DmLocation.IntraState.getId()).toString();
//                } else {
//                    location = DmShared.iNull(DmLocation.IntraState.getId()).toString();
//                }
//            }
//            rsTemp.close();
//            psTemp.close();

            dmrTemp = STServer.getITS(itemCode, rate, itsBasedOn, ITSType.SALES, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
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
//Client

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    @Override
    public DmResult<String> isItemEverReturned(String itemCode, String ibCode, String prvQty, String prvFreeQty) {
        Connection cnTemp = null;
        DmResult<String> dmrTemp = new DmResult();
        String returnValue = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            returnValue = checkItemSale(itemCode, ibCode, prvQty, prvFreeQty, cnTemp);

            dmrTemp.setStatus(true);
            dmrTemp.setData(returnValue);
            cnTemp.close();
        } catch (Exception e) {
            try {
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
                e.printStackTrace();
            }
        }

        return dmrTemp;
    }

    private String checkItemSale(String itemCode, String ibCode, String prvQty, String prvFreeQty, Connection cnTemp) throws SQLException {
        DmPreparedStatement psTemp = null;
        String sqlQuery, returnValue = "";
        ResultSet rsTemp = null;

        sqlQuery = " Select sbd.ibCode ";
        if (DmShared.vNull(prvQty).doubleValue() == 0) {
            sqlQuery += " , SUM( CASE WHEN vt.category = " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + " THEN 0 - ISNULL(sbd.quantity,0) ELSE sbd.quantity END) as quantity ";
        } else {
            sqlQuery += " , SUM( CASE WHEN vt.category = " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + " THEN 0 - ISNULL(sbd.quantity,0) ELSE sbd.quantity END)  + " + Math.abs(DmShared.vNull(prvQty).doubleValue()) + " as quantity ";
        }
        if (DmShared.vNull(prvFreeQty).doubleValue() == 0) {
            sqlQuery += " , SUM( CASE WHEN vt.category = " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + " THEN 0 - ISNULL(sbd.freeQty,0) ELSE sbd.freeQty END) as freeQty ";
        } else {
            sqlQuery += " , SUM( CASE WHEN vt.category = " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + " THEN 0 - ISNULL(sbd.freeQty,0) ELSE sbd.freeQty END)  + " + Math.abs(DmShared.vNull(prvFreeQty).doubleValue()) + " as freeQty ";
        }

        sqlQuery += " FROM SaleBill sb "
                + " INNER JOIN saleBillDetail sbd On sb.docid=sbd.docid "
                + " INNER JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
                + " WHERE sbd.itemCode = :itemCode "
                + " AND sbd.ibCode = :ibCode "
                + " AND sb.isCancelled = 0 ";

        sqlQuery += " AND vt.category IN (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ") "
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

    private boolean isEstimateBillCreated(String docId, boolean isMoveRec, Connection cnTemp) {
        boolean returnValue = false;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        String sqlQuery = "";

        try {
            sqlQuery = "select COUNT(*) AS cnt  "
                    + " from saleBill sb  "
                    + " left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                    + " where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ")"
                    + " and sb.isCancelled = 0  "
                    + " and sb.estimateDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", docId);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    returnValue = true;
                    if (!isMoveRec) {
                        throw new DmException(0, "Bill is generated for this Estimate! You can not modify / delete this record.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            if (!isMoveRec) {
                throw new DmException(0, e.getMessage());
            } else {
                if (!returnValue) {
                    throw new DmException(0, e.getMessage());
                }
            }

        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return returnValue;
    }

    private CompareProperties_Mdl iniItemBatchProperties(SaleBillWholeSale_Mdl mrTempCurrent, SaleBillWholeSale_Mdl mrTempPrevious) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();

        lstItemBatch_MdlCurnt.clear();
        for (GrdMedicine_Mdl detail_MdlCurrent : mrTempCurrent.getGrdMediDetails()) {
            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0 && detail_MdlCurrent.isReturnSale()) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                batch_Mdl.setReceiveQty(DmServer.vNull(0 - DmShared.vNull(detail_MdlCurrent.getQuantitySKU())));
                batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
                lstItemBatch_MdlCurnt.add(batch_Mdl);
            }
        }

        lstItemBatch_MdlPrev.clear();
        if (mrTempPrevious != null) {
            for (GrdMedicine_Mdl detail_MdlPrev : mrTempPrevious.getGrdMediDetails()) {
                ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
                if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0 && detail_MdlPrev.isReturnSale()) {
                    batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                    batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                    batch_MdlPrev.setReceiveQty(DmServer.vNull(0 - DmShared.vNull(detail_MdlPrev.getQuantitySKU())));
                    batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
                    batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentCode()));
                    lstItemBatch_MdlPrev.add(batch_MdlPrev);
                }
            }
        }

        compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
        compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
        compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdSaleBill().getPrefix()));
        compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdSaleBill().getCounter().toString()));
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
    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date) {
        DmResult<String> dmrtemp = new DmResult<String>();
        String skuLandedRate = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            skuLandedRate = DmServer.vNull(STServer.getSkuLandedRate(itemCode, ibCode, departmentCode, DmServer.getSsnSiteCode(this), v_Date, cnTemp), "0.00");
            dmrtemp.setData(skuLandedRate);
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
    public DmResultPageing<List<TxtHVendor_Mdl>> getTxtHVendor(String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVendor_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVendor_Mdl> lstTemp = new ArrayList<>();
        TxtHVendor_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("select sg.name AS Name,sg.subCode AS code, c.stateCode, sm.name as stateName"
                    + "          ,sg.cityCode,ct.cityName ,sg.dueDays, sg.profitPercentage, sg.isDiscountAllow "
                    + "         from subGroup sg \n"
                    + " left join subGroupNature sgn on sgn.code = sg.nature \n"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode \n"
                    + " LEFT JOIN stateMast sm ON sm.code = c.stateCode \n"
                    + " WHERE sgn.manualCode = 'CT' AND  sg.IsAllowForSale = 1\n");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVendor_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setDueDays(DmShared.xNull(rsTemp.getString("dueDays")));
                listNode.setProfitPercentage(DmServer.vNull(rsTemp.getString("profitPercentage")));
                listNode.setAllowDiscount(DmShared.xNull(rsTemp.getString("isDiscountAllow")));
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
    public DmResult<PartyDetailPopup_Mdl> fillVendorDetails(String subCode, int recordTypeCode, boolean isTransporter) {
        DmResult<PartyDetailPopup_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PartyDetailPopup_Mdl mrTemp = new PartyDetailPopup_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp = PartyDetailPopup_Server.getPartyDetailPopup_Mdl(subCode, cnTemp);

            if (!isTransporter) {

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
                        location = DmShared.iNull(DmLocation.UnionTerritory.getId()).toString();
                    } else {
                        Sitelocation = DmShared.xNull(rsTemp.getString("stateCode"));
                    }

                }

                psTemp.close();
                rsTemp.close();

                if (DmShared.xNull(debtorlocation).equalsIgnoreCase(DmShared.xNull(Sitelocation))) {
                    mrTemp.setSharedPreparedIP(DmShared.iNull(DmLocation.IntraState.getId()).toString());
                    mrTemp.setSharedPreparedBy(DmShared.xNull(DmLocation.IntraState.getValue()));
                } else if (!DmShared.xNull(location).equalsIgnoreCase("")) {
                    mrTemp.setSharedPreparedIP(DmShared.iNull(DmLocation.UnionTerritory.getId()).toString());
                    mrTemp.setSharedPreparedBy(DmShared.xNull(DmLocation.UnionTerritory.getValue()));
                } else {
                    mrTemp.setSharedPreparedIP(DmShared.iNull(DmLocation.InterState.getId()).toString());
                    mrTemp.setSharedPreparedBy(DmShared.xNull(DmLocation.InterState.getValue()));
                }
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
                checkIsDocumentInUseBeforeDiscard(masterEGst_Mdl.geteGst_Mdl().getDocid(), true, cnTemp);
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

                if (!DmShared.xNull(rsTemp.getString("cancelledBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is discarded, can't process this action.");
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
                    finalizeGstDocumentProcess(gst_Mdl, actionCode, cnTemp);
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
            sqlQuery = "update "
                    + " saleBill set "
                    + " eWayBillDt=:eWayBillDt, "
                    + " eWayBillNo=:eWayBillNo, "
                    + " eInvIrnNo=:eInvIrnNo, "
                    + " eInvAckDt=:eInvAckDt, "
                    + " eInvAckNo=:eInvAckNo, "
                    + " eInvMongoLinkId=:eInvMongoLinkId, "
                    //                    + " deliveryTo=:deliveryTo, "
                    //                    + " deliveryCity=:deliveryCity, "
                    + " lrNo=:lrNo, "
                    + " lrDate=:lrDate, "
                    + " vehicleNo=:vehicleNo, "
                    + " transporterCode=:transporterCode, "
                    + " transportMode=:transportMode, "
                    + " vehicleTypeCode=:vehicleTypeCode, "
                    + " u_EntDt_LatestLine = :uEntDt \n"
                    + " where docId=:docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", eGst_Mdl.getDocid());
            psTemp.setTimestamp("eWayBillDt", Gst_Server.convertTimestamp(eGst_Mdl.getEway_BillDate()));
            psTemp.setString("eWayBillNo", DmShared.foriegnKey(eGst_Mdl.getEway_BillNo()));
            psTemp.setString("eInvIrnNo", DmShared.foriegnKey(eGst_Mdl.getEinv_IrnNo()));
            psTemp.setTimestamp("eInvAckDt", Gst_Server.convertTimestamp(eGst_Mdl.getEinv_ackDt()));
            psTemp.setString("eInvAckNo", DmShared.foriegnKey(eGst_Mdl.getEinv_ackNo()));
            psTemp.setString("eInvMongoLinkId", DmShared.foriegnKey(eGst_Mdl.getMongoLinkId()));
//            psTemp.setString("deliveryTo", DmShared.foriegnKey(eGst_Mdl.getShipTo_Mdl() != null ? eGst_Mdl.getShipTo_Mdl().getSharedKeyFieldCode() : ""));
//            psTemp.setString("deliveryCity", DmShared.foriegnKey(eGst_Mdl.getShipTo_Mdl() != null ? eGst_Mdl.getShipTo_Mdl().getCityCode() : ""));
            psTemp.setString("lrNo", DmShared.xNull(eGst_Mdl.getTransportLrNo()));
            psTemp.setDate("lrDate", DmServer.convertDate(eGst_Mdl.getTransportLrDate()));
            psTemp.setString("vehicleNo", DmShared.xNull(eGst_Mdl.getVehicleNo()));
            psTemp.setString("transporterCode", DmShared.foriegnKey(eGst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode()));
            psTemp.setString("transportMode", DmShared.foriegnKey(eGst_Mdl.getTransportModeCode()));
            psTemp.setString("vehicleTypeCode", DmShared.foriegnKey(eGst_Mdl.getVehicleTypeCode()));
            psTemp.setTimestamp("uEntDt", timestamp);
            psTemp.executeUpdate();
            psTemp.close();
            //<editor-fold defaultstate="collapsed" desc="UPDATE Transporter Detail in party Trans Info">
            PartyDetailPopup_Server.deletePartyTransRecord("partyTransInfo", eGst_Mdl.getDocid(), DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), cnTemp);
            if (eGst_Mdl.getTransporter_Mdl() != null && !DmShared.xNull(eGst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                /*==============================<< save transporter into transPartyInfo >>====================================*/
                PartyDetailPopup_Mdl partyDetailPopup_Mdl = PartyDetailPopup_Server.getPartyDetailPopup_Mdl(eGst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode(), cnTemp);
                partyDetailPopup_Mdl.setSharedKeyFieldCode(DmShared.xNull(eGst_Mdl.getDocid()));
                partyDetailPopup_Mdl.setSharedKeyFieldName(DmShared.iNull(DmEnum.PartyTransRecordType.TRANSPORTER.getCode()).toString());
                PartyDetailPopup_Server.savePartyTransRecord(partyDetailPopup_Mdl, this, cnTemp);
            }
//            </editor-fold>
//
            PartyDetailPopup_Server.updatePartyTransRecord(DmShared.xNull(eGst_Mdl.getDocid()), DmEnum.PartyTransRecordType.TRANSACTION_SITE.getCode(), eGst_Mdl.getConsigner_Mdl(), false, cnTemp);
            PartyDetailPopup_Server.updatePartyTransRecord(DmShared.xNull(eGst_Mdl.getDocid()), DmEnum.PartyTransRecordType.PARTY.getCode(), eGst_Mdl.getBillTo_Mdl(), false, cnTemp);
            PartyDetailPopup_Server.updatePartyTransRecord(DmShared.xNull(eGst_Mdl.getDocid()), DmEnum.PartyTransRecordType.CONSIGNEE.getCode(), eGst_Mdl.getConsignee_Mdl(), false, cnTemp);
            PartyDetailPopup_Server.updatePartyTransRecord(DmShared.xNull(eGst_Mdl.getDocid()), DmEnum.PartyTransRecordType.DELIVERY_TO_PARTY.getCode(), eGst_Mdl.getShipTo_Mdl(), true, cnTemp);
            if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
//                STApprovals.salesInvoiceApproval(WalkThroughEnum.DECLINE, eGst_Mdl.getDocid(), DmServer.getSsnUserName(this), cnTemp, this, true, false, new pmSales().SALES_INVOICE.caption, timestamp);
                deleteRecord(eGst_Mdl.getDocid(), cnTemp);
//==============================<< Log Save >>====================================
                //                saveLog(eGst_Mdl.getDocid(), null, null, DmEnum.DmTransactionMode.DISCARD, null, timestamp, true, cnTemp);
                ///*==============================<< ************** >>====================================*/
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

    private void finalizeGstDocumentProcess(GstDocument_Mdl gst_Mdl, int actionCode, Connection cnTemp) throws Exception {
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
        GstDocument_Mdl gdm = new GstDocument_Mdl();
        dmrTemp.setData(masterEgst);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            masterEgst = Gst_Server.getEGstRecord(strKey, cnTemp, this);
            if (masterEgst != null) {
                eGst_Mdl = masterEgst.geteGst_Mdl();
                gdm = masterEgst.getGst_Mdl();
                if (eGst_Mdl != null) {
                    if (DmShared.xNull(eGst_Mdl.getDocid()).trim().equalsIgnoreCase("")) {
                        eGst_Mdl = getEgstDetail(getMoveRec(strKey, cnTemp), cnTemp);
                    }
                } else {
                    eGst_Mdl = getEgstDetail(getMoveRec(strKey, cnTemp), cnTemp);
                }
            } else {
                eGst_Mdl = getEgstDetail(getMoveRec(strKey, cnTemp), cnTemp);
            }
            if (DmShared.xNull(gdm.getDocid()).trim().equalsIgnoreCase("")) {
                gdm.setDocid(strKey);
            }
            masterEgst.seteGst_Mdl(eGst_Mdl);
            masterEgst.setGst_Mdl(gdm);
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

    private EGst_Mdl getEgstDetail(SaleBillWholeSale_Mdl mrTemp, Connection cnTemp) throws Exception {

        EGst_Mdl eGst_Mdl = new EGst_Mdl();
        String siteCode = DmServer.getSsnSiteCode(this);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            eGst_Mdl = new EGst_Mdl();
            eGst_Mdl.setDocid(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            eGst_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
            eGst_Mdl.setV_Prefix(DmShared.xNull(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "docid", "saleBill", "v_Prefix", cnTemp)));
            eGst_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
            eGst_Mdl.setSite_Code(DmShared.xNull(siteCode));
            eGst_Mdl.setRecId(mrTemp.getRecIdSaleBill());
            eGst_Mdl.setPartyType(DmShared.xNull(mrTemp.getPartyType()));
            // ye abhi hardcoded hai 
            eGst_Mdl.setLocationCode(DmShared.xNull(mrTemp.getLocationCode()));
//            eGst_Mdl.setGstType(String.valueOf((DmShared.iNull(mrTemp.getItcCode()).intValue() == 1) ? (DmEnum.DmGSTType.OUTPUT.toInt()) : (DmEnum.DmGSTType.VOID.toInt())));
            eGst_Mdl.setPosStateNumericCode(DmShared.foriegnKey(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getPlaceOfSupply()), "code", "stateMast", "numericCode", cnTemp)));
            eGst_Mdl.setPosStateName(DmShared.foriegnKey(DmServer.getCodeViaManualCode(DmShared.xNull(mrTemp.getPlaceOfSupply()), "code", "stateMast", "name", cnTemp)));

            eGst_Mdl.setEwayBillRequired(DmShared.iNull(mrTemp.geteWayBillRequiredCode()).intValue() == DmEnum.DmYesNo.YES.toInt());
            eGst_Mdl.setEway_GenerationMode("");
            eGst_Mdl.setU_EntDt_LatestLine(DmShared.xNull(mrTemp.getUserEntDtLatestLine()));

            eGst_Mdl.setCgstValue(DmServer.vNull(mrTemp.getCgstAmt(), "0.00"));
            eGst_Mdl.setIgstValue(DmServer.vNull(mrTemp.getIgstAmt(), "0.00"));
            double sgstUGstValue = DmShared.vNull(mrTemp.getSgstAmt()).doubleValue() + DmShared.vNull(mrTemp.getUgstAmt()).doubleValue();
            eGst_Mdl.setSgstValue(DmServer.vNull(sgstUGstValue, "0.00"));

//            eGst_Mdl.setCessValue(DmServer.vNull(mrTemp.getCessAmount(), "0.00"));
//            eGst_Mdl.setCessNonAdvolValue(DmServer.vNull(mrTemp.getCessNaAmount(), "0.00"));
            eGst_Mdl.setTaxableValue(DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
//            eGst_Mdl.setTotInvValue(DmServer.vNull(mrTemp.gett(), "0.00"));
            eGst_Mdl.setNetInvAmount(DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
            eGst_Mdl.setOtherAmount("0.00");
            //<editor-fold defaultstate="collapsed" desc="From & DispatchFrom">
            Party_Mdl consigner_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.TRANSACTION_SITE.getCode(), DmShared.dNull(mrTemp.getInvoiceDate()), null, false, cnTemp);
            Party_Mdl dispatchFrom_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.DISPATCH_FROM.getCode(), DmShared.dNull(mrTemp.getInvoiceDate()), null, false, cnTemp);
            if (consigner_Mdl == null || DmShared.xNull(consigner_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Consigner is mandatory!");
            } else {
                if (dispatchFrom_Mdl == null || DmShared.xNull(dispatchFrom_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    dispatchFrom_Mdl = consigner_Mdl.clone();
                }
            }
            eGst_Mdl.setConsigner_Mdl(consigner_Mdl.clone());
            eGst_Mdl.setActFromStateNumericCode(DmShared.xNull(dispatchFrom_Mdl.getStateNumericCode()));
            eGst_Mdl.setDispatchFrom_Mdl(dispatchFrom_Mdl.clone());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Bill To, Consignee & shipTo">
            Party_Mdl billTo_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.PARTY.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), DmEnum.DmPartyType.getValueOf(mrTemp.getPartyType()), false, cnTemp);
            Party_Mdl consignee_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.PARTY.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, false, cnTemp);
            Party_Mdl shipTo_Mdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.DELIVERY_TO_PARTY.getCode(), DmShared.dNull(mrTemp.getVoucherDate()), null, false, cnTemp);
            if (billTo_Mdl == null || DmShared.xNull(billTo_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Bill To is mandatory!");
            } else {
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
            }
            eGst_Mdl.setBillTo_Mdl(billTo_Mdl.clone());
            eGst_Mdl.setConsignee_Mdl(consignee_Mdl.clone());
//            eGst_Mdl.setActToStateNumericCode(DmShared.xNull(shipTo_Mdl.getStateNumericCode()));
            eGst_Mdl.setShipTo_Mdl(shipTo_Mdl.clone());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Transporter">
            Party_Mdl transporterMdl = PartyDetailPopup_Server.getPartyTransInfo(mrTemp.getSharedKeyFieldCode(), DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), DmShared.dNull(mrTemp.getInvoiceDate()), null, true, cnTemp);
            eGst_Mdl.setTransporter_Mdl(transporterMdl.clone());
            eGst_Mdl.setTransportModeCode(DmShared.foriegnKey(mrTemp.getTransportModeCode()));
            eGst_Mdl.setTransportModeName(DmShared.foriegnKey(mrTemp.getTransportModeName()));
            eGst_Mdl.setTransportLrNo(DmShared.foriegnKey(mrTemp.getLrNo()));
            eGst_Mdl.setTransportLrDate(DmShared.foriegnKey(mrTemp.getLrDate()));
            //</editor-fold>    
            //            //<editor-fold defaultstate="collapsed" desc="GstVehicleType">
            eGst_Mdl.setVehicleNo(DmShared.foriegnKey(mrTemp.getVehicleNo()));
            eGst_Mdl.setVehicleTypeCode(DmShared.foriegnKey(mrTemp.getVehicleTypeCode()));
            eGst_Mdl.setVehicleTypeName(DmShared.foriegnKey(mrTemp.getVehicleTypeName()));
            if (!DmShared.xNull(mrTemp.getVehicleTypeCode()).trim().equalsIgnoreCase("")) {
                sqlQuery = "select vtm.gstVehicleType \n"
                        + " from vehicleTypeMast vtm \n"
                        + " where vtm.code = :code \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getVehicleTypeCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    eGst_Mdl.setGstVehicleType(DmShared.foriegnKey(rsTemp.getString("gstVehicleType")));
                }
                rsTemp.close();
                psTemp.close();
            }
            //            //</editor-fold>
            //            //<editor-fold defaultstate="collapsed" desc="Transaction Type">
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
            eGst_Mdl.setEwayTransTypeCode(String.valueOf(transTypeCode));
            eGst_Mdl.setEwayTransTypeName(DmEnum.Eway_TransactionType.getViaCode(transTypeCode));
            //</editor-fold>
            //            //<editor-fold defaultstate="collapsed" desc="supply Type, subSupplyType, DocType">
            double totalTaxValue = DmShared.vNull(mrTemp.getSgstAmt()).doubleValue()
                    + DmShared.vNull(mrTemp.getUgstAmt()).doubleValue()
                    + DmShared.vNull(mrTemp.getCgstAmt()).doubleValue()
                    + DmShared.vNull(mrTemp.getIgstAmt()).doubleValue();
//                    + DmShared.vNull(mrTemp.getCessAmount()).doubleValue()
//                    + DmShared.vNull(mrTemp.getCessNaAmount()).doubleValue();
//
            String ewaySubSupplyTypeCode = String.valueOf(DmEnum.Eway_SubSupplyType.SUPPLY.getCode()),
                    ewaySubSupplyTypeName = DmShared.xNull(DmEnum.Eway_SubSupplyType.SUPPLY.getName()),
                    ewayDocTypeCode = (totalTaxValue > 0) ? DmShared.xNull(DmEnum.Eway_DocType.TAX_INVOICE.getCode()) : DmShared.xNull(DmEnum.Eway_DocType.BILL_OF_SUPPLY.getCode()),
                    ewayDocTypeName = DmEnum.Eway_DocType.getName(ewayDocTypeCode),
                    einvSupplyTypeCode = "",
                    einvSupplyTypeName = "",
                    einvDocTypeCode = DmShared.xNull(DmEnum.Einv_DocTypeCode.INVOICE.getCode()),
                    einvDocTypeName = DmShared.xNull(DmEnum.Einv_DocTypeCode.INVOICE.getValue());

            if (DmShared.xNull(billTo_Mdl.getGstin()).equalsIgnoreCase("URP")) {
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

            eGst_Mdl.setEwaySupplyTypeCode(DmShared.xNull(DmEnum.Eway_SupplyType.OUTWARD.getCode()));
            eGst_Mdl.setEwaySupplyTypeName(DmShared.xNull(DmEnum.Eway_SupplyType.OUTWARD.getName()));

            if (1 == 1) {
                eGst_Mdl.setEwaySubSupplyTypeCode(DmShared.foriegnKey(ewaySubSupplyTypeCode));
                eGst_Mdl.setEwaySubSupplyTypeName(DmShared.foriegnKey(ewaySubSupplyTypeName));
                eGst_Mdl.setEwaySubSupplyDesc(DmShared.foriegnKey(ewaySubSupplyTypeName));
                eGst_Mdl.setEwayDocTypeCode(DmShared.foriegnKey(ewayDocTypeCode));
                eGst_Mdl.setEwayDocTypeName(DmShared.foriegnKey(ewayDocTypeName));
                eGst_Mdl.setEinvSupplyTypeCode(DmShared.foriegnKey(einvSupplyTypeCode));
                eGst_Mdl.setEinvSupplyTypeName(DmShared.foriegnKey(einvSupplyTypeName));
                eGst_Mdl.setEinvDocTypeCode(DmShared.foriegnKey(einvDocTypeCode));
                eGst_Mdl.setEinvDocTypeName(DmShared.foriegnKey(einvDocTypeName));
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

//                detail_Mdl.setRate(DmServer.vNull(itemDetail_Mdl.getRate(), "0.00")); Pankaj
                //<editor-fold defaultstate="collapsed" desc="item Rate">
                double itemRate = 0;
                String tempQtyForRate = DmServer.vNull(DmShared.vNull(itemDetail_Mdl.getQuantity()) + DmShared.vNull(itemDetail_Mdl.getFreeQuantityInPurchaseUnit()));

                if (DmShared.vNull(tempQtyForRate).doubleValue() != 0) {
                    itemRate = Math.abs(DmShared.vNull(itemDetail_Mdl.getTaxableAmt()).doubleValue() / DmShared.vNull(tempQtyForRate).doubleValue());
                }
                detail_Mdl.setRate(DmServer.vNull(itemRate, "0.00"));

//</editor-fold>
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
                detail_Mdl.setHsn(DmShared.foriegnKey(itemDetail_Mdl.getMedicineMdl().getHsnNo()));
                detail_Mdl.setQuantity(tempQtyForRate);
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
                    psTemp.setString("code", DmShared.xNull(itemDetail_Mdl.getPurchaseUnitCode()));
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        gstUnitCode = DmShared.xNull(rsTemp.getString("gstUnitCode"));
                    }
                    rsTemp.close();
                    psTemp.close();
                }
//                if (DmShared.xNull(gstUnitCode).trim().equalsIgnoreCase("")) {
//                    throw new Exception("gst unit code is mandatory for item: " + DmShared.xNull(itemDetail_Mdl.getSharedKeyFieldName()) + ".");
//                }
                detail_Mdl.setGstUnit(DmShared.foriegnKey(gstUnitCode));
//                </editor-fold>
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
//                detail_Mdl.setCessRate(DmServer.vNull(itemDetail_Mdl.getCessPer()));
//                detail_Mdl.setCessNonAdvolRate(DmServer.vNull(itemDetail_Mdl.getCessNaRatePerUnit()));
                detail_Mdl.setTaxableAmount(DmServer.vNull(itemDetail_Mdl.getTaxableAmt(), "0.00"));
//                double grossAmount = DmShared.vNull(itemDetail_Mdl.getAmount());
//                detail_Mdl.setGrossAmount(DmServer.vNull(grossAmount, "0.00"));Pankaj
                detail_Mdl.setGrossAmount(DmServer.vNull(itemDetail_Mdl.getTaxableAmt(), "0.00"));
                double lineSgstUGstValue = DmShared.vNull(itemDetail_Mdl.getSgstAmt()).doubleValue() + DmShared.vNull(itemDetail_Mdl.getUgstAmt()).doubleValue();
                detail_Mdl.setSgstAmount(DmServer.vNull(lineSgstUGstValue, "0.00"));
                detail_Mdl.setCgstAmount(DmServer.vNull(itemDetail_Mdl.getCgstAmt(), "0.00"));
                detail_Mdl.setIgstAmount(DmServer.vNull(itemDetail_Mdl.getIgstAmt(), "0.00"));
                detail_Mdl.setCessAmount("0.00");
                detail_Mdl.setCessNaAmount("0.00");
                detail_Mdl.setDiscountAmount("0.00");
                detail_Mdl.setNetAmount(DmServer.vNull(itemDetail_Mdl.getNetAmt(), "0.00"));
                detail_Mdl.setOrderRefNo("");
                //<editor-fold defaultstate="collapsed" desc="propertyDispCsv">
                String propertyDispCsv = "";
//                if (!DmShared.xNull(itemDetail_Mdl.getIbCode()).trim().equalsIgnoreCase("")) {
//                    sqlQuery = "SELECT ib.propertyDispCsv FROM itemBatch ib WHERE ib.code = :ibCode \n";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setString("ibCode", DmShared.xNull(itemDetail_Mdl.getIbCode()));
//                    rsTemp = psTemp.executeQuery();
//                    if (rsTemp.next()) {
//                        propertyDispCsv = DmShared.xNull(rsTemp.getString("propertyDispCsv"));
//                    }
//                    rsTemp.close();
//                    psTemp.close();
//                }
                detail_Mdl.setPropertieDispCsv(DmShared.xNull(propertyDispCsv));
                //</editor-fold>
                detail_Mdl.setOtherCharges("0.00");
                detail_Mdl.setBarCode(null);
                detail_Mdl.setOriginCountry(null);
                detail_Mdl.setItemUSno(null);
                eGst_Mdl.getItemList().add(detail_Mdl);
            }
            //<editor-fold defaultstate="collapsed" desc="Invoice Fields">
            if (!DmShared.xNull(billTo_Mdl.getGstin()).trim().equalsIgnoreCase("") && !DmShared.xNull(billTo_Mdl.getGstin()).equalsIgnoreCase("URP")) {
                eGst_Mdl.setEinvoiceRequired(true);
            } else {
                eGst_Mdl.setEinvoiceRequired(false);
            }
            eGst_Mdl.setEinv_version("1.1");
            eGst_Mdl.setTaxSchemeCode(String.valueOf(DmEnum.Egst_TaxScheme.GST.getCode()));
            eGst_Mdl.setTaxSchemeName(DmEnum.Egst_TaxScheme.GST.toString());
            eGst_Mdl.setReverseCharge(DmEnum.DmYesNo.NO.getEnumShortName());
            eGst_Mdl.setIgstOnIntra(DmEnum.DmYesNo.NO.getEnumShortName());
            eGst_Mdl.setRoundOfAmount(DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
            eGst_Mdl.setDiscountAmount("0.00");
            eGst_Mdl.setTotalInvAmtInFc("0.00");
            //<editor-fold defaultstate="collapsed" desc="Paye Details">
            eGst_Mdl.setPayee_Name(null);
            eGst_Mdl.setPayee_AccountNo(null);
            eGst_Mdl.setPayee_ModeOfPayment(null);
            eGst_Mdl.setPayee_BranchCode(null);
            eGst_Mdl.setPayee_CrTrn(null);
            eGst_Mdl.setPayee_PaymentInstruction(null);
            eGst_Mdl.setPayee_PaymentTerms(null);
            eGst_Mdl.setPayee_DirectDebit(null);
            eGst_Mdl.setPayee_CreditDays(null);
            eGst_Mdl.setPayee_PaidAmount(null);
            eGst_Mdl.setPayee_PaymentDue(null);
            //</editor-fold>
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return eGst_Mdl;
    }
    //</editor-fold>

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
    public DmResultPageing<List<KeyFieldExtended>> getHYesNo(DmHelper pageing, boolean blnToEndOfFile) {
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

            lstTemp = DmShared.filterKeyFieldList(pageing, lstTemp);
            rstTemp.setData(lstTemp);

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

    public void saveAccountPosting(List<ItemAccountPosting_Mdl> itemList, String docId, Connection cnTemp) throws Exception {
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

            for (ItemAccountPosting_Mdl node : itemList) {
                /*====================================< Insert records into ledger >====================================*/
                sqlQuery = "Insert into ledger (docId, v_Sno, v_Type, v_Prefix, v_Date, v_No, recId, recIdPrefix, narration, "
                        + " subCode, contraSub, contraText, amtDr, amtCr, site_Code, isPosted, chq_No, chq_Date, favourOf, dueDate) "
                        + " values (:docId, :v_Sno, :v_Type, :v_Prefix, :v_Date, :v_No, :recId, :recIdPrefix, :narration, "
                        + " :subCode, :contraSub, :contraText, :amtDr, :amtCr, :site_Code, :isPosted, :chq_No, :chq_Date, :favourOf, :dueDate) ";
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
                psTemp.setString("contraSub", null);
                psTemp.setString("contraText", DmShared.foriegnKey(node.getContraText()));
                psTemp.setString("amtDr", DmServer.vNull(node.getAmtdr(), "0.00"));
                psTemp.setString("amtCr", DmServer.vNull(node.getAmtcr(), "0.00"));

                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setDate("dueDate", DmServer.convertDate(node.getDueDate()));

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

    public void saveGatePass(SaleBillWholeSale_Mdl mrTemp, boolean blnAdd, String voucherPrefix, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {

            String v_TypeCode = DmServer.getCodeViaManualCode("WSGPO", "category", "voucher_Type", "v_Type", cnTemp);

            if (DmShared.xNull(mrTemp.getGatePassDocId()).equalsIgnoreCase("")) {
                //<editor-fold defaultstate="collapsed" desc="New Case and DocId Formation">
                mrTemp.setGatePassDocId(DmServer.getDocId(DmShared.xNull(v_TypeCode), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdGatePass(DmServer.getRecId("wholeSaleGatePass", "docId", DmShared.xNull(mrTemp.getGatePassDocId()), "recId", mrTemp.getRecIdGatePass(), mrTemp.getVoucherDate(), DmShared.xNull(v_TypeCode), voucherPrefix, cnTemp, this, true));
                mrTemp.setGatePassRecId(DmShared.xNull(mrTemp.getRecIdGatePass().getRecIdValue()));
                //</editor-fold>

                DmServer.getRecId("wholeSaleGatePass", "docId", DmShared.xNull(mrTemp.getGatePassDocId()), "recId", mrTemp.getRecIdGatePass(), mrTemp.getVoucherDate(), DmShared.xNull(v_TypeCode), voucherPrefix, cnTemp, this, true);
            }

            String[] dateTime = DmServer.getServerDateTime();

            if (blnAdd) {
                //<editor-fold defaultstate="collapsed" desc="Insert SaleBill">
                sqlQuery = " INSERT INTO wholeSaleGatePass \n"
                        + "(docId, v_Type, v_No, recIdPrefix, recId, v_Prefix, v_Date, v_Time, u_EntDt_LatestLine,\n"
                        + " u_Name, preparedDate, preparedBy,  site_Code, sbDocId)\n"
                        + " values (:docId, :v_Type, :v_No, :recIdPrefix, :recId, :v_Prefix, :v_Date, :v_Time,:u_EntDt_LatestLine, \n"
                        + " :u_Name, :preparedDate, :preparedBy, :site_Code, :sbDocId)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getGatePassDocId()));
                psTemp.setString("v_Type", DmShared.xNull(v_TypeCode));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdGatePass().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdGatePass().getPrefix()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getGatePassRecId()));
                psTemp.setString("v_Prefix", DmShared.xNull(voucherPrefix));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Time", DmShared.xNull(dateTime[1]));
                psTemp.setTimestamp("u_EntDt_LatestLine", DmServer.getServerTimestamp());
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
//                psTemp.setString("approvedBy", DmServer.getSsnUserName(this));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("sbDocId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));

//</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="UPDATE saleBill">
                sqlQuery = "UPDATE wholeSaleGatePass SET \n"
                        + "    v_Date = :v_Date,\n"
                        + "    v_Time = :v_Time,\n"
                        + "    u_EntDt_LatestLine = :u_EntDt_LatestLine,\n"
                        + "    u_Name = :u_Name,\n"
                        + "    preparedDate = :preparedDate,\n"
                        + "    preparedBy = :preparedBy,\n"
                        //                        + "    approvedBy = :approvedBy,\n"
                        + "    site_Code = :site_Code,\n"
                        + "    sbDocId = :sbDocId\n"
                        + "    WHERE docId = :docId\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getGatePassDocId()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Time", DmShared.xNull(dateTime[1]));
                psTemp.setTimestamp("u_EntDt_LatestLine", DmServer.getServerTimestamp());
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
//                psTemp.setString("approvedBy", DmServer.getSsnUserName(this));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("sbDocId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));

//</editor-fold>
            }
            psTemp.executeUpdate();
            psTemp.close();

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

    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String gatePassDocId, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                sqlQuery = "UPDATE wholeSaleGatePass SET \n"
                        + "    approvedBy = :approvedBy,\n"
                        + "    approvalDateTime = :approvalDateTime \n"
                        + "    WHERE docId = :docId\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(gatePassDocId));
                psTemp.setString("approvedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("approvalDateTime", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "UPDATE wholeSaleGatePass SET \n"
                        + "    approvedBy = :approvedBy,\n"
                        + "    approvalDateTime = :approvalDateTime \n"
                        + "    WHERE docId = :docId\n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(gatePassDocId));
                psTemp.setString("approvedBy", null);
                psTemp.setTimestamp("approvalDateTime", null);
            }

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void saveLog(String strKey, ModifyReason_Mdl modifyReasonMdl, SaleBillWholeSale_Mdl mrTemp, DmEnum.DmTransactionMode transactionMode, DmEnum.DmTransactionMode controlText) {
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
            SaleBillWholeSale_Mdl oldMdl = null;
            //create new reason mode in case of print, new
            if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DISCARD.getValue())
                    || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                //check if latest record's old data is saved in moverec
                oldMdl = (SaleBillWholeSale_Mdl) DmServer.getSsnOldData(getClass().getName(), this);
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
                modifyData_Mdl.setMenuName(new pmPharmacy().SALE_BILL_WHOLE_SALE.caption);
                if (controlText != null) {
                    modifyData_Mdl.setControlText(controlText.getValue());
                }
                modifyData_Mdl.setModelClassName(SaleBillWholeSale_Mdl.class.getName());
                modifyData_Mdl.setVrTypeDescription(description);
                modifyData_Mdl.setVrTypeSName(DmShared.xNull(shortName));
                modifyData_Mdl.setTableName("saleBill");
                //saving old record for edit, delete
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DISCARD.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.DELETE.getValue())) {
                    modifyData_Mdl.setOldData(DmServer.convertToJson(oldMdl, new TypeToken<SaleBillWholeSale_Mdl>() {
                    }));
                }
                if (DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.NEW.getValue())
                        || DmShared.xNull(transactionMode.getValue()).equalsIgnoreCase(DmEnum.DmTransactionMode.EDIT.getValue())) {
                    modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<SaleBillWholeSale_Mdl>() {
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
