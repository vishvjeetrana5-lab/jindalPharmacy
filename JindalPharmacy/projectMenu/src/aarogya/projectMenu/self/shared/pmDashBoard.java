package aarogya.projectMenu.self.shared;

import dataman.dmBase.self.shared.MenuLink;

public class pmDashBoard {

    public final String moduleName = "Dashboard";

    public final MenuLink WALK_THROUGH = new MenuLink(moduleName, "Walk Through");
    public final MenuLink MEDICINE_REQUISITION = new MenuLink(moduleName, "Medicine Requisition");
    public final MenuLink ITEM_REQUISITION = new MenuLink(moduleName, "Item Requisition");
    public final MenuLink DATA_MODIFICATION_LOG = new MenuLink(moduleName, "Data Modification Log");
    public final MenuLink INVENTORY_PENDING_PO = new MenuLink(moduleName, "Inv. Pending PO");
    public final MenuLink PENDING_INDENT_FOR_APPROVAL = new MenuLink(moduleName, "Indent For Approval");
    public final MenuLink PENDING_INDENT = new MenuLink(moduleName, "Pending Indent");
    public final MenuLink NEAR_EXPIRY_MEDICINE = new MenuLink(moduleName, "Near Expiry Medicine");
    public final MenuLink MEDICINE_STOCK_OUT = new MenuLink(moduleName, "Medi. Stock Out");
    public final MenuLink NON_SOLD_MEDICINE = new MenuLink(moduleName, "Non Sold Medicine");
    public final MenuLink FAST_MOVING_MEDICINE = new MenuLink(moduleName, "Fast Moving Medicine");
    //
    public final MenuLink ADV_PENDING_FOR_SETTLEMENT = new MenuLink(moduleName, "Adv. For Bill stlmnt.");
    public final MenuLink BILL_DUE_NOT_PAID = new MenuLink(moduleName, "Bill Due Not Paid");
    public final MenuLink BILL_PENDING_FOR_APPROVAL = new MenuLink(moduleName, "Inv./Ph. Bill For Aprl.");
    public final MenuLink BILL_PENDING_FOR_GRN = new MenuLink(moduleName, "Bill Pending For GRN");
    public final MenuLink TOTAL_CASH_COLLECTION = new MenuLink(moduleName, "Total Cash Collection");
    public final MenuLink TOTAL_REFUND_FOR_DATE = new MenuLink(moduleName, "Total Refund ForDate");
    public final MenuLink WHOLE_SALE_COLLECTION_DASHBOARD = new MenuLink(moduleName, "WS Collection");

}
