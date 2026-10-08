package dataman.payroll.attendanceEntry.client;

import dataman.payroll.attendanceEntry.shared.Find_Mdl;
import dataman.payroll.attendanceEntry.shared.AttendanceEntry_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.DmEnum.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.payroll.attendanceEntry.shared.GrdAttendanceEntry_Mdl;
import dataman.payroll.attendanceEntry.shared.SalaryMethod_Mdl;

abstract class AttendanceEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmPayroll().ATTENDANCE_ENTRY.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwButtonFillDetails = new FlowPanel();
    final Button btnFillDetails = new Button("Fill Details");
    final Button btngetAttendance = new Button("Gat Details");
    final FlowPanel flwButtonGetAttendance = new FlowPanel();

    // <editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("SalaryMethod")
        ValueProvider<Find_Mdl, String> salaryMethod();

        @Editor.Path("DepartmentName")
        ValueProvider<Find_Mdl, String> departmentName();
    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 22);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().salaryMethod(), "Salary Method", 100, "salaryMethod");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().departmentName(), "Department Name", 100, "departmentName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1280, 600, 1);

        dmFind.getHelper().addSort("salaryMethod", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHReceiptVoucher">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdAttendanceEntry_Mdl> {

        @Editor.Path("EmployeeName")
        ValueProvider<GrdAttendanceEntry_Mdl, String> employeeName();

        @Editor.Path("EmployeeCode")
        ValueProvider<GrdAttendanceEntry_Mdl, String> employeeCode();

        @Editor.Path("Day1")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day1();

        @Editor.Path("Day2")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day2();

        @Editor.Path("Day3")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day3();

        @Editor.Path("Day4")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day4();

        @Editor.Path("Day5")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day5();

        @Editor.Path("Day6")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day6();

        @Editor.Path("Day7")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day7();

        @Editor.Path("Day8")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day8();

        @Editor.Path("Day9")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day9();

        @Editor.Path("Day10")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day10();

        @Editor.Path("Day11")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day11();

        @Editor.Path("Day12")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day12();

        @Editor.Path("Day13")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day13();

        @Editor.Path("Day14")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day14();

        @Editor.Path("Day15")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day15();

        @Editor.Path("Day16")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day16();

        @Editor.Path("Day17")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day17();

        @Editor.Path("Day18")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day18();

        @Editor.Path("Day19")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day19();

        @Editor.Path("Day20")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day20();

        @Editor.Path("Day21")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day21();

        @Editor.Path("Day22")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day22();

        @Editor.Path("Day23")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day23();

        @Editor.Path("Day24")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day24();

        @Editor.Path("Day25")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day25();

        @Editor.Path("Day26")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day26();

        @Editor.Path("Day27")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day27();

        @Editor.Path("Day28")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day28();

        @Editor.Path("Day29")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day29();

        @Editor.Path("Day30")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day30();

        @Editor.Path("Day31")
        ValueProvider<GrdAttendanceEntry_Mdl, String> day31();

        /**
         * ************************************************************************************************************
         */
        @Editor.Path("Of_Ho_Lv1")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv1();

        @Editor.Path("Of_Ho_Lv2")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv2();

        @Editor.Path("Of_Ho_Lv3")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv3();

        @Editor.Path("Of_Ho_Lv4")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv4();

        @Editor.Path("Of_Ho_Lv5")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv5();

        @Editor.Path("Of_Ho_Lv6")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv6();

        @Editor.Path("Of_Ho_Lv7")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv7();

        @Editor.Path("Of_Ho_Lv8")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv8();

        @Editor.Path("Of_Ho_Lv9")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv9();

        @Editor.Path("Of_Ho_Lv10")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv10();

        @Editor.Path("Of_Ho_Lv11")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv11();

        @Editor.Path("Of_Ho_Lv12")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv12();

        @Editor.Path("Of_Ho_Lv13")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv13();

        @Editor.Path("Of_Ho_Lv14")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv14();

        @Editor.Path("Of_Ho_Lv15")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv15();

        @Editor.Path("Of_Ho_Lv16")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv16();

        @Editor.Path("Of_Ho_Lv17")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv17();

        @Editor.Path("Of_Ho_Lv18")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv18();

        @Editor.Path("Of_Ho_Lv19")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv19();

        @Editor.Path("Of_Ho_Lv20")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv20();

        @Editor.Path("Of_Ho_Lv21")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv21();

        @Editor.Path("Of_Ho_Lv22")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv22();

        @Editor.Path("Of_Ho_Lv23")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv23();

        @Editor.Path("Of_Ho_Lv24")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv24();

        @Editor.Path("Of_Ho_Lv25")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv25();

        @Editor.Path("Of_Ho_Lv26")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv26();

        @Editor.Path("Of_Ho_Lv27")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv27();

        @Editor.Path("Of_Ho_Lv28")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv28();

        @Editor.Path("Of_Ho_Lv29")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv29();

        @Editor.Path("Of_Ho_Lv30")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv30();

        @Editor.Path("Of_Ho_Lv31")
        ValueProvider<GrdAttendanceEntry_Mdl, String> of_Ho_Lv31();

    }

    final DmGrid<GrdAttendanceEntry_Mdl, Grd_PrpAcs> dmGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        ColumnConfig<GrdAttendanceEntry_Mdl, String> day1, day2, day3, day4, day5, day6, day7, day8, day9, day10,
                day11, day12, day13, day14, day15, day16, day17, day18, day19,
                day20, day21, day22, day23, day24, day25, day26, day27,
                day28, day29, day30, day31,
                of_Ho_Lv1, of_Ho_Lv2, of_Ho_Lv3, of_Ho_Lv4, of_Ho_Lv5, of_Ho_Lv6, of_Ho_Lv7, of_Ho_Lv8, of_Ho_Lv9, of_Ho_Lv10,
                of_Ho_Lv11, of_Ho_Lv12, of_Ho_Lv13, of_Ho_Lv14, of_Ho_Lv15, of_Ho_Lv16, of_Ho_Lv17, of_Ho_Lv18, of_Ho_Lv19,
                of_Ho_Lv20, of_Ho_Lv21, of_Ho_Lv22, of_Ho_Lv23, of_Ho_Lv24, of_Ho_Lv25, of_Ho_Lv26, of_Ho_Lv27,
                of_Ho_Lv28, of_Ho_Lv29, of_Ho_Lv30, of_Ho_Lv31, employee, employeeCode;
        dmGrd.addRowNumberColumn();
        employee = dmGrd.addTextColumn(dmGrd.getPropertyAccess().employeeName(), "Employee Name", 200, "EmployeeName");
        employeeCode = dmGrd.addTextColumn(dmGrd.getPropertyAccess().employeeCode(), "Employee Code", 0, "EmployeeCode");
        employee.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        day1 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day1(), "1", 30, "");
        day2 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day2(), "2", 30, "");
        day3 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day3(), "3", 30, "");
        day4 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day4(), "4", 30, "");
        day5 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day5(), "5", 30, "");
        day6 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day6(), "6", 30, "");
        day7 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day7(), "7", 30, "");
        day8 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day8(), "8", 30, "");
        day9 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day9(), "9", 30, "");
        day10 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day10(), "10", 30, "");
        day11 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day11(), "11", 30, "");
        day12 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day12(), "12", 30, "");
        day13 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day13(), "13", 30, "");
        day14 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day14(), "14", 30, "");
        day15 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day15(), "15", 30, "");
        day16 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day16(), "16", 30, "");
        day17 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day17(), "17", 30, "");
        day18 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day18(), "18", 30, "");
        day19 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day19(), "19", 30, "");
        day20 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day20(), "20", 30, "");
        day21 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day21(), "21", 30, "");
        day22 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day22(), "22", 30, "");
        day23 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day23(), "23", 30, "");
        day24 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day24(), "24", 30, "");
        day25 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day25(), "25", 30, "");
        day26 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day26(), "26", 30, "");
        day27 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day27(), "27", 30, "");
        day28 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day28(), "28", 30, "");
        day29 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day29(), "29", 30, "");
        day30 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day30(), "30", 30, "");
        day31 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().day31(), "31", 30, "");

        of_Ho_Lv1 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv1(), "1", 0, "");
        of_Ho_Lv2 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv2(), "2", 0, "");
        of_Ho_Lv3 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv3(), "3", 0, "");
        of_Ho_Lv4 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv4(), "4", 0, "");
        of_Ho_Lv5 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv5(), "5", 0, "");
        of_Ho_Lv6 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv6(), "6", 0, "");
        of_Ho_Lv7 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv7(), "7", 0, "");
        of_Ho_Lv8 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv8(), "8", 0, "");
        of_Ho_Lv9 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv9(), "9", 0, "");
        of_Ho_Lv10 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv10(), "10", 0, "");
        of_Ho_Lv11 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv11(), "11", 0, "");
        of_Ho_Lv12 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv12(), "12", 0, "");
        of_Ho_Lv13 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv13(), "13", 0, "");
        of_Ho_Lv14 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv14(), "14", 0, "");
        of_Ho_Lv15 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv15(), "15", 0, "");
        of_Ho_Lv16 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv16(), "16", 0, "");
        of_Ho_Lv17 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv17(), "17", 0, "");
        of_Ho_Lv18 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv18(), "18", 0, "");
        of_Ho_Lv19 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv19(), "19", 0, "");
        of_Ho_Lv20 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv20(), "20", 0, "");
        of_Ho_Lv21 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv21(), "21", 0, "");
        of_Ho_Lv22 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv22(), "22", 0, "");
        of_Ho_Lv23 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv23(), "23", 0, "");
        of_Ho_Lv24 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv24(), "24", 0, "");
        of_Ho_Lv25 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv25(), "25", 0, "");
        of_Ho_Lv26 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv26(), "26", 0, "");
        of_Ho_Lv27 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv27(), "27", 0, "");
        of_Ho_Lv28 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv28(), "28", 0, "");
        of_Ho_Lv29 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv29(), "29", 0, "");
        of_Ho_Lv30 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv30(), "30", 0, "");
        of_Ho_Lv31 = dmGrd.addTextColumn(dmGrd.getPropertyAccess().of_Ho_Lv31(), "31", 0, "");

        dmGrd.initializeGrid(dmGrd.getPropertyAccess().fieldHashCode(), 1270, 470);

