package aarogya.pharmacy.saleBill.shared;

import aarogya.pharmacy.requisitionItemDetails.shared.GrdRequisitionItemDetails_Mdl;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.RecId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SaleBill_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String documentNatureCode;
    private String documentTypeCode;
    private String voucherTypeName;
    private String typeCode;
    private String typeName;
    private String consultantCode;
    private String consultantName;
    private String operationCode;
    private String operationName;
    private String medicineCode;
    private String medicineName;
    private String voucherDate;
    private String uhidRecId;
    private String uhidDocid;
    private String regRecID;
    private String prescDocId;
    private String docIdRequisition;
    private String recIdRequisition;
    private String docIdEstimate;
    private String recIdEstimate;
    private String regDocid;
    private String regSiteCode;
    private String roomCode;
    private String patientName;
    private String patientIntial;
    private String Remark;
    private String departmentName;
    private String departmentCode;
    private String controlAccount;
    private String roundOffAcCode;
    private String cancelRecord;
    private String userEntDtLatestLine;
    private String isReturnExist;
    private String isEstimateSaleCreated;
    private String reqItemChangeCondition;

    private String partyType;
    private String partyManualCode;
    private String defaultPharmacyServiceCode;
    private String inCorrectSelectedBatchSql;
    private String inCorrectSelectedBarcodeSql;

    private String lmCode;
    private String lmName;
    private String lmManualCode;
    private String subCode;
    private String address;
    private String cityCode;
    private String cityName;
    private String siteStateCode;
    private String placeOfSupply;
    private String idTypeCode, idTypeName, idNo;
    private String mobile;
    private boolean patientsReqExists;

    private String pharmacyBalance;
    private String registrationBalance;
    private String grossAmt, discountAmt, discountAmt2, billAmt, taxableAmt, cgstAmt, sgstAmt, ugstAmt, roundOffAmt, netAmount, landedValue;
    private RecId recIdSaleBill = new RecId();
    private RecId recIdRecipt = new RecId();

    private Organization_Mdl organizationMdl;
    private MedicineSaleOn saleMedicine = MedicineSaleOn.SALE_RATE;
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private ModifyReason_Mdl modifyReasonMdl;
    private List<String> lstFreeWithItemCodes = new ArrayList<>();

    private List<GrdMedicine_Mdl> GrdMediDetails = new ArrayList<GrdMedicine_Mdl>();
    private List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails = new ArrayList<GrdRequisitionItemDetails_Mdl>();

    private String precVDescription, precVType, precAttachPaymentMode, receiptTypeCode, receiptTypeName, receiptTypeCateory;
    private String flagCardPayment;
    private String gstAmt;
    private String paymentSource;
    private String patientClassType;
    private String patientPackagedInfo;
    private String voucherTypeShortName;
    private String approvalRequired;
    private String voucherCategory;
    private String siteCode_HIS;
    private boolean linkParentDatabase;

    public SaleBill_Mdl() {
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

    public String getLmName() {
        return lmName;
    }

    public void setLmName(String lmName) {
        this.lmName = lmName;
    }

    public String getLmManualCode() {
        return lmManualCode;
    }

    public void setLmManualCode(String lmManualCode) {
        this.lmManualCode = lmManualCode;
    }

    public String getInCorrectSelectedBatchSql() {
        return inCorrectSelectedBatchSql;
    }

    public void setInCorrectSelectedBatchSql(String inCorrectSelectedBatchSql) {
        this.inCorrectSelectedBatchSql = inCorrectSelectedBatchSql;
    }

    public String getInCorrectSelectedBarcodeSql() {
        return inCorrectSelectedBarcodeSql;
    }

    public void setInCorrectSelectedBarcodeSql(String inCorrectSelectedBarcodeSql) {
        this.inCorrectSelectedBarcodeSql = inCorrectSelectedBarcodeSql;
    }

    public String getDefaultPharmacyServiceCode() {
        return defaultPharmacyServiceCode;
    }

    public void setDefaultPharmacyServiceCode(String defaultPharmacyServiceCode) {
        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
    }

    public boolean isPatientsReqExists() {
        return patientsReqExists;
    }

    public void setPatientsReqExists(boolean patientsReqExists) {
        this.patientsReqExists = patientsReqExists;
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

    public String getRegistrationBalance() {
        return registrationBalance;
    }

    public void setRegistrationBalance(String registrationBalance) {
        this.registrationBalance = registrationBalance;
    }

    public String getPharmacyBalance() {
        return pharmacyBalance;
    }

    public void setPharmacyBalance(String pharmacyBalance) {
        this.pharmacyBalance = pharmacyBalance;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getReqItemChangeCondition() {
        return reqItemChangeCondition;
    }

    public void setReqItemChangeCondition(String reqItemChangeCondition) {
        this.reqItemChangeCondition = reqItemChangeCondition;
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

    public List<GrdRequisitionItemDetails_Mdl> getLstRequisitionItemDetails() {
        return lstRequisitionItemDetails;
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

    public void setLstRequisitionItemDetails(List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails) {
        this.lstRequisitionItemDetails = lstRequisitionItemDetails;
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

    public String getUhidDocid() {
        return uhidDocid;
    }

    public void setUhidDocid(String uhidDocid) {
        this.uhidDocid = uhidDocid;
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

    public String getPrescDocId() {
        return prescDocId;
    }

    public void setPrescDocId(String prescDocId) {
        this.prescDocId = prescDocId;
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

    public String getUhidRecId() {
        return uhidRecId;
    }

    public void setUhidRecId(String uhidRecId) {
        this.uhidRecId = uhidRecId;
    }

    public String getRegRecID() {
        return regRecID;
    }

    public void setRegRecID(String regRecID) {
        this.regRecID = regRecID;
    }

    public String getRegDocid() {
        return regDocid;
    }

    public void setRegDocid(String regDocid) {
        this.regDocid = regDocid;
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

    public String getDocIdRequisition() {
        return docIdRequisition;
    }

    public void setDocIdRequisition(String docIdRequisition) {
        this.docIdRequisition = docIdRequisition;
    }

    public String getRecIdRequisition() {
        return recIdRequisition;
    }

    public void setRecIdRequisition(String recIdRequisition) {
        this.recIdRequisition = recIdRequisition;
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

    public String getRegSiteCode() {
        return regSiteCode;
    }

    public void setRegSiteCode(String regSiteCode) {
        this.regSiteCode = regSiteCode;
    }

    public String getRoundOffAcCode() {
        return roundOffAcCode;
    }

    public void setRoundOffAcCode(String roundOffAcCode) {
        this.roundOffAcCode = roundOffAcCode;
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getIdTypeName() {
        return idTypeName;
    }

    public void setIdTypeName(String idTypeName) {
        this.idTypeName = idTypeName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getDocIdEstimate() {
        return docIdEstimate;
    }

    public void setDocIdEstimate(String docIdEstimate) {
        this.docIdEstimate = docIdEstimate;
    }

    public String getRecIdEstimate() {
        return recIdEstimate;
    }

    public void setRecIdEstimate(String recIdEstimate) {
        this.recIdEstimate = recIdEstimate;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsEstimateSaleCreated() {
        return isEstimateSaleCreated;
    }

    public void setIsEstimateSaleCreated(String isEstimateSaleCreated) {
        this.isEstimateSaleCreated = isEstimateSaleCreated;
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

    public String getMedicineCode() {
        return medicineCode;
    }

    public void setMedicineCode(String medicineCode) {
        this.medicineCode = medicineCode;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getPatientClassType() {
        return patientClassType;
    }

    public void setPatientClassType(String patientClassType) {
        this.patientClassType = patientClassType;
    }

    public String getPatientPackagedInfo() {
        return patientPackagedInfo;
    }

    public void setPatientPackagedInfo(String patientPackagedInfo) {
        this.patientPackagedInfo = patientPackagedInfo;
    }

    private SaleBill_Mdl(String voucherTypeCode, String documentNatureCode, String documentTypeCode, String voucherTypeName, String typeCode, String typeName, String consultantCode, String consultantName, String operationCode, String operationName, String medicineCode, String medicineName, String voucherDate, String uhidRecId, String uhidDocid, String regRecID, String prescDocId, String docIdRequisition, String recIdRequisition, String docIdEstimate, String recIdEstimate, String regDocid, String regSiteCode, String roomCode, String patientName, String patientIntial, String Remark, String departmentName, String departmentCode, String controlAccount, String roundOffAcCode, String cancelRecord, String userEntDtLatestLine, String isReturnExist, String isEstimateSaleCreated, String reqItemChangeCondition, String partyType, String partyManualCode, String lmCode, String subCode, String address, String cityCode, String cityName, String placeOfSupply, String siteStateCode, String idTypeCode, String idTypeName, String idNo, String mobile, boolean patientsReqExists, String pharmacyBalance, String registrationBalance, Organization_Mdl organizationMdl, String grossAmt, String discountAmt, String discountAmt2, String billAmt, String taxableAmt, String cgstAmt, String sgstAmt, String ugstAmt, String roundOffAmt, String netAmount, String landedValue, ModifyReason_Mdl modifyReasonMdl, String precVDescription, String precVType, String precAttachPaymentMode, String receiptTypeCode, String receiptTypeName, String receiptTypeCateory, String flagCardPayment, String KeyFieldCode, String KeyFieldName, MedicineSaleOn saleMedicine, CardPaymentPopup_Mdl cardPymentMdl, List<String> lstFreeWithItemCodes, List<GrdMedicine_Mdl> GrdMediDetails, List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails, String defaultPharmacyServiceCode, String inCorrectSelectedBatchSql, String inCorrectSelectedBarcodeSql, String lmName, String lmManualCode, String gstAmt, String paymentSource, String patientClassType,String patientPackagedInfo,String voucherTypeShortName) {
        super(KeyFieldCode, KeyFieldName);
        this.voucherTypeCode = voucherTypeCode;
        this.documentNatureCode = documentNatureCode;
        this.documentTypeCode = documentTypeCode;
        this.voucherTypeName = voucherTypeName;
        this.typeCode = typeCode;
        this.typeName = typeName;
        this.consultantCode = consultantCode;
        this.consultantName = consultantName;
        this.operationCode = operationCode;
        this.operationName = operationName;
        this.medicineCode = medicineCode;
        this.medicineName = medicineName;
        this.voucherDate = voucherDate;
        this.uhidRecId = uhidRecId;
        this.uhidDocid = uhidDocid;
        this.regRecID = regRecID;
        this.prescDocId = prescDocId;
        this.docIdRequisition = docIdRequisition;
        this.recIdRequisition = recIdRequisition;
        this.docIdEstimate = docIdEstimate;
        this.recIdEstimate = recIdEstimate;
        this.regDocid = regDocid;
        this.regSiteCode = regSiteCode;
        this.roomCode = roomCode;
        this.patientName = patientName;
        this.patientIntial = patientIntial;
        this.Remark = Remark;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.controlAccount = controlAccount;
        this.roundOffAcCode = roundOffAcCode;
        this.cancelRecord = cancelRecord;
        this.userEntDtLatestLine = userEntDtLatestLine;
        this.isReturnExist = isReturnExist;
        this.isEstimateSaleCreated = isEstimateSaleCreated;
        this.reqItemChangeCondition = reqItemChangeCondition;
        this.partyType = partyType;
        this.partyManualCode = partyManualCode;
        this.lmCode = lmCode;
        this.subCode = subCode;
        this.address = address;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.placeOfSupply = placeOfSupply;
        this.siteStateCode = siteStateCode;
        this.idTypeCode = idTypeCode;
        this.idTypeName = idTypeName;
        this.idNo = idNo;
        this.mobile = mobile;
        this.patientsReqExists = patientsReqExists;
        this.pharmacyBalance = pharmacyBalance;
        this.registrationBalance = registrationBalance;
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
        this.approvalRequired = approvalRequired;

        this.voucherTypeShortName=voucherTypeShortName;
        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
        this.inCorrectSelectedBatchSql = inCorrectSelectedBatchSql;
        this.inCorrectSelectedBarcodeSql = inCorrectSelectedBarcodeSql;
        this.lmName = lmName;
        this.lmManualCode = lmManualCode;
        this.paymentSource = paymentSource;
        this.patientClassType = patientClassType;
this.patientPackagedInfo=patientPackagedInfo;
        
        this.lstFreeWithItemCodes = new ArrayList<>();
        for (String s : lstFreeWithItemCodes) {
            this.lstFreeWithItemCodes.add(s);
        }
        this.GrdMediDetails = new ArrayList<>();
        for (GrdMedicine_Mdl s : GrdMediDetails) {
            this.GrdMediDetails.add(new GrdMedicine_Mdl(s));
        }
        this.lstRequisitionItemDetails = new ArrayList<>();
        for (GrdRequisitionItemDetails_Mdl s : lstRequisitionItemDetails) {
            this.lstRequisitionItemDetails.add(new GrdRequisitionItemDetails_Mdl(s));
        }
    }

    public SaleBill_Mdl(SaleBill_Mdl mdl) {
        this(mdl.getVoucherTypeCode(), mdl.getDocumentNatureCode(), mdl.getDocumentTypeCode(), mdl.getVoucherTypeName(), mdl.getTypeCode(), mdl.getTypeName(), mdl.getConsultantCode(), mdl.getConsultantName(), mdl.getOperationCode(), mdl.getOperationName(), mdl.getMedicineCode(), mdl.getMedicineName(), mdl.getVoucherDate(), mdl.getUhidRecId(), mdl.getUhidDocid(), mdl.getRegRecID(), mdl.getPrescDocId(), mdl.getDocIdRequisition(), mdl.getRecIdRequisition(), mdl.getDocIdEstimate(), mdl.getRecIdEstimate(), mdl.getRegDocid(), mdl.getRegSiteCode(), mdl.getRoomCode(), mdl.getPatientName(), mdl.getPatientIntial(), mdl.getRemark(), mdl.getDepartmentName(), mdl.getDepartmentCode(), mdl.getControlAccount(), mdl.getRoundOffAcCode(), mdl.getCancelRecord(), mdl.getUserEntDtLatestLine(), mdl.getIsReturnExist(), mdl.getIsEstimateSaleCreated(), mdl.getReqItemChangeCondition(), mdl.getPartyType(), mdl.getPartyManualCode(), mdl.getLmCode(), mdl.getSubCode(), mdl.getAddress(), mdl.getCityCode(), mdl.getCityName(), mdl.getPlaceOfSupply(), mdl.getSiteStateCode(), mdl.getIdTypeCode(), mdl.getIdTypeName(), mdl.getIdNo(), mdl.getMobile(), mdl.isPatientsReqExists(), mdl.getPharmacyBalance(), mdl.getRegistrationBalance(), mdl.getOrganizationMdl(), mdl.getGrossAmt(), mdl.getDiscountAmt(), mdl.getDiscountAmt2(), mdl.getBillAmt(), mdl.getTaxableAmt(), mdl.getCgstAmt(), mdl.getSgstAmt(), mdl.getUgstAmt(), mdl.getRoundOffAmt(), mdl.getNetAmount(), mdl.getLandedValue(), mdl.getModifyReasonMdl(), mdl.getPrecVDescription(), mdl.getPrecVType(), mdl.getPrecAttachPaymentMode(), mdl.getReceiptTypeCode(), mdl.getReceiptTypeName(), mdl.getReceiptTypeCateory(), mdl.getFlagCardPayment(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.getSaleMedicine(), mdl.getCardPayment(), mdl.getLstFreeWithItemCodes(), mdl.getGrdMediDetails(), mdl.getLstRequisitionItemDetails(), mdl.getDefaultPharmacyServiceCode(), mdl.getInCorrectSelectedBatchSql(), mdl.getInCorrectSelectedBarcodeSql(), mdl.getLmName(), mdl.getLmManualCode(), mdl.getGstAmt(), mdl.getPaymentSource(),mdl.getPatientClassType(),mdl.getPatientPackagedInfo(),mdl.voucherTypeShortName);
    }

    public String getVoucherTypeShortName() {
        return voucherTypeShortName;
    }

    public void setVoucherTypeShortName(String voucherTypeShortName) {
        this.voucherTypeShortName = voucherTypeShortName;
    }

    public String getApprovalRequired() {
        return approvalRequired;
    }

    public void setApprovalRequired(String approvalRequired) {
        this.approvalRequired = approvalRequired;
    }

    public String getVoucherCategory() {
        return voucherCategory;
    }

    public void setVoucherCategory(String voucherCategory) {
        this.voucherCategory = voucherCategory;
    }

    public String getSiteCode_HIS() {
        return siteCode_HIS;
    }

    public void setSiteCode_HIS(String siteCode_HIS) {
        this.siteCode_HIS = siteCode_HIS;
    }

    public boolean isLinkParentDatabase() {
        return linkParentDatabase;
    }

    public void setLinkParentDatabase(boolean linkParentDatabase) {
        this.linkParentDatabase = linkParentDatabase;
    }
}
