package dataman.accounts.finalPaymentEntry.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.accounts.finalPaymentEntry.shared.Find_Mdl;
import dataman.accounts.finalPaymentEntry.shared.GrdOtherAc_Mdl;
import dataman.accounts.finalPaymentEntry.shared.GrdPendingFPReqDetail_Mdl;
import dataman.accounts.finalPaymentEntry.shared.TxtHSupplier_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.accounts.finalPaymentEntry.shared.GridDrdAdjust_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
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

public abstract class FinalPaymentEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().FINAL_PAYMENT_ENTRY.caption;
    final String moduleName = new lmAccounts().moduleName;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";

    final DmUserControl userControl = new DmUserControl();
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    public static final String tabFPDetail = "Pending F.P. Req. Details";
    final Button btnApprovedBy = new Button();
    final FlowPanel flwFPDetail = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtVoucherType = new DmTextBox(20, false);
    final DmTextBox txtPayNo = new DmTextBox(20, true);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final InlineLabel chkCheckAll = new InlineLabel();
    final DmTextBoxNumeric txtPartyAcBalance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBoxNumeric txtBankCashBalance = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final DmTextBoxNumeric txtSelectedBillDr = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtLessOtherCr = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtNetValueCr = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtOriginalAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBoxNumeric txtAmountPaid = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmTextBoxNumeric txtTdsDeduction = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, true);
    final DmRemarkTextBox txtRemak = new DmRemarkTextBox(200, false);
    final DmTextBox txtSupplierDisplayName = new DmTextBox(100, true);
    final DmTextBoxNumeric txtDrAdjustAmt = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, true, 10, false);
    final InlineLabel drChkCheckAll = new InlineLabel();

    DmControlBlog paymentCBlog;

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
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("PayDate")
        ValueProvider<Find_Mdl, String> payDate();

        @Editor.Path("Module")
        ValueProvider<Find_Mdl, String> module();

        @Editor.Path("Supplier")
        ValueProvider<Find_Mdl, String> supplier();

        @Editor.Path("AmtPaid")
        ValueProvider<Find_Mdl, String> amtPaid();

        @Editor.Path("PayMode")
        ValueProvider<Find_Mdl, String> payMode();

        @Editor.Path("ChqNo")
        ValueProvider<Find_Mdl, String> chqNo();

        @Editor.Path("Bank")
        ValueProvider<Find_Mdl, String> bank();

        @Editor.Path("Remark")
        ValueProvider<Find_Mdl, String> remark();

        @Editor.Path("OrgAmt")
        ValueProvider<Find_Mdl, String> orgAmt();

        @Editor.Path("Tds")
        ValueProvider<Find_Mdl, String> tds();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22, true);

    public void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Pay. Req. No.", 100, "recid");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().payDate(), "Date", 80, "date", DmEnum.DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().module(), "Module", 120, "module");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplier(), "Supplier", 200, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().orgAmt(), "Final Amt.", 100, "finalAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().tds(), "Tds", 100, "TDSDeducted").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().amtPaid(), "Paid Amt.", 100, "amtCr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().payMode(), "Pay Mode", 100, "cardTypeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().chqNo(), "Inst. No.", 100, "insNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().bank(), "A/c Name", 200, "acName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remark(), "Remark", 300, "remark");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 500);
        dmFind.getHelper().addSort("date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("V_No", DmEnum.DmSortType.ASC, "V_No");
        dmFind.getHelper().addSort("recid", DmEnum.DmSortType.ASC, "recid");

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

    public FinalPaymentEntry_Dsg() {
        ini();
        iniFind();
        iniModuleType();
        iniSupplier();
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
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "517px", null, null, DmEnum.DmPosition.CENTER, true);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "30px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "267px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);
        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; border-bottom:1px solid gray; margin: 1px 0px;");
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47.3%", "150px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(AcDetails());
        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "52%", "150px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(DrdAdjustDetails());
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style")
                + ";border-top:1px solid gray; border-bottom:1px solid gray; margin: 1px 0px;");
        DmClient.setStyle(flwTr, "100%", "65px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(summaryArea());
        flwTable.add(flwTr);

        return flwTable;
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
        lbl = new DmLabel("Pay. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPayNo));
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

    private TabLayoutPanel tabArea() {
        int tabIndex = 0;
        DmClient.setStyle(tabPanel, "100%", "270px", null, null, DmEnum.DmPosition.NONE, true);

        tabPanel.add(flwFPDetail, tabFPDetail);
        tabIndex++;

        flwFPDetail.add(pendingFPReqDetailTabArea());

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel pendingFPReqDetailTabArea() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "234px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdFpReqDetail());
        flwTable.add(chkCheckAll);
        chkCheckAll.addStyleName("fa fa-square-o");
        chkCheckAll.getElement().setAttribute("style", "position:absolute; top:5px; left:33px; z-index:10;cursor:pointer;");
        chkCheckAll.setTitle("Select all");

        return flwTable;
    }

    private FlowPanel AcDetails() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "150px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdAcDetail());
        return flwTable;
    }

    private FlowPanel DrdAdjustDetails() {
        FlowPanel flwTable;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "148px", null, null, DmEnum.DmPosition.CENTER, true);
        flwTable.add(iniGrdDrdAdjust());
        flwTable.add(drChkCheckAll);
        drChkCheckAll.addStyleName("fa fa-square-o");
        drChkCheckAll.getElement().setAttribute("style", "position:absolute; top:5px; left:33px; z-index:10;cursor:pointer;");
        drChkCheckAll.setTitle("Select all");
        return flwTable;
    }

    private FlowPanel summaryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Party A/C Bal.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtPartyAcBalance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Bank/Cash A/c Bal.");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBankCashBalance));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Selected Bills Tot.(Dr.)");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSelectedBillDr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Less Other(Cr.)");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtLessOtherCr));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Net Value(Cr.)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetValueCr));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtOriginalAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount Paid");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmountPaid));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Tds");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTdsDeduction));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "61%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemak));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Dr Adjust Amt");
        lbl.getElement().getStyle().setMarginLeft(9, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDrAdjustAmt));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Pending FP Req. Detail">
    interface GridFPReqDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdPendingFPReqDetail_Mdl> {

        @Editor.Path("Checked")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> checked();

        @Editor.Path("Date")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> date();

        @Editor.Path("PbillNo")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> pbillNo();

        @Editor.Path("PbillDate")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> pbillDate();

        @Editor.Path("TotalPo")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalPo();

        @Editor.Path("TotalGrn")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalGrn();

        @Editor.Path("TotalBill")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalBill();

        @Editor.Path("TotalTds")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalTds();

        @Editor.Path("NetBill")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> netBill();

        @Editor.Path("TotalAdv")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalAdv();

        @Editor.Path("TotalAdvNet")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalAdvNet();

        @Editor.Path("TotalAdvTds")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalAdvTds();

        @Editor.Path("AdvanceAdjusted")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> advanceAdjusted();

        @Editor.Path("TotalBillpayment")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> totalBillpayment();

        @Editor.Path("FinalDuePaymnet")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> finalDuePaymnet();

        @Editor.Path("FinalPayReq")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> finalPayReq();

        @Editor.Path("BillDueDt")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> billDueDt();

        @Editor.Path("PayDueDt")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> payDueDt();

        @Editor.Path("EnterdBy")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> enterdBy();

        @Editor.Path("PartyBillNo")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> partyBillNo();

        @Editor.Path("DrAdjustAmt")
        ValueProvider<GrdPendingFPReqDetail_Mdl, String> drAdjustAmt();

    }
    final DmGrid<GrdPendingFPReqDetail_Mdl, GridFPReqDetail_PrpAcs> grdPeningFPReqDetail = new DmGrid(GWT.create(GridFPReqDetail_PrpAcs.class));

    private Grid iniGrdFpReqDetail() {

        ColumnConfig<GrdPendingFPReqDetail_Mdl, String> selectCheckBox;
        grdPeningFPReqDetail.addRowNumberColumn();
        selectCheckBox = grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().checked(), "", 25, "");
