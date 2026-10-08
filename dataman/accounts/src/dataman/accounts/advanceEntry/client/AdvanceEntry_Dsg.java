package dataman.accounts.advanceEntry.client;

import dataman.accounts.advanceEntry.shared.Find_Mdl;
import dataman.accounts.advanceEntry.shared.GrdAdvPaymentDetail_Mdl;
import dataman.accounts.advanceEntry.shared.GrdBillDetail_Mdl;
import dataman.accounts.advanceEntry.shared.GrdFinalPaymentDetail_Mdl;
import dataman.accounts.advanceEntry.shared.GrdGRNDetail_Mdl;
import dataman.accounts.advanceEntry.shared.GrdPODetail_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmControlBlog;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

public abstract class AdvanceEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().ADVANCE_ENTRY.caption;
    final String moduleName = new lmAccounts().moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";
    public final static String deleteFileBlog = "Delete Files";
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
    final FlowPanel flwGUI = new FlowPanel();
    final FlowPanel flwAdvReqSearch = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtVoucherType = new DmTextBox(20, false);
    final DmTextBox txtModuleType = new DmTextBox(20, false);
    final DmTextBox txtEntryNo = new DmTextBox(20, true);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtAdvReqNo = new DmTextBox(20, true);
    final DmTextBox txtPurchaseOrder = new DmTextBox(20, false);
    final DmTextBox txtPurchaseOrderDate = new DmTextBox(11, false);
    final DmTextBox txtSupplierName = new DmTextBox(30, false);
    final DmTextBox txtSupplierDisplayName = new DmTextBox(100, true);

    final DmTextBoxNumeric txtAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);

    final DmTextBoxNumeric txtTotalPoValue = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalMrn = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalBill = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalAdvance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtTotalBillPay = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtAdvanceReqAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBoxNumeric txtPartyLedBal = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBoxNumeric txtBankCashLedBal = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBoxNumeric txtTotalTaxableAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);

    final DmTextBoxNumeric txtPaidAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBox txtDueDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(30, false);
    final DmTextBoxNumeric txtOriginalAmt = new DmTextBoxNumeric(2, true, 12, true);
    final DmTextBoxNumeric txtTotalTds = new DmTextBoxNumeric(2, true, 12, false);
    DmControlBlog paymentCBlog;
    final Button btnAdReqSearch = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="TxtHAccount">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccount = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHAccount() {
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHAccount.getDmGrid().initializeGrid(txtHAccount.getDmGrid().getPropertyAccess().code(), 325, 0, 1);
        txtHAccount.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHAccount.getHelper().addSort("nature", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ReqDate")
        ValueProvider<Find_Mdl, String> reqDate();

        @Editor.Path("ReqNo")
        ValueProvider<Find_Mdl, String> reqNo();

        @Editor.Path("SupplierName")
        ValueProvider<Find_Mdl, String> supplierName();
        
        @Editor.Path("PoNoText")
        ValueProvider<Find_Mdl, String> poNoText();

        @Editor.Path("AdvanceReqAmt")
        ValueProvider<Find_Mdl, String> advanceReqAmt();

        @Editor.Path("PaidAmt")
        ValueProvider<Find_Mdl, String> paidAmt();

        @Editor.Path("AdvanceAmt")
        ValueProvider<Find_Mdl, String> advanceAmt();

        @Editor.Path("TdsDeduction")
        ValueProvider<Find_Mdl, String> tdsDeduction();

        @Editor.Path("PayMode")
        ValueProvider<Find_Mdl, String> payMode();

        @Editor.Path("BankName")
        ValueProvider<Find_Mdl, String> bankName();

        @Editor.Path("ApprovedBy")
        ValueProvider<Find_Mdl, String> approvedBy();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22, true);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Entry. No.", 150, "adReqNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().reqDate(), "Entry. Date", 200, "v_date", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 150, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().reqNo(), "Req. No.", 100, "adRecId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().poNoText(), "PO. NO.", 100, "poNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().advanceReqAmt(), "Advance Req. Amt.", 100, "advanceReqAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().advanceAmt(), "Advance Amt.", 100, "advanceAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tdsDeduction(), "Tds", 100, "tdsDeduction").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().paidAmt(), "Paid Amt.", 100, "netPay").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().payMode(), "Mode", 100, "cardTypeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().bankName(), "Bank Name", 200, "acName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedBy(), "Approved By", 100, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1360, 500, 0);
        dmFind.getHelper().addSort("v_date", DmEnum.DmSortType.DESC, "v_date");
        dmFind.getHelper().addSort("V_No", DmEnum.DmSortType.ASC, "V_No");
        dmFind.getHelper().addSort("adReqNo", DmEnum.DmSortType.ASC, "adReqNo");
        dmFind.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<Find_Mdl>() {
            @Override
            public String getColStyle(Find_Mdl model, ValueProvider<? super Find_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (!DmShared.xNull(model.getApprovedBy()).equalsIgnoreCase("")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(Find_Mdl model, int rowIndex) {
                return "";
            }
        });
    }
