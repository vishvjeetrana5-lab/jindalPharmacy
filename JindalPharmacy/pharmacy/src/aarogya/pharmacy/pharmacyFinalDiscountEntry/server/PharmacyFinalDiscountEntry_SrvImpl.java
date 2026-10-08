package aarogya.pharmacy.pharmacyFinalDiscountEntry.server;

import aarogya.pharmacy.ledgerMaster.server.LedgerMasterServer;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.server.PartySearch_SrvImpl;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.DefaultValue_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.Find_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.GrdDiscountDetails_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.HSN_TAX_WISE_PopUp_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_Server;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_Srv;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_Vld;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.TxtHVType_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;

public class PharmacyFinalDiscountEntry_SrvImpl extends DmRSSPrivate implements PharmacyFinalDiscountEntry_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "'";
    private final String voucherTypefieldName = "sb.V_Type", voucherDateFieldName = "sb.V_Date";

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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
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

            sqlQuery = " SELECT sb.DocId  "
                    + " FROM saleBill sb  "
                    + " LEFT JOIN voucher_type vt ON vt.v_type = sb.v_type  "
                    + " WHERE vt.category IN ( " + vCat + " ) "
                    + " and sb.site_Code=:siteCode "
                    + " and sb.v_Prefix =:vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += "order by sb.v_Date desc, vt.short_Name, sb.v_no desc, sb.recId ";
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String pharmacyFinalDiscountCode = "";
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mrTemp.setPartyType(DmEnum.DmPartyType.Unregistered.getId());

            sqlQuery = " select TOP 1 vt.v_Type, vt.description, vt.documentNatureCode, vt.documentTypeCode "
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN (" + vCat + ")"
                    + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                TxtHVType_Mdl vTypeMdl = new TxtHVType_Mdl();
                vTypeMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                vTypeMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                vTypeMdl.setDocumentTypeCode(DmShared.xNull(rsTemp.getString("documentTypeCode")));
                vTypeMdl.setDocumentNatureCode(DmShared.xNull(rsTemp.getString("documentNatureCode")));
                mrTemp.setVoucherTypeMdl(vTypeMdl);
            }
            psTemp.close();
            rsTemp.close();

            //Sales Ac Code 
            sqlQuery = "SELECT  "
                    + "    env.pharmacyFinalDiscountOHCode "
                    + " ,  env.pharmacyFinalDiscountAcCode "
                    + " ,  env.defaultPharmacyServiceCode "
                    + " FROM enviro as env "
                    + " Where env.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                pharmacyFinalDiscountCode = DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountOHCode"));
                mrTemp.setSalesAcCode(DmShared.xNull(rsTemp.getString("pharmacyFinalDiscountAcCode")));
            }
            psTemp.close();
            rsTemp.close();

            String pharmacyFinalDiscountItemCode = DmServer.getCodeViaManualCode(pharmacyFinalDiscountCode, "code", "overheadMast", "itemCode", cnTemp);
            sqlQuery = " SELECT im.code"
                    + " , im.name as itemName"
                    + " , im.hsnCode"
                    + " , im.sku"
                    + " , im.saleUnit "
                    + " , um.gstUnitCode  "
                    + "	FROM itemMast AS im "
                    + "	LEFT JOIN unitMast um ON um.code=im.saleUnit "
                    + "	WHERE im.code = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", pharmacyFinalDiscountItemCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setItemCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                mrTemp.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));
                mrTemp.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnit")));
            }
            psTemp.close();
            rsTemp.close();

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

            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);

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
    public DmResult<PharmacyFinalDiscountEntry_Mdl> getMoveRec(String strKey) {
        DmResult<PharmacyFinalDiscountEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        CardPaymentPopup_Mdl cardPaymentPopup_Mdl = new CardPaymentPopup_Mdl();
        PharmacyFinalDiscountEntry_Mdl mrTemp = new PharmacyFinalDiscountEntry_Mdl();
        dmrTemp.setData(mrTemp);
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT "
                    + "   sb.docId"
                    + " , sb.recId"
                    + " , sb.v_Type"
                    + " , vt.description"
                    + " , sb.v_Date"
                    + " , prvt.category"
                    + " , sb.regDocId"
                    + " , sb.subCode"
                    + " , pr.recId AS regNo"
                    + " , pr.site_Code AS regSiteCode"
                    + " , pr.roomCode"
                    + " , sb.uhid"
                    + " , pu.recId AS UHIDNo"
                    + " , sb.departmentCode"
                    + " , sdm.name AS departmentName"
                    + " , lm.displayName as patientName"
                    + " , sgL.manualCode"
                    + " , sb.preparedBy"
                    + " , sb.u_Name"
                    + " , sb.remark"
                    + " , sb.oh_Amt_Gross"
                    + " , sb.oh_Amt_Net"
                    + " , sb.oh_Amt_BAMT"
                    + " , sb.oh_Amt_RoundOff"
                    + " , sb.oh_Amt_CGST"
                    + " , sb.oh_Amt_SGST"
                    + " , sb.oh_Amt_UGST"
                    + " , sb.oh_Amt_Taxable"
                    + " , sb.recIdPrefix"
                    + " , sb.v_No"
                    + " , prec.v_type as precVType"
                    + " , prec.recid as precRecID"
                    + " , prec.recIdPrefix as precRecIdPrefix"
                    + " , prec.v_No as precRecIdCounter"
                    + " , vtprec.description as precVDescription"
                    + " , vtprec.Category as rcptVTypeCategory"
                    + " , sb.idTypeCode"
                    + " , sb.idNo"
                    + " , idType.name idType"
                    + " , vtprec.attachPaymentMode AS attachPaymentMode"
                    + " , sb.isCancelled"
                    + " , sb.mobile"
                    + " , sb.u_EntDt_LatestLine"
                    + " , sb.partyType"
                    + " , sb.lmCode"
                    + " , lm.address"
                    + " , c.cityName"
                    + " , lm.cityCode"
                    + " , sb.placeOfsupply as stateCode"
                    + " , sm.name as stateName "
                    + "  FROM saleBill sb "
                    + "  LEFT JOIN voucher_type vt ON sb.v_Type=vt.v_Type    "
                    + "  LEFT JOIN LedgerMast AS lm ON lm.code = sb.lmCode "
                    + "  LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  "
                    + "  LEFT JOIN voucher_type prvt ON pr.v_Type=prvt.v_Type    "
                    + "  LEFT JOIN SiteMast prsm ON pr.site_Code=prsm.code  "
                    + "  LEFT JOIN patientuhid pu ON pr.uhid = pu.docId  "
                    + "  LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode  "
                    + "  LEFT JOIN subgroup sgL ON sgL.subCode=lm.subcode  "
                    + "  LEFT JOIN patientReceipt AS prec ON sb.DocId = prec.docId "
                    + "  LEFT JOIN city AS c ON c.citycode = lm.citycode "
                    + "  LEFT JOIN stateMast AS sm ON sm.code = sb.placeOfsupply "
                    + "	 LEFT JOIN hl7Mast as idType on idType.code = sb.idTypeCode "
                    + "	 LEFT JOIN voucher_type vtprec ON prec.v_type = vtprec.v_type "
                    + "  WHERE sb.docID = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setCustomerTypeCode(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setCustomerTypeCode(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setRegRecID(DmShared.xNull(rsTemp.getString("regNo")));
                mrTemp.setRegSiteCode(DmShared.xNull(rsTemp.getString("regSiteCode")));
                mrTemp.setRoomCode(DmShared.xNull(rsTemp.getString("roomCode")));

                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                mrTemp.setUhidDocId(DmShared.xNull(rsTemp.getString("uhid")));
                mrTemp.setUhidRecId(DmShared.xNull(rsTemp.getString("UHIDNo")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("patientName")));

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

                mrTemp.setTaxableAmountToSave(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_Taxable")).doubleValue(), "0.00"));
                mrTemp.setCgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue(), "0.00"));
                mrTemp.setSgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue(), "0.00"));
                mrTemp.setUgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue(), "0.00"));
                mrTemp.setNetAmtToSave(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_Net")).doubleValue(), "0.00"));
//                mrTemp.setReceiptTypeCode(DmShared.xNull(rsTemp.getString("precVType")));
//                mrTemp.setReceiptTypeName(DmShared.xNull(rsTemp.getString("precVDescription")));
//                mrTemp.setReceiptTypeCateory(DmShared.xNull(rsTemp.getString("rcptVTypeCategory")));
                mrTemp.getRecIdRecipt().setRecIdValue(DmShared.xNull(rsTemp.getString("precRecID")));
                mrTemp.getRecIdRecipt().setPrefix(DmShared.xNull(rsTemp.getString("precRecIdPrefix")));
                mrTemp.getRecIdRecipt().setCounter(DmShared.iNull(rsTemp.getString("precRecIdCounter")).longValue());
                mrTemp.setRecptVoucherTypeName(DmShared.xNull(rsTemp.getString("precVDescription")));
                mrTemp.setRecptVoucherTypeCode(DmShared.xNull(rsTemp.getString("precVType")));
                mrTemp.setRecptAttachedPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));
                mrTemp.setIsCancel(DmShared.iNull(rsTemp.getString("isCancelled")).toString());
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));

                if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() > 0) {
                    mrTemp.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(DmShared.iNull(rsTemp.getString("lmCode")).toString(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
                }
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() != 0) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("regNo")));
                    mrTemp.setCustomerTypeCode(DmShared.xNull(rsTemp.getString("category")));
                    mrTemp.setCustomerTypeName(DmShared.xNull(rsTemp.getString("category")));
                } else if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() == 0 && DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (DmShared.iNull(rsTemp.getString("lmCode")).longValue() > 0) {
                        mrTemp.setCustomerTypeCode("Ledger");
                        mrTemp.setCustomerTypeName("Ledger");
                    }
                } else if (DmShared.iNull(rsTemp.getString("regDocId")).longValue() == 0) {
                    mrTemp.setCustomerTypeCode("Walk-In");
                    mrTemp.setCustomerTypeName("Walk-In");
                }

                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " SELECT "
                    + "   sbd.docId"
                    + " , sbd.v_SNo"
                    + " , mm.isBestBefore"
                    + " , sbd.hsnCode"
                    + " , hm.hsn"
                    + " , sbd.itemGSTNature"
                    + " , sbd.itemCode"
                    + " , mm.name AS itemName"
                    + " , sbd.quantity"
                    + " , sbd.rate"
                    + " , sbd.oh_Amt_Net"
                    + " , sbd.oh_Amt_Gross"
                    + " , sbd.oh_at_CGST"
                    + " , sbd.isFooter"
                    + " , mm.sku"
                    + " , umsale.gstUnitCode"
                    + " , sbd.sbDocId"
                    + " , sbl.recId AS mBillNo"
                    + " , sbd.saleUnitCode"
                    + " , sbd.oh_Amt_CGST "
                    + " , sbd.oh_Amt_SGST "
                    + " , sbd.oh_at_SGST"
                    + " , sbd.oh_Amt_Taxable"
                    + " , sbd.oh_Amt_UGST"
                    + " , sbd.oh_at_UGST"
                    + " , sbd.mrp"
                    + " , sbd.skuRate"
                    + " , sbd.skuMrp"
                    + " , sbd.oh_Ac_CGST"
                    + " , sbd.oh_Ac_SGST"
                    + " , sbd.oh_Ac_UGST"
                    + " , sbd.oh_Ac_Sales"
                    + " , mm.itsBasedOn"
                    + " , mm.purchaseUnit"
                    + " , mm.itsBasedOn "
                    + "	from saleBillDetail AS sbd "
                    + "	left join saleBill AS sbl ON sbd.sbDocId = sbl.docId "
                    + "	left join itemMast AS mm ON sbd.itemCode = mm.code "
                    + "	left join unitMast AS umsale ON umsale.code = sbd.saleUnitCode "
                    + "	left join hsnMast AS hm ON sbd.hsnCode = hm.code "
                    + "	where sbd.docId = :docId and isnull(sbd.isFooter, 0) = 0 "
                    + " order by sbd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdDiscountDetails_Mdl detailMdl = new GrdDiscountDetails_Mdl();
                detailMdl.setV_SNo(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                detailMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                detailMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                detailMdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                detailMdl.setHsnName(DmShared.xNull(rsTemp.getString("hsn")));
                detailMdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
//                detailMdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));

                detailMdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                detailMdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));

                detailMdl.setQty(DmServer.vNull(rsTemp.getString("quantity")));
                detailMdl.setSaleUnitCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                detailMdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));

                detailMdl.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                detailMdl.setCgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue(), "0.00"));
                detailMdl.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                detailMdl.setUgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue(), "0.00"));
                detailMdl.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                detailMdl.setSgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue(), "0.00"));
                detailMdl.setTaxableValue(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_Taxable")).doubleValue(), "0.00"));
                detailMdl.setNetValue(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_Net")).doubleValue(), "0.00"));
                detailMdl.setSaleAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_Sales")));
                detailMdl.setCgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_CGST")));
                detailMdl.setSgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_SGST")));
                detailMdl.setUgstAcCode(DmShared.xNull(rsTemp.getString("oh_Ac_UGST")));

                detailMdl.setGstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0 ? DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue()), "0.00"));
                detailMdl.setGstAmt(DmServer.vNull(DmShared.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue() + (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() != 0 ? DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() : DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue())).doubleValue(), "0.00"));

                mrTemp.getGrdDiscountDetails().add(detailMdl);
            }
            psTemp.close();
            rsTemp.close();

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
    public DmResult<String> saveRecord(UserPermission userPermission, PharmacyFinalDiscountEntry_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        dmrTemp.setData("");

        try {

            PharmacyFinalDiscountEntry_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

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
                if (STServer.getBalanceLedgerMast(mrTemp.getLmCode(), siteCode, cnTemp) <= 0) {
                    throw new DmException(0, "No Balance Amount.Cannot Give Discount");
                }
                //<editor-fold defaultstate="collapsed" desc="New Case and DocId Formation">
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                //</editor-fold>
            } else {
                DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

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

            //<editor-fold defaultstate="collapsed" desc="Check whether posted (via EOD) or not">
            STServer.checkEOD("saleBillDetail", "docId", mrTemp.getSharedKeyFieldCode(), cnTemp);
            //</editor-fold>

            if (blnAdd) {
                //<editor-fold defaultstate="collapsed" desc="Insert SaleBill">
                sqlQuery = " insert into saleBill "
                        + " ("
                        + "   docId"
                        + " , v_Type"
                        + " , v_No"
                        + " , recId"
                        + " , v_Prefix"
                        + " , v_Date"
                        + " , regDocId"
                        + " , uhid"
                        + " , remark"
                        + " , u_EntDt"
                        + " , preparedDate"
                        + " , preparedBy"
                        + " , site_Code"
                        + " , oh_Amt_Gross"
                        + " , oh_Amt_Net"
                        + " , oh_Amt_BAMT"
                        + " , oh_Amt_CGST"
                        + " , oh_Amt_SGST"
                        + " , oh_Amt_Taxable"
                        + " , recIdPrefix"
                        + " , oh_Amt_UGST"
                        + " , idTypeCode"
                        + " , idNo"
                        + " , mobile"
                        + " , u_EntDt_LatestLine"
                        + " , partyType"
                        + " , lmCode"
                        + " , subCode"
                        + " , consignee"
                        + " , placeOfSupply"
                        + " )"
                        + " VALUES "
                        + " ("
                        + "   :docId"
                        + " , :vType"
                        + " , :vNo"
                        + " , :recId"
                        + " , :vPrefix"
                        + " , :vDate"
                        + " , :regDocId"
                        + " , :uhid"
                        + " , :remark"
                        + " , :uEntDt"
                        + " , :uEntDt"
                        + " , :userName"
                        + " , :siteCode"
                        + " , :ohAmtGross"
                        + " , :ohAmtNet"
                        + " , :ohAmtBAMT"
                        + " , :ohAmtCGST"
                        + " , :ohAmtSGST"
                        + " , :ohAmtTaxable"
                        + " , :recIdPrefix"
                        + " , :ohAmtUGST"
                        + " , :idTypeCode"
                        + " , :idNo"
                        + " , :mobile"
                        + " , :uEntDt"
                        + " , :partyType"
                        + " , :lmCode"
                        + " , :subCode"
                        + " , :consignee"
                        + " , :placeOfSupply"
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("vNo", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("vPrefix", voucherPrefix);
                psTemp.setString("regDocId", DmShared.foriegnKey(mrTemp.getRegDocId()));
                psTemp.setString("uhid", DmShared.foriegnKey(mrTemp.getUhidDocId()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("userName", DmShared.xNull(userName));
                psTemp.setString("siteCode", DmShared.xNull(siteCode));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyType()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("ohAmtGross", DmServer.vNull(DmShared.vNull(mrTemp.getTaxableAmountToSave()).doubleValue(), "0.00"));
                psTemp.setString("ohAmtCGST", DmServer.vNull(DmShared.vNull(mrTemp.getCgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("ohAmtSGST", DmServer.vNull(DmShared.vNull(mrTemp.getSgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("ohAmtUGST", DmServer.vNull(DmShared.vNull(mrTemp.getUgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("ohAmtTaxable", DmServer.vNull(DmShared.vNull(mrTemp.getTaxableAmountToSave()).doubleValue(), "0.00"));
                psTemp.setString("ohAmtNet", DmServer.vNull(DmShared.vNull(mrTemp.getNetAmtToSave()).doubleValue(), "0.00"));
                psTemp.setString("ohAmtBAMT", DmServer.vNull(DmShared.vNull(mrTemp.getNetAmtToSave()).doubleValue(), "0.00"));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
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
                        + " remark=:remark,"
                        + " idTypeCode=:idTypeCode,"
                        + " idNo=:idNo,"
                        + " mobile=:mobile,"
                        + " oh_Amt_Gross=:oh_Amt_Gross,"
                        + " oh_Amt_CGST=:oh_Amt_CGST,"
                        + " oh_Amt_SGST=:oh_Amt_SGST,"
                        + " oh_Amt_UGST=:oh_Amt_UGST,"
                        + " oh_Amt_Taxable=:oh_Amt_Taxable,"
                        + " oh_Amt_Net=:oh_Amt_Net,"
                        + " oh_Amt_BAMT=:oh_Amt_BAMT,"
                        + " u_Name = :userName "
                        + " WHERE DocID = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("lmCode", DmShared.foriegnKey(mrTemp.getLmCode()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("idTypeCode", DmShared.foriegnKey(mrTemp.getIdTypeCode()));
                psTemp.setString("idNo", DmShared.xNull(mrTemp.getIdNo()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(DmShared.vNull(mrTemp.getTaxableAmountToSave()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(DmShared.vNull(mrTemp.getCgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(DmShared.vNull(mrTemp.getSgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(DmShared.vNull(mrTemp.getUgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(DmShared.vNull(mrTemp.getTaxableAmountToSave()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(DmShared.vNull(mrTemp.getNetAmtToSave()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_BAMT", DmServer.vNull(DmShared.vNull(mrTemp.getNetAmtToSave()).doubleValue(), "0.00"));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSubCode()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));

//</editor-fold>
            }
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Delete From GST Trans And Patient Receipt">
            GstDocument_Mdl gdm = Gst_Server.getGstDocumentDetail((blnAdd ? "" : DmShared.xNull(mrTemp.getSharedKeyFieldCode())), DmShared.xNull(mrTemp.getVoucherTypeCode()), cnTemp);
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="INSERT IN SALE BILL DETAIL">
            sqlQuery = "Delete From saleBillDetail Where DocId= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            int vSNo = 0;
            for (GrdDiscountDetails_Mdl detail_Mdl : mrTemp.getGrdDiscountDetails()) {
                detail_Mdl.setV_SNo(String.valueOf(++vSNo));
                sqlQuery = " INSERT into saleBillDetail "
                        + " ("
                        + "   docId"
                        + " , v_SNo"
                        + " , itemCode"
                        + " , hsnCode"
                        + " , saleUnitCode"
                        + " , quantity"
                        + " , oh_Amt_Net"
                        + " , oh_Amt_CGST"
                        + " , oh_at_CGST"
                        + " , oh_Amt_SGST"
                        + " , oh_at_SGST"
                        + " , oh_at_UGST"
                        + " , oh_Amt_UGST"
                        + " , rate"
                        + " , oh_Amt_Taxable"
                        + " , itemGSTNature"
                        + " , skuQuantity"
                        + " , oh_Ac_Sales"
                        + " , oh_Ac_CGST"
                        + " , oh_Ac_UGST"
                        + " , oh_Ac_SGST"
                        + " , oh_Amt_Gross"
                        + " )"
                        + "  VALUES "
                        + " ("
                        + "   :docId"
                        + " , :v_sNo"
                        + " , :itemCode"
                        + " , :hsnCode"
                        + " , :saleUnitCode"
                        + " , :quantity"
                        + " , :oh_Amt_Net"
                        + " , :oh_Amt_CGST"
                        + " , :oh_at_CGST"
                        + " , :oh_Amt_SGST"
                        + " , :oh_at_SGST"
                        + " , :oh_at_UGST"
                        + " , :oh_Amt_UGST"
                        + " , :rate"
                        + " , :oh_Amt_Taxable"
                        + " , :itemGSTNature"
                        + " , :skuQuantity"
                        + " , :oh_Ac_Sales"
                        + " , :oh_Ac_CGST"
                        + " , :oh_Ac_UGST"
                        + " , :oh_Ac_SGST"
                        + " , :oh_Amt_Gross"
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                psTemp.setString("saleUnitCode", DmShared.foriegnKey(detail_Mdl.getSaleUnitCode()));
                psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQty()));
                psTemp.setString("skuQuantity", DmServer.vNull(detail_Mdl.getQty()));
                psTemp.setString("oh_Ac_SGST", DmShared.foriegnKey(detail_Mdl.getSgstAcCode()));
                psTemp.setString("oh_Ac_UGST", DmShared.foriegnKey(detail_Mdl.getUgstAcCode()));
                psTemp.setString("oh_Ac_CGST", DmShared.foriegnKey(detail_Mdl.getCgstAcCode()));
                psTemp.setString("oh_Ac_Sales", DmShared.foriegnKey(detail_Mdl.getSaleAcCode()));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(DmShared.vNull(detail_Mdl.getTaxableValue()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(DmShared.vNull(detail_Mdl.getNetValue()).doubleValue(), "0.00"));
                psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(DmShared.vNull(detail_Mdl.getCgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(DmShared.vNull(detail_Mdl.getSgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(DmShared.vNull(detail_Mdl.getUgstAmt()).doubleValue(), "0.00"));
                psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                psTemp.setString("rate", DmServer.vNull(DmShared.vNull(detail_Mdl.getRate()).doubleValue(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(DmShared.vNull(detail_Mdl.getTaxableValue()).doubleValue(), "0.00"));

                psTemp.executeUpdate();
                psTemp.close();

                //<editor-fold defaultstate="collapsed" desc="Save in GST Trans">
                /*===================================================================================================================*/
                GstTrans_Mdl gstTrans_Mdl = new GstTrans_Mdl();
                gstTrans_Mdl.setDocId(mrTemp.getSharedKeyFieldCode());
                gstTrans_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                gstTrans_Mdl.setV_Sno(detail_Mdl.getV_SNo());
                gstTrans_Mdl.setV_Type(mrTemp.getVoucherTypeCode());
                gstTrans_Mdl.setGstUnitCode(detail_Mdl.getGstUnitCode());
                gstTrans_Mdl.setHsnCode(detail_Mdl.getHsnCode());
                gstTrans_Mdl.setPartyCode(mrTemp.getSubCode());
                gstTrans_Mdl.setConsignee(mrTemp.getSubCode());
                gstTrans_Mdl.setPartyType(mrTemp.getPartyType());
                gstTrans_Mdl.setPlaceOfSupply(mrTemp.getPlaceOfSupply());
                gstTrans_Mdl.setInvoiceCGST(DmServer.vNull(DmShared.vNull(mrTemp.getCgstAmt()).doubleValue(), "0.00"));
                gstTrans_Mdl.setInvoiceSGST(DmServer.vNull(DmShared.vNull(mrTemp.getSgstAmt()).doubleValue(), "0.00"));
                gstTrans_Mdl.setInvoiceUGST(DmServer.vNull(DmShared.vNull(mrTemp.getUgstAmt()).doubleValue(), "0.00"));
                gstTrans_Mdl.setInvoiceValue(DmServer.vNull(DmShared.vNull(mrTemp.getNetAmtToSave()).doubleValue(), "0.00"));
                gstTrans_Mdl.setItemCgstAmount(DmServer.vNull(DmShared.vNull(detail_Mdl.getCgstAmt()).doubleValue(), "0.00"));
                gstTrans_Mdl.setItemCgstRate(DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                gstTrans_Mdl.setItemSgstRate(DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                gstTrans_Mdl.setItemSgstAmout(DmServer.vNull(DmShared.vNull(detail_Mdl.getSgstAmt()).doubleValue(), "0.00"));
                gstTrans_Mdl.setItemUgstRate(DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                gstTrans_Mdl.setItemUgstAmount(DmServer.vNull(DmShared.vNull(detail_Mdl.getUgstAmt()).doubleValue(), "0.00"));
                gstTrans_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                gstTrans_Mdl.setItemGSTNature(DmShared.xNull(detail_Mdl.getItemGSTNature()));
                gstTrans_Mdl.setItemGrossAmount(DmServer.vNull(DmShared.vNull(detail_Mdl.getAmount()).doubleValue(), "0.00"));
                gstTrans_Mdl.setItemTaxableAmount(DmServer.vNull(DmShared.vNull(detail_Mdl.getTaxableValue()).doubleValue(), "0.00"));
                gstTrans_Mdl.setItemNetAmount(DmServer.vNull(DmShared.vNull(detail_Mdl.getNetValue()).doubleValue(), "0.00"));
                gstTrans_Mdl.setQty(DmServer.vNull(DmShared.vNull("-1").doubleValue()));
                gstTrans_Mdl.setRecID(mrTemp.getSharedKeyFieldName());
                gstTrans_Mdl.setRecIdPrefix(mrTemp.getRecId().getPrefix());
                gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                gstTrans_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
                gstTrans_Mdl.setIsTaxInclusive("1");
                gstTrans_Mdl.setGstType(String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()));
                gstTrans_Mdl.setGstDocTypeCode(DmShared.xNull(gdm.getGstDocTypeCode()));
                gstTrans_Mdl.setGstDocTypeName(DmShared.xNull(gdm.getGstDocTypeName()));
                gstTrans_Mdl.setGstDocNatureCode(DmShared.xNull(gdm.getGstDocNatureCode()));
                gstTrans_Mdl.setGstDocNatureName(DmShared.xNull(gdm.getGstDocNatureName()));
                gstTrans_Mdl.setGstTransNature(String.valueOf(DmEnum.DmGstTransNature.OUTPUT.toInt()));

                Gst_Server.saveGstTrans(gstTrans_Mdl, false, cnTemp, this);

//</editor-fold>
            }

//</editor-fold>
            if (mrTemp.getModifyReason() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReason(), mrTemp.getSharedKeyFieldCode(), userName, cnTemp);
            }

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

    @Override
    public DmResult<String> deleteRecord(UserPermission userPermission, String keyCode, ModifyReason_Mdl modifyReasonMdl, String regDocid) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);
        PharmacyFinalDiscountEntry_Mdl mrTemp;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
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

            //<editor-fold defaultstate="collapsed" desc="Approval Check">
            if (DmShared.iNull(regDocid).longValue() > 0) {
                mrTemp = new PharmacyFinalDiscountEntry_Mdl();
                mrTemp.setSharedKeyFieldCode(keyCode);
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Check whether posted (via EOD) or not">
            STServer.checkEOD("saleBillDetail", "docId", keyCode, cnTemp);
            //</editor-fold>

            CardPaymentPopup_Server.cancel(keyCode, cnTemp);

            Gst_Server.deleteGstTrans(keyCode, cnTemp);

            sqlQuery = "UPDATE saleBill "
                    + " SET "
                    + "   saleBill.u_EntDt_LatestLine = :uEntDt "
                    + " , saleBill.isCancelled = 1 "
                    + " WHERE saleBill.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("docId", keyCode);
            psTemp.executeUpdate();
            psTemp.close();

            if (modifyReasonMdl != null) {
                ModifyReason_Server.save(modifyReasonMdl, keyCode, DmServer.getSsnUserName(this), cnTemp);
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper pageing, boolean blnToEndOfFile) {
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

            sqlQuery = "SELECT "
                    + "   sb.DocId"
                    + " , sb.recId AS billNo"
                    + " , sb.v_Date"
                    + " , pr.recId AS regNo"
                    + " , pu.recId AS UHIDNo"
                    + " , lm.manualCode"
                    + " , pbvt.short_Name"
                    + " , sb.v_no"
                    + " , sb.recId"
                    + " , sb.oh_Amt_Net "
                    + " , pbvt.v_Type "
                    + " , lm.displayName as patientName"
                    + " , CASE WHEN sb.isCancelled = 1 THEN 'Discarded' ELSE '' END as status "
                    + "   FROM saleBill sb "
                    + "   LEFT JOIN voucher_type AS pbvt ON sb.v_Type = pbvt.v_Type "
                    + "   LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid "
                    + "   LEFT JOIN voucher_type AS prvt ON pr.v_Type = prvt.v_Type "
                    + "   LEFT JOIN patientuhid pu ON pr.UHID = pu.docId "
                    + "   LEFT JOIN ledgerMast lm ON lm.code = sb.lmCode "
                    + "   LEFT JOIN voucher_type AS puvt ON pu.v_Type = puvt.v_Type "
                    + "   WHERE sb.site_Code =:siteCode   "
                    + " AND pbvt.category IN (" + vCat + ")"
                    + " AND sb.v_prefix =:vPrefix ";

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
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setUhidNo(DmShared.xNull(rsTemp.getString("UHIDNo")));
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));
                listNode.setStatus(DmShared.xNull(rsTemp.getString("status")));
                listNode.setAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_Amt_Net")).doubleValue()));
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
    public DmResult<LedgerMaster_Mdl> getCustomerDetails(String customerType, String uniqueId, String v_Date) {
        DmResult<LedgerMaster_Mdl> dmrTemp = new DmResult<LedgerMaster_Mdl>();
        LedgerMaster_Mdl lmMdl = new LedgerMaster_Mdl();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            lmMdl.setRegType(DmEnum.DmPartyType.Unregistered.getId());
            if (DmShared.xNull(customerType).equalsIgnoreCase("Walk-In")) {
                lmMdl = LedgerMasterServer.getRecordByManualCode(uniqueId, DmServer.getSsnSiteCode(this), v_Date, cnTemp);
            } else if (DmShared.xNull(customerType).equalsIgnoreCase("Ledger")) {
                PartySearch_Mdl psMdl = PartySearch_SrvImpl.getRecord(uniqueId, "", this, cnTemp);
                lmMdl.setSharedKeyFieldCode(DmShared.xNull(psMdl.getLmCode()));
                lmMdl.setAccountCode(DmShared.xNull(psMdl.getSharedKeyFieldCode()));
                lmMdl.setManualCode(DmShared.xNull(psMdl.getManualCode()));
                lmMdl.setDisplayName(DmShared.xNull(psMdl.getSharedKeyFieldName()));
                lmMdl.setMobile(DmShared.xNull(psMdl.getMobile()));
                lmMdl.setAddress(DmShared.xNull(psMdl.getAddress()));
                lmMdl.setCityCode(DmShared.xNull(psMdl.getCityCode()));
                lmMdl.setCityName(DmShared.xNull(psMdl.getCityName()));
                lmMdl.setStateCode(DmShared.xNull(psMdl.getStateCode()));
                lmMdl.setStateName(DmShared.xNull(psMdl.getStateName()));
                lmMdl.setRegType(DmShared.xNull(psMdl.getPartTypeCode()));
                lmMdl.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(lmMdl.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
            } else {
                sqlQuery = "SELECT"
                        + "   pr.regDocId"
                        + " , pr.recId AS regRecId"
                        + " , pu.patientName"
                        + " , pu.address1"
                        + " , pu.address2"
                        + " , ct.cityCode"
                        + " , ct.cityName"
                        + " , pu.mobile"
                        + " , pu.idNo"
                        + " , pu.docId as uhidDocId "
                        + " , pu.recId AS uhidNo"
                        + " , hl.code AS idTypeCode"
                        + " , hl.name AS idTypeName"
                        + " , lm.code AS lmCode"
                        + " , sm.name as stateName "
                        + " , sm.code as stateCode "
                        + " FROM patientRegistration pr"
                        + " LEFT JOIN patientUHID pu ON pu.docId = pr.uhid"
                        + " LEFT JOIN patientUHID2 pu2 ON pu2.uhid = pu.docId"
                        + " LEFT JOIN city ct ON ct.cityCode = pu.cityCode"
                        + " LEFT JOIN stateMast sm ON sm.code = ct.stateCode"
                        + " LEFT JOIN hl7Mast hl ON hl.code = pu2.idTypeCode"
                        + " LEFT JOIN ledgerMast lm ON lm.regDocId = pr.regDocId"
                        + " WHERE pr.isCancelled = 0"
                        + " AND pr.recId = :regRecId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("regRecId", DmShared.xNull(uniqueId));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    lmMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("lmCode")));
                    lmMdl.setRegDocId(DmShared.xNull(rsTemp.getString("regDocId")));
                    lmMdl.setManualCode(DmShared.xNull(rsTemp.getString("regRecId")));
                    lmMdl.setDisplayName(DmShared.xNull(rsTemp.getString("patientName")));
                    lmMdl.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                    lmMdl.setIdTypeCode(DmShared.xNull(rsTemp.getString("idTypeCode")));
                    lmMdl.setIdTypeName(DmShared.xNull(rsTemp.getString("idTypeName")));
                    lmMdl.setUhidDocid(DmShared.xNull(rsTemp.getString("uhidDocId")));
                    lmMdl.setUhidRecId(DmShared.xNull(rsTemp.getString("uhidNo")));
                    lmMdl.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                    lmMdl.setStateName(DmShared.xNull(rsTemp.getString("stateName")));

                    lmMdl.setIdNo(DmShared.xNull(rsTemp.getString("idNo")));
                    lmMdl.setAddress(DmShared.xNull(rsTemp.getString("address1")) + " " + DmShared.xNull(rsTemp.getString("address2")));
                    lmMdl.setCityCode(DmShared.xNull(rsTemp.getString("cityCode")));
                    lmMdl.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                    lmMdl.setPharmacyBalance(DmServer.vNull(STServer.getBalanceLedgerMast(lmMdl.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp), "0.00"));
                }
                psTemp.close();
                rsTemp.close();
            }
            dmrTemp.setData(lmMdl);

            if (dmrTemp.getData() == null || DmShared.xNull(dmrTemp.getData().getDisplayName()).trim().equals("")) {
                throw new Exception("No Record Found. Try Again.");
            }

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
    public DmResult<List<HSN_TAX_WISE_PopUp_Mdl>> getSaleRecords(String v_Date, String lmCode) {
        DmResult<List<HSN_TAX_WISE_PopUp_Mdl>> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery = "", join = "", quantity = "0";
        List<HSN_TAX_WISE_PopUp_Mdl> lstTemp = new ArrayList<HSN_TAX_WISE_PopUp_Mdl>();
        HSN_TAX_WISE_PopUp_Mdl node;
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                condition += " AND sb.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }

            if (!DmShared.xNull(lmCode).equalsIgnoreCase("")) {
                condition += " AND sb.lmCode = :lmCode";
            } else {
                condition += " AND 1 = 2";
            }

            sqlQuery = " SELECT (ISNULL(sbd.oh_At_SGST,0) + ISNULL(sbd.oh_At_UGST,0) + ISNULL(sbd.oh_At_CGST,0) + ISNULL(sbd.oh_At_IGST,0))  AS taxRate"
                    + ", MAX(hm.hsn) as hsn "
                    + ", MAX(sbd.oh_Ac_CGST) as oh_Ac_CGST"
                    + ", MAX(sbd.oh_Ac_SGST) as oh_Ac_SGST"
                    + ", MAX(sbd.oh_Ac_UGST) as oh_Ac_UGST"
                    + ", MAX(sbd.itemGstNature) as itemGstNature"
                    + ", sbd.hsnCode "
                    + ", MAX(ISNULL(sbd.oh_At_SGST,0)) as sgstPer "
                    + ", MAX(ISNULL(sbd.oh_At_UGST,0)) as ugstPer "
                    + ", MAX(ISNULL(sbd.oh_At_CGST,0)) as cgstPer "
                    + ", MAX(ISNULL(sbd.oh_At_IGST,0)) as igstPer "
                    + ", SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_Taxable,0) ELSE ISNULL(sbd.oh_Amt_Taxable,0) END) AS taxableAmount"
                    + ", SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_Net,0) ELSE ISNULL(sbd.oh_Amt_Net,0) END) AS netAmount"
                    + ", SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_SGST,0) ELSE ISNULL(sbd.oh_Amt_SGST,0) END) AS SGST"
                    + ", SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_UGST,0) ELSE ISNULL(sbd.oh_Amt_UGST,0) END) AS UGST"
                    + ", SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_CGST,0) ELSE ISNULL(sbd.oh_Amt_CGST,0) END) AS CGST"
                    + ", SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_IGST,0) ELSE ISNULL(sbd.oh_Amt_IGST,0) END) AS IGST"
                    + ", SUM("
                    + "       (CASE WHEN vt.category ='" + pvc.PHARMACY_BILL_RETURN + "' THEN 0-isNull(sbd.oh_Amt_SGST,0) ELSE isNull(sbd.oh_Amt_SGST,0) END)+"
                    + "       (CASE WHEN vt.category ='" + pvc.PHARMACY_BILL_RETURN + "' THEN 0-isNull(sbd.oh_Amt_CGST,0) ELSE isNull(sbd.oh_Amt_CGST,0) END)+"
                    + "       (CASE WHEN vt.category ='" + pvc.PHARMACY_BILL_RETURN + "' THEN 0-isNull(sbd.oh_Amt_UGST,0) ELSE isNull(sbd.oh_Amt_UGST,0) END)+"
                    + "       (CASE WHEN vt.category ='" + pvc.PHARMACY_BILL_RETURN + "' THEN 0-isNull(sbd.oh_Amt_IGST,0) ELSE isNull(sbd.oh_Amt_IGST,0) END)"
                    + "	 ) as totalTaxAmount"
                    + " FROM   saleBillDetail AS sbd"
                    + " LEFT JOIN salebill AS sb ON sbd.docid=sb.docId"
                    + " LEFT JOIN ledgerMast AS lm ON lm.code=sb.lmCode"
                    + " LEFT JOIN voucher_type AS vt ON sb.v_type=vt.v_type"
                    + " LEFT JOIN hsnMast AS hm ON hm.code=sbd.hsnCode"
                    + " WHERE  vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "') "
                    + " AND ISNULL(sb.isCancelled, 0) = 0 "
                    + " AND sb.site_Code = :site_Code " + condition
                    + " GROUP BY sbd.hsnCode,(ISNULL(sbd.oh_At_SGST,0) + ISNULL(sbd.oh_At_UGST,0) + ISNULL(sbd.oh_At_CGST,0) + ISNULL(sbd.oh_At_IGST,0))"
                    + " HAVING (SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN 0 - ISNULL(sbd.oh_Amt_Net,0) ELSE ISNULL(sbd.oh_Amt_Net,0) END)) > 0 ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.dNull(v_Date).equalsIgnoreCase("")) {
                psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            if (!DmShared.xNull(lmCode).equalsIgnoreCase("")) {
                psTemp.setString("lmCode", lmCode);
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new HSN_TAX_WISE_PopUp_Mdl();
                node.setCgstPer(DmServer.vNull(rsTemp.getString("cgstPer")));
                node.setSgstPer(DmServer.vNull(rsTemp.getString("sgstPer")));
                node.setIgstPer(DmServer.vNull(rsTemp.getString("igstPer")));
                node.setUgstPer(DmServer.vNull(rsTemp.getString("ugstPer")));
                node.setUgstAmt(DmServer.vNull(rsTemp.getString("UGST")));
                node.setCgstAmt(DmServer.vNull(rsTemp.getString("CGST")));
                node.setSgstAmt(DmServer.vNull(rsTemp.getString("SGST")));

                node.setUgstAcCode(DmServer.vNull(rsTemp.getString("oh_Ac_UGST")));
                node.setCgstAcCode(DmServer.vNull(rsTemp.getString("oh_Ac_CGST")));
                node.setSgstAcCode(DmServer.vNull(rsTemp.getString("oh_Ac_SGST")));

                node.setSgstUgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("SGST")).doubleValue() + DmShared.vNull(rsTemp.getString("UGST")).doubleValue()));
                node.setSgstUgstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("sgstPer")).doubleValue() + DmShared.vNull(rsTemp.getString("ugstPer")).doubleValue()));
                node.setGstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("SGST")).doubleValue() + DmShared.vNull(rsTemp.getString("UGST")).doubleValue() + DmShared.vNull(rsTemp.getString("CGST")).doubleValue() + DmShared.vNull(rsTemp.getString("IGST")).doubleValue()));
                node.setGstPer(DmShared.xNull(rsTemp.getString("taxRate")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsnName(DmShared.xNull(rsTemp.getString("hsn")));
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGstNature")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("taxRate")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("hsn")));
                node.setIgstAmt(DmServer.vNull(rsTemp.getString("IGST")));
                node.setTaxableValue(DmServer.vNull(rsTemp.getString("taxableAmount")));
                node.setNetValue(DmServer.vNull(DmShared.vNull(node.getTaxableValue()).doubleValue() + DmShared.vNull(rsTemp.getString("totalTaxAmount")).doubleValue()));
                node.setIsChecked("0");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDiscountReason(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select code, name from discountReasonMast ";
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

}
