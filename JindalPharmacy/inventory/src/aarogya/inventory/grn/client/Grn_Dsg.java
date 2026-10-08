package aarogya.inventory.grn.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import aarogya.inventory.grn.shared.Find_Mdl;
import aarogya.inventory.grn.shared.GrdGrnDetail_Mdl;
import aarogya.inventory.grn.shared.TxtHSupplier_Mdl;
import aarogya.inventory.grn.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.taxSummary.client.TaxSummary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
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
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.grn.shared.TxtHGateEntry_Mdl;
import aarogya.inventory.grn.shared.TxtHTransporter_Mdl;
import aarogya.selfToolkit.serviceGrid.client.ServiceGrid;
import aarogya.selfToolkit.stStatic.shared.STEnum;

abstract class Grn_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.GRN.caption;
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
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);
    final DmTextBox txtChallanDate = new DmTextBox(11, true);
    final DmTextBox txtChallanNo = new DmTextBox(16, true);

    final DmTextBox txtEwayNo = new DmTextBox(20, false);
    final DmTextBox txtEwayDate = new DmTextBox(11, false);
    final DmTextBox txtLRNo = new DmTextBox(20, false);
    final DmTextBox txtLRDate = new DmTextBox(11, false);

    final FlowPanel flwAddOrder = new FlowPanel();
    final Button btnAddOrder = new Button("Add Order");
    final DmGUI gui = new DmGUI(topControl, 170);
    final TabLayoutPanel tabPanel1 = new TabLayoutPanel(1.6, Style.Unit.EM);
    final TabLayoutPanel tabPanel2 = new TabLayoutPanel(1.6, Style.Unit.EM);
    final TaxSummary taxSummary = new TaxSummary(625, 120,1);

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

    final ServiceGrid serviceDetailGrid = new ServiceGrid(625, 120);
    protected int colIndex = 0;

    //<editor-fold defaultstate="collapsed" desc="Help events">
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

    //    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
