package aarogya.selfToolkit.cardPaymentPopup.server;

import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.ChequePrintSetup_Mdl;
import aarogya.selfToolkit.stStatic.shared.RegisteredWallet_Mdl;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmJasperCompiler;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.razorPay.server.RazorPayPOS_Server;
import dataman.dmToolkit.razorPay.shared.param.RazorPayPosPmtGatewayParam;
import dataman.dmToolkit.razorPay.shared.pmtIntegration.shared.PmtGatewayDetail_Mdl;
import dataman.dmToolkit.razorPay.shared.pmtStatusRes.GetStatusResponse_Mdl;
import java.sql.Connection;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class CardPaymentPopup_SrvImpl extends DmRSSPrivate implements CardPaymentPopup_Srv {

    @Override
    public DmResultPageing<List<GrdCardPayment_Mdl>> getTxtHCardType(String[] includeCardType, DmHelper helper, boolean blnToEndOfFile, String receiptType) {
        DmResultPageing<List<GrdCardPayment_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdCardPayment_Mdl> lstTemp = new ArrayList<>();
        GrdCardPayment_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", cardSNameList = "", cardSNameList1 = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (includeCardType != null) {

                if (!(DmServer.getSsnUserName(this).equalsIgnoreCase("SA") || DmServer.getSsnUserName(this).equalsIgnoreCase("Super"))) {

                    sqlQuery = "select paymentTypeCode from " + DmServer.dbCompany + ".dbo.userPayModePermission \n"
                            + "where userName = '" + DmServer.getSsnUserName(this) + "' \n"
                            + "And v_Type = :v_Type ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("v_Type", receiptType);
                    rsTemp = psTemp.executeQuery();
                    while (rsTemp.next()) {
                        String code = DmShared.xNull(rsTemp.getString("paymentTypeCode"));
                        if (!cardSNameList1.equalsIgnoreCase("")) {
                            cardSNameList1 += ",";
                        }
                        cardSNameList1 += "'" + code + "'";
                    }

                }
            }

            for (String sName : includeCardType) {

                if (!DmShared.xNull(sName).trim().equalsIgnoreCase("")) {
                    if (!cardSNameList.equalsIgnoreCase("")) {
                        cardSNameList += ",";
                    }
                    cardSNameList += "'" + sName.trim() + "'";
                }

            }

            sqlQuery = "Select  cm.code as cardTypeCode, cm.Name as cardTypeName, cm.shortName as cardShortName, cm.currencyCode, cm.isAccountEditable, "
                    + "         crr.name as currencyName, sg.subcode, sg.name as acName, sg.nature as acNature, cm.cardType, cm.nature as cardTypeNature "
                    + "         from cardTypeMast cm "
                    + "         left join subGroup sg on cm.subcode = sg.subCode "
                    + "         left join currency crr on crr.code=cm.currencyCode "
                    + "         where cm.isActive = 1 ";
//            if (!cardSNameList1.equalsIgnoreCase("") && !cardSNameList.equalsIgnoreCase("")) {
//                sqlQuery += "and cm.code in (" + DmServer.getParamList(":shortName", cardSNameList) + ")  and cm.code in (" + DmServer.getParamList(":shortName1", cardSNameList1) + ")";
//
//                sqlWithParam.setStringList("shortName1", cardSNameList1);
//
//            } else {
//                sqlQuery += "and cm.code in (" + DmServer.getParamList(":shortName", cardSNameList) + ")";
//            }
//
//            sqlWithParam.setSQL(sqlQuery);
//            if (!cardSNameList.equalsIgnoreCase("")) {
//                sqlWithParam.setStringList("shortName", cardSNameList);
//            }

            if (!cardSNameList1.equalsIgnoreCase("") && !cardSNameList.equalsIgnoreCase("")) {
                sqlQuery += "and cm.code in (" + DmServer.getParamList(":shortName", cardSNameList) + ")  and cm.code in (" + DmServer.getParamList(":shortName1", cardSNameList1) + ")";

                sqlWithParam.setStringList("shortName1", cardSNameList1);
                sqlWithParam.setStringList("shortName", cardSNameList);

            } else if (!cardSNameList.equalsIgnoreCase("")) {

                sqlQuery += "and cm.code in (" + DmServer.getParamList(":shortName", cardSNameList) + ")";
                sqlWithParam.setStringList("shortName", cardSNameList);

            }

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdCardPayment_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                listNode.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardTypeNatureCode(DmShared.xNull(rsTemp.getString("cardType")));
                listNode.setCardTypeNatureName(CardType.getValueOf(DmShared.iNull(rsTemp.getString("cardType")).intValue()).getEnumName());
                listNode.setCardTypeShortName(DmShared.xNull(rsTemp.getString("cardShortName")));
                listNode.setAccountEditable(DmShared.iNull(rsTemp.getString("isAccountEditable")).intValue() == 1);
                listNode.setAcNameCode(DmShared.xNull(rsTemp.getString("subcode")));
                listNode.setAcNameName(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setAcNature(DmShared.xNull(rsTemp.getString("acNature")));
                listNode.setCardNature(DmShared.xNull(rsTemp.getString("cardTypeNature")));
                listNode.setCurrencyCode(DmShared.xNull(rsTemp.getString("currencyCode")));
                listNode.setCurrencyName(DmShared.xNull(rsTemp.getString("currencyName")));
                listNode.setConversionRate(CardPaymentPopup_Server.getBestFitRate(DmShared.iNull(rsTemp.getString("currencyCode")).toString(), cnTemp));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccounts(String receiptType, DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select  sg.subCode, sg.name, sg.nature "
                    + "		from subGroup sg "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :siteCode "
                    + "		and sg.nature in (select nature from acFilteration where v_Type = :vType)");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vType", DmShared.iNull(receiptType).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("nature")));
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
    public DmResult<RegisteredWallet_Mdl> getWalletInfo(String cardNo, String physicalId, String uhid) {
        DmResult<RegisteredWallet_Mdl> dmrTemp = new DmResult<RegisteredWallet_Mdl>();
        Connection cnTemp = null;
        RegisteredWallet_Mdl mrTemp = new RegisteredWallet_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            mrTemp = STServer.getRegisteredWallet(cardNo, physicalId, uhid, cnTemp);

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

    @Override
    public DmResult<PaymentProcess_Mdl> getMakePayment(PaymentProcess_Mdl mrTemp) {
        DmResult<PaymentProcess_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, vNo = "";
        DmSavePoint spBegin = null;

        String vType = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            vType = DmServer.getCodeViaManualCode("PSPMT", "short_Name", "voucher_Type", "v_Type", cnTemp);

            Timestamp timestamp = DmServer.getServerTimestamp();
            /*==============================<< SAVE & UPDATE >>====================================*/
            if (mrTemp.isBlnAdd()) {

                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(vType, DmShared.xNull(mrTemp.getVoucherPrefix()), cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("posPayment", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), DmServer.getServerDateTime()[0], vType, DmShared.xNull(mrTemp.getVoucherPrefix()), cnTemp, this, true));
                DmServer.getRecId("posPayment", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), DmServer.getServerDateTime()[0], vType, DmShared.xNull(mrTemp.getVoucherPrefix()), cnTemp, this, true);

                vNo = DmShared.iNull(mrTemp.getRecId().getCounter()).toString();

                if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Invalid key.");
                }

                sqlQuery = "INSERT INTO posPayment(docId, v_Type,v_No, recId, v_Date, v_Prefix, site_Code,recIdPrefix, v_Time, amount, "
                        + "                         request_TimeStamp,paymentMode,deviceId,userName,appKey,mobile,cardTypeMastCode,preparedBy) "
                        + "VALUES (:docId, :v_Type,:v_No,  :recId, :v_Date, :v_Prefix, :site_Code, :recIdPrefix, :v_Time, :amount, "
                        + "        :request_TimeStamp,:paymentMode,:deviceId,:userName,:appKey,:mobile,:cardTypeMastCode,:preparedBy) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("request_TimeStamp", timestamp);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("v_Type", DmShared.xNull(vType));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recId", mrTemp.getRecId().getRecIdValue());
                psTemp.setString("v_Date", DmServer.getServerDateTime()[0]);
                psTemp.setString("v_Time", DmServer.getServerDateTime()[1]);
                psTemp.setString("v_Prefix", DmShared.xNull(mrTemp.getVoucherPrefix()));
                psTemp.setString("site_code", DmServer.getSsnSiteCode(this));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("amount", DmServer.vNull(mrTemp.getAmount()));

                psTemp.setString("appKey", DmShared.xNull(mrTemp.getAppKey()));
                psTemp.setString("userName", DmShared.xNull(mrTemp.getUserName()));
                psTemp.setString("deviceId", DmShared.xNull(mrTemp.getDeviceId()));
                psTemp.setString("paymentMode", DmShared.xNull(mrTemp.getPaymentMode()));
                psTemp.setString("mobile", DmShared.xNull(mrTemp.getMobile()));
                psTemp.setString("cardTypeMastCode", DmShared.xNull(mrTemp.getCardTypeMastCode()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));

                psTemp.executeUpdate();
                psTemp.close();

                mrTemp.setBlnAdd(false);
                mrTemp.setPosPaymentDocid(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            }

            RazorPayPosPmtGatewayParam gtwParams = new RazorPayPosPmtGatewayParam();

            gtwParams.setAppKey(DmShared.xNull(mrTemp.getAppKey()));
            gtwParams.setUsername(DmShared.xNull(mrTemp.getUserName()));
            gtwParams.setMode(DmShared.xNull(mrTemp.getPaymentMode()));
            gtwParams.setDefaultDeviceId(DmShared.xNull(mrTemp.getDeviceId()));
            gtwParams.setUrlInitiate(DmShared.xNull(mrTemp.getRequestURL()));
            gtwParams.setUrlStatus(DmShared.xNull(mrTemp.getResponseURL()));
            gtwParams.setUrlCancel(DmShared.xNull(mrTemp.getCancelURL()));

            PmtGatewayDetail_Mdl pmtMdl = new PmtGatewayDetail_Mdl();

            pmtMdl.setAmount(DmShared.xNull(mrTemp.getAmount()));
            pmtMdl.setDeviceId(DmShared.xNull(mrTemp.getDeviceId()));
            pmtMdl.setEmail("");
            pmtMdl.setId(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
            pmtMdl.setMobile(DmShared.xNull(mrTemp.getMobile()));
            pmtMdl.setRefName("");

            pmtMdl = RazorPayPOS_Server.initiatePayment(pmtMdl, gtwParams);

            mrTemp.setGtwAckNo(pmtMdl.getGtwAckNo());

            sqlQuery = "UPDATE posPayment \n"
                    + " SET gtwAckNo = :gtwAckNo , initiateResponseJson=:initiateResponseJson\n"
                    + " WHERE docid = :docId \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("gtwAckNo", DmShared.xNull(mrTemp.getGtwAckNo()));
            psTemp.setString("initiateResponseJson", DmShared.xNull(pmtMdl.getResponseString()));
            psTemp.executeUpdate();
            psTemp.close();

            dmrTemp.setData(mrTemp);
            spBegin.commit();
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
    public DmResult<PaymentProcess_Mdl> getCancelPayment(PaymentProcess_Mdl mrTemp) {
        DmResult<PaymentProcess_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            RazorPayPosPmtGatewayParam gtwParams = new RazorPayPosPmtGatewayParam();

            gtwParams.setAppKey(DmShared.xNull(mrTemp.getAppKey()));
            gtwParams.setUsername(DmShared.xNull(mrTemp.getUserName()));
            gtwParams.setMode(DmShared.xNull(mrTemp.getPaymentMode()));
            gtwParams.setDefaultDeviceId(DmShared.xNull(mrTemp.getDeviceId()));
            gtwParams.setUrlInitiate(DmShared.xNull(mrTemp.getRequestURL()));
            gtwParams.setUrlStatus(DmShared.xNull(mrTemp.getResponseURL()));
            gtwParams.setUrlCancel(DmShared.xNull(mrTemp.getCancelURL()));

            PmtGatewayDetail_Mdl pmtMdl = new PmtGatewayDetail_Mdl();

            pmtMdl.setAmount(DmShared.xNull(mrTemp.getAmount()));
            pmtMdl.setDeviceId(DmShared.xNull(mrTemp.getDeviceId()));
            pmtMdl.setEmail("");
            pmtMdl.setId(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
            pmtMdl.setMobile(DmShared.xNull(mrTemp.getMobile()));
            pmtMdl.setRefName("");
            pmtMdl.setGtwAckNo(DmShared.xNull(mrTemp.getGtwAckNo()));

            pmtMdl = RazorPayPOS_Server.cancelPayment(pmtMdl, gtwParams);

            mrTemp.setPaymentSuccess(DmShared.xNull(pmtMdl.getRespMdl().getAbstractPaymentStatus()).equalsIgnoreCase("SUCCESS"));
            mrTemp.setAbstractPaymentStatus(DmShared.xNull(pmtMdl.getRespMdl().getAbstractPaymentStatus()));

            mrTemp.setRespMdl(pmtMdl.getRespMdl());

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
    public DmResult<PaymentProcess_Mdl> getCheckStatus(PaymentProcess_Mdl mrTemp) {
        DmResult<PaymentProcess_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        GetStatusResponse_Mdl statusFinal = new GetStatusResponse_Mdl();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            RazorPayPosPmtGatewayParam gtwParams = new RazorPayPosPmtGatewayParam();

            gtwParams.setAppKey(DmShared.xNull(mrTemp.getAppKey()));
            gtwParams.setUsername(DmShared.xNull(mrTemp.getUserName()));
            gtwParams.setMode(DmShared.xNull(mrTemp.getPaymentMode()));
            gtwParams.setDefaultDeviceId(DmShared.xNull(mrTemp.getDeviceId()));
            gtwParams.setUrlInitiate(DmShared.xNull(mrTemp.getRequestURL()));
            gtwParams.setUrlStatus(DmShared.xNull(mrTemp.getResponseURL()));
            gtwParams.setUrlCancel(DmShared.xNull(mrTemp.getCancelURL()));

            //
            PmtGatewayDetail_Mdl pmtMdl = new PmtGatewayDetail_Mdl();

            pmtMdl.setAmount(DmShared.xNull(mrTemp.getAmount()));
            pmtMdl.setDeviceId(DmShared.xNull(mrTemp.getDeviceId()));
            pmtMdl.setEmail("");
            pmtMdl.setId(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
            pmtMdl.setMobile(DmShared.xNull(mrTemp.getMobile()));
            pmtMdl.setRefName("");
            pmtMdl.setGtwAckNo(DmShared.xNull(mrTemp.getGtwAckNo()));

            pmtMdl = RazorPayPOS_Server.checkPaymentStaus(pmtMdl, gtwParams);

//            mrTemp.setStatus(DmShared.iNull(pmtMdl.getStatus()).intValue());
            mrTemp.setPaymentSuccess(DmShared.xNull(pmtMdl.getRespMdl().getAbstractPaymentStatus()).equalsIgnoreCase("SUCCESS"));
            mrTemp.setAbstractPaymentStatus(DmShared.xNull(pmtMdl.getRespMdl().getAbstractPaymentStatus()));

            mrTemp.setRespMdl(pmtMdl.getRespMdl());

            Timestamp timestamp = DmServer.getServerTimestamp();
            statusFinal = pmtMdl.getRespMdl();
            sqlQuery = "UPDATE posPayment \n"
                    + " SET   checkStatusResponseJson=:checkStatusResponseJson  "
                    + " ,paymentResponceStatus=:paymentResponceStatus "
                    + ",response_TimeStamp=:response_TimeStamp , \n "
                    + "     txnId = :txnId , "
                    + "     rrNumber = :rrNumber , "
                    + "     p2pRequestId = :p2pRequestId , "
                    + "     formattedPan = :formattedPan , "
                    + "     payerName = :payerName , "
                    + "     paymentCardType = :paymentCardType , "
                    + "     tid = :tid , "
                    + "     customerName = :customerName , "
                    + "     paymentCardBrand = :paymentCardBrand  "
                    + " WHERE gtwAckNo = :gtwAckNo \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("gtwAckNo", DmShared.xNull(mrTemp.getGtwAckNo()));
            psTemp.setString("paymentResponceStatus", DmShared.xNull(pmtMdl.getRespMdl().getAbstractPaymentStatus()));
            psTemp.setString("checkStatusResponseJson", DmShared.xNull(pmtMdl.getRespMdl().getResponseString()));
            //
            psTemp.setString("txnId", DmShared.xNull(pmtMdl.getRespMdl().getTxnId()));
            psTemp.setString("rrNumber", DmShared.xNull(pmtMdl.getRespMdl().getRrNumber()));
            psTemp.setString("p2pRequestId", DmShared.xNull(pmtMdl.getRespMdl().getP2pRequestId()));
            psTemp.setString("formattedPan", DmShared.xNull(statusFinal.getFormattedPan()));
            psTemp.setString("payerName", DmShared.xNull(statusFinal.getPayerName()));
            psTemp.setString("paymentCardType", DmShared.xNull(statusFinal.getPaymentCardType()));
            psTemp.setString("paymentCardBrand", DmShared.xNull(statusFinal.getPaymentCardBrand()));
            psTemp.setString("tid", DmShared.xNull(statusFinal.getTid()));
            psTemp.setString("customerName", DmShared.xNull(statusFinal.getCustomerName()));
            //

            psTemp.setTimestamp("response_TimeStamp", timestamp);

            psTemp.executeUpdate();
            psTemp.close();

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
    public DmResult<RazorPayParams_Mdl> getPOSParams(String cardTypeCode) {
        DmResult<RazorPayParams_Mdl> dmrTemp = new DmResult();

        RazorPayParams_Mdl mrTemp = new RazorPayParams_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select integrationParams from cardTypeMast where code=:cardTypeCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("cardTypeCode", DmShared.xNull(cardTypeCode));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp = DmServer.convertFromJson(DmShared.xNull(rsTemp.getString("integrationParams")), new TypeToken<RazorPayParams_Mdl>() {
                });
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPaymentMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("CARD");
            listNode.setSharedKeyFieldName("CARD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("UPI");
            listNode.setSharedKeyFieldName("UPI");
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
    public RazorPayParams_Mdl posSettingsJSON(String posSettingsJSON) {
        RazorPayParams_Mdl mrTemp = new RazorPayParams_Mdl();
        RazorPayParamsList_Mdl lst = new RazorPayParamsList_Mdl();

        try {

            lst = DmServer.convertFromJson(posSettingsJSON, new TypeToken<RazorPayParamsList_Mdl>() {
            });

            for (RazorPayParams_Mdl node : lst.getConfigList()) {

                if (DmShared.xNull(node.getSiteCode()).equalsIgnoreCase(DmServer.getSsnSiteCode(this)) && DmShared.xNull(node.getSiteShortName()).equalsIgnoreCase(DmServer.getSsnSiteSName(this))) {
                    mrTemp = node;
                    break;
                }
            }

//            mrTemp = DmServer.convertFromJson(posSettingsJSON, new TypeToken<RazorPayParams_Mdl>() {
//            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        return mrTemp;
    }

    @Override
    public DmResult<String> printRecords(GrdCardPayment_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String strSQL, strSQL1;
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        ChequePrintSetup_Mdl mrTempChquePrintSetup = new ChequePrintSetup_Mdl();
        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "select " + mrTemp.getAmount() + " as amount, \n"
                    + "   FORMAT(CONVERT(DATE, '" + mrTemp.getCardDate() + "', 106), 'dd-MM-yyyy') AS chqDate, \n"
                    + " " + mrTemp.getCardNo() + " as chqNo, \n"
                    + " '" + mrTemp.getCardHolderName() + "' as favourOf, \n"
                    + " 1 as isCrossCheque \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            rsTemp = psTemp.executeQuery();
            if (DmShared.iNull(mrTemp.getAcNameCode()).longValue() > 0) {
                strSQL1 = "  SELECT  "
                        + "   cps.bankCode "
                        + " , cps.chequeDateTop "
                        + " , cps.chequeDateLeft "
                        + " , cps.crossChequeTop "
                        + " , cps.crossChequeLeft "
                        + " , cps.favourOfTop "
                        + " , cps.favourOfLeft "
                        + " , cps.AmtInWordsTop "
                        + " , cps.AmtInWordsLeft "
                        + " , cps.AmtTop "
                        + " , cps.AmtLeft "
                        + " , cps.authSignatory "
                        + " , cps.authSignatoryTop "
                        + " , cps.authSignatoryLeft "
                        + " , cps.companyName "
                        + " , cps.companyNameTop "
                        + " , cps.companyNameLeft"
                        + " , cps.paperWidth"
                        + " , cps.paperHeight"
                        + " FROM chequePrintingSetup as cps "
                        + " where cps.bankCode= :bankCode ";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL1);
                if (!DmShared.xNull(mrTemp.getAcNameCode()).trim().equalsIgnoreCase("")) {
                    psTemp1.setString("bankCode", DmShared.xNull(mrTemp.getAcNameCode()));
                }
                rsTemp1 = psTemp1.executeQuery();
                if (rsTemp1.next()) {
                    mrTempChquePrintSetup.setBankCode(DmShared.xNull(rsTemp1.getString("bankCode")));
                    mrTempChquePrintSetup.setChequeDateTop(DmServer.vNull(rsTemp1.getString("chequeDateTop"), "0.00"));
                    mrTempChquePrintSetup.setChequeDateLeft(DmServer.vNull(rsTemp1.getString("chequeDateLeft"), "0.00"));
                    mrTempChquePrintSetup.setFavourOfTop(DmServer.vNull(rsTemp1.getString("favourOfTop"), "0.00"));
                    mrTempChquePrintSetup.setFavourOfLeft(DmServer.vNull(rsTemp1.getString("favourOfLeft"), "0.00"));
                    mrTempChquePrintSetup.setAmtInWordsTop(DmServer.vNull(rsTemp1.getString("AmtInWordsTop"), "0.00"));
                    mrTempChquePrintSetup.setAmtInWordsLeft(DmServer.vNull(rsTemp1.getString("AmtInWordsLeft"), "0.00"));
                    mrTempChquePrintSetup.setAmtTop(DmServer.vNull(rsTemp1.getString("AmtTop"), "0.00"));
                    mrTempChquePrintSetup.setAmtLeft(DmServer.vNull(rsTemp1.getString("AmtLeft"), "0.00"));
                    mrTempChquePrintSetup.setAuthSign(DmShared.xNull(rsTemp1.getString("authSignatory")));
                    mrTempChquePrintSetup.setAuthSignTop(DmServer.vNull(rsTemp1.getString("authSignatoryTop"), "0.00"));
                    mrTempChquePrintSetup.setAuthSignLeft(DmServer.vNull(rsTemp1.getString("authSignatoryLeft"), "0.00"));
                    mrTempChquePrintSetup.setCompanyName(DmShared.xNull(rsTemp1.getString("companyName")));
                    mrTempChquePrintSetup.setCompanyNameTop(DmServer.vNull(rsTemp1.getString("companyNameTop"), "0.00"));
                    mrTempChquePrintSetup.setCompanyNameLeft(DmServer.vNull(rsTemp1.getString("companyNameLeft"), "0.00"));
                    mrTempChquePrintSetup.setCrossChequeTop(DmServer.vNull(rsTemp1.getString("crossChequeTop"), "0.00"));
                    mrTempChquePrintSetup.setCrossChequeLeft(DmServer.vNull(rsTemp1.getString("crossChequeLeft"), "0.00"));
                    mrTempChquePrintSetup.setPaperWidth(DmServer.vNull(rsTemp1.getString("paperWidth"), "0.00"));
                    mrTempChquePrintSetup.setPaperHeight(DmServer.vNull(rsTemp1.getString("paperHeight"), "0.00"));
                }
                psTemp1.close();
                rsTemp1.close();

                //<editor-fold defaultstate="collapsed" desc="JASPER">
                LinkedHashMap hm = new LinkedHashMap();
                hm.put("companyName", DmShared.xNull(mrTempChquePrintSetup.getCompanyName()));
                hm.put("authSignatory", DmShared.xNull(mrTempChquePrintSetup.getAuthSign()));

                //<editor-fold defaultstate="collapsed" desc="comment">
                String reportPath = DmBaseServer.Path.reports;
                DmJasperCompiler jasperCompiler = null;

                String subDirectory = "";
                if (DmBaseShared.xNull(subDirectory).equalsIgnoreCase("")) {
                    subDirectory = "system";
                }

                reportPath = reportPath + "/" + subDirectory + "/General _ChequePrint.jrxml";

                JasperDesign jasperDesign = JRXmlLoader.load(reportPath);

                jasperDesign.setPageHeight(DmShared.iNull(mrTempChquePrintSetup.getPaperHeight()).intValue());
                jasperDesign.setPageWidth(DmShared.iNull(mrTempChquePrintSetup.getPaperWidth()).intValue());

                jasperReport = JasperCompileManager.compileReport(jasperDesign);
//</editor-fold>

                JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {
                    @Override
                    public void visitBreak(JRBreak jrb) {
                        try {
                            Field topField = JRBaseElement.class.getDeclaredField("y");
                            topField.setAccessible(true);
                            int oldValue = topField.getInt(jrb);
                            int newTopValue = 0;
                            if (DmShared.xNull(jrb.getKey()).equalsIgnoreCase("breakPage")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getPaperHeight()).doubleValue())).intValue();
                                topField.setInt(jrb, newTopValue);
                                ((JRBaseElement) jrb).getEventSupport().firePropertyChange(topField.getName(), oldValue, newTopValue);
                                jrb.setX(0);
                            }
                        } catch (Exception e) {
                        }
                    }

                    @Override
                    public void visitChart(JRChart jrc) {
                    }

                    @Override
                    public void visitCrosstab(JRCrosstab jrc) {
                    }

                    @Override
                    public void visitElementGroup(JRElementGroup jreg) {
                    }

                    @Override
                    public void visitEllipse(JREllipse jre) {

                    }

                    @Override
                    public void visitFrame(JRFrame jrf) {

                    }

                    @Override
                    public void visitImage(JRImage jri) {

                    }

                    @Override
                    public void visitLine(JRLine jrline) {

                    }

                    @Override
                    public void visitRectangle(JRRectangle jrr) {

                    }

                    @Override
                    public void visitStaticText(JRStaticText jrst) {
                    }

                    @Override
                    public void visitSubreport(JRSubreport jrs) {

                    }

                    @Override
                    public void visitTextField(JRTextField textField) {
                        try {
                            Field topField = JRBaseElement.class.getDeclaredField("y");
                            topField.setAccessible(true);
                            int oldValue = topField.getInt(textField);
                            boolean flagSetValue = false;
                            int newTopValue = 0, newLeftValue = 0;

                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("chqDate")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getChequeDateTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getChequeDateLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("amountInWords")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtInWordsTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtInWordsLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("favourOf")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getFavourOfTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getFavourOfLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("amount")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("authSignatory")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAuthSignTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAuthSignLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("companyName")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCompanyNameTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCompanyNameLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("acPayee")) {
                                newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCrossChequeTop()).doubleValue())).intValue();
                                newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCrossChequeLeft()).doubleValue())).intValue();
                                flagSetValue = true;
                            }
                            if (flagSetValue) {
                                topField.setInt(textField, newTopValue);
                                ((JRBaseElement) textField).getEventSupport().firePropertyChange(topField.getName(), oldValue, newTopValue);
                                textField.setX(newLeftValue);
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void visitComponentElement(JRComponentElement jrce) {
                    }

                    @Override
                    public void visitGenericElement(JRGenericElement jrge) {

                    }
                });
                jasperPrint = DmServer.fillJasper(this, "", jasperReport, hm, null, rsTemp);
                DmReportProperty_Mdl property_Mdl = new DmReportProperty_Mdl();
                property_Mdl.setExportType(DmEnum.DmExportType.PDF);
                dmrTemp.setData(DmServer.showJasper(this, jasperPrint, "", property_Mdl));
//</editor-fold>

                spBegin.commit();
                dmrTemp.setStatus(true);
            }
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