//</editor-fold>

    public AdvanceEntry_Dsg() {
        ini();
        iniFind();
        iniTxtHAccount();
    }

    private void ini() {
        Button button;
        Anchor aBtn;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGap(80);

        button = new Button("Payment Mode");
        button.addStyleName("dm-button-grad");
        button.setWidth("100Px");
        paymentCBlog = userControl.addGeneralBlog(button);

        userControl.addTextBlog(approvedByBlog, 100, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.setTitle("Approved By");
        btnApprovedBy.addStyleName("dm-top-control-btnStyle");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);

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
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "377px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; solid gray; margin: 1px 0px;");
        DmClient.setStyle(flwTr, "100%", "90px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(summaryArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel PoDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "347px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdPoDetail());
        return flwTable;
    }

    private FlowPanel GrnDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "347px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdGRNDetail());
        return flwTable;
    }

    private FlowPanel BillDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "347px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdBillDetail());
        return flwTable;
    }

    private FlowPanel AdvPaymentDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "347px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdAdvPaymentDetail());
        return flwTable;
    }

    private FlowPanel FinalPayDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "347px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdFinalPaymentDetail());
        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        int tabIndex = 0;
        DmClient.setStyle(tabPanel, "100%", "380px", null, null, DmEnum.DmPosition.NONE, true);

        tabPanel.add(flwPoDetail, tabPODetail);
        tabIndex++;

        tabPanel.add(flwGrnDetail, tabGrnDetail);
        tabIndex++;

        tabPanel.add(flwBillDetail, tabBillDetail);
        tabIndex++;

        tabPanel.add(flwAdvPaymentDetail, tabAdPayDetail);
        tabIndex++;

        tabPanel.add(flwFinalPayDetail, tabFinalPayDetail);
        tabIndex++;

        flwPoDetail.add(PoDetailTabArea());
        flwGrnDetail.add(GrnDetailTabArea());
        flwBillDetail.add(BillDetailTabArea());
        flwAdvPaymentDetail.add(AdvPaymentDetailTabArea());
        flwFinalPayDetail.add(FinalPayDetailTabArea());

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "50px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Entry. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Ent. Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Adv. Req. NO.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        hrtPnl.add(txtAdvReqNo);
        hrtPnl.setCellWidth(txtAdvReqNo, "100%");
        DmClient.setStyle(flwAdvReqSearch, "100%", "20px", "left", null, DmEnum.DmPosition.NONE, true);

        btnAdReqSearch.setWidth("20px");
        btnAdReqSearch.setHeight("20px");

        btnAdReqSearch.getElement().setAttribute("style", btnAdReqSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAdReqSearch.addStyleName("dm-button-grad");
        btnAdReqSearch.setTitle("Patient Search");

        flwAdvReqSearch.add(btnAdReqSearch);
        hrtPnl.add(flwAdvReqSearch);
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Module");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtModuleType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("P.Order No.");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurchaseOrder));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("P.Order Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPurchaseOrderDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Supplier Name");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel summaryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "85px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total P.O. Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalPoValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total GRN Against P.O");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalMrn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Bill Against P.O.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalBill));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Advance Paid");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalAdvance));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //////////////////////////////////////////////2ndRow////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Bill Payment");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalBillPay));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Advance Req.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdvanceReqAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Party Ledger Bal.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPartyLedBal));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Bank/Cash Ledger Bal.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtBankCashLedBal);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //////////////////////////////////////////////3rdRow////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Due Date");

        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDueDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Advance Amt");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtOriginalAmt);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Net Pay");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPaidAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Tds Deduction");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtTotalTds);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Taxable Amt.");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtTotalTaxableAmt);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdPODetail">
    interface GridPoDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPODetail_Mdl> {

        @Editor.Path("Amt")
        ValueProvider<GrdPODetail_Mdl, String> amt();

        @Editor.Path("Unit")
        ValueProvider<GrdPODetail_Mdl, String> unit();

        @Editor.Path("Qty")
        ValueProvider<GrdPODetail_Mdl, String> qty();

        @Editor.Path("Rate")
        ValueProvider<GrdPODetail_Mdl, String> rate();
        
        @Editor.Path("TaxableAmt")
        ValueProvider<GrdPODetail_Mdl, String> taxableAmt();

        @Editor.Path("TaxAmt")
        ValueProvider<GrdPODetail_Mdl, String> taxAmt();
    }
    final DmGrid<GrdPODetail_Mdl, GridPoDetail_PrpAcs> grdPODetail = new DmGrid(GWT.create(GridPoDetail_PrpAcs.class));

    private Grid iniGrdPoDetail() {
        grdPODetail.addRowNumberColumn();
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().name(), "Item Name", 370, "");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().unit(), "Unit", 80, "");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().qty(), "Qty", 190, "");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().rate(), "Rate", 150, "");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().taxableAmt(), "Taxable Amt.", 150, "");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().taxAmt(), "Tax Amt", 150, "");
        grdPODetail.addTextColumn(grdPODetail.getPropertyAccess().amt(), "Amount", 150, "");
        grdPODetail.initializeGrid(grdPODetail.getPropertyAccess().fieldHashCode(), 1260, 347);
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
        grdMRNDetail.addRowNumberColumn();
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().name(), "GRN Date", 80, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().grnNo(), "GRN No.", 150, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().challanNo(), "Challan No.", 120, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().challanDate(), "Challan Date", 80, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().itemName(), "Item", 300, "itemName");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().value(), "Value", 150, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().status(), "Status", 80, "");
        grdMRNDetail.addTextColumn(grdMRNDetail.getPropertyAccess().entryBy(), "Entered By", 150, "");
        grdMRNDetail.initializeGrid(grdMRNDetail.getPropertyAccess().fieldHashCode(), 1260, 347);
        return grdMRNDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="grdBillDetail">
    interface GridBillDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBillDetail_Mdl> {

        @Editor.Path("BillNo")
        ValueProvider<GrdBillDetail_Mdl, String> billNo();

        @Editor.Path("PartyNo")
        ValueProvider<GrdBillDetail_Mdl, String> partyNo();

        @Editor.Path("PartyDate")
        ValueProvider<GrdBillDetail_Mdl, String> partyDate();

        @Editor.Path("Value")
        ValueProvider<GrdBillDetail_Mdl, String> value();

        @Editor.Path("Tds")
        ValueProvider<GrdBillDetail_Mdl, String> tds();

        @Editor.Path("NetPay")
        ValueProvider<GrdBillDetail_Mdl, String> netPay();

        @Editor.Path("Status")
        ValueProvider<GrdBillDetail_Mdl, String> status();

        @Editor.Path("LastLocation")
        ValueProvider<GrdBillDetail_Mdl, String> lastLocation();

        @Editor.Path("PaymDueDate")
        ValueProvider<GrdBillDetail_Mdl, String> paymDueDate();

        @Editor.Path("EntryBy")
        ValueProvider<GrdBillDetail_Mdl, String> entryBy();

        @Editor.Path("ItemName")
        ValueProvider<GrdBillDetail_Mdl, String> itemName();
    }
    final DmGrid<GrdBillDetail_Mdl, GridBillDetail_PrpAcs> grdBillDetail = new DmGrid(GWT.create(GridBillDetail_PrpAcs.class));

    private Grid iniGrdBillDetail() {
        grdBillDetail.addRowNumberColumn();
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().name(), "Bill Date", 80, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().billNo(), "Bill No.", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyNo(), "Party No.", 120, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().partyDate(), "Party Date", 80, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().itemName(), "Item", 280, "itemName");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().value(), "Value", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().tds(), "TDS", 80, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().netPay(), "Net Pay", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().status(), "Status", 65, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().lastLocation(), "Last Location", 100, "");
//        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().paymDueDate(), "Pay. due Dt.", 100, "");
        grdBillDetail.addTextColumn(grdBillDetail.getPropertyAccess().entryBy(), "Entered By", 80, "");
        grdBillDetail.initializeGrid(grdBillDetail.getPropertyAccess().fieldHashCode(), 1260, 347);
        return grdBillDetail.getGrid();
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
        grdAdvPaymnetDetail.initializeGrid(grdAdvPaymnetDetail.getPropertyAccess().fieldHashCode(), 1260, 347);
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
        grdFinalPaymnetDetail.initializeGrid(grdFinalPaymnetDetail.getPropertyAccess().fieldHashCode(), 1260, 347);
        return grdFinalPaymnetDetail.getGrid();
    }
    //</editor-fold>
}
