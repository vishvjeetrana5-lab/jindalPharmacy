package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmDiagnostic {

    public final String moduleName = "Diagnostic";

    public final MenuLink UNIT_MASTER = new MenuLink(moduleName, new pmInventory().UNIT_MASTER.caption);
    public final MenuLink RESULT_REMARK_MASTER = new MenuLink(moduleName, "Result Remark Master");
    public final MenuLink SAMPLE_MASTER = new MenuLink(moduleName, "Sample Master");
    public final MenuLink TEST_SENSITIVITY_MASTER = new MenuLink(moduleName, "Test Sensitivity Master");
    public final MenuLink PATHOLOGY_TEST_MASTER = new MenuLink(moduleName, "Pathology Test Master");
    public final MenuLink RADIOLOGY_TEST_MASTER = new MenuLink(moduleName, "Radiology Test Master");
    public final MenuLink SAMPLE_COLLECTION = new MenuLink(moduleName, "Sample Collection");
    public final MenuLink SAMPLE_LAB_COLLECTION = new MenuLink(moduleName, "Sample Lab Collection");
    public final MenuLink TEST_SERIAL_NO_ADJUSTMENT = new MenuLink(moduleName, "Test Serial No. Adjustment");
    public final MenuLink F_ENTRY = new MenuLink(moduleName, "F Entry");
    public final MenuLink LAB_STATUS = new MenuLink(moduleName, "Lab Status");
    public final MenuLink LAB_REGISTRATION = new MenuLink(moduleName, "Lab Registration");
    public final MenuLink LAB_REGISTRATION_MODIFICATION = new MenuLink(moduleName, "Lab Registration Modification");
    public final MenuLink LAB_CONSUMPTION = new MenuLink(moduleName, "Lab Redo / Calibration");
    public final MenuLink LAB_TEST_VALUE = new MenuLink(moduleName, "Lab Test Value");
    public final MenuLink LAB_TEST_VALUE_APPROVAL = new MenuLink(moduleName, "Lab Test Value Approval");
    public final MenuLink LAB_REPORT_UPLOADING = new MenuLink(moduleName, "Lab Report Uploading");
    public final MenuLink CANCEL_TEST = new MenuLink(moduleName, "Cancel Test");
    public final MenuLink LAB_DISCOUNT = new MenuLink(moduleName, "LAB Discount");
    public final MenuLink DIAGNOSTIC_RECEIPT = new MenuLink(moduleName, "Diagnostic Receipt");
    public final MenuLink DIAGNOSTIC_REFUND = new MenuLink(moduleName, "Diagnostic Refund");
    public final MenuLink DIAGNOSTIC_CATEGORY_MASTER = new MenuLink(moduleName, "Diagnostic Category Master");
    public final MenuLink DIAGNOSTIC_GROUP_MASTER = new MenuLink(moduleName, "Diagnostic Group Master");
    public final MenuLink PACKAGE_PANEL_MASTER = new MenuLink(moduleName, "Package / Panel Master");
    public final MenuLink PACKAGE_PANEL_PRICE_LIST = new MenuLink(moduleName, "Package / Panel Price List");
    public final MenuLink SAMPLE_ID_GROUPING = new MenuLink(moduleName, "Sample Id Grouping");

//    public final MenuLink AREA_WISE_LAB_REGISTRATION_REPORT = new MenuLink(moduleName, "Area Wise Lab Registration Report");
//    public final MenuLink GROUP_WISE_CENSUS_REPORT = new MenuLink(moduleName, "Group Wise Census Report");
//    public final MenuLink GROUP_WISE_LAB_SUMMARY_REPORT = new MenuLink(moduleName, "Group Wise Lab Summary Report");
//    public final MenuLink GROUP_WISE_TEST_WISE_REPORT = new MenuLink(moduleName, "Group Wise Test Wise Report");
//    public final MenuLink COLLECTION_CENTER_WISE_LAB_REPORT = new MenuLink(moduleName, "Collection Center Wise Lab Report");
    public final MenuLink INVESTIGATION_REPORT = new MenuLink(moduleName, "Investigation Report");
    public final MenuLink DIAGNOSTIC_DUES_REPORT = new MenuLink(moduleName, "Diagnostic Dues Report");
    public final MenuLink CONSULTANT_WISE_INVESTIGATION_REPORT = new MenuLink(moduleName, "Consultant Wise Investigation Report");
    public final MenuLink LAB_SUMMARY_REPORT = new MenuLink(moduleName, "Lab Summary Report");
    public final MenuLink LAB_DISCOUNT_REPORT = new MenuLink(moduleName, "Lab Discount Report");
//    public final MenuLink LAB_DUES_REPORT = new MenuLink(moduleName, "Lab Dues Report");
//    public final MenuLink LAB_REFUND_REPORT = new MenuLink(moduleName, "Lab Refund Report");
//    public final MenuLink MONTH_SUMMARY_REPORT = new MenuLink(moduleName, "Month Summary Report");
    public final MenuLink PATIENT_TEST_REPORT = new MenuLink(moduleName, "Patient Test Report");
//    public final MenuLink PATIENT_CONSULTANT_WISE_REPORT = new MenuLink(moduleName, "Consultant Wise Registration Report");
    public final MenuLink PENDING_INVESTIGATION_REPORT = new MenuLink(moduleName, "Pending Investigation Report");
    public final MenuLink CRITICAL_INVESTIGATION_REPORT = new MenuLink(moduleName, "Critical Investigation Report");
//    public final MenuLink TEST_WISE_INCENTIVE_REPORT = new MenuLink(moduleName, "Test Wise Incentive Report");
    public final MenuLink TURN_AROUND_TIME_REPORT = new MenuLink(moduleName, "Turn Around Time Report");
    public final MenuLink FEntry_REPORT = new MenuLink(moduleName, "Form - F Report");
    public final MenuLink WORKSHEET_REPORT = new MenuLink(moduleName, "WorkSheet Report");
//    public final MenuLink COLLECTION_REGISTER = new MenuLink(moduleName, "Collection Register");
//    public final MenuLink COLLECTION_REGISTER_USERWISE = new MenuLink(moduleName, "Collection Register (User Wise)");
//    public final MenuLink DEPARTMENT_PATIENT_STATISTICAL_REPORT = new MenuLink(moduleName, "Department Patient Statistical Report");
//    public final MenuLink LAB_LEDGER_REPORT = new MenuLink(moduleName, "Lab Ledger Report");

    public final MenuLink CATEGORY_WISE_COLLECTION_REPORT = new MenuLink(moduleName, "Category Wise Collection Report");

    public final MenuLink SEND_CRITICAL_VALUE_ALERT = new MenuLink(moduleName, "Send Critical Value Alert");
    public final MenuLink SERVICE_ACKNOWLEDGEMENT = new MenuLink(moduleName, "Serivce Acknowledgement");
    public final MenuLink DIAGNOSTIC_LEDGER = new MenuLink(moduleName, "Diagnostics Ledger");
}
