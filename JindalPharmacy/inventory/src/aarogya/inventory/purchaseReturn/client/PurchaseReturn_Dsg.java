package aarogya.inventory.purchaseReturn.client;

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
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
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
import dataman.dmToolkit.overHead.client.OverHead;
import dataman.dmToolkit.overHead.shared.OverHeadDimension;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.purchaseReturn.shared.Find_Mdl;
import aarogya.inventory.purchaseReturn.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHBillNo_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHGateEntry_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHSupplier_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHTransporter_Mdl;
import aarogya.inventory.purchaseReturn.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;

abstract class PurchaseReturn_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.PURCHASE_RETURN.caption;
    final String moduleName = pmInventory.moduleName;
    final String preparedByBlog = "Prepared By & Date";
    final String modifiedByBlog = "Modified By & Date";
    public final static String approvedByBlog = "Approved By & Date";
    public final static String departmentEditableBlog = "Department Editable";
    public final static String dateEditableBlog = "Date Editable";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final Button btnApprovedBy = new Button();
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(100, false);
    final DmTextBox txtBillDate = new DmTextBox(11, false);
    final DmTextBox txtSupplierBillNo = new DmTextBox(16, true);
    final DmTextBox txtSupplierBillDate = new DmTextBox(11, true);

    final DmTextBox txtVehicleNo = new DmTextBox(15, false);

    final DmTextBox txtEwayNo = new DmTextBox(20, false);
    final DmTextBox txtEwayDate = new DmTextBox(11, false);

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final TabLayoutPanel tabPanel2 = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 170);
    final TaxSummary taxSummary = new TaxSummary(620, 140,1);
    final ServiceGrid serviceDetailGrid = new ServiceGrid(625, 140);
    //final OverHead overHead = new OverHead(topControl, new OverHeadDimension(370, 140, 0, 0, 0, false));

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
    //<editor-fold defaultstate="collapsed" desc="txtHSupplier">
    interface TxtHVendor_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHSupplier_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHSupplier_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHSupplier_Mdl, String> cityName();

    }
    final DmTextHelp<TxtHSupplier_Mdl, TxtHVendor_PrpAcs> txtHSupplier = new DmTextHelp(GWT.create(TxtHVendor_PrpAcs.class), 6, true);

    private void iniTxtHSupplier() {
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHSupplier.getDmGrid().addTextColumn(txtHSupplier.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().fieldHashCode(), 550, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHBillNo">
    interface TxtHBillNo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHBillNo_Mdl> {

        @Editor.Path("PbV_Date")
        ValueProvider<TxtHBillNo_Mdl, String> pbV_Date();

        @Editor.Path("PbRefNo")
        ValueProvider<TxtHBillNo_Mdl, String> pbRefNo();

        @Editor.Path("PbRefDate")
        ValueProvider<TxtHBillNo_Mdl, String> pbRefDate();

    }

    final DmTextHelp<TxtHBillNo_Mdl, TxtHBillNo_PrpAcs> txtHBillNo = new DmTextHelp(GWT.create(TxtHBillNo_PrpAcs.class), 6, false);

    private void iniTxtHBillNo() {
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().name(), "Bill No.", 0, "name");
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().pbRefNo(), "Ref. No.", 100, "pbRefNo");
        txtHBillNo.getDmGrid().addTextColumn(txtHBillNo.getDmGrid().getPropertyAccess().pbRefDate(), "Ref. Date", 100, "pbRefDate");
        txtHBillNo.getDmGrid().initializeGrid(txtHBillNo.getDmGrid().getPropertyAccess().fieldHashCode(), 400, 0, 1);
        txtHBillNo.getHelper().addSort("PbV_Date", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGstReason">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstReason = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHReason() {
        txtHGstReason.getDmGrid().addTextColumn(txtHGstReason.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstReason.getDmGrid().initializeGrid(txtHGstReason.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHGstReason.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTransporter">
    interface TxtHtransporter_PrpAcs extends KeyField_T_PrpAcs<TxtHTransporter_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHTransporter_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHTransporter_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHTransporter_Mdl, TxtHtransporter_PrpAcs> txtHTransporter = new DmTextHelp(GWT.create(TxtHtransporter_PrpAcs.class), 6, false);

    private void iniTxtHTransporter() {
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHTransporter.getDmGrid().initializeGrid(txtHTransporter.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHTransporter.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHTransporter.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtGateEntry">
    interface TxtHGateEntryNo_PrpAcs extends KeyField_T_PrpAcs<TxtHGateEntry_Mdl> {

        @Editor.Path("GateEntryDate")
        ValueProvider<TxtHGateEntry_Mdl, String> gateEntryDate();

        @Editor.Path("Transportername")
        ValueProvider<TxtHGateEntry_Mdl, String> transportername();

        @Editor.Path("VehicleNo")
        ValueProvider<TxtHGateEntry_Mdl, String> vehicleNo();

    }

    final DmTextHelp<TxtHGateEntry_Mdl, TxtHGateEntryNo_PrpAcs> txtHGateEntryNo = new DmTextHelp(GWT.create(TxtHGateEntryNo_PrpAcs.class), 6, false);

    private void iniTxtHGateEntryNo() {
        txtHGateEntryNo.getDmGrid().addTextColumn(txtHGateEntryNo.getDmGrid().getPropertyAccess().name(), "Gate No.", 120, "recId");
        txtHGateEntryNo.getDmGrid().addTextColumn(txtHGateEntryNo.getDmGrid().getPropertyAccess().gateEntryDate(), "Gate Entry Date", 100, "gateEntryDate");
        txtHGateEntryNo.getDmGrid().addTextColumn(txtHGateEntryNo.getDmGrid().getPropertyAccess().transportername(), "Transporter", 200, "gateEntryDate");
        txtHGateEntryNo.getDmGrid().addTextColumn(txtHGateEntryNo.getDmGrid().getPropertyAccess().vehicleNo(), "Vehicle No.", 80, "gateEntryDate");
        txtHGateEntryNo.getDmGrid().initializeGrid(txtHGateEntryNo.getDmGrid().getPropertyAccess().code(), 530, 0);
        txtHGateEntryNo.getHelper().addSort("gateEntryDate", DmSortType.DESC);
        txtHGateEntryNo.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHGateEntryNo.getHelper().addSort("v_No", DmSortType.DESC, "v_No");
        txtHGateEntryNo.getHelper().addSort("recId", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEwayBillReq">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEwayBillReq = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHEwayBillReq() {
        txtHEwayBillReq.getDmGrid().addTextColumn(txtHEwayBillReq.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHEwayBillReq.getDmGrid().initializeGrid(txtHEwayBillReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTransportMode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTransportMode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHTransportMode() {
        txtHTransportMode.getDmGrid().addTextColumn(txtHTransportMode.getDmGrid().getPropertyAccess().name(), "Mode", 170, "Name");
        txtHTransportMode.getDmGrid().initializeGrid(txtHTransportMode.getDmGrid().getPropertyAccess().code(), 200, 0);
        txtHTransportMode.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHRCMApplicable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRCMApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHRCMApplicable() {
        txtHRCMApplicable.getDmGrid().addTextColumn(txtHRCMApplicable.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHRCMApplicable.getDmGrid().initializeGrid(txtHRCMApplicable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }//</editor-fold>

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("SupplierName")
        ValueProvider<Find_Mdl, String> supplierName();

        @Editor.Path("GateNo")
        ValueProvider<Find_Mdl, String> gateNo();

        @Editor.Path("RefNo")
        ValueProvider<Find_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<Find_Mdl, String> refDate();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();
        
        @Editor.Path("DepartmentName")
        ValueProvider<Find_Mdl, String> departmentName();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Return No.", 150, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 60, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierName(), "Supplier", 150, "supplierName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gateNo(), "Gate No.", 150, "gateNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refNo(), "Ref No.", 150, "partyBillNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refDate(), "Ref Date", 80, "replace(convert(varchar, partyBillDate, 106), ' ','-')");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approved By", 150, "approvedBy");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentName(), "Department", 150, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0,6);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    public PurchaseReturn_Dsg() {
        ini();
        iniFind();
        iniTxtHSupplier();
        iniTxtHVoucherType();
        iniTxtHReason();
        iniTxtHBillNo();
        iniTxtHTransporter();
        iniTxtHGateEntryNo();
        iniTxtHEwayBillReq();
        iniTxtHTransportMode();
        iniTxtHRCMApplicable();
    }

    private void ini() {
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 28) + "px");

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

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
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
        flwTd.add(gui);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel tabGeneral() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", "453px", "left", null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.7%", "106px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48.5%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.5%", null, "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "98.7%", "88px", "left", null, DmPosition.NONE, true);
        DmClient.setStyle(flwTr, "98.7%", "5%", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "92%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "162px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdGRNDetail()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(13, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "147px", "left", null, DmPosition.NONE, false);
        flwTd.add(workingAreaBottomGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "right", null, DmPosition.NONE, true);
        flwTd.add(vl.add(overHeadArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private TabLayoutPanel workingAreaBottomGrid() {
        DmClient.setStyle(tabPanel2, "100%", "167px", null, null, DmPosition.NONE, true);

        tabPanel2.add(vl.add(serviceDetailGrid), "Service");
        tabPanel2.add(vl.add(taxSummary), "Tax Summary");

        tabPanel2.selectTab(0);
        tabPanel2.ensureDebugId("cwTabPanel");

        return tabPanel2;
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
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.setTitle("Round Off - RO; Final Discount - FD;");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("RCM");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRCMApplicable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtBillDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref.Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSupplierBillDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "84%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstReason));
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
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Return No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gate No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGateEntryNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Bill Req.");
        lbl.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEwayBillReq));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transporter");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "83%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransporter));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transport Mode");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransportMode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Vehicle No.");
        lbl.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVehicleNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("E-Way Bill No");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(17, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="GrdGrn">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("PropertiesCsv")
        ValueProvider<GrdItemDetail_Mdl, String> propertiesCsv();

        @Editor.Path("PcRecId")
        ValueProvider<GrdItemDetail_Mdl, String> pcRecId();

        @Editor.Path("PoRecId")
        ValueProvider<GrdItemDetail_Mdl, String> poRecId();

        @Editor.Path("BatchNo")
        ValueProvider<GrdItemDetail_Mdl, String> batchNo();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("Quantity")
        ValueProvider<GrdItemDetail_Mdl, String> quantity();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

        @Editor.Path("DepartmentName")
        ValueProvider<GrdItemDetail_Mdl, String> departmentName();

    }

    final DmGrid<GrdItemDetail_Mdl, Grd_PrpAcs> grdItemDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdGRNDetail() {
        grdItemDetail.addRowNumberColumn();
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().departmentName(), "Department", 150, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().name(), "Item Name", 220, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().propertiesCsv(), "Properties", 250, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().pcRecId(), "GRN No.", 110, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().poRecId(), "Order No.", 110, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().purchaseUnitName(), "Unit", 50, "");
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().quantity(), "Quantity", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().rate(), "Rate", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().amount(), "Amount", 110, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdItemDetail.initializeGrid(grdItemDetail.getPropertyAccess().fieldHashCode(), 1248, 160);
        grdItemDetail.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        grdItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (model.isServiceRecord()) {
                    styleClass = "dm-GridRemoveRow";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                String styleClass = "";
                return styleClass;
            }
        });

        return grdItemDetail.getGrid();
    }
    //</editor-fold>

    private FlowPanel overHeadArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        flwTable.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
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
        lbl = new DmLabel("Bill Amount");
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
        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
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

}
