package aarogya.master.self.server;

import aarogya.master.D365ImportExport.client.ImportExportData;
import aarogya.master.D365ImportExport.server.CommanScheduler_Server;
import aarogya.master.D365ImportExport.server.D365ToHISItemSync_Server;
import aarogya.master.D365ImportExport.server.ImportExportData_SrvImpl;
import aarogya.master.additionalUserPermission.client.AdditionalUserPermission;
import aarogya.master.additionalUserPermission.server.AdditionalUserPermission_SrvImpl;
import aarogya.master.allergyMaster.client.AllergyMaster;
import aarogya.master.allergyMaster.server.AllergyMaster_SrvImpl;
import aarogya.master.cardDeactive.client.CardDeactivate;
import aarogya.master.cardDeactive.server.CardDeactivate_SrvImpl;
import aarogya.master.prescriptionGuiMapping.client.PrescriptionGuiMapping;
import aarogya.master.prescriptionGuiMapping.server.PrescriptionGuiMapping_SrvImpl;
import aarogya.master.serviceMaster.client.ServiceMaster;
import aarogya.master.serviceMaster.server.ServiceMaster_SrvImpl;
import aarogya.master.cardRegistration.client.CardRegistration;
import aarogya.master.cardRegistration.server.CardRegistration_SrvImpl;
import aarogya.master.cardDischarge.client.CardDischarge;
import aarogya.master.cardDischarge.server.CardDischarge_SrvImpl;
import aarogya.master.cardReceive.client.CardReceive;
import aarogya.master.cardReceive.server.CardReceive_SrvImpl;
import aarogya.master.cardRecharge.client.CardRecharge;
import aarogya.master.cardRecharge.server.CardRecharge_SrvImpl;
import aarogya.master.caseMaster.client.CaseMaster;
import aarogya.master.caseMaster.server.CaseMaster_SrvImpl;
import aarogya.master.consultantMaster.client.ConsultantMaster;
import aarogya.master.consultantMaster.server.ConsultantMaster_SrvImpl;
import aarogya.master.consultantSpeciMaster.client.ConsultantSpeciMaster;
import aarogya.master.consultantSpeciMaster.server.ConsultantSpeciMaster_SrvImpl;
import aarogya.master.departmentMaster.client.DepartmentMaster;
import aarogya.master.departmentMaster.server.DepartmentMaster_SrvImpl;
import aarogya.master.frequencyMaster.client.FrequencyMaster;
import aarogya.master.frequencyMaster.server.FrequencyMaster_SrvImpl;
import aarogya.master.icdxMaster.client.ICDXMaster;
import aarogya.master.icdxMaster.server.ICDXMaster_SrvImpl;
import aarogya.master.cardTypeMaster.client.CardTypeMaster;
import aarogya.master.cardTypeMaster.server.CardTypeMaster_SrvImpl;
import aarogya.master.categoryPoolMaster.client.CategoryPoolMaster;
import aarogya.master.categoryPoolMaster.server.CategoryPoolMaster_SrvImpl;
import aarogya.master.environmentSettingCompany.client.EnvironmentSettingCompany;
import aarogya.master.environmentSettingCompany.server.EnvironmentSettingCompany_SrvImpl;
import aarogya.master.consultantLeave.client.ConsultantLeaveMaster;
import aarogya.master.consultantLeave.server.ConsultantLeaveMaster_SrvImpl;
import aarogya.master.consultantSharingMaster.client.ConsultantSharingMaster;
import aarogya.master.consultantSharingMaster.server.ConsultantSharingMaster_SrvImpl;
import aarogya.master.corporateEmployeeMaster.client.CorporateEmployeeMaster;
import aarogya.master.corporateEmployeeMaster.server.CorporateEmployeeMaster_SrvImpl;
import aarogya.master.customEnviro.client.CustomEnviro;
import aarogya.master.customEnviro.server.CustomEnviro_SrvImpl;
import aarogya.master.designationMaster.client.DesignationMaster;
import aarogya.master.designationMaster.server.DesignationMaster_SrvImpl;
import aarogya.master.disabilityMaster.client.DisabilityMaster;
import aarogya.master.disabilityMaster.server.DisabilityMaster_SrvImpl;
import aarogya.master.discountReasonMaster.client.DiscountReasonMaster;
import aarogya.master.discountReasonMaster.server.DiscountReasonMaster_SrvImpl;
import aarogya.master.earningDeductionEntry.client.EarningDeductionEntry;
import aarogya.master.earningDeductionEntry.server.EarningDeductionEntry_SrvImpl;
import aarogya.master.earningDeductionMaster.client.EarningDeductionMaster;
import aarogya.master.earningDeductionMaster.server.EarningDeductionMaster_SrvImpl;
import aarogya.master.employeeMaster.client.EmployeeMaster;
import aarogya.master.employeeMaster.server.EmployeeMaster_SrvImpl;
import aarogya.master.environmentService.client.EnvironmentService;
import aarogya.master.environmentService.server.EnvironmentService_SrvImpl;
import aarogya.master.environmentSettingSite.client.EnvironmentSettingSite;
import aarogya.master.environmentSettingSite.server.EnvironmentSettingSite_SrvImpl;
import aarogya.master.eod.client.EOD;
import aarogya.master.eod.server.DayCareAcPosting_Server;
import aarogya.master.eod.server.DiagnosticAcPosting_Server;
import aarogya.master.eod.server.EOD_SrvImpl;
import aarogya.master.eod.server.EmergencyAcPosting_Server;
import aarogya.master.eod.server.IPDAcPosting_Server;
import aarogya.master.eod.server.LabConsumption_Server;
import aarogya.master.eod.server.MISAppUpdation_Server;
import aarogya.master.eod.server.OPDAcPosting_Server;
import aarogya.master.eod.server.PharmacyAcPosting_Server;
import aarogya.master.eod.server.RoomPosting_Server;
import aarogya.master.foreignCurrencyExchange.client.ForeignCurrencyExchange;
import aarogya.master.foreignCurrencyExchange.server.ForeignCurrencyExchange_SrvImpl;
import aarogya.master.generateGstCsv.client.GenerateGstCsv;
import aarogya.master.generateGstCsv.server.GenerateGstCsv_SrvImpl;
import aarogya.master.guiMapping.client.GuiMapping;
import aarogya.master.guiMapping.server.GuiMapping_SrvImpl;
import aarogya.master.holidayMaster.client.HolidayMaster;
import aarogya.master.holidayMaster.server.HolidayMaster_SrvImpl;
import aarogya.master.hsnMaster.client.HSNMaster;
import aarogya.master.hsnMaster.server.HSNMaster_SrvImpl;
import aarogya.master.importData.client.ImportData;
import aarogya.master.importData.server.ImportData_SrvImpl;
import aarogya.master.logConfiguration.client.LogConfiguration;
import aarogya.master.logConfiguration.server.LogConfiguration_SrvImpl;
import aarogya.master.managePatientSchedule.client.ManagePatientSchedule;
import aarogya.master.managePatientSchedule.server.ManagePatientSchedule_SrvImpl;
import aarogya.master.prescriptionValueTemplate.client.PrescriptionValueTemplate;
import aarogya.master.prescriptionValueTemplate.server.PrescriptionValueTemplate_SrvImpl;
import aarogya.master.organizationMaster.client.OrganizationMaster;
import aarogya.master.organizationMaster.server.OrganizationMaster_SrvImpl;
import aarogya.master.organizationServiceMapping.client.OrganizationServiceMapping;
import aarogya.master.organizationServiceMapping.server.OrganizationServiceMapping_SrvImpl;
import aarogya.master.organizationServiceMaster.client.OrganizationServiceMaster;
import aarogya.master.organizationServiceMaster.server.OrganizationServiceMaster_SrvImpl;
import aarogya.master.overHeadCategoryMaster.client.OverHeadCategoryMaster;
import aarogya.master.overHeadCategoryMaster.server.OverHeadCategoryMaster_SrvImpl;
import aarogya.master.overHeadMaster.client.OverHeadMaster;
import aarogya.master.overHeadMaster.server.OverHeadMaster_SrvImpl;
import aarogya.master.packageDetail.client.PackageDetail;
import aarogya.master.packageDetail.server.PackageDetail_SrvImpl;
import aarogya.master.packageMaster.client.PackageMaster;
import aarogya.master.packageMaster.server.PackageMaster_SrvImpl;
import aarogya.master.packageRateList.client.PackageRateList;
import aarogya.master.packageRateList.server.PackageRateList_SrvImpl;
import aarogya.master.patientScheduleMaster.client.PatientScheduleMaster;
import aarogya.master.patientScheduleMaster.server.PatientScheduleMaster_SrvImpl;
import aarogya.master.patientTypeMaster.client.PatientTypeMaster;
import aarogya.master.patientTypeMaster.server.PatientTypeMaster_SrvImpl;
import aarogya.master.poolEarningDeductionEntry.client.PoolEarningDeductionEntry;
import aarogya.master.poolEarningDeductionEntry.server.PoolEarningDeductionEntry_SrvImpl;
import aarogya.master.poolGroupDetails.client.PoolGroupDetails;
import aarogya.master.poolGroupDetails.server.PoolGroupDetails_SrvImpl;
import aarogya.master.poolGroupMaster.client.PoolGroupMaster;
import aarogya.master.poolGroupMaster.server.PoolGroupMaster_SrvImpl;
import aarogya.master.prescriptionCustomGroupMast.client.PrescriptionCustomGroupMast;
import aarogya.master.prescriptionCustomGroupMast.server.PrescriptionCustomGroupMast_SrvImpl;
import aarogya.master.prescriptionCustomItemMast.client.PrescriptionCustomItemMast;
import aarogya.master.prescriptionCustomItemMast.server.PrescriptionCustomItemMast_SrvImpl;
import aarogya.master.priceListMaster.client.PriceListMaster;
import aarogya.master.priceListMaster.server.PriceListMaster_SrvImpl;
import aarogya.master.provisionalStatement.client.ProvisionalStatement;
import aarogya.master.provisionalStatement.server.ProvisionalStatement_SrvImpl;
import aarogya.master.provisionalStatementApproval.client.ProvisionalStatementApproval;
import aarogya.master.provisionalStatementApproval.server.ProvisionalStatementApproval_SrvImpl;
import aarogya.master.rateListMaster.client.RateListMaster;
import aarogya.master.rateListMaster.server.RateListMaster_SrvImpl;
import aarogya.master.receiptBookMapping.client.ReceiptBookMapping;
import aarogya.master.receiptBookMapping.server.ReceiptBookMapping_SrvImpl;
import aarogya.master.referredMaster.client.ReferredMaster;
import aarogya.master.referredMaster.server.ReferredMaster_SrvImpl;
import aarogya.master.reports.client.RptConsultantSharing;
import aarogya.master.reports.client.RptOrganizationMaster;
import aarogya.master.reports.client.RptSharingMaster;
import aarogya.master.reports.server.RptConsultantSharing_SrvImpl;
import aarogya.master.reports.server.RptOrganizationMaster_SrvImpl;
import aarogya.master.reports.server.RptSharingMaster_SrvImpl;
import aarogya.master.salaryCodeDefinition.client.SalaryCodeDefinition;
import aarogya.master.salaryCodeDefinition.server.SalaryCodeDefinition_SrvImpl;
import aarogya.master.serviceCategoryMaster.client.ServiceCategoryMaster;
import aarogya.master.serviceCategoryMaster.server.ServiceCategoryMaster_SrvImpl;
import aarogya.master.serviceGroupMaster.client.ServiceGroupMaster;
import aarogya.master.serviceGroupMaster.server.ServiceGroupMaster_SrvImpl;
import aarogya.master.servicePriceList.client.ServicePriceList;
import aarogya.master.servicePriceList.server.ServicePriceList_SrvImpl;
import aarogya.master.servicesApplicableTo.client.ServicesApplicableTo;
import aarogya.master.servicesApplicableTo.server.ServicesApplicableTo_SrvImpl;
import aarogya.master.sittingLocation.client.SittingLocation;
import aarogya.master.sittingLocation.server.SittingLocation_SrvImpl;
import aarogya.master.tabIndexSettings.client.TabIndexSettings;
import aarogya.master.tabIndexSettings.server.TabIndexSettings_SrvImpl;
import aarogya.master.workingDayEntry.client.WorkingDayEntry;
import aarogya.master.workingDayEntry.server.WorkingDayEntry_SrvImpl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

