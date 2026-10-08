package dataman.utility.self.client;

import dataman.libraryMenu.self.shared.lmUtility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.*;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum.SQLDataType;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.utility.companyMaster.client.CompanyMaster;
import dataman.utility.siteMaster.client.SiteMaster;
import dataman.utility.branchMaster.client.BranchMaster;
import dataman.utility.changePassword.client.ChangePassword;
import dataman.utility.copyUserPermission.client.CopyUserPermission;
import dataman.utility.dataBackup.client.DataBackup;
import dataman.utility.docIdMaster.client.DocIdMaster;
import dataman.utility.exportProcedure.client.ExportProcedure;
import dataman.utility.fileTypeMaster.client.FileTypeMaster;
import dataman.utility.guiConfig.client.GUIConfig;
import dataman.utility.importProcedure.client.ImportProcedure;
import dataman.utility.gstStructure.client.GstStructure;
import dataman.utility.itemTaxStructure.client.ItemTaxStructure;
import dataman.utility.menuCodification.client.MenuCodification;
import dataman.utility.numberSystem.client.NumberSystem;
import dataman.utility.overheadConfiguration.client.OverheadConfiguration;
import dataman.utility.overheadLineConfiguration.client.OverheadLineConfiguration;
import dataman.utility.overheadMaster.client.OverheadMaster;
import dataman.utility.productLicensedTo.client.ProductLicensedTo;
import dataman.utility.reports.client.RptDepartmentWiseUserReport;
import dataman.utility.reports.client.RptUserPermission;
import dataman.utility.userMaster.client.UserMaster;
import dataman.utility.userPermission.client.UserPermission;
import dataman.utility.voucherType.client.VoucherType;
import dataman.utility.walkThroughMaster.client.WalkThroughMaster;
import java.util.HashMap;
import java.util.List;

public class Utility implements IsWidget {

    lmUtility pmMenu = new lmUtility();
    //   public static final String moduleName = "Utility";
    DmMenuItem.InitializeWidgetInvoker iwiTemp;
    DmMenuBar menu;

    public Utility(final SetWidgetInvoker swiMain) {
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

    public void invokeSetWidget(final String menuText, final HashMap paramHashMap, final UserMenu userMenu, final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.CURRENCY_MASTER.caption)) {
                swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().CURRENCY_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().CURRENCY_MASTER.caption, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COUNTRY_MASTER.caption)) {
                swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().COUNTRY_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().COUNTRY_MASTER.caption, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STATE_MASTER.caption)) {
                swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().STATE_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().STATE_MASTER.caption, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CITY_MASTER.caption)) {
                swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().CITY_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().CITY_MASTER.caption, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ZONE_MASTER.caption)) {
                swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().ZONE_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().ZONE_MASTER.caption, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NATIONALITY_MASTER.caption)) {
                swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().NATIONALITY_MASTER.caption, paramHashMap, menuPermission, swiMain);
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
//                        swiMain.setWidget(new lmAccounts().moduleName, new lmAccounts().NATIONALITY_MASTER.caption, paramHashMap, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WALK_THROUGH_MASTER.caption)) {
                new WalkThroughMaster(menuPermission, swiMain);
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
//                        new WalkThroughMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COMPANY_MASTER.caption)) {
                new CompanyMaster(menuPermission, swiMain);
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
//                        new CompanyMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BRANCH_MASTER.caption)) {
                new BranchMaster(menuPermission, swiMain);
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
//                        new BranchMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DATA_BACKUP.caption)) {
                new DataBackup(menuPermission, swiMain);
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
//                        new DataBackup(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DOCID_MASTER.caption)) {
                new DocIdMaster(menuPermission, swiMain);
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
//                        new DocIdMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NUMBER_SYSTEM.caption)) {
                new NumberSystem(menuPermission, swiMain);
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
//                        new NumberSystem(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PRODUCT_LICENSED_TO.caption)) {
                new ProductLicensedTo(menuPermission, swiMain);
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
//                        new ProductLicensedTo(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SITE_MASTER.caption)) {
                new SiteMaster(menuPermission, swiMain);
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
//                        new SiteMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.USER_MASTER.caption)) {
                new UserMaster(menuPermission, swiMain);
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
//                        new UserMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.USER_PERMISSION.caption)) {
                new UserPermission(menuPermission, swiMain);
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
//                        new UserPermission(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.COPY_USER_PERMISSION.caption)) {
                new CopyUserPermission(swiMain);
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
//                        new CopyUserPermission(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MANAGE_VOUCHER_TYPE.caption)) {
                new VoucherType(menuPermission, swiMain);
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
//                        new VoucherType(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.CHANGE_PASSWORD.caption)) {
                new ChangePassword(menuPermission, swiMain);
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
//                        new ChangePassword(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OVERHEAD_MASTER.caption)) {
                new OverheadMaster(menuPermission, swiMain);
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
//                        new OverheadMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OVERHEAD_CONFIGURATION.caption)) {
                new OverheadConfiguration(menuPermission, swiMain);
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
//                        new OverheadConfiguration(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OVERHEAD_LINE_CONFIGURATION.caption)) {
                new OverheadLineConfiguration(menuPermission, swiMain);
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
//                        new OverheadLineConfiguration(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GUI_TEMPLATE_MASTER.caption)) {
                new SingleLineMaster("GUI Template Master", "guiTemplateMast", "Template Name", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster("GUI Template Master", "guiTemplateMast", "Template Name", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MODIFY_REASON_MASTER.caption)) {
                new SingleLineMaster("Modify Reason Master", "modifyReasonMast", "Reason", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, swiMain);
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
//                        new SingleLineMaster("Modify Reason Master", "modifyReasonMast", "Reason", "Code", "Name", 50, false, false, SQLDataType.INT, menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GUI_CONFIGURATION.caption)) {
                new GUIConfig(menuPermission, swiMain);
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
//                        new GUIConfig(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FILE_TYPE_MASTER.caption)) {
                new FileTypeMaster(menuPermission, swiMain);
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
//                        new FileTypeMaster(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_TAX_STRUCTURE.caption)) {
                new ItemTaxStructure(menuPermission, swiMain);
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
//                        new ItemTaxStructure(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MENU_CODIFICATION_MASTER.caption)) {
                new MenuCodification(menuPermission, swiMain);
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
//                        new MenuCodification(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OSR_EXPORT.caption)) {
                new ExportProcedure(menuPermission, swiMain);
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
//                        new ExportProcedure(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OSR_IMPORT.caption)) {
                new ImportProcedure(menuPermission, swiMain);
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
//                        new ImportProcedure(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else //==================== Reports =================================
            if (menuText.trim().equalsIgnoreCase(pmMenu.USER_PERMISSION_REPORT.caption)) {
                new RptUserPermission(swiMain);
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
//                        new RptUserPermission(swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GST_STRUCTURE.caption)) {
                new GstStructure(menuPermission, swiMain);
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
//                        new GstStructure(menuPermission, swiMain);
//                        ppCallBackWait.hide(true);
//                    }
//                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DEPARTMENT_WISE_USER_REPORT.caption)) {
                new RptDepartmentWiseUserReport(swiMain);
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
//                        new RptDepartmentWiseUserReport(swiMain);
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
