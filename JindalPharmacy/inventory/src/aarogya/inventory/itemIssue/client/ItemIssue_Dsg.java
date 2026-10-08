package aarogya.inventory.itemIssue.client;

import aarogya.inventory.itemIssue.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemIssue.shared.Find_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHRequisitionNo_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHIssueTo_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHProject_Mdl;
import aarogya.inventory.itemIssue.shared.TxtHRegNo_Mdl;
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
import aarogya.projectMenu.self.shared.pmInventory;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

abstract class ItemIssue_Dsg extends IsWidgetExtended {

    final pmInventory pmInventory = new pmInventory();
    final String widgetName = pmInventory.ITEM_ISSUE.caption;
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
    final DmTextBox txtProjectAccount = new DmTextBox(20, false);
    final DmTextBox txtPatientName = new DmTextBox(100, false);
    final DmTextBox txtUHIDNo = new DmTextBox(35, false);

    final DmTextBox txtRefDate = new DmTextBox(11, false);
    final DmTextBox txtRefNo = new DmTextBox(20, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox();

    public FlowPanel flwBlank = new FlowPanel();
    public FlowPanel flwProject = new FlowPanel();
    public FlowPanel flwPatientDet = new FlowPanel();
    public FlowPanel flwSearchRoom = new FlowPanel();
    final Button btnSearchRoom = new Button("...");
    final Button btnLedgerSearch = new Button("...");
    final FlowPanel flwPatientSearch = new FlowPanel();
    final FlowPanel flwPatientSearchTd = new FlowPanel();
    final Button btnPatientSearch = new Button("Patient Search");
    final Button btnOPDSearch = new Button("...");

    //<editor-fold defaultstate="collapsed" desc="Help Events">
    //<editor-fold defaultstate="collapsed" desc="txtHVoucherType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVoucherType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHVoucherType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIssueTo">
    interface TxtHIssueTo_PrpAcs extends KeyField_T_PrpAcs<TxtHIssueTo_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHIssueTo_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHIssueTo_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHIssueTo_Mdl, TxtHIssueTo_PrpAcs> txtHIssueTo = new DmTextHelp(GWT.create(TxtHIssueTo_PrpAcs.class), 6, false);

    private void iniTxtHIssueTo() {
        txtHIssueTo.getDmGrid().addTextColumn(txtHIssueTo.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHIssueTo.getDmGrid().addTextColumn(txtHIssueTo.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHIssueTo.getDmGrid().addTextColumn(txtHIssueTo.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHIssueTo.getDmGrid().initializeGrid(txtHIssueTo.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHIssueTo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHIssueTo.getHelper().addSort("cityName", DmSortType.ASC);
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
        txtHRequisition.getDmGrid().initializeGrid(txtHRequisition.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHRequisition.getHelper().addSort("v_Date", DmSortType.DESC);
        txtHRequisition.getHelper().addSort("short_Name", DmSortType.ASC);
        txtHRequisition.getHelper().addSort("v_No", DmSortType.DESC);
        txtHRequisition.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHProject">
    interface TxtHProject_PrpAcs extends KeyField_T_PrpAcs<TxtHProject_Mdl> {

        @Editor.Path("ProjectAcName")
        ValueProvider<TxtHProject_Mdl, String> projectAcName();

        @Editor.Path("ProjectAcCode")
        ValueProvider<TxtHProject_Mdl, String> projectAcCode();
    }

    final DmTextHelp<TxtHProject_Mdl, TxtHProject_PrpAcs> txtHProject = new DmTextHelp(GWT.create(TxtHProject_PrpAcs.class), 6, true);

    private void iniTxtHProject() {
        txtHProject.getDmGrid().addTextColumn(txtHProject.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHProject.getDmGrid().addTextColumn(txtHProject.getDmGrid().getPropertyAccess().projectAcName(), "Project A/c", 100, "projectAccountName");
        txtHProject.getDmGrid().addTextColumn(txtHProject.getDmGrid().getPropertyAccess().projectAcCode(), "Project A/c Code", 0, "projectAccountCode");
        txtHProject.getDmGrid().initializeGrid(txtHProject.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHProject.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRegNo">
    interface TxtHRegNo_PrpAcs extends KeyField_T_PrpAcs<TxtHRegNo_Mdl> {

        @Editor.Path("Uhid")
        ValueProvider<TxtHRegNo_Mdl, String> uhid();

        @Editor.Path("PatientName")
        ValueProvider<TxtHRegNo_Mdl, String> patientName();

        @Editor.Path("VoucherDescription")
        ValueProvider<TxtHRegNo_Mdl, String> voucherDescription();

        @Editor.Path("ConsultantName")
        ValueProvider<TxtHRegNo_Mdl, String> consultantName();
    }

    final DmTextHelp<TxtHRegNo_Mdl, TxtHRegNo_PrpAcs> txtHRegNo = new DmTextHelp(GWT.create(TxtHRegNo_PrpAcs.class), 6, true);

    private void iniTxtHRegNo() {
        txtHRegNo.getDmGrid().addTextColumn(txtHRegNo.getDmGrid().getPropertyAccess().name(), "Reg. ID", 90, "name");
        txtHRegNo.getDmGrid().addTextColumn(txtHRegNo.getDmGrid().getPropertyAccess().patientName(), "Patient Name", 100, "patientName");
        txtHRegNo.getDmGrid().addTextColumn(txtHRegNo.getDmGrid().getPropertyAccess().uhid(), "Uhid", 90, "uhid");
        txtHRegNo.getDmGrid().addTextColumn(txtHRegNo.getDmGrid().getPropertyAccess().consultantName(), "Consultant", 100, "consultantName");
        txtHRegNo.getDmGrid().initializeGrid(txtHRegNo.getDmGrid().getPropertyAccess().code(), 450, 0, 2);
        txtHRegNo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoomType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniTxtHCustomerType() {
        txtHRoomType.getDmGrid().addTextColumn(txtHRoomType.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHRoomType.getDmGrid().initializeGrid(txtHRoomType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
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
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Issue No.", 150, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherDate(), "Date", 80, "replace(convert(varchar, v_Date, 106), ' ','-')", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().voucherType(), "Type", 80, "short_Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().receivedBy(), "Received From", 1, "receivedByName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().department(), "Department In", 350, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0, 4);

        dmFind.getHelper().addSort("v_Date", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmEnum.DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public ItemIssue_Dsg() {
        ini();
        iniFind();
        iniTxtHVoucherType();
        iniTxtHIssueTo();
        iniTxtHDepartment();
        iniTxtHRequisition();
        iniTxtHProject();
        iniTxtHCustomerType();
        iniTxtHRegNo();
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
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "476px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98.4%", "70px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "70px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topLeftArea()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "70px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(topRightArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
//        flwTr = new FlowPanel();
        flwBlank = new FlowPanel();
        DmClient.setStyle(flwBlank, "98.4%", "22px", "left", null, DmPosition.NONE, true);
//        flwTr.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);

//        DmClient.setStyle(flwBlank, "100%", "16px", "left", null, DmPosition.NONE, true);

        flwTable.add(flwBlank);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "98%", "350px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
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
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consumption Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        lbl = new DmLabel("Requisition");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRequisition));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
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
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
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
        lbl = new DmLabel("Godown From");
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

    public FlowPanel projectDet() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTr = new FlowPanel();

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);

        //Name
        flwTr = new FlowPanel();
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9.8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Project Name");
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "36.8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHProject));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    public FlowPanel patientDet() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTr = new FlowPanel();
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);

        //Name
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3.5%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoomType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRegNo));
        flwTr.add(flwTd);

//        hrtPnl = new HorizontalPanel();
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
//        hrtPnl = new HorizontalPanel();
//        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);
//        hrtPnl.add(vl.add(txtHRegNo));
//        hrtPnl.setCellWidth(txtHRegNo, "100%");
//        DmClient.setStyle(flwSearchRoom, "100%", "20px", "left", null, DmPosition.NONE, true);
//        btnSearchRoom.setWidth("20px");
//        btnSearchRoom.setHeight("20px");
//        btnSearchRoom.getElement().setAttribute("style", btnSearchRoom.getElement().getAttribute("style")
//                + ";font-size:11px;padding:0px;");
//        btnSearchRoom.setTitle("Patient Search");
//        flwSearchRoom.add(btnSearchRoom);
//
////        btnOPDSearch.setWidth("20px");
////        btnOPDSearch.setHeight("20px");
////        btnOPDSearch.getElement().setAttribute("style", btnOPDSearch.getElement().getAttribute("style")
////                + ";font-size:11px;padding:0px;");
////        btnOPDSearch.setTitle("Patient Search (OPD)");
////        flwSearchRoom.add(btnOPDSearch);
////
////        btnLedgerSearch.setWidth("20px");
////        btnLedgerSearch.setHeight("20px");
////        btnLedgerSearch.getElement().setAttribute("style", btnLedgerSearch.getElement().getAttribute("style")
////                + ";font-size:11px;padding:0px;");
////        btnLedgerSearch.setTitle("Ledger Search");
////        flwSearchRoom.add(btnLedgerSearch);
//        hrtPnl.add(flwSearchRoom);
//        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "19.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatientName));
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

        @Editor.Path("RequisitionNo")
        ValueProvider<GrdItemDetail_Mdl, String> requisitionNo();

        @Editor.Path("Remark")
        ValueProvider<GrdItemDetail_Mdl, String> remark();

    }
    final DmGrid<GrdItemDetail_Mdl, GridItemDetail_PrpAcs> gridItemDetail = new DmGrid(GWT.create(GridItemDetail_PrpAcs.class));

    public Grid iniGrdItemDetail() {
        gridItemDetail.addRowNumberColumn();
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().name(), "Item Name", 350, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().propertyDisplay(), "Properties", 200, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().quantity(), "Quantity", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().skuName(), "SKU", 50, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().requisitionNo(), "Requisition No.", 120, "");
        gridItemDetail.addTextColumn(gridItemDetail.getPropertyAccess().remark(), "Remark", 1, "");
        gridItemDetail.initializeGrid(gridItemDetail.getPropertyAccess().fieldHashCode(), 1256, 380, 7);
        return gridItemDetail.getGrid();
    }
    //</editor-fold>

}
