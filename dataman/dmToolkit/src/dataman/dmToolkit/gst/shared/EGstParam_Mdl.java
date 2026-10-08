package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyFieldExtended;
import java.io.Serializable;
import java.util.LinkedList;

public class EGstParam_Mdl implements Serializable, IsSerializable {

    private String userGstin;
    //<editor-fold defaultstate="collapsed" desc="e-way">
    private String eway_UserName;
    private String eway_UserPassword;
    private String eway_GSPName;
    private String eway_AspUserId;
    private String eway_AspPassword;
    private String eway_IrpCode;
    private String eway_IrpName;
    private String eway_BaseUrl;
    private String eway_ClientId;
    private String eway_ClientSecret;
    private String eway_GSPUserID;
    //<editor-fold defaultstate="collapsed" desc="eGst format">
    private String eway_UrlFormat;
    private String eway_PrintFormat;
    private String eway_AuthFormat;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Folder">
    private String eway_AuthFolderName;
    private String eway_BillFolderName;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Api Name">
    private String eway_AuthApiName;
    private String eway_BillApiName;
    private String eway_PrintApiName;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Action">
    private String eway_AuthAction;
    private String eway_GenerateAction;
    private String eway_CancelAction;
    private String eway_PrintAction;
    private String eway_DetailePrintAction;
    private String eway_getDetailAction;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Version">
    private String eway_AuthApiVersion;
    private String eway_BillApiVersion;
    private String eway_PrintApiVersion;
    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="e-Inv">
    private String einv_UserName;
    private String einv_UserPassword;
    private String einv_IrpCode;
    private String einv_IrpName;
    private String einv_BaseUrl;
    private String einv_GSPName;
    private String einv_ASPID;
    private String einv_ASPID_Password;
    private String einv_UrlFormat;
    private String einv_AuthFormat;
    private String einv_PrintFormat;
    private String einv_CancelFormat;
    private String einv_AuthFolderName;
    private String einv_ApiFolderName;
    private String einv_AuthApiName;
    private String einv_BillApiName;
    private String einv_PrintApiName;
    private String einv_AuthApiVersion;
    private String einv_BillApiVersion;
    private String einv_PrintApiVersion;
    private String einv_AuthAction;
    private String einv_GenerateAction;
    private String einv_CancelAction;
    private String einv_PrintAction;
    private String einv_DetailedPrintAction;
    private String einv_getDetailAction;
    private String einv_qrCodeSize;
    private String einv_parseIrnResp;
    private String einv_eWayViaIrnUrlFormat;
    private String einv_eWayViaIrnApiName;
    private String einv_eWayViaIrnApiFolderName;
    private String einv_eWayViaIrnApiVersion;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="STP: Search Tax Payer">
    private String stp_Url;
    private LinkedList<KeyFieldExtended> stp_UrlParam = new LinkedList<>();
//</editor-fold>
    private String userDateTime;

    public EGstParam_Mdl() {
    }

    public String getUserGstin() {
        return userGstin;
    }

    public void setUserGstin(String userGstin) {
        this.userGstin = userGstin;
    }

    public String getEway_UserName() {
        return eway_UserName;
    }

    public void setEway_UserName(String eway_UserName) {
        this.eway_UserName = eway_UserName;
    }

    public String getEway_UserPassword() {
        return eway_UserPassword;
    }

    public void setEway_UserPassword(String eway_UserPassword) {
        this.eway_UserPassword = eway_UserPassword;
    }

    public String getEway_GSPName() {
        return eway_GSPName;
    }

    public void setEway_GSPName(String eway_GSPName) {
        this.eway_GSPName = eway_GSPName;
    }

    public String getEway_AspUserId() {
        return eway_AspUserId;
    }

    public void setEway_AspUserId(String eway_AspUserId) {
        this.eway_AspUserId = eway_AspUserId;
    }

    public String getEway_AspPassword() {
        return eway_AspPassword;
    }

    public void setEway_AspPassword(String eway_AspPassword) {
        this.eway_AspPassword = eway_AspPassword;
    }

    public String getEway_ClientId() {
        return eway_ClientId;
    }

    public void setEway_ClientId(String eway_ClientId) {
        this.eway_ClientId = eway_ClientId;
    }

