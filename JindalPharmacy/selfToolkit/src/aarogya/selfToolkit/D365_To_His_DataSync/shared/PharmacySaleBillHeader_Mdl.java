package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import java.util.List;

public class PharmacySaleBillHeader_Mdl {

    private String docId;
    private String entryType;
    private String entryNumber;
    private String entryDate;
    private String documentType;
    private String documentNumber;
    private String d365DepartmentCode;
    private String departmentName;
    private String locationCode;
    private String locationName;
    private String businessUnit;
    private String legalEntity;
    private String consultantName;
    private String speciality;
    private String patientUHID;
    private String patientRegNo;
    private String patientName;
    private String patientMobile;
    private String billDate;
    private String billNo;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    
    private String oh_Amt_Gross,oh_Amt_Discount,oh_Amt_Taxable,oh_Amt_CGST,oh_Amt_SGST,oh_Amt_UGST,oh_Amt_IGST,oh_Amt_RoundOff,oh_Amt_Net,preparedBy,billTime;
     
    private List<PharmacySaleBillDetail_Mdl> lineDetails;

    public PharmacySaleBillHeader_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getD365DepartmentCode() {
        return d365DepartmentCode;
    }

    public void setD365DepartmentCode(String d365DepartmentCode) {
        this.d365DepartmentCode = d365DepartmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getPatientUHID() {
        return patientUHID;
    }

    public void setPatientUHID(String patientUHID) {
        this.patientUHID = patientUHID;
    }

    public String getPatientRegNo() {
        return patientRegNo;
    }

    public void setPatientRegNo(String patientRegNo) {
        this.patientRegNo = patientRegNo;
    }



    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public List<PharmacySaleBillDetail_Mdl> getLineDetails() {
        return lineDetails;
    }

    public void setLineDetails(List<PharmacySaleBillDetail_Mdl> lineDetails) {
        this.lineDetails = lineDetails;
    }

    public String getOh_Amt_Gross() {
        return oh_Amt_Gross;
    }

    public void setOh_Amt_Gross(String oh_Amt_Gross) {
        this.oh_Amt_Gross = oh_Amt_Gross;
    }

    public String getOh_Amt_Discount() {
        return oh_Amt_Discount;
    }

    public void setOh_Amt_Discount(String oh_Amt_Discount) {
        this.oh_Amt_Discount = oh_Amt_Discount;
    }

    public String getOh_Amt_Taxable() {
        return oh_Amt_Taxable;
    }

    public void setOh_Amt_Taxable(String oh_Amt_Taxable) {
        this.oh_Amt_Taxable = oh_Amt_Taxable;
    }

    public String getOh_Amt_CGST() {
        return oh_Amt_CGST;
    }

    public void setOh_Amt_CGST(String oh_Amt_CGST) {
        this.oh_Amt_CGST = oh_Amt_CGST;
    }

    public String getOh_Amt_SGST() {
        return oh_Amt_SGST;
    }

    public void setOh_Amt_SGST(String oh_Amt_SGST) {
        this.oh_Amt_SGST = oh_Amt_SGST;
    }

    public String getOh_Amt_UGST() {
        return oh_Amt_UGST;
    }

    public void setOh_Amt_UGST(String oh_Amt_UGST) {
        this.oh_Amt_UGST = oh_Amt_UGST;
    }

    public String getOh_Amt_IGST() {
        return oh_Amt_IGST;
    }

    public void setOh_Amt_IGST(String oh_Amt_IGST) {
        this.oh_Amt_IGST = oh_Amt_IGST;
    }

    public String getOh_Amt_Net() {
        return oh_Amt_Net;
    }

    public void setOh_Amt_Net(String oh_Amt_Net) {
        this.oh_Amt_Net = oh_Amt_Net;
    }

    public String getPreparedBy() {
        return preparedBy;
    }

    public void setPreparedBy(String preparedBy) {
        this.preparedBy = preparedBy;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public String getOh_Amt_RoundOff() {
        return oh_Amt_RoundOff;
    }

    public void setOh_Amt_RoundOff(String oh_Amt_RoundOff) {
        this.oh_Amt_RoundOff = oh_Amt_RoundOff;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

}
