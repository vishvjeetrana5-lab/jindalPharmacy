package aarogya.inventory.self.client;

import aarogya.inventory.workBillDirect.client.WorkBillDirect;
import aarogya.inventory.divisionMaster.client.DivisionMaster;
import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.enquiry.client.Enquiry;
import aarogya.inventory.enquiryApproval.client.EnquiryApproval;
import aarogya.inventory.gateIn.client.GateIn;
import aarogya.inventory.gateOut.client.GateOut;
import aarogya.inventory.grn.client.Grn;
import aarogya.inventory.grnApproval.client.GrnApproval;
import aarogya.inventory.indentApproval.client.IndentApproval;
import aarogya.inventory.itemTransferApproval.client.ItemTransferApproval;
import aarogya.inventory.indentClose.client.IndentClose;
import aarogya.inventory.indent.client.Indent;
import aarogya.inventory.itemCategoryMaster.client.ItemCategoryMaster;
import aarogya.inventory.itemDepartmentInfo.client.ItemDepartmentInfo;
import aarogya.inventory.itemGroupMaster.client.ItemGroupMaster;
import aarogya.inventory.projectMaster.client.ProjectMaster;
import aarogya.inventory.itemMakeMaster.client.ItemMakeMaster;
import aarogya.inventory.itemMaster.client.ItemMaster;
import aarogya.inventory.purchaseBill.client.PurchaseBill;
import aarogya.inventory.purchaseBillApproval.client.PurchaseBillApproval;
import aarogya.inventory.purchaseBillDirect.client.PurchaseBillDirect;
import aarogya.inventory.purchaseOrder.client.PurchaseOrder;
import aarogya.inventory.purchaseOrderApproval.client.PurchaseOrderApproval;
import aarogya.inventory.purchaseOrderClose.client.PurchaseOrderClose;
import aarogya.inventory.purchaseReturn.client.PurchaseReturn;
import aarogya.inventory.purchaseReturnApproval.client.PurchaseReturnApproval;
import aarogya.inventory.quotation.client.Quotation;
import aarogya.inventory.reports.client.RptEnquiryReport;
import aarogya.inventory.reports.client.RptGrnRegister;
import aarogya.inventory.reports.client.RptIndent;
import aarogya.inventory.reports.client.RptPurchaseOrderReport;
import aarogya.inventory.reports.client.RptPurchaseRegister;
import aarogya.inventory.reports.client.RptQuotation;
import aarogya.inventory.unitMaster.client.UnitMaster;
import aarogya.inventory.itemIssue.client.ItemIssue;
import aarogya.inventory.itemOpening.client.ItemOpening;
import aarogya.inventory.itemReceive.client.ItemReceive;
import aarogya.inventory.itemTransfer.client.ItemTransfer;
import aarogya.inventory.wipToStockConversion.client.WipToStockConversion;
import aarogya.inventory.manufacturerMaster.client.ManufacturerMaster;
import aarogya.inventory.overHeadItemMaster.client.OverHeadItemMaster;
import aarogya.inventory.physicalStock.client.PhysicalStock;
import aarogya.inventory.physicalStockClearance.client.PhysicalStockClearance;
import aarogya.inventory.physicalStockPosting.client.PhysicalStockPosting;
import aarogya.inventory.reports.client.RptExpiryRegister;
import aarogya.inventory.reports.client.RptGSTPurchaseRegister;
import aarogya.inventory.reports.client.RptItemIssueRegister;
import aarogya.inventory.reports.client.RptItemOpeningRegister;
import aarogya.inventory.reports.client.RptItemReceiveRegister;
import aarogya.inventory.reports.client.RptItemTransferRegister;
import aarogya.inventory.reports.client.RptNonMovingItemRegister;
import aarogya.inventory.reports.client.RptPhysicalStockBeforePosting;
import aarogya.inventory.reports.client.RptPurchaseReturnRegister;
import aarogya.inventory.reports.client.RptRequistionRegister;
import aarogya.inventory.reports.client.RptStockLedger;
import aarogya.inventory.reports.client.RptStockSummary;
import aarogya.inventory.requisition.client.Requisition;
import aarogya.inventory.requisitionApproval.client.RequisitionApproval;
import aarogya.inventory.requisitionClose.client.RequisitionClose;
import aarogya.inventory.stockBatch.client.StockBatch;
import aarogya.inventory.purchaseOrderDisplay.client.PurchaseOrderDisplay;
import aarogya.inventory.stockLedger.client.StockLedger;
import aarogya.inventory.stockSummary.client.StockSummary;
import aarogya.inventory.supplierMaster.client.SupplierMaster;
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
import dataman.dmToolkit.singleLineMaster.client.SingleLineMaster;
import aarogya.inventory.workOrder.client.WorkOrder;
import aarogya.inventory.gstServiceMaster.client.GstServiceMaster;
import aarogya.inventory.pendingPODisplay.client.PendingPO_Display;
import aarogya.inventory.reports.client.RptGatePassOut;
import aarogya.inventory.reports.client.RptPendingGrnAcRegister;
import aarogya.inventory.visitorGatePass.client.VisitorGatePass;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import java.util.HashMap;
import java.util.List;
import aarogya.projectMenu.self.shared.pmPharmacy;

