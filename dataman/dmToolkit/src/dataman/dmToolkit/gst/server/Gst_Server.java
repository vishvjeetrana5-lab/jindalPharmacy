package dataman.dmToolkit.gst.server;

import com.google.gson.Gson;
import TaxProEInvoice.Java.API.E_InvoiceCommon;
import TaxProEInvoice.Java.API.E_InvoiceCommon.ReqPlCancelIRN;
import TaxProEInvoice.Java.API.E_InvoiceCommon.ReqPlGenIRN.ItmList.BatchDetails;
import TaxProEwb.Java.API.EWBJson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.EWAY_GenerationMode;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.eGstPopup.shared.IEGstTransaction;
import dataman.dmToolkit.gst.shared.EGstDetail_Mdl;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import dataman.dmToolkit.gst.shared.EGstToken_Mdl;
import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.EInvCancelData_Mdl;
import dataman.dmToolkit.gst.shared.EInvCancelResponse_Mdl;
import dataman.dmToolkit.gst.shared.EInvData_Mdl;
import dataman.dmToolkit.gst.shared.EInvoice_Mdl;
import dataman.dmToolkit.gst.shared.EWayViaIrnResponse_Mdl;
import dataman.dmToolkit.gst.shared.EinvAdditionalDocDtls_Mdl;
import dataman.dmToolkit.gst.shared.EinvCancelInfoDtls_Mdl;
import dataman.dmToolkit.gst.shared.EinvRefContractDtls_Mdl;
import dataman.dmToolkit.gst.shared.EinvoiceMongo_Mdl;
import dataman.dmToolkit.gst.shared.ErrorDetails_Mdl;
import dataman.dmToolkit.gst.shared.EwayCancelRequest_Mdl;
import dataman.dmToolkit.gst.shared.EwayCancelResponse_Mdl;
import dataman.dmToolkit.gst.shared.GstDocument_Mdl;
import dataman.dmToolkit.gst.shared.GstTrans_Mdl;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.gst.shared.Eway_Mdl;
import dataman.dmToolkit.gst.shared.Party_Mdl;
import dataman.dmToolkit.modifyReason.server.ModifyReason_Server;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.gst.shared.Gst_Shared;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Gst_Server {

    //<editor-fold defaultstate="collapsed" desc="E-GST Variables">
    private final static EGstApiCall apiCall = new EGstApiCall();
    private static EGstToken_Mdl eGstToken_Mdl = new EGstToken_Mdl();
    private static EGstParam_Mdl eGSTParam_Mdl = new EGstParam_Mdl();
    private final static String EWAY_DATETIME_FORMAT_ACTUAL = "dd/MM/yyyy hh:mm:ss aa";
    private final static String EINV_DATETIME_FORMAT_ACTUAL = "yyyy-MM-dd HH:mm:ss";  //2021-10-30 22:08:05
    public final static boolean isSandBoxTesting = false;
    //</editor-fold>

    public static void saveGstTrans(GstTrans_Mdl gstTrans_Mdl, Connection cnTemp, DmRSSPrivate rssTemp) {
        try {
            saveGstTrans(gstTrans_Mdl, true, cnTemp, rssTemp);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void saveGstTrans(GstTrans_Mdl gstTrans_Mdl, boolean isApprovalRequired, Connection cnTemp, DmRSSPrivate rssTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            sqlQuery = "INSERT INTO gstTrans"
                    + "           (docId"
                    + "           ,v_Sno"
                    + "           ,site_Code"
                    + "           ,v_Type"
                    + "           ,recID"
                    + "           ,recIdPrefix"
                    + "           ,recIdSerial"
                    + "           ,v_Date"
                    + "           ,partyBillNo"
                    + "           ,partyBillDt"
                    + "           ,shippingBillNo"
                    + "           ,shippingBillDt"
                    + "           ,shippingPortCode"
                    + "           ,partyCode"
                    + "           ,consignee"
                    + "           ,partyType"
                    + "           ,gsTin"
                    + "           ,gstinDate"
                    + "           ,placeOfSupply"
                    + "           ,refDocId"
                    + "           ,refRecId"
                    + "           ,refV_Date"
                    + "           ,refV_Type"
                    + "           ,refDocValue"
                    + "           ,refPartyBillNo"
                    + "           ,refPartyBillDt"
                    + "           ,gstReasonCode"
                    + "           ,rcmApplicable"
                    + "           ,itemCode"
                    + "           ,hsnCode"
                    + "           ,itemGSTNature"
                    + "           ,departmentCode"
                    + "           ,qty"
                    + "           ,gstUnitCode"
                    + "           ,itemGrossAmount"
                    + "           ,itemTaxableAmount"
                    + "           ,itemCgstRate"
                    + "           ,itemCgstAmount"
                    + "           ,itemSgstRate"
                    + "           ,itemSgstAmount"
                    + "           ,itemIgstRate"
                    + "           ,itemIgstAmount"
                    + "           ,itemUgstRate"
                    + "           ,itemUgstAmount"
                    + "           ,itemCessRate"
                    + "           ,itemCessAmount"
                    + "           ,itemNetAmount"
                    + "           ,invoiceSGST"
                    + "           ,invoiceCGST"
                    + "           ,invoiceIGST"
                    + "           ,invoiceUGST"
                    + "           ,invoiceValue"
                    + "           ,gstType"
                    + "           ,gstDocType\n"
                    + "           ,gstDocNature\n"
                    + "           ,gstTransNature"
                    + "           ,isStatus\n"
                    + "           ,isTaxInclusive)"
                    + " VALUES"
                    + "           (:docId"
                    + "           ,:v_Sno"
                    + "           ,:site_Code"
                    + "           ,:v_Type"
                    + "           ,:recID"
                    + "           ,:recIdPrefix"
                    + "           ,:recIdSerial"
                    + "           ,:v_Date"
                    + "           ,:partyBillNo"
                    + "           ,:partyBillDt"
                    + "           ,:shippingBillNo"
                    + "           ,:shippingBillDt"
                    + "           ,:shippingPortCode"
                    + "           ,:partyCode"
                    + "           ,:consignee"
                    + "           ,:partyType"
                    + "           ,:gsTin"
                    + "           ,:gstinDate"
                    + "           ,:placeOfSupply"
                    + "           ,:refDocId"
                    + "           ,:refRecId"
                    + "           ,:refV_Date"
                    + "           ,:refV_Type"
                    + "           ,:refDocValue"
                    + "           ,:refPartyBillNo"
                    + "           ,:refPartyBillDt"
                    + "           ,:gstReasonCode"
                    + "           ,:rcmApplicable"
                    + "           ,:itemCode"
                    + "           ,:hsnCode"
                    + "           ,:itemGSTNature"
                    + "           ,:departmentCode"
                    + "           ,:qty"
                    + "           ,:gstUnitCode"
                    + "           ,:itemGrossAmount"
                    + "           ,:itemTaxableAmount"
                    + "           ,:itemCgstRate"
                    + "           ,:itemCgstAmount"
                    + "           ,:itemSgstRate"
                    + "           ,:itemSgstAmount"
                    + "           ,:itemIgstRate"
                    + "           ,:itemIgstAmount"
                    + "           ,:itemUgstRate"
                    + "           ,:itemUgstAmount"
                    + "           ,:itemCessRate"
                    + "           ,:itemCessAmount"
                    + "           ,:itemNetAmount"
                    + "           ,:invoiceSGST"
                    + "           ,:invoiceCGST"
                    + "           ,:invoiceIGST"
                    + "           ,:invoiceUGST"
                    + "           ,:invoiceValue"
                    + "           ,:gstType"
                    + "           ,:gstDocType \n"
                    + "           ,:gstDocNature \n"
                    + "           ,:gstTransNature \n"
                    + "           ,:isStatus \n"
                    + "           ,:isTaxInclusive"
                    + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(gstTrans_Mdl.getDocId()));
            psTemp.setString("v_Sno", DmShared.xNull(gstTrans_Mdl.getV_Sno()));
            psTemp.setString("site_Code", DmShared.foriegnKey(gstTrans_Mdl.getSite_Code()));
            psTemp.setString("v_Type", DmShared.foriegnKey(gstTrans_Mdl.getV_Type()));
            psTemp.setString("recID", DmShared.xNull(gstTrans_Mdl.getRecID()));
            psTemp.setString("recIdPrefix", DmShared.xNull(gstTrans_Mdl.getRecIdPrefix()));
            psTemp.setString("recIdSerial", DmShared.xNull(gstTrans_Mdl.getRecIdSerial()));
            psTemp.setDate("v_Date", DmServer.convertDate(gstTrans_Mdl.getV_Date()));
            psTemp.setString("partyBillNo", DmShared.xNull(gstTrans_Mdl.getPartyBillNo()));
            psTemp.setDate("partyBillDt", DmServer.convertDate(gstTrans_Mdl.getPartyBillDt()));
            psTemp.setString("shippingBillNo", DmShared.foriegnKey(gstTrans_Mdl.getShippingBillNo()));
            psTemp.setDate("shippingBillDt", DmServer.convertDate(gstTrans_Mdl.getShippingBillDt()));
            psTemp.setString("shippingPortCode", DmShared.xNull(gstTrans_Mdl.getShippingPortCode()));
            psTemp.setString("partyCode", DmShared.foriegnKey(gstTrans_Mdl.getPartyCode()));
            psTemp.setString("consignee", DmShared.foriegnKey(gstTrans_Mdl.getConsignee()));
            psTemp.setString("partyType", DmShared.xNull(gstTrans_Mdl.getPartyType()));
            psTemp.setString("gsTin", DmShared.xNull(gstTrans_Mdl.getGstin()));
            psTemp.setDate("gstinDate", DmServer.convertDate(gstTrans_Mdl.getGstinDate()));
            psTemp.setString("placeOfSupply", DmShared.foriegnKey(gstTrans_Mdl.getPlaceOfSupply()));
            psTemp.setString("refDocId", DmShared.foriegnKey(gstTrans_Mdl.getRefDocId()));
            psTemp.setString("refPartyBillNo", DmShared.xNull(gstTrans_Mdl.getRefPartyBillNo()));
            psTemp.setDate("refPartyBillDt", DmServer.convertDate(gstTrans_Mdl.getRefPartyBillDt()));
            psTemp.setString("refRecId", DmShared.xNull(gstTrans_Mdl.getRefRecId()));
            psTemp.setDate("refV_Date", DmServer.convertDate(gstTrans_Mdl.getRefV_Date()));
            psTemp.setString("refV_Type", DmShared.foriegnKey(gstTrans_Mdl.getRefV_Type()));
            psTemp.setString("refDocValue", DmServer.vNull(gstTrans_Mdl.getRefDocValue()));
            psTemp.setString("gstReasonCode", DmShared.foriegnKey(gstTrans_Mdl.getGstReasonCode()));
            psTemp.setString("rcmApplicable", DmShared.xNull(gstTrans_Mdl.getRcmApplicable()));
            psTemp.setString("itemCode", DmShared.foriegnKey(gstTrans_Mdl.getItemCode()));
            psTemp.setString("hsnCode", DmShared.foriegnKey(gstTrans_Mdl.getHsnCode()));
            psTemp.setString("itemGSTNature", DmShared.xNull(gstTrans_Mdl.getItemGSTNature()));
            psTemp.setString("departmentCode", DmShared.foriegnKey(gstTrans_Mdl.getDepartmentCode()));
            psTemp.setString("qty", DmServer.vNull(gstTrans_Mdl.getQty()));
            psTemp.setString("gstUnitCode", DmShared.foriegnKey(gstTrans_Mdl.getGstUnitCode()));
            psTemp.setString("itemGrossAmount", DmServer.vNull(gstTrans_Mdl.getItemGrossAmount()));
            psTemp.setString("itemTaxableAmount", DmServer.vNull(gstTrans_Mdl.getItemTaxableAmount()));
            psTemp.setString("itemCgstRate", DmServer.vNull(gstTrans_Mdl.getItemCgstRate()));
            psTemp.setString("itemCgstAmount", DmServer.vNull(gstTrans_Mdl.getItemCgstAmount()));
            psTemp.setString("itemSgstRate", DmServer.vNull(gstTrans_Mdl.getItemSgstRate()));
            psTemp.setString("itemSgstAmount", DmServer.vNull(gstTrans_Mdl.getItemSgstAmout()));
            psTemp.setString("itemIgstRate", DmServer.vNull(gstTrans_Mdl.getItemIgstRate()));
            psTemp.setString("itemIgstAmount", DmServer.vNull(gstTrans_Mdl.getItemIgstAmount()));
            psTemp.setString("itemUgstRate", DmServer.vNull(gstTrans_Mdl.getItemUgstRate()));
            psTemp.setString("itemUgstAmount", DmServer.vNull(gstTrans_Mdl.getItemUgstAmount()));
            psTemp.setString("itemCessRate", DmServer.vNull(gstTrans_Mdl.getItemCessRate()));
            psTemp.setString("itemCessAmount", DmServer.vNull(gstTrans_Mdl.getItemCessAmount()));
            psTemp.setString("itemNetAmount", DmServer.vNull(gstTrans_Mdl.getItemNetAmount()));
            psTemp.setString("invoiceSGST", DmServer.vNull(gstTrans_Mdl.getInvoiceSGST()));
            psTemp.setString("invoiceCGST", DmServer.vNull(gstTrans_Mdl.getInvoiceCGST()));
            psTemp.setString("invoiceIGST", DmServer.vNull(gstTrans_Mdl.getInvoiceIGST()));
            psTemp.setString("invoiceUGST", DmServer.vNull(gstTrans_Mdl.getInvoiceUGST()));
            psTemp.setString("invoiceValue", DmServer.vNull(gstTrans_Mdl.getInvoiceValue()));
            psTemp.setString("gstType", DmShared.iNull(gstTrans_Mdl.getGstType()).toString());
            psTemp.setString("isTaxInclusive", DmShared.iNull(gstTrans_Mdl.getIsTaxInclusive()).toString());
            psTemp.setString("gstDocType", DmShared.foriegnKey(gstTrans_Mdl.getGstDocTypeCode()));
            psTemp.setString("gstDocNature", DmShared.foriegnKey(gstTrans_Mdl.getGstDocNatureCode()));
            psTemp.setString("gstTransNature", DmShared.foriegnKey(gstTrans_Mdl.getGstTransNature()).toString());
            psTemp.setInt("isStatus", isApprovalRequired ? 1 : 0);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }
    
    public static void manageGstTransRecord(String docid, boolean isApprove, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {
            if (DmShared.iNull(docid).longValue() > 0) {
                /*==============================<< SAVE IN TRANSPORTER TRANS >>====================================*/

                sqlQuery = " UPDATE \n"
                        + " gstTrans \n"
                        + " SET isStatus = :isStatus \n"
                        + " WHERE docid = :docid \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", docid);
                psTemp.setInt("isStatus", isApprove ? 0 : 1);
                psTemp.executeUpdate();
                psTemp.close();

            }

            /*==============================<< ************************ >>====================================*/
        } catch (Exception e) {
            throw e;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="egstTokensAction">
    public static EGstToken_Mdl getToken(Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
        EGstToken_Mdl token_Mdl = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            //<editor-fold defaultstate="collapsed" desc="getTokenDetails">
            sqlQuery = " select	egt.tokenNo,egt.tokenDt, \n"
                    + " egt.clientId,egt.userName,tokenExpiryDt,egt.sek \n"
                    + " from eGstToken egt \n "
                    + " where egt.userGstin = :userGstin ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userGstin", DmServer.getSsnSiteGSTIN(dmRss));
            if (isSandBoxTesting) {
                //For Testing with SandBox
                psTemp.setString("userGstin", "34AACCC1596Q002");
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                boolean isTokenExpired = false;
                if (rsTemp.getTimestamp("tokenExpiryDt") != null) {
                    if (DmServer.getServerTimestamp().getTime() >= rsTemp.getTimestamp("tokenExpiryDt").getTime()) {  //<Ag> TBC
                        isTokenExpired = true;
                    }
                }
                if (isTokenExpired == false) {
                    token_Mdl = new EGstToken_Mdl();
                    token_Mdl.setClientId(DmShared.xNull(rsTemp.getString("clientId")));
                    token_Mdl.setSek(DmShared.xNull(rsTemp.getString("sek")));
                    token_Mdl.setAuthtoken(DmShared.xNull(rsTemp.getString("tokenNo")));
                    token_Mdl.setTokenDt(DmShared.xNull(rsTemp.getString("tokenDt")));
                    token_Mdl.setUserName(DmShared.xNull(rsTemp.getString("userName")));
                    if (rsTemp.getTimestamp("tokenExpiryDt") != null) {
                        token_Mdl.setTokenExpiry(String.valueOf(rsTemp.getTimestamp("tokenExpiryDt").getTime()));
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return token_Mdl;
    }

    private static void saveToken(EGstToken_Mdl token_Mdl, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {
            Timestamp expiryTimestamp = null;
            if (!DmShared.xNull(token_Mdl.getTokenExpiry()).trim().equalsIgnoreCase("")) {
                expiryTimestamp = Timestamp.valueOf(token_Mdl.getTokenExpiry());
            }
            deleteToken(DmShared.xNull(token_Mdl.getUserGstin()), cnTemp);
            sqlQuery = " INSERT INTO eGstToken ("
                    + "   userGstin, clientId, tokenNo, tokenDt , userName, tokenExpiryDt,sek)  \n"
                    + "   VALUES (:userGstin, :clientId, :tokenNo, :tokenDt , :userName, :tokenExpiryDt ,:sek ) \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userGstin", DmShared.xNull(token_Mdl.getUserGstin()));
            psTemp.setString("tokenNo", DmShared.xNull(token_Mdl.getAuthtoken()));
            psTemp.setString("clientId", DmShared.xNull(token_Mdl.getClientId()));
            psTemp.setTimestamp("tokenDt", convertTimestamp(token_Mdl.getTokenDt()));  //<Ag> Why we are saving server time stamp value in takenDt
            psTemp.setString("sek", DmShared.xNull(token_Mdl.getSek()));
            psTemp.setString("userName", DmShared.xNull(token_Mdl.getUserName()));
            psTemp.setTimestamp("tokenExpiryDt", expiryTimestamp);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void deleteToken(String userGstin, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {
            sqlQuery = " delete eGstToken where userGstin = :userGstin \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userGstin", DmShared.xNull(userGstin));
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Timestamp convertTimestamp(String value) throws Exception {
        Timestamp timestamp = null;
        try {
            if (!DmShared.xNull(value).trim().equalsIgnoreCase("")) {
                timestamp = convertTimestamp(EINV_DATETIME_FORMAT_ACTUAL, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return timestamp;
    }

    private static Timestamp convertTimestamp(String pattern, String value) throws Exception {
        Date dateTime = null;
        try {
            String dateTimeStr = value;
            if (!DmShared.xNull(pattern).trim().equalsIgnoreCase("")) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                dateTime = dateFormat.parse(dateTimeStr);
            } else {
                throw new Exception("Pattern should not be blank.");
            }
        } catch (Exception e) {
            try {
                if (DmShared.xNull(pattern).trim().equalsIgnoreCase(EINV_DATETIME_FORMAT_ACTUAL)) {
                    return convertTimestamp(EWAY_DATETIME_FORMAT_ACTUAL, value);
                } else if (DmShared.xNull(pattern).trim().equalsIgnoreCase(EWAY_DATETIME_FORMAT_ACTUAL)) {
                    return convertTimestamp("dd/MM/yyyy hh:mm:ss", value);
                } else {
                    throw e;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
        if (dateTime == null) {
            throw new Exception("Error in converting value in time stamp.");
        }
        return new Timestamp(dateTime.getTime());
    }

    public static void deleteGstTrans(String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {
            sqlQuery = "DELETE FROM gstTrans WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static EGstToken_Mdl getAuthToken(int actionCode, Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
        EGstToken_Mdl eGstToken_Mdl = new EGstToken_Mdl();
        try {
            loadEgstParamDetails(cnTemp, dmRss);
            eGstToken_Mdl = apiCall.getAuthToken(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            if (eGstToken_Mdl != null
                    && !DmShared.xNull(eGstToken_Mdl.getAuthtoken()).trim().equalsIgnoreCase("")) {
                eGstToken_Mdl.setUserName(eGSTParam_Mdl.getEway_UserName());
                eGstToken_Mdl.setUserGstin(eGSTParam_Mdl.getUserGstin());
                saveToken(eGstToken_Mdl, cnTemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return eGstToken_Mdl;
    }

    private static void loadEgstParamDetails(Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
        try {
            //<editor-fold defaultstate="collapsed" desc="getEgstParamDetails">
            eGSTParam_Mdl = new EGstParam_Mdl();
            eGSTParam_Mdl = (EGstParam_Mdl) DmBaseServer.getSsnData(dmRss, "eGstParam");
            eGstToken_Mdl = new EGstToken_Mdl();
            eGstToken_Mdl = getToken(cnTemp, dmRss);
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void processEGst(MasterEGst_Mdl currentMasterEGst_Mdl, int actionCode, Connection cnTemp, DmRSSPrivate dmRss, IEGstTransaction gstTransaction) throws Exception {
        MasterEGst_Mdl prevMasterEGst_Mdl = new MasterEGst_Mdl();
        EGst_Mdl previousEGst_Mdl = new EGst_Mdl();
        try {
            loadEgstParamDetails(cnTemp, dmRss);
            EGst_Mdl currentEgst_Mdl = currentMasterEGst_Mdl.geteGst_Mdl();
            GstDocument_Mdl gstDocument_Mdl = currentMasterEGst_Mdl.getGst_Mdl();
            //<editor-fold defaultstate="collapsed" desc="Get Previous Master Egst">
            String docid = "";
            if (actionCode == DmEnum.EGST_ACTION.GST_UPDATE_DOCUMENTS.getCode()) {
                docid = DmShared.xNull(gstDocument_Mdl.getDocid());
            } else {
                docid = DmShared.xNull(currentEgst_Mdl.getDocid());
            }
            prevMasterEGst_Mdl = getEGstRecord(DmShared.xNull(docid), cnTemp, dmRss);
            if (prevMasterEGst_Mdl != null) {
                previousEGst_Mdl = prevMasterEGst_Mdl.geteGst_Mdl();
            }
            //</editor-fold>
            if (actionCode == DmEnum.EGST_ACTION.E_WAY_GENRATE_BILL.getCode()) {
                if (previousEGst_Mdl != null) {
                    if (!DmShared.xNull(previousEGst_Mdl.getEway_BillNo()).trim().equalsIgnoreCase("")
                            && DmShared.xNull(previousEGst_Mdl.getEway_Status()).trim().equalsIgnoreCase(DmEnum.Egst_Status.Active.getCode())) {
                        throw new DmException(0, "E-Way bill already active for this document!");
                    }
                }
                Eway_Mdl eway_Mdl = apiCall.genrateEWayBill(eGSTParam_Mdl, eGstToken_Mdl, getEWayJson(currentEgst_Mdl), actionCode);
                if (eway_Mdl != null) {
                    currentEgst_Mdl.setEway_BillNo(DmShared.xNull(eway_Mdl.getEwayBillNo()));
                    currentEgst_Mdl.setEway_BillDate(DmShared.xNull(eway_Mdl.getEwayBillDate()));
                    currentEgst_Mdl.setEway_validUpto(DmShared.xNull(eway_Mdl.getValidUpto()));
                    currentEgst_Mdl.setEway_alert(DmShared.xNull(eway_Mdl.getAlert()));
                    currentEgst_Mdl.setEway_Status(DmEnum.Egst_Status.Active.getCode());
                    currentEgst_Mdl.setEway_GenerationMode(String.valueOf(EWAY_GenerationMode.E_WAY_GENERATED_DIRECTLY.getCode()));
                    //<editor-fold defaultstate="collapsed" desc="Clear E-Way Cancel Detail">
                    currentEgst_Mdl.setEway_cancelRsnCode("");
                    currentEgst_Mdl.setEway_cancelRmrk("");
                    currentEgst_Mdl.setEway_cancelDate("");
                    //</editor-fold>
                } else {
                    throw new DmException(0, "E-Way Bill Response Error.");
                }
            } else if (actionCode == DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode()) {

                if (previousEGst_Mdl != null) {
                    if (!DmShared.xNull(previousEGst_Mdl.getEinv_IrnNo()).trim().equalsIgnoreCase("")
                            && DmShared.xNull(previousEGst_Mdl.getEinv_Status()).trim().equalsIgnoreCase(DmEnum.Egst_Status.Active.getCode())) {
                        throw new DmException(0, "E-invoice already active for this document!");
                    }
                }
                EInvoice_Mdl einvoice_Mdl = apiCall.genrateEInvoice(eGSTParam_Mdl, eGstToken_Mdl, getEInvoiceJson(currentEgst_Mdl), actionCode);
                if (einvoice_Mdl != null) {
                    if (!DmShared.xNull(einvoice_Mdl.getData()).trim().equalsIgnoreCase("")) {
                        EInvData_Mdl eInvData = new ObjectMapper().readValue(einvoice_Mdl.getData(), EInvData_Mdl.class);
                        if (eInvData != null) {
                            currentEgst_Mdl.setEinv_ackNo(DmShared.xNull(eInvData.getAckNo()));
                            currentEgst_Mdl.setEinv_ackDt(DmShared.xNull(eInvData.getAckDt()));
                            currentEgst_Mdl.setEinv_IrnNo(DmShared.xNull(eInvData.getIrn()));
                            currentEgst_Mdl.setEinv_JwtIssuer(DmShared.xNull(eInvData.getJwtIssuer()));
                            currentEgst_Mdl.setEinv_SignedInvoice(DmShared.xNull(eInvData.getSignedInvoice()));
                            currentEgst_Mdl.setEinv_SignedQrCode(DmShared.xNull(eInvData.getSignedQRCode()));
                            currentEgst_Mdl.setEinv_SignedQrCodeImage(DmShared.xNull(eInvData.getQrCodeImage()));
                            currentEgst_Mdl.setEinv_ExtractedSignedInvoiceDataJson(DmShared.xNull(new Gson().toJson(eInvData.getExtractedSignedInvoiceData())));
                            currentEgst_Mdl.setEinv_ExtractedSignedQrCodeJson(DmShared.xNull(new Gson().toJson(eInvData.getExtractedSignedQrCode())));
                            if (!DmShared.xNull(eInvData.getEwbNo()).trim().equalsIgnoreCase("")) {
                                currentEgst_Mdl.setEway_BillNo(DmShared.xNull(eInvData.getEwbNo()));
                                currentEgst_Mdl.setEway_BillDate(DmShared.xNull(eInvData.getEwbDt()));
                                currentEgst_Mdl.setEway_validUpto(DmShared.xNull(eInvData.getEwbValidTill()));
                                currentEgst_Mdl.setEway_Status(DmEnum.Egst_Status.Active.getCode());
                                currentEgst_Mdl.setEway_GenerationMode(String.valueOf(EWAY_GenerationMode.E_WAY_GENERATED_WITH_E_INVOICE.getCode()));
                            }
                            currentEgst_Mdl.setEinv_Status(DmEnum.Egst_Status.Active.getCode());
                        } else {
                            throw new DmException(0, "E-invoice Response Error.");
                        }
                    } else {
                        throw new DmException(0, "E-invoice Response Error.");
                    }
                } else {
                    throw new DmException(0, "E-invoice Response Error.");
                }
            } else if (actionCode == DmEnum.EGST_ACTION.E_WAY_GENERATE_VIA_IRN.getCode()) {
                if (previousEGst_Mdl != null) {
                    if (DmShared.xNull(previousEGst_Mdl.getEway_BillNo()).trim().equalsIgnoreCase("")
                            && DmShared.xNull(previousEGst_Mdl.getEway_Status()).trim().equalsIgnoreCase(DmEnum.Egst_Status.Active.getCode())) {
                        throw new DmException(0, "E-Way bill already active for this document!");
                    }
                }
                EWayViaIrnResponse_Mdl eWayViaIrnResponse_Mdl = new EWayViaIrnResponse_Mdl();
                eWayViaIrnResponse_Mdl = apiCall.genrateEWayBillViaIRN(eGSTParam_Mdl, eGstToken_Mdl, getEWayViaIrnJson(currentEgst_Mdl), actionCode, DmShared.xNull(currentEgst_Mdl.getEinv_IrnNo()));
                if (eWayViaIrnResponse_Mdl != null) {
                    if (!DmShared.xNull(eWayViaIrnResponse_Mdl.getData()).trim().equalsIgnoreCase("")) {
                        Eway_Mdl ewayData = new ObjectMapper().readValue(eWayViaIrnResponse_Mdl.getData(), Eway_Mdl.class);
                        if (ewayData != null && !DmShared.xNull(ewayData.getEwayBillNo()).trim().equalsIgnoreCase("")) {
                            currentEgst_Mdl.setEway_BillNo(DmShared.xNull(ewayData.getEwayBillNo()));
                            currentEgst_Mdl.setEway_BillDate(DmShared.xNull(ewayData.getEwayBillDate()));
                            currentEgst_Mdl.setEway_validUpto(DmShared.xNull(ewayData.getValidUpto()));
                            currentEgst_Mdl.setEway_alert(DmShared.xNull(ewayData.getAlert()));
                            currentEgst_Mdl.setEway_Status(DmEnum.Egst_Status.Active.getCode());
                            currentEgst_Mdl.setEway_GenerationMode(String.valueOf(EWAY_GenerationMode.E_WAY_GENERATED_VIA_IRN.getCode()));
                            //<editor-fold defaultstate="collapsed" desc="Clear E-Way Cancel Detail">
                            currentEgst_Mdl.setEway_cancelRsnCode("");
                            currentEgst_Mdl.setEway_cancelRmrk("");
                            currentEgst_Mdl.setEway_cancelDate("");
                            //</editor-fold> 
                        } else {
                            List<ErrorDetails_Mdl> errorDetails_Mdl = eWayViaIrnResponse_Mdl.getErrorDetails();
                            List<EinvCancelInfoDtls_Mdl> infoDetails_Mdls = eWayViaIrnResponse_Mdl.getInfoDtls();
                            String errorInfoMsg = "";
                            int count = 1;
                            if (errorDetails_Mdl != null) {
                                for (ErrorDetails_Mdl errorDtls : errorDetails_Mdl) {
                                    errorInfoMsg += count++ + ". " + errorDtls.getMessage() + "<br>";
                                }
                            }
                            if (infoDetails_Mdls != null) {
                                for (EinvCancelInfoDtls_Mdl infoDtls : infoDetails_Mdls) {
                                    errorInfoMsg += count++ + ". " + infoDtls.getDesc() + "<br>";
                                }
                            }
                            if (!DmShared.xNull(errorInfoMsg).trim().equalsIgnoreCase("")) {
                                throw new DmException(0, errorInfoMsg);
                            } else {
                                throw new DmException(0, "E-Way Bill via IRN Response Error.");
                            }
                        }
                    } else {
                        List<ErrorDetails_Mdl> errorDetails_Mdl = eWayViaIrnResponse_Mdl.getErrorDetails();
                        List<EinvCancelInfoDtls_Mdl> infoDetails_Mdls = eWayViaIrnResponse_Mdl.getInfoDtls();
                        String errorInfoMsg = "", errorMsg = "", infoMsg = "";
                        if (errorDetails_Mdl != null) {
                            for (ErrorDetails_Mdl errorDtls : errorDetails_Mdl) {
                                if (!DmShared.xNull(errorMsg).trim().equalsIgnoreCase("")) {
                                    errorMsg += "<b>Error<b><br>";
                                }
                                errorMsg += errorDtls.getMessage() + "<br>";
                            }
                        }
                        if (infoDetails_Mdls != null) {
                            for (EinvCancelInfoDtls_Mdl infoDtls : infoDetails_Mdls) {
                                if (!DmShared.xNull(errorMsg).trim().equalsIgnoreCase("")) {
                                    infoMsg += "<b>Info<b><br>";
                                }
                                infoMsg += infoDtls.getDesc() + "<br>";
                            }
                        }
                        if (!DmShared.xNull(errorMsg).trim().equalsIgnoreCase("")) {
                            errorInfoMsg += errorMsg;
                        }
                        if (!DmShared.xNull(infoMsg).trim().equalsIgnoreCase("")) {
                            errorInfoMsg += "<br>" + infoMsg;
                        }
                        if (!DmShared.xNull(errorInfoMsg).trim().equalsIgnoreCase("")) {
                            throw new DmException(0, errorInfoMsg);
                        } else {
                            throw new DmException(0, "E-Way Bill via IRN Response Error.");
                        }
                    }
                } else {
                    throw new DmException(0, "E-Way Bill via IRN Response Error.");
                }
            } else if (actionCode == DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode()) {
                if (previousEGst_Mdl != null) {
                    if (DmShared.xNull(previousEGst_Mdl.getEway_Status()).trim().equalsIgnoreCase(DmEnum.Egst_Status.Cancelled.getCode())) {
                        throw new DmException(0, "E-Way bill already cancel for this document!");
                    }
                    EwayCancelRequest_Mdl cancelRequest_Mdl = new EwayCancelRequest_Mdl();
                    cancelRequest_Mdl.setEwbNo(currentEgst_Mdl.getEway_BillNo());
                    cancelRequest_Mdl.setCancelRsnCode(currentEgst_Mdl.getEway_cancelRsnCode());
                    cancelRequest_Mdl.setCancelRmrk(currentEgst_Mdl.getEway_cancelRmrk());
                    EwayCancelResponse_Mdl cancelResponse_Mdl = apiCall.cancelEWayBill(eGSTParam_Mdl, eGstToken_Mdl, actionCode, new Gson().toJson(cancelRequest_Mdl));
                    if (cancelResponse_Mdl != null) {
                        if (DmShared.xNull(currentEgst_Mdl.getEway_BillNo()).trim().equalsIgnoreCase(cancelResponse_Mdl.getEwayBillNo())) {
                            currentEgst_Mdl.setEway_cancelDate(DmShared.xNull(cancelResponse_Mdl.getCancelDate()));
                            currentEgst_Mdl.setEway_BillNo("");
                            currentEgst_Mdl.setEway_BillDate("");
                            currentEgst_Mdl.setEway_validUpto("");
                            currentEgst_Mdl.setEway_alert("");
                            currentEgst_Mdl.setEway_Status(DmEnum.Egst_Status.Cancelled.getCode());
                        } else {
                            throw new DmException(0, "E-way Cancel Response Error.");
                        }
                    } else {
                        throw new DmException(0, "E-way Cancel Response Error.");
                    }

                } else {
                    throw new DmException(0, "E-way bill not exist for this document!");
                }
            } else if (actionCode == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
                if (previousEGst_Mdl != null) {
                    if (DmShared.xNull(previousEGst_Mdl.getEinv_Status()).trim().equalsIgnoreCase(DmEnum.Egst_Status.Cancelled.getCode())) {
                        throw new DmException(0, "E-invoice bill already cancel for this document!");
                    }
                    ReqPlCancelIRN reqPlCancelIRN = new ReqPlCancelIRN();
                    reqPlCancelIRN.Irn = DmShared.xNull(currentEgst_Mdl.getEinv_IrnNo());
                    reqPlCancelIRN.CnlRem = DmShared.xNull(currentEgst_Mdl.getEinv_cancelRmrk());
                    reqPlCancelIRN.CnlRsn = DmShared.xNull(currentEgst_Mdl.getEinv_cancelRsnCode());
                    EInvCancelResponse_Mdl cancelResponse_Mdl = apiCall.cancelEinvIRN(eGSTParam_Mdl, eGstToken_Mdl, actionCode, new Gson().toJson(reqPlCancelIRN));
                    if (cancelResponse_Mdl != null) {
                        if (!DmShared.xNull(cancelResponse_Mdl.getData()).trim().equalsIgnoreCase("")) {
                            EInvCancelData_Mdl eInvCancelData = new ObjectMapper().readValue(cancelResponse_Mdl.getData(), EInvCancelData_Mdl.class);
                            if (eInvCancelData != null) {
                                if (DmShared.xNull(currentEgst_Mdl.getEinv_IrnNo()).trim().equalsIgnoreCase(DmShared.xNull(eInvCancelData.getIrn()).trim())) {
                                    currentEgst_Mdl.setEinv_cancelDate(DmShared.xNull(eInvCancelData.getCancelDate()));
                                    currentEgst_Mdl.setEinv_Status(DmEnum.Egst_Status.Cancelled.getCode());
                                } else {
                                    throw new DmException(0, "E-invoice Cancel Response Error.");
                                }
                            } else {
                                throw new DmException(0, "E-invoice Cancel Response Error.");
                            }
                        } else {
                            throw new DmException(0, "E-invoice Cancel Response Error.");
                        }
                    } else {
                        throw new DmException(0, "E-invoice Cancel Response Error.");
                    }
                } else {
                    throw new DmException(0, "E-invoice not exist for this document!");
                }
            }
            currentMasterEGst_Mdl.seteGst_Mdl(currentEgst_Mdl);
            currentMasterEGst_Mdl.setGst_Mdl(gstDocument_Mdl);
            if (gstTransaction != null) {
                gstTransaction.processPostApi(prevMasterEGst_Mdl, currentMasterEGst_Mdl, actionCode, cnTemp, dmRss);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getEWayViaIrnJson(EGst_Mdl eGstMdl) {
        boolean flagSampleData = false;
        E_InvoiceCommon.ReqPlGenEwbByIRN reqPlGenEwbByIRN = new E_InvoiceCommon.ReqPlGenEwbByIRN();
        //<editor-fold defaultstate="collapsed" desc="Testing MDL">
        if (flagSampleData) {
            reqPlGenEwbByIRN.Irn = "0fab4fedafae332f2ea658d6f0095514cd2f194db0c91eb83c704396ce11c348";
            //<editor-fold defaultstate="collapsed" desc="Transporter Detail">
            reqPlGenEwbByIRN.TransId = "12AWGPV7107B1Z1";
            reqPlGenEwbByIRN.TransName = "trans name";
            reqPlGenEwbByIRN.TransDocNo = "TRAN/DOC/11";
            reqPlGenEwbByIRN.TransDocDt = "01/08/2020";
            reqPlGenEwbByIRN.TransMode = "1";
            reqPlGenEwbByIRN.VehNo = "KA12ER1234";
            reqPlGenEwbByIRN.VehType = "R";
            reqPlGenEwbByIRN.Distance = 100;
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ExportShipDetails">
            reqPlGenEwbByIRN.ExpShipDtls = (new E_InvoiceCommon.ReqPlGenEwbByIRN()).new ExportShipDetails();
            reqPlGenEwbByIRN.ExpShipDtls.Addr1 = "7th block, kuvempu layout";
            reqPlGenEwbByIRN.ExpShipDtls.Addr2 = "kuvempu layout";
            reqPlGenEwbByIRN.ExpShipDtls.Loc = "Banagalore";
            reqPlGenEwbByIRN.ExpShipDtls.Pin = 562160;
            reqPlGenEwbByIRN.ExpShipDtls.Stcd = "29";
            //</editor-fold>      
            //<editor-fold defaultstate="collapsed" desc="Dispatched From Details">
            reqPlGenEwbByIRN.DispDtls = (new E_InvoiceCommon.ReqPlGenEwbByIRN()).new DispatchedDetails();
            reqPlGenEwbByIRN.DispDtls.Nm = "ABC company pvt ltd";
            reqPlGenEwbByIRN.DispDtls.Addr1 = "7th block, kuvempu layout";
            reqPlGenEwbByIRN.DispDtls.Addr2 = "kuvempu layout";
            reqPlGenEwbByIRN.DispDtls.Loc = "Banagalore";
            reqPlGenEwbByIRN.DispDtls.Pin = 562160;
            reqPlGenEwbByIRN.DispDtls.Stcd = "29";
            //</editor-fold>          
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="fill eGST Data">
        if (!flagSampleData) {
            reqPlGenEwbByIRN.Irn = DmShared.foriegnKey(eGstMdl.getEinv_IrnNo());
            //<editor-fold defaultstate="collapsed" desc="Transporter Detail">
            reqPlGenEwbByIRN.TransId = DmShared.foriegnKey(eGstMdl.getTransporter_Mdl().getTransporterId());
            reqPlGenEwbByIRN.TransName = DmShared.foriegnKey(eGstMdl.getTransporter_Mdl().getSharedKeyFieldName());
            reqPlGenEwbByIRN.TransMode = DmShared.foriegnKey(eGstMdl.getTransportModeCode());
            reqPlGenEwbByIRN.Distance = DmShared.iNull(eGstMdl.getTransDistance()).intValue();
            reqPlGenEwbByIRN.TransDocNo = DmShared.foriegnKey(eGstMdl.getTransportLrNo());
            reqPlGenEwbByIRN.TransDocDt = DmShared.foriegnKey(convertDate(eGstMdl.getTransportLrDate()));
            reqPlGenEwbByIRN.VehNo = DmShared.foriegnKey(eGstMdl.getVehicleNo());
            reqPlGenEwbByIRN.VehType = DmShared.foriegnKey(eGstMdl.getGstVehicleType());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ExportShipDetails">
            Party_Mdl billToMdl = eGstMdl.getBillTo_Mdl().clone();
            Party_Mdl shipToMdl = eGstMdl.getShipTo_Mdl().clone();
            boolean flagShipDetail = false;
            String buyerInfo = Gst_Shared.concatPartyInfo(billToMdl.getGstin(), billToMdl.getLegalName(), billToMdl.getAddress1(), billToMdl.getAddress2(), billToMdl.getCityName(), billToMdl.getPinCode(), billToMdl.getStateName(), billToMdl.getCountryName());
            String shipToInfo = Gst_Shared.concatPartyInfo(shipToMdl.getGstin(), shipToMdl.getLegalName(), shipToMdl.getAddress1(), shipToMdl.getAddress2(), shipToMdl.getCityName(), shipToMdl.getPinCode(), shipToMdl.getStateName(), shipToMdl.getCountryName());
            if (!DmShared.xNull(buyerInfo).equalsIgnoreCase(shipToInfo)) {
                flagShipDetail = true;
            }
            if (flagShipDetail) {
                reqPlGenEwbByIRN.ExpShipDtls = (new E_InvoiceCommon.ReqPlGenEwbByIRN()).new ExportShipDetails();
                reqPlGenEwbByIRN.ExpShipDtls.Addr1 = DmShared.foriegnKey(shipToMdl.getAddress1());
                reqPlGenEwbByIRN.ExpShipDtls.Addr2 = DmShared.foriegnKey(shipToMdl.getAddress2());
                reqPlGenEwbByIRN.ExpShipDtls.Loc = DmShared.foriegnKey(shipToMdl.getCityName());
                reqPlGenEwbByIRN.ExpShipDtls.Pin = DmShared.iNull(shipToMdl.getPinCode()).intValue();
                reqPlGenEwbByIRN.ExpShipDtls.Stcd = DmShared.foriegnKey(shipToMdl.getStateNumericCode());
                eGstMdl.setActToStateNumericCode(DmShared.xNull(shipToMdl.getStateNumericCode()));

            } else {
                reqPlGenEwbByIRN.ExpShipDtls = null;
            }
            //</editor-fold>      
            //<editor-fold defaultstate="collapsed" desc="Dispatched From Details">
            Party_Mdl consignerMdl = eGstMdl.getConsigner_Mdl().clone();
            Party_Mdl dispatcherMdl = eGstMdl.getDispatchFrom_Mdl().clone();
            boolean flagDispatchDetail = false;
            String consignerInfo = Gst_Shared.concatPartyInfo(consignerMdl.getGstin(), consignerMdl.getLegalName(), consignerMdl.getAddress1(), consignerMdl.getAddress2(), consignerMdl.getCityName(), consignerMdl.getPinCode(), consignerMdl.getStateName(), consignerMdl.getCountryName());
            String dispatcherInfo = Gst_Shared.concatPartyInfo(dispatcherMdl.getGstin(), dispatcherMdl.getLegalName(), dispatcherMdl.getAddress1(), dispatcherMdl.getAddress2(), dispatcherMdl.getCityName(), dispatcherMdl.getPinCode(), dispatcherMdl.getStateName(), dispatcherMdl.getCountryName());
            if (!DmShared.xNull(consignerInfo).equalsIgnoreCase(dispatcherInfo)) {
                flagDispatchDetail = true;
            }
            if (flagDispatchDetail) {
                reqPlGenEwbByIRN.DispDtls = (new E_InvoiceCommon.ReqPlGenEwbByIRN()).new DispatchedDetails();
                reqPlGenEwbByIRN.DispDtls.Nm = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getLegalName());
                reqPlGenEwbByIRN.DispDtls.Addr1 = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getAddress1());
                reqPlGenEwbByIRN.DispDtls.Addr2 = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getAddress2());
                reqPlGenEwbByIRN.DispDtls.Loc = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getCityName());
                reqPlGenEwbByIRN.DispDtls.Pin = DmShared.iNull(eGstMdl.getDispatchFrom_Mdl().getPinCode()).intValue();
                reqPlGenEwbByIRN.DispDtls.Stcd = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getStateNumericCode());
                eGstMdl.setActFromStateNumericCode(DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getStateNumericCode()));
            } else {
                reqPlGenEwbByIRN.DispDtls = null;
            }
            //</editor-fold>            
        }
        //</editor-fold>
        return new Gson().toJson(reqPlGenEwbByIRN);
    }

    public static MasterEGst_Mdl getEGstRecord(String docid, Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        MasterEGst_Mdl masterEGst_Mdl = null;
        EGst_Mdl egst_Mdl = null;
        GstDocument_Mdl gdm = new GstDocument_Mdl();
        EGstToken_Mdl eGstToken_Mdl = null;
        try {
            //<editor-fold defaultstate="collapsed" desc="get Egst Details">
            sqlQuery = "SELECT \n"
                    + "   eg.docid, eg.v_type, eg.v_Prefix, eg.site_Code, eg.v_date, eg.taxScheme, eg.eComGstin \n"
                    + " , eg.recID, eg.recIdPrefix, eg.recIdSerial, eg.isIgstOnIntra \n"
                    + " , eg.gstType, eg.isRCM, eg.posStateNumericCode, eg.posStateName, eg.partyType, eg.locationCode \n"
                    + "	, eg.seller_SiteCode, eg.seller_Name, eg.seller_Gstin, eg.seller_GstinDate \n"
                    + " , eg.seller_TradeName, eg.seller_LegalName, eg.seller_Add1, eg.seller_Add2 \n"
                    + " , eg.seller_Pin, eg.actFrom_StateNumericCode, eg.seller_StateName, eg.seller_StateNumericCode \n"
                    + " , eg.seller_CityCode , eg.seller_CityName, eg.seller_Mobile, eg.seller_Email \n"
                    + "	, eg.buyer_AcCode,eg.buyer_SiteCode, eg.buyer_Name, eg.buyer_Gstin, eg.buyer_GstinDate"
                    + " , eg.buyer_LegalName, eg.buyer_TradeName, eg.buyer_Add1, eg.buyer_Add2 \n"
                    + " , eg.actTo_StateNumericCode, eg.buyer_StateNumericCode, eg.buyer_StateName,eg.buyer_CityCode, eg.buyer_CityName \n"
                    + " , eg.buyer_Pin, eg.buyer_Mobile, eg.buyer_Email \n"
                    + "	, eg.dispFrom_SiteCode, eg.dispFrom_Name, eg.dispFrom_Gstin, eg.dispFrom_GstinDate"
                    + " , eg.dispFrom_LegalName, eg.dispFrom_TradeName , eg.dispFrom_Add1, eg.dispFrom_Add2 \n "
                    + " , eg.dispFrom_Pin, eg.dispFrom_StateNumericCode, eg.dispFrom_StateName ,eg.dispFrom_CityCode,eg.dispFrom_CityName \n"
                    + " , eg.dispFrom_Mobile, eg.dispFrom_Email \n"
                    + "	, eg.shipTo_AcCode, eg.shipTo_SiteCode , eg.shipTo_Name , eg.shipTo_Gstin, eg.shipTo_GstinDate"
                    + " , eg.shipTo_LegalName, eg.shipTo_TradeName, eg.shipTo_Add1, eg.shipTo_Add2 \n"
                    + " , eg.shipTo_Pin, eg.shipTo_StateNumericCode ,shipTo_StateName"
                    + " , eg.shipTo_Mobile, eg.shipTo_Email, eg.shipTo_CityCode, eg.shipTo_CityName \n"
                    + "	, eg.consignee_AcCode, eg.consignee_SiteCode, eg.consignee_Name, eg.consignee_Gstin, eg.consignee_GstinDate"
                    + " , eg.consignee_LegalName, eg.consignee_TradeName, eg.consignee_pin, eg.consignee_Mobile, eg.consignee_Email \n"
                    + " , eg.consignee_Add1, eg.consignee_Add2, eg.consignee_StateNumericCode"
                    + " , eg.consignee_StateName , eg.consignee_CityCode, eg.consignee_CityName \n"
                    + " , eg.transportMode, eg.transporter_AcCode, eg.transporter_Name, eg.transporter_Id \n"
                    + " , eg.transporter_Gstin, eg.transporter_GstinDate \n"
                    + " , eg.lrNo, eg.lrDate, eg.vehicleNo, eg.vehicleTypeCode, eg.vehicleTypeName, eg.gstVehicleType, eg.distance \n"
                    + " , eg.payee_Name, eg.payee_AcNo, eg.payee_ModeOfPayment, eg.payee_BranchCode, eg.payee_CreditTransfer \n"
                    + " , eg.payee_PaymentInstruction, eg.payee_PaymentTerms, eg.payee_DirectDebit, eg.payee_CreditDays \n"
                    + " , eg.payee_PaidAmt, eg.payee_DueAmt \n"
                    + " , eg.eway_TransactionType , eg.eway_BillNo, eg.eWay_BillDt , eg.eway_ValidUpTo , eg.eway_Alert \n"
                    + " , eg.eway_SupplyType, eg.eway_SubSupplyType, eg.eway_SubSupplyDesc, eg.eway_DocType \n"
                    + " , eg.eway_Status, eg.eway_CancelReason , eg.eway_CancelRemark, eg.eway_cancelDt \n"
                    + " , eg.einv_Version, eg.einv_SupplyType, eg.einv_DocType, eg.einv_IrnNo, eg.einv_ackNo, eg.einv_ackDt \n"
                    + " , eg.einv_JwtIssuer, eg.einv_Status \n"
                    + " , eg.discountAmount, eg.taxableAmount, eg.sgstAmount, eg.cgstAmount, eg.igstAmount, eg.cessAmount, eg.cessNaAmount \n"
                    + " , eg.scessAmount, eg.otherCharges, eg.totalInvAmount, eg.roundOffAmount, eg.netInvAmount, eg.netInvAmountInFC \n"
                    + " , eg.mongoLinkId \n"
                    + " , eg.einv_CancelReason, eg.einv_CancelRemark, eg.einv_cancelDt, eg.eWay_GenerationMode"
                    + " , eg.u_EntDt_LatestLine, eg.isEWayBillRequired, eg.isEInvoiceRequired, eg.isSiteTransaction \n"
                    + " , eg.seller_GstCountryCode, eg.buyer_GstCountryCode, eg.dispFrom_GstCountryCode, eg.shipTo_GstCountryCode, eg.consignee_GstCountryCode \n"
                    + " , eg.seller_CountryName, eg.buyer_CountryName, eg.dispFrom_CountryName, eg.shipTo_CountryName, eg.consignee_CountryName \n"
                    + " , eg.refDocId, eg.refRecId, eg.refV_Date, eg.refDocOtherRef, eg.refInvDocStartDate \n"
                    + " , eg.refInvDocEndDate, eg.refRemark, eg.shippingBillNo, eg.shippingBillDate \n"
                    + " , eg.gstPortCode, eg.refundClaim, eg.gstCurrencyCode \n"
                    + " FROM eGst eg \n"
                    + " WHERE eg.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", docid);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                egst_Mdl = new EGst_Mdl();
                egst_Mdl.setDocid(DmShared.xNull(rsTemp.getString("docid")));
                egst_Mdl.setMongoLinkId(DmShared.xNull(rsTemp.getString("mongoLinkId")));
                egst_Mdl.setV_Type(DmShared.xNull(rsTemp.getString("v_Type")));
                egst_Mdl.setV_Prefix(DmShared.xNull(rsTemp.getString("v_Prefix")));
                egst_Mdl.setV_Date(DmShared.dNull(rsTemp.getDate("v_date")));
                egst_Mdl.setSite_Code(DmShared.xNull(rsTemp.getString("site_Code")));
                egst_Mdl.setTaxSchemeCode(DmShared.xNull(rsTemp.getString("taxScheme")));
                egst_Mdl.setTaxSchemeName(DmEnum.Egst_TaxScheme.getName(DmShared.iNull(rsTemp.getString("taxScheme")).intValue()).toString());
                egst_Mdl.seteComGstin(DmShared.xNull(rsTemp.getString("eComGstin")));
                egst_Mdl.setIgstOnIntra(DmEnum.DmYesNo.valueOf(DmShared.iNull(rsTemp.getString("isIgstOnIntra")).intValue()).getEnumShortName());
                egst_Mdl.setGstType(DmShared.xNull(rsTemp.getString("gstType")));
                egst_Mdl.setReverseCharge(DmEnum.DmYesNo.valueOf(DmShared.iNull(rsTemp.getString("isRCM")).intValue()).getEnumShortName());
                egst_Mdl.setPosStateNumericCode(DmShared.xNull(rsTemp.getString("posStateNumericCode")));
                egst_Mdl.setPosStateName(DmShared.xNull(rsTemp.getString("posStateName")));
                egst_Mdl.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                egst_Mdl.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    egst_Mdl.setU_EntDt_LatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
                egst_Mdl.setEinvoiceRequired(DmShared.iNull(rsTemp.getString("isEInvoiceRequired")).intValue() == DmEnum.DmYesNo.YES.toInt());
                egst_Mdl.setEwayBillRequired(DmShared.iNull(rsTemp.getString("isEWayBillRequired")).intValue() == DmEnum.DmYesNo.YES.toInt());
                egst_Mdl.setSiteTransaction(DmShared.iNull(rsTemp.getString("isSiteTransaction")).intValue() == DmEnum.DmYesNo.YES.toInt());

                //<editor-fold defaultstate="collapsed" desc="Recid">
                RecId recId = new RecId();
                recId.setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                recId.setCounter(DmShared.iNull(rsTemp.getString("recIdSerial")).longValue());
                recId.setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                egst_Mdl.setRecId(recId);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="SELLER INFORMATION">
                Party_Mdl fromSeller = new Party_Mdl();
                fromSeller.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("seller_SiteCode")));
                fromSeller.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("seller_Name")));
                fromSeller.setSiteCode(DmShared.xNull(rsTemp.getString("seller_SiteCode")));

                fromSeller.setGstin(DmShared.xNull(rsTemp.getString("seller_Gstin")));
                fromSeller.setGstinDate(DmShared.dNull(rsTemp.getDate("seller_GstinDate")));
                fromSeller.setLegalName(DmShared.xNull(rsTemp.getString("seller_LegalName")));
                fromSeller.setTradeName(DmShared.xNull(rsTemp.getString("seller_TradeName")));
                fromSeller.setAddress1(DmShared.xNull(rsTemp.getString("seller_Add1")));
                fromSeller.setAddress2(DmShared.xNull(rsTemp.getString("seller_Add2")));
                fromSeller.setPinCode(DmShared.xNull(rsTemp.getString("seller_Pin")));
                fromSeller.setStateNumericCode(DmShared.xNull(rsTemp.getString("seller_StateNumericCode")));
                fromSeller.setStateName(DmShared.xNull(rsTemp.getString("seller_StateName")));
                fromSeller.setCityCode(DmShared.xNull(rsTemp.getString("seller_CityCode")));
                fromSeller.setCityName(DmShared.xNull(rsTemp.getString("seller_CityName")));
                fromSeller.setPhone(DmShared.xNull(rsTemp.getString("seller_Mobile")));
                fromSeller.setEmail(DmShared.xNull(rsTemp.getString("seller_Email")));
                fromSeller.setGstCountryCode(DmShared.xNull(rsTemp.getString("seller_GstCountryCode")));
                fromSeller.setCountryName(DmShared.xNull(rsTemp.getString("seller_CountryName")));
                egst_Mdl.setConsigner_Mdl(fromSeller);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="BUYER INFORMATION">
                Party_Mdl toBuyer = new Party_Mdl();
                toBuyer.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("buyer_AcCode")));
                toBuyer.setSiteCode(DmShared.xNull(rsTemp.getString("buyer_SiteCode")));
                toBuyer.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("buyer_Name")));

                toBuyer.setGstin(DmShared.xNull(rsTemp.getString("buyer_Gstin")));
                toBuyer.setGstinDate(DmShared.dNull(rsTemp.getDate("buyer_GstinDate")));
                toBuyer.setLegalName(DmShared.xNull(rsTemp.getString("buyer_LegalName")));
                toBuyer.setTradeName(DmShared.xNull(rsTemp.getString("buyer_TradeName")));
                toBuyer.setAddress1(DmShared.xNull(rsTemp.getString("buyer_Add1")));
                toBuyer.setAddress2(DmShared.xNull(rsTemp.getString("buyer_Add2")));
                toBuyer.setPinCode(DmShared.xNull(rsTemp.getString("buyer_Pin")));
                toBuyer.setPlaceOfSupplyCode(DmShared.xNull(rsTemp.getString("posStateNumericCode")));
                toBuyer.setStateName(DmShared.xNull(rsTemp.getString("buyer_StateName")));
                toBuyer.setStateNumericCode(DmShared.xNull(rsTemp.getString("buyer_StateNumericCode")));
                toBuyer.setCityCode(DmShared.xNull(rsTemp.getString("buyer_CityCode")));
                toBuyer.setCityName(DmShared.xNull(rsTemp.getString("buyer_CityName")));
                toBuyer.setPhone(DmShared.xNull(rsTemp.getString("buyer_Mobile")));
                toBuyer.setEmail(DmShared.xNull(rsTemp.getString("buyer_Email")));
                toBuyer.setGstCountryCode(DmShared.xNull(rsTemp.getString("buyer_GstCountryCode")));
                toBuyer.setCountryName(DmShared.xNull(rsTemp.getString("buyer_CountryName")));
                egst_Mdl.setBillTo_Mdl(toBuyer);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Consignee INFORMATION">
                Party_Mdl toConsignee = new Party_Mdl();
                toConsignee.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("consignee_AcCode")));
                toConsignee.setSiteCode(DmShared.xNull(rsTemp.getString("consignee_SiteCode")));
                toConsignee.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("consignee_Name")));

                toConsignee.setGstin(DmShared.xNull(rsTemp.getString("consignee_Gstin")));
                toConsignee.setGstinDate(DmShared.dNull(rsTemp.getDate("consignee_GstinDate")));
                toConsignee.setLegalName(DmShared.xNull(rsTemp.getString("consignee_LegalName")));
                toConsignee.setTradeName(DmShared.xNull(rsTemp.getString("consignee_TradeName")));
                toConsignee.setAddress1(DmShared.xNull(rsTemp.getString("consignee_Add1")));
                toConsignee.setAddress2(DmShared.xNull(rsTemp.getString("consignee_Add2")));
                toConsignee.setPinCode(DmShared.xNull(rsTemp.getString("consignee_Pin")));
                toConsignee.setStateName(DmShared.xNull(rsTemp.getString("consignee_StateName")));
                toConsignee.setStateNumericCode(DmShared.xNull(rsTemp.getString("consignee_StateNumericCode")));
                toConsignee.setCityCode(DmShared.xNull(rsTemp.getString("consignee_CityCode")));
                toConsignee.setCityName(DmShared.xNull(rsTemp.getString("consignee_CityName")));
                toConsignee.setPhone(DmShared.xNull(rsTemp.getString("consignee_Mobile")));
                toConsignee.setEmail(DmShared.xNull(rsTemp.getString("consignee_Email")));
                toConsignee.setGstCountryCode(DmShared.xNull(rsTemp.getString("consignee_GstCountryCode")));
                toConsignee.setCountryName(DmShared.xNull(rsTemp.getString("consignee_CountryName")));
                egst_Mdl.setConsignee_Mdl(toConsignee);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="DISPATCH INFORMATION">
                Party_Mdl dispatchFrom = new Party_Mdl();
                dispatchFrom.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("dispFrom_SiteCode")));
                dispatchFrom.setSiteCode(DmShared.xNull(rsTemp.getString("dispFrom_SiteCode")));
                dispatchFrom.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("dispFrom_Name")));

                dispatchFrom.setGstin(DmShared.xNull(rsTemp.getString("dispFrom_Gstin")));
                dispatchFrom.setGstinDate(DmShared.dNull(rsTemp.getDate("dispFrom_GstinDate")));
                dispatchFrom.setLegalName(DmShared.xNull(rsTemp.getString("dispFrom_LegalName")));
                dispatchFrom.setTradeName(DmShared.xNull(rsTemp.getString("dispFrom_TradeName")));
                dispatchFrom.setAddress1(DmShared.xNull(rsTemp.getString("dispFrom_Add1")));
                dispatchFrom.setAddress2(DmShared.xNull(rsTemp.getString("dispFrom_Add2")));
                dispatchFrom.setPinCode(DmShared.xNull(rsTemp.getString("dispFrom_Pin")));
                dispatchFrom.setStateNumericCode(DmShared.xNull(rsTemp.getString("dispFrom_StateNumericCode")));
                dispatchFrom.setStateName(DmShared.xNull(rsTemp.getString("dispFrom_StateName")));
                dispatchFrom.setCityCode(DmShared.xNull(rsTemp.getString("dispFrom_CityCode")));
                dispatchFrom.setCityName(DmShared.xNull(rsTemp.getString("dispFrom_CityName")));
                dispatchFrom.setPhone(DmShared.xNull(rsTemp.getString("dispFrom_Mobile")));
                dispatchFrom.setEmail(DmShared.xNull(rsTemp.getString("dispFrom_Email")));
                dispatchFrom.setGstCountryCode(DmShared.xNull(rsTemp.getString("dispFrom_GstCountryCode")));
                dispatchFrom.setCountryName(DmShared.xNull(rsTemp.getString("dispFrom_CountryName")));
                egst_Mdl.setDispatchFrom_Mdl(dispatchFrom);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="SHIP TO INFORMATION">
                Party_Mdl shipTo = new Party_Mdl();
                shipTo.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("shipTo_AcCode")));
                shipTo.setSiteCode(DmShared.xNull(rsTemp.getString("shipTo_SiteCode")));
                shipTo.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("shipTo_Name")));

                shipTo.setGstin(DmShared.xNull(rsTemp.getString("shipTo_Gstin")));
                shipTo.setGstinDate(DmShared.dNull(rsTemp.getDate("shipTo_GstinDate")));
                shipTo.setTradeName(DmShared.xNull(rsTemp.getString("shipTo_TradeName")));
                shipTo.setLegalName(DmShared.xNull(rsTemp.getString("shipTo_LegalName")));
                shipTo.setAddress1(DmShared.xNull(rsTemp.getString("shipTo_Add1")));
                shipTo.setAddress2(DmShared.xNull(rsTemp.getString("shipTo_Add2")));
                shipTo.setPinCode(DmShared.xNull(rsTemp.getString("shipTo_Pin")));
                shipTo.setStateNumericCode(DmShared.xNull(rsTemp.getString("shipTo_StateNumericCode")));
                shipTo.setStateName(DmShared.xNull(rsTemp.getString("shipTo_StateName")));
                shipTo.setCityCode(DmShared.xNull(rsTemp.getString("shipTo_CityCode")));
                shipTo.setCityName(DmShared.xNull(rsTemp.getString("shipTo_CityName")));
                shipTo.setPhone(DmShared.xNull(rsTemp.getString("shipTo_Mobile")));
                shipTo.setEmail(DmShared.xNull(rsTemp.getString("shipTo_Email")));
                shipTo.setGstCountryCode(DmShared.xNull(rsTemp.getString("shipTo_GstCountryCode")));
                shipTo.setCountryName(DmShared.xNull(rsTemp.getString("shipTo_CountryName")));
                egst_Mdl.setShipTo_Mdl(shipTo);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="TRANSPORTER INFORMATION">
                egst_Mdl.setTransportModeCode(DmShared.xNull(rsTemp.getString("transportMode")));
                if (DmShared.iNull(rsTemp.getString("transportMode")).intValue() > 0) {
                    egst_Mdl.setTransportModeName(DmEnum.TransportMode.getViaKey(DmShared.iNull(rsTemp.getString("transportMode")).intValue()).getValue());
                }
                egst_Mdl.setTransportLrNo(DmShared.xNull(rsTemp.getString("lrNo")));
                egst_Mdl.setTransportLrDate(DmShared.dNull(rsTemp.getDate("lrDate")));
                egst_Mdl.setVehicleNo(DmShared.xNull(rsTemp.getString("vehicleNo")));
                egst_Mdl.setVehicleTypeCode(DmShared.xNull(rsTemp.getString("vehicleTypeCode")));
                egst_Mdl.setVehicleTypeName(DmShared.xNull(rsTemp.getString("vehicleTypeName")));
                egst_Mdl.setGstVehicleType(DmShared.xNull(rsTemp.getString("gstVehicleType")));
                egst_Mdl.setTransDistance(DmShared.xNull(rsTemp.getString("distance")));
                Party_Mdl transporterInfo = new Party_Mdl();
                transporterInfo.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("transporter_AcCode")));
                transporterInfo.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("transporter_Name")));
                transporterInfo.setGstin(DmShared.xNull(rsTemp.getString("transporter_Gstin")));
                transporterInfo.setGstinDate(DmShared.dNull(rsTemp.getDate("transporter_GstinDate")));
                transporterInfo.setTransporterId(DmShared.xNull(rsTemp.getString("transporter_Id")));
                egst_Mdl.setTransporter_Mdl(transporterInfo);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Payee Details">
                egst_Mdl.setPayee_Name(DmShared.xNull(rsTemp.getString("payee_Name")));
                egst_Mdl.setPayee_AccountNo(DmShared.xNull(rsTemp.getString("payee_AcNo")));
                egst_Mdl.setPayee_ModeOfPayment(DmShared.xNull(rsTemp.getString("payee_ModeOfPayment")));
                egst_Mdl.setPayee_BranchCode(DmShared.xNull(rsTemp.getString("payee_BranchCode")));
                egst_Mdl.setPayee_CrTrn(DmShared.xNull(rsTemp.getString("payee_CreditTransfer")));
                egst_Mdl.setPayee_PaymentInstruction(DmShared.xNull(rsTemp.getString("payee_PaymentInstruction")));
                egst_Mdl.setPayee_PaymentTerms(DmShared.xNull(rsTemp.getString("payee_PaymentTerms")));
                egst_Mdl.setPayee_DirectDebit(DmShared.xNull(rsTemp.getString("payee_DirectDebit")));
                egst_Mdl.setPayee_CreditDays(DmShared.xNull(rsTemp.getString("payee_CreditDays")));
                egst_Mdl.setPayee_PaidAmount(DmShared.xNull(rsTemp.getString("payee_PaidAmt")));
                egst_Mdl.setPayee_PaymentDue(DmShared.xNull(rsTemp.getString("payee_DueAmt")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="E-Way Details">
                egst_Mdl.setEway_GenerationMode(DmShared.xNull(rsTemp.getString("eWay_GenerationMode")));
                egst_Mdl.setEwayDocTypeCode(DmShared.xNull(rsTemp.getString("eway_DocType")));
                egst_Mdl.setEwayDocTypeName(DmEnum.Eway_DocType.getName(DmShared.xNull(rsTemp.getString("eway_DocType"))));
                egst_Mdl.setEwaySupplyTypeCode(DmShared.xNull(rsTemp.getString("eway_SupplyType")));
                egst_Mdl.setEwaySupplyTypeName(DmEnum.Eway_SupplyType.getName(DmShared.xNull(rsTemp.getString("eway_SupplyType"))));
                egst_Mdl.setEwaySubSupplyTypeCode(DmShared.xNull(rsTemp.getString("eway_SubSupplyType")));
                egst_Mdl.setEwaySubSupplyTypeName(DmEnum.Eway_SubSupplyType.getName(DmShared.iNull(rsTemp.getString("eway_SubSupplyType")).intValue()));
                egst_Mdl.setEwaySubSupplyDesc(DmShared.xNull(rsTemp.getString("eway_SubSupplyDesc")));
                egst_Mdl.setEwayTransTypeCode(DmShared.xNull(rsTemp.getString("eway_TransactionType")));
                egst_Mdl.setEwayTransTypeName(DmEnum.Eway_TransactionType.getViaCode(DmShared.iNull(rsTemp.getString("eway_TransactionType")).intValue()));
                egst_Mdl.setEway_Status(DmShared.xNull(rsTemp.getString("eway_Status")));
                //<editor-fold defaultstate="collapsed" desc="E-Way Bill Info">
                egst_Mdl.setEway_BillNo(DmShared.xNull(rsTemp.getString("eway_BillNo")));
                egst_Mdl.setEway_BillDate(getDateTime(rsTemp.getTimestamp("eWay_BillDt")));
                egst_Mdl.setEway_validUpto(getDateTime(rsTemp.getTimestamp("eway_ValidUpTo")));
                egst_Mdl.setEway_alert(DmShared.xNull(rsTemp.getString("eway_Alert")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="E-Way Cancel Details">
                egst_Mdl.setEway_cancelRsnCode(DmShared.xNull(rsTemp.getString("eway_CancelReason")));
                egst_Mdl.setEway_cancelRmrk(DmShared.xNull(rsTemp.getString("eway_CancelRemark")));
                egst_Mdl.setEway_cancelDate(getDateTime(rsTemp.getTimestamp("eway_CancelDt")));
                //</editor-fold>                 
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Einvoice Details">
                egst_Mdl.setEinv_version(DmShared.xNull(rsTemp.getString("einv_Version")));
                egst_Mdl.setEinvSupplyTypeCode(DmShared.xNull(rsTemp.getString("einv_SupplyType")));
                egst_Mdl.setEinvSupplyTypeName(DmEnum.Einv_SupplyTypeCode.getValue(DmShared.xNull(rsTemp.getString("einv_SupplyType"))));
                egst_Mdl.setEinvDocTypeCode(DmShared.xNull(rsTemp.getString("einv_DocType")));
                egst_Mdl.setEinvDocTypeName(DmEnum.Einv_DocTypeCode.getValue(DmShared.xNull(rsTemp.getString("einv_DocType"))));
                egst_Mdl.setEinv_IrnNo(DmShared.xNull(rsTemp.getString("einv_IrnNo")));
                egst_Mdl.setEinv_ackNo(DmShared.xNull(rsTemp.getString("einv_ackNo")));
                egst_Mdl.setEinv_ackDt(DmShared.xNull(getDateTime(rsTemp.getTimestamp("einv_ackDt"))));
                egst_Mdl.setEinv_Status(DmShared.xNull(rsTemp.getString("einv_Status")));
                egst_Mdl.setEinv_JwtIssuer(DmShared.xNull(rsTemp.getString("einv_JwtIssuer")));
                //<editor-fold defaultstate="collapsed" desc="Einv Cancel Details">
                egst_Mdl.setEinv_cancelRsnCode(DmShared.xNull(rsTemp.getString("einv_CancelReason")));
                egst_Mdl.setEinv_cancelRmrk(DmShared.xNull(rsTemp.getString("einv_CancelRemark")));
                egst_Mdl.setEinv_cancelDate(getDateTime(rsTemp.getTimestamp("einv_cancelDt")));
                //</editor-fold>       
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="get Header Tax Info">
                egst_Mdl.setDiscountAmount(DmShared.xNull(rsTemp.getString("discountAmount")));
                egst_Mdl.setTaxableValue(DmServer.vNull(rsTemp.getString("taxableAmount"), "0.00"));
                egst_Mdl.setSgstValue(DmServer.vNull(rsTemp.getString("sgstAmount"), "0.00"));
                egst_Mdl.setCgstValue(DmServer.vNull(rsTemp.getString("cgstAmount"), "0.00"));
                egst_Mdl.setIgstValue(DmServer.vNull(rsTemp.getString("igstAmount"), "0.00"));
                egst_Mdl.setCessValue(DmServer.vNull(rsTemp.getString("cessAmount"), "0.00"));
                egst_Mdl.setCessNonAdvolValue(DmServer.vNull(rsTemp.getString("cessNaAmount"), "0.00"));
                egst_Mdl.setStateCessAmount(DmShared.xNull(rsTemp.getString("scessAmount")));
                egst_Mdl.setOtherAmount(DmServer.vNull(rsTemp.getString("otherCharges"), "0.00"));
                egst_Mdl.setTotInvValue(DmServer.vNull(rsTemp.getString("totalInvAmount"), "0.00"));
                egst_Mdl.setRoundOfAmount(DmServer.vNull(rsTemp.getString("roundOffAmount"), "0.00"));
                egst_Mdl.setNetInvAmount(DmServer.vNull(rsTemp.getString("netInvAmount"), "0.00"));
                egst_Mdl.setTotalInvAmtInFc(DmServer.vNull(rsTemp.getString("netInvAmountInFC"), "0.00"));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="ActStateCode">
                egst_Mdl.setActFromStateNumericCode(DmShared.xNull(rsTemp.getString("actFrom_StateNumericCode")));
                egst_Mdl.setActToStateNumericCode(DmShared.xNull(rsTemp.getString("actTo_StateNumericCode")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="RefDtls">
                egst_Mdl.setRefDocid(DmShared.xNull(rsTemp.getString("refDocId")));
                egst_Mdl.setRefRecId(DmShared.xNull(rsTemp.getString("refRecId")));
                egst_Mdl.setRefV_Date(DmShared.dNull(rsTemp.getDate("refV_Date")));
                egst_Mdl.setRefDocOtherRef(DmShared.xNull(rsTemp.getString("refDocOtherRef")));
                egst_Mdl.setRefInvDocStartDate(DmShared.dNull(rsTemp.getDate("refInvDocStartDate")));
                egst_Mdl.setRefInvDocEndDate(DmShared.dNull(rsTemp.getDate("refInvDocEndDate")));
                egst_Mdl.setRefRemark(DmShared.xNull(rsTemp.getString("refRemark")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Export Details">
                egst_Mdl.setShippingBillNo(DmShared.xNull(rsTemp.getString("shippingBillNo")));
                egst_Mdl.setShippingBillDate(DmShared.dNull(rsTemp.getDate("shippingBillDate")));
                egst_Mdl.setPortCode(DmShared.xNull(rsTemp.getString("gstPortCode")));
                egst_Mdl.setRefundClaim(DmShared.iNull(rsTemp.getString("refundClaim")).toString());
                egst_Mdl.setCurrencyCode(DmShared.xNull(rsTemp.getString("gstCurrencyCode")));
                //</editor-fold>
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>
            if (egst_Mdl != null) {
                //<editor-fold defaultstate="collapsed" desc="Check TP Bill Genrated or not">
                boolean isTpBillGenrated = false;
                String tt_u_EntDt_LatestLine = "";
                sqlQuery = "SELECT tt.isStatus,tt.u_EntDt_LatestLine \n"
                        + " FROM transporterTrans tt \n"
                        + " WHERE tt.docid = :docid \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.xNull(docid));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    isTpBillGenrated = (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 2);
                    if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                        tt_u_EntDt_LatestLine = DmShared.iNull(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()).toString();
                    }
                }
                rsTemp.close();
                psTemp.close();

                egst_Mdl.setTpBillGenrated(isTpBillGenrated);
                egst_Mdl.setTt_u_EntDt_LatestLine(tt_u_EntDt_LatestLine);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="get Mongo Details">

                if (!DmShared.xNull(egst_Mdl.getMongoLinkId()).trim().equalsIgnoreCase("")) {
                    EinvoiceMongo_Mdl einvMongo = EinvoiceMongo_Server.get("_id", DmShared.xNull(egst_Mdl.getMongoLinkId()), dmRss);
                    if (einvMongo != null) {
                        if (DmShared.xNull(einvMongo.getDocid()).trim().equalsIgnoreCase(DmShared.xNull(docid))) {
                            egst_Mdl.setEinv_ExtractedSignedQrCodeJson(DmShared.xNull(einvMongo.getExtracted_Signed_QrCodeJson()));
                            egst_Mdl.setEinv_SignedInvoice(DmShared.xNull(einvMongo.getSigned_e_invoice()));
                            egst_Mdl.setEinv_SignedQrCode(DmShared.xNull(einvMongo.getSigned_Qr_Code()));
                            egst_Mdl.setEinv_SignedQrCodeImage(DmShared.xNull(einvMongo.getSigned_Qr_Code_Img()));
                            egst_Mdl.setEinv_ExtractedSignedInvoiceDataJson(DmShared.xNull(einvMongo.getExtracted_Signed_Invoice_DataJson()));
                        }
                    }
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="eGstContractDetail">
                sqlQuery = "SELECT \n"
                        + "   egcd.docid, egcd.v_SNo, egcd.receiptAdviceNo, egcd.receiptAdviceDate "
                        + " , egcd.tenderRefNo, egcd.contractNo \n"
                        + " , egcd.extraRefNo, egcd.projectRefNo , egcd.poRefNo, egcd.poRefDate \n"
                        + " FROM eGstContractDetail egcd \n"
                        + " WHERE egcd.docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", docid);
                rsTemp = psTemp.executeQuery();
                egst_Mdl.getContractDetails().clear();
                while (rsTemp.next()) {
                    EinvRefContractDtls_Mdl contract = new EinvRefContractDtls_Mdl();
                    contract.setDocid(DmShared.iNull(rsTemp.getString("docid")).toString());
                    contract.setvSNo(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                    contract.setReceiptAdviceNo(DmShared.xNull(rsTemp.getString("receiptAdviceNo")));
                    contract.setReceiptAdviceDate(DmShared.xNull(rsTemp.getString("receiptAdviceDate")));
                    contract.setTenderRefNo(DmShared.xNull(rsTemp.getString("tenderRefNo")));
                    contract.setContractNo(DmShared.xNull(rsTemp.getString("contractNo")));
                    contract.setExtraRefNo(DmShared.xNull(rsTemp.getString("extraRefNo")));
                    contract.setProjectRefNo(DmShared.xNull(rsTemp.getString("projectRefNo")));
                    contract.setPoRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));
                    contract.setPoRefDate(DmShared.xNull(rsTemp.getString("poRefDate")));
                    egst_Mdl.getContractDetails().add(contract);
                }
                rsTemp.close();
                psTemp.close();
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="eGstDocDetail">
                sqlQuery = " SELECT \n"
                        + " egdd.docid, egdd.v_SNo, egdd.docUrl, egdd.docEncoded , egdd.docInfo \n"
                        + " FROM eGstDocDetail egdd \n"
                        + " WHERE egdd.docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", docid);
                rsTemp = psTemp.executeQuery();
                egst_Mdl.getAddDocList().clear();
                while (rsTemp.next()) {
                    EinvAdditionalDocDtls_Mdl addDoc = new EinvAdditionalDocDtls_Mdl();
                    addDoc.setDocid(DmShared.iNull(rsTemp.getString("docid")).toString());
                    addDoc.setvSno(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                    addDoc.setDocumentUrl(DmShared.xNull(rsTemp.getString("docUrl")));
                    addDoc.setDocument(DmShared.xNull(rsTemp.getString("docEncoded")));
                    addDoc.setAdditionalInfoOfDocument(DmShared.xNull(rsTemp.getString("docInfo")));
                    egst_Mdl.getAddDocList().add(addDoc);
                }
                rsTemp.close();
                psTemp.close();
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Item Details">
                sqlQuery = "SELECT \n"
                        + "   egd.docid, egd.v_SNo, egd.itemCode, egd.ibCode , egd.itemName, egd.itemDesc \n"
                        + " , egd.hsn, egd.itemGSTNature, egd.rate, egd.quantity, egd.freeQty \n"
                        + " , egd.unitCode, egd.unitName, egd.gstUnit, egd.batchNo \n"
                        + " , egd.warrentyDate, egd.expiryDate \n"
                        + " , egd.grossAmount, egd.discountAmount, egd.taxableAmount, egd.gstRate, egd.cgstRate, egd.sgstRate \n"
                        + " , egd.igstRate, egd.cessRate, egd.cessNonAdvolRate, egd.sCessRate \n"
                        + " , egd.sgstAmount, egd.cgstAmount, egd.igstAmount, egd.cessAmount, egd.cessNaAmount"
                        + " , egd.scessAmount, egd.otherCharges, egd.netAmount  \n"
                        + " , egd.isService ,egd.propertyDispCsv,egd.barCode,egd.poRefNo,egd.gstOriginCountryCode,egd.itemUSNo \n"
                        + " FROM eGstDetail egd \n"
                        + " WHERE egd.docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", docid);
                rsTemp = psTemp.executeQuery();
                egst_Mdl.getItemList().clear();
                while (rsTemp.next()) {
                    EGstDetail_Mdl detail_Mdl = new EGstDetail_Mdl();
                    detail_Mdl.setDocid(DmShared.iNull(rsTemp.getString("docid")).toString());
                    detail_Mdl.setvSno(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                    detail_Mdl.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                    detail_Mdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                    detail_Mdl.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                    detail_Mdl.setItemDesc(DmShared.xNull(rsTemp.getString("itemDesc")));
                    detail_Mdl.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                    detail_Mdl.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                    detail_Mdl.setRate(DmShared.xNull(rsTemp.getString("rate")));
                    detail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                    detail_Mdl.setFreeQty(DmServer.vNull(rsTemp.getString("freeQty")));
                    detail_Mdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                    detail_Mdl.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                    detail_Mdl.setGstUnit(DmShared.xNull(rsTemp.getString("gstUnit")));
                    detail_Mdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                    detail_Mdl.setWarrantyDate(DmShared.dNull(rsTemp.getDate("warrentyDate")));
                    detail_Mdl.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                    detail_Mdl.setGstRate(DmServer.vNull(rsTemp.getString("gstRate")));
                    detail_Mdl.setCgstRate(DmServer.vNull(rsTemp.getString("cgstRate")));
                    detail_Mdl.setSgstRate(DmServer.vNull(rsTemp.getString("sgstRate")));
                    detail_Mdl.setIgstRate(DmServer.vNull(rsTemp.getString("igstRate")));
                    detail_Mdl.setCessRate(DmServer.vNull(rsTemp.getString("cessRate")));
                    detail_Mdl.setStateCessRate(DmServer.vNull(rsTemp.getString("sCessRate")));
                    detail_Mdl.setCessNonAdvolRate(DmServer.vNull(rsTemp.getString("cessNonAdvolRate")));
                    detail_Mdl.setGrossAmount(DmServer.vNull(rsTemp.getString("grossAmount")));
                    detail_Mdl.setDiscountAmount(DmServer.vNull(rsTemp.getString("discountAmount")));
                    detail_Mdl.setTaxableAmount(DmServer.vNull(rsTemp.getString("taxableAmount"), "0.00"));
                    detail_Mdl.setCgstAmount(DmServer.vNull(rsTemp.getString("cgstAmount"), "0.00"));
                    detail_Mdl.setSgstAmount(DmServer.vNull(rsTemp.getString("sgstAmount"), "0.00"));
                    detail_Mdl.setIgstAmount(DmServer.vNull(rsTemp.getString("igstAmount"), "0.00"));
                    detail_Mdl.setCessAmount(DmServer.vNull(rsTemp.getString("cessAmount"), "0.00"));
                    detail_Mdl.setCessNaAmount(DmServer.vNull(rsTemp.getString("cessNaAmount"), "0.00"));
                    detail_Mdl.setStateCessAmount(DmServer.vNull(rsTemp.getString("scessAmount"), "0.00"));
                    detail_Mdl.setStateCessNaAmount(DmServer.vNull(rsTemp.getString("scessAmount"), "0.00"));//<Tbd>
                    detail_Mdl.setOtherCharges(DmServer.vNull(rsTemp.getString("otherCharges"), "0.00"));
                    detail_Mdl.setNetAmount(DmServer.vNull(rsTemp.getString("netAmount"), "0.00"));
                    detail_Mdl.setIsService(DmEnum.DmYesNo.valueOf(DmShared.iNull(rsTemp.getString("isService")).intValue()).getEnumShortName());
                    detail_Mdl.setPropertieDispCsv(DmShared.xNull(rsTemp.getString("propertyDispCsv")));
                    detail_Mdl.setBarCode(DmShared.xNull(rsTemp.getString("barCode")));
                    detail_Mdl.setOrderRefNo(DmShared.xNull(rsTemp.getString("poRefNo")));
                    detail_Mdl.setOriginCountry(DmShared.xNull(rsTemp.getString("gstOriginCountryCode")));
                    detail_Mdl.setItemUSno(DmShared.xNull(rsTemp.getString("itemUSNo")));
                    egst_Mdl.getItemList().add(detail_Mdl);
                }
                rsTemp.close();
                psTemp.close();

                //</editor-fold>
            }
            //<editor-fold defaultstate="collapsed" desc="GET GST RECORD">
            gdm = getGstDocumentDetail(docid, "", cnTemp);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="GET TOKEN">
            eGstToken_Mdl = getToken(cnTemp, dmRss);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="SET MASTER">
            masterEGst_Mdl = new MasterEGst_Mdl();
            masterEGst_Mdl.seteGst_Mdl(egst_Mdl);
            masterEGst_Mdl.setGst_Mdl(gdm);
            masterEGst_Mdl.seteGstToken_Mdl(eGstToken_Mdl);
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
        }
        return masterEGst_Mdl;
    }

    public static String getDateTime(Timestamp entryDateTime) throws Exception {
        String rtnValue = "";
        try {
            rtnValue = getDateTime(entryDateTime, EINV_DATETIME_FORMAT_ACTUAL);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return rtnValue;
    }

    public static String getDateTime(Timestamp entryDateTime, String format) throws Exception {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            if (entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = sdf.format(cal.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return rtnValue;
    }

    private static String convertDate(String date) {
        String dt = "";
        try {
            dt = convertDate(date, "dd/MM/yyyy");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dt;
    }

    private static String convertDate(String date, String pattern) {
        String dt = "";
        try {
            dt = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH).format(DmServer.convertDate(date).toLocalDate());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return dt;
    }

    private static EinvoiceMongo_Mdl getEinvoice_Mongo_Mdl(EGst_Mdl eGst_Mdl, Connection cnTemp) throws Exception {
        EinvoiceMongo_Mdl einvoice_Mongo = null;
        try {
            DmPreparedStatement psTemp = null;
            ResultSet rsTemp = null;
            String sqlQuery = "", menuName = "", moduleName = "";
            //<editor-fold defaultstate="collapsed" desc="GET TRANSCATION DETAILS">
            sqlQuery = "SELECT TOP 1 vt.mnuText as menuName, vt.mnuAttachedInModule as module \n"
                    + "from gstTrans gt \n"
                    + "LEFT JOIN voucher_Type vt on vt.v_type = gt.v_type \n"
                    + "WHERE gt.docid =:docid \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", eGst_Mdl.getDocid());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                menuName = DmShared.xNull(rsTemp.getString("menuName"));
                moduleName = DmShared.xNull(rsTemp.getString("module"));
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>
            einvoice_Mongo = new EinvoiceMongo_Mdl();
            einvoice_Mongo.setMongoLinkId(DmShared.xNull(eGst_Mdl.getMongoLinkId()));
            einvoice_Mongo.setDocid(DmShared.xNull(eGst_Mdl.getDocid()));
            einvoice_Mongo.setRecid(DmShared.xNull(eGst_Mdl.getRecId().getRecIdValue()));
            einvoice_Mongo.setModulename(DmShared.xNull(moduleName));
            einvoice_Mongo.setMenuname(DmShared.xNull(menuName));
            einvoice_Mongo.setAck_no(DmShared.xNull(eGst_Mdl.getEinv_ackNo()));
            einvoice_Mongo.setAck_no_date(DmShared.xNull(eGst_Mdl.getEinv_ackDt()));
            einvoice_Mongo.setIrn(DmShared.xNull(eGst_Mdl.getEinv_IrnNo()));
            einvoice_Mongo.setExtracted_Signed_Invoice_DataJson(DmShared.xNull(eGst_Mdl.getEinv_ExtractedSignedInvoiceDataJson()));
            einvoice_Mongo.setExtracted_Signed_QrCodeJson(DmShared.xNull(eGst_Mdl.getEinv_ExtractedSignedQrCodeJson()));
            einvoice_Mongo.setSigned_e_invoice(DmShared.xNull(eGst_Mdl.getEinv_SignedInvoice()));
            einvoice_Mongo.setSigned_Qr_Code(DmShared.xNull(eGst_Mdl.getEinv_SignedQrCode()));
            einvoice_Mongo.setSigned_Qr_Code_Img(DmShared.xNull(eGst_Mdl.getEinv_SignedQrCodeImage()));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return einvoice_Mongo;
    }

    public static GstDocument_Mdl getGstDocumentDetail(String docid, String vTypeCode, Connection cnTemp) throws Exception {
        GstDocument_Mdl gdm = new GstDocument_Mdl();
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {
            gdm.setDocid(docid);
            if (!DmShared.xNull(docid).trim().equalsIgnoreCase("")) {
                sqlQuery = " SELECT TOP 1\n"
                        + " gt.gstDocType, gt.gstDocNature \n"
                        + " FROM gstTrans gt \n"
                        + " WHERE gt.docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", docid);

            } else {
                if (!DmShared.xNull(vTypeCode).trim().equalsIgnoreCase("")) {
                    sqlQuery = "select \n"
                            + " vt.documentTypeCode as gstDocType, vt.documentNatureCode as gstDocNature \n"
                            + " from voucher_Type vt \n"
                            + " where vt.v_Type = :v_Type \n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("v_Type", DmShared.xNull(vTypeCode));
                } else {
                    throw new Exception("Error in fetch gst document detail!");
                }
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("gstDocType")).trim().equalsIgnoreCase("")) {
                    gdm.setGstDocTypeCode(DmShared.xNull(rsTemp.getString("gstDocType")));
                    gdm.setGstDocTypeName(DmEnum.GstDocumentType.getViaShortName(DmShared.xNull(rsTemp.getString("gstDocType"))).getValue());
                }
                if (!DmShared.xNull(rsTemp.getString("gstDocNature")).trim().equalsIgnoreCase("")) {
                    gdm.setGstDocNatureCode(DmShared.foriegnKey(DmShared.xNull(rsTemp.getString("gstDocNature"))));
                    gdm.setGstDocNatureName(DmShared.foriegnKey(DmEnum.GstDocumentNature.getViaShortName(DmShared.xNull(rsTemp.getString("gstDocNature"))).getValue()));
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return gdm;
    }

    private static String getEInvoiceJson(EGst_Mdl eGstMdl) {
        boolean flagSampleData = false, flagTransportDetail = false, flagRefDetails = false;
        int freeQtyCount = 0;
        E_InvoiceCommon.ReqPlGenIRN reqPlGenIRN = new E_InvoiceCommon.ReqPlGenIRN();
        //<editor-fold defaultstate="collapsed" desc="Testing MDL">
        if (flagSampleData) {

            reqPlGenIRN.Version = "1.1";
            reqPlGenIRN.TranDtls = new E_InvoiceCommon.ReqPlGenIRN.TranDetails();
            reqPlGenIRN.TranDtls.TaxSch = "GST";
            reqPlGenIRN.TranDtls.SupTyp = "B2B";
            reqPlGenIRN.TranDtls.RegRev = "Y";
            reqPlGenIRN.TranDtls.EcmGstin = null;
            reqPlGenIRN.TranDtls.IgstOnIntra = "N";

            reqPlGenIRN.DocDtls = new E_InvoiceCommon.ReqPlGenIRN.DocDetails();
            reqPlGenIRN.DocDtls.Typ = "INV";
            reqPlGenIRN.DocDtls.No = "DOC-NO/dm/ag/3";
            reqPlGenIRN.DocDtls.Dt = "13/10/2021";
            reqPlGenIRN.SellerDtls = new E_InvoiceCommon.ReqPlGenIRN.SellerDetails();
            reqPlGenIRN.SellerDtls.Gstin = "34AACCC1596Q002";
            reqPlGenIRN.SellerDtls.LglNm = "ABC company pvt ltd";
            reqPlGenIRN.SellerDtls.TrdNm = null;
            reqPlGenIRN.SellerDtls.Addr1 = "5th block, kuvempu layout";
            reqPlGenIRN.SellerDtls.Addr2 = null;
            reqPlGenIRN.SellerDtls.Loc = "GANDHINAGAR";
            reqPlGenIRN.SellerDtls.Pin = 605001;
            reqPlGenIRN.SellerDtls.Stcd = "34";
            reqPlGenIRN.SellerDtls.Ph = null;
            reqPlGenIRN.SellerDtls.Em = null;
            reqPlGenIRN.BuyerDtls = new E_InvoiceCommon.ReqPlGenIRN.BuyerDetails();
            reqPlGenIRN.BuyerDtls.Gstin = "29AWGPV7107B1Z1";
            reqPlGenIRN.BuyerDtls.LglNm = "XYZ company pvt ltd";
            reqPlGenIRN.BuyerDtls.TrdNm = null;
            reqPlGenIRN.BuyerDtls.Pos = "12";
            reqPlGenIRN.BuyerDtls.Addr1 = "7th block, kuvempu layout";
            reqPlGenIRN.BuyerDtls.Addr2 = null;
            reqPlGenIRN.BuyerDtls.Loc = "GANDHINAGAR";
            reqPlGenIRN.BuyerDtls.Pin = 562160;
            reqPlGenIRN.BuyerDtls.Stcd = "29";
            reqPlGenIRN.BuyerDtls.Ph = null;
            reqPlGenIRN.BuyerDtls.Em = null;
            reqPlGenIRN.DispDtls = new E_InvoiceCommon.ReqPlGenIRN.DispatchedDetails();
            reqPlGenIRN.DispDtls.Nm = "ABC company pvt ltd";
            reqPlGenIRN.DispDtls.Addr1 = "7th block, kuvempu layout";
            reqPlGenIRN.DispDtls.Addr2 = null;
            reqPlGenIRN.DispDtls.Loc = "Banagalore";
            reqPlGenIRN.DispDtls.Pin = 562160;
            reqPlGenIRN.DispDtls.Stcd = "29";
            reqPlGenIRN.ShipDtls = new E_InvoiceCommon.ReqPlGenIRN.ShippedDetails();
            reqPlGenIRN.ShipDtls.Gstin = null;
            reqPlGenIRN.ShipDtls.LglNm = "CBE company pvt ltd";
            reqPlGenIRN.ShipDtls.TrdNm = null;
            reqPlGenIRN.ShipDtls.Addr1 = "7th block, kuvempu layout";
            reqPlGenIRN.ShipDtls.Addr2 = null;
            reqPlGenIRN.ShipDtls.Loc = "Banagalore";
            reqPlGenIRN.ShipDtls.Pin = 562160;
            reqPlGenIRN.ShipDtls.Stcd = "29";
            reqPlGenIRN.ItemList = new ArrayList<E_InvoiceCommon.ReqPlGenIRN.ItmList>();
            E_InvoiceCommon.ReqPlGenIRN.ItmList itm = new E_InvoiceCommon.ReqPlGenIRN.ItmList();
            itm.SlNo = "1";
            itm.IsServc = "N";
            itm.PrdDesc = "Rice";
            itm.HsnCd = "1001";
            itm.BchDtls = null;
            itm.Qty = 101;
            itm.Unit = "BAG";
            itm.UnitPrice = 10;
            itm.TotAmt = 1010;
            itm.Discount = 10;
            itm.AssAmt = 1000;
            itm.GstRt = 18.00;
            itm.SgstAmt = 0.0;
            itm.IgstAmt = 180;
            itm.CgstAmt = 0.0;
            itm.CesRt = 0;
            itm.CesAmt = 0;
            itm.CesNonAdvlAmt = 0;
            itm.StateCesRt = 0;
            itm.StateCesAmt = 0;
            itm.StateCesNonAdvlAmt = 0;
            itm.OthChrg = 0;
            itm.TotItemVal = 1180;
            itm.AttribDtls = null;
            reqPlGenIRN.ItemList.add(itm);

            if (!DmShared.xNull(eGstMdl.getEway_GenerationMode()).trim().equalsIgnoreCase("")) {
                if (DmEnum.EWAY_GenerationMode.getViaCode(DmShared.iNull(eGstMdl.getEway_GenerationMode()).intValue()).getCode() == DmEnum.EWAY_GenerationMode.E_WAY_GENERATED_WITH_E_INVOICE.getCode()) {
                    reqPlGenIRN.EwbDtls.TransId = "29AAAAA1303P1ZV";
                    reqPlGenIRN.EwbDtls.TransDocNo = "LR12345678";
                    reqPlGenIRN.EwbDtls.TransDocDt = "13/10/2021";
                    reqPlGenIRN.EwbDtls.TransMode = "1";
                    reqPlGenIRN.EwbDtls.TransName = "Malik Transporter";
                    reqPlGenIRN.EwbDtls.VehNo = "UP78CL6587";
                    reqPlGenIRN.EwbDtls.VehType = "O";
                    reqPlGenIRN.EwbDtls.Distance = "";
                }
            }
            reqPlGenIRN.PayDtls = null;
            reqPlGenIRN.RefDtls = null;
            reqPlGenIRN.AddlDocDtls = null;
            reqPlGenIRN.ExpDtls = null;
            reqPlGenIRN.ValDtls = new E_InvoiceCommon.ReqPlGenIRN.ValDetails();
            reqPlGenIRN.ValDtls.AssVal = 1000;
            reqPlGenIRN.ValDtls.CgstVal = 0.0;
            reqPlGenIRN.ValDtls.SgstVal = 0.0;
            reqPlGenIRN.ValDtls.IgstVal = 180;
            reqPlGenIRN.ValDtls.CesVal = 0;
            reqPlGenIRN.ValDtls.StCesVal = 0;
            reqPlGenIRN.ValDtls.RndOffAmt = 0;
            reqPlGenIRN.ValDtls.TotInvVal = 1180;
            reqPlGenIRN.ValDtls.TotInvValFc = 0;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="fill eGST Data">
        if (!flagSampleData) {
            reqPlGenIRN.Version = DmShared.foriegnKey(eGstMdl.getEinv_version());
            //<editor-fold defaultstate="collapsed" desc="Transaction Details">
            reqPlGenIRN.TranDtls = new E_InvoiceCommon.ReqPlGenIRN.TranDetails();
            reqPlGenIRN.TranDtls.TaxSch = DmShared.foriegnKey(eGstMdl.getTaxSchemeName());
            reqPlGenIRN.TranDtls.SupTyp = DmShared.foriegnKey(eGstMdl.getEinvSupplyTypeCode());
            reqPlGenIRN.TranDtls.RegRev = DmShared.foriegnKey(eGstMdl.getReverseCharge());
            reqPlGenIRN.TranDtls.EcmGstin = DmShared.foriegnKey(eGstMdl.geteComGstin());
            reqPlGenIRN.TranDtls.IgstOnIntra = DmShared.foriegnKey(eGstMdl.getIgstOnIntra());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Document Details">
            reqPlGenIRN.DocDtls = new E_InvoiceCommon.ReqPlGenIRN.DocDetails();
            reqPlGenIRN.DocDtls.Typ = DmShared.foriegnKey(eGstMdl.getEinvDocTypeCode());
            reqPlGenIRN.DocDtls.No = DmShared.foriegnKey(eGstMdl.getRecId().getRecIdValue());
            reqPlGenIRN.DocDtls.Dt = DmShared.foriegnKey(convertDate(eGstMdl.getV_Date()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Seller Details">
            reqPlGenIRN.SellerDtls = new E_InvoiceCommon.ReqPlGenIRN.SellerDetails();
            reqPlGenIRN.SellerDtls.Gstin = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getGstin());
            reqPlGenIRN.SellerDtls.LglNm = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getLegalName());
            reqPlGenIRN.SellerDtls.TrdNm = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getTradeName());
            reqPlGenIRN.SellerDtls.Addr1 = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getAddress1());
            reqPlGenIRN.SellerDtls.Addr2 = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getAddress2());
            reqPlGenIRN.SellerDtls.Loc = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getCityName());
            reqPlGenIRN.SellerDtls.Pin = DmShared.iNull(eGstMdl.getConsigner_Mdl().getPinCode()).intValue();
            reqPlGenIRN.SellerDtls.Stcd = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getStateNumericCode());
            reqPlGenIRN.SellerDtls.Ph = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getPhone());
            reqPlGenIRN.SellerDtls.Em = DmShared.foriegnKey(eGstMdl.getConsigner_Mdl().getEmail());
            eGstMdl.setActFromStateNumericCode(DmShared.xNull(eGstMdl.getConsigner_Mdl().getStateNumericCode()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="DispDtls">
            boolean flagDispatchDetail = false;
            String consignerInfo = Gst_Shared.concatPartyInfo(eGstMdl.getConsigner_Mdl().getGstin(), eGstMdl.getConsigner_Mdl().getLegalName(), eGstMdl.getConsigner_Mdl().getAddress1(), eGstMdl.getConsigner_Mdl().getAddress2(), eGstMdl.getConsigner_Mdl().getCityName(), eGstMdl.getConsigner_Mdl().getPinCode(), eGstMdl.getConsigner_Mdl().getStateName(), eGstMdl.getConsigner_Mdl().getCountryName());
            String dispatchFromInfo = Gst_Shared.concatPartyInfo(eGstMdl.getDispatchFrom_Mdl().getGstin(), eGstMdl.getDispatchFrom_Mdl().getLegalName(), eGstMdl.getDispatchFrom_Mdl().getAddress1(), eGstMdl.getDispatchFrom_Mdl().getAddress2(), eGstMdl.getDispatchFrom_Mdl().getCityName(), eGstMdl.getDispatchFrom_Mdl().getPinCode(), eGstMdl.getDispatchFrom_Mdl().getStateName(), eGstMdl.getDispatchFrom_Mdl().getCountryName());
            if (!DmShared.xNull(dispatchFromInfo).equalsIgnoreCase(consignerInfo)) {
                flagDispatchDetail = true;
            }
            if (flagDispatchDetail) {
                reqPlGenIRN.DispDtls = new E_InvoiceCommon.ReqPlGenIRN.DispatchedDetails();
                reqPlGenIRN.DispDtls.Nm = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getLegalName());
                reqPlGenIRN.DispDtls.Addr1 = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getAddress1());
                reqPlGenIRN.DispDtls.Addr2 = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getAddress2());
                reqPlGenIRN.DispDtls.Loc = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getCityName());
                reqPlGenIRN.DispDtls.Pin = DmShared.iNull(eGstMdl.getDispatchFrom_Mdl().getPinCode()).intValue();
                reqPlGenIRN.DispDtls.Stcd = DmShared.foriegnKey(eGstMdl.getDispatchFrom_Mdl().getStateNumericCode());
                eGstMdl.setActFromStateNumericCode(DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getStateNumericCode()));
            } else {
                reqPlGenIRN.DispDtls = null;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="BuyerDtls">
            Party_Mdl buyerBillToMdl = eGstMdl.getBillTo_Mdl().clone();
            reqPlGenIRN.BuyerDtls = new E_InvoiceCommon.ReqPlGenIRN.BuyerDetails();
            reqPlGenIRN.BuyerDtls.Gstin = DmShared.foriegnKey(buyerBillToMdl.getGstin());
            reqPlGenIRN.BuyerDtls.LglNm = DmShared.foriegnKey(buyerBillToMdl.getLegalName());
            reqPlGenIRN.BuyerDtls.TrdNm = DmShared.foriegnKey(buyerBillToMdl.getTradeName());
            reqPlGenIRN.BuyerDtls.Pos = DmShared.foriegnKey(eGstMdl.getPosStateNumericCode());
            reqPlGenIRN.BuyerDtls.Addr1 = DmShared.foriegnKey(buyerBillToMdl.getAddress1());
            reqPlGenIRN.BuyerDtls.Addr2 = DmShared.foriegnKey(buyerBillToMdl.getAddress2());
            reqPlGenIRN.BuyerDtls.Loc = DmShared.foriegnKey(buyerBillToMdl.getCityName());
            reqPlGenIRN.BuyerDtls.Pin = DmShared.iNull(buyerBillToMdl.getPinCode()).intValue();
            reqPlGenIRN.BuyerDtls.Stcd = DmShared.foriegnKey(buyerBillToMdl.getStateNumericCode());
            reqPlGenIRN.BuyerDtls.Ph = DmShared.foriegnKey(buyerBillToMdl.getPhone());
            reqPlGenIRN.BuyerDtls.Em = DmShared.foriegnKey(buyerBillToMdl.getEmail());
            eGstMdl.setActToStateNumericCode(DmShared.xNull(buyerBillToMdl.getStateNumericCode()));
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ShipDtls">
            Party_Mdl shipToMdl = eGstMdl.getBillTo_Mdl().clone();
            boolean flagShipDetail = false;
            String buyerInfo = Gst_Shared.concatPartyInfo(buyerBillToMdl.getGstin(), buyerBillToMdl.getLegalName(), buyerBillToMdl.getAddress1(), buyerBillToMdl.getAddress2(), buyerBillToMdl.getCityName(), buyerBillToMdl.getPinCode(), buyerBillToMdl.getStateName(), buyerBillToMdl.getCountryName());
            String shipToInfo = Gst_Shared.concatPartyInfo(shipToMdl.getGstin(), shipToMdl.getLegalName(), shipToMdl.getAddress1(), shipToMdl.getAddress2(), shipToMdl.getCityName(), shipToMdl.getPinCode(), shipToMdl.getStateName(), shipToMdl.getCountryName());
            if (!DmShared.xNull(buyerInfo).equalsIgnoreCase(shipToInfo)) {
                flagDispatchDetail = true;
            }
            if (flagShipDetail) {
                reqPlGenIRN.ShipDtls = new E_InvoiceCommon.ReqPlGenIRN.ShippedDetails();
                reqPlGenIRN.ShipDtls.Gstin = DmShared.foriegnKey(shipToMdl.getGstin());
                reqPlGenIRN.ShipDtls.LglNm = DmShared.foriegnKey(shipToMdl.getLegalName());
                reqPlGenIRN.ShipDtls.TrdNm = DmShared.foriegnKey(shipToMdl.getTradeName());
                reqPlGenIRN.ShipDtls.Stcd = DmShared.foriegnKey(shipToMdl.getStateNumericCode());
                reqPlGenIRN.ShipDtls.Addr1 = DmShared.foriegnKey(shipToMdl.getAddress1());
                reqPlGenIRN.ShipDtls.Addr2 = DmShared.foriegnKey(shipToMdl.getAddress2());
                reqPlGenIRN.ShipDtls.Loc = DmShared.foriegnKey(shipToMdl.getCityName());
                reqPlGenIRN.ShipDtls.Pin = DmShared.iNull(shipToMdl.getPinCode()).intValue();
                eGstMdl.setActToStateNumericCode(DmShared.xNull(shipToMdl.getStateNumericCode()));

            } else {
                reqPlGenIRN.ShipDtls = null;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Transporter Detail">
            if (!DmShared.xNull(eGstMdl.getEway_GenerationMode()).trim().equalsIgnoreCase("")) {
                if (DmEnum.EWAY_GenerationMode.getViaCode(DmShared.iNull(eGstMdl.getEway_GenerationMode()).intValue()).getCode() == DmEnum.EWAY_GenerationMode.E_WAY_GENERATED_WITH_E_INVOICE.getCode()) {
                    flagTransportDetail = true;
                    reqPlGenIRN.EwbDtls = (new E_InvoiceCommon.ReqPlGenIRN()).new EwbDetails();
                    reqPlGenIRN.EwbDtls.TransId = DmShared.foriegnKey(eGstMdl.getTransporter_Mdl().getTransporterId());
                    reqPlGenIRN.EwbDtls.TransName = DmShared.foriegnKey(eGstMdl.getTransporter_Mdl().getSharedKeyFieldName());
                    reqPlGenIRN.EwbDtls.TransDocNo = DmShared.foriegnKey(eGstMdl.getTransportLrNo());
                    reqPlGenIRN.EwbDtls.TransDocDt = DmShared.foriegnKey(convertDate(eGstMdl.getTransportLrDate()));
                    reqPlGenIRN.EwbDtls.TransMode = DmShared.foriegnKey(eGstMdl.getTransportModeCode());
                    reqPlGenIRN.EwbDtls.VehNo = DmShared.foriegnKey(eGstMdl.getVehicleNo());
                    reqPlGenIRN.EwbDtls.VehType = DmShared.foriegnKey(eGstMdl.getGstVehicleType());
                    reqPlGenIRN.EwbDtls.Distance = DmServer.vNull(eGstMdl.getTransDistance());
                }
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="ItemDetails">
            reqPlGenIRN.ItemList = new ArrayList<E_InvoiceCommon.ReqPlGenIRN.ItmList>();
            for (EGstDetail_Mdl detail_Mdl : eGstMdl.getItemList()) {
                E_InvoiceCommon.ReqPlGenIRN.ItmList itm = new E_InvoiceCommon.ReqPlGenIRN.ItmList();
                itm.SlNo = DmShared.foriegnKey(detail_Mdl.getvSno());
                itm.IsServc = DmShared.foriegnKey(detail_Mdl.getIsService());
                if (DmShared.xNull(detail_Mdl.getItemDesc()).trim().equalsIgnoreCase("")) {
                    itm.PrdDesc = DmShared.foriegnKey(detail_Mdl.getItemName());
                } else {
                    itm.PrdDesc = DmShared.foriegnKey(detail_Mdl.getItemDesc());
                }
                itm.HsnCd = DmShared.foriegnKey(detail_Mdl.getHsn());
                itm.Qty = DmShared.vNull(detail_Mdl.getQuantity()).doubleValue();
                if (DmShared.vNull(detail_Mdl.getFreeQty()).doubleValue() > 0) {
                    itm.FreeQty = "FreeQty" + freeQtyCount++;
                }
                itm.Unit = DmShared.foriegnKey(detail_Mdl.getGstUnit());
                itm.UnitPrice = DmShared.vNull(detail_Mdl.getRate()).doubleValue();
                itm.TotAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getGrossAmount(), "0.00")).doubleValue();
                itm.Discount = DmShared.vNull(DmServer.vNull(detail_Mdl.getDiscountAmount(), "0.00")).doubleValue();
                itm.PreTaxVal = DmShared.vNull(DmServer.vNull(detail_Mdl.getTaxableAmount(), "0.00")).doubleValue();
                itm.AssAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getTaxableAmount(), "0.00")).doubleValue();
                itm.GstRt = DmShared.vNull(detail_Mdl.getGstRate()).doubleValue();//<Tbd>
                itm.SgstAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getSgstAmount(), "0.00")).doubleValue();
                itm.IgstAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getIgstAmount(), "0.00")).doubleValue();
                itm.CesRt = DmShared.vNull(detail_Mdl.getCessRate()).doubleValue();
                itm.CgstAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getCgstAmount(), "0.00")).doubleValue();
                itm.CesAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getCessAmount(), "0.00")).doubleValue();
                itm.CesNonAdvlAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getCessNaAmount(), "0.00")).doubleValue();
                itm.StateCesRt = DmShared.vNull(detail_Mdl.getStateCessRate()).doubleValue();  //<Ag>
                itm.StateCesAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getStateCessAmount(), "0.00")).doubleValue();
                itm.StateCesNonAdvlAmt = DmShared.vNull(DmServer.vNull(detail_Mdl.getStateCessNaAmount(), "0.00")).doubleValue();
                itm.OthChrg = DmShared.vNull(DmServer.vNull(detail_Mdl.getOtherCharges(), "0.00")).doubleValue();
                itm.TotItemVal = DmShared.vNull(DmServer.vNull(detail_Mdl.getNetAmount(), "0.00")).doubleValue();
                //<editor-fold defaultstate="collapsed" desc="Batch Details">
                if (!DmShared.xNull(detail_Mdl.getBatchNo()).trim().equalsIgnoreCase("")) {
                    itm.BchDtls = (new E_InvoiceCommon.ReqPlGenIRN.ItmList()).new BatchDetails();
                    itm.BchDtls.Nm = DmShared.foriegnKey(detail_Mdl.getBatchNo());
                    itm.BchDtls.ExpDt = DmShared.foriegnKey(convertDate(detail_Mdl.getExpiryDate()));
                    itm.BchDtls.WrDt = DmShared.foriegnKey(convertDate(detail_Mdl.getWarrantyDate()));
                } else {
                    itm.BchDtls = null;
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Other Details">
                //<editor-fold defaultstate="collapsed" desc="AttribDtls">
                itm.AttribDtls = null;
                if (!DmShared.xNull(detail_Mdl.getPropertieDispCsv()).trim().equalsIgnoreCase("")) {
                    HashMap<String, String> propertyList = getListFromCsv(detail_Mdl.getPropertieDispCsv());
                    if (propertyList != null) {
                        if (!propertyList.isEmpty()) {
                            itm.AttribDtls = new ArrayList<>();
                            for (String pName : propertyList.keySet()) {
                                if (pName.length() > 2 && propertyList.get(pName).trim().length() > 2) {
                                    E_InvoiceCommon.ReqPlGenIRN.ItmList.AttributeDtls tempPropertyDetails = (new E_InvoiceCommon.ReqPlGenIRN.ItmList()).new AttributeDtls();
                                    tempPropertyDetails.Nm = DmShared.foriegnKey(pName);
                                    tempPropertyDetails.Val = DmShared.foriegnKey(propertyList.get(pName));
                                    itm.AttribDtls.add(tempPropertyDetails);
                                }
                            }
                            if (itm.AttribDtls.isEmpty()) {
                                itm.AttribDtls = null;
                            }
                        }
                    }
                }
                //</editor-fold>
                itm.Barcde = DmShared.foriegnKey(detail_Mdl.getBarCode());
                itm.OrdLineRef = DmShared.foriegnKey(detail_Mdl.getOrderRefNo());
                itm.OrgCntry = DmShared.foriegnKey(detail_Mdl.getOriginCountry());
                itm.PrdSlNo = DmShared.foriegnKey(detail_Mdl.getItemUSno());
                //</editor-fold>                
                reqPlGenIRN.ItemList.add(itm);
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Payee Details">
            if (!DmShared.xNull(eGstMdl.getPayee_Name()).trim().equalsIgnoreCase("")) {
                reqPlGenIRN.PayDtls = new E_InvoiceCommon.ReqPlGenIRN.PayDetails();
                reqPlGenIRN.PayDtls.Nm = DmShared.foriegnKey(eGstMdl.getPayee_Name());
                reqPlGenIRN.PayDtls.AcctDet = DmShared.foriegnKey(eGstMdl.getPayee_AccountNo());
                reqPlGenIRN.PayDtls.CrDay = DmShared.iNull(eGstMdl.getPayee_CreditDays()).intValue();
                reqPlGenIRN.PayDtls.CrTrn = DmShared.foriegnKey(eGstMdl.getPayee_CrTrn());
                reqPlGenIRN.PayDtls.DirDr = DmShared.foriegnKey(eGstMdl.getPayee_DirectDebit());
                reqPlGenIRN.PayDtls.FinInsBr = DmShared.foriegnKey(eGstMdl.getPayee_BranchCode());
                reqPlGenIRN.PayDtls.Mode = DmShared.foriegnKey(eGstMdl.getPayee_ModeOfPayment());
                reqPlGenIRN.PayDtls.PaidAmt = DmShared.vNull(DmServer.vNull(eGstMdl.getPayee_PaidAmount())).doubleValue();
                reqPlGenIRN.PayDtls.PayInstr = DmShared.foriegnKey(eGstMdl.getPayee_PaymentInstruction());
                reqPlGenIRN.PayDtls.PayTerm = DmShared.foriegnKey(eGstMdl.getPayee_PaymentTerms());
                reqPlGenIRN.PayDtls.PaymtDue = DmShared.vNull(DmServer.vNull(eGstMdl.getPayee_PaymentDue())).doubleValue();
            } else {
                reqPlGenIRN.PayDtls = null;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Ref Details">
            if (!DmShared.xNull(eGstMdl.getRefRecId()).trim().equalsIgnoreCase("")) {
                flagRefDetails = true;
                reqPlGenIRN.RefDtls = (new E_InvoiceCommon.ReqPlGenIRN()).new RefDetails();
                reqPlGenIRN.RefDtls.InvRm = DmShared.foriegnKey(eGstMdl.getRefRemark());
                //<editor-fold defaultstate="collapsed" desc="PrecDocDtls">
                reqPlGenIRN.RefDtls.PrecDocDtls = ((new E_InvoiceCommon.ReqPlGenIRN()).new RefDetails()).new PrecDocument();
                reqPlGenIRN.RefDtls.PrecDocDtls.InvNo = DmShared.foriegnKey(eGstMdl.getRefRecId());
                reqPlGenIRN.RefDtls.PrecDocDtls.InvDt = DmShared.foriegnKey(convertDate(eGstMdl.getRefV_Date()));
                reqPlGenIRN.RefDtls.PrecDocDtls.OthRefNo = DmShared.foriegnKey(eGstMdl.getRefDocOtherRef());
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="DocPerdDtls">
                reqPlGenIRN.RefDtls.DocPerdDtls = ((new E_InvoiceCommon.ReqPlGenIRN()).new RefDetails()).new DocPerdDetails();
                reqPlGenIRN.RefDtls.DocPerdDtls.InvStDt = DmShared.foriegnKey(convertDate(eGstMdl.getRefInvDocStartDate()));
                reqPlGenIRN.RefDtls.DocPerdDtls.InvEndDt = DmShared.foriegnKey(convertDate(eGstMdl.getRefInvDocEndDate()));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="getContrDtls">
                if (eGstMdl.getContractDetails() != null) {
                    if (!eGstMdl.getContractDetails().isEmpty()) {
                        reqPlGenIRN.RefDtls.ContrDtls = new ArrayList<E_InvoiceCommon.ReqPlGenIRN.RefDetails.Contract>();
                        for (EinvRefContractDtls_Mdl contract : eGstMdl.getContractDetails()) {
                            E_InvoiceCommon.ReqPlGenIRN.RefDetails.Contract tempContract = ((new E_InvoiceCommon.ReqPlGenIRN()).new RefDetails()).new Contract();
                            tempContract.ContrRefr = DmShared.foriegnKey(contract.getContractNo());
                            tempContract.ExtRefr = DmShared.foriegnKey(contract.getExtraRefNo());
                            tempContract.PORefDt = DmShared.foriegnKey(convertDate(contract.getPoRefDate()));
                            tempContract.PORefr = DmShared.foriegnKey(contract.getPoRefNo());
                            tempContract.ProjRefr = DmShared.foriegnKey(contract.getProjectRefNo());
                            tempContract.RecAdvDt = DmShared.foriegnKey(convertDate(contract.getReceiptAdviceDate()));
                            tempContract.RecAdvRefr = DmShared.foriegnKey(contract.getReceiptAdviceNo());
                            tempContract.TendRefr = DmShared.foriegnKey(contract.getTenderRefNo());
                            reqPlGenIRN.RefDtls.ContrDtls.add(tempContract);
                        }
                    }
                }
                //</editor-fold>
            } else {
                reqPlGenIRN.RefDtls = null;
            }

            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Additional document Details">
            if (eGstMdl.getAddDocList() != null) {
                if (!eGstMdl.getAddDocList().isEmpty()) {
                    reqPlGenIRN.AddlDocDtls = new ArrayList<E_InvoiceCommon.ReqPlGenIRN.AddlDocument>();
                    for (EinvAdditionalDocDtls_Mdl eGstAddDoc : eGstMdl.getAddDocList()) {
                        E_InvoiceCommon.ReqPlGenIRN.AddlDocument addDocs = (new E_InvoiceCommon.ReqPlGenIRN()).new AddlDocument();
                        addDocs.Url = DmShared.foriegnKey(eGstAddDoc.getDocumentUrl());
                        addDocs.Docs = DmShared.foriegnKey(eGstAddDoc.getDocument());
                        addDocs.Info = DmShared.foriegnKey(eGstAddDoc.getAdditionalInfoOfDocument());
                        reqPlGenIRN.AddlDocDtls.add(addDocs);
                    }
                } else {
                    reqPlGenIRN.AddlDocDtls = null;
                }
            } else {
                reqPlGenIRN.AddlDocDtls = null;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Export details">
            if (!DmShared.xNull(eGstMdl.getShippingBillNo()).trim().equalsIgnoreCase("")) {
                reqPlGenIRN.ExpDtls = new E_InvoiceCommon.ReqPlGenIRN.ExpDetails();
                reqPlGenIRN.ExpDtls.ShipBNo = DmShared.foriegnKey(eGstMdl.getShippingBillNo());
                reqPlGenIRN.ExpDtls.ShipBDt = DmShared.foriegnKey(convertDate(eGstMdl.getShippingBillDate()));
                reqPlGenIRN.ExpDtls.RefClm = DmShared.foriegnKey(DmShared.iNull(eGstMdl.getRefundClaim()).intValue() == 1 ? "Y" : "N");
                reqPlGenIRN.ExpDtls.CntCode = DmShared.foriegnKey(eGstMdl.getBillTo_Mdl().getGstCountryCode());
                reqPlGenIRN.ExpDtls.Port = DmShared.foriegnKey(eGstMdl.getPortCode());
                reqPlGenIRN.ExpDtls.ForCur = DmShared.foriegnKey(eGstMdl.getCurrencyCode());
                reqPlGenIRN.ExpDtls.InvForCur = DmShared.vNull(eGstMdl.getTotalInvAmtInFc());
            } else {
                reqPlGenIRN.ExpDtls = null;
            }
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Tax Value">
            reqPlGenIRN.ValDtls = new E_InvoiceCommon.ReqPlGenIRN.ValDetails();
            reqPlGenIRN.ValDtls.AssVal = DmShared.vNull(DmServer.vNull(eGstMdl.getTaxableValue(), "0.00"));
            reqPlGenIRN.ValDtls.CgstVal = DmShared.vNull(DmServer.vNull(eGstMdl.getCgstValue(), "0.00"));
            reqPlGenIRN.ValDtls.SgstVal = DmShared.vNull(DmServer.vNull(eGstMdl.getSgstValue(), "0.00"));
            reqPlGenIRN.ValDtls.IgstVal = DmShared.vNull(DmServer.vNull(eGstMdl.getIgstValue(), "0.00"));
            reqPlGenIRN.ValDtls.CesVal = DmShared.vNull(DmServer.vNull(eGstMdl.getCessValue(), "0.00"));
            reqPlGenIRN.ValDtls.StCesVal = DmShared.vNull(DmServer.vNull(eGstMdl.getStateCessAmount(), "0.00"));
            reqPlGenIRN.ValDtls.RndOffAmt = DmShared.vNull(DmServer.vNull(eGstMdl.getRoundOfAmount(), "0.00"));
            reqPlGenIRN.ValDtls.OthChrg = DmShared.vNull(DmServer.vNull(eGstMdl.getOtherAmount(), "0.00"));
            reqPlGenIRN.ValDtls.Discount = DmShared.vNull(DmServer.vNull(eGstMdl.getDiscountAmount(), "0.00"));
            reqPlGenIRN.ValDtls.TotInvVal = DmShared.vNull(DmServer.vNull(eGstMdl.getNetInvAmount(), "0.00"));
            reqPlGenIRN.ValDtls.TotInvValFc = DmShared.vNull(DmServer.vNull(eGstMdl.getTotalInvAmtInFc(), "0.00"));
            //</editor-fold>
        }
        //</editor-fold>

        String gsonStr = new Gson().toJson(reqPlGenIRN);
        if (flagTransportDetail) {
            gsonStr = gsonStr.replace("\"Distance\":\"" + DmServer.vNull(eGstMdl.getTransDistance()) + "\"", "\"Distance\":" + DmServer.vNull(eGstMdl.getTransDistance()));
        }
        if (flagRefDetails) {
            gsonStr = gsonStr.replace("\"PrecDocDtls\":{\"InvNo\":\"" + DmShared.foriegnKey(eGstMdl.getRefRecId()) + "\",\"InvDt\":\"" + DmShared.foriegnKey(convertDate(eGstMdl.getRefV_Date())) + "\",\"OthRefNo\":\"" + DmShared.foriegnKey(eGstMdl.getRefDocOtherRef()) + "\"}", "\"PrecDocDtls\":[{\"InvNo\":\"" + DmShared.foriegnKey(eGstMdl.getRefRecId()) + "\",\"InvDt\":\"" + DmShared.foriegnKey(convertDate(eGstMdl.getRefV_Date())) + "\",\"OthRefNo\":\"" + DmShared.foriegnKey(eGstMdl.getRefDocOtherRef()) + "\"}]");
        }
        if (freeQtyCount > 0) {
            freeQtyCount = 0;
            for (EGstDetail_Mdl detail_Mdl : eGstMdl.getItemList()) {
                if (DmShared.vNull(detail_Mdl.getFreeQty()).doubleValue() > 0) {
                    gsonStr = gsonStr.replace("\"FreeQty\":\"" + "FreeQty" + freeQtyCount++ + "\"", "\"FreeQty\":" + DmShared.vNull(detail_Mdl.getFreeQty()).doubleValue());
                }
            }
        }
        return gsonStr;
    }

    private static HashMap<String, String> getListFromCsv(String strCsv) {
        HashMap<String, String> listHmap = new HashMap<String, String>();
        for (String propertie : strCsv.split(";")) {
            String[] str = propertie.split(":");
            listHmap.put(str[0], str[1]);
        }
        return listHmap;
    }

    private static String getEWayJson(EGst_Mdl eGstMdl) {
        boolean flagSampleData = false;
        EWBJson.ReqGenEwbPl ewbGen = new EWBJson.ReqGenEwbPl();
        //<editor-fold defaultstate="collapsed" desc="Testing MDL">
        if (flagSampleData) {
            ewbGen.supplyType = "O";
            ewbGen.subSupplyType = "1";
            ewbGen.subSupplyDesc = "Supply";
            ewbGen.docType = "INV";
            ewbGen.docNo = "SD19Y-DATAMAN/21";
            ewbGen.docDate = "21/09/2021";
            ewbGen.fromGstin = "34AACCC1596Q002";
            ewbGen.fromTrdName = "ANKIT PULPS \\u0026 BOARDS PRIVATE LIMITED";
            ewbGen.fromAddr1 = "39/2 BHILGAON";
            ewbGen.fromAddr2 = "KAMPTEE ROAD";
            ewbGen.fromPlace = "NAV";
            ewbGen.fromPincode = 605001;
            ewbGen.fromStateCode = 34;
            ewbGen.actFromStateCode = 34;
            ewbGen.toGstin = "09AAACD5213H1ZY";
            ewbGen.toTrdName = "sthuthya";
            ewbGen.toAddr1 = "Shree Nilaya";
            ewbGen.toAddr2 = "Dasarahosahalli";
            ewbGen.toPlace = "Beml Nagar";
            ewbGen.toPincode = 208005;
            ewbGen.toStateCode = 9;
            ewbGen.actToStateCode = 9;
            ewbGen.transactionType = 2;
            ewbGen.dispatchFromGSTIN = "";
            ewbGen.dispatchFromTradeName = "ABC Traders";
            ewbGen.shipToGSTIN = "09AAACD5213H1ZY";
            ewbGen.shipToTradeName = "XYZ Traders";
            ewbGen.otherValue = 0.0;
            ewbGen.totalValue = 0.0;
            ewbGen.cgstValue = 0.0;
            ewbGen.sgstValue = 0.0;
            ewbGen.igstValue = 0.0;
            ewbGen.cessValue = 0.0;
            ewbGen.cessNonAdvolValue = 0.0;
            ewbGen.transporterId = "";
            ewbGen.transporterName = "Nagpur Bombay Roadlines , Pune";
            ewbGen.transDocNo = "";
            ewbGen.totInvValue = 0.0;
            ewbGen.transMode = "1"; //1
            ewbGen.transDistance = "0";
            ewbGen.transDocDate = "";
            ewbGen.vehicleNo = "PVC1234"; //PVC1234
            ewbGen.vehicleType = "R";//R
            ewbGen.itemList = new ArrayList<EWBJson.ReqGenEwbPl.ItemList>();

            EWBJson.ReqGenEwbPl.ItemList item = new EWBJson.ReqGenEwbPl.ItemList();

            item.productName = "MICCEL-103 BP Domestic(SM)";
            item.productDesc = "MICCEL-103 BP Domestic(SM)";
            item.hsnCode = 7006;
            item.quantity = 1.0;
            item.qtyUnit = "KGS";
            item.cgstRate = 0.0;
            item.sgstRate = 0.0;
            item.igstRate = 0.0;
            item.cessRate = 0.0;
            item.cessNonAdvol = 0.0;
            item.taxableAmount = 0.0;
            ewbGen.itemList.add(item);

        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="fill eGST Data">
        if (!flagSampleData) {
            ewbGen.supplyType = DmShared.xNull(eGstMdl.getEwaySupplyTypeCode());
            ewbGen.subSupplyType = DmShared.xNull(eGstMdl.getEwaySubSupplyTypeCode());
            ewbGen.subSupplyDesc = DmShared.xNull(eGstMdl.getEwaySubSupplyDesc());
            ewbGen.docType = DmShared.xNull(eGstMdl.getEwayDocTypeCode());
            ewbGen.docNo = DmShared.xNull(eGstMdl.getRecId().getRecIdValue());
            ewbGen.docDate = DmShared.xNull(convertDate(eGstMdl.getV_Date()));
            ewbGen.transactionType = DmShared.iNull(eGstMdl.getEwayTransTypeCode()).intValue();
            //<editor-fold defaultstate="collapsed" desc="Consigner & Dispatcher">
            ewbGen.fromGstin = DmShared.xNull(eGstMdl.getConsigner_Mdl().getGstin());
            ewbGen.fromTrdName = DmShared.xNull(eGstMdl.getConsigner_Mdl().getTradeName());
            ewbGen.fromStateCode = DmShared.iNull(eGstMdl.getConsigner_Mdl().getStateNumericCode()).intValue();
            ewbGen.fromAddr1 = DmShared.xNull(eGstMdl.getConsigner_Mdl().getAddress1());
            ewbGen.fromAddr2 = DmShared.xNull(eGstMdl.getConsigner_Mdl().getAddress2());
            ewbGen.fromPlace = DmShared.xNull(eGstMdl.getConsigner_Mdl().getCityName());
            ewbGen.fromPincode = DmShared.iNull(eGstMdl.getConsigner_Mdl().getPinCode()).intValue();
            ewbGen.actFromStateCode = DmShared.iNull(eGstMdl.getConsigner_Mdl().getStateNumericCode()).intValue();
            ewbGen.dispatchFromGSTIN = DmShared.xNull(eGstMdl.getConsigner_Mdl().getGstin());
            ewbGen.dispatchFromTradeName = DmShared.xNull(eGstMdl.getConsigner_Mdl().getTradeName());
            //******************************************************************************************************//
            String consignerInfo = Gst_Shared.concatPartyInfo(eGstMdl.getConsigner_Mdl().getGstin(), eGstMdl.getConsigner_Mdl().getLegalName(), eGstMdl.getConsigner_Mdl().getAddress1(), eGstMdl.getConsigner_Mdl().getAddress2(), eGstMdl.getConsigner_Mdl().getCityName(), eGstMdl.getConsigner_Mdl().getPinCode(), eGstMdl.getConsigner_Mdl().getStateName(), eGstMdl.getConsigner_Mdl().getCountryName()), dispatcherInfo = Gst_Shared.concatPartyInfo(eGstMdl.getDispatchFrom_Mdl().getGstin(), eGstMdl.getDispatchFrom_Mdl().getLegalName(), eGstMdl.getDispatchFrom_Mdl().getAddress1(), eGstMdl.getDispatchFrom_Mdl().getAddress2(), eGstMdl.getDispatchFrom_Mdl().getCityName(), eGstMdl.getDispatchFrom_Mdl().getPinCode(), eGstMdl.getDispatchFrom_Mdl().getStateName(), eGstMdl.getDispatchFrom_Mdl().getCountryName());
            if (!DmShared.xNull(dispatcherInfo).trim().equalsIgnoreCase("")) {
                ewbGen.dispatchFromGSTIN = DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getGstin());
                ewbGen.dispatchFromTradeName = DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getTradeName());
                if (!DmShared.xNull(consignerInfo).equalsIgnoreCase(dispatcherInfo)) {
                    ewbGen.fromAddr1 = DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getAddress1());
                    ewbGen.fromAddr2 = DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getAddress2());
                    ewbGen.fromPlace = DmShared.xNull(eGstMdl.getDispatchFrom_Mdl().getCityName());
                    ewbGen.fromPincode = DmShared.iNull(eGstMdl.getDispatchFrom_Mdl().getPinCode()).intValue();
                    ewbGen.actFromStateCode = DmShared.iNull(eGstMdl.getDispatchFrom_Mdl().getStateNumericCode()).intValue();
                }
            }
            eGstMdl.setActFromStateNumericCode(DmShared.iNull(ewbGen.actFromStateCode).toString());
            //******************************************************************************************************//
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Bill To & Ship To">
            ewbGen.toGstin = DmShared.xNull(eGstMdl.getBillTo_Mdl().getGstin());
            ewbGen.toTrdName = DmShared.xNull(eGstMdl.getBillTo_Mdl().getTradeName());
            ewbGen.toStateCode = DmShared.iNull(eGstMdl.getBillTo_Mdl().getStateNumericCode()).intValue();
            ewbGen.toAddr1 = DmShared.xNull(eGstMdl.getBillTo_Mdl().getAddress1());
            ewbGen.toAddr2 = DmShared.xNull(eGstMdl.getBillTo_Mdl().getAddress2());
            ewbGen.toPlace = DmShared.xNull(eGstMdl.getBillTo_Mdl().getCityName());
            ewbGen.toPincode = DmShared.iNull(eGstMdl.getBillTo_Mdl().getPinCode()).intValue();
            ewbGen.actToStateCode = DmShared.iNull(eGstMdl.getBillTo_Mdl().getStateNumericCode()).intValue();
            ewbGen.shipToGSTIN = DmShared.xNull(eGstMdl.getBillTo_Mdl().getGstin());
            ewbGen.shipToTradeName = DmShared.xNull(eGstMdl.getBillTo_Mdl().getTradeName());
            //******************************************************************************************************//
            String billToInfo = Gst_Shared.concatPartyInfo(eGstMdl.getBillTo_Mdl().getGstin(), eGstMdl.getBillTo_Mdl().getLegalName(), eGstMdl.getBillTo_Mdl().getAddress1(), eGstMdl.getBillTo_Mdl().getAddress2(), eGstMdl.getBillTo_Mdl().getCityName(), eGstMdl.getBillTo_Mdl().getPinCode(), eGstMdl.getBillTo_Mdl().getStateName(), eGstMdl.getBillTo_Mdl().getCountryName()), shipToInfo = Gst_Shared.concatPartyInfo(eGstMdl.getShipTo_Mdl().getGstin(), eGstMdl.getShipTo_Mdl().getLegalName(), eGstMdl.getShipTo_Mdl().getAddress1(), eGstMdl.getShipTo_Mdl().getAddress2(), eGstMdl.getShipTo_Mdl().getCityName(), eGstMdl.getShipTo_Mdl().getPinCode(), eGstMdl.getShipTo_Mdl().getStateName(), eGstMdl.getShipTo_Mdl().getCountryName());
            if (!DmShared.xNull(shipToInfo).trim().equalsIgnoreCase("")) {
                ewbGen.shipToGSTIN = DmShared.xNull(eGstMdl.getShipTo_Mdl().getGstin());
                ewbGen.shipToTradeName = DmShared.xNull(eGstMdl.getShipTo_Mdl().getTradeName());
                if (!DmShared.xNull(billToInfo).equalsIgnoreCase(shipToInfo)) {
                    ewbGen.toAddr1 = DmShared.xNull(eGstMdl.getShipTo_Mdl().getAddress1());
                    ewbGen.toAddr2 = DmShared.xNull(eGstMdl.getShipTo_Mdl().getAddress2());
                    ewbGen.toPlace = DmShared.xNull(eGstMdl.getShipTo_Mdl().getCityName());
                    ewbGen.toPincode = DmShared.iNull(eGstMdl.getShipTo_Mdl().getPinCode()).intValue();
                    ewbGen.actToStateCode = DmShared.iNull(eGstMdl.getShipTo_Mdl().getStateNumericCode()).intValue();
                }
            }
            eGstMdl.setActToStateNumericCode(DmShared.iNull(ewbGen.actToStateCode).toString());
            //****************************************************************************************************//
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Transporter Details">
            ewbGen.transporterId = DmShared.xNull(eGstMdl.getTransporter_Mdl().getTransporterId());
            ewbGen.transporterName = DmShared.xNull(eGstMdl.getTransporter_Mdl().getSharedKeyFieldName());
            ewbGen.transDocNo = DmShared.xNull(eGstMdl.getTransportLrNo());
            if (!DmShared.dNull(eGstMdl.getTransportLrDate()).equalsIgnoreCase("")) {
                ewbGen.transDocDate = DmShared.xNull(convertDate(eGstMdl.getTransportLrDate()));
            }
            ewbGen.transMode = DmShared.xNull(eGstMdl.getTransportModeCode());
            ewbGen.transDistance = DmShared.xNull(String.valueOf(DmServer.vNull(eGstMdl.getTransDistance())));
            ewbGen.vehicleNo = DmShared.xNull(eGstMdl.getVehicleNo());
            ewbGen.vehicleType = DmShared.xNull(eGstMdl.getGstVehicleType());
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="item Tax Details">
            ewbGen.otherValue = DmShared.vNull(DmShared.vNull(eGstMdl.getOtherAmount()).doubleValue() - DmShared.vNull(eGstMdl.getDiscountAmount()).doubleValue()).doubleValue();
            ewbGen.totalValue = DmShared.vNull(eGstMdl.getTaxableValue()).doubleValue();
            ewbGen.cgstValue = DmShared.vNull(eGstMdl.getCgstValue()).doubleValue();
            ewbGen.sgstValue = DmShared.vNull(eGstMdl.getSgstValue()).doubleValue();
            ewbGen.igstValue = DmShared.vNull(eGstMdl.getIgstValue()).doubleValue();
            ewbGen.cessValue = DmShared.vNull(eGstMdl.getCessValue()).doubleValue();
            ewbGen.cessNonAdvolValue = DmShared.vNull(eGstMdl.getCessNonAdvolValue()).doubleValue();
            ewbGen.totInvValue = DmShared.vNull(eGstMdl.getNetInvAmount()).doubleValue();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Item Details">
            ewbGen.itemList = new ArrayList<EWBJson.ReqGenEwbPl.ItemList>();
            List<EWBJson.ReqGenEwbPl.ItemList> tempItemList = new ArrayList<EWBJson.ReqGenEwbPl.ItemList>();
            boolean isGoodsItem = false;
            for (EGstDetail_Mdl detail_Mdl : eGstMdl.getItemList()) {
                if (!DmShared.xNull(detail_Mdl.getIsService()).equalsIgnoreCase(DmEnum.DmYesNo.YES.getEnumShortName())) {
                    isGoodsItem = true;
                }
                EWBJson.ReqGenEwbPl.ItemList item = new EWBJson.ReqGenEwbPl.ItemList();
                item.productName = DmShared.xNull(detail_Mdl.getItemName());
                item.productDesc = DmShared.xNull(detail_Mdl.getItemDesc());
                item.hsnCode = DmShared.iNull(detail_Mdl.getHsn()).intValue();
                item.quantity = DmShared.vNull(detail_Mdl.getQuantity()).doubleValue();
                item.qtyUnit = DmShared.xNull(detail_Mdl.getGstUnit());
                item.cgstRate = DmShared.vNull(detail_Mdl.getCgstRate()).doubleValue();
                item.sgstRate = DmShared.vNull(detail_Mdl.getSgstRate()).doubleValue();
                item.igstRate = DmShared.vNull(detail_Mdl.getIgstRate()).doubleValue();
                item.cessRate = DmShared.vNull(detail_Mdl.getCessRate()).doubleValue();
                item.cessNonAdvol = DmShared.vNull(detail_Mdl.getCessNonAdvolRate()).doubleValue();
                item.taxableAmount = DmShared.vNull(detail_Mdl.getTaxableAmount()).doubleValue();
                tempItemList.add(item);
            }
            if (isGoodsItem) {
                ewbGen.itemList.addAll(tempItemList);
            }
            //</editor-fold>
        }
        //</editor-fold>

        return new Gson().toJson(ewbGen);
    }

    public static void updateGstDocument(GstDocument_Mdl prevGstDoc_Mdl, GstDocument_Mdl currentGstDoc_Mdl, int actionCode, Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", recid = "", description = "", shortName = "", menuName = "", module = "";
        try {
            //<editor-fold defaultstate="collapsed" desc="GET TRANSCATION DETAILS">
            sqlQuery = "SELECT TOP 1 gt.recid, vt.description, vt.short_Name \n"
                    + ", vt.mnuText as menuName, vt.mnuAttachedInModule as module \n"
                    + "from gstTrans gt \n"
                    + "LEFT JOIN voucher_Type vt on vt.v_type = gt.v_type \n"
                    + "WHERE gt.docid =:docid \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", currentGstDoc_Mdl.getDocid());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                recid = DmShared.xNull(rsTemp.getString("recid"));
                description = DmShared.xNull(rsTemp.getString("description"));
                shortName = DmShared.xNull(rsTemp.getString("short_Name"));
                menuName = DmShared.xNull(rsTemp.getString("menuName"));
                module = DmShared.xNull(rsTemp.getString("module"));
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="UPDATE GST DOCUMENT">
            sqlQuery = "update "
                    + " gstTrans set gstDocType=:gstDocType , "
                    + " gstDocNature=:gstDocNature "
                    + " where docId=:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", currentGstDoc_Mdl.getDocid());
            psTemp.setString("gstDocType", DmShared.foriegnKey(currentGstDoc_Mdl.getGstDocTypeCode()));
            psTemp.setString("gstDocNature", DmShared.foriegnKey(currentGstDoc_Mdl.getGstDocNatureCode()));
            psTemp.executeUpdate();
            psTemp.close();
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="UPDATE GST REFERNECE DETAIL">
            updateRefGstDocument(currentGstDoc_Mdl.getDocid(), currentGstDoc_Mdl.getGstDocTypeCode(), currentGstDoc_Mdl.getGstDocNatureCode(), cnTemp);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="SaveLog">
            //==============================<< Log Save >>====================================
            ModifyReason_Mdl modifyReasonMdl = new ModifyReason_Mdl();
            modifyReasonMdl.setMode(DmEnum.EGST_ACTION.getViaCode(actionCode).getTransMode());
            modifyReasonMdl.setSharedKeyFieldCode(DmShared.xNull(DmServer.getCodeViaManualCode(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName(), "name", "modifyReasonMast", "code", cnTemp)));
            modifyReasonMdl.setSharedKeyFieldName(DmShared.xNull(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName()));
            ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
            modifyData_Mdl.setKeyId_1(DmShared.xNull(currentGstDoc_Mdl.getDocid()));
            modifyData_Mdl.setKeyName_1(DmShared.xNull(recid));
            modifyData_Mdl.setKeyDescription_1("");
            modifyData_Mdl.setKeyDescription_2("");
            modifyData_Mdl.setModuleName(DmShared.xNull(module));
            modifyData_Mdl.setMenuName(DmShared.xNull(menuName));
            modifyData_Mdl.setModelClassName(GstDocument_Mdl.class.getName());
            modifyData_Mdl.setVrTypeDescription(DmShared.xNull(description));
            modifyData_Mdl.setVrTypeSName(DmShared.xNull(shortName));
            modifyData_Mdl.setTableName("gstTrans");
            modifyData_Mdl.setOldData(DmServer.convertToJson(prevGstDoc_Mdl, new TypeToken<GstDocument_Mdl>() {
            }));
            modifyData_Mdl.setNewData(DmServer.convertToJson(currentGstDoc_Mdl, new TypeToken<GstDocument_Mdl>() {
            }));
            ModifyReason_Server.saveNew(modifyData_Mdl, modifyReasonMdl, dmRss);
            //</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
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

    }

    private static void updateRefGstDocument(String docId, String gstDocumentType, String gstDocumentNature, Connection cnTemp) throws Exception {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        try {

            //<editor-fold defaultstate="collapsed" desc="UPDATE GST refernece detail">
            if (!DmShared.xNull(docId).trim().equalsIgnoreCase("")) {
                sqlQuery = " UPDATE gstTrans SET \n"
                        + " refGstDocType =:gstDocType, \n"
                        + " refGstDocNature =:gstDocNature \n"
                        + " WHERE refDocid = :docid ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(docId));
                psTemp.setString("gstDocType", DmShared.foriegnKey(gstDocumentType));
                psTemp.setString("gstDocNature", DmShared.foriegnKey(gstDocumentNature));
                psTemp.executeUpdate();
                psTemp.close();
            }
            //</editor-fold>

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (psTemp != null) {
                psTemp.close();
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="EWay Bill Printing">
    public static KeyFieldExtended printEWayBill(String eWayBillNo, int actionCode, Connection cnTemp, DmRSSPrivate dmRss) throws Exception {
        KeyFieldExtended keyField = new KeyFieldExtended();
        try {
            loadEgstParamDetails(cnTemp, dmRss);
            String jsonEgstDetail = apiCall.getEWayBillDetail(eGSTParam_Mdl, eGstToken_Mdl, eWayBillNo);
            jsonEgstDetail = rectifyEwayJson(eWayBillNo, jsonEgstDetail, cnTemp);
            byte[] file = apiCall.printEWayBill(eGSTParam_Mdl, eGstToken_Mdl, jsonEgstDetail, actionCode);
            String directoryPath = DmServer.Path.sessions + File.separatorChar + DmRequestBuilder.getSessionId();
            String serverFileName = eWayBillNo + ".pdf";
            Gst_Server.createFile(file, directoryPath, serverFileName);
            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return keyField;
    }

    private static String rectifyEwayJson(String eWayBillNo, String jsonEgstDetail, Connection cnTemp) {
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        boolean flagInsertManualShipTo_Gstin_LegalName = false;
        try {
            if (!DmShared.xNull(eWayBillNo).trim().equalsIgnoreCase("") && !DmShared.xNull(jsonEgstDetail).trim().equalsIgnoreCase("")) {
                //<editor-fold defaultstate="collapsed" desc="rectifyEwayJson">
                sqlQuery = " SELECT eg.eway_subSupplyType,eg.eway_subSupplyDesc"
                        + "  , eg.shipTo_Gstin, eg.shipTo_LegalName, eg.eway_TransactionType \n"
                        + "  FROM eGST AS eg \n"
                        + "  WHERE eg.eway_BillNo = :ewbNo ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("ewbNo", DmShared.xNull(eWayBillNo));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    JsonObject jsonObject = new JsonParser().parse(jsonEgstDetail).getAsJsonObject();
                    boolean isChanged = false;
                    if (DmShared.iNull(rsTemp.getString("eway_subSupplyType")).intValue() == DmEnum.Eway_SubSupplyType.OTHERS.getCode()) {
                        String eway_subSupplyDesc = DmShared.xNull(rsTemp.getString("eway_subSupplyDesc"));
                        if (!jsonObject.has("subSupplyDesc")) {
                            //=======================================================================================================================================//                        
                            //========== //Note: - 10-Apr-2022:  At Present Taxpro if EWay SubSupply Type = "OTHERS"  then it is not printing its DESCRIPTON ========//
                            //========== To Manage this we will manually change the JSON and insert SUB-SUPPLY DESCRIPTON. ==========================================//
                            //========================================================================================================================================//
                            jsonObject.addProperty("subSupplyDesc", DmShared.xNull(eway_subSupplyDesc));
                            isChanged = true;
                            //========================================================================================================================================//
                        }
                    }

                    if (DmShared.iNull(rsTemp.getString("eway_TransactionType")).intValue() == DmEnum.Eway_TransactionType.Bill_To_Ship_To.getCode()) {
                        //========================================================================================================================================//                        
                        //========== //Note: - 10-Apr-2022:  At Present Taxpro is not printing ship to GSTIN, Legal Name in eWay Report. =========================//
                        //========== To Manage this we will manually change the JSON and insert Ship To GSTIN and his Legal Name. ================================//
                        //========================================================================================================================================//
                        flagInsertManualShipTo_Gstin_LegalName = true;
                        //========================================================================================================================================//                        
                    }
                    if (flagInsertManualShipTo_Gstin_LegalName) {
                        if (jsonObject.has("toAddr1")) {
                            String shipToGsTin = DmShared.xNull(rsTemp.getString("shipTo_Gstin")),
                                    shipToLegalName = DmShared.xNull(rsTemp.getString("shipTo_LegalName")),
                                    shipToAdd1 = jsonObject.get("toAddr1").toString().replace("\"", ""), resultStr = "";
                            if (!DmShared.xNull(shipToGsTin).trim().equalsIgnoreCase("")
                                    && !DmShared.xNull(shipToGsTin).trim().equalsIgnoreCase("URP")) {
                                resultStr += "GSTIN : " + shipToGsTin + "\n";
                            }
                            if (!DmShared.xNull(shipToLegalName).trim().equalsIgnoreCase("")) {
                                resultStr += shipToLegalName + "\n";
                            }
                            resultStr += shipToAdd1;
                            if (!DmShared.xNull(resultStr).trim().equalsIgnoreCase("")) {
                                jsonObject.remove("toAddr1");
                                jsonObject.addProperty("toAddr1", resultStr);
                                isChanged = true;
                            }
                        }
                    }
                    if (isChanged) {
                        jsonEgstDetail = jsonObject.toString();
                    }
                }
                rsTemp.close();
                psTemp.close();
                //</editor-fold>
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonEgstDetail;
    }
//</editor-fold>

    private static void createFile(byte[] byteArray, String directoryPath, String serverFileName) throws Exception {
        try {
            String fullFilePath = directoryPath + File.separatorChar + serverFileName;
            new File(directoryPath).mkdir();
            File file = new File(fullFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            os.write(byteArray);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void saveEGstRecord(EGst_Mdl previousEgst_Mdl, EGst_Mdl currentEgst_Mdl, int actionCode, Connection cnTemp, DmRSSPrivate dmRss) throws Exception {

        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        try {

            boolean flagSaveRecord = true;

            if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode()) {
                sqlQuery = "SELECT eg.einv_IrnNo \n"
                        + "FROM eGst eg \n"
                        + "WHERE docid = :docid \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.xNull(currentEgst_Mdl.getDocid()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.xNull(rsTemp.getString("einv_IrnNo")).trim().equalsIgnoreCase("")) {
                        flagSaveRecord = false;
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            deleteEGstRecord(DmShared.xNull(currentEgst_Mdl.getDocid()), cnTemp);

            if (flagSaveRecord) {
                //<editor-fold defaultstate="collapsed" desc="Insert Egst">

                sqlQuery = " INSERT INTO eGst ("
                        + "   docid, v_type, v_Prefix, site_Code, v_date, taxScheme, eComGstin \n"
                        + " , recID, recIdPrefix, recIdSerial, isIgstOnIntra \n"
                        + " , gstType, isRCM, posStateNumericCode, posStateName, partyType, locationCode \n"
                        + " , seller_SiteCode, seller_Name, seller_Gstin, seller_GstinDate \n"
                        + " , seller_TradeName, seller_LegalName, seller_Add1, seller_Add2 \n"
                        + " , seller_Pin, actFrom_StateNumericCode, seller_StateName, seller_StateNumericCode \n"
                        + " , seller_CityCode, seller_CityName, seller_Mobile, seller_Email, seller_GstCountryCode, seller_CountryName \n"
                        + " , buyer_AcCode,buyer_SiteCode, buyer_Name, buyer_Gstin, buyer_GstinDate"
                        + " , buyer_LegalName, buyer_TradeName, buyer_Add1, buyer_Add2 \n"
                        + " , actTo_StateNumericCode, buyer_StateNumericCode, buyer_StateName, buyer_CityCode, buyer_CityName \n"
                        + " , buyer_Pin, buyer_Mobile, buyer_Email, buyer_GstCountryCode, buyer_CountryName \n"
                        + " , dispFrom_SiteCode, dispFrom_Name, dispFrom_Gstin, dispFrom_GstinDate"
                        + " , dispFrom_LegalName, dispFrom_TradeName , dispFrom_Add1, dispFrom_Add2 \n "
                        + " , dispFrom_Pin, dispFrom_StateNumericCode, dispFrom_StateName ,dispFrom_CityCode, dispFrom_CityName \n"
                        + " , dispFrom_Mobile, dispFrom_Email, dispFrom_GstCountryCode, dispFrom_CountryName \n"
                        + " , shipTo_AcCode, shipTo_SiteCode, shipTo_Name , shipTo_Gstin, shipTo_GstinDate"
                        + " , shipTo_LegalName, shipTo_TradeName, shipTo_Add1, shipTo_Add2 \n"
                        + " , shipTo_Pin, shipTo_StateNumericCode ,shipTo_StateName"
                        + " , shipTo_Mobile, shipTo_Email, shipTo_CityCode, shipTo_CityName, shipTo_GstCountryCode, shipTo_CountryName \n"
                        + " , consignee_AcCode,consignee_SiteCode, consignee_Name, consignee_Gstin, consignee_GstinDate"
                        + " , consignee_LegalName, consignee_TradeName, consignee_pin, consignee_Mobile, consignee_Email \n"
                        + " , consignee_Add1, consignee_Add2, consignee_StateNumericCode"
                        + " , consignee_StateName , consignee_CityCode, consignee_CityName, consignee_GstCountryCode, consignee_CountryName \n"
                        + " , transportMode, transporter_AcCode, transporter_Name, transporter_Id \n"
                        + " , transporter_Gstin, transporter_GstinDate \n"
                        + " , lrNo, lrDate, vehicleNo, vehicleTypeCode, vehicleTypeName, gstVehicleType, distance \n"
                        + " , payee_Name, payee_AcNo, payee_ModeOfPayment, payee_BranchCode, payee_CreditTransfer \n"
                        + " , payee_PaymentInstruction, payee_PaymentTerms, payee_DirectDebit, payee_CreditDays \n"
                        + " , payee_PaidAmt, payee_DueAmt \n"
                        + " , eway_TransactionType , eway_BillNo, eWay_BillDt , eway_ValidUpTo , eway_Alert \n"
                        + " , eway_SupplyType, eway_SubSupplyType, eway_SubSupplyDesc, eway_DocType \n"
                        + " , eway_Status, eway_CancelReason , eway_CancelRemark, eway_cancelDt \n"
                        + " , einv_Version, einv_SupplyType, einv_DocType, einv_IrnNo, einv_ackNo, einv_ackDt \n"
                        + " , einv_JwtIssuer, einv_Status \n"
                        + " , discountAmount, taxableAmount, sgstAmount, cgstAmount, igstAmount, cessAmount, cessNaAmount \n"
                        + " , scessAmount, otherCharges, totalInvAmount, roundOffAmount, netInvAmount, netInvAmountInFC \n"
                        + " , einv_CancelReason, einv_CancelRemark, einv_cancelDt, eWay_GenerationMode"
                        + " , isEWayBillRequired, isEInvoiceRequired, isSiteTransaction \n"
                        + " , refDocId,refRecId,refV_Date,refDocOtherRef,refInvDocStartDate \n"
                        + " , refInvDocEndDate,refRemark,shippingBillNo,shippingBillDate \n"
                        + " , gstPortCode, refundClaim, gstCurrencyCode \n"
                        + " ) VALUES ("
                        + "   :docid, :v_type, :v_Prefix, :site_Code, :v_date, :taxScheme, :eComGstin \n"
                        + " , :recID, :recIdPrefix, :recIdSerial, :isIgstOnIntra \n"
                        + " , :gstType, :isRCM, :posStateNumericCode, :posStateName, :partyType, :locationCode \n"
                        + " , :seller_SiteCode, :seller_Name, :seller_Gstin, :seller_GstinDate \n"
                        + " , :seller_TradeName, :seller_LegalName, :seller_Add1, :seller_Add2 \n"
                        + " , :seller_Pin, :actFrom_StateNumericCode, :seller_StateName, :seller_StateNumericCode \n"
                        + " , :seller_CityCode, :seller_CityName, :seller_Mobile, :seller_Email, :seller_GstCountryCode, :seller_CountryName \n"
                        + " , :buyer_AcCode, :buyer_SiteCode, :buyer_Name, :buyer_Gstin, :buyer_GstinDate"
                        + " , :buyer_LegalName, :buyer_TradeName, :buyer_Add1, :buyer_Add2 \n"
                        + " , :actTo_StateNumericCode, :buyer_StateNumericCode, :buyer_StateName, :buyer_CityCode, :buyer_CityName \n"
                        + " , :buyer_Pin, :buyer_Mobile, :buyer_Email, :buyer_GstCountryCode, :buyer_CountryName \n"
                        + " , :dispFrom_SiteCode, :dispFrom_Name, :dispFrom_Gstin, :dispFrom_GstinDate"
                        + " , :dispFrom_LegalName, :dispFrom_TradeName , :dispFrom_Add1, :dispFrom_Add2 \n "
                        + " , :dispFrom_Pin, :dispFrom_StateNumericCode, :dispFrom_StateName , :dispFrom_CityCode ,:dispFrom_CityName \n"
                        + " , :dispFrom_Mobile, :dispFrom_Email, :dispFrom_GstCountryCode, :dispFrom_CountryName \n"
                        + " , :shipTo_AcCode, :shipTo_SiteCode ,:shipTo_Name , :shipTo_Gstin, :shipTo_GstinDate"
                        + " , :shipTo_LegalName, :shipTo_TradeName, :shipTo_Add1, :shipTo_Add2 \n"
                        + " , :shipTo_Pin, :shipTo_StateNumericCode ,:shipTo_StateName"
                        + " , :shipTo_Mobile, :shipTo_Email, :shipTo_CityCode , :shipTo_CityName, :shipTo_GstCountryCode, :shipTo_CountryName \n"
                        + " , :consignee_AcCode,:consignee_SiteCode, :consignee_Name, :consignee_Gstin, :consignee_GstinDate"
                        + " , :consignee_LegalName, :consignee_TradeName, :consignee_pin, :consignee_Mobile, :consignee_Email \n"
                        + " , :consignee_Add1, :consignee_Add2, :consignee_StateNumericCode"
                        + " , :consignee_StateName , :consignee_CityCode , :consignee_CityName, :consignee_GstCountryCode, :consignee_CountryName \n"
                        + " , :transportMode, :transporter_AcCode, :transporter_Name, :transporter_Id \n"
                        + " , :transporter_Gstin, :transporter_GstinDate \n"
                        + " , :lrNo, :lrDate, :vehicleNo, :vehicleTypeCode, :vehicleTypeName, :gstVehicleType, :distance \n"
                        + " , :payee_Name, :payee_AcNo, :payee_ModeOfPayment, :payee_BranchCode, :payee_CreditTransfer \n"
                        + " , :payee_PaymentInstruction, :payee_PaymentTerms, :payee_DirectDebit, :payee_CreditDays \n"
                        + " , :payee_PaidAmt, :payee_DueAmt \n"
                        + " , :eway_TransactionType , :eway_BillNo, :eWay_BillDt , :eway_ValidUpTo , :eway_Alert \n"
                        + " , :eway_SupplyType, :eway_SubSupplyType, :eway_SubSupplyDesc, :eway_DocType \n"
                        + " , :eway_Status, :eway_CancelReason , :eway_CancelRemark, :eway_cancelDt \n"
                        + " , :einv_Version, :einv_SupplyType, :einv_DocType, :einv_IrnNo, :einv_ackNo, :einv_ackDt \n"
                        + " , :einv_JwtIssuer, :einv_Status \n"
                        + " , :discountAmount, :taxableAmount, :sgstAmount, :cgstAmount, :igstAmount, :cessAmount, :cessNaAmount \n"
                        + " , :scessAmount, :otherCharges, :totalInvAmount, :roundOffAmount, :netInvAmount, :netInvAmountInFC \n"
                        + " , :einv_CancelReason, :einv_CancelRemark, :einv_cancelDt, :eWay_GenerationMode"
                        + " , :isEWayBillRequired, :isEInvoiceRequired, :isSiteTransaction \n"
                        + " , :refDocId,:refRecId,:refV_Date,:refDocOtherRef,:refInvDocStartDate"
                        + " , :refInvDocEndDate,:refRemark,:shippingBillNo,:shippingBillDate \n"
                        + " , :gstPortCode, :refundClaim, :gstCurrencyCode \n"
                        + ") \n";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docid", DmShared.iNull(currentEgst_Mdl.getDocid()).toString());
                psTemp.setString("v_type", DmShared.xNull(currentEgst_Mdl.getV_Type()));
                psTemp.setString("v_Prefix", DmShared.foriegnKey(currentEgst_Mdl.getV_Prefix()));
                psTemp.setString("site_Code", DmShared.foriegnKey(currentEgst_Mdl.getSite_Code()));
                psTemp.setString("v_date", DmShared.dNull(currentEgst_Mdl.getV_Date()));
                psTemp.setString("taxScheme", DmShared.iNull(currentEgst_Mdl.getTaxSchemeCode()).toString());
                psTemp.setString("eComGstin", DmShared.xNull(currentEgst_Mdl.geteComGstin()));
                psTemp.setString("recID", DmShared.xNull(currentEgst_Mdl.getRecId().getRecIdValue()));
                psTemp.setString("recIdSerial", DmShared.xNull(String.valueOf(currentEgst_Mdl.getRecId().getCounter())));
                psTemp.setString("recIdPrefix", DmShared.xNull(currentEgst_Mdl.getRecId().getPrefix()));
                psTemp.setString("eWay_GenerationMode", DmShared.foriegnKey(currentEgst_Mdl.getEway_GenerationMode()));
                psTemp.setInt("isIgstOnIntra", (DmShared.xNull(currentEgst_Mdl.getIgstOnIntra()).trim().equalsIgnoreCase("") ? DmEnum.DmYesNo.NO.toInt() : DmEnum.DmYesNo.getViaShortName(DmShared.xNull(currentEgst_Mdl.getIgstOnIntra())).toInt()));
                psTemp.setString("gstType", DmShared.iNull(currentEgst_Mdl.getGstType()).toString());
                psTemp.setInt("isEWayBillRequired", currentEgst_Mdl.isEwayBillRequired() ? 1 : 0);
                psTemp.setInt("isEInvoiceRequired", currentEgst_Mdl.isEinvoiceRequired() ? 1 : 0);
                psTemp.setInt("isSiteTransaction", currentEgst_Mdl.isSiteTransaction() ? 1 : 0);
                psTemp.setInt("isRCM", (DmShared.xNull(currentEgst_Mdl.getReverseCharge()).trim().equalsIgnoreCase("") ? DmEnum.DmYesNo.NO.toInt() : DmEnum.DmYesNo.getViaShortName(DmShared.xNull(currentEgst_Mdl.getReverseCharge())).toInt()));
                psTemp.setString("posStateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getPosStateNumericCode()));
                psTemp.setString("posStateName", DmShared.foriegnKey(currentEgst_Mdl.getPosStateName()));
                psTemp.setString("partyType", DmShared.xNull(currentEgst_Mdl.getPartyType()));
                psTemp.setString("locationCode", DmShared.foriegnKey(currentEgst_Mdl.getLocationCode()));
                psTemp.setString("seller_SiteCode", DmShared.foriegnKey(currentEgst_Mdl.getConsigner_Mdl().getSiteCode()));
                psTemp.setString("seller_Name", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getSharedKeyFieldName()));
                psTemp.setString("seller_Gstin", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getGstin()));
                psTemp.setDate("seller_GstinDate", DmServer.convertDate(currentEgst_Mdl.getConsigner_Mdl().getGstinDate()));
                psTemp.setString("seller_TradeName", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getTradeName()));
                psTemp.setString("seller_LegalName", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getLegalName()));
                psTemp.setString("seller_Add1", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getAddress1()));
                psTemp.setString("seller_Add2", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getAddress2()));
                psTemp.setString("seller_Pin", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getPinCode()));
                psTemp.setString("seller_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getConsigner_Mdl().getStateNumericCode()));
                psTemp.setString("seller_StateName", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getStateName()));
                psTemp.setString("seller_CityCode", DmShared.foriegnKey(currentEgst_Mdl.getConsigner_Mdl().getCityCode()));
                psTemp.setString("seller_CityName", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getCityName()));
                psTemp.setString("seller_Mobile", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getPhone()));
                psTemp.setString("seller_Email", DmShared.xNull(currentEgst_Mdl.getConsigner_Mdl().getEmail()));
                psTemp.setString("seller_GstCountryCode", DmShared.foriegnKey(currentEgst_Mdl.getConsigner_Mdl().getGstCountryCode()));
                psTemp.setString("seller_CountryName", DmShared.foriegnKey(currentEgst_Mdl.getConsigner_Mdl().getCountryName()));
                psTemp.setString("buyer_AcCode", DmShared.foriegnKey(currentEgst_Mdl.getBillTo_Mdl().getSharedKeyFieldCode()));
                psTemp.setString("buyer_SiteCode", DmShared.foriegnKey(currentEgst_Mdl.getBillTo_Mdl().getSiteCode()));
                psTemp.setString("buyer_Name", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getSharedKeyFieldName()));
                psTemp.setString("buyer_Gstin", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getGstin()));
                psTemp.setDate("buyer_GstinDate", DmServer.convertDate(currentEgst_Mdl.getBillTo_Mdl().getGstinDate()));
                psTemp.setString("buyer_LegalName", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getLegalName()));
                psTemp.setString("buyer_TradeName", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getTradeName()));
                psTemp.setString("buyer_Add1", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getAddress1()));
                psTemp.setString("buyer_Add2", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getAddress2()));
                psTemp.setString("buyer_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getBillTo_Mdl().getStateNumericCode()));
                psTemp.setString("buyer_StateName", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getStateName()));
                psTemp.setString("buyer_CityCode", DmShared.foriegnKey(currentEgst_Mdl.getBillTo_Mdl().getCityCode()));
                psTemp.setString("buyer_CityName", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getCityName()));
                psTemp.setString("buyer_Pin", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getPinCode()));
                psTemp.setString("buyer_Mobile", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getPhone()));
                psTemp.setString("buyer_Email", DmShared.xNull(currentEgst_Mdl.getBillTo_Mdl().getEmail()));
                psTemp.setString("buyer_GstCountryCode", DmShared.foriegnKey(currentEgst_Mdl.getBillTo_Mdl().getGstCountryCode()));
                psTemp.setString("buyer_CountryName", DmShared.foriegnKey(currentEgst_Mdl.getBillTo_Mdl().getCountryName()));
                psTemp.setString("dispFrom_SiteCode", DmShared.foriegnKey(currentEgst_Mdl.getDispatchFrom_Mdl().getSiteCode()));
                psTemp.setString("dispFrom_Name", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getSharedKeyFieldName()));
                psTemp.setString("dispFrom_Gstin", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getGstin()));
                psTemp.setDate("dispFrom_GstinDate", DmServer.convertDate(currentEgst_Mdl.getDispatchFrom_Mdl().getGstinDate()));
                psTemp.setString("dispFrom_TradeName", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getTradeName()));
                psTemp.setString("dispFrom_LegalName", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getLegalName()));
                psTemp.setString("dispFrom_Add1", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getAddress1()));
                psTemp.setString("dispFrom_Add2", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getAddress2()));
                psTemp.setString("dispFrom_CityCode", DmShared.foriegnKey(currentEgst_Mdl.getDispatchFrom_Mdl().getCityCode()));
                psTemp.setString("dispFrom_CityName", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getCityName()));
                psTemp.setString("dispFrom_StateName", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getStateName()));
                psTemp.setString("dispFrom_Pin", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getPinCode()));
                psTemp.setString("dispFrom_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getDispatchFrom_Mdl().getStateNumericCode()));
                psTemp.setString("dispFrom_Mobile", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getPhone()));
                psTemp.setString("dispFrom_Email", DmShared.xNull(currentEgst_Mdl.getDispatchFrom_Mdl().getEmail()));
                psTemp.setString("dispFrom_GstCountryCode", DmShared.foriegnKey(currentEgst_Mdl.getDispatchFrom_Mdl().getGstCountryCode()));
                psTemp.setString("dispFrom_CountryName", DmShared.foriegnKey(currentEgst_Mdl.getDispatchFrom_Mdl().getCountryName()));
                psTemp.setString("shipTo_AcCode", DmShared.foriegnKey(currentEgst_Mdl.getShipTo_Mdl().getSharedKeyFieldCode()));
                psTemp.setString("shipTo_SiteCode", DmShared.foriegnKey(currentEgst_Mdl.getShipTo_Mdl().getSiteCode()));
                psTemp.setString("shipTo_Name", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getSharedKeyFieldName()));
                psTemp.setString("shipTo_Gstin", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getGstin()));
                psTemp.setDate("shipTo_GstinDate", DmServer.convertDate(currentEgst_Mdl.getShipTo_Mdl().getGstinDate()));
                psTemp.setString("shipTo_LegalName", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getLegalName()));
                psTemp.setString("shipTo_TradeName", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getTradeName()));
                psTemp.setString("shipTo_Add1", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getAddress1()));
                psTemp.setString("shipTo_Add2", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getAddress2()));
                psTemp.setString("shipTo_Pin", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getPinCode()));
                psTemp.setString("shipTo_CityCode", DmShared.foriegnKey(currentEgst_Mdl.getShipTo_Mdl().getCityCode()));
                psTemp.setString("shipTo_CityName", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getCityName()));
                psTemp.setString("shipTo_StateName", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getStateName()));
                psTemp.setString("shipTo_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getShipTo_Mdl().getStateNumericCode()));
                psTemp.setString("shipTo_Mobile", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getPhone()));
                psTemp.setString("shipTo_Email", DmShared.xNull(currentEgst_Mdl.getShipTo_Mdl().getEmail()));
                psTemp.setString("shipTo_GstCountryCode", DmShared.foriegnKey(currentEgst_Mdl.getShipTo_Mdl().getGstCountryCode()));
                psTemp.setString("shipTo_CountryName", DmShared.foriegnKey(currentEgst_Mdl.getShipTo_Mdl().getCountryName()));
                psTemp.setString("consignee_AcCode", DmShared.foriegnKey(currentEgst_Mdl.getConsignee_Mdl().getSharedKeyFieldCode()));
                psTemp.setString("consignee_SiteCode", DmShared.foriegnKey(currentEgst_Mdl.getConsignee_Mdl().getSiteCode()));
                psTemp.setString("consignee_Name", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getSharedKeyFieldName()));
                psTemp.setString("consignee_Gstin", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getGstin()));
                psTemp.setDate("consignee_GstinDate", DmServer.convertDate(currentEgst_Mdl.getConsignee_Mdl().getGstinDate()));
                psTemp.setString("consignee_LegalName", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getLegalName()));
                psTemp.setString("consignee_TradeName", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getTradeName()));
                psTemp.setString("consignee_Add1", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getAddress1()));
                psTemp.setString("consignee_Add2", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getAddress2()));
                psTemp.setString("consignee_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getConsignee_Mdl().getStateNumericCode()));
                psTemp.setString("consignee_StateName", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getStateName()));
                psTemp.setString("consignee_CityCode", DmShared.foriegnKey(currentEgst_Mdl.getConsignee_Mdl().getCityCode()));
                psTemp.setString("consignee_CityName", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getCityName()));
                psTemp.setString("consignee_Pin", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getPinCode()));
                psTemp.setString("consignee_Mobile", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getPhone()));
                psTemp.setString("consignee_Email", DmShared.xNull(currentEgst_Mdl.getConsignee_Mdl().getEmail()));
                psTemp.setString("consignee_GstCountryCode", DmShared.foriegnKey(currentEgst_Mdl.getConsignee_Mdl().getGstCountryCode()));
                psTemp.setString("consignee_CountryName", DmShared.foriegnKey(currentEgst_Mdl.getConsignee_Mdl().getCountryName()));
                psTemp.setString("transportMode", DmShared.foriegnKey(currentEgst_Mdl.getTransportModeCode()));
                psTemp.setString("transporter_AcCode", DmShared.foriegnKey(currentEgst_Mdl.getTransporter_Mdl().getSharedKeyFieldCode()));
                psTemp.setString("transporter_Id", DmShared.foriegnKey(currentEgst_Mdl.getTransporter_Mdl().getTransporterId()));
                psTemp.setString("transporter_Name", DmShared.xNull(currentEgst_Mdl.getTransporter_Mdl().getSharedKeyFieldName()));
                psTemp.setString("transporter_Gstin", DmShared.xNull(currentEgst_Mdl.getTransporter_Mdl().getGstin()));
                psTemp.setDate("transporter_GstinDate", DmServer.convertDate(currentEgst_Mdl.getTransporter_Mdl().getGstinDate()));
                psTemp.setString("lrNo", DmShared.xNull(currentEgst_Mdl.getTransportLrNo()));
                psTemp.setDate("lrDate", DmServer.convertDate(currentEgst_Mdl.getTransportLrDate()));
                psTemp.setString("vehicleNo", DmShared.xNull(currentEgst_Mdl.getVehicleNo()));
                psTemp.setString("vehicleTypeCode", DmShared.foriegnKey(currentEgst_Mdl.getVehicleTypeCode()));
                psTemp.setString("vehicleTypeName", DmShared.xNull(currentEgst_Mdl.getVehicleTypeName()));
                psTemp.setString("gstVehicleType", DmShared.foriegnKey(currentEgst_Mdl.getGstVehicleType()));
                psTemp.setString("distance", DmServer.vNull(currentEgst_Mdl.getTransDistance()));
                psTemp.setString("payee_Name", DmShared.xNull(currentEgst_Mdl.getPayee_Name()));
                psTemp.setString("payee_AcNo", DmShared.xNull(currentEgst_Mdl.getPayee_AccountNo()));
                psTemp.setString("payee_ModeOfPayment", DmShared.xNull(currentEgst_Mdl.getPayee_ModeOfPayment()));
                psTemp.setString("payee_BranchCode", DmShared.xNull(currentEgst_Mdl.getPayee_BranchCode()));
                psTemp.setString("payee_CreditTransfer", DmShared.xNull(currentEgst_Mdl.getPayee_CrTrn()));
                psTemp.setString("payee_PaymentInstruction", DmShared.xNull(currentEgst_Mdl.getPayee_PaymentInstruction()));
                psTemp.setString("payee_PaymentTerms", DmShared.xNull(currentEgst_Mdl.getPayee_PaymentTerms()));
                psTemp.setString("payee_DirectDebit", DmShared.xNull(currentEgst_Mdl.getPayee_DirectDebit()));
                psTemp.setString("payee_CreditDays", DmShared.iNull(currentEgst_Mdl.getPayee_CreditDays()).toString());
                psTemp.setString("payee_PaidAmt", DmServer.vNull(currentEgst_Mdl.getPayee_PaidAmount(), "0.00"));
                psTemp.setString("payee_DueAmt", DmServer.vNull(currentEgst_Mdl.getPayee_PaymentDue(), "0.00"));
                psTemp.setString("eway_TransactionType", DmShared.foriegnKey(currentEgst_Mdl.getEwayTransTypeCode()));
                psTemp.setString("eway_DocType", DmShared.foriegnKey(currentEgst_Mdl.getEwayDocTypeCode()));
                psTemp.setString("eway_BillNo", DmShared.foriegnKey(currentEgst_Mdl.getEway_BillNo()));
                psTemp.setTimestamp("eWay_BillDt", convertTimestamp(currentEgst_Mdl.getEway_BillDate()));
                psTemp.setTimestamp("eway_ValidUpTo", convertTimestamp(currentEgst_Mdl.getEway_validUpto()));
                psTemp.setString("eway_Alert", DmShared.xNull(currentEgst_Mdl.getEway_alert()));
                psTemp.setString("eway_SupplyType", DmShared.foriegnKey(currentEgst_Mdl.getEwaySupplyTypeCode()));
                psTemp.setString("eway_SubSupplyType", DmShared.foriegnKey(currentEgst_Mdl.getEwaySubSupplyTypeCode()));
                psTemp.setString("eway_SubSupplyDesc", DmShared.xNull(currentEgst_Mdl.getEwaySubSupplyDesc()));
                psTemp.setString("eway_Status", DmShared.xNull(currentEgst_Mdl.getEway_Status()));
                psTemp.setString("eway_CancelReason", DmShared.foriegnKey(currentEgst_Mdl.getEway_cancelRsnCode()));
                psTemp.setString("eway_CancelRemark", DmShared.xNull(currentEgst_Mdl.getEway_cancelRmrk()));
                psTemp.setTimestamp("eway_cancelDt", convertTimestamp(currentEgst_Mdl.getEway_cancelDate()));
                psTemp.setString("einv_CancelReason", DmShared.foriegnKey(currentEgst_Mdl.getEinv_cancelRsnCode()));
                psTemp.setString("einv_CancelRemark", DmShared.xNull(currentEgst_Mdl.getEinv_cancelRmrk()));
                psTemp.setTimestamp("einv_cancelDt", convertTimestamp(currentEgst_Mdl.getEinv_cancelDate()));
                psTemp.setString("einv_Version", DmShared.xNull(currentEgst_Mdl.getEinv_version()));
                psTemp.setString("einv_SupplyType", DmShared.xNull(currentEgst_Mdl.getEinvSupplyTypeCode()));
                psTemp.setString("einv_DocType", DmShared.foriegnKey(currentEgst_Mdl.getEinvDocTypeCode()));
                psTemp.setString("einv_IrnNo", DmShared.foriegnKey(currentEgst_Mdl.getEinv_IrnNo()));
                psTemp.setString("einv_ackNo", DmShared.foriegnKey(currentEgst_Mdl.getEinv_ackNo()));
                psTemp.setTimestamp("einv_ackDt", convertTimestamp(currentEgst_Mdl.getEinv_ackDt()));
                psTemp.setString("einv_Status", DmShared.xNull(currentEgst_Mdl.getEinv_Status()));
                psTemp.setString("einv_JwtIssuer", DmShared.xNull(currentEgst_Mdl.getEinv_JwtIssuer()));
                psTemp.setString("discountAmount", DmServer.vNull(currentEgst_Mdl.getDiscountAmount(), "0.00"));
                psTemp.setString("taxableAmount", DmServer.vNull(currentEgst_Mdl.getTaxableValue(), "0.00"));
                psTemp.setString("cgstAmount", DmServer.vNull(currentEgst_Mdl.getCgstValue(), "0.00"));
                psTemp.setString("sgstAmount", DmServer.vNull(currentEgst_Mdl.getSgstValue(), "0.00"));
                psTemp.setString("igstAmount", DmServer.vNull(currentEgst_Mdl.getIgstValue(), "0.00"));
                psTemp.setString("cessAmount", DmServer.vNull(currentEgst_Mdl.getCessValue(), "0.00"));
                psTemp.setString("cessNaAmount", DmServer.vNull(currentEgst_Mdl.getCessNonAdvolValue(), "0.00"));
                psTemp.setString("scessAmount", DmServer.vNull(currentEgst_Mdl.getStateCessAmount(), "0.00"));
                psTemp.setString("totalInvAmount", DmServer.vNull(currentEgst_Mdl.getTotInvValue(), "0.00"));
                psTemp.setString("otherCharges", DmServer.vNull(currentEgst_Mdl.getOtherAmount(), "0.00"));
                psTemp.setString("roundOffAmount", DmServer.vNull(currentEgst_Mdl.getRoundOfAmount(), "0.00"));
                psTemp.setString("netInvAmount", DmServer.vNull(currentEgst_Mdl.getNetInvAmount(), "0.00"));
                psTemp.setString("netInvAmountInFC", DmServer.vNull(currentEgst_Mdl.getTotalInvAmtInFc(), "0.00"));
                psTemp.setString("actTo_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getActToStateNumericCode()));
                psTemp.setString("actFrom_StateNumericCode", DmShared.foriegnKey(currentEgst_Mdl.getActFromStateNumericCode()));
                psTemp.setString("refDocId", DmShared.foriegnKey(currentEgst_Mdl.getRefDocid()));
                psTemp.setString("refRecId", DmShared.foriegnKey(currentEgst_Mdl.getRefRecId()));
                psTemp.setDate("refV_Date", DmServer.convertDate(currentEgst_Mdl.getRefV_Date()));
                psTemp.setString("refDocOtherRef", DmShared.foriegnKey(currentEgst_Mdl.getRefDocid()));
                psTemp.setDate("refInvDocStartDate", DmServer.convertDate(currentEgst_Mdl.getRefInvDocStartDate()));
                psTemp.setDate("refInvDocEndDate", DmServer.convertDate(currentEgst_Mdl.getRefInvDocEndDate()));
                psTemp.setString("refRemark", DmShared.xNull(currentEgst_Mdl.getRefRemark()));
                psTemp.setString("shippingBillNo", DmShared.foriegnKey(currentEgst_Mdl.getShippingBillNo()));
                psTemp.setDate("shippingBillDate", DmServer.convertDate(currentEgst_Mdl.getShippingBillDate()));
                psTemp.setString("gstPortCode", DmShared.xNull(currentEgst_Mdl.getPortCode()));
                psTemp.setString("refundClaim", DmShared.iNull(currentEgst_Mdl.getRefundClaim()).toString());
                psTemp.setString("gstCurrencyCode", DmShared.xNull(currentEgst_Mdl.getCurrencyCode()));
                psTemp.executeUpdate();
                psTemp.close();

                int vSno = 1;
                for (EGstDetail_Mdl detail_Mdl : currentEgst_Mdl.getItemList()) {
                    sqlQuery = " INSERT INTO eGstDetail ("
                            + "   docid, v_SNo, itemCode, ibCode , itemName, itemDesc \n"
                            + " , hsn, itemGSTNature, rate, quantity, freeQty \n"
                            + " , unitCode, unitName, gstUnit, batchNo, warrentyDate, expiryDate \n"
                            + " , grossAmount, discountAmount, taxableAmount, gstRate, cgstRate, sgstRate \n"
                            + " , igstRate, cessRate, cessNonAdvolRate, sCessRate \n"
                            + " , sgstAmount, cgstAmount, igstAmount, cessAmount, cessNaAmount, scessAmount, otherCharges, netAmount  \n"
                            + " , isService ,propertyDispCsv,barCode,poRefNo,gstOriginCountryCode,itemUSNo \n"
                            + " ) VALUES ("
                            + "   :docid, :v_SNo, :itemCode, :ibCode , :itemName, :itemDesc \n"
                            + " , :hsn, :itemGSTNature, :rate, :quantity, :freeQty \n"
                            + " , :unitCode, :unitName, :gstUnit, :batchNo, :warrentyDate, :expiryDate \n"
                            + " , :grossAmount, :discountAmount, :taxableAmount, :gstRate, :cgstRate, :sgstRate \n"
                            + " , :igstRate, :cessRate, :cessNonAdvolRate, :sCessRate \n"
                            + " , :sgstAmount, :cgstAmount, :igstAmount, :cessAmount, :cessNaAmount, :scessAmount, :otherCharges, :netAmount  \n"
                            + " , :isService ,:propertyDispCsv,:barCode,:poRefNo,:gstOriginCountryCode,:itemUSNo \n"
                            + ") \n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docid", DmShared.iNull(currentEgst_Mdl.getDocid()).toString());
                    psTemp.setString("v_Sno", DmShared.iNull(vSno++).toString());
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getItemCode()));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("itemName", DmShared.xNull(detail_Mdl.getItemName()));
                    psTemp.setString("itemDesc", DmShared.xNull(detail_Mdl.getItemDesc()));
                    psTemp.setString("hsn", DmShared.foriegnKey(detail_Mdl.getHsn()));
                    psTemp.setString("itemGSTNature", DmShared.foriegnKey(detail_Mdl.getItemGstNature()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("freeQty", DmServer.vNull(detail_Mdl.getFreeQty()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getUnitCode()));
                    psTemp.setString("unitName", DmShared.xNull(detail_Mdl.getUnitName()));
                    psTemp.setString("gstUnit", DmShared.xNull(detail_Mdl.getGstUnit()));
                    psTemp.setString("batchNo", DmShared.xNull(detail_Mdl.getBatchNo()));
                    psTemp.setDate("warrentyDate", DmServer.convertDate(detail_Mdl.getWarrantyDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("grossAmount", DmServer.vNull(detail_Mdl.getGrossAmount()));
                    psTemp.setString("discountAmount", DmServer.vNull(detail_Mdl.getDiscountAmount()));
                    psTemp.setString("taxableAmount", DmServer.vNull(detail_Mdl.getTaxableAmount()));
                    psTemp.setString("gstRate", DmServer.vNull(detail_Mdl.getGstRate()));
                    psTemp.setString("cgstRate", DmServer.vNull(detail_Mdl.getCgstRate()));
                    psTemp.setString("sgstRate", DmServer.vNull(detail_Mdl.getSgstRate()));
                    psTemp.setString("igstRate", DmServer.vNull(detail_Mdl.getIgstRate()));
                    psTemp.setString("cessRate", DmServer.vNull(detail_Mdl.getCessRate()));
                    psTemp.setString("cessNonAdvolRate", DmServer.vNull(detail_Mdl.getCessNonAdvolRate()));
                    psTemp.setString("sCessRate", DmServer.vNull(detail_Mdl.getStateCessRate()));
                    psTemp.setString("sgstAmount", DmServer.vNull(detail_Mdl.getSgstAmount(), "0.00"));
                    psTemp.setString("igstAmount", DmServer.vNull(detail_Mdl.getIgstAmount(), "0.00"));
                    psTemp.setString("cgstAmount", DmServer.vNull(detail_Mdl.getCgstAmount(), "0.00"));
                    psTemp.setString("cessAmount", DmServer.vNull(detail_Mdl.getCessAmount(), "0.00"));
                    psTemp.setString("cessNaAmount", DmServer.vNull(detail_Mdl.getCessNaAmount(), "0.00"));
                    psTemp.setString("scessAmount", DmServer.vNull(detail_Mdl.getStateCessAmount(), "0.00"));
                    psTemp.setString("otherCharges", DmServer.vNull(detail_Mdl.getOtherCharges(), "0.00"));
                    psTemp.setString("netAmount", DmServer.vNull(detail_Mdl.getNetAmount(), "0.00"));
//                    psTemp.setInt("isService", (DmShared.xNull(detail_Mdl.getIsService()).trim().equalsIgnoreCase("") ? DmEnum.DmYesNo.NO.toInt() : DmEnum.DmYesNo.getViaShortName(DmShared.xNull(detail_Mdl.getIsService())).toInt()));
                    psTemp.setInt("isService", 0);
                    psTemp.setString("propertyDispCsv", DmShared.xNull(detail_Mdl.getPropertieDispCsv()));
                    psTemp.setString("barCode", DmShared.foriegnKey(detail_Mdl.getBarCode()));
                    psTemp.setString("poRefNo", DmShared.foriegnKey(detail_Mdl.getOrderRefNo()));
                    psTemp.setString("gstOriginCountryCode", DmShared.foriegnKey(detail_Mdl.getOriginCountry()));
                    psTemp.setString("itemUSNo", DmShared.foriegnKey(detail_Mdl.getItemUSno()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                vSno = 1;
                for (EinvRefContractDtls_Mdl contract : currentEgst_Mdl.getContractDetails()) {
                    sqlQuery = " INSERT INTO eGstContractDetail ( \n"
                            + "   docid, v_SNo \n"
                            + " , receiptAdviceNo,receiptAdviceDate,tenderRefNo,contractNo,extraRefNo,projectRefNo,poRefNo,poRefDate \n"
                            + " ) VALUES ( \n"
                            + "   :docid, :v_SNo \n"
                            + " , :receiptAdviceNo,:receiptAdviceDate,:tenderRefNo,:contractNo,:extraRefNo,:projectRefNo,:poRefNo,:poRefDate \n"
                            + ") \n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docid", DmShared.iNull(currentEgst_Mdl.getDocid()).toString());
                    psTemp.setString("v_Sno", DmShared.iNull(vSno++).toString());
                    psTemp.setString("receiptAdviceNo", DmShared.xNull(contract.getReceiptAdviceNo()));
                    psTemp.setString("receiptAdviceDate", DmServer.convertDate(contract.getReceiptAdviceDate()).toString());
                    psTemp.setString("tenderRefNo", DmShared.foriegnKey(contract.getTenderRefNo()));
                    psTemp.setString("contractNo", DmShared.foriegnKey(contract.getContractNo()));
                    psTemp.setString("extraRefNo", DmShared.foriegnKey(contract.getExtraRefNo()));
                    psTemp.setString("projectRefNo", DmShared.foriegnKey(contract.getProjectRefNo()));
                    psTemp.setString("poRefNo", DmShared.foriegnKey(contract.getPoRefNo()));
                    psTemp.setDate("poRefDate", DmServer.convertDate(contract.getPoRefDate()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                vSno = 1;
                for (EinvAdditionalDocDtls_Mdl addDoc : currentEgst_Mdl.getAddDocList()) {
                    sqlQuery = " INSERT INTO eGstDocDetail ( \n"
                            + "   docid, v_SNo \n"
                            + " , docUrl,docEncoded,docInfo \n"
                            + " ) VALUES ( \n"
                            + "   :docid, :v_SNo \n"
                            + " , :docUrl,:docEncoded,:docInfo \n"
                            + ") \n";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docid", DmShared.iNull(currentEgst_Mdl.getDocid()).toString());
                    psTemp.setString("v_Sno", DmShared.iNull(vSno++).toString());
                    psTemp.setString("docUrl", DmShared.foriegnKey(addDoc.getDocumentUrl()));
                    psTemp.setString("docEncoded", DmShared.foriegnKey(addDoc.getDocument()));
                    psTemp.setString("docInfo", DmShared.foriegnKey(addDoc.getAdditionalInfoOfDocument()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Save E-Invoice Mongo Data">
                if (actionCode == DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode()) {
                    if (DmShared.xNull(currentEgst_Mdl.getMongoLinkId()).trim().equalsIgnoreCase("")) {
                        currentEgst_Mdl.setMongoLinkId(DmShared.xNull(EinvoiceMongo_Server.getUUID()));
                    }
                    EinvoiceMongo_Server.save(getEinvoice_Mongo_Mdl(currentEgst_Mdl, cnTemp), dmRss);
                }
                //</editor-fold>
            }
            //<editor-fold defaultstate="collapsed" desc="SaveLog">
            String module = "", menuName = "", shortName = "", description = "";
            //<editor-fold defaultstate="collapsed" desc="GET TRANSCATION DETAILS">
            sqlQuery = "SELECT TOP 1 vt.description, vt.short_Name \n"
                    + ", vt.mnuText as menuName, vt.mnuAttachedInModule as module \n"
                    + "from gstTrans gt \n"
                    + "LEFT JOIN voucher_Type vt on vt.v_type = gt.v_type \n"
                    + "WHERE gt.docid = :docid \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", currentEgst_Mdl.getDocid());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                description = DmShared.xNull(rsTemp.getString("description"));
                shortName = DmShared.xNull(rsTemp.getString("short_Name"));
                menuName = DmShared.xNull(rsTemp.getString("menuName"));
                module = DmShared.xNull(rsTemp.getString("module"));
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>
            //==============================<< Log Save >>====================================
            ModifyReason_Mdl modifyReasonMdl = new ModifyReason_Mdl();
            modifyReasonMdl.setMode(DmEnum.EGST_ACTION.getViaCode(actionCode).getTransMode());
            modifyReasonMdl.setSharedKeyFieldCode(DmShared.xNull(DmServer.getCodeViaManualCode(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName(), "name", "modifyReasonMast", "code", cnTemp)));
            modifyReasonMdl.setSharedKeyFieldName(DmShared.xNull(DmEnum.DmModifyReason.SYSTEM_ACTIVITY.getName()));
            ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
            modifyData_Mdl.setKeyId_1(DmShared.xNull(currentEgst_Mdl.getDocid()));
            modifyData_Mdl.setKeyName_1(DmShared.xNull(currentEgst_Mdl.getRecId().getRecIdValue()));
            modifyData_Mdl.setKeyDescription_1(DmShared.xNull(currentEgst_Mdl.getEway_BillNo()));
            modifyData_Mdl.setKeyDescription_2(DmShared.xNull(currentEgst_Mdl.getEinv_IrnNo()));
            modifyData_Mdl.setModuleName(DmShared.xNull(module));
            modifyData_Mdl.setMenuName(DmShared.xNull(menuName));
            modifyData_Mdl.setModelClassName(EGst_Mdl.class.getName());
            modifyData_Mdl.setVrTypeDescription(DmShared.xNull(description));
            modifyData_Mdl.setVrTypeSName(DmShared.xNull(shortName));
            modifyData_Mdl.setTableName("eGst");
            modifyData_Mdl.setOldData(DmServer.convertToJson(previousEgst_Mdl, new TypeToken<EGst_Mdl>() {
            }));
            modifyData_Mdl.setNewData(DmServer.convertToJson(currentEgst_Mdl, new TypeToken<EGst_Mdl>() {
            }));
            ModifyReason_Server.saveNew(modifyData_Mdl, modifyReasonMdl, dmRss);
            //</editor-fold>

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void deleteEGstRecord(String docId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        try {

            sqlQuery = "DELETE FROM eGstContractDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM eGstDocDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM eGstDetail WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "DELETE FROM eGst WHERE docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
