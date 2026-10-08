package aarogya.master.cardDischarge.server;

import aarogya.master.cardDischarge.shared.*;
import aarogya.selfToolkit.cardPaymentPopup.server.CardPaymentPopup_Server;
import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardDischarge_SrvImpl extends DmRSSPrivate implements CardDischarge_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private final String vCat = "'" + pvc.CARD_DISCHARGE + "'";
    private final String voucherTypefieldName = "gr.v_Type", voucherDateFieldName = "gr.v_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstTmp;
        String sqlQuery = "", sqlCondition = "";
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  docId "
                    + "         from generalReceipt gr "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         where gr.v_Prefix = :vPrefix and gr.site_Code = :siteCode and vt.category in (" + vCat + ") ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlQuery += " order by gr.v_Date desc, vt.short_Name, gr.v_no desc, gr.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstTmp = new KeyFieldExtended();
                lstTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstTmp);
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
    public DmResult<CardDischarge_Mdl> getMoveRec(String keyCode) {
        DmResult<CardDischarge_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double netReceipt = 0;
        CardDischarge_Mdl mrTemp = new CardDischarge_Mdl();
        RegisteredWallet_Mdl cardMdl;
        GrdCardPayment_Mdl node;
        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  gr.docId, gr.v_Type, gr.v_No, gr.recIdPrefix, gr.recId, gr.v_Date, gr.remark, gr.u_Name, gr.preparedBy, gr.isCancelled, "
                    + "		cl.cardRegDocId, cr.recId as cardRegNo, cr.v_Date as allotedDate, cr.expiryDate, cl.subCode, sg.name linkAccount, "
                    + "         cl.cardNo, cl.cardHolderName, cl.amtDr, vt.description, ctm.name as cardTypeName, cr.mobile, cl.cardTypeCode, "
                    + "         cr.physicalId "
                    + "		from generalReceipt AS gr "
                    + "		left join cardLedger cl on cl.docId = gr.docId and cl.v_SNo = 1 "
                    + "		left join cardRegistration cr on cr.docId = cl.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "		left join subGroup sg on sg.subCode = cl.subCode "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
                    + "         where gr.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setAmount(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                cardMdl = new RegisteredWallet_Mdl();
                cardMdl.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                cardMdl.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                cardMdl.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                cardMdl.setContactPerson(DmShared.xNull(rsTemp.getString("cardHolderName")));
                cardMdl.setLinkAccountCode(DmShared.xNull(rsTemp.getString("subCode")));
                cardMdl.setLinkAccountName(DmShared.xNull(rsTemp.getString("linkAccount")));
                cardMdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardRegDocId")));
                cardMdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardRegNo")));
                cardMdl.setPhysicalId(DmShared.xNull(rsTemp.getString("physicalId")));
                mrTemp.setCardRegMdl(cardMdl);
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "select  cl.docId, cl.v_Type, cl.v_SNo, cl.recId, cl.v_Date, cl.cardRegDocId, cl.subCode, sg.name linkAccount, cl.cardNo, "
                    + "         cl.cardHolderName, cl.amtCr, ctm.name as cardTypeName, ctm.nature, ctm.cardType, ctm.shortName as cardShortName, "
                    + "         cl.cardAuthorization, cl.cardTypeCode "
                    + "		from cardLedger cl "
                    + "		left join cardRegistration cr on cr.docId = cl.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "		left join subGroup sg on sg.subCode = cl.subCode"
                    + "		where cl.v_Sno <> 1 and cl.docId = :docId "
                    + "         order by cl.v_Sno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCardPayment_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setAcNameCode(DmShared.xNull(rsTemp.getString("subCode")));
                node.setAcNameName(DmShared.xNull(rsTemp.getString("linkAccount")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                node.setCardAuthorization(DmShared.xNull(rsTemp.getString("cardAuthorization")));
                node.setCardHolderName(DmShared.xNull(rsTemp.getString("cardHolderName")));
                node.setCardNature(DmShared.xNull(rsTemp.getString("nature")));
                node.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                node.setCardTypeShortName(DmShared.xNull(rsTemp.getString("cardShortName")));
                node.setCardTypeNatureCode(DmShared.iNull(rsTemp.getString("cardType")).toString());
                node.setCardTypeNatureName(CardType.getValueOf(DmShared.iNull(rsTemp.getString("cardType")).intValue()).getEnumName());
                netReceipt += DmShared.vNull(node.getAmount()).doubleValue();
                mrTemp.getLstPayment().add(node);
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.setNetReceipt(DmServer.vNull(netReceipt, "0.00"));
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
    public DmResult<String> saveRecord(CardDischarge_Mdl mrTemp, String voucherPrefix, UserPermission userPermission) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        RegisteredWallet_Mdl cardMdl;
        GrdCardPayment_Mdl paymentMdl;
        List<GrdCardPayment_Mdl> lstPayment = new ArrayList<>();
        boolean blnAdd = false;
        int vSno = 0;
        String sqlQuery = "", userName, siteCode, insufficientException = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            CardDischarge_Vld.chkSave(mrTemp);
            cardMdl = mrTemp.getCardRegMdl();

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("generalReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=======================================<Check Header Cards Values>=======================================*/
            paymentMdl = new GrdCardPayment_Mdl();
            paymentMdl.setCardAuthorizationDocId(cardMdl.getSharedKeyFieldCode());
            paymentMdl.setCardTypeNatureCode(DmShared.iNull(CardType.WALLET.getEnumValue()).toString());
            paymentMdl.setAmount(DmServer.vNull(mrTemp.getAmount(), "0.00"));
            lstPayment.add(paymentMdl);
            STServer.checkCardBalance(lstPayment, mrTemp.getSharedKeyFieldCode(), true, cnTemp);
            /*=======================================================================================================*/

//            /*=======================================<Check Grid Cards Values>=======================================*/
//            STServer.checkCardBalance(mrTemp.getLstPayment(), mrTemp.getSharedKeyFieldCode(), false, cnTemp);
//            /*=======================================================================================================*/
//            sqlQuery = "select  count(cardRegDocId) as cnt "
//                    + "         from generalReceipt gr "
//                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type "
//                    + "         left join cardLedger cl on cl.docId = gr.docId and cl.v_Sno = 1 "
//                    + "         where vt.category in ('" + pvc.CARD_DEACTIVE + "') and cl.cardRegDocId = :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
//                    throw new DmException(0, "This card registration has been deactive. you can not modify this record.");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
            STServer.checkEOD("generalReceipt", "docId", mrTemp.getSharedKeyFieldCode(), cnTemp);
            if (blnAdd) {
                sqlQuery = "INSERT INTO generalReceipt(DocId,v_Type,recId,recIdPrefix,v_No,v_Date,v_Prefix,site_Code,preparedBy,u_EntDt,remark) "
                        + " VALUES (:docId,:v_Type,:recID,:recIdPrefix,:v_No,:v_Date,:v_Prefix,:siteCode,:preparedBy, :u_EntDt,:remark) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecId().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("siteCode", siteCode);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "UPDATE generalReceipt SET "
                        + " u_EntDt =  :u_EntDt,"
                        + " u_Name = :userName, "
                        + " remark =:remark "
                        + " WHERE docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("userName", userName);
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From CardLedger Where DocId = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Insert Into cardLedger (docId, v_SNo, v_Type, recId, v_Date,cardAuthorization,cardHolderName, cardTypeCode, cardNo, "
                    + " subCode, amtDr, amtCr, narration, site_Code, cardRegDocId, u_Name, u_EntDt) "
                    + " values (:docId, :v_SNo, :v_Type, :recId,  :v_Date,:cardAuthorization,:cardHolderName,:cardTypeCode, :cardNo, "
                    + " :subCode, :amtDr, :amtCr, :narration, :site_Code, :cardRegDocId, :u_Name, :u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
            psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
            psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setInt("v_SNo", 1);
            psTemp.setString("cardAuthorization", DmShared.xNull(cardMdl.getSharedKeyFieldName()));
            psTemp.setString("cardHolderName", DmShared.foriegnKey(cardMdl.getContactPerson()));
            psTemp.setString("cardTypeCode", DmShared.foriegnKey(cardMdl.getCardTypeCode()));
            psTemp.setString("cardNo", DmShared.foriegnKey(cardMdl.getCardNo()));
            psTemp.setString("subCode", DmShared.foriegnKey(cardMdl.getLinkAccountCode()));
            psTemp.setString("amtDr", DmServer.vNull(mrTemp.getAmount(), "0.00"));
            psTemp.setString("cardRegDocId", DmShared.foriegnKey(cardMdl.getSharedKeyFieldCode()));
            psTemp.setString("amtCr", "0");
            psTemp.setString("narration", null);
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("u_Name", userName);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            vSno = 2;
            for (GrdCardPayment_Mdl grdMdl : mrTemp.getLstPayment()) {
                if (DmShared.iNull(grdMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    sqlQuery = "Insert Into cardLedger (docId, v_SNo, v_Type, recId, v_Date,cardAuthorization, cardHolderName,cardTypeCode, cardNo, "
                            + " subCode, amtDr, amtCr, narration, site_Code, cardRegDocId, u_Name, u_EntDt) "
                            + " values (:docId, :v_SNo, :v_Type, :recId,  :v_Date,:cardAuthorization,:cardHolderName ,:cardTypeCode, :cardNo, "
                            + " :subCode, :amtDr, :amtCr, :narration, :site_Code, :cardRegDocId, :u_Name, :u_EntDt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("recId", DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
                    psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                    psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setInt("v_SNo", vSno++);
                    psTemp.setString("cardAuthorization", DmShared.xNull(grdMdl.getCardAuthorization()));
                    psTemp.setString("cardHolderName", DmShared.foriegnKey(grdMdl.getCardHolderName()));
                    psTemp.setString("cardTypeCode", DmShared.foriegnKey(grdMdl.getSharedKeyFieldCode()));
                    psTemp.setString("cardNo", DmShared.foriegnKey(grdMdl.getCardNo()));
                    psTemp.setString("subCode", DmShared.foriegnKey(grdMdl.getAcNameCode()));
                    psTemp.setString("amtCr", DmServer.vNull(grdMdl.getAmount(), "0.00"));
                    psTemp.setString("amtDr", "0");
                    psTemp.setString("narration", null);
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("cardRegDocId", DmShared.foriegnKey(grdMdl.getCardAuthorizationDocId()));
                    psTemp.setString("u_Name", userName);
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            sqlQuery = "select  count(cl.cardRegDocId) as cnt "
                    + "         from cardLedger cl "
                    + "         where cl.docId = :docId and cl.v_Sno <> 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).longValue() > 0) {
                    throw new DmException(0, "Wallet/Card is not allow in payment mode to Dicharge.");
                }
            }
            rsTemp.close();
            psTemp.close();
//            sqlQuery = "select  cl.cardRegDocId, cl.cardNo, ctm.isCheckLimit "
//                    + "         from cardLedger cl "
//                    + "         left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
//                    + "         where cl.docId = :docId and cl.v_Sno = 1 ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                if (DmShared.iNull(rsTemp.getString("cardRegDocId")).longValue() > 0 && DmShared.iNull(rsTemp.getString("isCheckLimit")).intValue() == 1) {
//                    if (DmShared.vNull(STServer.getCardRegistrationBalance(DmShared.xNull(rsTemp.getString("cardRegDocId")), cnTemp)).doubleValue() >= 0) {
//                        insufficientException += "Insufficient balance in cardNo.: <b>" + DmShared.xNull(rsTemp.getString("cardNo")) + "<b><br/>";
//                    }
//                }
//            }
//            rsTemp.close();
//            psTemp.close();
            if (!insufficientException.trim().equalsIgnoreCase("")) {
                throw new DmException(0, insufficientException);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey, String V_Date) {
        return null;
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
        String sqlQuery, sqlCondition;
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select  gr.docId, gr.v_Type, gr.v_No, gr.recIdPrefix, gr.recId, gr.v_Date, gr.remark, gr.u_Name, gr.preparedBy, gr.isCancelled, "
                    + "		cl.cardRegDocId, cr.recId as cardRegNo, cr.v_Date as allotedDate, cr.expiryDate, cl.subCode, sg.name linkAccount, "
                    + "         cl.cardNo, cl.cardHolderName, cl.amtDr, vt.description, ctm.name as cardTypeName, cr.mobile, vt.short_Name "
                    + "		from generalReceipt AS gr "
                    + "		left join cardLedger cl on cl.docId = gr.docId and cl.v_SNo = 1 "
                    + "		left join cardRegistration cr on cr.docId = cl.cardRegDocId "
                    + "		left join cardTypeMast ctm on ctm.code = cl.cardTypeCode "
                    + "		left join subGroup sg on sg.subCode = cl.subCode "
                    + "         left join voucher_Type vt on vt.v_Type = gr.v_Type"
                    + "         where gr.v_Prefix = :vPrefix and gr.site_Code = :siteCode and vt.category in (" + vCat + ") ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("description")));
                listNode.setCardRegNo(DmShared.xNull(rsTemp.getString("cardRegNo")));
                listNode.setCardType(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                listNode.setLinkAccount(DmShared.xNull(rsTemp.getString("linkAccount")));
                listNode.setPersonName(DmShared.xNull(rsTemp.getString("cardHolderName")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                listNode.setAmount(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
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
    public DmResultPageing<List<RegisteredWallet_Mdl>> getTxtHCardNo(String voucherDate, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<RegisteredWallet_Mdl>> rstTemp = new DmResultPageing<>();
        List<RegisteredWallet_Mdl> lstTemp = new ArrayList<>();
        RegisteredWallet_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  cr.docId, cr.recId, cr.v_Date, cr.v_No, vt.short_Name, cr.personName, cs.cardNo, "
                    + "		cr.mobile, ctm.name as cardTypeName "
                    + "		from cardStock cs "
                    + "		left join cardRegistration cr on cr.docId = cs.cardRegDocId "
                    + "		left join voucher_Type vt on vt.v_Type = cr.v_Type "
                    + "		left join cardTypeMast ctm on ctm.code = cr.cardTypeCode "
                    + "		where cs.cardRegDocId is not null and isnull(cr.expiryDate, :voucherDate) >= :voucherDate ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setDate("voucherDate", DmServer.convertDate(voucherDate));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new RegisteredWallet_Mdl();
                node.setCardNo(DmShared.xNull(rsTemp.getString("cardNo")));
                node.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setContactPerson(DmShared.xNull(rsTemp.getString("personName")));
                node.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                lstTemp.add(node);
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT  vt.v_Type, vt.description, vt.attachPaymentMode, vt.isSystemDefine"
                    + "         FROM voucher_Type vt "
                    + "         WHERE vt.short_Name = '" + pvc.CARD_DISCHARGE + "' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherText(DmShared.xNull(rsTemp.getString("Description")));
                defaultValue.setVoucherTag(DmShared.xNull(rsTemp.getString("V_Type")));
                defaultValue.setPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));
                defaultValue.setIsDefault(DmShared.xNull(rsTemp.getString("isSystemDefine")));
                if (!DmShared.xNull(defaultValue.getPaymentMode()).equalsIgnoreCase("")) {
                    String[] pmArray = DmShared.xNull(defaultValue.getPaymentMode()).split("\\|");
                    defaultValue.setCardMdl(CardPaymentPopup_Server.getCardListViaCardCode(new String[]{DmShared.xNull(pmArray[0])}, cnTemp).get(0));
                }
            }
            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setCurrentTime(DmServer.getServerDateTime()[1]);

            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return dmrTemp;
    }

    @Override
    public DmResult<RegisteredWallet_Mdl> getCardDetail(String cardNo, String physicalId, String docId) {
        DmResult<RegisteredWallet_Mdl> dmrTemp = new DmResult<RegisteredWallet_Mdl>();
        Connection cnTemp = null;
        RegisteredWallet_Mdl mrTemp;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.iNull(docId).longValue() > 0) {
                cardNo = DmServer.getCodeViaManualCode(docId, "docId", "cardRegistration", "cardNo", cnTemp);
            }
            mrTemp = STServer.getRegisteredWallet(cardNo, physicalId, "", cnTemp);

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setStatus(false);
            dmrTemp.setErrorMessage(e.getMessage());
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

}
