package aarogya.pharmacy.medicineTransfer.client;

import aarogya.pharmacy.medicineTransfer.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineTransfer.shared.Find_Mdl;
import aarogya.pharmacy.medicineTransfer.shared.TxtHRequisitionNo_Mdl;
import aarogya.pharmacy.medicineTransfer.shared.TxtHTransferTo_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineTransfer_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_TRANSFER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By & Date";
    public final static String departmentEditableBlog = "Department Editable";
    public final static String dateEditableBlog = "Date Editable";
    public final static String addBatchBlog = "Add Batch";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtWTACode = new DmTextBox(15, true);
    final DmTextBox txtRefNo = new DmTextBox(20, false);
    final DmTextBox txtRefDate = new DmTextBox(11, false);

    final Button btnApprovedBy = new Button();
    DmLabel lblTotalPurchseValue = new DmLabel("-");

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentFrom">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentFrom = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartmentFrom() {
        txtHDepartmentFrom.getDmGrid().addTextColumn(txtHDepartmentFrom.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartmentFrom.getDmGrid().initializeGrid(txtHDepartmentFrom.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartmentFrom.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentTo">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentTo = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartmentTo() {
        txtHDepartmentTo.getDmGrid().addTextColumn(txtHDepartmentTo.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartmentTo.getDmGrid().initializeGrid(txtHDepartmentTo.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartmentTo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("RefNo")
        ValueProvider<Find_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<Find_Mdl, String> refDate();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 140, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refNo(), "Ref. No.", 1, "refNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().refDate(), "Ref Date", 85, "refDate", DmDataType.DATE);
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.DESC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHTransferTo">
    interface TxtHTransferTo_PrpAcs extends KeyField_T_PrpAcs<TxtHTransferTo_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHTransferTo_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHTransferTo_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHTransferTo_Mdl, TxtHTransferTo_PrpAcs> txtHTransferTo = new DmTextHelp(GWT.create(TxtHTransferTo_PrpAcs.class), 6, false);

    private void iniTxtHTransferTo() {
        txtHTransferTo.getDmGrid().addTextColumn(txtHTransferTo.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTransferTo.getDmGrid().addTextColumn(txtHTransferTo.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHTransferTo.getDmGrid().addTextColumn(txtHTransferTo.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHTransferTo.getDmGrid().initializeGrid(txtHTransferTo.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHTransferTo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHTransferTo.getHelper().addSort("cityName", DmSortType.ASC);
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
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().name(), "Requisition", 120, "recId");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().dateReq(), "Date", 75, "v_Date", DmDataType.DATE);
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().requisitionBy(), "Requisition By", 120, "requestedByName");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().department(), "Department By", 120, "department");
        txtHRequisition.getDmGrid().initializeGrid(txtHRequisition.getDmGrid().getPropertyAccess().code(), 500, 0, 3);

        txtHRequisition.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHRequisition.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHRequisition.getHelper().addSort("v_No", DmSortType.DESC);
        txtHRequisition.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    public MedicineTransfer_Dsg() {
        ini();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHDepartmentFrom();
        iniTxtHDepartmentTo();
        iniTxtHTransferTo();
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
//        userControl.addTextBlog(preparedByBlog, 100, false, true);
//        userControl.addGap(100);
//        userControl.addTextBlog(modifiedByBlog, 100, true, false);
//        return userControl;
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(approvedByBlog, 200, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnApprovedBy.addStyleName("dm-button-grad");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        vrtInnerForm.add(userControl);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "502px", null, null, DmPosition.CENTER, true);

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
        DmClient.setStyle(flwTr, "100%", "402px", "left", null, DmPosition.NONE, true);
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
        lbl = new DmLabel("Department From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentFrom));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To ");
        lbl.getElement().getStyle().setMarginLeft(35, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentTo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transfer To");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransferTo));
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
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtVoucherDate);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefDate));
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
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisition));
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("");
//        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
//        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
//        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridMedicineDetail">
    interface GridMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDetail_Mdl> {

        @Editor.Path("Quantity")
        ValueProvider<GrdMedicineDetail_Mdl, String> quantity();

        @Editor.Path("SkuName")
        ValueProvider<GrdMedicineDetail_Mdl, String> skuName();

        @Editor.Path("BatchNo")
        ValueProvider<GrdMedicineDetail_Mdl, String> batchNo();

        @Editor.Path("RequisitionNo")
        ValueProvider<GrdMedicineDetail_Mdl, String> requisitionNo();

        @Editor.Path("Remark")
        ValueProvider<GrdMedicineDetail_Mdl, String> remark();

        @Editor.Path("FromDeptStk")
        ValueProvider<GrdMedicineDetail_Mdl, String> fromDeptStk();

        @Editor.Path("SaleRate")
        ValueProvider<GrdMedicineDetail_Mdl, String> saleRate();
        
        @Editor.Path("PurchaseValue")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseValue();

    }
    final DmGrid<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> gridMedicineDetail = new DmGrid(GWT.create(GridMedicineDetail_PrpAcs.class));

    public Grid iniGrdMedicineDetail() {
        gridMedicineDetail.addRowNumberColumn();
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().name(), "Medicine Name", 1, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().quantity(), "Qty.", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().skuName(), "Unit", 120, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().saleRate(), "Sale Rate", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().purchaseValue(), "Purchase Value", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().batchNo(), "Batch No.", 120, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().requisitionNo(), "Requisition No.", 120, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().remark(), "Remark", 250, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().fromDeptStk(), "Fr.Stk.", 50, "");

        gridMedicineDetail.initializeGrid(gridMedicineDetail.getPropertyAccess().fieldHashCode(), 1280, 402, 2);

        return gridMedicineDetail.getGrid();
    }
    //</editor-fold>

}
