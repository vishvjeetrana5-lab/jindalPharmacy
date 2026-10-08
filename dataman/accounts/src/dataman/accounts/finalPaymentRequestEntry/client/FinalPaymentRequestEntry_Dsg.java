package dataman.accounts.finalPaymentRequestEntry.client;

import dataman.accounts.finalPaymentRequestEntry.shared.Find_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.GrdAdvPaymentDetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.GrdBillDetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.GrdFinalPaymentDetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.GrdGRNDetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.GrdPODetail_Mdl;
import dataman.accounts.finalPaymentRequestEntry.shared.TxtHSupplier_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

public abstract class FinalPaymentRequestEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().FINAL_PAYMENT_REQUEST_ENTRY.caption;
    final String moduleName = new lmAccounts().moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmUserControl userControl = new DmUserControl();
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    public static final String tabPODetail = "P.Order Details";
    public static final String tabGrnDetail = "GRN Details";
    public static final String tabBillDetail = "Bill Details";
    public static final String tabAdPayDetail = "Adv. Payment Details";
    public static final String tabFinalPayDetail = "Final Payment Details";
    final Button btnApprovedBy = new Button();
    final FlowPanel flwSummary = new FlowPanel();
    final FlowPanel flwPoDetail = new FlowPanel();
    final FlowPanel flwGrnDetail = new FlowPanel();
    final FlowPanel flwBillDetail = new FlowPanel();
    final FlowPanel flwAdvPaymentDetail = new FlowPanel();
    final FlowPanel flwFinalPayDetail = new FlowPanel();
    final FlowPanel flwPayBillSearch = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtVoucherType = new DmTextBox(20, false);
    final DmTextBox txtPayReqNo = new DmTextBox(20, true);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtPayBillNo = new DmTextBox(20, true);

    final DmTextBox txtPayBillDate = new DmTextBox(11, true);
    final DmTextBoxNumeric txtPBillAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBox txtSupplierName = new DmTextBox(30, true);
    final DmTextBox txtPayBillApprovedDate = new DmTextBox(11, false);
    final DmTextBox txtPayBillApprovedBy = new DmTextBox(30, false);
    final DmTextBox txtPayDueDate = new DmTextBox(11, false);
    final DmRemarkTextBox txtPayBillRemark = new DmRemarkTextBox(30, false);

    final DmTextBoxNumeric txtTotalPoValue = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalGrn = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalBillAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtNetBillEntry = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtPBillTDS = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalPoAdvance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtPoAdvanceNet = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtPOAdvanceTDS = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalBillPayment = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);

    final DmTextBoxNumeric txtFinalDuePay = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBox txtPaymnetDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(30, false);
    final Button btnPayBillSearch = new Button("...");

    final FlowPanel flwChk = new FlowPanel();
    final DmCheckBox chk = new DmCheckBox();
//
    final DmTextBoxNumeric txtPoAdvanceSetteled = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtPoAdvanceUnSetteled = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtPBillAdvanceSetteled = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);

//    final DmTextBoxNumeric txtAdvanceToBeAdjusted = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    //<editor-fold defaultstate="collapsed" desc="txtHModuleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModuleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    public void iniModuleType() {
        txtHModuleType.getDmGrid().addTextColumn(txtHModuleType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHModuleType.getDmGrid().initializeGrid(txtHModuleType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModuleType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplierName_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHSupplier_Mdl, TxtHSupplierName_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHSupplierName_PrpAcs.class), 7, true);

    private void iniSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("ModuleText")
        ValueProvider<Find_Mdl, String> moduleText();

        @Editor.Path("PbNo")
        ValueProvider<Find_Mdl, String> pbNo();

        @Editor.Path("SupplierText")
        ValueProvider<Find_Mdl, String> supplierText();

        @Editor.Path("FinalDuePay")
        ValueProvider<Find_Mdl, String> finalDuePay();

        @Editor.Path("PaymnetDt")
        ValueProvider<Find_Mdl, String> paymnetDt();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22, true);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Pay. Req. No.", 150, "payReqNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 80, "date", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().moduleText(), "Module", 150, "module");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierText(), "Supplier", 350, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().pbNo(), "PB. No.", 200, "pbNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().finalDuePay(), "Final Due Pay.", 150, "finDuePay").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().paymnetDt(), "Payment Dt", 80, "paymDt", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 500);
        dmFind.getHelper().addSort("date", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("V_No", DmEnum.DmSortType.ASC, "V_No");
        dmFind.getHelper().addSort("payReqNo", DmEnum.DmSortType.ASC, "payReqNo");
    }
