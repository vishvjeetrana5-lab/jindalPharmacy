package dataman.webJobs.self.shared;

import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.dmBase.self.shared.UserMenu;
import dataman.webJobs.userEmailConfiguration.client.UserEmailConfiguration;
import java.util.ArrayList;
import java.util.List;

public class WebJobs_Model {

    lmWebJobs pmMenu = new lmWebJobs();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Masters"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getGeneral(mainMenuList);

        menuList.add(new UserMenu("Transactions"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getManageSMS(mainMenuList);
        getManageEMail(mainMenuList);

        return menuList;
    }

//<editor-fold defaultstate="collapsed" desc="General">
    //  public final static String templateGroupMaster = "Template Group Master";
    private void getGeneral(List<UserMenu> menuList) {
        menuList.add(new UserMenu("General"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.ENVIRONMENT_SETTING.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.TEMPLATE_GROUP_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.SCHEDULER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.USER_EMAIL_CONFIGURATION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().get(menuList.get(menuList.size() - 1).getUserMenu().size() - 1).getControlPermission().add(UserEmailConfiguration.userEditableBlog);
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manage SMS">
    private void getManageSMS(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Manage SMS"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.SMS_TEMPLATE_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.SMS_MANAGER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.NOTIFICATION_ENTRY.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Manage eMail">
    private void getManageEMail(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Manage eMail"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.EMAIL_TEMPLATE_MASTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.EMAIL_MANAGER.caption));

    }
//</editor-fold>

}
