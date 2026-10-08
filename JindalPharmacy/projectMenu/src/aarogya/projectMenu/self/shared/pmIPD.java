package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmIPD {

    public final String moduleName = "IPD";

    public final MenuLink OPERATION_MASTER = new MenuLink(moduleName, "Operation Master");
    public final MenuLink ROOM_CATEGORY_MASTER = new MenuLink(moduleName, "Room Category Master");
    public final MenuLink ROOM_MASTER = new MenuLink(moduleName, "Room Master");
    public final MenuLink NURSING_STATION_MASTER = new MenuLink(moduleName, "Nursing Station Master");
    public final MenuLink DISCHARGE_CHECKLIST_MASTER = new MenuLink(moduleName, "Discharge Checklist Master");

//    public final MenuLink INITIAL_ASSESSMENT = new MenuLink(moduleName, "Initial Assessment");
    public final MenuLink ROOM_CHANGE = new MenuLink(moduleName, "Room Change");
    public final MenuLink CONSULTANT_CHANGE = new MenuLink(moduleName, "Consultant Change");
    public final MenuLink MLC = new MenuLink(moduleName, "MLC");
    public final MenuLink UHID = new MenuLink(moduleName, new pmOPD().UHID.caption);
    public final MenuLink ADMISSION = new MenuLink(moduleName, "Admission");
    public final MenuLink IPD_SERVICES = new MenuLink(moduleName, "IPD Services");
    public final MenuLink CANCEL_IPD_SERVICES = new MenuLink(moduleName, "Cancel IPD Services");
 //   public final MenuLink CANCEL_IPD_BILLED_SERVICES = new MenuLink(moduleName, "Cancel IPD Billed Services");
    public final MenuLink PACKAGE_ALLOCATION = new MenuLink(moduleName, "Package Allocation");
    public final MenuLink IPD_RECEIPT = new MenuLink(moduleName, "IPD Receipt");
    public final MenuLink IPD_REFUND = new MenuLink(moduleName, "IPD Refund");
    public final MenuLink DISCHARGE_CHECKLIST = new MenuLink(moduleName, "Discharge Checklist");
    public final MenuLink ADVANCE_BOOKING = new MenuLink(moduleName, "Advance Booking");
    public final MenuLink ADVANCE_BOOKING_REFUND = new MenuLink(moduleName, "Advance Booking Refund");
    public final MenuLink ADVANCE_ADJUSTMENT = new MenuLink(moduleName, "Advance Adjustment");
    public final MenuLink DISCHARGE = new MenuLink(moduleName, "Discharge");
    public final MenuLink DISCHARGE_CARD = new MenuLink(moduleName, "Discharge Card");
    public final MenuLink IPD_BILL_PROVISIONAL_NON_TPA = new MenuLink(moduleName, new pmTPA().IPD_BILL_PROVISIONAL_NON_TPA.caption);
    public final MenuLink IPD_BILL_NON_TPA = new MenuLink(moduleName, new pmTPA().IPD_BILL_NON_TPA.caption);
    public final MenuLink IPD_SERVICE_BILLABLE = new MenuLink(moduleName, new pmTPA().IPD_SERVICE_BILLABLE.caption);
    public final MenuLink MEDICAL_CERTIFICATE = new MenuLink(moduleName, "Medical Certificate");
    public final MenuLink DELIVERY = new MenuLink(moduleName, "Delivery");
    public final MenuLink ROOM_DIARY = new MenuLink(moduleName, "Room Diary");
    public final MenuLink HOURLY_SERVICE = new MenuLink(moduleName, "Hourly Service");
    public final MenuLink WARN_LIMIT = new MenuLink(moduleName, "Warn Limit");
    public final MenuLink SERVICE_REQUISITION = new MenuLink(moduleName, "Service Requisition");

    public final MenuLink DEATH_CERTIFICATE = new MenuLink(moduleName, "Death Certificate");

    public final MenuLink IDSP_REPORT = new MenuLink(moduleName, "IDSP Report");
    public final MenuLink IPD_REGISTRATION_REPORT = new MenuLink(moduleName, "Admission Report");
    public final MenuLink DISCHARGE_PATIENT_DUES_REPORT = new MenuLink(moduleName, "Discharge Patient Dues Report");
//    public final MenuLink DISCHARGE_PATIENT_REPORT = new MenuLink(moduleName, "Discharge Patient Report");
    public final MenuLink ADVANCE_BOOKING_REPORT = new MenuLink(moduleName, "Advance Booking Report");
//    public final MenuLink IPD_REFUND_REPORT = new MenuLink(moduleName, "IPD Refund Report");
//    public final MenuLink ROOM_CATEGORY_WISE_REPORT = new MenuLink(moduleName, "Room Category Wise Report");
    public final MenuLink DEPARTMENT_CENSUS_REPORT = new MenuLink(moduleName, "Department Census Report");
//    public final MenuLink CATEGORY_WISE_SERVICE_REPORT = new MenuLink(moduleName, "Category Wise Service Report");
//    public final MenuLink IPD_DISCOUNT_REPORT = new MenuLink(moduleName, "IPD Discount Report");
    public final MenuLink DISCHARGE_BILL_REPORT = new MenuLink(moduleName, "Discharge Bill Report");
    public final MenuLink IPD_PACKAGE_REPORT = new MenuLink(moduleName, "Package Report");
    public final MenuLink IPD_SERVICE_REPORT = new MenuLink(moduleName, "IPD Service Report");
    public final MenuLink DISCHARGE_PATIENT_STATUS_REPORT = new MenuLink(moduleName, "Discharge Patient Status Report");
    public final MenuLink PATIENT_RECEIPT_REPORT = new MenuLink(moduleName, "Patient Receipt Report");
//    public final MenuLink PATIENT_CONSULTANT_WISE_REPORT = new MenuLink(moduleName, "Patient Consultant Wise Report");
    public final MenuLink IN_PATIENT = new MenuLink(moduleName, "In Patient");
    public final MenuLink IN_PATIENT_WITHOUT_AMOUNT = new MenuLink(moduleName, "In Patient Without Amount");
    public final MenuLink DISCHARGE_PATIENT_DETAIL_REPORT = new MenuLink(moduleName, "Discharge Patient Detail Report");
//    public final MenuLink DISCHARGE_PATIENT_STATUS_DETAIL = new MenuLink(moduleName, "Patient Status Detail Report");
    public final MenuLink ROOM_CHANGE_REPORT = new MenuLink(moduleName, "Room Change Report");
    public final MenuLink BIRTH_REPORT = new MenuLink(moduleName, "Birth Report");
    public final MenuLink MTP_REGISTER_REPORT = new MenuLink(moduleName, "MTP Register");
    public final MenuLink DISCHARGE_MLC_REPORT = new MenuLink(moduleName, "Discharge MLC Report");
    public final MenuLink STATISTICAL_IPD_REPORT = new MenuLink(moduleName, "IPD Statistical Report");
    public final MenuLink DISCHARGE_STATISTICAL_REPORT = new MenuLink(moduleName, "Discharge Statistical Report");
    public final MenuLink OCCUPANCY_MONTHLY_REPORT = new MenuLink(moduleName, "Occupancy Monthly Report");
    public final MenuLink OCCUPANCY_MONTHWISE_REPORT = new MenuLink(moduleName, "Occupancy Monthwise Report");
    public final MenuLink CURRENT_IPD_STATISTICS_REPORT = new MenuLink(moduleName, "Current IPD Statistics Report");
    public final MenuLink GENDER_WISE_REPORT = new MenuLink(moduleName, "Gender Wise Report");
    public final MenuLink OCCUPANCY_RATE_REPORT = new MenuLink(moduleName, "Occupancy Report % wise");
    public final MenuLink UPDATE_PATIENT_DETAIL = new MenuLink(moduleName, "Update Patient Detail");
    public final MenuLink SUPPLEMENTARY_BILL = new MenuLink(moduleName, "Supplementary Bill");
    public final MenuLink IPD_BILL_PAYMENT_REFUND = new MenuLink(moduleName, "IPD Bill Payment Refund");
}
