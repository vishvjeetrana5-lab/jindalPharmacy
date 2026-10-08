package aarogya.pharmacy.saleBill.server;

import aarogya.pharmacy.ledgerMaster.server.LedgerMasterServer;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.saleBill.shared.*;
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
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
import dataman.dmBase.dmPrepareStatement.server.*;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum.DmLocation;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.CardType;
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
import java.util.List;

public class SaleBill_SrvImpl extends DmRSSPrivate implements SaleBill_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypefieldName = "mb.V_Type", voucherDateFieldName = "mb.V_Date";
    private String category = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";

    //pr.site_Code in (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ")
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

    @Override
    public DmResult<SaleBill_Mdl> getMoveRec(String strKey) {
        DmResult<SaleBill_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        CardPaymentPopup_Mdl cardPaymentPopup_Mdl = new CardPaymentPopup_Mdl();
        SaleBill_Mdl mrTemp = new SaleBill_Mdl();
        dmrTemp.setData(mrTemp);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        TxtHMedicine_Mdl medicineMdl;
        SearchUHID_Mdl searchMdl = new SearchUHID_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT mb.docId, mb.recId, mb.v_Type, pbvt.description, mb.v_Date, prvt.category, mb.regDocId,mb.subCode, isNull(pr.recId,lm.manualcode) AS regNo, pr.site_Code AS regSiteCode, \n"
                    + "     pr.roomCode,isNull(mb.uhid,lm.code) as uhid, isNull(pu.recId,lm.manualcode) AS UHIDNo, mb.departmentCode, sdm.name AS departmentName, lm.displayName as patientName, \n"
                    + "     mb.consultantCode, sgcon.name AS consultantName, mb.preparedBy, mb.u_Name, mb.remark, mb.oh_Amt_Gross, mb.oh_Amt_Net, mb.oh_Amt_LandedValue, \n"
                    + "     mb.oh_Amt_BAMT, mb.oh_Amt_RoundOff, mb.oh_Amt_CGST, mb.oh_Amt_SGST, mb.oh_Amt_UGST, mb.oh_Amt_Taxable, mb.oh_Amt_Discount,mb.oh_Amt_Discount2, \n"
                    + "     mb.recIdPrefix, mb.v_No, prec.v_type as precVType, prec.recid as precRecID, prec.recIdPrefix as precRecIdPrefix, prec.v_No as precRecIdCounter, \n"
                    + "     vtprec.description as precVDescription, vtprec.Category as rcptVTypeCategory, mb.idTypeCode, mb.idNo, idType.name idType, \n"
                    + "     vtprec.attachPaymentMode AS attachPaymentMode, mb.isCancelled, mb.estimateDocId, \n"
                    + "     est.recId estNo, mb.mobile,mb.u_EntDt_LatestLine\n"
                    + "     ,mb.partyType\n"
                    + "     ,mb.lmCode,lm.address,c.cityName,lm.cityCode,mb.placeOfsupply as stateCode\n"
                    + "     ,sm.name as stateName \n"
                    + "     , lm.name as lmName \n"
                    + "     , lm.manualCode as lmManualCode, mb.paymentSource,h7m.name as patientClass \n"
                    + "     , case when isnull(pak.packageCount,0)>0 then 'Package Alloted' else '' end as pakAlloted,pbvt.short_Name \n "
                    + "   , mb.regDocId_HIS ,mb.uhidNo_HIS ,mb.regNo_HIS , mb.regDocid_HIS "
                    + "  FROM saleBill mb \n"
                    + "  LEFT JOIN voucher_type pbvt ON mb.v_Type=pbvt.v_Type    \n"
                    + "  LEFT JOIN LedgerMast AS lm ON lm.code = mb.lmCode \n"
                    + "  LEFT JOIN patientregistration pr ON mb.regDocid=pr.regDocid  \n"
                    + "  left join patientRegistration2 pr2 on pr.regDocID=pr2.regDocID \n "
                    + "  left join hl7mast h7m on pr2.viptYpeCode=h7m.code  \n"
                    + "  LEFT JOIN (  \n"
                    + "             select count(packageCode) as packageCount,regDocID from patientPackageAllocation group by regDocID \n"
                    + "             ) As pak on pr.regDocID=pak.regDocID \n"
                    + "  LEFT JOIN voucher_type prvt ON pr.v_Type=prvt.v_Type    \n"
                    + "  LEFT JOIN SiteMast prsm ON pr.site_Code=prsm.code  \n"
                    + "  LEFT JOIN patientuhid pu ON mb.uhid = pu.docId  \n"
                    + "  LEFT JOIN subgroup sdm ON mb.departmentCode=sdm.subcode  \n"
                    + "  LEFT JOIN consultantmast cm ON mb.consultantCode=cm.subCode  \n"
                    + "  LEFT JOIN subGroup AS sgcon ON cm.subCode = sgcon.subCode  \n"
                    + "  LEFT JOIN patientReceipt AS prec ON mb.DocId = prec.docId \n"
                    + "  LEFT JOIN city AS c ON c.citycode = lm.citycode \n"
                    + "  LEFT JOIN stateMast AS sm ON sm.code = mb.placeOfsupply \n"
                    + "	 LEFT JOIN hl7Mast as idType on idType.code = mb.idTypeCode \n"
                    + "	 LEFT JOIN voucher_type vtprec ON prec.v_type = vtprec.v_type \n"
                    + "	 LEFT JOIN saleBill est ON est.docId = mb.estimateDocId \n"
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
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setVoucherTypeShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("category")));

                if (!DmShared.xNull(rsTemp.getString("regNo_HIS")).equalsIgnoreCase("")) {
                    mrTemp.setRegRecID(DmShared.xNull(rsTemp.getString("regNo_HIS")));
                } else {
                    mrTemp.setRegRecID(DmShared.xNull(rsTemp.getString("regNo")));
                }

                mrTemp.setRegSiteCode(DmShared.xNull(rsTemp.getString("regSiteCode")));
                mrTemp.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));
                mrTemp.setDocIdEstimate(DmShared.xNull(rsTemp.getString("estimateDocid")));
                mrTemp.setRecIdEstimate(DmShared.xNull(rsTemp.getString("estNo")));

                mrTemp.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                mrTemp.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));

                if (!DmShared.xNull(rsTemp.getString("regNo_HIS")).equalsIgnoreCase("")) {
                    mrTemp.setRegDocid(DmShared.xNull(rsTemp.getString("regDocid_HIS")));
                } else {
                    mrTemp.setRegDocid(DmShared.xNull(rsTemp.getString("regDocId")));
                }

                mrTemp.setUhidDocid(DmShared.xNull(rsTemp.getString("uhid")));

                if (!DmShared.xNull(rsTemp.getString("uhidNo_HIS")).equalsIgnoreCase("")) {
                    mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo_HIS")));
                } else {
                    mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("UHIDNo")));
                }

                mrTemp.setPatientClassType(DmShared.xNull(rsTemp.getString("patientClass")));
                mrTemp.setPatientPackagedInfo(DmShared.xNull(rsTemp.getString("pakAlloted")));
                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));

                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("stateCode")));
                mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idType")));
                mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));

                mrTemp.setGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.setDiscountAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                mrTemp.setDiscountAmt2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));
                mrTemp.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                mrTemp.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                mrTemp.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                mrTemp.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                mrTemp.setGstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));
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
                searchMdl.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                searchMdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));

                mrTemp.setPaymentSource(DmShared.xNull(rsTemp.getString("paymentSource")));
                // to Fetch the patient info from aarogya_Transaction database
                cnTemp.setCatalog(STServer.getSsnParentDateBase(this));
                getPatientDetail(mrTemp, searchMdl, true, cnTemp);
                cnTemp.setCatalog(DmServer.getSsnDBName(this));
                if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() > 0) {
                    mrTemp.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(DmShared.iNull(rsTemp.getString("lmCode")).toString(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
                }

                if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() == 0 && DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() > 0) {
                        mrTemp.setTypeCode("Ledger");
                        mrTemp.setTypeName("Ledger");
                    }
                } else if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() == 0) {
                    mrTemp.setTypeCode("UHID");
                    mrTemp.setTypeName("UHID");
                }

                if (DmShared.iNull(rsTemp.getString("regDocId_HIS")).longValue() != 0) {
                    mrTemp.setTypeCode("IPD");
                    mrTemp.setTypeName("IPD");
                }

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select mbd.docId, mbd.v_SNo, mm.isBestBefore, mbd.hsnCode, mbd.itemGSTNature, mbd.itemCode, mm.name AS itemName, mbd.quantity, "
                    + "         mbd.skuQuantity as totalSkuQty, mbd.rate, ic.name as itemCategory, mbd.oh_at_Discount, mbd.oh_at_Discount2, mbd.oh_Amt_Discount, mbd.oh_Amt_Discount2, um.decimalPlaces as skuDecimalPlaces, "
                    + "         mbd.oh_Amt_Net, mbd.landedSkuValue, mbd.oh_Amt_Gross, mbd.oh_at_CGST, mbd.isFooter, mm.sku, um.unit AS skuName, umsale.gstUnitCode, "
                    + "         mbd.requisitionDocId, mbd.requisitionV_SNo, mr.recId reqNo, mbd.sbDocId, mbl.recId AS mBillNo, "
                    + "         umsale.unit AS saleUnitName, mbd.saleUnitCode AS saleUnit"
                    + "         ,CAST(mm.saleUnitConversionRate * mbd.quantity AS decimal(18, 7)) AS skuQuantity "
                    + "         ,mm.saleUnitConversionRate, "
                    + "         umsale.decimalPlaces as saleUnitDecimalPlaces, mbd.oh_Amt_CGST, mbd.oh_Amt_SGST, mbd.oh_at_SGST, mbd.oh_Amt_Taxable, mbd.oh_Amt_UGST, "
                    + "         mbd.oh_at_UGST, mbd.mrp, mbd.skuRate, mbd.skuMrp, mm.allowNegativeStock, mm.isBarcodeActive, mbd.isReturn, mbd.oh_Ac_CGST, "
                    + "         mbd.oh_Ac_SGST, mbd.oh_Ac_UGST, mbd.oh_Ac_Sales, mm.itsBasedOn, mbd.batchNo, mbd.expiryDate, mbd.manufacturingDate, "
                    + "         mbd.ibCode, mm.restrictGroup, mm.purchaseUnit, ump.unit purchaseUnitName, mbd.skuQtyForRate,"
                    + "         (IsNull(mbd.quantity,0)-IsNull(mbd.returnedQty,0)) as sbQuantity ,mbd.returnedQty"
                    + "         ,ig.code as groupCode,ig.name as groupName,mbd.discountGivenBy,mbd.discountReason "
                    + "         ,mbd.freeWithItemCode,mbd.freeQty,mm.isReturnableItem, mm.isDiscountAllow, mm.isSaleRateEditable, mm.itsBasedOn, mbd.barcode,mbd.grossPurchaseRate "
                    + "	from saleBillDetail AS mbd "
                    + "	left join saleBill AS mbl ON mbd.sbDocId = mbl.docId "
                    + "	left join requisition AS mr ON mr.docId = mbd.requisitionDocId "
                    + "	left join itemMast AS mm ON mbd.itemCode = mm.code "
                    + " left join itemGroup ig on ig.code = mm.itemGroup "
                    + " left join itemCat ic on ic.code = mm.itemCategoryCode "
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
                detailMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                detailMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                detailMdl.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                detailMdl.setDiscountGivenByTag(DmShared.xNull(rsTemp.getString("discountGivenBy")));
                detailMdl.setDiscountReason(DmShared.xNull(rsTemp.getString("discountReason")));
                detailMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                detailMdl.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));
                detailMdl.setGrossPurchaseRate(DmServer.vNull(rsTemp.getString("grossPurchaseRate"), "0.00"));

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue());
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
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).intValue() == 1);
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
//                medicineMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
//                medicineMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
//                medicineMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
//                medicineMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                detailMdl.setMedicineMdl(medicineMdl);

                detailMdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                detailMdl.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                detailMdl.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                detailMdl.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));

                if (DmShared.vNull(rsTemp.getString("returnedQty")).doubleValue() == 0) {
                    detailMdl.setStatus("0");//no Color
                } else if (DmShared.vNull(rsTemp.getString("sbQuantity")).doubleValue() == 0) {
                    detailMdl.setStatus("2");//green color
                } else if (DmShared.vNull(rsTemp.getString("returnedQty")).doubleValue() != 0) {
                    detailMdl.setStatus("1");//light gray
                }

                detailMdl.setFreeQuantityInSaleUnit(DmServer.vNull(rsTemp.getString("freeQty")));
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
                detailMdl.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                detailMdl.setQuantitySKU(DmServer.vNull(rsTemp.getString("skuQuantity")));
                detailMdl.setTotalQuantitySKU(DmServer.vNull(rsTemp.getString("totalSkuQty")));
                detailMdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                detailMdl.setReturnSale(DmShared.iNull(rsTemp.getString("isReturn")).intValue() == 1);
                detailMdl.setReqDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));

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

                detailMdl.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
                detailMdl.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));

                if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() == 0) {
                    mrTemp.setDocIdRequisition(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                    mrTemp.setRecIdRequisition(DmShared.xNull(rsTemp.getString("reqNo")));
                }
                detailMdl.setReqVSno(DmShared.xNull(rsTemp.getString("requisitionV_SNo")));
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

            if (isEstimateBillCreated(mrTemp.getSharedKeyFieldCode(), true, cnTemp)) {
                mrTemp.setIsEstimateSaleCreated("1");
            } else {
                mrTemp.setIsEstimateSaleCreated("0");
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

            cnTemp.close();

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

            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(String voucherCategory, SaleBill_Mdl mrTemp, SaleBill_Mdl prevMdl, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, isHaveValue = false, isEstimateBill = false, isEstimate = false, xisCTPAllow = false, allowReturnWithoutSale = false; //, allowReturnValue = false
        String sqlQuery, userName, siteCode, receiptVType = "", requisitionDocId = "", requisitionVSno = "", saleQty = "";
        TxtHMedicine_Mdl medicineMdl;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        CardType cardType = null;
        dmrTemp.setData("");

        try {
            if (DmShared.xNull(mrTemp.getIdTypeName()).trim().equalsIgnoreCase("Aadhaar")) {
                mrTemp.setIdNo(DmShared.xNull(mrTemp.getIdNo()).replaceAll(" ", ""));
                if (DmShared.xNull(mrTemp.getIdNo()).trim().length() != 12) {
                    throw new DmException(0, "Aadhaar No. should be 12 digit.");
                }
                if (!DmShared.isNumeric(mrTemp.getIdNo())) {
                    throw new DmException(0, "Aadhaar No. should be numeric.");
                }
            }

            SaleBill_Vld.chkSave(mrTemp, voucherCategory);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
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

            sqlQuery = "select isNull(isActive,0) as isActive from subGroup where subCode = :subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(mrTemp.getConsultantCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isActive")).intValue() == 0) {
                    throw new DmException(0, "Consultant is not available");
                }
            }
            rsTemp.close();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Check for Requisition item change">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getReqItemChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM requisitionDetail rqd " + DmShared.xNull(mrTemp.getReqItemChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Requisition has been changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="CHK For Incorrect Batch">
            /*==============================<<  >>====================================*/
            if (!DmShared.xNull(mrTemp.getInCorrectSelectedBatchSql()).equalsIgnoreCase("")) {
//                sqlQuery = " SELECT STUFF((SELECT char(10)+','+ vib.itemName + '-' + vib.batchNo AS [text()]"
//                        + " FROM (" + mrTemp.getInCorrectSelectedBatchSql() + ") vIb "
//                        + " LEFT JOIN itemBatch ib  ON vIb.ibCode = ib.code  AND vIb.itemCode = ib.itemCode "
//                        + " WHERE vIb.ibCode is Not Null and IB.itemCode IS NULL"
//                        + " FOR XML PATH('')), 1,2, '') "
//                        + " as incorrectMedicineBatches ";
                sqlQuery = "select  stuff(("
                        + "         select char(10)+','+ vib.itemName + '-' + vib.batchNo AS [text()] "
                        + "         from (" + mrTemp.getInCorrectSelectedBatchSql() + ") vIb "
                        + "         left join itemBatch ib  ON vIb.ibCode = ib.code and vIb.itemCode = ib.itemCode "
                        + "         where vib.ibCode is not null and ib.itemCode is null "
                        + " FOR XML PATH(''), TYPE).value('.[1]','nvarchar(max)'), 1,2,'') "
                        + " as incorrectMedicineBatches ";
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

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                //<editor-fold defaultstate="collapsed" desc="New Case and DocId Formation">
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdSaleBill(DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdSaleBill(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdSaleBill().getRecIdValue()));
                //</editor-fold>
            } else {
                DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdSaleBill(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
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

                if (DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.UHID)) {
                    ledgerMdl.setRegDocId("");
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
                    if (DmShared.iNull(mrTemp.getRegDocid()).longValue() != 0 || DmShared.iNull(mrTemp.getUhidDocid()).longValue() != 0 || DmShared.iNull(mrTemp.getLmCode()).longValue() != 0) {
                        if (mdl.isReturnSale() && DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                            saleQty = checkItemSale(mdl.getSharedKeyFieldCode(), mdl.getIbCode(), mrTemp.getRegDocid(), mrTemp.getUhidDocid(), mrTemp.getLmCode(), mdl.getPrvQty(), mdl.getPrvFreeQty(), cnTemp);
                            if (DmShared.vNull(saleQty).doubleValue() > 0) {
                                if ((Math.abs(DmShared.vNull(mdl.getQuantity()).doubleValue()) + Math.abs(DmShared.vNull(mdl.getFreeQuantityInSaleUnit()).doubleValue())) > DmShared.vNull(saleQty).doubleValue()) {
                                    throw new DmException(0, mdl.getSharedKeyFieldName() + " with Batch No. '" + mdl.getBatchNo() + "' has Return Quantity : " + (Math.abs(DmShared.vNull(mdl.getQuantity()).doubleValue()) + Math.abs(DmShared.vNull(mdl.getFreeQuantityInSaleUnit()).doubleValue())) + " more than Sale Quantity :  " + saleQty);
                                }
                            } else {
                                throw new DmException(0, mdl.getSharedKeyFieldName() + " with Batch No. '" + mdl.getBatchNo() + "', never sold to/already returned by, " + mrTemp.getPatientName());
                            }
                        }
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Chk change of saleBill">
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

            //<editor-fold defaultstate="collapsed" desc="chek Estimate">
//            if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
//                sqlQuery = "select COUNT(*) AS cnt  "
//                        + " from saleBill sb  "
//                        + " left join voucher_Type vt on vt.v_Type = sb.v_Type  "
//                        + " where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ")"
//                        + " and sb.isCancelled = 0  "
//                        + " and sb.estimateDocId = :estimateDocId  "
//                        + " and sb.docId != :docId ";
//                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setString("estimateDocId", DmShared.iNull(mrTemp.getDocIdEstimate()).toString());
//                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//                rsTemp = psTemp.executeQuery();
//                if (rsTemp.next()) {
//                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
//                        throw new DmException(0, "Bill is already generated for this Estimate!");
//                    }
//                }
//                rsTemp.close();
//                psTemp.close();
//            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="chk Return">
            if (!isEstimate) {
                isReturnedFromSaleBillReturn(mrTemp.getSharedKeyFieldCode(), false, cnTemp);
            }
            //</editor-fold>

            if (isEstimate) {
                isEstimateBillCreated(mrTemp.getSharedKeyFieldCode(), false, cnTemp);
            }

            if (!isEstimateBill && !isEstimate) {
                //<editor-fold defaultstate="collapsed" desc="IPD/OPD CASE CHECK">
                if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION
                        || mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {

                    if (DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase("")) {
                        if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                            throw new UnsupportedOperationException("If Payment Mode is Credit to Room then IPD / Day Care / Emergency Type is Mandatory.");
                        } else {
                            throw new UnsupportedOperationException("If Payment Mode is Credit to Organization then OPD Type is Mandatory.");
                        }

                    } else {
                        if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0) {
                            if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                                throw new UnsupportedOperationException("If Payment Mode is Credit to Room then IPD No. / Day Care No./ Emergency No. is Mandatory.");
                            } else {
                                throw new UnsupportedOperationException("If Payment Mode is Credit to Organization then OPD No. is Mandatory.");
                            }
                        } else {
                            if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                                if (!(DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.IPD) || DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.DAY_CARE) || DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.EMERGENCY))) {
                                    throw new UnsupportedOperationException("When we use card type Credit To Room then Type should be IPD / Day Care / Emergency and No. will Be IPD No. / Day Care No. / Emergency No..");
                                }
                            } else {
                                if (!DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.OPD)) {
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
                            throw new DmException(0, "Patient Bill is generated, can't discard this entry!");
                        }
                    }
                    rsTemp.close();
                    psTemp.close();

                    STServer.checkBillApproval(mrTemp.getRegDocid(), cnTemp);

                    if (mrTemp.getCardPayment().getGrdCardPaymentList() != null && mrTemp.getCardPayment().getGrdCardPaymentList().size() > 0) {
                        if (DmShared.vNull(mrTemp.getCardPayment().getGrdCardPaymentList().get(0).getAmount()).doubleValue() != 0) {
                            throw new UnsupportedOperationException("In case of CTR / CTO you have to settle full bill amount.");
                        }
                    }

                    if (DmShared.iNull(mrTemp.getDefaultPharmacyServiceCode()).longValue() == 0) {
                        throw new UnsupportedOperationException("Please define Pharmacy Service in Environment Settings.");
                    }
                }
                if (DmShared.xNull(mrTemp.getFlagCardPayment()).trim().equalsIgnoreCase("1")) {
                    CardPaymentPopup_Server.chkSave(true, mrTemp.getNetAmount(), mrTemp.getSharedKeyFieldCode(), mrTemp.getCardPayment(), cnTemp, true, true, true);
                }
                CardPaymentPopup_Server.delete(mrTemp.getSharedKeyFieldCode(), cnTemp);

                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Check whether posted (via EOD) or not">
                STServer.checkEOD("saleBillDetail", "docId", mrTemp.getSharedKeyFieldCode(), cnTemp);
                //</editor-fold>
            }

            //<editor-fold defaultstate="collapsed" desc="Check of CTO">
            if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
