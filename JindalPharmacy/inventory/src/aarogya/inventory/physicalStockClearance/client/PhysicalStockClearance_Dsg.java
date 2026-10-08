package aarogya.inventory.physicalStockClearance.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.physicalStockClearance.shared.Find_Mdl;
import aarogya.inventory.physicalStockClearance.shared.TxtHDateOn_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PhysicalStockClearance_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.PHYSICAL_STOCK_CLEARANCE.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String postedByBlog = "Posted By & Date";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtVoucherNo = new DmTextBox(20, false);
    final DmTextBox txtDepartment = new DmTextBox();
    final DmTextBox txtFilterType = new DmTextBox();
    final DmTextBox txtFilterValue = new DmTextBox();
    final Button btnPostedBy = new Button();

    //<editor-fold defaultstate="collapsed" desc="txtHVoucherDate">
    interface TxtHDateOn_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHDateOn_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<TxtHDateOn_Mdl, String> departmentName();

        @Editor.Path("FilterTypeName")
        ValueProvider<TxtHDateOn_Mdl, String> filterTypeName();

        @Editor.Path("FilterValueName")
        ValueProvider<TxtHDateOn_Mdl, String> filterValueName();
    }

    final DmTextHelp<TxtHDateOn_Mdl, TxtHDateOn_PrpAcs> txtHVoucherDate = new DmTextHelp(GWT.create(TxtHDateOn_PrpAcs.class), 6, true);

    private void iniTxtHDateOn() {
        txtHVoucherDate.getDmGrid().addTextColumn(txtHVoucherDate.getDmGrid().getPropertyAccess().name(), "Date", 100, "REPLACE(CONVERT(varchar, date,106), ' ', '-')");
        txtHVoucherDate.getDmGrid().addTextColumn(txtHVoucherDate.getDmGrid().getPropertyAccess().departmentName(), "Godown", 180, "departmentName");
        txtHVoucherDate.getDmGrid().addTextColumn(txtHVoucherDate.getDmGrid().getPropertyAccess().filterTypeName(), "Filter Type", 140, "filterTypeName");
        txtHVoucherDate.getDmGrid().addTextColumn(txtHVoucherDate.getDmGrid().getPropertyAccess().filterValueName(), "Filter Value", 180, "filterValueName");
        txtHVoucherDate.getDmGrid().initializeGrid(txtHVoucherDate.getDmGrid().getPropertyAccess().fieldHashCode(), 600, 0, 2);
        txtHVoucherDate.getHelper().addSort("date", DmEnum.DmSortType.DESC);
        txtHVoucherDate.getHelper().addSort("departmentName", DmEnum.DmSortType.ASC);
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

        @Editor.Path("Posting")
        ValueProvider<Find_Mdl, String> posting();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 10);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Voucher No.", 130, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Godown", 100, "departmentName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().posting(), "Posting", 300, "postedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 5);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public PhysicalStockClearance_Dsg() {
        ini();
        iniFind();
        iniTxtHDateOn();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(topControl);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(addUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl addUserControl() {
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(postedByBlog, 200, true, false);
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        btnPostedBy.addStyleName("dm-button-grad");
        btnPostedBy.getElement().appendChild(aBtn.getElement());
        btnPostedBy.setWidth("30px");
        userControl.addControlBlog(postedByBlog, btnPostedBy);
        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "50px", null, null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date On");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No.");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVoucherNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Godown");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDepartment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filter Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFilterType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 1st Row >====================================//        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Filter Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFilterValue));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

}
