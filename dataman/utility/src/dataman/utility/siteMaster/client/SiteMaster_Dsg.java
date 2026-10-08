package dataman.utility.siteMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.siteMaster.shared.Find_Mdl;
import dataman.utility.siteMaster.shared.TxtHBranch_Mdl;
import dataman.utility.siteMaster.shared.TxtHPLT_Mdl;

public abstract class SiteMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().SITE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtSiteName = new DmTextBox(50, true);
    final DmTextBoxNumeric txtCode = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, false, 4, true);
    final DmTextBox txtManualCode = new DmTextBox(2, true);
    final DmTextBox txtSiteDescription = new DmTextBox(100, false);
    final DmTextBox txtAddress1 = new DmTextBox(50, false);
    final DmTextBox txtAddress2 = new DmTextBox(50, false);
    final DmTextBox txtCity = new DmTextBox(50, false);
    final DmTextBoxNumeric txtPinNo = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 6, false);
    final DmTextBox txtCountry = new DmTextBox(35, false);
    final DmTextBox txtGram = new DmTextBox(50, false);
    final DmTextBox txtPhone = new DmTextBox(30, false);
    final DmTextBox txtFaxNo = new DmTextBox(25, false);
    final DmTextBox txtEmail = new DmTextBox(50, false);
    final DmTextBox txtNotificationNo = new DmTextBox(20, false);
    final DmTextBox txtTinNo = new DmTextBox(12, false);
    final DmTextBox txtTinNoDate = new DmTextBox(12, false);
    final DmTextBox txtGSTin = new DmTextBox(15, false);
    final DmTextBox txtCin = new DmTextBox(15, false);
    final DmTextBox txtPanNo = new DmTextBox(10, false);
    final DmTextBox txtGSTINDate = new DmTextBox(11, false);
    final DmTextBox txtDrugLicenseNo = new DmTextBox(35, false);
    final DmTextBox txtClientCode = new DmTextBox(10, false);
    final DmTextBox txtSerialKey = new DmTextBox(25, false);
    final DmTextBox txtSiteDisplayName = new DmTextBox(75, true);
    final DmTextBox txtIECode = new DmTextBox(20, false);
    final DmTextBox txtRBICode = new DmTextBox(20, false);

    public SiteMaster_Dsg() {
        fIni();
        iniHBranch();
        iniHProductLicense();
        initxtHHeadOffice();
        initxtHRegionalLanguage();
        iniFind();
        initxtHRegistrationType();
        initxtHState();
    }

    //<editor-fold defaultstate="collapsed" desc="txtHProductLicenseKey">
    interface DmTxtHPLT_PrpAcs extends KeyField_T_PrpAcs<TxtHPLT_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHPLT_Mdl, String> shortName();

    }
    final DmTextHelp<TxtHPLT_Mdl, DmTxtHPLT_PrpAcs> txtHProductLicenseKey = new DmTextHelp(GWT.create(DmTxtHPLT_PrpAcs.class), 8);

    private void iniHProductLicense() {
        txtHProductLicenseKey.getDmGrid().addTextColumn(txtHProductLicenseKey.getDmGrid().getPropertyAccess().name(), "Name", 0, "Comp_Name");
        txtHProductLicenseKey.getDmGrid().addTextColumn(txtHProductLicenseKey.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "SName");
        txtHProductLicenseKey.getDmGrid().initializeGrid(txtHProductLicenseKey.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHProductLicenseKey.getHelper().addSort("Comp_Name", DmSortType.ASC);
        txtHProductLicenseKey.getHelper().addSort("SName", DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPBranch">
    interface DmTxtHBranch_PrpAcs extends KeyField_T_PrpAcs<TxtHBranch_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHBranch_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHBranch_Mdl, DmTxtHBranch_PrpAcs> txtHBranch = new DmTextHelp(GWT.create(DmTxtHBranch_PrpAcs.class), 10);

    private void iniHBranch() {

        txtHBranch.getDmGrid().addTextColumn(txtHBranch.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHBranch.getDmGrid().addTextColumn(txtHBranch.getDmGrid().getPropertyAccess().cityName(), "City Name", 0, "City");
        txtHBranch.getDmGrid().initializeGrid(txtHBranch.getDmGrid().getPropertyAccess().code(), 400, 0, 1); //244        
        txtHBranch.getHelper().addSort("Name", DmSortType.ASC);
        txtHBranch.getHelper().addSort("City", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHheadOffice">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeadOffice = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);

    private void initxtHHeadOffice() {
        txtHHeadOffice.getDmGrid().addTextColumn(txtHHeadOffice.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHHeadOffice.getDmGrid().initializeGrid(txtHHeadOffice.getDmGrid().getPropertyAccess().code(), 100, 0, 1); //244
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRegionalLanguage">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRegionalLanguage = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void initxtHRegionalLanguage() {
        txtHRegionalLanguage.getDmGrid().addTextColumn(txtHRegionalLanguage.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRegionalLanguage.getDmGrid().initializeGrid(txtHRegionalLanguage.getDmGrid().getPropertyAccess().code(), 100, 0, 1); //244
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHState">
    interface DmState_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();
    }
    final DmTextHelp<KeyFieldExtended, DmState_PrpAcs> txtHState = new DmTextHelp(GWT.create(DmState_PrpAcs.class), 5, true);

    private void initxtHState() {
        txtHState.getDmGrid().addTextColumn(txtHState.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHState.getDmGrid().addTextColumn(txtHState.getDmGrid().getPropertyAccess().sharedModifiedBy(), "State Code", 100, "numericCode");
        txtHState.getDmGrid().initializeGrid(txtHState.getDmGrid().getPropertyAccess().code(), 300, 0, 1); //244

        txtHState.getHelper().addSort("Name", DmSortType.ASC);
        txtHState.getHelper().addSort("numericCode", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRegistrationType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRegistrationType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void initxtHRegistrationType() {
        txtHRegistrationType.getDmGrid().addTextColumn(txtHRegistrationType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRegistrationType.getDmGrid().initializeGrid(txtHRegistrationType.getDmGrid().getPropertyAccess().code(), 100, 0, 1); //244
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("CodeValue")
        ValueProvider<Find_Mdl, String> codeValue();

        @Editor.Path("SiteName")
        ValueProvider<Find_Mdl, String> siteName();

        @Editor.Path("CompanyName")
        ValueProvider<Find_Mdl, String> companyName();

        @Editor.Path("BranchName")
        ValueProvider<Find_Mdl, String> branchName();

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 20);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().siteName(), "Site Name", 100, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().codeValue(), "Code", 100, "Code");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().companyName(), "Company Name", 200, "CompanyName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().branchName(), "Branch Name", 200, "BranchName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 200, "SName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 200, 1);//183

        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("Code", DmSortType.ASC);
        dmFind.getHelper().addSort("CompanyName", DmSortType.ASC);
        dmFind.getHelper().addSort("BranchName", DmSortType.ASC);
        dmFind.getHelper().addSort("SName", DmSortType.ASC);
    }

    private void fIni() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "695px", "410px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Branch Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBranch));
        txtHBranch.setMandatory(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Regional Language");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRegionalLanguage));
        txtHBranch.setMandatory(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////// TR2 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Product Licensed To");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHProductLicenseKey));
        txtHProductLicenseKey.setMandatory(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////// TR3 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSiteName));
        txtSiteName.setMandatory(true);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////// TR4 //////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-Line-Horizontal");
        flwTd.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginBottom(3, Style.Unit.PX);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        ///////////////////////// TR5 //////////////////////////////////////////

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Display Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSiteDisplayName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Cilent Code");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtClientCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /////////////////////////// TR6 /////////////////////////////////////////
        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCode));
        txtCode.setMandatory(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Head Office");
        lblTemp.getElement().getStyle().setMarginLeft(18, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeadOffice));
        txtHHeadOffice.setMandatory(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Manual Code");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR6 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Description");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSiteDescription));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR7 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Address");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR8 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(vl.add(txtAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR9 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("PIN");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPinNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR10 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHState));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Country");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCountry));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /////////////////////////// TR11 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Registration Type {GST)");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRegistrationType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Gram");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGram));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR11 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPhone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("FAX No.");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFaxNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR12 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Notification No.");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNotificationNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR13 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("TIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTinNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTinNoDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR14 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGSTin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN Date");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGSTINDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR15 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("PAN");

        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPanNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("CIN");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCin));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /////////////////////////// TR15 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("IE Code");

        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIECode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("RBI Code");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRBICode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /////////////////////////// TR15 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Drug License No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDrugLicenseNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Serial Key");
        lblTemp.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSerialKey));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /////////////////////////// TR15 /////////////////////////////////////////

        return flwTable;
    }

}
