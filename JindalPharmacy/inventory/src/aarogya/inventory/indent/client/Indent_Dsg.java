package aarogya.inventory.indent.client;

import aarogya.inventory.indent.shared.Find_Mdl;
import aarogya.inventory.indent.shared.GrdItemDetail_Mdl;
import aarogya.inventory.indent.shared.TxtHRequisition_Mdl;
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
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.projectMenu.self.shared.pmInventory;

abstract class Indent_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.INDENT.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String approvedByBlog = "Approved By & Date";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtIndentNo = new DmTextBox(20, false);
    final DmTextBox txtHProcurementPlanNo = new DmTextBox(20, false);
    final DmTextBox txtWTACode = new DmTextBox(15, true);
    final Button btnApprovedBy = new Button();
    final DmTextBox txtIndentDate = new DmTextBox(11, true);
    final DmTextBox txtRemark = new DmTextBox(250, false);
    int rowIndexGrid, rowIndexRequisitionFor;

    //<editor-fold defaultstate="collapsed" desc="TxtHIndentType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIndentType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHIndentType() {
        txtHIndentType.getDmGrid().addTextColumn(txtHIndentType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHIndentType.getDmGrid().initializeGrid(txtHIndentType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHIndentType.getHelper().addSort("Description", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHDepartment.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHToDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHToDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHToDepartment() {
        txtHToDepartment.getDmGrid().addTextColumn(txtHToDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHToDepartment.getDmGrid().initializeGrid(txtHToDepartment.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHToDepartment.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHIndentType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIndentor = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHIndentor() {
        txtHIndentor.getDmGrid().addTextColumn(txtHIndentor.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHIndentor.getDmGrid().initializeGrid(txtHIndentor.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHIndentor.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHRequisition">
    interface DmRequisition_PrpAcs extends KeyField_T_PrpAcs<TxtHRequisition_Mdl> {

        @Editor.Path("Date")
        ValueProvider<TxtHRequisition_Mdl, String> date();

        @Editor.Path("Department")
        ValueProvider<TxtHRequisition_Mdl, String> department();

        @Editor.Path("RequisitionBy")
        ValueProvider<TxtHRequisition_Mdl, String> requisitionBy();
    }

    final DmTextHelp<TxtHRequisition_Mdl, DmRequisition_PrpAcs> txtHRequisition = new DmTextHelp(GWT.create(DmRequisition_PrpAcs.class), 4, false);

    private void iniTxtHRequisition() {
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().date(), "Date", 100, "v_Date");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().department(), "Department", 200, "departmentname");
        txtHRequisition.getDmGrid().addTextColumn(txtHRequisition.getDmGrid().getPropertyAccess().requisitionBy(), "Requisition By", 200, "subgroupName");
        txtHRequisition.getDmGrid().initializeGrid(txtHRequisition.getDmGrid().getPropertyAccess().code(), 600, 0, 1);

        txtHRequisition.getHelper().addSort("recid", DmSortType.ASC);
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("IndentType")
        ValueProvider<Find_Mdl, String> indentType();

        @Editor.Path("Department")
        ValueProvider<Find_Mdl, String> department();

        @Editor.Path("Indentor")
        ValueProvider<Find_Mdl, String> indentor();

        @Editor.Path("Requisition")
        ValueProvider<Find_Mdl, String> requisition();

        @Editor.Path("IndentDate")
        ValueProvider<Find_Mdl, String> indentDate();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Indent No.", 150, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().indentDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().indentType(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department", 230, "departmentName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().indentor(), "Indentor", 1, "indentorName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().requisition(), "Requisition", 250, "reqName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 130, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 0, 5);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC, "v_No");
        dmFind.getHelper().addSort("recid", DmSortType.ASC);
    }
//</editor-fold>

    public Indent_Dsg() {
        ini();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHIndentType();
        iniTxtHDepartment();
        iniTxtHToDepartment();
        iniTxtHIndentor();
        iniTxtHRequisition();
        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");

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

        flwForm.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98.5%", "473px", null, null, DmPosition.CENTER, true);

        //entryArea
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "75px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        //Grid Area
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "394px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdMedicineDetail()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "76px", null, null, DmPosition.CENTER, true);

        //entryArea
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "76px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "76px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea1());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "76px", "left", null, DmPosition.NONE, true);
        flwTd.add(entryArea2());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel entryArea1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "76px", null, null, DmPosition.CENTER, true);

        //Name
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIndentType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Indent No
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indent No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIndentNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);

        lbl = new DmLabel("Indent Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIndentDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Department
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);

        lbl = new DmLabel("To Department");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHToDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea2() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "76px", null, null, DmPosition.CENTER, true);

        //Indentor
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Indentor");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIndentor));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Requisition
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Requisition");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisition));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Procurement No.");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHProcurementPlanNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Remark
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdItemDetail">
    interface GrdItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("Qty")
        ValueProvider<GrdItemDetail_Mdl, String> qty();

        @Editor.Path("RequisitionFor")
        ValueProvider<GrdItemDetail_Mdl, String> requisitionFor();

        @Editor.Path("UnitName")
        ValueProvider<GrdItemDetail_Mdl, String> unitName();

        @Editor.Path("Size")
        ValueProvider<GrdItemDetail_Mdl, String> size();

        @Editor.Path("Color")
        ValueProvider<GrdItemDetail_Mdl, String> color();

        @Editor.Path("Properties")
        ValueProvider<GrdItemDetail_Mdl, String> properties();

        @Editor.Path("ReqDate")
        ValueProvider<GrdItemDetail_Mdl, String> reqDate();

        @Editor.Path("Remarks")
        ValueProvider<GrdItemDetail_Mdl, String> remarks();

    }
    final DmGrid<GrdItemDetail_Mdl, GrdItemDetail_PrpAcs> grdItemDetail = new DmGrid(com.google.gwt.core.shared.GWT.create(GrdItemDetail_PrpAcs.class));

    public Grid iniGrdMedicineDetail() {
        grdItemDetail.addRowNumberColumn();
        rowIndexGrid = 1;
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().name(), "Item Name", 1, "");
        ++rowIndexGrid;
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().properties(), "Properties", 230, "");
        ++rowIndexGrid;
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().qty(), "Quantity", 110, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++rowIndexGrid;
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().unitName(), "SKU", 50, "");
        ++rowIndexGrid;
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().reqDate(), "Req. Date", 85, "");
        ++rowIndexGrid;
        grdItemDetail.addTextColumn(grdItemDetail.getPropertyAccess().remarks(), "Remark", 270, "");
        grdItemDetail.initializeGrid(grdItemDetail.getPropertyAccess().fieldHashCode(), 1260, 398, 2);

        grdItemDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdItemDetail_Mdl>() {
            @Override
            public String getColStyle(GrdItemDetail_Mdl model, ValueProvider<? super GrdItemDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getIsStatus()).equalsIgnoreCase("Cancelled")) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.xNull(model.getIsStatus()).equalsIgnoreCase("Completed")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdItemDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdItemDetail.getGrid();
    }
//</editor-fold>

}
