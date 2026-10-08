package aarogya.pharmacy.requisition.client;

import aarogya.pharmacy.requisition.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class Requisition_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().REQUISITION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmTextBox txtWTACode = new DmTextBox(15, true);

    final Button btnApprovedBy = new Button();
    final DmTextBox txtRequisionNo = new DmTextBox(20, false);
    final DmTextBox txtVDate = new DmTextBox(20, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);
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
        txtHRequisitionType.getDmGrid().initializeGrid(txtHRequisitionType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHRequisitionType.getHelper().addSort("Description", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartmentBy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentBy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

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
    final DmTextHelp<KeyFieldExtended, DmDepartment_PrpAcs> txtHDepartmentTo = new DmTextHelp(GWT.create(DmDepartment_PrpAcs.class), 10, true);

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
        txtHSubGroup.getDmGrid().initializeGrid(txtHSubGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

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
        ValueProvider<Find_Mdl, String> department();

        @Editor.Path("DepartmentTo")
        ValueProvider<Find_Mdl, String> departmentTo();

        @Editor.Path("SubName")
        ValueProvider<Find_Mdl, String> subName();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();

    }

    final DmFind<Find_Mdl, Find_PrpAcs> dmFind = new DmFind(GWT.create(Find_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().requisitionNo(), "Requisition No.", 100, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().date(), "Date", 100, "v_date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().subName(), "Requisition By", 0, "subgroup");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department By", 150, "departmentByName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentTo(), "Department To", 150, "departmentToName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 130, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 958, 3);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC, "v_No");
        dmFind.getHelper().addSort("recid", DmSortType.ASC);
    }
    //</editor-fold>

    public Requisition_Dsg() {
        ini();
        iniTxtHDepartmentBy();
        iniTxtHDepartmentTo();
        iniTxtHSubGroup();
        iniTxtHRequisitionType();
        iniFind();
        iniTxtHAddPriority();
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

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addTextBlog(modifiedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(approvedByBlog, 100, true, false);
        Anchor aBtn = new Anchor();
        aBtn.addStyleName("fa fa-thumbs-o-up");
        aBtn.getElement().getStyle().setColor("green");
        btnApprovedBy.getElement().appendChild(aBtn.getElement());
        btnApprovedBy.setWidth("30px");
        userControl.addControlBlog(approvedByBlog, btnApprovedBy);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable;
        FlowPanel flwTr, flwTD;
        CaptionPanel cPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "450px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "65px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "10px", "left", null, DmPosition.NONE, true);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGrdMedicineDetail()));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10.5%", "25px", "left", null, DmPosition.NONE, false);
        DmLabel lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PX);
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "88.8%", "25px", "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRemark));
        flwTr.add(flwTD);
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "70px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "70px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "65px", "left", null, DmPosition.NONE, false);
        flwTd.add(leftArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", "70px", "left", null, DmPosition.NONE, false);
        flwTd.add(middleArea());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", "70px", "left", null, DmPosition.NONE, false);
        flwTd.add(rightArea());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Requisition Type");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisitionType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Requisition No.");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRequisionNo));
        txtRequisionNo.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        lblTemp.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Requisition By");
        lblTemp.getElement().getStyle().setMarginLeft(6, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSubGroup));
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel middleArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department By");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentBy));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Department To");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartmentTo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "65px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Priority");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPriority));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site To");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSiteName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="grdMedicineDetail">
    interface GrdMedicineDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdMedicineDetail_Mdl> {

        @Editor.Path("SkuName")
        ValueProvider<GrdMedicineDetail_Mdl, String> skuName();

        @Editor.Path("Date")
        ValueProvider<GrdMedicineDetail_Mdl, String> date();

        @Editor.Path("Remark")
        ValueProvider<GrdMedicineDetail_Mdl, String> remark();

        @Editor.Path("ItemCurrPosition")
        ValueProvider<GrdMedicineDetail_Mdl, String> itemCurrPosition();

        @Editor.Path("Qty")
        ValueProvider<GrdMedicineDetail_Mdl, String> displayQty();

        @Editor.Path("PurchaseUnit")
        ValueProvider<GrdMedicineDetail_Mdl, String> purchaseUnit();

    }
    final DmGrid<GrdMedicineDetail_Mdl, GrdMedicineDetail_PrpAcs> grdMedicineDetail = new DmGrid(GWT.create(GrdMedicineDetail_PrpAcs.class));

    private Grid iniGrdMedicineDetail() {
        grdMedicineDetail.addRowNumberColumn();
        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().name(), "Medicine Name", 580, "");
        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().displayQty(), "Req. Qty.", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().skuName(), "Unit", 80, "");
        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().purchaseUnit(), "Purchase Unit", 80, "");
        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().date(), "Req. Date", 100, "");

        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().remark(), "Remark", 200, "");
        grdMedicineDetail.addTextColumn(grdMedicineDetail.getPropertyAccess().itemCurrPosition(), "Curr. Position", 220, "");
        grdMedicineDetail.initializeGrid(grdMedicineDetail.getPropertyAccess().fieldHashCode(), 1260, 342, 2);

        grdMedicineDetail.getGrid().getView().setViewConfig(new GridViewConfig<GrdMedicineDetail_Mdl>() {
            @Override
            public String getColStyle(GrdMedicineDetail_Mdl model, ValueProvider<? super GrdMedicineDetail_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel ";
                } else if (DmShared.xNull(model.getIsStatus()).equals("2")) {
                    styleClass += "dm-GridCellDairyConfirm ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdMedicineDetail_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdMedicineDetail.getGrid();
    }
//</editor-fold>

}
