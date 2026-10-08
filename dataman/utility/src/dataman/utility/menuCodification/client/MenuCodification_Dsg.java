package dataman.utility.menuCodification.client;

import dataman.utility.menuCodification.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.libraryMenu.self.shared.lmUtility;

abstract class MenuCodification_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().MENU_CODIFICATION_MASTER.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnRefresh = new Button();
    final Button btnClose = new Button();
    final Button btnExport = new Button();

    final RadioButton radioMenu = new RadioButton("a", "Menu");
    final RadioButton radioMenuCode = new RadioButton("a", "Menu-Code");
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdAllModule">
    interface GrdAllModule_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdModuleMenu_Mdl> {

        @Editor.Path("MenuShortHand")
        ValueProvider<GrdModuleMenu_Mdl, String> menuCode();
    }
    final DmGrid<GrdModuleMenu_Mdl, GrdAllModule_PrpAcs> grdAllModule = new DmGrid(GWT.create(GrdAllModule_PrpAcs.class));

    private Grid iniGrdAllModule() {
        grdAllModule.addRowNumberColumn();
        grdAllModule.addTextColumn(grdAllModule.getPropertyAccess().name(), "Module", 1, "module");
        grdAllModule.initializeGrid(grdAllModule.getPropertyAccess().fieldHashCode(), 192, 406, 2);

        return grdAllModule.getGrid();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdAllMenu">
    interface GrdAllMenu_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDescription_Mdl> {

        @Editor.Path("MenuCode")
        ValueProvider<GrdDescription_Mdl, String> menuCode();
    }

    final DmGrid<GrdDescription_Mdl, GrdAllMenu_PrpAcs> grdAllMenu = new DmGrid(GWT.create(GrdAllMenu_PrpAcs.class));

    private Grid iniGrdAllMenu() {
        grdAllMenu.addRowNumberColumn();
        grdAllMenu.addTextColumn(grdAllMenu.getPropertyAccess().name(), "...", 180, "");
        grdAllMenu.addTextColumn(grdAllMenu.getPropertyAccess().menuCode(), "Menu Code", 80, "");
        grdAllMenu.initializeGrid(grdAllMenu.getPropertyAccess().fieldHashCode(), 502, 406, 2);

        return grdAllMenu.getGrid();
    }
//</editor-fold>

    public MenuCodification_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
//        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        flwForm.setHeight("450px");
        vrtInnerForm.add(vl.add(iniToolControl()));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());

        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmToolContainer iniToolControl() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Refresh");
        // label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        //aBtn.getElement().getStyle().setColor("orange");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.setTitle("Refresh");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("70px");

        label = new InlineLabel("Close");
        //  label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        //aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        label = new InlineLabel("Export");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-upload");
        // aBtn.getElement().getStyle().setColor("blue");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExport.addStyleName("dm-top-control-btnStyle");
        btnExport.setTitle("Export");
        btnExport.getElement().appendChild(aBtn.getElement());
        btnExport.getElement().appendChild(label.getElement());
        btnExport.setWidth("70px");

        toolContainer.addWidget(vl.add(btnRefresh));
        toolContainer.addWidget(btnExport);
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr;

        //upper Area
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "430px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTr.add(footerSection());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "410px", "left", null, DmPosition.NONE, false);
        flwTr.add(bottomArea());

        flwTable.add(flwTr);

        return flwTable;

    }

    private HorizontalPanel footerSection() {
        FlowPanel flwTmp;
        InlineLabel lbl;
        HorizontalPanel hrtPanel = new HorizontalPanel();

        //upper Area
        flwTmp = new FlowPanel();
        flwTmp.setWidth("50px");
        flwTmp.getElement().getStyle().setTop(6, Style.Unit.PX);
        flwTmp.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        lbl = new InlineLabel("Sort");
        flwTmp.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        lbl.getElement().getStyle().setTop(4, Style.Unit.PX);
        hrtPanel.add(flwTmp);
        hrtPanel.setCellWidth(lbl, "100%");

        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().getStyle().setBackgroundColor("rgb(210, 245, 210)");
        hrtPanel.getElement().getStyle().setMarginLeft(34, Style.Unit.PCT);
        hrtPanel.getElement().getStyle().setWidth(51.3, Style.Unit.PCT);
        radioMenu.getElement().setAttribute("style", radioMenu.getElement().getAttribute("style")
                + "; float: left; width: 65px;top:3px;position:relative;"
        );
        radioMenuCode.getElement().setAttribute("style", radioMenuCode.getElement().getAttribute("style")
                + "; float: left; width: 90px;top:3px;position:relative;"
        );
        hrtPanel.add(radioMenu);
        hrtPanel.add(radioMenuCode);

        return hrtPanel;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "410px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(iniGrdAllModule());
        flwTd.getElement().getStyle().setMarginLeft(14, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdAllMenu()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
