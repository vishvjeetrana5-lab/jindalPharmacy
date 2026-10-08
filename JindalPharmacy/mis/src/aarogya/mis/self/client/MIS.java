package aarogya.mis.self.client;

import aarogya.mis.graphicalDashboard.client.GraphicalDashBoard;
import aarogya.mis.reports.client.*;
import aarogya.mis.standardReports.client.*;
import aarogya.projectMenu.self.shared.pmMIS;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import java.util.HashMap;
import java.util.List;

public class MIS implements IsWidget {

    private pmMIS pmMenu = new pmMIS();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public MIS(final SetWidgetInvoker swiMain) {
        super();
        iwiTemp = new DmMenuItem.InitializeWidgetInvoker() {
            @Override
            public void initializeWidget(String menuText) {
                invokeSetWidget(menuText, swiMain);
            }
        };

        menu = new DmMenuBar();
//        menu.setHeight("25px");
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

            //==================== Reports =================================
            if (menuText.trim().equalsIgnoreCase(pmMenu.GRAPHICAL_DASHBOARD.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {

                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GraphicalDashBoard(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COLLECTION_REGISTER.caption)) {
                // widgetNm="COLLECTION_REGISTER";
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {

                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {

                        HashMap paramHashMapTemp;

                        if (paramHashMap != null) {
                            paramHashMapTemp = paramHashMap;
                        } else {
                            paramHashMapTemp = new HashMap();
                        }

                        if (!paramHashMapTemp.containsKey("widgetName")) {
                            paramHashMapTemp.put("widgetName", new pmMIS().COLLECTION_REGISTER.caption);
                        }

                        new RptCollectionRegister(menuPermission, swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COLLECTION_REGISTER_USERWISE.caption)) {
                // widgetNm="COLLECTION REGISTER USERWISE";
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {

                        HashMap paramHashMapTemp;

                        if (paramHashMap != null) {
                            paramHashMapTemp = paramHashMap;
                        } else {
                            paramHashMapTemp = new HashMap();
                        }

                        if (!paramHashMapTemp.containsKey("widgetName")) {
                            paramHashMapTemp.put("widgetName", new pmMIS().COLLECTION_REGISTER_USERWISE.caption);
                        }

                        //  new RptCollectionUserWiseRegister(menuPermission, swiMain, paramHashMap);
                        new RptCollectionRegister(menuPermission, swiMain, paramHashMapTemp);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_SHARING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptConsultantSharing(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UNBILLED_SERVICE_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIPD_UnbilledServiceReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPatientLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_SUMMARY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPatientSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EMERGENCY_STATISTICS_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptEmergencyStatistics(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BIRTHDAY_ANNIVERSARY_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptBirthdayAnniv(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NET_REVENUE_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptNetRevenueRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DATE_WISE_COLLECTION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptDateWiseCollection(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptConsultationReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OPD_SERVICES_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptOPDServicesReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DEPARTMENT_WISE_CONSULTATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptDepartmentWiseConsultation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.AREA_WISE_CONSULTATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptAreaWiseConsultation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CASE_WISE_CONSULTATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptCaseWiseConsultation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CASTE_WISE_CONSULTATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptCasteWiseConsultation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CATEGORY_WISE_SERVICE_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptCategoryWiseServiceReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OPD_SERVICE_CANCELLATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptOPDServiceCancellation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DISCHARGE_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptDischargeReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ROOM_CHANGE_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptRoomChange(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DISCHARGE_PATIENT_DUES_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptDischargePatientDues(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IPD_REFUND_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIPDRefundReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IPD_SERVICES_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIPDServicesReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LAB_CANCELLATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptLabCancellationReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LAB_DISCOUNT_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptLabDiscountReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LAB_DUES_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptLabDuesReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.AREA_WISE_LAB_REGISTRATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptAreaWiseLabRegistrationReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.LAB_REFUND_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptLabRefundReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OPD_DISCOUNT_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptOpdDiscountReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OPD_REFUND_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptOPDRefundReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COLLECTION_BOY_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptCollectionBoyReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONSULTANT_WISE_CONSULTATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptConsultantWiseConsultationReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PACKAGE_WISE_INVESTIGATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPackageWiseInvestigationReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TASK_EXECUTION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptTaskExecutionReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UNBILLED_OPD_ORGANIZATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptUnBilledOPDOrganizationReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ROOM_CATEGORY_WISE_IPD_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptRoomCategoryWiseIPDReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IPD_DISCOUNT_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIPDDiscountReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MODIFICATION_HISTORY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptModificationHistory(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MODIFICATION_Log.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptLogSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_WALLET_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPatientWalletLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UHID_REGISTRATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptUHIDRegistration(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IPD_SERVICE_CANCELLATION_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIPDServiceCancellation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CONTROL_ACCOUNTS_DETAILS_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptControlAccountsDetails(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CANCEL_SERVICE_REFUND_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptCancelServiceRefundReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } 
                else if (menuText.trim().equalsIgnoreCase(pmMenu.POS_PATMENT_COLLECTION.caption)) {
                new RptPOSPaymentCollection(swiMain);
                ppCallBackWait.hide(true);

            }
            
            else {
                ppCallBackWait.hide(true);

            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }
}
