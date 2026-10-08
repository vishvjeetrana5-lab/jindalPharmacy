package aarogya.localSetting.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class LocalSetting_Dsg extends IsWidgetExtended {

    public final static String widgetName = "Local Setting";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();

    final Button btnPrinter = new Button("Printer Mapping");
    final Button btnMachineInterface = new Button("Machine Interface");
    final Button btnPosSetting = new Button("POS Settings");

    final Button btnDwnSetup = new Button();
    final Button btnClose = new Button();

    final DmToolContainer toolContainer = new DmToolContainer();

    final Menu menu = new Menu();
    MenuItem dwnPackSetup, dwnJDK64, dwnJDK32;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        dwnPackSetup = new MenuItem("PAC Setup", selectionHandler);
        menu.add(dwnPackSetup);

        dwnJDK64 = new MenuItem("JDK x64 Setup", selectionHandler);
        menu.add(dwnJDK64);

        dwnJDK32 = new MenuItem("JDK x32 Setup", selectionHandler);
        menu.add(dwnJDK32);

        return menu;
    }

    public LocalSetting_Dsg() {

        ini();
        createMenu();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("270px");
        vrtInnerForm.add(toolContainer);
        createToolControl();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "60%", "151px", null, null, DmPosition.CENTER, true);

        //==============================<Header Area>==========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        btnPrinter.setHeight("150px");
        btnPrinter.setWidth("150px");
        btnPrinter.getElement().setAttribute("style", btnPrinter.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPrinter.addStyleName("dm-button-grad");
        flwTd.add(btnPrinter);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        btnMachineInterface.setHeight("150px");
        btnMachineInterface.setWidth("150px");
        btnMachineInterface.getElement().setAttribute("style", btnMachineInterface.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnMachineInterface.addStyleName("dm-button-grad");
        flwTd.add(btnMachineInterface);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
        btnPosSetting.setHeight("150px");
        btnPosSetting.setWidth("150px");
        btnPosSetting.getElement().setAttribute("style", btnPosSetting.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPosSetting.addStyleName("dm-button-grad");
        flwTd.add(btnPosSetting);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Download");
        // label.setWidth("60px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-cloud-download");
        //aBtn.getElement().getStyle().setColor("gray");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDwnSetup.addStyleName("dm-top-control-btnStyle");
        btnDwnSetup.setTitle("Download");
        btnDwnSetup.getElement().appendChild(aBtn.getElement());
        btnDwnSetup.getElement().appendChild(label.getElement());
        btnDwnSetup.setWidth("90px");

        label = new InlineLabel("Close");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

    }

}
