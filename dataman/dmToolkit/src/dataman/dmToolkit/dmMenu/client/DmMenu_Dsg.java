package dataman.dmToolkit.dmMenu.client;

import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmMenu_Dsg {

    final FlowPanel menuFlw = new FlowPanel();
    final FlowPanel menuItemContainerFlw = new FlowPanel();

    public DmMenu_Dsg(UserMenu userMenu) {
        FlowPanel flw = new FlowPanel();
        menuFlw.addStyleName("dm-Menu-item-container");
        flw.addStyleName("dm-Menu-item-module");
        flw.add(new DmLabel(DmShared.xNull(userMenu.getMenuName())));
        menuFlw.add(flw);
        menuFlw.add(menuItemContainerFlw);
    }
}
