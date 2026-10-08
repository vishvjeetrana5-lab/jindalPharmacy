package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmCSSD {

    public final String moduleName = "CSSD";

    public final MenuLink CSSD_ITEM_REGISTER = new MenuLink(moduleName, "CSSD Item Register");

    public final MenuLink ALLOW_ASSET_GROUP_IN_CSSD = new MenuLink(moduleName, "Allow Asset Group In CSSD");
    public final MenuLink ALLOT_EQUIPMENTS = new MenuLink(moduleName, "Allot Equipments");
    public final MenuLink RELEASE_EQUIPMENTS = new MenuLink(moduleName, "Release Equipments");
    public final MenuLink TRANSFER_EQUIPMENTS = new MenuLink(moduleName, "Transfer Equipments");

//== Reports    
    public final MenuLink EQUIPMENT_ALLOTMENT_REGISTER = new MenuLink(moduleName, "Equipment Allotment Register");
    public final MenuLink EQUIPMENT_RELEASE_PENDENCY_REGISTER = new MenuLink(moduleName, "Equipment Release Pendency Register");
//    public final MenuLink EQUIPMENT_RELEASE_REGISTER = new MenuLink(moduleName, "Equipment Release Register");
//    public final MenuLink EQUIPMENT_TRANSFER_REGISTER = new MenuLink(moduleName, "Equipment Transfer Register");

}