//============================================================================================
        employee.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = dmGrd.getGrid().getStore().get(context.getIndex()).getEmployeeName();

                sb.appendHtmlConstant("<span class = '' style='font-size:1.3em;color:black;font-weight: bold;'>" + val + "</span>");

            }
        });
        day1.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv1());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay1());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;font-weight: bold;color:rgb(242, 247, 252);display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day2.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv2());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay2());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;font-weight: bold;color:rgb(242, 247, 252);display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day3.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv3());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay3());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight:bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;font-weight: bold;color:rgb(242, 247, 252);display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day4.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv4());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay4());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;font-weight: bold;color:rgb(242, 247, 252);display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day5.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv5());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay5());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day6.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv6());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay6());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day7.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv7());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay7());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day8.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv8());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay8());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day9.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv9());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay9());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day10.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv10());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay10());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day11.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv11());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay11());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day12.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv12());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay12());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day13.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv13());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay13());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day14.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv14());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay14());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day15.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv15());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay15());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day16.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv16());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay16());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day17.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv17());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay17());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day18.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv18());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay18());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day19.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv19());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay19());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day20.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv20());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay20());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day21.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv21());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay21());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day22.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv22());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay22());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day23.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv23());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay23());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day24.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv24());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay24());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day25.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv25());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay25());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day26.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv26());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay26());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day27.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv27());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay27());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day28.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv28());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay28());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("NA")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;'></span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day29.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv29());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay29());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("NA")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;'></span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day30.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv30());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay30());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("NA")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;'></span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>"+ manualCode+"</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
        day31.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                String val = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getOf_Ho_Lv31());
                String manualCode = DmShared.xNull(dmGrd.getGrid().getStore().get(context.getIndex()).getDay31());
                if (val.equalsIgnoreCase("AA")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:red;font-weight: bold;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("HH")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:rgb(153, 255, 204);'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("NA")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;'></span>");
                } else if (val.equalsIgnoreCase("OFD")) {
                    sb.appendHtmlConstant("<span  style='display:block;height:20px;background-color:red;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("PP")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:black;'>" + manualCode + "</span>");
                } else if (val.equalsIgnoreCase("LV")) {
                    sb.appendHtmlConstant("<span  style='font-size:1.3em;color:rgb(242, 247, 252);font-weight: bold;display:block;height:20px;background-color: rgb(63, 200, 242);'>" + manualCode + "</span>");
                }
            }
        });
