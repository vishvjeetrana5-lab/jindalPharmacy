package dataman.utility.companyMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.companyMaster.shared.Find_Mdl;
import dataman.utility.companyMaster.shared.GrdCompanyMaster_Mdl;
import dataman.utility.companyMaster.shared.TxtHProductLicenseKey_Mdl;

public abstract class CompanyMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().COMPANY_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtCompanyCode = new DmTextBox(3, false);
    final DmTextBox txtShortName = new DmTextBox(9, false);
    final DmTextBox txtCurrentYear = new DmTextBox(9, true);
    final DmTextBox txtPreviousYear = new DmTextBox(9, true);
    final DmTextBox txtYearStartDate = new DmTextBox(15, true);
    final DmTextBox txtYearEndDate = new DmTextBox(15, true);
    final DmTextBoxNumeric txtVoucherPrefix = new DmTextBoxNumeric(0, false, 2, true);
    final DmTextBox txtDatabaseName = new DmTextBox(100, true);
    final DmTextBox txtFileDatabaseName = new DmTextBox(100, true);
    final DmTextBox txtMajorCurrency = new DmTextBox(10, false);
    final DmTextBox txtMinorCurrency = new DmTextBox(10, false);

    final Button btnRepairDb = new Button("Repair Db");

    //<editor-fold defaultstate="collapsed" desc="txtHProductLicenseKey">
    interface TxtHProductLicenseKey_PrpAcs extends KeyField_T_PrpAcs<TxtHProductLicenseKey_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHProductLicenseKey_Mdl, String> shortName();
    }
    final DmTextHelp<TxtHProductLicenseKey_Mdl, TxtHProductLicenseKey_PrpAcs> txtHProductLicenseKey = new DmTextHelp(GWT.create(TxtHProductLicenseKey_PrpAcs.class), 10, true);

    public void iniPLTHelp() {
        txtHProductLicenseKey.getDmGrid().addTextColumn(txtHProductLicenseKey.getDmGrid().getPropertyAccess().name(), "Name", 0, "Comp_Name");
        txtHProductLicenseKey.getDmGrid().addTextColumn(txtHProductLicenseKey.getDmGrid().getPropertyAccess().shortName(), "Short Name", 0, "SName");
        txtHProductLicenseKey.getDmGrid().initializeGrid(txtHProductLicenseKey.getDmGrid().getPropertyAccess().code(), 400, 0, 1); //244
        txtHProductLicenseKey.getHelper().addSort("Comp_Name", DmSortType.ASC);
        txtHProductLicenseKey.getHelper().addSort("SName", DmSortType.ASC);
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("YearStartDate")
        ValueProvider<Find_Mdl, String> yearStartDate();

        @Editor.Path("YearEndDate")
        ValueProvider<Find_Mdl, String> yearEndDate();

        @Editor.Path("CurrentFinancialYear")
        ValueProvider<Find_Mdl, String> currentFinancialYear();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 23);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().yearStartDate(), "Year Start Date", 150, "start_Dt");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().yearEndDate(), "Year End Date", 180, "end_Dt");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().currentFinancialYear(), "Financial Year", 150, "cYear");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 200, 1);//183
        dmFind.getHelper().addSort("Name", DmSortType.ASC, "Comp_Name");
        dmFind.getHelper().addSort("start_Dt", DmSortType.ASC);
        dmFind.getHelper().addSort("end_Dt", DmSortType.ASC);
        dmFind.getHelper().addSort("cYear", DmSortType.ASC);
    }

//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHLatFinancial">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLatFinancial = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHFinancial() {
        txtHLatFinancial.getDmGrid().addTextColumn(txtHLatFinancial.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHLatFinancial.getDmGrid().initializeGrid(txtHLatFinancial.getDmGrid().getPropertyAccess().code(), 75, 0, 1);

    }

    //</editor-fold>
    
    public CompanyMaster_Dsg() {
        ini();
        iniPLTHelp();
        iniFind();
        iniTxtHFinancial();
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
        vrtInnerForm.add(createGrid());

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addControlBlog("", btnRepairDb);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "900px", "125px", null, null, DmPosition.CENTER, true);
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        ///////////////////////// TR1 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Product Licensed To");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "83%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHProductLicenseKey));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ///////////////////////// TR2 //////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.66%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Company Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCompanyCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.66%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Short Name");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        ////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.66%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Major Currency");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMajorCurrency));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /////////////////////////// TR3 /////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.66%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Current Year");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCurrentYear));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.66%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Previous Year");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPreviousYear));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        ////////////////////////////////////////////////////
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.66%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Minor Currency");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMinorCurrency));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        ///////////////////////////////// TR 4 ////////////////////////////////////////////

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Year Start Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtYearStartDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Year End Date");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtYearEndDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ////////////////////////////////////TR 5//////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Voucher Prefix");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherPrefix));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Latest Financial Year");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLatFinancial));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        ////////////////////////////////////TR 6//////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Database Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDatabaseName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("File Database Name");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16.67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFileDatabaseName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    interface Grd_PrpAcs extends KeyField_T_PrpAcs<GrdCompanyMaster_Mdl> {

        @Editor.Path("CurrentYear")
        ValueProvider<GrdCompanyMaster_Mdl, String> currentYear();

        @Editor.Path("PreviousYear")
        ValueProvider<GrdCompanyMaster_Mdl, String> previousYear();

        @Editor.Path("YearStart")
        ValueProvider<GrdCompanyMaster_Mdl, String> yearStart();

        @Editor.Path("YearEnd")
        ValueProvider<GrdCompanyMaster_Mdl, String> yearEnd();

        @Editor.Path("VoucherPrefix")
        ValueProvider<GrdCompanyMaster_Mdl, String> voucherPrefix();

        @Editor.Path("DatabaseName")
        ValueProvider<GrdCompanyMaster_Mdl, String> databaseName();

        @Editor.Path("FileDatabaseName")
        ValueProvider<GrdCompanyMaster_Mdl, String> fileDatabaseName();
        
        @Editor.Path("LatestCompCode")
        ValueProvider<GrdCompanyMaster_Mdl, String> latestCompCode();

    }

    final DmGrid<GrdCompanyMaster_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    private FlowPanel createGrid() {
        FlowPanel flwTable = new FlowPanel();
        FlowPanel flwTr = new FlowPanel();
        FlowPanel flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.setStyleName("dm-StripLabel");
        DmLabel lblTemp = new DmLabel("Product To License Details");
        lblTemp.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Company Name", 125, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().currentYear(), "Current Year", 75, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().previousYear(), "Previous Year", 80, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().yearStart(), "Strat Date", 80, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().yearEnd(), "End Date", 80, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().voucherPrefix(), "Voucher Prefix", 90, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().databaseName(), "Database Name", 120, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().fileDatabaseName(), "File Db Name", 90, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().latestCompCode(),   "Lat.Fin.Year", 90, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().code(), 1280, 330, 2);

        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(dmGrd.getGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
