package dataman.payroll.attendenceEntryManual.client;

import dataman.payroll.attendenceEntryManual.shared.*;
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
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

import dataman.libraryMenu.self.shared.lmPayroll;

abstract class AttendenceEntryManual_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().ATTENDANCE_ENTRY_MANUAL.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final Button btnApprovedBy = new Button();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtDepartment = new DmTextBox(100, false);
    final DmTextBox txtEntryNo = new DmTextBox(100, false);
    final DmTextBox txtAttendanceDate = new DmTextBox(100, false);
    final DmTextBox txtToDate = new DmTextBox(100, false);
    final DmTextBox txtFromDate = new DmTextBox(100, false);
    final DmTextBox txtAvailable = new DmTextBox(100, false);
    final DmTextBox txtDuration = new DmTextBox(100, false);
    final DmTextBoxTime txtToTime = new DmTextBoxTime(true);
    final DmTextBoxTime txtFromTime = new DmTextBoxTime(true);
    final DmTextBox txtAttendanceDetail = new DmTextBox(100, false);
    final DmTextBoxNumeric txtWorkingHrs = new DmTextBoxNumeric(2, false, 5, false);
    final DmTextBox txtRemark = new DmTextBox(100, false);
    final DmLabel lblCurrentEntryNo = new DmLabel("");
    final DmLabel lblLeaveStatus = new DmLabel("");
    AttendenceEntryManual_Mdl leaveEntryMdl;

    // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("EntryDate")
        ValueProvider<Find_Mdl, String> entryDate();

        @Editor.Path("FromDate")
        ValueProvider<Find_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<Find_Mdl, String> toDate();

        @Editor.Path("AttendanceName")
        ValueProvider<Find_Mdl, String> attendanceName();

        @Editor.Path("EntryNo")
        ValueProvider<Find_Mdl, String> entryNo();
    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryNo(), "Entry No", 100, "EntryNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employeeName(), "Employee Name", 100, "EmployeeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 100, "v_Date");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromDate(), "From Date", 100, "fromDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toDate(), "To Date", 100, "toDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().attendanceName(), "Attendance Name", 100, "attendanceName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
    }
    //</editor-fold>
    // Constructor

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

    interface TxtHAttendance_PrpAcs extends KeyField_T_PrpAcs<AttendenceTextHelp_Mdl> {
        @Editor.Path("ShortName")
        ValueProvider<AttendenceTextHelp_Mdl, String> shortName();
    }

    final DmTextHelp<AttendenceTextHelp_Mdl, TxtHAttendance_PrpAcs> txtHAttendence = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHAttendance_PrpAcs.class), 5, true);

    private void iniTxtHAttendance() {
        txtHAttendence.getDmGrid().addTextColumn(txtHAttendence.getDmGrid().getPropertyAccess().name(), "Attendance", 180, "name");
        txtHAttendence.getDmGrid().addTextColumn(txtHAttendence.getDmGrid().getPropertyAccess().shortName(), "Short Name", 80, "manualCode");
        txtHAttendence.getDmGrid().initializeGrid(txtHAttendence.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHAttendence.getHelper().addSort("name", DmSortType.ASC);
    }

    public AttendenceEntryManual_Dsg() {
        ini();
        iniTxtHEmployee();
        iniTxtHAttendance();
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
        DmClient.setStyle(flwTable, "100%", "325px", null, null, DmPosition.CENTER, true);

        //================1st Row============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        //===========Left Section=====================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "100%", "200px", "left", null, DmPosition.NONE, false);
        flwTD.add(top_leftSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel top_leftSection() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "140px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
//        flwTR = new FlowPanel();
//        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTR.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Entry No.");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtEntryNo));
//
//        flwTR.add(flwTD);
//        flwTable.add(flwTR);

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

//        /*====================================< 3rd Row >====================================*/
//        flwTR = new FlowPanel();
//        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
//        //   flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Attendance Date");
//        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
//        flwTD.add(lbl);
//        flwTR.add(flwTD);
//
//        flwTD = new FlowPanel();
//        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
//        flwTD.add(vl.add(txtAttendanceDate));
//        flwTR.add(flwTD);
//
//        flwTable.add(flwTR);
        /*====================================< 4th Row >====================================*/

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //     flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Attendance");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAttendence));
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

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Duration (Hr.)");
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

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "75%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtRemark));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private DmUserControl addUserControl() {
        Anchor aBtn;
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
//        userControl.addGap(100);

        return userControl;
    }
}
