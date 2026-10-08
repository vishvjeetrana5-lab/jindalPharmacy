package aarogya.dashBoard.self.client;

import aarogya.dashBoard.advancesPendingForBillSettlement.client.AdvancesPendingForBillSettlement;
import aarogya.dashBoard.billDueNotPaid.client.BillDueNotPaid;
import aarogya.dashBoard.billPendingForApproval.client.BillPendingForApproval;
import aarogya.dashBoard.billPendingForGRN.client.BillPendingForGRN;
import aarogya.dashBoard.dataModificationLog.client.DataModificationLog;
import aarogya.dashBoard.fastMovingMedicine.client.FastMovingMedicine;
import aarogya.dashBoard.inventoryIndentPendingForApproval.client.InventoryIndentPendingForApproval;
import aarogya.dashBoard.inventoryPendingIndent.client.InventoryPendingIndent;
import aarogya.dashBoard.inventoryPendingPO.client.InventoryPendingPO;
import aarogya.dashBoard.nearExpiryMedicine.client.NearExpiryMedicine;
import aarogya.dashBoard.nonSoldMedicine.client.NonSoldMedicine;
import aarogya.dashBoard.requisitionInventory.client.RequisitionInventory;
import aarogya.dashBoard.requisitionPharmacy.client.RequisitionPharmacy;
import aarogya.dashBoard.totalCashCollection.client.TotalCashCollection;
import aarogya.dashBoard.totalRefundForDate.client.TotalRefundForDate;
import aarogya.dashBoard.walkThrough.client.WalkThrough;
import aarogya.dashBoard.whCollectionDashBoard.client.WHCollectionDashBoard;
import aarogya.projectMenu.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import dataman.dmToolkit.dmMenuBar.client.DmMenuBar;
import dataman.dmToolkit.dmMenuItem.client.DmMenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import java.util.List;

public class DashBoard implements IsWidget {

    private final pmDashBoard pmMenu = new pmDashBoard();
    private final DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private final DmMenuBar menu;

    public DashBoard(final SetWidgetInvoker swiMain) {
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
            }  else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_REQUISITION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RequisitionInventory(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MEDICINE_REQUISITION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RequisitionPharmacy(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WALK_THROUGH.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WalkThrough(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DATA_MODIFICATION_LOG.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DataModificationLog(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } //===========================================
            //===========================================
            //===========================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.INVENTORY_PENDING_PO.caption)) {
                new InventoryPendingPO(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PENDING_INDENT_FOR_APPROVAL.caption)) {
                new InventoryIndentPendingForApproval(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } //===========================================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.PENDING_INDENT.caption)) {
                new InventoryPendingIndent(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NEAR_EXPIRY_MEDICINE.caption)) {
                new NearExpiryMedicine(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NON_SOLD_MEDICINE.caption)) {
                new NonSoldMedicine(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.FAST_MOVING_MEDICINE.caption)) {
                new FastMovingMedicine(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } //
            else if (menuText.trim().equalsIgnoreCase(pmMenu.TOTAL_CASH_COLLECTION.caption)) {
                new TotalCashCollection(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.TOTAL_REFUND_FOR_DATE.caption)) {
                new TotalRefundForDate(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            }  else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_PENDING_FOR_GRN.caption)) {
                new BillPendingForGRN(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_PENDING_FOR_APPROVAL.caption)) {
                new BillPendingForApproval(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.BILL_DUE_NOT_PAID.caption)) {
                new BillDueNotPaid(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ADV_PENDING_FOR_SETTLEMENT.caption)) {
                new AdvancesPendingForBillSettlement(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            }else if (menuText.trim().equalsIgnoreCase(pmMenu.WHOLE_SALE_COLLECTION_DASHBOARD.caption)) {
                new WHCollectionDashBoard(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } else {
                ppCallBackWait.hide(true);
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            Window.alert(e.getMessage());
        }
    }
}
