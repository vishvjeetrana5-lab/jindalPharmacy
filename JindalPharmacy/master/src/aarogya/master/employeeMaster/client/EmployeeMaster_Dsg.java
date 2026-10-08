package aarogya.master.employeeMaster.client;

import aarogya.master.employeeMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
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
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmFileManager.shared.DmFileRuleType;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmSnapshot.client.DmSnapshot;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class EmployeeMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().EMPLOYEE_MASTER.caption;
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
    final DmTextBox txtManualCode = new DmTextBox(25, false);
    final DmTextBox txtFatherName = new DmTextBox(50, false);
    final DmTextBox txtAddress = new DmTextBox(100, false);
    final DmTextBox txtAddress1 = new DmTextBox(100, false);
    final DmTextBox txtAddresstemp = new DmTextBox(100, false);
    final DmTextBox txtAddress1temp = new DmTextBox(100, false);
    final DmTextBoxNumeric txtPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBoxNumeric txtPinCodetemp = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtPhoneNo = new DmTextBox(35, false);
    final DmTextBoxMobile txtMobileNo = new DmTextBoxMobile(false);
    final DmTextBox txtEMail = new DmTextBox(250, false);
    final DmTextBox txtDob = new DmTextBox(11, false);
    final DmTextBox txtDOA = new DmTextBox(11, false);
    final DmTextBox txtDOJ = new DmTextBox(11, false);
    final DmTextBox txtDOR = new DmTextBox(11, false);
    final DmTextBox txtQualification = new DmTextBox(50, false);
    final DmTextBox txtPAN = new DmTextBox(10, false);
    final DmTextBox txtBankAccNo = new DmTextBox(20, false);
    final DmTextBox txtPfAccNo = new DmTextBox(25, false);
    final DmTextBox txtifscode = new DmTextBox(15, false);
    final DmTextBox txtPassporNo = new DmTextBox(20, false);
    final DmTextBox txtPassportIssueDate = new DmTextBox(35, false);
    final DmTextBox txtPassportIssuePlace = new DmTextBox(20, false);
    final DmTextBox txtPassportExpiryDate = new DmTextBox(35, false);
    final DmTextBox txtLicenseNo = new DmTextBox(25, false);
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final DmTextBox txtCountry = new DmTextBox(50, false);
    final DmTextBox txtCountryTemp = new DmTextBox(50, false);
    final Button btnCopyAddress = new Button();
    final FlowPanel flwCopyAddress = new FlowPanel();
    final DmTextBox txtAadhaarNo = new DmTextBox(12, false);
    final DmTextBox txtLicenseExpiry = new DmTextBox(11, false);
    final DmTextBoxNumeric txtSalary = new DmTextBoxNumeric(2, false, 7, false);
    DmFileRuleType fileRuleType;
    DmSnapshot snapshot;
    final Menu menu = new Menu();
    MenuItem manageSiteMenu, manageFileMenu;
    SelectionHandler<MenuItem> selectionHandler;
    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHtAccountNamePrefix">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccountNamePrefix = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

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
    final DmTextHelp<TxtHAccountGroup_Mdl, TxtHAccountGroup> txtHAccountGroup = new DmTextHelp(GWT.create(TxtHAccountGroup.class), 10, true);

    private void iniTxtHAccountGroup() {
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().name(), "Group", 0, "GroupName");
        txtHAccountGroup.getDmGrid().addTextColumn(txtHAccountGroup.getDmGrid().getPropertyAccess().natureName(), "Nature", 150, "NatureName");
        txtHAccountGroup.getDmGrid().initializeGrid(txtHAccountGroup.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHAccountGroup.getHelper().addSort("GroupName", DmSortType.ASC);
        txtHAccountGroup.getHelper().addSort("NatureName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDesignation">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDesignation = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHDesignation() {
        txtHDesignation.getDmGrid().addTextColumn(txtHDesignation.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDesignation.getDmGrid().initializeGrid(txtHDesignation.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHDesignation.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface TxtHCity_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

    }

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_PrpAcs.class), 10);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);
        txtHCity.getHelper().addSort("stateName", DmSortType.ASC);
        txtHCity.getHelper().addSort("countryName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCityTemp">
    interface TxtHCityTemp_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

    }
    final DmTextHelp<TxtHCity_Mdl, TxtHCityTemp_PrpAcs> txtHCityTemp = new DmTextHelp(GWT.create(TxtHCityTemp_PrpAcs.class), 10);

    private void iniTxtHCityTemp() {
        txtHCityTemp.getDmGrid().addTextColumn(txtHCityTemp.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHCityTemp.getDmGrid().addTextColumn(txtHCityTemp.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCityTemp.getDmGrid().addTextColumn(txtHCityTemp.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCityTemp.getDmGrid().initializeGrid(txtHCityTemp.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHCityTemp.getHelper().addSort("CityName", DmSortType.ASC);
        txtHCityTemp.getHelper().addSort("stateName", DmSortType.ASC);
        txtHCityTemp.getHelper().addSort("countryName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHZone">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHZone = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHZone() {
        txtHZone.getDmGrid().addTextColumn(txtHZone.getDmGrid().getPropertyAccess().name(), "Zone", 0, "NAME");
        txtHZone.getDmGrid().initializeGrid(txtHZone.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHZone.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 1, "Nature");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHNature.getHelper().addSort("Nature", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTDSCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTDSCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHTDSCategory() {
        txtHTDSCategory.getDmGrid().addTextColumn(txtHTDSCategory.getDmGrid().getPropertyAccess().name(), "TDS Category", 0, "NAME");
        txtHTDSCategory.getDmGrid().initializeGrid(txtHTDSCategory.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHTDSCategory.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGender">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGender = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtGender() {
        txtHGender.getDmGrid().addTextColumn(txtHGender.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGender.getDmGrid().initializeGrid(txtHGender.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHGender.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHMaritalStatus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMaritalStatus = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHMaritalStatus() {
        txtHMaritalStatus.getDmGrid().addTextColumn(txtHMaritalStatus.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMaritalStatus.getDmGrid().initializeGrid(txtHMaritalStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHMaritalStatus.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHBloodGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHBloodGroup = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 8);

    private void iniTxtHBloodGroup() {
        txtHBloodGroup.getDmGrid().addTextColumn(txtHBloodGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHBloodGroup.getDmGrid().initializeGrid(txtHBloodGroup.getDmGrid().getPropertyAccess().code(), 75, 0, 1);
        txtHBloodGroup.getHelper().addSort("Name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHReligion">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReligion = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHReligion() {
        txtHReligion.getDmGrid().addTextColumn(txtHReligion.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHReligion.getDmGrid().initializeGrid(txtHReligion.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHReligion.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends PropertyAccess<Find_Mdl> {

        @Editor.Path("code")
        ModelKeyProvider<Find_Mdl> code();

        @Editor.Path("AccountName")
        ValueProvider<Find_Mdl, String> accountName();

        @Editor.Path("DisplayName")
        ValueProvider<Find_Mdl, String> displayName();

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
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accountName(), "Account Name", 0, "Account_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().displayName(), "Display Name", 100, "displayName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 80, "Manual_Code");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accGroupName(), "Account Group", 100, "Ac_Group");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 80, "Nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address(), "Address", 200, "Address");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cityName(), "City", 100, "City");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "Country");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().zoneName(), "Zone", 80, "Zone");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().pin(), "PIN", 90, "Pin");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().phoneNo(), "Phone", 100, "Phone");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobileNo(), "Mobile", 100, "Mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().email(), "Email", 130, "E_Mail");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 200);
        dmFind.getHelper().addSort("Account_Name", DmSortType.ASC);

    }
    //</editor-fold>

    public EmployeeMaster_Dsg() {

        ini();
    }

    private void ini() {
        createMenu();
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        DmClient.setStyle(flwWorkingArea, "100%", "520px", null, null, DmPosition.NONE, true);
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
        DmClient.setStyle(tabPanel, "100%", "475px", null, null, DmPosition.CENTER_HORIZONTAL, true);
        tabPanel.add(generalTab(), "General");
        tabPanel.add(gridTab(), "Bank Account Detail");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel generalTab() {
        FlowPanel flwTable, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "430px", null, null, DmPosition.CENTER, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "430px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftArea());
        flwTable.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "430px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightArea());
        flwTable.add(flwTd);
        return flwTable;
    }

    private FlowPanel gridTab() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "429px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "260px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "260px", "left", null, DmPosition.NONE, false);

        flwTd.add(vl.add(iniGridBank()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "430px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        iniAccountNamePrefix();
        flwTd.add(vl.add(txtHAccountNamePrefix));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Display Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtGender();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Marital Status");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHMaritalStatus();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMaritalStatus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Religion");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        iniTxtHReligion();
        flwTd.add(vl.add(txtHReligion));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHAccountGroup();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        iniNature();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Zone");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHZone();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHZone));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Designation");
        lbl.setWidth("100%");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHDesignation();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDesignation));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        lbl.setWidth("100%");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHDepartment();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Father Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFatherName));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 17th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Phone No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPhoneNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobileNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 17th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Aadhaar No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAadhaarNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Permanent Address");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lbl.getElement().getStyle().setColor("maroon");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHCity();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Country");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCountry));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 12th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PIN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPinCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 13th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Temporary Address");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lbl.getElement().getStyle().setColor("maroon");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        btnCopyAddress.addStyleName("dm-button-grad");
        btnCopyAddress.setText("Copy Address");
        btnCopyAddress.setWidth("88px");
        btnCopyAddress.setHeight("20px");
        btnCopyAddress.getElement().setAttribute("style", btnCopyAddress.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");

        btnCopyAddress.setTitle("Copy Address");
        flwCopyAddress.add(btnCopyAddress);
        flwTd.add(flwCopyAddress);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 13th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddresstemp));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 14th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1temp));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 15th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHCityTemp();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCityTemp));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Country");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
//        iniTxtHCountryTemp();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCountryTemp));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 16th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PIN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPinCodetemp));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;

    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "410px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "148px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "53%", "150px", "left", null, DmPosition.NONE, false);
        flwTd.add(RightAreaTop());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "44%", "150px", "left", null, DmPosition.NONE, false);
        flwTd.add(imageArea());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "260px", "left", null, DmPosition.NONE, true);
        flwTr.add(rightAreaBottom());
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel RightAreaTop() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "150px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date of Birth");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDob));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Anniv. Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOA));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Joining Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOJ));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Resignation Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOR));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Blood Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        iniTxtHBloodGroup();
        flwTd.add(vl.add(txtHBloodGroup));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PAN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPAN));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "51.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("IFSC Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtifscode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel imageArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "96%", "150px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, null, "150px", "right", null, DmPosition.NONE, false);
        fileRuleType = new DmFileRuleType();
        fileRuleType.setRuleCode(ProjectRuleCode.CAMERA.getRuleCode());
        fileRuleType.setFileTypeName("Snapshot");
        fileRuleType.setFileSizeLimit("5120");
        fileRuleType.setFilters("jpg,bmp,gif,jpeg");
        snapshot = new DmSnapshot(150, 140, fileRuleType);
        flwTr.add(snapshot);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightAreaBottom() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "260px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bank A/c No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBankAccNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PF A/c No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPfAccNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("License No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLicenseNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("License Expiry");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLicenseExpiry));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Passport No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPassporNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Passport Issue Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPassportIssueDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPassportExpiryDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Passport Issue Place");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPassportIssuePlace));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEMail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Qualification");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQualification));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/


 /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("TDS Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        iniTxtHTDSCategory();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTDSCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/


 /*====================================< 12th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 13th Row >====================================*/

 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHActive));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Salary");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSalary));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

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
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().accountNo(), "Account No.", 150, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().bankName(), "Bank", 180, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().branch(), "Branch", 160, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().branchAddress(), "Branch Address", 180, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().ifscCode(), "IFSC Code", 100, "");
        grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().swiftNo(), "Swift No.", 100, "");
        selectDefaultYes = grdBankDetail.addTextColumn(grdBankDetail.getPropertyAccess().isDefault(), "Default", 50, "");
        grdBankDetail.initializeGrid(grdBankDetail.getPropertyAccess().fieldHashCode(), 1260, 240);
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
