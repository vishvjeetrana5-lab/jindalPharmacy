package aarogya.pharmacy.saleBillWholeSale.shared;

import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaleBillWholeSale_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String documentNatureCode;
    private String documentTypeCode;
    private String voucherTypeName;
    private String voucherCategory;
    private String typeCode;
    private String typeName;
    private String consultantCode;
    private String consultantName;
    private String voucherDate;
    private String roomCode;
    private String patientName;
    private String Remark;
    private String departmentName;
    private String departmentCode;
    private String controlAccount;
    private String roundOffAcCode;
    private String cancelRecord;
    private String userEntDtLatestLine;
    private String isReturnExist;

    private String partyType;
    private String defaultPharmacyServiceCode;

    private String siteStateCode;
    private String invoiceDate;
    private String partySubCode;

    private String placeOfSupply;
    private String placeOfSupplyName;
    private String vehicleNo;
    private String transporterCode;
    private String transporterName;
    private String transportModeCode;
    private String transportModeName;
    private String lrNo;
    private String lrDate;
    private String eWayBillRequiredCode;
    private String eWayBillRequiredName;
    private String eWayBillDateTime;
    private String eWayBillNo;
    private String locationCode;
    private String locationName;
    private String vehicleTypeCode;
    private String vehicleTypeName;
    private String eInvIrnNo;
    private String mongoLinkId;
    private String gatePassDocId;
    private String gatePassRecId;
    private String approvedBy;
    private String partyGstIn;
    private String partyGstinDate;

    private String grossAmt, discountAmt, discountAmt2, billAmt, taxableAmt, cgstAmt, sgstAmt, igstAmt, ugstAmt, roundOffAmt, netAmount, landedValue;
    private RecId recIdSaleBill = new RecId();
    private RecId recIdGatePass = new RecId();
    private RecId recIdRecipt = new RecId();

    private Organization_Mdl organizationMdl;
    private MedicineSaleOn saleMedicine = MedicineSaleOn.SALE_RATE;
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private ModifyReason_Mdl modifyReasonMdl;
    private List<String> lstFreeWithItemCodes = new ArrayList<>();

    private List<GrdMedicine_Mdl> GrdMediDetails = new ArrayList<GrdMedicine_Mdl>();
    private HashMap<Integer, PartyDetailPopup_Mdl> partyDetail_Mdl = new HashMap<>();

    private String precVDescription, precVType, precAttachPaymentMode, receiptTypeCode, receiptTypeName, receiptTypeCateory;
    private String flagCardPayment;
    private String gstAmt;
    private String paymentSource;
    private String paymentMode;

    //DueDate
    private String dueDate;
    private String dueDaysMaster;
    private String profitPercentage;
    private String partyPODate;
    private String partyPONO;

    public SaleBillWholeSale_Mdl() {
    }

    public String getDiscountAmt2() {
        return discountAmt2;
    }

    public void setDiscountAmt2(String discountAmt2) {
        this.discountAmt2 = discountAmt2;
    }

    public String getSiteStateCode() {
        return siteStateCode;
    }

    public void setSiteStateCode(String siteStateCode) {
        this.siteStateCode = siteStateCode;
    }

    public String getDefaultPharmacyServiceCode() {
        return defaultPharmacyServiceCode;
    }

    public void setDefaultPharmacyServiceCode(String defaultPharmacyServiceCode) {
        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
    }

    public List<String> getLstFreeWithItemCodes() {
        return lstFreeWithItemCodes;
    }

    public void setLstFreeWithItemCodes(List<String> lstFreeWithItemCodes) {
        this.lstFreeWithItemCodes = lstFreeWithItemCodes;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getPlaceOfSupplyName() {
        return placeOfSupplyName;
    }

    public void setPlaceOfSupplyName(String placeOfSupplyName) {
        this.placeOfSupplyName = placeOfSupplyName;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getIsReturnExist() {
        return isReturnExist;
    }

    public void setIsReturnExist(String isReturnExist) {
        this.isReturnExist = isReturnExist;
    }

    public String getUserEntDtLatestLine() {
        return userEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String userEntDtLatestLine) {
        this.userEntDtLatestLine = userEntDtLatestLine;
    }

    public String getConsultantCode() {
        return consultantCode;
    }

    public void setConsultantCode(String consultantCode) {
        this.consultantCode = consultantCode;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getPrecVDescription() {
        return precVDescription;
    }

    public void setPrecVDescription(String precVDescription) {
        this.precVDescription = precVDescription;
    }

    public String getPrecVType() {
        return precVType;
    }

    public void setPrecVType(String precVType) {
        this.precVType = precVType;
    }

    public String getPrecAttachPaymentMode() {
        return precAttachPaymentMode;
    }

    public void setPrecAttachPaymentMode(String precAttachPaymentMode) {
        this.precAttachPaymentMode = precAttachPaymentMode;
    }

    public RecId getRecIdRecipt() {
        return recIdRecipt;
    }

    public void setRecIdRecipt(RecId recIdRecipt) {
        this.recIdRecipt = recIdRecipt;
    }

    public ModifyReason_Mdl getModifyReasonMdl() {
        return modifyReasonMdl;
    }

    public void setModifyReasonMdl(ModifyReason_Mdl modifyReasonMdl) {
        this.modifyReasonMdl = modifyReasonMdl;
    }

    public CardPaymentPopup_Mdl getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(CardPaymentPopup_Mdl cardPayment) {
        this.cardPayment = cardPayment;
    }

    public List<GrdMedicine_Mdl> getGrdMediDetails() {
        return GrdMediDetails;
    }

    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }

    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(String netAmount) {
        this.netAmount = netAmount;
    }

    public String getLandedValue() {
        return landedValue;
    }

    public void setLandedValue(String landedValue) {
        this.landedValue = landedValue;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public RecId getRecIdSaleBill() {
        return recIdSaleBill;
    }

    public void setRecIdSaleBill(RecId recIdSaleBill) {
        this.recIdSaleBill = recIdSaleBill;
    }

    public RecId getRecIdGatePass() {
        return recIdGatePass;
    }

    public void setRecIdGatePass(RecId recIdGatePass) {
        this.recIdGatePass = recIdGatePass;
    }

    public String getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(String taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(String cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public String getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(String sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public String getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        this.grossAmt = grossAmt;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getBillAmt() {
        return billAmt;
    }

    public void setBillAmt(String billAmt) {
        this.billAmt = billAmt;
    }

    public String getRoundOffAmt() {
        return roundOffAmt;
    }

    public void setRoundOffAmt(String roundOffAmt) {
        this.roundOffAmt = roundOffAmt;
    }

    public String getDocumentNatureCode() {
        return documentNatureCode;
    }

    public void setDocumentNatureCode(String documentNatureCode) {
        this.documentNatureCode = documentNatureCode;
    }

    public String getDocumentTypeCode() {
        return documentTypeCode;
    }

    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }

    public String getReceiptTypeCode() {
        return receiptTypeCode;
    }

    public void setReceiptTypeCode(String receiptTypeCode) {
        this.receiptTypeCode = receiptTypeCode;
    }

    public String getReceiptTypeName() {
        return receiptTypeName;
    }

    public void setReceiptTypeName(String receiptTypeName) {
        this.receiptTypeName = receiptTypeName;
    }

    public String getReceiptTypeCateory() {
        return receiptTypeCateory;
    }

    public void setReceiptTypeCateory(String receiptTypeCateory) {
        this.receiptTypeCateory = receiptTypeCateory;
    }

    public String getUgstAmt() {
        return ugstAmt;
    }

    public void setUgstAmt(String ugstAmt) {
        this.ugstAmt = ugstAmt;
    }

    public String getControlAccount() {
        return controlAccount;
    }

    public void setControlAccount(String controlAccount) {
        this.controlAccount = controlAccount;
    }

    public Organization_Mdl getOrganizationMdl() {
        return organizationMdl;
    }

    public void setOrganizationMdl(Organization_Mdl organizationMdl) {
        this.organizationMdl = organizationMdl;
    }

    public MedicineSaleOn getSaleMedicine() {
        return saleMedicine;
    }

    public void setSaleMedicine(MedicineSaleOn saleMedicine) {
        this.saleMedicine = saleMedicine;
    }

    public String getCancelRecord() {
        return cancelRecord;
    }

    public void setCancelRecord(String cancelRecord) {
        this.cancelRecord = cancelRecord;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public String getFlagCardPayment() {
        return flagCardPayment;
    }

    public void setFlagCardPayment(String flagCardPayment) {
        this.flagCardPayment = flagCardPayment;
    }

    public String getGstAmt() {
        return gstAmt;
    }

    public void setGstAmt(String gstAmt) {
        this.gstAmt = gstAmt;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public String getPartySubCode() {
        return partySubCode;
    }

    public void setPartySubCode(String partySubCode) {
        this.partySubCode = partySubCode;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTransporterCode() {
        return transporterCode;
    }

    public void setTransporterCode(String transporterCode) {
        this.transporterCode = transporterCode;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public String getTransportModeCode() {
        return transportModeCode;
    }

    public void setTransportModeCode(String transportModeCode) {
        this.transportModeCode = transportModeCode;
    }

    public String getTransportModeName() {
        return transportModeName;
    }

    public void setTransportModeName(String transportModeName) {
        this.transportModeName = transportModeName;
    }

    public String getLrNo() {
        return lrNo;
    }

    public void setLrNo(String lrNo) {
        this.lrNo = lrNo;
    }

    public String getLrDate() {
        return lrDate;
    }

    public void setLrDate(String lrDate) {
        this.lrDate = lrDate;
    }

    public String geteWayBillRequiredCode() {
        return eWayBillRequiredCode;
    }

    public void seteWayBillRequiredCode(String eWayBillRequiredCode) {
        this.eWayBillRequiredCode = eWayBillRequiredCode;
    }

    public String geteWayBillRequiredName() {
        return eWayBillRequiredName;
    }

    public void seteWayBillRequiredName(String eWayBillRequiredName) {
        this.eWayBillRequiredName = eWayBillRequiredName;
    }

    public String geteWayBillDateTime() {
        return eWayBillDateTime;
    }

    public void seteWayBillDateTime(String eWayBillDateTime) {
        this.eWayBillDateTime = eWayBillDateTime;
    }

    public String geteWayBillNo() {
        return eWayBillNo;
    }

    public void seteWayBillNo(String eWayBillNo) {
        this.eWayBillNo = eWayBillNo;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void setVehicleTypeCode(String vehicleTypeCode) {
        this.vehicleTypeCode = vehicleTypeCode;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String geteInvIrnNo() {
        return eInvIrnNo;
    }

    public void seteInvIrnNo(String eInvIrnNo) {
        this.eInvIrnNo = eInvIrnNo;
    }

    public String getMongoLinkId() {
        return mongoLinkId;
    }

    public void setMongoLinkId(String mongoLinkId) {
        this.mongoLinkId = mongoLinkId;
    }

    public String getGatePassDocId() {
        return gatePassDocId;
    }

    public void setGatePassDocId(String gatePassDocId) {
        this.gatePassDocId = gatePassDocId;
    }

    public String getGatePassRecId() {
        return gatePassRecId;
    }

    public void setGatePassRecId(String gatePassRecId) {
        this.gatePassRecId = gatePassRecId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getPartyGstIn() {
        return partyGstIn;
    }

    public void setPartyGstIn(String partyGstIn) {
        this.partyGstIn = partyGstIn;
    }

    public String getPartyGstinDate() {
        return partyGstinDate;
    }

    public void setPartyGstinDate(String partyGstinDate) {
        this.partyGstinDate = partyGstinDate;
    }

    private SaleBillWholeSale_Mdl(String voucherTypeCode, String documentNatureCode, String documentTypeCode, String voucherTypeName, String typeCode,
            String typeName, String consultantCode, String consultantName,
            String voucherDate, String roomCode, String patientName,
            String Remark, String departmentName, String departmentCode, String controlAccount, String roundOffAcCode, String cancelRecord, String userEntDtLatestLine,
            String isReturnExist, String partyType,
            String siteStateCode,
            Organization_Mdl organizationMdl, String grossAmt, String discountAmt,
            String discountAmt2, String billAmt, String taxableAmt, String cgstAmt, String sgstAmt, String ugstAmt, String roundOffAmt, String netAmount,
            String landedValue, ModifyReason_Mdl modifyReasonMdl, String precVDescription, String precVType, String precAttachPaymentMode, String receiptTypeCode,
            String receiptTypeName, String receiptTypeCateory, String flagCardPayment, String KeyFieldCode, String KeyFieldName, MedicineSaleOn saleMedicine,
            CardPaymentPopup_Mdl cardPymentMdl, List<String> lstFreeWithItemCodes, List<GrdMedicine_Mdl> GrdMediDetails,
            String defaultPharmacyServiceCode,
            String gstAmt,
            String paymentSource, String partySubCode, String vehicleNo, String transporterCode, String transporterName,
            String transportModeCode, String transportModeName, String lrNo, String lrDate, String eWayBillRequiredCode, String eWayBillRequiredName, String eWayBillDateTime,
            String eWayBillNo, String locationCode, String locationName, String vehicleTypeCode, String vehicleTypeName, String placeOfSupply, String placeOfSupplyName, String mongoLinkId, String gatePassDocId, String profitPercentage) {

        super(KeyFieldCode, KeyFieldName);
        this.voucherTypeCode = voucherTypeCode;
        this.documentNatureCode = documentNatureCode;
        this.documentTypeCode = documentTypeCode;
        this.voucherTypeName = voucherTypeName;
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.consultantCode = consultantCode;
        this.consultantName = consultantName;
        this.voucherDate = voucherDate;
        this.roomCode = roomCode;
        this.partySubCode = partySubCode;
        this.patientName = patientName;
        this.Remark = Remark;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.controlAccount = controlAccount;
        this.roundOffAcCode = roundOffAcCode;
        this.cancelRecord = cancelRecord;
        this.userEntDtLatestLine = userEntDtLatestLine;
        this.isReturnExist = isReturnExist;
        this.partyType = partyType;
        this.siteStateCode = siteStateCode;
        this.organizationMdl = organizationMdl != null ? new Organization_Mdl(organizationMdl) : null;
        this.grossAmt = grossAmt;
        this.discountAmt = discountAmt;
        this.discountAmt2 = discountAmt2;
        this.billAmt = billAmt;
        this.taxableAmt = taxableAmt;
        this.cgstAmt = cgstAmt;
        this.sgstAmt = sgstAmt;
        this.ugstAmt = ugstAmt;
        this.roundOffAmt = roundOffAmt;
        this.netAmount = netAmount;
        this.landedValue = landedValue;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;
        this.cardPayment = cardPymentMdl != null ? cardPymentMdl.clone() : null;
        this.precVDescription = precVDescription;
        this.precVType = precVType;
        this.precAttachPaymentMode = precAttachPaymentMode;
        this.receiptTypeCode = receiptTypeCode;
        this.receiptTypeName = receiptTypeName;
        this.receiptTypeCateory = receiptTypeCateory;
        this.flagCardPayment = flagCardPayment;
        this.saleMedicine = saleMedicine;
        this.gstAmt = gstAmt;
        this.vehicleNo = vehicleNo;
        this.transporterCode = transporterCode;
        this.transporterName = transporterName;
        this.transportModeCode = transportModeCode;
        this.transportModeName = transportModeName;
        this.lrNo = lrNo;
        this.lrDate = lrDate;
        this.eWayBillRequiredCode = eWayBillRequiredCode;
        this.eWayBillRequiredName = eWayBillRequiredName;
        this.eWayBillDateTime = eWayBillDateTime;
        this.eWayBillNo = eWayBillNo;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.vehicleTypeCode = vehicleTypeCode;
        this.vehicleTypeName = vehicleTypeName;
        this.mongoLinkId = mongoLinkId;
        this.gatePassDocId = gatePassDocId;
        this.placeOfSupply = placeOfSupply;
        this.placeOfSupplyName = placeOfSupplyName;
        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
        this.paymentSource = paymentSource;
        this.profitPercentage = profitPercentage;

        this.lstFreeWithItemCodes = new ArrayList<>();
        for (String s : lstFreeWithItemCodes) {
            this.lstFreeWithItemCodes.add(s);
        }
        this.GrdMediDetails = new ArrayList<>();
        for (GrdMedicine_Mdl s : GrdMediDetails) {
            this.GrdMediDetails.add(new GrdMedicine_Mdl(s));
        }
    }

    public SaleBillWholeSale_Mdl(SaleBillWholeSale_Mdl mdl) {
        this(mdl.getVoucherTypeCode(), mdl.getDocumentNatureCode(), mdl.getDocumentTypeCode(), mdl.getVoucherTypeName(), mdl.getTypeCode(), mdl.getTypeName(),
                mdl.getConsultantCode(), mdl.getConsultantName(),
                mdl.getVoucherDate(), mdl.getRoomCode(),
                mdl.getPatientName(), mdl.getRemark(), mdl.getDepartmentName(), mdl.getDepartmentCode(), mdl.getControlAccount(),
                mdl.getRoundOffAcCode(), mdl.getCancelRecord(), mdl.getUserEntDtLatestLine(), mdl.getIsReturnExist(),
                mdl.getPartyType(), mdl.getSiteStateCode(),
                mdl.getOrganizationMdl(), mdl.getGrossAmt(),
                mdl.getDiscountAmt(), mdl.getDiscountAmt2(), mdl.getBillAmt(), mdl.getTaxableAmt(), mdl.getCgstAmt(), mdl.getSgstAmt(), mdl.getUgstAmt(),
                mdl.getRoundOffAmt(), mdl.getNetAmount(), mdl.getLandedValue(), mdl.getModifyReasonMdl(), mdl.getPrecVDescription(), mdl.getPrecVType(),
                mdl.getPrecAttachPaymentMode(), mdl.getReceiptTypeCode(), mdl.getReceiptTypeName(), mdl.getReceiptTypeCateory(), mdl.getFlagCardPayment(),
                mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.getSaleMedicine(), mdl.getCardPayment(), mdl.getLstFreeWithItemCodes(),
                mdl.getGrdMediDetails(), mdl.getDefaultPharmacyServiceCode(),
                mdl.getGstAmt(), mdl.getPaymentSource(), mdl.getPartySubCode(),
                mdl.getVehicleNo(), mdl.getTransporterCode(), mdl.getTransporterName(), mdl.getTransportModeCode(),
                mdl.getTransportModeName(), mdl.getLrNo(), mdl.getLrDate(), mdl.geteWayBillRequiredCode(), mdl.geteWayBillRequiredName(), mdl.geteWayBillDateTime(),
                mdl.geteWayBillNo(), mdl.getLocationCode(), mdl.getLocationName(), mdl.getVehicleTypeCode(),
                mdl.getVehicleTypeName(), mdl.getPlaceOfSupply(), mdl.getPlaceOfSupplyName(), mdl.getMongoLinkId(), mdl.getGatePassDocId(), mdl.getProfitPercentage());
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public HashMap<Integer, PartyDetailPopup_Mdl> getPartyDetail_Mdl() {
        return partyDetail_Mdl;
    }

    public void setPartyDetail_Mdl(HashMap<Integer, PartyDetailPopup_Mdl> partyDetail_Mdl) {
        this.partyDetail_Mdl = partyDetail_Mdl;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDaysMaster() {
        return dueDaysMaster;
    }

    public void setDueDaysMaster(String dueDaysMaster) {
        this.dueDaysMaster = dueDaysMaster;
    }

    public String getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(String profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    public String getPartyPODate() {
        return partyPODate;
    }

    public void setPartyPODate(String partyPODate) {
        this.partyPODate = partyPODate;
    }

    public String getPartyPONO() {
        return partyPONO;
    }

    public void setPartyPONO(String partyPONO) {
        this.partyPONO = partyPONO;
    }

}
