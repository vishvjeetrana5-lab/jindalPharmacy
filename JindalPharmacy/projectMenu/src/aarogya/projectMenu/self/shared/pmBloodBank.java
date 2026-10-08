package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmBloodBank {

    public final String moduleName = "Blood Bank";

    public final MenuLink BLOOD_DONATION_CAMP_MASTER = new MenuLink(moduleName, "Blood Donation Camp Master");
    public final MenuLink COMPONENT_MASTER = new MenuLink(moduleName, "Component Master");
    public final MenuLink DONATION_INVESTIGATION_MAPPING = new MenuLink(moduleName, "Donation Investigation Mapping");
    public final MenuLink DONATION_TEMPLATE_MASTER = new MenuLink(moduleName, "Donation Template Master");

    public final MenuLink DONOR_REGISTRATION = new MenuLink(moduleName, "Donor Registration");
    public final MenuLink DONOR_SCREENING = new MenuLink(moduleName, "Donor Screening");

    public final MenuLink BLOOD_TEST_RESULT = new MenuLink(moduleName, "Blood Test Result");
    public final MenuLink CREATE_COMPONENT = new MenuLink(moduleName, "Create Component");
    public final MenuLink DISPOSE_COMPONENT = new MenuLink(moduleName, "Dispose Component");
    public final MenuLink BLOOD_COMPONENT_TRANSFER = new MenuLink(moduleName, "Blood Component Transfer");

    public final MenuLink COMPONENT_REQUISITION = new MenuLink(moduleName, "Component Requisition");
    
    public final MenuLink BLOOD_COMPONENT_REQUISITION = new MenuLink(moduleName, "Blood Requisition");
    public final MenuLink BLOOD_COMPONENT_ISSUE = new MenuLink(moduleName, "Blood Issue");
    
    public final MenuLink COMPONENT_ISSUE = new MenuLink(moduleName, "Component Issue");
    public final MenuLink COMPONENT_RETURN = new MenuLink(moduleName, "Component Return");

    public final MenuLink COMPONENT_ISSUE_PATIENT = new MenuLink(moduleName, "Component Issue Patient");

    //Reports
    public final MenuLink DONOR_REGISTER = new MenuLink(moduleName, "Donor Register");
    public final MenuLink COMPONENT_STOCK = new MenuLink(moduleName, "Component Stock");
    public final MenuLink BLOOD_RECEIVE_ACKNOWLEDGEMENT = new MenuLink(moduleName, "Blood Receive Ack.");

}
