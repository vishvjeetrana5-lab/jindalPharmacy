package dataman.payroll.self.client;

import dataman.libraryMenu.self.shared.lmPayroll;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.payroll.attendanceEntry.client.AttendanceEntry;
import dataman.payroll.attendanceImport.client.AttendanceImport;
import dataman.payroll.attendenceCodeDefinition.client.AttendenceCodeDefinition;
import dataman.payroll.reports.client.*;
import dataman.payroll.departmentMaster.client.DepartmentMaster;
import dataman.payroll.earningDeductionMaster.client.EarningDeductionMaster;
import dataman.payroll.employeeMaster.client.EmployeeMaster;
import dataman.payroll.holidayMaster.client.HolidayMaster;
import dataman.payroll.leaveEntry.client.LeaveEntry;
import dataman.payroll.attendenceEntryManual.client.AttendenceEntryManual;
import dataman.payroll.leaveEntryApproval.client.LeaveEntryApproval;
import dataman.payroll.leaveMaster.client.LeaveMaster;
import dataman.payroll.leavingMaster.client.LeavingMaster;
import dataman.payroll.salaryCodeDefinition.client.SalaryCodeDefinition;
import dataman.payroll.salaryCreation.client.SalaryCreation;
import dataman.payroll.shiftMaster.client.ShiftMaster;
import dataman.payroll.employeeShiftDetail.client.EmployeeShiftDetail;
import dataman.payroll.loanEntry.client.LoanEntry;
import dataman.payroll.employeeExpenceEntry.client.EmployeeExpenceEntry;

import java.util.HashMap;
import java.util.List;

public class Payroll implements IsWidget {

    private lmPayroll pmMenu = new lmPayroll();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public Payroll(final SetWidgetInvoker swiMain) {
        super();
        iwiTemp = new DmMenuItem.InitializeWidgetInvoker() {
            @Override
            public void initializeWidget(String menuText) {
                invokeSetWidget(menuText, swiMain);
            }
        };
        menu = new DmMenuBar();
    }

    public void addMenu(List<UserMenu> menuList) {
        DmClient.addMenuBarItem(menuList, menu, iwiTemp);
    }

    @Override
    public Widget asWidget() {
        return menu.asWidget();
    }

    public void invokeSetWidget(final String menuText, final SetWidgetInvoker swiMain) {
        invokeSetWidget(menuText, null, null, swiMain);
    }

    public void invokeSetWidget(final String menuText, final HashMap paramHashMap, UserMenu userMenu, final SetWidgetInvoker swiMain) {
        final PopupPanel ppCallBackWait = new PopupPanel(false, true);
        FlowPanel flwTemp = new FlowPanel();
        try {
            ppCallBackWait.setGlassStyleName("dm-PopupPanelGlass");
            flwTemp.setWidth("100%");
            ppCallBackWait.getElement().setAttribute("style", "border-radius: 50px 50px 50px 50px;background:none;border:none;rgb(59, 153, 217);padding:0px;");
            ppCallBackWait.add(DmClient.createProgressBar());
            ppCallBackWait.setGlassEnabled(true);
            ppCallBackWait.show();
            ppCallBackWait.center();
            final UserMenu menuPermission;

            if (userMenu == null) {
                menuPermission = DmClient.searchMenu(pmMenu.moduleName, menuText);
            } else {
                menuPermission = userMenu;
            }

            if (menuPermission == null) {
                ppCallBackWait.hide(true);
                return;
            }

            if (swiMain == null) {
                ppCallBackWait.hide(true);
                return;
            }

            if (menuText.trim().equalsIgnoreCase(pmMenu.DEPARTMENT_MASTER.caption)) {
                new DepartmentMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new DepartmentMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEAVING_MASTER.caption)) {
                new LeavingMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new LeavingMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.HOLIDAY_MASTER.caption)) {
                new HolidayMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new HolidayMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEAVE_MASTER.caption)) {
                new LeaveMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new LeaveMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALARY_CODE_DEFINITION.caption)) {
                new SalaryCodeDefinition(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new SalaryCodeDefinition(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ATTENDENCE_CODE_DEFINITION.caption)) {
                new AttendenceCodeDefinition(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new AttendenceCodeDefinition(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EARNING_DEDUCATION_MASTER.caption)) {
                new EarningDeductionMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new EarningDeductionMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EMPLOYEE_MASTER.caption)) {
                new EmployeeMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new EmployeeMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ATTENDANCE_ENTRY.caption)) {
                new AttendanceEntry(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new AttendanceEntry(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //=========================================< Shift Master >==============================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.SHIFT_MASTER.caption)) {
                new ShiftMaster(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new ShiftMaster(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //=========================================< Employee Shift Detail >==============================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.EMPLOYEE_SHIFT_DETAIL.caption)) {
                new EmployeeShiftDetail(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new EmployeeShiftDetail(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //=========================================< Loan Entry >==============================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.LOAN_ENTRY.caption)) {
                new LoanEntry(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new LoanEntry(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PERKS_ENTRY.caption)) {
                new EmployeeExpenceEntry(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new EmployeeExpenceEntry(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //========================================< Leave Entry>=============================================================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.LEAVE_ENTRY.caption)) {
                new LeaveEntry(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new LeaveEntry(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //========================================< Attendence Entry Manual>=============================================================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.ATTENDANCE_ENTRY_MANUAL.caption)) {
                new AttendenceEntryManual(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new AttendenceEntryManual(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //========================================< Attendance Import >=============================================================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.ATTENDANCE_IMPORT.caption)) {
                new AttendanceImport(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new AttendanceImport(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } //========================================< Leave Entry Approval >=============================================================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.LEAVE_ENTRY_APPROVAL.caption)) {
                new LeaveEntryApproval(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new LeaveEntryApproval(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALARY_CREATION.caption)) {
                new SalaryCreation(menuPermission, paramHashMap, swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new SalaryCreation(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LEAVE_REGISTER_REPORT.caption)) {
                new RptLeaveRegister(swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new RptLeaveRegister(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SALARY_SHEET_REPORT.caption)) {
                new RptSalarySheet(swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new RptSalarySheet(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DEPT_WISE_SALARY_SHEET_REPORT.caption)) {
                new RptDeptWiseSalarySheet(swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new RptDeptWiseSalarySheet(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ATTENDANCE_REGISTER_REPORT.caption)) {
                new RptAttendanceRegister(swiMain);
                ppCallBackWait.hide(true);
//                GWT.runAsync(new RunAsyncCallback() {
//
//                    @Override
//                    public void onFailure(Throwable reason) {
//                        ppCallBackWait.hide(true);
//                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        new RptAttendanceRegister(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else {
                ppCallBackWait.hide(true);
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }
}
