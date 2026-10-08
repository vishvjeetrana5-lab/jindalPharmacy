package aarogya.inventory.physicalStock.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.physicalStock.shared.GrdItemDetail_Mdl;
import aarogya.inventory.physicalStock.shared.Find_Mdl;
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
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PhysicalStock_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.PHYSICAL_STOCK.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String departmentEditableBlog = "Department Editable";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtVoucherDate = new DmTextBox(11, true);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmCheckBox cbAllowDuplicate = new DmCheckBox();

    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHFilterType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFilterType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);

    private void iniTxtHFilterType() {
        txtHFilterType.getDmGrid().addTextColumn(txtHFilterType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHFilterType.getDmGrid().initializeGrid(txtHFilterType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHFilterType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHFilterValue">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHFilterValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHFilterValue() {
        txtHFilterValue.getDmGrid().addTextColumn(txtHFilterValue.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHFilterValue.getDmGrid().initializeGrid(txtHFilterValue.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHFilterValue.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("VoucherType")
        ValueProvider<Find_Mdl, String> voucherType();

        @Editor.Path("VoucherDate")
        ValueProvider<Find_Mdl, String> voucherDate();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 19);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 130, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Godown", 350, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PhysicalStock_Dsg() {
        ini();
        iniFind();
        iniTxtHDepartment();
        iniTxtHFilterType();
        iniTxtHFilterValue();
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
        DmClient.setStyle(flwTable, "100%", null, "left", null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(topLeftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, true);
        flwTd.add(topRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdItemDetail()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("Style", "width: 98%; float: left; padding: 1%; margin-top: 2px;");

        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Godown");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filter Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFilterType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        flwTable.getElement().setAttribute("Style", "width: 98%; float: left; padding: 1%; margin-top: 2px;");
        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("Style", "width: 31%; float: right; margin-left: 10px; margin-bottom: 1px; margin-top: 2px;");
        flwTd.add(cbAllowDuplicate);
        lbl = new DmLabel("Allow Duplicate Items");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filter Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHFilterValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

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

        @Editor.Path("Rate")
        ValueProvider<GrdItemDetail_Mdl, String> rate();

        @Editor.Path("PurchaseUnitName")
        ValueProvider<GrdItemDetail_Mdl, String> purchaseUnitName();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Item Name", 400, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().propertyDisplay(), "Properties", 150, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().rate(), "Rate/pack", 80, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().purchaseUnitName(), "Pack Unit", 80, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().quantity(), "Quantity", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().skuName(), "SKU", 50, "");
        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1280, 380, 3);
        return gridItemDetail.getGrid();
    }
    //</editor-fold>

}