//                sqlQuery = "  select count(*) as cnt from "
//                        + " patientLimitApproval pla "
//                        + " WHERE pla.docid=:regDocId "
//                        + " AND pla.nature=:nature ";
                sqlQuery = "  select count(*) as cnt from "
                        + " patientRegistration pr "
                        + " WHERE pr.regDocId=:regDocId "
                        + " AND pr.paymentSource=:nature ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocid()).toString());
                psTemp.setString("nature", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 0) {
                        throw new DmException(0, "Credit To Organisation(CTO) cannot be done.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>

            if (blnAdd) {
                /*==============================< chek Estimate >===========================================*/
                sqlQuery = " select sb.docId  from saleBill sb  "
                        + " where isnull(sb.isCancelled,0) = 0  "
                        + " and sb.estimateDocId = :estimateDocId  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("estimateDocId", DmShared.iNull(mrTemp.getDocIdEstimate()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    throw new DmException(0, "Bill is already generated for this Estimate!");
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.xNull(mrTemp.getTypeCode()).equalsIgnoreCase(pvc.UHID)) {
                    mrTemp.setRegDocid("");
                }

                //<editor-fold defaultstate="collapsed" desc="Insert SaleBill">
                sqlQuery = " insert into saleBill (docId, v_Type, v_No, recId, v_Prefix, v_Date,v_Time, regDocId,regDocId_HIS, "
                        + "     departmentCode, consultantCode, remark, u_EntDt, preparedDate, preparedBy, site_Code, oh_Amt_Gross, oh_Amt_Net, oh_Amt_BAMT, "
                        + "     oh_Amt_RoundOff, oh_Amt_CGST, oh_Amt_SGST, oh_Amt_Taxable, oh_Amt_Discount, oh_Amt_Discount2, recIdPrefix, oh_Amt_UGST,"
                        + "     estimateDocId, uhid, idTypeCode, idNo, mobile, u_EntDt_LatestLine,partyType,lmCode,subCode,consignee,placeOfSupply, oh_Amt_LandedValue, paymentSource,regNo_HIS,uhidNo_HIS,siteCode_HIS,regType_HIS)"
                        + " values (:docId, :vType, :vNo, :recId, :vPrefix, :vDate,:v_Time, :regDocId,:regDocId_HIS, :departmentCode,"
                        + "      :consultantCode, :remark, :uEntDt, :uEntDt, :userName, :siteCode, :ohAmtGross, :ohAmtNet, :ohAmtBAMT, :oh_Amt_RoundOff, "
                        + "      :ohAmtCGST, :ohAmtSGST, :ohAmtTaxable, :ohAmtDiscount, :ohAmtDiscount2, :recIdPrefix, :ohAmtUGST, :estimateDocId,"
                        + "      :uhid, :idTypeCode, :idNo, :mobile,:uEntDt,:partyType,:lmCode,:subCode,:consignee,:placeOfSupply, :oh_Amt_LandedValue,:paymentSource,:regNo_HIS,:uhidNo_HIS,:siteCode_HIS,:regType_HIS)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("vNo", DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
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
                psTemp.setString("estimateDocId", DmShared.foriegnKey(mrTemp.getDocIdEstimate()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("uhid", DmShared.foriegnKey(""));
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
                psTemp.setString("ohAmtTaxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("ohAmtNet", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("oh_Amt_LandedValue", DmServer.vNull(mrTemp.getLandedValue(), "0.00"));
                psTemp.setString("ohAmtBAMT", DmServer.vNull(mrTemp.getBillAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmt(), "0.00"));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("paymentSource", mrTemp.getCardPayment().getPaymentSource() == null ? null : DmShared.iNull(mrTemp.getCardPayment().getPaymentSource().getEnumValue()).toString());
//</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="UPDATE saleBill">
                sqlQuery = "UPDATE saleBill SET "
                        + " v_Date = :vDate, "
                        + " u_EntDt = :uEntDt, "
                        + " u_EntDt_LatestLine= :uEntDt ,"
                        + " lmCode= :lmCode ,"
                        + " subCode= :subCode ,"
                        + " consignee= :consignee ,"
                        + " placeOfSupply= :placeOfSupply ,"
                        + " departmentCode = :departmentCode, consultantCode = :consultantCode,"
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
                        + " u_Name = :userName "
                        + " WHERE DocID = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentCode()));
                psTemp.setString("consultantCode", DmShared.foriegnKey(mrTemp.getConsultantCode()));
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
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));

//</editor-fold>
            }
            psTemp.executeUpdate();
            psTemp.close();

            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            if (!isEstimate) {
                /*=============================<< Validate Removed Received Items >>================================*/
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), DmEnum.StockEntryType.OTHER, cnTemp, this);

                Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp, true);
                Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);

                sqlQuery = "Delete From patientReceipt Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), true, cnTemp);

            }

            //<editor-fold defaultstate="collapsed" desc="INSERT IN SALE BILL DETAIL">
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
                    sqlQuery = "insert into saleBillDetail (docId, v_SNo, itemCode, hsnCode, saleUnitCode, quantity, requisitionDocId, requisitionV_SNo, "
                            + "		oh_at_Discount, oh_at_Discount2, oh_Amt_Discount, oh_Amt_Discount2, oh_Amt_Net, oh_Amt_CGST, oh_at_CGST, oh_Amt_SGST, oh_at_SGST, oh_at_UGST, "
                            + "         oh_Amt_UGST, rate, oh_Amt_Taxable, itemGSTNature, mrp, isReturn, skuQuantity, oh_Ac_Sales, oh_Ac_CGST, "
                            + "         oh_Ac_UGST, oh_Ac_SGST, oh_Amt_Gross, batchNo, manufacturingDate, expiryDate, ibCode, "
                            + "         skuMrp, skuRate, skuQtyForRate,discountGivenBy,discountReason,freeWithItemCode,freeQty, landedSkuValue, barcode,grossPurchaseRate)"
                            + "         values (:docId, :v_sNo, :itemCode, :hsnCode, :saleUnitCode, :quantity, :requisitionDocId, :requisitionV_SNo,"
                            + "		:oh_at_Discount, :oh_at_Discount2, :oh_Amt_Discount, :oh_Amt_Discount2, :oh_Amt_Net, :oh_Amt_CGST, :oh_at_CGST, :oh_Amt_SGST, :oh_at_SGST, "
                            + "		:oh_at_UGST, :oh_Amt_UGST, :rate, :oh_Amt_Taxable, :itemGSTNature, :mrp, :isReturn, :skuQuantity, :oh_Ac_Sales, "
                            + "         :oh_Ac_CGST, :oh_Ac_UGST, :oh_Ac_SGST, :oh_Amt_Gross, :batchNo, :manufacturingDate, :expiryDate, :ibCode, :skuMrp, "
                            + "         :skuRate, :skuQtyForRate,:discountGivenBy,:discountReason,:freeWithItemCode,:freeQty, :landedSkuValue, :barcode,:grossPurchaseRate)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSno()).toString());
                    psTemp.setString("itemCode", medicineMdl.getSharedKeyFieldCode());
                    psTemp.setString("hsnCode", DmShared.foriegnKey(medicineMdl.getHsnCode()));
                    psTemp.setString("itemGSTNature", DmShared.xNull(medicineMdl.getItemGstNature()));
                    psTemp.setString("saleUnitCode", DmShared.foriegnKey(medicineMdl.getSaleUnitCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQuantityInSaleUnit()));
                    psTemp.setString("skuQuantity", DmServer.vNull(detail_Mdl.getTotalQuantitySKU()));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(medicineMdl.getPurchaseUnitConversionRate()));
                    psTemp.setString("requisitionDocId", DmShared.foriegnKey(detail_Mdl.getReqDocId()));
                    psTemp.setString("requisitionV_SNo", DmShared.foriegnKey(detail_Mdl.getReqVSno()));
                    psTemp.setString("freeWithItemCode", DmShared.foriegnKey(detail_Mdl.getFreeWithItemCode()));
                    psTemp.setString("oh_Ac_SGST", DmShared.foriegnKey(detail_Mdl.getSgstAcCode()));
                    psTemp.setString("oh_Ac_UGST", DmShared.foriegnKey(detail_Mdl.getUgstAcCode()));
                    psTemp.setString("oh_Ac_CGST", DmShared.foriegnKey(detail_Mdl.getCgstAcCode()));
                    psTemp.setString("oh_Ac_Sales", DmShared.foriegnKey(detail_Mdl.getSaleAcCode()));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    psTemp.setString("oh_at_Discount", DmServer.vNull(medicineMdl.getDiscountPer(), "0.00"));
                    psTemp.setString("oh_at_Discount2", DmServer.vNull(detail_Mdl.getDiscountPer2(), "0.00"));
                    psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                    psTemp.setString("oh_Amt_Discount2", DmServer.vNull(detail_Mdl.getDiscount2(), "0.00"));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getNetAmt(), "0.00"));
                    psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgstAmt(), "0.00"));
                    psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgstAmt(), "0.00"));
                    psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                    psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgstAmt(), "0.00"));
                    psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                    psTemp.setString("isReturn", detail_Mdl.isReturnSale() ? "1" : "0");
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setString("discountGivenBy", DmShared.foriegnKey(detail_Mdl.getDiscountGivenByTag()));
                    psTemp.setString("discountReason", DmShared.xNull(detail_Mdl.getDiscountReason()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getMfgDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpDate()));
                    psTemp.setString("landedSkuValue", DmServer.vNull(detail_Mdl.getLandedSkuValue(), "0.00"));
                    psTemp.setString("barcode", DmShared.xNull(detail_Mdl.getBarcode()));
                    psTemp.setString("grossPurchaseRate", DmServer.vNull(DmShared.vNull(detail_Mdl.getGrossPurchaseRate()), "0.00"));

                    psTemp.executeUpdate();
                    psTemp.close();

                    requisitionDocId = detail_Mdl.getReqDocId();
                    requisitionVSno = detail_Mdl.getReqVSno();
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
                        stock_Mdl.setGrossPurchaseRate(DmServer.vNull((DmShared.vNull(detail_Mdl.getTotalQuantitySKU()) * DmShared.vNull(detail_Mdl.getGrossPurchaseRate())), "0.00"));

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
                    if (!isEstimateBill && !isEstimate) {
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
                        gstTrans_Mdl.setQty(DmServer.vNull(DmShared.vNull(detail_Mdl.getQuantity()).doubleValue() + DmShared.vNull(detail_Mdl.getFreeQuantityInSaleUnit()).doubleValue()));
                        gstTrans_Mdl.setRecID(mrTemp.getSharedKeyFieldName());
                        gstTrans_Mdl.setRecIdPrefix(mrTemp.getRecIdSaleBill().getPrefix());
                        gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
                        gstTrans_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
                        gstTrans_Mdl.setIsTaxInclusive("1");
                        gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()));
                        gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
                        gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                        gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                        gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                        gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.OUTPUT.toInt()));

                        Gst_Server.saveGstTrans(gstTrans_Mdl, false, cnTemp, this);
                    }
//</editor-fold>
                    /*------------------------------------------------------------------------------------------------------------------------------------*/
                    if (DmShared.vNull(detail_Mdl.getAmount()).doubleValue() != 0) {
                        isHaveValue = true;
                    }

                }

            }

//</editor-fold>
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
                            + "VALUES (:docId,:v_sNo,:itemCode, :hsnCode,:quantity,:roffAmount,:roffAmount,:rate, 1,:roffAmount,:itemGSTNature, :roffAcCode) ";
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

                    if (!isEstimateBill && !isEstimate) {
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
                        gstTrans_Mdl.setRecIdPrefix(mrTemp.getRecIdSaleBill().getPrefix());
                        gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
                        gstTrans_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
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
                rsTemp.close();
                psTemp.close();
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="SAVE in patient service">
            if (!isEstimateBill && !isEstimate) {
                if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                    if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION || mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {

                        cnTemp.setCatalog(STServer.getSsnParentDateBase(this));

                        if (blnAdd) {

                            sqlQuery = "INSERT INTO patientService(docId, v_No, recId, v_Type, v_Date, v_Time, v_Prefix, site_Code, preparedBy, u_EntDt, transferDt,oh_Amt_Net,oh_Amt_Gross,oh_Amt_BAMT,oh_Amt_ROFF, recIdPrefix,regDocId)"
                                    + " VALUES (:docId, :v_No, :recId, :v_Type, :v_Date, :v_Time, :v_Prefix, :site_Code, :preparedBy, :uEntDt, :transferDt, :oh_Amt_Net,:oh_Amt_Gross,:oh_Amt_BAMT,:oh_Amt_ROFF, :recIdPrefix,:regDocId)";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString().replaceFirst("1", "2"));
//                            psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                            psTemp.setString("v_Type", "346");
                            psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdSaleBill().getCounter()).toString());
                            psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                            psTemp.setString("recIdPrefix", mrTemp.getRecIdSaleBill().getPrefix());
                            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                            psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                            psTemp.setString("v_Prefix", voucherPrefix);
                            psTemp.setString("site_Code", mrTemp.getSiteCode_HIS());
                            psTemp.setString("preparedBy", userName);
                            psTemp.setString("transferDt", null);
                            psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                            psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                            psTemp.setString("oh_Amt_BAMT", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                            psTemp.setString("oh_Amt_ROFF", "0.00");
                            psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocid()));

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
                            psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                            psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                            psTemp.setString("oh_Amt_BAMT", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
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
                                + " discountPer, oh_Amt_Gross,oh_Amt_Net, referenceValue, remark, v_Type, v_Date, regDocId, site_Code, "
                                + " discountReason, discountGivenBy,collectionBoyCode,roomCode,isOutPharmacy,v_Time) "
                                + "VALUES (:docId, :vSNo, :service, :quantity, :rate, :grossAmount, :discount, :discountPer,:grossAmount,:grossAmount,:grossAmount, "
                                + ":remark, :vType, :vDate, :regDocId, :siteCode,:discountReason, "
                                + ":discountGiven,:collectionBoyCode,:roomCode,:isOutPharmacy,:v_Time)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString().replaceFirst("1", "2"));
                        psTemp.setString("vSNo", DmShared.iNull(pvc.HIDDEN_SERVICES_START_INDEX).toString());
//                        psTemp.setString("service", DmServer.getCodeViaManualCode("PRM", "ShortName", "patientServiceMast", "Code", cnTemp));
                        psTemp.setString("service", DmShared.foriegnKey(mrTemp.getDefaultPharmacyServiceCode()));
                        psTemp.setString("quantity", DmShared.vNull(mrTemp.getNetAmount()).doubleValue() < 0 ? "-1" : "1");
                        psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTemp.getNetAmount()).doubleValue()), "0.00"));
                        psTemp.setString("grossAmount", DmShared.vNull(mrTemp.getNetAmount()).toString());
                        psTemp.setString("discount", DmShared.vNull("0").toString());
                        psTemp.setString("discountPer", DmServer.vNull("0", "0.00"));
                        psTemp.setString("remark", DmShared.xNull(""));
                        psTemp.setString("discountReason", DmShared.xNull(""));
