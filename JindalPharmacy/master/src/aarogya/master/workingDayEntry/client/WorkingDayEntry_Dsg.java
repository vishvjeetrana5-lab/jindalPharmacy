package aarogya.master.workingDayEntry.client;

import aarogya.master.workingDayEntry.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class WorkingDayEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().WORKING_DAYS.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBoxNumeric txtDays = new DmTextBoxNumeric(2, false, 8, true);
    final DmTextBoxNumeric txtMinimumGurantee = new DmTextBoxNumeric(2, false, 8, true);

    //<editor-fold defaultstate="collapsed" desc="txtHSalaryPeriod">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSalaryPeriod = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHSalName() {
        txtHSalaryPeriod.getDmGrid().addTextColumn(txtHSalaryPeriod.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHSalaryPeriod.getDmGrid().initializeGrid(txtHSalaryPeriod.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHSalaryPeriod.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //    //<editor-fold defaultstate="collapsed" desc="txtHYear">
//    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHYear = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);
//
//    private void iniTxtHYear() {
//        txtHYear.getDmGrid().addTextColumn(txtHYear.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
//        txtHYear.getDmGrid().initializeGrid(txtHYear.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
//
//        txtHYear.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
//    }
//    //</editor-fold>

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmployeeName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    public void iniTxtHEmployeetName() {
        txtHEmployeeName.getDmGrid().addTextColumn(txtHEmployeeName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHEmployeeName.getDmGrid().initializeGrid(txtHEmployeeName.getDmGrid().getPropertyAccess().code(), 380, 0, 1);
        txtHEmployeeName.getHelper().addSort("name", DmSortType.ASC);

    }

    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<Find_Mdl, String> employeeName();

        @Editor.Path("SalName")
        ValueProvider<Find_Mdl, String> salName();

        @Editor.Path("WorkingDays")
        ValueProvider<Find_Mdl, String> workingDays();

        @Editor.Path("MinimumGurantee")
        ValueProvider<Find_Mdl, String> minimumGurantee();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().employeeName(), "Employee Name", 100, "empName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().salName(), "Salary Name", 100, "salaryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().workingDays(), "Working Days", 100, "workingDays");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().minimumGurantee(), "Minimum Gurantee", 100, "minimumGurantee");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);
        dmFind.getHelper().addSort("empName", DmSortType.ASC);
    }

    public WorkingDayEntry_Dsg() {
        ini();
        iniTxtHSalName();
        iniTxtHEmployeetName();
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
        DmClient.setStyle(flwTable, "40%", "476px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(34, Style.Unit.PCT);

        //Consultant Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Salary Period");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSalaryPeriod));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Employee");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "47%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmployeeName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

//
//        flwTr = new FlowPanel();
//        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
//        lbl = new DmLabel("Year");
//        flwTd.add(lbl);
//        flwTr.add(flwTd);
//
//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtHYear));
//        flwTr.add(flwTd);
//        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Working Days");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDays));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Minimum Guarantee");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMinimumGurantee));
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
