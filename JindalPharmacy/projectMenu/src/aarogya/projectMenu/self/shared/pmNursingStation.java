package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmNursingStation {

    public final String moduleName = "Nursing Station";

    public final MenuLink POINT_OF_CARE = new MenuLink(moduleName, "Point Of Care");
    public final MenuLink INITIAL_ASSESSMENT = new MenuLink(moduleName, "Initial Assessment");
    public final MenuLink EMR = new MenuLink(moduleName, "EMRs");
    public final MenuLink ASSIGN_STAFF = new MenuLink(moduleName, "Assign Staff");
//    public final MenuLink STAFF_HANDOVER = new MenuLink(moduleName, "Staff Handover");
    public final MenuLink NURSING_STATION_HANDOVER = new MenuLink(moduleName, "Nursing Station Handover");
    public final MenuLink SALE_BILL_RETURN_PROVISIONAL = new MenuLink(moduleName, "Medicine Return Request");
    public final MenuLink SEND_REFERRAL_ALERT = new MenuLink(moduleName, "Send Referal Alert");
    
    public final MenuLink FILE_REQUISITION = new MenuLink(moduleName, new pmMRD().FILE_REQUISITION.caption);

    public final MenuLink SEND_TO_MRD = new MenuLink(moduleName, "Send To MRD");

}