//                        psTemp.setString("vType", DmShared.xNull(mrTemp.getVoucherTypeCode()));
                        psTemp.setString("vType", "346");
                        psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocid()));
                        psTemp.setString("discountGiven", DmShared.foriegnKey(""));
                        psTemp.setString("collectionBoyCode", DmShared.foriegnKey(""));
                        psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getSiteCode_HIS()));
                        psTemp.setString("roomCode", DmShared.foriegnKey(mrTemp.getRoomCode()));
                        psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("isOutPharmacy", "1");
                        psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                        psTemp.executeUpdate();
                        psTemp.close();

                        cnTemp.setCatalog(DmServer.getSsnDBName(this));
                    }
                }
            }

            //</editor-fold>
            if (isHaveValue && !isEstimateBill && !isEstimate) {
                if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.PAYMENT
                        || mrTemp.getCardPayment().getPaymentSource() == PaymentSource.PARTIAL_PAYMENT) {

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
                        //<editor-fold defaultstate="collapsed" desc="Insert into patientReceipt">
                        /*====================================< Insert into patientReceipt >====================================*/
                        sqlQuery = "Insert into patientReceipt (docId,lmCode, v_Type, v_No,v_Time, recId, recIdPrefix, v_Prefix, v_Date, u_EntDt, preparedBy, site_Code, amount,refDocid,regDocID,regDocId_HIS) "
                                + " values (:docId,:lmCode, :v_Type, :v_No, :v_Time, :recId, :recIdPrefix, :v_Prefix, :v_Date, :uEntDt, :userName, :site_Code, :amount,:refDocid,:regDocID,:regDocId_HIS)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("refDocid", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getCardPayment().getReceiptTypeCode()));
                        psTemp.setString("v_Time", DmServer.vNull(DmServer.getServerDateTime()[1], "0.00"));
                        psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdRecipt().getPrefix()));
                        psTemp.setString("recId", DmShared.xNull(mrTemp.getRecIdRecipt().getRecIdValue()));
                        psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdRecipt().getCounter()).toString());
                        psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("v_Prefix", voucherPrefix);
                        psTemp.setString("site_Code", siteCode);
                        psTemp.setString("userName", userName);
                        psTemp.setString("amount", DmServer.vNull(reciptAmt, "0.00"));

                        //
                        if (DmShared.xNull(mrTemp.getTypeCode()).trim().equalsIgnoreCase(pvc.IPD)) {

                            psTemp.setString("regDocId", DmShared.foriegnKey(""));
                            psTemp.setString("regDocId_HIS", DmShared.foriegnKey(mrTemp.getRegDocid()));

                        } else {
                            psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocid()));
                            psTemp.setString("regDocId_HIS", DmShared.foriegnKey(""));

                        }

                        //
                        psTemp.executeUpdate();
                        psTemp.close();

                        //</editor-fold>
                    }
                }

                if (mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION || mrTemp.getCardPayment().getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                    cnTemp.setCatalog(STServer.getSsnParentDateBase(this));

                    CardPaymentPopup_Server.save(true, mrTemp.getCardPayment(), mrTemp.getRegDocid(), mrTemp.getSharedKeyFieldCode().toString().replaceFirst("1", "2"), mrTemp.getSharedKeyFieldName(), mrTemp.getVoucherTypeCode(), siteCode, mrTemp.getVoucherDate(), DmServer.getSsnUserName(this), "", cnTemp);

                    cnTemp.setCatalog(DmServer.getSsnDBName(this));
                } else {
                    CardPaymentPopup_Server.save(true, mrTemp.getCardPayment(), mrTemp.getRegDocid(), mrTemp.getSharedKeyFieldCode(), mrTemp.getSharedKeyFieldName(), mrTemp.getVoucherTypeCode(), siteCode, mrTemp.getVoucherDate(), DmServer.getSsnUserName(this), mrTemp.getSharedKeyFieldCode(), cnTemp);
                }

                /*====================================< Payment Save Case >====================================*/
            }

            if (!isEstimate) {
                manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), false, cnTemp);

                if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                    sqlQuery = "UPDATE saleBill "
                            + " SET u_EntDt_LatestLine = :uEntDt "
                            + " WHERE docId = :estimateDocId";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("estimateDocId", DmShared.foriegnKey(mrTemp.getDocIdEstimate()));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getVoucherTypeCode(), this);

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
    public DmResult<String> deleteRecord(String voucherCategory, String keyCode, ModifyReason_Mdl reason_Mdl, String regDocid) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);
        SaleBill_Mdl mrTemp;
        boolean isEstimateBill = false, isEstimate = false;
        String regDocid_HIS = "";
        try {
            isEstimateBill = voucherCategory.equalsIgnoreCase(pvc.PHARMACY_ESTIMATE_BILL);
            isEstimate = voucherCategory.equalsIgnoreCase(pvc.PHARMACY_ESTIMATE);

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

            /*================< Check whether Bill has been Acknowledged by Nusring Staion against requisition >================*/
            sqlQuery = "select rqd.DocID "
                    + " 	from saleBillDetail sbd "
                    + " 	left join requisitionDetail rqd on sbd.requisitionDocId =rqd.docID and sbd.requisitionV_SNo=rqd.v_SNo "
                    + " 	where rqd.receivedStatus=1 and sbd.docid=:docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("DocID")).intValue() == 1) {
                    throw new DmException(0, "Bill has been acknowledged by nursing station. You can not delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (isEstimate) {
                isEstimateBillCreated(keyCode, false, cnTemp);
            }

            if (!isEstimateBill && !isEstimate) {
                //<editor-fold defaultstate="collapsed" desc="Approval Check">
                if (DmShared.iNull(regDocid).longValue() > 0) {
                    mrTemp = new SaleBill_Mdl();
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

                CardPaymentPopup_Server.cancel(keyCode, cnTemp);
            }

            if (!isEstimate) {
                Stock_Server.deleteStock(keyCode, cnTemp);

                manageRequisitionStatus(keyCode, true, cnTemp);
            }

            if (!isEstimateBill && !isEstimate) {
                Gst_Server.deleteGstTrans(keyCode, cnTemp);
            }

            if (!isEstimate) {
                if (!isEstimateBill) {
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
                }

                sqlQuery = "update saleBill set isCancelled = 1,estimateDocId = null Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode);
                psTemp.executeUpdate();

                if (reason_Mdl != null) {
                    ModifyReason_Server.save(reason_Mdl, keyCode, DmServer.getSsnUserName(this), cnTemp);
                }
            } else {
                sqlQuery = "delete from saleBillDetail Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode);
                psTemp.executeUpdate();

                sqlQuery = "delete from saleBill Where DocId= :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", keyCode);
                psTemp.executeUpdate();

            }

            //
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

            //
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

    private void manageRequisitionStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, rqDocIds = "";
        double reqQty, issueQty, billQty;
        Integer isStatus;

        try {

            sqlQuery = "select	req.docId as rqDocId, reqd.v_SNo AS rqV_SNo, mbd.Qty as billQty, reqd.reqQty as reqQty, reqd.issueQty, reqd.isStatus, req.approvedBy,  "
                    + "        	req.recId as reqNo, vt.short_name as reqType, reqd.itemCode as medicineCode,reqd.v_SNo  "
                    + "        	from requisitionDetail reqd  "
                    + "        	left join requisition req on req.docId = reqd.docId  "
                    + "        	left join voucher_type vt on vt.v_type = req.v_Type  "
                    + "        	inner join (  "
                    + "        			select	mbd.requisitionDocId, mbd.requisitionV_SNo, SUM(mbd.skuQuantity) as Qty  "
                    + "        			from	saleBillDetail mbd  "
                    + "        			where mbd.docId = :docId  "
                    + "        			group by mbd.requisitionDocId, mbd.requisitionV_SNo  "
                    + "        	) mbd on reqd.docId = mbd.requisitionDocId and reqd.v_SNo = mbd.requisitionV_SNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition is Cancelled");
                }
                reqQty = DmShared.vNull(rsTemp.getString("reqQty")).doubleValue();
                issueQty = DmShared.vNull(rsTemp.getString("issueQty")).doubleValue();
                billQty = DmShared.vNull(rsTemp.getString("billQty")).doubleValue();

                if (isDelete) {
                    issueQty -= billQty;
                } else {
                    issueQty += billQty;
                }

                if (issueQty > reqQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition overflows by " + DmServer.vNull(issueQty - reqQty));
                } else if (issueQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition overflows by " + DmServer.vNull(issueQty));
                } else if (issueQty == reqQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (!rqDocIds.equalsIgnoreCase("")) {
                    rqDocIds += ",";
                }
                rqDocIds += DmShared.iNull(rsTemp.getString("rqDocId")).toString();

                sqlQuery = "update requisitionDetail set "
                        + " isStatus = :isStatus,"
                        + " issueQty = :issueQty "
                        + " where docId = :docId and v_SNo = :v_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("rqDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("issueQty", DmServer.vNull(issueQty, "0.0000000"));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!rqDocIds.equalsIgnoreCase("")) {
                sqlQuery = " update requisition set u_EntDt_LatestLine = :uEntDt where docId in (" + rqDocIds + ") ";
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
    public DmResultPageing<List<Find_Mdl>> getFind(String voucherCategory, UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition, mainBillJoin = "", estimateStatus = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (voucherCategory.trim().equalsIgnoreCase(pvc.PHARMACY_ESTIMATE)) {
                mainBillJoin = "    LEFT JOIN saleBill sbs ON sbs.estimateDocId = mb.docID\n ";
                estimateStatus = ",case when sbs.docid is not null then 'Billed' else '' end as status\n ";
            } else {
                mainBillJoin = " ";
                estimateStatus = ",CASE WHEN mb.isCancelled = 1 THEN 'Discarded' ELSE '' END as status \n ";
            }

            if (!STServer.getSsnDepartmentCodeList(this).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":subCode", STServer.getSsnDepartmentCodeList(this)) + ")";

            }

            sqlQuery = "SELECT mb.DocId, mb.recId AS billNo,sbe.recID as estimateNo,\n"
                    + "    mb.v_Date, pr.recId AS regNo, pu.recId AS UHIDNo,\n"
                    + "    puvt.short_Name, mb.v_no, mb.recId, \n"
                    + "    sdm.name AS departmentName, lm.displayName as patientName, \n"
                    + "    sgcon.name AS consultantName \n"
                    + "    " + estimateStatus
                    + "    ,CAST(mb.preparedDate AS DATE) as preparedDate, \n"
                    + "    CAST(REPLACE(LEFT(CONVERT(varchar, mb.preparedDate, 8),5),':','.') AS NUMERIC(18,2)) as preparedTime,mb.preparedBy \n"
                    + "    FROM saleBill mb \n"
                    + "    LEFT JOIN voucher_type AS pbvt ON mb.v_Type = pbvt.v_Type \n"
                    + "    LEFT JOIN saleBill sbe ON mb.estimateDocId = sbe.docID \n"
                    + mainBillJoin
                    + "    LEFT JOIN patientregistration pr ON mb.regDocid = pr.regDocid \n"
                    + "    LEFT JOIN voucher_type AS prvt ON pr.v_Type = prvt.v_Type \n"
                    + "    LEFT JOIN patientuhid pu ON pr.UHID = pu.docId \n"
                    + "    LEFT JOIN ledgerMast lm ON lm.code = mb.lmCode \n"
                    + "    LEFT JOIN voucher_type AS puvt ON pu.v_Type = puvt.v_Type \n"
                    + "    LEFT JOIN subgroup sdm ON mb.departmentCode = sdm.subcode \n"
                    + "    LEFT JOIN subGroup AS sgcon ON mb.consultantCode  = sgcon.subCode    and sgcon.filterNature=15 \n"
                    + "    left join departmentMast dm on dm.subCode= mb.departmentCode"
                    + " WHERE mb.site_Code =:siteCode   \n"
                    + " AND pbvt.category IN (" + getVoucherCategory(voucherCategory) + ")\n"
                    + " AND mb.v_prefix =:vPrefix  " + condition;

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            if (!STServer.getSsnDepartmentCodeList(this).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("subCode", DmShared.xNull(STServer.getSsnDepartmentCodeList(this)));
            }
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
                listNode.setEstimateNo(DmShared.xNull(rsTemp.getString("estimateNo")));
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
            String userName = DmServer.getSsnUserName(this);
            if (!DmServer.getUserVoucherTypes(userPermission).equalsIgnoreCase("")) {
                condition = " and vt.v_Type in (" + DmServer.getUserVoucherTypes(userPermission) + ")";
            } else {
                condition = " and vt.Category in( " + getVoucherCategory(voucherCategory) + ") ";
            }
            sqlWithParam.setSQL(" SELECT VT.V_Type, VT.Description, vt.documentNatureCode, vt.documentTypeCode,vt.short_Name,vt.category ,vt.paymentSource \n"
                    + "from voucher_Type vt \n"
                    + "where vt.isActive = 1 " + condition);
//            sqlWithParam.setSQL(" SELECT VT.V_Type, VT.Description, vt.documentNatureCode, vt.documentTypeCode,vt.short_Name,vt.category \n"
//                    + "from voucher_Type vt \n"
//                    + "where vt.category IN (" + getVoucherCategory(voucherCategory) + ") \n"
//                    + "and vt.isActive = 1 " + condition);

//            if ((userPemissionSName.equalsIgnoreCase("")) || userName.equalsIgnoreCase("SA") || userName.equalsIgnoreCase("SUPER")) {
//                sqlWithParam.setSQL("SELECT VT.V_Type, VT.Description, vt.documentNatureCode, vt.documentTypeCode,vt.short_Name "
//                        + "FROM Voucher_Type VT  "
//                        + "WHERE VT.category IN (" + getVoucherCategory(voucherCategory) + ") AND VT.isActive = 1  ");
//            } else {
//                sqlWithParam.setSQL("SELECT VT.V_Type, VT.Description, vt.documentNatureCode, vt.documentTypeCode,vt.short_Name \n"
//                        + "FROM "+dbCompany.trim()+".dbo.user_Voucher_Permission uvp  \n"
//                        + "left join voucher_Type VT on vt.short_name  = uvp.vTypeSName \n"
//                        + "WHERE VT.Short_Name IN ('" + userPemissionSName + "') AND VT.isActive = 1 "
//                        + "and uvp.user_Name = '" + DmServer.getSsnUserName(this) + "' ");
//            }
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
    public DmResult<SaleBill_Mdl> getUHIDDetails(SearchUHID_Mdl searchMdl, boolean linkParentDatabase) {
        DmResult<SaleBill_Mdl> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Connection cnTemp = null;
        String sqlQuery = "", conditionIn = "", conditionOut = "", category = "";
        SaleBill_Mdl mrTemp = new SaleBill_Mdl();
        dmrTemp.setData(mrTemp);

        try {
//            if (DmShared.xNull(searchMdl.getVoucherCategory()).trim().equalsIgnoreCase("")) {
//                category = getVoucherCategory(pvc.OPD) + "," + getVoucherCategory(pvc.EMERGENCY_OPD) + "," + getVoucherCategory(pvc.IPD) + "," + getVoucherCategory(pvc.EMERGENCY) + "," + getVoucherCategory(pvc.DAY_CARE);
//            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.OPD)) {
//                category = getVoucherCategory(pvc.OPD) + "," + getVoucherCategory(pvc.EMERGENCY_OPD);
//            } else {
//                category = getVoucherCategory(searchMdl.getVoucherCategory());
//            }

            mrTemp.setLinkParentDatabase(linkParentDatabase);
            if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.IPD)) {
                category = "'" + pvc.IPD + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.OPD)) {
                category = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.DAY_CARE)) {
                category = "'" + pvc.DAY_CARE + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.EMERGENCY)) {
                category = "'" + pvc.EMERGENCY + "'";
            } else if (DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.UHID)) {
                category = "'" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "','" + pvc.PAY_CLINIC + "','" + pvc.IPD + "','" + pvc.LAB + "'";
            }

            if (DmShared.xNull(category).trim().equalsIgnoreCase("")) {
                category = "'" + category + "'";
            } else if (!DmShared.xNull(searchMdl.getVoucherCategory()).equalsIgnoreCase(pvc.UHID)) {
                conditionOut += "  AND (pr.regDocId IS NOT NULL) ";
            }

            if (linkParentDatabase) {
                cnTemp = DmServer.getConnection(STServer.getSsnParentDateBase(this));
            } else {
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            }

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

            if (DmShared.iNull(searchMdl.getDocIdRequisition()).longValue() == 0 && DmShared.iNull(searchMdl.getDocIdEstimate()).longValue() == 0) {
                sqlQuery = "select pu.docId, pr.regDocId, pr.category,pr.patientClass,pr.pakAlloted , pr.siteCode "
                        + "	from patientUHID pu "
                        + "	left join ( "
                        + "                 select top 1 pr.regDocId, pr.uhid, vt.category,h7m.name as patientClass"
                        + "                 , case when isnull(pak.packageCount,0)>0 then 'Package Alloted' else '' end as pakAlloted , pr.site_Code as siteCode "
                        + "                     from patientRegistration pr"
                        + "			left join patientRegistration2 pr2 on pr.regDocID=pr2.regDocID"
                        + "			left join hl7mast h7m on pr2.vipTypeCode=h7m.code "
                        + "			left join voucher_Type vt on vt.v_Type = pr.v_Type"
                        + "			left join patientUHID pu on pr.uhid = pu.docId"
                        + "                     LEFT JOIN (  "
                        + "                                select count(packageCode) as packageCount,regDocID from patientPackageAllocation group by regDocID "
                        + "                                ) As pak on pr.regDocID=pak.regDocID "
                        + "			where  pr.isCancelled = 0 and vt.category  in (" + category + ") " + conditionIn
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
                    mrTemp.setPatientClassType(DmShared.xNull(rsTemp.getString("patientClass")));
                    mrTemp.setPatientPackagedInfo(DmShared.xNull(rsTemp.getString("pakAlloted")));
                    mrTemp.setSiteCode_HIS(DmShared.xNull(rsTemp.getString("siteCode")));
                } else {
                    throw new DmException(0, "No record found. Try Again.");
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getRegDocid()).longValue() == 0 && DmShared.iNull(mrTemp.getUhidDocid()).longValue() == 0) {
                    throw new DmException(0, "No record found. Try Again.");
                }
            } else if (DmShared.iNull(searchMdl.getDocIdRequisition()).longValue() != 0 && DmShared.iNull(searchMdl.getDocIdEstimate()).longValue() == 0) {
                sqlQuery = "select mr.docId, mr.regDocId "
                        + "	from requisition mr"
                        + "	left join voucher_Type vt on vt.v_Type = mr.v_Type"
                        + "	left Join ("
                        + "				select rd.docId "
                        + "					from requisitionDetail rd "
                        + "                    where rd.isStatus = 0 and ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0"
                        + "                    group by rd.docId "
                        + "            ) mrd on mrd.docId = mr.docid "
                        + "	WHERE vt.category in ( " + getVoucherCategory(pvc.PHARMACY_REQUISITION) + "," + getVoucherCategory(pvc.NURSING_MEDICINE_REQUISTION) + ")   "
                        + "         and mrd.docId is not null and isnull(mr.approvedBy,'') <> '' and mr.v_Date <= :vDate "
                        + "         and mr.docId = :docId "
                        + "	order by mr.v_Date desc, vt.short_Name, mr.v_No desc, mr.recId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(searchMdl.getDocIdRequisition()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(searchMdl.getVoucherDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setDocIdRequisition(DmShared.xNull(rsTemp.getString("docId")));
                    mrTemp.setRegDocid(DmShared.xNull(rsTemp.getString("regDocId")));
                } else {
                    throw new DmException(0, "No record found. Try Again.");
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() == 0) {
                    throw new DmException(0, "No record found. Try Again.");
                }
            } else if (DmShared.iNull(searchMdl.getDocIdRequisition()).longValue() == 0 && DmShared.iNull(searchMdl.getDocIdEstimate()).longValue() != 0) {

                sqlQuery = " select est.docId, est.regDocId, isnUll(est.uhid,lm.code) uhid "
                        + " ,est.consultantCode,sgc.name as consultantName,est.remark,est.siteCode_HIS "
                        + "	from saleBill est  "
                        + "	left join voucher_Type vt on vt.v_Type = est.v_Type  "
                        + "	left join ledgerMast lm on lm.code = est.lmCode "
                        + "	left join subGroup dep on dep.subCode = est.departmentCode  "
                        + "	left join subGroup sgc on est.consultantCode = sgc.subCode "
                        + "	left join ( "
                        + "                 select sb.estimateDocId  "
                        + "                     from saleBill sb  "
                        + "			left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                        + "			where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ") and sb.isCancelled = 0  "
                        + "		) sb on sb.estimateDocId = est.docId  "
                        + "	where est.docId = :docId "
                        + "     and est.v_Date <= :vDate "
                        + "     and sb.estimateDocId is null ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(searchMdl.getDocIdEstimate()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(searchMdl.getVoucherDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setDocIdEstimate(DmShared.xNull(rsTemp.getString("docId")));
                    mrTemp.setRegDocid(DmShared.xNull(rsTemp.getString("regDocId")));
                    mrTemp.setUhidDocid(DmShared.xNull(rsTemp.getString("uhid")));
                    mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                    mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultantName")));
                    mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                    mrTemp.setSiteCode_HIS(DmShared.xNull(rsTemp.getString("siteCode_HIS")));
                } else {
                    throw new DmException(0, "No record found. Try Again.");
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() == 0) {
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

    private void getPatientDetail(SaleBill_Mdl mrTemp, SearchUHID_Mdl searchMdl, boolean isFromMoveRec, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Organization_Mdl organizationMdl;
        String sqlQuery;
        Connection cnTemp1 = null;

        try {
            if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0 || (DmShared.iNull(mrTemp.getUhidDocid()).longValue() > 0 && DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() == 0)) {
                sqlQuery = " SELECT "
                        + "   lm.code as lmCode "
                        + " , lm.subCode as lmSubCode "
                        + " , lm.name as lmName "
                        + " , lm.manualCode as lmManualCode "
                        + " , pr.regDocId "
                        + " , pr.recId AS regNo"
                        + " , pr.category"
                        + " , pu.docId"
                        + " , pu.recId AS uhidNo"
                        + " , pu.patientName as patientName"
                        + " , pr.v_Date"
                        + " , pr.site_Code"
                        + " , CONCAT(pu.address1,' ',pu.address2) as address"
                        + " , pu.cityCode"
                        + " , pu.mobile"
                        + " , c.cityName"
                        + " , pu.idNo"
                        + " , pu2.idTypeCode"
                        + " , idType.name as idTypeName"
                        + " , pr.consultantCode"
                        + " , sg.name AS consultant"
                        + " , sgo.name organization"
                        + " , rlm.medicineSaleOn"
                        + " , om.isTPA "
                        + " , om.shortName "
                        + " , cem.empName "
                        + " , pr.roomCode ";
                if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() > 0) {
                    sqlQuery += " , vr.docId docIdReq"
                            + " , vr.recId reqNo"
                            + " , sgrq.name as reqDepartmentName "
                            + " , vr.departmentToCode as reqDepartmentCode ";
                } else if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                    sqlQuery += " , vr.docId docIdReq"
                            + " , vr.recId reqNo";
                }
                if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                    sqlQuery += " , est.docId docIdEst"
                            + " , est.recId estNo";
                }
                sqlQuery += " , CASE "
                        + "     pr.category "
                        + "     WHEN " + getVoucherCategory(pvc.DAY_CARE) + " THEN 'Day Care' "
                        + "     WHEN " + getVoucherCategory(pvc.EMERGENCY) + " THEN 'Emergency'  "
                        + "   ELSE "
                        + "     pr.category "
                        + "   END regType"
                        + " , rlm.pharmacyDiscountPer"
                        + " , rlm.isPharmacyDiscount "
                        + " , CASE when pr.regDocId is null then pu.corporateEmployeeCode else pr.corporateEmployeeCode end corporateEmployeeCode "
                        + " , CASE when pr.regDocId is null then pu.organizationCode else pr.organizationCode end organizationCode"
                        + " , CASE when pr.regDocId is null then pu.orgPolicyNo else pr.orgPolicyNo end orgPolicyNo"
                        + " , CASE when pr.regDocId is null then pu.rankNo else pr.rankNo end rankNo"
                        + " , CASE when pr.regDocId is null then pu.orgHolderName else pr.orgHolderName end orgHolderName"
                        + " , CASE when pr.regDocId is null then pu.orgRelationShip else pr.orgRelationShip end orgRelationShip"
                        + " , pp.prescDocId"
                        + "   FROM patientUHID AS pu "
                        + "   LEFT JOIN patientUHID2 AS pu2 ON pu2.uhid = pu.docId "
                        + "   LEFT JOIN hl7Mast as idType on idType.code = pu2.idTypeCode "
                        + "   LEFT JOIN ( "
                        + "             select "
                        + "       top 1 pr.regDocId"
                        + "     , pr.uhid"
                        + "     , vt.category"
                        + "     , pr.consultantCode"
                        + "     , pr.organizationCode"
                        + "     , pr.corporateEmployeeCode"
                        + "     , pr.recId"
                        + "     , pr.v_Date"
                        + "     , pr.orgPolicyNo"
                        + "     , pr.rankNo"
                        + "     , pr.orgHolderName"
                        + "     , pr.orgRelationShip"
                        + "     , pr.site_Code "
                        + "     , pr.roomCode "
                        + "	from patientRegistration pr "
                        + "	LEFT JOIN voucher_Type vt on vt.v_Type = pr.v_Type "
                        + "	WHERE pr.regDocId = :regDocId "
                        + " ) pr ON pu.docId = pr.uhid "
                        + "	LEFT JOIN subGroup as sg ON pr.consultantCode = sg.subCode "
                        + "	LEFT JOIN organizationMast om ON om.subCode = (case when pr.regDocId is null then pu.organizationCode else pr.organizationCode end) "
                        + "     Left JOin rateListMast rlm on om.rateListcode=rlm.code "
                        + "	LEFT JOIN subGroup as sgo ON om.subCode = sgo.subCode "
                        + "	LEFT JOIN corporateEmployeeMast cem ON cem.code = (case when pr.regDocId is null then pu.corporateEmployeeCode else pr.corporateEmployeeCode end) ";
                if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() > 0) {
                    sqlQuery += " LEFT JOIN requisition vr ON vr.regDocId = pr.regDocId "
                            + " LEFT JOIN subGroup sgrq ON sgrq.subCode= vr.departmentToCode ";
                } else if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                    sqlQuery += "     LEFT JOIN (Select r.regDocId,max(r.docId) as docId,max(r.recId) as recId from requisition r where r.regDocId=:regDocId group By r.regDocId) vr ON vr.regDocId = pr.regDocId   ";
                }
                sqlQuery += "     LEFT JOIN city c ON c.cityCode = pu.cityCode "
                        + "     LEFT JOIN ledgerMast lm ON lm.regDocId = pr.regDocId ";
                if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                    sqlQuery += "     LEFT JOIN saleBill est ON (case when pr.regDocId is null then est.uhid else est.regDocId end) = (case when pr.regDocId is null then pu.docId else pr.regDocId end) ";
                }
                sqlQuery += "     LEFT JOIN (select top 1 pp.regDocId,pp.docId as prescDocId  "
                        + "			from " + STServer.getSsnParentDateBase(this) + ".dbo.patientPrescription pp  "
                        + "			where pp.regDocId = :regDocId  "
                        + "			order by pp.v_Date desc, pp.v_Time desc  "
                        + "         ) as pp on pp.regDocId = pr.regDocId "
                        + "	where pu.isCancelled = 0 ";
