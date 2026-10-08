package aarogya.master.tabIndexSettings.client;

import aarogya.master.tabIndexSettings.shared.GridTabIndexSettings_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;

abstract class TabIndexSettings_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().TAB_INDEX_SETTING.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnSave = new Button();
    final Button btnClose = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwReset = new FlowPanel();
    final Button btnReset = new Button("");
    final Button btnExport = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHModule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Module", 0, "module");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
        txtHModule.getHelper().addSort("module", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEntry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEntry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHEntry() {
        txtHEntry.getDmGrid().addTextColumn(txtHEntry.getDmGrid().getPropertyAccess().name(), "Entry", 0, "name");
        txtHEntry.getDmGrid().initializeGrid(txtHEntry.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHEntry.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GrdTabIndex">
    interface GrdTabIndex_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridTabIndexSettings_Mdl> {

        @Editor.Path("SerialNo")
        ValueProvider<GridTabIndexSettings_Mdl, String> serialNo();

    }
    final DmGrid<GridTabIndexSettings_Mdl, GrdTabIndex_PrpAcs> grdTabIndex = new DmGrid(GWT.create(GrdTabIndex_PrpAcs.class));

    private Grid iniGridtabIndex() {
        grdTabIndex.addRowNumberColumn();
        grdTabIndex.addTextColumn(grdTabIndex.getPropertyAccess().name(), "Caption", 150, "");
        grdTabIndex.addTextColumn(grdTabIndex.getPropertyAccess().serialNo(), "Serial No.", 120, "");
        grdTabIndex.initializeGrid(grdTabIndex.getPropertyAccess().fieldHashCode(), 510, 500, 2);
        return grdTabIndex.getGrid();
    }
    //</editor-fold>

    public TabIndexSettings_Dsg() {
        ini();
        iniTxtHModule();
        iniTxtHEntry();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("578px");
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

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
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

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(btnExport);
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;

        //upper Area
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "545px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "45px", "left", null, DmPosition.NONE, false);
        flwTr.add(upperArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "500px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "500px", null, null, DmPosition.CENTER, false);
        flwTd.add(vl.add(iniGridtabIndex()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel upperArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;
        Anchor aBtn;
        InlineLabel label;

        //Module
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "38%", "45px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Module");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "81%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHModule));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Entry
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "60%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEntry));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "19%", null, "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        label = new InlineLabel("Reset");
        label.setWidth("40px");
        aBtn = new Anchor();
        btnReset.getElement().appendChild(aBtn.getElement());
        btnReset.getElement().appendChild(label.getElement());
        btnReset.getElement().setAttribute("style", btnReset.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnReset.setWidth("70px");
        btnReset.setHeight("22px");
        btnReset.addStyleName("dm-button-grad");
        flwReset.add(btnReset);
        flwTD.add(flwReset);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;

    }
}