//</editor-fold>

    public FinalPaymentRequestEntry_Dsg() {
        ini();
        iniFind();
        iniModuleType();
        iniSupplier();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "517px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; border-bottom:1px solid gray; margin: 1px 0px;");
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(billDetails());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "327px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; border-bottom:1px solid gray; margin: 1px 0px;");
        DmClient.setStyle(flwTr, "100%", "80px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(summaryArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(bottomArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel PoDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "294px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdPoDetail());
        return flwTable;
    }

    private FlowPanel GrnDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "294px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdGRNDetail());
        return flwTable;
    }

    private FlowPanel BillDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "294px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdBillDetail());
        return flwTable;
    }

    private FlowPanel AdvPaymentDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "294px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdAdvPaymentDetail());
        return flwTable;
    }

    private FlowPanel FinalPayDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "294px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdFinalPaymentDetail());
        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        int tabIndex = 0;
        DmClient.setStyle(tabPanel, "100%", "330px", null, null, DmEnum.DmPosition.NONE, true);

        tabPanel.add(flwBillDetail, tabBillDetail);
        tabIndex++;

        tabPanel.add(flwPoDetail, tabPODetail);
        tabIndex++;

        tabPanel.add(flwGrnDetail, tabGrnDetail);
        tabIndex++;

        tabPanel.add(flwAdvPaymentDetail, tabAdPayDetail);
        tabIndex++;

        tabPanel.add(flwFinalPayDetail, tabFinalPayDetail);
        tabIndex++;

        flwBillDetail.add(BillDetailTabArea());
        flwPoDetail.add(PoDetailTabArea());
        flwGrnDetail.add(GrnDetailTabArea());
        flwAdvPaymentDetail.add(AdvPaymentDetailTabArea());
        flwFinalPayDetail.add(FinalPayDetailTabArea());

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "30px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Pay. Req. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayReqNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Module");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHModuleType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel billDetails() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("PB. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        hrtPnl.add(txtPayBillNo);
        hrtPnl.setCellWidth(txtPayBillNo, "100%");
        DmClient.setStyle(flwPayBillSearch, "100%", "18px", "left", null, DmEnum.DmPosition.NONE, true);

        btnPayBillSearch.setWidth("18px");
        btnPayBillSearch.setHeight("20px");

        btnPayBillSearch.getElement().setAttribute("style", btnPayBillSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnPayBillSearch.addStyleName("dm-button-grad");
        btnPayBillSearch.setTitle("PB. Search");

        flwPayBillSearch.add(btnPayBillSearch);
        hrtPnl.add(flwPayBillSearch);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("P. Bill Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayBillDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPBillAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Pay Due Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayDueDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Approved Date");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayBillApprovedDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Approval By");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayBillApprovedBy));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayBillRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel summaryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "80px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Bill Amt.");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalBillAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total P.Bill TDS");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPBillTDS));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Net Bill Entry");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetBillEntry));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total P.O. Value");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalPoValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Advance");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalPoAdvance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Adv. Tds");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPOAdvanceTDS));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Advance Net");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPoAdvanceNet));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total P.Bill Paid");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalBillPayment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

