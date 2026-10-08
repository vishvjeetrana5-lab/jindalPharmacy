package dataman.libraryMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public  class lmUtility {
 
    public final String moduleName = "Utility";

    public final MenuLink PRODUCT_LICENSED_TO = new MenuLink(moduleName, "Product Licensed To");
    public final MenuLink COMPANY_MASTER = new MenuLink(moduleName, "Company Master");
    public final MenuLink BRANCH_MASTER = new MenuLink(moduleName, "Branch Master");
    public final MenuLink USER_MASTER = new MenuLink(moduleName, "User Master");
    public final MenuLink SITE_MASTER = new MenuLink(moduleName, "Site Master");
    public final MenuLink USER_PERMISSION = new MenuLink(moduleName, "User Permission");
    public final MenuLink COPY_USER_PERMISSION = new MenuLink(moduleName, "Copy User Permission");
    public final MenuLink CHANGE_PASSWORD = new MenuLink(moduleName, "Change Password");
    public final MenuLink OVERHEAD_MASTER = new MenuLink(moduleName, "Overhead Master");
    public final MenuLink OVERHEAD_CONFIGURATION = new MenuLink(moduleName, "Overhead Configuration");
    public final MenuLink OVERHEAD_LINE_CONFIGURATION = new MenuLink(moduleName, "Overhead Line Configuration");
    public final MenuLink ITEM_TAX_STRUCTURE = new MenuLink(moduleName, "Item Tax Structure");
    public final MenuLink GST_STRUCTURE = new MenuLink(moduleName, "GST Tax Structure");
    public final MenuLink GUI_TEMPLATE_MASTER = new MenuLink(moduleName, "GUI Template Master");
    public final MenuLink GUI_CONFIGURATION = new MenuLink(moduleName, "GUI Configuration");
    public final MenuLink WALK_THROUGH_MASTER = new MenuLink(moduleName, "Walk Through Master");
    public final MenuLink MANAGE_VOUCHER_TYPE = new MenuLink(moduleName, "Manage Voucher Type");
    public final MenuLink DOCID_MASTER = new MenuLink(moduleName, "DocId Master");
    public final MenuLink NUMBER_SYSTEM = new MenuLink(moduleName, "Number System");
    public final MenuLink FILE_TYPE_MASTER = new MenuLink(moduleName, "File Type Master");
    public final MenuLink MODIFY_REASON_MASTER = new MenuLink(moduleName, "Modify Reason Master");
    public final MenuLink MENU_CODIFICATION_MASTER = new MenuLink(moduleName, "Menu Codification Master");
    public final MenuLink OSR_EXPORT = new MenuLink(moduleName, "OSR Export");
    public final MenuLink OSR_IMPORT = new MenuLink(moduleName, "OSR Import");
    public final MenuLink DATA_BACKUP = new MenuLink(moduleName, "Data Backup");
    public final MenuLink USER_PERMISSION_REPORT = new MenuLink(moduleName, "User Permission Report");
    
    public final MenuLink CURRENCY_MASTER = new MenuLink(moduleName, new lmAccounts().CURRENCY_MASTER.caption);
    public final MenuLink COUNTRY_MASTER = new MenuLink(moduleName, new lmAccounts().COUNTRY_MASTER.caption);
    public final MenuLink STATE_MASTER = new MenuLink(moduleName, new lmAccounts().STATE_MASTER.caption);
    public final MenuLink CITY_MASTER = new MenuLink(moduleName, new lmAccounts().CITY_MASTER.caption);
    public final MenuLink ZONE_MASTER = new MenuLink(moduleName, new lmAccounts().ZONE_MASTER.caption);
    public final MenuLink NATIONALITY_MASTER = new MenuLink(moduleName, new lmAccounts().NATIONALITY_MASTER.caption);
    public final MenuLink DEPARTMENT_WISE_USER_REPORT = new MenuLink(moduleName, "Department Wise User Report");
}