//      grdPeningFPReqDetail.addCheckSelectionColumn(com.sencha.gxt.core.client.Style.SelectionMode.SIMPLE);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().name(), "No.", 100, "no");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().date(), "Date", 80, "date");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().pbillNo(), "P. Bill No.", 100, "pbNo");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().partyBillNo(), "Party Bill No.", 100, "partyBillNo");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().pbillDate(), "P. Bill Date", 80, "pbDate");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalPo(), "Total P.O.", 100, "totalPo").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalGrn(), "Total GRN", 100, "totalGrn").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalBill(), "Total Bill", 100, "totalBill").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalTds(), "Total Bill TDS", 100, "totalTds").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().netBill(), "Net(tot. Bill-Tot.TDS)", 120, "netBill").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalAdv(), "Total Adv.", 100, "advanceAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalAdvNet(), "Total Adv. Net(totl Adv.-tot. TDS).", 100, "advancePaid").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalAdvTds(), "Total Adv. Tds", 100, "TDSDeducted").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().advanceAdjusted(), "Adv. adjusted against current bill", 120, "advanceSettled").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().totalBillpayment(), "Total Bill Paym.", 120, "totalBillPaym").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().finalDuePaymnet(), "Fin. Due Pay.(Net Bill-(Tot. Adv-Tot. Pay))", 150, "finDuePay").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().drAdjustAmt(), "Dr. Adjust Amt", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().finalPayReq(), "Fin. Pay. Req.", 120, "finPayReq").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().billDueDt(), "Bill Due Dt.", 80, "billDueDt");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().payDueDt(), "Pay Due Date", 80, "paymDt");
        grdPeningFPReqDetail.addTextColumn(grdPeningFPReqDetail.getPropertyAccess().enterdBy(), "Entered By", 80, "enterdBy");
        grdPeningFPReqDetail.initializeGrid(grdPeningFPReqDetail.getPropertyAccess().fieldHashCode(), 1260, 234);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdPeningFPReqDetail.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                } else if (DmShared.iNull(grdPeningFPReqDetail.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                }

            }
        });
        return grdPeningFPReqDetail.getGrid();

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="A/c Detail">
    interface GridAcDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdOtherAc_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdOtherAc_Mdl, String> amount();

        @Editor.Path("Remark")
        ValueProvider<GrdOtherAc_Mdl, String> remark();

    }
    final DmGrid<GrdOtherAc_Mdl, GridAcDetail_PrpAcs> grdAcDetail = new DmGrid(GWT.create(GridAcDetail_PrpAcs.class));

    private Grid iniGrdAcDetail() {
        grdAcDetail.addTextColumn(grdAcDetail.getPropertyAccess().name(), "Other A/c", 200, "otherAc");
        grdAcDetail.addTextColumn(grdAcDetail.getPropertyAccess().amount(), "Amount", 100, "amount").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdAcDetail.addTextColumn(grdAcDetail.getPropertyAccess().remark(), "Remark", 300, "remark");
        grdAcDetail.initializeGrid(grdAcDetail.getPropertyAccess().fieldHashCode(), 600, 150);
        return grdAcDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DRD Adjust Detail">
    interface GridDrdAdjust_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GridDrdAdjust_Mdl> {

        @Editor.Path("Checked")
        ValueProvider<GridDrdAdjust_Mdl, String> checked();

        @Editor.Path("SupplierText")
        ValueProvider<GridDrdAdjust_Mdl, String> supplierText();

        @Editor.Path("VoucherNo")
        ValueProvider<GridDrdAdjust_Mdl, String> voucherNo();

        @Editor.Path("ContraAC")
        ValueProvider<GridDrdAdjust_Mdl, String> contraAC();

        @Editor.Path("VoucherType")
        ValueProvider<GridDrdAdjust_Mdl, String> voucherType();

        @Editor.Path("AmtDr")
        ValueProvider<GridDrdAdjust_Mdl, String> amtDr();

        @Editor.Path("AdjAmt")
        ValueProvider<GridDrdAdjust_Mdl, String> adjAmt();

        @Editor.Path("PendingAdjustment")
        ValueProvider<GridDrdAdjust_Mdl, String> pendingAdjustment();
    }
    final DmGrid<GridDrdAdjust_Mdl, GridDrdAdjust_PrpAcs> grdDrdAdjust = new DmGrid(GWT.create(GridDrdAdjust_PrpAcs.class));

    private Grid iniGrdDrdAdjust() {
        ColumnConfig<GridDrdAdjust_Mdl, String> selectCheckBox;
        grdDrdAdjust.addRowNumberColumn();
        selectCheckBox = grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().checked(), "", 25, "");
//        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().supplierText(), "Supplier", 120, "supplier");
        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().voucherNo(), "Voucher No.", 150, "voucherNo");
//        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().contraAC(), "Contra/Ac", 170, "contraAc");
        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().voucherType(), "VoucherType", 200, "voucherType");
//        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().amtDr(), "Dr Amt.", 90, "amtDr").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().adjAmt(), "Adj Amt.", 90, "adjAmt").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDrdAdjust.addTextColumn(grdDrdAdjust.getPropertyAccess().pendingAdjustment(), "Dr Amt.", 140, "pendingAdjustment").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdDrdAdjust.initializeGrid(grdDrdAdjust.getPropertyAccess().fieldHashCode(), 665, 147, 4);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (DmShared.iNull(grdDrdAdjust.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                } else if (DmShared.iNull(grdDrdAdjust.getGrid().getStore().get(context.getIndex()).getChecked()).intValue() == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                }

            }
        });
        return grdDrdAdjust.getGrid();
    }
    //</editor-fold>
}