    public String getEway_ClientSecret() {
        return eway_ClientSecret;
    }

    public void setEway_ClientSecret(String eway_ClientSecret) {
        this.eway_ClientSecret = eway_ClientSecret;
    }

    public String getEway_GSPUserID() {
        return eway_GSPUserID;
    }

    public void setEway_GSPUserID(String eway_GSPUserID) {
        this.eway_GSPUserID = eway_GSPUserID;
    }

    public String getEway_IrpCode() {
        return eway_IrpCode;
    }

    public void setEway_IrpCode(String eway_IrpCode) {
        this.eway_IrpCode = eway_IrpCode;
    }

    public String getEway_IrpName() {
        return eway_IrpName;
    }

    public void setEway_IrpName(String eway_IrpName) {
        this.eway_IrpName = eway_IrpName;
    }

    public String getEway_BaseUrl() {
        return eway_BaseUrl;
    }

    public void setEway_BaseUrl(String eway_BaseUrl) {
        this.eway_BaseUrl = eway_BaseUrl;
    }

    public String getEway_UrlFormat() {
        return eway_UrlFormat;
    }

    public void setEway_UrlFormat(String eway_UrlFormat) {
        this.eway_UrlFormat = eway_UrlFormat;
    }

    public String getEway_AuthApiName() {
        return eway_AuthApiName;
    }

    public void setEway_AuthApiName(String eway_AuthApiName) {
        this.eway_AuthApiName = eway_AuthApiName;
    }

    public String getEway_BillApiName() {
        return eway_BillApiName;
    }

    public void setEway_BillApiName(String eway_BillApiName) {
        this.eway_BillApiName = eway_BillApiName;
    }

    public String getEway_AuthAction() {
        return eway_AuthAction;
    }

    public void setEway_AuthAction(String eway_AuthAction) {
        this.eway_AuthAction = eway_AuthAction;
    }

    public String getEway_AuthFormat() {
        return eway_AuthFormat;
    }

    public void setEway_AuthFormat(String eway_AuthFormat) {
        this.eway_AuthFormat = eway_AuthFormat;
    }

    public String getEway_PrintApiName() {
        return eway_PrintApiName;
    }

    public void setEway_PrintApiName(String eway_PrintApiName) {
        this.eway_PrintApiName = eway_PrintApiName;
    }

    public String getEway_PrintAction() {
        return eway_PrintAction;
    }

    public void setEway_PrintAction(String eway_PrintAction) {
        this.eway_PrintAction = eway_PrintAction;
    }

    public String getEway_DetailePrintAction() {
        return eway_DetailePrintAction;
    }

    public void setEway_DetailePrintAction(String eway_DetailePrintAction) {
        this.eway_DetailePrintAction = eway_DetailePrintAction;
    }

    public String getEway_GenerateAction() {
        return eway_GenerateAction;
    }

    public void setEway_GenerateAction(String eway_GenerateAction) {
        this.eway_GenerateAction = eway_GenerateAction;
    }

    public String getEway_CancelAction() {
        return eway_CancelAction;
    }

    public void setEway_CancelAction(String eway_CancelAction) {
        this.eway_CancelAction = eway_CancelAction;
    }

    public String getEway_PrintFormat() {
        return eway_PrintFormat;
    }

    public void setEway_PrintFormat(String eway_PrintFormat) {
        this.eway_PrintFormat = eway_PrintFormat;
    }

    public String getEway_AuthFolderName() {
        return eway_AuthFolderName;
    }

    public void setEway_AuthFolderName(String eway_AuthFolderName) {
        this.eway_AuthFolderName = eway_AuthFolderName;
    }

    public String getEway_BillFolderName() {
        return eway_BillFolderName;
    }

    public void setEway_BillFolderName(String eway_BillFolderName) {
        this.eway_BillFolderName = eway_BillFolderName;
    }

    public String getEway_getDetailAction() {
        return eway_getDetailAction;
    }

    public void setEway_getDetailAction(String eway_getDetailAction) {
        this.eway_getDetailAction = eway_getDetailAction;
    }

    public String getEway_AuthApiVersion() {
        return eway_AuthApiVersion;
    }

    public void setEway_AuthApiVersion(String eway_AuthApiVersion) {
        this.eway_AuthApiVersion = eway_AuthApiVersion;
    }

