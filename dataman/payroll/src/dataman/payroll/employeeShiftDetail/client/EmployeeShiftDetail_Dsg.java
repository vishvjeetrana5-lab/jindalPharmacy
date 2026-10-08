package dataman.payroll.employeeShiftDetail.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.employeeShiftDetail.shared.Employee_Mdl;
import dataman.payroll.employeeShiftDetail.shared.Find_Mdl;
import dataman.payroll.employeeShiftDetail.shared.TxtHShift_Mdl;

abstract class EmployeeShiftDetail_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().EMPLOYEE_SHIFT_DETAIL.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtFromDate = new DmTextBox(11, true);
    final DmTextBox txtToDate = new DmTextBox(11, true);


    // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("ShiftName")
        ValueProvider<Find_Mdl, String> shiftName();

        @Editor.Path("FromDate")
        ValueProvider<Find_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<Find_Mdl, String> toDate();


    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 10);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Employee Name", 100, "employeeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shiftName(), "Shift Name", 100, "shiftName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().fromDate(), "From Date", 100, "fromDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().toDate(), "To Date", 100, "toDate");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("shiftName", DmSortType.ASC);
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
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().id(), "ID", 100, "");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().fatherName(), "Father Name", 100, "fatherName");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().department(), "Department", 100, "department");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().DOJ(), "DOJ", 100, "doj");
        txtHEmployee.getDmGrid().initializeGrid(txtHEmployee.getDmGrid().getPropertyAccess().code(), 570, 300);
        txtHEmployee.getHelper().addSort("Employee", DmSortType.ASC);
    }
    
       interface TxtHShift_PrpAcs extends KeyField_T_PrpAcs<TxtHShift_Mdl> {

        @Editor.Path("FromTime")
        ValueProvider<TxtHShift_Mdl, String> fromTime();

        @Editor.Path("ToTime")
        ValueProvider<TxtHShift_Mdl, String> toTime();
    }

    final DmTextHelp<TxtHShift_Mdl, TxtHShift_PrpAcs> txtHShift = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHShift_PrpAcs.class), 5, true);

    private void iniTxtHShift() {
        txtHShift.getDmGrid().addTextColumn(txtHShift.getDmGrid().getPropertyAccess().name(), "Shift", 150, "shiftName");
        txtHShift.getDmGrid().addTextColumn(txtHShift.getDmGrid().getPropertyAccess().fromTime(), "From Time", 100, "fromTime");
        txtHShift.getDmGrid().addTextColumn(txtHShift.getDmGrid().getPropertyAccess().toTime(), "To Time", 100, "toTime");
        txtHShift.getDmGrid().initializeGrid(txtHShift.getDmGrid().getPropertyAccess().code(), 300, 0,1);
        txtHShift.getHelper().addSort("shiftName", DmSortType.ASC);
    }


    public EmployeeShiftDetail_Dsg() {
        ini();
        iniTxtHShift();
        iniTxtHEmployee();
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
        vrtInnerForm.add(addUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "200px", null, null, DmPosition.CENTER, true);

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
        DmClient.setStyle(flwTable, "60%", "150px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(3, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHEmployee));

        flwTR.add(flwTD);
        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //    flwTR.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Shift Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHShift));

        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        //flwTR.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtFromDate));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtToDate));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        return flwTable;
    }

    private DmUserControl addUserControl() {
        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, false, true);
//        userControl.addGap(100);

        return userControl;
    }
}
