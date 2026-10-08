package aarogya.pharmacy.purchaseBill.client;

import aarogya.pharmacy.purchaseBill.shared.Find_Mdl;
import aarogya.pharmacy.purchaseBill.shared.GrdBillOtherDetails_Mdl;
import aarogya.pharmacy.purchaseBill.shared.GrdItemDetail_Mdl;
import aarogya.pharmacy.purchaseBill.shared.TxtHSupplier_Mdl;
import aarogya.pharmacy.purchaseBill.shared.TxtHVoucher_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
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
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
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

abstract class PurchaseBill_Dsg extends IsWidgetExtended {

    final pmPharmacy pmPharmacy = new pmPharmacy();
    final String widgetName = pmPharmacy.PURCHASE_BILL.caption;
    final String moduleName = pmPharmacy.moduleName;
    final String preparedByBlog = "Prepared By";
    final String modifiedByBlog = "Modified By";
    public static final String approvedByBlog = "Approved By";
    public final static String finalDisc = "Final Discount";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final Button btnApprovedBy = new Button();

    final DmLabel labelCashDiscount = new DmLabel("Final Discount");
    final DmLabel labelLineDiscount = new DmLabel("Discount");

    final DmLabel labelTDS = new DmLabel("TDS:");
    final DmLabel labelTDSAmount = new DmLabel("");
    final OverHead overHead = new OverHead(topControl, new OverHeadDimension(370, 105, 0, 0, 0, false));
    final TaxSummary taxSummary = new TaxSummary(595, 140, 2);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtPaymentDueDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtSupplierBillNo = new DmTextBox(25, true);
    final DmTextBox txtSupplierBillDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);

    final FlowPanel flwBillOtherDetails = new FlowPanel();
    final FlowPanel flwAddGRN = new FlowPanel();
    final Button btnAddGRN = new Button("Add GRN");
    final Button btnBillHistory = new Button("Bill History");
    final Button btnPaymDetails = new Button("Paym. Details");
    final Button btnAdvDetails = new Button("Adv. Details");

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 450);
    final MenuItem mnuManageFile = new MenuItem("Manage File");
    final Menu menu = new Menu();
    public final static String attachFileBlog = "Attach Files";
    public final static String deleteFileBlog = "Delete Files";

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    interface TxtHVoucher_PrpAcs extends KeyField_T_PrpAcs<TxtHVoucher_Mdl> {

    }

    final DmTextHelp<TxtHVoucher_Mdl, TxtHVoucher_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucher_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCalculateOn">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCalculateOn = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniTxtHCalcOn() {
        txtHCalculateOn.getDmGrid().addTextColumn(txtHCalculateOn.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHCalculateOn.getDmGrid().initializeGrid(txtHCalculateOn.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
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

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 110, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().billDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().billType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierName(), "Supplier Name", 1, "vendorName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierBillNo(), "Ref. No.", 110, "partyBillNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierBillDate(), "Ref. Date", 110, "partyBillDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 130, "approvedBy");
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
        iniMenu();
        iniTxtHVoucherType();
        iniTxtHSupplier();
        iniTxtHCalcOn();
        iniGrdBillPaymentDetail();
        iniGrdBillHistory();
        iniGrdBillAdvanceDetail();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", "left", null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        vrtInnerForm.add(flw);
        flw.add(workingArea());
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

    private TabLayoutPanel workingArea() {
        DmClient.setStyle(tabPanel, "100%", "100%", "left", null, DmPosition.NONE, true);

        tabPanel.add(vl.add(tabGeneral()), "General");
        tabPanel.add(vl.add(tabTermsCondition()), "Terms & Conditions");
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
        btn.getElement().setAttribute("style", btnAddGRN.getElement().getAttribute("style")
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

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", null, "left", null, DmPosition.NONE, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdItemDetail()));
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
        flwTd.add(labelCashDiscount);

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
        lbl.setTitle("Supplier - PT; Purchase Account - GS; Round Off - RO; Final Discount - FD;");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
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
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
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
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCalculateOn));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "25%", "20px", "left", null, DmPosition.NONE, false);
        btnAddGRN.setWidth("120px");
        btnAddGRN.setHeight("20px");
        btnAddGRN.getElement().setAttribute("style", btnAddGRN.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddGRN.setTitle("Add GRN");
        flwAddGRN.add(btnAddGRN);
        flwTd.add(flwAddGRN);
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

    //<editor-fold defaultstate="collapsed" desc="gridMedDetail">
    interface GridItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("PoRecId")
        ValueProvider<GrdItemDetail_Mdl, String> poRecId();

        @Editor.Path("PcRecId")
        ValueProvider<GrdItemDetail_Mdl, String> pcRecId();

        @Editor.Path("BatchNo")
        ValueProvider<GrdItemDetail_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdItemDetail_Mdl, String> expiryDate();

        @Editor.Path("Mrp")
        ValueProvider<GrdItemDetail_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<GrdItemDetail_Mdl, String> saleRate();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdItemDetail_Mdl, String> acceptQty();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridMedDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridMedDetail.addRowNumberColumn();
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().name(), "Medicine Name", 230, "");
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().poRecId(), "Order No.", 80, "");
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().pcRecId(), "GRN No.", 70, "");
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().batchNo(), "Batch No.", 70, "");
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().expiryDate(), "Expiry Date", 80, "");
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().mrp(), "MRP", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().saleRate(), "Sale Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().acceptQty(), "Accept Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().purchaseUnitName(), "Unit", 40, "");
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().rate(), "Rate", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedDetail.addTextColumn(gridMedDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedDetail.initializeGrid(gridMedDetail.getPropertyAccess().fieldHashCode(), 1260, 240, 2);

        gridMedDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
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
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return gridMedDetail.getGrid();
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
