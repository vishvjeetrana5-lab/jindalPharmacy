package dataman.utility.guiConfig.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.guiConfig.shared.*;

abstract class GUIConfig_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().GUI_CONFIGURATION.caption;
    public final static String modifiedByBlog = "Modified By";
    public final static String preparedByBlog = "Prepared By";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtName = new DmTextBox(50, true);
    final DmTextBox txtQuery = new DmTextBox(1000, true);
    final DmTextBox txtReportPrefix = new DmTextBox(10, false);

    final FlowPanel flwQuery = new FlowPanel();
    final Button btnQuery = new Button("Q");
    final Menu menu = new Menu();
    MenuItem manageFileMenu, mnuExportData, mnuImportData;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="TxtHTable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTable() {
        txtHTable.getDmGrid().addTextColumn(txtHTable.getDmGrid().getPropertyAccess().name(), "Table", 150, "name");
        txtHTable.getDmGrid().initializeGrid(txtHTable.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHTable.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTemplate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTemplate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTamplate() {
        txtHTemplate.getDmGrid().addTextColumn(txtHTemplate.getDmGrid().getPropertyAccess().name(), "Name", 150, "Name");
        txtHTemplate.getDmGrid().initializeGrid(txtHTemplate.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHTemplate.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="DmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("TemplateName")
        ValueProvider<Find_Mdl, String> template();

        @Editor.Path("TableName")
        ValueProvider<Find_Mdl, String> tableName();

        @Editor.Path("ApplicableSite")
        ValueProvider<Find_Mdl, String> site();

        @Editor.Path("ReportPrefix")
        ValueProvider<Find_Mdl, String> reportName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "GUI Name", 300, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().template(), "Template Name", 120, "guiTemplateName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tableName(), "Table Name", 150, "tableName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().site(), "Applicable Site", 150, "siteName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().reportName(), "Report Name Prefix", 150, "reportNamePrefix");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
        dmFind.getHelper().addSort("guiTemplateName", DmSortType.ASC);
        dmFind.getHelper().addSort("tableName", DmSortType.ASC);
    }
    //</editor-fold>

    public GUIConfig_Dsg() {
        ini();
        iniFind();
        iniTable();
        iniTamplate();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());

        iniMenu();
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "479px", "left", null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "65px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "413px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", "413px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdDesc()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", "413px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdApplicableSite()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI Name");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Template Name");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTemplate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Table Name");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTable));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Report Prefix");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReportPrefix));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SQL Query");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuery));
        flwTr.add(flwTd);

        DmClient.setStyle(flwQuery, "2.5%", "20px", "left", null, DmPosition.NONE, true);
        btnQuery.setWidth("100%");
        btnQuery.setHeight("20px");
        btnQuery.getElement().setAttribute("style", btnQuery.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px; margin-left: 2px;");
        btnQuery.addStyleName("dm-button-grad");
        flwQuery.add(btnQuery);
        flwTr.add(flwQuery);

        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(preparedByBlog, 100, true, false);
        return userControl;

    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        manageFileMenu = new MenuItem("Manage File", selectionHandler);
        menu.add(manageFileMenu);

        mnuExportData = new MenuItem("Export Data", selectionHandler);
        menu.add(mnuExportData);

        mnuImportData = new MenuItem("Import Data", selectionHandler);
        menu.add(mnuImportData);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Applicable Site">
    interface GrdApplicableSite_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {
    }
    final DmGrid<KeyFieldExtended, GrdApplicableSite_PrpAcs> grdApplicableSite = new DmGrid(GWT.create(GrdApplicableSite_PrpAcs.class));

    private Grid iniGrdApplicableSite() {
        grdApplicableSite.addRowNumberColumn();
        grdApplicableSite.addTextColumn(grdApplicableSite.getPropertyAccess().name(), "Applicable To Site", 285, "");
        grdApplicableSite.initializeGrid(grdApplicableSite.getPropertyAccess().fieldHashCode(), 380, 413, 2);
        return grdApplicableSite.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Description">
    interface GrdDescription_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GUIRow_Mdl> {

        @Editor.Path("SrNo")
        ValueProvider<GUIRow_Mdl, String> srNo();

        @Editor.Path("Description")
        ValueProvider<GUIRow_Mdl, String> desc();
    }
    final DmGrid<GUIRow_Mdl, GrdDescription_PrpAcs> grdDescription = new DmGrid(GWT.create(GrdDescription_PrpAcs.class));

    private Grid iniGrdDesc() {
        grdDescription.addTextColumn(grdDescription.getPropertyAccess().srNo(), "Row No.", 70, "");
        grdDescription.addTextColumn(grdDescription.getPropertyAccess().desc(), "Description", 800, "");
        grdDescription.initializeGrid(grdDescription.getPropertyAccess().fieldHashCode(), 895, 413);

        return grdDescription.getGrid();
    }
    //</editor-fold>
}
