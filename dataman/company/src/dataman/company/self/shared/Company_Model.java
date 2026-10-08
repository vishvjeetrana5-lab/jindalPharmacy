package dataman.company.self.shared;

import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class Company_Model {

//    public static final String moduleName = "Company";

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();

        getManageAcconts(menuList);

        return menuList;
    }

//<editor-fold defaultstate="collapsed" desc="Manage Accounts">
    private  void getManageAcconts(List<UserMenu> menuList) {
//        menuList.add(new UserMenu("Manage Accounts"));
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(AccountGroup.widgetName));
//
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(groupLedger));
//
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(AccountMaster.widgetName));
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(AddressBook.widgetName));
//
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu("J"));
//        menuList.get(menuList.size() - 1).getUserMenu().get(menuList.get(menuList.size() - 1).getUserMenu().size() - 1).getUserMenu().add(new UserMenu(AccountGroup.widgetName + "_1"));
//        menuList.get(menuList.size() - 1).getUserMenu().get(menuList.get(menuList.size() - 1).getUserMenu().size() - 1).getUserMenu().add(new UserMenu(groupLedger + "_2"));
    }
//</editor-fold>

}
