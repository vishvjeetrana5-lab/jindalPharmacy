package aarogya.master.organizationServiceMapping.client;

import aarogya.master.organizationServiceMapping.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFileUploadButton.client.DmFileUploadButton;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class OrganizationServiceMapping_Dsg extends IsWidgetExtended implements IFileUploadNotifier {

    final String widgetName = new pmMaster().IMPORT_ORGANIZATION_RATELIST.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmTextBox txtApplicableDate = new DmTextBox(11, true);
    final DmTextBox txtApplicableTo = new DmTextBox(11, false);
    final DmLabel lblImportData = new DmLabel("Import From");
    final DmFileRuleType fileRuleType = new DmFileRuleType();
    final DmFileUploadButton btnUpload = new DmFileUploadButton(this, fileRuleType);
    final DmTextBox txtImportFrom = new DmTextBox(50, false);
    final FlowPanel flwBtnUpload = new FlowPanel();

    final Menu menu = new Menu();
    MenuItem mnuSearch, mnuDwnldCsv;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHEmpanelment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmpanelment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHOrganization() {
        txtHEmpanelment.getDmGrid().addTextColumn(txtHEmpanelment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHEmpanelment.getDmGrid().initializeGrid(txtHEmpanelment.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHEmpanelment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("OrganizationText")
        ValueProvider<Find_Mdl, String> organizationText();

        @Editor.Path("applicableFrom")
        ValueProvider<Find_Mdl, String> applicableFrom();

        @Editor.Path("applicableTo")
        ValueProvider<Find_Mdl, String> applicableTo();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().organizationText(), "Empanelment Name", 0, "organizationName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().applicableFrom(), "Applicable From", 100, "applicableFrom");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().applicableTo(), "Applicable To", 100, "applicableTo");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("organizationName", DmSortType.ASC);
        dmFind.getHelper().addSort("applicableFromDate", DmSortType.ASC);
    }
    //</editor-fold>

    public OrganizationServiceMapping_Dsg() {
        ini();
        iniFind();
        iniTxtHOrganization();
        iniMenu();
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

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuSearch = new MenuItem("Search Org. Service / Mapped Service", selectionHandler);
        mnuDwnldCsv = new MenuItem("Download csv", selectionHandler);
        menu.add(mnuSearch);
        menu.add(mnuDwnldCsv);

        return menu;
    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "435px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "85px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", "85px", "left", null, DmPosition.NONE, false);
        flwTd.add(topArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", "85px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdOrganizationDetail()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(tabArea()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        Anchor aBtn;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "85px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Empanelment");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmpanelment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Applicable To");
        lbl.getElement().getStyle().setMarginLeft(70, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApplicableTo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblImportData.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lblImportData);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
        hrtPnl.add(vl.add(txtImportFrom));
        hrtPnl.setCellWidth(txtImportFrom, "100%");
        DmClient.setStyle(flwBtnUpload, "100%", "20px", "left", null, DmPosition.NONE, true);

        fileRuleType.setFilters("csv");
        fileRuleType.setFileSizeLimit("10240");

        aBtn = new Anchor();
        aBtn.addStyleName("fa  fa-paperclip");
        aBtn.getElement().getStyle().setColor("black");
        btnUpload.getButton().getElement().appendChild(aBtn.getElement());
        btnUpload.getButton().setSize("25px", "20px");
        flwBtnUpload.add(vl.add(btnUpload));
        hrtPnl.add(flwBtnUpload);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "340px", null, null, DmPosition.CENTER, true);
        tabPanel.add(vl.add(iniGrdService()), "Service");
        tabPanel.add(vl.add(iniGrdRoomCategory()), "General");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Room Category">
    interface GrdRoomCategory_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRoomCategory_Mdl> {

        @Editor.Path("RatePercent")
        ValueProvider<GrdRoomCategory_Mdl, String> ratePercent();

    }
    final DmGrid<GrdRoomCategory_Mdl, GrdRoomCategory_PrpAcs> grdRoomCategory = new DmGrid(GWT.create(GrdRoomCategory_PrpAcs.class));

    private Grid iniGrdRoomCategory() {
        grdRoomCategory.addRowNumberColumn();
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().name(), "Room Category", 800, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().ratePercent(), "Rate %", 115, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdRoomCategory.initializeGrid(grdRoomCategory.getPropertyAccess().fieldHashCode(), 970, 325);

        return grdRoomCategory.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    interface GridService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdService_Mdl> {

        @Editor.Path("OrgGroupName")
        ValueProvider<GrdService_Mdl, String> orgGroupName();

        @Editor.Path("OrgServiceName")
        ValueProvider<GrdService_Mdl, String> orgServiceName();

        @Editor.Path("OrgServiceCode")
        ValueProvider<GrdService_Mdl, String> orgServiceCode();

        @Editor.Path("Rate")
        ValueProvider<GrdService_Mdl, String> rate();

        @Editor.Path("MappedServiceName")
        ValueProvider<GrdService_Mdl, String> mappedServiceName();

    }
    final DmGrid<GrdService_Mdl, GridService_PrpAcs> grdService = new DmGrid(GWT.create(GridService_PrpAcs.class));

    private Grid iniGrdService() {
        grdService.addRowNumberColumn();
        grdService.addTextColumn(grdService.getPropertyAccess().orgServiceName(), "Org. Sevice Name", 1, "");
        grdService.addTextColumn(grdService.getPropertyAccess().orgGroupName(), "Org. Group Name", 300, "");
        grdService.addTextColumn(grdService.getPropertyAccess().orgServiceCode(), "Org. Service Code", 120, "");
        grdService.addTextColumn(grdService.getPropertyAccess().rate(), "Rate", 140, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdService.addTextColumn(grdService.getPropertyAccess().mappedServiceName(), "Service Mapping With", 335, "");
        grdService.initializeGrid(grdService.getPropertyAccess().fieldHashCode(), 1280, 325, 2);

        return grdService.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Organization Detail">
    interface GridOrganizationDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdOrganizationDetail_Mdl> {

        @Editor.Path("ApplicableFrom")
        ValueProvider<GrdOrganizationDetail_Mdl, String> applicableFrom();

        @Editor.Path("ApplicableTo")
        ValueProvider<GrdOrganizationDetail_Mdl, String> applicableTo();

    }
    final DmGrid<GrdOrganizationDetail_Mdl, GridOrganizationDetail_PrpAcs> grdOrganizationDetail = new DmGrid(GWT.create(GridOrganizationDetail_PrpAcs.class));

    private Grid iniGrdOrganizationDetail() {
        grdOrganizationDetail.addRowNumberColumn();
        grdOrganizationDetail.addTextColumn(grdOrganizationDetail.getPropertyAccess().applicableFrom(), "Appl. From Date", 100, "");
        grdOrganizationDetail.addTextColumn(grdOrganizationDetail.getPropertyAccess().applicableTo(), "To Date", 120, "");
        grdOrganizationDetail.initializeGrid(grdOrganizationDetail.getPropertyAccess().fieldHashCode(), 290, 85, 2);

        return grdOrganizationDetail.getGrid();
    }
    //</editor-fold>

}
