package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmDayCare {

    public final String moduleName = "Day Care";

    public final MenuLink DAY_CARE_REGISTRATION = new MenuLink(moduleName, "Day Care Registration");
    public final MenuLink DAY_CARE_CONSULTATION = new MenuLink(moduleName, "Day Care Consultation");
    public final MenuLink DAY_CARE_SERVICES = new MenuLink(moduleName, "Day Care Services");
    public final MenuLink DAY_CARE_CANCEL_SERVICES = new MenuLink(moduleName, "Day Care Cancel Services");
//    public final MenuLink DAY_CARE_SCHEDULE = new MenuLink(moduleName, "Day Care Schedule");
    public final MenuLink DAY_CARE_BOOKING = new MenuLink(moduleName, "Day Care Booking");
    public final MenuLink DAY_CARE_RECEIPT = new MenuLink(moduleName, "Day Care Receipt");
    public final MenuLink DAY_CARE_DISCOUNT = new MenuLink(moduleName, "Day Care Discount");
    public final MenuLink DAY_CARE_REFUND = new MenuLink(moduleName, "Day Care Refund");
//    public final MenuLink DAY_CARE_MANAGE_SCHEDULE = new MenuLink(moduleName, "Manage Schedule");
    public final MenuLink DAY_CARE_PATIENT_FOLLOW_UP = new MenuLink(moduleName, "Patient Schedule Follow-up");
    public final MenuLink DAY_CARE_CASE_SUMMARY = new MenuLink(moduleName, "Day Care Case Summary");

    public final MenuLink SCHEDULE_REGISTER = new MenuLink(moduleName, "Schedule Register");
//    public final MenuLink CONSULTANT_WISE_REGISTRATION_REPORT = new MenuLink(moduleName, "Consultant Wise Registration Report");
    public final MenuLink DAY_CARE_SERVICES_REPORT = new MenuLink(moduleName, "Day Care Services Report");
    public final MenuLink DAY_CARE_REGISTER = new MenuLink(moduleName, "Day Care Register");
//    public final MenuLink REFUND_REPORT = new MenuLink(moduleName, "Refund Report");
    public final MenuLink DISCOUNT_REPORT = new MenuLink(moduleName, "Discount Report");
    public final MenuLink CONSULTATION_REPORT = new MenuLink(moduleName, "Consultaion Report");

    public final MenuLink DAY_CARE_BILL_PROVISIONAL_NON_TPA = new MenuLink(moduleName, new pmTPA().DAY_CARE_BILL_PROVISIONAL_NON_TPA.caption);
    public final MenuLink DAY_CARE_BILL_NON_TPA = new MenuLink(moduleName, new pmTPA().DAY_CARE_BILL_NON_TPA.caption);
    public final MenuLink DAY_CARE_SERVICE_BILLABLE = new MenuLink(moduleName, new pmTPA().DAY_CARE_SERVICE_BILLABLE.caption);

    public final MenuLink NURSING_STATION_MASTER = new MenuLink(moduleName, "Nursing Station Master");
    public final MenuLink ROOM_CATEGORY_MASTER = new MenuLink(moduleName, "Room Category Master");
    public final MenuLink ROOM_MASTER = new MenuLink(moduleName, "Room Master");
    public final MenuLink DAY_CARE_BOOKING_REPORT = new MenuLink(moduleName, "Booking Report");
    public final MenuLink DAY_CARE_DUES_REPORT = new MenuLink(moduleName, "Day Care Dues Report");
    public final MenuLink DAY_CARE_RECEIPT_REPORT = new MenuLink(moduleName, "Day Care Receipt Report");
}
