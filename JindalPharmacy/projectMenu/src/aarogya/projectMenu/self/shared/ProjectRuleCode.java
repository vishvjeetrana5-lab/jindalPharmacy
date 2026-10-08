package aarogya.projectMenu.self.shared;

import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;

public enum ProjectRuleCode {
    PURCHASE_ORDER("PO", "Purchase Order", "Documents", 5120, "pdf,docx,doc,txt,xls,xlsx"),
    QUOTATION("QOT", "Quotation", "Documents", 5120, "pdf,docx,doc,txt,xls,xlsx"),
    CONSULTATION("OPD", "Consultation, OPD Prescription", "Documents", 5120, "pdf,docx,doc,txt,xls,xlsx"),
    ASSETS("ASITM", "Asset Properties Master", "Documents", 5120, "docx"),
    DAY_CARE("CSSIM", "Manage Schedule, Patient FollowUp,Day Care Case Summary, Day Care Registration ", "Images", 1024, "jpg,png"),
    EMERGENCY("EMRG", "Discharge Card, Emergency", "image", 1012, "jpg,jpeg,png,gif,bmp"),
    HOUSE_KEEPING("LF", "Lost & Found ,Lost & Found Settlement", "Profile", 5120, "jpg,bmp,gif,jpeg"),
    PHARMACY_DIVISION("DVMST", "Division Master", "Images", 5120, "jpg,bmp,gif,jpeg"),
    MANUFACTURER("MFMST", "Manufacturer Master", "Documents", 5120, "docx"),
    SUPPLIER("SPMST", "Supplier Master", "Documents", 5120, "docx"),
    CUSTOMER("CTMST", "Customer Master", "Documents", 5120, "docx"),
    IPD("IPD", "Discharge Card ,Admission,Point Of Care, InitialAssessment", "Profile", 5120, "jpg,bmp,gif,jpeg"),
    IPDBILL("IPDBL", "IPD Bill [Non-TPA],IPD Bill [TPA]", "Profile", 5120, "jpg,bmp,gif,jpeg"),
    CONSULTANT("DRPIC", "Department Master,ReferredMaster", "Profile", 5120, "jpg,bmp,gif,jpeg"),
    EMPLOYEE("EMPFI", "Employee Master", "Documents", 5120, "docx"),
    UHID("UHID", "Patient File,UHID Modification", "Profile", 5012, "jpg,png,bmp,jpeg"),
    MRD("FLREG", "File Registration", "File Regis", 10240, "pdf,docx,xls,xlsx"),
    CAMERA("SSHOT", "Donor Register,Billing Forecast,CPOE,Current Occupancy,"
            + "Day Care Schedule,Dietitian,Day Care Case Summary,Day Care Registration,Discharge Card, "
            + "Emergency,Admission,Employee Master,Point Of Care,Consultation,Patient File,Prescription,UHID Modification",
            "Snapshot", 5120, "jpg,bmp,jpeg"),
    RADIOLOGY_TEST("DGRAD", "Environment Setting, Radiology Test", "Report", 5120, "docx,dotx"),
    PATHOLOGY_TEST("DGRPT", "Environment Setting, Radiology Test", "Report", 5120, "pdf"),
    OT_SURGICAL_REGISTRATION("OTSRR", "Surgical Registration", "Profile", 5120, "pg,bmp,gif,jpeg"),
    PATIENT_SCHEDULE_FOLLOW("MPSCH", "Manage Schedule, Patient Follow-Up", "Images", 1024, "jpg,png"),
    DONOR_REGISTRATION("DNRG", "Donor Registration", "Profile", 5120, "pg,bmp,gif,jpeg"),
    LAB_TEST_VALUE("LABTV", "Lab Test Value", "Documents", 5120, "doc,docx,pdf"),
    PURCHASE_CHALLAN_ENTRY("PUCHL", "GRN", "Documents", 5120, "doc,docx,pdf"),
    PURCHASE_BILL_DIRECT_ENTRY("PURBD", "Purchase Bill Direct, Purchase Bill", "Documents", 5120, "doc,docx,pdf"),
    QUOTATION_ENTRY("QOT", "Quotation", "Documents", 5120, "doc,docx,pdf"),
    TPADISPATCH("DPBLL", "TPA Bill Dispatch", "Profile", 5120, "jpg,bmp,gif,jpeg"),
    
    FIRST_WEIGHMENT("FWGMT", "First Weighment", "Images", 5120, "png,jpeg"),
    SECOND_WEIGHMENT("SWGMT", "Second Weighment", "Images", 5120, "png,jpeg"),
    MEDICAL_RECORD_REGISTER("MDFLR", "Medical Record Issue Register", "Documents", 10240, "pdf,docx,xls,xlsx"),;

    private String ruleCode, description, fileType, filters;
    private int size;

    private ProjectRuleCode(String ruleCode, String description, String fileType, int size, String filters) {
        this.ruleCode = ruleCode;
        this.description = description;
        this.fileType = fileType;
        this.size = size;
        this.filters = filters;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public String getDescription() {
        return description;
    }

    public String getFileType() {
        return fileType;
    }

    public int getSize() {
        return size;
    }

    public String getFilters() {
        return filters;
    }

    public static ProjectRuleCode getProjectRuleCode(String ruleCode) {
        if (!DmBaseShared.xNull(ruleCode).equalsIgnoreCase("")) {
            ruleCode = DmBaseShared.removeSpace(ruleCode).replace(" ", "");
            for (ProjectRuleCode rule : values()) {
                if (ruleCode.equalsIgnoreCase(rule.ruleCode)) {
                    return rule;
                }
            }
        }
        return null;
    }

}
