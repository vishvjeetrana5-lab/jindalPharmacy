package dataman.dmToolkit.dmGUI.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmStatic.client.DmClient;

abstract class DmGUI_Dsg {

    final VerticalPanel vrtForm = new VerticalPanel();
    final HorizontalPanel hrtBanner = new HorizontalPanel();
    final FlowPanel workingAreaFlow = new FlowPanel();
    final VerticalPanel workingAreaVrt = new VerticalPanel();
    final Button btnMenu = new Button();
    final Menu menu = new Menu();
    MenuItem repairOverHead;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    public DmGUI_Dsg(int height) {
        DmClient.setStyle(vrtForm, "100%", "100%", "left", null, DmPosition.NONE, true);
        vrtForm.add(createFooter());
        DmClient.setStyle(workingAreaFlow, "100%", height + "px", "left", null, DmPosition.NONE, true);
        workingAreaFlow.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        vrtForm.add(workingAreaFlow);
        vrtForm.setCellHeight(workingAreaFlow, "100%");
        DmClient.setStyle(workingAreaVrt, "99%", "100%", "left", null, DmPosition.NONE, true);
        workingAreaFlow.add(workingAreaVrt);
    }

    private HorizontalPanel createFooter() {
        Anchor aBtn;

        hrtBanner.addStyleName("dm-StripLabel");
        hrtBanner.setHeight("18px");
        hrtBanner.getElement().getStyle().setMarginBottom(1, Style.Unit.PX);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-bars");
        aBtn.getElement().getStyle().setColor("brown");
        btnMenu.getElement().setAttribute("style", "width: 20px;height:18px;Border:none;float:left;padding-top: 0px;");
        btnMenu.getElement().appendChild(aBtn.getElement());
        hrtBanner.add(btnMenu);
        createMenu();

        return hrtBanner;
    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        repairOverHead = new MenuItem("Repair overhead", selectionHandler);
        menu.add(repairOverHead);
        return menu;
    }

}
