package aarogya.dashBoard.self.client;

import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.MenuLink;
import java.util.List;

public class DashBoard_Client {

    public static void addToList(String caption, MenuLink menuLink, List<MenuLink> menuLinkList) {
        MenuLink menuLinkNew;

        if (DmClient.searchMenu(menuLink.moduleName, menuLink.caption) != null) {
            menuLinkNew = new MenuLink(menuLink.moduleName, caption, menuLink.caption);
            menuLinkList.add(menuLinkNew);
        }
    }

    public static void addToList(MenuLink menuLink, List<MenuLink> menuLinkList) {

        addToList(menuLink.caption, menuLink, menuLinkList);
    }
}
