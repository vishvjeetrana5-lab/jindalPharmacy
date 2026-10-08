package dataman.accounts.fundInTransitEntry.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.accounts.fundInTransitEntry.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class FundInTransitEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().FUND_IN_TRANSIT.caption;
        final String moduleName = new lmAccounts().moduleName;

    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";

    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtEntryNo = new DmTextBox();
    final DmTextBox txtDtEntry = new DmTextBox();
    final DmTextBox txtCurrentSite = new DmTextBox();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 13, true);
    final DmTextBox txtChequeNo = new DmTextBox();
    final DmTextBox txtDtCheque = new DmTextBox();
    final DmTextBox txtDtClearing = new DmTextBox();
    final DmTextBox txtRemark = new DmTextBox();
    final DmTextBox txtTrnSite = new DmTextBox();
    final DmTextBox txtTrnCostCenter = new DmTextBox();
    final DmTextBox txtTrnDrAccount = new DmTextBox();
    final DmTextBox txtTrnCrAccount = new DmTextBox();
    final DmTextBox txtTrnRemark = new DmTextBox();
    Button btnApprovedBy = new Button();
    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHAccount">
    interface TxtHAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHAccounts_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccounts_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHAccounts_Mdl, String> cityName();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccounts_Mdl, String> nature();
    }
    final DmTextHelp<TxtHAccounts_Mdl, TxtHAccount_PrpAcs> txtHDrAccount = new DmTextHelp(GWT.create(TxtHAccount_PrpAcs.class), 7);
    final DmTextHelp<TxtHAccounts_Mdl, TxtHAccount_PrpAcs> txtHCrAccount = new DmTextHelp(GWT.create(TxtHAccount_PrpAcs.class), 7);

    private void iniDrAccount() {
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "ManualCode");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().cityName(), "City", 150, "CityName");
        txtHDrAccount.getDmGrid().addTextColumn(txtHDrAccount.getDmGrid().getPropertyAccess().nature(), "Nature", 75, "Nature");
        txtHDrAccount.getDmGrid().initializeGrid(txtHDrAccount.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        txtHDrAccount.getHelper().addSort("Name", DmSortType.ASC);
        txtHDrAccount.getHelper().addSort("ManualCode", DmSortType.ASC);
        txtHDrAccount.getHelper().addSort("CityName", DmSortType.ASC);
        txtHDrAccount.getHelper().addSort("Nature", DmSortType.ASC);
    }

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

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenter">
    interface TxtHCostCenter_PrpAcs extends KeyField_T_PrpAcs<TxtHCostCenter_Mdl> {

        @Editor.Path("Site")
        ValueProvider<TxtHCostCenter_Mdl, String> site();
    }
    final DmTextHelp<TxtHCostCenter_Mdl, TxtHCostCenter_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(TxtHCostCenter_PrpAcs.class), 7);

    private void iniCostCenter() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().site(), "Site Name", 150, "Site_Code");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHCostCenter.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTransferNo">
    interface TxtHTransferNo_PrpAcs extends KeyField_T_PrpAcs<TxtHTransferNo_Mdl> {

        @Editor.Path("VType")
        ValueProvider<TxtHTransferNo_Mdl, String> type();

        @Editor.Path("VDate")
        ValueProvider<TxtHTransferNo_Mdl, String> entryDate();

        @Editor.Path("SiteCode")
        ValueProvider<TxtHTransferNo_Mdl, String> siteCode();

        @Editor.Path("SName")
        ValueProvider<TxtHTransferNo_Mdl, String> sName();

        @Editor.Path("Amount")
        ValueProvider<TxtHTransferNo_Mdl, String> amount();

        @Editor.Path("SubName")
        ValueProvider<TxtHTransferNo_Mdl, String> crAccount();

        @Editor.Path("ContraName")
        ValueProvider<TxtHTransferNo_Mdl, String> drAccount();
    }
    final DmTextHelp<TxtHTransferNo_Mdl, TxtHTransferNo_PrpAcs> txtHTransferNo = new DmTextHelp(GWT.create(TxtHTransferNo_PrpAcs.class), 7);

    private void iniTxtHTransferNo() {
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().name(), "Entry No.", 0, "RecId");
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().type(), "Type", 75, "V_Type");
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 75, "V_Date");
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().sName(), "Name", 100, "SName");
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().amount(), "Amount", 75, "Amount");
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().crAccount(), "Credit A/c", 100, "SubName");
        txtHTransferNo.getDmGrid().addTextColumn(txtHTransferNo.getDmGrid().getPropertyAccess().drAccount(), "Debit A/c", 100, "ContraName");
        txtHTransferNo.getDmGrid().initializeGrid(txtHTransferNo.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        txtHTransferNo.getHelper().addSort("V_Date", DmSortType.ASC);
        txtHTransferNo.getHelper().addSort("RecId", DmSortType.ASC);
        txtHTransferNo.getHelper().addSort("SName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VDate")
        ValueProvider<Find_Mdl, String> dtEntry();

        @Editor.Path("TrnSite")
        ValueProvider<Find_Mdl, String> currentSiteName();

        @Editor.Path("CostCenter")
        ValueProvider<Find_Mdl, String> costCenter();

        @Editor.Path("CrParty")
        ValueProvider<Find_Mdl, String> crParty();

        @Editor.Path("DrParty")
        ValueProvider<Find_Mdl, String> DrParty();

        @Editor.Path("AmtCr")
        ValueProvider<Find_Mdl, String> amount();

        @Editor.Path("Narration")
        ValueProvider<Find_Mdl, String> remark();

        @Editor.Path("TrnCrParty")
        ValueProvider<Find_Mdl, String> trnCostCenter();

        @Editor.Path("TrnCrParty")
        ValueProvider<Find_Mdl, String> trnCrParty();

        @Editor.Path("TrnDrParty")
        ValueProvider<Find_Mdl, String> trnDrParty();

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
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry No.", 100, "DocId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dtEntry(), "Entry Date", 75, "Entry_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().currentSiteName(), "Current Site", 150, "Trn_SiteName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().costCenter(), "Cost Center", 150, "CostCenterName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().crParty(), "Credit A/c", 150, "Credit_Ac");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().DrParty(), "Debit A/c", 150, "Debit_Ac");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amount(), "Amount", 150, "Amount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().trnCostCenter(), "Transfer Cost Center", 150, "Trn_Cost_Center_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().trnCrParty(), "Transfer Credit A/c", 150, "Trn_Credit_Ac");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().trnDrParty(), "Transfer Debit A/c", 150, "Trn_Debit_Ac");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 100, "PreparedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 100, "PostedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().modifiedBy(), "Modified By", 100, "U_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 125, "entryDateTime", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 0);
        dmFind.getHelper().addSort("V_Date", DmSortType.ASC, "Voucher_Date");
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC, "sName");
        dmFind.getHelper().addSort("V_No", DmSortType.ASC, "VNo");
    }
    //</editor-fold>

    public FundInTransitEntry_Dsg() {
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
        iniTxtHTransferNo();
        iniDrAccount();
        iniCrAccount();
        iniCostCenter();
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
        DmClient.setStyle(flwTable, "50%", "330px", null, "relative", DmPosition.CENTER, true);
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
        //stl.setMarginLeft(2, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Entry Date");
        lblTemp.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtDtEntry.setMaxLength(11);
        txtDtEntry.setMandatory(true);
        flwTd.add(vl.add(txtDtEntry));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Transfer No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtHTransferNo.setMandatory(true);
        flwTd.add(vl.add(txtHTransferNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
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
        lblTemp = new DmLabel("Cost Center");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCostCenter));
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
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtAmount.setMandatory(true);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        stl = flwTd.getElement().getStyle();
        //stl.setMarginLeft(2, Style.Unit.PCT);
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
        //stl.setMarginLeft(2, Style.Unit.PCT);
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
        lblTemp = new DmLabel("Transfer Detail");
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
        lblTemp = new DmLabel("Transfer From Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTrnSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 10th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Transfer From Cost Center");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTrnCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******** >====================================*/
 /*====================================< 11th Row >====================================*/
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
 /*====================================< 12th Row >====================================*/
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
 /*====================================< 13th Row >====================================*/
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
        /*====================================< ********* >====================================*/

        return flwTable;
    }
}
