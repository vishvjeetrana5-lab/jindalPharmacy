package dataman.webJobs.self.client;

import dataman.libraryMenu.self.shared.lmWebJobs;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import dataman.webJobs.emailManager.client.EmailManager;
import dataman.webJobs.emailTemplateMast.client.EmailTemplateMast;
import dataman.webJobs.environmentSetting.client.EnvironmentSetting;
import dataman.webJobs.notificationEntry.client.NotificationEntry;
import dataman.webJobs.scheduler.client.Scheduler;
import dataman.webJobs.self.shared.WebJobs_Model;
import dataman.webJobs.smsManager.client.SmsManager;
import dataman.webJobs.smsTemplate.client.SmsTemplate;
import dataman.webJobs.userEmailConfiguration.client.UserEmailConfiguration;
import java.util.HashMap;
import java.util.List;

public class WebJobs implements IsWidget {

    lmWebJobs pmMenu = new lmWebJobs();
    DmMenuItem.InitializeWidgetInvoker iwiTemp;
    DmMenuBar menu;

    public WebJobs(final SetWidgetInvoker swiMain) {
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

    public void invokeSetWidget(final String menuText, HashMap paramHashMap, UserMenu userMenu, final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.SMS_MANAGER.caption)) {
                new SmsManager(menuPermission, swiMain);
                ppCallBackWait.hide(true);
//                    GWT.runAsync(new RunAsyncCallback() {
//
//                        @Override
//                        public void onFailure(Throwable reason) {
//                            ppCallBackWait.hide(true);
//                            new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
//                        }
//
//                        @Override
//                        public void onSuccess() {
//                            new SmsManager(menuPermission, swiMain);
//                            ppCallBackWait.hide(true);
//                        }
//                    });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.USER_EMAIL_CONFIGURATION.caption)) {
                new UserEmailConfiguration(menuPermission, swiMain);
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
//                        new UserEmailConfiguration(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EMAIL_MANAGER.caption)) {
                new EmailManager(menuPermission, swiMain);
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
//                        new EmailManager(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SCHEDULER.caption)) {
                new Scheduler(menuPermission, swiMain);
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
//                        new Scheduler(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.EMAIL_TEMPLATE_MASTER.caption)) {
                new EmailTemplateMast(menuPermission, swiMain);
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
//                        new EmailTemplateMast(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENVIRONMENT_SETTING.caption)) {
                new EnvironmentSetting(menuPermission, swiMain);
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
//                        new EnvironmentSetting(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SMS_TEMPLATE_MASTER.caption)) {

                new SmsTemplate(menuPermission, swiMain);
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
//                        new SmsTemplate(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TEMPLATE_GROUP_MASTER.caption)) {
                new SingleLineMaster(new lmWebJobs().TEMPLATE_GROUP_MASTER.caption, "wjTemplateGroupMast", "Group Name", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster(new lmWebJobs().TEMPLATE_GROUP_MASTER.caption, "wjTemplateGroupMast", "Group Name", "Code", "Name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NOTIFICATION_ENTRY.caption)) {

                new NotificationEntry(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else {
                //==================== Reports =================================

                ppCallBackWait.hide(true);

            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }

    }
}