@WebListener
public class Master_Cntx implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String strPath;
        ServletRegistration.Dynamic dyn;

        try {
            strPath = EnvironmentSettingCompany.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnvironmentSettingCompany_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = FrequencyMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, FrequencyMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ConsultantSpeciMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ConsultantSpeciMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DesignationMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DesignationMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DepartmentMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DepartmentMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CaseMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CaseMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ICDXMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ICDXMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardDeactivate.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardDeactivate_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardTypeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardTypeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardReceive.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardReceive_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardRegistration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardRegistration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardRecharge.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardRecharge_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CardDischarge.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CardDischarge_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ConsultantMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ConsultantMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ReferredMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ReferredMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrganizationMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrganizationMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ServicesApplicableTo.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ServicesApplicableTo_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ServiceCategoryMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ServiceCategoryMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ServiceMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ServiceMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PackageDetail.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PackageDetail_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PackageRateList.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PackageRateList_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PackageMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PackageMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ConsultantLeaveMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ConsultantLeaveMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnvironmentSettingSite.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnvironmentSettingSite_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PrescriptionValueTemplate.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrescriptionValueTemplate_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PrescriptionGuiMapping.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrescriptionGuiMapping_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ConsultantSharingMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ConsultantSharingMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptOrganizationMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptOrganizationMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptConsultantSharing.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptConsultantSharing_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AllergyMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AllergyMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ImportData.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ImportData_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = HSNMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, HSNMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ServiceGroupMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ServiceGroupMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EOD.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EOD_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EmployeeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EmployeeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SittingLocation.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SittingLocation_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrganizationServiceMapping.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrganizationServiceMapping_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverHeadCategoryMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverHeadCategoryMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OverHeadMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OverHeadMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = OrganizationServiceMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, OrganizationServiceMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = TabIndexSettings.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, TabIndexSettings_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = LogConfiguration.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, LogConfiguration_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CorporateEmployeeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CorporateEmployeeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = AdditionalUserPermission.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, AdditionalUserPermission_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RptSharingMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RptSharingMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PrescriptionCustomItemMast.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrescriptionCustomItemMast_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PrescriptionCustomGroupMast.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PrescriptionCustomGroupMast_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GenerateGstCsv.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GenerateGstCsv_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EnvironmentService.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EnvironmentService_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ManagePatientSchedule.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ManagePatientSchedule_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientScheduleMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientScheduleMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ForeignCurrencyExchange.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ForeignCurrencyExchange_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = HolidayMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, HolidayMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ServicePriceList.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ServicePriceList_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ReceiptBookMapping.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ReceiptBookMapping_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = GuiMapping.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, GuiMapping_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = WorkingDayEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, WorkingDayEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CategoryPoolMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CategoryPoolMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EarningDeductionMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EarningDeductionMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = SalaryCodeDefinition.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, SalaryCodeDefinition_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PoolGroupMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PoolGroupMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PoolGroupDetails.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PoolGroupDetails_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = EarningDeductionEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, EarningDeductionEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PoolEarningDeductionEntry.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PoolEarningDeductionEntry_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PatientTypeMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PatientTypeMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = RateListMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, RateListMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProvisionalStatement.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProvisionalStatement_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = ProvisionalStatementApproval.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ProvisionalStatementApproval_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DiscountReasonMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DiscountReasonMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = PriceListMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, PriceListMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = DisabilityMaster.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, DisabilityMaster_SrvImpl.class);
            dyn.addMapping("/" + strPath);

            strPath = CustomEnviro.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, CustomEnviro_SrvImpl.class);
            dyn.addMapping("/" + strPath);


            strPath = ImportExportData.class.getName().trim().replace(".", "/");
            dyn = sce.getServletContext().addServlet(strPath, ImportExportData_SrvImpl.class);
            dyn.addMapping("/" + strPath);
            System.out.println("PrescriptionFileServlet : " + strPath);


            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.ROOM_POSTING.getValue(), new RoomPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.LAB_CONSUMPTION.getValue(), new LabConsumption_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.OPD_AC_POSTING.getValue(), new OPDAcPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.DAY_CARE_AC_POSTING.getValue(), new DayCareAcPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.DIAGNOSTIC_AC_POSTING.getValue(), new DiagnosticAcPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.EMERGENCY_AC_POSTING.getValue(), new EmergencyAcPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.IPD_AC_POSTING.getValue(), new IPDAcPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.PHARMACY_AC_POSTING.getValue(), new PharmacyAcPosting_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.MIS_APP_UPDATION.getValue(), new MISAppUpdation_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.D365_To_HIS_ItemSync.getValue(), new D365ToHISItemSync_Server());
            DmServer.getWebServiceSchedulars().addEventHandler(STEnum.ProcessEOD.Comman_Scheduler.getValue(), new CommanScheduler_Server());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
