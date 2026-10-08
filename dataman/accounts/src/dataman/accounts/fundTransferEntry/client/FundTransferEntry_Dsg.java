package dataman.accounts.fundTransferEntry.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import dataman.accounts.fundTransferEntry.shared.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class FundTransferEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().FUND_TRANSFER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtEntryNo = new DmTextBox();
    final DmTextBox txtDtEntry = new DmTextBox();
    final DmTextBox txtCurrentSite = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 13, true);
    final DmTextBox txtChequeNo = new DmTextBox();
    final DmTextBox txtDtCheque = new DmTextBox();
    final DmTextBox txtDtClearing = new DmTextBox();
    final DmTextBox txtRemark = new DmTextBox();
    final DmTextBox txtTrnRemark = new DmTextBox();
    final DmTextBox txtToCostCenter = new DmTextBox();
    final DmTextBox txtTrnDrAccount = new DmTextBox();
    final DmTextBox txtTrnCrAccount = new DmTextBox();
    Button btnApprovedBy = new Button();
    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHFromCostCenter">
    interface TxtHFromCostCenter_PrpAcs extends KeyField_T_PrpAcs<TxtHFromCostCenter_Mdl> {

        @Editor.Path("Site")
        ValueProvider<TxtHFromCostCenter_Mdl, String> site();
    }
    final DmTextHelp<TxtHFromCostCenter_Mdl, TxtHFromCostCenter_PrpAcs> txtHFromCostCenter = new DmTextHelp(GWT.create(TxtHFromCostCenter_PrpAcs.class), 7);

    private void iniFromCostCentr() {
        txtHFromCostCenter.getDmGrid().addTextColumn(txtHFromCostCenter.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHFromCostCenter.getDmGrid().addTextColumn(txtHFromCostCenter.getDmGrid().getPropertyAccess().site(), "Site Name", 150, "Site_Code");
        txtHFromCostCenter.getDmGrid().initializeGrid(txtHFromCostCenter.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHFromCostCenter.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCrAccount">
    final DmTextHelp<TxtHAccounts_Mdl, TxtHAccount_PrpAcs> txtHCrAccount = new DmTextHelp(GWT.create(TxtHAccount_PrpAcs.class), 7);

    private void iniCrAccount() {
        txtHCrAccount.getDmGrid().addTextColumn(txtHCrAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCrAccount.getDmGrid().addTextColumn(txtHCrAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "ManualCode");
        txtHCrAccount.getDmGrid().addTextColumn(txtHCrAccount.getDmGrid().getPropertyAccess().cityName(), "City", 150, "CityName");
        txtHCrAccount.getDmGrid().addTextColumn(txtHCrAccount.getDmGrid().getPropertyAccess().nature(), "Nature", 75, "Nature");
        txtHCrAccount.getDmGrid().initializeGrid(txtHCrAccount.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        txtHCrAccount.getHelper().addSort("Name", DmSortType.ASC);
        txtHCrAccount.getHelper().addSort("ManualCode", DmSortType.ASC);
        txtHCrAccount.getHelper().addSort("CityName", DmSortType.ASC);
        txtHCrAccount.getHelper().addSort("Nature", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDrAccount">
    interface TxtHAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHAccounts_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccounts_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHAccounts_Mdl, String> cityName();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccounts_Mdl, String> nature();
    }
    final DmTextHelp<TxtHAccounts_Mdl, TxtHAccount_PrpAcs> txtHDrAccount = new DmTextHelp(GWT.create(TxtHAccount_PrpAcs.class), 7);

    private void iniDrAccount() {
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "ManualCode");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().cityName(), "City", 150, "CityName");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "natureName");
        txtHDrAccount.getDmGrid().initializeGrid(txtHDrAccount.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        txtHDrAccount.getHelper().addSort("Name", DmSortType.ASC);
        txtHDrAccount.getHelper().addSort("ManualCode", DmSortType.ASC);
        txtHDrAccount.getHelper().addSort("CityName", DmSortType.ASC);
        txtHDrAccount.getHelper().addSort("natureName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTransferSite">
    interface TxtHTransferSite_PrpAcs extends KeyField_T_PrpAcs<TxtHTransferSite_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHTransferSite_Mdl, String> city();

        @Editor.Path("AccountName")
        ValueProvider<TxtHTransferSite_Mdl, String> accountName();
    }
    final DmTextHelp<TxtHTransferSite_Mdl, TxtHTransferSite_PrpAcs> txtHTransferSite = new DmTextHelp(GWT.create(TxtHTransferSite_PrpAcs.class), 7);

    private void iniTransferSite() {
        txtHTransferSite.getDmGrid().addTextColumn(txtHTransferSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "SiteName");
        txtHTransferSite.getDmGrid().addTextColumn(txtHTransferSite.getDmGrid().getPropertyAccess().city(), "City", 75, "City");
        txtHTransferSite.getDmGrid().addTextColumn(txtHTransferSite.getDmGrid().getPropertyAccess().accountName(), "A/c Name", 125, "ACName");
        txtHTransferSite.getDmGrid().initializeGrid(txtHTransferSite.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHTransferSite.getHelper().addSort("SiteName", DmSortType.ASC);
        txtHTransferSite.getHelper().addSort("City", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VDate")
        ValueProvider<Find_Mdl, String> dtEntry();

        @Editor.Path("CostCenter")
        ValueProvider<Find_Mdl, String> costCenter();

        @Editor.Path("CrAccount")
        ValueProvider<Find_Mdl, String> crAccount();

        @Editor.Path("DrAccount")
        ValueProvider<Find_Mdl, String> drAccount();

        @Editor.Path("Amount")
        ValueProvider<Find_Mdl, String> amount();

        @Editor.Path("ChaqNo")
        ValueProvider<Find_Mdl, String> chaqNo();

        @Editor.Path("DtCheque")
        ValueProvider<Find_Mdl, String> dtCheque();

        @Editor.Path("DtClearing")
        ValueProvider<Find_Mdl, String> dtClearing();

        @Editor.Path("Narration")
        ValueProvider<Find_Mdl, String> remark();

        @Editor.Path("TrnCostCenter")
        ValueProvider<Find_Mdl, String> trnCostCenter();

        @Editor.Path("TrnCrAccount")
        ValueProvider<Find_Mdl, String> trnCrAccount();

        @Editor.Path("TrnDrAccount")
        ValueProvider<Find_Mdl, String> trnDrAccount();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> preparedBy();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();

        @Editor.Path("ModifiedBy")
        ValueProvider<Find_Mdl, String> modifiedBy();

        @Editor.Path("EntryDtTime")
        ValueProvider<Find_Mdl, String> entryDate();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 20, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry No.", 100, "Entry_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dtEntry(), "Entry Date", 80, "Entry_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().costCenter(), "From Cost Center", 150, "CostCenterName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().drAccount(), "Debit A/c", 150, "Debit_Ac");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().crAccount(), "Credit A/c", 150, "Credit_Ac");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amount(), "Amount", 150, "Amount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().chaqNo(), "Cheque No.", 150, "Chq_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dtCheque(), "Cheque Date", 80, "Chq_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dtClearing(), "Clearing Date", 80, "Clg_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().trnCostCenter(), "Transfer Cost Coenter", 150, "Trn_CostCenterName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 100, "PreparedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 100, "PostedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().modifiedBy(), "Modified By", 100, "ModifiedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 125, "entryDateTime", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 0);
        dmFind.getHelper().addSort("V_Date", DmSortType.ASC, "V_Date");
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC, "short_Name");
        dmFind.getHelper().addSort("V_No", DmSortType.ASC, "V_No");
    }
    //</editor-fold>

    public FundTransferEntry_Dsg() {
        ini();
    }

    private void ini() {
        Anchor aBtn;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        /*====================================< Find Grid & Help >====================================*/
        iniFind();
        iniFromCostCentr();
        iniDrAccount();
        iniCrAccount();
        iniTransferSite();
        /*====================================< **************** >====================================*/
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(createWorkingArea());
        vrtInnerForm.add(flwWorkingArea);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);

        userControl.addGap(100);
        userControl.addTextBlog(approvedByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel createWorkingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr;
        FlowPanel flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "320px", null, "relative", DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Entry No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        stl = flwTd.getElement().getStyle();
        //stl.setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Entry Date");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtDtEntry.setMandatory(true);
        txtDtEntry.setMaxLength(11);
        flwTd.add(vl.add(txtDtEntry));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Current Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtCurrentSite.setMandatory(true);
        flwTd.add(vl.add(txtCurrentSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Transfer To Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtHTransferSite.setMandatory(true);
        flwTd.add(vl.add(txtHTransferSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Cost Center");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFromCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Debit Account");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtHDrAccount.setMandatory(true);
        flwTd.add(vl.add(txtHDrAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Credit Account");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtHCrAccount.setMandatory(true);
        flwTd.add(vl.add(txtHCrAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Transfer Amount");
        lblTemp.setWidth("100%");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtAmount.setMandatory(true);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        stl = flwTd.getElement().getStyle();
        //stl.setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Cheque No.");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtChequeNo.setMaxLength(15);
        flwTd.add(vl.add(txtChequeNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Cheque Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtDtCheque.setMaxLength(11);
        flwTd.add(vl.add(txtDtCheque));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        stl = flwTd.getElement().getStyle();
        //stl.setMarginLeft(5, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Clearing Date");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtDtClearing.setMaxLength(11);
        flwTd.add(vl.add(txtDtClearing));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Remark");
        lblTemp.setWidth("100%");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtRemark.setMaxLength(100);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        stl = flwTr.getElement().getStyle();
        flwTr.setStyleName("dm-Strip");
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        stl.setTextAlign(Style.TextAlign.CENTER);
        stl.setMarginTop(2, Style.Unit.PCT);
        lblTemp = new DmLabel("In Transit Detail");
        lblTemp.setStyleName("dm-HLabel");
        lblTemp.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        flwTr.setStyleName("dm-StripLabel");
        flwTr.add(lblTemp);
        flwTable.add(flwTr);
        /*====================================< 9th Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Transfer To Cost Center");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtToCostCenter.getElement().getStyle().setProperty("disabled", "disabled");
        flwTd.add(vl.add(txtToCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Debit Account");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTrnDrAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 11th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Credit Account");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTrnCrAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 12th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Remark");
        lblTemp.setWidth("100%");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTrnRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/

        return flwTable;
    }
}
