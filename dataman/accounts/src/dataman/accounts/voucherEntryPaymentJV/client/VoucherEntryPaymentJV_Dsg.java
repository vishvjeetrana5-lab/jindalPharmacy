package dataman.accounts.voucherEntryPaymentJV.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.accounts.voucherEntryPaymentJV.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class VoucherEntryPaymentJV_Dsg extends IsWidgetExtended {

    public String currentWidgetName = "";
    public String voucherCategory;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Posted By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtRefNo = new DmTextBox(30, false);
    final DmTextBox txtRefDate = new DmTextBox(11, false);
    final DmTextBox txtWTACode = new DmTextBox(50, false);

    final Button btnApprovedBy = new Button();
    final InlineLabel lblDebit = new InlineLabel("0");
    final InlineLabel lblCredit = new InlineLabel("0");
    final InlineLabel lblDiff_DebCre = new InlineLabel("0");
    final DmLabel lblLedgerBalance = new DmLabel("Dr 0.00");
    final RadioButton radioAll = new RadioButton("a", "All");
    final RadioButton radioPosted = new RadioButton("a", "Posted");
    final RadioButton radioUnPosted = new RadioButton("a", "Un-Posted");
    final InlineLabel lblPartyBalance = new InlineLabel("");
    final FlowPanel flwlblAccountName = new FlowPanel();
    final FlowPanel flwtxtAccountName = new FlowPanel();
    final FlowPanel flwlblDrCrAccountName = new FlowPanel();
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);

    //<editor-fold defaultstate="collapsed" desc="TxtHVoucherType">
    interface TxtHVoucherType extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHVoucherType> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucherType.class), 7, true);

    public void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCostCenter">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCostCenter = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7);

    public void iniTxtHCostCenter() {
        txtHCostCenter.getDmGrid().addTextColumn(txtHCostCenter.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHCostCenter.getDmGrid().initializeGrid(txtHCostCenter.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHCostCenter.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">
    interface TxtHAccountName extends KeyField_T_PrpAcs<TxtHAccountName_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHAccountName_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccountName_Mdl, String> manualCode();

        @Editor.Path("Nature")
        ValueProvider<TxtHAccountName_Mdl, String> nature();
    }
    final DmTextHelp<TxtHAccountName_Mdl, TxtHAccountName> txtHAccountName = new DmTextHelp(GWT.create(TxtHAccountName.class), 7, true);

    public void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "nature");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "manualCode");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHAccountName.getHelper().addSort("name", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("cityName", DmSortType.ASC);
        txtHAccountName.getHelper().addSort("nature", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("V_Date")
        ValueProvider<Find_Mdl, String> V_Date();

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> VoucherType();

        @Editor.Path("Account1")
        ValueProvider<Find_Mdl, String> Account1();

        @Editor.Path("Account2")
        ValueProvider<Find_Mdl, String> Account2();

        @Editor.Path("Debit")
        ValueProvider<Find_Mdl, String> Debit();

        @Editor.Path("Credit")
        ValueProvider<Find_Mdl, String> Credit();

        @Editor.Path("Naration")
        ValueProvider<Find_Mdl, String> Naration();

        @Editor.Path("PreparedBy")
        ValueProvider<Find_Mdl, String> PreparedBy();

        @Editor.Path("ModifiyedBy")
        ValueProvider<Find_Mdl, String> ModifiyedBy();

        @Editor.Path("PostedBy")
        ValueProvider<Find_Mdl, String> PostedBy();

        @Editor.Path("EntryDtTime")
        ValueProvider<Find_Mdl, String> entryDtTime();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18, true);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 120, "Voucher_No");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().VoucherType(), "Voucher Type", 100, "Voucher_Type");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().V_Date(), "Voucher Date", 80, "Voucher_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Account1(), "Account1", 150, "Account1");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Account2(), "Account2", 150, "Account2");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Debit(), "Debit", 150, "Debit").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Credit(), "Credit", 150, "Debit").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().Naration(), "Naration", 150, "Narration");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().PreparedBy(), "Prepared By", 100, "Prepared_By");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().ModifiyedBy(), "Modifiyed By", 100, "Modified_By");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().PostedBy(), "Posted By", 100, "Posted_By");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDtTime(), "Entry Date", 125, "entryDateTime", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0);
        dmFind.getHelper().addSort("V_Date", DmSortType.ASC);
        dmFind.getHelper().addSort("Voucher_Type", DmSortType.ASC);
        dmFind.getHelper().addSort("Voucher_No", DmSortType.DESC);

    }
    //</editor-fold>

    public VoucherEntryPaymentJV_Dsg() {
        ini();
        iniTxtHVoucherType();
        iniTxtHAccountName();
        iniTxtHCostCenter();
        iniFind();
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
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {
        Anchor aBtn;
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

        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "405px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "68px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "69px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(leftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "69px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(rightArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "290px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(createGrid()));

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(footerSection());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", null, null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "21px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "21px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(rightAreaTop()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "39px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwlblAccountName, "100%", "39px", "left", null, DmPosition.NONE, true);
        flwlblAccountName.add(vl.add(rightAreaBottom()));
        flwTd.add(flwlblAccountName);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel rightAreaTop() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "21px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reference No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);


        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("<Cost Center>");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCostCenter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel rightAreaBottom() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "39px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTable.add(flwTr);

        return flwTable;

    }

    private HorizontalPanel footerSection() {
        HorizontalPanel hrtPanel = new HorizontalPanel();
        FlowPanel flwTmp;
        InlineLabel lbl;

        hrtPanel.addStyleName("dm-StripLabel");
        flwTmp = new FlowPanel();
        radioAll.getElement().setAttribute("style", radioAll.getElement().getAttribute("style")
                + "; float: left; width: 40px;top:3px;position:relative;"
        );
        radioPosted.getElement().setAttribute("style", radioAll.getElement().getAttribute("style")
                + "; float: left; width: 65px;top:3px;position:relative;"
        );
        radioUnPosted.getElement().setAttribute("style", radioAll.getElement().getAttribute("style")
                + "; float: left; width: 90px;top:3px;position:relative;"
        );
        hrtPanel.add(radioAll);
        hrtPanel.add(radioPosted);
        hrtPanel.add(radioUnPosted);

        flwTmp = new FlowPanel();
        flwTmp.setWidth("150px");
        flwTmp.getElement().getStyle().setTop(6, Style.Unit.PX);
        flwTmp.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        lbl = new InlineLabel("");
        flwTmp.add(lbl);
        flwTmp.add(lblPartyBalance);
        lblPartyBalance.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        lblPartyBalance.getElement().getStyle().setTop(4, Style.Unit.PX);
        hrtPanel.add(flwTmp);
        hrtPanel.setCellWidth(lblPartyBalance, "100%");

        flwTmp = new FlowPanel();
        flwTmp.setWidth("235px");
        flwTmp.getElement().getStyle().setTop(6, Style.Unit.PX);
        flwTmp.getElement().getStyle().setLeft(45, Style.Unit.PX);
        flwTmp.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        lbl = new InlineLabel("Difference: ");
        flwTmp.add(lbl);
        flwTmp.add(lblDiff_DebCre);
        hrtPanel.add(flwTmp);

        flwTmp = new FlowPanel();
        flwTmp.setWidth("150px");
        flwTmp.getElement().getStyle().setTop(6, Style.Unit.PX);
        flwTmp.getElement().getStyle().setPosition(Style.Position.RELATIVE);

        lbl = new InlineLabel("Dr: ");
        flwTmp.add(lbl);
        flwTmp.add(lblDebit);
        hrtPanel.add(flwTmp);

        flwTmp = new FlowPanel();
        flwTmp.setWidth("150px");
        flwTmp.getElement().getStyle().setTop(6, Style.Unit.PX);
        flwTmp.getElement().getStyle().setPosition(Style.Position.RELATIVE);
        lbl = new InlineLabel("Cr: ");
        flwTmp.add(lbl);
        flwTmp.add(lblCredit);
        hrtPanel.add(flwTmp);

        return hrtPanel;
    }

    //<editor-fold defaultstate="collapsed" desc="grdAccountDetail">
    public static class GridIndex {

        public final static int manualCode = 1;
        public final static int acName = 2;
        public final static int narration = 3;
        public final static int debit = 4;
        public final static int credit = 5;
    }

    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdVoucherEntryPaymentJV_Mdl> {

        @Editor.Path("accMCode")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> accMCode();

        @Editor.Path("accName")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> accName();

        @Editor.Path("narration")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> narration();
        
        @Editor.Path("favourOf")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> favourOf();
        
        @Editor.Path("chqNo")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> chqNo();

        @Editor.Path("debit")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> debit();

        @Editor.Path("credit")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> credit();

        @Editor.Path("tds")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> tds();

        @Editor.Path("totalAmount")
        ValueProvider<GrdVoucherEntryPaymentJV_Mdl, String> totalAmount();
    }

    final DmGrid<GrdVoucherEntryPaymentJV_Mdl, Grd_PrpAcs> grdAccountDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        grdAccountDetail.addRowNumberColumn();
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().accMCode(), "A/c Code", 110, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().accName(), "A/c Name", 200, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().narration(), "Narration", 270, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().favourOf(), "Favour Of", 150, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().chqNo(), "Chq. No.", 80, null);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().debit(), "Debit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().credit(), "Credit", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().tds(), "TDS", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.addTextColumn(grdAccountDetail.getPropertyAccess().totalAmount(), "Total", 90, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAccountDetail.initializeGrid(grdAccountDetail.getPropertyAccess().fieldHashCode(), 1280, 290, 3);
        return grdAccountDetail.getGrid();
    }
    //</editor-fold>

}
