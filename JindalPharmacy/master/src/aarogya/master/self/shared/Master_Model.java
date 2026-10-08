package aarogya.master.self.shared;

import aarogya.master.additionalUserPermission.client.AdditionalUserPermission;
import aarogya.master.serviceMaster.client.ServiceMaster;
import aarogya.master.consultantMaster.client.ConsultantMaster;
import aarogya.master.employeeMaster.client.EmployeeMaster;
import aarogya.master.environmentService.client.EnvironmentService;
import aarogya.master.eod.client.EOD;
import aarogya.master.organizationMaster.client.OrganizationMaster;
import aarogya.master.organizationServiceMaster.client.OrganizationServiceMaster;
import aarogya.master.packageMaster.client.PackageMaster;
import aarogya.master.prescriptionValueTemplate.client.PrescriptionValueTemplate;
import aarogya.master.priceListMaster.client.PriceListMaster;
import aarogya.master.rateListMaster.client.RateListMaster;
import aarogya.master.referredMaster.client.ReferredMaster;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.UserMenu;
import java.util.ArrayList;
import java.util.List;

public class Master_Model {

    pmMaster pmMenu = new pmMaster();

    public List<UserMenu> getMenu() {
        List<UserMenu> menuList = new ArrayList<>();
        List<UserMenu> mainMenuList;

        menuList.add(new UserMenu("General"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getCommon(mainMenuList);
        getConsultant(mainMenuList);
        getManageCard(mainMenuList);
        getCorporate(mainMenuList);
        getPatientSchedule(mainMenuList);
        getRoom(mainMenuList);

        menuList.add(new UserMenu("Configs"));
        mainMenuList = menuList.get(menuList.size() - 1).getUserMenu();
        getAdmin(mainMenuList);
        getSharing(mainMenuList);
//        getBillHandOver(mainMenuList);
        getImportExportData(mainMenuList);

        return menuList;
    }

    private void getCommon(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Common"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.HOLIDAY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.OCCUPATION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.DESIGNATION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.DEPARTMENT_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.FREQUENCY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.CASE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ICD_X_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.ALLERGY_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.SERVICE_CATEGORY_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.SERVICE_GROUP_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.SERVICE_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ServiceMaster.priceListBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ServiceMaster.packageDetailBlog);

        subMenuList.add(new UserMenu(pmMenu.PACKAGE_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PackageMaster.priceListBlog);

        subMenuList.add(new UserMenu(pmMenu.PACKAGE_PRICE_LIST.caption));

        subMenuList.add(new UserMenu(pmMenu.INSURANCE_COMPANY_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.ORGANIZATION_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(OrganizationMaster.priceListBlog);

//        subMenuList.add(new UserMenu(pmMenu.SERVICE_PRICELIST_MASTER.caption));
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ServicePriceList.priceListBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ServicePriceList.percentBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ServicePriceList.savePriceBlog);
//        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ServicePriceList.deletePriceBlog);
        subMenuList.add(new UserMenu(pmMenu.COLLECTION_CENTER_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(OrganizationMaster.priceListBlog);

        subMenuList.add(new UserMenu(pmMenu.SERVICE_CATEGORY_APPLICABLE_TO.caption));

        subMenuList.add(new UserMenu(pmMenu.HSN_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.EMPLOYEE_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EmployeeMaster.deleteFileBlog);

//        subMenuList.add(new UserMenu(pmMenu.EARNDEDUCTION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PRESCRIPTION_CUSTOM_GROUP_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.PRESCRIPTION_CUSTOM_ITEM_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.PATIENT_TYPE_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.RATE_LIST_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(RateListMaster.newRateListBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(RateListMaster.priceListBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(RateListMaster.percentBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(RateListMaster.savePriceBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(RateListMaster.deletePriceBlog);

        subMenuList.add(new UserMenu(pmMenu.ITEM_PRICELIST_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PriceListMaster.savePriceBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PriceListMaster.priceListBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PriceListMaster.newRateListBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PriceListMaster.deletePriceBlog);

        subMenuList.add(new UserMenu(pmMenu.DISABILITY_MASTER.caption));
    }

    private void getSharing(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Sharing"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.SHARING_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.SALARY_CODE_DEFINITION.caption));
        subMenuList.add(new UserMenu(pmMenu.EARNDEDUCTION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.WORKING_DAYS.caption));
        subMenuList.add(new UserMenu(pmMenu.CATEGORY_POOL_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.POOL_GROUP_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.POOL_GROUP_DETAILS_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.EARNING_DEDUCTION_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.POOL_EARNING_DEDUCTION_ENTRY.caption));
        subMenuList.add(new UserMenu(pmMenu.CONSULTANT_SHARING_REPORT.caption));
        subMenuList.add(new UserMenu(pmMenu.PROVISIONAL_STATEMENT.caption));
        subMenuList.add(new UserMenu(pmMenu.FINAL_SHARING.caption));

    }

    private void getConsultant(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Consultant"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.SPECIALIZATION_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.CONSULTANT_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ConsultantMaster.priceListBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ConsultantMaster.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ConsultantMaster.deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.REFERRED_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ReferredMaster.attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(ReferredMaster.deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.PRESCRIPTION_VALUE_TEMPLATE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PrescriptionValueTemplate.addMedicineEditable);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PrescriptionValueTemplate.addFrequencyEditable);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(PrescriptionValueTemplate.addAllergyEditable);

        subMenuList.add(new UserMenu(pmMenu.CONSULTANT_LEAVE.caption));
        subMenuList.add(new UserMenu(pmMenu.CONSULTANT_SITTING_LOCATION.caption));
    }

    private void getRoom(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Room"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.NURSING_STATION_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ROOM_CATEGORY_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.ROOM_MASTER.caption));
    }

    private void getManageCard(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Manage Card"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.CARD_TYPE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.CARD_RECEIVE.caption));
        subMenuList.add(new UserMenu(pmMenu.CARD_REGISTRATION.caption));
        subMenuList.add(new UserMenu(pmMenu.CARD_RECHARGE.caption));
        subMenuList.add(new UserMenu(pmMenu.CARD_REFUND.caption));
        subMenuList.add(new UserMenu(pmMenu.CARD_DEACTIVE.caption));
    }

    private void getCorporate(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Corporate"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.CORPORATE_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.CORPORATE_EMPLOYEE_MASTER.caption));
    }

    public static final String patientFollowUpBlog = "Patient Follow-Up";
    public static final String saveScheduleBlog = "Save Schedule";
    public static final String deleteSchduleBlog = "Delete Schedule";
    public static final String manageSchduleBlog = "Change Schedule";

    private void getPatientSchedule(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Patient Schedule"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.PATIENT_SCHEDULE_GROUP_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.PATIENT_SCHEDULE_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.MANAGE_PATIENT_SCHEDULE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(patientFollowUpBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(saveScheduleBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(deleteSchduleBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(manageSchduleBlog);
    }

    private void getImportExportData(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("D365 Data"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.IMPORT_EXPORT_DATA.caption));

    }

    public static final String uhidRegistration_approvedByBlog = "Approved By";
    public static final String uhidRegistration_attachFileBlog = "Attach Files";
    public static final String uhidRegistration_deleteFileBlog = "Delete Files";

    private void getAdmin(List<UserMenu> menuList) {
        List<UserMenu> subMenuList;

        menuList.add(new UserMenu("Admin"));
        subMenuList = menuList.get(menuList.size() - 1).getUserMenu();

        subMenuList.add(new UserMenu(pmMenu.ENVIRONMENT_SETTING.caption));

        subMenuList.add(new UserMenu(pmMenu.ENVIRONMENT_SETTING_COMPANY.caption));

        subMenuList.add(new UserMenu(pmMenu.ENVIRONMENT_SERVICE.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EnvironmentService.tabServiceCategory);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EnvironmentService.tabDiagnosticCategory);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EnvironmentService.tabBloodComponent);

        subMenuList.add(new UserMenu(pmMenu.CUSTOM_ENVIRO.caption));

        subMenuList.add(new UserMenu(pmMenu.ADDITIONAL_USER_PERMISSION.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AdditionalUserPermission.additionalPermissionBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AdditionalUserPermission.createUserBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AdditionalUserPermission.userPermissionBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(AdditionalUserPermission.manageUserRoleBlog);

        subMenuList.add(new UserMenu(pmMenu.UHID_MODIFICATION.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(uhidRegistration_approvedByBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(uhidRegistration_attachFileBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(uhidRegistration_deleteFileBlog);

        subMenuList.add(new UserMenu(pmMenu.PRESCRIPTION_GUI_MAPPING.caption));

        subMenuList.add(new UserMenu(pmMenu.EOD.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.dayCareAcPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.diagnosticAcPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.emergencyAcPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.ipdAcPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.opdAcPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.labConsumptionBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.roomPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.pharmacyAcPostingBlog);
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(EOD.misAppUpdation);

        subMenuList.add(new UserMenu(pmMenu.IMPORT_DATA.caption));

        subMenuList.add(new UserMenu(pmMenu.ORGANIZATION_SERVICE_MASTER.caption));
        subMenuList.get(subMenuList.size() - 1).getControlPermission().add(OrganizationServiceMaster.priceListBlog);

        subMenuList.add(new UserMenu(pmMenu.IMPORT_ORGANIZATION_RATELIST.caption));

        subMenuList.add(new UserMenu(pmMenu.OVERHEAD_SERVICE_CATEGORY_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.OVERHEAD_SERVICE_MASTER.caption));

        subMenuList.add(new UserMenu(pmMenu.FOREIGN_CURRENCY_EXCHANGE.caption));

        subMenuList.add(new UserMenu(pmMenu.TAB_INDEX_SETTING.caption));
        subMenuList.add(new UserMenu(pmMenu.GENERATE_GST_CSV.caption));
        subMenuList.add(new UserMenu(pmMenu.RECEIPT_BOOK_MAPPING.caption));
        subMenuList.add(new UserMenu(pmMenu.LOG_CONFIGURATION.caption));
        subMenuList.add(new UserMenu(pmMenu.DISCOUNT_REASON_MASTER.caption));
        subMenuList.add(new UserMenu(pmMenu.GUI_MAPPING_MASTER.caption));
    }
}
