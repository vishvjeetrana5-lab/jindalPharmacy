package aarogya.master.self.client;

import aarogya.master.D365ImportExport.client.ImportExportData;
import aarogya.master.additionalUserPermission.client.AdditionalUserPermission;
import aarogya.master.allergyMaster.client.AllergyMaster;
import aarogya.master.cardDeactive.client.CardDeactivate;
import aarogya.master.prescriptionGuiMapping.client.PrescriptionGuiMapping;
import aarogya.master.serviceMaster.client.ServiceMaster;
import aarogya.master.cardRegistration.client.CardRegistration;
import aarogya.master.cardDischarge.client.CardDischarge;
import aarogya.master.cardReceive.client.CardReceive;
import aarogya.master.cardRecharge.client.CardRecharge;
import aarogya.master.caseMaster.client.CaseMaster;
import aarogya.master.consultantMaster.client.ConsultantMaster;
import aarogya.master.consultantSpeciMaster.client.ConsultantSpeciMaster;
import aarogya.master.departmentMaster.client.DepartmentMaster;
import aarogya.master.frequencyMaster.client.FrequencyMaster;
import aarogya.master.icdxMaster.client.ICDXMaster;
import aarogya.master.cardTypeMaster.client.CardTypeMaster;
import aarogya.master.categoryPoolMaster.client.CategoryPoolMaster;
import aarogya.master.environmentSettingCompany.client.EnvironmentSettingCompany;
import aarogya.master.consultantLeave.client.ConsultantLeaveMaster;
import aarogya.master.consultantSharingMaster.client.ConsultantSharingMaster;
import aarogya.master.corporateEmployeeMaster.client.CorporateEmployeeMaster;
import aarogya.master.customEnviro.client.CustomEnviro;
import aarogya.master.designationMaster.client.DesignationMaster;
import aarogya.master.disabilityMaster.client.DisabilityMaster;
import aarogya.master.discountReasonMaster.client.DiscountReasonMaster;
import aarogya.master.earningDeductionEntry.client.EarningDeductionEntry;
import aarogya.master.earningDeductionMaster.client.EarningDeductionMaster;
import aarogya.master.employeeMaster.client.EmployeeMaster;
import aarogya.master.environmentService.client.EnvironmentService;
import aarogya.master.environmentSettingSite.client.EnvironmentSettingSite;
import aarogya.master.eod.client.EOD;
import aarogya.master.foreignCurrencyExchange.client.ForeignCurrencyExchange;
import aarogya.master.generateGstCsv.client.GenerateGstCsv;
import aarogya.master.guiMapping.client.GuiMapping;
import aarogya.master.holidayMaster.client.HolidayMaster;
import aarogya.master.hsnMaster.client.HSNMaster;
import aarogya.master.importData.client.ImportData;
import aarogya.master.logConfiguration.client.LogConfiguration;
import aarogya.master.managePatientSchedule.client.ManagePatientSchedule;
import aarogya.master.prescriptionValueTemplate.client.PrescriptionValueTemplate;
import aarogya.master.organizationMaster.client.OrganizationMaster;
import aarogya.master.organizationServiceMapping.client.OrganizationServiceMapping;
import aarogya.master.organizationServiceMaster.client.OrganizationServiceMaster;
import aarogya.master.overHeadCategoryMaster.client.OverHeadCategoryMaster;
import aarogya.master.overHeadMaster.client.OverHeadMaster;
import aarogya.master.packageMaster.client.PackageMaster;
import aarogya.master.packageRateList.client.PackageRateList;
import aarogya.master.patientScheduleMaster.client.PatientScheduleMaster;
import aarogya.master.patientTypeMaster.client.PatientTypeMaster;
import aarogya.master.poolEarningDeductionEntry.client.PoolEarningDeductionEntry;
import aarogya.master.poolGroupDetails.client.PoolGroupDetails;
import aarogya.master.poolGroupMaster.client.PoolGroupMaster;
import aarogya.master.prescriptionCustomGroupMast.client.PrescriptionCustomGroupMast;
import aarogya.master.prescriptionCustomItemMast.client.PrescriptionCustomItemMast;
import aarogya.master.priceListMaster.client.PriceListMaster;
import aarogya.master.provisionalStatement.client.ProvisionalStatement;
import aarogya.master.provisionalStatementApproval.client.ProvisionalStatementApproval;
import aarogya.master.rateListMaster.client.RateListMaster;
import aarogya.master.receiptBookMapping.client.ReceiptBookMapping;
import aarogya.master.referredMaster.client.ReferredMaster;
import aarogya.master.reports.client.RptConsultantSharing;
import aarogya.master.salaryCodeDefinition.client.SalaryCodeDefinition;
import aarogya.master.serviceCategoryMaster.client.ServiceCategoryMaster;
import aarogya.master.serviceGroupMaster.client.ServiceGroupMaster;
import aarogya.master.servicePriceList.client.ServicePriceList;
import aarogya.master.servicesApplicableTo.client.ServicesApplicableTo;
import aarogya.master.sittingLocation.client.SittingLocation;
import aarogya.master.tabIndexSettings.client.TabIndexSettings;
import aarogya.master.workingDayEntry.client.WorkingDayEntry;
import aarogya.projectMenu.self.shared.pmIPD;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.projectMenu.self.shared.pmOPD;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import java.util.HashMap;
import java.util.List;

