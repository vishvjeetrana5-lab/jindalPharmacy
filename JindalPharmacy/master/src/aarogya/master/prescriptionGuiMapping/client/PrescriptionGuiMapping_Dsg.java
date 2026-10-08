package aarogya.master.prescriptionGuiMapping.client;

import aarogya.master.prescriptionGuiMapping.shared.Find_Mdl;
import aarogya.master.prescriptionGuiMapping.shared.GridDescription_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class PrescriptionGuiMapping_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().PRESCRIPTION_GUI_MAPPING.caption;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();
    final Button btnSave = new Button();
    final Button btnFind = new Button();
    final Button btnOther = new Button();
    final Button btnClose = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();

    final Menu menu = new Menu();
    MenuItem manageExport;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHEntry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEntry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, true);

    private void iniTxtHEntry() {
        txtHEntry.getDmGrid().addTextColumn(txtHEntry.getDmGrid().getPropertyAccess().name(), "Entry", 0, "");
        txtHEntry.getDmGrid().initializeGrid(txtHEntry.getDmGrid().getPropertyAccess().code(), 180, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GrdDescription">
    interface GridDescription_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridDescription_Mdl> {

    }
    final DmGrid<GridDescription_Mdl, GridDescription_PrpAcs> grdDescription = new DmGrid(GWT.create(GridDescription_PrpAcs.class));

    private Grid iniGrdDescription() {

        grdDescription.addRowNumberColumn();
        grdDescription.addTextColumn(grdDescription.getPropertyAccess().name(), "GUI Template", 150, "");
        grdDescription.initializeGrid(grdDescription.getPropertyAccess().fieldHashCode(), 500, 510, 2);

        return grdDescription.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("GuiTemplateName")
        ValueProvider<Find_Mdl, String> guiTemplateName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry", 200, "nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().guiTemplateName(), "GUI Template", 0, "guiname");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 0, 2);
        dmFind.getHelper().addSort("nature", DmSortType.ASC);
    }
    //</editor-fold>

    public PrescriptionGuiMapping_Dsg() {
        ini();
        iniFind();
        iniTxtHEntry();
    }

    private void ini() {
        iniMenu();
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
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
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Find");
        aBtn = new Anchor();
        aBtn.addStyleName("gwt-Anchor fa fa-search");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFind.setTitle("Find");
        btnFind.addStyleName("dm-top-control-btnStyle");
        btnFind.getElement().appendChild(aBtn.getElement());
        btnFind.getElement().appendChild(label.getElement());
        btnFind.setWidth("70px");

        label = new InlineLabel("Other");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-share-alt");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOther.setTitle("Other");
        btnOther.addStyleName("dm-top-control-btnStyle");
        btnOther.getElement().appendChild(aBtn.getElement());
        btnOther.getElement().appendChild(label.getElement());
        btnOther.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escape)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnFind));
        toolContainer.addWidget(vl.add(btnOther));
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTR;

        //upper Area
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "547px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "20px", "left", null, DmPosition.NONE, false);
        flwTR.add(upperArea());
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "512px", "left", null, DmPosition.NONE, false);
        flwTR.add(bottomArea());

        flwTable.add(flwTR);

        return flwTable;

    }

    private FlowPanel upperArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        //Name
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "25%", "20px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "80%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEntry));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;

    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTR, flwTD;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "512px", null, null, DmPosition.CENTER, true);
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(iniGrdDescription()));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };
        manageExport = new MenuItem("Export", selectionHandler);
        menu.add(manageExport);
        return menu;
    }

}