//==================================================================================================
        return dmGrd.getGrid();

    }

    interface TxtHSalaryMethod_PrpAcs extends KeyField_T_PrpAcs<SalaryMethod_Mdl> {

        @Editor.Path("SalaryMethodName")
        ValueProvider<SalaryMethod_Mdl, String> salaryMethodName();
    }

    final DmTextHelp<SalaryMethod_Mdl, TxtHSalaryMethod_PrpAcs> txtHSalaryMethod = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHSalaryMethod_PrpAcs.class), 5, true);

    private void iniTxtHSalaryMethod() {
        txtHSalaryMethod.getDmGrid().addTextColumn(txtHSalaryMethod.getDmGrid().getPropertyAccess().salaryMethodName(), "Salary Method", 180, "name");
        txtHSalaryMethod.getDmGrid().initializeGrid(txtHSalaryMethod.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHSalaryMethod.getHelper().addSort("name", DmSortType.ASC);
    }

    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDepartmentName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHDepartmentName() {
        txtHDepartmentName.getDmGrid().addTextColumn(txtHDepartmentName.getDmGrid().getPropertyAccess().name(), "Department Name", 180, "name");
        txtHDepartmentName.getDmGrid().initializeGrid(txtHDepartmentName.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHDepartmentName.getHelper().addSort("name", DmSortType.ASC);
    }

    public AttendanceEntry_Dsg() {
        ini();
        iniTxtHDepartmentName();
        iniTxtHSalaryMethod();

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
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel legendSection() {
        FlowPanel flwTable, flwTr, flwTd, flwTemp;
        Label lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "20%", "90px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        flwTable.getElement().setAttribute("style", "border: 1px solid rgb(224, 223, 220);height:90px;");

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "90%", "25px", "right", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 90%; display: block; float: left;");
        flwTemp = new FlowPanel();
        flwTemp.getElement().setAttribute("style", "width: 32px; height: 20px; background-color: red; float: left; margin-right: 10px; ");
        flwTd.add(flwTemp);
        lbl = new Label("Off Day");
        lbl.getElement().getStyle().setPaddingTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "90%", "25px", "right", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 90%; display: block; float: left;");
        flwTemp = new FlowPanel();
        flwTemp.getElement().setAttribute("style", "width: 32px; height: 20px; background-color: rgb(63, 200, 242); float: left; margin-right: 10px; ");
        flwTd.add(flwTemp);
        lbl = new Label("Leave");
        lbl.getElement().getStyle().setPaddingTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "90%", "25px", "right", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        flwTd.getElement().setAttribute("style", "width: 90%; display: block; float: left;");
        flwTemp = new FlowPanel();
        flwTemp.getElement().setAttribute("style", "width: 32px; height: 20px; background-color: rgb(153, 255, 204); float: left; margin-right: 10px;");
        flwTd.add(flwTemp);
        lbl = new Label("Holiday");
        lbl.getElement().getStyle().setPaddingTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "575px", null, null, DmPosition.CENTER, true);

        //================1st Row============================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "105px", "left", null, DmPosition.NONE, false);

        //===========Left Section=====================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "49%", "105px", "left", null, DmPosition.NONE, false);
        flwTD.add(leftSection());
        flwTR.add(flwTD);

        //==============Right Section==================================
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "49%", "105px", "left", null, DmPosition.NONE, false);
        flwTD.add(rightSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //===============2nd row=======================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", "470px", "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "98%", "470px", "left", null, DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);
        flwTD.add(grdSection());
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "105px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(4, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Salary Method");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);

        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSalaryMethod));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /*====================================< 2nd Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTR.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Department Name");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "50%", null, "left", null, DmPosition.NONE, false);

        flwTD.add(vl.add(txtHDepartmentName));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

    private FlowPanel rightSection() {
        FlowPanel flwTable, flwTr, flwTd, flwColorTable, colorTr, colorTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "105px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "105px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(6, Style.Unit.PCT);
        btnFillDetails.setWidth("60%");//btnFillDetails.setWidth("60%");
        btnFillDetails.addStyleName("dm-button-grad");
        btnFillDetails.setHeight("20px");
        btnFillDetails.getElement().setAttribute("style", btnFillDetails.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;margin-left: 50px;margin-top: 13px;");
        btnFillDetails.setTitle("Fill Details");
        flwButtonFillDetails.add(vl.add(btnFillDetails));
        flwTd.add(flwButtonFillDetails);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        btngetAttendance.setWidth("60%");
        btngetAttendance.addStyleName("dm-button-grad");
        btngetAttendance.setHeight("20px");
        btngetAttendance.getElement().setAttribute("style", btnFillDetails.getElement().getAttribute("style")
                + ";font-size:12px;padding:0px;margin-left: 50px;margin-top: 13px;");
        btngetAttendance.setTitle("Fill Details");
        flwButtonGetAttendance.add(vl.add(btnFillDetails));
        flwTd.add(flwButtonFillDetails);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "90px", "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        flwTd.add(legendSection());
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel grdSection() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "470px", null, null, DmPosition.CENTER, true);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(createGrid()));

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl createBottom() {

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

}
