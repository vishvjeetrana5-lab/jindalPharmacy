package dataman.payroll.leavingMaster.client;

import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.leavingMaster.shared.Employee_Mdl;
import dataman.payroll.leavingMaster.shared.Find_Mdl;

abstract class LeavingMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().LEAVING_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtDepartment = new DmTextBox(100, false);
    final DmTextBox txtDesignation = new DmTextBox(100, false);
    final DmTextBox txtJoiningDate = new DmTextBox(100, false);
    final DmTextBox txtDate = new DmTextBox(100, false);
    final DmTextBox txtLastDuty = new DmTextBox(100, false);
 
    
    interface TxtHEmployee_PrpAcs extends KeyField_T_PrpAcs<Employee_Mdl> {

        @Editor.Path("Employee")
        ValueProvider<Employee_Mdl, String> employee();

        @Editor.Path("Id")
        ValueProvider<Employee_Mdl, String> id();

        @Editor.Path("FatherName")
        ValueProvider<Employee_Mdl, String> fatherName();

        @Editor.Path("Department")
        ValueProvider<Employee_Mdl, String> department();
    
        @Editor.Path("Designation")
        ValueProvider<Employee_Mdl, String> designation();

        @Editor.Path("DOJ")
        ValueProvider<Employee_Mdl, String> DOJ();
    }

    final DmTextHelp<Employee_Mdl, TxtHEmployee_PrpAcs> txtHEmployee = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHEmployee_PrpAcs.class), 10, true);

    private void iniTxtHEmployee() {
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().employee(), "Employee", 150, "Employee");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().id(), "ID", 90, "");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().fatherName(), "Father Name", 100, "fatherName");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().department(), "Department", 100, "department");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().designation(), "Designation", 100, "Designation");
        txtHEmployee.getDmGrid().addTextColumn(txtHEmployee.getDmGrid().getPropertyAccess().DOJ(), "DOJ", 90, "doj");
        txtHEmployee.getDmGrid().initializeGrid(txtHEmployee.getDmGrid().getPropertyAccess().code(), 600, 300);
        txtHEmployee.getHelper().addSort("Employee", DmSortType.ASC);
    }
    
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReason = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHReason() {
        txtHReason.getDmGrid().addTextColumn(txtHReason.getDmGrid().getPropertyAccess().name(), "Reason", 452, "name");
        txtHReason.getDmGrid().initializeGrid(txtHReason.getDmGrid().getPropertyAccess().code(), 455, 300);
        txtHReason.getHelper().addSort("name", DmSortType.ASC);
    }
    
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHResignationMode = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHResignationMode() {
        txtHResignationMode.getDmGrid().addTextColumn(txtHResignationMode.getDmGrid().getPropertyAccess().name(), "Resignation Mode", 450, "name");
        txtHResignationMode.getDmGrid().initializeGrid(txtHResignationMode.getDmGrid().getPropertyAccess().code(), 455, 300);
        txtHResignationMode.getHelper().addSort("name", DmSortType.ASC);
    }
    
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSalaryStatus = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHSalaryStatus() {
        txtHSalaryStatus.getDmGrid().addTextColumn(txtHSalaryStatus.getDmGrid().getPropertyAccess().name(), "Salary Status", 450, "name");
        txtHSalaryStatus.getDmGrid().initializeGrid(txtHSalaryStatus.getDmGrid().getPropertyAccess().code(), 455, 300);
        txtHSalaryStatus.getHelper().addSort("name", DmSortType.ASC);
    }


    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("DepartmentName")
        ValueProvider<Find_Mdl, String> departmentName();
        
        @Editor.Path("DesignationName")
        ValueProvider<Find_Mdl, String> designationName();
        
        @Editor.Path("DateOfJoining")
        ValueProvider<Find_Mdl, String> dateOfJoining();
        
        @Editor.Path("EmpId")
        ValueProvider<Find_Mdl, String> empId();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 14);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employeeName(), "Employee Name",170, "EmployeeName");
                dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().empId(), "Employee Id", 100, "EmpId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentName(), "Department", 100, "Department");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().designationName(), "Designation", 170, "Designation");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dateOfJoining(), "Joining Date", 170, "doj");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("EmployeeName", DmSortType.ASC);

    }
    //</editor-fold>

    public LeavingMaster_Dsg() {
        ini();
        iniTxtHEmployee();
        iniTxtHReason();
        iniTxtHResignationMode();
        iniTxtHSalaryStatus();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "62%", "170px", null, null, DmPosition.CENTER, true);

        //Department Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmployee));
        flwTr.add(flwTd);
        flwTable.add(flwTr);        

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDepartment));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Designation");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDesignation));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Joining Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtJoiningDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Last Duty");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLastDuty));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Resignation Mode");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHResignationMode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reason");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHReason));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Last Salary Status");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "57%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSalaryStatus));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private DmUserControl iniUserControl() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

}
