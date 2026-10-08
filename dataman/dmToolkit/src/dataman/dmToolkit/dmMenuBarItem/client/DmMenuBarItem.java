package dataman.dmToolkit.dmMenuBarItem.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenu.client.DmMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.ArrayList;
import java.util.List;

public class DmMenuBarItem extends DmMenuBarItem_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    private DmMenuBar menuBar;
    private List<DmMenu> menuList = new ArrayList<DmMenu>();
    private UserMenu userMenu;

    public DmMenuBarItem(UserMenu userMenu, DmMenuBar menuBar) {
        super(userMenu);
        this.userMenu = userMenu;
        this.menuBar = menuBar;
        this.menuBar.addMenuBarItem(this);
        handleEvents();
    }

    public DmMenuBar getMenuBar() {
        return menuBar;
    }

    @Override
    public Widget asWidget() {
        return menuBarItemFlw;
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
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            menuContainerPopupPanel.hide();
        }
    }

    public void addMenu(DmMenu menu) {
        if (menu != null) {
            menuList.add(menu);
            menuContainerFlw.add(menu);
        }
    }

    public void hideMenuContainer() {
        menuContainerPopupPanel.hide();
    }

    private void handleEvents() {
        menuContainerPopupPanel.addAttachHandler(this);
        menuBarItemName.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                menuContainerPopupPanel.showRelativeTo(menuBarItemName);
            }
        });
    }

}
