package aarogya.nabh.self.client;

import aarogya.nabh.nabhNavigation.client.NABHNavigation;
import aarogya.nabh.patientInfectionEntry.client.PatientInfectionEntry;
import aarogya.nabh.reports.client.*;
import aarogya.nabh.roomHelp.client.RoomHelp;
import aarogya.projectMenu.self.shared.pmNABH;
import aarogya.selfToolkit.stStatic.shared.STEnum.MappingNature;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
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

public class NABH implements IsWidget {

    private pmNABH pmMenu = new pmNABH();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public NABH(final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_1.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.Indicator_2.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.Indicator_3.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.Indicator_4.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.Indicator_11.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.Indicator_12.caption)
                    || menuText.trim().equalsIgnoreCase(pmMenu.Indicator_52.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        HashMap hmTemp = null;
                        MappingNature mappingNature = null;

                        if (paramHashMap != null) {
                            hmTemp = paramHashMap;
                        } else {
                            hmTemp = new HashMap<String, Object>();
                        }

                        if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_1.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_1;

                        } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_2.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_2;

                        } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_3.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_3;

                        } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_4.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_4;

                        } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_11.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_11;

                        } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_12.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_12;

                        } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_52.caption)) {
                            mappingNature = MappingNature.NABH_INDICATOR_52;
                        }

                        if (!hmTemp.containsKey("indicator")) {
                            hmTemp.put("indicator", mappingNature);
                        }

                        new RoomHelp(menuPermission, hmTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NABH_NAVIGATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new NABHNavigation(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PATIENT_INFECTION_ENTRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PatientInfectionEntry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NABH_INDICATORS_SUMMARY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new NABHIndicatorsSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDICATOR_10_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator10(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDICATOR_13_REPORT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator13(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_27.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator27(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_28.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator28(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_29.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator29(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_30.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator30(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_6.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator6(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_61.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator61(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.Indicator_62.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndicator62(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else {
                ppCallBackWait.hide(true);
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            Window.alert(e.getMessage());
        }
    }
}
