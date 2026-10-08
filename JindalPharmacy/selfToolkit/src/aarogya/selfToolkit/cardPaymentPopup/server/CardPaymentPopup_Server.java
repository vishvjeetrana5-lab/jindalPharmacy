package aarogya.selfToolkit.cardPaymentPopup.server;

import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentPopup_Server {

    public static CardPaymentPopup_Mdl getCardListViaDocId(String docId, Connection cnTemp) {
        List<GrdCardPayment_Mdl> lstNode = new ArrayList<GrdCardPayment_Mdl>();
        CardPaymentPopup_Mdl mrTemp = new CardPaymentPopup_Mdl();
        GrdCardPayment_Mdl node;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            mrTemp.setPaymentSource(PaymentSource.NO_PAYMENT);

            /*==============================<< Get Card detail >>===============================*/
            sqlQuery = "Select	cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, cl.amtDr, cl.amtCr, "
                    + "         cl.CardNo, cl.cardHolderName, cl.cardRegDocId, cl.cardAuthorization, sg.subcode, sg.name as acName, cm.isAccountEditable, "
                    + "		sg.nature as acNature, cm.nature as cardTypeNature, cl.physicalId, cl.currencyCode, cl.currencyAmount, "
                    + "         cl.conversionRate, cr.name as currencyName,"
                    + "         cl.cardDate,cl.posPaymentMode,cl.posPayByMobile ,cl.posPaymentDocid,cl.isPosPaymentSuccess,cl.favourOf "
                    + "		from cardLedger cl "
                    + "		left join cardTypeMast cm On cl.cardTypeCode = cm.code "
                    + "		left join subGroup sg On cl.subcode = sg.subCode "
                    + "		left join currency cr on cr.code=cl.currencyCode"
                    + "		where cl.docId = :docId "
                    + "		order By cl.V_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdCardPayment_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                node.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setCardTypeShortName(DmShared.xNull(rsTemp.getString("cardShortName")));
                node.setCardTypeNatureCode(DmShared.xNull(rsTemp.getString("cardType")));
                node.setCardTypeNatureName(DmEnum.CardType.getValueOf(DmShared.iNull(rsTemp.getString("cardType")).intValue()).getEnumName());
                node.setAcNameCode(DmShared.xNull(rsTemp.getString("subcode")));
                node.setAcNameName(DmShared.xNull(rsTemp.getString("acName")));
                node.setAcNature(DmShared.xNull(rsTemp.getString("acNature")));
                node.setCardNature(DmShared.xNull(rsTemp.getString("cardTypeNature")));
                node.setCardNo(DmShared.xNull(rsTemp.getString("CardNo")));
                node.setCardDate(DmShared.dNull(rsTemp.getDate("cardDate")));
                node.setCardPhysicalId(DmShared.xNull(rsTemp.getString("physicalId")));
                node.setCardHolderName(DmShared.xNull(rsTemp.getString("cardHolderName")));
                node.setCardAuthorization(DmShared.xNull(rsTemp.getString("cardAuthorization")));
                node.setCardAuthorizationDocId(DmShared.xNull(rsTemp.getString("cardRegDocId")));
                node.setAccountEditable(DmShared.iNull(rsTemp.getString("isAccountEditable")).intValue() == 1);
                node.setCurrencyCode(DmShared.xNull(rsTemp.getString("currencyCode")));
                node.setCurrencyName(DmShared.xNull(rsTemp.getString("currencyName")));
                node.setConversionRate(DmServer.vNull(rsTemp.getString("conversionRate")));
                node.setForeignCurrencyAmount(DmServer.vNull(rsTemp.getString("currencyAmount")));
                node.setCardFavourOf(DmShared.xNull(rsTemp.getString("favourOf")));
//                node.setBalance(DmShared.vNull(rsTemp.getString("balance")).doubleValue());
//                walletMdl = STServer.getRegisteredWallet("", DmShared.xNull(rsTemp.getString("cardRegDocId")), cnTemp);
//                node.setRegisteredWallet(walletMdl);
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setAmount(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setAmount(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setPosPaymentMode(DmShared.xNull(rsTemp.getString("posPaymentMode")));
                node.setPosPayByMobile(DmShared.xNull(rsTemp.getString("posPayByMobile")));
                node.setPosPaymentDocid(DmServer.vNull(rsTemp.getString("posPaymentDocid")));
                node.setPaymentSuccess(DmShared.iNull(rsTemp.getString("isPosPaymentSuccess")).intValue() == 1);
                lstNode.add(node);
                mrTemp.setPaymentSource(PaymentSource.PAYMENT);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getGrdCardPaymentList().addAll(lstNode);

            sqlQuery = "Select  pla.amount as creditAmt, pla.authLetterNo as authBy, pla.remark, pla.nature "
                    + "         from patientLimitApproval pla "
                    + "         where pla.docId = :docId  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("nature")).intValue() == PaymentSource.CREDIT_TO_PATIENT.getEnumValue()) {
                    if (mrTemp.getPaymentSource() == PaymentSource.PAYMENT) {
                        mrTemp.setPaymentSource(PaymentSource.PARTIAL_PAYMENT);
                    } else {
                        mrTemp.setPaymentSource(PaymentSource.CREDIT_TO_PATIENT);
                    }

                } else if (DmShared.iNull(rsTemp.getString("nature")).intValue() == PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) {
                    mrTemp.setPaymentSource(PaymentSource.CREDIT_TO_ORGANIZATION);

                } else if (DmShared.iNull(rsTemp.getString("nature")).intValue() == PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) {
                    mrTemp.setPaymentSource(PaymentSource.CREDIT_TO_REGISTRATION);

                }

                node = new GrdCardPayment_Mdl();
                mrTemp.setCreditAmount(DmServer.vNull(rsTemp.getString("CreditAmt"), "0.00"));

                if (mrTemp.getPaymentSource() == PaymentSource.PAYMENT
                        || mrTemp.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT
                        || mrTemp.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT) {
                    mrTemp.setAuthBy(DmShared.xNull(rsTemp.getString("authBy")));
                    mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                } else if (mrTemp.getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
                    mrTemp.setAuthBy("");
                    mrTemp.setRemark("");

                } else if (mrTemp.getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                    mrTemp.setAuthBy("");
                    mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                }

            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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

    public static List<GrdCardPayment_Mdl> getCardListViaCardSName(String[] cardTypeSName, Connection cnTemp) {
        return getCardList(false, cardTypeSName, cnTemp);
    }

    public static List<GrdCardPayment_Mdl> getCardListViaCardCode(String[] cardTypeCode, Connection cnTemp) {
        return getCardList(true, cardTypeCode, cnTemp);
    }

    public static List<GrdCardPayment_Mdl> getCardList(boolean isCardCode, String[] cardTypeName, Connection cnTemp) {
        List<GrdCardPayment_Mdl> lstTemp = new ArrayList<>();
        GrdCardPayment_Mdl listNode;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, cardTypeList = "", condition = "";

        try {
            if (cardTypeName != null) {
                for (int i = 0; i < cardTypeName.length; i++) {
                    if (!DmShared.xNull(cardTypeName[i]).trim().equalsIgnoreCase("")) {
                        if (!cardTypeList.trim().equalsIgnoreCase("")) {
                            cardTypeList += ",";
                        }
                        cardTypeList += DmShared.xNull(cardTypeName[i]);
                    }
                }
            }

            if (!cardTypeList.trim().equalsIgnoreCase("")) {
                if (isCardCode) {
                    condition = " and cm.code in (" + DmServer.getParamList(":cardValues", cardTypeList) + ") ";
                } else {
                    condition = " and cm.shortName in (" + DmServer.getParamList(":cardValues", cardTypeList) + ") ";
                }
            }

            /*==============================<< Get Card detail >>===============================*/
            sqlQuery = "Select  cm.code as cardTypeCode, cm.Name as cardTypeName, cm.shortName as cardShortName, cm.currencyCode, crr.name as currencyName, "
                    + "         sg.subcode, sg.name as acName, sg.nature as acNature, cm.cardType, cm.isAccountEditable, cm.nature as cardTypeNature "
                    + "         from cardTypeMast cm "
                    + "         left join subGroup sg On cm.subcode = sg.subCode  "
                    + "        	left join currency crr on crr.code=cm.currencyCode "
                    + "         where cm.isActive = 1 " + condition
                    + "         order by cm.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setStringList("cardValues", cardTypeList, true);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdCardPayment_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                listNode.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardTypeShortName(DmShared.xNull(rsTemp.getString("cardShortName")));
                listNode.setCardTypeNatureCode(DmShared.xNull(rsTemp.getString("cardType")));
                listNode.setCardTypeNatureName(DmEnum.CardType.getValueOf(DmShared.iNull(rsTemp.getString("cardType")).intValue()).getEnumName());
                listNode.setAcNameCode(DmShared.xNull(rsTemp.getString("subcode")));
                listNode.setAcNameName(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setAcNature(DmShared.xNull(rsTemp.getString("acNature")));
                listNode.setCardNature(DmShared.xNull(rsTemp.getString("cardTypeNature")));
                listNode.setCurrencyCode(DmShared.xNull(rsTemp.getString("currencyCode")));
                listNode.setCurrencyName(DmShared.xNull(rsTemp.getString("currencyName")));
                listNode.setConversionRate(getBestFitRate(DmShared.iNull(rsTemp.getString("currencyCode")).toString(), cnTemp));
                listNode.setAccountEditable(DmShared.iNull(rsTemp.getString("isAccountEditable")).intValue() == 1);
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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

    public static String getBestFitRate(String currencyCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, conversionRate = "";

        try {
            sqlQuery = "Select	top 1 cem.currencyCode, cem.conversionRate "
                    + "		from currencyExchangeMast as cem "
                    + "		where cem.exchangeDate <= :exchangeDate and cem.currencyCode = :currencyCode "
                    + "		order by cem.exchangeDate desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("currencyCode", DmShared.iNull(currencyCode).toString());
            psTemp.setString("exchangeDate", DmServer.getServerDateTime()[0]);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                conversionRate = DmServer.vNull(rsTemp.getString("conversionRate"), "0.0000000");
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
        return conversionRate;
    }
//    public static void chkSave(boolean isDebit, String netAmount, String docId, CardPaymentPopup_Mdl cardPaymentPopup_Mdl, Connection cnTemp) {
//        chkSave(isDebit, netAmount, docId, cardPaymentPopup_Mdl, cnTemp, false, true);
//    }
//    public static void chkSave(boolean isDebit, String netAmount, String docId, CardPaymentPopup_Mdl cardPaymentPopup_Mdl, Connection cnTemp, boolean isCTRAllowed) {
//        chkSave(isDebit, netAmount, docId, cardPaymentPopup_Mdl, cnTemp, isCTRAllowed, false, false);
//    }

    public static void chkSave(boolean isDebit, String netAmount, String docId, CardPaymentPopup_Mdl cardPaymentPopup_Mdl, Connection cnTemp, boolean isCTRAllowed, boolean isCTOAllowed, boolean isCTPAllowed) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {
            CardPaymentPopup_Vld.chkSave(netAmount, cardPaymentPopup_Mdl, isCTRAllowed, isCTOAllowed, isCTPAllowed);
            STServer.checkCardBalance(cardPaymentPopup_Mdl.getGrdCardPaymentList(), docId, isDebit, cnTemp);
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

    public static void cancel(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        /*==============================<< Update record of patientLimitApproval >>===============================*/
        sqlQuery = "update patientLimitApproval set isCancelled = 1 Where docId = :docId ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", DmShared.iNull(docId).toString());
        psTemp.executeUpdate();
        psTemp.close();

        /*==============================<< Update record of cardLedger >>===============================*/
        sqlQuery = "update cardLedger set isCancelled = 1 Where docId = :docId ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", DmShared.iNull(docId).toString());
        psTemp.executeUpdate();
        psTemp.close();
    }

    public static void delete(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        /*==============================<< Delete records from patientLimitApproval >>==========================*/
        sqlQuery = "Delete From patientLimitApproval Where DocId = :docId ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", DmShared.iNull(docId).toString());
        psTemp.executeUpdate();
        psTemp.close();

        /*==============================<< Delete records from cardLedger >>==========================*/
        sqlQuery = "Delete From cardLedger Where docId = :docId ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("docId", DmShared.iNull(docId).toString());
        psTemp.executeUpdate();
        psTemp.close();
    }

    public static String getPaymentInString(List<GrdCardPayment_Mdl> cardPaymentList) {
        String cardList = "";

        for (int i = 0; i < cardPaymentList.size(); i++) {
            if (!DmShared.xNull(cardPaymentList.get(i).getCardTypeName()).equalsIgnoreCase("")) {
                if (!cardList.trim().equalsIgnoreCase("")) {
                    cardList += ", ";
                }
                cardList += DmShared.xNull(cardPaymentList.get(i).getCardTypeName()) + "(" + DmServer.vNull(cardPaymentList.get(i).getAmount(), "0.00") + ")";
            }
        }

        return cardList;
    }

    public static void save(boolean isDebit, CardPaymentPopup_Mdl cardPayment_Mdl, String regDocId, String docId, String recId, String vType, String siteCode, String vDate, String preparedBy, Connection cnTemp) throws Exception {
        save(isDebit, cardPayment_Mdl, regDocId, docId, recId, vType, siteCode, vDate, preparedBy, "", cnTemp);
    }

    public static void save(boolean isDebit, CardPaymentPopup_Mdl cardPayment_Mdl, String regDocId, String docId, String recId, String vType, String siteCode, String vDate, String preparedBy, String sbDocid, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        int srNo = 200;
        delete(docId, cnTemp);

        if (cardPayment_Mdl.getPaymentSource() == PaymentSource.PAYMENT
                || cardPayment_Mdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT) {

            for (int i = 0; i < cardPayment_Mdl.getGrdCardPaymentList().size(); i++) {
                GrdCardPayment_Mdl grdCardPayment_Mdl = cardPayment_Mdl.getGrdCardPaymentList().get(i);
                if (!DmShared.xNull(grdCardPayment_Mdl.getCardTypeCode()).equalsIgnoreCase("")) {
                    srNo = srNo + 1;
                    /*==============================<< Insert record into cardLedger >>==========================*/
                    sqlQuery = "Insert into cardLedger(docId, v_SNo, v_Type, recId, v_Date, cardTypeCode, cardNo, subCode, "
                            + " amtDr, amtCr, site_Code, cardHolderName, cardAuthorization,cardRegDocID,physicalId,"
                            + " currencyCode,currencyAmount,conversionRate, cardDate,posPaymentMode,posPayByMobile,posPaymentDocid,"
                            + "isPosPaymentSuccess,favourOf) "
                            + " values (:docId, :v_SNo, :v_Type, :recId, :v_Date, :cardTypeCode, :cardNo, :subCode, "
                            + " :amtDr, :amtCr, :site_Code, :cardHolderName, :cardAuthorization,:cardRegDocID,:physicalId,"
                            + " :currencyCode,:currencyAmount,:conversionRate, :cardDate,:posPaymentMode,"
                            + ":posPayByMobile,:posPaymentDocid,:isPosPaymentSuccess,:favourOf) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(docId).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(srNo).toString());
//                    psTemp.setString("v_Type", vType);
//                    psTemp.setString("recId", recId);
                    psTemp.setString("v_Type", DmShared.iNull(cardPayment_Mdl.getReceiptTypeCode()).toString());
                    psTemp.setString("recId", DmShared.xNull(cardPayment_Mdl.getReceiptNo()));
                    psTemp.setDate("v_Date", DmServer.convertDate(vDate));
                    psTemp.setString("cardTypeCode", DmShared.foriegnKey(grdCardPayment_Mdl.getCardTypeCode()));
                    psTemp.setString("cardNo", DmShared.xNull(grdCardPayment_Mdl.getCardNo()));
                    psTemp.setDate("cardDate", DmServer.convertDate(grdCardPayment_Mdl.getCardDate()));
                    psTemp.setString("posPaymentMode", DmShared.xNull(grdCardPayment_Mdl.getPosPaymentMode()));
                    psTemp.setString("posPayByMobile", DmShared.xNull(grdCardPayment_Mdl.getPosPayByMobile()));
                    psTemp.setString("posPaymentDocid", DmShared.iNull(grdCardPayment_Mdl.getPosPaymentDocid()).toString());
                    psTemp.setString("isPosPaymentSuccess", grdCardPayment_Mdl.isPaymentSuccess() ? "1" : "0");
                    psTemp.setString("favourOf", grdCardPayment_Mdl.getCardFavourOf());
                    psTemp.setString("subCode", DmShared.foriegnKey(grdCardPayment_Mdl.getAcNameCode()));
                    if (isDebit) {
                        psTemp.setString("amtDr", DmServer.vNull(grdCardPayment_Mdl.getAmount(), "0.00"));
                        psTemp.setString("amtCr", "0");
                    } else {
                        psTemp.setString("amtDr", "0");
                        psTemp.setString("amtCr", DmServer.vNull(grdCardPayment_Mdl.getAmount(), "0.00"));
                    }
                    psTemp.setString("site_Code", siteCode);
                    psTemp.setString("cardHolderName", grdCardPayment_Mdl.getCardHolderName());
                    psTemp.setString("cardAuthorization", grdCardPayment_Mdl.getCardAuthorization());
                    psTemp.setString("cardRegDocID", DmShared.foriegnKey(grdCardPayment_Mdl.getCardAuthorizationDocId()));
                    psTemp.setString("physicalId", DmShared.foriegnKey(grdCardPayment_Mdl.getCardPhysicalId()));
                    psTemp.setString("currencyCode", DmShared.foriegnKey(grdCardPayment_Mdl.getCurrencyCode()));
                    psTemp.setString("currencyAmount", DmServer.vNull(grdCardPayment_Mdl.getForeignCurrencyAmount()));
                    psTemp.setString("conversionRate", DmServer.vNull(grdCardPayment_Mdl.getConversionRate()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

        }

        /*==============================<< Insert record into patientLimitApproval >>==========================*/
        if (cardPayment_Mdl.getPaymentSource()
                == PaymentSource.PARTIAL_PAYMENT
                || cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT
                || cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION
                || cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
            if (DmShared.vNull(cardPayment_Mdl.getCreditAmount()).doubleValue() != 0) {
                sqlQuery = "Insert into patientLimitApproval(docId, v_No, v_Type, recId, v_Date, amount, site_Code, authLetterNo, "
                        + " remark, preparedBy ,u_EntDt, isCancelled, nature,regDocId, sbDocid) "
                        + " values (:docId, :v_No, :v_Type, :recId, :v_Date, :amount, :site_Code, :authLetterNo, "
                        + " :remark,:preparedBy, :uEntDt, :isCancelled, :nature,:regDocId, :sbDocid)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("v_No", DmShared.iNull(srNo).toString());
                psTemp.setString("v_Type", vType);
                psTemp.setString("recId", recId);
                psTemp.setDate("v_Date", DmServer.convertDate(vDate));
                psTemp.setString("amount", DmServer.vNull(cardPayment_Mdl.getCreditAmount(), "0.00"));
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("authLetterNo", DmShared.xNull(cardPayment_Mdl.getAuthBy()));
                psTemp.setString("remark", DmShared.xNull(cardPayment_Mdl.getRemark()));
                psTemp.setString("regDocId", DmShared.foriegnKey(regDocId));
                psTemp.setString("sbDocid", DmShared.foriegnKey(sbDocid));
                psTemp.setString("isCancelled", "0");
                psTemp.setString("preparedBy", DmShared.xNull(preparedBy));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());

                if (cardPayment_Mdl.getPaymentSource() == PaymentSource.PARTIAL_PAYMENT
                        || cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_PATIENT) {
                    psTemp.setString("nature", String.valueOf(PaymentSource.CREDIT_TO_PATIENT.getEnumValue()));

                } else if (cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
                    psTemp.setString("nature", String.valueOf(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()));

                } else if (cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_REGISTRATION) {
                    psTemp.setString("nature", String.valueOf(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()));
                }

                psTemp.executeUpdate();
                psTemp.close();

                if (cardPayment_Mdl.getPaymentSource() == PaymentSource.CREDIT_TO_ORGANIZATION) {
                    if (DmShared.iNull(regDocId).longValue() == 0) {
                        throw new UnsupportedOperationException("In case of CTO you have to mention Registration No.");
                    }

                    sqlQuery = "Select	pr.regDocId "
                            + "         from patientRegistration pr "
                            + "         where pr.regDocId = :regDocId and pr.paymentSource <> :paymentSource ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("regDocId", DmShared.iNull(regDocId).toString());
                    psTemp.setString("paymentSource", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        throw new UnsupportedOperationException("In case of CTO selected Registration should be of CTO too.");
                    }
                    rsTemp.close();
                    psTemp.close();

//                    sqlQuery = "select	pr.regDocId,pla.docId as plaDocId,prc.docId as prcDocId "
//                            + "from patientRegistration pr "
//                            + "left join patientLimitApproval pla on pr.regDocId = pla.docId and pla.nature = " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " "
//                            + "left join patientReceipt prc on pr.regDocId = prc.docId "
//                            + "where pr.regDocId = :regDocId ";
//                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                    psTemp.setString("regDocId", regDocId);
//                    rsTemp = psTemp.executeQuery();
//                    if (rsTemp.next()) {
//                        if (DmShared.iNull(rsTemp.getString("plaDocId")).longValue() > 0
//                                && DmShared.iNull(rsTemp.getString("prcDocId")).longValue() == 0) {
//
//                        } else {
//                            throw new UnsupportedOperationException("In case of CTO selected Registration should be of CTO too.");
//                        }
//                    }
//                    rsTemp.close();
//                    psTemp.close();
                }
            }
        }

    }
}
