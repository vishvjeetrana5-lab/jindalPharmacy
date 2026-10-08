package aarogya.pharmacy.saleBillReturnProvisional.shared;

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

public class SaleBillReturnProvisional_Mdl extends KeyField implements Serializable, IsSerializable {

    private String voucherTypeCode;
    private String documentNatureCode;
    private String documentTypeCode;
    private String voucherTypeName;
    private String typeCode;
    private String typeName;
    private String consultantCode;
    private String consultantName;
    private String voucherDate;
    private String uhidRecId;
    private String uhidDocid;
    private String regRecID;
    private String prescDocId;
    private String docIdSaleBill;
    private String recIdSaleBill;
    private String v_TypeSaleBill;
    private String net_AmtSaleBill;
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
    private String approvedRecord;
    private String userEntDtLatestLine;
    private String sbItemChangeCondition;
    private String cardPaymentPopupNetAmount;
    private String defaultPharmacyServiceCode;
    private String inCorrectSelectedBatchSql;
    private String inCorrectSelectedBarcodeSql;

    private String refNo;
    private String refDate;
    private String lmCode;
    private String pharmacyBalance;
    private String registrationBalance;
    private String siteStateCode;

    private String subCode;
    private String cityCode;
    private String cityName;
    private String gstReasonCode;
    private String gstReasonName;
    private String placeOfSupply;
    private String address;
    private String partyManualCode;
    private String idTypeCode, idTypeName, idNo;
    private String mobile;
    private String partyType;
    private String lmName;
    private String lmManualCode;
    private String grossAmt, discountAmt, billAmt, taxableAmt, cgstAmt, sgstAmt, ugstAmt, roundOffAmt, netAmount, landedValue;
    private String discountAmt2;

    private Organization_Mdl organizationMdl;
    private ModifyReason_Mdl modifyReasonMdl;

    private MedicineSaleOn saleMedicine = MedicineSaleOn.SALE_RATE;
    private CardPaymentPopup_Mdl cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
    private List<GrdMedicine_Mdl> GrdMediDetails = new ArrayList<GrdMedicine_Mdl>();
    private List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails = new ArrayList<GrdRequisitionItemDetails_Mdl>();

    private RecId recIdSaleBillReturn = new RecId();
    private RecId recIdRecipt = new RecId();

    private String precVDescription, precVType, precAttachPaymentMode, receiptTypeCode, receiptTypeName, receiptTypeCateory;
    private String flagCardPayment;
    private String gstAmt;
    private String paymentSource;

    public String getGstAmt() {
        return gstAmt;
    }

    public void setGstAmt(String gstAmt) {
        this.gstAmt = gstAmt;
    }

