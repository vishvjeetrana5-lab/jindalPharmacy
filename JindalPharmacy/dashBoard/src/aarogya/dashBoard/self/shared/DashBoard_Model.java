package aarogya.dashBoard.self.shared;

import aarogya.projectMenu.self.shared.pmDashBoard;
import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class DashBoard_Model {

    pmDashBoard pmMenu = new pmDashBoard();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Display"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        getDashboard(mainMenuList);
//        analyticsDashboard(mainMenuList);

        return menuList;
    }

    //<editor-fold defaultstate="collapsed" desc="Dashboard">
    private void getDashboard(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Dashboards"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.INVENTORY_PENDING_PO.caption));
        subMenuList.add(new UserMenu(pmMenu.PENDING_INDENT_FOR_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.PENDING_INDENT.caption));
        subMenuList.add(new UserMenu(pmMenu.NEAR_EXPIRY_MEDICINE.caption));
        subMenuList.add(new UserMenu(pmMenu.MEDICINE_STOCK_OUT.caption));
        subMenuList.add(new UserMenu(pmMenu.NON_SOLD_MEDICINE.caption));
        subMenuList.add(new UserMenu(pmMenu.FAST_MOVING_MEDICINE.caption));

        subMenuList.add(new UserMenu(pmMenu.TOTAL_CASH_COLLECTION.caption));
        subMenuList.add(new UserMenu(pmMenu.TOTAL_REFUND_FOR_DATE.caption));

        subMenuList.add(new UserMenu(pmMenu.BILL_PENDING_FOR_GRN.caption));
        subMenuList.add(new UserMenu(pmMenu.BILL_PENDING_FOR_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.BILL_DUE_NOT_PAID.caption));
        subMenuList.add(new UserMenu(pmMenu.ADV_PENDING_FOR_SETTLEMENT.caption));
        subMenuList.add(new UserMenu(pmMenu.DATA_MODIFICATION_LOG.caption));
        subMenuList.add(new UserMenu(pmMenu.WHOLE_SALE_COLLECTION_DASHBOARD.caption));

    }
    //</editor-fold>

}
