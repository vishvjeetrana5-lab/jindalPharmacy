package dataman.payroll.self.server;


import dataman.payroll.attendanceEntry.client.AttendanceEntry;
import dataman.payroll.attendanceEntry.server.AttendanceEntry_SrvImpl;
import dataman.payroll.attendenceCodeDefinition.client.AttendenceCodeDefinition;
import dataman.payroll.attendenceCodeDefinition.server.AttendenceCodeDefinition_SrvImpl;
import dataman.payroll.attendenceEntryManual.client.AttendenceEntryManual;
import dataman.payroll.attendenceEntryManual.server.AttendenceEntryManual_SrvImpl;
import dataman.payroll.attendanceImport.client.AttendanceImport;
import dataman.payroll.attendanceImport.server.AttendanceImport_SrvImpl;
import dataman.payroll.departmentMaster.client.DepartmentMaster;
import dataman.payroll.departmentMaster.server.DepartmentMaster_SrvImpl;
import dataman.payroll.earningDeductionMaster.client.EarningDeductionMaster;
import dataman.payroll.earningDeductionMaster.server.EarningDeductionMaster_SrvImpl;
import dataman.payroll.employeeMaster.client.EmployeeMaster;
import dataman.payroll.employeeMaster.server.EmployeeMaster_SrvImpl;
import dataman.payroll.employeeShiftDetail.client.EmployeeShiftDetail;
import dataman.payroll.employeeShiftDetail.server.EmployeeShiftDetail_SrvImpl;
import dataman.payroll.holidayMaster.client.HolidayMaster;
import dataman.payroll.holidayMaster.server.HolidayMaster_SrvImpl;
import dataman.payroll.leaveEntry.client.LeaveEntry;
import dataman.payroll.leaveEntry.server.LeaveEntry_SrvImpl;
import dataman.payroll.leaveEntryApproval.client.LeaveEntryApproval;
import dataman.payroll.leaveEntryApproval.server.LeaveEntryApproval_SrvImpl;
import dataman.payroll.leaveMaster.client.LeaveMaster;
import dataman.payroll.leaveMaster.server.LeaveMaster_SrvImpl;
import dataman.payroll.leavingMaster.client.LeavingMaster;
import dataman.payroll.leavingMaster.server.LeavingMaster_SrvImpl;
import dataman.payroll.reports.client.RptAttendanceRegister;
import dataman.payroll.reports.client.RptLeaveRegister;
import dataman.payroll.reports.client.RptSalarySheet;
import dataman.payroll.reports.client.RptDeptWiseSalarySheet;
import dataman.payroll.reports.server.RptAttendanceRegister_SrvImpl;
import dataman.payroll.reports.server.RptLeaveRegister_SrvImpl;
import dataman.payroll.reports.server.RptSalarySheet_SrvImpl;
import dataman.payroll.reports.server.RptDeptWiseSalarySheet_SrvImpl;
import dataman.payroll.salaryCodeDefinition.client.SalaryCodeDefinition;
import dataman.payroll.salaryCodeDefinition.server.SalaryCodeDefinition_SrvImpl;
import dataman.payroll.salaryCreation.client.SalaryCreation;
import dataman.payroll.salaryCreation.server.SalaryCreation_SrvImpl;
import dataman.payroll.shiftMaster.client.ShiftMaster;
import dataman.payroll.shiftMaster.server.ShiftMaster_SrvImpl;
import dataman.payroll.loanEntry.client.LoanEntry;
import dataman.payroll.loanEntry.server.LoanEntry_SrvImpl;
import dataman.payroll.employeeExpenceEntry.client.EmployeeExpenceEntry;
import dataman.payroll.employeeExpenceEntry.server.EmployeeExpenceEntry_SrvImpl;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Payroll_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;
        try {

        
            strPath = HolidayMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, HolidayMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = LeaveMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LeaveMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DepartmentMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DepartmentMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = LeavingMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LeavingMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SalaryCodeDefinition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SalaryCodeDefinition_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AttendenceCodeDefinition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AttendenceCodeDefinition_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = EarningDeductionMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EarningDeductionMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = EmployeeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmployeeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = AttendanceEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AttendanceEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = ShiftMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ShiftMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = EmployeeShiftDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmployeeShiftDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = LeaveEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LeaveEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = LeaveEntryApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LeaveEntryApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = AttendenceEntryManual.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AttendenceEntryManual_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = AttendanceImport.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AttendanceImport_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = LoanEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LoanEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = EmployeeExpenceEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmployeeExpenceEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            
            strPath = SalaryCreation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SalaryCreation_SrvImpl.class);
            dyn.addMapping("/" + strPath);
			
			strPath = RptLeaveRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptLeaveRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = RptSalarySheet.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSalarySheet_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = RptDeptWiseSalarySheet.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptDeptWiseSalarySheet_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
            strPath = RptAttendanceRegister.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptAttendanceRegister_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
