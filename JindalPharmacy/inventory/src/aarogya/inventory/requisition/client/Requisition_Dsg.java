package aarogya.inventory.requisition.client;

import aarogya.inventory.requisition.shared.TxtHPriority_Mdl;
import aarogya.inventory.requisition.shared.GrdRequisition_Mdl;
import aarogya.inventory.requisition.shared.TxtHRequisition_Mdl;
import aarogya.inventory.requisition.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmInventory;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.shared.GWT;
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

abstract class Requisition_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.REQUISITION.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String approvedByBlog = "Approved By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final Button btnApprovedBy = new Button();
    final DmTextBox txtWTACode = new DmTextBox(15, true);
    final DmTextBox txtRequisionNo = new DmTextBox(20, false);
    final DmTextBox txtVDate = new DmTextBox(20, true);
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final DmTextBox lblUEndDTLatest = new DmTextBox();
    final DmTextBox txtSiteName = new DmTextBox(100, false);

    //<editor-fold defaultstate="collapsed" desc="TxtHAddPriority">
    interface TxtHAddPriority_PrpAcs extends KeyField_T_PrpAcs<TxtHPriority_Mdl> {

        @Editor.Path("PriorityFullName")
        ValueProvider<TxtHPriority_Mdl, String> priorityFullName();

    }
    final DmTextHelp<TxtHPriority_Mdl, TxtHAddPriority_PrpAcs> txtHPriority = new DmTextHelp(com.google.gwt.core.client.GWT.create(TxtHAddPriority_PrpAcs.class), 7, false);

    private void iniTxtHAddPriority() {
        txtHPriority.getDmGrid().addTextColumn(txtHPriority.getDmGrid().getPropertyAccess().name(), "Short Name", 0, "shortName");
        txtHPriority.getDmGrid().addTextColumn(txtHPriority.getDmGrid().getPropertyAccess().priorityFullName(), "Name", 300, "NAME");
        txtHPriority.getDmGrid().initializeGrid(txtHPriority.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHPriority.getHelper().addSort("shortName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHRequisitionType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRequisitionType = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHRequisitionType() {
        txtHRequisitionType.getDmGrid().addTextColumn(txtHRequisitionType.getDmGrid().getPropertyAccess().name(), "Name", 0, "Description");
        txtHRequisitionType.getDmGrid().initializeGrid(txtHRequisitionType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHRequisitionType.getHelper().addSort("Description", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentBy = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniTxtHDepartmentBy() {
        txtHDepartmentBy.getDmGrid().addTextColumn(txtHDepartmentBy.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartmentBy.getDmGrid().initializeGrid(txtHDepartmentBy.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHDepartmentBy.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentTo">
    interface DmDepartment_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    final DmTextHelp<KeyFieldExtended, DmDepartment_PrpAcs> txtHDepartmentTo = new DmTextHelp(GWT.create(DmDepartment_PrpAcs.class), 5, true);

    public void iniTxtHDepartmentTo() {
        txtHDepartmentTo.getDmGrid().addTextColumn(txtHDepartmentTo.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHDepartmentTo.getDmGrid().addTextColumn(txtHDepartmentTo.getDmGrid().getPropertyAccess().sharedModifiedBy(), "Site", 100, "siteName");
        txtHDepartmentTo.getDmGrid().initializeGrid(txtHDepartmentTo.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHDepartmentTo.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHSubGroup">

    interface TxtHRequisition_PrpAcs extends KeyField_T_PrpAcs<TxtHRequisition_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<TxtHRequisition_Mdl, String> departmentName();

    }
    final DmTextHelp<TxtHRequisition_Mdl, TxtHRequisition_PrpAcs> txtHSubGroup = new DmTextHelp(GWT.create(TxtHRequisition_PrpAcs.class), 5, true);

    public void iniTxtHSubGroup() {
        txtHSubGroup.getDmGrid().addTextColumn(txtHSubGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSubGroup.getDmGrid().initializeGrid(txtHSubGroup.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHSubGroup.getHelper().addSort("NAME", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface Find_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("RequisitionNo")
        ValueProvider<Find_Mdl, String> requisitionNo();

        @Editor.Path("Date")
        ValueProvider<Find_Mdl, String> date();

        @Editor.Path("DepartmentBy")
        ValueProvider<Find_Mdl, String> departmentBy();

        @Editor.Path("DepartmentTo")
        ValueProvider<Find_Mdl, String> departmentTo();

        @Editor.Path("SubName")
        ValueProvider<Find_Mdl, String> subName();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();

    }

    final DmFind<Find_Mdl, Find_PrpAcs> dmFind = new DmFind(GWT.create(Find_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().requisitionNo(), "Requisition No.", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 100, "replace(convert(varchar, v_Date, 106), ' ','-')");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().subName(), "Requisition By", 300, "subgroup");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentBy(), "Department By", 150, "departmentByName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentTo(), "Department To", 150, "departmentToName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 130, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 958, 1);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Grd">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRequisition_Mdl> {

        @Editor.Path("UnitName")
        ValueProvider<GrdRequisition_Mdl, String> unitName();

        @Editor.Path("Date")
        ValueProvider<GrdRequisition_Mdl, String> date();

        @Editor.Path("Remark")
        ValueProvider<GrdRequisition_Mdl, String> remark();
        
        @Editor.Path("ItemCurrPosition")
        ValueProvider<GrdRequisition_Mdl, String> itemCurrPosition();

        @Editor.Path("Qty")
        ValueProvider<GrdRequisition_Mdl, String> qty();

        @Editor.Path("ColourName")
        ValueProvider<GrdRequisition_Mdl, String> colorName();

        @Editor.Path("Properties")
        ValueProvider<GrdRequisition_Mdl, String> properties();

        @Editor.Path("SizeName")
        ValueProvider<GrdRequisition_Mdl, String> sizeName();

        @Editor.Path("Approval")
        ValueProvider<GrdRequisition_Mdl, String> approval();

    }
    final DmGrid<GrdRequisition_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(GWT.create(Grd_PrpAcs.class));

    private Grid createGrid() {
        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().name(), "Item Name", 0, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().properties(), "Properties", 150, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().qty(), "Req. Qty.", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().unitName(), "Unit", 70, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().date(), "Req. Date", 80, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().remark(), "Remark", 200, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().itemCurrPosition(), "Curr. Position", 176, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1268, 400, 2);
        dmGrd.getGrid().getView().setViewConfig(new GridViewConfig<GrdRequisition_Mdl>() {
            @Override
            public String getColStyle(GrdRequisition_Mdl model, ValueProvider<? super GrdRequisition_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdRequisition_Mdl model, int rowIndex) {
                return "";
            }
        });

        return dmGrd.getGrid();
    }
//</editor-fold>

    public Requisition_Dsg() {
        ini();
        iniTxtHDepartmentBy();
        iniTxtHDepartmentTo();
        iniTxtHSubGroup();
        iniTxtHAddPriority();
        iniTxtHRequisitionType();
        iniFind();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

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
        FlowPanel flwTable;
        FlowPanel flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "476px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "70px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "405px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(createGrid()));
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "65px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", "65px", "left", null, DmPosition.NONE, false);
        flwTd.add(topLeftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", "65px", "left", null, DmPosition.NONE, false);
        flwTd.add(topRightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel topLeftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Requisition Type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisitionType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Requisition No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequisionNo));
        txtRequisionNo.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        lblTemp.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Requisition By");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSubGroup));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel topRightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department By");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentBy));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Priority");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPriority));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department To");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentTo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site To");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSiteName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Remark");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
