package dataman.utility.voucherType.client;

import dataman.libraryMenu.self.shared.lmUtility;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasAutoHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.utility.voucherType.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

public abstract class VoucherType_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().MANAGE_VOUCHER_TYPE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.5, Style.Unit.EM);
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

    final DmTextBoxNumeric txtVoucherType = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 3, true);
    final DmSuggestBox txtShortName = new DmSuggestBox(5, 10, true, "Short_Name", DmDataType.STRING);
    final DmSuggestBox txtDescription = new DmSuggestBox(50, 10, true, "Description", DmDataType.STRING);
    final DmTextBoxNumeric txtSerialNo = new DmTextBoxNumeric(0, false, 3, true);
    final DmTextBox txtMenuName = new DmTextBox(100, false);
    final DmTextBox txtMenuUsedInModule = new DmTextBox(100, false);
    final DmTextBox txtAcceptEntryAfter = new DmTextBox(11, false);
    final DmTextBox txtPaperSize = new DmTextBox(30, false);
    final DmTextBox txtReportName = new DmTextBox(50, false);
    final DmLabel lblSysGroup = new DmLabel("");

    final Menu menu = new Menu();
    MenuItem mnuExportData, mnuImportData;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="getTxtHCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void initxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getTxtHModule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModule.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHGUITemplate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGUITemplate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniGUITemplate() {
        txtHGUITemplate.getDmGrid().addTextColumn(txtHGUITemplate.getDmGrid().getPropertyAccess().name(), "Name", 50, "Name");
        txtHGUITemplate.getDmGrid().initializeGrid(txtHGUITemplate.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGUITemplate.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHParentVoucher">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHParentVoucher = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void initxtHParentVoucher() {
        txtHParentVoucher.getDmGrid().addTextColumn(txtHParentVoucher.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHParentVoucher.getDmGrid().initializeGrid(txtHParentVoucher.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefaultReport">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefaultReport = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void initxtHDefaultReport() {
        txtHDefaultReport.getDmGrid().addTextColumn(txtHDefaultReport.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDefaultReport.getDmGrid().initializeGrid(txtHDefaultReport.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHlandescape">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHlandescape = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void initxtHlandescape() {
        txtHlandescape.getDmGrid().addTextColumn(txtHlandescape.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHlandescape.getDmGrid().initializeGrid(txtHlandescape.getDmGrid().getPropertyAccess().code(), 100, 0, 1); //244
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActiveYN">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActiveYN = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void initxtHActiveYN() {
        txtHActiveYN.getDmGrid().addTextColumn(txtHActiveYN.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHActiveYN.getDmGrid().initializeGrid(txtHActiveYN.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getTxtHMenuText">
    interface DmtxtHmenuText_PrpAcs extends KeyField_T_PrpAcs<TxtHMenuText_Mdl> {

        @Editor.Path("menuText")
        ValueProvider<TxtHMenuText_Mdl, String> menuText();

        @Editor.Path("menuName")
        ValueProvider<TxtHMenuText_Mdl, String> menuName();

        @Editor.Path("menuModuleName")
        ValueProvider<TxtHMenuText_Mdl, String> menuModuleName();

    }
    final DmTextHelp<TxtHMenuText_Mdl, DmtxtHmenuText_PrpAcs> txtHMenuText = new DmTextHelp(GWT.create(DmtxtHmenuText_PrpAcs.class), 10);

    private void initxtHMenuText() {
        txtHMenuText.getDmGrid().addTextColumn(txtHMenuText.getDmGrid().getPropertyAccess().menuText(), "Menu Text", 200, "MnuText");
        txtHMenuText.getDmGrid().addTextColumn(txtHMenuText.getDmGrid().getPropertyAccess().menuName(), "Menu Name", 200, "MnuName");
        txtHMenuText.getDmGrid().addTextColumn(txtHMenuText.getDmGrid().getPropertyAccess().menuModuleName(), "Module Name", 200, "MnuModule");
        txtHMenuText.getDmGrid().initializeGrid(txtHMenuText.getDmGrid().getPropertyAccess().code(), 600, 0, 1); //244
        txtHMenuText.getHelper().addSort("MnuText", DmSortType.ASC);
        txtHMenuText.getHelper().addSort("MnuName", DmSortType.ASC);
        txtHMenuText.getHelper().addSort("MnuModule", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDefaultAC">
    interface DmtxtHDefaultAc_PrpAcs extends KeyField_T_PrpAcs<TxtHDefaultAC_Mdl> {

        @Editor.Path("menuModule")
        ValueProvider<TxtHDefaultAC_Mdl, String> menuModule();

    }
    final DmTextHelp<TxtHDefaultAC_Mdl, DmtxtHDefaultAc_PrpAcs> txtHDefaultAC = new DmTextHelp(GWT.create(DmtxtHDefaultAc_PrpAcs.class), 10);

    private void initxtHDefaultAC() {
        txtHDefaultAC.getDmGrid().addTextColumn(txtHDefaultAC.getDmGrid().getPropertyAccess().name(), "Name", 200, "Name");
        txtHDefaultAC.getDmGrid().addTextColumn(txtHDefaultAC.getDmGrid().getPropertyAccess().menuModule(), "Manual Code", 100, "ManualCode");
        txtHDefaultAC.getDmGrid().initializeGrid(txtHDefaultAC.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDefaultAC.getHelper().addSort("Name", DmSortType.ASC);
        txtHDefaultAC.getHelper().addSort("ManualCode", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBalancingAC">
    interface DmtxtHBalancingAc_PrpAcs extends KeyField_T_PrpAcs<TxtHDefaultAC_Mdl> {

        @Editor.Path("menuModule")
        ValueProvider<TxtHDefaultAC_Mdl, String> menuModule();

    }
    final DmTextHelp<TxtHDefaultAC_Mdl, DmtxtHBalancingAc_PrpAcs> txtHBalancingAC = new DmTextHelp(GWT.create(DmtxtHBalancingAc_PrpAcs.class), 10);

    private void initxtHBalancingAC() {
        txtHBalancingAC.getDmGrid().addTextColumn(txtHBalancingAC.getDmGrid().getPropertyAccess().name(), "Name", 200, "Name");
        txtHBalancingAC.getDmGrid().addTextColumn(txtHBalancingAC.getDmGrid().getPropertyAccess().menuModule(), "Manual Code", 100, "ManualCode");
        txtHBalancingAC.getDmGrid().initializeGrid(txtHBalancingAC.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHBalancingAC.getHelper().addSort("Name", DmSortType.ASC);
        txtHBalancingAC.getHelper().addSort("ManualCode", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHBankCashBook">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBankCashBook = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void initxtHtxtHBankCashBook() {
        txtHBankCashBook.getDmGrid().addTextColumn(txtHBankCashBook.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHBankCashBook.getDmGrid().initializeGrid(txtHBankCashBook.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
//</editor-fold>

    //
    //<editor-fold defaultstate="collapsed" desc="txtHTemplateName">
    interface Grid_TemplateName extends KeyField_T_PrpAcs<TxtHTemplate_Mdl> {
    }

    final DmTextHelp<TxtHTemplate_Mdl, Grid_TemplateName> txtHTemplateName = new DmTextHelp(GWT.create(Grid_TemplateName.class), 7, false);

    public Grid iniTemplateName() {
        txtHTemplateName.getDmGrid().addTextColumn(txtHTemplateName.getDmGrid().getPropertyAccess().name(), "Template Name", 1, "name");
        txtHTemplateName.getDmGrid().initializeGrid(txtHTemplateName.getDmGrid().getPropertyAccess().code(), 250, 200, 1);
        txtHTemplateName.getHelper().addSort("name", DmSortType.ASC);
        return txtHTemplateName.getDmGrid().getGrid();

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtDocumentMailUser">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtDocumentMailUser = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void initxtDocumentMailUser() {
        txtDocumentMailUser.getDmGrid().addTextColumn(txtDocumentMailUser.getDmGrid().getPropertyAccess().name(), "Name", 50, "Name");
        txtDocumentMailUser.getDmGrid().initializeGrid(txtDocumentMailUser.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtDocumentMailUser.getHelper().addSort("Name", DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("type")
        ValueProvider<Find_Mdl, String> type();

        @Editor.Path("Description")
        ValueProvider<Find_Mdl, String> Description();

        @Editor.Path("Module")
        ValueProvider<Find_Mdl, String> Module();

        @Editor.Path("Category")
        ValueProvider<Find_Mdl, String> Category();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Description(), "Description", 1, "Description");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Module(), "Module", 100, "ModuleName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Category(), "Category", 100, "Category");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().type(), "vType", 50, "VoucherType");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 200, 1);//183
        dmFind.getHelper().addSort("Description", DmSortType.ASC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("ModuleName", DmSortType.ASC);
        dmFind.getHelper().addSort("Category", DmSortType.ASC);
        dmFind.getHelper().addSort("VoucherType", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDocumentType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDocumentType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, false);

    private void iniDocumentType() {
        txtHDocumentType.getDmGrid().addTextColumn(txtHDocumentType.getDmGrid().getPropertyAccess().name(), "Name", 50, "name");
        txtHDocumentType.getDmGrid().initializeGrid(txtHDocumentType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDocumentType.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHDocumentNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDocumentNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, false);

    private void iniDocumentNature() {
        txtHDocumentNature.getDmGrid().addTextColumn(txtHDocumentNature.getDmGrid().getPropertyAccess().name(), "Name", 50, "name");
        txtHDocumentNature.getDmGrid().initializeGrid(txtHDocumentNature.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDocumentNature.getHelper().addSort("Name", DmSortType.ASC);

    }
    //</editor-fold>

    public VoucherType_Dsg() {
        ini();
        iniFind();
        initxtHCategory();
        iniTxtHModule();
        initxtHParentVoucher();
        initxtHDefaultReport();
        initxtHMenuText();
        initxtHlandescape();
        initxtHDefaultAC();
        initxtHBalancingAC();
        initxtHActiveYN();
        iniDocumentType();
        iniDocumentNature();
        iniGUITemplate();
        initxtHtxtHBankCashBook();
        iniMenu();
        iniTemplateName();
        initxtDocumentMailUser();
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
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {

        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "410px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr, flwTd;

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "15px", "left", null, DmPosition.NONE, true);
        flwTr.add(lblSysGroup);
        lblSysGroup.getElement().getStyle().setPaddingLeft(450, Style.Unit.PX);
        lblSysGroup.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "397px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51%", "372px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(leftArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", "372px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(rightArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "294px", null, null, DmPosition.CENTER, true);

        ///////////////////////// TR1 //////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtVoucherType);
        txtVoucherType.setEnabled(false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR2 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Description");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDescription));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR3 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Module Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHModule));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Category");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR4 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Serial No. / Priority");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSerialNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Parent Voucher");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHParentVoucher));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR7 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Menu Text");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMenuText));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR8 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Menu Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMenuName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR19 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Menu Used In Module");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMenuUsedInModule));
        flwTr.add(flwTd);

        /////////////////////////// TR10 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Accept (Adding) Entry After");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAcceptEntryAfter));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Report");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefaultReport));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR11 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Paper Size (Name)");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPaperSize));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Landscape");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHlandescape));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR12 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefaultAC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR12 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Balancing A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBalancingAC));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR13 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Document Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDocumentType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR14 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Document Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDocumentNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR13 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI Template");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGUITemplate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHActiveYN));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bank Cash Book");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBankCashBook));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //=====================================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Document Mail User");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDocumentMailUser));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mail Template");
        lbl.getElement().getStyle().setMarginLeft(23, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTemplateName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================< New Row >=====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Report Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReportName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "294px", null, null, DmPosition.CENTER, true);

        ///////////////////////// TR1 //////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "294px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(tabArea()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        tabPanel.setHeight("100%");
        tabPanel.setWidth("97%");
        tabPanel.add(vl.add(iniGridReportConfig()), "Report Config");
        tabPanel.add(vl.add(iniGridFormApplied()), "Form Applied");
        tabPanel.add(vl.add(iniGridACFilteration()), "A/c Filteration");
        tabPanel.add(vl.add(iniGridWebJobs()), "Web Jobs");
        tabPanel.add(vl.add(inigridPaymentMode()), "Payment Mode");
        tabPanel.add(vl.add(inigridItemCategory()), "Item Category Filteration");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuExportData = new MenuItem("Export Data", selectionHandler);
        menu.add(mnuExportData);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Applied Grid">
    interface Grid_FormApplied extends KeyFieldHashCode_T_PrpAcs<GrdFormApplied_Mdl> {

        @Editor.Path("Location")
        ValueProvider<GrdFormApplied_Mdl, String> location();

        @Editor.Path("AppliedFor")
        ValueProvider<GrdFormApplied_Mdl, String> appliedFor();

    }
    final DmGrid<GrdFormApplied_Mdl, Grid_FormApplied> gridFormApplied = new DmGrid(com.google.gwt.core.shared.GWT.create(Grid_FormApplied.class));

    public Grid iniGridFormApplied() {
        gridFormApplied.addRowNumberColumn();
        gridFormApplied.addTextColumn(gridFormApplied.getPropertyAccess().name(), "Name", 200, "");
        gridFormApplied.addTextColumn(gridFormApplied.getPropertyAccess().location(), "Location", 160, "");
        gridFormApplied.addTextColumn(gridFormApplied.getPropertyAccess().appliedFor(), "Applied For", 210, "");
        gridFormApplied.initializeGrid(gridFormApplied.getPropertyAccess().fieldHashCode(), 590, 270);
        return gridFormApplied.getGrid();

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridACFilteration ">
    interface Grd_Entry extends KeyFieldHashCode_T_PrpAcs<GrdACFilteration_Mdl> {

        @Editor.Path("nature")
        ValueProvider<GrdACFilteration_Mdl, String> nature();
    }

    final DmGrid<GrdACFilteration_Mdl, Grd_Entry> gridACFilteration = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_Entry.class));

    private Grid iniGridACFilteration() {

        gridACFilteration.addRowNumberColumn();
        gridACFilteration.addTextColumn(gridACFilteration.getPropertyAccess().nature(), "Nature", 485, "");
        gridACFilteration.initializeGrid(gridACFilteration.getPropertyAccess().fieldHashCode(), 590, 270);

        return gridACFilteration.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridWebJobs">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdWebJobs_Mdl> {

        @Editor.Path("TemplateTypeName")
        ValueProvider<GrdWebJobs_Mdl, String> templateTypeName();

    }

    final DmGrid<GrdWebJobs_Mdl, Grd_PrpAcs> gridWebJobs = new DmGrid(com.google.gwt.core.client.GWT.create(Grd_PrpAcs.class));

    public Grid iniGridWebJobs() {
        gridWebJobs.addRowNumberColumn();
        gridWebJobs.addTextColumn(gridWebJobs.getPropertyAccess().name(), "Template Name", 240, "");
        gridWebJobs.addTextColumn(gridWebJobs.getPropertyAccess().templateTypeName(), "Template Type", 250, "");
        gridWebJobs.initializeGrid(gridWebJobs.getPropertyAccess().fieldHashCode(), 590, 270);

        return gridWebJobs.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridReportConfig">
    interface GrdReport_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdReportConfig_Mdl> {

        @Editor.Path("ReportName")
        ValueProvider<GrdReportConfig_Mdl, String> reportName();

    }

    final DmGrid<GrdReportConfig_Mdl, GrdReport_PrpAcs> gridReportConfig = new DmGrid(GWT.create(GrdReport_PrpAcs.class));

    public Grid iniGridReportConfig() {
        gridReportConfig.addRowNumberColumn();
        gridReportConfig.addTextColumn(gridReportConfig.getPropertyAccess().name(), "Site", 220, "");
        gridReportConfig.addTextColumn(gridReportConfig.getPropertyAccess().reportName(), "Report Name", 285, "");
        gridReportConfig.initializeGrid(gridReportConfig.getPropertyAccess().fieldHashCode(), 590, 270);

        return gridReportConfig.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridPaymentMode">
    interface GrdReceiptType_Mdl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdReceiptType_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<GrdReceiptType_Mdl, String> shortName();

        @Editor.Path("IsDefaultCode")
        ValueProvider<GrdReceiptType_Mdl, String> isDefaultCode();
    }

    final DmGrid<GrdReceiptType_Mdl, GrdReceiptType_Mdl_PrpAcs> gridPaymentMode = new DmGrid(GWT.create(GrdReceiptType_Mdl_PrpAcs.class));

    public Grid inigridPaymentMode() {
        gridPaymentMode.addRowNumberColumn();
        gridPaymentMode.addTextColumn(gridPaymentMode.getPropertyAccess().name(), "Payment Mode ", 220, "");
        gridPaymentMode.addTextColumn(gridPaymentMode.getPropertyAccess().shortName(), "Short Name", 230, "");
        ColumnConfig<GrdReceiptType_Mdl, String> selectCheckBox;
        selectCheckBox = gridPaymentMode.addTextColumn(gridPaymentMode.getPropertyAccess().isDefaultCode(), "Default", 60, "");
        gridPaymentMode.initializeGrid(gridPaymentMode.getPropertyAccess().fieldHashCode(), 590, 270);

        selectCheckBox.setHorizontalAlignment(HasAutoHorizontalAlignment.ALIGN_CENTER);

        gridPaymentMode.getGrid().getView().setViewConfig(new GridViewConfig<GrdReceiptType_Mdl>() {

            @Override
            public String getColStyle(GrdReceiptType_Mdl model, ValueProvider<? super GrdReceiptType_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";
                if (colIndex == 2) {
                    if (DmShared.vNull(model.getIsDefaultCode()).doubleValue() > 1) {
                        styleClass += "dm-FontBold ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdReceiptType_Mdl model, int rowIndex) {
                return "";
            }
        });

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(gridPaymentMode.getGrid().getStore().get(context.getIndex()).getIsDefaultCode()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return gridPaymentMode.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridItemCat ">
    interface GrdItemCat_Entry extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmGrid<KeyFieldExtended, GrdItemCat_Entry> gridItemCat = new DmGrid(com.google.gwt.core.shared.GWT.create(GrdItemCat_Entry.class));

    private Grid inigridItemCategory() {
        gridItemCat.addRowNumberColumn();
        gridItemCat.addTextColumn(gridItemCat.getPropertyAccess().name(), "Item Category", 485, "");
        gridItemCat.initializeGrid(gridItemCat.getPropertyAccess().fieldHashCode(), 590, 270);

        return gridItemCat.getGrid();
    }
    //</editor-fold>

}