//                sqlQuery += " and pr.regDocID =  :regDocId ";
                if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                    sqlQuery += " and pr.regDocID =  :regDocId ";
                }
                if (DmShared.iNull(mrTemp.getUhidDocid()).longValue() > 0) {
                    sqlQuery += " and pu.docId =  :uhid ";
                }
                if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() > 0) {
                    sqlQuery += " and vr.docId =  :requisitionDocId ";
                }
                if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                    sqlQuery += " and est.docId =  :estimateDocId ";
                }
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                if (DmShared.iNull(mrTemp.getUhidDocid()).longValue() > 0) {
                    psTemp.setString("uhid", DmShared.iNull(mrTemp.getUhidDocid()).toString());
                }
                psTemp.setString("regDocId", DmShared.iNull(mrTemp.getRegDocid()).toString());
                if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() > 0) {
                    psTemp.setString("requisitionDocId", DmShared.iNull(mrTemp.getDocIdRequisition()).toString());
                }
                if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                    psTemp.setString("estimateDocId", DmShared.iNull(mrTemp.getDocIdEstimate()).toString());
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
                    organizationMdl.setPharmacyDiscountPer(DmServer.vNull(rsTemp.getString("pharmacyDiscountPer")));
                    organizationMdl.setTpa(DmShared.iNull(rsTemp.getString("isTPA")).intValue() == 1);
                    organizationMdl.setPharmacyDiscount(DmShared.iNull(rsTemp.getString("isPharmacyDiscount")).intValue() == 1);
                    mrTemp.setOrganizationMdl(organizationMdl);
                    mrTemp.setSaleMedicine(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));

                    if (!isFromMoveRec) {
                        mrTemp.setRegDocid(DmShared.iNull(mrTemp.getRegDocid()).toString());
                        mrTemp.setRegSiteCode(DmShared.iNull(rsTemp.getString("site_Code")).toString());
                        mrTemp.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));
                        mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                        mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo")));
                        mrTemp.setRegRecID(DmShared.xNull(rsTemp.getString("regNo")));

                        if (mrTemp.isLinkParentDatabase()) {
                            Connection cnTempJindalPharmacy = DmServer.getConnection(DmServer.getSsnDBName(this));
                            mrTemp.setConsultantCode(DmShared.xNull(DmServer.executeScalar(" select subcode from subgroup where name='"+DmShared.xNull(rsTemp.getString("consultant"))+"'", cnTempJindalPharmacy)));
                            mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultant")));
                            cnTempJindalPharmacy.close();
                        } else {
                            mrTemp.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                            mrTemp.setConsultantName(DmShared.xNull(rsTemp.getString("consultant")));
                        }

                        mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                        mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("lmSubCode")));
                        mrTemp.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                        mrTemp.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                        mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                        mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                        mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                        mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                        mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                        mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                        mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                        if (DmShared.iNull(mrTemp.getRegDocid()).longValue() > 0) {
                            if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() > 0) {
                                mrTemp.setRecIdRequisition(DmShared.xNull(rsTemp.getString("reqNo")));
                                mrTemp.setDocIdRequisition(DmShared.xNull(rsTemp.getString("docIdReq")));
                                mrTemp.setDepartmentCode(DmShared.xNull(rsTemp.getString("reqDepartmentCode")));
                                mrTemp.setDepartmentName(DmShared.xNull(rsTemp.getString("reqDepartmentName")));
                            }

                            if (DmShared.iNull(rsTemp.getString("docIdReq")).longValue() > 0) {
                                mrTemp.setPatientsReqExists(true);
                            } else {
                                mrTemp.setPatientsReqExists(false);
                            }
                        }
                        if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                            mrTemp.setRecIdEstimate(DmShared.xNull(rsTemp.getString("estNo")));
                            mrTemp.setDocIdEstimate(DmShared.xNull(rsTemp.getString("docIdEst")));
                        }

                        mrTemp.setSaleMedicine(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));
                        mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("category")));
                        mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("regType")));
                        mrTemp.setUhidDocid(DmShared.iNull(rsTemp.getString("docId")).toString());
                        if (searchMdl.getVoucherCategory().equalsIgnoreCase(pvc.OPD) || searchMdl.getVoucherCategory().equalsIgnoreCase(pvc.DAY_CARE) || searchMdl.getVoucherCategory().equalsIgnoreCase(pvc.UHID)) {
                            mrTemp.getGrdMediDetails().addAll(getPrescription(DmShared.xNull(rsTemp.getString("prescDocId")), cnTemp));
                        }
                    }

                }
                rsTemp.close();
                psTemp.close();
            } else if (DmShared.iNull(mrTemp.getDocIdRequisition()).longValue() > 0) {
                sqlQuery = "select mr.docId, mr.departmentToCode, mr.requestedByCode, reqBy.name requisitionBy, mr.recId "
                        + "	from requisition mr "
                        + "	left join subGroup reqBy on reqBy.subCode = mr.requestedByCode "
                        + "     left join voucher_Type vt on vt.v_Type = mr.v_Type "
                        + "	left Join ( "
                        + "                 select rd.docId "
                        + "                     from requisitionDetail rd "
                        + "			where rd.isStatus = 0 and ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 "
                        + "			group by rd.docId "
                        + "         ) mrd on mrd.docId = mr.docid "
                        + "	where mrd.docId is not null "
                        + "     and mr.docId = :requisitionDocId "
                        + "     and mr.site_Code_To IN (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ")  "
                        + "     and mr.v_Date <= :v_Date "
                        + "     and vt.category in (" + getVoucherCategory(pvc.PHARMACY_REQUISITION) + "," + getVoucherCategory(pvc.NURSING_MEDICINE_REQUISTION) + ") and mr.regDocId is null";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("requisitionDocId", DmShared.iNull(mrTemp.getDocIdRequisition()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(searchMdl.getVoucherDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("requisitionBy")));
                    mrTemp.setDocIdRequisition(DmShared.xNull(rsTemp.getString("docId")));
                    mrTemp.setRecIdRequisition(DmShared.xNull(rsTemp.getString("recId")));
                }
                rsTemp.close();
                psTemp.close();
            } else if (DmShared.iNull(mrTemp.getDocIdEstimate()).longValue() > 0) {
                cnTemp1 = DmServer.getConnection(STServer.getSsnParentDateBase(this));
                String organizationCode = DmServer.executeScalar("select organizationCode  from patientUHID where docid = " + DmShared.iNull(mrTemp.getUhidDocid()).toString() + "", cnTemp1);
                cnTemp1.close();
                sqlQuery = " select est.docId, lm.displayName as patientName,rlm.medicineSaleOn "
                        + "         ,est.partyType,est.mobile,est.lmCode, est.idTypeCode, est.idNo"
                        + "         , idType.name idType,est.subCode,lm.address,c.cityName,lm.cityCode"
                        + "         ,est.placeOfsupply as stateCode,sm.name as stateName,lm.name as lmName,lm.manualCode as lmManualCode,lm.mobile "
                        + "	from saleBill est  "
                        + "	LEFT join voucher_Type vt on vt.v_Type = est.v_Type  "
                        + "	LEFT join ledgerMast lm on lm.code = est.lmCode  "
                        + "     LEFT JOIN city AS c ON c.citycode = lm.citycode "
                        + "     LEFT JOIN stateMast AS sm ON sm.code = est.placeOfsupply "
                        + "	LEFT JOIN hl7Mast as idType on idType.code = est.idTypeCode "
                        + "	LEFT JOIN organizationMast om ON om.subCode = " + organizationCode + " \n"
                        + "     Left JOin rateListMast rlm on om.rateListcode=rlm.code  "
                        + "	LEFT JOIN ( "
                        + "                 select sb.estimateDocId  "
                        + "                     from saleBill sb  "
                        + "			left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                        + "			where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ") and sb.isCancelled = 0  "
                        + "		) sb on sb.estimateDocId = est.docId  "
                        + "	where est.docId = :docId "
                        + "     and est.v_Date <= :v_Date "
                        + "     and sb.estimateDocId is null "
                        + "     and est.regDocId is null";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getDocIdEstimate()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(searchMdl.getVoucherDate()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {

                    mrTemp.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                    mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("stateCode")));
                    mrTemp.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                    mrTemp.setAddress(DmShared.xNull(rsTemp.getString("address")));
                    mrTemp.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                    mrTemp.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                    mrTemp.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                    mrTemp.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                    mrTemp.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                    mrTemp.setIdTypeName(DmShared.xNull(rsTemp.getString("idType")));
                    mrTemp.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                    mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                    mrTemp.setSaleMedicine(MedicineSaleOn.getKeyOf(DmShared.iNull(rsTemp.getString("medicineSaleOn")).intValue()));

                    if (searchMdl.isCallByEstimate()) {
//                        mrTemp.setTypeCode("UHID");
//                        mrTemp.setTypeName("UHID");
                        mrTemp.setTypeCode(DmShared.xNull(rsTemp.getString("lmManualCode")).contains(":I") ? "IPD" : "UHID");
                        mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("lmManualCode")).contains(":I") ? "IPD" : "UHID");

                        mrTemp.setUhidDocid("");
                        mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("lmManualCode")));
                        mrTemp.setUhidDocid(DmShared.xNull(rsTemp.getString("lmCode")));
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
    public DmResult<List<GrdMedicine_Mdl>> getPrescMedicineDetail(String opdNo, String consultantCode) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        List<GrdMedicine_Mdl> listNode = new ArrayList<GrdMedicine_Mdl>();
        dmrTemp.setData(listNode);
        String sqlQuery, prescDocid = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            cnTemp = DmServer.getConnection(STServer.getSsnParentDateBase(this));
            sqlQuery = "select docid from patientPrescription pp where pp.regdocid = :opdNo"
                    + " and pp.consultantCode = :consultantCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("opdNo", DmShared.iNull(opdNo).toString());
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                prescDocid = DmShared.xNull(rsTemp.getString("docid"));
            }

            listNode = getPrescription(prescDocid, cnTemp);
            cnTemp.close();
            dmrTemp.setData(listNode);
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
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

    private List<GrdMedicine_Mdl> getPrescription(String prescDocId, Connection cnTemp) {
        List<GrdMedicine_Mdl> lstTemp = new ArrayList<>();
        GrdMedicine_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        TxtHMedicine_Mdl medicineMdl;
        try {
            sqlQuery = " SELECT "
                    + "      pma.docId"
                    + "    , pma.itemCode,pma.qty"
                    + "    , pma.doseText as doseName"
                    + "    , pma.frequencyCode"
                    + "    , fm.frequencyName"
                    + "    , fm.frequencyValue"
                    + "    , pma.days"
                    + "    , pma.durationCode"
                    + "    , pma.doseValue "
                    + "    , ISNULL(ic.allowBifurcation,0) AS allowBifurcation "
                    + "    , im.name  "
                    + "    , im.isActive  "
                    + "    , CASE WHEN isl.site_code IS NULL THEN 'No' ELSE 'Yes' END as siteAllowed "
                    + "    , CASE ISNULL(im.isActive,0) WHEN 0 THEN 'Inactive' WHEN 1 THEN 'Active' WHEN 2 THEN 'Hold' END AS activeText"
                    + "	FROM " + STServer.getSsnParentDateBase(this) + ".dbo.patientPrescriptionMedAdv pma "
                    + "	left join " + STServer.getSsnParentDateBase(this) + ".dbo.frequencyMast fm on fm.code = pma.frequencyCode "
                    + "	left join " + DmServer.getSsnDBName(this) + ".dbo.itemMast im on im.code = pma.itemCode "
                    + "	left join " + DmServer.getSsnDBName(this) + ".dbo.itemCat ic on ic.code = im.itemCategoryCode "
                    + " LEFT JOIN  " + DmServer.getSsnDBName(this) + ".dbo.itemSiteList isl ON isl.code=im.code AND isl.site_Code=:site_code "
                    + "	where pma.docid = :docId "
                    + "	order by im.name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(prescDocId).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setMedicineMdl(medicineMdl);
                node.setDoseName(DmShared.xNull(rsTemp.getString("doseName")));
                node.setMedicineActiveStatusCode(DmShared.xNull(rsTemp.getString("isActive")));
                node.setMedicineActiveStatusName(DmShared.xNull(rsTemp.getString("activeText")));
                node.setQuantity(DmShared.xNull(rsTemp.getString("qty")));

                node.setDoseValue(DmServer.vNull(rsTemp.getString("doseValue")));
                node.setFreqCode(DmShared.xNull(rsTemp.getString("frequencyCode")));
                node.setFreqName(DmShared.xNull(rsTemp.getString("frequencyName")));
                node.setFrequencyValue(DmServer.vNull(rsTemp.getString("frequencyValue")));
                node.setDay(DmShared.xNull(rsTemp.getString("days")));
                node.setDuration(DmShared.xNull(rsTemp.getString("durationCode")));
                node.setAllowBifurcation(DmShared.iNull(rsTemp.getString("allowBifurcation")).intValue() == 1);
                node.setSiteAllowed(DmShared.xNull(rsTemp.getString("siteAllowed")));
                if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("0")) {
                    node.setDurationText("Day");
                } else if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("1")) {
                    node.setDurationText("Day (Next)");
                } else if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("2")) {
                    node.setDurationText("Week");
                } else if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("3")) {
                    node.setDurationText("Week (Next)");
                } else if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("4")) {
                    node.setDurationText("Month");
                } else if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("5")) {
                    node.setDurationText("Month (Next)");
                } else if (DmShared.xNull(rsTemp.getString("durationCode")).equalsIgnoreCase("6")) {
                    node.setDurationText("Dose");
                }
                node.setCombineDuration(node.getDay() + " " + node.getDurationText());
