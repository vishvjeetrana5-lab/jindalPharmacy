package dataman.dmToolkit.dmMenuItem.client;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.shared.DmShared;

public class DmMenuItem_Dsg {

    final FlowPanel menuItemFlw = new FlowPanel();
    final Anchor menuItemName = new Anchor();

    public DmMenuItem_Dsg(UserMenu userMenu) {
        FlowPanel flw = new FlowPanel();
        menuItemFlw.addStyleName("dm-Menu-child-list");
        menuItemName.setText(DmShared.xNull(userMenu.getMenuName()));
        flw.add(menuItemName);
        flw.add(new DmLabel(DmShared.xNull(userMenu.getToolTip()).trim()));
        menuItemFlw.add(flw);
    }
}