//        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Advance Settled");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPoAdvanceSetteled));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Advance unsettled");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPoAdvanceUnSetteled));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Adv. Sett. Curr. Bill");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPBillAdvanceSetteled));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total GRN Against P.Bill");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalGrn));
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel bottomArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "30px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Final Due Payment");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtFinalDuePay));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwChk.getElement().setAttribute("style", "min-width:141px;padding:4px;height:13px;");
        chk.getElement().setAttribute("style", "margin-right:6px;margin-top:-1px");
        flwChk.add(chk);
        lbl = new DmLabel("Part Pay.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(-2, Style.Unit.PCT);
        flwChk.add(lbl);

        flwTd.add(flwChk);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Payment Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPaymnetDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdBillDetail">
    interface GridBillDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBillDetail_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdBillDetail_Mdl, String> amount();

        @Editor.Path("Qty")
        ValueProvider<GrdBillDetail_Mdl, String> qty();

        @Editor.Path("Rate")
        ValueProvider<GrdBillDetail_Mdl, String> rate();

    }
    final DmGrid<GrdBillDetail_Mdl, GridBillDetail_PrpAcs> grdBillDetail = new DmGrid(GWT.create(GridBillDetail_PrpAcs.class));

    private Grid iniGrdBillDetail() {
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().name(), "Item Name", 370, "itemName");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().qty(), "Qty", 190, "quantity").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().rate(), "Rate", 150, "rate").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().amount(), "Amount", 150, "amt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdBillDetail.initializeGrid(grdBillDetail.getPropertyAccess().fieldHashCode(), 1260, 294);
        return grdBillDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdPODetail">
    interface GridPoDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPODetail_Mdl> {

        @Editor.Path("Date")
        ValueProvider<GrdPODetail_Mdl, String> date();

        @Editor.Path("Supplier")
        ValueProvider<GrdPODetail_Mdl, String> supplier();

        @Editor.Path("Value")
        ValueProvider<GrdPODetail_Mdl, String> value();

        @Editor.Path("EntryBy")
        ValueProvider<GrdPODetail_Mdl, String> entryBy();

        @Editor.Path("ItemName")
        ValueProvider<GrdPODetail_Mdl, String> itemName();

    }
    final DmGrid<GrdPODetail_Mdl, GridPoDetail_PrpAcs> grdPODetail = new DmGrid(GWT.create(GridPoDetail_PrpAcs.class));

    private Grid iniGrdPoDetail() {
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().date(), "Date", 80, "date");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().name(), "Order No.", 100, "orderNo");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().supplier(), "Supplier", 250, "supplier");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().itemName(), "Item", 300, "itemName");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().value(), "Value", 150, "value").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().entryBy(), "Entry By", 150, "entryBy");
        grdPODetail.initializeGrid(grdPODetail.getPropertyAccess().fieldHashCode(), 1260, 294);
        return grdPODetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdMRNDetail">
    interface GridMRNDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGRNDetail_Mdl> {

        @Editor.Path("GrnNo")
        ValueProvider<GrdGRNDetail_Mdl, String> grnNo();

        @Editor.Path("ChallanNo")
        ValueProvider<GrdGRNDetail_Mdl, String> challanNo();

        @Editor.Path("ChallanDate")
        ValueProvider<GrdGRNDetail_Mdl, String> challanDate();

        @Editor.Path("Value")
        ValueProvider<GrdGRNDetail_Mdl, String> value();

        @Editor.Path("Status")
        ValueProvider<GrdGRNDetail_Mdl, String> status();

        @Editor.Path("EntryBy")
        ValueProvider<GrdGRNDetail_Mdl, String> entryBy();

        @Editor.Path("ItemName")
        ValueProvider<GrdGRNDetail_Mdl, String> itemName();
    }
    final DmGrid<GrdGRNDetail_Mdl, GridMRNDetail_PrpAcs> grdMRNDetail = new DmGrid(GWT.create(GridMRNDetail_PrpAcs.class));

    private Grid iniGrdGRNDetail() {
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().name(), "GRN Date", 80, "grnDate");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().grnNo(), "GRN No.", 120, "grnNo");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().challanNo(), "Challan No.", 120, "pChallanNo");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().challanDate(), "Challan Date", 80, "pChallanDate");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().itemName(), "Item", 280, "itemName");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().value(), "Value", 100, "value").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().status(), "Status", 70, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().entryBy(), "Entered By", 100, "preparedBy");
        grdMRNDetail.initializeGrid(grdMRNDetail.getPropertyAccess().fieldHashCode(), 1260, 294);
        return grdMRNDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdAdvPaymnetDetail">
    interface GridAdvPaymnetDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAdvPaymentDetail_Mdl> {

        @Editor.Path("No")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> no();

        @Editor.Path("Mode")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> mode();

        @Editor.Path("Bank")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> bank();

        @Editor.Path("ChqDate")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> chqDate();

        @Editor.Path("ChqNo")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> chqNo();

        @Editor.Path("Amt")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> amt();

        @Editor.Path("TdsDeduction")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> tdsDeduction();

        @Editor.Path("NetPay")
        ValueProvider<GrdAdvPaymentDetail_Mdl, String> netPay();
    }
    final DmGrid<GrdAdvPaymentDetail_Mdl, GridAdvPaymnetDetail_PrpAcs> grdAdvPaymnetDetail = new DmGrid(GWT.create(GridAdvPaymnetDetail_PrpAcs.class));

    private Grid iniGrdAdvPaymentDetail() {
        grdAdvPaymnetDetail.addRowNumberColumn();
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().name(), "Date", 80, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().no(), "No.", 100, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().mode(), "Mode", 100, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().bank(), "Bank", 300, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().chqDate(), "Ins. Date", 80, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().chqNo(), "Ins. No.", 200, "");
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().amt(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().tdsDeduction(), "Tds", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdvPaymnetDetail.addTextColumn(grdAdvPaymnetDetail.getPropertyAccess().netPay(), "Net Pay", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdAdvPaymnetDetail.initializeGrid(grdAdvPaymnetDetail.getPropertyAccess().fieldHashCode(), 1260, 294);
        return grdAdvPaymnetDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdFinalPaymnetDetail">
    interface GridFinalPaymnetDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdFinalPaymentDetail_Mdl> {

        @Editor.Path("No")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> no();

        @Editor.Path("Mode")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> mode();

        @Editor.Path("Bank")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> bank();

        @Editor.Path("ChqDate")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> chqDate();

        @Editor.Path("ChqNo")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> chqNo();

        @Editor.Path("Amt")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> amt();

        @Editor.Path("TdsDeduction")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> tdsDeduction();

        @Editor.Path("NetPay")
        ValueProvider<GrdFinalPaymentDetail_Mdl, String> netPay();

    }
    final DmGrid<GrdFinalPaymentDetail_Mdl, GridFinalPaymnetDetail_PrpAcs> grdFinalPaymnetDetail = new DmGrid(GWT.create(GridFinalPaymnetDetail_PrpAcs.class));

    private Grid iniGrdFinalPaymentDetail() {
        grdFinalPaymnetDetail.addRowNumberColumn();
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().name(), "Date", 80, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().no(), "No.", 100, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().mode(), "Mode", 100, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().bank(), "Bank", 300, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().chqDate(), "Ins. Date", 80, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().chqNo(), "Ins. No.", 200, "");
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().amt(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().tdsDeduction(), "Tds", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdFinalPaymnetDetail.addTextColumn(grdFinalPaymnetDetail.getPropertyAccess().netPay(), "Net Pay", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdFinalPaymnetDetail.initializeGrid(grdFinalPaymnetDetail.getPropertyAccess().fieldHashCode(), 1260, 294);
        return grdFinalPaymnetDetail.getGrid();
    }
    //</editor-fold>
}