//                node.setReqQty(DmServer.vNull(rsTemp.getString("reqQty")));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
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
        return lstTemp;
    }

    @Override
    public DmResult<Default_Mdl> getDefaultVal(String voucherCategory, UserPermission userPermission) {
        DmResult<Default_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHReceiptType_VList_Mdl> reciptList = new ArrayList<TxtHReceiptType_VList_Mdl>();
        Default_Mdl mrTemp = new Default_Mdl();
        Layout_Mdl layout;
        String condition = "";
        String defaultMode;
        List<String> PayMode = new ArrayList<String>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setCardPayment(new CardPaymentPopup_Mdl());

            mrTemp.setPartyType(DmEnum.DmPartyType.Unregistered.getId());

            if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("Super"))) {
                sqlQuery = "select paymentTypeCode \n"
                        + " from " + DmServer.dbCompany + ".dbo.userPayModePermission upm \n"
                        + "left join voucher_Type as vt on vt.v_Type =  upm.v_Type\n"
                        + "where  isnull(upm.isDefault,0)=1 and  upm.userName = '" + DmServer.getSsnUserName(this) + "' \n"
                        + "And vt.category = '" + pvc.PHARMACY_BILL_RECEIPT + "'   ";
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
                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL_RECEIPT + "' and vt.isActive = 1 ";

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
            mrTemp.getCardPayment().getTxtHReceiptTypeVList().addAll(reciptList);

//            /*======================UserPermissionShortName=====================================*/
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
            if (!DmServer.getUserVoucherTypes(userPermission).equalsIgnoreCase("")) {
                condition = " and vt.v_Type in (" + DmServer.getUserVoucherTypes(userPermission) + ")";
            } else {
                condition = " and vt.Category in( '" + voucherCategory + "') ";
            }
            sqlQuery = " select vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode,vt.Short_Name, vt.category , vt.paymentSource \n"
                    + "from voucher_Type vt \n"
                    + "where vt.isActive = 1 " + condition + " order by vt.description ";
//            sqlQuery = " select vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode,vt.Short_Name, vt.category \n"
//                    + "from voucher_Type vt \n"
//                    + "where vt.category in( '" + voucherCategory + "') \n"
//                    + "and vt.isActive = 1 " + condition + " order by vt.description ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                TxtHVType_Mdl vTypeMdl = new TxtHVType_Mdl();
                vTypeMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                vTypeMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                vTypeMdl.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                vTypeMdl.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                vTypeMdl.setShortName(DmShared.xNull(rsTemp.getString("Short_Name")));
                vTypeMdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
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

            sqlQuery = "SELECT  "
                    + "    env.expiryAlertBeforeDays "
                    + "  , env.medicineSaleOn "
                    + "  , env.alphCase_Patient "
                    + "  , env.alphaCase_Address "
                    + "  , env.defaultPharmacyServiceCode "
                    + "  , env.pharmacyAllowReturnWithoutSale "
                    + "  , env.isIncludeTaxInCost "
                    + "  , env.pharmacyBarcodeActive "
                    + "  ,env.pharmacyRequisitionWithoutUHIDInBill "
                    + "  ,REPLACE(CONVERT(VARCHAR(11), dateadd(d,-env.backDaysForEstimateInSaleBill,GETDATE()), 106), ' ', '-') as backDaysForEstimateInSaleBill"
                    + " ,env.isEstimateMandatoryInSaleBill "
                    + " FROM enviro as env "
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
                mrTemp.setRequisitionWithoutUHID(DmShared.xNull(rsTemp.getString("pharmacyRequisitionWithoutUHIDInBill")));
                mrTemp.setBackDateForEstimateHelpInSaleBill(DmShared.xNull(rsTemp.getString("backDaysForEstimateInSaleBill")));
                if (DmShared.xNull(rsTemp.getString("isIncludeTaxInCost")).trim().equals("")) {
                    throw new DmException(0, "Please set 'Include Tax In Cost' in environment settings.");
                } else {
                    mrTemp.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                }
                mrTemp.setEstimateMandatoryInSaleBill(DmShared.iNull(rsTemp.getString("isEstimateMandatoryInSaleBill")).intValue() == 1);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCustomerType(DmHelper helper, boolean blnToEndOfFile, boolean opdAllow, boolean ipdAllow, boolean dayCareAllow, boolean emergencyAllow, boolean walkInAllow, boolean ledgerAllow, boolean UhidAllow) {
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
            if (UhidAllow) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("UHID");
                listNode.setSharedKeyFieldName("UHID");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRemark(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Routine");
            listNode.setSharedKeyFieldName("Routine");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Discharge");
            listNode.setSharedKeyFieldName("Discharge");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("LP");
            listNode.setSharedKeyFieldName("LP");
            lstTemp.add(listNode);

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

    @Override
    public DmResult<List<GrdMedicine_Mdl>> getMedicineQty(List<GrdMedicine_Mdl> list, String totalDays, String vDate, String departmentCode, String pharmacyDiscountPer, MedicineSaleOn saleOn, boolean isPharmacyDiscount) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        List<GrdMedicine_Mdl> listTemp = new ArrayList<>();
        GrdMedicine_Mdl node;
        Connection cnTemp = null;
        String sqlQuery = "", query = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String location = "";
        TxtHMedicine_Mdl medicineMdl = null;
        double tempQty = 0;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            for (GrdMedicine_Mdl grdMediDetails_Mdl : list) {
                medicineMdl = grdMediDetails_Mdl.getMedicineMdl();
                if (!query.trim().equals("")) {
                    query += " UNION ALL ";
                }
                if (grdMediDetails_Mdl.isAllowBifurcation()) {
                    if (DmShared.vNull(grdMediDetails_Mdl.getDay()).doubleValue() <= DmShared.vNull(totalDays).doubleValue()) {
//                        query += "select '" + DmShared.xNull(medicineMdl.getSharedKeyFieldCode()) + "' as mediCode, "
//                                + " " + DmShared.vNull(grdMediDetails_Mdl.getDoseValue()) * DmShared.vNull(grdMediDetails_Mdl.getDay()) + " as reqQty ";
                        query += "select '" + DmShared.xNull(medicineMdl.getSharedKeyFieldCode()) + "' as mediCode, "
                                + " " + DmShared.vNull(grdMediDetails_Mdl.getDoseValue()) * DmShared.vNull(grdMediDetails_Mdl.getFrequencyValue()) * DmShared.vNull(grdMediDetails_Mdl.getDay()) + " as reqQty ";
                    } else {
//                        query += "select '" + DmShared.xNull(medicineMdl.getSharedKeyFieldCode()) + "' as mediCode, "
//                                + " " + DmShared.vNull(grdMediDetails_Mdl.getDoseValue()) * DmShared.vNull(totalDays) + " as reqQty ";
                        query += "select '" + DmShared.xNull(medicineMdl.getSharedKeyFieldCode()) + "' as mediCode, "
                                + " " + DmShared.vNull(grdMediDetails_Mdl.getDoseValue()) * DmShared.vNull(grdMediDetails_Mdl.getFrequencyValue()) * DmShared.vNull(totalDays) + " as reqQty ";
                    }
                } else {
                    query += "select '" + DmShared.xNull(medicineMdl.getSharedKeyFieldCode()) + "' as mediCode, "
                            + DmServer.vNull(grdMediDetails_Mdl.getQuantity()) + " as reqQty";
                }
            }

            query = " select pre.mediCode, sum(pre.reqQty) as qty "
                    + " FROM ( "
                    + " " + query + ""
                    + " ) pre "
                    + " group by pre.mediCode";

            sqlQuery = " SELECT vPre.mediCode "
                    + "         , vPre.qty "
                    + "		, im.name as mediName "
                    + "         , im.allowNegativeStock "
                    + "         , im.isBarcodeActive "
                    + "         , im.hsnCode "
                    + "         , im.isBestBefore "
                    + "         , im.isReturnableItem "
                    + "         , im.itemGSTNature "
                    + "         , im.itsBasedOn "
                    + "         , im.propertyList "
                    + "         , im.saleUnitConversionRate "
                    + "         , um.unit "
                    + "         , um.decimalPlaces AS skuDecimalPlaces "
                    + "         , ums.decimalPlaces as saleUnitDecimalPlaces "
                    + "         , im.restrictGroup "
                    + "         , im.saleUnit "
                    + "         , ums.unit saleUnitName "
                    + "         , im.sku "
                    + "         , um.unit skuName "
                    + "         , ig.code as groupCode "
                    + "         , ig.name as groupName "
                    + "         , im.isDiscountAllow "
                    + "         , ic.name as itemCategory "
                    + "         , hm.hsn "
                    + "         , im.purchaseUnitConversionRate "
                    + "         , im.saleDiscountPer "
                    + "         , im.isSaleRateEditable  "
                    + "         , im.purchaseUnit "
                    + "         , ump.unit purchaseUnitName "
                    + " FROM (" + query + ") AS vPre "
                    + "	left join itemMast im on im.code = vPre.mediCode "
                    + " left join itemCat ic on ic.code = im.itemCategoryCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um on um.code = im.sku "
                    + "	left join unitMast ums on ums.code = im.saleUnit "
                    + "	left join unitMast ump on ump.code = im.purchaseUnit "
                    + "	left join hsnMast hm on hm.code = im.hsnCode "
                    + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mediCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mediName")));
                node.setQuantityTotalPerItem(DmServer.vNull(rsTemp.getString("qty")));
                if (isPharmacyDiscount) {
                    node.setDiscountPer(DmShared.vNull(rsTemp.getString("saleDiscountPer")) > 0 ? DmServer.vNull(rsTemp.getString("saleDiscountPer"), "0.00") : DmServer.vNull(pharmacyDiscountPer, "0.00"));
                } else {
                    node.setDiscountPer("0.00");
                }
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));

//                tempQty = DmShared.vNull(rsTemp.getString("qty")) / DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
//                node.setQuantity(DmServer.vNull(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue()))));
//                node.setQuantitySKU(DmServer.vNull(rsTemp.getString("qty")));
                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mediCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mediName")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue());
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));

                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));

                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).intValue() == 1);
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsn")));
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                medicineMdl.setIsSaleRateEditable(DmServer.vNull(rsTemp.getString("isSaleRateEditable")));
                node.setMedicineMdl(medicineMdl);
                listTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(fillBatchFIFO(listTemp, vDate, departmentCode, saleOn, false, cnTemp));

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
    public DmResult<List<GrdMedicine_Mdl>> getRequisitionDetail(String docIdRequisition, String vDate) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        List<GrdMedicine_Mdl> listTemp = new ArrayList<>();
        GrdMedicine_Mdl node;
        Connection cnTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        TxtHMedicine_Mdl medicineMdl = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select mrq.docid, mrqd.v_SNo, mrqd.remark, mrqd.reqDate, ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) AS reqQty, "
                    + "         mrqd.itemCode,im.name as medicine, im.allowNegativeStock, im.isBarcodeActive, im.hsnCode, im.isBestBefore, im.isReturnableItem, "
                    + "         im.restrictGroup, im.itemGSTNature, im.itsBasedOn, im.propertyList, im.saleUnitConversionRate, um.unit, "
                    + "         um.decimalPlaces AS skuDecimalPlaces, ums.decimalPlaces as saleUnitDecimalPlaces, im.saleUnit, ums.unit saleUnitName, im.sku, um.unit skuName, "
                    + "         ic.name as itemCategory, hm.hsn, im.purchaseUnitConversionRate, ums.gstUnitCode, im.saleDiscountPer, im.isSaleRateEditable, "
                    + "         im.purchaseUnit, ump.unit purchaseUnitName,ig.code as groupCode,ig.name as groupName, im.isDiscountAllow "
                    + "	from requisition mrq "
                    + "	left Join requisitionDetail as mrqd on mrqd.docId = mrq.docid and mrqd.isStatus=0 "
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join itemMast im on im.code = mrqd.itemCode "
                    + " left join itemCat ic on ic.code = im.itemCategoryCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join unitMast ums on ums.code = im.saleUnit "
                    + "	left join unitMast ump on ump.code = im.purchaseUnit "
                    + "	left join hsnMast hm on hm.code = im.hsnCode "
                    + "	where mrq.docId = :docId  and ISNULL(mrq.approvedBy,'') <> '' "
                    + " and mrq.site_Code_To = " + DmServer.getSsnSiteCode(this) + "  "
                    + " and ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) > 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", docIdRequisition);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue());
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());

                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);
                medicineMdl.setDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).intValue() == 1);

                DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                dmrIts = getITS(DmShared.iNull(rsTemp.getString("itemCode")).toString(), 0, DmShared.xNull(rsTemp.getString("itsBasedOn")), vDate, cnTemp);
                medicineMdl.getLstITS().addAll(dmrIts.getData());

                node.setMedicineMdl(medicineMdl);
                node.setSaleAcCode(dmrIts.getValue1());

                double tempQty = DmShared.vNull(rsTemp.getString("reqQty")) / DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
                node.setQuantity(DmServer.vNull(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue()))));
                node.setQuantitySKU(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                node.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setReqDocId(DmShared.xNull(rsTemp.getString("docId")));
                node.setReqVSno(DmShared.xNull(rsTemp.getString("v_SNo")));
                listTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();

            dmrTemp.setData(listTemp);
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
    public DmResult<List<GrdMedicine_Mdl>> getEstimateDetail(String docIdEstimate, String vDate, String departmentCode, MedicineSaleOn saleOn) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        List<GrdMedicine_Mdl> listTemp = new ArrayList<>();
        GrdMedicine_Mdl node;
        Connection cnTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        TxtHMedicine_Mdl medicineMdl = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select mrqd.docid, mrqd.v_SNo, mrqd.quantity AS reqQty, mrqd.itemCode,im.name as medicine, im.allowNegativeStock, im.isBarcodeActive, im.hsnCode, "
                    + "         im.isBestBefore, im.isReturnableItem, im.itemGSTNature, im.itsBasedOn, im.propertyList, im.saleUnitConversionRate, "
                    + "         um.unit, um.decimalPlaces AS skuDecimalPlaces, ums.decimalPlaces as saleUnitDecimalPlaces, im.saleUnit, ums.unit saleUnitName, im.sku, "
                    + "         um.unit skuName, ic.name as itemCategory, hm.hsn, im.purchaseUnitConversionRate, ums.gstUnitCode, im.saleDiscountPer, mrqd.skuMrp,"
                    + "         im.isSaleRateEditable, im.restrictGroup, mrqd.skuRate, im.purchaseUnit,im.isDiscountAllow, ump.unit purchaseUnitName"
                    + "         ,ig.code as groupCode,ig.name as groupName,mrqd.ibCode,mrqd.batchNo"
                    + "         ,mrqd.manufacturingDate,mrqd.expiryDate,mrqd.mrp,mrqd.rate,mrqd.freeQty,mrqd.freeWithItemCode "
                    + " ,mrqd.oh_Amt_Discount "
                    + " ,mrqd.oh_Amt_Discount2 "
                    + " ,mrqd.oh_at_CGST "
                    + " ,mrqd.oh_Amt_CGST "
                    + " ,mrqd.oh_at_UGST "
                    + " ,mrqd.oh_Amt_UGST "
                    + " ,mrqd.oh_at_SGST "
                    + " ,mrqd.oh_Amt_SGST "
                    + " ,mrqd.oh_Amt_Taxable "
                    + " ,mrqd.oh_Amt_Net "
                    + " ,mrqd.oh_Ac_Sales "
                    + " ,mrqd.oh_Ac_CGST "
                    + " ,mrqd.oh_Ac_SGST "
                    + " ,mrqd.oh_Ac_UGST "
                    + " ,mrqd.oh_at_Discount "
                    + " ,mrqd.oh_at_Discount2 "
                    + " ,mrqd.grossPurchaseRate "
                    + "	from saleBillDetail as mrqd "
                    + "	Left join itemMast im on im.code = mrqd.itemCode "
                    + " left join itemCat ic on ic.code = im.itemCategoryCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join unitMast ums on ums.code = im.saleUnit "
                    + "	left join unitMast ump on ump.code = im.purchaseUnit "
                    + "	left join hsnMast hm on hm.code = im.hsnCode "
                    + "	where mrqd.docId = :docId  and isFooter = 0 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", docIdEstimate);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                double tempQty = DmShared.vNull(rsTemp.getString("reqQty")) * DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
                double tempFreeQty = DmShared.vNull(rsTemp.getString("freeQty")) * DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
                node.setQuantityTotalPerItem(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue())));

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue());
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount")));
                medicineMdl.setDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).intValue() == 1);
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());

                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setDiscount2(DmServer.vNull(rsTemp.getString("oh_Amt_Discount2"), "0.00"));

                node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscountPer2(DmServer.vNull(rsTemp.getString("oh_at_Discount2"), "0.00"));

                node.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                node.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                node.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));

                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                node.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                node.setSaleAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
                node.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                node.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                node.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));

                if (DmShared.iNull(rsTemp.getString("freeWithItemCode")).longValue() > 0) {
                    node.setFreeWithItemCode(DmShared.xNull(rsTemp.getString("freeWithItemCode")));
                }
                node.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
                node.setGstAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue()), "0.00"));

                node.setMedicineMdl(medicineMdl);

                node.setQuantity(DmServer.vNull(DmServer.vNull(DmShared.vNull(rsTemp.getString("reqQty")), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue()))));
                node.setQuantitySKU(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue())));
                node.setTotalQuantitySKU(DmServer.vNull(tempQty + tempFreeQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue())));
                node.setFreeQuantityInSku(DmServer.vNull(tempFreeQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue())));
                node.setFreeQuantityInSaleUnit(DmServer.vNull(DmShared.vNull(rsTemp.getString("freeQty")), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue())));
                if (Math.abs(DmShared.vNull(rsTemp.getString("freeQty")).doubleValue()) > 0) {
                    node.setSchemeItem(true);
                }
                node.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                node.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));

                node.setIbCode(DmShared.iNull(rsTemp.getString("ibCode")).toString());
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                node.setRate(DmShared.xNull(rsTemp.getString("rate")));
                node.setGrossPurchaseRate(DmShared.vNull(rsTemp.getString("grossPurchaseRate")).toString());

                listTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(fillBatchFIFO(listTemp, vDate, departmentCode, saleOn, true, cnTemp));

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
    public DmResult<List<GrdMedicine_Mdl>> getOperationMedicine(String operationCode, String vDate, String departmentCode, MedicineSaleOn saleOn, String pharmacyDiscountPer, boolean isPharmacyDiscount) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        List<GrdMedicine_Mdl> listTemp = new ArrayList<>();
        GrdMedicine_Mdl node;
        Connection cnTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        TxtHMedicine_Mdl medicineMdl = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT omd.itemCode "
                    + "         , im.name as medicine "
                    + "         , omd.qty "
                    + "         , im.allowNegativeStock"
                    + "         , im.isBarcodeActive"
                    + "         , im.hsnCode"
                    + "         , im.isBestBefore"
                    + "         , im.isReturnableItem"
                    + "         , im.itemGSTNature"
                    + "         , im.itsBasedOn"
                    + "         , im.propertyList"
                    + "         , im.saleUnitConversionRate"
                    + "         , um.unit"
                    + "         , um.decimalPlaces AS skuDecimalPlaces"
                    + "         , ums.decimalPlaces as saleUnitDecimalPlaces"
                    + "         , im.saleUnit"
                    + "         , ums.unit saleUnitName"
                    + "         , im.sku"
                    + "         , um.unit skuName"
                    + "         , ic.name as itemCategory"
                    + "         , hm.hsn"
                    + "         , im.purchaseUnitConversionRate"
                    + "         , ums.gstUnitCode "
                    + "         , im.saleDiscountPer "
                    + "         , im.restrictGroup "
                    + "         , ig.code as groupCode"
                    + "         , ig.name as groupName "
                    + "         , im.purchaseUnit "
                    + "         , ump.unit purchaseUnitName "
                    + "	from operationMediDetail omd "
                    + "	left join itemMast im on im.code = omd.itemCode "
                    + " left join itemCat ic on ic.code = im.itemCategoryCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + " left join unitMast um ON um.code = im.sku "
                    + " left join unitMast ums on ums.code = im.saleUnit "
                    + "	left join unitMast ump on ump.code = im.purchaseUnit "
                    + " left join hsnMast hm on hm.code = im.hsnCode "
                    + "	where omd.serviceCode = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", operationCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                node.setQuantityTotalPerItem(DmServer.vNull(rsTemp.getString("qty")));
                if (isPharmacyDiscount) {
                    node.setDiscountPer(DmShared.vNull(rsTemp.getString("saleDiscountPer")) > 0 ? DmServer.vNull(rsTemp.getString("saleDiscountPer"), "0.00") : DmServer.vNull(pharmacyDiscountPer, "0.00"));
                } else {
                    node.setDiscountPer("0.00");
                }

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue());
                node.setMedicineMdl(medicineMdl);