//    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);
//
//    private void iniTxtHDepartment() {
//        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
//        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
//        txtHDepartment.getHelper().addSort("name", DmSortType.ASC);
//    }
//    //</editor-fold>
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
        txtHSupplier.getDmGrid().initializeGrid(txtHSupplier.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHSupplier.getHelper().addSort("name", DmSortType.ASC);
        txtHSupplier.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("SupplierText")
        ValueProvider<Find_Mdl, String> supplierText();

        @Editor.Path("ChallanNo")
        ValueProvider<Find_Mdl, String> challanNo();

        @Editor.Path("ChallanDate")
        ValueProvider<Find_Mdl, String> challanDate();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "GRN No.", 120, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().supplierText(), "Supplier", 1, "supName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().challanNo(), "Ref. No.", 125, "pChallanNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().challanDate(), "Date", 80, "pChallanDate", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GrnDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdGrnDetail_Mdl> {

        @Editor.Path("PropertiesCsv")
        ValueProvider<GrdGrnDetail_Mdl, String> propertiesCsv();

        @Editor.Path("PoRecId")
        ValueProvider<GrdGrnDetail_Mdl, String> poRecId();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdGrnDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("ChallanQty")
        ValueProvider<GrdGrnDetail_Mdl, String> challanQty();

        @Editor.Path("AcceptQty")
        ValueProvider<GrdGrnDetail_Mdl, String> acceptQty();

        @Editor.Path("SkuName")
        ValueProvider<GrdGrnDetail_Mdl, String> skuName();

        @Editor.Path("SkuQty")
        ValueProvider<GrdGrnDetail_Mdl, String> skuQty();

        @Editor.Path("Rate")
        ValueProvider<GrdGrnDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdGrnDetail_Mdl, String> amount();

        @Editor.Path("DepartmentName")
        ValueProvider<GrdGrnDetail_Mdl, String> departmentName();

        @Editor.Path("IsLabRequired")
        ValueProvider<GrdGrnDetail_Mdl, String> isLabRequired();

        @Editor.Path("Hsn")
        ValueProvider<GrdGrnDetail_Mdl, String> hsn();

    }

    final DmGrid<GrdGrnDetail_Mdl, Grd_PrpAcs> grdGRNDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid iniGrdGRNDetail() {
        ColumnConfig<GrdGrnDetail_Mdl, String> selectCheckBox;
        grdGRNDetail.addRowNumberColumn();
        selectCheckBox = grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().isLabRequired(), "", 30, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().departmentName(), "Department", 140, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().name(), "Item Name", 200, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().propertiesCsv(), "Properties", 160, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().hsn(), "HSN", 85, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().poRecId(), "Order No.", 80, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().purchaseUnitName(), "Unit", 60, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().challanQty(), "Challan Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().acceptQty(), "Accept Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().skuName(), "SKU", 60, "");
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().skuQty(), "SKU Qty.", 65, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().rate(), "Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.addTextColumn(grdGRNDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdGRNDetail.initializeGrid(grdGRNDetail.getPropertyAccess().fieldHashCode(), 1247, 242);
        grdGRNDetail.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int isLabRequired = DmShared.vNull(grdGRNDetail.getGrid().getStore().get(context.getIndex()).getIsLabRequired()).intValue();
                boolean labStatus = grdGRNDetail.getGrid().getStore().get(context.getIndex()).isLabStatus();

                if (labStatus) {
                    sb.appendHtmlConstant("<span class = 'fa-labDone' style='font-size:1.3em;color:green;'></span>");
                } else {
                    if (isLabRequired == STEnum.LabRequired.NO.getId()) { //Cross beaker
                        sb.appendHtmlConstant("<span class = 'fa-labNotRequired' style='font-size:1.3em'></span>");
                    } else if (isLabRequired == STEnum.LabRequired.YES.getId()) { //Blank beaker
                        sb.appendHtmlConstant("<span class = 'fa-labNotDone' style='font-size:1.3em'></span>");
                    } else if (isLabRequired == STEnum.LabRequired.MAY_BE.getId()) { //May Be beaker
                        sb.appendHtmlConstant("<span class = 'fa-labMaybe' style='font-size:1.3em'></span>");
                    }
                }

            }
        });

        grdGRNDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdGrnDetail_Mdl>() {
            @Override
            public String getColStyle(GrdGrnDetail_Mdl model, ValueProvider<? super GrdGrnDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.iNull(model.getIsStatus()).intValue() == 1) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.iNull(model.getIsStatus()).intValue() == 2) {
                    styleClass += "dm-GridCellDairyConfirm ";
                } else if (DmShared.vNull(model.getBilledQty()) != 0) {
                    styleClass += "dm-GridCellDisable ";
                }
                if (model.isServiceRecord()) {
                    styleClass = "dm-GridRemoveRow";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdGrnDetail_Mdl model, int rowIndex) {
                String styleClass = "";
                return styleClass;
            }
        });

        return grdGRNDetail.getGrid();
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
    //<editor-fold defaultstate="collapsed" desc="txtHRCMApplicable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRCMApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHRCMApplicable() {
        txtHRCMApplicable.getDmGrid().addTextColumn(txtHRCMApplicable.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHRCMApplicable.getDmGrid().initializeGrid(txtHRCMApplicable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }//</editor-fold>
    
    public Grn_Dsg() {
        ini();
        iniFind();
        iniTxtHSupplier();
        iniTxtHVoucherType();
        iniTxtHTransporter();
        iniTxtHGateEntryNo();
        iniTxtHRCMApplicable();
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
        DmClient.setStyle(tabPanel1, "100%", "100%", "left", null, DmPosition.CENTER, true);

        tabPanel1.add(vl.add(tabGeneral()), "General");
        tabPanel1.add(vl.add(tabTermsCondition()), "Terms & Conditions");

        tabPanel1.selectTab(0);
        tabPanel1.ensureDebugId("cwTabPanel");

        return tabPanel1;
    }

    private TabLayoutPanel workingAreaBottomGrid() {
        DmClient.setStyle(tabPanel2, "100%", "147px", null, null, DmPosition.NONE, true);

        tabPanel2.add(vl.add(serviceDetailGrid), "Service");
        tabPanel2.add(vl.add(taxSummary), "Tax Summary");

        tabPanel2.selectTab(0);
        tabPanel2.ensureDebugId("cwTabPanel");

        return tabPanel2;
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
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.8%", "495px", "left", null, DmPosition.CENTER, true);

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.7%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(13, Style.Unit.PX);

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

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99%", "246px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdGRNDetail()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.7%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50.1%", "147px", "left", null, DmPosition.NONE, false);
        flwTd.add(workingAreaBottomGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", "147px", "right", null, DmPosition.NONE, false);
        flwTd.add(overHeadArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel overHeadArea() {
        FlowPanel flwTable, flwTr, flwTd, flwTemp;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", null, "right", null, DmPosition.NONE, true);

        //*************************<Left Side>****************************************
        flwTemp = new FlowPanel();
        DmClient.setStyle(flwTemp, "50%"  ,null , "left" , null , DmPosition.NONE, true);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Goods Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGoodsAmount));
        txtGoodsAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Service Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtServiceAmount));
        txtServiceAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Discount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDiscount));
        txtDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("Taxable Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTaxableAmt));
        txtTaxableAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSgstUgst));
        txtSgstUgst.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("CGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCGST));
        txtCGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        lbl = new DmLabel("IGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIGST));
        txtIGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        
        flwTemp.add(flwTr);
        flwTable.add(flwTemp);
     //*************************<Right Side>****************************************   
        
        flwTemp = new FlowPanel();
        DmClient.setStyle(flwTemp, "50%"  ,null , "left" , null , DmPosition.NONE, true);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("SubTotal");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSubTotal));
        txtSubTotal.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
//        flwTd.getElement().getStyle().setMarginLeft(110, Style.Unit.PX);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Final Discount");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFinalDiscountPer));
        txtFinalDiscountPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFinalDiscount));
        txtFinalDiscount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Bill Amount");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTotalAmount));
        txtTotalAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Round off");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRoundoff));
        txtRoundoff.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Net Amt");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetAmount));
        txtNetAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTemp.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Landed Value");
        lbl.getElement().getStyle().setMarginLeft(11, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLandedValue));
        txtLandedValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        
        flwTemp.add(flwTr);
        flwTable.add(flwTemp);
        
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
        lbl = new DmLabel("GRN Type");
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
        flwTd.add(vl.add(txtChallanNo));
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
        flwTd.add(vl.add(txtChallanDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Supplier");
        lbl.setTitle("Round Off - RO; Final Discount - FD");
        lbl.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSupplier));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("RCM");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRCMApplicable));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //=====================================================================
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gate No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHGateEntryNo);
        flwTr.add(flwTd);
                
        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transporter");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransporter));
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
        lbl = new DmLabel("GRN No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        DmClient.setStyle(flwTd, "19.4%", "20px", "left", null, DmPosition.NONE, false);
        btnAddOrder.addStyleName("dm-button-grad");
        btnAddOrder.setWidth("153px");
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
        lbl = new DmLabel("E-Way Bill No");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEwayNo));
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
        flwTd.add(vl.add(txtEwayDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("LR No");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLRNo));
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
        flwTd.add(vl.add(txtLRDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        return flwTable;
    }

}
