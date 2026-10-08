package aarogya.mis.self.shared;

import aarogya.mis.reports.client.RptCollectionRegister;
import aarogya.projectMenu.self.shared.pmMIS;
import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class MIS_Model {

    pmMIS pmMenu = new pmMIS();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getReports(mainMenuList);

        menuList.add(new UserMenu("Standard Reports"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getOPDReports(mainMenuList);
        getIPDReports(mainMenuList);
        getLabReports(mainMenuList);
        getNursingStationReports(mainMenuList);

        return menuList;
    }

//<editor-fold defaultstate="collapsed" desc="Reports">
    private void getReports(List<UserMenu> menuList) {
        menuList.add(new UserMenu("General Reports"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.COLLECTION_REGISTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.COLLECTION_REGISTER_USERWISE.caption));
        menuList.get(menuList.size() - 1).getUserMenu().get(menuList.get(menuList.size() - 1).getUserMenu().size() - 1).getControlPermission().add(RptCollectionRegister.userNameEditableBlog);
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.POS_PATMENT_COLLECTION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CONSULTANT_SHARING.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.UNBILLED_SERVICE_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.EMERGENCY_STATISTICS_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.BIRTHDAY_ANNIVERSARY_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.NET_REVENUE_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.GRAPHICAL_DASHBOARD.caption));
//        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.MODIFICATION_HISTORY.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.MODIFICATION_Log.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.PATIENT_LEDGER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.PATIENT_SUMMARY.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.DATE_WISE_COLLECTION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.UHID_REGISTRATION.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.PATIENT_WALLET_REGISTER.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CONTROL_ACCOUNTS_DETAILS_REPORT.caption));

        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CANCEL_SERVICE_REFUND_REPORT.caption));
        

    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="OPD">
    private void getOPDReports(List<UserMenu> menuList) {
        menuList.add(new UserMenu("OPD"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CONSULTATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CONSULTANT_WISE_CONSULTATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OPD_SERVICES_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CATEGORY_WISE_SERVICE_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OPD_SERVICE_CANCELLATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.DEPARTMENT_WISE_CONSULTATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.AREA_WISE_CONSULTATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CASE_WISE_CONSULTATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.CASTE_WISE_CONSULTATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OPD_DISCOUNT_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.OPD_REFUND_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.UNBILLED_OPD_ORGANIZATION_REPORT.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="IPD Reports">
    private void getIPDReports(List<UserMenu> menuList) {
        menuList.add(new UserMenu("IPD"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.ROOM_CHANGE_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.IPD_SERVICES_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.IPD_REFUND_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.DISCHARGE_PATIENT_DUES_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.DISCHARGE_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.ROOM_CATEGORY_WISE_IPD_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.IPD_DISCOUNT_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.IPD_SERVICE_CANCELLATION_REPORT.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Lab Reports">
    private void getLabReports(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Diagnostic"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.AREA_WISE_LAB_REGISTRATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.PACKAGE_WISE_INVESTIGATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.LAB_DUES_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.LAB_CANCELLATION_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.LAB_DISCOUNT_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.LAB_REFUND_REPORT.caption));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.COLLECTION_BOY_REPORT.caption));
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Nursing Station Reports">
    private void getNursingStationReports(List<UserMenu> menuList) {
        menuList.add(new UserMenu("Nursing Station"));
        menuList.get(menuList.size() - 1).getUserMenu().add(new UserMenu(pmMenu.TASK_EXECUTION_REPORT.caption));

    }
//</editor-fold>

}