public class Master implements IsWidget {

    private pmMaster pmMenu = new pmMaster();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public Master(final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.ADDITIONAL_USER_PERMISSION.caption)) {

                new AdditionalUserPermission(menuPermission, swiMain);
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
//                        new AdditionalUserPermission(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PRESCRIPTION_CUSTOM_GROUP_MASTER.caption)) {

                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                    paramHashMapTemp.put("flagOpenAsPopup", false);
                }
                new PrescriptionCustomGroupMast(menuPermission, paramHashMapTemp, swiMain);
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
//                        HashMap paramHashMapTemp;
//
//                        if (paramHashMap != null) {
//                            paramHashMapTemp = paramHashMap;
//                        } else {
//                            paramHashMapTemp = new HashMap();
//                        }
//                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
//                            paramHashMapTemp.put("flagOpenAsPopup", false);
//                        }
//                        new PrescriptionCustomGroupMast(menuPermission, paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PRESCRIPTION_CUSTOM_ITEM_MASTER.caption)) {

                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                    paramHashMapTemp.put("flagOpenAsPopup", false);
                }
                new PrescriptionCustomItemMast(menuPermission, paramHashMapTemp, swiMain);
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
//                        HashMap paramHashMapTemp;
//
//                        if (paramHashMap != null) {
//                            paramHashMapTemp = paramHashMap;
//                        } else {
//                            paramHashMapTemp = new HashMap();
//                        }
//                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
//                            paramHashMapTemp.put("flagOpenAsPopup", false);
//                        }
//                        new PrescriptionCustomItemMast(menuPermission, paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CORPORATE_EMPLOYEE_MASTER.caption)) {
                new CorporateEmployeeMaster(menuPermission, swiMain);
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
//                        new CorporateEmployeeMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CORPORATE_MASTER.caption)) {
                new SingleLineMaster(pmMenu.CORPORATE_MASTER.caption, "corporateMast", "Corporate Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster(pmMenu.CORPORATE_MASTER.caption, "corporateMast", "Corporate Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INSURANCE_COMPANY_MASTER.caption)) {
                new SingleLineMaster(pmMenu.INSURANCE_COMPANY_MASTER.caption, "insuranceCompanyMast", "Insurance Company Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster(pmMenu.INSURANCE_COMPANY_MASTER.caption, "insuranceCompanyMast", "Insurance Company Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PACKAGE_PRICE_LIST.caption)) {
                new PackageRateList(menuPermission, swiMain);
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
//                        new PackageRateList(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_SHARING_REPORT.caption)) {
                new RptConsultantSharing(swiMain);
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
//                        new RptConsultantSharing(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TAB_INDEX_SETTING.caption)) {

                new TabIndexSettings(menuPermission, swiMain);
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
//                        new TabIndexSettings(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LOG_CONFIGURATION.caption)) {
                new LogConfiguration(menuPermission, swiMain);
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
//                        new LogConfiguration(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IMPORT_ORGANIZATION_RATELIST.caption)) {
                new OrganizationServiceMapping(menuPermission, swiMain);
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
//                        new OrganizationServiceMapping(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ORGANIZATION_SERVICE_MASTER.caption)) {
                new OrganizationServiceMaster(menuPermission, swiMain);
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
//                        new OrganizationServiceMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_SITTING_LOCATION.caption)) {
                new SittingLocation(menuPermission, swiMain);
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
//                        new SittingLocation(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DESIGNATION_MASTER.caption)) {
                new DesignationMaster(menuPermission, swiMain);
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
//                        new DesignationMaster(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EOD.caption)) {
                new EOD(menuPermission, swiMain);
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
//                        new EOD(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SHARING_MASTER.caption)) {
                new ConsultantSharingMaster(menuPermission, swiMain);
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
//                        new ConsultantSharingMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CATEGORY_POOL_MASTER.caption)) {
                new CategoryPoolMaster(menuPermission, swiMain);
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
//                        new CategoryPoolMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.POOL_GROUP_DETAILS_ENTRY.caption)) {
                new PoolGroupDetails(menuPermission, swiMain);
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
//                        new PoolGroupDetails(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IMPORT_DATA.caption)) {
                new ImportData(menuPermission, swiMain);
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
//                        new ImportData(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UHID_MODIFICATION.caption)) {
                swiMain.setWidget(new pmOPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new pmOPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ROOM_MASTER.caption)) {
                swiMain.setWidget(new pmIPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new pmIPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ROOM_CATEGORY_MASTER.caption)) {
                swiMain.setWidget(new pmIPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new pmIPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NURSING_STATION_MASTER.caption)) {
                swiMain.setWidget(new pmIPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new pmIPD().moduleName, menuText, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ALLERGY_MASTER.caption)) {
                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                    paramHashMapTemp.put("flagOpenAsPopup", false);
                }
                new AllergyMaster(menuPermission, paramHashMapTemp, swiMain);
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
//                        HashMap paramHashMapTemp;
//
//                        if (paramHashMap != null) {
//                            paramHashMapTemp = paramHashMap;
//                        } else {
//                            paramHashMapTemp = new HashMap();
//                        }
//                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
//                            paramHashMapTemp.put("flagOpenAsPopup", false);
//                        }
//                        new AllergyMaster(menuPermission, paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.POOL_GROUP_MASTER.caption)) {
                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                    paramHashMapTemp.put("flagOpenAsPopup", false);
                }
                new PoolGroupMaster(menuPermission, paramHashMapTemp, swiMain);
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
//                        HashMap paramHashMapTemp;
//
//                        if (paramHashMap != null) {
//                            paramHashMapTemp = paramHashMap;
//                        } else {
//                            paramHashMapTemp = new HashMap();
//                        }
//                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
//                            paramHashMapTemp.put("flagOpenAsPopup", false);
//                        }
//                        new PoolGroupMaster(menuPermission, paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PRESCRIPTION_GUI_MAPPING.caption)) {
                new PrescriptionGuiMapping(menuPermission, swiMain);
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
//                        new PrescriptionGuiMapping(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENVIRONMENT_SETTING.caption)) {
                new EnvironmentSettingSite(menuPermission, swiMain);
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
//                        new EnvironmentSettingSite(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENVIRONMENT_SETTING_COMPANY.caption)) {
                new EnvironmentSettingCompany(menuPermission, swiMain);
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
//                        new EnvironmentSettingCompany(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FREQUENCY_MASTER.caption)) {
                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                    paramHashMapTemp.put("flagOpenAsPopup", false);
                }
                new FrequencyMaster(menuPermission, paramHashMapTemp, swiMain);
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
//                        HashMap paramHashMapTemp;
//
//                        if (paramHashMap != null) {
//                            paramHashMapTemp = paramHashMap;
//                        } else {
//                            paramHashMapTemp = new HashMap();
//                        }
//                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
//                            paramHashMapTemp.put("flagOpenAsPopup", false);
//                        }
//                        new FrequencyMaster(menuPermission, paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SERVICE_GROUP_MASTER.caption)) {
                new ServiceGroupMaster(menuPermission, swiMain);
                //new SingleLineMaster(pmMenu.SERVICE_GROUP_MASTER.caption, "serviceGroup", "Group Name", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
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
//                        new ServiceGroupMaster(menuPermission, swiMain);
//                        //new SingleLineMaster(pmMenu.SERVICE_GROUP_MASTER.caption, "serviceGroup", "Group Name", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SPECIALIZATION_MASTER.caption)) {
                new ConsultantSpeciMaster(menuPermission, swiMain);
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
//                        new ConsultantSpeciMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.HSN_MASTER.caption)) {
                new HSNMaster(menuPermission, swiMain);
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
//                        new HSNMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DEPARTMENT_MASTER.caption)) {
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CASE_MASTER.caption)) {
                new CaseMaster(menuPermission, swiMain);
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
//                        new CaseMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ICD_X_MASTER.caption)) {
                new ICDXMaster(menuPermission, swiMain);
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
//                        new ICDXMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CARD_TYPE_MASTER.caption)) {
                new CardTypeMaster(menuPermission, swiMain);
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
//                        new CardTypeMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CARD_RECEIVE.caption)) {
                new CardReceive(menuPermission, swiMain);
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
//                        new CardReceive(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CARD_REGISTRATION.caption)) {
                new CardRegistration(menuPermission, swiMain);
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
//                        new CardRegistration(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CARD_RECHARGE.caption)) {
                new CardRecharge(menuPermission, swiMain);
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
//                        new CardRecharge(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CARD_REFUND.caption)) {
                new CardDischarge(menuPermission, swiMain);
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
//                        new CardDischarge(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CARD_DEACTIVE.caption)) {
                new CardDeactivate(menuPermission, swiMain);
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
//                        new CardDeactivate(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OVERHEAD_SERVICE_CATEGORY_MASTER.caption)) {
                new OverHeadCategoryMaster(menuPermission, swiMain);
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
//                        new OverHeadCategoryMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OVERHEAD_SERVICE_MASTER.caption)) {
                new OverHeadMaster(menuPermission, swiMain);
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
//                        new OverHeadMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_MASTER.caption)) {
                new ConsultantMaster(menuPermission, swiMain);
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
//                        new ConsultantMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.REFERRED_MASTER.caption)) {
                new ReferredMaster(menuPermission, swiMain);
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
//                        new ReferredMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_LEAVE.caption)) {
                new ConsultantLeaveMaster(menuPermission, paramHashMap, swiMain);
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
//                        new ConsultantLeaveMaster(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WORKING_DAYS.caption)) {
                new WorkingDayEntry(menuPermission, paramHashMap, swiMain);
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
//                        new WorkingDayEntry(menuPermission, paramHashMap, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ORGANIZATION_MASTER.caption)) {
                new OrganizationMaster(menuPermission, null, swiMain);
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
//                        new OrganizationMaster(menuPermission, null, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SERVICE_PRICELIST_MASTER.caption)) {
                new ServicePriceList(menuPermission, swiMain);
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
//                        new ServicePriceList(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COLLECTION_CENTER_MASTER.caption)) {
                HashMap paramHashMapTemp;

                if (paramHashMap != null) {
                    paramHashMapTemp = paramHashMap;
                } else {
                    paramHashMapTemp = new HashMap();
                }
                if (!paramHashMapTemp.containsKey("widgetName")) {
                    paramHashMapTemp.put("widgetName", pmMenu.COLLECTION_CENTER_MASTER.caption);
                }
                if (!paramHashMapTemp.containsKey("isOrganization")) {
                    paramHashMapTemp.put("isOrganization", false);
                }
                new OrganizationMaster(menuPermission, paramHashMapTemp, swiMain);
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
//                        HashMap paramHashMapTemp;
//
//                        if (paramHashMap != null) {
//                            paramHashMapTemp = paramHashMap;
//                        } else {
//                            paramHashMapTemp = new HashMap();
//                        }
//                        if (!paramHashMapTemp.containsKey("widgetName")) {
//                            paramHashMapTemp.put("widgetName", pmMenu.COLLECTION_CENTER_MASTER.caption);
//                        }
//                        if (!paramHashMapTemp.containsKey("isOrganization")) {
//                            paramHashMapTemp.put("isOrganization", false);
//                        }
//                        new OrganizationMaster(menuPermission, paramHashMapTemp, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SERVICE_CATEGORY_APPLICABLE_TO.caption)) {
                new ServicesApplicableTo(menuPermission, swiMain);
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
//                        new ServicesApplicableTo(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SERVICE_CATEGORY_MASTER.caption)) {
                new ServiceCategoryMaster(menuPermission, swiMain);
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
//                        new ServiceCategoryMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SERVICE_MASTER.caption)) {
                new ServiceMaster(menuPermission, swiMain);
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
//                        new ServiceMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PACKAGE_MASTER.caption)) {
                new PackageMaster(menuPermission, swiMain);
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
//                        new PackageMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PRESCRIPTION_VALUE_TEMPLATE.caption)) {
                new PrescriptionValueTemplate(menuPermission, swiMain);
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
//                        new PrescriptionValueTemplate(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GENERATE_GST_CSV.caption)) {
                new GenerateGstCsv(menuPermission, swiMain);
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
//                        new GenerateGstCsv(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENVIRONMENT_SERVICE.caption)) {
                new EnvironmentService(menuPermission, swiMain);
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
//                        new EnvironmentService(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_SCHEDULE_GROUP_MASTER.caption)) {
                new SingleLineMaster(pmMenu.PATIENT_SCHEDULE_GROUP_MASTER.caption, "patientScheduleGroupMast", "Schedule Group Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster(pmMenu.PATIENT_SCHEDULE_GROUP_MASTER.caption, "patientScheduleGroupMast", "Schedule Group Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OCCUPATION_MASTER.caption)) {
                new SingleLineMaster(pmMenu.OCCUPATION_MASTER.caption, "occupationMast", "Occupation Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster(pmMenu.OCCUPATION_MASTER.caption, "occupationMast", "Occupation Master", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_SCHEDULE_MASTER.caption)) {
                new PatientScheduleMaster(menuPermission, swiMain);
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
//                        new PatientScheduleMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MANAGE_PATIENT_SCHEDULE.caption)) {
                new ManagePatientSchedule(menuPermission, swiMain);
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
//                        new ManagePatientSchedule(menuPermission, swiMain);
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FOREIGN_CURRENCY_EXCHANGE.caption)) {
                new ForeignCurrencyExchange(menuPermission, swiMain);
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
//                        new ForeignCurrencyExchange(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.RECEIPT_BOOK_MAPPING.caption)) {
                new ReceiptBookMapping(menuPermission, swiMain);
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
//                        new ReceiptBookMapping(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GUI_MAPPING_MASTER.caption)) {
                new GuiMapping(menuPermission, swiMain);
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
//                        new GuiMapping(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EARNDEDUCTION_MASTER.caption)) {
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
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.POOL_EARNING_DEDUCTION_ENTRY.caption)) {
                new PoolEarningDeductionEntry(menuPermission, swiMain);
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
//                        new PoolEarningDeductionEntry(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EARNING_DEDUCTION_ENTRY.caption)) {
                new EarningDeductionEntry(menuPermission, swiMain);
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
//                        new EarningDeductionEntry(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.RATE_LIST_MASTER.caption)) {

                new RateListMaster(menuPermission, swiMain);
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
//                        new RateListMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_TYPE_MASTER.caption)) {
                new PatientTypeMaster(menuPermission, swiMain);
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
//                        new PatientTypeMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROVISIONAL_STATEMENT.caption)) {
                new ProvisionalStatement(menuPermission, swiMain);
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
//                        new ProvisionalStatement(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FINAL_SHARING.caption)) {
                new ProvisionalStatementApproval(menuPermission, swiMain);
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
//                        new ProvisionalStatementApproval(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DISCOUNT_REASON_MASTER.caption)) {
                new DiscountReasonMaster(menuPermission, swiMain);
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
//                        new DiscountReasonMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_PRICELIST_MASTER.caption)) {
                new PriceListMaster(menuPermission, swiMain);
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
//                        new PriceListMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//
//                    }
//                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DISABILITY_MASTER.caption)) {
                new DisabilityMaster(menuPermission, swiMain);
                ppCallBackWait.hide(true);

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CUSTOM_ENVIRO.caption)) {
                new CustomEnviro(menuPermission, swiMain);
                ppCallBackWait.hide(true);

            } 
            else if (menuText.trim().equalsIgnoreCase(pmMenu.IMPORT_EXPORT_DATA.caption)) {
                new ImportExportData(menuPermission, swiMain);
                ppCallBackWait.hide(true);

            } 
            
            //==================== Reports =================================
            else {
                ppCallBackWait.hide(true);
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }
}