    public String getEway_BillApiVersion() {
        return eway_BillApiVersion;
    }

    public void setEway_BillApiVersion(String eway_BillApiVersion) {
        this.eway_BillApiVersion = eway_BillApiVersion;
    }

    public String getEway_PrintApiVersion() {
        return eway_PrintApiVersion;
    }

    public void setEway_PrintApiVersion(String eway_PrintApiVersion) {
        this.eway_PrintApiVersion = eway_PrintApiVersion;
    }

    public String getEinv_UserName() {
        return einv_UserName;
    }

    public void setEinv_UserName(String einv_UserName) {
        this.einv_UserName = einv_UserName;
    }

    public String getEinv_UserPassword() {
        return einv_UserPassword;
    }

    public void setEinv_UserPassword(String einv_UserPassword) {
        this.einv_UserPassword = einv_UserPassword;
    }

    public String getEinv_IrpCode() {
        return einv_IrpCode;
    }

    public void setEinv_IrpCode(String einv_IrpCode) {
        this.einv_IrpCode = einv_IrpCode;
    }

    public String getEinv_IrpName() {
        return einv_IrpName;
    }

    public void setEinv_IrpName(String einv_IrpName) {
        this.einv_IrpName = einv_IrpName;
    }

    public String getEinv_BaseUrl() {
        return einv_BaseUrl;
    }

    public void setEinv_BaseUrl(String einv_BaseUrl) {
        this.einv_BaseUrl = einv_BaseUrl;
    }

    public String getEinv_GSPName() {
        return einv_GSPName;
    }

    public void setEinv_GSPName(String einv_GSPName) {
        this.einv_GSPName = einv_GSPName;
    }

    public String getEinv_ASPID() {
        return einv_ASPID;
    }

    public void setEinv_ASPID(String einv_ASPID) {
        this.einv_ASPID = einv_ASPID;
    }

    public String getEinv_ASPID_Password() {
        return einv_ASPID_Password;
    }

    public void setEinv_ASPID_Password(String einv_ASPID_Password) {
        this.einv_ASPID_Password = einv_ASPID_Password;
    }

    public String getEinv_UrlFormat() {
        return einv_UrlFormat;
    }

    public void setEinv_UrlFormat(String einv_UrlFormat) {
        this.einv_UrlFormat = einv_UrlFormat;
    }

    public String getEinv_AuthFormat() {
        return einv_AuthFormat;
    }

    public void setEinv_AuthFormat(String einv_AuthFormat) {
        this.einv_AuthFormat = einv_AuthFormat;
    }

    public String getEinv_PrintFormat() {
        return einv_PrintFormat;
    }

    public void setEinv_PrintFormat(String einv_PrintFormat) {
        this.einv_PrintFormat = einv_PrintFormat;
    }

    public String getEinv_AuthFolderName() {
        return einv_AuthFolderName;
    }

    public void setEinv_AuthFolderName(String einv_AuthFolderName) {
        this.einv_AuthFolderName = einv_AuthFolderName;
    }

    public String getEinv_ApiFolderName() {
        return einv_ApiFolderName;
    }

    public void setEinv_ApiFolderName(String einv_ApiFolderName) {
        this.einv_ApiFolderName = einv_ApiFolderName;
    }

    public String getEinv_AuthApiName() {
        return einv_AuthApiName;
    }

    public void setEinv_AuthApiName(String einv_AuthApiName) {
        this.einv_AuthApiName = einv_AuthApiName;
    }

    public String getEinv_BillApiName() {
        return einv_BillApiName;
    }

    public void setEinv_BillApiName(String einv_BillApiName) {
        this.einv_BillApiName = einv_BillApiName;
    }

    public String getEinv_PrintApiName() {
        return einv_PrintApiName;
    }

    public void setEinv_PrintApiName(String einv_PrintApiName) {
        this.einv_PrintApiName = einv_PrintApiName;
    }

    public String getEinv_AuthApiVersion() {
        return einv_AuthApiVersion;
    }

    public void setEinv_AuthApiVersion(String einv_AuthApiVersion) {
        this.einv_AuthApiVersion = einv_AuthApiVersion;
    }

