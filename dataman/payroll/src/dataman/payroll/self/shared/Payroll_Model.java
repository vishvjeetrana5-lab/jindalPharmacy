package dataman.payroll.self.shared;

import dataman.libraryMenu.self.shared.lmPayroll;
import dataman.dmBase.self.shared.UserMenu;
import dataman.payroll.employeeMaster.client.EmployeeMaster;
import java.util.ArrayList;
import java.util.List;

public class Payroll_Model {

    private lmPayroll pmMenu = new lmPayroll();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Masters"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getMaster(mainMenuList);
        
    //    getTransaction(mainMenuList);
        
        menuList.add(new UserMenu("Transaction"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getTransaction(mainMenuList);
        
        menuList.add(new UserMenu("Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getReports(mainMenuList);

        return menuList;
    }

//<editor-fold defaultstate="collapsed" desc="Master">
    private void getMaster(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("GENERAL"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.DEPARTMENT_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.LEAVING_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.EMPLOYEE_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.deleteFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.addLeave);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.addEarningDeduction);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.addFamily);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.addSoftId);
        subMenuList.add(new UserMenu(pmMenu.HOLIDAY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.LEAVE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SALARY_CODE_DEFINITION.caption));
        subMenuList.add(new UserMenu(pmMenu.ATTENDENCE_CODE_DEFINITION.caption));
        subMenuList.add(new UserMenu(pmMenu.EARNING_DEDUCATION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SHIFT_MASTER.caption));
        
        
    }
//</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="Transaction">
    private void getTransaction(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;
        menuList.add(new UserMenu("EMPLOYEE"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        
        subMenuList.add(new UserMenu(pmMenu.ATTENDANCE_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.ATTENDANCE_ENTRY_MANUAL.caption));
        subMenuList.add(new UserMenu(pmMenu.SALARY_CREATION.caption));
        subMenuList.add(new UserMenu(pmMenu.LEAVE_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.LEAVE_ENTRY_APPROVAL.caption));
        subMenuList.add(new UserMenu(pmMenu.ATTENDANCE_IMPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.EMPLOYEE_SHIFT_DETAIL.caption));
        subMenuList.add(new UserMenu(pmMenu.LOAN_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.PERKS_ENTRY.caption));
    }
    //</editor-fold>
    
     private void getReports(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("General Reports"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.LEAVE_REGISTER_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.SALARY_SHEET_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.DEPT_WISE_SALARY_SHEET_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.ATTENDANCE_REGISTER_REPORT.caption));
    }

}
