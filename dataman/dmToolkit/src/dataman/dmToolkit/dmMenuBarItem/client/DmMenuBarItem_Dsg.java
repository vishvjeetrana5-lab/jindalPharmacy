package dataman.dmToolkit.dmMenuBarItem.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmMenuBarItem_Dsg {

    final PopupPanel menuContainerPopupPanel = new PopupPanel(true, false);
    final FlowPanel menuBarItemFlw = new FlowPanel();
    final FlowPanel menuContainerFlw = new FlowPanel();
    final FlowPanel menuParentContainerFlw = new FlowPanel();
    final Anchor menuBarItemName = new Anchor();

    public DmMenuBarItem_Dsg(UserMenu userMenu) {
        FlowPanel flw = new FlowPanel();
        menuBarItemFlw.addStyleName("dm-head-menu-bar-item");
        menuBarItemName.setText(DmShared.xNull(userMenu.getMenuName()));
        flw.add(menuBarItemName);
        menuBarItemFlw.add(flw);

        menuContainerFlw.addStyleName("dm-menu-container");
        menuParentContainerFlw.add(menuContainerFlw);
        menuParentContainerFlw.addStyleName("dm-menu-parent-container");
        menuContainerPopupPanel.add(menuParentContainerFlw);
        menuContainerPopupPanel.addStyleName("dm-menu-popup-style");
    }
}
