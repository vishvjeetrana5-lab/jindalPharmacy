package aarogya.inventory.purchaseBill.client;

import aarogya.inventory.purchaseBill.shared.GrdBillOtherDetails_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
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
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.purchaseBill.shared.Find_Mdl;
import aarogya.inventory.purchaseBill.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHHeaderGrnNo_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseBill.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;

abstract class PurchaseBill_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.PURCHASE_BILL.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String approvedByBlog = "Approved By & Date";
    public final static String dateEditableBlog = "Date Editable";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwBillOtherDetails = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Button btnApprovedBy = new Button();
    final DmLabel labelTDS = new DmLabel("TDS:");
    final DmLabel labelTDSAmount = new DmLabel("");
    final TaxSummary taxSummary = new TaxSummary(625, 120, 1);
    final ServiceGrid serviceDetailGrid = new ServiceGrid(625, 120);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtPaymentDueDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtSupplierBillNo = new DmTextBox(25, true);
    final DmTextBox txtSupplierBillDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);

    //<editor-fold defaultstate="collapsed" desc="OverHead Containers">
    final DmTextBoxNumeric txtGoodsAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtServiceAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTaxableAmt = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSgstUgst = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtUGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtIGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSubTotal = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtFinalDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtFinalDiscount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtTotalAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtRoundoff = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtLandedValue = new DmTextBoxNumeric(2, true, 12, false);
    final DmLabel lblSgstUgst = new DmLabel("SGST/UGST");

    //</editor-fold>
    final FlowPanel flwAddGRN = new FlowPanel();
    final Button btnAddGRN = new Button("Add GRN");
    final Button btnBillHistory = new Button("Bill History");
    final Button btnPaymDetails = new Button("Paym. Details");
    final Button btnAdvDetails = new Button("Adv. Details");

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final TabLayoutPanel tabPanel2 = new TabLayoutPanel(1.6, Style.Unit.EM);
    final TabLayoutPanel tabPanel3 = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 170);

    //<editor-fold defaultstate="collapsed" desc="Help Events">
    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface TxtHVoucher_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHVoucher_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucher_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCalculateOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCalculateOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniTxtHCalcOn() {
        txtHCalculateOn.getDmGrid().addTextColumn(txtHCalculateOn.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHCalculateOn.getDmGrid().initializeGrid(txtHCalculateOn.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHCalculateOn.getHelper().addSort("name", DmEnum.DmSortType.ASC);

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

    //<editor-fold defaultstate="collapsed" desc="txtHRCMApplicable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRCMApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHRCMApplicable() {
        txtHRCMApplicable.getDmGrid().addTextColumn(txtHRCMApplicable.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHRCMApplicable.getDmGrid().initializeGrid(txtHRCMApplicable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }//</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="txtHGrnNo">
    interface TxtHGrnNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHHeaderGrnNo_Mdl> {

        @Editor.Path("GrnDate")
        ValueProvider<TxtHHeaderGrnNo_Mdl, String> grnDate();

        @Editor.Path("ChallanNo")
        ValueProvider<TxtHHeaderGrnNo_Mdl, String> challanNo();

        @Editor.Path("ChallanDate")
        ValueProvider<TxtHHeaderGrnNo_Mdl, String> challanDate();

        @Editor.Path("PoNo")
        ValueProvider<TxtHHeaderGrnNo_Mdl, String> poRecId();

        @Editor.Path("PoDocID")
        ValueProvider<TxtHHeaderGrnNo_Mdl, String> poDocId();

    }

    final DmTextHelpMulti<TxtHHeaderGrnNo_Mdl, TxtHGrnNo_PrpAcs> txtHGrnNo = new DmTextHelpMulti<>(GWT.create(TxtHGrnNo_PrpAcs.class), 5, false);

    private void iniTxtHGrnNo() {
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().name(), "GRN No.", 120, "recId");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().grnDate(), "GRN Date", 85, "v_Date");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().challanNo(), "Challan No.", 120, "refNo");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().challanDate(), "Challan Date", 85, "refDate");
        txtHGrnNo.getDmGrid().addTextColumn(txtHGrnNo.getDmGrid().getPropertyAccess().poRecId(), "PO. No.", 85, "poRecId");
        txtHGrnNo.getDmGrid().initializeGrid(txtHGrnNo.getDmGrid().getPropertyAccess().fieldHashCode(), 430, 0, 3);
        txtHGrnNo.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHGrnNo.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("BillDate")
        ValueProvider<Find_Mdl, String> billDate();

        @Editor.Path("SupplierName")
        ValueProvider<Find_Mdl, String> supplierName();

        @Editor.Path("SupplierBillNo")
        ValueProvider<Find_Mdl, String> supplierBillNo();

        @Editor.Path("SupplierBillDate")
        ValueProvider<Find_Mdl, String> supplierBillDate();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();

        @Editor.Path("BillType")
        ValueProvider<Find_Mdl, String> billType();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 150, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().billDate(), "Date", 100, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().billType(), "Type", 120, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierName(), "Supplier Name", 1, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierBillNo(), "Bill No.", 130, "partyBillNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierBillDate(), "Bill Date", 110, "replace(convert(varchar, partyBillDate, 106), ' ','-')partyBillDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 160, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    public PurchaseBill_Dsg() {
        ini();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHSupplier();
        iniTxtHCalcOn();
        iniTxtHRCMApplicable();
        iniTxtHGrnNo();
        iniGrdBillPaymentDetail();
        iniGrdBillHistory();
        iniGrdBillAdvanceDetail();
    }

    private void ini() {
        FlowPanel flwWorkingArea;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flwWorkingArea);
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
        userControl.addGap(100);

        userControl.addTextBlog(approvedByBlog, 200, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.addStyleName("dm-button-grad");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        return userControl;
    }

    private TabLayoutPanel workingArea() {
        DmClient.setStyle(tabPanel, "100%", "100%", "left", null, DmPosition.CENTER, true);

        tabPanel.add(vl.add(tabGeneral()), "General");
        tabPanel.add(vl.add(tabTermsCondition()), "Terms & Conditions");
        tabPanel.add(vl.add(tabBillOtherDetails()), "Bill Other Detials");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private TabLayoutPanel workingAreaBottomGrid() {
        DmClient.setStyle(tabPanel2, "100%", "147px", null, null, DmPosition.NONE, true);

        tabPanel2.add(vl.add(serviceDetailGrid), "Service");
        tabPanel2.add(vl.add(taxSummary), "Tax Summary");

        tabPanel2.selectTab(0);
        tabPanel2.ensureDebugId("cwTabPanel");

        return tabPanel2;
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
        btn.getElement().setAttribute("style", btnAddGRN.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");

    }

    private FlowPanel tabTermsCondition() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(gui);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", "453", "left", null, DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.7%", "68px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "right", null, DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "242px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.add(vl.add(iniGrdItemDetail()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50.1%", "147px", "left", null, DmPosition.NONE, false);
        flwTd.add(workingAreaBottomGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", "147px", "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(overHeadArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel overHeadArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        //*************************<Row 1>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Goods Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtGoodsAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtGoodsAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SubTotal");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtSubTotal.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtSubTotal));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //*************************<Row 2>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("service Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtServiceAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtServiceAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Final Discount");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        txtFinalDiscountPer.getElement().setPropertyString("placeholder", " % ");
        txtFinalDiscountPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtFinalDiscountPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        txtFinalDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtFinalDiscount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //*************************<Row 3>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtDiscount.getElement().setPropertyString("placeholder", " % ");
        txtDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtDiscount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtTotalAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtTotalAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //*************************<Row 4>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Taxable Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtTaxableAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtTaxableAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Round off");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtRoundoff.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtRoundoff));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //*************************<Row 4>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtSgstUgst.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtSgstUgst));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Net Amt");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtNetAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtNetAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //*************************<Row 5>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("CGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtCGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtCGST));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Landed Value");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtLandedValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtLandedValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //*************************<Row 5>****************************************
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        labelTDS.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        labelTDS.getElement().getStyle().setWidth(100, Style.Unit.PCT);
        labelTDS.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        labelTDS.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        labelTDS.getElement().getStyle().setColor("blue");
        labelTDSAmount.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        labelTDSAmount.getElement().getStyle().setFontSize(91, Style.Unit.PCT);
        flwTd.add(labelTDS);
        flwTd.add(vl.add(labelTDSAmount));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(-12, Style.Unit.PX);
        lbl = new DmLabel("IGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtIGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(vl.add(txtIGST));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.setTitle("Round Off - RO; Final Discount - FD;");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("RCM");
        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRCMApplicable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillDate));
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
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
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
        lbl = new DmLabel("Calc. On");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCalculateOn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("GRN No");
        lbl = new DmLabel("");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
//        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "17x", "left", null, DmPosition.NONE, false);
        btnAddGRN.addStyleName("dm-button-grad");
        btnAddGRN.setWidth("156px");
        btnAddGRN.setHeight("20px");
        btnAddGRN.getElement().setAttribute("style", btnAddGRN.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddGRN.setTitle("Add GRN");
        flwTd.add(flwAddGRN);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHGrnNo));
        flwTr.add(flwTd);

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

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

        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridItemDetail">
    interface GridItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("PropertiesCsv")
        ValueProvider<GrdItemDetail_Mdl, String> propertiesCsv();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

        @Editor.Path("PcRefNo")
        ValueProvider<GrdItemDetail_Mdl, String> pcRefNo();

        @Editor.Path("PcRefDate")
        ValueProvider<GrdItemDetail_Mdl, String> pcRefDate();

        @Editor.Path("PoRecId")
        ValueProvider<GrdItemDetail_Mdl, String> poRecId();

        @Editor.Path("DocQty")
        ValueProvider<GrdItemDetail_Mdl, String> docQty();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdItemDetail_Mdl, String> acceptQty();

        @Editor.Path("PcRecId")
        ValueProvider<GrdItemDetail_Mdl, String> pcRecId();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().pcRecId(), "GRN No.", 90, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().pcRefNo(), "Challan No.", 90, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().pcRefDate(), "Challan Date", 75, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().poRecId(), "Order No.", 90, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Item Name", 150, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().propertiesCsv(), "Properties", 100, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().docQty(), "Doc. Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().acceptQty(), "Acp. Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().purchaseUnitName(), "Unit", 50, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().rate(), "Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1257, 239, 6);

        gridItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getIsStatus()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.iNull(model.getIsStatus()).intValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } else if (DmShared.vNull(model.getReturnedQty()) != 0) {
                    styleClass += "dm-GridCellDisable ";
                }
                if (model.isServiceRecord()) {
                    styleClass = "dm-GridRemoveRow";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return gridItemDetail.getGrid();
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
}
