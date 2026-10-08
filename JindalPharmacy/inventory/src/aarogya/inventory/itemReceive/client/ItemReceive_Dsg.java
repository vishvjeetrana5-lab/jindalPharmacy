package aarogya.inventory.itemReceive.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.itemReceive.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemReceive.shared.Find_Mdl;
import aarogya.inventory.itemReceive.shared.TxtHReceivedFrom_Mdl;
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
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ItemReceive_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.ITEM_RECEIVE.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String departmentEditableBlog = "Department Editable";
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

    //<editor-fold defaultstate="collapsed" desc="Help Events">
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
        txtHReceivedFrom.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHReceivedFrom.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

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

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Receive No.", 150, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 90, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().receivedBy(), "Received From", 1, "receivedByName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Godown In", 450, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ItemReceive_Dsg() {
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
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, true, false);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "465px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.4%", "64px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);

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
        DmClient.setStyle(flwTr, "99%", "401px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTr.add(vl.add(iniGrdItemDetail()));
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
        lbl = new DmLabel("Receive Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
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
        lbl = new DmLabel("Receive No.");
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
        lbl = new DmLabel("Godown In");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
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

    //<editor-fold defaultstate="collapsed" desc="gridItemDetail">
    interface GridItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("PropertyDisplay")
        ValueProvider<GrdItemDetail_Mdl, String> propertyDisplay();

        @Editor.Path("Quantity")
        ValueProvider<GrdItemDetail_Mdl, String> quantity();

        @Editor.Path("SkuName")
        ValueProvider<GrdItemDetail_Mdl, String> skuName();

        @Editor.Path("Totalpacks")
        ValueProvider<GrdItemDetail_Mdl, String> totalPacks();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("Amount")
        ValueProvider<GrdItemDetail_Mdl, String> amount();

        @Editor.Path("BatchNo")
        ValueProvider<GrdItemDetail_Mdl, String> batchNo();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Item Name", 300, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().propertyDisplay(), "Properties", 200, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().quantity(), "Qty.", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().skuName(), "SKU", 50, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().totalPacks(), "No. Of Pack", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().purchaseUnitName(), "Pack Unit", 60, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().rate(), "Rate/Pack", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().amount(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().remark(), "Remark", 260, "");

        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1256, 401);

        return gridItemDetail.getGrid();
    }
    //</editor-fold>

}