//                double tempQty = DmShared.vNull(rsTemp.getString("qty")) / DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
//                node.setQuantity(DmServer.vNull(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue()))));
//                node.setQuantitySKU(DmServer.vNull(rsTemp.getString("qty")));
                node.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                node.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));

                listTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(fillBatchFIFO(listTemp, vDate, departmentCode, saleOn, false, cnTemp));
//            dmrTemp.setData(listTemp);

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

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    @Override
    public DmResultPageing<List<TxtHRequisition_Mdl>> getTxtHRequisition(DmHelper helper, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate) {
        DmResultPageing<List<TxtHRequisition_Mdl>> rstTemp = new DmResultPageing<List<TxtHRequisition_Mdl>>();
        List<TxtHRequisition_Mdl> lstTemp = new ArrayList<TxtHRequisition_Mdl>();
        TxtHRequisition_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Voucher date is mandatory.");
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition += " and mrq.departmentToCode = :departmentCode ";
            }
            if (DmShared.iNull(regDocId).longValue() > 0) {
                condition += " and mrq.regDocId = :regDocId ";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select mrq.docid, mrq.recid, mrq.departmentCode, dm.name as department, rbc.name as requestedByName, vt.short_Name,"
                    + "         mrq.v_No, mrq.regDocId, pr.recId regNo, rm.roomNo, pu.patientName,  mrq.v_Date, mrq.departmentToCode, dm1.name departmentTo "
                    + "	from requisition mrq "
                    + "	left Join ( "
                    + "             select rd.docId "
                    + "                 from requisitionDetail rd "
                    + "			where rd.isStatus = 0 and ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 "
                    + "			group by rd.docId "
                    + "		) mrqd on mrqd.docId = mrq.docid "
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join subGroup dm on dm.subCode = mrq.departmentCode "
                    + "	left join subGroup dm1 on dm1.subCode = mrq.departmentToCode "
                    + "	left join subGroup rbc on rbc.subCode =	mrq.requestedByCode "
                    + " left join patientRegistration pr on pr.regDocId = mrq.regDocId "
                    + "	left join roomMast rm on rm.code = pr.roomCode "
                    + "	left join patientUHID pu on pu.docId = pr.uhid "
                    + "	where vt.category in (" + getVoucherCategory(pvc.PHARMACY_REQUISITION) + "," + getVoucherCategory(pvc.NURSING_MEDICINE_REQUISTION) + ") and ISNULL(mrq.approvedBy,'') <> '' "
                    + "     and mrq.site_Code_To IN (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ")   and mrq.v_Date <= :voucherDate and mrqd.docid is not null " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setDate("voucherDate", DmServer.convertDate(voucherDate));
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
            if (DmShared.iNull(regDocId).longValue() > 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisition_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                listNode.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentTo")));
                listNode.setRequisitionBy(DmShared.xNull(rsTemp.getString("requestedByName")));
                listNode.setDocIdRegistration(DmShared.xNull(rsTemp.getString("regDocId")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setRecIdRegistration(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setRequisitionDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRoomNo(DmShared.xNull(rsTemp.getString("roomNo")));
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
    public DmResultPageing<List<TxtHEstimate_Mdl>> getTxtHEstimate(DmHelper helper, boolean blnToEndOfFile, String departmentCode, String regDocId, String voucherDate, String lmCode, String backDaysForEstimateInSaleBill) {
        DmResultPageing<List<TxtHEstimate_Mdl>> rstTemp = new DmResultPageing<List<TxtHEstimate_Mdl>>();
        List<TxtHEstimate_Mdl> lstTemp = new ArrayList<TxtHEstimate_Mdl>();
        TxtHEstimate_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Voucher date is mandatory.");
            }
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                condition += " and est.departmentCode = :departmentCode ";
            }
            if (DmShared.iNull(regDocId).longValue() > 0) {
                condition += " and est.regDocId = :regDocId ";
            }
            if (DmShared.iNull(lmCode).longValue() > 0) {
                condition += " and est.lmCode = :lmCode ";
            }
            condition += " and est.v_Date >= :backDaysForEstimateInSaleBill ";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select est.docId, est.recId, est.v_Date, est.v_No, lm.displayName as patientName, "
                    + "		est.departmentCode, dep.name department, vt.short_Name "
                    + "         ,est.partyType,est.mobile,est.lmCode, est.idTypeCode, est.idNo, idType.name idType,est.subCode,lm.address"
                    + "         ,c.cityName,lm.cityCode,est.placeOfsupply as stateCode"
                    + "         ,sm.name as stateName "
                    + "         ,lm.name as lmName "
                    + "         ,lm.manualCode as lmManualCode ,est.regNo_HIS ,est.uhidNo_HIS,est.regDocId_HIS "
                    + "	from saleBill est "
                    + "	left join voucher_Type vt on vt.v_Type = est.v_Type "
                    + "	left join subGroup dep on dep.subCode = est.departmentCode "
                    + " LEFT JOIN ledgerMast lm ON lm.code = est.lmCode "
                    + " LEFT JOIN city AS c ON c.citycode = lm.citycode "
                    + " LEFT JOIN stateMast AS sm ON sm.code = est.placeOfsupply "
                    + "	LEFT JOIN hl7Mast as idType on idType.code = est.idTypeCode "
                    + "	left join ( "
                    + "             select sb.estimateDocId "
                    + "                 from saleBill sb "
                    + "			left join voucher_Type vt on vt.v_Type = sb.v_Type "
                    + "			where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_CASH) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ") and sb.isCancelled = 0 "
                    + "		) sb on sb.estimateDocId = est.docId "
                    + "	where vt.category in (" + getVoucherCategory(pvc.PHARMACY_ESTIMATE) + ") and sb.estimateDocId is null "
                    + " and est.site_Code = " + DmServer.getSsnSiteCode(this) + "  " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            if (DmShared.iNull(departmentCode).longValue() > 0) {
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            }
            if (DmShared.iNull(regDocId).longValue() > 0) {
                psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
            }
            if (DmShared.iNull(lmCode).longValue() > 0) {
                psTemp.setString("lmCode", DmShared.iNull(lmCode).toString());
            }
            psTemp.setString("backDaysForEstimateInSaleBill", DmShared.xNull(backDaysForEstimateInSaleBill).trim());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHEstimate_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setPatient(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setEstimateDate(DmShared.dNull(rsTemp.getDate("v_Date")));

                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setSubCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setAddress(DmShared.xNull(rsTemp.getString("address")));
                listNode.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                listNode.setLmCode(DmShared.xNull(rsTemp.getString("lmCode")));
                listNode.setLmName(DmShared.xNull(rsTemp.getString("lmName")));
                listNode.setLmManualCode(DmShared.xNull(rsTemp.getString("lmManualCode")));
                listNode.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                listNode.setIdTypeName(DmShared.xNull(rsTemp.getString("idType")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));

                listNode.setRegNo_HIS(DmShared.xNull(rsTemp.getString("regNo_HIS")));
                listNode.setUhidNo_HIS(DmShared.xNull(rsTemp.getString("uhidNo_HIS")));
                listNode.setRegDocId_HIS(DmShared.xNull(rsTemp.getString("regDocId_HIS")));

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

    @Override
    public DmResult<String> isItemEverReturned(String itemCode, String ibCode, String regDocid, String uhid, String lmCode, String prvQty, String prvFreeQty) {
        Connection cnTemp = null;
        DmResult<String> dmrTemp = new DmResult();
        String returnValue = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            returnValue = checkItemSale(itemCode, ibCode, regDocid, uhid, lmCode, prvQty, prvFreeQty, cnTemp);

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
        sqlQuery += " AND vt.category IN (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_RETURN) + ") "
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
    public DmResult<List<GrdMedicine_Mdl>> getRequisitionMedicineDetails(List<KeyFieldExtended> keyFields, String v_Date, String departmentCode, String pharmacyDiscountPer, MedicineSaleOn saleOn, boolean isPharmacyDiscount, boolean requisitionWithoutUHID) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        List<GrdMedicine_Mdl> listDetail = new ArrayList<>();
        List<GrdMedicine_Mdl> listNode;
        GrdMedicine_Mdl node;
        TxtHMedicine_Mdl medicineMdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            /*==============================<< Get Item detail (via list) >>===============================*/
            for (KeyFieldExtended field : keyFields) {
                if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition += " or ";
                    }

                    if (!requisitionWithoutUHID) {
                        condition += "  (rqd.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' ";
                        condition += " and  rqd.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "') ";
                    } else {
                        condition += "  (rqd.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "') ";
                    }
                }
            }
            sqlQuery = " SELECT  "
                    + "   rqd.itemCode as itemCode "
                    + " , im.name as itemName "
                    + " , rqd.docId as reqDocid "
                    + " , rqd.v_SNo as reqv_SNo "
                    + " , rq.recId as requisitionNo "
                    + " , rq.regDocId as patientRegDocId "
                    + " , ISNULL(rqd.reqQty,0) - ISNULL(rqd.issueQty,0) as quantity "
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
                    + " , im.saleDiscountPer as saleDiscountPer"
                    + "   from requisitionDetail as rqd "
                    + "   left join requisition as rq on rq.docId = rqd.docId "
                    + "   left join itemMast as im on rqd.itemCode = im.code "
                    + "   left join unitMast as um on im.sku = um.code "
                    + "   left join unitMast as ump on im.purchaseUnit = ump.code   "
                    + "   left join unitMast as ums on im.saleUnit = ums.code   "
                    + "   left join hsnMast hm on hm.code = im.hsnCode "
                    + "   left join itemGroup ig on ig.code = im.itemGroup "
                    + "   where rqd.isStatus = 0 "
                    + "   and ISNULL(rq.approvedBy,'') <> ''  "
                    + "   and ISNULL(rqd.reqQty,0) - ISNULL(rqd.issueQty,0) > 0 "
                    + "   and rq.site_Code_To IN (" + STServer.getSsnPharmacyAllowPatientForSite(this) + ")"
                    + "   and " + condition
                    //                    + "   group by rqd.itemCode "
                    //                    + "   group by rqd.docId,rqd.v_SNo "
                    + "   order by itemName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                medicineMdl = new TxtHMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));//rsTemp
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));//rsTemp
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));
                node.setReqDocId(DmShared.xNull(rsTemp.getString("reqDocid")));
                node.setReqVSno(DmShared.xNull(rsTemp.getString("reqv_SNo")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("requisitionNo")));
                node.setQuantityTotalPerItem(DmShared.xNull(rsTemp.getString("quantity")));
                node.setQuantity(DmShared.xNull(rsTemp.getString("quantity")));
                node.setPatientRegDocId(DmShared.xNull(rsTemp.getString("patientRegDocId")));
                if (isPharmacyDiscount) {
                    node.setDiscountPer(DmShared.vNull(rsTemp.getString("saleDiscountPer")) > 0 ? DmServer.vNull(rsTemp.getString("saleDiscountPer"), "0.00") : DmServer.vNull(pharmacyDiscountPer, "0.00"));
                } else {
                    node.setDiscountPer("0.00");
                }
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));//rsTemp
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));//rsTemp
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));//rsTemp
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue());//rsTemp
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);//rsTemp
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setIsSaleRateEditable(DmShared.xNull(rsTemp.getString("isSaleRateEditable")));//rsTemp
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));//rsTemp
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));//rsTemp
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));//rsTemp
                medicineMdl.setReturnableMedicine(DmShared.iNull(rsTemp.getString("isReturnableItem")).intValue() == 1);//rsTemp
                medicineMdl.setDiscountAllow(DmShared.iNull(rsTemp.getString("isDiscountAllow")).intValue() == 1);//rsTemp
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));//rsTemp
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));//rsTemp
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);//rsTemp
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));//rsTemp
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));//rsTemp
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));//rsTemp
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));//rsTemp
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));//rsTemp
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));//rsTemp
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));//rsTemp
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("allowedSaleDecimalPlaces")).intValue());//rsTemp

                node.setMedicineMdl(medicineMdl);

                listDetail.add(node);

            }
            rsTemp.close();
            psTemp.close();

            listNode = fillBatchFIFO(listDetail, v_Date, departmentCode, saleOn, false, cnTemp);
            dmrTemp.setData(listNode);

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

    private List<GrdMedicine_Mdl> fillBatchFIFO(List<GrdMedicine_Mdl> lstNode, String v_Date, String departmentCode, MedicineSaleOn saleOn, boolean isEstimateDetails, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdMedicine_Mdl> listNode = new ArrayList<>();
        GrdMedicine_Mdl node;
        TxtHMedicine_Mdl medicineMdl;
//        List<Long> lstIbCode = new ArrayList<>();
        double balanceQty = 0.0;
        String requiredQty = "";
        double batchStockQty = 0.0;
        String selectedIbCodeSQL = "";
        boolean fillBatch = false;

        try {

            selectedIbCodeSQL = " SELECT 0 AS ibCode, 0 AS qty ";

            for (GrdMedicine_Mdl node1 : lstNode) {

                balanceQty = DmShared.vNull(node1.getQuantityTotalPerItem()).doubleValue();
                requiredQty = DmShared.vNull(node1.getQuantityTotalPerItem()).toString();

                sqlQuery = " SELECT "
                        + "     vib.runningTotal "
                        + "   , vib.stockQuantity "
                        + "   , vib.ibCode "
                        + "   , vib.batchNo"
                        + "   , vib.manufacturingDate "
                        + "   , vib.expiryDate "
                        + "   , vib.saleRate "
                        + "   , vib.mrp "
                        + "   , vib.skuSaleRate "
                        + "   , vib.skuMrp "
                        + " FROM ("
                        + "		SELECT "
                        + "              (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) as stockQuantity  "
                        + "		 ,SUM(ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) OVER (ORDER BY ib.expiryDate , (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) DESC) as runningTotal"
                        + "		 ,ib.code as ibCode ,ib.batchNo  "
                        + "		 ,ib.manufacturingDate  "
                        + "		 ,ib.expiryDate  "
                        + "		 ,ib.saleRate  "
                        + "		 ,ib.mrp  "
                        + "		 ,ib.skuSaleRate  "
                        + "		 ,ib.skuMrp  "
                        + "		 FROM itembatch ib  "
                        + "		 LEFT JOIN itemBatchStock ibs ON ibs.ibCode=ib.code  "
                        + "		 LEFT JOIN ( "
                        + "			      SELECT ibTemp.ibCode, SUM(ibTemp.qty) AS qty "
                        + "			      FROM (" + selectedIbCodeSQL + ") AS ibTemp "
                        + "			      GROUP BY ibTemp.ibCode              "
                        + "			   ) AS ibt ON ibt.ibCode = ib.code "
                        + "              WHERE ib.itemCode=:itemCode "
                        + "              AND ISNULL(ibs.departmentCode,:departmentCode) = :departmentCode "
                        + "              AND ISNULL(ibs.site_Code,:site_Code) = :site_Code "
                        + "              AND (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) > 0 "
                        + "              AND ib.expiryDate > :v_Date "
                        + "              AND ib.Code = :ibCode   "
                        + "	) as vib WHERE 1=1  "
                        + "  and  ISNULL(vib.runningTotal,0) <= ISNULL(vib.stockQuantity,0) + :requiredQty ";

                if (DmShared.iNull(node1.getIbCode()).longValue() > 0) {
                    sqlQuery += " AND vib.ibCode = :ibCode ";
                }
                sqlQuery += " ORDER BY vib.expiryDate , vib.stockQuantity DESC ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
                psTemp.setString("itemCode", DmShared.xNull(node1.getSharedKeyFieldCode()));
                psTemp.setString("requiredQty", DmShared.xNull(requiredQty));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
                if (DmShared.iNull(node1.getIbCode()).longValue() > 0) {
                    psTemp.setString("ibCode", DmShared.iNull(node1.getIbCode()).toString());
                }
                rsTemp = psTemp.executeQuery();

                while (rsTemp.next()) {
//                    if (balanceQty != 0 && (batchStockQty != 0 ? true : (!lstIbCode.contains(DmShared.iNull(rsTemp.getString("ibCode")).longValue())))) {
                    if (balanceQty != 0) {
                        fillBatch = true;
                        node = new GrdMedicine_Mdl();
                        medicineMdl = new TxtHMedicine_Mdl();
                        node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));//rsTemp
                        node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));//rsTemp
                        node.setUnit(DmShared.xNull(node1.getUnit()));
                        node.setReqDocId(DmShared.xNull(node1.getReqDocId()));
                        node.setReqVSno(DmShared.xNull(node1.getReqVSno()));
                        node.setPatientRegDocId(DmShared.xNull(node1.getPatientRegDocId()));
                        node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                        node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                        medicineMdl.setHsnCode(DmShared.xNull(node1.getMedicineMdl().getHsnCode()));
                        medicineMdl.setHsnName(DmShared.xNull(node1.getMedicineMdl().getHsnName()));
                        medicineMdl.setPurchaseUnit(DmShared.xNull(node1.getMedicineMdl().getPurchaseUnit()));//rsTemp
                        medicineMdl.setPurchaseUnitName(DmShared.xNull(node1.getMedicineMdl().getPurchaseUnitName()));//rsTemp
                        medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(node1.getMedicineMdl().getPurchaseUnitConversionRate()));//rsTemp
                        medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getAllowSkuDecimalPlaces()).intValue());//rsTemp
                        medicineMdl.setAllowNegativeStock(node1.getMedicineMdl().isAllowNegativeStock());//rsTemp
                        medicineMdl.setBarcodeActive(node1.getMedicineMdl().isBarcodeActive());
                        medicineMdl.setIsSaleRateEditable(DmShared.xNull(node1.getMedicineMdl().getIsSaleRateEditable()));//rsTemp
                        medicineMdl.setConversionRate(DmServer.vNull(node1.getMedicineMdl().getConversionRate()));//rsTemp
                        medicineMdl.setSaleUnitCode(DmShared.xNull(node1.getMedicineMdl().getSaleUnitCode()));//rsTemp
                        medicineMdl.setSaleUnitName(DmShared.xNull(node1.getMedicineMdl().getSaleUnitName()));//rsTemp
                        medicineMdl.setReturnableMedicine(node1.getMedicineMdl().isReturnableMedicine());//rsTemp
                        medicineMdl.setDiscountAllow(node1.getMedicineMdl().isDiscountAllow());//rsTemp
                        medicineMdl.setSharedKeyFieldCode(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));//rsTemp
                        medicineMdl.setSharedKeyFieldName(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldName()));//rsTemp
                        medicineMdl.setBestBefore(node1.getMedicineMdl().isBestBefore());//rsTemp
                        medicineMdl.setRestrictGroup(DmShared.xNull(node1.getMedicineMdl().getRestrictGroup()));//rsTemp
                        medicineMdl.setItemGstNature(DmShared.xNull(node1.getMedicineMdl().getItemGstNature()));//rsTemp
                        medicineMdl.setItsBasedOn(DmShared.xNull(node1.getMedicineMdl().getItsBasedOn()));//rsTemp
                        medicineMdl.setSaleGSTUnit(DmShared.xNull(node1.getMedicineMdl().getSaleGSTUnit()));//rsTemp
                        medicineMdl.setDiscountPer(DmServer.vNull(node1.getMedicineMdl().getDiscountPer()));//rsTemp
                        medicineMdl.setSkuCode(DmShared.xNull(node1.getMedicineMdl().getSkuCode()));//rsTemp
                        medicineMdl.setSkuName(DmShared.xNull(node1.getMedicineMdl().getSkuName()));//rsTemp
                        medicineMdl.setAllowDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getAllowDecimalPlaces()).intValue());//rsTemp
                        medicineMdl.setMedicineGroupCode(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupCode()));//rsTemp
                        medicineMdl.setMedicineGroupName(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupName()));//rsTemp
                        node.setMedicineGroupCode(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupCode()));//rsTemp
                        node.setMedicineGroupName(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupName()));//rsTemp

                        node.setCgstPer(DmServer.vNull(node1.getCgstPer()));
                        node.setSgstPer(DmServer.vNull(node1.getSgstPer()));
                        node.setUgstPer(DmServer.vNull(node1.getUgstPer()));
                        node.setDiscountPer(DmServer.vNull(node1.getDiscountPer()));
                        node.setDiscountPer2(DmServer.vNull(node1.getDiscountPer2()));

                        node.setGstPer(DmServer.vNull(node1.getGstPer()));
                        node.setGstAmount(DmServer.vNull(node1.getGstAmount()));
                        node.setGrossPurchaseRate(DmServer.vNull(node1.getGrossPurchaseRate()));

                        if (isEstimateDetails && node1.getMedicineMdl().isAllowNegativeStock()) {
                            fillBatch = false;
                        }
                        if (fillBatch) {

//                            medicineMdl.setIbCode(DmShared.iNull(rsTemp.getString("ibCode")).toString());
//                            medicineMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
//                            medicineMdl.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
//                            medicineMdl.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                            if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() < balanceQty) {
                                node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                                node.setQuantitySKU(DmServer.vNull(rsTemp.getString("stockQuantity"), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                                balanceQty = balanceQty - DmShared.vNull(node.getQuantitySKU()).doubleValue();
                                batchStockQty = 0;
                            } else if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() == balanceQty) {
                                node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                                node.setQuantitySKU(DmServer.vNull(rsTemp.getString("stockQuantity"), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                                balanceQty = 0;
                                batchStockQty = 0;
                            } else {
                                node.setQuantity(DmServer.vNull(balanceQty));
                                node.setQuantitySKU(DmServer.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                                batchStockQty = DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() - balanceQty;
                                balanceQty = 0;
                            }
                            node.setTotalQuantitySKU(DmServer.vNull(node.getQuantitySKU()));
                            double stockQty = DmShared.vNull(rsTemp.getString("stockQuantity")) / DmShared.vNull(medicineMdl.getConversionRate());
                            node.setBatchQuantityInSaleUnit(DmServer.vNull(stockQty));
                            //<editor-fold defaultstate="collapsed" desc="BATCH Detail">

                            node.setIbCode(DmShared.iNull(rsTemp.getString("ibCode")).toString());
                            node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                            node.setMfgDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                            node.setExpDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                            node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                            node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));

                            if (!isEstimateDetails) {
                                if (saleOn == MedicineSaleOn.COST_PRICE) {
                                    String costPrice = "", skuCostPrice = "";
                                    skuCostPrice = DmServer.vNull(STServer.getSkuLandedRate(DmShared.xNull(node.getSharedKeyFieldCode()), rsTemp.getString("ibCode"), DmShared.iNull(departmentCode).toString(), DmServer.getSsnSiteCode(this), v_Date, cnTemp), "0.00");
                                    costPrice = DmServer.vNull(DmShared.vNull(skuCostPrice) * DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()));
                                    setSaleRate(node, DmShared.xNull(costPrice), DmShared.xNull(skuCostPrice), saleOn);
                                } else {
                                    setSaleRate(node, DmShared.xNull(rsTemp.getString("saleRate")), DmShared.xNull(rsTemp.getString("skuSaleRate")), saleOn);
                                }
                            } else {
                                node.setRate(DmServer.vNull(node1.getRate(), "0.00"));
                                node.setSkuRate(DmServer.vNull(node1.getSkuRate()));

                            }
//</editor-fold>
                            selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantitySKU()) + " AS qty ";

                            //<editor-fold defaultstate="collapsed" desc="Quantity in saleUnit">
                            if (DmShared.iNull(medicineMdl.getSkuCode()).longValue() != DmShared.iNull(medicineMdl.getSaleUnitCode()).longValue()) {
                                if (DmShared.vNull(node.getQuantity()) > 0) {
                                    double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(medicineMdl.getConversionRate());
                                    node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));

                                }
                            }