    public SaleBillReturnProvisional_Mdl() {
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

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
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

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getLmCode() {
        return lmCode;
    }

    public void setLmCode(String lmCode) {
        this.lmCode = lmCode;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

        public String getApprovedRecord() {
        return approvedRecord;
    }

    public void setApprovedRecord(String approvedRecord) {
        this.approvedRecord = approvedRecord;
    }

    private SaleBillReturnProvisional_Mdl(String voucherTypeCode, String documentNatureCode, String documentTypeCode, String voucherTypeName, String typeCode, String typeName, String consultantCode, String consultantName, String voucherDate, String uhidRecId, String uhidDocid, String regRecID, String prescDocId, String docIdSaleBill, String recIdSaleBill, String v_TypeSaleBill, String net_AmtSaleBill, String regDocid, String regSiteCode, String roomCode, String patientName, String patientIntial, String Remark, String departmentName, String departmentCode, String controlAccount, String roundOffAcCode, String cancelRecord,String approvedRecord, String userEntDtLatestLine, String sbItemChangeCondition, String cardPaymentPopupNetAmount, String refNo, String refDate, String lmCode, String pharmacyBalance, String registrationBalance, String subCode, String cityCode, String cityName, String gstReasonCode, String gstReasonName, String placeOfSupply, String siteStateCode, String address, String partyManualCode, String idTypeCode, String idTypeName, String idNo, String mobile, String partyType, String grossAmt, String discountAmt, String discountAmt2, String billAmt, String taxableAmt, String cgstAmt, String sgstAmt, String ugstAmt, String roundOffAmt, String netAmount, String landedValue, Organization_Mdl organizationMdl, ModifyReason_Mdl modifyReasonMdl, String precVDescription, String precVType, String precAttachPaymentMode, String receiptTypeCode, String receiptTypeName, String receiptTypeCateory, String flagCardPayment, String KeyFieldCode, String KeyFieldName, MedicineSaleOn saleMedicine, CardPaymentPopup_Mdl cardPaymentSaleBillReturn, List<GrdMedicine_Mdl> GrdMediDetails, List<GrdRequisitionItemDetails_Mdl> lstRequisitionItemDetails, String lmManualCode, String lmName, String inCorrectSelectedBatchSql, String inCorrectSelectedBarcodeSql, String defaultPharmacyServiceCode, String gstAmt, String paymentSource) {
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
        this.uhidRecId = uhidRecId;
        this.uhidDocid = uhidDocid;
        this.regRecID = regRecID;
        this.prescDocId = prescDocId;
        this.docIdSaleBill = docIdSaleBill;
        this.recIdSaleBill = recIdSaleBill;
        this.v_TypeSaleBill = v_TypeSaleBill;
        this.net_AmtSaleBill = net_AmtSaleBill;
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
        this.approvedRecord = approvedRecord;
        this.userEntDtLatestLine = userEntDtLatestLine;
        this.sbItemChangeCondition = sbItemChangeCondition;
        this.cardPaymentPopupNetAmount = cardPaymentPopupNetAmount;
        this.refNo = refNo;
        this.refDate = refDate;
        this.lmCode = lmCode;
        this.pharmacyBalance = pharmacyBalance;
        this.registrationBalance = registrationBalance;
        this.subCode = subCode;
        this.cityCode = cityCode;
        this.cityName = cityName;
        this.gstReasonCode = gstReasonCode;
        this.gstReasonName = gstReasonName;
        this.placeOfSupply = placeOfSupply;
        this.siteStateCode = siteStateCode;
        this.address = address;
        this.partyManualCode = partyManualCode;
        this.idTypeCode = idTypeCode;
        this.idTypeName = idTypeName;
        this.idNo = idNo;
        this.mobile = mobile;
        this.partyType = partyType;
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
        this.organizationMdl = organizationMdl != null ? new Organization_Mdl(organizationMdl) : null;
        this.modifyReasonMdl = modifyReasonMdl != null ? new ModifyReason_Mdl(modifyReasonMdl) : null;
        this.saleMedicine = saleMedicine;
        this.precVDescription = precVDescription;
        this.precVType = precVType;
        this.precAttachPaymentMode = precAttachPaymentMode;
        this.receiptTypeCode = receiptTypeCode;
        this.receiptTypeName = receiptTypeName;
        this.receiptTypeCateory = receiptTypeCateory;
        this.flagCardPayment = flagCardPayment;
        this.cardPaymentSaleBillReturn = cardPaymentSaleBillReturn != null ? cardPaymentSaleBillReturn.clone() : null;
        this.gstAmt = gstAmt;

        this.defaultPharmacyServiceCode = defaultPharmacyServiceCode;
        this.inCorrectSelectedBatchSql = inCorrectSelectedBatchSql;
        this.inCorrectSelectedBarcodeSql = inCorrectSelectedBarcodeSql;
        this.lmName = lmName;
        this.lmManualCode = lmManualCode;
        this.paymentSource = paymentSource;

        this.GrdMediDetails = new ArrayList<>();
        for (GrdMedicine_Mdl s : GrdMediDetails) {
            this.GrdMediDetails.add(new GrdMedicine_Mdl(s));
        }
        this.lstRequisitionItemDetails = new ArrayList<>();
        for (GrdRequisitionItemDetails_Mdl s : lstRequisitionItemDetails) {
            this.lstRequisitionItemDetails.add(new GrdRequisitionItemDetails_Mdl(s));
        }
    }

    public SaleBillReturnProvisional_Mdl(SaleBillReturnProvisional_Mdl mdl) {
        this(mdl.getVoucherTypeCode(), mdl.getDocumentNatureCode(), mdl.getDocumentTypeCode(), mdl.getVoucherTypeName(), mdl.getTypeCode(), mdl.getTypeName(), mdl.getConsultantCode(), mdl.getConsultantName(), mdl.getVoucherDate(), mdl.getUhidRecId(), mdl.getUhidDocid(), mdl.getRegRecID(), mdl.getPrescDocId(), mdl.getDocIdSaleBill(), mdl.getRecIdSaleBill(), mdl.getV_TypeSaleBill(), mdl.getNet_AmtSaleBill(), mdl.getRegDocid(), mdl.getRegSiteCode(), mdl.getRoomCode(), mdl.getPatientName(), mdl.getPatientIntial(), mdl.getRemark(), mdl.getDepartmentName(), mdl.getDepartmentCode(), mdl.getControlAccount(), mdl.getRoundOffAcCode(), mdl.getCancelRecord(),mdl.getApprovedRecord(), mdl.getUserEntDtLatestLine(), mdl.getSbItemChangeCondition(), mdl.getCardPaymentPopupNetAmount(), mdl.getRefNo(), mdl.getRefDate(), mdl.getLmCode(), mdl.getPharmacyBalance(), mdl.getRegistrationBalance(), mdl.getSubCode(), mdl.getCityCode(), mdl.getCityName(), mdl.getGstReasonCode(), mdl.getGstReasonName(), mdl.getPlaceOfSupply(), mdl.getSiteStateCode(), mdl.getAddress(), mdl.getPartyManualCode(), mdl.getIdTypeCode(), mdl.getIdTypeName(), mdl.getIdNo(), mdl.getMobile(), mdl.getPartyType(), mdl.getGrossAmt(), mdl.getDiscountAmt(), mdl.getDiscountAmt2(), mdl.getBillAmt(), mdl.getTaxableAmt(), mdl.getCgstAmt(), mdl.getSgstAmt(), mdl.getUgstAmt(), mdl.getRoundOffAmt(), mdl.getNetAmount(), mdl.getLandedValue(), mdl.getOrganizationMdl(), mdl.getModifyReasonMdl(), mdl.getPrecVDescription(), mdl.getPrecVType(), mdl.getPrecAttachPaymentMode(), mdl.getReceiptTypeCode(), mdl.getReceiptTypeName(), mdl.getReceiptTypeCateory(), mdl.getFlagCardPayment(), mdl.getSharedKeyFieldCode(), mdl.getSharedKeyFieldName(), mdl.getSaleMedicine(), mdl.getCardPaymentSaleBillReturn(), mdl.getGrdMediDetails(), mdl.getLstRequisitionItemDetails(), mdl.getLmManualCode(), mdl.getLmName(), mdl.getInCorrectSelectedBatchSql(), mdl.getInCorrectSelectedBarcodeSql(), mdl.getDefaultPharmacyServiceCode(), mdl.getGstAmt(), mdl.getPaymentSource());
    }

}
