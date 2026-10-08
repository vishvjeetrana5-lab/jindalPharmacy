package dataman.dmToolkit.dmMenu.client;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBarItem.client.DmMenuBarItem;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.ArrayList;
import java.util.List;

public class DmMenu extends DmMenu_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    private DmMenuBarItem menuBarItem;
    private List<DmMenuItem> menuItemLst = new ArrayList<DmMenuItem>();
    private UserMenu userMenu;

    public DmMenu(UserMenu userMenu, DmMenuBarItem menuBarItem) {
        super(userMenu);
        this.userMenu = userMenu;
        this.menuBarItem = menuBarItem;
        this.menuBarItem.addMenu(this);
    }

    public DmMenuBarItem getMenuBarItem() {
        return menuBarItem;
    }

    @Override
    public Widget asWidget() {
        return menuFlw;
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
    }

    public void addMenuItem(DmMenuItem menuItem) {
        if (menuItem != null) {
            menuItemLst.add(menuItem);
            menuItemContainerFlw.add(menuItem);
        }
    }

}
