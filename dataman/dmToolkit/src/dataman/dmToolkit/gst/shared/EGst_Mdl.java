package dataman.dmToolkit.gst.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;

public class EGst_Mdl implements Serializable, IsSerializable {

    private String docid;
    private String mongoLinkId;
    private String v_Type;
    private String v_Prefix;
    private String v_Date;
    private String site_Code;
    private RecId recId;
    private String gstType;
    private String partyType;
    private String locationCode;
    private String posStateNumericCode;
    private String posStateName;
    private String ewaySupplyTypeCode;
    private String ewaySupplyTypeName;
    private String ewaySubSupplyTypeCode;
    private String ewaySubSupplyTypeName;
    private String ewaySubSupplyDesc;
    private String ewayDocTypeCode;
    private String ewayDocTypeName;
    private String ewayTransTypeCode;
    private String ewayTransTypeName;
    private String actFromStateNumericCode;
    private String actToStateNumericCode;
    private String einv_version;
    private String taxSchemeCode;
    private String taxSchemeName;
    private String eComGstin;
    private String einvSupplyTypeCode;
    private String einvSupplyTypeName;
    private String einvDocTypeCode;
    private String einvDocTypeName;
    private String reverseCharge;
    private String igstOnIntra;
    private String u_EntDt_LatestLine;

    private Party_Mdl consigner_Mdl = new Party_Mdl();
    private Party_Mdl billTo_Mdl = new Party_Mdl();
    private Party_Mdl consignee_Mdl = new Party_Mdl();
    private Party_Mdl dispatchFrom_Mdl = new Party_Mdl();
    private Party_Mdl shipTo_Mdl = new Party_Mdl();
    private Party_Mdl transporter_Mdl = new Party_Mdl();

    //<editor-fold defaultstate="collapsed" desc="Bill & Tax Info">
    private String otherAmount;
    private String taxableValue;
    private String cgstValue;
    private String sgstValue;
    private String igstValue;
    private String cessValue;
    private String cessNonAdvolValue;
    private String stateCessAmount;
    private String roundOfAmount;
    private String netInvAmount;
    private String discountAmount;
    private String totalInvAmtInFc;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Transporter Detail">
    private String tt_u_EntDt_LatestLine;
    private boolean tpBillGenrated;
    private String transportLrNo;
    private String totInvValue;
    private String transportModeCode;
    private String transportModeName;
    private String transDistance;
    private String transportLrDate;
    private String vehicleNo;
    private String vehicleTypeCode;
    private String vehicleTypeName;
    private String gstVehicleType;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Response Detail">
    private String eway_BillNo;
    private String eway_BillDate;
    private String eway_validUpto;
    private String eway_alert;
    private String eway_cancelRsnCode;
    private String eway_cancelRmrk;
    private String eway_cancelDate;
    private String eway_Status;
    private String eway_GenerationMode;
    private String einv_cancelRsnCode;
    private String einv_cancelRmrk;
    private String einv_cancelDate;
    private String einv_Status;
    private String einv_IrnNo;
    private String einv_ackNo;
    private String einv_ackDt;
    private String einv_JwtIssuer;

    //get From Mongo
    private String einv_SignedQrCode;
    private String einv_SignedInvoice;
    private String einv_SignedQrCodeImage;
    private String einv_ExtractedSignedInvoiceDataJson;
    private String einv_ExtractedSignedQrCodeJson;

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Other Detail">
    private boolean ewayBillRequired;
    private boolean einvoiceRequired;
    private boolean siteTransaction;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RefDetails">
    private String refRemark;
    //<editor-fold defaultstate="collapsed" desc="RefDetails-->PrecDocument">
    private String refDocid;
    private String refRecId;
    private String refV_Date;
    private String refDocOtherRef;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RefDetails-->DocPerdDetails">
    private String refInvDocStartDate;
    private String refInvDocEndDate;
//</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ExportDetails">
    private String shippingBillNo;
    private String shippingBillDate;
    private String currencyCode;
    private String portCode;
    private String refundClaim;
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PayeeDetails">
    private String payee_Name;
    private String payee_AccountNo;
    private String payee_ModeOfPayment;
    private String payee_BranchCode;
    private String payee_CrTrn;
    private String payee_PaymentInstruction;
    private String payee_PaymentTerms;
    private String payee_DirectDebit;
    private String payee_CreditDays;
    private String payee_PaidAmount;
    private String payee_PaymentDue;
//</editor-fold>

