package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmEmergency {

    public final String moduleName = "Emergency";

    public final MenuLink EMERGENCY = new MenuLink(moduleName, "Emergency");
    public final MenuLink EMERGENCY_SERVICES = new MenuLink(moduleName, "Emergency Services");
    public final MenuLink EMERGENCY_CANCEL_SERVICES = new MenuLink(moduleName, "Emergency Cancel Service");
    public final MenuLink MLC = new MenuLink(moduleName, new pmIPD().MLC.caption);

    public final MenuLink EMERGENCY_RECEIPT = new MenuLink(moduleName, "Emergency Receipt");
    public final MenuLink EMERGENCY_REFUND = new MenuLink(moduleName, "Emergency Refund");

    public final MenuLink DISCHARGE_CHECKLIST = new MenuLink(moduleName, new pmIPD().DISCHARGE_CHECKLIST.caption);
    public final MenuLink DISCHARGE = new MenuLink(moduleName, new pmIPD().DISCHARGE.caption);
    public final MenuLink DISCHARGE_CARD = new MenuLink(moduleName, new pmIPD().DISCHARGE_CARD.caption);
    public final MenuLink OPERATION_MASTER = new MenuLink(moduleName, new pmIPD().OPERATION_MASTER.caption);
    public final MenuLink ROOM_CATEGORY_MASTER = new MenuLink(moduleName, new pmIPD().ROOM_CATEGORY_MASTER.caption);
    public final MenuLink ROOM_MASTER = new MenuLink(moduleName, new pmIPD().ROOM_MASTER.caption);
    public final MenuLink NURSING_STATION_MASTER = new MenuLink(moduleName, new pmIPD().NURSING_STATION_MASTER.caption);
    public final MenuLink DISCHARGE_CHECKLIST_MASTER = new MenuLink(moduleName, new pmIPD().DISCHARGE_CHECKLIST_MASTER.caption);
    public final MenuLink SERVICE_REQUISITION = new MenuLink(moduleName, new pmIPD().SERVICE_REQUISITION.caption);

    public final MenuLink SERVICES_REPORT = new MenuLink(moduleName, "Services Report");
    public final MenuLink EMERGENCY_REPROT = new MenuLink(moduleName, "Emergency Report");

    public final MenuLink EMERGENCY_BILL_PROVISIONAL_NON_TPA = new MenuLink(moduleName, new pmTPA().EMERGENCY_BILL_PROVISIONAL_NON_TPA.caption);
    public final MenuLink EMERGENCY_BILL_NON_TPA = new MenuLink(moduleName, new pmTPA().EMERGENCY_BILL_NON_TPA.caption);
    public final MenuLink EMERGENCY_SERVICE_BILLABLE = new MenuLink(moduleName, new pmTPA().EMERGENCY_SERVICE_BILLABLE.caption);

//    public final MenuLink REFUND_REPORT = new MenuLink(moduleName, "Refund Report");
//    public final MenuLink EMERGENCY_PATIENT_CONSULTANT_WISE_REPORT = new MenuLink(moduleName, new pmIPD().PATIENT_CONSULTANT_WISE_REPORT.caption);
//    public final MenuLink ROOM_CATEGORY_WISE_EMERGENCY_REPORT = new MenuLink(moduleName, new pmIPD().ROOM_CATEGORY_WISE_REPORT.caption);
    public final MenuLink DEPARTMENT_CENCUS_REPORT = new MenuLink(moduleName, new pmIPD().DEPARTMENT_CENSUS_REPORT.caption);
//    public final MenuLink CATEGORY_WISE_SERVICE_REPORT = new MenuLink(moduleName, new pmIPD().CATEGORY_WISE_SERVICE_REPORT.caption);
    public final MenuLink DISCHARGE_PATIENT_STATUS_REPORT = new MenuLink(moduleName, new pmIPD().DISCHARGE_PATIENT_STATUS_REPORT.caption);
//    public final MenuLink PATIENT_STATUS_DETAIL_REPORT = new MenuLink(moduleName, "Patient Status Detail Report");
    public final MenuLink PATIENT_RECEIPT_REPORT = new MenuLink(moduleName, new pmIPD().PATIENT_RECEIPT_REPORT.caption);
    public final MenuLink DISCHARGE_PATIENT_DUES_REPORT = new MenuLink(moduleName, new pmIPD().DISCHARGE_PATIENT_DUES_REPORT.caption);
//    public final MenuLink DISCHARGE_PATIENT_REPORT = new MenuLink(moduleName, "Discharge Patient Report");
    public final MenuLink IN_PATIENT = new MenuLink(moduleName, new pmIPD().IN_PATIENT.caption);
    public final MenuLink DISCHARGE_BILL_REPORT = new MenuLink(moduleName, new pmIPD().DISCHARGE_BILL_REPORT.caption);
    public final MenuLink EMERGANCY_DISCHARGE_MLC_REPORT = new MenuLink(moduleName, new pmIPD().DISCHARGE_MLC_REPORT.caption);
    public final MenuLink EMERGANCY_GENDERWISE_REPORT = new MenuLink(moduleName, new pmIPD().GENDER_WISE_REPORT.caption);
    public final MenuLink EMERGENCY_ADMISSION_REPORT = new MenuLink(moduleName, "Emergency Admission Report");
    public final MenuLink EMERGENCY_DISCHARGE_PATIENT_DETAIL_REPORT = new MenuLink(moduleName, "Discharge Patient Detail");
    public final MenuLink EMERGENCY_DISCHARGE_PATIENT_DUES_REPORT = new MenuLink(moduleName, "Discharge Patient Duse Report");
    public final MenuLink EMERGENCY_DISCHARGE_BILL_REPORT = new MenuLink(moduleName, "Emergency Discharge Bill Report");
    public final MenuLink EMERGENCY_IN_PATIENT = new MenuLink(moduleName, "Emergency In Patient");
    public final MenuLink EMERGENCY_SERVICE_REPORT = new MenuLink(moduleName, "Emergency Service Report");
    public final MenuLink EMERGENCY_MLC_REPORT = new MenuLink(moduleName, "Emergency MLC Report");
    public final MenuLink GENDERWISE_EMERGENCY_ADMISSION_REPORT = new MenuLink(moduleName, "GenderWise Emergency Admission Report");
}