    public String getEinv_BillApiVersion() {
        return einv_BillApiVersion;
    }

    public void setEinv_BillApiVersion(String einv_BillApiVersion) {
        this.einv_BillApiVersion = einv_BillApiVersion;
    }

    public String getEinv_PrintApiVersion() {
        return einv_PrintApiVersion;
    }

    public void setEinv_PrintApiVersion(String einv_PrintApiVersion) {
        this.einv_PrintApiVersion = einv_PrintApiVersion;
    }

    public String getEinv_AuthAction() {
        return einv_AuthAction;
    }

    public void setEinv_AuthAction(String einv_AuthAction) {
        this.einv_AuthAction = einv_AuthAction;
    }

    public String getEinv_GenerateAction() {
        return einv_GenerateAction;
    }

    public void setEinv_GenerateAction(String einv_GenerateAction) {
        this.einv_GenerateAction = einv_GenerateAction;
    }

    public String getEinv_CancelAction() {
        return einv_CancelAction;
    }

    public void setEinv_CancelAction(String einv_CancelAction) {
        this.einv_CancelAction = einv_CancelAction;
    }

    public String getEinv_PrintAction() {
        return einv_PrintAction;
    }

    public void setEinv_PrintAction(String einv_PrintAction) {
        this.einv_PrintAction = einv_PrintAction;
    }

    public String getEinv_DetailedPrintAction() {
        return einv_DetailedPrintAction;
    }

    public void setEinv_DetailedPrintAction(String einv_DetailedPrintAction) {
        this.einv_DetailedPrintAction = einv_DetailedPrintAction;
    }

    public String getEinv_getDetailAction() {
        return einv_getDetailAction;
    }

    public void setEinv_getDetailAction(String einv_getDetailAction) {
        this.einv_getDetailAction = einv_getDetailAction;
    }

    public String getEinv_qrCodeSize() {
        return einv_qrCodeSize;
    }

    public void setEinv_qrCodeSize(String einv_qrCodeSize) {
        this.einv_qrCodeSize = einv_qrCodeSize;
    }

    public String getEinv_parseIrnResp() {
        return einv_parseIrnResp;
    }

    public void setEinv_parseIrnResp(String einv_parseIrnResp) {
        this.einv_parseIrnResp = einv_parseIrnResp;
    }

    public String getUserDateTime() {
        return userDateTime;
    }

    public void setUserDateTime(String userDateTime) {
        this.userDateTime = userDateTime;
    }

    public String getEinv_CancelFormat() {
        return einv_CancelFormat;
    }

    public void setEinv_CancelFormat(String einv_CancelFormat) {
        this.einv_CancelFormat = einv_CancelFormat;
    }

    public String getEinv_eWayViaIrnUrlFormat() {
        return einv_eWayViaIrnUrlFormat;
    }

    public void setEinv_eWayViaIrnUrlFormat(String einv_eWayViaIrnUrlFormat) {
        this.einv_eWayViaIrnUrlFormat = einv_eWayViaIrnUrlFormat;
    }

    public String getEinv_eWayViaIrnApiName() {
        return einv_eWayViaIrnApiName;
    }

    public void setEinv_eWayViaIrnApiName(String einv_eWayViaIrnApiName) {
        this.einv_eWayViaIrnApiName = einv_eWayViaIrnApiName;
    }

    public String getEinv_eWayViaIrnApiFolderName() {
        return einv_eWayViaIrnApiFolderName;
    }

    public void setEinv_eWayViaIrnApiFolderName(String einv_eWayViaIrnApiFolderName) {
        this.einv_eWayViaIrnApiFolderName = einv_eWayViaIrnApiFolderName;
    }

    public String getEinv_eWayViaIrnApiVersion() {
        return einv_eWayViaIrnApiVersion;
    }

    public void setEinv_eWayViaIrnApiVersion(String einv_eWayViaIrnApiVersion) {
        this.einv_eWayViaIrnApiVersion = einv_eWayViaIrnApiVersion;
    }

    public String getStp_Url() {
        return stp_Url;
    }

    public void setStp_Url(String stp_Url) {
        this.stp_Url = stp_Url;
    }

    public LinkedList<KeyFieldExtended> getStp_UrlParam() {
        return stp_UrlParam;
    }
}
