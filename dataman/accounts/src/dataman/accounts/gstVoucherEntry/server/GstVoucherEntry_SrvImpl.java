package dataman.accounts.gstVoucherEntry.server;

import dataman.accounts.gstVoucherEntry.client.GstVoucherEntry;
import dataman.accounts.gstVoucherEntry.shared.DefaultValue_Mdl;
import dataman.accounts.gstVoucherEntry.shared.Find_Mdl;
import dataman.accounts.gstVoucherEntry.shared.GridAcDetailsMdl;
//import dataman.accounts.gstVoucherEntry.shared.GridHSNDetailsMdl;
import dataman.accounts.gstVoucherEntry.shared.GstVoucherEntry_Mdl;
import dataman.accounts.gstVoucherEntry.shared.GstVoucherEntry_Srv;
import dataman.accounts.gstVoucherEntry.shared.GstVoucherEntry_Vld;
import dataman.accounts.gstVoucherEntry.shared.SaveRecord_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHAccountNameGrid_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHAccountName_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHHSN_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHRefVoucher_Mdl;
import dataman.accounts.gstVoucherEntry.shared.TxtHVoucherType_Mdl;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.server.Gst_Server;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.itemAdjustment.server.ItemAdjustment_SrvImpl;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GstVoucherEntry_SrvImpl extends DmRSSPrivate implements GstVoucherEntry_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private String voucherTypefieldName = "lm.V_Type", voucherDateFieldName = "lm.V_Date";
    int taxVSno, maxLedVSNo;

    @Override
    public String[] getServerDateTime() {
        return DmServer.getServerDateTime();
    }

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String voucherCategory) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery, sqlCondition, condition = "";
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " AND vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "', '" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')";

            sqlQuery = "Select LM.DocId As SearchCode ";
            sqlQuery += "From LedgerM LM ";
            sqlQuery += "Left Join Voucher_Type VT On LM.V_Type = VT.V_Type ";
            sqlQuery += " Where LM.Site_Code = '" + DmServer.getSsnSiteCode(this) + "'";
            sqlQuery += " AND LM.V_Prefix = '" + DmServer.getSsnVoucherPrefix(this) + "'";
            sqlQuery += condition;

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlQuery += "Order By lm.V_Date Desc,lm.V_Type,lm.v_no Desc,lm.recid ";

            rsTemp = cnTemp.createStatement().executeQuery(sqlQuery);

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
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
    public DmResult<GstVoucherEntry_Mdl> getMoveRec(String strKey) {
        DmResult<GstVoucherEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        GstVoucherEntry_Mdl mrTemp = new GstVoucherEntry_Mdl();
        GridAcDetailsMdl acDetailsData;
//        GridHSNDetailsMdl hsnDetailsData;

        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select "
                    + "   LM.DocId"
                    + " , LM.V_Type"
                    + " , LM.V_Prefix"
                    + " , LM.Site_Code"
                    + " , LM.V_Date"
                    + " , LM.SubCode"
                    + " , SG.Name As AcName"
                    + " , LM.PostedBy"
                    + " , LM.RecId"
                    + " , lm.v_No"
                    + " , lm.recIdPrefix"
                    + " , LM.Narration"
                    + " , LM.U_Name"
                    + " , LM.PreparedBy"
                    + " , LM.PostedBy"
                    + " , VT.Description"
                    + " , VT.Category"
                    + " , sgn.manualCode As SGNature"
                    + " , LM.wtaCode"
                    + " , LM.remark"
                    + " , LM.consignee"
                    + " , LM.consigneeType"
                    + " , LM.placeOfSupply"
                    + " , sm.name as placeOfSupplyName "
                    + " , lm.gstReasonCode"
                    + " , grm.name AS gstReasonName"
                    + " , lm.refDocId"
                    + " , lm.refV_SNo "
                    + " , l.v_Type AS refV_Type"
                    + " , l.v_Date AS refV_Date"
                    + " , l.recId AS refRecId"
                    + " , CASE WHEN ISNULL(l.amtDr,0) > 0 THEN l.amtDr ELSE l.amtCr END AS refDocValue "
                    + " From LedgerM LM "
                    + " Left Join SubGroup SG On LM.SubCode = SG.SubCode "
                    + " Left Join Voucher_Type VT On VT.V_Type = LM.v_Type "
                    + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code "
                    + " LEFT JOIN gstReasonMast grm ON grm.code = lm.gstReasonCode "
                    + " LEFT JOIN stateMast sm ON sm.code = lm.placeOfSupply"
                    + " LEFT JOIN ledger l ON l.docId = lm.refDocId AND l.v_SNo = lm.refV_SNo "
                    + " Where LM.DocId = :docId "
                    + " Order By lm.V_Date,lm.V_Type,lm.v_no,lm.recid ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecId")));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("V_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("Description")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("Category")));
                mrTemp.setGstReasonCode(DmShared.xNull(rsTemp.getString("gstReasonCode")));
                mrTemp.setGstReasonName(DmShared.xNull(rsTemp.getString("gstReasonName")));
                mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setConsigneeType(DmShared.xNull(rsTemp.getString("consigneeType")));
                mrTemp.setPlaceOfSupplyCode(DmShared.xNull(rsTemp.getString("placeOfSupply")));
                mrTemp.setPlaceOfSupplyName(DmShared.xNull(rsTemp.getString("placeOfSupplyName")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("V_Date")));
                mrTemp.setAccountName(DmShared.xNull(rsTemp.getString("AcName")));
                mrTemp.setAccountNameCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setAccountNameNature(DmShared.xNull(rsTemp.getString("SGNature")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setRefDocId(DmShared.xNull(rsTemp.getString("refDocId")));
                mrTemp.setRefV_SNo(DmShared.xNull(rsTemp.getString("refV_SNo")));
                mrTemp.setRefV_Type(DmShared.xNull(rsTemp.getString("refV_Type")));
                mrTemp.setRefDocValue(DmServer.vNull(rsTemp.getString("refDocValue"), "0.00"));
                mrTemp.setRefRecId(DmShared.xNull(rsTemp.getString("refRecId")));
                mrTemp.setRefV_Date(DmShared.dNull(rsTemp.getDate("refV_Date")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select "
                    + "   CT.CityName"
                    + " , LG.RefNo"
                    + " , LG.RefDate"
                    + " , LG.SubCode"
                    + " , SG.Name As AcName"
                    + " , SG.ManualCode \n"
                    + " , sgn.manualCode As SGNature"
                    + " , LG.V_SNo"
                    + " , LG.AmtDr"
                    + " , LG.AmtCr"
                    + " , LG.remark as Narration"
                    + " , ct.stateCode AS acStateCode  \n"
                    + " , sg.partyType AS acPartyType "
                    + " , sg.gstin AS acGstin "
                    + " , sg.gstinDate AS acGstinDate  \n"
                    + " , lgst.docId"
                    + " , lgst.v_SNo"
                    + " , lgst.hsnCode"
                    + " , lgst.qty"
                    + " , lgst.rate \n"
                    + " , lgst.grossAmt"
                    + " , lgst.cgstPer"
                    + " , lgst.cgstAmt"
                    + " , lgst.sgstPer"
                    + " , lgst.sgstAmt \n"
                    + " , lgst.igstPer"
                    + " , lgst.igstAmt"
                    + " , lgst.ugstPer"
                    + " , lgst.ugstAmt"
                    + " , lgst.taxableValue \n"
                    + " , lgst.roundOffGST"
                    + " , lgst.isRcm"
                    + " , lgst.netAmt"
                    + " , lgst.gstType"
                    + " , hm.itemGSTNature \n"
                    + " , hm.name as hsnName,lgst.netAmt"
                    + " , hm.hsn,LGST.cgstSubCode,CGST.name as cgstAcName,LGST.sgstSubCode\n"
                    + " , SGST.name as sgstAcName,LGST.igstSubCode,IGST.name as igstAcName\n"
                    + " From ledgerGst lgst  \n"
                    + " LEFT JOIN Ledger LG on lg.docID=lgst.docID and lg.v_Sno=lgst.v_Sno\n"
                    + " Left Join SubGroup SG On LG.SubCode=SG.SubCode  \n"
                    + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code  \n"
                    + " Left Join SubGroup TDF On TDF.SubCode=LG.TDSDeductFrom  \n"
                    + " Left Join SubGroup CGST On CGST.SubCode=LGST.cgstSubCode \n"
                    + " Left Join SubGroup SGST On SGST.SubCode=LGST.sgstSubCode \n"
                    + " Left Join SubGroup IGST On IGST.SubCode=LGST.igstSubCode \n"
                    + " Left Join City CT On CT.CityCode=SG.CityCode   \n"
                    + " LEFT JOIN hsnMast hm ON hm.code = lgst.hsnCode  \n"
                    + " Where lgst.DocId = :docId ";
//                    + " Where LG.DocId = :docId And isNull(LG.isSystem_Generated,'0')='0'  \n"
//                    + " AND LG.V_SNo <> 1";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                acDetailsData = new GridAcDetailsMdl();
                mrTemp.setReferenceNo(DmShared.xNull(rsTemp.getString("RefNo")));
                mrTemp.setReferenceDate(DmShared.dNull(rsTemp.getDate("RefDate")));

                acDetailsData.setvSNo(DmServer.vNull(rsTemp.getString("V_SNo"), "0"));

                acDetailsData.setAccNatureManualCode(DmShared.xNull(rsTemp.getString("SGNature")));
                if (DmShared.xNull(rsTemp.getString("CityName")).equalsIgnoreCase("")) {
                    acDetailsData.setAccName(DmShared.xNull(rsTemp.getString("AcName")));
                } else {
                    acDetailsData.setAccName(DmShared.xNull(rsTemp.getString("AcName")) + " - " + DmShared.xNull(rsTemp.getString("CityName")));
                }

                acDetailsData.setAccCode(DmShared.xNull(rsTemp.getString("SubCode")));
                acDetailsData.setAccMCode(DmShared.xNull(rsTemp.getString("ManualCode")));

                acDetailsData.setAccStateCode(DmShared.xNull(rsTemp.getString("acStateCode")));
                acDetailsData.setAccPartyType(DmShared.xNull(rsTemp.getString("acPartyType")));
                acDetailsData.setAccGstin(DmShared.xNull(rsTemp.getString("acGstin")));
                acDetailsData.setAccGstinDate(DmShared.dNull(rsTemp.getString("acGstinDate")));

                acDetailsData.setNarration(DmShared.xNull(rsTemp.getString("Narration")));

                if (DmShared.vNull(rsTemp.getString("AmtDr")) > 0) {
                    acDetailsData.setDebit(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                    acDetailsData.setAmount(DmServer.vNull(rsTemp.getString("netAmt"), "0.00"));
                }

                if (DmShared.vNull(rsTemp.getString("AmtCr")) > 0) {
                    acDetailsData.setCredit(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                    acDetailsData.setAmount(DmServer.vNull(rsTemp.getString("netAmt"), "0.00"));
                }

                //==========================================HSN Detail=========================================//
                acDetailsData.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                acDetailsData.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("v_SNo")));
                acDetailsData.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                acDetailsData.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));
                acDetailsData.setHsnSac(DmShared.xNull(rsTemp.getString("hsn")));
                acDetailsData.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                acDetailsData.setQty(DmShared.xNull(rsTemp.getString("qty")));
                acDetailsData.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                acDetailsData.setGrossAmt(DmServer.vNull(rsTemp.getString("grossAmt"), "0.00"));
                acDetailsData.setCgstPer(DmServer.vNull(rsTemp.getString("cgstPer"), "0.00"));
                acDetailsData.setCgstAmt(DmServer.vNull(rsTemp.getString("cgstAmt"), "0.00"));
                acDetailsData.setSgstPer(DmServer.vNull(rsTemp.getString("sgstPer"), "0.00"));
                acDetailsData.setSgstAmt(DmServer.vNull(rsTemp.getString("sgstAmt"), "0.00"));
                acDetailsData.setIgstPer(DmServer.vNull(rsTemp.getString("igstPer"), "0.00"));
                acDetailsData.setIgstAmt(DmServer.vNull(rsTemp.getString("igstAmt"), "0.00"));
                acDetailsData.setUgstPer(DmServer.vNull(rsTemp.getString("ugstPer"), "0.00"));
                acDetailsData.setUgstAmt(DmServer.vNull(rsTemp.getString("ugstAmt"), "0.00"));
                acDetailsData.setSgstUgstPer(DmServer.vNull(DmShared.vNull(rsTemp.getString("ugstPer")).doubleValue() + DmShared.vNull(rsTemp.getString("sgstPer")).doubleValue(), "0.00"));
                acDetailsData.setSgstUgstAmt(DmServer.vNull(DmShared.vNull(rsTemp.getString("ugstAmt")).doubleValue() + DmShared.vNull(rsTemp.getString("sgstAmt")).doubleValue(), "0.00"));
                acDetailsData.setTaxableAmt(DmServer.vNull(rsTemp.getString("taxableValue"), "0.00"));
                acDetailsData.setRoundOffGST(DmShared.xNull(rsTemp.getString("roundOffGST")));
                acDetailsData.setIsRCM(DmShared.iNull(rsTemp.getString("isRcm")).toString());
                acDetailsData.setNetAmt(DmServer.vNull(rsTemp.getString("netAmt")));

                acDetailsData.setcGstAcNameTag(DmShared.xNull(rsTemp.getString("cgstSubCode")));
                acDetailsData.setcGstAcNameText(DmShared.xNull(rsTemp.getString("cgstAcName")));
                acDetailsData.setsGstAcNameTag(DmShared.xNull(rsTemp.getString("sgstSubCode")));
                acDetailsData.setsGstAcNameText(DmShared.xNull(rsTemp.getString("sgstAcName")));
                acDetailsData.setiGstAcNameTag(DmShared.xNull(rsTemp.getString("igstSubCode")));
                acDetailsData.setiGstAcNameText(DmShared.xNull(rsTemp.getString("igstAcName")));

                if (DmShared.xNull(mrTemp.getGstTypeCode()).equalsIgnoreCase("")) {
                    mrTemp.setGstTypeCode(DmShared.iNull(rsTemp.getString("gstType")).toString());
                    if (DmShared.iNull(rsTemp.getString("gstType")).intValue() == DmEnum.DmGSTType.INPUT.toInt()) {
                        mrTemp.setGstTypeName(DmShared.xNull(DmEnum.DmGSTType.INPUT.toString()));
                    } else if (DmShared.iNull(rsTemp.getString("gstType")).intValue() == DmEnum.DmGSTType.OUTPUT.toInt()) {
                        mrTemp.setGstTypeName(DmShared.xNull(DmEnum.DmGSTType.OUTPUT.toString()));
                    } else if (DmShared.iNull(rsTemp.getString("gstType")).intValue() == DmEnum.DmGSTType.VOID.toInt()) {
                        mrTemp.setGstTypeName(DmShared.xNull(DmEnum.DmGSTType.VOID.toString()));
                    }
                }

                mrTemp.getGrdAcDetails().add(acDetailsData);

                //======================================< Get Adjustments >======================================//
                List<GrdItemAdjustment_Mdl> lstItemAdjustments = ItemAdjustment_SrvImpl.getItemAdjustments(mrTemp.getSharedKeyFieldCode(), acDetailsData.getvSNo(), cnTemp);
                if (lstItemAdjustments != null) {
                    acDetailsData.setLstItemAdjustments(lstItemAdjustments);
                }
                //======================================< *************** >======================================//
            }
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

    @Override
    public DmResult<SaveRecord_Mdl> saveRecord(UserPermission userPermission, GstVoucherEntry_Mdl mrTemp) {
        DmResult<SaveRecord_Mdl> dmrTemp = new DmResult();
        SaveRecord_Mdl srmTemp = new SaveRecord_Mdl();
        List<GridAcDetailsMdl> listAcDetailsData;
//        List<GridHSNDetailsMdl> listHsnDetailsData;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, contraTextOther = "", contra = "", contraTextJV = "", narrationTemp = "";
        ResultSet rsTemp = null;
        int vSNo = 0, ledgerGstVSNo = 0;
        boolean autoApprove = false;

        try {
            GstVoucherEntry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            autoApprove = DmShared.haveControlPermission(userPermission, "", "", GstVoucherEntry.approvedByBlog);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getVoucherPrefix(), cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getVoucherPrefix(), cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getVoucherPrefix(), cnTemp, this, true);

                sqlQuery = "Select Count(*) As Cnt From DataAudit Where DocId=:DocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Select Count(DocId) As Cnt From LedgerM Where DocId=:DocId And isNull(PostedBy,'')<>''";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
                }
                rsTemp.close();
                psTemp.close();

            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            //*==============================<< Check for duplicate party reference no >>====================================*/
            if (mrTemp.getVoucherCategory().equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
                sqlQuery = " SELECT COUNT(l.docid) AS cnt "
                        + " FROM ledger l "
                        + " INNER JOIN voucher_type vt ON vt.v_Type = l.v_Type "
                        + " WHERE  l.refNo = :refNo "
                        + " AND l.subCode=:subCode "
                        + " AND l.docId != :docId "
                        + " AND vt.category = '" + lvc.CREDIT_INVOICE_GST + "' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getReferenceNo()));
                psTemp.setString("subCode", DmShared.xNull(mrTemp.getAccountNameCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                        throw new DmException(0, "Entry already exists with this Reference Number.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            listAcDetailsData = mrTemp.getGrdAcDetails();
//            listHsnDetailsData = mrTemp.getGrdHsnDetails();
            //================================================

            //================================================
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From Stock Where DocId = :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From LedgerCostCenter Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from gstTrans >>====================================*/
            Gst_Server.deleteGstTrans(mrTemp.getSharedKeyFieldCode(), cnTemp);
            AccountAdjustment_Server.deleteRecord(mrTemp.getSharedKeyFieldCode(), cnTemp);
            ItemAdjustment_SrvImpl.deleteRecord(mrTemp.getSharedKeyFieldCode(), cnTemp);

            sqlQuery = "Delete From LedgerGst Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From Ledger Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="SAVE">
            //<editor-fold defaultstate="collapsed" desc="LEDGERM">
            if (blnAdd) {
                sqlQuery = "Insert Into LedgerM "
                        + " ("
                        + "   DocId"
                        + " , V_Type"
                        + " , V_Prefix"
                        + " , Site_Code"
                        + " , V_No"
                        + " , V_Date"
                        + " , SubCode"
                        + " , Narration"
                        + " , PostedBy"
                        + " , RecId"
                        + " , recIdPrefix"
                        + " , U_EntDt"
                        + " , PreparedBy"
                        + " , wtaCode"
                        + " , remark"
                        + " , gstReasonCode"
                        + " , consignee"
                        + " , consigneeType "
                        + " , placeOfSupply "
                        + " , refDocId "
                        + " , refV_SNo "
                        + " ) "
                        + "Values "
                        + " ("
                        + "   :DocId"
                        + " , :V_Type"
                        + " , :V_Prefix"
                        + " , :Site_Code"
                        + " , :V_No"
                        + " , :V_Date"
                        + " , :SubCode"
                        + " , :Narration"
                        + " , :PostedBy"
                        + " , :RecId"
                        + " , :recIdPrefix"
                        + " , :uEntDt"
                        + " , :preparedBy"
                        + " , :wtaCode"
                        + " , :remark"
                        + " , :gstReasonCode"
                        + " , :consignee"
                        + " , :consigneeType "
                        + " , :placeOfSupply "
                        + " , :refDocId "
                        + " , :refV_SNo "
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("consigneeType", DmShared.foriegnKey(mrTemp.getConsigneeType()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupplyCode()));
                psTemp.setString("Narration", "");
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTemp.getGstReasonCode()));
                psTemp.setString("refDocId", DmShared.foriegnKey(mrTemp.getRefDocId()));
                psTemp.setString("refV_SNo", DmShared.foriegnKey(mrTemp.getRefV_SNo()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            } else {
                sqlQuery = "update LedgerM Set "
                        + "V_Date=:V_Date,"
                        + "SubCode=:SubCode,"
                        + "consignee=:consignee,"
                        + "consigneeType=:consigneeType,"
                        + "placeOfSupply=:placeOfSupply,"
                        + "gstReasonCode = :gstReasonCode, "
                        + "refDocId = :refDocId, "
                        + "refV_SNo = :refV_SNo, "
                        + "Narration=:Narration,"
                        + "PostedBy=:PostedBy,"
                        + "transferDt=:transferDt,"
                        + "U_Name=:uName,"
                        + "remark=:remark, "
                        + "U_EntDt=:uEntDt "
                        + "Where DocId=:DocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("consigneeType", DmShared.foriegnKey(mrTemp.getConsigneeType()));
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupplyCode()));
                psTemp.setString("Narration", "");
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setString("transferDt", null);
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("gstReasonCode", DmShared.foriegnKey(mrTemp.getGstReasonCode()));
                psTemp.setString("refDocId", DmShared.foriegnKey(mrTemp.getRefDocId()));
                psTemp.setString("refV_SNo", DmShared.foriegnKey(mrTemp.getRefV_SNo()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Calculate total Tax values for Insertion in GstTrans">
            double invoiceUgst = 0;
            double invoiceSgst = 0;
            double invoiceCgst = 0;
            double invoiceIgst = 0;
            double invoiceNetAmt = 0;
            for (GridAcDetailsMdl gstMdl : listAcDetailsData) {
                if (DmShared.iNull(gstMdl.getHsnCode()).longValue() != 0) {
                    invoiceUgst += DmShared.vNull(gstMdl.getUgstAmt());
                    invoiceSgst += DmShared.vNull(gstMdl.getSgstAmt());
                    invoiceCgst += DmShared.vNull(gstMdl.getCgstAmt());
                    invoiceIgst += DmShared.vNull(gstMdl.getIgstAmt());
                    invoiceNetAmt += DmShared.vNull(gstMdl.getNetAmt());
                }
            }
            //</editor-fold>

//<editor-fold defaultstate="collapsed" desc="LEDGER">
            vSNo = 1;
            maxLedVSNo = 1;
            for (int i = 0; i < listAcDetailsData.size(); i++) {
                if (!DmShared.xNull(listAcDetailsData.get(i).getAccCode()).equalsIgnoreCase("")) {
                    if (!contraTextOther.equalsIgnoreCase("")) {
                        contraTextOther = contraTextOther + "\n";
                    }
                    if (DmShared.vNull(listAcDetailsData.get(i).getDebit()) > 0) {
                        contraTextOther = contraTextOther + DmServer.prepareContraText(listAcDetailsData.get(i).getAccName(), DmShared.vNull(listAcDetailsData.get(i).getDebit()), "Dr");
                    } else {
                        contraTextOther = contraTextOther + DmServer.prepareContraText(listAcDetailsData.get(i).getAccName(), DmShared.vNull(listAcDetailsData.get(i).getCredit()), "Cr");
                    }

                    contra = mrTemp.getAccountNameCode();

                    if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)
                            || mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
                        contraTextJV = DmServer.prepareContraText(mrTemp.getAccountName(), DmShared.vNull(listAcDetailsData.get(i).getDebit()), "Cr");
                    } else {
                        contraTextJV = DmServer.prepareContraText(mrTemp.getAccountName(), DmShared.vNull(listAcDetailsData.get(i).getCredit()), "Dr");
                    }

                    if (!DmShared.xNull(mrTemp.getRemark().trim()).equalsIgnoreCase("")) {
                        narrationTemp = DmShared.xNull(mrTemp.getRemark()) + "\n";
                    }
                    narrationTemp = narrationTemp + DmShared.xNull(listAcDetailsData.get(i).getNarration());
                    maxLedVSNo++;
                    ledgerGstVSNo = maxLedVSNo;
                    sqlQuery = "Insert Into Ledger ("
                            + "   DocId"
                            + " , RecId"
                            + " , recIdPrefix"
                            + " , V_No"
                            + " , V_SNo"
                            + " , V_Date"
                            + " , SubCode"
                            + " , ContraSub"
                            + " , AmtDr"
                            + " , AmtCr"
                            + " , remark"
                            + " , V_Type"
                            + " , V_Prefix"
                            + " , Site_Code"
                            + " , ContraText"
                            + " , RefNo"
                            + " , RefDate"
                            + " , narration"
                            + " ) "
                            + "Values"
                            + " ("
                            + "   :DocId"
                            + " , :RecId"
                            + " , :recIdPrefix"
                            + " , :V_No"
                            + " , :V_SNo"
                            + " , :V_Date"
                            + " , :SubCode"
                            + " , :ContraSub"
                            + " , :AmtDr"
                            + " , :AmtCr"
                            + " , :remark"
                            + " , :V_Type"
                            + " , :V_Prefix"
                            + " , :Site_Code"
                            + " , :ContraText"
                            + " , :RefNo"
                            + " , :RefDate"
                            + " , :narration"
                            + ")";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(maxLedVSNo, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("SubCode", DmShared.foriegnKey(listAcDetailsData.get(i).getAccCode()));
                    psTemp.setString("ContraSub", DmShared.foriegnKey(contra));
//                    psTemp.setString("AmtDr", DmServer.vNull(listAcDetailsData.get(i).getDebit(), "0.00"));
//                    psTemp.setString("AmtCr", DmServer.vNull(listAcDetailsData.get(i).getCredit(), "0.00"));
                    if (DmShared.vNull(listAcDetailsData.get(i).getDebit()).doubleValue() > 0) {
                        psTemp.setString("AmtDr", DmServer.vNull(listAcDetailsData.get(i).getTaxableAmt(), "0.00"));
                        psTemp.setString("AmtCr", "0");
                    } else {
                        psTemp.setString("AmtDr", "0");
                        psTemp.setString("AmtCr", DmServer.vNull(listAcDetailsData.get(i).getTaxableAmt(), "0.00"));
                    }
                    psTemp.setString("remark", listAcDetailsData.get(i).getNarration());
                    psTemp.setString("narration", narrationTemp);
                    psTemp.setString("V_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                    psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                    psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("ContraText", contraTextJV);
                    psTemp.setString("RefNo", mrTemp.getReferenceNo());
                    psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    //===================================<GST Posting>===========================================//
                    String drCr = (DmShared.vNull(listAcDetailsData.get(i).getDebit()).doubleValue() > 0 ? "amtDr" : "amtCr");
                    if (DmShared.vNull(listAcDetailsData.get(i).getCgstAmt()).doubleValue() > 0) {
                        saveGST(mrTemp, i, drCr, cnTemp, narrationTemp, contraTextJV, "C");
                        saveGST(mrTemp, i, drCr, cnTemp, narrationTemp, contraTextJV, "S");
                    } else {
                        saveGST(mrTemp, i, drCr, cnTemp, narrationTemp, contraTextJV, "I");
                    }
                    //=================================<End GST Posting>=========================================//

                    //=====================================<ledgerGst insettion>==================================//
                    sqlQuery = "INSERT INTO ledgerGst "
                            + "("
                            + "  docId"
                            + ", v_SNo"
                            + ", hsnCode"
                            + ", qty"
                            + ", rate"
                            + ", grossAmt"
                            + ", taxableValue"
                            + ", cgstPer"
                            + ", cgstAmt"
                            + ", sgstPer"
                            + ", sgstAmt"
                            + ", ugstPer"
                            + ", ugstAmt"
                            + ", igstPer"
                            + ", igstAmt"
                            + ", roundOffGST"
                            + ", netAmt"
                            + ", isRcm"
                            + ", gstType"
                            + ", itemGstNature"
                            + ", cgstSubCode"
                            + ", sgstSubCode"
                            + ", igstSubCode"
                            + ")"
                            + " VALUES"
                            + "("
                            + "  :docId"
                            + ", :v_SNo"
                            + ", :hsnCode"
                            + ", :qty"
                            + ", :rate"
                            + ", :grossAmt"
                            + ", :taxableValue"
                            + ", :cgstPer"
                            + ", :cgstAmt"
                            + ", :sgstPer"
                            + ", :sgstAmt"
                            + ", :ugstPer"
                            + ", :ugstAmt"
                            + ", :igstPer"
                            + ", :igstAmt"
                            + ", :roundOffGST "
                            + ", :netAmt "
                            + ", :isRcm "
                            + ", :gstType"
                            + ", :itemGstNature"
                            + ", :cgstSubCode"
                            + ", :sgstSubCode"
                            + ", :igstSubCode"
                            + ")";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmServer.vNull(ledgerGstVSNo, "0"));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(listAcDetailsData.get(i).getHsnCode()));
                    psTemp.setString("qty", DmServer.vNull(listAcDetailsData.get(i).getQty()));
                    psTemp.setString("rate", DmServer.vNull(listAcDetailsData.get(i).getRate()));
                    psTemp.setString("grossAmt", DmServer.vNull(listAcDetailsData.get(i).getGrossAmt()));
                    psTemp.setString("cgstPer", DmServer.vNull(listAcDetailsData.get(i).getCgstPer()));
                    psTemp.setString("cgstAmt", DmServer.vNull(listAcDetailsData.get(i).getCgstAmt()));

                    psTemp.setString("sgstPer", DmServer.vNull(listAcDetailsData.get(i).getSgstPer()));
                    psTemp.setString("sgstAmt", DmServer.vNull(listAcDetailsData.get(i).getSgstAmt()));

                    psTemp.setString("igstPer", DmServer.vNull(listAcDetailsData.get(i).getIgstPer()));
                    psTemp.setString("igstAmt", DmServer.vNull(listAcDetailsData.get(i).getIgstAmt()));

                    psTemp.setString("ugstPer", DmServer.vNull(listAcDetailsData.get(i).getUgstPer()));
                    psTemp.setString("ugstAmt", DmServer.vNull(listAcDetailsData.get(i).getUgstAmt()));

                    psTemp.setString("taxableValue", DmServer.vNull(listAcDetailsData.get(i).getTaxableAmt()));
                    psTemp.setString("roundOffGST", DmShared.xNull(listAcDetailsData.get(i).getRoundOffGST()));
                    psTemp.setString("itemGstNature", DmShared.xNull(listAcDetailsData.get(i).getItemGstNature()));

                    psTemp.setString("netAmt", DmServer.vNull(listAcDetailsData.get(i).getNetAmt()));
                    psTemp.setString("isRcm", DmShared.iNull(listAcDetailsData.get(i).getIsRCM()).toString());
                    psTemp.setString("gstType", DmShared.iNull(mrTemp.getGstTypeCode()).toString());

                    psTemp.setString("cgstSubCode", DmShared.foriegnKey(listAcDetailsData.get(i).getcGstAcNameTag()));
                    psTemp.setString("sgstSubCode", DmShared.foriegnKey(listAcDetailsData.get(i).getsGstAcNameTag()));
                    psTemp.setString("igstSubCode", DmShared.foriegnKey(listAcDetailsData.get(i).getiGstAcNameTag()));

                    psTemp.executeUpdate();
                    psTemp.close();

                    //<editor-fold defaultstate="collapsed" desc="Set gstTrans_Mdl">
                    GstTrans_Mdl gstTrans_Mdl = new GstTrans_Mdl();
                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    gstTrans_Mdl.setV_Sno(DmServer.vNull(ledgerGstVSNo, "0"));
                    gstTrans_Mdl.setSite_Code(DmShared.xNull(DmServer.getSsnSiteCode(this)));
                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTemp.getVoucherDate()));
                    if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
                        gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTemp.getReferenceNo()));
                        gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTemp.getReferenceDate()));
                    }
                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTemp.getAccountNameCode()));
                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTemp.getAccountNameCode()));
                    gstTrans_Mdl.setConsigneeType(DmShared.xNull(mrTemp.getConsigneeType()));
                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTemp.getPlaceOfSupplyCode()));
                    if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)
                            || DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.DEBIT_NOTE_GST)) {
                        gstTrans_Mdl.setRefDocId(DmShared.xNull(mrTemp.getRefDocId()));
                        gstTrans_Mdl.setRefV_Type(DmShared.xNull(mrTemp.getRefV_Type()));
                        gstTrans_Mdl.setRefDocValue(DmShared.xNull(mrTemp.getRefDocValue()));

                        if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
                            gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getRefRecId()));
                            gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getRefV_Date()));
                            gstTrans_Mdl.setRefPartyBillNo(DmShared.xNull(mrTemp.getReferenceNo()));
                            gstTrans_Mdl.setRefPartyBillDt(DmShared.dNull(mrTemp.getReferenceDate()));
                        } else {
                            if (!DmShared.xNull(mrTemp.getRefRecId()).trim().equalsIgnoreCase("")) {
                                gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getRefRecId()));
                            } else {
                                gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getReferenceNo()));
                            }

                            if (!DmShared.dNull(mrTemp.getRefV_Date()).trim().equalsIgnoreCase("")) {
                                gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getRefV_Date()));
                            } else {
                                gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getReferenceDate()));
                            }

                        }
                    }
                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTemp.getGstReasonCode()));
                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(listAcDetailsData.get(i).getIsRCM()));
                    gstTrans_Mdl.setHsnCode(DmShared.xNull(listAcDetailsData.get(i).getHsnCode()));
                    gstTrans_Mdl.setQty(DmShared.iNull(listAcDetailsData.get(i).getQty()).toString());
                    gstTrans_Mdl.setItemGrossAmount(DmShared.xNull(listAcDetailsData.get(i).getGrossAmt()));
                    gstTrans_Mdl.setItemTaxableAmount(DmShared.xNull(listAcDetailsData.get(i).getTaxableAmt()));
                    gstTrans_Mdl.setItemNetAmount(DmShared.xNull(listAcDetailsData.get(i).getNetAmt()));
                    gstTrans_Mdl.setItemCgstRate(DmShared.xNull(listAcDetailsData.get(i).getCgstPer()));
                    gstTrans_Mdl.setItemCgstAmount(DmShared.xNull(listAcDetailsData.get(i).getCgstAmt()));
                    gstTrans_Mdl.setItemSgstRate(DmShared.xNull(listAcDetailsData.get(i).getSgstPer()));
                    gstTrans_Mdl.setItemSgstAmout(DmShared.xNull(listAcDetailsData.get(i).getSgstAmt()));
                    gstTrans_Mdl.setItemIgstRate(DmShared.xNull(listAcDetailsData.get(i).getIgstPer()));
                    gstTrans_Mdl.setItemIgstAmount(DmShared.xNull(listAcDetailsData.get(i).getIgstAmt()));
                    gstTrans_Mdl.setItemUgstRate(DmShared.xNull(listAcDetailsData.get(i).getUgstPer()));
                    gstTrans_Mdl.setItemUgstAmount(DmShared.xNull(listAcDetailsData.get(i).getUgstAmt()));
                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(listAcDetailsData.get(i).getItemGstNature()));
                    gstTrans_Mdl.setInvoiceCGST(DmServer.vNull(invoiceCgst, "0.00"));
                    gstTrans_Mdl.setInvoiceSGST(DmServer.vNull(invoiceSgst, "0.00"));
                    gstTrans_Mdl.setInvoiceIGST(DmServer.vNull(invoiceIgst, "0.00"));
                    gstTrans_Mdl.setInvoiceUGST(DmServer.vNull(invoiceUgst, "0.00"));
                    gstTrans_Mdl.setInvoiceValue(DmServer.vNull(Math.abs(DmShared.vNull(invoiceNetAmt)), "0.00"));
                    gstTrans_Mdl.setGstType(DmShared.xNull(mrTemp.getGstTypeCode()));
                    gstTrans_Mdl.setIsTaxInclusive(DmShared.iNull("0").toString());

                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
                    //</editor-fold>
                    //=====================================< Item Adjustment >====================================//
                    GridAcDetailsMdl gveMdl = listAcDetailsData.get(i);
                    if (gveMdl != null) {
                        if (gveMdl.getLstItemAdjustments() != null) {
                            if (!gveMdl.getLstItemAdjustments().isEmpty()) {
                                ItemAdjustmentSave_Mdl iadjSaveMdl = new ItemAdjustmentSave_Mdl();
                                iadjSaveMdl.setVr_DocId(mrTemp.getSharedKeyFieldCode());
                                iadjSaveMdl.setVr_V_SNo(DmShared.iNull(ledgerGstVSNo).toString());
                                iadjSaveMdl.setVr_V_Date(mrTemp.getVoucherDate());
                                iadjSaveMdl.setVr_SubCode(gveMdl.getAccCode());
                                iadjSaveMdl.setLstItemAdjustments(gveMdl.getLstItemAdjustments());
                                ItemAdjustment_SrvImpl.saveRecord(iadjSaveMdl, cnTemp);
                            }
                        }
                    }
                    //=======================================< ********** >=======================================//
                }
            }
            //<editor-fold defaultstate="collapsed" desc="HEADER ENTRY IN LEDGER">
            sqlQuery = "Insert Into Ledger ("
                    + "   DocId"
                    + " , RecId"
                    + " , recIdPrefix"
                    + " , V_No"
                    + " , V_SNo"
                    + " , V_Date"
                    + " , SubCode"
                    + " , ContraSub"
                    + " , AmtDr"
                    + " , AmtCr"
                    + " , remark"
                    + " , V_Type"
                    + " , V_Prefix"
                    + " , Site_Code"
                    + " , ContraText"
                    + " , RefNo"
                    + " , RefDate"
                    + " , narration"
                    + " ) "
                    + "Values"
                    + " ("
                    + "   :DocId"
                    + " , :RecId"
                    + " , :recIdPrefix"
                    + " , :V_No"
                    + " , :V_SNo"
                    + " , :V_Date"
                    + " , :SubCode"
                    + " , :ContraSub"
                    + " , :AmtDr"
                    + " , :AmtCr"
                    + " , :remark"
                    + " , :V_Type"
                    + " , :V_Prefix"
                    + " , :Site_Code"
                    + " , :ContraText"
                    + " , :RefNo"
                    + " , :RefDate"
                    + " , :narration"
                    + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
            psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
            psTemp.setString("V_SNo", DmServer.vNull(1, "0"));
            psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
            psTemp.setString("ContraSub", DmShared.foriegnKey(listAcDetailsData.get(0).getAccCode()));
            if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)
                    || mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
                psTemp.setString("AmtDr", "0.00");
                psTemp.setString("AmtCr", DmServer.vNull(mrTemp.getTotalCredit(), "0.00"));
            } else {
                psTemp.setString("AmtDr", DmServer.vNull(mrTemp.getTotalDebit(), "0.00"));
                psTemp.setString("AmtCr", "0.00");
            }
            psTemp.setString("remark", mrTemp.getRemark());
            psTemp.setString("narration", mrTemp.getRemark());
            psTemp.setString("V_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
            psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
            psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("ContraText", contraTextOther);
            psTemp.setString("RefNo", mrTemp.getReferenceNo());
            psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Adjustment">
            if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)
                    || DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.DEBIT_NOTE_GST)) {
                AdjustmentSave_Mdl adjSaveMdl = new AdjustmentSave_Mdl();
                adjSaveMdl.setVr_DocId(mrTemp.getSharedKeyFieldCode());
                adjSaveMdl.setVr_V_SNo("1");
                adjSaveMdl.setVr_V_Date(mrTemp.getVoucherDate());
                adjSaveMdl.setVr_SubCode(mrTemp.getAccountNameCode());

                GrdAdjustment_Mdl gaMdl = new GrdAdjustment_Mdl();
                gaMdl.setSharedKeyFieldCode(mrTemp.getRefDocId());
                gaMdl.setV_SNo(mrTemp.getRefV_SNo());
                gaMdl.setV_Date(mrTemp.getRefV_Date());
                gaMdl.setAcCode(mrTemp.getAccountNameCode());
                if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
                    gaMdl.setAdjustment(DmServer.vNull(mrTemp.getTotalCredit(), "0.00"));
                    gaMdl.setAdjustmentDrCr("Dr");
                } else if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.DEBIT_NOTE_GST)) {
                    gaMdl.setAdjustment(DmServer.vNull(mrTemp.getTotalDebit(), "0.00"));
                    gaMdl.setAdjustmentDrCr("Cr");
                }
                adjSaveMdl.getLstAdjustments().add(gaMdl);

                AccountAdjustment_Server.saveRecord(adjSaveMdl, cnTemp);
            }
            //</editor-fold>

            //</editor-fold>
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ledgerGst">
//            double invoiceUgst = 0;
//            double invoiceSgst = 0;
//            double invoiceCgst = 0;
//            double invoiceIgst = 0;
//            double invoiceNetAmt = 0;
//            for (GridAcDetailsMdl gstMdl : listAcDetailsData) {
//                if (DmShared.iNull(gstMdl.getHsnCode()).longValue() != 0) {
//                    invoiceUgst += DmShared.vNull(gstMdl.getUgstAmt());
//                    invoiceSgst += DmShared.vNull(gstMdl.getSgstAmt());
//                    invoiceCgst += DmShared.vNull(gstMdl.getCgstAmt());
//                    invoiceIgst += DmShared.vNull(gstMdl.getIgstAmt());
//                    invoiceNetAmt += DmShared.vNull(gstMdl.getNetAmt());
//                }
//            }
//            listAcDetailsData = mrTemp.getGrdAcDetails();
//            vSNo = 0;
//            for (GridAcDetailsMdl gstMdl : listAcDetailsData) {
//                gstMdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                if (DmShared.iNull(gstMdl.getHsnCode()).longValue() != 0) {
//                    gstMdl.setSharedKeyFieldName(DmShared.iNull(++vSNo).toString());
//                    sqlQuery = "INSERT INTO ledgerGst "
//                            + "("
//                            + "  docId"
//                            + ", v_SNo"
//                            + ", hsnCode"
//                            + ", qty"
//                            + ", rate"
//                            + ", grossAmt"
//                            + ", taxableValue"
//                            + ", cgstPer"
//                            + ", cgstAmt"
//                            + ", sgstPer"
//                            + ", sgstAmt"
//                            + ", ugstPer"
//                            + ", ugstAmt"
//                            + ", igstPer"
//                            + ", igstAmt"
//                            + ", roundOffGST"
//                            + ", netAmt"
//                            + ", isRcm"
//                            + ", gstType"
//                            + ", itemGstNature"
//                            + ")"
//                            + " VALUES"
//                            + "("
//                            + "  :docId"
//                            + ", :v_SNo"
//                            + ", :hsnCode"
//                            + ", :qty"
//                            + ", :rate"
//                            + ", :grossAmt"
//                            + ", :taxableValue"
//                            + ", :cgstPer"
//                            + ", :cgstAmt"
//                            + ", :sgstPer"
//                            + ", :sgstAmt"
//                            + ", :ugstPer"
//                            + ", :ugstAmt"
//                            + ", :igstPer"
//                            + ", :igstAmt"
//                            + ", :roundOffGST "
//                            + ", :netAmt "
//                            + ", :isRcm "
//                            + ", :gstType"
//                            + ", :itemGstNature"
//                            + ")";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setString("docId", DmShared.xNull(gstMdl.getSharedKeyFieldCode()));
//                    psTemp.setString("v_SNo", DmShared.xNull(gstMdl.getSharedKeyFieldName()));
//                    psTemp.setString("hsnCode", DmShared.foriegnKey(gstMdl.getHsnCode()));
//                    psTemp.setString("qty", DmServer.vNull(gstMdl.getQty()));
//                    psTemp.setString("rate", DmServer.vNull(gstMdl.getRate()));
//                    psTemp.setString("grossAmt", DmServer.vNull(gstMdl.getGrossAmt()));
//                    psTemp.setString("cgstPer", DmServer.vNull(gstMdl.getCgstPer()));
//                    psTemp.setString("cgstAmt", DmServer.vNull(gstMdl.getCgstAmt()));
//
//                    psTemp.setString("sgstPer", DmServer.vNull(gstMdl.getSgstPer()));
//                    psTemp.setString("sgstAmt", DmServer.vNull(gstMdl.getSgstAmt()));
//
//                    psTemp.setString("igstPer", DmServer.vNull(gstMdl.getIgstPer()));
//                    psTemp.setString("igstAmt", DmServer.vNull(gstMdl.getIgstAmt()));
//
//                    psTemp.setString("ugstPer", DmServer.vNull(gstMdl.getUgstPer()));
//                    psTemp.setString("ugstAmt", DmServer.vNull(gstMdl.getUgstAmt()));
//
//                    psTemp.setString("taxableValue", DmServer.vNull(gstMdl.getTaxableAmt()));
//                    psTemp.setString("roundOffGST", DmShared.xNull(gstMdl.getRoundOffGST()));
//                    psTemp.setString("itemGstNature", DmShared.xNull(gstMdl.getItemGstNature()));
//
//                    psTemp.setString("netAmt", DmServer.vNull(gstMdl.getNetAmt()));
//                    psTemp.setString("isRcm", DmShared.iNull(gstMdl.getIsRCM()).toString());
//                    psTemp.setString("gstType", DmShared.iNull(mrTemp.getGstTypeCode()).toString());
//                    psTemp.executeUpdate();
//                    psTemp.close();
//
//                    //<editor-fold defaultstate="collapsed" desc="Set gstTrans_Mdl">
//                    GstTrans_Mdl gstTrans_Mdl = new GstTrans_Mdl();
//                    gstTrans_Mdl.setDocId(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//                    gstTrans_Mdl.setV_Sno(DmShared.xNull(gstMdl.getSharedKeyFieldName()));
//                    gstTrans_Mdl.setSite_Code(DmShared.xNull(DmServer.getSsnSiteCode(this)));
//                    gstTrans_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
//                    gstTrans_Mdl.setRecID(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
//                    gstTrans_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
//                    gstTrans_Mdl.setRecIdSerial(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
//                    gstTrans_Mdl.setV_Date(DmShared.xNull(mrTemp.getVoucherDate()));
//                    if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
//                        gstTrans_Mdl.setPartyBillNo(DmShared.xNull(mrTemp.getReferenceNo()));
//                        gstTrans_Mdl.setPartyBillDt(DmShared.dNull(mrTemp.getReferenceDate()));
//                    }
//                    gstTrans_Mdl.setPartyCode(DmShared.xNull(mrTemp.getAccountNameCode()));
//                    gstTrans_Mdl.setConsignee(DmShared.xNull(mrTemp.getAccountNameCode()));
//                    gstTrans_Mdl.setConsigneeType(DmShared.xNull(mrTemp.getConsigneeType()));
//                    gstTrans_Mdl.setPlaceOfSupply(DmShared.xNull(mrTemp.getPlaceOfSupplyCode()));
//                    if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)
//                            || DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.DEBIT_NOTE_GST)) {
//                        gstTrans_Mdl.setRefDocId(DmShared.xNull(mrTemp.getRefDocId()));
//                        gstTrans_Mdl.setRefV_Type(DmShared.xNull(mrTemp.getRefV_Type()));
//                        gstTrans_Mdl.setRefDocValue(DmShared.xNull(mrTemp.getRefDocValue()));
//
//                        if (DmShared.xNull(mrTemp.getVoucherCategory()).trim().equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
//                            gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getRefRecId()));
//                            gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getRefV_Date()));
//                            gstTrans_Mdl.setRefPartyBillNo(DmShared.xNull(mrTemp.getReferenceNo()));
//                            gstTrans_Mdl.setRefPartyBillDt(DmShared.dNull(mrTemp.getReferenceDate()));
//                        } else {
//                            if (!DmShared.xNull(mrTemp.getRefRecId()).trim().equalsIgnoreCase("")) {
//                                gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getRefRecId()));
//                            } else {
//                                gstTrans_Mdl.setRefRecId(DmShared.xNull(mrTemp.getReferenceNo()));
//                            }
//
//                            if (!DmShared.dNull(mrTemp.getRefV_Date()).trim().equalsIgnoreCase("")) {
//                                gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getRefV_Date()));
//                            } else {
//                                gstTrans_Mdl.setRefV_Date(DmShared.dNull(mrTemp.getReferenceDate()));
//                            }
//
//                        }
//                    }
//                    gstTrans_Mdl.setGstReasonCode(DmShared.xNull(mrTemp.getGstReasonCode()));
//                    gstTrans_Mdl.setRcmApplicable(DmShared.xNull(gstMdl.getIsRCM()));
//                    gstTrans_Mdl.setHsnCode(DmShared.xNull(gstMdl.getHsnCode()));
//                    gstTrans_Mdl.setQty(DmShared.iNull(gstMdl.getQty()).toString());
//                    gstTrans_Mdl.setItemGrossAmount(DmShared.xNull(gstMdl.getGrossAmt()));
//                    gstTrans_Mdl.setItemTaxableAmount(DmShared.xNull(gstMdl.getTaxableAmt()));
//                    gstTrans_Mdl.setItemNetAmount(DmShared.xNull(gstMdl.getNetAmt()));
//                    gstTrans_Mdl.setItemCgstRate(DmShared.xNull(gstMdl.getCgstPer()));
//                    gstTrans_Mdl.setItemCgstAmount(DmShared.xNull(gstMdl.getCgstAmt()));
//                    gstTrans_Mdl.setItemSgstRate(DmShared.xNull(gstMdl.getSgstPer()));
//                    gstTrans_Mdl.setItemSgstAmout(DmShared.xNull(gstMdl.getSgstAmt()));
//                    gstTrans_Mdl.setItemIgstRate(DmShared.xNull(gstMdl.getIgstPer()));
//                    gstTrans_Mdl.setItemIgstAmount(DmShared.xNull(gstMdl.getIgstAmt()));
//                    gstTrans_Mdl.setItemUgstRate(DmShared.xNull(gstMdl.getUgstPer()));
//                    gstTrans_Mdl.setItemUgstAmount(DmShared.xNull(gstMdl.getUgstAmt()));
//                    gstTrans_Mdl.setItemGSTNature(DmShared.xNull(gstMdl.getItemGstNature()));
//                    gstTrans_Mdl.setInvoiceCGST(DmServer.vNull(invoiceCgst, "0.00"));
//                    gstTrans_Mdl.setInvoiceSGST(DmServer.vNull(invoiceSgst, "0.00"));
//                    gstTrans_Mdl.setInvoiceIGST(DmServer.vNull(invoiceIgst, "0.00"));
//                    gstTrans_Mdl.setInvoiceUGST(DmServer.vNull(invoiceUgst, "0.00"));
//                    gstTrans_Mdl.setInvoiceValue(DmServer.vNull(Math.abs(DmShared.vNull(invoiceNetAmt)), "0.00"));
//                    gstTrans_Mdl.setGstType(DmShared.xNull(mrTemp.getGstTypeCode()));
//                    gstTrans_Mdl.setIsTaxInclusive(DmShared.iNull("0").toString());
//
//                    Gst_Server.saveGstTrans(gstTrans_Mdl, cnTemp, this);
//                    //</editor-fold>
//                }
//            }
//</editor-fold>
            //</editor-fold>
            /*==============================<< Approve Document >>====================================*/
            if (autoApprove) {
                if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0) {
                    documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
                } else {
                    sqlQuery = "Update Ledger Set "
                            + "isPosted = 1 "
                            + "Where DocId = :DocId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }

            //<editor-fold defaultstate="collapsed" desc="Save Record Model">
            srmTemp.setDocId(mrTemp.getSharedKeyFieldCode());
            srmTemp.setVoucherTypeCode(mrTemp.getVoucherTypeCode());
            srmTemp.setVoucherTypeName(mrTemp.getVoucherTypeName());
            srmTemp.setVoucherCategory(mrTemp.getVoucherCategory());
            srmTemp.setRecId(mrTemp.getSharedKeyFieldName());
            srmTemp.setVoucherDate(mrTemp.getVoucherDate());
            //</editor-fold>

//            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            dmrTemp.setData(srmTemp);
            spBegin.commit();
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

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "Select Count(*) As Cnt From DataAudit Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select postedBy, postedByDateTime,"
                    + "(case when :userName IN (ISNULL(postedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from LedgerM where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("postedBy")).trim().equalsIgnoreCase("")) {
                        rsTemp.updateString("postedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("postedByDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = "Update Ledger Set "
                                + "isPosted = 1 "
                                + "Where DocId = :DocId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", keyCode);
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already posted.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("postedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {
                            rsTemp.updateString("postedBy", null);
                            rsTemp.updateDate("postedByDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "Update Ledger Set "
                                    + "isPosted = 0 "
                                    + "Where DocId = :DocId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("DocId", keyCode);
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to unpost.");
                        }
                    } else {
                        throw new Exception("Document already un-posted.");
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

    private WalkThroughDefault_Mdl getWalkThroughFor(GstVoucherEntry_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl mrTemp;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition, condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " AND vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "','" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')";

            sqlQuery = "Select	LM.DocId, LM.V_Type, LM.V_Date, VT.Description As Voucher_Type, "
                    + " LM.RecId As Voucher_No, LM.V_Date As Voucher_Date, LG.RefNo As Reference_No, "
                    + " LG.RefDate As Reference_Date, isNull(SG.Name,'') As Account1, isNull(SG1.Name,'') As Account2, "
                    + " IsNull(LG.AmtDr,0) As Debit, IsNull(LG.AmtCr,0) As Credit, LM.REMARK as Narration,  "
                    + " isNull(LM.PreparedBy,'') As Prepared_By, isNull(LM.U_Name,'') As Modified_By, "
                    + " FORMAT(lm.u_EntDt,'dd-MMM-yyyy HH.mm') AS entryDateTime ,"
                    + " isNull(LM.PostedBy,'') As Posted_By, LM.V_NO "
                    + " From LedgerM LM "
                    + " Left Join Voucher_Type VT On LM.V_Type=VT.V_Type   "
                    + " Left Join Ledger LG On LM.DocId=LG.DocId And LG.V_SNo=1  "
                    + " Left Join SubGroup SG On LM.SubCode=SG.SubCode   "
                    + " Left Join SubGroup SG1 On LG.SubCode=SG1.SubCode   "
                    + " Where  LM.Site_Code = :siteCode "
                    + " And LM.V_Prefix = :voucherPrefix " + condition;

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("voucherPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                try {
                    mrTemp = new Find_Mdl();
                    mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Voucher_No")));
                    mrTemp.setVoucherType(DmShared.xNull(rsTemp.getString("Voucher_Type")));
                    mrTemp.setV_Date(DmShared.dNull(rsTemp.getDate("Voucher_Date")));
                    mrTemp.setAccount1(DmShared.xNull(rsTemp.getString("Account1")));
                    mrTemp.setAccount2(DmShared.xNull(rsTemp.getString("Account2")));
                    mrTemp.setDebit(DmServer.vNull(rsTemp.getString("Debit"), "0.00"));
                    mrTemp.setCredit(DmServer.vNull(rsTemp.getString("Credit"), "0.00"));
                    mrTemp.setNaration(DmShared.xNull(rsTemp.getString("Narration")));
                    mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("Prepared_By")));
                    mrTemp.setModifiyedBy(DmShared.xNull(rsTemp.getString("Modified_By")));
                    mrTemp.setPostedBy(DmShared.xNull(rsTemp.getString("Posted_By")));
                    mrTemp.setEntryDtTime(DmShared.xNull(rsTemp.getString("entryDateTime")));
                    lstTemp.add(mrTemp);
                } catch (Exception e) {
                    rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
                }
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
    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, DocId = strKey, wtaCode = "";;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check for before deleting data >>===============================*/
            sqlQuery = "Select Count(*) As Cnt From DataAudit Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select Count(DocId) As Cnt From LedgerM Where DocId=:DocId And isNull(PostedBy,'')<>''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< ****************************** >>===============================*/

            sqlQuery = "Delete From LedgerCostCenter Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();

            sqlQuery = "Delete From Stock Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*==============================<< Delete records from gstTrans >>====================================*/
            Gst_Server.deleteGstTrans(DmShared.iNull(DocId).toString(), cnTemp);
            AccountAdjustment_Server.deleteRecord(DocId, cnTemp);
            ItemAdjustment_SrvImpl.deleteRecord(DocId, cnTemp);

            sqlQuery = "Delete From LedgerGst Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From Ledger Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From LedgerM Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);

            spBegin.commit();
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
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);

            condition += " AND vt.category IN ('" + lvc.CREDIT_INVOICE_GST + "','" + lvc.CREDIT_NOTE_GST + "', '" + lvc.DEBIT_NOTE_GST + "', '" + lvc.DEBIT_INVOICE_GST + "')";

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT VT.V_Type,VT.Description,VT.Category "
                    + " FROM Voucher_Type VT "
                    + " WHERE VT.NCat = 'FA' " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("Category")));
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
    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, String v_Type, String v_Date) {
        DmResultPageing<List<TxtHAccountName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountName_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.dNull(v_Date).trim().equals("")) {
                condition += " AND 1 = 2 ";
            }

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , c.cityName"
                    + " , c.stateCode"
                    + " , sm.name as stateName"
                    + " , sg.partyType"
                    + " , sg.gstin"
                    + " , sg.gstinDate"
                    + " , sg.manualCode"
                    + " , sgn.name AS nature"
                    + " , sgn.manualCode AS natureManualCode"
                    + "	FROM subGroup sg"
                    + "	LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + " LEFT JOIN stateMast sm ON sm.code = c.stateCode "
                    + "	LEFT JOIN subGroupSiteList sgl ON sg.subCode = sgl.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sg.nature = sgn.code"
                    + "	WHERE sgl.site_Code = :site_Code"
                    + " AND c.stateCode IS NOT NULL "
                    + " AND sgn.code IN (SELECT nature FROM acFilteration WHERE v_Type = :v_Type)"
                    + "	AND sg.isActive = 1"
                    + " " + condition;
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setPartyTypeCode(DmShared.xNull(rsTemp.getString("partyType")));
                listNode.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setNatureManualCode(DmShared.xNull(rsTemp.getString("natureManualCode")));

                if (DmShared.dNull(v_Date).trim().equals("")) {
                    listNode.setPartyTypeCode(listNode.getPartyTypeCode());
                    if (DmShared.xNull(listNode.getPartyTypeCode()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Registered.getValue());
                    } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                    } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Composite.getValue());
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("gstin")).equalsIgnoreCase("") && !DmShared.dNull(rsTemp.getDate("gstinDate")).equalsIgnoreCase("")) {
                        if (DmShared.convertDate(DmShared.dNull(rsTemp.getDate("gstinDate"))).getTime() > DmShared.convertDate(DmShared.dNull(v_Date)).getTime()) {
                            listNode.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                            listNode.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                        } else {
                            if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                                listNode.setPartyTypeCode(DmEnum.DmPartyType.Registered.getId());
                                listNode.setPartyTypeName(DmEnum.DmPartyType.Registered.getValue());
                            } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                                listNode.setPartyTypeCode(DmEnum.DmPartyType.Composite.getId());
                                listNode.setPartyTypeName(DmEnum.DmPartyType.Composite.getValue());
                            }
                        }
                    } else {
                        listNode.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                    }
                }

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
    public DmResultPageing<List<TxtHAccountNameGrid_Mdl>> getTxtHAccountNameGrid(DmHelper helper, boolean blnToEndOfFile, String exceptAcCode, String v_Date) {
        DmResultPageing<List<TxtHAccountNameGrid_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountNameGrid_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountNameGrid_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition += " AND sgn.manualCode not in ('BK','CS') ";

            if (DmShared.iNull(exceptAcCode).longValue() != 0) {
                condition += " AND sg.subCode != '" + DmShared.iNull(exceptAcCode).longValue() + "'";
            }

            sqlQuery = "SELECT"
                    + "   SG.subCode"
                    + " , SG.name"
                    + " , SG.manualCode"
                    + " , isNull(CT.CityName,'') AS CityName"
                    + " , SG.Nature"
                    + " , AG.GroupName"
                    + " , isNull(SG.DisplayName,SG.Name) AS DisplayName"
                    + " , sgn.Name AS SGNature"
                    + " , sgn.manualCode AS natureManualCode"
                    + " , ct.stateCode"
                    + " , sm.name as stateName"
                    + " , sg.partyType"
                    + " , sg.gstin"
                    + " , sg.gstinDate"
                    + " FROM SubGroup SG "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + " LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + " LEFT JOIN stateMast sm ON sm.code = CT.stateCode "
                    + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code "
                    + " LEFT JOIN subgroupsitelist SGL ON SG.subCode = SGL.subCode "
                    + " WHERE SGL.site_Code = '" + DmServer.getSsnSiteCode(this) + "'"
                    + " AND SG.isActive = 1 " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountNameGrid_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setGroup(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setNatureManualCode(DmShared.xNull(rsTemp.getString("natureManualCode")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setStateName(DmShared.xNull(rsTemp.getString("stateName")));
                listNode.setPartyTypeCode(DmShared.xNull(rsTemp.getString("partyType")));
                listNode.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));

                if (DmShared.dNull(v_Date).trim().equals("")) {
                    listNode.setPartyTypeCode(listNode.getPartyTypeCode());
                    if (DmShared.xNull(listNode.getPartyTypeCode()).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Registered.getValue());
                    } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                    } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Composite.getValue());
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("gstin")).equalsIgnoreCase("") && !DmShared.dNull(rsTemp.getDate("gstinDate")).equalsIgnoreCase("")) {
                        if (DmShared.convertDate(DmShared.dNull(rsTemp.getDate("gstinDate"))).getTime() > DmShared.convertDate(DmShared.dNull(v_Date)).getTime()) {
                            listNode.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                            listNode.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                        } else {
                            if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Registered.getId())) {
                                listNode.setPartyTypeCode(DmEnum.DmPartyType.Registered.getId());
                                listNode.setPartyTypeName(DmEnum.DmPartyType.Registered.getValue());
                            } else if (DmShared.xNull(rsTemp.getString("partyType")).equalsIgnoreCase(DmEnum.DmPartyType.Composite.getId())) {
                                listNode.setPartyTypeCode(DmEnum.DmPartyType.Composite.getId());
                                listNode.setPartyTypeName(DmEnum.DmPartyType.Composite.getValue());
                            }
                        }
                    } else {
                        listNode.setPartyTypeCode(DmEnum.DmPartyType.Unregistered.getId());
                        listNode.setPartyTypeName(DmEnum.DmPartyType.Unregistered.getValue());
                    }
                }

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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
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

            //==========================<< Get Site GST Details >>==========================//
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

            //==========================<< Get Site GST Details >>==========================//
            sqlQuery = "SELECT "
                    + " env.ac_RoundOffGST "
                    + " FROM enviro env"
                    + " WHERE env.site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setRoundOffGSTCode(DmShared.xNull(rsTemp.getString("ac_RoundOffGST")));
                if (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("Y")) {
                    defaultValue.setRoundOffGSTName("Yes");
                } else if (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("N")) {
                    defaultValue.setRoundOffGSTName("No");
                } else if (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("D")) {
                    defaultValue.setRoundOffGSTName("Disable");
                } else if (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("H")) {
                    defaultValue.setRoundOffGSTName("Higher");
                } else if (DmShared.xNull(rsTemp.getString("ac_RoundOffGST")).equalsIgnoreCase("L")) {
                    defaultValue.setRoundOffGSTName("Lower");
                }
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(defaultValue);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHState(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT code,Name,numericCode from stateMast  where isnull(numericCode,'0') <> '0' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("numericCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRoundOffGst(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Disable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("H");
            listNode.setSharedKeyFieldName("Higher");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Lower");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        String InputCode = String.valueOf(DmEnum.DmGSTType.INPUT.toInt());
        String InputName = String.valueOf(DmEnum.DmGSTType.INPUT.toString());

        String OutputCode = String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt());
        String OutputName = String.valueOf(DmEnum.DmGSTType.OUTPUT.toString());

        String VoidCode = String.valueOf(DmEnum.DmGSTType.VOID.toInt());
        String VoidName = String.valueOf(DmEnum.DmGSTType.VOID.toString());

        try {
            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(InputCode);
                listNode.setSharedKeyFieldName(InputName);
                lstTemp.add(listNode);
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.DEBIT_INVOICE_GST)) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(OutputCode);
                listNode.setSharedKeyFieldName(OutputName);
                lstTemp.add(listNode);
            } else {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(InputCode);
                listNode.setSharedKeyFieldName(InputName);
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(OutputCode);
                listNode.setSharedKeyFieldName(OutputName);
                lstTemp.add(listNode);

            }

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(VoidCode);
            listNode.setSharedKeyFieldName(VoidName);
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

    @Override
    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHHSN_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHHSN_Mdl> lstTemp = new ArrayList<>();
        TxtHHSN_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select hm.code, hm.name, hm.hsn ,hm.itemGSTNature from hsnMast hm";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHSN_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGSTNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (!DmShared.xNull(rsTemp.getString("itemGSTNature")).equalsIgnoreCase("")) {
                    listNode.setItemGSTNatureText(DmEnum.GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))).getValue());
                }
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

    @Override
    public DmResultPageing<List<TxtHRefVoucher_Mdl>> getTxtHRefVoucher(DmHelper helper, boolean blnToEndOfFile, String subCode) {
        DmResultPageing<List<TxtHRefVoucher_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRefVoucher_Mdl> lstTemp = new ArrayList<>();
        TxtHRefVoucher_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT "
                    + "   l.docId "
                    + " , l.recid "
                    + " , vt.short_Name as type "
                    + " , l.v_Date "
                    + " , l.v_type "
                    + " , l.v_SNo "
                    + " , l.v_No "
                    + " , l.refNo"
                    + " , l.refDate "
                    + " , CASE WHEN ISNULL(l.amtDr,0) > 0 THEN l.amtDr ELSE l.amtCr END as docValue "
                    + " FROM ledger l "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = l.v_type "
                    + " WHERE l.subCode = :subCode ";

            sqlWithParam.setString("subCode", DmShared.iNull(subCode).toString());
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRefVoucher_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setRefV_Type(DmShared.xNull(rsTemp.getString("v_type")));
                listNode.setRefV_Date(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRefDocType(DmShared.xNull(rsTemp.getString("type")));
                listNode.setRefDocValue(DmServer.vNull(rsTemp.getString("docValue"), "0.00"));
                listNode.setRefV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                if (!DmShared.xNull(rsTemp.getString("refNo")).trim().equalsIgnoreCase("")) {
                    listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                } else {
                    listNode.setRefNo(DmShared.xNull(rsTemp.getString("recid")));
                }
                if (!DmShared.dNull(rsTemp.getString("refDate")).trim().equalsIgnoreCase("")) {
                    listNode.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                } else {
                    listNode.setRefDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTaxAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select sg.subCode as code,sg.name from subgroup sg where groupCode=(select groupcode from acGroup where groupName='Duties & Taxes')";
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

    private void saveGST(GstVoucherEntry_Mdl mrTemp, int rowindex, String drCrFld, Connection cnTemp, String narration, String contraText, String taxType) {
        DmResult<SaveRecord_Mdl> dmrTemp = new DmResult();
        List<GridAcDetailsMdl> listAcDetailsData;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        String contraDrCrFld;
        int trnVSno = 0;
        try {
            sqlQuery = " SELECT max(v_Sno)+1 as vSno FROM ledger l WHERE  l.docid = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                trnVSno = DmShared.iNull(rsTemp.getString("vSno")).intValue();
                maxLedVSNo = trnVSno;
            }
            rsTemp.close();
            psTemp.close();

            contraDrCrFld = (drCrFld == "amtDr" ? "amtCr" : "amtDr");
            listAcDetailsData = mrTemp.getGrdAcDetails();
            sqlQuery = "Insert Into Ledger ("
                    + "   DocId"
                    + " , RecId"
                    + " , recIdPrefix"
                    + " , V_No"
                    + " , V_SNo"
                    + " , V_Date"
                    + " , SubCode"
                    + " , ContraSub"
                    + " , AmtDr"
                    + " , AmtCr"
                    + " , remark"
                    + " , V_Type"
                    + " , V_Prefix"
                    + " , Site_Code"
                    + " , ContraText"
                    + " , RefNo"
                    + " , RefDate"
                    + " , narration"
                    + " ) "
                    + "Values"
                    + " ("
                    + "   :DocId"
                    + " , :RecId"
                    + " , :recIdPrefix"
                    + " , :V_No"
                    + " , :V_SNo"
                    + " , :V_Date"
                    + " , :SubCode"
                    + " , :ContraSub"
                    + " , :AmtDr"
                    + " , :AmtCr"
                    + " , :remark"
                    + " , :V_Type"
                    + " , :V_Prefix"
                    + " , :Site_Code"
                    + " , :ContraText"
                    + " , :RefNo"
                    + " , :RefDate"
                    + " , :narration"
                    + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
            psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
            psTemp.setString("V_SNo", DmServer.vNull(trnVSno, "0"));
            psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));

            if (taxType == "C") {
                psTemp.setString("SubCode", DmShared.foriegnKey(listAcDetailsData.get(rowindex).getcGstAcNameTag()));
                psTemp.setString(drCrFld, DmServer.vNull(listAcDetailsData.get(rowindex).getCgstAmt(), "0.00"));
            } else if (taxType == "S") {
                psTemp.setString("SubCode", DmShared.foriegnKey(listAcDetailsData.get(rowindex).getsGstAcNameTag()));
                psTemp.setString(drCrFld, DmServer.vNull(listAcDetailsData.get(rowindex).getSgstAmt(), "0.00"));
            } else if (taxType == "I") {
                psTemp.setString("SubCode", DmShared.foriegnKey(listAcDetailsData.get(rowindex).getiGstAcNameTag()));
                psTemp.setString(drCrFld, DmServer.vNull(listAcDetailsData.get(rowindex).getIgstAmt(), "0.00"));
            }
            psTemp.setString(contraDrCrFld, "0");
            psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getAccountNameCode()));

            psTemp.setString("remark", listAcDetailsData.get(rowindex).getNarration());
            psTemp.setString("narration", narration);
            psTemp.setString("V_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
            psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
            psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("ContraText", contraText);
            psTemp.setString("RefNo", mrTemp.getReferenceNo());
            psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
    }
}
