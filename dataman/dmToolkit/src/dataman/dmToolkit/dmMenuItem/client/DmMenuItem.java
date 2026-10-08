package dataman.dmToolkit.dmMenuItem.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenu.client.DmMenu;
import dataman.dmToolkit.dmStatic.client.DmClient;

public class DmMenuItem extends DmMenuItem_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    public interface InitializeWidgetInvoker {

        public void initializeWidget(String strMenuText);
    }

    private DmMenu menu;
    final InitializeWidgetInvoker iwiMain;
    private final UserMenu userMenu;

    public DmMenuItem(UserMenu userMenu, DmMenu menu, InitializeWidgetInvoker iwiMain) {
        super(userMenu);
        this.userMenu = userMenu;
        this.menu = menu;
        this.iwiMain = iwiMain;
        this.menu.addMenuItem(this);

        menuItemName.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DmMenuItem.this.getMenu().getMenuBarItem().hideMenuContainer();
                DmMenuItem.this.iwiMain.initializeWidget(DmMenuItem.this.userMenu.getMenuName());
            }
        });
    }

    public DmMenu getMenu() {
        return menu;
    }

    @Override
    public Widget asWidget() {
        return menuItemFlw;
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

}
