package aarogya.inventory.enquiry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.enquiry.shared.Find_Mdl;
import aarogya.inventory.enquiry.shared.GrdItemDetail_Mdl;
import aarogya.inventory.enquiry.shared.GrdVendor_Mdl;

abstract class Enquiry_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.ENQUIRY.caption;
    final String moduleName = pmInventory.moduleName;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    public final static String approvedByBlog = "Approved By & Date";
    final Button btnAddInd = new Button("Add Indent");

    final Button btnApprovedBy = new Button();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmGUI gui = new DmGUI(topControl, 440);
    final DmTextBox txtDtEnquiry = new DmTextBox(11, true);
    final DmTextBox txtEnquiryNo = new DmTextBox(20, false);
    final DmTextBox txtRefNo = new DmTextBox(30, false);
    final DmTextBox txtWTACode = new DmTextBox();
    final DmTextBox txtRemark = new DmTextBox(250, false);
    final FlowPanel flwIndentBtn = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHEnquiryType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEnquiryType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHVoucherType() {
        txtHEnquiryType.getDmGrid().addTextColumn(txtHEnquiryType.getDmGrid().getPropertyAccess().name(), "Enquiry Type", 100, "Description");
        txtHEnquiryType.getDmGrid().initializeGrid(txtHEnquiryType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHEnquiryType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EnquiryTypeName")
        ValueProvider<Find_Mdl, String> enquiryTypeName();

        @Editor.Path("EnqiryDate")
        ValueProvider<Find_Mdl, String> enqiryDate();

        @Editor.Path("Remarks")
        ValueProvider<Find_Mdl, String> remarks();

        @Editor.Path("ReferenceNo")
        ValueProvider<Find_Mdl, String> referenceNo();

        @Editor.Path("Approval")
        ValueProvider<Find_Mdl, String> approval();

        @Editor.Path("IsStatus")
        ValueProvider<Find_Mdl, String> isStatus();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Enquiry No.", 127, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enqiryDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().enquiryTypeName(), "Type", 50, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().referenceNo(), "Ref. No.", 122, "referenceNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().remarks(), "Remarks", 210, "remark");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approval(), "Approval", 150, "approvedBy");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 5);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC, "v_No");
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHDepartment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHDepartment() {
        txtHDepartment.getDmGrid().addTextColumn(txtHDepartment.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDepartment.getDmGrid().initializeGrid(txtHDepartment.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartment.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public Enquiry_Dsg() {
        initialize();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHDepartment();
    }

    private void initialize() {

        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(tabArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(addUserControl());

        vrtInnerForm.setCellHeight(flw, "100%");

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

    private TabLayoutPanel tabArea() {

        DmClient.setStyle(tabPanel, "100%", "480px", null, null, DmPosition.NONE, true);

        tabPanel.add(vl.add(workingArea()), "General");
        tabPanel.add(vl.add(tabTermsCondition()), "Terms & Conditions");

        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");
        return tabPanel;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "454px", null, null, DmEnum.DmPosition.CENTER, true);

        //======================= Upper Area================================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "45px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(entryArea()));
        flwTable.add(flwTr);
        //======================= END ======================================================

        //======================= Grid Item Tab=============================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "252px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdItemDetail()));
        flwTable.add(flwTr);
        //====================== END========================================================

        //======================= Vendor Grid Tab===========================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "150px", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "150px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(iniGrdVendor()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "150px", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(new DmLabel(""));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //===================== END =========================================================

        return flwTable;
    }

    private FlowPanel tabTermsCondition() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "422px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "422px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "422px", "left", null, DmPosition.NONE, false);
        flwTd.add(gui);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "41px", null, null, DmEnum.DmPosition.CENTER, true);

        ////////////////////////////////1st Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEnquiryType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEnquiryNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Enquiry Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDtEnquiry));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Ref. No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRefNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        ////////////////////////////////2nd Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDepartment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49.7%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "right", null, DmPosition.NONE, false);
        btnAddInd.addStyleName("dm-button-grad");
        btnAddInd.setWidth("126px");
        btnAddInd.setHeight("20px");
        btnAddInd.getElement().setAttribute("style", btnAddInd.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnAddInd.setTitle("Add Indent");
        flwIndentBtn.add(btnAddInd);
        flwTd.add(flwIndentBtn);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="gridItemDetail">
    interface GridItemDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdItemDetail_Mdl> {

        @Editor.Path("Qty")
        ValueProvider<GrdItemDetail_Mdl, String> qty();

        @Editor.Path("DisplayUnit")
        ValueProvider<GrdItemDetail_Mdl, String> displayUnit();
//        @Editor.Path("PurchaseUnitName")
//        ValueProvider<GrdItemDetail_Mdl, String> unit();

        @Editor.Path("ScheduleDt")
        ValueProvider<GrdItemDetail_Mdl, String> scheduleDt();

        @Editor.Path("ColourName")
        ValueProvider<GrdItemDetail_Mdl, String> colorName();

        @Editor.Path("SizeName")
        ValueProvider<GrdItemDetail_Mdl, String> sizeName();

        @Editor.Path("Properties")
        ValueProvider<GrdItemDetail_Mdl, String> properties();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Item Name", 350, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().properties(), "Properties", 300, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().qty(), "Qty", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().displayUnit(), "Unit", 60, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().scheduleDt(), "Req. Date", 80, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().remark(), "Remarks", 310, "");
        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1250, 249);

        return gridItemDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gridVendor">
    interface GridVendor_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdVendor_Mdl> {

    }
    final DmGrid<GrdVendor_Mdl, GridVendor_PrpAcs> gridVendor = new DmGrid(GWT.create(GridVendor_PrpAcs.class));

    public Grid iniGrdVendor() {
        gridVendor.addRowNumberColumn();
        gridVendor.addTextColumn(gridVendor.getPropertyAccess().name(), "Suppliers", 310, "");
        gridVendor.initializeGrid(gridVendor.getPropertyAccess().fieldHashCode(), 500, 152, 2);
        gridVendor.getGrid().getElement().getStyle().setMarginBottom(0, Style.Unit.PX);

        gridVendor.getGrid().getView().setViewConfig(new GridViewConfig<GrdVendor_Mdl>() {
            @Override
            public String getColStyle(GrdVendor_Mdl model, ValueProvider<? super GrdVendor_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "dm-GridCellFormat ";
                if (DmShared.xNull(model.getIsStatus()).equals("1")) {
                    styleClass += "dm-GridCellDairyCancel ";
                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdVendor_Mdl model, int rowIndex) {
                return "";
            }
        });
        return gridVendor.getGrid();
    }
    //</editor-fold>
}
