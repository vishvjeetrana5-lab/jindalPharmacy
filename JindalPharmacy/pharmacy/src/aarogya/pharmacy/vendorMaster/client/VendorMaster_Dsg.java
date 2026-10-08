package aarogya.pharmacy.vendorMaster.client;

import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.pharmacy.vendorMaster.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class VendorMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().SENDARY_DEBTOR_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    final DmSuggestBox txtName = new DmSuggestBox(100, 10, true, "NAME", DmDataType.STRING);
    final DmTextBox txtDisplayName = new DmTextBox(100, true);
    final DmTextBox txtLegalName = new DmTextBox(100, true);
    final DmTextBox txtTradeName = new DmTextBox(100, false);
    final DmTextBox txtManualCode = new DmTextBox(25, false);
    final DmGUI gui = new DmGUI(topControl, 450);
    final DmTextBox txtAddress = new DmTextBox(100, true);
    final DmTextBox txtAddress1 = new DmTextBox(100, false);
    final DmTextBoxNumeric txtPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtPhoneNo = new DmTextBox(10, false);
    final DmTextBoxNumeric txtISDCode = new DmTextBoxNumeric(0, false, 5, false);
    final DmTextBox txtMobileNo = new DmTextBox(10, true);
    final DmTextBox txtFaxNo = new DmTextBox(35, false);
    final DmTextBox txtEMail = new DmTextBox(250, true);
    final DmTextBox txtPAN = new DmTextBox(10, true);
    final DmTextBox txtGstTin = new DmTextBox(15, false);
    final DmTextBox txtDrugLicense = new DmTextBox(100, false);
    final DmTextBox txtRegistration = new DmTextBox(20, false);
    final DmTextBox txtState = new DmTextBox(100, false);
    final DmTextBox txtTIN = new DmTextBox(20, false);
    final DmTextBox txtIECNo = new DmTextBox(35, false);
    final DmTextBoxNumeric txtCreditLimit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtDueDays = new DmTextBoxNumeric(0, false, 5, false);
    final DmTextBoxNumeric txtInvoiceDiscount = new DmTextBoxNumeric(2, false, 8, false);
    final DmTextBoxNumeric txtExpiryTerm = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBoxNumeric txtMax = new DmTextBoxNumeric(0, false, 8, false);
    final DmTextBoxNumeric txtMin = new DmTextBoxNumeric(0, false, 8, false);
    final DmTextBoxNumeric txtReorderLevel = new DmTextBoxNumeric(0, false, 8, false);
    final DmTextArea txtRemark = new DmTextArea(250, false);
    final DmTextBoxNumeric txtAadhar = new DmTextBoxNumeric(0, false, 12, false);
    final DmTextBox txtGstDate = new DmTextBox(11, false);
    final Menu menu = new Menu();
    final DmTextBoxNumeric txtProfitPercentage = new DmTextBoxNumeric(2, false, 8, false);
    final DmTextBoxNumeric txtCreditLimitAmt = new DmTextBoxNumeric(2, false, 8, false);
    final DmTextBoxNumeric txtCreditLimitDays = new DmTextBoxNumeric(0, false, 8, false);

    MenuItem manageSiteMenu, manageFileMenu;
    SelectionHandler<MenuItem> selectionHandler;

    int colIndex_EditAdditionalPlaceOfBussiness = -1;

    abstract void menuClick(SelectionEvent<MenuItem> event);
    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHtAccountNamePrefix">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccountNamePrefix = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniAccountNamePrefix() {
        txtHAccountNamePrefix.getDmGrid().addTextColumn(txtHAccountNamePrefix.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHAccountNamePrefix.getDmGrid().initializeGrid(txtHAccountNamePrefix.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHAccountNamePrefix.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAccountGroup">
    interface TxtHAccountGroup extends KeyField_T_PrpAcs<TxtHAccountGroup_Mdl> {

        @Editor.Path("NatureName")
        ValueProvider<TxtHAccountGroup_Mdl, String> natureName();

    }
    final DmTextHelp<TxtHAccountGroup_Mdl, TxtHAccountGroup> txtHAccountGroup = new DmTextHelp(GWT.create(TxtHAccountGroup.class), 6, true);

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Group", 0, "GroupName");
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().natureName(), "Nature", 150, "NatureName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHAccountGroup.getHelper().addSort("GroupName", DmSortType.ASC);
        txtHAccountGroup.getHelper().addSort("NatureName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenter">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHCostCenter() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHCostCenter.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface TxtHCity_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

    }

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, true);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHZone">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHZone = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHZone() {
        txtHZone.getDmGrid().addTextColumn(txtHZone.getDmGrid().getPropertyAccess().name(), "Zone", 0, "NAME");
        txtHZone.getDmGrid().initializeGrid(txtHZone.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHZone.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10, true);

    public void iniNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 1, "Nature");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHNature.getHelper().addSort("Nature", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPartyCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPartyCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHPartyCategory() {
        txtHPartyCategory.getDmGrid().addTextColumn(txtHPartyCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHPartyCategory.getDmGrid().initializeGrid(txtHPartyCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTDSCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTDSCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHTDSCategory() {
        txtHTDSCategory.getDmGrid().addTextColumn(txtHTDSCategory.getDmGrid().getPropertyAccess().name(), "TDS Category", 0, "NAME");
        txtHTDSCategory.getDmGrid().initializeGrid(txtHTDSCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHTDSCategory.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReferencedTo">
    interface TxtHReferencedTo extends KeyField_T_PrpAcs<TxtHReferencedTo_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHReferencedTo_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHReferencedTo_Mdl, String> manualCode();
    }
    final DmTextHelp<TxtHReferencedTo_Mdl, TxtHReferencedTo> txtHReferencedTo = new DmTextHelp(GWT.create(TxtHReferencedTo.class), 6);

    private void iniTxtHReferencedTo() {
        txtHReferencedTo.getDmGrid().addTextColumn(txtHReferencedTo.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHReferencedTo.getDmGrid().addTextColumn(txtHReferencedTo.getDmGrid().getPropertyAccess().cityName(), "City", 150, "CityName");
        txtHReferencedTo.getDmGrid().addTextColumn(txtHReferencedTo.getDmGrid().getPropertyAccess().manualCode(), "Code", 150, "ManualCode");
        txtHReferencedTo.getDmGrid().initializeGrid(txtHReferencedTo.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHReferencedTo.getHelper().addSort("NAME", DmSortType.ASC);
        txtHReferencedTo.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDuplicate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDuplicate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHDuplicate() {
        txtHDuplicate.getDmGrid().addTextColumn(txtHDuplicate.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHDuplicate.getDmGrid().initializeGrid(txtHDuplicate.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLedgerGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLedgerGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHLadgerGroup() {
        txtHLedgerGroup.getDmGrid().addTextColumn(txtHLedgerGroup.getDmGrid().getPropertyAccess().name(), "Ledger Group", 0, "NAME");
        txtHLedgerGroup.getDmGrid().initializeGrid(txtHLedgerGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHLedgerGroup.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPartyType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPartyType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHPartyType() {
        txtHPartyType.getDmGrid().addTextColumn(txtHPartyType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHPartyType.getDmGrid().initializeGrid(txtHPartyType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdManufacturer">
    interface GridManuf_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdManufacturer_Mdl> {
    }

    final DmGrid<GrdManufacturer_Mdl, GridManuf_PrpAcs> grdManufacturer = new DmGrid(GWT.create(GridManuf_PrpAcs.class));

    private Grid iniGrdManufacture() {
        grdManufacturer.addRowNumberColumn();
        grdManufacturer.addTextColumn(grdManufacturer.getPropertyAccess().name(), "Manufacturer", 330, "");
        grdManufacturer.initializeGrid(grdManufacturer.getPropertyAccess().fieldHashCode(), 377, 200);

        return grdManufacturer.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNonMoving">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNonMoving = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHNonMoving() {
        txtHNonMoving.getDmGrid().addTextColumn(txtHNonMoving.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHNonMoving.getDmGrid().initializeGrid(txtHNonMoving.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMonopoly">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMonopoly = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHMonopoly() {
        txtHMonopoly.getDmGrid().addTextColumn(txtHMonopoly.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHMonopoly.getDmGrid().initializeGrid(txtHMonopoly.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHLifeSaving">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllowForSale = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHtxtHAllowForSale() {
        txtHAllowForSale.getDmGrid().addTextColumn(txtHAllowForSale.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHAllowForSale.getDmGrid().initializeGrid(txtHAllowForSale.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPartyRType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPartyRType = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 7, true, true);

    private void initxtHPartyRType() {
        txtHPartyRType.getDmGrid().addTextColumn(txtHPartyRType.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPartyRType.getDmGrid().initializeGrid(txtHPartyRType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHPartyRType.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends PropertyAccess<Find_Mdl> {

        @Editor.Path("Code")
        ModelKeyProvider<Find_Mdl> code();

        @Editor.Path("CustomerName")
        ValueProvider<Find_Mdl, String> customerName();

        @Editor.Path("DisplayName")
        ValueProvider<Find_Mdl, String> displayName();

        @Editor.Path("LegalName")
        ValueProvider<Find_Mdl, String> legalName();

        @Editor.Path("TradeName")
        ValueProvider<Find_Mdl, String> tradeName();

        @Editor.Path("ManualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("Address")
        ValueProvider<Find_Mdl, String> address();

        @Editor.Path("City")
        ValueProvider<Find_Mdl, String> cityName();

        @Editor.Path("Country")
        ValueProvider<Find_Mdl, String> countryName();

        @Editor.Path("Zone")
        ValueProvider<Find_Mdl, String> zoneName();

        @Editor.Path("Pin")
        ValueProvider<Find_Mdl, String> pin();

        @Editor.Path("PhoneNo")
        ValueProvider<Find_Mdl, String> phoneNo();

        @Editor.Path("MobileNo")
        ValueProvider<Find_Mdl, String> mobileNo();

        @Editor.Path("Email")
        ValueProvider<Find_Mdl, String> email();

        @Editor.Path("FaxNo")
        ValueProvider<Find_Mdl, String> faxNo();

        @Editor.Path("AccGroup")
        ValueProvider<Find_Mdl, String> accGroupName();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("CostCenter")
        ValueProvider<Find_Mdl, String> costCenter();

        @Editor.Path("Tin")
        ValueProvider<Find_Mdl, String> tin();

        @Editor.Path("ReferencedTo")
        ValueProvider<Find_Mdl, String> referencedTo();
    }
    final DmFind<aarogya.pharmacy.vendorMaster.shared.Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().customerName(), "Customer Name", 200, "customerName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().displayName(), "Display Name", 200, "displayName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().legalName(), "Legal Name", 200, "legalname");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tradeName(), "Trade Name", 200, "tradename");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "Manual_Code");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accGroupName(), "Account Group", 150, "Ac_Group");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "Nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address(), "Address", 150, "Address");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cityName(), "City", 100, "City");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "Country");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().zoneName(), "Zone", 100, "Zone");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().pin(), "PIN", 100, "Pin");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().phoneNo(), "Phone", 120, "Phone");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobileNo(), "Mobile", 120, "Mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().email(), "Email", 150, "E_Mail");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().faxNo(), "FAX", 150, "Fax");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().costCenter(), "Cost Center", 100, "Cost_Center");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tin(), "TIN", 100, "TIN");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().referencedTo(), "Distributor", 100, "Distributor");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 200);
        dmFind.getHelper().addSort("customerName", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAllowDiscount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllowDiscount = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHAllowDiscount() {
        txtHAllowDiscount.getDmGrid().addTextColumn(txtHAllowDiscount.getDmGrid().getPropertyAccess().name(), "Allow", 0, "Name");
        txtHAllowDiscount.getDmGrid().initializeGrid(txtHAllowDiscount.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    public VendorMaster_Dsg() {
        super();
        ini();
        iniTxtHNonMoving();
        iniTxtHMonopoly();
        iniTxtHPartyType();
        iniNature();
        iniAccountNamePrefix();
        iniTxtHAccountGroup();
        iniTxtHCostCenter();
        iniTxtHPartyCategory();
        iniTxtHCity();
        iniTxtHZone();
        iniTxtHTDSCategory();
        iniTxtHDuplicate();
        iniTxtHLadgerGroup();
        iniTxtHReferencedTo();
        iniTxtHtxtHAllowForSale();
        initxtHPartyRType();
        iniTxtHAllowDiscount();
    }

    private void ini() {
        createMenu();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flwWorkingArea, "100%", "455px", null, null, DmPosition.NONE, true);
        flwWorkingArea.add(createWorkingArea());
        vrtInnerForm.add(flwWorkingArea);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
        iniFind();

    }

    private TabLayoutPanel createWorkingArea() {
        DmClient.setStyle(tabPanel, "100%", "455px", null, null, DmPosition.CENTER_HORIZONTAL, true);
        tabPanel.add(createGeneralTab(), "General");
        tabPanel.add(createAccountDetailTab(), "Detail - 1");
        tabPanel.add(createOtherDetailTab(), "Detail - 2");
        tabPanel.add(vl.add(gui), "Terms & Condition");
        tabPanel.add(createParameterDetailTab(), "Whole Sale Parameters");
        tabPanel.add(createAdditionalDetailTab(), "Additional Places Of Business");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel createGeneralTab() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        stl = flwTable.getElement().getStyle();
        flwTable.setStyleName("dm-FlwCenter");
        DmClient.setStyle(flwTable, "570px", "350px", null, null, DmPosition.NONE, true);
        stl.setBorderStyle(Style.BorderStyle.NONE);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Customer Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(txtHAccountNamePrefix));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Display Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Legal Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLegalName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Trade Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTradeName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Manual Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Group");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Nature");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Cost Center");
        lblTemp.setWidth("100%");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Party Category");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPartyCategory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Party Record Type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPartyRType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Address");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< t12h Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Pin");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPinCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 13th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 14th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Party Type(GST)");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPartyType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

 /*====================================< 15th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGstTin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGstDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

 /*====================================< 16th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Aadhaar");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAadhar));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("PAN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPAN));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

        return flwTable;
    }

    private FlowPanel createAccountDetailTab() {

        FlowPanel flwTabContent = new FlowPanel();
        flwTabContent.setStyleName("dm-FlwCenter");
        flwTabContent.setWidth("50%");
        flwTabContent.setHeight("380px");
        stl = flwTabContent.getElement().getStyle();
        stl.setBorderStyle(Style.BorderStyle.NONE);
        stl.setTop(10, Style.Unit.PCT);
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        /*====================================< ************ >====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmPosition.CENTER, true);
        /*====================================< Right Section >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Zone");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHZone));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPhoneNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Mobile No.");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(6, Style.Unit.PX);
        flwTd.add(vl.add(txtISDCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTd.add(vl.add(txtMobileNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Fax No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFaxNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("EMail");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEMail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
//        /*====================================< ******** >====================================*/
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Exp. Terms(days)");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryTerm));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Invoice Dis.");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtInvoiceDiscount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Credit Limit");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCreditLimit));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Credit Period");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDueDays));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTIN));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Duplicate");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDuplicate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("IEC No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIECNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Ledger Group");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLedgerGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Drug License");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDrugLicense));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Registration No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegistration));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Referenced To");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReferencedTo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******** >====================================*/
 /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Non-Moving");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNonMoving));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Monopoly");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMonopoly));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Minimum");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Maximum");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMax));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Re-Order Level");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReorderLevel));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TDS Category");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTDSCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Remark");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        txtRemark.setHeight("55px");
        flwTd.add(txtRemark);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< ************* >====================================*/
        flwTabContent.add(flwTable);

        return flwTabContent;
    }

    private FlowPanel createOtherDetailTab() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl, lbl2;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "419px", null, null, DmPosition.CENTER, true);

        // <<<<<<<<<<<<<<<<<<<<<<<<<  1st Row  >>>>>>>>>>>>>>>>>>>>>>>>>
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "18px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manufacturer Detail");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "18px", "left", null, DmPosition.NONE, false);
        lbl2 = new DmLabel("Bank Account Detail");
        lbl2.setStyleName("dm-HLabel");
        lbl2.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl2.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl2.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl2.setHeight("15px");
        lbl2.setWidth("70%");
        flwTd.add(lbl2);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdManufacture()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGridBank()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "18px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person Detail");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        lbl.setWidth("70%");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdContactPerson()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel createParameterDetailTab() {

        FlowPanel flwTabContent = new FlowPanel();
        flwTabContent.setStyleName("dm-FlwCenter");
        flwTabContent.setWidth("50%");
        flwTabContent.setHeight("380px");
        stl = flwTabContent.getElement().getStyle();
        stl.setBorderStyle(Style.BorderStyle.NONE);
        stl.setTop(10, Style.Unit.PCT);
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        /*====================================< ************ >====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Allow For Sale");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllowForSale));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Profit Percentage");
        lblTemp.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtProfitPercentage));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 17th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Allow Discount for Party");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllowDiscount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< End >====================================*/
        flwTabContent.add(flwTable);

        return flwTabContent;
    }

    private FlowPanel createAdditionalDetailTab() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl, lbl2;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "419px", null, null, DmPosition.CENTER, true);

        // <<<<<<<<<<<<<<<<<<<<<<<<<  1st Row  >>>>>>>>>>>>>>>>>>>>>>>>>
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "18px", "left", null, DmPosition.NONE, false);
        lbl2 = new DmLabel("Additional Places of Business");
        lbl2.setStyleName("dm-HLabel");
        lbl2.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lbl2.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl2.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl2.setHeight("15px");
        lbl2.setWidth("70%");
        flwTd.add(lbl2);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGridAdditionalPlace()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdPlaceDetail">
    interface GrdAdditionalPlace_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAdditionalPlace_Mdl> {

        @Editor.Path("Place")
        ValueProvider<GrdAdditionalPlace_Mdl, String> place();

        @Editor.Path("Address1")
        ValueProvider<GrdAdditionalPlace_Mdl, String> address1();

        @Editor.Path("Address2")
        ValueProvider<GrdAdditionalPlace_Mdl, String> address2();

        @Editor.Path("CityName")
        ValueProvider<GrdAdditionalPlace_Mdl, String> cityName();

        @Editor.Path("StateName")
        ValueProvider<GrdAdditionalPlace_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<GrdAdditionalPlace_Mdl, String> countryName();

        @Editor.Path("Mobile")
        ValueProvider<GrdAdditionalPlace_Mdl, String> mobile();

        @Editor.Path("Pin")
        ValueProvider<GrdAdditionalPlace_Mdl, String> pin();

        @Editor.Path("Email")
        ValueProvider<GrdAdditionalPlace_Mdl, String> email();

        @Editor.Path("ColEdit")
        ValueProvider<GrdAdditionalPlace_Mdl, String> colEdit();
    }
    final DmGrid<GrdAdditionalPlace_Mdl, GrdAdditionalPlace_PrpAcs> grdAdditionalPlaceDetail = new DmGrid(GWT.create(GrdAdditionalPlace_PrpAcs.class));

    private Grid iniGridAdditionalPlace() {
        int colIndex = -1;
        ColumnConfig<GrdAdditionalPlace_Mdl, String> editCheckBox;

        ++colIndex;
        grdAdditionalPlaceDetail.addRowNumberColumn();
        colIndex_EditAdditionalPlaceOfBussiness = ++colIndex;
        editCheckBox = grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().colEdit(), "", 30, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().place(), "Place", 150, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().address1(), "Address 1", 150, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().address2(), "Address 2", 150, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().cityName(), "City", 100, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().stateName(), "State", 100, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().countryName(), "Country", 150, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().mobile(), "Mobile", 80, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().email(), "Email", 200, "");
        grdAdditionalPlaceDetail.addTextColumn(grdAdditionalPlaceDetail.getPropertyAccess().pin(), "Pin", 60, "");
        grdAdditionalPlaceDetail.initializeGrid(grdAdditionalPlaceDetail.getPropertyAccess().fieldHashCode(), 1241, 200);

        editCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        editCheckBox.setCellPadding(false);
        editCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-pencil' style='font-size: 1.1em; padding: 4px;'></span>");

            }
        });
        return grdAdditionalPlaceDetail.getGrid();
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="grdBankDetail">

    interface GrdBank_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBankDetail_Mdl> {

        @Editor.Path("AccountNo")
        ValueProvider<GrdBankDetail_Mdl, String> accountNo();

        @Editor.Path("BankName")
        ValueProvider<GrdBankDetail_Mdl, String> bankName();

        @Editor.Path("IfscCode")
        ValueProvider<GrdBankDetail_Mdl, String> ifscCode();

        @Editor.Path("BranchAddress")
        ValueProvider<GrdBankDetail_Mdl, String> branchAddress();

        @Editor.Path("Branch")
        ValueProvider<GrdBankDetail_Mdl, String> branch();

        @Editor.Path("SwiftNo")
        ValueProvider<GrdBankDetail_Mdl, String> swiftNo();

        @Editor.Path("IsDefault")
        ValueProvider<GrdBankDetail_Mdl, String> isDefault();
    }
    final DmGrid<GrdBankDetail_Mdl, GrdBank_PrpAcs> grdBankDetail = new DmGrid(GWT.create(GrdBank_PrpAcs.class));

    private Grid iniGridBank() {
        ColumnConfig<GrdBankDetail_Mdl, String> selectDefaultYes;
        grdBankDetail.addRowNumberColumn();
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().accountNo(), "Account No.", 100, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().bankName(), "Bank", 100, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().branch(), "Branch", 80, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().branchAddress(), "Branch Address", 150, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().ifscCode(), "IFSC Code", 80, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().swiftNo(), "Swift No.", 80, "");
        selectDefaultYes = grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().isDefault(), "Default", 50, "");
        grdBankDetail.initializeGrid(grdBankDetail.getPropertyAccess().fieldHashCode(), 745, 200);

        selectDefaultYes.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectDefaultYes.setCellPadding(false);
        selectDefaultYes.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdBankDetail.getGrid().getStore().get(context.getIndex()).getIsDefault()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });
        return grdBankDetail.getGrid();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="grdContactPerson">
    interface GridContactPerson_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdContactPerson_Mdl> {

        @Editor.Path("MobileNo")
        ValueProvider<GrdContactPerson_Mdl, String> mobileNo();

        @Editor.Path("LandlineNo")
        ValueProvider<GrdContactPerson_Mdl, String> landlineNo();

        @Editor.Path("Designation")
        ValueProvider<GrdContactPerson_Mdl, String> designation();

        @Editor.Path("IsChecked")
        ValueProvider<GrdContactPerson_Mdl, String> isChecked();

    }
    final DmGrid<GrdContactPerson_Mdl, GridContactPerson_PrpAcs> grdContactPerson = new DmGrid(GWT.create(GridContactPerson_PrpAcs.class));

    private Grid iniGrdContactPerson() {
        ColumnConfig<GrdContactPerson_Mdl, String> selectCheckBox;

        grdContactPerson.addRowNumberColumn();
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().name(), "Contact Person", 285, "");
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().mobileNo(), "Mobile No.", 185, "");
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().landlineNo(), "LandLine No.", 150, "");
        grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().designation(), "Designation", 250, "");
        selectCheckBox = grdContactPerson.addTextColumn(grdContactPerson.getPropertyAccess().isChecked(), "Default", 55, "");
        grdContactPerson.initializeGrid(grdContactPerson.getPropertyAccess().fieldHashCode(), 1242, 174);
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

    private Menu createMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        manageSiteMenu = new MenuItem("Manage Site", selectionHandler);
        menu.add(manageSiteMenu);

        manageFileMenu = new MenuItem("Manage File", selectionHandler);
        menu.add(manageFileMenu);

        return menu;
    }

}