import aarogya.inventory.projectIndicator.client.ProjectIndicator;
import aarogya.inventory.reports.client.RptPOReleaseAmountWithCD;
import aarogya.inventory.reports.client.RptPurchaseBillValueRegister;
import aarogya.inventory.reports.client.RptStockValuation;

public class Inventory implements IsWidget {

    private pmInventory pmMenu = new pmInventory();
    private pmPharmacy pmPhMenu = new pmPharmacy();
    private DmMenuItem.InitializeWidgetInvoker iwiTemp;
    private DmMenuBar menu;

    public Inventory(final SetWidgetInvoker swiMain) {
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

            if (menuText.trim().equalsIgnoreCase(pmMenu.MANUFACTURER_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ManufacturerMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.DIVISION_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new DivisionMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SUPPLIER_MASTER.caption)) {

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
                        if (!paramHashMapTemp.containsKey("module")) {
                            paramHashMapTemp.put("module", pmMenu.moduleName);
                        }
                        new SupplierMaster(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_SPECIALITY_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Speciality Master", "itemSpecialityMast", "Item Speciality", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MATERIAL_TYPE_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Material Type Master", "materialTypeMast", "Material Type", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_MODEL_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Item Model Master", "itemModelMast", "Model Name", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_STRENGTH_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster("Item Strength Master", "itemStrengthMast", "Strength Name", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.MATERIAL_CATEGORY_MASTER.caption)) {
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
                        if (!paramHashMapTemp.containsKey("module")) {
                            paramHashMapTemp.put("module", pmMenu.moduleName);
                        }
                        swiMain.setWidget(new pmPharmacy().moduleName, menuText, paramHashMapTemp, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_COLOUR_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster(pmMenu.ITEM_COLOUR_MASTER.caption, "itemColourMast", "Item Colour", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_SIZE_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster(pmMenu.ITEM_SIZE_MASTER.caption, "itemSizeMast", "Item Size", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.IMPURITY_MASTER.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new SingleLineMaster(pmMenu.IMPURITY_MASTER.caption, "impurityMast", "Impurity", "code", "name", 50, false, false, DmEnum.SQLDataType.INT, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_BILL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseBill(menuPermission, paramHashMap, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_BILL_DIRECT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseBillDirect(menuPermission, paramHashMap, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WORK_BILL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WorkBillDirect(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_RETURN.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseReturn(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GRN.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Grn(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_ORDER_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseOrderApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_ORDER_CLOSE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseOrderClose(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.UNIT_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new UnitMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_MAKE_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemMakeMaster(menuPermission, swiMain, STEnum.ITEM_MAKE_ENTRY_TYPE.MAKE.getId());
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_MARKETING_COMPANY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemMakeMaster(menuPermission, swiMain, STEnum.ITEM_MAKE_ENTRY_TYPE.MARKETING.getId());
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_CATEGORY_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemCategoryMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_GROUP_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemGroupMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROJECT_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProjectMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.OVERHEAD_ITEM_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new OverHeadItemMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.SERVICE_OVERHEAD_MASTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GstServiceMaster(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDENT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Indent(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENQUIRY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Enquiry(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.QUOTATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Quotation(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_ORDER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseOrder(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WORK_ORDER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WorkOrder(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDENT_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new IndentApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_TRANSFER_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemTransferApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDENT_CLOSE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new IndentClose(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENQUIRY_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new EnquiryApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.INDENT_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptIndent(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ENQUIRY_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptEnquiryReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } /*==================================== Report ===================================================*/ else if (menuText.trim().equalsIgnoreCase(pmMenu.PO_RELEASE_AMOUNT_WITH_CD.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPOReleaseAmountWithCD(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PB_Value_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPurchaseBillValueRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.QUOTATION_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptQuotation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_ORDER_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPurchaseOrderReport(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GRN_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptGrnRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPurchaseRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_RETURN_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPurchaseReturnRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_EXPIRY_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptExpiryRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GRN_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GrnApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_BILL_APPROVAL.caption)) {
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
                        new PurchaseBillApproval(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_BILL_RETURN_APPROVAL.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PurchaseReturnApproval(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.REQUISITION.caption)) {

                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new Requisition(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.REQUISITION_APPROVAL.caption)) {
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

                        if (!paramHashMapTemp.containsKey("autoFill")) {
                            paramHashMapTemp.put("autoFill", true);
                        }

                        new RequisitionApproval(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.REQUISITION_CLOSE.caption)) {
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

                        if (!paramHashMapTemp.containsKey("autoFill")) {
                            paramHashMapTemp.put("autoFill", true);
                        }

                        new RequisitionClose(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_TRANSFER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemTransfer(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_OPENING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemOpening(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_RECEIVE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemReceive(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.WIP_TO_STOCK_CONVERSION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new WipToStockConversion(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_ISSUE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemIssue(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STOCK_SUMMARY_DISPLAY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new StockSummary(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STOCK_LEDGER_DISPLAY.caption)) {
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
                            paramHashMapTemp.put("widgetName", menuText);
                        }

                        new StockLedger(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STOCK_BATCH_DISPLAY.caption)) {
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
                            paramHashMapTemp.put("widgetName", menuText);
                        }

                        new StockBatch(menuPermission, paramHashMapTemp, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PURCHASE_ORDER_DISPLAY.caption)) {
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
                            paramHashMapTemp.put("widgetName", menuText);
                        }

                        if (!paramHashMapTemp.containsKey("flagOpenAsPopup")) {
                            paramHashMapTemp.put("flagOpenAsPopup", false);
                        }

                        new PurchaseOrderDisplay(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.REQUISITION_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptRequistionRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_OPENING_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptItemOpeningRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_RECEIVE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptItemReceiveRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_ISSUE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptItemIssueRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_TRANSFER_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptItemTransferRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STOCK_LEDGER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptStockLedger(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.STOCK_SUMMARY.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptStockSummary(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHYSICAL_STOCK_BEFORE_POSTING_REPROT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPhysicalStockBeforePosting(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHYSICAL_STOCK.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PhysicalStock(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHYSICAL_STOCK_POSTING.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PhysicalStockPosting(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PHYSICAL_STOCK_CLEARANCE.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new PhysicalStockClearance(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_DEPARTMENT_INFO.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ItemDepartmentInfo(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GST_PURCHASE_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptGSTPurchaseRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.NON_MOVING_ITEM_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptNonMovingItemRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } //====================
            else if (menuText.trim().equalsIgnoreCase(pmMenu.GATE_PASS_OUT.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GateOut(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.VISITOR_GATE_PASS.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new VisitorGatePass(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GATE_PASS_OUT_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptGatePassOut(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });

            } else if (menuText.trim().equalsIgnoreCase(pmMenu.GATE_PASS_IN.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new GateIn(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PENDING_GRN_CONTROL_AC_REGISTER.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptPendingGrnAcRegister(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROJECT_INDICATOR.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new ProjectIndicator(menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PROCUREMENT_PLANNER_DISPLAY.caption)) {
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
                        if (!paramHashMapTemp.containsKey("module")) {
                            paramHashMapTemp.put("module", pmMenu.moduleName);
                        }
                        swiMain.setWidget(new pmPharmacy().moduleName, menuText, paramHashMapTemp, menuPermission, swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.ITEM_STOCK_VALUATION.caption)) {
                GWT.runAsync(new RunAsyncCallback() {

                    @Override
                    public void onFailure(Throwable reason) {
                        ppCallBackWait.hide(true);
                        new DmAlertMessageBox(reason.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess() {
                        new RptStockValuation(swiMain);
                        ppCallBackWait.hide(true);
                    }
                });
            } else if (menuText.trim().equalsIgnoreCase(pmMenu.PENDING_PURCHASE_ORDER_DISPLAY.caption)) {
                new PendingPO_Display(menuPermission, swiMain);
                ppCallBackWait.hide(true);
            } //==========================
            else {
                ppCallBackWait.hide(true);
            }
        } catch (Exception e) {
            ppCallBackWait.hide(true);
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }
}
