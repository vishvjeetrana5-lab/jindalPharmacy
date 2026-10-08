package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class OrganizationMaster_Dsg extends IsWidgetExtended {

    public static String widgetName = new pmMaster().ORGANIZATION_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String priceListBlog = "Price List";
    public static final String tabContactPerson = "Contact Person";
    public static final String tabRateList = "Rate List Detail";

    public static final String tabGUI = "GUI";
    public static final String tabLinkAccounts = "Link Accounts";

    public final static String percentBlog = "Organization %";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final FlowPanel flwContact = new FlowPanel();
    final FlowPanel flwGUI = new FlowPanel();
    final FlowPanel flwLinkAccounts = new FlowPanel();

    final DmSuggestBox txtName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);
    final DmSuggestBox txtShortName = new DmSuggestBox(10, 10, true, "shortName", DmDataType.STRING);

    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);
    final DmTextBox txtAgreementNo = new DmTextBox(20, false);
    final DmTextBox txtAgreementDate = new DmTextBox(11, false);
    final DmTextBox txtRevisionDate = new DmTextBox(11, false);
    final DmTextBox txtTarrifRevisionDate = new DmTextBox(11, false);
    final DmTextBox txtGSTIN = new DmTextBox(15, false);
    final Button priceListBtn = new Button("Price List");

    final DmTextBox txtPharmacyDiscount = new DmTextBox(50, false);
    final DmTextBox txtPharmacyDiscountAllow = new DmTextBox(50, false);
    final DmTextBox txtMedicineSaleOn = new DmTextBox(50, false);
    final Button btnOrgDiscountPercent = new Button();
    final FlowPanel flwOrgPercent = new FlowPanel();
    final DmTextBox txtEffectiveDate = new DmTextBox(11, true);
    final DmTextBox txtServiceAppcblDate = new DmTextBox(11, false);

    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final MenuItem mnuExportOrganization = new MenuItem("Export Organization");
    final Menu menu = new Menu();
    final DmTextBox txtEmail = new DmTextBox(50, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmTextBoxNumeric txtBillingPer = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 6, false);
    final DmTextBox txtConsFeesOrganizationService = new DmTextBox(100, false);

    Button btnOperationPercentage = new Button();
    final DmGUI gui = new DmGUI(new DmTopControl(vl, userControl), 360);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("TdsAc")
        ValueProvider<Find_Mdl, String> tds();

        @Editor.Path("GsTin")
        ValueProvider<Find_Mdl, String> gsTin();

        @Editor.Path("OrgEMail")
        ValueProvider<Find_Mdl, String> eMail();

        @Editor.Path("Mobile")
        ValueProvider<Find_Mdl, String> mobile();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 110, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 110, "nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().eMail(), "e-Mail", 150, "eMail");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gsTin(), "GSTIN", 100, "gsTin");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tds(), "TDS A/c", 120, "tds");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 50, "isActive");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
        dmFind.getHelper().addSort("manualCode", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsTpa">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHIsTpa() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHNature.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTDSAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTDSAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHTDSAc() {
        txtHTDSAc.getDmGrid().addTextColumn(txtHTDSAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHTDSAc.getDmGrid().initializeGrid(txtHTDSAc.getDmGrid().getPropertyAccess().code(), 240, 0, 1);

        txtHTDSAc.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    
        //<editor-fold defaultstate="collapsed" desc="txtHDiscountGiven">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDiscountGiven = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHDiscountGiven() {
        txtHDiscountGiven.getDmGrid().addTextColumn(txtHDiscountGiven.getDmGrid().getPropertyAccess().name(), "Consultant", 0, "name");
        txtHDiscountGiven.getDmGrid().initializeGrid(txtHDiscountGiven.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHDiscountGiven.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHDiscountReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReason = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHReason() {
        txtHReason.getDmGrid().addTextColumn(txtHReason.getDmGrid().getPropertyAccess().name(), "Consultant", 0, "NAME");
        txtHReason.getDmGrid().initializeGrid(txtHReason.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHReason.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTPAControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTPAControlAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHTPAControlAc() {
        txtHTPAControlAc.getDmGrid().addTextColumn(txtHTPAControlAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHTPAControlAc.getDmGrid().initializeGrid(txtHTPAControlAc.getDmGrid().getPropertyAccess().code(), 240, 0, 1);

        txtHTPAControlAc.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTPAControlAc">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHExcessecurityAc = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHExcessecurityAc() {
        txtHExcessecurityAc.getDmGrid().addTextColumn(txtHExcessecurityAc.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHExcessecurityAc.getDmGrid().initializeGrid(txtHExcessecurityAc.getDmGrid().getPropertyAccess().code(), 240, 0, 1);

        txtHExcessecurityAc.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHRateList">
    interface RateList_PrpAcs extends KeyFieldHashCode_T_PrpAcs<RateList_Mdl> {

        @Editor.Path("RateListType")
        ValueProvider<RateList_Mdl, String> rateListType();

    }
    final DmTextHelp<RateList_Mdl, RateList_PrpAcs> txtHRateList = new DmTextHelp(GWT.create(RateList_PrpAcs.class), 10, true);

    private void iniTxtHRateList() {
        txtHRateList.getDmGrid().addTextColumn(txtHRateList.getDmGrid().getPropertyAccess().name(), "Name", 120, "Name");
        txtHRateList.getDmGrid().addTextColumn(txtHRateList.getDmGrid().getPropertyAccess().rateListType(), "Type", 70, "name");
        txtHRateList.getDmGrid().initializeGrid(txtHRateList.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 1);
        txtHRateList.getHelper().addSort("name", DmSortType.ASC);
        txtHRateList.getHelper().addSort("rateListType", DmSortType.ASC);
    }
    //</editor-fold>

    public OrganizationMaster_Dsg() {
        ini();
        iniTxtHIsTpa();
        iniTxtHActive();
        iniTxtHTDSAc();
        iniTxtHRateList();
        iniTxtHDiscountGiven();
        iniTxtHReason();
        iniTxtHTPAControlAc();
        iniTxtHExcessecurityAc();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        iniMenu();
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(50);
        priceListBtn.addStyleName("dm-button-grad");
        priceListBtn.setWidth("100px");
        userControl.addControlBlog(priceListBlog, priceListBtn);
        userControl.addGap(50);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "517px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "100px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "417px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());

        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        int tabIndex = 0;
        DmClient.setStyle(tabPanel, "100%", "420px", null, null, DmPosition.NONE, true);

        flwContact.add(iniGrdContactPerson());
        flwGUI.add(GUITabArea());
        flwLinkAccounts.add(LinkAccounts());

        tabPanel.add(serviceGrd(), tabRateList);
        tabPanel.getTabWidget(tabIndex).getParent().setWidth("103px");
        tabIndex++;

        tabPanel.add(flwContact, tabContactPerson);
        tabPanel.getTabWidget(tabIndex).getParent().setWidth("103px");
        tabIndex++;

        tabPanel.add(flwGUI, tabGUI);
        tabPanel.getTabWidget(tabIndex).getParent().setWidth("38px");
        tabIndex++;

        tabPanel.add(flwLinkAccounts, tabLinkAccounts);
        tabPanel.getTabWidget(tabIndex).getParent().setWidth("103px");
        tabIndex++;

        tabPanel.ensureDebugId("cwTabPanel");
        tabPanel.selectTab(0);
        return tabPanel;
    }

    protected FlowPanel serviceGrd() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "390px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginBottom(1, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate List");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRateList));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Effective Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEffectiveDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy Discount Allow");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtPharmacyDiscountAllow);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Sale On");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtMedicineSaleOn);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pharmacy %");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtPharmacyDiscount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "right", null, DmPosition.NONE, false);
        DmClient.setStyle(btnOrgDiscountPercent, "150px", "22px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        btnOrgDiscountPercent.setText("Organization %");
        btnOrgDiscountPercent.addStyleName("dm-button-grad");
        flwOrgPercent.add(btnOrgDiscountPercent);
        flwTd.add(flwOrgPercent);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "left", null, DmPosition.NONE, false);
        flwTd.add(iniGrdRateList());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGridServices()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel GUITabArea() {
        FlowPanel flwTable;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "404px", null, null, DmPosition.CENTER, true);
        flwTable.add(gui);
        return flwTable;
    }

    private FlowPanel LinkAccounts() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "404px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TPA Control A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTPAControlAc));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Excess A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHExcessecurityAc));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Given By");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDiscountGiven));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Dis. Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReason));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GSTIN No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGSTIN));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTDSAc));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Billing %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBillingPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Agreement No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAgreementNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Agreement Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAgreementDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Revision Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRevisionDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Tarrif Revision Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTarrifRevisionDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "right", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        btnOperationPercentage.setText("Operation %");
        btnOperationPercentage.setWidth("100px");
        flwTd.add(vl.add(btnOperationPercentage));

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuManageSite);
        menu.add(mnuExportOrganization);
        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="grdContactPerson">
    interface GridContactPerson_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdContactPerson_Mdl> {

        @Editor.Path("mobileNo")
        ValueProvider<GrdContactPerson_Mdl, String> mobileNo();

        @Editor.Path("landlineNo")
        ValueProvider<GrdContactPerson_Mdl, String> landlineNo();

        @Editor.Path("designation")
        ValueProvider<GrdContactPerson_Mdl, String> designation();

        @Editor.Path("IsChecked")
        ValueProvider<GrdContactPerson_Mdl, String> isChecked();

    }
    final DmGrid<GrdContactPerson_Mdl, GridContactPerson_PrpAcs> grdContactPerson = new DmGrid(GWT.create(GridContactPerson_PrpAcs.class));

    private Grid iniGrdContactPerson() {
        ColumnConfig<GrdContactPerson_Mdl, String> selectCheckBox;

        grdContactPerson.addRowNumberColumn();
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().name(), "Contact Person", 370, "");
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().mobileNo(), "Mobile No", 180, "");
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().landlineNo(), "LandLine No", 190, "");
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().designation(), "Designation", 350, "");
        selectCheckBox = grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().isChecked(), "Default", 100, "");
        grdContactPerson.initializeGrid(grdContactPerson.getPropertyAccess().fieldHashCode(), 1250, 365);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdContactPerson.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdContactPerson.getGrid();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GrdRateList">
    interface GridRateList_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRateList_Mdl> {

        @Editor.Path("RateListType")
        ValueProvider<GrdRateList_Mdl, String> rateListType();

        @Editor.Path("ApplicableFrom")
        ValueProvider<GrdRateList_Mdl, String> applicableFrom();

    }
    final DmGrid<GrdRateList_Mdl, GridRateList_PrpAcs> grdRateList = new DmGrid(GWT.create(GridRateList_PrpAcs.class));

    private Grid iniGrdRateList() {
        grdRateList.addRowNumberColumn();
        grdRateList.addTextColumn(grdRateList.getPropertyAccess().name(), "Rate List", 150, "");
        grdRateList.addTextColumn(grdRateList.getPropertyAccess().rateListType(), " Rate List Type", 120, "");
        grdRateList.addTextColumn(grdRateList.getPropertyAccess().applicableFrom(), "Applicable From", 120, "");
        grdRateList.initializeGrid(grdRateList.getPropertyAccess().fieldHashCode(), 440, 130, 2);

        return grdRateList.getGrid();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Grd Service & Investigation">
    interface GrdServices_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServices_Mdl> {

        @Editor.Path("CategoryText")
        ValueProvider<GrdServices_Mdl, String> category();

        @Editor.Path("OrganizationServiceName")
        ValueProvider<GrdServices_Mdl, String> organizationServiceName();

        @Editor.Path("Rate")
        ValueProvider<GrdServices_Mdl, String> rate();

        @Editor.Path("DiscountAmount")
        ValueProvider<GrdServices_Mdl, String> discountAmount();

        @Editor.Path("DiscountPercentage")
        ValueProvider<GrdServices_Mdl, String> discountPercentage();

        @Editor.Path("PrintSNo")
        ValueProvider<GrdServices_Mdl, String> printSNo();

    }
    final DmGrid<GrdServices_Mdl, GrdServices_PrpAcs> grdService = new DmGrid(GWT.create(GrdServices_PrpAcs.class));

    private Grid iniGridServices() {
        final int colRate, colDiscPer, colDisAmt;
        int index = 0;
        grdService.addRowNumberColumn();
        index++;
        grdService.addTextColumn(grdService.getPropertyAccess().name(), "Service", 280, "");
//        index++;
//        grdService.addTextColumn(grdService.getPropertyAccess().organizationServiceName(), "Org. Service Name", 280, "");
        index++;
        grdService.addTextColumn(grdService.getPropertyAccess().category(), "Service Category", 385, "");
        index++;
        colRate = index;
        grdService.addTextColumn(grdService.getPropertyAccess().rate(), "Rate", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        colDiscPer = index;
        grdService.addTextColumn(grdService.getPropertyAccess().discountPercentage(), "Discount %", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        colDisAmt = index;
        grdService.addTextColumn(grdService.getPropertyAccess().discountAmount(), "Discount", 85, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        grdService.addTextColumn(grdService.getPropertyAccess().printSNo(), "Print S.No.", 95, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        index++;
        grdService.initializeGrid(grdService.getPropertyAccess().fieldHashCode(), 1245, 235, 2);

        grdService.getGrid().getView().setViewConfig(new GridViewConfig<GrdServices_Mdl>() {
            @Override
            public String getColStyle(GrdServices_Mdl model, ValueProvider<? super GrdServices_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (model.isColorChange()) {
                    if ((colIndex == colDisAmt) || (colIndex == colDiscPer) || (colIndex == colRate)) {
                        styleClass += "dm-GridCellDisable ";
                    }
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdServices_Mdl model, int rowIndex) {
                return "";
            }
        });
        return grdService.getGrid();

    }
    //</editor-fold>

}
