package aarogya.master.consultantSharingMaster.client;

import aarogya.master.consultantSharingMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.HeaderGroupConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ConsultantSharingMaster_Dsg extends IsWidgetExtended {

    public final static String widgetName = new pmMaster().SHARING_MASTER.caption;
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtApplicableFrom = new DmTextBox(11, false);

    final FlowPanel flwApplicable = new FlowPanel();
    final Button btnPriceList = new Button("Price List");
    final Menu menu = new Menu();
    MenuItem miCopySharing;
    MenuItem searching;

    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Consultant", 100, "name");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHConsultant.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ApplicableFrom")
        ValueProvider<Find_Mdl, String> applicableFrom();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Consultant Name", 1, "consultantName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().applicableFrom(), "Applicable From", 100, "applicableFrom", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 1);
        dmFind.getHelper().addSort("consultantName", DmSortType.ASC);
    }
    //</editor-fold>

    public ConsultantSharingMaster_Dsg() {
        initialize();
        iniFind();
        iniTxtHConsultant();
        createMenu();
    }

    private void initialize() {

        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {
        btnPriceList.addStyleName("dm-button-grad");
        btnPriceList.setWidth("100px");
        flwApplicable.add(btnPriceList);
        userControl.addGeneralBlog(flwApplicable);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "522px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "495px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdSharingDetail()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsultant));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableFrom));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        miCopySharing = new MenuItem("Copy Sharing Detail", selectionHandler);
        menu.add(miCopySharing);
        searching = new MenuItem("Searching", selectionHandler);
        menu.add(searching);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid Sharing Detail">
    interface GridSharingDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSharingDetail_Mdl> {

        @Editor.Path("ModuleName")
        ValueProvider<GrdSharingDetail_Mdl, String> moduleName();

        @Editor.Path("CategoryName")
        ValueProvider<GrdSharingDetail_Mdl, String> categoryName();

        @Editor.Path("GroupName")
        ValueProvider<GrdSharingDetail_Mdl, String> groupName();

        @Editor.Path("RoomCategoryName")
        ValueProvider<GrdSharingDetail_Mdl, String> roomCategory();

        @Editor.Path("ServiceName")
        ValueProvider<GrdSharingDetail_Mdl, String> serviceName();

        @Editor.Path("HeaderConSharingDisplay")
        ValueProvider<GrdSharingDetail_Mdl, String> headerConSharingDisplay();

        @Editor.Path("HeaderRefSharingDisplay")
        ValueProvider<GrdSharingDetail_Mdl, String> headerRefSharingDisplay();

        @Editor.Path("LineConSharingDisplay")
        ValueProvider<GrdSharingDetail_Mdl, String> lineConSharingDisplay();

        @Editor.Path("LineRefSharingDisplay")
        ValueProvider<GrdSharingDetail_Mdl, String> lineRefSharingDisplay();

    }
    final DmGrid<GrdSharingDetail_Mdl, GridSharingDetail_PrpAcs> gridSharingDetail = new DmGrid(GWT.create(GridSharingDetail_PrpAcs.class));

    public Grid iniGrdSharingDetail() {
        DmColumnConfig<GrdSharingDetail_Mdl, String> roomCategory, empanelment, moduleName, serviceCategory, serviceGroup, service;
        Cell<String> cell = new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        };

        gridSharingDetail.addRowNumberColumn();
        roomCategory = gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().roomCategory(), "Room Category", 170, "");
        empanelment = gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().name(), "Empanelment", 170, "");
        moduleName = gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().moduleName(), "Module Name", 100, "");
        serviceCategory = gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().categoryName(), "Service Category", 147, "");
        serviceGroup = gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().groupName(), "Service Group", 147, "");
        service = gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().serviceName(), "Service", 180, "");
        gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().headerConSharingDisplay(), "Consultant", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().headerRefSharingDisplay(), "Referred", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().lineConSharingDisplay(), "Consultant", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridSharingDetail.addTextColumn(gridSharingDetail.getPropertyAccess().lineRefSharingDisplay(), "Referred", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        gridSharingDetail.addBeforeInitializeHandler(new DmGrid.BeforeInitialize() {
            @Override
            public void beforeInitialize(ColumnModel columnModel) {
                columnModel.addHeaderGroup(0, 7, new HeaderGroupConfig("Header (Registration)", 1, 2));
                columnModel.addHeaderGroup(0, 9, new HeaderGroupConfig("Performing (Service)", 1, 2));
            }
        });

        gridSharingDetail.initializeGrid(gridSharingDetail.getPropertyAccess().fieldHashCode(), 1280, 495);

        roomCategory.setCell(cell);
        empanelment.setCell(cell);
        moduleName.setCell(cell);
        serviceCategory.setCell(cell);
        serviceGroup.setCell(cell);
        service.setCell(cell);

        gridSharingDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdSharingDetail_Mdl>() {

            @Override
            public String getColStyle(GrdSharingDetail_Mdl model, ValueProvider<? super GrdSharingDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (DmShared.vNull(model.getSearchStatus()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyConfirm";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdSharingDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return gridSharingDetail.getGrid();
    }
    //</editor-fold>

}
