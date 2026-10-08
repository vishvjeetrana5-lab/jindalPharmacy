package dataman.accounts.accountOpeningEntry.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.accounts.accountOpeningEntry.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;

abstract class AccountOpeningEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().ACCOUNT_OPENING.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Posted By";

    final DmUserControl userControl = new DmUserControl();

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmTextBox txtRefNo = new DmTextBox(10, false);
    final DmTextBox txtRefDate = new DmTextBox(11, false);
    final DmTextBox txtVoucherNo = new DmTextBox(10, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBoxNumeric txtDebit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 15, false);
    final DmTextBoxNumeric txtCredit = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 15, false);
    final DmTextBox txtChequeNo = new DmTextBox(20, false);
    final DmTextBox txtChequeDate = new DmTextBox(11, false);
    final DmRemarkTextBox txtNarration = new DmRemarkTextBox(100, false);
    final DmRemarkTextBox txtFavourOf = new DmRemarkTextBox(100, false);
    final DmTextBox txtClearDate = new DmTextBox(11, false);
    final DmTextBox txtReconcilationRemark = new DmTextBox(100, false);
    Button btnApprovedBy = new Button();
    Style stl;

    //<editor-fold defaultstate="collapsed" desc="txtHAccountName">
    interface TxtHAccountName_PrpAcs extends KeyField_T_PrpAcs<TxtHAccountName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccountName_Mdl, String> manualCode();

        @Editor.Path("GroupName")
        ValueProvider<TxtHAccountName_Mdl, String> groupName();

        @Editor.Path("CityName")
        ValueProvider<TxtHAccountName_Mdl, String> cityName();

        @Editor.Path("Address1")
        ValueProvider<TxtHAccountName_Mdl, String> address1();

        @Editor.Path("Address2")
        ValueProvider<TxtHAccountName_Mdl, String> address2();
    }

    final DmTextHelp<TxtHAccountName_Mdl, TxtHAccountName_PrpAcs> txtHAccountName = new DmTextHelp(GWT.create(TxtHAccountName_PrpAcs.class), 7, true);

    public void iniAccountName() {

        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Account Name", 0, "PartyName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 100, "Code");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().groupName(), "Group Name", 200, "GroupName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().cityName(), "City Name", 100, "CityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().address1(), "Address-1", 100, "Add1");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().address2(), "Address-2", 100, "Add2");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 800, 0, 1);
        txtHAccountName.getHelper().addSort("PartyName", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("Code", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("GroupName", DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true);

    public void iniVoucherType() {

        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("Description", DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenter">
    interface TxtHCostCenter_PrpAcs extends KeyField_T_PrpAcs<TxtHCostCenter_Mdl> {

        @Editor.Path("MaualCode")
        ValueProvider<TxtHCostCenter_Mdl, String> maualCode();

        @Editor.Path("Site")
        ValueProvider<TxtHCostCenter_Mdl, String> site();
    }
    final DmTextHelp<TxtHCostCenter_Mdl, TxtHCostCenter_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(TxtHCostCenter_PrpAcs.class), 7);

    public void iniCostCenter() {

        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "CostCenter Name", 0, "Name");
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().maualCode(), "Code", 100, "ManualCode");
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().site(), "Site", 150, "Site_Code");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHCostCenter.getHelper().addSort("Name", DmSortType.ASC);
        txtHCostCenter.getHelper().addSort("ManualCode", DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyField_T_PrpAcs<GrdAccountOpeningEntry_Mdl> {

        @Editor.Path("VType")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> vType();

        @Editor.Path("Date")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> date();

        @Editor.Path("AccountName")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> accountName();

        @Editor.Path("CostCenterName")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> costCenterName();

        @Editor.Path("Narration")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> narration();

        @Editor.Path("RefNo")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> refDate();

        @Editor.Path("Debit")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> credit();

        @Editor.Path("ChqNo")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> chqNo();

        @Editor.Path("ChqDate")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> chqDate();

        @Editor.Path("FavourOf")
        ValueProvider<GrdAccountOpeningEntry_Mdl, String> favourOf();

    }
    final DmGrid<GrdAccountOpeningEntry_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrid() {
        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().vType(), "V. Type", 50, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Vr. No.", 90, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().date(), "Vr. Date", 80, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().refNo(), "Ref. No.", 100, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().refDate(), "Ref. Date", 80, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().chqNo(), "Chq.", 80, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().chqDate(), "Chq. Date", 80, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().narration(), "Narration", 100, null);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().debit(), "Debit", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().credit(), "Credit", 80, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().costCenterName(), "Cost Center", 100, null);
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().code(), 1260, 370, 2);

        return dmGrd.getGrid();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("AccountName")
        ValueProvider<Find_Mdl, String> accountName();

        @Editor.Path("VType")
        ValueProvider<Find_Mdl, String> vType();

        @Editor.Path("VoucherTypeName")
        ValueProvider<Find_Mdl, String> voucherTypeName();

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("CostCenterName")
        ValueProvider<Find_Mdl, String> costCenterName();

        @Editor.Path("RefNo")
        ValueProvider<Find_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<Find_Mdl, String> refDate();

        @Editor.Path("Narration")
        ValueProvider<Find_Mdl, String> narration();

        @Editor.Path("Debit")
        ValueProvider<Find_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<Find_Mdl, String> credit();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> preparedBy();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();

        @Editor.Path("ModifiedBy")
        ValueProvider<Find_Mdl, String> modifiedBy();

        @Editor.Path("EntryDtTime")
        ValueProvider<Find_Mdl, String> entryDate();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22, true);

    public void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 150, "Voucher_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accountName(), "Account Name", 200, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Voucher Date", 80, "Voucher_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().costCenterName(), "Cost Center", 100, "Cost_Center");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refNo(), "Ref No.", 150, "Ref_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refDate(), "Ref Date", 100, "Ref_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().narration(), "Narratioin", 150, "Narration");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().debit(), "Debit", 100, "Debit").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().credit(), "Credit", 100, "Credit").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().preparedBy(), "Prepared By", 100, "PreparedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 100, "PostedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().modifiedBy(), "Modified By", 100, "ModifiedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 125, "entryDateTime", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 500, 0);
        dmFind.getHelper().addSort("Voucher_Date", DmSortType.ASC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC, "short_Name");
        dmFind.getHelper().addSort("V_No", DmSortType.ASC, "V_No");
        dmFind.getHelper().addSort("recID", DmSortType.ASC, "recID");
    }
//</editor-fold>

    public AccountOpeningEntry_Dsg() {
        ini();
    }

    private void ini() {
        Anchor aBtn;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniAccountName();
        iniCostCenter();
        iniVoucherType();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
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

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "495px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "105px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "105px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "105px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightSection());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "371px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTr.add(iniGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        /*====================================< Left Section >====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "105px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Vr No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));

        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Account Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 4th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("<Cost Center>");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 5th Row >====================================*/

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Narration");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNarration));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< ************ >====================================*/

        return flwTable;
    }

    private FlowPanel rightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        /*====================================< Right Section>====================================*/
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "105px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Debit");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDebit));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Credit");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCredit));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cheque No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChequeNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();

        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Cheque Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtChequeDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Favour Of");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFavourOf));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Clear Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtClearDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtReconcilationRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
