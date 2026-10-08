package aarogya.pharmacy.medicineOpening.client;

import aarogya.pharmacy.medicineOpening.shared.GrdMedicineDetail_Mdl;
import aarogya.pharmacy.medicineOpening.shared.Find_Mdl;
import aarogya.pharmacy.medicineOpening.shared.TxtHReceivedFrom_Mdl;
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
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class MedicineOpening_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().MEDICINE_OPENING.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String departmentEditableBlog = "Department Editable";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReceivedFrom">
    interface TxtHReceivedFrom_PrpAcs extends KeyField_T_PrpAcs<TxtHReceivedFrom_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHReceivedFrom_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHReceivedFrom_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHReceivedFrom_Mdl, TxtHReceivedFrom_PrpAcs> txtHReceivedFrom = new DmTextHelp(GWT.create(TxtHReceivedFrom_PrpAcs.class), 6, false);

    private void iniTxtHReceivedFrom() {
        txtHReceivedFrom.getDmGrid().addTextColumn(txtHReceivedFrom.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHReceivedFrom.getDmGrid().addTextColumn(txtHReceivedFrom.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHReceivedFrom.getDmGrid().addTextColumn(txtHReceivedFrom.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHReceivedFrom.getDmGrid().initializeGrid(txtHReceivedFrom.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHReceivedFrom.getHelper().addSort("name", DmSortType.ASC);
        txtHReceivedFrom.getHelper().addSort("cityName", DmSortType.ASC);
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

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("ReceivedBy")
        ValueProvider<Find_Mdl, String> receivedBy();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 130, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "v_Date", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().receivedBy(), "Received From", 1, "receivedByName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department In", 350, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public MedicineOpening_Dsg() {
        ini();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHReceivedFrom();
        iniTxtHDepartment();
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

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "480px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "50px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "50px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "425px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdItemDetail()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
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

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Received From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReceivedFrom));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "40px", null, null, DmPosition.CENTER, true);

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
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department In");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridMedicineDetail">
    interface GridMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDetail_Mdl> {

        @Editor.Path("TotalQuantity")
        ValueProvider<GrdMedicineDetail_Mdl, String> totalQuantity();

        @Editor.Path("SkuName")
        ValueProvider<GrdMedicineDetail_Mdl, String> skuName();

        @Editor.Path("Rate")
        ValueProvider<GrdMedicineDetail_Mdl, String> rate();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("PurchaseUnitQuantity")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseUnitQuantity();

        @Editor.Path("SkuValue")
        ValueProvider<GrdMedicineDetail_Mdl, String> skuValue();

        @Editor.Path("BatchNo")
        ValueProvider<GrdMedicineDetail_Mdl, String> batchNo();

        @Editor.Path("ExpiryDate")
        ValueProvider<GrdMedicineDetail_Mdl, String> expiryDate();

        @Editor.Path("Mrp")
        ValueProvider<GrdMedicineDetail_Mdl, String> mrp();

        @Editor.Path("SaleRate")
        ValueProvider<GrdMedicineDetail_Mdl, String> saleRate();

        @Editor.Path("RefNo")
        ValueProvider<GrdMedicineDetail_Mdl, String> refNo();

        @Editor.Path("RefDate")
        ValueProvider<GrdMedicineDetail_Mdl, String> refDate();

        @Editor.Path("Remark")
        ValueProvider<GrdMedicineDetail_Mdl, String> remark();

    }
    final DmGrid<GrdMedicineDetail_Mdl, GridMedicineDetail_PrpAcs> gridMedicineDetail = new DmGrid(GWT.create(GridMedicineDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridMedicineDetail.addRowNumberColumn();
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().name(), "Medicine Name", 250, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().batchNo(), "Batch No.", 80, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().totalQuantity(), "Total Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().skuName(), "SKU", 60, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().purchaseUnitQuantity(), "No. Of Pack", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().purchaseUnitName(), "Pack Unit", 60, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().rate(), "Rate/Pack", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().skuValue(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().expiryDate(), "Expiry Date", 75, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().mrp(), "MRP/Pack", 75, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().saleRate(), "Sale Rate/Pack", 80, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().refNo(), "Ref. No.", 75, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().refDate(), "Ref. Dt.", 75, "");
        gridMedicineDetail.addTextColumn(gridMedicineDetail.getPropertyAccess().remark(), "Remark", 90, "");

        gridMedicineDetail.initializeGrid(gridMedicineDetail.getPropertyAccess().fieldHashCode(), 1280, 400);

        return gridMedicineDetail.getGrid();
    }
    //</editor-fold>

}
