package dataman.dmToolkit.dmMenuBar.client;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmMenuBarItem.client.DmMenuBarItem;
import dataman.dmToolkit.dmStatic.client.DmClient;
import java.util.ArrayList;
import java.util.List;

public class DmMenuBar extends DmMenuBar_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents {

    private List<DmMenuBarItem> menuBarItemLst = new ArrayList<>();

    public DmMenuBar() {
        super();
    }

    @Override
    public Widget asWidget() {
        return menuBarFlw;
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

    public void addMenuBarItem(DmMenuBarItem menuBarItem) {
        if (menuBarItem != null) {
            menuBarItemLst.add(menuBarItem);
            menuBarFlw.add(menuBarItem);
        }
    }

}
