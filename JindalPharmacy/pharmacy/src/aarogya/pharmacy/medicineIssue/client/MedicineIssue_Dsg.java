package aarogya.pharmacy.medicineIssue.client;

import aarogya.pharmacy.medicineIssue.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineIssue_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_ISSUE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String departmentEditableBlog = "Department Editable";
    public final static String addBatchBlog = "Add Batch";
    public final static String dateEditableBlog = "Date Editable";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);

    final DmTextBox txtRefDate = new DmTextBox(11, false);
    final DmTextBox txtRefNo = new DmTextBox(20, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox();
    final DmCheckBox chkExpiryItem = new DmCheckBox();
    final DmLabel lblExpiryMedicine = new DmLabel("Expired Medicines");
    DmLabel lblTotalPurchseValue = new DmLabel("-");
    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIssueTo">
    interface TxtHSupplier_PrpAcs extends KeyField_T_PrpAcs<TxtHIssueTo_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHIssueTo_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHIssueTo_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHIssueTo_Mdl, TxtHSupplier_PrpAcs> txtHIssueTo = new DmTextHelp(GWT.create(TxtHSupplier_PrpAcs.class), 6, false);

    private void iniTxtHIssueTo() {
        txtHIssueTo.getDmGrid().addTextColumn(txtHIssueTo.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHIssueTo.getDmGrid().addTextColumn(txtHIssueTo.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHIssueTo.getDmGrid().addTextColumn(txtHIssueTo.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHIssueTo.getDmGrid().initializeGrid(txtHIssueTo.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHIssueTo.getHelper().addSort("name", DmSortType.ASC);
        txtHIssueTo.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRequisition">
    interface TxtHRequisition_PrpAcs extends KeyField_T_PrpAcs<TxtHRequisitionNo_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<TxtHRequisitionNo_Mdl, String> department();

        @Editor.Path("RequisitionBy")
        ValueProvider<TxtHRequisitionNo_Mdl, String> requisitionBy();

        @Editor.Path("RequisitionDate")
        ValueProvider<TxtHRequisitionNo_Mdl, String> dateReq();

    }
    final DmTextHelp<TxtHRequisitionNo_Mdl, TxtHRequisition_PrpAcs> txtHRequisition = new DmTextHelp(GWT.create(TxtHRequisition_PrpAcs.class), 7, false);

    private void iniTxtHRequisition() {
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().name(), "Requisition", 0, "recId");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().dateReq(), "Date", 75, "v_Date", DmDataType.DATE);
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().requisitionBy(), "Requisition By", 100, "requestedByName");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().department(), "Department By", 100, "department");
        txtHRequisition.getDmGrid().initializeGrid(txtHRequisition.getDmGrid().getPropertyAccess().code(), 375, 0, 1);

        txtHRequisition.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHRequisition.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHRequisition.getHelper().addSort("v_No", DmSortType.DESC);
        txtHRequisition.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("IssueTo")
        ValueProvider<Find_Mdl, String> issueTo();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Issue No.", 130, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().issueTo(), "Issue To", 1, "issueToName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department From", 350, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>

    public MedicineIssue_Dsg() {
        ini();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHIssueTo();
        iniTxtHDepartment();
        iniTxtHRequisition();
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
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "472px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "70px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "70px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "70px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "338px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdMedicineDetail()));
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Total Purchase Value");
        lbl.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        flwTd.add(lbl);
        flwTd.getElement().getStyle().setMarginLeft(41, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lblTotalPurchseValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblTotalPurchseValue.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblTotalPurchseValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.add(lblTotalPurchseValue);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consumption Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        lbl = new DmLabel("Department From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Issue To");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIssueTo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        lbl = new DmLabel("Ref. Date.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "60px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consumption No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtVoucherDate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "43%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisition));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "2%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(chkExpiryItem);
        chkExpiryItem.setTitle("Expiry Medicine");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(lblExpiryMedicine));
        lblExpiryMedicine.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblExpiryMedicine.getElement().getStyle().setMarginLeft(4, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridMedicineDetail">
    interface GridMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDetail_Mdl> {

        @Editor.Path("BatchNo")
        ValueProvider<GrdMedicineDetail_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdMedicineDetail_Mdl, String> expiryDate();

        @Editor.Path("Mrp")
        ValueProvider<GrdMedicineDetail_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<GrdMedicineDetail_Mdl, String> saleRate();

        @Editor.Path("Quantity")
        ValueProvider<GrdMedicineDetail_Mdl, String> quantity();

        @Editor.Path("SkuName")
        ValueProvider<GrdMedicineDetail_Mdl, String> skuName();

        @Editor.Path("RequisitionNo")
        ValueProvider<GrdMedicineDetail_Mdl, String> requisitionNo();

        @Editor.Path("Remark")
        ValueProvider<GrdMedicineDetail_Mdl, String> remark();

        @Editor.Path("PurchaseValue")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseValue();

    }
    final DmGrid<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> gridMedicineDetail = new DmGrid(GWT.create(GridMedicineDetail_PrpAcs.class));

    public Grid iniGrdMedicineDetail() {
        gridMedicineDetail.addRowNumberColumn();
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().name(), "Medicine Name", 300, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().batchNo(), "Batch No.", 80, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().expiryDate(), "Expiry Date", 80, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().mrp(), "MRP", 60, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().saleRate(), "Sale Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().purchaseValue(), "Purchase Value", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().quantity(), "Issue Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().skuName(), "SKU", 50, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().requisitionNo(), "Requisition No.", 120, "");     
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().remark(), "Remark", 1, "");
        gridMedicineDetail.initializeGrid(gridMedicineDetail.getPropertyAccess().fieldHashCode(), 1280, 338, 11);
        return gridMedicineDetail.getGrid();
    }
    //</editor-fold>

}
