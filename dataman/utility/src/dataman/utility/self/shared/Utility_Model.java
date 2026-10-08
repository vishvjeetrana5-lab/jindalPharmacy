package dataman.utility.self.shared;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.UserMenu;
import dataman.utility.overheadConfiguration.client.OverheadConfiguration;
import dataman.utility.overheadLineConfiguration.client.OverheadLineConfiguration;
import java.util.ArrayList;
import java.util.List;

public class Utility_Model {

    lmUtility pmMenu = new lmUtility();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Masters"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getGeneral(mainMenuList);
        getManageCompany(mainMenuList);
        getManageUser(mainMenuList);

        menuList.add(new UserMenu("Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getReports(mainMenuList);

        menuList.add(new UserMenu("Configs"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getConfigurations(mainMenuList);
        getTools(mainMenuList);

        return menuList;
    }

    private void getReports(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("General Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.USER_PERMISSION_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.DEPARTMENT_WISE_USER_REPORT.caption));

    }

//<editor-fold defaultstate="collapsed" desc="General">
    private void getGeneral(List<UserMenu> menuList) {
        menuList.add(new UserMenu("General"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CURRENCY_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.COUNTRY_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.STATE_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CITY_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.ZONE_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.NATIONALITY_MASTER.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manage Company">
    private void getManageCompany(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Manage Company"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.PRODUCT_LICENSED_TO.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.COMPANY_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.BRANCH_MASTER.caption));
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(DocIdMaster.widgetName));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.SITE_MASTER.caption));
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(NumberSystem.widgetName));
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(ProductLicensedTo.widgetName));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manage User">
    private void getManageUser(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Manage User"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.USER_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.USER_PERMISSION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.COPY_USER_PERMISSION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CHANGE_PASSWORD.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Configurations">
    private void getConfigurations(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Configurations"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OVERHEAD_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OVERHEAD_CONFIGURATION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().get(menuList.get(menuList.size() - 1).getUserMenu().size() - 1).getControlPermission().add(OverheadConfiguration.applicableByBlog);
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OVERHEAD_LINE_CONFIGURATION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().get(menuList.get(menuList.size() - 1).getUserMenu().size() - 1).getControlPermission().add(OverheadLineConfiguration.applicableByBlog);
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.ITEM_TAX_STRUCTURE.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.GST_STRUCTURE.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.GUI_TEMPLATE_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.GUI_CONFIGURATION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.WALK_THROUGH_MASTER.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Tools">
    private void getTools(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Tools"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.MANAGE_VOUCHER_TYPE.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.DOCID_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.NUMBER_SYSTEM.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.FILE_TYPE_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.MODIFY_REASON_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.MENU_CODIFICATION_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OSR_EXPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OSR_IMPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.DATA_BACKUP.caption));
    }
//</editor-fold>

}