    private ArrayList<EinvAdditionalDocDtls_Mdl> addDocList = new ArrayList<EinvAdditionalDocDtls_Mdl>();
    private ArrayList<EinvRefContractDtls_Mdl> ContractDetails = new ArrayList<EinvRefContractDtls_Mdl>();
    private ArrayList<EGstDetail_Mdl> itemList = new ArrayList<EGstDetail_Mdl>();

    public EGst_Mdl() {
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getV_Type() {
        return v_Type;
    }

    public void setV_Type(String v_Type) {
        this.v_Type = v_Type;
    }

    public String getSite_Code() {
        return site_Code;
    }

    public void setSite_Code(String site_Code) {
        this.site_Code = site_Code;
    }

    public String getEwaySupplyTypeCode() {
        return ewaySupplyTypeCode;
    }

    public void setEwaySupplyTypeCode(String ewaySupplyTypeCode) {
        this.ewaySupplyTypeCode = ewaySupplyTypeCode;
    }

    public String getEwaySubSupplyTypeCode() {
        return ewaySubSupplyTypeCode;
    }

    public void setEwaySubSupplyTypeCode(String ewaySubSupplyTypeCode) {
        this.ewaySubSupplyTypeCode = ewaySubSupplyTypeCode;
    }

    public String getEwaySubSupplyTypeName() {
        return ewaySubSupplyTypeName;
    }

    public void setEwaySubSupplyTypeName(String ewaySubSupplyTypeName) {
        this.ewaySubSupplyTypeName = ewaySubSupplyTypeName;
    }

    public String getEwaySubSupplyDesc() {
        return ewaySubSupplyDesc;
    }

    public void setEwaySubSupplyDesc(String ewaySubSupplyDesc) {
        this.ewaySubSupplyDesc = ewaySubSupplyDesc;
    }

    public String getEwayDocTypeCode() {
        return ewayDocTypeCode;
    }

    public void setEwayDocTypeCode(String ewayDocTypeCode) {
        this.ewayDocTypeCode = ewayDocTypeCode;
    }

    public String getEwayDocTypeName() {
        return ewayDocTypeName;
    }

    public void setEwayDocTypeName(String ewayDocTypeName) {
        this.ewayDocTypeName = ewayDocTypeName;
    }

    public String getV_Date() {
        return v_Date;
    }

    public void setV_Date(String v_Date) {
        this.v_Date = v_Date;
    }

    public String getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(String otherAmount) {
        this.otherAmount = otherAmount;
    }

    public String getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(String taxableValue) {
        this.taxableValue = taxableValue;
    }

    public String getCgstValue() {
        return cgstValue;
    }

    public void setCgstValue(String cgstValue) {
        this.cgstValue = cgstValue;
    }

    public String getSgstValue() {
        return sgstValue;
    }

    public void setSgstValue(String sgstValue) {
        this.sgstValue = sgstValue;
    }

    public String getIgstValue() {
        return igstValue;
    }

    public void setIgstValue(String igstValue) {
        this.igstValue = igstValue;
    }

    public String getCessValue() {
        return cessValue;
    }

    public void setCessValue(String cessValue) {
        this.cessValue = cessValue;
    }

    public String getCessNonAdvolValue() {
        return cessNonAdvolValue;
    }

    public void setCessNonAdvolValue(String cessNonAdvolValue) {
        this.cessNonAdvolValue = cessNonAdvolValue;
    }

    public String getTransportLrNo() {
        return transportLrNo;
    }

    public void setTransportLrNo(String transportLrNo) {
        this.transportLrNo = transportLrNo;
    }

    public String getTotInvValue() {
        return totInvValue;
    }

    public void setTotInvValue(String totInvValue) {
        this.totInvValue = totInvValue;
    }

    public String getTransportModeCode() {
        return transportModeCode;
    }

    public void setTransportModeCode(String transportModeCode) {
        this.transportModeCode = transportModeCode;
    }

    public String getTransDistance() {
        return transDistance;
    }

    public void setTransDistance(String transDistance) {
        this.transDistance = transDistance;
    }

    public String getTransportLrDate() {
        return transportLrDate;
    }

    public void setTransportLrDate(String transportLrDate) {
        this.transportLrDate = transportLrDate;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode(String vehicleTypeCode) {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public ArrayList<EGstDetail_Mdl> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<EGstDetail_Mdl> itemList) {
        this.itemList = itemList;
    }

    public Party_Mdl getConsigner_Mdl() {
        return consigner_Mdl;
    }

    public void setConsigner_Mdl(Party_Mdl consigner_Mdl) {
        this.consigner_Mdl = consigner_Mdl;
    }

    public Party_Mdl getBillTo_Mdl() {
        return billTo_Mdl;
    }

    public void setBillTo_Mdl(Party_Mdl billTo_Mdl) {
        this.billTo_Mdl = billTo_Mdl;
    }

    public Party_Mdl getConsignee_Mdl() {
        return consignee_Mdl;
    }

    public void setConsignee_Mdl(Party_Mdl consignee_Mdl) {
        this.consignee_Mdl = consignee_Mdl;
    }

    public Party_Mdl getDispatchFrom_Mdl() {
        return dispatchFrom_Mdl;
    }

    public void setDispatchFrom_Mdl(Party_Mdl dispatchFrom_Mdl) {
        this.dispatchFrom_Mdl = dispatchFrom_Mdl;
    }

    public Party_Mdl getShipTo_Mdl() {
        return shipTo_Mdl;
    }

    public void setShipTo_Mdl(Party_Mdl shipTo_Mdl) {
        this.shipTo_Mdl = shipTo_Mdl;
    }

    public RecId getRecId() {
        return recId;
    }

    public void setRecId(RecId recId) {
        this.recId = recId;
    }

    public String getTransportModeName() {
        return transportModeName;
    }

    public void setTransportModeName(String transportModeName) {
        this.transportModeName = transportModeName;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getEwaySupplyTypeName() {
        return ewaySupplyTypeName;
    }

    public void setEwaySupplyTypeName(String ewaySupplyTypeName) {
        this.ewaySupplyTypeName = ewaySupplyTypeName;
    }

    public String getEwayTransTypeCode() {
        return ewayTransTypeCode;
    }

    public void setEwayTransTypeCode(String ewayTransTypeCode) {
        this.ewayTransTypeCode = ewayTransTypeCode;
    }

    public String getEwayTransTypeName() {
        return ewayTransTypeName;
    }

    public void setEwayTransTypeName(String ewayTransTypeName) {
        this.ewayTransTypeName = ewayTransTypeName;
    }

    public String getEinvSupplyTypeCode() {
        return einvSupplyTypeCode;
    }

    public void setEinvSupplyTypeCode(String einvSupplyTypeCode) {
        this.einvSupplyTypeCode = einvSupplyTypeCode;
    }

    public String getEinvSupplyTypeName() {
        return einvSupplyTypeName;
    }

    public void setEinvSupplyTypeName(String einvSupplyTypeName) {
        this.einvSupplyTypeName = einvSupplyTypeName;
    }

    public String getEinvDocTypeCode() {
        return einvDocTypeCode;
    }

    public void setEinvDocTypeCode(String einvDocTypeCode) {
        this.einvDocTypeCode = einvDocTypeCode;
    }

    public String getEinvDocTypeName() {
        return einvDocTypeName;
    }

    public void setEinvDocTypeName(String einvDocTypeName) {
        this.einvDocTypeName = einvDocTypeName;
    }

    public String getReverseCharge() {
        return reverseCharge;
    }

    public void setReverseCharge(String reverseCharge) {
        this.reverseCharge = reverseCharge;
    }

    public String getIgstOnIntra() {
        return igstOnIntra;
    }

    public void setIgstOnIntra(String igstOnIntra) {
        this.igstOnIntra = igstOnIntra;
    }

    public String getEinv_version() {
        return einv_version;
    }

    public void setEinv_version(String einv_version) {
        this.einv_version = einv_version;
    }

    public String geteComGstin() {
        return eComGstin;
    }

    public void seteComGstin(String eComGstin) {
        this.eComGstin = eComGstin;
    }

    public String getStateCessAmount() {
        return stateCessAmount;
    }

    public void setStateCessAmount(String stateCessAmount) {
        this.stateCessAmount = stateCessAmount;
    }

    public String getRoundOfAmount() {
        return roundOfAmount;
    }

    public void setRoundOfAmount(String roundOfAmount) {
        this.roundOfAmount = roundOfAmount;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getTotalInvAmtInFc() {
        return totalInvAmtInFc;
    }

    public void setTotalInvAmtInFc(String totalInvAmtInFc) {
        this.totalInvAmtInFc = totalInvAmtInFc;
    }

    public String getEinv_IrnNo() {
        return einv_IrnNo;
    }

    public void setEinv_IrnNo(String einv_IrnNo) {
        this.einv_IrnNo = einv_IrnNo;
    }

    public String getEinv_ackNo() {
        return einv_ackNo;
    }

    public void setEinv_ackNo(String einv_ackNo) {
        this.einv_ackNo = einv_ackNo;
    }

    public String getEinv_ackDt() {
        return einv_ackDt;
    }

    public void setEinv_ackDt(String einv_ackDt) {
        this.einv_ackDt = einv_ackDt;
    }

    public String getEinv_Status() {
        return einv_Status;
    }

    public void setEinv_Status(String einv_Status) {
        this.einv_Status = einv_Status;
    }

    public String getEinv_JwtIssuer() {
        return einv_JwtIssuer;
    }

    public void setEinv_JwtIssuer(String einv_JwtIssuer) {
        this.einv_JwtIssuer = einv_JwtIssuer;
    }

    public String getEinv_SignedQrCode() {
        return einv_SignedQrCode;
    }

    public void setEinv_SignedQrCode(String einv_SignedQrCode) {
        this.einv_SignedQrCode = einv_SignedQrCode;
    }

    public String getEinv_SignedInvoice() {
        return einv_SignedInvoice;
    }

    public void setEinv_SignedInvoice(String einv_SignedInvoice) {
        this.einv_SignedInvoice = einv_SignedInvoice;
    }

    public String getTaxSchemeCode() {
        return taxSchemeCode;
    }

    public void setTaxSchemeCode(String taxSchemeCode) {
        this.taxSchemeCode = taxSchemeCode;
    }

    public String getTaxSchemeName() {
        return taxSchemeName;
    }

    public void setTaxSchemeName(String taxSchemeName) {
        this.taxSchemeName = taxSchemeName;
    }

    public String getGstType() {
        return gstType;
    }

    public void setGstType(String gstType) {
        this.gstType = gstType;
    }

    public String getPosStateNumericCode() {
        return posStateNumericCode;
    }

    public void setPosStateNumericCode(String posStateNumericCode) {
        this.posStateNumericCode = posStateNumericCode;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getGstVehicleType() {
        return gstVehicleType;
    }

    public void setGstVehicleType(String gstVehicleType) {
        this.gstVehicleType = gstVehicleType;
    }

    public String getNetInvAmount() {
        return netInvAmount;
    }

    public void setNetInvAmount(String netInvAmount) {
        this.netInvAmount = netInvAmount;
    }

    public String getActFromStateNumericCode() {
        return actFromStateNumericCode;
    }

    public void setActFromStateNumericCode(String actFromStateNumericCode) {
        this.actFromStateNumericCode = actFromStateNumericCode;
    }

    public String getActToStateNumericCode() {
        return actToStateNumericCode;
    }

    public void setActToStateNumericCode(String actToStateNumericCode) {
        this.actToStateNumericCode = actToStateNumericCode;
    }

    public String getV_Prefix() {
        return v_Prefix;
    }

    public void setV_Prefix(String v_Prefix) {
        this.v_Prefix = v_Prefix;
    }

    public String getEway_Status() {
        return eway_Status;
    }

    public void setEway_Status(String eway_Status) {
        this.eway_Status = eway_Status;
    }

    public Party_Mdl getTransporter_Mdl() {
        return transporter_Mdl;
    }

    public void setTransporter_Mdl(Party_Mdl transporter_Mdl) {
        this.transporter_Mdl = transporter_Mdl;
    }

    public String getEway_BillNo() {
        return eway_BillNo;
    }

    public void setEway_BillNo(String eway_BillNo) {
        this.eway_BillNo = eway_BillNo;
    }

    public String getEway_BillDate() {
        return eway_BillDate;
    }

    public void setEway_BillDate(String eway_BillDate) {
        this.eway_BillDate = eway_BillDate;
    }

    public String getEway_validUpto() {
        return eway_validUpto;
    }

    public void setEway_validUpto(String eway_validUpto) {
        this.eway_validUpto = eway_validUpto;
    }

    public String getEway_alert() {
        return eway_alert;
    }

    public void setEway_alert(String eway_alert) {
        this.eway_alert = eway_alert;
    }

    public String getEway_cancelRsnCode() {
        return eway_cancelRsnCode;
    }

    public void setEway_cancelRsnCode(String eway_cancelRsnCode) {
        this.eway_cancelRsnCode = eway_cancelRsnCode;
    }

    public String getEway_cancelRmrk() {
        return eway_cancelRmrk;
    }

    public void setEway_cancelRmrk(String eway_cancelRmrk) {
        this.eway_cancelRmrk = eway_cancelRmrk;
    }

    public String getEway_cancelDate() {
        return eway_cancelDate;
    }

    public void setEway_cancelDate(String eway_cancelDate) {
        this.eway_cancelDate = eway_cancelDate;
    }

    public String getEinv_SignedQrCodeImage() {
        return einv_SignedQrCodeImage;
    }

    public void setEinv_SignedQrCodeImage(String einv_SignedQrCodeImage) {
        this.einv_SignedQrCodeImage = einv_SignedQrCodeImage;
    }

    public String getEinv_ExtractedSignedInvoiceDataJson() {
        return einv_ExtractedSignedInvoiceDataJson;
    }

    public void setEinv_ExtractedSignedInvoiceDataJson(String einv_ExtractedSignedInvoiceDataJson) {
        this.einv_ExtractedSignedInvoiceDataJson = einv_ExtractedSignedInvoiceDataJson;
    }

    public String getEinv_ExtractedSignedQrCodeJson() {
        return einv_ExtractedSignedQrCodeJson;
    }

    public void setEinv_ExtractedSignedQrCodeJson(String einv_ExtractedSignedQrCodeJson) {
        this.einv_ExtractedSignedQrCodeJson = einv_ExtractedSignedQrCodeJson;
    }

    public String getMongoLinkId() {
        return mongoLinkId;
    }

    public void setMongoLinkId(String mongoLinkId) {
        this.mongoLinkId = mongoLinkId;
    }

    public String getEinv_cancelRsnCode() {
        return einv_cancelRsnCode;
    }

    public void setEinv_cancelRsnCode(String einv_cancelRsnCode) {
        this.einv_cancelRsnCode = einv_cancelRsnCode;
    }

    public String getEinv_cancelRmrk() {
        return einv_cancelRmrk;
    }

    public void setEinv_cancelRmrk(String einv_cancelRmrk) {
        this.einv_cancelRmrk = einv_cancelRmrk;
    }

    public String getEinv_cancelDate() {
        return einv_cancelDate;
    }

    public void setEinv_cancelDate(String einv_cancelDate) {
        this.einv_cancelDate = einv_cancelDate;
    }

    public String getEway_GenerationMode() {
        return eway_GenerationMode;
    }

    public void setEway_GenerationMode(String eway_GenerationMode) {
        this.eway_GenerationMode = eway_GenerationMode;
    }

    public boolean isEwayBillRequired() {
        return ewayBillRequired;
    }

    public void setEwayBillRequired(boolean ewayBillRequired) {
        this.ewayBillRequired = ewayBillRequired;
    }

    public boolean isEinvoiceRequired() {
        return einvoiceRequired;
    }

    public void setEinvoiceRequired(boolean einvoiceRequired) {
        this.einvoiceRequired = einvoiceRequired;
    }

    public String getU_EntDt_LatestLine() {
        return u_EntDt_LatestLine;
    }

    public void setU_EntDt_LatestLine(String u_EntDt_LatestLine) {
        this.u_EntDt_LatestLine = u_EntDt_LatestLine;
    }

    public boolean isSiteTransaction() {
        return siteTransaction;
    }

    public void setSiteTransaction(boolean siteTransaction) {
        this.siteTransaction = siteTransaction;
    }

    public ArrayList<EinvAdditionalDocDtls_Mdl> getAddDocList() {
        return addDocList;
    }

    public void setAddDocList(ArrayList<EinvAdditionalDocDtls_Mdl> addDocList) {
        this.addDocList = addDocList;
    }

    public String getRefRemark() {
        return refRemark;
    }

    public void setRefRemark(String refRemark) {
        this.refRemark = refRemark;
    }

    public ArrayList<EinvRefContractDtls_Mdl> getContractDetails() {
        return ContractDetails;
    }

    public void setContractDetails(ArrayList<EinvRefContractDtls_Mdl> ContractDetails) {
        this.ContractDetails = ContractDetails;
    }

    public String getRefDocid() {
        return refDocid;
    }

    public void setRefDocid(String refDocid) {
        this.refDocid = refDocid;
    }

    public String getRefRecId() {
        return refRecId;
    }

    public void setRefRecId(String refRecId) {
        this.refRecId = refRecId;
    }

    public String getRefV_Date() {
        return refV_Date;
    }

    public void setRefV_Date(String refV_Date) {
        this.refV_Date = refV_Date;
    }

    public String getRefDocOtherRef() {
        return refDocOtherRef;
    }

    public void setRefDocOtherRef(String refDocOtherRef) {
        this.refDocOtherRef = refDocOtherRef;
    }

    public String getRefInvDocStartDate() {
        return refInvDocStartDate;
    }

    public void setRefInvDocStartDate(String refInvDocStartDate) {
        this.refInvDocStartDate = refInvDocStartDate;
    }

    public String getRefInvDocEndDate() {
        return refInvDocEndDate;
    }

    public void setRefInvDocEndDate(String refInvDocEndDate) {
        this.refInvDocEndDate = refInvDocEndDate;
    }

    public String getShippingBillNo() {
        return shippingBillNo;
    }

    public void setShippingBillNo(String shippingBillNo) {
        this.shippingBillNo = shippingBillNo;
    }

    public String getShippingBillDate() {
        return shippingBillDate;
    }

    public void setShippingBillDate(String shippingBillDate) {
        this.shippingBillDate = shippingBillDate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getRefundClaim() {
        return refundClaim;
    }

    public void setRefundClaim(String refundClaim) {
        this.refundClaim = refundClaim;
    }

    public String getPayee_Name() {
        return payee_Name;
    }

    public void setPayee_Name(String payee_Name) {
        this.payee_Name = payee_Name;
    }

    public String getPayee_AccountNo() {
        return payee_AccountNo;
    }

    public void setPayee_AccountNo(String payee_AccountNo) {
        this.payee_AccountNo = payee_AccountNo;
    }

    public String getPayee_ModeOfPayment() {
        return payee_ModeOfPayment;
    }

    public void setPayee_ModeOfPayment(String payee_ModeOfPayment) {
        this.payee_ModeOfPayment = payee_ModeOfPayment;
    }

    public String getPayee_BranchCode() {
        return payee_BranchCode;
    }

    public void setPayee_BranchCode(String payee_BranchCode) {
        this.payee_BranchCode = payee_BranchCode;
    }

    public String getPayee_CrTrn() {
        return payee_CrTrn;
    }

    public void setPayee_CrTrn(String payee_CrTrn) {
        this.payee_CrTrn = payee_CrTrn;
    }

    public String getPayee_PaymentInstruction() {
        return payee_PaymentInstruction;
    }

    public void setPayee_PaymentInstruction(String payee_PaymentInstruction) {
        this.payee_PaymentInstruction = payee_PaymentInstruction;
    }

    public String getPayee_PaymentTerms() {
        return payee_PaymentTerms;
    }

    public void setPayee_PaymentTerms(String payee_PaymentTerms) {
        this.payee_PaymentTerms = payee_PaymentTerms;
    }

    public String getPayee_DirectDebit() {
        return payee_DirectDebit;
    }

    public void setPayee_DirectDebit(String payee_DirectDebit) {
        this.payee_DirectDebit = payee_DirectDebit;
    }

    public String getPayee_CreditDays() {
        return payee_CreditDays;
    }

    public void setPayee_CreditDays(String payee_CreditDays) {
        this.payee_CreditDays = payee_CreditDays;
    }

    public String getPayee_PaidAmount() {
        return payee_PaidAmount;
    }

    public void setPayee_PaidAmount(String payee_PaidAmount) {
        this.payee_PaidAmount = payee_PaidAmount;
    }

    public String getPayee_PaymentDue() {
        return payee_PaymentDue;
    }

    public void setPayee_PaymentDue(String payee_PaymentDue) {
        this.payee_PaymentDue = payee_PaymentDue;
    }

    public String getPosStateName() {
        return posStateName;
    }

    public void setPosStateName(String posStateName) {
        this.posStateName = posStateName;
    }

    public String getTt_u_EntDt_LatestLine() {
        return tt_u_EntDt_LatestLine;
    }

    public void setTt_u_EntDt_LatestLine(String tt_u_EntDt_LatestLine) {
        this.tt_u_EntDt_LatestLine = tt_u_EntDt_LatestLine;
    }

    public boolean isTpBillGenrated() {
        return tpBillGenrated;
    }

    public void setTpBillGenrated(boolean tpBillGenrated) {
        this.tpBillGenrated = tpBillGenrated;
    }
}