//</editor-fold>
                        } else {

//                            lstIbCode.add(DmShared.iNull(medicineMdl.getIbCode()).longValue());
                            node.setQuantity(DmServer.vNull(node1.getQuantity()));
                            node.setQuantitySKU(DmServer.vNull(DmShared.vNull(node1.getQuantitySKU()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                            node.setTotalQuantitySKU(DmServer.vNull(DmShared.vNull(node1.getTotalQuantitySKU()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                            balanceQty = 0;
                            batchStockQty = 0;

                            selectedIbCodeSQL += " UNION ALL SELECT " + node1.getIbCode() + " AS ibCode, " + DmServer.vNull(node1.getQuantitySKU()) + " AS qty ";

                            //<editor-fold defaultstate="collapsed" desc="BATCH Detail">
//                            medicineMdl.setIbCode(DmShared.iNull(node1.getMedicineMdl().getIbCode()).toString());
//                            medicineMdl.setBatchNo(DmShared.xNull(node1.getMedicineMdl().getBatchNo()));
//                            medicineMdl.setMfgDate(DmShared.dNull(node1.getMedicineMdl().getMfgDate()));
//                            medicineMdl.setExpDate(DmShared.dNull(node1.getMedicineMdl().getExpDate()));
                            node.setIbCode(DmShared.iNull(node1.getIbCode()).toString());
                            node.setBatchNo(DmShared.xNull(node1.getBatchNo()));
                            node.setMfgDate(DmShared.dNull(node1.getMfgDate()));
                            node.setExpDate(DmShared.dNull(node1.getExpDate()));

                            node.setDiscountPer(DmServer.vNull(node1.getDiscountPer()));
                            node.setDiscountPer2(DmServer.vNull(node1.getDiscountPer2()));
                            node.setMrp(DmServer.vNull(node1.getMrp(), "0.00"));
                            node.setSkuMrp(DmServer.vNull(node1.getSkuMrp()));

                            if (!isEstimateDetails) {
                                if (saleOn == MedicineSaleOn.COST_PRICE) {
                                    String costPrice = "", skuCostPrice = "";
                                    skuCostPrice = DmServer.vNull(STServer.getSkuLandedRate(DmShared.xNull(node.getSharedKeyFieldCode()), rsTemp.getString("ibCode"), DmShared.iNull(departmentCode).toString(), DmServer.getSsnSiteCode(this), v_Date, cnTemp), "0.00");
                                    costPrice = DmServer.vNull(DmShared.vNull(skuCostPrice) * DmShared.vNull(medicineMdl.getPurchaseUnitConversionRate()));
                                    setSaleRate(node, DmShared.xNull(costPrice), DmShared.xNull(skuCostPrice), saleOn);
                                } else {
                                    setSaleRate(node, DmShared.xNull(node1.getRate()), DmShared.xNull(node1.getSkuRate()), saleOn);
                                }
                            } else {
                                node.setRate(DmServer.vNull(node1.getRate(), "0.00"));
                                node.setSkuRate(DmServer.vNull(node1.getSkuRate()));
                            }

                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="Quantity in saleUnit">
                            if (DmShared.iNull(medicineMdl.getSkuCode()).longValue() != DmShared.iNull(medicineMdl.getSaleUnitCode()).longValue()) {
                                if (DmShared.vNull(node.getQuantity()) > 0) {
                                    double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(medicineMdl.getConversionRate());
                                    node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
                                }
                            }
                        }
                        //<editor-fold defaultstate="collapsed" desc="ITS AND SALEACCODE">
                        DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                        dmrIts = getITS(DmShared.iNull(node1.getMedicineMdl().getSharedKeyFieldCode()).toString(), DmShared.vNull(node.getRate()).doubleValue(), DmShared.xNull(node1.getMedicineMdl().getItsBasedOn()), v_Date, cnTemp);
                        medicineMdl.getLstITS().addAll(dmrIts.getData());
                        node.setSaleAcCode(dmrIts.getValue1());

//</editor-fold>
                        node.setMedicineMdl(medicineMdl);

                        listNode.add(node);

                    }
                }
                rsTemp.close();
                psTemp.close();

                if (balanceQty != 0) {
                    node = new GrdMedicine_Mdl();
                    medicineMdl = new TxtHMedicine_Mdl();
                    node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));//rsTemp
                    node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));//rsTemp
                    node.setUnit(DmShared.xNull(node1.getUnit()));
                    node.setReqDocId(DmShared.xNull(node1.getReqDocId()));
                    node.setReqVSno(DmShared.xNull(node1.getReqVSno()));
                    node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                    node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                    node.setPatientRegDocId(DmShared.xNull(node1.getPatientRegDocId()));

                    medicineMdl.setHsnCode(DmShared.xNull(node1.getMedicineMdl().getHsnCode()));
                    medicineMdl.setHsnName(DmShared.xNull(node1.getMedicineMdl().getHsnName()));
                    medicineMdl.setPurchaseUnit(DmShared.xNull(node1.getMedicineMdl().getPurchaseUnit()));//rsTemp
                    medicineMdl.setPurchaseUnitName(DmShared.xNull(node1.getMedicineMdl().getPurchaseUnitName()));//rsTemp
                    medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(node1.getMedicineMdl().getPurchaseUnitConversionRate()));//rsTemp
                    medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getAllowSkuDecimalPlaces()).intValue());//rsTemp
                    medicineMdl.setAllowNegativeStock(node1.getMedicineMdl().isAllowNegativeStock());//rsTemp
                    medicineMdl.setBarcodeActive(node1.getMedicineMdl().isBarcodeActive());
                    medicineMdl.setIsSaleRateEditable(DmShared.xNull(node1.getMedicineMdl().getIsSaleRateEditable()));//rsTemp
                    medicineMdl.setConversionRate(DmServer.vNull(node1.getMedicineMdl().getConversionRate()));//rsTemp
                    medicineMdl.setSaleUnitCode(DmShared.xNull(node1.getMedicineMdl().getSaleUnitCode()));//rsTemp
                    medicineMdl.setSaleUnitName(DmShared.xNull(node1.getMedicineMdl().getSaleUnitName()));//rsTemp
                    medicineMdl.setReturnableMedicine(node1.getMedicineMdl().isReturnableMedicine());//rsTemp
                    medicineMdl.setDiscountAllow(node1.getMedicineMdl().isDiscountAllow());//rsTemp
                    medicineMdl.setSharedKeyFieldCode(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));//rsTemp
                    medicineMdl.setSharedKeyFieldName(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldName()));//rsTemp
                    medicineMdl.setBestBefore(node1.getMedicineMdl().isBestBefore());//rsTemp
                    medicineMdl.setRestrictGroup(DmShared.xNull(node1.getMedicineMdl().getRestrictGroup()));//rsTemp
                    medicineMdl.setItemGstNature(DmShared.xNull(node1.getMedicineMdl().getItemGstNature()));//rsTemp
                    medicineMdl.setItsBasedOn(DmShared.xNull(node1.getMedicineMdl().getItsBasedOn()));//rsTemp
                    medicineMdl.setSaleGSTUnit(DmShared.xNull(node1.getMedicineMdl().getSaleGSTUnit()));//rsTemp
                    medicineMdl.setDiscountPer(DmServer.vNull(node1.getMedicineMdl().getDiscountPer()));//rsTemp
                    medicineMdl.setSkuCode(DmShared.xNull(node1.getMedicineMdl().getSkuCode()));//rsTemp
                    medicineMdl.setSkuName(DmShared.xNull(node1.getMedicineMdl().getSkuName()));//rsTemp
                    medicineMdl.setAllowDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getAllowDecimalPlaces()).intValue());//rsTemp
                    medicineMdl.setMedicineGroupCode(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupCode()));//rsTemp
                    medicineMdl.setMedicineGroupName(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupName()));//rsTemp

                    node.setCgstPer(DmServer.vNull(node1.getCgstPer()));
                    node.setSgstPer(DmServer.vNull(node1.getSgstPer()));
                    node.setUgstPer(DmServer.vNull(node1.getUgstPer()));
                    node.setDiscountPer(DmServer.vNull(node1.getDiscountPer()));
                    node.setDiscountPer2(DmServer.vNull(node1.getDiscountPer2()));
                    node.setGstPer(DmServer.vNull(node1.getGstPer()));
                    node.setGstAmount(DmServer.vNull(node1.getGstAmount()));

                    node.setQuantity(DmServer.vNull(balanceQty));
                    node.setQuantitySKU(DmServer.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    node.setTotalQuantitySKU(DmServer.vNull(node.getQuantitySKU(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    balanceQty = 0;
                    //<editor-fold defaultstate="collapsed" desc="BATCH Detail">
                    if ((isEstimateDetails && node1.getMedicineMdl().isAllowNegativeStock())) {
//                        medicineMdl.setIbCode(DmShared.iNull(node1.getMedicineMdl().getIbCode()).toString());
//                        medicineMdl.setBatchNo(DmShared.xNull(node1.getMedicineMdl().getBatchNo()));
//                        medicineMdl.setMfgDate(DmShared.dNull(node1.getMedicineMdl().getMfgDate()));
//                        medicineMdl.setExpDate(DmShared.dNull(node1.getMedicineMdl().getExpDate()));

                        node.setIbCode(DmShared.iNull(node1.getIbCode()).toString());
                        node.setBatchNo(DmShared.xNull(node1.getBatchNo()));
                        node.setMfgDate(DmShared.dNull(node1.getMfgDate()));
                        node.setExpDate(DmShared.dNull(node1.getExpDate()));

                        node.setMrp(DmServer.vNull(node1.getMrp(), "0.00"));
                        node.setSkuMrp(DmServer.vNull(node1.getSkuMrp()));

                        node.setRate(DmServer.vNull(node1.getRate(), "0.00"));
                        node.setSkuRate(DmServer.vNull(node1.getSkuRate()));

                    }
                    //</editor-fold>
                    selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantitySKU()) + " AS qty ";
                    //<editor-fold defaultstate="collapsed" desc="Quantity in sale unit">
                    if (DmShared.iNull(medicineMdl.getSkuCode()).longValue() != DmShared.iNull(medicineMdl.getSaleUnitCode()).longValue()) {
                        if (DmShared.vNull(node.getQuantity()) > 0) {
                            double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(medicineMdl.getConversionRate());
                            node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
                        }
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="ITS AND SALEACCODE">
                    DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                    dmrIts = getITS(DmShared.iNull(node1.getMedicineMdl().getSharedKeyFieldCode()).toString(), DmShared.vNull(node.getRate()).doubleValue(), DmShared.xNull(node1.getMedicineMdl().getItsBasedOn()), v_Date, cnTemp);
                    medicineMdl.getLstITS().addAll(dmrIts.getData());
                    node.setSaleAcCode(dmrIts.getValue1());
//</editor-fold>
                    node.setMedicineMdl(medicineMdl);

                    listNode.add(node);

                }
                if (Math.abs(DmShared.vNull(node1.getFreeQuantityInSaleUnit())) > 0) {
                    node = new GrdMedicine_Mdl();
                    medicineMdl = new TxtHMedicine_Mdl();
                    node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));//rsTemp
                    node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));//rsTemp
                    node.setUnit(DmShared.xNull(node1.getUnit()));
                    node.setReqDocId(DmShared.xNull(node1.getReqDocId()));
                    node.setReqVSno(DmShared.xNull(node1.getReqVSno()));
                    node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                    node.setQuantityTotalPerItem(DmShared.xNull(node1.getQuantityTotalPerItem()));
                    medicineMdl.setHsnCode(DmShared.xNull(node1.getMedicineMdl().getHsnCode()));
                    medicineMdl.setHsnName(DmShared.xNull(node1.getMedicineMdl().getHsnName()));
                    medicineMdl.setPurchaseUnit(DmShared.xNull(node1.getMedicineMdl().getPurchaseUnit()));//rsTemp
                    medicineMdl.setPurchaseUnitName(DmShared.xNull(node1.getMedicineMdl().getPurchaseUnitName()));//rsTemp
                    medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(node1.getMedicineMdl().getPurchaseUnitConversionRate()));//rsTemp
                    medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getAllowSkuDecimalPlaces()).intValue());//rsTemp
                    medicineMdl.setAllowNegativeStock(node1.getMedicineMdl().isAllowNegativeStock());//rsTemp
                    medicineMdl.setBarcodeActive(node1.getMedicineMdl().isBarcodeActive());
                    medicineMdl.setIsSaleRateEditable(DmShared.xNull(node1.getMedicineMdl().getIsSaleRateEditable()));//rsTemp
                    medicineMdl.setConversionRate(DmServer.vNull(node1.getMedicineMdl().getConversionRate()));//rsTemp
                    medicineMdl.setSaleUnitCode(DmShared.xNull(node1.getMedicineMdl().getSaleUnitCode()));//rsTemp
                    medicineMdl.setSaleUnitName(DmShared.xNull(node1.getMedicineMdl().getSaleUnitName()));//rsTemp
                    medicineMdl.setReturnableMedicine(node1.getMedicineMdl().isReturnableMedicine());//rsTemp
                    medicineMdl.setDiscountAllow(node1.getMedicineMdl().isDiscountAllow());//rsTemp
                    medicineMdl.setSharedKeyFieldCode(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldCode()));//rsTemp
                    medicineMdl.setSharedKeyFieldName(DmShared.xNull(node1.getMedicineMdl().getSharedKeyFieldName()));//rsTemp
                    medicineMdl.setBestBefore(node1.getMedicineMdl().isBestBefore());//rsTemp
                    medicineMdl.setRestrictGroup(DmShared.xNull(node1.getMedicineMdl().getRestrictGroup()));//rsTemp
                    medicineMdl.setItemGstNature(DmShared.xNull(node1.getMedicineMdl().getItemGstNature()));//rsTemp
                    medicineMdl.setItsBasedOn(DmShared.xNull(node1.getMedicineMdl().getItsBasedOn()));//rsTemp
                    medicineMdl.setSaleGSTUnit(DmShared.xNull(node1.getMedicineMdl().getSaleGSTUnit()));//rsTemp
                    medicineMdl.setDiscountPer(DmServer.vNull(node1.getMedicineMdl().getDiscountPer()));//rsTemp
                    medicineMdl.setSkuCode(DmShared.xNull(node1.getMedicineMdl().getSkuCode()));//rsTemp
                    medicineMdl.setSkuName(DmShared.xNull(node1.getMedicineMdl().getSkuName()));//rsTemp
                    medicineMdl.setAllowDecimalPlaces(DmShared.iNull(node1.getMedicineMdl().getAllowDecimalPlaces()).intValue());//rsTemp
                    medicineMdl.setMedicineGroupCode(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupCode()));//rsTemp
                    medicineMdl.setMedicineGroupName(DmShared.xNull(node1.getMedicineMdl().getMedicineGroupName()));//rsTemp

                    node.setCgstPer(DmServer.vNull(node1.getCgstPer()));
                    node.setSgstPer(DmServer.vNull(node1.getSgstPer()));
                    node.setUgstPer(DmServer.vNull(node1.getUgstPer()));
                    node.setDiscountPer(DmServer.vNull(node1.getDiscountPer()));
                    node.setDiscountPer2(DmServer.vNull(node1.getDiscountPer2()));
                    node.setGstPer(DmServer.vNull(node1.getGstPer()));
                    node.setGstAmount(DmServer.vNull(node1.getGstAmount()));
                    node.setFreeWithItemCode(DmShared.xNull(node1.getFreeWithItemCode()));

                    node.setQuantity(DmServer.vNull(0));
                    node.setQuantitySKU(DmServer.vNull(0, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    node.setTotalQuantitySKU(DmServer.vNull(node1.getFreeQuantityInSku(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    node.setFreeQuantityInSaleUnit(DmServer.vNull(node1.getFreeQuantityInSaleUnit(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    if (Math.abs(DmShared.vNull(node.getFreeQuantityInSaleUnit()).doubleValue()) > 0) {
                        node.setSchemeItem(true);
                    }

                    node.setFreeQuantityInSku(DmServer.vNull(node1.getFreeQuantityInSku(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    balanceQty = 0;
                    //<editor-fold defaultstate="collapsed" desc="BATCH Detail">
                    if ((isEstimateDetails)) {
//                        medicineMdl.setIbCode(DmShared.iNull(node1.getMedicineMdl().getIbCode()).toString());
//                        medicineMdl.setBatchNo(DmShared.xNull(node1.getMedicineMdl().getBatchNo()));
//                        medicineMdl.setMfgDate(DmShared.dNull(node1.getMedicineMdl().getMfgDate()));
//                        medicineMdl.setExpDate(DmShared.dNull(node1.getMedicineMdl().getExpDate()));

                        node.setIbCode(DmShared.iNull(node1.getIbCode()).toString());
                        node.setBatchNo(DmShared.xNull(node1.getBatchNo()));
                        node.setMfgDate(DmShared.dNull(node1.getMfgDate()));
                        node.setExpDate(DmShared.dNull(node1.getExpDate()));

                        node.setMrp(DmServer.vNull(node1.getMrp(), "0.00"));
                        node.setSkuMrp(DmServer.vNull(node1.getSkuMrp()));

                        node.setRate(DmServer.vNull(node1.getRate(), "0.00"));
                        node.setSkuRate(DmServer.vNull(node1.getSkuRate()));

                    }
                    //</editor-fold>
                    selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantitySKU()) + " AS qty ";
                    //<editor-fold defaultstate="collapsed" desc="Quantity in sale unit">
                    if (DmShared.iNull(medicineMdl.getSkuCode()).longValue() != DmShared.iNull(medicineMdl.getSaleUnitCode()).longValue()) {
                        if (DmShared.vNull(node.getQuantity()) > 0) {
                            double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(medicineMdl.getConversionRate());
                            node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowDecimalPlaces()).intValue())));
                        }
                    }
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="ITS AND SALEACCODE">
                    DmResult<List<OverHeadITS_Mdl>> dmrIts = new DmResult();
                    dmrIts = getITS(DmShared.iNull(node1.getMedicineMdl().getSharedKeyFieldCode()).toString(), DmShared.vNull(node.getRate()).doubleValue(), DmShared.xNull(node1.getMedicineMdl().getItsBasedOn()), v_Date, cnTemp);
                    medicineMdl.getLstITS().addAll(dmrIts.getData());
                    node.setSaleAcCode(dmrIts.getValue1());
//</editor-fold>
                    node.setMedicineMdl(medicineMdl);

                    listNode.add(node);

                }
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
        return listNode;
    }

    private void setSaleRate(GrdMedicine_Mdl grdMedicine_Mdl, String saleRate, String skuSaleRate, MedicineSaleOn saleOn) {
        double tempRate = 0, tempSkuRate = 0;

        if (saleOn == MedicineSaleOn.MRP) {
            tempRate = DmShared.vNull(grdMedicine_Mdl.getMrp()).doubleValue();
            tempSkuRate = DmShared.vNull(grdMedicine_Mdl.getSkuMrp()).doubleValue();
        } else {
            tempRate = DmShared.vNull(saleRate).doubleValue();
            tempSkuRate = DmShared.vNull(skuSaleRate).doubleValue();
        }

        grdMedicine_Mdl.setRate(DmServer.vNull(tempRate, "0.00"));
        grdMedicine_Mdl.setSkuRate(DmServer.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
    }

    @Override
    public DmResultPageing<List<TxtHOldOpd_Mdl>> getTxtHOldOpd(String uhid, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHOldOpd_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHOldOpd_Mdl> lstTemp = new ArrayList<>();
        TxtHOldOpd_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);
        boolean isResult = false;

        try {
            cnTemp = DmServer.getConnection(STServer.getSsnParentDateBase(this));

            sqlQuery = "select pp.consultantCode,sgCons.name as consultant,pr.v_Date,pr.recId OPDNo,pr.regDocid, pp.docId\n"
                    + "from patientRegistration pr \n"
                    + "left join voucher_Type vt on vt.v_Type = pr.v_Type\n"
                    + "left join patientPrescription pp on pp.regdocid = pr.regDocId\n"
                    + "left join subGroup sgCons on sgCons.subCode = pp.consultantCode \n"
                    + "where \n"
                    + "pr.uhid = :uhid\n"
                    + "and isNull(pp.docid,'') <> '' \n"
                    + "and vt.category in (" + category + ")\n"
                    + "and pr.v_Date > DATEADD(MONTH, -6,  convert(date,getDate())) and  pr.v_Date <= convert(date,getDate())\n"
                    + "and pr.site_Code = :site_Code\n";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("uhid", DmShared.iNull(uhid).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isResult = true;
                listNode = new TxtHOldOpd_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("regDocid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("OPDNo")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setConsultant(DmShared.xNull(rsTemp.getString("consultant")));
                listNode.setConsultantCode(DmShared.xNull(rsTemp.getString("consultantCode")));
                listNode.setPrescDocid(DmShared.xNull(rsTemp.getString("docId")));
                lstTemp.add(listNode);
            }
            if (!isResult) {
                throw new DmException(0, "No Record Found.");
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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

    private boolean isEstimateBillCreated(String docId, boolean isMoveRec, Connection cnTemp) {
        boolean returnValue = false;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        String sqlQuery = "";

        try {
            sqlQuery = "select COUNT(*) AS cnt  "
                    + " from saleBill sb  "
                    + " left join voucher_Type vt on vt.v_Type = sb.v_Type  "
                    + " where vt.category in (" + getVoucherCategory(pvc.PHARMACY_BILL) + ", " + getVoucherCategory(pvc.PHARMACY_BILL_CASH) + ", " + getVoucherCategory(pvc.PHARMACY_ESTIMATE_BILL) + ")"
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

    private CompareProperties_Mdl iniItemBatchProperties(SaleBill_Mdl mrTempCurrent, SaleBill_Mdl mrTempPrevious) {
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select mtm.code, mtm.name "
                    + "	from medicineTemplateMast mtm ");
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
    public DmResult<List<GrdMedicine_Mdl>> getMedicine(String operationCode, String vDate, String departmentCode, MedicineSaleOn saleOn, String pharmacyDiscountPer, boolean isPharmacyDiscount) {
        DmResult<List<GrdMedicine_Mdl>> dmrTemp = new DmResult();
        List<GrdMedicine_Mdl> listTemp = new ArrayList<>();
        GrdMedicine_Mdl node;
        Connection cnTemp = null;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        TxtHMedicine_Mdl medicineMdl = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT mtd.itemCode "
                    + "         , im.name as medicine "
                    + "         , mtd.quantity "
                    + "         , im.allowNegativeStock"
                    + "         , im.isBarcodeActive"
                    + "         , im.hsnCode"
                    + "         , im.isBestBefore"
                    + "         , im.isReturnableItem"
                    + "         , im.itemGSTNature"
                    + "         , im.itsBasedOn"
                    + "         , im.propertyList"
                    + "         , im.saleUnitConversionRate"
                    + "         , um.unit"
                    + "         , um.decimalPlaces AS skuDecimalPlaces"
                    + "         , ums.decimalPlaces as saleUnitDecimalPlaces"
                    + "         , im.saleUnit"
                    + "         , ums.unit saleUnitName"
                    + "         , im.sku"
                    + "         , um.unit skuName"
                    + "         , ic.name as itemCategory"
                    + "         , hm.hsn"
                    + "         , im.purchaseUnitConversionRate"
                    + "         , ums.gstUnitCode "
                    + "         , im.saleDiscountPer "
                    + "         , im.restrictGroup "
                    + "         , ig.code as groupCode"
                    + "         , ig.name as groupName "
                    + "         , im.purchaseUnit "
                    + "         , ump.unit purchaseUnitName "
                    + "	from medicineTemplateDetail mtd "
                    + "	left join itemMast im on im.code = mtd.itemCode "
                    + " left join itemCat ic on ic.code = im.itemCategoryCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + " left join unitMast um ON um.code = im.sku "
                    + " left join unitMast ums on ums.code = im.saleUnit "
                    + "	left join unitMast ump on ump.code = im.purchaseUnit "
                    + " left join hsnMast hm on hm.code = im.hsnCode "
                    + "	where mtd.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", operationCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                node.setQuantityTotalPerItem(DmServer.vNull(rsTemp.getString("quantity")));
                if (isPharmacyDiscount) {
                    node.setDiscountPer(DmShared.vNull(rsTemp.getString("saleDiscountPer")) > 0 ? DmServer.vNull(rsTemp.getString("saleDiscountPer"), "0.00") : DmServer.vNull(pharmacyDiscountPer, "0.00"));
                } else {
                    node.setDiscountPer("0.00");
                }

                medicineMdl = new TxtHMedicine_Mdl();
                medicineMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                medicineMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicine")));
                medicineMdl.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                medicineMdl.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                medicineMdl.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                medicineMdl.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                medicineMdl.setDiscountPer(DmServer.vNull(rsTemp.getString("saleDiscountPer")));
                medicineMdl.setConversionRate(DmServer.vNull(rsTemp.getString("saleUnitConversionRate")));
                medicineMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                medicineMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
                medicineMdl.setSaleUnitName(DmShared.xNull(rsTemp.getString("saleUnitName")));
                medicineMdl.setPurchaseUnit(DmShared.xNull(rsTemp.getString("purchaseUnit")));
                medicineMdl.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                medicineMdl.setRestrictGroup(DmShared.xNull(rsTemp.getString("restrictGroup")));
                medicineMdl.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
                medicineMdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                medicineMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                medicineMdl.setSaleGSTUnit(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                medicineMdl.setSkuCode(DmShared.xNull(rsTemp.getString("sku")));
                medicineMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                medicineMdl.setAllowNegativeStock(DmShared.iNull(rsTemp.getString("allowNegativeStock")).intValue() == 1);
                medicineMdl.setBarcodeActive(DmShared.iNull(rsTemp.getString("isBarcodeActive")).intValue() == 1);
                medicineMdl.setAllowSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue());
                medicineMdl.setAllowDecimalPlaces(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue());
                node.setMedicineMdl(medicineMdl);

//                double tempQty = DmShared.vNull(rsTemp.getString("qty")) / DmShared.vNull(rsTemp.getString("saleUnitConversionRate"));
//                node.setQuantity(DmServer.vNull(DmServer.vNull(tempQty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("saleUnitDecimalPlaces")).intValue()))));
//                node.setQuantitySKU(DmServer.vNull(rsTemp.getString("qty")));
                node.setMedicineCategory(DmShared.xNull(rsTemp.getString("itemCategory")));
                node.setMedicineGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setMedicineGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("saleUnitName")));

                listTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(fillBatchFIFO(listTemp, vDate, departmentCode, saleOn, false, cnTemp));
//            dmrTemp.setData(listTemp);

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
    public DmResultPageing<List<UhidPopup_Mdl>> getTxtHUHID(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<UhidPopup_Mdl>> rstTemp = new DmResultPageing<>();
        List<UhidPopup_Mdl> lstTemp = new ArrayList<>();
        UhidPopup_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";

        try {
            rstTemp.setData(lstTemp);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            cnTemp = DmServer.getConnection(STServer.getSsnParentDateBase(this));

            sqlQuery = "SELECT  pu.docId, pu.recId AS uhid, pu.guardianName AS guardian, pu.patientFirstName, pu.patientMiddleName, "
                    + " pu.patientLastName, pu.patientInitial,pu.guardianInitial ,"
                    + " pu2.idTypeCode, id.name as idType, pu.mobile, pu.eMail, pu.idNo,null as regDocId, null as regNo "
                    + " FROM patientUHID AS pu "
                    + " LEFT JOIN patientUHID2 AS pu2 ON pu2.uhid = pu.docId "
                    + " LEFT JOIN hl7Mast AS id ON pu2.idTypeCode = id.code "
                    + " LEFT JOIN voucher_Type AS vt ON vt.v_Type = pu.v_Type "
                    + " WHERE (pu.isCancelled <> 1) and isNull(pu.approvedBy,'')<>'' and isNull(pu.isExpired,0)=0 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new UhidPopup_Mdl();
                listNode.setRecIdUHID(DmShared.xNull(rsTemp.getString("uhid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("uhid")));
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("DocId")).toString());
                listNode.setDocIdUHID(DmShared.iNull(rsTemp.getString("DocId")).toString());
                listNode.setRecIdRegistration(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setDocIdRegistration(DmShared.iNull(rsTemp.getString("regDocId")).toString());
                listNode.setPatientInitial(DmShared.xNull(rsTemp.getString("patientInitial")));
                listNode.setFirstName(DmShared.xNull(rsTemp.getString("patientFirstName")));
                listNode.setMiddleName(DmShared.xNull(rsTemp.getString("patientMiddleName")));
                listNode.setFirstName(DmShared.xNull(rsTemp.getString("patientFirstName")));
                listNode.setMiddleName(DmShared.xNull(rsTemp.getString("patientMiddleName")));
                listNode.setLastName(DmShared.xNull(rsTemp.getString("patientLastName")));
                listNode.setGuardianName(DmShared.xNull(rsTemp.getString("guardian")));
                listNode.setMobileNo(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setIdType(DmShared.xNull(rsTemp.getString("idType")));
                listNode.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                listNode.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                listNode.setEmail(DmShared.xNull(rsTemp.getString("email")));
                listNode.setGuardianInitial(DmShared.xNull(rsTemp.getString("guardianInitial")));
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
}
