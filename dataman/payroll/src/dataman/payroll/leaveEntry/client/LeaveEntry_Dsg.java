package dataman.payroll.leaveEntry.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.payroll.leaveEntry.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.leaveEntry.shared.Employee_Mdl;
import dataman.payroll.leaveEntry.shared.GrdLeaveHistory_Mdl;
import dataman.payroll.leaveEntry.shared.GrdLeaveStatus_Mdl;
import dataman.payroll.leaveEntry.shared.LeaveTextHelp_Mdl;
import dataman.payroll.leaveEntryApproval.shared.GrdLeaveEntryApproval_Mdl;
import dataman.payroll.self.shared.PREnum;

abstract class LeaveEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().LEAVE_ENTRY.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String approvedByBlog = "Approved By & Date";
    final Button btnApprovedBy = new Button();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtDepartment = new DmTextBox(100, false);
    final DmTextBox txtEntryNo = new DmTextBox(100, false);
    final DmTextBox txtApplyDate = new DmTextBox(100, false);
    final DmTextBox txtToDate = new DmTextBox(100, false);
    final DmTextBoxNumeric txtToTime = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBox txtFromDate = new DmTextBox(100, false);
    final DmTextBoxNumeric txtFromTime = new DmTextBoxNumeric(2, false, 5, true);
    final DmTextBox txtAvailable = new DmTextBox(100, false);
    final DmTextBox txtDuration = new DmTextBox(100, false);
    final DmTextBox txtReason = new DmTextBox(100, true);
    final DmTextBox txtLeaveDetail = new DmTextBox(100, false);
    final DmLabel lblCurrentEntryNo = new DmLabel("");
    final DmLabel lblLeaveStatus = new DmLabel("");
    LeaveEntry_Mdl leaveEntryMdl;

    // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("ApplyDate")
        ValueProvider<Find_Mdl, String> applyDate();
        
        @Editor.Path("FromDate")
        ValueProvider<Find_Mdl, String> fromDate();
        
        @Editor.Path("ToDate")
        ValueProvider<Find_Mdl, String> toDate();
        
        @Editor.Path("LeaveName")
        ValueProvider<Find_Mdl, String> leaveName();
        
        @Editor.Path("EntryNo")
        ValueProvider<Find_Mdl, String> entryNo();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryNo(), "Entry No", 100, "EntryNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employeeName(), "Employee Name", 100, "EmployeeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().applyDate(), "Apply Date", 100, "applyDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromDate(), "From Date", 100, "fromDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toDate(), "To Date", 100, "toDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().leaveName(), "Leave Name", 100, "LeaveNature");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 2);
        
        dmFind.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<Find_Mdl>() {

            @Override
            public String getColStyle(Find_Mdl model, ValueProvider<? super Find_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String style = "dm-GridCellFormat ";

                if (Integer.valueOf(model.getStatusCode()) == PREnum.LEAVE_STATUS.APPROVED.getId()) {

                    style = " cpoeOPD-Genearal";

                } else if (Integer.valueOf(model.getStatusCode()) == PREnum.LEAVE_STATUS.CANCELED.getId()) {
                    style = "dm-GridCellDairyCancel ";
                }

                return style;
            }

            @Override
            public String getRowStyle(Find_Mdl model, int rowIndex) {
                return "";
            }
        });

        dmFind.getHelper().addSort("applyDate", DmSortType.DESC);
    }
    //</editor-fold>
    // Constructor

    //<editor-fold defaultstate="collapsed" desc="txtHReceiptVoucher">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLeaveStatus_Mdl> {

        @Editor.Path("Leave")
        ValueProvider<GrdLeaveStatus_Mdl, String> leave();

        @Editor.Path("Opening")
        ValueProvider<GrdLeaveStatus_Mdl, String> opening();

        @Editor.Path("Taken")
        ValueProvider<GrdLeaveStatus_Mdl, String> taken();

        @Editor.Path("Balance")
        ValueProvider<GrdLeaveStatus_Mdl, String> balance();

    }

    final DmGrid<GrdLeaveStatus_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    public Grid leaveStatusGrid() {

        dmGrd.addRowNumberColumn();
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().leave(), "Leave", 101, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().opening(), "Opening", 101, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().taken(), "Taken", 101, "");
        dmGrd.addTextColumn(dmGrd.getPropertyAccess().balance(), "Balance", 150, "");
        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 648, 185, 4);

        return dmGrd.getGrid();
    }

    interface GrdHistory_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLeaveHistory_Mdl> {

        @Editor.Path("EntryNo")
        ValueProvider<GrdLeaveHistory_Mdl, String> entryNo();

        @Editor.Path("LeaveName")
        ValueProvider<GrdLeaveHistory_Mdl, String> leaveName();

        @Editor.Path("FromDate")
        ValueProvider<GrdLeaveHistory_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<GrdLeaveHistory_Mdl, String> toDate();

        @Editor.Path("Duration")
        ValueProvider<GrdLeaveHistory_Mdl, String> duration();

    }
    final DmGrid<GrdLeaveHistory_Mdl, GrdHistory_PrpAcs> dmHistoryGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(GrdHistory_PrpAcs.class));

    public Grid leaveHistoryGrid() {

        dmHistoryGrd.addRowNumberColumn();
        dmHistoryGrd.addTextColumn(dmHistoryGrd.getPropertyAccess().entryNo(), "Entry No.", 101, "");
        dmHistoryGrd.addTextColumn(dmHistoryGrd.getPropertyAccess().leaveName(), "Leave", 101, "");
        dmHistoryGrd.addTextColumn(dmHistoryGrd.getPropertyAccess().fromDate(), "From Date", 101, "");
        dmHistoryGrd.addTextColumn(dmHistoryGrd.getPropertyAccess().toDate(), "To Date", 150, "");
        dmHistoryGrd.addTextColumn(dmHistoryGrd.getPropertyAccess().duration(), "Duration", 150, "");
        dmHistoryGrd.initializeGrid(dmHistoryGrd.getPropertyAccess().fieldHashCode(), 648, 285, 4);

        return dmHistoryGrd.getGrid();

    }

    interface GrdEncashHistory_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLeaveHistory_Mdl> {

        @Editor.Path("EntryNo")
        ValueProvider<GrdLeaveHistory_Mdl, String> entryNo();

        @Editor.Path("LeaveName")
        ValueProvider<GrdLeaveHistory_Mdl, String> leaveName();

        @Editor.Path("Date")
        ValueProvider<GrdLeaveHistory_Mdl, String> date();

        @Editor.Path("Duration")
        ValueProvider<GrdLeaveHistory_Mdl, String> duration();

    }
    final DmGrid<GrdLeaveHistory_Mdl, GrdEncashHistory_PrpAcs> dmEncashHistoryGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(GrdEncashHistory_PrpAcs.class));

    public Grid encashHistoryGrid() {

        dmEncashHistoryGrd.addRowNumberColumn();
        dmEncashHistoryGrd.addTextColumn(dmEncashHistoryGrd.getPropertyAccess().entryNo(), "Entry No.", 101, "");
        dmEncashHistoryGrd.addTextColumn(dmEncashHistoryGrd.getPropertyAccess().date(), "Date", 150, "");
        dmEncashHistoryGrd.addTextColumn(dmEncashHistoryGrd.getPropertyAccess().leaveName(), "Leave", 101, "");
        dmEncashHistoryGrd.addTextColumn(dmEncashHistoryGrd.getPropertyAccess().duration(), "Duration", 150, "");
        dmEncashHistoryGrd.initializeGrid(dmEncashHistoryGrd.getPropertyAccess().fieldHashCode(), 614, 285);

        return dmEncashHistoryGrd.getGrid();

    }

    interface TxtHEmployee_PrpAcs extends KeyField_T_PrpAcs<Employee_Mdl> {

        @Editor.Path("Employee")
        ValueProvider<Employee_Mdl, String> employee();

        @Editor.Path("Id")
        ValueProvider<Employee_Mdl, String> id();

        @Editor.Path("FatherName")
        ValueProvider<Employee_Mdl, String> fatherName();

        @Editor.Path("Department")
        ValueProvider<Employee_Mdl, String> department();

        @Editor.Path("DOJ")
        ValueProvider<Employee_Mdl, String> DOJ();
    }

    final DmTextHelp<Employee_Mdl, TxtHEmployee_PrpAcs> txtHEmployee = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHEmployee_PrpAcs.class), 10, true);

    private void iniTxtHEmployee() {
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().employee(), "Employee", 150, "Employee");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().id(), "ID", 50, "");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().fatherName(), "Father Name", 100, "fatherName");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().department(), "Department", 100, "department");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().DOJ(), "DOJ", 100, "doj");
        txtHEmployee.getDmGrid().initializeGrid(txtHEmployee.getDmGrid().getPropertyAccess().code(), 500, 300);
        txtHEmployee.getHelper().addSort("Employee", DmSortType.ASC);
    }

    interface TxtHLeave_PrpAcs extends KeyField_T_PrpAcs<LeaveTextHelp_Mdl> {

        @Editor.Path("Leave")
        ValueProvider<LeaveTextHelp_Mdl, String> leave();

        @Editor.Path("ShortName")
        ValueProvider<LeaveTextHelp_Mdl, String> shortName();
    }

    final DmTextHelp<LeaveTextHelp_Mdl, TxtHLeave_PrpAcs> txtHLeave = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHLeave_PrpAcs.class), 5, true);

    private void iniTxtHLeave() {
        txtHLeave.getDmGrid().addTextColumn(txtHLeave.getDmGrid().getPropertyAccess().leave(), "Leave", 180, "name");
        txtHLeave.getDmGrid().addTextColumn(txtHLeave.getDmGrid().getPropertyAccess().shortName(), "Short Name", 180, "manualCode");
        txtHLeave.getDmGrid().initializeGrid(txtHLeave.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHLeave.getHelper().addSort("name", DmSortType.ASC);
    }

    public LeaveEntry_Dsg() {
        ini();
        iniTxtHEmployee();
        iniTxtHLeave();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true); // It sets the 
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
    //    vrtInnerForm.add(createBottom());
        vrtInnerForm.add(addUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "500px", null, null, DmPosition.CENTER, true);

        //================1st Row============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        //===========Left Section=====================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", "200px", "left", null, DmPosition.NONE, false);
        flwTD.add(top_leftSection());
        flwTR.add(flwTD);

        //==============Right Section==================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "49%", "200px", "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        flwTD.add(top_rightSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //===============2nd row=======================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", "300px", "left", null, DmPosition.NONE, false);
        flwTD.add(bottomLeftSection());
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "49%", "300px", "left", null, DmPosition.NONE, false);
        flwTD.add(bottom_rightSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel top_leftSection() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtEntryNo));

        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //    flwTR.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEmployee));

        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //flwTR.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtDepartment));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 3rd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //   flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Apply Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtApplyDate));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //     flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Leave");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHLeave));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Available");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtAvailable));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 5th Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //   flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date/Time");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFromDate));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "7%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFromTime));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date/Time");
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtToDate));
        flwTR.add(flwTD);
        
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "7%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtToTime));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 6th Row >====================================*/

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //  flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);


        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Duration");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtDuration));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 6th Row >====================================*/

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        // flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtReason));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel top_rightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "15px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "15px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Current Leave Status");
        lbl.getElement().getStyle().setColor("blue");
        lbl.getElement().setAttribute("style", lbl.getElement().getAttribute("style")
                + ";font-weight:bold");
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "185px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "185px", "left", null, DmPosition.NONE, false);
        flwTd.add(leaveStatusGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel bottom_rightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "300px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "15px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "15px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        lbl = new DmLabel("Leave Taken History");
        lbl.getElement().getStyle().setColor("blue");
        lbl.getElement().setAttribute("style", lbl.getElement().getAttribute("style")
                + ";font-weight:bold");
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "285px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "285px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        flwTd.add(vl.add(leaveHistoryGrid()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel bottomLeftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "300px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "15px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "15px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(11, Style.Unit.PX);
        lbl = new DmLabel("Leave Encash History");
        lbl.getElement().getStyle().setColor("blue");
        lbl.getElement().setAttribute("style", lbl.getElement().getAttribute("style")
                + ";font-weight:bold");
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "285px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "285px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(0, Style.Unit.PX);
        flwTd.add(vl.add(encashHistoryGrid()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
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

    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        
        userControl.addGeneralBlog(lblCurrentEntryNo);
        lblCurrentEntryNo.setHeight("15Px");
        lblCurrentEntryNo.getElement().getStyle().setColor("blue");
        lblCurrentEntryNo.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblCurrentEntryNo.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblCurrentEntryNo.getElement().getStyle().setMarginRight(38, Style.Unit.PX);
        lblCurrentEntryNo.getElement().getStyle().setFontSize(17, Style.Unit.PX);
        
        userControl.addGeneralBlog(lblLeaveStatus);
        lblLeaveStatus.setHeight("15Px");
        lblLeaveStatus.getElement().getStyle().setColor("blue");
        lblLeaveStatus.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblLeaveStatus.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblLeaveStatus.getElement().getStyle().setMarginRight(38, Style.Unit.PX);
        lblLeaveStatus.getElement().getStyle().setFontSize(17, Style.Unit.PX);
       
        
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        

        return userControl;

    }

}
