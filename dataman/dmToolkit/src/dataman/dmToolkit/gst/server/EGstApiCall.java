package dataman.dmToolkit.gst.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dataman.dmBase.self.server.DmApiBase_Server;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.gst.shared.*;
import java.util.HashMap;
import okhttp3.Response;
import org.json.JSONObject;

public class EGstApiCall {

    private final DmApiBase_Server dmApiBase = new DmApiBase_Server();
    private final String CONTENT_TYPE = "application/json";

    public EGstApiCall() {
    }

    public EGstToken_Mdl getAuthToken(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, int actionCode) throws Exception {
        EGstToken_Mdl egstToken_Mdl = new EGstToken_Mdl();
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.GET, CONTENT_TYPE, (String) null, hmHeaderList)) {
                String jsonResponse = response.body().string();
                if (response.isSuccessful()) {
                    egstToken_Mdl = new Gson().fromJson(jsonResponse, EGstToken_Mdl.class);
                } else {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResponse, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return egstToken_Mdl;
    }

    public Eway_Mdl genrateEWayBill(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, String jsonData, int actionCode) throws Exception {
        Eway_Mdl ewayDetails = new Eway_Mdl();
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.POST, CONTENT_TYPE, jsonData, hmHeaderList)) {
                String jsonResult = response.body().string();
                if (response.isSuccessful()) {
                    ewayDetails = new Gson().fromJson(jsonResult, Eway_Mdl.class);
                } else {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return ewayDetails;
    }

    public EInvoice_Mdl genrateEInvoice(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, String jsonData, int actionCode) throws Exception {
        EInvoice_Mdl einvDetails = new EInvoice_Mdl();
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.POST, CONTENT_TYPE, jsonData, hmHeaderList)) {
                String jsonResult = response.body().string();
                if (response.isSuccessful()) {
                    ObjectMapper om = new ObjectMapper();
                    JSONObject json = new JSONObject(jsonResult);
                    einvDetails = om.readValue(json.toString(), EInvoice_Mdl.class);
                    System.err.println("");
                } else {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
                if (einvDetails.getErrorDetails() != null) {
                    String errorMsg = "";
                    int count = 0;
                    for (ErrorDetails_Mdl errorDetails : einvDetails.getErrorDetails()) {
                        errorMsg += ++count + ". " + errorDetails.getMessage() + "! </br>";
                    }
                    throw new Exception(DmShared.xNull(errorMsg));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return einvDetails;
    }

    public EWayViaIrnResponse_Mdl genrateEWayBillViaIRN(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, String jsonData, int actionCode, String irn) throws Exception {
        EWayViaIrnResponse_Mdl eWayViaIrnResponse_Mdl = new EWayViaIrnResponse_Mdl();
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.POST, CONTENT_TYPE, jsonData, hmHeaderList)) {
                String jsonResult = response.body().string();

                if (response.isSuccessful()) {
                    ObjectMapper om = new ObjectMapper();
                    JSONObject json = new JSONObject(jsonResult);
                    eWayViaIrnResponse_Mdl = om.readValue(json.toString(), EWayViaIrnResponse_Mdl.class);
                } else {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return eWayViaIrnResponse_Mdl;
    }

    public String getEWayBillDetail(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, String ewayNo) throws Exception {
        String jsonResult = "";
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, DmEnum.EGST_ACTION.E_WAY_GET_BILL.getCode());
            tokenUrl = tokenUrl.concat(DmEnum.EGST_URL_FORMAT_TAG.EWAY_NO.getValue() + ewayNo);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.GET, CONTENT_TYPE, (String) null, hmHeaderList)) {
                jsonResult = response.body().string();
                if (!response.isSuccessful()) {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return jsonResult;
    }

    public EwayCancelResponse_Mdl cancelEWayBill(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, int actionCode, String jsonData) throws Exception {
        EwayCancelResponse_Mdl cancelResponse_Mdl = new EwayCancelResponse_Mdl();
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.POST, CONTENT_TYPE, jsonData, hmHeaderList)) {
                String jsonResult = response.body().string();
                if (response.isSuccessful()) {
                    cancelResponse_Mdl = new Gson().fromJson(jsonResult, EwayCancelResponse_Mdl.class);
                } else {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return cancelResponse_Mdl;
    }

    public EInvCancelResponse_Mdl cancelEinvIRN(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, int actionCode, String jsonData) throws Exception {
        EInvCancelResponse_Mdl respPlCancelIRN = new EInvCancelResponse_Mdl();
        try {
            String tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.POST, CONTENT_TYPE, jsonData, hmHeaderList)) {
                String jsonResult = response.body().string();
                if (response.isSuccessful()) {
                    respPlCancelIRN = new ObjectMapper().readValue(jsonResult, EInvCancelResponse_Mdl.class);;
                } else {
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return respPlCancelIRN;
    }

    public byte[] printEWayBill(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, String jsonData, int actionCode) throws Exception {
        byte[] pdfFileResp = null;
        try {
            String tokenUrl = "";
            tokenUrl = getURL(eGSTParam_Mdl, eGstToken_Mdl, actionCode);
            HashMap<String, String> hmHeaderList = new HashMap<>();
            hmHeaderList.put("Content-Type", CONTENT_TYPE);
            try (Response response = dmApiBase.startProcessApiRequest(tokenUrl, DmApiBase_Server.HTTP_METHOD.POST, CONTENT_TYPE, jsonData, hmHeaderList)) {
                if (response.isSuccessful()) {
                    pdfFileResp = response.body().bytes();
                } else {
                    String jsonResult = response.body().string();
                    Error_Mdl errorMdl = new Gson().fromJson(jsonResult, Error_Mdl.class);
                    throw new Exception(DmShared.xNull(errorMdl.getError().getMessage()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return pdfFileResp;
    }

    private String getURL(EGstParam_Mdl eGSTParam_Mdl, EGstToken_Mdl eGstToken_Mdl, int actionCode) throws Exception {
        String url = "", format = "", baseUrl = "", folder = "", version = "", apiName = "", urlActionCode = "", actionName = "";
        try {
            //<editor-fold defaultstate="collapsed" desc="Initialize Paramters">
            if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_AUTHENTICATION.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_AUTHENTICATION.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEway_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEway_AuthFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEway_AuthFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEway_AuthApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEway_AuthApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEway_AuthAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_GENRATE_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_GENRATE_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEway_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEway_UrlFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEway_BillFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEway_BillApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEway_BillApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEway_GenerateAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_GET_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_GET_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEway_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEway_UrlFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEway_BillFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEway_BillApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEway_BillApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEway_getDetailAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_CANCEL_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEway_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEway_UrlFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEway_BillFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEway_BillApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEway_BillApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEway_CancelAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_PRINT_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_PRINT_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEway_BaseUrl());
                if (Gst_Server.isSandBoxTesting) {
                    baseUrl = "https://einvapi.charteredinfo.com";
                }
                format = DmShared.xNull(eGSTParam_Mdl.getEway_PrintFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEway_BillFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEway_PrintApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEway_PrintApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEway_PrintAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_PRINT_DETAILED_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_PRINT_DETAILED_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEway_BaseUrl());
                if (Gst_Server.isSandBoxTesting) {
                    baseUrl = "https://einvapi.charteredinfo.com";
                }
                format = DmShared.xNull(eGSTParam_Mdl.getEway_PrintFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEway_BillFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEway_PrintApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEway_PrintApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEway_DetailePrintAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_WAY_GENERATE_VIA_IRN.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_WAY_GENERATE_VIA_IRN.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_eWayViaIrnUrlFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_eWayViaIrnApiFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_eWayViaIrnApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_eWayViaIrnApiName());

            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_INV_AUTHENTICATION.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_INV_AUTHENTICATION.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_AuthFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_AuthFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_AuthApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_AuthApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEinv_AuthAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_INV_GENRATE_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_UrlFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_ApiFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_BillApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_BillApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEinv_GenerateAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_INV_GET_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_INV_GET_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_UrlFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_ApiFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_BillApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_BillApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEinv_getDetailAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_INV_CANCEL_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_CancelFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_ApiFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_BillApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_BillApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEinv_CancelAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_INV_PRINT_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_INV_PRINT_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                if (Gst_Server.isSandBoxTesting) {
                    baseUrl = "https://einvapi.charteredinfo.com";
                }
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_ApiFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintAction());
            } else if (DmShared.iNull(actionCode).intValue() == DmEnum.EGST_ACTION.E_INV_PRINT_DETAILED_BILL.getCode()) {
                actionName = DmEnum.EGST_ACTION.E_INV_PRINT_DETAILED_BILL.getName();
                baseUrl = DmShared.xNull(eGSTParam_Mdl.getEinv_BaseUrl());
                if (Gst_Server.isSandBoxTesting) {
                    baseUrl = "https://einvapi.charteredinfo.com";
                }
                format = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintFormat());
                folder = DmShared.xNull(eGSTParam_Mdl.getEinv_ApiFolderName());
                version = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintApiVersion());
                apiName = DmShared.xNull(eGSTParam_Mdl.getEinv_PrintApiName());
                urlActionCode = DmShared.xNull(eGSTParam_Mdl.getEinv_DetailedPrintAction());
            }

//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Validate Parameters">
            if (!DmShared.xNull(format).trim().equalsIgnoreCase("")) {
                url = DmShared.xNull(format);
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.BASE_URL.getValue())) {
                    if (!DmShared.xNull(baseUrl).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.BASE_URL.getValue(), baseUrl);
                    } else {
                        throw new Exception(actionName + " Base URL is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.FOLDER.getValue())) {
                    if (!DmShared.xNull(folder).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.FOLDER.getValue(), folder);
                    } else {
                        throw new Exception(actionName + " Folder is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.VERSION.getValue())) {
                    if (!DmShared.xNull(version).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.VERSION.getValue(), version);
                    } else {
                        throw new Exception(actionName + " Version is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.API_NAME.getValue())) {
                    if (!DmShared.xNull(apiName).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.API_NAME.getValue(), apiName);
                    } else {
                        throw new Exception(actionName + " Api Name is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.ACTION.getValue())) {
                    if (!DmShared.xNull(urlActionCode).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.ACTION.getValue(), urlActionCode);
                    } else {
                        throw new Exception(actionName + " Action is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.ASP_ID.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getEway_AspUserId()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.ASP_ID.getValue(), DmShared.xNull(eGSTParam_Mdl.getEway_AspUserId()));
                    } else {
                        throw new Exception(actionName + " AspUserId is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.ASP_PASSWORD.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getEway_AspPassword()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.ASP_PASSWORD.getValue(), DmShared.xNull(eGSTParam_Mdl.getEway_AspPassword()));
                    } else {
                        throw new Exception(actionName + " AspPassword is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.GSTIN.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getUserGstin()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.GSTIN.getValue(), DmShared.xNull(eGSTParam_Mdl.getUserGstin()));
                    } else {
                        throw new Exception(actionName + " GSTIN is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.GSP_USER_NAME.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getEway_UserName()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.GSP_USER_NAME.getValue(), DmShared.xNull(eGSTParam_Mdl.getEway_UserName()));
                    } else {
                        throw new Exception(actionName + " User Name is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.GSP_PASSWORD.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getEway_UserPassword()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.GSP_PASSWORD.getValue(), DmShared.xNull(eGSTParam_Mdl.getEway_UserPassword()));
                    } else {
                        throw new Exception(actionName + " Password is mandatory");
                    }
                }
//                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.TAXPRO_PASSWORD_EINV.getValue())) {
//                    if (!DmShared.xNull(eGSTParam_Mdl.getEway_UserPassword()).trim().equalsIgnoreCase("")) {
//                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.TAXPRO_PASSWORD_EINV.getValue(), DmShared.xNull(eGSTParam_Mdl.getEway_UserPassword()));
//                    } else {
//                        throw new Exception(actionName + " Password is mandatory");
//                    }
//                }

                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.AUTH_TOKEN.getValue())) {
                    if (eGstToken_Mdl != null) {
                        if (!DmShared.xNull(eGstToken_Mdl.getAuthtoken()).trim().equalsIgnoreCase("")) {
                            url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.AUTH_TOKEN.getValue(), DmShared.xNull(eGstToken_Mdl.getAuthtoken()));
                        } else {
                            throw new Exception(actionName + "Auth. Token is mandatory. Please refresh token!");
                        }
                    } else {
                        throw new Exception(actionName + "Please refresh Token !");
                    }
                }

                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.QR_CODE_SIZE.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getEinv_qrCodeSize()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.QR_CODE_SIZE.getValue(), DmShared.xNull(eGSTParam_Mdl.getEinv_qrCodeSize()));
                    } else {
                        throw new Exception(actionName + " QR code size is mandatory");
                    }
                }
                if (url.contains(DmEnum.EGST_URL_FORMAT_TAG.PARSE_IRN_RESP.getValue())) {
                    if (!DmShared.xNull(eGSTParam_Mdl.getEinv_parseIrnResp()).trim().equalsIgnoreCase("")) {
                        url = url.replace(DmEnum.EGST_URL_FORMAT_TAG.PARSE_IRN_RESP.getValue(), DmShared.xNull(eGSTParam_Mdl.getEinv_parseIrnResp()));
                    } else {
                        throw new Exception(actionName + " Parse in Response is mandatory");
                    }
                }
            } else {
                throw new Exception(actionName + " format is mandatory");
            }
//</editor-fold>
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return url;
    }

}
