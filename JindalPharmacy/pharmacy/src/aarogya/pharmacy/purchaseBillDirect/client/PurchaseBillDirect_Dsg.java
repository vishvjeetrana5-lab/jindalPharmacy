package aarogya.pharmacy.purchaseBillDirect.client;

import aarogya.pharmacy.purchaseBillDirect.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadDimension;

abstract class PurchaseBillDirect_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.PURCHASE_BILL_DIRECT.caption;
    final String moduleName = pmPharmacy.PURCHASE_BILL_DIRECT.moduleName;
    final String preparedByBlog = "Prepared By";
    final String modifiedByBlog = "Modified By";
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";
    public final static String finalDisc = "Final Discount";
    public final static String approvedByBlog = "Approved By";
    public final static String departmentEditableBlog = "Department Editable";

    final FlowPanel flwBillOtherDetails = new FlowPanel();
    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final Button btnApprovedBy = new Button();
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtPaymentDueDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);
    final DmTextBox txtSupplierBillDate = new DmTextBox(11, true);
    final DmTextBox txtSupplierBillNo = new DmTextBox(25, true);

    final DmLabel labelCashDiscount = new DmLabel("Final Discount");
    final DmLabel labelLineDiscount = new DmLabel("Discount");

    final FlowPanel flwAddOrder = new FlowPanel();
    final Button btnAddOrder = new Button("Select Order");
    final Button btnBillHistory = new Button("Bill History");
    final Button btnPaymDetails = new Button("Paym. Details");
    final Button btnAdvDetails = new Button("Adv. Details");
    final DmGUI gui = new DmGUI(topControl, 450);
    final DmLabel lblAmtount = new DmLabel("0");
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final TaxSummary taxSummary = new TaxSummary(595, 140, 2);
    final OverHead overHead = new OverHead(topControl, new OverHeadDimension(370, 105, 0, 0, 0, false));

    final MenuItem mnuManageFile = new MenuItem("Manage File");
    final Menu menu = new Menu();

    final DmLabel labelTDS = new DmLabel("TDS:");
    final DmLabel labelTDSAmount = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface TxtHVoucher_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucher_Mdl> {

    }

    final DmTextHelp<TxtHVoucher_Mdl, TxtHVoucher_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucher_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHSupplier_PrpAcs extends KeyField_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHSupplier_Mdl, TxtHSupplier_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHSupplier_PrpAcs.class), 6, true);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCalculateOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCalculateOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniTxtHCalculateOn() {
        txtHCalculateOn.getDmGrid().addTextColumn(txtHCalculateOn.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHCalculateOn.getDmGrid().initializeGrid(txtHCalculateOn.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("SupplierText")
        ValueProvider<Find_Mdl, String> supplierText();

        @Editor.Path("DepartmentText")
        ValueProvider<Find_Mdl, String> departmentText();

        @Editor.Path("SupplierBillNo")
        ValueProvider<Find_Mdl, String> supplierBillNo();

        @Editor.Path("SupplierBillDate")
        ValueProvider<Find_Mdl, String> supplierBillDate();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 120, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierText(), "Supplier", 1, "supName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentText(), "Department", 250, "departmentName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierBillNo(), "Ref. No.", 120, "partyBillNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierBillDate(), "Date", 80, "partyBillDate", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GrdGrn">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGrnDetail_Mdl> {

        @Editor.Path("PoRecId")
        ValueProvider<GrdGrnDetail_Mdl, String> poRecId();

        @Editor.Path("BatchNo")
        ValueProvider<GrdGrnDetail_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdGrnDetail_Mdl, String> expiryDate();

        @Editor.Path("Mrp")
        ValueProvider<GrdGrnDetail_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<GrdGrnDetail_Mdl, String> saleRate();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdGrnDetail_Mdl, String> acceptQty();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdGrnDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("FreeQty")
        ValueProvider<GrdGrnDetail_Mdl, String> freeQty();

        @Editor.Path("SkuQty")
        ValueProvider<GrdGrnDetail_Mdl, String> skuQty();

        @Editor.Path("SkuName")
        ValueProvider<GrdGrnDetail_Mdl, String> skuName();

        @Editor.Path("Rate")
        ValueProvider<GrdGrnDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdGrnDetail_Mdl, String> amount();

        @Editor.Path("Remark")
        ValueProvider<GrdGrnDetail_Mdl, String> remark();

        @Editor.Path("SaleRateProffitMargine")
        ValueProvider<GrdGrnDetail_Mdl, String> saleRateProffitMargine();

    }

    final DmGrid<GrdGrnDetail_Mdl, Grd_PrpAcs> grdGRNDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdGRNDetail() {
        grdGRNDetail.addRowNumberColumn();
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().name(), "Medicine Name", 370, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().poRecId(), "Order No.", 80, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().batchNo(), "Batch No.", 70, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().expiryDate(), "Expiry Date", 80, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().mrp(), "MRP", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().saleRate(), "Sale Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().acceptQty(), "Accept Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().freeQty(), "Free Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().purchaseUnitName(), "Unit", 40, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().skuQty(), "SKU Qty.", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().skuName(), "SKU", 40, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().rate(), "Rate", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().remark(), "Remark", 80, "");
        grdGRNDetail.initializeGrid(grdGRNDetail.getPropertyAccess().fieldHashCode(), 1260, 240, 2);
        grdGRNDetail.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        grdGRNDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdGrnDetail_Mdl>() {
            @Override
            public String getColStyle(GrdGrnDetail_Mdl model, ValueProvider<? super GrdGrnDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getIsStatus()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.iNull(model.getIsStatus()).intValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } else if (DmShared.vNull(model.getReturnedQty()) != 0) {
                    styleClass += "dm-GridCellDisable ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdGrnDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdGRNDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BillOtherDetails">
    interface GridBillOtherDetails_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBillOtherDetails_Mdl> {

        @Editor.Path("FinalPayReq")
        ValueProvider<GrdBillOtherDetails_Mdl, String> finalPayReq();

        @Editor.Path("PayType")
        ValueProvider<GrdBillOtherDetails_Mdl, String> payType();

        @Editor.Path("Bank")
        ValueProvider<GrdBillOtherDetails_Mdl, String> bank();

        @Editor.Path("CheqNo")
        ValueProvider<GrdBillOtherDetails_Mdl, String> cheqNo();

        @Editor.Path("BillNo")
        ValueProvider<GrdBillOtherDetails_Mdl, String> billNo();

        @Editor.Path("FromLocation")
        ValueProvider<GrdBillOtherDetails_Mdl, String> fromLocation();

        @Editor.Path("ToLocation")
        ValueProvider<GrdBillOtherDetails_Mdl, String> toLocation();

        @Editor.Path("DateTime")
        ValueProvider<GrdBillOtherDetails_Mdl, String> dateTime();

        @Editor.Path("Amount")
        ValueProvider<GrdBillOtherDetails_Mdl, String> amount();

        @Editor.Path("Remark")
        ValueProvider<GrdBillOtherDetails_Mdl, String> remark();

    }
    final DmGrid<GrdBillOtherDetails_Mdl, GridBillOtherDetails_PrpAcs> gridBillFinalPayment = new DmGrid(GWT.create(GridBillOtherDetails_PrpAcs.class));
    final DmGrid<GrdBillOtherDetails_Mdl, GridBillOtherDetails_PrpAcs> gridBillHistory = new DmGrid(GWT.create(GridBillOtherDetails_PrpAcs.class));
    final DmGrid<GrdBillOtherDetails_Mdl, GridBillOtherDetails_PrpAcs> gridBillAdvance = new DmGrid(GWT.create(GridBillOtherDetails_PrpAcs.class));

    public Grid iniGrdBillPaymentDetail() {
        gridBillFinalPayment.addRowNumberColumn();
        gridBillFinalPayment.addTextColumn(gridBillFinalPayment.getPropertyAccess().dateTime(), "Date/Time", 150, "");
        gridBillFinalPayment.addTextColumn(gridBillFinalPayment.getPropertyAccess().billNo(), "F.Pay.No.", 110, "");
        gridBillFinalPayment.addTextColumn(gridBillFinalPayment.getPropertyAccess().cheqNo(), "Cheque No.", 110, "");
        gridBillFinalPayment.addTextColumn(gridBillFinalPayment.getPropertyAccess().bank(), "Bank", 200, "");
        gridBillFinalPayment.addTextColumn(gridBillFinalPayment.getPropertyAccess().amount(), "Amount", 90, "");
        gridBillFinalPayment.addTextColumn(gridBillFinalPayment.getPropertyAccess().payType(), "Pay. Type", 90, "");
        gridBillFinalPayment.initializeGrid(gridBillFinalPayment.getPropertyAccess().fieldHashCode(), 780, 400);

        return gridBillFinalPayment.getGrid();
    }

    public Grid iniGrdBillHistory() {
        gridBillHistory.addRowNumberColumn();
        gridBillHistory.addTextColumn(gridBillHistory.getPropertyAccess().dateTime(), "Date/Time", 150, "");
        gridBillHistory.addTextColumn(gridBillHistory.getPropertyAccess().billNo(), "Bill.H.No.", 110, "");
        gridBillHistory.addTextColumn(gridBillHistory.getPropertyAccess().fromLocation(), "From Location", 110, "");
        gridBillHistory.addTextColumn(gridBillHistory.getPropertyAccess().toLocation(), "To Location", 110, "");
        gridBillHistory.addTextColumn(gridBillHistory.getPropertyAccess().remark(), "Remark", 270, "");

        gridBillHistory.initializeGrid(gridBillHistory.getPropertyAccess().fieldHashCode(), 800, 400, 6);

        return gridBillHistory.getGrid();
    }

    public Grid iniGrdBillAdvanceDetail() {
        gridBillAdvance.addRowNumberColumn();
        gridBillAdvance.addTextColumn(gridBillAdvance.getPropertyAccess().billNo(), "Pay.No.", 90, "");
        gridBillAdvance.addTextColumn(gridBillAdvance.getPropertyAccess().amount(), "Amount", 75, "");
        gridBillAdvance.addTextColumn(gridBillAdvance.getPropertyAccess().cheqNo(), "Cheque No.", 90, "");
        gridBillAdvance.addTextColumn(gridBillAdvance.getPropertyAccess().bank(), "Bank", 150, "");
        gridBillAdvance.addTextColumn(gridBillAdvance.getPropertyAccess().payType(), "Pay. Type", 100, "");
        gridBillAdvance.initializeGrid(gridBillAdvance.getPropertyAccess().fieldHashCode(), 550, 400);
//        gridBillAdvance.initializeGrid(gridBillAdvance.getPropertyAccess().fieldHashCode(), 1257, 120, 1);

        return gridBillAdvance.getGrid();
    }

    //</editor-fold>
    public PurchaseBillDirect_Dsg() {
        ini();
        iniFind();
        iniMenu();
        iniTxtHSupplier();
        iniTxtHVoucherType();
        iniTxtHDepartment();
        iniTxtHCalculateOn();
        iniGrdBillPaymentDetail();
        iniGrdBillHistory();
        iniGrdBillAdvanceDetail();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkingArea);
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
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

    private TabLayoutPanel workingArea() {
        DmClient.setStyle(tabPanel, "100%", "100%", "left", null, DmPosition.NONE, true);

        tabPanel.add(tabGeneral(), "General");
        tabPanel.add(tabTermsCondition(), "Terms & Conditions");
        tabPanel.add(vl.add(tabBillOtherDetails()), "Bill Other Detials");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel tabBillOtherDetails() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "99%", "left", null, DmPosition.CENTER, true);
//        DmClient.setStyle(flwTable, "100%", "42%", "left", null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(31, Style.Unit.PCT);
        flwTd.add(btnBillHistory);
        buttonDesign(btnBillHistory);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(btnPaymDetails);
        buttonDesign(btnPaymDetails);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(btnAdvDetails);
        buttonDesign(btnAdvDetails);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", "242px", "left", null, DmPosition.NONE, true);
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "625px", "120px", "left", null, DmPosition.NONE, false);
//        DmClient.setStyle(flwTd, "625px", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(flwBillOtherDetails);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private void buttonDesign(Button btn) {
        btn.addStyleName("dm-button-grad");
        btn.setWidth("115px");
        btn.setHeight("20px");
        btn.getElement().setAttribute("style", btnAddOrder.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");

    }

    private FlowPanel tabTermsCondition() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(gui));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }
    FlowPanel flwFinalDisc = new FlowPanel();

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", null, "left", null, DmPosition.NONE, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(topLeftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "right", null, DmPosition.NONE, true);
        flwTd.add(topRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdGRNDetail()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "left", null, DmPosition.NONE, false);
        flwTd.add(taxSummary);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, null, null, "left", null, DmPosition.NONE, false);
        labelLineDiscount.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        labelLineDiscount.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        labelLineDiscount.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        labelLineDiscount.getElement().getStyle().setColor("blue");
        flwTd.add(labelLineDiscount);
        flwTd.getElement().getStyle().setMarginLeft(14, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "370px", null, "right", null, DmPosition.NONE, false);
        labelCashDiscount.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        labelCashDiscount.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        labelCashDiscount.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        labelCashDiscount.getElement().getStyle().setColor("blue");

//        flwTd.add(labelCashDiscount);
        flwFinalDisc.clear();
//        flwFinalDisc.add(labelCashDiscount);
        flwTd.add(flwFinalDisc);

        labelTDS.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        labelTDS.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        labelTDS.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        labelTDS.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        labelTDS.getElement().getStyle().setColor("blue");
        labelTDSAmount.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        labelTDSAmount.getElement().getStyle().setFontSize(91, Style.Unit.PCT);
        flwTd.add(labelTDS);
        flwTd.add(vl.add(labelTDSAmount));
        flwTd.add(vl.add(overHead));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.setTitle("Supplier - PT; Purchase Account - GS; Discount - DS; Round Off - RO; Final Discount - FD;");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnAddOrder.setWidth("155px");
        btnAddOrder.setHeight("20px");
        btnAddOrder.getElement().setAttribute("style", btnAddOrder.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddOrder.setTitle("Add Order");
        flwAddOrder.add(btnAddOrder);
        flwTd.add(flwAddOrder);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Calc. On");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCalculateOn));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Pmt. Due Dt.");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPaymentDueDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuManageFile);
        return menu;
    }
}
