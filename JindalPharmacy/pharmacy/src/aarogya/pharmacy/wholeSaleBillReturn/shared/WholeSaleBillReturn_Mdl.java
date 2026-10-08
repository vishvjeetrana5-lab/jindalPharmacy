package aarogya.pharmacy.wholeSaleBillReturn.shared;

import aarogya.pharmacy.requisitionItemDetails.shared.GrdRequisitionItemDetails_Mdl;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.RecId;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WholeSaleBillReturn_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String documentNatureCode;
    private String documentTypeCode;
    private String voucherTypeName;
    private String voucherDate;
    private String prescDocId;
    private String docIdSaleBill;
    private String recIdSaleBill;
    private String v_TypeSaleBill;
    private String net_AmtSaleBill;
    private String debtorName;
    private String debtorCode;
    private String patientIntial;
    private String Remark;
    private String departmentName;
    private String departmentCode;
    private String controlAccount;
    private String roundOffAcCode;
    private String cancelRecord;
    private String userEntDtLatestLine;
    private String sbItemChangeCondition;
    private String cardPaymentPopupNetAmount;
    private String defaultPharmacyServiceCode;
    private String refundDocId;

    private String refNo;
    private String refDate;
    private String pharmacyBalance;
    private String registrationBalance;
    private String siteStateCode;

    private String placeOfSupply;
    private String placeOfSupplyName;
    private String vehicleNo;
    private String transporterCode;
    private String transporterName;
    private String transportModeCode;
    private String transportModeName;
    private String lrNo;
    private String lrDate;
    private String eWayBillDateTime;
    private String eWayBillNo;
    private String locationCode;
    private String locationName;
    private String vehicleTypeCode;
    private String vehicleTypeName;
    private String eInvIrnNo;
    private String partyGstin;
    private String partyGstinDate;

    private String subCode;
    private String gstReasonCode;
    private String gstReasonName;
    private String partyManualCode;
    private String partyType;
    private String sBillType;
    private String grossAmt, discountAmt, discountAmt2, billAmt, taxableAmt, cgstAmt, sgstAmt, igstAmt, ugstAmt, roundOffAmt, netAmount, landedValue;
    private String partySubCode;
    private String mongoLinkId;
    private ModifyReason_Mdl modifyReasonMdl;

    private STEnum.MedicineSaleOn saleMedicine = STEnum.MedicineSaleOn.SALE_RATE;
    private CardPaymentPopup_Mdl cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
    private List<GrdMedicine_Mdl> GrdMediDetails = new ArrayList<GrdMedicine_Mdl>();
    private List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails = new ArrayList<GrdRequisitionItemDetails_Mdl>();
    private HashMap<Integer, PartyDetailPopup_Mdl> partyDetail_Mdl = new HashMap<>();

    private RecId recIdSaleBillReturn = new RecId();
    private RecId recIdRecipt = new RecId();

    private String flagCardPayment;
    private String gstAmt;
    private String paymentSource;

    private String medicineReturnRequestDocID;
    private String voucherShortName;
    private String approvalRequired;
    private String voucherCategory;

    private String paymentSourceName;
    private boolean allowCredittoRoom;

    private String saleReturnVTypeShortName;
    private String saleBillPaymentSourceCode;
    private String profitPercentage;

    public String getGstAmt() {
        return gstAmt;
    }

    public void setGstAmt(String gstAmt) {
        this.gstAmt = gstAmt;
    }

    public WholeSaleBillReturn_Mdl() {
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

    public String getGstReasonCode() {
        return gstReasonCode;
    }

    public void setGstReasonCode(String gstReasonCode) {
        this.gstReasonCode = gstReasonCode;
    }

    public String getGstReasonName() {
        return gstReasonName;
    }

    public void setGstReasonName(String gstReasonName) {
        this.gstReasonName = gstReasonName;
    }

    public String getPharmacyBalance() {
        return pharmacyBalance;
    }

    public void setPharmacyBalance(String pharmacyBalance) {
        this.pharmacyBalance = pharmacyBalance;
    }

    public String getRegistrationBalance() {
        return registrationBalance;
    }

    public void setRegistrationBalance(String registrationBalance) {
        this.registrationBalance = registrationBalance;
    }

    public String getPartyManualCode() {
        return partyManualCode;
    }

    public void setPartyManualCode(String partyManualCode) {
        this.partyManualCode = partyManualCode;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
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

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getCardPaymentPopupNetAmount() {
        return cardPaymentPopupNetAmount;
    }

    public void setCardPaymentPopupNetAmount(String cardPaymentPopupNetAmount) {
        this.cardPaymentPopupNetAmount = cardPaymentPopupNetAmount;
    }

    public CardPaymentPopup_Mdl getCardPaymentSaleBillReturn() {
        return cardPaymentSaleBillReturn;
    }

    public void setCardPaymentSaleBillReturn(CardPaymentPopup_Mdl cardPaymentSaleBillReturn) {
        this.cardPaymentSaleBillReturn = cardPaymentSaleBillReturn;
    }

    public String getSbItemChangeCondition() {
        return sbItemChangeCondition;
    }

    public void setSbItemChangeCondition(String sbItemChangeCondition) {
        this.sbItemChangeCondition = sbItemChangeCondition;
    }

    public String getNet_AmtSaleBill() {
        return net_AmtSaleBill;
    }

    public void setNet_AmtSaleBill(String net_AmtSaleBill) {
        this.net_AmtSaleBill = net_AmtSaleBill;
    }

    public String getV_TypeSaleBill() {
        return v_TypeSaleBill;
    }

    public void setV_TypeSaleBill(String v_TypeSaleBill) {
        this.v_TypeSaleBill = v_TypeSaleBill;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getRefDate() {
        return refDate;
    }

    public void setRefDate(String refDate) {
        this.refDate = refDate;
    }

    public String getUserEntDtLatestLine() {
        return userEntDtLatestLine;
    }

    public void setUserEntDtLatestLine(String userEntDtLatestLine) {
        this.userEntDtLatestLine = userEntDtLatestLine;
    }

    public List<GrdRequisitionItemDetails_Mdl> getLstRequisitionItemDetails() {
        return lstRequisitionItemDetails;
    }

    public void setLstRequisitionItemDetails(List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails) {
        this.lstRequisitionItemDetails = lstRequisitionItemDetails;
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

    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getDebtorCode() {
        return debtorCode;
    }

    public void setDebtorCode(String debtorCode) {
        this.debtorCode = debtorCode;
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

    public String getPrescDocId() {
        return prescDocId;
    }

    public void setPrescDocId(String prescDocId) {
        this.prescDocId = prescDocId;
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

    public String getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(String igstAmt) {
        this.igstAmt = igstAmt;
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

    public String getDiscountAmt2() {
        return discountAmt2;
    }

    public void setDiscountAmt2(String discountAmt2) {
        this.discountAmt2 = discountAmt2;
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

    public String getPatientIntial() {
        return patientIntial;
    }

    public void setPatientIntial(String patientIntial) {
        this.patientIntial = patientIntial;
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

    public STEnum.MedicineSaleOn getSaleMedicine() {
        return saleMedicine;
    }

    public void setSaleMedicine(STEnum.MedicineSaleOn saleMedicine) {
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

    public String getDocIdSaleBill() {
        return docIdSaleBill;
    }

    public void setDocIdSaleBill(String docIdSaleBill) {
        this.docIdSaleBill = docIdSaleBill;
    }

    public String getRecIdSaleBill() {
        return recIdSaleBill;
    }

    public void setRecIdSaleBill(String recIdSaleBill) {
        this.recIdSaleBill = recIdSaleBill;
    }

    public RecId getRecIdSaleBillReturn() {
        return recIdSaleBillReturn;
    }

    public void setRecIdSaleBillReturn(RecId recIdSaleBillReturn) {
        this.recIdSaleBillReturn = recIdSaleBillReturn;
    }

    public String getFlagCardPayment() {
        return flagCardPayment;
    }

    public void setFlagCardPayment(String flagCardPayment) {
        this.flagCardPayment = flagCardPayment;
    }

    public String getPaymentSource() {
        return paymentSource;
    }

    public void setPaymentSource(String paymentSource) {
        this.paymentSource = paymentSource;
    }

    public String getMedicineReturnRequestDocID() {
        return medicineReturnRequestDocID;
    }

    public void setMedicineReturnRequestDocID(String medicineReturnRequestDocID) {
        this.medicineReturnRequestDocID = medicineReturnRequestDocID;
    }

    public String getPartySubCode() {
        return partySubCode;
    }

    public void setPartySubCode(String partySubCode) {
        this.partySubCode = partySubCode;
    }

    public String getMongoLinkId() {
        return mongoLinkId;
    }

    public void setMongoLinkId(String mongoLinkId) {
        this.mongoLinkId = mongoLinkId;
    }

    public String geteInvIrnNo() {
        return eInvIrnNo;
    }

    public void seteInvIrnNo(String eInvIrnNo) {
        this.eInvIrnNo = eInvIrnNo;
    }

    public String getPartyGstin() {
        return partyGstin;
    }

    public void setPartyGstin(String partyGstin) {
        this.partyGstin = partyGstin;
    }

    public String getPartyGstinDate() {
        return partyGstinDate;
    }

    public void setPartyGstinDate(String partyGstinDate) {
        this.partyGstinDate = partyGstinDate;
    }

    private WholeSaleBillReturn_Mdl(String voucherTypeCode, String documentNatureCode, String documentTypeCode, String voucherTypeName, String voucherDate, String prescDocId, String docIdSaleBill, String recIdSaleBill, String v_TypeSaleBill, String net_AmtSaleBill, String debtorName, String patientIntial, String Remark, String departmentName, String departmentCode, String controlAccount, String roundOffAcCode, String cancelRecord, String userEntDtLatestLine, String sbItemChangeCondition, String cardPaymentPopupNetAmount, String refNo, String refDate, String pharmacyBalance, String registrationBalance, String subCode, String gstReasonCode, String gstReasonName, String siteStateCode, String partyManualCode, String partyType, String grossAmt, String discountAmt, String discountAmt2, String billAmt, String taxableAmt, String cgstAmt, String sgstAmt, String ugstAmt, String roundOffAmt, String netAmount, String landedValue, ModifyReason_Mdl modifyReasonMdl, String flagCardPayment, String KeyFieldCode, String KeyFieldName, STEnum.MedicineSaleOn saleMedicine, CardPaymentPopup_Mdl cardPaymentSaleBillReturn, List<GrdMedicine_Mdl> GrdMediDetails, List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails,
            String defaultPharmacyServiceCode, String gstAmt, String paymentSource, String medicineReturnRequestDocID, String voucherShortName, String approvalRequired, String refundDocId, String sBillType, String vehicleNo, String transporterCode, String transporterName,
            String transportModeCode, String transportModeName, String lrNo, String lrDate, String eWayBillDateTime,
            String eWayBillNo, String locationCode, String locationName, String vehicleTypeCode, String vehicleTypeName, String placeOfSupply, String placeOfSupplyName, String mongoLinkId, String eInvIrnNo, String igstAmt, String partyGstin, String partyGstinDate, String profitPercentage) {
        super(KeyFieldCode, KeyFieldName);
        this.voucherTypeCode = voucherTypeCode;
        this.documentNatureCode = documentNatureCode;
        this.documentTypeCode = documentTypeCode;
        this.voucherTypeName = voucherTypeName;
        this.voucherDate = voucherDate;
        this.prescDocId = prescDocId;
        this.docIdSaleBill = docIdSaleBill;
        this.recIdSaleBill = recIdSaleBill;
        this.v_TypeSaleBill = v_TypeSaleBill;
        this.net_AmtSaleBill = net_AmtSaleBill;
        this.debtorName = debtorName;
        this.patientIntial = patientIntial;
        this.Remark = Remark;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.controlAccount = controlAccount;
        this.roundOffAcCode = roundOffAcCode;
        this.cancelRecord = cancelRecord;
        this.userEntDtLatestLine = userEntDtLatestLine;
        this.sbItemChangeCondition = sbItemChangeCondition;
        this.cardPaymentPopupNetAmount = cardPaymentPopupNetAmount;
        this.refNo = refNo;
        this.refDate = refDate;
        this.sBillType = sBillType;
        this.pharmacyBalance = pharmacyBalance;
        this.registrationBalance = registrationBalance;
        this.subCode = subCode;
        this.gstReasonCode = gstReasonCode;
        this.gstReasonName = gstReasonName;
        this.siteStateCode = siteStateCode;
        this.partyManualCode = partyManualCode;
        this.partyType = partyType;
        this.grossAmt = grossAmt;
        this.discountAmt = discountAmt;
        this.discountAmt2 = discountAmt2;
        this.billAmt = billAmt;
        this.taxableAmt = taxableAmt;
        this.cgstAmt = cgstAmt;
        this.igstAmt = igstAmt;
        this.sgstAmt = sgstAmt;
        this.ugstAmt = ugstAmt;
        this.roundOffAmt = roundOffAmt;
        this.netAmount = netAmount;
        this.landedValue = landedValue;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;
        this.saleMedicine = saleMedicine;
        this.flagCardPayment = flagCardPayment;
        this.cardPaymentSaleBillReturn = cardPaymentSaleBillReturn != null ? cardPaymentSaleBillReturn.clone() : null;
        this.gstAmt = gstAmt;
        this.vehicleNo = vehicleNo;
        this.transporterCode = transporterCode;
        this.transporterName = transporterName;
        this.transportModeCode = transportModeCode;
        this.transportModeName = transportModeName;
        this.lrNo = lrNo;
        this.lrDate = lrDate;
        this.eWayBillDateTime = eWayBillDateTime;
        this.eWayBillNo = eWayBillNo;
        this.locationCode = locationCode;
        this.locationName = locationName;
        this.vehicleTypeCode = vehicleTypeCode;
        this.vehicleTypeName = vehicleTypeName;
        this.placeOfSupply = placeOfSupply;
        this.placeOfSupplyName = placeOfSupplyName;
        this.mongoLinkId = mongoLinkId;
        this.eInvIrnNo = eInvIrnNo;
        this.partyGstin = partyGstin;
        this.partyGstinDate = partyGstinDate;
        this.profitPercentage = profitPercentage;

        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;

        this.paymentSource = paymentSource;

        this.GrdMediDetails = new ArrayList<>();
        for (GrdMedicine_Mdl s : GrdMediDetails) {
            this.GrdMediDetails.add(new GrdMedicine_Mdl(s));
        }
        this.lstRequisitionItemDetails = new ArrayList<>();
        for (GrdRequisitionItemDetails_Mdl s : lstRequisitionItemDetails) {
            this.lstRequisitionItemDetails.add(new GrdRequisitionItemDetails_Mdl(s));
        }

        this.medicineReturnRequestDocID = medicineReturnRequestDocID;
        this.voucherShortName = voucherShortName;
        this.approvalRequired = approvalRequired;
        this.refundDocId = refundDocId;
    }

    public WholeSaleBillReturn_Mdl(WholeSaleBillReturn_Mdl mdl) {
        this(mdl.getVoucherTypeCode(), mdl.getDocumentNatureCode(), mdl.getDocumentTypeCode(), mdl.getVoucherTypeName(),
                mdl.getVoucherDate(), mdl.getPrescDocId(),
                mdl.getDocIdSaleBill(), mdl.getRecIdSaleBill(), mdl.getV_TypeSaleBill(), mdl.getNet_AmtSaleBill(),
                mdl.getDebtorName(), mdl.getPatientIntial(), mdl.getRemark(), mdl.getDepartmentName(), mdl.getDepartmentCode(), mdl.getControlAccount(), mdl.getRoundOffAcCode(),
                mdl.getCancelRecord(), mdl.getUserEntDtLatestLine(), mdl.getSbItemChangeCondition(), mdl.getCardPaymentPopupNetAmount(), mdl.getRefNo(), mdl.getRefDate(),
                mdl.getPharmacyBalance(), mdl.getRegistrationBalance(), mdl.getSubCode(), mdl.getGstReasonCode(),
                mdl.getGstReasonName(), mdl.getSiteStateCode(), mdl.getPartyManualCode(),
                mdl.getPartyType(), mdl.getGrossAmt(), mdl.getDiscountAmt(), mdl.getDiscountAmt2(), mdl.getBillAmt(), mdl.getTaxableAmt(), mdl.getCgstAmt(),
                mdl.getSgstAmt(), mdl.getUgstAmt(), mdl.getRoundOffAmt(), mdl.getNetAmount(), mdl.getLandedValue(), mdl.getModifyReasonMdl(),
                mdl.getFlagCardPayment(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.getSaleMedicine(), mdl.getCardPaymentSaleBillReturn(),
                mdl.getGrdMediDetails(), mdl.getLstRequisitionItemDetails(), mdl.getDefaultPharmacyServiceCode(),
                mdl.getGstAmt(), mdl.getPaymentSource(), mdl.getMedicineReturnRequestDocID(), mdl.getVoucherShortName(), mdl.getApprovalRequired(), mdl.getRefundDocId(), mdl.getsBillType(),
                mdl.getVehicleNo(), mdl.getTransporterCode(), mdl.getTransporterName(), mdl.getTransportModeCode(),
                mdl.getTransportModeName(), mdl.getLrNo(), mdl.getLrDate(), mdl.geteWayBillDateTime(),
                mdl.geteWayBillNo(), mdl.getLocationCode(), mdl.getLocationName(), mdl.getVehicleTypeCode(),
                mdl.getVehicleTypeName(), mdl.getPlaceOfSupply(), mdl.getPlaceOfSupplyName(), mdl.getMongoLinkId(), mdl.geteInvIrnNo(), mdl.getIgstAmt(), mdl.getPartyGstin(), mdl.getPartyGstinDate(), mdl.getProfitPercentage());
    }

    public String getVoucherShortName() {
        return voucherShortName;
    }

    public void setVoucherShortName(String voucherShortName) {
        this.voucherShortName = voucherShortName;
    }

    public String getApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(String approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public String getRefundDocId() {
        return refundDocId;
    }

    public void setRefundDocId(String refundDocId) {
        this.refundDocId = refundDocId;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getPaymentSourceName() {
        return paymentSourceName;
    }

    public void setPaymentSourceName(String paymentSourceName) {
        this.paymentSourceName = paymentSourceName;
    }

    public boolean isAllowCredittoRoom() {
        return allowCredittoRoom;
    }

    public void setAllowCredittoRoom(boolean allowCredittoRoom) {
        this.allowCredittoRoom = allowCredittoRoom;
    }

    public String getSaleReturnVTypeShortName() {
        return saleReturnVTypeShortName;
    }

    public void setSaleReturnVTypeShortName(String saleReturnVTypeShortName) {
        this.saleReturnVTypeShortName = saleReturnVTypeShortName;
    }

    public String getSaleBillPaymentSourceCode() {
        return saleBillPaymentSourceCode;
    }

    public void setSaleBillPaymentSourceCode(String saleBillPaymentSourceCode) {
        this.saleBillPaymentSourceCode = saleBillPaymentSourceCode;
    }

    public String getsBillType() {
        return sBillType;
    }

    public void setsBillType(String sBillType) {
        this.sBillType = sBillType;
    }

    public HashMap<Integer, PartyDetailPopup_Mdl> getPartyDetail_Mdl() {
        return partyDetail_Mdl;
    }

    public void setPartyDetail_Mdl(HashMap<Integer, PartyDetailPopup_Mdl> partyDetail_Mdl) {
        this.partyDetail_Mdl = partyDetail_Mdl;
    }

    public String getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(String profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

}
